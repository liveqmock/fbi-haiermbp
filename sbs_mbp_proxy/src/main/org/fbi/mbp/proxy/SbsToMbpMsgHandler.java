package org.fbi.mbp.proxy;

import org.fbi.xplay.ChannelContext;
import org.fbi.xplay.ChannelHandler;
import org.fbi.xplay.SocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhanrui on 2014/9/23.
 * 报文转换
 */
public  class SbsToMbpMsgHandler implements ChannelHandler {
    //现在MBP的地址及端口
    private final String remoteHost = ProjectConfigManager.getInstance().getStringProperty("remote_server_ip");
    private final int remotePort = ProjectConfigManager.getInstance().getIntProperty("remote_server_port");

    private int timeout = ProjectConfigManager.getInstance().getIntProperty("remote_server_timeout"); //默认超时时间：ms  连接超时与读超时统一
    private int warningtime = ProjectConfigManager.getInstance().getIntProperty("remote_server_txn_warning_time"); //长交易时间预警阈值：ms

    private static int ROUTE_HEADER_LEN = 32 + 16 + 16;   //路由header 长度
    private static int MSG_LENGTH_FIELD_LEN = 10;  //表示业务交易整包长度的字段的长度
    private static int MSG_HEADER_LEN = 16 + 6 + 3;   //业务交易包header 长度

    private ConcurrentLinkedQueue<String> taskLogQueue;
    private ConcurrentLinkedQueue<String> warningTaskLogQueue;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public SbsToMbpMsgHandler(ConcurrentLinkedQueue<String> taskLogQueue,
                              ConcurrentLinkedQueue<String> warningTaskLogQueue) {
        this.taskLogQueue = taskLogQueue;
        this.warningTaskLogQueue = warningTaskLogQueue;
    }

    //短链接 处理完成后 由客户端close connection
    @Override
    public void onRead(ChannelContext ctx) throws IOException {
        Socket connection = ctx.connection();
        SocketAddress clientAddress = connection.getRemoteSocketAddress();
        logger.debug("Client IP: " + clientAddress);

        try {
            InputStream in = connection.getInputStream();
            OutputStream out = connection.getOutputStream();

            //处理 64字节 route header
            byte[] inRouteHeaderBuf = new byte[ROUTE_HEADER_LEN];
            int readNum = in.read(inRouteHeaderBuf);
            if (readNum == -1) {
                throw new RuntimeException("服务器连接已关闭!");
            }
            if (readNum < ROUTE_HEADER_LEN) {
                throw new RuntimeException("读取报文头（ROUTE HEADER）长度部分错误...");
            }

            Calendar start = Calendar.getInstance();
            RouteHeader routeHeader = new RouteHeader(new String(inRouteHeaderBuf));
            logger.info("Client request route header:" + routeHeader.toString());

            //TODO 其他银行
            if (!"105".equals(routeHeader.getBankCode())) {
                throw new RuntimeException("目前只能处理建行交易！");
            }

            //处理 10字节 msg length field
            byte[] inMsgLenBuf = new byte[MSG_LENGTH_FIELD_LEN];
            readNum = in.read(inMsgLenBuf);
            if (readNum == -1) {
                throw new RuntimeException("服务器连接已关闭!");
            }
            if (readNum < MSG_LENGTH_FIELD_LEN) {
                throw new RuntimeException("读取报文头长度部分错误...");
            }

            logger.info("Txn message length:" + new String(inMsgLenBuf));
            int msgLen = Integer.parseInt(new String(inMsgLenBuf));

            //处理 业务交易报文体
            byte[] inMsgBuf = new byte[msgLen];
            readNum = in.read(inMsgBuf);   //阻塞读
            if (readNum != msgLen) {
                throw new RuntimeException("报文长度错误,应为:[" + msgLen + "], 实际获取长度:[" + readNum + "]");
            }

            //byte[] inHeadBuf = new byte[MSG_HEADER_LEN];
            //System.arraycopy(inMsgBuf, 0, inHeadBuf, 0, inHeadBuf.length);
            //MsgHeader msgHeader = new MsgHeader(new String(inHeadBuf));

            MDC.put("txnCode", routeHeader.getTxnCode());

            String inBodyData = new String(inMsgBuf, "GBK");
            logger.debug("Client request body:" + inBodyData);

            //proxy
            handleProxy(SocketUtils.bytesMerger(inRouteHeaderBuf, SocketUtils.bytesMerger(inMsgLenBuf, inMsgBuf)), out);

            Calendar end = Calendar.getInstance();
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long elapse = end.getTimeInMillis() - start.getTimeInMillis();
            String monitorLog = routeHeader.getTxnCode() + "|" + df.format(start.getTime()) + "|" + df.format(end.getTime()) + "|" + elapse;

            taskLogQueue.add(monitorLog);
            if (elapse >= warningtime) {
                warningTaskLogQueue.add(monitorLog);
            }

            logger.info("交易[" + routeHeader.getTxnCode() + "] 执行时间:" + elapse + "ms.");
        } finally {
            try {
                //connection.close();
            } catch (Exception e) {
                //
            }
            MDC.remove("txnCode");
        }
    }

    private void handleProxy(byte[] sendRemoteBuf, OutputStream sendClientOS) throws IOException {
        InetAddress addr = InetAddress.getByName(remoteHost);
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(addr, remotePort), timeout);
            socket.setSoTimeout(timeout);

            OutputStream os = socket.getOutputStream();
            os.write(sendRemoteBuf);
            os.flush();
            InputStream is = socket.getInputStream();

            byte[] inMsgLenBuf = new byte[MSG_LENGTH_FIELD_LEN];
            int readNum = is.read(inMsgLenBuf);
            if (readNum == -1) {
                throw new RuntimeException("服务器连接已关闭!");
            }
            if (readNum < MSG_LENGTH_FIELD_LEN) {
                throw new RuntimeException("读取报文头长度部分错误...");
            }

            int msgLen = Integer.parseInt(new String(inMsgLenBuf));
            byte[] inMsgBuf = new byte[msgLen];

            readNum = is.read(inMsgBuf);   //阻塞读
            if (readNum != msgLen) {
                throw new RuntimeException("报文长度错误,应为:[" + msgLen + "], 实际获取长度:[" + readNum + "]");
            }

            //byte[] inHeadBuf = new byte[MSG_HEADER_LEN];
            //System.arraycopy(inMsgBuf, 0, inHeadBuf, 0, inHeadBuf.length);
            //MsgHeader msgHeader = new MsgHeader(new String(inHeadBuf));
            //logger.debug("Remote server response header:" + msgHeader.toString());

            String inBodyData = new String(inMsgBuf, "GBK");
            logger.debug("Remote server response body:" + inBodyData);

            byte[] sendClientBuf = SocketUtils.bytesMerger(inMsgLenBuf, inMsgBuf);
            sendClientOS.write(sendClientBuf, 0, sendClientBuf.length);
            sendClientOS.flush();
        } finally {
            try {
                //主动关闭连接
                socket.close();
            } catch (IOException e) {
                //
            }
        }
    }
}

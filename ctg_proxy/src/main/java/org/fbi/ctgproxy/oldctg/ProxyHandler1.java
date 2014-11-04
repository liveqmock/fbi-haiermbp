package org.fbi.ctgproxy.oldctg;

import org.fbi.ctgproxy.CtgRequest;
import org.fbi.ctgproxy.ProjectConfigManager;
import org.fbi.xplay.ChannelContext;
import org.fbi.xplay.ChannelHandler;
import org.fbi.xplay.SocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhanrui on 2014/9/23.
 * 业务处理主进程
 * SBS-MBP报文协议   64字节通讯报文头 + 10字节长度（通讯报文体长度） +  通讯报文体
 * 64字节通讯报文头 = 32字节交易号 + 16字节客户端名称 + 16字节银行代码
 * 例：{txnCode='Transact', clientName='SBS', bankCode='105'}
 */
public class ProxyHandler1 implements ChannelHandler {
    private final String projectRootDir = ProjectConfigManager.getInstance().getStringProperty("prj_root_dir");

//    private final String remoteHost = ProjectConfigManager.getInstance().getStringProperty("remote_server_ip");
//    private final int remotePort = ProjectConfigManager.getInstance().getIntProperty("remote_server_port");
    private final String remoteHost = "10.143.20.130";
    private final int remotePort = 2006;

    private int timeout = ProjectConfigManager.getInstance().getIntProperty("remote_server_timeout"); //默认超时时间：ms  连接超时与读超时统一
    private int warningtime = ProjectConfigManager.getInstance().getIntProperty("remote_server_txn_warning_time"); //长交易时间预警阈值：ms

    private static int MSG_LENGTH_FIELD_LEN = 10;  //表示业务交易整包长度的字段的长度

    HashMap<String, String> ccbRouterConfig = new HashMap<>();
    List<String> localTxncodes = new ArrayList<>();

    private ConcurrentLinkedQueue<String> taskLogQueue;
    private ConcurrentLinkedQueue<String> warningTaskLogQueue;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public ProxyHandler1(ConcurrentLinkedQueue<String> taskLogQueue,
                         ConcurrentLinkedQueue<String> warningTaskLogQueue) {
        this.taskLogQueue = taskLogQueue;
        this.warningTaskLogQueue = warningTaskLogQueue;

        initLocalTxncodeList();
    }

    //短链接 处理完成后 由客户端close connection
    @Override
    public void onRead(ChannelContext ctx) throws IOException {
        Socket clntConnection = ctx.connection();
        SocketAddress clientAddress = clntConnection.getRemoteSocketAddress();
        logger.debug("Client IP: " + clientAddress);

        try {
            DataInputStream osFromClient = new DataInputStream(clntConnection.getInputStream());
            DataOutputStream osToClient = new DataOutputStream(clntConnection.getOutputStream());

            //与CTG server建立连接
            InetAddress addr = InetAddress.getByName(remoteHost);
            Socket ctgServerConnection = new Socket();
            ctgServerConnection.connect(new InetSocketAddress(addr, remotePort), timeout);
            ctgServerConnection.setSoTimeout(timeout);
            DataOutputStream osToTps = new DataOutputStream(ctgServerConnection.getOutputStream());
            DataInputStream osFromTps = new DataInputStream(ctgServerConnection.getInputStream());

            for (;;) {
                CtgRequest ctgRequest = new CtgRequest();
                ctgRequest.readObject(osFromClient);

                //转发
                ctgRequest.writeObject(osToTps);
                ctgRequest.writeObject(osToTps);

                //收tps响应
                CtgRequest tpsCtgRequest = new CtgRequest();
                tpsCtgRequest.readObject(osFromTps);

                //返回客户端
                tpsCtgRequest.writeObject(osToClient);
                tpsCtgRequest.writeObject(osToClient);
            }


/*
            //处理报文ROOT部分
            int magicNum = osFromClient.readInt();
            if (magicNum != 0x47617465) {
                logger.error("非CTG请求报文.");
                //TODO rtn
                return;
            }

            int flowVersion = osFromClient.readInt();
            int flowType = osFromClient.readInt();
            int msgId = osFromClient.readInt();
            int gatewayRc = osFromClient.readInt();
            String strRequestType = "";

            int paddedStrLen = 0;
            if (flowVersion == 0x10100) {
                paddedStrLen = 4;
            } else {
                paddedStrLen = osFromClient.readInt();
            }
            byte paddedStrBuf[] = new byte[paddedStrLen];
            osFromClient.readFully(paddedStrBuf, 0, paddedStrLen);
            strRequestType = new String(paddedStrBuf, "ASCII");

            boolean isSecurity = false;
            if (flowVersion >= 0x200000) {
                isSecurity = osFromClient.readBoolean();
            }

            int eciDataLen = osFromClient.readInt();
            byte[] eciDataBuf = new byte[eciDataLen];
            int readNum = osFromClient.read(eciDataBuf);
            if (readNum == -1) {
                throw new RuntimeException("服务器连接已关闭!");
            }
            if (readNum < eciDataLen) {
                throw new RuntimeException("读取ECIDATA错误...");
            }

            //处理ECIDATA部分

*/

/*
            String txnCode = routeHeader.getTxnCode();
            MDC.put("txnCode", txnCode);

            String inBodyData = new String(inMsgBuf, "GBK");
            logger.debug("Client request body:" + inBodyData);


                byte[] requestBuffer = SocketUtils.bytesMerger(inRouteHeaderBuf, SocketUtils.bytesMerger(inMsgLenBuf, inMsgBuf));
                handleProxy(requestBuffer, osToClient);

            Calendar end = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long elapse = end.getTimeInMillis() - start.getTimeInMillis();
            String monitorLog = txnCode + "|" + df.format(start.getTime()) + "|" + df.format(end.getTime()) + "|" + elapse;

            taskLogQueue.add(monitorLog);
            if (elapse >= warningtime) {
                warningTaskLogQueue.add(monitorLog);
            }
            logger.info("交易[" + txnCode + "] 执行时间:" + elapse + "ms.");
*/
        } finally {
            try {
                //clntConnection.setSoLinger(true,5);

//                Thread.sleep(200);//等待客户端首先关闭连接
//                clntConnection.close();
            } catch (Exception e) {
                logger.debug("连接关闭失败.可忽略.", e);
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
                logger.debug("连接关闭失败.可忽略.", e);
            }
        }
    }

    //======================
    //初始化本地交易处理列表
    private void initLocalTxncodeList() {
        String localTxncode = ProjectConfigManager.getInstance().getStringProperty("local_txncode");
        this.localTxncodes = Arrays.asList(localTxncode.split(","));
    }

    private boolean isLocalTxncode(String txncode) {
        for (String localTxncode : localTxncodes) {
            if (txncode.equalsIgnoreCase(localTxncode)) {
                return true;
            }
        }
        return false;
    }


    /**
     * @param out
     * @param requestRouteHeadBuffer SBS-MBP之间64字节通讯报文头
     * @param requestBuffer          SBS-MBP之间通讯报文体
     * @return
     */
/*    private TxnContext initTxnContext(OutputStream out, byte[] requestRouteHeadBuffer, byte[] requestBuffer) {
        TxnContext txnContext = new TxnContext();
        txnContext.setProjectRootDir(this.projectRootDir);
        txnContext.setCcbRouterConfig(ccbRouterConfig);
        txnContext.setRequestRouteHeadBuffer(requestRouteHeadBuffer);
        txnContext.setRequestBuffer(requestBuffer);
        txnContext.setClientReponseOutputStream(out);
        return txnContext;
    }*/

}

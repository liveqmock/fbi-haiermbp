package org.fbi.mbp.proxy.processor;

import org.fbi.mbp.proxy.ProjectConfigManager;
import org.fbi.xplay.SocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by zhanrui on 2014/10/11.
 * 纯 proxy 处理
 */
public class AbstractProxyProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //现在MBP的地址及端口 proxy的目的地
    private final String remoteHost = ProjectConfigManager.getInstance().getStringProperty("remote_server_ip");
    private final int remotePort = ProjectConfigManager.getInstance().getIntProperty("remote_server_port");

    private int timeout = ProjectConfigManager.getInstance().getIntProperty("remote_server_timeout"); //默认超时时间：ms  连接超时与读超时统一
    private int warningtime = ProjectConfigManager.getInstance().getIntProperty("remote_server_txn_warning_time"); //长交易时间预警阈值：ms

    private static int ROUTE_HEADER_LEN = 32 + 16 + 16;   //路由header 长度
    private static int MSG_LENGTH_FIELD_LEN = 10;  //表示业务交易整包长度的字段的长度
    private static int MSG_HEADER_LEN = 16 + 6 + 3;   //业务交易包header 长度


    protected void processProxy(byte[] sendRemoteBuf, OutputStream clientOsForSendBack) throws IOException {
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
            clientOsForSendBack.write(sendClientBuf, 0, sendClientBuf.length);
            clientOsForSendBack.flush();
        } finally {
            try {
                //主动关闭连接
                socket.close();
            } catch (IOException e) {
                logger.debug("连接关闭失败.可忽略.", e);
            }
        }
    }

}

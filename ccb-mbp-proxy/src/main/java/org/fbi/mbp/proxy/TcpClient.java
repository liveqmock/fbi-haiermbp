package org.fbi.mbp.proxy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * User: zhanrui
 * Date: 13-11-27
 */
public class TcpClient {
    private String ip;
    private int port;
    private int timeout = 30000; //默认超时时间：ms  连接超时与读超时统一

    private  static int MSG_HEADER_LENGTH = 60 + 51;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public TcpClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * @throws Exception 其中：SocketTimeoutException为超时异常
     */
    public byte[] call(byte[] sendbuf) throws Exception {
        byte[] recvbuf = null;

        InetAddress addr = InetAddress.getByName(ip);
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(addr, port), timeout);
            socket.setSoTimeout(timeout);

            OutputStream os = socket.getOutputStream();
            os.write(sendbuf);
            os.flush();

            InputStream is = socket.getInputStream();
            recvbuf = new byte[MSG_HEADER_LENGTH];
            int readNum = is.read(recvbuf);
            if (readNum == -1) {
                throw new RuntimeException("服务器连接已关闭!");
            }
            if (readNum < MSG_HEADER_LENGTH) {
                throw new RuntimeException("读取报文头长度部分错误...");
            }

            VipMsgHeader msgHeader = new VipMsgHeader(new String(recvbuf));
            int msgLen = Integer.parseInt(msgHeader.getMsgDataLen());

            logger.info("报文体长度:" + msgLen);
            recvbuf = new byte[msgLen];

            Thread.sleep(50);

            readNum = is.read(recvbuf);   //阻塞读
            if (readNum != msgLen) {
                throw new RuntimeException("报文长度错误,应为:[" + msgLen + "], 实际获取长度:[" + readNum + "]");
            }
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                //
            }
        }
        return recvbuf;
    }


    public static void main(String... argv) throws UnsupportedEncodingException {
        TcpClient mock = new TcpClient("127.0.0.1", 7500);

        VipMsgHeader msgHeader = new VipMsgHeader("1","2","4879","4","5","6","7","8","9","10","11");
//        String msgData = "<FileName>VIPC18.tmp</FileName>";
        String msgData = "<FileName>VIP538.tmp</FileName>";

        String msgDataLen =  "" + msgData.getBytes("GBK").length;
        msgHeader.setMsgDataLen(StringUtils.leftPad(msgDataLen, 9, "0"));

        byte[] recvbuf = new byte[0];
        try {
            recvbuf = mock.call((msgHeader.getHeaderString() + msgData).getBytes("GBK"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.printf("服务器返回：%s\n", new String(recvbuf, "GBK"));
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}

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
 * �� proxy ����
 */
public class AbstractProxyProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //����MBP�ĵ�ַ���˿� proxy��Ŀ�ĵ�
    private final String remoteHost = ProjectConfigManager.getInstance().getStringProperty("remote_server_ip");
    private final int remotePort = ProjectConfigManager.getInstance().getIntProperty("remote_server_port");

    private int timeout = ProjectConfigManager.getInstance().getIntProperty("remote_server_timeout"); //Ĭ�ϳ�ʱʱ�䣺ms  ���ӳ�ʱ�����ʱͳһ
    private int warningtime = ProjectConfigManager.getInstance().getIntProperty("remote_server_txn_warning_time"); //������ʱ��Ԥ����ֵ��ms

    private static int ROUTE_HEADER_LEN = 32 + 16 + 16;   //·��header ����
    private static int MSG_LENGTH_FIELD_LEN = 10;  //��ʾҵ�����������ȵ��ֶεĳ���
    private static int MSG_HEADER_LEN = 16 + 6 + 3;   //ҵ���װ�header ����


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
                throw new RuntimeException("�����������ѹر�!");
            }
            if (readNum < MSG_LENGTH_FIELD_LEN) {
                throw new RuntimeException("��ȡ����ͷ���Ȳ��ִ���...");
            }

            int msgLen = Integer.parseInt(new String(inMsgLenBuf));
            byte[] inMsgBuf = new byte[msgLen];

            readNum = is.read(inMsgBuf);   //������
            if (readNum != msgLen) {
                throw new RuntimeException("���ĳ��ȴ���,ӦΪ:[" + msgLen + "], ʵ�ʻ�ȡ����:[" + readNum + "]");
            }

            String inBodyData = new String(inMsgBuf, "GBK");
            logger.debug("Remote server response body:" + inBodyData);

            byte[] sendClientBuf = SocketUtils.bytesMerger(inMsgLenBuf, inMsgBuf);
            clientOsForSendBack.write(sendClientBuf, 0, sendClientBuf.length);
            clientOsForSendBack.flush();
        } finally {
            try {
                //�����ر�����
                socket.close();
            } catch (IOException e) {
                logger.debug("���ӹر�ʧ��.�ɺ���.", e);
            }
        }
    }

}

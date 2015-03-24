package org.fbi.mbp.proxy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by zhanrui on 2014/12/1.
 * ͨ��DEPƽ̨�Ķ��Žӿڷ���SMS
 */
public class SmsHelper {
    private static final Logger logger = LoggerFactory.getLogger(SmsHelper.class);

    private static int MSG_HEADER_LENGTH = 8;

    //txMsg: �绰����|��Ϣ����
    //����绰�����ԡ������ָ�
    public static void asyncSendSms(final String phones, final String msg) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    sendMsgToDep(phones + "|" + msg);
                } catch (IOException e) {
                    logger.error("���ŷ���ʧ��", e);
                }
            }
        };
        new Thread(task).start();
    }

    //txMsg: �绰����|��Ϣ����
    //����绰�����ԡ������ָ�
    public static String syncSendSms(final String phones, final String msg) throws IOException {
        String rtnMsg = "0000"; //����������
        try {
            rtnMsg = sendMsgToDep(phones + "|" + msg);
        } catch (IOException e) {
            rtnMsg = "9999"; //�����쳣
            logger.error("���ŷ���ʧ��", e);
        }
        return rtnMsg;
    }

    public static String sendMsgToDep(String txMsg) throws IOException {
        //InetAddress addr = InetAddress.getByName(PropertyManager.getProperty("haier.dep.server.ip"));
        InetAddress addr = InetAddress.getByName(ProjectConfigManager.getInstance().getStringProperty("haier.dep.server.ip"));
        Socket socket = new Socket();
        int timeout_ms = 30 * 1000;
        try {
            //int port = PropertyManager.getIntProperty("haier.dep.server.port.sms");
            int port = ProjectConfigManager.getInstance().getIntProperty("haier.dep.server.port.sms");
            socket.connect(new InetSocketAddress(addr, port), timeout_ms);
            socket.setSoTimeout(timeout_ms);

            //�鱨��ͷ
            String smsTxnCode = "0011";
            txMsg = smsTxnCode + txMsg;
            String msgLen = StringUtils.rightPad("" + (txMsg.getBytes("GBK").length + MSG_HEADER_LENGTH), MSG_HEADER_LENGTH, " ");

            OutputStream os = socket.getOutputStream();
            os.write((msgLen + txMsg).getBytes("GBK"));
            os.flush();
            InputStream is = socket.getInputStream();

            byte[] inHeaderBuf = new byte[MSG_HEADER_LENGTH];
            int readNum = is.read(inHeaderBuf);
            if (readNum == -1) {
                throw new RuntimeException("�����������ѹر�!");
            }
            if (readNum < MSG_HEADER_LENGTH) {
                throw new RuntimeException("��ȡ����ͷ���Ȳ��ִ���...");
            }

            int bodyLen = Integer.parseInt((new String(inHeaderBuf).trim())) - MSG_HEADER_LENGTH;
            byte[] inBodyBuf = new byte[bodyLen];

            readNum = is.read(inBodyBuf);   //������
            if (readNum != bodyLen) {
                throw new RuntimeException("���ĳ��ȴ���,ӦΪ:[" + bodyLen + "], ʵ�ʻ�ȡ����:[" + readNum + "]");
            }

            return new String(inBodyBuf, "GBK");  //���ر���������
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                //
            }
        }
    }
}

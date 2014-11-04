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
 * ҵ����������
 * SBS-MBP����Э��   64�ֽ�ͨѶ����ͷ + 10�ֽڳ��ȣ�ͨѶ�����峤�ȣ� +  ͨѶ������
 * 64�ֽ�ͨѶ����ͷ = 32�ֽڽ��׺� + 16�ֽڿͻ������� + 16�ֽ����д���
 * ����{txnCode='Transact', clientName='SBS', bankCode='105'}
 */
public class ProxyHandler1 implements ChannelHandler {
    private final String projectRootDir = ProjectConfigManager.getInstance().getStringProperty("prj_root_dir");

//    private final String remoteHost = ProjectConfigManager.getInstance().getStringProperty("remote_server_ip");
//    private final int remotePort = ProjectConfigManager.getInstance().getIntProperty("remote_server_port");
    private final String remoteHost = "10.143.20.130";
    private final int remotePort = 2006;

    private int timeout = ProjectConfigManager.getInstance().getIntProperty("remote_server_timeout"); //Ĭ�ϳ�ʱʱ�䣺ms  ���ӳ�ʱ�����ʱͳһ
    private int warningtime = ProjectConfigManager.getInstance().getIntProperty("remote_server_txn_warning_time"); //������ʱ��Ԥ����ֵ��ms

    private static int MSG_LENGTH_FIELD_LEN = 10;  //��ʾҵ�����������ȵ��ֶεĳ���

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

    //������ ������ɺ� �ɿͻ���close connection
    @Override
    public void onRead(ChannelContext ctx) throws IOException {
        Socket clntConnection = ctx.connection();
        SocketAddress clientAddress = clntConnection.getRemoteSocketAddress();
        logger.debug("Client IP: " + clientAddress);

        try {
            DataInputStream osFromClient = new DataInputStream(clntConnection.getInputStream());
            DataOutputStream osToClient = new DataOutputStream(clntConnection.getOutputStream());

            //��CTG server��������
            InetAddress addr = InetAddress.getByName(remoteHost);
            Socket ctgServerConnection = new Socket();
            ctgServerConnection.connect(new InetSocketAddress(addr, remotePort), timeout);
            ctgServerConnection.setSoTimeout(timeout);
            DataOutputStream osToTps = new DataOutputStream(ctgServerConnection.getOutputStream());
            DataInputStream osFromTps = new DataInputStream(ctgServerConnection.getInputStream());

            for (;;) {
                CtgRequest ctgRequest = new CtgRequest();
                ctgRequest.readObject(osFromClient);

                //ת��
                ctgRequest.writeObject(osToTps);
                ctgRequest.writeObject(osToTps);

                //��tps��Ӧ
                CtgRequest tpsCtgRequest = new CtgRequest();
                tpsCtgRequest.readObject(osFromTps);

                //���ؿͻ���
                tpsCtgRequest.writeObject(osToClient);
                tpsCtgRequest.writeObject(osToClient);
            }


/*
            //������ROOT����
            int magicNum = osFromClient.readInt();
            if (magicNum != 0x47617465) {
                logger.error("��CTG������.");
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
                throw new RuntimeException("�����������ѹر�!");
            }
            if (readNum < eciDataLen) {
                throw new RuntimeException("��ȡECIDATA����...");
            }

            //����ECIDATA����

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
            logger.info("����[" + txnCode + "] ִ��ʱ��:" + elapse + "ms.");
*/
        } finally {
            try {
                //clntConnection.setSoLinger(true,5);

//                Thread.sleep(200);//�ȴ��ͻ������ȹر�����
//                clntConnection.close();
            } catch (Exception e) {
                logger.debug("���ӹر�ʧ��.�ɺ���.", e);
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
            sendClientOS.write(sendClientBuf, 0, sendClientBuf.length);
            sendClientOS.flush();
        } finally {
            try {
                //�����ر�����
                socket.close();
            } catch (IOException e) {
                logger.debug("���ӹر�ʧ��.�ɺ���.", e);
            }
        }
    }

    //======================
    //��ʼ�����ؽ��״����б�
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
     * @param requestRouteHeadBuffer SBS-MBP֮��64�ֽ�ͨѶ����ͷ
     * @param requestBuffer          SBS-MBP֮��ͨѶ������
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

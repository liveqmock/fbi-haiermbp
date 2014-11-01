package org.fbi.ctgproxy;

import org.fbi.xplay.ChannelContext;
import org.fbi.xplay.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhanrui on 2014/9/23.
 */
public class ProxyHandler implements ChannelHandler {
    private final String projectRootDir = ProjectConfigManager.getInstance().getStringProperty("prj_root_dir");

    //    private final String remoteHost = ProjectConfigManager.getInstance().getStringProperty("remote_server_ip");
//    private final int remotePort = ProjectConfigManager.getInstance().getIntProperty("remote_server_port");
    private final String remoteHost = "10.143.20.130";
    private final int remotePort = 2006;

    private int timeout = ProjectConfigManager.getInstance().getIntProperty("remote_server_timeout"); //Ĭ�ϳ�ʱʱ�䣺ms  ���ӳ�ʱ�����ʱͳһ
    private int warningtime = ProjectConfigManager.getInstance().getIntProperty("remote_server_txn_warning_time"); //������ʱ��Ԥ����ֵ��ms

    HashMap<String, String> ccbRouterConfig = new HashMap<>();
    List<String> localTxncodes = new ArrayList<>();

    private ConcurrentLinkedQueue<String> taskLogQueue;
    private ConcurrentLinkedQueue<String> warningTaskLogQueue;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public ProxyHandler(ConcurrentLinkedQueue<String> taskLogQueue,
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

        //CTG Server
        InetAddress addr = InetAddress.getByName(remoteHost);
        Socket sbsConnection = new Socket();
        sbsConnection.connect(new InetSocketAddress(addr, remotePort), timeout);
        sbsConnection.setSoTimeout(timeout);

        logger.debug("Client[ " + clientAddress + "]  CTG server[" + remoteHost + ":" + remotePort + "]");

        try {
            DataInputStream osFromClient = new DataInputStream(clntConnection.getInputStream());
            DataOutputStream osToClient = new DataOutputStream(clntConnection.getOutputStream());

            //��CTG server��������
            DataOutputStream osToTps = new DataOutputStream(sbsConnection.getOutputStream());
            DataInputStream osFromTps = new DataInputStream(sbsConnection.getInputStream());

            for (; ; ) {
                CtgRequest ctgRequest = new CtgRequest();
                ctgRequest.readObject(osFromClient);

                int flowType = ctgRequest.getiFlowType();
                if (flowType == 1) {
                    CtgEciRequest eciRequest = new CtgEciRequest();
                    eciRequest.readObject(new DataInputStream(new ByteArrayInputStream(ctgRequest.getEciArea())));
                    if (eciRequest.Call_Type == 1) {
                        //TODO �жϽ��׺� ���ն˺�
                        CtgSif txnHeader = new CtgSif(eciRequest.Commarea);
                        logger.debug("===" + txnHeader);
                        String txnCode = txnHeader.getTxnCode();
                        String termId = txnHeader.getTermId();

                        processCpsTxn(ctgRequest, osToClient);
                        return;
                    }

                }
                //ת��
                ctgRequest.writeRootObject(osToTps);
                //ctgRequest.writeObject(osToTps);

                //��tps��Ӧ
                CtgRequest sbsCtgRequest = new CtgRequest();
                sbsCtgRequest.readObject(osFromTps);

                //���ؿͻ���
                sbsCtgRequest.writeRootObject(osToClient);
                //sbsCtgRequest.writeObject(osToClient);
            }
        } catch (SocketException e) {
            logger.info("�����ѹر�", e.getMessage());
        } catch (Exception e) {
            logger.error("���״����쳣",e);
            //TODO
        } finally {
            try {
                //�����ر�server������
                sbsConnection.close();
            } catch (Exception e) {
                logger.debug("���ӹر�ʧ��.�ɺ���.", e);
            }
            MDC.remove("txnCode");
        }
    }

    private  void  processCpsTxn(CtgRequest ctgRequest, DataOutputStream osToClient) throws IOException {
        //cbs pre server
        InetAddress addrCps = InetAddress.getByName("127.0.0.1");
        Socket cpsConnection = new Socket();
        cpsConnection.connect(new InetSocketAddress(addrCps, 2007), timeout);
        cpsConnection.setSoTimeout(timeout);
        DataOutputStream osToCps = new DataOutputStream(cpsConnection.getOutputStream());
        DataInputStream osFromCps = new DataInputStream(cpsConnection.getInputStream());


        osToCps.writeInt(ctgRequest.EYECATCODE); //CTG���ױ�ʶ
        osToCps.write(ctgRequest.getEciArea());

        //���ձ���
        int msgLen = osFromCps.readInt();
        byte[] msgBuf = new byte[msgLen];
        osFromCps.readFully(msgBuf);

        ctgRequest.setiFlowType(3);
        ctgRequest.setEciArea(msgBuf);

        //���ؿͻ���
        ctgRequest.writeRootObject(osToClient);
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

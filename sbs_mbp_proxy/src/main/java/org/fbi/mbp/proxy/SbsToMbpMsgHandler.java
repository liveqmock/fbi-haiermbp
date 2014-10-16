package org.fbi.mbp.proxy;

import org.fbi.xplay.ChannelContext;
import org.fbi.xplay.ChannelHandler;
import org.fbi.xplay.SocketUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhanrui on 2014/9/23.
 * ҵ����������
 * SBS-MBP����Э��   64�ֽ�ͨѶ����ͷ + 10�ֽڳ��ȣ�ͨѶ�����峤�ȣ� +  ͨѶ������
 *                  64�ֽ�ͨѶ����ͷ = 32�ֽڽ��׺� + 16�ֽڿͻ������� + 16�ֽ����д���
 *                                 ����{txnCode='Transact', clientName='SBS', bankCode='105'}
 */
public  class SbsToMbpMsgHandler implements ChannelHandler {
    private final String projectRootDir = ProjectConfigManager.getInstance().getStringProperty("prj_root_dir");

    //����MBP�ĵ�ַ���˿� proxy��Ŀ�ĵ�
    private final String remoteHost = ProjectConfigManager.getInstance().getStringProperty("remote_server_ip");
    private final int remotePort = ProjectConfigManager.getInstance().getIntProperty("remote_server_port");

    private int timeout = ProjectConfigManager.getInstance().getIntProperty("remote_server_timeout"); //Ĭ�ϳ�ʱʱ�䣺ms  ���ӳ�ʱ�����ʱͳһ
    private int warningtime = ProjectConfigManager.getInstance().getIntProperty("remote_server_txn_warning_time"); //������ʱ��Ԥ����ֵ��ms

    private static int ROUTE_HEADER_LEN = 32 + 16 + 16;   //·��header ����
    private static int MSG_LENGTH_FIELD_LEN = 10;  //��ʾҵ�����������ȵ��ֶεĳ���
    private static int MSG_HEADER_LEN = 16 + 6 + 3;   //ҵ���װ�header ����

    HashMap<String,String> ccbRouterConfig = new HashMap<>();
    List<String> localTxncodes = new ArrayList<>();

    private ConcurrentLinkedQueue<String> taskLogQueue;
    private ConcurrentLinkedQueue<String> warningTaskLogQueue;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public SbsToMbpMsgHandler(ConcurrentLinkedQueue<String> taskLogQueue,
                              ConcurrentLinkedQueue<String> warningTaskLogQueue) {
        this.taskLogQueue = taskLogQueue;
        this.warningTaskLogQueue = warningTaskLogQueue;

        initCcbConfig();
        initLocalTxncodeList();
    }

    //������ ������ɺ� �ɿͻ���close connection
    @Override
    public void onRead(ChannelContext ctx) throws IOException {
        Socket connection = ctx.connection();
        SocketAddress clientAddress = connection.getRemoteSocketAddress();
        logger.debug("Client IP: " + clientAddress);

        try {
            InputStream in = connection.getInputStream();
            OutputStream out = connection.getOutputStream();

            //���� 64�ֽ� route header
            byte[] inRouteHeaderBuf = new byte[ROUTE_HEADER_LEN];
            int readNum = in.read(inRouteHeaderBuf);
            if (readNum == -1) {
                throw new RuntimeException("�����������ѹر�!");
            }
            if (readNum < ROUTE_HEADER_LEN) {
                throw new RuntimeException("��ȡ����ͷ��ROUTE HEADER�����Ȳ��ִ���...");
            }

            Calendar start = Calendar.getInstance();
            RouteHeader routeHeader = new RouteHeader(new String(inRouteHeaderBuf));
            logger.info("Client request route header:" + routeHeader.toString());

            //TODO ��������
            if (!"105".equals(routeHeader.getBankCode())) {
                throw new RuntimeException("Ŀǰֻ�ܴ����н��ף�");
            }

            //���� 10�ֽ� msg length field
            byte[] inMsgLenBuf = new byte[MSG_LENGTH_FIELD_LEN];
            readNum = in.read(inMsgLenBuf);
            if (readNum == -1) {
                throw new RuntimeException("�����������ѹر�!");
            }
            if (readNum < MSG_LENGTH_FIELD_LEN) {
                throw new RuntimeException("��ȡ����ͷ���Ȳ��ִ���...");
            }

            logger.info("Txn message length:" + new String(inMsgLenBuf));
            int msgLen = Integer.parseInt(new String(inMsgLenBuf));

            //���� ҵ���ױ�����
            byte[] inMsgBuf = new byte[msgLen];
            readNum = in.read(inMsgBuf);   //������
            if (readNum != msgLen) {
                throw new RuntimeException("���ĳ��ȴ���,ӦΪ:[" + msgLen + "], ʵ�ʻ�ȡ����:[" + readNum + "]");
            }

            String txnCode = routeHeader.getTxnCode();
            MDC.put("txnCode", txnCode);

            String inBodyData = new String(inMsgBuf, "GBK");
            logger.debug("Client request body:" + inBodyData);

            if (isLocalTxncode(txnCode)) {
                Class processType = Class.forName("org.fbi.mbp.proxy.processor." + txnCode + "Processor");
                TxnProcessor processor = (TxnProcessor) processType.newInstance();

                TxnContext txnContext = initTxnContext(out, inRouteHeaderBuf, inMsgBuf);
                try {
                    processor.process(txnContext);
                } catch (Exception e) {
                    //TODO
                    logger.error("���״���ʧ��.", e);
                }
            } else {
                byte[] requestBuffer = SocketUtils.bytesMerger(inRouteHeaderBuf, SocketUtils.bytesMerger(inMsgLenBuf, inMsgBuf));
                handleProxy(requestBuffer, out);
            }

            Calendar end = Calendar.getInstance();
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long elapse = end.getTimeInMillis() - start.getTimeInMillis();
            String monitorLog = txnCode + "|" + df.format(start.getTime()) + "|" + df.format(end.getTime()) + "|" + elapse;

            taskLogQueue.add(monitorLog);
            if (elapse >= warningtime) {
                warningTaskLogQueue.add(monitorLog);
            }
            logger.info("����[" + txnCode + "] ִ��ʱ��:" + elapse + "ms.");
        } catch (ClassNotFoundException e) {
            logger.error("Txn processor not found!", e);
            throw new RuntimeException(e); //TODO  catch thread exception
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Txn processor InstantiationException error!", e);
            throw new RuntimeException(e); //TODO  catch thread exception
        } finally {
            try {
                //connection.setSoLinger(true,5);
                Thread.sleep(200);//�ȴ��ͻ������ȹر�����
                connection.close();
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
    private void initLocalTxncodeList(){
        String localTxncode = ProjectConfigManager.getInstance().getStringProperty("local_txncode");
        this.localTxncodes = Arrays.asList(localTxncode.split(","));
    }

    private boolean isLocalTxncode(String txncode){
        for (String localTxncode : localTxncodes) {
            if (txncode.equalsIgnoreCase(localTxncode)) {
                return true;
            }
        }
        return false;
    }

    private void initCcbConfig() {

        String xmlfilename = projectRootDir + "/conf/SBS_105.bic";
        try {
            SAXBuilder b = new SAXBuilder();
            Document doc = b.build(new File(xmlfilename));
            Element rootElement = doc.getRootElement();
            List<Element> allChildren = rootElement.getChildren();
            for (Element child : allChildren) {
                ccbRouterConfig.put(child.getName(), child.getText());
            }

/*
            File file = new File(pathname);
            ccbRouterConfig.loadFromXML(new FileInputStream(file));
            logger.info("=====" + ccbRouterConfig.getProperty("version"));
*/
        } catch (Exception e) {
            throw new RuntimeException("�����ļ���ȡʧ��" + xmlfilename, e);
        }
    }

    /**
     *
     * @param out
     * @param requestRouteHeadBuffer   SBS-MBP֮��64�ֽ�ͨѶ����ͷ
     * @param requestBuffer SBS-MBP֮��ͨѶ������
     * @return
     */
    private TxnContext initTxnContext(OutputStream out,byte[] requestRouteHeadBuffer, byte[] requestBuffer) {
        TxnContext txnContext = new TxnContext();
        txnContext.setProjectRootDir(this.projectRootDir);
        txnContext.setCcbRouterConfig(ccbRouterConfig);
        txnContext.setRequestRouteHeadBuffer(requestRouteHeadBuffer);
        txnContext.setRequestBuffer(requestBuffer);
        txnContext.setClientReponseOutputStream(out);
        return txnContext;
    }
}

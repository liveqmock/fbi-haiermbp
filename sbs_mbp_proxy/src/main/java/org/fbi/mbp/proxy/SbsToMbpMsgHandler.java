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
 * 业务处理主进程
 * SBS-MBP报文协议   64字节通讯报文头 + 10字节长度（通讯报文体长度） +  通讯报文体
 *                  64字节通讯报文头 = 32字节交易号 + 16字节客户端名称 + 16字节银行代码
 *                                 例：{txnCode='Transact', clientName='SBS', bankCode='105'}
 */
public  class SbsToMbpMsgHandler implements ChannelHandler {
    private final String projectRootDir = ProjectConfigManager.getInstance().getStringProperty("prj_root_dir");

    //现在MBP的地址及端口 proxy的目的地
    private final String remoteHost = ProjectConfigManager.getInstance().getStringProperty("remote_server_ip");
    private final int remotePort = ProjectConfigManager.getInstance().getIntProperty("remote_server_port");

    private int timeout = ProjectConfigManager.getInstance().getIntProperty("remote_server_timeout"); //默认超时时间：ms  连接超时与读超时统一
    private int warningtime = ProjectConfigManager.getInstance().getIntProperty("remote_server_txn_warning_time"); //长交易时间预警阈值：ms

    private static int ROUTE_HEADER_LEN = 32 + 16 + 16;   //路由header 长度
    private static int MSG_LENGTH_FIELD_LEN = 10;  //表示业务交易整包长度的字段的长度
    private static int MSG_HEADER_LEN = 16 + 6 + 3;   //业务交易包header 长度

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
                    logger.error("交易处理失败.", e);
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
            logger.info("交易[" + txnCode + "] 执行时间:" + elapse + "ms.");
        } catch (ClassNotFoundException e) {
            logger.error("Txn processor not found!", e);
            throw new RuntimeException(e); //TODO  catch thread exception
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Txn processor InstantiationException error!", e);
            throw new RuntimeException(e); //TODO  catch thread exception
        } finally {
            try {
                //connection.setSoLinger(true,5);
                Thread.sleep(200);//等待客户端首先关闭连接
                connection.close();
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
            throw new RuntimeException("配置文件读取失败" + xmlfilename, e);
        }
    }

    /**
     *
     * @param out
     * @param requestRouteHeadBuffer   SBS-MBP之间64字节通讯报文头
     * @param requestBuffer SBS-MBP之间通讯报文体
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

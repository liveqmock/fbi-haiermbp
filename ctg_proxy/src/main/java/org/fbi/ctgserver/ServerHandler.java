package org.fbi.ctgserver;

import org.fbi.ctgproxy.CtgEciRequest;
import org.fbi.ctgproxy.CtgRequest;
import org.fbi.ctgproxy.CtgSif;
import org.fbi.ctgproxy.ProjectConfigManager;
import org.fbi.xplay.ChannelContext;
import org.fbi.xplay.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhanrui on 2014/9/23.
 * 业务处理主进程
 */
public class ServerHandler implements ChannelHandler {
    private final String projectRootDir = ProjectConfigManager.getInstance().getStringProperty("prj_root_dir");
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


    public ServerHandler(ConcurrentLinkedQueue<String> taskLogQueue,
                         ConcurrentLinkedQueue<String> warningTaskLogQueue) {
        this.taskLogQueue = taskLogQueue;
        this.warningTaskLogQueue = warningTaskLogQueue;
    }

    //短链接 处理完成后 由客户端close connection
    @Override
    public void onRead(ChannelContext ctx) throws IOException {
        Socket clntConnection = ctx.connection();
        SocketAddress clientAddress = clntConnection.getRemoteSocketAddress();
        logger.debug("Client[ " + clientAddress +  "]");

        try {
            DataInputStream osFromClient = new DataInputStream(clntConnection.getInputStream());
            DataOutputStream osToClient = new DataOutputStream(clntConnection.getOutputStream());

            for (;;) {
                int eyecatCode = osFromClient.readInt();
                if (eyecatCode != CtgRequest.EYECATCODE) {
                    logger.error("请求报文识别码错");
                    //TODO 组错误响应报文
                    return;
                }
                CtgEciRequest eciRequest = new CtgEciRequest();
                eciRequest.readObject(osFromClient);

                CtgSif ctgSif = new CtgSif(eciRequest.Commarea);
                logger.debug("===" + ctgSif);
                String txnCode = ctgSif.getTxnCode().trim();
                if (txnCode.length() > 4) {
                    txnCode = txnCode.substring(0,4);
                }
                String termId = ctgSif.getTermId();

                TxnContext txnContext = new TxnContext();
                txnContext.setCtgSif(ctgSif);

                Class txnClazz = Class.forName(this.getClass().getPackage().getName() + ".processor.T" + txnCode + "Processor");
                TxnProcessor processor = (TxnProcessor) txnClazz.newInstance();
                processor.process(txnContext);

            }
        } catch (SocketException e) {
            logger.info("连接已关闭", e.getMessage());
        } catch (Exception e) {
            logger.error("交易处理异常",e);
            //TODO
        } finally {
            try {
                //主动关闭server端连接
//                ctgServerConnection.close();
            } catch (Exception e) {
                logger.debug("连接关闭失败.可忽略.", e);
            }
            MDC.remove("txnCode");
        }
    }

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

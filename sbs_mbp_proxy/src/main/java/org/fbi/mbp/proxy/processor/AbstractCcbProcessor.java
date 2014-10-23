package org.fbi.mbp.proxy.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.mbp.proxy.ProjectConfigManager;
import org.fbi.mbp.proxy.TxnContext;
import org.fbi.mbp.proxy.domain.ccbvip.CcbvipMsgHead;
import org.fbi.xplay.SocketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhanrui on 2014/10/11.
 */
public class AbstractCcbProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //WinBridge的地址及端口
    //private final String ccbWinbridgeHost = ProjectConfigManager.getInstance().getStringProperty("ccb_winbridge_server_ip");
    //private final int ccbWinbridgePort = ProjectConfigManager.getInstance().getIntProperty("ccb_winbridge_server_port");

    private int timeout = ProjectConfigManager.getInstance().getIntProperty("remote_server_timeout"); //默认超时时间：ms  连接超时与读超时统一

    private static int MSG_HEADER_LEN = 111;   //header 长度

    protected String processServerRequest(TxnContext txnContext, String txnCode, String xmlMsg, String tpsTxnSn) throws IOException {
        //组CCB请求报文头
        CcbCommMsgHead ccbReqCommMsgHead = new CcbCommMsgHead();
        ccbReqCommMsgHead.setTxCode(StringUtils.rightPad(txnCode, 5, " "));
        xmlMsg = "<?xml version=\"1.0\"?>" + xmlMsg;
        byte[] xmlBuf = xmlMsg.getBytes("GBK");
        ccbReqCommMsgHead.setMsgDataLen(StringUtils.rightPad(("" + xmlBuf.length), 9, " "));
        String ccbReqStr = ccbReqCommMsgHead.toMsgString() +  xmlMsg;
        logger.info("=[" + tpsTxnSn + "]=" + "CCB Request Msg:" + ccbReqStr);

        InetAddress addr = InetAddress.getByName(txnContext.getCcbRouterConfigByKey("ICIP"));
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(addr, Integer.parseInt(txnContext.getCcbRouterConfigByKey("ICPort"))), timeout);
            socket.setSoTimeout(timeout);

            OutputStream os = socket.getOutputStream();
            os.write(ccbReqStr.getBytes("GBK"));
            os.flush();
            InputStream is = socket.getInputStream();

            byte[] ccbRespCommHeadBuf = new byte[MSG_HEADER_LEN];
            int readNum = is.read(ccbRespCommHeadBuf);
            if (readNum == -1) {
                throw new RuntimeException("服务器连接已关闭!");
            }
            if (readNum < MSG_HEADER_LEN) {
                throw new RuntimeException("读取通讯报文头长度部分错误...");
            }

            logger.info("CCB response comm header:" + new String(ccbRespCommHeadBuf));
            CcbCommMsgHead ccbRespCommMsgHead = new CcbCommMsgHead(new String(ccbRespCommHeadBuf));
            //logger.info("CCB response comm header:" + ccbRespCommMsgHead);

            int msgLen = Integer.parseInt(ccbRespCommMsgHead.getMsgDataLen().trim());
            byte[] ccbRespMsgBuf = new byte[msgLen];

            readNum = is.read(ccbRespMsgBuf);   //阻塞读
            if (readNum != msgLen) {
                throw new RuntimeException("报文长度错误,应为:[" + msgLen + "], 实际获取长度:[" + readNum + "]");
            }

            String ccbRespMsg = new String(ccbRespMsgBuf, "GBK");
            logger.debug("=[" + tpsTxnSn + "]=" +"CCB server response msg:" + ccbRespMsg);

            return ccbRespMsg;
        } finally {
            try {
                //主动关闭与第三方服务器的连接
                socket.close();
            } catch (IOException e) {
                logger.debug("连接关闭失败.可忽略.", e);
            }
        }
    }

    protected void processClientResponse(TxnContext context, String tpsTxnSn) throws IOException {
        String msgLen = StringUtils.leftPad(("" + context.getResponseBuffer().length), 10, "0");

        //client 响应报文
        OutputStream sendClientOs = context.getClientReponseOutputStream();
        byte[] buffer = SocketUtils.bytesMerger(msgLen.getBytes(),context.getResponseBuffer());
        logger.info("=[" + tpsTxnSn + "]=" + "Client response msg:[" + new String(buffer,"GBK") + "]");
        sendClientOs.write(buffer);
        sendClientOs.flush();
    }

    //CCB 机构码转换
    //TODO 读配置文件
    protected String getCcbBranchIdCode(String branchid) {
        if ("54751".equals(branchid)) {
            return "37199";
        } else {
            return "00000";
        }

    }

    protected  CcbvipMsgHead generateTpsRequestHeaderBean(TxnContext context, String txnDate, String txnCode, String tpsTxnSn){
        CcbvipMsgHead servReqHead = new CcbvipMsgHead();
        servReqHead.setVersion(context.getCcbRouterConfigByKey("version"));
        servReqHead.setTxCode(txnCode);
        servReqHead.setFuncCode("000");
        servReqHead.setChannel(context.getCcbRouterConfigByKey("channel"));
        servReqHead.setSubCenterId(context.getCcbRouterConfigByKey("subcenterid"));
        servReqHead.setNodeId(context.getCcbRouterConfigByKey("nodeid"));
        servReqHead.setTellerId(context.getCcbRouterConfigByKey("tellerid"));
        servReqHead.setTxSeqId(tpsTxnSn);
        servReqHead.setTxDate(txnDate);
        servReqHead.setTxTime(new SimpleDateFormat("HHmmss").format(new Date()));
        servReqHead.setUserId(context.getCcbRouterConfigByKey("userid"));
        return servReqHead;
    }
}

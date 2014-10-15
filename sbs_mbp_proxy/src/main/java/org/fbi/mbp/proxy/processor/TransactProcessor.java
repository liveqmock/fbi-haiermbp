package org.fbi.mbp.proxy.processor;

import org.fbi.mbp.proxy.TxnContext;
import org.fbi.mbp.proxy.TxnProcessor;
import org.fbi.mbp.proxy.domain.ccb.ServerMsgHead;
import org.fbi.mbp.proxy.domain.ccb.t2719request.ServerT2719RequestRoot;
import org.fbi.mbp.proxy.domain.ccb.t2719request.ServerT2719RequestBody;
import org.fbi.mbp.proxy.domain.ccb.t2719response.ServerT2719ResponseRoot;
import org.fbi.mbp.proxy.domain.sbs.transactrequest.TransactRequestParam;
import org.fbi.mbp.proxy.domain.sbs.transactrequest.TransactRequestRoot;
import org.fbi.mbp.proxy.utils.JaxbHelper;
import org.fbi.mbp.proxy.utils.TxnSnFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhanrui on 2014/10/8.
 * 对公支付交易 Transact
 */
public class TransactProcessor extends AbstractCcbProcessor implements TxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String TPS_TXNCODE = "2719";

    @Override
    public void process(TxnContext context) {
        try {
            //转换1：Client Request XML -> Cleint Request Bean
            JaxbHelper jaxbHelper = new JaxbHelper();
            TransactRequestRoot clientReqBean = jaxbHelper.xmlToBean(TransactRequestRoot.class, context.getRequestBuffer());
            logger.info("SBS Request:" + clientReqBean);

            //本地业务逻辑处理
            String tpsTxnSn = processTxn(context, clientReqBean);
            if (tpsTxnSn == null) {
                throw new RuntimeException("报文流水号转换错误");
            }

            //转换2：Client Request Bean -> Server Request Bean
            ServerT2719RequestRoot servReqBean =  new ServerT2719RequestRoot();
            assembleServerRequestRoot(context, clientReqBean, servReqBean, tpsTxnSn);

            //转换3：Server Request Bean -> Server Request Xml
            String ccbReqXml = jaxbHelper.beanToXml(ServerT2719RequestRoot.class, servReqBean);

            //与第三方Server通讯
            String tpsRespXml = processServerRequest(context, "2719", ccbReqXml);
            logger.info("CCB Response xmlmsg" + tpsRespXml);

            //转换4：Server Response Xml -> Server Response Bean
            ServerT2719ResponseRoot servRespBean =  jaxbHelper.xmlToBean(ServerT2719ResponseRoot.class, tpsRespXml.getBytes("GBK"));

            //检查逻辑
            if (!servReqBean.getHead().getTxSeqId().equals(servRespBean.getHead().getTxSeqId())) {
                logger.error("响应报文流水号与请求报文流水号不符。" + tpsRespXml);
                throw new RuntimeException("响应报文流水号与请求报文流水号不符。");
            }

            //转换5：Server Response Bean -> Client Response Bean


            //转换6：Client Response Bean -> Client Response Xml

            //Client响应
            //servReqHead.setTxSeqId(clientReqRoot.getHead().getOpBankCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //业务逻辑处理
    private String processTxn(TxnContext context, TransactRequestRoot clientReqestBean) {
        String txnDate = clientReqestBean.getHead().getOpDate();
        String filename = context.getProjectRootDir() + "/ccbserials/" + txnDate + ".txt";
        //检查本地文件 流水号是否重复
        String clientSn = clientReqestBean.getHead().getOpID();
        if (TxnSnFileHelper.isRepeatSn(filename, clientSn)) {
            //组错误信息报文
            logger.info("流水号重复:");
            return null;
        }

        //流水号转换 并保存
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
        String ccbSn = sdf.format(new Date()).substring(0, 8);
        String record = clientSn + ":" + ccbSn;
        TxnSnFileHelper.writeFileIsAppend(filename, record);
        return ccbSn;
    }

    //转换成 CCB Bean
    private void assembleServerRequestRoot(TxnContext context, TransactRequestRoot clientReqestBean, ServerT2719RequestRoot servReqRoot, String ccbSn) {
        ServerMsgHead servReqHead = new ServerMsgHead();
        ServerT2719RequestBody servReqBody = new ServerT2719RequestBody();
        //ServerT2719RequestRoot servReqRoot = new ServerT2719RequestRoot();
        servReqRoot.setHead(servReqHead);
        servReqRoot.setBody(servReqBody);

        servReqHead.setVersion(context.getCcbRouterConfigByKey("version"));
        servReqHead.setTxCode(TPS_TXNCODE);
        servReqHead.setFuncCode("000");
        servReqHead.setChannel(context.getCcbRouterConfigByKey("channel"));
        servReqHead.setSubCenterId(context.getCcbRouterConfigByKey("subcenterid"));
        servReqHead.setNodeId(context.getCcbRouterConfigByKey("nodeid"));
        servReqHead.setTellerId(context.getCcbRouterConfigByKey("tellerid"));
        servReqHead.setTxSeqId(ccbSn);
        servReqHead.setTxDate(clientReqestBean.getHead().getOpDate());
        servReqHead.setTxTime(new SimpleDateFormat("HHmmss").format(new Date()));
        String userid = context.getCcbRouterConfigByKey("userid");
        servReqHead.setUserId(userid);

        servReqBody.setCoSeqId(ccbSn);
        servReqBody.setOperatorUserId(userid);
        servReqBody.setOutUserId(userid);
        servReqBody.setOutDepId(getCcbBranchIdCode(context.getCcbRouterConfigByKey("branchid")));

        //确认付款账号 与配置文件一直
        String outAcctId = context.getCcbRouterConfigByKey("TotalAccount");
        TransactRequestParam param = clientReqestBean.getParam();

        if (!outAcctId.equals(param.getFromAccount())) {
            throw new RuntimeException("付款账号与配置文件中的账号不一致");
        }
        servReqBody.setOutAcctId(outAcctId);
        servReqBody.setOutAcctName(param.getFromName());
        servReqBody.setOutBranchName(param.getBank());

        String[] quotaFields = param.getReserved1().split(",");
        servReqBody.setQuotaAcctId(quotaFields[0]);
        servReqBody.setQuotaAcctName(quotaFields[1]);

        //TODO
        servReqBody.setInUserId("9999999999999999");
        servReqBody.setInDepId("00000");

        servReqBody.setInAcctId(param.getToAccount());
        servReqBody.setInAcctName(param.getToName());
        servReqBody.setInBranchName(param.getToBank());
        servReqBody.setInBranchId(param.getToReserved2());

        //是否他行标志
        String bankFlag = "1"; //默认本行
        if ("1".equals(param.getBank())) {//SBS报文中 “1”代表他行
            bankFlag = "0"; //设为他行
        }
        servReqBody.setBankFlag(bankFlag);

        servReqBody.setTxAmount(param.getAmount());  //金额不做转换
        servReqBody.setMemo("代理支付");
    }
}

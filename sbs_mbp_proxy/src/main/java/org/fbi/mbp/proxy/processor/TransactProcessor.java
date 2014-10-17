package org.fbi.mbp.proxy.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.mbp.proxy.TxnContext;
import org.fbi.mbp.proxy.TxnProcessor;
import org.fbi.mbp.proxy.domain.ccbvip.t2719request.CcbvipT2719RequestBody;
import org.fbi.mbp.proxy.domain.ccbvip.t2719request.CcbvipT2719RequestRoot;
import org.fbi.mbp.proxy.domain.ccbvip.t2719response.CcbvipT2719ResponseRoot;
import org.fbi.mbp.proxy.domain.sbs.ClientResponseHead;
import org.fbi.mbp.proxy.domain.sbs.transactreponse.TransactResponseParam;
import org.fbi.mbp.proxy.domain.sbs.transactreponse.TransactResponseRoot;
import org.fbi.mbp.proxy.domain.sbs.transactrequest.TransactRequestParam;
import org.fbi.mbp.proxy.domain.sbs.transactrequest.TransactRequestRoot;
import org.fbi.mbp.proxy.utils.JaxbHelper;
import org.fbi.mbp.proxy.utils.TxnSnFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
        TransactResponseRoot clientRespBean = new TransactResponseRoot();
        ClientResponseHead clientResponseHead = new ClientResponseHead();
        TransactResponseParam clientResponseParam = new TransactResponseParam();
        clientRespBean.setHead(clientResponseHead);
        clientRespBean.setParam(clientResponseParam);

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
            CcbvipT2719RequestRoot servReqBean =  generateServerRequestRoot(context, clientReqBean, tpsTxnSn);

            //转换3：Server Request Bean -> Server Request Xml
            String ccbReqXml = jaxbHelper.beanToXml(CcbvipT2719RequestRoot.class, servReqBean);

            //与第三方Server通讯
            String tpsRespXml = processServerRequest(context, TPS_TXNCODE, ccbReqXml);
            logger.info("CCB Response xmlmsg:[" + tpsRespXml + "]");

            //转换4：Server Response Xml -> Server Response Bean
            tpsRespXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + tpsRespXml.substring(21);
            CcbvipT2719ResponseRoot servRespBean =  jaxbHelper.xmlToBean(CcbvipT2719ResponseRoot.class, tpsRespXml.getBytes("GBK"));

            //检查逻辑
            if (!servReqBean.getHead().getTxSeqId().equals(servRespBean.getHead().getTxSeqId())) {
                logger.error("响应报文流水号与请求报文流水号不符。" + tpsRespXml);
                throw new RuntimeException("响应报文流水号与请求报文流水号不符。");
            }

            //转换5：Server Response Bean -> Client Response Bean
            //响应码转换 重要！
            String tpsRespCode = servRespBean.getBody().getRespCode();
            String clientRespCode = "0"; //CCB 完成
            if ("M0001".equals(tpsRespCode)) {
                clientResponseParam.setResult("0");
            } else {
                clientResponseParam.setResult("1");
            }
            clientResponseHead.setOpRetCode(clientRespCode);
            clientResponseHead.setOpRetMsg(servRespBean.getBody().getRespMsg());
            clientResponseParam.setBankSerial(servRespBean.getHead().getTxSeqId());

            //转换6：Client Response Bean -> Client Response Xml
            String clientRespXml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" + jaxbHelper.beanToXml(TransactResponseRoot.class, clientRespBean);
            logger.info("Client Response xmlmsg:[" + clientRespXml+ "]");

            //Client响应
            context.setResponseBuffer(clientRespXml.getBytes("GBK"));
            processClientResponse(context);
        } catch (Exception e) {
            logger.error("交易处理异常", e);

            //与CCB通讯超时等异常，返回retcode=0，result=非0  SBS会识别为1002->超时
            clientResponseHead.setOpRetCode("0");
            String msg = StringUtils.substring(e.getMessage(), 0, 20);
            clientResponseHead.setOpRetMsg(msg);
            clientResponseParam.setResult("1");

            JaxbHelper jaxbHelper = new JaxbHelper();
            String clientRespXml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" + jaxbHelper.beanToXml(TransactResponseRoot.class, clientRespBean);
            logger.info("Client Response xmlmsg:[" + clientRespXml+ "]");

            //Client响应
            try {
                context.setResponseBuffer(clientRespXml.getBytes("GBK"));
                processClientResponse(context);
            } catch (IOException e1) {
                logger.error("处理Client响应报文错误.", e);
                //不抛异常
            }
        }
    }

    //业务逻辑处理
    private String processTxn(TxnContext context, TransactRequestRoot clientReqestBean) {
        String txnDate = clientReqestBean.getHead().getOpDate();
        String filename = context.getProjectRootDir() + "/ccbserials/" + txnDate + ".txt";
        //检查本地文件 流水号是否重复
        String clientTxnSn = clientReqestBean.getParam().getEnterpriseSerial().trim();
        String tpsTxnSn = TxnSnFileHelper.getRepeatClientTxnSn(filename, clientTxnSn);
        if (tpsTxnSn != null) {
            //logger.info("流水号重复:");
            return tpsTxnSn;
        }

        //流水号转换 并保存
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
        tpsTxnSn = sdf.format(new Date()).substring(0, 8);
        String record = clientTxnSn + ":" + tpsTxnSn;
        TxnSnFileHelper.writeFileIsAppend(filename, record);
        return tpsTxnSn;
    }

    //转换成 CCB Bean
    private CcbvipT2719RequestRoot generateServerRequestRoot(TxnContext context, TransactRequestRoot clientReqestBean,  String tpsTxnSn) {
        CcbvipT2719RequestRoot servReqBean =  new CcbvipT2719RequestRoot();
        servReqBean.setHead(generateTpsRequestHeaderBean(context, clientReqestBean.getHead().getOpDate(), TPS_TXNCODE, tpsTxnSn));

        CcbvipT2719RequestBody servReqBody = new CcbvipT2719RequestBody();
        servReqBean.setBody(servReqBody);
        servReqBody.setCoSeqId(tpsTxnSn);
        servReqBody.setOperatorUserId(context.getCcbRouterConfigByKey("userid"));
        servReqBody.setOutUserId(context.getCcbRouterConfigByKey("userid"));
        servReqBody.setOutDepId(getCcbBranchIdCode(context.getCcbRouterConfigByKey("branchid")));

        //确认付款账号 与配置文件一直
        String outAcctId = context.getCcbRouterConfigByKey("TotalAccount");
        TransactRequestParam param = clientReqestBean.getParam();

        if (!outAcctId.equals(param.getFromAccount())) {
            throw new RuntimeException("付款账号与配置文件中的账号不一致");
        }
        servReqBody.setOutAcctId(outAcctId);
        servReqBody.setOutAcctName(param.getFromName());
        servReqBody.setOutBranchName(param.getFromBank());

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

        return servReqBean;
    }
}

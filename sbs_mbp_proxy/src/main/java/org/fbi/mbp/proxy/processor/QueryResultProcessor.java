package org.fbi.mbp.proxy.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.mbp.proxy.TxnContext;
import org.fbi.mbp.proxy.TxnProcessor;
import org.fbi.mbp.proxy.domain.ccbvip.t4868request.CcbvipT4868RequestBody;
import org.fbi.mbp.proxy.domain.ccbvip.t4868request.CcbvipT4868RequestRoot;
import org.fbi.mbp.proxy.domain.ccbvip.t4868response.CcbvipT4868ResponseRoot;
import org.fbi.mbp.proxy.domain.sbs.ClientResponseHead;
import org.fbi.mbp.proxy.domain.sbs.queryresultrequest.QueryResultRequestRoot;
import org.fbi.mbp.proxy.domain.sbs.queryresultresponse.QueryResultResponseParam;
import org.fbi.mbp.proxy.domain.sbs.queryresultresponse.QueryResultResponseRoot;
import org.fbi.mbp.proxy.utils.JaxbHelper;
import org.fbi.mbp.proxy.utils.TxnSnFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by zhanrui on 2014/10/8.
 * 对公支付结果查询交易 QueryResult
 */
public class QueryResultProcessor extends AbstractCcbProcessor implements TxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String TPS_TXNCODE = "4868";

    @Override
    public void process(TxnContext context) {
        //init1：客户端响应Bean
        QueryResultResponseRoot clientRespBean = new QueryResultResponseRoot();
        ClientResponseHead clientResponseHead = new ClientResponseHead();
        QueryResultResponseParam clientResponseParam = new QueryResultResponseParam();
        clientRespBean.setHead(clientResponseHead);
        clientRespBean.setParam(clientResponseParam);

        try {
            //转换1：Client Request XML -> Cleint Request Bean
            JaxbHelper jaxbHelper = new JaxbHelper();
            QueryResultRequestRoot clientReqBean = jaxbHelper.xmlToBean(QueryResultRequestRoot.class, context.getRequestBuffer());
            logger.info("SBS Request:" + clientReqBean);

            //本地业务逻辑处理
            String tpsTxnSn = processTxn(context, clientReqBean);
            if (tpsTxnSn == null) { //本地文件中无此流水号（交易日期文件）
                //直接返回
                clientResponseHead.setOpRetCode("0");
                clientResponseHead.setOpRetMsg("MBP-E0001:查无此交易流水号");
                clientResponseParam.setReason("MBP-E0001:查无此交易流水号");
                clientResponseParam.setResult("2");
                String clientRespXml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" + jaxbHelper.beanToXml(QueryResultResponseRoot.class, clientRespBean);
                logger.info("Client Response xmlmsg:[" + clientRespXml + "]");

                //Client响应
                context.setResponseBuffer(clientRespXml.getBytes("GBK"));
                processClientResponse(context);
                return;
            }

            //转换2：Client Request Bean -> Server Request Bean
            CcbvipT4868RequestRoot servReqBean = generateServerRequestRoot(context, clientReqBean, tpsTxnSn);

            //转换3：Server Request Bean -> Server Request Xml
            String ccbReqXml = jaxbHelper.beanToXml(CcbvipT4868RequestRoot.class, servReqBean);

            //与第三方Server通讯
            String tpsRespXml = processServerRequest(context, TPS_TXNCODE, ccbReqXml);
            logger.info("CCB Response xmlmsg:[" + tpsRespXml + "]");

            //转换4：Server Response Xml -> Server Response Bean
            tpsRespXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + tpsRespXml.substring(21);
            CcbvipT4868ResponseRoot servRespBean = jaxbHelper.xmlToBean(CcbvipT4868ResponseRoot.class, tpsRespXml.getBytes("GBK"));

            //检查逻辑
            if (!servReqBean.getHead().getTxSeqId().equals(servRespBean.getHead().getTxSeqId())) {
                logger.error("响应报文流水号与请求报文流水号不符。" + tpsRespXml);
                throw new RuntimeException("响应报文流水号与请求报文流水号不符。");
            }
            if (!servReqBean.getBody().getCoSeqId().equals(servRespBean.getBody().getCoSeqId())) {
                logger.error("响应报文中的查询流水号与请求报文的查询流水号不符。" + tpsRespXml);
                throw new RuntimeException("响应报文中的查询流水号与请求报文的查询流水号不符。");
            }

            //转换5：Server Response Bean -> Client Response Bean
            //响应码转换 重要！
            String tpsRespCode = servRespBean.getBody().getRespCode();
            String tpsRespMsg = servRespBean.getBody().getRespMsg();
            if (StringUtils.isEmpty(tpsRespCode)) {
                tpsRespCode = "E9999";
            }
            if (StringUtils.isEmpty(tpsRespMsg)) {
                tpsRespMsg = "CCB无响应信息.";
            }
            tpsRespMsg = "CCB-" + tpsRespCode + tpsRespMsg;

            String txnEndFlag = servRespBean.getBody().getTxnEndFlag();
            clientResponseHead.setOpRetCode("Unknown"); //默认值

            if ("M0001".equals(tpsRespCode)) {
                if (StringUtils.isEmpty(txnEndFlag)) {
                    clientResponseHead.setOpRetCode("0");
                    clientResponseParam.setResult("1");
                } else {
                    switch (txnEndFlag) {
                        case "1": //ccb处理成功结束
                            clientResponseHead.setOpRetCode("0");
                            clientResponseParam.setResult("0");
                            break;
                        case "2": //ccb处理失败结束
                            clientResponseHead.setOpRetCode("0");
                            clientResponseParam.setResult("2");
                            break;
                        case "3": //ccb处理中
                        case "0":
                            clientResponseHead.setOpRetCode("Unknown");
                            break;
                        default:
                            clientResponseHead.setOpRetCode("Unknown");
                    }
                }
            } else {
                switch (tpsRespCode) {
                    case "E2033": //SBS C客户端将会转换成 “1001”，SBS最终生成“WB02" ->不存在
                        clientResponseHead.setOpRetCode("0");
                        clientResponseParam.setResult("2");
                        break;
                    default:   //SBS C客户端将会转换成 “1002”，SBS最终生成“MZZZ" ->其它失败 需继续确认
                        clientResponseHead.setOpRetCode("9");
                        clientResponseParam.setResult("");
                }
            }

            clientResponseHead.setOpRetMsg(tpsRespMsg);
            clientResponseParam.setReason(tpsRespMsg);
            clientResponseParam.setBankSerial(servRespBean.getHead().getTxSeqId());

            //转换6：Client Response Bean -> Client Response Xml
            String clientRespXml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" + jaxbHelper.beanToXml(QueryResultResponseRoot.class, clientRespBean);
            logger.info("Client Response xmlmsg:[" + clientRespXml + "]");

            //Client响应
            context.setResponseBuffer(clientRespXml.getBytes("GBK"));
            processClientResponse(context);
        } catch (Exception e) {
            logger.error("交易处理异常", e);

            //与CCB通讯超时等异常，返回retcode=9，SBS会识别为1002->超时
            clientResponseHead.setOpRetCode("9");
            String msg = StringUtils.substring(e.getMessage(), 0, 20);
            clientResponseHead.setOpRetMsg(msg);
            clientResponseParam.setReason(msg);
            clientResponseParam.setResult("1");  //非0即可

            JaxbHelper jaxbHelper = new JaxbHelper();
            String clientRespXml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" + jaxbHelper.beanToXml(QueryResultResponseRoot.class, clientRespBean);
            logger.info("Client Response xmlmsg:[" + clientRespXml + "]");

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
    private String processTxn(TxnContext context, QueryResultRequestRoot clientReqestBean) {
        String txnDate = clientReqestBean.getParam().getTransDate(); //使用请求报文体中的日期字段
        String filename = context.getProjectRootDir() + "/ccbserials/" + txnDate + ".txt";
        //检查本地文件 流水号是否重复
        String clientTxnSn = clientReqestBean.getParam().getEnterpriseSerial().trim();
        return TxnSnFileHelper.getRepeatClientTxnSn(filename, clientTxnSn);
    }


    //转换成 CCB Bean
    private CcbvipT4868RequestRoot generateServerRequestRoot(TxnContext context, QueryResultRequestRoot clientReqestBean, String tpsTxnSn) {
        CcbvipT4868RequestRoot servReqBean = new CcbvipT4868RequestRoot();
        servReqBean.setHead(generateTpsRequestHeaderBean(context, clientReqestBean.getHead().getOpDate(), TPS_TXNCODE, tpsTxnSn));

        CcbvipT4868RequestBody servReqBody = new CcbvipT4868RequestBody();
        servReqBean.setBody(servReqBody);

        servReqBody.setTxDate(clientReqestBean.getParam().getTransDate());
        servReqBody.setCoSeqId(tpsTxnSn);
        servReqBody.setOperatorUserId(context.getCcbRouterConfigByKey("userid"));
        return servReqBean;
    }
}

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
 * �Թ�֧�������ѯ���� QueryResult
 */
public class QueryResultProcessor extends AbstractCcbProcessor implements TxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String TPS_TXNCODE = "4868";

    @Override
    public void process(TxnContext context) {
        //init1���ͻ�����ӦBean
        QueryResultResponseRoot clientRespBean = new QueryResultResponseRoot();
        ClientResponseHead clientResponseHead = new ClientResponseHead();
        QueryResultResponseParam clientResponseParam = new QueryResultResponseParam();
        clientRespBean.setHead(clientResponseHead);
        clientRespBean.setParam(clientResponseParam);

        try {
            //ת��1��Client Request XML -> Cleint Request Bean
            JaxbHelper jaxbHelper = new JaxbHelper();
            QueryResultRequestRoot clientReqBean = jaxbHelper.xmlToBean(QueryResultRequestRoot.class, context.getRequestBuffer());
            logger.info("SBS Request:" + clientReqBean);

            //����ҵ���߼�����
            String tpsTxnSn = processTxn(context, clientReqBean);
            if (tpsTxnSn == null) { //�����ļ����޴���ˮ�ţ����������ļ���
                //ֱ�ӷ���
                clientResponseHead.setOpRetCode("0");
                clientResponseHead.setOpRetMsg("MBP-E0001:���޴˽�����ˮ��");
                clientResponseParam.setReason("MBP-E0001:���޴˽�����ˮ��");
                clientResponseParam.setResult("2");
                String clientRespXml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" + jaxbHelper.beanToXml(QueryResultResponseRoot.class, clientRespBean);
                logger.info("Client Response xmlmsg:[" + clientRespXml + "]");

                //Client��Ӧ
                context.setResponseBuffer(clientRespXml.getBytes("GBK"));
                processClientResponse(context);
                return;
            }

            //ת��2��Client Request Bean -> Server Request Bean
            CcbvipT4868RequestRoot servReqBean = generateServerRequestRoot(context, clientReqBean, tpsTxnSn);

            //ת��3��Server Request Bean -> Server Request Xml
            String ccbReqXml = jaxbHelper.beanToXml(CcbvipT4868RequestRoot.class, servReqBean);

            //�������ServerͨѶ
            String tpsRespXml = processServerRequest(context, TPS_TXNCODE, ccbReqXml);
            logger.info("CCB Response xmlmsg:[" + tpsRespXml + "]");

            //ת��4��Server Response Xml -> Server Response Bean
            tpsRespXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + tpsRespXml.substring(21);
            CcbvipT4868ResponseRoot servRespBean = jaxbHelper.xmlToBean(CcbvipT4868ResponseRoot.class, tpsRespXml.getBytes("GBK"));

            //����߼�
            if (!servReqBean.getHead().getTxSeqId().equals(servRespBean.getHead().getTxSeqId())) {
                logger.error("��Ӧ������ˮ������������ˮ�Ų�����" + tpsRespXml);
                throw new RuntimeException("��Ӧ������ˮ������������ˮ�Ų�����");
            }
            if (!servReqBean.getBody().getCoSeqId().equals(servRespBean.getBody().getCoSeqId())) {
                logger.error("��Ӧ�����еĲ�ѯ��ˮ���������ĵĲ�ѯ��ˮ�Ų�����" + tpsRespXml);
                throw new RuntimeException("��Ӧ�����еĲ�ѯ��ˮ���������ĵĲ�ѯ��ˮ�Ų�����");
            }

            //ת��5��Server Response Bean -> Client Response Bean
            //��Ӧ��ת�� ��Ҫ��
            String tpsRespCode = servRespBean.getBody().getRespCode();
            String tpsRespMsg = servRespBean.getBody().getRespMsg();
            if (StringUtils.isEmpty(tpsRespCode)) {
                tpsRespCode = "E9999";
            }
            if (StringUtils.isEmpty(tpsRespMsg)) {
                tpsRespMsg = "CCB����Ӧ��Ϣ.";
            }
            tpsRespMsg = "CCB-" + tpsRespCode + tpsRespMsg;

            String txnEndFlag = servRespBean.getBody().getTxnEndFlag();
            clientResponseHead.setOpRetCode("Unknown"); //Ĭ��ֵ

            if ("M0001".equals(tpsRespCode)) {
                if (StringUtils.isEmpty(txnEndFlag)) {
                    clientResponseHead.setOpRetCode("0");
                    clientResponseParam.setResult("1");
                } else {
                    switch (txnEndFlag) {
                        case "1": //ccb����ɹ�����
                            clientResponseHead.setOpRetCode("0");
                            clientResponseParam.setResult("0");
                            break;
                        case "2": //ccb����ʧ�ܽ���
                            clientResponseHead.setOpRetCode("0");
                            clientResponseParam.setResult("2");
                            break;
                        case "3": //ccb������
                        case "0":
                            clientResponseHead.setOpRetCode("Unknown");
                            break;
                        default:
                            clientResponseHead.setOpRetCode("Unknown");
                    }
                }
            } else {
                switch (tpsRespCode) {
                    case "E2033": //SBS C�ͻ��˽���ת���� ��1001����SBS�������ɡ�WB02" ->������
                        clientResponseHead.setOpRetCode("0");
                        clientResponseParam.setResult("2");
                        break;
                    default:   //SBS C�ͻ��˽���ת���� ��1002����SBS�������ɡ�MZZZ" ->����ʧ�� �����ȷ��
                        clientResponseHead.setOpRetCode("9");
                        clientResponseParam.setResult("");
                }
            }

            clientResponseHead.setOpRetMsg(tpsRespMsg);
            clientResponseParam.setReason(tpsRespMsg);
            clientResponseParam.setBankSerial(servRespBean.getHead().getTxSeqId());

            //ת��6��Client Response Bean -> Client Response Xml
            String clientRespXml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" + jaxbHelper.beanToXml(QueryResultResponseRoot.class, clientRespBean);
            logger.info("Client Response xmlmsg:[" + clientRespXml + "]");

            //Client��Ӧ
            context.setResponseBuffer(clientRespXml.getBytes("GBK"));
            processClientResponse(context);
        } catch (Exception e) {
            logger.error("���״����쳣", e);

            //��CCBͨѶ��ʱ���쳣������retcode=9��SBS��ʶ��Ϊ1002->��ʱ
            clientResponseHead.setOpRetCode("9");
            String msg = StringUtils.substring(e.getMessage(), 0, 20);
            clientResponseHead.setOpRetMsg(msg);
            clientResponseParam.setReason(msg);
            clientResponseParam.setResult("1");  //��0����

            JaxbHelper jaxbHelper = new JaxbHelper();
            String clientRespXml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" + jaxbHelper.beanToXml(QueryResultResponseRoot.class, clientRespBean);
            logger.info("Client Response xmlmsg:[" + clientRespXml + "]");

            //Client��Ӧ
            try {
                context.setResponseBuffer(clientRespXml.getBytes("GBK"));
                processClientResponse(context);
            } catch (IOException e1) {
                logger.error("����Client��Ӧ���Ĵ���.", e);
                //�����쳣
            }
        }
    }

    //ҵ���߼�����
    private String processTxn(TxnContext context, QueryResultRequestRoot clientReqestBean) {
        String txnDate = clientReqestBean.getParam().getTransDate(); //ʹ�����������е������ֶ�
        String filename = context.getProjectRootDir() + "/ccbserials/" + txnDate + ".txt";
        //��鱾���ļ� ��ˮ���Ƿ��ظ�
        String clientTxnSn = clientReqestBean.getParam().getEnterpriseSerial().trim();
        return TxnSnFileHelper.getRepeatClientTxnSn(filename, clientTxnSn);
    }


    //ת���� CCB Bean
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

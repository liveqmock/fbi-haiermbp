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
 * �Թ�֧������ Transact
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
            //ת��1��Client Request XML -> Cleint Request Bean
            JaxbHelper jaxbHelper = new JaxbHelper();
            TransactRequestRoot clientReqBean = jaxbHelper.xmlToBean(TransactRequestRoot.class, context.getRequestBuffer());
            logger.info("SBS Request:" + clientReqBean);

            //����ҵ���߼�����
            String tpsTxnSn = processTxn(context, clientReqBean);
            if (tpsTxnSn == null) {
                throw new RuntimeException("������ˮ��ת������");
            }

            //ת��2��Client Request Bean -> Server Request Bean
            CcbvipT2719RequestRoot servReqBean =  generateServerRequestRoot(context, clientReqBean, tpsTxnSn);

            //ת��3��Server Request Bean -> Server Request Xml
            String ccbReqXml = jaxbHelper.beanToXml(CcbvipT2719RequestRoot.class, servReqBean);

            //�������ServerͨѶ
            String tpsRespXml = processServerRequest(context, TPS_TXNCODE, ccbReqXml);
            logger.info("CCB Response xmlmsg:[" + tpsRespXml + "]");

            //ת��4��Server Response Xml -> Server Response Bean
            tpsRespXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + tpsRespXml.substring(21);
            CcbvipT2719ResponseRoot servRespBean =  jaxbHelper.xmlToBean(CcbvipT2719ResponseRoot.class, tpsRespXml.getBytes("GBK"));

            //����߼�
            if (!servReqBean.getHead().getTxSeqId().equals(servRespBean.getHead().getTxSeqId())) {
                logger.error("��Ӧ������ˮ������������ˮ�Ų�����" + tpsRespXml);
                throw new RuntimeException("��Ӧ������ˮ������������ˮ�Ų�����");
            }

            //ת��5��Server Response Bean -> Client Response Bean
            //��Ӧ��ת�� ��Ҫ��
            String tpsRespCode = servRespBean.getBody().getRespCode();
            String clientRespCode = "0"; //CCB ���
            if ("M0001".equals(tpsRespCode)) {
                clientResponseParam.setResult("0");
            } else {
                clientResponseParam.setResult("1");
            }
            clientResponseHead.setOpRetCode(clientRespCode);
            clientResponseHead.setOpRetMsg(servRespBean.getBody().getRespMsg());
            clientResponseParam.setBankSerial(servRespBean.getHead().getTxSeqId());

            //ת��6��Client Response Bean -> Client Response Xml
            String clientRespXml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" + jaxbHelper.beanToXml(TransactResponseRoot.class, clientRespBean);
            logger.info("Client Response xmlmsg:[" + clientRespXml+ "]");

            //Client��Ӧ
            context.setResponseBuffer(clientRespXml.getBytes("GBK"));
            processClientResponse(context);
        } catch (Exception e) {
            logger.error("���״����쳣", e);

            //��CCBͨѶ��ʱ���쳣������retcode=0��result=��0  SBS��ʶ��Ϊ1002->��ʱ
            clientResponseHead.setOpRetCode("0");
            String msg = StringUtils.substring(e.getMessage(), 0, 20);
            clientResponseHead.setOpRetMsg(msg);
            clientResponseParam.setResult("1");

            JaxbHelper jaxbHelper = new JaxbHelper();
            String clientRespXml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" + jaxbHelper.beanToXml(TransactResponseRoot.class, clientRespBean);
            logger.info("Client Response xmlmsg:[" + clientRespXml+ "]");

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
    private String processTxn(TxnContext context, TransactRequestRoot clientReqestBean) {
        String txnDate = clientReqestBean.getHead().getOpDate();
        String filename = context.getProjectRootDir() + "/ccbserials/" + txnDate + ".txt";
        //��鱾���ļ� ��ˮ���Ƿ��ظ�
        String clientTxnSn = clientReqestBean.getParam().getEnterpriseSerial().trim();
        String tpsTxnSn = TxnSnFileHelper.getRepeatClientTxnSn(filename, clientTxnSn);
        if (tpsTxnSn != null) {
            //logger.info("��ˮ���ظ�:");
            return tpsTxnSn;
        }

        //��ˮ��ת�� ������
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
        tpsTxnSn = sdf.format(new Date()).substring(0, 8);
        String record = clientTxnSn + ":" + tpsTxnSn;
        TxnSnFileHelper.writeFileIsAppend(filename, record);
        return tpsTxnSn;
    }

    //ת���� CCB Bean
    private CcbvipT2719RequestRoot generateServerRequestRoot(TxnContext context, TransactRequestRoot clientReqestBean,  String tpsTxnSn) {
        CcbvipT2719RequestRoot servReqBean =  new CcbvipT2719RequestRoot();
        servReqBean.setHead(generateTpsRequestHeaderBean(context, clientReqestBean.getHead().getOpDate(), TPS_TXNCODE, tpsTxnSn));

        CcbvipT2719RequestBody servReqBody = new CcbvipT2719RequestBody();
        servReqBean.setBody(servReqBody);
        servReqBody.setCoSeqId(tpsTxnSn);
        servReqBody.setOperatorUserId(context.getCcbRouterConfigByKey("userid"));
        servReqBody.setOutUserId(context.getCcbRouterConfigByKey("userid"));
        servReqBody.setOutDepId(getCcbBranchIdCode(context.getCcbRouterConfigByKey("branchid")));

        //ȷ�ϸ����˺� �������ļ�һֱ
        String outAcctId = context.getCcbRouterConfigByKey("TotalAccount");
        TransactRequestParam param = clientReqestBean.getParam();

        if (!outAcctId.equals(param.getFromAccount())) {
            throw new RuntimeException("�����˺��������ļ��е��˺Ų�һ��");
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

        //�Ƿ����б�־
        String bankFlag = "1"; //Ĭ�ϱ���
        if ("1".equals(param.getBank())) {//SBS������ ��1����������
            bankFlag = "0"; //��Ϊ����
        }
        servReqBody.setBankFlag(bankFlag);

        servReqBody.setTxAmount(param.getAmount());  //����ת��
        servReqBody.setMemo("����֧��");

        return servReqBean;
    }
}

package org.fbi.mbp.proxy.processor;

import org.fbi.mbp.proxy.TxnContext;
import org.fbi.mbp.proxy.TxnProcessor;
import org.fbi.mbp.proxy.domain.ccbvip.CcbvipMsgHead;
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
            CcbvipT2719RequestRoot servReqBean =  new CcbvipT2719RequestRoot();
            assembleServerRequestRoot(context, clientReqBean, servReqBean, tpsTxnSn);

            //ת��3��Server Request Bean -> Server Request Xml
            String ccbReqXml = jaxbHelper.beanToXml(CcbvipT2719RequestRoot.class, servReqBean);

            //�������ServerͨѶ
            String tpsRespXml = processServerRequest(context, "2719", ccbReqXml);
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
            TransactResponseRoot clientRespBean = new TransactResponseRoot();
            ClientResponseHead clientResponseHead = new ClientResponseHead();
            TransactResponseParam clientResponseParam = new TransactResponseParam();
            clientRespBean.setHead(clientResponseHead);
            clientRespBean.setParam(clientResponseParam);

            //��Ӧ��ת�� ��Ҫ��
            String tpsRespCode = servRespBean.getBody().getRespCode();
            String clientRespCode = "9"; //Ĭ��Ϊ��������
            if ("M0001".equals(tpsRespCode)) {
                clientRespCode = "0";
            }
            clientResponseHead.setOpRetCode(clientRespCode);
            clientResponseHead.setOpRetMsg(servRespBean.getBody().getRespMsg());

            clientResponseParam.setResult(clientRespCode);
            clientResponseParam.setResult("0");
            clientResponseParam.setBankSerial(servRespBean.getHead().getTxSeqId());

            //ת��6��Client Response Bean -> Client Response Xml
            String clientRespXml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>" + jaxbHelper.beanToXml(TransactResponseRoot.class, clientRespBean);
            logger.info("Client Response xmlmsg:[" + clientRespXml+ "]");

            //Client��Ӧ
            context.setResponseBuffer(clientRespXml.getBytes("GBK"));
            processClientResponse(context);
        } catch (Exception e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    //ҵ���߼�����
    private String processTxn(TxnContext context, TransactRequestRoot clientReqestBean) {
        String txnDate = clientReqestBean.getHead().getOpDate();
        String filename = context.getProjectRootDir() + "/ccbserials/" + txnDate + ".txt";
        //��鱾���ļ� ��ˮ���Ƿ��ظ�
        String clientTxnSn = clientReqestBean.getHead().getOpID();
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
    private void assembleServerRequestRoot(TxnContext context, TransactRequestRoot clientReqestBean, CcbvipT2719RequestRoot servReqRoot, String tpsTxnSn) {
        CcbvipMsgHead servReqHead = new CcbvipMsgHead();
        CcbvipT2719RequestBody servReqBody = new CcbvipT2719RequestBody();
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
        servReqHead.setTxSeqId(tpsTxnSn);
        servReqHead.setTxDate(clientReqestBean.getHead().getOpDate());
        servReqHead.setTxTime(new SimpleDateFormat("HHmmss").format(new Date()));
        String userid = context.getCcbRouterConfigByKey("userid");
        servReqHead.setUserId(userid);

        servReqBody.setCoSeqId(tpsTxnSn);
        servReqBody.setOperatorUserId(userid);
        servReqBody.setOutUserId(userid);
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
    }
}

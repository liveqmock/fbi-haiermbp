package org.fbi.mbp.proxy.processor;

import org.fbi.mbp.proxy.TxnContext;
import org.fbi.mbp.proxy.TxnProcessor;
import org.fbi.mbp.proxy.domain.sbs.ClientReqestRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;

/**
 * Created by zhanrui on 2014/10/8.
 * �Թ�֧������ Transact
 */
class TransactProcessor implements TxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void process(TxnContext context) {
        try {
            //SBS ����
            ByteArrayInputStream is = new ByteArrayInputStream(context.getRequestBuffer());
            JAXBContext jaxbContext = JAXBContext.newInstance(ClientReqestRoot.class);
            Unmarshaller um = jaxbContext.createUnmarshaller();
            ClientReqestRoot clientReqRoot = (ClientReqestRoot)um.unmarshal(is);
            logger.info("SBS Request:" + clientReqRoot);

            //ת���� CCB Bean
            org.fbi.mbp.proxy.domain.ccb.t2719request.Head servReqHead = new org.fbi.mbp.proxy.domain.ccb.t2719request.Head();
            org.fbi.mbp.proxy.domain.ccb.t2719request.Body servReqBody = new org.fbi.mbp.proxy.domain.ccb.t2719request.Body();
            org.fbi.mbp.proxy.domain.ccb.t2719request.Root servReqRoot = new org.fbi.mbp.proxy.domain.ccb.t2719request.Root();


            //servReqHead.setTxSeqId(clientReqRoot.getHead().getOpBankCode());

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}

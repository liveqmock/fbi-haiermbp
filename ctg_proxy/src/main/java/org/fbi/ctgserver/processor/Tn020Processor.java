package org.fbi.ctgserver.processor;

import org.fbi.ctgserver.TxnContext;
import org.fbi.ctgserver.TxnProcessor;
import org.fbi.ctgserver.domain.ibpv10.TIAn020;
import org.fbi.ctgserver.domain.sbs.form.T531;
import org.fbi.ctgserver.util.sbsmsg.SBSResponse;
import org.fbi.ctgserver.util.sbsmsg.domain.SOFForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by zhanrui on 2014/10/29.
 */
public class Tn020Processor extends  AbstractProcessor implements TxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void process(TxnContext context) {
        TIAn020 tia = new TIAn020();
        marshelIbpTiaBean(context.getCtgSif(), tia);
        logger.info("TIA=" + tia);

        //SBS
        String sbsTxnCode = "n020";
        List<String> reqList = unmarshalSbsRequest(tia);

        String termId = context.getCtgSif().getTermId();
        String tellerId = context.getCtgSif().getTellerId();
        SBSResponse response = sendRequestToSbs(termId, tellerId, sbsTxnCode, reqList);
        SOFForm  sofForm = response.getSofForms().get(0);
        if ("T531".equals(sofForm.getFormHeader().getFormCode())) {
            T531 t531 = (T531) sofForm.getFormBody();
        } else {

        }

        //组客户端响应报文（ECI报文  FORM HEADER + 2bytes Body length + FORM BODY）
        SOFForm toClientForm = new SOFForm();
        toClientForm.
    }

}

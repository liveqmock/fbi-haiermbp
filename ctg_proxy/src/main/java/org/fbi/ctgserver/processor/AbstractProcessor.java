package org.fbi.ctgserver.processor;

import org.fbi.ctgproxy.CtgSif;
import org.fbi.ctgserver.TxnContext;
import org.fbi.ctgserver.TxnProcessor;
import org.fbi.ctgserver.util.sbsmsg.CtgManager;
import org.fbi.ctgserver.util.sbsmsg.SBSRequest;
import org.fbi.ctgserver.util.sbsmsg.SBSResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanrui on 2014/10/11.
 */
public class AbstractProcessor implements TxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void process(TxnContext context){}

    protected void marshelIbpTiaBean(CtgSif sif, Object tia) {
        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(sif.getSifData()));
            Field[] fields = tia.getClass().getDeclaredFields();
            for (Field field : fields) {
                int len = dis.readShort();
                byte[] f = new byte[len];
                dis.read(f, 0, len);
                String s = new String(f, "GBK").trim();
                field.setAccessible(true);
                field.set(tia, s.trim());
            }
        } catch (Exception e) {
            throw new RuntimeException("报文解析异常！", e);
        }
    }

    protected List<String> unmarshalSbsRequest(Object tia){
        try {
            List<String> reqList = new ArrayList<>();
            Field[] fields = tia.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                reqList.add((String)field.get(tia));
            }
            return reqList;
        } catch (Exception e) {
            throw new RuntimeException("报文处理异常！", e);
        }
    }

    protected SBSResponse sendRequestToSbs(String termid, String tellerid, String txnCode, List<String> paramList) {
        CtgManager ctgManager = new CtgManager();
        SBSRequest sbsRequest = new SBSRequest(termid, tellerid, txnCode, paramList);
        SBSResponse sbsResponse = new SBSResponse();
        ctgManager.processSingleResponsePkg(sbsRequest, sbsResponse);
        return sbsResponse;
    }

    protected  void sendSbsReponseToClient(){

    }
}

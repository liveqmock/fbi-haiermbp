package org.fbi.mbp.proxy.domain.ccbvip.t4868request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by zhanrui on 2014/10/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class CcbvipT4868RequestBody {
    @XmlElement(name = "TxDate", required = true)
    protected String txDate;

    @XmlElement(name = "OperatorUserId", required = true)
    protected String operatorUserId;

    @XmlElement(name = "CoSeqId", required = true)
    protected String coSeqId;


    public String getTxDate() {
        return txDate;
    }

    public void setTxDate(String txDate) {
        this.txDate = txDate;
    }

    public String getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(String operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

    public String getCoSeqId() {
        return coSeqId;
    }

    public void setCoSeqId(String coSeqId) {
        this.coSeqId = coSeqId;
    }

    @Override
    public String toString() {
        return "CcbvipT4868RequestBody{" +
                "txDate='" + txDate + '\'' +
                ", operatorUserId='" + operatorUserId + '\'' +
                ", coSeqId='" + coSeqId + '\'' +
                '}';
    }
}

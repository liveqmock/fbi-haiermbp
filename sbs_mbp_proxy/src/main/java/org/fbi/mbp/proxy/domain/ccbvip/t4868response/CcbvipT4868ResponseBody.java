package org.fbi.mbp.proxy.domain.ccbvip.t4868response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by zhanrui on 2014/10/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class CcbvipT4868ResponseBody {
    @XmlElement(name = "CoSeqId", required = true)
    protected String coSeqId;
    @XmlElement(name = "OperatorUserId", required = true)
    protected String operatorUserId;
    @XmlElement(name = "OutBranchName", required = true)
    protected String outBranchName;
    @XmlElement(name = "OutDepId", required = true)
    protected String outDepId;
    @XmlElement(name = "OutAcctId", required = true)
    protected String outAcctId;
    @XmlElement(name = "OutAcctName", required = true)
    protected String outAcctName;
    @XmlElement(name = "InBranchName", required = true)
    protected String inBranchName;
    @XmlElement(name = "InDepId", required = true)
    protected String inDepId;
    @XmlElement(name = "InBranchId", required = true)
    protected String inBranchId;
    @XmlElement(name = "InAcctId", required = true)
    protected String inAcctId;
    @XmlElement(name = "InAcctName", required = true)
    protected String inAcctName;
    @XmlElement(name = "TxAmount", required = true)
    protected String txAmount;
    @XmlElement(name = "TxnEndFlag", required = true)
    protected String txnEndFlag;
    @XmlElement(name = "RespMsg", required = true)
    protected String respMsg;
    @XmlElement(name = "RespCode", required = true)
    protected String respCode;
    @XmlElement(name = "CurCode", required = true)
    protected String curCode;

    public String getCoSeqId() {
        return coSeqId;
    }

    public void setCoSeqId(String coSeqId) {
        this.coSeqId = coSeqId;
    }

    public String getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(String operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

    public String getOutBranchName() {
        return outBranchName;
    }

    public void setOutBranchName(String outBranchName) {
        this.outBranchName = outBranchName;
    }

    public String getOutDepId() {
        return outDepId;
    }

    public void setOutDepId(String outDepId) {
        this.outDepId = outDepId;
    }

    public String getOutAcctId() {
        return outAcctId;
    }

    public void setOutAcctId(String outAcctId) {
        this.outAcctId = outAcctId;
    }

    public String getOutAcctName() {
        return outAcctName;
    }

    public void setOutAcctName(String outAcctName) {
        this.outAcctName = outAcctName;
    }

    public String getInBranchName() {
        return inBranchName;
    }

    public void setInBranchName(String inBranchName) {
        this.inBranchName = inBranchName;
    }

    public String getInDepId() {
        return inDepId;
    }

    public void setInDepId(String inDepId) {
        this.inDepId = inDepId;
    }

    public String getInBranchId() {
        return inBranchId;
    }

    public void setInBranchId(String inBranchId) {
        this.inBranchId = inBranchId;
    }

    public String getInAcctId() {
        return inAcctId;
    }

    public void setInAcctId(String inAcctId) {
        this.inAcctId = inAcctId;
    }

    public String getInAcctName() {
        return inAcctName;
    }

    public void setInAcctName(String inAcctName) {
        this.inAcctName = inAcctName;
    }

    public String getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(String txAmount) {
        this.txAmount = txAmount;
    }

    public String getTxnEndFlag() {
        return txnEndFlag;
    }

    public void setTxnEndFlag(String txnEndFlag) {
        this.txnEndFlag = txnEndFlag;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    @Override
    public String toString() {
        return "CcbvipT4868ResponseBody{" +
                "coSeqId='" + coSeqId + '\'' +
                ", operatorUserId='" + operatorUserId + '\'' +
                ", outBranchName='" + outBranchName + '\'' +
                ", outDepId='" + outDepId + '\'' +
                ", outAcctId='" + outAcctId + '\'' +
                ", outAcctName='" + outAcctName + '\'' +
                ", inBranchName='" + inBranchName + '\'' +
                ", inDepId='" + inDepId + '\'' +
                ", inBranchId='" + inBranchId + '\'' +
                ", inAcctId='" + inAcctId + '\'' +
                ", inAcctName='" + inAcctName + '\'' +
                ", txAmount='" + txAmount + '\'' +
                ", txnEndFlag='" + txnEndFlag + '\'' +
                ", respMsg='" + respMsg + '\'' +
                ", respCode='" + respCode + '\'' +
                ", curCode='" + curCode + '\'' +
                '}';
    }
}

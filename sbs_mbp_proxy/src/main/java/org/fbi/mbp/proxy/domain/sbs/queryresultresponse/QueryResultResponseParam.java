package org.fbi.mbp.proxy.domain.sbs.queryresultresponse;

import javax.xml.bind.annotation.*;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Param")
public class QueryResultResponseParam {
    @XmlElement(name = "Result", required = true)
    protected String result="";
    @XmlElement(name = "BankSerial", required = true)
    protected String bankSerial="";
    @XmlElement(name = "Reason", required = true)
    protected String reason="";
    @XmlElement(name = "Reserved1", required = true)
    protected String reserved1="";
    @XmlElement(name = "Reserved2", required = true)
    protected String reserved2="";
    @XmlElement(name = "Reserved3", required = true)
    protected String reserved3="";
    @XmlElement(name = "Reserved4", required = true)
    protected String reserved4="";

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBankSerial() {
        return bankSerial;
    }

    public void setBankSerial(String bankSerial) {
        this.bankSerial = bankSerial;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    @Override
    public String toString() {
        return "QueryResultResponseParam{" +
                "result='" + result + '\'' +
                ", bankSerial='" + bankSerial + '\'' +
                ", reason='" + reason + '\'' +
                ", reserved1='" + reserved1 + '\'' +
                ", reserved2='" + reserved2 + '\'' +
                ", reserved3='" + reserved3 + '\'' +
                ", reserved4='" + reserved4 + '\'' +
                '}';
    }
}

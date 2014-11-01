package org.fbi.ctgserver.domain.ibpv10;

/**
 * Created by zhanrui on 2014/10/30.
 */
public class TIAn020 extends TIABody {
    private String FBTIDX;
    private String ORGIDT;
    private String ORDDAT;
    private String FBTACT;
    private String ORDTYP;
    private String TXNCUR;
    private String TXNAMT;
    private String RMTTYP;
    private String CUSTYP;
    private String CUSACT;
    private String FEETYP;
    private String FEEACT;
    private String BENACT;
    private String BENNAM;
    private String BENBNK;
    private String AGENBK;
    private String PBKACT;
    private String RETNAM;
    private String RETAUX;
    private String CHQTYP;
    private String CHQNUM;
    private String CHQPWD;


    public String getFBTIDX() {
        return FBTIDX;
    }

    public void setFBTIDX(String FBTIDX) {
        this.FBTIDX = FBTIDX;
    }

    public String getORGIDT() {
        return ORGIDT;
    }

    public void setORGIDT(String ORGIDT) {
        this.ORGIDT = ORGIDT;
    }

    public String getORDDAT() {
        return ORDDAT;
    }

    public void setORDDAT(String ORDDAT) {
        this.ORDDAT = ORDDAT;
    }

    public String getFBTACT() {
        return FBTACT;
    }

    public void setFBTACT(String FBTACT) {
        this.FBTACT = FBTACT;
    }

    public String getORDTYP() {
        return ORDTYP;
    }

    public void setORDTYP(String ORDTYP) {
        this.ORDTYP = ORDTYP;
    }

    public String getTXNCUR() {
        return TXNCUR;
    }

    public void setTXNCUR(String TXNCUR) {
        this.TXNCUR = TXNCUR;
    }

    public String getTXNAMT() {
        return TXNAMT;
    }

    public void setTXNAMT(String TXNAMT) {
        this.TXNAMT = TXNAMT;
    }

    public String getRMTTYP() {
        return RMTTYP;
    }

    public void setRMTTYP(String RMTTYP) {
        this.RMTTYP = RMTTYP;
    }

    public String getCUSTYP() {
        return CUSTYP;
    }

    public void setCUSTYP(String CUSTYP) {
        this.CUSTYP = CUSTYP;
    }

    public String getCUSACT() {
        return CUSACT;
    }

    public void setCUSACT(String CUSACT) {
        this.CUSACT = CUSACT;
    }

    public String getFEETYP() {
        return FEETYP;
    }

    public void setFEETYP(String FEETYP) {
        this.FEETYP = FEETYP;
    }

    public String getFEEACT() {
        return FEEACT;
    }

    public void setFEEACT(String FEEACT) {
        this.FEEACT = FEEACT;
    }

    public String getBENACT() {
        return BENACT;
    }

    public void setBENACT(String BENACT) {
        this.BENACT = BENACT;
    }

    public String getBENNAM() {
        return BENNAM;
    }

    public void setBENNAM(String BENNAM) {
        this.BENNAM = BENNAM;
    }

    public String getBENBNK() {
        return BENBNK;
    }

    public void setBENBNK(String BENBNK) {
        this.BENBNK = BENBNK;
    }

    public String getAGENBK() {
        return AGENBK;
    }

    public void setAGENBK(String AGENBK) {
        this.AGENBK = AGENBK;
    }

    public String getPBKACT() {
        return PBKACT;
    }

    public void setPBKACT(String PBKACT) {
        this.PBKACT = PBKACT;
    }

    public String getRETNAM() {
        return RETNAM;
    }

    public void setRETNAM(String RETNAM) {
        this.RETNAM = RETNAM;
    }

    public String getRETAUX() {
        return RETAUX;
    }

    public void setRETAUX(String RETAUX) {
        this.RETAUX = RETAUX;
    }

    public String getCHQTYP() {
        return CHQTYP;
    }

    public void setCHQTYP(String CHQTYP) {
        this.CHQTYP = CHQTYP;
    }

    public String getCHQNUM() {
        return CHQNUM;
    }

    public void setCHQNUM(String CHQNUM) {
        this.CHQNUM = CHQNUM;
    }

    public String getCHQPWD() {
        return CHQPWD;
    }

    public void setCHQPWD(String CHQPWD) {
        this.CHQPWD = CHQPWD;
    }

    @Override
    public String toString() {
        return "TIAn020{" +
                "FBTIDX='" + FBTIDX + '\'' +
                ", ORGIDT='" + ORGIDT + '\'' +
                ", ORDDAT='" + ORDDAT + '\'' +
                ", FBTACT='" + FBTACT + '\'' +
                ", ORDTYP='" + ORDTYP + '\'' +
                ", TXNCUR='" + TXNCUR + '\'' +
                ", TXNAMT='" + TXNAMT + '\'' +
                ", RMTTYP='" + RMTTYP + '\'' +
                ", CUSTYP='" + CUSTYP + '\'' +
                ", CUSACT='" + CUSACT + '\'' +
                ", FEETYP='" + FEETYP + '\'' +
                ", FEEACT='" + FEEACT + '\'' +
                ", BENACT='" + BENACT + '\'' +
                ", BENNAM='" + BENNAM + '\'' +
                ", BENBNK='" + BENBNK + '\'' +
                ", AGENBK='" + AGENBK + '\'' +
                ", PBKACT='" + PBKACT + '\'' +
                ", RETNAM='" + RETNAM + '\'' +
                ", RETAUX='" + RETAUX + '\'' +
                ", CHQTYP='" + CHQTYP + '\'' +
                ", CHQNUM='" + CHQNUM + '\'' +
                ", CHQPWD='" + CHQPWD + '\'' +
                '}';
    }
}

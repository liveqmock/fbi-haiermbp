package org.fbi.ctgserver.domain.sbs.form;

import org.fbi.ctgserver.util.sbsmsg.domain.SOFFormBody;

/**
 * Created by zhanrui on 2014/10/30.
 */
public class T531 extends SOFFormBody {
    {
        fieldTypes = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1};
        fieldLengths = new int[]{18,4,4,4,2,8,3,17,2,22,7,30,2,2,22,7,30,2,8,4,2,2};
    }

    private String SECNUM;
    private String TXNCDE;
    private String TLRNUM;
    private String VCHSET;
    private String SETSEQ;
    private String TXNDAT;
    private String INTCUR;
    private String TXNAMT;
    private String ACTTY1;
    private String IPTAC1;
    private String CUSID1;
    private String ACTNM1;
    private String SHTSEQ;
    private String ACTTY2;
    private String IPTAC2;
    private String CUSID2;
    private String ACTNM2;
    private String DPTPRD;
    private String VALDAT;
    private String AUTTLR;
    private String ACTTYP1;
    private String ACTTYP2;

    public String getSECNUM() {
        return SECNUM;
    }

    public void setSECNUM(String SECNUM) {
        this.SECNUM = SECNUM;
    }

    public String getTXNCDE() {
        return TXNCDE;
    }

    public void setTXNCDE(String TXNCDE) {
        this.TXNCDE = TXNCDE;
    }

    public String getTLRNUM() {
        return TLRNUM;
    }

    public void setTLRNUM(String TLRNUM) {
        this.TLRNUM = TLRNUM;
    }

    public String getVCHSET() {
        return VCHSET;
    }

    public void setVCHSET(String VCHSET) {
        this.VCHSET = VCHSET;
    }

    public String getSETSEQ() {
        return SETSEQ;
    }

    public void setSETSEQ(String SETSEQ) {
        this.SETSEQ = SETSEQ;
    }

    public String getTXNDAT() {
        return TXNDAT;
    }

    public void setTXNDAT(String TXNDAT) {
        this.TXNDAT = TXNDAT;
    }

    public String getINTCUR() {
        return INTCUR;
    }

    public void setINTCUR(String INTCUR) {
        this.INTCUR = INTCUR;
    }

    public String getTXNAMT() {
        return TXNAMT;
    }

    public void setTXNAMT(String TXNAMT) {
        this.TXNAMT = TXNAMT;
    }

    public String getACTTY1() {
        return ACTTY1;
    }

    public void setACTTY1(String ACTTY1) {
        this.ACTTY1 = ACTTY1;
    }

    public String getIPTAC1() {
        return IPTAC1;
    }

    public void setIPTAC1(String IPTAC1) {
        this.IPTAC1 = IPTAC1;
    }

    public String getCUSID1() {
        return CUSID1;
    }

    public void setCUSID1(String CUSID1) {
        this.CUSID1 = CUSID1;
    }

    public String getACTNM1() {
        return ACTNM1;
    }

    public void setACTNM1(String ACTNM1) {
        this.ACTNM1 = ACTNM1;
    }

    public String getSHTSEQ() {
        return SHTSEQ;
    }

    public void setSHTSEQ(String SHTSEQ) {
        this.SHTSEQ = SHTSEQ;
    }

    public String getACTTY2() {
        return ACTTY2;
    }

    public void setACTTY2(String ACTTY2) {
        this.ACTTY2 = ACTTY2;
    }

    public String getIPTAC2() {
        return IPTAC2;
    }

    public void setIPTAC2(String IPTAC2) {
        this.IPTAC2 = IPTAC2;
    }

    public String getCUSID2() {
        return CUSID2;
    }

    public void setCUSID2(String CUSID2) {
        this.CUSID2 = CUSID2;
    }

    public String getACTNM2() {
        return ACTNM2;
    }

    public void setACTNM2(String ACTNM2) {
        this.ACTNM2 = ACTNM2;
    }

    public String getDPTPRD() {
        return DPTPRD;
    }

    public void setDPTPRD(String DPTPRD) {
        this.DPTPRD = DPTPRD;
    }

    public String getVALDAT() {
        return VALDAT;
    }

    public void setVALDAT(String VALDAT) {
        this.VALDAT = VALDAT;
    }

    public String getAUTTLR() {
        return AUTTLR;
    }

    public void setAUTTLR(String AUTTLR) {
        this.AUTTLR = AUTTLR;
    }

    public String getACTTYP1() {
        return ACTTYP1;
    }

    public void setACTTYP1(String ACTTYP1) {
        this.ACTTYP1 = ACTTYP1;
    }

    public String getACTTYP2() {
        return ACTTYP2;
    }

    public void setACTTYP2(String ACTTYP2) {
        this.ACTTYP2 = ACTTYP2;
    }
}

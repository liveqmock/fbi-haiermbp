package org.fbi.mbp.proxy;

import org.apache.commons.lang.StringUtils;

/**
 * Created by zhanrui on 2014/9/8.
 * 业务报文头
 */
public class MsgHeader {
    private String msgSn;
    private String txnCode;
    private String bankCode;

    private static int MSG_HEADER_LENGTH = 16 + 6 + 3;

    public MsgHeader(String header) {
        if (header == null || header.getBytes().length != MSG_HEADER_LENGTH) {
            throw new IllegalArgumentException("报文头长度错误.");
        }

        int index = 0;

        int step = 16;
        msgSn = header.substring(index, index + step).trim();
        index += step;

        step = 6;
        txnCode = header.substring(index, index + step).trim();
        index += step;

        step = 3;
        bankCode = header.substring(index, index + step).trim();

    }

    public MsgHeader(String msgSn, String txnCode, String bankCode) {
        this.msgSn = StringUtils.rightPad(msgSn, 16, " ");
        this.txnCode = StringUtils.rightPad(txnCode, 6, " ");
        this.bankCode = StringUtils.rightPad(bankCode, 2, " ");
    }

    public String getMsgSn() {
        return msgSn;
    }

    public void setMsgSn(String msgSn) {
        this.msgSn = msgSn;
    }

    public String getTxnCode() {
        return txnCode;
    }

    public void setTxnCode(String txnCode) {
        this.txnCode = txnCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MsgHeader msgHeader = (MsgHeader) o;

        if (bankCode != null ? !bankCode.equals(msgHeader.bankCode) : msgHeader.bankCode != null) return false;
        if (msgSn != null ? !msgSn.equals(msgHeader.msgSn) : msgHeader.msgSn != null) return false;
        if (txnCode != null ? !txnCode.equals(msgHeader.txnCode) : msgHeader.txnCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = msgSn != null ? msgSn.hashCode() : 0;
        result = 31 * result + (txnCode != null ? txnCode.hashCode() : 0);
        result = 31 * result + (bankCode != null ? bankCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MsgHeader{" +
                "msgSn='" + msgSn + '\'' +
                ", txnCode='" + txnCode + '\'' +
                ", bankCode='" + bankCode + '\'' +
                '}';
    }

    public String getHeaderString() {
        return msgSn
                + txnCode
                + bankCode;
    }
}

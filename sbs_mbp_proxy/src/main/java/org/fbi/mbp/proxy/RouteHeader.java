package org.fbi.mbp.proxy;

import org.apache.commons.lang.StringUtils;

/**
 * Created by zhanrui on 2014/9/8.
 */
public class RouteHeader {
    private String txnCode;
    private String clientName;
    private String bankCode;

    private static int MSG_HEADER_LENGTH = 32 + 16 + 16;

    public RouteHeader(String header) {
        if (header == null || header.getBytes().length != MSG_HEADER_LENGTH) {
            throw new IllegalArgumentException("报文头长度错误.");
        }

        int index = 0;

        int step = 32;
        txnCode = header.substring(index, index + step).trim();
        index += step;

        step = 16;
        clientName = header.substring(index, index + step).trim();
        index += step;

        bankCode = header.substring(index, index + step).trim();

    }

    public RouteHeader(String clientName, String txnCode, String bankCode) {
        this.txnCode = StringUtils.rightPad(txnCode, 32, " ");
        this.clientName = StringUtils.rightPad(clientName, 16, " ");
        this.bankCode = StringUtils.rightPad(bankCode, 16, " ");
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

        RouteHeader msgHeader = (RouteHeader) o;

        if (bankCode != null ? !bankCode.equals(msgHeader.bankCode) : msgHeader.bankCode != null) return false;
        if (clientName != null ? !clientName.equals(msgHeader.clientName) : msgHeader.clientName != null) return false;
        if (txnCode != null ? !txnCode.equals(msgHeader.txnCode) : msgHeader.txnCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clientName != null ? clientName.hashCode() : 0;
        result = 31 * result + (txnCode != null ? txnCode.hashCode() : 0);
        result = 31 * result + (bankCode != null ? bankCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RouteHeader{" +
                "txnCode='" + txnCode + '\'' +
                ", clientName='" + clientName  + '\'' +
                ", bankCode='" + bankCode + '\'' +
                '}';
    }

    public String getHeaderString() {
        return txnCode + clientName
                + bankCode;
    }
}

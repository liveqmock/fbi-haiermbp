package org.fbi.mbp.proxy;

import org.apache.commons.lang.StringUtils;

/**
 * Created by zhanrui on 2014/9/8.
 */
public class VipMsgHeader {
    private String version;
    private String packType;
    private String txCode;
    private String funcCode;
    private String commMode;
    private String totalBlock;
    private String curBlock;
    private String msgDataLen;
    private String msgExtLen;
    private String encryptMode;
    private String noUse;

    private static int MSG_HEADER_LENGTH = 60 + 51;

    public VipMsgHeader(String header) {
        if (header == null || header.getBytes().length != MSG_HEADER_LENGTH) {
            throw new IllegalArgumentException("报文头长度错误.");
        }

        int index = 0;

        int step = 9;
        version = header.substring(index, index + step).trim();
        index += step;

        step = 2;
        packType = header.substring(index, index + step).trim();
        index += step;

        step = 5;
        txCode = header.substring(index, index + step).trim();
        index += step;

        step = 4;
        funcCode = header.substring(index, index + step).trim();
        index += step;

        step = 2;
        commMode = header.substring(index, index + step).trim();
        index += step;

        step = 9;
        totalBlock = header.substring(index, index + step).trim();
        index += step;

        step = 9;
        curBlock = header.substring(index, index + step).trim();
        index += step;

        step = 9;
        msgDataLen = header.substring(index, index + step).trim();
        index += step;

        step = 9;
        msgExtLen = header.substring(index, index + step).trim();
        index += step;

        step = 2;
        encryptMode = header.substring(index, index + step).trim();
        index += step;

        step = 51;
        noUse = header.substring(index, index + step).trim();

    }

    public VipMsgHeader(String version, String packType, String txCode, String funcCode, String commMode, String totalBlock, String curBlock, String msgDataLen, String msgExtLen, String encryptMode, String noUse) {
        this.version = StringUtils.rightPad(version, 9, " ");
        this.packType = StringUtils.rightPad(packType, 2, " ");
        this.txCode = StringUtils.rightPad(txCode, 5, " ");
        this.funcCode = StringUtils.rightPad(funcCode, 4, " ");
        this.commMode = StringUtils.rightPad(commMode, 2, " ");
        this.totalBlock = StringUtils.rightPad(totalBlock, 9, " ");
        this.curBlock = StringUtils.rightPad(curBlock, 9, " ");
        this.msgDataLen = StringUtils.leftPad(msgDataLen, 9, "0");
        this.msgExtLen = StringUtils.leftPad(msgExtLen, 9, "0");
        this.encryptMode = StringUtils.rightPad(encryptMode, 2, " ");
        this.noUse = StringUtils.rightPad(noUse, 51, " ");
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getTxCode() {
        return txCode;
    }

    public void setTxCode(String txCode) {
        this.txCode = txCode;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public String getCommMode() {
        return commMode;
    }

    public void setCommMode(String commMode) {
        this.commMode = commMode;
    }

    public String getTotalBlock() {
        return totalBlock;
    }

    public void setTotalBlock(String totalBlock) {
        this.totalBlock = totalBlock;
    }

    public String getCurBlock() {
        return curBlock;
    }

    public void setCurBlock(String curBlock) {
        this.curBlock = curBlock;
    }

    public String getMsgDataLen() {
        return msgDataLen;
    }

    public void setMsgDataLen(String msgDataLen) {
        this.msgDataLen = msgDataLen;
    }

    public String getMsgExtLen() {
        return msgExtLen;
    }

    public void setMsgExtLen(String msgExtLen) {
        this.msgExtLen = msgExtLen;
    }

    public String getEncryptMode() {
        return encryptMode;
    }

    public void setEncryptMode(String encryptMode) {
        this.encryptMode = encryptMode;
    }

    public String getNoUse() {
        return noUse;
    }

    public void setNoUse(String noUse) {
        this.noUse = noUse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VipMsgHeader vipMsgHeader = (VipMsgHeader) o;

        if (commMode != null ? !commMode.equals(vipMsgHeader.commMode) : vipMsgHeader.commMode != null) return false;
        if (curBlock != null ? !curBlock.equals(vipMsgHeader.curBlock) : vipMsgHeader.curBlock != null) return false;
        if (encryptMode != null ? !encryptMode.equals(vipMsgHeader.encryptMode) : vipMsgHeader.encryptMode != null)
            return false;
        if (funcCode != null ? !funcCode.equals(vipMsgHeader.funcCode) : vipMsgHeader.funcCode != null) return false;
        if (msgDataLen != null ? !msgDataLen.equals(vipMsgHeader.msgDataLen) : vipMsgHeader.msgDataLen != null)
            return false;
        if (msgExtLen != null ? !msgExtLen.equals(vipMsgHeader.msgExtLen) : vipMsgHeader.msgExtLen != null)
            return false;
        if (noUse != null ? !noUse.equals(vipMsgHeader.noUse) : vipMsgHeader.noUse != null) return false;
        if (packType != null ? !packType.equals(vipMsgHeader.packType) : vipMsgHeader.packType != null) return false;
        if (totalBlock != null ? !totalBlock.equals(vipMsgHeader.totalBlock) : vipMsgHeader.totalBlock != null)
            return false;
        if (txCode != null ? !txCode.equals(vipMsgHeader.txCode) : vipMsgHeader.txCode != null) return false;
        if (version != null ? !version.equals(vipMsgHeader.version) : vipMsgHeader.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = version != null ? version.hashCode() : 0;
        result = 31 * result + (packType != null ? packType.hashCode() : 0);
        result = 31 * result + (txCode != null ? txCode.hashCode() : 0);
        result = 31 * result + (funcCode != null ? funcCode.hashCode() : 0);
        result = 31 * result + (commMode != null ? commMode.hashCode() : 0);
        result = 31 * result + (totalBlock != null ? totalBlock.hashCode() : 0);
        result = 31 * result + (curBlock != null ? curBlock.hashCode() : 0);
        result = 31 * result + (msgDataLen != null ? msgDataLen.hashCode() : 0);
        result = 31 * result + (msgExtLen != null ? msgExtLen.hashCode() : 0);
        result = 31 * result + (encryptMode != null ? encryptMode.hashCode() : 0);
        result = 31 * result + (noUse != null ? noUse.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VipMsgHeader{" +
                "version='" + version + '\'' +
                ", packType='" + packType + '\'' +
                ", txCode='" + txCode + '\'' +
                ", funcCode='" + funcCode + '\'' +
                ", commMode='" + commMode + '\'' +
                ", totalBlock='" + totalBlock + '\'' +
                ", curBlock='" + curBlock + '\'' +
                ", msgDataLen='" + msgDataLen + '\'' +
                ", msgExtLen='" + msgExtLen + '\'' +
                ", encryptMode='" + encryptMode + '\'' +
                ", noUse='" + noUse + '\'' +
                '}';
    }

    public String getHeaderString() {
        return version
                + packType
                + txCode
                + funcCode
                + commMode
                + totalBlock
                + curBlock
                + msgDataLen
                + msgExtLen
                + encryptMode
                + noUse;
    }
}

package org.fbi.mbp.proxy.processor;

import org.apache.commons.lang.StringUtils;

/**
 * Created by zhanrui on 2014/10/13.
 * Ccb server 通讯报文头
 * struct MSG_HEADER {
         char		version[ 9 ];
         char		packType[ 2 ];
         char		txCode[ 5 ];
         char		funcCode[ 4 ];
         char		commMode[ 2 ];
         char		totalBlock[ 9 ];
         char		curBlock[ 9 ];
         char		msgDataLen[ 9 ];
         char		msgExtLen[ 9 ];
         char		encryptMode[ 2 ];
         char		noUse[ 51 ];
 */
public class CcbCommMsgHead {
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
    private String noUse = StringUtils.rightPad("00", 51, " ");

    //默认 请求报文通讯头
    public CcbCommMsgHead() {
        this.version = StringUtils.rightPad("01", 9, " ");
        this.packType = "0 ";
        this.txCode = "     ";
        this.funcCode = "000 ";
        this.commMode = "M ";
        this.totalBlock = StringUtils.rightPad("00", 9, " ");
        this.curBlock = StringUtils.rightPad("00", 9, " ");
        this.msgDataLen = StringUtils.rightPad("00", 9, " ");
        this.msgExtLen = StringUtils.rightPad("00", 9, " ");
        this.encryptMode = "N ";
        this.noUse = StringUtils.rightPad("00", 51, " ");
    }
    public CcbCommMsgHead(String header) {
        int start = 0;
        int step = 9;
        this.version = StringUtils.mid(header, start, step);

        start += step;
        step = 2;
        this.packType = StringUtils.mid(header, start, step);

        start += step;
        step = 5;
        this.txCode = StringUtils.mid(header, start, step);

        start += step;
        step = 4;
        this.funcCode = StringUtils.mid(header, start, step);

        start += step;
        step = 2;
        this.commMode = StringUtils.mid(header, start, step);

        start += step;
        step = 9;
        this.totalBlock = StringUtils.mid(header, start, step);

        start += step;
        step = 9;
        this.curBlock = StringUtils.mid(header, start, step);

        start += step;
        step = 9;
        this.msgDataLen = StringUtils.mid(header, start, step);

        start += step;
        step = 9;
        this.msgExtLen = StringUtils.mid(header, start, step);

        start += step;
        step = 2;
        this.encryptMode = StringUtils.mid(header, start, step);

        start += step;
        step = 51;
        this.noUse = StringUtils.mid(header, start, step);

    }

    public String toMsgString(){
        return this.version +
                this.packType +
                this.txCode +
                this.funcCode +
                this.commMode +
                this.totalBlock +
                this.curBlock +
                this.msgDataLen +
                this.msgExtLen +
                this.encryptMode +
                this.noUse;
    }
    //======
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
    public String toString() {
        return "CcbCommMsgHead{" +
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
}

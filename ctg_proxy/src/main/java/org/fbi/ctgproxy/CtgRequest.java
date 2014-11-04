package org.fbi.ctgproxy;

import org.fbi.ctgproxy.oldctg.Callbackable;

import java.io.*;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by zhanrui on 2014/10/25.
 */
public class CtgRequest {
    public static final String BASE = "BASE";
    public static final int EYECATCODE = 0x47617465;
    public static final int GATEWAY_FLOW_V501 = 0x500010;
    public static final int GATEWAY_FLOW_V410 = 0x401000;
    public static final int GATEWAY_FLOW_V400 = 0x400000;
    public static final int GATEWAY_FLOW_V310 = 0x301000;
    public static final int GATEWAY_FLOW_V300 = 0x300000;
    public static final int GATEWAY_FLOW_V201 = 0x200010;
    public static final int GATEWAY_FLOW_V200 = 0x200000;
    public static final int GATEWAY_FLOW_V111 = 0x101010;
    public static final int GATEWAY_FLOW_V110 = 0x10200;
    public static final int GATEWAY_FLOW_V100 = 0x10100;
    public static final int GATEWAY_FLOW_VER = 0x500010;
    public static final int FLOW_REQUEST = 1;
    public static final int FLOW_CONFIRM = 2;
    public static final int FLOW_REPLY = 3;
    public static final int FLOW_ERROR = 4;
    public static final int FLOW_HANDSHAKE = 5;
    public static final int FLOW_EXCEPTION = 6;
    public static final int FLOW_PING = 7;
    public static final int FLOW_PONG = 8;
    public static final int FLOW_CONFIRM_CALLBACK = 9;
    public static final int FLOW_CLOSE = 10;
    public static final int FLOW_REQUEST_AUTHORIZED = 11;
    public static final int NULL_CLEANUP = 0;
    public static final int ADD_CLEANUP = 1;
    public static final int UPDATE_CLEANUP = 2;

    private int iFlowVersion;
    private String strRequestType;
    private int iFlowType;
    private int iMessageId;
    private int iGatewayRc;
    private boolean bHasSecurity;
    private int iDataWhichFollows;
    private byte[] eciArea = null;

    static transient boolean bSpecifyASCII;
    private Callbackable calBack;

    public Object objRequestToken;
    public Locale locExchange;
    public String strServerJVM;
    public String strServerSecurityClass;
    public byte abytHandshake[];
    public boolean boolCloseHint;
    public String serverSideException;

    public CtgRequest() {
        iFlowVersion = 0x500010;
        strRequestType = null;
        iFlowType = 1;
        iMessageId = 0;
        iGatewayRc = 0;
        bHasSecurity = false;
        iDataWhichFollows = 0;
        calBack = null;
        objRequestToken = null;
        locExchange = null;
        strServerJVM = null;
        strServerSecurityClass = null;
        abytHandshake = null;
        boolCloseHint = false;
        serverSideException = null;
        strRequestType = "BASE";
    }

    protected CtgRequest(String reqType) {
        iFlowVersion = 0x500010;
        strRequestType = null;
        iFlowType = 1;
        iMessageId = 0;
        iGatewayRc = 0;
        bHasSecurity = false;
        iDataWhichFollows = 0;
        calBack = null;
        objRequestToken = null;
        locExchange = null;
        strServerJVM = null;
        strServerSecurityClass = null;
        abytHandshake = null;
        boolCloseHint = false;
        serverSideException = null;
        strRequestType = reqType;
    }

    public void setRoot(CtgRequest ctgRequest) {
        iFlowVersion = ctgRequest.iFlowVersion;
        iFlowType = ctgRequest.iFlowType;
        iMessageId = ctgRequest.iMessageId;
        iGatewayRc = ctgRequest.iGatewayRc;
        strRequestType = ctgRequest.strRequestType;
        bHasSecurity = ctgRequest.bHasSecurity;
        iDataWhichFollows = ctgRequest.iDataWhichFollows;
        objRequestToken = ctgRequest.objRequestToken;
        abytHandshake = ctgRequest.abytHandshake;
        locExchange = ctgRequest.locExchange;
        boolCloseHint = ctgRequest.boolCloseHint;
        serverSideException = ctgRequest.serverSideException;
        strServerJVM = ctgRequest.strServerJVM;
    }

    public void readObject(DataInputStream in) throws IOException {
        int i;
        if ((i = in.readInt()) != EYECATCODE) throw new IOException("Eyecat code error.");
        iFlowVersion = in.readInt();
        iFlowType = in.readInt();
        iMessageId = in.readInt();
        iGatewayRc = in.readInt();
        if (iFlowVersion == 0x10100) {
            strRequestType = readPaddedString(in, 4);
        } else {
            int j = in.readInt();
            strRequestType = readPaddedString(in, j);
        }
        if (iFlowVersion >= 0x200000)
            bHasSecurity = in.readBoolean();
        iDataWhichFollows = in.readInt();

        eciArea = new byte[iDataWhichFollows];
        in.readFully(eciArea);


/*
        if (iFlowType == 5) {  //Type: FLOW_HANDSHAKE
            String s1 = in.readUTF();
            String s2 = in.readUTF();
            String s3 = in.readUTF();
            try {
                locExchange = new Locale(s1, s2, s3);
            } catch (NoClassDefFoundError noclassdeffounderror) {
                locExchange = null;
            }
            if (iFlowVersion >= 0x301000)
                strServerJVM = in.readUTF();
            strServerSecurityClass = in.readUTF();  //Server security class

            int k = in.readInt();
            if (k > 0) {
                abytHandshake = new byte[k];
                in.readFully(abytHandshake);
            } else {
                abytHandshake = null;
            }
        } else if (iFlowType == 6) {   //Type: FLOW_EXCEPTION
            if (iFlowVersion >= 0x300000)
                boolCloseHint = in.readBoolean();
            serverSideException = in.readUTF();
        }
*/

    }


    protected String readPaddedString(DataInputStream in, int i) throws IOException {
        byte abyte0[] = new byte[i];
        in.readFully(abyte0, 0, i);
        return new String(abyte0, "ASCII");
    }


    public void writeObject(DataOutputStream out) throws IOException {
        out.writeInt(EYECATCODE);
        out.writeInt(iFlowVersion);
        out.writeInt(iFlowType);
        out.writeInt(iMessageId);
        out.writeInt(iGatewayRc);
        if (iFlowVersion == 0x10100) {
            out.writeBytes(toPaddedString(strRequestType, 4));
        } else {
            out.writeInt(strRequestType.length());
            out.writeBytes(toPaddedString(strRequestType, strRequestType.length()));
        }
        if (iFlowVersion >= 0x200000)
            out.writeBoolean(bHasSecurity);
        out.writeInt(iDataWhichFollows);

        out.write(eciArea, 0, iDataWhichFollows);
    }


    protected String toPaddedString(String s, int i) {
        StringBuilder sb = new StringBuilder(i);
        if (s != null)
            sb.append(s);
        sb.setLength(i);
        return sb.toString();
    }


/*
    protected void setContentsFromPartner(CtgRequest request)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        request.writeObject(dos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream dis = new DataInputStream(bais);
        readObject(dis);
    }
*/


    //==============
    protected int getPasswordOffset() {
        return -1;
    }
    //===============

    public static int getFlowRequest() {
        return FLOW_REQUEST;
    }

    public static int getFlowConfirm() {
        return FLOW_CONFIRM;
    }

    public static int getFlowReply() {
        return FLOW_REPLY;
    }

    public static int getFlowError() {
        return FLOW_ERROR;
    }

    public static int getFlowHandshake() {
        return FLOW_HANDSHAKE;
    }

    public static int getFlowException() {
        return FLOW_EXCEPTION;
    }

    public static int getFlowPing() {
        return FLOW_PING;
    }

    public static int getFlowPong() {
        return FLOW_PONG;
    }

    public static int getFlowConfirmCallback() {
        return FLOW_CONFIRM_CALLBACK;
    }

    public static int getFlowClose() {
        return FLOW_CLOSE;
    }

    public static int getFlowRequestAuthorized() {
        return FLOW_REQUEST_AUTHORIZED;
    }

    public static int getNullCleanup() {
        return NULL_CLEANUP;
    }

    public static int getAddCleanup() {
        return ADD_CLEANUP;
    }

    public static int getUpdateCleanup() {
        return UPDATE_CLEANUP;
    }

    public int getiFlowVersion() {
        return iFlowVersion;
    }

    public void setiFlowVersion(int iFlowVersion) {
        this.iFlowVersion = iFlowVersion;
    }

    public String getStrRequestType() {
        return strRequestType;
    }

    public void setStrRequestType(String strRequestType) {
        this.strRequestType = strRequestType;
    }

    public int getiFlowType() {
        return iFlowType;
    }

    public void setiFlowType(int iFlowType) {
        this.iFlowType = iFlowType;
    }

    public int getiMessageId() {
        return iMessageId;
    }

    public void setiMessageId(int iMessageId) {
        this.iMessageId = iMessageId;
    }

    public int getiGatewayRc() {
        return iGatewayRc;
    }

    public void setiGatewayRc(int iGatewayRc) {
        this.iGatewayRc = iGatewayRc;
    }

    public boolean isbHasSecurity() {
        return bHasSecurity;
    }

    public void setbHasSecurity(boolean bHasSecurity) {
        this.bHasSecurity = bHasSecurity;
    }

    public int getiDataWhichFollows() {
        return iDataWhichFollows;
    }

    public void setiDataWhichFollows(int iDataWhichFollows) {
        this.iDataWhichFollows = iDataWhichFollows;
    }

    public static boolean isbSpecifyASCII() {
        return bSpecifyASCII;
    }

    public static void setbSpecifyASCII(boolean bSpecifyASCII) {
        CtgRequest.bSpecifyASCII = bSpecifyASCII;
    }

    public Callbackable getCalBack() {
        return calBack;
    }

    public void setCalBack(Callbackable calBack) {
        this.calBack = calBack;
    }

    public byte[] getEciArea() {
        return eciArea;
    }

    public void setEciArea(byte[] eciArea) {
        this.eciArea = eciArea;
    }

    @Override
    public String toString() {
        return "CtgRequest{" +
                "iFlowVersion=" + iFlowVersion +
                ", strRequestType='" + strRequestType + '\'' +
                ", iFlowType=" + iFlowType +
                ", iMessageId=" + iMessageId +
                ", iGatewayRc=" + iGatewayRc +
                ", bHasSecurity=" + bHasSecurity +
                ", iDataWhichFollows=" + iDataWhichFollows +
                ", calBack=" + calBack +
                ", objRequestToken=" + objRequestToken +
                ", locExchange=" + locExchange +
                ", strServerJVM='" + strServerJVM + '\'' +
                ", strServerSecurityClass='" + strServerSecurityClass + '\'' +
                ", abytHandshake=" + Arrays.toString(abytHandshake) +
                ", boolCloseHint=" + boolCloseHint +
                ", serverSideException='" + serverSideException + '\'' +
                '}';
    }
}

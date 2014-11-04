package org.fbi.ctgproxy.oldctg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Vector;

public class ECIRequest extends GatewayRequest implements ECIReturnCodes {

    private static final String OUR_TYPE = "ECI";
    public static final int ECI_SYNC = 1;
    public static final int ECI_ASYNC = 2;
    public static final int ECI_GET_REPLY = 3;
    public static final int ECI_GET_REPLY_WAIT = 4;
    public static final int ECI_GET_SPECIFIC_REPLY = 5;
    public static final int ECI_GET_SPECIFIC_REPLY_WAIT = 6;
    public static final int ECI_STATE_SYNC = 7;
    public static final int ECI_STATE_ASYNC = 8;
    public static final int CICS_EciListSystems = 9;
    public static final int ECI_STATE_SYNC_JAVA = 10;
    public static final int ECI_STATE_ASYNC_JAVA = 11;
    public static final int ECI_SYNC_TPN = 12;
    public static final int ECI_ASYNC_TPN = 13;
    public static final int ECI_NO_EXTEND = 0;
    public static final int ECI_EXTENDED = 1;
    public static final int ECI_COMMIT = 2;
    /**
     * @deprecated Field ECI_CANCEL is deprecated
     */
    public static final int ECI_CANCEL = 2;
    public static final int ECI_BACKOUT = 4;
    public static final int ECI_STATE_IMMEDIATE = 5;
    public static final int ECI_STATE_CHANGED = 6;
    public static final int ECI_STATE_CANCEL = 7;
    public static final int ECI_LUW_NEW = 0;
    public static final int CONST_AV = 19429;
    public static final int CONST_GLOBAL_TRAN_SUPPORT = 1;
    public static final int ECI_STATUS_LENGTH = 10;
    /**
     * @deprecated Field ECI_STATUS_LENGTH_AIX is deprecated
     */
    public static final int ECI_STATUS_LENGTH_AIX = 6;
    public static final short ECI_CONNECTED_NOWHERE = 0;
    public static final short ECI_CONNECTED_TO_SERVER = 1;
    public static final short ECI_CONNECTED_TO_CLIENT = 2;
    public static final short ECI_SERVERSTATE_UNKNOWN = 0;
    public static final short ECI_SERVERSTATE_UP = 1;
    public static final short ECI_SERVERSTATE_DOWN = 2;
    public static final short ECI_CLIENTSTATE_UNKNOWN = 0;
    public static final short ECI_CLIENTSTATE_UP = 1;
    public static final short ECI_CLIENTSTATE_INAPPLICABLE = 2;
    public static final String astrConnectionType[] = {
            "ECI connected nowhere", "ECI connected to server", "ECI connected to client", "Invalid connection type"
    };
    public static final String astrCicsServerStatus[] = {
            "ECI server state unknown", "ECI server state up", "ECI server state down", "Invalid server state"
    };
    public static final String astrCicsClientStatus[] = {
            "ECI client state unknown", "ECI client state up", "ECI client state inapplicable", "Invalid argument"
    };
    public static final int CICS_ECI_SYSTEM_MAX = 8;
    public static final int CICS_ECI_DESCRIPTION_MAX = 60;
    private transient int iPasswordOffset;
    public int Call_Type;
    public int Extend_Mode;
    public int Luw_Token;
    /**
     * @deprecated Field Message_Qualifier is deprecated
     */
    public int Message_Qualifier;
    public int AV;
    public int GlobalTranSupport;
    public String Server;
    public String Userid;
    public String Password;
    public String Program;
    public String Transid;
    public int Commarea_Length;
    public byte Commarea[];
    public int Cics_Rc;
    public String Abend_Code;
    public Vector SystemList;
    public int ConnectionType;
    public int CicsServerStatus;
    public int CicsClientStatus;
    public int maxNumServers;
    public int numServersKnown;
    public int numServersReturned;
    protected int iNumOfSystems;
    protected boolean bCallbackExists;
    private static final int MAX_COMMAREA_LENGTH = 32768;
    private static final byte referenceNullArray[] = new byte[32768];
    protected boolean bCommareaLengthOut;
    protected int iCommareaLengthOut;
    protected boolean bCommareaLengthIn;
    protected int iCommareaLengthIn;
    private boolean bJNIStrippedLenIn;
    private int iJNIStrippedLenIn;
    protected short sECITimeout;
    protected boolean autoMsgQual;
    private static final String RBBASENAME = "CicsResourceBundle";
    private static ResourceWrapper rbDefault;
    private static final String astrCall_Type[] = {
            "ECI_UNKNOWN_CALL_TYPE", "ECI_SYNC", "ECI_ASYNC", "ECI_GET_REPLY", "ECI_GET_REPLY_WAIT", "ECI_GET_SPECIFIC_REPLY", "ECI_GET_SPECIFIC_REPLY_WAIT", "ECI_STATE_SYNC", "ECI_STATE_ASYNC", "CICS_EciListSystems",
            "ECI_STATE_SYNC_JAVA", "ECI_STATE_ASYNC_JAVA", "ECI_SYNC_TPN", "ECI_ASYNC_TPN"
    };
    private static final String strINVALID_CALL = "ECI_UNKNOWN_CALL_TYPE";
    private static final String astrExtend_Mode[] = {
            "ECI_NO_EXTEND", "ECI_EXTENDED", "ECI_COMMIT", "ECI_CANCEL", "ECI_BACKOUT", "ECI_STATE_IMMEDIATE", "ECI_STATE_CHANGED", "ECI_STATE_CANCEL"
    };
    private static final String strINVALID_EXTEND_MODE = "ECI_UNKNOWN_EXTEND_MODE";
    private static final String astrCT[] = {
            "ECI_CONNECTED_NOWHERE", "ECI_CONNECTED_TO_SERVER", "ECI_CONNECTED_TO_CLIENT"
    };
    private static final String strINVALID_CONNECTION_TYPE = "ECI_UNKNOWN_CONNECTION_TYPE";
    private static final String astrCSS[] = {
            "ECI_SERVERSTATE_UNKNOWN", "ECI_SERVERSTATE_UP", "ECI_SERVERSTATE_DOWN"
    };
    private static final String strINVALID_SERVER_STATE = "ECI_UNKNOWN_SERVER_STATE";
    private static final String astrCCS[] = {
            "ECI_CLIENTSTATE_UNKNOWN", "ECI_CLIENTSTATE_UP", "ECI_CLIENTSTATE_INAPPLICABLE"
    };
    private static final String strINVALID_CLIENT_STATE = "ECI_UNKNOWN_CLIENT_STATE";

    public void initialize()
            throws Exception {
        if (T.bDebug)
            T.in(this, "initialize()");
    }

    public ECIRequest() {
        super("ECI");
        iPasswordOffset = -1;
        Call_Type = 1;
        Extend_Mode = 0;
        Luw_Token = 0;
        Message_Qualifier = 0;
        AV = 0;
        GlobalTranSupport = 0;
        Server = null;
        Userid = null;
        Password = null;
        Program = null;
        Transid = null;
        Commarea_Length = 0;
        Commarea = null;
        Cics_Rc = 0;
        Abend_Code = null;
        SystemList = new Vector();
        ConnectionType = 0;
        CicsServerStatus = 0;
        CicsClientStatus = 0;
        maxNumServers = 0;
        numServersKnown = 0;
        numServersReturned = 0;
        iNumOfSystems = 0;
        bCallbackExists = false;
        bCommareaLengthOut = false;
        iCommareaLengthOut = 0;
        bCommareaLengthIn = false;
        iCommareaLengthIn = 0;
        bJNIStrippedLenIn = false;
        iJNIStrippedLenIn = 0;
        sECITimeout = 0;
        if (T.bDebug)
            T.in(this, "CT1()");
    }

    private ECIRequest(int i) {
        super("ECI");
        iPasswordOffset = -1;
        Call_Type = 1;
        Extend_Mode = 0;
        Luw_Token = 0;
        Message_Qualifier = 0;
        AV = 0;
        GlobalTranSupport = 0;
        Server = null;
        Userid = null;
        Password = null;
        Program = null;
        Transid = null;
        Commarea_Length = 0;
        Commarea = null;
        Cics_Rc = 0;
        Abend_Code = null;
        SystemList = new Vector();
        ConnectionType = 0;
        CicsServerStatus = 0;
        CicsClientStatus = 0;
        maxNumServers = 0;
        numServersKnown = 0;
        numServersReturned = 0;
        iNumOfSystems = 0;
        bCallbackExists = false;
        bCommareaLengthOut = false;
        iCommareaLengthOut = 0;
        bCommareaLengthIn = false;
        iCommareaLengthIn = 0;
        bJNIStrippedLenIn = false;
        iJNIStrippedLenIn = 0;
        sECITimeout = 0;
        if (T.bDebug)
            T.in(this, "CT2()", new Integer(i));
        Call_Type = 9;
        maxNumServers = i;
    }

    public ECIRequest(String s, String s1, String s2, String s3, byte abyte0[], int i, int j) {
        super("ECI");
        iPasswordOffset = -1;
        Call_Type = 1;
        Extend_Mode = 0;
        Luw_Token = 0;
        Message_Qualifier = 0;
        AV = 0;
        GlobalTranSupport = 0;
        Server = null;
        Userid = null;
        Password = null;
        Program = null;
        Transid = null;
        Commarea_Length = 0;
        Commarea = null;
        Cics_Rc = 0;
        Abend_Code = null;
        SystemList = new Vector();
        ConnectionType = 0;
        CicsServerStatus = 0;
        CicsClientStatus = 0;
        maxNumServers = 0;
        numServersKnown = 0;
        numServersReturned = 0;
        iNumOfSystems = 0;
        bCallbackExists = false;
        bCommareaLengthOut = false;
        iCommareaLengthOut = 0;
        bCommareaLengthIn = false;
        iCommareaLengthIn = 0;
        bJNIStrippedLenIn = false;
        iJNIStrippedLenIn = 0;
        sECITimeout = 0;
        if (T.bDebug)
            T.in(this, "CT3()", s, s1, "PASSWORD", s3, new Integer(i), new Integer(j));
        Call_Type = 1;
        Server = s;
        Userid = s1;
        Password = s2;
        Program = s3;
        Commarea = abyte0;
        if (Commarea != null)
            Commarea_Length = Commarea.length;
        Extend_Mode = i;
        Luw_Token = j;
    }

    public ECIRequest(int i, String s, String s1, String s2, String s3, String s4, byte abyte0[]) {
        super("ECI");
        iPasswordOffset = -1;
        Call_Type = 1;
        Extend_Mode = 0;
        Luw_Token = 0;
        Message_Qualifier = 0;
        AV = 0;
        GlobalTranSupport = 0;
        Server = null;
        Userid = null;
        Password = null;
        Program = null;
        Transid = null;
        Commarea_Length = 0;
        Commarea = null;
        Cics_Rc = 0;
        Abend_Code = null;
        SystemList = new Vector();
        ConnectionType = 0;
        CicsServerStatus = 0;
        CicsClientStatus = 0;
        maxNumServers = 0;
        numServersKnown = 0;
        numServersReturned = 0;
        iNumOfSystems = 0;
        bCallbackExists = false;
        bCommareaLengthOut = false;
        iCommareaLengthOut = 0;
        bCommareaLengthIn = false;
        iCommareaLengthIn = 0;
        bJNIStrippedLenIn = false;
        iJNIStrippedLenIn = 0;
        sECITimeout = 0;
        if (T.bDebug)
            T.in(this, "CT4()", new Integer(i), s, s1, "PASSWORD", s3, s4);
        Call_Type = i;
        Server = s;
        Userid = s1;
        Password = s2;
        Program = s3;
        Transid = s4;
        Commarea = abyte0;
        if (Commarea != null)
            Commarea_Length = Commarea.length;
    }

    public ECIRequest(int i, String s, String s1, String s2, String s3, String s4, byte abyte0[],
                      int j, int k, int l) {
        super("ECI");
        iPasswordOffset = -1;
        Call_Type = 1;
        Extend_Mode = 0;
        Luw_Token = 0;
        Message_Qualifier = 0;
        AV = 0;
        GlobalTranSupport = 0;
        Server = null;
        Userid = null;
        Password = null;
        Program = null;
        Transid = null;
        Commarea_Length = 0;
        Commarea = null;
        Cics_Rc = 0;
        Abend_Code = null;
        SystemList = new Vector();
        ConnectionType = 0;
        CicsServerStatus = 0;
        CicsClientStatus = 0;
        maxNumServers = 0;
        numServersKnown = 0;
        numServersReturned = 0;
        iNumOfSystems = 0;
        bCallbackExists = false;
        bCommareaLengthOut = false;
        iCommareaLengthOut = 0;
        bCommareaLengthIn = false;
        iCommareaLengthIn = 0;
        bJNIStrippedLenIn = false;
        iJNIStrippedLenIn = 0;
        sECITimeout = 0;
        if (T.bDebug)
            T.in(this, "CT5()", new Integer(i), s, s1, "PASSWORD", s3, s4, new Integer(j), new Integer(k), new Integer(l));
        Call_Type = i;
        Server = s;
        Userid = s1;
        Password = s2;
        Program = s3;
        Transid = s4;
        Commarea = abyte0;
        Commarea_Length = validateInt(j);
        validateCommareaLength();
        Extend_Mode = k;
        Luw_Token = l;
    }

    public ECIRequest(int i, String s, String s1, String s2, String s3, String s4, byte abyte0[],
                      int j, int k, int l, int i1, Callbackable callbackable) {
        super("ECI");
        iPasswordOffset = -1;
        Call_Type = 1;
        Extend_Mode = 0;
        Luw_Token = 0;
        Message_Qualifier = 0;
        AV = 0;
        GlobalTranSupport = 0;
        Server = null;
        Userid = null;
        Password = null;
        Program = null;
        Transid = null;
        Commarea_Length = 0;
        Commarea = null;
        Cics_Rc = 0;
        Abend_Code = null;
        SystemList = new Vector();
        ConnectionType = 0;
        CicsServerStatus = 0;
        CicsClientStatus = 0;
        maxNumServers = 0;
        numServersKnown = 0;
        numServersReturned = 0;
        iNumOfSystems = 0;
        bCallbackExists = false;
        bCommareaLengthOut = false;
        iCommareaLengthOut = 0;
        bCommareaLengthIn = false;
        iCommareaLengthIn = 0;
        bJNIStrippedLenIn = false;
        iJNIStrippedLenIn = 0;
        sECITimeout = 0;
        if (T.bDebug)
            T.in(this, "CT6()", new Integer(i), s, s1, "PASSWORD", s3, s4, new Integer(j), new Integer(k), new Integer(l), new Integer(i1));
        Call_Type = i;
        Server = s;
        Userid = s1;
        Password = s2;
        Program = s3;
        Transid = s4;
        Commarea = abyte0;
        Commarea_Length = validateInt(j);
        validateCommareaLength();
        Extend_Mode = k;
        Luw_Token = l;
        Message_Qualifier = i1;
        if (callbackable != null)
            setCallback(callbackable);
    }

    public int getAV() {
        return AV;
    }

    public void setAV(int i) {
        if (i == 19429)
            AV = i;
        else
            AV = 0;
    }

    public int getGlobalTranSupport() {
        return GlobalTranSupport;
    }

    public void setGlobalTranSupport(int i) {
        if (i == 1)
            GlobalTranSupport = i;
        else
            GlobalTranSupport = 0;
    }

    public static ECIRequest listSystems(int i) {
        if (T.bDebug)
            T.in(null, "ECIRequest: listSystems()", new Integer(i));
        ECIRequest ecirequest = new ECIRequest(i);
        return ecirequest;
    }

    protected int validateInt(int i) {
        if (T.bDebug)
            T.in(this, "validateInt()");
        int j = 0;
        if (i > 0)
            j = i;
        return j;
    }

    protected int allocateCommarea(int i) {
        if (T.bDebug)
            T.in(this, "allocateCommarea()", new Integer(i));
        switch (i) {
            default:
                if (i < 0)
                    i = 0;
                else if (!getReplyReturnedInvalidDataLength() && (Commarea == null || i > Commarea.length))
                    Commarea = new byte[i];
                // fall through

            case 0: // '\0'
                return i;
        }
    }

    protected int validateCommareaOutboundLength(int i) {
        if (T.bDebug)
            T.in(this, "validateCommareaOutboundLength()", new Integer(i));
        switch (i) {
            default:
                if (i < 0)
                    i = 0;
                else if (Commarea == null)
                    i = 0;
                else if (i > Commarea.length)
                    i = Commarea.length;
                // fall through

            case 0: // '\0'
                return i;
        }
    }

    protected void validateCommareaLength() {
        if (T.bDebug)
            T.in(this, "validateCommareaLength()");
        switch (Commarea_Length) {
            case 0: // '\0'
                if (Commarea != null)
                    Commarea_Length = Commarea.length;
                break;

            default:
                if (Commarea == null) {
                    Commarea_Length = 0;
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(30));
                    break;
                }
                if (Commarea_Length <= Commarea.length)
                    break;
                Commarea_Length = Commarea.length;
                if (T.bTrace)
                    T.traceln(ClientTraceMessages.getMessage(31));
                break;
        }
    }

    protected void validateCommareaLength(ECIRequest ecirequest) {
        if (T.bDebug)
            T.in(this, "validateCommareaLength()", ecirequest);
        switch (ecirequest.Commarea_Length) {
            case 0: // '\0'
                if (ecirequest.Commarea != null)
                    ecirequest.Commarea_Length = ecirequest.Commarea.length;
                break;

            default:
                if (ecirequest.Commarea == null) {
                    ecirequest.Commarea_Length = 0;
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(30));
                    break;
                }
                if (ecirequest.Commarea_Length > ecirequest.Commarea.length) {
                    ecirequest.Commarea_Length = ecirequest.Commarea.length;
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(31));
                    break;
                }
                if (ecirequest.Commarea_Length < 0)
                    ecirequest.Commarea_Length = 0;
                break;
        }
    }

    protected void localFlowOccurred() {
        T.in(this, "localFlowOccurred");
        Cics_Rc = 0;
        T.out(this, "localFlowOccurred");
    }

    protected void setContentsFromPartner(GatewayRequest gatewayrequest)
            throws IOException {
        if (T.bDebug)
            T.in(this, "setContentsFromPartner()", gatewayrequest);
        ECIRequest ecirequest = (ECIRequest) gatewayrequest;
        Call_Type = ecirequest.Call_Type;
        switch (Call_Type) {
            case 9: // '\t'
                copyEciListSystems(ecirequest);
                break;

            case 10: // '\n'
            case 11: // '\013'
                try {
                    Luw_Token = ecirequest.Luw_Token;
                    Message_Qualifier = ecirequest.Message_Qualifier;
                    Cics_Rc = ecirequest.Cics_Rc;
                    ConnectionType = ecirequest.ConnectionType;
                    CicsServerStatus = ecirequest.CicsServerStatus;
                    CicsClientStatus = ecirequest.CicsClientStatus;
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(23, getCallTypeString(), getCicsRcString(), getConnectionTypeString(), getClientStatusString(), getServerStatusString(), Luw_Token, Message_Qualifier));
                } catch (Exception exception) {
                    T.ex(this, exception);
                    String s1 = ClientMessages.getMessage(null, 61, exception);
                    throw new IOException(s1);
                }
                break;

            case 3: // '\003'
            case 4: // '\004'
                if (T.bTrace)
                    T.traceln(ClientTraceMessages.getMessage(32));
                // fall through

            case 1: // '\001'
            case 2: // '\002'
            case 5: // '\005'
            case 6: // '\006'
            case 7: // '\007'
            case 8: // '\b'
            case 12: // '\f'
            case 13: // '\r'
                try {
                    Luw_Token = ecirequest.Luw_Token;
                    Message_Qualifier = ecirequest.Message_Qualifier;
                    Cics_Rc = ecirequest.Cics_Rc;
                    Abend_Code = trimString(ecirequest.Abend_Code);
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(22, getCallTypeString(), getCicsRcString(), Abend_Code, Luw_Token, Message_Qualifier));
                    Commarea_Length = validateInt(ecirequest.Commarea_Length);
                    setCommareaInboundLength(ecirequest.isCommareaInboundLength());
                    setInboundDataLength(ecirequest.isInboundDataLength());
                    if (isCommareaInboundLength() || isInboundDataLength())
                        setContentsWithInboundLength(ecirequest);
                    else
                        setContentsWithoutInboundLength(ecirequest);
                } catch (Exception exception1) {
                    T.ex(this, exception1);
                    String s2 = ClientMessages.getMessage(null, 61, exception1);
                    throw new IOException(s2);
                }
                break;

            default:
                String s = ClientTraceMessages.getMessage(71);
                if (T.bTrace)
                    T.traceln(s);
                throw new IOException(s);
        }
        if (T.bDebug)
            T.out(this, "setContentsFromPartner()");
    }

    protected void setContentsWithInboundLength(ECIRequest ecirequest)
            throws IOException {
        if (T.bDebug)
            T.in(this, "setContentsWithInboundLength()", ecirequest);
        int i;
        if (ecirequest.isCommareaInboundLength())
            i = validateInt(ecirequest.getCommareaInboundLength());
        else
            i = validateInt(ecirequest.getInboundDataLength());
        if (T.bTrace)
            T.traceln(ClientTraceMessages.getMessage(30, i));
        if (i > 0) {
            if (i > Commarea_Length && T.bTrace)
                T.traceln(ClientTraceMessages.getMessage(35));
            int j;
            if (ecirequest.isInboundDataLength())
                j = allocateCommarea(Commarea_Length);
            else
                i = allocateCommarea(i);
            if (ServerECIRequestWontSendCommarea(getVersion()))
                T.ln(this, "Inbound length + no commarea sent");
            else if (ecirequest.Commarea != null) {
                int k = Math.min(ecirequest.Commarea.length, i);
                System.arraycopy(ecirequest.Commarea, 0, Commarea, 0, k);
            }
        }
        if (Commarea != null && i < Commarea.length)
            nullPad(Commarea, i);
        if (ecirequest.isCommareaInboundLength())
            setCommareaInboundLength(i);
        else
            setInboundDataLength(i);
        if (T.bDebug)
            T.out(this, "setContentsWithInboundLength()");
    }

    protected void setContentsWithoutInboundLength(ECIRequest ecirequest)
            throws IOException {
        if (T.bDebug)
            T.in(this, "setContentsWithoutInboundLength()", ecirequest);
        if (getReplyReturnedInvalidDataLength()) {
            Commarea_Length = ecirequest.Commarea_Length;
            if (T.getLinesOn())
                T.ln(this, "ECI_ERR_INVALID_DATA_LENGTH: Commarea_Length = {0}", new Integer(Commarea_Length));
        } else if (ecirequest.Commarea_Length <= 0 || ecirequest.Commarea == null) {
            Commarea_Length = 0;
            if (T.bTrace)
                T.traceln(ClientTraceMessages.getMessage(28, Commarea_Length));
        } else {
            if (ecirequest.Commarea_Length > ecirequest.Commarea.length)
                Commarea_Length = ecirequest.Commarea.length;
            else
                Commarea_Length = ecirequest.Commarea_Length;
            Commarea_Length = allocateCommarea(Commarea_Length);
            if (ServerECIRequestWontSendCommarea(getVersion()))
                T.ln(this, "Inbound length + no commarea sent");
            else
                System.arraycopy(ecirequest.Commarea, 0, Commarea, 0, ecirequest.Commarea_Length);
            T.hexDump(this, Commarea, "Commarea Inbound");
        }
        if (T.bDebug)
            T.out(this, "setContentsWithoutInboundLength()");
    }

    private String trimString(String s) {
        if (T.bDebug)
            T.in(this, "trimString()", s);
        String s1 = null;
        if (s != null) {
            s1 = s.trim();
            if (s1.length() == 0)
                s1 = null;
        }
        if (T.bDebug)
            if (s1 == null) {
                T.out(this, "trimString() = null");
            } else {
                String s2 = "\"" + s1 + "\"";
                T.out(this, "trimString()", s2);
            }
        return s1;
    }

    public ECIRequest getStatus(String s) {
        if (T.bDebug)
            T.in(this, "getStatus()", s);
        Call_Type = 10;
        Extend_Mode = 5;
        Server = s;
        return this;
    }

    public ECIRequest getStatus(String s, int i, Callbackable callbackable) {
        if (T.bDebug)
            T.in(this, "getStatus()", s, new Integer(i), callbackable);
        Call_Type = 11;
        Extend_Mode = 5;
        Server = s;
        Message_Qualifier = i;
        if (callbackable != null)
            setCallback(callbackable);
        return this;
    }

    public int getCallType() {
        if (T.bDebug)
            T.out(this, "getCallType()", Call_Type);
        return Call_Type;
    }

    public String getCallTypeString() {
        String s = astrCall_Type[0];
        if (Call_Type < astrCall_Type.length)
            s = astrCall_Type[Call_Type];
        if (T.bDebug)
            T.out(this, "getCallTypeString()", s);
        return s;
    }

    public int getExtendMode() {
        if (T.bDebug)
            T.out(this, "getExtendMode()", Extend_Mode);
        return Extend_Mode;
    }

    public String getExtendModeString() {
        String s = "ECI_UNKNOWN_EXTEND_MODE";
        if (Extend_Mode < astrExtend_Mode.length)
            s = astrExtend_Mode[Extend_Mode];
        if (T.bDebug)
            T.out(this, "getExtendModeString()", s);
        return s;
    }

    public String getConnectionTypeString() {
        String s = "ECI_UNKNOWN_CONNECTION_TYPE";
        if (ConnectionType < astrCT.length)
            s = astrCT[ConnectionType];
        if (T.bDebug)
            T.out(this, "getConnectionTypeString()", s);
        return s;
    }

    public String stringConnectionType(int i) {
        if (T.bDebug)
            T.in(this, "stringConnectionType()", new Integer(i));
        String s = astrConnectionType[3];
        switch (i) {
            case 0: // '\0'
                s = astrConnectionType[0];
                break;

            case 1: // '\001'
                s = astrConnectionType[1];
                break;

            case 2: // '\002'
                s = astrConnectionType[2];
                break;
        }
        return s;
    }

    public String getServerStatusString() {
        String s = "ECI_UNKNOWN_SERVER_STATE";
        if (CicsServerStatus < astrCSS.length)
            s = astrCSS[CicsServerStatus];
        if (T.bDebug)
            T.out(this, "getServerStatusString()", s);
        return s;
    }

    public String stringServerStatus(int i) {
        if (T.bDebug)
            T.in(this, "stringServerStatus()", new Integer(i));
        String s = astrCicsServerStatus[3];
        switch (i) {
            case 0: // '\0'
                s = astrCicsServerStatus[0];
                break;

            case 1: // '\001'
                s = astrCicsServerStatus[1];
                break;

            case 2: // '\002'
                s = astrCicsServerStatus[2];
                break;
        }
        return s;
    }

    public String getClientStatusString() {
        String s = "ECI_UNKNOWN_CLIENT_STATE";
        if (CicsClientStatus < astrCCS.length)
            s = astrCCS[CicsClientStatus];
        if (T.bDebug)
            T.out(this, "getClientStatusString()", s);
        return s;
    }

    public String stringClientStatus(int i) {
        if (T.bDebug)
            T.in(this, "stringClientStatus()", new Integer(i));
        String s = astrCicsClientStatus[3];
        switch (i) {
            case 0: // '\0'
                s = astrCicsClientStatus[0];
                break;

            case 1: // '\001'
                s = astrCicsClientStatus[1];
                break;

            case 2: // '\002'
                s = astrCicsClientStatus[2];
                break;
        }
        return s;
    }

    public int getRc() {
        int i = 0;
        i = super.getRc();
        if (i == 0)
            i = Cics_Rc;
        if (T.bDebug)
            T.out(this, "getRc()", i);
        return i;
    }

    public String getRcString() {
        String s = null;
        int i = getGatewayRc();
        if (i == 0)
            s = getCicsRcString();
        else
            s = getGatewayRcString();
        if (T.bDebug)
            T.out(this, "getRcString", s);
        return s;
    }

    public int getCicsRc() {
        if (T.bDebug)
            T.out(this, "getCicsRc()", Cics_Rc);
        return Cics_Rc;
    }

    public String getCicsRcString() {
        String s = "ECI_UNKNOWN_CICS_RC";
        int i = Math.abs(Cics_Rc);
        if (i < 1000) {
            if (i < ECIReturnCodes.astrCics_Rc.length)
                s = ECIReturnCodes.astrCics_Rc[i];
        } else {
            i -= 1000;
            if (i < ECIReturnCodes.astrCics_Rc2.length)
                s = ECIReturnCodes.astrCics_Rc2[i];
        }
        if (T.bDebug)
            T.out(this, "getCicsRcString()", s);
        return s;
    }

    public void setCallback(Callbackable callbackable) {
        if (T.bDebug)
            T.in(this, "setCallback()", callbackable);
        super.setCallback(callbackable);
        bCallbackExists = callbackable != null;
    }

    public boolean isCallback() {
        if (T.bDebug)
            T.out(this, "isCallback()", bCallbackExists);
        return bCallbackExists;
    }

    public void setCommareaOutboundLength(boolean flag) {
        if (T.bDebug)
            T.in(this, "setCommareaOutboundLength()", new Boolean(flag));
        bCommareaLengthOut = flag;
    }

    public void setCommareaOutboundLength(int i)
            throws IllegalArgumentException {
        if (T.bDebug)
            T.in(this, "setCommareaOutboundLength()", new Integer(i));
        if (i < 0) {
            String s = ClientTraceMessages.getMessage(72);
            if (T.bTrace)
                T.traceln(s);
            throw new IllegalArgumentException(s);
        } else {
            bCommareaLengthOut = true;
            iCommareaLengthOut = i;
            return;
        }
    }

    public boolean isCommareaOutboundLength() {
        if (T.bDebug)
            T.out(this, "isCommareaOutboundLength()", bCommareaLengthOut);
        return bCommareaLengthOut;
    }

    public int getCommareaOutboundLength() {
        if (T.bDebug)
            T.out(this, "getCommareaOutboundLength()", iCommareaLengthOut);
        return iCommareaLengthOut;
    }

    public void setCommareaInboundLength(boolean flag) {
        if (T.bDebug)
            T.in(this, "setCommareaInboundLength()", new Boolean(flag));
        bCommareaLengthIn = flag;
    }

    public void setCommareaInboundLength(int i)
            throws IllegalArgumentException {
        if (T.bDebug)
            T.in(this, "setCommareaInboundLength()", new Integer(i));
        if (i < 0) {
            String s = ClientTraceMessages.getMessage(74);
            if (T.bTrace)
                T.traceln(s);
            throw new IllegalArgumentException(s);
        } else {
            bCommareaLengthIn = true;
            iCommareaLengthIn = i;
            return;
        }
    }

    public boolean isCommareaInboundLength() {
        if (T.bDebug)
            T.out(this, "isCommareaInboundLength()", bCommareaLengthIn);
        return bCommareaLengthIn;
    }

    public int getCommareaInboundLength() {
        if (T.bDebug)
            T.out(this, "getCommareaInboundLength()", iCommareaLengthIn);
        return iCommareaLengthIn;
    }

    public boolean isInboundDataLength() {
        if (T.bDebug)
            T.out(this, "isInboundDataLength()", bJNIStrippedLenIn);
        return bJNIStrippedLenIn;
    }

    public int getInboundDataLength() {
        if (T.bDebug)
            T.out(this, "getInboundDataLength()", iJNIStrippedLenIn);
        return iJNIStrippedLenIn;
    }

    protected void setInboundDataLength(boolean flag) {
        if (T.bDebug)
            T.out(this, "setInboundDataLength()", flag);
        bJNIStrippedLenIn = flag;
    }

    protected void setInboundDataLength(int i) {
        if (T.bDebug)
            T.out(this, "setInboundDataLength()", i);
        bJNIStrippedLenIn = true;
        iJNIStrippedLenIn = i;
    }

    public short getECITimeout() {
        if (T.bDebug)
            T.out(this, "getECITimeout()", sECITimeout);
        return sECITimeout;
    }

    public void setECITimeout(short word0)
            throws IllegalArgumentException {
        if (T.bDebug)
            T.in(this, "setECITimeout()", new Integer(word0));
        if (word0 < 0) {
            String s = ClientTraceMessages.getMessage(73);
            if (T.bTrace)
                T.traceln(s);
            throw new IllegalArgumentException(s);
        } else {
            sECITimeout = word0;
            return;
        }
    }

    public boolean isTPNTransid() {
        if (T.bDebug)
            T.in(this, "isTPNTransid()");
        return Call_Type == 12 || Call_Type == 13;
    }

    public void setAutoMsgQual(boolean flag) {
        autoMsgQual = flag;
    }

    public boolean isAutoMsgQual() {
        return autoMsgQual;
    }

    public int getMessageQualifier() {
        if (T.bDebug)
            T.out(this, "getMessageQualifier()", Message_Qualifier);
        return Message_Qualifier;
    }

    public void setMessageQualifier(int i)
            throws IllegalArgumentException {
        if (T.bDebug)
            T.in(this, "setMessageQualifier()", new Integer(i));
        if (i < -32767 || i > 32767) {
            String s = ClientTraceMessages.getMessage(37);
            if (T.bTrace)
                T.traceln(s);
            throw new IllegalArgumentException(s);
        } else {
            Message_Qualifier = i;
            return;
        }
    }

    public void writeObject(DataOutputStream dataoutputstream)
            throws IOException {
        if (T.bDebug)
            T.in(this, "writeObject()", dataoutputstream);
        boolean flag = T.bTrace;
        iPasswordOffset = -1;
        Cics_Rc = 0;
        dataoutputstream.writeInt(Call_Type);
        try {
            switch (Call_Type) {
                case 9: // '\t'
                    if (flag)
                        T.traceln(ClientTraceMessages.getMessage(24, maxNumServers));
                    dataoutputstream.writeInt(maxNumServers);
                    break;

                case 10: // '\n'
                case 11: // '\013'
                    if (flag)
                        T.traceln(ClientTraceMessages.getMessage(36, getCallTypeString(), Server));
                    dataoutputstream.writeInt(Message_Qualifier);
                    dataoutputstream.writeBoolean(bCallbackExists);
                    dataoutputstream.writeBytes(toPaddedString(Server, 8));
                    break;

                case 3: // '\003'
                case 4: // '\004'
                    if (flag)
                        T.traceln(ClientTraceMessages.getMessage(32));
                    // fall through

                case 1: // '\001'
                case 2: // '\002'
                case 5: // '\005'
                case 6: // '\006'
                case 7: // '\007'
                case 8: // '\b'
                case 12: // '\f'
                case 13: // '\r'
                    dataoutputstream.writeInt(Extend_Mode);
                    dataoutputstream.writeInt(Luw_Token);
                    dataoutputstream.writeInt(Message_Qualifier);
                    dataoutputstream.writeBoolean(bCallbackExists);
                    dataoutputstream.writeBytes(toPaddedString(Server, 8));
                    dataoutputstream.writeBytes(toPaddedString(Userid, 16));
                    iPasswordOffset = dataoutputstream.size();
                    dataoutputstream.writeBytes(toPaddedString(Password, 16, true));
                    dataoutputstream.writeBytes(toPaddedString(Program, 8));
                    dataoutputstream.writeBytes(toPaddedString(Transid, 4));
                    int i = writeCommareaLengths(dataoutputstream);
                    if (i > 0)
                        if (ECIRequestWontSendCommarea(getVersion()))
                            T.ln(this, "No commarea sent to ServerECIRequest");
                        else
                            dataoutputstream.write(Commarea, 0, i);
                    if (flag) {
                        T.traceln(ClientTraceMessages.getMessage(21, getCallTypeString(), Server, Program, Transid, getExtendModeString(), Luw_Token, Message_Qualifier, bCallbackExists));
                        T.traceln(ClientTraceMessages.getMessage(28, Commarea_Length));
                    }
                    break;

                default:
                    String s = ClientTraceMessages.getMessage(71);
                    if (T.bTrace)
                        T.traceln(s);
                    throw new IOException(s);
            }
            dataoutputstream.writeShort(getECITimeout());
            dataoutputstream.writeBoolean(isAutoMsgQual());
        } catch (IOException ioexception) {
            T.ex(this, ioexception);
            String s1 = ClientMessages.getMessage(null, 68, ioexception);
            throw new IOException(s1);
        }
        if (T.bDebug)
            T.out(this, "writeObject()");
    }

    public void readObject(DataInputStream datainputstream)
            throws IOException {
        if (T.bDebug)
            T.in(this, "readObject()", datainputstream);
        boolean flag = T.bTrace;
        boolean flag1 = getVersion() >= 0x401000;
        Call_Type = datainputstream.readInt();
        switch (Call_Type) {
            case 9: // '\t'
                setEciListSystems(datainputstream);
                break;

            case 10: // '\n'
            case 11: // '\013'
                try {
                    Luw_Token = datainputstream.readInt();
                    Message_Qualifier = datainputstream.readInt();
                    Cics_Rc = datainputstream.readInt();
                    ConnectionType = datainputstream.readShort();
                    CicsServerStatus = datainputstream.readShort();
                    CicsClientStatus = datainputstream.readShort();
                    if (flag)
                        T.traceln(ClientTraceMessages.getMessage(23, getCallTypeString(), getCicsRcString(), getConnectionTypeString(), getClientStatusString(), getServerStatusString(), Luw_Token, Message_Qualifier));
                } catch (IOException ioexception) {
                    T.ex(this, ioexception);
                    String s1 = ClientMessages.getMessage(null, 55, ioexception);
                    throw new IOException(s1);
                }
                break;

            case 3: // '\003'
            case 4: // '\004'
                if (flag)
                    T.traceln(ClientTraceMessages.getMessage(32));
                // fall through

            case 1: // '\001'
            case 2: // '\002'
            case 5: // '\005'
            case 6: // '\006'
            case 7: // '\007'
            case 8: // '\b'
            case 12: // '\f'
            case 13: // '\r'
                try {
                    Luw_Token = datainputstream.readInt();
                    Message_Qualifier = datainputstream.readInt();
                    Cics_Rc = datainputstream.readInt();
                    Abend_Code = readPaddedString(datainputstream, 4);
                    Commarea_Length = datainputstream.readInt();
                    boolean flag2 = datainputstream.readBoolean();
                    int i;
                    if (flag2) {
                        boolean flag3 = datainputstream.readBoolean();
                        i = datainputstream.readInt();
                        if (flag3) {
                            int j = allocateCommarea(Commarea_Length);
                            setInboundDataLength(i);
                        } else {
                            i = allocateCommarea(i);
                            setCommareaInboundLength(i);
                        }
                        if (flag)
                            T.traceln(ClientTraceMessages.getMessage(30, i));
                    } else {
                        Commarea_Length = allocateCommarea(Commarea_Length);
                        i = Commarea_Length;
                    }
                    if (ServerECIRequestWontSendCommarea(getVersion())) {
                        T.ln(this, "ServerECIRequest will not have sent commarea.");
                    } else {
                        datainputstream.readFully(Commarea, 0, i);
                        if (Commarea != null && i < Commarea.length)
                            nullPad(Commarea, i);
                    }
                    if (flag) {
                        T.traceln(ClientTraceMessages.getMessage(22, getCallTypeString(), getCicsRcString(), Abend_Code, Luw_Token, Message_Qualifier));
                        T.traceln(ClientTraceMessages.getMessage(28, Commarea_Length));
                    }
                    T.hexDump(this, Commarea, "Inbound Commarea");
                } catch (IOException ioexception1) {
                    T.ex(this, ioexception1);
                    String s2 = ClientMessages.getMessage(null, 55, ioexception1);
                    throw new IOException(s2);
                }
                break;

            default:
                String s = ClientTraceMessages.getMessage(71);
                if (T.bTrace)
                    T.traceln(s);
                throw new IOException(s);
        }
        if (T.bDebug)
            T.out(this, "readObject()");
    }

    private int writeCommareaLengths(DataOutputStream dataoutputstream)
            throws IOException {
        if (T.bDebug)
            T.in(this, "writeCommareaLengths()", dataoutputstream);
        boolean flag = T.bTrace;
        Commarea_Length = validateInt(Commarea_Length);
        if (!isCommareaOutboundLength())
            validateCommareaLength();
        dataoutputstream.writeInt(Commarea_Length);
        dataoutputstream.writeBoolean(isCommareaOutboundLength());
        int i;
        if (isCommareaOutboundLength()) {
            iCommareaLengthOut = validateCommareaOutboundLength(iCommareaLengthOut);
            if (flag) {
                T.traceln(ClientTraceMessages.getMessage(29, iCommareaLengthOut));
                if (iCommareaLengthOut > Commarea_Length)
                    T.traceln(ClientTraceMessages.getMessage(34));
            }
            i = iCommareaLengthOut;
            dataoutputstream.writeInt(i);
        } else {
            i = Commarea_Length;
        }
        dataoutputstream.writeBoolean(isCommareaInboundLength());
        if (isCommareaInboundLength()) {
            iCommareaLengthIn = validateInt(iCommareaLengthIn);
            if (flag) {
                T.traceln(ClientTraceMessages.getMessage(30, iCommareaLengthIn));
                if (iCommareaLengthIn > Commarea_Length)
                    T.traceln(ClientTraceMessages.getMessage(35));
            }
            dataoutputstream.writeInt(iCommareaLengthIn);
        }
        return i;
    }

    private void setEciListSystems(DataInputStream datainputstream)
            throws IOException {
        if (T.bDebug)
            T.in(this, "setECIListSystems()", datainputstream);
        boolean flag = false;
        Object obj = null;
        Object obj1 = null;
        try {
            numServersKnown = datainputstream.readInt();
            Cics_Rc = datainputstream.readInt();
            int i = datainputstream.readInt();
            SystemList.removeAllElements();
            if (i > 0) {
                numServersReturned = i / 70;
                for (int j = 0; j < numServersReturned; j++) {
                    String s = readPaddedString(datainputstream, 9);
                    String s2 = s.trim();
                    SystemList.addElement(s2);
                    if (T.bDebug) {
                        s = "\"" + s2 + "\"";
                        T.ln(this, "System = {0}", s);
                    }
                    s = readPaddedString(datainputstream, 61);
                    if (s == null)
                        s2 = "";
                    else
                        s2 = s.trim();
                    SystemList.addElement(s2);
                    if (T.bDebug) {
                        String s1 = "\"" + s2 + "\"";
                        T.ln(this, "Description = {0}", s1);
                    }
                }

            } else {
                numServersReturned = 0;
            }
        } catch (IOException ioexception) {
            T.ex(this, ioexception);
            String s3 = ClientMessages.getMessage(null, 55, ioexception);
            throw new IOException(s3);
        }
        if (T.bTrace)
            T.traceln(ClientTraceMessages.getMessage(25, getCicsRcString(), numServersKnown, numServersReturned));
        if (T.bDebug)
            T.out(this, "setECIListSystems()");
    }

    private void copyEciListSystems(ECIRequest ecirequest)
            throws IOException {
        if (T.bDebug)
            T.in(this, "copyECIListSystems()", ecirequest);
        boolean flag = false;
        Object obj = null;
        Object obj1 = null;
        byte abyte0[] = null;
        try {
            numServersKnown = ecirequest.iNumOfSystems;
            Cics_Rc = ecirequest.Cics_Rc;
            int i;
            if (numServersKnown <= 0 || ecirequest.Commarea_Length <= 0 || ecirequest.Commarea == null)
                i = 0;
            else if (ecirequest.Commarea_Length >= ecirequest.Commarea.length) {
                i = ecirequest.Commarea.length;
                abyte0 = ecirequest.Commarea;
            } else {
                i = ecirequest.Commarea_Length;
                abyte0 = new byte[ecirequest.Commarea_Length];
                System.arraycopy(ecirequest.Commarea, 0, abyte0, 0, ecirequest.Commarea_Length);
            }
            SystemList.removeAllElements();
            if (i > 0) {
                numServersReturned = i / 70;
                int j = 0;
                int k = 0;
                for (; j < numServersReturned; j++) {
                    String s;
                    if (bSpecifyASCII) {
                        s = new String(abyte0, k, 9, "ASCII");
                    } else {
                        T.ln(this, "JVM doesn't support ASCII codepage");
                        s = new String(abyte0, 0, k, 9);
                    }
                    String s2 = s.trim();
                    SystemList.addElement(s2);
                    k += 9;
                    if (T.bDebug) {
                        s = "\"" + s2 + "\"";
                        T.ln(this, "System = {0}", s);
                    }
                    if (bSpecifyASCII) {
                        s = new String(abyte0, k, 61, "ASCII");
                    } else {
                        T.ln(this, "JVM doesn't support ASCII codepage");
                        s = new String(abyte0, 0, k, 61);
                    }
                    s2 = s.trim();
                    SystemList.addElement(s2);
                    k += 61;
                    if (T.bDebug) {
                        String s1 = "\"" + s2 + "\"";
                        T.ln(this, "Description = {0}", s1);
                    }
                }

            } else {
                numServersReturned = 0;
            }
        } catch (Exception exception) {
            T.ex(this, exception);
            String s3 = ClientMessages.getMessage(null, 61, exception);
            throw new IOException(s3);
        }
        if (T.bTrace)
            T.traceln(ClientTraceMessages.getMessage(25, getCicsRcString(), numServersKnown, numServersReturned));
        if (T.bDebug)
            T.out(this, "copyECIListSystems()");
    }

    protected int getPasswordOffset() {
        if (T.bDebug)
            T.out(this, "getPasswordOffset = " + iPasswordOffset);
        return iPasswordOffset;
    }

    protected boolean ECIRequestWontSendCommarea(int i) {
        if (i >= 0x401000)
            return Call_Type == 5 || Call_Type == 6 || Call_Type == 3 || Call_Type == 4 || Extend_Mode == 2 || Extend_Mode == 4;
        else
            return false;
    }

    protected boolean ServerECIRequestWontSendCommarea(int i) {
        if (i >= 0x401000) {
            if (getReplyReturnedError())
                return true;
            else
                return Extend_Mode == 2 || Extend_Mode == 4 || !bCallbackExists && Call_Type == 2 || !bCallbackExists && Call_Type == 13;
        } else {
            return false;
        }
    }

    protected boolean getReplyReturnedInvalidDataLength() {
        return Cics_Rc == -1 && (Call_Type == 3 || Call_Type == 4 || Call_Type == 5 || Call_Type == 6);
    }

    private boolean getReplyReturnedError() {
        return Cics_Rc != 0 && (Call_Type == 3 || Call_Type == 4 || Call_Type == 5 || Call_Type == 6);
    }

    final void nullPad(byte abyte0[], int i) {
        int j = abyte0.length - i;
        if (j <= referenceNullArray.length)
            System.arraycopy(referenceNullArray, 0, abyte0, i, j);
        else
            System.arraycopy(referenceNullArray, 0, abyte0, i, referenceNullArray.length);
    }

    static {
        if (T.bDebug)
            T.in(null, "ECIRequest: static initializer");
        try {
            rbDefault = ResourceWrapper.getBundle("org.fbi.ctgproxy.CicsResourceBundle");
        } catch (MissingResourceException missingresourceexception) {
            T.ex(null, missingresourceexception);
            throw missingresourceexception;
        }
    }
}

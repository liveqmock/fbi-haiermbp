package org.fbi.ctgproxy.oldctg;

import org.fbi.ctgproxy.util.OSInfo;
import org.fbi.ctgproxy.util.OSVersion;

import java.io.*;
import java.util.Hashtable;
import java.util.MissingResourceException;

public class ServerECIRequest extends ECIRequest implements ServerGatewayRequest, Cloneable {
    private static String strOS;
    private static String strJVMVersion;
    private static String strJVMDefaultEnc;
    private static boolean boolOS = false;
    private static boolean boolJVMVersion = false;
    private static boolean boolJVMDefaultEnc = false;
    private static final String strCicsClientCp = "Cp437";
    private static final String zOSabendCodePage = "Cp1047";
    public static final String strECIGenericReplies = "gateway.ecigenericreplies";
    private static final String strUOWValidation = "gateway.uowvalidation";
    private static boolean uowValDisabled = false;
    private static final String strMsgQualValidation = "gateway.msgqualvalidation";
    private static boolean mqValDisabled = false;
    private static final String strRpcClient = "gateway.rpcclient";
    private static boolean boolLittleEndian = true;
    private static boolean boolRpcClient = false;
    private static boolean boolMVS = false;
    private static boolean boolNativeLibraryLoaded = false;
    private boolean boolLocalGateway;
    private static boolean boolSetLocale = false;
    private static boolean boolInitJNI = false;
    private static boolean boolInitTrace = false;
    private static Object objInitTraceMonitor = new Object();
    private boolean boolConfirmationRequired;
    private boolean boolExecuteOK;
    private int iSave_Extend_Mode;
    private int iSave_Luw_Token;
    private int cleanupLogicalWorkState;
    private boolean isNowCleanupRequest;
    private Integer iobjCMIndex;
    private static Hashtable usedLUWs = new Hashtable(200);
    private int cleanupAction;
    private static final long EXCI_SYSTEM_ERROR = 16L;
    private static final long INCORRECT_SVC_LEVEL = 627L;
    private static final int EXTENDED_NO = 0;
    private static final int EXTENDED_NEW = 1;
    private static final int EXTENDED_YES = 2;
    private static final int EXTENDED_LAST = 3;
    private static final int GLOBAL_TRAN_SUPPORT_FLAG = 1;
    private static final int AV_FLAG = 2;
    private static final String RBBASENAME = "org.fbi.ctgproxy.CicsResourceBundle";
    private static boolean boolResourceBundle = false;
    private static ResourceWrapper rbDefault = null;

    public boolean isLocalMode() {
        return boolLocalGateway;
    }

    public void setLocalMode(boolean flag) {
        boolLocalGateway = flag;
    }

    private static String getDefaultCpForJVM()
            throws Exception {
        if (T.bDebug)
            T.in(null, "ServerECIRequest: getDefaultCpForJVM()");
        String s = "";
        byte abyte0[] = new byte[10];
        try {
            ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
            InputStreamReader inputstreamreader = new InputStreamReader(bytearrayinputstream);
            s = inputstreamreader.getEncoding();
        } catch (Throwable throwable) {
            T.ex(null, throwable);
            String s1 = ClientTraceMessages.getMessage(20, throwable);
            if (T.bTrace)
                T.traceln(s1);
            throw new Exception(s1);
        }
        if (T.bDebug)
            T.out(null, "ServerECIRequest: getDefaultCpForJVM()", s);
        return s;
    }

    private native void CcicsInit(String s, long al[], int i);

    private native void CcicsECI(String s, String s1, String s2, String s3, String s4, String s5, byte abyte0[],
                                 byte abyte1[], int ai[]);

    private native void CcicsLIST(String s, byte abyte0[], int ai[]);

    private native void CcicsTerm(String s);

    public ServerECIRequest() {
        boolLocalGateway = false;
        boolConfirmationRequired = false;
        boolExecuteOK = false;
        iSave_Extend_Mode = 0;
        iSave_Luw_Token = 0;
        cleanupLogicalWorkState = 0;
        isNowCleanupRequest = false;
        if (T.bDebug)
            T.in(this, "CT()");
    }

    public void initialize()
            throws Exception {
        if (T.bDebug)
            T.in(this, "initialize()");
        mqValDisabled = System.getProperty("gateway.msgqualvalidation", "on").toLowerCase().equals("off");
        uowValDisabled = System.getProperty("gateway.uowvalidation", "on").toLowerCase().equals("off");
        boolean flag = T.bTrace;
        try {
            if (!boolResourceBundle) {
                rbDefault = ResourceWrapper.getBundle("com.ibm.ctg.client.CicsResourceBundle");
                boolResourceBundle = true;
                if (flag)
                    T.traceln(ClientTraceMessages.getMessage(13, "com.ibm.ctg.client.CicsResourceBundle"));
            }
            if (!boolOS) {
                strOS = System.getProperty("os.name");
                OSInfo osinfo = OSVersion.OPERATING_SYSTEM;
                if (osinfo.equals(OSInfo.AIX) || osinfo.equals(OSInfo.LINUX_ZSERIES) || osinfo.equals(OSInfo.LINUX_PPC) || osinfo.equals(OSInfo.HPUX) || osinfo.equals(OSInfo.SOLARIS)) {
                    boolLittleEndian = false;
                    if (System.getProperty("gateway.rpcclient", "no").toLowerCase().equals("yes"))
                        boolRpcClient = true;
                } else if (osinfo.equals(OSInfo.ZOS))
                    boolMVS = true;
                boolOS = true;
                if (flag)
                    T.traceln(ClientTraceMessages.getMessage(1, strOS));
            }
            if (!boolJVMVersion) {
                strJVMVersion = System.getProperty("java.version");
                boolJVMVersion = true;
                if (flag)
                    T.traceln(ClientTraceMessages.getMessage(2, strJVMVersion));
            }
            if (!boolJVMDefaultEnc) {
                strJVMDefaultEnc = getDefaultCpForJVM();
                boolJVMDefaultEnc = true;
                if (flag)
                    T.traceln(ClientTraceMessages.getMessage(12, strJVMDefaultEnc));
            }
            if (!boolNativeLibraryLoaded) {
                System.loadLibrary("ctgjni");
                boolNativeLibraryLoaded = true;
                if (flag)
                    T.traceln(ClientTraceMessages.getMessage(3));
            }
            if (!boolInitJNI)
                initializeJNI();
            if (!boolMVS && !boolInitTrace)
                initTrace();
        } catch (MissingResourceException missingresourceexception) {
            T.ex(this, missingresourceexception);
            throw missingresourceexception;
        } catch (UnsatisfiedLinkError unsatisfiedlinkerror) {
            T.ex(this, unsatisfiedlinkerror);
            String s = ClientTraceMessages.getMessage(4, unsatisfiedlinkerror);
            if (T.bTrace)
                T.traceln(s);
            throw new Exception(s);
        } catch (Throwable throwable) {
            T.ex(null, throwable);
            String s1 = ClientTraceMessages.getMessage(17, throwable);
            if (T.bTrace)
                T.traceln(s1);
            throw new Exception(s1);
        }
        if (T.bDebug)
            T.out(this, "initialize()");
    }

    public void initializeJNI()
            throws Exception {
        long al[] = new long[6];
        if (T.bDebug)
            T.in(this, "initializeJNI()");
        if (T.bTrace)
            T.traceln(ClientTraceMessages.getMessage(99, "CcicsInit (initializeJNI)"));
        if (isLocalMode())
            CcicsInit(Thread.currentThread().getName(), al, 1);
        else
            CcicsInit(Thread.currentThread().getName(), al, 0);
        if (T.bTrace)
            T.traceln(ClientTraceMessages.getMessage(92, "CcicsInit (initializeJNI)"));
        if (T.bDebug)
            T.out(this, "initializeJNI()");
        boolInitJNI = true;
    }

    public void initTrace()
            throws Exception {
        long al[] = new long[6];
        if (T.bDebug)
            T.in(null, "ServerECIRequest: initTrace()");
        synchronized (objInitTraceMonitor) {
            if (!boolSetLocale && !boolInitTrace) {
                if (T.bTrace)
                    T.traceln(ClientTraceMessages.getMessage(99, "CcicsInit (initTrace)"));
                boolSetLocale = true;
                boolInitTrace = true;
            }
        }
        if (T.bDebug)
            T.out(null, "ServerECIRequest: initTrace()");
    }

    private void convertStatusBytes(byte abyte0[]) {
        if (T.bDebug)
            T.in(this, "convertStatusBytes()", abyte0);
        if (abyte0 == null) {
            if (T.bTrace)
                T.traceln(ClientTraceMessages.getMessage(33));
            ConnectionType = 0;
            CicsServerStatus = 0;
            CicsClientStatus = 0;
            return;
        }
        if (boolLittleEndian) {
            ConnectionType = abyte0[1] << 8 | abyte0[0];
            CicsServerStatus = abyte0[3] << 8 | abyte0[2];
            CicsClientStatus = abyte0[5] << 8 | abyte0[4];
        } else {
            ConnectionType = abyte0[0] << 8 | abyte0[1];
            CicsServerStatus = abyte0[2] << 8 | abyte0[3];
            CicsClientStatus = abyte0[4] << 8 | abyte0[5];
        }
        if (T.bDebug)
            T.out(this, "convertStatusBytes()");
    }

    protected String serverAdjustString(String s) {
        return serverAdjustString(s, false);
    }

    private String serverAdjustString(String s, boolean flag) {
        if (T.bDebug)
            T.in(this, "serverAdjustString()", flag ? "PASSWORD" : ((Object) (s)));
        String s1 = null;
        if (s != null) {
            s1 = s.trim();
            if (s1.length() == 0)
                s1 = null;
        }
        if (T.bDebug)
            if (s1 == null) {
                T.out(this, "serverAdjustString() = null");
            } else {
                String s2 = "\"" + s1 + "\"";
                T.out(this, "serverAdjustString()", flag ? "PASSWORD" : ((Object) (s2)));
            }
        return s1;
    }

    protected void setContentsFromPartner(GatewayRequest gatewayrequest)
            throws IOException {
        if (T.bDebug)
            T.in(this, "setContentsFromPartner()", gatewayrequest);
        ECIRequest ecirequest = (ECIRequest) gatewayrequest;
        int i = ecirequest.getVersion();
        if (i >= 0x200000)
            setContentsV2(ecirequest);
        else
            setContentsV113(ecirequest);
        if (T.bDebug)
            T.out(this, "setContentsFromPartner()");
    }

    protected void setContentsV113(ECIRequest ecirequest)
            throws IOException {
        if (T.bDebug)
            T.in(this, "setContentsV113()", ecirequest);
        try {
            Call_Type = ecirequest.Call_Type;
            switch (Call_Type) {
                case 9: // '\t'
                    iNumOfSystems = ecirequest.maxNumServers;
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(24, iNumOfSystems));
                    if (iNumOfSystems < 0)
                        iNumOfSystems = 0;
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
                case 10: // '\n'
                case 11: // '\013'
                case 12: // '\f'
                case 13: // '\r'
                    Extend_Mode = ecirequest.Extend_Mode;
                    Luw_Token = ecirequest.Luw_Token;
                    Message_Qualifier = ecirequest.Message_Qualifier;
                    bCallbackExists = ecirequest.isCallback();
                    Server = serverAdjustString(ecirequest.Server);
                    Userid = serverAdjustString(ecirequest.Userid);
                    Password = serverAdjustString(ecirequest.Password, true);
                    Program = serverAdjustString(ecirequest.Program);
                    Transid = serverAdjustString(ecirequest.Transid);
                    validateCommareaLength(ecirequest);
                    Commarea_Length = ecirequest.Commarea_Length;
                    if (Commarea_Length > 0) {
                        Commarea = new byte[Commarea_Length];
                        System.arraycopy(ecirequest.Commarea, 0, Commarea, 0, Commarea_Length);
                    }
                    if (T.bTrace) {
                        T.traceln(ClientTraceMessages.getMessage(21, getCallTypeString(), Server, Program, Transid, getExtendModeString(), Luw_Token, Message_Qualifier, bCallbackExists));
                        T.traceln(ClientTraceMessages.getMessage(28, Commarea_Length));
                        if (Commarea != null)
                            T.hexDump(this, Commarea, "Commarea Inbound");
                    }
                    break;

                default:
                    String s = ClientTraceMessages.getMessage(71);
                    if (T.bTrace)
                        T.traceln(s);
                    throw new IOException(s);
            }
        } catch (Throwable throwable) {
            T.ex(this, throwable);
            throw new IOException("6556");
        }
        if (T.bDebug)
            T.out(this, "setContentsV113()");
    }

    protected void setContentsV2(ECIRequest ecirequest)
            throws IOException {
        if (T.bDebug)
            T.in(this, "setContentsV2()", ecirequest);
        boolean flag = false;
        int i = ecirequest.getVersion();
        try {
            Call_Type = ecirequest.Call_Type;
            switch (Call_Type) {
                case 9: // '\t'
                    iNumOfSystems = ecirequest.maxNumServers;
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(24, iNumOfSystems));
                    if (iNumOfSystems < 0)
                        iNumOfSystems = 0;
                    break;

                case 10: // '\n'
                case 11: // '\013'
                    Message_Qualifier = ecirequest.Message_Qualifier;
                    bCallbackExists = ecirequest.isCallback();
                    Server = serverAdjustString(ecirequest.Server);
                    Extend_Mode = 5;
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(36, getCallTypeString(), Server));
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
                    Extend_Mode = ecirequest.Extend_Mode;
                    Luw_Token = ecirequest.Luw_Token;
                    Message_Qualifier = ecirequest.Message_Qualifier;
                    bCallbackExists = ecirequest.isCallback();
                    Server = serverAdjustString(ecirequest.Server);
                    Userid = serverAdjustString(ecirequest.Userid);
                    Password = serverAdjustString(ecirequest.Password, true);
                    Program = serverAdjustString(ecirequest.Program);
                    Transid = serverAdjustString(ecirequest.Transid);
                    AV = ecirequest.AV;
                    GlobalTranSupport = ecirequest.GlobalTranSupport;
                    boolean flag1 = ecirequest.isCommareaInboundLength();
                    if (flag1) {
                        setCommareaInboundLength(ecirequest.getCommareaInboundLength());
                        if (T.bTrace)
                            T.traceln(ClientTraceMessages.getMessage(30, iCommareaLengthIn));
                    }
                    if (i >= 0x401000 && ecirequest.isInboundDataLength())
                        setInboundDataLength(ecirequest.getInboundDataLength());
                    ecirequest.Commarea_Length = validateInt(ecirequest.Commarea_Length);
                    if (!ecirequest.isCommareaOutboundLength()) {
                        validateCommareaLength(ecirequest);
                        Commarea_Length = ecirequest.Commarea_Length;
                        if (Commarea_Length > 0) {
                            Commarea = new byte[Commarea_Length];
                            if (ECIRequestWontSendCommarea(i))
                                T.ln(this, "no commarea copied from application ECIRequest (1)");
                            else
                                System.arraycopy(ecirequest.Commarea, 0, Commarea, 0, Commarea_Length);
                        } else {
                            Commarea = null;
                        }
                    } else {
                        Commarea_Length = ecirequest.Commarea_Length;
                        ecirequest.setCommareaOutboundLength(validateCommareaOutboundLength(ecirequest));
                        setCommareaOutboundLength(ecirequest.getCommareaOutboundLength());
                        if (T.bTrace) {
                            T.traceln(ClientTraceMessages.getMessage(29, iCommareaLengthOut));
                            if (iCommareaLengthOut > Commarea_Length)
                                T.traceln(ClientTraceMessages.getMessage(34));
                        }
                        if (Commarea_Length > 0) {
                            Commarea = new byte[Commarea_Length];
                            if (ECIRequestWontSendCommarea(i)) {
                                T.ln(this, "no commarea copied from application ECIRequest(2)");
                            } else {
                                int j = Math.min(Commarea_Length, iCommareaLengthOut);
                                System.arraycopy(ecirequest.Commarea, 0, Commarea, 0, j);
                            }
                        } else {
                            Commarea = null;
                        }
                    }
                    if (T.bTrace) {
                        T.traceln(ClientTraceMessages.getMessage(21, getCallTypeString(), Server, Program, Transid, getExtendModeString(), Luw_Token, Message_Qualifier, bCallbackExists));
                        T.traceln(ClientTraceMessages.getMessage(28, Commarea_Length));
                        if (Commarea != null)
                            T.hexDump(this, Commarea, "Commarea Inbound");
                    }
                    break;

                default:
                    String s = ClientTraceMessages.getMessage(71);
                    if (T.bTrace)
                        T.traceln(s);
                    throw new IOException(s);
            }
            if (i >= 0x200010)
                setECITimeout(ecirequest.getECITimeout());
            if (i >= 0x400000)
                setAutoMsgQual(ecirequest.isAutoMsgQual());
        } catch (Throwable throwable) {
            T.ex(this, throwable);
            throw new IOException("6556");
        }
        if (T.bDebug)
            T.out(this, "setContentsV2()");
    }

    protected int validateCommareaOutboundLength(ECIRequest ecirequest) {
        if (T.bDebug)
            T.in(this, "validateCommareaOutboundLength()", ecirequest);
        int i = ecirequest.getCommareaOutboundLength();
        switch (i) {
            default:
                if (i < 0)
                    i = 0;
                else if (ecirequest.Commarea == null)
                    i = 0;
                else if (i > ecirequest.Commarea.length)
                    i = ecirequest.Commarea.length;
                // fall through

            case 0: // '\0'
                return i;
        }
    }

    private int isExtendedRequest() {
        if (T.bDebug)
            T.in(this, "isExtendedRequest()");
        byte byte0 = 0;
        switch (iSave_Extend_Mode) {
            case 0: // '\0'
            case 2: // '\002'
            case 4: // '\004'
                if (iSave_Luw_Token != 0)
                    if (Cics_Rc == 0)
                        byte0 = 3;
                    else if (Luw_Token == 0)
                        byte0 = 3;
                    else
                        byte0 = 2;
                break;

            case 1: // '\001'
                if (iSave_Luw_Token == 0 && Luw_Token != 0)
                    byte0 = 1;
                else if (Cics_Rc == 0)
                    byte0 = 2;
                else if (Luw_Token == 0) {
                    if (iSave_Luw_Token != 0)
                        byte0 = 3;
                } else {
                    byte0 = 2;
                }
                break;

            case 3: // '\003'
            default:
                if (T.bDebug)
                    T.ln(this, "Extend_Mode = {0}", new Integer(Extend_Mode));
                break;
        }
        if (T.bDebug)
            T.out(this, "isExtendedRequest()", byte0);
        return byte0;
    }

    public boolean confirmationRequired() {
        if (T.bDebug)
            T.in(this, "confirmationRequired()");
        boolConfirmationRequired = false;
        switch (Call_Type) {
            case 2: // '\002'
            case 8: // '\b'
            case 11: // '\013'
            case 13: // '\r'
                if (bCallbackExists)
                    boolConfirmationRequired = true;
                break;
        }
        if (T.bDebug)
            T.out(this, "confirmationRequired()", boolConfirmationRequired);
        return boolConfirmationRequired;
    }

    public void writeObject(DataOutputStream dataoutputstream)
            throws IOException {
        if (T.bDebug)
            T.in(this, "writeObject()", dataoutputstream);
        int i = getVersion();
        dataoutputstream.writeInt(Call_Type);
        try {
            switch (Call_Type) {
                case 9: // '\t'
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(26, getCicsRcString(), iNumOfSystems));
                    dataoutputstream.writeInt(iNumOfSystems);
                    dataoutputstream.writeInt(Cics_Rc);
                    if (iNumOfSystems <= 0 || Commarea == null || Commarea_Length <= 0)
                        dataoutputstream.writeInt(0);
                    else
                        try {
                            if (Commarea_Length > Commarea.length) {
                                dataoutputstream.writeInt(Commarea.length);
                                dataoutputstream.write(Commarea, 0, Commarea.length);
                            } else {
                                dataoutputstream.writeInt(Commarea_Length);
                                dataoutputstream.write(Commarea, 0, Commarea_Length);
                            }
                        } catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
                            T.ex(null, arrayindexoutofboundsexception);
                            throw new IOException("6556");
                        }
                    break;

                case 10: // '\n'
                case 11: // '\013'
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(23, getCallTypeString(), getCicsRcString(), getConnectionTypeString(), getClientStatusString(), getServerStatusString(), Luw_Token, Message_Qualifier));
                    dataoutputstream.writeInt(Luw_Token);
                    dataoutputstream.writeInt(Message_Qualifier);
                    dataoutputstream.writeInt(Cics_Rc);
                    dataoutputstream.writeShort(ConnectionType);
                    dataoutputstream.writeShort(CicsServerStatus);
                    dataoutputstream.writeShort(CicsClientStatus);
                    break;

                default:
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(22, getCallTypeString(), getCicsRcString(), Abend_Code, Luw_Token, Message_Qualifier));
                    dataoutputstream.writeInt(Luw_Token);
                    dataoutputstream.writeInt(Message_Qualifier);
                    dataoutputstream.writeInt(Cics_Rc);
                    dataoutputstream.writeBytes(toPaddedString(Abend_Code, 4));
                    if (i >= 0x401000 && getReplyReturnedInvalidDataLength()) {
                        if (Commarea_Length <= 0) {
                            T.ln(this, "Error: get reply returned negative length");
                            Commarea_Length = 0;
                        }
                    } else if (Commarea_Length <= 0 || Commarea == null) {
                        Commarea_Length = 0;
                        Commarea = null;
                    }
                    dataoutputstream.writeInt(Commarea_Length);
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(28, Commarea_Length));
                    if (i >= 0x200000)
                        writeObjectV2(dataoutputstream);
                    if (Commarea != null)
                        if (ServerECIRequestWontSendCommarea(i))
                            T.ln(this, "No commarea sent.");
                        else
                            dataoutputstream.write(Commarea, 0, Commarea_Length);
                    break;
            }
        } catch (Throwable throwable) {
            T.ex(this, throwable);
            throw new IOException("6556");
        }
        if (T.bDebug)
            T.out(this, "writeObject()");
    }

    public void readObject(DataInputStream datainputstream)
            throws IOException {
        if (T.bDebug)
            T.in(this, "readObject()", datainputstream);
        boolLocalGateway = false;
        int i = getVersion();
        try {
            Call_Type = datainputstream.readInt();
            switch (Call_Type) {
                default:
                    break;

                case 9: // '\t'
                    iNumOfSystems = datainputstream.readInt();
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(24, iNumOfSystems));
                    if (iNumOfSystems < 0)
                        iNumOfSystems = 0;
                    break;

                case 10: // '\n'
                case 11: // '\013'
                    if (i >= 0x200000) {
                        Message_Qualifier = datainputstream.readInt();
                        bCallbackExists = datainputstream.readBoolean();
                        Server = readPaddedString(datainputstream, 8);
                        Extend_Mode = 5;
                        if (T.bTrace)
                            T.traceln(ClientTraceMessages.getMessage(36, getCallTypeString(), Server));
                        break;
                    }
                    // fall through

                case 1: // '\001'
                case 2: // '\002'
                case 3: // '\003'
                case 4: // '\004'
                case 5: // '\005'
                case 6: // '\006'
                case 7: // '\007'
                case 8: // '\b'
                case 12: // '\f'
                case 13: // '\r'
                    if (i >= 0x200000) {
                        readObjectV2(datainputstream);
                    } else {
                        Extend_Mode = datainputstream.readInt();
                        Luw_Token = datainputstream.readInt();
                        Message_Qualifier = datainputstream.readInt();
                        bCallbackExists = datainputstream.readBoolean();
                        Server = readPaddedString(datainputstream, 8);
                        Userid = readPaddedString(datainputstream, 16);
                        Password = readPaddedString(datainputstream, 16, true);
                        Program = readPaddedString(datainputstream, 8);
                        Transid = readPaddedString(datainputstream, 4);
                        Commarea_Length = datainputstream.readInt();
                        if (Commarea_Length <= 0) {
                            Commarea_Length = 0;
                            Commarea = null;
                        } else {
                            Commarea = new byte[Commarea_Length];
                            datainputstream.readFully(Commarea);
                        }
                    }
                    if (T.bTrace) {
                        T.traceln(ClientTraceMessages.getMessage(21, getCallTypeString(), Server, Program, Transid, getExtendModeString(), Luw_Token, Message_Qualifier, bCallbackExists));
                        T.traceln(ClientTraceMessages.getMessage(28, Commarea_Length));
                        if (Commarea != null)
                            T.hexDump(this, Commarea, "Commarea Inbound");
                    }
                    break;
            }
            if (i >= 0x200010)
                setECITimeout(datainputstream.readShort());
            if (i >= 0x400000)
                setAutoMsgQual(datainputstream.readBoolean());
        } catch (Throwable throwable) {
            T.ex(this, throwable);
            throw new IOException("6553");

        }
        if (T.bDebug)
            T.out(this, "readObject()");
    }

    private void writeObjectV2(DataOutputStream dataoutputstream)
            throws IOException {
        if (T.bDebug)
            T.in(this, "writeObjectV2()", dataoutputstream);
        boolean flag = T.bTrace;
        boolean flag2 = false;
        boolean flag3 = getVersion() >= 0x401000;
        boolean flag1;
        if (flag3) {
            flag1 = isCommareaInboundLength() || isInboundDataLength();
            flag2 = isInboundDataLength() && !isCommareaInboundLength();
        } else {
            flag1 = isCommareaInboundLength();
        }
        dataoutputstream.writeBoolean(flag1);
        if (flag3 && flag1)
            dataoutputstream.writeBoolean(flag2);
        if (flag1) {
            int i;
            if (flag2)
                i = validateInt(getInboundDataLength());
            else
                i = validateInt(getCommareaInboundLength());
            dataoutputstream.writeInt(i);
            if (flag)
                T.traceln(ClientTraceMessages.getMessage(30, i));
            if (i == 0) {
                Commarea_Length = 0;
                Commarea = null;
            } else {
                if (i > Commarea_Length && flag)
                    T.traceln(ClientTraceMessages.getMessage(35));
                byte abyte0[] = new byte[i];
                if (Commarea != null && Commarea_Length > 0) {
                    int j = Math.min(Commarea_Length, i);
                    System.arraycopy(Commarea, 0, abyte0, 0, j);
                }
                Commarea = abyte0;
                Commarea_Length = i;
            }
        }
        if (T.bDebug)
            T.out(this, "writeObjectV2()");
    }

    private void readObjectV2(DataInputStream datainputstream)
            throws IOException {
        if (T.bDebug)
            T.in(this, "readObjectV2()", datainputstream);
        int i = 0;
        boolean flag = false;
        boolean flag1 = T.bTrace;
        boolean flag2 = false;
        boolean flag3 = false;
        int j = getVersion();
        Extend_Mode = datainputstream.readInt();
        Luw_Token = datainputstream.readInt();
        Message_Qualifier = datainputstream.readInt();
        bCallbackExists = datainputstream.readBoolean();
        Server = readPaddedString(datainputstream, 8);
        Userid = readPaddedString(datainputstream, 16);
        Password = readPaddedString(datainputstream, 16, true);
        Program = readPaddedString(datainputstream, 8);
        Transid = readPaddedString(datainputstream, 4);
        Commarea_Length = datainputstream.readInt();
        if (Commarea_Length <= 0) {
            Commarea_Length = 0;
            Commarea = null;
        } else {
            Commarea = new byte[Commarea_Length];
        }
        if (flag1)
            T.traceln(ClientTraceMessages.getMessage(28, Commarea_Length));
        flag2 = datainputstream.readBoolean();
        if (flag2) {
            setCommareaOutboundLength(datainputstream.readInt());
            i = getCommareaOutboundLength();
            if (flag1)
                T.traceln(ClientTraceMessages.getMessage(29, i));
        } else {
            i = Commarea_Length;
        }
        flag3 = datainputstream.readBoolean();
        if (flag3) {
            setCommareaInboundLength(datainputstream.readInt());
            if (flag1)
                T.traceln(ClientTraceMessages.getMessage(30, iCommareaLengthIn));
        }
        if (i > 0)
            if (ECIRequestWontSendCommarea(j))
                T.ln(this, "GET REPLY,commit or backout. No commarea to read.");
            else if (i > Commarea_Length) {
                byte abyte0[] = new byte[i];
                datainputstream.readFully(abyte0);
                if (Commarea_Length > 0)
                    System.arraycopy(abyte0, 0, Commarea, 0, Commarea_Length);
                T.traceln(ClientTraceMessages.getMessage(34));
            } else {
                datainputstream.readFully(Commarea, 0, i);
            }
        if (T.bDebug)
            T.out(this, "readObjectV2()");
    }

    public boolean execute()
            throws Throwable {
/*
        if (T.bDebug)
            T.in(this, "execute()");
        boolean flag = false;
        boolExecuteOK = false;
        Cics_Rc = 0;
        if (!boolNativeLibraryLoaded) {
            loadNativeLibrary();
            if (!boolNativeLibraryLoaded) {
                if (T.bDebug)
                    T.out(this, "execute(), failed to load native library", false);
                return false;
            }
        }
        switch (Call_Type) {
            case 9: // '\t'
                executeList();
                break;

            case 11: // '\013'
                if (!bCallbackExists) {
                    flag = lockMsgQual();
                    if (Cics_Rc == -1000 || Cics_Rc == -1001 && !boolLocalGateway && !mqValDisabled)
                        break;
                }
                // fall through

            case 10: // '\n'
                if (boolRpcClient)
                    Commarea = new byte[6];
                else
                    Commarea = new byte[10];
                Commarea_Length = Commarea.length;
                executeECI(false);
                if (iSave_Extend_Mode == 7) {
                    ConnectionType = 0;
                    CicsServerStatus = 0;
                    CicsClientStatus = 0;
                } else {
                    convertStatusBytes(Commarea);
                }
                Abend_Code = null;
                break;

            case 8: // '\b'
                if (!bCallbackExists) {
                    flag = lockMsgQual();
                    if (Cics_Rc == -1000 || Cics_Rc == -1001 && !boolLocalGateway && !mqValDisabled)
                        break;
                }
                // fall through

            case 7: // '\007'
                executeECI(true);
                break;

            case 3: // '\003'
            case 4: // '\004'
                if (!boolLocalGateway) {
                    if (T.bTrace)
                        T.traceln(ClientTraceMessages.getMessage(32));
                    if (System.getProperty("gateway.ecigenericreplies", "on").toLowerCase().equals("off")) {
                        if (T.bTrace)
                            T.traceln(ClientTraceMessages.getMessage(71));
                        Cics_Rc = -14;
                        break;
                    }
                }
                executeECI(true);
                if (!uowValDisabled && !boolLocalGateway && replyRetrieved()) {
                    iSave_Luw_Token = MsgQualMgr.getInstance().getMsgQualLuw(Message_Qualifier);
                    updateLUWValState(true);
                }
                freeMsgQual();
                break;

            case 5: // '\005'
            case 6: // '\006'
                MsgQualMgr msgqualmgr = MsgQualMgr.getInstance();
                if (!boolLocalGateway && !mqValDisabled) {
                    Integer integer = msgqualmgr.getMsgQualCM(Message_Qualifier);
                    T.ln(this, "execute(), validating MsgQ {0}.  My Connection Manager={1}, Connection Manager for MsgQ={2}", new Integer(Message_Qualifier), iobjCMIndex, integer);
                    if (integer == null || iobjCMIndex.intValue() != integer.intValue()) {
                        Cics_Rc = -5;
                        break;
                    }
                }
                executeECI(true);
                if (!uowValDisabled && !boolLocalGateway && replyRetrieved()) {
                    iSave_Luw_Token = msgqualmgr.getMsgQualLuw(Message_Qualifier);
                    updateLUWValState(true);
                }
                freeMsgQual();
                break;

            case 2: // '\002'
            case 13: // '\r'
                if (!bCallbackExists) {
                    flag = lockMsgQual();
                    if (Cics_Rc == -1000 || Cics_Rc == -1001 && !boolLocalGateway && !mqValDisabled)
                        break;
                }
                // fall through

            case 1: // '\001'
            case 12: // '\f'
                if (uowValDisabled || validateLUW()) {
                    executeECI(true);
                    if (!uowValDisabled && !boolLocalGateway)
                        updateLUWValState(false);
                    if ((Call_Type == 2 || Call_Type == 13) && !bCallbackExists && Cics_Rc == 0)
                        MsgQualMgr.getInstance().associateLUW(Message_Qualifier, Luw_Token);
                }
                break;

            default:
                String s = ClientTraceMessages.getMessage(71);
                if (T.bTrace)
                    T.traceln(s);
                if (T.bDebug)
                    T.ln(this, "execute(), Call_Type = {0}", new Integer(Call_Type));
                throw new Throwable(s);
        }
        if (Cics_Rc == 0) {
            boolExecuteOK = true;
        } else {
            if (flag) {
                MsgQualMgr msgqualmgr1 = MsgQualMgr.getInstance();
                msgqualmgr1.freeMsgQual(Message_Qualifier);
            }
            if (T.bDebug)
                T.ln(this, "execute(), CICS RC = {0}", new Integer(Cics_Rc));
        }
        if (T.bDebug)
            T.out(this, "execute(), Normal Cics_Rc", boolExecuteOK);
*/
        return boolExecuteOK;
    }
/*

    private boolean lockMsgQual() {
        T.in(this, "lockMsgQual()");
        boolean flag = true;
        MsgQualMgr msgqualmgr = MsgQualMgr.getInstance();
        if (!isAutoMsgQual()) {
            flag = msgqualmgr.lockMsgQual(Message_Qualifier, iobjCMIndex);
            if (!flag)
                Cics_Rc = -1001;
        } else {
            try {
                Message_Qualifier = msgqualmgr.requestMsgQual(iobjCMIndex);
            } catch (Exception exception) {
                Cics_Rc = -1000;
                flag = false;
            }
        }
        T.out(this, "lockMsgQual()", flag);
        return flag;
    }

    private void freeMsgQual() {
        T.in(this, "freeMsqQual()");
        if (replyRetrieved()) {
            MsgQualMgr msgqualmgr = MsgQualMgr.getInstance();
            msgqualmgr.freeMsgQual(Message_Qualifier);
        }
        T.out(this, "freeMsgQual()");
    }

*/
    private boolean replyRetrieved() {
        return Cics_Rc != -1 && Cics_Rc != -19 && Cics_Rc != -5;
    }

    private boolean validateLUW() {
        T.in(this, "validateLUW()");
        boolean flag = true;
        if (!boolLocalGateway && Luw_Token != 0) {
            Integer integer = (Integer) usedLUWs.get(new Integer(Luw_Token));
            if (T.bDebug)
                T.ln(this, "validateLUW(), validating luw {0}.  My Connection Manager={1}, Connection Manager for LUW={2}", new Integer(Luw_Token), iobjCMIndex, integer);
            if (integer == null || iobjCMIndex.intValue() != integer.intValue()) {
                Cics_Rc = -8;
                flag = false;
            }
        }
        T.out(this, "validateLUW()", flag);
        return flag;
    }

    private void updateLUWValState(boolean flag) {
        T.in(this, "updateLUWValState()");
        int i = 0;
        if (flag) {
            if (iSave_Luw_Token != Luw_Token)
                if (iSave_Luw_Token != 0)
                    i = 3;
                else
                    i = 1;
        } else {
            i = isExtendedRequest();
        }
        switch (i) {
            default:
                break;

            case 3: // '\003'
                synchronized (usedLUWs) {
                    Integer integer = new Integer(iSave_Luw_Token);
                    Integer integer1 = (Integer) usedLUWs.get(integer);
                    if (integer1 != null && iobjCMIndex.intValue() == integer1.intValue()) {
                        usedLUWs.remove(integer);
                        if (T.bDebug)
                            T.ln(this, "updateLUWValState(), removed luw {0} from LUW Validation table", integer);
                    } else if (T.bDebug)
                        T.ln(this, "updateLUWValState(), Cannot remove luw {0} from LUW Validation table, owner Connection index {1}, requester Connection index {2}", integer, integer1, iobjCMIndex);
                }
                break;

            case 1: // '\001'
                synchronized (usedLUWs) {
                    usedLUWs.put(new Integer(Luw_Token), iobjCMIndex);
                }
                if (T.bDebug)
                    T.ln(this, "updateLUWValState(), Added luw {0} to LUW Validation table with Connection Index {1}", new Integer(Luw_Token), iobjCMIndex);
                break;
        }
        T.out(this, "updateLUWValState()");
    }

    private void executeECI(boolean flag)
            throws Throwable {
        int ai[] = new int[10];
        byte abyte0[] = new byte[5];
        boolean flag1 = true;
        iSave_Extend_Mode = Extend_Mode;
        iSave_Luw_Token = Luw_Token;
        ai[0] = returnCallType();
        ai[1] = Extend_Mode;
        ai[2] = Luw_Token;
        ai[3] = Commarea_Length;
        ai[4] = Cics_Rc;
        ai[5] = Message_Qualifier;
        ai[6] = getECITimeout();
        if (AV == 19429)
            ai[7] = 2;
        if (GlobalTranSupport == 1)
            ai[7] = ai[7] | 1;
        byte abyte1[];
        if (Extend_Mode == 2 || Extend_Mode == 4) {
            abyte1 = null;
            ai[3] = 0;
            ai[9] = 0;
            flag1 = false;
        } else {
            abyte1 = Commarea;
            if (isCommareaOutboundLength())
                ai[9] = getCommareaOutboundLength();
            else
                ai[9] = Commarea_Length;
        }
        try {
            if (T.bTrace)
                T.traceln(ClientTraceMessages.getMessage(97, "CcicsECI(1)", Server, Program, ai[3], ai[1], ai[2]));
            CcicsECI(Thread.currentThread().getName(), Server, Userid, Password, Program, Transid, abyte1, abyte0, ai);
            if (T.bTrace)
                T.traceln(ClientTraceMessages.getMessage(100, "CcicsECI(1)", ai[4], ai[2], ai[8]));
        } catch (Throwable throwable) {
            T.ex(this, throwable);
            String s = "ctgjni: " + throwable.toString();
            throw new Throwable("6554");
        }
        Cics_Rc = ai[4];
        Luw_Token = ai[2];
        Message_Qualifier = ai[5];
        int i = ai[8];
        if (getVersion() >= 0x401000 && getReplyReturnedInvalidDataLength()) {
            Commarea_Length = ai[3];
            if (T.getLinesOn())
                T.ln(this, "Get reply returned Commarea length {0}", new Integer(Commarea_Length));
        }
        if (Cics_Rc == 0 && Commarea != null && i < Commarea.length && !isCommareaInboundLength()) {
            setInboundDataLength(i);
        } else {
            T.ln(this, "Not using JNI null stripping. JNI returned data len {0}", new Integer(i));
            setInboundDataLength(false);
        }
        if (Cics_Rc == 0)
            if (isCommareaInboundLength()) {
                int j = Math.min(getCommareaInboundLength(), Commarea_Length);
                if (i < j) {
                    for (int i1 = i; i1 < j; i1++)
                        Commarea[i1] = 0;

                }
            } else if (getVersion() < 0x401000 && flag1 && i < Commarea_Length) {
                for (int k = i; k < Commarea_Length; k++)
                    Commarea[k] = 0;

            }
        if (flag) {
            int l;
            for (l = 0; l < 4 && abyte0[l] != 0; l++) ;
            if (l == 0)
                Abend_Code = null;
            else if (boolMVS)
                Abend_Code = new String(abyte0, 0, l, "Cp1047");
            else
                Abend_Code = new String(abyte0, 0, l, "Cp437");
        }
    }

    private void executeList()
            throws Throwable {
        if (T.bDebug)
            T.in(this, "executeList()");
        Object obj = null;
        int i = iNumOfSystems * 70;
        byte abyte0[] = new byte[i];
        Commarea = new byte[i];
        int ai[] = new int[2];
        ai[0] = iNumOfSystems;
        ai[1] = Cics_Rc;
        try {
            if (T.bTrace)
                T.traceln(ClientTraceMessages.getMessage(99, "CcicsList"));
            CcicsLIST(Thread.currentThread().getName(), Commarea, ai);
            if (T.bTrace)
                T.traceln(ClientTraceMessages.getMessage(98, "CcicsList", ai[1]));
        } catch (Throwable throwable) {
            T.ex(this, throwable);
            String s = "ctgjni: " + throwable.toString();
            throw new Throwable("6554");
        }
        iNumOfSystems = ai[0];
        Cics_Rc = ai[1];
        if (Commarea != null && (Cics_Rc == 0 || Cics_Rc == -25)) {
            Commarea_Length = iNumOfSystems * 70;
        } else {
            Commarea_Length = 0;
            Commarea = null;
            iNumOfSystems = 0;
        }
        if (T.bDebug)
            T.out(this, "executeList()");
    }

    private void loadNativeLibrary()
            throws Throwable {
        if (T.bDebug)
            T.in(this, "loadNativeLibrary()");
        try {
            System.loadLibrary("ctgjni");
            boolNativeLibraryLoaded = true;
            if (T.bTrace)
                T.traceln(ClientTraceMessages.getMessage(3));
        } catch (UnsatisfiedLinkError unsatisfiedlinkerror) {
            T.ex(this, unsatisfiedlinkerror);
            throw new Throwable(ClientMessages.getMessage(rbDefault, 4, unsatisfiedlinkerror));
        }
        if (T.bDebug)
            T.out(this, "loadNativeLibrary()");
    }

    private int returnCallType() {
        if (T.bDebug)
            T.in(this, "returnCallType()");
        int i = Call_Type;
        switch (Call_Type) {
            case 2: // '\002'
                if (bCallbackExists)
                    i = 1;
                break;

            case 13: // '\r'
                if (bCallbackExists)
                    i = 12;
                break;

            case 8: // '\b'
                if (bCallbackExists)
                    i = 7;
                break;

            case 10: // '\n'
                i = 7;
                break;

            case 11: // '\013'
                if (bCallbackExists)
                    i = 7;
                else
                    i = 8;
                break;
        }
        if (T.bDebug)
            T.out(this, "returnCallType()", i);
        return i;
    }

    public void setConnectionIndex(int i) {
        iobjCMIndex = new Integer(i);
        if (T.bDebug)
            T.in(this, "setConnectionIndex()", iobjCMIndex);
    }

    public int getCleanupAction() {
        T.out(this, "getCleanupAction()", cleanupAction);
        return cleanupAction;
    }

    public void setCleanupAction(int i) {
        if (T.bDebug)
            T.in(this, "setCleanupAction()", new Integer(i));
        cleanupAction = i;
    }

    public GatewayRequest[] getCleanupRequests() {
        T.in(this, "getCleanupRequests()");
        ServerECIRequest serverecirequest = null;
        GatewayRequest agatewayrequest[] = null;
        int i = 0;
        switch (returnCallType()) {
            case 2: // '\002'
            case 8: // '\b'
            case 11: // '\013'
            case 13: // '\r'
                if (!mqValDisabled && Cics_Rc == 0) {
                    serverecirequest = new ServerECIRequest();
                    serverecirequest.Call_Type = 6;
                    serverecirequest.Message_Qualifier = Message_Qualifier;
                    serverecirequest.Commarea = Commarea;
                    serverecirequest.Commarea_Length = Commarea_Length;
                    serverecirequest.Luw_Token = Luw_Token;
                    serverecirequest.setCleanupAction(1);
                    i = 1;
                }
                break;

            case 3: // '\003'
            case 4: // '\004'
            case 5: // '\005'
            case 6: // '\006'
                if (!uowValDisabled && replyRetrieved() && Luw_Token != 0) {
                    serverecirequest = new ServerECIRequest();
                    serverecirequest.Call_Type = 1;
                    serverecirequest.Extend_Mode = 4;
                    serverecirequest.Userid = Userid;
                    serverecirequest.Password = Password;
                    serverecirequest.Luw_Token = Luw_Token;
                    serverecirequest.iSave_Luw_Token = Luw_Token;
                    serverecirequest.setCleanupAction(1);
                    i = 1;
                }
                break;

            case 1: // '\001'
            case 12: // '\f'
                if (!uowValDisabled) {
                    int j = isExtendedRequest();
                    if (j == 1 || j == 2) {
                        serverecirequest = new ServerECIRequest();
                        serverecirequest.Call_Type = 1;
                        serverecirequest.Extend_Mode = 4;
                        serverecirequest.Userid = Userid;
                        serverecirequest.Password = Password;
                        serverecirequest.Luw_Token = Luw_Token;
                        serverecirequest.iSave_Luw_Token = Luw_Token;
                        if (j == 1)
                            serverecirequest.setCleanupAction(1);
                        else
                            serverecirequest.setCleanupAction(2);
                        i = 1;
                    }
                }
                break;
        }
        if (i != 0)
            agatewayrequest = (new GatewayRequest[]{
                    serverecirequest
            });
        T.out(this, "getCleanupRequests()", i);
        return agatewayrequest;
    }

    public int getCleanupId() {
        T.in(this, "getCleanupId");
        switch (Call_Type) {
            case 1: // '\001'
            case 2: // '\002'
            case 12: // '\f'
            case 13: // '\r'
                T.out(this, "getCleanupId", iSave_Luw_Token);
                return iSave_Luw_Token;

            case 3: // '\003'
            case 4: // '\004'
            case 5: // '\005'
            case 6: // '\006'
                T.out(this, "getCleanupId", Message_Qualifier);
                return Message_Qualifier;

            case 7: // '\007'
            case 8: // '\b'
            case 9: // '\t'
            case 10: // '\n'
            case 11: // '\013'
            default:
                T.out(this, "getCleanupId", 0);
                return 0;
        }
    }

    public String getCleanupType() {
        T.in(this, "getCleanupType()");
        switch (Call_Type) {
            case 1: // '\001'
            case 2: // '\002'
            case 12: // '\f'
            case 13: // '\r'
                T.out(this, "getCleanupType() = LUW");
                return "LUW";

            case 3: // '\003'
            case 4: // '\004'
            case 5: // '\005'
            case 6: // '\006'
                T.out(this, "getCleanupType() = MSG");
                return "MSG";

            case 7: // '\007'
            case 8: // '\b'
            case 9: // '\t'
            case 10: // '\n'
            case 11: // '\013'
            default:
                T.out(this, "getCleanupType() not assigned");
                return "DOH";
        }
    }

    public boolean isCleanupRequest() {
        T.in(this, "isCleanupRequest()");
        boolean flag = false;
        switch (Call_Type) {
            case 7: // '\007'
            case 8: // '\b'
            case 9: // '\t'
            case 10: // '\n'
            case 11: // '\013'
            default:
                break;

            case 3: // '\003'
            case 4: // '\004'
            case 5: // '\005'
            case 6: // '\006'
                if (!mqValDisabled && replyRetrieved())
                    flag = true;
                break;

            case 1: // '\001'
            case 2: // '\002'
            case 12: // '\f'
            case 13: // '\r'
                if (uowValDisabled)
                    break;
                if (isExtendedRequest() == 3)
                    flag = true;
                if ((Call_Type == 2 || Call_Type == 13) && isExtendedRequest() == 2)
                    flag = true;
                break;
        }
        T.out(this, "isCleanupRequest()", flag);
        return flag;
    }

    public boolean executeCleanup() {
        boolean flag = false;
/*
        isNowCleanupRequest = true;
        T.in(this, "executeCleanup()");
        if (T.bDebug)
            if (Call_Type == 1)
                T.ln(this, "executeCleanup(), Backing out LUW {0}", new Integer(Luw_Token));
            else
                T.ln(this, "executeCleanup(), Throwing away reply for qualifier {0}", new Integer(getMessageQualifier()));
        try {
            flag = execute();
            T.ln(this, "executeCleanup(), Returncode was {0}", getCicsRcString());
            if (Call_Type == 1) {
                if (flag)
                    cleanupLogicalWorkState--;
            } else {
                if (!flag && (Cics_Rc == -1 || Cics_Rc == -19)) {
                    if (T.bDebug)
                        T.ln(this, "executeCleanup(), re-attempting getReply as Commarea not valid");
                    if (Cics_Rc == -19 || Cics_Rc == -1)
                        Commarea_Length = 32500;
                    Commarea = new byte[Commarea_Length];
                    flag = execute();
                    if (T.bDebug)
                        T.ln(this, "executeCleanup(), Returncode was {0}", getCicsRcString());
                }
                if (!replyRetrieved()) {
                    MsgQualMgr msgqualmgr = MsgQualMgr.getInstance();
                    T.ln(this, "executeCleanup(), can't get reply for qualifier {0} invalidating", new Integer(getMessageQualifier()));
                    msgqualmgr.invalidateMsgQual(getMessageQualifier());
                } else {
                    iSave_Luw_Token = MsgQualMgr.getInstance().getMsgQualLuw(getMessageQualifier());
                    if (iSave_Luw_Token == Luw_Token || iSave_Luw_Token == 0)
                        cleanupLogicalWorkState--;
                    else
                        cleanupLogicalWorkState -= 2;
                    if (Luw_Token != 0) {
                        if (T.bDebug)
                            T.ln(this, "executeCleanup(), Backing out LUW {0}", new Integer(Luw_Token));
                        Call_Type = 1;
                        Extend_Mode = 4;
                        flag = execute();
                        if (T.bDebug)
                            T.ln(this, "executeCleanup(), Returncode was {0}", getCicsRcString());
                        if (flag)
                            cleanupLogicalWorkState--;
                    }
                }
            }
        } catch (Throwable throwable) {
            T.ex(this, throwable);
            if (T.bTrace)
                T.traceln(TraceMessages.getMessage(60, throwable));
        }
        T.out(this, "executeCleanup()");
*/
        return flag;
    }

    public void terminate() {
        if (T.bDebug)
            T.in(this, "terminate()");
        try {
            if (boolMVS) {
                if (T.bTrace)
                    T.traceln(ClientTraceMessages.getMessage(99, "CcicsTerm"));
                CcicsTerm(Thread.currentThread().getName());
                if (T.bTrace)
                    T.traceln(ClientTraceMessages.getMessage(92, "CcicsTerm"));
            }
        } catch (Throwable throwable) {
            T.ex(this, throwable);
            if (T.bTrace)
                T.traceln("62");
        }
        if (T.bDebug)
            T.out(this, "terminate()");
    }

    public boolean isThisQuiesceBlockingWork() {
        T.in(this, "isThisQuiesceBlockingWork");
        boolean flag;
        if (Call_Type == 4 || Call_Type == 6)
            flag = false;
        else
            flag = true;
        T.out(this, "isThisQuiesceBlockingWork", flag);
        return flag;
    }

    public int getLogicalWorkState() {
        T.in(this, "getLogicalWorkState");
        int i;
/*
        if (isNowCleanupRequest) {
            i = cleanupLogicalWorkState;
            cleanupLogicalWorkState = 0;
        } else {
            switch (returnCallType()) {
                case 2: // '\002'
                case 8: // '\b'
                case 11: // '\013'
                case 13: // '\r'
                    if (Cics_Rc == 0)
                        i = 1;
                    else
                        i = 0;
                    break;

                case 3: // '\003'
                case 4: // '\004'
                case 5: // '\005'
                case 6: // '\006'
                    if (replyRetrieved()) {
                        iSave_Luw_Token = MsgQualMgr.getInstance().getMsgQualLuw(getMessageQualifier());
                        if (iSave_Luw_Token == Luw_Token) {
                            i = -1;
                            break;
                        }
                        if (iSave_Luw_Token == 0)
                            i = 0;
                        else
                            i = -2;
                    } else {
                        i = 0;
                    }
                    break;

                case 1: // '\001'
                case 12: // '\f'
                    if (isExtendedRequest() == 1) {
                        i = 1;
                        break;
                    }
                    if (isExtendedRequest() == 3)
                        i = -1;
                    else
                        i = 0;
                    break;

                case 7: // '\007'
                case 9: // '\t'
                case 10: // '\n'
                default:
                    i = 0;
                    break;
            }
        }
        T.out(this, "getLogicalWorkState", i);
*/
//        return i;
        return 0;
    }

    public boolean isThisAllowedDuringQuiesce() {
        T.in(this, "isThisAllowedDuringQuiesce");
        boolean flag;
        switch (Call_Type) {
            case 3: // '\003'
            case 4: // '\004'
            case 5: // '\005'
            case 6: // '\006'
                flag = true;
                break;

            case 1: // '\001'
            case 2: // '\002'
            case 12: // '\f'
            case 13: // '\r'
                if (Luw_Token == 0)
                    flag = false;
                else
                    flag = true;
                break;

            case 7: // '\007'
            case 8: // '\b'
            case 9: // '\t'
            case 10: // '\n'
            case 11: // '\013'
            default:
                flag = false;
                break;
        }
        T.out(this, "isAllowedDuringQuiesce", flag);
        return flag;
    }

}

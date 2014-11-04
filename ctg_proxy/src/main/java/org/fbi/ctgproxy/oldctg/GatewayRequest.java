package org.fbi.ctgproxy.oldctg;

import org.fbi.ctgproxy.CtgRequest;

import java.io.*;
import java.util.Locale;


public class GatewayRequest
	implements GatewayReturnCodes
{
	public static final String BASE = "BASE";
	public static final int GATEWAY_EYECATCHER = 0x47617465;
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
	static transient boolean bSpecifyASCII;
	private Callbackable calBack;
	public Object objRequestToken;
	public Locale locExchange;
	public String strServerJVM;
	public String strServerSecurityClass;
	public byte abytHandshake[];
	public boolean boolCloseHint;
	public String serverSideException;

	public GatewayRequest()
	{
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
		T.in(this, "GatewayRequest");
		strRequestType = "BASE";
		T.out(this, "GatewayRequest");
	}

	protected GatewayRequest(String s)
	{
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
		T.in(this, "GatewayRequest", s);
		strRequestType = s;
		T.out(this, "GatewayRequest");
	}

	public void initialize()
		throws Exception
	{
	}

	public void terminate()
	{
	}

	public void setRoot(GatewayRequest gatewayrequest)
	{
		T.in(this, "setRoot", gatewayrequest);
		iFlowVersion = gatewayrequest.iFlowVersion;
		iFlowType = gatewayrequest.iFlowType;
		iMessageId = gatewayrequest.iMessageId;
		iGatewayRc = gatewayrequest.iGatewayRc;
		strRequestType = gatewayrequest.strRequestType;
		bHasSecurity = gatewayrequest.bHasSecurity;
		iDataWhichFollows = gatewayrequest.iDataWhichFollows;
		objRequestToken = gatewayrequest.objRequestToken;
		abytHandshake = gatewayrequest.abytHandshake;
		locExchange = gatewayrequest.locExchange;
		boolCloseHint = gatewayrequest.boolCloseHint;
		serverSideException = gatewayrequest.serverSideException;
		strServerJVM = gatewayrequest.strServerJVM;
		T.out(this, "setRoot");
	}

	public void setRoot(CtgRequest gatewayrequest)
	{
		T.in(this, "setRoot", gatewayrequest);
		iFlowVersion = gatewayrequest.getiFlowVersion();
		iFlowType = gatewayrequest.getiFlowType();
		iMessageId = gatewayrequest.getiMessageId();
		iGatewayRc = gatewayrequest.getiGatewayRc();
		strRequestType = gatewayrequest.getStrRequestType();
		bHasSecurity = false;
		iDataWhichFollows = gatewayrequest.getiDataWhichFollows();
		objRequestToken = gatewayrequest.objRequestToken;
		abytHandshake = gatewayrequest.abytHandshake;
		locExchange = gatewayrequest.locExchange;
		boolCloseHint = gatewayrequest.boolCloseHint;
		serverSideException = gatewayrequest.serverSideException;
		strServerJVM = gatewayrequest.strServerJVM;
		T.out(this, "setRoot");
	}

	public int getVersion()
	{
		T.out(this, "getVersion", iFlowVersion);
		return iFlowVersion;
	}

	public void setMessageId(int i)
	{
		T.in(this, "setMessageId", new Integer(i));
		iMessageId = i;
	}

	public int getMessageId()
	{
		T.out(this, "getMessageId", iMessageId);
		return iMessageId;
	}

	public String getRequestType()
	{
		T.out(this, "getRequestType", strRequestType);
		return strRequestType;
	}

	public void setRc(int i)
	{
		T.in(this, "setRc", new Integer(i));
		iGatewayRc = i;
	}

	public int getRc()
	{
		T.out(this, "getRc", iGatewayRc);
		return iGatewayRc;
	}

	public String getRcString()
	{
		return getGatewayRcString();
	}

	public int getGatewayRc()
	{
		T.out(this, "getGatewayRc", iGatewayRc);
		return iGatewayRc;
	}

	public String getGatewayRcString()
	{
		String s = "ERROR_UNKNOWN_GATEWAY_RC";
		if (iGatewayRc <= GatewayReturnCodes.astrGateway_Rc.length + 61440)
			if (iGatewayRc == 0)
				s = "OK";
			else
				s = GatewayReturnCodes.astrGateway_Rc[iGatewayRc - 61440 - 1];
		if (T.bDebug)
			T.out(this, "getRcString()", s);
		return s;
	}

	public void setFlowType(int i)
	{
		T.in(this, "setFlowType", new Integer(i));
		iFlowType = i;
	}

	public int getFlowType()
	{
		T.out(this, "getFlowType", iFlowType);
		return iFlowType;
	}

	public int getDataWhichFollows()
	{
		T.out(this, "getDataWhichFollows", iDataWhichFollows);
		return iDataWhichFollows;
	}

	public void setDataWhichFollows(int i)
	{
		T.in(this, "setDataWhichFollows", new Integer(i));
		iDataWhichFollows = i;
	}

	public void setConnectionIndex(int i)
	{
		T.in(this, "setConnectionIndex", new Integer(i));
	}

	public void setCallback(Callbackable callbackable)
	{
		T.in(this, "setCallback", callbackable);
		calBack = callbackable;
	}

	protected Callbackable getCallback()
	{
		T.out(this, "getCallback", calBack);
		return calBack;
	}

	public void setHasSecurity(boolean flag)
	{
		T.in(this, "setHasSecurity", new Boolean(flag));
		bHasSecurity = flag;
	}

	public boolean getHasSecurity()
	{
		T.out(this, "getHasSecurity", bHasSecurity);
		return bHasSecurity;
	}

	public boolean isCleanupRequest()
	{
		T.out(this, "isCleanupRequest", new Boolean(false));
		return false;
	}

	public GatewayRequest[] getCleanupRequests()
	{
		T.out(this, "getCleanupRequests", 0);
		return null;
	}

	public String getCleanupType()
	{
		T.out(this, "getCleanupType");
		return "";
	}

	public int getCleanupId()
	{
		T.out(this, "getCleanupId", 0);
		return 0;
	}

	public int getCleanupAction()
	{
		T.out(this, "getCleanupAction", 0);
		return 0;
	}

	public boolean confirmationRequired()
	{
		T.out(this, "confirmationRequired", false);
		return false;
	}

	protected String toPaddedString(String s, int i)
	{
		T.in(this, "toPaddedString", s, new Integer(i));
		StringBuffer stringbuffer = new StringBuffer(i);
		if (s != null)
			stringbuffer.append(s);
		stringbuffer.setLength(i);
		String s1 = stringbuffer.toString();
		T.out(this, "toPaddedString", s1);
		return s1;
	}

	protected String toPaddedString(String s, int i, boolean flag)
	{
		T.in(this, "toPaddedString", flag ? "PASSWORD" : ((Object) (s)), new Integer(i));
		StringBuffer stringbuffer = new StringBuffer(i);
		if (s != null)
			stringbuffer.append(s);
		stringbuffer.setLength(i);
		String s1 = stringbuffer.toString();
		T.out(this, "toPaddedString", flag ? "PASSWORD" : ((Object) (s1)));
		return s1;
	}

	public void writeObject(DataOutputStream dataoutputstream)
		throws IOException
	{
		T.in(this, "writeObject");
		if (iFlowType == 5)
		{
			T.ln(this, "Type: FLOW_HANDSHAKE");
			if (locExchange != null)
			{
				dataoutputstream.writeUTF(locExchange.getLanguage());
				dataoutputstream.writeUTF(locExchange.getCountry());
				dataoutputstream.writeUTF(locExchange.getVariant());
			} else
			{
				dataoutputstream.writeUTF("en");
				dataoutputstream.writeUTF("US");
				dataoutputstream.writeUTF("");
			}
			if (iFlowVersion >= 0x301000)
				if (strServerJVM != null)
					dataoutputstream.writeUTF(strServerJVM);
				else
					dataoutputstream.writeUTF("");
			dataoutputstream.writeUTF(strServerSecurityClass);
			if (abytHandshake != null)
			{
				dataoutputstream.writeInt(abytHandshake.length);
				dataoutputstream.write(abytHandshake, 0, abytHandshake.length);
			} else
			{
				dataoutputstream.writeInt(0);
			}
		} else
		if (iFlowType == 6)
		{
			T.ln(this, "Type: FLOW_EXCEPTION");
			if (iFlowVersion >= 0x300000)
				dataoutputstream.writeBoolean(boolCloseHint);
			dataoutputstream.writeUTF(serverSideException);
		}
		T.out(this, "writeObject");
	}

	public void writeRootObject(DataOutputStream dataoutputstream)
		throws IOException
	{
		T.in(this, "writeRootObject", dataoutputstream);
		dataoutputstream.writeInt(0x47617465);
		dataoutputstream.writeInt(iFlowVersion);
		dataoutputstream.writeInt(iFlowType);
		dataoutputstream.writeInt(iMessageId);
		dataoutputstream.writeInt(iGatewayRc);
		if (iFlowVersion == 0x10100)
		{
			dataoutputstream.writeBytes(toPaddedString(strRequestType, 4));
		} else
		{
			dataoutputstream.writeInt(strRequestType.length());
			dataoutputstream.writeBytes(toPaddedString(strRequestType, strRequestType.length()));
		}
		if (iFlowVersion >= 0x200000)
			dataoutputstream.writeBoolean(bHasSecurity);
		dataoutputstream.writeInt(iDataWhichFollows);
		T.out(this, "writeRootObject");
	}

	protected String readPaddedString(DataInputStream datainputstream, int i)
		throws IOException
	{
		T.in(this, "readPaddedString", datainputstream, new Integer(i));
		byte abyte0[] = new byte[i];
		datainputstream.readFully(abyte0, 0, i);
		int j;
		for (j = 0; j < i && abyte0[j] != 0; j++);
		String s = null;
		if (j != 0)
			if (bSpecifyASCII)
				s = new String(abyte0, 0, j, "ASCII");
			else
				s = new String(abyte0, 0, 0, j);
		T.out(this, "readPaddedString", s);
		return s;
	}

	protected String readPaddedString(DataInputStream datainputstream, int i, boolean flag)
		throws IOException
	{
		T.in(this, "readPaddedString", datainputstream, new Integer(i));
		byte abyte0[] = new byte[i];
		datainputstream.readFully(abyte0, 0, i);
		int j;
		for (j = 0; j < i && abyte0[j] != 0; j++);
		String s = null;
		if (j != 0)
			if (bSpecifyASCII)
			{
				s = new String(abyte0, 0, j, "ASCII");
			} else
			{
				T.ln(this, "JVM doesn't support ASCII codepage");
				s = new String(abyte0, 0, 0, j);
			}
		T.out(this, "readPaddedString", "PASSWORD");
		return s;
	}

	public void readObject(DataInputStream datainputstream)
		throws IOException
	{
		T.in(this, "readObject", datainputstream);
		int i;
		if ((i = datainputstream.readInt()) != 0x47617465)
			throw new IOException(ClientMessages.getMessage(null, 57, Integer.toHexString(i)));
		iFlowVersion = datainputstream.readInt();
		iFlowType = datainputstream.readInt();
		iMessageId = datainputstream.readInt();
		iGatewayRc = datainputstream.readInt();
		if (iFlowVersion == 0x10100)
		{
			strRequestType = readPaddedString(datainputstream, 4);
		} else
		{
			int j = datainputstream.readInt();
			strRequestType = readPaddedString(datainputstream, j);
		}
		if (iFlowVersion >= 0x200000)
			bHasSecurity = datainputstream.readBoolean();
		iDataWhichFollows = datainputstream.readInt();
		if (T.bTrace || T.bDebug)
		{
			String s = Integer.toString(iFlowVersion, 16);
			T.traceln(ClientMessages.getMessage(null, 2, strRequestType, s, iFlowType, iGatewayRc, iDataWhichFollows));
		}
		if (iFlowType == 5)
		{
			T.ln(this, "Type: FLOW_HANDSHAKE");
			String s1 = datainputstream.readUTF();
			String s2 = datainputstream.readUTF();
			String s3 = datainputstream.readUTF();
			T.ln(this, "Read Locale : Language = {0}, Country = {1}, Variant = {2}", s1, s2, s3);
			try
			{
				locExchange = new Locale(s1, s2, s3);
			}
			catch (NoClassDefFoundError noclassdeffounderror)
			{
				locExchange = null;
			}
			if (iFlowVersion >= 0x301000)
				strServerJVM = datainputstream.readUTF();
			strServerSecurityClass = datainputstream.readUTF();
			T.ln(this, "Server security class = {0}", strServerSecurityClass);
			int k = datainputstream.readInt();
			if (k > 0)
			{
				abytHandshake = new byte[k];
				datainputstream.readFully(abytHandshake);
				T.ln(this, "Read {0} bytes of handshake", new Integer(k));
			} else
			{
				abytHandshake = null;
			}
		} else
		if (iFlowType == 6)
		{
			T.ln(this, "Type: FLOW_EXCEPTION");
			if (iFlowVersion >= 0x300000)
				boolCloseHint = datainputstream.readBoolean();
			serverSideException = datainputstream.readUTF();
			T.ln(this, "Close hint = {0}, Server side exception = {1}", new Boolean(boolCloseHint), serverSideException);
		}
		T.out(this, "readObject");
	}

	protected void setContentsFromPartner(GatewayRequest gatewayrequest)
		throws IOException
	{
		T.in(this, "setContentsFromPartner", gatewayrequest);
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
		gatewayrequest.writeObject(dataoutputstream);
		ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(bytearrayoutputstream.toByteArray());
		DataInputStream datainputstream = new DataInputStream(bytearrayinputstream);
		readObject(datainputstream);
		T.out(this, "setContentsFromPartner");
	}

	protected void localFlowOccurred()
	{
	}

	protected int getPasswordOffset()
	{
		return -1;
	}

	public boolean isThisQuiesceBlockingWork()
	{
		return true;
	}

	public int getLogicalWorkState()
	{
		return 0;
	}

	public boolean isThisAllowedDuringQuiesce()
	{
		return false;
	}

	static 
	{
		byte abyte0[] = new byte[2];
		try
		{
			String s = new String(abyte0, 0, abyte0.length, "ASCII");
			bSpecifyASCII = true;
		}
		catch (UnsupportedEncodingException unsupportedencodingexception)
		{
			bSpecifyASCII = false;
		}
	}
}

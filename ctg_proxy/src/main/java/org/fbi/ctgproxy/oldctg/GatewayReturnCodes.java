package org.fbi.ctgproxy.oldctg;


public interface GatewayReturnCodes
{
	public static final int ERROR_BASE = 61440;
	public static final int ERROR_CONNECTION_FAILED = 61441;
	public static final int ERROR_UNKNOWN_REQUEST_TYPE = 61442;
	public static final int ERROR_REPLY_MISMATCH = 61443;
	public static final int ERROR_GATEWAY_CLOSED = 61444;
	public static final int ERROR_WORK_WAS_REFUSED = 61445;
	public static final int ERROR_GATEWAY_EXCEPTION = 61446;
	public static final int ERROR_NOT_AUTHORIZED = 61447;
	public static final int ERROR_NOT_SUPPORTED = 61448;
	public static final int ERROR_XA_SUPPORT_NOT_ENABLED = 61449;
	public static final int ERROR_GATEWAY_BACK_LEVEL = 61450;
	public static final String astrGateway_Rc[] = {
		"ERROR_CONNECTION_FAILED", "ERROR_UNKNOWN_REQUEST_TYPE", "ERROR_REPLY_MISMATCH", "ERROR_GATEWAY_CLOSED", "ERROR_WORK_WAS_REFUSED", "ERROR_GATEWAY_EXCEPTION", "ERROR_NOT_AUTHORIZED", "ERROR_NOT_SUPPORTED", "ERROR_XA_NOT_SUPPORTED", "ERROR_GATEWAY_BACK_LEVEL"
	};
	public static final String strINVALID_GATEWAY_RC = "ERROR_UNKNOWN_GATEWAY_RC";

}

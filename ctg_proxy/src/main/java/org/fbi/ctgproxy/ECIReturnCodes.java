// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ECIReturnCodes.java

package org.fbi.ctgproxy;


public interface ECIReturnCodes
{

	public static final int ECI_NO_ERROR = 0;
	public static final int ECI_ERR_INVALID_DATA_LENGTH = -1;
	public static final int ECI_ERR_INVALID_EXTEND_MODE = -2;
	public static final int ECI_ERR_NO_CICS = -3;
	public static final int ECI_ERR_CICS_DIED = -4;
	public static final int ECI_ERR_REQUEST_TIMEOUT = -5;
	public static final int ECI_ERR_NO_REPLY = -5;
	public static final int ECI_ERR_RESPONSE_TIMEOUT = -6;
	public static final int ECI_ERR_TRANSACTION_ABEND = -7;
	/**
	 * @deprecated Field ECI_ERR_EXEC_NOT_RESIDENT is deprecated
	 */
	public static final int ECI_ERR_EXEC_NOT_RESIDENT = -8;
	public static final int ECI_ERR_LUW_TOKEN = -8;
	public static final int ECI_ERR_SYSTEM_ERROR = -9;
	public static final int ECI_ERR_NULL_WIN_HANDLE = -10;
	public static final int ECI_ERR_NULL_MESSAGE_ID = -12;
	public static final int ECI_ERR_THREAD_CREATE_ERROR = -13;
	public static final int ECI_ERR_INVALID_CALL_TYPE = -14;
	public static final int ECI_ERR_ALREADY_ACTIVE = -15;
	public static final int ECI_ERR_RESOURCE_SHORTAGE = -16;
	public static final int ECI_ERR_NO_SESSIONS = -17;
	public static final int ECI_ERR_NULL_SEM_HANDLE = -18;
	public static final int ECI_ERR_INVALID_DATA_AREA = -19;
	public static final int ECI_ERR_INVALID_VERSION = -21;
	public static final int ECI_ERR_UNKNOWN_SERVER = -22;
	public static final int ECI_ERR_CALL_FROM_CALLBACK = -23;
	public static final int ECI_ERR_MORE_SYSTEMS = -25;
	public static final int ECI_ERR_NO_SYSTEMS = -26;
	public static final int ECI_ERR_SECURITY_ERROR = -27;
	public static final int ECI_ERR_MAX_SYSTEMS = -28;
	public static final int ECI_ERR_MAX_SESSIONS = -29;
	public static final int ECI_ERR_ROLLEDBACK = -30;
	public static final int ECI_ERR_NO_MSG_QUALS = -1000;
	public static final int ECI_ERR_MSG_QUAL_IN_USE = -1001;
	public static final String astrCics_Rc[] = {
		"ECI_NO_ERROR", "ECI_ERR_INVALID_DATA_LENGTH", "ECI_ERR_INVALID_EXTEND_MODE", "ECI_ERR_NO_CICS", "ECI_ERR_CICS_DIED", "ECI_ERR_REQUEST_TIMEOUT_OR_ERR_NO_REPLY", "ECI_ERR_RESPONSE_TIMEOUT", "ECI_ERR_TRANSACTION_ABEND", "ERR_LUW_TOKEN", "ECI_ERR_SYSTEM_ERROR", 
		"ECI_ERR_NULL_WIN_HANDLE", "ECI_UNKNOWN_CICS_RC", "ECI_ERR_NULL_MESSAGE_ID", "ECI_ERR_THREAD_CREATE_ERROR", "ECI_ERR_INVALID_CALL_TYPE", "ECI_ERR_ALREADY_ACTIVE", "ECI_ERR_RESOURCE_SHORTAGE", "ECI_ERR_NO_SESSIONS", "ECI_ERR_NULL_SEM_HANDLE", "ECI_ERR_INVALID_DATA_AREA", 
		"ECI_UNKNOWN_CICS_RC", "ECI_ERR_INVALID_VERSION", "ECI_ERR_UNKNOWN_SERVER", "ECI_ERR_CALL_FROM_CALLBACK", "ECI_UNKNOWN_CICS_RC", "ECI_ERR_MORE_SYSTEMS", "ECI_ERR_NO_SYSTEMS", "ECI_ERR_SECURITY_ERROR", "ECI_ERR_MAX_SYSTEMS", "ECI_ERR_MAX_SESSIONS", 
		"ECI_ERR_ROLLEDBACK"
	};
	public static final String astrCics_Rc2[] = {
		"ECI_ERR_NO_MSG_QUALS", "ECI_ERR_MSG_QUAL_IN_USE"
	};
	public static final String strINVALID_CICS_RC = "ECI_UNKNOWN_CICS_RC";

}

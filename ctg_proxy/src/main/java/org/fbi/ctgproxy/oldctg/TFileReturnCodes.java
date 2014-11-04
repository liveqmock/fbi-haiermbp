package org.fbi.ctgproxy.oldctg;


public interface TFileReturnCodes
{
	public static final int TFILE_NO_ERROR = 0;
	public static final int TFILE_ERR_UNKNOWN = 1;
	public static final int TFILE_ERR_INVALID_PATH = 2;
	public static final int TFILE_ERR_PERMISSION_DENIED = 3;
	public static final int TFILE_ERR_DIRECTORY = 4;
	public static final int TFILE_ERR_CHANGE_WHEN_RUNNING = 5;
}

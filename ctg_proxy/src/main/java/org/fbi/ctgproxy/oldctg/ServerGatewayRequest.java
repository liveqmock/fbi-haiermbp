package org.fbi.ctgproxy.oldctg;


public interface ServerGatewayRequest
{
	public static final String strNativeLib = "ctgjni";
	public static final String strNativeLibrary = "ctgjni: ";

	public abstract boolean isLocalMode();

	public abstract void setLocalMode(boolean flag);

	public abstract boolean execute()
		throws Throwable;

	public abstract boolean executeCleanup()
		throws Throwable;
}

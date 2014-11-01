// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ServerGatewayRequest.java

package org.fbi.ctgserver.sbsctgserver;


public interface ServerGatewayRequest
{

	public static final String CLASS_VERSION = "@(#) java/com/ibm/ctg/server/ServerGatewayRequest.java, client_java, c602, c602-20060418 1.3.2.4 04/08/24 22:38:59";
	public static final String COPYRIGHT_NOTICE = "(c) Copyright IBM Corporation 2000.";
	public static final String strNativeLib = "ctgjni";
	public static final String strNativeLibrary = "ctgjni: ";

	public abstract boolean isLocalMode();

	public abstract void setLocalMode(boolean flag);

	public abstract boolean execute()
		throws Throwable;

	public abstract boolean executeCleanup()
		throws Throwable;
}

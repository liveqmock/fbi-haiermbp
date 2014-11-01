// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   OSVersion.java

package org.fbi.ctgserver.sbsctgserver.util;


// Referenced classes of package com.ibm.ctg.util:
//			OSInfo

public final class OSVersion
{

	public static final String CLASS_VERSION = "@(#) java/com/ibm/ctg/util/OSVersion.java, cf_interoperability, c602, c602-20060418 1.3.1.7 06/01/04 18:28:41";
	private static final String copyright_notice = "Licensed Materials - Property of IBM (c) Copyright IBM Corp. 2004, 2006 All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.";
	public static final String SYSTEM_VERSION = "6.0";
	public static final String SYSTEM_LEVEL = "02";
	public static final OSInfo OPERATING_SYSTEM;
	public static final String BUILD_LEVEL = "c602-20060419";

	public OSVersion()
	{
	}

	public static boolean onSupportedPlatform()
	{
		return !OPERATING_SYSTEM.equals(OSInfo.UNKNOWN);
	}

	static 
	{
		String s = System.getProperty("os.name");
		String s1 = System.getProperty("os.arch");
		if (s.equals("AIX"))
			OPERATING_SYSTEM = OSInfo.AIX;
		else
		if (s.equals("Solaris") || s.equals("SunOS"))
			OPERATING_SYSTEM = OSInfo.SOLARIS;
		else
		if (s.equals("z/OS"))
			OPERATING_SYSTEM = OSInfo.ZOS;
		else
		if (s.equals("Linux"))
		{
			if (s1.startsWith("s390"))
				OPERATING_SYSTEM = OSInfo.LINUX_ZSERIES;
			else
			if (s1.endsWith("86"))
				OPERATING_SYSTEM = OSInfo.LINUX_INTEL;
			else
			if (s1.startsWith("ppc"))
				OPERATING_SYSTEM = OSInfo.LINUX_PPC;
			else
				OPERATING_SYSTEM = OSInfo.UNKNOWN;
		} else
		if (s.equals("HP-UX"))
			OPERATING_SYSTEM = OSInfo.HPUX;
		else
		if (s.startsWith("Windows"))
			OPERATING_SYSTEM = OSInfo.WINDOWS;
		else
			OPERATING_SYSTEM = OSInfo.UNKNOWN;
	}
}

package org.fbi.ctgproxy.util;


public final class OSInfo
{

	public static final String CLASS_VERSION = "@(#) java/com/ibm/ctg/util/OSInfo.java, cf_interoperability, c602, c602-20060418 1.4 05/04/13 15:21:18";
	private static final String copyright_notice = "Licensed Materials - Property of IBM (c) Copyright IBM Corp. 2004     All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.";
	public static final OSInfo WINDOWS = new OSInfo("Windows");
	public static final OSInfo AIX = new OSInfo("AIX");
	public static final OSInfo SOLARIS = new OSInfo("Solaris");
	public static final OSInfo LINUX_INTEL = new OSInfo("Linux on Intel");
	public static final OSInfo LINUX_ZSERIES = new OSInfo("Linux on zSeries");
	public static final OSInfo LINUX_PPC = new OSInfo("Linux on POWER");
	public static final OSInfo HPUX = new OSInfo("HPUX");
	public static final OSInfo ZOS = new OSInfo("z/OS");
	public static final OSInfo UNKNOWN = new OSInfo("Unknown");
	private String name;

	private OSInfo(String s)
	{
		name = s;
	}

	public boolean equals(Object obj)
	{
		OSInfo osinfo = (OSInfo)obj;
		return name.equals(osinfo.getName());
	}

	public String getName()
	{
		return name;
	}

}

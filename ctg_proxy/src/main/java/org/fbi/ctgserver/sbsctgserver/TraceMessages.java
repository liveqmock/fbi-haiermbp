// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TraceMessages.java

package org.fbi.ctgserver.sbsctgserver;

import com.ibm.ctg.client.T;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class TraceMessages
{

	public static final String CLASS_VERSION = "@(#) java/com/ibm/ctg/server/TraceMessages.java, client_java, c602, c602-20060418 1.5 01/07/10 13:07:12";
	private static final String COPYRIGHT_NOTICE = "(c) Copyright IBM Corporation 2000.";
	private static final String RBBASENAME = "com.ibm.ctg.server.TraceResourceBundle";
	private static final String astrMsgKey[] = {
		"msg0", "msg1", "msg2", "msg3", "msg4", "msg5", "msg6", "msg7", "msg8", "msg9", 
		"msg10", "msg11", "msg12", "msg13", "msg14", "msg15", "msg16", "msg17", "msg18", "msg19", 
		"msg20", "msg21", "msg22", "msg23", "msg24", "msg25", "msg26", "msg27", "msg28", "msg29", 
		"msg30", "msg31", "msg32", "msg33", "msg34", "msg35", "msg36", "msg37", "msg38", "msg39", 
		"msg40", "msg41", "msg42", "msg43", "msg44", "msg45", "msg46", "msg47", "msg48", "msg49", 
		"msg50", "msg51", "msg52", "msg53", "msg54", "msg55", "msg56", "msg57", "msg58", "msg59", 
		"msg60", "msg61", "msg62", "msg63", "msg64", "msg65", "msg66", "msg67", "msg68", "msg69", 
		"msg70", "msg71", "msg72", "msg73", "msg74", "msg75", "msg76", "msg77", "msg78", "msg79", 
		"msg80", "msg81", "msg82", "msg83", "msg84", "msg85", "msg86", "msg87", "msg88", "msg89", 
		"msg90", "msg91", "msg92", "msg93", "msg94", "msg95", "msg96", "msg97", "msg98", "msg99", 
		"msg100", "msg101", "msg102", "msg103", "msg104", "msg105", "msg106", "msg107", "msg108", "msg109"
	};
	private static final String MISSINGRESOURCEMSG = "CCLnnnnI: ";
	private static final String DEFAULTSTRING = "";
	private static ResourceBundle rbDefault;
	private static Object aobjInserts[] = new Object[5];

	private TraceMessages()
	{
	}

	public static String getMessage(int i)
	{
		String s;
		try
		{
			s = rbDefault.getString(astrMsgKey[i]);
		}
		catch (MissingResourceException missingresourceexception)
		{
			s = getDefaultMessage(i);
		}
		return s;
	}

	public static synchronized String getMessage(int i, String s)
	{
		aobjInserts[0] = s;
		return getMsg(i, 1, 0);
	}

	public static synchronized String getMessage(int i, int j)
	{
		aobjInserts[0] = Integer.toString(j);
		return getMsg(i, 0, 0);
	}

	public static synchronized String getMessage(int i, Throwable throwable)
	{
		aobjInserts[0] = throwable.toString();
		return getMsg(i, 0, 0);
	}

	public static synchronized String getMessage(int i, String s, Throwable throwable)
	{
		aobjInserts[0] = s;
		aobjInserts[1] = throwable.toString();
		return getMsg(i, 0, 0);
	}

	public static synchronized String getMessage(int i, int j, String s)
	{
		aobjInserts[0] = Integer.toString(j);
		aobjInserts[1] = s;
		return getMsg(i, 1, 1);
	}

	public static synchronized String getMessage(int i, String s, int j)
	{
		aobjInserts[0] = s;
		aobjInserts[1] = Integer.toString(j);
		return getMsg(i, 0, 0);
	}

	public static synchronized String getMessage(int i, String s, String s1)
	{
		aobjInserts[0] = s;
		aobjInserts[1] = s1;
		return getMsg(i, 0, 0);
	}

	public static synchronized String getMessage(int i, int j, int k)
	{
		aobjInserts[0] = Integer.toString(j);
		aobjInserts[1] = Integer.toString(k);
		return getMsg(i, 0, 0);
	}

	public static synchronized String getMessage(int i, int j, int k, int l)
	{
		aobjInserts[0] = Integer.toString(j);
		aobjInserts[1] = Integer.toString(k);
		aobjInserts[2] = Integer.toString(l);
		return getMsg(i, 0, 0);
	}

	private static String getDefaultMessage(int i)
	{
		String s;
		try
		{
			s = rbDefault.getString(astrMsgKey[0]) + " [" + i + "]";
		}
		catch (MissingResourceException missingresourceexception)
		{
			T.ex(null, missingresourceexception);
			s = missingresourceexception.toString();
		}
		return s;
	}

	private static String getMsg(int i, int j, int k)
	{
		if (j > 0)
			validateStrings(j, k);
		String s1;
		try
		{
			String s = rbDefault.getString(astrMsgKey[i]);
			s1 = MessageFormat.format(s, aobjInserts);
		}
		catch (MissingResourceException missingresourceexception)
		{
			T.ex(null, missingresourceexception);
			s1 = getDefaultMessage(i);
		}
		catch (NullPointerException nullpointerexception)
		{
			T.ex(null, nullpointerexception);
			s1 = getDefaultMessage(i);
		}
		return s1;
	}

	private static void validateStrings(int i, int j)
	{
		try
		{
			if (i > aobjInserts.length)
				i = aobjInserts.length;
			if (j > aobjInserts.length - 1)
				j = aobjInserts.length - 1;
			for (int k = j; k < i + j; k++)
				if (aobjInserts[k] == null)
					aobjInserts[k] = "";

		}
		catch (Throwable throwable)
		{
			T.ex(null, throwable);
		}
	}

	static 
	{
		try
		{
			rbDefault = ResourceBundle.getBundle("com.ibm.ctg.server.TraceResourceBundle");
		}
		catch (MissingResourceException missingresourceexception)
		{
			T.ex(null, missingresourceexception);
			throw missingresourceexception;
		}
	}
}

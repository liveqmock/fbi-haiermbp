// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ServerMessages.java

package org.fbi.ctgserver.sbsctgserver;

import com.ibm.ctg.client.T;
import org.fbi.ctgserver.sbsctgserver.util.OSInfo;
import org.fbi.ctgserver.sbsctgserver.util.OSVersion;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class ServerMessages
{

	public static final String CLASS_VERSION = "@(#) java/com/ibm/ctg/server/ServerMessages.java, client_java, c000 1.31 04/08/11 15:20:23";
	private static final String COPYRIGHT_NOTICE = "(c) Copyright IBM Corporation 2000, 2004.";
	private static final String RBBASENAME = "com.ibm.ctg.server.ServerResourceBundle";
	private static final String MISSINGRESOURCEMSG = "CCLnnnnI: ";
	private static final String DEFAULTSTRING = "";
	private static ResourceBundle rbDefault;
	private static ResourceBundle rbUK;

	private ServerMessages()
	{
	}

	public static String getInsert(String s)
	{
		T.in(null, "ServerMessages: getInsert", s);
		String s1 = "";
		try
		{
			s1 = rbDefault.getString(s);
		}
		catch (MissingResourceException missingresourceexception)
		{
			T.ex(null, missingresourceexception);
		}
		T.out(null, "getInsert", s1);
		return s1;
	}

	public static void outputHelp(PrintStream printstream)
	{
		T.in(null, "outputHelp");
		int ai[] = null;
		if (OSVersion.OPERATING_SYSTEM.equals(OSInfo.ZOS))
			ai = (new int[] {
				6548, 0, 6593, 0, 6549, 0, 6575, 6583, 6584, 6585, 
				6586, 6587, 6588, 6590, 6595, 6596, 6598, 6576, 6578, 6579, 
				8816, 0, 6591
			});
		else
			ai = (new int[] {
				6548, 0, 6593, 0, 6549, 0, 6575, 6594, 6583, 6584, 
				6585, 6586, 6587, 6599, 6590, 6595, 6596, 6598, 6576, 6578, 
				6579, 8816, 0, 6591
			});
		for (int i = 0; i < ai.length; i++)
			if (ai[i] == 0)
				printstream.println();
			else
				printstream.println(getMessage((new Integer(ai[i])).toString()));

		T.out(null, "outputHelp");
	}

	private static String getLocale()
	{
		return Locale.getDefault().getLanguage();
	}

	public static String getMessage(String s, Object aobj[], boolean flag)
	{
		T.in(null, "ServerMessages.getMessage", s, ((Object) (aobj)), new Boolean(flag));
		boolean flag1 = false;
		if (aobj != null)
		{
			int i = aobj.length;
			for (int j = 0; j < aobj.length; j++)
				if (aobj[j] == null)
					aobj[j] = "";

		}
		String s1 = "";
		String s3 = "";
		try
		{
			String s2 = rbUK.getString(s);
			if (flag)
				s2 = rbDefault.getString(s);
			s3 = MessageFormat.format(s2, aobj);
		}
		catch (MissingResourceException missingresourceexception)
		{
			T.ex(null, missingresourceexception);
		}
		catch (ClassCastException classcastexception)
		{
			T.ex(null, classcastexception);
		}
		catch (NullPointerException nullpointerexception)
		{
			T.ex(null, nullpointerexception);
		}
		if (s3 == null || s3.equals(""))
			s3 = getDefaultMessage(s);
		T.out(null, "ServerMessages.getMessage", s3);
		return s3;
	}

	public static String getMessage(String s, Object aobj[])
	{
		return getMessage(s, aobj, true);
	}

	public static String getMessage(String s)
	{
		return getMessage(s, null, true);
	}

	private static String getDefaultMessage(String s)
	{
		T.in(null, "ServerMessages.getDefaultMessage", s);
		String s1 = getMessage("65XX", new Object[] {
			s
		});
		T.out(null, "ServerMessages.getDefaultMessage", s1);
		return s1;
	}

	static 
	{
		try
		{
			rbDefault = ResourceBundle.getBundle("com.ibm.ctg.server.ServerResourceBundle");
			rbUK = ResourceBundle.getBundle("com.ibm.ctg.server.ServerResourceBundle", new Locale("en"));
		}
		catch (MissingResourceException missingresourceexception)
		{
			T.ex(null, missingresourceexception);
			throw missingresourceexception;
		}
	}
}

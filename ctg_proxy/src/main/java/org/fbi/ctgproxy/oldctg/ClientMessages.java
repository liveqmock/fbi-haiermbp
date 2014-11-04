package org.fbi.ctgproxy.oldctg;

import java.text.MessageFormat;
import java.util.MissingResourceException;

public final class ClientMessages
{
	private static final String RBBASENAME = "ClientResourceBundle";
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
		"msg100"
	};
	private static final String MISSINGRESOURCEMSG = "CCLnnnnI: ";
	private static final String DEFAULTSTRING = "";
	private static ResourceWrapper rbDefault;
	private static Object aobjInserts[] = new Object[10];

	private ClientMessages()
	{
	}

	public static String getMessage(ResourceWrapper resourcewrapper, int i)
	{
		resourcewrapper = validateResourceBundle(resourcewrapper);
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", resourcewrapper, new Integer(i));
		String s;
		try
		{
			s = resourcewrapper.getString(astrMsgKey[i]);
		}
		catch (MissingResourceException missingresourceexception)
		{
			s = getDefaultMessage(i);
		}
		return s;
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, String s)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", resourcewrapper, new Integer(i), s);
		aobjInserts[0] = s;
		return getMsg(resourcewrapper, i, 1);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, int j)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", resourcewrapper, new Integer(i), new Integer(j));
		aobjInserts[0] = Integer.toString(j);
		return getMsg(resourcewrapper, i, 0);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, boolean flag)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", resourcewrapper, new Integer(i), new Boolean(flag));
		aobjInserts[0] = String.valueOf(flag);
		return getMsg(resourcewrapper, i, 0);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, Throwable throwable)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, throwable);
		aobjInserts[0] = throwable.toString();
		return getMsg(resourcewrapper, i, 0);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, int j, int k)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, new Integer(j), new Integer(k));
		aobjInserts[0] = Integer.toString(j);
		aobjInserts[1] = Integer.toString(k);
		return getMsg(resourcewrapper, i, 0);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, String s, int j)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, s, new Integer(j));
		aobjInserts[0] = s;
		aobjInserts[1] = Integer.toString(j);
		return getMsg(resourcewrapper, i, 1);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, String s, int j, Throwable throwable)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, s, new Integer(j), throwable);
		aobjInserts[0] = s;
		aobjInserts[1] = Integer.toString(j);
		aobjInserts[2] = throwable.toString();
		return getMsg(resourcewrapper, i, 1);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, String s, int j, int k)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, s, new Integer(j), new Integer(k));
		aobjInserts[0] = s;
		aobjInserts[1] = Integer.toString(j);
		aobjInserts[2] = Integer.toString(k);
		return getMsg(resourcewrapper, i, 1);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, String s, int j, int k, int l, int i1)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, s, new Integer(j), new Integer(k), new Integer(l), new Integer(i1));
		aobjInserts[0] = s;
		aobjInserts[1] = Integer.toString(j);
		aobjInserts[2] = Integer.toString(k);
		aobjInserts[3] = Integer.toString(l);
		aobjInserts[4] = Integer.toString(i1);
		return getMsg(resourcewrapper, i, 1);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, String s, String s1, int j, int k)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, s, s1, new Integer(j), new Integer(k));
		aobjInserts[0] = s;
		aobjInserts[1] = s1;
		aobjInserts[2] = Integer.toString(j);
		aobjInserts[3] = Integer.toString(k);
		return getMsg(resourcewrapper, i, 2);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, String s, String s1)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, s, s1);
		aobjInserts[0] = s;
		aobjInserts[1] = s1;
		return getMsg(resourcewrapper, i, 2);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, String s, String s1, String s2)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, s, s1, s2);
		aobjInserts[0] = s;
		aobjInserts[1] = s1;
		aobjInserts[2] = s2;
		return getMsg(resourcewrapper, i, 3);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, String s, String s1, String s2, int j, int k)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, s, s1, s2, new Integer(j), new Integer(k));
		aobjInserts[0] = s;
		aobjInserts[1] = s1;
		aobjInserts[2] = s2;
		aobjInserts[3] = Integer.toString(j);
		aobjInserts[4] = Integer.toString(k);
		return getMsg(resourcewrapper, i, 3);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, String s, String s1, String s2, String s3, String s4, int j,
			int k)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, s, s1, s2, s3, s4, new Integer(j), new Integer(k));
		aobjInserts[0] = s;
		aobjInserts[1] = s1;
		aobjInserts[2] = s2;
		aobjInserts[3] = s3;
		aobjInserts[4] = s4;
		aobjInserts[5] = Integer.toString(j);
		aobjInserts[6] = Integer.toString(k);
		return getMsg(resourcewrapper, i, 5);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, String s, String s1, String s2, String s3, String s4, int j,
			int k, boolean flag)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, s, s1, s2, s3, s4, new Integer(j), new Integer(k));
		aobjInserts[0] = s;
		aobjInserts[1] = s1;
		aobjInserts[2] = s2;
		aobjInserts[3] = s3;
		aobjInserts[4] = s4;
		aobjInserts[5] = Integer.toString(j);
		aobjInserts[6] = Integer.toString(k);
		aobjInserts[7] = String.valueOf(flag);
		return getMsg(resourcewrapper, i, 5);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, int j, int k, int l, String s, int i1)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, new Integer(j), new Integer(k), new Integer(l), s, new Integer(i1));
		aobjInserts[0] = Integer.toString(j);
		aobjInserts[1] = Integer.toString(k);
		aobjInserts[2] = Integer.toString(l);
		aobjInserts[3] = s;
		aobjInserts[4] = Integer.toString(i1);
		return getMsg(resourcewrapper, i, 4);
	}

	public static synchronized String getMessage(ResourceWrapper resourcewrapper, int i, String s, String s1, int j, int k, int l)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMessage()", new Integer(i), resourcewrapper, s, s1, new Integer(j), new Integer(k), new Integer(l));
		aobjInserts[0] = s;
		aobjInserts[1] = s1;
		aobjInserts[2] = Integer.toString(j);
		aobjInserts[3] = Integer.toString(k);
		aobjInserts[4] = Integer.toString(l);
		return getMsg(resourcewrapper, i, 0);
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
		if (T.bDebug)
			T.out(null, "ClientMessages: getDefaultMessage()", s);
		return s;
	}

	private static ResourceWrapper validateResourceBundle(ResourceWrapper resourcewrapper)
	{
		if (resourcewrapper == null)
			resourcewrapper = rbDefault;
		if (T.bDebug)
			T.out(null, "ClientMessages: validateResourceBundle()", resourcewrapper);
		return resourcewrapper;
	}

	private static String getMsg(ResourceWrapper resourcewrapper, int i, int j)
	{
		if (T.bDebug)
			T.in(null, "ClientMessages: getMsg()", resourcewrapper, new Integer(i), new Integer(j));
		resourcewrapper = validateResourceBundle(resourcewrapper);
		if (j > 0)
			validateStrings(j);
		String s = null;
		String s1;
		try
		{
			s = resourcewrapper.getString(astrMsgKey[i]);
			s1 = MessageFormat.format(s, aobjInserts);
		}
		catch (NoClassDefFoundError noclassdeffounderror)
		{
			s1 = s;
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
		if (T.bDebug)
			T.out(null, "ClientMessages: getMsg()", s1);
		return s1;
	}

	private static void validateStrings(int i)
	{
		if (i > aobjInserts.length)
			i = aobjInserts.length;
		for (int j = 0; j < i; j++)
			if (aobjInserts[j] == null)
				aobjInserts[j] = "";

		if (T.bDebug)
			T.out(null, "ClientMessages: validateStrings()");
	}

	static
	{
		try
		{
			rbDefault = ResourceWrapper.getBundle("ClientResourceBundle");
		}
		catch (MissingResourceException missingresourceexception)
		{
			T.ex(null, missingresourceexception);
			throw missingresourceexception;
		}
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MsgQualMgr.java

package org.fbi.ctgserver.sbsctgserver;

import com.ibm.ctg.client.T;

import java.util.Hashtable;

public class MsgQualMgr
{
	static class MsgQualAssoc
	{

		private Integer cmIndex;
		private int luw;

		Integer getConnectionManagerIndex()
		{
			return cmIndex;
		}

		int getLuw()
		{
			return luw;
		}

		void setLuw(int i)
		{
			luw = i;
		}

		MsgQualAssoc(Integer integer)
		{
			luw = 0;
			cmIndex = integer;
		}
	}


	public static final String CLASS_VERSION = "@(#) java/com/ibm/ctg/server/MsgQualMgr.java, client_java, c602, c602-20060418 1.5 02/04/24 10:59:12";
	private static final String COPYRIGHT_NOTICE = "(c) Copyright IBM Corporation 2001, 2002.";
	private static final int FIRST_MQ = -32767;
	private static final int START_MQ = 0;
	private static final int LAST_MQ = 32767;
	private static final MsgQualAssoc INVALID_CM = new MsgQualAssoc(new Integer(-22443));
	private static MsgQualMgr _instance = new MsgQualMgr();
	private volatile int count;
	private volatile Hashtable usedMQs;

	private MsgQualMgr()
	{
		count = 0;
		usedMQs = new Hashtable(50);
	}

	public static MsgQualMgr getInstance()
	{
		return _instance;
	}

	synchronized boolean lockMsgQual(int i, Integer integer)
	{
		Integer integer1 = new Integer(i);
		T.in(this, "lockMsgQual", integer1, integer);
		boolean flag = false;
		if (!usedMQs.containsKey(integer1))
		{
			MsgQualAssoc msgqualassoc = new MsgQualAssoc(integer);
			usedMQs.put(integer1, msgqualassoc);
			flag = true;
			if (T.bDebug)
				T.ln(this, "lockMsgQual(), Message Qualifier {0} locked", integer1);
		}
		T.out(this, "lockMsgQual", new Boolean(flag));
		return flag;
	}

	synchronized int requestMsgQual(Integer integer)
		throws Exception
	{
		T.in(this, "requestMsgQual", integer);
		boolean flag = false;
		int i = count;
		int j = 0;
		do
		{
			Integer integer1 = new Integer(count);
			if (!usedMQs.containsKey(integer1))
			{
				flag = true;
				j = count;
				lockMsgQual(count, integer);
			}
			if (count >= 32767 || count < -32767)
				count = -32767;
			else
				count++;
		} while (!flag && count != i);
		if (!flag)
		{
			Exception exception = new Exception("No Message Qualifiers");
			T.ex(this, exception);
			throw exception;
		} else
		{
			T.out(this, "requestMsgQual", j);
			return j;
		}
	}

	synchronized void freeMsgQual(int i)
	{
		Integer integer = new Integer(i);
		T.in(this, "freeMsgQual", integer);
		usedMQs.remove(integer);
		T.out(this, "freeMsgQual");
	}

	synchronized void invalidateMsgQual(int i)
	{
		Integer integer = new Integer(i);
		T.in(this, "invalidateMsgQual", integer);
		if (usedMQs.containsKey(integer))
			usedMQs.remove(integer);
		usedMQs.put(integer, INVALID_CM);
		T.out(this, "invalidateMsgQual");
	}

	synchronized void associateLUW(int i, int j)
	{
		Integer integer = new Integer(i);
		Integer integer1 = new Integer(j);
		T.in(this, "associateLUW", integer, integer1);
		MsgQualAssoc msgqualassoc = (MsgQualAssoc)usedMQs.get(integer);
		if (msgqualassoc != null)
		{
			msgqualassoc.setLuw(j);
			if (T.bDebug)
				T.ln(this, "Associating LUW {0} with Message Qualifier {1}", integer1, integer);
		}
		T.out(this, "associateLUW");
	}

	synchronized Integer getMsgQualCM(int i)
	{
		Integer integer = new Integer(i);
		T.in(this, "getMsgQualState", integer);
		MsgQualAssoc msgqualassoc = (MsgQualAssoc)usedMQs.get(integer);
		Integer integer1 = null;
		if (msgqualassoc != null)
			integer1 = msgqualassoc.getConnectionManagerIndex();
		T.out(this, "getMsgQualState", integer1);
		return integer1;
	}

	synchronized int getMsgQualLuw(int i)
	{
		Integer integer = new Integer(i);
		T.in(this, "getMsgQualLuw", integer);
		MsgQualAssoc msgqualassoc = (MsgQualAssoc)usedMQs.get(integer);
		int j = 0;
		if (msgqualassoc != null)
			j = msgqualassoc.getLuw();
		T.out(this, "getMsgQualLuw", j);
		return j;
	}

}

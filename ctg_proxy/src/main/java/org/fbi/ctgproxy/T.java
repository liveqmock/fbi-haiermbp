package org.fbi.ctgproxy;


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;


public class T
{
	private static PrintStream prsTrace = null;
	private static PrintStream prsLog = null;
	private static SimpleDateFormat dfTime;
	public static final int PRODUCT = 32;
	private static boolean bTraceOn = false;
	public static final int ENTRY = 1;
	private static boolean bEntryOn = false;
	public static final int EXIT = 2;
	private static boolean bExitOn = false;
	public static final int LINES = 4;
	private static boolean bLinesOn = false;
	public static final int STACK = 8;
	private static boolean bStackOn = false;
	public static final int TIMING = 16;
	private static boolean bTimingOn = true;
	public static final int FULL_DATADUMP = 64;
	private static boolean bNoDumpTruncation = false;
	private static boolean bNoDumpOffset = false;
	private static int hexDumpTruncation = 80;
	private static boolean invalidTruncationSize = false;
	private static boolean invalidDumpOffset = false;
	private static int dumpOffset = 0;
	public static final int ALL = 95;
	public static boolean bTrace = false;
	public static boolean bDebug = false;
	public static boolean bTFile = false;
	private static RandomAccessFile fosTFile = null;
	public static boolean bFirstTime = true;
	public static long wrapSize = 0L;
	public static long currenttrace = 0L;
	private static Object tserver = null;
	private static boolean loadTServerAttempted = false;
	private static Method tserverSetJNITFile = null;
	private static Method tserverSetJNITrace = null;
	private static Method tserverGetJNITFile = null;
	private static Method tserverGetJNITrace = null;
	public static final int JNI_TRACE_ON = 1;
	public static final int JNI_TRACE_OFF = 0;
	private static final int NO_TIMESTAMPS = 0;
	private static final int TIME_AND_DATESTAMP = 1;
	private static final int TIMESTAMP_ONLY = 2;
	private static String tFileName = "";
	private static final String EXCEPTION_SECURITY = "Security";

	private T()
	{
	}

	public static void setOn(boolean flag)
	{
		bTraceOn = flag;
		bTrace = flag;
		checkTFileOpenClosed();
	}

	public static void setDebugOn(boolean flag)
	{
		bTraceOn = bTrace = bEntryOn = bExitOn = bStackOn = bLinesOn = bDebug = bNoDumpTruncation = bNoDumpOffset = flag;
		checkTFileOpenClosed();
	}

	public static void setMask(int i)
	{
		bEntryOn = (i & 1) == 1;
		bExitOn = (i & 2) == 2;
		bLinesOn = (i & 4) == 4;
		bStackOn = (i & 8) == 8;
		bTimingOn = (i & 0x10) == 16;
		bTraceOn = (i & 0x20) == 32;
		bNoDumpTruncation = bNoDumpOffset = (i & 0x40) == 64;
		bDebug = bEntryOn || bExitOn || bLinesOn;
		checkTFileOpenClosed();
	}

	public static void setEntryOn(boolean flag)
	{
		bEntryOn = flag;
		bDebug = bEntryOn || bExitOn || bLinesOn;
		checkTFileOpenClosed();
	}

	public static void setLinesOn(boolean flag)
	{
		bLinesOn = flag;
		bDebug = bEntryOn || bExitOn || bLinesOn;
		checkTFileOpenClosed();
	}

	public static void setExitOn(boolean flag)
	{
		bExitOn = flag;
		bDebug = bEntryOn || bExitOn || bLinesOn;
		checkTFileOpenClosed();
	}

	public static void setStackOn(boolean flag)
	{
		bStackOn = flag;
		checkTFileOpenClosed();
	}

	public static void setTimingOn(boolean flag)
	{
		bTimingOn = flag;
	}

	public static void setOutput(PrintStream printstream)
	{
		prsTrace = printstream;
	}

	public static void setInfoLogOutput(PrintStream printstream)
	{
		prsLog = printstream;
	}

	public static void setfullDataDumpOn(boolean flag)
	{
		bNoDumpTruncation = bNoDumpOffset = flag;
	}

	public static void setTruncationSize(int i)
		throws IllegalArgumentException
	{
		if (i >= 0)
		{
			hexDumpTruncation = i;
			bNoDumpTruncation = false;
		} else
		{
			throw new IllegalArgumentException();
		}
	}

	public static void setDumpOffset(int i)
		throws IllegalArgumentException
	{
		if (i >= 0)
		{
			dumpOffset = i;
			bNoDumpOffset = false;
		} else
		{
			throw new IllegalArgumentException();
		}
	}

	public static void setTFile(boolean flag, String s)
	{
		setTFile(flag, s, 0L);
	}

	public static void setTFile(boolean flag, String s, long l)
	{
		bTFile = flag;
		if (flag)
		{
			try
			{
				fosTFile = new RandomAccessFile(s, "rw");
				long l1 = fosTFile.length();
				fosTFile.seek(l1);
			}
			catch (Exception exception) { }
			tFileName = s;
			if (l != 0L)
				if (l < 4L)
					wrapSize = 4096L;
				else
				if (l > 0x200000L)
					wrapSize = 0x80000000L;
				else
					wrapSize = l * 1024L;
		} else
		{
			tFileName = "";
		}
	}

	public static boolean setJNITFile(int i, String s)
		throws IOException, IllegalArgumentException
	{
		if (1 != i && 0 != i)
			throw new IllegalArgumentException();
		if (tserver == null)
			instantiateTServer();
		if (tserver != null)
		{
			int ai[] = new int[1];
			String s1 = "";
			try
			{
				Object obj = tserverSetJNITFile.invoke(tserver, new Object[] {
					Thread.currentThread().getName(), new Integer(i), s, ai
				});
				s1 = (String)obj;
			}
			catch (IllegalAccessException illegalaccessexception)
			{
				return false;
			}
			catch (IllegalArgumentException illegalargumentexception)
			{
				return false;
			}
			catch (ClassCastException classcastexception)
			{
				return false;
			}
			catch (InvocationTargetException invocationtargetexception)
			{
				return false;
			}
			if (ai[0] != 0)
				throw new TFileException(s1, ai[0]);
		}
		return tserver != null;
	}

	public static boolean setJNITrace(int i)
		throws IllegalArgumentException
	{
		if (1 != i && 0 != i)
			throw new IllegalArgumentException();
		if (tserver == null)
			instantiateTServer();
		if (tserver != null)
			try
			{
				tserverSetJNITrace.invoke(tserver, new Object[] {
					new Integer(i)
				});
			}
			catch (IllegalAccessException illegalaccessexception)
			{
				return false;
			}
			catch (IllegalArgumentException illegalargumentexception)
			{
				return false;
			}
			catch (InvocationTargetException invocationtargetexception)
			{
				return false;
			}
		return tserver != null;
	}

	public static String getJNITFile()
	{
		if (tserver == null)
			instantiateTServer();
		if (tserver != null)
		{
			String s = "";
			try
			{
				Object obj = tserverGetJNITFile.invoke(tserver, new Object[0]);
				s = (String)obj;
			}
			catch (IllegalAccessException illegalaccessexception)
			{
				return "";
			}
			catch (IllegalArgumentException illegalargumentexception)
			{
				return "";
			}
			catch (ClassCastException classcastexception)
			{
				return "";
			}
			catch (InvocationTargetException invocationtargetexception)
			{
				return "";
			}
			return s;
		} else
		{
			return "";
		}
	}

	public static int getJNITrace()
	{
		if (tserver == null)
			instantiateTServer();
		if (tserver != null)
		{
			int i = 0;
			try
			{
				Object obj = tserverGetJNITrace.invoke(tserver, new Object[0]);
				i = ((Integer)obj).intValue();
			}
			catch (IllegalAccessException illegalaccessexception)
			{
				return 0;
			}
			catch (IllegalArgumentException illegalargumentexception)
			{
				return 0;
			}
			catch (ClassCastException classcastexception)
			{
				return 0;
			}
			catch (InvocationTargetException invocationtargetexception)
			{
				return 0;
			}
			return i;
		} else
		{
			return 0;
		}
	}

	private static synchronized void instantiateTServer()
	{
		if (loadTServerAttempted)
			return;
		loadTServerAttempted = true;
		if (tserver == null)
			try
			{
				Class class1 = Class.forName("com.ibm.ctg.server.TServer");
				tserver = class1.newInstance();
				Method amethod[] = class1.getMethods();
				for (int i = 0; i < amethod.length; i++)
				{
					String s = amethod[i].getName();
					if (s.equals("setJNITFile"))
						tserverSetJNITFile = amethod[i];
					else
					if (s.equals("setJNITrace"))
						tserverSetJNITrace = amethod[i];
					else
					if (s.equals("getJNITFile"))
						tserverGetJNITFile = amethod[i];
					else
					if (s.equals("getJNITrace"))
						tserverGetJNITrace = amethod[i];
				}

			}
			catch (InstantiationException instantiationexception)
			{
				tserver = null;
			}
			catch (IllegalAccessException illegalaccessexception)
			{
				tserver = null;
			}
			catch (ClassNotFoundException classnotfoundexception)
			{
				tserver = null;
			}
			catch (SecurityException securityexception)
			{
				tserver = null;
			}
	}

	public static boolean getLinesOn()
	{
		return bLinesOn;
	}

	public static boolean getDebugOn()
	{
		return bDebug;
	}

	public static boolean getTraceOn()
	{
		return bTraceOn;
	}

	public static boolean getEntryOn()
	{
		return bEntryOn;
	}

	public static boolean getExitOn()
	{
		return bExitOn;
	}

	public static boolean getTimingOn()
	{
		return bTimingOn;
	}

	public static boolean getStackOn()
	{
		return bStackOn;
	}

	static Object getPrsTrace()
	{
		return prsTrace;
	}

	public static int getTruncationSize()
	{
		return hexDumpTruncation;
	}

	public static int getDumpOffset()
	{
		return dumpOffset;
	}

	public static boolean getFullDataDumpOn()
	{
		return bNoDumpTruncation && bNoDumpOffset;
	}

	public static String getTFileName()
	{
		return tFileName;
	}

	public static long getTFileWrapSize()
	{
		return wrapSize / 1024L;
	}

	public static void setTFileWrapSize(long l)
	{
		wrapSize = l * 1024L;
	}

	static void setTFileOn(String s)
		throws IOException
	{
		if (s.equals(""))
		{
			bTFile = false;
			tFileName = "";
			if (fosTFile != null)
			{
				fosTFile.close();
				fosTFile = null;
			}
			return;
		}
		File file = new File(s);
		File file1 = file.getAbsoluteFile().getParentFile();
		try
		{
			if (file1 != null && !file1.exists())
				throw new TFileException(2);
			if (file.isDirectory())
				throw new TFileException(4);
			if (file.exists())
			{
				if (!file.canWrite())
					throw new TFileException(3);
			} else
			if (file1 != null && !file1.canWrite())
				throw new TFileException(3);
		}
		catch (SecurityException securityexception)
		{
			throw new IOException("Security");
		}
		RandomAccessFile randomaccessfile;
		try
		{
			randomaccessfile = new RandomAccessFile(s, "rw");
			long l = randomaccessfile.length();
			randomaccessfile.seek(l);
		}
		catch (FileNotFoundException filenotfoundexception)
		{
			throw new TFileException(1);
		}
		catch (SecurityException securityexception1)
		{
			throw new IOException("Security");
		}
		if (fosTFile != null)
			fosTFile.close();
		fosTFile = randomaccessfile;
		tFileName = s;
		if (bFirstTime)
		{
			try
			{
				writeInitialTraceInfo();
			}
			catch (IOException ioexception)
			{
				throw new TFileException(1);
			}
			bFirstTime = false;
		}
		bTFile = true;
	}

	private static void checkTFileOpenClosed()
	{
		if (!bLinesOn && !bTraceOn && !bEntryOn && !bExitOn && !bStackOn)
		{
			if (fosTFile != null)
			{
				try
				{
					fosTFile.close();
					fosTFile = null;
				}
				catch (IOException ioexception)
				{
					return;
				}
				bTFile = false;
			}
		} else
		if (!tFileName.equals("") && fosTFile == null)
		{
			try
			{
				fosTFile = new RandomAccessFile(tFileName, "rw");
				long l = fosTFile.length();
				fosTFile.seek(l);
			}
			catch (FileNotFoundException filenotfoundexception)
			{
				return;
			}
			catch (SecurityException securityexception)
			{
				return;
			}
			catch (IOException ioexception1)
			{
				return;
			}
			bTFile = true;
		}
	}

	private static void outputLine(boolean flag, Object obj, String s, boolean flag1, int i)
	{
		synchronized (prsTrace)
		{
			StringBuffer stringbuffer = new StringBuffer(128);
			if (i != 0 && bTimingOn && dfTime != null)
			{
				if (i == 2)
					dfTime.applyPattern("HH:mm:ss:SSS");
				else
					dfTime.applyPattern("MM/dd/yy HH:mm:ss:SSS");
				Date date = new Date();
				stringbuffer.append(dfTime.format(date));
				stringbuffer.append(" ");
			}
			if (!flag1)
			{
				stringbuffer.append(Thread.currentThread().getName());
				stringbuffer.append(": ");
				if (flag && obj != null)
				{
					String s1 = obj.getClass().toString().substring(6);
					int j = s1.lastIndexOf('.');
					if (j != -1)
						s1 = s1.substring(j);
					stringbuffer.append(s1);
					stringbuffer.append(':');
				} else
				{
					stringbuffer.append("S-C: ");
				}
			}
			stringbuffer.append(s);
			if (bTFile)
			{
				stringbuffer.append('\n');
				try
				{
					if (bFirstTime)
					{
						writeInitialTraceInfo();
						bFirstTime = false;
					} else
					if (wrapSize > 0L)
					{
						currenttrace = fosTFile.getFilePointer();
						if (currenttrace > wrapSize - (long)stringbuffer.toString().getBytes().length)
						{
							seekTraceFileStart();
							writeInitialTraceInfo();
						}
					}
					fosTFile.write(stringbuffer.toString().getBytes());
				}
				catch (Exception exception) { }
			} else
			{
				if (bFirstTime && !flag1)
				{
					prsTrace.println(dumpSystemInfo(false));
					bFirstTime = false;
				}
				prsTrace.println(stringbuffer.toString());
			}
		}
	}

	/**
	 * @deprecated Method printErrorLn is deprecated
	 */

	public static final void printErrorLn(String s)
	{
		outputLine(false, null, s, true, 1);
	}

	/**
	 * @deprecated Method printInfoLn is deprecated
	 */

	public static final void printInfoLn(String s)
	{
		outputLine(false, null, s, true, 1);
	}

	public static void traceln(String s)
	{
		if (bTraceOn || bDebug)
			outputLine(false, null, s, false, 2);
	}

	public static void in(Object obj, String s)
	{
		if (bEntryOn)
			in(obj, s, 0, null, null, null, null, null, null, null, null, null, null);
	}

	public static void in(Object obj, String s, Object obj1)
	{
		if (bEntryOn)
			in(obj, s, 1, obj1, null, null, null, null, null, null, null, null, null);
	}

	public static void in(Object obj, String s, Object obj1, Object obj2)
	{
		if (bEntryOn)
			in(obj, s, 2, obj1, obj2, null, null, null, null, null, null, null, null);
	}

	public static void in(Object obj, String s, Object obj1, Object obj2, Object obj3)
	{
		if (bEntryOn)
			in(obj, s, 3, obj1, obj2, obj3, null, null, null, null, null, null, null);
	}

	public static void in(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4)
	{
		if (bEntryOn)
			in(obj, s, 4, obj1, obj2, obj3, obj4, null, null, null, null, null, null);
	}

	public static void in(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5)
	{
		if (bEntryOn)
			in(obj, s, 5, obj1, obj2, obj3, obj4, obj5, null, null, null, null, null);
	}

	public static void in(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6)
	{
		if (bEntryOn)
			in(obj, s, 6, obj1, obj2, obj3, obj4, obj5, obj6, null, null, null, null);
	}

	public static void in(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6,
			Object obj7)
	{
		if (bEntryOn)
			in(obj, s, 7, obj1, obj2, obj3, obj4, obj5, obj6, obj7, null, null, null);
	}

	public static void in(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6,
			Object obj7, Object obj8)
	{
		if (bEntryOn)
			in(obj, s, 8, obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, null, null);
	}

	public static void in(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6,
			Object obj7, Object obj8, Object obj9)
	{
		if (bEntryOn)
			in(obj, s, 9, obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9, null);
	}

	public static void in(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6,
			Object obj7, Object obj8, Object obj9, Object obj10)
	{
		if (bEntryOn)
			in(obj, s, 10, obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9, obj10);
	}

	public static void in(Object obj, String s, int i, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5,
			Object obj6, Object obj7, Object obj8, Object obj9, Object obj10)
	{
		if (!bEntryOn)
			return;
		StringBuffer stringbuffer = new StringBuffer(80);
		stringbuffer.append("-> [");
		stringbuffer.append(s);
		stringbuffer.append("] (");
		if (i-- != 0)
		{
			Object aobj[] = {
				obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9, obj10
			};
			for (int j = 0; j <= i; j++)
			{
				if (aobj[j] == null)
					stringbuffer.append("null");
				else
					stringbuffer.append(aobj[j].toString());
				if (j < i)
					stringbuffer.append(", ");
			}

		}
		stringbuffer.append(")");
		outputLine(true, obj, stringbuffer.toString(), false, 2);
	}

	public static void out(Object obj, String s)
	{
		if (bExitOn)
			outputLine(true, obj, "<- [" + s + "]", false, 2);
	}

	public static void out(Object obj, String s, boolean flag)
	{
		if (bExitOn)
			outputLine(true, obj, "<- [" + s + "] = " + flag, false, 2);
	}

	public static void out(Object obj, String s, int i)
	{
		if (bExitOn)
			outputLine(true, obj, "<- [" + s + "] = " + i, false, 2);
	}

	public static void out(Object obj, String s, Object obj1)
	{
		if (bExitOn)
			outputLine(true, obj, "<- [" + s + "] = " + obj1, false, 2);
	}

	public static void ln(Object obj, String s)
	{
		if (bLinesOn)
			ln(obj, s, 0, null, null, null, null, null, null, null, null, null, null);
	}

	public static void ln(Object obj, String s, Object obj1)
	{
		if (bLinesOn)
			ln(obj, s, 1, obj1, null, null, null, null, null, null, null, null, null);
	}

	public static void ln(Object obj, String s, Object obj1, Object obj2)
	{
		if (bLinesOn)
			ln(obj, s, 2, obj1, obj2, null, null, null, null, null, null, null, null);
	}

	public static void ln(Object obj, String s, Object obj1, Object obj2, Object obj3)
	{
		if (bLinesOn)
			ln(obj, s, 3, obj1, obj2, obj3, null, null, null, null, null, null, null);
	}

	public static void ln(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4)
	{
		if (bLinesOn)
			ln(obj, s, 4, obj1, obj2, obj3, obj4, null, null, null, null, null, null);
	}

	public static void ln(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5)
	{
		if (bLinesOn)
			ln(obj, s, 5, obj1, obj2, obj3, obj4, obj5, null, null, null, null, null);
	}

	public static void ln(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6)
	{
		if (bLinesOn)
			ln(obj, s, 6, obj1, obj2, obj3, obj4, obj5, obj6, null, null, null, null);
	}

	public static void ln(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6,
			Object obj7)
	{
		if (bLinesOn)
			ln(obj, s, 7, obj1, obj2, obj3, obj4, obj5, obj6, obj7, null, null, null);
	}

	public static void ln(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6,
			Object obj7, Object obj8)
	{
		if (bLinesOn)
			ln(obj, s, 8, obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, null, null);
	}

	public static void ln(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6,
			Object obj7, Object obj8, Object obj9)
	{
		if (bLinesOn)
			ln(obj, s, 9, obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9, null);
	}

	public static void ln(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6,
			Object obj7, Object obj8, Object obj9, Object obj10)
	{
		if (bLinesOn)
			ln(obj, s, 10, obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9, obj10);
	}

	public static void ln(Object obj, String s, int i, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5,
			Object obj6, Object obj7, Object obj8, Object obj9, Object obj10)
	{
		if (!bLinesOn)
			return;
		StringBuffer stringbuffer = new StringBuffer(80);
		stringbuffer.append(" + ");
		if (i != 0)
		{
			Object aobj[] = {
				obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9, obj10
			};
			do
			{
				if (i <= 0)
					break;
				if (aobj[--i] == null)
					aobj[i] = "";
				else
				if (!(aobj[i] instanceof Number) && !(aobj[i] instanceof Date) && !(aobj[i] instanceof String))
					aobj[i] = aobj[i].toString();
			} while (true);
			try
			{
				String s1 = MessageFormat.format(s, aobj);
				stringbuffer.append(s1);
			}
			catch (Throwable throwable)
			{
				ex(obj, throwable);
				stringbuffer.append("!Fmt! : ");
				stringbuffer.append(s);
			}
		} else
		{
			stringbuffer.append(s);
		}
		outputLine(true, obj, stringbuffer.toString(), false, 2);
	}

	public static void ex(Object obj, Throwable throwable)
	{
		if (bStackOn)
			try
			{
				CharArrayWriter chararraywriter = new CharArrayWriter();
				PrintWriter printwriter = new PrintWriter(chararraywriter);
				throwable.printStackTrace(printwriter);
				BufferedReader bufferedreader = new BufferedReader(new CharArrayReader(chararraywriter.toCharArray()));
				String s;
				while ((s = bufferedreader.readLine()) != null) 
					outputLine(true, obj, s, false, 2);
			}
			catch (NoClassDefFoundError noclassdeffounderror)
			{
				try
				{
					ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
					PrintStream printstream = new PrintStream(bytearrayoutputstream);
					throwable.printStackTrace(printstream);
					BufferedReader bufferedreader1 = new BufferedReader(new InputStreamReader(new DataInputStream(new ByteArrayInputStream(bytearrayoutputstream.toByteArray()))));
					String s1;
					while ((s1 = bufferedreader1.readLine()) != null) 
						outputLine(true, obj, " ! " + s1, false, 2);
				}
				catch (IOException ioexception1)
				{
					outputLine(true, obj, "! Exception caught during stack trace of " + throwable, false, 2);
				}
			}
			catch (IOException ioexception)
			{
				outputLine(true, obj, "! Exception caught during stack trace of " + throwable, false, 2);
			}
	}

	public static void hexDump(Object obj, byte abyte0[], String s)
	{
		hexDump(obj, abyte0, s, -1, -1);
	}

	public static void hexDump(Object obj, byte abyte0[], String s, int i, int j)
	{
		if (!bLinesOn && !bTraceOn)
			return;
		boolean flag = false;
		if (i >= 0 && j > 0 && j > i)
			flag = true;
		String s1 = "";
		if (bTraceOn)
			s1 = ClientMessages.getMessage(null, 3);
		if (abyte0 == null)
		{
			outputLine(true, obj, s1 + " # Dump: 0 bytes : " + s, false, 2);
			return;
		}
		synchronized (prsTrace)
		{
			byte byte0 = 16;
			int j1 = 0;
			char ac[] = new char[2];
			Object obj1 = null;
			Object obj2 = null;
			StringBuffer stringbuffer2 = null;
			int i1;
			if (!bNoDumpTruncation)
				i1 = Math.min(abyte0.length, hexDumpTruncation);
			else
				i1 = abyte0.length;
			if (!bNoDumpOffset && dumpOffset < i1)
				j1 = dumpOffset;
			stringbuffer2 = new StringBuffer(s1);
			stringbuffer2.append(" # Dump: ");
			stringbuffer2.append(i1);
			stringbuffer2.append("/");
			stringbuffer2.append(abyte0.length);
			stringbuffer2.append(" bytes : Offset = ");
			stringbuffer2.append(j1);
			stringbuffer2.append(' ');
			stringbuffer2.append(s);
			outputLine(bLinesOn, obj, stringbuffer2.toString(), false, 2);
			for (int k1 = j1; k1 < i1; k1 += byte0)
			{
				StringBuffer stringbuffer1 = new StringBuffer("00000");
				stringbuffer1.append(k1);
				stringbuffer1.reverse();
				stringbuffer1.setLength(5);
				stringbuffer1.reverse();
				StringBuffer stringbuffer = new StringBuffer(90);
				stringbuffer.append(s1);
				stringbuffer.append(" # ");
				stringbuffer.append(stringbuffer1.toString());
				stringbuffer.append(": ");
				stringbuffer1 = new StringBuffer(18);
				stringbuffer1.append(' ');
				for (int l1 = k1; l1 < k1 + byte0; l1++)
					if (l1 < i1)
					{
						byte byte1;
						if (flag && l1 >= i && l1 <= j)
							byte1 = 42;
						else
							byte1 = abyte0[l1];
						int k = (byte1 & 0xf0) >> 4;
						int l = byte1 & 0xf;
						ac[0] = k >= 10 ? (char)(65 + (char)(k - 10)) : (char)(48 + (char)k);
						ac[1] = l >= 10 ? (char)(65 + (char)(l - 10)) : (char)(48 + (char)l);
						stringbuffer.append(ac);
						stringbuffer.append(' ');
						stringbuffer1.append(byte1 >= 32 ? (char)byte1 : '.');
					} else
					{
						stringbuffer.append("   ");
					}

				stringbuffer.append(' ');
				stringbuffer.append(stringbuffer1.toString());
				outputLine(bLinesOn, obj, stringbuffer.toString(), false, 0);
			}

		}
	}

	private static void seekTraceFileStart()
		throws IOException
	{
		fosTFile.seek(0L);
		currenttrace = 0L;
	}

	public static void writeInitialTraceInfo()
		throws IOException
	{
		Date date = new Date();
		dfTime.applyPattern("dd MMMMM yyyyy HH:mm:ss:SSS");
		String s;
		if (wrapSize > 0L)
			s = "CICS Transaction Gateway tracefile started: " + dfTime.format(date) + " ftilesize=" + wrapSize + "\n";
		else
			s = "CICS Transaction Gateway tracefile started: " + dfTime.format(date) + "\n";
		fosTFile.write(s.getBytes());
		s = dumpSystemInfo(false);
		fosTFile.write(s.getBytes());
	}

	public static String dumpSystemInfo(boolean flag)
	{
		StringBuffer stringbuffer = new StringBuffer();
		stringbuffer.append("Class version ");
		stringbuffer.append("@(#) java/com/ibm/ctg/client/T.java, client_java, c602, c602-20060418 1.25.2.37 04/10/11 15:14:27");
		stringbuffer.append('\n');
		if (!flag)
		{
			try
			{
				stringbuffer.append("System properties :\n");
				stringbuffer.append("java.version = ");
				stringbuffer.append(System.getProperty("java.version"));
				stringbuffer.append('\n');
				stringbuffer.append("java.vendor = ");
				stringbuffer.append(System.getProperty("java.vendor"));
				stringbuffer.append('\n');
				stringbuffer.append("java.class.version = ");
				stringbuffer.append(System.getProperty("java.class.version"));
				stringbuffer.append('\n');
				stringbuffer.append("OS details :- ");
				stringbuffer.append(System.getProperty("os.name"));
				stringbuffer.append(' ');
				stringbuffer.append(System.getProperty("os.version"));
				stringbuffer.append(' ');
				stringbuffer.append(System.getProperty("os.arch"));
				stringbuffer.append('\n');
				String s = System.getProperty("java.class.path");
				stringbuffer.append("java.class.path = ");
				stringbuffer.append(s);
				stringbuffer.append('\n');
				s = null;
				stringbuffer.append("user.name = ");
				stringbuffer.append(System.getProperty("user.name"));
				stringbuffer.append('\n');
				stringbuffer.append("Current directory (user.dir) = ");
				stringbuffer.append(System.getProperty("user.dir"));
				stringbuffer.append('\n');
				stringbuffer.append("user.language = ");
				stringbuffer.append(System.getProperty("user.language"));
				stringbuffer.append(" , ");
				stringbuffer.append("user.timezone = ");
				stringbuffer.append(System.getProperty("user.timezone"));
				stringbuffer.append('\n');
				stringbuffer.append("file.encoding = ");
				stringbuffer.append(System.getProperty("file.encoding"));
				stringbuffer.append('\n');
			}
			catch (SecurityException securityexception)
			{
				stringbuffer.append("Untrusted applet\n");
				stringbuffer.append('\n');
			}
			if (invalidDumpOffset)
			{
				stringbuffer.append(ClientTraceMessages.getMessage(6, "gateway.T.setDumpOffset"));
				stringbuffer.append('\n');
			}
			if (invalidTruncationSize)
			{
				stringbuffer.append(ClientTraceMessages.getMessage(6, "gateway.T.setTruncationSize"));
				stringbuffer.append('\n');
			}
		}
		return stringbuffer.toString();
	}

	public static void writeLogToTrace(String s)
	{
		if (bTrace || bDebug)
			outputLine(false, null, s, true, 2);
	}

	static 
	{
		dfTime = null;
		Locale locale = Locale.getDefault();
		Object obj = TimeZone.getDefault();
		if (locale.getCountry() == "GB" || locale.getCountry() == "IE")
			obj = new SimpleTimeZone(0, "Europe/London", 2, -1, 1, 0x36ee80, 9, -1, 1, 0x6ddd00);
		prsTrace = System.err;
		prsLog = System.out;
		try
		{
			dfTime = (SimpleDateFormat) DateFormat.getTimeInstance();
			dfTime.setTimeZone(((TimeZone) (obj)));
		}
		catch (NoClassDefFoundError noclassdeffounderror)
		{
			dfTime = null;
		}
		try
		{
			if (System.getProperty("gateway.T", "off").toLowerCase().equals("on"))
				setDebugOn(true);
			if (System.getProperty("gateway.T.entry", "off").toLowerCase().equals("on"))
				setEntryOn(true);
			if (System.getProperty("gateway.T.lines", "off").toLowerCase().equals("on"))
				setLinesOn(true);
			if (System.getProperty("gateway.T.exit", "off").toLowerCase().equals("on"))
				setExitOn(true);
			if (System.getProperty("gateway.T.stack", "off").toLowerCase().equals("on"))
				setStackOn(true);
			if (System.getProperty("gateway.T.trace", "off").toLowerCase().equals("on"))
				setOn(true);
			if (System.getProperty("gateway.T.timing", "off").toLowerCase().equals("on"))
				setTimingOn(true);
			if (System.getProperty("gateway.T.fullDataDump", "off").toLowerCase().equals("on"))
				setfullDataDumpOn(true);
			String s = System.getProperty("gateway.T.setDumpOffset");
			try
			{
				if (s != null)
					setDumpOffset(Integer.parseInt(s));
			}
			catch (NumberFormatException numberformatexception)
			{
				invalidDumpOffset = true;
			}
			catch (IllegalArgumentException illegalargumentexception)
			{
				invalidDumpOffset = true;
			}
			try
			{
				s = System.getProperty("gateway.T.setTruncationSize");
				if (s != null)
					setTruncationSize(Integer.parseInt(s));
			}
			catch (NumberFormatException numberformatexception1)
			{
				invalidTruncationSize = true;
			}
			catch (IllegalArgumentException illegalargumentexception1)
			{
				invalidTruncationSize = true;
			}
			if ((s = System.getProperty("gateway.T.setTFile")) != null)
				setTFile(true, s);
			if ((s = System.getProperty("gateway.T.setJNITFile")) != null)
				setJNITFile(1, s);
			s = null;
		}
		catch (SecurityException securityexception) { }
		catch (IOException ioexception) { }
	}
}

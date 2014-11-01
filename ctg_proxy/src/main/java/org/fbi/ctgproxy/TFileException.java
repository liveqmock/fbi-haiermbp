package org.fbi.ctgproxy;

import java.io.IOException;
public class TFileException extends IOException
	implements TFileReturnCodes
{
	private int rc;

	public TFileException(int i)
	{
		rc = i;
	}

	public TFileException(String s, int i)
	{
		super(s);
		rc = i;
	}

	public int getRc()
	{
		return rc;
	}

	public String toString()
	{
		return super.toString() + ". rc=" + rc;
	}
}

package org.fbi.ctgproxy;

import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceWrapper
{
	ResourceBundle bundle;
	Hashtable msgs;

	public ResourceWrapper(ResourceBundle resourcebundle)
	{
		bundle = null;
		msgs = null;
		if (resourcebundle == null)
		{
			Object aobj[][] = getContents();
			msgs = new Hashtable(aobj.length);
			for (int i = 0; i < aobj.length; i++)
				msgs.put(aobj[i][0], aobj[i][1]);

		} else
		{
			bundle = resourcebundle;
		}
	}

	protected Object[][] getContents()
	{
		return (Object[][])null;
	}

	public final String getString(String s)
	{
		if (bundle != null)
		{
			return bundle.getString(s);
		} else
		{
			Object obj = msgs.get(s);
			return (String)obj;
		}
	}

	public final Object getObject(String s)
	{
		if (bundle != null)
			return bundle.getObject(s);
		else
			return msgs.get(s);
	}

	public static final ResourceWrapper getBundle(String s)
	{
		ResourceBundle resourcebundle = ResourceBundle.getBundle(s);
		return new ResourceWrapper(resourcebundle);
	}

	public static final ResourceWrapper getBundle(String s, Locale locale)
	{
		ResourceBundle resourcebundle = ResourceBundle.getBundle(s, locale);
		return new ResourceWrapper(resourcebundle);
	}
}

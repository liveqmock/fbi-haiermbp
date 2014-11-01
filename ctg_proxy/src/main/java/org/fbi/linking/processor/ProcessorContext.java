package org.fbi.linking.processor;

import java.util.Enumeration;

/**
 * User: zhanrui
 * Date: 13-8-3
 * Time: ÉÏÎç11:20
 */
public interface ProcessorContext {
    public  ProcessorContext getContext(String uripath);
    public String getContextPath();

    public Processor getProcessor(String ProcessorName) throws ProcessorException;

    public String getInitParameter(String name);
    public Enumeration<String> getInitParameterNames();
    public boolean setInitParameter(String name, String value);

    public Object getAttribute(String name);
    public Enumeration<String> getAttributeNames();
    public void setAttribute(String name, Object object);
    public void removeAttribute(String name);
}

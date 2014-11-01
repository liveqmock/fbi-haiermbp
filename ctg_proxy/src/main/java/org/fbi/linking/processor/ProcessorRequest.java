package org.fbi.linking.processor;

import java.util.Enumeration;
import java.util.Map;

public interface ProcessorRequest  {
    public String getParameter(String name);
    public Enumeration<String> getParameterNames();
    public String[] getParameterValues(String name);
    public Map<String, String[]> getParameterMap();

    public Object getAttribute(String name);
    public Enumeration<String> getAttributeNames();
    public void setAttribute(String name, Object o);
    public void removeAttribute(String name);

    public String getCharacterEncoding();
    public void setCharacterEncoding(String env) throws java.io.UnsupportedEncodingException;
    public ProcessorContext getProcessorContext();
}

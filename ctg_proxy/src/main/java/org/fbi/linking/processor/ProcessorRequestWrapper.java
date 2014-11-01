package org.fbi.linking.processor;

import java.util.Enumeration;
import java.util.Map;

/**
 * User: zhanrui
 * Date: 13-11-20
 */
public class ProcessorRequestWrapper implements ProcessorRequest{
    private ProcessorRequest request;

    public ProcessorRequestWrapper(ProcessorRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        this.request = request;
    }

    public ProcessorRequest getRequest() {
        return this.request;
    }

    public void setRequest(ProcessorRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        this.request = request;
    }

    @Override
    public Object getAttribute(String name) {
        return this.request.getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return this.request.getAttributeNames();
    }

    @Override
    public String getCharacterEncoding() {
        return this.request.getCharacterEncoding();
    }

    @Override
    public void setCharacterEncoding(String enc)
            throws java.io.UnsupportedEncodingException {
        this.request.setCharacterEncoding(enc);
    }

    @Override
    public String getParameter(String name) {
        return this.request.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return this.request.getParameterMap();
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return this.request.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        return this.request.getParameterValues(name);
    }

    @Override
    public void setAttribute(String name, Object o) {
        this.request.setAttribute(name, o);
    }

    @Override
    public void removeAttribute(String name) {
        this.request.removeAttribute(name);
    }

    @Override
    public ProcessorContext getProcessorContext() {
        return request.getProcessorContext();
    }
}

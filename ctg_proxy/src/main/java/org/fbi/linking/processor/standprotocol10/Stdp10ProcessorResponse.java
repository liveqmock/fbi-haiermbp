package org.fbi.linking.processor.standprotocol10;

import org.fbi.linking.processor.ProcessorResponse;

import java.util.Collection;

/**
 * User: zhanrui
 * Date: 13-11-20
 */
public interface Stdp10ProcessorResponse extends ProcessorResponse{
    public void setHeader(String name, String value);
    public void addHeader(String name, String value);
    public void setIntHeader(String name, int value);
    public void addIntHeader(String name, int value);
    public String getHeader(String name);
    public Collection<String> getHeaders(String name);
    public Collection<String> getHeaderNames();

    public void setResponseBody(byte[] body);
    public byte[] getResponseBody();
}

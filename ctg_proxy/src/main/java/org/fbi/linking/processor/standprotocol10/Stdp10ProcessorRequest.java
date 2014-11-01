package org.fbi.linking.processor.standprotocol10;

import org.fbi.linking.processor.ProcessorContext;
import org.fbi.linking.processor.ProcessorRequest;

import java.util.Enumeration;

/**
 * User: zhanrui
 * Date: 13-11-20
 * Time: 上午11:03
 */
public interface Stdp10ProcessorRequest extends ProcessorRequest{
    public String getHeader(String name);
    public Enumeration<String> getHeaders(String name);
    public Enumeration<String> getHeaderNames();
    public int getIntHeader(String name);

    public byte[] getRequestBody();
    public void setProcessorContext(ProcessorContext context);
}

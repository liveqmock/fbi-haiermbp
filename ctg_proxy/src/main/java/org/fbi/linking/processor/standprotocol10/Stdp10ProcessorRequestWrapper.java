package org.fbi.linking.processor.standprotocol10;

import org.fbi.linking.processor.ProcessorContext;
import org.fbi.linking.processor.ProcessorRequestWrapper;

import java.util.Enumeration;

/**
 * User: zhanrui
 * Date: 13-11-26
 */
public class Stdp10ProcessorRequestWrapper extends ProcessorRequestWrapper implements Stdp10ProcessorRequest {
    public Stdp10ProcessorRequestWrapper(Stdp10ProcessorRequest request) {
        super(request);
    }

    private Stdp10ProcessorRequest _getStdp10ProcessorRequest() {
        return (Stdp10ProcessorRequest) super.getRequest();
    }

    @Override
    public String getHeader(String name) {
        return this._getStdp10ProcessorRequest().getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return this._getStdp10ProcessorRequest().getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return this._getStdp10ProcessorRequest().getHeaderNames();
    }

    @Override
    public int getIntHeader(String name) {
        return this._getStdp10ProcessorRequest().getIntHeader(name);
    }


    @Override
    public byte[] getRequestBody() {
        return  this._getStdp10ProcessorRequest().getRequestBody();
    }

    @Override
    public void setProcessorContext(ProcessorContext context) {
        this._getStdp10ProcessorRequest().setProcessorContext(context);
    }
}

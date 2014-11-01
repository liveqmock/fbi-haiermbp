package org.fbi.linking.processor.standprotocol10;

import org.fbi.linking.processor.ProcessorResponseWrapper;

import java.util.Collection;

/**
 * User: zhanrui
 * Date: 13-11-20
 */
public class Stdp10ProcessorResponseWrapper extends ProcessorResponseWrapper implements Stdp10ProcessorResponse{
    public Stdp10ProcessorResponseWrapper(Stdp10ProcessorResponse response) {
        super(response);
    }

    private Stdp10ProcessorResponse _getStdp10ProcessorResponse() {
        return (Stdp10ProcessorResponse) super.getResponse();
    }

    @Override
    public void setHeader(String name, String value) {
        this._getStdp10ProcessorResponse().setHeader(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        this._getStdp10ProcessorResponse().addHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        this._getStdp10ProcessorResponse().setIntHeader(name, value);
    }

    @Override
    public void addIntHeader(String name, int value) {
        this._getStdp10ProcessorResponse().addIntHeader(name, value);
    }
    @Override
    public String getHeader(String name) {
        return this._getStdp10ProcessorResponse().getHeader(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return this._getStdp10ProcessorResponse().getHeaders(name);
    }

    @Override
    public Collection<String> getHeaderNames() {
        return this._getStdp10ProcessorResponse().getHeaderNames();
    }

    @Override
    public void setResponseBody(byte[] body) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public byte[] getResponseBody() {
        return this._getStdp10ProcessorResponse().getResponseBody();
    }
}

package org.fbi.mbp.proxy;

import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by zhanrui on 2014/10/11.
 */
public class TxnContext {
    private byte[] requestBuffer = null;
    private byte[] responseBuffer = null;
    private OutputStream os = null;
    private  Properties ccbRouterConfig = null;

    public byte[] getRequestBuffer() {
        return requestBuffer;
    }

    public void setRequestBuffer(byte[] requestBuffer) {
        this.requestBuffer = requestBuffer;
    }

    public byte[] getResponseBuffer() {
        return responseBuffer;
    }

    public void setResponseBuffer(byte[] responseBuffer) {
        this.responseBuffer = responseBuffer;
    }

    public OutputStream getOs() {
        return os;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }

    public Properties getCcbRouterConfig() {
        return ccbRouterConfig;
    }

    public void setCcbRouterConfig(Properties ccbRouterConfig) {
        this.ccbRouterConfig = ccbRouterConfig;
    }
}

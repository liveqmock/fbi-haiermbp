package org.fbi.mbp.proxy;

import java.io.OutputStream;
import java.util.HashMap;

/**
 * Created by zhanrui on 2014/10/11.
 */
public class TxnContext {
    private String projectRootDir;
    private byte[] requestBuffer = null;
    private byte[] responseBuffer = null;
    private OutputStream clientReponseOutputStream = null;
    private HashMap<String,String> ccbRouterConfig = new HashMap<>();

    public String getProjectRootDir() {
        return projectRootDir;
    }

    public void setProjectRootDir(String projectRootDir) {
        this.projectRootDir = projectRootDir;
    }

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

    public OutputStream getClientReponseOutputStream() {
        return clientReponseOutputStream;
    }

    public void setClientReponseOutputStream(OutputStream clientReponseOutputStream) {
        this.clientReponseOutputStream = clientReponseOutputStream;
    }

    public String getCcbRouterConfigByKey(String key) {
        return ccbRouterConfig.get(key);
    }

    public void setCcbRouterConfig(HashMap<String, String> ccbRouterConfig) {
        this.ccbRouterConfig = ccbRouterConfig;
    }
}

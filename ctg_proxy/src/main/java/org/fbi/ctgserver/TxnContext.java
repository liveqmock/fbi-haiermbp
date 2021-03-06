package org.fbi.ctgserver;

import org.fbi.ctgproxy.CtgSif;

import java.io.DataOutputStream;
import java.util.HashMap;

/**
 * Created by zhanrui on 2014/10/11.
 */
public class TxnContext {
    private String projectRootDir;
    private DataOutputStream clientOutputStream = null;
    private HashMap<String,String> routerConfig = new HashMap<>();

    private CtgSif ctgSif;

    public String getProjectRootDir() {
        return projectRootDir;
    }

    public void setProjectRootDir(String projectRootDir) {
        this.projectRootDir = projectRootDir;
    }

    public DataOutputStream getClientOutputStream() {
        return clientOutputStream;
    }

    public void setClientOutputStream(DataOutputStream clientOutputStream) {
        this.clientOutputStream = clientOutputStream;
    }

    public HashMap<String, String> getRouterConfig() {
        return routerConfig;
    }

    public void setRouterConfig(HashMap<String, String> routerConfig) {
        this.routerConfig = routerConfig;
    }

    public CtgSif getCtgSif() {
        return ctgSif;
    }

    public void setCtgSif(CtgSif ctgSif) {
        this.ctgSif = ctgSif;
    }
}

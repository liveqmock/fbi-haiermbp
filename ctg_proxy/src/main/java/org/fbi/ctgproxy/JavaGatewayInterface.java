package org.fbi.ctgproxy;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Locale;
import java.util.Properties;

public abstract class JavaGatewayInterface {
    protected Object objOpenMonitor;
    boolean bOpen;
    protected String strAddress;
    protected int iPort;
    protected InetAddress ipGateway;
    protected String strClientSecurityClass;
    protected String strServerSecurityClass;
    protected ClientSecurity cliSecurity;
    protected Properties protocolProperties;
    boolean bInitialFlow;
    Locale locServer;
    String strGatewayOs;

    public JavaGatewayInterface() {
        objOpenMonitor = new Object();
        bOpen = false;
        strAddress = null;
        iPort = 0;
        ipGateway = null;
        strClientSecurityClass = null;
        strServerSecurityClass = null;
        cliSecurity = null;
        bInitialFlow = true;
        locServer = null;
        strGatewayOs = null;
    }

    abstract void open() throws IOException;

    abstract int flow(CtgRequest CtgRequest) throws IOException;

    abstract void close() throws IOException;

    JavaGatewayInterface realInstance(String s, int i, String s1, String s2, boolean flag) throws IOException {
        strAddress = s;
        iPort = i;
        strClientSecurityClass = s1;
        strServerSecurityClass = s2;
        bInitialFlow = flag;
        return this;
    }

    protected void initialFlow() throws IOException {
        if (!bInitialFlow) {
            //logger.error("Initial flows have been disabled");
            return;
        }
        int i = 0;
        CtgRequest CtgRequest = new CtgRequest();
        try {
            if (strClientSecurityClass != null && !strClientSecurityClass.equals(""))
                cliSecurity = (ClientSecurity) Class.forName(strClientSecurityClass).newInstance();
            CtgRequest.setiFlowType(5);
            try {
                CtgRequest.locExchange = Locale.getDefault();
            } catch (NoClassDefFoundError noclassdeffounderror) {
                CtgRequest.locExchange = null;
            }
            if (cliSecurity != null) {
                CtgRequest.abytHandshake = cliSecurity.generateHandshake(ipGateway);
            }
            if (strServerSecurityClass != null)
                CtgRequest.strServerSecurityClass = strServerSecurityClass;
            else
                CtgRequest.strServerSecurityClass = "";
            i = flow(CtgRequest);
        } catch (Exception exception) {
            IOException ioexception1 = new IOException("");
            throw ioexception1;
        }
        if (i != 0) {
            IOException ioexception = new IOException("");
            throw ioexception;
        }
        if (cliSecurity != null) {
            cliSecurity.repliedHandshake(CtgRequest.abytHandshake);
        }
        locServer = CtgRequest.locExchange;
        strGatewayOs = CtgRequest.strServerJVM;
    }

    protected void setProtocolProperties(Properties properties) {
        protocolProperties = properties;
    }
}

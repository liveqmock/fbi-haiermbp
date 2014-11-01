package org.fbi.ctgproxy;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class JavaGateway {
    private static final String PROTOCOL_TCP = "tcp";
    private boolean bShouldBeOpen;
    private String strProtocol;
    private String strAddress;
    private int iPort;
    private Properties protocolProperties;
    private String strClientSecurity;
    private String strServerSecurity;
    private boolean bInitialFlow;
    private JavaGatewayInterface jgaReal;
    public static final String SSL_PROP_KEYRING_CLASS = "SslKeyRingClass";
    public static final String SSL_PROP_KEYRING_PW = "SslKeyRingPassword";

    public JavaGateway() {
        bShouldBeOpen = false;
        strProtocol = "tcp";
        strAddress = null;
        iPort = 0;
        strClientSecurity = null;
        strServerSecurity = null;
        bInitialFlow = true;
        jgaReal = null;
    }

    public JavaGateway(String s, int i) throws IOException {
        bShouldBeOpen = false;
        strProtocol = "tcp";
        strAddress = null;
        iPort = 0;
        strClientSecurity = null;
        strServerSecurity = null;
        bInitialFlow = true;
        jgaReal = null;
        setURL(s);
        iPort = i;
        open();
    }

    public JavaGateway(String s, int i, Properties properties) throws IOException {
        bShouldBeOpen = false;
        strProtocol = "tcp";
        strAddress = null;
        iPort = 0;
        strClientSecurity = null;
        strServerSecurity = null;
        bInitialFlow = true;
        jgaReal = null;
        setURL(s);
        iPort = i;
        setProtocolProperties(properties);
        open();
    }

    public JavaGateway(String s, int i, String s1, String s2, Properties properties) throws IOException {
        bShouldBeOpen = false;
        strProtocol = "tcp";
        strAddress = null;
        iPort = 0;
        strClientSecurity = null;
        strServerSecurity = null;
        bInitialFlow = true;
        jgaReal = null;
        setURL(s);
        iPort = i;
        strClientSecurity = s1;
        strServerSecurity = s2;
        setProtocolProperties(properties);
        open();
    }

    public JavaGateway(String s, int i, String s1, String s2) throws IOException {
        bShouldBeOpen = false;
        strProtocol = "tcp";
        strAddress = null;
        iPort = 0;
        strClientSecurity = null;
        strServerSecurity = null;
        bInitialFlow = true;
        jgaReal = null;
        setURL(s);
        iPort = i;
        strClientSecurity = s1;
        strServerSecurity = s2;
        open();
    }

    public synchronized void open() throws IOException {
        if (bShouldBeOpen && jgaReal.bOpen) {
            IOException ioexception = new IOException("63");
            throw ioexception;
        }
        if (strProtocol == null) {
            IOException ioexception1 = new IOException("64");
            throw ioexception1;
        }
        boolean flag = strClientSecurity != null && strClientSecurity.length() != 0;
        boolean flag1 = strServerSecurity != null && strServerSecurity.length() != 0;
        if (flag && !flag1 || flag1 && !flag) {
            IOException ioexception2 = new IOException("70");
            throw ioexception2;
        }
        if (flag && flag1)
            bInitialFlow = true;
        StringBuilder sb = new StringBuilder(80);
        sb.append(strProtocol);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        //TODO
        sb.insert(0, "org.fbi.ctgproxy.");
        sb.append("JavaGateway");
        try {
            JavaGatewayInterface jgi = (JavaGatewayInterface) Class.forName(sb.toString()).newInstance();
            jgaReal = jgi.realInstance(strAddress, iPort, strClientSecurity, strServerSecurity, bInitialFlow);
            jgaReal.setProtocolProperties(protocolProperties);
            jgaReal.open();
            bShouldBeOpen = true;
        } catch (IOException ioexception3) {
            throw ioexception3;
        } catch (Exception exception) {
            IOException ioexception4 = new IOException("65");
            throw ioexception4;
        }
    }

    public int flow(CtgRequest CtgRequest) throws IOException {
        if (CtgRequest != null) {
            if (jgaReal != null) {
                return jgaReal.flow(CtgRequest);
            } else {
                IOException ioexception = new IOException("72");
                throw ioexception;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public synchronized void close() throws IOException {
        if (jgaReal != null) {
            bShouldBeOpen = false;
            jgaReal.close();
        } else {
            IOException ioexception = new IOException("72");
            throw ioexception;
        }
    }

    public synchronized void setAddress(String s)
            throws IOException {
        if (bShouldBeOpen && jgaReal.bOpen) {
            IOException ioexception = new IOException("62");
            throw ioexception;
        } else {
            strAddress = s;
            return;
        }
    }

    public synchronized String getAddress() {
        return strAddress;
    }

    public synchronized void setPort(int i) throws IOException {
        if (bShouldBeOpen && jgaReal.bOpen) {
            IOException ioexception = new IOException("62");
            throw ioexception;
        } else {
            iPort = i;
            return;
        }
    }

    public synchronized int getPort() {
        return iPort;
    }

    public synchronized void setProtocol(String s) throws IOException {
        if (bShouldBeOpen && jgaReal.bOpen) {
            IOException ioexception = new IOException("62");
            throw ioexception;
        }
        int i = s.indexOf(':');
        if (i != -1)
            strProtocol = s.substring(0, i);
        else
            strProtocol = s;
    }

    public synchronized String getProtocol() {
        return strProtocol;
    }

    public synchronized void setURL(String s)
            throws IOException {
        if (bShouldBeOpen && jgaReal.bOpen) {
            IOException ioexception = new IOException("62");
            throw ioexception;
        }
        if (s == null) {
            IOException ioexception1 = new IOException("58");
            throw ioexception1;
        }
        int i = 0;
        int j;
        for (j = s.length(); j > 0 && s.charAt(j - 1) <= ' '; j--) ;
        for (; i < j && s.charAt(i) <= ' '; i++) ;
        if (i == j) {
            IOException ioexception2 = new IOException("58");
            throw ioexception2;
        }
        strProtocol = null;
        int k = s.indexOf(':', i);
        if (k != -1) {
            strProtocol = s.substring(i, k);
            if (strProtocol.indexOf('/') != -1 || strProtocol.indexOf('\\') != -1) {
                IOException ioexception3 = new IOException("58");
                throw ioexception3;
            }
            strProtocol = strProtocol.toLowerCase();
            i = k + 1;
        }
        for (; j > i && (s.charAt(j - 1) == '\\' || s.charAt(j - 1) == '/'); j--) ;
        for (; i < j && (s.charAt(i) == '\\' || s.charAt(i) == '/'); i++) ;
        int l = s.indexOf(':', i);
        if (l == i) {
            IOException ioexception4 = new IOException("58");
            throw ioexception4;
        }
        if (l != -1 && l != j) {
            String s1 = s.substring(l + 1, j);
            iPort = Integer.parseInt(s1);
            j = l;
        }
        strAddress = s.substring(i, j);
            strProtocol = "tcp";
    }

    public synchronized String getURL() {
        StringBuilder sb = new StringBuilder(80);
        sb.append(strProtocol);
        sb.append("://");
        sb.append(strAddress);
        if (iPort != 0) {
            sb.append(':');
            sb.append(iPort);
        }
        sb.append('/');
        String s = sb.toString();
        return s;
    }

    public synchronized void setProtocolProperties(Properties properties) throws IOException {
        if (bShouldBeOpen && jgaReal.bOpen) {
            IOException ioexception = new IOException("62");
            throw ioexception;
        } else {
            protocolProperties = properties;
            return;
        }
    }

    public synchronized Properties getProtocolProperties() {
        return protocolProperties;
    }

    public synchronized boolean isOpen() {
        boolean flag = bShouldBeOpen && jgaReal.bOpen;
        return flag;
    }

    public synchronized void setSecurity(String s, String s1) throws IOException {
        if (bShouldBeOpen && jgaReal.bOpen) {
            IOException ioexception = new IOException("62");
            throw ioexception;
        }
        strClientSecurity = s;
        strServerSecurity = s1;
        if (strClientSecurity != null && strClientSecurity.length() != 0 && strServerSecurity != null && strServerSecurity.length() != 0)
            bInitialFlow = true;
    }

    public synchronized void setInitialFlow(boolean flag) throws IOException {
        if (bShouldBeOpen && jgaReal.bOpen) {
            IOException ioexception = new IOException("62");
            throw ioexception;
        } else {
            bInitialFlow = flag;
            return;
        }
    }

    public synchronized boolean isInitialFlow() {
        boolean flag = jgaReal == null ? bInitialFlow : jgaReal.bInitialFlow;
        return flag;
    }

    public synchronized Locale getGatewayLocale() {
        Locale locale = null;
        if (jgaReal != null)
            locale = jgaReal.locServer;
        return locale;
    }

    public synchronized String getGatewayOs() {
        String s = null;
        if (jgaReal != null)
            s = jgaReal.strGatewayOs;
        return s;
    }
}

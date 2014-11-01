package org.fbi.ctgproxy;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SafeIP {
    private static boolean bAddName = false;
    private static Object objAddNameMonitor = null;

    public SafeIP() {
    }

    public static void setNamesOn(boolean flag) {
        synchronized (objAddNameMonitor) {
            bAddName = flag;
        }
    }

    public static boolean getNamesState() {
        return bAddName;
    }

    public static String toString(Socket socket) {
        StringBuilder sb = new StringBuilder(64);
        sb.append("Socket[addr=");
        InetAddress inetaddress = socket.getInetAddress();
        if (bAddName) {
            sb.append(inetaddress.getHostName());
            sb.append('/');
        }
        byte abyte0[] = inetaddress.getAddress();
        sb.append((abyte0[0] + 256) % 256);
        sb.append('.');
        sb.append((abyte0[1] + 256) % 256);
        sb.append('.');
        sb.append((abyte0[2] + 256) % 256);
        sb.append('.');
        sb.append((abyte0[3] + 256) % 256);
        sb.append(",port=");
        sb.append(socket.getPort());
        sb.append(",localport=");
        sb.append(socket.getLocalPort());
        sb.append(']');
        return sb.toString();
    }

    public static String toString(ServerSocket serversocket) {
        StringBuilder sb = new StringBuilder(64);
        sb.append("ServerSocket[addr=");
        byte abyte0[] = serversocket.getInetAddress().getAddress();
        sb.append((abyte0[0] + 256) % 256);
        sb.append('.');
        sb.append((abyte0[1] + 256) % 256);
        sb.append('.');
        sb.append((abyte0[2] + 256) % 256);
        sb.append('.');
        sb.append((abyte0[3] + 256) % 256);
        sb.append(",localport=");
        sb.append(serversocket.getLocalPort());
        sb.append(']');
        return sb.toString();
    }

    public static String toString(InetAddress inetaddress) {
        StringBuilder sb = new StringBuilder(64);
        sb.append("InetAddress[addr=");
        if (bAddName) {
            sb.append(inetaddress.getHostName());
            sb.append('/');
        }
        byte abyte0[] = inetaddress.getAddress();
        sb.append((abyte0[0] + 256) % 256);
        sb.append('.');
        sb.append((abyte0[1] + 256) % 256);
        sb.append('.');
        sb.append((abyte0[2] + 256) % 256);
        sb.append('.');
        sb.append((abyte0[3] + 256) % 256);
        sb.append(']');
        return sb.toString();
    }

    static {
        objAddNameMonitor = new Object();
    }
}

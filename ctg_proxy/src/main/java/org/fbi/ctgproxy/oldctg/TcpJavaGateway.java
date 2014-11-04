package org.fbi.ctgproxy.oldctg;

import org.fbi.ctgproxy.CtgRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Stack;
import java.util.Vector;

public class TcpJavaGateway extends JavaGatewayInterface implements Runnable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Thread thrListener;
    private Socket socJGate;
    private static int iDefaultPort = 2006;
    private Vector vgatFlowed;
    private Vector vobjMonitor;
    private Vector vcalBacks;
    private Stack sintReuse;
    private int iVectorSize;
    private boolean bThreadOk;
    private boolean bNotifyCalled;

    TcpJavaGateway() {
        thrListener = null;
        socJGate = null;
        vgatFlowed = null;
        vobjMonitor = null;
        vcalBacks = null;
        sintReuse = null;
        iVectorSize = 0;
        bThreadOk = false;
        bNotifyCalled = false;
    }

    synchronized void open() throws IOException {
        if (strAddress == null || strAddress.trim().length() == 0)
            throw new IOException("58");
        if (iPort == 0) {
            iPort = iDefaultPort;
        }
        try {
            socJGate = new Socket(strAddress, iPort);
            ipGateway = socJGate.getInetAddress();
        } catch (IOException ioexception) {
            IOException ioexception3 = new IOException("51");
            throw ioexception3;
        }
        thrListener = new Thread(this, "LL-" + SafeIP.toString(socJGate) + toString());
        logger.info("Starting Listener thread = " + thrListener);
        try {
            thrListener.setDaemon(true);
        } catch (SecurityException securityexception) {
            logger.error("Caught SecurityException ", securityexception);
        }
        bThreadOk = false;
        vgatFlowed = new Vector(5, 0);
        vobjMonitor = new Vector(5, 0);
        vcalBacks = new Vector(5, 0);
        sintReuse = new Stack();

        thrListener.start();
        synchronized (this) {
            try {
                if (!bNotifyCalled)
                    wait(10000L);
            } catch (InterruptedException interruptedexception) {
                //
            }
            bNotifyCalled = false;
        }
        if (thrListener == null || !bThreadOk) {
            socJGate.close();
            socJGate = null;
            IOException ioexception1 = new IOException("52");
            throw ioexception1;
        }
        logger.info("Listener thread started OK");

        synchronized (objOpenMonitor) {
            if (socJGate != null)
                bOpen = true;
        }
        try {
            initialFlow();
        } catch (IOException ioexception2) {
            close();
            throw ioexception2;
        }
    }

    int flow(CtgRequest gatewayrequest) throws IOException {
        synchronized (objOpenMonitor) {
            if (!bOpen) {
                IOException ioexception = new IOException("53");
                throw ioexception;
            }
        }
        int j = gatewayrequest.getiFlowType();
        if (j != 5) {
            gatewayrequest.setiFlowType(1);
            j = 1;
        }
        int k = 0;
        Event event = new Event();
        Callbackable callbackable = gatewayrequest.getCalBack();
        synchronized (this) {
            if (sintReuse.empty()) {
                k = iVectorSize++;
                gatewayrequest.setiMessageId(k);
                vgatFlowed.addElement(gatewayrequest);
                vobjMonitor.addElement(event);
                vcalBacks.addElement(callbackable);
            } else {
                k = ((Integer) sintReuse.pop()).intValue();
                gatewayrequest.setiMessageId(k);
                vgatFlowed.setElementAt(gatewayrequest, k);
                vobjMonitor.setElementAt(event, k);
                vcalBacks.setElementAt(callbackable, k);
            }
        }

        boolean flag = false;
        synchronized (event) {
            try {
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
                gatewayrequest.writeObject(dataoutputstream);
                byte abyte0[] = bytearrayoutputstream.toByteArray();
                if (cliSecurity != null && j == 1) {
                    logger.info("This connection has a ClientSecurity handler");
                    abyte0 = cliSecurity.encodeRequest(abyte0, gatewayrequest);
                    gatewayrequest.setbHasSecurity(true);
                } else {
                    gatewayrequest.setbHasSecurity(false);
                }
                gatewayrequest.setiDataWhichFollows(abyte0.length);
                bytearrayoutputstream.reset();
                gatewayrequest.writeObject(dataoutputstream);
                int i;
                if (gatewayrequest.getPasswordOffset() != -1 && cliSecurity == null)
                    i = bytearrayoutputstream.size() + gatewayrequest.getPasswordOffset();
                else
                    i = -1;
                bytearrayoutputstream.write(abyte0, 0, abyte0.length);
                byte abyte1[] = bytearrayoutputstream.toByteArray();
                synchronized (objOpenMonitor) {
                    if (bOpen) {
                        synchronized (socJGate) {
                            socJGate.getOutputStream().write(abyte1);
                            flag = true;
                        }
                    } else {
                        IOException ioexception4 = new IOException("66");
                        throw ioexception4;
                    }
                }
            } catch (IOException ioexception2) {
                IOException ioexception3 = new IOException("67");
                throw ioexception3;
            } finally {
                if (flag) {
                    logger.info("Flow was successful - suspending thread on monitor ,event=" + event);
                    try {
                        event.waitForEvent();
                    } catch (Exception exception5) {
                        //
                    }
                } else {
                    vgatFlowed.setElementAt(null, k);
                    vobjMonitor.setElementAt(null, k);
                    vcalBacks.setElementAt(null, k);
                    sintReuse.push(new Integer(k));
                }
            }
        }
        if (gatewayrequest.getiFlowType() == 6) {
            IOException ioexception1 = new IOException("71");
            throw ioexception1;
        } else {
            int l = gatewayrequest.getiGatewayRc();
            return l;
        }
    }

    protected void finalize() throws IOException {
        close();
    }

    synchronized void close() throws IOException {
        label0:
        {
            Object obj = null;
            synchronized (objOpenMonitor) {
                if (bOpen)
                    break label0;
            }
            return;
        }
        Thread thread;
        bOpen = false;
        thread = thrListener;
        String s = System.getProperty("os.name");
        CtgRequest gatewayrequest = new CtgRequest();
        gatewayrequest.setiFlowType(10);
        gatewayrequest.setiMessageId(-1);
        try {
            sendSimpleRequest(gatewayrequest);
        } catch (IOException ioexception) {
            //;
        }


        try {
            logger.info("Waiting for listener to die...");
            thread.join(2000L);
            logger.info("Listener should be dead !!");
        } catch (Exception exception) {
            //
        }
        return;
    }

    private void sendSimpleRequest(CtgRequest gatewayrequest) throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
        gatewayrequest.writeObject(dataoutputstream);
        synchronized (socJGate) {
            bytearrayoutputstream.writeTo(socJGate.getOutputStream());
        }
    }

    public void run() {
/*
        DataInputStream dis;
        CtgRequest gatewayrequest;
        Event event;
        int j;
        dis = null;
        try {
            dis = new DataInputStream(socJGate.getInputStream());
        } catch (IOException ioexception) {
            thrListener = null;
        }
        synchronized (this) {
            bThreadOk = true;
            bNotifyCalled = true;
            notify();
        }
        if (thrListener == null) {
            logger.error("Listener thread exiting due to an error");
            return;
        }
        gatewayrequest = new CtgRequest();
        Object obj = null;
        event = null;
        Object obj1 = null;
        boolean flag = false;
        j = 61441;
        _L1:
        if (thrListener == null)
            break MISSING_BLOCK_LABEL_1068;
        int k;
        label0:
        {
            T.ln(this, "Waiting for a reply ...");
            gatewayrequest.readObject(dis);
            k = gatewayrequest.getiFlowType();
            if (k != 7)
                break label0;
            T.ln(this, "Type: FLOW_PING");
            gatewayrequest.setiFlowType(8);
            sendSimpleRequest(gatewayrequest);
        }
        goto _L1
        CtgRequest gatewayrequest1;
        int i;
        label1:
        {
            i = gatewayrequest.getiMessageId();
            if (i == -1) {
                T.ln(this, "Gateway has been closed");
                j = 61444;
                throw new IOException("Gateway closed");
            }
            gatewayrequest1 = (CtgRequest) vgatFlowed.elementAt(i);
            if (gatewayrequest1 == null || !gatewayrequest.getStrRequestType().equals(gatewayrequest1.getstrRequestType())) {
                if (gatewayrequest1 == null)
                    T.ln(this, "!! unexpected flow !!");
                else
                    T.ln(this, "!! Reply does not match request !!");
                int l = gatewayrequest.getiDataWhichFollows();
                try {
                    InputStream inputstream = socJGate.getInputStream();
                    for (; l > 0; l--)
                        inputstream.read();

                } catch (IOException ioexception2) {
                    T.ex(this, ioexception2);
                }
                gatewayrequest.setiFlowType(4);
                gatewayrequest.setiGatewayRc(61443);
            }
            if (gatewayrequest1 != null)
                break label1;
            sendSimpleRequest(gatewayrequest);
        }
        goto _L1
                event = (Event) vobjMonitor.elementAt(i);
        switch (k) {
            case 4: // '\004'
                T.ln(this, "Type: FLOW_ERROR");
                gatewayrequest1.setRc(gatewayrequest.getRc());
                break;

            case 2: // '\002'
                T.ln(this, "Type: FLOW_CONFIRM");
                gatewayrequest1.setRc(gatewayrequest.getRc());
                vobjMonitor.setElementAt(null, i);
                break;

            case 3: // '\003'
                T.ln(this, "Type: FLOW_REPLY");
                if (cliSecurity != null && gatewayrequest.getHasSecurity()) {
                    T.ln(this, "This connection has a ClientSecurity handler");
                    byte abyte0[] = new byte[gatewayrequest.getDataWhichFollows()];
                    dis.readFully(abyte0, 0, abyte0.length);
                    T.ln(this, "Read remaining {0} bytes of this request", new Integer(abyte0.length));
                    T.ln(this, "Passing encoded flow to {0}", cliSecurity);
                    abyte0 = cliSecurity.decodeReply(abyte0);
                    T.ln(this, "Decoded request length = {0} bytes", new Integer(abyte0.length));
                    DataInputStream datainputstream1 = new DataInputStream(new ByteArrayInputStream(abyte0));
                    gatewayrequest1.readObject(datainputstream1);
                } else {
                    gatewayrequest1.readObject(dis);
                }
                if (cliSecurity != null && gatewayrequest.getHasSecurity()) {
                    T.ln(this, "Calling ClientSecurity's afterDecode");
                    cliSecurity.afterDecode(gatewayrequest1);
                }
                Callbackable callbackable = (Callbackable) vcalBacks.elementAt(i);
                if (callbackable != null) {
                    T.ln(this, "There is an associated Callbackable {0}", callbackable);
                    callbackable.setResults(gatewayrequest1);
                    (new Thread(callbackable)).start();
                }
                T.ln(this, "Flow reply.  Cleaning up slot {0}", new Integer(i));
                vgatFlowed.setElementAt(null, i);
                vobjMonitor.setElementAt(null, i);
                vcalBacks.setElementAt(null, i);
                sintReuse.push(new Integer(i));
                break;

            case 5: // '\005'
                T.ln(this, "Type: FLOW_HANDSHAKE");
                gatewayrequest1.setRoot(gatewayrequest);
                T.ln(this, "Flow handshake.  Cleaning up slot {0}", new Integer(i));
                vgatFlowed.setElementAt(null, i);
                vobjMonitor.setElementAt(null, i);
                vcalBacks.setElementAt(null, i);
                sintReuse.push(new Integer(i));
                break;

            case 6: // '\006'
                T.ln(this, "Type: FLOW_EXCEPTION");
                gatewayrequest1.setRoot(gatewayrequest);
                T.ln(this, "Flow exception.  Cleaning up slot {0}", new Integer(i));
                vgatFlowed.setElementAt(null, i);
                vobjMonitor.setElementAt(null, i);
                vcalBacks.setElementAt(null, i);
                sintReuse.push(new Integer(i));
                break;
        }
        if (event != null)
            synchronized (event) {
                T.ln(this, "Rousing thread waiting on monitor {0}", event);
                event.signalEvent();
            }
        goto _L1
        IOException ioexception1;
        ioexception1;
        T.ex(this, ioexception1);
        synchronized (objOpenMonitor) {
            bOpen = false;
            try {
                if (socJGate != null) {
                    Socket socket = socJGate;
                    socJGate = null;
                    socket.close();
                }
            } catch (Exception exception1) {
                T.ex(this, exception1);
            }
        }
        if (vgatFlowed != null) {
            for (int i1 = 0; i1 < vgatFlowed.size(); i1++) {
                GatewayRequest gatewayrequest2;
                if ((gatewayrequest2 = (GatewayRequest) vgatFlowed.elementAt(i1)) == null)
                    continue;
                T.ln(this, "Cleaned up outstanding request at slot {0}", new Integer(i1));
                gatewayrequest2.setRc(j);
                Event event2 = (Event) vobjMonitor.elementAt(i1);
                if (event2 == null)
                    continue;
                synchronized (event2) {
                    T.ln(this, "Rousing thread waiting on monitor {0}", event);
                    event2.signalEvent();
                }
            }

        }
        thrListener = null;
        T.out(this, "run");
        return;
*/
    }

}

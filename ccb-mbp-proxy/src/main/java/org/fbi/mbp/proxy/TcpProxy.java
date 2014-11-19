package org.fbi.mbp.proxy;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhanrui on 2014/9/8.
 */
public class TcpProxy {
    private static final int THREADS = ProjectConfigManager.getInstance().getIntProperty("proxy_server_threads");
    private static final Executor executor = Executors.newFixedThreadPool(THREADS);

    private final String mbpHost = ProjectConfigManager.getInstance().getStringProperty("mbp.server.ip");
    private final int mbpPort = ProjectConfigManager.getInstance().getIntProperty("mbp.server.port");
    private int timeout = ProjectConfigManager.getInstance().getIntProperty("mbp.server.timeout.ms"); //默认超时时间：ms  连接超时与读超时统一

    private static int MSG_HEADER_LENGTH = 60 + 51;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(ProjectConfigManager.getInstance().getIntProperty("proxy_server_port"));
        while (true) {
            final Socket connection = serverSocket.accept();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        handleRequest(connection);
                    } catch (Exception e) {
                        logger.error("Server error:", e);
                    }
                }
            };
            executor.execute(task);
        }
    }

    void handleRequest(Socket connection) throws IOException {
        SocketAddress clientAddress = connection.getRemoteSocketAddress();
        logger.debug("Client IP: " + clientAddress);

        try {
            InputStream in = connection.getInputStream();
            OutputStream out = connection.getOutputStream();

            byte[] inHeaderBuf = new byte[MSG_HEADER_LENGTH];
            int readNum = in.read(inHeaderBuf);
            if (readNum == -1) {
                throw new RuntimeException("服务器连接已关闭!");
            }
            if (readNum < MSG_HEADER_LENGTH) {
                throw new RuntimeException("读取报文头长度部分错误...");
            }

            VipMsgHeader msgHeader = new VipMsgHeader(new String(inHeaderBuf));
            String txnCode = msgHeader.getTxCode();
            MDC.put("txnCode", txnCode);

            int bodyLen = Integer.parseInt(msgHeader.getMsgDataLen());

            logger.info(">>>>报文体长度:" + bodyLen);
            byte[] inBodyBuf = new byte[bodyLen];

            readNum = in.read(inBodyBuf);   //阻塞读
            if (readNum != bodyLen) {
                throw new RuntimeException("报文长度错误,应为:[" + bodyLen + "], 实际获取长度:[" + readNum + "]");
            }

            final String inBodyData = new String(inBodyBuf, "GBK");
            logger.debug(">>>>Client request header:" + msgHeader.toString());
            logger.debug(">>>>Client request body:" + inBodyData);

            //2014-11-13 对4879到账通知报文进行处理
            if ("4879".equals(txnCode)) {
                Runnable task4879 = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);  //wait MBP to copy winbridge tmp file...
                        } catch (InterruptedException e) {
                            //
                        }
                        processT4879(inBodyData);
                        logger.debug("T4879处理完成");
                    }
                };
                executor.execute(task4879);
            }

            //proxy
            handleProxy(bytesMerger(inHeaderBuf, inBodyBuf), out);
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                //
            }
            MDC.remove("txnCode");
        }
    }

    private void handleProxy(byte[] sendRemoteBuf, OutputStream sendClientOS) throws IOException {
        InetAddress addr = InetAddress.getByName(mbpHost);
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(addr, mbpPort), timeout);
            socket.setSoTimeout(timeout);

            OutputStream os = socket.getOutputStream();
            os.write(sendRemoteBuf);
            os.flush();
            InputStream is = socket.getInputStream();

            byte[] inHeaderBuf = new byte[MSG_HEADER_LENGTH];
            int readNum = is.read(inHeaderBuf);
            if (readNum == -1) {
                throw new RuntimeException("服务器连接已关闭!");
            }
            if (readNum < MSG_HEADER_LENGTH) {
                throw new RuntimeException("读取报文头长度部分错误...");
            }

            VipMsgHeader msgHeader = new VipMsgHeader(new String(inHeaderBuf));
            int bodyLen = Integer.parseInt(msgHeader.getMsgDataLen());

            logger.info("<<<<<<<<报文体长度:" + bodyLen);
            byte[] inBodyBuf = new byte[bodyLen];

            readNum = is.read(inBodyBuf);   //阻塞读
            if (readNum != bodyLen) {
                throw new RuntimeException("报文长度错误,应为:[" + bodyLen + "], 实际获取长度:[" + readNum + "]");
            }

            String inBodyData = new String(inBodyBuf, "GBK");
            logger.debug("<<<<<<<<Remote server response header:" + msgHeader.toString());
            logger.debug("<<<<<<<<Remote server response body:" + inBodyData);

            byte[] sendClientBuf = bytesMerger(inHeaderBuf, inBodyBuf);
            sendClientOS.write(sendClientBuf, 0, sendClientBuf.length);
            sendClientOS.flush();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                //
            }
        }
    }


    private byte[] bytesMerger(byte[] bytes1, byte[] bytes2) {
        byte[] bytes3 = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, bytes3, 0, bytes1.length);
        System.arraycopy(bytes2, 0, bytes3, bytes1.length, bytes2.length);
        return bytes3;
    }

    private void processT4879(String reqXml) {
        int startIndex = reqXml.indexOf("<FileName>") + "<FileName>".length();
        int endIndex = reqXml.indexOf("</FileName>");
        String filename = reqXml.substring(startIndex, endIndex);

        Integer repeatTimes = ProjectConfigManager.getInstance().getIntProperty("txn4879.fip.fail.repeat.times");
        String tmpfilePath = ProjectConfigManager.getInstance().getStringProperty("txn4879.file.path");
        String fipUrl = ProjectConfigManager.getInstance().getStringProperty("txn4879.fip.url");
        Integer fipDelaySeconds = ProjectConfigManager.getInstance().getIntProperty("txn4879.fip.fail.repeat.delay.second");

        int count = 0;
        //等待CCB文件
        String xml = null;
        while (count <= 10) {
            try {
                xml = readFileByLines(tmpfilePath + filename);
            } catch (Exception e) {
                try {
                    Thread.sleep(fipDelaySeconds * 1000);
                } catch (InterruptedException e1) {
                    //
                }
                count++;
            }
        }

        if (xml == null) {
            logger.error("文件未发现:" + tmpfilePath + filename);
            return;
        }

        boolean isDone = false;
        count = 0;
        while (!isDone && count <= repeatTimes) {
            try {
                String resp = doPost(fipUrl, xml, "GBK");
                if (StringUtils.isEmpty(resp)) {
                    count++;
                    Thread.sleep(fipDelaySeconds * 1000);
                    logger.error("<<<<<<<<Fip response error.");
                } else {
                    isDone = true;
                    logger.debug("<<<<<<<<" + resp);
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(fipDelaySeconds * 1000);
                } catch (InterruptedException e1) {
                    //
                }
                count++;
            }
        }
    }

    private synchronized String readFileByLines(String fileName) {
        StringBuilder sb = new StringBuilder();
        if (fileName == null || "".equals(fileName)) {
            throw new IllegalArgumentException("文件名不能为空.");
        }
        File file = new File(fileName);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            logger.error("文件不存在", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("文件处理错误", e);
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error("文件处理错误", e);
                }
            }
        }
        return sb.toString();
    }

    private String doPost(String serverUrl, String datagram, String charsetName) {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            //请求超时
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000 * 5);
            //读取超时
            httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1000 * 10);

            HttpPost httppost = new HttpPost(serverUrl);
            httppost.getURI();
            StringEntity xmlSE = new StringEntity(datagram, charsetName);
            httppost.setEntity(xmlSE);

            HttpResponse httpResponse = httpclient.execute(httppost);

            //HttpStatus.SC_OK)表示连接成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(httpResponse.getEntity(), charsetName);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Http 通讯错误", e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

    //=================
    public static void main(String[] args) throws IOException {
        TcpProxy proxy = new TcpProxy();
        proxy.logger.info("CCB to MBP proxy server is starting...");
        proxy.start();
    }
}

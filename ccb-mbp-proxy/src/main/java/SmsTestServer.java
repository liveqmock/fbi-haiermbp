import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhanrui on 2014/9/8.
 */
public class SmsTestServer {
    private static final  int THREADS = 2;
    private static final Executor executor = Executors.newFixedThreadPool(THREADS);

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9495);
        while (true) {
            final Socket connection = serverSocket.accept();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream in = connection.getInputStream();
                        OutputStream out = connection.getOutputStream();

                        byte[] inHeaderBuf = new byte[8];
                        int readNum = in.read(inHeaderBuf);
                        if (readNum == -1) {
                            throw new RuntimeException("�����������ѹر�!");
                        }
                        if (readNum < 8) {
                            throw new RuntimeException("��ȡ����ͷ���Ȳ��ִ���...");
                        }
                        int bodyLen = Integer.parseInt(new String(inHeaderBuf).trim());

                        System.out.println(">>>>�����峤��:" + bodyLen);
                        byte[] inBodyBuf = new byte[bodyLen];

                        readNum = in.read(inBodyBuf);   //������
                        if (readNum != bodyLen) {
                            throw new RuntimeException("���ĳ��ȴ���,ӦΪ:[" + bodyLen + "], ʵ�ʻ�ȡ����:[" + readNum + "]");
                        }

                        final String inBodyData = new String(inBodyBuf, "GBK");
                        System.out.println(">>>>Client request body:" + inBodyData);

                        //��Ӧ
                        out.write("4       1234".getBytes());
                        out.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            executor.execute(task);
        }
    }

    //=================
    public static void main(String[] args) throws IOException {
        SmsTestServer svr = new SmsTestServer();
        svr.start();
    }
}

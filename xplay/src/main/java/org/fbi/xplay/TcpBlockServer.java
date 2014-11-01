package org.fbi.xplay;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhanrui on 2014/9/8.
 * 阻塞 短链接
 */
public class TcpBlockServer implements Server {
    private Executor executor = Executors.newFixedThreadPool(100);
    private int port;
    private final ArrayList<ServerListener> listeners = new ArrayList<ServerListener>();
    private ChannelHandler channelHandler;

    public void start() throws IOException {
        ServerHelper.start(this);
    }

    @Override
    public Executor getWorkerpool() {
        return null;
    }

    @Override
    public Server setWorkerpool(Executor workerpool) {
        return null;
    }

    @Override
    public Server addListener(ServerListener listener) {
        listeners.add(listener);
        return this;
    }

    @Override
    public boolean removeListener(ServerListener listener) {
        return listeners.remove(listener);
    }

    @Override
    public Server handler(ChannelHandler handler) {
        this.channelHandler = handler;
        return this;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public Server executor(Executor executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public Server bind(int port) {
        this.port = port;
        return this;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);

            //通知listener  TODO：抽出单独类
            for (ServerListener listener : (ArrayList<ServerListener>) listeners.clone()) {
                listener.onInit();
            }

            while (true) {
                final Socket connection = serverSocket.accept();

                final DefaultChannelContext channelContext = new DefaultChannelContext(connection);
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            channelHandler.onRead(channelContext);
                        } catch (IOException e) {
                            //throw new RuntimeException(e);
                            e.printStackTrace();
                        }
                    }
                };
                executor.execute(task);
            }
        } catch (IOException e) {
            throw new RuntimeException("Xplay Server init error.", e);
        }
    }
}

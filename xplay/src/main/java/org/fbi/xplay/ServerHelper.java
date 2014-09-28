package org.fbi.xplay;

import java.net.SocketTimeoutException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhanrui on 2014/9/25.
 */
public class ServerHelper {
    public static void start(Server server) throws SocketTimeoutException {
        start(server, 60);
    }

    public static void start(Server server, int timeoutSecond) throws SocketTimeoutException {
        final CountDownLatch startedSignal = new CountDownLatch(1);
        ServerListener startupListener = new ServerListener() {
            public void onInit() {
                startedSignal.countDown();
            }

            public void onDestroy() {
            }
        };

        server.addListener(startupListener);
        Thread t = new Thread(server);
        t.setName("xplayServer");
        t.start();

        boolean isStarted = false;
        try {
            isStarted = startedSignal.await(timeoutSecond, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Server started signal error. " + e.toString());
        }

        //timeout
        if (!isStarted) {
            throw new SocketTimeoutException("Server start timeout (" + toFormatedDuration((long) timeoutSecond * 1000) + ")");
        }

        t.setName("XplayServer:" + server.getLocalPort());

        server.removeListener(startupListener);
    }

    public static String toFormatedDuration(long duration) {
        if (duration < 5 * 1000) {
            return duration + "ms";
        } else if (duration < (60 * 1000)) {
            return ((int) (duration / 1000)) + "s";
        } else if (duration < (60 * 60 * 1000)) {
            return ((int) (duration / (60 * 1000))) + "m";
        } else if (duration < (24 * 60 * 60 * 1000)) {
            return ((int) (duration / (60 * 60 * 1000))) + "h";
        } else {
            return ((long) (duration / (24 * 60 * 60 * 1000))) + "d";
        }
    }
}
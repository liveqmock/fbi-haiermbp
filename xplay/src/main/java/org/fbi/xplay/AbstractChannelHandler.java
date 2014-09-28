package org.fbi.xplay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

/**
 * Created by zhanrui on 2014/9/23.
 */
public abstract class AbstractChannelHandler implements Runnable, ChannelHandler {
    protected Socket connection;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected AbstractChannelHandler() {
    }

    public void init(Socket connection) {
        this.connection = connection;
    }
/*
    public void init(Socket connection,
                              ConcurrentLinkedQueue<String> taskLogQueue,
                              ConcurrentLinkedQueue<String> warningTaskLogQueue) {
        this.connection = connection;
        this.taskLogQueue = taskLogQueue;
        this.warningTaskLogQueue = warningTaskLogQueue;
    }
*/


/*
    @Override
    public void run() {
        try {
            handleRequest();
        } catch (Exception e) {
            logger.error("Server error:", e);
        }
    }

*/


    protected byte[] bytesMerger(byte[] bytes1, byte[] bytes2) {
        byte[] bytes3 = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, bytes3, 0, bytes1.length);
        System.arraycopy(bytes2, 0, bytes3, bytes1.length, bytes2.length);
        return bytes3;
    }
}

package org.fbi.ctgserver;

import org.fbi.ctgproxy.ProjectConfigManager;
import org.fbi.ctgproxy.TxnMonitor;
import org.fbi.xplay.TcpBlockServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhanrui on 2014/9/23.
 * CTG SERVER
 */
public class CtgServer {
    private static Logger logger = LoggerFactory.getLogger(CtgServer.class);

    public static void main(String[] args) throws IOException {
        int THREADS = ProjectConfigManager.getInstance().getIntProperty("proxy_server_threads");
        Executor executor = Executors.newFixedThreadPool(THREADS);

        TcpBlockServer proxy = new TcpBlockServer();

        ConcurrentLinkedQueue<String> taskLogQueue = new ConcurrentLinkedQueue<String>();
        ConcurrentLinkedQueue<String> warningTaskLogQueue = new ConcurrentLinkedQueue<String>();

        Integer port = ProjectConfigManager.getInstance().getIntProperty("proxy_server_port");
        port = 2007;

        proxy.executor(executor)
                .bind(port)
                .handler(new ServerHandler(taskLogQueue, warningTaskLogQueue));

        //½»Ò×¼à¿Ø
        new Thread(new TxnMonitor(taskLogQueue, warningTaskLogQueue), "Txn monitor").start();
        logger.info("CtgServer txn monitor started...");

        //PROXY
        proxy.start();
        logger.info("CtgServer server started...");
    }
}

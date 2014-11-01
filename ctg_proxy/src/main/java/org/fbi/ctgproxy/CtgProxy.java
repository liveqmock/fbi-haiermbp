package org.fbi.ctgproxy;

import org.fbi.xplay.TcpBlockServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhanrui on 2014/9/23.
 * SBS -> MBP  交易代理
 * 对外支付交易
 */
public class CtgProxy {
    private static Logger logger = LoggerFactory.getLogger(CtgProxy.class);

    public static void main(String[] args) throws IOException {
        int THREADS = ProjectConfigManager.getInstance().getIntProperty("proxy_server_threads");
        Executor executor = Executors.newFixedThreadPool(THREADS);

        TcpBlockServer proxy = new TcpBlockServer();

        ConcurrentLinkedQueue<String> taskLogQueue = new ConcurrentLinkedQueue<String>();
        ConcurrentLinkedQueue<String> warningTaskLogQueue = new ConcurrentLinkedQueue<String>();

        Integer port = ProjectConfigManager.getInstance().getIntProperty("proxy_server_port");
        port = 2006;

        proxy.executor(executor)
                .bind(port)
                .handler(new ProxyHandler(taskLogQueue, warningTaskLogQueue));

        //交易监控
        new Thread(new TxnMonitor(taskLogQueue, warningTaskLogQueue), "Txn monitor").start();
        logger.info("CtgProxy txn monitor started...");

        //PROXY
        proxy.start();
        logger.info("CtgProxy server started...");
    }
}

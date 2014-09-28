package org.fbi.mbp.proxy;

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
public class SbsToMbpProxy {
    private static Logger logger = LoggerFactory.getLogger(SbsToMbpProxy.class);

    public static void main(String[] args) throws IOException {
        int THREADS = ProjectConfigManager.getInstance().getIntProperty("proxy_server_threads");
        Executor executor = Executors.newFixedThreadPool(THREADS);

        TcpBlockServer proxy = new TcpBlockServer();

        ConcurrentLinkedQueue<String> taskLogQueue = new ConcurrentLinkedQueue<String>();
        ConcurrentLinkedQueue<String> warningTaskLogQueue = new ConcurrentLinkedQueue<String>();

        proxy.executor(executor)
                .bind(ProjectConfigManager.getInstance().getIntProperty("proxy_server_port"))
                .handler(new SbsToMbpMsgHandler(taskLogQueue, warningTaskLogQueue));

        //交易监控
        new Thread(new TxnMonitor(taskLogQueue, warningTaskLogQueue)).start();
        logger.info("SbsToMbp proxy txn monitor started...");

        //PROXY
        proxy.start();
        logger.info("SbsToMbp proxy server started...");
    }
}

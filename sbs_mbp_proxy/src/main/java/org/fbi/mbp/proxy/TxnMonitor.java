package org.fbi.mbp.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhanrui on 2014/9/17.
 * 交易监控及预警
 */
public class TxnMonitor implements Runnable {
    private ConcurrentLinkedQueue<String> taskLogQueue;
    private ConcurrentLinkedQueue<String> warningLogQueue;

    private FileOutputStream taskLogOut;
    private FileOutputStream warningLogOut;
    private int count = 0;
    private int warningCount = 0;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public TxnMonitor(ConcurrentLinkedQueue<String> taskLogQueue, ConcurrentLinkedQueue<String> warningLogQueue) throws FileNotFoundException {
        String prj_root_dir = ProjectConfigManager.getInstance().getStringProperty("prj_root_dir");
        this.taskLogOut = new FileOutputStream(new File(prj_root_dir + "/log/txnstats.txt"), true);
        this.warningLogOut = new FileOutputStream(new File(prj_root_dir + "/log/txnstats_warning.txt"), true);
        this.taskLogQueue = taskLogQueue;
        this.warningLogQueue = warningLogQueue;
    }

    @Override
    public void run() {
        while (true) {
            //long start = System.nanoTime();
            boolean isEmpty = true;
            if (!taskLogQueue.isEmpty()) {
                try {
                    isEmpty = false;
                    count++;
                    taskLogOut.write(("" + count + "|").getBytes());
                    taskLogOut.write(taskLogQueue.poll().getBytes());
                    taskLogOut.write("\r\n".getBytes());
                } catch (IOException e) {
                    logger.error("TaskMonitor 队列读取失败.", e);
                }
            }

            if (!warningLogQueue.isEmpty()) {
                try {
                    isEmpty = false;
                    warningCount++;
                    warningLogOut.write(("" + warningCount + "|").getBytes());
                    warningLogOut.write(warningLogQueue.poll().getBytes());
                    warningLogOut.write("\r\n".getBytes());
                } catch (IOException e) {
                    logger.error("TaskMonitor 队列读取失败.", e);
                }

            }

            if (isEmpty) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    logger.error("TaskMonitor 写文件失败.", e);
                }
            }
        }
    }

    public  void addTaskLogToQueue(String log){
          this.taskLogQueue.add(log);
    }
    public  void addWarningLogToQueue(String log){
          this.warningLogQueue.add(log);
    }
}

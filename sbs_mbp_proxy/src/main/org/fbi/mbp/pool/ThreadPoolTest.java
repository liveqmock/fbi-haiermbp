package org.fbi.mbp.pool;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhanrui on 2014/9/28.
 */
public class ThreadPoolTest {
    public void start() {
        Executor executor = Executors.newFixedThreadPool(10);
        int cnt = 0;
        while (true) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        System.out.println(Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            executor.execute(task);
            cnt++;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (cnt > 20) {
                break;
            }
        }
    }

    public static void main(String... argv) {
        ThreadPoolTest pool = new ThreadPoolTest();
        pool.start();
        try {
            Thread.sleep(5000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

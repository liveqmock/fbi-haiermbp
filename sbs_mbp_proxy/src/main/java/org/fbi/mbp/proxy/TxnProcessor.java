package org.fbi.mbp.proxy;

/**
 * Created by zhanrui on 2014/10/11.
 */
public interface TxnProcessor {
    void process(TxnContext context);
}

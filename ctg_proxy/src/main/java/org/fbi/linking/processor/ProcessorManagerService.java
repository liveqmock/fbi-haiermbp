package org.fbi.linking.processor;

/**
 * User: zhanrui
 * Date: 13-8-22
 * Time: 上午7:58
 */
public interface ProcessorManagerService {
    String getName();
    Processor getProcessor(String txnCode) throws ClassNotFoundException, IllegalAccessException, InstantiationException;
}

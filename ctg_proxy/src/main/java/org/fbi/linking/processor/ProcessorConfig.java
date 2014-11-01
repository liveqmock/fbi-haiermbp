package org.fbi.linking.processor;


import java.util.Enumeration;

/**
 * User: zhanrui
 * Date: 13-5-17
 */
public interface ProcessorConfig {
    final static int LEN_MSG_HEADER = 92;
    final static int LEN_MSG_TXNCODE = 4;
    final static int LEN_MSG_ERRCODE = 4;
    final static int LEN_MSG_POSNO = 32;
    final static int LEN_MSG_CARDNO = 20;
    final static String clientUserId = "111";


    public String getProcessorName();
    public ProcessorContext getProcessorContext();
    public String getInitParameter(String name);
    public Enumeration<String> getInitParameterNames();
}

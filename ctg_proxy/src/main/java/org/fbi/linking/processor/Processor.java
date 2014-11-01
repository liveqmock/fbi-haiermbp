package org.fbi.linking.processor;


import java.io.IOException;

public interface Processor {
    public void init(ProcessorConfig config) throws ProcessorException;
    public ProcessorConfig getProcessorConfig();
    public String getProcessorInfo();
    public void service(ProcessorRequest request, ProcessorResponse response) throws ProcessorException, IOException;
    public void destroy();
}

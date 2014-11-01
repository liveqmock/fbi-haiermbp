package org.fbi.linking.processor;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;

/**
 * User: zhanrui
 * Date: 13-8-3
 * Time: ÉÏÎç11:45
 */
public abstract class GenericProcessor implements Processor, ProcessorConfig, Serializable {
    private static final long serialVersionUID = 1L;

    private transient ProcessorConfig config;

    public GenericProcessor() {
        //
    }

    @Override
    public void init(ProcessorConfig config) throws ProcessorException {
        this.config = config;
        this.init();
    }
    public void init() throws ProcessorException {
        //
    }

    @Override
    public ProcessorConfig getProcessorConfig() {
        return config;
    }

    @Override
    public String getProcessorInfo() {
        return "";
    }

    @Override
    public abstract void service(ProcessorRequest req, ProcessorResponse res) throws ProcessorException, IOException;

    @Override
    public void destroy() {
        //
    }

    @Override
    public String getProcessorName() {
        return config.getProcessorName();
    }

    @Override
    public ProcessorContext getProcessorContext() {
        return getProcessorConfig().getProcessorContext();
    }

    @Override
    public String getInitParameter(String name) {
        return getProcessorConfig().getInitParameter(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return getProcessorConfig().getInitParameterNames();
    }
}

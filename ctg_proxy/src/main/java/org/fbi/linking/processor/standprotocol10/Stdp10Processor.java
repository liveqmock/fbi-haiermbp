package org.fbi.linking.processor.standprotocol10;

import org.fbi.linking.processor.GenericProcessor;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.ProcessorRequest;
import org.fbi.linking.processor.ProcessorResponse;

import java.io.IOException;

/**
 * User: zhanrui
 * Date: 13-11-20
 */
public abstract  class Stdp10Processor extends GenericProcessor{
    private static final long serialVersionUID = 1L;

    public Stdp10Processor() {
        // NOOP
    }

    protected long getLastModified(Stdp10ProcessorRequest req) {
        return -1;
    }

    protected void service(Stdp10ProcessorRequest req, Stdp10ProcessorResponse resp) throws ProcessorException, IOException {
           //TODO
    }

    @Override
    public void service(ProcessorRequest req, ProcessorResponse res)
            throws ProcessorException, IOException {

        Stdp10ProcessorRequest  request;
        Stdp10ProcessorResponse response;

        try {
            request = (Stdp10ProcessorRequest) req;
            response = (Stdp10ProcessorResponse) res;
        } catch (ClassCastException e) {
            throw new ProcessorException("Non-Stdp request or response");
        }
        service(request, response);
    }

}

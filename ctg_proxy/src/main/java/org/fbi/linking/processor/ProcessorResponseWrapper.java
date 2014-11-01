package org.fbi.linking.processor;

/**
 * User: zhanrui
 * Date: 13-11-20
 */
public class ProcessorResponseWrapper implements ProcessorResponse{
    private ProcessorResponse response;

    public ProcessorResponseWrapper(ProcessorResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("Response cannot be null");
        }
        this.response = response;
    }
    public ProcessorResponse getResponse() {
        return this.response;
    }
    public void setResponse(ProcessorResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("Response cannot be null");
        }
        this.response = response;
    }
    @Override
    public void setCharacterEncoding(String charset) {
        this.response.setCharacterEncoding(charset);
    }
    @Override
    public String getCharacterEncoding() {
        return this.response.getCharacterEncoding();
    }

}

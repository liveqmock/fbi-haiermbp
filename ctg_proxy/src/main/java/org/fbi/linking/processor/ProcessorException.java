package org.fbi.linking.processor;

/**
 * User: zhanrui
 * Date: 13-8-3
 * Time: ионГ11:23
 */
public class ProcessorException extends Exception {
    private static final long serialVersionUID = 1L;

    public ProcessorException() {
        super();
    }
    public ProcessorException(String message) {
        super(message);
    }
    public ProcessorException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
    public ProcessorException(Throwable rootCause) {
        super(rootCause);
    }
    public Throwable getRootCause() {
        return getCause();
    }
}

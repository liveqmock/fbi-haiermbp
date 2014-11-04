package org.fbi.ctgproxy;

public interface Callbackable extends Runnable {
    void setResults(CtgRequest ctgRequest);
}
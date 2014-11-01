package org.fbi.ctgproxy;

public interface Callbackable extends java.lang.Runnable {
    void setResults(CtgRequest ctgRequest);
}
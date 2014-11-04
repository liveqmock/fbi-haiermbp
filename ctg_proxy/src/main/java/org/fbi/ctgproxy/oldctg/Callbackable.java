package org.fbi.ctgproxy.oldctg;

import org.fbi.ctgproxy.CtgRequest;

public interface Callbackable extends java.lang.Runnable {
    void setResults(CtgRequest ctgRequest);
}
package org.fbi.ctgserver.util.sbsmsg.domain;

public interface Assemble {
    void marshalMsgToBean(int offset, byte[] buffer);
}

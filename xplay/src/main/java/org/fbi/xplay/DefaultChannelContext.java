package org.fbi.xplay;

import java.net.Socket;

/**
 * Created by zhanrui on 2014/9/28.
 */
public class DefaultChannelContext implements ChannelContext {
    private  Socket connection;

    public DefaultChannelContext(Socket connection) {
        this.connection = connection;
    }

    @Override
    public Socket connection() {
        return connection;
    }
}

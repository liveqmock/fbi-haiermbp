package org.fbi.xplay;

import java.io.IOException;

/**
 * Created by zhanrui on 2014/9/28.
 */
public interface ChannelHandler {
    void onRead(ChannelContext ctx)throws IOException;
}

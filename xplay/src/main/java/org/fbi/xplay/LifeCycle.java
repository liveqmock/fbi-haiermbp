package org.fbi.xplay;

import java.io.IOException;

/**
 * Created by zhanrui on 2014/9/24.
 */
public interface LifeCycle {
    void onInit();
    void onDestroy() throws IOException;
}

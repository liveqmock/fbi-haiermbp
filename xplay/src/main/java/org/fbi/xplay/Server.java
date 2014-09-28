package org.fbi.xplay;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executor;

public interface Server extends Runnable, Closeable {
	public static final int DEFAULT_IDLE_TIMEOUT_SEC = 1 * 60 * 60;
	public static final int DEFAULT_CONNECTION_TIMEOUT_SEC = Integer.MAX_VALUE;


    void start() throws IOException;

    Server handler(ChannelHandler handler);
    Server executor(Executor executor);
    Server bind(int port);

    int getLocalPort();

/*
    boolean isOpen();

	long getIdleTimeoutMillis();
    Server setIdleTimeoutMillis(long timeoutInMillis);

	long getConnectionTimeoutMillis();
    Server setConnectionTimeoutMillis(long timeoutMillis);
*/

	Executor getWorkerpool();
    Server setWorkerpool(Executor workerpool);

	//Handler getHandler();

    Server addListener(ServerListener listener);
	boolean removeListener(ServerListener listener);

}

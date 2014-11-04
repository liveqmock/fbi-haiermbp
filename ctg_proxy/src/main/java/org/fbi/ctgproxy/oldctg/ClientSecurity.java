package org.fbi.ctgproxy.oldctg;

import org.fbi.ctgproxy.CtgRequest;

import java.io.IOException;
import java.net.InetAddress;

public interface ClientSecurity
{
	public abstract byte[] generateHandshake(InetAddress inetaddress)
		throws IOException;

	public abstract void repliedHandshake(byte abyte0[])
		throws IOException;

	public abstract byte[] encodeRequest(byte abyte0[], CtgRequest CtgRequest)
		throws IOException;

	public abstract byte[] decodeReply(byte abyte0[])
		throws IOException;

	public abstract void afterDecode(CtgRequest CtgRequest);
}

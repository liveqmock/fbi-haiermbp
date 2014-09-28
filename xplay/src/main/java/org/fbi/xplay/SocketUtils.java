package org.fbi.xplay;

/**
 * Created by zhanrui on 2014/9/28.
 */
public class SocketUtils {
    public static byte[] bytesMerger(byte[] bytes1, byte[] bytes2) {
        byte[] bytes3 = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, bytes3, 0, bytes1.length);
        System.arraycopy(bytes2, 0, bytes3, bytes1.length, bytes2.length);
        return bytes3;
    }
}

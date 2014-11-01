package org.fbi.linking.connector.cbs10;

import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: zhanrui
 * Date: 13-11-26
 * Time: 下午8:14
 */
public class Cbs10Response implements Stdp10ProcessorResponse {
    private String encoding = "GBK";
    private Map<String, String> headerMap = new HashMap<>();
    private byte[] body;


    @Override
    public void setHeader(String key, String value) {
        //TODO 判断异常情况
        headerMap.put(key, value);
    }

    @Override
    public void addHeader(String key, String value) {
        headerMap.put(key, value);
    }

    @Override
    public void setIntHeader(String s, int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addIntHeader(String s, int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getHeader(String key) {
        return headerMap.get(key);
    }

    @Override
    public Collection<String> getHeaders(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setResponseBody(byte[] body) {
        this.body = body;
    }

    @Override
    public byte[] getResponseBody() {
        return body;
    }

    @Override
    public String getCharacterEncoding() {
        return this.encoding;
    }

    @Override
    public void setCharacterEncoding(String s) {
        this.encoding = s;
    }

    @Override
    public String toString() {
        String header = "Cbs10Response header {" +
                "version=" + headerMap.get("version") + "," +
                "serialNo=" + headerMap.get("serialNo") + "," +
                "rtnCode=" + headerMap.get("rtnCode") + "," +
                "txnCode=" + headerMap.get("txnCode") + "," +
                "branchId=" + headerMap.get("branchId") + "," +
                "tellerId=" + headerMap.get("tellerId") + "," +
                "ueserId=" + headerMap.get("ueserId") + "," +
                "appId=" + headerMap.get("appId") + "," +
                "txnTime=" + headerMap.get("txnTime") + "," +
                "mac=" + headerMap.get("mac") + "}";
        String body = "";
        try {
            byte[] responseBody = this.getResponseBody();
            if (responseBody != null) {
                body = "Cbs10Response body {" + new String(responseBody, this.encoding) + "}";
            } else {
                body = "Cbs10Response body {}";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return header + "\n" + body;
    }
}

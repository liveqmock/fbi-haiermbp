package org.fbi.linking.connector.cbs10;

import org.fbi.linking.processor.ProcessorContext;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * User: zhanrui
 * Date: 13-11-26
 * Time: 下午8:06
 */
public class Cbs10Request implements Stdp10ProcessorRequest {
    private String encoding = "GBK";
    private Map<String, String> headerMap = new HashMap<>();
    private byte[] body;
    private ProcessorContext context;

    public Cbs10Request(String buf){
        if (buf == null || buf.length() < 111) {
            throw new RuntimeException("报文头字节长度错误！");
        }
        int index = 0 + 6;
        int step = 3;
        headerMap.put("version", buf.substring(index, index + step).trim());
        index += step;
        step = 18;
        headerMap.put("serialNo", buf.substring(index, index + step).trim());
        index += step;
        step = 4;
        headerMap.put("rtnCode", buf.substring(index, index + step).trim());
        index += step;
        step = 7;
        headerMap.put("txnCode", buf.substring(index, index + step).trim());
        index += step;
        step = 9;
        headerMap.put("branchId", buf.substring(index, index + step).trim());
        index += step;
        step = 12;
        headerMap.put("tellerId", buf.substring(index, index + step).trim());
        index += step;
        step = 6;
        headerMap.put("ueserId", buf.substring(index, index + step).trim());
        index += step;
        step = 6;
        headerMap.put("appId", buf.substring(index, index + step).trim());
        index += step;
        step = 14;
        headerMap.put("txnTime", buf.substring(index, index + step).trim());
        index += step;
        step = 32;
        headerMap.put("mac", buf.substring(index, index + step));

        index += step;
        try {
            this.body = buf.substring(index).getBytes(this.encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("编码类型错误：" + this.encoding);
        }
    }

    @Override
    public String getHeader(String s) {
        return headerMap.get(s);
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getIntHeader(String s) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public byte[] getRequestBody() {
        return body;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getAttribute(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setAttribute(String s, Object o) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeAttribute(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCharacterEncoding() {
        return this.encoding;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ProcessorContext getProcessorContext() {
        return this.context;
    }
    @Override
    public void setProcessorContext(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public String toString() {
        String header = "Cbs10Request header {" +
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
            body = "Cbs10Request body {" + new String(this.getRequestBody(),this.encoding) + "}";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return header + "\n" + body;
    }
}

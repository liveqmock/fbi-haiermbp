package org.fbi.ctgserver.util.sbsmsg.domain;

import org.fbi.ctgserver.domain.sbs.form.T531;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * sbs响应报文form
 */
public class SOFForm {
    private static Logger logger = LoggerFactory.getLogger(SOFForm.class);

    public int length;                                      // Form总长度
    public final static int formHeaderLength = 27;          // Form头长度
    public final static int formBodyFieldLength = 2;        // Form体长度域字节数
    public short formBodyLength;                            // Form体长度
    private SOFFormHeader formHeader;                       // Form头
    private SOFFormBody formBody;                           // Form体

    public void marshalMsgToFormBean(int offset, byte[] buffer) {
        // 包头解析
        formHeader = new SOFFormHeader();
        formHeader.marshalMsgToBean(offset, buffer);
        // 包体长度
        byte[] dataLengthBytes = new byte[formBodyFieldLength];
        System.arraycopy(buffer, offset + formHeaderLength, dataLengthBytes, 0, formBodyFieldLength);
        short s1 = (short) (dataLengthBytes[1] & 0xff);
        short s2 = (short) ((dataLengthBytes[0] << 8) & 0xff00);
        formBodyLength = (short) (s2 | s1);
        // 包总长度
        length = formHeaderLength + formBodyFieldLength + formBodyLength;
        // 包体
        logger.info("FormCode:" + formHeader.getFormCode() + " 包体长度：" + formBodyLength);
        if (formBodyLength != 0) {
            try {
                // 实例化Form体
                T531 t531 = new T531();
                String pkgName = t531.getClass().getPackage().getName();
                Class clazz = Class.forName(pkgName + "." + formHeader.getFormCode());
                formBody = (SOFFormBody) clazz.newInstance();
                // 截取Form体字节数据
                byte[] bodyBytes = new byte[formBodyLength];
                System.arraycopy(buffer, offset + formHeaderLength + formBodyFieldLength, bodyBytes, 0, formBodyLength);
                // 装配Form体
                formBody.marshalMsgToBean(0, bodyBytes);
            } catch (Exception e) {
                logger.error("Form解析错误", e);
                throw new RuntimeException("Form解析错误：" + formHeader.getFormCode());
            }
        }
    }

    public byte[] unmarshalFormBeanToMsg() {
        try {
            byte[] header = this.formHeader.unmarshalBeanToMsg().getBytes("GBK");
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.write(header);

            if (this.formBody != null) {
                byte[] body = this.formBody.unmarshalBeanToMsg().getBytes("GBK");
                dos.writeShort(body.length);
                dos.write(body);
            } else {
                dos.writeShort(0);
            }

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("组TOA报文错误：" + formHeader.getFormCode());
        }
    }

    public SOFFormHeader getFormHeader() {
        return formHeader;
    }

    public SOFFormBody getFormBody() {
        return formBody;
    }

}

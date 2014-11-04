package org.fbi.ctgserver.util.sbsmsg.domain;

import org.fbi.ctgserver.domain.sbs.form.T531;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * sbs��Ӧ����form
 */
public class SOFForm {
    private static Logger logger = LoggerFactory.getLogger(SOFForm.class);

    public int length;                                      // Form�ܳ���
    public final static int formHeaderLength = 27;          // Formͷ����
    public final static int formBodyFieldLength = 2;        // Form�峤�����ֽ���
    public short formBodyLength;                            // Form�峤��
    private SOFFormHeader formHeader;                       // Formͷ
    private SOFFormBody formBody;                           // Form��

    public void marshalMsgToFormBean(int offset, byte[] buffer) {
        // ��ͷ����
        formHeader = new SOFFormHeader();
        formHeader.marshalMsgToBean(offset, buffer);
        // ���峤��
        byte[] dataLengthBytes = new byte[formBodyFieldLength];
        System.arraycopy(buffer, offset + formHeaderLength, dataLengthBytes, 0, formBodyFieldLength);
        short s1 = (short) (dataLengthBytes[1] & 0xff);
        short s2 = (short) ((dataLengthBytes[0] << 8) & 0xff00);
        formBodyLength = (short) (s2 | s1);
        // ���ܳ���
        length = formHeaderLength + formBodyFieldLength + formBodyLength;
        // ����
        logger.info("FormCode:" + formHeader.getFormCode() + " ���峤�ȣ�" + formBodyLength);
        if (formBodyLength != 0) {
            try {
                // ʵ����Form��
                T531 t531 = new T531();
                String pkgName = t531.getClass().getPackage().getName();
                Class clazz = Class.forName(pkgName + "." + formHeader.getFormCode());
                formBody = (SOFFormBody) clazz.newInstance();
                // ��ȡForm���ֽ�����
                byte[] bodyBytes = new byte[formBodyLength];
                System.arraycopy(buffer, offset + formHeaderLength + formBodyFieldLength, bodyBytes, 0, formBodyLength);
                // װ��Form��
                formBody.marshalMsgToBean(0, bodyBytes);
            } catch (Exception e) {
                logger.error("Form��������", e);
                throw new RuntimeException("Form��������" + formHeader.getFormCode());
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
            throw new RuntimeException("��TOA���Ĵ���" + formHeader.getFormCode());
        }
    }

    public SOFFormHeader getFormHeader() {
        return formHeader;
    }

    public SOFFormBody getFormBody() {
        return formBody;
    }

}

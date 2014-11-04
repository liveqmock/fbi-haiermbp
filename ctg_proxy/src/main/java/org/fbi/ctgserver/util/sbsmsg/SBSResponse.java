package org.fbi.ctgserver.util.sbsmsg;


import org.fbi.ctgserver.util.sbsmsg.domain.SOFForm;

import java.util.ArrayList;
import java.util.List;

/**
 * SBS��Ӧ
 */
public class SBSResponse {
    protected List<String> formCodes = new ArrayList<>();
    protected List<SOFForm> sofForms = new ArrayList<>();

    public List<String> getFormCodes() {
        return formCodes;
    }

    public List<SOFForm> getSofForms() {
        return sofForms;
    }

    // ��������
    public void init(byte[] buffer) {
        int index = 0;
        do {
            // �������Կո�ͷ������Ϊ�հ���������
            if (buffer[index] != 0x20) {
                return;
            }
            SOFForm sofForm = new SOFForm();
            sofForm.marshalMsgToFormBean(index, buffer);
            formCodes.add(sofForm.getFormHeader().getFormCode());
            sofForms.add(sofForm);
            index += sofForm.length;
        } while (buffer.length > index);
    }
}

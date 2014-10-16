package org.fbi.mbp.proxy.domain.ccbvip.t4868response;

import org.fbi.mbp.proxy.domain.ccbvip.CcbvipMsgRoot;

import javax.xml.bind.annotation.*;

/**
 * Created by zhanrui on 2014/10/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "head",
        "body"
})
@XmlRootElement(name = "Root")
public class CcbvipT4868ResponseRoot extends CcbvipMsgRoot {
    @XmlElement(name = "Body", required = true)
    protected CcbvipT4868ResponseRoot body;

    public CcbvipT4868ResponseRoot getBody() {
        return body;
    }

    public void setBody(CcbvipT4868ResponseRoot body) {
        this.body = body;
    }
}

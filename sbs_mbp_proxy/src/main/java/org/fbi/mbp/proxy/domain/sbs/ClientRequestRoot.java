package org.fbi.mbp.proxy.domain.sbs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class ClientRequestRoot {

    @XmlElement(name = "Head", required = true)
    protected ClientReqestHead head;

    /**
     * Gets the value of the head property.
     * 
     * @return
     *     possible object is
     *     {@link ClientReqestHead }
     *     
     */
    public ClientReqestHead getHead() {
        return head;
    }

    /**
     * Sets the value of the head property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClientReqestHead }
     *     
     */
    public void setHead(ClientReqestHead value) {
        this.head = value;
    }

}

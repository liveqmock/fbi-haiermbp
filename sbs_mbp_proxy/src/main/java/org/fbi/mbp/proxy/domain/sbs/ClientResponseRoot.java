package org.fbi.mbp.proxy.domain.sbs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class ClientResponseRoot {

    @XmlElement(name = "Head", required = true)
    protected ClientResponseHead head;

    /**
     * Gets the value of the head property.
     *
     * @return
     *     possible object is
     *     {@link ClientRequestHead }
     *
     */
    public ClientResponseHead getHead() {
        return head;
    }

    /**
     * Sets the value of the head property.
     *
     * @param value
     *     allowed object is
     *     {@link ClientRequestHead }
     *     
     */
    public void setHead(ClientResponseHead value) {
        this.head = value;
    }

}

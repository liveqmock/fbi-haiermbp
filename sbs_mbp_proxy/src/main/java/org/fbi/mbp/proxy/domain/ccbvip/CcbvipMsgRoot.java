//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.11 at 11:36:32 AM CST 
//


package org.fbi.mbp.proxy.domain.ccbvip;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}Head"/>
 *         &lt;element ref="{}Body"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

@XmlTransient
public class CcbvipMsgRoot {

    @XmlElement(name = "Head", required = true)
    protected CcbvipMsgHead head;

    /**
     * Gets the value of the head property.
     * 
     * @return
     *     possible object is
     *     {@link CcbvipMsgHead }
     *     
     */
    public CcbvipMsgHead getHead() {
        return head;
    }

    /**
     * Sets the value of the head property.
     * 
     * @param value
     *     allowed object is
     *     {@link CcbvipMsgHead }
     *     
     */
    public void setHead(CcbvipMsgHead value) {
        this.head = value;
    }

}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.10 at 09:34:41 PM CST 
//


package org.fbi.mbp.proxy.domain.sbs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{}OpBankCode"/>
 *         &lt;element ref="{}OpName"/>
 *         &lt;element ref="{}OpEntID"/>
 *         &lt;element ref="{}OpDate"/>
 *         &lt;element ref="{}OpID"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "opBankCode",
    "opName",
    "opEntID",
    "opDate",
    "opID"
})
@XmlRootElement(name = "Head")
public class ClientRequestHead {

    @XmlElement(name = "OpBankCode", required = true)
    protected String opBankCode="";
    @XmlElement(name = "OpName", required = true)
    protected String opName="";
    @XmlElement(name = "OpEntID", required = true)
    protected String opEntID="";
    @XmlElement(name = "OpDate", required = true)
    protected String opDate="";
    @XmlElement(name = "OpID", required = true)
    protected String opID="";

    /**
     * Gets the value of the opBankCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpBankCode() {
        return opBankCode;
    }

    /**
     * Sets the value of the opBankCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpBankCode(String value) {
        this.opBankCode = value;
    }

    /**
     * Gets the value of the opName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpName() {
        return opName;
    }

    /**
     * Sets the value of the opName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpName(String value) {
        this.opName = value;
    }

    /**
     * Gets the value of the opEntID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpEntID() {
        return opEntID;
    }

    /**
     * Sets the value of the opEntID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpEntID(String value) {
        this.opEntID = value;
    }

    /**
     * Gets the value of the opDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpDate() {
        return opDate;
    }

    /**
     * Sets the value of the opDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpDate(String value) {
        this.opDate = value;
    }

    /**
     * Gets the value of the opID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpID() {
        return opID;
    }

    /**
     * Sets the value of the opID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpID(String value) {
        this.opID = value;
    }

    @Override
    public String toString() {
        return "Head{" +
                "opBankCode='" + opBankCode + '\'' +
                ", opName='" + opName + '\'' +
                ", opEntID='" + opEntID + '\'' +
                ", opDate='" + opDate + '\'' +
                ", opID='" + opID + '\'' +
                '}';
    }
}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.10 at 09:34:41 PM CST 
//


package org.fbi.mbp.proxy.domain.sbs.transactrequest;

import org.fbi.mbp.proxy.domain.sbs.ClientReqestParam;

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
 *         &lt;element ref="{}ToAccount"/>
 *         &lt;element ref="{}ToName"/>
 *         &lt;element ref="{}ToBank"/>
 *         &lt;element ref="{}ToReserved1"/>
 *         &lt;element ref="{}ToReserved2"/>
 *         &lt;element ref="{}ToReserved3"/>
 *         &lt;element ref="{}ToReserved4"/>
 *         &lt;element ref="{}FromAccount"/>
 *         &lt;element ref="{}FromName"/>
 *         &lt;element ref="{}FromBank"/>
 *         &lt;element ref="{}FromReserved1"/>
 *         &lt;element ref="{}FromReserved2"/>
 *         &lt;element ref="{}FromReserved3"/>
 *         &lt;element ref="{}FromReserved4"/>
 *         &lt;element ref="{}EnterpriseSerial"/>
 *         &lt;element ref="{}VoucherNum"/>
 *         &lt;element ref="{}Amount"/>
 *         &lt;element ref="{}Currency"/>
 *         &lt;element ref="{}Usage"/>
 *         &lt;element ref="{}TransDate"/>
 *         &lt;element ref="{}System"/>
 *         &lt;element ref="{}Bank"/>
 *         &lt;element ref="{}Local"/>
 *         &lt;element ref="{}Internal"/>
 *         &lt;element ref="{}Public"/>
 *         &lt;element ref="{}Speed"/>
 *         &lt;element ref="{}Reserved1"/>
 *         &lt;element ref="{}Reserved2"/>
 *         &lt;element ref="{}Reserved3"/>
 *         &lt;element ref="{}Reserved4"/>
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
    "toAccount",
    "toName",
    "toBank",
    "toReserved1",
    "toReserved2",
    "toReserved3",
    "toReserved4",
    "fromAccount",
    "fromName",
    "fromBank",
    "fromReserved1",
    "fromReserved2",
    "fromReserved3",
    "fromReserved4",
    "enterpriseSerial",
    "voucherNum",
    "amount",
    "currency",
    "usage",
    "transDate",
    "system",
    "bank",
    "local",
    "internal",
    "_public",
    "speed",
    "reserved1",
    "reserved2",
    "reserved3",
    "reserved4"
})
@XmlRootElement(name = "Param")
public class TransactParam extends ClientReqestParam {

    @XmlElement(name = "ToAccount", required = true)
    protected String toAccount;
    @XmlElement(name = "ToName", required = true)
    protected String toName;
    @XmlElement(name = "ToBank", required = true)
    protected String toBank;
    @XmlElement(name = "ToReserved1", required = true)
    protected String toReserved1;
    @XmlElement(name = "ToReserved2", required = true)
    protected String toReserved2;
    @XmlElement(name = "ToReserved3", required = true)
    protected String toReserved3;
    @XmlElement(name = "ToReserved4", required = true)
    protected String toReserved4;
    @XmlElement(name = "FromAccount", required = true)
    protected String fromAccount;
    @XmlElement(name = "FromName", required = true)
    protected String fromName;
    @XmlElement(name = "FromBank", required = true)
    protected String fromBank;
    @XmlElement(name = "FromReserved1", required = true)
    protected String fromReserved1;
    @XmlElement(name = "FromReserved2", required = true)
    protected String fromReserved2;
    @XmlElement(name = "FromReserved3", required = true)
    protected String fromReserved3;
    @XmlElement(name = "FromReserved4", required = true)
    protected String fromReserved4;
    @XmlElement(name = "EnterpriseSerial", required = true)
    protected String enterpriseSerial;
    @XmlElement(name = "VoucherNum", required = true)
    protected String voucherNum;
    @XmlElement(name = "Amount", required = true)
    protected String amount;
    @XmlElement(name = "Currency", required = true)
    protected String currency;
    @XmlElement(name = "Usage", required = true)
    protected String usage;
    @XmlElement(name = "TransDate", required = true)
    protected String transDate;
    @XmlElement(name = "System", required = true)
    protected String system;
    @XmlElement(name = "Bank", required = true)
    protected String bank;
    @XmlElement(name = "Local", required = true)
    protected String local;
    @XmlElement(name = "Internal", required = true)
    protected String internal;
    @XmlElement(name = "Public", required = true)
    protected String _public;
    @XmlElement(name = "Speed", required = true)
    protected String speed;
    @XmlElement(name = "Reserved1", required = true)
    protected String reserved1;
    @XmlElement(name = "Reserved2", required = true)
    protected String reserved2;
    @XmlElement(name = "Reserved3", required = true)
    protected String reserved3;
    @XmlElement(name = "Reserved4", required = true)
    protected String reserved4;

    /**
     * Gets the value of the toAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToAccount() {
        return toAccount;
    }

    /**
     * Sets the value of the toAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToAccount(String value) {
        this.toAccount = value;
    }

    /**
     * Gets the value of the toName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToName() {
        return toName;
    }

    /**
     * Sets the value of the toName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToName(String value) {
        this.toName = value;
    }

    /**
     * Gets the value of the toBank property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToBank() {
        return toBank;
    }

    /**
     * Sets the value of the toBank property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToBank(String value) {
        this.toBank = value;
    }

    /**
     * Gets the value of the toReserved1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToReserved1() {
        return toReserved1;
    }

    /**
     * Sets the value of the toReserved1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToReserved1(String value) {
        this.toReserved1 = value;
    }

    /**
     * Gets the value of the toReserved2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToReserved2() {
        return toReserved2;
    }

    /**
     * Sets the value of the toReserved2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToReserved2(String value) {
        this.toReserved2 = value;
    }

    /**
     * Gets the value of the toReserved3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToReserved3() {
        return toReserved3;
    }

    /**
     * Sets the value of the toReserved3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToReserved3(String value) {
        this.toReserved3 = value;
    }

    /**
     * Gets the value of the toReserved4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToReserved4() {
        return toReserved4;
    }

    /**
     * Sets the value of the toReserved4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToReserved4(String value) {
        this.toReserved4 = value;
    }

    /**
     * Gets the value of the fromAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromAccount() {
        return fromAccount;
    }

    /**
     * Sets the value of the fromAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromAccount(String value) {
        this.fromAccount = value;
    }

    /**
     * Gets the value of the fromName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromName() {
        return fromName;
    }

    /**
     * Sets the value of the fromName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromName(String value) {
        this.fromName = value;
    }

    /**
     * Gets the value of the fromBank property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromBank() {
        return fromBank;
    }

    /**
     * Sets the value of the fromBank property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromBank(String value) {
        this.fromBank = value;
    }

    /**
     * Gets the value of the fromReserved1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromReserved1() {
        return fromReserved1;
    }

    /**
     * Sets the value of the fromReserved1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromReserved1(String value) {
        this.fromReserved1 = value;
    }

    /**
     * Gets the value of the fromReserved2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromReserved2() {
        return fromReserved2;
    }

    /**
     * Sets the value of the fromReserved2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromReserved2(String value) {
        this.fromReserved2 = value;
    }

    /**
     * Gets the value of the fromReserved3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromReserved3() {
        return fromReserved3;
    }

    /**
     * Sets the value of the fromReserved3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromReserved3(String value) {
        this.fromReserved3 = value;
    }

    /**
     * Gets the value of the fromReserved4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromReserved4() {
        return fromReserved4;
    }

    /**
     * Sets the value of the fromReserved4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromReserved4(String value) {
        this.fromReserved4 = value;
    }

    /**
     * Gets the value of the enterpriseSerial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnterpriseSerial() {
        return enterpriseSerial;
    }

    /**
     * Sets the value of the enterpriseSerial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnterpriseSerial(String value) {
        this.enterpriseSerial = value;
    }

    /**
     * Gets the value of the voucherNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVoucherNum() {
        return voucherNum;
    }

    /**
     * Sets the value of the voucherNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVoucherNum(String value) {
        this.voucherNum = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmount(String value) {
        this.amount = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the usage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Sets the value of the usage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsage(String value) {
        this.usage = value;
    }

    /**
     * Gets the value of the transDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransDate() {
        return transDate;
    }

    /**
     * Sets the value of the transDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransDate(String value) {
        this.transDate = value;
    }

    /**
     * Gets the value of the system property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystem() {
        return system;
    }

    /**
     * Sets the value of the system property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystem(String value) {
        this.system = value;
    }

    /**
     * Gets the value of the bank property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBank() {
        return bank;
    }

    /**
     * Sets the value of the bank property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBank(String value) {
        this.bank = value;
    }

    /**
     * Gets the value of the local property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocal() {
        return local;
    }

    /**
     * Sets the value of the local property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocal(String value) {
        this.local = value;
    }

    /**
     * Gets the value of the internal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternal() {
        return internal;
    }

    /**
     * Sets the value of the internal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternal(String value) {
        this.internal = value;
    }

    /**
     * Gets the value of the public property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublic() {
        return _public;
    }

    /**
     * Sets the value of the public property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublic(String value) {
        this._public = value;
    }

    /**
     * Gets the value of the speed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * Sets the value of the speed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpeed(String value) {
        this.speed = value;
    }

    /**
     * Gets the value of the reserved1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReserved1() {
        return reserved1;
    }

    /**
     * Sets the value of the reserved1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReserved1(String value) {
        this.reserved1 = value;
    }

    /**
     * Gets the value of the reserved2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReserved2() {
        return reserved2;
    }

    /**
     * Sets the value of the reserved2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReserved2(String value) {
        this.reserved2 = value;
    }

    /**
     * Gets the value of the reserved3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReserved3() {
        return reserved3;
    }

    /**
     * Sets the value of the reserved3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReserved3(String value) {
        this.reserved3 = value;
    }

    /**
     * Gets the value of the reserved4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReserved4() {
        return reserved4;
    }

    /**
     * Sets the value of the reserved4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReserved4(String value) {
        this.reserved4 = value;
    }

    @Override
    public String toString() {
        return "Param{" +
                "toAccount='" + toAccount + '\'' +
                ", toName='" + toName + '\'' +
                ", toBank='" + toBank + '\'' +
                ", toReserved1='" + toReserved1 + '\'' +
                ", toReserved2='" + toReserved2 + '\'' +
                ", toReserved3='" + toReserved3 + '\'' +
                ", toReserved4='" + toReserved4 + '\'' +
                ", fromAccount='" + fromAccount + '\'' +
                ", fromName='" + fromName + '\'' +
                ", fromBank='" + fromBank + '\'' +
                ", fromReserved1='" + fromReserved1 + '\'' +
                ", fromReserved2='" + fromReserved2 + '\'' +
                ", fromReserved3='" + fromReserved3 + '\'' +
                ", fromReserved4='" + fromReserved4 + '\'' +
                ", enterpriseSerial='" + enterpriseSerial + '\'' +
                ", voucherNum='" + voucherNum + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", usage='" + usage + '\'' +
                ", transDate='" + transDate + '\'' +
                ", system='" + system + '\'' +
                ", bank='" + bank + '\'' +
                ", local='" + local + '\'' +
                ", internal='" + internal + '\'' +
                ", _public='" + _public + '\'' +
                ", speed='" + speed + '\'' +
                ", reserved1='" + reserved1 + '\'' +
                ", reserved2='" + reserved2 + '\'' +
                ", reserved3='" + reserved3 + '\'' +
                ", reserved4='" + reserved4 + '\'' +
                '}';
    }
}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.17 at 11:06:37 AM CET 
//


package hr.zaba.session;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CheckAuthIdRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CheckAuthIdRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="auth_id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dest_app" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CheckAuthIdRequestType", propOrder = {
    "authId",
    "destApp"
})
public class CheckAuthIdRequestType {

    @XmlElement(name = "auth_id", required = true)
    protected String authId;
    @XmlElement(name = "dest_app", required = true)
    protected String destApp;

    /**
     * Gets the value of the authId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthId() {
        return authId;
    }

    /**
     * Sets the value of the authId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthId(String value) {
        this.authId = value;
    }

    /**
     * Gets the value of the destApp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestApp() {
        return destApp;
    }

    /**
     * Sets the value of the destApp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestApp(String value) {
        this.destApp = value;
    }

}
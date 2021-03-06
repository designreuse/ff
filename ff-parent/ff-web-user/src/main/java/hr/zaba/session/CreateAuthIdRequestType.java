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
 * <p>Java class for CreateAuthIdRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateAuthIdRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="auth_id_type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="user_id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="user_id2" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="token_sn" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="token_app" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="src_app" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dest_app" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="params" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateAuthIdRequestType", propOrder = {
    "authIdType",
    "userId",
    "userId2",
    "tokenSn",
    "tokenApp",
    "srcApp",
    "destApp",
    "params"
})
public class CreateAuthIdRequestType {

    @XmlElement(name = "auth_id_type", required = true)
    protected String authIdType;
    @XmlElement(name = "user_id", required = true)
    protected String userId;
    @XmlElement(name = "user_id2", required = true)
    protected String userId2;
    @XmlElement(name = "token_sn", required = true)
    protected String tokenSn;
    @XmlElement(name = "token_app", required = true)
    protected String tokenApp;
    @XmlElement(name = "src_app", required = true)
    protected String srcApp;
    @XmlElement(name = "dest_app", required = true)
    protected String destApp;
    @XmlElement(required = true)
    protected String params;

    /**
     * Gets the value of the authIdType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthIdType() {
        return authIdType;
    }

    /**
     * Sets the value of the authIdType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthIdType(String value) {
        this.authIdType = value;
    }

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Gets the value of the userId2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId2() {
        return userId2;
    }

    /**
     * Sets the value of the userId2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId2(String value) {
        this.userId2 = value;
    }

    /**
     * Gets the value of the tokenSn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTokenSn() {
        return tokenSn;
    }

    /**
     * Sets the value of the tokenSn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTokenSn(String value) {
        this.tokenSn = value;
    }

    /**
     * Gets the value of the tokenApp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTokenApp() {
        return tokenApp;
    }

    /**
     * Sets the value of the tokenApp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTokenApp(String value) {
        this.tokenApp = value;
    }

    /**
     * Gets the value of the srcApp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrcApp() {
        return srcApp;
    }

    /**
     * Sets the value of the srcApp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrcApp(String value) {
        this.srcApp = value;
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

    /**
     * Gets the value of the params property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParams() {
        return params;
    }

    /**
     * Sets the value of the params property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParams(String value) {
        this.params = value;
    }

}

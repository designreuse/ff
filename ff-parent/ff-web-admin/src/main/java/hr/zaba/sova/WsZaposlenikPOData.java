//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.11 at 05:05:40 PM CET 
//


package hr.zaba.sova;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wsZaposlenikPOData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wsZaposlenikPOData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="brojRadnika" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="iznosPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="kombinacijaPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sifraPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="iDPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsZaposlenikPOData", propOrder = {
    "brojRadnika",
    "iznosPO",
    "kombinacijaPO",
    "sifraPO",
    "idpo"
})
public class WsZaposlenikPOData {

    protected String brojRadnika;
    protected String iznosPO;
    protected String kombinacijaPO;
    protected String sifraPO;
    @XmlElement(name = "iDPO")
    protected String idpo;

    /**
     * Gets the value of the brojRadnika property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBrojRadnika() {
        return brojRadnika;
    }

    /**
     * Sets the value of the brojRadnika property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBrojRadnika(String value) {
        this.brojRadnika = value;
    }

    /**
     * Gets the value of the iznosPO property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIznosPO() {
        return iznosPO;
    }

    /**
     * Sets the value of the iznosPO property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIznosPO(String value) {
        this.iznosPO = value;
    }

    /**
     * Gets the value of the kombinacijaPO property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKombinacijaPO() {
        return kombinacijaPO;
    }

    /**
     * Sets the value of the kombinacijaPO property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKombinacijaPO(String value) {
        this.kombinacijaPO = value;
    }

    /**
     * Gets the value of the sifraPO property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSifraPO() {
        return sifraPO;
    }

    /**
     * Sets the value of the sifraPO property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSifraPO(String value) {
        this.sifraPO = value;
    }

    /**
     * Gets the value of the idpo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDPO() {
        return idpo;
    }

    /**
     * Sets the value of the idpo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDPO(String value) {
        this.idpo = value;
    }

}

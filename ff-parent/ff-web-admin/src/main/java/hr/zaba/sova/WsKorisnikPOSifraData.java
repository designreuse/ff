//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.10 at 09:44:29 PM CET 
//


package hr.zaba.sova;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wsKorisnikPOSifraData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wsKorisnikPOSifraData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="funkcija" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idpo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="iznosPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="putanja" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sifraPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zamjenjuje" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsKorisnikPOSifraData", propOrder = {
    "funkcija",
    "idpo",
    "iznosPO",
    "putanja",
    "sifraPO",
    "tipPO",
    "zamjenjuje"
})
public class WsKorisnikPOSifraData {

    protected String funkcija;
    protected String idpo;
    protected String iznosPO;
    protected String putanja;
    protected String sifraPO;
    protected String tipPO;
    protected String zamjenjuje;

    /**
     * Gets the value of the funkcija property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunkcija() {
        return funkcija;
    }

    /**
     * Sets the value of the funkcija property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunkcija(String value) {
        this.funkcija = value;
    }

    /**
     * Gets the value of the idpo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdpo() {
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
    public void setIdpo(String value) {
        this.idpo = value;
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
     * Gets the value of the putanja property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPutanja() {
        return putanja;
    }

    /**
     * Sets the value of the putanja property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPutanja(String value) {
        this.putanja = value;
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
     * Gets the value of the tipPO property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipPO() {
        return tipPO;
    }

    /**
     * Sets the value of the tipPO property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipPO(String value) {
        this.tipPO = value;
    }

    /**
     * Gets the value of the zamjenjuje property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZamjenjuje() {
        return zamjenjuje;
    }

    /**
     * Sets the value of the zamjenjuje property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZamjenjuje(String value) {
        this.zamjenjuje = value;
    }

}

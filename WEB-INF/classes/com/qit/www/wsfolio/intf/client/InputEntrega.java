/**
 * InputEntrega.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.qit.www.wsfolio.intf.client;

public class InputEntrega  implements java.io.Serializable {
    private java.lang.String codPlaza;

    private java.lang.String codOficina;

    private java.lang.String tipoFac;

    private java.lang.String serieFac;

    private java.lang.String dispositivo;

    private java.lang.String puntoVenta;

    private java.lang.String usuario;

    public InputEntrega() {
    }

    public InputEntrega(
           java.lang.String codPlaza,
           java.lang.String codOficina,
           java.lang.String tipoFac,
           java.lang.String serieFac,
           java.lang.String dispositivo,
           java.lang.String puntoVenta,
           java.lang.String usuario) {
           this.codPlaza = codPlaza;
           this.codOficina = codOficina;
           this.tipoFac = tipoFac;
           this.serieFac = serieFac;
           this.dispositivo = dispositivo;
           this.puntoVenta = puntoVenta;
           this.usuario = usuario;
    }


    /**
     * Gets the codPlaza value for this InputEntrega.
     * 
     * @return codPlaza
     */
    public java.lang.String getCodPlaza() {
        return codPlaza;
    }


    /**
     * Sets the codPlaza value for this InputEntrega.
     * 
     * @param codPlaza
     */
    public void setCodPlaza(java.lang.String codPlaza) {
        this.codPlaza = codPlaza;
    }


    /**
     * Gets the codOficina value for this InputEntrega.
     * 
     * @return codOficina
     */
    public java.lang.String getCodOficina() {
        return codOficina;
    }


    /**
     * Sets the codOficina value for this InputEntrega.
     * 
     * @param codOficina
     */
    public void setCodOficina(java.lang.String codOficina) {
        this.codOficina = codOficina;
    }


    /**
     * Gets the tipoFac value for this InputEntrega.
     * 
     * @return tipoFac
     */
    public java.lang.String getTipoFac() {
        return tipoFac;
    }


    /**
     * Sets the tipoFac value for this InputEntrega.
     * 
     * @param tipoFac
     */
    public void setTipoFac(java.lang.String tipoFac) {
        this.tipoFac = tipoFac;
    }


    /**
     * Gets the serieFac value for this InputEntrega.
     * 
     * @return serieFac
     */
    public java.lang.String getSerieFac() {
        return serieFac;
    }


    /**
     * Sets the serieFac value for this InputEntrega.
     * 
     * @param serieFac
     */
    public void setSerieFac(java.lang.String serieFac) {
        this.serieFac = serieFac;
    }


    /**
     * Gets the dispositivo value for this InputEntrega.
     * 
     * @return dispositivo
     */
    public java.lang.String getDispositivo() {
        return dispositivo;
    }


    /**
     * Sets the dispositivo value for this InputEntrega.
     * 
     * @param dispositivo
     */
    public void setDispositivo(java.lang.String dispositivo) {
        this.dispositivo = dispositivo;
    }


    /**
     * Gets the puntoVenta value for this InputEntrega.
     * 
     * @return puntoVenta
     */
    public java.lang.String getPuntoVenta() {
        return puntoVenta;
    }


    /**
     * Sets the puntoVenta value for this InputEntrega.
     * 
     * @param puntoVenta
     */
    public void setPuntoVenta(java.lang.String puntoVenta) {
        this.puntoVenta = puntoVenta;
    }


    /**
     * Gets the usuario value for this InputEntrega.
     * 
     * @return usuario
     */
    public java.lang.String getUsuario() {
        return usuario;
    }


    /**
     * Sets the usuario value for this InputEntrega.
     * 
     * @param usuario
     */
    public void setUsuario(java.lang.String usuario) {
        this.usuario = usuario;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InputEntrega)) return false;
        InputEntrega other = (InputEntrega) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codPlaza==null && other.getCodPlaza()==null) || 
             (this.codPlaza!=null &&
              this.codPlaza.equals(other.getCodPlaza()))) &&
            ((this.codOficina==null && other.getCodOficina()==null) || 
             (this.codOficina!=null &&
              this.codOficina.equals(other.getCodOficina()))) &&
            ((this.tipoFac==null && other.getTipoFac()==null) || 
             (this.tipoFac!=null &&
              this.tipoFac.equals(other.getTipoFac()))) &&
            ((this.serieFac==null && other.getSerieFac()==null) || 
             (this.serieFac!=null &&
              this.serieFac.equals(other.getSerieFac()))) &&
            ((this.dispositivo==null && other.getDispositivo()==null) || 
             (this.dispositivo!=null &&
              this.dispositivo.equals(other.getDispositivo()))) &&
            ((this.puntoVenta==null && other.getPuntoVenta()==null) || 
             (this.puntoVenta!=null &&
              this.puntoVenta.equals(other.getPuntoVenta()))) &&
            ((this.usuario==null && other.getUsuario()==null) || 
             (this.usuario!=null &&
              this.usuario.equals(other.getUsuario())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCodPlaza() != null) {
            _hashCode += getCodPlaza().hashCode();
        }
        if (getCodOficina() != null) {
            _hashCode += getCodOficina().hashCode();
        }
        if (getTipoFac() != null) {
            _hashCode += getTipoFac().hashCode();
        }
        if (getSerieFac() != null) {
            _hashCode += getSerieFac().hashCode();
        }
        if (getDispositivo() != null) {
            _hashCode += getDispositivo().hashCode();
        }
        if (getPuntoVenta() != null) {
            _hashCode += getPuntoVenta().hashCode();
        }
        if (getUsuario() != null) {
            _hashCode += getUsuario().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InputEntrega.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qit.com/wsfolio/intf/client", "inputEntrega"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codPlaza");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qit.com/wsfolio/intf/client", "codPlaza"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codOficina");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qit.com/wsfolio/intf/client", "codOficina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoFac");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qit.com/wsfolio/intf/client", "tipoFac"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serieFac");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qit.com/wsfolio/intf/client", "serieFac"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dispositivo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qit.com/wsfolio/intf/client", "dispositivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("puntoVenta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qit.com/wsfolio/intf/client", "puntoVenta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usuario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qit.com/wsfolio/intf/client", "usuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

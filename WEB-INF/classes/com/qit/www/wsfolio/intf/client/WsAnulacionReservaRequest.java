/**
 * WsAnulacionReservaRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.qit.www.wsfolio.intf.client;

public class WsAnulacionReservaRequest  implements java.io.Serializable {
    private com.qit.www.wsfolio.intf.client.InputAnulacion inputAnulacion;

    public WsAnulacionReservaRequest() {
    }

    public WsAnulacionReservaRequest(
           com.qit.www.wsfolio.intf.client.InputAnulacion inputAnulacion) {
           this.inputAnulacion = inputAnulacion;
    }


    /**
     * Gets the inputAnulacion value for this WsAnulacionReservaRequest.
     * 
     * @return inputAnulacion
     */
    public com.qit.www.wsfolio.intf.client.InputAnulacion getInputAnulacion() {
        return inputAnulacion;
    }


    /**
     * Sets the inputAnulacion value for this WsAnulacionReservaRequest.
     * 
     * @param inputAnulacion
     */
    public void setInputAnulacion(com.qit.www.wsfolio.intf.client.InputAnulacion inputAnulacion) {
        this.inputAnulacion = inputAnulacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsAnulacionReservaRequest)) return false;
        WsAnulacionReservaRequest other = (WsAnulacionReservaRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.inputAnulacion==null && other.getInputAnulacion()==null) || 
             (this.inputAnulacion!=null &&
              this.inputAnulacion.equals(other.getInputAnulacion())));
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
        if (getInputAnulacion() != null) {
            _hashCode += getInputAnulacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WsAnulacionReservaRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qit.com/wsfolio/intf/client", ">WsAnulacionReservaRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputAnulacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qit.com/wsfolio/intf/client", "inputAnulacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.qit.com/wsfolio/intf/client", "inputAnulacion"));
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

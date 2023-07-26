/**
 * WsFolioSoap11QSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.qit.www.wsfolio.intf.client;

public class WsFolioSoap11QSServiceLocator extends org.apache.axis.client.Service implements com.qit.www.wsfolio.intf.client.WsFolioSoap11QSService {

/**
 * OSB Service
 */

    public WsFolioSoap11QSServiceLocator() {
    }


    public WsFolioSoap11QSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WsFolioSoap11QSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WsFolioSoap11QSPort
    private java.lang.String WsFolioSoap11QSPort_address = "http://10.225.170.209:7003/wsfolio/proxys/WsFolioProxy";

    public java.lang.String getWsFolioSoap11QSPortAddress() {
        return WsFolioSoap11QSPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WsFolioSoap11QSPortWSDDServiceName = "WsFolioSoap11QSPort";

    public java.lang.String getWsFolioSoap11QSPortWSDDServiceName() {
        return WsFolioSoap11QSPortWSDDServiceName;
    }

    public void setWsFolioSoap11QSPortWSDDServiceName(java.lang.String name) {
        WsFolioSoap11QSPortWSDDServiceName = name;
    }

    public com.qit.www.wsfolio.intf.client.WsFolio getWsFolioSoap11QSPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WsFolioSoap11QSPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWsFolioSoap11QSPort(endpoint);
    }

    public com.qit.www.wsfolio.intf.client.WsFolio getWsFolioSoap11QSPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.qit.www.wsfolio.intf.client.WsFolioSoap11Stub _stub = new com.qit.www.wsfolio.intf.client.WsFolioSoap11Stub(portAddress, this);
            _stub.setPortName(getWsFolioSoap11QSPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWsFolioSoap11QSPortEndpointAddress(java.lang.String address) {
        WsFolioSoap11QSPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.qit.www.wsfolio.intf.client.WsFolio.class.isAssignableFrom(serviceEndpointInterface)) {
                com.qit.www.wsfolio.intf.client.WsFolioSoap11Stub _stub = new com.qit.www.wsfolio.intf.client.WsFolioSoap11Stub(new java.net.URL(WsFolioSoap11QSPort_address), this);
                _stub.setPortName(getWsFolioSoap11QSPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WsFolioSoap11QSPort".equals(inputPortName)) {
            return getWsFolioSoap11QSPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.qit.com/wsfolio/intf/client", "WsFolioSoap11QSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.qit.com/wsfolio/intf/client", "WsFolioSoap11QSPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WsFolioSoap11QSPort".equals(portName)) {
            setWsFolioSoap11QSPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

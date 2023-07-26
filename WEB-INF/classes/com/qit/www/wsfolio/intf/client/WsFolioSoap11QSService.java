/**
 * WsFolioSoap11QSService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.qit.www.wsfolio.intf.client;

public interface WsFolioSoap11QSService extends javax.xml.rpc.Service {

/**
 * OSB Service
 */
    public java.lang.String getWsFolioSoap11QSPortAddress();

    public com.qit.www.wsfolio.intf.client.WsFolio getWsFolioSoap11QSPort() throws javax.xml.rpc.ServiceException;

    public com.qit.www.wsfolio.intf.client.WsFolio getWsFolioSoap11QSPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

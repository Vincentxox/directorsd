package com.qit.www.wsfolio.intf.client;

public class WsFolioProxy implements com.qit.www.wsfolio.intf.client.WsFolio {
  private String _endpoint = null;
  private com.qit.www.wsfolio.intf.client.WsFolio wsFolio = null;
  
  public WsFolioProxy() {
    _initWsFolioProxy();
  }
  
  public WsFolioProxy(String endpoint) {
    _endpoint = endpoint;
    _initWsFolioProxy();
  }
  
  private void _initWsFolioProxy() {
    try {
      wsFolio = (new com.qit.www.wsfolio.intf.client.WsFolioSoap11QSServiceLocator()).getWsFolioSoap11QSPort();
      if (wsFolio != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wsFolio)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wsFolio)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wsFolio != null)
      ((javax.xml.rpc.Stub)wsFolio)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.qit.www.wsfolio.intf.client.WsFolio getWsFolio() {
    if (wsFolio == null)
      _initWsFolioProxy();
    return wsFolio;
  }
  
  public com.qit.www.wsfolio.intf.client.WsAnulacionReservaResponse wsAnulacionReserva(com.qit.www.wsfolio.intf.client.WsAnulacionReservaRequest wsAnulacionReservaRequest) throws java.rmi.RemoteException{
    if (wsFolio == null)
      _initWsFolioProxy();
    return wsFolio.wsAnulacionReserva(wsAnulacionReservaRequest);
  }
  
  public com.qit.www.wsfolio.intf.client.WsCreacionReservaResponse wsCreacionReserva(com.qit.www.wsfolio.intf.client.WsCreacionReservaRequest wsCreacionReservaRequest) throws java.rmi.RemoteException{
    if (wsFolio == null)
      _initWsFolioProxy();
    return wsFolio.wsCreacionReserva(wsCreacionReservaRequest);
  }
  
  public com.qit.www.wsfolio.intf.client.WsEntregaFolioResponse wsEntregaFolio(com.qit.www.wsfolio.intf.client.WsEntregaFolioRequest wsEntregaFolioRequest) throws java.rmi.RemoteException{
    if (wsFolio == null)
      _initWsFolioProxy();
    return wsFolio.wsEntregaFolio(wsEntregaFolioRequest);
  }
  
  
}
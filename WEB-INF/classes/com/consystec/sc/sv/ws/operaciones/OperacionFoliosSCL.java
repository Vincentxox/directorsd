package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Holder;

import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.folio.InputFolioVirtual;
import com.consystec.sc.ca.ws.input.general.InputFolioRutaPanel;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.folio.OutputConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.FSUtil;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.google.gson.GsonBuilder;
import com.qit.www.wsfolio.intf.client.InputCreacion;
import com.qit.www.wsfolio.intf.client.WsCreacionReservaRequest;
import com.qit.www.wsfolio.intf.client.WsCreacionReservaResponse;
import com.qit.www.wsfolio.intf.client.WsFolioProxy;
import com.telefonica.globalintegration.header.HeaderInType;
import com.telefonica.globalintegration.header.HeaderOutType;
import com.telefonica.globalintegration.services.customerbillnumberreservation.v1.CustomerBillNumberReservationRequestType;
import com.telefonica.globalintegration.services.customerbillnumberreservation.v1.CustomerBillNumberReservationResponseType;
import com.telefonica.globalintegration.services.soap.customerbillnumberreservation.v1.CustomerBillNumberReservationBindingQSService;
import com.telefonica.globalintegration.services.soap.customerbillnumberreservation.v1.CustomerBillNumberReservationV1;
import com.telefonica.globalintegration.services.soap.customerbillnumberreservation.v1.MessageFault;


/**
 * @author Victor Cifuentes @ Consystec - 2017
 *
 */
public class OperacionFoliosSCL {
	private OperacionFoliosSCL(){}
    private static final Logger log = Logger.getLogger(OperacionFoliosSCL.class);
    
    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * @throws MalformedURLException 
     */

    
    public static OutputConfiguracionFolioVirtual doGet(Connection conn, InputFolioVirtual input)throws SQLException, MalformedURLException {
    	String nombreMetodo = "doGet";
    	String nombreClase = new CurrentClassGetter().getClassName();

    	List<InputFolioRutaPanel> list = null;
    	Respuesta respuesta = new Respuesta();
    	OutputConfiguracionFolioVirtual output = new OutputConfiguracionFolioVirtual();

    	URL endpoint = new URL(UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_RESERVA_FOLIOS, input.getCodArea()));

    	CustomerBillNumberReservationBindingQSService ws = new CustomerBillNumberReservationBindingQSService(endpoint);
    	CustomerBillNumberReservationV1 w = ws.getCustomerBillNumberReservationBindingQSPort();
    	CustomerBillNumberReservationRequestType request = new CustomerBillNumberReservationRequestType();
    	CustomerBillNumberReservationResponseType response = new CustomerBillNumberReservationResponseType();

    	try {
    		request.setObjectIDLogicalDevice(input.getCodDispositivo());
    		request.setAmountQuantity(new BigDecimal(input.getCantFolios()));
    		request.setOfficeId(input.getCodOficina());
    		request.setBillNoCustomerBill(input.getSerie());
    		request.setDocumentTypeCustomerBill(input.getTipoDocumento());

    		log.trace("request: " + new GsonBuilder().setPrettyPrinting().create().toJson(request));
    		HeaderInType h = FSUtil.instanciaHeader(input.getUsuario(), input.getCodArea());
    		
    		log.trace("header: " + new GsonBuilder().setPrettyPrinting().create().toJson(h));
    		
    		Holder<HeaderOutType> hres = new Holder<HeaderOutType>();
    		response = w.customerBillNumberReservation(request, h, hres);
    	} catch (MessageFault e) {
    		if(e.getFaultInfo().getExceptionCode() == 1050){
    			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_FOLIOS_UTILIZADOS_682, e.getFaultInfo().getAppDetail().getExceptionAppCause(), 
    					nombreClase, nombreMetodo, null, input.getCodArea());
    		}else{
    			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_FOLIOS_SCL_855, e.getFaultInfo().getAppDetail().getExceptionAppCause(), 
    					nombreClase, nombreMetodo,  e.getFaultInfo().getExceptionMessage(), input.getCodArea());
    		}
    		
            log.error("Excepcion: " + e.getMessage(), e);
            output.setRespuesta(respuesta);
    	}
    	
    	if(response.getIDBusinessInteraction() != null){
    		respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_GET_FOLIOS_SCL_73, null, nombreClase, nombreMetodo, null, input.getCodArea());
    		InputFolioRutaPanel item = new InputFolioRutaPanel();
    		output = new OutputConfiguracionFolioVirtual();
    		list = new ArrayList<InputFolioRutaPanel>();
    		
    		item.setNoInicialFolio(response.getRangeItem().getLowerValueRange());
    		item.setNoFinalFolio(response.getRangeItem().getUpperValueRange());
    		item.setIdReserva(response.getIDBusinessInteraction());
    		
    		log.trace("Rango Inicial  " + response.getRangeItem().getLowerValueRange());
    		log.trace("Rango Final  " + response.getRangeItem().getUpperValueRange());
    		log.trace("Rango Id Reserva  " + response.getIDBusinessInteraction());
    		
    		list.add(item);
    		output.setRespuesta(respuesta);
    		output.setFolios(list);
    	}
    	
    	
        return output;
    }
    
    public static OutputConfiguracionFolioVirtual doGetVantive(Connection conn, InputFolioVirtual input, String idPlaza)throws SQLException, MalformedURLException {
    	String nombreMetodo = "doGet";
    	String nombreClase = new CurrentClassGetter().getClassName();

    	List<InputFolioRutaPanel> list = null;
    	Respuesta respuesta = new Respuesta();
    	OutputConfiguracionFolioVirtual output = new OutputConfiguracionFolioVirtual();

    	
    	String endpointFolios = UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_RESERVA_FOLIOS, input.getCodArea());
    	
    	WsFolioProxy wsProxyFolio = new WsFolioProxy(endpointFolios);
    	
    	WsCreacionReservaRequest reqFolio = new WsCreacionReservaRequest();
    	InputCreacion inputFolio = new InputCreacion();
    	
    	inputFolio.setCodOficina(input.getCodOficina());
    	inputFolio.setCodPlaza(idPlaza);
    	inputFolio.setCantidad(Integer.parseInt(input.getCantFolios()));
    	inputFolio.setTipoFac(input.getTipoDocumento());
    	inputFolio.setSerieFac(input.getSerie());
    	inputFolio.setDispositivo(input.getCodDispositivo());
    	inputFolio.setPuntoVenta("");
    	inputFolio.setUsuario(input.getUsuario());
    	
    	reqFolio.setInputCreacion(inputFolio);
    	WsCreacionReservaResponse resp = new WsCreacionReservaResponse();
    	
    	try {
			 resp = wsProxyFolio.wsCreacionReserva(reqFolio);
			
			
		} catch (RemoteException e1) {
	
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_FOLIOS_SCL_855, e1.getMessage(), 
					nombreClase, nombreMetodo,  e1.getMessage(), input.getCodArea());
			
			
			log.error("Excepcion: " + e1.getMessage(), e1);
			output.setRespuesta(respuesta);
		}
    	

    	
    	if(resp.getIdReserva() != null && !resp.getIdReserva().equals("0")){
    		respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_GET_FOLIOS_SCL_73, null, nombreClase, nombreMetodo, null, input.getCodArea());
    		InputFolioRutaPanel item = new InputFolioRutaPanel();
    		output = new OutputConfiguracionFolioVirtual();
    		list = new ArrayList<>();
    		
    		item.setNoInicialFolio(resp.getFolioInicial());
    		
    		
    		item.setNoFinalFolio(resp.getFoliofinal());
    		item.setIdReserva(resp.getIdReserva());
    		
    		log.trace("Rango Inicial  " + input.getIdRangoFolio());
    		log.trace("Rango Final  " + resp.getFoliofinal());
    		log.trace("Rango Id Reserva  " + resp.getIdReserva());
    		
    		list.add(item);
    		output.setRespuesta(respuesta);
    		output.setFolios(list);
    	}else {
    		
    		respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_FOLIOS_SCL_855, resp.getMensaje(), 
					nombreClase, nombreMetodo,  resp.getMensaje(), input.getCodArea());
			
			
			log.debug(resp.getMensaje());
			output.setRespuesta(respuesta);
    	}
    	
    	
        return output;
    }
}

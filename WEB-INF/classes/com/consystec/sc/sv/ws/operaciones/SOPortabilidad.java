package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.ws.Holder;

import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.orm.MensajeVW;
import com.consystec.sc.ca.ws.so.request.AtributosModify;
import com.consystec.sc.ca.ws.so.request.ConsultaCrearOrdenVenta;
import com.consystec.sc.ca.ws.so.request.ModifySalesOrder;
import com.consystec.sc.ca.ws.so.response.RespuestaCrearOrdenVenta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.FSUtil;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telefonica.globalintegration.header.HeaderInType;
import com.telefonica.globalintegration.header.HeaderOutType;
import com.telefonica.globalintegration.services.createcustomerorder.v1.CustomerOrderItemItemType;
import com.telefonica.globalintegration.services.createcustomerorder.v1.CustomerOrderItemListItemType;
import com.telefonica.globalintegration.services.createportedphonenumber.v1.CreatePortedPhoneNumberRequestType;
import com.telefonica.globalintegration.services.createportedphonenumber.v1.CreatePortedPhoneNumberResponseType;
import com.telefonica.globalintegration.services.createportedphonenumber.v1.CustomerOwnerItemType;
import com.telefonica.globalintegration.services.getcustomerorder.v1.GetCustomerOrderRequestType;
import com.telefonica.globalintegration.services.getcustomerorder.v1.GetCustomerOrderResponseType;
import com.telefonica.globalintegration.services.modifycustomerorderitem.v1.AttributeCustomerOrderItemType;
import com.telefonica.globalintegration.services.modifycustomerorderitem.v1.AttributeCustomerOrderListItemType;
import com.telefonica.globalintegration.services.modifycustomerorderitem.v1.ModifyCustomerOrderItemRequestType;
import com.telefonica.globalintegration.services.modifycustomerorderitem.v1.ModifyCustomerOrderItemResponseType;
import com.telefonica.globalintegration.services.soap.createportedphonenumber.v1.CreatePortedPhoneNumberBindingQSService;
import com.telefonica.globalintegration.services.soap.createportedphonenumber.v1.CreatePortedPhoneNumberV1;
import com.telefonica.globalintegration.services.soap.getcustomerorder.v1.GetCustomerOrderBindingQSService;
import com.telefonica.globalintegration.services.soap.getcustomerorder.v1.GetCustomerOrderV1;
import com.telefonica.globalintegration.services.soap.getcustomerorder.v1.MessageFault;
import com.telefonica.globalintegration.services.soap.modifycustomerorderitem.v1.ModifyCustomerOrderItemV1;
import com.telefonica.globalintegration.services.soap.modifycustomerorderitem.v1.ModifyCustomerOrderItemV1BindingQSService;

public class SOPortabilidad {
	private SOPortabilidad(){}
    private static final Logger log = Logger.getLogger(SOPortabilidad.class);
    
    /**
     * Consumo de WS para crear orden de venta FS.
	 * @param url
	 * @param consulta
	 * @return
	 */
    public static RespuestaCrearOrdenVenta crearOrdenVentaFS(String url, ConsultaCrearOrdenVenta consulta) {
    	RespuestaCrearOrdenVenta respuesta = new RespuestaCrearOrdenVenta();
    	MensajeVW msj = null;
    	try {
			URL endpoint = new URL(url);
			
	    	com.telefonica.globalintegration.services.soap.createcustomerorder.v1.CreateCustomerOrderBindingQSService ws = new com.telefonica.globalintegration.services.soap.createcustomerorder.v1.CreateCustomerOrderBindingQSService(endpoint);
	    	com.telefonica.globalintegration.services.soap.createcustomerorder.v1.CreateCustomerOrderV1 w = ws.getCreateCustomerOrderBindingQSPort();

	    	HeaderInType headerReq = FSUtil.instanciaHeader(consulta.getUsuario(), consulta.getPais());
	    	Holder<HeaderOutType> headerResp = new Holder<HeaderOutType>();
	    	try {
	    		com.telefonica.globalintegration.services.createcustomerorder.v1.CreateCustomerOrderRequestType bodyReq = new com.telefonica.globalintegration.services.createcustomerorder.v1.CreateCustomerOrderRequestType();
	        	com.telefonica.globalintegration.services.createcustomerorder.v1.CustomerOrderItemType customerOrderItem = new com.telefonica.globalintegration.services.createcustomerorder.v1.CustomerOrderItemType();
	        	customerOrderItem.setIDCustomer(consulta.getIdCliente());
	        	customerOrderItem.setIDDistributionChannel(consulta.getIdCanalDistribucion());
	        	customerOrderItem.setIDGeographicLocation(consulta.getIdUbicacion());	        	
	        	  //--------------cambio
	        	  CustomerOrderItemListItemType lst = new CustomerOrderItemListItemType();
	              CustomerOrderItemItemType objeto = new CustomerOrderItemItemType();
	              objeto.setIdProductOffering(consulta.getIdOffering());
	              lst.getCustomerOrderItemItem().add(objeto);
	              customerOrderItem.setCustomerOrderItemListItem(lst);
	              //-------------fin cambio
	        	bodyReq.setCustomerOrderItem(customerOrderItem);
	        	
	        	log.trace("request servicio");
				Gson gson1 = new GsonBuilder().create();
				log.trace("Body:"+gson1.toJson(bodyReq));
				log.trace("Encabezado:"+gson1.toJson(headerReq));
				
	        	com.telefonica.globalintegration.services.createcustomerorder.v1.CreateCustomerOrderResponseType bodyResp = w.createCustomerOrder(bodyReq, headerReq, headerResp);
	        	try {
	        		respuesta.setIdOrdenVenta(bodyResp.getIDCustomerOrder());
	        		respuesta.setNombreOrdenVenta(bodyResp.getNameCustomerOrder());
	        		msj = getMensajeResultadoExito("Orden de venta creada exitosamente.");
	        		log.info("Orden de venta creada exitosamente.");
	        	} catch (NullPointerException e) {
	    			msj = getMensajeResultadoError("Respuesta de Fullstack vac\u00EDa.");
	    			log.error(e.getMessage(), e);
	        	}
			} catch (com.telefonica.globalintegration.services.soap.createcustomerorder.v1.MessageFault e) {
				msj = getMensajeResultadoError("Excepcion: " + e.getFaultInfo().getExceptionCategory() + " - " + e.getFaultInfo().getExceptionCode() + " - " + e.getFaultInfo().getExceptionMessage() + ".");
				log.error(e.getMessage(), e);
			}
		} catch (MalformedURLException e) {
			msj = getMensajeResultadoError("Ocurri\u00F3 un error desconocido, comun\u00EDquese con su administrador. " + e.getMessage());
			log.error(e.getMessage(), e);
		}
    	respuesta.setMensaje(msj);
    	return respuesta;
    }
    
    


    /**
     * M\u00E9todo para modificar un item de una orden de venta. (Agrega caracteristicas).
     * @param conn
     * @param usuario
     * @param codArea
     * @param idCustomerOrder
     * @param idDistributionChannel
     * @param idGeographicLocation
     * @param idCustomerOrderItem
     * @return
     * @throws MalformedURLException
     * @throws SQLException
     */
	public static RespuestaCrearOrdenVenta modificarItemOrdenVentaFS(
			Connection conn, ModifySalesOrder datos, String instanceIdProduct, String codArea)
			throws MalformedURLException, SQLException {
		log.trace("Consumientod servicio ModificarItemOrdenVentaFS");
		ModifyCustomerOrderItemResponseType bodyResp = null;
		MensajeVW msj = null;
		RespuestaCrearOrdenVenta respuesta = new RespuestaCrearOrdenVenta();
		URL endpoint = new URL(UtileriasJava.getConfig(conn,
				Conf.GRUPO_URL_WS_FS, Conf.FS_MODIFICA_ITEM_ORDEN_VENTA,  codArea));
	
		ModifyCustomerOrderItemV1BindingQSService ws = new com.telefonica.globalintegration.services.soap.modifycustomerorderitem.v1.ModifyCustomerOrderItemV1BindingQSService(
				endpoint);
		ModifyCustomerOrderItemV1 w = ws.getModifyCustomerOrderItemV1BindingQSPort();

		HeaderInType headerReq = FSUtil.instanciaHeader(datos.getUsuario(), datos.getCodArea());
		Holder<HeaderOutType> headerResp = new Holder<HeaderOutType>();
		try {
			ModifyCustomerOrderItemRequestType requestBody = new ModifyCustomerOrderItemRequestType();
		    com.telefonica.globalintegration.services.modifycustomerorderitem.v1.CustomerOrderItemType customerOrderItem = new com.telefonica.globalintegration.services.modifycustomerorderitem.v1.CustomerOrderItemType();
			
			AttributeCustomerOrderListItemType LstOrderItem = new AttributeCustomerOrderListItemType();
			AttributeCustomerOrderItemType atributo = null;
			
		
			//customer order item			 
		
			customerOrderItem.setIDCustomer(datos.getIdCustomer());
			customerOrderItem.setIDDistributionChannel(datos.getIdDistributionChanel());
			customerOrderItem.setIDGeographicLocation(datos.getIdLocation());
			
			//atributos
			for (AtributosModify obj: datos.getAtributos()){
				atributo = new AttributeCustomerOrderItemType();
				atributo.setAttributeIDAttributeValuePair(obj.getIdAtributo());
				atributo.setAttributeValueAttributeValuePair(obj.getValorAtributo());
				
				LstOrderItem.getAttributeCustomerOrderItem().add(atributo);
			}
			//armando body			
			requestBody.setInstanceIdProduct(instanceIdProduct);
			requestBody.setCustomerOrderItem(customerOrderItem);
			requestBody.setAttributeCustomerOrderListItem(LstOrderItem);
			
			
			Gson gson = new GsonBuilder().create();
			log.trace("Body" + gson.toJson(requestBody));
			Gson gson2 = new GsonBuilder().create();
			log.trace("Header Req" +gson2.toJson(headerReq));

			
			bodyResp = w.modifyCustomerOrderItem(requestBody, headerReq, headerResp);
			log.trace("Se registro exitosamente sales order: "+ bodyResp.getIDCustomerOrder());
			respuesta.setIdOrdenVenta(bodyResp.getIDCustomerOrder());
			respuesta.setNombreOrdenVenta(bodyResp.getNameCustomerOrder());
			msj=getMensajeResultadoExito("Se consumio correctamente el Serviciio osb");
		}catch (NullPointerException e) {
			msj = getMensajeResultadoError("Respuesta de Fullstack vac\u00EDa.");
			log.error(e.getMessage(), e);				
    	}
		catch (com.telefonica.globalintegration.services.soap.modifycustomerorderitem.v1.MessageFault e) {
			log.error("Excepcion: " + e.getFaultInfo().getExceptionCategory()
					+ " - " + e.getFaultInfo().getExceptionCode() + " - "
					+ e.getFaultInfo().getExceptionMessage() + ". ", e);
			msj = getMensajeResultadoError( e.getMessage());			
		}
		respuesta.setMensaje(msj);
		return respuesta;
	}
	
	/**
	 * M\u00E9todo para obtener sales order realizada obtiene el orderitem sobre 
	 * el cual hay que modificar las caracteritiscas para portabilidad
	 * @param conn
	 * @param usuario
	 * @param codArea
	 * @param idSalesOrder
	 * @return
	 * @throws MessageFault
	 * @throws MalformedURLException
	 * @throws SQLException
	 */
	public static String getSalesOrder (Connection conn, String usuario, String codArea, String idSalesOrder, String numTemporal) throws MessageFault, MalformedURLException, SQLException{
		
		GetCustomerOrderResponseType bodyResp ;
		URL endpoint =null;
		GetCustomerOrderBindingQSService ws = null;
		GetCustomerOrderV1 w= null;
		HeaderInType headerReq = null; 
		Holder<HeaderOutType> headerResp = new Holder<HeaderOutType>();
		GetCustomerOrderRequestType requestBody= null;
		String idcustomerorder="";
		String nuevoNumTemporal="";
		 
		 com.telefonica.globalintegration.services.getcustomerorder.v1.CustomCustomerProductType datos ; 
		 
			endpoint = new URL(UtileriasJava.getConfig(conn,Conf.GRUPO_URL_WS_FS, Conf.FS_OBTIENE_ORDEN_VENTA,codArea));
			ws = new GetCustomerOrderBindingQSService(endpoint);
			headerReq = null; FSUtil.instanciaHeader(usuario, codArea);
			requestBody = new GetCustomerOrderRequestType();
			
			requestBody.setIDCustomerOrder(idSalesOrder);
			
			nuevoNumTemporal=numTemporal.substring(0, 4)+" "+numTemporal.substring(4, 8);
			log.trace("num temporal:"+nuevoNumTemporal);
			w=ws.getGetCustomerOrderBindingQSPort();
			log.trace("request servicio");
			Gson gson1 = new GsonBuilder().create();
			log.trace("Body:"+gson1.toJson(requestBody));
			log.trace("Encabezado:"+gson1.toJson(headerReq));
			bodyResp =w.getCustomerOrder(requestBody,headerReq,headerResp);
			log.trace("consumio servicio");
			Gson gson = new GsonBuilder().create();
			log.trace("consumio servicio:"+gson.toJson(bodyResp));
			//L\u00EDnea M\u00F3vil Prepago
			if(bodyResp==null ){
				log.trace("no trae respuesta");

				idcustomerorder="No se obtuvo sales order";
			}else{
			for (int a=0; a<bodyResp.getCustomerOrderRecordItem().getCustomerProductListItem().getCustomerProductItem().size(); a++){
				log.trace("inicia a recorrer datos");
				datos= bodyResp.getCustomerOrderRecordItem().getCustomerProductListItem().getCustomerProductItem().get(a);
				if (datos.getCustomerProductListItem()!=null)
				{
					for (int b=0; b<datos.getCustomerProductListItem().getCustomerProductItem().size(); b++){
						log.trace("recorre order item");
						log.trace("nombreProduct:"+ datos.getCustomerProductListItem().getCustomerProductItem().get(b).getNameProduct());
						
						if (datos.getCustomerProductListItem().getCustomerProductItem().get(b).getNameProduct().contains(" Prepago - "+nuevoNumTemporal)){
							log.trace("nombreProduct encontrado:"+ datos.getCustomerProductListItem().getCustomerProductItem().get(b).getNameProduct());
							idcustomerorder=datos.getCustomerProductListItem().getCustomerProductItem().get(b).getIDCustomerOrderItem();
							break;
						}	
					}
				}					
			}
			}
		
		return idcustomerorder;
	}
    
	/**
	  * Consumo de WS para finalizar orden de venta FS.
	  * @param conn
	  * @param usuario
	  * @param codArea
	  * @param input
	  * @return
	  * @throws MalformedURLException
	  * @throws SQLException
	  */
	 
	public static RespuestaCrearOrdenVenta finalizarOrdenVentaFS(Connection conn,
			String usuario, String codArea, String idCustomerOrder)
			{
		
		RespuestaCrearOrdenVenta objResp = new RespuestaCrearOrdenVenta();
		MensajeVW msj = null;
		com.telefonica.globalintegration.services.submitcustomerorderbyid.v1.SubmitCustomerOrderByIDResponseType bodyResp = null;
		
		try {
			URL endpoint = new URL(UtileriasJava.getConfig(conn,
					Conf.GRUPO_URL_WS_FS, Conf.FS_FINALIZA_ORDEN_VENTA,codArea));

			com.telefonica.globalintegration.services.soap.submitcustomerorderbyid.v1.SubmitCustomerOrderByIDBindingQSService ws = new com.telefonica.globalintegration.services.soap.submitcustomerorderbyid.v1.SubmitCustomerOrderByIDBindingQSService(
					endpoint);
			com.telefonica.globalintegration.services.soap.submitcustomerorderbyid.v1.SubmitCustomerOrderByIDV1 w = ws
					.getSubmitCustomerOrderByIDBindingQSPort();

			HeaderInType headerReq = FSUtil.instanciaHeader(usuario, codArea);
			Holder<HeaderOutType> headerRes = new Holder<HeaderOutType>();
			com.telefonica.globalintegration.services.submitcustomerorderbyid.v1.SubmitCustomerOrderByIDRequestType bodyReq = new com.telefonica.globalintegration.services.submitcustomerorderbyid.v1.SubmitCustomerOrderByIDRequestType();
			bodyReq.setIDCustomerOrder(idCustomerOrder);

			bodyResp = w.submitCustomerOrderByID(bodyReq, headerReq, headerRes);
			objResp.setIdOrdenVenta(bodyResp.getIDCustomerOrder());
			msj = getMensajeResultadoExito("Orden de venta finalizada exitosamente.");
    		log.info("Orden de venta finalizada exitosamente.");
    		
		}catch (NullPointerException e) {
			msj = getMensajeResultadoError("Respuesta de Fullstack vac\u00EDa.");
			log.error(e.getMessage(), e);
    	}
		catch (com.telefonica.globalintegration.services.soap.submitcustomerorderbyid.v1.MessageFault e) {
			msj = getMensajeResultadoError("Excepcion: " + e.getFaultInfo().getExceptionCategory() + " - " + e.getFaultInfo().getExceptionCode() + " - " + e.getFaultInfo().getExceptionMessage() + ".");
			log.error(e.getMessage(), e);
		} catch (MalformedURLException e) {
			msj = getMensajeResultadoError( e.getMessage());
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			msj = getMensajeResultadoError( e.getMessage());
			log.error(e.getMessage(), e);
		}
		objResp.setMensaje(msj);
		return objResp;
	}
	
	
	
	/**
	 * N\u00FAmero 
	 * @throws com.telefonica.globalintegration.services.soap.createportedphonenumber.v1.MessageFault 
	 * */
	public static RespuestaCrearOrdenVenta creaNumeroFS (Connection conn, String numero, String cliente, String usuario, String codArea) throws MalformedURLException, SQLException, com.telefonica.globalintegration.services.soap.createportedphonenumber.v1.MessageFault{
		MensajeVW msj = null;
		RespuestaCrearOrdenVenta ret = new RespuestaCrearOrdenVenta();
		try {
		URL endpoint = new URL(UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_CREA_NUMERO,codArea));
		CreatePortedPhoneNumberBindingQSService ws = new CreatePortedPhoneNumberBindingQSService(endpoint);
		CreatePortedPhoneNumberV1 w= ws.getCreatePortedPhoneNumberBindingQSPort();
		
		//armando request
		CreatePortedPhoneNumberRequestType requestBody = new CreatePortedPhoneNumberRequestType();
		HeaderInType headerRequest = new HeaderInType();
		CustomerOwnerItemType customerItem = new CustomerOwnerItemType();
	
		CreatePortedPhoneNumberResponseType responseBody = new CreatePortedPhoneNumberResponseType();
		Holder<HeaderOutType> hOutType = new Holder<HeaderOutType>();
		 
		headerRequest = FSUtil.instanciaHeader(usuario, codArea);
		requestBody.setMsisdn(numero);
		requestBody.setTypeProductCatalog(UtileriasJava.getConfig(conn, Conf.GRUPO_CREA_NUMERO, Conf.TIPO_PRODUCTO_M_PREPAGO,codArea));
		customerItem.setIDCustomer(cliente);
		requestBody.setCustomerOwnerItem(customerItem);
		
		 Gson gson = new GsonBuilder().create();
		 log.trace("BODY:"+ gson.toJson(requestBody));
			responseBody = w.createPortedPhoneNumber(requestBody, headerRequest, hOutType);
			 if(!(responseBody.getMsisdn() == null)){
	    		 //mensaje de respuesta
				 msj=getMensajeResultadoExito("N\u00FAmero a portar creado exitosamente");
		    		log.info("N\u00FAmero a portar creado exitosamente");
	    	 }
		}catch (NullPointerException e) {
			msj = getMensajeResultadoError("Respuesta de Fullstack vac\u00EDa.");
			log.error(e.getMessage(), e);
    	}
		catch (com.telefonica.globalintegration.services.soap.createportedphonenumber.v1.MessageFault e) {
			msj = getMensajeResultadoError("Excepcion: " + e.getFaultInfo().getExceptionCategory() + " - " + e.getFaultInfo().getExceptionCode() + " - " + e.getFaultInfo().getExceptionMessage() + ".");
			log.error(e.getMessage(), e);
		} catch (MalformedURLException e) {
			msj = getMensajeResultadoError( e.getMessage());
			log.error(e.getMessage(), e);
		} catch (SQLException e) {
			msj = getMensajeResultadoError( e.getMessage());
			log.error(e.getMessage(), e);
		}finally{
			ret.setMensaje(msj);
		}
		
	return ret;
	}
	
	public static MensajeVW getMensajeResultadoExito(String descripcion){
		MensajeVW mensaje = new MensajeVW();
		mensaje.setCodresultado(BigDecimal.ONE);
		mensaje.setDescripcion(descripcion);
		return mensaje;
	}

	public static MensajeVW getMensajeResultadoError(String descripcion){
		MensajeVW mensaje = new MensajeVW();
		mensaje.setCodresultado(BigDecimal.ZERO);
		mensaje.setDescripcion(descripcion);
		return mensaje;
	}
}

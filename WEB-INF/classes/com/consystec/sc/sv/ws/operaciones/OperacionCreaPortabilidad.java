package com.consystec.sc.sv.ws.operaciones;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Holder;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.portabilidad.InputCargaAdjuntoPorta;
import com.consystec.sc.ca.ws.input.portabilidad.InputCreaPortabilidad;
import com.consystec.sc.ca.ws.input.portabilidad.InputProductoCustomer;
import com.consystec.sc.ca.ws.output.fichacliente.IndividualContactItem;
import com.consystec.sc.ca.ws.output.fichacliente.OutputDatosClienteFS;
import com.consystec.sc.ca.ws.output.portabilidad.OutputCreaPortabilidad;
import com.consystec.sc.ca.ws.output.portabilidad.OutputInformacionClienteFS;
import com.consystec.sc.sv.ws.orm.AdjuntoPorta;
import com.consystec.sc.sv.ws.orm.Portabilidad;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.FSUtil;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telefonica.globalintegration.header.HeaderInType;
import com.telefonica.globalintegration.header.HeaderOutType;
import com.telefonica.globalintegration.services.addcustomerattachment.v1.AddCustomerAttachmentRequestType;
import com.telefonica.globalintegration.services.addcustomerattachment.v1.AddCustomerAttachmentResponseType;
import com.telefonica.globalintegration.services.getcustomerfullinformation.v1.GetCustomerFullInformationRequestType;
import com.telefonica.globalintegration.services.getcustomerfullinformation.v1.GetCustomerFullInformationResponseType;
import com.telefonica.globalintegration.services.getproductcustomeraccount.v1.AttributeValuePairItemType;
import com.telefonica.globalintegration.services.getproductcustomeraccount.v1.CustomProductCustomerAccountType;
import com.telefonica.globalintegration.services.getproductcustomeraccount.v1.GetProductCustomerAccountRequestType;
import com.telefonica.globalintegration.services.getproductcustomeraccount.v1.GetProductCustomerAccountResponseType;
import com.telefonica.globalintegration.services.getproductcustomeraccountbyid.v1.CustomCustomerProductListType;
import com.telefonica.globalintegration.services.getproductcustomeraccountbyid.v1.CustomCustomerProductType;
import com.telefonica.globalintegration.services.getproductcustomeraccountbyid.v1.GetProductCustomerAccountByIDRequestType;
import com.telefonica.globalintegration.services.getproductcustomeraccountbyid.v1.GetProductCustomerAccountByIDResponseType;
import com.telefonica.globalintegration.services.getresidentialcustomer.v1.GetResidentialCustomerRequestType;
import com.telefonica.globalintegration.services.getresidentialcustomer.v1.GetResidentialCustomerResponseType;
import com.telefonica.globalintegration.services.soap.addcustomerattachment.v1.AddCustomerAttachmentBindingQSService;
import com.telefonica.globalintegration.services.soap.addcustomerattachment.v1.AddCustomerAttachmentV1;
import com.telefonica.globalintegration.services.soap.getcustomerfullinformation.v1.GetCustomerFullInformationBindingQSService;
import com.telefonica.globalintegration.services.soap.getcustomerfullinformation.v1.GetCustomerFullInformationV1;
import com.telefonica.globalintegration.services.soap.getproductcustomeraccount.v1.GetProductCustomerAccountBindingQSService;
import com.telefonica.globalintegration.services.soap.getproductcustomeraccount.v1.GetProductCustomerAccountV1;
import com.telefonica.globalintegration.services.soap.getproductcustomeraccountbyid.v1.GetProductCustomerAccountByIDBindingQSService;
import com.telefonica.globalintegration.services.soap.getproductcustomeraccountbyid.v1.GetProductCustomerAccountByIDV1;
import com.telefonica.globalintegration.services.soap.getresidentialcustomer.v1.GetResidentialCustomerBindingQSService;
import com.telefonica.globalintegration.services.soap.getresidentialcustomer.v1.GetResidentialCustomerV1;
import com.telefonica.globalintegration.services.soap.updatecustomer.v1.UpdateCustomerBindingQSService;
import com.telefonica.globalintegration.services.soap.updatecustomer.v1.UpdateCustomerV1;
import com.telefonica.globalintegration.services.updatecustomer.v1.IndividualContactListItemType;
import com.telefonica.globalintegration.services.updatecustomer.v1.IndividualIdentificationItemType;
import com.telefonica.globalintegration.services.updatecustomer.v1.SegmentCustomerItemType;
import com.telefonica.globalintegration.services.updatecustomer.v1.UpdateCustomerRequestType;
import com.telefonica.globalintegration.services.updatecustomer.v1.UpdateCustomerResponseType;

public class OperacionCreaPortabilidad {
	private OperacionCreaPortabilidad(){}
	private static final Logger log = Logger.getLogger(OperacionCreaPortabilidad.class);
	public static InputProductoCustomer getProductoCustomer (Connection conn, InputCreaPortabilidad input)throws MalformedURLException, SQLException 
	{	
		InputProductoCustomer output =new InputProductoCustomer();
		System.out.println("Verificamos el Producto Customer");
		log.trace("Verificamos el Producto Customer:" );
		String valorIdAttributeValue="";
	
		URL endpoint = new URL(UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_GET_DATOS_CONSUMER, input.getCodArea()));
		GetProductCustomerAccountBindingQSService ws = new GetProductCustomerAccountBindingQSService(endpoint); 
		GetProductCustomerAccountV1 w=ws.getGetProductCustomerAccountBindingQSPort();
		GetProductCustomerAccountRequestType requestBody=new GetProductCustomerAccountRequestType(); 
		GetProductCustomerAccountResponseType response= new GetProductCustomerAccountResponseType(); 
		
		log.trace("Obtenemos informacion del producto" );
		try{
			valorIdAttributeValue= UtileriasJava.getConfig(conn, Conf.GRUPO_PRODUCT_CUSTOMER, Conf.VALOR_IDATTRIBUTE, input.getCodArea());
			log.trace("valor idAttributevalue "+ valorIdAttributeValue );
			com.telefonica.globalintegration.services.getproductcustomeraccount.v1.AttributeValuePairListItemType AttributeValue= new com.telefonica.globalintegration.services.getproductcustomeraccount.v1.AttributeValuePairListItemType();
			
			AttributeValuePairItemType Obj =new AttributeValuePairItemType();
			Obj.setAttributeIDAttributeValuePair(valorIdAttributeValue); //9143722377013301268
			Obj.setAttributeNameAttributeValuePair("");			
			Obj.setAttributeTypeAttributeValuePair("");
			Obj.setAttributeValueAttributeValuePair(input.getNumTemporal());
			Obj.setAttributeIndexAttributeValuePair("");
			
			AttributeValue.getAttributeValuePairItem().add(Obj);
			requestBody.setAttributeValuePairListItem(AttributeValue);
			log.trace("atributos ingresados");
			Holder<HeaderOutType> hres = new Holder<HeaderOutType>();
			HeaderInType h = FSUtil.instanciaHeader(input.getUsuario(), input.getCodArea());
			response = w.getProductCustomerAccount(requestBody, h, hres);
			
			Gson gsonresponse = new GsonBuilder().create();
			log.trace(gsonresponse.toJson(requestBody));
			
		}catch(com.telefonica.globalintegration.services.soap.getproductcustomeraccount.v1.MessageFault e)
		{
			log.error(e.getMessage(), e);
			output.setStatusProduct("0");
			
		}
		log.trace("se consumio ws product customer: " );
		Gson gson = new GsonBuilder().create();
		log.trace(gson.toJson(response));
		if(response.getProductCustomerAccountListItem() != null){
			log.trace("se valida la response: " );
		   			response.getProductCustomerAccountListItem().getProductCustomerAccountItem().add(new CustomProductCustomerAccountType());
		   				log.trace("se obtiene getProductStatusProduct: " +response.getProductCustomerAccountListItem().getProductCustomerAccountItem().get(0).getProductStatusProduct());	   				
		   				response.getProductCustomerAccountListItem().getProductCustomerAccountItem().add(new CustomProductCustomerAccountType());
			   			output.setStatusProduct(response.getProductCustomerAccountListItem().getProductCustomerAccountItem().get(0).getProductStatusProduct());
			   			output.setParentIDProduct(response.getProductCustomerAccountListItem().getProductCustomerAccountItem().get(0).getParentIDProduct());
		}
		else
		{
			log.error("No se encontraron elementos");
			output.setStatusProduct("0");
		}
		log.trace("se consumio ws product customer resultado: " + output.getStatusProduct());
		return output;
	
	}
	
	public static String updateDatoscliente(Connection conn, InputCreaPortabilidad input, OutputInformacionClienteFS InfoCustomer)throws MalformedURLException, SQLException {
		log.trace("Se actualiza informaci\u00F3n del cliente");
		log.trace("Se actualiza informaci\u00F3n del cliente");
		String output="";
		String idDocumento="";
		URL endpoint = new URL(UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_UPDATE_DATOS_CONSUMER, input.getCodArea()));
		UpdateCustomerBindingQSService ws= new UpdateCustomerBindingQSService(endpoint); 
		UpdateCustomerV1 w=ws.getUpdateCustomerBindingQSPort();
		UpdateCustomerRequestType requestBody= new UpdateCustomerRequestType(); 
		UpdateCustomerResponseType response=new UpdateCustomerResponseType();
		log.trace("Actualizamos los Datos dle cliente" );
		try
		{
			
			idDocumento=UtileriasJava.getConfig(conn, Conf.GRUPO_FICHA_CLIENTE_CLIENTE, input.getTipoDocumento().toUpperCase(), input.getCodArea());
		    IndividualContactListItemType  datos= new IndividualContactListItemType();
		    IndividualIdentificationItemType objIdentificacion = new IndividualIdentificationItemType();
		    com.telefonica.globalintegration.services.updatecustomer.v1.IndividualContactItemType contacto = new  com.telefonica.globalintegration.services.updatecustomer.v1.IndividualContactItemType();
		    SegmentCustomerItemType obj= new SegmentCustomerItemType();
		    obj.setIDSegmentCustomer(InfoCustomer.getIDSegmentCustomer());
		    obj.setNameSegmentCustomer(InfoCustomer.getNameSegmentCustomer());
			requestBody.setIDCustomer(InfoCustomer.getIDCustomer());
			requestBody.setPreferredIDCustomerAccountContact(InfoCustomer.getIDCustomerAccountContact());
			requestBody.setSegmentCustomerItem(obj);
		
			
			objIdentificacion.setIdentificationNumberIndividualIdentification(input.getNoDocumento());
			objIdentificacion.setIdentificationTypeIndividualIdentification(idDocumento);
			
			contacto.setGivenNamesIndividualName(input.getPrimerNombre());
			contacto.setMiddleNamesIndividualName(input.getSegundoNombre());
			contacto.setFirstLastNameIndividualName(input.getPrimerApellido());
			contacto.setSecondLastNameIndividualName(input.getSegundoApellido());
			contacto.setIndividualIdentificationItem(objIdentificacion);

			
			datos.getIndividualContactItem().add(0,contacto);
			requestBody.setIndividualContactListItem(datos);
			
			Holder<HeaderOutType> hres = new Holder<HeaderOutType>();
			HeaderInType h = FSUtil.instanciaHeader(input.getUsuario(), input.getCodArea());
			response = w.updateCustomer(requestBody, h, hres);
		}
		catch (com.telefonica.globalintegration.services.soap.updatecustomer.v1.MessageFault e) {
			e.printStackTrace();
		}

		if(response.getIDCustomer() != null){

			
			output=(response.getIDCustomer());
			
		}
		return output;
	}
	
	public static OutputInformacionClienteFS getDatoscliente(Connection conn, InputCreaPortabilidad input)throws MalformedURLException, SQLException {
		System.out.println("obtener full informaci\u00F3n de cliente");
		log.trace("Obtenemos informacion Cliente:" );
		OutputInformacionClienteFS output = null;
		
		URL endpoint = new URL(UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_GET_ALL_DATOS_CLIENTE, input.getCodArea()));
		GetCustomerFullInformationBindingQSService ws = new GetCustomerFullInformationBindingQSService(endpoint);
		GetCustomerFullInformationV1 w = ws.getGetCustomerFullInformationBindingQSPort();
		GetCustomerFullInformationRequestType requestBody = new GetCustomerFullInformationRequestType();
		GetCustomerFullInformationResponseType response = new GetCustomerFullInformationResponseType();
		
		log.trace("Obtenemos informacion Cliente" );
		try{			
			requestBody.setMsisdn(input.getNumTemporal());
			Holder<HeaderOutType> hres = new Holder<HeaderOutType>();
			HeaderInType h = FSUtil.instanciaHeader(input.getUsuario(), input.getCodArea());
			response = w.getCustomerFullInformation(requestBody, h, hres);
		}catch(com.telefonica.globalintegration.services.soap.getcustomerfullinformation.v1.MessageFault e)
		{
			return output;	
		}
		if(response.getCustomerItem().getIDCustomer() != null){
			output = new OutputInformacionClienteFS();
			log.trace("setiando informacion getdatoscliente");

			output.setIDCustomer(response.getCustomerItem().getIDCustomer());
			output.setAccountNumberCustomer(response.getCustomerItem().getAccountNumberCustomer());
			output.setTypeCustomer(response.getCustomerItem().getTypeCustomer());
			output.setIDCustomerAccountContact(response.getCustomerItem().getIndividualContactListItem().getIndividualContactItem().get(0).getIDCustomerAccountContact());
			output.setGivenNamesIndividualName(response.getCustomerItem().getIndividualContactListItem().getIndividualContactItem().get(0).getGivenNamesIndividualName());
			output.setMiddleNamesIndividualName(response.getCustomerItem().getIndividualContactListItem().getIndividualContactItem().get(0).getMiddleNamesIndividualName());
			output.setFirstLastNameIndividualName(response.getCustomerItem().getIndividualContactListItem().getIndividualContactItem().get(0).getFirstLastNameIndividualName());
			output.setSecondLastNameIndividualName(response.getCustomerItem().getIndividualContactListItem().getIndividualContactItem().get(0).getSecondLastNameIndividualName());
			output.setIDIndividualIdentification(response.getCustomerItem().getIndividualContactListItem().getIndividualContactItem().get(0).getIndividualIdentificationItem().getIDIndividualIdentification());
			output.setIdentificationNumberIndividualIdentification(response.getCustomerItem().getIndividualContactListItem().getIndividualContactItem().get(0).getIndividualIdentificationItem().getIdentificationNumberIndividualIdentification());
			output.setIdentificationTypeIndividualIdentification(response.getCustomerItem().getIndividualContactListItem().getIndividualContactItem().get(0).getIndividualIdentificationItem().getIdentificationTypeIndividualIdentification());
			output.setLocationAttachment(response.getCustomerItem().getCustomerAccountListItem().getCustomerAccountItem().get(0).getCustomerBillingCycleItem().getLocationAttachment());
			output.setIDSegmentCustomer(response.getCustomerItem().getSegmentCustomerItem().getIDSegmentCustomer());
			output.setNameSegmentCustomer(response.getCustomerItem().getSegmentCustomerItem().getNameSegmentCustomer());
			
			
			output.setInstanceIdProduct(response.getCustomerItem().getCustomerAccountListItem().getCustomerAccountItem().get(0).getIDCustomerAccount());
			log.trace("datos de retorno " + output.getIDCustomer());
		}
	
		return output;

	
	}
	
	public static BigDecimal insertarPortabilidad(Connection conn, InputCreaPortabilidad input, BigDecimal ID_PAIS) throws SQLException
	{
	    BigDecimal idPortabilidad =null;
		PreparedStatement pstmt = null;

	   
	            String query = "INSERT INTO TC_SC_PORTABILIDAD ("
			        + "tcsccatpaisid, "
			        + "tcscportabilidadid, "
			        + "idportamovil, "
			        + "tcscjornadavenid, "
			        +"cod_dispositivo, "
			        + "num_portar, "
			        + "cip, "
			        + "tipo_producto, "
			        + "num_temporal, "
			        + "primer_nombre, "
			        + "segundo_nombre, "
			        + "primer_apellido, "
			        + "segundo_apellido, "
			        + "tipo_documento, "
			        + "no_documento, "
			        + "creado_el, "
			        + "creado_por, " 
			        +"operador_donante,"
					+ "id_vendedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?,?,?)";

	            log.debug("inset portabilidad: " + query);
	     try {            	
	            pstmt = conn.prepareStatement(query);
	            idPortabilidad = JavaUtils.getSecuencia(conn, Portabilidad.SEQUENCE);
	            pstmt.setBigDecimal(1, ID_PAIS);
	            pstmt.setBigDecimal(2, idPortabilidad);
	            pstmt.setString(3,input.getIdPortaMovil());
	            pstmt.setBigDecimal(4, new BigDecimal(input.getIdJornada()));
	            pstmt.setString(5, input.getCodDispositivo());
	            pstmt.setBigDecimal(6, new BigDecimal(input.getNumPortar()));
	            pstmt.setBigDecimal(7, new BigDecimal(input.getCip()));
	            pstmt.setString(8, input.getProductoDonante());
	            pstmt.setString(9, input.getNumTemporal());
	            pstmt.setString(10, input.getPrimerNombre());
	            pstmt.setString(11, input.getSegundoNombre());
	            pstmt.setString(12, input.getPrimerApellido());
	            pstmt.setString(13, input.getSegundoApellido());
	            pstmt.setString(14, input.getTipoDocumento());
	            pstmt.setString(15, input.getNoDocumento());
	            pstmt.setString(16, input.getUsuario());
	            pstmt.setString(17, input.getOperadorDonante());
	            pstmt.setString(18, input.getIdVendedor());
	            int res = pstmt.executeUpdate();
	            if (res !=1) {
	            	
	            	idPortabilidad = new BigDecimal(0); 
	            	 log.debug("id de tabal tc_sc_portabilidad "+ idPortabilidad);
	            }
	        } finally {
	            DbUtils.closeQuietly(pstmt);
	        }
	        return idPortabilidad;
	    
	}
	
	/***
	 * M\u00E9todo para actualizar portabilidad cuando ya se tiene idSalesOrder
	 * @param conn
	 * @param idPorta
	 * @param idSalesOrder
	 * @param usuario
	 * @param pais
	 * @return
	 * @throws SQLException
	 */
	public static OutputCreaPortabilidad updatePorta(Connection conn, String idPorta, String idSalesOrder, String usuario, BigDecimal ID_PAIS) throws SQLException
	{
		PreparedStatement pstmt = null;
		OutputCreaPortabilidad out=null;
    	String sql = "";
    	sql = "UPDATE TC_SC_PORTABILIDAD" +
    			"   SET SALESORDERID =?,"+
    			" 		MODIFICADO_POR =?,"+
    			" 		MODIFICADO_EL =SYSDATE "+
    			" WHERE IDPORTAMOVIL = ?"+
    			" AND TCSCCATPAISID= ?"; 

    	log.trace("UPDATE PUNTO DE VENTA " + sql);
    	try{
	    	pstmt = conn.prepareStatement(sql);
	    	pstmt.setBigDecimal(1, new BigDecimal(idSalesOrder));
	    	pstmt.setString(2, usuario);
	    	pstmt.setBigDecimal(3, new BigDecimal(idPorta));
	    	pstmt.setBigDecimal(4,  ID_PAIS);
	    	pstmt.executeUpdate();
	
	    	
    	}finally{
    		DbUtils.closeQuietly(pstmt);
    	}
        return out;
		
	}
	
	 public static OutputDatosClienteFS getGeographicLocation(Connection conn, InputCreaPortabilidad input, String idCustomer) throws MalformedURLException, SQLException{
		    System.out.println("obtener id Geographic Location");
			log.trace("Obtenemos id Geographic Location");
			
		 	OutputDatosClienteFS output = new OutputDatosClienteFS();
	    	
	    	URL endpoint = new URL(UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_GET_DATOS_CLIENTE, input.getCodArea()));
	    	GetResidentialCustomerBindingQSService ws = new GetResidentialCustomerBindingQSService(endpoint);
	    	GetResidentialCustomerV1 w = ws.getGetResidentialCustomerBindingQSPort();
	    	GetResidentialCustomerRequestType requestBody = new GetResidentialCustomerRequestType();
	    	GetResidentialCustomerResponseType response = new GetResidentialCustomerResponseType();
	    	log.trace("consumiendo ws osb ");
			try {
				requestBody.setIDCustomer(idCustomer);		    	
		    	Holder<HeaderOutType> hres = new Holder<HeaderOutType>();
		       	HeaderInType h = FSUtil.instanciaHeader(input.getUsuario(), input.getCodArea());
		       	response=w.getResidentialCustomer(requestBody, h, hres);
			} catch (com.telefonica.globalintegration.services.soap.getresidentialcustomer.v1.MessageFault e) {
				
				log.trace("error consumiendo ws osb " + e);
			}
			log.trace("verificamos respuesta");
	    	if(response.getIDCustomer() != null){
	    		List<IndividualContactItem> list = new ArrayList<IndividualContactItem>();
	    		IndividualContactItem item = new IndividualContactItem();
	    		item.setIDCustomerAccountContact(response.getIndividualContactListItem().getIndividualContactItem().get(0).getIDCustomerAccountContact());    		
	    		list.add(item);    		
	    		output.setIndividualContactItem(list);
	    		output.setIdCustomer(response.getIDCustomer());  
	    		if (response.getCustomerAccountLocationListItem()!=null)
	    		{
	    			output.setIdGeographicLocation(response.getCustomerAccountLocationListItem().getCustomerAccountLocationItem().get(0).getIDGeographicLocation());
	    		}
	    	}
	    	log.trace("id geografic obtenido" + output.getIdGeographicLocation());
	    	return output;
	    }
	 
	 public static BigDecimal saveAdjunto(Connection conn,  String nombrArchivo,  BigDecimal idPortabilidad, String idPortaMovil, String Usuario, String IDAttachment)
	            throws SQLException {
	        PreparedStatement pstmt = null;
	        BigDecimal idAdjuntoPorta=null;
	        
	        String query = "INSERT INTO TC_SC_ADJUNTO_PORTA ("
			        + "tcscadjuntoportaid, "
			        + "tcscportabilidadid, "
			        + "idportamovil, "
			        + "nombre_archivo, "
			        + "extension, "
			        + "tipo_archivo, "
			        + "idattachment, "
			        + "creado_el, "
					+ "creado_por) VALUES (?, ?, ?, ?, ?, ?,?, SYSDATE, ?)";
	        try {
	             idAdjuntoPorta = JavaUtils.getSecuencia(conn, AdjuntoPorta.SEQUENCE);

	        

	            log.debug("QryInsertAdjunto: " + query);
	            log.trace("idAdjuntoPorta:"+idAdjuntoPorta);
	            log.trace("idPortabilidad:"+idPortabilidad);
	            log.trace("idPortaMovil:"+idPortaMovil);
	            log.trace("nombrArchivo:"+nombrArchivo);
	            log.trace("Usuario:"+Usuario);
	            log.trace("IDAttachment:"+IDAttachment);
	            
	            pstmt = conn.prepareStatement(query);
	            pstmt.setBigDecimal(1, idAdjuntoPorta);
	            pstmt.setBigDecimal(2, idPortabilidad);
	            pstmt.setString(3,idPortaMovil);
	            pstmt.setString(4, nombrArchivo);
	            pstmt.setString(5, "PDF");
	            pstmt.setString(6, "SOLICITUD PORTA");
	            pstmt.setBigDecimal(7, new BigDecimal(IDAttachment));
	            pstmt.setString(8, Usuario);
	         

	            int ret = pstmt.executeUpdate();

	            if (ret !=1) {
	            	 idAdjuntoPorta= new BigDecimal(0);
	            	 log.debug("id de tabal tc_sc_portabilidad "+ idPortabilidad);
	            	//enviar archvivo a repositorio 
	                 
	            }
	        } finally {
	            DbUtils.closeQuietly(pstmt);
	        }
	        return idAdjuntoPorta;
	    }

	 public static List<InputCargaAdjuntoPorta> getDocumentosAdicionales(Connection conn, String idPortalMovil, BigDecimal ID_PAIS) throws SQLException
	 {
		  List<InputCargaAdjuntoPorta> lstArchivos = new ArrayList<InputCargaAdjuntoPorta>();
		  PreparedStatement pstmt = null;
	      ResultSet rst = null;
	      String query="";
	      try
	      {
	    	  
	    	  query =" SELECT TCADJUNTO.TCSCADJUNTOID, TCADJUNTO.ARCHIVO, TCADJUNTO.NOMBRE_ARCHIVO,"
	    	  		+ " TCADJUNTO.TIPO_ARCHIVO, TCADJUNTOPORTA.TCSCADJUNTOPORTAID "
	    	  		+ " FROM TC_SC_ADJUNTO TCADJUNTO INNER JOIN TC_SC_ADJUNTO_PORTA TCADJUNTOPORTA ON  TCADJUNTO.IDPORTAMOVIL=TCADJUNTOPORTA.IDPORTAMOVIL"
	    	  		+ " WHERE TCADJUNTO.TCSCCATPAISID= ?"
	    	  		+ " AND TCADJUNTO.IDPORTAMOVIL= ?";
	    	  log.debug("Qry para buscar archivos adicionales para el proceso de portabilidad: " + query);
	    	  pstmt = conn.prepareStatement(query);
	    	  pstmt.setBigDecimal(1,  ID_PAIS);
	    	  pstmt.setBigDecimal(2, new BigDecimal(idPortalMovil));
	            rst = pstmt.executeQuery(); 
	            
	                if (rst.next()) {
	                    do {
	                    	InputCargaAdjuntoPorta archviosAdicionales = new InputCargaAdjuntoPorta();
	                    	archviosAdicionales.setTcscadjuntoid(rst.getString("TCSCADJUNTOID"));
	                    	archviosAdicionales.setArchivo(rst.getBytes("ARCHIVO"));	                    	                    	                                         
	                        lstArchivos.add(archviosAdicionales);
	                    } while (rst.next());
	            }
	      }
	     finally{
	    	 DbUtils.closeQuietly(rst);
	    	 DbUtils.closeQuietly(pstmt);
	     }
		  
		  return lstArchivos;
	 }
	 

	 
	 public static boolean delArchivo(Connection conn, BigDecimal idAdjunto, BigDecimal idPais) throws SQLException {
	        PreparedStatement pstmt = null;

	            String query = "DELETE FROM TC_SC_ADJUNTO" 
	                + " WHERE tcscadjuntoid = ?"
	                + " AND tcsccatpaisid = ?";
		    
	         try {
	            log.debug("QryDelImgPdv: " + query);
	            pstmt = conn.prepareStatement(query);
	            pstmt.setBigDecimal(1, idAdjunto);
	            pstmt.setBigDecimal(2, idPais);

	            int ret = pstmt.executeUpdate();
	            if (ret > 0) {
	                return true;
	            } else {
	                return false;
	            }
	        } finally {
	            DbUtils.closeQuietly(pstmt);
	        }
	    }

	 public static String getAddCustomerAttachment(Connection conn, String objectIDRootEntity, String nombreArchivo, String usuario, String area, String BusinessInteraction) throws MalformedURLException, SQLException
	 {
		
		 System.out.println("obtener IDAttachment servicio CustomerAttachment");
		 log.trace("obtener IDAttachment servicio CustomerAttachment" );
		 String output = "0";

		URL endpoint = new URL(UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_ADD_CUSTOMER_ATTACHMENT, area));
		AddCustomerAttachmentBindingQSService ws= new AddCustomerAttachmentBindingQSService(endpoint); 
		AddCustomerAttachmentV1 w=ws.getAddCustomerAttachmentBindingQSPort();
		AddCustomerAttachmentRequestType requestBody= new AddCustomerAttachmentRequestType();
		AddCustomerAttachmentResponseType response= new AddCustomerAttachmentResponseType();
		
		
		 log.trace("Agregamos documentos del cliente ws fs ");
		 try
		 {
			 requestBody.setObjectIDRootEntity(objectIDRootEntity);
			 requestBody.setFlagBusinessInteraction(BusinessInteraction);
			 requestBody.setFileNameDocument(nombreArchivo + ".pdf");
			 requestBody.setTypeNameDocument("pdf");
			 requestBody.setLocationAttachment(nombreArchivo);
			 
			 

			 Holder<HeaderOutType> hres=new Holder<HeaderOutType>();
			 HeaderInType h=FSUtil.instanciaHeader(usuario, area);
			 Gson gson = new GsonBuilder().create();
			 log.trace("BODY:"+ gson.toJson(requestBody));
			 
			 response=w.addCustomerAttachment(requestBody, h, hres);			 
		 }
		 catch(com.telefonica.globalintegration.services.soap.addcustomerattachment.v1.MessageFault e)
		 {
			 log.trace("Error al consumir ws osb " + e);
			 return output;	 
		 }
		 log.trace("se consumio ws product customer: " );
			Gson gson = new GsonBuilder().create();
			log.trace(gson.toJson(response));
		 if (response.getIDAttachment()!=null)
		 {
			 output= response.getIDAttachment().toString();
		 }
		 else
		 {
			 output="0";			 
		 }
		 
		 return output;
	 } 

	 public static String getCustomerById(Connection conn, String instanceIdProduct, String usuario, String area, String nuevoNumTemporal)throws MalformedURLException, SQLException
	{
		System.out.println("obtener Customer byid");
		log.trace("obtener Customer byid");

		String idProductoInstanciaSLO = "0";
		String numeroTemporal="";
		URL endpoint = new URL(UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_WS_PRODUCTO_BYID, area));
		GetProductCustomerAccountByIDBindingQSService ws = new GetProductCustomerAccountByIDBindingQSService(endpoint);
		GetProductCustomerAccountByIDV1 w = ws.getGetProductCustomerAccountByIDBindingQSPort();
		GetProductCustomerAccountByIDRequestType requestBody = new GetProductCustomerAccountByIDRequestType();
		GetProductCustomerAccountByIDResponseType response = new GetProductCustomerAccountByIDResponseType();
		log.trace("Consumiendo ws osb ByID Customer");
		try {
			
			numeroTemporal=nuevoNumTemporal.substring(0, 4)+" "+nuevoNumTemporal.substring(4, 8);
			log.trace("num temporal:"+numeroTemporal);
			
			requestBody.setInstanceIdProduct(instanceIdProduct);
			Holder<HeaderOutType> hres = new Holder<HeaderOutType>();
			HeaderInType h = FSUtil.instanciaHeader(usuario, area);
			
			Gson gson = new GsonBuilder().create();
			log.trace("BODY:"+ gson.toJson(requestBody));
			response = w.getProductCustomerAccountByID(requestBody, h, hres);
			
			log.trace("Se registro exitosamente:" + gson.toJson(response.getIDCustomer())) ;
		} catch (com.telefonica.globalintegration.services.soap.getproductcustomeraccountbyid.v1.MessageFault e) {
			idProductoInstanciaSLO = "0";
			log.trace("Error" + e);
		}
		if (response.getCustomerProductListItem()!=null) {
			CustomCustomerProductListType listaProductos = response.getCustomerProductListItem();
			List<CustomCustomerProductType> listadoSLOS = listaProductos.getCustomerProductItem();
			if (listadoSLOS != null && listadoSLOS.size() > 0) {

				for (CustomCustomerProductType s : listadoSLOS) {
					if (s.getNameProduct().trim().contains("Prepago - " + numeroTemporal)) {
						idProductoInstanciaSLO = s.getInstanceIdProduct();
						break;
					}
				}

			}
		}
	
		log.trace("verificamos respuesta del consumo ByID Customer" + idProductoInstanciaSLO);

		 return idProductoInstanciaSLO;
	 }
}

package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.solicitudescip.InputSolicitudesCip;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

import es.tid.spn.spnv.ws.server.tmsv.tmsvwebservice.EntidadLlamanteSolicitudTipo;
import es.tid.spn.spnv.ws.server.tmsv.tmsvwebservice.MsisdnNipSolicitudTipo;
import es.tid.spn.spnv.ws.server.tmsv.tmsvwebservice.RepuestaSolicitudNipTipo;
import es.tid.spn.spnv.ws.server.tmsv.tmsvwebservice.SolicitudNipTipo;
import es.tid.spn.spnv.ws.server.tmsv.tmsvwebservice.TMSVWebServiceImpl;
import es.tid.spn.spnv.ws.server.tmsv.tmsvwebservice.TMSVWebServiceImplService;



public class OperacionesCip  extends ControladorBase {
private static final Logger log = Logger.getLogger(OperacionFoliosSCL.class);
private static String servicioGet = Conf.LOG_GET_SOLICITUDES_CIP;	
	public static Respuesta doGet(Connection conn, InputSolicitudesCip input, BigDecimal idPais) throws MalformedURLException 
	{
    	//OBTENEMOS VALORES
		String operadorReceptro="";
		String RespuestaWS="";
		String TipoDocumento="";
		String estadoAlta="";
		String campo="";
		String noDocumento="";
	    Respuesta res=new Respuesta();
	    List<LogSidra> listaLog = new ArrayList<LogSidra>();
	    URL urls;
		try
		{
	    operadorReceptro=UtileriasJava.getConfig(conn, Conf.GRUPO_CONSULTA_CIP, Conf.OPERADOR_DONANTE, input.getCodArea());
		RespuestaWS=UtileriasJava.getConfig(conn, Conf.GRUPO_CONSULTA_CIP, Conf.RESPUESTA_CERVICIO, input.getCodArea());
	    SolicitudNipTipo objSolNipTipo = new SolicitudNipTipo();
	   
	    campo=Catalogo.CAMPO_VALOR;
	    //validamos el tipo documento
	    estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
    	List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE, input.getTipoDocumento()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.GRUPO_TIPO_DOCUMENTO));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));	         
        TipoDocumento = UtileriasBD.getOneRecord(conn, campo, Catalogo.N_TABLA, condiciones);

	    //creando objetos de parametros de entrada
		 MsisdnNipSolicitudTipo numero= new MsisdnNipSolicitudTipo();
		 EntidadLlamanteSolicitudTipo objEntLlamante = new EntidadLlamanteSolicitudTipo();
		 
		 /*Asignando valores a objeto SolicitudNipTipo que */
		 noDocumento=input.getNumeroDocumento().replace("-", "");
		 objSolNipTipo.setNumeroIdentificacion(noDocumento);		 
		 numero.setMsisdn(input.getNumeroaPortar());
		 objSolNipTipo.getListaMsisdnNipSolicitud().add(numero);		
		 objSolNipTipo. setOperadorReceptor(operadorReceptro);
		 objSolNipTipo.setTipoIdentificacion(TipoDocumento);		

		 /* Asignando valores a objeto EntidadLlamanteSolicitudTipo */
		 objEntLlamante.setUsuario(input.getUsuario());
		 objEntLlamante.setIdSistema("1");
		 objEntLlamante.setIdExterno("0");
						
			urls = new URL(UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_SOLICITUDES_CIP, input.getCodArea())); 
			log.trace("URL OBTENIDA:"+ urls);
			  			
			 TMSVWebServiceImplService wsSPN = new TMSVWebServiceImplService(urls);
			 TMSVWebServiceImpl WS= wsSPN.getTMSVWebServiceImplPort();			 
			 
			 RepuestaSolicitudNipTipo RestpuestaServicio= WS.spnAlimSolicitudNip(objSolNipTipo, objEntLlamante);	          
	          log.trace("RESULTADO SERVICIO:"+RestpuestaServicio.getCodigoError());
			  log.trace("RESULTADO DESCRIPCION ERROR:"+RestpuestaServicio.getDescError());
			  log.trace("ID SOLICITUD:"+ RestpuestaServicio.getIdNip());			  			  
			
			  res.setDescripcion(RestpuestaServicio.getCodigoError());
			  long sCodigoCip =RestpuestaServicio.getIdNip();
			  //Verificar Respuesta
			  if( res.getDescripcion().equalsIgnoreCase(RespuestaWS)){
				  listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, input.getNumeroaPortar(), Conf.LOG_TIPO_NINGUNO,
		                    "Se consulto corectamente codigo CIP. "+ sCodigoCip , null));
			
			  }else{
				  listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, input.getNumeroaPortar(), Conf.LOG_TIPO_NINGUNO,
		                    "No se pudo obtener codigo CIP. " , null));
				  res.setDescripcion(RestpuestaServicio.getDescError());
			  }
		}catch(Exception e)
		{
			 log.error(e.getMessage(), e);
			 
	            listaLog = new ArrayList<LogSidra>();
	            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, input.getNumeroaPortar(),
	                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar CIP en WS de FS.", e.getMessage()));
	            
		}
		UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

		 return res;
 
	}
}

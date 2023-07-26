package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.portabilidad.InputEstadoPortabilidad;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.portabilidad.OutputEstadoPortabilidad;
import com.consystec.sc.ca.ws.output.portabilidad.VentaPorta;
import com.consystec.sc.sv.ws.operaciones.OperacionEstadoPorta;
import com.consystec.sc.sv.ws.orm.Portabilidad;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlEstadoPorta extends ControladorBase {
	private static final Logger log = Logger.getLogger(CtrlEstadoPorta.class);
	private static String servicioGet =Conf.SERVICIO_LOCAL_ESTADO_PORTA;
	
	public Respuesta validarInput(Connection conn, InputEstadoPortabilidad input){
		 String nombreMetodo = "validarInput";
	     String nombreClase = new CurrentClassGetter().getClassName();
	     Respuesta r = new Respuesta();
	        String datosErroneos = "";
	        boolean flag = false;
		 
	        if (input.getCodArea() == null || "".equals(input.getCodArea())) {
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_303, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        } else if (input.getCodArea().length() != 3){
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_LONG_304, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        } else if (!isNumeric(input.getCodArea())) {
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_NUM_305, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        }
	        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
	            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase,
	                    nombreMetodo, null, input.getCodArea()).getDescripcion();
	            flag = true;
	        }
	       
	        if (input.getCodDispositivo()==null || "".equals(input.getCodDispositivo()))
	        {
	        	datosErroneos += getMensaje(Conf_Mensajes.MSJ_CODDISPOSITIVO_NULO_67, null, this.getClass().toString(),
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
	        	flag = true;
	        }
	    
	        if (flag) {
	        	 r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos, input.getCodArea());
	        } else {
	            r.setDescripcion("OK");
	            r.setCodResultado("1");
	            r.setMostrar("0");
	        }
		 return r;
		
	}
	
	public OutputEstadoPortabilidad getDatos(InputEstadoPortabilidad input) {
		List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = new Respuesta();
        Respuesta r = new Respuesta();
        OutputEstadoPortabilidad output = new OutputEstadoPortabilidad();
        Connection conn = null;
        Connection connPorta = null;
        List<VentaPorta> detalles =new ArrayList<VentaPorta>();
        VentaPorta detalle=new VentaPorta();
        HashMap <String, String> estadosPorta = new  HashMap <String, String>();
        HashMap <String, String> grupoEstados = new  HashMap <String, String>();
        List<Portabilidad> datosPorta = new ArrayList< Portabilidad>();
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        String estadoAlta="";
        String llave="";
        String datos="";
        try
        {
        	
        	 connPorta=getConnPorta();
        	 conn=getConnRegional();
        	
        	 BigDecimal idPais = getIdPais(conn, input.getCodArea());
        	 respuesta = validarInput(conn, input);
        	 r=validarFechas(input.getFechaInicio(), input.getFechaFin(),true, 7, nombreMetodo, input.getCodArea());
        	 if (!"OK".equals(respuesta.getDescripcion())) {
                 output.setRespuesta(respuesta);
                 listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));
                 return output;
             }	else if (r!=null){
            	  output.setRespuesta(r);
            	  listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));
                  return output;
             }
        	  estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
        	 lstFiltros=getFiltros(input, idPais);
        	 grupoEstados = getGrupoParam(conn, estadoAlta, Conf.GRUPO_ESTADO_NUM_PORTA, idPais);
        	 datosPorta=OperacionEstadoPorta.getDatosPorta(conn, lstFiltros);
        	
        	 if (datosPorta==null || datosPorta.isEmpty()){
        		 respuesta= getMensaje(Conf_Mensajes.MSJ_NO_HAY_ESTADOS_PORTA_919, null, nombreClase, nombreMetodo, null, input.getCodArea());
        		 output.setRespuesta(respuesta);
        	 }else{
        		 datos=OperacionEstadoPorta.varNum.substring(0,(OperacionEstadoPorta.varNum.length()-1 ));
	        	 estadosPorta=OperacionEstadoPorta.getEstado(connPorta,datos );
	        	 if (estadosPorta==null || estadosPorta.isEmpty())
	        	 {
	        		respuesta= getMensaje(Conf_Mensajes.MSJ_NO_HAY_ESTADOS_PORTA_919, null, nombreClase, nombreMetodo, null, input.getCodArea());
	        		output.setRespuesta(respuesta);
	        		
	        	 }
	        	 else
	        	 {
	        		 for (int a=0; a<datosPorta.size(); a++){
	 	        		detalle= new VentaPorta();
	 	        		llave=estadosPorta.get(datosPorta.get(a).getNum_portar().toString());
	 	        		log.trace("llave:"+llave);
	 	        		if(llave!=null)
	 	        		{
	 	        			detalle.setEstado(grupoEstados.get(llave.toUpperCase()));
		 	        		log.trace("estadoPorta:"+detalle.getEstado());
		 	        		detalle.setIdVenta(""+datosPorta.get(a).getIdportamovil());
		 	        		detalle.setNumeroaPortar(""+datosPorta.get(a).getNum_portar().toString());
		 	        		detalle.setNumeroTemporal(""+datosPorta.get(a).getNum_temporal().toString());
		 	        		detalle.setOperadorDonante(datosPorta.get(a).getOperador_donante());
		 	        		detalles.add(detalle);	
	 	        		}
	 	        		
	 	        	 }
	        		 respuesta= getMensaje(Conf_Mensajes.OK_SOLICITUDES_OBTENIDAS_506, null, nombreClase, nombreMetodo, null, input.getCodArea());
	        		 output.setRespuesta(respuesta);
	 	        	output.setVentaportadetalle(detalles); 
	        	 }
	        	 
        	 }
        }
        catch(Exception e)
        {
        	respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
            log.error("Excepcion: " + e.getMessage(), e);
            output.setRespuesta(respuesta);
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet , "0", Conf.LOG_TIPO_NINGUNO, "Problema al consultar estado de portabilidad.", e.getMessage()));
        }
        finally
        {
        	log.trace("Descripcion: " + output.getRespuesta());
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        	 DbUtils.closeQuietly(conn);
        	 DbUtils.closeQuietly(connPorta);
        	
        }
        return output;
	}
	
	
	/**
     * Este m\u00E9todo recibe dos fechas, inicio y fin, realiza validaciones sobre las fechas 
     * y si no cumpliece una retornaria un objeto Respuesta
     * 
     * @param inicio
     * @param fin
     * @param diferencia
     * @param metodo
     * 
     * @return 
     * */
    private Respuesta validarFechas(String inicio, String fin, boolean diferencia, int numeroDif, String metodo, String codPais)
            throws Exception {
        Respuesta resultado = null;

        if (!(inicio == null || "".equals(inicio.trim())) && !(fin == null || "".equals(fin.trim()))) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(inicio, formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(fin, formatoFecha);

            if (fechaFin.before(fechaInicio)) {
                resultado = getMensaje(Conf_Mensajes.MSJ_FECHAFIN_MENOR_62, null, this.getClass().toString(), metodo,
                        null, codPais);
            } else {
                if (diferencia) {
                    long diferenciaDias = fechaFin.getTime() - fechaInicio.getTime();
                    long dias = diferenciaDias / (1000 * 60 * 60 * 24);
                    int totalDias = (int) dias;
                    if (totalDias > numeroDif) {
                        resultado = getMensaje(Conf_Mensajes.MSJ_RANGO_FECHAS_GETPORTA_694, null,
                                this.getClass().toString(), metodo, null, codPais);
                    }
                }
            }
        } else if ((inicio == null || "".equals(inicio.trim())) && (fin == null || "".equals(fin.trim()))) {
            resultado = getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, this.getClass().toString(), metodo, null, codPais);
        }

        // validando rango
        if (!(inicio == null || "".equals(inicio.trim())) && !(fin == null || "".equals(fin.trim()))) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(inicio, formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(fin, formatoFecha);

            if (fechaFin.before(fechaInicio)) {
                resultado = getMensaje(Conf_Mensajes.MSJ_FECHAFIN_MENOR_62, null, this.getClass().toString(), metodo,
                        null, codPais);
            }
        } else if ((inicio == null || "".equals(inicio.trim())) && (fin == null || "".equals(fin.trim()))) {
            resultado = getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, this.getClass().toString(), metodo, null, codPais);
        }

        return resultado;
    }
    
    
    /**
     * M\u00E9todo para obtener los posibles filtros que se aplicaran en la consulta de estado portabilidad
     * 
     * @param objDatos
     * @return List<Filtro>
     */
    public List<Filtro> getFiltros(InputEstadoPortabilidad objDatos, BigDecimal idPais) {
    	List<Filtro> lstFiltros = new ArrayList<Filtro>();
    	SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA);
    	if (!(objDatos.getIdJornada() == null || "".equals(objDatos.getIdJornada().trim()))) {
			log.trace("entra a filtro IDJORNADA");
			lstFiltros.add(new Filtro( Portabilidad.CAMPO_TCSCJORNADAVENID, Filtro.EQ, objDatos.getIdJornada()));
		}

    	lstFiltros.add(new Filtro(Portabilidad.CAMPO_TCSCCATPAISID, Filtro.EQ, idPais));
    	
    	if (!(objDatos.getIdVendedor() == null || "".equals(objDatos.getIdVendedor().trim()))) {
			log.trace("entra a filtro ID VENDEDOR");
			lstFiltros.add(new Filtro(Portabilidad.CAMPO_ID_VENDEDOR, Filtro.EQ, objDatos.getIdVendedor()));
		}
    	
    	if (!(objDatos.getNumeroaPortar() == null || "".equals(objDatos.getNumeroaPortar().trim()))) {
			log.trace("entra a filtro numero a portar");
			lstFiltros.add(new Filtro( Portabilidad.CAMPO_NUM_PORTAR, Filtro.EQ, objDatos.getNumeroaPortar()));
		}
    	
    	if (!(objDatos.getNumeroTemporal() == null || "".equals(objDatos.getNumeroTemporal().trim()))) {
			log.trace("entra a filtro ID_DISTRIBUIDOR");
			lstFiltros.add(new Filtro( Portabilidad.CAMPO_NUM_TEMPORAL, Filtro.EQ, objDatos.getNumeroTemporal()));
		}
    	
    	if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && !(objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            log.trace("entra a filtro FECHAS");
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(objDatos.getFechaFin(), formatoFecha);
            lstFiltros.add(new Filtro("TRUNC(" + Portabilidad.CAMPO_CREADO_EL + ")", Filtro.GTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaInicio) + "','dd/MM/YYYY')"));
            lstFiltros.add(new Filtro("TRUNC(" + Portabilidad.CAMPO_CREADO_EL + ")", Filtro.LTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaFin) + "','dd/MM/YYYY')"));
        }
    	
    	return lstFiltros;
    }
}

package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCumplimientoVenta;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCumplimientoVisita;
import com.consystec.sc.ca.ws.input.reporte.InputReporteEfectividadVenta;
import com.consystec.sc.ca.ws.input.reporte.InputReporteInventarioVendido;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.reporte.CumplimientoVenta;
import com.consystec.sc.ca.ws.output.reporte.CumplimientoVisita;
import com.consystec.sc.ca.ws.output.reporte.DatosEfectividad;
import com.consystec.sc.ca.ws.output.reporte.DetalleArticulo;
import com.consystec.sc.ca.ws.output.reporte.DetalleVendido;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCumplimientoVenta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCumplimientoVisita;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteEfectividadVenta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteInventarioVendido;
import com.consystec.sc.ca.ws.output.reporte.OutputResumenInventarioVendido;
import com.consystec.sc.sv.ws.operaciones.OperacionReporte;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.orm.Venta;
import com.consystec.sc.sv.ws.orm.Visita;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Consystec - Jairo Yupe
 *
 */
public class CtrlReporte extends ControladorBase {
	private static final Logger log = Logger.getLogger(CtrlReporte.class);
    private static String servicioGet = Conf.LOG_GET_REPORTE;
    SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA);
    /**
     * M\u00E9todo para obtener los posibles filtros que se aplicaran en la consulta de Cumplimiento Visitas
     * 
     * @param objDatos
     * @return List<Filtro>
     */
    public List<Filtro> getFiltrosCumplVisita(InputReporteCumplimientoVisita objDatos, BigDecimal ID_PAIS) {
    	List<Filtro> lstFiltros = new ArrayList<Filtro>();
    	
    	if (!(objDatos.getIdDistribuidor() == null || "".equals(objDatos.getIdDistribuidor().trim()))) {
    		log.trace("entra a filtro ID_DISTRIBUIDOR");
    		lstFiltros.add(new Filtro("B." + Distribuidor.CAMPO_TC_SC_DTS_ID, Filtro.EQ, objDatos.getIdDistribuidor()));
		}
    	
    	if (!(objDatos.getIdRuta() == null || "".equals(objDatos.getIdRuta().trim()))) {
    		log.trace("entra a filtro ID_RUTA");
    		lstFiltros.add(new Filtro("E." + Ruta.CAMPO_TC_SC_RUTA_ID, Filtro.EQ, objDatos.getIdRuta()));
		}
    	
    	if (!(objDatos.getIdPuntoVenta() == null || "".equals(objDatos.getIdPuntoVenta().trim()))) {
    		log.trace("entra a filtro ID_PUNTOVENTA");
    		lstFiltros.add(new Filtro("A." + PuntoVenta.CAMPO_TCSCPUNTOVENTAID, Filtro.EQ, objDatos.getIdPuntoVenta()));
		}
    	
    	lstFiltros.add(new Filtro("A." + PuntoVenta.CAMPO_TCSCCATPAISID, Filtro.EQ, ID_PAIS));
    	
        if (!(objDatos.getDepartamento() == null || "".equals(objDatos.getDepartamento().trim()))) {
            log.trace("entra a filtro DEPARTAMENTO");
            lstFiltros.add(new Filtro("UPPER(A." + PuntoVenta.CAMPO_DEPARTAMENTO + ")", Filtro.EQ, "'" + objDatos.getDepartamento().toUpperCase() + "'"));
        }

        if (!(objDatos.getMunicipio() == null || "".equals(objDatos.getMunicipio().trim()))) {
            log.trace("entra a filtro MUNICIPIO");
            lstFiltros.add(new Filtro("UPPER(A." + PuntoVenta.CAMPO_MUNICIPIO + ")", Filtro.EQ, "'" + objDatos.getMunicipio().toUpperCase() + "'"));
        }

        if (!(objDatos.getDistrito() == null || "".equals(objDatos.getDistrito().trim()))) {
            log.trace("entra a filtro DISTRITO");
            lstFiltros.add(new Filtro("UPPER(A." + PuntoVenta.CAMPO_DISTRITO + ")", Filtro.EQ, "'" + objDatos.getDistrito().toUpperCase() + "'"));
        }

    	if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && !(objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            log.trace("entra a filtro FECHAS");
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(objDatos.getFechaFin(), formatoFecha);
            lstFiltros.add(new Filtro("TRUNC(F." + Visita.CAMPO_FECHA + ")", Filtro.GTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaInicio) + "','dd/MM/YYYY')"));
            lstFiltros.add(new Filtro("TRUNC(F." + Visita.CAMPO_FECHA + ")", Filtro.LTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaFin) + "','dd/MM/YYYY')"));
        }
    	
    	return lstFiltros;
    }
    
    /**
     * M\u00E9todo para obtener los posibles filtros que se aplicaran en la consulta de Cumplimiento Ventas
     * 
     * @param objDatos
     * @return List<Filtro>
     */
    public List<Filtro> getFiltrosCumplVenta(InputReporteCumplimientoVenta objDatos, BigDecimal ID_PAIS) {
    	List<Filtro> lstFiltros = new ArrayList<Filtro>();
    	
    	if (!(objDatos.getIdPuntoVenta() == null || "".equals(objDatos.getIdPuntoVenta().trim()))) {
			log.trace("entra a filtro ID_PUNTO_VENTA");
			lstFiltros.add(new Filtro("A." + PuntoVenta.CAMPO_TCSCPUNTOVENTAID, Filtro.EQ, objDatos.getIdPuntoVenta()));
		}

    	lstFiltros.add(new Filtro("A." + PuntoVenta.CAMPO_TCSCCATPAISID, Filtro.EQ, ID_PAIS));
    	
    	if (!(objDatos.getIdDistribuidor() == null || "".equals(objDatos.getIdDistribuidor().trim()))) {
			log.trace("entra a filtro ID_DISTRIBUIDOR");
			lstFiltros.add(new Filtro("B." + Distribuidor.CAMPO_TC_SC_DTS_ID, Filtro.EQ, objDatos.getIdDistribuidor()));
		}
    	
    	if (!(objDatos.getIdJornada() == null || "".equals(objDatos.getIdJornada().trim()))) {
			log.trace("entra a filtro ID_JORNADA");
			lstFiltros.add(new Filtro("F." + Jornada.CAMPO_TCSCJORNADAVENID, Filtro.EQ, objDatos.getIdJornada()));
		}
    	
    	if (!(objDatos.getIdRuta() == null || "".equals(objDatos.getIdRuta().trim()))) {
			log.trace("entra a filtro ID_DISTRIBUIDOR");
			lstFiltros.add(new Filtro("C." + Ruta.CAMPO_TC_SC_RUTA_ID, Filtro.EQ, objDatos.getIdRuta()));
		}
    	
    	if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && !(objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            log.trace("entra a filtro FECHAS");
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(objDatos.getFechaFin(), formatoFecha);
            lstFiltros.add(new Filtro("TRUNC(F." + Venta.CAMPO_FECHA_EMISION + ")", Filtro.GTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaInicio) + "','dd/MM/YYYY')"));
            lstFiltros.add(new Filtro("TRUNC(F." + Venta.CAMPO_FECHA_EMISION + ")", Filtro.LTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaFin) + "','dd/MM/YYYY')"));
        }
    	
    	return lstFiltros;
    }
    
    /**
     * M\u00E9todo para obtener los posibles filtros que se aplicaran en la consulta de Inventario Vendido
     * 
     * @param objDatos
     * @return List<Filtro>
     */
    public List<Filtro> getFiltrosInventVendido(InputReporteInventarioVendido objDatos) {
    	List<Filtro> lstFiltros = new ArrayList<Filtro>();
    	

    	
    	if (!(objDatos.getIdBodegaVirtual() == null || "".equals(objDatos.getIdBodegaVirtual().trim()))) {
			log.trace("entra a filtro ID_BODEGA_VIRTUAL");
			lstFiltros.add(new Filtro("B." + BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, Filtro.EQ, objDatos.getIdBodegaVirtual()));
		}
    	

    	
    	if (!(objDatos.getIdVendedor() == null || "".equals(objDatos.getIdVendedor().trim()))) {
			log.trace("entra a filtro ID_VENDEDOR");
			lstFiltros.add(new Filtro("A." + Venta.CAMPO_VENDEDOR, Filtro.EQ, objDatos.getIdVendedor()));
		}

    

    	if (!(objDatos.getIdTipo() == null || "".equals(objDatos.getIdTipo().trim()))) {
			log.trace("entra a filtro ID_TIPO");
			lstFiltros.add(new Filtro("C." + Jornada.CAMPO_IDTIPO, Filtro.EQ, objDatos.getIdTipo()));
		}
    	
    	if (!(objDatos.getTipo() == null || "".equals(objDatos.getTipo().trim()))) {
			log.trace("entra a filtro TIPO");
			lstFiltros.add(new Filtro("C." + Jornada.CAMPO_DESCRIPCION_TIPO, Filtro.EQ, "'" + objDatos.getTipo() + "'"));
		}
    	
    	if (!(objDatos.getFechaInicio() == null || "".equals(objDatos.getFechaInicio().trim()))
                && !(objDatos.getFechaFin() == null || "".equals(objDatos.getFechaFin().trim()))) {
            log.trace("entra a filtro FECHAS");
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFechaInicio(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(objDatos.getFechaFin(), formatoFecha);
            lstFiltros.add(new Filtro("TRUNC(A." + Venta.CAMPO_FECHA_EMISION + ")", Filtro.GTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaInicio) + "','dd/MM/YYYY')"));
            lstFiltros.add(new Filtro("TRUNC(A." + Venta.CAMPO_FECHA_EMISION + ")", Filtro.LTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaFin) + "','dd/MM/YYYY')"));
        }
    	
    	return lstFiltros;
    }
    
    /**
     * M\u00E9todo para obtener los posibles filtros que se aplicaran en la consulta de Efectividad de Venta
     * 
     * @param objDatos
     * @return List<Filtro>
     */
    public List<Filtro> getFiltrosEfectividadVenta(InputReporteEfectividadVenta objDatos) {
    	List<Filtro> lstFiltros = new ArrayList<Filtro>();
    	
    	if (!(objDatos.getIdDistribuidor() == null || "".equals(objDatos.getIdDistribuidor().trim()))) {
			log.trace("entra a filtro ID_DISTRIBUIDOR");
			lstFiltros.add(new Filtro(Distribuidor.CAMPO_TC_SC_DTS_ID, Filtro.EQ, objDatos.getIdDistribuidor()));
		}
    	
    	if (!(objDatos.getIdBodegaVirtual() == null || "".equals(objDatos.getIdBodegaVirtual().trim()))) {
			log.trace("entra a filtro ID_BODEGAVIRTUAL");
			lstFiltros.add(new Filtro(BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, Filtro.EQ, objDatos.getIdBodegaVirtual()));
		}
    	
    	if (!(objDatos.getIdRuta() == null || "".equals(objDatos.getIdRuta().trim()))) {
			log.trace("entra a filtro ID_RUTA");
			lstFiltros.add(new Filtro(Ruta.CAMPO_TC_SC_RUTA_ID, Filtro.EQ, objDatos.getIdRuta()));
		}
    	
    	return lstFiltros;
    }
    
    /**
     * M\u00E9todo para obtener los posibles filtros que se aplicaran en las consultas de Efectividad de Venta 
     * pero la principal funcion es retornar filtros de fechas.
     * 
     * @param objDatos
     * @return List<Filtro>
     */
    public List<Filtro> getFiltrosFechaEfectividad(InputReporteEfectividadVenta objDatos, String idVenta, BigDecimal ID_PAIS) {
    	List<Filtro> lstFiltros = new ArrayList<Filtro>();
    	
    	if (!(idVenta == null || "".equals(idVenta))) {
			log.trace("entra a filtro ID_VENTA");
			lstFiltros.add(new Filtro("A." + Venta.CAMPO_TCSCVENTAID, Filtro.EQ, Filtro.EQ, idVenta));
		}

    	lstFiltros.add(new Filtro("A." + Venta.CAMPO_TCSCCATPAISID, Filtro.EQ, ID_PAIS));
    	
    	if (!(objDatos.getFechaInicioMesActual() == null || "".equals(objDatos.getFechaInicioMesActual().trim()))
                && !(objDatos.getFechaFinMesActual() == null || "".equals(objDatos.getFechaFinMesActual().trim()))) {
            log.trace("entra a filtro FECHAS");
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(objDatos.getFechaInicioMesActual(), formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(objDatos.getFechaFinMesActual(), formatoFecha);
            lstFiltros.add(new Filtro("TRUNC(A." + Venta.CAMPO_FECHA_EMISION + ")", Filtro.GTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaInicio) + "','dd/MM/YYYY')"));
            lstFiltros.add(new Filtro("TRUNC(A." + Venta.CAMPO_FECHA_EMISION + ")", Filtro.LTE,
                    "TO_DATE('" + FORMATO_FECHA.format(fechaFin) + "','dd/MM/YYYY')"));
        }
    	
    	return lstFiltros;
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
    private Respuesta validarFechas(String inicio, String fin, boolean diferencia, int numeroDif, String metodo, String codArea)
            throws Exception {
        Respuesta resultado = null;

        if (!(inicio == null || "".equals(inicio.trim())) && !(fin == null || "".equals(fin.trim()))) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(inicio, formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(fin, formatoFecha);

            if (fechaFin.before(fechaInicio)) {
                resultado = getMensaje(Conf_Mensajes.MSJ_FECHAFIN_MENOR_62, null, this.getClass().toString(), metodo,
                        null, codArea);
            } else {
                if (diferencia) {
                    long diferenciaDias = fechaFin.getTime() - fechaInicio.getTime();
                    long dias = diferenciaDias / (1000 * 60 * 60 * 24);
                    int totalDias = (int) dias;
                    if (totalDias > numeroDif) {
                        resultado = getMensaje(Conf_Mensajes.MSJ_RANGO_FECHA_INVALIDO_511, null,
                                this.getClass().toString(), metodo, null, codArea);
                    }
                }
            }
        } else if ((inicio == null || "".equals(inicio.trim())) && (fin == null || "".equals(fin.trim()))) {
            resultado = getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, this.getClass().toString(), metodo, null, codArea);
        }

        // validando rango
        if (!(inicio == null || "".equals(inicio.trim())) && !(fin == null || "".equals(fin.trim()))) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
            Date fechaInicio = UtileriasJava.parseDate(inicio, formatoFecha);
            Date fechaFin = UtileriasJava.parseDate(fin, formatoFecha);

            if (fechaFin.before(fechaInicio)) {
                resultado = getMensaje(Conf_Mensajes.MSJ_FECHAFIN_MENOR_62, null, this.getClass().toString(), metodo,
                        null, codArea);
            }
        } else if ((inicio == null || "".equals(inicio.trim())) && (fin == null || "".equals(fin.trim()))) {
            resultado = getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, this.getClass().toString(), metodo, null, codArea);
        }

        return resultado;
    }
    
    /**
     * Este m\u00E9todo recibe fechas, inicio y fin de dos meses distintos, realiza validaciones sobre las fechas 
     * y si no cumpliece una retornaria un objeto Respuesta
     * 
     * @param inicio
     * @param fin
     * @param diferencia
     * @param metodo
     * 
     * @return 
     * */
    private Respuesta validarOrdenDeFechas(String iActual, String fActual, String iAntes, String fAntes, String metodo, String codArea) throws Exception {
    	Respuesta resultado = null;
    	boolean val1=!(fActual == null || "".equals(fActual.trim()));
    	boolean val2=!(iAntes == null || "".equals(iAntes.trim()));
    	boolean val3=!(fAntes == null || "".equals(fAntes.trim()));
    	if (!(iActual == null || "".equals(iActual.trim()))
                && val1 && val2 && val3) {
    		
    		//Validar inicio y fin solo de un mes
    		resultado = validarFechas(iActual, fActual, true, 31, metodo, codArea);
            if (resultado != null) {
                return resultado;
            } else {
                resultado = validarFechas(iAntes, fAntes, true, 31, metodo, codArea);
                if (resultado != null)
                    return resultado;
            }

            SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);

            Date fechaInicioActual = UtileriasJava.parseDate(iActual, formatoFecha);
            Date fechaFinActual = UtileriasJava.parseDate(fActual, formatoFecha);

            Date fechaInicioAntes = UtileriasJava.parseDate(iAntes, formatoFecha);
            Date fechaFinAntes = UtileriasJava.parseDate(fAntes, formatoFecha);

            if (fechaInicioActual.before(fechaInicioAntes)) {
                return  getMensaje(Conf_Mensajes.MSJ_FECHA_MESANTERIOR_INVALIDA_521, null,
                        this.getClass().toString(), metodo, null, codArea);
            }

            if (fechaFinActual.before(fechaFinAntes)) {
                return  getMensaje(Conf_Mensajes.MSJ_FECHA_MESANTERIOR_INVALIDA_521, null,
                        this.getClass().toString(), metodo, null, codArea);
            }

        } else {
            resultado = getMensaje(Conf_Mensajes.MSJ_FECHAS_NULAS_61, null, this.getClass().toString(), metodo, null, codArea);
        }

        return resultado;
    }
    
    /**
     * Este m\u00E9todo recibe dos cantidades, max y min, para validar que son n\u00FAmeros correctos
     * y que sen validos para utilizar en un query para realizar pagineo 
     * 
     * @param min
     * @param max
     * 
     * @return 
     * */
    private Respuesta validarCantidadPagineo(String min, String max, int tipoConsulta, String codArea) throws SQLException, Exception {
    	String metodo = "validarCantidadPagineo";
    	Respuesta resultado = null;
    	Connection conn = null;
   // 	
    	if(tipoConsulta == 1){
	        if(min == null || "".equals(min.trim())){
	            resultado = getMensaje(Conf_Mensajes.MSJ_MIN_NULO_512, null, this.getClass().toString(), metodo,
	                    null, codArea);
	        }else{
	        	 if(!isNumeric(min)) {
	    	            resultado = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MINIMO_198, null, this.getClass().toString(), metodo,
	    	                    null, codArea);
	        	 }
	        }
	        
	        
	        if(max == null || "".equals(max.trim())){
	            resultado = getMensaje(Conf_Mensajes.MSJ_MAX_NULO_513, null, this.getClass().toString(), metodo,
	                    null, codArea);
	        }else{
	        	if(!isNumeric(max)) {
		            resultado = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MAXIMO_217, null, this.getClass().toString(), metodo,
		                    null, codArea);
	        	 }
	        }
	        	
	        
	        if(!(min == null || "".equals(min.trim())) && !(max == null || "".equals(max.trim()))){
	        	try{
		        	BigDecimal minimo=null;
		        	BigDecimal maximo=null;
		        	String registrosMaximos="";
		        	
		        			
		        	minimo=new BigDecimal(min);
		        	maximo=new BigDecimal(max);
		        	
		        	conn=getConnRegional();
		        	registrosMaximos = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.REGISTROS_MAXIMOS, codArea);
		        
		        	if(minimo.intValue()>maximo.intValue()){
			            resultado = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MINIMO_MAYOR_218, null, this.getClass().toString(), metodo,
			                    null, codArea);
		        	}else if((maximo.intValue()-minimo.intValue())>new Integer(registrosMaximos)){
			            resultado = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_RANGO_MAX_219, null, this.getClass().toString(), metodo,
			            		registrosMaximos, codArea);
		        	}
	        	}finally{
	        		DbUtils.closeQuietly(conn);
	        	}
	        }
        }
    	return resultado;
    }
    
    /**
     * M\u00E9todo para obtener el listado de registros de cumplimiento de visitas encontrados en la base de datos.
     * @param objDatos
     * @return OutputReporteCumplimientoVisita
     */
    public OutputReporteCumplimientoVisita getReporteCumplimientoVisita(InputReporteCumplimientoVisita objDatos) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
    	OutputReporteCumplimientoVisita objReporte = new OutputReporteCumplimientoVisita();
    	Respuesta objRespuesta = null;
    	List<CumplimientoVisita> lstVisitas = new ArrayList<CumplimientoVisita>();
    	List<Filtro> lstFiltros = new ArrayList<Filtro>();
        String metodo = "getReporteCumplimientoVisita";
        Connection conn = null;
    	
        try {
        	objRespuesta = validarFechas(objDatos.getFechaInicio(), objDatos.getFechaFin(), true, 31, metodo, objDatos.getCodArea());

        	if (objRespuesta == null) {

        		conn = getConnRegional();
                BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
        		lstFiltros = getFiltrosCumplVisita(objDatos, ID_PAIS);
        		lstVisitas = OperacionReporte.getDatosCumplimientoVisita(conn, lstFiltros, objDatos.getCodArea(), ID_PAIS);
        		if (lstVisitas.isEmpty()) {
        			objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
        					null, objDatos.getCodArea());
        		} else {
        			objReporte.setCumplVisita(lstVisitas);
        			objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
        		}

        		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        				Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de Reporte Cumplimiento Visita de sidra.", ""));

        	} else {
        		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        				Conf.LOG_TIPO_NINGUNO, "Problema al consultar Reporte Cumplimiento Visita.", objRespuesta.getDescripcion()));
        		UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        	}
        } catch (SQLException e) {
        	objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
        	log.error(e.getMessage(), e);

        	listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        			Conf.LOG_TIPO_NINGUNO, "Problema al consultar Reporte Cumplimiento Visita.", e.getMessage()));
        } catch (Exception e) {
        	objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
        			metodo, "", objDatos.getCodArea());
        	log.error(e.getMessage(), e);

        	listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        			Conf.LOG_TIPO_NINGUNO, "Problema al consultar Reporte Cumplimiento Visita.", e.getMessage()));
        } finally {
        	DbUtils.closeQuietly(conn);

        	UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }
        
        objReporte.setRespuesta(objRespuesta);

        return objReporte;
    }
    
    /**
     * M\u00E9todo para obtener el listado de registros de cumplimiento de ventas encontrados en la base de datos.
     * @param objDatos
     * @return OutputReporteCumplimientoVenta
     */
    public OutputReporteCumplimientoVenta getReporteCumplimientoVenta(InputReporteCumplimientoVenta objDatos) {
    	List<LogSidra> listaLog = new ArrayList<LogSidra>();
    	OutputReporteCumplimientoVenta objReporte = new OutputReporteCumplimientoVenta();
    	Respuesta objRespuesta = null;
    	List<CumplimientoVenta> lstVentas = new ArrayList<CumplimientoVenta>();
    	List<Filtro> lstFiltros = new ArrayList<Filtro>();
        String metodo = "getReporteCumplimientoVenta";
        Connection conn = null;
    	
        try {
        	//Validaciones de parametros
        	objRespuesta = validarFechas(objDatos.getFechaInicio(), objDatos.getFechaFin(), false, 31, metodo, objDatos.getCodArea());
        	objRespuesta = validarCantidadPagineo(objDatos.getMin(), objDatos.getMax(), 1, objDatos.getCodArea());
 
        	if (objRespuesta == null) {

        		conn = getConnRegional();
                BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
        		lstFiltros = getFiltrosCumplVenta(objDatos, ID_PAIS);
        		lstVentas = OperacionReporte.getDatosVentaPdv(conn, lstFiltros, Integer.parseInt(objDatos.getMin()), Integer.parseInt(objDatos.getMax()), objDatos.getCodArea(), ID_PAIS);
        		if (lstVentas.isEmpty()) {
        			objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
        					null, objDatos.getCodArea());
        		} else {
        			objReporte.setCumplVenta(lstVentas);
        			objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
        		}

        		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        				Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de Reporte Cumplimiento Ventas de sidra.", ""));

        	} else {
        		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        				Conf.LOG_TIPO_NINGUNO, "Problema al consultar Reporte Cumplimiento Venta.", objRespuesta.getDescripcion()));
        		UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        	}
        } catch (SQLException e) {
        	objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
        	log.error(e.getMessage(), e);

        	listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        			Conf.LOG_TIPO_NINGUNO, "Problema al consultar Reporte Cumplimiento Ventas.", e.getMessage()));
        } catch (Exception e) {
        	objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
        			metodo, "", objDatos.getCodArea());
        	log.error(e.getMessage(), e);

        	listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        			Conf.LOG_TIPO_NINGUNO, "Problema al consultar Reporte Cumplimiento Ventas.", e.getMessage()));
        } finally {
        	DbUtils.closeQuietly(conn);

        	UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }
        
        objReporte.setRespuesta(objRespuesta);

        return objReporte;
    }
    
    /**
     * M\u00E9todo para obtener el listado de registros de cumplimiento de ventas encontrados en la base de datos.
     * @param objDatos
     * @return OutputReporteCumplimientoVenta
     */
    public OutputReporteCumplimientoVenta getCountCumplimientoVenta(InputReporteCumplimientoVenta objDatos) {
    	List<LogSidra> listaLog = new ArrayList<LogSidra>();
    	OutputReporteCumplimientoVenta objReporte = new OutputReporteCumplimientoVenta();
    	Respuesta objRespuesta = null;
    	List<Filtro> lstFiltros = new ArrayList<Filtro>();
        String metodo = "getReporteCumplimientoVenta";
        Connection conn = null;
        BigDecimal conteo = null;
    	
        try {
        	//Validaciones de parametros
        	objRespuesta = validarFechas(objDatos.getFechaInicio(), objDatos.getFechaFin(), false, 31, metodo, objDatos.getCodArea());
 
        	if (objRespuesta == null) {

        		conn = getConnRegional();
                BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
        		lstFiltros = getFiltrosCumplVenta(objDatos, ID_PAIS);
        		conteo = OperacionReporte.getContVentaPdv(conn, lstFiltros, objDatos.getCodArea(), ID_PAIS);
        		objReporte.setCantRegistros(conteo.toString());
        		objRespuesta = new Respuesta();
        		objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());

        		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        				Conf.LOG_TIPO_NINGUNO, "Se consulto conteo de registros Reporte Cumplimiento Ventas de sidra.", ""));

        	} else {
        		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        				Conf.LOG_TIPO_NINGUNO, "Problema al consultar conteo de Reporte Cumplimiento Venta.", objRespuesta.getDescripcion()));
        		UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        	}
        } catch (SQLException e) {
        	objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
        	log.error(e.getMessage(), e);

        	listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        			Conf.LOG_TIPO_NINGUNO, "Problema al consultar conteo de Reporte Cumplimiento Ventas.", e.getMessage()));
        } catch (Exception e) {
        	objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
        			metodo, "", objDatos.getCodArea());
        	log.error(e.getMessage(), e);

        	listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        			Conf.LOG_TIPO_NINGUNO, "Problema al consultar conteo de Reporte Cumplimiento Ventas.", e.getMessage()));
        } finally {
        	DbUtils.closeQuietly(conn);
        	UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }
        
        objReporte.setRespuesta(objRespuesta);

        return objReporte;
    }

    /**
     * M\u00E9todo para obtener el listado de registros de articulos vendidos.
     * @param objDatos
     * @return OutputReporteCumplimientoVenta
     */
    public OutputReporteInventarioVendido getReporteInventarioVendido(InputReporteInventarioVendido objDatos) {
    	List<LogSidra> listaLog = new ArrayList<LogSidra>();
    	OutputReporteInventarioVendido objReporte = new OutputReporteInventarioVendido();
    	Respuesta objRespuesta = null;
    	List<DetalleVendido> lstVentas = new ArrayList<DetalleVendido>();
    	List<Filtro> lstFiltros = new ArrayList<Filtro>();
        String metodo = "getReporteInventarioVendido";
        Connection conn = null;
    	
        try {
        	//Validaciones de parametros
        	objRespuesta = validarFechas(objDatos.getFechaInicio(), objDatos.getFechaFin(), true, 61, metodo, objDatos.getCodArea());
 
        	if (objRespuesta == null) {

        		conn = getConnRegional();
                BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
        		lstFiltros = getFiltrosInventVendido(objDatos);
        		lstVentas = OperacionReporte.getDatosDetalleVendido(conn, lstFiltros,objDatos.getIdJornada(), objDatos.getIdDistribuidor(), objDatos.getCodArea(), ID_PAIS);
        		if (lstVentas.isEmpty()) {
        			objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
        					null, objDatos.getCodArea());
        		} else {
        			objReporte.setDetalle(lstVentas);
        			objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
        		}

        		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        				Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de Reporte Inventario Vendido de sidra.", ""));

        	} else {
        		if (new BigDecimal(objRespuesta.getCodResultado()).intValue() == Conf_Mensajes.MSJ_RANGO_FECHA_INVALIDO_511) {
        			objRespuesta = getMensaje(Conf_Mensajes.MSJ_RANGO_61DIAS_INVALIDO_519, null, this.getClass().toString(), metodo,
	                        null, objDatos.getCodArea());	
				}
        		
        		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        				Conf.LOG_TIPO_NINGUNO, "Problema al consultar Reporte Inventario Vendido.", objRespuesta.getDescripcion()));
        		UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        	}
        } catch (SQLException e) {
        	objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
        	log.error(e.getMessage(), e);

        	listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        			Conf.LOG_TIPO_NINGUNO, "Problema al consultar Reporte Inventario Vendido.", e.getMessage()));
        } catch (Exception e) {
        	objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
        			metodo, "", objDatos.getCodArea());
        	log.error(e.getMessage(), e);

        	listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        			Conf.LOG_TIPO_NINGUNO, "Problema al consultar Reporte Inventario Vendido.", e.getMessage()));
        } finally {
        	DbUtils.closeQuietly(conn);
        	UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }
        
        objReporte.setRespuesta(objRespuesta);

        return objReporte;
    }
    
    /**
     * M\u00E9todo para obtener el listado resumen de registros de articulos vendidos.
     * @param objDatos
     * @return OutputResumenInventarioVendido
     */
    public OutputResumenInventarioVendido getResumenInventarioVendido(InputReporteInventarioVendido objDatos) {
    	List<LogSidra> listaLog = new ArrayList<LogSidra>();
    	OutputResumenInventarioVendido objResumen = new OutputResumenInventarioVendido();
    	Respuesta objRespuesta = null;
    	List<DetalleArticulo> lstDetArticulo = new ArrayList<DetalleArticulo>();
    	List<Filtro> lstFiltros = new ArrayList<Filtro>();
        String metodo = "getResumenInventarioVendido";
        Connection conn = null;
    	
        try {
        	//Validaciones de parametros
        	objRespuesta = validarFechas(objDatos.getFechaInicio(), objDatos.getFechaFin(), true, 61, metodo, objDatos.getCodArea());
 
        	if (objRespuesta == null) {

        		conn = getConnRegional();
                BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
        		lstFiltros = getFiltrosInventVendido(objDatos);
        		lstDetArticulo = OperacionReporte.getDatosResumenVendido(conn, lstFiltros, objDatos.getCodArea(), ID_PAIS);
        		if (lstDetArticulo.isEmpty()) {
        			objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
        					null, objDatos.getCodArea());
        		} else {
        			objResumen.setResumen(lstDetArticulo);
        			objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
        		}

        		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        				Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de Resumen Inventario Vendido de sidra.", ""));

        	} else {
        		if (new BigDecimal(objRespuesta.getCodResultado()).intValue() == Conf_Mensajes.MSJ_RANGO_FECHA_INVALIDO_511) {
        			objRespuesta = getMensaje(Conf_Mensajes.MSJ_RANGO_61DIAS_INVALIDO_519, null, this.getClass().toString(), metodo,
	                        null, objDatos.getCodArea());	
				}
        		
        		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        				Conf.LOG_TIPO_NINGUNO, "Problema al consultar Resumen Inventario Vendido.", objRespuesta.getDescripcion()));
        		UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        	}
        } catch (SQLException e) {
        	objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
        	log.error(e.getMessage(), e);

        	listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        			Conf.LOG_TIPO_NINGUNO, "Problema al consultar Resumen Inventario Vendido.", e.getMessage()));
        } catch (Exception e) {
        	objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
        			metodo, "", objDatos.getCodArea());
        	log.error(e.getMessage(), e);

        	listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        			Conf.LOG_TIPO_NINGUNO, "Problema al consultar Resumen Inventario Vendido.", e.getMessage()));
        } finally {
        	DbUtils.closeQuietly(conn);
        	UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }
        
        objResumen.setRespuesta(objRespuesta);

        return objResumen;
    }
    
    /**
     * M\u00E9todo para obtener el listado de datos de un reporte de efectividad de venta.
     * @param objDatos
     * @return OutputResumenInventarioVendido
     */
    public OutputReporteEfectividadVenta getReporteEfectividadVenta(InputReporteEfectividadVenta objDatos) {
    	List<LogSidra> listaLog = new ArrayList<LogSidra>();
    	OutputReporteEfectividadVenta objReporte = new OutputReporteEfectividadVenta();
    	Respuesta objRespuesta = null;
    	List<DatosEfectividad> lstDatosEfectividad = new ArrayList<DatosEfectividad>();
    	List<Filtro> lstFiltros = new ArrayList<Filtro>();
    	List<Filtro> lstFiltrosFecha = new ArrayList<Filtro>();
        String metodo = "getReporteEfectividadVenta";
        Connection conn = null;
        
        try {
        	//Validaciones del orden entre fechas
        	objRespuesta = validarOrdenDeFechas(objDatos.getFechaInicioMesActual(), objDatos.getFechaFinMesActual(), 
        			objDatos.getFechaInicioMesAnterior(), objDatos.getFechaFinMesAnterior(), metodo, objDatos.getCodArea());
        	
        	if (objRespuesta == null) {
        		
        		conn = getConnRegional();
                BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
        		lstFiltros = getFiltrosEfectividadVenta(objDatos);
        		lstFiltrosFecha = getFiltrosFechaEfectividad(objDatos, "", ID_PAIS);
        		lstDatosEfectividad = OperacionReporte.getDatosEfectividad(conn, lstFiltros, lstFiltrosFecha, objDatos, objDatos.getCodArea(), ID_PAIS);
        		
        		if (lstDatosEfectividad.isEmpty()) {
        			objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
        					null, objDatos.getCodArea());
        		} else {
        			objReporte.setDatosEfectividad(lstDatosEfectividad);
        			objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
        		}

        		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        				Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de Reporte Efectividad de Venta de sidra.", ""));
        	} else {
        		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        				Conf.LOG_TIPO_NINGUNO, "Problema al consultar Reporte Efectividad Venta.", objRespuesta.getDescripcion()));
        		UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        	}
        } catch (SQLException e) {
        	objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
        	log.error(e.getMessage(), e);
        	
        	listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        			Conf.LOG_TIPO_NINGUNO, "Problema al consultar Resumen Efectividad Venta.", e.getMessage()));
        } catch (Exception e) {
        	objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
        			metodo, "", objDatos.getCodArea());
        	log.error(e.getMessage(), e);

        	listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
        			Conf.LOG_TIPO_NINGUNO, "Problema al consultar Resumen Efectividad Venta.", e.getMessage()));
        } finally {
        	DbUtils.closeQuietly(conn);

        	UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }
        
        objReporte.setRespuesta(objRespuesta);
        return objReporte;
    }
}

package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.asignacion.InputArticuloAsignacion;
import com.consystec.sc.ca.ws.input.asignacion.InputAsignacion;
import com.consystec.sc.ca.ws.input.asignacion.RespuestaAsignacion;
import com.consystec.sc.ca.ws.input.historico.InputArticuloHistorico;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.asignacion.OutputAsignacion;
import com.consystec.sc.sv.ws.metodos.CtrlAsignacion;
import com.consystec.sc.sv.ws.orm.AsignacionReserva;
import com.consystec.sc.sv.ws.orm.AsignacionReservaDet;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.HistoricoInv;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class OperacionAsignacion extends ControladorBase {
    private static final Logger log = Logger.getLogger(OperacionAsignacion.class);
    private static String servicioGetAsignacion = Conf.LOG_GET_ASIGNACIONES_RESERVAS;
    private static String servicioPostAsignacion = Conf.LOG_POST_ASIGNACIONES_RESERVAS;
    private static String servicioPutAsignacion = Conf.LOG_PUT_ASIGNACIONES_RESERVAS;
    private static int tipoSuma = 1;
    private static int tipoResta = 0;
    private static boolean existeError = false;
    private static boolean existeErrorArticulo = false;
    private static boolean existeErrorSerie = false;
    private static boolean existeErrorExistencias = false;
    private static boolean existeErrorHistorico = false;
    private static List<InputArticuloAsignacion> listSeriesError = new ArrayList<InputArticuloAsignacion>();
    private static List<InputArticuloAsignacion> listArticulosError = new ArrayList<InputArticuloAsignacion>();
    private static List<InputArticuloAsignacion> listExistenciasError = new ArrayList<InputArticuloAsignacion>();
    
    //--------------------------------- Operaciones para obtener Asignaciones/Reservas ------------//
    public static OutputAsignacion doGet(Connection conn, InputAsignacion input, int metodo, BigDecimal idPais)
            throws SQLException {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<InputAsignacion> list = new ArrayList<InputAsignacion>();
        String tablaAsignacionConParticion = getParticion(AsignacionReserva.N_TABLA, Conf.PARTITION, "", input.getCodArea());
        Respuesta respuesta = new Respuesta();
        OutputAsignacion output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        String sql = null;
        String campos[] = CtrlAsignacion.obtenerCamposGetPost(metodo,idPais);

        List<Filtro> condiciones = CtrlAsignacion.obtenerCondiciones(input, metodo,idPais);

        try {
            sql = UtileriasBD.armarQuerySelect(tablaAsignacionConParticion, campos, condiciones);
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
	            if (!rst.next()) {
	                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_ASIGNACIONES_814, null,
	                        nombreClase, nombreMetodo, null, input.getCodArea());
	
	                output = new OutputAsignacion();
	                output.setRespuesta(respuesta);
	            } else {
	                respuesta=new ControladorBase()
	                        .getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, input.getCodArea());
	

                    do {
                        InputAsignacion item = new InputAsignacion();
                        String idAsignacionReserva = rst.getString(AsignacionReserva.CAMPO_TCSCASIGNACIONRESERVAID);
                        item.setIdAsignacionReserva(idAsignacionReserva);

                        item.setTipo(rst.getString(AsignacionReserva.CAMPO_TIPO));
                        item.setIdVendedor(rst.getString(AsignacionReserva.CAMPO_IDVENDEDOR));
                        item.setNombreVendedor(getNombreVendedor(conn, rst.getString(AsignacionReserva.CAMPO_IDVENDEDOR), idPais));

                        item.setIdBodegaOrigen(rst.getString(AsignacionReserva.CAMPO_BODEGA_ORIGEN));
                        item.setIdBodegaDestino(rst.getString(AsignacionReserva.CAMPO_BODEGA_DESTINO));
                        item.setNombreBodegaOrigen(rst.getString("NOM_BOD_ORIGEN"));
                        item.setNombreBodegaDestino(rst.getString("NOM_BOD_DESTINO"));

	                    item.setObservaciones(UtileriasJava.getValue(rst.getString(AsignacionReserva.CAMPO_OBSERVACIONES)));
	                    item.setEstado(rst.getString(AsignacionReserva.CAMPO_ESTADO));
	                    item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(AsignacionReserva.CAMPO_CREADO_EL)));
	                    item.setCreado_por(rst.getString(AsignacionReserva.CAMPO_CREADO_POR));
	                    item.setModificado_el(UtileriasJava.formatStringDate(rst.getString(AsignacionReserva.CAMPO_MODIFICADO_EL)));
	                    item.setModificado_por(rst.getString(AsignacionReserva.CAMPO_MODIFICADO_POR));

                        List<InputArticuloAsignacion> articulos = new ArrayList<InputArticuloAsignacion>();
                        articulos = getDatosTablaHija(conn,  metodo, idAsignacionReserva, input.getCodArea());
                        item.setArticulos(articulos);

                        if (articulos.size() == 0) {
	                        InputArticuloAsignacion itemVacio = new InputArticuloAsignacion();
	                        itemVacio.setEstado(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion());
                            articulos.add(itemVacio);

                            item.setArticulos(articulos);
                        }

                        list.add(item);
                    } while (rst.next());

                    output = new OutputAsignacion();
                    output.setRespuesta(respuesta);
                    output.setAsignacionReserva(list);
                }
            }

            listaLog = new ArrayList<LogSidra>();
            String filtros = "Se consultaron asignaciones/reservas sin filtros.";
            if (condiciones.size() > 0) {
                filtros = "Se consultaron asignaciones/reservas con los filtros: " + sql.split("WHERE")[1];
            }
            listaLog.add(addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGetAsignacion, "0", Conf.LOG_TIPO_NINGUNO,
                    filtros.replace("'", "") + ". " + respuesta.getDescripcion(), ""));

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return output;
    }

    private static List<InputArticuloAsignacion> getDatosTablaHija(Connection conn,  int metodo,
            String idPadre, String codArea) throws SQLException {
        String nombreMetodo = "getDatosTablaHija";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        List<InputArticuloAsignacion> list = new ArrayList<InputArticuloAsignacion>();
        PreparedStatement pstmtIn = null;
        ResultSet rstIn = null;
   
        String campos[] = CtrlAsignacion.obtenerCamposTablaHija(metodo);
        
        List<Filtro> condiciones = new ArrayList<Filtro>();
        
        try{
	        if (!idPadre.equals("")) {
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AsignacionReservaDet.CAMPO_TCSCASIGNACIONID, idPadre));
	        }
	        
	        String sql = UtileriasBD.armarQuerySelect(AsignacionReservaDet.N_TABLA, campos, condiciones);
	        log.info("SQL ASIGNACION"+ sql);
	
	        pstmtIn = conn.prepareStatement(sql);
	        rstIn = pstmtIn.executeQuery();
	        
	        if (rstIn != null) {
	            if (!rstIn.next()) {
	                log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
	                InputArticuloAsignacion item = new InputArticuloAsignacion();
	                
	                Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null,
	                    nombreClase, nombreMetodo, null,codArea);
	                
	                item.setEstado(respuesta.getDescripcion());
	                list.add(item);
	            } else {
	                do {
	                    InputArticuloAsignacion item = new InputArticuloAsignacion();
	                    item.setIdAsignacionReservaDet(rstIn.getString(AsignacionReservaDet.CAMPO_TCSCASIGNACIONRESERVADETID));
	                    item.setIdAsignacionReserva(rstIn.getString(AsignacionReservaDet.CAMPO_TCSCASIGNACIONID));
	                    item.setIdArticulo(rstIn.getString(AsignacionReservaDet.CAMPO_ARTICULO) == null ? "NULL" : rstIn.getString(AsignacionReservaDet.CAMPO_ARTICULO));
	                    item.setDescripcion(rstIn.getString(AsignacionReservaDet.CAMPO_DESCRIPCION) == null ? "No se encontr\u00F3 el nombre del art\u00EDculo." : rstIn.getString(AsignacionReservaDet.CAMPO_DESCRIPCION));
	                    item.setSerie(rstIn.getString(AsignacionReservaDet.CAMPO_SERIE) == null ? "NULL" : rstIn.getString(AsignacionReservaDet.CAMPO_SERIE));
	                    item.setSerieFinal(rstIn.getString(AsignacionReservaDet.CAMPO_SERIE_FINAL) == null ? "NULL" : rstIn.getString(AsignacionReservaDet.CAMPO_SERIE_FINAL));
	                    item.setSerieAsociada(rstIn.getString(AsignacionReservaDet.CAMPO_SERIE_ASOCIADA) == null ? "" : rstIn.getString(AsignacionReservaDet.CAMPO_SERIE_ASOCIADA));
	                    item.setCantidad(rstIn.getString(AsignacionReservaDet.CAMPO_CANTIDAD));
	                    item.setObservaciones(rstIn.getString(AsignacionReservaDet.CAMPO_OBSEVACIONES));
	                    item.setEstado(rstIn.getString(AsignacionReservaDet.CAMPO_ESTADO));
	                    item.setCreado_por(rstIn.getString(AsignacionReservaDet.CAMPO_CREADO_POR));
	                    item.setCreado_el(UtileriasJava.formatStringDate(rstIn.getString(AsignacionReservaDet.CAMPO_CREADO_EL)));
	                    item.setModificado_por(rstIn.getString(AsignacionReservaDet.CAMPO_MODIFICADO_POR));
	                    item.setModificado_el(UtileriasJava.formatStringDate(rstIn.getString(AsignacionReservaDet.CAMPO_MODIFICADO_EL)));
	                    if(rstIn.getString("IMEI")==null || "".equals(rstIn.getString("IMEI"))){
	                    	item.setImei("");
	                    }else{
	                    	item.setImei(rstIn.getString("IMEI"));
	                    }
	                    if(rstIn.getString("ICC")==null || "".equals(rstIn.getString("ICC"))){
	                    	item.setIcc("");
	                    }else{
	                    	item.setIcc(rstIn.getString("ICC"));
	                    }
	                    list.add(item);
	                } while (rstIn.next());
	            }
            }
        } finally {

            DbUtils.closeQuietly(rstIn);
            DbUtils.closeQuietly(pstmtIn);
        }
        return list;
    }
    //--------------------------------- Operaciones para obtener Asignaciones/Reservas ------------//
    
    //--------------------------------- Operaciones para crear Asignaciones/Reservas --------------//
    public static OutputAsignacion doPost(Connection conn, InputAsignacion input, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doPost";
        String nombreClase = new CurrentClassGetter().getClassName();
        List<LogSidra> listaLog = new ArrayList<LogSidra>();

        Respuesta respuesta = null;
        OutputAsignacion output = null;
        boolean insertHijo = false;
        String detalleError = "";

        String tipoAsignacion = UtileriasJava.getConfig(conn, Conf.GRUPO_ASIGNACIONES_TIPO, Conf.AR_TIPO_ASIGNACION, input.getCodArea());

        InputAsignacion inputSeriado = new InputAsignacion();
        InputAsignacion inputNoSeriado = new InputAsignacion();
        List<InputArticuloAsignacion> articulosSeriados = new ArrayList<InputArticuloAsignacion>();
        List<InputArticuloAsignacion> articulosNoSeriados = new ArrayList<InputArticuloAsignacion>();
        RespuestaAsignacion respTransaccion = new RespuestaAsignacion();

        String sql = null;
        String campos[] = CtrlAsignacion.obtenerCamposGetPost(Conf.METODO_POST, idPais);
        List<String> inserts = CtrlAsignacion.obtenerInsertsPost(conn, input, AsignacionReserva.SEQUENCE, tipoAsignacion, idPais);

        sql = UtileriasBD.armarQueryInsert(AsignacionReserva.N_TABLA, campos, inserts);

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
        	 int idPadre = 0;
        	try{
	            conn.setAutoCommit(false);
	            String generatedColumns[] = { AsignacionReserva.CAMPO_TCSCASIGNACIONRESERVAID };
	            pstmt = conn.prepareStatement(sql, generatedColumns);
	
	            pstmt.executeUpdate();
	            rs = pstmt.getGeneratedKeys();
	
	                if (rs.next()) {
	                    idPadre = rs.getInt(1);
	                }
        	}finally{

                DbUtils.closeQuietly(rs);
                DbUtils.closeQuietly(pstmt);
            
        	}
            if (idPadre >0) {                
                log.debug("idPadre: " + idPadre);
                output = new OutputAsignacion();

                inputSeriado = (InputAsignacion) input.clone();
                inputNoSeriado = (InputAsignacion) input.clone();

                for (int i = 0; i < input.getArticulos().size(); i++) {
                    if (input.getArticulos().get(i).getSerie() != null && !input.getArticulos().get(i).getSerie().equals("")){
                        articulosSeriados.add(input.getArticulos().get(i));
                    } else {
                        articulosNoSeriados.add(input.getArticulos().get(i));
                    }
                }
                inputSeriado.setArticulos(articulosSeriados);
                inputNoSeriado.setArticulos(articulosNoSeriados);

                if (input.getTipo().equalsIgnoreCase(tipoAsignacion)) {
                    // Proceso completo de asignaciones
                    respTransaccion = doAsignacion(conn, inputSeriado, inputNoSeriado, idPadre, input.getCodArea(), idPais);
                    log.trace("termino asignacion");
                    insertHijo = respTransaccion.isResultado();
                    log.trace("insertHijo:"+insertHijo);
                    detalleError = respTransaccion.getDescripcion();
                    output = respTransaccion.getDatos();

                } else {
                    // Proceso completo de reservas
                    respTransaccion = doReservacion(conn, idPadre, inputSeriado, inputNoSeriado, input.getCodArea(), idPais);
                    insertHijo = respTransaccion.isResultado();
                    detalleError = respTransaccion.getDescripcion();
                    output = respTransaccion.getDatos();

                }


                if (insertHijo == true) {
                    conn.commit();
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_ASIGNACION_26, null, nombreClase,
                            nombreMetodo, detalleError, input.getCodArea());

                    output.setRespuesta(respuesta);
                    output.setIdAsignacion(idPadre + "");

                    listaLog = new ArrayList<LogSidra>();
                    listaLog.add(
                            addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, idPadre + "",
                                    Conf.LOG_TIPO_ASIGNACION_RESERVA, output.getRespuesta().getDescripcion(), ""));

                    UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
                } else {
                    conn.rollback();
                    log.debug("Rollback");
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
                            nombreMetodo, detalleError, input.getCodArea());

                    output = new OutputAsignacion();
                    output.setRespuesta(respuesta);

                    listaLog = new ArrayList<LogSidra>();
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
                            Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), ""));

                    UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            respuesta = new ControladorBase().getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo,
                    null, input.getCodArea());

            output = new OutputAsignacion();
            output.setRespuesta(respuesta);

            conn.rollback();
            log.error("Rollback");

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
                    Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), e.getMessage()));

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase,
                    nombreMetodo, null, input.getCodArea());

            output = new OutputAsignacion();
            output.setRespuesta(respuesta);

            conn.rollback();
            log.error("Rollback");

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
                    Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), e.getMessage()));

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        } finally {
            conn.setAutoCommit(true);
         
        }

        return output;
    }

    //--------------------------------- Operaciones de Asignacion ---------------------------------//
    private static RespuestaAsignacion doAsignacion(Connection conn, InputAsignacion inputSeriado,
            InputAsignacion inputNoSeriado, int idPadre, String codArea, BigDecimal idPais) throws SQLException {
        RespuestaAsignacion output = new RespuestaAsignacion();
        OutputAsignacion respDoAsignacion = new OutputAsignacion();
        RespuestaAsignacion respAsignacionSeriado = new RespuestaAsignacion();
        RespuestaAsignacion respAsignacionNoSeriado ;
        String estadoDisponible = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DISPONIBLE, codArea);
        String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA,codArea);
        String estadoAsignado = UtileriasJava.getConfig(conn, Conf.GRUPO_ASIGNACIONES_ESTADOS, Conf.AR_ESTADO_ART_ASIGNADO,codArea);
        BigDecimal idTipoTransaccion = getIdTransaccion(conn, Conf.CODIGO_TRANSACCION_ASIGNACION,idPais);
        boolean insertSeriado = false;
        boolean insertNoSeriado = false;
        String detalleError = "";

        if (inputSeriado.getArticulos().size() > 0) {
            // Operacion para asignar series
            respAsignacionSeriado = asignacionSeriado(conn, inputSeriado, idPadre, estadoDisponible, estadoAlta,
                    estadoAsignado, idTipoTransaccion, codArea, idPais);

            if (respAsignacionSeriado.isResultado()) {
                detalleError = respAsignacionSeriado.getDescripcion();
                insertSeriado = respAsignacionSeriado.isResultado();

                respDoAsignacion = respAsignacionSeriado.getDatos();
                respDoAsignacion.setDescErrorSeries(respAsignacionSeriado.getDatos().getDescErrorSeries());
                respDoAsignacion.setSeries(respAsignacionSeriado.getDatos().getSeries());
            } else {
                if (respAsignacionSeriado.getDescripcion().startsWith("ERROR")) {
                    respAsignacionSeriado.setDescripcion(respAsignacionSeriado.getDescripcion().replace("ERROR: ", ""));
                    output.setDescripcion(respAsignacionSeriado.getDescripcion());

                    return output;
                }

                detalleError = respAsignacionSeriado.getDescripcion();
                insertSeriado = respAsignacionSeriado.isResultado();

                respDoAsignacion = respAsignacionSeriado.getDatos();
            }
        } else {
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_ARTICULOS_SERIADOS_108, null, null, null, null, codArea);
            detalleError = r.getDescripcion();
        }

        if (inputNoSeriado.getArticulos().size() > 0) {
            // operacion para asignar articulos
            respAsignacionNoSeriado = asignacionNoSeriado(conn, inputNoSeriado, idPadre, estadoDisponible, estadoAlta,
                    estadoAsignado, idTipoTransaccion, idPais);

            if (respAsignacionNoSeriado.isResultado()) {
                insertNoSeriado = respAsignacionNoSeriado.isResultado();
                detalleError += " " + respAsignacionNoSeriado.getDescripcion();

                respDoAsignacion = respAsignacionNoSeriado.getDatos();
                if (respAsignacionSeriado.getDatos() != null) {
                    respDoAsignacion.setDescErrorSeries(respAsignacionSeriado.getDatos().getDescErrorSeries());
                    respDoAsignacion.setSeries(respAsignacionSeriado.getDatos().getSeries());
                }
                respDoAsignacion.setDescErrorArticulos(respAsignacionNoSeriado.getDatos().getDescErrorArticulos());
                respDoAsignacion.setArticulos(respAsignacionNoSeriado.getDatos().getArticulos());
                respDoAsignacion.setDescErrorExistencias(respAsignacionNoSeriado.getDatos().getDescErrorExistencias());
                respDoAsignacion.setExistencias(respAsignacionNoSeriado.getDatos().getExistencias());
            } else {
                if (respAsignacionNoSeriado.getDescripcion().startsWith("ERROR")) {
                    respAsignacionNoSeriado.setDescripcion(respAsignacionNoSeriado.getDescripcion().replace("ERROR: ", ""));
                    output.setDescripcion(respAsignacionNoSeriado.getDescripcion());

                    return output;
                }

                insertNoSeriado = respAsignacionNoSeriado.isResultado();
                detalleError += " " + respAsignacionNoSeriado.getDescripcion();

                respDoAsignacion = respAsignacionNoSeriado.getDatos();
            }
        } else {
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_ARTICULOS_NO_SERIADOS_109, null, null, null, null, codArea);
            detalleError += " " + r.getDescripcion();
        }

        if (insertSeriado == true || insertNoSeriado == true) {
            output.setResultado(true);
        } else {
            output.setResultado(false);
        }
        output.setDatos(respDoAsignacion);
        output.setDescripcion(detalleError);

        return output;
    }

    private static RespuestaAsignacion asignacionSeriado(Connection conn, InputAsignacion input, int idPadre,
            String estadoDisponible, String estadoAlta, String estadoAsignado, BigDecimal idTipoTransaccion, String codArea, BigDecimal idPais)
                    throws SQLException {
        RespuestaAsignacion respAsignacion = new RespuestaAsignacion();
        OutputAsignacion output = new OutputAsignacion();
        boolean insertHijo = false;
        String detalleError = "";

        List<InputArticuloAsignacion> updSeries = doUpdSeriesAsignacion(conn, input, idPadre, estadoDisponible,
                estadoAlta, estadoAsignado, idTipoTransaccion, idPais);
        log.trace("ya inserto y actualizo series");
        if (updSeries.get(0).getEstado().equalsIgnoreCase("OK")) {
            insertHijo = true;
        } else if (updSeries.get(0).getEstado().equalsIgnoreCase("GOOD")) {
            if (updSeries.size() > 1) {
                updSeries.remove(0);

                detalleError += new ControladorBase()
                        .getMensaje(Conf_Mensajes.MSJ_TITULO_ASIGNACION_SERIADA_355, null, null, null, null, codArea)
                        .getDescripcion();
             

                output.setDescErrorSeries(new ControladorBase()
                        .getMensaje(Conf_Mensajes.MSJ_ERROR_SERIES_VALIDAS_BODEGA_342, null, null, null, null, codArea)
                        .getDescripcion());
                output.setSeries(updSeries);
            }
            insertHijo = true;
        } else if (updSeries.get(0).getEstado().equalsIgnoreCase("ERROR")) {
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_SERIES_NO_VALIDAS_107, null, null, null,
                    null, codArea);
            detalleError = r.getDescripcion();
            insertHijo = false;
        } else {
            // Cualquier caso de fallo

            detalleError = "ERROR: " + updSeries.get(0).getEstado();
            insertHijo = false;
        }

        respAsignacion.setResultado(insertHijo);
        respAsignacion.setDescripcion(detalleError);
        respAsignacion.setDatos(output);

        return respAsignacion;
    }

    private static List<InputArticuloAsignacion> doUpdSeriesAsignacion(Connection conn, InputAsignacion input,
            int idPadre, String estadoDisponible, String estadoAlta, String estadoAsignado,
            BigDecimal idTipoTransaccion, BigDecimal idPais) throws SQLException {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();

        String sql = "";
        String existencia = "";
        String sql1="";
        QueryRunner Qr = new QueryRunner();

        List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
        List<InputArticuloAsignacion> listItemSeriesFail = new ArrayList<InputArticuloAsignacion>();
        List<HistoricoInv> listHistorico = new ArrayList<HistoricoInv>();
        Statement stmtUpdates = null;
        Statement stmtInsert  = null;
        int contBatchAdded = 0;
        InputArticuloAsignacion itemSeriesFail = new InputArticuloAsignacion();
        itemSeriesFail.setEstado("OK");
        listItemSeriesFail.add(itemSeriesFail);
        
        try{
	     	stmtInsert=conn.createStatement();
	     	stmtUpdates=conn.createStatement();
	        for (int j = 0; j < input.getArticulos().size(); j++) {
	            if (input.getArticulos().get(j).getSerieFinal() != null && !input.getArticulos().get(j).getSerieFinal().equals("")) {
	                // validaci\u00F3n y proceso de rango de series
	                String seriesInvalidas = OperacionMovimientosInventario.validarSeries(conn,
	                        input.getArticulos().get(j).getSerie(), input.getArticulos().get(j).getSerieFinal(),
	                        input.getIdBodegaOrigen(), 0, estadoDisponible, input.getArticulos().get(j).getTipoInv(), input.getCodArea());
	
	                if (seriesInvalidas.equalsIgnoreCase("OK")) {
	                    // procesos para hacer el update de las series
	
	                    String seriesActualizadas = OperacionMovimientosInventario.actualizarSeriesAsignacion(conn,
	                            new BigInteger(input.getArticulos().get(j).getSerie()),
	                            new BigInteger(input.getArticulos().get(j).getSerieFinal()), input.getIdBodegaOrigen(),
	                            input.getIdBodegaDestino(), idPadre, input.getIdVendedor(), input.getUsuario(),
	                            estadoDisponible, input.getArticulos().get(j).getTipoInv(), idPais, input.getCodArea());
	
	                    if (seriesActualizadas.equalsIgnoreCase("OK")) {
	                        // Insert detalle
	                        String[] campos = CtrlAsignacion.obtenerCamposTablaHija(Conf.METODO_POST);
	                        List<String> inserts = CtrlAsignacion.obtenerInsertsPostHijo( idPadre, input, j,
	                                AsignacionReservaDet.SEQUENCE, estadoAsignado,idPais);
	                        sql = UtileriasBD.armarQueryInsertAll(AsignacionReservaDet.N_TABLA, campos, inserts);
	
	                        try {
	                        	
	
	                            // proceso para registrar series enviadas al proceso de activacion
	                            if ("506".equals(input.getCodArea())&& (input.getArticulos().get(j).getTipoGrupoSidra() != null &&
	                                        (input.getArticulos().get(j).getTipoGrupoSidra().equalsIgnoreCase(Conf.TIPO_GRUPO_TARJETASRASCA)
	                                        || input.getArticulos().get(j).getTipoGrupoSidra().startsWith("SIMCARD")))) {
	                                    registroSeriesActivacion(conn, idPadre, input.getArticulos().get(j), input.getUsuario(), idPais);
	                            }
	
	                        	Qr.update(conn, sql);
	                        } catch (SQLException e) {
	                            conn.rollback();
	                            log.error("Rollback: problema al insertar detalle de asignacion.", e);
	                            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_INSERT_DET_ASIGNACION_124, null, null, null, null,input.getCodArea());
	                            listItemSeriesFail.get(0).setEstado(r.getDescripcion());
	
	                            listaLog = new ArrayList<LogSidra>();
	                            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA,
	                                servicioPostAsignacion, "0", Conf.LOG_TIPO_NINGUNO,
	                                listItemSeriesFail.get(0).getEstado(), e.getMessage()));
	                            
	                            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
	                            
	                            return listItemSeriesFail;
	                        } catch (Exception e) {
	                            conn.rollback();
	                            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_INSERT_DET_ASIGNACION_124, null, null, null, null,input.getCodArea());
	                            listItemSeriesFail.get(0).setEstado(r.getDescripcion());
	
	                            listaLog = new ArrayList<LogSidra>();
	                            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA,
	                                servicioPostAsignacion, "0", Conf.LOG_TIPO_NINGUNO,
	                                listItemSeriesFail.get(0).getEstado(), e.getMessage()));
	                            
	                            UtileriasJava.doInsertLog(listaLog, input.getUsuario(),input.getCodArea());
	                            
	                            return listItemSeriesFail;
	                        }
	
	                        // Insert historico
	                        listHistorico.add(OperacionMovimientosInventario.getInsertHistoricoAsignacion(
	                                input.getIdBodegaOrigen(), input.getIdBodegaDestino(), input.getArticulos().get(j),
	                                idTipoTransaccion, estadoAlta, input.getUsuario(), input.getCodArea(), idPais));
	                        insertaHistorico2(conn, listHistorico);
	                    	listHistorico.clear();
	                        // Insert log
	                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO, servicioPostAsignacion,
	                                idPadre + "", Conf.LOG_TIPO_ASIGNACION_RESERVA,
	                                "Se asign\u00F3 el rango de series " + input.getArticulos().get(j).getSerie() + " - "
	                                        + input.getArticulos().get(j).getSerieFinal() + " de la bodega "
	                                        + input.getIdBodegaOrigen() + " a la bodega " + input.getIdBodegaDestino()
	                                        + ".",
	                                ""));
	
	                    } else {
	                        // se deshace todo porque no fueron actualizadas todas las series
	                        conn.rollback();
	                        log.error("Rollback: problema al actualizar las series.");
	                        Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105,
	                                null, null, null, null,input.getCodArea());
	                        listItemSeriesFail.get(0).setEstado(r.getDescripcion());
	
	                        listaLog = new ArrayList<LogSidra>();
	                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
	                                Conf.LOG_TIPO_NINGUNO, listItemSeriesFail.get(0).getEstado(),
	                                "Fallaron las series: " + seriesActualizadas));
	
	                        UtileriasJava.doInsertLog(listaLog, input.getUsuario(),input.getCodArea());
	
	                        return listItemSeriesFail;
	                    }
	                } else {
	                    listItemSeriesFail.get(0).setEstado("GOOD");
	                    itemSeriesFail = new InputArticuloAsignacion();
	                    itemSeriesFail.setSerie(input.getArticulos().get(j).getSerie());
	                    itemSeriesFail.setSerieFinal(input.getArticulos().get(j).getSerieFinal());
	                    listItemSeriesFail.add(itemSeriesFail);
	                }
	
	            } else {
	                // validaci\u00F3n y proceso normal de una serie
	                condicionesExistencia.clear();
	                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodegaOrigen()));
	                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_SERIE, input.getArticulos().get(j).getSerie()));
	                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_TIPO_INV, input.getArticulos().get(j).getTipoInv()));
	                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_ESTADO, estadoDisponible));
	                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
	
	                if ("506".equals(input.getCodArea())) {//Solo para CR
	                    String[] camposSerie = {
	                            "COUNT (1) AS COUNT",
	                            Inventario.CAMPO_TIPO_GRUPO_SIDRA,
	                            Inventario.CAMPO_NUM_TELEFONO
	                    };
	                    String[] group = { Inventario.CAMPO_TIPO_GRUPO_SIDRA, Inventario.CAMPO_NUM_TELEFONO };
	                    List<Map<String, String>> datosSeries = UtileriasBD.getSingleData(conn, getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()), camposSerie, condicionesExistencia, null, group);
	
	                    if (datosSeries.size() > 0) {
	                        existencia = datosSeries.get(0).get("COUNT");
	                        input.getArticulos().get(j).setTipoGrupoSidra(UtileriasJava.getValue(datosSeries.get(0).get(Inventario.CAMPO_TIPO_GRUPO_SIDRA)));
	                        input.getArticulos().get(j).setNumTelefono(UtileriasJava.getValue(datosSeries.get(0).get(Inventario.CAMPO_NUM_TELEFONO)));
	                    } else {
	                        existencia = "0";
	                    }
	                } else {
	                	existencia = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_ARTICULO, ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()), condicionesExistencia);
	                }
	
	                if (!(existencia==null || "".equals(existencia))) {
	                    // Update
	                	// Insert historico
						input.getArticulos().get(j).setIdArticulo(existencia);
						log.trace("idArticulo:"+ input.getArticulos().get(j).getIdArticulo());
						log.trace("serie:"+input.getArticulos().get(j).getSerie());
	                    String camposUpdate[][] = CtrlAsignacion.obtenerCamposPutDel(input, idPadre);
	                    sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()), camposUpdate, condicionesExistencia);
	
	                        stmtUpdates.addBatch(sql);
	
	
	                    //Insert detalle
	                    String[] campos = CtrlAsignacion.obtenerCamposTablaHija(Conf.METODO_POST);
	                    List<String> inserts = CtrlAsignacion.obtenerInsertsPostHijo( idPadre, input, j, AsignacionReservaDet.SEQUENCE, estadoAsignado, idPais);
	                    sql1 = UtileriasBD.armarQueryInsertAll(AsignacionReservaDet.N_TABLA, campos, inserts);
	                  
	                    stmtInsert.addBatch(sql1);
	
	
	                    // Insert historico
	                    listHistorico.add(OperacionMovimientosInventario.getInsertHistoricoAsignacion(
	                            input.getIdBodegaOrigen(), input.getIdBodegaDestino(), input.getArticulos().get(j),
	                            idTipoTransaccion, estadoAlta, input.getUsuario(), input.getCodArea(),idPais));
	                    
	                    
	              	  if(++contBatchAdded % 500 == 0) {
				    		stmtUpdates.executeBatch();
				    		stmtInsert.executeBatch();
				    		insertaHistorico2(conn, listHistorico);
				    		listHistorico.clear();			    		
				    		contBatchAdded=1;
	              	  }else if(contBatchAdded>0 && contBatchAdded<500 && (j+1)==input.getArticulos().size()){
	              		  stmtUpdates.executeBatch();
	              		  stmtInsert.executeBatch();
	              		  insertaHistorico2(conn, listHistorico);
	              		  listHistorico.clear();
	              	  }
	
	                } else {
	                    listItemSeriesFail.get(0).setEstado("GOOD");
	                    itemSeriesFail = new InputArticuloAsignacion();
	                    itemSeriesFail.setSerie(input.getArticulos().get(j).getSerie());
	                    listItemSeriesFail.add(itemSeriesFail);
	                }
	            }
	        }
        }finally{

	        DbUtils.closeQuietly(stmtInsert);
	        DbUtils.closeQuietly(stmtUpdates);
        }
        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

        // verficar si no se inserto ningun articulo
        int cantArticulosConError = (listItemSeriesFail.size() - 1);
        if (cantArticulosConError >= new Integer(input.getArticulos().size())) {
            // no se agrego ninguno
            listItemSeriesFail.clear();
            itemSeriesFail.setEstado("ERROR");
            listItemSeriesFail.add(itemSeriesFail);
            log.debug("Ninguna serie es v\u00E9lida para ingresar.");
        }

        return listItemSeriesFail;
    }

    private static RespuestaAsignacion asignacionNoSeriado(Connection conn, InputAsignacion input, int idPadre,
            String estadoDisponible, String estadoAlta, String estadoAsignado, BigDecimal idTipoTransaccion, BigDecimal idPais)
                    throws SQLException {
        RespuestaAsignacion respAsignacion = new RespuestaAsignacion();
        OutputAsignacion output = new OutputAsignacion();
        boolean insertHijo = false;
        String detalleError = "";
        Respuesta r;

        OutputAsignacion updArticulos = doUpdArticulosAsignacion(conn, input, idPadre, estadoDisponible, estadoAlta,
                estadoAsignado, idTipoTransaccion, idPais);
        if (updArticulos.getMensaje().equalsIgnoreCase("OK")) {
            // no hubieron errores con todos los articulos
            insertHijo = true;
        } else if (updArticulos.getMensaje().equalsIgnoreCase("GOOD")) {
            // se insertaron algunos
          	  detalleError = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_ASIGNACION_NO_SERIADA_356, null, null, null, null, input.getCodArea()).getDescripcion();

            if (updArticulos.getArticulos().size() > 0) {
                r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_ARTICULOS_NO_EXISTEN_345, null, null,
                        null, null, input.getCodArea());
                output.setDescErrorArticulos(r.getDescripcion());
                detalleError += r.getDescripcion();

                for (int i = 0; i < updArticulos.getArticulos().size(); i++) {
                    detalleError += updArticulos.getArticulos().get(i).getIdArticulo();
                    if (i < updArticulos.getArticulos().size() - 1) {
                        detalleError += ", ";
                    } else {
                        detalleError += ". ";
                    }
                }
            }

            if (updArticulos.getExistencias().size() > 0) {
                r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_ARTICULOS_STOCK_346, null, null, null,
                        null, input.getCodArea());
                output.setDescErrorExistencias(r.getDescripcion());
                detalleError += r.getDescripcion();

                for (int i = 0; i < updArticulos.getExistencias().size(); i++) {
                    detalleError += updArticulos.getExistencias().get(i).getIdArticulo();
                    if (i < updArticulos.getExistencias().size() - 1) {
                        detalleError += ", ";
                    } else {
                        detalleError += ". ";
                    }
                }
            }

            output.setArticulos(updArticulos.getArticulos());
            output.setExistencias(updArticulos.getExistencias());

            insertHijo = true;
        } else if (updArticulos.getMensaje().equalsIgnoreCase("ERROR")) {
            // no se inserto nada
            r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ARTICULOS_NO_VALIDOS_104, null, null, null, null, input.getCodArea());
            detalleError = r.getDescripcion();
            insertHijo = false;
        } else {
            // Cualquier otro caso de fallo
            insertHijo = false;
            detalleError = "ERROR: " + updArticulos.getMensaje();
        }

        respAsignacion.setResultado(insertHijo);
        respAsignacion.setDescripcion(detalleError);
        respAsignacion.setDatos(output);

        return respAsignacion;
    }

    private static OutputAsignacion doUpdArticulosAsignacion(Connection conn, InputAsignacion input, int idPadre,
            String estadoDisponible, String estadoAlta, String estadoAsignado, BigDecimal idTipoTransaccion, BigDecimal idPais) throws SQLException {
        int tipoActualizacion = 0;
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String sql = "";
        String existencia = "";
        QueryRunner Qr = new QueryRunner();
        List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
        List<Filtro> condicionesExistenciaDestino = new ArrayList<Filtro>();
        List<InputArticuloAsignacion> listItemArticulosFail = new ArrayList<InputArticuloAsignacion>();
        InputArticuloAsignacion itemArticulosFail = new InputArticuloAsignacion();
        List<InputArticuloAsignacion> listItemArticulosStock = new ArrayList<InputArticuloAsignacion>();
        InputArticuloAsignacion itemArticulosStock = new InputArticuloAsignacion();
        List<HistoricoInv> listHistorico = new ArrayList<HistoricoInv>();

        OutputAsignacion output = new OutputAsignacion();
        output.setMensaje("OK");

        itemArticulosStock.setEstado(new ControladorBase()
                .getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_STOCK_348, null, null, null, null, input.getCodArea()).getDescripcion());
        listItemArticulosStock.add(itemArticulosStock);

        itemArticulosFail.setEstado(new ControladorBase()
                .getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_NO_EXISTENTE_349, null, null, null, null, input.getCodArea())
                .getDescripcion());
        listItemArticulosFail.add(itemArticulosFail);

        for (int j = 0; j < input.getArticulos().size(); j++) {
            condicionesExistencia.clear();
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodegaOrigen()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO, input.getArticulos().get(j).getIdArticulo()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_TIPO_INV, input.getArticulos().get(j).getTipoInv()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoDisponible));

            condicionesExistenciaDestino.clear();
            condicionesExistenciaDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodegaDestino()));
            condicionesExistenciaDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO, input.getArticulos().get(j).getIdArticulo()));
            condicionesExistenciaDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
            condicionesExistenciaDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_TIPO_INV, input.getArticulos().get(j).getTipoInv()));
            condicionesExistenciaDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoDisponible));
            
            existencia = UtileriasBD.verificarExistencia(conn, getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()), condicionesExistencia);
            if (new Integer(existencia) > 0) {
                String existenciaArticulos = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_CANTIDAD, getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()), condicionesExistencia);
                if (existenciaArticulos.equals("")) {
                    existenciaArticulos = "0";
                }

                // comprobando inventario de bodega destino
                String existenciaArticulosDestino = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_CANTIDAD,
                        getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaDestino(), input.getCodArea()), condicionesExistenciaDestino);
                if (existenciaArticulosDestino.equals("")) {
                    existenciaArticulosDestino = "0";
                }
                if (new Integer(existenciaArticulosDestino) > 0) {
                    // ya existe el articulo disponible en la bodega
                    tipoActualizacion = 1;
                } else {
                    // no existe, crearlo
                    tipoActualizacion = 0;
                }

                // con existencia en la bodega origen y sin existencia en bodega destino
                int cantSolicitada = new Integer(input.getArticulos().get(j).getCantidad());
                if (cantSolicitada > new Integer(existenciaArticulos)) {
                    output.setMensaje("GOOD");

                    itemArticulosStock = new InputArticuloAsignacion();
                    itemArticulosStock.setIdArticulo(input.getArticulos().get(j).getIdArticulo());
                    listItemArticulosStock.add(itemArticulosStock);

                } else if (cantSolicitada == new Integer(existenciaArticulos)) {
                    if (tipoActualizacion == 1) {
                        // Actualiza inventario en destino
                        String camposUpdateDestino[][] = {
                            { Inventario.CAMPO_CANTIDAD, UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
                                "(" + Inventario.CAMPO_CANTIDAD + " + " + input.getArticulos().get(j).getCantidad() + ")") },
                            { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                            { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
                        };

                        sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaDestino(), input.getCodArea()),
                                camposUpdateDestino, condicionesExistenciaDestino);
                        try {
                            Qr.update(conn, sql);
                        } catch (SQLException e) {
                            conn.rollback();
                            log.error("Rollback: Problema al actualizar los datos del inventario en bodega destino.", e);
                            Respuesta r = new ControladorBase()
                                    .getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INV_DESTINO_352, null, null, null, "", input.getCodArea());
                            output = new OutputAsignacion();
                            output.setMensaje(r.getDescripcion());

                            listaLog = new ArrayList<LogSidra>();
                            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA,
                                    servicioPostAsignacion, "0", Conf.LOG_TIPO_NINGUNO, output.getMensaje(),
                                    e.getMessage()));
                            
                            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                            return output;
                        }

                        // Elimina el articulo de bodega origen
                        sql = UtileriasBD.armarQueryDelete(getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()),
                                condicionesExistencia);
                        try {
                            Qr.update(conn, sql);
                        } catch (SQLException e) {
                            conn.rollback();
                            log.error("Rollback: Problema al actualizar los datos del inventario en bodega origen.", e);
                            Respuesta r = new ControladorBase()
                                    .getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INV_ORIGEN_351, null, null, null, "", input.getCodArea());
                            output = new OutputAsignacion();
                            output.setMensaje(r.getDescripcion());

                            listaLog = new ArrayList<LogSidra>();
                            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA,
                                    servicioPostAsignacion, "0", Conf.LOG_TIPO_NINGUNO, output.getMensaje(),
                                    e.getMessage()));
                            
                            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                            return output;
                        }
                    } else {
                        // Actualiza inventario en origen para bodega destino
                        String camposUpdate[][] = CtrlAsignacion.obtenerCamposPutDel(input, idPadre);
                        sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()),
                                camposUpdate, condicionesExistencia);

                        try {
                            Qr.update(conn, sql);
                        } catch (SQLException e) {
                            conn.rollback();
                            log.error("Rollback: Problema al actualizar los datos del inventario.", e);
                            Respuesta r = new ControladorBase()
                                    .getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105, null, null, null, null, input.getCodArea());
                            output.setMensaje(r.getDescripcion());

                            listaLog = new ArrayList<LogSidra>();
                            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA,
                                    servicioPostAsignacion, "0", Conf.LOG_TIPO_NINGUNO, output.getMensaje(),
                                    e.getMessage()));
                            
                            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                            return output;
                        }
                    }

                    // Insert detalle
                    String campos[] = CtrlAsignacion.obtenerCamposTablaHija(Conf.METODO_POST);
                    List<String> inserts = CtrlAsignacion.obtenerInsertsPostHijo( idPadre, input, j,
                            AsignacionReservaDet.SEQUENCE, estadoAsignado, idPais);
                    sql = UtileriasBD.armarQueryInsertAll(AsignacionReservaDet.N_TABLA, campos, inserts);

                    try {
                        Qr.update(conn, sql);
                    } catch (SQLException e) {
                        conn.rollback();
                        log.error("Rollback: Problema al insertar el detalle de asignacion.", e);
                        Respuesta r = new ControladorBase()
                                .getMensaje(Conf_Mensajes.MSJ_ERROR_INSERT_DET_ASIGNACION_124, null, null, null, null, input.getCodArea());
                        output.setMensaje(r.getDescripcion());

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
                                Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));

                        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                        return output;
                    }

                    // Insert historico
                    listHistorico.add(OperacionMovimientosInventario.getInsertHistoricoAsignacion(
                            input.getIdBodegaOrigen(), input.getIdBodegaDestino(), input.getArticulos().get(j),
                            idTipoTransaccion, estadoAlta, input.getUsuario(), input.getCodArea(),idPais));

                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO, servicioPostAsignacion,
                            idPadre + "", Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            input.getArticulos().get(j).getCantidad() + " art\u00EDculos identificados con el ID "
                                    + input.getArticulos().get(j).getIdArticulo() + " se asignaron de la bodega "
                                    + input.getIdBodegaOrigen() + " a la bodega " + input.getIdBodegaDestino() + ".",
                            ""));
                } else {
                    // Actualiza inventario en origen
                    String camposUpdate[][] = {
                            { Inventario.CAMPO_CANTIDAD,
                                    UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
                                            "(" + Inventario.CAMPO_CANTIDAD + " - "
                                                    + input.getArticulos().get(j).getCantidad() + ")") },
                            { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                            { Inventario.CAMPO_MODIFICADO_POR,
                                    UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };
                    
                    sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()), camposUpdate,
                            condicionesExistencia);

                    try {
                        Qr.update(conn, sql);
                    } catch (SQLException e) {
                        conn.rollback();
                        log.error("Rollback: Problema al actualizar los datos del inventario en bodega origen.", e);
                        Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INV_ORIGEN_351,
                                null, null, null, "", input.getCodArea());
                        output.setMensaje(r.getDescripcion());

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA,
                                servicioPostAsignacion, "0", Conf.LOG_TIPO_NINGUNO, output.getMensaje(),
                                e.getMessage()));
                        
                        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                        return output;
                    }

                    if (tipoActualizacion == 1) {
                        // Actualiza inventario en destino
                        String camposUpdateDestino[][] = {
                            { Inventario.CAMPO_CANTIDAD, UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
                                "(" + Inventario.CAMPO_CANTIDAD + " + " + input.getArticulos().get(j).getCantidad() + ")") },
                            { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                            { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
                        };

                        sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaDestino(), input.getCodArea()), camposUpdateDestino,
                                condicionesExistenciaDestino);

                        try {
                            Qr.update(conn, sql);
                        } catch (SQLException e) {
                            conn.rollback();
                            log.error("Rollback: Problema al actualizar los datos del inventario en bodega destino.", e);
                            Respuesta r = new ControladorBase()
                                    .getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INV_DESTINO_352, null, null, null, "", input.getCodArea());
                            output.setMensaje(r.getDescripcion());

                            listaLog = new ArrayList<LogSidra>();
                            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion,
                                    "0", Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));

                            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                            return output;
                        }
                    } else {
                        sql = OperacionMovimientosInventario.getInsertInventario(input.getIdBodegaDestino(),
                                new Integer(input.getArticulos().get(j).getCantidad()), estadoDisponible,
                                condicionesExistencia, input.getUsuario(), idPadre + "", input.getIdVendedor(), idPais);

                        try {
                            Qr.update(conn, sql);
                        } catch (SQLException e) {
                            conn.rollback();
                            log.error("Rollback: Problema al insertar los datos del inventario.", e);
                            Respuesta r = new ControladorBase()
                                    .getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105, null, null, null, null, input.getCodArea());
                            output.setMensaje(r.getDescripcion());

                            listaLog = new ArrayList<LogSidra>();
                            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion,
                                    "0", Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));

                            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                            return output;
                        }
                    }

                    // Insert detalle
                    String campos[] = CtrlAsignacion.obtenerCamposTablaHija(Conf.METODO_POST);
                    List<String> inserts = CtrlAsignacion.obtenerInsertsPostHijo( idPadre, input, j,
                            AsignacionReservaDet.SEQUENCE, estadoAsignado,idPais);
                    sql = UtileriasBD.armarQueryInsertAll(AsignacionReservaDet.N_TABLA, campos, inserts);

                    try {
                        Qr.update(conn, sql);
                    } catch (SQLException e) {
                        conn.rollback();
                        log.error("Rollback: Problema al insertar los datos del detalle de asignacion.", e);
                        Respuesta r = new ControladorBase()
                                .getMensaje(Conf_Mensajes.MSJ_ERROR_INSERT_DET_ASIGNACION_124, null, null, null, null, input.getCodArea());
                        output.setMensaje(r.getDescripcion());

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
                                Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));

                        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                        return output;
                    }

                    // Insert historico
                    listHistorico.add(OperacionMovimientosInventario.getInsertHistoricoAsignacion(
                            input.getIdBodegaOrigen(), input.getIdBodegaDestino(), input.getArticulos().get(j),
                            idTipoTransaccion, estadoAlta, input.getUsuario(), input.getCodArea(),idPais));

                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO, servicioPostAsignacion,
                            idPadre + "", Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            input.getArticulos().get(j).getCantidad() + " art\u00EDculos identificados con el ID "
                                    + input.getArticulos().get(j).getIdArticulo() + " se asignaron de la bodega "
                                    + input.getIdBodegaOrigen() + " a la bodega " + input.getIdBodegaDestino() + ".",
                            ""));
                }
            } else {
                output.setMensaje("GOOD");

                itemArticulosFail = new InputArticuloAsignacion();
                itemArticulosFail.setIdArticulo(input.getArticulos().get(j).getIdArticulo());
                listItemArticulosFail.add(itemArticulosFail);
            }
        }

        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

        // verificar si no se inserto ningun articulo para avisar
        listItemArticulosFail.remove(0);
        listItemArticulosStock.remove(0);
        int cantArticulosConError = (listItemArticulosFail.size() + listItemArticulosStock.size());
        if (cantArticulosConError >= new Integer(input.getArticulos().size())) {
            // no se agrego ninguno
            output.setMensaje("ERROR");
            output.setArticulos(null);
            output.setExistencias(null);
            log.error("Ning\u00FAn art\u00EDculo es v\u00E9lido para ingresar.");
        } else {
            // se agrego al menos uno
            output.setArticulos(listItemArticulosFail);
            output.setExistencias(listItemArticulosStock);
        }

        return output;
    }
    //--------------------------------- Operaciones de Asignacion ---------------------------------//
  
    //--------------------------------- Operaciones de Reserva ------------------------------------//
    private static RespuestaAsignacion doReservacion(Connection conn, int idPadre, InputAsignacion inputSeriado,
            InputAsignacion inputNoSeriado, String codArea, BigDecimal idPais) throws SQLException {
        OutputAsignacion respDoReservacion = new OutputAsignacion();
        RespuestaAsignacion output = new RespuestaAsignacion();
        RespuestaAsignacion respReservacionSeriada = new RespuestaAsignacion();
        RespuestaAsignacion respReservacionNoSeriada;
        String estadoDisponible = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DISPONIBLE,codArea);
        String estadoReservado = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_RESERVADO,codArea);
        String estadoReservadoDet = UtileriasJava.getConfig(conn, Conf.GRUPO_ASIGNACIONES_ESTADOS, Conf.AR_ESTADO_ART_RESERVADO,codArea);
        boolean insertSeriado = false;
        boolean insertNoSeriado = false;
        String detalleError = "";

        if (inputSeriado.getArticulos().size() > 0) {
            respReservacionSeriada = reservacionSeriada(conn, inputSeriado, idPadre, estadoDisponible, estadoReservado,
                     estadoReservadoDet, idPais);

            if (respReservacionSeriada.isResultado()) {
                detalleError = respReservacionSeriada.getDescripcion();
                insertSeriado = respReservacionSeriada.isResultado();

                respDoReservacion = respReservacionSeriada.getDatos();
                respDoReservacion.setDescErrorSeries(respReservacionSeriada.getDatos().getDescErrorSeries());
                respDoReservacion.setSeries(respReservacionSeriada.getDatos().getSeries());
            } else {
                detalleError = respReservacionSeriada.getDescripcion();
                insertSeriado = respReservacionSeriada.isResultado();

                respDoReservacion = respReservacionSeriada.getDatos();
            }
        } else {
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_ARTICULOS_SERIADOS_108, null, null,
                    null, null,codArea);
            detalleError = r.getDescripcion();
        }

        if (inputNoSeriado.getArticulos().size() > 0) {
            respReservacionNoSeriada = reservacionNoSeriada(conn, inputNoSeriado, idPadre, estadoDisponible,
                    estadoReservado, estadoReservadoDet, idPais);

            if (respReservacionNoSeriada.isResultado()) {
                insertNoSeriado = respReservacionNoSeriada.isResultado();
                detalleError += " " + respReservacionNoSeriada.getDescripcion();

                respDoReservacion = respReservacionNoSeriada.getDatos();
                if (respReservacionSeriada.getDatos() != null) {
                    respDoReservacion.setDescErrorSeries(respReservacionSeriada.getDatos().getDescErrorSeries());
                    respDoReservacion.setSeries(respReservacionSeriada.getDatos().getSeries());
                }
                respDoReservacion.setDescErrorArticulos(respReservacionNoSeriada.getDatos().getDescErrorArticulos());
                respDoReservacion.setArticulos(respReservacionNoSeriada.getDatos().getArticulos());
                respDoReservacion.setDescErrorExistencias(respReservacionNoSeriada.getDatos().getDescErrorExistencias());
                respDoReservacion.setExistencias(respReservacionNoSeriada.getDatos().getExistencias());
            } else {
                insertNoSeriado = respReservacionNoSeriada.isResultado();
                detalleError += " " + respReservacionNoSeriada.getDescripcion();

                respDoReservacion = respReservacionNoSeriada.getDatos();
            }
        } else {
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_ARTICULOS_NO_SERIADOS_109, null, null,
                    null, null,codArea);
            detalleError += " " + r.getDescripcion();
        }

        if (insertSeriado == true || insertNoSeriado == true) {
            output.setResultado(true);
        } else {
            output.setResultado(false);
        }
        output.setDatos(respDoReservacion);
        output.setDescripcion(detalleError);

        return output;
    }

    private static RespuestaAsignacion reservacionSeriada(Connection conn, InputAsignacion input, int idPadre,
            String estadoDisponible, String estadoReservado, String estadoReservadoDet, BigDecimal idPais) throws SQLException {
        RespuestaAsignacion respReservacion = new RespuestaAsignacion();
        OutputAsignacion output = new OutputAsignacion();
        boolean insertHijo = false;
        String detalleError = "";

        List<InputArticuloAsignacion> updSeries = doUpdSeriesReserva(conn, idPadre, input, estadoDisponible,
                estadoReservado,  estadoReservadoDet, idPais);

        if (updSeries.get(0).getEstado().equalsIgnoreCase("OK")) {
            insertHijo = true;
        } else if (updSeries.get(0).getEstado().equalsIgnoreCase("GOOD")) {
            if (updSeries.size() > 1) {
                updSeries.remove(0);

                detalleError = new ControladorBase()
                        .getMensaje(Conf_Mensajes.MSJ_TITULO_SOLICITUD_SERIADA_353, null, null, null, null, input.getCodArea())
                        .getDescripcion();
           
                output.setDescErrorSeries(new ControladorBase()
                        .getMensaje(Conf_Mensajes.MSJ_ERROR_SERIES_VALIDAS_BODEGA_342, null, null, null, null, input.getCodArea())
                        .getDescripcion());
                output.setSeries(updSeries);
            }
            insertHijo = true;
        } else if (updSeries.get(0).getEstado().equalsIgnoreCase("ERROR")) {
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_SERIES_NO_VALIDAS_107, null, null, null,
                    null, input.getCodArea());
            detalleError = r.getDescripcion();
            insertHijo = false;

        } else {
            // Cualquier caso de fallo

            detalleError = "ERROR: " + updSeries.get(0).getEstado();
            insertHijo = false;
        }

        respReservacion.setResultado(insertHijo);
        respReservacion.setDescripcion(detalleError);
        respReservacion.setDatos(output);

        return respReservacion;
    }

    private static List<InputArticuloAsignacion> doUpdSeriesReserva(Connection conn, int idPadre, InputAsignacion input,
            String estadoDisponible, String estadoReservado,  String estadoReservadoDet, BigDecimal idPais) throws SQLException {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();

        String sql = "";
        String existencia = "";

        QueryRunner Qr = new QueryRunner();

        List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
        List<InputArticuloAsignacion> listItemSeriesFail = new ArrayList<InputArticuloAsignacion>();

        InputArticuloAsignacion itemSeriesFail = new InputArticuloAsignacion();
        itemSeriesFail.setEstado("OK");
        listItemSeriesFail.add(itemSeriesFail);

        for (int j = 0; j < input.getArticulos().size(); j++) {
            if (input.getArticulos().get(j).getSerieFinal() != null
                    && !"".equals(input.getArticulos().get(j).getSerieFinal())) {
                // validaci\u00F3n y proceso de rango de series
                String seriesInvalidas = OperacionMovimientosInventario.validarSeries(conn,
                        input.getArticulos().get(j).getSerie(), input.getArticulos().get(j).getSerieFinal(),
                        input.getIdBodegaOrigen(), 0, estadoDisponible, input.getArticulos().get(j).getTipoInv(),input.getCodArea());

                if (seriesInvalidas.equalsIgnoreCase("OK")) {
                    // procesos para hacer el update de las series

                    String seriesActualizadas = OperacionMovimientosInventario.actualizarSeriesReserva(conn,
                            new BigInteger(input.getArticulos().get(j).getSerie()),
                            new BigInteger(input.getArticulos().get(j).getSerieFinal()), input.getIdBodegaOrigen(),
                           idPadre, input.getIdVendedor(), input.getUsuario(),
                            estadoDisponible, estadoReservado, input.getArticulos().get(j).getTipoInv(), idPais, input.getCodArea());

                    if (seriesActualizadas.equalsIgnoreCase("OK")) {
                        // Insert detalle
                        String[] campos = CtrlAsignacion.obtenerCamposTablaHija(Conf.METODO_POST);
                        List<String> inserts = CtrlAsignacion.obtenerInsertsPostHijo( idPadre, input, j,
                                AsignacionReservaDet.SEQUENCE, estadoReservadoDet, idPais);
                        sql = UtileriasBD.armarQueryInsertAll(AsignacionReservaDet.N_TABLA, campos, inserts);

                        try {
                            Qr.update(conn, sql);
                        } catch (SQLException e) {
                            conn.rollback();
                            log.error("Rollback: problema al insertar detalle de asignacion.", e);
                            Respuesta r = new ControladorBase().getMensaje(
                                    Conf_Mensajes.MSJ_ERROR_INSERT_DET_ASIGNACION_124, null, null, null, null, input.getCodArea());
                            listItemSeriesFail.get(0).setEstado(r.getDescripcion());

                            listaLog = new ArrayList<LogSidra>();
                            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion,
                                    "0", Conf.LOG_TIPO_NINGUNO, listItemSeriesFail.get(0).getEstado(), e.getMessage()));

                            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                            return listItemSeriesFail;
                        }                       

                    

                        // Insert log
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO, servicioPostAsignacion,
                                idPadre + "", Conf.LOG_TIPO_ASIGNACION_RESERVA,
                                "Se reserv\u00F3 el rango de series " + input.getArticulos().get(j).getSerie() + " - "
                                        + input.getArticulos().get(j).getSerieFinal() + " de la bodega "
                                        + input.getIdBodegaOrigen() + " para la bodega " + input.getIdBodegaDestino()
                                        + ", se cambi\u00F3 el estado de " + estadoDisponible.toUpperCase() + " a "
                                        + estadoReservado.toUpperCase() + ".",
                                ""));

                    } else {
                        // se deshace todo porque no fueron actualizadas todas las series
                        conn.rollback();
                        log.error("Rollback: problema al actualizar las series.");
                        Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105,
                                null, null, null, null, input.getCodArea());
                        listItemSeriesFail.get(0).setEstado(r.getDescripcion());

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
                                Conf.LOG_TIPO_NINGUNO, listItemSeriesFail.get(0).getEstado(),
                                "Fallaron las series: " + seriesActualizadas));

                        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                        return listItemSeriesFail;
                    }
                } else {
                    listItemSeriesFail.get(0).setEstado("GOOD");
                    itemSeriesFail = new InputArticuloAsignacion();
                    itemSeriesFail.setSerie(input.getArticulos().get(j).getSerie());
                    itemSeriesFail.setSerieFinal(input.getArticulos().get(j).getSerieFinal());
                    listItemSeriesFail.add(itemSeriesFail);
                }

            } else {
                // validaci\u00F3n y proceso normal de una serie
                int cantSeries = 0;
                condicionesExistencia.clear();
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodegaOrigen()));
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
                
                if (input.getArticulos().get(j).getSerieAsociada() != null && !input.getArticulos().get(j).getSerieAsociada().equals("")) {
                    condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Inventario.CAMPO_SERIE,
                            "'" + input.getArticulos().get(j).getSerie() + "','" + input.getArticulos().get(j).getSerieAsociada() + "'"));
                    cantSeries = 1;
                } else {
                    condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_SERIE, input.getArticulos().get(j).getSerie()));
                }
                
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_TIPO_INV, input.getArticulos().get(j).getTipoInv()));
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoDisponible));
                
                existencia = UtileriasBD.verificarExistencia(conn, getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()), condicionesExistencia);
                if (new Integer(existencia) > cantSeries) {
                    //Update
                    String camposUpdate[][] = {
                        { Inventario.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoReservado) },
                        { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                        { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) },
                        { Inventario.CAMPO_TCSCASIGNACIONRESERVAID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, idPadre+"") },
                        { Inventario.CAMPO_IDVENDEDOR, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getIdVendedor()) }
                    };
                    
                    sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()), camposUpdate, condicionesExistencia);
                    
                    try {
                        Qr.update(conn, sql);
                    } catch (SQLException e) {
                        conn.rollback();
                        log.error("Rollback: Problema al actualizar los datos del inventario.", e);
                        Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105, null, null, null, null, input.getCodArea());
                        listItemSeriesFail.get(0).setEstado(r.getDescripcion());

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
                                Conf.LOG_TIPO_NINGUNO, listItemSeriesFail.get(0).getEstado(), e.getMessage()));

                        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                        return listItemSeriesFail;
                    }

                    //Insert detalle
                    String[] campos = CtrlAsignacion.obtenerCamposTablaHija(Conf.METODO_POST);
                    List<String> inserts = CtrlAsignacion.obtenerInsertsPostHijo( idPadre, input, j, AsignacionReservaDet.SEQUENCE, estadoReservadoDet, idPais);
                    sql = UtileriasBD.armarQueryInsertAll(AsignacionReservaDet.N_TABLA, campos, inserts);

                    try {
                        Qr.update(conn, sql);
                    } catch (SQLException e) {
                        conn.rollback();
                        log.error("Rollback: problema al insertar detalle de asignacion.", e);
                        Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_INSERT_DET_ASIGNACION_124, null, null, null, null, input.getCodArea());
                        listItemSeriesFail.get(0).setEstado(r.getDescripcion());

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
                                Conf.LOG_TIPO_NINGUNO, listItemSeriesFail.get(0).getEstado(), e.getMessage()));

                        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                        return listItemSeriesFail;
                    }

                 
                    
                    listaLog.add(
                            addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO, servicioPostAsignacion, idPadre + "",
                                    Conf.LOG_TIPO_ASIGNACION_RESERVA,
                                    "Se reserv\u00F3 el art\u00EDculo con serie " + input.getArticulos().get(j).getSerie()
                                            + ((cantSeries > 0) ? " y su serie asociada "
                                                    + input.getArticulos().get(j).getSerieAsociada() : "")
                                    + " de la bodega " + input.getIdBodegaOrigen() + " para la bodega "
                                    + input.getIdBodegaDestino() + ", se cambi\u00F3 el estado de "
                                    + estadoDisponible.toUpperCase() + " a " + estadoReservado.toUpperCase() + ".",
                            ""));
                } else {
                    listItemSeriesFail.get(0).setEstado("GOOD");
                    itemSeriesFail = new InputArticuloAsignacion();
                    itemSeriesFail.setSerie(input.getArticulos().get(j).getSerie());
                    listItemSeriesFail.add(itemSeriesFail);
                }
            }
        }

        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

        // verficar si no se inserto ningun articulo para avisar que se no guarda la Asignacion
        int cantArticulosConError = (listItemSeriesFail.size() - 1);
        if (cantArticulosConError >= new Integer(input.getArticulos().size())) {
            // no se agrego ninguno
            listItemSeriesFail.clear();
            itemSeriesFail.setEstado("ERROR");
            listItemSeriesFail.add(itemSeriesFail);
            log.debug("Ninguna serie es v\u00E9lida para ingresar.");
        }

        return listItemSeriesFail;
    }

    private static RespuestaAsignacion reservacionNoSeriada(Connection conn, InputAsignacion input, int idPadre,
            String estadoDisponible, String estadoReservado,  String estadoReservadoDet, BigDecimal idPais) throws SQLException {
        RespuestaAsignacion respReservacion = new RespuestaAsignacion();
        OutputAsignacion output = new OutputAsignacion();
        boolean insertHijo = false;
        String detalleError = "";
        Respuesta r;

        OutputAsignacion updArticulos ;
        updArticulos = doUpdArticulosReserva(conn, idPadre, input, estadoDisponible, estadoReservado, 
                estadoReservadoDet, idPais);

        // si los mensajes de respuesta (mas los listados) son correctos retorna true
        if (updArticulos.getMensaje().equalsIgnoreCase("OK")) {
            // todo bien insertado y actualizado
            insertHijo = true;

        } else if (updArticulos.getMensaje().equalsIgnoreCase("GOOD")) {
            // se insertaron varios pero hay unos q no xq no existen en bodega o no existen tienen stock suficiente
            detalleError = new ControladorBase()
                    .getMensaje(Conf_Mensajes.MSJ_TITULO_SOLICITUD_NO_SERIADA_354, null, null, null, null, input.getCodArea())
                    .getDescripcion();

            if (updArticulos.getArticulos().size() > 0) {
                r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_ARTICULOS_NO_EXISTEN_345, null, null,
                        null, null, input.getCodArea());
                output.setDescErrorArticulos(r.getDescripcion());
                detalleError += r.getDescripcion();

                for (int i = 0; i < updArticulos.getArticulos().size(); i++) {
                    detalleError += updArticulos.getArticulos().get(i).getIdArticulo();
                    if (i < updArticulos.getArticulos().size() - 1) {
                        detalleError += ", ";
                    } else {
                        detalleError += ". ";
                    }
                }
            }

            if (updArticulos.getExistencias().size() > 0) {
                r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_ARTICULOS_STOCK_346, null, null, null,
                        null, input.getCodArea());
                output.setDescErrorExistencias(r.getDescripcion());
                detalleError += r.getDescripcion();

                for (int i = 0; i < updArticulos.getExistencias().size(); i++) {
                    detalleError += updArticulos.getExistencias().get(i).getIdArticulo();
                    if (i < updArticulos.getExistencias().size() - 1) {
                        detalleError += ", ";
                    } else {
                        detalleError += ". ";
                    }
                }
            }

            insertHijo = true;
            output.setArticulos(updArticulos.getArticulos());
            output.setExistencias(updArticulos.getExistencias());

        } else if (updArticulos.getMensaje().equalsIgnoreCase("ERROR")) {
            // no se inserto nada

            respReservacion.setResultado(false);
            r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ARTICULOS_NO_VALIDOS_104, null, null, null, null, input.getCodArea());
            respReservacion.setDescripcion(r.getDescripcion());
            return respReservacion;
        } else {
            // Cualquier caso de fallo

            respReservacion.setResultado(false);
            respReservacion.setDescripcion("ERROR: " + updArticulos.getMensaje());
            return respReservacion;
        }

        respReservacion.setResultado(insertHijo);
        respReservacion.setDescripcion(detalleError);
        respReservacion.setDatos(output);

        return respReservacion;
    }

    private static OutputAsignacion doUpdArticulosReserva(Connection conn, int idPadre, InputAsignacion input,
            String estadoDisponible, String estadoReservado,  String estadoReservadoDet, BigDecimal idPais) throws SQLException {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();

        String sql = "";
        String existencia = "";

        QueryRunner Qr = new QueryRunner();

        List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
        List<InputArticuloAsignacion> listItemArticulosFail = new ArrayList<InputArticuloAsignacion>();
        InputArticuloAsignacion itemArticulosFail = new InputArticuloAsignacion();

        List<InputArticuloAsignacion> listItemArticulosStock = new ArrayList<InputArticuloAsignacion>();
        InputArticuloAsignacion itemArticulosStock = new InputArticuloAsignacion();

        OutputAsignacion output = new OutputAsignacion();
        output.setMensaje("OK");

        itemArticulosStock.setEstado(new ControladorBase()
                .getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_STOCK_348, null, null, null, null, input.getCodArea()).getDescripcion());
        listItemArticulosStock.add(itemArticulosStock);

        itemArticulosFail.setEstado(new ControladorBase()
                .getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_NO_EXISTENTE_349, null, null, null, null, input.getCodArea())
                .getDescripcion());
        listItemArticulosFail.add(itemArticulosFail);

        for (int j = 0; j < input.getArticulos().size(); j++) {
            condicionesExistencia.clear();
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodegaOrigen()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO, input.getArticulos().get(j).getIdArticulo()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoDisponible));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_TIPO_INV, input.getArticulos().get(j).getTipoInv()));

            existencia = UtileriasBD.verificarExistencia(conn, getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()), condicionesExistencia);
            if (new Integer(existencia) > 0) {
                String existenciaArticulos = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_CANTIDAD,
                        getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()), condicionesExistencia);
                int cantSolicitada = new Integer(input.getArticulos().get(j).getCantidad());
                if (cantSolicitada > new Integer(existenciaArticulos)) {
                    output.setMensaje("GOOD");

                    itemArticulosStock = new InputArticuloAsignacion();
                    itemArticulosStock.setIdArticulo(input.getArticulos().get(j).getIdArticulo());
                    listItemArticulosStock.add(itemArticulosStock);

                } else if (cantSolicitada == new Integer(existenciaArticulos)) {
                    // Update
                    String camposUpdate[][] = {
                        { Inventario.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoReservado) },
                        { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                        { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) },
                        { Inventario.CAMPO_TCSCASIGNACIONRESERVAID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, idPadre+"") },
                        { Inventario.CAMPO_IDVENDEDOR, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getIdVendedor()) }
                    };

                    sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()),
                            camposUpdate, condicionesExistencia);

                    try {
                        Qr.update(conn, sql);
                    } catch (SQLException e) {
                        conn.rollback();
                        output = new OutputAsignacion();
                        log.error("Rollback: Problema al actualizar los datos del inventario.", e);
                        Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105,
                                null, null, null, null, input.getCodArea());
                        output.setMensaje(r.getDescripcion());

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
                                Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));

                        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                        return output;
                    }

                    // Insert detalle
                    String campos[] = CtrlAsignacion.obtenerCamposTablaHija(Conf.METODO_POST);
                    List<String> inserts = CtrlAsignacion.obtenerInsertsPostHijo( idPadre, input, j,
                            AsignacionReservaDet.SEQUENCE, estadoReservadoDet, idPais);
                    sql = UtileriasBD.armarQueryInsertAll(AsignacionReservaDet.N_TABLA, campos, inserts);

                    try {
                        Qr.update(conn, sql);
                    } catch (SQLException e) {
                        conn.rollback();
                        log.error("Rollback: Problema al insertar el detalle de asignacion.", e);
                        Respuesta r = new ControladorBase()
                                .getMensaje(Conf_Mensajes.MSJ_ERROR_INSERT_DET_ASIGNACION_124, null, null, null, null, input.getCodArea());
                        output.setMensaje(r.getDescripcion());

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
                                Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));

                        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                        return output;
                    }

                   

                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO, servicioPostAsignacion,
                            idPadre + "", Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            input.getArticulos().get(j).getCantidad() + " art\u00EDculos identificados con el ID "
                                    + input.getArticulos().get(j).getIdArticulo() + " se reservaron de la bodega "
                                    + input.getIdBodegaOrigen() + " para la bodega " + input.getIdBodegaDestino()
                                    + ", se cambi\u00F3 el estado de " + estadoDisponible.toUpperCase() + " a "
                                    + estadoReservado.toUpperCase() + ".",
                            ""));
                } else {
                    // Update
                    String camposUpdate[][] = {
                        { Inventario.CAMPO_CANTIDAD, UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
                            "(" + Inventario.CAMPO_CANTIDAD + " - " + input.getArticulos().get(j).getCantidad() + ")") },
                        { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                        { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
                    };

                    sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodegaOrigen(), input.getCodArea()), 
                            camposUpdate, condicionesExistencia);

                    try {
                        Qr.update(conn, sql);
                    } catch (SQLException e) {
                        conn.rollback();
                        log.error("Rollback: Problema al actualizar los datos del inventario.", e);
                        output = new OutputAsignacion();
                        Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105,
                                null, null, null, null, input.getCodArea());
                        output.setMensaje(r.getDescripcion());

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
                                Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));

                        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                        return output;
                    }

                    // Insert inventario
                    sql = OperacionMovimientosInventario.getInsertInventario(input.getIdBodegaOrigen(),
                            new Integer(input.getArticulos().get(j).getCantidad()), estadoReservado,
                            condicionesExistencia, input.getUsuario(), idPadre + "", input.getIdVendedor(), idPais);
                    try {
                        Qr.update(conn, sql);
                    } catch (SQLException e) {
                        conn.rollback();
                        log.error("Rollback: Problema al insertar los datos del inventario.", e);
                        output = new OutputAsignacion();
                        Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_UPDATE_INVENTARIO_105,
                                null, null, null, null, input.getCodArea());
                        output.setMensaje(r.getDescripcion());

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
                                Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));

                        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                        return output;
                    }

                    // Insert detalle
                    String campos[] = CtrlAsignacion.obtenerCamposTablaHija(Conf.METODO_POST);
                    List<String> inserts = CtrlAsignacion.obtenerInsertsPostHijo( idPadre, input, j,
                            AsignacionReservaDet.SEQUENCE, estadoReservadoDet, idPais);
                    sql = UtileriasBD.armarQueryInsertAll(AsignacionReservaDet.N_TABLA, campos, inserts);

                    try {
                        Qr.update(conn, sql);
                    } catch (SQLException e) {
                        conn.rollback();
                        log.error("Rollback: Problema al insertar el detalle de asignacion.", e);
                        Respuesta r = new ControladorBase()
                                .getMensaje(Conf_Mensajes.MSJ_ERROR_INSERT_DET_ASIGNACION_124, null, null, null, null, input.getCodArea());
                        output.setMensaje(r.getDescripcion());

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPostAsignacion, "0",
                                Conf.LOG_TIPO_NINGUNO, output.getMensaje(), e.getMessage()));

                        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                        return output;
                    }

                

                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO, servicioPostAsignacion,
                            idPadre + "", Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            input.getArticulos().get(j).getCantidad() + " art\u00EDculos identificados con el ID "
                                    + input.getArticulos().get(j).getIdArticulo() + " se reservaron de la bodega "
                                    + input.getIdBodegaOrigen() + " para la bodega " + input.getIdBodegaDestino()
                                    + ", se cambi\u00F3 el estado de " + estadoDisponible.toUpperCase() + " a "
                                    + estadoReservado.toUpperCase() + ".",
                            ""));
                }
            } else {
                output.setMensaje("GOOD");

                itemArticulosFail = new InputArticuloAsignacion();
                itemArticulosFail.setIdArticulo(input.getArticulos().get(j).getIdArticulo());
                listItemArticulosFail.add(itemArticulosFail);
            }
        }

        UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

        // verficar si no se inserto ningun articulo para avisar que se no guarda la reserva
        listItemArticulosFail.remove(0);
        listItemArticulosStock.remove(0);
        int cantArticulosConError = (listItemArticulosFail.size() + listItemArticulosStock.size());
        if (cantArticulosConError >= new Integer(input.getArticulos().size())) {
            // no se agrego ninguno
            output.setMensaje("ERROR");
            output.setArticulos(null);
            output.setExistencias(null);
            log.error("Ning\u00FAn art\u00EDculo es valido para ingresar.");
        } else {
            // se agrego al menos uno
            output.setArticulos(listItemArticulosFail);
            output.setExistencias(listItemArticulosStock);
        }

        return output;
    }
    //--------------------------------- Operaciones de Reserva ------------------------------------//
    //--------------------------------- Operaciones para crear Asignaciones/Reservas --------------//
    
    //--------------------------------- Operaciones para asignar o cancelar Reservas --------------//
    public static OutputAsignacion doModReserva(Connection conn, InputAsignacion input, int metodo, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "doPut";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputAsignacion output = null;
        List<HistoricoInv> listHistorico = new ArrayList<HistoricoInv>();
        HistoricoInv historico = new HistoricoInv();
        List<LogSidra> listaLog = new ArrayList<LogSidra>();

        String idBodegaOrigen = null;
        String idBodegaDestino = null;
        String estadoActual = null;
        String tipoActual = null;
        String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
        String estadoFinalizada = null;
        String tipoAsignacion = null;
        String tipoReserva = null;
        String estadoReservado = null;
        String estadoDisponible = null;
        String estadoAsignado = null;
        String estadoCancelado = null;
        String idVendedor=null;
        String creadoEL=null;
        BigDecimal idTipoTransaccion = getIdTransaccion(conn, Conf.CODIGO_TRANSACCION_ASIGNA_RESERVA, idPais);
        String tablaAsignacionConParticion = getParticion(AsignacionReserva.N_TABLA, Conf.PARTITION, "", input.getCodArea());
        String camposConfig[] = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };

        List<Map<String, String>> datosConfig = new ArrayList<Map<String, String>>();
        // Se obtienen las configuraciones.
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ASIGNACIONES_TIPO));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ASIGNACIONES_ESTADOS));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS_INVENTARIO));
        try {
            datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, camposConfig, condiciones, null);
        } catch (SQLException e) {
            log.error("Error al obtener par\u00E9metros de configuraci\u00F3n. " + e);
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_GET_CONFIG_103, null, nombreClase,
                    nombreMetodo, e.getMessage(), input.getCodArea());

            output = new OutputAsignacion();
            output.setRespuesta(respuesta);

            return output;
        }

        for (int i = 0; i < datosConfig.size(); i++) {
            if (Conf.AR_ESTADO_FINALIZADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                estadoFinalizada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.AR_ESTADO_ART_ASIGNADO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                estadoAsignado = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.AR_ESTADO_ART_CANCELADO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                estadoCancelado = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.AR_TIPO_ASIGNACION.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                tipoAsignacion = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.AR_TIPO_RESERVA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                tipoReserva = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.INV_EST_RESERVADO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE).toUpperCase())) {
                estadoReservado = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.INV_EST_DISPONIBLE.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE).toUpperCase())) {
                estadoDisponible = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
        }
        log.trace("estado asignado:"+estadoAsignado);
        condiciones = CtrlAsignacion.obtenerCondiciones(input, metodo, idPais);

        String campos[] = {
                AsignacionReserva.CAMPO_TIPO,
                AsignacionReserva.CAMPO_ESTADO,
                AsignacionReserva.CAMPO_BODEGA_ORIGEN,
                AsignacionReserva.CAMPO_BODEGA_DESTINO,
                AsignacionReserva.CAMPO_IDVENDEDOR,
                "to_char(CREADO_EL,'YYYYMMDD')",
        };
        Map<String, String> datos = UtileriasBD.getSingleFirstData(conn, tablaAsignacionConParticion, campos,
                condiciones);

        if (datos.isEmpty()) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());

            output = new OutputAsignacion();
            output.setRespuesta(respuesta);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPutAsignacion,
                    input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
                    output.getRespuesta().getDescripcion(), ""));
            
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

            return output;
        } else {
            idBodegaOrigen = datos.get(AsignacionReserva.CAMPO_BODEGA_ORIGEN);
            idBodegaDestino = datos.get(AsignacionReserva.CAMPO_BODEGA_DESTINO);
            estadoActual = datos.get(AsignacionReserva.CAMPO_ESTADO);
            tipoActual = datos.get(AsignacionReserva.CAMPO_TIPO);
            idVendedor= datos.get(AsignacionReserva.CAMPO_IDVENDEDOR);            
            creadoEL = datos.get("to_char(CREADO_EL,'YYYYMMDD')");
            log.trace("creado el:"+creadoEL);
       
            if (input.getEstado().equalsIgnoreCase(estadoFinalizada)&& (verificarJornadasLiquidadas(conn, datos.get(AsignacionReserva.CAMPO_IDVENDEDOR),
                        idBodegaDestino, input.getCodArea(), idPais) > 0)) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_JORNADAS_NO_LIQUIDADAS_794,
                            null, nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputAsignacion();
                    output.setRespuesta(respuesta);

                    listaLog = new ArrayList<LogSidra>();
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPutAsignacion,
                            input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            output.getRespuesta().getDescripcion(), ""));

                    UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                    return output;
            }

            historico.setTcsctipotransaccionid(idTipoTransaccion);
            historico.setBodega_origen(new BigDecimal(idBodegaOrigen));
            historico.setBodega_destino(new BigDecimal(idBodegaDestino));
            historico.setCod_num(null);
            historico.setEstado(estadoAlta);
            historico.setCreado_por(input.getUsuario());

            if (tipoActual.equalsIgnoreCase(tipoAsignacion)) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_TIPO_RESERVA_111, null,
                        nombreClase, nombreMetodo, null, input.getCodArea());

                output = new OutputAsignacion();
                output.setRespuesta(respuesta);

                listaLog = new ArrayList<LogSidra>();
                listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPutAsignacion,
                        input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
                        output.getRespuesta().getDescripcion(), ""));

                UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                return output;
            } else {
                if (!estadoActual.equalsIgnoreCase(estadoAlta.toUpperCase())) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_ESTADO_RESERVA_112, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputAsignacion();
                    output.setRespuesta(respuesta);

                    listaLog = new ArrayList<LogSidra>();
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPutAsignacion,
                            input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            output.getRespuesta().getDescripcion(), ""));

                    UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                    return output;
                }
            }
        }

        String sql = null;
        String tipoOperacion = null;
        if (input.getEstado().equalsIgnoreCase(estadoFinalizada)) {
            tipoOperacion = tipoAsignacion;
            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPutAsignacion,
                    input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
                    "Se cambi\u00F3 el tipo de " + tipoActual.toUpperCase() + " a " + tipoOperacion.toUpperCase() + ".",
                    ""));
        } else {
            tipoOperacion = tipoReserva;
        }

        String camposAR[][] = {
            { AsignacionReserva.CAMPO_TIPO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, tipoOperacion) },
            { AsignacionReserva.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getEstado()) },
            { AsignacionReserva.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
            { AsignacionReserva.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) },
            { AsignacionReserva.CAMPO_OBSERVACIONES, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getObservaciones().trim()) }
        };

        listaLog.add(addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_OPERACION, servicioPutAsignacion,
                input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
                "Se cambi\u00F3 el estado de " + estadoActual.toUpperCase() + " a " + input.getEstado().toUpperCase() + ".",
                ""));

        sql = UtileriasBD.armarQueryUpdate(tablaAsignacionConParticion, camposAR, condiciones);

        try {
            conn.setAutoCommit(false);
            QueryRunner Qr = new QueryRunner();
            Qr.update(conn, sql);

            if (Qr != null) {
                boolean update = false;

                List<InputArticuloAsignacion> datosDetalles = getDatosDetalles(conn, input, idPais);

                String estadoDetalle;
                if (input.getEstado().equalsIgnoreCase(estadoFinalizada)) {
                    tipoOperacion = "FIN";
                    // todos los articulos se pasan a la NUEVA bodega con estado disponible
                    estadoDetalle = estadoAsignado;
                } else {
                    tipoOperacion = "CAN";
                    // los articulos se quedan en la MISMA bodega con estado disponible y sin idAsignacionReserva
                    estadoDetalle = estadoCancelado;
                }

                /*actualizando el detalle de la reserva a asignado o cancelado*/
                if(!datosDetalles.isEmpty()){
					update = updateInvAsignacion(conn, tipoOperacion,
							idBodegaOrigen, idBodegaDestino, input,
							estadoDisponible, estadoReservado, 
							 idVendedor, input.getCodArea(), idPais);
					
					
					  if (update) {
	                        // Actualiza detalle de asignacion
	                        String camposUpdDetalle[][] = {
	                                { AsignacionReservaDet.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoDetalle) },
	                                { AsignacionReservaDet.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
	                                { AsignacionReservaDet.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };

	                        condiciones.clear();
	                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
	                                AsignacionReservaDet.CAMPO_TCSCASIGNACIONID,
	                               input.getIdAsignacionReserva()));
	                        sql = UtileriasBD.armarQueryUpdate("TC_SC_ASIGNACION_DET PARTITION (D"+creadoEL+")" , camposUpdDetalle, condiciones);
	                        try {
	                            Qr.update(conn, sql);
	                            update = true;
        
	                        } catch (SQLException e) {
	                            conn.rollback();
	                            log.error("Rollback: problema al actualizar detalle de asignacion.", e);
	                            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_PROCESO_RESERVA_113,
	                                    null, nombreClase, nombreMetodo, "Problema al actualizar detalle de asignacion.", input.getCodArea());
	                            update = false;
	                            output = new OutputAsignacion();
	                            output.setRespuesta(respuesta);

	                            listaLog = new ArrayList<LogSidra>();
	                            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPutAsignacion,
	                                    "0", Conf.LOG_TIPO_NINGUNO, "Problema al actualizar detalle de asignacion.",
	                                    e.getMessage()));

	                            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

	                            return output;
	                        }


	                    }
					  
			           if (!update) {
	                        log.error("Rollback");
	                        conn.rollback();
	                    }
			           
			           if(update){
			           /*vamos a insertar log e historico de lo actualizado*/
			           for (int i = 0; i < datosDetalles.size(); i++) {
		  

		                        // Inserta hist\u00F3rico por detalle en caso de finalizarse la reserva
			        	   if (datosDetalles.get(i).getSerie()!=null || !"".equals(datosDetalles.get(i).getSerie())){
		                   if (tipoOperacion.equalsIgnoreCase("FIN")) {
		                       listHistorico.add(OperacionMovimientosInventario.getInsertHistoricoAsignacion(
		                        idBodegaOrigen, idBodegaDestino, datosDetalles.get(i), idTipoTransaccion,
		                         estadoAlta, input.getUsuario(), input.getCodArea(),idPais));
		                       
		                       // log cuando se finaliza y asigna la reserva
		                       if (datosDetalles.get(i).getSerieFinal() != null && !datosDetalles.get(i).getSerieFinal().equals("")) {
		                           listaLog.add(addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO, servicioPutAsignacion,
		                                   input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
		                                   "Se asign\u00F3 la reservaci\u00F3n del rango de series " + datosDetalles.get(i).getSerie() + " - "
		                                           + datosDetalles.get(i).getSerieFinal() + " del art\u00EDculo ID " + datosDetalles.get(i).getIdArticulo()
		                                           + ", de la bodega " + idBodegaOrigen + " a la bodega " + idBodegaDestino
		                                           + ", se cambi\u00F3 el estado de " + estadoReservado.toUpperCase() + " a "
		                                           + estadoDisponible.toUpperCase() + ".",
		                                   ""));
		                       } else {
		                           listaLog.add(addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO, servicioPutAsignacion,
		                                   input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
		                                   "Se asign\u00F3 el art\u00EDculo reservado con serie " + datosDetalles.get(i).getSerie()
		                                           + ((datosDetalles.get(i).getSerieFinal() != null && !datosDetalles.get(i).getSerieFinal().equals(""))
		                                                   ? " y su serie asociada " + datosDetalles.get(i).getSerieAsociada() : "")
		                                           + " de la bodega " + idBodegaOrigen + " a la bodega " + idBodegaDestino
		                                           + ", se cambi\u00F3 el estado de " + estadoReservado.toUpperCase() + " a "
		                                           + estadoDisponible.toUpperCase() + ".",
		                                   ""));
		                       }
		                    }else{
		                        // log cuando se cancela la reserva
		                        if (datosDetalles.get(i).getSerieFinal() != null && !datosDetalles.get(i).getSerieFinal().equals("")) {
		                            listaLog.add(addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO, servicioPutAsignacion,
		                                    input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
		                                    "Se cancel\u00F3 la reservaci\u00F3n del rango de series " + datosDetalles.get(i).getSerie() + " - "
		                                            + datosDetalles.get(i).getSerieFinal() + " del art\u00EDculo ID " + datosDetalles.get(i).getIdArticulo()
		                                            + ", se cambi\u00F3 el estado de " + estadoReservado.toUpperCase() + " a "
		                                            + estadoDisponible.toUpperCase() + ".",
		                                    ""));
		                        } else {
		                            listaLog.add(addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO, servicioPutAsignacion,
		                                    input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
		                                    "Se cancel\u00F3 la reservaci\u00F3n del art\u00EDculo con serie " + datosDetalles.get(i).getSerie()
		                                            + ((datosDetalles.get(i).getSerieFinal() != null && !datosDetalles.get(i).getSerieFinal().equals(""))
		                                                    ? " y su serie asociada " + datosDetalles.get(i).getSerieAsociada() : "")
		                                            + ", se cambi\u00F3 el estado de " + estadoReservado.toUpperCase() + " a "
		                                            + estadoDisponible.toUpperCase() + ".",
		                                    ""));
		                        }
		                    }
			        	   }else{
			        	        if (tipoOperacion.equalsIgnoreCase("FIN")) {
			        	           
			        	        	listHistorico.add(OperacionMovimientosInventario.getInsertHistoricoAsignacion(
					                        idBodegaOrigen, idBodegaDestino, datosDetalles.get(i), idTipoTransaccion,
					                         estadoAlta, input.getUsuario(), input.getCodArea(),idPais));
			        	            // log cuando se finaliza y asigna la reserva
			        	            listaLog.add(addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO, servicioPutAsignacion,
			        	                    input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
			        	                    "Se asignaron " + datosDetalles.get(i).getCantidad() + " art\u00EDculos reservados identificados con el ID "
			        	                            + datosDetalles.get(i).getIdArticulo() + " de la bodega " + idBodegaOrigen + " a la bodega "
			        	                            + idBodegaDestino + ", se cambi\u00F3 el estado de " + estadoReservado.toUpperCase() + " a "
			        	                            + estadoDisponible.toUpperCase() + ".",
			        	                    ""));
			        	        } else {
			        	          
			        	            // log cuando se cancela la reserva
			        	            listaLog.add(addLog(Conf.LOG_TRANSACCION_CAMBIO_ESTADO_ARTICULO, servicioPutAsignacion,
			        	                    input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
			        	                    "Se cancel\u00F3 la reservaci\u00F3n de " + datosDetalles.get(i).getCantidad()
			        	                            + " art\u00EDculos reservados identificados con el ID " + datosDetalles.get(i).getIdArticulo()
			        	                            + ", se cambi\u00F3 el estado de " + estadoReservado.toUpperCase() + " a "
			        	                            + estadoDisponible.toUpperCase() + ".",
			        	                    ""));
			        	        }
			        	   }
		                }
			           
			           }

		          
		         }
			           

                output = new OutputAsignacion();
                if (update) {
                    // todo se actualizo
                    insertaHistorico(conn, listHistorico);
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_ASIGNACION_27, null, nombreClase,
                            nombreMetodo, null, input.getCodArea());
                } else {
                    // fallo en algun punto
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_PROCESO_RESERVA_113, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Rollback: " + respuesta.getDescripcion());
                    conn.rollback();

                    listaLog = new ArrayList<LogSidra>();
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPutAsignacion, "0",
                            Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), ""));
                }

                output.setRespuesta(respuesta);

            
                UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

                conn.commit();
            }
        } catch (SQLException e) {
            respuesta = new ControladorBase().getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo,
                    null, input.getCodArea());

            output = new OutputAsignacion();
            output.setRespuesta(respuesta);

            log.error("Rollback");
            conn.rollback();

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPutAsignacion, "0",
                    Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), e.getMessage()));

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

        } catch (Exception e) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase,
                    nombreMetodo, null, input.getCodArea());

            output = new OutputAsignacion();
            output.setRespuesta(respuesta);

            log.error("Rollback");
            conn.rollback();

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPutAsignacion, "0",
                    Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), e.getMessage()));

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());

        } finally {
            conn.setAutoCommit(true);
        }

        return output;
    }

   

   

    private static List<InputArticuloAsignacion> getDatosDetalles(Connection conn, InputAsignacion input, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "getDatosDetalles";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<InputArticuloAsignacion> list = new ArrayList<InputArticuloAsignacion>();
        PreparedStatement pstmtIn = null;
        ResultSet rstIn = null;

        String campos[] = {
            AsignacionReservaDet.CAMPO_TCSCASIGNACIONRESERVADETID,
            AsignacionReservaDet.CAMPO_ARTICULO,
            "(SELECT TIPO_GRUPO_SIDRA FROM TC_SC_ARTICULO_INV I WHERE I.ARTICULO = DA.ARTICULO AND TCSCCATPAISID = " + idPais + ") AS TIPO_GRUPO_SIDRA",
            AsignacionReservaDet.CAMPO_CANTIDAD,
            AsignacionReservaDet.CAMPO_SERIE,
            AsignacionReservaDet.CAMPO_SERIE_FINAL,
            AsignacionReservaDet.CAMPO_SERIE_ASOCIADA,
            AsignacionReservaDet.CAMPO_ESTADO,
            AsignacionReservaDet.CAMPO_TIPO_INV,
        };
        
        List<Filtro> condiciones = new ArrayList<Filtro>();
        
        try{
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                    AsignacionReservaDet.CAMPO_TCSCASIGNACIONID, input.getIdAsignacionReserva()));
	
	        String sql = UtileriasBD.armarQuerySelect(AsignacionReservaDet.N_TABLA + " DA", campos, condiciones);
	
	        pstmtIn = conn.prepareStatement(sql);
	        rstIn = pstmtIn.executeQuery();
	
	        if (rstIn != null) {
	            if (!rstIn.next()) {
	                log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
	                InputArticuloAsignacion item = new InputArticuloAsignacion();
	
	                Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null,
	                        nombreClase, nombreMetodo, null, input.getCodArea());
	
	                item.setEstado(respuesta.getDescripcion());
	                list.add(item);
	            } else {
	                do {
	                    InputArticuloAsignacion item = new InputArticuloAsignacion();
	                    item.setIdAsignacionReservaDet(rstIn.getString(AsignacionReservaDet.CAMPO_TCSCASIGNACIONRESERVADETID));
	                    item.setIdArticulo(rstIn.getString(AsignacionReservaDet.CAMPO_ARTICULO));
	                    item.setCantidad(rstIn.getString(AsignacionReservaDet.CAMPO_CANTIDAD));
	                    item.setSerie(rstIn.getString(AsignacionReservaDet.CAMPO_SERIE));
	                    item.setSerieFinal(rstIn.getString(AsignacionReservaDet.CAMPO_SERIE_FINAL));
	                    item.setSerieAsociada(rstIn.getString(AsignacionReservaDet.CAMPO_SERIE_ASOCIADA));
	                    item.setTipoInv(rstIn.getString(AsignacionReservaDet.CAMPO_TIPO_INV));
	                    item.setEstado(rstIn.getString(AsignacionReservaDet.CAMPO_ESTADO));
	                    item.setTipoGrupoSidra(rstIn.getString("TIPO_GRUPO_SIDRA"));
	
	                    list.add(item);
	                } while (rstIn.next());
                }
            }
        } finally {
            DbUtils.closeQuietly(rstIn);
            DbUtils.closeQuietly(pstmtIn);
        }
        return list;
    }
    //--------------------------------- Operaciones para asignar o cancelar Reservas --------------//

    public static int verificarJornadasLiquidadas(Connection conn, String idVendedor, String idBodegaDestino, String codArea, BigDecimal idPais)
            throws SQLException {
        int existencia = 0;
        PreparedStatement pstm = null;
        ResultSet rst = null;
        
	        String sql = "SELECT COUNT(1) FROM DUAL WHERE EXISTS (SELECT 1 FROM " + getParticion(Jornada.N_TABLA, Conf.PARTITION, "",codArea)
	                + " WHERE TCSCCATPAISID =? AND VENDEDOR = ?"
	                + " AND TCSCBODEGAVIRTUAL = ?" 
	                + " AND (ESTADO_LIQUIDACION != "
	                    + "(SELECT VALOR FROM TC_SC_CONFIGURACION "
	                        + "WHERE TCSCCATPAISID = ? AND UPPER(GRUPO) = ?"
	                        + "AND UPPER(NOMBRE) = ?)"
	                + " OR ESTADO_LIQUIDACION IS NULL))";

            log.debug("Qry existencia jornada: " + sql);
       try {
            pstm = conn.prepareStatement(sql);
            pstm.setBigDecimal(1, idPais);
            pstm.setBigDecimal(2, new BigDecimal(idVendedor));
            pstm.setBigDecimal(3,new BigDecimal(idBodegaDestino));
            pstm.setBigDecimal(4, idPais);
            pstm.setString(5, Conf.GRUPO_JORNADA_ESTADOS_LIQ.toUpperCase());
            pstm.setString(6, Conf.JORNADA_ESTADO_LIQUIDADA.toUpperCase() );
            
            rst = pstm.executeQuery();
            if (rst.next()){
                existencia = rst.getInt(1);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstm);
        }
        return existencia;
    }

    public static int verificarJornadaIniciada(Connection conn, String idVendedor, String idBodegaDestino, String codArea, BigDecimal idPais)
            throws SQLException {
        int existencia = 0;
        PreparedStatement pstm = null;
        ResultSet rst = null;

       
	        String sql = "SELECT COUNT(1) FROM DUAL WHERE EXISTS (SELECT 1 FROM " + getParticion(Jornada.N_TABLA, Conf.PARTITION, "",codArea)
	                + " WHERE TCSCCATPAISID =? AND VENDEDOR = ?" 
	                + " AND TCSCBODEGAVIRTUAL = ?"
	                + " AND (UPPER(ESTADO) = "
	                    + "(SELECT VALOR FROM TC_SC_CONFIGURACION "
	                        + "WHERE TCSCCATPAISID =? AND UPPER(GRUPO) = ? "
	                        + "AND UPPER(NOMBRE) = ?)))";

            log.debug("Qry existencia jornada: " + sql);
       try {
            pstm = conn.prepareStatement(sql);
            pstm.setBigDecimal(1, idPais);
            pstm.setBigDecimal(2, new BigDecimal(idVendedor));
            pstm.setBigDecimal(3, new BigDecimal(idBodegaDestino));
            pstm.setBigDecimal(4, idPais);
            pstm.setString(5,  Conf.GRUPO_JORNADA_ESTADOS.toUpperCase());
            pstm.setString(6,  Conf.JORNADA_ESTADO_INICIADA.toUpperCase());
            rst = pstm.executeQuery();
            if (rst.next())
                existencia = rst.getInt(1);
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstm);
        }
        return existencia;
    }

    //--------------------------------- Operaciones para modificar Asignaciones/Reservas ----------//
    public static RespuestaAsignacion doModAsignacionReserva(Connection conn, InputAsignacion input,
            List<LogSidra> listaLog, BigDecimal idPais) throws Exception {
        String nombreMetodo = "doModAsignacionReserva";
        String nombreClase = new CurrentClassGetter().getClassName();
        existeError = false;
        existeErrorArticulo = false;
        existeErrorSerie = false;
        existeErrorExistencias = false;
        existeErrorHistorico = false;
        listSeriesError = new ArrayList<InputArticuloAsignacion>();
        listArticulosError = new ArrayList<InputArticuloAsignacion>();
        listExistenciasError = new ArrayList<InputArticuloAsignacion>();

        Respuesta respuesta = null;
        OutputAsignacion output = null;
        RespuestaAsignacion update = new RespuestaAsignacion();

        String idBodegaOrigen = null;
        String idBodegaDestino = null;
        String estadoAlta = null;
        String estadoActual = null;
        String estadoCancelada = null;
        String tipoActual = null;
        String tipoReserva = null;
        String estadoReservado = null;
        String estadoDisponible = null;
        BigDecimal idTipoTransaccion = null;
        String idVendedor = null;
        String sql = null;
        boolean esCancelacion = false;
        String detalleError = null;
        String tablaAsignacionConParticion = getParticion(AsignacionReserva.N_TABLA, Conf.PARTITION, "", input.getCodArea());
        String camposConfig[] = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };

        List<Map<String, String>> datosConfig = new ArrayList<Map<String, String>>();
        // Se obtienen las configuraciones.
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ASIGNACIONES_TIPO));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ASIGNACIONES_ESTADOS));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS_INVENTARIO));
        try {
            datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, camposConfig, condiciones, null);
        } catch (SQLException e) {
            log.error("Error al obtener par\u00E9metros de configuraci\u00F3n. " + e);
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_GET_CONFIG_103, null, nombreClase,
                    nombreMetodo, e.getMessage(), input.getCodArea());

            output = new OutputAsignacion();
            output.setRespuesta(respuesta);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                    input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA, respuesta.getDescripcion(), ""));

            update.setDatos(output);
            update.setListaLog(listaLog);

            return update;
        }

        try {
            conn.setAutoCommit(false);
            QueryRunner Qr = new QueryRunner();
            
            for (int i = 0; i < datosConfig.size(); i++) {
                if (Conf.AR_ESTADO_ALTA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoAlta = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.AR_ESTADO_CANCELADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoCancelada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.AR_TIPO_RESERVA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoReserva = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.INV_EST_RESERVADO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE).toUpperCase())) {
                    estadoReservado = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.INV_EST_DISPONIBLE.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE).toUpperCase())) {
                    estadoDisponible = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
            }

            condiciones = CtrlAsignacion.obtenerCondiciones(input, Conf.METODO_PUT, idPais);

            String campos[] = {
                    AsignacionReserva.CAMPO_TIPO,
                    AsignacionReserva.CAMPO_ESTADO,
                    AsignacionReserva.CAMPO_IDVENDEDOR,
                    AsignacionReserva.CAMPO_BODEGA_ORIGEN,
                    AsignacionReserva.CAMPO_BODEGA_DESTINO
            };
            Map<String, String> datos = UtileriasBD.getSingleFirstData(conn, tablaAsignacionConParticion, campos,
                    condiciones);

            if (datos.isEmpty()) {
                log.error("No existe el recurso.");
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output = new OutputAsignacion();
                output.setRespuesta(respuesta);

                listaLog = new ArrayList<LogSidra>();
                listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                        input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA, respuesta.getDescripcion(),
                        ""));

                update.setDatos(output);
                update.setListaLog(listaLog);

                return update;
            } else {
                idBodegaOrigen = datos.get(AsignacionReserva.CAMPO_BODEGA_ORIGEN);
                idBodegaDestino = datos.get(AsignacionReserva.CAMPO_BODEGA_DESTINO);
                estadoActual = datos.get(AsignacionReserva.CAMPO_ESTADO);
                tipoActual = datos.get(AsignacionReserva.CAMPO_TIPO);
                idVendedor = datos.get(AsignacionReserva.CAMPO_IDVENDEDOR);

                if (!tipoActual.equalsIgnoreCase(tipoReserva)&& verificarJornadasLiquidadas(conn, idVendedor, idBodegaDestino, input.getCodArea(), idPais) > 0) {
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_JORNADAS_NO_LIQUIDADAS_794,
                                null, nombreClase, nombreMetodo, null, input.getCodArea());

                        output = new OutputAsignacion();
                        output.setRespuesta(respuesta);

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                                input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
                                respuesta.getDescripcion(), ""));

                        update.setDatos(output);
                        update.setListaLog(listaLog);

                        return update;
                }

                if (estadoActual.equalsIgnoreCase(estadoCancelada)) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_ESTADO_ASIG_RESERVA_369, null,
                            nombreClase, nombreMetodo, estadoCancelada + ".",input.getCodArea());

                    output = new OutputAsignacion();
                    output.setRespuesta(respuesta);

                    listaLog = new ArrayList<LogSidra>();
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                            input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            respuesta.getDescripcion(), ""));

                    update.setDatos(output);
                    update.setListaLog(listaLog);

                    return update;
                }
            }

            List<InputArticuloAsignacion> datosDetalles = getDatosDetalles(conn, input, idPais);

            int res = 0;
            // se verifica si posee articulos para cambiar el estado a cancelado
            if (input.getArticulos() == null || (input.getArticulos().get(0).getSerie() == null
                    && input.getArticulos().get(0).getSerieFinal() == null
                    && input.getArticulos().get(0).getIdArticulo() == null
                    && input.getArticulos().get(0).getCantidad() == null)) {
                //TODO Pendiente opciones de modificacion de detalles con serie asociada
                // no lleva articulos
                esCancelacion = true;
                idTipoTransaccion = getIdTransaccion(conn, Conf.CODIGO_TRANSACCION_CAN_ASIG, idPais);

                // se borran los detalles y los articulos se devuelven
                log.debug("-------> Inicia proceso de eliminaci\u00F3n de detalles existentes.");
                update = doEliminarDetalle(conn, input.getIdAsignacionReserva(), datosDetalles, idBodegaOrigen,
                        idBodegaDestino, tipoActual, tipoReserva, estadoDisponible, estadoReservado, input.getUsuario(),
                        estadoAlta, idTipoTransaccion, listaLog, esCancelacion, idPais, input.getCodArea());
                log.debug("-------> Finaliza proceso de eliminación de detalles existentes.");

                if (!update.isResultado()) {
                    // fallo al eliminar un detalle
                    log.trace("Problema al eliminar un detalle.");

                    update.getListaLog().add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA,
                            servicioPutAsignacion, input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            "Fallo al eliminar un detalle de la asignaci\u00F3n o reserva.", ""));

                    detalleError = update.getDescripcion();
                } else {
                    // se cambia el estado
                    String camposAR[][] = {
                        { AsignacionReserva.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                        { AsignacionReserva.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) },
                        { AsignacionReserva.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estadoCancelada) },
                        { AsignacionReserva.CAMPO_OBSERVACIONES, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getObservaciones().trim()) }
                    };
                    
                    update.getListaLog().add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA,
                            servicioPutAsignacion, input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            "Se cambi\u00F3 el estado de la asignaci\u00F3n de " + estadoActual + " a "
                                    + estadoCancelada + ".",
                            ""));
                    
                    sql = UtileriasBD.armarQueryUpdate(tablaAsignacionConParticion, camposAR, condiciones);
                    res = Qr.update(conn, sql);
                }
                
            } else {
                // en caso que lleve algun articulo se sigue el proceso normal
                idTipoTransaccion = getIdTransaccion(conn, Conf.CODIGO_TRANSACCION_MOD_ASIG_RES, idPais);
                
                String camposAR[][] = {
                    { AsignacionReserva.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                    { AsignacionReserva.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) },
                    { AsignacionReserva.CAMPO_OBSERVACIONES, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getObservaciones()) }
                };
                
                sql = UtileriasBD.armarQueryUpdate(tablaAsignacionConParticion, camposAR, condiciones);
                res = Qr.update(conn, sql);
            }

            if (res == 1 && !esCancelacion) {
                // se actualiz\u00F3 la asignaci\u00F3n o reserva y no es cancelaci\u00F3n
                InputAsignacion inputSeriado = new InputAsignacion();
                InputAsignacion inputNoSeriado = new InputAsignacion();
                List<InputArticuloAsignacion> articulosSeriados = new ArrayList<InputArticuloAsignacion>();
                List<InputArticuloAsignacion> articulosNoSeriados = new ArrayList<InputArticuloAsignacion>();

                output = new OutputAsignacion();
                RespuestaAsignacion respTransaccion;

                /*
                 * Se verifican los articulos o series que existen en el detalle
                 * registrado contra los que se estan recibiendo en el input.
                 */
                String idArticulo = "";
                String serie = "";
                String serieFinal = "";
                String tipoInv = "";
                String cantidad = "";

                String idArticuloDetalle = "";
                String serieDetalle = "";
                String serieFinalDetalle = "";
                String tipoInvDetalle = "";
                String cantidadDetalle = "";

                List<Integer> indexBorrarDetalles = new ArrayList<Integer>();
                List<InputArticuloAsignacion> listBorrarInput = new ArrayList<InputArticuloAsignacion>();
                List<InputArticuloAsignacion> listBorrarDetalles = new ArrayList<InputArticuloAsignacion>();

                for (int i = 0; i < input.getArticulos().size(); i++) {
                    update.setResultado(false);
                    idArticulo = input.getArticulos().get(i).getIdArticulo() == null ? "" : input.getArticulos().get(i).getIdArticulo();
                    serie = input.getArticulos().get(i).getSerie() == null ? "" : input.getArticulos().get(i).getSerie();
                    serieFinal = input.getArticulos().get(i).getSerieFinal() == null ? "" : input.getArticulos().get(i).getSerieFinal();
                    tipoInv = input.getArticulos().get(i).getTipoInv() == null ? "" : input.getArticulos().get(i).getTipoInv();
                    cantidad = input.getArticulos().get(i).getCantidad() == null ? "" : input.getArticulos().get(i).getCantidad();

                    for (int j = 0; j < datosDetalles.size(); j++) {
                        if (indexBorrarDetalles.contains(j)) continue;
                        
                        idArticuloDetalle = datosDetalles.get(j).getIdArticulo() == null ? "" : datosDetalles.get(j).getIdArticulo();
                        serieDetalle = datosDetalles.get(j).getSerie() == null ? "" : datosDetalles.get(j).getSerie();
                        serieFinalDetalle = datosDetalles.get(j).getSerieFinal() == null ? "" : datosDetalles.get(j).getSerieFinal();
                        tipoInvDetalle = datosDetalles.get(j).getTipoInv() == null ? "" : datosDetalles.get(j).getTipoInv();                        
                        cantidadDetalle = datosDetalles.get(j).getCantidad() == null ? "" : datosDetalles.get(j).getCantidad();
                        
                        if (!serie.equals("") && serie.equalsIgnoreCase(serieDetalle) && tipoInv.equalsIgnoreCase(tipoInvDetalle)) {
                            if (serieFinal.equalsIgnoreCase(serieFinalDetalle) || serieFinal.equals("")) {
                                // ya existe la serie o rango, no hacer nada mas
                                // se agregan a listado de input y detalle para removerlos y no volverlos a verificar
                                listBorrarInput.add(input.getArticulos().get(i));
                                listBorrarDetalles.add(datosDetalles.get(j));
                                indexBorrarDetalles.add(j);
                                break;
                            }
                        } else if (serie.equals("")&& (idArticulo.equalsIgnoreCase(idArticuloDetalle) && tipoInv.equalsIgnoreCase(tipoInvDetalle))) {
                                if (new Integer(cantidad).compareTo(new Integer(cantidadDetalle)) == 0) {
                                    // se piden el mismo articulo y cantidad se agregan a listado de input y detalle para removerlos y no volverlos a verificar
                                    listBorrarInput.add(input.getArticulos().get(i));
                                    listBorrarDetalles.add(datosDetalles.get(j));
                                    indexBorrarDetalles.add(j);
                                    break;
                                } else {
                                    int diferencia = new Integer(cantidad) - new Integer(cantidadDetalle);
                                    if (new Integer(cantidad) > new Integer(cantidadDetalle)) {
                                        // la cantidad que se pide es mayor a la registrada, adicionar al detalle e insertar historico con la diferencia
                                        update = cambiarCantidad(tipoSuma, conn, input.getIdAsignacionReserva(),
                                                Math.abs(diferencia), datosDetalles.get(j), idBodegaOrigen,
                                                idBodegaDestino, tipoActual, tipoReserva, estadoDisponible,
                                                estadoReservado, estadoAlta, idTipoTransaccion, input.getUsuario(),
                                                listaLog, input.getCodArea(), idPais);
                                    } else if (new Integer(cantidad) < new Integer(cantidadDetalle)) {
                                        // la cantidad que se pide es menor a la registrada, restar del detalle e insertar historico con la diferencia
                                        update = cambiarCantidad(tipoResta, conn, input.getIdAsignacionReserva(),
                                                Math.abs(diferencia), datosDetalles.get(j), idBodegaOrigen,
                                                idBodegaDestino, tipoActual, tipoReserva, estadoDisponible,
                                                estadoReservado, estadoAlta, idTipoTransaccion, input.getUsuario(),
                                                listaLog, input.getCodArea(),idPais);
                                    }

                                    if (update.isResultado()) {
                                        // se agregan a listado de input y detalle para removerlos y no volverlos a verificar
                                        listBorrarInput.add(input.getArticulos().get(i));
                                        listBorrarDetalles.add(datosDetalles.get(j));
                                        indexBorrarDetalles.add(j);
                                        break;
                                    } else {
                                        respuesta = new ControladorBase().getMensaje(
                                                Conf_Mensajes.MSJ_ERROR_MOD_ASIG_RESERVA_366, null, nombreClase,
                                                nombreMetodo, "",input.getCodArea());

                                        log.error("Rollback: Fallo al actualizar cantidad de articulos no seriados.");
                                        conn.rollback();
                                        
                                        respuesta.setDescripcion(respuesta.getDescripcion().concat(" " + update.getDescripcion()));
                                        update.getListaLog().add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA,
                                                servicioPutAsignacion, input.getIdAsignacionReserva(),
                                                Conf.LOG_TIPO_ASIGNACION_RESERVA,
                                                respuesta.getDescripcion() + " Fallo al actualizar cantidad de articulos no seriados.", ""));

                                        update.getDatos().setRespuesta(respuesta);

                                        return update;
                                    }
                                }
                        }
                    }
                }

                // se quitan los elementos que ya se han verificado
                for (int i = 0; i < listBorrarDetalles.size(); i++) {
                    datosDetalles.remove(listBorrarDetalles.get(i));
                }
                for (int i = 0; i < listBorrarInput.size(); i++) {
                    input.getArticulos().remove(listBorrarInput.get(i));
                }

                /*
                 * datosDetalles lleva \u00FAnicamente los que se tienen que borrar.
                 * 
                 * input lleva \u00FAnicamente los que se tienen que insertar.
                 */

                // se agregan los datos faltantes al input para usarse en las inserciones
                input.setTipo(tipoActual);
                input.setIdVendedor(idVendedor);
                input.setIdBodegaOrigen(idBodegaOrigen);
                input.setIdBodegaDestino(idBodegaDestino);

                // input se separa entre los articulos seriados y los no seriados
                inputSeriado = (InputAsignacion) input.clone();
                inputNoSeriado = (InputAsignacion) input.clone();

                for (int i = 0; i < input.getArticulos().size(); i++) {
                    if (input.getArticulos().get(i).getSerie() != null
                            && !input.getArticulos().get(i).getSerie().equals("")) {
                        articulosSeriados.add(input.getArticulos().get(i));
                    } else {
                        articulosNoSeriados.add(input.getArticulos().get(i));
                    }
                }
                inputSeriado.setArticulos(articulosSeriados);
                inputNoSeriado.setArticulos(articulosNoSeriados);

                log.debug("-------> Inicia proceso de eliminaci\u00F3n de detalles existentes.");
                update = doEliminarDetalle(conn, input.getIdAsignacionReserva(), datosDetalles, idBodegaOrigen,
                        idBodegaDestino, tipoActual, tipoReserva, estadoDisponible, estadoReservado, input.getUsuario(),
                        estadoAlta, idTipoTransaccion, listaLog, esCancelacion, idPais, input.getCodArea());
                log.debug("-------> Finaliza proceso de eliminaci\u00F3n de detalles existentes.");

                if (update.isResultado()) {
                    // se verifica si hay algo que insertar
                    if (!input.getArticulos().isEmpty()) {
                        log.debug("-------> Inicia proceso de inserci\u00F3n de detalles nuevos.");
                        if (!tipoActual.equalsIgnoreCase(tipoReserva)) {
                            // Proceso completo de asignaciones
                            respTransaccion = doAsignacion(conn, inputSeriado, inputNoSeriado,
                                    new Integer(input.getIdAsignacionReserva()), input.getCodArea(), idPais);
                        } else {
                            // Proceso completo de reservas
                            respTransaccion = doReservacion(conn, new Integer(input.getIdAsignacionReserva()),
                                    inputSeriado, inputNoSeriado, input.getCodArea(), idPais);
                        }
                        log.debug("-------> Finaliza proceso de inserci\u00F3n de detalles nuevos.");

                        update.setResultado(respTransaccion.isResultado());
                        detalleError = respTransaccion.getDescripcion();
                        update.setDatos(respTransaccion.getDatos());
                    } else {
                        log.debug("-------> No existen detalles nuevos insertar.");
                        update.setResultado(true);
                        update.setDatos(new OutputAsignacion());
                    }
                } else {
                    // fallo al eliminar un detalle
                    log.trace("Problema al eliminar un detalle.");

                    update.getListaLog().add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA,
                            servicioPutAsignacion, input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            "Fallo al eliminar un detalle de la asignaci\u00F3n o reserva.", ""));

                    detalleError = update.getDescripcion();
                }
            } else if (res == 1) {
                // la actualizaci\u00F3n modific\u00F3 correctamente el registro y es cancelaci\u00F3n
                log.trace("Se actualiza estado de la asignaci\u00F3n.");
                if (update.getDatos() == null) {
                    update.setDatos(new OutputAsignacion());
                }
            } else {
                // la actualizaci\u00F3n modific\u00F3 mas de un registro
                if (update.getDatos() == null) {
                    update.setDatos(new OutputAsignacion());
                }
                update.setResultado(false);
            }

            if (update.isResultado()) {
                // todo se actualizo
                log.debug("Todo se actualiz\u00F3 correctamente.");
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_ASIGNACION_27, null, nombreClase,
                        nombreMetodo, detalleError,input.getCodArea());

                conn.commit();
            } else {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_MOD_ASIG_RESERVA_366, null,
                        nombreClase, nombreMetodo, detalleError,input.getCodArea());

                if (!esCancelacion) {
                    // fallo en algun punto de la inserci\u00F3n de detalles
                    log.error("Rollback - Fallo al realizar procesos de inserci\u00F3n de detalles.");

                    update.setListaLog(new ArrayList<LogSidra>());
                    update.getListaLog().add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA,
                            servicioPutAsignacion, input.getIdAsignacionReserva(), Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            "Fallo al realizar procesos de inserci\u00F3n de detalles.", detalleError));
                }

                conn.rollback();
            }

            update.getDatos().setRespuesta(respuesta);

        } finally {
            conn.setAutoCommit(true);
          
        }

        return update;
    }
    
    private static RespuestaAsignacion doEliminarDetalle(Connection conn, String idAsignacionReserva,
            List<InputArticuloAsignacion> datosDetalles, String idBodegaOrigen, String idBodegaDestino,
            String tipoActual, String tipoReserva, String estadoDisponible, String estadoReservado, String usuario,
            String estadoAlta, BigDecimal idTipoTransaccion, List<LogSidra> listaLog, boolean esCancelacion, BigDecimal idPais, String codArea)
                    throws Exception {
        Statement stmtUpdates = conn.createStatement();
        Statement stmtUpdateRangos = conn.createStatement();
        List<Integer> cantRangoUpdate = new ArrayList<Integer>();
        RespuestaAsignacion respuesta = new RespuestaAsignacion();
        Statement stmtInserts = conn.createStatement();
        Statement stmtDeletes = conn.createStatement();
        conn.setAutoCommit(false);
        List<Filtro> condiciones = new ArrayList<Filtro>();
        String idBodegaActual = "";
        String estadoActual = "";
        String existencia = "";
        String sql = "";
        List<InputArticuloHistorico> lstHistorico = new ArrayList<InputArticuloHistorico>();
        InputArticuloHistorico itemHistorico = new InputArticuloHistorico();
        int[] updateCounts = null;
        int[] insertCounts = null;
        int[] updateRangoCounts = null;
        int[] deleteCounts = null;

        try {
            if (tipoActual.equalsIgnoreCase(tipoReserva)) {
                // procesos para quitar o agregar detalles de reserva
                idBodegaActual = idBodegaOrigen;
                estadoActual = estadoReservado;
            } else {
                // procesos para quitar o agregar detalles de asignacion
                idBodegaActual = idBodegaDestino;
                estadoActual = estadoDisponible;
            }

            for (int i = 0; i < datosDetalles.size(); i++) {


                // se borra el detalle
                condiciones.clear();
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AsignacionReservaDet.CAMPO_TCSCASIGNACIONRESERVADETID, datosDetalles.get(i).getIdAsignacionReservaDet()));
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AsignacionReservaDet.CAMPO_TCSCASIGNACIONID, idAsignacionReserva));
	            sql = UtileriasBD.armarQueryDelete(AsignacionReservaDet.N_TABLA, condiciones);
	            stmtDeletes.addBatch(sql);
	
	            condiciones.clear();
	            // se verifica que el articulo exista en la bodega del encabezado
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, idBodegaActual));
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO, datosDetalles.get(i).getIdArticulo()));
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_TIPO_INV, datosDetalles.get(i).getTipoInv()));
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_ESTADO, estadoActual));
	            
	            if (datosDetalles.get(i).getSerie() != null && !datosDetalles.get(i).getSerie().equals("")) {
	                // detalle seriado
	                if (datosDetalles.get(i).getSerieFinal() != null && !datosDetalles.get(i).getSerieFinal().equals("")) {
	                    // Rango de series
	                    condiciones.add(new Filtro(Inventario.CAMPO_SERIE, Filtro.GTE, datosDetalles.get(i).getSerie()));
	                    condiciones.add(new Filtro(Inventario.CAMPO_SERIE, Filtro.LTE, datosDetalles.get(i).getSerieFinal()));

	                   
	                } else {
	                    // Serie unica
	                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_SERIE, datosDetalles.get(i).getSerie()));

	                }

	                existencia = UtileriasBD.verificarExistencia(conn, getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaActual, codArea), condiciones);
	                if (existencia.equalsIgnoreCase(datosDetalles.get(i).getCantidad())) {
	                    // existen todas las series
	                    // se reasigna a la bodega original
	                    String campos[][] = {
	                        { Inventario.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoDisponible) },
	                        { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
	                        { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) },
	                        { Inventario.CAMPO_TCSCASIGNACIONRESERVAID, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
	                        { Inventario.CAMPO_IDVENDEDOR, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
	                        { Inventario.CAMPO_TCSCBODEGAVIRTUALID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, idBodegaOrigen) }
	                    };

	                    sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaActual, codArea), campos, condiciones);
                        if (new Integer(existencia) > 1) {
                            cantRangoUpdate.add(new Integer(existencia));
                            stmtUpdateRangos.addBatch(sql);
                        } else {
                            stmtUpdates.addBatch(sql);
                        }
                      

                    } else {
	                    // al menos una serie ya no esta en esa bodega
                        log.trace("No todas las series existen en esa bodega.");

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                                idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                                (datosDetalles.get(i).getSerieFinal() == null
                                        ? "La serie " + datosDetalles.get(i).getSerie()
                                        : "Alguna de las series del rango " + datosDetalles.get(i).getSerie() + "-"
                                                + datosDetalles.get(i).getSerieFinal())
                                        + " no se encuentra en la bodega " + idBodegaActual + " en estado "
                                        + estadoActual.toUpperCase() + ".",
                                ""));

                        existeErrorSerie = true;
                        listSeriesError.add(datosDetalles.get(i));

                        return validarSalida("", listaLog, codArea);
                    }

                    listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                            idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            "El art\u00EDculo con ID " + datosDetalles.get(i).getIdArticulo()
                                    + (datosDetalles.get(i).getSerieFinal() != null
                                            ? " y rango de series " + datosDetalles.get(i).getSerie() + "-"
                                                    + datosDetalles.get(i).getSerieFinal()
                                            : " y serie " + datosDetalles.get(i).getSerie())
                                    + (tipoActual.equalsIgnoreCase(tipoReserva)
                                            ? " se traslad\u00F3 dentro de la bodega " + idBodegaActual
                                            : " se traslad\u00F3 de la bodega " + idBodegaActual + " para la bodega "
                                                    + idBodegaOrigen)
                                    + (esCancelacion ? " por cancelaci\u00F3n de " : " por cambio en reserva o ")
                                    + "asignaci\u00F3n"
                                    + (!estadoActual.equalsIgnoreCase(estadoDisponible) ? ", se cambi\u00F3 el estado de "
                                            + estadoActual.toUpperCase() + " a " + estadoDisponible.toUpperCase() + "."
                                            : "."),
                            ""));

                } else {
	                // detalle no seriado
	                boolean borrarArticuloBodActual = false;
	                if (tipoActual.equalsIgnoreCase(tipoReserva)) {
	                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCASIGNACIONRESERVAID, idAsignacionReserva));
	                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_CANTIDAD, datosDetalles.get(i).getCantidad()));
	                    borrarArticuloBodActual = true;
	                } else {
	                    condiciones.add(new Filtro(Inventario.CAMPO_CANTIDAD, Filtro.GTE, datosDetalles.get(i).getCantidad()));
	                }

                    // verifico que exista en la bodega actual
                    existencia = UtileriasBD.verificarExistencia(conn, getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaActual, codArea), condiciones);
	                if (!existencia.equals("") && new Integer(existencia) > 0) {
                        String cantidadBodActual = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_CANTIDAD,
                                getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaActual,codArea), condiciones);

	                    if ((new Integer(cantidadBodActual).compareTo(new Integer(datosDetalles.get(i).getCantidad())) == 0)
	                            || borrarArticuloBodActual == true) {
	                        // borrar de la bodega actual
	                        sql = UtileriasBD.armarQueryDelete(getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaActual,codArea), condiciones);
	                        stmtDeletes.addBatch(sql);

                        } else if (new Integer(cantidadBodActual) > new Integer(datosDetalles.get(i).getCantidad())) {
	                        // restar de la bodega actual
	                        String campos[][] = {
	                            { Inventario.CAMPO_CANTIDAD, UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
	                                    "(" + Inventario.CAMPO_CANTIDAD + " - " + datosDetalles.get(i).getCantidad() + ")") },
	                            { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
	                            { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) }
	                        };
	
	                        sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaActual,codArea), campos, condiciones);
	                        stmtUpdates.addBatch(sql);
	
	                    } else {
	                        log.trace("La bodega actual no cuenta con existencias para deshacer la asignaci\u00F3n o reserva.");
	                        
	                        listaLog = new ArrayList<LogSidra>();
	                        listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA,
	                                servicioPutAsignacion, idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
	                                "La bodega " + idBodegaActual + " no cuenta con existencias del art\u00EDculo "
	                                        + datosDetalles.get(i).getIdArticulo() + " en estado " + estadoActual.toUpperCase() + ".",
	                                ""));

                            existeErrorExistencias = true;
                            listExistenciasError.add(datosDetalles.get(i));

                            return validarSalida("", listaLog, codArea);
                        }

                        // verificar que exista en la bodega origen
	                    List<Filtro> condicionesOrigen = new ArrayList<Filtro>();
	                    condicionesOrigen.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, idBodegaOrigen));
	                    condicionesOrigen.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
	                    condicionesOrigen.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO, datosDetalles.get(i).getIdArticulo()));
	                    condicionesOrigen.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_TIPO_INV, datosDetalles.get(i).getTipoInv()));
	                    condicionesOrigen.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoDisponible));

                        existencia = UtileriasBD.verificarExistencia(conn, getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaOrigen,codArea), condicionesOrigen);
	                    if (new Integer(existencia) > 0) {
	                        // sumar a la bodega origen
	                        String campos[][] = {
	                                { Inventario.CAMPO_CANTIDAD, UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
	                                        "(" + Inventario.CAMPO_CANTIDAD + " + " + datosDetalles.get(i).getCantidad() + ")") },
	                                { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
	                                { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) },
	                                { Inventario.CAMPO_TCSCASIGNACIONRESERVAID, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
	                                { Inventario.CAMPO_IDVENDEDOR, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
	                        };

	                        sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaOrigen,codArea), campos, condicionesOrigen);
	                        stmtUpdates.addBatch(sql);
	                    } else {
	                        // insert en bodega origen con la cantidad del detalle
	                        sql = OperacionMovimientosInventario.getInsertInventario(idBodegaOrigen, new Integer(datosDetalles.get(i).getCantidad()),
	                                estadoDisponible, condiciones, usuario, null, null, idPais);

	                        stmtInserts.addBatch(sql);
                        }

                        listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                                idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                                datosDetalles.get(i).getCantidad() + " art\u00EDculo(s) identificados con el ID "
                                        + datosDetalles.get(i).getIdArticulo()
                                        + (tipoActual.equalsIgnoreCase(tipoReserva)
                                                ? " se trasladaron dentro de la bodega " + idBodegaActual
                                                : " se trasladaron de la bodega " + idBodegaActual + " para la bodega "
                                                        + idBodegaOrigen)
                                        + (esCancelacion ? " por cancelaci\u00F3n de " : " por cambio en reserva o ")
                                        + "asignaci\u00F3n"
                                        + (!estadoActual.equalsIgnoreCase(estadoDisponible)
                                                ? ", se cambi\u00F3 el estado de " + estadoActual.toUpperCase() + " a "
                                                        + estadoDisponible.toUpperCase() + "."
                                                : "."),
                                ""));

                    } else {
                        log.trace("El art\u00EDculo no se encuentra en la bodega.");

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                                idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                                "No existe la cantidad de " + datosDetalles.get(i).getCantidad()
                                        + " art\u00EDculo(s) con ID " + datosDetalles.get(i).getIdArticulo() + " en estado "
                                        + estadoActual.toUpperCase() + " en la bodega " + idBodegaActual + ".",
                                ""));

                        existeErrorArticulo = true;
                        listArticulosError.add(datosDetalles.get(i));

                        return validarSalida("", listaLog, codArea);
                    }
                }

                if (!tipoActual.equalsIgnoreCase(tipoReserva)) {
                    itemHistorico = new InputArticuloHistorico();
                    itemHistorico.setIdArticulo(datosDetalles.get(i).getIdArticulo());
                    itemHistorico.setSerie(datosDetalles.get(i).getSerie());
                    itemHistorico.setSerieFinal(datosDetalles.get(i).getSerieFinal());
                    itemHistorico.setSerieAsociada(datosDetalles.get(i).getSerieAsociada());
                    itemHistorico.setTipoInv(datosDetalles.get(i).getTipoInv());
                    itemHistorico.setCantidad(datosDetalles.get(i).getCantidad());

                    lstHistorico.add(itemHistorico);
                }
            }

            if (!tipoActual.equalsIgnoreCase(tipoReserva)&& !OperacionMovimientosInventario.insertaHistorico(conn, idBodegaDestino, idBodegaOrigen,
                        lstHistorico, idTipoTransaccion, estadoAlta, usuario, codArea,idPais)) {
                    log.trace("fallo al insertar historico");

                    listaLog = new ArrayList<LogSidra>();
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                            idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            "Problemas al insertar el hist\u00F3rico al eliminar detalles de asignaci\u00F3n o reserva.", ""));

                    existeErrorHistorico = true;

                    return validarSalida("", listaLog,codArea);
            }

            updateCounts = stmtUpdates.executeBatch();
            insertCounts = stmtInserts.executeBatch();
            updateRangoCounts = stmtUpdateRangos.executeBatch();
            deleteCounts = stmtDeletes.executeBatch();

            respuesta.setListaLog(listaLog);
            respuesta.setResultado(UtileriasJava.validarBatch(1, updateCounts)
                    && UtileriasJava.validarBatch(1, insertCounts) && UtileriasJava.validarBatch(1, deleteCounts)
                    && UtileriasJava.validarBatchRangos(cantRangoUpdate, updateRangoCounts));
        } finally {
            DbUtils.closeQuietly(stmtUpdates);
            DbUtils.closeQuietly(stmtInserts);
            DbUtils.closeQuietly(stmtUpdateRangos);
            DbUtils.closeQuietly(stmtDeletes);
        }
        return (respuesta);
    }

    /**
     * Funci\u00F3n que suma o resta la cantidad de articulos necesarios de las
     * respectivas bodegas.
     * 
     * @param tipo
     * @param conn
     * @param idAsignacionReserva
     * @param diferencia
     * @param detalle
     * @param idBodegaOrigen
     * @param idBodegaDestino
     * @param tipoActual
     * @param tipoReserva
     * @param estadoDisponible
     * @param estadoReservado
     * @param estadoAlta
     * @param idTipoTransaccion
     * @param usuario
     * @param listaLog 
     * @return
     * @throws SQLException
     */
    private static RespuestaAsignacion cambiarCantidad(int tipo, Connection conn, String idAsignacionReserva,
            int diferencia, InputArticuloAsignacion detalle, String idBodegaOrigen, String idBodegaDestino,
            String tipoActual, String tipoReserva, String estadoDisponible, String estadoReservado, String estadoAlta,
            BigDecimal idTipoTransaccion, String usuario, List<LogSidra> listaLog, String codArea, BigDecimal idPais) throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        List<Filtro> condicionesDestino = new ArrayList<Filtro>();

        String sql = "";
        List<InputArticuloHistorico> lstHistorico = new ArrayList<InputArticuloHistorico>();
        InputArticuloHistorico itemHistorico = new InputArticuloHistorico();

        String idBodegaActual = "";
        String estadoActual = "";
        String cantidadBodOrigen = "";
        String cantidadBodDestino = "";
        String signoOperacion = "";
        String tablaConParticionOrigen = "";
        String tablaConParticionDestino = "";
        RespuestaAsignacion respuesta = new RespuestaAsignacion();
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            conn.setAutoCommit(false);

            if (tipoActual.equalsIgnoreCase(tipoReserva)) {
                idBodegaActual = idBodegaOrigen;
                estadoActual = estadoReservado;

                // log
                listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                        idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                        "Se trasladaron " + diferencia + " art\u00EDculo(s) con ID " + detalle.getIdArticulo()
                                + " dentro de la bodega " + idBodegaOrigen + " por cambio en reserva.",
                        ""));
            } else {
                idBodegaActual = idBodegaDestino;
                estadoActual = estadoDisponible;
            }

            if (tipo == tipoSuma) {
                // condiciones origen
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, idBodegaOrigen));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO, detalle.getIdArticulo()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_TIPO_INV, detalle.getTipoInv()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoDisponible));

                // condiciones destino
                condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, idBodegaActual));
                condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
                condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO, detalle.getIdArticulo()));
                condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_TIPO_INV, detalle.getTipoInv()));
                condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoActual));
            
                if (tipoActual.equalsIgnoreCase(tipoReserva)) {
                    condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCASIGNACIONRESERVAID, idAsignacionReserva));

                } else {
                    // log
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                            idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            "Se trasladaron " + diferencia + " art\u00EDculo(s) con ID " + detalle.getIdArticulo()
                                    + " de la bodega " + idBodegaOrigen + " para la bodega " + idBodegaDestino
                                    + " por cambio en asignaci\u00F3n.",
                            ""));
                }

                signoOperacion = " + ";
                tablaConParticionOrigen = getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaOrigen,codArea);
                tablaConParticionDestino = getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaActual,codArea);
            } else if (tipo == tipoResta) {
                // condiciones origen
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, idBodegaActual));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO, detalle.getIdArticulo()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_TIPO_INV, detalle.getTipoInv()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoActual));
                if (tipoActual.equalsIgnoreCase(tipoReserva)) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCASIGNACIONRESERVAID, idAsignacionReserva));

                } else {
                    // log
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                            idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            "Se trasladaron " + diferencia + " art\u00EDculo(s) con ID " + detalle.getIdArticulo()
                                    + " de la bodega " + idBodegaDestino + " para la bodega " + idBodegaOrigen
                                    + " por cambio en asignaci\u00F3n.",
                            ""));
                }

                // condiciones destino
                condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, idBodegaOrigen));
                condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
                condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO, detalle.getIdArticulo()));
                condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_TIPO_INV, detalle.getTipoInv()));
                condicionesDestino.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoDisponible));
            
                signoOperacion = " - ";
                tablaConParticionOrigen = getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaActual,codArea);
                tablaConParticionDestino = getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaOrigen,codArea);
            }
        
            // verificando origen
            String existencia = UtileriasBD.verificarExistencia(conn, tablaConParticionOrigen, condiciones);
            if (new Integer(existencia) > 0) {
                cantidadBodOrigen = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_CANTIDAD, tablaConParticionOrigen, condiciones);

                if (tipoActual.equalsIgnoreCase(tipoReserva) && tipo == tipoResta
                        && !cantidadBodOrigen.equalsIgnoreCase(detalle.getCantidad())) {
                    log.trace("Las cantidades de reserva no coinciden: cantidad reservada en bodega " + cantidadBodOrigen
                            + " != cantidad del detalle " + detalle.getCantidad() + ".");

                    listaLog = new ArrayList<LogSidra>();
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                            idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            "Problema al verificar cantidad del art\u00EDculo reservado ID " + detalle.getIdArticulo()
                                    + " en el detalle " + detalle.getIdAsignacionReservaDet() + ".",
                            "Las cantidades de reserva no coinciden: cantidad reservada en bodega " + cantidadBodOrigen
                                    + " != cantidad del detalle " + detalle.getCantidad() + "."));

                    existeError = true;
                    existeErrorArticulo = true;
                    listArticulosError.add(detalle);

                    return validarSalida("Las cantidades de reserva no coinciden: cantidad reservada en bodega "
                            + cantidadBodOrigen + " != cantidad del detalle " + detalle.getCantidad() + ".", listaLog, codArea);
                }
            } else {
                cantidadBodOrigen = null;
            }
        
            // verificando destino
            existencia = UtileriasBD.verificarExistencia(conn, tablaConParticionDestino, condicionesDestino);
            if (new Integer(existencia) > 0) {
                cantidadBodDestino = UtileriasBD.getOneRecord(conn, Inventario.CAMPO_CANTIDAD, tablaConParticionDestino, condicionesDestino);

                if (tipoActual.equalsIgnoreCase(tipoReserva) && tipo == tipoSuma
                        && !cantidadBodDestino.equalsIgnoreCase(detalle.getCantidad())) {
                    log.trace("Las cantidades de reserva no coinciden: cantidad reservada en bodega "
                            + cantidadBodDestino + " != cantidad del detalle " + detalle.getCantidad() + ".");

                    listaLog = new ArrayList<LogSidra>();
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                            idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            "Problema al verificar cantidad del art\u00EDculo reservado ID " + detalle.getIdArticulo()
                                    + " en el detalle " + detalle.getIdAsignacionReservaDet() + ".",
                            "Las cantidades de reserva no coinciden: cantidad reservada en bodega " + cantidadBodDestino
                                    + " != cantidad del detalle " + detalle.getCantidad() + "."));

                    existeError = true;
                    existeErrorArticulo = true;
                    listArticulosError.add(detalle);

                    return validarSalida("Las cantidades de reserva no coinciden: cantidad reservada en bodega "
                            + cantidadBodDestino + " != cantidad del detalle " + detalle.getCantidad() + ".", listaLog, codArea);
                }

                if (cantidadBodOrigen == null || cantidadBodOrigen.equals("")) {
                    // no hay inventario en bodega origen
                    log.trace("La bodega origen no tiene el articulo disponible para sumarlo al detalle.");

                    listaLog = new ArrayList<LogSidra>();
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                            idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            "No existe el art\u00EDculo ID " + detalle.getIdArticulo() + " en la bodega origen.", ""));

                    existeErrorArticulo = true;
                    listArticulosError.add(detalle);

                    return validarSalida("", listaLog,codArea);

                } else {
                    if (new Integer(cantidadBodOrigen) > diferencia) {
                        // sumar en la bodega destino
                        String camposUpd[][] = {
                                { Inventario.CAMPO_CANTIDAD, UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
                                            "(" + Inventario.CAMPO_CANTIDAD + " + " + diferencia + ")") },
                                { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                                { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) } };
                        sql = UtileriasBD.armarQueryUpdate(tablaConParticionDestino, camposUpd, condicionesDestino);
                        stmt.addBatch(sql);

                        // restar de bodega origen
                        String campos[][] = {
                                { Inventario.CAMPO_CANTIDAD, UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
                                            "(" + Inventario.CAMPO_CANTIDAD + " - " + diferencia + ")") },
                                { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                                { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) } };
                        sql = UtileriasBD.armarQueryUpdate(tablaConParticionOrigen, campos, condiciones);
                        stmt.addBatch(sql);

                    } else if (new Integer(cantidadBodOrigen) == diferencia) {
                        // sumar a la bodega destino
                        String camposUpd[][] = {
                                { Inventario.CAMPO_CANTIDAD, UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
                                            "(" + Inventario.CAMPO_CANTIDAD + " + " + diferencia + ")") },
                                { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                                { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) } };
                        sql = UtileriasBD.armarQueryUpdate(tablaConParticionDestino, camposUpd, condicionesDestino);
                        stmt.addBatch(sql);
    
                        // eliminar el registro de la bodega origen
                        condiciones.get(0).setValue(idBodegaOrigen);
                        sql = UtileriasBD.armarQueryDelete(tablaConParticionOrigen, condiciones);
                        stmt.addBatch(sql);

                    } else if (new Integer(cantidadBodOrigen) < diferencia) {
                        // no hay stock
                        log.trace(
                                "Las existencias no son suficientes para realizar el cambio en la asignaci\u00F3n o reserva.");

                        listaLog = new ArrayList<LogSidra>();
                        listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                                idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                                "El art\u00EDculo con ID " + detalle.getIdArticulo()
                                        + " no cuenta con existencias en la bodega origen para el cambio en reserva o asignaci\u00F3n.",
                                ""));

                        existeErrorExistencias = true;
                        listExistenciasError.add(detalle);

                        return validarSalida("", listaLog,codArea);
                    }
                }
            } else {
                if (tipoActual.equalsIgnoreCase(tipoReserva) && tipo == tipoSuma) {
                    // no existe inventario reservado en bodega destino
                    log.trace("La bodega destino no tiene el articulo reservado para sumarlo al detalle.");

                    listaLog = new ArrayList<LogSidra>();
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                            idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            "El art\u00EDculo con ID " + detalle.getIdArticulo()
                                    + " no existe en la bodega destino para el cambio en reserva o asignaci\u00F3n.",
                            ""));

                    existeErrorArticulo = true;
                    listArticulosError.add(detalle);

                    return validarSalida("", listaLog,codArea);
                }

                // restar de bodega origen
                String campos[][] = {
                    { Inventario.CAMPO_CANTIDAD, UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
                            "(" + Inventario.CAMPO_CANTIDAD + " - " + diferencia + ")") },
                    { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                    { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) }
                };

                sql = UtileriasBD.armarQueryUpdate(tablaConParticionOrigen, campos, condiciones);
                stmt.addBatch(sql);

                // se inserta en bodega destino
                if (tipo == tipoSuma) {
                    sql = OperacionMovimientosInventario.getInsertInventario(idBodegaDestino, diferencia, estadoDisponible, condiciones, usuario, null, null,idPais);
                } else {
                    sql = OperacionMovimientosInventario.getInsertInventario(idBodegaOrigen, diferencia, estadoDisponible, condiciones, usuario, null, null,idPais);
                }
                stmt.addBatch(sql);
            }

            // se suma o resta la diferencia en el detalle
            condiciones.clear();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AsignacionReservaDet.CAMPO_TCSCASIGNACIONID, idAsignacionReserva));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AsignacionReservaDet.CAMPO_TCSCASIGNACIONRESERVADETID, detalle.getIdAsignacionReservaDet()));

            String campos[][] = {
                { AsignacionReservaDet.CAMPO_CANTIDAD, UtileriasJava.setUpdate(Conf.INSERT_NUMERO,
                        "(" + AsignacionReservaDet.CAMPO_CANTIDAD + signoOperacion + diferencia + ")") },
                { AsignacionReservaDet.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                { AsignacionReservaDet.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) }
            };

            sql = UtileriasBD.armarQueryUpdate(AsignacionReservaDet.N_TABLA, campos, condiciones);
            stmt.addBatch(sql);

            // se verifica si es tipo Asignacion para insertar el hist\u00F3rico
            if (!tipoActual.equalsIgnoreCase(tipoReserva)) {
                itemHistorico = new InputArticuloHistorico();
                itemHistorico.setIdArticulo(detalle.getIdArticulo());
                itemHistorico.setSerie(detalle.getSerie());
                itemHistorico.setSerieFinal(detalle.getSerieFinal());
                itemHistorico.setSerieAsociada(detalle.getSerieAsociada());
                itemHistorico.setTipoInv(detalle.getTipoInv());
                itemHistorico.setCantidad(diferencia + "");

                lstHistorico.add(itemHistorico);

                if (tipo == tipoResta) {
                    idBodegaActual = idBodegaOrigen;
                    idBodegaOrigen = idBodegaDestino;
                    idBodegaDestino = idBodegaActual;
                }

                if (!OperacionMovimientosInventario.insertaHistorico(conn, idBodegaOrigen, idBodegaDestino,
                        lstHistorico, idTipoTransaccion, estadoAlta, usuario, codArea,idPais)) {
                    log.trace("fallo al insertar historico");

                    listaLog = new ArrayList<LogSidra>();
                    listaLog.add(addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, servicioPutAsignacion,
                            idAsignacionReserva, Conf.LOG_TIPO_ASIGNACION_RESERVA,
                            "Problema al insertar el hist\u00F3rico tras actualizar cantidad de detalle de asignaci\u00F3n o reserva.",
                            ""));

                    existeErrorHistorico = true;

                    return validarSalida("", listaLog, codArea);
                }
            }

            int[] updateCounts = stmt.executeBatch();
            respuesta.setListaLog(listaLog);
            respuesta.setResultado(UtileriasJava.validarBatch(1, updateCounts));
            respuesta.setDatos(new OutputAsignacion());
        } finally {
            DbUtils.closeQuietly(stmt);
        }

        return (respuesta);
    }

    private static RespuestaAsignacion validarSalida(String msjError, List<LogSidra> listaLog, String codArea) {
        String nombreMetodo = "validarSalida";
        String nombreClase = new CurrentClassGetter().getClassName();

        RespuestaAsignacion respuestaValidacion = new RespuestaAsignacion();
        OutputAsignacion output = new OutputAsignacion();
        Respuesta respuesta;
        respuestaValidacion.setResultado(true);
        respuestaValidacion.setDescripcion("");
        respuestaValidacion.setListaLog(listaLog);

        if (existeError) {
            respuestaValidacion.setResultado(false);
            respuestaValidacion.setDescripcion(msjError + " ");
        }

        if (existeErrorSerie) {
            respuestaValidacion.setResultado(false);
            output.setDescErrorSeries("Series que no existen en bodega.");
            output.setSeries(listSeriesError);
        }

        if (existeErrorArticulo) {
            respuestaValidacion.setResultado(false);
            output.setDescErrorArticulos("Art\u00EDculos que no existen en bodega o se encuentran con errores.");
            output.setArticulos(listArticulosError);
        }

        if (existeErrorExistencias) {
            respuestaValidacion.setResultado(false);
            output.setDescErrorExistencias("Art\u00EDculos que no poseen existencias suficientes en bodega.");
            output.setExistencias(listExistenciasError);
        }

        if (existeErrorHistorico) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_INSERT_HISTORICO_126, null,
                    nombreClase, nombreMetodo, null, codArea);
            respuestaValidacion.setResultado(false);
            respuestaValidacion.setDescripcion(respuesta.getDescripcion());
        }

        if (!respuestaValidacion.isResultado()) {

            respuestaValidacion.setDescripcion(respuestaValidacion.getDescripcion());
            respuestaValidacion.setDatos(output);
        }

        return respuestaValidacion;
    }
    // --------------------------------- Operaciones para modificar Asignaciones/Reservas ----------//
    
    // --------------------------------- Operaciones de CR para activacion de rascas ---------------//
public static String getUpdateActivacion ( int idAsignacion,
        InputArticuloAsignacion articuloAsignacion, String usuario, BigDecimal idPais) {
	String   update = "INSERT INTO TC_SC_TUX_ACTIVACION_LOG "
            + "(TCSCTUXACTIVACIONLOGID, TCSCCATPAISID, TCSCASIGNACIONRESERVAID, SERIE, TIPO_GRUPO_SIDRA, CREADO_EL, CREADO_POR) "
            + "(SELECT TC_SC_TUX_ACTIVACION_LOG_SQ.nextval, TCSCCATPAISID, " + idAsignacion + ", "
                + "SERIE, TIPO_GRUPO_SIDRA, SYSDATE, '" + usuario + "'"

            + " FROM TC_SC_INVENTARIO WHERE TCSCCATPAISID = " + idPais
            + " AND SERIE BETWEEN '" + articuloAsignacion.getSerie() + "' AND '" + articuloAsignacion.getSerieFinal() + "'"
            + " AND UPPER(TIPO_GRUPO_SIDRA) = '" + articuloAsignacion.getTipoGrupoSidra().toUpperCase() + "'"
            + " AND SERIE NOT IN ("
                + "SELECT SERIE FROM TC_SC_TUX_ACTIVACION_LOG WHERE TCSCCATPAISID = " + idPais
                + " AND SERIE BETWEEN '" + articuloAsignacion.getSerie() + "' AND '" + articuloAsignacion.getSerieFinal() + "'"
                + " AND UPPER(TIPO_GRUPO_SIDRA) = '" + articuloAsignacion.getTipoGrupoSidra().toUpperCase() + "'))";
	return update;
}

public static String getUpdateActivacion2(int idAsignacion,
        InputArticuloAsignacion articuloAsignacion, String usuario, boolean tieneNumero, BigDecimal idPais) {
	String update = "INSERT INTO TC_SC_TUX_ACTIVACION_LOG "
            + "(TCSCTUXACTIVACIONLOGID, TCSCCATPAISID, TCSCASIGNACIONRESERVAID, SERIE, NUM_TELEFONO, TIPO_GRUPO_SIDRA, CREADO_EL, CREADO_POR) "
            + "(SELECT TC_SC_TUX_ACTIVACION_LOG_SQ.nextval, " + idPais + ", " + idAsignacion + ", SERIE, "
            + (tieneNumero ? articuloAsignacion.getNumTelefono() : "NULL") + ","
            + "TIPO_GRUPO_SIDRA, SYSDATE, '" + usuario + "'"

            + " FROM TC_SC_INVENTARIO WHERE TCSCCATPAISID = " + idPais
            + " AND SERIE = '" + articuloAsignacion.getSerie() + "'"
            + " AND UPPER(TIPO_GRUPO_SIDRA) = '" + articuloAsignacion.getTipoGrupoSidra().toUpperCase() + "'"
            + " AND SERIE NOT IN ("
                + "SELECT SERIE FROM TC_SC_TUX_ACTIVACION_LOG WHERE TCSCCATPAISID = " + idPais
                + " AND SERIE = '" + articuloAsignacion.getSerie() + "'"
                + (tieneNumero ? " AND NUM_TELEFONO = " + articuloAsignacion.getNumTelefono() : "")
                + " AND UPPER(TIPO_GRUPO_SIDRA) = '" + articuloAsignacion.getTipoGrupoSidra().toUpperCase() + "'))";

	return update;
}

    private static void registroSeriesActivacion(Connection conn, int idAsignacion,
            InputArticuloAsignacion articuloAsignacion, String usuario, BigDecimal idPais) throws Exception {
        Statement stmt = null;
        String update="";
       
            if (articuloAsignacion.getSerieFinal() != null && !articuloAsignacion.getSerieFinal().equals("")) {
                update =  getUpdateActivacion ( idAsignacion,
                         articuloAsignacion,  usuario, idPais) ;

                log.debug("insert rango activacion: " + update);
               

            } else {
                boolean tieneNumero = articuloAsignacion.getNumTelefono() != null && !articuloAsignacion.getNumTelefono().equals("");

                update =getUpdateActivacion2( idAsignacion,
                         articuloAsignacion,  usuario,  tieneNumero,idPais);

                log.debug("insert serie activacion: " + update);
               
            }
        try {
        	stmt = conn.createStatement();
        	stmt.addBatch(update);
            stmt.executeBatch();
        } finally {
            DbUtils.closeQuietly(stmt);
        }
    }
    
    /**
     * M\u00E9todo para actualizar inventario que se convertira de reserva a asignacion
     * */
    
    
    private static boolean updateInvAsignacion(Connection conn, String tipoOperacion, 
            String idBodegaOrigen, String idBodegaDestino, InputAsignacion input, 
            String estadoDisponible, String estadoReservado,  String idVendedor, String codArea, BigDecimal idPais)
                    throws SQLException {
        String sql = "";
        QueryRunner Qr = new QueryRunner();
        boolean update = false;
        List<LogSidra> listaLog ;
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, idBodegaOrigen));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCASIGNACIONRESERVAID, input.getIdAsignacionReserva()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_IDVENDEDOR, idVendedor));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoReservado.toUpperCase()));
  

        // Update
        if (tipoOperacion.equalsIgnoreCase("FIN")) {
            String camposUpdInventario[][] = {
                    { Inventario.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoDisponible) },
                    { Inventario.CAMPO_TCSCBODEGAVIRTUALID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, idBodegaDestino) },
                    { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                    { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };
                
            sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaOrigen,codArea), camposUpdInventario, condiciones);

        } else {
            String camposUpdInventario[][] = {
                    { Inventario.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoDisponible) },
                    { Inventario.CAMPO_TCSCASIGNACIONRESERVAID, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
                    { Inventario.CAMPO_IDVENDEDOR, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
                    { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                    { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };

            sql = UtileriasBD.armarQueryUpdate(getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodegaOrigen,codArea), camposUpdInventario, condiciones);

        }
        try {
            Qr.update(conn, sql);
            update = true;
        } catch (SQLException e) {
            conn.rollback();
            log.error("Rollback: problema al actualizar los datos del inventario.", e);
            update = false;

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(addLog(Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA, servicioPutAsignacion, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al actualizar seriados.", e.getMessage()));

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(),codArea);

            return update;
        }
        return update;
    }
}

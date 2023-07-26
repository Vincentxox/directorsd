package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.jornada.InputJornada;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCantInvJornada;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.jornada.OutputJornada;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCantInvJornada;
import com.consystec.sc.sv.ws.orm.CantInvJornada;
import com.consystec.sc.sv.ws.orm.FechaJornada;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.JornadaObservaciones;
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
public class OperacionJornada extends ControladorBase {
    private static final Logger log = Logger.getLogger(OperacionJornada.class);

    /**
     * Funci\u00F3n que realiza las operaciones al trabajar en m\u00E9todo PUT.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @param listaLog
     * @param servicioPut
     * @param logTransaccionModJornada
     * @param estadoLiquidada
     * @param estadoRechazada
     * @param estadoPendiente
     * @param estadoFinalizada
     * @param estadoIniciada
     * @param estadoAlta
     * @param tipoRechazo
     * @param tipoLiquidacion
     * @param tipoFinJornada
     * @return
     * @throws SQLException
     */
    public static OutputJornada doPut(Connection conn, InputJornada input, List<LogSidra> listaLog,
            String logTransaccionModJornada, String servicioPut, String tipoLiquidacion, String tipoRechazo,
            String estadoFinalizada, String estadoRechazada, String estadoLiquidada, String codArea, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doPut";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = null;
        OutputJornada output = new OutputJornada();
        Statement stmtUpdate = null;
        Statement stmtInsert =null;
        
        String sql = null;
        boolean update = true;
        
        List<Filtro> condiciones = new ArrayList<Filtro>();
        
        try{
        	conn.setAutoCommit(false);
        	 stmtUpdate = conn.createStatement();
             stmtInsert = conn.createStatement();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCJORNADAVENID, input.getIdJornada()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCCATPAISID, idPais.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_VENDEDOR, input.getIdVendedor()));

        String[] campos = { Jornada.CAMPO_ESTADO, Jornada.CAMPO_ESTADO_LIQUIDACION };
        Map<String, String> datosJornada = UtileriasBD.getSingleFirstData(conn, getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea), campos, condiciones);

        // procesos para liquidar jornada
        log.debug("la jornada se va a procesos de liquidaci\u00F3n");
        if (datosJornada.isEmpty()) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_JORNADA_NOEXISTE_93, null, nombreClase,
                    nombreMetodo, null, codArea);

        } else if (!datosJornada.get(Jornada.CAMPO_ESTADO).equalsIgnoreCase(estadoFinalizada)) {
            // la jornada no esta en un estado correcto
            log.debug("la jornada no esta finalizada");
            update = false;
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_ESTADO_JORNADA_368, null, nombreClase,
                    nombreMetodo, estadoFinalizada + ".", codArea);

        } else {
            if (datosJornada.get(Jornada.CAMPO_ESTADO_LIQUIDACION).equalsIgnoreCase(estadoLiquidada)) {
                // la jornada no esta en un estado de liquidacion correcto
                log.debug("la jornada esta finalizada pero ya fue liquidada");
                update = false;
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_ESTADO_LIQ_JORNADA_387, null,
                        nombreClase, nombreMetodo, null, codArea);
            } else {
                // procesos de liquidacion de jornada
                String estado = null;
                String fecha = null;
                String estadoPendiente = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD, Conf.SOL_ESTADO_PENDIENTE, codArea);

                if (input.getTipoOperacion().equalsIgnoreCase(tipoRechazo)) {
                    // procesos para rechazar una jornada finalizada
                    log.debug("la jornada se va a rechazar");
                    estado = estadoRechazada;
                    fecha = UtileriasJava.setUpdate(Conf.INSERT_NULL, null);

                } else if (input.getTipoOperacion().equalsIgnoreCase(tipoLiquidacion)) {
                    // procesos para liquidar una jornada finalizada
                    log.debug("la jornada se va a liquidar");
                    estado = estadoLiquidada;
                    fecha = UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null);
                }

                String camposUpd[][] = {
                    { Jornada.CAMPO_ESTADO_LIQUIDACION, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estado) },
                    { Jornada.CAMPO_ESTADO_PAGO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estadoPendiente) },
                    { Jornada.CAMPO_FECHA_LIQUIDACION, fecha },
                    { Jornada.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                    { Jornada.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
                };

                sql = UtileriasBD.armarQueryUpdate(getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea), camposUpd, condiciones);
                stmtUpdate.addBatch(sql);

                // log
                listaLog.add(addLog(logTransaccionModJornada, servicioPut, input.getIdJornada(), Conf.LOG_TIPO_JORNADA,
                        "Se cambi\u00F3 el estado de liquidaci\u00F3n de "
                                + datosJornada.get(Jornada.CAMPO_ESTADO_LIQUIDACION) + " a " + estado + ".",
                        ""));

                String camposInsert[] = {
                    JornadaObservaciones.CAMPO_TCSCOBSJORNADAID,
                    JornadaObservaciones.CAMPO_TCSCJORNADAID,
                    JornadaObservaciones.CAMPO_OBSERVACION,
                    JornadaObservaciones.CAMPO_CREADO_EL,
                    JornadaObservaciones.CAMPO_CREADO_POR
                };

                List<String> inserts = new ArrayList<String>();
                String insert = "("
                        + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, JornadaObservaciones.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdJornada(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getObservaciones(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
                + ") ";
                inserts.add(insert);
                sql = UtileriasBD.armarQueryInsert(JornadaObservaciones.N_TABLA, camposInsert, inserts);
                stmtInsert.addBatch(sql);
            }
        }

        if (update) {
            int[] updateCounts = stmtUpdate.executeBatch();
            int[] insertCounts = stmtInsert.executeBatch();
  

            update = UtileriasJava.validarBatch(1, updateCounts) && UtileriasJava.validarBatch(1, insertCounts);

            if (update) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_JORNADA_46, null, nombreClase,
                        nombreMetodo, null, codArea);

                conn.commit();
            } else {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null, nombreClase,
                        nombreMetodo, null, codArea);
            }
        }

        if (!update) {
            log.debug("Rollback");
            conn.rollback();
        }
        }finally{
            DbUtils.closeQuietly(stmtUpdate);
            DbUtils.closeQuietly(stmtInsert);
        }

        output.setRespuesta(respuesta);

        return output;
    }

    /**
     * Funci\u00F3n que verifica si una ruta o panel posee jornada iniciada.
     * 
     * @param conn
     *            Objeto de tipo connection con la conexi\u00F3n activa.
     * @param idPanelRuta
     *            Id de la panel o ruta a consultar.
     * @param tipoPanelRuta
     *            Nombre del tipo a consultar, pudiendo ser PANEL o RUTA.
     * @return <b>true</b> en caso de encontrar jornadas sin liquidar,
     *         <b>false</b> en caso contrario.
     * @throws SQLException
     */
    public static boolean existeJornadaIniciada(Connection conn, String idPanelRuta, String tipoPanelRuta, String codArea, BigDecimal idPais)
            throws SQLException {
        String estadoIniciada = UtileriasJava.getConfig(conn, Conf.GRUPO_JORNADA_ESTADOS, Conf.JORNADA_ESTADO_INICIADA, codArea);

        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCCATPAISID, idPais.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_IDTIPO, idPanelRuta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.CAMPO_DESCRIPCION_TIPO, tipoPanelRuta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.CAMPO_ESTADO, estadoIniciada));

        if (UtileriasBD.selectCount(conn, getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea), condiciones) > 0) {
            return true;
        }

        return false;
    }
    
    /**
     * Funci\u00F3n que verifica si una ruta o panel posee jornadas sin liquidar.
     * 
     * @param conn
     *            Objeto de tipo connection con la conexi\u00F3n activa.
     * @param idPanelRuta
     *            Id de la panel o ruta a consultar.
     * @param tipoPanelRuta
     *            Nombre del tipo a consultar, pudiendo ser PANEL o RUTA.
     * @return <b>true</b> en caso de encontrar jornadas sin liquidar,
     *         <b>false</b> en caso contrario.
     * @throws SQLException
     */
    public static boolean existeJornadaSinLiquidar(Connection conn, String idPanelRuta, String tipoPanelRuta, String codArea, BigDecimal idPais)
            throws SQLException {
        String estadoRechazada = UtileriasJava.getConfig(conn, Conf.GRUPO_JORNADA_ESTADOS_LIQ, Conf.JORNADA_ESTADO_RECHAZADA, codArea);

        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCCATPAISID, idPais.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_IDTIPO, idPanelRuta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.CAMPO_DESCRIPCION_TIPO, tipoPanelRuta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Jornada.CAMPO_ESTADO_LIQUIDACION, estadoRechazada));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NULL_OR, Jornada.CAMPO_ESTADO_LIQUIDACION, null));

        if (UtileriasBD.selectCount(conn, getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea), condiciones) > 0) {
            return true;
        }

        return false;
    }

    public static OutputJornada asignaFechaJornada(Connection conn, InputJornada input, String codArea, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "asignaFechaJornada";
        String nombreClase = new CurrentClassGetter().getClassName();
        PreparedStatement pstmt = null;
        Respuesta respuesta = null;
        OutputJornada output = new OutputJornada();
        String sql = "";
        
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, FechaJornada.CAMPO_TCSCCATPAISID, idPais.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, FechaJornada.CAMPO_VENDEDOR, input.getIdVendedor()));
        String idFecha = UtileriasBD.getOneRecord(conn, FechaJornada.CAMPO_TCSCFECHAJORNADAID, getParticion(FechaJornada.N_TABLA, Conf.PARTITION, "", codArea), condiciones);

        try {
            if (idFecha.equals("")) {
	            // se crea el registro
	            String campos[] = {
	                    FechaJornada.CAMPO_TCSCFECHAJORNADAID,
	                    FechaJornada.CAMPO_TCSCCATPAISID,
	                    FechaJornada.CAMPO_VENDEDOR,
	                    FechaJornada.CAMPO_FECHA,
	                    FechaJornada.CAMPO_CREADO_EL,
	                    FechaJornada.CAMPO_CREADO_POR
	            };
	    
	            List<String> inserts = new ArrayList<String>();
	            String valores = "("
	                + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, FechaJornada.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
	                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais.toString(), Conf.INSERT_SEPARADOR_SI)
	                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdVendedor(), Conf.INSERT_SEPARADOR_SI)
	                + UtileriasJava.setInsertFecha(Conf.INSERT_TIMESTAMP, input.getFecha(), Conf.TXT_FORMATO_FECHA_CORTA_GT, Conf.INSERT_SEPARADOR_SI)
	                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
	                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
	            + ") ";
	            inserts.add(valores);

                sql = UtileriasBD.armarQueryInsert(FechaJornada.N_TABLA, campos, inserts);
            } else {
                String campos[][] = {
	                    { FechaJornada.CAMPO_FECHA, UtileriasJava.setUpdateFecha(Conf.INSERT_TIMESTAMP, input.getFecha(), Conf.TXT_FORMATO_FECHA_CORTA_GT) },
	                    { FechaJornada.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
	                    { FechaJornada.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
	            };

                condiciones.clear();
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, FechaJornada.CAMPO_TCSCFECHAJORNADAID, idFecha));
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, FechaJornada.CAMPO_TCSCCATPAISID, idPais.toString()));

                sql = UtileriasBD.armarQueryUpdate(getParticion(FechaJornada.N_TABLA, Conf.PARTITION, "", codArea), campos, condiciones);
            }

            pstmt = conn.prepareStatement(sql);
            int res = pstmt.executeUpdate();

            if (res > 0) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_FECHA_JORNADA_71, null, nombreClase,
                        nombreMetodo, null, codArea);

            } else {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_FECHA_JORNADA_849, null,
                        nombreClase, nombreMetodo, null, codArea);
            }

            output.setRespuesta(respuesta);
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
        return output;
    }

    /**
     * Funci\u00F3n que valida si un vendedor es responsable o no.
     * @param conn Objeto con la conexi\u00F3n activa.
     * @param idVendedor ID del vendedor a consultar.
     * @param estadoAlta Par\u00E9metro con el estado de alta.
     * @param tipoPanel Par\u00E9metro con el tipo Panel.
     * @return true si es vendedor responsable de alguna ruta o panel, false si no es responsable o no existe.
     * @throws SQLException
     */
    public static boolean validarResponsable(Connection conn, String idVendedor, String estadoAlta, String tipoPanel, BigDecimal idPais)
            throws SQLException {
        int cant = 0;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

       
	        String sql = "SELECT SUM(CANT) AS CANT FROM ("
                    + "SELECT COUNT(1) AS CANT FROM TC_SC_RUTA WHERE TCSCCATPAISID = ?"
                    + " AND SECUSUARIOID =? AND UPPER(ESTADO) = ? UNION "
	                + "SELECT COUNT(1) AS CANT FROM TC_SC_VEND_PANELPDV WHERE TCSCCATPAISID = ?" 
                    + " AND UPPER(TIPO) =? AND VENDEDOR = ?"
	                + " AND RESPONSABLE = 1 AND UPPER(ESTADO) =?)";
	    try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1,  idPais);
            pstmt.setBigDecimal(2, new BigDecimal(idVendedor));
            pstmt.setString(3, estadoAlta.toUpperCase());
            pstmt.setBigDecimal(4,  idPais);
            pstmt.setString(5, tipoPanel.toUpperCase());
            pstmt.setBigDecimal(6, new BigDecimal(idVendedor));
            pstmt.setString(7, estadoAlta.toUpperCase());
            rst = pstmt.executeQuery();

            if (rst.next()) {
                do {
                    cant += rst.getInt("CANT");
                } while (rst.next());
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return cant > 0 ? true : false;
    }
    
    public static OutputJornada getFechaJornada(Connection conn, InputJornada input, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "getFechaJornada";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputJornada output = new OutputJornada();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql = null;
        InputJornada item = new InputJornada();
        SimpleDateFormat FORMATO_FECHA_GT = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA_GT);
        SimpleDateFormat FECHAHORA2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String campos[] = {
	                FechaJornada.CAMPO_VENDEDOR,
	                FechaJornada.CAMPO_FECHA,
	                FechaJornada.CAMPO_CREADO_EL,
	                FechaJornada.CAMPO_CREADO_POR,
	                FechaJornada.CAMPO_MODIFICADO_EL,
	                FechaJornada.CAMPO_MODIFICADO_POR
	        };
	
	        List<Filtro> condiciones = new ArrayList<Filtro>();
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, FechaJornada.CAMPO_TCSCCATPAISID, idPais.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, FechaJornada.CAMPO_VENDEDOR, input.getIdVendedor()));
	
            sql = UtileriasBD.armarQuerySelect(getParticion(FechaJornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()), campos, condiciones);
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_GET_FECHA_JORNADA_72, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                if (!rst.next()) {
                    item.setIdVendedor(input.getIdVendedor());
                    item.setFechaCierre("");
                    item.setCreado_el("");
                    item.setCreado_por("");

                } else {
                    do {
	                    item.setIdVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, FechaJornada.CAMPO_VENDEDOR));
	                    item.setFechaCierre(UtileriasJava.formatStringDate(rst.getString(Jornada.CAMPO_FECHA), FECHAHORA2, FORMATO_FECHA_GT));
	                    item.setCreado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Jornada.CAMPO_CREADO_EL));
	                    item.setCreado_por(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_CREADO_POR));
	                    item.setModificado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Jornada.CAMPO_MODIFICADO_EL));
	                    item.setModificado_por(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_MODIFICADO_POR));
	
	                } while (rst.next());
                }
            }

            output.setRespuesta(respuesta);
            output.setJornada(item);
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return output;
    }

    public static boolean borrarFechaCierre(Connection conn, String idResponsable, String usuario, String codArea, BigDecimal idPais) throws SQLException {
        PreparedStatement pstmt = null;
        int res = 0;
        try {
	    	String campos[][] = {
                { FechaJornada.CAMPO_FECHA, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
                { FechaJornada.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                { FechaJornada.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) }
	        };
	        
	        List<Filtro> condiciones = new ArrayList<Filtro>();
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, FechaJornada.CAMPO_TCSCCATPAISID, idPais.toString()));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, FechaJornada.CAMPO_VENDEDOR, idResponsable));

            String sql = UtileriasBD.armarQueryUpdate(getParticion(FechaJornada.N_TABLA, Conf.PARTITION, "", codArea), campos, condiciones);

            pstmt = conn.prepareStatement(sql);
            res = pstmt.executeUpdate();
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
        return res > 0 ? true : false;
    }
    
    static boolean insertaInvJornada(Connection conn, String idJornadaResponsable, String idVendedorResponsable,
            String idBodega, String usuario, String estadoProcesoSiniestro, String codArea, BigDecimal idPais) throws SQLException {
        String estadoDisponible = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DISPONIBLE, codArea);
        String estadoProcesoDevolucion = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DEVOLUCION, codArea);
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        PreparedStatement pstmt2 = null;
        Statement stmtInsert = null;
        int[] insertCounts;
    
            // se arma el listado de campos a insertar

            String campos[] = {
	                CantInvJornada.CAMPO_TCSC_CANTINV_ID,
	                CantInvJornada.CAMPO_TCSCCATPAISID,
	                CantInvJornada.CAMPO_IDJORNADA_RESPONSABLE,
	                CantInvJornada.CAMPO_VENDEDOR_RESPONSABLE,
	                CantInvJornada.CAMPO_ARTICULO,
	                CantInvJornada.CAMPO_TIPO_INV,
	                CantInvJornada.CAMPO_CANT_INICIAL,
	                CantInvJornada.CAMPO_CANT_VENDIDA,
	                CantInvJornada.CAMPO_CANT_RESERVADO,
	                CantInvJornada.CAMPO_CANT_PROC_DEV,
	                CantInvJornada.CAMPO_CANT_DEVOLUCION,
	                CantInvJornada.CAMPO_CANT_PROC_SINIESTRO,
	                CantInvJornada.CAMPO_CANT_SINIESTRO,
	                CantInvJornada.CAMPO_TIPO_GRUPO_SIDRA,
	                CantInvJornada.CAMPO_CREADO_EL,
	                CantInvJornada.CAMPO_CREADO_POR
	        };

	        // se obtienen todos los datos del inventario inicial
	        String sql = "SELECT articulo, tipo_inv, tipo_grupo_sidra, SUM(cantidad) AS cantidad"
	            + " FROM " + getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodega, codArea) + " WHERE "
                    + " tcsccatpaisid = ?" 
	                + " AND tcscbodegavirtualid = ?"
	                + " AND idvendedor = ?"
	                + " AND (UPPER(ESTADO) = ?" 
                        + " OR UPPER(ESTADO) = ?"
                        + " OR UPPER(ESTADO) = ?)"
	                + " GROUP BY articulo, tipo_inv,tipo_grupo_sidra ";
	   try {
            log.debug("Qry inv inicial: " + sql);

            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, idPais);
            pstmt.setBigDecimal(2, new BigDecimal(idBodega));
            pstmt.setBigDecimal(3, new BigDecimal(idVendedorResponsable));
            pstmt.setString(4, estadoDisponible.toUpperCase());
            pstmt.setString(5,  estadoProcesoDevolucion.toUpperCase() );
            pstmt.setString(6,estadoProcesoSiniestro.toUpperCase());
            rst = pstmt.executeQuery();
            stmtInsert = conn.createStatement();

            if (rst.next()) {
                // se arma el listado de elementos a insertar
                do {
                    String insert = "("
	                    + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, CantInvJornada.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
	                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais.toString(), Conf.INSERT_SEPARADOR_SI)
	                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idJornadaResponsable, Conf.INSERT_SEPARADOR_SI)
	                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idVendedorResponsable, Conf.INSERT_SEPARADOR_SI)
	                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Inventario.CAMPO_ARTICULO), Conf.INSERT_SEPARADOR_SI)
	                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO, UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Inventario.CAMPO_TIPO_INV), Conf.INSERT_SEPARADOR_SI)
	                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Inventario.CAMPO_CANTIDAD), Conf.INSERT_SEPARADOR_SI)
	                    + "0, 0, 0, 0, 0, 0, "
	                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO, UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Inventario.CAMPO_TIPO_GRUPO_SIDRA), Conf.INSERT_SEPARADOR_SI)
	                    + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
	                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO, usuario, Conf.INSERT_SEPARADOR_NO)
                    + ") ";

                    stmtInsert.addBatch(UtileriasBD.armarQryInsert(CantInvJornada.N_TABLA, campos, insert));

                } while (rst.next());
            }
                // se insertan los valores
                 insertCounts = stmtInsert.executeBatch();
            } finally {
                DbUtils.closeQuietly(stmtInsert);
                DbUtils.closeQuietly(pstmt);
                DbUtils.closeQuietly(rst);
            }
	   
	   	if(insertCounts!=null){
                // insert de ids para los conteos posteriores
	            sql =  insertinvJornada(  idJornadaResponsable,  idVendedorResponsable,
	                     idBodega, estadoDisponible, estadoProcesoDevolucion, estadoProcesoSiniestro, codArea, idPais);

        try{
	            pstmt2 = conn.prepareStatement(sql);
                int res = pstmt2.executeUpdate();
                log.trace("ids insertados: " + res);
        }finally{
        	DbUtils.closeQuietly(pstmt2);
        }
                return UtileriasJava.validarBatch(1, insertCounts);
	   	}
	   	
        return false;
    }
    
    public static String insertinvJornada( String idJornadaResponsable, String idVendedorResponsable,
            String idBodega,String estadoDisponible,String estadoProcesoDevolucion,String estadoProcesoSiniestro, String codArea, BigDecimal idPais) {
    	String sql="";
        sql = "INSERT INTO TC_SC_ID_INV_JORNADA (TCSCINVJORNADAID, TCSCJORNADAVENID, TCSCINVENTARIOID)"
                + " WITH DATA AS ("
                    + "SELECT TCSCINVENTARIOINVID FROM " + getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodega, codArea) + " WHERE "
                        + Inventario.CAMPO_TCSCCATPAISID + " = " + idPais
                        + " AND " + Inventario.CAMPO_TCSCBODEGAVIRTUALID + " = " + idBodega
                        + " AND " + Inventario.CAMPO_IDVENDEDOR + " = " + idVendedorResponsable 
                        + " AND (UPPER(" + Inventario.CAMPO_ESTADO + ") = '" + estadoDisponible.toUpperCase() + "'"
                                + " OR UPPER(" + Inventario.CAMPO_ESTADO + ") = '" + estadoProcesoDevolucion.toUpperCase() + "'"
                                + " OR UPPER(" + Inventario.CAMPO_ESTADO + ") = '" + estadoProcesoSiniestro.toUpperCase() + "')"
                + ") SELECT TC_SC_ID_INV_JORNADA_SQ.NEXTVAL, " + idJornadaResponsable + ", TCSCINVENTARIOINVID FROM DATA";
        return sql;
    }

    public static boolean actualizarInvJornada(Connection conn, String idJornada, String idResponsable, String usuario, String codArea, BigDecimal idPais)
            throws SQLException {
        Statement stmtUpdate = conn.createStatement();
        String sql = "";
        int[] updateCounts;
        List<Filtro> condiciones = new ArrayList<Filtro>();
        int res = 0;
        PreparedStatement pstmt=null;
        // se obtienen y actualizan los campos en tabla de inventario jornada
        InputReporteCantInvJornada input = new InputReporteCantInvJornada();
        try {
            input.setIdJornada(idJornada);
            input.setCodArea(codArea);
            OutputReporteCantInvJornada datosInv = OperacionReporteCantInvJornada.doGet(conn, input, true, idPais);

            if (new BigDecimal(datosInv.getRespuesta().getCodResultado()).intValue() < 0) {
                return false;

            }else if (new BigDecimal(datosInv.getRespuesta().getCodResultado()).intValue() ==-847){
            	res=1;
            } else {
	            //se actualiza y registra el valor de las cantidades del inventario
	            for (int i = 0; i < datosInv.getDatos().getArticulos().size(); i++) {
	                String campos[][] = {
	                    { CantInvJornada.CAMPO_CANT_RESERVADO, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, datosInv.getDatos().getArticulos().get(i).getCantReservada()) },
	                    { CantInvJornada.CAMPO_CANT_VENDIDA, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, datosInv.getDatos().getArticulos().get(i).getCantVendida()) },
	                    { CantInvJornada.CAMPO_CANT_PROC_DEV, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, datosInv.getDatos().getArticulos().get(i).getCantProcDevolucion()) },
	                    { CantInvJornada.CAMPO_CANT_DEVOLUCION, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, datosInv.getDatos().getArticulos().get(i).getCantDevuelta()) },
	                    { CantInvJornada.CAMPO_CANT_PROC_SINIESTRO, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, datosInv.getDatos().getArticulos().get(i).getCantProcSiniestro()) },
	                    { CantInvJornada.CAMPO_CANT_SINIESTRO, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, datosInv.getDatos().getArticulos().get(i).getCantSiniestrada()) },
	                    { CantInvJornada.CAMPO_CANT_DISPONIBLE, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, datosInv.getDatos().getArticulos().get(i).getCantFinal()) },
	                    { CantInvJornada.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
	                    { CantInvJornada.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) }
	                };
	
	                condiciones.clear();
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CantInvJornada.CAMPO_TCSCCATPAISID, idPais.toString()));
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CantInvJornada.CAMPO_IDJORNADA_RESPONSABLE, idJornada));
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CantInvJornada.CAMPO_VENDEDOR_RESPONSABLE, idResponsable));
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CantInvJornada.CAMPO_ARTICULO, datosInv.getDatos().getArticulos().get(i).getIdArticulo()));
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, CantInvJornada.CAMPO_TIPO_INV, datosInv.getDatos().getArticulos().get(i).getTipoInv()));
	
	                sql = UtileriasBD.armarQueryUpdate(getParticion(CantInvJornada.N_TABLA, Conf.PARTITION, "", codArea), campos, condiciones);
	                stmtUpdate.addBatch(sql);
                }

                updateCounts = stmtUpdate.executeBatch();
                if (UtileriasJava.validarBatch(1, updateCounts)) {
                    // se borran los registros de inventario inicial
                    condiciones.clear();
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, "TCSCJORNADAVENID", idJornada));

                    sql = UtileriasBD.armarQueryDelete("TC_SC_ID_INV_JORNADA", condiciones);
                    pstmt = conn.prepareStatement(sql);
                    res = pstmt.executeUpdate();
               
                }
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(stmtUpdate);
            DbUtils.closeQuietly(stmtUpdate);
        }
        return res > 0 ? true : false;
    }
}

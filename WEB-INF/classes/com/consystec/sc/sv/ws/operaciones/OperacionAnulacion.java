package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.anulacion.InputAnulacion;
import com.consystec.sc.ca.ws.input.asignacion.RespuestaAsignacion;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.solicitud.InputArticuloSolicitud;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.anulacion.OutputAnulacion;
import com.consystec.sc.sv.ws.metodos.CtrlAnulacion;
import com.consystec.sc.sv.ws.orm.AnulacionVenta;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.Venta;
import com.consystec.sc.sv.ws.orm.VentaDet;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class OperacionAnulacion {
	private OperacionAnulacion(){}
    private static final Logger log = Logger.getLogger(OperacionAnulacion.class);
  

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return
     * @throws SQLException
     */
    public static OutputAnulacion doGet(Connection conn, InputAnulacion input, int metodo, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<InputAnulacion> list = new ArrayList<InputAnulacion>();
        Respuesta respuesta = new Respuesta();
        OutputAnulacion output = new OutputAnulacion();

        PreparedStatement pstmt = null;
        ResultSet rst = null;

        List<Filtro> condiciones = new ArrayList<Filtro>();

        try {
            String tablas[] = { ControladorBase.getParticion(AnulacionVenta.N_TABLA, Conf.PARTITION, "", input.getCodArea()), VendedorDTS.N_TABLA };

            String[][] campos = CtrlAnulacion.obtenerCamposGet();

            condiciones = CtrlAnulacion.obtenerCondiciones(input, metodo, idPais);
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, AnulacionVenta.N_TABLA,
                    AnulacionVenta.CAMPO_VENDEDOR, VendedorDTS.N_TABLA, VendedorDTS.CAMPO_VENDEDOR));

            String sql = UtileriasBD.armarQueryGetMultiple(campos, tablas, condiciones, null);

            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_ANULACIONES_801, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    output.setRespuesta(respuesta);
                } else {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CARGA_ANULACIONES_22, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    do {
	                    InputAnulacion item = new InputAnulacion();
	                    item.setIdAnulacion(rst.getString(AnulacionVenta.CAMPO_TCSCANULACIONID));
	                    item.setIdJornada(rst.getString(AnulacionVenta.CAMPO_TCSCJORNADAVENID));
	                    item.setIdVendedor(rst.getString(AnulacionVenta.CAMPO_VENDEDOR));
	                    item.setNombreVendedor(rst.getString(VendedorDTS.CAMPO_USUARIO));
	                    item.setIdVenta(rst.getString(AnulacionVenta.CAMPO_TCSCVENTAID));
	                    item.setFechaAnulacion(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, AnulacionVenta.CAMPO_FECHA_ANULACION));
	                    item.setObservaciones(rst.getString(AnulacionVenta.CAMPO_OBSERVACIONES));
	                    item.setCreado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, AnulacionVenta.CAMPO_CREADO_EL));
	                    item.setCreado_por(rst.getString(AnulacionVenta.CAMPO_CREADO_POR));

                        list.add(item);
                    } while (rst.next());

                    output = new OutputAnulacion();
                    output.setRespuesta(respuesta);
                    output.setAnulaciones(list);
                }
            }

        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return output;
    }

    /**
     * Funci\u00F3n que arma el query a utilizar al trabajar en m\u00E9todo POST.
     * 
     * @param conn
     * @param input
     * @param listaLog 
     * @return
     * @throws SQLException
     */
    public static OutputAnulacion doPost(Connection conn, InputAnulacion input, List<LogSidra> listaLog, BigDecimal idPais)
            throws SQLException {
    	
        String nombreMetodo = "doPost";
        String nombreClase = new CurrentClassGetter().getClassName();
        OutputAnulacion output = new OutputAnulacion();
        Respuesta respuesta = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        RespuestaAsignacion anulacion = new RespuestaAsignacion();

        try {
            conn.setAutoCommit(false);

            String campos[] = CtrlAnulacion.obtenerCamposPost();
            List<String> inserts = CtrlAnulacion.obtenerInsertsPost(input, AnulacionVenta.SEQUENCE, idPais );

            String sql = UtileriasBD.armarQueryInsert(AnulacionVenta.N_TABLA, campos, inserts);

            String generatedColumns[] = { AnulacionVenta.CAMPO_TCSCANULACIONID };
            pstmt = conn.prepareStatement(sql, generatedColumns);

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();

            if (rs != null) {
                int idPadre = 0;
                if (rs.next()) {
                    idPadre = rs.getInt(1);
                }
                log.debug("idAnulacion: " + idPadre);

                anulacion = anularVenta(conn, idPadre, input, listaLog, idPais);

                if (anulacion.isResultado()) {
                    // se anularon los elementos correctamente

                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_ANULACION_28, null, nombreClase,
                            nombreMetodo, anulacion.getDescripcion(),input.getCodArea());
                    output.setIdAnulacion(idPadre + "");
                    conn.commit();
                } else {
                    // no se anulo la venta
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
                            nombreMetodo, anulacion.getDescripcion(),input.getCodArea());
                    log.trace("Rollback");
                    conn.rollback();
                }
            }

        } finally {
        	DbUtils.closeQuietly(pstmt);
        	DbUtils.closeQuietly(rs);
            conn.setAutoCommit(true);
            output.setRespuesta(respuesta);
        }

        return output;
    }

    private static RespuestaAsignacion anularVenta(Connection conn, int idPadre, InputAnulacion input,
            List<LogSidra> listaLog, BigDecimal idPais) throws SQLException {
        String idVenta = input.getIdVenta();
        String usuario = input.getUsuario();
        RespuestaAsignacion respuesta = new RespuestaAsignacion();
        Statement stmtUpdateVenta=null;
        Statement stmtUpdatesVentaDet = null;
        Statement stmtUpdatesInvSeriado =null;
        Statement stmtUpdatesInvNoSeriado =null;
        String tablaInventarioConParticion = ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, "", input.getCodArea());
       try{
	        stmtUpdateVenta = conn.createStatement();
	        stmtUpdatesVentaDet = conn.createStatement();
	        stmtUpdatesInvSeriado = conn.createStatement();
	        stmtUpdatesInvNoSeriado = conn.createStatement();
	        String update = "";
	    
	        List<InputArticuloSolicitud> articulosSeriados = new ArrayList<InputArticuloSolicitud>();
	        List<InputArticuloSolicitud> articulosNoSeriados = new ArrayList<InputArticuloSolicitud>();
	        String respListados = null;
	
	        String estadoAnulada = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_VENTA, Conf.VENTA_ANULADO,input.getCodArea());
	        String estadoVendido = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_VENDIDO,input.getCodArea());
	        String estadoDisponible = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DISPONIBLE,input.getCodArea());
	        String articuloRecarga = UtileriasJava.getConfig(conn, Conf.GRUPO_ARTICULO_CANTIDAD, Conf.ARTICULO_RECARGA,input.getCodArea());
	
	        /*----------------------------------------------------------------------*/
	        //se obtienen datos de la venta
	        List<Filtro> condiciones = new ArrayList<Filtro>();
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Venta.CAMPO_TCSCVENTAID, idVenta));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Venta.CAMPO_TCSCCATPAISID, idPais+""));
	
	        /*----------------------------------------------------------------------*/
	        // se actualiza el encabezado de la venta
	        String[][] camposVenta = {
	                { Venta.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estadoAnulada) },
	                { Venta.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) },
	                { Venta.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) }
	        };
	
	        update = UtileriasBD.armarQueryUpdate(ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", input.getCodArea()), camposVenta, condiciones);
	        stmtUpdateVenta.addBatch(update);
	
	        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ANULACION, Conf.LOG_POST_ANULACION, idPadre + "",
	                Conf.LOG_TIPO_ANULACION, "Se cambi\u00F3 la venta con ID " + idVenta + " a estado " + estadoAnulada.toUpperCase() + ".", ""));
	
	        /*----------------------------------------------------------------------*/
	        // se actualiza el detalle de la venta
	        condiciones.clear();
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VentaDet.CAMPO_TCSCVENTAID, idVenta));
	        
	        String[][] camposDetVenta = {
	                { VentaDet.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estadoAnulada) },
	                { VentaDet.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) },
	                { VentaDet.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) }
	        };
	
	        update = UtileriasBD.armarQueryUpdate(VentaDet.N_TABLA, camposDetVenta, condiciones);
	//        update = UtileriasBD.armarQueryUpdate(ControladorBase.getParticion(VentaDet.N_TABLA, Conf.PARTITION_DATE, fechaVenta), camposDetVenta, condiciones);//TODO DESCOMENTAR
	        stmtUpdatesVentaDet.addBatch(update);
	
	        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ANULACION, Conf.LOG_POST_ANULACION, idPadre + "",
	                Conf.LOG_TIPO_ANULACION, "Se cambiaron los detalles de la venta con ID " + idVenta + " a estado "
	                        + estadoAnulada.toUpperCase() + ".", ""));
	
	        /*----------------------------------------------------------------------*/
	        // se obtiene la bodega de la venta
	        String idBodega = UtileriasBD.getOneRecord(conn, VentaDet.CAMPO_TCSCBODEGAVIRTUALID, VentaDet.N_TABLA, condiciones);
	
	        /*----------------------------------------------------------------------*/
	        // se actualizan los art\u00EDculos del inventario
	        List<InputArticuloSolicitud> listInventario = getInventarioAnulacion(conn, idVenta);
	        if (listInventario.size() > 0) {
	
	            // se separan los listados seriados y no seriados
	            for (int i = 0; i < listInventario.size(); i++) {
	                if (listInventario.get(i).getIdArticulo().equals(articuloRecarga)) {
	                    continue;
	                }
	                if (listInventario.get(i).getSerie() != null && !listInventario.get(i).getSerie().equals("")) {
	                    articulosSeriados.add(listInventario.get(i));
	                } else {
	                    articulosNoSeriados.add(listInventario.get(i));
	                }
	            }
	
	            if (articulosSeriados.size() > 0) {
	                // se valida el listado de articulos seriados
	                respListados = validaSeriados(articulosSeriados, input.getCodArea());
	            }
	
	            if (articulosNoSeriados.size() > 0) {
	                // se valida el listado de articulos por cantidad
	                respListados = validaNoSeriados(articulosNoSeriados, input.getCodArea());
	            }
	
	            if (respListados != null && !respListados.equals("")) {
	                respuesta.setResultado(false);
	                respuesta.setDescripcion(respListados);
	                return respuesta;
	            }
	
	            // se crean los updates de los articulos seriados
	            for (int i = 0; i < articulosSeriados.size(); i++) {
	                condiciones.clear();//TODO Pendiente de definicion de la serie asociada.
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCVENTAID, idVenta));
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, idBodega));
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_SERIE, articulosSeriados.get(i).getSerie()));
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_TIPO_INV, articulosSeriados.get(i).getTipoInv()));
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoVendido));
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
	                
	                String[][] camposInv = {
	                    { Inventario.CAMPO_TCSCVENTAID, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
	                    { Inventario.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoDisponible) },
	                    { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) },
	                    { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) }
	                };
	
	                update = UtileriasBD.armarQueryUpdate(tablaInventarioConParticion, camposInv, condiciones);
	                stmtUpdatesInvSeriado.addBatch(update);
	
	                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ANULACION, Conf.LOG_POST_ANULACION,
	                        idPadre + "", Conf.LOG_TIPO_ANULACION,
	                        "Se cambi\u00F3 el art\u00EDculo con serie " + articulosSeriados.get(i).getSerie() + " de estado "
	                                + estadoVendido.toUpperCase() + " a " + estadoDisponible.toUpperCase()
	                                + " por anulaci\u00F3n de venta ID " + idVenta + ".",
	                        ""));
	            }
	
	            // se crean los querys de las operaciones para articulos no seriados
	            for (int i = 0; i < articulosNoSeriados.size(); i++) {
	                // se verifica que exista el articulo disponible
	                condiciones.clear();
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, idBodega));
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO, articulosNoSeriados.get(i).getIdArticulo()));
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_TIPO_INV, articulosNoSeriados.get(i).getTipoInv()));
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoDisponible));
	
	                if (UtileriasBD.selectCount(conn, tablaInventarioConParticion, condiciones) > 0) {
	                    // si el articulo existe, se actualiza inventario de disponibles
	                    String camposInv[][] = {
	                        { Inventario.CAMPO_CANTIDAD, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, "(" + Inventario.CAMPO_CANTIDAD + " + " + articulosNoSeriados.get(i).getCantidad() + ")") },
	                        { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
	                        { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) }
	                    };
	
	                    update = UtileriasBD.armarQueryUpdate(tablaInventarioConParticion, camposInv, condiciones);
	                    stmtUpdatesInvNoSeriado.addBatch(update);
	
	                    // elimina el articulo de origen
	                    condiciones.remove(4); // se remueve el estado disponible
	                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCVENTAID, idVenta));
	                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_CANTIDAD, articulosNoSeriados.get(i).getCantidad()));
	                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoVendido));
	
	                    update = UtileriasBD.armarQueryDelete(tablaInventarioConParticion, condiciones);
	                    stmtUpdatesInvNoSeriado.addBatch(update);
	
	                } else {
	                    // si el articulo no existe, se cambia el estado del registro actual 
	                    condiciones.remove(4); // se remueve el estado disponible
	                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCVENTAID, idVenta));
	                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_CANTIDAD, articulosNoSeriados.get(i).getCantidad()));
	                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.CAMPO_ESTADO, estadoVendido));
	
	                    String[][] camposInv = {
	                        { Inventario.CAMPO_TCSCVENTAID, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
	                        { Inventario.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, estadoDisponible) },
	                        { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) },
	                        { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) }
	                    };
	
	                    update = UtileriasBD.armarQueryUpdate(tablaInventarioConParticion, camposInv, condiciones);
	                    stmtUpdatesInvNoSeriado.addBatch(update);
	                }
	
	                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ANULACION, Conf.LOG_POST_ANULACION,
	                        idPadre + "", Conf.LOG_TIPO_ANULACION,
	                        articulosNoSeriados.get(i).getCantidad() + " art\u00EDculos identificados con el ID "
	                                + articulosNoSeriados.get(i).getIdArticulo() + " se cambiaron de estado "
	                                + estadoVendido.toUpperCase() + " a " + estadoDisponible.toUpperCase()
	                                + " por anulaci\u00F3n de venta ID " + idVenta + ".",
	                        ""));
	            }
	
	        } else {
	            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_ANULACION, Conf.LOG_POST_ANULACION,
	                    idPadre + "", Conf.LOG_TIPO_ANULACION, "Se registr\u00F3 la anulaci\u00F3n de la venta ID " + idVenta
	                            + " pero no se modific\u00F3 ning\u00FAn art\u00EDculo del inventario.",
	                    ""));
	            respuesta.setResultado(true);
	            respuesta.setDescripcion(new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_REGISTROS_ANULACION_VENTA_803, null,
	                    null, null, null, input.getCodArea()).getDescripcion());
	            return respuesta;
	        }
	
	        // se realizan las operaciones
	        int[] countUpdVenta = stmtUpdateVenta.executeBatch();
	        int[] countUpdVentaDet = stmtUpdatesVentaDet.executeBatch();
	        int[] countUpdInvSeriado = stmtUpdatesInvSeriado.executeBatch();
	        int[] countUpdInvNoSeriado = stmtUpdatesInvNoSeriado.executeBatch();
	
	        // se valida que todos los inserts se hayan procesado correctamente
	        boolean resultado = UtileriasJava.validarBatch(1, countUpdVenta);
	        if (resultado) {
	            // se actualizo correctamente el encabezado
	
	            resultado = UtileriasJava.validarBatch(listInventario.size(), countUpdVentaDet);
	            if (resultado) {
	                // se actualizo correctamente el detalle completo
	
	                resultado = UtileriasJava.validarBatch(1, countUpdInvSeriado);
	                if (resultado) {
	                    // se actualizo correctamente el detalle completo
	
	                    resultado = UtileriasJava.validarBatch(1, countUpdInvNoSeriado);
	                }
	            }
	        }
	        respuesta.setResultado(resultado);
       }finally{
    	   DbUtils.closeQuietly(stmtUpdatesInvNoSeriado);
    	   DbUtils.closeQuietly(stmtUpdatesInvSeriado);
    	   DbUtils.closeQuietly(stmtUpdatesVentaDet);
    	   DbUtils.closeQuietly(stmtUpdateVenta);
       }
      
        return respuesta;
    }

    private static List<InputArticuloSolicitud> getInventarioAnulacion(Connection conn, String idVenta      ) throws SQLException {
        List<InputArticuloSolicitud> list = new ArrayList<InputArticuloSolicitud>();
        PreparedStatement pstmtIn = null;
        ResultSet rstIn = null;

        String[] campos = {
            VentaDet.CAMPO_ARTICULO,
            VentaDet.CAMPO_SERIE,
            VentaDet.CAMPO_SERIE_ASOCIADA,
            VentaDet.CAMPO_CANTIDAD,
            VentaDet.CAMPO_TIPO_INV
        };

        List<Filtro> condiciones = new ArrayList<Filtro>();
        try {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VentaDet.CAMPO_TCSCVENTAID, idVenta));

	        String sql = UtileriasBD.armarQuerySelect(VentaDet.N_TABLA, campos, condiciones);

            pstmtIn = conn.prepareStatement(sql);
            rstIn = pstmtIn.executeQuery();

            if (rstIn != null) {
                if (!rstIn.next()) {
                    log.debug("No existen registros del detalle de venta (probablemente solo sean recargas).");

                } else {
                    do {
                        InputArticuloSolicitud item = new InputArticuloSolicitud();
                        item.setIdArticulo(rstIn.getString(Inventario.CAMPO_ARTICULO));
                        item.setSerie(rstIn.getString(Inventario.CAMPO_SERIE));
                        item.setSerieAsociada(rstIn.getString(Inventario.CAMPO_SERIE_ASOCIADA));
                        item.setCantidad(rstIn.getString(Inventario.CAMPO_CANTIDAD));
                        item.setTipoInv(rstIn.getString(Inventario.CAMPO_TIPO_INV));

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

    /**
     * Funci\u00F3n que valida la parte seriada de los articulos.
     * 
     * @param articulos Listado de art\u00EDculos.
     * @return
     */
    private static String validaSeriados(List<InputArticuloSolicitud> articulos, String codArea) {
        String nombreMetodo = "validaSeriados";
        String nombreClase = new CurrentClassGetter().getClassName();

        String datosErroneos = "";
        boolean flag = false;
        int numeroArticulo = 0;

        for (int i = 0; i < articulos.size(); i++) {
            if (flag == true)
                break;
            String serie = null;
            String tipoInv = null;
            numeroArticulo = i + 1;

            if (articulos.get(i).getSerie() != null) {
                serie = articulos.get(i).getSerie().trim();
            }
            if (articulos.get(i).getTipoInv() != null) {
                tipoInv = articulos.get(i).getTipoInv().trim();
            }

            if (serie == null || serie.equals("")) {
                datosErroneos += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_SERIE_153, null,
                        nombreClase, nombreMetodo, numeroArticulo + ".",codArea).getDescripcion();
                flag = true;
            }

            if (tipoInv == null || tipoInv.equals("")) {
                datosErroneos += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_TIPOINV_152, null,
                        nombreClase, nombreMetodo, numeroArticulo + ".",codArea).getDescripcion();
                flag = true;
            }
        }

        numeroArticulo = 1;
        for (InputArticuloSolicitud detActual : articulos) {
            if (flag == true)
                break;
            int indexArt = 1;

            for (InputArticuloSolicitud detalle : articulos) {
                if (flag == true)
                    break;

                if (indexArt != numeroArticulo && (detalle.getSerie().toUpperCase().trim()
                        .equals(detActual.getSerie().toUpperCase().trim()))) {
                    log.error(" Los Art\u00EDculos #" + indexArt + " y #" + numeroArticulo + " tienen la misma serie.");
                    datosErroneos += " Los Art\u00EDculos #" + indexArt + " y #" + numeroArticulo
                            + " tienen la misma serie: " + detActual.getSerie() + ".";

                    flag = true;
                }
                indexArt++;
            }
            numeroArticulo++;
        }

        return datosErroneos;
    }
    
    /**
     * Funci\u00F3n que valida la parte no seriada de los articulos.
     * 
     * @param articulosNoSeriados
     * @return
     */
    private static String validaNoSeriados(List<InputArticuloSolicitud> articulosNoSeriados, String codArea) {
        String nombreMetodo = "validaNoSeriados";
        String nombreClase = new CurrentClassGetter().getClassName();

        String datosErroneos = "";
        boolean flag = false;
        int numeroArticulo = 0;

        for (int i = 0; i < articulosNoSeriados.size(); i++) {
            if (flag == true)
                break;

            String idArticulo = null;
            String cant = null;
            String tipoInv = null;

            numeroArticulo = i + 1;

            if (articulosNoSeriados.get(i).getIdArticulo() != null) {
                idArticulo = articulosNoSeriados.get(i).getIdArticulo().trim();
            }
            if (articulosNoSeriados.get(i).getCantidad() != null) {
                cant = articulosNoSeriados.get(i).getCantidad().trim();
            }
            if (articulosNoSeriados.get(i).getTipoInv() != null) {
                tipoInv = articulosNoSeriados.get(i).getTipoInv().trim();
            }

            if (idArticulo == null || idArticulo.equals("")) {
                datosErroneos += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_IDART_146, null,
                        nombreClase, nombreMetodo, numeroArticulo + ".", codArea).getDescripcion();
                flag = true;
            } else if (!ControladorBase.isNumeric(idArticulo)) {
                datosErroneos += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_IDART_NUM_147, null,
                        nombreClase, nombreMetodo, numeroArticulo + ".", codArea).getDescripcion();
                flag = true;
            }

            if (cant == null || cant.equals("")) {
                datosErroneos += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT_148, null,
                        nombreClase, nombreMetodo, numeroArticulo + ".", codArea).getDescripcion();
                flag = true;
            } else if (!ControladorBase.isNumeric(cant)) {
                datosErroneos += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT_NUM_149, null,
                        nombreClase, nombreMetodo, numeroArticulo + ".", codArea).getDescripcion();
                flag = true;
            } else if (new Integer(cant) <= 0) {
                datosErroneos += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT0_151, null,
                        nombreClase, nombreMetodo, numeroArticulo + ".", codArea).getDescripcion();
                flag = true;
            }

            if (tipoInv == null || tipoInv.equals("")) {
                datosErroneos += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_TIPOINV_152, null,
                        nombreClase, nombreMetodo, numeroArticulo + ".", codArea).getDescripcion();
                flag = true;
            }
        }

     

        return datosErroneos;
    }
}

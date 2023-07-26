package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.dts.InputDistribuidor;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.dts.OutputDistribuidor;
import com.consystec.sc.sv.ws.metodos.CtrlDistribuidor;
import com.consystec.sc.sv.ws.orm.AlmacenBod;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.Panel;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class OperacionDistribuidor {
	private OperacionDistribuidor(){}
    private static final Logger log = Logger.getLogger(OperacionDistribuidor.class);

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @param idPais 
     * @return OutputDistribuidor
     * @throws SQLException
     */
    public static OutputDistribuidor doGet(Connection conn, InputDistribuidor input, int metodo, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<InputDistribuidor> list = new ArrayList<InputDistribuidor>();

        Respuesta respuesta = null;
        OutputDistribuidor output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        
        String sql = null;
        List<Filtro> condiciones = CtrlDistribuidor.obtenerCondiciones(input, metodo, idPais);
        
        List<Order> orden = new ArrayList<Order>();
        orden.add(new Order(Distribuidor.CAMPO_NOMBRES, Order.ASC));

        try {
	        //*------------------------------------------------------------------
	        String camposM[][] = {
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_TC_SC_DTS_ID },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_TCSCBODEGAVIRTUALID },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_TIPO },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_NOMBRES },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_NUMERO },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_EMAIL },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_ADMINISTRADOR },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_PAGO_AUTOMATICO },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_CANAL },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_NUM_CONVENIO },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_COD_CLIENTE },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_COD_CUENTA },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_RESULTADO_SCL },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_ESTADO },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_CREADO_EL },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_CREADO_POR },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_MODIFICADO_EL },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_MODIFICADO_POR },
	            { BodegaVirtual.N_TABLA, "TCSCBODEGAVIRTUALID as tcscalmacenbodid " },
	            { BodegaVirtual.N_TABLA, " TCSCBODEGAVIRTUALID as tcbodegasclid" }
	        };
	        
	        String tablas[] = {
	            Distribuidor.N_TABLA,
	            BodegaVirtual.N_TABLA 
	        };
	        
	        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.N_TABLA, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, Distribuidor.N_TABLA, Distribuidor.CAMPO_TCSCBODEGAVIRTUALID));
	        sql = UtileriasBD.armarQueryGetMultiple(camposM, tablas, condiciones, orden);
	        //------------------------------------------------------------------*/
	
	        pstmt = conn.prepareStatement(sql);
	        rst = pstmt.executeQuery();
	        
	        if (rst != null) {
	            if (!rst.next()) {
	                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_DTS_818, null, nombreClase,
	                        nombreMetodo, null, input.getCodArea());
	
	                output = new OutputDistribuidor();
	                output.setRespuesta(respuesta);
	            } else {
	                respuesta = new Respuesta();
	                respuesta=new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
	                    nombreClase, nombreMetodo, null, input.getCodArea());
	                
	                do {
	                    InputDistribuidor item = new InputDistribuidor();
	                    item.setIdDTS(rst.getString(Distribuidor.CAMPO_TC_SC_DTS_ID));
	                    item.setTipo(rst.getString(Distribuidor.CAMPO_TIPO));
	                    item.setAdministrador(rst.getString(Distribuidor.CAMPO_ADMINISTRADOR));
	                    item.setNombres(rst.getString(Distribuidor.CAMPO_NOMBRES));
	                    item.setIdBodegaVirtual(rst.getString(Distribuidor.CAMPO_TCSCBODEGAVIRTUALID));
	                    item.setIdAlmacenBod(rst.getString(AlmacenBod.CAMPO_TCSCALMACENBODID));
	                    item.setIdBodegaSCL(rst.getString(AlmacenBod.CAMPO_TC_BODEGA_SCL_ID));
	                    item.setNumero(rst.getString(Distribuidor.CAMPO_NUMERO));
	                    item.setEmail(rst.getString(Distribuidor.CAMPO_EMAIL));
	                    item.setPagoAutomatico(rst.getString(Distribuidor.CAMPO_PAGO_AUTOMATICO));
	                    item.setCanal(UtileriasJava.getValue(rst.getString(Distribuidor.CAMPO_CANAL)));
	                    item.setNumConvenio(UtileriasJava.getValue(rst.getString(Distribuidor.CAMPO_NUM_CONVENIO)));
	                    item.setCodCuenta(UtileriasJava.getValue(rst.getString(Distribuidor.CAMPO_COD_CUENTA)));
	                    item.setCodCliente(UtileriasJava.getValue(rst.getString(Distribuidor.CAMPO_COD_CLIENTE)));
	                    item.setResultadoSCL(UtileriasJava.getValue(rst.getString(Distribuidor.CAMPO_RESULTADO_SCL)));
	                    item.setEstado(rst.getString(Distribuidor.CAMPO_ESTADO));
	                    item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(Distribuidor.CAMPO_CREADO_EL)));
	                    item.setCreado_por(rst.getString(Distribuidor.CAMPO_CREADO_POR));
	                    item.setModificado_el(UtileriasJava.formatStringDate(rst.getString(Distribuidor.CAMPO_MODIFICADO_EL)));
	                    item.setModificado_por(rst.getString(Distribuidor.CAMPO_MODIFICADO_POR));
	
	                    list.add(item);
	                } while (rst.next());
	
	                output = new OutputDistribuidor();
	                output.setRespuesta(respuesta);
	                output.setDistribuidor(list);
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
     * @param estadoAlta 
     * @param metodo 
     * @param idPais 
     * @return OutputDistribuidor
     * @throws SQLException
     */
    public static OutputDistribuidor doPost(Connection conn, InputDistribuidor input, String estadoAlta, int metodo,
            BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doPost";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputDistribuidor output = new OutputDistribuidor();
        BigDecimal existBodVirtual = null;

        //Verificando que no exista la bodega virtual a asignar en otro dts
        existBodVirtual = UtileriasJava.existeBodegaVirtual(conn, estadoAlta.toUpperCase(), input.getIdBodegaVirtual(), "", idPais);
        if (existBodVirtual.intValue() > 0) {
            log.error("Ya existe el recurso.");
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_BODEGA_VIRTUAL_DTS_834, null,
                    nombreClase, nombreMetodo, null, input.getCodArea());
            
            output.setRespuesta(respuesta);

            return output;
        }

        List<BigDecimal> datosDTS = verificarDatosDTS(conn, input, estadoAlta, metodo, idPais);
        if (!datosDTS.isEmpty() && datosDTS.get(0).intValue() > 0) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NOMBRE_DTS_835, null, nombreClase, nombreMetodo, null, input.getCodArea());
            output.setRespuesta(respuesta);
            return output;
        } else if (!datosDTS.isEmpty() && datosDTS.get(1).intValue() > 0) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_ADMIN_DTS_836, null, nombreClase, nombreMetodo, null, input.getCodArea());
            output.setRespuesta(respuesta);
            return output;
        } else if (!datosDTS.isEmpty() && datosDTS.get(2).intValue() > 0) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NUMERO_DTS_837, null, nombreClase, nombreMetodo, null, input.getCodArea());
            output.setRespuesta(respuesta);
            return output;
        } else if (!datosDTS.isEmpty() && datosDTS.get(3).intValue() > 0) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_CODCUENTA_DTS_873, null, nombreClase, nombreMetodo, null, input.getCodArea());
            output.setRespuesta(respuesta);
            return output;
        } else if (!datosDTS.isEmpty() && datosDTS.get(4).intValue() > 0) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_CODCLIENTE_DTS_874, null, nombreClase, nombreMetodo, null, input.getCodArea());
            output.setRespuesta(respuesta);
            return output;
        }

        String sql = null;
        String campos[] = CtrlDistribuidor.obtenerCamposPost();
        List<String> inserts = CtrlDistribuidor.obtenerInsertsPost(input, Distribuidor.SEQUENCE, estadoAlta, idPais);

        sql = UtileriasBD.armarQueryInsert(Distribuidor.N_TABLA, campos, inserts);
        BigDecimal idDTS = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            String generatedColumns[] = { Distribuidor.CAMPO_TC_SC_DTS_ID };
            pstmt = conn.prepareStatement(sql, generatedColumns);

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();

 
                if (rs.next()) {
                    idDTS = rs.getBigDecimal(1);
                }
                
        } finally {
           
            DbUtils.closeQuietly(rs);
	        DbUtils.closeQuietly(pstmt);
        } 
                log.debug("idDTS: " + idDTS);
              if (idDTS != null) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_DTS_41, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output.setIdDTS(idDTS.toString());
                output.setRespuesta(respuesta);

          
                conn.commit();
            } else {
                log.debug("Rollback");
                conn.rollback();
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output.setRespuesta(respuesta);
            }
              conn.setAutoCommit(true);

        return output;
    }    


    
    /**
     * Funci\u00F3n que arma el query a utilizar al modificar datos en m\u00E9todos PUT y DELETE.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @param estadoAlta 
     * @param estadoBaja 
     * @param idPais 
     * @return OutputDistribuidor
     * @throws SQLException
     */
    public static OutputDistribuidor doPutDel(Connection conn, InputDistribuidor input, int metodo, String estadoAlta,
            String estadoBaja, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doPutDel";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputDistribuidor output = new OutputDistribuidor();
        BigDecimal existBodVirtual = null;
        String sql = null;
        
        List<Filtro> condicionesExistencia = CtrlDistribuidor.obtenerCondicionesExistencia(input, metodo, estadoAlta, idPais);
        String existencia = UtileriasBD.verificarExistencia(conn, Distribuidor.N_TABLA, condicionesExistencia);
        if (new Integer(existencia) < 1) {
            log.error("No existe el recurso.");
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());

            output.setRespuesta(respuesta);

            return output;
        } else {
            if (metodo == Conf.METODO_PUT) {
                // Verificando que no exista la bodega virtual a asignar en otro dts
                String condicion = " AND TCSCDTSID NOT IN " + input.getIdDTS();

                existBodVirtual = UtileriasJava.existeBodegaVirtual(conn, estadoAlta.toUpperCase(), input.getIdBodegaVirtual(), condicion, idPais);

                if (existBodVirtual.intValue() > 0) {
                    log.error("Ya existe el recurso.");
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_BODEGA_VIRTUAL_DTS_834, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    output.setRespuesta(respuesta);

                    return output;
                }

                if (!input.getEstado().equals(estadoAlta)) {
                    //procesos de baja de elementos relacionados al DTS
                    List<BigDecimal> lista = validarBajaDTS(conn, input.getIdDTS(), estadoAlta, input.getCodArea(), idPais);

                    if (!lista.isEmpty() && lista.get(0).intValue() > 0) {
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_DTS_TIENE_PANEL_120, null, nombreClase, nombreMetodo, null, input.getCodArea());
                        output.setRespuesta(respuesta);
                        return output;
                    } else if (!lista.isEmpty() && lista.get(1).intValue() > 0) {
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_DTS_TIENE_RUTA_121, null, nombreClase, nombreMetodo, null, input.getCodArea());
                        output.setRespuesta(respuesta);
                        return output;
                    } else if (!lista.isEmpty() && lista.get(2).intValue() > 0) {
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_DTS_TIENE_PDV_122, null, nombreClase, nombreMetodo, null, input.getCodArea());
                         output.setRespuesta(respuesta);
                        return output;
                    } else if (!lista.isEmpty() && lista.get(3).intValue() > 0) {
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_DTS_TIENE_INV_123, null, nombreClase, nombreMetodo, null, input.getCodArea());
                        output.setRespuesta(respuesta);
                        return output;
                    }
                }
                
                List<BigDecimal> datosDTS = verificarDatosDTS(conn, input, estadoAlta, metodo, idPais);
                if (!datosDTS.isEmpty() && datosDTS.get(0).intValue() > 0) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NOMBRE_DTS_835, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    output.setRespuesta(respuesta);
                    return output;
                } else if (!datosDTS.isEmpty() && datosDTS.get(1).intValue() > 0) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_ADMIN_DTS_836, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    output.setRespuesta(respuesta);
                    return output;
                } else if (!datosDTS.isEmpty() && datosDTS.get(2).intValue() > 0) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NUMERO_DTS_837, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    output.setRespuesta(respuesta);
                    return output;
                }
            }
        }

        String campos[][] = CtrlDistribuidor.obtenerCamposPutDel(input, metodo, estadoBaja);
        List<Filtro> condiciones = CtrlDistribuidor.obtenerCondiciones(input, metodo, idPais);
        
        if (metodo == Conf.METODO_PUT) {
            sql = UtileriasBD.armarQueryUpdate(Distribuidor.N_TABLA, campos, condiciones);
        } else {
            sql = UtileriasBD.armarQueryDelete(Distribuidor.N_TABLA, condiciones);
        }
        
        try {
            conn.setAutoCommit(false);
            QueryRunner Qr = new QueryRunner();
            Qr.update(conn, sql);

            if (Qr != null) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_DTS_42, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output.setRespuesta(respuesta);
                conn.commit();
                

            }
        } finally {
            conn.setAutoCommit(true);

        }

        return output;
    }

    private static List<BigDecimal> verificarDatosDTS(Connection conn, InputDistribuidor input, String estadoAlta,
            int metodo, BigDecimal idPais) throws SQLException {
        String[] campos = { "COUNT(1)" };
        int indexFiltro = 2;
        
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TCSCCATPAISID, idPais.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.CAMPO_ESTADO, estadoAlta.toUpperCase()));
        if (metodo == Conf.METODO_PUT) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_NOT_IN_AND, Distribuidor.CAMPO_TC_SC_DTS_ID, input.getIdDTS()));
            indexFiltro = 3;
        }

        // validar nombre
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.CAMPO_NOMBRES, input.getNombres()));
        String queryNombre = UtileriasBD.armarQuerySelect(Distribuidor.N_TABLA, campos, condiciones);

        // validar admininistrador
        condiciones.set(indexFiltro, UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_ADMINISTRADOR, input.getAdministrador()));
        String queryAdministrador = UtileriasBD.armarQuerySelect(Distribuidor.N_TABLA, campos, condiciones);

        // validar n\u00FAmero
        condiciones.set(indexFiltro, UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_NUMERO, input.getNumero()));
        String queryNumero = UtileriasBD.armarQuerySelect(Distribuidor.N_TABLA, campos, condiciones);

        // validar cod_cuenta
        condiciones.set(indexFiltro, UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_COD_CUENTA, input.getCodCuenta()));
        String queryCodCuenta = UtileriasBD.armarQuerySelect(Distribuidor.N_TABLA, campos, condiciones);

        // validar cod_cliente
        condiciones.set(indexFiltro, UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_COD_CLIENTE, input.getCodCliente()));
        String queryCodCliente = UtileriasBD.armarQuerySelect(Distribuidor.N_TABLA, campos, condiciones);

        List<BigDecimal> list = new ArrayList<BigDecimal>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;

      
            String query = "SELECT "
                + "(" + queryNombre + ") AS NOMBRE, "
                + "(" + queryAdministrador + ") AS ADMIN, "
                + "(" + queryNumero + ") AS NUMERO, "
                + "(" + queryCodCuenta + ") AS CUENTA, "
                + "(" + queryCodCliente + ") AS CLIENTE "
            + "FROM DUAL";

            log.trace("Verificando datos del DTS: " + query);
       try {
            pstmt = conn.prepareStatement(query);
            rst = pstmt.executeQuery();

            if (rst.next()) {
                list.add(0, rst.getBigDecimal(1));
                list.add(1, rst.getBigDecimal(2));
                list.add(2, rst.getBigDecimal(3));
                list.add(3, rst.getBigDecimal(4));
                list.add(4, rst.getBigDecimal(5));
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }
        return list;
    }

    private static List<BigDecimal> validarBajaDTS(Connection conn, String idDTS, String estadoAlta, String codArea, BigDecimal idPais)
            throws SQLException {
        String estadoActivo = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_ESTADOS_PDV, Conf.ESTADO_ACTIVO, codArea);

        List<Filtro> condiciones = new ArrayList<Filtro>();
        List<BigDecimal> list = new ArrayList<BigDecimal>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String[] campos = { "COUNT(*)" };

        try {
	        //validar en panel
	        //select count(1) from tc_sc_panel where tcscdtsid = 60
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCCATPAISID, idPais.toString()));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCDTSID, idDTS));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Panel.CAMPO_ESTADO, estadoAlta));
	        String queryPanel = UtileriasBD.armarQuerySelect(Panel.N_TABLA, campos, condiciones);
	        
	        //validar en ruta
	        //select count(1) from tc_sc_ruta where tcscdtsid = 60
	        condiciones.clear();
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TCSCCATPAISID, idPais.toString()));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TC_SC_DTS_ID, idDTS));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Ruta.CAMPO_ESTADO, estadoAlta));
	        String queryRuta = UtileriasBD.armarQuerySelect(Ruta.N_TABLA, campos, condiciones);
	        
	        //validar en pdv
	        //select count(1) from tc_sc_puntoventa where tcscdtsid = 60
	        condiciones.clear();
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCCATPAISID, idPais.toString()));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCDTSID, idDTS));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PuntoVenta.CAMPO_ESTADO, estadoActivo));
	        String queryPDVs = UtileriasBD.armarQuerySelect(PuntoVenta.N_TABLA, campos, condiciones);
	        
	        //validar en articulos de las bodegas 
	        String[] campoBodegaDTS = { Distribuidor.CAMPO_TCSCBODEGAVIRTUALID };
	        condiciones.clear();
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TCSCCATPAISID, idPais.toString()));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TC_SC_DTS_ID, idDTS));
	        String queryIdBodegaDTS = UtileriasBD.armarQuerySelect(Distribuidor.N_TABLA, campoBodegaDTS , condiciones);
	        campoBodegaDTS[0] = BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID;
	        condiciones.clear();
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, BodegaVirtual.CAMPO_TCSCCATPAISID, idPais.toString()));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_OR, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, queryIdBodegaDTS));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_OR, BodegaVirtual.CAMPO_IDBODEGA_PADRE, queryIdBodegaDTS));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_OR, BodegaVirtual.CAMPO_IDBODEGA_ORIGEN, queryIdBodegaDTS));
	        String queryBodegasVirtualesDTS = UtileriasBD.armarQuerySelect(BodegaVirtual.N_TABLA, campoBodegaDTS , condiciones);
	        
	        condiciones.clear();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_OR, Inventario.CAMPO_TCSCBODEGAVIRTUALID, queryBodegasVirtualesDTS));
            String queryInventario = UtileriasBD.armarQuerySelect(Inventario.N_TABLA, campos, condiciones);

	        String query = "SELECT "
	                + "(" + queryPanel + ") AS PANELES, "
	                + "(" + queryRuta + ") AS RUTAS, "
	                + "(" + queryPDVs + ") AS PDVS, "
	                + "(" + queryInventario + ") AS INVENTARIO "
	                + "FROM DUAL";
	
	        log.trace("Verificando elementos dependientes del DTS: " + query);

            pstmt = conn.prepareStatement(query);
            rst = pstmt.executeQuery();

            if (rst.next()) {
                list.add(0, rst.getBigDecimal(1));
                list.add(1, rst.getBigDecimal(2));
                list.add(2, rst.getBigDecimal(3));
                list.add(3, rst.getBigDecimal(4));
            }

            return list;
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
    }
}

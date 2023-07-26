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

import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaVirtual;
import com.consystec.sc.ca.ws.input.solicitud.RespuestaSolicitud;
import com.consystec.sc.ca.ws.input.vendedordts.InputVendedorDTS;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.vendedordts.OutputVendedorDTS;
import com.consystec.sc.sv.ws.metodos.CtrlVendedorDTS;
import com.consystec.sc.sv.ws.orm.BodegaVendedor;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.VendedorPDV;
import com.consystec.sc.sv.ws.orm.Inventario;
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
public class OperacionVendedorDTS {
	private OperacionVendedorDTS(){}
    private static final Logger log = Logger.getLogger(OperacionVendedorDTS.class);

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @param estadoAlta 
     * @return OutputVendedorDTS
     * @throws SQLException
     */
    public static OutputVendedorDTS doGet(Connection conn, InputVendedorDTS input, int metodo, String estadoAlta, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();
        String tipoPanel = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_PANEL, input.getCodArea());
        String tipoRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_RUTA, input.getCodArea());

        List<InputVendedorDTS> list = new ArrayList<InputVendedorDTS>();

        Respuesta respuesta = null;
        OutputVendedorDTS output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql = null;

        List<Order> orden = new ArrayList<Order>();

        try {
	        orden.add(new Order(VendedorDTS.CAMPO_USUARIO, Order.ASC));
	        
	        String[][] campos = CtrlVendedorDTS.obtenerCamposGet(tipoPanel, estadoAlta, idPais);
	        String tablas[] = {
                ControladorBase.getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, "", input.getCodArea()),
	            Distribuidor.N_TABLA,
	            ControladorBase.getParticion(BodegaVirtual.N_TABLA , Conf.PARTITION, "", input.getCodArea())
	        };
	        
	        List<Filtro> condiciones = CtrlVendedorDTS.obtenerCondiciones(input, metodo, idPais);
	        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.N_TABLA, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, VendedorDTS.N_TABLA + "." + VendedorDTS.CAMPO_TCSCBODEGAVIRTUALID));
	        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.N_TABLA, Distribuidor.CAMPO_TC_SC_DTS_ID, VendedorDTS.N_TABLA, VendedorDTS.CAMPO_TCSCDTSID));
	        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.N_TABLA, Distribuidor.CAMPO_TCSCCATPAISID, VendedorDTS.N_TABLA, VendedorDTS.CAMPO_TCSCCATPAISID));
	        
            sql = UtileriasBD.armarQueryGetMultiple(campos, tablas, condiciones, null);

            if ((input.getSoloDisponibles() != null && !"".equals(input.getSoloDisponibles().trim()))&&"1".equals(input.getSoloDisponibles())) {
                    sql = "SELECT * FROM (" + sql + ") WHERE RUTA IS NULL AND PANEL IS NULL";
            }

            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_HAY_VEND_ASOCIADO_524, null,
                            nombreClase,  nombreMetodo, null, input.getCodArea());

                    output = new OutputVendedorDTS();
                    output.setRespuesta(respuesta);
                } else {
                    respuesta = new Respuesta();
                    respuesta=new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase,  nombreMetodo, null, input.getCodArea());
                            

                    do {
	                    InputVendedorDTS item = new InputVendedorDTS();
	                    item.setIdVendedorDTS(rst.getString(VendedorDTS.CAMPO_TCSCVENDDTSID));
	                    item.setIdDistribuidor(rst.getString(VendedorDTS.CAMPO_TCSCDTSID));
	                    item.setCodVendedor(rst.getString(VendedorDTS.CAMPO_COD_VENDEDOR));
	                    item.setNombreDistribuidor(rst.getString("NOMBREDTS"));
	                    item.setIdBodegaVirtual(rst.getString(VendedorDTS.CAMPO_TCSCBODEGAVIRTUALID));
	                    item.setNombreBodegaVirtual(rst.getString("NOMBREBODEGA"));
	                    item.setIdBodegaVendedor(UtileriasJava.getValue(rst.getString("BODEGAVENDEDOR")));
	                    item.setIdVendedor(rst.getString(VendedorDTS.CAMPO_VENDEDOR));
	                    item.setUsuarioVendedor(rst.getString(VendedorDTS.CAMPO_USUARIO));
	                    item.setNombres(rst.getString(VendedorDTS.CAMPO_NOMBRE));
	                    item.setApellidos(rst.getString(VendedorDTS.CAMPO_APELLIDO));
	                    item.setEmail(rst.getString(VendedorDTS.CAMPO_EMAIL));
	                    item.setEstado(rst.getString(VendedorDTS.CAMPO_ESTADO));
	                    item.setTipoAsociado(rst.getString("RUTA") != null ? tipoRuta
	                            : (rst.getString("PANEL") != null ? tipoPanel : ""));
	                    item.setNombreAsociado(rst.getString("RUTA") != null ? rst.getString("RUTA")
	                            : (rst.getString("PANEL") != null ? rst.getString("PANEL") : ""));
	                    item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(VendedorDTS.CAMPO_CREADO_EL)));
	                    item.setCreado_por(rst.getString(VendedorDTS.CAMPO_CREADO_POR));
	                    //agregado sbarrios 15-07-16
	                    item.setNumeroRecarga(rst.getString(VendedorDTS.CAMPO_NUM_RECARGA));
	                    item.setPin(rst.getString(VendedorDTS.CAMPO_PIN));
	                    item.setCanal(rst.getString(VendedorDTS.CAMPO_CANAL));
	                    item.setSubcanal(rst.getString(VendedorDTS.CAMPO_SUBCANAL));
	                    //item.setEnvioCodVend(rst.getString(VendedorDTS.CAMPO_ENVIO_COD_VEND));
                        list.add(item);

                    } while (rst.next());

                    output = new OutputVendedorDTS();
                    output.setRespuesta(respuesta);
                    output.setVendedores(list);
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
     * @return OutputVendedorDTS
     * @throws SQLException
     */
    public static OutputVendedorDTS doPost(Connection conn, InputVendedorDTS input, String estadoAlta, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "doPost";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputVendedorDTS output = new OutputVendedorDTS();
        List<String> existeNumRecarga = new ArrayList<String>();

      

        List<Filtro> condicionesExistencia = CtrlVendedorDTS.obtenerCondicionesExistencia(input, Conf.METODO_POST,
                estadoAlta, idPais);

        int existencia = UtileriasBD.selectCount(conn, VendedorDTS.N_TABLA, condicionesExistencia);
        if (existencia > 0) {
            log.error("Ya existe el recurso.");
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_VENDEDOR_REGISTRADO_160, null, nombreClase,
                     nombreMetodo, null, input.getCodArea());

            output.setRespuesta(respuesta);
            return output;
        }

        existeNumRecarga = existeNumRecarga(conn, estadoAlta, input.getNumeroRecarga(), "PDV", null, idPais, input.getCodArea());

        if (!(existeNumRecarga == null || existeNumRecarga.isEmpty())) {
            log.error("Ya existe el n\u00FAmero de recarga ingresado.");
            String razon = "";

            if (existeNumRecarga.get(1).equals("1")) {
                razon = "Punto de venta: " + existeNumRecarga.get(0);
            } else {
                razon = "Vendedor: " + existeNumRecarga.get(0);
            }

            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NUMRECARGA_EXISTE_724, null,
                    nombreClase, nombreMetodo, razon, input.getCodArea());

            output.setRespuesta(respuesta);
            return output;
        }

        String sql = null;
        String campos[] = CtrlVendedorDTS.obtenerCamposPost();
        List<String> inserts = CtrlVendedorDTS.obtenerInsertsPost(input, VendedorDTS.SEQUENCE, estadoAlta, idPais);
        
        sql = UtileriasBD.armarQueryInsert(VendedorDTS.N_TABLA, campos, inserts);
        BigDecimal idVendedorDTS = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            String generatedColumns[] = { VendedorDTS.CAMPO_TCSCVENDDTSID };
            pstmt = conn.prepareStatement(sql, generatedColumns);

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();

            if (rs != null) {
                if (rs.next()) {
                    idVendedorDTS = rs.getBigDecimal(1);
                }
                log.debug("idVendedorDTS: " + idVendedorDTS);

                if (idVendedorDTS != null) {
                    // Existencia de bodega vendedor
                    condicionesExistencia.clear();
                    condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVendedor.CAMPO_TCSCCATPAISID, idPais.toString()));
                    condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVendedor.CAMPO_VENDEDOR, input.getIdVendedor()));
                    existencia = UtileriasBD.selectCount(conn, BodegaVendedor.N_TABLA, condicionesExistencia);
                    if (existencia < 1) {
                        // creando bodega virtual para vendedor
                        List<InputBodegaVirtual> bod = new ArrayList<InputBodegaVirtual>();

                        InputBodegaVirtual objBod = new InputBodegaVirtual();
                        objBod.setCodArea(input.getCodArea());
                        objBod.setNivel("3");
                        objBod.setNombre("BODEGA VENDEDOR: " + input.getNombres() + " " + input.getApellidos());
                        objBod.setIdDTS(input.getIdVendedor()); //Se setea aqui el idVendedor para reutilizar la clase
                        objBod.setUsuario(input.getUsuario());
                        bod.add(objBod);

                        OperacionesBodegaVend.creaBodVirtualVendedor(conn, bod, estadoAlta, idPais);
                    }
                }

                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_VEND_DTS_61, null, nombreClase,
                         nombreMetodo, null, input.getCodArea());

                output.setRespuesta(respuesta);

                conn.commit();
            } else {
                log.debug("Rollback");
                conn.rollback();
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
                         nombreMetodo, null, input.getCodArea());

                output.setRespuesta(respuesta);
            }

        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(pstmt);
            conn.setAutoCommit(true);
            
        }

        return output;
    }

    /**
     * Funci\u00F3n que arma el query a utilizar al modificar datos en m\u00E9todos PUT y DELETE.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @param estadoAlta 
     * @return OutputVendedorDTS
     * @throws SQLException
     */
    public static OutputVendedorDTS doPutDel(Connection conn, InputVendedorDTS input, int metodo, String estadoAlta, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "doPutDel";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputVendedorDTS output = null;
        String sql = null;
        BigDecimal cambioBodega = null;
        List<Filtro> condicionesExistencia = CtrlVendedorDTS.obtenerCondicionesExistencia(input, metodo, estadoAlta, idPais);

        try {
            conn.setAutoCommit(false);

            // se valida que exista el recurso y se obtiene idVendedor
            String idVendedor = UtileriasBD.getOneRecord(conn, VendedorDTS.CAMPO_VENDEDOR, ControladorBase.getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, input.getIdDistribuidor(), input.getCodArea()), condicionesExistencia);
            if (idVendedor == null || idVendedor.equals("")) {
                log.error("No existe el recurso.");
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
                         nombreMetodo, null, input.getCodArea());

                output = new OutputVendedorDTS();
                output.setRespuesta(respuesta);

                return output;
            } else {
                if (metodo == Conf.METODO_PUT) {
                    // se verifica si cambio de bodega o distribuidor
                    cambioBodega = cambioDTSBOD(conn,  input, idPais);
                }

                if (metodo == Conf.METODO_DELETE || (metodo == Conf.METODO_PUT
                        && (cambioBodega==null || !(input.getEstado().equalsIgnoreCase(estadoAlta))))) {
                    // se verifican los elementos relacionados al vendedor asociado para poder dar de baja o eliminar.
                    List<BigDecimal> lista = validarBajaVendedor(conn, input.getIdVendedorDTS(), estadoAlta, input.getIdDistribuidor(), input.getCodArea(),idPais);

                    if (!lista.isEmpty() && lista.get(0).intValue() > 0) {
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_VEND_TIENE_PANEL_175, null,
                                nombreClase,  nombreMetodo, null, input.getCodArea());
                        output = new OutputVendedorDTS();
                        output.setRespuesta(respuesta);
                        return output;
                    } else if (!lista.isEmpty() && lista.get(1).intValue() > 0) {
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_VEND_TIENE_RUTA_176, null,
                                nombreClase,  nombreMetodo, null, input.getCodArea());
                        output = new OutputVendedorDTS();
                        output.setRespuesta(respuesta);
                        return output;
                
                    } else if (!lista.isEmpty() && lista.get(2).intValue() > 0) {
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_VEND_TIENE_INV_178, null,
                                nombreClase,  nombreMetodo, null, input.getCodArea());
                        output = new OutputVendedorDTS();
                        output.setRespuesta(respuesta);
                        return output;
                    }
                }
            }

            if (metodo == Conf.METODO_DELETE) {
                List<Filtro> condiciones = CtrlVendedorDTS.obtenerCondiciones(input, metodo, idPais);
                sql = UtileriasBD.armarQueryDelete(ControladorBase.getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, input.getIdDistribuidor(), input.getCodArea()), condiciones);

                QueryRunner Qr = new QueryRunner();
                if (Qr.update(conn, sql) == 1) {

                    // si se elimina el vendedor, se elimina la bodega vendedor
                    List<VendedorPDV> lista = new ArrayList<VendedorPDV>();
                    VendedorPDV objVend = new VendedorPDV();
                    objVend.setVendedor(new BigDecimal(idVendedor));
                    lista.add(objVend);

                    OperacionesBodegaVend.deleteBodVendedor(conn, lista);

                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_DEL_VEND_DTS_63, null, nombreClase,
                             nombreMetodo, null, input.getCodArea());

                    output = new OutputVendedorDTS();
                    output.setRespuesta(respuesta);

                    conn.commit();
                }

            } else if (metodo == Conf.METODO_PUT) {
       

                List<String> existeNumRecarga = new ArrayList<String>();

                existeNumRecarga = existeNumRecarga(conn, estadoAlta, input.getNumeroRecarga(), "PDV",
                        input.getIdVendedorDTS(),idPais, input.getCodArea() );
                if (!(existeNumRecarga == null || existeNumRecarga.isEmpty())) {
                    log.error("Ya existe el n\u00FAmero de recarga ingresado.");
                    String razon = "";

                    if (existeNumRecarga.get(1).equals("1")) {
                        razon = "Punto de venta: " + existeNumRecarga.get(0);
                    } else {
                        razon = "Vendedor: " + existeNumRecarga.get(0);
                    }

                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NUMRECARGA_EXISTE_724, null,
                            nombreClase, nombreMetodo, razon, input.getCodArea());

                    output = new OutputVendedorDTS();
                    output.setRespuesta(respuesta);

                    return output;
                } else {
                    BigDecimal res = new BigDecimal(0);
                    res = updateVendDTS(conn, input, idPais);

                    if (res.intValue() == 1) {
                        // si se modifica correctamente se actualiza la bodega vendedor al estado enviado
                        boolean update = OperacionesBodegaVend.updateBodVendedor(conn, idVendedor, input.getEstado(),
                                input.getUsuario());
                        if (!update) {
                            conn.rollback();
                            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null,
                                    nombreClase,  nombreMetodo, null, input.getCodArea());
                        } else {
                            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_VEND_DTS_62, null,
                                    nombreClase,  nombreMetodo, null, input.getCodArea());
                        }

                        output = new OutputVendedorDTS();
                        output.setRespuesta(respuesta);
                    }
                }
            }

        } finally {
            conn.setAutoCommit(true);
            DbUtils.closeQuietly(conn);
        }

        return output;
    }

    /**
     * Funcion para validar si el vendedor se encuentra asociado a una panel,
     * ruta, punto de venta o si tiene inventario asignado.
     * 
     * @param conn
     * @param idVendedorDTS
     * @param estadoAlta
     * @param idDistribuidor
     * @return
     * @throws SQLException
     */
    private static List<BigDecimal> validarBajaVendedor(Connection conn, String idVendedorDTS, String estadoAlta, String idDistribuidor, String codArea, BigDecimal idPais) throws SQLException {
        String tipoPanel = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELPDV, Conf.TIPO_PANEL,codArea);

        List<Filtro> condiciones = new ArrayList<Filtro>();
        String[] campos = { VendedorDTS.CAMPO_VENDEDOR };

        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCVENDDTSID, idVendedorDTS));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCCATPAISID,""+ idPais));
        String idVendedor = UtileriasBD.armarQuerySelect(ControladorBase.getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, idDistribuidor,codArea), campos, condiciones);

        campos[0] = "COUNT(*)";
        //validar en panel
        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, VendedorPDV.CAMPO_VENDEDOR, idVendedor));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorPDV.CAMPO_TCSCCATPAISID,""+ idPais));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_TIPO, tipoPanel));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_ESTADO, estadoAlta));
        String queryPanel = UtileriasBD.armarQuerySelect(VendedorPDV.N_TABLA, campos, condiciones);

      
        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Ruta.CAMPO_SEC_USUARIO_ID, idVendedor));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Ruta.CAMPO_ESTADO, estadoAlta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TCSCCATPAISID,""+ idPais));
        String queryRuta = UtileriasBD.armarQuerySelect(Ruta.N_TABLA, campos, condiciones);

        //validar en articulos de las bodegas
        String[] campoBodegaVendedor = { BodegaVendedor.CAMPO_TCSCBODEGAVIRTUALID };
        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, BodegaVendedor.CAMPO_VENDEDOR, idVendedor));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVendedor.CAMPO_TCSCCATPAISID,""+ idPais));
        String queryIdBodegaVendedor = UtileriasBD.armarQuerySelect(BodegaVendedor.N_TABLA, campoBodegaVendedor , condiciones);

        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, queryIdBodegaVendedor));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID,""+ idPais));

        String queryInventario = UtileriasBD.armarQuerySelect(ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, "",codArea), campos , condiciones);

        List<BigDecimal> list = new ArrayList<BigDecimal>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;

    
            String query = "SELECT "
	                + "(" + queryPanel + ") AS PANELES, "
	                + "(" + queryRuta + ") AS RUTAS, "
	                + "(" + queryInventario + ") AS INVENTARIO "
	                + "FROM DUAL";

            log.trace("Verificando elementos dependientes del Vendedor: " + query);
        try {
            pstmt = conn.prepareStatement(query);
            rst = pstmt.executeQuery();

            if (rst.next()) {
                list.add(0, rst.getBigDecimal(1));
                list.add(1, rst.getBigDecimal(2));
                list.add(2, rst.getBigDecimal(3));
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }

        return list;
    }

    public static List<String> existeNumRecarga(Connection conn, String estadoAlta, String numRecarga, String tipoPDV,
            String id, BigDecimal idPais, String codArea) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        List<String> lista = new ArrayList<String>();
        StringBuilder query = new StringBuilder();
     
	        query.append( "SELECT V.NOMBRE  recurso, 1 tipo" );
	        		query.append( 	" FROM " + ControladorBase.getParticion(PuntoVenta.N_TABLA, Conf.PARTITION, "", codArea) + " V, TC_SC_NUMRECARGA N " );
	        		query.append(    " WHERE V.TCSCCATPAISID = " + idPais );
	        		query.append(  " AND V.TCSCPUNTOVENTAID = N.IDTIPO " );
	        		query.append( " AND TIPO = '" + tipoPDV + "'" );
	        		query.append( " AND NUM_RECARGA = " + numRecarga );
	        		query.append(  " AND N.ESTADO = 'ALTA'" );
	        		query.append( " UNION " );
	        		query.append( "SELECT USUARIO recurso, 0 tipo" );
	        		query.append(  "  FROM " + ControladorBase.getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, "",codArea) );
	        		query.append( " WHERE TCSCCATPAISID = " + idPais + " AND ESTADO = '" + estadoAlta + "' AND NUM_RECARGA = " + numRecarga);
            if (id != null) {
                query.append(" AND TCSCVENDDTSID NOT IN (" + id + ")");
            }

            log.trace("verificando existencia numRecarga: " + query.toString());
         try {
            pstmt = conn.prepareStatement(query.toString());
            rst = pstmt.executeQuery();

            if (rst.next()) {
                lista.add(0, rst.getString("recurso"));
                lista.add(1, rst.getString("tipo"));
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }

        return lista;
    }

    /**
     * M\u00E9todo para verificar si un vendedor a modificar cambiara de bodega o distribuidor
     * @param conn
     * @param estadoAlta
     * @param input
     * @return
     * @throws SQLException
     */
    public static BigDecimal cambioDTSBOD(Connection conn, InputVendedorDTS input, BigDecimal idPais)
            throws SQLException {
        BigDecimal respuesta = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        StringBuilder query = new StringBuilder(); 
        
	        query.append("SELECT COUNT (1)");
	        query.append(" FROM " + ControladorBase.getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, "", input.getCodArea()));
	        query.append( " WHERE TCSCDTSID = ?" );
	        query.append( " AND TCSCBODEGAVIRTUALID =?" );
	        query.append(" AND TCSCVENDDTSID = ?" );
	        query.append(" AND TCSCCATPAISID = ?");
	    try {
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setBigDecimal(1, new BigDecimal( input.getIdDistribuidor()));
            pstmt.setBigDecimal(2, new BigDecimal(input.getIdBodegaVirtual()));
            pstmt.setBigDecimal(3,  new BigDecimal(input.getIdVendedorDTS()));
            pstmt.setBigDecimal(4,idPais);
            rst = pstmt.executeQuery();

            if (rst.next()) {
                respuesta = rst.getBigDecimal(1);
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }
        return respuesta;
    }

    public static BigDecimal updateVendDTS(Connection conn, InputVendedorDTS objeto, BigDecimal idPais) throws SQLException {
        BigDecimal ret = null;
        PreparedStatement pstmt = null;

        
	        String update = "UPDATE TC_SC_VEND_DTS " +
	        		"   SET tcscdtsid = ?, " +
	        		"       tcscbodegavirtualid = ?, " +
	        		"       nombre = ?, " +
	        		"       apellido = ?, " +
	        		"       email = ?, " +
	        		"       canal = ?, " +
	        		"       subcanal = ?, " +
	        		"       num_recarga = ?, " +
	        		"       pin = ?, " +
	        		"       dts_fuente = ?, " +
	        		"       cod_oficina = ?, " +
	        		"       cod_vendedor = ?, " +
	        		"       estado = ?, " +
	        		"       modificado_el = SYSDATE, " +
	        		"       modificado_por = ?, " +
	        		"       ENVIO_COD_VEND = ? " +
	        		" WHERE tcscvenddtsid = ? AND tcsccatpaisid = ? " ;
	
	        log.trace("update buzon:" + update);
	        log.trace("nombre :" + objeto.getNombres());
	        log.trace("id venddts:" + objeto.getIdVendedorDTS());
	        log.trace("estado venddts:" + objeto.getEstado());
	        log.trace("id distribuidor:" + objeto.getIdDistribuidor());
	      try {
	        pstmt = conn.prepareStatement(update);
	
	        pstmt.setBigDecimal(1, new BigDecimal(objeto.getIdDistribuidor()));
	        pstmt.setBigDecimal(2, new BigDecimal(objeto.getIdBodegaVirtual()));
	        pstmt.setString(3, objeto.getNombres());
	        pstmt.setString(4, objeto.getApellidos());
	        pstmt.setString(5, objeto.getEmail());
	        pstmt.setString(6, objeto.getCanal());
	        pstmt.setString(7, objeto.getSubcanal());
	        pstmt.setBigDecimal(8, new BigDecimal(objeto.getNumeroRecarga()));
	        pstmt.setBigDecimal(9, new BigDecimal(objeto.getPin()));
	        pstmt.setString(10, null);//objeto.getDtsFuente()
	        pstmt.setString(11, objeto.getCodOficina());
	        pstmt.setString(12, objeto.getCodVendedor());
	        pstmt.setString(13, objeto.getEstado().toUpperCase());
	        pstmt.setString(14, objeto.getUsuario());
	        pstmt.setString(15, objeto.getEnvioCodVend());
	        pstmt.setBigDecimal(16, new BigDecimal(objeto.getIdVendedorDTS()));
	        pstmt.setBigDecimal(17, idPais);

            int res = pstmt.executeUpdate();
            if (res != 1) {
                ret = new BigDecimal(0);
            } else {
                ret = new BigDecimal(res);
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
        return ret;
    }

    public static RespuestaSolicitud validarVendSCL(Connection conn,  String idDistribuidor,
            String estadoAlta, String nombreMetodo, String nombreClase, String codArea) throws SQLException {
    	/*al  usar este método buscar la forma de obtener codVendedor*/
        List<String> respuestaValidacion = validaVendedorSCL(conn,  idDistribuidor, "codVendedor", estadoAlta);
        Respuesta respuesta = null;
        RespuestaSolicitud output = new RespuestaSolicitud();
        output.setResultado(false);
        output.setDescripcion("");

        if (respuestaValidacion.get(0) == null || respuestaValidacion.get(0).equals("")) {
            // no tiene codigo de oficina, no existe vendedor
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_CODVENDEDOR_SCL_852, null, nombreClase,
                     nombreMetodo, null, codArea);
            output.setRespuesta(respuesta);
            return output;
        } else if (!respuestaValidacion.get(1).equals("1")) {
            // no coincide el codigo de bodega con la bodega asociada al dts
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_BODEGASCL_DTS_853, null, nombreClase,
                     nombreMetodo, null,codArea);
            output.setRespuesta(respuesta);
            return output;
        } else if (new Integer(respuestaValidacion.get(1)) > 1) {
            // el codigo de vendedor tiene mas de una bodega en scl
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_BODEGASCL_NO_UNICA_854, null,
                    nombreClase,  nombreMetodo, null, codArea);
            output.setRespuesta(respuesta);
            return output;
        } else if (new Integer(respuestaValidacion.get(2)) > 0) {
            // el codigo de vendedor ya esta asignado a otro vendedor de sidra
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_CODVENDEDOR_SIDRA_862, null,
                    nombreClase,  nombreMetodo, null, codArea);
            output.setRespuesta(respuesta);
            return output;
        } else {
            output.setResultado(true);
            output.setDescripcion(respuestaValidacion.get(0));
        }
        return output;
    }

    public static List<String> validaVendedorSCL(Connection conn, String idDTS,String codVendedor, String estadoAlta)
            throws SQLException {
        List<String> respuesta = new ArrayList<String>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        StringBuilder query = new StringBuilder();
       
	        query.append( "SELECT (SELECT COD_OFICINA FROM VE_VENDEDORES" + Conf.DBLINK_SCL);
	        query.append( " WHERE VE_INDBLOQUEO = 0 AND FEC_FINCONTRATO IS NULL ");
	        query.append( "AND COD_ESTADO = 0 AND COD_VENDEDOR = " + codVendedor + ") AS COD_OFICINA, ");
	        query.append( "(SELECT COUNT(1) FROM TC_SC_ALMACEN_BOD WHERE TCSCDTSID = " + idDTS);
	        query.append( " AND TCBODEGASCLID IN (SELECT COD_BODEGA FROM VE_VENDALMAC" + Conf.DBLINK_SCL);
	        query.append( " WHERE COD_VENDEDOR = " + codVendedor + " AND FEC_DESASIGNAC IS NULL)) AS COD_BODEGA,");
	        query.append( " (SELECT COUNT(1) FROM TC_SC_VEND_DTS WHERE UPPER(ESTADO) = UPPER('" + estadoAlta + "')");
	        query.append(" AND COD_VENDEDOR = " + codVendedor + ") VEND");
	        query.append(" FROM DUAL");

            log.trace("Qry validar vendedor en SCL: " + query.toString());
         try {
            pstmt = conn.prepareStatement(query.toString());
            rst = pstmt.executeQuery();

            if (rst.next()) {
                respuesta.add(rst.getString(1));
                respuesta.add(rst.getString(2));
                respuesta.add(rst.getString(3));
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }

        return respuesta;
    }
}

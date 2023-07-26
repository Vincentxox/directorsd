package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.condicion.InputCondicion;
import com.consystec.sc.ca.ws.input.condicion.InputCondicionPrincipal;
import com.consystec.sc.ca.ws.input.condicion.InputDetCondicionCampania;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.condicion.OutputCondicion;
import com.consystec.sc.sv.ws.metodos.CtrlCondicion;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.Condicion;
import com.consystec.sc.sv.ws.orm.CondicionCampania;
import com.consystec.sc.sv.ws.orm.OfertaCampania;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes - Consystec - 2016
 *
 */
public class OperacionCondicion {
	private OperacionCondicion(){}
	
    private static final Logger log = Logger.getLogger(OperacionCondicion.class);

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputCondicion
     * @throws SQLException
     */
    public static OutputCondicion doGet(Connection conn, InputCondicionPrincipal input, int metodo, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = null;
        OutputCondicion output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        try {
            String campania = UtileriasJava.getConfig(conn, Conf.GRUPO_OFERTA_COMERCIAL, Conf.TIPO_CAMPANIA, input.getCodArea());
            String invTelca = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPOINV, Conf.SOL_TIPOINV_TELCA, input.getCodArea());

            List<InputCondicion> list = new ArrayList<InputCondicion>();

            String tablas[] = { Condicion.N_TABLA, ControladorBase.getParticion(OfertaCampania.N_TABLA, Conf.PARTITION, "", input.getCodArea()) };
            String[][] campos = CtrlCondicion.obtenerCamposGet();

            List<Filtro> condiciones = CtrlCondicion.obtenerCondiciones( input, metodo, idPais);
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.N_TABLA,
                    OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, Condicion.N_TABLA, Condicion.CAMPO_TCSCOFERTACAMPANIAID));
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Condicion.N_TABLA,
                    Condicion.CAMPO_TIPO_OFERTACAMPANIA, campania));

            List<Order> orden = new ArrayList<Order>();
            orden.add(new Order(Condicion.N_TABLA + "." + Condicion.CAMPO_NOMBRE, Order.ASC));

            String sql = UtileriasBD.armarQueryGetMultiple(campos, tablas, condiciones, orden);
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_CONDICIONES_391, null,
                            nombreClase, nombreMetodo, null,  input.getCodArea());

                    output = new OutputCondicion();
                    output.setRespuesta(respuesta);
                } else {
                    respuesta = new Respuesta();
                    respuesta=new ControladorBase()
                            .getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, input.getCodArea());

                    String idCondicion = "";
                    do {
                        InputCondicion item = new InputCondicion();
                        idCondicion = rst.getString(Condicion.CAMPO_TCSCCONDICIONID);
                        item.setIdCondicion(idCondicion);
                        item.setTipoGestion(rst.getString(Condicion.CAMPO_TIPO_GESTION));
                        item.setTipoCondicion(rst.getString(Condicion.CAMPO_TIPO_CONDICION));
                        item.setNombre(rst.getString(Condicion.CAMPO_NOMBRE));
                        item.setIdOfertaCampania(rst.getString(Condicion.CAMPO_TCSCOFERTACAMPANIAID));
                        item.setNombreCampania(rst.getString("NOMBRECAMPANIA"));
                        item.setEstado(rst.getString(Condicion.CAMPO_ESTADO));
                        item.setCreadoEl(UtileriasJava.formatStringDate(rst.getString(Condicion.CAMPO_CREADO_EL)));
                        item.setCreadoPor(rst.getString(Condicion.CAMPO_CREADO_POR));
                        item.setModificadoEl(UtileriasJava.formatStringDate(rst.getString(Condicion.CAMPO_MODIFICADO_EL)));
                        item.setModificadoPor(rst.getString(Condicion.CAMPO_MODIFICADO_POR));

                        List<InputDetCondicionCampania> detalles = getDatosTablaHija(conn, idCondicion, 
                                invTelca, idPais);
                        item.setDetalle(detalles);

                        list.add(item);
                    } while (rst.next());

                    output = new OutputCondicion();
                    output.setRespuesta(respuesta);
                    output.setCondiciones(list);
                }
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return output;
    }

    /**
     * Funci\u00F3n que obtiene los datos relacionados de la tabla hija mediante el
     * id de la tabla padre.
     * 
     * @param conn
     * @param idPadre
     * @param input
     * @param invTelca
     * @return OutputBodegaDTS
     * @throws SQLException
     */
    private static List<InputDetCondicionCampania> getDatosTablaHija(Connection conn, String idPadre, String invTelca, BigDecimal idPais) throws SQLException {
        List<InputDetCondicionCampania> list = new ArrayList<InputDetCondicionCampania>();
        PreparedStatement pstmtIn = null;
        ResultSet rstIn = null;

        String idArticulo = "";
        String tipoInv = "";
        String nombreArticulo = "";

        try {
            String camposInterno[] = CtrlCondicion.obtenerCamposTablaHijaGet(idPais);

            List<Filtro> condicionesInterno = new ArrayList<Filtro>();
            condicionesInterno.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CondicionCampania.CAMPO_TCSCCONDICIONID, idPadre));

            String sql = UtileriasBD.armarQuerySelect(CondicionCampania.N_TABLA + " B", camposInterno, condicionesInterno, null);
            pstmtIn = conn.prepareStatement(sql);
            rstIn = pstmtIn.executeQuery();

            if (rstIn != null) {
                if (!rstIn.next()) {
                    log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
                    InputDetCondicionCampania item = new InputDetCondicionCampania();
                    list.add(item);
                } else {
                    do {
                        if (rstIn.getString(CondicionCampania.CAMPO_ARTICULO) != null) {
                            idArticulo = rstIn.getString(CondicionCampania.CAMPO_ARTICULO);
                            tipoInv = invTelca;
                            nombreArticulo = rstIn.getString("NOMBREART") != null ? rstIn.getString("NOMBREART") : "";
                        } else {
                            idArticulo = "";
                            tipoInv = "";
                            nombreArticulo = "";
                        }

                        InputDetCondicionCampania item = new InputDetCondicionCampania();
                        item.setIdCondicion(rstIn.getString(CondicionCampania.CAMPO_TCSCCONDICIONID));
                        item.setTipo(UtileriasJava.getValue(rstIn.getString(CondicionCampania.CAMPO_TIPO)));
                        item.setIdArticulo(idArticulo);
                        item.setNombreArticulo(nombreArticulo);
                        item.setTecnologia(UtileriasJava.getValue(rstIn.getString(CondicionCampania.CAMPO_TECNOLOGIA)));
                        item.setTipoInv(tipoInv);
                        item.setMontoInicial(UtileriasJava.getValue(rstIn.getString(CondicionCampania.CAMPO_MONTO_INICIAL)));
                        item.setMontoFinal(UtileriasJava.getValue(rstIn.getString(CondicionCampania.CAMPO_MONTO_FINAL)));
                        item.setEstado(rstIn.getString(CondicionCampania.CAMPO_ESTADO));
                        item.setCreadoEl(rstIn.getString(CondicionCampania.CAMPO_CREADO_POR));
                        item.setCreadoEl(UtileriasJava.formatStringDate(rstIn.getString(CondicionCampania.CAMPO_CREADO_EL)));
                        item.setModificadoEl(rstIn.getString(CondicionCampania.CAMPO_MODIFICADO_POR));
                        item.setModificadoEl(UtileriasJava.formatStringDate(rstIn.getString(CondicionCampania.CAMPO_MODIFICADO_EL)));

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
     * Funci\u00F3n que arma el query a utilizar al trabajar en m\u00E9todo POST.
     * 
     * @param conn
     * @param input
     * @param estadoAlta 
     * @return OutputCondicion
     * @throws SQLException
     */
    public static OutputCondicion doPost(Connection conn, InputCondicionPrincipal input, String estadoAlta, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "doPost";
        String nombreClase = new CurrentClassGetter().getClassName();
        String campania = UtileriasJava.getConfig(conn, Conf.GRUPO_OFERTA_COMERCIAL, Conf.TIPO_CAMPANIA, input.getCodArea());

        Respuesta respuesta = null;
        OutputCondicion output = new OutputCondicion();
        int idPadre = 0;
        boolean insertHijo = false;
        boolean insertAll = false;

        try {
            conn.setAutoCommit(false);

            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
                    OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));

            String existencia = UtileriasBD.verificarExistencia(conn, ControladorBase.getParticion(OfertaCampania.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);
            if (new Integer(existencia) < 1) {
                log.error("No existe la campania.");
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_CAMPANIA_242, null,
                        nombreClase, nombreMetodo, null, input.getCodArea());

                output = new OutputCondicion();
                output.setRespuesta(respuesta);

                return output;
            }

            String generatedColumns[] = { Condicion.CAMPO_TCSCCONDICIONID };

     
            String campos[] = CtrlCondicion.obtenerCamposPost();

            List<String> inserts = new ArrayList<String>();

            for (int i = 0; i < input.getCondiciones().size(); i++) {
                // Se verifica si el idOfertaCampania pertenece a campania y esta de alta
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.CAMPO_TCSCCATPAISID,idPais.toString()));
                String camposCampania[] = { OfertaCampania.CAMPO_TIPO, OfertaCampania.CAMPO_ESTADO };

                List<Map<String, String>> datosCampania = UtileriasBD.getSingleData(conn,
                        ControladorBase.getParticion(OfertaCampania.N_TABLA, Conf.PARTITION, "", input.getCodArea()), camposCampania,
                        condiciones, null);
                String tipoOfertaCampania = datosCampania.get(0).get(OfertaCampania.CAMPO_TIPO);
                if (!tipoOfertaCampania.equalsIgnoreCase(campania)) {
                    // no es tipo campania
                    log.error("La oferta no corresponde a campania.");
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAM_TIPO_248,
                            null, nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputCondicion();
                    output.setRespuesta(respuesta);

                    return output;

                } else if (!datosCampania.get(0).get(OfertaCampania.CAMPO_ESTADO).equalsIgnoreCase(estadoAlta)) {
                    // es tipo campania
                    log.error("La oferta corresponde a campania pero no esta de alta.");
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_OFERTACAMPANIA_BAJA_249, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputCondicion();
                    output.setRespuesta(respuesta);

                    return output;
                }

                // Se verifica si ya existe una condicion con ese nombre
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Condicion.CAMPO_NOMBRE, input.getCondiciones().get(i).getNombre()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Condicion.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Condicion.CAMPO_ESTADO, estadoAlta));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Condicion.CAMPO_TCSCCATPAISID,idPais.toString()));

                existencia = UtileriasBD.verificarExistencia(conn, Condicion.N_TABLA, condiciones);
                if (new Integer(existencia) > 0) {
                    log.error("Ya existe el nombre de condicion.");
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_EXISTE_CONDICION_412, null,
                            nombreClase, nombreMetodo, "En la condici\u00F3n " + (i + 1) + ".", input.getCodArea());

                    output = new OutputCondicion();
                    output.setRespuesta(respuesta);

                    return output;
                }

                // Se verifica si el tipo gestion es v\u00E9lido
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.GRUPO_TIPO_GESTION_VENTA));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_NOMBRE, input.getCondiciones().get(i).getTipoGestion()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));

                existencia = UtileriasBD.verificarExistencia(conn, Catalogo.N_TABLA, condiciones);
                if (new Integer(existencia) < 1) {
                    log.error("No existe el tipo de gesti\u00F3n.");
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TIPO_GESTION_NO_DEFINIDA_415, null,
                            nombreClase, nombreMetodo, "En la condici\u00F3n " + (i + 1) + ".", input.getCodArea());

                    output = new OutputCondicion();
                    output.setRespuesta(respuesta);
                    
                    return output;
                }

                // Se verifica si el tipo de condici\u00F3n es v\u00E9lido
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.GRUPO_CONDICION_TIPO));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE, input.getCondiciones().get(i).getTipoCondicion()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));

                existencia = UtileriasBD.verificarExistencia(conn, Catalogo.N_TABLA, condiciones);
                if (new Integer(existencia) < 1) {
                    log.error("No existe el tipo de condicion.");
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TIPO_CONDICION_NO_DEFINIDA_416, null,
                            nombreClase, nombreMetodo, "En la condici\u00F3n " + (i + 1) + ".", input.getCodArea());

                    output = new OutputCondicion();
                    output.setRespuesta(respuesta);

                    return output;
                }

                // Insert header
                inserts.clear();
                String valores = " ("
                        + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, Condicion.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais.toString(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdOfertaCampania(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getCondiciones().get(i).getNombre(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getCondiciones().get(i).getTipoGestion(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getCondiciones().get(i).getTipoCondicion(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, tipoOfertaCampania, Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
                    + ")";
                inserts.add(valores);

              
                idPadre=insertCondicion(conn, generatedColumns, campos, inserts); 	
                    log.debug("idPadre: " + idPadre);
                    if (idPadre !=0) {
                    // Inserts detail
                    insertHijo = doPostHijo(conn, idPadre, input.getCondiciones().get(i), estadoAlta,
                            input.getUsuario(), idPais);

                    if (insertHijo) {
                        insertAll = true;
                    } else {
                        insertAll = false;
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null,
                                nombreClase, nombreMetodo, "Problema al insertar un detalle.", input.getCodArea());

                        output.setRespuesta(respuesta);

                        return output;
                    }
                }
            }
          

            if (insertAll ) {
                conn.commit();
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_CONDICION_35, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output.setRespuesta(respuesta);
                output.setIdCondicion(idPadre + "");
            } else {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output.setRespuesta(respuesta);
            }
        } finally {
            if (!insertAll) {
                log.error("Rollback");
                conn.rollback();
            }

            conn.setAutoCommit(true);
          
           
        }

        return output;
    }
    
    public static int insertCondicion(Connection conn, String generatedColumns[],String campos[],List<String> inserts ) throws SQLException{
    	   int idPadre = 0;
           PreparedStatement pstmt = null;
           ResultSet rs = null;
           String sql="";
        try{
        	sql = UtileriasBD.armarQueryInsert(Condicion.N_TABLA, campos, inserts);
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
        return idPadre;
    } 

    /**
     * Funci\u00F3n de realiza las inserciones en la tabla relacionada.
     * 
     * @param conn
     * @param idPadre
     * @param inputCondicion
     * @param estadoAlta
     * @param usuario
     * @return boolean
     * @throws SQLException
     */
    private static boolean doPostHijo(Connection conn, int idPadre, InputCondicion inputCondicion, String estadoAlta,
            String usuario, BigDecimal idPais) throws SQLException {
        String campos[] = CtrlCondicion.obtenerCamposTablaHijaPost();
        List<String> inserts = new ArrayList<String>();
        QueryRunner Qr = new QueryRunner();
        String sql = null;

        for (int j = 0; j < inputCondicion.getDetalle().size(); j++) {
            String valores = null;
            String tipo = null;
            String idArticulo = null;
            String montoInicial = null;
            String montoFinal = null;
            String tecnologia = "";

            tipo = inputCondicion.getDetalle().get(j).getTipo();
            if (tipo == null || tipo.equals("")) {
                tipo = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
            } else {
                tipo = UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, inputCondicion.getDetalle().get(j).getTipo(),
                        Conf.INSERT_SEPARADOR_SI);
            }

            idArticulo = inputCondicion.getDetalle().get(j).getIdArticulo();
            if (idArticulo == null || idArticulo.equals("")) {
                idArticulo = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
            } else {
                idArticulo = UtileriasJava.setInsert(Conf.INSERT_NUMERO,
                        inputCondicion.getDetalle().get(j).getIdArticulo(), Conf.INSERT_SEPARADOR_SI);
            }

            montoInicial = inputCondicion.getDetalle().get(j).getMontoInicial();
            if (montoInicial == null || montoInicial.equals("")) {
                montoInicial = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
            } else {
                montoInicial = UtileriasJava.setInsert(Conf.INSERT_NUMERO,
                        inputCondicion.getDetalle().get(j).getMontoInicial(), Conf.INSERT_SEPARADOR_SI);
            }

            montoFinal = inputCondicion.getDetalle().get(j).getMontoFinal();
            if (montoFinal == null || montoFinal.equals("")) {
                montoFinal = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
            } else {
                montoFinal = UtileriasJava.setInsert(Conf.INSERT_NUMERO,
                        inputCondicion.getDetalle().get(j).getMontoFinal(), Conf.INSERT_SEPARADOR_SI);
            }

            tecnologia = inputCondicion.getDetalle().get(j).getTecnologia();
            if (tecnologia == null || tecnologia.equals("")) {
                tecnologia = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
            } else {
                tecnologia = UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, tecnologia, Conf.INSERT_SEPARADOR_SI);
            }

            valores = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, CondicionCampania.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO,idPais + "", Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPadre + "", Conf.INSERT_SEPARADOR_SI)
                    + tipo
                    + idArticulo
                    + tecnologia
                    + montoInicial
                    + montoFinal
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO, usuario, Conf.INSERT_SEPARADOR_NO);
            inserts.add(valores);
        }

        sql = UtileriasBD.armarQueryInsertAll(CondicionCampania.N_TABLA, campos, inserts);

        int res = Qr.update(conn, sql);
        if (res > 0) {
            return Qr != null;
        } else {
            return false;
        }
    }

    /**
     * Funci\u00F3n que arma el query a utilizar al modificar datos en m\u00E9todos PUT y
     * DELETE.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputCondicion
     * @throws SQLException
     */
    public static OutputCondicion doDel(Connection conn, InputCondicionPrincipal input, int metodo, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "doDel";
        String nombreClase = new CurrentClassGetter().getClassName();
        String campania = UtileriasJava.getConfig(conn, Conf.GRUPO_OFERTA_COMERCIAL, Conf.TIPO_CAMPANIA, input.getCodArea());

        Respuesta respuesta = null;
        OutputCondicion output = null;
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Condicion.CAMPO_TCSCCONDICIONID, input.getIdCondicion()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Condicion.CAMPO_TIPO_OFERTACAMPANIA, campania));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Condicion.CAMPO_TCSCCATPAISID, idPais.toString()));
        String existencia = UtileriasBD.verificarExistencia(conn, Condicion.N_TABLA, condiciones);
        if (new Integer(existencia) < 1) {
            log.error("No existe el recurso.");
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());

            output = new OutputCondicion();
            output.setRespuesta(respuesta);

            return output;
        }

        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CondicionCampania.CAMPO_TCSCCONDICIONID, input.getIdCondicion()));
        String sql = UtileriasBD.armarQueryDelete(CondicionCampania.N_TABLA, condiciones);

        try {
            conn.setAutoCommit(false);
            QueryRunner Qr = new QueryRunner();
            int res = Qr.update(conn, sql);

            if (res >= 0) {
                condiciones = CtrlCondicion.obtenerCondiciones( input, metodo, idPais);
                sql = UtileriasBD.armarQueryDelete(Condicion.N_TABLA, condiciones);

                res = Qr.update(conn, sql);
                if (res >= 0) {
                    conn.commit();
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_DEL_CONDICION_37, null, nombreClase,
                            nombreMetodo, null, input.getCodArea());

                    output = new OutputCondicion();
                    output.setRespuesta(respuesta);
                } else {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputCondicion();
                    output.setRespuesta(respuesta);

                    log.error("Rollback");
                    conn.rollback();
                }
            } else {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output = new OutputCondicion();
                output.setRespuesta(respuesta);

                log.error("Rollback");
                conn.rollback();
            }
        } finally {
            conn.setAutoCommit(true);
    
        }

        return output;
    }
}

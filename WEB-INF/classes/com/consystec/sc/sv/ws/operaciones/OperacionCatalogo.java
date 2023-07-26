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

import com.consystec.sc.ca.ws.input.catalogo.InputCatalogo;
import com.consystec.sc.ca.ws.input.catalogo.InputGrupoCatalogo;
import com.consystec.sc.ca.ws.input.catalogo.InputParametro;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.catalogo.OutputCatalogo;
import com.consystec.sc.sv.ws.metodos.CtrlCatalogo;
import com.consystec.sc.sv.ws.orm.Catalogo;
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
public class OperacionCatalogo {
	private OperacionCatalogo(){}
    private static final Logger log = Logger.getLogger(OperacionCatalogo.class);

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputCatalogo
     * @throws SQLException
     */
    public static OutputCatalogo doGet(Connection conn, InputCatalogo input, int metodo, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<InputParametro> list = new ArrayList<InputParametro>();
        List<InputGrupoCatalogo> grupos = new ArrayList<InputGrupoCatalogo>();

        Respuesta respuesta = null;
        OutputCatalogo output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        String sql = null;
        String campos[] = CtrlCatalogo.obtenerCamposGetPost(metodo);

        List<Order> orden = new ArrayList<Order>();

        try {
            orden.add(new Order(Catalogo.CAMPO_GRUPO, Order.ASC));
            orden.add(new Order(Catalogo.CAMPO_NOMBRE, Order.ASC));

            List<Filtro> condiciones = CtrlCatalogo.obtenerCondiciones(input, metodo,idPais );

            sql = UtileriasBD.armarQuerySelect(Catalogo.N_TABLA, campos, condiciones, orden);
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_CATALOGOS_816, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputCatalogo();
                    output.setRespuesta(respuesta);
                } else {
                    respuesta = new Respuesta();
                    respuesta=new ControladorBase()
                            .getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, input.getCodArea());

                    do {
	                    InputParametro item = new InputParametro();
	                    item.setGrupo(rst.getString(Catalogo.CAMPO_GRUPO));
	                    item.setDescripcion(rst.getString(Catalogo.CAMPO_DESCRIPCION));
	                    item.setNombre(rst.getString(Catalogo.CAMPO_NOMBRE));
	                    item.setValor(rst.getString(Catalogo.CAMPO_VALOR));
	                    item.setEstado(rst.getString(Catalogo.CAMPO_ESTADO));
	                    item.setTipoParametro(rst.getString(Catalogo.CAMPO_PARAM_INTERNO));
	                    item.setTabla(rst.getString(Catalogo.CAMPO_TABLA));
	                    item.setCampoTabla(rst.getString(Catalogo.CAMPO_CAMPO_TABLA));
	                    item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(Catalogo.CAMPO_CREADO_EL)));
	                    item.setCreado_por(rst.getString(Catalogo.CAMPO_CREADO_POR));
	                    item.setModificado_el(UtileriasJava.formatStringDate(rst.getString(Catalogo.CAMPO_MODIFICADO_EL)));
	                    item.setModificado_por(rst.getString(Catalogo.CAMPO_MODIFICADO_POR));

                        list.add(item);
                    } while (rst.next());

                    orden.clear();
                    orden.add(new Order(Catalogo.CAMPO_GRUPO, Order.ASC));

                    List<String> listadoGrupos = UtileriasBD.getOneField(conn,
                            UtileriasJava.setSelect(Conf.SELECT_DISTINCT, Catalogo.CAMPO_GRUPO), Catalogo.N_TABLA,
                            condiciones, orden);

                    for (String grupo : listadoGrupos) {
                        InputGrupoCatalogo g = new InputGrupoCatalogo();
                        List<InputParametro> parametrosGrupo = new ArrayList<InputParametro>();
                        for (InputParametro parametro : list) {
                            if (parametro.getGrupo().equals(grupo)) {
                                parametrosGrupo.add(parametro);
                            }
                        }
                        if (parametrosGrupo.size() > 0) {
                            g.setGrupo(grupo);
                            g.setParametros(parametrosGrupo);
                            grupos.add(g);
                        }
                    }

                    output = new OutputCatalogo();
                    output.setRespuesta(respuesta);
                    output.setGrupo(grupos);
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
     * @return OutputCatalogo
     * @throws SQLException
     */
    public static OutputCatalogo doPost(Connection conn, InputCatalogo input, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doPost";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputCatalogo output = null;

        List<Filtro> condicionesExistencia = CtrlCatalogo.obtenerCondicionesExistencia(input,idPais);

        String existencia = UtileriasBD.verificarExistencia(conn, Catalogo.N_TABLA, condicionesExistencia);
        if (new Integer(existencia) > 0) {
            log.error("Ya existe el recurso.");
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NOMBRE_CAT_839, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());

            output = new OutputCatalogo();
            output.setRespuesta(respuesta);

            return output;
        }

        String sql = null;
        String campos[] = CtrlCatalogo.obtenerCamposGetPost(Conf.METODO_POST);
        List<String> inserts = CtrlCatalogo.obtenerInsertsPost(conn, input, Catalogo.SEQUENCE, idPais);

        sql = UtileriasBD.armarQueryInsertAll(Catalogo.N_TABLA, campos, inserts);

        try {
        	int i=0;
            conn.setAutoCommit(false);
            QueryRunner Qr = new QueryRunner();
            i=Qr.update(conn, sql);

            if (i>0) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_CATALOGO_33, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output = new OutputCatalogo();
                output.setRespuesta(respuesta);

                conn.commit();
            }else{
            	respuesta= new Respuesta();
            	respuesta.setCodResultado("0");
            	output = new OutputCatalogo();
                output.setRespuesta(respuesta);

            }
        } finally {
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
     * @param estadoBaja 
     * @param estadoAlta 
     * @return OutputCatalogo
     * @throws SQLException
     */
    public static OutputCatalogo doPutDel(Connection conn, InputCatalogo input, int metodo,
            String estadoBaja, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doPutDel";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputCatalogo output = null;

        List<Filtro> condicionesExistencia = CtrlCatalogo.obtenerCondicionesExistencia(input, idPais);
        String existencia = UtileriasBD.verificarExistencia(conn, Catalogo.N_TABLA, condicionesExistencia);
        String nombreTabla = Conf.NOMBRE_GENERICO;
        String nombreCampo = Conf.NOMBRE_GENERICO;
        String tipoParametro = "";
        if (new Integer(existencia) < 1) {
            log.error("No existe el recurso.");
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());

            output = new OutputCatalogo();
            output.setRespuesta(respuesta);

            return output;
        } else {
            String[] campos = {
                Catalogo.CAMPO_PARAM_INTERNO,
                Catalogo.CAMPO_TABLA,
                Catalogo.CAMPO_CAMPO_TABLA
            };
            
            List<Map<String, String>> datosParametro = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condicionesExistencia, null);
            tipoParametro = datosParametro.get(0).get(Catalogo.CAMPO_PARAM_INTERNO);
            nombreTabla = datosParametro.get(0).get(Catalogo.CAMPO_TABLA);
            nombreCampo = datosParametro.get(0).get(Catalogo.CAMPO_CAMPO_TABLA);

            if (tipoParametro == null || tipoParametro.equalsIgnoreCase("1")) {
                log.error("El par\u00E9metro no es externo.");
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_TIPO_CONFIG_190, null,
                    nombreClase, nombreMetodo, null, input.getCodArea());

                output = new OutputCatalogo();
                output.setRespuesta(respuesta);

                return output;
            } else {
                log.error("El par\u00E9metro es externo.");
                if (tipoParametro.equalsIgnoreCase("0")) {
                    if (nombreTabla.equalsIgnoreCase(Conf.NOMBRE_GENERICO) || nombreCampo.equalsIgnoreCase(Conf.NOMBRE_GENERICO)) {
                        log.error("El par\u00E9metro no esta configurado correctamente con el nombre de la tabla y nombre del campo.");
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_CONFIG_CAMPOS_192, null,
                                nombreClase, nombreMetodo, null, input.getCodArea());

                        output = new OutputCatalogo();
                        output.setRespuesta(respuesta);

                        return output;
                    }

                    if (metodo == Conf.METODO_DELETE
                            || (metodo == Conf.METODO_PUT && input.getParametros().get(0).getEstado() != null
                                    && input.getParametros().get(0).getEstado().equalsIgnoreCase(estadoBaja))) {
                        log.trace("Valida el par\u00E9metro para dar de baja.");
                        condicionesExistencia.clear();
                        condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, nombreCampo,
                                input.getParametros().get(0).getNombre()));

                        existencia = UtileriasBD.verificarExistencia(conn, nombreTabla, condicionesExistencia);
                        if (new Integer(existencia) > 0) {
                            log.trace("Existen " + existencia + " registros con esa configuraci\u00F3n.");
                            respuesta = new ControladorBase().getMensaje(
                                    Conf_Mensajes.MSJ_ERROR_CONFIG_DEPENDIENTES_191, null, nombreClase, nombreMetodo,
                                    existencia, input.getCodArea());

                            output = new OutputCatalogo();
                            output.setRespuesta(respuesta);

                            return output;
                        }
                    }
                } else {
                    if (metodo == Conf.METODO_DELETE
                            || (metodo == Conf.METODO_PUT && input.getParametros().get(0).getEstado() != null
                                    && input.getParametros().get(0).getEstado().equalsIgnoreCase(estadoBaja))) {

                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_BAJA_CONFIG_377, null,
                                nombreClase, nombreMetodo, null, input.getCodArea());

                        output = new OutputCatalogo();
                        output.setRespuesta(respuesta);

                        return output;
                    }
                }
            }
        }

        String campos[][] = CtrlCatalogo.obtenerCamposPutDel(input, metodo, estadoBaja);
        List<Filtro> condiciones = CtrlCatalogo.obtenerCondiciones(input, metodo, idPais);

        String sql = UtileriasBD.armarQueryUpdate(Catalogo.N_TABLA, campos, condiciones);

        try {
            conn.setAutoCommit(false);
            QueryRunner Qr = new QueryRunner();

            condicionesExistencia.clear();
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO,
                    input.getGrupoParametro()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE,
                    input.getParametros().get(0).getNombre()));
            String valorCatalogoBD = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_VALOR, Catalogo.N_TABLA,
                    condicionesExistencia);

            int res = Qr.update(conn, sql);

            if (res > 0) {
                String msjUpd = "";
                if ("0".equals(tipoParametro)&&  (!valorCatalogoBD.equals(input.getParametros().get(0).getValor()))) {
                        String camposUpd[][] = {
                            { nombreCampo, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getParametros().get(0).getValor()) }
                        };
                        condiciones.clear();
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, nombreCampo, valorCatalogoBD));

                        sql = UtileriasBD.armarQueryUpdate(nombreTabla, camposUpd, condiciones);
                        res = Qr.update(conn, sql);
                        msjUpd = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_CONFIG_UPDATE_362, null,
                                nombreClase, nombreMetodo, res + ".", input.getCodArea()).getDescripcion();
                }

                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_CATALOGO_34, null, nombreClase,
                        nombreMetodo, msjUpd, input.getCodArea());

                output = new OutputCatalogo();
                output.setRespuesta(respuesta);

                conn.commit();
            }
        } finally {
            conn.setAutoCommit(true);
        }

        return output;
    }
}

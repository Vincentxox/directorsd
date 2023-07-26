package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.cliente.VersionCatalogoCliente;
import com.consystec.sc.ca.ws.input.general.InputCatalogoVer;
import com.consystec.sc.ca.ws.input.general.InputVersionCatalogo;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.CatalogoVer.OutputCatalogoVer;
import com.consystec.sc.ca.ws.output.CatalogoVer.OutputVersionCatalogo;
import com.consystec.sc.ca.ws.output.CatalogoVer.RListCatalogo;
import com.consystec.sc.ca.ws.output.CatalogoVer.VersionCatalogo;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.Util;
import com.consystec.sc.sv.ws.metodos.CtrlVersionCatalogo;
import com.google.gson.GsonBuilder;

public class CatalogoVer extends ControladorBase {

    private static final Logger log = Logger.getLogger(CatalogoVer.class);

    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputCatalogoVer objDatos) {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getCodArea() == null || "".equals(objDatos.getCodArea())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);

        } else if (objDatos.getCodArea().length() != Conf.LONG_CODPAIS) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGINVALIDA_AREA_6, null, this.getClass().toString(), metodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        } else {
            BigDecimal idPais = null;
            try {
                conn = getConnLocal();
                idPais = getidpais(conn, objDatos.getCodArea());
                if (idPais == null || idPais.intValue() == 0) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_AREAINVALIDA_8, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        }


        if (objDatos.getFecha() == null || "".equals(objDatos.getFecha())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHA_NULO_11, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para obtener los parametros a utilizar en la aplicaci\u00F3n
     **/
    public OutputCatalogoVer obtenerCatalogo(BigDecimal pais, String version) {
        Respuesta mensajeAdv = new Respuesta();
        Connection conn = null;
        String metodo = "obtenerCatalogo";
        String query = "";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        int cont = 0;
        System.out.println("12");
        SimpleDateFormat FORMATO_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");
        String fecha = FORMATO_TIMESTAMP.format(new Date());
        System.out.println(fecha);
		//Lista para almacenar los registros obtenidos en el query
        List<RListCatalogo> Lstcatalogo = new ArrayList<RListCatalogo>();
        boolean existenDatos = false;

        try {
            conn = getConnLocal();
            log.trace("Obtiene catalogos locales");

            // obtener versiones locales
            Lstcatalogo = getVersionLocal(conn, pais.toString(), version);

            log.trace("tama\u00F1o actual lista:" + Lstcatalogo.size());
			/* Consulta para obtener los parametros utilizables */
			query = "SELECT * " +
					"  FROM TC_SC_CATALOGO_VER_VW " +
					" WHERE TO_DATE (version, 'yyyymmddHH24MiSS') >= " +
					"          TO_DATE (?, 'yyyymmddHH24MiSS')  " +
					"          AND AREA=? OR AREA=-1" ;

            pstmt = conn.prepareStatement(query);
            log.trace("query:" + query);
            log.trace("pais:" + pais);
            pstmt.setString(1, version);
            pstmt.setBigDecimal(2, pais);

            rst = pstmt.executeQuery();


            while (rst.next()) {
                existenDatos = true;
				/*Se almacenan los registros encontrados en la lista de la clase tipo Parametros*/
                RListCatalogo obj = new RListCatalogo(rst.getString("CATALOGO"), rst.getString("SERVICIO"),
                        rst.getString("AREA"));

                Lstcatalogo.add(obj);
                cont++;
            }
            log.trace("CANTIDAD DE PARAMETROS ENCONTRADA:" + cont);
            log.trace("EXISTEN DATOS:" + existenDatos);

            // obteniendo los catalogos del pa\u00EDs ingresado

			/*si se obtuvieron registros se construye el objeto de respuesta de lo contrario 
			 * se retornara una excepcion*/
            if (existenDatos || !Lstcatalogo.isEmpty()) {
                mensajeAdv = getMensaje(Conf_Mensajes.OK_ACTUALIZACION2, null, null, null, null, null);

                OutputCatalogoVer respuesta = new OutputCatalogoVer(mensajeAdv, fecha, Lstcatalogo);

                log.trace("json:" + new GsonBuilder().setPrettyPrinting().create().toJson(respuesta));
                return respuesta;
            } else {
                mensajeAdv = getMensaje(Conf_Mensajes.MSJ_DATOSNOENCONTRADOS_10, null, null, this.getClass().getName(),
                        metodo, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                OutputCatalogoVer respuesta = new OutputCatalogoVer(mensajeAdv, null, Lstcatalogo);
                return respuesta;
            }

        } catch (SQLException e) {
        	log.error(e,e);
            mensajeAdv = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().getName(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            OutputCatalogoVer respuesta = new OutputCatalogoVer(mensajeAdv, null, Lstcatalogo);
            return respuesta;
        } catch (Exception e) {
        	log.error(e,e);
            mensajeAdv = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            OutputCatalogoVer respuesta = new OutputCatalogoVer(mensajeAdv, null, Lstcatalogo);
            return respuesta;
        } finally {
        	DbUtils.closeQuietly(pstmt);
        	DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(conn);
        }
    }

    public List<RListCatalogo> getVersionLocal(Connection conn, String pais, String version)
            throws InterruptedException {
        List<RListCatalogo> lista = new ArrayList<RListCatalogo>();
        RListCatalogo objeto = new RListCatalogo();
        String url = "";
        // objetos para servicio de versiones locales
        InputVersionCatalogo objInput = new InputVersionCatalogo();
        OutputVersionCatalogo respuestaws = new OutputVersionCatalogo();
        try {
            objInput.setFecha(version);
            objInput.setGrupo(getParametro(Conf.CATALOGOS_MOVIL, pais));
            objInput.setCodArea(pais);
            
            if (isFullStack(objInput.getCodArea())){
            	  CtrlVersionCatalogo recurso = new CtrlVersionCatalogo();
            	  respuestaws=recurso.getCatalogo(objInput);
        		
                log.trace("respuesta:" + respuestaws.getRespuesta().getDescripcion() + ", "
                        + respuestaws.getRespuesta().getExcepcion());
                if (!respuestaws.getCatalogos().isEmpty()) {
                    log.trace("encontro catalogos:" + respuestaws.getCatalogos());
                    for (VersionCatalogo obj : respuestaws.getCatalogos()) {
                        if (obj.getUrl() != null) {
                            objeto = new RListCatalogo();
                            objeto.setCodPais(pais);
                            objeto.setNombre(obj.getNombre());
                            objeto.setUrl(obj.getUrl());

                            lista.add(objeto);
                        }
                    }
                }
           	log.trace("consume metodo");
            }
           else{
            
            url = Util.getURLWSLOCAL(conn, pais, Conf.SERVICIO_LOCAL_GETVERSIONCATALOGO);
            log.trace("URL:" + url);

            if (!(url == null || "".equals(url))) {
                log.trace("si entra a consumir ws");
                VersionCatalogoCliente ws = new VersionCatalogoCliente();
                ws.setServerUrl(url);
                respuestaws = ws.getVersiones(objInput);

                log.trace("respuesta:" + respuestaws.getRespuesta().getDescripcion() + ", "
                        + respuestaws.getRespuesta().getExcepcion());
                if (!respuestaws.getCatalogos().isEmpty()) {
                    log.trace("encontro catalogos:" + respuestaws.getCatalogos());
                    for (VersionCatalogo obj : respuestaws.getCatalogos()) {
                        if (obj.getUrl() != null) {
                            objeto = new RListCatalogo();
                            objeto.setCodPais(pais);
                            objeto.setNombre(obj.getNombre());
                            objeto.setUrl(obj.getUrl());

                            lista.add(objeto);
                        }
                    }
                }
            }
        }
        } catch (Exception e) {
        	log.error(e,e);
        }

        return lista;
    }

    public OutputCatalogoVer getCatalogo(InputCatalogoVer objeto) {
        OutputCatalogoVer respuesta = new OutputCatalogoVer();
        Respuesta objRespuesta;

        objRespuesta = validarDatos(objeto);

        if (objRespuesta == null) {
            respuesta = obtenerCatalogo(new BigDecimal(objeto.getCodArea()), objeto.getFecha());
        } else {
            respuesta.setMensaje(objRespuesta);
        }

        return respuesta;
    }
}

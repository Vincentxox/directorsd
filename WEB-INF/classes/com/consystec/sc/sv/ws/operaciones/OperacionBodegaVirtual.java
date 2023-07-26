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
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegaVirtual;
import com.consystec.sc.sv.ws.metodos.CtrlBodegaAlmacen;
import com.consystec.sc.sv.ws.metodos.CtrlBodegaVirtual;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Panel;
import com.consystec.sc.sv.ws.orm.Ruta;
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
public class OperacionBodegaVirtual {
	private OperacionBodegaVirtual(){}
    private static final Logger log = Logger.getLogger(OperacionBodegaVirtual.class);
    
    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputBodegaVirtual
     * @throws SQLException
     */
    public static OutputBodegaVirtual doGet(Connection conn, InputBodegaVirtual input, int metodo, BigDecimal idPais) throws SQLException {
    	String nombreMetodo = "doGet";
    	String nombreClase = new CurrentClassGetter().getClassName();

    	List<InputBodegaVirtual> list = new ArrayList<InputBodegaVirtual>();

    	Respuesta respuesta = null;
    	OutputBodegaVirtual output = null;
    	PreparedStatement pstmt = null;
    	ResultSet rst = null;

    	String sql = null;
    	String campos[] = CtrlBodegaVirtual.obtenerCamposGetPost(metodo);
    	List<Filtro> condiciones = CtrlBodegaVirtual.obtenerCondiciones(input, metodo,idPais);

    	sql = UtileriasBD.armarQuerySelect(ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea()), campos, condiciones);

    	try {
    		pstmt = conn.prepareStatement(sql);
    		rst = pstmt.executeQuery();

    		if (rst != null) {
    			if (!rst.next()) {
    				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_BODEGAS_815, null,
    						nombreClase, nombreMetodo, null, input.getCodArea());

    				output = new OutputBodegaVirtual();
    				output.setRespuesta(respuesta);
    			} else {
    				respuesta = new Respuesta();
    				respuesta=new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
    						nombreClase, nombreMetodo, null, input.getCodArea());

    				do {
    					InputBodegaVirtual item = new InputBodegaVirtual();
    					String idBodega = rst.getString(BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID);
    					item.setIdBodega(idBodega);
    					item.setNombre(rst.getString(BodegaVirtual.CAMPO_NOMBRE));
    					item.setUsuario(input.getUsuario());
    					item.setEstado(rst.getString(BodegaVirtual.CAMPO_ESTADO));
    					item.setLatitud(UtileriasJava.getValue(rst.getString(BodegaVirtual.CAMPO_LATITUD)));
    					item.setLongitud(UtileriasJava.getValue(rst.getString(BodegaVirtual.CAMPO_LONGITUD)));
    					item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(BodegaVirtual.CAMPO_CREADO_EL)));
    					item.setCreado_por(rst.getString(BodegaVirtual.CAMPO_CREADO_POR));
    					item.setModificado_el(UtileriasJava.formatStringDate(rst.getString(BodegaVirtual.CAMPO_MODIFICADO_EL)));
    					item.setModificado_por(rst.getString(BodegaVirtual.CAMPO_MODIFICADO_POR));
    					item.setIdBodegaPadre(UtileriasJava.getValue(rst.getString(BodegaVirtual.CAMPO_IDBODEGA_PADRE)));
    					item.setNivel(rst.getString(BodegaVirtual.CAMPO_NIVEL));
    					item.setIdBodegaOrigen(UtileriasJava.getValue(rst.getString(BodegaVirtual.CAMPO_IDBODEGA_ORIGEN)));
    					item.setTipo(rst.getString(BodegaVirtual.CAMPO_TIPO));
    					if(rst.getString(BodegaVirtual.CAMPO_DIRECCION)==null || "".equals(rst.getString(BodegaVirtual.CAMPO_DIRECCION))){
    						item.setDireccion("");
    					}else{
    						item.setDireccion(rst.getString(BodegaVirtual.CAMPO_DIRECCION));
    					}
    					list.add(item);
    				} while (rst.next());

    				output = new OutputBodegaVirtual();
    				output.setRespuesta(respuesta);
    				output.setBodegaVirtual(list);
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
     * @return OutputBodegaVirtual
     * @throws SQLException
     */
    public static OutputBodegaVirtual doPost(Connection conn, InputBodegaVirtual input, int metodo, String estadoAlta, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "doPost";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputBodegaVirtual output = null;
        int idObtenido = 0;

        List<Filtro> condicionesExistencia = CtrlBodegaVirtual.obtenerCondicionesExistencia(input,  metodo, estadoAlta,idPais);
        String existencia = UtileriasBD.verificarExistencia(conn, BodegaVirtual.N_TABLA, condicionesExistencia);
        if (new Integer(existencia) > 0) {
            log.error("Ya existe el recurso.");
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NOMBRE_BOD_297, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());

            output = new OutputBodegaVirtual();
            output.setRespuesta(respuesta);

            return output;
        }

        String sql = null;
        String campos[] = CtrlBodegaVirtual.obtenerCamposGetPost(Conf.METODO_POST);
        List<String> inserts = CtrlBodegaVirtual.obtenerInsertsPostPadre(conn, input, BodegaVirtual.SEQUENCE,idPais);

        sql = UtileriasBD.armarQueryInsert(BodegaVirtual.N_TABLA, campos, inserts);

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn.setAutoCommit(false);
            String generatedColumns[] = { BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID };
            pstmt = conn.prepareStatement(sql, generatedColumns);
            
            pstmt.executeUpdate();  
            rs = pstmt.getGeneratedKeys();
           
            if (rs == null) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null,
                        nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputBodegaVirtual();
                    output.setRespuesta(respuesta);
            } else {
                if (rs.next()) {
                    idObtenido = rs.getInt(1);
                    log.trace("ID BODEGA VIRTUAL: " + rs.getInt(1));
                }
                conn.commit();
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_BODEGA_VIRTUAL_29, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output = new OutputBodegaVirtual();
                output.setIdBodegaVirtual("" + idObtenido);
                output.setRespuesta(respuesta);
            }
        } finally {
            if (pstmt != null) {
                DbUtils.closeQuietly(rs);
                DbUtils.closeQuietly(pstmt);
            }
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
     * @return OutputBodegaVirtual
     * @throws SQLException
     */
    public static OutputBodegaVirtual doPutDel(Connection conn, InputBodegaVirtual input, int metodo, String estadoAlta, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "doPutDel";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputBodegaVirtual output = null;
        
        List<Filtro> condicionesExistencia = CtrlBodegaVirtual.obtenerCondicionesExistencia(input,  metodo, estadoAlta, idPais);
        String existencia = UtileriasBD.verificarExistencia(conn, BodegaVirtual.N_TABLA, condicionesExistencia);
        if (new Integer(existencia) < 1) {
            log.error("No existe el recurso.");
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null,
                nombreClase, nombreMetodo, null, input.getCodArea());
            
            output = new OutputBodegaVirtual();
            output.setRespuesta(respuesta);

            return output;
        } else {
            if (metodo == Conf.METODO_DELETE
                    || (input.getEstado() != null && !input.getEstado().equalsIgnoreCase(estadoAlta))) {
               
                
                // validacion de bodega hijas
                condicionesExistencia.clear();
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_OR, BodegaVirtual.CAMPO_IDBODEGA_ORIGEN, input.getIdBodega()));
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_OR, BodegaVirtual.CAMPO_IDBODEGA_PADRE, input.getIdBodega()));
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVirtual.CAMPO_ESTADO, estadoAlta));
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TCSCCATPAISID, idPais.toString()));
                existencia = UtileriasBD.verificarExistencia(conn, BodegaVirtual.N_TABLA, condicionesExistencia);
                if (new Integer(existencia) > 0) {
                    log.error("La bodega tiene bodegas hijas.");
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_EXISTEN_BODEGAS_HIJAS_808,
                            null, nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputBodegaVirtual();
                    output.setRespuesta(respuesta);

                    return output;
                }
            }
            
            if (metodo == Conf.METODO_PUT && !input.getEstado().equalsIgnoreCase(estadoAlta)) {
                // validacion de bodega en ruta
                condicionesExistencia.clear();
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TC_SC_BODEGAVIRTUAL_ID, input.getIdBodega()));
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Ruta.CAMPO_ESTADO, estadoAlta));
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TCSCCATPAISID, idPais.toString()));
                existencia = UtileriasBD.verificarExistencia(conn, Ruta.N_TABLA, condicionesExistencia);
                if (new Integer(existencia) > 0) {
                    log.error("La bodega esta asociada a una ruta.");
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_BODEGA_EXISTE_RUTA_119, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputBodegaVirtual();
                    output.setRespuesta(respuesta);

                    return output;
                }

                // validacion de bodega en panel
                condicionesExistencia.clear();
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodega()));
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCCATPAISID, idPais.toString()));
                condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Panel.CAMPO_ESTADO, estadoAlta));
                existencia = UtileriasBD.verificarExistencia(conn, Panel.N_TABLA, condicionesExistencia);
                if (new Integer(existencia) > 0) {
                    log.error("La bodega esta asociada a una panel.");
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_BODEGA_EXISTE_PANEL_118, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputBodegaVirtual();
                    output.setRespuesta(respuesta);

                    return output;
                }
            }
        }

        String sql = null;
        int codMensaje = 0;

        String campos[][] = CtrlBodegaVirtual.obtenerCamposPutDelPadre(input,  estadoAlta);
        List<Filtro> condiciones = CtrlBodegaVirtual.obtenerCondiciones(input, metodo, idPais);
        

        if (metodo == Conf.METODO_PUT) {
            sql = UtileriasBD.armarQueryUpdate(BodegaVirtual.N_TABLA, campos, condiciones);
            codMensaje = Conf_Mensajes.OK_MOD_BODEGA_VIRTUAL_30;
        } else {
            sql = UtileriasBD.armarQueryDelete(BodegaVirtual.N_TABLA, condiciones);
            codMensaje = Conf_Mensajes.OK_DEL_BODEGA_VIRTUAL_31;
        }

        try {
        	int i=0;
            conn.setAutoCommit(false);
            QueryRunner Qr = new QueryRunner();
            i=Qr.update(conn, sql);

            if (i>0) {
                conn.commit();
                respuesta = new ControladorBase().getMensaje(codMensaje, null, nombreClase, nombreMetodo, null, input.getCodArea());

                output = new OutputBodegaVirtual();
                output.setRespuesta(respuesta);
            }else{
            	respuesta= new Respuesta();
            	respuesta.setCodResultado("0");
            	output = new OutputBodegaVirtual();
            	output.setRespuesta(respuesta);
            }
        } finally {
            conn.setAutoCommit(true);
            
        }

        return output;
    }

    public static boolean verificarNivelBodega(Connection conn, String idBodega) {
        try {
            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, idBodega));
            String nivelPadre = UtileriasBD.getOneRecord(conn, BodegaVirtual.CAMPO_NIVEL, BodegaVirtual.N_TABLA, condiciones);
            

            if (!nivelPadre.equals("1")) {
                log.error("La bodega padre/origen enviada no es de nivel para ser bodega padre.");
                return true;
            }
        } catch (SQLException e) {
            log.error("Excepcion al consultar nivel de bodega: " + e);
            return true;
        }
        
        return false;
    }
}

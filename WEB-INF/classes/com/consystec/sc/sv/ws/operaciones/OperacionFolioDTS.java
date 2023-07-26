package com.consystec.sc.sv.ws.operaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.bodegadts.InputConfiguracionFolio;
import com.consystec.sc.ca.ws.input.general.InputFolioDTS;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.bogegas.OutputConfiguracionFolio;
import com.consystec.sc.sv.ws.metodos.CtrlFolioDTS;
import com.consystec.sc.sv.ws.orm.ConfiguracionFolioDTS;
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
public class OperacionFolioDTS {
	private OperacionFolioDTS(){}
    private static final Logger log = Logger.getLogger(OperacionFolioDTS.class);
    
    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return respuestaConfiguracionFolio
     * @throws SQLException
     */
    public static OutputConfiguracionFolio doGet(Connection conn, InputFolioDTS input, int metodo) throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        List<InputConfiguracionFolio> list = new ArrayList<InputConfiguracionFolio>();
        
        Respuesta respuesta = null;
        OutputConfiguracionFolio output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        
        String sql = null;
        String campos[] = CtrlFolioDTS.obtenerCamposGetPost(metodo);

        List<Filtro> condiciones = CtrlFolioDTS.obtenerCondiciones(input, metodo);

        try{
	        sql = UtileriasBD.armarQuerySelect(ConfiguracionFolioDTS.N_TABLA, campos, condiciones);
	        pstmt = conn.prepareStatement(sql);
	        rst = pstmt.executeQuery();
	        
	        if (rst != null) {
	            if (!rst.next()) {
	                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSOS, null,
	                    nombreClase, nombreMetodo, null, input.getCodArea());
	
	                output = new OutputConfiguracionFolio();
	                output.setRespuesta(respuesta);
	            } else {
	                respuesta = new Respuesta();
	                respuesta=new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
	                    nombreClase, nombreMetodo, null, input.getCodArea());
	                
	                do {
	                	InputConfiguracionFolio item = new InputConfiguracionFolio();
	                    item.setIdBodega(rst.getString(ConfiguracionFolioDTS.CAMPO_TC_SC_ALMACENBODID));
	                    item.setTipoDocumento(rst.getString(ConfiguracionFolioDTS.CAMPO_TIPODOCUMENTO));
	                    item.setSerie(rst.getString(ConfiguracionFolioDTS.CAMPO_SERIE));
	                    item.setNoInicialFolio(rst.getString(ConfiguracionFolioDTS.CAMPO_NO_INICIAL_FOLIO));
	                    item.setNoFinalFolio(rst.getString(ConfiguracionFolioDTS.CAMPO_NO_FINAL_FOLIO));
	                    item.setUsuario(rst.getString(ConfiguracionFolioDTS.CAMPO_CREADO_POR));
	                    item.setEstado(rst.getString(ConfiguracionFolioDTS.CAMPO_ESTADO));
	                    item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(ConfiguracionFolioDTS.CAMPO_CREADO_EL)));
	                    item.setCreado_por(rst.getString(ConfiguracionFolioDTS.CAMPO_CREADO_POR));
	                    item.setModificado_el(UtileriasJava.formatStringDate(rst.getString(ConfiguracionFolioDTS.CAMPO_MODIFICADO_EL)));
	                    item.setModificado_por(rst.getString(ConfiguracionFolioDTS.CAMPO_MODIFICADO_POR));
	                    
	                    list.add(item);
	                } while (rst.next());
	                
	                output = new OutputConfiguracionFolio();
	                output.setRespuesta(respuesta);
	                output.setConfiguracionFolio(list);
	            }
	        }
        }finally{
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
     * @return respuestaConfiguracionFolio
     * @throws SQLException
     */
    public static OutputConfiguracionFolio doPost(Connection conn, InputFolioDTS input) throws SQLException {
        String nombreMetodo = "doPost";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta respuesta = null;
        OutputConfiguracionFolio output = null;
        
        List<Filtro> condicionesExistencia = CtrlFolioDTS.obtenerCondicionesExistencia(conn, input);
        String existencia = UtileriasBD.verificarExistencia(conn, ConfiguracionFolioDTS.N_TABLA, condicionesExistencia);
        if (new Integer(existencia) > 0) {
            log.error("Ya existe el recurso.");
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_EXISTENTE, null,
                nombreClase, nombreMetodo, null, input.getCodArea());

            output = new OutputConfiguracionFolio();
            output.setRespuesta(respuesta);
            
            return output;
        };
        
        String sql = null;
        String campos[] = CtrlFolioDTS.obtenerCamposGetPost(Conf.METODO_POST);
        List<String> inserts = CtrlFolioDTS.obtenerInsertsPost(input, ConfiguracionFolioDTS.SEQUENCE);
        
        sql = UtileriasBD.armarQueryInsert(ConfiguracionFolioDTS.N_TABLA, campos, inserts);

        try {
        	int i=0;
            conn.setAutoCommit(false);
            QueryRunner Qr = new QueryRunner();
            i=Qr.update(conn, sql);

            if (i>0) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_CREADO, null,
                    nombreClase, nombreMetodo, null, input.getCodArea());

                output = new OutputConfiguracionFolio();
                output.setRespuesta(respuesta);
            }else{
            	respuesta = new Respuesta();
            	respuesta.setCodResultado("0");
            	output = new OutputConfiguracionFolio();
                output.setRespuesta(respuesta);
            }
            conn.commit();
        } catch (SQLException e) {
            respuesta = new ControladorBase().getMensaje(e.getErrorCode(), e.getMessage(),
                nombreClase, nombreMetodo, null, input.getCodArea());

            output = new OutputConfiguracionFolio();
            output.setRespuesta(respuesta);
            
            log.error("Rollback");
            conn.rollback();
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
     * @return respuestaConfiguracionFolio
     * @throws SQLException
     */
    public static OutputConfiguracionFolio doPutDel(Connection conn, InputFolioDTS input, int metodo) throws SQLException {
        String nombreMetodo = "doPutDel";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta respuesta = null;
        OutputConfiguracionFolio output = null;
        
        List<Filtro> condicionesExistencia = CtrlFolioDTS.obtenerCondicionesExistencia(conn, input);
        String existencia = UtileriasBD.verificarExistencia(conn, ConfiguracionFolioDTS.N_TABLA, condicionesExistencia);
        if (new Integer(existencia) < 1) {
            log.error("No existe el recurso.");
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null,
                nombreClase, nombreMetodo, null, input.getCodArea());
            
            output = new OutputConfiguracionFolio();
            output.setRespuesta(respuesta);

            return output;
        };
                
        String sql = null;

        String campos[][] = CtrlFolioDTS.obtenerCamposPutDel(input);
        List<Filtro> condiciones = CtrlFolioDTS.obtenerCondiciones(input, metodo);
        
        sql = UtileriasBD.armarQueryUpdate(ConfiguracionFolioDTS.N_TABLA, campos, condiciones);
        
        try {
        	int i=0;
            conn.setAutoCommit(false);
            QueryRunner Qr = new QueryRunner();
            i=Qr.update(conn, sql);

            if (i>0) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_MODIFICADO, null,
                    nombreClase, nombreMetodo, null, input.getCodArea());

                output = new OutputConfiguracionFolio();
                output.setRespuesta(respuesta);
            }else{
            	respuesta = new Respuesta();
            	respuesta.setCodResultado("0");
            	output = new OutputConfiguracionFolio();
                output.setRespuesta(respuesta);
            }
            conn.commit();
        } catch (SQLException e) {
            respuesta = new ControladorBase().getMensaje(e.getErrorCode(), e.getMessage(),
                nombreClase, nombreMetodo, null, input.getCodArea());

            output = new OutputConfiguracionFolio();
            output.setRespuesta(respuesta);
            
            log.error("Rollback");
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
        
        return output;
    }
}

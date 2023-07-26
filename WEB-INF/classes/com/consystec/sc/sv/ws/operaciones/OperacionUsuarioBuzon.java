package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.ms.seguridad.excepciones.ExcepcionSeguridad;
import com.consystec.sc.ca.ws.input.buzon.InputBuzon;
import com.consystec.sc.ca.ws.input.buzon.InputUsuarioBuzon;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.buzon.OutputUsuarioBuzon;
import com.consystec.sc.sv.ws.metodos.CtrlUsuarioBuzon;
import com.consystec.sc.sv.ws.orm.UsuarioBuzon;
import com.consystec.sc.sv.ws.orm.BuzonSidra;
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
public class OperacionUsuarioBuzon {
	private OperacionUsuarioBuzon(){}
    private static final Logger log = Logger.getLogger(OperacionUsuarioBuzon.class);

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputUsuarioBuzon
     * @throws SQLException
     * @throws NamingException 
     * @throws ExcepcionSeguridad 
     */
    public static OutputUsuarioBuzon doGet(Connection conn,
            InputUsuarioBuzon input, int metodo, BigDecimal ID_PAIS) throws SQLException, ExcepcionSeguridad, NamingException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        List<InputUsuarioBuzon> list = new ArrayList<InputUsuarioBuzon>();
        
        Respuesta respuesta = null;
        OutputUsuarioBuzon output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        
        String sql = null;
        String campos[] = CtrlUsuarioBuzon.obtenerCamposGetPost(metodo);

        List<Filtro> condiciones = CtrlUsuarioBuzon.obtenerCondiciones(input, metodo, ID_PAIS);
        
        List<Order> orden = new ArrayList<Order>();
       
        try{
	        orden.add(new Order(UsuarioBuzon.CAMPO_TCSCBUZONID, Order.ASC));
	
	        sql = UtileriasBD.armarQuerySelect(UsuarioBuzon.N_TABLA, campos, condiciones, orden);
	        pstmt = conn.prepareStatement(sql);
	        rst = pstmt.executeQuery();
	        
	        if (rst != null) {
	            if (!rst.next()) {
	                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_USUARIOBUZON_823, null,
	                        nombreClase, nombreMetodo, null, input.getCodArea());
	
	                output = new OutputUsuarioBuzon();
	                output.setRespuesta(respuesta);
	            } else {
	                respuesta = new Respuesta();
	                respuesta=new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
	                    nombreClase, nombreMetodo, null, input.getCodArea());
	
	                String idVendedor = "";
	                String idBuzon = "";
	
	                do {
	                    InputUsuarioBuzon item = new InputUsuarioBuzon();
	                    idVendedor = rst.getString(UsuarioBuzon.CAMPO_SECUSUARIOID);
	                    idBuzon = rst.getString(UsuarioBuzon.CAMPO_TCSCBUZONID);
	                    
	                    item.setIdUsuarioBuzon(rst.getString(UsuarioBuzon.CAMPO_TCSCUSUARIOBUZONID));
	                    item.setIdBuzon(idBuzon);
	
	                    item.setIdVendedor(idVendedor);
	                    item.setNombreVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, UsuarioBuzon.CAMPO_NOMBRE_VEND));
	                    item.setUsuarioVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, UsuarioBuzon.CAMPO_USUARIO_VEND));
	                    
	                    item.setEstado(rst.getString(UsuarioBuzon.CAMPO_ESTADO));
	                    item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(UsuarioBuzon.CAMPO_CREADO_EL)));
	                    item.setCreado_por(rst.getString(UsuarioBuzon.CAMPO_CREADO_POR));
	                    item.setModificado_el(UtileriasJava.formatStringDate(rst.getString(UsuarioBuzon.CAMPO_MODIFICADO_EL)));
	                    item.setModificado_por(rst.getString(UsuarioBuzon.CAMPO_MODIFICADO_POR));
	                    
	                    list.add(item);
	                } while (rst.next());
	                log.trace("termino de agregar usuarios");
	            
	                
	                List<InputBuzon> listBuzones = getBuzones(conn, input.getCodArea());
	                List<InputUsuarioBuzon> listado = new ArrayList<InputUsuarioBuzon>();
	                for (InputBuzon itemBuzones : listBuzones) {
	                	InputUsuarioBuzon b = new InputUsuarioBuzon();
	                    List<InputUsuarioBuzon> buzones = new ArrayList<InputUsuarioBuzon>();
	                    log.trace("Compara buzones");
	                    for (InputUsuarioBuzon buzonUsuario : list) {
	                    	log.trace("Compara buzones");
	                        if (buzonUsuario.getIdBuzon().equals(itemBuzones.getIdBuzon())){
	                        	log.trace("Compara buzones");
	                            buzones.add(buzonUsuario);
	                        }
	                    }
	                    if (buzones.size() > 0){
	                        b.setIdBuzon(itemBuzones.getIdBuzon());
	                        b.setNombreBuzon(itemBuzones.getNombre());
	                        b.setNivelBuzon(itemBuzones.getNivel());
	                        b.setBuzones(buzones);
	                        listado.add(b);
	                    }
	                }
	                
	                output = new OutputUsuarioBuzon();
	                output.setRespuesta(respuesta);
	                output.setBuzones(listado);
	            }
	        }
        }finally{
	        DbUtils.closeQuietly(rst);
	        DbUtils.closeQuietly(pstmt);
        }
        return output;
    }
    
    private static List<InputBuzon> getBuzones(Connection conn, String codArea) throws SQLException {
        String nombreMetodo = "getBuzones";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        InputBuzon item = new InputBuzon();
        List<InputBuzon> list = new ArrayList<InputBuzon>();
        PreparedStatement pstmtIn = null;
        ResultSet rstIn = null;
        
        String campos[] = {
            BuzonSidra.CAMPO_TCSCBUZONID,
            BuzonSidra.CAMPO_NOMBRE,
            BuzonSidra.CAMPO_NIVEL
        };
        
        List<Order> orden = new ArrayList<Order>();
        List<Filtro> condiciones = new ArrayList<Filtro>();
        orden.add(new Order(BuzonSidra.CAMPO_NOMBRE, Order.ASC));

        
        try{
	    
	        condiciones.add(UtileriasJava.getCondicionDefault(Conf.FILTRO_ESTADO, BuzonSidra.CAMPO_ESTADO, conn, codArea));
	        
	        String sql = UtileriasBD.armarQuerySelect(BuzonSidra.N_TABLA, campos, condiciones, orden);
	        
	        pstmtIn = conn.prepareStatement(sql);
	        rstIn = pstmtIn.executeQuery();
	        
	        if (rstIn != null) {
	            if (!rstIn.next()) {
	                log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
	                item = new InputBuzon();
	
	                Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null,
	                        nombreClase, nombreMetodo, null, codArea);
	
	                item.setNombre(respuesta.getDescripcion());
	            } else {
	                do {
	                    item = new InputBuzon();
	                    item.setIdBuzon(rstIn.getString(BuzonSidra.CAMPO_TCSCBUZONID));
	                    item.setNombre(rstIn.getString(BuzonSidra.CAMPO_NOMBRE));
	                    item.setNivel(rstIn.getString(BuzonSidra.CAMPO_NIVEL));
	                    list.add(item);
	                } while (rstIn.next());
	            }
	        }
        }finally{

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
     * @return OutputUsuarioBuzon
     * @throws SQLException
     */
    public static OutputUsuarioBuzon doPost(Connection conn,
            InputUsuarioBuzon input, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "doPost";
        String nombreClase = new CurrentClassGetter().getClassName();
        String nombreBuzon ="";
        String nivel ="";
        String nombreDts ="";
        PreparedStatement pstmt=null;
        ResultSet rst=null;
        String query="";
        
        Respuesta respuesta = null;
        OutputUsuarioBuzon output = null;
        
        String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
        
        List<Filtro> condicionesExistencia = CtrlUsuarioBuzon.obtenerCondicionesExistencia(input, Conf.METODO_POST,  estadoAlta, ID_PAIS);
        
        String existencia = UtileriasBD.verificarExistencia(conn, UsuarioBuzon.N_TABLA, condicionesExistencia);
        if (new Integer(existencia) > 0) {
            log.error("Ya existe el recurso.");
            
            String camposSelect[] = { UsuarioBuzon.CAMPO_TCSCBUZONID };
            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, UsuarioBuzon.CAMPO_SECUSUARIOID, input.getIdVendedor()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, UsuarioBuzon.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, UsuarioBuzon.CAMPO_ESTADO, estadoAlta));

            String selectIds = UtileriasBD.armarQuerySelect(UsuarioBuzon.N_TABLA, camposSelect, condiciones);
            
            query="SELECT A.NOMBRE, A.NIVEL, B.NOMBRES FROM TC_SC_BUZONSIDRA A "
            		+ " INNER JOIN TC_SC_DTS B on A.TCSCDTSID=B.TCSCDTSID "
            		+ " WHERE TCSCBUZONID IN (" + selectIds + ")"
            		+ " AND A.ESTADO = '" + estadoAlta + "'"
            		+ " AND A.TCSCCATPAISID = " + ID_PAIS.toString();
            
            log.trace("Datos de nombre, nivel y dst del buzon " + query);
            try{
	            pstmt=conn.prepareStatement(query);
	            rst=pstmt.executeQuery();
	            
	            if (rst.next()){
	            	nombreBuzon= rst.getString(1);
	            	nivel=rst.getString(2);
	            	nombreDts=rst.getString(3);
	            }
            }finally{
            	DbUtils.closeQuietly(pstmt);
            	DbUtils.closeQuietly(rst);
            }
            if (!nombreBuzon.equals("")) {
            	respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_USUARIO_BUZON_838, null,
                        nombreClase, nombreMetodo, "BUZON: " + nombreBuzon.toUpperCase() + " Nivel: " + nivel + " y pertenecen al DTS: "+ nombreDts, input.getCodArea());
            } else {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_USUARIO_BUZON_838, null,
                        nombreClase, nombreMetodo, null, input.getCodArea());
            }

            output = new OutputUsuarioBuzon();
            output.setRespuesta(respuesta);
            
            return output;
        };
        
        String sql = null;
        String campos[] = CtrlUsuarioBuzon.obtenerCamposGetPost(Conf.METODO_POST);
        List<String> inserts = CtrlUsuarioBuzon.obtenerInsertsPost(input, UsuarioBuzon.SEQUENCE, estadoAlta, ID_PAIS);

        sql = UtileriasBD.armarQueryInsert(UsuarioBuzon.N_TABLA, campos, inserts);

        try {
        	int i=0;
            conn.setAutoCommit(false);
            QueryRunner Qr = new QueryRunner();
            i=Qr.update(conn, sql);

            if (i>0) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_USUARIOBUZON_59, null, nombreClase, nombreMetodo, null, input.getCodArea());

                output = new OutputUsuarioBuzon();
                output.setRespuesta(respuesta);
            }else{
            	respuesta = new Respuesta();
            	respuesta.setCodResultado("0");
            	output = new OutputUsuarioBuzon();
                output.setRespuesta(respuesta);
            }
            conn.commit();
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
     * @return OutputUsuarioBuzon
     * @throws SQLException
     */
    public static OutputUsuarioBuzon doPutDel(Connection conn,
            InputUsuarioBuzon input, int metodo, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "doPutDel";
        String nombreClase = new CurrentClassGetter().getClassName();
              
        Respuesta respuesta = null;
        OutputUsuarioBuzon output = null;
        
        List<Filtro> condicionesExistencia = CtrlUsuarioBuzon.obtenerCondicionesExistencia(input, metodo,  "", ID_PAIS);
        String existencia = UtileriasBD.verificarExistencia(conn, UsuarioBuzon.N_TABLA, condicionesExistencia);
        if (new Integer(existencia) < 1) {
            log.error("No existe el recurso.");
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null,
                nombreClase, nombreMetodo, null, input.getCodArea());
            
            output = new OutputUsuarioBuzon();
            output.setRespuesta(respuesta);

            return output;
        }
        
        String campos[][] = CtrlUsuarioBuzon.obtenerCamposPutDel(input, metodo);
        List<Filtro> condiciones = CtrlUsuarioBuzon.obtenerCondiciones(input, metodo, ID_PAIS);
        
        String sql = "";
        
        if (metodo == Conf.METODO_PUT) {
            sql = UtileriasBD.armarQueryUpdate(UsuarioBuzon.N_TABLA, campos, condiciones);
        } else {
            sql = UtileriasBD.armarQueryDelete(UsuarioBuzon.N_TABLA, condiciones);
        }

        try {
        	int i=0;
            conn.setAutoCommit(false);
            QueryRunner Qr = new QueryRunner();
            i=Qr.update(conn, sql);

            if (i>0) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_USUARIOBUZON_60, null,
                    nombreClase, nombreMetodo, null, input.getCodArea());

                output = new OutputUsuarioBuzon();
                output.setRespuesta(respuesta);
            }else{
            	respuesta = new Respuesta();
            	respuesta.setCodResultado("0");
            	output = new OutputUsuarioBuzon();
                output.setRespuesta(respuesta);
            }
            conn.commit();
        } finally {
            conn.setAutoCommit(true);
          
        }
        
        return output;
    }
}

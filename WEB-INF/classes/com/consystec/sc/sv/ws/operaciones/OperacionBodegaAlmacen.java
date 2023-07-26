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

import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaAlmacen;
import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaDTS;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegaDTS;
import com.consystec.sc.sv.ws.metodos.CtrlBodegaAlmacen;
import com.consystec.sc.sv.ws.orm.AlmacenBod;
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
public class OperacionBodegaAlmacen {
	private OperacionBodegaAlmacen(){}
    private static final Logger log = Logger.getLogger(OperacionBodegaAlmacen.class);
    
    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputBodegaDTS
     * @throws SQLException
     */
    public static OutputBodegaDTS doGet(Connection conn, 
    		InputBodegaDTS input, int metodo, BigDecimal idPais) throws SQLException {
    	String nombreMetodo = "doGet";
    	String nombreClase = new CurrentClassGetter().getClassName();

    	List<InputBodegaAlmacen> list = new ArrayList<InputBodegaAlmacen>();

    	Respuesta respuesta = null;
    	OutputBodegaDTS output = null;
    	PreparedStatement pstmt = null;
    	ResultSet rst = null;

    	String sql = null;
    	String[] campos = CtrlBodegaAlmacen.obtenerCamposGetPost(metodo);
    	List<Filtro> condiciones = CtrlBodegaAlmacen.obtenerCondicionesPadre( input, metodo,idPais);

    	List<Order> orden = new ArrayList<>();
    	orden.add(new Order(AlmacenBod.CAMPO_NOMBRE, Order.ASC));

    	sql = UtileriasBD.armarQuerySelect(AlmacenBod.N_TABLA, campos, condiciones, orden);

    	try {
    		pstmt = conn.prepareStatement(sql);
    		rst = pstmt.executeQuery();

    		if (rst != null) {
    			if (!rst.next()) {
    				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSOS, null,
    						nombreClase, nombreMetodo, null, input.getCodArea());

    				output = new OutputBodegaDTS();
    				output.setRespuesta(respuesta);
    			} else {
    				respuesta = new Respuesta();
    				respuesta=new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
    						nombreClase, nombreMetodo, null, input.getCodArea());

    				//Se obtienen todos los datos hijos.

    				do {
    					InputBodegaAlmacen item = new InputBodegaAlmacen();
    					String idBodega = rst.getString(AlmacenBod.CAMPO_TCSCALMACENBODID);
    					item.setBodegaSCL(rst.getString(AlmacenBod.CAMPO_TC_BODEGA_SCL_ID));
    					item.setIdBodega(idBodega);
    					item.setNombre(rst.getString(AlmacenBod.CAMPO_NOMBRE));
    					item.setDistribuidor(rst.getString(AlmacenBod.CAMPO_TC_SC_DTS_ID));
    					item.setUsuario(input.getUsuario());
    					item.setEstado(rst.getString(AlmacenBod.CAMPO_ESTADO));
    					item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(AlmacenBod.CAMPO_CREADO_EL)));
    					item.setCreado_por(rst.getString(AlmacenBod.CAMPO_CREADO_POR));
    					item.setModificado_el(UtileriasJava.formatStringDate(rst.getString(AlmacenBod.CAMPO_MODIFICADO_EL)));
    					item.setModificado_por(rst.getString(AlmacenBod.CAMPO_MODIFICADO_POR));

    					list.add(item);
    				} while (rst.next());

    				output = new OutputBodegaDTS();
    				output.setRespuesta(respuesta);
    				output.setBodegaDTS(list);
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
	 * @return OutputBodegaDTS
	 * @throws SQLException
	 */
	public static OutputBodegaDTS doPost(Connection conn, InputBodegaDTS input, BigDecimal idPais) throws SQLException {
		String nombreMetodo = "doPost";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta respuesta = null;
		OutputBodegaDTS output = null;
		String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

		// verificando que el DTS no exista en otra bodega
		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones.add(new Filtro(AlmacenBod.CAMPO_TC_SC_DTS_ID, Filtro.EQ, input.getDistribuidor()));
		condiciones.add(new Filtro(AlmacenBod.CAMPO_TC_SC_CATPAIS_ID, Filtro.EQ, idPais));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, AlmacenBod.CAMPO_ESTADO, estadoAlta));
		String existencia = UtileriasBD.verificarExistencia(conn, AlmacenBod.N_TABLA, condiciones);
		// if (new Integer(existencia) > 0) {
		// log.error("Ya existe el recurso.");
		// respuesta = new
		// ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DTS_BODEGA_831, null,
		// nombreClase,
		// nombreMetodo, null, input.getCodArea());
		//
		// output = new OutputBodegaDTS();
		// output.setRespuesta(respuesta);
		//
		// return output;
		// }

		// verificando que la bodega scl no exista en otra bodega existente
		condiciones = new ArrayList<Filtro>();
		condiciones.add(new Filtro(AlmacenBod.CAMPO_TC_BODEGA_SCL_ID, Filtro.EQ, input.getBodegaSCL()));
		condiciones.add(new Filtro(AlmacenBod.CAMPO_TC_SC_CATPAIS_ID, Filtro.EQ, idPais));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, AlmacenBod.CAMPO_ESTADO, estadoAlta));
		existencia = UtileriasBD.verificarExistencia(conn, AlmacenBod.N_TABLA, condiciones);
		if (new Integer(existencia) > 0) {
			log.error("Ya existe el recurso.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_COD_BODEGA_832, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputBodegaDTS();
			output.setRespuesta(respuesta);

			return output;
		}

		// verificando que el nombre no exista en otra bodega existente
		condiciones = new ArrayList<Filtro>();
		condiciones.add(
				UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, AlmacenBod.CAMPO_NOMBRE, input.getNombre()));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, AlmacenBod.CAMPO_ESTADO, estadoAlta));
		condiciones.add(new Filtro(AlmacenBod.CAMPO_TC_SC_CATPAIS_ID, Filtro.EQ, idPais));
		existencia = UtileriasBD.verificarExistencia(conn, AlmacenBod.N_TABLA, condiciones);
		if (new Integer(existencia) > 0) {
			log.error("Ya existe el recurso.");
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NOMBRE_BODEGA_833, null, nombreClase,
					nombreMetodo, null, input.getCodArea());

			output = new OutputBodegaDTS();
			output.setRespuesta(respuesta);

			return output;
		}

		String sql = null;
		String campos[] = CtrlBodegaAlmacen.obtenerCamposGetPost(Conf.METODO_POST);
		List<String> inserts = CtrlBodegaAlmacen.obtenerInsertsPostPadre(input, AlmacenBod.SEQUENCE, idPais);

		sql = UtileriasBD.armarQueryInsert(AlmacenBod.N_TABLA, campos, inserts);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			String generatedColumns[] = { AlmacenBod.CAMPO_TCSCALMACENBODID };
			pstmt = conn.prepareStatement(sql, generatedColumns);

			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();

			if (isValidResultSet(rs)) {

				int idPadre = 0;
				if (rs.next()) {
					idPadre = rs.getInt(1);
				}
				log.debug("idPadre: " + idPadre);

				conn.commit();

				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_BODEGA_VIRTUAL_29, null, nombreClase,
						nombreMetodo, null, input.getCodArea());

				output = new OutputBodegaDTS();
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
     * @return OutputBodegaDTS
     * @throws SQLException
     */
    public static OutputBodegaDTS doPutDel(Connection conn, InputBodegaDTS input, int metodo, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doPutDel";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputBodegaDTS output = null;

        String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

        List<Filtro> condicionesExistencia = CtrlBodegaAlmacen.obtenerCondicionesExistencia(conn, input, metodo, idPais);
        String existencia = UtileriasBD.verificarExistencia(conn, AlmacenBod.N_TABLA, condicionesExistencia);
        if (new Integer(existencia) < 1) {
            log.error("No existe el recurso.");
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());

            output = new OutputBodegaDTS();
            output.setRespuesta(respuesta);

            return output;
        }

        if (metodo == Conf.METODO_PUT) {
            //verificando que el nombre no exista en otra bodega existente.
            condicionesExistencia.clear();
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, AlmacenBod.CAMPO_NOMBRE, input.getNombre()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_NOT_IN_AND, AlmacenBod.CAMPO_TCSCALMACENBODID, input.getIdBodega()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, AlmacenBod.CAMPO_ESTADO, estadoAlta));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AlmacenBod.CAMPO_TC_SC_CATPAIS_ID,idPais.toString()));
            existencia = UtileriasBD.verificarExistencia(conn, AlmacenBod.N_TABLA, condicionesExistencia);
            if (new Integer(existencia) > 0) {
                log.error("Ya existe el nombre.");
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NOMBRE_BODEGA_833, null,
                        nombreClase, nombreMetodo, null, input.getCodArea());

                output = new OutputBodegaDTS();
                output.setRespuesta(respuesta);

                return output;
            }

            //verificando que la bodega scl no exista en otra bodega existente.
            condicionesExistencia.clear();
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AlmacenBod.CAMPO_TC_BODEGA_SCL_ID, input.getBodegaSCL()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_NOT_IN_AND, AlmacenBod.CAMPO_TCSCALMACENBODID, input.getIdBodega()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, AlmacenBod.CAMPO_ESTADO, estadoAlta));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AlmacenBod.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
            existencia = UtileriasBD.verificarExistencia(conn, AlmacenBod.N_TABLA, condicionesExistencia);
            if (new Integer(existencia) > 0) {
                log.error("Ya existe el recurso.");
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_COD_BODEGA_832, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output = new OutputBodegaDTS();
                output.setRespuesta(respuesta);

                return output;
            }

            //verificando que el dts no exista en otra bodega existente.
            condicionesExistencia.clear();
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AlmacenBod.CAMPO_TC_SC_DTS_ID, input.getDistribuidor()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_NOT_IN_AND, AlmacenBod.CAMPO_TCSCALMACENBODID, input.getIdBodega()));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, AlmacenBod.CAMPO_ESTADO, estadoAlta));
            condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AlmacenBod.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
            existencia = UtileriasBD.verificarExistencia(conn, AlmacenBod.N_TABLA, condicionesExistencia);
            if (new Integer(existencia) > 0) {
                log.error("Ya existe el recurso.");
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DTS_BODEGA_831, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output = new OutputBodegaDTS();
                output.setRespuesta(respuesta);

                return output;
            }
        }

        String sql = null;

        String campos[][] = CtrlBodegaAlmacen.obtenerCamposPutDelPadre(input, metodo);
        List<Filtro> condiciones = CtrlBodegaAlmacen.obtenerCondicionesPadre(input, metodo, idPais);

        sql = UtileriasBD.armarQueryUpdate(AlmacenBod.N_TABLA, campos, condiciones);

        try {
        	int i=0;
            conn.setAutoCommit(false);
            QueryRunner Qr = new QueryRunner();
            i=Qr.update(conn, sql);

            if (i>0) {

            	    //aqui se manda a llamar el m\u00E9todo para que realice el cambio tambi\u00E9n en la base de datos local

                    String idDTS = UtileriasBD.getOneRecord(conn, AlmacenBod.CAMPO_TC_SC_DTS_ID, AlmacenBod.N_TABLA, condiciones);
                    condiciones.clear();
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AlmacenBod.CAMPO_TC_SC_DTS_ID, idDTS));
                    sql = UtileriasBD.armarQueryUpdate(AlmacenBod.N_TABLA, campos, condiciones);
                    conn.commit();
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_MODIFICADO, null,
                        nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputBodegaDTS();
                    output.setRespuesta(respuesta);
           }else{
        	   respuesta = new Respuesta();
        	   respuesta.setCodResultado("0");
        	   output = new OutputBodegaDTS();
               output.setRespuesta(respuesta);
           }
        } finally {
          
            conn.setAutoCommit(true);
            
        }

        return output;
    }

    private static boolean isValidResultSet(ResultSet rs) {
    	return rs != null;
    }

}

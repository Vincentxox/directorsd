package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.inventariopromo.InputGetArtPromInventario;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventariopromo.OutputArtPromInventario;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionConsultaArticulosPromocionales {
	private OperacionConsultaArticulosPromocionales(){}
	private static final Logger log = Logger.getLogger(OperacionConsultaPais.class);

	 public static OutputArtPromInventario Articulos(Connection conn, InputGetArtPromInventario input, BigDecimal idPais)
	            throws SQLException

	    {
	        String nombreMetodo = "Articulos";
	        String nombreClase = new CurrentClassGetter().getClassName();

	        List<InputGetArtPromInventario> lstArticulosPromInve = new ArrayList<InputGetArtPromInventario>();
	        InputGetArtPromInventario ObjArticulos = null;
	        Respuesta respuesta = null;
	        OutputArtPromInventario output = null;
	        PreparedStatement pstmt = null;
	        ResultSet rst = null;
	        String sql = "";
	        try {
	        
		        sql = "  SELECT  " + "A.ARTICULO, " + "A.DESCRIPCION, " + "SUM(A.CANTIDAD) AS CANTIDAD, " + "A.ESTADO, "
		                + "A.TCSCBODEGAVIRTUALID, " + "B.NOMBRE " + "FROM TC_SC_INVENTARIO A " + "INNER JOIN "
		                + "TC_SC_BODEGAVIRTUAL B " + "ON A.TCSCBODEGAVIRTUALID = B.TCSCBODEGAVIRTUALID "
		                + "WHERE A.TCSCCATPAISID =" + idPais.toString() + " AND A.TIPO_INV = ('INV_SIDRA') "
		                + " AND A.TCSCBODEGAVIRTUALID = " + input.getIdBodegaVirtual();
	
		        if (!(input.getidArticulo() == null || input.getidArticulo().trim().equals(""))) {
		            sql += " AND A.ARTICULO= " + input.getidArticulo();
		        }
	
		        if (!(input.getDescripcion() == null || input.getDescripcion().trim().equals(""))) {
		            sql += " AND A.DESCRIPCION like'%" + input.getDescripcion() + "%'";
		        }
		        if (!(input.getEstado() == null || input.getEstado().trim().equals(""))) {
		            sql += " AND A.ESTADO='" + input.getEstado() + "'";
		        }
	
		        sql += " GROUP BY A.DESCRIPCION, " + "A.TCSCBODEGAVIRTUALID, " + "A.ESTADO, " + "B.NOMBRE, " + "A.articulo "
		                + "order by a.articulo asc";
	
		        log.debug("Qry consulta de productos promocionales: " + sql);

	      
	            pstmt = conn.prepareStatement(sql);
	            rst = pstmt.executeQuery();
	            if (rst != null) {
	                if (!rst.next()) {

	                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_TITULO_ARTICULOS_NO_EXISTEN_345,
	                            null, nombreClase, nombreMetodo, null, input.getCodArea());
	                    output = new OutputArtPromInventario();
	                    output.setRespuesta(respuesta);
	                } else {
	                    log.trace("Datos obtenidos.");
	                    respuesta = new Respuesta();

	                    respuesta=new ControladorBase()
	                            .getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, input.getCodArea());
	                    output = new OutputArtPromInventario();
	                    output.setIdBodegaVirtual(rst.getString(Inventario.CAMPO_TCSCBODEGAVIRTUALID));
	                    output.setNombreBodegaVirtual(rst.getString(BodegaVirtual.CAMPO_NOMBRE));

	                    do {
	                        ObjArticulos = new InputGetArtPromInventario();
	                        ObjArticulos.setidArticulo(rst.getString(Inventario.CAMPO_ARTICULO));
	                        ObjArticulos.setDescripcion(rst.getString(Inventario.CAMPO_DESCRIPCION));
	                        ObjArticulos.setCantidadArticulo(rst.getString("CANTIDAD"));
	                        ObjArticulos.setEstado(rst.getString(Inventario.CAMPO_ESTADO));

	                        lstArticulosPromInve.add(ObjArticulos);
	                    } while (rst.next());

	                    if (output.getNombreBodegaVirtual() != null) {
	                        output.setArticulosPromo(lstArticulosPromInve);
	                        respuesta.setCodResultado("1");
	                        respuesta.setMostrar("0");
	                    } else {
	                        log.trace("Datos no encontrados.");
	                        respuesta = new Respuesta();
	                        respuesta.setToken("");
	                        respuesta=new ControladorBase()
	                                .getMensaje(Conf_Mensajes.MSJ_TITULO_ARTICULOS_NO_EXISTEN_345, null, nombreClase,
	                                        nombreMetodo, null, input.getCodArea());

	                    }
	                    output.setRespuesta(respuesta);
	                }
	            }
	        }

	        finally {
	            DbUtils.closeQuietly(rst);
	            DbUtils.closeQuietly(pstmt);

	        }

	        return output;
	    }

	 public static int validarBodega(Connection conn, InputGetArtPromInventario input, BigDecimal idPais) throws SQLException {
	        String sql = null;
	        int ExisteBodega = 0;
	        PreparedStatement pstmt = null;
	        ResultSet rst = null;
	        String campos[] = { BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, BodegaVirtual.CAMPO_NOMBRE };
	        List<Filtro> condiciones = new ArrayList<Filtro>();
	        List<Order> orden = new ArrayList<Order>();

	        try {
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
	                    BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getIdBodegaVirtual().toString()));
	            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TCSCCATPAISID,
	                    idPais.toString()));
	            orden.add(new Order(BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, Order.ASC));
	            sql = UtileriasBD.armarQuerySelect(BodegaVirtual.N_TABLA, campos, condiciones, orden);
	            pstmt = conn.prepareStatement(sql);
	            rst = pstmt.executeQuery();
	            if (rst != null) {
	                if (!rst.next()) {
	                    ExisteBodega = 0;
	                } else {
	                    ExisteBodega = 1;
	                }
	            }
	        } finally {
	        	DbUtils.closeQuietly(rst);
	        	DbUtils.closeQuietly(pstmt);
	        }
	        return ExisteBodega;
	    }

}

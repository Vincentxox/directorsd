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

import com.consystec.sc.sv.ws.orm.ConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.SolicitudDet;

public class OperacionDevolucion {
	private OperacionDevolucion(){}
    private static final Logger log = Logger.getLogger(OperacionDevolucion.class);

	/***
	 * M\u00E9todo para verificar si la solicitud ingresada existe, se retorna una lista string
	 * Index 0 --> devuelve 1 si existe
	 * Index 1 --> devuelve el tipo de solicitud, esto servira para verificar que sea una DEVOLUCION 
	 **/
    public static List<String> existeSol(Connection conn, BigDecimal idSolicitud, String estadoAlta,
            String alta) throws SQLException {
        List<String> lista = new ArrayList<String>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;

     
			String query = "SELECT COUNT (*), S.tipo_solicitud, nvl(S.tcscbodegavirtualid,0), " +
					"	( SELECT COUNT(*) FROM TC_SC_SOLICITUD_DET WHERE TCSCSOLICITUDID=? AND upper(ESTADO)=?), S.ESTADO, S.tcscbuzonid,"+
					"         (SELECT B.NIVEL " +
					"            FROM tc_Sc_buzonsidra b " +
					"           WHERE     B.TCSCBUZONID = S.TCSCBUZONID " +
					"                 AND upper(B.ESTADO) = ? " +
					"                 AND S.TCSCSOLICITUDID =?) " +
					"            NIVEL, S.tcscdtsid, " +
					"	(SELECT TIPO FROM TC_Sc_DTS WHERE TCSCDTSID=S.TCSCDTSID) "+
					"    FROM tc_sc_solicitud  S" +
					"   WHERE S.tcscsolicitudid = ?"+
					" GROUP BY S.tipo_solicitud,S.tcscbodegavirtualid,S.estado, S.tcscbuzonid,S.tcscsolicitudid, S.tcscdtsid " ;

            log.trace("query para validar que existe solicitud: " + query);
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1,idSolicitud);
            pstmt.setString(2, estadoAlta.toUpperCase());
            pstmt.setString(3,alta.toUpperCase());
            pstmt.setBigDecimal(4,idSolicitud);
            pstmt.setBigDecimal(5,idSolicitud);
            rst = pstmt.executeQuery();

            if (rst.next()) {
	            lista.add(0, rst.getString(1));
	            lista.add(1, rst.getString(2));
	            lista.add(2, rst.getString(3));
	            lista.add(3, rst.getString(4));
	            lista.add(4, rst.getString(5));
	            lista.add(5, rst.getString(6));
	            lista.add(6, rst.getString(7));// nivel de buzon en el que solicitud se encuentra actualmente
	            lista.add(7, rst.getString(8));// id del distribuidor para la notificacion por correo
	            lista.add(8, rst.getString(9)); //tipo de distribuidor (INTERNO/EXTERNO)
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return lista;
    }

    public static BigDecimal getBodPapa(Connection conn, String idSolicitud, String nivelBodega) throws SQLException {
        BigDecimal ret = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String query ="";
   
            if (nivelBodega == null) {
                query =   "SELECT tcscbodegavirtualid " +
    					"  FROM tc_sc_dts " +
    					" WHERE tcscdtsid = (SELECT tcscdtsid " +
    					"                      FROM tc_Sc_solicitud " +
                        "                     WHERE tcscsolicitudid =?) " ;
            } else { //en caso de ser nivel 2 se buscar\u00E9 la bodega que tiene asociada la ruta o panel, ya que al ser zona comercial la ruta o panel siempre
            		//pertenecera a una bodega de nivel 2
                query="SELECT CASE " +
				"          WHEN S.TIPO = 'RUTA' " +
				"          THEN " +
				"             (SELECT TCSCBODEGAVIRTUALID " +
				"                FROM tc_sc_ruta " +
				"               WHERE tcscrutaid = s.idtipo AND estado = 'ALTA') " +
				"          WHEN S.TIPO = 'PANEL' " +
				"          THEN " +
				"             (SELECT TCSCBODEGAVIRTUALID " +
				"                FROM tc_sc_panel " +
				"               WHERE tcscpanelid = s.idtipo AND estado = 'ALTA') " +
				"       END " +
				"          bodegavirtual " +
				"  FROM tc_sc_solicitud s " +
				" WHERE tcscsolicitudid =  ?"; 

            }
 
        try {   
          
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, new BigDecimal(idSolicitud));
            rst = pstmt.executeQuery();
            log.trace("query para obtener bodega papa: " + query);
            if (rst.next()) {
                ret = rst.getBigDecimal(1);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return ret;
    }

	/**
	 * M\u00E9todo para obtener los art\u00EDculos que esten disponibles en la bodega pap\u00E9 en caso de que los art\u00EDculos de devolucion sean por cantidad
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
    public static List<Inventario> getInvDisponibles(Connection conn, String estadoAlta, String estadoDisponible,
            String idSolicitud, String idBodegaPapa) throws SQLException {
        List<Inventario> lista = new ArrayList<Inventario>();
        Inventario obj = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
       
	        String query = "SELECT * " +
					"  FROM TC_SC_INVENTARIO " +
					" WHERE     upper(ESTADO) = ?" +
					"       AND articulo IN (SELECT articulo " +
					"                          FROM TC_SC_SOLICITUD_dET " +
					"                         WHERE TCSCSOLICITUDID =?"+
					"                         AND upper(ESTADO)=?) " +
					"       AND TIPO_INV IN (SELECT TIPO_INV " +
					"                          FROM TC_SC_SOLICITUD_dET " +
					"                         WHERE TCSCSOLICITUDID = ? AND upper(ESTADO)=?) " +
					"       AND tcscbodegavirtualid =?";
	     try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1,estadoDisponible.toUpperCase() );
            pstmt.setBigDecimal(2, new BigDecimal(idSolicitud));
            pstmt.setString(3, estadoAlta.toUpperCase());
            pstmt.setBigDecimal(4, new BigDecimal(idSolicitud));
            pstmt.setString(5, estadoAlta.toUpperCase());
            pstmt.setBigDecimal(6, new BigDecimal(idBodegaPapa));
            rst = pstmt.executeQuery();
            log.trace("query para obtener articulos de solicitud en el inventario: " + query);
            while (rst.next()) {
	            obj = new Inventario();
	            obj.setArticulo(rst.getBigDecimal(Inventario.CAMPO_ARTICULO));
	            obj.setCantidad(rst.getBigDecimal(Inventario.CAMPO_CANTIDAD));
	            obj.setDescripcion(rst.getString(Inventario.CAMPO_DESCRIPCION));
	            obj.setEstado(rst.getString(Inventario.CAMPO_ESTADO));
	            obj.setEstado_comercial(rst.getString(Inventario.CAMPO_ESTADO_COMERCIAL));
	            obj.setSeriado(rst.getString(Inventario.CAMPO_SERIADO));
	            obj.setSerie(rst.getString(Inventario.CAMPO_SERIE));
	            obj.setTcscbodegavirtualid(rst.getBigDecimal(Inventario.CAMPO_TCSCBODEGAVIRTUALID));
	            obj.setTcscinventarioinvid(rst.getBigDecimal(Inventario.N_TABLA_ID));
	            obj.setTcscsolicitudid(rst.getBigDecimal(Inventario.CAMPO_TCSCSOLICITUDID));
	            obj.setTipo_inv(rst.getString(Inventario.CAMPO_TIPO_INV));
                obj.setTipo_grupo(rst.getString(Inventario.CAMPO_TIPO_GRUPO));
                obj.setIdVendedor(rst.getBigDecimal(Inventario.CAMPO_IDVENDEDOR));

                lista.add(obj);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return lista;
    }

	/**
	 * M\u00E9todo para obtener el inventario que se encuentra en proceso devolucion de una  solicitud
	 * @param conn
	 * @param idSolicitud
	 * @param estadoProcDevolucion
	 * @param idBodVirtual
	 * @return
	 * @throws SQLException
	 */	
    public static List<Inventario> getInvSolicitud(Connection conn, String idSolicitud, String estadoProcDevolucion) throws SQLException {
        List<Inventario> lista = new ArrayList<Inventario>();
        Inventario obj = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
   
         
			 String query="SELECT "
			 		+ "		TCSCSOLICITUDID,"
			 		+"		TCSCSOLICITUDDETID,"
			 		+ "		ARTICULO, " +
					 "       SERIE, " +
					 "       SERIE_ASOCIADA, " +
					 "       SERIE_FINAL, " +
					 "       CANTIDAD, " +
					 "       DECODE (SERIE,NULL, 0, 1) SERIADO, " +
					 "       TIPO_INV " +
					 "  FROM TC_SC_SOLICITUD_DET " +
					 " WHERE TCSCSOLICITUDID =? 	AND upper(ESTADO)= ? ";
		try {	
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, new BigDecimal(idSolicitud));
            pstmt.setString(2, estadoProcDevolucion);
            rst = pstmt.executeQuery();
            log.trace("query2 para obtener articulos de solicitud en el inventario: " + query);
            while (rst.next()) {
                obj = new Inventario();
                obj.setArticulo(rst.getBigDecimal(Inventario.CAMPO_ARTICULO));
                obj.setCantidad(rst.getBigDecimal(Inventario.CAMPO_CANTIDAD));
                obj.setSeriado(rst.getString(Inventario.CAMPO_SERIADO));
                log.trace("campo seriado art\u00EDculo:" + obj.getArticulo() + "; seriado:" + obj.getSeriado());
                obj.setSerie(rst.getString("SERIE"));
                log.trace("serie:" + obj.getSerie());
                obj.setTcscsolicitudid(rst.getBigDecimal(Inventario.CAMPO_TCSCSOLICITUDID));
                obj.setTipo_inv(rst.getString(Inventario.CAMPO_TIPO_INV));
                obj.setSeriefinal(rst.getString("SERIE_FINAL"));
				//aqui estoy asignado el id del detalle de la solicitud, esto es util para compararlo con el input del ws 
                obj.setTcscinventarioinvid(rst.getBigDecimal("TCSCSOLICITUDDETID"));
                lista.add(obj);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return lista;
    }

    /**
     * Actualizando el detalle de una solicitud tipo DEVOLUCION
     * 
     * @param conn
     * @param obj
     * @throws SQLException
     */
    public static void updateDetSolicitud(Connection conn, SolicitudDet obj) throws SQLException {
        PreparedStatement pstmt = null;
        String condicion = "";
     
            if (!(obj.getArticulo() == null || obj.getArticulo().equals(""))) {
                condicion = " AND ARTICULO =" + obj.getArticulo();
            } else if (!(obj.getCod_dispositivo() == null || obj.getCod_dispositivo().equals(""))) {
                condicion = " AND UPPER(COD_DISPOSITIVO) ='" + obj.getCod_dispositivo().toUpperCase() + "'";
            }

			String query="UPDATE TC_SC_SOLICITUD_DET " +
					"   SET OBSERVACIONES =?, " +
					"       ESTADO =?, " +
					"       MODIFICADO_EL = SYSDATE, " +
					"       MODIFICADO_POR = ? " +
					" WHERE TCSCSOLICITUDDETID = ?"+condicion; 

            log.trace("update detalle Solicitud:" + query);
         try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, obj.getObservaciones());
            pstmt.setString(2, obj.getEstado());
            pstmt.setString(3, obj.getModificado_por());
            pstmt.setBigDecimal(4, obj.getTcscsolicituddetid());
            pstmt.executeUpdate();
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }

    /**
     * M\u00E9todo para actualizar la cantidad de inventario de un art\u00EDculo, tipo: si
     * se envia 1 se suma cantidad, si se envia 2 si es para cambiar de bodega
     * 
     * @throws SQLException
     */
    public static void updateCantInventario(Connection conn, String idArticulo, String cantidad, String estadoInv,
            String nuevoEstado, String idBodVirtual, String tipoInv, String usuario, int tipo, String idSolicitud,
            String serie) throws SQLException {
        String update = "";
        String condicionSolicitud = "";
        PreparedStatement pstmt = null;
      
            if (idSolicitud == null || "".equals(idSolicitud)) {
                idSolicitud = "";
            } else {
                condicionSolicitud = " AND TCSCSOLICITUDID = "+idSolicitud;
            }

            if (!(serie == null || "".equals(serie.trim()))) {
                condicionSolicitud += "	AND SERIE ='" + serie + "'";
            }
			 
            update=armaQueryUpd( tipo,  condicionSolicitud);
            log.trace("paramétro cantidad o bodega papa:" + cantidad + ",idBodegaVirtual:" + idBodVirtual + ", idSolicitud:" + idSolicitud);
            log.trace("NUEVO ESTADO:" + nuevoEstado + ", ESTADO INVENTARIO:" + estadoInv);
        try { 
            pstmt = conn.prepareStatement(update);
            log.trace("update inventario:" + update);
            pstmt.setBigDecimal(1, new BigDecimal(cantidad));
            pstmt.setString(2, nuevoEstado);
            pstmt.setString(3, usuario);
            pstmt.setBigDecimal(4, new BigDecimal(idArticulo));
            pstmt.setBigDecimal(5, new BigDecimal(idBodVirtual));
            pstmt.setString(6, tipoInv);
            pstmt.setString(7, estadoInv);
            pstmt.executeQuery();
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }

    
    public static String armaQueryUpd(int tipo, String condicionSolicitud) {
    	//suma cantidad
    	String update="";
    	
		if(tipo==1){
			update="UPDATE TC_SC_INVENTARIO " +
					"   SET CANTIDAD = CANTIDAD + ?, ESTADO=?, MODIFICADO_POR=?, MODIFICADO_EL=SYSDATE, TCSCSOLICITUDID=NULL " +
					" WHERE     ARTICULO = ? " +
					"       AND TCSCBODEGAVIRTUALID = ? " +
					"       AND TIPO_INV = ? " +
					"       AND ESTADO = ? "+ 
					condicionSolicitud ;

		}
        //
        if (tipo == 2) {
			update="UPDATE TC_SC_INVENTARIO " +
					"   SET TCSCBODEGAVIRTUALID=?, ESTADO=?, MODIFICADO_POR=?, MODIFICADO_EL=SYSDATE, TCSCSOLICITUDID=NULL " +
					" WHERE     ARTICULO = ? " +
					"       AND TCSCBODEGAVIRTUALID = ? " +
					"       AND TIPO_INV = ? " +
					"       AND ESTADO = ? "+
					condicionSolicitud ;
        }
        if (tipo == 3) {
			update="UPDATE TC_SC_INVENTARIO " +
					"   SET TCSCBODEGAVIRTUALID=?, ESTADO=?, MODIFICADO_POR=?, MODIFICADO_EL=SYSDATE, TCSCSOLICITUDID=NULL, IDVENDEDOR=NULL " +
					" WHERE     ARTICULO = ? " +
					"       AND TCSCBODEGAVIRTUALID = ? " +
					"       AND TIPO_INV = ? " +
					"       AND ESTADO = ? " +
					
					condicionSolicitud ;
        }
        return update;
    }
    /**
     * M\u00E9todo para actualizar series que vienen en incluidas en un rango
     * 
     * @param conn
     * @param idArticulo
     * @param cantidad
     * @param estadoInv
     * @param nuevoEstado
     * @param idBodVirtual
     * @param tipoInv
     * @param usuario
     * @param tipo
     * @param idSolicitud
     * @param serie
     * @throws SQLException
     */
    public static void updateRango(Connection conn, String idArticulo, String cantidad, String estadoInv,
            String nuevoEstado, String idBodVirtual, String tipoInv, String usuario,  String idSolicitud,
            String serie, boolean updateVendedor) throws SQLException {
        String update = "";
        PreparedStatement pstmt = null;
        	
        	if (updateVendedor)
        	{
    	        update = "UPDATE TC_SC_INVENTARIO " +
    						"   SET TCSCBODEGAVIRTUALID=?, ESTADO=?, MODIFICADO_POR=?, MODIFICADO_EL=SYSDATE, TCSCSOLICITUDID=null, IDVENDEDOR=null " +
    						" WHERE     ARTICULO = ? " +
    						"       AND TCSCBODEGAVIRTUALID = ? " +
    						"       AND TIPO_INV = ? " +
    						"       AND ESTADO = ? " +
    						"       AND TCSCSOLICITUDID = ? "+
    						"       AND SERIE = ? " ;
        	}else{
    	        update = "UPDATE TC_SC_INVENTARIO " +
    						"   SET TCSCBODEGAVIRTUALID=?, ESTADO=?, MODIFICADO_POR=?, MODIFICADO_EL=SYSDATE, TCSCSOLICITUDID=null" +
    						" WHERE     ARTICULO = ? " +
    						"       AND TCSCBODEGAVIRTUALID = ? " +
    						"       AND TIPO_INV = ? " +
    						"       AND ESTADO = ? " +
    						"       AND TCSCSOLICITUDID = ? "+
    						"       AND SERIE = ? " ;
        	}
        
        try {

            pstmt = conn.prepareStatement(update);
            log.trace("update inventario:" + update);
            pstmt.setBigDecimal(1, new BigDecimal(cantidad));
            pstmt.setString(2, nuevoEstado);
            pstmt.setString(3, usuario);
            pstmt.setBigDecimal(4, new BigDecimal(idArticulo));
            pstmt.setBigDecimal(5, new BigDecimal(idBodVirtual));
            pstmt.setString(6, tipoInv);
            pstmt.setString(7, estadoInv);
            pstmt.setBigDecimal(8, new BigDecimal(idSolicitud));
            pstmt.setString(9, serie);
            log.trace("update RANGO:" + update);

            pstmt.executeQuery();
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }

    public static void updateSolicitud(Connection conn, String estado, String usuario, String idSolicitud,
            String observaciones, BigDecimal idBuzon, String buzonAnterior) throws SQLException {
        String addAnterior = "";

        if (buzonAnterior == null || "".equals(buzonAnterior)) {
            addAnterior = " ";
        } else {
            addAnterior = "	buzon_Anterior=" + buzonAnterior + ",";
        }

        String query =queryUpdSolicitud( addAnterior );
        String obsv = "";

        if (observaciones == null || observaciones.equals("")) {
            obsv = "";
        } else {
            obsv = ". OBSERVACION ACEPTAR_RECHAZAR DEVOLUCION: " + observaciones;
        }
        PreparedStatement pstmt = null;
        try {
            log.trace("update solicitud observaciones:" + observaciones);
            log.trace("Update solicitud:" + query);
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, estado);
            pstmt.setString(2, usuario);
            pstmt.setString(3, obsv);
            pstmt.setBigDecimal(4, idBuzon);
            pstmt.setBigDecimal(5, new BigDecimal(idSolicitud));

            pstmt.executeQuery();
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }
    
    public static String queryUpdSolicitud(String addAnterior ) {
    	 String query = "UPDATE TC_SC_SOLICITUD SET ESTADO=?, MODIFICADO_POR=?,  OBSERVACIONES=OBSERVACIONES || ?, TCSCBUZONID=?, "
                 + addAnterior + "   MODIFICADO_EL=SYSDATE  WHERE TCSCSOLICITUDID=? ";
    	 return query;
    }

    /**
     * M\u00E9todo para obtener los dipositivos de una solicitud de siniestro a
     * aceptar o rechazar
     * 
     * @param conn
     * @param idSolicitud
     * @param estado
     * @return
     * @throws SQLException
     */
    public static List<SolicitudDet> getDetalleDispositivos(Connection conn, String idSolicitud, String estado)
            throws SQLException {
        List<SolicitudDet> lstDispositivos = new ArrayList<SolicitudDet>();
        SolicitudDet obj = new SolicitudDet();
        PreparedStatement pstmt = null;
        ResultSet rst = null;

      
	        String query = "SELECT * " +
				"  FROM tc_Sc_solicitud_Det " +
				" WHERE     tcscsolicitudid =?"+
				"       AND articulo IS NULL " +
				"       AND cod_dispositivo IS NOT NULL " +
				"       AND upper(estado) =?";
            log.trace("query para obtener dispositivos de solicitud: " + query);
       try { 
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, new BigDecimal(idSolicitud));
            pstmt.setString(2, estado.toUpperCase());
            rst = pstmt.executeQuery();

            while (rst.next()) {
                obj = new SolicitudDet();
                obj.setTcscsolicituddetid(rst.getBigDecimal(SolicitudDet.CAMPO_TCSCSOLICITUDDETID));
                obj.setTcscsolicitudid(rst.getBigDecimal(SolicitudDet.CAMPO_TCSCSOLICITUDID));
                obj.setCod_dispositivo(rst.getString(SolicitudDet.CAMPO_COD_DISPOSITIVO));
                obj.setEstado(rst.getString(SolicitudDet.CAMPO_ESTADO));
                lstDispositivos.add(obj);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return lstDispositivos;
    }

    /**
     * M\u00E9todo para actualizar el estado de un dispositivo en proceso de
     * siniestro
     * 
     * @param conn
     * @param estadoSiniestro
     * @param estadoProceso
     * @param codDispositivo
     * @throws SQLException
     */
    public static void updateDispositivo(Connection conn, String estadoSiniestro, String estadoProceso,
            String codDispositivo) throws SQLException {
        PreparedStatement pstmt = null;
        String query = "UPDATE TC_SC_DISPOSITIVO SET ESTADO=? WHERE upper(CODIGO_DISPOSITIVO)=? AND upper(ESTADO)=?";
        log.trace("update dispositivo:" + query);
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, estadoSiniestro);
            pstmt.setString(2,  codDispositivo.toUpperCase());
            pstmt.setString(3,estadoProceso.toUpperCase() );
            pstmt.executeUpdate();
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }

    /**
     * M\u00E9todo para actualizar folios que se encuentran en estado proceso de
     * siniestro
     * 
     * @param conn
     * @param tipo
     * @param codDispositivo
     * @param estadoSiniestro
     * @param estadoProceso
     * @param tipoUpdate
     *            si tiene valor 0 indica que es un update para todos los folios
     *            aceptados en siniestro si es 1 es porque fueron rechazados
     * @throws SQLException
     */
    public static void updateFolio(Connection conn, String tipo, String codDispositivo, String estadoSiniestro,
            String estadoProceso, String idFolio, int tipoUpdate) throws SQLException {
        PreparedStatement pstmt = null;

    
            String folio = "";
            if (tipoUpdate == 1) {
                folio = " AND TCSCFOLIOID=" + idFolio;
            }
	        String query =   queryUpFolio( folio);

	    try {
	        log.trace("update folio:" + query);
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, estadoSiniestro);
            pstmt.setString(2, tipo.toUpperCase());
            pstmt.setString(3, codDispositivo.toUpperCase() );
            pstmt.setString(4,estadoProceso.toUpperCase() );
            pstmt.executeUpdate();
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }
    
    public static String queryUpFolio(String folio) {
    	  String query = "UPDATE TC_SC_FOLIO SET ESTADO=?  WHERE upper(TIPO)=? AND upper(IDTIPO)=? AND upper(ESTADO)=?" + folio;
    	  return query;
    }

    public static List<ConfiguracionFolioVirtual> getFoliosSiniestro(Connection conn, String estadoProceso, String tipo,
            String codDispositivo) throws SQLException {
        List<ConfiguracionFolioVirtual> lstFolios = new ArrayList<ConfiguracionFolioVirtual>();
        ConfiguracionFolioVirtual objFolio = new ConfiguracionFolioVirtual();
        PreparedStatement pstmt = null;
        ResultSet rst = null;

       
	        String query = "SELECT * " +
				"  FROM TC_SC_FOLIO " +
				" WHERE UPPER (IDTIPO) = ? AND UPPER (TIPO) =? AND UPPER (ESTADO) =?" ;
	    try {
	        pstmt = conn.prepareStatement(query);
	        pstmt.setString(1, codDispositivo.toUpperCase());
	        pstmt.setString(2, tipo.toUpperCase());
	        pstmt.setString(3, estadoProceso.toUpperCase());
	        rst = pstmt.executeQuery();
	        log.trace("query para obtener folios en siniestro: " + query);
            while (rst.next()) {
                objFolio = new ConfiguracionFolioVirtual();
                objFolio.setTcScFolioId(rst.getBigDecimal(ConfiguracionFolioVirtual.CAMPO_TC_SC_FOLIO_ID));
                objFolio.setCant_utilizados(rst.getString(ConfiguracionFolioVirtual.CAMPO_CANT_UTILIZADOS));
                objFolio.setIdTipo(rst.getString(ConfiguracionFolioVirtual.CAMPO_ID_TIPO));
                objFolio.setTipo(rst.getString(ConfiguracionFolioVirtual.CAMPO_TIPO));
                lstFolios.add(objFolio);

            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }
        return lstFolios;
    }

    public static BigDecimal getPendientes(Connection conn, String solicitud, String estado) throws SQLException {
        BigDecimal cant = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        
	        String query="SELECT COUNT (1) " +
				"  FROM TC_SC_SOLICITUD_DET " +
				" WHERE TCSCSOLICITUDID = ? AND UPPER (ESTADO)=?" ;
	    
	   try {
            log.trace("Query para saber si hay articulos o dispositivos pendientes:" + query);
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, new BigDecimal(solicitud));
            pstmt.setString(2, estado.toUpperCase());
            rst = pstmt.executeQuery();

            if (rst.next()) {
                cant = rst.getBigDecimal(1);
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }

        return cant;
    }
    
    
    public static void updateInvCantJornada(Connection conn, String vendedor, String articulo,
            int cant, String usuario, String tipoInv, BigDecimal idPais) throws SQLException {
          PreparedStatement pstmt = null;

         
  	        String sql = "UPDATE tc_sC_cant_inv_jornada SET  CANT_DEVOLUCION=CANT_DEVOLUCION+?, MODIFICADO_EL=SYSDATE, MODIFICADO_POR=?"
  	                + " WHERE idjornada_responsable=(SELECT NVL(JORNADA_RESPONSABLE, TCSCJORNADAVENID) FROM TC_Sc_JORNADA_VEND WHERE VENDEDOR=? and estado='INICIADA') AND ARTICULO=? AND TIPO_INV=? AND TCSCCATPAISID=?";
  	        log.debug("update inv cantidad JORNADA: " + sql);
          try {
  	        pstmt = conn.prepareStatement(sql);
  	        pstmt.setInt(1,cant);
  	        pstmt.setString(2, usuario);
  	        pstmt.setBigDecimal(3, new BigDecimal(vendedor));
  	        pstmt.setBigDecimal(4, new BigDecimal(articulo));
  	        pstmt.setString(5, tipoInv);
  	        pstmt.setBigDecimal(6, idPais);
              pstmt.execute();
          } finally {
              DbUtils.closeQuietly(pstmt);
          }
      }
}

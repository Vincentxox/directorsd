package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.ms.seguridad.excepciones.ExcepcionSeguridad;
import com.consystec.sc.ca.ws.input.panel.InputPanel;
import com.consystec.sc.ca.ws.input.ruta.InputVendedor;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.panel.OutputPanel;
import com.consystec.sc.sv.ws.orm.BodegaVendedor;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.NumRecarga;
import com.consystec.sc.sv.ws.orm.Panel;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.VendedorPDV;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;


public class OperacionPanel {
	private OperacionPanel(){}
    private static final Logger log = Logger.getLogger(OperacionPanel.class);

    public static int insertarPanel(Connection conn, Panel objeto) throws SQLException {
        PreparedStatement pstmt = null;
        int ret;
       
	        String insert=" INSERT INTO TC_SC_PANEL(tcscpanelid,tcscdtsid,nombre,estado,recargas,creado_el,creado_por,tcscbodegavirtualid,tcsccatpaisid) VALUES (?,?,?,?,?,sysdate,?,?,?)";  
	        		
	    try {
            pstmt = conn.prepareStatement(insert);
            pstmt.setBigDecimal(1, objeto.getTcscpanelid());
            pstmt.setBigDecimal(2, objeto.getTcscdtsid());
            pstmt.setString(3, objeto.getNombre());
            pstmt.setString(4, objeto.getEstado());
            pstmt.setString(5, objeto.getRecargas());
            pstmt.setString(6, objeto.getCreado_por());
            pstmt.setBigDecimal(7, objeto.getTcscbodegavirtualid());
            pstmt.setBigDecimal(8, objeto.getTcsccatpaisid());

            ret = pstmt.executeUpdate();
        } finally {
            DbUtils.closeQuietly(pstmt);
        }

        return ret;
    }

    public static int updatePanel(Connection conn, Panel objeto) throws SQLException {
        PreparedStatement pstmt = null;
        int ret;
      
	        String insert = " UPDATE TC_SC_PANEL SET tcscdtsid = ?, nombre = ?, estado = ?, recargas = ?, tcscbodegavirtualid = ?, modificado_el = SYSDATE, modificado_por = ? WHERE tcscpanelid = ? AND tcsccatpaisid = ?";  
	        		
	   
	     try{
	        pstmt = conn.prepareStatement(insert);
	
	        pstmt.setBigDecimal(1, objeto.getTcscdtsid());
	        pstmt.setString(2, objeto.getNombre());
	        pstmt.setString(3, objeto.getEstado());
	        pstmt.setString(4, objeto.getRecargas());
	        pstmt.setBigDecimal(5, objeto.getTcscbodegavirtualid());
	        pstmt.setString(6, objeto.getModificado_por());
	        pstmt.setBigDecimal(7, objeto.getTcscpanelid());
	        pstmt.setBigDecimal(8, objeto.getTcsccatpaisid());
	    	
	        
	        
	        ret = pstmt.executeUpdate();
        }finally{
        	DbUtils.closeQuietly(pstmt);
        }

        return ret;
    }

    /**
     * M\u00E9todo utilizado para cuando se cambie de estado un pdv
     * 
     * @throws SQLException
     **/
    public static void updateTable(Connection conn, String tabla, String estado, BigDecimal idPDV, BigDecimal idPais) throws SQLException {
        PreparedStatement pstmt = null;
        StringBuilder query = new StringBuilder();
        query.append( "UPDATE ");
        query.append( tabla );
        query.append(" set estado=? where TCSCPANELID=? and tcsccatpaisid=?");
        log.trace("update estado PANEL:" + query);
       try{
    	   pstmt = conn.prepareStatement(query.toString());
    	   pstmt.setString(1, estado);
    	   pstmt.setBigDecimal(2,idPDV);
    	   pstmt.setBigDecimal(3, idPais);
    	   pstmt.executeUpdate();
        }finally{
        	DbUtils.closeQuietly(pstmt);
        }
    }

    /**
     * M\u00E9todo para cambiar de estado un VENDEDOR DE PANEL
     * 
     * @throws SQLException
     **/
    public static void updateVendedor(Connection conn, String tabla, String estado, BigDecimal id, String panel,
            String vendedor, String usuario) throws SQLException {
        PreparedStatement pstmt = null;
        StringBuilder query = new StringBuilder();
        query.append( "UPDATE ");
        query.append( tabla );
        query.append(" set estado=?, modificado_el=sysdate, modificado_por=? where idTipo=? and tipo=?' and vendedor in(");
        query.append( vendedor + ")");
	        log.trace("update estado PANEL:" + query);
	     try{
	        pstmt = conn.prepareStatement(query.toString());
	        pstmt.setString(1, estado.toUpperCase());
	        pstmt.setString(2, usuario);
	        pstmt.setBigDecimal(1, id);
	        pstmt.setString(4, panel);
	        
	        pstmt.executeUpdate();
	    }finally{
	    	DbUtils.closeQuietly(pstmt);
	    }
    }
    
    /**
     * Metodo para actualizar los datos de los vendedores asociados a panel.
     * 
     * @param conn
     * @param idTipo
     * @param tipoPanel
     * @param lstVendedores
     * @param usuario
     * @param estadoAlta
     * @throws SQLException
     */
    public static void updateVendedores(Connection conn, BigDecimal idTipo, String tipoPanel,
            List<VendedorPDV> lstVendedores, String usuario, String estadoAlta) throws SQLException {
        Statement stmt = null;
        int[] updateCounts;
        String queryFinal="";
        try{
	        stmt = conn.createStatement();
	        for (int i = 0; i < lstVendedores.size(); i++) {
	            String[][] campos={
	                { VendedorPDV.CAMPO_RESPONSABLE, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, lstVendedores.get(i).getResponsable().toString()) },
	                { VendedorPDV.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estadoAlta) },
	                { VendedorPDV.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
	                { VendedorPDV.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, usuario) }
	            };
	            
	            String qry =queryVendedoresupd(  campos, idTipo,  tipoPanel,
	            		lstVendedores.get(i).getVendedor()) ;
	            
	            queryFinal=qry;
	            log.trace("Update Vendedores:" + queryFinal);
	            
	            stmt.addBatch(queryFinal);
	        }
	
	        updateCounts = stmt.executeBatch();
        }finally{
        	DbUtils.closeQuietly(stmt);
        }
        
        log.debug("Update Vendedores Vend?" + UtileriasJava.validarBatch(1, updateCounts));
    }

    public static String queryVendedoresupd( String[][] campos,BigDecimal idTipo, String tipoPanel,
           BigDecimal vendedor) {
    	  String qry = UtileriasBD.armarQueryUpdate(VendedorPDV.N_TABLA, campos, null);            
          qry += " WHERE " + VendedorPDV.CAMPO_IDTIPO + " = " + idTipo
              + " AND UPPER(" + VendedorPDV.CAMPO_TIPO + ") = '" + tipoPanel.toUpperCase() + "'"
              + " AND " + VendedorPDV.CAMPO_VENDEDOR + " = " +vendedor;
          return qry;
          
    }
    /**
     * M\u00E9todo para verificar si Vendedor ya Existe en otra panel
     * 
     * @throws SQLException
     */
    public static List<BigDecimal> existeVendedor(Connection conn, BigDecimal idVendedor, BigDecimal idPanel, String tipo, String estado, BigDecimal idPais) throws SQLException{
    	List<BigDecimal> existe= new ArrayList<BigDecimal>();
    	PreparedStatement pstmt = null;
	    ResultSet rst = null;
	
	    	String query="SELECT   (SELECT COUNT (*) " +
	    			"            FROM TC_SC_VEND_PANELPDV " +
	    			"           WHERE     VENDEDOR = ?"+
	    			"                 AND tipo = ? " +
	    			"                 AND upper(ESTADO) = ? " +
	    			"                 AND IDTIPO NOT IN (?)), " +
	    			"        (SELECT COUNT (*) " +
	    			"            FROM TC_SC_RUTA " +
	    			"           WHERE SECUSUARIOID = ?" +
	    			"                 AND  upper(ESTADO) = ?"+
	    			"			AND TCSCCATPAISID=? ) " +
	    			"  FROM DUAL " ;
	    try{
	    	pstmt = conn.prepareStatement(query);
	    	pstmt.setBigDecimal(1, idVendedor);
	    	pstmt.setString(2, tipo);
	    	pstmt.setString(3, estado.toUpperCase());
	    	pstmt.setBigDecimal(4, idPanel);
	    	pstmt.setBigDecimal(5, idVendedor);
	    	pstmt.setString(6, estado.toUpperCase());
	    	pstmt.setBigDecimal(7, idPais);
	        rst = pstmt.executeQuery();
	        log.trace("validando si existe vendedor en otra panel:" + query);
	
	        if (rst.next()) {
	            existe.add(0, rst.getBigDecimal(1));
	            existe.add(1, rst.getBigDecimal(2));
	        }
	    }finally{
	    	DbUtils.closeQuietly(pstmt);
	    	DbUtils.closeQuietly(rst);
	    }

        return existe;
    }

    /**
     * Funci\u00F3n que realiza las operaciones necesarias para obtener los datos al
     * consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param idPais
     * @param input
     * @param metodo
     * @return OutputPanel
     * @throws SQLException
     * @throws NamingException
     * @throws ExcepcionSeguridad
     */
    public static OutputPanel doGet(Connection conn,  InputPanel input, BigDecimal idPais)
            throws SQLException, ExcepcionSeguridad, NamingException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<InputPanel> list = new ArrayList<InputPanel>();

        Respuesta respuesta = null;
        OutputPanel output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        
        try{
	        String tipoPanel = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_PANEL, input.getCodArea());
	
	        String sql = null;
	
	        String campos[][] = {
	            { Panel.N_TABLA, Panel.N_TABLA_ID },
	            { Panel.N_TABLA, Panel.CAMPO_TCSCDTSID },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_NOMBRES },
	            { Distribuidor.N_TABLA, Distribuidor.CAMPO_TIPO },
	            { Panel.N_TABLA, Panel.CAMPO_TCSCBODEGAVIRTUALID },
	            { Panel.N_TABLA, Panel.CAMPO_NOMBRE },
	            { Panel.N_TABLA, Panel.CAMPO_ESTADO },
	            { Panel.N_TABLA, Panel.CAMPO_CREADO_EL },
	            { Panel.N_TABLA, Panel.CAMPO_CREADO_POR },
	            { Panel.N_TABLA, Panel.CAMPO_MODIFICADO_EL },
	            { Panel.N_TABLA, Panel.CAMPO_MODIFICADO_POR }
	        };
	
	        String[] tablas={
	            Panel.N_TABLA,
	            Distribuidor.N_TABLA
	        };
	        
	        List<Filtro> condiciones = new ArrayList<Filtro>();
	        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Panel.N_TABLA,
	                Panel.CAMPO_TCSCDTSID, Distribuidor.N_TABLA, Distribuidor.CAMPO_TC_SC_DTS_ID));
	        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Panel.N_TABLA,
	                Panel.CAMPO_TCSCCATPAISID, Distribuidor.N_TABLA, Distribuidor.CAMPO_TCSCCATPAISID));
	        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Panel.N_TABLA,
	                Panel.CAMPO_TCSCCATPAISID, ""+idPais));
	        
	
	        if (!input.getIdPanel().equals("")) {
	            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Panel.N_TABLA,
	                    Panel.N_TABLA_ID, input.getIdPanel()));
	        }
	        if (!input.getIdDistribuidor().equals("")) {
	            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Panel.N_TABLA,
	                    Panel.CAMPO_TCSCDTSID, input.getIdDistribuidor()));
	        }
	        if (input.getEstado() != null && !input.getEstado().equals("")) {
	            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Panel.N_TABLA,
	                    Panel.CAMPO_ESTADO, input.getEstado()));
	        }
	        if (input.getIdBodegaVirtual() != null && !input.getIdBodegaVirtual().equals("")) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Panel.N_TABLA,
                        Panel.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodegaVirtual()));
            }

	        List<Order> orden = new ArrayList<Order>();
	        orden.add(new Order(Panel.CAMPO_NOMBRE, Order.ASC));
	        
	        sql = UtileriasBD.armarQueryGetMultiple(campos, tablas, condiciones, orden);
	
	        pstmt = conn.prepareStatement(sql);
	        rst = pstmt.executeQuery();
	
	        if (rst != null) {
	            if (!rst.next()) {
	                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_PANELES_824, null,
	                        nombreClase, nombreMetodo, null, input.getCodArea());
	
	                output = new OutputPanel();
	                output.setRespuesta(respuesta);
	            } else {
	                respuesta = new Respuesta();
	                respuesta=new ControladorBase()
	                        .getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, input.getCodArea());
	
	                String idPanel = "";
	
	                do {
	                    InputPanel item = new InputPanel();
	                    InputPanel datosVendedor = new InputPanel();
	
	                    idPanel = rst.getString(Panel.N_TABLA_ID);                    
	                    item.setIdPanel(idPanel);
	                    
	                    item.setIdDistribuidor(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Panel.CAMPO_TCSCDTSID));
	                    item.setNombreDTS(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Distribuidor.CAMPO_NOMBRES));
	                    item.setTipoDTS(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Distribuidor.CAMPO_TIPO));
	
	                    item.setIdBodegaVirtual(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Panel.CAMPO_TCSCBODEGAVIRTUALID));
	                    item.setNombre(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Panel.CAMPO_NOMBRE));
	                    item.setEstado(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Panel.CAMPO_ESTADO));
	                    item.setCreado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Panel.CAMPO_CREADO_EL));
	                    item.setCreado_por(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Panel.CAMPO_CREADO_POR));
	                    item.setModificado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Panel.CAMPO_MODIFICADO_EL));
	                    item.setModificado_por(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Panel.CAMPO_MODIFICADO_POR));
	
	                    // Se verifica si la panel tiene n\u00FAmeros de recarga o vendedores como datos asociados.
	                    List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
	                    condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorPDV.CAMPO_IDTIPO, idPanel));
	                    condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_TIPO, tipoPanel));
	
	                    datosVendedor = getDatosVendedor(conn,   condicionesExistencia, input.getCodArea(), idPais);
	                    item.setDatosVendedor(datosVendedor.getDatosVendedor());
	                    item.setResponsable(UtileriasJava.getValue(datosVendedor.getResponsable()));
	                    item.setNombreResponsable(UtileriasJava.getValue(datosVendedor.getNombreResponsable()));
	                    item.setIdBodResponsable(UtileriasJava.getValue(datosVendedor.getIdBodResponsable()));
	                    item.setNombreBodResponsable(UtileriasJava.getValue(datosVendedor.getNombreBodResponsable()));
	
	                  
	                    list.add(item);
	                } while (rst.next());
	
	                output = new OutputPanel();
	                output.setRespuesta(respuesta);
	                output.setPanel(list);
	            }
	        }
        }finally
        {
	        DbUtils.closeQuietly(rst);
	        DbUtils.closeQuietly(pstmt);
        }
        return output;
    }
    
    /**
     * Funcion que se utiliza para consultar y devolver todos los datos del vendedor asociado a una panel.
     * 
     * @param conn
     * @param idPanel
     * @param input
     * @param condiciones
     * @return
     * @throws SQLException
     * @throws ExcepcionSeguridad
     * @throws NamingException
     */
    private static InputPanel getDatosVendedor(Connection conn, 
            List<Filtro> condiciones, String codArea, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "getDatosVendedor";
        String nombreClase = new CurrentClassGetter().getClassName();

        InputPanel output = new InputPanel();
        List<InputVendedor> list = new ArrayList<InputVendedor>();
        InputVendedor item = new InputVendedor();
        PreparedStatement pstmtIn = null;
        ResultSet rstIn = null;

        try {
	        String sqlIdBodResponsable = "SELECT " + BodegaVendedor.N_TABLA + "." + BodegaVendedor.CAMPO_TCSCBODEGAVIRTUALID
	                + " FROM " + BodegaVendedor.N_TABLA
	                + " WHERE " + BodegaVendedor.N_TABLA + "." + BodegaVendedor.CAMPO_TCSCCATPAISID + " = " + idPais
	                + " AND " + BodegaVendedor.N_TABLA + "." + BodegaVendedor.CAMPO_VENDEDOR + " = " + VendedorPDV.N_TABLA + "." + VendedorPDV.CAMPO_VENDEDOR;
	        
	        String campos[] = {
	            VendedorPDV.N_TABLA_ID,
	            VendedorPDV.CAMPO_VENDEDOR,
	            VendedorPDV.CAMPO_ESTADO,
	            VendedorPDV.CAMPO_RESPONSABLE,
	            "(SELECT " + VendedorDTS.N_TABLA + "." + VendedorDTS.CAMPO_USUARIO + " FROM " + VendedorDTS.N_TABLA
	                    + " WHERE " + VendedorDTS.N_TABLA + "." + VendedorDTS.CAMPO_TCSCCATPAISID + " = " + idPais
	                    + " AND " + VendedorDTS.N_TABLA + "." + VendedorDTS.CAMPO_VENDEDOR + " = " + VendedorPDV.N_TABLA + "." + VendedorPDV.CAMPO_VENDEDOR + ") AS NOMBRE_VEND,"
	            + "(" + sqlIdBodResponsable + ") AS IDBODRESP, "
	
	            + "(SELECT " + BodegaVirtual.N_TABLA + "." + BodegaVirtual.CAMPO_NOMBRE
	            + " FROM " + BodegaVirtual.N_TABLA
	            + " WHERE " + BodegaVirtual.N_TABLA + "." + BodegaVirtual.CAMPO_TCSCCATPAISID + " = " + idPais
	            + " AND " + BodegaVirtual.N_TABLA + "." + BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID + " = " + "(" + sqlIdBodResponsable + ")) AS NOMBRE_BODRESP"
	        };
	
	        String sql = UtileriasBD.armarQuerySelect(VendedorPDV.N_TABLA, campos, condiciones);
	
	        pstmtIn = conn.prepareStatement(sql);
	        rstIn = pstmtIn.executeQuery();
	
	        if (rstIn != null) {
	            if (!rstIn.next()) {
	                log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
	                item = new InputVendedor();
	
	                Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null,
	                        nombreClase, nombreMetodo, null, codArea);
	
	                item.setEstado(respuesta.getDescripcion());
	            } else {
	                List<Map<String, String>> listInvVendedor = getInvVendedor(conn, codArea, idPais);
	                String idVendedor = "";
	                String nombreVendedor = "";
	
	                do {
	                    item = new InputVendedor();
	                    idVendedor = UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO, VendedorPDV.CAMPO_VENDEDOR);
	                    item.setIdVendPanelPDV(idVendedor);
	                    nombreVendedor = UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, "NOMBRE_VEND");
	                    item.setNombre(nombreVendedor);
	                    item.setEstado(UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, VendedorPDV.CAMPO_ESTADO));
	
	                    if (listInvVendedor.size() > 0) {
	                        for (int i = 0; i < listInvVendedor.size(); i++) {
	                            if (idVendedor.equals(listInvVendedor.get(i).get("VENDEDOR"))) {
	                                item.setCantInventario(listInvVendedor.get(i).get("CANT"));
	                            } else {
	                                item.setCantInventario("0");
	                            }
	                        }
	                    } else {
	                        item.setCantInventario("0");
	                    }
	
	                    if (UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO, VendedorPDV.CAMPO_RESPONSABLE).equals("1")) {
	                        output.setResponsable(idVendedor);
	                        output.setNombreResponsable(nombreVendedor);
	                        output.setIdBodResponsable(UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO, "IDBODRESP"));
	                        output.setNombreBodResponsable(UtileriasJava.getRstValue(rstIn, Conf.TIPO_TEXTO, "NOMBRE_BODRESP"));
	                    }
	
	                    list.add(item);
                    } while (rstIn.next());

                    output.setDatosVendedor(list);
                }
            }
        } finally {
            DbUtils.closeQuietly(rstIn);
            DbUtils.closeQuietly(pstmtIn);
        }

        return output;
    }
    
    /**
     * Metodo que sirve para consultar el inventario que tengan asignado los vendedores.
     * 
     * @param conn
     * @return
     * @throws SQLException
     */
    private static List<Map<String, String>> getInvVendedor(Connection conn, String codArea, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "getInvVendedor";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        PreparedStatement pstmtIn = null;
        ResultSet rstIn = null;

      
	        String campos[] = {
	            UtileriasJava.setSelect(Conf.SELECT_SUM, Inventario.CAMPO_CANTIDAD),
	            Inventario.CAMPO_IDVENDEDOR
	        };
	
	        List<Filtro> condiciones = new ArrayList<Filtro>();
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NOT_NULL_AND, Inventario.CAMPO_IDVENDEDOR, null));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, ""+idPais));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_NOT_IN_AND, "UPPER("+Inventario.CAMPO_ESTADO+")", "'VENDIDO'"));
	        String sql = armaQueryIdVendedor(  campos,condiciones) ;
	        log.debug("SQL completo: " + sql);
	    try{
	        pstmtIn = conn.prepareStatement(sql);
	        rstIn = pstmtIn.executeQuery();
	
	        if (rstIn != null) {
	            if (!rstIn.next()) {
	                log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
	
	                Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null,
	                        nombreClase, nombreMetodo, null, codArea);
	                item = new HashMap<String, String>();
	                item.put("CANT", respuesta.getDescripcion());
	            } else {
	                do {
	                    item = new HashMap<String, String>();
	                    
	                    item.put("CANT", rstIn.getString(Inventario.CAMPO_CANTIDAD));
	                    item.put("VENDEDOR", rstIn.getString(Inventario.CAMPO_IDVENDEDOR));
	                    
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
public static String armaQueryIdVendedor( String campos[],List<Filtro> condiciones) {
    String sql = UtileriasBD.armarQuerySelect(Inventario.N_TABLA, campos, condiciones);
    sql += " GROUP BY " + Inventario.CAMPO_IDVENDEDOR;
    
    return sql;
}
  
    /**
     * M\u00E9todo para consultar los datos de una panel mediante el vendedor enviado.
     * 
     * @param conn
     * @param idVendedor
     * @param input
     * @return
     * @throws SQLException
     */
    public static OutputPanel doGetByVendedor(Connection conn, String idVendedor, String codArea) throws SQLException {
        String nombreMetodo = "doGetByVendedor";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta respuesta = null;
        OutputPanel output = new OutputPanel();
        InputPanel item = new InputPanel();;
        List<InputPanel> list = new ArrayList<InputPanel>();
        PreparedStatement pstmtIn = null;
        ResultSet rstIn = null;

        try{
	        String campos[] = {
	            Panel.N_TABLA_ID,
	            Panel.CAMPO_NOMBRE
	        };
	        
	        List<Filtro> condiciones = new ArrayList<Filtro>();
	        condiciones.add(new Filtro(VendedorPDV.CAMPO_VENDEDOR, Filtro.EQ, idVendedor));
	        condiciones.add(UtileriasJava.getCondicionDefault(Conf.FILTRO_PANEL, NumRecarga.CAMPO_TIPO, conn, codArea));
	        
	        List<String> listadoVendedores = UtileriasBD.getOneField(conn,
	            VendedorPDV.CAMPO_IDTIPO, VendedorPDV.N_TABLA, condiciones, null);
	        
	        if (listadoVendedores.size() > 0){
	            condiciones.clear();
	            for (int i = 0; i < listadoVendedores.size(); i++) {
	                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_OR, Panel.N_TABLA_ID, listadoVendedores.get(i)));
	            }
	        } else {
	            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null,
	                nombreClase, nombreMetodo, null, codArea);
	
	            output.setRespuesta(respuesta);
	            return output;
	        }
	
	        String sql = UtileriasBD.armarQuerySelect(Panel.N_TABLA, campos, condiciones);
	
	        pstmtIn = conn.prepareStatement(sql);
	        rstIn = pstmtIn.executeQuery();
	
	        if (rstIn != null) {
	            if (!rstIn.next()) {
	                log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
	   
	                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null,
	                    nombreClase, nombreMetodo, null, codArea);
	
	                item.setEstado(respuesta.getDescripcion());
	                list.add(item);
	                output.setRespuesta(respuesta);
	                output.setPanel(list);
	            } else {
	                respuesta = new Respuesta();
	                respuesta=new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
	                    nombreClase, nombreMetodo, null, codArea);
	                
	                do {
	                    item = new InputPanel();
	                    item.setIdPanel(rstIn.getString(Panel.N_TABLA_ID));
	                    item.setNombre(rstIn.getString(Panel.CAMPO_NOMBRE));
	                    
	                    list.add(item);
	                } while (rstIn.next());
	                
	                output.setRespuesta(respuesta);
	                output.setPanel(list);
	            }
	        }
        }finally{

        	DbUtils.closeQuietly(rstIn);
        	DbUtils.closeQuietly(pstmtIn);
        }
        return output;
    }

    /***
     * M\u00E9todo para obtener la cantidad de inventario que tiene una Panel
     * 
     * @throws SQLException
     */
    public static List<BigDecimal> getInvPanel(Connection conn, String tipo, String id, BigDecimal idPais) throws SQLException {
        List<BigDecimal> lstPanel = new ArrayList<BigDecimal>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
     
	    	String query = "SELECT 0," + 
	    			"       (SELECT COUNT (*)  " +
	    			"          FROM tc_Sc_inventario " +
	    			"         WHERE idvendedor IN (SELECT vendedor " +
	    			"                                FROM tc_sc_vend_PANELpdv " +
	    			"                               WHERE idtipo = ? AND tipo = ?"
	    					+ ") and tcsccatpaisid=?)inv_vendedor  " +
	    			"  FROM DUAL " ;
	   try{
	    	pstmt = conn.prepareStatement(query);
	    	pstmt.setBigDecimal(1, new BigDecimal(id));
	    	pstmt.setString(2, tipo);
	    	pstmt.setBigDecimal(3, idPais);
	    	
	        rst = pstmt.executeQuery();
	        log.trace("Verificando si la panel tiene inventario:" + query);
	
	        if (rst.next()) {
	            lstPanel.add(0, rst.getBigDecimal(1));
	            lstPanel.add(1, rst.getBigDecimal(2));
	        }
        }finally{
        	DbUtils.closeQuietly(rst);
        	DbUtils.closeQuietly(pstmt);
        }
        return lstPanel;
    }

    /**
     * M\u00E9todo para consultar la existencia de un numero de recarga de vendedor en cualquiera de los otros recursos.
     * 
     * @param conn
     * @param metodo
     * @param datosVendedor
     * @param estadoAlta
     * @param tipoPanel
     * @param idPanel
     * @return
     * @throws SQLException
     */
    public static String existeNumRecarga(Connection conn, int metodo, List<InputVendedor> datosVendedor,
            String estadoAlta, String tipoPanel, String idPanel) throws SQLException {
        String numeros = "";
        String respuesta = "";

        for (int i = 0; i < datosVendedor.size(); i++) {
            numeros += datosVendedor.get(i).getNumRecarga();

            if (i != datosVendedor.size() - 1)
                numeros += ",";
        }

        List<Filtro> condiciones = new ArrayList<Filtro>();
        if (metodo == Conf.METODO_PUT) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND_NEQ, VendedorPDV.CAMPO_IDTIPO, idPanel));
        }
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_ESTADO, estadoAlta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, VendedorPDV.CAMPO_TIPO, tipoPanel));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, VendedorPDV.CAMPO_NUM_RECARGA, numeros));

        List<String> numerosExistentes = UtileriasBD.getOneField(conn, VendedorPDV.CAMPO_NUM_RECARGA,
                VendedorPDV.N_TABLA, condiciones, null);

        if (!numerosExistentes.isEmpty()) {
            return "Vendedor de Panel: "
                    + UtileriasJava.listToString(Conf.TIPO_NUMERICO, numerosExistentes, Conf.INSERT_TEXTO_SEPARADOR);
        }

        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Ruta.CAMPO_ESTADO, estadoAlta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Ruta.CAMPO_NUM_RECARGA, numeros));

        numerosExistentes.addAll(UtileriasBD.getOneField(conn, Ruta.CAMPO_NUM_RECARGA, Ruta.N_TABLA, condiciones, null));

        if (!numerosExistentes.isEmpty()) {
            return "Ruta: " + UtileriasJava.listToString(Conf.TIPO_NUMERICO, numerosExistentes, Conf.INSERT_TEXTO_SEPARADOR);
        }
        
        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, NumRecarga.CAMPO_ESTADO, estadoAlta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, NumRecarga.CAMPO_NUM_RECARGA, numeros));

        numerosExistentes.addAll(UtileriasBD.getOneField(conn, NumRecarga.CAMPO_NUM_RECARGA, NumRecarga.N_TABLA, condiciones, null));
        
        if (!numerosExistentes.isEmpty()) {
            respuesta = "PDV: ";
        }

        return respuesta + UtileriasJava.listToString(Conf.TIPO_NUMERICO, numerosExistentes, Conf.INSERT_TEXTO_SEPARADOR);
    }

    /**
     * M\u00E9todo para validar si los vendedores no se encuentran asociados a otra panel o puntos de venta. 
     * @throws SQLException 
     * @throws NamingException 
     * @throws ExcepcionSeguridad 
     * */
    public static String validarVendedorPanel(Connection conn, List<VendedorPDV> lstVendedor, BigDecimal idPanel,
            String tipo, String estado, BigDecimal idPais) throws SQLException, ExcepcionSeguridad, NamingException {
        String respuesta = "";

        for (VendedorPDV obj : lstVendedor) {
            List<BigDecimal> existe ;
            existe = OperacionPanel.existeVendedor(conn, obj.getVendedor(), idPanel, tipo, estado, idPais);
            if (existe.get(0).intValue() > 0) {
                respuesta += " El vendedor " + ControladorBase.getNombreVendedor(conn, obj.getVendedor() + "", idPais)
                        + " ya ha sido asignado a una panel.";
            }
            if (existe.get(1).intValue() > 0) {
                respuesta += " El vendedor " + ControladorBase.getNombreVendedor(conn, obj.getVendedor() + "", idPais)
                + " ya ha sido asignado a una ruta.";
            }
        }
        
        return respuesta;
    }
}

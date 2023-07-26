package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaVirtual;
import com.consystec.sc.ca.ws.input.condicionoferta.InputDetCondicionOferta;
import com.consystec.sc.ca.ws.input.pdv.InputPDV;
import com.consystec.sc.ca.ws.input.vendedordts.InputVendedorDTS;
import com.consystec.sc.ca.ws.input.venta.ArticuloPromocionalVenta;
import com.consystec.sc.ca.ws.input.venta.ArticuloVenta;
import com.consystec.sc.ca.ws.input.venta.Descuento;
import com.consystec.sc.ca.ws.input.venta.DetallePago;
import com.consystec.sc.ca.ws.input.venta.Impuesto;
import com.consystec.sc.ca.ws.input.venta.InputGetDetalle;
import com.consystec.sc.ca.ws.input.venta.InputGetVenta;
import com.consystec.sc.ca.ws.input.venta.InputVenta;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.venta.OutputArticuloVenta;
import com.consystec.sc.ca.ws.output.venta.OutputVenta;
import com.consystec.sc.sv.ws.orm.ArticulosSidra;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Exento;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.PagoDet;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.Venta;
import com.consystec.sc.sv.ws.orm.VentaDet;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionVentasCopia {
	private OperacionVentasCopia(){}
    private static final Logger log = Logger.getLogger(OperacionVentasCopia.class);

    
   

   

    public static Inventario getArticulo(Connection conn, String estado, String serie, String idBodega, String articulo,
            String tipoInv, boolean recarga, String idJornada, String codArea, BigDecimal idPais) throws SQLException {
        Inventario objInv = new Inventario();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String serieCondicion = "";

       
            if (recarga) {
	            idBodega = "SELECT IDBODEGA_PADRE FROM " + ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", codArea)
	                    + " WHERE TCSCBODEGAVIRTUALID IN ("
	                        + " SELECT CASE DESCRIPCION_TIPO "
	                            + " WHEN 'PANEL' THEN (SELECT TCSCBODEGAVIRTUALID FROM TC_SC_PANEL WHERE TCSCPANELID = IDTIPO AND TCSCCATPAISID = " + idPais + ") "
	                            +  "WHEN 'RUTA' THEN (SELECT TCSCBODEGAVIRTUALID FROM TC_SC_RUTA WHERE TCSCRUTAID = IDTIPO AND TCSCCATPAISID = " + idPais + ") "
	                    + " END FROM " + ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea) + " WHERE TCSCJORNADAVENID = " + idJornada + ")";
	        }

            if (serie == null || "".equals(serie)) {
                serieCondicion = " IS NULL ";
            } else {
                serieCondicion = " = '" + serie + "' ";
            }

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * " );
            sql.append(		"  FROM " + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, idBodega,codArea) );
            sql.append(		" WHERE SERIE" + serieCondicion );
            sql.append(		"   AND ARTICULO = " + articulo );
            sql.append(		"   AND TIPO_INV = '" + tipoInv + "'" );
            sql.append(	    "   AND TCSCBODEGAVIRTUALID IN (" + idBodega + ")" );
            sql.append(	    " AND UPPER(ESTADO) IN ('" + estado.toUpperCase() + "')" );
            sql.append(	    " AND CANTIDAD > 0");

            log.debug("query inv: " + sql.toString());
        try {
            pstmt = conn.prepareStatement(sql.toString());
            rst = pstmt.executeQuery();

            if (rst.next()) {
                objInv.setArticulo(rst.getBigDecimal(Inventario.CAMPO_ARTICULO));
                objInv.setCantidad(rst.getBigDecimal(Inventario.CAMPO_CANTIDAD));
                objInv.setDescripcion(rst.getString(Inventario.CAMPO_DESCRIPCION));
                objInv.setEstado(rst.getString(Inventario.CAMPO_ESTADO));
                objInv.setEstado_comercial(rst.getString(Inventario.CAMPO_ESTADO_COMERCIAL));
                objInv.setSeriado(rst.getString(Inventario.CAMPO_SERIADO));
                objInv.setSerie(rst.getString(Inventario.CAMPO_SERIE));
                objInv.setTcscbodegavirtualid(rst.getBigDecimal(Inventario.CAMPO_TCSCBODEGAVIRTUALID));
                objInv.setTcscinventarioinvid(rst.getBigDecimal(Inventario.N_TABLA_ID));
                objInv.setTipo_inv(rst.getString(Inventario.CAMPO_TIPO_INV));
                objInv.setTipo_grupo(rst.getString(Inventario.CAMPO_TIPO_GRUPO));
                objInv.setTipo_grupo_sidra(rst.getString(Inventario.CAMPO_TIPO_GRUPO_SIDRA));
                objInv.setTecnologia(rst.getString(Inventario.CAMPO_TECNOLOGIA));
            } else {
                objInv = null;
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return objInv;
    }

   

    public static int validaSerieIndividual(Connection conn, String serie, String idBodegaVendedor, String articulo,
            String estadoAnulado, String codArea) throws SQLException {
        int respuesta = 0;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        StringBuilder sql = new StringBuilder();
     
        sql.append( "SELECT COUNT(1)");
        sql.append(" FROM TC_SC_VENTA_DET" );
        sql.append( " WHERE articulo=" + articulo);
        sql.append( " AND tcscbodegavirtualid=" + idBodegaVendedor );
        sql.append(" AND serie = '" + serie + "'");
        sql.append( " AND tcscventaid NOT IN (");
        sql.append( "SELECT tcscventaid" );
        sql.append( " FROM " + ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "",codArea));
        sql.append(" WHERE UPPER(estado) = UPPER(?))");
        try {
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setBigDecimal(1, new BigDecimal(articulo));
            pstmt.setBigDecimal(2, new BigDecimal(idBodegaVendedor));
            pstmt.setString(3, serie);
            pstmt.setString(4,  estadoAnulado);
	        rst = pstmt.executeQuery();
            log.debug(sql);

            if (rst.next()) {
                respuesta = rst.getInt(1);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return respuesta;
    }

    /***
     * Validar si el rango de series de tarjeta rasca existe
     * 
     * @param conn
     * @param rangoInicial
     * @param rangoFinal
     * @param articulo
     * @param bodega
     * @param codArea 
     * @return
     * @throws SQLException
     */
    public static String validarSeriesRasca(Connection conn, String rangoInicial, String rangoFinal,
            BigDecimal articulo, BigDecimal bodega, String codArea) throws SQLException {
        String respuesta = "";
        PreparedStatement pstmt = null;
        ResultSet rst = null;

  
            String nombrePaquete = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.PAQUETE_SIDRA,codArea);
            int diferenciaCeros = OperacionMovimientosInventario.getDiferenciaCeros(rangoInicial);

	        String sql = OperacionVentas.queryValidaSeries(  rangoInicial,  rangoFinal,
	                 articulo,  bodega,  nombrePaquete, diferenciaCeros, codArea);
	      try {
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();
            log.debug(sql);

            if (rst.next()) {
                respuesta = rst.getString(1);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return respuesta;
    }

  

    /* Funciones para servicios de consulta de ventas. */
    public static OutputVenta doGet(Connection conn, InputGetVenta inputVenta, BigDecimal idPais)
            throws SQLException,  NamingException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();
        InputVendedorDTS vendedor = null;
        InputBodegaVirtual bodegaVendedor = null;
        InputPDV puntoVenta = new InputPDV();
        List<InputVenta> list = new ArrayList<InputVenta>();

        Respuesta respuesta = null;
        OutputVenta output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        
            String tipoCF = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPO_CLIENTE_VENTA, Conf.CLIENTEFINAL, inputVenta.getCodArea());

            List<Order> orden = new ArrayList<Order>();
            orden.add(new Order(Venta.CAMPO_FECHA_EMISION, Order.ASC));

	        String sql = OperacionVentas. queryGet( inputVenta);

            log.debug("QUERY ARMADO: " + sql);
        try {
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_VENTAS_826, null,
                            nombreClase, nombreMetodo, null, inputVenta.getCodArea());
                    output = new OutputVenta();
                    output.setRespuesta(respuesta);
                } else {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_GETDATOS12, null, nombreClase,
                            nombreMetodo, null, inputVenta.getCodArea());

                    String idVendedor = "";
                    String idTipo = "";
                    String tipo = "";
                    String idJornada = "";

                    do {
	                    InputVenta item = new InputVenta();
	                    idVendedor = rst.getString(Venta.CAMPO_VENDEDOR);
	                    idTipo = rst.getString(Venta.CAMPO_IDTIPO);
                        tipo = rst.getString(Venta.CAMPO_TIPO);
                        idJornada = rst.getString(Venta.CAMPO_TCSCJORNADAVENID);
                        String zonaComercial = "";

                        if (!tipo.equals(tipoCF)) {
                            puntoVenta = datosPuntoVenta(idTipo, conn, inputVenta.getIdDTS(), inputVenta.getCodArea(), idPais);
                        } else {
                            puntoVenta = new InputPDV();
                            zonaComercial = getZonaComercial(conn, idJornada, idPais);
                        }

                        item.setIdVenta(rst.getString(Venta.CAMPO_TCSCVENTAID));
                        item.setIdJornada(idJornada);
	                    item.setFecha(UtileriasJava.formatStringDate(rst.getString(Venta.CAMPO_FECHA_EMISION)));
	                    item.setFolio(UtileriasJava.getValue(rst.getString(Venta.CAMPO_NUMERO)));
	                    item.setSerie(UtileriasJava.getValue(rst.getString(Venta.CAMPO_SERIE)));
	                    item.setFolioSidra(UtileriasJava.getValue(rst.getString(Venta.CAMPO_FOLIO_SIDRA)));
	                    item.setSerieSidra(UtileriasJava.getValue(rst.getString(Venta.CAMPO_SERIE_SIDRA)));
                        item.setTipoDocumento(rst.getString(Venta.CAMPO_TIPO_DOCUMENTO));

	                    /* Datos ficha ciente */
	                    item.setNombre(UtileriasJava.getValue(rst.getString(Venta.CAMPO_NOMBRE)));
	                    item.setSegundoNombre(UtileriasJava.getValue(rst.getString(Venta.CAMPO_SEGUNDO_NOMBRE)));
	                    item.setApellido(UtileriasJava.getValue(rst.getString(Venta.CAMPO_APELLIDO)));
	                    item.setSegundoApellido(UtileriasJava.getValue(rst.getString(Venta.CAMPO_SEGUNDO_APELLIDO)));
	                    item.setDireccion(UtileriasJava.getValue(rst.getString(Venta.CAMPO_DIRECCION)));
	                    item.setNumTelefono(UtileriasJava.getValue(rst.getString(Venta.CAMPO_NUM_TELEFONO)));
	                    item.setTipoDocCliente(UtileriasJava.getValue(rst.getString(Venta.CAMPO_TIPO_DOCCLIENTE)));
	                    item.setNumDocCliente(UtileriasJava.getValue(rst.getString(Venta.CAMPO_NUM_DOCCLIENTE)));

	                    item.setNombresFacturacion(UtileriasJava.getValue(rst.getString(Venta.CAMPO_NOMBRES_FACTURA)));
	                    item.setApellidosFacturacion(UtileriasJava.getValue(rst.getString(Venta.CAMPO_APELLIDOS_FACTURA)));

	                    item.setTasaCambio(UtileriasJava.getValue(rst.getString(Venta.CAMPO_TASA_CAMBIO)));
	                    item.setIdOfertaCampania(rst.getString(Venta.CAMPO_IDOFERTACAMPANIA));
	                    item.setDescuentoMontoVenta(UtileriasJava.convertirMoneda(rst.getString(Venta.CAMPO_DESC_MONTOVENTA), item.getTasaCambio()));
	                    item.setDescuentoTotal(UtileriasJava.convertirMoneda(rst.getString(Venta.CAMPO_DESCUENTOS), item.getTasaCambio()));
	                    item.setEstado(rst.getString(Venta.CAMPO_ESTADO));
	                    item.setObservaciones(UtileriasJava.getValue(rst.getString(Venta.CAMPO_OBSERVACIONES)));
	                    item.setMontoFactura(UtileriasJava.convertirMoneda(rst.getString(Venta.CAMPO_MONTO_FACTURA), item.getTasaCambio()));
	                    item.setMontoPagado(UtileriasJava.convertirMoneda(rst.getString(Venta.CAMPO_MONTO_PAGADO), item.getTasaCambio()));
	                    item.setExento(rst.getString(Venta.CAMPO_EXENTO));
	                    item.setImpuesto(UtileriasJava.convertirMoneda(rst.getString(Venta.CAMPO_IMPUESTOS), item.getTasaCambio()));
	                    item.setEnvioAlarma(UtileriasJava.getValue(rst.getString(Venta.CAMPO_ENVIO_ALARMA)));
	                    item.setLatitud(UtileriasJava.getValue(rst.getString(Venta.CAMPO_LATITUD)));
	                    item.setLongitud(UtileriasJava.getValue(rst.getString(Venta.CAMPO_LONGITUD)));
	                    item.setZonaComercial(UtileriasJava.getValue(zonaComercial));

	                    item.setCreadoPor(rst.getString(Venta.CAMPO_CREADO_POR));
	                    item.setCreadoEl(UtileriasJava.formatStringDate(rst.getString(Venta.CAMPO_CREADO_EL)));
	                    item.setModificadoPor(UtileriasJava.getValue(rst.getString(Venta.CAMPO_MODIFICADO_POR)));
	                    item.setModificadoEl(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Venta.CAMPO_MODIFICADO_EL));

                        /* Datos para vendedor */
                        item.setIdVendedor(idVendedor);
                        vendedor = datosVendedor(idVendedor, conn, idPais);
                      if(vendedor!=null){
                        item.setNombreVendedor(vendedor.getNombres());
                        item.setIdBodegaVendedor(vendedor.getIdBodegaVirtual());

                        /* Datos bodega vendedor */
                        bodegaVendedor = datosBodegaVirtual(vendedor.getIdBodegaVirtual(), conn, inputVenta.getCodArea(), idPais);
                        item.setBodegaVendedor(bodegaVendedor.getNombre());
                      }
                        /* Datos PDV y fiscales */
                        item.setTipo(tipo);
                        item.setIdTipo(idTipo);
	                    item.setNombrePDV(UtileriasJava.getValue(puntoVenta.getNombrePDV()));
	                    item.setNit(rst.getString(Venta.CAMPO_NIT));
	                    item.setRegistroFiscal(UtileriasJava.getValue(puntoVenta.getRegistroFiscal()));
	                    item.setGiro(UtileriasJava.getValue(puntoVenta.getGiroNegocio()));
	                    item.setNombreFiscal(UtileriasJava.getValue(puntoVenta.getNombreFiscal()));
	                    item.setDepartamento(UtileriasJava.getValue(puntoVenta.getDepartamento()));
	                    item.setMunicipio(UtileriasJava.getValue(puntoVenta.getMunicipio()));
	                    item.setLatitudPdv(UtileriasJava.getValue(puntoVenta.getLatitud()));
	                    item.setLongitudPdv(UtileriasJava.getValue(puntoVenta.getLongitud()));

                        /* Detalle Pago */
                        item.setDetallePago(detallePago(item.getIdVenta(), conn, item.getTasaCambio()));


                        item.setNombrePanelRuta(getNombrePanelRuta(conn, idJornada, idPais, inputVenta.getCodArea()));

                        list.add(item);
                    } while (rst.next());

                    output = new OutputVenta();
                    output.setVenta(list);
                    output.setRespuesta(respuesta);
                }
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return output;
    }

    private static String getZonaComercial(Connection conn, String idJornada, BigDecimal idPais) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;

   
        String query = "WITH JORNADA AS (SELECT IDTIPO, DESCRIPCION_TIPO TIPO, TCSCCATPAISID PAIS "
                + "FROM TC_SC_JORNADA_VEND WHERE TCSCJORNADAVENID = ?)"
			+ "SELECT NOMBRE FROM TC_SC_BODEGAVIRTUAL WHERE TCSCCATPAISID = ?" 
			+ " AND TCSCBODEGAVIRTUALID = (SELECT (CASE TIPO "
			+ "WHEN 'RUTA' THEN (SELECT TCSCBODEGAVIRTUALID FROM TC_SC_RUTA WHERE TCSCRUTAID = IDTIPO AND TCSCCATPAISID = PAIS) "
			+ "ELSE (SELECT TCSCBODEGAVIRTUALID FROM TC_SC_PANEL WHERE TCSCPANELID = IDTIPO AND TCSCCATPAISID = PAIS)"
			+ "END) AS IDBODEGA FROM JORNADA)";
			
			log.debug("Query para obtener zona comercial: " + query);
			try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, new BigDecimal(idJornada));
			pstmt.setBigDecimal(2, idPais);
			rst = pstmt.executeQuery();

            while (rst.next()) {
                return rst.getString("NOMBRE");
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return "";
    }

    public static List<ArticuloPromocionalVenta> articulosPromocionales(String idVenta, Connection con, BigDecimal idPais) throws SQLException{
        List<ArticuloPromocionalVenta> res = new ArrayList<ArticuloPromocionalVenta>();
        ArticuloPromocionalVenta item = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;


		String query = "SELECT (SELECT A.DESCRIPCION FROM TC_SC_ART_PROMOCIONAL A WHERE B.ARTICULO = A.TCSCARTPROMOCIONALID"
		        + " AND A.TCSCCATPAISID = ?) AS NOMBREART, "
				+ " B.CANTIDAD " +
				" FROM TC_SC_VENTA_DET B " +
			    " WHERE B.TCSCVENTAID = ?"
                + " AND B.TIPO_INV = (SELECT C.VALOR FROM TC_SC_CONFIGURACION C "
                + "WHERE C.TCSCCATPAISID = ?"
                + " AND C.GRUPO = ? AND C.NOMBRE = ?)"; 
		
        log.debug("Query para obtener articulosPromocionales: " + query);
    try {
        pstmt = con.prepareStatement(query);
        pstmt.setBigDecimal(1,idPais);
        pstmt.setBigDecimal(2, new BigDecimal(idVenta));
        pstmt.setBigDecimal(3,idPais);
        pstmt.setString(4, Conf.GRUPO_SOLICITUDES_TIPOINV);
        pstmt.setString(5, Conf.SOL_TIPOINV_SIDRA);
	        rst = pstmt.executeQuery();
	
	        while (rst.next()) {
	            item = new ArticuloPromocionalVenta();
	            item.setArticuloPromocional(rst.getString("NOMBREART"));
                item.setCantidad(rst.getString("CANTIDAD"));

                res.add(item);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return res;
    }
	
    public static InputVendedorDTS datosVendedor(String idVendedor, Connection con, BigDecimal idPais) throws SQLException {
        InputVendedorDTS res = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

      
        String query = "SELECT V.TCSCVENDDTSID, "
   					+ "     V.VENDEDOR, " +
   					"       V.USUARIO, " +
   					"       V.NOMBRE, " +
   					"       V.APELLIDO, " +
   					"       V.TCSCBODEGAVIRTUALID " +
   					"  FROM TC_SC_VEND_DTS V" +
                       " WHERE V.TCSCCATPAISID = ? AND V.VENDEDOR = ?" ;
   	
               log.debug("Query para obtener datosVendedor: " + query);
             try {
               pstmt = con.prepareStatement(query);
               pstmt.setBigDecimal(1, idPais);
               pstmt.setBigDecimal(2, new BigDecimal(idVendedor));
            rst = pstmt.executeQuery();

            while (rst.next()) {
                res = new InputVendedorDTS();
                res.setUsuarioVendedor(rst.getString("USUARIO"));
                res.setNombres(rst.getString("NOMBRE") + " " + rst.getString("APELLIDO"));
                res.setIdBodegaVirtual(rst.getString("TCSCBODEGAVIRTUALID"));
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return res;
    }

    public static InputBodegaVirtual datosBodegaVirtual(String idBodegaVirtual, Connection con, String codArea, BigDecimal idPais) throws SQLException {
        InputBodegaVirtual res = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        
        StringBuilder query = new StringBuilder(); 
        query.append("SELECT B.TCSCBODEGAVIRTUALID, B.NOMBRE");
        query.append(" FROM " + ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", codArea) + " B" );
        query.append(" WHERE B.TCSCBODEGAVIRTUALID = ? AND B.TCSCCATPAISID = ?");
      


            log.debug("Query para obtener datosBodegaVirtual: " + query);
        try {
            pstmt = con.prepareStatement(query.toString());
            pstmt.setBigDecimal(1, new BigDecimal(idBodegaVirtual));
            pstmt.setBigDecimal(2, idPais);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                res = new InputBodegaVirtual();
                res.setIdBodega(rst.getString("TCSCBODEGAVIRTUALID"));
                res.setNombre(rst.getString("NOMBRE"));
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return res;
    }

    public static InputPDV datosPuntoVenta(String idPdv, Connection con, String idDts, String codArea, BigDecimal idPais) throws SQLException {
        InputPDV res = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        
        StringBuilder query = new StringBuilder(); 
		
        query.append("SELECT P.TCSCPUNTOVENTAID, P.NOMBRE, P.NIT, P.REGISTRO_FISCAL, P.NOMBRE_FISCAL, P.DEPARTAMENTO, P.MUNICIPIO, P.GIRO_NEGOCIO, P.LATITUD, P.LONGITUD");
        query.append(" FROM " + ControladorBase.getParticion(PuntoVenta.N_TABLA, Conf.PARTITION, idDts, codArea) + " P" );
		query.append(" WHERE P.TCSCPUNTOVENTAID =? AND TCSCCATPAISID = ?");

            log.debug("Query para obtener datosPuntoVenta:" + query.toString());
        try {
            pstmt = con.prepareStatement(query.toString());
            pstmt.setBigDecimal(1, new BigDecimal(idPdv));
            pstmt.setBigDecimal(2, idPais);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                res = new InputPDV();
                res.setIdPDV(rst.getString("TCSCPUNTOVENTAID"));
                res.setNombrePDV(rst.getString("NOMBRE"));
                res.setNit(rst.getString("NIT"));
                res.setRegistroFiscal(rst.getString("REGISTRO_FISCAL"));
                res.setNombreFiscal(rst.getString("NOMBRE_FISCAL"));
                res.setDepartamento(rst.getString("DEPARTAMENTO"));
                res.setMunicipio(rst.getString("MUNICIPIO"));
                res.setGiroNegocio(rst.getString("GIRO_NEGOCIO"));
                res.setLatitud(rst.getString("LATITUD"));
                res.setLongitud(rst.getString("LONGITUD"));
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return res;
    }

    public static List<DetallePago> detallePago(String idVenta, Connection con, String tasaCambio) throws SQLException {
        List<DetallePago> res = new ArrayList<DetallePago>();
        DetallePago item = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

       
        String query ="SELECT D.formapago, " +
        		"       D.monto, " +
        		"       D.num_autorizacion, " +
        		"       D.banco, " +
        		"       D.marca_tarjeta, " +
        		"       D.digitos_tarjeta, " +
        		"       D.num_cheque, " +
        		"       D.fecha_emision, " +
        		"       D.num_reserva, " +
        		"       D.no_cuenta " +
        		"  FROM TC_SC_DET_PAGO D " +
        		" WHERE D.tcscventaid = ? " ;

        log.debug("Query para obtener detallePago: " + query);
    try {
        pstmt = con.prepareStatement(query);
        pstmt.setBigDecimal(1, new BigDecimal(idVenta));
            rst = pstmt.executeQuery();

            while (rst.next()) {
                item = new DetallePago();
                item.setFormaPago(rst.getString(PagoDet.CAMPO_FORMAPAGO));
                item.setMonto(UtileriasJava.convertirMoneda(rst.getString(PagoDet.CAMPO_MONTO), tasaCambio));
                item.setNumAutorizacion(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, PagoDet.CAMPO_NUM_AUTORIZACION));
                item.setBanco(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PagoDet.CAMPO_BANCO));
                item.setMarcaTarjeta(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PagoDet.CAMPO_MARCA_TARJETA));
                item.setDigitosTarjeta(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, PagoDet.CAMPO_DIGITOS_TARJETA));
                item.setNumeroCheque(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PagoDet.CAMPO_NUM_CHEQUE));
                item.setFechaEmision(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, PagoDet.CAMPO_FECHA_EMISION));
                item.setNumeroReserva(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, PagoDet.CAMPO_NUM_RESERVA));
                item.setCuentaCliente(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, PagoDet.CAMPO_NO_CUENTA));

                res.add(item);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return res;
    }

    public static String getNombrePanelRuta(Connection conn, String idJornada, BigDecimal idPais, String codArea) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String result="";
        StringBuilder query =  new StringBuilder();
        query.append("SELECT CASE ");
        query.append( "WHEN DESCRIPCION_TIPO='RUTA' THEN (SELECT NOMBRE FROM TC_SC_RUTA WHERE TCSCRUTAID = J.IDTIPO AND TCSCCATPAISID=J.TCSCCATPAISID) ");
        query.append( "WHEN DESCRIPCION_TIPO='PANEL' THEN (SELECT NOMBRE FROM TC_SC_PANEL WHERE TCSCPANELID = J.IDTIPO AND TCSCCATPAISID=J.TCSCCATPAISID) ");
        query.append("END NOMBREPANELRUTA FROM" + ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea));
        query.append( " J WHERE TCSCCATPAISID =? AND TCSCJORNADAVENID = ?");

            log.debug("Query getNombrePanelRuta: " + query.toString());
        try {
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setBigDecimal(1, idPais);
            pstmt.setBigDecimal(2, new BigDecimal(idJornada));
            rst = pstmt.executeQuery();

            while (rst.next()) {
                result = rst.getString("NOMBREPANELRUTA");
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return result;
    }

    /* DETALLE DE VENTAS */
    public static OutputArticuloVenta getDetalleVenta(Connection conn, List<Filtro> condiciones,
            InputGetDetalle inputDet, BigDecimal idPais, String codArea) throws SQLException {
        String nombreMetodo = "getDetalleVenta";
        String nombreClase = new CurrentClassGetter().getClassName();
        OutputArticuloVenta output = new OutputArticuloVenta();
        Respuesta respuesta = null;
        List<Map<String, String>> datosDetalleVenta;
        List<Order> orden = new ArrayList<Order>();
        List<ArticuloVenta> list = new ArrayList<ArticuloVenta>();

        int min = (inputDet.getMin() != null && !inputDet.getMin().equals("")) ? new Integer(inputDet.getMin()) : 0;
        int max = (inputDet.getMax() != null && !inputDet.getMax().equals("")) ? new Integer(inputDet.getMax()) : 0;

        String campos[] = {
			VentaDet.CAMPO_TCSCVENTADETID,
			VentaDet.CAMPO_TCSCVENTAID,
			VentaDet.CAMPO_TCSCBODEGAVIRTUALID,
			VentaDet.CAMPO_ARTICULO,
            "CASE WHEN " + VentaDet.CAMPO_TIPO_GRUPO_SIDRA + " = 'BONO' THEN " + VentaDet.CAMPO_OBSERVACIONES + " ELSE "
			+ "NVL((SELECT A." + ArticulosSidra.CAMPO_DESCRIPCION + " FROM " + ArticulosSidra.N_TABLA + " A WHERE A."
	                + ArticulosSidra.CAMPO_ARTICULO + " = V." + VentaDet.CAMPO_ARTICULO + 
	                " AND A." + ArticulosSidra.CAMPO_TCSCCATPAISID + " = " + idPais + "),"
	                + "(SELECT A." + Inventario.CAMPO_DESCRIPCION + " FROM " + Inventario.N_TABLA + " A WHERE A."
	                + ArticulosSidra.CAMPO_ARTICULO + " = V." + VentaDet.CAMPO_ARTICULO + 
	                " AND A." + ArticulosSidra.CAMPO_TCSCCATPAISID + " = " + idPais + " AND ROWNUM = 1)) "
            + "END AS NOM_ARTICULO",
			VentaDet.CAMPO_TIPO_INV,
			VentaDet.CAMPO_TIPO_GRUPO_SIDRA,
			VentaDet.CAMPO_SERIE,
			VentaDet.CAMPO_SERIE_ASOCIADA,
			VentaDet.CAMPO_NUM_TELEFONO,
			VentaDet.CAMPO_CANTIDAD,
			VentaDet.CAMPO_PRECIO_UNITARIO,
			VentaDet.CAMPO_PRECIO_TOTAL,
			VentaDet.CAMPO_IMPUESTO,
			VentaDet.CAMPO_DESCUENTO_SCL,
			VentaDet.CAMPO_DESCUENTO_SIDRA,
			VentaDet.CAMPO_TCSCOFERTACAMPANIAID,
			VentaDet.CAMPO_PRECIO_FINAL,
			VentaDet.CAMPO_GESTION,
			VentaDet.CAMPO_OBSERVACIONES,
			VentaDet.CAMPO_MODALIDAD,
			"(CASE WHEN " + VentaDet.CAMPO_SERIE + " IS NULL THEN 0 ELSE 1 END) AS SERIADO",
			VentaDet.CAMPO_ESTADO,
			VentaDet.CAMPO_CREADO_EL,
			VentaDet.CAMPO_CREADO_POR,
			VentaDet.CAMPO_MODIFICADO_EL,
			VentaDet.CAMPO_MODIFICADO_POR,
		};

        orden.add(new Order(VentaDet.CAMPO_ARTICULO, Order.ASC));

        datosDetalleVenta = UtileriasBD.getPaginatedData(conn, VentaDet.N_TABLA + " V", campos, condiciones, null, orden, min, max);

        String idVenta = "";
        String idDetalleVenta = "";

        if (datosDetalleVenta.isEmpty()) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_DET_VENTAS_827, null, nombreClase, nombreMetodo, null,codArea);

        } else {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null,codArea);
            String tasaCambio = UtileriasBD.getOneRecord(conn, Venta.CAMPO_TASA_CAMBIO, Venta.N_TABLA, condiciones);

            for (int x = 0; x < datosDetalleVenta.size(); x++) {
                ArticuloVenta item = new ArticuloVenta();
                idVenta = inputDet.getIdVenta();
                idDetalleVenta = datosDetalleVenta.get(x).get(VentaDet.CAMPO_TCSCVENTADETID);

                item.setIdVentaDet(datosDetalleVenta.get(x).get(VentaDet.CAMPO_TCSCVENTADETID));
                item.setArticulo(datosDetalleVenta.get(x).get(VentaDet.CAMPO_ARTICULO));
                item.setDescripcion(datosDetalleVenta.get(x).get("NOM_ARTICULO"));
                item.setCantidad(datosDetalleVenta.get(x).get(VentaDet.CAMPO_CANTIDAD));
                item.setSeriado(datosDetalleVenta.get(x).get("SERIADO"));
                item.setSerie(UtileriasJava.getValue(datosDetalleVenta.get(x).get(VentaDet.CAMPO_SERIE)));
                item.setSerieAsociada(UtileriasJava.getValue(datosDetalleVenta.get(x).get(VentaDet.CAMPO_SERIE_ASOCIADA)));
                item.setTipoInv(datosDetalleVenta.get(x).get(VentaDet.CAMPO_TIPO_INV));
                item.setTipoGrupoSidra(datosDetalleVenta.get(x).get(VentaDet.CAMPO_TIPO_GRUPO_SIDRA));
                item.setPrecio(UtileriasJava.convertirMoneda(datosDetalleVenta.get(x).get(VentaDet.CAMPO_PRECIO_UNITARIO), tasaCambio));
                item.setGestion(UtileriasJava.getValue(datosDetalleVenta.get(x).get(VentaDet.CAMPO_GESTION)));
                item.setDescuentoSCL(UtileriasJava.convertirMoneda(datosDetalleVenta.get(x).get(VentaDet.CAMPO_DESCUENTO_SCL), tasaCambio));
                item.setNumTelefono(UtileriasJava.getValue(datosDetalleVenta.get(x).get(VentaDet.CAMPO_NUM_TELEFONO)));

                item.setDescuentoSidra(UtileriasJava.convertirMoneda(datosDetalleVenta.get(x).get(VentaDet.CAMPO_DESCUENTO_SIDRA), tasaCambio));
                item.setDetalleDescuentosSidra(descuentosArticulo(conn, idVenta, idDetalleVenta, tasaCambio));

                item.setImpuesto(UtileriasJava.convertirMoneda(UtileriasJava.getValue(datosDetalleVenta.get(x).get(VentaDet.CAMPO_IMPUESTO)), tasaCambio));
                item.setPrecioTotal(UtileriasJava.convertirMoneda(datosDetalleVenta.get(x).get(VentaDet.CAMPO_PRECIO_FINAL), tasaCambio));
                item.setModalidad(UtileriasJava.getValue(datosDetalleVenta.get(x).get(VentaDet.CAMPO_MODALIDAD)));
                item.setObservaciones(datosDetalleVenta.get(x).get(VentaDet.CAMPO_OBSERVACIONES));
                item.setEstado(datosDetalleVenta.get(x).get(VentaDet.CAMPO_ESTADO));

                item.setImpuestosArticulo(impuestosArticulo(conn, idVenta, idDetalleVenta, tasaCambio));

                list.add(item);
            }

            output.setDetalleVenta(list);
        }

        output.setRespuesta(respuesta);

        return output;
    }

    private static List<Descuento> descuentosArticulo(Connection conn, String idVenta, String idDetalleVenta,
            String tasaCambio) throws SQLException {
        List<Descuento> res = new ArrayList<Descuento>();
        Descuento item = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        String query = "SELECT D.TCSCDESCUENTOSIDRAID, D.TCSCOFERTACAMPANIAID, OC.NOMBRE, D.VALOR "
                + "FROM TC_SC_DESCUENTO_SIDRA D, TC_SC_OFERTA_CAMPANIA OC "
                + "WHERE D.TCSCVENTAID = ? AND TCSCVENTADETID = ?"
                + " AND D.TCSCOFERTACAMPANIAID = OC.TCSCOFERTACAMPANIAID";

        log.debug("Query para obtener descuentos: " + query);
     try {
        pstmt = conn.prepareStatement(query);
        pstmt.setBigDecimal(1, new BigDecimal(idVenta));
        pstmt.setBigDecimal(2, new BigDecimal(idDetalleVenta));
            rst = pstmt.executeQuery();

            while (rst.next()) {
                item = new Descuento();
                item.setIdOfertaCampania(rst.getString("NOMBRE"));
                item.setNombreOfertaCampania(rst.getString("VALOR"));
                item.setDescuento(UtileriasJava.convertirMoneda(rst.getString("VALOR"), tasaCambio));

                res.add(item);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return res;
    }

    public static List<Impuesto> impuestosArticulo(Connection conn, String idVenta, String idDetalleVenta,
            String tasaCambio) throws SQLException {
        List<Impuesto> res = new ArrayList<Impuesto>();
        Impuesto item = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

   
        String query = "SELECT I.IMPUESTO, I.VALOR "
                + "  FROM TC_SC_PAGO_IMPUESTO I"
                + " WHERE I.TCSCVENTAID = ? AND TCSCVENTADETID = ?";

        log.debug("Query para obtener impuestosArticulo: " + query);
     try {
	        pstmt = conn.prepareStatement(query);
	        pstmt.setBigDecimal(1, new BigDecimal(idVenta));
	        pstmt.setBigDecimal(2, new BigDecimal(idDetalleVenta));
            rst = pstmt.executeQuery();

            while (rst.next()) {
                item = new Impuesto();
                item.setNombreImpuesto(rst.getString("IMPUESTO"));
                item.setValor(UtileriasJava.convertirMoneda(rst.getString("VALOR"), tasaCambio));

                res.add(item);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return res;
    }

    public static List<InputDetCondicionOferta> obtenerOfertasVenta(Connection conn, String idPanelRuta,
            String tipoPanelRuta, String estadoAlta, String tipoOferta, String tipoGestionVenta, String codArea, BigDecimal idPais) throws SQLException {
        List<InputDetCondicionOferta> list = new ArrayList<InputDetCondicionOferta>();
        InputDetCondicionOferta item = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

      
	        String tipoOfertaVenta = UtileriasJava.getConfig(conn, Conf.GRUPO_CONDICION_TIPOOFERTA, Conf.CONDICION_OFERTA_VENTA,codArea);
	        String tipoCondicionGenerico = UtileriasJava.getConfig(conn, Conf.GRUPO_CONDICION_TIPO, Conf.TIPO_CONDICION_GENERICO, codArea);

	        String query = "SELECT A.TCSCOFERTACAMPANIAID, "
	                + "C.TIPO_DESCUENTO, "
	                + "C.VALOR_DESCUENTO, "
	                + "C.MONTO_INICIAL, "
	                + "C.MONTO_FINAL "
	                + "FROM TC_SC_DET_PANELRUTA A, TC_SC_CONDICION B, TC_SC_DET_CONDICION_OFERTA C "
	                + "WHERE A.TCSCTIPOID = ?" 
	                    + " AND UPPER(A.TIPO) = ? "
	                    + "AND UPPER(A.ESTADO) =? "
	                    + "AND A.TCSCOFERTACAMPANIAID = B.TCSCOFERTACAMPANIAID "
	                    + "AND UPPER(B.TIPO_OFERTACAMPANIA) =? "
	                    + "AND UPPER(B.TIPO_CONDICION) = ? "
	                    + "AND UPPER(B.TIPO_GESTION) =? "
	                    + "AND UPPER(B.ESTADO) = ?"
	                    + "AND B.TCSCCONDICIONID = C.TCSCCONDICIONID "
	                    + "AND UPPER(C.TIPO_OFERTA) =? "
	                    + "AND UPPER(C.ESTADO) = ? "
	                    + "AND A.TCSCCATPAISID = B.TCSCCATPAISID "
	                    + "AND C.TCSCCATPAISID = B.TCSCCATPAISID "
	                    + "AND A.TCSCCATPAISID = ?";

            log.debug("Query para obtener ofertas por venta: " + query);
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, new BigDecimal(idPanelRuta));
            pstmt.setString(2, tipoPanelRuta.toUpperCase());
            pstmt.setString(3, estadoAlta.toUpperCase());
            pstmt.setString(4, tipoOferta.toUpperCase() );
            pstmt.setString(5, tipoCondicionGenerico.toUpperCase() );
            pstmt.setString(6, tipoGestionVenta.toUpperCase());
            pstmt.setString(7,  estadoAlta.toUpperCase());
            pstmt.setString(8, tipoOfertaVenta.toUpperCase());
            pstmt.setString(9, estadoAlta.toUpperCase());
            pstmt.setBigDecimal(10, idPais);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                item = new InputDetCondicionOferta();
                item.setIdOfertaCampania(rst.getString("TCSCOFERTACAMPANIAID"));
                item.setTipoDescuento(rst.getString("TIPO_DESCUENTO"));
                item.setValorDescuento(rst.getString("VALOR_DESCUENTO"));
                item.setMontoInicial(rst.getString("MONTO_INICIAL"));
                item.setMontoFinal(rst.getString("MONTO_FINAL"));

                list.add(item);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return list;
    }

    /***
     * M\u00E9todo para obtener catologos de art\u00EDculos que pertenecen a telca pero
     * son utilizados en SIDRA.
     * 
     * @param conn
     * @param filtros
     * @return
     * @throws SQLException
     */
    public static List<ArticulosSidra> getCatArticulo(Connection conn, List<Filtro> filtros, BigDecimal idPais) throws SQLException {
        List<ArticulosSidra> lstArticulos = new ArrayList<ArticulosSidra>();
        ArticulosSidra objArticulo = new ArticulosSidra();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String query = queryCatArticulo(filtros);
        log.debug("Query para obtener historico:" + query);
        
        try {
	        pstmt = conn.prepareStatement(query);
	        pstmt.setBigDecimal(1, idPais);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                objArticulo = new ArticulosSidra();

                objArticulo.setArticulo(rst.getBigDecimal(ArticulosSidra.CAMPO_ARTICULO));
                objArticulo.setDescripcion(rst.getString(ArticulosSidra.CAMPO_DESCRIPCION));
                objArticulo.setTipo_grupo_sidra(rst.getString(ArticulosSidra.CAMPO_TIPO_GRUPO_SIDRA));
                objArticulo.setEstado(rst.getString(ArticulosSidra.CAMPO_ESTADO));
                objArticulo.setTcscarticuloinvid(rst.getBigDecimal(ArticulosSidra.CAMPO_TCSCARTICULOINVID));

                lstArticulos.add(objArticulo);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return lstArticulos;
    }
    
    public static String queryCatArticulo(List<Filtro> filtros) {
        String query = "SELECT * FROM TC_SC_ARTICULO_INV WHERE TCSCCATPAISID = ?";

        if (!filtros.isEmpty()) {
            for (int i = 0; i < filtros.size(); i++) {
                if (filtros.get(i).getOperator().toString() == "between") {
                    query += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator() + filtros.get(i).getValue();
                } else {
                    query += " AND " + filtros.get(i).getField() + " " + filtros.get(i).getOperator() + "" + filtros.get(i).getValue() + "";
                }
            }
        }
        return query;
    }

    public static Respuesta validarImpuestosExentos(Connection conn, String listadoImpuestosExentos, String estadoAlta, String   codArea, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "validarImpuestosExentos";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = new Respuesta();
        PreparedStatement pstmt = null;
        ResultSet rst = null;

     
	        String sql =OperacionVentas.validaImp( listadoImpuestosExentos,  estadoAlta, idPais);

            log.debug("Qry impuestos exentos: " + sql);
        try {
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();
            sql = ""; // reutilizo la variable

            if (rst.next()) {
                do {
                    sql += rst.getString("IMPUESTO") + ", ";
                } while (rst.next());
            }

            if (!sql.equals("")) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_IMPUESTOS_EXENTO_811, null,
                        nombreClase, nombreMetodo, sql.substring(0, sql.length() - 2), codArea);
                return respuesta;
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);
        }
        return null;
    }

    public static void insertaDetImpuestoExento(Connection conn, List<Impuesto> impuestosExento, BigDecimal idVenta,
            String usuario) throws SQLException {
        if (impuestosExento != null && !impuestosExento.isEmpty()) {
            String valores = "";
            PreparedStatement pstmt = null;
            String insert = "";

            try {
                List<String> listaInserts = new ArrayList<String>();

                String campos[] = {
                    Exento.CAMPO_TCSCEXENTOID,
                    Exento.CAMPO_TCSCVENTAID,
                    Exento.CAMPO_DESCRIPCION,
                    Exento.CAMPO_CREADO_POR,
                    Exento.CAMPO_CREADO_EL
                };

                for (Impuesto obj : impuestosExento) {
                    valores = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, Exento.SEQUENCE, Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idVenta.toString(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO, obj.getNombreImpuesto(), Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_TEXTO, usuario, Conf.INSERT_SEPARADOR_SI)
                        + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_NO);

                    listaInserts.add(valores);
                }

                // armando insert
                insert = UtileriasBD.armarQueryInsertAll(Exento.N_TABLA, campos, listaInserts);

                pstmt = conn.prepareStatement(insert);
                pstmt.executeUpdate();

            } finally {
                DbUtils.closeQuietly(pstmt);
            }
        }
    }
}

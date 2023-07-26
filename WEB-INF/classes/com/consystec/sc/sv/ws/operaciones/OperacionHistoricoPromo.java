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

import com.consystec.sc.ca.ws.input.historico.InputHistoricoPromoArt;
import com.consystec.sc.ca.ws.input.historico.InputHistoricoPromoCampania;
import com.consystec.sc.ca.ws.input.historico.InputHistoricoPromoCliente;
import com.consystec.sc.ca.ws.input.historico.InputHistoricoPromocionales;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.historico.OutputHistoricoPromo;
import com.consystec.sc.sv.ws.metodos.CtrlHistoricoPromo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class OperacionHistoricoPromo {
	private OperacionHistoricoPromo(){}
    private static final Logger log = Logger.getLogger(OperacionHistoricoPromo.class);
    
    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputHistoricoPromocionales
     * @throws SQLException
     */
    public static OutputHistoricoPromo doGet(Connection conn, InputHistoricoPromocionales input, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();
        String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
        String invSidra = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPOINV, Conf.SOL_TIPOINV_SIDRA, input.getCodArea());
        String tipoRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_RUTA, input.getCodArea());
        
        Respuesta respuesta;
        OutputHistoricoPromo output = new OutputHistoricoPromo();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        
        String sql = "SELECT "

                + "/* DATOS CLIENTE */ "
                + "B.TIPO, "
                + "B.IDTIPO, "
                
                + "/* DATOS CAMPANIA */ "
                + "A.TCSCOFERTACAMPANIAID, "
                
                + "/* DATOS ARTICULOS */ "
                + "A.ARTICULO, "
                + "(SELECT E.DESCRIPCION "
                    + "FROM TC_SC_ART_PROMOCIONAL E "
                    + "WHERE E.TCSCARTPROMOCIONALID = A.ARTICULO AND TCSCCATPAISID=?) "
                + "AS DESCRIPCION, "
                + "A.TIPO_INV, "
                + "A.CANTIDAD "
           + "FROM TC_SC_VENTA B, TC_SC_VENTA_DET A";

        String filtros = CtrlHistoricoPromo.obtenerCondiciones(input, estadoAlta, invSidra, tipoRuta, idPais);
        
        sql += filtros;

        log.trace("QryConsulta: " + sql);
        try{
        pstmt = conn.prepareStatement(sql);
        pstmt.setBigDecimal(1, idPais);
        rst = pstmt.executeQuery();

        if (rst != null) {
            if (!rst.next()) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_HIST_PROMO_393, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output = new OutputHistoricoPromo();
                output.setRespuesta(respuesta);
            } else {
                respuesta=new ControladorBase()
                        .getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, input.getCodArea());

                List<InputHistoricoPromoArt> list = new ArrayList<InputHistoricoPromoArt>();

                do {
                    InputHistoricoPromoArt item = new InputHistoricoPromoArt();
                    item.setTipoCliente(rst.getString("TIPO"));
                    item.setIdTipo(rst.getString("IDTIPO"));
                    item.setIdCampania(rst.getString("TCSCOFERTACAMPANIAID"));
                    item.setIdArticulo(rst.getString("ARTICULO"));
                    item.setDescripcion(rst.getString("DESCRIPCION"));
                    item.setCantidad(rst.getString("CANTIDAD"));
                    item.setTipoInv(rst.getString("TIPO_INV"));

                    list.add(item);
                } while (rst.next());

                // se agrupan los resultados
                List<InputHistoricoPromoArt> articulos ;
                List<Integer> indexArticulosAgrupados = new ArrayList<Integer>();

                List<InputHistoricoPromoCampania> datosCampanias = getDatosCampania(conn, filtros, input.getCodArea());
                List<InputHistoricoPromoCliente> datosClientes = getListClientes(conn, filtros, datosCampanias, input.getCodArea());

                for (int i = 0; i < datosClientes.size(); i++) {

                    for (int j = 0; j < datosClientes.get(i).getCampanias().size(); j++) {

                        articulos = new ArrayList<InputHistoricoPromoArt>();
                        for (int k = 0; k < list.size(); k++) {
                            if (indexArticulosAgrupados.contains(k))
                                continue;

                            if (list.get(k).getIdTipo().equals(datosClientes.get(i).getIdTipo())
                                    && list.get(k).getTipoCliente().equals(datosClientes.get(i).getTipoCliente())
                                    && list.get(k).getIdCampania().equals(datosClientes.get(i).getCampanias().get(j).getIdCampania())) {

                                list.get(k).setIdCampania(null);
                                list.get(k).setIdTipo(null);
                                list.get(k).setTipoCliente(null);

                                articulos.add(list.get(k));
                                indexArticulosAgrupados.add(k);
                            }
                        }
                        datosClientes.get(i).getCampanias().get(j).setArticulos(articulos);
                    }
                }

                if (indexArticulosAgrupados.size() != list.size()) {
                    log.error("no se han agregado todos los articulos");
                }

                output = new OutputHistoricoPromo();
                output.setRespuesta(respuesta);
                output.setClientes(datosClientes);
            }
        }
        }finally{
        	DbUtils.closeQuietly(rst);
        	DbUtils.closeQuietly(pstmt);
        }
        return output;
    }
    
    public static List<InputHistoricoPromoCampania> getDatosCampania(Connection conn, String filtros, String codArea)
            throws SQLException {
        InputHistoricoPromoCampania item = new InputHistoricoPromoCampania();
        List<InputHistoricoPromoCampania> list = new ArrayList<InputHistoricoPromoCampania>();

        PreparedStatement pstmt = null;
        ResultSet rst = null;

      
	        String sql =queryDatosCampania(filtros, codArea);
	
	        log.debug("QryDatosCampania: " + sql);
	    try{
	        pstmt = conn.prepareStatement(sql);
	        rst = pstmt.executeQuery();
	
	        if (rst != null) {
	            if (!rst.next()) {
	                log.debug("No existen registros en la tabla con esos par\u00E9metros.");
	            } else {
	                do {
	                    item = new InputHistoricoPromoCampania();
	                    item.setIdTipo(rst.getString("IDTIPO"));
	                    item.setIdCampania(rst.getString("TCSCOFERTACAMPANIAID"));
	                    item.setNombreCampania(rst.getString("NOMBRE_CAMPANIA"));
	                    item.setCantPromocionales(rst.getString("CANT_PROMOS"));
	
	                    list.add(item);
	                } while (rst.next());
	            }
	        }
        }finally{

        	DbUtils.closeQuietly(rst);
        	DbUtils.closeQuietly(pstmt);
        }
        return list;
    }

    public static String queryDatosCampania(String filtros, String codArea) {
    	   String sql = "SELECT "
	                + "B.IDTIPO, "
	                + "A.TCSCOFERTACAMPANIAID, "
	                    + "(SELECT C.NOMBRE "
	                    + "FROM TC_SC_OFERTA_CAMPANIA  PARTITION ("+ControladorBase.getPais(codArea)+") C  "
	                    + "WHERE C.TCSCOFERTACAMPANIAID = A.TCSCOFERTACAMPANIAID"
	                + ") AS NOMBRE_CAMPANIA, "
	                + "SUM (A.CANTIDAD) AS CANT_PROMOS "
	                + "FROM TC_SC_VENTA B, TC_SC_VENTA_DET A";
	        
	        sql += filtros + " GROUP BY A.TCSCOFERTACAMPANIAID, B.IDTIPO";
	        
	        return sql;
    }
    public static List<InputHistoricoPromoCliente> getListClientes(Connection conn, String filtros,
            List<InputHistoricoPromoCampania> datosCampanias, String codArea) throws SQLException {
        InputHistoricoPromoCliente item = new InputHistoricoPromoCliente();
        List<InputHistoricoPromoCliente> list = new ArrayList<InputHistoricoPromoCliente>();

        PreparedStatement pstmt = null;
        ResultSet rst = null;

       
	        String sql =getListaClientesQUERY(filtros, codArea);
	
	        log.debug("QryListClientes: " + sql);
	    try{
	        pstmt = conn.prepareStatement(sql);
	        rst = pstmt.executeQuery();
	
	        if (rst != null) {
	            if (!rst.next()) {
	                log.debug("No existen registros en la tabla con esos par\u00E9metros.");
	            } else {
	                do {
	                    item = new InputHistoricoPromoCliente();
	                    item.setTipoCliente(rst.getString("TIPO"));
	                    item.setIdTipo(rst.getString("IDTIPO"));
	                                        
	                    List<InputHistoricoPromoCampania> campanias = new ArrayList<InputHistoricoPromoCampania>();
	                    
	                        for (int k = 0; k < datosCampanias.size(); k++) {
	                            log.debug(k+": "+ datosCampanias.get(k).getIdTipo());
	                            log.debug(k+": "+ rst.getString("IDTIPO"));
	                            if (datosCampanias.get(k).getIdTipo().equalsIgnoreCase(rst.getString("IDTIPO"))) {
	
	                                    campanias.add(datosCampanias.get(k));
	                                
	                            }
	                    }
	                   
	                    item.setCampanias(campanias);
	
	                    list.add(item);
	                } while (rst.next());
	            }
	        }
        }finally{

        	DbUtils.closeQuietly(rst);     	
        	DbUtils.closeQuietly(pstmt);
        }
        return list;
    }
    
    public static String getListaClientesQUERY(String filtros, String codArea) {
    	 String sql = "SELECT DISTINCT "
	                + "B.TIPO, "
	                + "B.IDTIPO "
	                + "FROM TC_SC_VENTA PARTITION("+ControladorBase.getPais(codArea)+") B, TC_SC_VENTA_DET A";
	        
	        sql += filtros;
	        return sql;
    }
}

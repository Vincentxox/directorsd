package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.oferCom.InputConsultaArticulos;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.oferCom.OutputConsultaArticulos;
import com.consystec.sc.sv.ws.orm.ArticulosSidra;
import com.consystec.sc.sv.ws.orm.Catalogo;
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
public class OperacionArticulo {
	
	private OperacionArticulo(){}
    private static final Logger log = Logger.getLogger(OperacionArticulo.class);
    // TODO cambiar funciones espec\u00EDficas por pais

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputConsultaArticulos
     * @throws SQLException
     */
    public static OutputConsultaArticulos doGet(Connection conn, String codArea, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<InputConsultaArticulos> list = new ArrayList<InputConsultaArticulos>();

        Respuesta respuesta = null;
        OutputConsultaArticulos output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        String sql = null;
        String campos[] = {
            ArticulosSidra.CAMPO_ARTICULO,
            ArticulosSidra.CAMPO_TIPO_GRUPO_SIDRA,
            ArticulosSidra.CAMPO_DESCRIPCION
            
        };

        List<Filtro> condiciones = new ArrayList<Filtro>();
        List<Order> orden = new ArrayList<Order>();

        try {
	        condiciones.add(UtileriasJava.getCondicionDefault(Conf.FILTRO_ESTADO, ArticulosSidra.CAMPO_ESTADO, conn, codArea));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ArticulosSidra.CAMPO_TCSCCATPAISID, idPais+""));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND_NEQ, ArticulosSidra.CAMPO_TIPO_GRUPO_SIDRA, Conf.TIPO_GRUPO_BONO));
            orden.add(new Order(ArticulosSidra.CAMPO_DESCRIPCION, Order.ASC));

            sql = UtileriasBD.armarQuerySelect(ArticulosSidra.N_TABLA, campos, condiciones, orden);
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSOS, null, nombreClase,
                            nombreMetodo, null, codArea);

                    output = new OutputConsultaArticulos();
                    output.setRespuesta(respuesta);
                } else {
                    log.trace("Datos obtenidos.");
                    respuesta = new Respuesta();
                    respuesta=new ControladorBase()
                            .getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, codArea);

                    do {
                        InputConsultaArticulos item = new InputConsultaArticulos();
                        item.setIdArticulo(rst.getString(ArticulosSidra.CAMPO_ARTICULO));
                        item.setCodArticulo("");
                        item.setDescArticulo(rst.getString(ArticulosSidra.CAMPO_DESCRIPCION));
                        item.setTipoGrupoSidra(rst.getString(ArticulosSidra.CAMPO_TIPO_GRUPO_SIDRA));

                        list.add(item);
                    } while (rst.next());

                    output = new OutputConsultaArticulos();
                    output.setRespuesta(respuesta);
                    output.setArticulos(list);
                }
            }
        } finally {

            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return output;
    }

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputConsultaArticulos
     * @throws SOAPException 
     * @throws Exception 
     */
    //* El Salvador
    public static OutputConsultaArticulos doGetPrecio(Connection conn, InputConsultaArticulos input)
            throws SOAPException, Exception {
        String nombreMetodo = "doGetPrecio";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<InputConsultaArticulos> list = new ArrayList<InputConsultaArticulos>();

        Respuesta respuesta = null;
        OutputConsultaArticulos output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, "ARTICULOS_TIPOGESTION"));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE, input.getTipoGestion()));

        String tipoGestion = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_VALOR, Catalogo.N_TABLA, condiciones);
        if (tipoGestion.equals("")) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DATOS_TIPOGESTION, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());

            output = new OutputConsultaArticulos();
            output.setRespuesta(respuesta);
            return output;
        }

        String sql = "SELECT A.TESARTICULOSINVID ARTICULO, "
                + "D.COD_ARTICULO_SAP, "
                + "B.DESCRIPCION, "
                + "A.TCTIPOSPRECIOID, "
                + "C.DES_TIPO_PRECIO, "
                + "A.PRECIO PRECIO_SIN_IVA, "
                + "A.ESTADO "
                + "FROM TC_PRECIOS_ARTICULOS A, "
                     + "TES_ARTICULOS_INV    B, "
                     + "TC_TIPOS_PRECIO      C, "
                     + "TES_ARTICULOS_INT    D "
                + "WHERE A.ESTADO = 'ALTA' "
                + "AND   A.TESARTICULOSINVID = B.TESARTICULOSINVID "
                + "AND   A.TCTIPOSPRECIOID = C.TCTIPOSPRECIOID "
                + "AND   A.TESARTICULOSINVID = D.TESARTICULOSINVID "
                + "AND   B.TESARTICULOSINVID = " + input.getIdArticulo()  + " "
                + "AND   C.TCTIPOSPRECIOID in (" + tipoGestion + ")";

        log.debug(sql);
       try{
	        pstmt = conn.prepareStatement(sql);
	        rst = pstmt.executeQuery();
	
	        if (rst != null) {
	            if (!rst.next()) {
	                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSOS, null, nombreClase,
	                        nombreMetodo, null, input.getCodArea());
	
	                output = new OutputConsultaArticulos();
	                output.setRespuesta(respuesta);
	            } else {
	                log.trace("Datos obtenidos.");
	                respuesta=new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
	                    nombreClase, nombreMetodo, null, input.getCodArea());
	
	                do {
	                    InputConsultaArticulos item = new InputConsultaArticulos();
	                    item.setDesTipoPrecio(rst.getString("DES_TIPO_PRECIO"));
	                    item.setDescArticulo(rst.getString("DESCRIPCION"));
	                    item.setPrecioArticulo(UtileriasJava.redondear(rst.getBigDecimal("PRECIO_SIN_IVA"), 2) + "");
	
	                    list.add(item);
	                } while (rst.next());
	
	                output = new OutputConsultaArticulos();
	                output.setRespuesta(respuesta);
	                output.setArticulos(list);
	            }
	        }
       }finally{
        DbUtils.closeQuietly(rst);
        DbUtils.closeQuietly(pstmt);
       }
        return output;
    }

}

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

import com.consystec.sc.ca.ws.input.ofertacampania.InputOfertaCampaniaMovil;
import com.consystec.sc.ca.ws.input.ofertacampania.InputPromoOfertaCampaniaDet;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputOfertaCampaniaMovil;
import com.consystec.sc.sv.ws.metodos.CtrlOfertaCampaniaMovil;
import com.consystec.sc.sv.ws.orm.OfertaCampania;
import com.consystec.sc.sv.ws.orm.PromoOfertaCampania;
import com.consystec.sc.sv.ws.orm.Promocionales;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes - Consystec - 2016
 *
 */
public class OperacionOfertaCampaniaMovil {
	private OperacionOfertaCampaniaMovil(){}
    private static final Logger log = Logger.getLogger(OperacionOfertaCampaniaMovil.class);

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputOfertaCampaniaMovil
     * @throws SQLException
     */
    public static OutputOfertaCampaniaMovil doGet(Connection conn, InputOfertaCampaniaMovil input, int metodo, BigDecimal ID_PAIS)
            throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();
        String invSidra = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPOINV, Conf.SOL_TIPOINV_SIDRA, input.getCodArea());
        List<InputOfertaCampaniaMovil> list = new ArrayList<InputOfertaCampaniaMovil>();
        Respuesta respuesta = null;
        OutputOfertaCampaniaMovil output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql = null;
        List<Filtro> condiciones = new ArrayList<Filtro>();
        List<Order> orden = new ArrayList<Order>();

        try {
            String campos[] = CtrlOfertaCampaniaMovil.obtenerCamposGetPost();
            condiciones = CtrlOfertaCampaniaMovil.obtenerCondiciones(conn, input, metodo, ID_PAIS);
            orden.add(new Order(OfertaCampania.CAMPO_NOMBRE, Order.ASC));

            sql = UtileriasBD.armarQuerySelect(OfertaCampania.N_TABLA, campos, condiciones, orden);
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_CAMPANIAS_390, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputOfertaCampaniaMovil();
                    output.setRespuesta(respuesta);
                } else {
                    respuesta = new Respuesta();
                    respuesta=new ControladorBase()
                            .getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, input.getCodArea());

                    String idOfertaCampania = "";
	                do {
	                    InputOfertaCampaniaMovil item = new InputOfertaCampaniaMovil();
	                    idOfertaCampania = rst.getString(OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID);
	                    item.setIdOfertaCampania(idOfertaCampania);
	                    item.setTipo(rst.getString(OfertaCampania.CAMPO_TIPO));
	                    item.setNombre(rst.getString(OfertaCampania.CAMPO_NOMBRE));
	                    item.setDescripcion(rst.getString(OfertaCampania.CAMPO_DESCRIPCION));
	                    item.setCantMaxPromocionales(rst.getString(OfertaCampania.CAMPO_CANT_MAX_PROMOCIONALES));
	                    item.setFechaDesde(UtileriasJava.formatStringDate(rst.getString(OfertaCampania.CAMPO_FECHA_DESDE)));
	                    item.setFechaHasta(UtileriasJava.formatStringDate(rst.getString(OfertaCampania.CAMPO_FECHA_HASTA)));
	                    item.setEstado(rst.getString(OfertaCampania.CAMPO_ESTADO));
	                    item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(OfertaCampania.CAMPO_CREADO_EL)));
	                    item.setCreado_por(rst.getString(OfertaCampania.CAMPO_CREADO_POR));
	                    item.setModificado_el(UtileriasJava.formatStringDate(rst.getString(OfertaCampania.CAMPO_MODIFICADO_EL)));
	                    item.setModificado_por(rst.getString(OfertaCampania.CAMPO_MODIFICADO_POR));
	                    
	                    List<InputPromoOfertaCampaniaDet> detalles = getDatosTablaHija(conn, idOfertaCampania,  invSidra);
	                    item.setArticulosPromocionales(detalles);

                        list.add(item);
                    } while (rst.next());

                    output = new OutputOfertaCampaniaMovil();
                    output.setRespuesta(respuesta);
                    output.setOfertaCampania(list);
                }
            }
        } finally {

            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return output;
    }

    /**
     * Funci\u00F3n que obtiene los datos relacionados de la tabla hija mediante el id de la tabla padre.
     * 
     * @param conn
     * @param idPadre
     * @param input
     * @param invSidra 
     * @param metodo
     * @return OutputBodegaDTS
     * @throws SQLException
     */
    private static List<InputPromoOfertaCampaniaDet> getDatosTablaHija(Connection conn, String idPadre,
            String invSidra) throws SQLException {
        List<InputPromoOfertaCampaniaDet> list = new ArrayList<InputPromoOfertaCampaniaDet>();
        PreparedStatement pstmtIn = null;
        ResultSet rstIn = null;
        List<Filtro> condicionesInterno = new ArrayList<Filtro>();
        String tablas[] = {
            PromoOfertaCampania.N_TABLA,
            Promocionales.N_TABLA
        };
        String[][] camposInterno = CtrlOfertaCampaniaMovil.obtenerCamposTablaHija();

        try {
	        condicionesInterno.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, idPadre));
	        condicionesInterno.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Promocionales.N_TABLA, Promocionales.CAMPO_TCSCARTPROMOCIONALID, PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_TCSCARTPROMOCIONALID));

            String sql = UtileriasBD.armarQueryGetMultiple(camposInterno, tablas, condicionesInterno, null);

            pstmtIn = conn.prepareStatement(sql);
            rstIn = pstmtIn.executeQuery();

            if (rstIn != null) {
                if (!rstIn.next()) {
	                log.debug("No existen registros en la tabla hija con esos parametros.");
	                InputPromoOfertaCampaniaDet item = new InputPromoOfertaCampaniaDet();
	                list.add(item);
	            } else {
	                do {
	                    InputPromoOfertaCampaniaDet item = new InputPromoOfertaCampaniaDet();
	                    item.setIdPromoOfertaCampania(rstIn.getString(PromoOfertaCampania.CAMPO_TCSCDETARTPROMOID));
	                    item.setIdOfertaCampania(rstIn.getString(PromoOfertaCampania.CAMPO_TCSCOFERTACAMPANIAID));
	                    item.setIdArtPromocional(rstIn.getString(PromoOfertaCampania.CAMPO_TCSCARTPROMOCIONALID));
	                    item.setNombreArticulo(rstIn.getString(Promocionales.CAMPO_DESCRIPCION));
	                    item.setTipoInv(invSidra);
	                    item.setCantArticulos(rstIn.getString(PromoOfertaCampania.CAMPO_CANT_ARTICULOS));
	                    item.setEstado(rstIn.getString(PromoOfertaCampania.CAMPO_ESTADO));
	                    item.setCreado_el(UtileriasJava.formatStringDate(rstIn.getString(PromoOfertaCampania.CAMPO_CREADO_EL)));
	                    item.setCreado_por(rstIn.getString(PromoOfertaCampania.CAMPO_CREADO_POR));
	                    item.setModificado_el(UtileriasJava.formatStringDate(rstIn.getString(PromoOfertaCampania.CAMPO_MODIFICADO_EL)));
	                    item.setModificado_por(rstIn.getString(PromoOfertaCampania.CAMPO_MODIFICADO_POR));

                        list.add(item);
                    } while (rstIn.next());
                }
            }
        } finally {

            DbUtils.closeQuietly(rstIn);
            DbUtils.closeQuietly(pstmtIn);
        }
        return list;
    }
}

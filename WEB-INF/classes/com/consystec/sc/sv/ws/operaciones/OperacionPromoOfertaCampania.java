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

import com.consystec.sc.ca.ws.input.ofertacampania.InputPromoOfertaCampania;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputPromoOfertaCampania;
import com.consystec.sc.sv.ws.metodos.CtrlPromoOfertaCampania;
import com.consystec.sc.sv.ws.orm.OfertaCampania;
import com.consystec.sc.sv.ws.orm.PromoOfertaCampania;
import com.consystec.sc.sv.ws.orm.Promocionales;
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
public class OperacionPromoOfertaCampania {
private OperacionPromoOfertaCampania(){}
    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputPromoOfertaCampania
     * @throws SQLException
     */
    public static OutputPromoOfertaCampania doGet(Connection conn, InputPromoOfertaCampania input, int metodo, BigDecimal ID_PAIS)
            throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<InputPromoOfertaCampania> campanias = new ArrayList<InputPromoOfertaCampania>();
        List<InputPromoOfertaCampania> list = new ArrayList<InputPromoOfertaCampania>();

        Respuesta respuesta = null;
        OutputPromoOfertaCampania output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        try {
            String[][] campos = CtrlPromoOfertaCampania.obtenerCamposGet();
	        String tablas[] = {
	            PromoOfertaCampania.N_TABLA,
	            Promocionales.N_TABLA,
	            OfertaCampania.N_TABLA
	        };
	        
	        List<Filtro> condiciones = CtrlPromoOfertaCampania.obtenerCondiciones(input, metodo, ID_PAIS);
	        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, OfertaCampania.N_TABLA, OfertaCampania.CAMPO_TCSCOFERTACAMPANIAID, PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_TCSCOFERTACAMPANIAID));
	        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Promocionales.N_TABLA, Promocionales.CAMPO_TCSCARTPROMOCIONALID, PromoOfertaCampania.N_TABLA, PromoOfertaCampania.CAMPO_TCSCARTPROMOCIONALID));

            String sql = UtileriasBD.armarQueryGetMultiple(campos, tablas, condiciones, null);

            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_PROMO_CAMPANIA_821, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputPromoOfertaCampania();
                    output.setRespuesta(respuesta);
                } else {
                    respuesta = new Respuesta();
                    respuesta=new ControladorBase()
                            .getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, input.getCodArea());

                    do {
	                    InputPromoOfertaCampania item = new InputPromoOfertaCampania();
	                    item.setIdPromoCampania(rst.getString(PromoOfertaCampania.CAMPO_TCSCDETARTPROMOID));
	                    item.setIdOfertaCampania(rst.getString(PromoOfertaCampania.CAMPO_TCSCOFERTACAMPANIAID));
	                    item.setNombreCampania(rst.getString(OfertaCampania.CAMPO_NOMBRE));
	                    item.setIdArtPromocional(rst.getString(PromoOfertaCampania.CAMPO_TCSCARTPROMOCIONALID));
	                    item.setNombreArticulo(rst.getString(Promocionales.CAMPO_DESCRIPCION));
	                    item.setCantArticulos(rst.getString(PromoOfertaCampania.CAMPO_CANT_ARTICULOS));
	                    item.setEstado(rst.getString(PromoOfertaCampania.CAMPO_ESTADO));
	                    item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(PromoOfertaCampania.CAMPO_CREADO_EL)));
	                    item.setCreado_por(rst.getString(PromoOfertaCampania.CAMPO_CREADO_POR));
	                    item.setModificado_el(UtileriasJava.formatStringDate(rst.getString(PromoOfertaCampania.CAMPO_MODIFICADO_EL)));
	                    item.setModificado_por(rst.getString(PromoOfertaCampania.CAMPO_MODIFICADO_POR));

                        list.add(item);
                    } while (rst.next());

                    List<String> listadoCampanias = UtileriasBD.getOneField(conn,
                            "DISTINCT(" + PromoOfertaCampania.CAMPO_TCSCOFERTACAMPANIAID + ")",
                            PromoOfertaCampania.N_TABLA, null, null);
                    for (String campania : listadoCampanias) {
                        InputPromoOfertaCampania itemCampania = new InputPromoOfertaCampania();
                        List<InputPromoOfertaCampania> articulosCampania = new ArrayList<InputPromoOfertaCampania>();
                        for (InputPromoOfertaCampania item : list) {
                            if (item.getIdOfertaCampania().equalsIgnoreCase(campania)) {
                                itemCampania.setIdOfertaCampania(campania);
                                itemCampania.setNombreCampania(item.getNombreCampania());
                                articulosCampania.add(item);
                                item.setNombreCampania(null);
                            }
                        }

                        if (articulosCampania.size() > 0) {
                            itemCampania.setArticulosPromocionales(articulosCampania);
                            campanias.add(itemCampania);
                        }
                    }
                    output = new OutputPromoOfertaCampania();
                    output.setRespuesta(respuesta);
                    output.setCampanias(campanias);
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
     * @param metodo 
     * @param estadoAlta 
     * @return OutputPromoOfertaCampania
     * @throws SQLException
     */
    public static OutputPromoOfertaCampania doPost(Connection conn, InputPromoOfertaCampania input, int metodo,
            String estadoAlta, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "doPost";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = null;
        OutputPromoOfertaCampania output = new OutputPromoOfertaCampania();

        QueryRunner Qr = new QueryRunner();
   
        String sql = null;
        String campos[] = CtrlPromoOfertaCampania.obtenerCamposPost();
        List<String> inserts = CtrlPromoOfertaCampania.obtenerInsertsPost(input, PromoOfertaCampania.SEQUENCE, estadoAlta, ID_PAIS);
        List<Filtro> condiciones = CtrlPromoOfertaCampania.obtenerCondiciones(input, metodo, ID_PAIS);

        try {
            conn.setAutoCommit(false);

            sql = UtileriasBD.armarQueryDelete(PromoOfertaCampania.N_TABLA, condiciones);

            sql = UtileriasBD.armarQueryInsertAll(PromoOfertaCampania.N_TABLA, campos, inserts);
            Qr.update(conn, sql);

            if (Qr != null) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_PROMOCIONAL_CAMPANIA_51, null,
                        nombreClase, nombreMetodo, null, input.getCodArea());

                output = new OutputPromoOfertaCampania();
                output.setRespuesta(respuesta);
                conn.commit();
            }
        } finally {
            conn.setAutoCommit(true);
           
        }

        return output;
    }
}

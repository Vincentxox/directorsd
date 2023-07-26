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

import com.consystec.sc.ca.ws.input.inventario.InputConsultaCantInv;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventario.OutputConsultaCantInv;
import com.consystec.sc.sv.ws.metodos.CtrlConsultaCantInv;
import com.consystec.sc.sv.ws.orm.Inventario;
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
public class OperacionConsultaCantInv {
	private OperacionConsultaCantInv(){}
    private static final Logger log = Logger.getLogger(OperacionConsultaCantInv.class);

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputDescuento
     * @throws SQLException
     */
    public static OutputConsultaCantInv doGet(Connection conn, InputConsultaCantInv input, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();
        String idBodega0 = "0";

        Respuesta respuesta = null;
        List<InputConsultaCantInv> list = new ArrayList<InputConsultaCantInv>();
        OutputConsultaCantInv output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql = null;
        
        String campos[] = CtrlConsultaCantInv.obtenerCamposGetPost( input.getIdBodega(), idBodega0);
        
        List<Order> orden = new ArrayList<Order>();
        orden.add(new Order(Inventario.CAMPO_TCSCBODEGAVIRTUALID, Order.ASC));
        
        String[] groupBy = {
            Inventario.CAMPO_TCSCBODEGAVIRTUALID
        };
        
        List<Filtro> condiciones = CtrlConsultaCantInv.obtenerCondiciones( input, idPais );
        String articuloRecarga = UtileriasJava.getConfig(conn, Conf.GRUPO_ARTICULO_CANTIDAD, Conf.ARTICULO_RECARGA, input.getCodArea());
        if (!"".equals(articuloRecarga)) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND_NEQ, Inventario.CAMPO_ARTICULO,
                    articuloRecarga));
        }
        
        if (input.getIdBodega() == null || input.getIdBodega().equals(idBodega0)) {
            condiciones.remove(0);
            sql = UtileriasBD.armarQuerySelect(Inventario.N_TABLA, campos, condiciones);
            
        } else {
            sql = "SELECT ";
            sql += UtileriasBD.getCampos(campos);
            sql += " FROM " + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodega(), input.getCodArea());
            sql += UtileriasBD.getCondiciones(condiciones);
            sql += UtileriasBD.getGroupBy(groupBy);
            log.debug("Qry cant bodega: "+ sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSOS, null, nombreClase,
                            nombreMetodo, null, input.getCodArea());

                    output = new OutputConsultaCantInv();
                    output.setRespuesta(respuesta);
                } else {
                    respuesta = new Respuesta();
                    respuesta=new ControladorBase()
                            .getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, input.getCodArea());

                    do {
                        InputConsultaCantInv item = new InputConsultaCantInv();
                        if (input.getIdBodega() != null && !input.getIdBodega().equals(idBodega0)) {
                            item.setIdBodega(rst.getString(Inventario.CAMPO_TCSCBODEGAVIRTUALID));
                        }
                        item.setCantInv(rst.getString(Inventario.CAMPO_ARTICULO));
                        item.setCantTotalInv(rst.getString("TOTALINV"));

                        list.add(item);
                    } while (rst.next());

                    output = new OutputConsultaCantInv();
                    output.setRespuesta(respuesta);
                    output.setDatos(list);
                }
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return output;
    }
}

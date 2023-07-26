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

import com.consystec.sc.ca.ws.input.inventario.InputConsultaSeries;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventario.OutputConsultaSeries;
import com.consystec.sc.sv.ws.metodos.CtrlConsultaSeries;
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
public class OperacionConsultaSeries {
	private OperacionConsultaSeries(){}
    private static final Logger log = Logger.getLogger(OperacionConsultaSeries.class);

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputDescuento
     * @throws SQLException
     */
    public static OutputConsultaSeries doGet(Connection conn, InputConsultaSeries input, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputConsultaSeries output = new OutputConsultaSeries();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        InputConsultaSeries item = new InputConsultaSeries();
        List<InputConsultaSeries> list = new ArrayList<InputConsultaSeries>();

        try {
            int min = (input.getMin() != null && !input.getMin().equals("")) ? new Integer(input.getMin()) : 0;
            int max = (input.getMax() != null && !input.getMax().equals("")) ? new Integer(input.getMax()) : 0;

            String campos[] = CtrlConsultaSeries.obtenerCamposGetPost();
            List<Filtro> condiciones = CtrlConsultaSeries.obtenerCondiciones(input, idPais);

            List<Order> orden = new ArrayList<Order>();
            orden.add(new Order(Inventario.CAMPO_SERIE, Order.ASC));

            String sql = UtileriasBD.armarQrySelect(ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodega(), input.getCodArea()), campos,
                    condiciones, null, orden, min, max);

            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    log.debug("No existen registros en la tabla con esos par\u00E9metros.");
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSOS, null, nombreClase,
                            nombreMetodo, null, input.getCodArea());

                } else {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    output.setIdArticulo(rst.getString(Inventario.CAMPO_ARTICULO));
                    output.setDescripcion(rst.getString(Inventario.CAMPO_DESCRIPCION));
                    output.setTipoInv(rst.getString(Inventario.CAMPO_TIPO_INV));
                    output.setPrecioScl(UtileriasJava.getValue(rst.getString("PRECIO")));
                    do {
                        item = new InputConsultaSeries();
                        item.setIdInventario(rst.getString(Inventario.N_TABLA_ID));
                        item.setSerie(rst.getString(Inventario.CAMPO_SERIE));
                        item.setSerieAsociada(rst.getString(Inventario.CAMPO_SERIE_ASOCIADA));
                        if(rst.getString(Inventario.CAMPO_IMEI)==null){
                        	item.setImei("");
                        }else{
                        	item.setImei(rst.getString(Inventario.CAMPO_IMEI));
                        }

                        
                        if(rst.getString(Inventario.CAMPO_ICC)==null){
                        	item.setIcc("");
                        }else{
                        	item.setIcc(rst.getString(Inventario.CAMPO_ICC));
                        }
                        list.add(item);
                    } while (rst.next());

                    output.setSeries(list);
                }
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);


            output.setRespuesta(respuesta);
        }
        return output;
    }
}

package com.consystec.sc.sv.ws.operaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.vendedordts.InputVendedorDTS;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.vendedordts.OutputVendedorDTS;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;

/**
 * @author Victor Cifuentes @ Consystec - 2017
 *
 */
public class OperacionOficinasSCL {
	private OperacionOficinasSCL(){}
    private static final Logger log = Logger.getLogger(OperacionOficinasSCL.class);
    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     */
    public static OutputVendedorDTS doGet(Connection conn, String codArea) throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<InputVendedorDTS> list = new ArrayList<InputVendedorDTS>();
        Respuesta respuesta = new Respuesta();
        OutputVendedorDTS output = new OutputVendedorDTS();
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        try {
	        String sql = "SELECT COD_OFICINA, DES_OFICINA, "
	                + "(SELECT COD_VENDEDOR FROM AL_ASIG_DOCUMENTOS F WHERE F.COD_OFICINA = O.COD_OFICINA "
	                + "AND IND_RESERVA = 1 AND ROWNUM = 1 AND COD_VENDEDOR IS NOT NULL) COD_VENDEDOR "
	                + "FROM GE_OFICINAS O WHERE COD_OFICINA IN "
	                + "(SELECT COD_OFICINA FROM AL_DOCUM_SUCURSAL WHERE COD_TIPDOCUM = 1 AND COD_OFICINA IN "
	                + "(SELECT DISTINCT(COD_OFICINA) FROM AL_ASIG_DOCUMENTOS WHERE IND_RESERVA = 1))";

            log.debug("qry oficinas: " + sql);
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();
	
	        if (rst != null) {
	            if (!rst.next()) {
	                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_OFICINAS_SCL_868, null,
	                        nombreClase, nombreMetodo, null, codArea);
	
	                output.setRespuesta(respuesta);
	            } else {
	                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_GET_OFICINAS_SCL_74, null, nombreClase,
	                        nombreMetodo, null, codArea);
	
	                do {
	                    InputVendedorDTS item = new InputVendedorDTS();
	                    item.setCodOficina(rst.getString("COD_OFICINA"));
	                    item.setNombres(rst.getString("DES_OFICINA"));
	                    item.setCodVendedor(rst.getString("COD_VENDEDOR"));
	
	                    list.add(item);
	                } while (rst.next());
	
	                output = new OutputVendedorDTS();
	                output.setRespuesta(respuesta);
	                output.setOficinas(list);
                }
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return output;
    }
}

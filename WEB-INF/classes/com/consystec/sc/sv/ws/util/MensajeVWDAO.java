package com.consystec.sc.sv.ws.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.consystec.sc.sv.ws.orm.MensajeVW;

public class MensajeVWDAO {

    public List<MensajeVW> select(Connection conn, BigDecimal CodResultado, String area) throws SQLException {
        List<MensajeVW> registro = new ArrayList<MensajeVW>();
        PreparedStatement stm =null;
        MensajeVW Msj = new MensajeVW();
        String sSql = null;
        ResultSet rst = null;

        try {
            sSql = "SELECT * FROM TC_SC_MENSAJE_VW WHERE codresultado = ? AND estado = 1  AND tcsccatpaisid = (SELECT TCSCCATPAISID FROM TC_SC_CAT_PAIS WHERE AREA = ?) ";

            stm=conn.prepareStatement(sSql);
            stm.setBigDecimal(1, CodResultado);
            stm.setString(2, area);
            rst = stm.executeQuery();
            while (rst.next()) {
                Msj.setTcscmensajeid(rst.getBigDecimal("TCSCMENSAJEID"));
                Msj.setCodresultado(rst.getBigDecimal("CODRESULTADO"));
                Msj.setDescripcion(rst.getString("DESCRIPCION"));
                Msj.setMostrar(rst.getBigDecimal("MOSTRAR"));
                Msj.setTipo_excepcion(rst.getString("TIPO_EXCEPCION"));

                if (Msj.getTipo_excepcion() != null && Msj.getTipo_excepcion().equalsIgnoreCase("Excepcion de SQL")) {
                    Msj.setCodresultado(Msj.getCodresultado().negate());
                }
                Msj.setEstado(rst.getBigDecimal("ESTADO"));

                registro.add(Msj);
            }
        } finally {
            DbUtils.closeQuietly(stm);
            DbUtils.closeQuietly(rst);
        }

        return registro;
    }
}
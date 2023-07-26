package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.reporte.InputReporteCtaDTS;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCtaDTS;
import com.consystec.sc.sv.ws.metodos.CtrlReporteCtaDTS;
import com.consystec.sc.sv.ws.orm.CuentaDebeDTS;
import com.consystec.sc.sv.ws.orm.CuentaHaberDTS;
import com.consystec.sc.sv.ws.orm.CuentaResumenDTS;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionReporteCtaDTS extends ControladorBase{
    private static final Logger log = Logger.getLogger(OperacionReporteCtaDTS.class);
     
    public static OutputReporteCtaDTS doGetDebe(Connection conn, InputReporteCtaDTS input, BigDecimal ID_PAIS)
            throws SQLException, ParseException {
        String nombreMetodo = "doGetDebe";
        String nombreClase = new CurrentClassGetter().getClassName();
        OutputReporteCtaDTS output = new OutputReporteCtaDTS();
        List<InputReporteCtaDTS> list = new ArrayList<InputReporteCtaDTS>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        Respuesta respuesta = new Respuesta();
        String sql = "";
        SimpleDateFormat FORMATO_FECHA_GT = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA_GT);
        SimpleDateFormat FORMATO_FECHA_CORTA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
        List<Filtro> condiciones = new ArrayList<Filtro>();
        String idDTS = input.getIdDTS();
        String nombreDTS = UtileriasBD.getOneRecord(conn, Distribuidor.CAMPO_NOMBRES, Distribuidor.N_TABLA, condiciones);

        try {
            String[] campos = {
                CuentaDebeDTS.CAMPO_ID_RUTAPANEL,
                CuentaDebeDTS.CAMPO_TIPO_ID,
                CuentaDebeDTS.CAMPO_TCSCVENTAID,
                CuentaDebeDTS.CAMPO_FOLIO_SCL,
                CuentaDebeDTS.CAMPO_VENDEDOR,
                "(SELECT USUARIO FROM " + getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, idDTS, input.getCodArea()) + " V WHERE V.VENDEDOR = C."
                        + CuentaDebeDTS.CAMPO_VENDEDOR + ") USUARIO_VENDEDOR",
                "(SELECT NOMBRE || ' ' || APELLIDO FROM " + getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, idDTS, input.getCodArea()) + " V WHERE V.VENDEDOR = C."
                        + CuentaDebeDTS.CAMPO_VENDEDOR + ") NOMBRE_VENDEDOR",
                CuentaDebeDTS.CAMPO_FECHA,
                CuentaDebeDTS.CAMPO_MONTO_VENTA,
                CuentaDebeDTS.CAMPO_MONEDA,
                CuentaDebeDTS.CAMPO_TIPO_CLIENTE,
                CuentaDebeDTS.CAMPO_CREADO_EL,
                CuentaDebeDTS.CAMPO_CREADO_POR
            };

            condiciones.clear();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CuentaDebeDTS.CAMPO_TCSCDTSID, idDTS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CuentaDebeDTS.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_TRUNC, CuentaDebeDTS.CAMPO_FECHA, input.getFecha(), null, Conf.TXT_FORMATO_FECHA_CORTA_GT));
            Date fecha = FORMATO_FECHA_GT.parse(input.getFecha());
            input.setFecha(FORMATO_FECHA_CORTA.format(fecha));
            sql = UtileriasBD.armarQuerySelect(CuentaDebeDTS.N_TABLA + " SUBPARTITION(D" + ControladorBase.getPais(input.getCodArea()) + input.getFecha() + ") C", campos, condiciones);
            
            log.trace("Qry cta dts debe: " + sql);

            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_GETDATOS12, null, nombreClase,
                            nombreMetodo, null, input.getCodArea());

                    do {
                        InputReporteCtaDTS item = new InputReporteCtaDTS();
                        item.setIdDTS(idDTS);
                        item.setNombreDTS(nombreDTS);
                        item.setIdVenta(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, CuentaDebeDTS.CAMPO_TCSCVENTAID));
                        item.setIdVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, CuentaDebeDTS.CAMPO_VENDEDOR));
                        item.setNombreVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE_VENDEDOR"));
                        item.setUsuarioVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "USUARIO_VENDEDOR"));
                        item.setTotalVendido(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, CuentaDebeDTS.CAMPO_MONTO_VENTA));
                        item.setMoneda(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CuentaDebeDTS.CAMPO_MONEDA));
                        item.setFecha(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, CuentaDebeDTS.CAMPO_FECHA));
                        item.setTipoCliente(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CuentaDebeDTS.CAMPO_TIPO_CLIENTE));
                        item.setTipoRutaPanel(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CuentaDebeDTS.CAMPO_TIPO_ID));

                        list.add(item);
                    } while (rst.next());

                    output.setDatos(list);
                } else {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_DATOS_REPORTE_865, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());
                }
            }
            return output;

        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);

            output.setRespuesta(respuesta);
        }
    }

    public static OutputReporteCtaDTS doGetHaber(Connection conn, InputReporteCtaDTS input, BigDecimal ID_PAIS) throws SQLException, ParseException {
        String nombreMetodo = "doGetHaber";
        String nombreClase = new CurrentClassGetter().getClassName();
        OutputReporteCtaDTS output = new OutputReporteCtaDTS();
        List<InputReporteCtaDTS> list = new ArrayList<InputReporteCtaDTS>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        Respuesta respuesta = new Respuesta();
        String sql = "";

        List<Filtro> condiciones = new ArrayList<Filtro>();
        String idDTS = input.getIdDTS();
        String nombreDTS = UtileriasBD.getOneRecord(conn, Distribuidor.CAMPO_NOMBRES, Distribuidor.N_TABLA, condiciones);
        SimpleDateFormat FORMATO_FECHA_GT = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA_GT);
        SimpleDateFormat FORMATO_FECHA_CORTA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
        try {
            String[] campos = CtrlReporteCtaDTS.obtenerCamposGetPost();

            condiciones.clear();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CuentaHaberDTS.CAMPO_TCSCDTSID, idDTS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CuentaDebeDTS.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_TRUNC, CuentaHaberDTS.CAMPO_FECHA_DEPOSITO, input.getFecha(), null, Conf.TXT_FORMATO_FECHA_CORTA_GT));
            Date fecha = FORMATO_FECHA_GT.parse(input.getFecha());
            input.setFecha(FORMATO_FECHA_CORTA.format(fecha));
            sql = UtileriasBD.armarQuerySelect(CuentaHaberDTS.N_TABLA + " SUBPARTITION(D" + ControladorBase.getPais(input.getCodArea()) + input.getFecha() + ") C", campos, condiciones);

            log.trace("Qry cta dts haber: " + sql);

            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_GETDATOS12, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    do {
                        InputReporteCtaDTS item = new InputReporteCtaDTS();
                        item.setIdDTS(idDTS);
                        item.setNombreDTS(nombreDTS);
                        item.setCuenta(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, CuentaHaberDTS.CAMPO_CUENTA));
                        item.setNoDeposito(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CuentaHaberDTS.CAMPO_NO_DEPOSITO));
                        item.setMontoDeposito(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, CuentaHaberDTS.CAMPO_MONTO_DEPOSITO));
                        item.setFechaDeposito(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, CuentaHaberDTS.CAMPO_FECHA_DEPOSITO));
                        item.setMoneda(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CuentaHaberDTS.CAMPO_MONEDA));
                        item.setSoc(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, CuentaHaberDTS.CAMPO_SOC));
                        item.setAsignacion(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, CuentaHaberDTS.CAMPO_ASIGNACION));
                        item.setReferencia(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, CuentaHaberDTS.CAMPO_REFERENCIA));
                        item.setUsuarioHaber(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CuentaHaberDTS.CAMPO_USUARIO));
                        item.setCtaCP(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, CuentaHaberDTS.CAMPO_CTA_CP));
                        item.setFeContab(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, CuentaHaberDTS.CAMPO_FE_CONTAB));
                        item.setFechaDoc(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, CuentaHaberDTS.CAMPO_FECHA_DOC));
                        item.setCreadoEl(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, CuentaHaberDTS.CAMPO_CREADO_EL));
                        item.setCreadoPor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CuentaHaberDTS.CAMPO_CREADO_POR));

                        list.add(item);
                    } while (rst.next());

                    output.setDatos(list);
                } else {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_DATOS_REPORTE_865, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());
                }
            }
            return output;

        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);

            output.setRespuesta(respuesta);
        }
    }

    public static OutputReporteCtaDTS doGetResumen(Connection conn, InputReporteCtaDTS input, BigDecimal ID_PAIS) throws SQLException, ParseException {
        String nombreMetodo = "doGetResumen";
        String nombreClase = new CurrentClassGetter().getClassName();
        OutputReporteCtaDTS output = new OutputReporteCtaDTS();
        List<InputReporteCtaDTS> list = new ArrayList<InputReporteCtaDTS>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        Respuesta respuesta = new Respuesta();
        String sql = "";
        SimpleDateFormat FECHAHORA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<Filtro> condiciones = new ArrayList<Filtro>();
        String idDTS = input.getIdDTS();
        String nombreDTS = UtileriasBD.getOneRecord(conn, Distribuidor.CAMPO_NOMBRES, Distribuidor.N_TABLA, condiciones);
        SimpleDateFormat FORMATO_FECHA_GT = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA_GT);
        SimpleDateFormat FORMATO_FECHA_CORTA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
        try {
            String[] campos = {
                CuentaResumenDTS.CAMPO_DEBE,
                CuentaResumenDTS.CAMPO_HABER,
                CuentaResumenDTS.CAMPO_SALDO,
                CuentaResumenDTS.CAMPO_MONEDA,
                CuentaResumenDTS.CAMPO_CREADO_EL,
                CuentaResumenDTS.CAMPO_CREADO_POR
            };

            condiciones.clear();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CuentaResumenDTS.CAMPO_TCSCDTSID, idDTS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CuentaDebeDTS.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_TRUNC, CuentaResumenDTS.CAMPO_CREADO_EL, input.getFecha(), null, Conf.TXT_FORMATO_FECHA_CORTA_GT));
            Date fecha = FORMATO_FECHA_GT.parse(input.getFecha());
            input.setFecha(FORMATO_FECHA_CORTA.format(fecha));
            sql = UtileriasBD.armarQuerySelect(CuentaResumenDTS.N_TABLA + " SUBPARTITION(D" + ControladorBase.getPais(input.getCodArea()) + input.getFecha() + ") C", campos, condiciones);

            log.trace("Qry cta dts resumen: " + sql);
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_GETDATOS12, null, nombreClase,
                            nombreMetodo, null, input.getCodArea());
                    String fechaActual = FECHAHORA.format(new Date());

                    do {
                        InputReporteCtaDTS item = new InputReporteCtaDTS();
                        item.setIdDTS(idDTS);
                        item.setNombreDTS(nombreDTS);
                        item.setFechaGeneracion(fechaActual);
                        item.setFechaCreacion(input.getFecha());
                        item.setDebe(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, CuentaResumenDTS.CAMPO_DEBE));
                        item.setHaber(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, CuentaResumenDTS.CAMPO_HABER));
                        item.setSaldo(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, CuentaResumenDTS.CAMPO_SALDO));
                        item.setMoneda(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CuentaResumenDTS.CAMPO_MONEDA));
                        item.setCreadoEl(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, CuentaResumenDTS.CAMPO_CREADO_EL));
                        item.setCreadoPor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CuentaResumenDTS.CAMPO_CREADO_POR));

                        list.add(item);
                    } while (rst.next());

                    output.setDatos(list);
                } else {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_DATOS_REPORTE_865, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());
                }
            }
            return output;

        } finally {
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(rst);

            output.setRespuesta(respuesta);
        }
    }

    public static OutputReporteCtaDTS postHaber(Connection conn, InputReporteCtaDTS input, BigDecimal ID_PAIS) {
        String nombreMetodo = "postHaber";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = null;
        OutputReporteCtaDTS output = null;
        String sql = null;
        String campos[] = CtrlReporteCtaDTS.obtenerCamposGetPost();
        List<String> inserts = CtrlReporteCtaDTS.obtenerInsertsPost(input, CuentaHaberDTS.SEQUENCE, ID_PAIS);

        sql = UtileriasBD.armarQueryInsertAll(CuentaHaberDTS.N_TABLA, campos, inserts);
        try {
            QueryRunner Qr = new QueryRunner();
            int res = Qr.update(conn, sql);
            if (res > 0) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CARGA_HABER_SCL_75, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output = new OutputReporteCtaDTS();
                output.setRespuesta(respuesta);
            }
            conn.commit();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            DbUtils.closeQuietly(conn);
        }

        return output;
    }
}
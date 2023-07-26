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

import com.consystec.sc.ca.ws.input.deuda.InputTransDeuda;
import com.consystec.sc.ca.ws.input.remesa.InputRemesa;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCantInvJornada;
import com.consystec.sc.ca.ws.input.venta.DetallePago;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.deuda.OutputTransDeuda;
import com.consystec.sc.sv.ws.orm.Cuenta;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.PagoDet;
import com.consystec.sc.sv.ws.orm.Remesa;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;


public class OperacionTransaccionesDeuda {
	private OperacionTransaccionesDeuda(){}
    private static final Logger log = Logger.getLogger(OperacionTransaccionesDeuda.class);

    public static OutputTransDeuda doGet(Connection conn, InputTransDeuda input, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        OutputTransDeuda output = new OutputTransDeuda();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        PreparedStatement pstmt1 = null;
        ResultSet rst1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rst2 = null;
        PreparedStatement pstmt3 = null;
        ResultSet rst3 = null;
        Respuesta respuesta = new Respuesta();
        String sql = "";
        StringBuilder sqlB = new StringBuilder();

        try {
            String bancosEfectivo = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.BANCOS_EFECTIVO, input.getCodArea());
            String estadosAnulados = UtileriasJava.getConfig(conn, Conf.ESTADOS_ANULADOS, Conf.ESTADOS_ANULADOS, input.getCodArea());
            
            // se obtienen datos de jornadas
            List<InputReporteCantInvJornada> listJornadas = new ArrayList<InputReporteCantInvJornada>();

            sql = getTransaccionesq(input, ID_PAIS) ;

            log.debug("Qry datos jornada: " + sql);
          try{
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_REMESAS_714, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());
                } else {

                    do {
                        InputReporteCantInvJornada item = new InputReporteCantInvJornada();
                        item.setIdJornada(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "TCSCJORNADAVENID"));
                        item.setIdVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "VENDEDOR"));
                        item.setNombreVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE_VENDEDOR"));
                        item.setUsuarioVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "USUARIO_VENDEDOR"));
                        item.setIdRutaPanel(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "ID_PANELRUTA"));
                        item.setTipoRutaPanel(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "TIPO_PANELRUTA"));
                        item.setNombreRutaPanel(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE_PANELRUTA"));

                        listJornadas.add(item);
                    } while (rst.next());
                }
            }
          }finally{
        	  DbUtils.closeQuietly(pstmt);
        	  DbUtils.closeQuietly(rst);
          }

            // se obtienen datos de remesas
            List<InputRemesa> listRemesas = new ArrayList<InputRemesa>();
           
            sql = insertTran(input, bancosEfectivo, ID_PAIS);

            try{
	            pstmt1 = conn.prepareStatement(sql);
	            rst1 = pstmt1.executeQuery();
	
	            if (rst1 != null) {
	                while (rst1.next()) {
	                    InputRemesa item = new InputRemesa();
	                    item.setIdJornada(rst1.getString(Remesa.CAMPO_TCSCJORNADAVENID));
	                    int indexJornada = getIndexJornada(listJornadas, item.getIdJornada());
	                    item.setIdVendedor(listJornadas.get(indexJornada).getIdVendedor());
	                    item.setNombreVendedor(listJornadas.get(indexJornada).getNombreVendedor());
	                    item.setIdTipo(listJornadas.get(indexJornada).getIdRutaPanel());
	                    item.setTipo(listJornadas.get(indexJornada).getTipoRutaPanel());
	                    item.setNombreTipo(listJornadas.get(indexJornada).getNombreRutaPanel());
	
	                    item.setIdRemesa(rst1.getString(Remesa.CAMPO_TCSCREMESAID));
	                    item.setTasaCambio(rst1.getString(Remesa.CAMPO_TASA_CAMBIO));
	                    item.setMonto(UtileriasJava.convertirMoneda(rst1.getString(Remesa.CAMPO_MONTO), item.getTasaCambio()));
	                    item.setNoBoleta(rst1.getString(Remesa.CAMPO_NO_BOLETA));
	                    item.setIdCuenta(rst1.getString(Remesa.CAMPO_TCSCCTABANCARIAID));
	                    item.setBanco(rst1.getString(Cuenta.CAMPO_BANCO));
	                    item.setNoCuenta(rst1.getString(Cuenta.CAMPO_NO_CUENTA));
	                    item.setTipoCuenta(rst1.getString(Cuenta.CAMPO_TIPO_CUENTA));
	                    item.setNombreCuenta(rst1.getString(Cuenta.CAMPO_NOMBRE_CUENTA));
	                    item.setEstado(rst1.getString(Remesa.CAMPO_ESTADO));
	                    item.setOrigen(UtileriasJava.getRstValue(rst1, Conf.TIPO_TEXTO, Remesa.CAMPO_ORIGEN));
	                    item.setCreado_el(UtileriasJava.getRstValue(rst1, Conf.TIPO_FECHA, Remesa.CAMPO_CREADO_EL));
	                    item.setCreado_por(rst1.getString(Remesa.CAMPO_CREADO_POR));
	                    item.setModificado_el(UtileriasJava.getRstValue(rst1, Conf.TIPO_FECHA, Remesa.CAMPO_MODIFICADO_EL));
	                    item.setModificado_por(UtileriasJava.getRstValue(rst1, Conf.TIPO_TEXTO, Remesa.CAMPO_MODIFICADO_POR));
	
	                    listRemesas.add(item);
	                }
	            }
            }finally{
            	DbUtils.closeQuietly(pstmt1);
            	DbUtils.closeQuietly(rst1);
            }

            // se obtienen datos de transacciones de pago con tarjeta
            List<DetallePago> listTarjeta = new ArrayList<DetallePago>();
            sqlB.append("SELECT V.TCSCVENTAID, V.TCSCJORNADAVENID, (P.MONTO * V.TASA_CAMBIO) AS TOTAL, ");
            sqlB.append("P.NUM_AUTORIZACION, P.BANCO, P.MARCA_TARJETA, P.DIGITOS_TARJETA ");
            sqlB.append("FROM TC_SC_VENTA V, TC_SC_DET_PAGO P WHERE P.TCSCVENTAID = V.TCSCVENTAID");
            sqlB.append(" AND V.TCSCCATPAISID =?" );
            sqlB.append(" AND V.TCSCJORNADAVENID IN (?)");
            sqlB.append(" AND UPPER(P.FORMAPAGO) = ?");
            sqlB.append(" AND UPPER(V.ESTADO) NOT IN (" + estadosAnulados + ")");

            log.debug("Query para obtener detalle pago tarjeta: " + sqlB.toString());
            
            try{
	            pstmt2 = conn.prepareStatement(sqlB.toString());
	            pstmt2.setBigDecimal(1, ID_PAIS);
	            pstmt2.setBigDecimal(2, new BigDecimal(input.getIdJornada()));
	            pstmt2.setString(3, Conf.FORMA_PAGO_TARJETA.toUpperCase());
	            rst2 = pstmt2.executeQuery();
	
	            while (rst2.next()) {
	                DetallePago item = new DetallePago();
	                item.setMonto(UtileriasJava.getRstValue(rst2, Conf.TIPO_NUMERICO, "TOTAL"));
	                item.setNumAutorizacion(UtileriasJava.getRstValue(rst2, Conf.TIPO_NUMERICO, PagoDet.CAMPO_NUM_AUTORIZACION));
	                item.setBanco(UtileriasJava.getRstValue(rst2, Conf.TIPO_TEXTO, PagoDet.CAMPO_BANCO));
	                item.setMarcaTarjeta(UtileriasJava.getRstValue(rst2, Conf.TIPO_TEXTO, PagoDet.CAMPO_MARCA_TARJETA));
	                item.setDigitosTarjeta(UtileriasJava.getRstValue(rst2, Conf.TIPO_NUMERICO, PagoDet.CAMPO_DIGITOS_TARJETA));
	
	                item.setIdJornada(rst2.getString("TCSCJORNADAVENID"));
	                int indexJornada = getIndexJornada(listJornadas, item.getIdJornada());
	                item.setIdVendedor(listJornadas.get(indexJornada).getIdVendedor());
	                item.setNombreVendedor(listJornadas.get(indexJornada).getNombreVendedor());
	                item.setIdTipo(listJornadas.get(indexJornada).getIdRutaPanel());
	                item.setTipo(listJornadas.get(indexJornada).getTipoRutaPanel());
	                item.setNombreTipo(listJornadas.get(indexJornada).getNombreRutaPanel());
	
	                listTarjeta.add(item);
	            }
            }finally{
            	DbUtils.closeQuietly(pstmt2);
            	DbUtils.closeQuietly(rst2);
            }

            //solo en CR no aplica pago con cheque
            List<DetallePago> listCheque = new ArrayList<DetallePago>();
            if (!input.getCodArea().equals("506")) {
                // se obtienen datos de transacciones de pago con tarjeta
            	sqlB = new StringBuilder();
                sqlB.append("SELECT V.TCSCVENTAID, V.TCSCJORNADAVENID, (P.MONTO * V.TASA_CAMBIO) AS TOTAL, ");
                sqlB.append("P.BANCO, P.NUM_CHEQUE, P.FECHA_EMISION, P.NUM_RESERVA, P.NO_CUENTA ");
                sqlB.append("FROM TC_SC_VENTA V, TC_SC_DET_PAGO P WHERE P.TCSCVENTAID = V.TCSCVENTAID");
                sqlB.append(" AND V.TCSCCATPAISID = ?");
                sqlB.append(" AND V.TCSCJORNADAVENID IN (?)");
                sqlB.append(" AND UPPER(P.FORMAPAGO) = ?");
                sqlB.append(" AND UPPER(V.ESTADO) NOT IN (" + estadosAnulados + ")");

                log.debug("Query para obtener detalle pago cheque: " + sqlB.toString());
               try{ 
                pstmt3 = conn.prepareStatement(sqlB.toString());
                pstmt3.setBigDecimal(1, ID_PAIS);
                pstmt3.setBigDecimal(2, new BigDecimal(input.getIdJornada()));
                pstmt3.setString(3, Conf.FORMA_PAGO_CHEQUE.toUpperCase());
                
                rst3 = pstmt3.executeQuery();
 
                while (rst3.next()) {
                    DetallePago item = new DetallePago();
                    item.setMonto(UtileriasJava.getRstValue(rst3, Conf.TIPO_NUMERICO, "TOTAL"));
                    item.setBanco(UtileriasJava.getRstValue(rst3, Conf.TIPO_TEXTO, PagoDet.CAMPO_BANCO));
                    item.setNumeroCheque(UtileriasJava.getRstValue(rst3, Conf.TIPO_TEXTO, PagoDet.CAMPO_NUM_CHEQUE));
                    item.setFechaEmision(UtileriasJava.getRstValue(rst3, Conf.TIPO_FECHA, PagoDet.CAMPO_FECHA_EMISION));
                    item.setNumeroReserva(UtileriasJava.getRstValue(rst3, Conf.TIPO_NUMERICO, PagoDet.CAMPO_NUM_RESERVA));
                    item.setCuentaCliente(UtileriasJava.getRstValue(rst3, Conf.TIPO_TEXTO, PagoDet.CAMPO_NO_CUENTA));

                    item.setIdJornada(rst3.getString("TCSCJORNADAVENID"));
                    int indexJornada = getIndexJornada(listJornadas, item.getIdJornada());
                    item.setIdVendedor(listJornadas.get(indexJornada).getIdVendedor());
                    item.setNombreVendedor(listJornadas.get(indexJornada).getNombreVendedor());
                    item.setIdTipo(listJornadas.get(indexJornada).getIdRutaPanel());
                    item.setTipo(listJornadas.get(indexJornada).getTipoRutaPanel());
                    item.setNombreTipo(listJornadas.get(indexJornada).getNombreRutaPanel());

                    listCheque.add(item);
                }
              
            }finally{
            	DbUtils.closeQuietly(pstmt3);
            	DbUtils.closeQuietly(rst3);
            }
            }
            if (listRemesas.isEmpty() && listTarjeta.isEmpty() && listCheque.isEmpty()) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, nombreClase, nombreMetodo, null, input.getCodArea());
            } else {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_GETDATOS12, null, nombreClase, nombreMetodo, null, input.getCodArea());
                output.setRemesas(listRemesas);
                output.setTransaccionesTarjeta(listTarjeta);
                output.setTransaccionesCheque(listCheque);
            }

        } finally {
            output.setRespuesta(respuesta);
        }

        return output;
    }
    
    public static String getTransaccionesq( InputTransDeuda input, BigDecimal ID_PAIS) 
    {
    	String   sql = "SELECT J.TCSCJORNADAVENID, J.IDTIPO AS ID_PANELRUTA, J.DESCRIPCION_TIPO AS TIPO_PANELRUTA, "
                   + "CASE J.DESCRIPCION_TIPO "
                       + "WHEN 'RUTA' THEN "
                       + "(SELECT NOMBRE FROM TC_SC_RUTA WHERE TCSCCATPAISID = J.TCSCCATPAISID AND TCSCRUTAID = J.IDTIPO) "
                       + "WHEN 'PANEL' THEN "
                       + "(SELECT NOMBRE FROM TC_SC_PANEL WHERE TCSCCATPAISID = J.TCSCCATPAISID AND TCSCPANELID = J.IDTIPO) "
                   + "END AS NOMBRE_PANELRUTA, "
                   + "V.VENDEDOR, V.USUARIO AS USUARIO_VENDEDOR, V.NOMBRE || ' ' || V.APELLIDO AS NOMBRE_VENDEDOR "
                   + "FROM TC_SC_VEND_DTS V, " + ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()) + " J "
                   + "WHERE J.VENDEDOR = V.VENDEDOR AND J.TCSCCATPAISID = V.TCSCCATPAISID "
                   + "AND J.TCSCJORNADAVENID IN (" + input.getIdJornada() + ")"
                   + " AND J.TCSCCATPAISID = " + ID_PAIS;
    	   return sql;
    }

    public static String insertTran(InputTransDeuda input, String bancosEfectivo, BigDecimal ID_PAIS) {
    	List<Filtro> condiciones = new ArrayList<Filtro>();
    	String sql="";
    	   String tablas[] = {
                   ControladorBase.getParticion(Remesa.N_TABLA, Conf.PARTITION, "", input.getCodArea()),
                   Cuenta.N_TABLA
           };

           String campos[][] = {
               { Remesa.N_TABLA, Remesa.CAMPO_TCSCREMESAID },
               { Remesa.N_TABLA, Remesa.CAMPO_TCSCJORNADAVENID },
               { Remesa.N_TABLA, Remesa.CAMPO_MONTO },
               { Remesa.N_TABLA, Remesa.CAMPO_TASA_CAMBIO },
               { Remesa.N_TABLA, Remesa.CAMPO_NO_BOLETA },
               { Remesa.N_TABLA, Remesa.CAMPO_TCSCCTABANCARIAID },
               { Cuenta.N_TABLA, Cuenta.CAMPO_BANCO },
               { Cuenta.N_TABLA, Cuenta.CAMPO_NO_CUENTA },
               { Cuenta.N_TABLA, Cuenta.CAMPO_TIPO_CUENTA },
               { Cuenta.N_TABLA, Cuenta.CAMPO_NOMBRE_CUENTA },
               { Remesa.N_TABLA, Remesa.CAMPO_ESTADO },
               { Remesa.N_TABLA, Remesa.CAMPO_ORIGEN },
               { Remesa.N_TABLA, Remesa.CAMPO_CREADO_EL },
               { Remesa.N_TABLA, Remesa.CAMPO_CREADO_POR },
               { Remesa.N_TABLA, Remesa.CAMPO_MODIFICADO_EL },
               { Remesa.N_TABLA, Remesa.CAMPO_MODIFICADO_POR }
           };

           condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Remesa.N_TABLA, Remesa.CAMPO_TCSCCATPAISID, ID_PAIS + ""));
           condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, "TC_SC_REMESA." + Remesa.CAMPO_TCSCJORNADAVENID, input.getIdJornada()));
           condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_NOT_IN_AND, "TC_SC_REMESA." + Remesa.CAMPO_BANCO, bancosEfectivo));
           condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Remesa.N_TABLA, Remesa.CAMPO_TCSCCTABANCARIAID, Cuenta.N_TABLA, Cuenta.CAMPO_TCSCCTABANCARIAID));
           condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Remesa.N_TABLA, Remesa.CAMPO_TCSCCATPAISID, Cuenta.N_TABLA, Cuenta.CAMPO_TCSCCATPAISID));

           sql = UtileriasBD.armarQueryGetMultiple(campos, tablas, condiciones, null);
           
           return sql;
    }
    private static int getIndexJornada(List<InputReporteCantInvJornada> listJornadas, String idJornada) {
        for (int i = 0; i < listJornadas.size(); i++) {
            if (listJornadas.get(i).getIdJornada().equals(idJornada)) {
                return i;
            }
        }
        return -1;
    }
}
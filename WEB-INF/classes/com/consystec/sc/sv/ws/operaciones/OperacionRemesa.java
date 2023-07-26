package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.remesa.InputRemesa;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.remesa.OutputRemesa;
import com.consystec.sc.sv.ws.metodos.CtrlRemesa;
import com.consystec.sc.sv.ws.orm.Adjunto;
import com.consystec.sc.sv.ws.orm.Cuenta;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.Remesa;
import com.consystec.sc.sv.ws.orm.Solicitud;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.Venta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class OperacionRemesa extends ControladorBase {
    private static final Logger log = Logger.getLogger(OperacionRemesa.class);

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @param estadoAnulado 
     * @param estadoBaja 
     * @param estadoAlta 
     * @return OutputRemesa
     * @throws SQLException
     */
    @SuppressWarnings("unlikely-arg-type")
	public static OutputRemesa doGet(Connection conn, InputRemesa input, String tipoTarjeta,
            String estadoAnulado, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<InputRemesa> list = new ArrayList<InputRemesa>();
        Respuesta respuesta = new Respuesta();
        OutputRemesa output = new OutputRemesa();

        PreparedStatement pstmt = null;
        ResultSet rst = null;

        List<Filtro> condiciones = new ArrayList<Filtro>();
        List<String> listJornadas = new ArrayList<String>();

        try {
	        String tablas[] = {
                ControladorBase.getParticion(Remesa.N_TABLA, Conf.PARTITION, "", input.getCodArea()),
	            Cuenta.N_TABLA,
	            ControladorBase.getParticion(Jornada.N_TABLA, Conf.PARTITION, "", input.getCodArea()),
	            Distribuidor.N_TABLA,
	            ControladorBase.getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, "", input.getCodArea())
	        };

            String[][] campos = CtrlRemesa.obtenerCamposGet(conn, input.getCodArea(), ID_PAIS);
            condiciones = CtrlRemesa.obtenerCondiciones(input, ID_PAIS);

            
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Remesa.N_TABLA,
                    Remesa.CAMPO_TCSCCTABANCARIAID, Cuenta.N_TABLA, Cuenta.CAMPO_TCSCCTABANCARIAID));

            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Remesa.N_TABLA,
                    Remesa.CAMPO_TCSCCATPAISID, Cuenta.N_TABLA, Cuenta.CAMPO_TCSCCATPAISID));

            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Remesa.N_TABLA,
                    Remesa.CAMPO_TCSCJORNADAVENID, Jornada.N_TABLA, Jornada.CAMPO_TCSCJORNADAVENID));

            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Jornada.N_TABLA,
                    Jornada.CAMPO_VENDEDOR, VendedorDTS.N_TABLA, VendedorDTS.CAMPO_VENDEDOR));

            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Jornada.N_TABLA,
                    Jornada.CAMPO_TCSCDTSID, Distribuidor.N_TABLA, Distribuidor.CAMPO_TC_SC_DTS_ID));

            String sql = UtileriasBD.armarQueryGetMultiple(campos, tablas, condiciones, null);

            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    if (input.getToken().equals("WEB")) {
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_REMESAS_714, null,
                                nombreClase, nombreMetodo, null, input.getCodArea());
                    } else {
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CARGA_REMESA_78, null,
                                nombreClase, nombreMetodo, null, input.getCodArea());

                        List<InputRemesa> jornadas = new ArrayList<InputRemesa>();                        
                        InputRemesa jornada = new InputRemesa();
                        // se obtiene el listado de jornadas
                        listJornadas.add(input.getIdJornada());
                        ArrayList<InputRemesa> listVentas = getVentas(conn, tipoTarjeta, listJornadas, estadoAnulado, input.getCodArea(), ID_PAIS);
                   
                        for (int i = 0; i < listVentas.size(); i++) {
                             jornada = new InputRemesa();
                            jornada.setIdJornada(listVentas.get(i).getIdJornada());
                            jornada.setCantVentas(listVentas.get(i).getCantVentas());
                            jornada.setTotalVentas(listVentas.get(i).getTotalVentas());
                            jornada.setTotaltarjetaCredito(listVentas.get(i).getTotaltarjetaCredito());
                            jornada.setTotalCredito(listVentas.get(i).getTotalCredito());
                            jornada.setTotalMpos(listVentas.get(i).getTotalMpos());
                            jornada.setRemesas(new ArrayList<InputRemesa>());
                            jornadas.add(jornada);
                            jornada.setRemesas(list);
                        }                        
                        


                        output.setDatos(jornadas);
                        
                    }

                } else {
                    do {
                        InputRemesa item = new InputRemesa();
                        String idJornada = rst.getString(Remesa.CAMPO_TCSCJORNADAVENID);
                        item.setIdJornada(idJornada);

                        if (!listJornadas.contains(idJornada)) {
                            listJornadas.add(idJornada);
                        }

                        item.setFechaInicioJornada(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Jornada.CAMPO_FECHA));
                        item.setFechaFinJornada(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Jornada.CAMPO_FECHA_FINALIZACION));
                        item.setFechaLiqJornada(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Jornada.CAMPO_FECHA_LIQUIDACION));
                        item.setEstadoJornada(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "ESTADO_JORNADA"));
                        item.setEstadoLiqJornada(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_ESTADO_LIQUIDACION));
                        item.setIdRemesa(rst.getString(Remesa.CAMPO_TCSCREMESAID));
                        item.setIdDistribuidor(rst.getString(Jornada.CAMPO_TCSCDTSID));
                        item.setNombreDistribuidor(rst.getString(Distribuidor.CAMPO_NOMBRES));
                        item.setIdVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_VENDEDOR));
                        item.setNombreVendedor(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, VendedorDTS.CAMPO_USUARIO));
                        item.setIdTipo(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Jornada.CAMPO_IDTIPO));
                        item.setTipo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Jornada.CAMPO_DESCRIPCION_TIPO));
                        item.setNombreTipo(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NOMBRE_TIPO"));
                        item.setTasaCambio(rst.getString(Remesa.CAMPO_TASA_CAMBIO));
                        item.setMonto(UtileriasJava.convertirMoneda(rst.getString(Remesa.CAMPO_MONTO), item.getTasaCambio()));
                        item.setNoBoleta(rst.getString(Remesa.CAMPO_NO_BOLETA));
                        item.setIdCuenta(rst.getString(Remesa.CAMPO_TCSCCTABANCARIAID));
                        item.setBanco(rst.getString(Cuenta.CAMPO_BANCO));
                        item.setNoCuenta(rst.getString(Cuenta.CAMPO_NO_CUENTA));
                        item.setTipoCuenta(rst.getString(Cuenta.CAMPO_TIPO_CUENTA));
                        item.setNombreCuenta(rst.getString(Cuenta.CAMPO_NOMBRE_CUENTA));
                        item.setEstado(rst.getString(Remesa.CAMPO_ESTADO));
                        item.setIdApp(rst.getString(Remesa.CAMPO_ID_REMESA_MOVIL) == null ? "" : rst.getString(Remesa.CAMPO_ID_REMESA_MOVIL));
                        item.setOrigen(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Remesa.CAMPO_ORIGEN));
                        item.setCreado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Remesa.CAMPO_CREADO_EL));
                        item.setCreado_por(rst.getString(Remesa.CAMPO_CREADO_POR));
                        item.setModificado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, Remesa.CAMPO_MODIFICADO_EL));
                        item.setModificado_por(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, Remesa.CAMPO_MODIFICADO_POR));

                        list.add(item);
                    } while (rst.next());


                    // se obtiene el listado de jornadas
                    ArrayList<InputRemesa> listVentas = getVentas(conn, tipoTarjeta, listJornadas, estadoAnulado, input.getCodArea(), ID_PAIS);

                    List<InputRemesa> jornadas = new ArrayList<InputRemesa>();
                    for (int i = 0; i < listVentas.size(); i++) {
                        InputRemesa jornada = new InputRemesa();
                        jornada.setIdJornada(listVentas.get(i).getIdJornada());
                        jornada.setCantVentas(listVentas.get(i).getCantVentas());
                        jornada.setTotalVentas(listVentas.get(i).getTotalVentas());
                        jornada.setTotaltarjetaCredito(listVentas.get(i).getTotaltarjetaCredito());
                        jornada.setTotalCredito(listVentas.get(i).getTotalCredito());
                        jornada.setTotalMpos(listVentas.get(i).getTotalMpos());
                        jornada.setRemesas(new ArrayList<InputRemesa>());
                        jornadas.add(jornada);
                    }

                    List<Integer> indexRevisados = new ArrayList<Integer>();
                    for (int i = 0; i < jornadas.size(); i++) {
                        for (int j = 0; j < list.size(); j++) {

                            if (jornadas.get(i).getIdJornada().equals(list.get(j).getIdJornada())) {
                                jornadas.get(i).setEstadoJornada(list.get(j).getEstadoJornada());
                                list.get(j).setEstadoJornada(null);
                                jornadas.get(i).setEstadoLiqJornada(list.get(j).getEstadoLiqJornada());
                                list.get(j).setEstadoLiqJornada(null);
                                jornadas.get(i).setFechaInicioJornada(list.get(j).getFechaInicioJornada());
                                list.get(j).setFechaInicioJornada(null);
                                jornadas.get(i).setFechaFinJornada(list.get(j).getFechaFinJornada());
                                list.get(j).setFechaFinJornada(null);
                                jornadas.get(i).setFechaLiqJornada(list.get(j).getFechaLiqJornada());
                                list.get(j).setFechaLiqJornada(null);
                                list.get(j).setIdJornada(null);

                                jornadas.get(i).getRemesas().add(list.get(j));

                                indexRevisados.add(j);
                            }
                        }

                        list.remove(indexRevisados);

                        if (jornadas.get(i).getRemesas().isEmpty()) {
                            jornadas.set(i, null);
                        }
                    }

                    if (jornadas.isEmpty()) {
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_REMESAS_714, null,
                                nombreClase, nombreMetodo, null, input.getCodArea());
                    } else {
                        respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CARGA_REMESA_78, null,
                                nombreClase, nombreMetodo, null, input.getCodArea());
                    }

                    output.setDatos(jornadas);
                }
            }
        } finally {
            output.setRespuesta(respuesta);
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return output;
    }

    private static ArrayList<InputRemesa> getVentas(Connection conn, String tipoTarjeta, List<String> listJornadas, String estadoAnulado, String codArea, BigDecimal ID_PAIS)
            throws SQLException {
        ArrayList<InputRemesa> list = new ArrayList<InputRemesa>();

        PreparedStatement pstmt = null;
        ResultSet rst = null;
      
            String jornadas = UtileriasJava.listToString(Conf.TIPO_NUMERICO, listJornadas, ",");

            String sql =queryVentas( tipoTarjeta, jornadas,  estadoAnulado, codArea, ID_PAIS);

        try {  	

            log.trace("Query ventas: " + sql);
            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst.next()) {
                do {
                    InputRemesa item = new InputRemesa();
	                item.setIdJornada(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, Venta.CAMPO_TCSCJORNADAVENID));
	                item.setCantVentas(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "VENTAS"));
                    item.setTotalVentas(UtileriasJava.redondear(rst.getBigDecimal("TOTAL"), 2).toString());
                    if (rst.getBigDecimal("TARJETACREDITO") == null) {
                        item.setTotaltarjetaCredito(UtileriasJava.redondear(new BigDecimal(0), 2).toString());
                    } else {
                        item.setTotaltarjetaCredito(UtileriasJava.redondear(rst.getBigDecimal("TARJETACREDITO"), 2).toString());
                    }
                    if (rst.getBigDecimal("CREDITO") == null) {
                        item.setTotalCredito(UtileriasJava.redondear(new BigDecimal(0), 2).toString());
                    } else {
                        item.setTotalCredito(UtileriasJava.redondear(rst.getBigDecimal("CREDITO"), 2).toString());
                    }
                    if (rst.getBigDecimal("MPOS") == null) {
                        item.setTotalMpos(UtileriasJava.redondear(new BigDecimal(0), 2).toString());
                    } else {
                        item.setTotalMpos(UtileriasJava.redondear(rst.getBigDecimal("MPOS"), 2).toString());
                    }
                    list.add(item);
                } while (rst.next());
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return list;
    }

    public static String queryVentas(String tipoTarjeta,String jornadas, String estadoAnulado, String codArea, BigDecimal ID_PAIS) {
    	  String sql ="  SELECT a.tcscjornadavenid, " +
          		"         COUNT (a.monto_pagado) VENTAS, " +
          		"         NVL(SUM (ROUND(a.monto_pagado * a.tasa_cambio,2)),0) TOTAL, " +
          		"         NVL ( " +
          		"            (SELECT SUM (p.monto * V.tasa_Cambio) TARJETACREDITO " +
          		"               FROM tc_Sc_det_pago p, "+ ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) +  " V "+
          		"              WHERE     UPPER (p.formapago) = '"+tipoTarjeta.toUpperCase()+"' " +
          		"                    AND UPPER (V.estado) NOT IN ("+estadoAnulado.toUpperCase()+") " +
          		"                    AND P.TCSCVENTAID = V.TCSCVENTAID " +
          		"                    AND V.TCSCCATPAISID =  " + ID_PAIS +
          		"                    AND V.TCSCJORNADAVENID IN (a.TCSCJORNADAVENID)), " +
          		"            0) " +
          		"            TARJETACREDITO, " +
          		"         NVL ( " +
          		"            (SELECT SUM (p.monto * V.tasa_Cambio) CREDITO " +
          		"               FROM tc_Sc_det_pago p, "+ ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) +  " V "+
          		"              WHERE     UPPER (p.formapago) = '"+"CREDITO".toUpperCase()+"' " +
          		"                    AND UPPER (V.estado) NOT IN ("+estadoAnulado.toUpperCase()+") " +
          		"                    AND P.TCSCVENTAID = V.TCSCVENTAID " +
          		"                    AND V.TCSCCATPAISID =  " + ID_PAIS +
          		"                    AND V.TCSCJORNADAVENID IN (a.TCSCJORNADAVENID)), " +
          		"            0) " +
          		"            CREDITO, " +
          		"         NVL ( " +
          		"            (SELECT SUM (p.monto * V.tasa_Cambio) MPOS " +
          		"               FROM tc_Sc_det_pago p, "+ ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) +  " V "+
          		"              WHERE     UPPER (p.formapago) = '"+"MPOS".toUpperCase()+"' " +
          		"                    AND UPPER (V.estado) NOT IN ("+estadoAnulado.toUpperCase()+") " +
          		"                    AND P.TCSCVENTAID = V.TCSCVENTAID " +
          		"                    AND V.TCSCCATPAISID =  " + ID_PAIS +
          		"                    AND V.TCSCJORNADAVENID IN (a.TCSCJORNADAVENID)), " +
          		"            0) " +
          		"            MPOS " +
          		"    FROM "+ ControladorBase.getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea) +  " a " +
          		"   WHERE     a.TCSCCATPAISID = "+ ID_PAIS +
          		"         AND UPPER (a.estado) NOT IN ("+estadoAnulado.toUpperCase()+") " +
          		"         AND a.TCSCJORNADAVENID IN ("+jornadas+") " +
          		"GROUP BY a.TCSCJORNADAVENID ";
    	  return sql;
    }
    /**
     * Funci\u00F3n que arma el query a utilizar al trabajar en m\u00E9todo POST.
     * 
     * @param conn
     * @param input
     * @param estadoBaja 
     * @param estadoAlta 
     * @param origenPC 
     * @return OutputRemesa
     * @throws SQLException
     */
	public static OutputRemesa doPost(Connection conn, InputRemesa input, String estadoAlta, String origenPC, BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "doPost";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta respuesta = null;
		OutputRemesa output = new OutputRemesa();

		String campos[] = CtrlRemesa.obtenerCamposPost();
		List<String> inserts = new ArrayList<String>();
		List<Filtro> condiciones = new ArrayList<Filtro>();
		InputRemesa remesa = new InputRemesa();
		String valores = "";
		String insertDeuda = "";
		String insertJornada = "";
		String insertTasaCambio = "";
		String insertMonto = "";
		String insertCuenta = "";
		String AltaEstado = "";
		String sqlnuevos = "";

		List<String> respuesta2 = new ArrayList<String>();

		List<InputRemesa> remesas = new ArrayList<InputRemesa>();

		String tasaCambio = "";
		BigDecimal monto = BigDecimal.ZERO;
		MathContext mc = new MathContext(200, RoundingMode.HALF_UP);
		int cantDecimalesBD = new Integer(UtileriasJava.getConfig(conn, Conf.GRUPO_PARAM_VENTA, Conf.CANT_DECIMALES_BD, input.getCodArea()));
		List<Order> orden = new ArrayList<Order>();
		orden.add(new Order(Remesa.CAMPO_TCSCREMESAID, Order.ASC));

		if (input.getOrigen().equalsIgnoreCase(origenPC)) {
			tasaCambio = OperacionVendxDTS.getTasaCambio(input.getCodArea());
			insertTasaCambio = UtileriasJava.setInsert(Conf.INSERT_NUMERO, tasaCambio, Conf.INSERT_SEPARADOR_SI);
		}
		try {
			AltaEstado = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.CAMPO_ESTADO, AltaEstado));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));

			conn.setAutoCommit(false);
			for (int i = 0; i < input.getRemesas().size(); i++) {
				PreparedStatement pstmt = null;
				PreparedStatement pstmt1 = null;
				PreparedStatement pstmt2 = null;
				PreparedStatement pstmt3 = null;
				PreparedStatement pstmt4 = null;
				PreparedStatement pstmt5 = null;
				respuesta2.clear();
				String query = "";
				String sql = "";
				ResultSet rst = null;
				ResultSet rst1 = null;
				ResultSet rst2 = null;
				valores = "";
				inserts.clear();
				String idRemesa = "";
				// validamos que no se tenga ingresado la remesa
				query = queryValidacionRemesa1(input.getRemesas().get(i), input.getCodDispositivo(), input.getOrigen(), input.getIdJornada(), AltaEstado, origenPC, ID_PAIS);
				log.debug("queryValidacionRemesa1: " + query);
				try {
					pstmt = conn.prepareStatement(query);
					rst = pstmt.executeQuery();

					if (rst.next()) {
						respuesta2.add(rst.getString(1));
						respuesta2.add(rst.getString(2));
						respuesta2.add(rst.getString(3));
						respuesta2.add(rst.getString(4));
						respuesta2.add(rst.getString(5));
						respuesta2.add(rst.getString(6));
						remesa.setIdApp(rst.getString(6));
					}
				} finally {
					DbUtils.closeQuietly(pstmt);
					DbUtils.closeQuietly(rst);
				}
				
				if (respuesta2.size() > 0) {
					respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_REMESA_52, null, nombreClase, nombreMetodo, null, input.getCodArea());
					// el registro ya esta ingresado se retorna el id
					remesa.setGuardadoExitosamente("1");
					remesa.setIdRemesa(respuesta2.get(0));
					remesas.add(remesa);
				} else {
					// --validamos si codigo banco y el no. boleta ya existen y se actualizara el
					// registro
					query = queryValidacionRemesa2(input.getRemesas().get(i), input.getCodDispositivo(), input.getOrigen(), input.getIdJornada(), AltaEstado, origenPC, ID_PAIS);
					log.debug("queryValidacionRemesa2: " + query);
					
					try {
						pstmt2 = conn.prepareStatement(query);
						rst2 = pstmt2.executeQuery();

						if (rst2.next()) {
							respuesta2.add(rst2.getString(1));
							respuesta2.add(rst2.getString(2));
							respuesta2.add(rst2.getString(3));
							respuesta2.add(rst2.getString(4));
							respuesta2.add(rst2.getString(5));
							respuesta2.add(rst2.getString(6));
							condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
									Remesa.CAMPO_TCSCREMESAID, rst2.getString(1)));
						}
					} finally {
						DbUtils.closeQuietly(pstmt2);
						DbUtils.closeQuietly(rst2);
					}

					if (respuesta2.size() > 0) {
						// el registro solo se actualizara y se retorna el id
						String camposUpdate[][] = {
								{ Remesa.CAMPO_TCSCJORNADAVENID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getIdJornada()) },
								{ Remesa.CAMPO_MONTO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getRemesas().get(i).getMonto()) },
								{ Remesa.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
								{ Remesa.CAMPO_TASA_CAMBIO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getRemesas().get(i).getTasaCambio()) },
								{ Remesa.CAMPO_ID_REMESA_MOVIL, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getRemesas().get(i).getIdRemesa()) },
								{ Remesa.CAMPO_CODIGO_DISPOSITIVO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getCodDispositivo()) } };

						sql = UtileriasBD.armarQueryUpdate(Remesa.N_TABLA, camposUpdate, condiciones);
						try {
							pstmt3 = conn.prepareStatement(sql);
							pstmt3.executeUpdate();

							respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_REMESA_52, null, nombreClase, nombreMetodo, null, input.getCodArea());
							remesa = new InputRemesa();
							remesa.setIdApp(input.getRemesas().get(i).getIdRemesa());
							remesa.setGuardadoExitosamente("1");
							remesa.setIdRemesa(respuesta2.get(0).toString());
							remesas.add(remesa);
							conn.commit();

						} catch (Exception e) {
							conn.rollback();
							log.error(e.getMessage(), e);
						} finally {
							DbUtils.closeQuietly(pstmt3);
						}
					} else {
						// se valida el codigo Banco, no. boleta y monto y si es distinto el
						// dispositivo, solo se actualiza y se retorna el id

						String query2 = "";
						query2 = queryValidacionRemesa3(input.getRemesas().get(i), input.getCodDispositivo(), input.getOrigen(), input.getIdJornada(), AltaEstado, origenPC, ID_PAIS);
						log.debug("queryValidacionRemesa3: " + query2);
						
						try {
							pstmt1 = conn.prepareStatement(query2);
							rst1 = pstmt1.executeQuery();

							if (rst1.next()) {
								respuesta2.add(rst1.getString(1));
								respuesta2.add(rst1.getString(2));
								respuesta2.add(rst1.getString(3));
								respuesta2.add(rst1.getString(4));
								respuesta2.add(rst1.getString(5));
								respuesta2.add(rst1.getString(6));
								condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Remesa.CAMPO_TCSCREMESAID, rst1.getString(1)));
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						} finally {
							DbUtils.closeQuietly(pstmt1);
							DbUtils.closeQuietly(rst1);
						}
						if (respuesta2.size() > 0) {
							// se actualiza el registro y se retorna id

							String camposUpdate[][] = {
									{ Remesa.CAMPO_TCSCJORNADAVENID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getIdJornada()) },
									{ Remesa.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
									{ Remesa.CAMPO_TASA_CAMBIO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getRemesas().get(i).getTasaCambio()) },
									{ Remesa.CAMPO_ID_REMESA_MOVIL, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getRemesas().get(i).getIdRemesa()) },
									{ Remesa.CAMPO_CODIGO_DISPOSITIVO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getCodDispositivo()) } };

							sql = UtileriasBD.armarQueryUpdate(Remesa.N_TABLA, camposUpdate, condiciones);
							log.debug("queryUpdate: " + sql);
							try {
								pstmt4 = conn.prepareStatement(sql);
								pstmt4.executeUpdate();
								respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_REMESA_52, null,
										nombreClase, nombreMetodo, null, input.getCodArea());
								remesa = new InputRemesa();
								remesa.setIdApp(input.getRemesas().get(i).getIdRemesa());
								remesa.setGuardadoExitosamente("1");
								remesa.setIdRemesa(respuesta2.get(0));
								remesas.add(remesa);

								conn.commit();
								DbUtils.close(pstmt4);
							} catch (Exception e) {
								conn.rollback();
								log.error(e.getMessage(), e);
							} finally {
								DbUtils.closeQuietly(pstmt4);
							}

						} else {
							// el registro no esta ingresado
							idRemesa = UtileriasBD.getOneRecord(conn, Remesa.SEQUENCE, "DUAL", null);

							if (input.getIdDeuda() != null && !input.getIdDeuda().trim().equals("")) {
								insertDeuda = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdDeuda(), Conf.INSERT_SEPARADOR_SI);
							} else {
								insertDeuda = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
							}

							if (input.getOrigen().equalsIgnoreCase(origenPC)) {
								insertCuenta = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
							} else {
								insertCuenta = UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getRemesas().get(i).getIdCuenta(), Conf.INSERT_SEPARADOR_SI);
							}

							if (input.getIdJornada() != null && !input.getIdJornada().trim().equals("")) {
								insertJornada = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdJornada(), Conf.INSERT_SEPARADOR_SI);
							} else {
								insertJornada = UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI);
							}

							if (input.getOrigen().equalsIgnoreCase(origenPC)) {
								monto = new BigDecimal(input.getRemesas().get(i).getMonto()) .divide(new BigDecimal(tasaCambio), mc);
								insertMonto = UtileriasJava.setInsert(Conf.INSERT_NUMERO, UtileriasJava.redondearBD(monto, cantDecimalesBD) + "", Conf.INSERT_SEPARADOR_SI);
							} else {
								insertTasaCambio = UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getRemesas().get(i).getTasaCambio(), Conf.INSERT_SEPARADOR_SI);
								insertMonto = UtileriasJava.setInsert(Conf.INSERT_NUMERO, UtileriasJava.redondearBD(new BigDecimal(input.getRemesas().get(i).getMonto()), cantDecimalesBD) + "", Conf.INSERT_SEPARADOR_SI);
							}

							valores = ""
									+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idRemesa, Conf.INSERT_SEPARADOR_SI)
									+ insertJornada + insertDeuda
									+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, ID_PAIS.toString(), Conf.INSERT_SEPARADOR_SI)
									+ insertMonto + insertTasaCambio
									+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getRemesas().get(i).getNoBoleta(), Conf.INSERT_SEPARADOR_SI)
									+ insertCuenta
									+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getRemesas().get(i).getBanco(), Conf.INSERT_SEPARADOR_SI)
									+ UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
									+ UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getOrigen(), Conf.INSERT_SEPARADOR_SI)
									+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
									+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_SI)
									+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getRemesas().get(i).getIdRemesa(), Conf.INSERT_SEPARADOR_SI)
									+ UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getCodDispositivo(), Conf.INSERT_SEPARADOR_NO);

							inserts.add(valores);

							sqlnuevos = UtileriasBD.armarQueryInsertAll(Remesa.N_TABLA, campos, inserts);
							log.debug("queryInsert: " + sqlnuevos);
							try {
								pstmt5 = conn.prepareStatement(sqlnuevos);
								pstmt5.executeQuery();
								remesa = new InputRemesa();
								respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_REMESA_52, null,
										nombreClase, nombreMetodo, null, input.getCodArea());
								remesa.setIdApp(input.getRemesas().get(i).getIdRemesa());
								remesa.setGuardadoExitosamente("1");
								remesa.setIdRemesa(idRemesa);
								remesas.add(remesa);
								conn.commit();
							} catch (Exception e) {
								log.error(e.getMessage(), e);
								respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase, nombreMetodo, null, input.getCodArea());
								conn.rollback();
							} finally {
								DbUtils.closeQuietly(pstmt5);
							}

						}

					}
				}

			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, ex.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
		} finally {
			output.setDatos(remesas);
			conn.setAutoCommit(true);
			output.setRespuesta(respuesta);
		}
		return output;
	}

    public static String queryValidacionRemesa1(InputRemesa input, String codDispositivo, String origen, String idJornada, String altaEstado,String origenPC, BigDecimal ID_PAIS) {
    	 String query = "SELECT tcscremesaid, tcscctabancariaid, no_boleta, monto, codigo_dispositivo, id_remesa_movil "
    		        + " FROM TC_SC_REMESA  WHERE  UPPER(estado)  = '"+altaEstado+"' AND tcsccatpaisid= " + ID_PAIS 
    		        + " AND monto ="+ input.getMonto() 
    		        + " AND no_boleta ='"+ input.getNoBoleta()+"'" 
    		        + " AND tcscctabancariaid ="+input.getIdCuenta()
  		        + " AND id_remesa_movil="+ input.getIdRemesa()
  		        + " AND codigo_dispositivo='" + codDispositivo +"'";
  		  if (!origen.equalsIgnoreCase(origenPC)) {
		        	 query=query+ " AND TCSCJORNADAVENID="+ idJornada;
		         }
  		  return query;
    }
    
    public static String queryValidacionRemesa2(InputRemesa input, String codDispositivo, String origen, String idJornada, String AltaEstado,String origenPC, BigDecimal ID_PAIS) {
    	String query="";
    	query = "SELECT tcscremesaid, tcscctabancariaid, no_boleta, monto, codigo_dispositivo, id_remesa_movil "
		        + " FROM TC_SC_REMESA  WHERE  UPPER(estado)  = '"+AltaEstado+"'"
		        + " AND tcsccatpaisid= " + ID_PAIS             		      
		        + " AND no_boleta ='"+ input.getNoBoleta()+"'" 
		        + " AND tcscctabancariaid ="+input.getIdCuenta();
	   if (!origen.equalsIgnoreCase(origenPC)) {
		   query=query+ " AND TCSCJORNADAVENID="+ idJornada
  			 + " AND id_remesa_movil="+ input.getIdRemesa()
  			 +" AND codigo_dispositivo='"+ codDispositivo +"'";
	   }
	   return query;
    }
    
    public static String queryValidacionRemesa3( InputRemesa input, String codDispositivo, String origen, String idJornada, String AltaEstado,String origenPC, BigDecimal ID_PAIS) { 
 	String query2="";
	  	query2 = "SELECT tcscremesaid, tcscctabancariaid, no_boleta, monto, codigo_dispositivo, id_remesa_movil "
		        + " FROM TC_SC_REMESA  WHERE  UPPER(estado)  = '"+AltaEstado+"'"
		        + " AND tcsccatpaisid= " + ID_PAIS             		                        		       
		        + " AND tcscctabancariaid ="+input.getIdCuenta()
		        + " AND no_boleta ='"+ input.getNoBoleta()+"'" 
	  			+ " AND monto ="+ input.getMonto()                   	  			                                    	  		
	  			+ " AND codigo_dispositivo='" + codDispositivo +"'";
	  	
	 if (!origen.equalsIgnoreCase(origenPC)) {
    	 query2=query2+ " AND TCSCJORNADAVENID=" + idJornada;
    	
	   }
	 return query2;
    }
    /**
     * Funci\u00F3n que arma el query a utilizar al modificar datos de remesa.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @param estadoAlta
     * @param estadoPendiente
     * @param tipoDeuda 
     * @param estadoRechazadaTelca 
     * @return
     * @throws SQLException
     */
    public static OutputRemesa doPut(Connection conn, InputRemesa input,  
            String estadoPendiente, String tipoDeuda, String estadoRechazadaTelca, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "doPut";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputRemesa output = new OutputRemesa();
        int existencia = 0;
        List<Filtro> condicionesRemesa = new ArrayList<Filtro>();
        List<Filtro> condicionesAdjunto = new ArrayList<Filtro>();

        String sql = "";
        Statement stmtRemesas = null;
        Statement stmtAdjuntos = null;
        int[] deleteCounts;

        try {
            conn.setAutoCommit(false);
            stmtRemesas = conn.createStatement();
            stmtAdjuntos = conn.createStatement();

            if (input.getIdRemesa() != null && !input.getIdRemesa().equals("")) {
                condicionesRemesa.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Remesa.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condicionesRemesa.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Remesa.CAMPO_TCSCREMESAID, input.getIdRemesa()));

                // se verifica que existe la remesa
                existencia = UtileriasBD.selectCount(conn, Remesa.N_TABLA, condicionesRemesa);

                condicionesAdjunto.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Adjunto.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condicionesAdjunto.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Adjunto.CAMPO_TCSCGESTIONID, input.getIdRemesa()));
                condicionesAdjunto.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Adjunto.CAMPO_GESTION, "REMESA"));

            } else if (input.getIdDeuda() != null && !input.getIdDeuda().equals("")) {
                // condiciones de deuda
                condicionesRemesa.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condicionesRemesa.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCSOLICITUDID, input.getIdDeuda()));
                condicionesRemesa.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Solicitud.CAMPO_ESTADO, estadoPendiente));
                condicionesRemesa.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Solicitud.CAMPO_ESTADO, estadoRechazadaTelca));
                condicionesRemesa.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Solicitud.CAMPO_TIPO_SOLICITUD, tipoDeuda));

                // se verifica que existe la solicitud en estado pendiente
                existencia = UtileriasBD.selectCount(conn, Solicitud.N_TABLA, condicionesRemesa);

                if (existencia <= 0) {
                    log.error("No existe la solicitud.");
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_IDDEUDA_894, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    return output;
                }

                // condiciones de remesas
                condicionesRemesa.clear();
                condicionesRemesa.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Remesa.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condicionesRemesa.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Remesa.CAMPO_TCSCSOLICITUDID, input.getIdDeuda()));
                String idsRemesas = UtileriasBD.getOneRecord(conn, UtileriasJava.setSelect(Conf.SELECT_COUNT, Remesa.CAMPO_TCSCREMESAID), Remesa.N_TABLA, condicionesRemesa);
                existencia = new Integer(idsRemesas);
                idsRemesas = UtileriasBD.armarQuerySelectField(Remesa.N_TABLA, Remesa.CAMPO_TCSCREMESAID, condicionesRemesa, null);

                // condiciones de adjuntos
                condicionesAdjunto.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Adjunto.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condicionesAdjunto.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Adjunto.CAMPO_TCSCGESTIONID, idsRemesas));
                condicionesAdjunto.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Adjunto.CAMPO_GESTION, "REMESA"));
            }

            if (existencia <= 0) {
                log.error("No existe el recurso.");
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_REMESAS_714, null, nombreClase, nombreMetodo, null, input.getCodArea());
                return output;
            }

            try{
            	sql = UtileriasBD.armarQueryDelete(Remesa.N_TABLA, condicionesRemesa);
	            stmtRemesas.addBatch(sql);
	            sql = UtileriasBD.armarQueryDelete(Adjunto.N_TABLA, condicionesAdjunto);
	            stmtAdjuntos.addBatch(sql);
	
	            deleteCounts = stmtAdjuntos.executeBatch();
	            deleteCounts = stmtRemesas.executeBatch();
            }finally{
            	DbUtils.closeQuietly(stmtAdjuntos);
            	DbUtils.closeQuietly(stmtRemesas);
            }
            if (UtileriasJava.validarBatch(existencia, deleteCounts)) {
                conn.commit();
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_REMESA_53, null, nombreClase, nombreMetodo, null, input.getCodArea());

            } else {
                conn.rollback();
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null, nombreClase, nombreMetodo, null, input.getCodArea());
                log.error("Inconveniente al dar eliminar un registro.");
            }

        } finally {
            conn.setAutoCommit(true);
            output.setRespuesta(respuesta);
          
        }

        return output;
    }
}
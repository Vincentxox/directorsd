package com.ericsson.sc.ca.ws.controller;

import java.sql.Connection;
import java.util.List;

import com.consystec.ms.seguridad.util.JavaUtils;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.ericsson.sc.ca.ws.input.ventacredito.AbonoVentaCredito;

public class ControllerAbonoVentaCredito extends ControladorBase {

	private static final String INSERT_TC_SC_VENTA_ABONO = "INSERT INTO  TC_SC_VENTA_ABONO (TCSCVENTAABONOID,TCSCVENTAID,FECHA_PAGO,MONTO_PAGADO) "
			+ "VALUES (TC_SC_VENTA_ABONO_SQ.NEXTVAL,?,?,?)";

	private static final String SELECT_TCSC_ABONOS_VENTA_CREDITO = "Select TCSCVENTAID tcscventaid, FECHA_PAGO fechapago, MONTO_PAGADO montopagados FROM TC_SC_VENTA_ABONO WHERE TCSCVENTAABONOID= ?";

	public List<AbonoVentaCredito> getListAbonosCreditos(int idventa) {
		List<AbonoVentaCredito> listatempbonos = null;
		try (Connection conn = getConnLocal()) {

			listatempbonos = JavaUtils.executeSelect(conn, AbonoVentaCredito.class, SELECT_TCSC_ABONOS_VENTA_CREDITO,
					new Object[] { idventa });

			return listatempbonos;

		} catch (Exception e) {

		}
		return listatempbonos;

	}

	public int insertAbonocredito(AbonoVentaCredito input) {

		int i = 0;

		try (Connection conn = getConnLocal()) {

			i = JavaUtils.executeUpdate(conn, INSERT_TC_SC_VENTA_ABONO,
					new Object[] { input.getTcscventaid(), input.getFechapago(), input.getMontopagado() });

			return i;
		} catch (Exception e) {

		}
		return i;

	}

}

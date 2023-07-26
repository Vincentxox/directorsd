package com.ericsson.sc.ca.ws.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.ericsson.sc.ca.ws.input.ventacredito.AbonoVentaCredito;
import com.ericsson.sc.ca.ws.output.ventacredito.OutputVentaCredito;
import com.ericsson.sc.ca.ws.output.ventacredito.VentaCredito;

public class ControllerVentaCredito extends ControladorBase {

 
	public static final String SELECT_VENTA_CREDITO2 =
			"select * from ( " + 
			"SELECT  VE.TCSCVENTAID IDVENTA, VE.IDTIPO IDPDV, VE.TCSCCATPAISID IDPAIS, VE.VENDEDOR IDUSUARIO,   VE.SERIE SERIE,  VE.NUMERO NUMEROFACTURA, TO_CHAR(VE.FECHA_EMISION,'YYYYMMDD') FECHAVENTA,DET.MONTO MONTO, DET.MONTO-B.MONTOPAGADO SALDO  " + 
			"FROM tc_sc_det_pago DET  JOIN tc_sc_venta VE  ON  DET.TCSCVENTAID=VE.TCSCVENTAID  JOIN  " + 
			"(SELECT DETT.TCSCVENTAID VENTAID, NVL(SUM(MONTO_PAGADO),0) montopagado from TC_SC_VENTA_ABONO VA  RIGHT JOIN tc_sc_det_pago DETT on VA.TCSCVENTAID=DETT.TCSCVENTAID " + 
			"WHERE DETT.formapago='Credito'  " + 
			"    GROUP BY DETT.TCSCVENTAID  " + 
			") B ON VE.TCSCVENTAID= B.VENTAID " + 
			"WHERE DET.formapago='Credito' AND  VE.TCSCVENTAID =DET.TCSCVENTAID  AND VE.COD_DISPOSITIVO=?) where SALDO>0";
	
	private static final String SELECT_TCSC_ABONOS_VENTA_CREDITO = "SELECT TCSCVENTAID, FECHA_PAGO FECHAPAGO, MONTO_PAGADO MONTOPAGADO "
			+ "FROM  TC_SC_VENTA_ABONO " + "WHERE TCSCVENTAID = ? ";

	public OutputVentaCredito getVentasCredito(VentaCredito input) throws SQLException {
		OutputVentaCredito response = new OutputVentaCredito();
		List<VentaCredito> listaventascredito = null;
		try (Connection conn = getConnLocal()) {

			listaventascredito = JavaUtils.executeSelect(conn, VentaCredito.class, SELECT_VENTA_CREDITO2,
					new Object[] { input.getIddispositivo() });

			if (listaventascredito != null) {
				for (VentaCredito vc : listaventascredito) {
					vc.setListabonos(JavaUtils.executeSelect(conn, AbonoVentaCredito.class,
							SELECT_TCSC_ABONOS_VENTA_CREDITO, new Object[] { vc.getIdventa() }));

				}

			}

			response.setListaventascredito(listaventascredito);

			return response;

		} catch (Exception e) {
			System.out.println(e.toString());
			
		}

		return response;
	}
}

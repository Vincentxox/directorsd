package com.consystec.sc.sv.ws.metodos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.sv.ws.operaciones.OperacionOfertaFS;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.sv.ws.orm.ofertafs.input.InputOfertaFS;
import com.consystec.sc.sv.ws.orm.ofertafs.output.Ofertas;
import com.consystec.sc.sv.ws.orm.ofertafs.output.OutputOfertaFS;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlOfertasFS extends ControladorBase{
	private static final Logger log = Logger.getLogger(CtrlRemesa.class);
	private static String servicioGet = Conf.LOG_GET_OFERTA_FS;

	public Respuesta validarInput(InputOfertaFS input){
		String nombreMetodo = "validarInput";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta respuesta = null;

		log.debug("Validando datos...");

		if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) 
			respuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, input.getCodArea());
		

		return respuesta;
	}
	
	public OutputOfertaFS getOfertaFs(InputOfertaFS objDatos) {
		List<LogSidra> listaLog = new ArrayList<LogSidra>();
    	OutputOfertaFS respuesta = new OutputOfertaFS();
        Respuesta objRespuesta = null;
        String metodo = "getReporteRecarga";
        Connection conn = null;
        List<Ofertas> lstOfertas = new ArrayList<Ofertas>();

        // validando filtros
        try {
            objRespuesta = validarInput(objDatos);
            if (objRespuesta == null) {
                conn = getConnRegional();
                getIdPais(conn, objDatos.getCodArea());

                lstOfertas = OperacionOfertaFS.doGet(conn, objDatos);

                if (lstOfertas.isEmpty()) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo, null, objDatos.getCodArea());
                } else {
                    respuesta.setOferta(lstOfertas);
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
                }

            } else {
                listaLog = new ArrayList<LogSidra>();
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema al consultar reporte recargas.",
                        objRespuesta.getDescripcion()));

            }
        } catch (SQLException e) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar reporte recargas.", e.getMessage()));
        } catch (Exception e) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar reporte recargas.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            respuesta.setRespuesta(objRespuesta);
        }
        return respuesta;
    }
}

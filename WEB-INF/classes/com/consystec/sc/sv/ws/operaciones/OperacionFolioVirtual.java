package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Holder;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.folio.InputFolioVirtual;
import com.consystec.sc.ca.ws.input.general.InputFolioRutaPanel;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.folio.OutputConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.metodos.CtrlFolioVirtual;
import com.consystec.sc.sv.ws.orm.ConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.FSUtil;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.telefonica.globalintegration.header.HeaderInType;
import com.telefonica.globalintegration.header.HeaderOutType;
import com.telefonica.globalintegration.services.cancelcustomerbillnumberreservation.v1.CancelCustomerBillNumberReservationRequestType;
import com.telefonica.globalintegration.services.cancelcustomerbillnumberreservation.v1.CancelCustomerBillNumberReservationResponseType;
import com.telefonica.globalintegration.services.soap.cancelcustomerbillnumberreservation.v1.CancelCustomerBillNumberReservationBindingQSService;
import com.telefonica.globalintegration.services.soap.cancelcustomerbillnumberreservation.v1.CancelCustomerBillNumberReservationV1;
import com.telefonica.globalintegration.services.soap.cancelcustomerbillnumberreservation.v1.MessageFault;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class OperacionFolioVirtual {
	private OperacionFolioVirtual(){}
    private static final Logger log = Logger.getLogger(OperacionFolioVirtual.class);

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return respuestaConfiguracionFolio
     * @throws SQLException
     */
    public static OutputConfiguracionFolioVirtual doGet(Connection conn, InputFolioVirtual input, int metodo, BigDecimal idPais)
            throws SQLException {
    	 String nombreMetodo = "doGet";
         String nombreClase = new CurrentClassGetter().getClassName();

         List<InputFolioVirtual> list = new ArrayList<InputFolioVirtual>();
         InputFolioVirtual item = new InputFolioVirtual();

         Respuesta respuesta = null;
         OutputConfiguracionFolioVirtual output = null;
         PreparedStatement pstmt = null;
         ResultSet rst = null;

         String sql = null;
         String campos[] = { UtileriasJava.setSelect(Conf.SELECT_DISTINCT, ConfiguracionFolioVirtual.CAMPO_ID_TIPO),
                 ConfiguracionFolioVirtual.CAMPO_TIPO };

         List<Filtro> condiciones = CtrlFolioVirtual.obtenerCondiciones(input, metodo, idPais);

         List<Order> orden = new ArrayList<Order>();
         orden.add(new Order(ConfiguracionFolioVirtual.CAMPO_ID_TIPO, Order.ASC));

         try{
 	        sql = UtileriasBD.armarQuerySelect(ConfiguracionFolioVirtual.N_TABLA, campos, condiciones, orden);
 	        pstmt = conn.prepareStatement(sql);
 	        rst = pstmt.executeQuery();
 	
 	        if (rst != null) {
 	            if (!rst.next()) {
 	                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_FOLIOS_394, null, nombreClase,
 	                        nombreMetodo, null, input.getCodArea());
 	
 	                output = new OutputConfiguracionFolioVirtual();
 	                output.setRespuesta(respuesta);
                 } else {
                	 
                     respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, input.getCodArea());

 	                List<InputFolioRutaPanel> listFolios = getFolios(conn, input, metodo, idPais);
 	
 	                do {
 	                    item = new InputFolioVirtual();
 	                    item.setUsuario(input.getUsuario());
 	                    String idTipo = rst.getString(ConfiguracionFolioVirtual.CAMPO_ID_TIPO);
 	                    item.setIdTipo(idTipo);
 	                    item.setTipo(rst.getString(ConfiguracionFolioVirtual.CAMPO_TIPO));
 	
 	                    List<InputFolioRutaPanel> folios = new ArrayList<InputFolioRutaPanel>();
 	                    for (int i = 0; i < listFolios.size(); i++) {
 	                        if (idTipo.equals(listFolios.get(i).getIdTipo())) {
 	                            listFolios.get(i).setIdTipo(null);
 	                            listFolios.get(i).setTipo(null);
 	                            folios.add(listFolios.get(i));
 	                        }
 	                    }
 	                    if (folios.size() > 0) {
 	                        item.setFolios(folios);
 	                        list.add(item);
 	                    }
 	                } while (rst.next());
 	
                     output = new OutputConfiguracionFolioVirtual();
                     output.setRespuesta(respuesta);
                     output.setConfiguracionFolio(list);
                 }
             }
         } finally {
             DbUtils.closeQuietly(rst);
             DbUtils.closeQuietly(pstmt);
         }
         return output;
    }

    /**
     * Funci\u00F3n que obtiene los datos relacionados de la tabla hija.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return
     * @throws SQLException
     */
    private static List<InputFolioRutaPanel> getFolios(Connection conn, InputFolioVirtual input, int metodo, BigDecimal idPais)
            throws SQLException {
    	String nombreMetodo = "getFolios";
        String nombreClase = new CurrentClassGetter().getClassName();
        String query="";
        PreparedStatement pstm = null;
        ResultSet rst = null;
        List<InputFolioRutaPanel> list = new ArrayList<InputFolioRutaPanel>();

        PreparedStatement pstmtIn = null;
        ResultSet rstIn = null;

        try {
            String camposInterno[] = CtrlFolioVirtual.obtenerCamposGetPost(metodo);
            List<Filtro> condicionesInterno = CtrlFolioVirtual.obtenerCondiciones(input, metodo, idPais);

	        List<Order> orden = new ArrayList<Order>();
	        orden.add(new Order("DECODE(ESTADO, 'ALTA', 1, 'EN_USO', 2, 'FINALIZADO', 3, 'BAJA', 4, 5)", ""));

            String sql = UtileriasBD.armarQuerySelect(ConfiguracionFolioVirtual.N_TABLA, camposInterno,
                    condicionesInterno, orden);
            pstmtIn = conn.prepareStatement(sql);
            rstIn = pstmtIn.executeQuery();

            if (rstIn != null) {
                if (!rstIn.next()) {
                    log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
                    InputFolioRutaPanel item = new InputFolioRutaPanel();

                    Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_FOLIOS_394,
                            null, nombreClase, nombreMetodo, null, input.getCodArea());

                    item.setEstado(respuesta.getDescripcion());

                    list.add(item);
                } else {
                	SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
                    do {
                    	query="";
                        InputFolioRutaPanel item = new InputFolioRutaPanel();
                        item.setIdFolio(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_TC_SC_FOLIO_ID));
                        item.setIdTipo(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_ID_TIPO));
                        item.setTipo(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_TIPO));
                        item.setTipoDocumento(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_TIPODOCUMENTO));
                        item.setSerie(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_SERIE));
                        item.setNoInicialFolio(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_NO_INICIAL_FOLIO));
                        item.setNoFinalFolio(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_NO_FINAL_FOLIO));
                        item.setCant_utilizados(UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO, ConfiguracionFolioVirtual.CAMPO_CANT_UTILIZADOS));
                        item.setUltimo_utilizado(UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO, ConfiguracionFolioVirtual.CAMPO_ULTIMO_UTILIZADO));
                        item.setFolio_siguiente(UtileriasJava.getRstValue(rstIn, Conf.TIPO_NUMERICO, ConfiguracionFolioVirtual.CAMPO_FOLIO_SIGUIENTE));
                        item.setEstado(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_ESTADO));
                        item.setCreado_el(UtileriasJava.formatStringDate(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_CREADO_EL)));
                        item.setCreado_por(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_CREADO_POR));
                        item.setModificado_el(UtileriasJava.formatStringDate(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_MODIFICADO_EL)));
                        item.setModificado_por(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_MODIFICADO_POR));
                        item.setIdReserva(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_ID_RESERVA));
                        item.setResolucion(rstIn.getString(ConfiguracionFolioVirtual.CAMPO_RESOLUCION));
                        Timestamp fecha_resolucion = rstIn.getTimestamp(ConfiguracionFolioVirtual.CAMPO_FECHA_RESOLUCION);
                        item.setFecha_resolucion(fecha_resolucion != null?sdf.format(fecha_resolucion):"");
                        //obtenemos los campos de caja numero y zona del dispositivo
                        query="SELECT ZONA, CAJA_NUMERO FROM TC_sC_DISPOSITIVO WHERE CODIGO_DISPOSITIVO=? AND TCSCCATPAISID=?" ;
                        log.trace("query obtener caja numero y zona: " + query);
                        try {
                        	pstm = conn.prepareStatement(query);
                        	pstm.setString(1, rstIn.getString(ConfiguracionFolioVirtual.CAMPO_ID_TIPO));
                        	pstm.setBigDecimal(2, idPais);
                        	rst = pstm.executeQuery();
                        	if (rst.next()) {
                        		item.setZona(rst.getString("ZONA"));
                        		item.setCaja_numero(rst.getString("CAJA_NUMERO"));
                        	}
                    	} finally {
                        	DbUtils.closeQuietly(rst);
                        	DbUtils.closeQuietly(pstm);
                        }

                        list.add(item);
                    } while (rstIn.next());
                }
            }
        } finally {
            DbUtils.closeQuietly(rstIn);
            DbUtils.closeQuietly(pstmtIn);
        }
        return list;
    }

    /**
     * Funci\u00F3n que arma el query a utilizar al trabajar en m\u00E9todo POST.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @param estadoAlta
     * @return respuestaConfiguracionFolio
     * @throws SQLException
     */
    public static OutputConfiguracionFolioVirtual doPost(Connection conn, InputFolioVirtual input, int metodo,
            String estadoAlta, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doPost";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputConfiguracionFolioVirtual output = null;

        if (input.getCodArea().equals("503")) {
            List<Filtro> condicionesExistencia = CtrlFolioVirtual.obtenerCondicionesExistencia(conn, input, metodo,
                    estadoAlta, idPais);
            String existencia = UtileriasBD.verificarExistencia(conn, ConfiguracionFolioVirtual.N_TABLA, condicionesExistencia);
            if (new Integer(existencia) > 0) {
                log.error("Ya existe el recurso.");
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_FOLIO_EXISTENTE, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output = new OutputConfiguracionFolioVirtual();
                output.setRespuesta(respuesta);

                return output;
            }
        }

        String sql = null;
        String campos[] = CtrlFolioVirtual.obtenerCamposGetPost(Conf.METODO_POST);
        List<String> inserts = CtrlFolioVirtual.obtenerInsertsPost(input, ConfiguracionFolioVirtual.SEQUENCE,
                estadoAlta, idPais);

        sql = UtileriasBD.armarQueryInsertAll(ConfiguracionFolioVirtual.N_TABLA, campos, inserts);

        try {
        	int i=0;
            conn.setAutoCommit(false);
            QueryRunner Qr = new QueryRunner();
            i=Qr.update(conn, sql);

            if (i>0) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_FOLIO_43, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output = new OutputConfiguracionFolioVirtual();
                output.setRespuesta(respuesta);
            }else{
            	respuesta = new Respuesta();
            	respuesta.setCodResultado("0");
            	 output = new OutputConfiguracionFolioVirtual();
                 output.setRespuesta(respuesta);
            }
            conn.commit();
        } finally {
            conn.setAutoCommit(true);
            
        }

        return output;
    }

    /**
     * Funci\u00F3n que arma el query a utilizar al modificar datos en m\u00E9todos PUT y
     * DELETE.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @param estadoAlta
     * @return respuestaConfiguracionFolio
     * @throws SQLException
     * @throws MalformedURLException 
     */
	public static OutputConfiguracionFolioVirtual doPutDel(Connection conn, InputFolioVirtual input, int metodo,
            String estadoAlta, BigDecimal idPais) throws SQLException, MalformedURLException {
        String nombreMetodo = "doPutDel";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta respuesta = null;
        OutputConfiguracionFolioVirtual output = null;

        List<Filtro> condicionesExistencia = CtrlFolioVirtual.obtenerCondicionesExistencia(conn, input, metodo, estadoAlta, idPais);
        String existencia = UtileriasBD.verificarExistencia(conn, ConfiguracionFolioVirtual.N_TABLA, condicionesExistencia);
        if (new Integer(existencia) < 1) {
            log.error("No existe el recurso.");
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());

            output = new OutputConfiguracionFolioVirtual();
            output.setRespuesta(respuesta);

            return output;
        } else {
            if (verificarJornadaIniciada(conn, input.getIdTipo(), idPais) > 0) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_JORNADA_DISPOSITIVO_870, null,
                        nombreClase, nombreMetodo, null, input.getCodArea());
                output = new OutputConfiguracionFolioVirtual();
                output.setRespuesta(respuesta);

                return output;
            }

            String estadoActual = UtileriasBD.getOneRecord(conn, ConfiguracionFolioVirtual.CAMPO_ESTADO,
                    ConfiguracionFolioVirtual.N_TABLA, condicionesExistencia);
            if (!estadoActual.equalsIgnoreCase(estadoAlta)) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_FOLIO_BAJA_381, null, nombreClase,
                        nombreMetodo, estadoActual, input.getCodArea());

                output = new OutputConfiguracionFolioVirtual();
                output.setRespuesta(respuesta);

                return output;
            }
        }
        
			OutputConfiguracionFolioVirtual cancelaFolioFS = cancelarFoliosFS(conn, input);
			
			if(new BigDecimal(cancelaFolioFS.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_FOLIO_44){
				String sql = null;

		        String campos[][] = CtrlFolioVirtual.obtenerCamposPutDel(input);
		        sql = UtileriasBD.armarQueryUpdate(ConfiguracionFolioVirtual.N_TABLA, campos, condicionesExistencia);

		        try {
		        	int i=0;
		            conn.setAutoCommit(false);
		            QueryRunner Qr = new QueryRunner();
		            i=Qr.update(conn, sql);

		            if (i>0) {
		                output = new OutputConfiguracionFolioVirtual();
		                output.setRespuesta(cancelaFolioFS.getRespuesta());
		            }else{
		            	respuesta = new Respuesta();
		            	respuesta.setCodResultado("0");
		            	 output = new OutputConfiguracionFolioVirtual();
		                 output.setRespuesta(respuesta);
		            }
		            conn.commit();
		        } finally {
		            conn.setAutoCommit(true);
		            
		        }
			}else{
                output = new OutputConfiguracionFolioVirtual();
                output.setRespuesta(cancelaFolioFS.getRespuesta());
			}
        return output;
    }
    
    public static OutputConfiguracionFolioVirtual cancelarFoliosFS(Connection conn, InputFolioVirtual input) throws MalformedURLException, SQLException{
    	String nombreMetodo = "cancelarFoliosFS";
    	String nombreClase = new CurrentClassGetter().getClassName();
    	OutputConfiguracionFolioVirtual output = new OutputConfiguracionFolioVirtual();
    	Respuesta respuesta = new Respuesta();
    	URL endpoint = new URL(UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_CANCELAR_FOLIOS, input.getCodArea()));
    	CancelCustomerBillNumberReservationBindingQSService ws = new CancelCustomerBillNumberReservationBindingQSService(endpoint);
    	CancelCustomerBillNumberReservationV1 w = ws.getCancelCustomerBillNumberReservationBindingQSPort();
    	CancelCustomerBillNumberReservationRequestType request = new CancelCustomerBillNumberReservationRequestType();
    	CancelCustomerBillNumberReservationResponseType response = new CancelCustomerBillNumberReservationResponseType();

    	try {
    		request.setIDBusinessInteraction(input.getFolios().get(0).getIdReserva());

    		Holder<HeaderOutType> hres = new Holder<HeaderOutType>();
    		HeaderInType h = FSUtil.instanciaHeader(input.getUsuario(), input.getCodArea());
    		response = w.cancelCustomerBillNumberReservation(request, h, hres);
    	} catch (MessageFault e) {
    		if(e.getFaultInfo().getAppDetail().getExceptionAppCode() != null && "500".equals(e.getFaultInfo().getAppDetail().getExceptionAppCode())){
    			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_RANGO_EN_USO_685, null, nombreClase, nombreMetodo, null, input.getCodArea());
    			log.error("Excepcion: " + e.getFaultInfo().getAppDetail().getExceptionAppCause(), e);
    		}else if(e.getFaultInfo().getAppDetail().getExceptionAppCode() == null){
    			respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NO_BAJA_686, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
                log.error("Excepcion: " + e.getMessage(), e);
    		}
    	}

    	if(response.getCodeStatusAbstract() != null && !"".equals(response.getCodeStatusAbstract())){
    		 respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_MOD_FOLIO_44, null, nombreClase, nombreMetodo, null, input.getCodArea());
    		 respuesta.setCodResultado(""+Conf_Mensajes.OK_MOD_FOLIO_44);
    		 log.trace("OK folios cancelados desde OSB");
    	}
    	
    	output.setRespuesta(respuesta);
    	return output;
    }

    public static int verificarJornadaIniciada(Connection conn, String codDispositivo, BigDecimal idPais) throws SQLException {
        int existencia = 0;
        PreparedStatement pstm = null;
        ResultSet rst = null;
     
	        String sql = "SELECT COUNT(1) FROM TC_SC_JORNADA_VEND"
	                + " WHERE TCSCCATPAISID = ?" 
	                + " AND IDTIPO = (SELECT RESPONSABLE FROM TC_SC_DISPOSITIVO WHERE "
	                    + "CODIGO_DISPOSITIVO = ? AND TCSCCATPAISID = ?  and estado = 'ALTA' )"
	                + " AND DESCRIPCION_TIPO = (SELECT TIPO_RESPONSABLE FROM TC_SC_DISPOSITIVO WHERE "
	                    + "CODIGO_DISPOSITIVO = ? AND TCSCCATPAISID = ?   and estado = 'ALTA' )"
	                + " AND (UPPER(ESTADO) = (SELECT VALOR FROM TC_SC_CONFIGURACION "
	                        + "WHERE TCSCCATPAISID =? " 
	                        + " AND UPPER(GRUPO) = ?"
	                        + " AND UPPER(NOMBRE) = ?))";

            log.debug("Qry existencia jornada: " + sql);
       try {
            pstm = conn.prepareStatement(sql);
            pstm.setBigDecimal(1, idPais);
            pstm.setString(2, codDispositivo);
            pstm.setBigDecimal(3, idPais);
            pstm.setString(4, codDispositivo);
            pstm.setBigDecimal(5, idPais);
            pstm.setBigDecimal(6, idPais);
            pstm.setString(7, Conf.GRUPO_JORNADA_ESTADOS.toUpperCase());
            pstm.setString(8, Conf.JORNADA_ESTADO_INICIADA.toUpperCase());
            rst = pstm.executeQuery();
            if (rst.next())
                existencia = rst.getInt(1);
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstm);
        }
        return existencia;
    }
}

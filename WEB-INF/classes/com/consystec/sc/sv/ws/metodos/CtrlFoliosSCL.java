package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.folio.InputFolioVirtual;
import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.input.general.InputFolioRutaPanel;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.folio.OutputConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.operaciones.OperacionFoliosSCL;
import com.consystec.sc.sv.ws.orm.ConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.google.gson.GsonBuilder;

/**
 * @author Victor Cifuentes @ Consystec - 2017
 *
 */
public class CtrlFoliosSCL extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlFoliosSCL.class);
    private static String servicioGet = Conf.LOG_GET_FOLIOS_SCL;

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al
     * servicio.
     * 
     * @param conn
     * @param input
     *            Objeto con los datos enviados mediante POST.
     * @param metodo
     *            Valor que indica tipo de operaci\u00F3n que se desea realizar en el
     *            servicio.
     * @return Respuesta
     */
    public Respuesta validarInput(Connection conn, InputFolioVirtual input) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        log.debug("Validando datos...");

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (input.getCodOficina() == null || "".equals(input.getCodOficina())) {
            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CODOFICINA_856, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if (input.getIdDTS() == null || "".equals(input.getIdDTS().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_161, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        } else if (!isNumeric(input.getIdDTS())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if (input.getCodDispositivo() == null || "".equals(input.getCodDispositivo())) {
            return getMensaje(Conf_Mensajes.MSJ_CODDISPOSITIVO_NULO_67, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }
        
        if (input.getIdDispositivo() == null || "".equals(input.getIdDispositivo())) {
            return getMensaje(Conf_Mensajes.MSJ_IDDISPOSITIVO_NULO_66, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }else if (!isNumeric(input.getIdDispositivo())) {
        	r = getMensaje(Conf_Mensajes.MSJ_ID_DISPOSITIVO_NO_NUMERICO_635, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if (input.getCantFolios() == null || "".equals(input.getCantFolios())) {
            return getMensaje(Conf_Mensajes.MSJ_CANTIDAD_FOLIO_VACIO_637, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }else if (!isNumeric(input.getCantFolios())) {
        	r = getMensaje(Conf_Mensajes.MSJ_CANTIDAD_FOLIO_NO_NUMERICO_638, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if (input.getSerie() == null || "".equals(input.getSerie())) {
            return getMensaje(Conf_Mensajes.MSJ_PARM_SERIE_VACIO_639, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }
        
        if (input.getTipoDocumento() == null || "".equals(input.getTipoDocumento())) {
            return getMensaje(Conf_Mensajes.MSJ_TIPODOCUMENTO_NULO_428, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if (flag) {
            r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos, input.getCodArea());
        } else {
            r.setDescripcion("OK");
            r.setCodResultado("1");
            r.setMostrar("0");
        }
        return r;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST.
     * 
     * @param input
     *            Objeto con los datos enviados mediante POST.
     * @param metodo
     * @return output Respuesta y listado con los Descuentos encontrados.
     */

    
	public OutputConfiguracionFolioVirtual getDatos(InputFolioVirtual input) {
	    List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta respuesta = new Respuesta();
        OutputConfiguracionFolioVirtual output = new OutputConfiguracionFolioVirtual();
        Connection conn = null;
        try {
            conn = getConnRegional();
            BigDecimal idPais = getIdPais(conn, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input);
            log.trace("Respuesta validaci\u00F3n: " + respuesta.getDescripcion());
            if (!"OK".equals(respuesta.getDescripcion())) {
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));

                return output;
            }
            
            boolean existeDispositivos = existeDispositivo(conn, input, idPais);
            if(!existeDispositivos){
            	respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_COD_DISPOSITIVO_INVALIDO, null, nombreClase, nombreMetodo, null, input.getCodArea());
            	output.setRespuesta(respuesta);
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO, "El dispositivo no existe", respuesta.getDescripcion()));
                return output;
            }

            String tipoDocumentoFS = tipoDocumentoFS(conn, input, idPais);
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
            //condicion agregada por pablo.lopez 12/01/2018
			List<Filtro> condicionesExistencia = obtenerCondicionesExistencia(conn, input, estadoAlta, tipoDocumentoFS, idPais);
	        String existencia = UtileriasBD.verificarExistencia(conn, ConfiguracionFolioVirtual.N_TABLA, condicionesExistencia);
	        if (new Integer(existencia) > 0) {
            	respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_FOLIO_EXISTENTE, null, nombreClase, nombreMetodo, null, input.getCodArea());
            	output.setRespuesta(respuesta);
                return output;
	        }
            
            String idPlazaDispositivo = getIdPlazaDispositivo(conn, input, idPais);
            OutputConfiguracionFolioVirtual outputFS = OperacionFoliosSCL.doGetVantive(conn, input, idPlazaDispositivo);
            
            if(outputFS.getFolios() != null && outputFS.getFolios().size() > 0){
            	InputDispositivo dataDispotivico = datosDispositivo(conn, input.getIdDispositivo(), idPais);
            	InputFolioVirtual inputInsert = new InputFolioVirtual();
                InputFolioRutaPanel folio = new InputFolioRutaPanel();
                List<InputFolioRutaPanel> folios = new ArrayList<InputFolioRutaPanel>();
                
                inputInsert.setCodArea(input.getCodArea());
                inputInsert.setUsuario(input.getUsuario());
                inputInsert.setIdTipo(input.getCodDispositivo());
                inputInsert.setTipo(Conf.TIPO_DISPOSITIVOS);
                
                folio.setTipoDocumento(tipoDocumentoFS);
                folio.setSerie(input.getSerie());
                folio.setNoInicialFolio(outputFS.getFolios().get(0).getNoInicialFolio());
                folio.setNoFinalFolio(outputFS.getFolios().get(0).getNoFinalFolio());
                folio.setIdReserva(outputFS.getFolios().get(0).getIdReserva());
                folio.setEstado(estadoAlta);
                //agregado por Daniel Tobar
                folio.setCodOficina(input.getCodOficina());
                folio.setCodVendedor(dataDispotivico.getCodVendedor());  
                folio.setResolucion(dataDispotivico.getResolucion());
                folio.setFecha_resolucion(dataDispotivico.getFechaResolucion());
                folio.setIdTipo(input.getCodDispositivo());
                folios.add(folio);
                inputInsert.setFolios(folios);
                
                doPost(conn, inputInsert, estadoAlta, idPais);
                log.trace(new GsonBuilder().setPrettyPrinting().create().toJson(inputInsert));
                output.setFolios(folios);
                output.setRespuesta(outputFS.getRespuesta());
                
            }
            else{
            	output.setRespuesta(outputFS.getRespuesta());
            }

          
          
            
          listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_PDV, "Se consultaron folios de SCL para el vendedor " + input.getCodVendedor() + ".", ""));
        } catch (SQLException e) {
            respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
            log.error("Excepcion: " + e.getMessage(), e);

            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO, "Problema al reservar folios de FS", e.getMessage()));

        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
            log.error("Excepcion: " + e.getMessage(), e);

            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0", Conf.LOG_TIPO_NINGUNO, "Inconvenientes en el servicio de reserva de folios de FS.", e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }
        return output;
    }
    
    public static boolean existeDispositivo(Connection conn, InputFolioVirtual obj, BigDecimal idPais) throws SQLException{
		boolean ret = true;
        BigDecimal rstRes = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql = "";

        try {
        	sql = "SELECT COUNT (1)  " ;
        	sql = sql + "  FROM TC_SC_DISPOSITIVO  " ;
        	sql = sql + " WHERE CODIGO_DISPOSITIVO = ? AND TCSCCATPAISID = ?";

        	
            log.trace("EXISTE DISPOSITIVO: " + sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, obj.getCodDispositivo());
            pstmt.setBigDecimal(2, idPais);
            rst = pstmt.executeQuery();

            if (rst.next()) {
            	rstRes = rst.getBigDecimal(1);
            	
            	if("0".equals(rstRes.toString())){
            		ret = false;
            	}
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return ret;
	}
    
    
    public static String getIdPlazaDispositivo(Connection conn, InputFolioVirtual obj, BigDecimal idPais) throws SQLException{
        String rstRes = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql = "";

        try {
        	sql = "SELECT ID_PLAZA  " ;
        	sql = sql + "  FROM TC_SC_DISPOSITIVO  " ;
        	sql = sql + " WHERE CODIGO_DISPOSITIVO = ? AND TCSCCATPAISID = ?";

        	
            log.trace("EXISTE DISPOSITIVO: " + sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, obj.getCodDispositivo());
            pstmt.setBigDecimal(2, idPais);
            rst = pstmt.executeQuery();

            if (rst.next()) {
            	rstRes = rst.getString(1);
            	
            
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return rstRes;
	}
    
    private String tipoDocumentoFS(Connection conn, InputFolioVirtual input, BigDecimal idPais) throws SQLException{
    	PreparedStatement pstm = null;
        ResultSet rst = null;
        String sql = "";
        String valor = "";
    	
    	sql = "SELECT VALOR " ;
    	sql = sql + "  FROM TC_SC_CONFIGURACION " ;
    	sql = sql + " WHERE GRUPO = ? AND VALOR= ? AND TCSCCATPAISID = ?" ;
    	
    	log.debug("Qry Count: " + sql);

        try {
            pstm = conn.prepareStatement(sql);
            pstm.setString(1,Conf.TIPO_DOC_FS);
            pstm.setString(2,input.getTipoDocumento());
            pstm.setBigDecimal(3, idPais);
            rst = pstm.executeQuery();
            if (rst.next()) {
                valor = rst.getString("VALOR");
                log.trace("valor: " + valor);
            }
		} finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstm);
        }
        
    	return valor;
    }
    
    public static InputDispositivo datosDispositivo(Connection conn, String idDispositivo, BigDecimal idPais) throws SQLException{
    	PreparedStatement pstm = null;
        ResultSet rst = null;
        String sql = "";
        InputDispositivo valor = new InputDispositivo();
        SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA);
        sql = "SELECT USERID, RESOLUCION, FECHA_RESOLUCION FROM TC_SC_DISPOSITIVO"
        		+ " WHERE TCSCCATPAISID = ?" 
        		+ " AND TCSCDISPOSITIVOID =? " ;
        
    	log.debug("Qry datos dispositivo: " + sql);

        try {
            pstm = conn.prepareStatement(sql);
            pstm.setBigDecimal(1, idPais);
            pstm.setBigDecimal(2, new BigDecimal(idDispositivo));
            rst = pstm.executeQuery();
            if (rst.next()) {
            	valor.setCodVendedor(rst.getString("USERID"));
            	valor.setResolucion(rst.getString("RESOLUCION"));
            	valor.setFechaResolucion(FORMATO_FECHA.format(rst.getTimestamp("FECHA_RESOLUCION")));
            }
		} finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstm);
        }
        
    	return valor;
    }
    
    /**
     * M\u00E9todo que arma el INSERT para insertar un registro en TC_SC_FOLIO
     * @param conn
     * @param input
     * @param estadoAlta
     * @return
     * @throws SQLException
     */
    public static OutputConfiguracionFolioVirtual doPost(Connection conn, InputFolioVirtual input, String estadoAlta, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doPost";
        String nombreClase = new CurrentClassGetter().getClassName();
        int i=0;
        Respuesta respuesta = null;
        OutputConfiguracionFolioVirtual output = null;

        String campos[] = CtrlFolioVirtual.obtenerCamposGetPost(Conf.METODO_POST);
        List<String> inserts = CtrlFolioVirtual.obtenerInsertsPost(input, ConfiguracionFolioVirtual.SEQUENCE, estadoAlta, idPais);

        String sql = UtileriasBD.armarQueryInsertAll(ConfiguracionFolioVirtual.N_TABLA, campos, inserts);
        
        QueryRunner Qr = new QueryRunner();
        i=Qr.update(conn, sql);

        if (i>0) {
            respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_FOLIO_43, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());

            output = new OutputConfiguracionFolioVirtual();
            output.setRespuesta(respuesta);
        }else{
        	respuesta= new Respuesta();
        	respuesta.setCodResultado("0");
        	output = new OutputConfiguracionFolioVirtual();
            output.setRespuesta(respuesta);
        }

        return output;
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las consultas de verificaci\u00F3n
     * de existencia del recurso.
     * @param conn 
     * 
     * @param idPadre
     * @param input
     * @param metodo 
     * @param estadoAlta 
     * @return
     * @throws SQLException 
     */
    public static List<Filtro> obtenerCondicionesExistencia(Connection conn, InputFolioVirtual input, String estadoAlta, String tipoDocumentoFS, BigDecimal idPais) throws SQLException {
    	List<Filtro> condiciones = new ArrayList<Filtro>();
    	List<Filtro> condicionesEstados = new ArrayList<Filtro>();
    	List<Filtro> condicionesAgrupadas = new ArrayList<Filtro>();
    	String estadoEnUso = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_FOLIOS, Conf.FOLIO_EN_USO, input.getCodArea());
    	condicionesAgrupadas.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ConfiguracionFolioVirtual.CAMPO_TC_SC_CATPAISID, idPais.toString()));

    	condicionesEstados.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, ConfiguracionFolioVirtual.CAMPO_ESTADO, estadoAlta));
    	condicionesEstados.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, ConfiguracionFolioVirtual.CAMPO_ESTADO, estadoEnUso));

		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioVirtual.CAMPO_TIPODOCUMENTO, tipoDocumentoFS));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioVirtual.CAMPO_SERIE, input.getSerie()));

		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioVirtual.CAMPO_COD_OFICINA, input.getCodOficina()));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioVirtual.CAMPO_ID_TIPO, input.getCodDispositivo()));
		condiciones.add(new Filtro(Filtro.AND, "", "", UtileriasBD.agruparCondiciones(condicionesEstados)));
		condicionesAgrupadas.add(new Filtro(Filtro.OR, "", "", UtileriasBD.agruparCondiciones(condiciones)));

    	return condicionesAgrupadas;
    }
}

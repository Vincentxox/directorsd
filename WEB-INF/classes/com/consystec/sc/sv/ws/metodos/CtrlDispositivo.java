package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.dispositivo.OutputDispositivo;
import com.consystec.sc.sv.ws.operaciones.OperacionDispositivo;
import com.consystec.sc.sv.ws.orm.Dispositivo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author susana barrios Consystec 2015
 */
public class CtrlDispositivo extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlDispositivo.class);
    private static String servicioGet = Conf.LOG_GET_DISPOSITIVO;
    private static String servicioPost = Conf.LOG_POST_DISPOSITIVO;
    private static String servicioPut = Conf.LOG_PUT_DISPOSITIVO;

    /***
     * Validando que no vengan par\u00E9metros nulos
     * @param estadoAlta 
     * @param conn 
     * @throws SQLException 
     */
    public Respuesta validarDatos(Connection conn, InputDispositivo objDatos, int metodo, String estadoAlta, BigDecimal idPais) throws SQLException {
        Respuesta objRespuesta = null;
        String nombreMetodo = "validarDatos";
        List<BigDecimal> lstValidaVendAsignado ;
        boolean validacionesTipo = false;

        if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
            if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            }

            if (objDatos.getNumTelefono() == null || "".equals(objDatos.getNumTelefono().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_NUMTELEFONO_70, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            }

            if (objDatos.getDescripcion() == null || "".equals(objDatos.getDescripcion().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_DESCRIPCION_NULO_69, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            }

            if (objDatos.getCajaNumero() == null || "".equals(objDatos.getCajaNumero().trim()) ) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_CAJA_NUMERO_766, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            }

            if (objDatos.getZona() == null || "".equals(objDatos.getZona().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ZONA_768, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            }

            if (objDatos.getResolucion() == null || "".equals(objDatos.getResolucion().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_RESOLUCION_767, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            }

            // Verificaci\u00F3n de fecha
            try {
            	log.info("Fecha "+objDatos.getFechaResolucion());
                if (objDatos.getFechaResolucion() == null || "".equals(objDatos.getFechaResolucion().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHANULO_40, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
                    Date fecha = sdf.parse(objDatos.getFechaResolucion());
                    log.trace("Fecha: " + fecha);
                    Date fechaActual = sdf.parse(sdf.format(new Date()));
                    log.trace("Fecha actual: " + fechaActual);

                    if (fecha.after(fechaActual)) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHA_VENTA_INCORRECTA_476, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
                    }
                }
            } catch (java.text.ParseException e) {
                log.error("Problema al convertir la fecha.",e);
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_CONVERTIR_FECHA_165, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            }
            
            //
            if ("503".equals(objDatos.getCodArea()))
            {
            	if (objDatos.getCodOficina()==null || "".equals(objDatos.getCodOficina()))
            	{
                     objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CODOFICINA_856, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            	}


            	if (objDatos.getIdPlaza()==null || "".equals(objDatos.getIdPlaza()))
            	{
                     objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDPLAZA_VACIO_687, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            	}
            	

            	if (objDatos.getIdPuntoVenta()==null || "".equals(objDatos.getIdPuntoVenta()))
            	{
                     objRespuesta = getMensaje(Conf_Mensajes.MSJ_IPUNTOVENTA_VACIO_688, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            	}
            	
            	if (objDatos.getUserId()==null || "".equals(objDatos.getUserId()))
            	{
                     objRespuesta = getMensaje(Conf_Mensajes.MSJ_USERID_VACIO_689, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            	}
            	
            	if (objDatos.getUsername()==null || "".equals(objDatos.getUsername()))
            	{
                     objRespuesta = getMensaje(Conf_Mensajes.MSJ_USERNAMER_VACIO_690, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            	}
            	
            }
            
        }

        if (metodo == Conf.METODO_POST) {
            if (objDatos.getCodigoDispositivo() == null || "".equals(objDatos.getCodigoDispositivo().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODDISPOSITIVO_NULO_67, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            }

            if (objDatos.getModelo() == null || "".equals(objDatos.getModelo().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_MODELO_NULO_68, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            }
        }

        if ((metodo == Conf.METODO_POST && "505".equals(objDatos.getCodArea()))
                || (metodo == Conf.METODO_PUT && objDatos.getEstado() != null && objDatos.getEstado().equalsIgnoreCase(estadoAlta))) {
        		if("505".equals(objDatos.getCodArea())) {
	            	if (objDatos.getCodVendedor() == null || "".equals(objDatos.getCodVendedor().trim()) || !isNumeric(objDatos.getCodVendedor())) {
	                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CODVENDEDOR_851, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
	                } else {
	                    if (OperacionDispositivo.validarCodVendedor(conn, objDatos.getCodVendedor(), estadoAlta, objDatos.getIdDistribuidor(), idPais)) {
	                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_CODVENDEDOR_DISPOSITIVO_886, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
	                    }
	
	                    String codOficina = OperacionDispositivo.getCodOficina(objDatos.getIdDistribuidor(), objDatos.getCodVendedor(), idPais);
	                    if (codOficina == null) {
	                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_VEND_OFICINA_DISPOSITIVO_885, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
	                    } else {
	                        objDatos.setCodOficina(codOficina);
	                    }
                }
            }
        }


        boolean condicion3=metodo == Conf.METODO_POST && "505".equals(objDatos.getCodArea());
        if ((metodo == Conf.METODO_PUT && (objDatos.getResponsable() != null && !"".equals(objDatos.getResponsable().trim()))) || (condicion3)) {
            validacionesTipo = true;
        }

        if (validacionesTipo) {
            if (objDatos.getIdDistribuidor() == null || "".equals(objDatos.getIdDistribuidor().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.IDDTS_NULO_36, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            } else if (!isNumeric(objDatos.getIdDistribuidor())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            }  
        	
            if (objDatos.getResponsable() == null || "".equals(objDatos.getResponsable().trim()) || !isNumeric(objDatos.getResponsable())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_RESPONSABLE_NUM_717, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
            } else {
                if (objDatos.getTipoResponsable() == null || "".equals(objDatos.getTipoResponsable().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPORESPONSABLE_NULO_80, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
                } else {
                    String tipo = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, objDatos.getTipoResponsable().toUpperCase(), objDatos.getCodArea());

                    if (tipo == null || "".equals(tipo)) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_TIPOOFERTA_NO_DEFINIDO_424, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
                    }
                }
            }

            if (objRespuesta == null) {
                objRespuesta = OperacionDispositivo.validarPanelRuta(conn, objDatos.getResponsable(),
                        objDatos.getTipoResponsable(), estadoAlta, objDatos.getIdDispositivo(), objDatos.getIdDistribuidor(), objDatos.getCodArea(), idPais);
            }

            if (objRespuesta == null) {
                if (objDatos.getVendedorAsignado() == null || "".equals(objDatos.getVendedorAsignado().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_VEND_ASIGNADO_NULO_543, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
                } else {
                    lstValidaVendAsignado = OperacionDispositivo.validaVendDispositivo(conn,
                            objDatos.getTipoResponsable(), objDatos.getVendedorAsignado(), estadoAlta,
                            objDatos.getResponsable(), objDatos.getIdDispositivo(), objDatos.getCodArea(), idPais);

                    if (lstValidaVendAsignado.get(0).intValue() == 0) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_VEND_NO_ASIGNADO_RUTAPANEL_544, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
                    }

                    if (lstValidaVendAsignado.get(1).intValue() > 0) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_VEND_ASIGNADO_TIENE_DISP_545, null, this.getClass().toString(), nombreMetodo, null, objDatos.getCodArea());
                    }
                }
            }
        }

        if (objRespuesta != null) {
            log.trace("resultado:" + objRespuesta.getDescripcion());
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para registrar dispositivos en SIDRA los cuales ser\u00E9n utilizados
     * por vendedores
     * 
     * @param objDatos
     * @return
     */
    public OutputDispositivo creaDispositivo(InputDispositivo objDatos) {
    	List<LogSidra> listaLog = new ArrayList<LogSidra>();
        OutputDispositivo respuesta = new OutputDispositivo();
        Respuesta objRespuesta = new Respuesta();
        String estadoAlta = "";
        List<BigDecimal> lista = new ArrayList<BigDecimal>();
        BigDecimal idDispositivo = null;
        Connection conn = null;
        String metodo = "creaDispositivo";
        Dispositivo objNuevo = new Dispositivo();

        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
            estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());

            objRespuesta = validarDatos(conn, objDatos, Conf.METODO_POST, estadoAlta, ID_PAIS);

            if (objRespuesta == null) {
                // verificando si el codigo dispositivo o numero de telefono ingresados ya existen
                lista = OperacionDispositivo.existeCodDispositivo(conn, objDatos.getCodigoDispositivo(), estadoAlta,
                        new BigDecimal(objDatos.getNumTelefono()), new BigDecimal(0), ID_PAIS);

                if (lista.get(0).intValue() > 0) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_YAEXISTE_CODDISPOSITIVO_71, null,
                            this.getClass().toString(), metodo, "", objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_DISPOSITIVO, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                            objRespuesta.getDescripcion()));

                } else if (lista.get(1).intValue() > 0) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NUMTELEFONO_EXISTE_72, null, this.getClass().toString(),
                            metodo, "", objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_DISPOSITIVO, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                            objRespuesta.getDescripcion()));

                } else {
                    // si no existen valores ingresados se procede a insertar
                    objNuevo.setTcsccatpaisid(ID_PAIS);
                    objNuevo.setCodigo_dispositivo(objDatos.getCodigoDispositivo());
                    objNuevo.setDescripcion(objDatos.getDescripcion());
                    objNuevo.setModelo(objDatos.getModelo());
                    objNuevo.setCreado_por(objDatos.getUsuario());
                    objNuevo.setEstado(estadoAlta);
                    objNuevo.setCaja_numero(new BigDecimal(objDatos.getCajaNumero()));
                    objNuevo.setZona(objDatos.getZona());
                    objNuevo.setResolucion(objDatos.getResolucion());
                    Date fechaResolucion = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA).parse(objDatos.getFechaResolucion());
                    objNuevo.setFecha_resolucion(new Timestamp(fechaResolucion.getTime()));
                    objNuevo.setNum_telefono(new BigDecimal(objDatos.getNumTelefono()));

                    objNuevo.setTcscdtsid(objDatos.getIdDistribuidor() != null && !"".equals(objDatos.getIdDistribuidor().trim())
                            ? new BigDecimal(objDatos.getIdDistribuidor()) : null);
                    objNuevo.setResponsable(objDatos.getResponsable() != null && !"".equals(objDatos.getResponsable().trim())
                            ? new BigDecimal(objDatos.getResponsable()) : null);
                    objNuevo.setTipo_responsable(objDatos.getTipoResponsable());
                    objNuevo.setVendedor_asignado(objDatos.getVendedorAsignado() != null && !"".equals(objDatos.getVendedorAsignado().trim())
                            ? new BigDecimal(objDatos.getVendedorAsignado()) : null);
                    objNuevo.setCod_oficina(objDatos.getCodOficina());
                    objNuevo.setCod_vendedor(objDatos.getCodVendedor());
                    objNuevo.setId_plaza(objDatos.getIdPlaza());
                    objNuevo.setId_puntoventa(objDatos.getIdPuntoVenta());
                    objNuevo.setUserid(objDatos.getUserId());
                    objNuevo.setUsername(objDatos.getUsername());
                    objNuevo.setUserToken(objDatos.getUserToken());
                    objNuevo.setDevelopToken(objDatos.getDevelopToken());
                    objNuevo.setUserToken(objDatos.getUserToken());
                    

                    idDispositivo = OperacionDispositivo.insertDispositivo(conn, objNuevo);
                    respuesta.setIdDispositivo(idDispositivo.toString());

                    objRespuesta = getMensaje(Conf_Mensajes.OK_DISPOSITIVO15, null, null, null, "", objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_DISPOSITIVO, servicioPost,
                            idDispositivo + "", Conf.LOG_TIPO_DISPOSITIVO,
                            "Se cre\u00F3 un nuevo dispositivo con ID " + idDispositivo + " y c\u00F3digo "
                                    + objDatos.getCodigoDispositivo().toUpperCase() + ".",
                            ""));
                }
            } else {
                respuesta.setRespuesta(objRespuesta);
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_DISPOSITIVO, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
            }

        } catch (SQLException e) {
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_DISPOSITIVO, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al crear nuevo dispositivo.", e.getMessage()));

        } catch (Exception e) {
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_DISPOSITIVO, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al crear nuevo dispositivo.", e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);
            respuesta.setRespuesta(objRespuesta);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }

        return respuesta;
    }

    /**
     * M\u00E9todo para modificar dispositivos en SIDRA los cuales ser\u00E9n utilizados
     * por vendedores
     * 
     * @param objDatos
     * @return
     */
    public OutputDispositivo modificaDispositivo(InputDispositivo objDatos) {
    	List<LogSidra> listaLog = new ArrayList<LogSidra>();
        OutputDispositivo respuesta = new OutputDispositivo();
        Respuesta objRespuesta = new Respuesta();
        String estadoAlta = "";
        List<BigDecimal> lista = new ArrayList<BigDecimal>();
        Connection conn = null;
        String metodo = "modificaDispositivo";
        Dispositivo objNuevo = new Dispositivo();

        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
            estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());

            objRespuesta = validarDatos(conn, objDatos, Conf.METODO_PUT, estadoAlta, ID_PAIS);

            if (objRespuesta == null) {
                if (objDatos.getIdDispositivo() == null || "".equals(objDatos.getIdDispositivo())) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDDISPOSITIVO_NULO_66, null, this.getClass().toString(), metodo, null, objDatos.getCodArea());
                } else {
                	if (objDatos.getEstado() == null || "".equals(objDatos.getEstado())) {
                        objRespuesta = new Respuesta();
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_ESTADO_NULO_11, null, this.getClass().toString(), metodo, null, objDatos.getCodArea());
                    } else {
                    	String estadoBaja = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, objDatos.getCodArea());
                    	String estadoDispositivo = getEstadoDispositivo(conn, objDatos.getIdDispositivo(), ID_PAIS);
                    	if (estadoBaja.equalsIgnoreCase(objDatos.getEstado()) || estadoAlta.equalsIgnoreCase(objDatos.getEstado())) {
                        	String estadoPendienteSiniestro = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SINIESTROS, Conf.SINIESTRO_ESTADO_EN_PROCESO, objDatos.getCodArea());
                        	String estadoSiniestrado = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SINIESTROS, Conf.SINIESTRO_ESTADO_SINIESTRADO, objDatos.getCodArea());
                        	if(estadoDispositivo.equalsIgnoreCase(estadoPendienteSiniestro) || estadoDispositivo.equalsIgnoreCase(estadoSiniestrado)) {
                                objRespuesta = new Respuesta();
                                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ESTADODISPOSITIVO_NO_MODIFICAR_696, null, this.getClass().toString(), metodo, null, objDatos.getCodArea());
                                objRespuesta.setDescripcion(objRespuesta.getDescripcion().replace("@@ESTADO", estadoDispositivo));
                        	}
                    	}
                    }
                }

                if (objRespuesta == null) {
                    // verificando si el codigo dispositivo o numero de telefono ingresados ya existen
                    lista = OperacionDispositivo.existeCodDispositivo(conn, "", estadoAlta,
                            new BigDecimal(objDatos.getNumTelefono()), new BigDecimal(objDatos.getIdDispositivo()), ID_PAIS);

                    if (lista.get(0).intValue() > 0) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_YAEXISTE_CODDISPOSITIVO_71, null, this.getClass().toString(), metodo, "", objDatos.getCodArea());

                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_DISPOSITIVO, servicioPut,
                                objDatos.getIdDispositivo(), Conf.LOG_TIPO_DISPOSITIVO,
                                "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

                    } else if (lista.get(1).intValue() > 0) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_NUMTELEFONO_EXISTE_72, null, this.getClass().toString(), metodo, "", objDatos.getCodArea());

                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_DISPOSITIVO, servicioPut,
                                objDatos.getIdDispositivo(), Conf.LOG_TIPO_DISPOSITIVO,
                                "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

                    } else {
                        //si no existen valores ingresados se procede a modificar

                        objNuevo.setTcscdispositivoid(new BigDecimal(objDatos.getIdDispositivo()));
                        objNuevo.setTcsccatpaisid(ID_PAIS);
                        objNuevo.setDescripcion(objDatos.getDescripcion());
                        objNuevo.setModificado_por(objDatos.getUsuario());
                        objNuevo.setEstado(objDatos.getEstado().toUpperCase());
                        objNuevo.setNum_telefono(new BigDecimal(objDatos.getNumTelefono()));
                        objNuevo.setCaja_numero(new BigDecimal(objDatos.getCajaNumero()));
                        objNuevo.setZona(objDatos.getZona());
                        objNuevo.setResolucion(objDatos.getResolucion());
                        Date fechaResolucion = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA).parse(objDatos.getFechaResolucion());
                        objNuevo.setFecha_resolucion(new Timestamp(fechaResolucion.getTime()));
                        objNuevo.setTcscdtsid(objDatos.getIdDistribuidor() != null && !"".equals(objDatos.getIdDistribuidor().trim())
                                ? new BigDecimal(objDatos.getIdDistribuidor()) : null);
                        objNuevo.setResponsable(objDatos.getResponsable() != null && !"".equals(objDatos.getResponsable().trim())
                                ? new BigDecimal(objDatos.getResponsable()) : null);
                        objNuevo.setNombre_responsable(objDatos.getNombreResponsable());
                        objNuevo.setTipo_responsable(objDatos.getTipoResponsable());
                        objNuevo.setVendedor_asignado(objDatos.getVendedorAsignado() != null && !"".equals(objDatos.getVendedorAsignado().trim())
                                ? new BigDecimal(objDatos.getVendedorAsignado()) : null);
                        objNuevo.setCod_oficina(objDatos.getCodOficina());
                        objNuevo.setCod_vendedor(objDatos.getCodVendedor());
                        objNuevo.setId_plaza(objDatos.getIdPlaza());
                        objNuevo.setId_puntoventa(objDatos.getIdPuntoVenta());
                        objNuevo.setUserid(objDatos.getUserId());
                        objNuevo.setUsername(objDatos.getUsername());
                        objNuevo.setDevelopToken(objDatos.getDevelopToken());
                        objNuevo.setUserToken(objDatos.getUserToken()); 

                        OperacionDispositivo.updateDispositivo(conn, objNuevo, objDatos.getCodArea());

                        objRespuesta = getMensaje(Conf_Mensajes.OK_MOD_DISPOSITIVO_40, null, null, null, "", objDatos.getCodArea());

                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_DISPOSITIVO, servicioPut,
                                objDatos.getIdDispositivo(), Conf.LOG_TIPO_DISPOSITIVO,
                                "Se modificaron datos del dispositivo con ID " + objDatos.getIdDispositivo() + ".",
                                ""));
                    }

                } else {
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_DISPOSITIVO, servicioPut,
                            objDatos.getIdDispositivo() != null ? objDatos.getIdDispositivo() : "0",
                            Conf.LOG_TIPO_DISPOSITIVO, "Problema en la validaci\u00F3n de datos.",
                            objRespuesta.getDescripcion()));
                }
            }
        } catch (SQLException e) {
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_DISPOSITIVO, servicioPut,
                    objDatos.getIdDispositivo(), Conf.LOG_TIPO_DISPOSITIVO, "Problema al modificar dispositivo.",
                    e.getMessage()));

        } catch (Exception e) {
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_DISPOSITIVO, servicioPut,
                    objDatos.getIdDispositivo(), Conf.LOG_TIPO_DISPOSITIVO, "Problema al modificar dispositivo.",
                    e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);
            respuesta.setRespuesta(objRespuesta);
            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }

        return respuesta;
    }

    /**
     * M\u00E9todo para obtener informaci\u00F3n de dispositivos
     * 
     ***/
    public OutputDispositivo getDispositivo(InputDispositivo objDatos) {
    	List<LogSidra> listaLog = new ArrayList<LogSidra>();
        OutputDispositivo respuesta = new OutputDispositivo();
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        List<InputDispositivo> lstDispositivo = new ArrayList<InputDispositivo>();
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getDispositivo";
        Connection conn = null;
        try {
            if (!(objDatos.getIdDispositivo() == null || "".equals(objDatos.getIdDispositivo().trim()))) {
                log.trace("entra a filtro id Dispositivo");
                lstFiltros.add(new Filtro(Dispositivo.CAMPO_TCSCDISPOSITIVOID, Filtro.EQ, objDatos.getIdDispositivo()));
            }

            if (!(objDatos.getNumTelefono() == null || "".equals(objDatos.getNumTelefono().trim()))) {
                log.trace("entra a filtro numTelefono");
                lstFiltros.add(new Filtro(Dispositivo.CAMPO_NUM_TELEFONO, Filtro.EQ, objDatos.getNumTelefono()));
            }

            if (!(objDatos.getDescripcion() == null || "".equals(objDatos.getDescripcion().trim()))) {
                log.trace("entra a filtro descripcion");
                lstFiltros.add(new Filtro("UPPER(" + Dispositivo.CAMPO_DESCRIPCION + ")", Filtro.LIKE,
                        "'%" + objDatos.getDescripcion().toUpperCase() + "%'"));
            }

            if (!(objDatos.getModelo() == null || "".equals(objDatos.getModelo().trim()))) {
                log.trace("entra a filtro modelo");
                lstFiltros.add(new Filtro("UPPER(" + Dispositivo.CAMPO_MODELO + ")", Filtro.LIKE,
                        "'%" + objDatos.getModelo().toUpperCase() + "%'"));
            }

            if (!(objDatos.getEstado() == null || "".equals(objDatos.getEstado().trim()))) {
                log.trace("entra a filtro  estado");
                lstFiltros.add(new Filtro("UPPER(" + Dispositivo.CAMPO_ESTADO + ")", Filtro.EQ,
                        "'" + objDatos.getEstado().toUpperCase() + "'"));
            }

            if (!(objDatos.getCodigoDispositivo() == null || "".equals(objDatos.getCodigoDispositivo().trim()))) {
                log.trace("entra a filtro  codigo dispositivo");
                lstFiltros.add(new Filtro("UPPER(" + Dispositivo.CAMPO_CODIGO_DISPOSITIVO + ")", Filtro.EQ,
                        "'" + objDatos.getCodigoDispositivo().toUpperCase() + "'"));
            }

            if (objDatos.getResponsable() != null && !"".equals(objDatos.getResponsable().trim())) {
                if (!isNumeric(objDatos.getResponsable())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_RESPONSABLE_NUM_717, null,
                            this.getClass().toString(), metodo, null, objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n al consultar datos de dispositivos.",
                            null));

                    return respuesta;

                } else if (objDatos.getTipoResponsable() == null ||"".equals(objDatos.getTipoResponsable().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPO_NULO_98, null, this.getClass().toString(), metodo,
                            null, objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n al consultar datos de dispositivos.",
                            null));

                    return respuesta;

                } else {
                    lstFiltros.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                            Dispositivo.CAMPO_RESPONSABLE, objDatos.getResponsable()));
                }
            }

            if (objDatos.getTipoResponsable() != null && !"".equals(objDatos.getTipoResponsable().trim())) {
                lstFiltros.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
                        Dispositivo.CAMPO_TIPO_RESPONSABLE, objDatos.getTipoResponsable()));
            }

            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
            lstFiltros.add(new Filtro(Dispositivo.CAMPO_TCSCCATPAISID, Filtro.EQ, ID_PAIS));
            lstDispositivo = OperacionDispositivo.getDispositivo(conn, lstFiltros, objDatos.getCodArea(), ID_PAIS);
            if (lstDispositivo.isEmpty()) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOHAYDATOS_42, null, this.getClass().toString(), metodo,
                        null, objDatos.getCodArea());
            } else {
                respuesta.setDispositivos(lstDispositivo);
                objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null, null, objDatos.getCodArea());
            }

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de dispositivos.", ""));

        } catch (SQLException e) {
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de dispositivos.", e.getMessage()));

        } catch (Exception e) {
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de dispositivos.", e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);
            respuesta.setRespuesta(objRespuesta);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }

        return respuesta;
    }
    
    public String getEstadoDispositivo(Connection conn, String idDispositivo, BigDecimal idPais) throws SQLException {
    	PreparedStatement stmt = null; 
    	ResultSet rst = null;
    	String query = "SELECT estado FROM TC_SC_DISPOSITIVO WHERE tcscdispositivoid = ? AND tcsccatpaisid = ?";
    	try {
    		stmt = conn.prepareStatement(query);
    		stmt.setBigDecimal(1, new BigDecimal(idDispositivo));
    		stmt.setBigDecimal(2, idPais);
    		rst = stmt.executeQuery();
    		if(rst != null && rst.next()) {
    			return rst.getString(Dispositivo.CAMPO_ESTADO);
    		}
    	} finally {
    		DbUtils.closeQuietly(rst);
    		DbUtils.closeQuietly(stmt);
    	}
    	return "";
    }
}


package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.buzon.InputBuzon;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.buzon.OutputBuzon;
import com.consystec.sc.sv.ws.operaciones.OperacionBuzon;
import com.consystec.sc.sv.ws.orm.BuzonSidra;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author susana barrios Consystec 2015
 */
public class CtrlBuzonSidra extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlBuzonSidra.class);
    private static List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_BUZON;
    private static String servicioPost = Conf.LOG_POST_BUZON;
    private static String servicioPut = Conf.LOG_PUT_BUZON;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    
    /***
     * Validando que no vengan par\u00E9metros nulos
     * @param conn 
     * @throws SQLException 
     */
    public Respuesta validarDatos(Connection conn, InputBuzon objDatos, int tipoMetodo) throws SQLException {
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";
        String tipoWf = "";
        String estadoAlta="";
        String tipoWfTodos="";
        String tipoWfDeuda="";
        List<Filtro> condiciones=new ArrayList<Filtro>();
        
        if (tipoMetodo == 1 || tipoMetodo == 0) {
            if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, this.getClass().toString(), null, metodo,
                        null, objDatos.getCodArea());
            }

            if (objDatos.getNombre() == null || "".equals(objDatos.getNombre().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOMBRE_NULO_45, this.getClass().toString(), null, metodo,
                         null, objDatos.getCodArea());
            }
        }
        if (tipoMetodo == 1 || tipoMetodo == 2) {
            if (objDatos.getNivel() == null || "".equals(objDatos.getNivel().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_NIVELBUZON_NULO_529, this.getClass().toString(), null,
                        metodo,  null, objDatos.getCodArea());

            } else if (objDatos.getNivel().equalsIgnoreCase(Conf.NIVEL_BUZON_DTS + "")
                    || objDatos.getNivel().trim().equalsIgnoreCase(Conf.NIVEL_BUZON_TELCA + "")
                    || objDatos.getNivel().trim().equalsIgnoreCase(Conf.NIVEL_ZONACOMERCIAL + "")) {

                if (objDatos.getNivel().equalsIgnoreCase(Conf.NIVEL_BUZON_DTS + "")&& (objDatos.getIdDistribuidor() == null || "".equals(objDatos.getIdDistribuidor().trim()))) {
                        objRespuesta = getMensaje(Conf_Mensajes.IDDTS_NULO_36, this.getClass().toString(), null, metodo,
                                 null, objDatos.getCodArea());
                }
                //validando si es buzon de nivel 3 debe ir obligatoriamente el id bodega virtual, es que la zona comercial
                if (objDatos.getNivel().equalsIgnoreCase(Conf.NIVEL_ZONACOMERCIAL + "")) {

                    if (objDatos.getIdDistribuidor() == null || "".equals(objDatos.getIdDistribuidor().trim())) {
                        objRespuesta = getMensaje(Conf_Mensajes.IDDTS_NULO_36, this.getClass().toString(), null, metodo,
                                 null, objDatos.getCodArea());
                    }
                    if( tipoMetodo == 1 && (objDatos.getIdBodegaVirtual() == null || "".equals(objDatos.getIdBodegaVirtual().trim()))) {
	                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDBODVIRTUAL_NULO_45, this.getClass().toString(), null, metodo,
	                                 null, objDatos.getCodArea());
                    }
                }
            } else {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_NIVELBUZON_INVALIDO_530, this.getClass().toString(), null,
                        metodo,  null, objDatos.getCodArea());
            }
        }

        if (tipoMetodo == 1) {
            if (objDatos.getTipoWF() == null || "".equals(objDatos.getTipoWF().trim())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOWF_NULO_531, this.getClass().toString(), null, metodo,
                         null, objDatos.getCodArea());
            } else {
                tipoWf = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPO, objDatos.getTipoWF(), objDatos.getCodArea());

                if (tipoWf == null || "".equals(tipoWf.trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOWF_INVALIDO_532, this.getClass().toString(), null,
                            metodo,  null, objDatos.getCodArea());
                }
            }
            
            if(objDatos.getNivel().equalsIgnoreCase(Conf.NIVEL_BUZON_DTS + ""))
        	{
        		tipoWfTodos = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPO, Conf.SOL_TIPO_TODAS, objDatos.getCodArea());
        		tipoWfDeuda = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPO, Conf.SOL_TIPO_DEUDA, objDatos.getCodArea());
        		
        		//validamos que el tipo de WF sea Todos y Deuda
        		if ((tipoWfTodos).equalsIgnoreCase(objDatos.getTipoWF()) || (tipoWfDeuda).equalsIgnoreCase(objDatos.getTipoWF()))
        		{
        			//validamos que no exista mas de un buzon de tipo Todos y Deuda
            		try
            		{
            			estadoAlta=UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());
            			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_TCSCCATPAIS_ID, ID_PAIS.toString()));
            			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BuzonSidra.CAMPO_ESTADO, estadoAlta));
            			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BuzonSidra.CAMPO_TIPO_WORKFLOW, objDatos.getTipoWF()));
            			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_TCSCDTSID, objDatos.getIdDistribuidor()));
            			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BuzonSidra.CAMPO_NIVEL, objDatos.getNivel()));
            			
            			int existeBuzon=UtileriasBD.selectCount(conn, BuzonSidra.N_TABLA, condiciones);
            			if (existeBuzon>0)
            			{
            				//El buzon ya existe para el distribuidor ingresado
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPO_BUZON_EXISTENTE_662, this.getClass().toString(), null,
                                    metodo,  null, objDatos.getCodArea());
            			}
            		}
            		catch(Exception e)
            		{
            			log.error(e, e);
                        objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), 
                                metodo,  null, objDatos.getCodArea());
            		}
        		}
        		else
        		{        		
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOWF_INVALIDO_532, this.getClass().toString(), null,
                            metodo,  null, objDatos.getCodArea());
        		}
        		
        	}
        }

        if (objRespuesta != null) {
            log.trace("resultado:" + objRespuesta.getDescripcion());
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para crear buzones
     * @throws SQLException 
     */
    public OutputBuzon crearBuzon(InputBuzon objDatos) {
        listaLog = new ArrayList<LogSidra>();
        OutputBuzon objReturn = new OutputBuzon();
        BigDecimal idBuzon = null;
        Respuesta objRespuesta = new Respuesta();
        BuzonSidra objBuzon = new BuzonSidra();
        BigDecimal existeBuzon = null;
        Connection conn = null;
        String metodo = "crearBuzon";
        String estadoAlta = "";
        COD_PAIS = objDatos.getCodArea();


        log.trace("inicia a validar valores...");

        try {
            conn = getConnRegional();
             ID_PAIS=getIdPais(conn, objDatos.getCodArea());
            objRespuesta = validarDatos(conn, objDatos, 1);

            if (objRespuesta == null) {
                estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());

                // armando objeto Buzon
                objBuzon.setCreado_por(objDatos.getUsuario());
                objBuzon.setNombre(objDatos.getNombre());
                objBuzon.setEstado(estadoAlta);
                objBuzon.setTipo_workflow(objDatos.getTipoWF());
                objBuzon.setNivel(new BigDecimal(objDatos.getNivel()));
                objBuzon.setTcsccatpaisid(ID_PAIS);
                
                if((objDatos.getNivel().equalsIgnoreCase("" + Conf.NIVEL_ZONACOMERCIAL))){
                	objBuzon.setTcscbodegavirtualid(new BigDecimal(objDatos.getIdBodegaVirtual()));                	
                }else{
                	objBuzon.setTcscbodegavirtualid(null);
                }

                if ((objDatos.getNivel().equalsIgnoreCase("" + Conf.NIVEL_BUZON_DTS))||(objDatos.getNivel().equalsIgnoreCase("" + Conf.NIVEL_ZONACOMERCIAL))) {
                    objBuzon.setTcscdtsid(new BigDecimal(objDatos.getIdDistribuidor()));
                } else {
                    objBuzon.setTcscdtsid(null);
                }

                existeBuzon = OperacionBuzon.existeBuzon(conn, objBuzon.getNombre().toUpperCase(), new BigDecimal(0),
                        objBuzon.getTcscdtsid(), estadoAlta, objDatos.getNivel(), objBuzon.getTcscbodegavirtualid(), ID_PAIS);
                log.trace("existebuzon:" + existeBuzon);
                if (existeBuzon.intValue() == 0) {
                    idBuzon = OperacionBuzon.insertBuzon(conn, objBuzon);
                    objRespuesta = getMensaje(Conf_Mensajes.OK_CREABUZON10, null, null, null, "", objDatos.getCodArea());
                    objReturn.setIdBuzon("" + idBuzon);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BUZON, servicioPost, idBuzon + "",
                            Conf.LOG_TIPO_BUZON, "Se cre\u00F3 un nuevo buz\u00F3n con ID " + idBuzon
                            + " y nombre " + objBuzon.getNombre().toUpperCase() + ".", ""));
                } else {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOMBREBUZON_YAEXISTE_46, "", this.getClass().toString(),
                            metodo, "", objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BUZON, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al crear nuevo buz\u00F3n.", objRespuesta.getDescripcion()));
                }

            } else {
                objReturn.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BUZON, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BUZON, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al crear nuevo buz\u00F3n.", e.getMessage()));
        } catch (Exception e) {
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BUZON, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al crear nuevo buz\u00F3n.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            objReturn.setRespuesta(objRespuesta);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }

        return objReturn;
    }

    /**
     * M\u00E9todo para crear modificar buzones
     * @throws SQLException 
     */
    public OutputBuzon modBuzon(InputBuzon objDatos) {
        listaLog = new ArrayList<LogSidra>();
        OutputBuzon objReturn = new OutputBuzon();
        BigDecimal existeUsuarioBuzon = null;
        Respuesta objRespuesta = new Respuesta();
        BuzonSidra objBuzon = new BuzonSidra();
        BigDecimal existeBuzon = null;
        Connection conn = null;
        String metodo = "crearBuzon";
        String estadoBaja = "";
        String estadoAlta = "";
        COD_PAIS = objDatos.getCodArea();
        log.trace("inicia a validar valores...");

        try {
            conn = getConnRegional();
            ID_PAIS=getIdPais(conn, objDatos.getCodArea());
            
            estadoBaja = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, objDatos.getCodArea());
            estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());

            objRespuesta = validarDatos(conn, objDatos, 0);
            if (objRespuesta == null) {
                if (objDatos.getEstado() == null || "".equals(objDatos.getEstado().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_ESTADO_NULO_11, "", this.getClass().toString(), metodo,
                            "", objDatos.getCodArea());
                    objReturn.setRespuesta(objRespuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BUZON, servicioPut,
                            objDatos.getIdBuzon() != null ? objDatos.getIdBuzon() : "0", Conf.LOG_TIPO_BUZON,
                            "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

                } else if (objDatos.getIdBuzon() == null || "".equals(objDatos.getIdBuzon().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDBUZONNULO_47, "", this.getClass().toString(), metodo,
                            "", objDatos.getCodArea());
                    objReturn.setRespuesta(objRespuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BUZON, servicioPut,
                            objDatos.getIdBuzon() != null ? objDatos.getIdBuzon() : "0", Conf.LOG_TIPO_BUZON,
                            "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

                } else {
                    // armando objeto Buzon
                    objBuzon.setModificado_por(objDatos.getUsuario());
                    objBuzon.setNombre(objDatos.getNombre());
                    objBuzon.setEstado(objDatos.getEstado().toUpperCase());
                    objBuzon.setTcscbuzonid(new BigDecimal(objDatos.getIdBuzon()));

                    List<Filtro> condiciones = new ArrayList<Filtro>();
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                            BuzonSidra.CAMPO_TCSCBUZONID, objDatos.getIdBuzon()));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                            BuzonSidra.CAMPO_TCSCCATPAIS_ID, ID_PAIS.toString()));
                    String existeIdBuzon = UtileriasBD.verificarExistencia(conn, BuzonSidra.N_TABLA, condiciones);
                    if (new Integer(existeIdBuzon) < 1) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_EXISTENCIA_BUZON_306, "",
                                this.getClass().toString(), metodo, "", objDatos.getCodArea());

                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BUZON, servicioPut,
                                objDatos.getIdBuzon(), Conf.LOG_TIPO_BUZON, "Problema al modificar datos de buz\u00F3n.",
                                objRespuesta.getDescripcion()));
                    } else {
                        existeBuzon = OperacionBuzon.existeBuzon(conn, objBuzon.getNombre().toUpperCase(),
                                objBuzon.getTcscbuzonid(), objBuzon.getTcscdtsid(), estadoAlta, objDatos.getNivel(), objBuzon.getTcscbodegavirtualid(), ID_PAIS);
                        log.trace("existebuzon: " + existeBuzon);

                        if (existeBuzon.intValue() == 0) {
                            if (objDatos.getEstado().trim().equalsIgnoreCase(estadoBaja)) {
                                existeUsuarioBuzon = OperacionBuzon.existeUsuarioBuzon(conn, objBuzon.getTcscbuzonid(),
                                        estadoAlta, ID_PAIS);
                            }
                            if (existeUsuarioBuzon == null || existeUsuarioBuzon.intValue() == 0) {
                                log.trace("actualiza buzon");
                                int idBuzon = OperacionBuzon.updateBuzon(conn, objBuzon);
                                objReturn.setIdBuzon(idBuzon + "");
                                if (idBuzon < 1) {
                                    // no se modifico el buzon
                                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null, null, null, "", objDatos.getCodArea());
                                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BUZON, servicioPut,
                                            objDatos.getIdBuzon(), Conf.LOG_TIPO_BUZON,
                                            "Se modificaron datos del buz\u00F3n con ID " + objDatos.getIdBuzon() + " y nombre "
                                                    + objBuzon.getNombre().toUpperCase() + ".",
                                            ""));
                                } else {
                                    // se modifico el buzon correctamente
                                    objRespuesta = getMensaje(Conf_Mensajes.OK_BUZON_UPDATE11, null, null, null, "", objDatos.getCodArea());
                                    listaLog.add(
                                            ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BUZON, servicioPut,
                                                    objDatos.getIdBuzon(), Conf.LOG_TIPO_BUZON,
                                                    "Se modificaron datos del buz\u00F3n con ID " + objDatos.getIdBuzon()
                                                            + " y nombre " + objBuzon.getNombre().toUpperCase() + ".",
                                                    ""));
                                }
                            } else {
                                objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIENEUSUARIOS_87, "",
                                        this.getClass().toString(), metodo, "", objDatos.getCodArea());

                                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BUZON, servicioPut,
                                        objDatos.getIdBuzon(), Conf.LOG_TIPO_BUZON,
                                        "Problema al modificar datos de buz\u00F3n.", objRespuesta.getDescripcion()));
                            }
                        } else {
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOMBREBUZON_YAEXISTE_46, "",
                                    this.getClass().toString(), metodo, "", objDatos.getCodArea());

                            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BUZON, servicioPut,
                                    objDatos.getIdBuzon(), Conf.LOG_TIPO_BUZON, "Problema al modificar datos de buz\u00F3n.",
                                    objRespuesta.getDescripcion()));
                        }
                    }
                }
            } else {
                objReturn.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BUZON, servicioPut, objDatos.getIdBuzon(),
                        Conf.LOG_TIPO_BUZON, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BUZON, servicioPut, objDatos.getIdBuzon(),
                    Conf.LOG_TIPO_BUZON, "Problema al modificar buz\u00F3n.", e.getMessage()));
        } catch (Exception e) {
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BUZON, servicioPut, objDatos.getIdBuzon(),
                    Conf.LOG_TIPO_BUZON, "Problema al modificar buz\u00F3n.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            objReturn.setRespuesta(objRespuesta);
        }

        return objReturn;
    }

    /**
     * M\u00E9todo para obtener informaci\u00F3n de buzones
     * @throws SQLException 
     ***/
    public OutputBuzon getBuzon(InputBuzon objDatos) {
        listaLog = new ArrayList<LogSidra>();
        OutputBuzon respBuzon = new OutputBuzon();
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        List<InputBuzon> lstBuzon = new ArrayList<InputBuzon>();
        Respuesta objRespuesta = new Respuesta();
        String metodo = "getBuzon";
        Connection conn = null;
        COD_PAIS = objDatos.getCodArea();

        try {
            conn = getConnRegional();
            ID_PAIS=getIdPais(conn, objDatos.getCodArea());

            objRespuesta = validarDatos(conn, objDatos, 2);
            if (objRespuesta == null) {
                if (!(objDatos.getIdBuzon() == null || "".equals(objDatos.getIdBuzon().trim()))) {
                    log.trace("entra a filtro buzon");
                    lstFiltros.add(new Filtro(BuzonSidra.N_TABLA + "." + BuzonSidra.CAMPO_TCSCBUZONID, Filtro.EQ,
                            objDatos.getIdBuzon()));
                }
                
                lstFiltros.add(new Filtro(BuzonSidra.N_TABLA + "." + BuzonSidra.CAMPO_TCSCCATPAIS_ID, Filtro.EQ,
                        ID_PAIS));

                if (!(objDatos.getNombre() == null || "".equals(objDatos.getNombre().trim()))) {
                    log.trace("entra a filtro nombre");
                    lstFiltros.add(new Filtro("UPPER(" + BuzonSidra.N_TABLA + "." + BuzonSidra.CAMPO_NOMBRE + ")",
                            Filtro.LIKE, "'%" + objDatos.getNombre().toUpperCase() + "%'"));
                }

                if (!(objDatos.getEstado() == null || "".equals(objDatos.getEstado().trim()))) {
                    log.trace("entra a filtro  estado");
                    lstFiltros.add(new Filtro("UPPER(" + BuzonSidra.N_TABLA + "." + BuzonSidra.CAMPO_ESTADO + ")",
                            Filtro.EQ, "'" + objDatos.getEstado().toUpperCase() + "'"));
                }

                if (!(objDatos.getNivel() == null || "".equals(objDatos.getNivel().trim()))) {
                    log.trace("entra a filtro nivel");
                    lstFiltros.add(new Filtro(BuzonSidra.N_TABLA + "." + BuzonSidra.CAMPO_NIVEL, Filtro.EQ,
                            objDatos.getNivel()));
                }
                if ((objDatos.getNivel().equals("" + Conf.NIVEL_BUZON_DTS)||objDatos.getNivel().equals("" + Conf.NIVEL_ZONACOMERCIAL)) &&(!(objDatos.getIdDistribuidor() == null || "".equals(objDatos.getIdDistribuidor().trim())))) {
                        log.trace("entra a filtro idDistribuidor");
                        lstFiltros.add(new Filtro(BuzonSidra.N_TABLA + "." + BuzonSidra.CAMPO_TCSCDTSID, Filtro.EQ,
                                objDatos.getIdDistribuidor()));
                }

                if (!(objDatos.getTipoWF() == null || "".equals(objDatos.getTipoWF().trim()))) {
                    log.trace("entra a filtro tipoWorkFlow");
                    lstFiltros.add(new Filtro("UPPER(" + BuzonSidra.N_TABLA + "." + BuzonSidra.CAMPO_TIPO_WORKFLOW + ")",
                        Filtro.EQ, "'" + objDatos.getTipoWF().toUpperCase() + "'"));
                }
                
                if (!(objDatos.getIdBodegaVirtual() == null || "".equals(objDatos.getIdBodegaVirtual().trim()))) {
                    log.trace("entra a filtro getIdBodegaVirtual");
                    lstFiltros.add(new Filtro(BuzonSidra.N_TABLA + "." + BuzonSidra.CAMPO_TCSCBODEGAVIRTUALID, Filtro.EQ,
                            objDatos.getIdBodegaVirtual()));
                }

                if ((objDatos.getNivel().equals("" + Conf.NIVEL_BUZON_DTS))||objDatos.getNivel().equals("" + Conf.NIVEL_ZONACOMERCIAL)) {
                    log.trace("Busca buzones nivel 2-3");
                    lstBuzon = OperacionBuzon.getBuzones(conn, lstFiltros, objDatos.getNivel(), ID_PAIS);
                } else if (objDatos.getNivel().equals("" + Conf.NIVEL_BUZON_TELCA)) {
                    log.trace("Busca buzones nivel 1");
                    lstBuzon = OperacionBuzon.getBuzonesNivel1(conn, lstFiltros);
                }
                
            

                if (lstBuzon.isEmpty()) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_BUZONES_392, null,
                            this.getClass().toString(), metodo,  null, objDatos.getCodArea());
                } else {
                    respBuzon.setBuzones(lstBuzon);
                    objRespuesta = getMensaje(Conf_Mensajes.OK_GETDATOS12, null, null, null,  null, objDatos.getCodArea());
                }

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de buzones.", ""));

            } else {
                respBuzon.setRespuesta(objRespuesta);
            }
        } catch (SQLException e) {
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de buzones.", e.getMessage()));
        } catch (Exception e) {
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    "", objDatos.getCodArea());
            log.error(e.getMessage(), e);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de buzones.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            respBuzon.setRespuesta(objRespuesta);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }

        return respBuzon;
    }
}

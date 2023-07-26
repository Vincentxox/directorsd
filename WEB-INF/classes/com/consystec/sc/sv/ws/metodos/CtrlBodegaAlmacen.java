package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaDTS;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegaDTS;
import com.consystec.sc.ca.ws.util.ObjectUtils;
import com.consystec.sc.sv.ws.operaciones.OperacionBodegaAlmacen;
import com.consystec.sc.sv.ws.orm.AlmacenBod;
import com.consystec.sc.sv.ws.orm.ConfiguracionFolioDTS;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class CtrlBodegaAlmacen extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlBodegaAlmacen.class);
    private static String servicioGet = Conf.LOG_GET_BODEGA_ALMACEN;
    private static String servicioPost = Conf.LOG_POST_BODEGA_ALMACEN;
    private static String servicioPut = Conf.LOG_PUT_BODEGA_ALMACEN;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al servicio.
     * 
     * @param input
     * @param metodo
     * @return Respuesta
     */
    public Respuesta validarInput(InputBodegaDTS input, int metodo) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase,
                nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
            if (input.getDistribuidor() == null || "".equals(input.getDistribuidor()) ) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_161, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if(!isNumeric(input.getDistribuidor())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
            if (input.getBodegaSCL() == null || "".equals(input.getBodegaSCL())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_BODEGASCL_281, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } /*else if(!isNumeric(input.getBodegaSCL())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_BODEGASCL_NUM_282, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }*/
            
            if (input.getNombre() == null || "".equals(input.getNombre().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NOMBRE_222, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
            
			if (ObjectUtils.isEmpty(input.getBodegaVirtualId())) {
				r = getMensaje(Conf_Mensajes.MSG_ERROR_BODEGAVIRTUALID_REQUERIDO, null, nombreClase, nombreMetodo, null,
						input.getCodArea());
				datosErroneos = datosErroneos.concat(r.getDescripcion());
				flag = true;
			}
			if (!isNumeric(input.getBodegaVirtualId())) {
				r = getMensaje(Conf_Mensajes.MSG_ERROR_BODEGAVIRTUALID_INVALIDO, null, nombreClase, nombreMetodo, null,
						input.getCodArea());
				datosErroneos = datosErroneos.concat(r.getDescripcion());
				flag = true;
			}
        }
        
        if (metodo==Conf.METODO_PUT || metodo==Conf.METODO_DELETE){
            log.debug("Validando datos para edici\u00F3n en m\u00E9todos PUT.");

            if (input.getIdBodega() == null) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_170, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if(!isNumeric(input.getIdBodega())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_NUM_171, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
        }
        
        if (metodo==Conf.METODO_PUT && (input.getEstado() == null || "".equals(input.getEstado().trim()))) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_134, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
        }
        
        if (flag) {
            r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase,
                nombreMetodo, datosErroneos, input.getCodArea());
        } else {
            r.setDescripcion("OK");
            r.setCodResultado("1");
            r.setMostrar("0");
        }
        return r;
    }
    
    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET 
     * y en este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @param metodo
     * @return
     */
    public static String[] obtenerCamposGetPost(int metodo) {
        if (metodo == Conf.METODO_GET) {
            String campos[] = {
                AlmacenBod.CAMPO_TCSCALMACENBODID,
                AlmacenBod.CAMPO_TC_SC_DTS_ID,
                AlmacenBod.CAMPO_TC_BODEGA_SCL_ID,
                AlmacenBod.CAMPO_NOMBRE,
                AlmacenBod.CAMPO_ESTADO,
                AlmacenBod.CAMPO_CREADO_EL,
                AlmacenBod.CAMPO_CREADO_POR,
                AlmacenBod.CAMPO_MODIFICADO_EL,
                AlmacenBod.CAMPO_MODIFICADO_POR,
                AlmacenBod.CAMPO_TC_SC_CATPAIS_ID,
                AlmacenBod.CAMPO_TCSCBODEGAVIRTUALID
            };
            return campos;
        } else {
            String campos[] = {
                AlmacenBod.CAMPO_TCSCALMACENBODID,
                AlmacenBod.CAMPO_TC_SC_DTS_ID,
                AlmacenBod.CAMPO_TC_BODEGA_SCL_ID,
                AlmacenBod.CAMPO_NOMBRE,
                AlmacenBod.CAMPO_CREADO_EL,
                AlmacenBod.CAMPO_CREADO_POR,
                AlmacenBod.CAMPO_TC_SC_CATPAIS_ID,
                AlmacenBod.CAMPO_TCSCBODEGAVIRTUALID
            };
            return campos;
        }
    }
    
    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET 
     * y en este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST
     * de la tabla relacionada.
     * 
     * @param metodo
     * @return
     */
    public static String[] obtenerCamposTablaHija(int metodo) {
        if (metodo == Conf.METODO_GET) {
            String campos[] = {
                ConfiguracionFolioDTS.CAMPO_TC_SC_ALMACENBODID,
                ConfiguracionFolioDTS.CAMPO_TIPODOCUMENTO,
                ConfiguracionFolioDTS.CAMPO_SERIE,
                ConfiguracionFolioDTS.CAMPO_NO_INICIAL_FOLIO,
                ConfiguracionFolioDTS.CAMPO_NO_FINAL_FOLIO,
                ConfiguracionFolioDTS.CAMPO_ESTADO,
                ConfiguracionFolioDTS.CAMPO_CREADO_EL,
                ConfiguracionFolioDTS.CAMPO_CREADO_POR,
                ConfiguracionFolioDTS.CAMPO_MODIFICADO_EL,
                ConfiguracionFolioDTS.CAMPO_MODIFICADO_POR
            };
            return campos;
        } else {
            String campos[] = {
                ConfiguracionFolioDTS.CAMPO_TC_SC_FOLIO_BODEGA_ID,
                ConfiguracionFolioDTS.CAMPO_TC_SC_ALMACENBODID,
                ConfiguracionFolioDTS.CAMPO_TIPODOCUMENTO,
                ConfiguracionFolioDTS.CAMPO_SERIE,
                ConfiguracionFolioDTS.CAMPO_NO_INICIAL_FOLIO,
                ConfiguracionFolioDTS.CAMPO_NO_FINAL_FOLIO,
                ConfiguracionFolioDTS.CAMPO_CREADO_EL,
                ConfiguracionFolioDTS.CAMPO_CREADO_POR
            };
            return campos;
        }
    }
    
    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * 
     * @param input
     * @param sequencia
     * @return inserts
     */
    public static List<String> obtenerInsertsPostPadre( InputBodegaDTS input, String sequencia, BigDecimal idPais) {
        List<String> inserts = new ArrayList<>();
        String valores = "";
        
        valores = "("
            + "(extractvalue(dbms_xmlgen.getxmltype('select " + sequencia + " from dual'),'//text()')), "
            + "" + input.getDistribuidor() + ", "
            + "" + input.getBodegaSCL() + ", "
            + "UPPER('" + input.getNombre() + "'), "
            + "sysdate, "
            + "'" + input.getUsuario() + "', " 
            + " " + idPais + ", " 
            + " " + input.getBodegaVirtualId() + " " 
        + ") ";
        
        inserts.add(valores);
        
        return inserts;
    }
    

    /**
     * Funci\u00F3n que indica los campos que se utilizar\u00E9n para modificaciones  
     * al recurso en los m\u00E9todos PUT y DELETE.
     * 
     * @param input
     * @param metodo
     * @return campos
     * @throws SQLException 
     */
    public static String[][] obtenerCamposPutDelPadre(InputBodegaDTS input, int metodo) throws SQLException {
      //Los valores que sean tipo texto deben ir entre ap\u00F3strofres o comillas simples.
        if (metodo == Conf.METODO_DELETE) {
            String campos[][] = {
                { AlmacenBod.CAMPO_ESTADO, "'" + UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea()) + "'" },
                { AlmacenBod.CAMPO_MODIFICADO_EL, "sysdate" },
                { AlmacenBod.CAMPO_MODIFICADO_POR, "'" + input.getUsuario() + "'" }
            };
            return campos;
        } else {
            String campos[][] = {
                { AlmacenBod.CAMPO_TC_BODEGA_SCL_ID, "'" + input.getBodegaSCL() + "'" },
                { AlmacenBod.CAMPO_NOMBRE, "UPPER('" + input.getNombre() + "')" },
                { AlmacenBod.CAMPO_ESTADO, "UPPER('" + (input.getEstado() == null
                    ? UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea()) : input.getEstado()) + "')" },
                { AlmacenBod.CAMPO_MODIFICADO_EL, "sysdate" },
                { AlmacenBod.CAMPO_MODIFICADO_POR, "'" + input.getUsuario() + "'" }
            };
            return campos;
        }
    }
    
    /**
     * Funci\u00F3n que indica los campos que se utilizar\u00E9n para modificaciones  
     * al recurso relacionado en los m\u00E9todos PUT y DELETE.
     * @param metodo 
     * @param input 
     * @return campos
     * @throws SQLException 
     */
    public static String[][] obtenerCamposPutDelHijo(InputBodegaDTS input) throws SQLException {
        //Los valores que sean tipo texto deben ir entre ap\u00F3strofres o comillas simples.
        String campos[][] = {
            { ConfiguracionFolioDTS.CAMPO_ESTADO, "'" + UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea()) + "'" },
            { ConfiguracionFolioDTS.CAMPO_MODIFICADO_EL, "sysdate" },
            { ConfiguracionFolioDTS.CAMPO_MODIFICADO_POR, "'" + input.getUsuario() + "'" }
        };
        return campos;
    }
    
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param input
     * @param metodo
     * @return condiciones
     */
	public static List<Filtro> obtenerCondicionesPadre(InputBodegaDTS input, int metodo, BigDecimal idPais) {
		List<Filtro> condiciones = new ArrayList<Filtro>();
		if ((metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE)
				&& (input.getIdBodega() != null && !"".equals(input.getIdBodega()))) {
			condiciones.add(new Filtro(AlmacenBod.CAMPO_TCSCALMACENBODID, Filtro.EQ, input.getIdBodega()));
		}
		if (metodo == Conf.METODO_GET) {
			if (input.getIdBodega() != null && !"".equals(input.getIdBodega())) {
				condiciones.add(new Filtro(AlmacenBod.CAMPO_TCSCALMACENBODID, Filtro.EQ, input.getIdBodega()));
			}
			if (input.getBodegaSCL() != null && !"".equals(input.getBodegaSCL())) {
				condiciones.add(new Filtro(AlmacenBod.CAMPO_TC_BODEGA_SCL_ID, Filtro.EQ, input.getBodegaSCL()));
			}
			if (input.getNombre() != null && !"".equals(input.getNombre())) {
				condiciones.add(new Filtro(AlmacenBod.CAMPO_NOMBRE, Filtro.EQ, "UPPER('" + input.getNombre() + "')"));
			}
			if (input.getDistribuidor() != null && !"".equals(input.getDistribuidor())) {
				condiciones.add(new Filtro(AlmacenBod.CAMPO_TC_SC_DTS_ID, Filtro.EQ, input.getDistribuidor()));
			}
			if (input.getEstado() != null && !"".equals(input.getEstado())) {
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, AlmacenBod.CAMPO_ESTADO,
						input.getEstado()));
			}
			condiciones.add(new Filtro(AlmacenBod.CAMPO_TC_SC_CATPAIS_ID, Filtro.EQ, idPais)); // Filtrar siempre por
																								// pais
			if (ObjectUtils.nonEmpty(input.getBodegaVirtualId())) {
				condiciones.add(new Filtro(AlmacenBod.CAMPO_TCSCBODEGAVIRTUALID, Filtro.EQ, input.getBodegaVirtualId())); 
			}
		}

		return condiciones;
	}
    
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje para la tabla relacionada.
     * 
     * @param idPadre
     * @param input
     * @param metodo
     * @return
     */
    public static List<Filtro> obtenerCondicionesTablaHija(String idPadre, int metodo) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_DELETE) {
            condiciones.add(new Filtro(Filtro.AND, ConfiguracionFolioDTS.CAMPO_TC_SC_ALMACENBODID, Filtro.EQ, idPadre));
        }
        
        return condiciones;
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las consultas de verificaci\u00F3n
     * de existencia del recurso.
     * 
     * @param input
     * @return condiciones
     * @throws SQLException 
     */
    public static List<Filtro> obtenerCondicionesExistencia(Connection conn, InputBodegaDTS input, int metodo, BigDecimal idPais) throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE) {
        	if (ObjectUtils.isEmpty(input.getIdBodega())) {
        		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AlmacenBod.CAMPO_TCSCBODEGAVIRTUALID, input.getBodegaVirtualId()));
        	} else {
        		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AlmacenBod.CAMPO_TCSCALMACENBODID, input.getIdBodega()));
        	}
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AlmacenBod.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
        } else {
            if (!"".equals(input.getDistribuidor())) {
                condiciones.add(new Filtro(Filtro.AND, AlmacenBod.CAMPO_TC_SC_DTS_ID, Filtro.EQ, input.getDistribuidor()));
            }

            if (!"".equals(input.getBodegaSCL())) {
                condiciones.add(new Filtro(Filtro.AND, AlmacenBod.CAMPO_TC_BODEGA_SCL_ID, Filtro.EQ, input.getBodegaSCL()));
            }

            if (!"".equals(input.getNombre())) {
                condiciones.add(new Filtro(Filtro.AND, AlmacenBod.CAMPO_NOMBRE, Filtro.EQ, "UPPER('" + input.getNombre() + "')"));
            }

            if (!"".equals(input.getEstado())) {
                condiciones.add(UtileriasJava.getCondicionDefault(Conf.FILTRO_ESTADO, AlmacenBod.CAMPO_ESTADO, conn, input.getCodArea()));
            }
        }

        return condiciones;
    }
    


    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     * @param metodo
     * @return
     */
    public OutputBodegaDTS getDatos(InputBodegaDTS input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        OutputBodegaDTS output = null;
        Respuesta r = null;
 
        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            ID_PAIS = getIdPais(conn, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            r = validarInput(input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equalsIgnoreCase(r.getDescripcion())) {
                output = new OutputBodegaDTS();
                output.setRespuesta(r);
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BODEGA_ALMACEN, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));
                
                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionBodegaAlmacen.doGet(conn, input, metodo, ID_PAIS);
                    
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de bodegas almac\u00E9n.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputBodegaDTS();
                    output.setRespuesta(r);
                    
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de bodegas almac\u00E9n.", e.getMessage()));
                }
            
            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
            } else if (metodo == Conf.METODO_POST) {
                try {
                    output = OperacionBodegaAlmacen.doPost(conn, input, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_BODEGA_VIRTUAL_29) {

                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BODEGA_ALMACEN, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO,
                                "Se cre\u00F3 nueva bodega almac\u00E9n de nombre " + input.getNombre().toUpperCase() + ".", ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BODEGA_ALMACEN, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema al crear bodega almac\u00E9n.",
                                output.getRespuesta().getDescripcion()));
                    }

                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputBodegaDTS();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BODEGA_ALMACEN, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al crear bodega almac\u00E9n.", e.getMessage()));
                }
                
            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT o DELETE
            } else if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
                try {
                    output = OperacionBodegaAlmacen.doPutDel(conn, input, metodo, ID_PAIS);
                    
                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_BODEGA_VIRTUAL_30) {

                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BODEGA_ALMACEN, servicioPut,
                                input.getIdBodega(), Conf.LOG_TIPO_BODEGA_ALMACEN,
                                "Se modificaron datos de la bodega almac\u00E9n con ID " + input.getIdBodega() + ".", ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BODEGA_ALMACEN, servicioPut,
                                input.getIdBodega(), Conf.LOG_TIPO_BODEGA_ALMACEN,
                                "Problema al modificar datos de bodega almac\u00E9n.",
                                output.getRespuesta().getDescripcion()));
                    }
                    
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputBodegaDTS();
                    output.setRespuesta(r);
                    
                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BODEGA_ALMACEN, servicioPut, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al modificar bodega almac\u00E9n.", e.getMessage()));
                }
            }
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputBodegaDTS();
            output.setRespuesta(r);
            
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BODEGA_ALMACEN, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de bodega almac\u00E9n.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}

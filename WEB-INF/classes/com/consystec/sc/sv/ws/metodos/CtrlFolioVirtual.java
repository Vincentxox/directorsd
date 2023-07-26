package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.folio.InputFolioVirtual;
import com.consystec.sc.ca.ws.input.general.InputFolioRutaPanel;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.output.folio.OutputConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.operaciones.OperacionFolioVirtual;
import com.consystec.sc.sv.ws.orm.ConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.orm.Dispositivo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.consystec.sc.ca.ws.orm.Respuesta;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class CtrlFolioVirtual extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlFolioVirtual.class);
    private static String servicioGet = Conf.LOG_GET_FOLIOVIRTUAL;
    private static String servicioPost = Conf.LOG_POST_FOLIOVIRTUAL;
    private static String servicioPut = Conf.LOG_PUT_FOLIOVIRTUAL;
    
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al
     * servicio.
     * 
     * @param input
     * @param metodo
     * @param estadoAlta
     * @return
     * @throws SQLException
     */
    public Respuesta validarInput(Connection conn, InputFolioVirtual input, int metodo, String estadoAlta, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        int numeroFolio = 0;
        
        String dispositivo = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPO_FOLIO, Conf.TIPO_DISPOSITIVOS, input.getCodArea());

        if (metodo == Conf.METODO_GET) {

                if (input.getIdTipo() == null || "".equals(input.getIdTipo())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDTIPO_166, null, nombreClase, nombreMetodo,
                            null, input.getCodArea()).getDescripcion();
                    flag = true;
                }
    
                if (input.getTipo() == null || "".equals(input.getTipo())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_NULO_150, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                } else if (!input.getTipo().equalsIgnoreCase(dispositivo)) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_136, null, nombreClase, nombreMetodo,
                            dispositivo + ".", input.getCodArea()).getDescripcion();
                    flag = true;
                }
        } else {
            if (input.getIdTipo() == null || "".equals(input.getIdTipo())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDTIPO_166, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                List<Filtro> condiciones = new ArrayList<Filtro>();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Dispositivo.CAMPO_TCSCCATPAISID, idPais.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Dispositivo.CAMPO_CODIGO_DISPOSITIVO, input.getIdTipo()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Dispositivo.N_TABLA+"."+Dispositivo.CAMPO_ESTADO, estadoAlta));
                String existencia = UtileriasBD.verificarExistencia(conn, Dispositivo.N_TABLA, condiciones);
                if (new Integer(existencia) <= 0) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_DISPOSITIVO_239, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }

            if (input.getTipo() == null || "".equals(input.getTipo())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_NULO_150, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!input.getTipo().equalsIgnoreCase(dispositivo)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_136, null, nombreClase, nombreMetodo,
                        dispositivo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        if (metodo == Conf.METODO_DELETE) {
            if (input.getFolios().size() > 1) {
                datosErroneos = getMensaje(Conf_Mensajes.MSJ_UPD_RECURSO_UNITARIO, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }
            
            if(input.getFolios().get(0).getIdReserva() == null || "".equals(input.getFolios().get(0).getIdReserva().trim())){
            	datosErroneos += getMensaje(Conf_Mensajes.MSJ_PARM_IDRESERVA_VACIO_640, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        if (metodo == Conf.METODO_DELETE) {
            log.debug("Validando datos para dar de baja.");
            if (input.getFolios() != null && (input.getFolios().get(0).getIdFolio() == null
                    || "".equals(input.getFolios().get(0).getIdFolio())
                    || !isNumeric(input.getFolios().get(0).getIdFolio()))) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_IDRANGO_FOLIO_385, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }
        }
        
        if (metodo == Conf.METODO_POST) {
            log.debug("Validando datos para creaci\u00F3n.");

            if (input.getFolios() != null) {
                for (int i = 0; i < input.getFolios().size(); i++) {
                    if (flag == true) break;
                    String tipoDocumento = "";
                    String serie = "";
                    String noInicialFolio = "";
                    String noFinalFolio = "";
                    String idReserva = "";

                    if (input.getFolios().get(i).getTipoDocumento() != null) {
                        tipoDocumento = input.getFolios().get(i).getTipoDocumento().trim();
                    }
                    if (input.getFolios().get(i).getSerie() != null) {
                        serie = input.getFolios().get(i).getSerie().trim();
                    }
                    if (input.getFolios().get(i).getNoInicialFolio() != null) {
                        noInicialFolio = input.getFolios().get(i).getNoInicialFolio().trim();
                    }
                    if (input.getFolios().get(i).getNoFinalFolio() != null) {
                        noFinalFolio = input.getFolios().get(i).getNoFinalFolio().trim();
                    }
                    if(input.getFolios().get(i).getIdReserva() != null){
                    	idReserva = input.getFolios().get(i).getIdReserva().trim();
                    }

                    numeroFolio = i + 1;
                    if(idReserva == null || "".equals(idReserva)){
                    	datosErroneos += getMensaje(Conf_Mensajes.MSJ_PARM_IDRESERVA_VACIO_640, null, nombreClase,
                                nombreMetodo, numeroFolio + ".", input.getCodArea()).getDescripcion();
                        flag = true;
                    }
                    if (tipoDocumento == null || "".equals(tipoDocumento)) {
                        datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FOLIODET_TIPO_184, null, nombreClase,
                                nombreMetodo, numeroFolio + ".", input.getCodArea()).getDescripcion();
                        flag = true;
                    }
                    if (serie == null || "".equals(serie)) {
                        datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FOLIODET_SERIE_185, null, nombreClase,
                                nombreMetodo, numeroFolio + ".", input.getCodArea()).getDescripcion();
                        flag = true;
                    }

                    if (noInicialFolio == null || "".equals(noInicialFolio) || !isNumeric(noInicialFolio)
                            || noFinalFolio == null || noFinalFolio.equals("") || !isNumeric(noFinalFolio)) {
                        datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FOLIODET_RANGO_186, null, nombreClase,
                                nombreMetodo, numeroFolio + ".", input.getCodArea()).getDescripcion();
                        flag = true;
                    } else {
                        if (new Integer(noInicialFolio) >= new Integer(noFinalFolio)) {
                            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FOLIODET_RANGOFIN_187, null,
                                    nombreClase, nombreMetodo, numeroFolio + ".", input.getCodArea()).getDescripcion();
                            flag = true;
                        }
                        if ("503".equals(input.getCodArea())&& i > 0) {
                                for (int j = 0; j < input.getFolios().size(); j++) {
                                    if (flag == true) break;
                                    if ((tipoDocumento.toUpperCase().trim().equals(input.getFolios().get(j).getTipoDocumento().toUpperCase().trim())
                                            && i != j && i > j) &&  (new Integer(noInicialFolio) <= new Integer(input.getFolios().get(j).getNoFinalFolio().trim()))) {
                                            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FOLIODET_RANGOINICIO_188, null,
                                                    nombreClase, nombreMetodo, numeroFolio + ".", input.getCodArea()).getDescripcion();
                                            flag = true;
                                    }
                                }
                        }
                    }
                }
                
                numeroFolio = 1;
                for (InputFolioRutaPanel folioActual: input.getFolios()) {
                    if (flag ) break;
                    int indexFolio = 1;
                    
                    for (InputFolioRutaPanel folio: input.getFolios()) {
                        if (flag ) break;

                        if (indexFolio != numeroFolio
                                && folio.getTipoDocumento().toUpperCase().trim().equals(folioActual.getTipoDocumento().toUpperCase().trim())
                                && folio.getSerie().toUpperCase().trim().equals(folioActual.getSerie().toUpperCase().trim())
                                && Integer.valueOf(folio.getNoInicialFolio().trim()).equals(Integer.valueOf(folioActual.getNoInicialFolio().trim()))
                                && Integer.valueOf(folio.getNoFinalFolio().trim()).equals(Integer.valueOf(folioActual.getNoFinalFolio()))) {
                            log.error("El folio #" + indexFolio + " es igual al #" + numeroFolio);
                            datosErroneos += " Los datos del folio #" + indexFolio + " son iguales al folio #"
                                    + numeroFolio + ".";

                            flag = true;
                        }
                        indexFolio++;
                    }
                    numeroFolio++;
                }
            } else {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FOLIOS_189, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        if (flag ) {
            r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos, input.getCodArea());
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
            		ConfiguracionFolioVirtual.CAMPO_TC_SC_FOLIO_ID,
                    ConfiguracionFolioVirtual.CAMPO_ID_TIPO,
                    ConfiguracionFolioVirtual.CAMPO_TIPO,
                    ConfiguracionFolioVirtual.CAMPO_TIPODOCUMENTO,
                    ConfiguracionFolioVirtual.CAMPO_SERIE,
                    ConfiguracionFolioVirtual.CAMPO_NO_INICIAL_FOLIO,
                    ConfiguracionFolioVirtual.CAMPO_NO_FINAL_FOLIO,
                    ConfiguracionFolioVirtual.CAMPO_CANT_UTILIZADOS,
                    ConfiguracionFolioVirtual.CAMPO_ULTIMO_UTILIZADO,
                    ConfiguracionFolioVirtual.CAMPO_FOLIO_SIGUIENTE,
                    ConfiguracionFolioVirtual.CAMPO_ESTADO,
                    ConfiguracionFolioVirtual.CAMPO_CREADO_EL,
                    ConfiguracionFolioVirtual.CAMPO_CREADO_POR,
                    ConfiguracionFolioVirtual.CAMPO_MODIFICADO_EL,
                    ConfiguracionFolioVirtual.CAMPO_MODIFICADO_POR,
                    ConfiguracionFolioVirtual.CAMPO_ID_RESERVA,
                    ConfiguracionFolioVirtual.CAMPO_COD_OFICINA,
                    ConfiguracionFolioVirtual.CAMPO_COD_VENDE,
                    ConfiguracionFolioVirtual.CAMPO_RESOLUCION,
                    ConfiguracionFolioVirtual.CAMPO_FECHA_RESOLUCION
            };
            return campos;
        } else {
            String campos[] = {
                ConfiguracionFolioVirtual.CAMPO_TC_SC_FOLIO_ID,
                ConfiguracionFolioVirtual.CAMPO_TC_SC_CATPAISID,
                ConfiguracionFolioVirtual.CAMPO_ID_TIPO,
                ConfiguracionFolioVirtual.CAMPO_TIPO,
                ConfiguracionFolioVirtual.CAMPO_TIPODOCUMENTO,
                ConfiguracionFolioVirtual.CAMPO_SERIE,
                ConfiguracionFolioVirtual.CAMPO_NO_INICIAL_FOLIO,
                ConfiguracionFolioVirtual.CAMPO_NO_FINAL_FOLIO,
                ConfiguracionFolioVirtual.CAMPO_CANT_UTILIZADOS,
                ConfiguracionFolioVirtual.CAMPO_ULTIMO_UTILIZADO,
                ConfiguracionFolioVirtual.CAMPO_FOLIO_SIGUIENTE,
                ConfiguracionFolioVirtual.CAMPO_ESTADO,
                ConfiguracionFolioVirtual.CAMPO_CREADO_EL,
                ConfiguracionFolioVirtual.CAMPO_CREADO_POR,
                ConfiguracionFolioVirtual.CAMPO_ID_RESERVA,
                ConfiguracionFolioVirtual.CAMPO_COD_OFICINA,
                ConfiguracionFolioVirtual.CAMPO_COD_VENDE,
                ConfiguracionFolioVirtual.CAMPO_RESOLUCION,
                ConfiguracionFolioVirtual.CAMPO_FECHA_RESOLUCION
            };
            return campos;
        }
    }
    
    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * 
     * @param input
     * @param sequencia
     * @param estadoAlta 
     * @return inserts
     */
    public static List<String> obtenerInsertsPost(InputFolioVirtual input, String sequencia, String estadoAlta, BigDecimal idPais) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
        for (int i = 0; i < input.getFolios().size(); i++) {
            valores = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais.toString(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getIdTipo(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getTipo(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getFolios().get(i).getTipoDocumento(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getFolios().get(i).getSerie(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getFolios().get(i).getNoInicialFolio(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getFolios().get(i).getNoFinalFolio(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, "0", Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getFolios().get(i).getNoInicialFolio(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getFolios().get(i).getIdReserva(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getFolios().get(i).getCodOficina(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getFolios().get(i).getCodVendedor(), Conf.INSERT_SEPARADOR_SI)
                    + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getFolios().get(i).getResolucion(), Conf.INSERT_SEPARADOR_SI)
                   + UtileriasJava.setInsertFecha(Conf.INSERT_FECHA, input.getFolios().get(i).getFecha_resolucion() , Conf.TXT_FORMATO_FECHA_INPUT, Conf.INSERT_SEPARADOR_NO);
            inserts.add(valores);
        }
        
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
    public static String[][] obtenerCamposPutDel(InputFolioVirtual input) throws SQLException {
        String campos[][] = {
            { ConfiguracionFolioVirtual.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea())) },
            { ConfiguracionFolioVirtual.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
            { ConfiguracionFolioVirtual.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
        };
        return campos;
    }
    
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje para la tabla relacionada.
     * 
     * @param input
     * @param metodo
     * @return
     */
    public static List<Filtro> obtenerCondiciones(InputFolioVirtual input, int metodo, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ConfiguracionFolioVirtual.CAMPO_TC_SC_CATPAISID, idPais.toString()));

        if (metodo == Conf.METODO_GET) {
            if (input.getEstado() != null && !"".equals(input.getEstado())) {
                String[] estados = input.getEstado().split(",");
                for (int i = 0; i < estados.length; i++) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, ConfiguracionFolioVirtual.CAMPO_ESTADO, estados[i].trim()));
                }
            }

            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioVirtual.N_TABLA+"."+ConfiguracionFolioVirtual.CAMPO_ID_TIPO, input.getIdTipo()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioVirtual.N_TABLA+"."+ConfiguracionFolioVirtual.CAMPO_TIPO, input.getTipo()));

        }

        return condiciones;
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
    public static List<Filtro> obtenerCondicionesExistencia(Connection conn, InputFolioVirtual input, int metodo, String estadoAlta, BigDecimal idPais)
            throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        List<Filtro> condicionesEstados = new ArrayList<Filtro>();
        List<Filtro> condicionesAgrupadas = new ArrayList<Filtro>();
        String estadoEnUso = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_FOLIOS, Conf.FOLIO_EN_USO, input.getCodArea());
        condicionesAgrupadas.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ConfiguracionFolioVirtual.CAMPO_TC_SC_CATPAISID, idPais.toString()));

        if (metodo == Conf.METODO_DELETE) {
            condicionesAgrupadas.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ConfiguracionFolioVirtual.CAMPO_TC_SC_FOLIO_ID, input.getFolios().get(0).getIdFolio()));
            condicionesAgrupadas.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioVirtual.CAMPO_TIPODOCUMENTO, input.getFolios().get(0).getTipoDocumento()));
            condicionesAgrupadas.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioVirtual.CAMPO_SERIE, input.getFolios().get(0).getSerie()));
            condicionesAgrupadas.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ConfiguracionFolioVirtual.CAMPO_NO_INICIAL_FOLIO, input.getFolios().get(0).getNoInicialFolio()));
            condicionesAgrupadas.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ConfiguracionFolioVirtual.CAMPO_NO_FINAL_FOLIO, input.getFolios().get(0).getNoFinalFolio()));
        } else {
            condicionesEstados.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, ConfiguracionFolioVirtual.CAMPO_ESTADO, estadoAlta));
            condicionesEstados.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, ConfiguracionFolioVirtual.CAMPO_ESTADO, estadoEnUso));
            
            for (int i = 0; i < input.getFolios().size(); i++) {
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioVirtual.CAMPO_TIPODOCUMENTO, input.getFolios().get(i).getTipoDocumento()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioVirtual.CAMPO_SERIE, input.getFolios().get(i).getSerie()));
               
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioVirtual.CAMPO_COD_OFICINA, input.getFolios().get(i).getCodOficina()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, ConfiguracionFolioVirtual.CAMPO_ID_TIPO, input.getFolios().get(i).getIdTipo()));
                condiciones.add(new Filtro(Filtro.AND, ConfiguracionFolioVirtual.CAMPO_NO_FINAL_FOLIO, Filtro.GTE, input.getFolios().get(i).getNoInicialFolio()));
                condiciones.add(new Filtro(Filtro.AND, "", "", UtileriasBD.agruparCondiciones(condicionesEstados)));
                condicionesAgrupadas.add(new Filtro(Filtro.OR, "", "", UtileriasBD.agruparCondiciones(condiciones)));
            }
        }

        return condicionesAgrupadas;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     * @param metodo
     * @return
     */
    public OutputConfiguracionFolioVirtual getDatos(InputFolioVirtual input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        List<Respuesta> respuesta = new ArrayList<Respuesta>();
        Respuesta r = null;
        OutputConfiguracionFolioVirtual output = null;
        Connection conn = null;
        
        try {
            conn = getConnRegional();
            BigDecimal idPais = getIdPais(conn, input.getCodArea());
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            r = validarInput(conn, input, metodo, estadoAlta, idPais);

            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equals(r.getDescripcion())) {
                respuesta.add(r);
                output = new OutputConfiguracionFolioVirtual();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_FOLIO, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionFolioVirtual.doGet(conn, input, metodo, idPais);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de folios virtuales.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputConfiguracionFolioVirtual();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de folios virtuales.", e.getMessage()));
                }

                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
            } else if (metodo == Conf.METODO_POST) {
                try {
                    output = OperacionFolioVirtual.doPost(conn, input, metodo, estadoAlta, idPais);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_FOLIO_43) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_FOLIO, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Se crearon folios virtuales asociados al recurso con ID "
                                        + input.getIdTipo() + " de tipo " + input.getTipo().toUpperCase() + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_FOLIO, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema al crear folio virtual.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputConfiguracionFolioVirtual();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_FOLIO, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al crear folio virtual.", e.getMessage()));
                }

                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT o DELETE
            } else if (metodo == Conf.METODO_DELETE) {
                try {
                    output = OperacionFolioVirtual.doPutDel(conn, input, metodo, estadoAlta, idPais);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_FOLIO_44) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_FOLIO, servicioPut, "0",
                                Conf.LOG_TIPO_FOLIO,
                                "Se dio de baja el folio virtual de tipo " + input.getFolios().get(0).getTipoDocumento()
                                        + ", de serie " + input.getFolios().get(0).getSerie().toUpperCase()
                                        + " y rango " + input.getFolios().get(0).getNoInicialFolio() + "-"
                                        + input.getFolios().get(0).getNoFinalFolio() + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_FOLIO, servicioPut, "0",
                                Conf.LOG_TIPO_FOLIO, "Problema al modificar datos de folio virtual.",
                                output.getRespuesta().getDescripcion()));
                        
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputConfiguracionFolioVirtual();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_FOLIO, servicioPut, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al modificar datos de folio virtual.", e.getMessage()));
                }
            }

            
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputConfiguracionFolioVirtual();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_DISTRIBUIDOR, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de folios virtuales.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}

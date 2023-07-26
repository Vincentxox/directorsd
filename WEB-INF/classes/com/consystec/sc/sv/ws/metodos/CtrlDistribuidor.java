package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.dts.InputDistribuidor;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.dts.OutputDistribuidor;
import com.consystec.sc.sv.ws.operaciones.OperacionDistribuidor;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class CtrlDistribuidor extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlDistribuidor.class);
    private static String servicioGet = Conf.LOG_GET_DISTRIBUIDOR;
    private static String servicioPost = Conf.LOG_POST_DISTRIBUIDOR;
    private static String servicioPut = Conf.LOG_PUT_DISTRIBUIDOR;

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al servicio.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @param estadoAlta
     * @param estadoBaja
     * @param estadoAltaBool
     * @param estadoBajaBool
     * @param campoNumConvenio
     * @return
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputDistribuidor input, int metodo, String estadoAlta,
            String estadoBaja, String estadoAltaBool, String estadoBajaBool, String campoNumConvenio) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;

        log.debug("Validando datos...");

        if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
            log.trace("ID del Distribuidor: " + input.getIdDTS());
            if (input.getIdDTS() == null || "".equals(input.getIdDTS())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_161, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdDTS())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }
        }
        
        if (metodo == Conf.METODO_POST) {
            if (input.getCodCuenta() == null || "".equals(input.getCodCuenta().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_CUENTA_614, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }
            if (input.getCodCliente() == null || "".equals(input.getCodCliente().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_CLIENTE_607, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
            if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }
            
            if (input.getTipo() == null || "".equals(input.getTipo().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_NULO_150, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (input.getIdBodegaVirtual() == null || "".equals(input.getIdBodegaVirtual().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_170, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdBodegaVirtual())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_NUM_171, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (input.getNombres() == null || "".equals(input.getNombres().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NOMBRES_156, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }
            
            if (input.getNumero() == null || "".equals(input.getNumero().trim()) || !isNumeric(input.getNumero())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NUMERO_264, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                if ("507".equals(input.getCodArea())) {
                    if (input.getNumero().length() < Conf.LONG_NUMERO - 1 || input.getNumero().length() > Conf.LONG_NUMERO) {
                        datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_LONG_NUMERO_310, null, nombreClase,
                                nombreMetodo, Conf.LONG_NUMERO - 1 + " o " + Conf.LONG_NUMERO + ".", input.getCodArea()).getDescripcion();
                        flag = true;
                    }
                } else {
                    if (input.getNumero().length() != Conf.LONG_NUMERO) {
                        datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_LONG_NUMERO_310, null, nombreClase,
                                nombreMetodo, Conf.LONG_NUMERO + ".", input.getCodArea()).getDescripcion();
                        flag = true;
                    }
                }
            }

            if (input.getEmail() == null || "".equals(input.getEmail().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_EMAIL_159, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            }

    
            if (input.getAdministrador() == null || "".equals(input.getAdministrador().trim())
                    || !isNumeric(input.getAdministrador())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ADMIN_NULO_340, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (input.getCanal() == null || "".equals(input.getCanal().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_CANAL_DTS_828, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                List<Filtro> condiciones = new ArrayList<Filtro>();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.GRUPO_CANAL_DTS));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_VALOR, input.getCanal()));

                if (UtileriasBD.selectCount(conn, Catalogo.N_TABLA, condiciones) < 1) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_CANAL_830, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                }
            }

            if ("1".equals(campoNumConvenio)&&  (input.getNumConvenio() == null || "".equals(input.getNumConvenio().trim()))) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NUMCONVENIO_829, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }
        }

        if (metodo == Conf.METODO_PUT) {
            if (input.getEstado() == null || "".equals(input.getEstado().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_134, null, nombreClase, nombreMetodo,
                        null, input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!input.getEstado().equalsIgnoreCase(estadoBaja)
                    && !input.getEstado().equalsIgnoreCase(estadoAlta)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_TIPO_135, null, nombreClase,
                        nombreMetodo, estadoAlta + " o " + estadoBaja + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }
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
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET y en
     * este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @param metodo
     * @return
     */
    public static String[] obtenerCamposPost() {
        String campos[] = {
                Distribuidor.CAMPO_TC_SC_DTS_ID,
                Distribuidor.CAMPO_TCSCCATPAISID,
                Distribuidor.CAMPO_TCSCBODEGAVIRTUALID,
                Distribuidor.CAMPO_TIPO,
                Distribuidor.CAMPO_NOMBRES,
                Distribuidor.CAMPO_NUMERO,
                Distribuidor.CAMPO_EMAIL,
                Distribuidor.CAMPO_ADMINISTRADOR,
                Distribuidor.CAMPO_CANAL,
                Distribuidor.CAMPO_COD_CUENTA,
                Distribuidor.CAMPO_COD_CLIENTE,
                Distribuidor.CAMPO_ESTADO,
                Distribuidor.CAMPO_CREADO_EL,
                Distribuidor.CAMPO_CREADO_POR
        };
        return campos;
    }

    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * 
     * @param conn
     * 
     * @param input
     * @param sequencia
     * @return inserts
     */
    public static List<String> obtenerInsertsPost(InputDistribuidor input, String sequencia, String estadoAlta, BigDecimal idPais) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";

        valores = "("
                + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais.toString(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdBodegaVirtual(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getTipo(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getNombres(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getNumero(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getEmail(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getAdministrador(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getCanal(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getCodCuenta(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getCodCliente(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
        + ") ";
        inserts.add(valores);

        return inserts;
    }

    /**
     * Funci\u00F3n que indica los campos que se utilizar\u00E9n para modificaciones al
     * recurso en los m\u00E9todos PUT y DELETE.
     * 
     * @param input
     * @param metodo
     * @return campos
     */
    public static String[][] obtenerCamposPutDel(InputDistribuidor input, int metodo, String estadoBaja) {
        if (metodo == Conf.METODO_DELETE) {
            String campos[][] = {
                    { Distribuidor.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, estadoBaja) },
                    { Distribuidor.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                    { Distribuidor.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };
            return campos;
        } else {
            String campos[][] = {
                    { Distribuidor.CAMPO_TCSCBODEGAVIRTUALID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getIdBodegaVirtual()) },
                    { Distribuidor.CAMPO_TIPO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getTipo()) },
                    { Distribuidor.CAMPO_NOMBRES, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getNombres()) },
                    { Distribuidor.CAMPO_NUMERO, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getNumero()) },
                    { Distribuidor.CAMPO_EMAIL, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getEmail()) },
                    { Distribuidor.CAMPO_ADMINISTRADOR, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getAdministrador()) },
                    { Distribuidor.CAMPO_CANAL, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getCanal()) },
                    { Distribuidor.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getEstado()) },
                    { Distribuidor.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                    { Distribuidor.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };
            return campos;
        }
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes
     * consultas seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param input
     * @param metodo
     * @return condiciones
     */
    public static List<Filtro> obtenerCondiciones(InputDistribuidor input, int metodo, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if ((metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE) &&!"".equals(input.getIdDTS())) {
                condiciones.add(new Filtro(Filtro.AND, Distribuidor.CAMPO_TC_SC_DTS_ID, Filtro.EQ, input.getIdDTS()));
        }

        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.N_TABLA,
                Distribuidor.CAMPO_TCSCCATPAISID, idPais.toString()));

        if (metodo == Conf.METODO_GET) {
            if (input.getIdDTS() != null && !"".equals(input.getIdDTS())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.N_TABLA,
                        Distribuidor.CAMPO_TC_SC_DTS_ID, input.getIdDTS()));
            }

            if (input.getTipo() != null && !"".equals(input.getTipo())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.N_TABLA,
                        Distribuidor.CAMPO_TIPO, input.getTipo()));
            }

            if (input.getIdBodegaVirtual() != null && !"".equals(input.getIdBodegaVirtual())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.N_TABLA,
                        Distribuidor.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodegaVirtual()));
            }

            if (input.getNombres() != null && !"".equals(input.getNombres())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_LIKE_AND, Distribuidor.N_TABLA,
                        Distribuidor.CAMPO_NOMBRES, input.getNombres()));
            }

            if (input.getNumero() != null && !"".equals(input.getNumero())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_LIKE_AND, Distribuidor.N_TABLA,
                        Distribuidor.CAMPO_NUMERO, input.getNumero()));
            }

            if (input.getEmail() != null && !"".equals(input.getEmail())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.N_TABLA,
                        Distribuidor.CAMPO_EMAIL, input.getEmail()));
            }

           

            if (input.getEstado() != null && !"".equals(input.getEstado())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.N_TABLA,
                        Distribuidor.CAMPO_ESTADO, input.getEstado()));
            }

            if (input.getAdministrador() != null && !"".equals(input.getAdministrador())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.N_TABLA,
                        Distribuidor.CAMPO_ADMINISTRADOR, input.getAdministrador()));
            }

            if (input.getCanal() != null && !"".equals(input.getCanal())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.N_TABLA,
                        Distribuidor.CAMPO_CANAL, input.getCanal()));
            }

  
            if (input.getCodCliente() != null && !"".equals(input.getCodCliente())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.N_TABLA,
                        Distribuidor.CAMPO_COD_CLIENTE, input.getCodCliente()));
            }

            if (input.getCodCuenta() != null && !"".equals(input.getCodCuenta())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.N_TABLA,
                        Distribuidor.CAMPO_COD_CUENTA, input.getCodCuenta()));
            }

            if (input.getResultadoSCL() != null && !"".equals(input.getResultadoSCL())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.N_TABLA,
                        Distribuidor.CAMPO_RESULTADO_SCL, input.getResultadoSCL()));
            }
        }

        return condiciones;
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las consultas de
     * verificaci\u00F3n de existencia del recurso.
     * 
     * @param input
     * @param metodo
     * @return condiciones
     */
    public static List<Filtro> obtenerCondicionesExistencia(InputDistribuidor input, int metodo, String estadoAlta, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TCSCCATPAISID, idPais.toString()));

        if (metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TC_SC_DTS_ID, input.getIdDTS()));
        }

        if (metodo != Conf.METODO_PUT) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.CAMPO_ESTADO, estadoAlta));
        }

        return condiciones;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     * @param metodo
     * @return respuestaDistribuidor
     */
    public OutputDistribuidor getDatos(InputDistribuidor input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<Respuesta> respuesta = new ArrayList<Respuesta>();
        OutputDistribuidor output = null;
        Respuesta r = new Respuesta();

        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());

            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
            String estadoBaja = "";
            String estadoAltaBool = "";
            String estadoBajaBool = "";
            String campoNumConvenio = "";

            String campos[] = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };

            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, ID_PAIS.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_CAMPOS_DTS));

            List<Map<String, String>> datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);

            for (int i = 0; i < datosConfig.size(); i++) {
                if (Conf.ESTADO_BAJA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoBaja = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.ESTADO_ALTA_BOOL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoAltaBool = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.ESTADO_BAJA_BOOL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoBajaBool = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CAMPO_NUM_CONVENIO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    campoNumConvenio = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
            }

            // Validaci\u00F3n de datos en el input
            r = validarInput(conn, input, metodo, estadoAlta, estadoBaja, estadoAltaBool, estadoBajaBool, campoNumConvenio);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equals(r.getDescripcion())) {
                respuesta.add(r);
                output = new OutputDistribuidor();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_DISTRIBUIDOR, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionDistribuidor.doGet(conn, input, metodo, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de distribuidores.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputDistribuidor();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de distribuidores.", e.getMessage()));
                }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
            } else if (metodo == Conf.METODO_POST) {
                try {
                    output = OperacionDistribuidor.doPost(conn, input, estadoAlta, metodo, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_DTS_41) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_DISTRIBUIDOR, servicioPost,
                                output.getIdDTS(), Conf.LOG_TIPO_DISTRIBUIDOR, "Se creó distribuidor nuevo con ID "
                                        + output.getIdDTS() + " y nombre " + input.getNombres().toUpperCase() + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_DISTRIBUIDOR, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema al crear distribuidor.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputDistribuidor();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_DISTRIBUIDOR, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al crear distribuidor.", e.getMessage()));
                }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT o DELETE
            } else if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
                try {
                    output = OperacionDistribuidor.doPutDel(conn, input, metodo, estadoAlta, estadoBaja, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_DTS_42) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_DISTRIBUIDOR, servicioPut,
                                input.getIdDTS(), Conf.LOG_TIPO_DISTRIBUIDOR,
                                "Se modificaron datos del distribuidor ID " + input.getIdDTS() + ".", ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_DISTRIBUIDOR, servicioPut,
                                input.getIdDTS(), Conf.LOG_TIPO_DISTRIBUIDOR,
                                "Problema al modificar datos de distribuidor.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputDistribuidor();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_DISTRIBUIDOR, servicioPut, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al modificar distribuidor.", e.getMessage()));
                }
            }

        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputDistribuidor();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_DISTRIBUIDOR, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de distribuidores.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}

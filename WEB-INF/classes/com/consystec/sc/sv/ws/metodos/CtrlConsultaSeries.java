package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.inventario.InputConsultaSeries;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventario.OutputConsultaSeries;
import com.consystec.sc.sv.ws.operaciones.OperacionConsultaSeries;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.Inventario;
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
public class CtrlConsultaSeries extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlConsultaSeries.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_DATOS_SERIES;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al servicio.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return Respuesta
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputConsultaSeries input, int metodo) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;

        log.debug("Validando datos...");

        String campos[] = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };

        // Se obtienen todas las configuraciones.
        String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_SOLICITUDES_TIPOINV));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_CONFIG_SIDRA));

        List<Map<String, String>> datosConfig = new ArrayList<Map<String, String>>();
        try {
            datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);
        } catch (SQLException e) {
            log.error("Error al obtener par\u00E9metros de configuraci\u00F3n. " + e);
            r = getMensaje(Conf_Mensajes.MSJ_ERROR_GET_CONFIG_103, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        String invTelca = "";
        String invSidra = "";
        String registrosMaximos = "";

        for (int i = 0; i < datosConfig.size(); i++) {
            if (Conf.SOL_TIPOINV_TELCA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                invTelca = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.SOL_TIPOINV_SIDRA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                invSidra = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.REGISTROS_MAXIMOS.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                registrosMaximos = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
        }

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (input.getIdBodega() == null || "".equals(input.getIdBodega().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_170, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        } else if (!isNumeric(input.getIdBodega())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_NUM_171, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (input.getIdArticulo() == null || "".equals(input.getIdArticulo().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDARTICULO_130, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        } else if (!isNumeric(input.getIdArticulo())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDARTICULO_NUM_131, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (input.getSerie() == null || "".equals(input.getSerie())) {
            if (input.getTipoInv() == null || "".equals(input.getTipoInv().trim())
                    || (!input.getTipoInv().equalsIgnoreCase(invTelca)
                            && !input.getTipoInv().equalsIgnoreCase(invSidra))) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPOINV_197, null, nombreClase, nombreMetodo,
                        invTelca + " o " + invSidra + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (input.getMin() == null || "".equals(input.getMin().trim()) || !isNumeric(input.getMin())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MINIMO_198, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (input.getMax() == null || "".equals(input.getMax().trim()) || !isNumeric(input.getMax())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MAXIMO_217, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (input.getMin() != null && !"".equals(input.getMin()) && input.getMax() != null
                    && ! "".equals(input.getMax()) && !flag ) {
                if (new Integer(input.getMin()) > new Integer(input.getMax())) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MINIMO_MAYOR_218, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
                } else if ((new Integer(input.getMax()) - new Integer(input.getMin())) > new Integer(
                        registrosMaximos)) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_RANGO_MAX_219, null, nombreClase, nombreMetodo,
                            registrosMaximos + ".", input.getCodArea());
                    datosErroneos += r.getDescripcion();
                    flag = true;
                }
            }

            if (input.getEstado() == null || "".equals(input.getEstado().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_134, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
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
     * @param mostrarDetalle 
     * @param estadoAltaBool 
     * @return
     */
    public static String[] obtenerCamposGetPost() {
        String campos[] = {
            Inventario.N_TABLA_ID,
            Inventario.CAMPO_ARTICULO,
            Inventario.CAMPO_SERIE,
            Inventario.CAMPO_ICC,
            Inventario.CAMPO_IMEI,
            Inventario.CAMPO_DESCRIPCION,
            Inventario.CAMPO_SERIE_ASOCIADA,
            Inventario.CAMPO_SERIADO,
            Inventario.CAMPO_TIPO_INV,
            "(SELECT PRECIO FROM TC_SC_PRECIO_ARTICULO P WHERE P.TCSCCATPAISID = TC_SC_INVENTARIO.TCSCCATPAISID"
                    + " AND P.ARTICULO = TC_SC_INVENTARIO.ARTICULO AND P.TIPO_GESTION='VENTA' AND P.ESTADO='ALTA' AND ROWNUM=1) AS PRECIO"
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
    public static List<Filtro> obtenerCondiciones(InputConsultaSeries input, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodega()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_ARTICULO, input.getIdArticulo()));

        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));

        if (input.getSerie() != null && ! "".equals(input.getSerie())) {
          	if(input.getSerie().length()==19){
      		  condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_AND, Inventario.CAMPO_ICC, input.getSerie()));
	      	}
	      	else if(input.getSerie().length()==15){
	      		  condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_AND, Inventario.CAMPO_IMEI, input.getSerie()));
	      	}else{
	      		  condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_AND, Inventario.CAMPO_SERIE, input.getSerie()));	
	      	}
      	
        } else {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, "UPPER(" + Inventario.CAMPO_ESTADO + ")", input.getEstado().toUpperCase()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NOT_NULL_AND, Inventario.CAMPO_SERIE, null));
        }

        return condiciones;
    }
   
    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo 
     * @return output Respuesta y listado con los Descuentos encontrados.
     */
    public OutputConsultaSeries getDatos(InputConsultaSeries input, int metodo) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta r = new Respuesta();
        OutputConsultaSeries output = null;
        COD_PAIS = input.getCodArea();

        log.trace("Usuario: " + input.getUsuario());
        Connection conn = null;
        try {
            conn = getConnRegional();
            ID_PAIS = getIdPais(conn, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            r = validarInput(conn, input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equalsIgnoreCase(r.getDescripcion())) {
                output = new OutputConsultaSeries();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionConsultaSeries.doGet(conn, input, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO,
                            "Se consultaron datos de series del art\u00EDculo ID " + input.getIdArticulo() + ".", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputConsultaSeries();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos del pa\u00EDs.", e.getMessage()));
                }
            }
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputConsultaSeries();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de consulta datos de pa\u00EDs.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}

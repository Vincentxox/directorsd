package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.inventariomovil.InputConsultaInventarioMovil;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventariomovil.OutputInventarioMovil;
import com.consystec.sc.sv.ws.operaciones.OperacionInventarioMovil;
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
 * @author Victor Cifuentes - Consystec - 2016
 *
 */
public class CtrlInventarioMovil extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlInventarioMovil.class);
    private static String servicioGet = Conf.LOG_GET_INVENTARIO_MOVIL;
    
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al servicio.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param tipoRuta 
     * @param tipoPanel 
     * @param invTelca 
     * @param invSidra 
     * @return Respuesta
     */
    public Respuesta validarInput(Connection conn, InputConsultaInventarioMovil input, String invSidra, String invTelca,
            String tipoPanel, String tipoRuta) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        
        log.debug("Validando datos...");
        
        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase,
                nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if(input.getIdBodega() == null || "".equals(input.getIdBodega())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_170, null, nombreClase,
                nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        } else {
            if(!isNumeric(input.getIdBodega())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODVIRTUAL_NUM_171, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
        }
        
        if(input.getIdArticulo() != null && !isNumeric(input.getIdArticulo())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDARTICULO_NUM_131, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
        }
        
        if ((input.getTipoInv() != null || !"".equals(input.getTipoInv().trim()))&&  (!input.getTipoInv().equalsIgnoreCase(invTelca)
                    && !input.getTipoInv().equalsIgnoreCase(invSidra)
                    && !"".equals(input.getTipoInv().trim()))) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPOINV_197, null, nombreClase,
                    nombreMetodo, invTelca + " o " + invSidra + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
        }
        
        if(input.getEstado() == null ||"".equals(input.getEstado().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_134, null, nombreClase,
                nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if(input.getIdTipo() == null || "".equals(input.getIdTipo())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDTIPO_166, null, nombreClase,
                nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        } else {
            if(!isNumeric(input.getIdTipo())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDTIPO_NUM_167, null, nombreClase,
                    nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
        }
        
        if (input.getTipo() == null || "".equals(input.getTipo())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_NULO_150, null, nombreClase,
                nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        } else if (!input.getTipo().equalsIgnoreCase(tipoPanel) && !input.getTipo().equalsIgnoreCase(tipoRuta)) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_136, null, nombreClase, nombreMetodo,
                    tipoPanel + " o " + tipoRuta + ".", input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        if (flag ) {
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
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param input
     * @return condiciones
     */
    public static List<Filtro> obtenerCondiciones( InputConsultaInventarioMovil input, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (input.getIdBodega() != null && !"".equals(input.getIdBodega())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Inventario.N_TABLA,
                    Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodega()));
        }

        if (input.getIdArticulo() != null && !"".equals(input.getIdArticulo())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Inventario.N_TABLA,
                    Inventario.CAMPO_ARTICULO, input.getIdArticulo()));
        }

        if (input.getSerie() != null && !"".equals(input.getSerie())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_AND, Inventario.N_TABLA,
                    Inventario.CAMPO_SERIE, input.getSerie()));
        }
        
        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Inventario.N_TABLA,
                Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));

        if (input.getTipoInv() != null && !"".equals(input.getTipoInv())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_AND, Inventario.N_TABLA,
                    Inventario.CAMPO_TIPO_INV, input.getTipoInv()));
        }

        if (input.getTipoGrupo() != null && !"".equals(input.getTipoGrupo())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.N_TABLA,
                    Inventario.CAMPO_TIPO_GRUPO_SIDRA, input.getTipoGrupo()));
        }

        if (input.getEstado() != null && !"".equals(input.getEstado())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Inventario.N_TABLA,
                    Inventario.CAMPO_ESTADO, input.getEstado()));
        }

        if (input.getSeriado() != null && !"".equals(input.getSeriado())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Inventario.N_TABLA,
                    Inventario.CAMPO_SERIADO, input.getSeriado()));
        }

        if (input.getIdVendedor() != null && !"".equals(input.getIdVendedor())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Inventario.N_TABLA,
                    Inventario.CAMPO_IDVENDEDOR, input.getIdVendedor()));
        }

        if (input.getTecnologia() != null && !"".equals(input.getTecnologia())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_IN_UPPER_AND, Inventario.N_TABLA,
                    Inventario.CAMPO_TECNOLOGIA, input.getTecnologia()));
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
    public OutputInventarioMovil getDatos(InputConsultaInventarioMovil input, int metodo) {
    	List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<Respuesta> respuesta = new ArrayList<Respuesta>();

        Respuesta r = new Respuesta();
        OutputInventarioMovil output = null;

        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            BigDecimal idPais = getIdPais(conn, input.getCodArea());

            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

            String campos[] = {
                Catalogo.CAMPO_NOMBRE,
                Catalogo.CAMPO_VALOR
            };

            // Se obtienen todas las configuraciones.
            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_SOLICITUDES_TIPOINV));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_PANELRUTA));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_CONDICION_TIPO));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_CONDICION_TIPOOFERTA));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_OFERTA_COMERCIAL));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));

            List<Map<String, String>> datosConfig = new ArrayList<Map<String, String>>();
            try {
                datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);
            } catch (SQLException e) {
                log.error("Error al obtener par\u00E9metros de configuraci\u00F3n. " + e);
                r = getMensaje(Conf_Mensajes.MSJ_ERROR_GET_CONFIG_103, null, nombreClase, nombreMetodo, null, input.getCodArea());
                output = new OutputInventarioMovil();
                output.setRespuesta(r);
                return output;
            }

            String invTelca = "";
            String invSidra = "";
            String tipoPanel = "";
            String tipoRuta = "";
            String tipoOfertaArticulo = "";
            String tipoCondicionArticulo = "";
            String tipoCondicionTecnologia = "";
            String tipoOferta = "";

            for (int i = 0; i < datosConfig.size(); i++) {
                if (Conf.SOL_TIPOINV_TELCA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    invTelca = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.SOL_TIPOINV_SIDRA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    invSidra = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_PANEL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoPanel = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_RUTA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoRuta = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CONDICION_OFERTA_ARTICULO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoOfertaArticulo = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_CONDICION_ARTICULO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoCondicionArticulo = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_CONDICION_TECNOLOGIA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoCondicionTecnologia = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_OFERTA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoOferta = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
            }

            // Validaci\u00F3n de datos en el input
            r = validarInput(conn, input, invSidra, invTelca, tipoPanel, tipoRuta);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equals(r.getDescripcion())) {
                respuesta.add(r);
                output = new OutputInventarioMovil();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionInventarioMovil.doGet(conn, input, invSidra, invTelca, tipoRuta,
                            estadoAlta, tipoOfertaArticulo, tipoCondicionArticulo, tipoOferta, tipoCondicionTecnologia, idPais);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos del inventario m\u00F3vil.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputInventarioMovil();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos del inventario m\u00F3vil.",
                            e.getMessage()));
                }
            }
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputInventarioMovil();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio consulta de inventario m\u00F3vil.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}

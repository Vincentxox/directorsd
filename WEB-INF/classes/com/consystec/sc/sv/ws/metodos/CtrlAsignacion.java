package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.asignacion.InputArticuloAsignacion;
import com.consystec.sc.ca.ws.input.asignacion.InputAsignacion;
import com.consystec.sc.ca.ws.input.asignacion.RespuestaAsignacion;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.asignacion.OutputAsignacion;
import com.consystec.sc.sv.ws.operaciones.OperacionAsignacion;
import com.consystec.sc.sv.ws.orm.ArticulosSidra;
import com.consystec.sc.sv.ws.orm.AsignacionReserva;
import com.consystec.sc.sv.ws.orm.BodegaVendedor;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.Promocionales;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.orm.AsignacionReservaDet;
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
public class CtrlAsignacion extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlAsignacion.class);
    private static String nombreServicio = "";
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
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
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputAsignacion input, int metodo) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        String respArticulos = "";

        log.debug("Validando datos...");

        String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
        String asignacion = "";
        String reserva = "";
        String estadoFinalizada = "";
        String estadoCancelada = "";

        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, ID_PAIS.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));

        String campos[] = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };

        List<Map<String, String>> datosConfig ;

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (metodo != Conf.METODO_PUT) {
            // Se obtienen las configuraciones.
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ASIGNACIONES_TIPO));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS));
            datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);

            for (int i = 0; i < datosConfig.size(); i++) {
                if (Conf.AR_TIPO_ASIGNACION.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    asignacion = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.AR_TIPO_RESERVA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    reserva = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
            }
        } else {
            // Se obtienen todas configuraciones.
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ASIGNACIONES_ESTADOS));
            datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);
    
            for (int i = 0; i < datosConfig.size(); i++) {
                if (Conf.AR_ESTADO_FINALIZADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoFinalizada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.AR_ESTADO_CANCELADA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    estadoCancelada = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
            }

            if (input.getIdAsignacionReserva() == null || "".equals(input.getIdAsignacionReserva().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDASIGRES_132, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdAsignacionReserva())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDASIGRES_NUM_133, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (input.getEstado() == null ||  "".equals(input.getEstado().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_134, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!input.getEstado().equalsIgnoreCase(estadoFinalizada) && !input.getEstado().equalsIgnoreCase(estadoCancelada)) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_TIPO_135, null, nombreClase, nombreMetodo,
                        estadoFinalizada + " o " + estadoCancelada + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
        }

        if (metodo == Conf.METODO_GET) {
            if (input.getIdAsignacionReserva() != null && !isNumeric(input.getIdAsignacionReserva())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDASIGRES_NUM_133, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }

            if ((input.getTipo() != null && ! "".equals(input.getTipo())) &&(!input.getTipo().equalsIgnoreCase(asignacion) && !input.getTipo().equalsIgnoreCase(reserva))) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_136, null, nombreClase, nombreMetodo,
                            asignacion + " o " + reserva + ".", input.getCodArea()).getDescripcion();
                    flag = true;
            }

            if (input.getIdDTS() != null && !isNumeric(input.getIdDTS())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }

            if (input.getIdVendedor() != null && !isNumeric(input.getIdVendedor())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }

            if (input.getIdBodegaOrigen() != null && !isNumeric(input.getIdBodegaOrigen())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODORIGEN_NUM_140, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }

            if (input.getIdBodegaDestino() != null && !isNumeric(input.getIdBodegaDestino())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODDESTINO_NUM_142, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
            }

            if (input.getFechaInicio() != null && !"".equals(input.getFechaInicio().trim())
                    && input.getFechaFin() != null && ! "".equals(input.getFechaFin().trim())) {
                SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
                Date fechaInicio = UtileriasJava.parseDate(input.getFechaInicio(), formatoFecha);
                Date fechaFin = UtileriasJava.parseDate(input.getFechaFin(), formatoFecha);

                if ((fechaInicio != null && fechaFin != null)&& (fechaInicio.compareTo(fechaFin) > 0) ){
                        datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_INICIOFIN_143, null,
                                nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                        flag = true;
                }
            }
        }

        if (metodo == Conf.METODO_POST) {
            if (input.getTipo() == null || "".equals(input.getTipo().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_NULO_150, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!input.getTipo().equalsIgnoreCase(asignacion) && !input.getTipo().equalsIgnoreCase(reserva)) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_136, null, nombreClase, nombreMetodo,
                        asignacion + " o " + reserva + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (input.getIdVendedor() == null || "".equals(input.getIdVendedor().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_137, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdVendedor())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
            
            if (input.getIdBodegaOrigen() == null || "".equals(input.getIdBodegaOrigen().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODORIGEN_139, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdBodegaOrigen())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODORIGEN_NUM_140, null, nombreClase, nombreMetodo,
                        null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else {
                // Validaci\u00F3n Origen
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getIdBodegaOrigen()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVirtual.CAMPO_ESTADO, estadoAlta));

                String existencia = UtileriasBD.verificarExistencia(conn, getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);
                if (new Integer(existencia) <= 0) {
                    log.error("No existe la bodega Origen.");
                    r = getMensaje(Conf_Mensajes.MSJ_NO_BODEGA_ORIGEN_110, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    datosErroneos += " " + r.getDescripcion();
                    flag = true;
                }
                // Validaci\u00F3n Origen
            }

            if (input.getIdBodegaDestino() == null || "".equals(input.getIdBodegaDestino().trim())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODDESTINO_141, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(input.getIdBodegaDestino())) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODDESTINO_NUM_142, null, nombreClase, nombreMetodo,
                        null, input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else {
                // Validaci\u00F3n Destino
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVendedor.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodegaDestino()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVendedor.CAMPO_VENDEDOR, input.getIdVendedor()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVendedor.CAMPO_ESTADO, estadoAlta));
                String existencia = UtileriasBD.verificarExistencia(conn, BodegaVendedor.N_TABLA, condiciones);
                if (new Integer(existencia) <= 0) {
                    log.error("La bodega Destino enviada no es apta para la asignaci\u00F3n.");
                    r = getMensaje(Conf_Mensajes.MSJ_NO_BODEGA_DESTINO_102, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    datosErroneos += " " + r.getDescripcion();
                    flag = true;
                }
                // Validaci\u00F3n Destino
            }

            if (input.getTipo().equalsIgnoreCase(asignacion)) {
            	//SPRINT 2, SIDRA SCRUM, SE SOLICITA QUE LA ASIGNACION PUEDA HACERSE SIN TENER JORNADA FINALIZADA
            	//RV 29-08-2018
                // no se puede asignar si se tiene jornada iniciada
//                if (OperacionAsignacion.verificarJornadaIniciada(conn, input.getIdVendedor(), input.getIdBodegaDestino(),input.getCodArea(), ID_PAIS) > 0) {
//                    return getMensaje(Conf_Mensajes.MSJ_ERROR_JORNADA_ASIGNACION_864, null, nombreClase, nombreMetodo, null, input.getCodArea());
//                }

                // no se puede asignar si existe jornadas sin liquidar
//                if (OperacionAsignacion.verificarJornadasLiquidadas(conn, input.getIdVendedor(), input.getIdBodegaDestino(),input.getCodArea(), ID_PAIS) > 0) {
//                    return getMensaje(Conf_Mensajes.MSJ_ERROR_JORNADAS_NO_LIQUIDADAS_794, null, nombreClase, nombreMetodo, null, input.getCodArea());
//                }
            }

            InputAsignacion inputSeriado = new InputAsignacion();
            InputAsignacion inputNoSeriado = new InputAsignacion();

            List<InputArticuloAsignacion> articulosSeriados = new ArrayList<InputArticuloAsignacion>();
            List<InputArticuloAsignacion> articulosNoSeriados = new ArrayList<InputArticuloAsignacion>();

            for (int i = 0; i < input.getArticulos().size(); i++) {
                if (input.getArticulos().get(i).getSerie() != null
                        && !"".equals(input.getArticulos().get(i).getSerie())) {
                    articulosSeriados.add(input.getArticulos().get(i));
                } else {
                    articulosNoSeriados.add(input.getArticulos().get(i));
                }
                inputSeriado.setArticulos(articulosSeriados);
                inputNoSeriado.setArticulos(articulosNoSeriados);
            }

            respArticulos = validaSeriados(inputSeriado);
            if (!"".equals(respArticulos)) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_LISTADO_SERIADO_144, null, nombreClase, nombreMetodo, null, input.getCodArea());
                datosErroneos += r.getDescripcion() + respArticulos;
                flag = true;
            }
            if (!flag) {
                respArticulos = validaNoSeriados(inputNoSeriado);
                if (!"".equals(respArticulos)) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_LISTADO_NO_SERIADO_145, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());
                    datosErroneos += r.getDescripcion() + respArticulos;
                    flag = true;
                }
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

    public Respuesta validarInput(Connection conn, InputAsignacion input, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        String respArticulos = "";

        log.debug("Validando datos...");

        if (input.getUsuario() == null ||"".equals( input.getUsuario().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }

        if (input.getIdAsignacionReserva() == null || "".equals(input.getIdAsignacionReserva().trim())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDASIGRES_132, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        } else if (!isNumeric(input.getIdAsignacionReserva())) {
            r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDASIGRES_NUM_133, null, nombreClase, nombreMetodo, null, input.getCodArea());
            datosErroneos += r.getDescripcion();
            flag = true;
        }
        
        List<Filtro> condiciones = CtrlAsignacion.obtenerCondiciones(input, Conf.METODO_PUT, idPais);
        String tipo = UtileriasBD.getOneRecord(conn, AsignacionReserva.CAMPO_TIPO,
                getParticion(AsignacionReserva.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);
        String tipoAsignacion = UtileriasJava.getConfig(conn, Conf.GRUPO_ASIGNACIONES_TIPO, Conf.AR_TIPO_ASIGNACION, input.getCodArea());

        log.trace("verifica que lleve art\u00EDculos");
        boolean esCancelacion = false;
        if (input.getArticulos() == null || (input.getArticulos().get(0).getSerie() == null
                && input.getArticulos().get(0).getSerieFinal() == null
                && input.getArticulos().get(0).getIdArticulo() == null
                && input.getArticulos().get(0).getCantidad() == null)) {
            esCancelacion = true;
        }
        log.trace("finaliza verificaci\u00F3n de art\u00EDculos");
        
        if (tipo.equalsIgnoreCase(tipoAsignacion) && esCancelacion) {
            // no se verifica porque se cancela la asignacion
            log.trace("es cancelacion");
        } else {
            // se verifican todos los articulos para realizar los movimientos del inventario
            if (input.getArticulos() == null || input.getArticulos().isEmpty()){
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULOS_370, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                InputAsignacion inputSeriado = new InputAsignacion();
                InputAsignacion inputNoSeriado = new InputAsignacion();

                List<InputArticuloAsignacion> articulosSeriados = new ArrayList<InputArticuloAsignacion>();
                List<InputArticuloAsignacion> articulosNoSeriados = new ArrayList<InputArticuloAsignacion>();

                for (int i = 0; i < input.getArticulos().size(); i++) {
                    if (input.getArticulos().get(i).getSerie() != null
                            && !input.getArticulos().get(i).getSerie().equals("")) {
                        articulosSeriados.add(input.getArticulos().get(i));
                    } else {
                        articulosNoSeriados.add(input.getArticulos().get(i));
                    }
                    inputSeriado.setArticulos(articulosSeriados);
                    inputNoSeriado.setArticulos(articulosNoSeriados);
                }

                respArticulos = validaSeriados(inputSeriado);
                if (!"".equals(respArticulos)) {
                    r = getMensaje(Conf_Mensajes.MSJ_INPUT_LISTADO_SERIADO_144, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    datosErroneos += r.getDescripcion() + respArticulos;
                    flag = true;
                }
                if (!flag ) {
                    respArticulos = validaNoSeriados(inputNoSeriado);
                    if (!"".equals(respArticulos)) {
                        r = getMensaje(Conf_Mensajes.MSJ_INPUT_LISTADO_NO_SERIADO_145, null, nombreClase, nombreMetodo,
                                null, input.getCodArea());
                        datosErroneos += r.getDescripcion() + respArticulos;
                        flag = true;
                    }
                }
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
    
    private String validaNoSeriados(InputAsignacion input) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta r ;
        String datosErroneos = "";
        boolean flag = false;
        int numeroArticulo = 0;

        for (int i = 0; i < input.getArticulos().size(); i++) {
            if (flag){
                break;
            }
            String idArticulo = null;
            String cant = null;
            String tipoInv = null;

            if (input.getArticulos().get(i).getIdArticulo() != null) {
                idArticulo = input.getArticulos().get(i).getIdArticulo().trim();
            }
            if (input.getArticulos().get(i).getCantidad() != null) {
                cant = input.getArticulos().get(i).getCantidad().trim();
            }
            if (input.getArticulos().get(i).getTipoInv() != null) {
                tipoInv = input.getArticulos().get(i).getTipoInv().trim();
            }

            numeroArticulo = i + 1;

            if (idArticulo == null || "".equals(idArticulo)) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_IDART_146, null, nombreClase, nombreMetodo,
                        numeroArticulo + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(idArticulo)) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_IDART_NUM_147, null, nombreClase, nombreMetodo,
                        numeroArticulo + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (cant == null || "".equals(cant)) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT_148, null, nombreClase, nombreMetodo,
                        numeroArticulo + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (!isNumeric(cant)) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT_NUM_149, null, nombreClase, nombreMetodo,
                        numeroArticulo + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            } else if (new Integer(cant) <= 0) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT0_151, null, nombreClase, nombreMetodo,
                        numeroArticulo + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (tipoInv == null || "".equals(tipoInv)) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_TIPOINV_152, null, nombreClase, nombreMetodo,
                        numeroArticulo + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
        }

        numeroArticulo = 1;
        for (InputArticuloAsignacion detActual : input.getArticulos()) {
            if (flag)
                break;
            int indexArt = 1;

            for (InputArticuloAsignacion detalle : input.getArticulos()) {
                if (flag)
                    break;

                if (indexArt != numeroArticulo
                        && detalle.getIdArticulo().toUpperCase().trim()
                                .equals(detActual.getIdArticulo().toUpperCase().trim())
                        && detalle.getTipoInv().toUpperCase().trim()
                                .equals(detActual.getTipoInv().toUpperCase().trim())) {
                    log.error("El Art\u00EDculo #" + indexArt + " es igual al #" + numeroArticulo);
                    datosErroneos += " Los datos del Art\u00EDculo #" + indexArt + " son iguales al Art\u00EDculo #"
                            + numeroArticulo + ", ID de art\u00EDculo " + detActual.getIdArticulo() + ".";

                    flag = true;
                }
                indexArt++;
            }
            numeroArticulo++;
        }

        return datosErroneos;
    }

    private String validaSeriados(InputAsignacion input) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta r;
        String datosErroneos = "";
        boolean flag = false;
        int numeroArticulo = 0;

        for (int i = 0; i < input.getArticulos().size(); i++) {
            if (flag )
                break;
            String serie = null;
            String serieFinal = null;
            String tipoInv = null;

            if (input.getArticulos().get(i).getSerie() != null) {
                serie = input.getArticulos().get(i).getSerie().trim();
            }
            if (input.getArticulos().get(i).getSerieFinal() != null) {
                serieFinal = input.getArticulos().get(i).getSerieFinal().trim();
            }
            if (input.getArticulos().get(i).getTipoInv() != null) {
                tipoInv = input.getArticulos().get(i).getTipoInv().trim();
            }
            numeroArticulo = i + 1;

            if (serie == null || "".equals(serie)) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_SERIE_153, null, nombreClase, nombreMetodo,
                        numeroArticulo + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }

            if (serieFinal != null && !"".equals(serieFinal)) {
                if (!isNumeric(serie) || !isNumeric(serieFinal)) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_RANGO_SERIES_NUM_363, null, nombreClase,
                            nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                    flag = true;
                } else if (new BigInteger(serie).compareTo(new BigInteger(serieFinal)) > 0) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_RANGO_SERIES_INV_364, null, nombreClase,
                            nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }

            if (tipoInv == null || "".equals(tipoInv)) {
                r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_TIPOINV_152, null, nombreClase, nombreMetodo,
                        numeroArticulo + ".", input.getCodArea());
                datosErroneos += r.getDescripcion();
                flag = true;
            }
        }

        numeroArticulo = 1;
        for (InputArticuloAsignacion detActual : input.getArticulos()) {
            if (flag)
                break;
            int indexArt = 1;

            for (InputArticuloAsignacion detalle : input.getArticulos()) {
                if (flag)
                    break;

                if (indexArt != numeroArticulo && (detalle.getSerie().toUpperCase().trim()
                        .equals(detActual.getSerie().toUpperCase().trim()))) {
                    log.error(" Los Art\u00EDculos #" + indexArt + " y #" + numeroArticulo + " tienen la misma serie.");
                    datosErroneos += " Los Art\u00EDculos #" + indexArt + " y #" + numeroArticulo
                            + " tienen la misma serie: " + detActual.getSerie() + ".";

                    flag = true;
                }
                indexArt++;
            }
            numeroArticulo++;
        }

        return datosErroneos;
    }

    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET y en
     * este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @param metodo
     *            Valor que indica tipo de operaci\u00F3n que se desea realizar en el
     *            servicio.
     * @return campos Array con los nombres de los campos.
     */
    public static String[] obtenerCamposGetPost(int metodo, BigDecimal idPais) {
        if (metodo == Conf.METODO_GET) {
            String campos[] = {
                    AsignacionReserva.CAMPO_TCSCASIGNACIONRESERVAID,
                    AsignacionReserva.CAMPO_TIPO,
                    AsignacionReserva.CAMPO_IDVENDEDOR,
                    AsignacionReserva.CAMPO_BODEGA_ORIGEN,
                    "(SELECT NOMBRE FROM TC_SC_BODEGAVIRTUAL B WHERE B.TCSCBODEGAVIRTUALID = "
                            + AsignacionReserva.CAMPO_BODEGA_ORIGEN + " AND B.TCSCCATPAISID = " + idPais + ") AS NOM_BOD_ORIGEN",
                    AsignacionReserva.CAMPO_BODEGA_DESTINO,
                    "(SELECT NOMBRE FROM TC_SC_BODEGAVIRTUAL B WHERE B.TCSCBODEGAVIRTUALID = "
                            + AsignacionReserva.CAMPO_BODEGA_DESTINO + " AND B.TCSCCATPAISID = " + idPais + ") AS NOM_BOD_DESTINO",
                    AsignacionReserva.CAMPO_OBSERVACIONES,
                    AsignacionReserva.CAMPO_ESTADO,
                    AsignacionReserva.CAMPO_CREADO_EL,
                    AsignacionReserva.CAMPO_CREADO_POR,
                    AsignacionReserva.CAMPO_MODIFICADO_EL,
                    AsignacionReserva.CAMPO_MODIFICADO_POR
            };
            return campos;
        } else {
            String campos[] = {
                    AsignacionReserva.CAMPO_TCSCASIGNACIONRESERVAID,
                    AsignacionReserva.CAMPO_TCSCCATPAISID,
                    AsignacionReserva.CAMPO_TIPO,
                    AsignacionReserva.CAMPO_IDVENDEDOR,
                    AsignacionReserva.CAMPO_BODEGA_ORIGEN,
                    AsignacionReserva.CAMPO_BODEGA_DESTINO,
                    AsignacionReserva.CAMPO_OBSERVACIONES,
                    AsignacionReserva.CAMPO_ESTADO,
                    AsignacionReserva.CAMPO_CREADO_EL,
                    AsignacionReserva.CAMPO_CREADO_POR
            };
            return campos;
        }
    }

    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET y en
     * este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST de la
     * tabla relacionada.
     * 
     * @param metodo
     * @return
     */
    public static String[] obtenerCamposTablaHija(int metodo) {
        if (metodo == Conf.METODO_GET) {
            String campos[] = { AsignacionReservaDet.CAMPO_TCSCASIGNACIONRESERVADETID,
                    AsignacionReservaDet.CAMPO_TCSCASIGNACIONID, AsignacionReservaDet.CAMPO_ARTICULO,
                    AsignacionReservaDet.CAMPO_DESCRIPCION, AsignacionReservaDet.CAMPO_SERIE,
                    AsignacionReservaDet.CAMPO_SERIE_FINAL, AsignacionReservaDet.CAMPO_SERIE_ASOCIADA,
                    "(select icc from tc_sc_inventario where   serie = TC_SC_ASIGNACION_DET.SERIE   AND tcscasignacionreservaid = TC_SC_ASIGNACION_DET.tcscasignacionreservaid) icc",
                    "(select imei from tc_sc_inventario where   serie = TC_SC_ASIGNACION_DET.SERIE  AND tcscasignacionreservaid = TC_SC_ASIGNACION_DET.tcscasignacionreservaid) imei",
                    AsignacionReservaDet.CAMPO_CANTIDAD, AsignacionReservaDet.CAMPO_TIPO_INV,
                    AsignacionReservaDet.CAMPO_OBSEVACIONES, AsignacionReservaDet.CAMPO_ESTADO,
                    AsignacionReservaDet.CAMPO_CREADO_EL, AsignacionReservaDet.CAMPO_CREADO_POR,
                    AsignacionReservaDet.CAMPO_MODIFICADO_EL, AsignacionReservaDet.CAMPO_MODIFICADO_POR };
            return campos;
        } else {
            String campos[] = { AsignacionReservaDet.CAMPO_TCSCASIGNACIONRESERVADETID,
                    AsignacionReservaDet.CAMPO_TCSCASIGNACIONID, AsignacionReservaDet.CAMPO_ARTICULO,
                    AsignacionReservaDet.CAMPO_DESCRIPCION, AsignacionReservaDet.CAMPO_SERIE,
                    AsignacionReservaDet.CAMPO_SERIE_FINAL, AsignacionReservaDet.CAMPO_SERIE_ASOCIADA,
                    AsignacionReservaDet.CAMPO_CANTIDAD, AsignacionReservaDet.CAMPO_TIPO_INV,
                    AsignacionReservaDet.CAMPO_ESTADO, AsignacionReservaDet.CAMPO_CREADO_EL,
                    AsignacionReservaDet.CAMPO_CREADO_POR };
            return campos;
        }
    }

    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * 
     * @param conn
     * @param input
     *            Objeto con los datos enviados mediante POST.
     * @param sequencia
     *            Nombre de la secuencia a utilizar para la inserci\u00F3n.
     * @param tipoAsignacion2
     * @return inserts Listado de cadenas con los valores a insertar.
     * @throws SQLException
     */
    public static List<String> obtenerInsertsPost(Connection conn, InputAsignacion input, String sequencia,
            String tipoAsignacion, BigDecimal idPais) throws SQLException {
        List<String> inserts = new ArrayList<String>();
        String estado = "";

        if (input.getTipo().equalsIgnoreCase(tipoAsignacion)) {
            estado = UtileriasJava.getConfig(conn, Conf.GRUPO_ASIGNACIONES_ESTADOS, Conf.AR_ESTADO_FINALIZADA, input.getCodArea());
        } else {
            estado = UtileriasJava.getConfig(conn, Conf.GRUPO_ASIGNACIONES_ESTADOS, Conf.AR_ESTADO_ALTA, input.getCodArea());
        }

        String valores = "";
        valores = "("
                + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais.toString(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getTipo(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdVendedor(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdBodegaOrigen(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdBodegaDestino(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getObservaciones(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estado, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
        + ") ";

        inserts.add(valores);

        return inserts;
    }

    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST de la
     * tabla relacionada.
     * 
     * @param idPadre
     * @param input
     * @param sequencia
     * @param estado
     * @return
     * @throws SQLException
     */
    public static List<String> obtenerInsertsPostHijo( int idPadre, InputAsignacion input, int i,
            String sequencia, String estado, BigDecimal idPais) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";

        String serie = "";
        if (input.getArticulos().get(i).getSerie() != null && !"".equals(input.getArticulos().get(i).getSerie())) {
            serie = UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getArticulos().get(i).getSerie(),
                    Conf.INSERT_SEPARADOR_SI);
        } else {
            serie = null + ", ";
        }

        String idArticulo = null;
        if (input.getArticulos().get(i).getIdArticulo() != null && !"".equals(input.getArticulos().get(i).getIdArticulo())) {
            idArticulo = input.getArticulos().get(i).getIdArticulo();
        } else if (input.getArticulos().get(i).getSerie() != null && !"".equals(input.getArticulos().get(i).getSerie())) {
            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_SERIE, input.getArticulos().get(i).getSerie()));
            idArticulo = "(" + UtileriasBD.armarQuerySelectField(Inventario.N_TABLA, Inventario.CAMPO_ARTICULO, condiciones, null) + ")";
        }

        String serieAsociada = "";
        if (input.getArticulos().get(i).getSerie() != null && !"".equals(input.getArticulos().get(i).getSerie())
                && input.getArticulos().get(i).getSerieAsociada() != null
                && !input.getArticulos().get(i).getSerieAsociada().equals("")) {
            serieAsociada = UtileriasJava.setInsert(Conf.INSERT_TEXTO,
                    input.getArticulos().get(i).getSerieAsociada(), Conf.INSERT_SEPARADOR_SI);
        } else {
            serieAsociada = null + ", ";
        }

        String cantidad = null;
        if ((input.getArticulos().get(i).getSerie() == null || "".equals(input.getArticulos().get(i).getSerie()))
                && input.getArticulos().get(i).getCantidad() != null
                && !"".equals(input.getArticulos().get(i).getCantidad())) {
            cantidad = input.getArticulos().get(i).getCantidad();
        } else {
            cantidad = "1";
        }

        String serieFinal = "";
        if (input.getArticulos().get(i).getSerie() != null && !"".equals(input.getArticulos().get(i).getSerie())
                && input.getArticulos().get(i).getSerieFinal() != null
                && !"".equals(input.getArticulos().get(i).getSerieFinal())) {
            serieFinal = UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getArticulos().get(i).getSerieFinal(),
                    Conf.INSERT_SEPARADOR_SI);

            cantidad = new BigInteger(input.getArticulos().get(i).getSerieFinal())
                    .subtract(new BigInteger(input.getArticulos().get(i).getSerie())).add(BigInteger.ONE) + "";

            serieAsociada = null + ", ";
        } else {
            serieFinal = null + ", ";
        }

        String descripcion = "";
        if (input.getArticulos().get(i).getTipoInv().equals(Conf.SOL_TIPOINV_SIDRA)) {
            descripcion = "(SELECT " + Promocionales.CAMPO_DESCRIPCION + " FROM " + Promocionales.N_TABLA + " WHERE "
                    + Promocionales.CAMPO_TCSCARTPROMOCIONALID + " = " + idArticulo + " AND TCSCCATPAISID = " + idPais + "), ";
        } else {
            descripcion = "(SELECT " + ArticulosSidra.CAMPO_DESCRIPCION + " FROM " + ArticulosSidra.N_TABLA + " WHERE "
                    + ArticulosSidra.CAMPO_ARTICULO + " = " + idArticulo + " AND TCSCCATPAISID = " + idPais + "), ";
        }

        valores = UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPadre + "", Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idArticulo, Conf.INSERT_SEPARADOR_SI)
                + descripcion
                + serie
                + serieFinal
                + serieAsociada
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, cantidad, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getArticulos().get(i).getTipoInv(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, estado, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO);
        inserts.add(valores);

        return inserts;
    }

    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET y en
     * este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @param metodo
     *            Valor que indica tipo de operaci\u00F3n que se desea realizar en el
     *            servicio.
     * @return campos Array con los nombres de los campos.
     */
    public static String[][] obtenerCamposPutDel(InputAsignacion input, int idPadre) {
        String campos[][] = {
                { Inventario.CAMPO_TCSCBODEGAVIRTUALID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getIdBodegaDestino()) },
                { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) },
                { Inventario.CAMPO_IDVENDEDOR, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getIdVendedor()) },
                { Inventario.CAMPO_TCSCASIGNACIONRESERVAID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, idPadre + "") }
        };

        return campos;
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes
     * consultas seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param input
     *            Objeto con los datos enviados mediante POST.
     * @param metodo
     *            Valor que indica tipo de operaci\u00F3n que se desea realizar en el
     *            servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo
     *         deseado.
     */
    public static List<Filtro> obtenerCondiciones(InputAsignacion input, int metodo, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_PUT) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                    AsignacionReserva.CAMPO_TCSCASIGNACIONRESERVAID, input.getIdAsignacionReserva()));
        }
        
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, AsignacionReserva.CAMPO_TCSCCATPAISID, idPais.toString()));

        if (metodo == Conf.METODO_GET) {
            if (input.getIdAsignacionReserva() != null && !"".equals(input.getIdAsignacionReserva())) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                            AsignacionReserva.CAMPO_TCSCASIGNACIONRESERVAID, input.getIdAsignacionReserva()));
            }
            if (input.getTipo() != null && !"".equals(input.getTipo())) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
                            AsignacionReserva.CAMPO_TIPO, input.getTipo()));
            }
            if (input.getIdDTS() != null && !"".equals(input.getIdDTS())) {
                List<Filtro> condicionesIn = new ArrayList<Filtro>();
                condicionesIn.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCDTSID, input.getIdDTS()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, VendedorDTS.CAMPO_TCSCCATPAISID, idPais.toString()));

                String selectVendedores = UtileriasBD.armarQuerySelectField(
                        getParticion(VendedorDTS.N_TABLA, Conf.PARTITION, input.getIdDTS(), input.getCodArea()), VendedorDTS.CAMPO_VENDEDOR,
                        condicionesIn, null);

                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, AsignacionReserva.CAMPO_IDVENDEDOR,
                        selectVendedores));
            }
            if (input.getIdVendedor() != null && !"".equals(input.getIdVendedor())) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                            AsignacionReserva.CAMPO_IDVENDEDOR, input.getIdVendedor()));
            }
            if (input.getIdBodegaOrigen() != null && !"".equals(input.getIdBodegaOrigen())) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                            AsignacionReserva.CAMPO_BODEGA_ORIGEN, input.getIdBodegaOrigen()));
            }
            if (input.getIdBodegaDestino() != null && !"".equals(input.getIdBodegaDestino())) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                            AsignacionReserva.CAMPO_BODEGA_DESTINO, input.getIdBodegaDestino()));
            }
            if ((input.getFechaInicio() != null && input.getFechaFin() != null) && (!"".equals(input.getFechaInicio()) && !"".equals(input.getFechaFin()))) {
                    condiciones
                            .add(UtileriasJava.setCondicion(Conf.FILTRO_RANGO_FECHAS, AsignacionReserva.CAMPO_CREADO_EL,
                                    input.getFechaInicio(), input.getFechaFin(), Conf.TXT_FORMATO_FECHA_CORTA));
            }
            if (input.getEstado() != null && !"".equals(input.getEstado())) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
                            AsignacionReserva.CAMPO_ESTADO, input.getEstado()));
            }
        }
        return condiciones;
    }

    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo
     * @return output Respuesta y listado con los datos encontrados.
     * @throws SQLException
     */
    public OutputAsignacion getDatos(InputAsignacion input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta r = new Respuesta();
        OutputAsignacion output = null;
        Connection conn = null;
        String nombreTransaccionLog = "";
        COD_PAIS = input.getCodArea();

        switch (metodo) {
        case Conf.METODO_GET:
            nombreServicio = Conf.LOG_GET_ASIGNACIONES_RESERVAS;
            nombreTransaccionLog = Conf.LOG_TRANSACCION_CONSULTA_DATOS;
            break;
        case Conf.METODO_POST:
            nombreServicio = Conf.LOG_POST_ASIGNACIONES_RESERVAS;
            nombreTransaccionLog = Conf.LOG_TRANSACCION_CREA_ASIGNACION_RESERVA;
            break;
        case Conf.METODO_PUT:
            nombreServicio = Conf.LOG_PUT_ASIGNACIONES_RESERVAS;
            nombreTransaccionLog = Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA;
            break;
        }

        try {
            conn = getConnRegional();
           ID_PAIS = getIdPais(conn, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            r = validarInput(conn, input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equalsIgnoreCase(r.getDescripcion())) {
                output = new OutputAsignacion();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(nombreTransaccionLog, nombreServicio, "0", Conf.LOG_TIPO_NINGUNO,
                        output.getRespuesta().getDescripcion(), ""));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                output = OperacionAsignacion.doGet(conn, input, metodo, ID_PAIS);

            } else if (metodo == Conf.METODO_POST) {
                output = OperacionAsignacion.doPost(conn, input, ID_PAIS);

            } else if (metodo == Conf.METODO_PUT) {
                output = OperacionAsignacion.doModReserva(conn, input, metodo, ID_PAIS);
            }
            
        } catch (SQLException e) {
            r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputAsignacion();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(nombreTransaccionLog, nombreServicio, "0", Conf.LOG_TIPO_NINGUNO,
                    output.getRespuesta().getDescripcion(), ""));

        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputAsignacion();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(nombreTransaccionLog, nombreServicio, "0", Conf.LOG_TIPO_NINGUNO,
                    output.getRespuesta().getDescripcion(), ""));

        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
    
    /**
     * M\u00E9todo para modificar articulos de una asignaci\u00F3n o reserva.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @return output Respuesta de la modificacion.
     */
    public OutputAsignacion modDetAsignacionReserva(InputAsignacion input) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "modDetAsignacion";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        OutputAsignacion output = null;
        RespuestaAsignacion respuestaMod = new RespuestaAsignacion();
        COD_PAIS = input.getCodArea();

        Connection conn = null;
        try {
            conn = getConnRegional();
           ID_PAIS = getIdPais(conn, input.getCodArea());
            nombreServicio = Conf.LOG_PUT_ASIGNACIONES_RESERVAS;

            // Validaci\u00F3n de datos en el input
            r = validarInput(conn, input, ID_PAIS);
            log.trace("Respuesta validación: " + r.getDescripcion());
            if (!"OK".equalsIgnoreCase(r.getDescripcion())) {
                output = new OutputAsignacion();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, nombreServicio, "0",
                        Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), ""));

                return output;
            }

            respuestaMod = OperacionAsignacion.doModAsignacionReserva(conn, input, listaLog, ID_PAIS);
            listaLog = new ArrayList<LogSidra>();
            listaLog.addAll(respuestaMod.getListaLog());
        } catch (SQLException e) {
            r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputAsignacion();
            output.setRespuesta(r);
            respuestaMod = new RespuestaAsignacion();
            respuestaMod.setDatos(output);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, nombreServicio, "0",
                    Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), e.getMessage()));

        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputAsignacion();
            output.setRespuesta(r);
            respuestaMod = new RespuestaAsignacion();
            respuestaMod.setDatos(output);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_ASIGNACION_RESERVA, nombreServicio, "0",
                    Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return respuestaMod.getDatos();
    }
}

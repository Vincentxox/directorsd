package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.condicionoferta.InputCondicionPrincipalOferta;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.condicionoferta.OutputCondicionOferta;
import com.consystec.sc.sv.ws.operaciones.OperacionCondicionOferta;
import com.consystec.sc.sv.ws.orm.ArticulosSidra;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.Condicion;
import com.consystec.sc.sv.ws.orm.CondicionOferta;
import com.consystec.sc.sv.ws.orm.OfertaCampania;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
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
 * @changelog
 *  2016-07-20: Agregados tipos de condici\u00F3n por PDV o Zona+CategoriaPDV<br>
 *  2017-02-13: Agregados tipo de cliente a condici\u00F3n por Art\u00EDculo<br>
 *  2017-03-28: Agregadas condiciones de tipo PAGUE_LLEVE<br>
 * 
 */
public class CtrlCondicionOferta extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlCondicionOferta.class);
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_CONDICION_OFERTA;
    private static String servicioPost = Conf.LOG_POST_CONDICION_OFERTA;
    private static String servicioPut = Conf.LOG_PUT_CONDICION_OFERTA;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
    /***
     * Validando que no vengan par\u00E9metros nulos
     * 
     * @param conn 
     * @param metodo
     * @param invTelca 
     * @param estadoAlta 
     * @param tipoPanel 
     * @param tipoRuta 
     * @param tipoOferta 
     * @param tipoCampania 
     * @param tipoZona 
     * @param tipoPDV 
     * @param tipoArticulo 
     * @param tipoVenta 
     * @param tipoMonto 
     * @param tipoPorcentaje 
     * @param tipoTecnologia 
     * @param tipoClienteAmbos 
     * @param tipoClienteFinal 
     * @param tipoClientePDV 
     * @param tipoPagueLleve 
     * @throws SQLException 
    */
    public Respuesta validarDatos(Connection conn, InputCondicionPrincipalOferta objDatos, int metodo,
            String tipoPorcentaje, String tipoMonto, String tipoVenta, String tipoArticulo, String tipoPDV,
            String tipoZona, String tipoCampania, String tipoOferta, String tipoRuta, String tipoPanel,
            String estadoAlta, String tipoTecnologia, String tipoClientePDV, String tipoClienteFinal,
            String tipoClienteAmbos, String tipoPagueLleve, BigDecimal idPais) throws SQLException {
        Respuesta objRespuesta = new Respuesta();
        String nombreMetodo = "validarDatos";
        String nombreCampania="";
        String nombreClase = new CurrentClassGetter().getClassName();

        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
            return objRespuesta;
        }

        if (metodo == Conf.METODO_POST) {
            if (objDatos.getIdOfertaCampania() == null || "".equals(objDatos.getIdOfertaCampania().trim())) {
                return getMensaje(Conf_Mensajes.MSJ_IDOFERTACAMPANIA_NULO_95, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
            } else {
                if (!isNumeric(objDatos.getIdOfertaCampania())) {
                    // error, no numerico
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAMP_NUM_221, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
                }
            }

            if (objDatos.getCondiciones() == null || objDatos.getCondiciones().isEmpty()) {
                return getMensaje(Conf_Mensajes.MSJ_CONDICION_NULA_402, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
            } else {
                for (int a = 0; a < objDatos.getCondiciones().size(); a++) {
                    if (objDatos.getCondiciones().get(a).getNombre() == null
                            || "".equals(objDatos.getCondiciones().get(a).getNombre().trim())) {
                        return getMensaje(Conf_Mensajes.MSJ_NOMBRE_NULO_96, null, nombreClase, nombreMetodo,
                                "En la condici\u00F3n " + (a + 1) + ".", objDatos.getCodArea());
                    }

                    if (objDatos.getCondiciones().get(a).getTipoGestion() == null
                            || "".equals(objDatos.getCondiciones().get(a).getTipoGestion().trim())) {
                        return getMensaje(Conf_Mensajes.MSJ_TIPOGESTION_NULO_97, null, nombreClase, nombreMetodo,
                                "En la condici\u00F3n " + (a + 1) + ".", objDatos.getCodArea());
                    }

                    if (objDatos.getCondiciones().get(a).getTipoCondicion() == null
                            || "".equals(objDatos.getCondiciones().get(a).getTipoCondicion().trim())) {
                        return getMensaje(Conf_Mensajes.MSJ_TIPO_CONDICION_NULO_414, null, nombreClase, nombreMetodo,
                                "En la condici\u00F3n " + (a + 1) + ".", objDatos.getCodArea());
                    }

                    if (objDatos.getCondiciones().get(a).getDetalle() == null
                            || objDatos.getCondiciones().get(a).getDetalle().isEmpty()) {
                        return getMensaje(Conf_Mensajes.MSJ_DET_CONDICION_NULO_401, null, nombreClase, nombreMetodo,
                                "En la condici\u00F3n " + (a + 1) + ".", objDatos.getCodArea());
                    } else {
                        for (int b = 0; b < objDatos.getCondiciones().get(a).getDetalle().size(); b++) {
                            String tipo = "";
                            String idArticulo = "";
                            String montoInicial = "";
                            String montoFinal = "";
                            String tipoDescuento = "";
                            String valorDescuento = "";
                            String idPDV = "";
                            String zonaPDV = "";
                            String categoriaPDV = "";
                            String tecnologia = "";
                            String tipoCliente = "";
                            List<BigDecimal> respValidarArticulo ;
                            boolean validarCondicionPDV = false;
                            String idArticuloRegalo = "";
                            String cantidadArticuloRegalo = "";

                            if (objDatos.getCondiciones().get(a).getDetalle().get(b).getTipo() != null) {
                                tipo = objDatos.getCondiciones().get(a).getDetalle().get(b).getTipo().trim();
                            }

                            if (objDatos.getCondiciones().get(a).getDetalle().get(b).getIdArticulo() != null) {
                                idArticulo = objDatos.getCondiciones().get(a).getDetalle().get(b).getIdArticulo().trim();

                                if (!isNumeric(idArticulo)) {
                                    // error, no numerico
                                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDARTICULO_NUM_131, null,
                                            nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                }
                            }

                            if (objDatos.getCondiciones().get(a).getDetalle().get(b).getIdPDV() != null) {
                                idPDV = objDatos.getCondiciones().get(a).getDetalle().get(b).getIdPDV().trim();

                                if (!isNumeric(idPDV)) {
                                    // error, no numerico
                                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPDV_NUM_315, null, nombreClase,
                                            nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                }
                            }

                            if (objDatos.getCondiciones().get(a).getDetalle().get(b).getZonaComercialPDV() != null) {
                                zonaPDV = objDatos.getCondiciones().get(a).getDetalle().get(b).getZonaComercialPDV().trim();
                            }

                            if (objDatos.getCondiciones().get(a).getDetalle().get(b).getCategoriaPDV() != null) {
                                categoriaPDV = objDatos.getCondiciones().get(a).getDetalle().get(b).getCategoriaPDV().trim();
                            }

                            // agregadas condiciones tipo pague_lleve
                            if (tipo.equalsIgnoreCase(tipoPagueLleve)) {
                                //se validan los elementos especificos de pague_lleve
                                if (objDatos.getCondiciones().get(a).getDetalle().get(b).getIdArticuloRegalo() != null) {
                                    idArticuloRegalo = objDatos.getCondiciones().get(a).getDetalle().get(b).getIdArticuloRegalo().trim();
                                }

                                if (objDatos.getCondiciones().get(a).getDetalle().get(b).getCantidadArticuloRegalo() != null) {
                                    cantidadArticuloRegalo = objDatos.getCondiciones().get(a).getDetalle().get(b).getCantidadArticuloRegalo().trim();
                                }

                                if ("".equals(idArticuloRegalo) || !isNumeric(idArticuloRegalo)) {
                                    return getMensaje(Conf_Mensajes.MSJ_ERROR_INPUT_ID_ART_REGALO_906, null,
                                            nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                }

                                if ("".equals(cantidadArticuloRegalo)) {
                                    return getMensaje(Conf_Mensajes.MSJ_ERROR_INPUT_CANT_ART_REGALO_907, null,
                                            nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());

                                } else if (!isNumeric(cantidadArticuloRegalo) || new Integer(cantidadArticuloRegalo) < 1) {
                                    return getMensaje(Conf_Mensajes.MSJ_ERROR_INPUT_CANT_ART_REGALO_NUM_908, null,
                                            nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                }
                                
                                if (objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoInicial() != null) {
                                    montoInicial = objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoInicial().trim();

                                    if (!isDecimal(montoInicial)) {
                                        // error, no numerico
                                        return getMensaje(Conf_Mensajes.MSJ_ELEMENTO_DET_MONTOINICIAL_404, null, nombreClase, nombreMetodo,
                                                "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                    }
                                }

                                if (objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoFinal() != null) {
                                    montoFinal = objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoFinal().trim();

                                    if (!isDecimal(montoFinal)) {
                                        // error, no numerico
                                        return getMensaje(Conf_Mensajes.MSJ_ELEMENTO_DET_MONTOFINAL_405, null, nombreClase, nombreMetodo,
                                                "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                    }
                                }

                                Respuesta validacionMontos = validarMontos(montoInicial, montoFinal, nombreClase, nombreMetodo, a, b, objDatos.getCodArea());
                                if (validacionMontos != null) {
                                    return validacionMontos;
                                }

                                // este tipo de condicion no debe llevar estos valores
                                objDatos.getCondiciones().get(a).getDetalle().get(b).setTipoDescuento("");
                                objDatos.getCondiciones().get(a).getDetalle().get(b).setValorDescuento("");

                            } else {
                                if (!tipo.equalsIgnoreCase(tipoTecnologia)) {
                                    if ((!tipo.equalsIgnoreCase(tipoZona) && !tipo.equalsIgnoreCase(tipoPDV)) &&  (objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoInicial() != null)) {
                                            montoInicial = objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoInicial().trim();
                                            if (!isDecimal(montoInicial)) {
                                                // error, no numerico
                                                return getMensaje(Conf_Mensajes.MSJ_ELEMENTO_DET_MONTOINICIAL_404, null, nombreClase,
                                                        nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                        }
                                    }

                                    if ("".equals(idArticulo) && (objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoFinal() != null)){
                                            montoFinal = objDatos.getCondiciones().get(a).getDetalle().get(b).getMontoFinal().trim();

                                            if (!isDecimal(montoFinal)) {
                                                // error, no numerico
                                                return getMensaje(Conf_Mensajes.MSJ_ELEMENTO_DET_MONTOFINAL_405, null, nombreClase, nombreMetodo,
                                                        "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                            }
                                    }
                                }
                                
                                // se valida normal el resto de condiciones
                                if (objDatos.getCondiciones().get(a).getDetalle().get(b).getValorDescuento() != null) {
                                    valorDescuento = objDatos.getCondiciones().get(a).getDetalle().get(b).getValorDescuento().trim();

                                    if (!isDecimal(valorDescuento)) {
                                        // error, no numerico
                                        return getMensaje(Conf_Mensajes.MSJ_VALOR_DESCUENTO_NULO_419, null, nombreClase,
                                                nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                    }
                                }

                                if (objDatos.getCondiciones().get(a).getDetalle().get(b).getTipoDescuento() != null) {
                                    tipoDescuento = objDatos.getCondiciones().get(a).getDetalle().get(b).getTipoDescuento().trim();

                                    if ("".equals(tipoDescuento) || (!tipoDescuento.equalsIgnoreCase(tipoPorcentaje)
                                            && !tipoDescuento.equalsIgnoreCase(tipoMonto))) {
                                        // error, no numerico
                                        return getMensaje(Conf_Mensajes.MSJ_TIPO_DESCUENTO_NO_DEFINIDO_420, null, nombreClase, nombreMetodo,
                                                "Tipo " + tipoPorcentaje + " o " + tipoMonto + " en el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                    }
                                }
                            }

                            if (tipo.equalsIgnoreCase(tipoTecnologia)) {
                                // agregadas condiciones por tecnologia
                                if (objDatos.getCondiciones().get(a).getDetalle().get(b).getTecnologia() != null) {
                                    tecnologia = objDatos.getCondiciones().get(a).getDetalle().get(b).getTecnologia().trim();
                                }

                                if ("".equals(tecnologia)) {
                                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TECNOLOGIA_842, null, nombreClase,
                                            nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                } else {
                                    // se verifica que exista configurado el tipo de tecnologia
                                    tecnologia = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPO_TECNOLOGIA, tecnologia, objDatos.getCodArea());

                                    if (tecnologia == null ||  "".equals(tecnologia)) {
                                        return getMensaje(Conf_Mensajes.MSJ_ERROR_TIPO_TECNOLOGIA_843, null,
                                                nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                    }
                                }

                            } else if (!tipo.equalsIgnoreCase(tipoVenta)) {
                                if ("".equals(idArticulo)) {
                                    // debe ingresar el articulo
                                    return getMensaje(Conf_Mensajes.MSJ_IDARTICULO_NULO_49, null, nombreClase, nombreMetodo,
                                            "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                }

                            } else if (tipo.equalsIgnoreCase(tipoVenta)) {
                                // valida que no lleve articulo
                                if (!"".equals(idArticulo)) {
                                    // no debe llevar articulo
                                    return getMensaje(Conf_Mensajes.MSJ_CONDICION_VENTA_ARTICULO_421, null, nombreClase,
                                            nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                } else {
                                    Respuesta validacionMontos = validarMontos(montoInicial, montoFinal, nombreClase, nombreMetodo, a, b, objDatos.getCodArea());
                                    if (validacionMontos != null) {
                                        return validacionMontos;
                                    }
                                }

                            } else {
                                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_136, null, nombreClase,
                                    nombreMetodo, tipoArticulo + ", " + tipoVenta + ", " + tipoPDV + ", " + tipoZona + " o "
                                            + tipoTecnologia + " en el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                            }

                            // agregadas condiciones por pdv o zona
                            if (tipo.equalsIgnoreCase(tipoPDV)) {
                                // verificando que no envie zona ni categoria
                                if (! "".equals(zonaPDV) || ! "".equals(categoriaPDV)) {
                                    return getMensaje(Conf_Mensajes.MSJ_INPUT_CONDICION_IDPDV_750, null, nombreClase,
                                            nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());

                                } else {
                                    if ("".equals(idPDV)) {
                                        // debe ingresar el idPDV
                                        return getMensaje(Conf_Mensajes.MSJ_IDPDV_VACIO_8, null, nombreClase,
                                                nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                    } else {
                                        //se verifica que exista el pdv y que este de alta.
                                        String estadoActivo = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_ESTADOS_PDV, Conf.ESTADO_ACTIVO, objDatos.getCodArea());
                                        List<Filtro> condiciones = new ArrayList<Filtro>();
                                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCPUNTOVENTAID, idPDV));
                                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PuntoVenta.CAMPO_ESTADO, estadoActivo));
                                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));

                                        String existencia = UtileriasBD.verificarExistencia(conn, getParticion(PuntoVenta.N_TABLA, Conf.PARTITION, "", objDatos.getCodArea()), condiciones);
                                        if (new Integer(existencia) < 1) {
                                            return getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_PDV_472, null, nombreClase,
                                                    nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                        }
                                        validarCondicionPDV = true;
                                    }
                                }

                            } else if (tipo.equalsIgnoreCase(tipoZona)) {
                                // verificando que no envie el idPDV
                                if (! "".equals(idPDV)) {
                                    return getMensaje(Conf_Mensajes.MSJ_INPUT_CONDICION_ZONACAT_749, null, nombreClase,
                                            nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());

                                } else {
                                    String validarZona = UtileriasJava.getConfig(conn, Conf.GRUPO_VALIDACIONES_DINAMICAS, Conf.VALIDAR_ZONA_PDV, objDatos.getCodArea());
                                    if ("1".equals(validarZona)) {
                                        if ("".equals(zonaPDV)) {
                                            // debe ingresar la zona
                                            return getMensaje(Conf_Mensajes.MSJ_ZONACOM_NULA_31, null, nombreClase,
                                                    nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                        } else {
                                            // se verifica que exista la zona
                                            zonaPDV = UtileriasJava.getConfig(conn, Conf.GRUPO_ZONA_COMERCIAL_PDV, zonaPDV, objDatos.getCodArea());

                                            if (zonaPDV == null || "".equals(zonaPDV.trim())) {
                                                return getMensaje(Conf_Mensajes.MSJ_ZONA_INVALIDA_752, null, nombreClase, nombreMetodo,
                                                        "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                            }
                                        }
                                    }

                                    if ("".equals(categoriaPDV)) {
                                        // debe ingresar la categoria
                                        return getMensaje(Conf_Mensajes.MSJ_CATEGORIA_NULO_485, null, nombreClase,
                                                nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                    } else {
                                        // se verifica que exista la categoria
                                        categoriaPDV = UtileriasJava.getConfig(conn, Conf.GRUPO_CATEGORIA_PDV, categoriaPDV, objDatos.getCodArea());

                                        if (categoriaPDV == null || "".equals(categoriaPDV.trim())) {
                                            return getMensaje(Conf_Mensajes.MSJ_CAT_INVALIDA_498, null, nombreClase,
                                                    nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                        }
                                    }
                                }
                            }

                            if ((tipo.equalsIgnoreCase(tipoPDV) || tipo.equalsIgnoreCase(tipoZona))&& objDatos.getCondiciones().get(a).getDetalle().size() > 1) {
                                    return getMensaje(Conf_Mensajes.MSJ_ERROR_DETALLES_CONDICION_PDV_751, null,
                                            nombreClase, nombreMetodo, tipoPDV + " o " + tipoZona + ".", objDatos.getCodArea());
                            }

                            if (tipo.equalsIgnoreCase(tipoArticulo) || tipo.equalsIgnoreCase(tipoTecnologia)
                                    || tipo.equalsIgnoreCase(tipoPagueLleve)) {
                                tipoCliente = objDatos.getCondiciones().get(a).getDetalle().get(b).getTipoCliente();
                                if (tipoCliente == null || "".equals(tipoCliente)) {
                                    return getMensaje(Conf_Mensajes.MSJ_INPUT_TIPOCLIENTE_881, null, nombreClase,
                                            nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                } else {
                                    // se verifica que exista configurado el tipo de cliente
                                    if (!tipoCliente.equalsIgnoreCase(tipoClientePDV) && !tipoCliente.equalsIgnoreCase(tipoClienteFinal)
                                            && !tipoCliente.equalsIgnoreCase(tipoClienteAmbos)) {
                                        return getMensaje(Conf_Mensajes.MSJ_ERROR_TIPOCLIENTE_882, null, nombreClase,
                                                nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                    }
                                }
                            }

                            if (tipo.equalsIgnoreCase(tipoTecnologia)) {
                                // se valida que no exista otra oferta de tecnologia con el tipo de cliente en general
                                respValidarArticulo = OperacionCondicionOferta.validarTecnologiaCondicion(conn,
                                        objDatos.getIdOfertaCampania(), tipoOferta.toUpperCase(),
                                        tipoRuta.toUpperCase(), tipoPanel.toUpperCase(), estadoAlta.toUpperCase(),
                                        objDatos.getCondiciones().get(a).getTipoGestion().toUpperCase(),
                                        tipoCliente.toUpperCase(), tipoClientePDV, tipoClienteFinal, tipoClienteAmbos,
                                        tipoTecnologia.toUpperCase(), tecnologia, ID_PAIS);

                                if (respValidarArticulo.get(0).intValue() > 0) {
                                    return getMensaje(Conf_Mensajes.MSJ_ERROR_TECNOLOGIA_OFERTA_RUTA_887, null,
                                            nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());

                                } else if (respValidarArticulo.get(1).intValue() > 0) {
                                    return getMensaje(Conf_Mensajes.MSJ_ERROR_TECNOLOGIA_OFERTA_PANEL_888, null,
                                            nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                }

                            } else {
                                if (validarCondicionPDV) {
                                    // se valida que no exista otra oferta con el articulo en el pdv  
                                    respValidarArticulo = OperacionCondicionOferta.validarArticuloCondicion(conn,
                                            objDatos.getIdOfertaCampania(), idArticulo, tipoOferta.toUpperCase(),
                                            tipoCampania.toUpperCase(), tipoRuta.toUpperCase(), tipoPanel.toUpperCase(),
                                            estadoAlta.toUpperCase(), objDatos.getCondiciones().get(a).getTipoGestion().toUpperCase(),
                                            tipoCliente.toUpperCase(), tipoClientePDV, tipoClienteFinal,
                                            tipoClienteAmbos, validarCondicionPDV, tipoPDV.toUpperCase(), idPDV, null, null, null, idPais);

                                } else {
                                    if (tipo.equalsIgnoreCase(tipoPagueLleve)) {
                                        //validaciones de condiciones pague_lleve
                                        respValidarArticulo = OperacionCondicionOferta.validarArticuloCondicion(conn,
                                                objDatos.getIdOfertaCampania(), idArticulo, tipoOferta.toUpperCase(),
                                                tipoCampania.toUpperCase(), tipoRuta.toUpperCase(),
                                                tipoPanel.toUpperCase(), estadoAlta.toUpperCase(),
                                                objDatos.getCondiciones().get(a).getTipoGestion().toUpperCase(),
                                                tipoCliente.toUpperCase(), tipoClientePDV, tipoClienteFinal,
                                                tipoClienteAmbos, validarCondicionPDV, tipoPagueLleve.toUpperCase(), "",
                                                idArticuloRegalo, montoInicial, montoFinal,idPais);
                                        
                                    } else {
                                        // se valida que no exista otra oferta con el articulo en general
                                        respValidarArticulo = OperacionCondicionOferta.validarArticuloCondicion(conn,
                                                objDatos.getIdOfertaCampania(), idArticulo, tipoOferta.toUpperCase(),
                                                tipoCampania.toUpperCase(), tipoRuta.toUpperCase(),
                                                tipoPanel.toUpperCase(), estadoAlta.toUpperCase(),
                                                objDatos.getCondiciones().get(a).getTipoGestion().toUpperCase(),
                                                tipoCliente.toUpperCase(), tipoClientePDV, tipoClienteFinal,
                                                tipoClienteAmbos, validarCondicionPDV, tipoArticulo.toUpperCase(), "", null, null, null,idPais);
                                    }
                                }

                                if (respValidarArticulo.get(0).intValue() < 1) {
                                    return getMensaje(Conf_Mensajes.MSJ_ERROR_EXISTENCIA_ARTICULO_257, null, nombreClase, nombreMetodo,
                                            "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());

                                } else if (respValidarArticulo.get(5).intValue() < 1) {
                                    return getMensaje(Conf_Mensajes.MSJ_ERROR_EXISTENCIA_ARTICULO_257, null, nombreClase, nombreMetodo,
                                            "idArticuloRegalo, En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());

                                } else {
                                    if (tipo.equalsIgnoreCase(tipoPagueLleve)) {
                                        if (respValidarArticulo.get(1).intValue() > 0 || respValidarArticulo.get(2).intValue() > 0
                                                || respValidarArticulo.get(3).intValue() > 0 || respValidarArticulo.get(4).intValue() > 0) {
                                            return getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_MONTOS_OFERTA_909, null,
                                                    nombreClase, nombreMetodo, null, objDatos.getCodArea());
                                        }
                                    } else {
                                        if (validarCondicionPDV) {
                                            if (respValidarArticulo.get(1).intValue() > 0
                                                    || respValidarArticulo.get(2).intValue() > 0) {
                                                return getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_OFERTA_PDV_883, null,
                                                        nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                            }
                                        } else {
                                            if (respValidarArticulo.get(1).intValue() > 0) {
                                            	//El artículo ya se encuentra en otra oferta para la misma ruta.
                                            	nombreCampania=OperacionCondicionOferta.buscarNombreCampania(conn,respValidarArticulo.get(1).intValue(), estadoAlta.toUpperCase(),idPais);
                                                return getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_OFERTA_RUTA_254,
                                                        null, nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + " con el nombre de: "+ nombreCampania +".", objDatos.getCodArea());

                                            } else if (respValidarArticulo.get(2).intValue() > 0) {
                                            	//El artículo ya se encuentra en otra oferta para la misma panel.
                                            	nombreCampania=OperacionCondicionOferta.buscarNombreCampania(conn,respValidarArticulo.get(2).intValue(), estadoAlta.toUpperCase(),idPais);
                                                return getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_OFERTA_PANEL_253,
                                                        null, nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + " con el nombre de: "+ nombreCampania +".", objDatos.getCodArea());

                                            } else if (respValidarArticulo.get(3).intValue() > 0) {
                                            	nombreCampania=OperacionCondicionOferta.buscarNombreCampania(conn,respValidarArticulo.get(3).intValue(), estadoAlta.toUpperCase(),idPais);
                                                return getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_CAMPANIA_RUTA_256,
                                                        null, nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + " con el nombre de: "+ nombreCampania +".", objDatos.getCodArea());

                                            } else if (respValidarArticulo.get(4).intValue() > 0) {
                                            	nombreCampania=OperacionCondicionOferta.buscarNombreCampania(conn,respValidarArticulo.get(4).intValue(), estadoAlta.toUpperCase(),idPais);
                                                return getMensaje(Conf_Mensajes.MSJ_ERROR_ARTICULO_CAMPANIA_PANEL_255,
                                                        null, nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + " con el nombre de: "+ nombreCampania +".", objDatos.getCodArea());
                                            }
                                        }
                                    }
                                }
                            }
                            
                            //validar Producto idProductOffering y idDescuento jcsimon
                           /* if (objDatos.getCondiciones().get(a).getDetalle().get(b).getIdDescuento()!=null && !"".equals(objDatos.getCondiciones().get(a).getDetalle().get(b).getIdDescuento().trim()))
                        	{
                        		if (!isNumeric(objDatos.getCondiciones().get(a).getDetalle().get(b).getIdDescuento()))
                        		{
                        			return getMensaje(Conf_Mensajes.MSJ_ID_DESCUENTO_NO_ES_NUMERICO_678,
                                            null, nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                        		}
                        		else
                        		{
                        			if(objDatos.getCondiciones().get(a).getDetalle().get(b).getIdProductOffering()==null ||  "".equals(objDatos.getCondiciones().get(a).getDetalle().get(b).getIdProductOffering().trim()))
                            		{
                            			return getMensaje(Conf_Mensajes.MSJ_ID_OFFERING_VACIO_665,
                                                null, nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                            		}
                        			else if (!isNumeric(objDatos.getCondiciones().get(a).getDetalle().get(b).getIdProductOffering()))
                        			{
                        				return getMensaje(Conf_Mensajes.MSJ_ID_OFFERING_NO_ES_NUMERICO_666,
                                                null, nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                        			}
                        			
//									validamos que el Id_rpducto_offering y id_descuento existan
                        			
                        			String sql= "SELECT  COUNT(*) FROM TC_SC_OFERTA_SIDRA_FS oferta"
                        					+ " INNER JOIN TC_SC_DESCUENTO_SIDRA_FS descuento "
                        					+ " ON oferta.ID_PRODUCT_OFFERING=descuento.ID_PRODUCT_OFFERING"
                        					+ " WHERE descuento.ID_DESCUENTO = " + objDatos.getCondiciones().get(a).getDetalle().get(b).getIdDescuento() + ""
                        					+ " AND  descuento.ID_PRODUCT_OFFERING=" + objDatos.getCondiciones().get(a).getDetalle().get(b).getIdProductOffering();

                                     int existencia =Integer.parseInt(UtileriasBD.executeQueryOneRecord(conn, sql));
                                     log.error("existencia: " + existencia);
                                     if (existencia<=0)
                                     {
                                    	 log.error("ofeta no valida: " + existencia);
                                    	 return getMensaje(Conf_Mensajes.MSJ_ID_OFERTA_FS_NO_VALIDA_679,
                                                 null, nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                     }
                        		}
                        		
                        	}else if(objDatos.getCondiciones().get(a).getDetalle().get(b).getIdProductOffering()!=null && !objDatos.getCondiciones().get(a).getDetalle().get(b).getIdProductOffering().trim().equals(""))
                        	{
                        		if (!isNumeric(objDatos.getCondiciones().get(a).getDetalle().get(b).getIdProductOffering()))
                    			{
                    				return getMensaje(Conf_Mensajes.MSJ_ID_OFFERING_NO_ES_NUMERICO_666,
                                            null, nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                    			}
                        		else
                        		{
                        			if (objDatos.getCondiciones().get(a).getDetalle().get(b).getIdDescuento()==null ||  "".equals(objDatos.getCondiciones().get(a).getDetalle().get(b).getIdDescuento().trim()))
                        			{
                        				return getMensaje(Conf_Mensajes.MSJ_ID_DESCUENTO_VACIO_677,
                                                null, nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                        			}
                        			else if(!isNumeric(objDatos.getCondiciones().get(a).getDetalle().get(b).getIdDescuento()))
                        			{
                        				return getMensaje(Conf_Mensajes.MSJ_ID_DESCUENTO_NO_ES_NUMERICO_678,
                                                null, nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                        			}
                        			//validamos que el Id_rpducto_offering y id_descuento existan
                        			
                        			String sql= "SELECT  COUNT(*) FROM TC_SC_OFERTA_SIDRA_FS oferta"
                        					+ " INNER JOIN TC_SC_DESCUENTO_SIDRA_FS descuento "
                        					+ " ON oferta.ID_PRODUCT_OFFERING=descuento.ID_PRODUCT_OFFERING"
                        					+ " WHERE descuento.ID_DESCUENTO = " + objDatos.getCondiciones().get(a).getDetalle().get(b).getIdDescuento() + ""
                        					+ " AND  descuento.ID_PRODUCT_OFFERING=" + objDatos.getCondiciones().get(a).getDetalle().get(b).getIdProductOffering();


                        			int existencia =Integer.parseInt(UtileriasBD.executeQueryOneRecord(conn, sql));
                        			log.error("existencia: " + existencia);
                                     if (existencia<=0)
                                     {
                                    	 log.error("ofeta no valida: " + existencia);
                                    	 return getMensaje(Conf_Mensajes.MSJ_ID_OFERTA_FS_NO_VALIDA_679,
                                                 null, nombreClase, nombreMetodo, "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", objDatos.getCodArea());
                                     }
                        			
                        		}
                        	}*/
                            	
                            log.error("paso opicones  nuevas: " );	
                           //fin validacion
                        }
                    }
                }
            }                      
        }

        if (metodo == Conf.METODO_DELETE) {
            if (objDatos.getIdCondicion() == null || "".equals(objDatos.getIdCondicion().trim())) {
                return getMensaje(Conf_Mensajes.MSJ_CONDICION_NULO_418, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
            } else {
                if (!isNumeric(objDatos.getIdCondicion())) {
                    // error, no numerico
                    return getMensaje(Conf_Mensajes.MSJ_CONDICION_NUM_413, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
                }
            }
        }

        if (metodo == Conf.METODO_GET) {
            if ((objDatos.getIdOfertaCampania() != null && !"".equals(objDatos.getIdOfertaCampania().trim()))&& !isNumeric(objDatos.getIdOfertaCampania())) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ID_OFERTACAMP_NUM_221, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
            }
            if ((objDatos.getIdCondicion() != null && !"".equals(objDatos.getIdCondicion().trim()))&& !isNumeric(objDatos.getIdCondicion())) {
                    return getMensaje(Conf_Mensajes.MSJ_CONDICION_NUM_413, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
            }
            if ((objDatos.getIdArticulo() != null && !"".equals(objDatos.getIdArticulo().trim()))&& !isNumeric(objDatos.getIdArticulo())) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDARTICULO_NUM_131, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
            }
        }

        log.error("valida codRespuesata " + objRespuesta.getCodResultado() );
        if (objRespuesta.getCodResultado() == null) {
            objRespuesta = new Respuesta();
            objRespuesta.setDescripcion("OK");
        }

        return objRespuesta;
    }

    private Respuesta validarMontos(String montoInicial, String montoFinal, String nombreClase, String nombreMetodo, int a, int b, String codArea) {
        if ("".equals(montoInicial) && "".equals(montoFinal)) {
            // error, debe ingresar uno de los montos
            return getMensaje(Conf_Mensajes.MSJ_ERROR_MONTO_423, null, nombreClase, nombreMetodo,
                    "En el detalle " + (b + 1) + " de la condicion " + (a + 1) + ".", codArea);
        } else {
            if (!"".equals(montoInicial) && !"".equals(montoFinal)) {
                if (new Integer(montoInicial) > new Integer(montoFinal)) {
                    // error, monto inicial mayor a final
                    return getMensaje(Conf_Mensajes.MSJ_ERROR_MONTO_INICIAL_MENOR_410, null, nombreClase, nombreMetodo,
                            "En el detalle " + (b + 1) + " de la condición " + (a + 1) + ".", codArea);
                }
            } else if (!"".equals(montoInicial) && new Integer(montoInicial) <= 0) {
                    // error, el monto inicial debe ser mayor a 0
                    return getMensaje(Conf_Mensajes.MSJ_ERROR_MONTO_INICIAL_0_411, null, nombreClase, nombreMetodo,
                            "En el detalle " + (b + 1) + " de la condicion " + (a + 1) + ".", codArea);
            } else if (!"".equals(montoFinal)&& new Integer(montoFinal) <= 0) {
                    // error, el monto final debe ser mayor a 0
                    return getMensaje(Conf_Mensajes.MSJ_ERROR_MONTO_FINAL_0_422, null, nombreClase, nombreMetodo,
                            "En el detalle " + (b + 1) + " de la condicion " + (a + 1) + ".", codArea);
            }
        }
        return null;
    }

    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en el m\u00E9todo GET.
     * 
     * @return
     */
    public static String[][] obtenerCamposGet() {
        String campos[][] = {
            { Condicion.N_TABLA, Condicion.CAMPO_TCSCCONDICIONID },
            { Condicion.N_TABLA, Condicion.CAMPO_TCSCOFERTACAMPANIAID },
            { OfertaCampania.N_TABLA, OfertaCampania.CAMPO_NOMBRE + " AS NOMBRECAMPANIA" },
            { Condicion.N_TABLA, Condicion.CAMPO_NOMBRE },
            { Condicion.N_TABLA, Condicion.CAMPO_TIPO_GESTION },
            { Condicion.N_TABLA, Condicion.CAMPO_TIPO_CONDICION },
            { Condicion.N_TABLA, Condicion.CAMPO_ESTADO },
            { Condicion.N_TABLA, Condicion.CAMPO_CREADO_EL },
            { Condicion.N_TABLA, Condicion.CAMPO_CREADO_POR },
            { Condicion.N_TABLA, Condicion.CAMPO_MODIFICADO_EL },
            { Condicion.N_TABLA, Condicion.CAMPO_MODIFICADO_POR }
        };

        return campos;
    }
    
    /**
     * Funci\u00F3n que indica los campos que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @return
     */
    public static String[] obtenerCamposPost() {
        String campos[] = {
            Condicion.CAMPO_TCSCCONDICIONID,
            Condicion.CAMPO_TCSCCATPAISID,
            Condicion.CAMPO_TCSCOFERTACAMPANIAID,
            Condicion.CAMPO_NOMBRE,
            Condicion.CAMPO_TIPO_GESTION,
            Condicion.CAMPO_TIPO_CONDICION,
            Condicion.CAMPO_TIPO_OFERTACAMPANIA,
            Condicion.CAMPO_ESTADO,
            Condicion.CAMPO_CREADO_EL,
            Condicion.CAMPO_CREADO_POR
        };

        return campos;
    }
    
    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET 
     * de la tabla relacionada.
     * 
     * @return
     */
    public static String[] obtenerCamposTablaHijaGet(BigDecimal idPais) {
        String campos[] = {
            CondicionOferta.CAMPO_TCSCDETCONDICIONOFERTAID,
            CondicionOferta.CAMPO_TCSCCONDICIONID,
            CondicionOferta.CAMPO_TIPO_OFERTA,
            CondicionOferta.CAMPO_ARTICULO,
                "(SELECT A." + ArticulosSidra.CAMPO_DESCRIPCION + " FROM " + ArticulosSidra.N_TABLA + " A WHERE TCSCCATPAISID = " + idPais.toString()
                + " AND A." + ArticulosSidra.CAMPO_ARTICULO + " = B." + CondicionOferta.CAMPO_ARTICULO + ") AS NOMBREART",
            CondicionOferta.CAMPO_TIPO_CLIENTE,
            CondicionOferta.CAMPO_TECNOLOGIA,
            CondicionOferta.CAMPO_MONTO_INICIAL,
            CondicionOferta.CAMPO_MONTO_FINAL,
            CondicionOferta.CAMPO_TIPO_DESCUENTO,
            CondicionOferta.CAMPO_VALOR_DESCUENTO,
            CondicionOferta.CAMPO_TCSCPUNTOVENTAID,
                "(SELECT P." + PuntoVenta.CAMPO_NOMBRE + " FROM " + PuntoVenta.N_TABLA + " P WHERE TCSCCATPAISID = " + idPais.toString()
                + " AND P." + PuntoVenta.CAMPO_TCSCPUNTOVENTAID + " = B." + CondicionOferta.CAMPO_TCSCPUNTOVENTAID + ") AS NOMBREPDV",
            CondicionOferta.CAMPO_ZONACOMERCIAL,
            CondicionOferta.CAMPO_CATEGORIA,
            CondicionOferta.CAMPO_ARTICULO_REGALO,
                "(SELECT A." + ArticulosSidra.CAMPO_DESCRIPCION + " FROM " + ArticulosSidra.N_TABLA + " A WHERE TCSCCATPAISID = " + idPais.toString()
                + " AND A." + ArticulosSidra.CAMPO_ARTICULO + " = B." + CondicionOferta.CAMPO_ARTICULO_REGALO + ") AS NOMBREART_REGALO",
            CondicionOferta.CAMPO_CANT_ARTICULO_REGALO,
            CondicionOferta.CAMPO_TIPO_DESC_REGALO,
            CondicionOferta.CAMPO_VALOR_DESC_REGALO,
            CondicionOferta.CAMPO_ESTADO,
            CondicionOferta.CAMPO_CREADO_EL,
            CondicionOferta.CAMPO_CREADO_POR,
            CondicionOferta.CAMPO_MODIFICADO_EL,
            CondicionOferta.CAMPO_MODIFICADO_POR
        };

        return campos;
    }

    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en el m\u00E9todo POST
     * de la tabla relacionada.
     * 
     * @return
     */
    public static String[] obtenerCamposTablaHijaPost() {
        String campos[] = {
            CondicionOferta.CAMPO_TCSCDETCONDICIONOFERTAID,
            CondicionOferta.CAMPO_TCSCCATPAISID,
            CondicionOferta.CAMPO_TCSCCONDICIONID,
            CondicionOferta.CAMPO_TIPO_OFERTA,
            CondicionOferta.CAMPO_ARTICULO,
            CondicionOferta.CAMPO_TIPO_CLIENTE,
            CondicionOferta.CAMPO_TECNOLOGIA,
            CondicionOferta.CAMPO_MONTO_INICIAL,
            CondicionOferta.CAMPO_MONTO_FINAL,
            CondicionOferta.CAMPO_TIPO_DESCUENTO,
            CondicionOferta.CAMPO_VALOR_DESCUENTO,
            CondicionOferta.CAMPO_TCSCPUNTOVENTAID,
            CondicionOferta.CAMPO_ZONACOMERCIAL,
            CondicionOferta.CAMPO_CATEGORIA,
            CondicionOferta.CAMPO_ARTICULO_REGALO,
            CondicionOferta.CAMPO_CANT_ARTICULO_REGALO,
            CondicionOferta.CAMPO_TIPO_DESC_REGALO,
            CondicionOferta.CAMPO_VALOR_DESC_REGALO,
            CondicionOferta.CAMPO_ESTADO,
            CondicionOferta.CAMPO_CREADO_EL,
            CondicionOferta.CAMPO_CREADO_POR,
            CondicionOferta.CAMPO_ID_DESCUENTO
        };

        return campos;
    }

    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo deseado.
     */
    public static List<Filtro> obtenerCondiciones(InputCondicionPrincipalOferta input, int metodo, BigDecimal idPais) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        List<Filtro> condicionesExtra = new ArrayList<Filtro>();

        if (metodo == Conf.METODO_DELETE && !"".equals(input.getIdCondicion())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Condicion.CAMPO_TCSCCONDICIONID, input.getIdCondicion()));
        }

        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Condicion.N_TABLA, Condicion.CAMPO_TCSCCATPAISID, idPais.toString()));

        if (metodo == Conf.METODO_GET) {
            if (input.getIdCondicion() != null && !"".equals(input.getIdCondicion())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Condicion.N_TABLA, Condicion.CAMPO_TCSCCONDICIONID, input.getIdCondicion()));
            }
            if (input.getIdOfertaCampania() != null && !"".equals(input.getIdOfertaCampania())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Condicion.N_TABLA, Condicion.CAMPO_TCSCOFERTACAMPANIAID, input.getIdOfertaCampania()));
            }
            if (input.getNombre() != null && !"".equals(input.getNombre())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Condicion.N_TABLA, Condicion.CAMPO_NOMBRE, input.getNombre()));
            }
            if (input.getTipoGestion() != null && !"".equals(input.getTipoGestion())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Condicion.N_TABLA, Condicion.CAMPO_TIPO_GESTION, input.getTipoGestion()));
            }
            if (input.getEstado() != null && !"".equals(input.getEstado())) {
                condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Condicion.N_TABLA, Condicion.CAMPO_ESTADO, input.getEstado()));
            }

            String[] campos = { CondicionOferta.CAMPO_TCSCCONDICIONID };
            if (input.getTipo() != null && !"".equals(input.getTipo())) {
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, CondicionOferta.CAMPO_TIPO_OFERTA, input.getTipo()));
            }
            if (input.getIdArticulo() != null && !"".equals(input.getIdArticulo())) {
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CondicionOferta.CAMPO_ARTICULO, input.getIdArticulo()));
            }

            if (input.getIdPDV() != null && !"".equals(input.getIdPDV())) {
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CondicionOferta.CAMPO_TCSCPUNTOVENTAID, input.getIdPDV()));
            }
            if (input.getZonaComercialPDV() != null && !"".equals(input.getZonaComercialPDV())) {
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, CondicionOferta.CAMPO_ZONACOMERCIAL, input.getZonaComercialPDV()));
            }
            if (input.getCategoriaPDV() != null && !"".equals(input.getCategoriaPDV())) {
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, CondicionOferta.CAMPO_CATEGORIA, input.getCategoriaPDV()));
            }

            if (!condicionesExtra.isEmpty()) {
                condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, CondicionOferta.CAMPO_TCSCCATPAISID, idPais.toString()));
                String selectSQL = UtileriasBD.armarQuerySelect(CondicionOferta.N_TABLA, campos, condicionesExtra);
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Condicion.CAMPO_TCSCCONDICIONID, selectSQL));
            }
        }

        return condiciones;
    }
    
    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo 
     * @return output Respuesta y listado con las Condiciones encontrados.
     */
    public OutputCondicionOferta getDatos(InputCondicionPrincipalOferta input, int metodo) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        OutputCondicionOferta output = new OutputCondicionOferta();
        COD_PAIS = input.getCodArea();

        Connection conn = null;
        try {
            conn = getConnRegional();
            ID_PAIS = getIdPais(conn, input.getCodArea());
            
            String campos[] = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };

            // Se obtienen todas las configuraciones.
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, ID_PAIS.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_DESCUENTOS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_OFERTA_COMERCIAL));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_PANELRUTA));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_CONDICION_TIPOOFERTA));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_TIPO_CLIENTE_OFERTA));

            List<Map<String, String>> datosConfig = new ArrayList<Map<String, String>>();
            try {
                datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);
            } catch (SQLException e) {
                log.error("Error al obtener par\u00E9metros de configuraci\u00F3n. " + e);
                r = getMensaje(Conf_Mensajes.MSJ_ERROR_GET_CONFIG_103, null, nombreClase, nombreMetodo,
                        null, input.getCodArea());

                output.setRespuesta(r);
                return output;
            }
            
            String tipoPorcentaje = "";
            String tipoMonto = "";
            String tipoVenta = "";
            String tipoArticulo = "";
            String tipoPDV = "";
            String tipoZona = "";
            String tipoTecnologia = "";
            String tipoCampania = "";
            String tipoOferta = "";
            String tipoRuta = "";
            String tipoPanel = "";
            String tipoClienteFinal = "";
            String tipoClientePDV = "";
            String tipoClienteAmbos = "";
            String tipoPagueLleve = "";
            
            for (int i = 0; i < datosConfig.size(); i++) {
                if (Conf.TIPO_PORCENTAJE.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoPorcentaje = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_MONTO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoMonto = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CONDICION_OFERTA_VENTA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoVenta = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CONDICION_OFERTA_ARTICULO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoArticulo = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CONDICION_OFERTA_PDV.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoPDV = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CONDICION_OFERTA_ZONA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoZona = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CONDICION_OFERTA_TECNOLOGIA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoTecnologia = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CONDICION_OFERTA_PAGUE_LLEVE.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoPagueLleve = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_OFERTA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoOferta = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_CAMPANIA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoCampania = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_RUTA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoRuta = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.TIPO_PANEL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoPanel = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CLIENTE_OFERTA_PDV.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoClientePDV = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CLIENTE_OFERTA_CF.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoClienteFinal = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.CLIENTE_OFERTA_AMBOS.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    tipoClienteAmbos = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
            }

            // Validaci\u00F3n de datos en el input
            r = validarDatos(conn, input, metodo, tipoPorcentaje, tipoMonto, tipoVenta, tipoArticulo, tipoPDV, tipoZona,
                    tipoCampania, tipoOferta, tipoRuta, tipoPanel, estadoAlta, tipoTecnologia,
                    tipoClientePDV.toUpperCase(), tipoClienteFinal.toUpperCase(), tipoClienteAmbos.toUpperCase(), tipoPagueLleve, ID_PAIS);
            log.trace("Respuesta validacion: " + r.getDescripcion());
            if (!"OK".equalsIgnoreCase(r.getDescripcion())) {
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CONDICION_OFERTA, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionCondicionOferta.doGet(conn, input, metodo, false, false, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de condiciones de oferta.", ""));
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputCondicionOferta();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar condiciones de oferta.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_POST) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
                try {
                    output = OperacionCondicionOferta.doPost(conn, input, tipoOferta, estadoAlta, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_CONDICION_OFERTA_36) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CONDICION_OFERTA, servicioPost,
                                output.getIdCondicion(), Conf.LOG_TIPO_CONDICION_OFERTA,
                                "Se cre\u00F3 condici\u00F3n con ID " + output.getIdCondicion() + " para la oferta "
                                        + input.getIdOfertaCampania() + ".",
                                ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CONDICION_OFERTA, servicioPost,
                                "0", Conf.LOG_TIPO_NINGUNO, "Problema al crear condici\u00F3n de oferta.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputCondicionOferta();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CONDICION_OFERTA, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al crear condici\u00F3n de oferta.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_DELETE) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo DELETE
                try {
                    output = OperacionCondicionOferta.doDel(conn, input, metodo, tipoOferta, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_DEL_CONDICION_37) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CONDICION_OFERTA, servicioPut,
                                input.getIdCondicion(), Conf.LOG_TIPO_CONDICION_OFERTA,
                                "Se di\u00F3 de baja la condici\u00F3n de oferta con ID " + input.getIdCondicion() + ".", ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CONDICION_OFERTA, servicioPut,
                                input.getIdCondicion(), Conf.LOG_TIPO_CONDICION_OFERTA,
                                "Problema al modificar datos de condici\u00F3n de oferta.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputCondicionOferta();
                    output.setRespuesta(r);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CONDICION_OFERTA, servicioPut,
                            input.getIdCondicion(), Conf.LOG_TIPO_CONDICION_OFERTA,
                            "Problema al modificar condici\u00F3n de oferta.", e.getMessage()));
                }
            }
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputCondicionOferta();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CONDICION_OFERTA, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de condiciones de oferta.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
    
    public OutputCondicionOferta getOfertasRuta(InputCondicionPrincipalOferta input) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = new Respuesta();
        OutputCondicionOferta output = new OutputCondicionOferta();
        OutputCondicionOferta outputGet = new OutputCondicionOferta();
        COD_PAIS = input.getCodArea();
        
        Connection conn = null;
        try {
            conn = getConnRegional();
            ID_PAIS = getIdPais(conn, input.getCodArea());

            if (input.getIdRuta() == null || "".equals(input.getIdRuta().trim())) {
                output.setRespuesta(getMensaje(Conf_Mensajes.MSJ_IDRUTA_NULO_536, null, nombreClase,
                        nombreMetodo, null, input.getCodArea()));
            } else {
                if (!isNumeric(input.getIdRuta())) {
                    // error, no numerico
                    output.setRespuesta(getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDRUTA_NUM_251, null, nombreClase,
                            nombreMetodo, null, input.getCodArea()));
                }
            }

            outputGet = OperacionCondicionOferta.doGet(conn, input, Conf.METODO_GET, true, false, ID_PAIS);
            if (outputGet.getCondiciones() != null) {
                output.setCondicionesPdv(outputGet.getCondiciones());
            }
            respuesta = outputGet.getRespuesta();

            outputGet = OperacionCondicionOferta.doGet(conn, input, Conf.METODO_GET, false, true, ID_PAIS);
            if (outputGet.getCondiciones() != null) {
                output.setCondicionesZonaCat(outputGet.getCondiciones());
            }

            if (new BigDecimal(respuesta.getCodResultado()).intValue() > 0 && new BigDecimal(outputGet.getRespuesta().getCodResultado()).intValue()>0) {
         	  log.trace("se retorna primera respuesta");
             } else if (new BigDecimal(respuesta.getCodResultado()).intValue() > 0 && new BigDecimal(outputGet.getRespuesta().getCodResultado()).intValue()<0) {
                 // me quedo con la respuesta negativa
             	  log.trace("se retorna primera respuesta");
             }else if (new BigDecimal(respuesta.getCodResultado()).intValue() < 0 && new BigDecimal(outputGet.getRespuesta().getCodResultado()).intValue()>0) {
             	respuesta = outputGet.getRespuesta();
             } else if (new BigDecimal(respuesta.getCodResultado()).intValue() < 0 && new BigDecimal(outputGet.getRespuesta().getCodResultado()).intValue()<0) {
             	respuesta = outputGet.getRespuesta();
             }

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de condiciones de oferta.", ""));
        } catch (SQLException e) {
            respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputCondicionOferta();
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar condiciones de oferta.", e.getMessage()));

        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputCondicionOferta();
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_CONDICION_OFERTA, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de condiciones de oferta.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            output.setRespuesta(respuesta);
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}

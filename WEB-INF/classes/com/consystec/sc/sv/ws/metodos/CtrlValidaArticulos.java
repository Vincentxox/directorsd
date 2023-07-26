package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.consystec.sc.ca.ws.input.inventario.InputValidaArticulo;
import com.consystec.sc.ca.ws.input.inventario.Series;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventario.OutputValidaInventario;
import com.consystec.sc.sv.ws.operaciones.OperacionVentasCopia;
import com.consystec.sc.sv.ws.orm.ArticulosSidra;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class CtrlValidaArticulos extends ControladorBase {
    private List<LogSidra> listaLog = new ArrayList<LogSidra>();

    /**
     * Validando datos de entrada
     * 
     * @param objDatos
     * @return
     */
    public Respuesta validarDatos(InputValidaArticulo objDatos) {
        String nombreMetodo = "validarDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta objRespuesta = null;

        if (objDatos.getIdBodegaVendedor() == null ||  "".equals(objDatos.getIdBodegaVendedor().trim())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDBOD_VENDEDOR_NULO_425, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
        }

        // validando listado de series
        if (objDatos.getListadoSeries() == null || objDatos.getListadoSeries().isEmpty()) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LISTADO_SERIE_NULO_527, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
        } else {
            for (int a = 0; a < objDatos.getListadoSeries().size(); a++) {
                if (objDatos.getListadoSeries().get(a).getIdArticulo() == null ||  "".equals(objDatos.getListadoSeries().get(a).getIdArticulo().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDARTICULO_NULO_49, null, nombreClase, nombreMetodo,
                            "El item No." + a + 1 + "Tiene los siguientes datos incompletos:", objDatos.getCodArea());
                }

                if (objDatos.getListadoSeries().get(a).getSerieInicial() == null ||  "".equals(objDatos.getListadoSeries().get(a).getSerieInicial().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SERIE_INICIAL_NULA_525, null, nombreClase, nombreMetodo, "El item No." + a + 1 + "Tiene los siguientes datos incompletos:", objDatos.getCodArea());
                } else {
                    if ((objDatos.getListadoSeries().get(a).getSerieFinal() == null) || (objDatos.getListadoSeries().get(a).getSerieFinal().trim().equals(""))) {
                    		log.trace(1);
                    } else {
                        log.trace("Encontro un rango." + objDatos.getListadoSeries().get(a).getSerieFinal());
                        if (!isNumeric(objDatos.getListadoSeries().get(a).getSerieFinal()) || !isNumeric(objDatos.getListadoSeries().get(a).getSerieInicial())) {
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_RANGO_SERIES_NUM_363, null, nombreClase, nombreMetodo, "", objDatos.getCodArea());
                        } else {
                            BigInteger serieIni = null;
                            BigInteger serieFin = null;

                            serieIni = new BigInteger(objDatos.getListadoSeries().get(a).getSerieInicial());
                            serieFin = new BigInteger(objDatos.getListadoSeries().get(a).getSerieFinal());

                            log.trace("Serie Inicial:" + serieIni);
                            if (serieIni.longValue() > serieFin.longValue() || serieIni.longValue() == serieFin.longValue()) {
                                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_RANGO_SERIES_INV_364, null, nombreClase, nombreMetodo, "", objDatos.getCodArea());
                            }
                        }
                    }
                }
            }
        }

        return objRespuesta;
    }

    public OutputValidaInventario validaSeries(InputValidaArticulo objDatos) {
        Respuesta objRespuesta = null;
        OutputValidaInventario objReturn = new OutputValidaInventario();
        String seriesInvalidas = "";
        Inventario objInventario = new Inventario();
        Inventario objInventarioDisponible = new Inventario();
        List<Series> lstSerieInvalidas = new ArrayList<Series>();
        List<Filtro> lstFiltros = new ArrayList<Filtro>();
        List<ArticulosSidra> lstArticulos = new ArrayList<ArticulosSidra>();
        Series objSerie = new Series();
        Connection conn = null;
        String tipoInv = "";
        String estadoVendido = "";
        String estadoReservado = "";
        String estadoDevolucion = "";
        String estadoProcDevolucion = "";
        String estadoSiniestro = "";
        String estadoDisponible = "";
        String estados = "";
        String estadoAlta = "";

        String nombreMetodo = "validaSeries";
        objRespuesta = validarDatos(objDatos);

        if (objRespuesta == null) {

            try {
                conn = getConnRegional();
                BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());

                estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());
                estadoVendido = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_VENDIDO, objDatos.getCodArea());
                estadoReservado = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_RESERVADO, objDatos.getCodArea());
                estadoDevolucion = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DEVUELTO, objDatos.getCodArea());
                estadoProcDevolucion = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DEVOLUCION, objDatos.getCodArea());
                estadoSiniestro = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_SINIESTRADO, objDatos.getCodArea());
                estadoDisponible = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DISPONIBLE, objDatos.getCodArea());
                tipoInv = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPOINV, Conf.SOL_TIPOINV_TELCA, objDatos.getCodArea());

                estados = estadoVendido + "','" + estadoReservado + "','" + estadoDevolucion + "','" + estadoProcDevolucion + "','" + estadoSiniestro;

                // recorriendo listado de series a validar
                log.trace("Tamanio listadoSeries:" + objDatos.getListadoSeries().size());
                for (int a = 0; a < objDatos.getListadoSeries().size(); a++) {

                    if (objDatos.getListadoSeries().get(a).getSerieFinal() == null
                            || objDatos.getListadoSeries().get(a).getSerieFinal().equals("")) {

                        // si serie final es nula, se evalua serie de manera individual
                        log.trace("Serie individual");
                        objInventario = OperacionVentasCopia.getArticulo(conn, estados,
                                objDatos.getListadoSeries().get(a).getSerieInicial(), objDatos.getIdBodegaVendedor(),
                                objDatos.getListadoSeries().get(a).getIdArticulo(), tipoInv, false, null, objDatos.getCodArea(), ID_PAIS);

                        // si inv. es diferente de null, se encontr\u00F3 serie inv\u00E9lida
                        if (objInventario != null) {
                            objSerie = new Series();

                            objSerie.setDescripcionArticulo(objInventario.getDescripcion());
                            objSerie.setEstado(objInventario.getEstado());
                            objSerie.setTipoInventario(objInventario.getTipo_inv());
                            objSerie.setGrupo(objInventario.getTipo_grupo_sidra());
                            objSerie.setIdArticulo("" + objInventario.getArticulo());
                            objSerie.setSerieInicial(objInventario.getSerie());
                            lstSerieInvalidas.add(objSerie);
                        } else if (objInventario == null) {

                            // al no encontrarse en un estado inv\u00E9lido se busca en estado disponible
                            objInventarioDisponible = OperacionVentasCopia.getArticulo(conn, estadoDisponible,
                                    objDatos.getListadoSeries().get(a).getSerieInicial(),
                                    objDatos.getIdBodegaVendedor(), objDatos.getListadoSeries().get(a).getIdArticulo(),
                                    tipoInv, false, null, objDatos.getCodArea(), ID_PAIS);

							//sino esta en estado disponible en inventario se proceder\u00E9 a buscar en el cat\u00E9logo
                            if (objInventarioDisponible == null) {

                                lstFiltros.add(new Filtro(ArticulosSidra.CAMPO_ARTICULO, Filtro.EQ, objDatos.getListadoSeries().get(a).getIdArticulo()));
                                lstFiltros.add(new Filtro("UPPER(" + ArticulosSidra.CAMPO_ESTADO + ")", Filtro.EQ, "'" + estadoAlta.toUpperCase() + "'"));
                                lstFiltros.add(new Filtro(ArticulosSidra.CAMPO_TCSCCATPAISID, Filtro.EQ, ID_PAIS));

                                lstArticulos = OperacionVentasCopia.getCatArticulo(conn, lstFiltros, ID_PAIS);

								 //si el articulo no es encontrado en el catalogo se indicar\u00E9 que no existe
                                if (lstArticulos.isEmpty()) {
                                    objSerie = new Series();

                                    objSerie.setDescripcionArticulo("NO ENCONTRADO");
                                    objSerie.setEstado("NO EXISTE");
                                    objSerie.setTipoInventario("NO ENCONTRADO");
                                    objSerie.setGrupo("NO ENCONTRADO");
                                    objSerie.setIdArticulo(objDatos.getListadoSeries().get(a).getIdArticulo());
                                    objSerie.setSerieInicial(objDatos.getListadoSeries().get(a).getSerieInicial());
                                    lstSerieInvalidas.add(objSerie);
                                } else {
                                    objSerie = new Series();

                                    objSerie.setDescripcionArticulo(lstArticulos.get(0).getDescripcion());
                                    objSerie.setEstado("NO EXISTE");
                                    objSerie.setTipoInventario(tipoInv);
                                    objSerie.setGrupo(lstArticulos.get(0).getTipo_grupo_sidra());
                                    objSerie.setIdArticulo("" + lstArticulos.get(0).getArticulo());
                                    objSerie.setSerieInicial(objDatos.getListadoSeries().get(a).getSerieInicial());
                                    lstSerieInvalidas.add(objSerie);
                                }
                            }
                        }

                    } else {
                        // si serie final no es nula, se evalua por rango
                        log.trace("rango series");
                        seriesInvalidas = OperacionVentasCopia.validarSeriesRasca(conn,
                                objDatos.getListadoSeries().get(a).getSerieInicial(),
                                objDatos.getListadoSeries().get(a).getSerieFinal(),
                                new BigDecimal(objDatos.getListadoSeries().get(a).getIdArticulo()),
                                new BigDecimal(objDatos.getIdBodegaVendedor()), objDatos.getCodArea());

                        if (!seriesInvalidas.equalsIgnoreCase("OK")) {

                            String seriesInvalidas1 = seriesInvalidas.substring(0, seriesInvalidas.length() - 1);
                            String series[] = seriesInvalidas1.split(",");

                            objInventario = OperacionVentasCopia.getArticulo(conn, estados, series[0],
                                    objDatos.getIdBodegaVendedor(), objDatos.getListadoSeries().get(a).getIdArticulo(),
                                    tipoInv, false, null, objDatos.getCodArea(), ID_PAIS);

                            if (objInventario != null) {
                                objSerie = new Series();

                                objSerie.setDescripcionArticulo(objInventario.getDescripcion());
                                objSerie.setEstado("RANGO INVALIDO");
                                objSerie.setTipoInventario(objInventario.getTipo_inv());
                                objSerie.setGrupo(objInventario.getTipo_grupo_sidra());
                                objSerie.setIdArticulo("" + objInventario.getArticulo());
                                objSerie.setSerieInicial(objDatos.getListadoSeries().get(a).getSerieInicial());
                                objSerie.setSerieFinal(objDatos.getListadoSeries().get(a).getSerieFinal());
                                lstSerieInvalidas.add(objSerie);
                            } else {

                                lstFiltros.clear();
                                lstFiltros.add(new Filtro(ArticulosSidra.CAMPO_ARTICULO, Filtro.EQ, objDatos.getListadoSeries().get(a).getIdArticulo()));
                                lstFiltros.add(new Filtro("UPPER(" + ArticulosSidra.CAMPO_ESTADO + ")", Filtro.EQ, "'" + estadoAlta.toUpperCase() + "'"));
                                lstFiltros.add(new Filtro(ArticulosSidra.CAMPO_TCSCCATPAISID, Filtro.EQ, ID_PAIS));

                                lstArticulos = OperacionVentasCopia.getCatArticulo(conn, lstFiltros, ID_PAIS);

                                // si el articulo no es encontrado en el catalogo se indicar\u00E9 que no existe
                                if (lstArticulos.isEmpty()) {
                                    objSerie = new Series();

                                    objSerie.setDescripcionArticulo("NO ENCONTRADO");
                                    objSerie.setEstado("RANGO INVALIDO");
                                    objSerie.setTipoInventario("NO ENCONTRADO");
                                    objSerie.setGrupo("NO ENCONTRADO");
                                    objSerie.setIdArticulo(objDatos.getListadoSeries().get(a).getIdArticulo());
                                    objSerie.setSerieInicial(objDatos.getListadoSeries().get(a).getSerieInicial());
                                    objSerie.setSerieFinal(objDatos.getListadoSeries().get(a).getSerieFinal());
                                    lstSerieInvalidas.add(objSerie);
                                } else {
                                    objSerie = new Series();

                                    objSerie.setDescripcionArticulo(lstArticulos.get(0).getDescripcion());
                                    objSerie.setEstado("NO EXISTE");
                                    objSerie.setTipoInventario(tipoInv);
                                    objSerie.setGrupo(lstArticulos.get(0).getTipo_grupo_sidra());
                                    objSerie.setIdArticulo("" + lstArticulos.get(0).getArticulo());
                                    objSerie.setSerieInicial(objDatos.getListadoSeries().get(a).getSerieInicial());
                                    objSerie.setSerieFinal(objDatos.getListadoSeries().get(a).getSerieFinal());
                                    lstSerieInvalidas.add(objSerie);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().getName(), nombreMetodo,
                        "", objDatos.getCodArea());

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, Conf.LOG_VALIDA_SERIES, "0",
                        Conf.LOG_TIPO_NINGUNO, "Ocurrieron problemas al validar series", e.getMessage()));

            } catch (Exception e) {
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().getName(),
                        nombreMetodo, null, objDatos.getCodArea());

                log.error("Excepcion: " + e.getMessage(), e);
                objReturn.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, Conf.LOG_VALIDA_SERIES, "0",
                        Conf.LOG_TIPO_NINGUNO, "Ocurrieron problemas al validar series", e.getMessage()));

            } finally {
                DbUtils.closeQuietly(conn);
                objReturn.setArticulos(lstSerieInvalidas);

                if (lstSerieInvalidas.isEmpty()) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.OK_SERIES20, null, null, null, "", objDatos.getCodArea());
                } else {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SERIES_INVALIDAS_528, null, this.getClass().getName(),
                            nombreMetodo, "", objDatos.getCodArea());
                }
                objReturn.setRespuesta(objRespuesta);
            }

        } else {
            objReturn.setRespuesta(objRespuesta);
        }

        return objReturn;
    }
}

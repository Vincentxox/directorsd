package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.inventariomovil.InputConsultaInventarioMovil;
import com.consystec.sc.ca.ws.input.inventariomovil.InputDatosInventarioMovil;
import com.consystec.sc.ca.ws.input.inventariomovil.InputInventarioArticulosMovil;
import com.consystec.sc.ca.ws.input.inventariomovil.InputInventarioBodegasMovil;
import com.consystec.sc.ca.ws.input.inventariomovil.InputInventarioCampaniaCondiciones;
import com.consystec.sc.ca.ws.input.inventariomovil.InputInventarioGruposMovil;
import com.consystec.sc.ca.ws.input.inventariomovil.InputInventarioMovil;
import com.consystec.sc.ca.ws.input.inventariomovil.InputPrecioDescuentoArticulo;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.inventariomovil.OutputInventarioMovil;
import com.consystec.sc.sv.ws.metodos.CtrlInventarioMovil;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Condicion;
import com.consystec.sc.sv.ws.orm.CondicionOferta;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.OfertaCampania;
import com.consystec.sc.sv.ws.orm.OfertaCampaniaDet;
import com.consystec.sc.sv.ws.orm.PrecioArticulo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class OperacionInventarioMovil {
	private OperacionInventarioMovil(){}
    private static final Logger log = Logger.getLogger(OperacionInventarioMovil.class);

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param tipoRuta
     * @param tipoPanel
     * @param invTelca
     * @param invSidra
     * @param tipoOferta
     * @param tipoCondicionArticulo
     * @param tipoOfertaArticulo
     * @param estadoAlta
     * @param tipoCondicionTecnologia
     * @return OutputDescuento
     * @throws SQLException
     */
    public static OutputInventarioMovil doGet(Connection conn, InputConsultaInventarioMovil input, String invSidra,
            String invTelca, String tipoRuta, String estadoAlta, String tipoOfertaArticulo,
            String tipoCondicionArticulo, String tipoOferta, String tipoCondicionTecnologia, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta respuesta = null;
        OutputInventarioMovil output = null;
        String idArticulo = "";
        String idBodega = "";
        String tipoGrupo = "";
        String descripcion = "";
        String cantidad = "";
        String estado = "";
        String idSolicitud = "";
        String idAsignacionReserva = "";
        String tipoInv = "";
        String tecnologia = "";
        Statement stm = null;
        ResultSet rst = null;

        try {
            String articuloRecarga = UtileriasJava.getConfig(conn, Conf.GRUPO_ARTICULO_CANTIDAD, Conf.ARTICULO_RECARGA, input.getCodArea());
            if (articuloRecarga.equals("")) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, null, nombreClase,
                        nombreMetodo, "Falta la configuración de recargas.", input.getCodArea());

                output = new OutputInventarioMovil();
                output.setRespuesta(respuesta);
                return output;
            }

            List<Filtro> condiciones = CtrlInventarioMovil.obtenerCondiciones(input, idPais);
            // Filtro para evitar articulos vendidos del inventario
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NULL_AND, Inventario.CAMPO_TCSCVENTAID, null));

            // Se obtienen los datos de las bodegas
            List<InputInventarioBodegasMovil> datosBodegas = getBodegas(conn, input.getIdBodega(), estadoAlta, input.getCodArea(), idPais);

            // Se obtienen todos los datos de precios seg\u00FAn la version
            List<InputPrecioDescuentoArticulo> datosPrecios = getPrecios(conn, input, estadoAlta, idPais);

            // Se obtienen los datos de las ofertas de art\u00EDculo que aplican a la ruta o panel
            List<InputInventarioCampaniaCondiciones> datosCampaniaCondiciones = getCampaniaCondiciones(conn,
                    input.getIdTipo(), input.getTipo(), estadoAlta, tipoCondicionArticulo, tipoOferta,
                    tipoOfertaArticulo, false, true, "", "", input.getCodArea(), idPais);

            // Se obtienen los datos de las ofertas de tecnolog\u00EDa que aplican a la ruta o panel
            List<InputInventarioCampaniaCondiciones> datosCampaniaCondicionesTecnologia = getCampaniaCondiciones(conn,
                    input.getIdTipo(), input.getTipo(), estadoAlta, tipoCondicionArticulo, tipoOferta,
                    tipoCondicionTecnologia, false, true, "", "", input.getCodArea(), idPais);

            // Se obtienen los grupos del inventario
            List<Order> orden = new ArrayList<Order>();
            orden.add(new Order(Inventario.CAMPO_TIPO_GRUPO_SIDRA, Order.ASC));
            List<String> datosGrupos = UtileriasBD.getOneField(conn,
                    UtileriasJava.setSelect(Conf.SELECT_DISTINCT, Inventario.CAMPO_TIPO_GRUPO_SIDRA),
                    ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodega(), input.getCodArea()), condiciones, orden);
            datosGrupos.add(Conf.TIPO_GRUPO_BONO);
            datosGrupos.add(Conf.TIPO_GRUPO_RECARGA);

            // Se establece el orden de los datos a mostrar
            orden.clear();
            orden.add(new Order(Inventario.CAMPO_DESCRIPCION, Order.ASC));

            // Se obtienen los campos que se consultar\u00E9n del inventario
            String[] campos = getCamposInv(false, "");

            // Se arma query para art\u00EDculos de Telca
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Inventario.CAMPO_TIPO_INV, invTelca.toUpperCase()));
            
            String sql = "SELECT ";
            sql += UtileriasBD.getCampos(campos);
            sql += " FROM " + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodega(), input.getCodArea());
            sql += UtileriasBD.getCondiciones(condiciones);

            // Se arma query para art\u00EDculos de Sidra (promocionales)
            condiciones.clear();
            condiciones = CtrlInventarioMovil.obtenerCondiciones(input, idPais);
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_AND, Inventario.N_TABLA, Inventario.CAMPO_TIPO_INV, invSidra.toUpperCase()));

            sql += " UNION SELECT ";
            sql += UtileriasBD.getCampos(campos);
            sql += " FROM " + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodega(), input.getCodArea());
            sql += UtileriasBD.getCondiciones(condiciones);
            
            // Se arma filtro para obtener la bodega origen
            String selectIdBodega = "SELECT TCSCBODEGAVIRTUALID FROM ";
            if (input.getTipo().equalsIgnoreCase(tipoRuta)) {
                selectIdBodega += "TC_SC_RUTA WHERE TCSCRUTAID = ";
            } else {
                selectIdBodega += "TC_SC_PANEL WHERE TCSCPANELID = ";
            }
            selectIdBodega += input.getIdTipo() + " AND UPPER(ESTADO) = '" + estadoAlta.toUpperCase() + "' AND TCSCCATPAISID = " + idPais;

            selectIdBodega = "SELECT IDBODEGA_ORIGEN FROM " + ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea())
                    + " WHERE TCSCBODEGAVIRTUALID IN (" + selectIdBodega + ") AND TCSCCATPAISID = " + idPais;
            
            // Se arma query para consultar art\u00EDculos de recarga y bonos
            condiciones.clear();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, selectIdBodega));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, idPais.toString()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_OR, Inventario.CAMPO_ARTICULO, articuloRecarga));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Inventario.CAMPO_TIPO_GRUPO_SIDRA, Conf.TIPO_GRUPO_BONO));

            sql += " UNION SELECT ";
            sql += UtileriasBD.getCampos(getCamposInv(true, input.getIdBodega()));
            sql += " FROM " + ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodega(), input.getCodArea());
            sql += UtileriasBD.getCondiciones(condiciones);

            log.debug("QryInventarioMovil: " + sql);

            stm = conn.createStatement();
            rst = stm.executeQuery(sql);

            List<Map<String, String>> datosArticulos = new ArrayList<Map<String, String>>();
            List<Map<String, String>> datosInventario = new ArrayList<Map<String, String>>();

            if (rst.next()) {
                do {
                    Map<String, String> datos = new HashMap<String, String>();
                    Map<String, String> datosCopia = new HashMap<String, String>();

                    for (int i = 0; i < campos.length; i++) {
                        datos.put(campos[i], rst.getString(i + 1));
                        datosCopia.put(campos[i], rst.getString(i + 1));
                    }
                    datosArticulos.add(datos);
                    datosInventario.add(datosCopia);
                } while (rst.next());
            }

            // Se obtienen datos del inventario
            boolean val1=(datosBodegas == null || datosBodegas.size() < 1 || datosBodegas.isEmpty() || datosGrupos == null);
            boolean val2=( datosGrupos.size() < 1 || datosGrupos.isEmpty() || datosArticulos == null);
            boolean val3=(datosArticulos.size() < 1 || datosArticulos.isEmpty() || datosInventario == null);
            boolean val4=(datosInventario.size() < 1 || datosInventario.isEmpty());
            if (val1 ||val2 || val3 || val4) {
                respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_INVENTARIO_397, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                output = new OutputInventarioMovil();
                output.setRespuesta(respuesta);
            } else {
                List<InputInventarioMovil> inventario = new ArrayList<InputInventarioMovil>();
                InputInventarioMovil itemInventario = new InputInventarioMovil();

                List<InputInventarioBodegasMovil> listBodegas = new ArrayList<InputInventarioBodegasMovil>();
                List<InputInventarioBodegasMovil> bodegas = new ArrayList<InputInventarioBodegasMovil>();
                InputInventarioBodegasMovil bodega = new InputInventarioBodegasMovil();

                List<InputInventarioArticulosMovil> articulos = new ArrayList<InputInventarioArticulosMovil>();
                InputInventarioArticulosMovil articulo = new InputInventarioArticulosMovil();

                List<InputDatosInventarioMovil> detalles = new ArrayList<InputDatosInventarioMovil>();
                InputDatosInventarioMovil detalle = new InputDatosInventarioMovil();

                // Se ordenan los datos de los grupos en la bodega
                for (int i = 0; i < datosBodegas.size(); i++) {
                    for (String nombreGrupo : datosGrupos) {
                        bodega = new InputInventarioBodegasMovil();

                        bodega.setIdBodega(datosBodegas.get(i).getIdBodega());
                        bodega.setNombreBodega(datosBodegas.get(i).getNombreBodega());
                        bodega.setGrupo(nombreGrupo);

                        bodegas.add(bodega);
                    }
                }

                // --------------------------------------------------------------------
                for (int i = 0; i < datosArticulos.size(); i++) {
                    articulo = new InputInventarioArticulosMovil();
                    detalles = new ArrayList<InputDatosInventarioMovil>();

                    idBodega = datosArticulos.get(i).get(Inventario.CAMPO_TCSCBODEGAVIRTUALID);
                    tipoGrupo = datosArticulos.get(i).get(Inventario.CAMPO_TIPO_GRUPO_SIDRA);
                    idArticulo = datosArticulos.get(i).get(Inventario.CAMPO_ARTICULO);
                    cantidad = datosArticulos.get(i).get(Inventario.CAMPO_CANTIDAD);
                    descripcion = datosArticulos.get(i).get(Inventario.CAMPO_DESCRIPCION);
                    estado = datosArticulos.get(i).get(Inventario.CAMPO_ESTADO);
                    idSolicitud = UtileriasJava.getValue(datosArticulos.get(i).get(Inventario.CAMPO_TCSCSOLICITUDID));
                    idAsignacionReserva = UtileriasJava.getValue(datosArticulos.get(i).get(Inventario.CAMPO_TCSCASIGNACIONRESERVAID));
                    tipoInv = datosArticulos.get(i).get(Inventario.CAMPO_TIPO_INV);
                    tecnologia = UtileriasJava.getValue(datosArticulos.get(i).get(Inventario.CAMPO_TECNOLOGIA));

                    articulo.setIdBodega(idBodega);
                    articulo.setGrupo(tipoGrupo);
                    articulo.setIdArticulo(idArticulo);
                    articulo.setDescripcion((datosArticulos.get(i).get(Inventario.CAMPO_TECNOLOGIA) != null
                            && !datosArticulos.get(i).get(Inventario.CAMPO_TECNOLOGIA).equals(""))
                                    ? datosArticulos.get(i).get(Inventario.CAMPO_TECNOLOGIA) + "| " + descripcion : descripcion);
                    articulo.setCantidad(cantidad);
                    articulo.setSeriado(datosArticulos.get(i).get(Inventario.CAMPO_SERIADO));
                    articulo.setEstado(estado);
                    articulo.setIdSolicitud(idSolicitud);
                    articulo.setIdAsignacionReserva(idAsignacionReserva);
                    articulo.setTipoInv(tipoInv);
                    articulo.setTecnologia(tecnologia);

                    articulo.setDetallePrecios(getDetallePrecios(idArticulo, tipoInv, datosPrecios, invSidra));
                    articulo.setDescuentos(getDescuentos(idArticulo, tecnologia, datosCampaniaCondiciones, datosCampaniaCondicionesTecnologia));

                    if (articulo.getDetallePrecios() != null && articulo.getDetallePrecios().size() > 0) {
                        for (int j = 0; j < datosInventario.size(); j++) {
                            detalle = new InputDatosInventarioMovil();
                            if (idArticulo.equals(datosInventario.get(j).get(Inventario.CAMPO_ARTICULO))
                                    && idBodega.equals(datosInventario.get(j).get(Inventario.CAMPO_TCSCBODEGAVIRTUALID))
                                    && descripcion.equals(datosInventario.get(j).get(Inventario.CAMPO_DESCRIPCION))
                                    && tipoGrupo.equals(datosInventario.get(j).get(Inventario.CAMPO_TIPO_GRUPO_SIDRA))
                                    && estado.equals(datosInventario.get(j).get(Inventario.CAMPO_ESTADO))
                                    && tipoInv.equals(datosInventario.get(j).get(Inventario.CAMPO_TIPO_INV))
                                 ) {
                                detalle.setIdInventario(datosInventario.get(j).get(Inventario.N_TABLA_ID));
                                detalle.setIdSolicitud(datosInventario.get(j).get(Inventario.CAMPO_TCSCSOLICITUDID));
                                detalle.setIdAsignacionReserva(datosInventario.get(j).get(Inventario.CAMPO_TCSCASIGNACIONRESERVAID));
                                detalle.setNumTelefono(UtileriasJava.getValue(datosInventario.get(j).get(Inventario.CAMPO_NUM_TELEFONO)));
                                detalle.setIcc(UtileriasJava.getValue(datosInventario.get(j).get(Inventario.CAMPO_ICC)));
                                detalle.setImei(UtileriasJava.getValue(datosInventario.get(j).get(Inventario.CAMPO_IMEI)));                                
                                detalle.setEstadoComercial(datosInventario.get(j).get(Inventario.CAMPO_ESTADO_COMERCIAL));
                                detalle.setSerieAsociada(datosInventario.get(j).get(Inventario.CAMPO_SERIE_ASOCIADA));
                                detalle.setTipoInv(datosInventario.get(j).get(Inventario.CAMPO_TIPO_INV));
                                detalle.setEstado(datosInventario.get(j).get(Inventario.CAMPO_ESTADO));
                                detalle.setCreado_el(UtileriasJava.formatStringDate(datosInventario.get(j).get(Inventario.CAMPO_CREADO_EL)));
                                detalle.setCreado_por(datosInventario.get(j).get(Inventario.CAMPO_CREADO_POR));
                                detalle.setModificado_el(UtileriasJava.formatStringDate(datosInventario.get(j).get(Inventario.CAMPO_MODIFICADO_EL)));
                                detalle.setModificado_por(datosInventario.get(j).get(Inventario.CAMPO_MODIFICADO_POR));
                                
                                //-validamos la serie para articulos SIMCARD  jcsimon
                                if (datosInventario.get(j).get(Inventario.CAMPO_TIPO_GRUPO_SIDRA).equalsIgnoreCase(Conf.TIPO_GRUPO_SIMCAR))
                                {
                                	detalle.setSerie(datosInventario.get(j).get(Inventario.CAMPO_SERIE).substring(0,18));
                                }
                                else
                                {
                                	detalle.setSerie(datosInventario.get(j).get(Inventario.CAMPO_SERIE));
                                }
                                //-Fin del cambio	

                                
                                detalles.add(detalle);
                                datosInventario.get(j).clear();
                            }
                        }
                    }

                    if (detalles.size() > 0) {
                        articulo.setDetalleArticulo(detalles);
                        articulos.add(articulo);
                    }
                }
                // --------------------------------------------------------------------

                // --------------------------------------------------------------------
                List<InputInventarioGruposMovil> grupos = new ArrayList<InputInventarioGruposMovil>();
                for (int i = 0; i < bodegas.size(); i++) {
                    tipoGrupo = bodegas.get(i).getGrupo();
                    idBodega = bodegas.get(i).getIdBodega();

                    InputInventarioGruposMovil grupo = new InputInventarioGruposMovil();
                    List<InputInventarioArticulosMovil> listArticulos = new ArrayList<InputInventarioArticulosMovil>();

                    for (InputInventarioArticulosMovil itemArticulo : articulos) {
                        if (itemArticulo.getGrupo() != null && itemArticulo.getGrupo().equals(tipoGrupo)
                                && itemArticulo.getIdBodega() != null && itemArticulo.getIdBodega().equals(idBodega)) {
                            itemArticulo.setIdBodega(null);
                            itemArticulo.setIdSolicitud(null);
                            itemArticulo.setIdAsignacionReserva(null);
                            itemArticulo.setGrupo(null);
                            itemArticulo.setEstado(null);

                            listArticulos.add(itemArticulo);
                        }
                    }

                    if (listArticulos.size() > 0) {
                        grupo.setIdBodega(idBodega);
                        grupo.setGrupo(tipoGrupo);
                        grupo.setArticulos(listArticulos);
                        grupos.add(grupo);
                    }
                }
                // --------------------------------------------------------------------

                // --------------------------------------------------------------------
                for (int i = 0; i < datosBodegas.size(); i++) {
                    idBodega = datosBodegas.get(i).getIdBodega();

                    bodega = new InputInventarioBodegasMovil();
                    List<InputInventarioGruposMovil> listGrupos = new ArrayList<InputInventarioGruposMovil>();

                    for (InputInventarioGruposMovil itemGrupo : grupos) {
                        if (itemGrupo.getIdBodega() != null && itemGrupo.getIdBodega().equals(idBodega)) {
                            itemGrupo.setIdBodega(null);
                            listGrupos.add(itemGrupo);
                        }
                    }

                    if (listGrupos.size() > 0) {
                        bodega.setIdBodega(idBodega);
                        bodega.setNombreBodega(datosBodegas.get(i).getNombreBodega());
                        bodega.setGrupos(listGrupos);
                        listBodegas.add(bodega);
                    }
                }
                // --------------------------------------------------------------------
                
                itemInventario.setBodegas(listBodegas);
                inventario.add(itemInventario);
                output = new OutputInventarioMovil();

                if (listBodegas.size() > 0) {
                    respuesta = new Respuesta();
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_INV_MOVIL19, null, nombreClase,
                            nombreMetodo, null, input.getCodArea());

                    output.setInventario(inventario);
                } else {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_INVENTARIO_397, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());
                }
                output.setRespuesta(respuesta);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(stm);
        }

        return output;
    }

    private static String[] getCamposInv(boolean esRecargaBono, String idBodega) {
        String campos[] = {
            Inventario.N_TABLA_ID,
            esRecargaBono == false ? Inventario.CAMPO_TCSCBODEGAVIRTUALID : idBodega,
            Inventario.CAMPO_ARTICULO,
            Inventario.CAMPO_SERIE,
            Inventario.CAMPO_DESCRIPCION,
            Inventario.CAMPO_CANTIDAD,
            Inventario.CAMPO_ESTADO_COMERCIAL,
            Inventario.CAMPO_SERIE_ASOCIADA,
            Inventario.CAMPO_SERIADO,
            Inventario.CAMPO_TIPO_INV,
            Inventario.CAMPO_TIPO_GRUPO_SIDRA,
            Inventario.CAMPO_IDVENDEDOR,
            Inventario.CAMPO_TECNOLOGIA,
            Inventario.CAMPO_ESTADO,
            Inventario.CAMPO_CREADO_EL,
            Inventario.CAMPO_CREADO_POR,
            Inventario.CAMPO_MODIFICADO_EL,
            Inventario.CAMPO_MODIFICADO_POR,
            Inventario.CAMPO_TCSCSOLICITUDID,
            Inventario.CAMPO_TCSCASIGNACIONRESERVAID,
            Inventario.CAMPO_NUM_TELEFONO,
            Inventario.CAMPO_ICC,
            Inventario.CAMPO_IMEI                       
        };

        return campos;
    }
    
    private static List<InputPrecioDescuentoArticulo> getDetallePrecios(String idArticulo, String tipoInv,
            List<InputPrecioDescuentoArticulo> datosPrecios, String invSidra) {
        List<InputPrecioDescuentoArticulo> preciosArticulo = new ArrayList<InputPrecioDescuentoArticulo>();
        List<InputPrecioDescuentoArticulo> preciosBorrar = new ArrayList<InputPrecioDescuentoArticulo>();

        for (int j = 0; j < datosPrecios.size(); j++) {
            if (datosPrecios.get(j).getIdArticulo().equals(idArticulo)) {
                preciosArticulo.add(datosPrecios.get(j));
                preciosBorrar.add(datosPrecios.get(j));
            }
        }

        datosPrecios.removeAll(preciosBorrar);

        if (preciosArticulo.size() > 0) {
            return preciosArticulo;
        } else if (tipoInv.equals(invSidra)) {
            InputPrecioDescuentoArticulo precioVacio = new InputPrecioDescuentoArticulo();
            preciosArticulo.add(precioVacio);
            return preciosArticulo;
        } else {
            return null;
        }
    }

    private static List<InputPrecioDescuentoArticulo> getDescuentos(String idArticulo, String tecnologia,
            List<InputInventarioCampaniaCondiciones> datosCampaniaCondiciones,
            List<InputInventarioCampaniaCondiciones> datosCampaniaCondicionesTecnologia) {
        List<InputPrecioDescuentoArticulo> descuentosArticulo = new ArrayList<InputPrecioDescuentoArticulo>();
        List<InputInventarioCampaniaCondiciones> descuentosBorrar = new ArrayList<InputInventarioCampaniaCondiciones>();

        if (datosCampaniaCondiciones != null && !datosCampaniaCondiciones.isEmpty()) {
            for (int j = 0; j < datosCampaniaCondiciones.size(); j++) {
                if (idArticulo.equals(datosCampaniaCondiciones.get(j).getIdArticulo())) {
                    InputPrecioDescuentoArticulo descuento = new InputPrecioDescuentoArticulo();

                    descuento.setTipo(datosCampaniaCondiciones.get(j).getTipoOferta());
                    descuento.setTecnologia(datosCampaniaCondiciones.get(j).getTecnologia());
                    descuento.setTipoDescuento(datosCampaniaCondiciones.get(j).getTipoDescuento());
                    descuento.setValorDescuento(datosCampaniaCondiciones.get(j).getValorDescuento());
                    descuento.setTipoGestion(datosCampaniaCondiciones.get(j).getTipoGestion());
                    descuento.setTipoCliente(datosCampaniaCondiciones.get(j).getTipoCliente());
                    descuento.setIdOfertaCampania(datosCampaniaCondiciones.get(j).getIdOfertaCampania());
                    descuento.setNombreCampania(datosCampaniaCondiciones.get(j).getNombreCampania());
                    descuento.setNombreCondicion(datosCampaniaCondiciones.get(j).getNombreCondicion());
                    descuento.setIdCondicion(datosCampaniaCondiciones.get(j).getIdCondicion());
                    
                    descuentosArticulo.add(descuento);
                    descuentosBorrar.add(datosCampaniaCondiciones.get(j));
                }
            }

            if (!descuentosBorrar.isEmpty()) {
                datosCampaniaCondiciones.removeAll(descuentosBorrar);
            }
        }

        descuentosBorrar.clear();

        if ((descuentosArticulo.isEmpty() && (tecnologia != null && !"".equals(tecnologia))) &&  (datosCampaniaCondicionesTecnologia != null && !datosCampaniaCondicionesTecnologia.isEmpty())) {
                for (int j = 0; j < datosCampaniaCondicionesTecnologia.size(); j++) {
                    if (tecnologia.equals(datosCampaniaCondicionesTecnologia.get(j).getTecnologia())) {
                        InputPrecioDescuentoArticulo descuento = new InputPrecioDescuentoArticulo();

                        descuento.setTipo(datosCampaniaCondicionesTecnologia.get(j).getTipoOferta());
                        descuento.setTecnologia(datosCampaniaCondicionesTecnologia.get(j).getTecnologia());
                        descuento.setTipoDescuento(datosCampaniaCondicionesTecnologia.get(j).getTipoDescuento());
                        descuento.setValorDescuento(datosCampaniaCondicionesTecnologia.get(j).getValorDescuento());
                        descuento.setTipoGestion(datosCampaniaCondicionesTecnologia.get(j).getTipoGestion());
                        descuento.setTipoCliente(datosCampaniaCondicionesTecnologia.get(j).getTipoCliente());
                        descuento.setIdOfertaCampania(datosCampaniaCondicionesTecnologia.get(j).getIdOfertaCampania());
                        descuento.setNombreCampania(datosCampaniaCondicionesTecnologia.get(j).getNombreCampania());

                        descuentosArticulo.add(descuento);
                    }
                }
        }

        if (descuentosArticulo.isEmpty()) {
            InputPrecioDescuentoArticulo descuentoVacio = new InputPrecioDescuentoArticulo();
            descuentosArticulo.add(descuentoVacio);
        }

        return descuentosArticulo;
    }

    private static List<InputPrecioDescuentoArticulo> getPrecios(Connection conn, InputConsultaInventarioMovil input,
            String estadoAlta, BigDecimal idPais) throws SQLException {
        InputPrecioDescuentoArticulo item = new InputPrecioDescuentoArticulo();
        List<InputPrecioDescuentoArticulo> list = new ArrayList<InputPrecioDescuentoArticulo>();

        PreparedStatement pstmt = null;
        ResultSet rst = null;

        try {
            String campos[] = {
	            PrecioArticulo.CAMPO_ARTICULO,
	            PrecioArticulo.CAMPO_PRECIO,
	            PrecioArticulo.CAMPO_DESCUENTO,
	            PrecioArticulo.CAMPO_TIPO_GESTION,
	            PrecioArticulo.CAMPO_VERSION,
	            PrecioArticulo.CAMPO_TASA_CAMBIO
	        };

            List<Order> orden = new ArrayList<Order>();
            orden.add(new Order(PrecioArticulo.CAMPO_VERSION, Order.DESC));

            List<Filtro> condiciones = new ArrayList<Filtro>();
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PrecioArticulo.CAMPO_TCSCCATPAISID, idPais.toString()));
	        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PrecioArticulo.CAMPO_ESTADO, estadoAlta));
            if (input.getVersion() != null && !"".equals(input.getVersion())) {
                    condiciones.add(new Filtro(PrecioArticulo.CAMPO_VERSION, Filtro.GTE, input.getVersion()));
            }

            String sql = UtileriasBD.armarQuerySelect(PrecioArticulo.N_TABLA, campos, condiciones, orden);

            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    log.debug("No existen precios.");
                } else {
                    do {
                        item = new InputPrecioDescuentoArticulo();
                        item.setIdArticulo(rst.getString(PrecioArticulo.CAMPO_ARTICULO));
                        item.setPrecioSCL(""+(rst.getDouble(PrecioArticulo.CAMPO_PRECIO)));
                        item.setDescuentoSCL(rst.getString(PrecioArticulo.CAMPO_DESCUENTO) == null ? "0"
                                : rst.getString(PrecioArticulo.CAMPO_DESCUENTO));
                        item.setTipoGestion(rst.getString(PrecioArticulo.CAMPO_TIPO_GESTION) == null ? ""
                                : rst.getString(PrecioArticulo.CAMPO_TIPO_GESTION));
                        item.setVersion(rst.getString(PrecioArticulo.CAMPO_VERSION));

                        list.add(item);
                    } while (rst.next());
                }
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return list;
    }

    private static List<InputInventarioBodegasMovil> getBodegas(Connection conn, String idBodega, String estadoAlta, String codArea, BigDecimal idPais)
            throws SQLException {
        String nombreMetodo = "getBodegas";
        String nombreClase = new CurrentClassGetter().getClassName();

        InputInventarioBodegasMovil item = new InputInventarioBodegasMovil();
        List<InputInventarioBodegasMovil> list = new ArrayList<InputInventarioBodegasMovil>();

        PreparedStatement pstmt = null;
        ResultSet rst = null;

     
	        String sql = querygetbodegas(idBodega, codArea);

            log.debug("QryBodegas: " + sql);
         try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,  estadoAlta.toUpperCase());
            pstmt.setBigDecimal(2,  idPais);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    log.debug("No existen registros en la tabla con esos par\u00E9metros.");

                    Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_INVENTARIO_397,
                            null, nombreClase, nombreMetodo, null, codArea);

                    item.setNombreBodega(respuesta.getDescripcion());
                } else {
                    do {
                        item = new InputInventarioBodegasMovil();
                        item.setIdBodega(rst.getString(BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID));
                        item.setNombreBodega(rst.getString(BodegaVirtual.CAMPO_NOMBRE));

                        list.add(item);
                    } while (rst.next());
                }
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return list;
    }
    
    public static String querygetbodegas( String idBodega, String codArea) {
    	  String sql = "SELECT TCSCBODEGAVIRTUALID, NOMBRE"
  				+ " FROM " + ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", codArea)
                  + " WHERE UPPER(ESTADO) = ?"
                  + " AND TCSCBODEGAVIRTUALID IN (" + idBodega + ")"
	                + " AND TCSCCATPAISID = ? ";

    	  return sql;
    }

    public static List<InputInventarioCampaniaCondiciones> getCampaniaCondiciones(Connection conn, String idTipo,
            String tipo, String estadoAlta, String tipoCondicionArticulo, String tipoOferta, String tipoOfertaArticulo,
            boolean esOrigenVenta, boolean modoOnline, String tipoCliente, String tipoOfertaTecnologia, String codArea, BigDecimal idPais) throws SQLException {
        InputInventarioCampaniaCondiciones item = new InputInventarioCampaniaCondiciones();
        List<InputInventarioCampaniaCondiciones> list = new ArrayList<InputInventarioCampaniaCondiciones>();
        estadoAlta = estadoAlta.toUpperCase();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String sql = "";

      
            sql = queryGetCampania(  idTipo,
                     tipo,  estadoAlta,  tipoCondicionArticulo,  tipoOferta,  tipoOfertaArticulo,
                     esOrigenVenta,  modoOnline,  tipoCliente,  tipoOfertaTecnologia, codArea, idPais) ;
           
          try {
            log.debug("QryArticulosCondiciones: " + sql);

            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    log.debug("No existen registros en la tabla con esos par\u00E9metros.");
                } else {
                    do {
                        item = new InputInventarioCampaniaCondiciones();
                        item.setIdOfertaCampania(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO, "IDCAMPANIA"));
                        item.setNombreCampania(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO,"NOMBRE_CAMPANIA"));
                        item.setTipoGestion(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO,"TIPO_GESTION"));
                        item.setIdArticulo(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO,"ART_CONDICION"));
                        item.setTipoOferta(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_TIPO_OFERTA));
                        item.setTipoCliente(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_TIPO_CLIENTE));
                        item.setTecnologia(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_TECNOLOGIA));
                        item.setIdPDV(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_TCSCPUNTOVENTAID));
                        item.setCategoria(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_CATEGORIA));
                        item.setZona(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_ZONACOMERCIAL));
                        item.setTipoDescuento(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, CondicionOferta.CAMPO_TIPO_DESCUENTO));
                        item.setValorDescuento(UtileriasJava.getRstValue(rst, Conf.TIPO_NUMERICO,CondicionOferta.CAMPO_VALOR_DESCUENTO));
                        item.setNombreCondicion(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO,"NOMBRE_CONDICION"));
                        item.setIdCondicion(UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO,"ID_CONDICION"));

                        list.add(item);
                    } while (rst.next());
                }
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return list;
    }
    
    public static String queryGetCampania( String idTipo,
            String tipo, String estadoAlta, String tipoCondicionArticulo, String tipoOferta, String tipoOfertaArticulo,
            boolean esOrigenVenta, boolean modoOnline, String tipoCliente, String tipoOfertaTecnologia, String codArea, BigDecimal idPais) {
    	 String sql = "SELECT "
                  + "E.TCSCOFERTACAMPANIAID AS IDCAMPANIA, "
                  + "(SELECT G.NOMBRE "
                      + "FROM " + ControladorBase.getParticion(OfertaCampania.N_TABLA, Conf.PARTITION, "", codArea) + " G "
                      + "WHERE G.TCSCOFERTACAMPANIAID = E.TCSCOFERTACAMPANIAID AND G.TCSCCATPAISID = " + idPais + ") "
                      + "AS NOMBRE_CAMPANIA, "
                  + "E.TIPO_GESTION, "
                  + "E.NOMBRE AS NOMBRE_CONDICION, "
                  + "E.TCSCCONDICIONID AS ID_CONDICION, "
                  + "F.ARTICULO AS ART_CONDICION, "
                  + "F.TIPO_OFERTA, "
                  + "F.TIPO_CLIENTE, "
                  + "F.TECNOLOGIA, "
                  + "F.TCSCPUNTOVENTAID, "
                  + "F.CATEGORIA, "
                  + "F.ZONACOMERCIAL, "
                  + "F.TIPO_DESCUENTO, "
                  + "F.VALOR_DESCUENTO "
                  + "FROM " + Condicion.N_TABLA + " E, " + CondicionOferta.N_TABLA + " F "
                  + "WHERE E.TCSCOFERTACAMPANIAID IN "
                      + "(SELECT D.TCSCOFERTACAMPANIAID "
                          + "FROM " + OfertaCampania.N_TABLA + " D "
                          + "WHERE D.TCSCOFERTACAMPANIAID IN "
                          + "(SELECT C.TCSCOFERTACAMPANIAID "
                              + "FROM " + OfertaCampaniaDet.N_TABLA + " C "
                              + "WHERE C.TCSCCATPAISID = " + idPais
                                  + " AND C.TCSCTIPOID = " + idTipo + " AND UPPER(C.TIPO) = '" + tipo.toUpperCase() + "'"
                                  + (modoOnline ? " AND UPPER(C.ESTADO) = '" + estadoAlta + "'" : "")
                          + ") AND D.TCSCCATPAISID = " + idPais
                          + (modoOnline ? " AND UPPER(D.ESTADO) = '" + estadoAlta + "'"
                              + " AND TRUNC(SYSDATE) BETWEEN TRUNC(D.FECHA_DESDE) AND TRUNC(D.FECHA_HASTA)" : "")
                      + ") "
                      + "AND E.TCSCCATPAISID = " + idPais + " AND F.TCSCCATPAISID = E.TCSCCATPAISID "
                      + (modoOnline ? "AND UPPER(E.ESTADO) = '" + estadoAlta + "' " : " ")
                      + (!esOrigenVenta ? "AND UPPER(E.TIPO_CONDICION) = '" + tipoCondicionArticulo.toUpperCase() + "' " : "")
                      + "AND UPPER(E.TIPO_OFERTACAMPANIA) = UPPER('" + tipoOferta + "') "
                      + "AND F.TCSCCONDICIONID = E.TCSCCONDICIONID"
                      + (esOrigenVenta ? " AND (UPPER(F.TIPO_CLIENTE) = '" + tipoCliente + "' OR UPPER(F.TIPO_CLIENTE) = 'AMBOS' OR F.TIPO_CLIENTE IS NULL)" : "")
                      + (modoOnline ? " AND UPPER(F.ESTADO) = '" + estadoAlta + "' " : "")
                      + (!esOrigenVenta ? " AND UPPER(F.TIPO_OFERTA) = '" + tipoOfertaArticulo.toUpperCase() + "'"
                              : " AND UPPER(F.TIPO_OFERTA) != '" + tipoOfertaTecnologia.toUpperCase() + "'");
    	 
    	 return sql;
    }
}

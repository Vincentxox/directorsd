package com.consystec.sc.sv.ws.metodos;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.descuento.InputDescuento;
import com.consystec.sc.ca.ws.input.descuento.InputDescuentoDet;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.descuento.OutputDescuento;
import com.consystec.sc.sv.ws.operaciones.OperacionDescuento;
import com.consystec.sc.sv.ws.orm.Articulos;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.Descuento;
import com.consystec.sc.sv.ws.orm.DescuentoDet;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 * <br>Servicios que no se utilizan.
 */
public class CtrlDescuento extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlDescuento.class);
    
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al servicio.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return Respuesta
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputDescuento input, int metodo) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        int numeroPanel = 0;
        String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

        log.debug("Validando datos...");

        String campos[] = { Catalogo.CAMPO_NOMBRE, Catalogo.CAMPO_VALOR };

        // Se obtienen todas las configuraciones.
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_DESCUENTOS));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_PANELRUTA));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Catalogo.CAMPO_GRUPO, Conf.GRUPO_ESTADOS));

        List<Map<String, String>> datosConfig = new ArrayList<Map<String, String>>();
        try {
            datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);
        } catch (SQLException e) {
            log.error("Error al obtener par\u00E9metros de configuraci\u00F3n. " + e);
            r = new ControladorBase().getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, null,
                nombreClase, nombreMetodo, "Error al obtener par\u00E9metros de configuración.", input.getCodArea());
            datosErroneos = r.getDescripcion();
            flag = true;
        }
        
        String campania = "";
        String promocion = "";
        String porcenaje = "";
        String monto = "";
        String mayor = "";
        String menor = "";
        String igual = "";
        String tipoPanel = "";
        String tipoRuta = "";
        String estadoBaja = "";
        
        for (int i = 0; i < datosConfig.size(); i++) {
            if (Conf.TIPO_CAMPANIA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                campania = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.TIPO_PROMOCION.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                promocion = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.TIPO_PORCENTAJE.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                porcenaje = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.TIPO_MONTO.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                monto = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.DESCU_CONFIG_MAYOR.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                mayor = datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE);
            }
            if (Conf.DESCU_CONFIG_MENOR.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                menor = datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE);
            }
            if (Conf.DESCU_CONFIG_IGUAL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                igual = datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE);
            }
            if (Conf.TIPO_PANEL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                tipoPanel = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.TIPO_RUTA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                tipoRuta = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.ESTADO_BAJA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                estadoBaja = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
        }
        
        if (metodo == Conf.METODO_PUT) {
            if (input.getEstado() == null || "".equals(input.getEstado().trim())) {
                datosErroneos += " Estado.";
                flag = true;
            } else if (!input.getEstado().equalsIgnoreCase(estadoAlta)
                    && !input.getEstado().equalsIgnoreCase(estadoBaja)) {
                datosErroneos += " Estado debe especificarse como " + estadoAlta + " o " + estadoBaja + ".";
                flag = true;
            }
        }
        
        if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
            log.trace("ID del Descuento: " + input.getIdDescuento());
            if (input.getIdDescuento() == null || "".equals(input.getIdDescuento())) {
                datosErroneos += " ID del Descuento.";
                flag = true;
            } else if (!isNumeric(input.getIdDescuento())) {
                datosErroneos += " Datos no num\u00E9ricos en ID del Descuento.";
                flag = true;
            }
        }
        
        if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
            if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
                datosErroneos += " Usuario.";
                flag = true;
            }
            if (input.getTipo() == null || "".equals(input.getTipo().trim())) {
                datosErroneos += " Tipo.";
                flag = true;
            } else if (!input.getTipo().equalsIgnoreCase(campania)
                    && !input.getTipo().equalsIgnoreCase(promocion) ) {
                datosErroneos += " Tipo debe especificarse como " + campania + " o " + promocion + ".";
                flag = true;
            }
            
            if (input.getNombre() == null || "".equals(input.getNombre().trim())) {
                datosErroneos += " Nombre.";
                flag = true;
            }
            if (input.getDescripcion() == null || "".equals(input.getDescripcion().trim())) {
                datosErroneos += " Descripcion.";
                flag = true;
            }
            
            if (input.getTipoDescuento() == null || "".equals(input.getTipoDescuento().trim())) {
                datosErroneos += " Tipo Descuento.";
                flag = true;
            } else if (!input.getTipoDescuento().equalsIgnoreCase(porcenaje)
                    && !input.getTipoDescuento().equalsIgnoreCase(monto) ) {
                datosErroneos += " Tipo Descuento debe especificarse como " + porcenaje + " o " + monto + ".";
                flag = true;
            }
            if (input.getConfiguracion() == null || "".equals(input.getConfiguracion().trim())) {
                datosErroneos += " Configuraci\u00F3n.";
                flag = true;
            } else if (!input.getConfiguracion().equalsIgnoreCase(mayor)
                    && !input.getConfiguracion().equalsIgnoreCase(menor)
                    && !input.getConfiguracion().equalsIgnoreCase(igual) ) {
                datosErroneos += " Configuraci\u00F3n debe especificarse como '" + mayor + "', '" + menor + "' o '" + igual + "'.";
                flag = true;
            }
            if (input.getDescuento() == null || "".equals(input.getDescuento().trim())) {
                datosErroneos += " Descuento.";
                flag = true;
            } else if(!isDecimal(input.getDescuento())){
                datosErroneos += " Datos no num\u00E9ricos en Descuento.";
                flag = true;
            }
            
            if (input.getTipo() != null && input.getTipo().equalsIgnoreCase(campania)) {
                if (input.getConfRecarga() == null || "".equals(input.getConfRecarga().trim())) {
                    datosErroneos += " Configuraci\u00F3n de Recarga.";
                    flag = true;
                } else if (!input.getConfRecarga().equalsIgnoreCase(mayor)
                        && !input.getConfRecarga().equalsIgnoreCase(menor)
                        && !input.getConfRecarga().equalsIgnoreCase(igual) ) {
                    datosErroneos += " Configuraci\u00F3n de Recarga debe especificarse como '" + mayor + "', '" + menor + "' o '" + igual + "'.";
                    flag = true;
                }
                if (input.getRecarga() == null || "".equals(input.getRecarga().trim())) {
                    datosErroneos += " Recarga.";
                    flag = true;
                } else if(!isDecimal(input.getRecarga())){
                    datosErroneos += " Datos no num\u00E9ricos en Recarga.";
                    flag = true;
                }
                if (input.getConfPrecio() == null || "".equals(input.getConfPrecio().trim())) {
                    datosErroneos += " Configuraci\u00F3n de Precio.";
                    flag = true;
                } else if (!input.getConfPrecio().equalsIgnoreCase(mayor)
                        && !input.getConfPrecio().equalsIgnoreCase(menor)
                        && !input.getConfPrecio().equalsIgnoreCase(igual) ) {
                    datosErroneos += " Configuraci\u00F3n de Precio debe especificarse como '" + mayor + "', '" + menor + "' o '" + igual + "'.";
                    flag = true;
                }
                if (input.getPrecio() == null || "".equals(input.getPrecio().trim())) {
                    datosErroneos += " Precio.";
                    flag = true;
                } else if(!isDecimal(input.getPrecio())){
                    datosErroneos += " Datos no num\u00E9ricos en Precio.";
                    flag = true;
                }
                
                SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_INPUT);
                Date fechaDesde = null;
                Date fechaHasta = null;
                if (input.getFechaDesde() == null ||"".equals( input.getFechaDesde().trim())) {
                    datosErroneos += " Fecha Desde.";
                    flag = true;
                } else {
                    fechaDesde = UtileriasJava.parseDate(input.getFechaDesde(), formatoFecha);
                }
                if (input.getFechaHasta() == null || "".equals(input.getFechaHasta().trim())) {
                    datosErroneos += " Fecha Hasta.";
                    flag = true;
                } else {
                    fechaHasta = UtileriasJava.parseDate(input.getFechaHasta(), formatoFecha);
                }
                
                Calendar fecha = new GregorianCalendar();
                int anio = fecha.get(Calendar.YEAR);
                int mes = fecha.get(Calendar.MONTH);
                int dia = fecha.get(Calendar.DAY_OF_MONTH);

                if (metodo == Conf.METODO_POST) {
                    Date fechaHoy = UtileriasJava.parseDate(dia + "/" + (mes+1) + "/" + anio, formatoFecha);
                    if (fechaDesde != null && fechaDesde.compareTo(fechaHoy) < 0){
                            datosErroneos += " Fecha Desde debe ser igual o mayor a la Fecha Actual.";
                            flag = true;
                    }
                }
                
                if (fechaDesde != null && fechaHasta != null && fechaDesde.compareTo(fechaHasta)  > 0) {
                        datosErroneos += " Fecha Desde debe ser menor o igual a Fecha Hasta.";
                        flag = true;
                }

                if (input.getIdArticulo() == null || "".equals(input.getIdArticulo().trim())) {
                    datosErroneos += " ID del Art\u00EDculo.";
                    flag = true;
                } else {
                    //TODO cambiar entre paises
                    //* Salvador y Nicaragua
                    try {
                        condiciones.clear();
                        
                        //Salvador
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Articulos.CAMPO_TESARTICULOSINVID, input.getIdArticulo()));
                        condiciones.add(UtileriasJava.getCondicionDefault(Conf.FILTRO_ESTADO, Articulos.CAMPO_ESTADO, conn, input.getCodArea()));
                        
                        String existencia = UtileriasBD.verificarExistencia(conn, Articulos.N_TABLA, condiciones);
                        
                        if (new Integer(existencia) < 1){
                            datosErroneos += " El art\u00EDculo que envi\u00F3 no existe en los registros.";
                            flag = true;
                        }
                    } catch (SQLException e) {
                        log.error("Error al verificar existencia del art\u00EDculo. " + e);
                        r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DATOS_ARTICULO, null,
                            nombreClase, nombreMetodo, "Error al verificar existencia del artículo.", input.getCodArea());
                        datosErroneos = r.getDescripcion();
                        flag = true;
                    }
                    
                }
                if (input.getNombreArt() == null || "".equals(input.getNombreArt().trim())) {
                    datosErroneos += " Nombre del Art\u00EDculo.";
                    flag = true;
                }
                if (input.getPrecioArt() == null || "".equals(input.getPrecioArt().trim())) {
                    datosErroneos += " Precio del Art\u00EDculo.";
                    flag = true;
                }
                
                if (input.getDescuentoDet() != null) {
                    for (int i = 0; i < input.getDescuentoDet().size(); i++) {
                        if (flag) break;
                        String idTipo = "";
                        String tipo = "";
        
                        if (input.getDescuentoDet().get(i).getIdTipo() != null) {
                            idTipo = input.getDescuentoDet().get(i).getIdTipo().trim();
                        }
                        if (input.getDescuentoDet().get(i).getTipo() != null) {
                            tipo = input.getDescuentoDet().get(i).getTipo().trim();
                        }
                        
                        numeroPanel = i + 1;
                        if (idTipo == null || "".equals(idTipo)) {
                            datosErroneos += " ID Tipo del detalle #" + numeroPanel + ".";
                            flag = true;
                        } else if(!isNumeric(idTipo)) {
                            datosErroneos += " Datos no num\u00E9ricos en ID Tipo del detalle #" + numeroPanel + ".";
                            flag = true;
                        }
                        if (tipo == null || "".equals(tipo)) {
                            datosErroneos += " Tipo del detalle #" + numeroPanel + ".";
                            flag = true;
                        } else if (!tipo.equalsIgnoreCase(tipoPanel) && !tipo.equalsIgnoreCase(tipoRuta)) {
                            datosErroneos += " Tipo del detalle #" + numeroPanel + " debe espec\u00EDficarse como " + tipoPanel + " o " + tipoRuta + ".";
                            flag = true;
                        }
                    }
                    
                    numeroPanel = 1;
                    for (InputDescuentoDet detActual: input.getDescuentoDet()) {
                        if (flag ) break;
                        int indexDet = 1;
                        
                        for (InputDescuentoDet detalle: input.getDescuentoDet()) {
                            if (flag) break;
                            
                            if(indexDet!=numeroPanel
                                    && detalle.getTipo().toUpperCase().trim()
                                        .equals(detActual.getTipo().toUpperCase().trim())
                                    && Integer.valueOf(detalle.getIdTipo().trim())
                                        .equals(Integer.valueOf(detActual.getIdTipo())) ) {
                                log.error("El detalle #" + indexDet + " es igual al #" + numeroPanel);
                                datosErroneos += " Los datos del detalle #" + indexDet
                                        + " son iguales al detalle #" + numeroPanel + ".";
                                
                                flag = true;
                            }
                            indexDet++;
                        }
                        numeroPanel++;
                    }
                } else {
                    datosErroneos += " Detalles de panel.";
                    flag = true;
                }
            }
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
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET 
     * y en este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return campos Array con los nombres de los campos.
     */
    public static String[] obtenerCamposGetPost(int metodo) {
        if (metodo == Conf.METODO_GET) {
            String campos[] = {
                Descuento.CAMPO_TC_SC_DESCUENTO_ID,
                Descuento.CAMPO_TIPO,
                Descuento.CAMPO_NOMBRE,
                Descuento.CAMPO_DESCRIPCION,
                Descuento.CAMPO_TIPO_DESCUENTO,
                Descuento.CAMPO_CONFIGURACION,
                Descuento.CAMPO_DESCUENTO,
                Descuento.CAMPO_CONF_RECARGA,
                Descuento.CAMPO_RECARGA,
                Descuento.CAMPO_CONF_PRECIO,
                Descuento.CAMPO_PRECIO,
                Descuento.CAMPO_FECHA_DESDE,
                Descuento.CAMPO_FECHA_HASTA,
                Descuento.CAMPO_ARTICULOID,
                Descuento.CAMPO_NOMBRE_ART,
                Descuento.CAMPO_PRECIO_ART,
                Descuento.CAMPO_ESTADO,
                Descuento.CAMPO_CREADO_EL,
                Descuento.CAMPO_CREADO_POR,
                Descuento.CAMPO_MODIFICADO_EL,
                Descuento.CAMPO_MODIFICADO_POR
            };
            return campos;
        } else {
            String campos[] = {
                Descuento.CAMPO_TC_SC_DESCUENTO_ID,
                Descuento.CAMPO_TIPO,
                Descuento.CAMPO_NOMBRE,
                Descuento.CAMPO_DESCRIPCION,
                Descuento.CAMPO_TIPO_DESCUENTO,
                Descuento.CAMPO_CONFIGURACION,
                Descuento.CAMPO_DESCUENTO,
                Descuento.CAMPO_CONF_RECARGA,
                Descuento.CAMPO_RECARGA,
                Descuento.CAMPO_CONF_PRECIO,
                Descuento.CAMPO_PRECIO,
                Descuento.CAMPO_FECHA_DESDE,
                Descuento.CAMPO_FECHA_HASTA,
                Descuento.CAMPO_ARTICULOID,
                Descuento.CAMPO_NOMBRE_ART,
                Descuento.CAMPO_PRECIO_ART,
                Descuento.CAMPO_ESTADO,
                Descuento.CAMPO_CREADO_EL,
                Descuento.CAMPO_CREADO_POR
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
                DescuentoDet.CAMPO_TCSCDESCUENTOSDETID,
                DescuentoDet.CAMPO_TCSCDESCUENTOID,
                DescuentoDet.CAMPO_TCSCTIPOID,
                DescuentoDet.CAMPO_TIPO,
                DescuentoDet.CAMPO_ESTADO,
                DescuentoDet.CAMPO_CREADO_EL,
                DescuentoDet.CAMPO_CREADO_POR,
                DescuentoDet.CAMPO_MODIFICADO_EL,
                DescuentoDet.CAMPO_MODIFICADO_POR
            };
            return campos;
        } else {
            String campos[] = {
                DescuentoDet.CAMPO_TCSCDESCUENTOSDETID,
                DescuentoDet.CAMPO_TCSCDESCUENTOID,
                DescuentoDet.CAMPO_TCSCTIPOID,
                DescuentoDet.CAMPO_TIPO,
                DescuentoDet.CAMPO_ESTADO,
                DescuentoDet.CAMPO_CREADO_EL,
                DescuentoDet.CAMPO_CREADO_POR
            };
            return campos;
        }
    }
    
    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param sequencia Nombre de la secuencia a utilizar para la inserci\u00F3n.
     * @param string 
     * @return inserts Listado de cadenas con los valores a insertar.
     */
    public static List<String> obtenerInsertsPost(Connection conn, InputDescuento input, String sequencia,
            String estadoAlta) {
        String nombreMetodo = "obtenerInsertsPost";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        List<String> inserts = new ArrayList<String>();

        String valores = "";
        String campania = "";
        String mayor = "";
        String mayorSym = "";
        String menor = "";
        String menorSym = "";
        String igual = "";
        String igualSym = "";
        String configDescuento = "";
        String configRecarga = "";
        String configPrecio = "";
        
        String campos[] = {
            Catalogo.CAMPO_NOMBRE,
            Catalogo.CAMPO_VALOR
        };
        
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.GRUPO_DESCUENTOS));

        List<Map<String, String>> datosConfig = new ArrayList<Map<String, String>>();
        try {
            datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, null);
        } catch (SQLException e) {
            log.error("Error al obtener par\u00E9metros de configuraci\u00F3n. " + e);
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, null,
                nombreClase, nombreMetodo, null, input.getCodArea());
            
            inserts.add(r.getDescripcion());
        }
        
        for (int i = 0; i < datosConfig.size(); i++) {
            if (Conf.TIPO_CAMPANIA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                campania = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.DESCU_CONFIG_MAYOR.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                mayor = datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE);
                mayorSym = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.DESCU_CONFIG_MENOR.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                menor = datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE);
                menorSym = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
            if (Conf.DESCU_CONFIG_IGUAL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                igual = datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE);
                igualSym = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
            }
        }
        
        if (input.getConfiguracion().equalsIgnoreCase(mayor)){
            configDescuento  = mayorSym;
        } else if (input.getConfiguracion().equalsIgnoreCase(menor)){
            configDescuento = menorSym;
        } else if (input.getConfiguracion().equalsIgnoreCase(igual)){
            configDescuento = igualSym;
        }
        
        if (input.getConfRecarga().equalsIgnoreCase(mayor)){
            configRecarga  = mayorSym;
        } else if (input.getConfRecarga().equalsIgnoreCase(menor)){
            configRecarga = menorSym;
        } else if (input.getConfRecarga().equalsIgnoreCase(igual)){
            configRecarga = igualSym;
        }
        
        if (input.getConfPrecio().equalsIgnoreCase(mayor)){
            configPrecio  = mayorSym;
        } else if (input.getConfPrecio().equalsIgnoreCase(menor)){
            configPrecio = menorSym;
        } else if (input.getConfPrecio().equalsIgnoreCase(igual)){
            configPrecio = igualSym;
        }
        
        if (input.getTipo().equalsIgnoreCase(campania)) {
            valores = "("
                + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getTipo(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getNombre(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDescripcion(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getTipoDescuento(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, configDescuento, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getDescuento(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, configRecarga, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getRecarga(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, configPrecio, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getPrecio(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsertFecha(Conf.INSERT_FECHA, input.getFechaDesde(), Conf.TXT_FORMATO_FECHA_INPUT, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsertFecha(Conf.INSERT_FECHA, input.getFechaHasta(), Conf.TXT_FORMATO_FECHA_INPUT, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getIdArticulo(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getNombreArt(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getPrecioArt(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
            + ") ";
        } else {
            valores = "("
                + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getTipo(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getNombre(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDescripcion(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getTipoDescuento(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, configDescuento, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getDescuento(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NULL, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO)
            + ") ";
        }
        
        inserts.add(valores);
        
        return inserts;
    }
    
    /**
     * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST de la tabla relacionada.
     * 
     * @param idPadre
     * @param input
     * @param sequencia
     * @param estadoAlta 
     * @return
     */
    public static List<String> obtenerInsertsPostHijo(int idPadre, InputDescuento input, String sequencia,
            String estadoAlta) {
        List<String> inserts = new ArrayList<String>();
        String valores = "";
        
        for (int i = 0; i < input.getDescuentoDet().size(); i++) {
            valores =
                UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPadre+"", Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getDescuentoDet().get(i).getIdTipo(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, input.getDescuentoDet().get(i).getTipo(), Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estadoAlta, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
                + UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_NO);
            inserts.add(valores);
        }
        
        return inserts;
    }
    
    /**
     * Funci\u00F3n que indica los campos que se utilizar\u00E9n para modificaciones  
     * al recurso en los m\u00E9todos PUT y DELETE.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @param estadoAlta 
     * @return campos Array bidimensional que contiene el nombre de los campos a modificar
     *  y su respectivo valor a insertar.
     * @throws SQLException 
     */
    public static String[][] obtenerCamposPutDel(Connection conn, InputDescuento input, int metodo, String estadoAlta)
            throws SQLException {
        if (metodo == Conf.METODO_DELETE) {
            String campos[][] = {
                { Descuento.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea())) },
                { Descuento.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                { Descuento.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
            };
            return campos;
        } else {
            String campania = "";
            String mayor = "";
            String mayorSym = "";
            String menor = "";
            String menorSym = "";
            String igual = "";
            String igualSym = "";
            String configDescuento = "";
            String configRecarga = "";
            String configPrecio = "";
            
            String camposConfig[] = {
                Catalogo.CAMPO_NOMBRE,
                Catalogo.CAMPO_VALOR
            };

            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.GRUPO_DESCUENTOS));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));

            List<Map<String, String>> datosConfig = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, camposConfig,
                    condiciones, null);

            for (int i = 0; i < datosConfig.size(); i++) {
                if (Conf.TIPO_CAMPANIA.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    campania = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.DESCU_CONFIG_MAYOR.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    mayor = datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE);
                    mayorSym = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.DESCU_CONFIG_MENOR.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    menor = datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE);
                    menorSym = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
                if (Conf.DESCU_CONFIG_IGUAL.equalsIgnoreCase(datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE))) {
                    igual = datosConfig.get(i).get(Catalogo.CAMPO_NOMBRE);
                    igualSym = datosConfig.get(i).get(Catalogo.CAMPO_VALOR);
                }
            }
            
            if (input.getConfiguracion().equalsIgnoreCase(mayor)){
                configDescuento  = mayorSym;
            } else if (input.getConfiguracion().equalsIgnoreCase(menor)){
                configDescuento = menorSym;
            } else if (input.getConfiguracion().equalsIgnoreCase(igual)){
                configDescuento = igualSym;
            }
            
            if (input.getConfRecarga().equalsIgnoreCase(mayor)){
                configRecarga  = mayorSym;
            } else if (input.getConfRecarga().equalsIgnoreCase(menor)){
                configRecarga = menorSym;
            } else if (input.getConfRecarga().equalsIgnoreCase(igual)){
                configRecarga = igualSym;
            }
            
            if (input.getConfPrecio().equalsIgnoreCase(mayor)){
                configPrecio  = mayorSym;
            } else if (input.getConfPrecio().equalsIgnoreCase(menor)){
                configPrecio = menorSym;
            } else if (input.getConfPrecio().equalsIgnoreCase(igual)){
                configPrecio = igualSym;
            }
            
            if (input.getTipo().equalsIgnoreCase(campania)) {
                String campos[][] = {
                    { Descuento.CAMPO_TIPO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getTipo()) },
                    { Descuento.CAMPO_NOMBRE, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getNombre()) },
                    { Descuento.CAMPO_DESCRIPCION, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getDescripcion()) },
                    { Descuento.CAMPO_TIPO_DESCUENTO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getTipoDescuento()) },
                    { Descuento.CAMPO_CONFIGURACION, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, configDescuento) },
                    { Descuento.CAMPO_DESCUENTO, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getDescuento()) },
                    { Descuento.CAMPO_CONF_RECARGA, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, configRecarga) },
                    { Descuento.CAMPO_RECARGA, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getRecarga()) },
                    { Descuento.CAMPO_CONF_PRECIO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, configPrecio) },
                    { Descuento.CAMPO_PRECIO, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getPrecio()) },
                    { Descuento.CAMPO_FECHA_DESDE, UtileriasJava.setUpdateFecha(Conf.INSERT_FECHA, input.getFechaDesde(), Conf.TXT_FORMATO_FECHA_INPUT) },
                    { Descuento.CAMPO_FECHA_HASTA, UtileriasJava.setUpdateFecha(Conf.INSERT_FECHA, input.getFechaHasta(), Conf.TXT_FORMATO_FECHA_INPUT) },
                    { Descuento.CAMPO_ARTICULOID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getIdArticulo()) },
                    { Descuento.CAMPO_NOMBRE_ART, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getNombreArt()) },
                    { Descuento.CAMPO_PRECIO_ART, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getPrecioArt()) },
                    { Descuento.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getEstado()) },
                    { Descuento.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                    { Descuento.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
                };
                return campos;
            } else { 
                String campos[][] = {
                    { Descuento.CAMPO_TIPO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getTipo()) },
                    { Descuento.CAMPO_NOMBRE, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getNombre()) },
                    { Descuento.CAMPO_DESCRIPCION, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getDescripcion()) },
                    { Descuento.CAMPO_TIPO_DESCUENTO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getTipoDescuento()) },
                    { Descuento.CAMPO_CONFIGURACION, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, configDescuento) },
                    { Descuento.CAMPO_DESCUENTO, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getDescuento()) },
                    { Descuento.CAMPO_CONF_RECARGA, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
                    { Descuento.CAMPO_RECARGA, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
                    { Descuento.CAMPO_CONF_PRECIO, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
                    { Descuento.CAMPO_PRECIO, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
                    { Descuento.CAMPO_FECHA_DESDE, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
                    { Descuento.CAMPO_FECHA_HASTA, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
                    { Descuento.CAMPO_ARTICULOID, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
                    { Descuento.CAMPO_NOMBRE_ART, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
                    { Descuento.CAMPO_PRECIO_ART, UtileriasJava.setUpdate(Conf.INSERT_NULL, null) },
                    { Descuento.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getEstado()) },
                    { Descuento.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
                    { Descuento.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
                };
                return campos;
            }
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
    public static String[][] obtenerCamposPutDelHijo(InputDescuento input) throws SQLException {
      //Los valores que sean tipo texto deben ir entre ap\u00F3strofres o comillas simples.
        String campos[][] = {
            { DescuentoDet.CAMPO_ESTADO, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea())) },
            { DescuentoDet.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
            { DescuentoDet.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
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
     * @throws SQLException 
     */
    public static List<Filtro> obtenerCondiciones(Connection conn, InputDescuento input, int metodo) throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        List<Filtro> condicionesExtra = new ArrayList<Filtro>();
        String tipoPanel = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_PANEL, input.getCodArea());
        String tipoRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_RUTA, input.getCodArea());

        if ((metodo==Conf.METODO_PUT || metodo==Conf.METODO_DELETE) && !"".equals(input.getIdDescuento())) {
                condiciones.add(UtileriasJava.setCondicion(
                    Conf.FILTRO_ID_NUMERICO_AND, Descuento.CAMPO_TC_SC_DESCUENTO_ID, input.getIdDescuento()));
        }
        
        if (metodo==Conf.METODO_GET){
            if (input.getIdDescuento() != null && !"".equals(input.getIdDescuento())) {
                    condiciones.add(UtileriasJava.setCondicion(
                        Conf.FILTRO_ID_NUMERICO_AND, Descuento.CAMPO_TC_SC_DESCUENTO_ID, input.getIdDescuento()));
            }
            if (input.getTipo() != null && !"".equals(input.getTipo())) {
                    condiciones.add(UtileriasJava.setCondicion(
                        Conf.FILTRO_TEXTO_UPPER_AND, Descuento.CAMPO_TIPO, input.getTipo()));
            }
            if (input.getNombre() != null && !"".equals(input.getNombre())) {
                    condiciones.add(UtileriasJava.setCondicion(
                        Conf.FILTRO_TEXTO_UPPER_AND, Descuento.CAMPO_NOMBRE, input.getNombre()));
            }
            if (input.getTipoDescuento() != null && !"".equals(input.getTipoDescuento())) {
                    condiciones.add(UtileriasJava.setCondicion(
                        Conf.FILTRO_TEXTO_UPPER_AND, Descuento.CAMPO_TIPO_DESCUENTO, input.getTipoDescuento()));
            }
            if (input.getIdArticulo() != null && !"".equals(input.getIdArticulo())) {
                    condiciones.add(UtileriasJava.setCondicion(
                        Conf.FILTRO_ID_NUMERICO_AND, Descuento.CAMPO_ARTICULOID, input.getIdArticulo()));
            }
            if (input.getEstado() != null && !"".equals(input.getEstado())) {
                    condiciones.add(UtileriasJava.setCondicion(
                        Conf.FILTRO_TEXTO_UPPER_AND, Descuento.CAMPO_ESTADO, input.getEstado()));
            }

            String[] campos = { DescuentoDet.CAMPO_TCSCDESCUENTOID };
            if (input.getIdPanel() != null && !"".equals(input.getIdPanel())) {
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, DescuentoDet.CAMPO_TCSCTIPOID, input.getIdPanel()));
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, DescuentoDet.CAMPO_TIPO, tipoPanel));
                    
                    String selectSQL = UtileriasBD.armarQuerySelect(DescuentoDet.N_TABLA, campos, condicionesExtra);
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Descuento.CAMPO_TC_SC_DESCUENTO_ID, selectSQL));
            }
            
            if (input.getIdRuta() != null && !"".equals(input.getIdRuta())) {
                    condicionesExtra.clear();
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, DescuentoDet.CAMPO_TCSCTIPOID, input.getIdRuta()));
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, DescuentoDet.CAMPO_TIPO, tipoRuta));
                    
                    String selectSQL = UtileriasBD.armarQuerySelect(DescuentoDet.N_TABLA, campos, condicionesExtra);
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Descuento.CAMPO_TC_SC_DESCUENTO_ID, selectSQL));
            }
            
            if (input.getFechaDesde() != null && !"".equals(input.getFechaDesde())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NULL_OR, Descuento.CAMPO_FECHA_DESDE, null));
                
                condicionesExtra.clear();

                if (input.getFechaHasta() != null && !"".equals(input.getFechaHasta())) {
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_GTE_AND, Descuento.CAMPO_FECHA_DESDE,
                        input.getFechaDesde(), null, Conf.TXT_FORMATO_FECHA_CORTA));
                    
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_LTE_AND, Descuento.CAMPO_FECHA_HASTA,
                        input.getFechaHasta(), null, Conf.TXT_FORMATO_FECHA_CORTA));
                } else {
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_LTE_AND, Descuento.CAMPO_FECHA_DESDE,
                        input.getFechaDesde(), null, Conf.TXT_FORMATO_FECHA_CORTA));
                        
                    condicionesExtra.add(UtileriasJava.setCondicion(Conf.FILTRO_FECHA_GTE_AND, Descuento.CAMPO_FECHA_HASTA,
                        input.getFechaDesde(), null, Conf.TXT_FORMATO_FECHA_CORTA));
                }
                
                condiciones.add(new Filtro(Filtro.OR, "", "", UtileriasBD.agruparCondiciones(condicionesExtra)));
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
    public static List<Filtro> obtenerCondicionesTablaHija(String idPadre,  int metodo) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        
        if (metodo==Conf.METODO_DELETE || metodo==Conf.METODO_PUT){
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, DescuentoDet.CAMPO_TCSCDESCUENTOID, idPadre));
        }
        
        return condiciones;
    }
    
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las consultas de verificaci\u00F3n
     * de existencia del recurso.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo deseado.
     * @throws SQLException 

     */
    public static List<Filtro> obtenerCondicionesExistencia(Connection conn, InputDescuento input, int metodo) throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        
        if (metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE){
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                Descuento.CAMPO_TC_SC_DESCUENTO_ID, input.getIdDescuento()));
        }

        if (metodo != Conf.METODO_PUT && metodo != Conf.METODO_DELETE){
            condiciones.add(UtileriasJava.getCondicionDefault(Conf.FILTRO_ESTADO, Descuento.CAMPO_ESTADO, conn, input.getCodArea()));
        }
        
        if (metodo == Conf.METODO_POST){
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, Descuento.CAMPO_ESTADO, input.getNombre()));
        }
        
        return condiciones;
    }
    
    public static List<Filtro> obtenerCondicionesExistenciaHijo(Connection conn, int idPadre, String codArea) throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        if (idPadre > 0) {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                    DescuentoDet.CAMPO_TCSCDESCUENTOID, String.valueOf(idPadre)));
        }
        
        condiciones.add(UtileriasJava.getCondicionDefault(Conf.FILTRO_ESTADO, Descuento.CAMPO_ESTADO, conn, codArea));
        
        return condiciones;
    }
    
    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo 
     * @return output Respuesta y listado con los Descuentos encontrados.
     */
    public OutputDescuento getDatos(InputDescuento input, int metodo) {
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        List<Respuesta> respuesta = new ArrayList<Respuesta>();

        Respuesta r = new Respuesta();
        OutputDescuento output = null;

        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            
            // Validaci\u00F3n de datos en el input
            r = validarInput(conn, input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equalsIgnoreCase(r.getDescripcion())) {
                respuesta.add(r);
                output = new OutputDescuento();
                output.setRespuesta(r);
                return output;
            }

            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
            if (metodo == Conf.METODO_GET) {
                try {
                    output = OperacionDescuento.doGet(conn, input, metodo);
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputDescuento();
                    output.setRespuesta(r);
                }
            
            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
            } else if (metodo == Conf.METODO_POST) {
                try {
                    output = OperacionDescuento.doPost(conn, input);
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputDescuento();
                    output.setRespuesta(r);
                }
                
            // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT o DELETE
            } else if (metodo == Conf.METODO_DELETE || metodo == Conf.METODO_PUT) {
                try {
                    output = OperacionDescuento.doPutDel(conn, input, metodo);
                } catch (SQLException e) {
                    r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output = new OutputDescuento();
                    output.setRespuesta(r);
                }
            }
        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputDescuento();
            output.setRespuesta(r);
        } finally {
        	DbUtils.closeQuietly(conn);
        }

        return output;
    }
}

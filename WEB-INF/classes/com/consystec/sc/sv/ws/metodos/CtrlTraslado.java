package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.traslado.InputArticuloTraslado;
import com.consystec.sc.ca.ws.input.traslado.InputTraslado;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.traslado.OutputTraslado;
import com.consystec.sc.sv.ws.operaciones.OperacionTraslado;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.HistoricoInv;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 * 
 * @changelog
 *      30-03-2017 Agregado traslado por n\u00FAmero de traspaso.
 *
 */
public class CtrlTraslado extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlTraslado.class);
    private static String nombreServicio = Conf.LOG_PUT_TRASLADO;
    private static boolean esTraspaso = false;

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al servicio.
     * @param conn 
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @param estadoAlta 
     * @return Respuesta
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputTraslado input, int metodo, String estadoAlta, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();

        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        String respArticulos = "";

        log.debug("Validando datos...");

        if (input.getUsuario() == null || input.getUsuario().trim().equals("")) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getNumTraspaso() != null && !input.getNumTraspaso().trim().equals("")) {
            if (!isNumeric(input.getNumTraspaso())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_NUM_TRASPASO_911, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                esTraspaso = true;
            }
        }

        if (input.getBodegaOrigen() == null || input.getBodegaOrigen().trim().equals("")) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODORIGEN_139, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        } else if (!isNumeric(input.getBodegaOrigen())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODORIGEN_NUM_140, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getBodegaDestino() == null || input.getBodegaDestino().trim().equals("")) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODDESTINO_141, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        } else if (!isNumeric(input.getBodegaDestino())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBODDESTINO_NUM_142, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        // Validaci\u00F3n Origen
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getBodegaOrigen()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVirtual.CAMPO_ESTADO, estadoAlta));

        String existencia;
        try {
            existencia = UtileriasBD.verificarExistencia(conn, getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);
            if (new Integer(existencia) <= 0) {
                log.error("No existe la bodega Origen.");
                datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_NO_BODEGA_ORIGEN_110, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }
        } catch (SQLException e) {
            log.error("Error al verificar existencia de la Bodega Origen.", e);
            datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_BODEGA_100, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }
        // Validaci\u00F3n Origen

    	// Validaci\u00F3n de nivel de bodegas para omitir/continuar con validaci\u00F3n de destino
    	condiciones.clear();
    	condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getBodegaOrigen()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
        // Se obtiene el nivel de la bodega origen
    	String nivelBodOrigen = "";
    	List<String> lstNivelBodOrigen = UtileriasBD.getOneField(conn, BodegaVirtual.CAMPO_NIVEL, getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones, null);
    	condiciones.clear();
    	condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getBodegaDestino()));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
        // Se obtiene el nivel de la bodega destino
        String nivelBodDestino = "";
    	List<String> lstNivelBodDestino = UtileriasBD.getOneField(conn, BodegaVirtual.CAMPO_NIVEL, getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones, null);

    	if (lstNivelBodOrigen != null && !lstNivelBodOrigen.isEmpty()) {
    		nivelBodOrigen = lstNivelBodOrigen.get(0);
    		input.setNvlBodegaOrigen(nivelBodOrigen);
    	} else {
        	log.error("Error al verificar el nivel de la Bodega Origen.");
        	datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_NIVEL_BODEGA_ORIGEN_634, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }
        if (lstNivelBodDestino != null && !lstNivelBodDestino.isEmpty()) {
        	nivelBodDestino = lstNivelBodDestino.get(0);
        } else {
            log.error("Error al verificar el nivel de la Bodega Destino.");
            datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_NIVEL_BODEGA_DESTINO_635, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        // Se realiza la validacion de destino cuando ninguna bodega es de nivel 0
    	if(!"0".equals(nivelBodOrigen) && !"0".equals(nivelBodDestino)) {
    		// Se realizan la Validaci\u00F3n de Destino
            // Validaci\u00F3n Destino
            try {
                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getBodegaOrigen()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));

                // se obtiene el query para el id de la bodega padre
                String selectBodPadre = UtileriasBD.armarQuerySelectField(getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea()), BodegaVirtual.CAMPO_IDBODEGA_PADRE, condiciones, null);
                // se obtiene el query para el id de la bodega origen
                String selectBodOrigen = UtileriasBD.armarQuerySelectField(getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea()), BodegaVirtual.CAMPO_IDBODEGA_ORIGEN, condiciones, null);

                condiciones.clear();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_OR, BodegaVirtual.CAMPO_IDBODEGA_PADRE,
                        "(" + selectBodPadre + "),(" + selectBodOrigen + ")," + input.getBodegaOrigen()));

                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_OR, BodegaVirtual.CAMPO_IDBODEGA_ORIGEN,
                        "(" + selectBodPadre + "),(" + selectBodOrigen + ")," + input.getBodegaOrigen()));

                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_OR, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID,
                        "(" + selectBodPadre + "),(" + selectBodOrigen + ")," + input.getBodegaOrigen()));
                
                List<String> bodegasPermitidas = UtileriasBD.getOneField(conn, BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones, null);

                boolean flagBodega = false;
                for (int i = 0; i < bodegasPermitidas.size(); i++) {
                    if (bodegasPermitidas.get(i).equals(input.getBodegaDestino())) {
                        flagBodega = true;
                        break;
                    }
                }

                if (flagBodega == false) {
                    datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_NO_BODEGA_DESTINO_102, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }
            } catch (SQLException e) {
                log.error("Error al verificar existencia de la Bodega Destino.", e);
                datosErroneos += " " + getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_BODEGA_100, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            }
            // Validaci\u00F3n Destino
    	}
    	// Validaci\u00F3n de nivel de bodegas

        try {
            if (input.getFecha() == null || "".equals(input.getFecha().trim())) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_314, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                flag = true;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
                Date fecha = sdf.parse(input.getFecha());
                log.trace(sdf.format(fecha));
                Date fechaActual = sdf.parse(sdf.format(new Date()));
                log.trace(sdf.format(fechaActual));

                if (fecha.before(fechaActual)) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_FECHA_MAYOR_ACTUAL_338, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }
        } catch (java.text.ParseException e) {
            log.error("Problema al convertir la fecha en clase " + nombreMetodo + " m\u00E9todo " + nombreClase + ".", e);
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_CONVERTIR_FECHA_165, null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (!esTraspaso) {
            InputTraslado inputSeriado = new InputTraslado();
            InputTraslado inputNoSeriado = new InputTraslado();

            List<InputArticuloTraslado> articulosSeriados = new ArrayList<InputArticuloTraslado>();
            List<InputArticuloTraslado> articulosNoSeriados = new ArrayList<InputArticuloTraslado>();

            for (int i = 0; i < input.getArticulos().size(); i++) {
                if (input.getArticulos().get(i).getSerie() != null && !input.getArticulos().get(i).getSerie().equals("")) {
                    articulosSeriados.add(input.getArticulos().get(i));
                } else {
                    articulosNoSeriados.add(input.getArticulos().get(i));
                }
                inputSeriado.setArticulos(articulosSeriados);
                inputNoSeriado.setArticulos(articulosNoSeriados);
            }

            respArticulos = validaSeriados(inputSeriado);
            if (!"".equals(respArticulos)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_LISTADO_SERIADO_144, null, nombreClase, nombreMetodo,
                        respArticulos, input.getCodArea()).getDescripcion();
                flag = true;
            }
            if (!flag ) {
                respArticulos = validaNoSeriados(inputNoSeriado);
                if (!respArticulos.equals("")) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_LISTADO_NO_SERIADO_145, null, nombreClase,
                            nombreMetodo, respArticulos, input.getCodArea()).getDescripcion();
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

    private String validaNoSeriados(InputTraslado input) {
        String nombreMetodo = "validaNoSeriados";
        String nombreClase = new CurrentClassGetter().getClassName();
        String datosErroneos = "";
        boolean flag = false;
        int numeroArticulo = 0;

        for (int i = 0; i < input.getArticulos().size(); i++) {
            if (flag == true)
                break;
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

            if (idArticulo == null || idArticulo.equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_IDART_146, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(idArticulo)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_IDART_NUM_147, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (cant == null || cant.equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT_148, null, nombreClase, nombreMetodo,
                        numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            } else if (!isNumeric(cant)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT_NUM_149, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            } else if (new Integer(cant) <= 0) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_CANT0_151, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }

            if (tipoInv == null || tipoInv.equals("")) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_TIPOINV_152, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }
        }
    
        numeroArticulo = 1;
        for (InputArticuloTraslado detActual: input.getArticulos()) {
            if (flag == true) break;
            int indexArt = 1;
            
            for (InputArticuloTraslado detalle: input.getArticulos()) {
                if (flag == true) break;
                
                if(indexArt!=numeroArticulo 
                        && detalle.getIdArticulo().toUpperCase().trim().equals(detActual.getIdArticulo().toUpperCase().trim()) 
                        && detalle.getTipoInv().toUpperCase().trim().equals(detActual.getTipoInv().toUpperCase().trim()) ) {
                    log.error("El Art\u00EDculo #" + indexArt + " es igual al #" + numeroArticulo);
                    datosErroneos += " Los datos del Art\u00EDculo #" + indexArt
                            + " son iguales al Art\u00EDculo #" + numeroArticulo + ", ID de art\u00EDculo " + detActual.getIdArticulo() + ".";
                    
                    flag = true;
                }
                indexArt++;
            }
            numeroArticulo++;
        }
        
        return datosErroneos;
    }

    private String validaSeriados(InputTraslado input) {
        String nombreMetodo = "validaSeriados";
        String nombreClase = new CurrentClassGetter().getClassName();
        String datosErroneos = "";
        boolean flag = false;
        int numeroArticulo = 0;

        for (int i = 0; i < input.getArticulos().size(); i++) {
            if (flag == true)
                break;
            String serie = null;
            String serieFinal = null;
            String tipoInv = null;
            String lote = null;//lote

            if (input.getArticulos().get(i).getSerie() != null) {
                serie = input.getArticulos().get(i).getSerie().trim();
            }
            if (input.getArticulos().get(i).getSerieFinal() != null) {
                serieFinal = input.getArticulos().get(i).getSerieFinal().trim();
            }
            if (input.getArticulos().get(i).getTipoInv() != null) {
                tipoInv = input.getArticulos().get(i).getTipoInv().trim();
            }
            if (input.getArticulos().get(i).getNoLote() != null) {
            	lote = input.getArticulos().get(i).getNoLote().trim();
            }
            numeroArticulo = i + 1;

            if (serie == null || "".equals(serie)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_SERIE_153, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }
            
            if (serieFinal != null && !serieFinal.equals("")) {
                if (!isNumeric(serie) || !isNumeric(serieFinal)) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_RANGO_SERIES_NUM_363, null, nombreClase,
                            nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                    flag = true;
                } else if (new BigInteger(serie).compareTo(new BigInteger(serieFinal)) >= 1) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_RANGO_SERIES_INV_364, null, nombreClase,
                            nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                    flag = true;
                }
                //validar que se haya enviado el lote cuando: 
                	// se envia serie final (rango)
                	// nivel de bodega origen = 1
                if ((lote == null || "".equals(lote)) && "1".equals(input.getNvlBodegaOrigen())) {
                    datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_LOTE_SERIE_641, null, nombreClase,
                            nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                    flag = true;
                }
            }

            if (tipoInv == null || "".equals(tipoInv)) {
                datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_DET_TIPOINV_152, null, nombreClase,
                        nombreMetodo, numeroArticulo + ".", input.getCodArea()).getDescripcion();
                flag = true;
            }
        }

        numeroArticulo = 1;
        for (InputArticuloTraslado detActual : input.getArticulos()) {
            if (flag == true)
                break;
            int indexArt = 1;

            for (InputArticuloTraslado detalle : input.getArticulos()) {
                if (flag == true)
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
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET 
     * y en este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return campos Array con los nombres de los campos.
     */
    public static String[][] obtenerCamposPutDel(InputTraslado input) {
        String campos[][] = {
            { Inventario.CAMPO_TCSCBODEGAVIRTUALID, UtileriasJava.setUpdate(Conf.INSERT_NUMERO, input.getBodegaDestino()) },
            { Inventario.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
            { Inventario.CAMPO_MODIFICADO_POR, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }
        };
        
        return campos;
    }
    
    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo
     * @return output Respuesta y listado con los Traslados encontrados.
     * @throws SQLException
     */
    public OutputTraslado getDatos(InputTraslado input, int metodo) {
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        Respuesta respuesta = new Respuesta();
        OutputTraslado output = null;
        BigDecimal idTraslado=null;
        esTraspaso = false;
        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());
            conn.setAutoCommit(false);
            
            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input, metodo, estadoAlta, ID_PAIS);
            log.trace("Respuesta validaci\u00F3n: " + respuesta.getDescripcion());
            if (!respuesta.getDescripcion().equals("OK")) {
                output = new OutputTraslado();
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, nombreServicio, "0",
                        Conf.LOG_TIPO_NINGUNO, output.getRespuesta().getDescripcion(), ""));

                return output;
            }
            
            String estadoDisponible = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DISPONIBLE, input.getCodArea());
            BigDecimal idTipoTransaccion = ControladorBase.getIdTransaccion(conn, Conf.CODIGO_TRANSACCION_TRASLADO, ID_PAIS);
            UtileriasJava.validarParametroVacio(idTipoTransaccion.toString());
        	idTraslado = JavaUtils.getSecuencia(conn, HistoricoInv.SEQUENCE_TRASLADO);
        	log.trace("idTraslado:"+idTraslado);
            if (esTraspaso) {
                output = OperacionTraslado.doTraspaso(conn, input, estadoAlta, estadoDisponible, idTipoTransaccion,idTraslado, ID_PAIS);
            } else {
                output = OperacionTraslado.doPutDel(conn, input, estadoAlta, estadoDisponible, idTipoTransaccion,idTraslado, ID_PAIS);
            }
            output.setIdTraslado(idTraslado+"");	
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputTraslado();
            output.setRespuesta(respuesta);
            output.setIdTraslado(null);
            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, nombreServicio, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de traslados.", e.getMessage()));

        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputTraslado();
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_TRASLADO, nombreServicio, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de traslados.", e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}

package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.panel.InputPanel;
import com.consystec.sc.ca.ws.input.ruta.InputVendedor;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.panel.OutputPanel;
import com.consystec.sc.sv.ws.operaciones.OperacionPuntoVenta;
import com.consystec.sc.sv.ws.operaciones.OperacionJornada;
import com.consystec.sc.sv.ws.operaciones.OperacionPanel;
import com.consystec.sc.sv.ws.operaciones.OperacionesBodegaVend;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.Panel;
import com.consystec.sc.sv.ws.orm.VendedorPDV;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author susana barrios Consystec 2015
 * 
 */
public class CtrlPanel extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlPanel.class);
    private static List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_PANEL;
    private static String servicioPost = Conf.LOG_POST_PANEL;
    private static String servicioPut = Conf.LOG_PUT_PANEL;

    /***
     * Validando que no vengan par\u00E9metros nulos
     * @param tipoPanel 
     * @param estadoAlta 
     * @param longNumero 
     * @param metodoPost 
     */
    public Respuesta validarDatos(InputPanel objDatos, int longNumero, String estadoAlta, String tipoPanel,
            int metodo) {
        Respuesta objRespuesta = null;
        String nombreMetodo = "validarDatos";

        if (metodo == Conf.METODO_PUT && (objDatos.getIdPanel() == null || "".equals(objDatos.getIdPanel()))) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDPANEL_NULO_39, this.getClass().toString(), null, nombreMetodo, "", objDatos.getCodArea());
        }

        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, this.getClass().toString(), null, nombreMetodo, null, objDatos.getCodArea());
        }

        if (objDatos.getNombre() == null || "".equals(objDatos.getNombre())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOMBRE_NULO_96, this.getClass().toString(), null, nombreMetodo, null, objDatos.getCodArea());
        }

        if (objDatos.getIdDistribuidor() == null || "".equals(objDatos.getIdDistribuidor())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_DTSASOCIADO_NULO_10, this.getClass().toString(), null,
                    nombreMetodo, null, objDatos.getCodArea());
        }

        if (objDatos.getIdBodegaVirtual() == null || "".equals(objDatos.getIdBodegaVirtual())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDBODVIRTUAL_NULO_45, this.getClass().toString(), null,
                    nombreMetodo, null, objDatos.getCodArea());
        }

               
        if (objDatos.getDatosVendedor() == null || objDatos.getDatosVendedor().isEmpty()) {
            return getMensaje(Conf_Mensajes.MSJ_INPUT_DATOSVENDEDOR_719, this.getClass().toString(), null, nombreMetodo, "", objDatos.getCodArea());
        } else {
            boolean banderaResponsable = false;
            for (int b = 1; b <= objDatos.getDatosVendedor().size(); b++) {
                String vendedor = objDatos.getDatosVendedor().get(b - 1).getVendedor();
                String responsable = objDatos.getDatosVendedor().get(b - 1).getResponsable();

                if (vendedor == null || "".equals(vendedor.trim())) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_VENDEDOR_VACIO_720, null, this.getClass().toString(),
                            nombreMetodo, b + ".", objDatos.getCodArea());
                } else if (!isNumeric(vendedor)) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_VENDEDOR_NUM_726, null, this.getClass().toString(),
                            nombreMetodo, b + ".", objDatos.getCodArea());
                }


                if (responsable == null || "".equals(responsable.trim()) || !isNumeric(responsable)
                        || (!"0".equals(responsable) && !"1".equals(responsable))) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_VEND_RESPONSABLE_732, null, this.getClass().toString(),
                            nombreMetodo, b + ".", objDatos.getCodArea());
                } else {
                    if ("1".equals(responsable)) {
                        if (banderaResponsable) {
                            return getMensaje(Conf_Mensajes.MSJ_INPUT_RESPONSABLE_PANEL_733, null,
                                    this.getClass().toString(), nombreMetodo, b + ".", objDatos.getCodArea());
                        } else {
                            banderaResponsable = true;
                        }
                    }
                }
            }
            
            if (!banderaResponsable) {
                return getMensaje(Conf_Mensajes.MSJ_ERROR_NO_RESPONSABLE_735, null, this.getClass().toString(),
                        nombreMetodo, null, objDatos.getCodArea());
            }

            int numeroVend = 1;
            for (InputVendedor vendActual : objDatos.getDatosVendedor()) {
                int indexVend = 1;

                for (InputVendedor detalle : objDatos.getDatosVendedor()) {
                    if (indexVend != numeroVend
                            && detalle.getVendedor().trim().equals(vendActual.getVendedor().trim())) {
                        return getMensaje(Conf_Mensajes.MSJ_INPUT_VENDEDORES_IGUALES_729, this.getClass().toString(),
                                null, nombreMetodo, numeroVend + " y " + indexVend + ".", objDatos.getCodArea());
                    }

                    
                    indexVend++;
                }
                
                numeroVend++;
            }
        }

        if (objRespuesta != null) {
            log.trace("resultado:" + objRespuesta.getDescripcion());
        }

        return objRespuesta;
    }

    /**************************************************************************
     * M\u00E9todo para insertar una panel
     */
    public OutputPanel insertarPanel(InputPanel objDatos) {
        listaLog = new ArrayList<LogSidra>();
        Respuesta objRespuesta = new Respuesta();
        OutputPanel salidaPanel = new OutputPanel();
        VendedorPDV vendedorPanel = new VendedorPDV();
        Panel objPanel = new Panel();
        List<VendedorPDV> lstVendedorInsertar = new ArrayList<VendedorPDV>();

        String razon = "";
        BigDecimal idPanel = null;
        int respinsert = 0;
        Connection conn = null;
        String metodo = "insertarPanel";
        String existeVend = "";
        String estadoAlta = "";
        int longNumero = 0;
        String tipoPanel = "";
        BigDecimal existeDTS = null;

        try {
            conn = getConnRegional();
            BigDecimal idPais = getIdPais(conn, objDatos.getCodArea());
            estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());
            tipoPanel = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_PANEL, objDatos.getCodArea());

            log.trace("inicia a validar valores...");
            objRespuesta = validarDatos(objDatos, longNumero, estadoAlta, tipoPanel, Conf.METODO_POST);

            if (objRespuesta == null) {

                // se valida que exista la bodega
                if (OperacionesBodegaVend.getVerificarBodegaVirtual(conn, objDatos.getIdBodegaVirtual(), estadoAlta, idPais) <= 0) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDBODVIRTUAL_NO_EXISTE_172, this.getClass().toString(),
                            null, metodo, null, objDatos.getCodArea());

                    salidaPanel.setRespuesta(objRespuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PANEL, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                            objRespuesta.getDescripcion()));

                    return salidaPanel;
                }
                
                //se verifica que exista el dts
                existeDTS = OperacionPuntoVenta.existeDts(conn, objDatos.getIdDistribuidor(), estadoAlta, idPais);

                if (existeDTS.intValue() == 0) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DTS_NOEXISTE_BAJA_88, this.getClass().toString(), null,
                            metodo, "", objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PANEL, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                            objRespuesta.getDescripcion()));

                } else {
                    // obteniendo id de Panel
                    idPanel = JavaUtils.getSecuencia(conn, Panel.SEQUENCE);
                    log.trace("idPanel: " + idPanel);
                    
                    // asignando valores a objeto a insertar de Panel
                    objPanel.setTcscpanelid(idPanel);
                    objPanel.setTcscdtsid(new BigDecimal(objDatos.getIdDistribuidor()));
                    objPanel.setNombre(objDatos.getNombre());
                    objPanel.setEstado(estadoAlta);
                    objPanel.setCreado_por(objDatos.getUsuario());
                    objPanel.setTcscbodegavirtualid(new BigDecimal(objDatos.getIdBodegaVirtual()));
                    objPanel.setTcsccatpaisid(idPais);
               

                    // Armando vendedores de panel
                    log.trace("tamanio de vendedores: " + objDatos.getDatosVendedor().size());

                    for (int b = 0; b < objDatos.getDatosVendedor().size(); b++) {
                        vendedorPanel = new VendedorPDV();

                        vendedorPanel.setIdtipo(objPanel.getTcscpanelid());
                        vendedorPanel.setEstado(estadoAlta);
                        vendedorPanel.setTipo(tipoPanel);
                        vendedorPanel.setVendedor(new BigDecimal(objDatos.getDatosVendedor().get(b).getVendedor()));
                        vendedorPanel.setResponsable(new BigDecimal(objDatos.getDatosVendedor().get(b).getResponsable()));
                        vendedorPanel.setCreado_por(objDatos.getUsuario());
                        
                        lstVendedorInsertar.add(vendedorPanel);

                    }

                        existeVend = OperacionPanel.validarVendedorPanel(conn, lstVendedorInsertar, new BigDecimal(0), tipoPanel, estadoAlta, idPais);
                        log.trace("resultado de vendedores repetidos: " + existeVend);
		                
		                if ( "".equals(existeVend)) {
		                    log.trace("Inicia a insertar valores");
		
		                    // se insertan valores
                            respinsert = OperacionPanel.insertarPanel(conn, objPanel);
    
                            if (respinsert > 0) {
                                OperacionPuntoVenta.insertVendedorPDV(conn, lstVendedorInsertar, idPais);
                                
                                
                                objRespuesta = getMensaje(Conf_Mensajes.OK_CREAPANEL6, null, null, null, razon, objDatos.getCodArea());
                                salidaPanel.setIdPanel(idPanel.toString());

                                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PANEL, servicioPost,
                                        idPanel.toString(), Conf.LOG_TIPO_PANEL,
                                        "Se cre\u00F3 nueva panel con ID " + idPanel + " y nombre " + objDatos.getNombre()
                                        + ".", ""));
                            }
                        } else if (existeVend.length() > 0) {
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENDREPETIDO_43, this.getClass().toString(), null,
                                    metodo, existeVend, objDatos.getCodArea());
                        
                            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PANEL, servicioPost, "0",
                                    Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                                    objRespuesta.getDescripcion()));
                        }
                }
            
            } else {
                salidaPanel.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PANEL, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            log.trace(e.getErrorCode());
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PANEL, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al crear nueva panel.", e.getMessage()));
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    "", objDatos.getCodArea());

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PANEL, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al crear nueva panel.", e.getMessage()));
            
        } finally {
            DbUtils.closeQuietly(conn);
            salidaPanel.setRespuesta(objRespuesta);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }

        return salidaPanel;
    }

    /**************************************************************************
     * M\u00E9todo para MODIFICAR UNA PANEL
     */
    public OutputPanel modificarPanel(InputPanel objDatos) {
        listaLog = new ArrayList<LogSidra>();
        Respuesta objRespuesta = new Respuesta();
        OutputPanel salidaPanel = new OutputPanel();
        VendedorPDV vendedorPanel = new VendedorPDV();
        Panel objPanel = new Panel();
        List<VendedorPDV> lstVendedorNuevos = new ArrayList<VendedorPDV>();
        List<VendedorPDV> lstVendedoresExistentes = new ArrayList<VendedorPDV>();
        List<VendedorPDV> lstVendPanel = new ArrayList<VendedorPDV>();
        List<VendedorPDV> lstVendPanelBorrar = new ArrayList<VendedorPDV>();
        List<Filtro> condiciones = new ArrayList<Filtro>();

        String razon = "";
        String existeVend = "";
        String existeVend1 = "";
        int respinsert = 0;
        Connection conn = null;
        String metodo = "modificarPanel";
        String estadoAlta = "";
        String tipoPanel = "";
        String condicion = "";
        BigDecimal existeDTS = null;

        try {
            conn = getConnRegional();
            BigDecimal idPais = getIdPais(conn, objDatos.getCodArea());
            estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());
            tipoPanel = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_PANEL, objDatos.getCodArea());

            if (OperacionJornada.existeJornadaSinLiquidar(conn, objDatos.getIdPanel(), tipoPanel, objDatos.getCodArea(), idPais)) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_JORNADAS_NO_LIQUIDADAS_794,
                        this.getClass().toString(), null, metodo, null, objDatos.getCodArea());

                salidaPanel.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PANEL, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

                return salidaPanel;
            }

            log.trace("inicia a validar valores...");
            objRespuesta = validarDatos(objDatos, /*longNumero*/0, estadoAlta, tipoPanel, Conf.METODO_PUT);

            if (objRespuesta == null) {
              

                // se valida que exista la bodega
                if (OperacionesBodegaVend.getVerificarBodegaVirtual(conn, objDatos.getIdBodegaVirtual(), estadoAlta, idPais) <= 0) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDBODVIRTUAL_NO_EXISTE_172, this.getClass().toString(),
                            null, metodo, null, objDatos.getCodArea());

                    salidaPanel.setRespuesta(objRespuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PANEL, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                            objRespuesta.getDescripcion()));

                    return salidaPanel;
                }

                //se verifica que exista el dts
                existeDTS = OperacionPuntoVenta.existeDts(conn, objDatos.getIdDistribuidor(), estadoAlta, idPais);

                if (existeDTS.intValue() == 0) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_DTS_NOEXISTE_BAJA_88, this.getClass().toString(), null,
                            metodo, "", objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut,
                            objDatos.getIdPanel(), Conf.LOG_TIPO_PANEL, "Problema en la validaci\u00F3n de datos.",
                            objRespuesta.getDescripcion()));

                } else {
                    // asignando valores a objeto a insertar de panel
                    objPanel.setTcscpanelid(new BigDecimal(objDatos.getIdPanel()));
                    objPanel.setTcscdtsid(new BigDecimal(objDatos.getIdDistribuidor()));
                    objPanel.setNombre(objDatos.getNombre());
                    objPanel.setEstado(estadoAlta);
                    objPanel.setModificado_por(objDatos.getUsuario());
                    objPanel.setTcscbodegavirtualid(new BigDecimal(objDatos.getIdBodegaVirtual()));
                    objPanel.setTcsccatpaisid(idPais);
                    
                    // Existencia de inventario en bodega
                    condiciones.clear();
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCPANELID, objDatos.getIdPanel()));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCCATPAISID, "" + idPais));
                    String idBodegaActual = UtileriasBD.getOneRecord(conn, Panel.CAMPO_TCSCBODEGAVIRTUALID, Panel.N_TABLA, condiciones);

                    if (!idBodegaActual.equals(objDatos.getIdBodegaVirtual())) {
                        condiciones.clear();
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCPANELID, objDatos.getIdPanel()));
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCCATPAISID, ""+idPais));
                        String selectIdBodega = UtileriasBD.armarQuerySelectField(Panel.N_TABLA, Panel.CAMPO_TCSCBODEGAVIRTUALID, condiciones, null);

                        condiciones.clear();
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID, selectIdBodega));
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCCATPAISID, "" + idPais));
                        String existencia = UtileriasBD.verificarExistencia(conn, Inventario.N_TABLA, condiciones);

                        if (new Integer(existencia) > 0) {
                            log.error("La bodega tiene inventario.");

                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_BODEGA_TIENE_INV_117, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());

                            salidaPanel.setRespuesta(objRespuesta);

                            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut,
                                    objDatos.getIdPanel(), Conf.LOG_TIPO_PANEL, "Problema en la validaci\u00F3n de datos.",
                                    objRespuesta.getDescripcion()));

                            return salidaPanel;
                        }
                    }

                    // Armando vendedores de panel
                    
                    /* 1- VERIFICAR SI EL VENDEDOR INGRESADO EXISTE
                     * 1.1 SI EXISTE LO DEJO COMO ESTA
                     * 1.2 SI NO EXISTE LO INSERTO, CREO BODEGA VIRTUAL Y ASOCIO
                     * 1.3 SI EXISTE EN TABLA Y NO ME LO INGRESARON ELIMINARLO (PARA OBTENER ESTOS HACER UN SELECT Y LUEGO EJECUTAR EL DELETE)
                     * */
                    
                    // se obtiene el listado de vendedores actuales de la panel
                    log.trace("tamanio de vendedores: " + objDatos.getDatosVendedor().size());
                    lstVendPanel = UtileriasJava.getVendPanelPdv(conn, new BigDecimal(objDatos.getIdPanel()), tipoPanel, null, null, idPais);


                    // se compara el listado de vendedores actuales con los vendedores enviados
                    for (int b = 0; b < objDatos.getDatosVendedor().size(); b++) {
                        boolean existe = false; // se setea existencia de vendedor en false
                        for (int c = 0; c < lstVendPanel.size(); c++) {
                            if (objDatos.getDatosVendedor().get(b).getVendedor().equals(lstVendPanel.get(c).getVendedor().toString())) {
                                existe = true;
                                
                                VendedorPDV vendExistente = new VendedorPDV();
                                vendExistente.setVendedor(new BigDecimal(objDatos.getDatosVendedor().get(b).getVendedor()));
                                
                                vendExistente.setResponsable(new BigDecimal(objDatos.getDatosVendedor().get(b).getResponsable()));

                                lstVendedoresExistentes.add(vendExistente);
                                break;

                            }
                        }

                        log.trace("VENDEDOR: ----> " + objDatos.getDatosVendedor().get(b).getVendedor() + ", EXISTE: ----> " + existe);
                        if (!existe) {
                            vendedorPanel = new VendedorPDV();

                            vendedorPanel.setIdtipo(objPanel.getTcscpanelid());
                            vendedorPanel.setEstado(estadoAlta);
                            vendedorPanel.setTipo(tipoPanel);
                            vendedorPanel.setVendedor(new BigDecimal(objDatos.getDatosVendedor().get(b).getVendedor()));
                            
                            vendedorPanel.setResponsable(new BigDecimal(objDatos.getDatosVendedor().get(b).getResponsable()));
                            
                            vendedorPanel.setCreado_por(objDatos.getUsuario());

                            lstVendedorNuevos.add(vendedorPanel);

                        }
                        
                        condicion += objDatos.getDatosVendedor().get(b).getVendedor() + ",";
                    }

                    // obtener lista de vendedores que hay que eliminar
                    log.trace("valores var Condicion: " + condicion);
                    if (!(condicion == null || "".equals(condicion))) {
                        lstVendPanelBorrar = UtileriasJava.getVendPanelPdv(conn, new BigDecimal(objDatos.getIdPanel()),
                                tipoPanel, "", condicion.substring(0, condicion.length() - 1), idPais);
                    }
                
                        
	                    //validando vendedores nuevos
	                    if(!lstVendedorNuevos.isEmpty()){
	                    	existeVend = OperacionPanel.validarVendedorPanel(conn, lstVendedorNuevos,  objPanel.getTcscpanelid(), tipoPanel,estadoAlta, idPais);
	                    }
	                    //validando vendedores existentes
                        if (!lstVendedoresExistentes.isEmpty()) {
                            existeVend1 = OperacionPanel.validarVendedorPanel(conn, lstVendedoresExistentes, objPanel.getTcscpanelid(), tipoPanel, estadoAlta, idPais);
                        }
                        
                        log.trace("existe vendedor: " + existeVend + existeVend1);
                        
                        if ("".equals(existeVend) && "".equals(existeVend1)) {
	                        log.trace("Inicia a insertar valores");
	                        
	                        // se insertan valores
                            respinsert = OperacionPanel.updatePanel(conn, objPanel);
                            if (respinsert > 0) {
                                if (!lstVendPanelBorrar.isEmpty()) {
                                    OperacionPuntoVenta.deleteVendedor2(conn, VendedorPDV.N_TABLA,
                                            objPanel.getTcscpanelid(), tipoPanel, lstVendPanelBorrar);
                                }

                                if (!lstVendedorNuevos.isEmpty()) {
                                    OperacionPuntoVenta.insertVendedorPDV(conn, lstVendedorNuevos, idPais);
                                }
                                
                                // se actualizan los vendedores seteando responsable
                                if (!lstVendedoresExistentes.isEmpty()) {
                                    OperacionPanel.updateVendedores(conn, new BigDecimal(objDatos.getIdPanel()), tipoPanel,
                                            lstVendedoresExistentes, objDatos.getUsuario(), estadoAlta);
                                }
    
                                
                                objRespuesta = getMensaje(Conf_Mensajes.OK_MODPANEL7, null, null, null, razon, objDatos.getCodArea());
                                salidaPanel.setIdPanel(objDatos.getIdPanel());
                                
                                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut,
                                        objDatos.getIdPanel(), Conf.LOG_TIPO_PANEL,
                                        "Se modificaron datos de la panel con ID " + objDatos.getIdPanel() + ".", ""));
                            }
	
                        } else if (existeVend.length() > 0) {
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENDREPETIDO_43, this.getClass().toString(),
                                    null, metodo, existeVend, objDatos.getCodArea());

                            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut, objDatos.getIdPanel(),
                                    Conf.LOG_TIPO_PANEL, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
                            
                        } else if (existeVend1.length() > 0) {
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENDREPETIDO_43, this.getClass().toString(),
                                    null, metodo, existeVend1, objDatos.getCodArea());

                            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut, objDatos.getIdPanel(),
                                    Conf.LOG_TIPO_PANEL, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
                        }
                }

            } else {
                salidaPanel.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut,
                        objDatos.getIdPanel() != null ? objDatos.getIdPanel() : "0", Conf.LOG_TIPO_PANEL,
                        "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            log.trace(e.getErrorCode());
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut, objDatos.getIdPanel(),
                    Conf.LOG_TIPO_PANEL, "Problema al modificar panel.", e.getMessage()));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    "", objDatos.getCodArea());

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut, objDatos.getIdPanel(),
                    Conf.LOG_TIPO_PANEL, "Problema al modificar panel.", e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);
            salidaPanel.setRespuesta(objRespuesta);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }

        return salidaPanel;
    }
    
    

    /**********************************************************************************
     * M\u00E9todo para dar de baja un pdv
     **********************************************************************************/
    public OutputPanel cambiarEstadoPanel(InputPanel objDatos) {
        listaLog = new ArrayList<LogSidra>();
        OutputPanel objSalida = new OutputPanel();
        Respuesta objRespuesta = new Respuesta();
        String metodo = "cambiarEstadoPanel";
        Connection conn = null;
        String estado = "";
        String estadoBaja = "";
        String estadoAlta = "";
        String tipoPanel = "";
        List<BigDecimal> lstPanel = new ArrayList<BigDecimal>();
        BigDecimal existeDTS = null;

        log.trace("inicia a validar valores...");

        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());

            tipoPanel = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_PANEL, objDatos.getCodArea());

            if (objDatos.getIdPanel() == null || "".equals(objDatos.getIdPanel())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDPANEL_NULO_39, this.getClass().toString(), null, metodo, "", objDatos.getCodArea());

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
                
            } else if (!isNumeric(objDatos.getIdPanel())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPANEL_NUM_252, this.getClass().toString(),
                        null, metodo, "", objDatos.getCodArea());

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
            
            } else if (objDatos.getEstado() == null || "".equals(objDatos.getEstado())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ESTADO_NULO_11, this.getClass().toString(), null, metodo,
                        null, objDatos.getCodArea());

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut, objDatos.getIdPanel(),
                        Conf.LOG_TIPO_PANEL, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
                
            } else {
                List<Filtro> condiciones = new ArrayList<Filtro>();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCPANELID, objDatos.getIdPanel()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCCATPAISID, ""+ID_PAIS));
                if(new Integer(UtileriasBD.verificarExistencia(conn, Panel.N_TABLA, condiciones)) < 1){
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_PANEL_181, this.getClass().toString(), null, metodo,
                            null, objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut, objDatos.getIdPanel(),
                            Conf.LOG_TIPO_PANEL, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
                    
                    objSalida.setRespuesta(objRespuesta);
                    return objSalida;
                }
                
                estado = getParametro(objDatos.getEstado(), 1, objDatos.getCodArea());

                if ("".equals(estado)) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_ESTADO_NOVALIDO_33, this.getClass().toString(), null,
                            metodo, null, objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut,
                            objDatos.getIdPanel(), Conf.LOG_TIPO_PANEL, "Problema en la validaci\u00F3n de datos.",
                            objRespuesta.getDescripcion()));
                } else {
                    estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());

                    existeDTS = OperacionPuntoVenta.existeDts(conn,
                            "(SELECT TCSCDTSID FROM TC_SC_PANEL WHERE TCSCPANELID=" + objDatos.getIdPanel() + " AND TCSCCATPAISID="+ID_PAIS+")",
                            estadoAlta, ID_PAIS);

                    if (existeDTS.intValue() == 0 && estado.equalsIgnoreCase(estadoAlta)) {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_DTS_NOEXISTE_BAJA_88, this.getClass().toString(),
                                null, metodo, "", objDatos.getCodArea());

                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut,
                                objDatos.getIdPanel(), Conf.LOG_TIPO_PANEL, "Problema en la validaci\u00F3n de datos.",
                                objRespuesta.getDescripcion()));
                    } else {
                        log.trace("Obtiene estado baja.");
                        estadoBaja = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, objDatos.getCodArea());
                        log.trace("ESTADO INPUT: " + estado + " -- ESTADO PARAM: " + estadoBaja);
                        if (estado.equalsIgnoreCase(estadoBaja)) {
                            lstPanel = OperacionPanel.getInvPanel(conn, tipoPanel, objDatos.getIdPanel(), ID_PAIS);
                        }

                        log.trace("validando inventario");
                        if (!lstPanel.isEmpty() && lstPanel.get(0).intValue() > 0) {
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_PANEL_TIENEINV_55, this.getClass().toString(),
                                    null, metodo, null, objDatos.getCodArea());

                            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut,
                                    objDatos.getIdPanel(), Conf.LOG_TIPO_PANEL, "Problema en la validaci\u00F3n de datos.",
                                    objRespuesta.getDescripcion()));

                        } else if (!lstPanel.isEmpty() && lstPanel.get(1).intValue() > 0) {
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_VEND_TIENEINVENTARIO_56,
                                    this.getClass().toString(), null, metodo, null, objDatos.getCodArea());

                            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut,
                                    objDatos.getIdPanel(), Conf.LOG_TIPO_PANEL, "Problema en la validaci\u00F3n de datos.",
                                    objRespuesta.getDescripcion()));

                        } else {
                            // actualizando a nuevo estado
                            OperacionPanel.updateTable(conn, Panel.N_TABLA, estado, new BigDecimal(objDatos.getIdPanel()), ID_PAIS);

 
                            OperacionPuntoVenta.updateTableRecarga(conn, VendedorPDV.N_TABLA, estado,
                                    new BigDecimal(objDatos.getIdPanel()), objDatos.getUsuario(), tipoPanel, "", ID_PAIS);

                            objRespuesta = getMensaje(Conf_Mensajes.OK_CAMBIOESTADO8, null, null, null, null, objDatos.getCodArea());
                            objSalida.setIdPanel(objDatos.getIdPanel());

                            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut,
                                    objDatos.getIdPanel(), Conf.LOG_TIPO_PANEL,
                                    "Se cambi\u00F3 el estado de la panel con ID " + objDatos.getIdPanel() + ".", ""));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            log.trace(e.getErrorCode());
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut,
                    objDatos.getIdPanel() != null ? objDatos.getIdPanel() : "0", Conf.LOG_TIPO_PANEL,
                    "Problema al modificar panel.", e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(), metodo,
                    "", objDatos.getCodArea());

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PANEL, servicioPut,
                    objDatos.getIdPanel() != null ? objDatos.getIdPanel() : "0", Conf.LOG_TIPO_PANEL,
                    "Problema al modificar panel.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            objSalida.setRespuesta(objRespuesta);

            UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
        }

        return objSalida;
    }

    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al
     * servicio.
     * 
     * @param input
     * @param metodo
     * @return Respuesta
     */
    public Respuesta validarInput(InputPanel input, int metodo) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        log.debug("Validando datos...");
        
        if (input.getIdPanel() == null || !isNumeric(input.getIdPanel())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPANEL_NUM_252, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getIdDistribuidor() == null) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_161, null, nombreClase, nombreMetodo, null, input.getCodArea())
                    .getDescripcion();
            flag = true;
        } else if (!isNumeric(input.getIdDistribuidor())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
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
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input
     * @param metodo
     * @return respuestaRuta
     */
    public OutputPanel getDatos(InputPanel input, int metodo) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        List<Respuesta> respuesta = new ArrayList<Respuesta>();
        OutputPanel output = null;
        Respuesta r = new Respuesta();
        log.trace("Usuario: " + input.getUsuario());

        Connection conn = null;
        try {
            conn = getConnRegional();
            BigDecimal idPais = getIdPais(conn, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            r = validarInput(input, metodo);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equals(r.getDescripcion())) {
                respuesta.add(r);
                output = new OutputPanel();
                output.setRespuesta(r);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

                return output;
            }

            log.debug("Distribuidor: " + input.getIdDistribuidor());
            log.debug("ID Panel: " + input.getIdPanel());

            output = OperacionPanel.doGet(conn,  input, idPais);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de paneles.", ""));

        } catch (SQLException e) {
            r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputPanel();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de paneles.", e.getMessage()));

        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputPanel();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de paneles.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }

    /**
     * M\u00E9todo para validar los datos enviados al servicio.
     * 
     * @param input
     * @return
     */
    private Respuesta validarInput(InputVendedor input) {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        boolean flag = false;
        log.trace("Validando datos...");

        if (input.getVendedor() == null || "".equals(input.getVendedor().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_137, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
        } else if (!isNumeric(input.getVendedor())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase,
                    nombreMetodo, null, input.getCodArea()).getDescripcion();
            flag = true;
        }

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo,
                    null, input.getCodArea()).getDescripcion();
            flag = true;
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
     * Funci\u00F3n que permite consultar y devolver los datos de las paneles registradas mediante el ID de un vendedor.
     * 
     * @param input
     * @return
     */
    public OutputPanel getDatos(InputVendedor input) {
        listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();
        List<Respuesta> respuesta = new ArrayList<Respuesta>();
        OutputPanel output = null;
        Respuesta r = new Respuesta();

        Connection conn = null;
        try {
            conn = getConnRegional();

            // Validaci\u00F3n de datos en el input
            r = validarInput(input);
            log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
            if (!"OK".equals(r.getDescripcion())) {
                respuesta.add(r);
                output = new OutputPanel();
                output.setRespuesta(r);
                
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));
                
                return output;
            }

            log.debug("ID Vendedor: " + input.getVendedor());
            output = OperacionPanel.doGetByVendedor(conn, input.getVendedor(), input.getCodArea());

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de paneles por vendedor.", ""));

        } catch (SQLException e) {
            r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputPanel();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de paneles por vendedor.", e.getMessage()));

        } catch (Exception e) {
            r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().getName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output = new OutputPanel();
            output.setRespuesta(r);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de paneles por vendedor.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}

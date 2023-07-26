package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.buzon.InputBuzon;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.solicitud.ArticulosDevolucion;
import com.consystec.sc.ca.ws.input.solicitud.InputAceptaRechazaDevolucion;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.sv.ws.operaciones.OperacionBuzon;
import com.consystec.sc.sv.ws.operaciones.OperacionDevolucion;
import com.consystec.sc.sv.ws.operaciones.OperacionIngresoSalida;
import com.consystec.sc.sv.ws.operaciones.OperacionMovimientosInventario;
import com.consystec.sc.sv.ws.orm.BuzonSidra;
import com.consystec.sc.sv.ws.orm.ConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.orm.HistoricoInv;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.SolicitudDet;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author susana barrios Consystec 2015
 */
public class CtrlDevolucion extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlDevolucion.class);
    List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static List<HistoricoInv> listaHistorico = new ArrayList<HistoricoInv>();
    private static String servicioPost = Conf.LOG_WS_ACEPTA_RECHAZA_DEVOLUCION;
    static String transaccionLog="";
    /***
     * Validando que no vengan par\u00E9metros nulos
     */
    public Respuesta validarDatos(InputAceptaRechazaDevolucion objDatos) {
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        }

        if (objDatos.getIdSolicitud() == null || "".equals(objDatos.getIdSolicitud())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDSOLICITUD_NULO_51, this.getClass().toString(), null, metodo,
                    null, objDatos.getCodArea());
        }

        if (objDatos.getIdBodega() == null || "".equals(objDatos.getIdBodega())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_BODEGA_NULA_64, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        }

        if (objDatos.getArticulos() == null || objDatos.getArticulos().size() == 0) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_ARTICULOS_NULO_48, this.getClass().toString(), null, metodo,
                    null, objDatos.getCodArea());
        } else {
            int a = 0;
            for (ArticulosDevolucion obj : objDatos.getArticulos()) {
                a++;
                if (obj.getTcscSolicitudId() == null || "".equals(obj.getTcscSolicitudId().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_TCSCSOLICITUDDETID_NULO_65, this.getClass().toString(),
                            null, metodo, "El elemento " + a + " de la lista de articulos se encuentra incompleto.", objDatos.getCodArea());
                }
                if((obj.getCodDispositivo()==null || "".equals(obj.getCodDispositivo()))&& (obj.getIdArticulo() == null || "".equals(obj.getIdArticulo().trim()))) {
	                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDARTICULO_NULO_49, this.getClass().toString(), null,
	                            metodo, "El elemento " + a + " de la lista de articulos se encuentra incompleto.", objDatos.getCodArea());
                }

                if (obj.getAceptado() == null || "".equals(obj.getAceptado().trim())) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_ACEPTADO_NULO_50, this.getClass().toString(), null,
                            metodo, "El elemento " + a + " de la lista de articulos se encuentra incompleto.", objDatos.getCodArea());
                } else if (!("0".equals(obj.getAceptado()) || "1".equals(obj.getAceptado()))) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_ACEPTADO_NOVALIDO_54, this.getClass().toString(), null,
                            metodo, null, objDatos.getCodArea());
                }
            }
        }

        if (objRespuesta != null) {
            log.trace("resultado:" + objRespuesta.getDescripcion());
        }

        return objRespuesta;
    }
    
    /**
     * M\u00E9todo que permite rechazar o aceptar solicitudes de siniestro
     * @param objDatos
     * @return
     */
    public Respuesta aceptaRechaza(InputAceptaRechazaDevolucion objDatos) {
        Respuesta objRespuesta = new Respuesta();
        List<String> listaExiste = new ArrayList<String>();
        SolicitudDet objSol = new SolicitudDet();
        List<Inventario> lstArticulosBodPapa = new ArrayList<Inventario>();
        List<Inventario> lstArticulosBodVirtual = new ArrayList<Inventario>();
        List<Inventario> articulosSolicitud = new ArrayList<Inventario>();
        List<InputBuzon> lstBuzon = new ArrayList<InputBuzon>();

        BigDecimal idBodegaPapa = null;
        BigDecimal tipoTransaccion = null;

        Connection conn = null;
        String metodo = "aceptaRechaza";
        String DEVOLUCION = "";
        String SINIESTRO = "";
        String EstadoCerrado = "";
        String EstadoAbierta = "";
        String EstadoDevolucion = "";
        String EstadoDisponible = "";
        String EstadoAceptada = "";
        String EstadoRechazada = "";
        String EstadoFinalizada = "";
        String EstadoRechazadaTelca = "";
        String EstadoProcesoDevo = "";
        String estadoAlta = "";
        String estadoPendiente = "";
        String InvTelca = "";
        String InvSidra = "";

        String nombreBuzon = "";
        String tipoWFtodas = "";

        log.trace("inicia a validar valores...");
        objRespuesta = validarDatos(objDatos);
        if (objRespuesta == null) {
            try {
                conn = getConnRegional();
                conn.setAutoCommit(false);
                BigDecimal idPais = getIdPais(conn, objDatos.getCodArea());

                //obteniendo parametros
                estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());
                EstadoAbierta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD, Conf.SOL_ESTADO_ABIERTA, objDatos.getCodArea());
                estadoPendiente = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD, Conf.SOL_ESTADO_PENDIENTE, objDatos.getCodArea());
                EstadoFinalizada = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD, Conf.SOL_ESTADO_FINALIZADA, objDatos.getCodArea());
                EstadoRechazadaTelca = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD, Conf.SOL_ESTADO_RECHAZADA_TELCA, objDatos.getCodArea());
                tipoWFtodas = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPO, Conf.SOL_TIPO_TODAS, objDatos.getCodArea());
                boolean insertoHist=false;
                DEVOLUCION = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPO, Conf.SOL_TIPO_DEVOLUCION, objDatos.getCodArea());
                SINIESTRO = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPO, Conf.SOL_TIPO_SINIESTRO, objDatos.getCodArea());

                listaExiste = OperacionDevolucion.existeSol(conn, new BigDecimal(objDatos.getIdSolicitud()),
                         estadoPendiente, estadoAlta);

                if (!listaExiste.isEmpty()) {
                    if (listaExiste.get(1).equalsIgnoreCase(DEVOLUCION)) {
                		//agregado para devolver a telefonica directamente 26-06-16
                        List<Filtro> lstFiltros = new ArrayList<Filtro>();
                        lstFiltros.add(new Filtro("UPPER(" + BuzonSidra.CAMPO_TCSCBUZONID + ")", Filtro.EQ, listaExiste.get(5)));
                        nombreBuzon = UtileriasBD.getOneRecord(conn, BuzonSidra.CAMPO_NOMBRE, BuzonSidra.N_TABLA, lstFiltros);
                        log.trace("NOMBRE BUZON SOLICITUD:" + nombreBuzon);
                        //fin agregado

                    } else if (listaExiste.get(1).equalsIgnoreCase(SINIESTRO)) {
                        transaccionLog = Conf.LOG_TRANSACCION_ACEPTA_SINIESTRO;
                    }
                }

                log.trace("CANTIDAD INGRESADA:" + objDatos.getArticulos().size());
                if (listaExiste.isEmpty()) {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_SOLICITUD_NOEXISTE_52, this.getClass().toString(), null,
                            metodo, null, objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

                } else if (new BigDecimal(listaExiste.get(3)).intValue() != objDatos.getArticulos().size()) {

                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_CANT_ART_INCORRECTA_63, this.getClass().toString(),
                            null, metodo, null, objDatos.getCodArea());

                    listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

                } else {

                    log.trace("tipo Devolucion:" + listaExiste.get(1));
                    if ("1".equals(listaExiste.get(0))) {

                        if (listaExiste.get(1).equalsIgnoreCase(DEVOLUCION) || listaExiste.get(1).equalsIgnoreCase(SINIESTRO)) {

                            if (listaExiste.get(2).equals(objDatos.getIdBodega())) {
                                String razonesLog = "";

                                EstadoCerrado = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD, Conf.SOL_ESTADO_CERRADA, objDatos.getCodArea());
                                EstadoAceptada = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD,Conf.SOL_ESTADO_ACEPTADA, objDatos.getCodArea());
                                EstadoRechazada = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD,Conf.SOL_ESTADO_RECHAZADA, objDatos.getCodArea());
                                EstadoDisponible = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO,Conf.INV_EST_DISPONIBLE, objDatos.getCodArea());

                                InvTelca = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPOINV, Conf.SOL_TIPOINV_TELCA, objDatos.getCodArea());
                                InvSidra = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPOINV, Conf.SOL_TIPOINV_SIDRA, objDatos.getCodArea());

                                // obteniendo parametros a utilizar por tipo de solicitud
                                if (listaExiste.get(1).equalsIgnoreCase(DEVOLUCION)) {

                                    tipoTransaccion = getIdTransaccion(conn, Conf.T_DEVOLUCION, idPais);
                                    EstadoDevolucion = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DEVUELTO, objDatos.getCodArea());
                                    EstadoProcesoDevo = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DEVOLUCION, objDatos.getCodArea());
                                    razonesLog = "Devoluci\u00F3n";
                                    transaccionLog = Conf.LOG_TRANSACCION_ACEPTA_DEVOLUCION;

                                } else if (listaExiste.get(1).equalsIgnoreCase(SINIESTRO)) {

                                    tipoTransaccion = getIdTransaccion(conn, Conf.T_SINESTRO, idPais);
                                    EstadoDevolucion = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_SINIESTRADO, objDatos.getCodArea());
                                    EstadoProcesoDevo = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_SINIESTRO, objDatos.getCodArea());
                                    razonesLog = "Siniestro";
                                    transaccionLog = Conf.LOG_TRANSACCION_ACEPTA_SINIESTRO;

                                }

                                //si es nivel 2 o 3 y trae buzon siguiente se procedera a realizar acciones diferentes
                                if ((new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_BUZON_DTS 
                                		|| new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_ZONACOMERCIAL)&& 
                                		!((objDatos.getIdBuzonSiguiente()==null) || ("".equals(objDatos.getIdBuzonSiguiente()))))  {

                                    if ((new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_BUZON_DTS
                                            && (!listaExiste.get(4).equalsIgnoreCase(EstadoAbierta)
                                            && !listaExiste.get(4).equalsIgnoreCase(EstadoAceptada)))
                                        || (new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_ZONACOMERCIAL
                                            && !listaExiste.get(4).equalsIgnoreCase(EstadoAbierta))) {

                                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_SOL_ESTADO_ERRONEO,
                                                this.getClass().toString(), null, metodo, EstadoAbierta, objDatos.getCodArea());

                                        listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0",
                                                Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                                                objRespuesta.getDescripcion()));

                                    } else {
                                        //si tiene buzon siquiente se verifica 
                                        if (objDatos.getIdBuzonSiguiente() != null && !"".equals(objDatos.getIdBuzonSiguiente())) {
                                        	
	                                            // verificando que el buz\u00F3n al que ser\u00E9 enviada la solicitud si corresponde a uno de telefonica
	                                            List<Filtro> lstFiltrosBuzon = new ArrayList<Filtro>();
	                                            lstFiltrosBuzon.add(new Filtro(BuzonSidra.N_TABLA + "." + BuzonSidra.CAMPO_TCSCBUZONID, Filtro.EQ, objDatos.getIdBuzonSiguiente()));
	                                            lstFiltrosBuzon.add(new Filtro("UPPER(" + BuzonSidra.N_TABLA + "." + BuzonSidra.CAMPO_ESTADO + ")", Filtro.EQ, "'" + estadoAlta + "'"));
	                                        
	                                            //segun el tipo de buzon asi se consultar\u00E9 el nivel
	                                            if(new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_BUZON_DTS 
	                                            			|| new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_ZONACOMERCIAL){
	                                            	lstFiltrosBuzon.add(new Filtro(BuzonSidra.N_TABLA + "." + BuzonSidra.CAMPO_NIVEL, Filtro.EQ, ((new BigDecimal(listaExiste.get(6)).intValue())-1)));
	                                            }else if (new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_BUZON_TELCA ){
	                                             	lstFiltrosBuzon.add(new Filtro(BuzonSidra.N_TABLA + "." + BuzonSidra.CAMPO_NIVEL, Filtro.EQ, Conf.BUZON_TELCA));
	                                            }
	                                            lstBuzon = OperacionBuzon.getBuzonesNivel1(conn, lstFiltrosBuzon);
	
	                                            if (lstBuzon.isEmpty()) {
	                                                objRespuesta = getMensaje(Conf_Mensajes.MSJ_BUZONSIG_INVALIDO_534, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
	                                                return objRespuesta;
	                                            } else if (!lstBuzon.get(0).getTipoWF().equalsIgnoreCase(tipoWFtodas)
	                                                    && !lstBuzon.get(0).getTipoWF().equalsIgnoreCase(DEVOLUCION)
	                                                    && !lstBuzon.get(0).getTipoWF().equalsIgnoreCase(SINIESTRO)) {
	
	                                                objRespuesta = getMensaje(Conf_Mensajes.MSJ_BUZONSIG_TIPOWF_INVALIDO_535, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
	                                                return objRespuesta;
	                                            }
                                        	
                                        }

                                        // obtener bodega papa
                                        //si no tiene buzon igual el dts puede aceptar o rechazar sin cambiar el buzon
                                        if (!lstBuzon.isEmpty() && lstBuzon.get(0).getNivel().equalsIgnoreCase(Conf.NIVEL_ZONACOMERCIAL + "")) {
                                            idBodegaPapa = OperacionDevolucion.getBodPapa(conn, objDatos.getIdSolicitud(),  "2");
                                        } else {
                                            idBodegaPapa = OperacionDevolucion.getBodPapa(conn, objDatos.getIdSolicitud(),  null);
                                        }

                                        // obtener articulos
                                        lstArticulosBodPapa = OperacionDevolucion.getInvDisponibles(conn,
                                                estadoPendiente, EstadoDisponible, objDatos.getIdSolicitud(), idBodegaPapa.toString());
                                        lstArticulosBodVirtual = OperacionDevolucion.getInvDisponibles(conn,
                                                estadoPendiente, EstadoDisponible, objDatos.getIdSolicitud(), objDatos.getIdBodega());

                                        // obteniendo los art\u00EDculos que se registraron en la solicitud de devolucion o siniestro
                                        articulosSolicitud = OperacionDevolucion.getInvSolicitud(conn,
                                                objDatos.getIdSolicitud(), estadoPendiente);

                                        int respuesta = atenderNivel2(conn, objDatos, EstadoAceptada, EstadoRechazada,
                                                listaExiste.get(5), listaExiste.get(1), articulosSolicitud,
                                                lstArticulosBodPapa, lstArticulosBodVirtual, InvTelca, InvSidra,
                                                EstadoProcesoDevo, EstadoDevolucion, idBodegaPapa, EstadoDisponible,
                                                estadoPendiente, estadoAlta, tipoTransaccion, razonesLog, idPais);
                                        log.trace("respuesta de atender nivel2:"+respuesta);                                      
                                        if (respuesta == 1) {
                                        	conn.rollback();
                                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOPROCESO_DATOS_SINIESTRO_523,
                                                    this.getClass().toString(), null, metodo, null, objDatos.getCodArea());

                                            listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0",
                                                    Conf.LOG_TIPO_NINGUNO,
                                                    "Problema en actualizaci\u00F3n de datos rechazados o aceptados.",
                                                    objRespuesta.getDescripcion()));
                                        } else {
                                            log.trace("TAMANIO DE REGISTROS HISTORICO:" + listaHistorico.size());
                                            if (!listaHistorico.isEmpty()) {
                                                log.trace("REGISTRA HISTORICO");
                                                insertoHist=insertaHistorico(conn, listaHistorico);
                                            }else{
                                            	insertoHist=true;
                                            }
                                            if(insertoHist){
	                                            conn.commit();
	                                            objRespuesta = getMensaje(Conf_Mensajes.OK_ACEPTARECHAZA_DEV13, null, null, null, null, objDatos.getCodArea());
	
	             
                                            }else{
                                                objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_INSERTO_HISTORICO_546,
                                                        this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
                                            }
                                        }
                                    }
                                }

                                //trabajando en buzon de telca O dts y zona comercial si no trae buzon siguiente ser\u00E9 finalizada la operacion
                                if (new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_BUZON_TELCA || 
                                		((new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_BUZON_DTS ||  
                                		new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_ZONACOMERCIAL) 
                                				&& (objDatos.getIdBuzonSiguiente()==null || "".equals(objDatos.getIdBuzonSiguiente())))) {

                                	
                                	
                                	//validando estado de solicitud para dts interno 
                                    if (listaExiste.get(1).equalsIgnoreCase(DEVOLUCION) && listaExiste.get(8).equalsIgnoreCase(Conf.DTS_INTERNO)) {
                                    	if(!listaExiste.get(4).equalsIgnoreCase(EstadoAceptada) && !listaExiste.get(4).equalsIgnoreCase(EstadoAbierta)){
                                    		objRespuesta = getMensaje(Conf_Mensajes.MSJ_SOL_ESTADO_ERRONEO,
                                                    this.getClass().toString(), null, metodo, EstadoAbierta+"/"+EstadoAceptada, objDatos.getCodArea());

                                            listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0",
                                                    Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                                                
                                                    objRespuesta.getDescripcion()));
                                    	}
                                    	else if(listaExiste.get(4).equalsIgnoreCase(EstadoAceptada)){
	                                    	if (!(new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_BUZON_TELCA ||new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_BUZON_DTS )) {
	                                       
		                                    	objRespuesta = getMensaje(Conf_Mensajes.MSJ_SOL_ESTADO_ERRONEO,
		                                                this.getClass().toString(), null, metodo, EstadoAbierta+"/"+EstadoAceptada, objDatos.getCodArea());
		
		                                        listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0",
		                                                Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
		                                                objRespuesta.getDescripcion()));
	                                    	}
                                    	}else if (listaExiste.get(4).equalsIgnoreCase(EstadoAbierta)&& (!(new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_BUZON_DTS || new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_ZONACOMERCIAL))){
                                    			objRespuesta = getMensaje(Conf_Mensajes.MSJ_SOL_ESTADO_ERRONEO,
		                                                this.getClass().toString(), null, metodo, EstadoAbierta+"/"+EstadoAceptada, objDatos.getCodArea());
		
		                                        listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0",
		                                                Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
		                                                objRespuesta.getDescripcion()));
                                    	}
                                     //validando estado de solicitud para dts externo
                                        
                                    }else if (listaExiste.get(1).equalsIgnoreCase(DEVOLUCION) && listaExiste.get(8).equalsIgnoreCase(Conf.DTS_EXTERNO)){
                                    	if(!listaExiste.get(4).equalsIgnoreCase(EstadoAceptada) && !listaExiste.get(4).equalsIgnoreCase(EstadoAbierta)){
                                    		objRespuesta = getMensaje(Conf_Mensajes.MSJ_SOL_ESTADO_ERRONEO,
                                                    this.getClass().toString(), null, metodo, EstadoAbierta+"/"+EstadoAceptada, objDatos.getCodArea());

                                            listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0",
                                                    Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                                                
                                                    objRespuesta.getDescripcion()));
                                    	}
                                    	else if(listaExiste.get(4).equalsIgnoreCase(EstadoAceptada)){
                                    		
                                    		if (new BigDecimal(listaExiste.get(6)).intValue() != Conf.NIVEL_BUZON_TELCA){
                                    			
                                    			objRespuesta = getMensaje(Conf_Mensajes.MSJ_SOL_ESTADO_ERRONEO,
                                                        this.getClass().toString(), null, metodo, EstadoAbierta+"/"+EstadoAceptada, objDatos.getCodArea());

                                                listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0",
                                                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                                                    
                                                        objRespuesta.getDescripcion()));
                                    		}
                                    	}else if (listaExiste.get(4).equalsIgnoreCase(EstadoAbierta)&& (new BigDecimal(listaExiste.get(6)).intValue() != Conf.NIVEL_BUZON_DTS && new BigDecimal(listaExiste.get(6)).intValue() != Conf.NIVEL_BUZON_TELCA)){
                                    			objRespuesta = getMensaje(Conf_Mensajes.MSJ_SOL_ESTADO_ERRONEO, this.getClass().toString(), null, metodo, EstadoAbierta+"/"+EstadoAceptada, objDatos.getCodArea());

                                                listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0",
                                                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                                                    
                                                        objRespuesta.getDescripcion()));
                                    	}
                                  
                                    	
                                    } 
                                    else if (listaExiste.get(1).equalsIgnoreCase(SINIESTRO)&& 
                                    		(new BigDecimal(listaExiste.get(6)).intValue() != Conf.NIVEL_BUZON_TELCA)) {
                                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_NIVEL_BUZON_INCORRECTO_615,
                                                    this.getClass().toString(), null, metodo, null, objDatos.getCodArea());

                                            listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0",
                                                    Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
                                                    objRespuesta.getDescripcion()));

                                        
                                    }
                                    	if (objRespuesta == null )
                                     {
                                    	String estadoDevolucionNew="";
                                    		if ((new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_ZONACOMERCIAL 
                                    				|| new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_BUZON_DTS) &&
                                    				listaExiste.get(1).equalsIgnoreCase(DEVOLUCION)){
                                    			estadoDevolucionNew=EstadoDisponible;
                                    		}else{
                                    			estadoDevolucionNew= EstadoDevolucion;
                                    		}
                                    		
                                    		  if (new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_ZONACOMERCIAL ) {
                                                  idBodegaPapa = OperacionDevolucion.getBodPapa(conn, objDatos.getIdSolicitud(),  "2");
                                              } else {
                                                  idBodegaPapa = OperacionDevolucion.getBodPapa(conn, objDatos.getIdSolicitud(),  null);
                                              }

                                        // obtener articulos
                                        lstArticulosBodPapa = OperacionDevolucion.getInvDisponibles(conn,
                                                estadoPendiente, EstadoDisponible, objDatos.getIdSolicitud(), idBodegaPapa.toString());
                                        lstArticulosBodVirtual = OperacionDevolucion.getInvDisponibles(conn,
                                                estadoPendiente, EstadoDisponible, objDatos.getIdSolicitud(), objDatos.getIdBodega());

                                        //obteniendo los art\u00EDculos que se registraron en la solicitud de devolucion o siniestro
                                        articulosSolicitud = OperacionDevolucion.getInvSolicitud(conn,
                                                objDatos.getIdSolicitud(), estadoPendiente);

		                                //recorriendo input registro por registro del detalle devoluci\u00F3n
                                        for (ArticulosDevolucion obj : objDatos.getArticulos()) {
                                            if (obj.getCodDispositivo() == null || "".equals(obj.getCodDispositivo())
                                                    && (!("".equals(obj.getIdArticulo())
                                                            || obj.getIdArticulo() == null))) {
                                                objSol = new SolicitudDet();
                                                log.trace("articulo:" + obj.getIdArticulo());

                                                objSol = new SolicitudDet();
                                                objSol = aceptaRechazaArt(conn, articulosSolicitud, EstadoAceptada, obj,
                                                        EstadoRechazada, lstArticulosBodPapa, lstArticulosBodVirtual,
                                                        InvTelca, InvSidra, EstadoProcesoDevo, estadoDevolucionNew,
                                                        idBodegaPapa, objDatos, EstadoDisponible, estadoAlta,
                                                        tipoTransaccion, razonesLog, 0, estadoPendiente, idPais);

                                            } else {

                                                // aceptandorechazando Dispositivos
                                                objSol = new SolicitudDet();
                                                objSol = aceptarRechazarDispositivos(conn, objDatos, estadoPendiente,
                                                        EstadoRechazada, estadoAlta, EstadoAceptada, obj, 0);
                                            }

                                            if (!(objSol.getEstado() == null || "".equals(objSol.getEstado()))) {
                                                log.trace("ESTADO DETALLE:" + objSol.getEstado());
                                                if (!(obj.getIdArticulo() == null || "".equals(obj.getIdArticulo()))) {
                                                    objSol.setArticulo(new BigDecimal(obj.getIdArticulo()));
                                                }

                                                objSol.setCod_dispositivo(obj.getCodDispositivo());
                                                objSol.setTcscsolicituddetid(new BigDecimal(obj.getTcscSolicitudId()));
                                                objSol.setObservaciones(obj.getObservaciones());
                                                objSol.setModificado_por(objDatos.getUsuario());

                                                OperacionDevolucion.updateDetSolicitud(conn, objSol);

                                                listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0",
                                                        Conf.LOG_TIPO_NINGUNO,
                                                        "Se actualizaron datos del detalle de la solicitud de "
                                                                + razonesLog + " con ID " + objDatos.getIdSolicitud()
                                                                + ".",
                                                        ""));
                                            }
                                        }

		                                //verificando que todos los items de la solicitud hayan sido procesados
                                        BigDecimal cantPendientes = null;

                                        cantPendientes = OperacionDevolucion.getPendientes(conn, objDatos.getIdSolicitud(), estadoPendiente);

                                        if (cantPendientes.intValue() == 0) {
                                        	
                                            String estadoUpdate = "";
                                            // obteniendo estado a registrar de solicitud
                                            int cantRechazados = 0;
                                            for (int a = 0; a < objDatos.getArticulos().size(); a++) {
                                                if ("0".equals(objDatos.getArticulos().get(a).getAceptado())) {
                                                    cantRechazados++;
                                                }
                                            }

                                            if (objDatos.getArticulos().size() == cantRechazados) {
                                                if (new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_BUZON_TELCA) {
                                                    estadoUpdate = EstadoRechazadaTelca;
                                                } else {
                                                    estadoUpdate = EstadoRechazada;
                                                }

                                            } else {
                                                estadoUpdate = EstadoFinalizada;
                                            }

                                            OperacionDevolucion.updateSolicitud(conn, estadoUpdate,
                                                    objDatos.getUsuario(), objDatos.getIdSolicitud(), null,
                                                    new BigDecimal(listaExiste.get(5)), null);
                                            listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0",
                                                    Conf.LOG_TIPO_NINGUNO,
                                                    "Se actualiz\u00F3 la observaci\u00F3n de la solicitud de " + razonesLog
                                                            + " con ID " + objDatos.getIdSolicitud()
                                                            + " y se cambi\u00F3 el estado a " + EstadoCerrado + ".",
                                                    ""));

                                            log.trace("TAMANIO DE REGISTROS HISTORICO:" + listaHistorico.size());
                                            if (!listaHistorico.isEmpty()) {
                                                log.trace("REGISTRA HISTORICO");
                                                insertoHist=insertaHistorico(conn, listaHistorico);
                                            }else{
                                            	insertoHist=true;
                                            }
                                            if(insertoHist){
	                                            conn.commit();
	                                            objRespuesta = getMensaje(Conf_Mensajes.OK_ACEPTARECHAZA_DEV13, null, null, null, null, objDatos.getCodArea());
	
	                                            // se notifica al distribuidor el cambio de la solicitud solo si fue buzon telca
	                                            if (new BigDecimal(listaExiste.get(6)).intValue() == Conf.NIVEL_BUZON_TELCA){
	                                                CtrlSolicitud.notificarDTS(conn, listaExiste.get(7), estadoUpdate,
	                                                        objDatos.getUsuario(), objDatos.getIdSolicitud(), objDatos.getCodArea(), idPais);
	                                            }
                                            }else{
                                                objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_INSERTO_HISTORICO_546,
                                                        this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
                                            }
                                        } else {
                                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOPROCESO_DATOS_SINIESTRO_523,
                                                    this.getClass().toString(), null, metodo, null, objDatos.getCodArea());

                                            listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0",
                                                    Conf.LOG_TIPO_NINGUNO,
                                                    "Problema en actualizaci\u00F3n de datos rechazados o aceptados.",
                                                    objRespuesta.getDescripcion()));
                                        }
                                    }
                                }

                            } else {
                                objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_BODEGA_ORIGEN_110,
                                        this.getClass().toString(), null, metodo, null, objDatos.getCodArea());

                                listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                                                "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
                            }

                        } else {
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOSOLICITUD_NOVALIDO_53,
                                    this.getClass().toString(), null, metodo, DEVOLUCION + " o " + SINIESTRO, objDatos.getCodArea());

                            listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                                            "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
                        }

                    } else {
                        objRespuesta = getMensaje(Conf_Mensajes.MSJ_SOLICITUD_NOEXISTE_52, this.getClass().toString(),
                                null, metodo, null, objDatos.getCodArea());

                        listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                                "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
                    }
                }

            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                log.trace(e.getErrorCode());
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, "", objDatos.getCodArea());

                try {
                    conn.rollback();

                    listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema al aceptar o rechazar devoluci\u00F3n o siniestro.", e.getMessage()));

                } catch (SQLException e1) {
                    log.error(e1.getMessage(), e1);
                    listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema al realizar el rollback en el servicio de aceptar o rechazar devoluci\u00F3n o siniestro.",
                            e1.getMessage()));
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), this.getClass().toString(),
                        metodo, "", objDatos.getCodArea());

                try {
                    conn.rollback();

                    listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema al aceptar o rechazar devoluci\u00F3n o siniestro.", e.getMessage()));
                } catch (SQLException e1) {
                    log.error(e1.getMessage(), e1);
                    listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                            "Problema al realizar el rollback en el servicio de aceptar o rechazar devoluciones o siniestros.",
                            e1.getMessage()));
                }
            } finally {
                DbUtils.closeQuietly(conn);

                UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
            }
        }

        return objRespuesta;
    }

    /**
     * M\u00E9todo para armar objeto de historico de inventario
     * 
     * @param conn
     * @param obj
     * @param usuario
     * @param estado
     * @param tipoInv
     * @return
     */
    public HistoricoInv armaHistorico(ArticulosDevolucion obj, String usuario, String estado, String tipoInv,
            BigDecimal idTransaccion, String bodegaOrigen, String bodegaDestino, String serie, String serieAsociada,
            String cantidad, String serieFinal, BigDecimal idPais) {
        HistoricoInv objHist = new HistoricoInv();
        log.trace("SERIE FINAL:"+ serieFinal);
        objHist.setArticulo(obj.getIdArticulo());
        objHist.setBodega_origen(new BigDecimal(bodegaOrigen));
        objHist.setBodega_destino(new BigDecimal(bodegaDestino));
        objHist.setCantidad(new BigDecimal(cantidad));
        objHist.setCreado_por(usuario);
        objHist.setTipo_inv(tipoInv);
        objHist.setEstado(estado);
        objHist.setTcsctipotransaccionid(idTransaccion);
        objHist.setSerie(serie);
        objHist.setSerie_asociada(serieAsociada);
        objHist.setSerie_final(serieFinal);
        objHist.setTcScCatPaisId(idPais);
        return objHist;
    }
    
    
    /**
     * M\u00E9todo para aceptar/rechazar solicitudes que se encuentran en buz\u00F3n de nivel2
     * @param conn
     * @param objDatos
     * @param estadoAceptada
     * @param estadoRechazada
     * @param buzonActual
     * @param tipoSolicitud
     * @throws SQLException
     */
    public int atenderNivel2(Connection conn, InputAceptaRechazaDevolucion objDatos, String estadoAceptada,
            String estadoRechazada, String buzonActual, String tipoSolicitud, List<Inventario> articulosSolicitud,
            List<Inventario> lstArticulosBodPapa, List<Inventario> lstArticulosBodVirtual, String InvTelca,
            String InvSidra, String EstadoProcesoDevo, String EstadoDevolucion, BigDecimal idBodegaPapa,
            String EstadoDisponible, String estadoPendiente, String estadoAlta, BigDecimal tipoTransaccion,
            String razonesLog, BigDecimal idPais) throws SQLException {

        int ret = 0;
        Boolean rechazado = false;
        SolicitudDet objSol ;

        // validando que no existan objetos rechazados
        for (int a = 0; a < objDatos.getArticulos().size(); a++) {
            if ("0".equals(objDatos.getArticulos().get(a).getAceptado())) {
                rechazado = true;
                break;
            }
        }
        log.trace("tiene rechazos:"+rechazado);
            if (!rechazado) { // aceptando solicitud para buzon siguiente

                if (objDatos.getIdBuzonSiguiente() != null && !"".equals(objDatos.getIdBuzonSiguiente())) {
                    OperacionDevolucion.updateSolicitud(conn, estadoAceptada, objDatos.getUsuario(),
                            objDatos.getIdSolicitud(), null, new BigDecimal(objDatos.getIdBuzonSiguiente()),
                            buzonActual);
                } else {
                    OperacionDevolucion.updateSolicitud(conn, estadoAceptada, objDatos.getUsuario(),
                            objDatos.getIdSolicitud(), null, new BigDecimal(buzonActual), null);
                }
                
                listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                        "Se actualiz\u00F3  la solicitud de " + tipoSolicitud + " con ID " + objDatos.getIdSolicitud()
                                + " y se cambi\u00F3 el estado a " + estadoAceptada
                                + ", para ser atentida por buz\u00F3n de telca.",
                        ""));
            } else {
                // recorriendo input registro por registro del detalle devoluci\u00F3n
                for (ArticulosDevolucion obj : objDatos.getArticulos()) {
                    if (obj.getCodDispositivo() == null || "".equals(obj.getCodDispositivo())
                            && (!(obj.getIdArticulo() == null || "".equals(obj.getIdArticulo().trim())))) {
                        log.trace("articulo:" + obj.getIdArticulo());
                        objSol = aceptaRechazaArt(conn, articulosSolicitud, estadoAceptada, obj, estadoRechazada,
                                lstArticulosBodPapa, lstArticulosBodVirtual, InvTelca, InvSidra, EstadoProcesoDevo,
                                EstadoDevolucion, idBodegaPapa, objDatos, EstadoDisponible, estadoAlta, tipoTransaccion,
                                razonesLog, 1, estadoPendiente, idPais);
                    } else {

                        // aceptandorechazando Dispositivos
                        objSol = aceptarRechazarDispositivos(conn, objDatos, estadoPendiente, estadoRechazada,
                                estadoAlta, estadoAceptada, obj, 1);
                    }
                    if (!(objSol.getEstado() == null || "".equals(objSol.getEstado()))) {
                        log.trace("ESTADO DETALLE:" + objSol.getEstado());
                        if (!(obj.getIdArticulo() == null || "".equals(obj.getIdArticulo()))) {
                            objSol.setArticulo(new BigDecimal(obj.getIdArticulo()));
                        }
						
                        objSol.setCod_dispositivo(obj.getCodDispositivo());
                        objSol.setTcscsolicituddetid(new BigDecimal(obj.getTcscSolicitudId()));
                        objSol.setObservaciones(obj.getObservaciones());
                        objSol.setModificado_por(objDatos.getUsuario());

                        OperacionDevolucion.updateDetSolicitud(conn, objSol);
                        listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
                                "Se actualizaron datos del detalle de la solicitud de " + razonesLog + " con ID "
                                        + objDatos.getIdSolicitud() + ".",
                                ""));
                    }
                }

                // verificando que todos los items de la solicitud hayan sido procesados
                BigDecimal cantPendientes = null;

                cantPendientes = OperacionDevolucion.getPendientes(conn, objDatos.getIdSolicitud(), estadoPendiente);

                if (cantPendientes.intValue() == 0) {
                    OperacionDevolucion.updateSolicitud(conn, estadoRechazada, objDatos.getUsuario(),
                            objDatos.getIdSolicitud(), null, new BigDecimal(buzonActual), null);
                    listaLog.add(ControladorBase.addLog(transaccionLog, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se actualiz\u00F3  la solicitud de " + tipoSolicitud + " con ID "
                                    + objDatos.getIdSolicitud() + " y se cambi\u00F3 el estado a " + estadoRechazada + ".",
                            ""));
                    log.trace("termina de hacer update a todo");
                    log.trace("ret:"+ret);
                } else {
                    log.trace("no se pudo aactualizar todo");
                    // si se retorna 1 quiere decir que no se pudo procesar correctamente la solicitud
                    ret = 1;
                }
            }
        log.trace("ret:"+ret);
        return ret;
    }
	
	/***
	 * M\u00E9todo para aceptar rechazar art\u00EDculos en caso fuera Siniestro
	 * @param conn
	 * @param objDatos
	 * @param estadoPendiente
	 * @param EstadoRechazada
	 * @param estadoAlta
	 * @param EstadoAceptada
	 * @param obj
	 * @param rechaza
	 * @return
	 * @throws SQLException
	 */
	
	public SolicitudDet aceptarRechazarDispositivos(Connection conn,
			InputAceptaRechazaDevolucion objDatos, String estadoPendiente,
			String EstadoRechazada, String estadoAlta, String EstadoAceptada, ArticulosDevolucion obj, int rechaza) throws SQLException {

		SolicitudDet objSol = new SolicitudDet();
		
        //variables y objetos para aceptar o rechazar dispositivos
        List<SolicitudDet> lstDispositivos ;
        List<ConfiguracionFolioVirtual> lstFolios ;
        String estadoPendienteSiniestro="";
        String estadoSiniestrado="";
        String estadoenUso="";
        String tipoDispositivo="";
        String folioManual="";
        
		//obteniendo dispositivos de solicitud
		lstDispositivos= OperacionDevolucion.getDetalleDispositivos(conn, objDatos.getIdSolicitud(), estadoPendiente);
		
		tipoDispositivo=UtileriasJava.getConfig(conn, Conf.GRUPO_TIPO_FOLIO, Conf.TIPO_DISPOSITIVOS, objDatos.getCodArea());
		estadoPendienteSiniestro=UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SINIESTROS, Conf.SINIESTRO_ESTADO_EN_PROCESO, objDatos.getCodArea());
		estadoSiniestrado=UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SINIESTROS, Conf.SINIESTRO_ESTADO_SINIESTRADO, objDatos.getCodArea());
		estadoenUso=UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_FOLIOS, Conf.FOLIO_EN_USO, objDatos.getCodArea());
		folioManual=UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.FOLIO_MANUAL, objDatos.getCodArea());
		
		
		for (int a=0; a<lstDispositivos.size(); a++){
			if(obj.getCodDispositivo().equalsIgnoreCase(lstDispositivos.get(a).getCod_dispositivo())){
				//verificando si el dispositivo fue aceptado o rechazado
				if("1".equals(obj.getAceptado()) && (rechaza==0)){
					log.trace("ESTADO ACEPTADA:"+EstadoAceptada);
					objSol.setEstado(EstadoAceptada);
					OperacionDevolucion.updateDispositivo(conn, estadoSiniestrado, estadoPendienteSiniestro, obj.getCodDispositivo());
					if("1".equals(folioManual)){
						//si tiene folios manuales se actualizar\u00E9n tambien a siniestrados
						OperacionDevolucion.updateFolio(conn, tipoDispositivo, obj.getCodDispositivo(), estadoSiniestrado, estadoPendienteSiniestro, null, 0);
					}
					 listaLog.add(addLog(transaccionLog,
		                        Conf.LOG_WS_ACEPTA_RECHAZA_DEVOLUCION, obj.getCodDispositivo(), Conf.LOG_TIPO_DISPOSITIVO,
		                        "Se acept\u00F3 en siniestro el dispositivo: "+ obj.getCodDispositivo(), ""));
				}else if ("0".equals(obj.getAceptado()) || (rechaza==1)){
					log.trace("ESTADO RECHAZADA:"+EstadoRechazada);
					objSol.setEstado(EstadoRechazada);
					OperacionDevolucion.updateDispositivo(conn, estadoAlta, estadoPendienteSiniestro, obj.getCodDispositivo());
					if("1".equals(folioManual)){
						//obteniendo folios manuales para saber si se pondra en alta o en uso
						lstFolios=OperacionDevolucion.getFoliosSiniestro(conn, estadoPendienteSiniestro, tipoDispositivo, obj.getCodDispositivo());
						 for (ConfiguracionFolioVirtual objFolio: lstFolios){
							 if("0".equals(objFolio.getCant_utilizados())){
								 OperacionDevolucion.updateFolio(conn, tipoDispositivo, obj.getCodDispositivo(), estadoAlta, estadoPendienteSiniestro, objFolio.getTcScFolioId().toString(), 1);
							 }else if(new BigDecimal(objFolio.getCant_utilizados()).intValue()>0){
								 OperacionDevolucion.updateFolio(conn, tipoDispositivo, obj.getCodDispositivo(), estadoenUso, estadoPendienteSiniestro, objFolio.getTcScFolioId().toString(), 1);
							 }
							 
						 }
					}
					
					 listaLog.add(addLog(transaccionLog,
		                        Conf.LOG_WS_ACEPTA_RECHAZA_DEVOLUCION, obj.getCodDispositivo(), Conf.LOG_TIPO_DISPOSITIVO,
		                        "Se rechaz\u00F3 en siniestro el dispositivo: "+ obj.getCodDispositivo(), ""));
				}
			}
		}
		
		return objSol;
	
	}
	
	/**
	 * M\u00E9todo para registrar articulos aceptados o rechazados por solicitudes
	 * @param conn
	 * @param articulosSolicitud
	 * @param EstadoAceptada
	 * @param obj
	 * @param EstadoRechazada
	 * @param lstArticulosBodPapa
	 * @param lstArticulosBodVirtual
	 * @param InvTelca
	 * @param InvSidra
	 * @param EstadoProcesoDevo
	 * @param EstadoDevolucion
	 * @param idBodegaPapa
	 * @param objDatos
	 * @param EstadoDisponible
	 * @param estadoAlta
	 * @param tipoTransaccion
	 * @param razonesLog
	 * @return
	 * @throws SQLException
	 */
	
	public  SolicitudDet aceptaRechazaArt(Connection conn,   List<Inventario> articulosSolicitud, String  EstadoAceptada, ArticulosDevolucion obj, 
			String EstadoRechazada,   List<Inventario> lstArticulosBodPapa,List<Inventario> lstArticulosBodVirtual, String InvTelca, String InvSidra, 
			String EstadoProcesoDevo, String EstadoDevolucion, BigDecimal idBodegaPapa, InputAceptaRechazaDevolucion objDatos, String EstadoDisponible,
			String estadoAlta, BigDecimal tipoTransaccion, String razonesLog, int rechazado, String estadoPendiente, BigDecimal idPais) throws SQLException {
		SolicitudDet objSol = new SolicitudDet();
       
		for (Inventario objViejo : articulosSolicitud) {

			// si el detalle id obtenido de mi query coincide con el input se
			// procede a aceptar o rechazar
			if (obj.getTcscSolicitudId().equals(
					objViejo.getTcscinventarioinvid().toString())) {
				// se realiza proceso para aceptados
				if ("1".equals(obj.getAceptado()) && rechazado==0) {
					objSol.setEstado(EstadoAceptada);
					int encontrado = 0;
					// verifica si existe en la bodega papa el art\u00EDculo
					for (Inventario objTemp : lstArticulosBodPapa) {
						if (obj.getIdArticulo().equals(objTemp.getArticulo().toString())) {
							encontrado = 1;
							break;
						}
					}

					if (obj.getIdArticulo().equals(objViejo.getArticulo().toString())) {
						log.trace("realiza update en bodega o en registro actual");
						//
						log.trace("TIPO INV:" + objViejo.getTipo_inv());
						log.trace("TIPO INV:" + InvTelca);
						log.trace("SERIADO:" + objViejo.getSeriado());
						log.trace("ENCONTRADO:" + encontrado);
						if (encontrado == 1	&& (objViejo.getSeriado() == null || "0".equals(objViejo.getSeriado()))&& 
								objViejo.getTipo_inv().trim().equalsIgnoreCase(InvTelca.trim())) {
							log.trace("realiza update en bodega  marco registro en estado devoluci\u00F3n");
							// validando si la devolucion se hace a telca o al dts

							log.trace("Devolucion hecha a dts");
							/*
							 * se comento esto porque alejandra solicito que
							 * todos los articulos aceptados en devoluci\u00F3n se
							 * marquen en la bodega pap\u00E9 con estado DEVOLUCION
							 */
					
							OperacionDevolucion.updateCantInventario(conn,
									obj.getIdArticulo(),
									idBodegaPapa.toString(), EstadoProcesoDevo,
									EstadoDevolucion, objDatos.getIdBodega(),
									objViejo.getTipo_inv(),
									objDatos.getUsuario(), 3,
									objDatos.getIdSolicitud(), null);

							listaHistorico.add(armaHistorico(obj, objDatos
									.getUsuario(), estadoAlta, objViejo
									.getTipo_inv(), tipoTransaccion, objDatos
									.getIdBodega(), idBodegaPapa.toString(),
									objViejo.getSerie(), objViejo
											.getSerie_asociada(), objViejo
											.getCantidad().toString(), null, idPais));

							listaLog.add(addLog(
									transaccionLog,
									Conf.LOG_WS_ACEPTA_RECHAZA_DEVOLUCION,
									obj.getIdArticulo(),
									Conf.LOG_TIPO_ARTICULO,
									"Se acept\u00F3 en devoluci\u00F3n "
											+ objViejo.getCantidad()
											+ " art\u00EDculo(s).", ""));
						} else if (((encontrado == 0 && objViejo.getTipo_inv().trim().equalsIgnoreCase(InvTelca.trim())) || 
								"1".equals(objViejo.getSeriado()))	&& objViejo.getSeriefinal() == null) {
							String razonLog = "";
							log.trace("Devolucion hecha a dts");
							OperacionDevolucion.updateCantInventario(conn,
									obj.getIdArticulo(),
									idBodegaPapa.toString(), EstadoProcesoDevo,
									EstadoDevolucion, objDatos.getIdBodega(),
									objViejo.getTipo_inv(),
									objDatos.getUsuario(), 3,
									objDatos.getIdSolicitud(),
									objViejo.getSerie());

							listaHistorico.add(armaHistorico(obj, objDatos
									.getUsuario(), estadoAlta, objViejo
									.getTipo_inv(), tipoTransaccion, objDatos
									.getIdBodega(), idBodegaPapa.toString(),
									objViejo.getSerie(), objViejo
											.getSerie_asociada(), objViejo
											.getCantidad().toString(), null,idPais));

							if (objViejo.getSeriefinal() == null|| "".equals(objViejo.getSeriefinal())) {
								razonLog = "Se acept\u00F3 en " + razonesLog	+ " la serie: " + objViejo.getSerie();
							} else {
								razonLog = "Se acept\u00F3 en " + razonesLog
										+ " el rango de series: "
										+ objViejo.getSerie() + " - "
										+ objViejo.getSeriefinal();
							}
							listaLog.add(addLog(transaccionLog,
									Conf.LOG_WS_ACEPTA_RECHAZA_DEVOLUCION,
									obj.getIdArticulo(),
									Conf.LOG_TIPO_ARTICULO, razonLog, ""));

						} else if ((encontrado == 0 && objViejo.getTipo_inv().trim().equalsIgnoreCase(InvSidra.trim()))) {

							log.trace("Devolucion hecha a dts");
							OperacionDevolucion.updateCantInventario(conn,
									obj.getIdArticulo(),
									idBodegaPapa.toString(), EstadoProcesoDevo,
									EstadoDisponible, objDatos.getIdBodega(),
									objViejo.getTipo_inv(),
									objDatos.getUsuario(), 3,
									objDatos.getIdSolicitud(), null);

							listaHistorico.add(armaHistorico(obj, objDatos
									.getUsuario(), estadoAlta, objViejo
									.getTipo_inv(), tipoTransaccion, objDatos
									.getIdBodega(), idBodegaPapa.toString(),
									objViejo.getSerie(), objViejo
											.getSerie_asociada(), objViejo
											.getCantidad().toString(), null,idPais));

							listaLog.add(addLog(
									transaccionLog,
									Conf.LOG_WS_ACEPTA_RECHAZA_DEVOLUCION,
									obj.getIdArticulo(),
									Conf.LOG_TIPO_ARTICULO,
									"Se acept\u00F3 en devoluci\u00F3n "
											+ objViejo.getCantidad()
											+ " art\u00EDculo(s) promocional(es).",
									""));
						}

						else if (encontrado == 1 && "0".equals(objViejo.getSeriado())
								&& objViejo.getTipo_inv().trim().equalsIgnoreCase(InvSidra.trim())) {

				
							if("Siniestrado".equalsIgnoreCase(EstadoDevolucion)){
								String bodVirtual="";
								for (Inventario objTemp : lstArticulosBodVirtual) {
									if (obj.getIdArticulo().equals(	objTemp.getArticulo().toString())) {
										bodVirtual=objTemp.getTcscbodegavirtualid()+"";
										break;
									}
								}
								log.trace("bodVirtual:"+bodVirtual);
								if(bodVirtual==null || "".equals(bodVirtual)){
									bodVirtual=objDatos.getIdBodega();
								}
								log.trace("bodVirtual:"+bodVirtual);
								log.trace("Siniestro realizado a inventario SIDRA PROMOCIONAL");
								OperacionDevolucion.updateCantInventario(conn, obj
										.getIdArticulo(),idBodegaPapa.toString()
										.toString(), EstadoProcesoDevo,
										EstadoDevolucion, bodVirtual+"",
										objViejo.getTipo_inv(), objDatos
												.getUsuario(), 3, null, null);
							}else{
								log.trace("Devolucion hecha a inventario SIDRA PROMOCIONAL");
								OperacionDevolucion.updateCantInventario(conn, obj
										.getIdArticulo(), objViejo.getCantidad()
										.toString(), EstadoDisponible,
										EstadoDisponible, idBodegaPapa.toString(),
										objViejo.getTipo_inv(), objDatos
												.getUsuario(), 1, null, null);
								OperacionIngresoSalida.deleteInvDev(conn, objViejo
										.getArticulo().toString(),
										EstadoProcesoDevo, objDatos.getIdBodega(),
										objViejo.getTipo_inv(), objDatos
												.getIdSolicitud(), objDatos.getCodArea(), idPais);
								
								String vendedor="";
								for (Inventario objTemp : lstArticulosBodVirtual) {
									if (obj.getIdArticulo().equals(	objTemp.getArticulo().toString())) {
										vendedor = objTemp.getIdVendedor() != null?objTemp.getIdVendedor().toString():"";
										break;
									}
								}
								if(!"".equals(vendedor)){
									//actualiza cantInv
									OperacionDevolucion.updateInvCantJornada(conn, vendedor, obj
										.getIdArticulo(), objViejo.getCantidad().intValue(), objDatos.getUsuario(), objViejo.getTipo_inv(), idPais);
								}
							}
							listaHistorico.add(armaHistorico(obj, objDatos
									.getUsuario(), estadoAlta, objViejo
									.getTipo_inv(), tipoTransaccion, objDatos
									.getIdBodega(), idBodegaPapa.toString(),
									objViejo.getSerie(), objViejo
											.getSerie_asociada(), objViejo
											.getCantidad().toString(), null,idPais));

							listaLog.add(addLog(
									transaccionLog,
									Conf.LOG_WS_ACEPTA_RECHAZA_DEVOLUCION,
									obj.getIdArticulo(),
									Conf.LOG_TIPO_ARTICULO,
									"Se acept\u00F3 en devoluci\u00F3n "
											+ objViejo.getCantidad()
											+ " art\u00EDculo(s) promocional(es).",
									""));
						}
						// para devolver rangos de series
						else if (((encontrado == 0 && objViejo.getTipo_inv().trim().equalsIgnoreCase(InvTelca.trim())) || 
								"1".equals(objViejo.getSeriado())) && objViejo.getSeriefinal().trim().length() > 0) {
							int diferenciaCeros = OperacionMovimientosInventario.getDiferenciaCeros(objViejo.getSerie());
							 String cerosSerie = "";
			                    for (int i = 0; i < diferenciaCeros; i++) {
			                        cerosSerie += "0";
			                    }
							log.trace("Devolucion hecha a dts Es un rango de series");
							BigDecimal rangoIni ;
							BigDecimal rangoFin ;
							String razonLog = "";
							rangoIni = new BigDecimal(objViejo.getSerie());
							rangoFin = new BigDecimal(objViejo.getSeriefinal());

							while (rangoIni.doubleValue() <= rangoFin
									.doubleValue()) {

								OperacionDevolucion.updateRango(conn,
										obj.getIdArticulo(),
										idBodegaPapa.toString(),
										EstadoProcesoDevo, EstadoDevolucion,
										objDatos.getIdBodega(),
										objViejo.getTipo_inv(),
										objDatos.getUsuario(), 
										objDatos.getIdSolicitud(),
										(cerosSerie + rangoIni.toString()),true);

								rangoIni = rangoIni.add(new BigDecimal(1));
							}

							listaHistorico.add(armaHistorico(obj, objDatos
									.getUsuario(), estadoAlta, objViejo
									.getTipo_inv(), tipoTransaccion, objDatos
									.getIdBodega(), idBodegaPapa.toString(),
									objViejo.getSerie(), objViejo
											.getSerie_asociada(), objViejo
											.getCantidad().toString(), objViejo
											.getSeriefinal(),idPais));

							if (objViejo.getSeriefinal() == null || "".equals(objViejo.getSeriefinal())) {
								razonLog = "Se acept\u00F3 en " + razonesLog	+ " la serie: " + objViejo.getSerie();
							} else {
								razonLog = "Se acept\u00F3 en " + razonesLog
										+ " el rango de series: "
										+ objViejo.getSerie() + " - "
										+ objViejo.getSeriefinal();
							}
							listaLog.add(addLog(transaccionLog,
									Conf.LOG_WS_ACEPTA_RECHAZA_DEVOLUCION,
									obj.getIdArticulo(),
									Conf.LOG_TIPO_ARTICULO, razonLog, ""));
						}

						break;

					}

				} else if ("0".equals(obj.getAceptado()) || rechazado==1) { // se realiza
															// proceso para
															// rechazados
					objSol.setEstado(EstadoRechazada);
					int encontrado = 0;
					
					// verifica si existe en la bodega papa el art\u00EDculo
					for (Inventario objTemp : lstArticulosBodVirtual) {
						if (obj.getIdArticulo().equals(	objTemp.getArticulo().toString())) {
							encontrado = 1;
							break;
						}
					}

					if (obj.getIdArticulo().equals(	objViejo.getArticulo().toString())) {
						if (encontrado == 1	&& "0".equals(objViejo.getSeriado())) {
							log.trace("realiza update en bodega y elimina registro de solicitud en inventario");
							OperacionDevolucion.updateCantInventario(conn, obj
									.getIdArticulo(), objViejo.getCantidad()
									.toString(), EstadoDisponible,
									EstadoDisponible, objDatos.getIdBodega(),
									objViejo.getTipo_inv(), objDatos
											.getUsuario(), 1, null, null);
							OperacionIngresoSalida.deleteInvDev(conn, objViejo
									.getArticulo().toString(),
									EstadoProcesoDevo, objDatos.getIdBodega(),
									objViejo.getTipo_inv(), objDatos
											.getIdSolicitud(), objDatos.getCodArea(), idPais);

							listaLog.add(addLog(transaccionLog,
									Conf.LOG_WS_ACEPTA_RECHAZA_DEVOLUCION,
									obj.getIdArticulo(),
									Conf.LOG_TIPO_ARTICULO,
									"Se rechaz\u00F3 en  " + razonesLog + " "
											+ objViejo.getCantidad()
											+ " art\u00EDculo(s).", ""));

						} else if (encontrado == 0	|| "1".equals(objViejo.getSeriado())
								&& objViejo.getSeriefinal() == null) {
							String razonLog = "";
							log.trace("realiza update en bodega  o en registro actual");
							OperacionDevolucion.updateCantInventario(conn,
									obj.getIdArticulo(),
									objDatos.getIdBodega(), EstadoProcesoDevo,
									EstadoDisponible, objDatos.getIdBodega(),
									objViejo.getTipo_inv(),
									objDatos.getUsuario(), 2,
									objDatos.getIdSolicitud(),
									objViejo.getSerie());
							
							if (objViejo.getSeriefinal() == null || "".equals(objViejo.getSeriefinal())) {
								razonLog = "Se rechaz\u00F3 en " + razonesLog + " la serie: " + objViejo.getSerie();
							} else {
								razonLog = "Se rechaz\u00F3 en " + razonesLog
										+ " el rango de series: "
										+ objViejo.getSerie() + " - "
										+ objViejo.getSeriefinal();
							}

							listaLog.add(addLog(transaccionLog,
									Conf.LOG_WS_ACEPTA_RECHAZA_DEVOLUCION,
									obj.getIdArticulo(),
									Conf.LOG_TIPO_ARTICULO, razonLog, ""));

						}

						else if (encontrado == 0  || "1".equals(objViejo.getSeriado())
								&& objViejo.getSeriefinal().length() > 0) {
							log.trace("realiza update en bodega  o en registro actual");

							int diferenciaCeros = OperacionMovimientosInventario.getDiferenciaCeros(objViejo.getSerie());
							 String cerosSerie = "";
			                    for (int i = 0; i < diferenciaCeros; i++) {
			                        cerosSerie += "0";
			                    }
							log.trace("Devolucion hecha a dts Es un rango de series");
							BigDecimal rangoIni ;
							BigDecimal rangoFin ;

							rangoIni = new BigDecimal(objViejo.getSerie());
							rangoFin = new BigDecimal(objViejo.getSeriefinal());

							while (rangoIni.doubleValue() <= rangoFin.doubleValue()) {

								OperacionDevolucion.updateRango(conn,
										obj.getIdArticulo(),
										objDatos.getIdBodega(),
										EstadoProcesoDevo, EstadoDisponible,
										objDatos.getIdBodega(),
										objViejo.getTipo_inv(),
										objDatos.getUsuario(), 
										objDatos.getIdSolicitud(),
										(cerosSerie+rangoIni.toString()), false);

								rangoIni = rangoIni.add(new BigDecimal(1));
							}

							listaLog.add(addLog(transaccionLog,
									Conf.LOG_WS_ACEPTA_RECHAZA_DEVOLUCION,
									obj.getIdArticulo(),
									Conf.LOG_TIPO_ARTICULO, "Se rechaz\u00F3 en "
											+ razonesLog + " la serie: "
											+ objViejo.getSerie(), ""));

						}

						break;
					}

				}
			}
		}
            return objSol;
	}
}

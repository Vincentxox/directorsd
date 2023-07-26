package com.consystec.sc.ca.ws.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.log4j.xml.DOMConfigurator;

import com.consystec.db.StatementException;
import com.consystec.ms.seguridad.excepciones.ExcepcionClaveExpirada;
import com.consystec.ms.seguridad.excepciones.ExcepcionClaveNoCumplePoliticas;
import com.consystec.ms.seguridad.excepciones.ExcepcionClaveNoValidaLDAP;
import com.consystec.ms.seguridad.excepciones.ExcepcionClaveNoValidaModsec;
import com.consystec.ms.seguridad.excepciones.ExcepcionConfiguracionLdap;
import com.consystec.ms.seguridad.excepciones.ExcepcionUsuarioBloqueado;
import com.consystec.sc.ca.ws.folio.InputFolioVirtual;
import com.consystec.sc.ca.ws.input.adjuntogestion.InputAdjuntoGestion;
import com.consystec.sc.ca.ws.input.anulacion.InputAnulacion;
import com.consystec.sc.ca.ws.input.articuloprecio.InputArticuloPrecio;
import com.consystec.sc.ca.ws.input.asignacion.InputAsignacion;
import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaDTS;
import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaVirtual;
import com.consystec.sc.ca.ws.input.buzon.InputBuzon;
import com.consystec.sc.ca.ws.input.buzon.InputUsuarioBuzon;
import com.consystec.sc.ca.ws.input.catalogo.InputCatalogo;
import com.consystec.sc.ca.ws.input.condicion.InputCondicionPrincipal;
import com.consystec.sc.ca.ws.input.condicionoferta.InputCondicionPrincipalOferta;
import com.consystec.sc.ca.ws.input.cuenta.InputCuenta;
import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.input.dts.InputDistribuidor;
import com.consystec.sc.ca.ws.input.fichacliente.InputFichaCliente;
import com.consystec.sc.ca.ws.input.file.InputCargaFile;
import com.consystec.sc.ca.ws.input.general.InputSesionParametros;
import com.consystec.sc.ca.ws.input.ingresosalida.InputIngresoSalida;
import com.consystec.sc.ca.ws.input.jornada.InputJornada;
import com.consystec.sc.ca.ws.input.log.InputLogSidra;
import com.consystec.sc.ca.ws.input.ofertacampania.InputOfertaCampania;
import com.consystec.sc.ca.ws.input.ofertacampania.InputPromoOfertaCampania;
import com.consystec.sc.ca.ws.input.panel.InputPanel;
import com.consystec.sc.ca.ws.input.pdv.InputBajaPDVDirec;
import com.consystec.sc.ca.ws.input.pdv.InputPDVDirec;
import com.consystec.sc.ca.ws.input.portabilidad.InputCargaAdjuntoPorta;
import com.consystec.sc.ca.ws.input.portabilidad.InputCreaPortabilidad;
import com.consystec.sc.ca.ws.input.promocionales.InputPromocionales;
import com.consystec.sc.ca.ws.input.remesa.InputRemesa;
import com.consystec.sc.ca.ws.input.ruta.InputRuta;
import com.consystec.sc.ca.ws.input.ruta.InputRutaPdv;
import com.consystec.sc.ca.ws.input.sincronizacion.InputSincronizacion;
import com.consystec.sc.ca.ws.input.solicitud.InputAceptaRechazaDevolucion;
import com.consystec.sc.ca.ws.input.solicitud.InputSolicitud;
import com.consystec.sc.ca.ws.input.ticket.InputTicket;
import com.consystec.sc.ca.ws.input.ticket.OutputTicket;
import com.consystec.sc.ca.ws.input.tipotransaccion.InputTransaccionInv;
import com.consystec.sc.ca.ws.input.traslado.InputTraslado;
import com.consystec.sc.ca.ws.input.vendedordts.InputVendedorDTS;
import com.consystec.sc.ca.ws.input.vendedorxdts.ValidarVendedor;
import com.consystec.sc.ca.ws.input.venta.DetallePago;
import com.consystec.sc.ca.ws.input.venta.InputVenta;
import com.consystec.sc.ca.ws.input.visita.InputVisita;
import com.consystec.sc.ca.ws.metodos.AdjuntoGestion;
import com.consystec.sc.ca.ws.metodos.AdjuntoPortabilidad;
import com.consystec.sc.ca.ws.metodos.Anulacion;
import com.consystec.sc.ca.ws.metodos.ArticuloPrecio;
import com.consystec.sc.ca.ws.metodos.Asignacion;
import com.consystec.sc.ca.ws.metodos.AsociaRutaPdv;
import com.consystec.sc.ca.ws.metodos.BodegaAlmacen;
import com.consystec.sc.ca.ws.metodos.BodegaVirtual;
import com.consystec.sc.ca.ws.metodos.BuzonSidra;
import com.consystec.sc.ca.ws.metodos.CargaFile;
import com.consystec.sc.ca.ws.metodos.Catalogos;
import com.consystec.sc.ca.ws.metodos.Condicion;
import com.consystec.sc.ca.ws.metodos.CondicionOferta;
import com.consystec.sc.ca.ws.metodos.Cuenta;
import com.consystec.sc.ca.ws.metodos.Devolucion;
import com.consystec.sc.ca.ws.metodos.Dispositivo;
import com.consystec.sc.ca.ws.metodos.Distribuidor;
import com.consystec.sc.ca.ws.metodos.FichaCliente;
import com.consystec.sc.ca.ws.metodos.FolioRutaPanel;
import com.consystec.sc.ca.ws.metodos.IngresoSalidaInvPromo;
import com.consystec.sc.ca.ws.metodos.Jornada;
import com.consystec.sc.ca.ws.metodos.JornadaMasiva;
import com.consystec.sc.ca.ws.metodos.LogSidraDirector;
import com.consystec.sc.ca.ws.metodos.Login;
import com.consystec.sc.ca.ws.metodos.OfertaCampania;
import com.consystec.sc.ca.ws.metodos.Panel;
import com.consystec.sc.ca.ws.metodos.Portabilidad;
import com.consystec.sc.ca.ws.metodos.PromoOfertaCampania;
import com.consystec.sc.ca.ws.metodos.Promocionales;
import com.consystec.sc.ca.ws.metodos.PuntoVenta;
import com.consystec.sc.ca.ws.metodos.Remesa;
import com.consystec.sc.ca.ws.metodos.Ruta;
import com.consystec.sc.ca.ws.metodos.Sincronizacion;
import com.consystec.sc.ca.ws.metodos.SolicitudWorkFlow;
import com.consystec.sc.ca.ws.metodos.TipoTransaccion;
import com.consystec.sc.ca.ws.metodos.Traslado;
import com.consystec.sc.ca.ws.metodos.UsuarioBuzon;
import com.consystec.sc.ca.ws.metodos.VendedorDTS;
import com.consystec.sc.ca.ws.metodos.VendedorXDTS;
import com.consystec.sc.ca.ws.metodos.VentaRuta;
import com.consystec.sc.ca.ws.metodos.VisitaGestion;
import com.consystec.sc.ca.ws.metodos.VisorTicket;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.adjuntogestion.OutputAdjuntoGestion;
import com.consystec.sc.ca.ws.output.anulacion.OutputAnulacion;
import com.consystec.sc.ca.ws.output.articuloprecio.OutputArticuloPrecio;
import com.consystec.sc.ca.ws.output.asignacion.OutputAsignacion;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegaDTS;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegaVirtual;
import com.consystec.sc.ca.ws.output.buzon.OutputBuzon;
import com.consystec.sc.ca.ws.output.buzon.OutputUsuarioBuzon;
import com.consystec.sc.ca.ws.output.catalogo.OutputCatalogo;
import com.consystec.sc.ca.ws.output.condicion.OutputCondicion;
import com.consystec.sc.ca.ws.output.condicionoferta.OutputCondicionOferta;
import com.consystec.sc.ca.ws.output.cuenta.OutputCuenta;
import com.consystec.sc.ca.ws.output.dispositivo.OutputDispositivo;
import com.consystec.sc.ca.ws.output.dts.OutputDistribuidor;
import com.consystec.sc.ca.ws.output.fichacliente.OutputFichaCliente;
import com.consystec.sc.ca.ws.output.file.OutputImagen;
import com.consystec.sc.ca.ws.output.folio.OutputConfiguracionFolioVirtual;
import com.consystec.sc.ca.ws.output.ingresosalida.OutputIngresoSalida;
import com.consystec.sc.ca.ws.output.jornada.OutputJornada;
import com.consystec.sc.ca.ws.output.login.OutputLogin;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputOfertaCampania;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputPromoOfertaCampania;
import com.consystec.sc.ca.ws.output.panel.OutputPanel;
import com.consystec.sc.ca.ws.output.pdv.OutputpdvDirec;
import com.consystec.sc.ca.ws.output.portabilidad.OutputAdjuntoPorta;
import com.consystec.sc.ca.ws.output.portabilidad.OutputCreaPortabilidad;
import com.consystec.sc.ca.ws.output.promocionales.OutputPromocionales;
import com.consystec.sc.ca.ws.output.remesa.OutputRemesa;
import com.consystec.sc.ca.ws.output.ruta.OutputRuta;
import com.consystec.sc.ca.ws.output.sincronizacion.OutputSincronizacion;
import com.consystec.sc.ca.ws.output.solicitud.OutputSolicitud;
import com.consystec.sc.ca.ws.output.transaccion.OutputTransaccionInv;
import com.consystec.sc.ca.ws.output.traslado.OutputTraslado;
import com.consystec.sc.ca.ws.output.vendedordts.OutputVendedorDTS;
import com.consystec.sc.ca.ws.output.vendedorxdts.Outputvendedorxdts;
import com.consystec.sc.ca.ws.output.venta.OutputVenta;
import com.consystec.sc.ca.ws.output.visita.OutputVisita;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.MediaType;
import com.consystec.sc.sv.ws.metodos.CtrlReEnvioSMS;
import com.consystec.sc.sv.ws.metodos.CtrlVentaRuta;
import com.ericsson.sc.ca.ws.controller.ControllerAbonoVentaCredito;
import com.ericsson.sc.ca.ws.controller.ControllerVentaCredito;
import com.ericsson.sc.ca.ws.input.reenviosms.movil.InputReenvioSMS;
import com.ericsson.sc.ca.ws.input.ventacredito.AbonoVentaCredito;
import com.ericsson.sc.ca.ws.output.reenviosms.movil.OutputReenvioSMS;
import com.ericsson.sc.ca.ws.output.ventacredito.OutputAbonoVentaCredito;
import com.ericsson.sc.ca.ws.output.ventacredito.OutputVentaCredito;
import com.ericsson.sc.ca.ws.output.ventacredito.VentaCredito;
import com.google.gson.GsonBuilder;
import com.novell.ldap.LDAPException;

/**
 * @author sbarrios Consystec 2015
 */
@Path("/opsidra/")
public class ServicioOperacion  extends ServicioBase{

    public static void main(String[] args) {
        DOMConfigurator.configure(ServicioOperacion.class.getClassLoader().getResource("log4j.xml"));
    }

    /*******************************************************************************************
     * Servicios para realizar configuraci\u00F3n de catalogos /
     ******************************************************************************************/
    @Path("/crearcatalogo/")
     /**
      * @api {POST} /opsidra/crearcatalogo/ [CrearCatalogo]
      * @apiName CrearCatalogo
      * @apiDescription Servicio para crear grupo de catalogos de configuraciones por pais
      * @apiGroup Configuraciones
      * @apiVersion 1.0.0
      * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais del que se desea obtener informaci\u00F3n de bodegas.
      * @apiParam {String} usuario nombre de usuario que solicita la operacion.
      * @apiParam {String} grupoParametro Nombre del grupo de catalago a crear.
      * @apiParam {String} nombre nombre del par\u00E9metro.      
      * @apiParam {String} valor valor que tendr\u00E9  el par\u00E9metro a crear.
      * @apiParam {String} [descripcion] descripci\u00F3n del par\u00E9metro a crear
      * 
      * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
      * {
      *    "codArea": "505",
      *    "usuario": "usuario.pruebas",
      *    "grupoParametro": "Grupo 4",
      *    "par\u00E9metros": [
      *        {
      *            "nombre": "Nombre Nuevo 1",
      *            "valor": "Valor Nuevo 1",
      *            "descripcion": "descripci\u00F3n Nueva 1"
      *        },
      *        {
      *            "nombre": "Nombre Nuevo 2",
      *            "valor": "Valor Nuevo 2",
      *            "descripcion": "descripci\u00F3n Nueva 2"
      *        }
      *    ]
      * }
      * 
      * @apiSuccessExample {json} Respuesta-\u00E9xito:
      * {
      *    "respuesta": {
      *        "codResultado": "200",
      *        "mostrar": "1",
      *        "descripcion": "Campos agregados exitosamente.",
      *        "clase": "OperacionCatalogo",
      *        "metodo": "doPost",
      *        "excepcion": " ",
      *        "tipoExepcion": " "
      *    }
      * }
      * @apiErrorExample {json} Respuesta-Error:      
      * {
      *    "respuesta": {
      *        "codResultado": "-199",
      *        "mostrar": "1",
      *        "descripcion": "Los siguientes datos faltan o son incorrectos:  Datos del par\u00E9metro #1. Datos del par\u00E9metro #2.",
      *        "clase": "CtrlCatalogo",
      *        "metodo": "validarInput",
      *        "excepcion": " ",
      *        "tipoExepcion": " "
      *    }
      * }
      * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputCatalogo crearCatalogo(@Context HttpServletRequest req, InputCatalogo input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputCatalogo response = new Catalogos().crearCatalogo(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modificarcatalogo/")
     /**
      * @api {POST} /opsidra/modificarcatalogo/ [ModificarCatalogo]
      * @apiName ModificarCatalogo
      * @apiDescription Servicio para modificar grupo de catalogos de configuraciones por pais
      * @apiGroup Configuraciones
      * @apiVersion 1.0.0
      * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais del que se desea obtener informaci\u00F3n de bodegas.
      * @apiParam {String} usuario nombre de usuario que solicita la operacion.
      * @apiParam {String} grupoParametro Nombre del grupo de catalago a modificar.
      * @apiParam {String} nombre nombre del par\u00E9metro a modificar.
      * @apiParam {String} valor valor que tendr\u00E9  el par\u00E9metro a modificar.
      * @apiParam {String} [descripcion] descripci\u00F3n del par\u00E9metro a modificar.
      * @apiParam {String} estado valor del estado del par\u00E9metro a modificar. 
      * 
      * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
      * {
      *    "codArea": "505",
      *    "usuario": "usuario.pruebas",
      *    "grupoParametro": "Grupo 4",
      *    "par\u00E9metros": [
      *        {
      *            "nombre": "Nombre par\u00E9metro 1",
      *            "valor": "Valor editado 1",
      *            "descripcion": "descripci\u00F3n Nueva 1",
      *            "estado": "ALTA"
      *        }
      *    ]
      * }
      * @apiSuccessExample {json} Respuesta-\u00E9xito:
      * {
      *   "respuesta": {
      *        "codResultado": "201",
      *        "mostrar": "1",
      *        "descripcion": "Recurso modificado exitosamente.",
      *        "clase": "OperacionCatalogo",
      *        "metodo": "doPutDel",
      *        "excepcion": " ",
      *        "tipoExepcion": " "
      *    }
      * }
      * @apiErrorExample {json} Respuesta-Error:      
      * {
      *    "respuesta": {
      *        "codResultado": "-199",
      *        "mostrar": "1",
      *        "descripcion": "Los siguientes datos faltan o son incorrectos:  Datos del par\u00E9metro #1. Datos del par\u00E9metro #2.",
      *        "clase": "CtrlCatalogo",
      *        "metodo": "validarInput",
      *        "excepcion": " ",
      *        "tipoExepcion": " "
      *    }
      * }
      * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputCatalogo modificarCatalogo(@Context HttpServletRequest req, InputCatalogo input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputCatalogo response = new Catalogos().modificarCatalogo(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/bajaCatalogo")
    /**
     * @api {POST} /opsidra/bajaCatalogo/ [bajaCatalogo]
     *
     * @apiName bajaCatalogo
     * @apiDescription Servicio para dar de baja un grupo de catalogos de configuraciones por pa\u00EDs.
     * @apiGroup Configuraciones
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} grupoParametro Nombre del grupo de catalago a dar de baja.
     * @apiParam {String} nombre Nombre del par\u00E9metro a dar de baja.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *    "codArea": "505",
     *    "usuario": "usuario.pruebas",
     *    "grupoParametro": "Grupo 1",
     *    "par\u00E9metros": [
     *        {
     *            "nombre": "Nombre par\u00E9metro 1"
     *        }
     *    ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente.",
     *        "clase": "OperacionCatalogo",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-201",
     *        "mostrar": "1",
     *        "descripcion": "No existe el recurso deseado.",
     *        "clase": "OperacionCatalogo",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputCatalogo doDelCatalogo(@Context HttpServletRequest req, InputCatalogo input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputCatalogo response = new Catalogos().bajaCatalogo(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    /*******************************************************************************************
     * Servicios para configurar asociaci\u00F3n de bodegas
     ******************************************************************************************/
    @Path("/asociarbodegadts/")
    /**
     * @api {POST} /opsidra/asociarbodegadts/ [AsociarBodegasDTS]
     * @apiName AsociarBodegasDTS
     * @apiDescription Servicio para asociar bodegas del sistema comercial a distribuidores internos o externos de SIDRA.
     * @apiGroup Bodegas_Almacen
     * @apiVersion 1.0.0
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} bodegaSCL Identificador de la bodega del sistema comercial que se asociar\u00E9 al distribuidor.
     * @apiParam {String} nombre Nombre de la bodega DTS que se crear\u00E9. 
     * @apiParam {String} distribuidor Identificador del distribuidor al que ser\u00E9 asociada la bodega del sistema comercial.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "vCifuentes",
     *     "bodegaSCL": "100",
     *     "nombre": "Bodega Prueba",
     *     "distribuidor": "15"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "200",
     *        "mostrar": "1",
     *        "descripcion": "Campos agregados exitosamente.",
     *        "clase": "OperacionBodegaDTS",
     *        "metodo": "doPost",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-205",
     *        "mostrar": "1",
     *        "descripcion": "Los siguientes datos faltan o son incorrectos:  Bodega.",
     *        "clase": "CtrlBodegaDTS",
     *        "metodo": "validarInput",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputBodegaDTS asociarBodegaDTS(@Context HttpServletRequest req, InputBodegaDTS input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputBodegaDTS response = new BodegaAlmacen().asociarBodegas(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modificarbodegadts/")
    /**
     * @api {POST} /opsidra/modificarbodegadts/ [ModificarBodegasDTS]
     * @apiName ModificarBodegasDTS
     * @apiDescription Servicio para modificar la asociaci\u00F3n de bodegas a distribuidores de SIDRA, solo podr\u00E9 modificarse la bodega de SCL por una diferente.
     * @apiGroup Bodegas_Almacen
     * @apiVersion 1.0.0
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idBodega Identificador de la bodega a modificar.
     * @apiParam {String} bodegaSCL Identificador de la bodega del sistema comercial.
     * @apiParam {String} nombre Nombre de la bodega asociada.
     * @apiParam {String} distribuidor Nombre del distribuidor al que esta asociada la bodega del sistema comercial.
     * @apiParam {String} estado Estado de la bodega.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *    "codArea": "505",
     *    "usuario": "usuario.pruebas",
     *    "idBodega": "73",
     *    "bodegaSCL": "101",
     *    "nombre": "Bodega Prueba2",
     *    "distribuidor": "16",
     *    "estado": "ALTA"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *       "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente.",
     *        "clase": "OperacionBodegaDTS",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-205",
     *        "mostrar": "1",
     *        "descripcion": "Los siguientes datos faltan o son incorrectos:  Bodega.",
     *        "clase": "CtrlBodegaDTS",
     *        "metodo": "validarInput",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiPermission admin
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputBodegaDTS modificarBodegaDTS(@Context HttpServletRequest req, InputBodegaDTS input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputBodegaDTS response = new BodegaAlmacen().modificarBodegas(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/bajabodegadts/")
    /**
     * @api {POST} /opsidra/bajabodegadts/ [BajaBodegaDTS]
     * @apiName BajaBodegaDTS
     * @apiDescription Servicio para dar de baja la asociaci\u00F3n de bodegas a distribuidores de SIDRA, de igual forma si contiene folios configurados estos tambi\u00E9n ser\u00E9n dados de baja.
     * @apiGroup Bodegas_Almacen
     * @apiVersion 1.0.0
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idBodega Identificador de la bodega a dar de baja.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "vCifuentes",
     *     "idBodega": "73"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente",
     *        "clase": "OperacionBodegaDTS",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-205",
     *        "mostrar": "1",
     *        "descripcion": "Los siguientes datos faltan o son incorrectos:  Bodega.",
     *        "clase": "CtrlBodegaDTS",
     *        "metodo": "validarInput",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputBodegaDTS bajaBodegaDTS(@Context HttpServletRequest req, InputBodegaDTS input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputBodegaDTS response = new BodegaAlmacen().bajaBodegas(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    /*******************************************************************************************
     * SERVICIOS PARA CONFIGURACION DE PUNTOS DE VENTA
     ******************************************************************************************/
    @Path("/creaPDV")
    /**
     * @api {POST} /opsidra/creaPDV/ [CreaPDV]
     * 
     * @apiName CreaPDV
     * @apiDescription Servicio para crear puntos de venta.
     * @apiGroup Puntos_de_Venta
     * @apiVersion 1.0.0
     * @apiParam {String} token codigo de validaci\u00F3n de sesi\u00F3n para realizar operaciones, en caso de que el servicio sea utilizado desde la app WEB, el valor del token sera:WEB.
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais al que se le creara un punto de venta.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} origen=MOVIL o PC indica desde donde se solicita la creaci\u00F3n del punto de venta.
     * @apiParam {String} tipoProducto indica que tipo de producto podr\u00E9n vender los puntos de venta.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} tipoProducto indica que tipo de producto podr\u00E9n vender los puntos de venta.
     * @apiParam {String} nombrePDV nombre para el punto de venta a crear.
     * @apiParam {String} canal canal de distribucion.
     * @apiParam {String} subcanal subcanal de distribucion.
     * @apiParam {String} categoria categor\u00EDa en la que se encuentra el pdv.
     * @apiParam {String} distribuidorAsociado Id del distribuidor al que pertenecer\u00E9 el punto de venta.
     * @apiParam {String} tipoNegocio indica el tipo del negocio del punto de venta a registrar.
     * @apiParam {String} documento tipo de documento del punto de venta. 
     * @apiParam {String} nit nit del punto de venta o encargado del negocio.
     * @apiParam {String} nombreFiscal Nombre fiscal del PDV.
     * @apiParam {String} registroFiscal Registro fiscal del PDV.
     * @apiParam {String} giroNegocio es el tipo de servicio que ofrezca el negocio.
     * @apiParam {String} tipoContribuyente indica si el pdv es gran o pequenio contribuyente.
     * @apiParam {String} digitoValidador digito ingresado por el usuario, no aplica para todos los paises.
     * @apiParam {String} calle n\u00FAmero de calle donde se encuentra el punto de venta.
     * @apiParam {String} avenida n\u00FAmero de avenida donde es encuentra ubicado el punto de venta 
     * @apiParam {String} pasaje parte de la direcci\u00F3n del punto de venta.
     * @apiParam {String} casa n\u00FAmero de casa donde se encuentra el punto de venta.
     * @apiParam {String} colonia colonia donde se encuentra ubicado el punto de venta.
     * @apiParam {String} barrio barrio donde se encuentra ubicado el punto de venta, no aplica para todos los paises.
     * @apiParam {String} referencia referencia sobre la ubicaci\u00F3n del punto de venta.
     * @apiParam {String} direccion Direcci\u00F3n del punto de venta que est\u00E9 siendo creado, no aplica para todos los paises.
     * @apiParam {String} departamento Nombre del departamento al que pertenece el PDV.
     * @apiParam {String} municipio Nombre del municipio al que pertenece el PDV.
     * @apiParam {String} distrito Nombre del distrito donde se encuentra ubicado el PDV, no aplica para todos los paises.
     * @apiParam {String} observaciones Observaciones q ue el usuario creador deseea agregar respecto a la  ubicaci\u00F3n del PDV.
     * @apiParam {String} dias Arreglo de d\u00EDas en el que el punto de venta puede ser visitado.
     * @apiParam {String} telefonoRecargo N\u00FAmeros de telefono que pueden utilizarse para recargas electronicas .
     * @apiParam {String} latitud ubicaci\u00F3n del punto de venta. 
     * @apiParam {String} longitud datas de ubicaci\u00F3n del punto de venta.
     * @apiParam {String} nombres nombres del encargado de punto de venta.
     * @apiParam {String} apellidos apellidos del encargado del punto de venta.
     * @apiParam {String} telefono n\u00FAmero de telefono del encargado del punto de venta.
     * @apiParam {String} cedula n\u00FAmero de c\u00E9dula del encargado, no aplica para todos los paises.
     * @apiParam {String} [qr] C\u00F3digo QR a recibir, no aplica para todos los paises.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "token": "WEB",
     *     "pdv": {
     *         "codArea": "505",
     *         "usuario": "usuario",
     *         "origen": "PC",
     *    	   "idRuta": "",
     *         "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
     *         "tipoProducto": "FISICO",
     *         "nombrePDV": "Prueba creacion3",
     *         "canal": "SIDRA",
     *         "subcanal": "DTS",
     *         "categoria": "A",
     *         "distribuidorAsociado": "1",
     *         "tipoNegocio": "VENTA",
     *         "documento": "NIT",
     *         "nit": "54545454545",
     *         "nombreFiscal": "EE",
     *         "registroFiscal": "EE",
     *         "giroNegocio": "VENTAS POR MENOR",
     *         "tipoContribuyente": "Gran Contribuyente",
     *         "digitoValidador": "",
     *         "calle": "10",
     *         "avenida": "5ta av.",
     *         "pasaje": "123",
     *         "casa": "2-20",
     *         "colonia": "Colonia Monja Blanca",
     *         "barrio": "",
     *         "referencia": "Frente a Casa 2 niveles port\u00F3n blanco",
     *         "direccion": "ZONA 10, GUATEMALA",
     *         "departamento": "GUATEMALA",
     *         "municipio": "GUATEMALA",
     *         "distrito": "",
     *         "observaciones": "estoy creando un pdv :D",
     *         "dias": ["Lunes", "Martes", "Mi\u00E9rcoles"],
     *         "telefonoRecargo": [{
     *             "numero": "74520000",
     *             "orden": "1"
     *         }, {
     *             "numero": "54120111",
     *             "orden": "2"
     *         }],
     *         "latitud": "14.581026",
     *         "longitud": "90.587321",
     *         "encargado": {
     *             "nombres": "LUISA FERNANDA",
     *             "apellidos": "ROSALES MENDEZ",
     *             "telefono": "45120022",
     *             "cedula": "54454545"
     *         },
     *         "qr": ""
     *     }
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "token": "WEB",
     *    "respuesta": {
     *        "codResultado": "4",
     *        "mostrar": "1",
     *        "descripcion": "Ok. Creaci\u00F3n de Punto de Venta exitosa. ",
     *        "clase": " ",
     *        "metodo": " ",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    },
     *    "idPDV": "9",
     *     "zonaComercial": "Managua",
     *    "idEncargadoPDV": "11"
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * { 
     *    "token":" ",
     *    "respuesta": {
     *        "codResultado": "-32",
     *        "mostrar": "1",
     *        "descripcion": "N\u00FAmeros de Recarga repetidos. El N\u00FAmero:50020111 ya ha sido asignado a otro punto de venta.",
     *        "clase": " ",
     *        "metodo": "insertarPDV",
     *        "excepcion": "class CtrlPuntoVenta",
     *        "tipoExepcion": "Generales"
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputpdvDirec creaPDV(@Context HttpServletRequest req, InputPDVDirec input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputpdvDirec response = new PuntoVenta().crearPDV(input);
        ControladorBase.logResponseTime(input.getPdv().getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getPdv().getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/cambiaestadoPDV")
    /**
     *@api {POST} /opsidra/cambiaestadoPDV/ [cambiaEstadoPDV]
     * 
     * @apiName cambiaEstadoPDV
     * @apiDescription Servicio para dar baja o alta un punto de venta existente.
     * @apiGroup Puntos_de_Venta
     * @apiVersion 1.0.0
     * @apiParam {String} token codigo de validaci\u00F3n de sesi\u00F3n para realizar operaciones, en caso de que el servicio sea utilizado desde la app WEB, el valor del token sera:WEB.
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais al que se le modificara un punto de venta.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} idPDV id del punto de venta a modificar. 
     * @apiParam {String} estado nombre del estado con el que ser\u00E9 modificado el punto de venta.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
    * {
    *  "token":"WEB", 
    *  "datos":{ 
    *     "codArea": "505",
    *     "usuario": "usuario",
    *     "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
    *     "idPDV": "9",
    *     "estado": "BAJA"
    *   }
    * }
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
    * {
    *    "token": "WEB",
    *    "respuesta": {
    *        "codResultado": "3",
    *        "mostrar": "1",
    *        "descripcion": "Ok. Se ha cambiado de estado correctamente al punto de venta",
    *        "clase": " ",
    *        "metodo": " ",
    *        "excepcion": " ",
    *        "tipoExepcion": ""
    *    },
    *    "idPDV": "9"
    * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
    * {
    *   "token": "WEB",
    *    "respuesta": {
    *        "codResultado": "-33",
    *        "mostrar": "1",
    *        "descripcion": "El estado ingresado no es v\u00E9lido o no existe.",
    *        "clase": " ",
    *        "metodo": "cambiarEstadoPDV",
    *        "excepcion": "class CtrlPuntoVenta",
    *        "tipoExepcion": "Generales"
    *    }
    * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputpdvDirec cambiaestadoPDV(@Context HttpServletRequest req, InputBajaPDVDirec input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputpdvDirec response = new PuntoVenta().bajaPDV(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getDatos().getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modificaPDV")
    /**
     *@api {POST} /opsidra/modificaPDV/ [ModificaPDV]
     * 
     * @apiName ModificaPDV
     * @apiDescription Servicio para modificar puntos de venta.
     * @apiGroup Puntos_de_Venta
     * @apiVersion 1.0.0
     * @apiParam {String} token codigo de validaci\u00F3n de sesi\u00F3n para realizar operaciones, en caso de que el servicio sea utilizado desde la app WEB, el valor del token sera:WEB.
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs al que se le modificara un punto de venta.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} idPDV id del punto de venta a modificar. 
     * @apiParam {String} tipoProducto indica que tipo de producto podr\u00E9n vender los puntos de venta.
     * @apiParam {String} nombrePDV nombre para el punto de venta a modificar.
     * @apiParam {String} canal canal de distribucion.
     * @apiParam {String} subcanal subcanal de distribucion.
     * @apiParam {String} categoria categor\u00EDa en la que se encuentra el pdv.
     * @apiParam {String} distribuidorAsociado Id del distribuidor al que pertenecer\u00E9 el punto de venta.
     * @apiParam {String} tipoNegocio indica el tipo del negocio del punto de venta a registrar.
     * @apiParam {String} documento tipo de documento del punto de venta. 
     * @apiParam {String} nit nit del punto de venta o encargado del negocio.
     * @apiParam {String} nombreFiscal Nombre fiscal del PDV.
     * @apiParam {String} registroFiscal Registro fiscal del PDV.
     * @apiParam {String} giroNegocio es el tipo de servicio que ofrezca el negocio.
     * @apiParam {String} tipoContribuyente indica si el pdv es gran o pequenio contribuyente.
     * @apiParam {String} digitoValidador digito ingresado por el usuario, no aplica para todos los paises.
     * @apiParam {String} calle n\u00FAmero de calle donde se encuentra el punto de venta.
     * @apiParam {String} avenida n\u00FAmero de avenida donde es encuentra ubicado el punto de venta 
     * @apiParam {String} pasaje parte de la direcci\u00F3n del punto de venta.
     * @apiParam {String} casa n\u00FAmero de casa donde se encuentra el punto de venta.
     * @apiParam {String} colonia colonia donde se encuentra ubicado el punto de venta.
     * @apiParam {String} barrio barrio donde se encuentra ubicado el punto de venta, no aplica para todos los paises.
     * @apiParam {String} referencia referencia sobre la ubicaci\u00F3n del punto de venta.
     * @apiParam {String} direccion Direcci\u00F3n del punto de venta, no aplica para todos los paises.
     * @apiParam {String} departamento Nombre del departamento al que pertenece el PDV.
     * @apiParam {String} municipio Nombre del municipio al que pertenece el PDV.
     * @apiParam {String} distrito Nombre del distrito donde se encuentra ubicado el PDV.
     * @apiParam {String} observaciones Observaciones que el usuario deseea agregar respecto a la  ubicaci\u00F3n del PDV.
     * @apiParam {String} dias Arreglo de d\u00EDas en el que el punto de venta puede ser visitado.
     * @apiParam {String} telefonoRecargo N\u00FAmeros de telefono que pueden utilizarse para recargas electronicas .
     * @apiParam {String} latitud ubicaci\u00F3n del punto de venta. 
     * @apiParam {String} longitud datas de ubicaci\u00F3n del punto de venta.
     * @apiParam {String} nombres nombres del encargado de punto de venta.
     * @apiParam {String} apellidos apellidos del encargado del punto de venta.
     * @apiParam {String} telefono n\u00FAmero de telefono del encargado del punto de venta.
     * @apiParam {String} cedula n\u00FAmero de c\u00E9dula del encargado, no aplica para todos los paises.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
    * {
     *     "token": "WEB",
     *     "pdv": {
     *         "codArea": "505",
     *         "usuario": "usuario",
     *         "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
     *         "idPDV":"1",
     *         "tipoProducto": "FISICO",
     *         "nombrePDV": "Prueba creacion3",
     *         "canal": "SIDRA",
     *         "subcanal": "DTS",
     *         "categoria": "A",
     *         "distribuidorAsociado": "1",
     *         "tipoNegocio": "VENTA",
     *         "documento": "NIT",
     *         "nit": "54545454545",
     *         "nombreFiscal": "EE",
     *         "registroFiscal": "EE",
     *         "giroNegocio": "VENTAS POR MENOR",
     *         "tipoContribuyente": "Gran Contribuyente",
     *         "digitoValidador": "",
     *         "calle": "10",
     *         "avenida": "5ta av.",
     *         "pasaje": "123",
     *         "casa": "2-20",
     *         "colonia": "Colonia Monja Blanca",
     *         "barrio": "",
     *         "referencia": "Frente a Casa 2 niveles port\u00F3n blanco",
     *         "direccion": "ZONA 10, GUATEMALA",
     *         "departamento": "GUATEMALA",
     *         "municipio": "GUATEMALA",
     *         "distrito": "CENTRAL",
     *         "observaciones": "estoy creando un pdv :D",
     *         "dias": ["Lunes", "Martes", "Mi\u00E9rcoles"],
     *         "telefonoRecargo": [{
     *             "numero": "74520000",
     *             "orden": "1",
     *             "estadoPayment": ""
     *         }, {
     *             "numero": "54120111",
     *             "orden": "2",
     *             "estadoPayment": ""
     *         }],
     *         "latitud": "14.581026",
     *         "longitud": "90.587321",
     *         "encargado": {
     *             "idEncargadoPDV": "1",
     *             "nombres": "LUISA FERNANDA",
     *             "apellidos": "ROSALES MENDEZ",
     *             "telefono": "45120022",
     *             "cedula": "54454545"
     *         }
     *     }
     * }
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
    * {
    *    "token": "WEB",
    *    "respuesta": {
    *        "codResultado": "4",
    *        "mostrar": "1",
    *        "descripcion": "Ok. Punto de venta modificado correctamente. ",
    *        "clase": " ",
    *        "metodo": " ",
    *        "excepcion": " ",
    *        "tipoExepcion": ""
    *    },
    *    "idPDV": "9",
    *    "zonaComercial": "Managua"
    * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
    * { 
    *    "token":"WEB",
    *    "respuesta": {
    *        "codResultado": "-32",
    *        "mostrar": "1",
    *        "descripcion": "N\u00FAmeros de Recarga repetidos. El N\u00FAmero:50020111 ya ha sido asignado a otro punto de venta.",
    *        "clase": " ",
    *        "metodo": "insertarPDV",
    *        "excepcion": "class CtrlPuntoVenta",
    *        "tipoExepcion": "Generales"
    *    }
    * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputpdvDirec modificaPDV(@Context HttpServletRequest req, InputPDVDirec input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputpdvDirec response = new PuntoVenta().modificarPDV(input);
        ControladorBase.logResponseTime(input.getPdv().getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getPdv().getUsuario(), req, input, response, beginTime);

        return response;
    }

    /*******************************************************************************************
     * SERVICIOS PARA ADJUNTAR IMAGENES A PDV
     ******************************************************************************************/
    @Path("/cargafile")
    /**
     * @api {POST} /opsidra/cargafile/ [CargaFile]
     * @apiName CargaFile
     * @apiDescription Servicio para cargar una fotografia de un punto de venta.
     * @apiGroup ImagenPDV
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de usuario.
     * @apiParam {String} idPDV Id del registro al cual se desea agregar la imagen.
     * @apiParam {String} archivo String codificado en base64 de la imagen a adjuntar.
     * @apiParam {String} nombreArchivo Nombre del archivo a adjuntar.
     * @apiParam {String} extension Extensi\u00F3n del archivo a adjuntar.
     * @apiParam {String} [idVisita] Id de la visita a la que se asociar\u00E9 la imagen.
     * @apiParam {String} [observaciones] Observaciones de la imagen de la visita.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * { 
     *     "token":"WEB",
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas" ,
     *     "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
     *     "idPDV": "32",
     *     "archivo": "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkG....",
     *     "nombreArchivo": "fotografia",
     *     "extension": ".jpg",
     *     "idVisita": "",
     *     "observaciones": ""
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "token":"WEB",
     *   "idImgPDV": "5",
     *   "respuesta": {
     *     "codResultado": "5",
     *     "mostrar": "1",
     *     "descripcion": "OK. Carga de imagen correctamente",
     *     "clase": "CtrlCargaFile",
     *     "metodo": "enviarImagen",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "token":"WEB",
     *   "respuesta": {
     *     "codResultado": "-372",
     *     "mostrar": "1",
     *     "descripcion": "El punto de venta ya cuenta con la cantidad m\u00E9xima de fotograf\u00EDas permitida 3.",
     *     "clase": " ",
     *     "metodo": "validarDatos",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputImagen cargafile(@Context HttpServletRequest req, InputCargaFile input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputImagen response = new CargaFile().crearCargaFile(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/delcargafile")
    /**
     * @api {POST} /opsidra/delcargafile/ [delCargafile]
     * @apiName delCargafile
     * @apiDescription Servicio para dar de baja una fotografia de un punto de venta.
     * @apiGroup ImagenPDV
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de usuario.
     * @apiParam {String} idImgPDV Id del registro que se desea dar de baja.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "token": "WEB",
     *     "usuario": "usuario.pruebas",
     *     "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
     *     "idImgPDV": "4"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "201",
     *     "mostrar": "1",
     *     "descripcion": "Recurso modificado exitosamente.",
     *     "clase": "CtrlCargaFile",
     *     "metodo": "enviarImagen",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-207",
     *     "mostrar": "1",
     *     "descripcion": "Ocurri\u00F3 un Problema al modificar los datos.",
     *     "clase": "CtrlCargaFile",
     *     "metodo": "enviarImagen",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputImagen delCargafile(@Context HttpServletRequest req, InputCargaFile input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputImagen response = new CargaFile().bajaCargaFile(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    /*******************************************************************************************
     * Servicios para realizar configuraci\u00F3n de distribuidores
     ******************************************************************************************/
    @Path("/creadts")
    /**
     * @api {POST} /opsidra/creadts/ [creaDistribuidor]
     * 
     * @apiName creaDistribuidor
     * @apiDescription Servicio para crear distribuidores por pais.
     * @apiGroup Distribuidor
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} tipo Nombre del tipo de distribuidor. 
     * @apiParam {String} idfichacliente Identificador de la ficha del cliente del sistema comercial.
     * @apiParam {String} idBodegaVirtual Identificador de la bodega virtual que ser\u00E9 asociada al distribuidor. 
     * @apiParam {String} nombres Nombre o nombres del distribuidor que se desea crear.     
     * @apiParam {String} numero N\u00FAmero del distribuidor que se desea crear. 
     * @apiParam {String} email Email del distribuidor que se desea crear.
     * @apiParam {String} administrador id del usuario del m\u00F3dulo de seguridad que es asociado a un distribuidor.
     * @apiParam {String} [pagoautomatico="1 o 0"] Dato boolean que indica si el distribuidor realiza pago autom\u00E9tico.
     * @apiParam {String} canal Canal del distribuidor a crear.
     * @apiParam {String} [numConvenio] N\u00FAmero de convenio del distribuidor a crear. 
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "tipo": "Externo",
     *     "idfichacliente": "1",
     *     "idBodegaVirtual": "1",     
     *     "nombres": "Victor",
     *     "numero": "40410431",
     *     "email": "correo@proveedor.com",
     *     "administrador":"41545",
     *     "pagoautomatico": "1",
     *     "canal": "CANAL",
     *     "numConvenio": "N1"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "idDTS": "8",
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionDistribuidor",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-200",
     *     "mostrar": "1",
     *     "descripcion": "Ya existe el recurso enviado.",
     *     "clase": "OperacionDistribuidor",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputDistribuidor crearDistribuidor(@Context HttpServletRequest req, InputDistribuidor input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputDistribuidor response = new Distribuidor().crearDTS(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modificadts")
    /**
     * @api {POST} /opsidra/modificadts/ [modDistribuidor]
     *
     * @apiName modDistribuidor
     * @apiDescription Servicio para modificar grupo de Distribuidors de configuraciones por pais.
     * @apiGroup Distribuidor
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idDTS Identificador del distribuidor que se desea modificar.
     * @apiParam {String} tipo Nombre del tipo de distribuidor.
     * @apiParam {String} idfichacliente Identificador de la ficha del cliente del sistema comercial.
     * @apiParam {String} nombres Nombre o nombres a modificar del distribuidor indicado.
     * @apiParam {String} apellidos Apellido o apellidos a modificar del distribuidor indicado.
     * @apiParam {String} numero N\u00FAmero nuevo para modificar del distribuidor indicado.
     * @apiParam {String} email Email a modificar del distribuidor indicado.
     * @apiParam {String} administrador id del usuario del m\u00F3dulo de seguridad que es asociado a un distribuidor.
     * @apiParam {String} [pagoautomatico="1 o 0"] Dato boolean que indica si el distribuidor realiza pago autom\u00E9tico.
     * @apiParam {String} canal Canal del distribuidor a crear.
     * @apiParam {String} [numConvenio] N\u00FAmero de convenio del distribuidor a crear.
     * @apiParam {String} estado="ALTA o BAJA" Estado nuevo del distribuidor.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idDTS": "1",
     *     "tipo": "Externo",
     *     "idfichacliente": "1",
     *     "nombres": "Victor",
     *     "apellidos": "M\u00E9ndez",
     *     "numero": "40410431",
     *     "email": "correo@proveedor.com",
     *     "administrador":"41545",
     *     "pagoautomatico": "1",
     *     "canal": "CANAL",
     *     "numConvenio": "N1",
     *     "estado": "ALTA"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente.",
     *        "clase": "OperacionDistribuidor",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-199",
     *        "mostrar": "1",
     *        "descripcion": "Los siguientes datos faltan o son incorrectos:  Datos no num\u00E9ricos en Ficha Cliente. Longitud del N\u00FAmero (8).",
     *        "clase": "CtrlDistribuidor",
     *        "metodo": "validarInput",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputDistribuidor modDistribuidor(@Context HttpServletRequest req, InputDistribuidor input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputDistribuidor response = new Distribuidor().modDTS(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/bajadistribuidor/")
    /**
     * @api {POST} /opsidra/bajadistribuidor/ [bajaDistribuidor]
     *
     * @apiName bajaDistribuidor
     * @apiDescription Servicio para dar de baja un distribuidor por pa\u00EDs.
     * @apiGroup Distribuidor
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9re del pa\u00EDs en el cual se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idDTS Identificador del distribuidor que se desea dar de baja.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idDTS": "6"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente.",
     *        "clase": "OperacionDistribuidor",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-201",
     *        "mostrar": "1",
     *        "descripcion": "No existe el recurso deseado.",
     *        "clase": "OperacionDistribuidor",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputDistribuidor bajaDistribuidor(@Context HttpServletRequest req, InputDistribuidor input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputDistribuidor response = new Distribuidor().bajaDTS(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    /*******************************************************************************************
     * SERVICIOS PARA BODEGAS VIRTUALES
     ******************************************************************************************/
    @Path("/creabodegavirtual")
    /**
     * @api {POST} /opsidra/creabodegavirtual/ [creaBodegaVirtual]
     * 
     * @apiName creaBodegaVirtual
     * @apiDescription Servicio para asociar bodegas virtuales a los distribuidores internos o externos de SIDRA.
     * @apiGroup Bodegas_Virtuales
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n de bodegas.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} nombre Nombre de la bodega virtual que ser\u00E9 asociada la bodega DTS.
     * @apiParam {String} nivel Indicar\u00E9 si la bodega a crear es bodega principal de DTS o es bodega virtual de DTS. Los niveles ser\u00E9n: Bodega Para Inventario de Sidra: 0, Bodega Principal: 1, Bodega Virtual: >= 2.
     * @apiParam {String} tipo: Indica el tipo de bodega de nivel 0, es obligatorio al crear bodegas de nivel 0.
     * @apiParam {String} idBodegaPadre ID de la bodega principal del dts a la que se esta creando bodega virtual, ser\u00E9 obligatoria para niveles mayores a 1.
     * @apiParam {String} idBodegaOrigen ID de la bodega de distribuidor de origen. Este campo es opcional.
     * @apiParam {String} latitud Latitud geogr\u00E9fica de la bodega, ser\u00E9 obligatorio para niveles mayores a 1.
     * @apiParam {String} longitud Longitud geogr\u00E9fica de la bodega, ser\u00E9 obligatorio para niveles mayores a 1.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *    "codArea": "505",
     *    "usuario": "usuario.pruebas",
     *    "nombre": "BODEGA VIRTUAL",
     *    "nivel":"1",
     *    "tipo": "PRINCIPAL",
     *    "idBodegaPadre":"",
     *    "idBodegaOrigen":"",
     *    "latitud": "",
     *    "longitud": ""
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "idBodegaVirtual":"1", 
     *    "respuesta": {
     *        "codResultado": "200",
     *        "mostrar": "1",
     *        "descripcion": "Campos agregados exitosamente.",
     *        "clase": "OperacionBodegaVirtual",
     *        "metodo": "doPost",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-205",
     *        "mostrar": "1",
     *        "descripcion": "Los siguientes datos faltan o son incorrectos:  Bodega.",
     *        "clase": "CtrlBodegaVirtual",
     *        "metodo": "validarInput",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputBodegaVirtual creaBodegaVirtual(@Context HttpServletRequest req, InputBodegaVirtual input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputBodegaVirtual response = new BodegaVirtual().crearBodegaVirtual(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/bajabodegavirtual")
    /**
     *@api {POST} /opsidra/bajabodegavirtual/ [bajaBodegaVirtual]
     * 
     * @apiName bajaBodegaVirtual
     * @apiDescription Servicio para dar de baja la asociaci\u00F3n de bodegas virtuales a distribuidores de SIDRA.
     * @apiGroup Bodegas_Virtuales
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n de bodegas.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idBodega Identificador de la bodega virtual que ser\u00E9 dada de baja.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idBodega": "63"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente",
     *        "clase": "OperacionBodegaVirtual",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-205",
     *        "mostrar": "1",
     *        "descripcion": "Los siguientes datos faltan o son incorrectos:  Bodega.",
     *        "clase": "CtrlBodegaVirtual",
     *        "metodo": "validarInput",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputBodegaVirtual bajaBodegaVirtual(@Context HttpServletRequest req, InputBodegaVirtual input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputBodegaVirtual response = new BodegaVirtual().bajaBodegaVirtual(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modbodegavirtual")
    /**
     * @api {POST} /opsidra/modbodegavirtual/ [modBodegaVirtual]
     *
     * @apiName modBodegaVirtual
     * @apiDescription Servicio para modificar la asociaci\u00F3n de bodegas virtuales a distribuidores de SIDRA, solo podr\u00E9 modificarse el nombre de la bodega por uno diferente.
     * @apiGroup Bodegas_Virtuales
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n de bodegas.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idBodega Identificador de la bodega a modificar.
     * @apiParam {String} nombre Nombre nuevo de la bodega indicada.
     * @apiParam {String} nivel indicar\u00E9 si la bodega a modificar es bodega principal de DTS o es bodega virtual de dts. Los niveles ser\u00E9n: Bodega Principal: 1, Bodega Virtual: 2.
     * @apiParam {String} tipo: Indica el tipo de bodega de nivel 0, es obligatorio al crear o modificar bodegas de nivel 0.
     * @apiParam {String} latitud Latitud geogr\u00E9fica de la bodega, ser\u00E9 obligatorio solo para nivel 2.
     * @apiParam {String} longitud Longitud geogr\u00E9fica de la bodega, ser\u00E9 obligatorio solo para nivel 2.
     * @apiParam {String} estado Estado de la bodega. 
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idBodega": "73",
     *     "nombre": "BODEGA VIRTUAL",
     *     "nivel": "2",
     *     "tipo": "PRINCIPAL",
     *     "latitud": "42.2",
     *     "longitud": "139.89",
     *     "estado": "ALTA"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *       "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente.",
     *        "clase": "OperacionBodegaVirtual",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-205",
     *        "mostrar": "1",
     *        "descripcion": "Los siguientes datos faltan o son incorrectos:  Bodega.",
     *        "clase": "CtrlBodegaVirtual",
     *        "metodo": "validarInput",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiPermission admin
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputBodegaVirtual modBodegaVirtual(@Context HttpServletRequest req, InputBodegaVirtual input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputBodegaVirtual response = new BodegaVirtual().modBodegaVirtual(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    /*******************************************************************************************
     * SERVICIOS PARA CONFIGURAR FOLIOS DE BODEGAS VIRTUALES
     ******************************************************************************************/
    @Path("/creafoliobodvirtual")
    /**
     * @api {POST} /opsidra/creafoliobodvirtual/ [creaFolioBodVirtual]
     * 
     * @apiName creaFolioBodVirtual
     * @apiDescription Servicio para asociar configuracion de folios a bodegas virtuales.
     * @apiGroup Folios
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idTipo Identificador del tipo al que se asociar\u00E9 el folio, actualmente la MAC ADRRESS.
     * @apiParam {String} tipo Nombre del tipo que se asociar\u00E9 el folio (DISPOSITIVOS).
     * @apiParam {String} tipoDocumento Tipo de documento por el que se manejan folios de la bodega asociada, en caso que los folios en el pa\u00EDs al que pertenece el distribuidor sea por reserva de folios.
     * @apiParam {String} serie Serie del folio a configurar en la bodega.
     * @apiParam {String} noInicialFolio Rango inicial del folio a configurar.
     * @apiParam {String} noFinalFolio N\u00FAmero final del rango a configurar para el folio.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idTipo": "11:22:33:44:55",
     *     "tipo": "DISPOSITIVOS",
     *     "folios": [
     *         {
     *             "tipoDocumento": "FACTURA",
     *             "serie": "A",
     *             "noInicialFolio": "1",
     *             "noFinalFolio": "50"
     *         },
     *         {
     *             "tipoDocumento": "FACTURA",
     *             "serie": "A",
     *             "noInicialFolio": "51",
     *             "noFinalFolio": "100"
     *         }
     *     ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "200",
     *        "mostrar": "1",
     *        "descripcion": "Campos agregados exitosamente.",
     *        "clase": "OperacionConfiguracionFolioVirtual",
     *        "metodo": "doPost",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-199",
     *        "mostrar": "1",
     *        "descripcion": "Los siguientes datos faltan o son incorrectos:  Datos no num\u00E9ricos en el rango.",
     *        "clase": "CtrlConfiguracionFolioVirtual",
     *        "metodo": "validarInput",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputConfiguracionFolioVirtual creaFolioBodVirtual(@Context HttpServletRequest req, InputFolioVirtual input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputConfiguracionFolioVirtual response = new FolioRutaPanel().crearFolioBodVirtual(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/bajafoliobodvirtual")
    /**
     *@api {POST} /opsidra/bajafoliobodvirtual/ [bajaFolioBodVirtual]
     * 
     * @apiName bajaFolioBodVirtual
     * @apiDescription Servicio para dar de baja folios de la bodega virtual asociada.
     * @apiGroup Folios
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idTipo Identificador del tipo al que se dar\u00E9 de baja, actualmente la MAC ADRRESS.
     * @apiParam {String} tipo Nombre del tipo que se dar\u00E9 de baja.
     * @apiParam {String} idFolio Id del rango del folio a dar de baja.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idTipo": "11:22:33:44:55",
     *     "tipo": "DISPOSITIVOS",
     *     "folios": [
     *         {
     *             "idFolio": "1"
     *         }
     *     ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente",
     *        "clase": "OperacionConfiguracionFolioVirtual",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-199",
     *        "mostrar": "1",
     *        "descripcion": "Los siguientes datos faltan o son incorrectos:  Datos no num\u00E9ricos en el rango.",
     *        "clase": "CtrlConfiguracionFolioVirtual",
     *        "metodo": "validarInput",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputConfiguracionFolioVirtual bajaFolioBodVirtual(@Context HttpServletRequest req, InputFolioVirtual input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputConfiguracionFolioVirtual response = new FolioRutaPanel().bajaFolioBodVirtual(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

  

    /*******************************************************************************************
     * SERVICIOS PARA CONFIGURACION DE PANELES
     ******************************************************************************************/
    @Path("/creapanel")
    /**
     *@api {POST} /opsidra/creapanel/ [creaPanel]
     * 
     * @apiName creaPanel
     * @apiDescription Servicio para crear paneles en Sidra.
     * @apiGroup Panel
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idDistribuidor Id del distribuidor al que pertenecer\u00E9 la panel.
     * @apiParam {String} idBodegaVirtual Id de la bodega que es asignada a la panel.
     * @apiParam {String} nombre Nombre para la panel.
     * @apiParam {String} datosVendedor Listado de vendedores con los datos implicados.
     * @apiParam {String} datosVendedor.vendedor Id del vendedor a asociar a la panel.
     * @apiParam {String} datosVendedor.responsable="1 o 0" Indica si el vendedor es el responsable de la panel o no. 1=Si o 0=No.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *    "usuario": "usuario.pruebas",
     *    "idDistribuidor": "1",
     *    "idBodegaVirtual": "1",
     *    "nombre": "Panel Prueba",
     *    "datosVendedor":[
     *        {
     *            "vendedor": "2322",
     *            "responsable": "1"
     *        }
     *    ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "idPanel": "1",
     *    "respuesta": {
     *        "codResultado": "6",
     *        "mostrar": "1",
     *        "descripcion": "OK. Creaci\u00F3n de panel exitosa",
     *        "clase": " ",
     *        "metodo": " ",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-43",
     *        "mostrar": "1",
     *        "descripcion": "Vendedores para panel no son admitidos. El vendedor supervisor3 ya ha sido asignado a una panel.",
     *        "clase": " ",
     *        "metodo": "insertarPanel",
     *        "excepcion": "CtrlPanel",
     *        "tipoExepcion": "Generales"
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputPanel creaPanel(@Context HttpServletRequest req, InputPanel input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputPanel response = new Panel().crearPanel(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modificapanel")
    /**
     *@api {POST} /opsidra/modificapanel/ [modificaPanel]
     * 
     * @apiName modificaPanel
     * @apiDescription Servicio para modificar datos de paneles.
     * @apiGroup Panel
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idPanel Id de la panel a modificar.
     * @apiParam {String} idDistribuidor Id del distribuidor al que pertenecer\u00E9 la panel.
     * @apiParam {String} idBodegaVirtual Id de la bodega que es asignada a la panel.
     * @apiParam {String} nombre Nombre para la panel.
     * @apiParam {String} datosVendedor Listado de vendedores con los datos implicados.
     * @apiParam {String} datosVendedor.vendedor Id del vendedor a asociar a la panel.
     * @apiParam {String} datosVendedor.responsable="1 o 0" Indica si el vendedor es el responsable de la panel o no. 1=Si o 0=No.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *    "codArea": "505",
     *    "usuario": "usuario.pruebas",
     *    "idPanel": "1",
     *    "idDistribuidor": "1",
     *    "idBodegaVirtual": "1",
     *    "nombre": "Panel Prueba",
     *    "datosVendedor":[
     *        {
     *            "vendedor": "2322",
     *            "responsable": "1"
     *        },{
     *            "vendedor": "2242",
     *            "responsable": "0"
     *        }
     *    ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "idPanel": "1",
     *    "respuesta": {
     *        "codResultado": "7",
     *        "mostrar": "1",
     *        "descripcion": "OK. Panel modificado correctamente.",
     *        "clase": " ",
     *        "metodo": " ",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-43",
     *        "mostrar": "1",
     *        "descripcion": "Vendedores para panel no son admitidos. El vendedor supervisor3 ya ha sido asignado a una panel.",
     *        "clase": " ",
     *        "metodo": "modificarPanel",
     *        "excepcion": "CtrlPanel",
     *        "tipoExepcion": "Generales"
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputPanel modificaPanel(@Context HttpServletRequest req, InputPanel input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputPanel response = new Panel().modificarPanel(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/cambiaestadopanel")
    /**
     *@api {POST} /opsidra/cambiaestadopanel/ [cambiaEstadoPanel]
     * 
     * @apiName cambiaEstadoPanel
     * @apiDescription Servicio para modificar puntos de venta.
     * @apiGroup Panel
     * @apiVersion 1.0.0
     * @apiParam (cambiaEstadoPanel) {String}   codArea C\u00F3digo de \u00E9rea del pa\u00EDs al que se le modificar\u00E9 una panel.
     * @apiParam (cambiaEstadoPanel) {String}   usuario Nombre de usuario que solicita la operacion.
     * @apiParam (cambiaEstadoPanel) {String}   idPanel id de la panel a modificar. 
     * @apiParam (cambiaEstadoPanel) {String}   estado nombre del estado que se desea modificar en panel.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
    * {
    *    "codArea": "505",
    *    "usuario": "usuario",
    *    "idPanel": "1",
    *    "estado": "baja"
    * }
    * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
    * {
    *    "idPanel": "1",
    *    "respuesta": {
    *        "codResultado": "8",
    *        "mostrar": "1",
    *        "descripcion": "OK.  Se cambio de estado correctamente.",
    *        "clase": " ",
    *        "metodo": " ",
    *        "excepcion": " ",
    *        "tipoExepcion": ""
    *    }
    * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
    * {
    *    "respuesta": {
    *        "codResultado": "-33",
    *        "mostrar": "1",
    *        "descripcion": "El estado ingresado no es v\u00E9lido o no existe.",
    *        "clase": " ",
    *        "metodo": "cambiarEstadoPanel",
    *        "excepcion": "class CtrlPanel",
    *        "tipoExepcion": "Generales"
    *    }
    * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputPanel cambiaEstadoPanel(@Context HttpServletRequest req, InputPanel input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputPanel response = new Panel().cambiaestadoPanel(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    /*******************************************************************************************
     * SERVICIOS PARA CONFIGURACI\u00F3N DE RUTAS
     ******************************************************************************************/
    @Path("/crearuta")
    /**
     * @api {POST} /opsidra/crearuta/ [creaRuta]
     * 
     * @apiName creaRuta
     * @apiDescription Servicio para crear Rutaes por pa\u00EDs.
     * @apiGroup Rutas
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idDTS Identificador del distribuidor asociado a la ruta que se desea crear.
     * @apiParam {String} nombreRuta Nombre de la ruta que se desea crear.
     * @apiParam {String} secUsuarioId Identificador del vendedor o usuario del modulo de seguridad asociado.
     * @apiParam {String} idBodegaVirtual Identificador de la bodega virtual.
     * @apiParam {String} estado Estado de la ruta, por defecto ser\u00E9 ALTA.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idDTS": "1",
     *     "nombreRuta": "RUTA 1",
     *     "secUsuarioId": "1",
     *     "idBodegaVirtual": "1",
     *     "estado": ""
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionRuta",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     * 	 "idRuta":"10",
     *   "respuesta": {
     *     "codResultado": "-200",
     *     "mostrar": "1",
     *     "descripcion": "Ya existe el recurso enviado. El ID de Usuario ya ha sido registrado.",
     *     "clase": "OperacionRuta",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputRuta creaRuta(@Context HttpServletRequest req, InputRuta input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputRuta response = new Ruta().crearRuta(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/bajaruta")
    /**
     * @api {POST} /opsidra/bajaruta/ [delRuta]
     *
     * @apiName delRuta
     * @apiDescription Servicio para dar de baja una rutas de configuraciones por pa\u00EDs.
     * @apiGroup Rutas
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idDTS Identificador de la ruta que se desea dar de baja.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idRuta": "1"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente.",
     *        "clase": "OperacionRuta",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-201",
     *        "mostrar": "1",
     *        "descripcion": "No existe el recurso deseado.",
     *        "clase": "OperacionRuta",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputRuta bajaRuta(@Context HttpServletRequest req, InputRuta input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputRuta response = new Ruta().bajaRuta(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modificaruta")
    /**
     * @api {POST} /opsidra/modificaruta/ [modificaRuta]
     *
     * @apiName modificaRuta
     * @apiDescription Servicio para modificar rutas creadas.
     * @apiGroup Rutas
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idRuta Identificador de la ruta que se desea modificar.
     * @apiParam {String} idDTS Identificador del distribuidor nuevo a modificar.
     * @apiParam {String} nombreRuta Nombre a modificar de la ruta.
     * @apiParam {String} secUsuarioId Identificador del vendedor o usuario del modulo de seguridad.
     * @apiParam {String} idBodegaVirtual Identificador de la bodega virtual.
     * @apiParam {String} estado Estado nuevo de la ruta (ALTA o BAJA).
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idRuta": "1",
     *     "idDTS": "1",
     *     "nombreRuta": "RUTA 1",
     *     "secUsuarioId": "1",
     *     "idBodegaVirtual": "1",
     *     "estado": "ALTA"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente.",
     *        "clase": "OperacionRuta",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-199",
     *        "mostrar": "1",
     *        "descripcion": "Los siguientes datos faltan o son incorrectos:  Datos no num\u00FAricos en Ficha Cliente. Longitud del N\u00FAmero (8).",
     *        "clase": "CtrlRuta",
     *        "metodo": "validarInput",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputRuta modificaRuta(@Context HttpServletRequest req, InputRuta input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputRuta response = new Ruta().modificaRuta(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

  

    /*******************************************************************************************
     * SERVICIOS PARA CONFIGURACION DE BUZONES
     ******************************************************************************************/
    @Path("/creabuzon")
    /**
     *@api {POST} /opsidra/creabuzon/ [creaBuzon]
     * 
     * @apiName creaBuzon
     * @apiDescription Servicio para crear buzones que ser\u00E9n utiles en la creaci\u00F3n de solitudes para el work flow.
     * @apiGroup Buzones
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo del pa\u00EDs donde se desea guardar la informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} Nombre nombre del buz\u00F3n a crear. 
     * @apiParam {String} idDistribuidor id del distribuidor al que se encontrar\u00E9 asociado el buzon. Este solo se ingresar\u00E9 en caso que el buz\u00F3n a registrar sea de nivel 2-3.
     * @apiParam {String} idBodegavirtual id de la bodega virtual al que se encontrar\u00E9 asociado el buzon. Este solo se ingresar\u00E9 en caso que el buz\u00F3n a registrar sea de nivel 3.
     * @apiParam {String} [nivel="1 o 2"] nivel del distribuidor a crear .
     * @apiParam {String} tipoWF Indica que tipo de solicitud atender\u00E9 el buzon. Ejemplo: DEVOLUCION, SINIESTRO o TODAS. 
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
    * {
    *    "codArea":"505",
    *    "usuario": "usuario",
    *    "nombre": "Log\u00EDstica",
    *    "idDistribuidor": "11",
    *    "idBodegaVirtual": "11",
    *    "nivel": "2",
    *    "tipoWF": "DEVOLUCION"
    * }
    * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
    * {
    *    "idBuzon": "1",
    *    "respuesta": {
    *        "codResultado": "10",
    *        "mostrar": "1",
    *        "descripcion": "OK. Creaci\u00F3n de Buz\u00F3n exitosa.",
    *        "clase": " ",
    *        "metodo": " ",
    *        "excepcion": " ",
    *        "tipoExepcion": ""
    *    }
    * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
    * {
    *    "respuesta": {
    *        "codResultado": "-45",
    *        "mostrar": "1",
    *        "descripcion": "El par\u00E9metro de entrada \"nombre"\ esta vac\u00EDo.",
    *        "clase": " ",
    *        "metodo": "crearBuzon",
    *        "excepcion": "class CtrlBuzonSidra",
    *        "tipoExepcion": "Generales"
    *    }
    * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputBuzon crearBuzon(@Context HttpServletRequest req, InputBuzon input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputBuzon response = new BuzonSidra().crearBuzon(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modbuzon")
    /**
     *@api {POST} /opsidra/modbuzon/ [modBuzon]
     * 
     * @apiName modBuzon
     * @apiDescription Servicio para modificar buzones que ser\u00E9n utiles en la creaci\u00F3n de solitudes para el work flow.
     * @apiGroup Buzones
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo del pa\u00EDs donde se desea modificar la informaci\u00F3n. 
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idBuzon id del buz\u00F3n que se desea modificar. 
     * @apiParam {String} Nombre nombre del buz\u00F3n a modificar. 
     * @apiParam {String} estado estado que desea aplicarse al buzon.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
    * {
    *    "codArea":"505",
    *    "usuario": "usuario",
    *    "idBuzon": "1",
    *    "nombre": "Log\u00EDstica",
    *    "estado": "BAJA"
    * }
    * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
    * {
    *    "idBuzon": "1",
    *    "respuesta": {
    *        "codResultado": "11",
    *        "mostrar": "1",
    *        "descripcion": "Ok. Buz\u00F3n modificado exitosamente.",
    *        "clase": " ",
    *        "metodo": " ",
    *        "excepcion": " ",
    *        "tipoExepcion": ""
    *    }
    * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
    * {
    *    "respuesta": {
    *        "codResultado": "-45",
    *        "mostrar": "1",
    *        "descripcion": "El par\u00E9metro de entrada \"nombre"\ esta vac\u00EDo.",
    *        "clase": " ",
    *        "metodo": "modBuzon",
    *        "excepcion": "class CtrlBuzonSidra",
    *        "tipoExepcion": "Generales"
    *    }
    * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputBuzon modBuzon(@Context HttpServletRequest req, InputBuzon input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputBuzon response = new BuzonSidra().modBuzon(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    //----------------------------------- Servicios Solicitud ----------------------------------//
    @Path("/creasolicitud")
    /**
     * @api {POST} /opsidra/creasolicitud/ [creasolicitud]
     * 
     * @apiName creasolicitud
     * @apiDescription Servicio para crear Solicitudes por pa\u00EDs.
     * @apiGroup Solicitud
     * @apiVersion 1.0.0
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} idBuzon Identificador del buz\u00F3n a asociar.
     * @apiParam {String} tipoSolicitud="DEVOLUCION, RESERVA, PEDIDO, NUMEROS_PAYMENT, SINIESTRO o DEUDA" Tipo de la solicitud.
     * @apiParam {String} idVendedor Identificador del vendedor a asociar. Opcional en solicitudes de origen PC.
     * @apiParam {String} idBodega Identificador de la bodega a asociar.
     * @apiParam {String} idDTS Identificador del distribuidor a asociar.
     * @apiParam {String} fecha Fecha de la solicitud en formato yyyyMMdd.
     * @apiParam {String} causaSolicitud Causa de la solicitud. Necesario \u00FAnicamente en solicitudes tipo DEVOLUCION.
     * @apiParam {String} idPDV Identificador del punto de venta al ser solicitud tipo NUMEROS_PAYMENT.
     * @apiParam {String} observaciones Observaciones de la solicitud.
     * @apiParam {String} origen="MOVIL o PC" Origen de la solicitud.
     * @apiParam {String} seriado Indica si la solicitud es con series o no (1 o 0). Opcional en solicitudes de origen PC.
     * @apiParam {String} tipoSiniestro="TOTAL, PARCIAL o DISPOSITIVO" Tipo de siniestro de la solicitud.
     * @apiParam {String} idTipo Identificador del tipo Ruta o Panel asociado al vendedor.
     * @apiParam {String} tipo="RUTA o PANEL" Nombre del tipo asociado al vendedor.
     * @apiParam {String} totalDeuda Total de la deuda. \u00FAnicamente en caso de ser solicitud de tipo DEUDA.
     * @apiParam {String} idBuzonSiguiente Buz\u00F3n al que se enviar\u00E9 la deuda. \u00FAnicamente en caso de ser solicitud de tipo DEUDA.
     * @apiParam {String} articulos Listado de art\u00EDculos de la solicitud. Innecesario al tratarse de una solicitud de tipo SINIESTRO y TOTAL.
     * @apiParam {String} articulos.idArticulo Identificador del art\u00EDculo a asociar.
     * @apiParam {String} articulos.serie Serie del art\u00EDculo a asociar.
     * @apiParam {String} articulos.serieFinal Serie final del rango del art\u00EDculo a asociar. Unicamente valores num\u00E9ricos y cuando sean devoluciones por rango.
     * @apiParam {String} articulos.cantidad Cantidad de art\u00EDculos a asociar.
     * @apiParam {String} articulos.serieAsociada Serie secundaria asociada al art\u00EDculo.
     * @apiParam {String} articulos.tipoInv Nombre del tipo de inventario del art\u00EDculo.
     * @apiParam {String} dispositivos Listado de dispositivos a reportar en siniestros.
     * @apiParam {String} dispositivos.codigoDispositivo C\u00F3digo del dispositivo a reportar en como siniestrado.
     * @apiParam {String} [detallePagos] Listado de dispositivos a reportar en siniestros.
     * @apiParam {String} detallePagos.formaPago Nombre de la forma de pago.
     * @apiParam {String} detallePagos.monto Monto de la forma de pago.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *   "token": "WEB",
     *   "codArea": "505",
     *   "usuario": "sergio.lujan",
     *   "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
     *   "idDTS": "33",
     *   "idBuzon": "2",
     *   "idBodega": "73",
     *   "fecha": "20160822",
     *   "tipoSolicitud": "Siniestro",
     *   "idVendedor": "",
     *   "causaSolicitud": "",
     *   "idPDV": "",
     *   "observaciones": "",
     *   "origen": "pc",
     *   "seriado": "1",
     *   "tipoSiniestro": "parcial",
     *   "idTipo": "82",
     *   "tipo": "panel",
     *   "totalDeuda": "",
     *   "idBuzonSiguiente": "",
     *   "articulos": [
     *     {
     *       "idArticulo": "8",
     *       "serie": "",
     *       "serieFinal": "",
     *       "cantidad": "10",
     *       "serieAsociada": "",
     *       "tipoInv": "INV_TELCA"
     *     },
     *     {
     *       "idArticulo": "",
     *       "serie": "A3",
     *       "serieFinal": "",
     *       "cantidad": "",
     *       "serieAsociada": "",
     *       "tipoInv": "INV_TELCA"
     *     },
     *     {
     *       "idArticulo": "",
     *       "serie": "130",
     *       "serieFinal": "139",
     *       "cantidad": "",
     *       "serieAsociada": "",
     *       "tipoInv": "INV_TELCA"
     *     }
     *   ],
     *   "dispositivos": [
     *      {
     *         "codigoDispositivo": "32DDA7D9A68CE8663B6315549EAAB637A79445D6"
     *      },{
     *         "codigoDispositivo": "3D3570521B4F675984131DCA362C04FD29A93A5B"
     *      }
     *   ],
     *   "detallePagos": [
     *      {
     *         "formaPago": "EFECTIVO",
     *         "monto": "1194987.5100257151"
     *      },{
     *         "formaPago": "TARTA",
     *         "monto": "0"
     *      }
     *   ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "idSolicitud": "212",
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente. Algunas series no se pudieron ingresar: A3. Algunos art\u00EDculos no se pudieron ingresar por las siguientes razones: Art\u00EDculos no cuentan con existencias suficientes: 8. ",
     *     "clase": "OperacionSolicitud",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   },
     *   "descErrorSeries": "Series no v\u00E9lidas en la bodega.",
     *   "series": {
     *     "serie": "A3"
     *   },
     *   "descErrorExistencias": "Art\u00EDculos no cuentan con existencias suficientes: ",
     *   "existencias": {
     *     "idArticulo": "8"
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos:  Origen debe especificarse como PC o MOVIL.",
     *     "clase": "CtrlSolicitud",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputSolicitud doPostSolicitud(@Context HttpServletRequest req, InputSolicitud input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputSolicitud response = new SolicitudWorkFlow().crearSolicitud(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modsolicitud")
    /**
     * @api {POST} /opsidra/modsolicitud/ [modsolicitud]
     * 
     * @apiName modsolicitud
     * @apiDescription Servicio para modificar el estado de Solicitudes por pa\u00EDs.
     * @apiGroup Solicitud
     * @apiVersion 1.0.0
     * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de usuario.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais donde se realizar\u00E9 la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idSolicitud Identificador de la solicitud a modificar.
     * @apiParam {String} estado="ACEPTADA, RECHAZADA, CANCELADA, FINALIZADA, RECHAZADA_TELCA o ENVIADO" Estado con el que se operar\u00E9 la solicitud.
     * @apiParam {String} [idVendedor] Identificador del vendedor que cancela la solicitud. Obligatorio cuando sea cancelaci\u00F3n de origen m\u00F3vil.
     * @apiParam {String} [observaciones] Observaciones de la cancelaci\u00F3n de la solicitud.
     * @apiParam {String} [idBuzonSiguiente] Identificador del buz\u00F3n al que se enviar\u00E9 la solicitud, necesario en caso de enviar estado ACEPTADA.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *      "token": "WEB",
     *      "codArea": "505",
     *      "usuario":"usuario.pruebas",
     *      "idSolicitud":"1",
     *      "estado": "CANCELADA",
     *      "idVendedor":"961",
     *      "observaciones":"",
     *      "idBuzonSiguiente": ""
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "201",
     *     "mostrar": "1",
     *     "descripcion": "Recurso modificado exitosamente.",
     *     "clase": "OperacionSolicitud",
     *     "metodo": "doPut",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-215",
     *     "mostrar": "1",
     *     "descripcion": "No se pueden cerrar solicitudes tipo DEVOLUCION o SINIESTRO.",
     *     "clase": "OperacionSolicitud",
     *     "metodo": "doPut",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputSolicitud doPutSolicitud(@Context HttpServletRequest req, InputSolicitud input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputSolicitud response = new SolicitudWorkFlow().modSolicitud(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios Solicitud ----------------------------------//

    //----------------------------------- Servicios UsuarioBuzon -------------------------------//
    @Path("/asignausuariobuzon")
    /**
     * @api {POST} /opsidra/asignausuariobuzon/ [asignaUsuarioBuzon]
     * 
     * @apiName asignaUsuarioBuzon
     * @apiDescription Servicio para asignar buzones a vendedores por pa\u00EDs.
     * @apiGroup UsuarioBuzon
     * @apiVersion 1.0.0
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idBuzon Identificador del buzon a asociar.
     * @apiParam {String} idVendedor Identificador del vendedor a asociar.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idBuzon": "1",
     *     "idVendedor": "1"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionUsuarioBuzon",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": " "
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos:  ID Buzon.",
     *     "clase": "CtrlUsuarioBuzon",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": " "
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputUsuarioBuzon doPostUsuarioBuzon(@Context HttpServletRequest req, InputUsuarioBuzon input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputUsuarioBuzon response = new UsuarioBuzon().crearUsuarioBuzon(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modusuariobuzon")
    /**
     * @api {POST} /opsidra/modusuariobuzon/ [modUsuarioBuzon]
     *
     * @apiName modUsuarioBuzon
     * @apiDescription Servicio para modificar UsuarioBuzons creados.
     * @apiGroup UsuarioBuzon
     * @apiVersion 1.0.0
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idUsuarioBuzon Identificador del buzon de usuario a modificar.
     * @apiParam {String} idBuzon Identificador del buzon.
     * @apiParam {String} estado Estado del buzon de usuario.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idUsuarioBuzon": "1",
     *     "idBuzon": "1",
     *     "estado": "ALTA"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionUsuarioBuzon",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": " "
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos:  Tipo.",
     *     "clase": "CtrlUsuarioBuzon",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": " "
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputUsuarioBuzon doPutUsuarioBuzon(@Context HttpServletRequest req, InputUsuarioBuzon input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputUsuarioBuzon response = new UsuarioBuzon().modUsuarioBuzon(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/bajausuariobuzon")
    /**
     * @api {POST} /opsidra/bajausuariobuzon/ [delUsuarioBuzon]
     *
     * @apiName delUsuarioBuzon
     * @apiDescription Servicio para dar de baja UsuarioBuzons por pa\u00EDs.
     * @apiGroup UsuarioBuzon
     * @apiVersion 1.0.0
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idUsuarioBuzon Identificador del buzon de usuario que se desea dar de baja.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idUsuarioBuzon": "1"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente.",
     *        "clase": "OperacionUsuarioBuzon",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-201",
     *        "mostrar": "1",
     *        "descripcion": "No existe el recurso deseado.",
     *        "clase": "OperacionUsuarioBuzon",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputUsuarioBuzon doDelUsuarioBuzon(@Context HttpServletRequest req, InputUsuarioBuzon input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputUsuarioBuzon response = new UsuarioBuzon().bajaUsuarioBuzon(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios UsuarioBuzon -------------------------------//

    //----------------------------------- Servicios Traslado -----------------------------------//
    @Path("/creatraslado")
    /**
     * @api {POST} /opsidra/creatraslado/ [creaTraslado]
     * 
     * @apiName creaTraslado
     * @apiDescription Servicio para crear Traslados por pa\u00EDs.
     * @apiGroup Traslado
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais donde se realizar\u00E9 la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} [numTraspaso] N\u00FAmero de traspaso de SCL.
     * @apiParam {String} bodegaOrigen Identificador de la bodega origen del traslado.
     * @apiParam {String} bodegaDestino Identificador de la bodega destino del traslado.
     * @apiParam {String} fecha Fecha del traslado.
     * @apiParam {String} articulos Listado de art\u00EDculos a trasladar.
     * @apiParam {String} articulos.idArticulo Identificador del art\u00EDculo a trasladar.
     * @apiParam {String} articulos.serie Serie del art\u00EDculo a trasladar.
     * @apiParam {String} articulos.serieFinal Serie final del rango del art\u00EDculo a trasladar.
     * @apiParam {String} articulos.cantidad Cantidad de art\u00EDculos a trasladar.
     * @apiParam {String} articulos.serieAsociada Serie asociada al art\u00EDculo.
     * @apiParam {String} articulos.tipoInv Nombre del tipo de inventario del art\u00EDculo.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea":"505",
     *     "usuario":"usuarioprueba",
     *     "numTraspaso":"",
     *     "bodegaOrigen":"1055",
     *     "bodegaDestino":"10",
     *     "fecha":"20151005",
     *     "articulos": [
     *         {
     *             "idArticulo":"",
     *             "serie":"1001",
     *             "serieFinal":"2000",
     *             "cantidad":"",
     *             "serieAsociada":"",
     *             "tipoInv": "INV_TELCA"
     *         },
     *         {
     *             "idArticulo":"8",
     *             "serie":"",
     *             "serieFinal":"",
     *             "cantidad":"50",
     *             "serieAsociada":"",
     *             "tipoInv": "INV_TELCA"
     *         }
     *     ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.  Algunos art\u00EDculos no se pudieron trasladar por las siguientes razones: Art\u00EDculos no existen en el inventario: 598. ",
     *     "clase": "OperacionTraslado",
     *     "metodo": "doPutDel",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   },
     *   "descErrorArticulos": "Art\u00EDculos no existen en el inventario: ",
     *   "articulos": {
     *     "idArticulo": "598"
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos:  La Bodega Destino enviada no es apta para el traslado.",
     *     "clase": "CtrlTraslado",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputTraslado doCreaTraslado(@Context HttpServletRequest req, InputTraslado input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputTraslado response = new Traslado().creaTraslado(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios Traslado -----------------------------------//

    //----------------------------------- Servicios Promocionales ------------------------------//
    @Path("/creapromocional")
    /**
     * @api {POST} /opsidra/creapromocional/ [creapromocional]
     * 
     * @apiName creapromocional
     * @apiDescription Servicio para crear Promocionaleses por pa\u00EDs.
     * @apiGroup Promocionales
     * @apiVersion 1.0.0
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} descripcion Nombre del art\u00EDculo promocional a crear.
     * @apiParam {String} tipoGrupo Nombre del grupo del art\u00EDculo promocional a crear.
     * @apiParam {String} idOferta Id de la oferta con la cual se venderan promocionales en FS, aplica solo para SV.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "descripcion": "Pachon Blanco",
     *     "tipoGrupo": "GRUPO B",
     *     "idOferta":"9146027023265528900"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "idArtPromocional": "4",
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionPromocionales",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos:  El nombre enviado se encontr\u00F3 con el c\u00F3digo 'C4', en el grupo: GRUPO B y con el estado: ALTA",
     *     "clase": "CtrlPromocionales",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputPromocionales doPostPromocionales(@Context HttpServletRequest req, InputPromocionales input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputPromocionales response = new Promocionales().crearPromocionales(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/bajapromocional")
    /**
     * @api {POST} /opsidra/bajapromocional/ [bajapromocional]
     *
     * @apiName bajapromocional
     * @apiDescription Servicio para dar de baja un art\u00EDculo promocional creado.
     * @apiGroup Promocionales
     * @apiVersion 1.0.0
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idArtPromocional Identificador del art\u00EDculo promocional a dar de baja.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idArtPromocional": "1"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente.",
     *        "clase": "OperacionPromocionales",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-201",
     *        "mostrar": "1",
     *        "descripcion": "No existe el recurso deseado.",
     *        "clase": "OperacionPromocionales",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputPromocionales doDelPromocionales(@Context HttpServletRequest req, InputPromocionales input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputPromocionales response = new Promocionales().bajaPromocionales(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modpromocional")
    /**
     * @api {POST} /opsidra/modpromocional/ [modpromocional]
     *
     * @apiName modpromocional
     * @apiDescription Servicio para modificar art\u00EDculos promocionales creados.
     * @apiGroup Promocionales
     * @apiVersion 1.0.0
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idArtPromocional Identificador del art\u00EDculo promocional a modificar.
     * @apiParam {String} codArticulo C\u00F3digo del art\u00EDculo promocional.
     * @apiParam {String} descripcion Nombre del art\u00EDculo promocional.
     * @apiParam {String} tipoGrupo Nombre del grupo del art\u00EDculo promocional.
     * @apiParam {String} estado Estado nuevo del art\u00EDculo promocional (ALTA o BAJA).
     * @apiParam {String} idOferta Id de la oferta con la cual se venderan promocionales en FS, aplica solo para SV.
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idArtPromocional": "2",
     *     "codArticulo": "C2",
     *     "descripcion": "Pachon negro",
     *     "tipoGrupo": "GRUPO B",
     *      "idOferta":"9146027023265528900",
     *     "estado": "ALTA"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente.",
     *        "clase": "OperacionPromocionales",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     *  
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos:  El nombre enviado se encontr\u00F3 con el c\u00F3digo 'C4', en el grupo: GRUPO B y con el estado: ALTA",
     *     "clase": "CtrlPromocionales",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputPromocionales doPutPromocionales(@Context HttpServletRequest req, InputPromocionales input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputPromocionales response = new Promocionales().modificaPromocionales(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios Promocionales ------------------------------//

    //----------------------------------- Servicios Asignaci\u00F3n ---------------------------------//
    @Path("/creaasignacionreserva")
    /**
     * @api {POST} /opsidra/creaasignacionreserva/ [creaAsignacion]
     * 
     * @apiName creaAsignacion
     * @apiDescription Servicio para crear Asignaciones por pa\u00EDs.
     * @apiGroup Asignacion/Reserva
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais donde se realizar\u00E9 la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} tipo Nombre transacci\u00F3n. Puede ser RESERVA o ASIGNACION.
     * @apiParam {String} idVendedor Identificador del vendedor que reserva/asigna.
     * @apiParam {String} idBodegaOrigen Identificador de la bodega origen.
     * @apiParam {String} idBodegaDestino Identificador de la bodega destino.
     * @apiParam {String} observaciones Observaciones de la transacci\u00F3n.
     * @apiParam {String} idArticulo Identificador del art\u00EDculo a reservar/asignar.
     * @apiParam {String} serie Serie del art\u00EDculo a reservar/asignar.
     * @apiParam {String} serieFinal Serie final del rango del art\u00EDculo a reservar/asignar.
     * @apiParam {String} cantidad Cantidad de art\u00EDculos a reservar/asignar.
     * @apiParam {String} serieAsociada Serie asociada al art\u00EDculo.
     * @apiParam {String} tipoInv Nombre del tipo de inventario del art\u00EDculo.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuarioprueba",
     *     "tipo": "RESERVA",
     *     "idVendedor": "10",
     *     "idBodegaOrigen": "100",
     *     "idBodegaDestino": "110",
     *     "observaciones": "",
     *     "articulos":[
     *         {
     *             "idArticulo":"15",
     *             "serie":"",
     *             "serieFinal":"",
     *             "cantidad":"10",
     *             "serieAsociada":"",
     *             "tipoInv": "INV_TELCA"
     *         },
     *         {
     *             "idArticulo":"",
     *             "serie":"A1",
     *             "serieFinal":"",
     *             "cantidad":"1",
     *             "serieAsociada":"",
     *             "tipoInv": "INV_TELCA"
     *         },
     *         {
     *             "idArticulo":"",
     *             "serie":"1000",
     *             "serieFinal":"2000",
     *             "cantidad":"",
     *             "serieAsociada":"",
     *             "tipoInv": "INV_TELCA"
     *         }
     *     ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "idAsignacion": "20",
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente. Algunas series no se pudieron asignar: A3 Algunos art\u00EDculos no se pudieron asignar por las siguientes razones: Art\u00EDculos no existen en el inventario: 598. ",
     *     "clase": "OperacionAsignacion",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   },
     *   "descErrorSeries": "Series no v\u00E9lidas en la bodega.",
     *   "series": {
     *     "serie": "A3"
     *   },
     *   "descErrorArticulos": "Art\u00EDculos no existen en el inventario: ",
     *   "articulos": {
     *     "idArticulo": "598"
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos:  La Bodega Destino enviada no es apta.",
     *     "clase": "CtrlAsignacion",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputAsignacion doCreaAsignacion(@Context HttpServletRequest req, InputAsignacion input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputAsignacion response = new Asignacion().creaAsignacion(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modasignacionreserva")
    /**
     * @api {POST} /opsidra/modasignacionreserva/ [modAsignacion]
     * 
     * @apiName modAsignacion
     * @apiDescription Servicio para modificar Asignaciones o Reservas por pa\u00EDs.
     * @apiGroup Asignacion/Reserva
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais donde se realizar\u00E9 la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idAsignacionReserva Identificador de la asignaci\u00F3n/reserva que se desea modificar.
     * @apiParam {String} estado Estado al que se desea cambiar la asignaci\u00F3n/reserva. Puede ser FINALIZADA o CANCELADA.
     * @apiParam {String} observaciones Observaciones de la transacci\u00F3n.
     * 
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *    "codArea": "505",
     *    "usuario": "usuario.pruebas",
     *    "idAsignacionReserva": "15",
     *    "estado": "CANCELADA",
     *    "observaciones": "Observaciones de la transacci\u00F3n."
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionAsignacion",
     *     "metodo": "doPut",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-201",
     *     "mostrar": "1",
     *     "descripcion": "No existe el recurso deseado.",
     *     "clase": "OperacionAsignacion",
     *     "metodo": "doPut",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputAsignacion doModAsignacion(@Context HttpServletRequest req, InputAsignacion input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputAsignacion response = new Asignacion().modAsignacion(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/moddetasignacionreserva")
    /**
     * @api {POST} /opsidra/moddetasignacionreserva/ [modDetAsignacion]
     * 
     * @apiName modDetAsignacion
     * @apiDescription Servicio para modificar detalle de Asignaciones o Reservas por pa\u00EDs.
     * @apiGroup Asignacion/Reserva
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais donde se realizar\u00E9 la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idAsignacionReserva Identificador de la asignaci\u00F3n/reserva que se desea modificar.
     * @apiParam {String} observaciones Observaciones de la transacci\u00F3n.
     * @apiParam {String} articulos Listado de art\u00EDculos a agregar/quitar/modificar de la asignaci\u00F3n o reserva. En caso de ser una Asignaci\u00F3n y no se env\u00EDa el listado de art\u00EDculos, se CANCELA la asignaci\u00F3n.
     * @apiParam {String} idArticulo Identificador del art\u00EDculo a reservar/asignar.
     * @apiParam {String} serie Serie del art\u00EDculo a reservar/asignar.
     * @apiParam {String} serieFinal Serie final del rango del art\u00EDculo a reservar/asignar.
     * @apiParam {String} cantidad Cantidad de art\u00EDculos a reservar/asignar.
     * @apiParam {String} serieAsociada Serie asociada al art\u00EDculo.
     * @apiParam {String} tipoInv Nombre del tipo de inventario del art\u00EDculo.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "idAsignacionReserva": "28",
     *     "observaciones": "Observaciones de la transacci\u00F3n.",
     *     "articulos": [
     *         {
     *             "idArticulo":"",
     *             "serie":"427C1F97",
     *             "serieFinal":"",
     *             "cantidad":"",
     *             "serieAsociada":"",
     *             "tipoInv": "INV_TELCA"
     *         },
     *         {
     *             "idArticulo":"",
     *             "serie":"143",
     *             "serieFinal":"145",
     *             "cantidad":"",
     *             "serieAsociada":"",
     *             "tipoInv": "INV_TELCA"
     *         },
     *         {
     *             "idArticulo":"9",
     *             "serie":"",
     *             "serieFinal":"",
     *             "cantidad":"15",
     *             "serieAsociada":"",
     *             "tipoInv": "INV_SIDRA"
     *         }
     *     ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "201",
     *     "mostrar": "1",
     *     "descripcion": "Recurso modificado exitosamente.",
     *     "clase": "OperacionAsignacion",
     *     "metodo": "doModAsignacionReserva",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-201",
     *     "mostrar": "1",
     *     "descripcion": "No existe el recurso deseado.",
     *     "clase": "OperacionAsignacion",
     *     "metodo": "doModAsignacionReserva",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputAsignacion doModDetAsignacion(@Context HttpServletRequest req, InputAsignacion input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputAsignacion response = new Asignacion().modDetAsignacion(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios Asignacion ---------------------------------//

    /**
     * Servicio de Login para app M\u00F3vil
     * @throws StatementException 
     * @throws LDAPException 
     * @throws ExcepcionClaveNoValidaLDAP 
     * @throws ExcepcionConfiguracionLdap 
     * @throws ExcepcionUsuarioBloqueado 
     * @throws ExcepcionClaveExpirada 
     * @throws ExcepcionClaveNoValidaModsec 
     * @throws ExcepcionClaveNoCumplePoliticas 
     */
    @Path("/login")
    /**
     *@api {POST} /opsidra/login/ [login]
     * 
     * @apiName login
     * @apiDescription Servicio para inicio de sesi\u00F3n de app m\u00F3vil.
     * @apiGroup LOGIN
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo del pa\u00EDs al que pertenece el usuario que inicia sesi\u00F3n. 
     * @apiParam {String} usuario Nombre de usuario que inicia sesion.
     * @apiParam {String} password Contrasenia del usuario. 
     * @apiParam {String} codDispositivo C\u00F3digo del dispositivo desde donde se inicia sesi\u00F3n.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
    * {
    *    "codArea":"505",
    *    "usuario": "usuario",
    *    "password": "1232",
    *    "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62"
    * }
    * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
    * {
    *     "respuesta": {
    *        "codResultado": "4",
    *        "mostrar": "1",
    *        "descripcion": "OK. Usuario logueado correctamente, pero no se encuentra asociado a una Panel o Ruta",
    *        "clase": " ",
    *        "metodo": " ",
    *        "excepcion": " ",
    *        "tipoExepcion": ""
    *     },
    *     "token": "FB116A9905314D410B56C2A8BC21F4902D54041634564CC41063560083B0E790",
    *     "idVendedor": "1823",
    *     "responsable":"1",
    *     "idResponsable":"1823",
    *     "idBodegaVirtual": "",
    *     "idBodegaVendedor": "",
    *     "idDTS": "",
    *     "nombreDistribuidor":"",
    *     "tipo": ""
    *     "idTipo": "97",
    *     "folioManual": "1",
    *     "longitud": " ",
    *	  "latitud": " ",
    * 	  "nombreTipo": "RUTA TECH S.A.",
    *     "numRecarga": "63257899",
    *     "fechaCierre": "ddMMyyyy",
    *     "numConvenio": "5452",
    *     "tasaCambio": "20.00",
    *     "vendedorAsignado": "4510",
    *     "pin": "512",
    *     "nivelBuzon": "2",
     *    "numIdentificacion": "34543534534534",
     *    "tipoIdentificacion": "RUC",
     *    "numTelefono": "88005544",
     *    "idDispositivo": "236"
    * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
    * {
    *    "respuesta": {
    *        "codResultado": "-101",
    *        "mostrar": "0",
    *        "descripcion": "Ocurrio un Problema inesperado, contacte a su Administrador.",
    *        "clase": " ", 
    *        "metodo": "",
    *        "excepcion": "",
    *        "tipoExepcion": ""
    *    }
    * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputLogin login(@Context HttpServletRequest req, InputSesionParametros input)
            throws ExcepcionClaveNoCumplePoliticas, ExcepcionClaveNoValidaModsec, ExcepcionClaveExpirada,
            ExcepcionUsuarioBloqueado, ExcepcionConfiguracionLdap, ExcepcionClaveNoValidaLDAP, LDAPException,
            StatementException {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputLogin response = new Login().iniciarSesion(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/ingresosalida")
    /**
     *@api {POST} /opsidra/ingresosalida/ [ingresoSalida]
     * 
     * @apiName ingresoSalida
     * @apiDescription Servicio para modificar buzones que ser\u00E9n utiles en la creaci\u00F3n de solitudes para el work flow.
     * @apiGroup Promocionales
     * @apiVersion 1.0.0
     * @apiParam {String} codArea c\u00F3digo de area del pa\u00EDs en el cual se desea 
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} articulos listado de articulos de los cuales se realizar\u00E9 ingreso o egreso de inventario. 
     * @apiParam {String} idBodegaVirtual id de bodega virtual a la que se realizar\u00E9 la transacci\u00F3n. 
     * @apiParam {String} tipo identifica si es ingreso o salida lo que se realizar\u00E9.
     * @apiParam {String} idArticulo id del Art\u00EDculo que se ingresar\u00E9 o egresar\u00E9.
     * @apiParam {String} cantidad cantidad de art\u00EDculos a operar.
     * @apiParam {String} observaciones observaciones varias.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario",
     *     "articulos": [{
     *         "idBodegaVirtual": "82",
     *         "tipo": "ingreso",
     *         "idArticulo": "1",
     *         "cantidad": "10",
     *         "observaciones": "probandoingresos"
     *     }, {
     *         "idBodegaVirtual": "82",
     *         "tipo": "salida",
     *         "idArticulo": "2",
     *         "cantidad": "10",
     *         "observaciones": "probandoingresos"
     *     }]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *     "respuesta": {
     *         "codResultado": "14",
     *         "mostrar": "1",
     *         "descripcion": "OK.Transacci\u00F3n procesada correctamente. Los siguientes art\u00EDculos no pudieron operarse correctamente.",
     *         "clase": " ",
     *         "metodo": " ",
     *         "excepcion": " ",
     *         "tipoExepcion": ""
     *     },
     *     "datosIncorrectos": {
     *         "razon": "Los art\u00EDculos siguientes no existen en el Sistema de SIDRA.",
     *         "articulos": {
     *             "tipo": "INGRESO",
     *             "idArticulo": "100"
     *         }
     *     }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
    *    {
    *        "respuesta": {
    *            "codResultado": "-59",
    *            "mostrar": "1",
    *            "descripcion": "El siguiente elemento del listado de art\u00EDculos tiene incompletos los siguientes datos: No. Elemento:3.El campo cantidad esta vac\u00EDo",
    *            "clase": " ",
    *            "metodo": "validarDatos",
    *            "excepcion": "class CtrlIngresoSalidaInvPromocional",
    *            "tipoExepcion": "Generales"
    *        }
    *    }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputIngresoSalida ingresoSalida(@Context HttpServletRequest req, InputIngresoSalida input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputIngresoSalida response = new IngresoSalidaInvPromo().realizarIngSalida(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/aceptarsolicitud")
    /**
     *@api {POST} /opsidra/aceptarsolicitud/ [aceptarSolicitud]
     * 
     * @apiName aceptarSolicitud
     * @apiDescription Servicio aceptar o rechazar art\u00EDculos de solicitudes tipo DEVOLUCION o SINIESTRO.
     * @apiGroup Solicitud
     * @apiVersion 1.0.0
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idSolicitud id de la solicitud a aceptar o rechazar, esta solicitud debe ser de tipo DEVOLUCION o SINIESTRO.
     * @apiParam {String} idBodegaVirtual id de bodega virtual a la que se realizar\u00E9 la transacci\u00F3n. 
     * @apiParam {String} observaciones observaciones acerca de proceso.
     * @apiParam {String} idBuzonSiguiente id del buzon de workflow hacia donde se enviar\u00E9 la solicitud.
     * @apiParam {String} articulos listado de articulos de los cuales se aceptara  o rechazar\u00E9 devolucion.
     * @apiParam {String} tcscSolicitudId Id del registro que se ingresar\u00E9 o egresar\u00E9. 
     * @apiParam {String} idArticulo id del Art\u00EDculo que se ingresar\u00E9 o egresar\u00E9.
     * @apiParam {String} codDispositivo C\u00F3digo del dispositivo que se aceptar\u00E9 o rechazar\u00E9.
     * @apiParam {String} aceptado indica si el art\u00EDculo se acepta o rechaza para la devoluci\u00F3n. 1=ACEPTADO, 0=RECHAZADO.
     * @apiParam {String} observaciones observaciones varias.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
        * {
        *    "codArea": "505",
        *    "usuario": "usuario.pruebas",
        *    "idSolicitud":"1",
        *    "idBodega":"100",
        *    "observaciones":"sdf5s5f4s65f456ssfds",
        *    "idBuzonSiguiente":"",
        *    "articulos":[
        *       {
        *           "tcscSolicitudId":"1",
        *           "idArticulo":"1",
        *           "codDispositivo":"",
        *           "aceptado":"1",
        *           "observaciones":""
        *       },
        *       {
        *           "tcscSolicitudId":"1",
        *           "idArticulo":"2",
        *           "codDispositivo":"",
        *           "aceptado":"0",
        *           "observaciones":""
        *        }
        *    ]
        * }
        * 
    * @apiSuccessExample {json} Respuesta-\u00E9xito:
    *   {
    *       "codResultado": "13",
    *       "mostrar": "1",
    *       "descripcion": "OK.Devolucion procesada correctamente",
    *       "clase": " ",
    *       "metodo": " ",
    *       "excepcion": " ",
    *       "tipoExepcion": ""
    *   }
    * 
    * @apiErrorExample {json} Respuesta-Error:     
    *   {
    *       "codResultado": "-216",
    *       "mostrar": "1",
    *       "descripcion": "La solicitud no se encuentra ABIERTA.",
    *       "clase": " ",
    *       "metodo": "aceptaRechaza",
    *       "excepcion": "class CtrlDevolucion",
    *       "tipoExepcion": "Generales"
    *   }
    * 
    * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public Respuesta ingresoSalida(@Context HttpServletRequest req, InputAceptaRechazaDevolucion input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        Respuesta response = new Devolucion().aceptarSolicitud(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    /***********************************************************************************
     * SERVICIOS PARA CONFIGURACION DE DISPOSITIVOS
     **********************************************************************************/
    @Path("/creadispositivo")
    /**
     *@api {POST} /opsidra/creadispositivo/ [creadispositivo]
     * 
     * @apiName creadispositivo
     * @apiDescription Servicio para crear dispositivos que ser\u00E9n utilizados por vendedores con la aplicacion movil de sidra.
     * @apiGroup Dispositivos
     * @apiVersion 1.0.0
     * @apiParam {String} codArea c\u00F3digo de \u00E9rea del pa\u00EDs donde se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} codigoDispositivo c\u00F3digo unico que identifica al dispositivo, el cual es anexado al folio que utiliza el vendedor.
     * @apiParam {String} modelo modelo del dispositivo que se esta registrando.
     * @apiParam {String} descripcion descripci\u00F3n del dispositivo.
     * @apiParam {String} numTelefono n\u00FAmero del telefono del dispositivo. 
     * @apiParam {String} cajaNumero N\u00FAmero de caja del dispositivo.
     * @apiParam {String} zona Zona del dispositivo.
     * @apiParam {String} codOficina codigo de la oficina.
     * @apiParam {String} codVendedor codigo de vendedor.
     * @apiParam {String} idPlaza id que identifica a la plaza.
     * @apiParam {String} idPuntoVenta id que identifica al Punto de Venta.
     * @apiParam {String} userId id de user.
     * @apiParam {String} username nombre de usuario.    
     * @apiParam {String} resolucion N\u00FAmero de resoluci\u00F3n del dispositivo.
     * @apiParam {String} fechaResolucion Fecha de la resoluci\u00F3n en formato yyyyMMdd.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
    * {
    *    "codArea":"505",
    *    "usuario": "usuario",
    *    "codigoDispositivo": "545645ds",
    *    "modelo":"AA",
    *    "descripcion":"SAMSUNG S4 MINI",
    *    "numTelefono":"54870022",
    *    "cajaNumero":"2",
    *    "zona":"1",
    *    "codOficina": "",
    *    "codVendedor": "",
    *    "idPlaza":"",
    *    "idPuntoVenta": "",
    *    "userId": "",
    *    "username": "",
    *    "resolucion":"54870021",
    *    "fechaResolucion":"20160728"
    * }
    * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
    *    {
    *        "idDispositivo": "1",
    *        "respuesta": {
    *            "codResultado": "15",
    *            "mostrar": "1",
    *            "descripcion": "Ok. Creaci\u00F3n de Dispositivo exitosa ",
    *            "clase": " ",
    *            "metodo": " ",
    *            "excepcion": " ",
    *            "tipoExepcion": ""
    *        }
    *    }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
    *    {
    *        "respuesta": {
    *            "codResultado": "-70",
    *            "mostrar": "1",
    *            "descripcion": "El par\u00E9metro de entrada \\\"numTelefono\"\\ esta vac\u00EDo.",
    *            "clase": " ",
    *            "metodo": "validarDatos",
    *            "excepcion": "class CtrlDispositivo",
    *            "tipoExepcion": "Generales"
    *        }
    *    }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputDispositivo creaDispositivo(@Context HttpServletRequest req, InputDispositivo input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputDispositivo response = new Dispositivo().crearDispositivo(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modificadispositivo")
    /**
     *@api {POST} /opsidra/modificadispositivo/ [modificadispositivo]
     * 
     * @apiName modificadispositivo
     * @apiDescription Servicio para modificar dispositivos.
     * @apiGroup Dispositivos
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs donde se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idDispositivo Id del dispositivo que se desea modificar.
     * @apiParam {String} descripcion Descripci\u00F3n a modificar.
     * @apiParam {String} numTelefono N\u00FAmero de tel\u00E9fono a modificar.
     * @apiParam {String} responsable Id de la panel o ruta a la que se asignar\u00E9 el dispositivo.
     * @apiParam {String} tipoResponsable="PANEL o RUTA" Tipo panel o ruta a la que se asignar\u00E9 el dispositivo.
     * @apiParam {String} vendedorAsingado id del vendedor de la ruta o panel que utilizar\u00E9 el dispositivo.
     * @apiParam {String} cajaNumero N\u00FAmero de caja del dispositivo.
     * @apiParam {String} zona Zona del dispositivo.
     * @apiParam {String} codOficina codigo de la oficina.
     * @apiParam {String} codVendedor codigo de vendedor.
     * @apiParam {String} idPlaza id que identifica a la plaza.
     * @apiParam {String} idPuntoVenta id que identifica al Punto de Venta.
     * @apiParam {String} userId id de user.
     * @apiParam {String} username nombre de usuario.   
     * @apiParam {String} resolucion N\u00FAmero de resoluci\u00F3n del dispositivo.
     * @apiParam {String} fechaResolucion Fecha de la resoluci\u00F3n en formato yyyyMMdd.
     * @apiParam {String} idDistribuidor Id del distribuidor asociado.
     * @apiParam {String} codOficina C\u00F3digo de oficina de SCL.
     * @apiParam {String} codVendedor C\u00F3digo de vendedor de SCL.
     * @apiParam {String} estado Estado que desea aplicarse al dispositivo.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *    "codArea": "505",
     *    "usuario": "usuario",
     *    "idDispositivo": "1",
     *    "descripcion": "ALCATEL ONE TOUCH ",
     *    "numTelefono": "78544500",
     *    "responsable": "10",
     *    "tipoResponsable": "PANEL",
     *    "vendedorAsignado": "1020",
     *    "cajaNumero":"2",
     *    "zona":"1",
     *    "idPlaza":"",
     *    "idPuntoVenta": "",
     *    "userId": "",
     *    "username": "",
     *    "resolucion":"54870021",
     *    "fechaResolucion":"20160728",
     *    "idDistribuidor":"",
     *    "codOficina":"",
     *    "codVendedor":"",
     *    "estado": "BAJA"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "idDispositivo": "1",
     *    "respuesta": {
     *        "codResultado": "11",
     *        "mostrar": "1",
     *        "descripcion": "Ok. Buz\u00F3n modificado exitosamente.",
     *        "clase": " ",
     *        "metodo": " ",
     *        "excepcion": " ",
     *        "tipoExepcion": ""
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *     "respuesta": {
     *         "codResultado": "-70",
     *         "mostrar": "1",
     *         "descripcion": "El par\u00E9metro de entrada \\\"numTelefono\"\\ esta vac\u00EDo.",
     *         "clase": " ",
     *         "metodo": "validarDatos",
     *         "excepcion": "class CtrlDispositivo",
     *         "tipoExepcion": "Generales"
     *     }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputDispositivo modDispositivo(@Context HttpServletRequest req, InputDispositivo input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputDispositivo response = new Dispositivo().modDispositivo(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    /********************************************************************************************
     * SERVICIOS PARA CONFIGURACION DE TIPO DE TRANSACCIONES
     *******************************************************************************************/
    @Path("/creatipotransaccion")
    /**
     *@api {POST} /opsidra/creatipotransaccion/ [creatipotransaccion]
     * 
     * @apiName creatipotransaccion
     * @apiDescription Servicio para crear un catalogo de las transacciones que son realizadas en el inventario de SIDRA.
     * @apiGroup TipoTransaccion
     * @apiVersion 1.0.0
     * @apiParam {String} codArea c\u00F3digo de area del pa\u00EDs que se desea  realizar la transacci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} codigoTransaccion c\u00F3digo unico que identifica el tipo de transacci\u00F3n.
     * @apiParam {String} descripcion descripci\u00F3n del tipo de transaccion a realizar.
     * @apiParam {String} tipoMovimiento indica el tipo de movimiento a realizar.
     * @apiParam {String} tipoAfecta indica de que manera afecta al inventario. Ej: Suma, Resta.
     *  
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
    * {
    *    "codArea":"505",
    *    "usuario": "usuario",
    *    "codigoTransaccion": "I",
    *    "descripcion":"Ingreso de art\u00EDculos a inventario",
    *    "tipoMovimiento":"Ingreso",
    *    "tipoAfecta":"Suma"
    * }
    * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
    *    {
    *        "idTransaccion": "9",
    *        "respuesta": {
    *            "codResultado": "200",
    *            "mostrar": "1",
    *            "descripcion": "Campos agregados exitosamente. ",
    *            "clase": " ",
    *            "metodo": " ",
    *            "excepcion": " ",
    *            "tipoExepcion": ""
    *        }
    *    }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
    *    {
    *        "respuesta": {
    *            "codResultado": "-82",
    *            "mostrar": "1",
    *            "descripcion": "El par\u00E9metro de entrada \\tipoAfecta\"\\ esta vac\u00EDo.",
    *            "clase": " ",
    *            "metodo": "validarDatos",
    *            "excepcion": "class CtrlTipoTransaccionInv",
    *            "tipoExepcion": "Generales"
    *        }
    *    }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputTransaccionInv creaTransaccion(@Context HttpServletRequest req, InputTransaccionInv input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputTransaccionInv response = new TipoTransaccion().creaTransaccion(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modificatipotransaccion")
    /**
     *@api {POST} /opsidra/modificatipotransaccion/ [modificatipotransaccion]
     * 
     * @apiName modificatipotransaccion
     * @apiDescription Servicio para modificar informaci\u00F3n sobre las transacciones que son realizadas en el inventario de SIDRA..
     * @apiGroup TipoTransaccion
     * @apiVersion 1.0.0
     * @apiParam {String} codArea c\u00F3digo de area del pa\u00EDs que se desea  realizar la transacci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} idTipoTransaccion id de la transaccion que se desea modificar. 
     * @apiParam {String} descripcion descripcion a modificar. 
     * @apiParam {String} tipoMovimiento descripcion del tipo de movimiento.
     * @apiParam {String} tipoAfecta descripci\u00F3n de la forma que afecta el inventario.
     * @apiParam {String} estado estado que desea aplicarse al dispositivo.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
    * {
    *    "codArea":"505",
    *    "usuario": "usuario",
    *    "idTipoTransaccion": "1",
    *    "descripcion": "Ingreso de art\u00EDculos promocionales",
    *    "tipoMovimiento":"Ingreso",
    *    "tipoAfecta":"Suma",
    *    "estado": "BAJA"
    * }
    * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
    *    {
    *        "respuesta": {
    *            "codResultado": "201",
    *            "mostrar": "1",
    *            "descripcion": "Recurso modificado exitosamente. ",
    *            "clase": " ",
    *            "metodo": " ",
    *            "excepcion": " ",
    *            "tipoExepcion": ""
    *        }
    *    }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
    *    {
    *        "respuesta": {
    *            "codResultado": "-82",
    *            "mostrar": "1",
    *            "descripcion": "El par\u00E9metro de entrada \\tipoAfecta\"\\ esta vac\u00EDo.",
    *            "clase": " ",
    *            "metodo": "validarDatos",
    *            "excepcion": "class CtrlTipoTransaccionInv",
    *            "tipoExepcion": "Generales"
    *        }
    *    }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputTransaccionInv modTransaccion(@Context HttpServletRequest req, InputTransaccionInv input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputTransaccionInv response = new TipoTransaccion().modTransaccion(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/crearlog")
    /**
     *@api {POST} /opsidra/crearlog/ [crearlog]
     * 
     * @apiName crearlog
     * @apiDescription Servicio para crear registros de log.
     * @apiGroup Log
     * @apiVersion 1.0.0
     * @apiParam {String} codArea c\u00F3digo de \u00E9rea del pa\u00EDs donde se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} log Listado de logs a insertar.
     * @apiParam {String} log.tipoTransaccion Nombre del tipo de transacci\u00F3n del log.
     * @apiParam {String} log.origen Origen del log.
     * @apiParam {String} log.id Identificador del tipo.
     * @apiParam {String} log.tipoId Tipo de Id que se insertar\u00E9 en el log.
     * @apiParam {String} log.resultado Resultado a ingresar en el log.
     * @apiParam {String} log.descripcionError Error que se desea insertar en el log. 
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
    * {
    *     "codArea": "505",
    *     "usuario": "usuario",
    *     "log" :[
    *         { 
    *             "tipoTransaccion": "CREAR_SOLICITUD_WF",
    *             "origen": "WS CREA SOLICITUD",
    *             "id": "10",
    *             "tipoId": "solicitud",
    *             "resultado": "OK. CREACION EXITOSA",
    *             "descripcionError": ""
    *         }
    *     ]
    * }
    * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
    * {
    *    "idBuzon": "1",
    *    "respuesta": {
    *        "codResultado": "11",
    *        "mostrar": "1",
    *        "descripcion": "Ok. Buz\u00F3n modificado exitosamente.",
    *        "clase": " ",
    *        "metodo": " ",
    *        "excepcion": " ",
    *        "tipoExepcion": ""
    *    }
    * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
    *   {
    *       "respuesta": {
    *           "codResultado": "-70",
    *           "mostrar": "1",
    *           "descripcion": "El par\u00E9metro de entrada \\\"numTelefono\"\\ esta vac\u00EDo.",
    *           "clase": " ",
    *           "metodo": "validarDatos",
    *           "excepcion": "class CtrlDispositivo",
    *           "tipoExepcion": "Generales"
    *       }
    *   }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public Respuesta crearLog(@Context HttpServletRequest req, InputLogSidra input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        Respuesta response = new LogSidraDirector().creaLog(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    /*************************************************************************/
    /*************************** SERVICIOS RELEASE 3 *************************/
    /*************************************************************************/
    

    @Path("/modjornada")
    /**
     * @api {POST} /opsidra/modjornada/ [modjornada]
     * 
     * @apiName modjornada
     * @apiDescription Servicio para modificar Jornadas por pa\u00EDs.
     * @apiGroup Jornada
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de usuario.
     * @apiParam {String} idVendedor Identificador del vendedor asociado.
     * @apiParam {String} idJornada Identificador de la jornada a modificar.
     * @apiParam {String} tipoOperacion="RECHAZO o LIQUIDACION" Nombre del tipo de operaci\u00F3n a aplicar.
     * @apiParam {String} observaciones Observaciones de la operacion de la Jornada.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "token": "WEB",
     *     "usuario": "usuario.pruebas",
     *     "idVendedor": "2322",
     *     "idJornada": "63",
     *     "tipoOperacion": "LIQUIDACION",
     *     "observaciones": ""
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "201",
     *     "mostrar": "1",
     *     "descripcion": "Recurso modificado exitosamente.",
     *     "clase": "OperacionJornada",
     *     "metodo": "doPut",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-368",
     *     "mostrar": "1",
     *     "descripcion": "La jornada no se encuentra en estado FINALIZADA.",
     *     "clase": "OperacionJornada",
     *     "metodo": "doPut",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputJornada doModJornada(@Context HttpServletRequest req, InputJornada input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputJornada response = new Jornada().modJornada(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios Jornada ------------------------------------//
    
    //----------------------------------- Servicios VendedorDTS --------------------------------//
    @Path("/asignavendedordts")
    /**
     * @api {POST} /opsidra/asignavendedordts/ [asignavendedordts]
     * 
     * @apiName asignavendedordts
     * @apiDescription Servicio para crear VendedorDTSes por pa\u00EDs.
     * @apiGroup VendedorDTS
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idDistribuidor Identificador del distribuidor a asociar.
     * @apiParam {String} idBodegaVirtual Identificador de la bodega virtual que ser\u00E9 asociada.
     * @apiParam {String} idVendedor Identificador del vendedor a asociar.
     * @apiParam {String} usuarioVendedor Usuario del vendedor a asociar.
     * @apiParam {String} nombres Nombre o nombres del vendedor que se desea asociar.
     * @apiParam {String} apellidos Apellido o apellidos del vendedor que se desea asociar.
     * @apiParam {String} canal canal de distribuci\u00F3n del vendedor.
     * @apiParam {String} subcanal subcanal de distribuci\u00F3n del vendedor.
     * @apiParam {String} numeroRecarga n\u00FAmero que tendran disponible el vendedor para vender o transferir saldo de recargas.
     * @apiParam {String} pin pin del n\u00FAmero de recarga.
     * @apiParam {String} [dtsFuente] n\u00FAmero de distribuidor fuente del n\u00FAmero de recarga.
     * @apiParam {String} codVendedor C\u00F3digo de vendedor de SCL (\u00FAnicamente PA).
     * @apiParam {String} email Email del vendedor que se desea asociar.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea":"505",
     *     "usuario":"usuario.pruebas",
     *     "idDistribuidor":"96",
     *     "idBodegaVirtual":"205",
     *     "idVendedor":"305",
     *     "usuarioVendedor":"sergio.lujan",
     *     "nombres":"Sergio",
     *     "apellidos":"Lujan",
     *     "canal":"SIDRA",
     *     "subcanal":"MULTIMARCA",
     *     "numeroRecarga":"45502255",
     *     "pin":"1123",
     *     "dtsFuente":"111",
     *     "email":"slujan@dts.com",
     *     "codVendedor":"442"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionVendedorDTS",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-160",
     *     "mostrar": "1",
     *     "descripcion": "El vendedor ya se encuentra registrado.",
     *     "clase": "OperacionVendedorDTS",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputVendedorDTS doPostVendedorDTS(@Context HttpServletRequest req, InputVendedorDTS input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputVendedorDTS response = new VendedorDTS().asignaVendedorDTS(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);
        
        return response;
    }

    @Path("/bajavendedordts")
    /**
     * @api {POST} /opsidra/bajavendedordts/ [bajavendedordts]
     *
     * @apiName bajavendedordts
     * @apiDescription Servicio para dar de baja un VendedorDTS por pa\u00EDs.
     * @apiGroup VendedorDTS
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idVendedorDTS Identificador del vendedor que se desea dar de baja.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea":"505",
     *     "usuario":"usuario.pruebas",
     *     "idVendedorDTS":"4"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta": {
     *        "codResultado": "201",
     *        "mostrar": "1",
     *        "descripcion": "Recurso modificado exitosamente.",
     *        "clase": "OperacionVendedorDTS",
     *        "metodo": "doPutDel",
     *        "excepcion": " ",
     *        "tipoExepcion": " "
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-178",
     *     "mostrar": "1",
     *     "descripcion": "Las bodegas virtuales asociadas al vendedor poseen inventario.",
     *     "clase": "OperacionVendedorDTS",
     *     "metodo": "doPutDel",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputVendedorDTS doDelVendedorDTS(@Context HttpServletRequest req, InputVendedorDTS input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputVendedorDTS response = new VendedorDTS().bajaVendedorDTS(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modVendedorDTS")
    /**
     * @api {POST} /opsidra/modVendedorDTS/ [modVendedorDTS]
     * 
     * @apiName modVendedorDTS
     * @apiDescription Servicio para modificar asociaciones de Vendedores con Distribuidores Sidra.
     * @apiGroup VendedorDTS
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idVendedorDTS Identificador de la asociaci\u00F3n de vendedor a modificar.
     * @apiParam {String} idDistribuidor Identificador del distribuidor a asociar.
     * @apiParam {String} idBodegaVirtual Identificador de la bodega virtual que ser\u00E9 asociada.
     * @apiParam {String} nombres Nombre o nombres del vendedor que se desea modificar.
     * @apiParam {String} apellidos Apellido o apellidos del vendedor que se desea modificar.
     * @apiParam {String} canal canal de distribuci\u00F3n del vendedor.
     * @apiParam {String} subcanal subcanal de distribuci\u00F3n del vendedor.
     * @apiParam {String} numeroRecarga n\u00FAmero que tendran disponible el vendedor para vender o transferir saldo de recargas.
     * @apiParam {String} pin pin del n\u00FAmero de recarga.
     * @apiParam {String} [dtsFuente] n\u00FAmero de distribuidor fuente del n\u00FAmero de recarga.
     * @apiParam {String} email Email del vendedor que se desea asociar.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea":"505",
     *     "usuario":"usuario.pruebas",
     *     "idVendedorDTS":"100",
     *     "idDistribuidor":"96",
     *     "idBodegaVirtual":"205",
     *     "nombres":"Sergio",
     *     "apellidos":"Lujan",
     *     "canal":"SIDRA",
     *     "subcanal":"MULTIMARCA",
     *     "numeroRecarga":"45502255",
     *     "pin":"1123",
     *     "dtsFuente":"111",
     *     "email":"slujan@dts.com",
     *     "estado":"ALTA"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos modificados exitosamente.",
     *     "clase": "OperacionVendedorDTS",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
	 *	{
	 *	    "respuesta": {
	 *	        "codResultado": "-201",
	 *	        "mostrar": "1",
	 *	        "descripcion": "No existe el recurso deseado.",
	 *	        "clase": "OperacionVendedorDTS",
	 *	        "metodo": "doPutDel",
	 *	        "excepcion": " ",
	 *	        "tipoExepcion": "Generales"
	 *	    }
	 *	 }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputVendedorDTS modVendedorDTS(@Context HttpServletRequest req, InputVendedorDTS input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputVendedorDTS response = new VendedorDTS().modVendedorDTS(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios VendedorDTS --------------------------------//
    
    //----------------------------------- Servicios OfertaCampania -----------------------------//
    @Path("/creaofertacampania")
    /**
     * @api {POST} /opsidra/creaofertacampania/ [creaofertacampania]
     * 
     * @apiName creaofertacampania
     * @apiDescription Servicio para crear Ofertas o Campanias por pa\u00EDs.
     * @apiGroup OfertaCampania
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} tipo Nombre del tipo de Oferta/Campania que se desea, puede ser OFERTA o CAMPAniA.
     * @apiParam {String} nombre Nombre de la Oferta o Campania a crear.
     * @apiParam {String} descripcion Descripci\u00F3n de la Oferta o Campania a crear.
     * @apiParam {String} cantMaxPromocionales Valor m\u00E9ximo de pormocionales a brindar en la Campania. Este campo es innecesario si el Tipo de Oferta/Campania es OFERTA.
     * @apiParam {String} fechaDesde Fecha en formato dd/MM/aaaa desde la que aplica la Oferta o Campania.
     * @apiParam {String} fechaHasta Fecha en formato dd/MM/aaaa hasta la que aplica la Oferta o Campania.
     * @apiParam {String} ofertaCampaniaDet Arreglo con el listado de detalles a asociar con la Oferta o Campania.
     * @apiParam {String} ofertaCampaniaDet.idTipo Identificador del tipo de detalle a asociar.
     * @apiParam {String} ofertaCampaniaDet.tipo Tipo del detalle a asociar, puede ser PANEL o RUTA.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.prueba",
     *     "tipo": "CAMPAniA",
     *     "nombre": "Oferta 1",
     *     "descripcion": "Desc Oferta 1",
     *     "cantMaxPromocionales": "5",
     *     "fechaDesde": "30/10/2015",
     *     "fechaHasta": "30/11/2015",
     *     "ofertaCampaniaDet": [
     *         {
     *             "idTipo": "1",
     *             "tipo": "PANEL"
     *         },
     *         {
     *             "idTipo": "2",
     *             "tipo": "PANEL"
     *         }
     *     ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "idOfertaCampania": "1",
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionOfertaCampania",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos: Fecha Desde debe ser igual o mayor a la Fecha Actual.",
     *     "clase": "CtrlOfertaCampania",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputOfertaCampania doPostOfertaCampania(@Context HttpServletRequest req, InputOfertaCampania input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputOfertaCampania response = new OfertaCampania().crearOfertaCampania(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modofertacampania")
    /**
     * @api {POST} /opsidra/modofertacampania/ [modofertacampania]
     *
     * @apiName modofertacampania
     * @apiDescription Servicio para modificar Ofertas o Campanias creadas.
     * @apiGroup OfertaCampania
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} idOfertaCampania Identificador de la Oferta o Campania que se desea modificar.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} tipo Nombre del tipo de Oferta/Campania que se desea, puede ser OFERTA o CAMPAniA.
     * @apiParam {String} nombre Nombre de la Oferta o Campania a modificar.
     * @apiParam {String} descripcion Descripci\u00F3n de la Oferta o Campania a modificar.
     * @apiParam {String} cantMaxPromocionales Valor m\u00E9ximo de pormocionales a brindar en la Campania. Este campo es innecesario si el Tipo de Oferta/Campania es OFERTA.
     * @apiParam {String} fechaDesde Fecha en formato dd/MM/aaaa desde la que aplica la Oferta o Campania.
     * @apiParam {String} fechaHasta Fecha en formato dd/MM/aaaa hasta la que aplica la Oferta o Campania.
     * @apiParam {String} estado Estado de la Oferta o Campania.
     * @apiParam {String} ofertaCampaniaDet Arreglo con el listado de detalles a asociar con la Oferta o Campania.
     * @apiParam {String} ofertaCampaniaDet.idTipo Identificador del tipo de detalle a asociar.
     * @apiParam {String} ofertaCampaniaDet.tipo Tipo del detalle a asociar, puede ser PANEL o RUTA.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.prueba",
     *     "idOfertaCampania": "1",
     *     "tipo": "CAMPAniA",
     *     "nombre": "Oferta 1",
     *     "descripcion": "Desc Oferta 1",
     *     "cantMaxPromocionales": "5",
     *     "fechaDesde": "30/10/2015",
     *     "fechaHasta": "30/11/2015",
     *     "estado": "ALTA",
     *     "OfertaCampaniaDet": [
     *         {
     *             "idTipo": "1",
     *             "tipo": "PANEL"
     *         },
     *         {
     *             "idTipo": "2",
     *             "tipo": "PANEL"
     *         }
     *     ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "201",
     *     "mostrar": "1",
     *     "descripcion": "Recurso modificado exitosamente.",
     *     "clase": "OperacionOfertaCampania",
     *     "metodo": "doPutDel",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos:  Estado.",
     *     "clase": "CtrlOfertaCampania",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputOfertaCampania doPutOfertaCampania(@Context HttpServletRequest req, InputOfertaCampania input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputOfertaCampania response = new OfertaCampania().modOfertaCampania(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios OfertaCampania -----------------------------//
    
    //----------------------------------- Servicios PromoOfertaCampania ------------------------//
    @Path("/asignapromocampania")
    /**
     * @api {POST} /opsidra/asignapromocampania/ [asignapromocampania]
     * 
     * @apiName asignapromocampania
     * @apiDescription Servicio para asignar Art\u00EDculos Promocionales a Campanias creadas.
     * @apiGroup OfertaCampania
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idOfertaCampania Identificador de la Campania a la que se asociar\u00E9n los art\u00EDculos.
     * @apiParam {String} ofertaCampaniaDet Arreglo con el listado de art\u00EDculos a asociar con la Oferta o Campania.
     * @apiParam {String} ofertaCampaniaDet.idArtPromocional Identificador del art\u00EDculo promocional a asociar.
     * @apiParam {String} ofertaCampaniaDet.cantArticulos Cantidad de art\u00EDculos promocionales a otorgar con la Campania.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.prueba",
     *     "idOfertaCampania": "1",
     *     "ofertaCampaniaDet": [
     *         {
     *             "idArtPromocional": "1",
     *             "cantArticulos": "2"
     *         },
     *         {
     *             "idArtPromocional": "2",
     *             "cantArticulos": "1"
     *         }
     *     ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "idPromoOfertaCampania": "1",
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionPromoOfertaCampania",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos:  Datos no num\u00E9ricos en ID OfertaCampania.",
     *     "clase": "CtrlPromoOfertaCampania",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputPromoOfertaCampania doPostPromoCampania(@Context HttpServletRequest req, InputPromoOfertaCampania input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputPromoOfertaCampania response = new PromoOfertaCampania().asignarPromoCampania(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios PromoOfertaCampania ------------------------//
    
    //----------------------------------- Servicios Condicion ----------------------------------//
    @Path("/creacondicion")
    /**
     * @api {POST} /opsidra/creacondicion/ [creacondicion]
     * 
     * @apiName creacondicion
     * @apiDescription Servicio para crear Condiciones a Campanias por pa\u00EDs.
     * @apiGroup Condicion
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idOfertaCampania Identificador de la Campania a la que se asociar\u00E9n las condiciones.
     * @apiParam {String} condiciones Arreglo con el listado de Condiciones a asociar con la Campania.
     * @apiParam {String} nombre Nombre de la Condici\u00F3n a crear.
     * @apiParam {String} tipoGestion Tipo de gesti\u00F3n de la Condici\u00F3n (Permitidos \u00FAnicamente los valores configurados en la aplicaci\u00F3n).
     * @apiParam {String} tipoCondicion Tipo de la Condici\u00F3n (Permitidos \u00FAnicamente los valores configurados en la aplicaci\u00F3n).
     * @apiParam {String} detalle Arreglo con el listado de detalles a asociar con la Condici\u00F3n.
     * @apiParam {String} tipo Tipo al que se le aplica la Condici\u00F3n.
     * @apiParam {String} idArticulo Identificador del art\u00EDculo al que se le aplica la Condici\u00F3n.
     * @apiParam {String} montoInicial Monto inicial de la condic\u00ED\u00F3n.
     * @apiParam {String} montoFinal Monto final de la condic\u00ED\u00F3n.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "usuario": "usuario.pruebas",
     *     "codArea": "505",
     *     "idOfertaCampania": "3",
     *     "condiciones":
     *     [
     *         {
     *             "nombre": "Nombre",
     *             "tipoGestion": "VENTA",
     *             "tipoCondicion": "GENERICO",
     *             "detalle":
     *             [
     *                 {
     *                     "tipo": "tipo 1",
     *                     "idArticulo": "",
     *                     "montoInicial": "1",
     *                     "montoFinal": "2"
     *                 },
     *                 {
     *                     "tipo": "",
     *                     "idArticulo": "3",
     *                     "montoInicial": "7",
     *                     "montoFinal": ""
     *                 }
     *             ]
     *         },
     *         {
     *             "nombre": "Nombre 2",
     *             "tipoGestion": "ALTA_PREPAGO",
     *             "tipoCondicion": "ARTICULO",
     *             "detalle":
     *             [
     *                 {
     *                     "tipo": "",
     *                     "idArticulo": "1",
     *                     "montoInicial": "21",
     *                     "montoFinal": ""
     *                 }
     *             ]
     *         }
     *     ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionCondicion",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-412",
     *     "mostrar": "1",
     *     "descripcion": "Ya existe una condici\u00F3n con el nombre enviado. En la condici\u00F3n 1.",
     *     "clase": "OperacionCondicion",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputCondicion doPostCondicion(@Context HttpServletRequest req, InputCondicionPrincipal input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputCondicion response = new Condicion().crearCondicion(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/bajacondicion")
    /**
     * @api {POST} /opsidra/bajacondicion/ [delCondicion]
     *
     * @apiName delcondicion
     * @apiDescription Servicio para dar de baja condiciones creadas.
     * @apiGroup Condicion
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idCondicion Identificador de la Condici\u00F3n que se desea borrar.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.prueba",
     *     "idCondicion": "1"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "201",
     *     "mostrar": "1",
     *     "descripcion": "Recurso modificado exitosamente.",
     *     "clase": "OperacionCondicion",
     *     "metodo": "doDel",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-201",
     *     "mostrar": "1",
     *     "descripcion": "No existe el recurso deseado.",
     *     "clase": "OperacionCondicion",
     *     "metodo": "doDel",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputCondicion doDelCondicion(@Context HttpServletRequest req, InputCondicionPrincipal input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputCondicion response = new Condicion().modCondicion(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios Condicion ----------------------------------//
    
    //----------------------------------- Servicios CondicionOferta ----------------------------//
    @Path("/creacondicionoferta")
    /**
     * @api {POST} /opsidra/creacondicionoferta/ [creacondicionoferta]
     * 
     * @apiName creacondicionoferta
     * @apiDescription Servicio para crear Condiciones a Ofertas por pa\u00EDs.
     * @apiGroup CondicionOferta
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idOfertaCampania Identificador de la Campania a la que se asociar\u00E9n las condiciones.
     * @apiParam {String} condiciones Arreglo con el listado de condiciones a asociar con la Campania.
     * @apiParam {String} condiciones.nombre Nombre de la Condici\u00F3n a crear.
     * @apiParam {String} condiciones.tipoGestion="ALTA_PREPAGO o VENTA" Tipo de gesti\u00F3n de la Condici\u00F3n.
     * @apiParam {String} condiciones.tipoCondicionOferta="VENTA, ARTICULO o PDV" Tipo de la Condici\u00F3n.
     * @apiParam {String} condiciones.detalle Arreglo con el listado de detalles a asociar con la Condici\u00F3n.
     * @apiParam {String} condiciones.detalle.tipo="VENTA, ARTICULO, PDV, ZONA o TECNOLOGIA" Tipo al que se le aplica la Condici\u00F3n.
     * @apiParam {String} [condiciones.detalle.idArticulo] Identificador del art\u00EDculo al que se le aplica la Condici\u00F3n.
     * @apiParam {String} [condiciones.detalle.tipoCliente] Tipo de cliente al que se le aplica la Condici\u00F3n.
     * @apiParam {String} [condiciones.detalle.tecnologia] Tipo de tecnolog\u00EDa a la que se le aplica la Condici\u00F3n.
     * @apiParam {String} [condiciones.detalle.montoInicial] Monto inicial de la condic\u00ED\u00F3n.
     * @apiParam {String} [condiciones.detalle.montoFinal] Monto final de la condic\u00ED\u00F3n.
     * @apiParam {String} [condiciones.detalle.idPDV] Identificador del PDV al que aplica la condici\u00F3n.
     * @apiParam {String} [condiciones.detalle.zonaComercialPDV] Zona del PDV al que aplica la condici\u00F3n.
     * @apiParam {String} [condiciones.detalle.categoriaPDV] Categoria del PDV al que aplica la condici\u00F3n.
     * @apiParam {String} [condiciones.detalle.idArticuloRegalo] Identificador del art\u00EDculo que se regalar\u00E9 con la condici\u00F3n.
     * @apiParam {String} [condiciones.detalle.cantidadArticuloRegalo] Cantidad de art\u00EDculos que se regalar\u00E9n con la condici\u00F3n.
     * @apiParam {String} [condiciones.detalle.tipoDescuento="MONTO o PORCENTAJE"] Tipo de descuento a aplicar.
     * @apiParam {String} [condiciones.detalle.valorDescuento] Valor del descuento a aplicar.
     * @apiParam {String} [condiciones.detalle.idDescuento] id que identifica el tipo de Descuento FS.
     * @apiParam {String} [condiciones.detalle.idProductOffering] id del producto ofertado de FS.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "usuario": "usuario.pruebas",
     *     "codArea": "505",
     *     "idOfertaCampania": "9",
     *     "condiciones":
     *     [
     *         {
     *             "nombre": "Nombre 1",
     *             "tipoGestion": "VENTA",
     *             "tipoCondicion": "ARTICULO",
     *             "detalle":
     *             [
     *                 {
     *                     "tipo": "PDV",
     *                     "idArticulo": "",
     *                     "tipoCliente": "",
     *                     "tecnologia": "",
     *                     "montoInicial": "",
     *                     "montoFinal": "",
     *                     "idPDV": "",
     *                     "zonaComercialPDV": "Zona 1",
     *                     "categoriaPDV": "A",
     *                     "tipoDescuento": "MONTO",
     *                     "valorDescuento": "5",
     *                     "idArticuloRegalo": "",
     *                     "cantidadArticuloRegalo": "",
     *                     "idDescuento": "35",
     *                     "idProductOffering":"654646545665"
     *                 },
     *                 {
     *                     "tipo": "ZONA",
     *                     "idArticulo": "",
     *                     "tipoCliente": "",
     *                     "tecnologia": "",
     *                     "montoInicial": "",
     *                     "montoFinal": "",
     *                     "idPDV": "55",
     *                     "zonaComercialPDV": "",
     *                     "categoriaPDV": "",
     *                     "tipoDescuento": "PORCENTAJE",
     *                     "valorDescuento": "10",
     *                     "idArticuloRegalo": "",
     *                     "cantidadArticuloRegalo": "",
     *                     "idDescuento": "35",
     *                     "idProductOffering":"654646545665"
     *                 }
     *             ]
     *         },
     *         {
     *             "nombre": "Nombre 2",
     *             "tipoGestion": "ALTA_PREPAGO",
     *             "tipoCondicion": "GENERICO",
     *             "detalle":
     *             [
     *                 {
     *                     "tipo": "VENTA",
     *                     "idArticulo": "1",
     *                     "tipoCliente": "CF",
     *                     "tecnologia": "",
     *                     "montoInicial": "1",
     *                     "montoFinal": "",
     *                     "idPDV": "",
     *                     "zonaComercialPDV": "",
     *                     "categoriaPDV": "",
     *                     "tipoDescuento": "MONTO",
     *                     "valorDescuento": "1",
     *                     "idArticuloRegalo": "",
     *                     "cantidadArticuloRegalo": "",
     *                     "idDescuento": "35",
     *                     "idProductOffering":"654646545665"
     *                 },
     *                 {
     *                     "tipo": "VENTA",
     *                     "idArticulo": "",
     *                     "tipoCliente": "",
     *                     "tecnologia": "",
     *                     "montoInicial": "2",
     *                     "montoFinal": "2",
     *                     "idPDV": "",
     *                     "zonaComercialPDV": "",
     *                     "categoriaPDV": "",
     *                     "tipoDescuento": "MONTO",
     *                     "valorDescuento": "1",
     *                     "idArticuloRegalo": "",
     *                     "cantidadArticuloRegalo": ""
     *                 }
     *             ]
     *         },
     *         {
     *             "nombre": "Nombre 3",
     *             "tipoGestion": "VENTA",
     *             "tipoCondicion": "ARTICULO",
     *             "detalle":
     *             [
     *                 {
     *                     "tipo": "TECNOLOGIA",
     *                     "idArticulo": "",
     *                     "tipoCliente": "",
     *                     "tecnologia": "3G",
     *                     "montoInicial": "",
     *                     "montoFinal": "",
     *                     "idPDV": "",
     *                     "zonaComercialPDV": "",
     *                     "categoriaPDV": "",
     *                     "tipoDescuento": "PORCENTAJE",
     *                     "valorDescuento": "5",
     *                     "idDescuento": "35",
     *                     "idProductOffering":"654646545665"
     *                 }
     *             ]
     *         }
     *     ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionCondicionOferta",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-412",
     *     "mostrar": "1",
     *     "descripcion": "Ya existe una condici\u00F3n con el nombre enviado. En la condici\u00F3n 1.",
     *     "clase": "OperacionCondicionOferta",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputCondicionOferta doPostCondicionOferta(@Context HttpServletRequest req, InputCondicionPrincipalOferta input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputCondicionOferta response = new CondicionOferta().crearCondicion(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/bajacondicionoferta")
    /**
     * @api {POST} /opsidra/bajacondicionoferta/ [delCondicionOferta]
     *
     * @apiName bajacondicionoferta
     * @apiDescription Servicio para dar de baja condiciones creadas.
     * @apiGroup CondicionOferta
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idCondicionOferta Identificador de la Condici\u00F3n que se desea borrar.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.prueba",
     *     "idCondicionOferta": "1"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "201",
     *     "mostrar": "1",
     *     "descripcion": "Recurso modificado exitosamente.",
     *     "clase": "OperacionCondicionOferta",
     *     "metodo": "doDel",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-201",
     *     "mostrar": "1",
     *     "descripcion": "No existe el recurso deseado.",
     *     "clase": "OperacionCondicionOferta",
     *     "metodo": "doDel",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST 
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputCondicionOferta doDelCondicionOferta(@Context HttpServletRequest req, InputCondicionPrincipalOferta input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputCondicionOferta response = new CondicionOferta().modCondicion(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios CondicionOferta ----------------------------//


    //----------------------------------- Servicios AdjuntoGestion -----------------------------//
    @Path("/cargaradjuntogestion")
    /**
     * @api {POST} /opsidra/cargaradjuntogestion/ [cargaradjuntogestion]
     * 
     * @apiName cargaradjuntogestion
     * @apiDescription Servicio para realizar cargas de archivos adjuntos a gestiones.
     * @apiGroup AdjuntoGestion
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} token token de validaci\u00F3n de usuario.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} idGestion Identificador de la gestion a la que se asociar\u00E9 el archivo.
     * @apiParam {String} gestion Nombre del tipo de gesti\u00F3n a la que pertenece el ID del archivo adjunto.
     * @apiParam {String} adjunto String codificado en base64 de la imagen a adjuntar.
     * @apiParam {String} nombreArchivo Nombre del archivo adjunto.
     * @apiParam {String} tipoArchivo Tipo de archivo a adjuntar.
     * @apiParam {String} extension Extensi\u00F3n del archivo a adjuntar.
     * @apiParam {String} tipoDocumento Tipo del documento a adjuntar.
     * @apiParam {String} idPortaMovil Id de portabilidad m\u00F3vil si el adjunto pertenece a portabilidad.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
     *     "usuario": "usuario.pruebas",
     *     "codArea": "505",
     *     "token": "WEB",
     *     "idGestion": "1",
     *     "gestion": "REMESA",
     *     "adjunto": "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkG....",
     *     "nombreArchivo": "Adjunto 1",
     *     "tipoArchivo": "Imagen",
     *     "extension": "JPG",
     *     "tipoDocumento": "DPI",
     *     "idPortaMovil": ""
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "idAdjunto": "1",
     *   "respuesta": {
     *     "codResultado": "64",
     *     "mostrar": "1",
     *     "descripcion": "OK. Se registr\u00F3 el archivo adjunto correctamente.",
     *     "clase": "CtrlAdjuntoGestion",
     *     "metodo": "cargarAdjunto",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos:  Tipo Documento no corresponde a ninguno de los tipos definidos.",
     *     "clase": "CtrlAdjuntoGestion",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputAdjuntoGestion doPostAdjuntoGestion(@Context HttpServletRequest req, InputAdjuntoGestion input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputAdjuntoGestion response = new AdjuntoGestion().cargarAdjunto(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios AdjuntoGestion -----------------------------//
    
    /***************************GESTION VISITA***************************/
    @Path("/registravisita")
    /**
     *@api {POST} /opsidra/registravisita/ [registravisita]
     * 
     * @apiName registravisita
     * @apiDescription Servicio para registrar las visitas realizadas por el vendedor a un punto de venta en el sistema de SIDRA..
     * @apiGroup GESTION_VISITA
     * @apiVersion 1.0.0
     * @apiParam {String} token token de validaci\u00F3n de usuario.
     * @apiParam {String} codArea c\u00F3digo del pa\u00EDs de donde se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} idVendedor id del vendedor que realiza la visita. 
     * @apiParam {String} idJornada id la jornada activa en la que se realiza la visita. 
     * @apiParam {String} fecha fecha en formato YYYYmmddHHMMss, que se registra al momento de realizar la visita. 
     * @apiParam {String} idPuntoVenta id del punto de venta que se visita.
     * @apiParam {String} latitud datos de ubicaci\u00F3n del pdv.
     * @apiParam {String} longitud datos de ubicaci\u00F3n del pdv.
     * @apiParam {String} gestion indica si se realizo venta o no durante la visita.
     * @apiParam {String} folio id de venta si se realiz\u00F3 venta.
     * @apiParam {String} observaciones comentarios de la visita realizada.
     * @apiParam {String} causa Causa de la gesti\u00F3n de NO VENTA.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
    * {
    *    "token":"DSFSDF",
    *    "codArea":"505",
    *    "usuario": "usuario",
    *    "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
    *    "idVendedor":"11",
    *    "idJornada":"11",
    *    "fecha":"YYYYmmddHHMMss",
    *    "idPuntoVenta":"12",
    *    "latitud":"",
    *    "longitud":"",
    *    "gestion":"",
    *    "folio":"",
    *    "causa": "No se encotr\u00F3 abierto.",
    *    "observaciones":""
    * }
    * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
    *    {
    *       "token":"dsfsddfsfdds",
    *         "respuesta": {
    *            "codResultado": "17",
    *            "mostrar": "1",
    *            "descripcion": "OK. Registro visita correctamente ",
    *            "clase": " ",
    *            "metodo": " ",
    *            "excepcion": " ",
    *            "tipoExepcion": ""
    *        }
    *    }
    * 
    * @apiErrorExample {json} Respuesta-Error:     
    *    {
    *        "codResultado": "-93",
    *        "mostrar": "1",
    *        "descripcion": "La Jornada ingresada no existe o no es v\u00E9lida para la fecha actual. ",
    *        "clase": "CtrlVisitaGestion",
    *        "metodo": "registraVisita",
    *        "excepcion": " ",
    *        "tipoExepcion": "Generales"
    *    }
    * 
    * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputVisita registraVisita(@Context HttpServletRequest req, InputVisita input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputVisita response = new VisitaGestion().registraVisita(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);
        return response;
    }

    /******************************************************************************************************************
     *                                         SERVICIO VENTA RUTA
     ******************************************************************************************************************/
    @Path("/creaventaruta")
    /**
     * @api {POST} /opsidra/creaventaruta/ [creaventaruta]
     *
     * @apiName creaventaruta
     * @apiDescription Servicio para crear ventas de rutas.
     * @apiGroup VENTAS
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} token Token de validaci\u00F3n de autenticaci\u00F3n de usuario.
     * @apiParam {String} idVentaMovil Id generado por el m\u00F3vil antes de sincronizar la venta.
     * @apiParam {String} idVendedor Id del vendedor que realiza la venta.
     * @apiParam {String} idJornada Id de la jornada en la que se realiza la venta.
     * @apiParam {String} idBodegaVendedor Id bodega de donde obtiene el inventario el vendedor.
     * @apiParam {String} fecha Fecha en la que se realizo la venta, formato de fecha: YYYYmmddHHMMss.
     * @apiParam {String} folioManual="1 o 0" Indica si se maneja folios manuales o autom\u00E9ticos, 1 = Folio Manual, 0 = Folio Autom\u00E9tico.
     * @apiParam {String} [idRangoFolio] Id del registro del rango de folios. Opcional seg\u00FAn el valor de folioManual.
     * @apiParam {String} folio N\u00FAmero de folio o factura generado por la app movil.
     * @apiParam {String} [serie] Serie de factura. Opcional seg\u00FAn el valor de folioManual.
     * @apiParam {String} tipoDocumento Tipo de documento que se esta generando para la venta (TICKET, FACTURA, CCF, FCF).
     * @apiParam {String} idTipo Id del cliente.
     * @apiParam {String} tipo Indica el tipo de cliente de la venta es punto de venta o cliente final (PDV o CF).
     * @apiParam {String} [nit] Nit del cliente. Es obligatorio solo para documento ccf.
     * @apiParam {String} [giro] Este campo solo es utilizado para ccf.
     * @apiParam {String} [registroFiscal] este campo solo es utilizado para ccf.
     * @apiParam {String} nombre Primer nombre del cliente.
     * @apiParam {String} [segundoNombre] Segundo nombre del cliente.
     * @apiParam {String} apellido Primer apellido del cliente.
     * @apiParam {String} [segundoApellido] Segundo apellido del cliente.
     * @apiParam {String} direccion Direcci\u00F3n del cliente.
     * @apiParam {String} numTelefono N\u00FAmero de telefono del cliente.
     * @apiParam {String} tipoDocCliente Tipo de documento de identificacion del cliente.
     * @apiParam {String} numDocCliente N\u00FAmero del documento de identificacion del cliente.
     * @apiParam {String} exento Indica si el cliente esta exento de impuestos o no.
     * @apiParam {String} impuestosExento Listado de impuestos exentos.
     * @apiParam {String} impuestosExento.nombreImpuesto Nombre del impuesto exento.
     * @apiParam {String} idOfertaCampania Identificador de la oferta que origin\u00F3 el descuento por monto de venta.
     * @apiParam {String} descuentoMontoVenta Total de descuentos realizados sobre el monto de venta.
     * @apiParam {String} montoFactura Monto total de la venta realizada.
     * @apiParam {String} montoPagado Cantidad cancelada por el cliente.
     * @apiParam {String} modoOnline="1 o 0" Par\u00E9metro que indica si la venta fue online u offline (1 = Online, 0 = Offline).
     * @apiParam {String} detallePago Detalle de pago de la venta.
     * @apiParam {String} detallePago.formaPago Nombre de la forma de pago para el detallePago.
     * @apiParam {String} detallePago.monto Monto de la factura que se cancelo con esa forma de pago.
     * @apiParam {String} [detallePago.banco] N\u00FAmero de autorizaci\u00F3n generado si el pago es con tarjeta.
     * @apiParam {String} [detallePago.numAutorizacion] N\u00FAmero de autorizaci\u00F3n generado si el pago es con tarjeta.
     * @apiParam {String} [detallePago.marcaTarjeta] Marca de la tarjeta en caso de pagarse con una.
     * @apiParam {String} [detallePago.digitosTarjeta] \u00FAltimos 4 digitos de la tarjeta en caso de pagarse con una.
     * @apiParam {String} [detallePago.numeroCheque] N\u00FAmero de cheque si el pago es con el mismo.
     * @apiParam {String} [detallePago.fechaEmision] Fecha de emision del cheque en caso de pagarse con uno, formato ddMMyyyy.
     * @apiParam {String} [detallePago.numeroReserva] N\u00FAmero de reserva en caso de pagarse con cheque.
     * @apiParam {String} [detallePago.cuentaCliente] Cuenta del cliente en caso de pagarse con cheque.
     * @apiParam {String} articulos Detalle de art\u00EDculos a vender.
     * @apiParam {String} articulos.articulo Id del art\u00EDculo del que se esta realizando la venta.
     * @apiParam {String} articulos.cantidad Cantidad de art\u00EDculos que se esta vendiendo.
     * @apiParam {String} articulos.seriado Indica si el art\u00EDculo a vender tiene serie o no, valores: 1: Tiene serie. 0: No tiene serie
     * @apiParam {String} articulos.rango Indica si estoy vendiendo un rango de series o solo una serie, este aplica para las tarjetas rasca. Valores:1: Tiene rango de series. 0: No tiene rango de series .
     * @apiParam {String} articulos.serieInicial Indica de donde inicia la serie en caso de ser rango, ahora si solo es una serie se agrega la serie a vender.
     * @apiParam {String} [articulos.serieFinal] Indica donde finaliza el rango de series a vender en caso de ser tarjetas rasca, cuando no es rango de series este campo va vac\u00EDo.
     * @apiParam {String} articulos.serieAsociada Serie en caso de que una terminal tenga un chip o simcard asociado.
     * @apiParam {String} articulos.numTelefono N\u00FAmero de tel\u00E9fono en caso de ser un simcard.
     * @apiParam {String} articulos.tipoInv="INV_TELCA" Indica el tipo de inventario al que pertenece el art\u00EDculo, para este caso es: INV_TELCA.
     * @apiParam {String} articulos.precio Precio del art\u00EDculo, este precio es el que se obtiene de scl o vantive.
     * @apiParam {String} articulos.descuentoSCL Valor de descuento que tiene configurado un art\u00EDculo en scl.
     * @apiParam {String} articulos.descuentoSidra Descuento que puede tener un art\u00EDculo para sidra.
     * @apiParam {String} articulos.idOfertaCampania Id de la oferta de la que se obtuvo el descuento de sidra.
     * @apiParam {String} [detalleDescuentosSidra] Detalle de descuentos del art\u00EDculo de venta.
     * @apiParam {String} detalleDescuentosSidra.descuento Valor del descuento a aplicar al art\u00EDculo.
     * @apiParam {String} detalleDescuentosSidra.idOfertaCampania Id de la campania a la que pertenece la oferta del art\u00EDculo.
     * @apiParam {String} [detalleDescuentosSidra.idCondicion] Id de la condici\u00F3n por la que se aplic\u00F3 el descuento. Solo necesario en caso de ser oferta de tipo PAGUE_LLEVE.
     * @apiParam {String} detalleDescuentosSidra.tipoDescuento Tipo del descuento que se aplica en la oferta.
     * @apiParam {String} articulos.gestion Nombre de la gesti\u00F3n a realizar en la venta.
     * @apiParam {String} articulos.impuesto Impuestos calculados al art\u00EDculo.
     * @apiParam {String} articulos.precioTotal Precio final con impuestos y descuentos.
     * @apiParam {String} articulos.modalidad Este campo aplica unicamente para el caso de recargas, indica si la recarga se hizo desde sidra o fuera del sistema de sidra.          * 
     * @apiParam {String} articulos.impuestosArticulo Detalle de impuestos por art\u00EDculo a vender.
     * @apiParam {String} articulos.impuestosArticulo.nombreImpuesto Nombre de un impuesto espec\u00EDfico calculado al art\u00EDculo.
     * @apiParam {String} articulos.impuestosArticulo.valor Valor total del impuesto espec\u00EDfico que se suma al precio del art\u00EDculo.
     * @apiParam {String} [articulosPromocionales] Detalle de art\u00EDculos promocionales a regalar en la venta.
     * @apiParam {String} articulosPromocionales.idOfertaCampania Id de la campania a la que pertenece el art\u00EDculo promocional.
     * @apiParam {String} articulosPromocionales.articuloPromocional Id del art\u00EDculo promocional si la venta aplica a regalar art\u00EDculos promocionales.
     * @apiParam {String} articulosPromocionales.cantidad Cantidad de art\u00EDculos promocionales a regalar.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *    "usuario": "usuario.pruebas",
     *    "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
     *    "codArea": "505",
     *    "token": "865454545",
     *    "idVentaMovil": "1",
     *    "idVendedor": "1",
     *    "idJornada": "1",
     *    "idBodegaVendedor": "1",
     *    "fecha": "YYYYmmddHHMMss",
     *    "folioManual": "1",
     *    "idRangoFolio": "3",
     *    "folio": "11221",
     *    "serie": "a",
     *    "tipoDocumento": "ds",
     *    "idTipo": "1",
     *    "tipo": "PDV/CF",
     *    "nit": "456445",
     *    "registroFiscal": "456445",
     *    "giro": "456445",
     *    
     *    "nombre": "Juan",
     *    "segundoNombre": "",
     *    "apellido": "D\u00EDaz",
     *    "segundoApellido": "",
     *    "direccion": "Nicaragua",
     *    "numTelefono": "12345678",
     *    "tipoDocCliente": "Pasaporte",
     *    "numDocCliente": "1821832151201",
     *        
     *    "exento": "NO EXENTO",
     *    "impuestosExento": [{
     *         "nombreImpuesto": "ISC"
     *     }],
     *    
     *    "idOfertaCampania": "",
     *    "descuentoMontoVenta": "",
     *    "montoFactura": "",
     *    "montoPagado": "",
     *    
     *    "detallePago": [{
     *        "formaPago": "Tarjeta",
     *        "monto": "50.181234",
     *        "banco": "BANCO",
     *        "numAutorizacion": "118588151358513",
     *        "marcaTarjeta": "VISA",
     *        "digitosTarjeta": "4241",
     *        "numeroCheque": "",
     *        "fechaEmision": "",
     *        "numeroReserva": "",
     *        "cuentaCliente": ""
     *    }, {
     *        "formaPago": "Efectivo",
     *        "monto": "25.01234",
     *        "banco": "",
     *        "numAutorizacion": "",
     *        "marcaTarjeta": "",
     *        "digitosTarjeta": "",
     *        "numeroCheque": "",
     *        "fechaEmision": "",
     *        "numeroReserva": "",
     *        "cuentaCliente": ""
     *    }, {
     *        "formaPago": "Tarjeta",
     *        "monto": "50.181234",
     *        "banco": "BANCO",
     *        "numAutorizacion": "",
     *        "marcaTarjeta": "",
     *        "digitosTarjeta": "",
     *        "numeroCheque": "124",
     *        "fechaEmision": "10022017",
     *        "numeroReserva": "245",
     *        "cuentaCliente": "12896742-553-1"
     *    }],
     *
     *    "articulos": [{
     *        "articulo": "1",
     *        "cantidad": "1",
     *        "seriado": "1",
     *        "rango": "0",
     *        "serieInicial": "232",
     *        "serieFinal": "",
     *        "serieAsociada": "2332",
     *        "numTelefono": "74552332",
     *        "tipoInv": "INV_TELCA",
     *        "precio": "55",
     *        "descuentoSCL": "4",
     *        "descuentoSidra": "4",
     *        "detalleDescuentosSidra": [
     *          {
     *             "descuento": "0.6",
     *             "idOfertaCampania": "11",
     *             "idCondicion": "",
     *             "tipoDescuento": "ARTICULO"
     *          },{
     *             "descuento": "0.1",
     *             "idOfertaCampania": "11",
     *             "idCondicion": "",
     *             "tipoDescuento": "TECNOLOGIA"
     *          }
     *        ],
     *        "gestion": "ALTA PREPAGO",
     *        "impuesto": "",
     *        "precioTotal": "",
     *        "modalidad": "",
     *        "impuestosArticulo": [
     *            {
     *                "nombreImpuesto": "",
     *                "valor": ""
     *            }
     *        ]
     *    }],
     *    
     *    "articulosPromocionales": [{
     *        "idOfertaCampania": "",
     *        "articuloPromocional": "",
     *        "cantidad": ""
     *    }, {
     *        "idOfertaCampania": "",
     *        "articuloPromocional": "",
     *        "cantidad": ""
     *    }]
     * }
     * 
     * @apiSuccessExample  {} JSON Respuesta-\u00E9xito:
     * {
     *   "token": "WEB",
     *   "respuesta": {
     *     "codResultado": "18",
     *     "mostrar": "1",
     *     "descripcion": "Ok.Venta registrada correctamente.",
     *     "clase": "",
     *     "metodo": "",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "token": "WEB",
     *   "respuesta": {
     *     "codResultado": "-92",
     *     "mostrar": "1",
     *     "descripcion": "La Jornada  no esta asociada al vendedor ingresado.",
     *     "clase": "CtrlVentaRuta",
     *     "metodo": "creaVentaRuta",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputVenta creaVentaRuta(@Context HttpServletRequest req, InputVenta input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputVenta response = new VentaRuta().creaVentaRuta(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);
        String jsonRequest = new GsonBuilder().setPrettyPrinting().create().toJson(input);
		String jsonResponse = new GsonBuilder().setPrettyPrinting().create().toJson(response);
		System.out.println("REQUEST: "+jsonRequest);
		System.out.println("RESPONSE: "+jsonResponse);
        
        return response;
    }
    
    @Path("/reenvioSMSOLS")
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputReenvioSMS reenvioSMSOLS(@Context HttpServletRequest req,InputReenvioSMS input)  {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        CtrlReEnvioSMS recurso = new CtrlReEnvioSMS();
        OutputReenvioSMS response = recurso.reenvioSMS(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);
        String jsonRequest = new GsonBuilder().setPrettyPrinting().create().toJson(input);
		String jsonResponse = new GsonBuilder().setPrettyPrinting().create().toJson(response);
		System.out.println("REQUEST: "+jsonRequest);
		System.out.println("RESPONSE: "+jsonResponse);
        return response;
    }

    /*************************************************************************/
    /*************************** SERVICIOS RELEASE 5 *************************/
    /*************************************************************************/
    //----------------------------------- Servicios Cuentas Bancarias --------------------------//
    @Path("/crearCuenta")
    /**
     * @api {POST} /opsidra/crearCuenta/ [crearCuenta]
     * 
     * @apiName crearCuenta
     * @apiDescription Servicio para crear cuentas bancarias por pa\u00EDs.
     * @apiGroup Cuenta
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idDts Id del distribuidor al que se le crea la cuenta.
     * @apiParam {String} banco Nombre del banco a asociar con la cuenta a crear.
     * @apiParam {String} noCuenta N\u00FAmero de la cuenta bancaria a crear.
     * @apiParam {String} tipoCuenta Tipo de la cuenta crear.
     * @apiParam {String} nombreCuenta Nombre de la cuenta a crear.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea":"505",
     *     "usuario":"usuario.pruebas",
     *     "idDts":"1",
     *     "banco":"Banco General",
     *     "noCuenta":"46794697154",
     *     "tipoCuenta":"Monetaria",
     *     "nombreCuenta":"Cuenta de Ahorro BdO"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "idCuenta": "7",
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionCuenta",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-701",
     *     "mostrar": "1",
     *     "descripcion": "El par\u00E9metro de entrada \"banco\" esta vac\u00EDo.",
     *     "clase": "CtrlCuenta",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputCuenta doPostCuenta(@Context HttpServletRequest req, InputCuenta input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputCuenta response = new Cuenta().crearCuenta(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modCuenta")
    /**
     * @api {POST} /opsidra/modCuenta/ [modCuenta]
     *
     * @apiName modCuenta
     * @apiDescription Servicio para modificar cuentas bancarias creadas en Sidra.
     * @apiGroup Cuenta
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} [idCuenta] Identificador de la cuenta a modificar.
     * @apiParam {String} [banco] Nombre del banco asociado a la cuenta a modificar.
     * @apiParam {String} [noCuenta] N\u00FAmero de la cuenta bancaria a modificar.
     * @apiParam {String} [tipoCuenta] Tipo de la cuenta modificar.
     * @apiParam {String} [nombreCuenta] Nombre de la cuenta a modificar.
     * @apiParam {String} [estado="ALTA o BAJA"] Estado de la cuenta a modificar.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea":"505",
     *     "usuario":"usuario.pruebas",
     *     "idCuenta":"4",
     *     "banco":"Banco General",
     *     "noCuenta":"46794697164",
     *     "tipoCuenta":"Monetaria",
     *     "nombreCuenta":"Cuenta corriente",
     *     "estado":"ALTA"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "201",
     *     "mostrar": "1",
     *     "descripcion": "Recurso modificado exitosamente.",
     *     "clase": "OperacionCuenta",
     *     "metodo": "doPut",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-705",
     *     "mostrar": "1",
     *     "descripcion": "El tipo de cuenta enviada no corresponde a ninguna de las registradas en el sistema.",
     *     "clase": "CtrlCuenta",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputCuenta doPutCuenta(@Context HttpServletRequest req, InputCuenta input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputCuenta response = new Cuenta().modCuenta(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios Cuentas Bancarias --------------------------//
    
    //----------------------------------- Servicios Remesas ------------------------------------//
    @Path("/crearemesa")
    /**
     * @api {POST} /opsidra/crearemesa/ [crearemesa]
     * 
     * @apiName crearemesa
     * @apiDescription Servicio para crear remesas en Sidra.
     * @apiGroup Remesa
     * @apiVersion 1.0.0
     * @apiParam {String} token Token de autenticaci\u00F3n de sesion de usuario.
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} origen="MOVIL o PC" Origen de creaci\u00F3n de las remesas.
     * @apiParam {String} [idJornada] Identificador de la jornada a la que se asocia la remesa. Obligatorio en caso de tratarse de origen MOVIL.
     * @apiParam {String} [idDeuda] Identificador de la deuda a la que se asociar\u00E9n las remesas. Obligatorio en caso de tratarse de origen PC.
     * @apiParam {String} remesas Listado de remesas a crear.
     * @apiParam {String} [remesas.idRemesa] Identificador de la remesa creada en aplicaci\u00F3n m\u00F3vil. Opcional en caso de tratarse de origen PC.
     * @apiParam {String} [remesas.idCuenta] Identificador de la cuenta a la que se asocia la remesa. Opcional en caso de tratarse de origen PC.
     * @apiParam {String} [remesas.banco] Nombre del banco de la remesa. Obligatorio en caso de tratarse de origen PC.
     * @apiParam {String} remesas.monto Monto de la remesa crear.
     * @apiParam {String} remesas.noBoleta N\u00FAmero de boleta de la remesa a crear.
     * @apiParam {String} [remesas.tasaCambio] Tasa de cambio de la moneda del d\u00EDa. Obligatorio en caso de tratarse de origen MOVIL.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "token":"WEB",
     *     "codArea":"505",
     *     "usuario":"usuario.pruebas",
     *     "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
     *     "origen":"MOVIL",
     *     "idJornada":"14",
     *     "idDeuda":"",
     *     "remesas":[
     *         {
     *             "idRemesa":"89",
     *             "idCuenta":"4",
     *             "banco":"",
     *             "monto":"100",
     *             "noBoleta":"A1",
     *             "tasaCambio":"29.39"
     *         },
     *         {
     *             "idRemesa":"90",
     *             "idCuenta":"4",
     *             "banco":"",
     *             "monto":"1000",
     *             "noBoleta":"A2",
     *             "tasaCambio":"29.39"
     *         }
     *     ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "token": "WEB",
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionRemesa",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   },
     *   "datos": [
     *     {
     *       "idRemesa": "93",
     *       "idApp": "1"
     *     },
     *     {
     *       "idRemesa": "94",
     *       "idApp": "2"
     *     }
     *   ]
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "token":"WEB",
     *   "respuesta": {
     *     "codResultado": "-711",
     *     "mostrar": "1",
     *     "descripcion": "El par\u00E9metro de entrada \"monto\" esta vac\u00EDo o no es dato num\u00E9rico.",
     *     "clase": "CtrlRemesa",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputRemesa doPostRemesa(@Context HttpServletRequest req, InputRemesa input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputRemesa response = new Remesa().crearRemesa(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modremesa")
    /**
     * @api {POST} /opsidra/modremesa/ [modremesa]
     *
     * @apiName modremesa
     * @apiDescription Servicio para modificar remesas creadas en Sidra.
     * @apiGroup Remesa
     * @apiVersion 1.0.0
     * @apiParam {String} token Token de autenticaci\u00F3n de sesion de usuario.
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} [idRemesa] Identificador de la remesa a eliminar. Excluyente con idDeuda.
     * @apiParam {String} [idDeuda] Identificador de la solicitud de tipo DEUDA de la que se desea eliminar las remesas. Excluyente con idRemesa.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "token":"WEB",
     *     "codArea":"505",
     *     "usuario":"usuario.pruebas",
     *     "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
     *     "idRemesa":"5",
     *     "idDeuda":""
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "token":"WEB",
     *   "respuesta": {
     *     "codResultado": "201",
     *     "mostrar": "1",
     *     "descripcion": "Recurso modificado exitosamente.",
     *     "clase": "OperacionRemesa",
     *     "metodo": "doPut",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "token":"WEB",
     *   "respuesta": {
     *     "codResultado": "-710",
     *     "mostrar": "1",
     *     "descripcion": "El par\u00E9metro de entrada \"idRemesa\" esta vac\u00EDo o no es dato num\u00E9rico.",
     *     "clase": "CtrlRemesa",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputRemesa doPutRemesa(@Context HttpServletRequest req, InputRemesa input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputRemesa response = new Remesa().modRemesa(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios Remesas ------------------------------------//
 
    //----------------------------------- Servicios Jornada Masiva -----------------------------//
    @Path("/creajornadamasiva")
    /**
     * @api {POST} /opsidra/creajornadamasiva/ [creajornadamasiva]
     * 
     * @apiName creajornadamasiva
     * @apiDescription Servicio para crear Jornadas por pa\u00EDs.
     * @apiGroup Jornada
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de usuario.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} fecha Fecha de la jornada en formato yyyyMMddHHMISS.
     * @apiParam {String} tipo="RUTA o PANEL" Nombre del tipo asociado.
     * @apiParam {String} idDistribuidor Identificador del distribuidor a asociar.
     * @apiParam {String} idBodegaVendedor Identificador de la bodega del vendedor que ser\u00E9 asociada a la Jornada.
     * @apiParam {String} idResponsable Identificador del vendedor responsable de la panel o ruta a asociar.
     * @apiParam {String} codDispositivo C\u00F3digo del dispositivo a asociar.
     * @apiParam {String} [observaciones] Observaciones de la Jornada que se desea crear.
     * @apiParam {String} vendedores Listado de vendedores que iniciar\u00E9n la jornada.
     * @apiParam {String} vendedores.idVendedor Identificador del vendedor que iniciar\u00E9 jornada.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "token": "WEB",
     *     "usuario": "usuario.pruebas",
     *     "fecha": "20160714143000",
     *     "tipo": "PANEL",
     *     "idDistribuidor": "17",
     *     "idBodegaVendedor": "60",
     *     "idResponsable": "462",
     *     "codDispositivo": "ASDF123QWER456ZXCV789",
     *     "observaciones": "",
     *     "vendedores": [
     *         {
     *             "idVendedor": "2322"
     *         },
     *         {
     *             "idVendedor": "2016"
     *         }
     *     ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "token": "WEB",
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionJornadaMasiva",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     *   "jornadas": [
     *     {
     *       "idJornada": "28",
     *       "idVendedor": "462",
     *       "saldoInicial": "2000"
     *     },
     *     {
     *       "idJornada": "29",
     *       "idVendedor": "904",
     *       "saldoInicial": "2000"
     *     },
     *     {
     *       "idJornada": "30",
     *       "idVendedor": "1382",
     *       "saldoInicial": "2000"
     *     }
     *   ]
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-739",
     *     "mostrar": "1",
     *     "descripcion": "El responsable enviado no coincide con el responsable asignado a la panel.",
     *     "clase": "OperacionJornadaMasiva",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputJornada doPostJornadaMasiva(@Context HttpServletRequest req, InputJornada input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputJornada response = new JornadaMasiva().crearJornada(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/modjornadamasiva")
    /**
     * @api {POST} /opsidra/modjornadamasiva/ [modjornadamasiva]
     * 
     * @apiName modjornadamasiva
     * @apiDescription Servicio para modificar Jornadas por pa\u00EDs.
     * @apiGroup Jornada
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de usuario.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idDistribuidor Identificador del distribuidor asociado.
     * @apiParam {String} idResponsable Identificador del vendedor responsable de la panel o ruta.
     * @apiParam {String} idJornada Identificador de la jornada a modificar.
     * @apiParam {String} codDispositivo C\u00F3digo del dispositivo asociado.
     * @apiParam {String} [observaciones] Observaciones de la operacion de la Jornada.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "token": "WEB",
     *     "usuario": "usuario.pruebas",
     *     "codArea": "505",
     *     "idDistribuidor": "17",
     *     "idResponsable": "462",
     *     "idJornada": "55",
     *     "codDispositivo": "ASDF123QWER456ZXCV789",
     *     "observaciones": ""
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "201",
     *     "mostrar": "1",
     *     "descripcion": "Recurso modificado exitosamente.",
     *     "clase": "OperacionJornadaMasiva",
     *     "metodo": "doPut",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   },
     *   "dispositivos": [
     *      {
     *          "codigoDispositivo": "64E1CE4A48BF656974C376B7905F7E62"
     *      }
     *   ]
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos:  C\u00F3digo de Dispositivo.",
     *     "clase": "CtrlJornadaMasiva",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputJornada doPutJornadaMasiva(@Context HttpServletRequest req, InputJornada input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputJornada response = new JornadaMasiva().modJornada(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios Jornada Masiva -----------------------------//

    //----------------------------------- Servicios Sincronizacion -----------------------------//
    @Path("/creasincronizacion")
    /**
     * @api {POST} /opsidra/creasincronizacion/ [creasincronizacion]
     * 
     * @apiName creasincronizacion
     * @apiDescription Servicio para registrar vendedores listos para finalizar Jornada.
     * @apiGroup Sincronizacion
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de usuario.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} idTipo Id de la ruta o panel al que pertenece el dispositivo.
     * @apiParam {String} tipo="RUTA o PANEL" Nombre del tipo asociado al pertenece el dispositivo.
     * @apiParam {String} idDispositivo Id del dispositivo.
     * @apiParam {String} datos Listado de vendedores con sus jornadas.
     * @apiParam {String} datos.idVendedor Id del vendedor asociado al tipo a registrar.
     * @apiParam {String} datos.idJornada Id de la jornada del vendedor a registrar.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "token": "WEB",
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",
     *     "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
     *     "idTipo": "63",
     *     "tipo": "panel",
     *     "idDispositivo": "ASDF123QWER456ZXCV789",
     *     "datos":[
     *         {
     *             "idVendedor": "904",
     *             "idJornada": "53"
     *         },
     *         {
     *             "idVendedor": "1382",
     *             "idJornada": "54"
     *         }
     *     ]
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionSincronizacion",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-744",
     *     "mostrar": "1",
     *     "descripcion": "No coinciden los datos de vendedor y dispositivo con la jornada 593.",
     *     "clase": "OperacionSincronizacion",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputSincronizacion doPostSincronizacion(@Context HttpServletRequest req, InputSincronizacion input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputSincronizacion response = new Sincronizacion().crearSincronizacion(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    //----------------------------------- Servicios Sincronizacion -----------------------------//
 
    
    @Path("/validavendedor")
    /**
     * @api {POST} /opsidra/validavendedor/ [validavendedor]
     * 
     * @apiName validavendedor
     * @apiDescription Servicio para registrar vendedores listos para finalizar Jornada.
     * @apiGroup Sincronizacion
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idVendedor Id del vendedor a validar.
     * @apiParam {String} codDispositivo Id del dispositivo que el vendedor utiliza.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea":"505",
     *     "usuario": "usuario.pruebas",
     *     "idVendedor": "63",
     *     "codDispositivo": "ASDF123QWER456ZXCV789"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
   *  {
       * "respuesta": {
       *     "codResultado": "12",
       *     "mostrar": "1",
       *     "descripcion": "OK. Datos obtenidos exitosamente",
       *     "clase": " ",
       *     "metodo": " ",
       *     "excepcion": " ",
       *     "tipoExepcion": ""
       * },
     * "vendedores": [
     *      {
     *          "idDTS": "41",
     *          "idBodegaVirtual": "50003",
     *          "idBodegaVendedor": "137",
     *          "idVendedor": "123",
     *          "nombreUsuario": "Nombre vendedor",
     *          "responsable": "1",
     *          "idResponsable": "1541",      
     *          "tipo": "PANEL",
     *          "longitud":"",
     *          "latitud": " ",
     *          "nombreTipo": "RUTA TECH S.A.",
     *          "numRecarga": "63257899",
     *          "numConvenio": "25121",
     *          "pin": "251",
     *          "vendedorAsignado": "236",
     *          "nivelBuzon": "2",
     *          "numIdentificacion": "34543534534534",
     *          "tipoIdentificacion": "RUC",
     *          "numTelefono": "88005544",
     *          "idDispositivo": "236"
     *      }
     *  ]
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
       * {
         *   "respuesta": {
         *     "codResultado": "-42",
         *     "mostrar": "1",
         *     "descripcion": "No se encontraron datos.",
         *     "clase": "",
         *     "metodo": "getVendedorxDTS",
         *     "excepcion": " ",
         *     "tipoExepcion": "Generales"
         *   }
         * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public Outputvendedorxdts validaVendedor(@Context HttpServletRequest req, ValidarVendedor input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        Outputvendedorxdts response = new VendedorXDTS().getValidarVend(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    /*************************************************************************/
    /*************************** SERVICIOS RELEASE 6 *************************/
    /*************************************************************************/
    @Path("/postfichacliente")
    /**
     * @api {POST} /opsidra/postfichacliente/ [postFichaCliente]
     * 
     * @apiName postFichaCliente
     * @apiDescription Servicio para crear fichas de Clientes en el sistema comercial por pa\u00EDs (SCL).
     * @apiGroup Ficha_Cliente
     * @apiVersion 1.0.0
     * 
     * @apiParam {String} token Token de validaci\u00F3n de sesi\u00F3n de usuario.
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
     * @apiParam {String} codDispositivo C\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} tipoCliente "DTS o PDV" Descripci\u00F3n que indica si es un distribuidor o punto de venta
     * @apiParam {String} idDts ID de Sidra que identifica un \u00FAnico Distribuidor.
     * @apiParam {String} idPdv ID de Sidra que identifica un \u00FAnico Punto de Venta. Este campo es opcional solo cuando tipoCliente = PDV
     * @apiParam {String} tipoDocumento Descripci\u00F3n del tipo de documento con el que se identifica el cliente.
     * @apiParam {String} noDocumento Numero \u00FAnico de identificaci\u00F3n del cliente.
     * @apiParam {String} primerNombre Primer nombre del cliente
     * @apiParam {String} segundoNombre Segundo nombre del cliente. Este campo es opcional
     * @apiParam {String} primerApellido Primer apellido del cliente.
     * @apiParam {String} segundoApellido Segundo apellido del cliente. Este campo es opcional
     * @apiParam {String} telContacto N\u00FAmero de tel\u00E9fono de contacto del cliente.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "token": "WEB",
     *     "codArea":"505",
     *     "usuario":"usuario.pruebas",
     *     "tipoCliente":"PDV",
     *     "idDts":"48",
     *     "idPdv":"133",
     *     "tipoDocumento":"RUC",
     *     "noDocumento":"8-443-43323",
     *     "primerNombre":"JOSE",
     *     "segundoNombre":"MARIO",
     *     "primerApellido":"YAC",
     *     "segundoApellido":"PEREZ",
     *     "telContacto":"67979799"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *    "respuesta":    {
     *       "codResultado": "68",
     *       "mostrar": "1",
     *       "descripcion": "OK. La ficha del cliente scl se creo correctamente. El cliente fue creado con \u00E9xito en SCL",
     *       "clase": "OperacionFichaCliente",
     *       "metodo": "doPost",
     *       "excepcion": " ",
     *       "tipoExepcion": ""
     *    },
     *    "cliente":    {
     *       "codCliente": "3009493",
     *       "codCuenta": "2766661",
     *       "idPdv": "132"
     *    }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:
     * {"respuesta": {
     * 		"codResultado": "-604",
     * 		"mostrar": "1",
     * 		"descripcion": "Ocurrio un inconveniente al crear la ficha de cliente en scl. Ocurri\u00F3 un error inesperado. 12037 - Formato numero de telefono inv\u00E9lido = null",
     * 		"clase": "OperacionFichaCliente",
     * 		"metodo": "doPost",
     * 		"excepcion": " ",
     * 		"tipoExepcion": "Generales"
     * }}
     * 
    **/
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputFichaCliente crearFichaClienteSCL(@Context HttpServletRequest req, InputFichaCliente input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputFichaCliente response = new FichaCliente().crearFichaClienteSCL(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/crearanulacion")
    /**
     * @api {POST} /opsidra/crearanulacion/ [crearanulacion]
     * 
     * @apiName crearanulacion
     * @apiDescription Servicio para anular ventas registradas en Sidra.
     * @apiGroup Anulacion
     * @apiVersion 1.0.0
     * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de usuario.
     * @apiParam {String} codDispositivo C\u00F3digo del dispositivo al que se reservar\u00E9n los folios.
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idJornada Identificador de la jornada activa.
     * @apiParam {String} idVendedor Identificador del vendedor de la jornada y la venta.
     * @apiParam {String} idVenta Identificador de la venta a anular.
     * @apiParam {String} fechaAnulacion Fecha y hora de la anulaci\u00F3n en formato yyyyMMddHHMISS.
     * @apiParam {String} razonAnulacion Raz\u00F3n de la anulaci\u00F3n, se permiten \u00FAnicamente los valores configurados en el sistema.
     * @apiParam {String} [observaciones] Observaciones de la anulaci\u00F3n.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "token": "WEB",
     *     "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
     *     "codArea":"505",
     *     "usuario":"usuario.pruebas",
     *     "idJornada":"4",
     *     "idVendedor":"2242",
     *     "idVenta":"8",
     *     "fechaAnulacion":"20161028161202",
     *     "razonAnulacion":"CANCELACION",
     *     "observaciones":""
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "token": "WEB",
     *   "idAnulacion": "1",
     *   "respuesta": {
     *     "codResultado": "200",
     *     "mostrar": "1",
     *     "descripcion": "Campos agregados exitosamente.",
     *     "clase": "OperacionAnulacion",
     *     "metodo": "doPost",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:
     * {
     *   "token": "WEB",
     *   "respuesta": {
     *     "codResultado": "-800",
     *     "mostrar": "1",
     *     "descripcion": "La fecha de anulaci\u00F3n no puede ser antes de la fecha de venta.",
     *     "clase": "CtrlAnulacion",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputAnulacion doPostAnulacion(@Context HttpServletRequest req, InputAnulacion input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputAnulacion response = new Anulacion().crearAnulacion(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/asociarutapdv")
    /**
     * @api {POST} /opsidra/asociarutapdv/ [asociarutapdv]
     * 
     * @apiName asociarutapdv
     * @apiDescription Servicio para anular ventas registradas en Sidra.
     * @apiGroup Rutas
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idRuta Id de la ruta a la que se realizar\u00E9 la asocici\u00F3n de pdv's.
     * @apiParam {String} pdv lista de pdv's a asociar.
     * @apiParam {String} asociacion="1 o 0" Con valor 1 se realiza asociaci\u00F3n. Con valor 0 se elimina asociaci\u00F3n.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea":"505",
     *     "usuario":"usuario.pruebas",
     *     "idRuta":"4",
     *     "pdv":[1,24,25],
     *     "asociacion":"1"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "23",
     *     "mostrar": "1",
     *     "descripcion": "Ok. Asociacion de punto de venta a ruta, realizada correctamente.",
     *     "clase": "",
     *     "metodo": "",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:
     * {
      *   "respuesta": {
     *     "codResultado": "-539",
     *     "mostrar": "1",
     *     "descripcion": "El valor para el campo asociaci\u00F3n es inv\u00E9lido.",
     *     "clase": "CtrlRutaPdv",
     *     "metodo": "asociaRutaPdv",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public Respuesta asociaRutaPdv(@Context HttpServletRequest req, InputRutaPdv input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        Respuesta response = new AsociaRutaPdv().asociaRutaPdv(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/asignafechajornada")
    /**
     * @api {POST} /opsidra/asignafechajornada/ [asignafechajornada]
     * 
     * @apiName asignafechajornada
     * @apiDescription Servicio para asignar fecha de cierrr de jornada por vendedor.
     * @apiGroup Jornada
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idVendedor Identificador del vendedor de la jornada y la venta.
     * @apiParam {String} fecha Fecha del cierre de jornada en formato ddMMyyyy.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea":"505",
     *     "usuario":"usuario.pruebas",
     *     "idVendedor":"2242",
     *     "fecha":"31122016"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "71",
     *     "mostrar": "1",
     *     "descripcion": "OK. Fecha registrada correctamente.",
     *     "clase": "OperacionJornada",
     *     "metodo": "asignaFechaJornada",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:
     * {
     *   "respuesta": {
     *     "codResultado": "-848",
     *     "mostrar": "1",
     *     "descripcion": "El vendedor enviado no se encontr\u00F3 como vendedor responsable.",
     *     "clase": "CtrlJornada",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputJornada asignaFechaJornada(@Context HttpServletRequest req, InputJornada input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputJornada response = new Jornada().asignaFechaJornada(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/eliminarutapdv")
    /**
     * @api {POST} /opsidra/eliminarutapdv/ [eliminarutapdv]
     * 
     * @apiName eliminarutapdv
     * @apiDescription Servicio para anular ventas registradas en Sidra.
     * @apiGroup Rutas
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idRuta Id de la ruta a la que se eliminar\u00E9 la asocici\u00F3n de pdv's.
     * @apiParam {String} pdv lista de pdv's a desasignar.
     * @apiParam {String} asociacion="1 o 0" Con valor 1 se realiza asociaci\u00F3n. Con valor 0 se elimina asociaci\u00F3n.
     * 
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea":"505",
     *     "usuario":"usuario.pruebas",
     *     "idRuta":"4",
     *     "pdv":[1,24,25],
     *     "asociacion":"1"
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "24",
     *     "mostrar": "1",
     *     "descripcion": "Ok. Asociaci\u00F3n de punto de venta y ruta, eliminada correctamente.",
     *     "clase": "",
     *     "metodo": "",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:
     * {
      *   "respuesta": {
     *     "codResultado": "-539",
     *     "mostrar": "1",
     *     "descripcion": "El valor para el campo asociaci\u00F3n es inv\u00E9lido.",
     *     "clase": "CtrlRutaPdv",
     *     "metodo": "asociaRutaPdv",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public Respuesta desasignaRutaPdv(@Context HttpServletRequest req, InputRutaPdv input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        Respuesta response = new AsociaRutaPdv().eliminaRutaPdv(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

   

    /*************************************************************************/
    /*************************** SERVICIOS RELEASE 6.5 ***********************/
    /*************************************************************************/
    @Path("/deladjuntogestion")
    /**
     * @api {POST} /opsidra/deladjuntogestion/ [deladjuntogestion]
     * 	
     * @apiName deladjuntogestion
     * @apiDescription Servicio para eliminar adjuntos.
     * @apiGroup AdjuntoGestion
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} token token de validaci\u00F3n de usuario.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} idAdjunto Identificador del adjunto de gesti\u00F3n a eliminar.
     * 
     * @apiParamExample {json} Ejemplo parametros de Entrada:
     * {
     *     "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
     *     "usuario": "usuario.pruebas",
     *     "codArea": "505",
     *     "token": "WEB",
     *     "idAdjunto": "1"
     * }
     * 
     * @apiSuccessExample {} JSON Respuesta-\u00E9xito:
     * {
     *   "token": "WEB",
     *   "respuesta": {
     *     "codResultado": "79",
     *     "mostrar": "1",
     *     "descripcion": "OK. Se elimin\u00F3 el adjunto correctamente.",
     *     "clase": "CtrlAdjuntoGestion",
     *     "metodo": "delAdjunto",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {} JSON Respuesta-Error:     
     * {
     *   "token": "WEB",
     *   "respuesta": {
     *     "codResultado": "-891",
     *     "mostrar": "1",
     *     "descripcion": "No se encontr\u00F3 el adjunto solicitado.",
     *     "clase": " ",
     *     "metodo": "delAdjunto",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputAdjuntoGestion doDelAdjuntoGestion(@Context HttpServletRequest req, InputAdjuntoGestion input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputAdjuntoGestion response = new AdjuntoGestion().delAdjunto(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    
    @Path("/asociaarticuloprecio")
    /**
     * @api {POST} /opsidra/asociaarticuloprecio/ [asociaarticuloprecio]
     * 
     * @apiName asociaarticuloprecio
     * @apiDescription Servicio para asociar articulo-precio.
     * @apiGroup ArticuloPrecio
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idArticulo Identificador del articulo a asociar.
     * @apiParam {String} tipoGestion nombre de la gesti\u00F3n a la que estar\u00E9 asociado el articulo y su precio. ALTA_PREPAGO y PORTABILIDAD.
     * @apiParam {String} precio cantidad en cuanto al precio que se le dar\u00E9 al art\u00EDculo.
     * @apiParam {String} idProductOffering Identificador de la oferta que se asociar\u00E9 al precio de un art\u00EDculo.
     * 
     * @apiParamExample {json} Ejemplo parametros de Entrada:
     * {
     *     "usuario": "usuario.pruebas",
     *     "codArea": "505",
     *     "idArticulo":"9293",
     *     "tipoGestion":"ALTA_PREPAGO",
     *     "precio":"4.45",
     *     	"idProductOffering":"9145627885465644655"
     * }
     * 
     * @apiSuccessExample {} JSON Respuesta-\u00E9xito:
     * {
     *   "token": "WEB",
     *   "respuesta": {
     *      "codResultado": "81",
     *     "mostrar": "1",
     *      "descripcion": "OK. Se creo asociaci\u00F3n de art\u00EDculo y oferta ",
     *     "clase": "",
     *     "metodo": "",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {} JSON Respuesta-Error:     
     * {
     *   "token": "WEB",
     *   "respuesta": {
     *     "codResultado": "-665",
     *     "mostrar": "1",
     *     "descripcion": "El par\u00E9metro de entrada \\\"IdProductOffering\"\\ esta vac\u00EDo",
     *     "clase": "CtrlArticuloPrecio",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputArticuloPrecio asociaArticuloPrecio(@Context HttpServletRequest req, InputArticuloPrecio input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputArticuloPrecio response = new ArticuloPrecio().crearArticuloPrecio(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);
        return response;
    }
    
    @Path("/modarticuloprecio")
    /**
     * @api {POST} /opsidra/modarticuloprecio/ [modarticuloprecio]
     * 
     * @apiName modarticuloprecio
     * @apiDescription Servicio para modificar asociaci\u00F3n de articulo-precio.
     * @apiGroup ArticuloPrecio
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} idArticulo Identificador del articulo a asociar.
     * @apiParam {String} tipoGestion nombre de la gesti\u00F3n a la que estar\u00E9 asociado el articulo y su precio. ALTA_PREPAGO y PORTABILIDAD.
     * @apiParam {String} precio cantidad en cuanto al precio que se le dar\u00E9 al art\u00EDculo.
     * @apiParam {String} idProductOffering Identificador de la oferta que se asociar\u00E9 al precio de un art\u00EDculo.
     * 
     * @apiParamExample {json} Ejemplo parametros de Entrada:
     * {
     *     "usuario": "usuario.pruebas",
     *     "codArea": "503",
     *     "idArticulo":"9293",
     *     "tipoGestion":"ALTA_PREPAGO",
     *     "precio":"4.45",
     *     "idProductOffering":"9145627885465644655"
     * }
     * 
     * @apiSuccessExample {} JSON Respuesta-\u00E9xito:
     * {
     *   "token": "WEB",
     *   "respuesta": {
     *      "codResultado": "81",
     *     "mostrar": "1",
     *      "descripcion": "OK. art\u00EDculo-precio modificado exitosamente. ",
     *     "clase": "",
     *     "metodo": "",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {} JSON Respuesta-Error:     
     * {
     *   "token": "WEB",
     *   "respuesta": {
     *     "codResultado": "-665",
     *     "mostrar": "1",
     *     "descripcion": "El par\u00E9metro de entrada \\\"IdProductOffering\"\\ esta vac\u00EDo",
     *     "clase": "CtrlArticuloPrecio",
     *     "metodo": "validarInput",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputArticuloPrecio modificaArticuloPrecio(@Context HttpServletRequest req, InputArticuloPrecio input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputArticuloPrecio response = new ArticuloPrecio().modificaArticuloPrecio(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);
        return response;
    }
    
    @Path("/creaportabilidad")
    /**
     * @api {POST} /opsidra/creaportabilidad/ [creaportabilidad]
     * 
     * @apiName creaportabilidad
     * @apiDescription Servicio para crear portabilidad
     * @apiGroup Portabilidad
     * @apiVersion 1.0.0   
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} token token de validaci\u00F3n de usuario.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} idJornada Jornada activa del vendedor.
     * @apiParam {String} idVendedor Id del vendedor del cual se desean registradas la portabilidad.
     * @apiParam {String} idPortaMovil Id del Portal Movil.
     * @apiParam {String} numPortar n\u00FAmero a portar.
     * @apiParam {String} operadorDonante Nombre del operador donante
     * @apiParam {String} cip codigo cip
     * @apiParam {String} productoDonante Tipo de producto donante puede ser: PREPAGO/POSTPAGO
     * @apiParam {String} numTemporal N\u00FAmero temporal 
     * @apiParam {String} primerNombre Primer nombre del cliente
     * @apiParam {String} segundoNombre Segundo nombre del cliente. Este campo es opcional
     * @apiParam {String} primerApellido Primer apellido del cliente.
     * @apiParam {String} segundoApellido Segundo apellido del cliente. Este campo es opcional
     * @apiParam {String} tipoDocumento Descripci\u00F3n del tipo de documento con el que se identifica el cliente.
     * @apiParam {String} noDocumento Numero \u00FAnico de identificaci\u00F3n del cliente.
     * @apiParam {String} [adjuntoporta.nombreArchivo] nombre archivo adjunto.
     * @apiParam {String} [adjuntoporta.adjunto] archivo adjunto.
     *      
     * @apiParamExample {json} Ejemplo parametros de Entrada:
     *  {
     *  	"usuario": "usuario.pruebas",
     *  	"codArea": "505",
     *  	"token": "",
     *  	"codDispositivo": "",
     *      "idJornada":"2",
     *      "idVendedor":"",
     *  	"idPortaMovil":"",
     *  	"numPortar":"",
     *  	"operadorDonante":"",
     *  	"cip":"",
     *  	"productoDonante":"POSTPAGO",
     *  	"numTemporal":"",
     *  	"primerNombre": "",
     *  	"segundoNombre": "",
     *  	"primerApellido": "",
     *  	"segundoApellido": "",
     *  	"tipoDocumento": "",
     *  	"noDocumento": "",
     *  	"adjuntoporta":[
     *  		{
     *  			"nombreArchivo":"",
     *  			"adjunto":""
     *  		}
     *  	]
     *  }
     * 
     * @apiSuccessExample {} JSON Respuesta-\u00E9xito:
	 *	{
	 *		"respuesta": {
	 *			"codResultado": "202",
	 *			"mostrar": "1",
	 *			"descripcion": "OK. Solicitud de Portabilidad Enviada Correctamente",
	 *		 	"clase": "",
	 *			"metodo": "",
	 *			"excepcion": " ",
	 *			"tipoExepcion": ""
	 *			},
	 *	
	 *			"idSolicitudPorta":""
	 *	}
     * 
     * @apiErrorExample {} JSON Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-643",
     *     "mostrar": "1",
     *     "descripcion": "No se encontraron datos.",
     *     "clase": "",
     *     "metodo": "",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputCreaPortabilidad creaPortabilidad(@Context HttpServletRequest req, InputCreaPortabilidad input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputCreaPortabilidad response = new Portabilidad().creaPortabilidad(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);
        return response;
    }
    
    @Path("/cargararchivosporta")
    /**
     * @api {POST} /opsidra/cargararchivosporta/ [cargararchivosporta]
     * 
     * @apiName cargararchivosporta
     * @apiDescription Servicio para realizar cargas de archivos para el proceso de Portabilidad.
     * @apiGroup Portabilidad
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} token token de validaci\u00F3n de usuario.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} idPortabilidad Identificador de la gestion de portabilidad.
     * @apiParam {String} idPortalMovil Identificador de la gestion a la que se asociar\u00E9 el archivo.
     * @apiParam {String} nombreArchivo Nombre del archivo adjunto.
     * @apiParam {String} extension Extensi\u00F3n del archivo a adjuntar.
	 * @apiParam {String} tipoArchivo Tipo del documento a adjuntar.
     * @apiParam {String} idAttachment id getion FS.
     * @apiParam {String} adjunto Archivo Adjunto.
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
     * {
     *     "codArea": "505",
     *     "usuario": "usuario.pruebas",     *     
     *     "token": "WEB",
     *     "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
     *     "idPortabilidad": "126"
     *     "idPortaMovil": ""
     *     "nombreArchivo": "Adjunto 1",
     *     "extension": "PDF",
     *     "tipoArchivo": "PDF",
     *     "idAttachment" "132";
     *     "adjunto": "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkG....",
     * }
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "idAdjunto": "1",
     *   "respuesta": {
     *     "codResultado": "64",
     *     "mostrar": "1",
     *     "descripcion": "OK. Se registr\u00F3 el archivo adjunto correctamente.",
     *     "clase": "CtrlCargaArchivosPorta",
     *     "metodo": "cargarArchivoPorta",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos:  Tipo Documento no corresponde a ninguno de los tipos definidos.",
     *     "clase": "CtrlCargaArchivosPorta",
     *     "metodo": "cargarArchivoPorta",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputAdjuntoPorta cargarAdjuntoPorta(@Context HttpServletRequest req, InputCargaAdjuntoPorta input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputAdjuntoPorta response = new AdjuntoPortabilidad().cargarAdjunto(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    
    @Path("/registraticket")
    /**
     * @api {POST} /opsidra/registraticket/ [registraticket]
     * 
     * @apiName registraticket
     * @apiDescription Servicio para registrar la impresion de ticket en SIDRA.
     * @apiGroup VisorTCK
     * @apiVersion 1.0.0
     * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que se desea realizar la operaci\u00F3n.
     * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
     * @apiParam {String} token token de validaci\u00F3n de usuario.
     * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
     * @apiParam {String} idVenta Id de la venta de la cual se registrar\u00E9 el visor.
     * @apiParam {String} lineas detalle de lineas del ticket.
     
     * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada:
	*	{
	*		'codArea':'503',
	*		'usuario':'pruebas',
	*		'codDispositivo':'456483',
	*		'token':'123484531813216843a5468as1d5123198135a1sd86151a2sd',
	*		'idVenta':'100',
	*		'lineas': [{
	*						'izquierda': '',
	*						'centro': null,
	*						'derecha': '',
	*						'alineacion': 'E',
	*						'estilo': null,
	*						'extra': null
	*					}, {
	*						'izquierda': 'TELEFONICA MOVILES EL SALVADOR, S.A. DE C.V.',
	*						'centro': null,
	*						'derecha': '',
	*						'alineacion': 'C',
	*						'estilo': null,
	*						'extra': null
	*					}
	*					]
	*	}
     * 
     * @apiSuccessExample {json} Respuesta-\u00E9xito:
     * {
     *   "respuesta": {
     *     "codResultado": "64",
     *     "token":"gfdgdfg",
     *     "mostrar": "1",
     *     "descripcion": "OK. Se registr\u00F3 el archivo adjunto correctamente.",
     *     "clase": "CtrlCargaArchivosPorta",
     *     "metodo": "cargarArchivoPorta",
     *     "excepcion": " ",
     *     "tipoExepcion": ""
     *   }
     * }
     * 
     * @apiErrorExample {json} Respuesta-Error:     
     * {
     *   "respuesta": {
     *     "codResultado": "-199", 
     *     "token":"gfdgdfg",
     *     "mostrar": "1",
     *     "descripcion": "Los siguientes datos faltan o son incorrectos:  Tipo Documento no corresponde a ninguno de los tipos definidos.",
     *     "clase": "CtrlCargaArchivosPorta",
     *     "metodo": "cargarArchivoPorta",
     *     "excepcion": " ",
     *     "tipoExepcion": "Generales"
     *   }
     * }
     * 
     * @apiPermission admin
    */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
    public OutputTicket registraTCK(@Context HttpServletRequest req, InputTicket input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputTicket response = new VisorTicket().registraVisorTCK(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
    
	@Path("/crearAbonoVentaCredito")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputAbonoVentaCredito crearAbonoVC(@Context HttpServletRequest req, AbonoVentaCredito input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputAbonoVentaCredito response = new OutputAbonoVentaCredito();
		ControllerAbonoVentaCredito controller = new ControllerAbonoVentaCredito();
		if (controller.insertAbonocredito(input) == 1) {
			response.setAbono(input);
		} else {
			response = null;
		}

		return response;
	}

	@Path("/crearVentasCredito")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputVentaCredito crearVentaCredito(@Context HttpServletRequest req, VentaCredito input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputVentaCredito response = null;
		ControllerVentaCredito controller = new ControllerVentaCredito();
		try {
			response = controller.getVentasCredito(input);
		} catch (Exception e) {

		}

		return response;

	}
	@Path("/registrarRefund")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public DetallePago registrarRefund(@Context HttpServletRequest req, DetallePago input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		DetallePago response = null;
		CtrlVentaRuta controller = new CtrlVentaRuta();
		
		 
		 
		try {
			response = controller.registrarRefund(input);
		} catch (Exception e) {
			System.out.println("ERROR AL HACER EL REFUND");
			return response;
		}
		String jsonRequest = new GsonBuilder().setPrettyPrinting().create().toJson(input);
		String jsonResponse = new GsonBuilder().setPrettyPrinting().create().toJson(response);
		System.out.println("REQUEST HACER REFUND: "+jsonRequest);
		System.out.println("RESPONSE HACER REFUND: "+jsonResponse);

		return response;

	}
    
}

package com.consystec.sc.ca.ws.service;

import java.io.File;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

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
import com.consystec.sc.ca.ws.input.consultas.InputConsultaNumRecarga;
import com.consystec.sc.ca.ws.input.consultas.InputConsultaSaldoPayment;
import com.consystec.sc.ca.ws.input.consultas.InputVentaFE;
import com.consystec.sc.ca.ws.input.cuenta.InputCuenta;
import com.consystec.sc.ca.ws.input.descuentoFS.InputDescuentoFS;
import com.consystec.sc.ca.ws.input.deuda.InputTransDeuda;
import com.consystec.sc.ca.ws.input.dispositivo.InputDispositivo;
import com.consystec.sc.ca.ws.input.dts.InputDistribuidor;
import com.consystec.sc.ca.ws.input.fichacliente.InputFichaCliente;
import com.consystec.sc.ca.ws.input.file.InputCargaFile;
import com.consystec.sc.ca.ws.input.general.InputConsultaWeb;
import com.consystec.sc.ca.ws.input.historico.InputHistoricoPromocionales;
import com.consystec.sc.ca.ws.input.impuestos.InputConsultaImpuestos;
import com.consystec.sc.ca.ws.input.inventario.InputConsultaCantInv;
import com.consystec.sc.ca.ws.input.inventario.InputConsultaInventario;
import com.consystec.sc.ca.ws.input.inventario.InputConsultaSeries;
import com.consystec.sc.ca.ws.input.inventario.InputHistoricoInv;
import com.consystec.sc.ca.ws.input.inventario.InputValidaArticulo;
import com.consystec.sc.ca.ws.input.inventariomovil.InputConsultaInventarioMovil;
import com.consystec.sc.ca.ws.input.inventariopromo.InputGetArtPromInventario;
import com.consystec.sc.ca.ws.input.jornada.InputJornada;
import com.consystec.sc.ca.ws.input.oferCom.InputConsultaArticulos;
import com.consystec.sc.ca.ws.input.ofertacampania.InputOfertaCampania;
import com.consystec.sc.ca.ws.input.ofertacampania.InputOfertaCampaniaMovil;
import com.consystec.sc.ca.ws.input.ofertacampania.InputPromoOfertaCampania;
import com.consystec.sc.ca.ws.input.panel.InputPanel;
import com.consystec.sc.ca.ws.input.pdv.InputConsultaPDV;
import com.consystec.sc.ca.ws.input.portabilidad.InputEstadoPortabilidad;
import com.consystec.sc.ca.ws.input.portabilidad.InputPortabilidad;
import com.consystec.sc.ca.ws.input.promocionales.InputPromocionales;
import com.consystec.sc.ca.ws.input.remesa.InputRemesa;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCantInvJornada;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCumplimientoVenta;
import com.consystec.sc.ca.ws.input.reporte.InputReporteCumplimientoVisita;
import com.consystec.sc.ca.ws.input.reporte.InputReporteEfectividadVenta;
import com.consystec.sc.ca.ws.input.reporte.InputReporteInventarioVendido;
import com.consystec.sc.ca.ws.input.reporte.InputReportePDV;
import com.consystec.sc.ca.ws.input.reporte.InputReporteRecarga;
import com.consystec.sc.ca.ws.input.reporte.InputReporteXZ;
import com.consystec.sc.ca.ws.input.ruta.InputRuta;
import com.consystec.sc.ca.ws.input.sincronizacion.InputSincronizacion;
import com.consystec.sc.ca.ws.input.solicitud.InputSolicitud;
import com.consystec.sc.ca.ws.input.solicitudescip.InputSolicitudesCip;
import com.consystec.sc.ca.ws.input.ticket.InputTicket;
import com.consystec.sc.ca.ws.input.ticket.OutputTicket;
import com.consystec.sc.ca.ws.input.tipotransaccion.InputTransaccionInv;
import com.consystec.sc.ca.ws.input.vendedordts.InputVendedorDTS;
import com.consystec.sc.ca.ws.input.vendedorxdts.InputVendxdts;
import com.consystec.sc.ca.ws.input.venta.InputControlPrecios;
import com.consystec.sc.ca.ws.input.venta.InputGetDetalle;
import com.consystec.sc.ca.ws.input.venta.InputGetVenta;
import com.consystec.sc.ca.ws.input.visita.InputGetVisita;
import com.consystec.sc.ca.ws.metodos.ActualizacionAPP;
import com.consystec.sc.ca.ws.metodos.AdjuntoGestion;
import com.consystec.sc.ca.ws.metodos.Anulacion;
import com.consystec.sc.ca.ws.metodos.ArticuloPrecio;
import com.consystec.sc.ca.ws.metodos.Asignacion;
import com.consystec.sc.ca.ws.metodos.BodegaAlmacen;
import com.consystec.sc.ca.ws.metodos.BodegaVirtual;
import com.consystec.sc.ca.ws.metodos.Bodegas;
import com.consystec.sc.ca.ws.metodos.BuzonSidra;
import com.consystec.sc.ca.ws.metodos.CargaFile;
import com.consystec.sc.ca.ws.metodos.Catalogos;
import com.consystec.sc.ca.ws.metodos.Condicion;
import com.consystec.sc.ca.ws.metodos.CondicionOferta;
import com.consystec.sc.ca.ws.metodos.ConsultaCantInv;
import com.consystec.sc.ca.ws.metodos.ConsultaNumRecarga;
import com.consystec.sc.ca.ws.metodos.ConsultaSaldoPayment;
import com.consystec.sc.ca.ws.metodos.ConsultaSeries;
import com.consystec.sc.ca.ws.metodos.ControlPrecios;
import com.consystec.sc.ca.ws.metodos.Cuenta;
import com.consystec.sc.ca.ws.metodos.DescuentoFS;
import com.consystec.sc.ca.ws.metodos.Dispositivo;
import com.consystec.sc.ca.ws.metodos.Distribuidor;
import com.consystec.sc.ca.ws.metodos.EstadoPorta;
import com.consystec.sc.ca.ws.metodos.FichaCliente;
import com.consystec.sc.ca.ws.metodos.FolioRutaPanel;
import com.consystec.sc.ca.ws.metodos.FolioSCL;
import com.consystec.sc.ca.ws.metodos.HistoricoInv;
import com.consystec.sc.ca.ws.metodos.HistoricoPromo;
import com.consystec.sc.ca.ws.metodos.Impuestos;
import com.consystec.sc.ca.ws.metodos.Inventario;
import com.consystec.sc.ca.ws.metodos.InventarioMovil;
import com.consystec.sc.ca.ws.metodos.Jornada;
import com.consystec.sc.ca.ws.metodos.JornadaMasiva;
import com.consystec.sc.ca.ws.metodos.MPOS;
import com.consystec.sc.ca.ws.metodos.OfertaCampania;
import com.consystec.sc.ca.ws.metodos.OfertaCampaniaMovil;
import com.consystec.sc.ca.ws.metodos.OfertaComercial;
import com.consystec.sc.ca.ws.metodos.OfertaFS;
import com.consystec.sc.ca.ws.metodos.Panel;
import com.consystec.sc.ca.ws.metodos.PromoOfertaCampania;
import com.consystec.sc.ca.ws.metodos.Promocionales;
import com.consystec.sc.ca.ws.metodos.PuntoVenta;
import com.consystec.sc.ca.ws.metodos.Remesa;
import com.consystec.sc.ca.ws.metodos.Reporte;
import com.consystec.sc.ca.ws.metodos.ReporteRecargas;
import com.consystec.sc.ca.ws.metodos.ReporteXZ;
import com.consystec.sc.ca.ws.metodos.ResumenDeuda;
import com.consystec.sc.ca.ws.metodos.Ruta;
import com.consystec.sc.ca.ws.metodos.Sincronizacion;
import com.consystec.sc.ca.ws.metodos.SolicitudWorkFlow;
import com.consystec.sc.ca.ws.metodos.SolicitudesCip;
import com.consystec.sc.ca.ws.metodos.TipoTransaccion;
import com.consystec.sc.ca.ws.metodos.UsuarioBuzon;
import com.consystec.sc.ca.ws.metodos.ValidaPortabilidad;
import com.consystec.sc.ca.ws.metodos.ValidaSerie;
import com.consystec.sc.ca.ws.metodos.VendedorDTS;
import com.consystec.sc.ca.ws.metodos.VendedorXDTS;
import com.consystec.sc.ca.ws.metodos.VentaFE;
import com.consystec.sc.ca.ws.metodos.VentaRuta;
import com.consystec.sc.ca.ws.metodos.VisitaGestion;
import com.consystec.sc.ca.ws.metodos.VisorTicket;
import com.consystec.sc.ca.ws.output.adjuntogestion.OutputAdjuntoGestion;
import com.consystec.sc.ca.ws.output.anulacion.OutputAnulacion;
import com.consystec.sc.ca.ws.output.articuloprecio.OutputArticuloPrecio;
import com.consystec.sc.ca.ws.output.asignacion.OutputAsignacion;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegaDTS;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegaVirtual;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegasSCL;
import com.consystec.sc.ca.ws.output.buzon.OutputBuzon;
import com.consystec.sc.ca.ws.output.buzon.OutputUsuarioBuzon;
import com.consystec.sc.ca.ws.output.catalogo.OutputCatalogo;
import com.consystec.sc.ca.ws.output.condicion.OutputCondicion;
import com.consystec.sc.ca.ws.output.condicionoferta.OutputCondicionOferta;
import com.consystec.sc.ca.ws.output.consultas.OutputConsultaNumRecarga;
import com.consystec.sc.ca.ws.output.consultas.OutputConsultaSaldoPayment;
import com.consystec.sc.ca.ws.output.consultas.OutputgetVentaFE;
import com.consystec.sc.ca.ws.output.cuenta.OutputCuenta;
import com.consystec.sc.ca.ws.output.descuentoFS.OutputDescuentoFS;
import com.consystec.sc.ca.ws.output.deuda.OutputTransDeuda;
import com.consystec.sc.ca.ws.output.dispositivo.OutputDispositivo;
import com.consystec.sc.ca.ws.output.dts.OutputDistribuidor;
import com.consystec.sc.ca.ws.output.fichacliente.OutputFichaCliente;
import com.consystec.sc.ca.ws.output.file.OutputImagen;
import com.consystec.sc.ca.ws.output.folio.OutputConfiguracionFolioVirtual;
import com.consystec.sc.ca.ws.output.historico.OutputHistoricoPromo;
import com.consystec.sc.ca.ws.output.impuestos.OutputImpuestos;
import com.consystec.sc.ca.ws.output.inventario.OutputConsultaCantInv;
import com.consystec.sc.ca.ws.output.inventario.OutputConsultaSeries;
import com.consystec.sc.ca.ws.output.inventario.OutputHistorico;
import com.consystec.sc.ca.ws.output.inventario.OutputInventario;
import com.consystec.sc.ca.ws.output.inventario.OutputValidaInventario;
import com.consystec.sc.ca.ws.output.inventariomovil.OutputInventarioMovil;
import com.consystec.sc.ca.ws.output.inventariopromo.OutputArtPromInventario;
import com.consystec.sc.ca.ws.output.jornada.OutputJornada;
import com.consystec.sc.ca.ws.output.oferCom.OutputConsultaArticulos;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputOfertaCampania;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputOfertaCampaniaMovil;
import com.consystec.sc.ca.ws.output.ofertacampania.OutputPromoOfertaCampania;
import com.consystec.sc.ca.ws.output.panel.OutputPanel;
import com.consystec.sc.ca.ws.output.pdv.OutputpdvDirec;
import com.consystec.sc.ca.ws.output.portabilidad.OutputEstadoPortabilidad;
import com.consystec.sc.ca.ws.output.portabilidad.OutputPortabilidad;
import com.consystec.sc.ca.ws.output.promocionales.OutputPromocionales;
import com.consystec.sc.ca.ws.output.remesa.OutputRemesa;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCantInvJornada;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCumplimientoVenta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteCumplimientoVisita;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteEfectividadVenta;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteInventarioVendido;
import com.consystec.sc.ca.ws.output.reporte.OutputReportePDV;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteRecarga;
import com.consystec.sc.ca.ws.output.reporte.OutputReporteXZ;
import com.consystec.sc.ca.ws.output.reporte.OutputResumenInventarioVendido;
import com.consystec.sc.ca.ws.output.ruta.OutputRuta;
import com.consystec.sc.ca.ws.output.sincronizacion.OutputSincronizacion;
import com.consystec.sc.ca.ws.output.solicitud.OutputDeuda;
import com.consystec.sc.ca.ws.output.solicitud.OutputSolicitud;
import com.consystec.sc.ca.ws.output.solicitudescip.OutputSolicitudesCip;
import com.consystec.sc.ca.ws.output.transaccion.OutputTransaccionInv;
import com.consystec.sc.ca.ws.output.vendedordts.OutputVendedorDTS;
import com.consystec.sc.ca.ws.output.vendedorxdts.Outputvendedorxdts;
import com.consystec.sc.ca.ws.output.venta.OutputArticuloVenta;
import com.consystec.sc.ca.ws.output.venta.OutputVenta;
import com.consystec.sc.ca.ws.output.venta.RespuestaControlPrecios;
import com.consystec.sc.ca.ws.output.visita.OutputVisita;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.MediaType;
import com.consystec.sc.sv.ws.metodos.CtrlPrueba;
import com.consystec.sc.sv.ws.orm.ofertafs.input.InputOfertaFS;
import com.consystec.sc.sv.ws.orm.ofertafs.output.OutputOfertaFS;
import com.ericsson.sc.ca.ws.input.actualizacion.movil.InputConsultaActualizacion;
import com.ericsson.sc.ca.ws.output.actualizacion.movil.OutputDispositivoActualizado;
import com.ericsson.sdr.dto.ws.request.RequestNumeroPayment;
import com.ericsson.sdr.dto.ws.response.AbstractResponse;
import com.google.gson.GsonBuilder;

/**
 * @author sbarrios Consystec 2015
 */

@Path("/consultasidra")
public class ServicioConsulta extends ServicioBase {
	private static final Logger log = Logger.getLogger(ServicioConsulta.class);
	
	public static void main(String[] args) {
		DOMConfigurator.configure(ServicioConsulta.class.getClassLoader().getResource("log4j.xml"));
	}

	@Path("/getbodegascl/")
	/**
	 * @api {POST} /consultasidra/getbodegascl/ [getBodegasSCL]
	 * @apiName getBodegasSCL
	 * @apiDescription Servicio para obtener las bodegas disponibles en el sistema
	 *                 comercial
	 * @apiGroup Bodegas
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se
	 *           desea obtener informaci\u00F3n de bodegas.
	 * @apiParam {String} usuario nombre de usuario que solicita la operacion.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "1", "mostrar": "1", "descripcion": "Ok.
	 *                    Obtenci\u00F3n de Bodegas exitosa.", "clase": " ",
	 *                    "metodo": " ", "excepcion": " ", "tipoExepcion": " " },
	 *                    "bodegas": [ { "idBodega": "576", "codBodega": "600",
	 *                    "nombreBodega": "UNIDAD DE RETENCI\u00F3n " }, {
	 *                    "idBodega": "577", "codBodega": "601", "nombreBodega":
	 *                    "SEGUROS" } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-101", "mostrar": "0", "descripcion": "Ocurrio un Problema
	 *                  inesperado, contacte a su Administrador.", "clase":
	 *                  "com.consystec.sc.ca.ws.metodos.Bodegas", "metodo":
	 *                  "getMensaje", "excepcion": "ORA-00942: table or view does
	 *                  not exist\n", "tipoExepcion": "Excepcion SQL" } }
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputBodegasSCL consultaBodega(@Context HttpServletRequest req, InputConsultaWeb input) {
		Date beginTime = new Date();
		OutputBodegasSCL response = new Bodegas().getBodegas(input);
		ControladorBase.setRequestGlobal(req);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/prueba/")
	@GET
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING })
	public Map<String, Object> consultaBodega() {
		return new CtrlPrueba().test();
	}
	
	@Path("/consultaActualizacion/")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING })
	public OutputDispositivoActualizado consultaActualizacionAPP(@Context HttpServletRequest req, InputConsultaActualizacion input) {
		Date beginTime = new Date();
		OutputDispositivoActualizado response = new ActualizacionAPP().existeActualizacion(input);
		ControladorBase.setRequestGlobal(req);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);
		return response;
	}
	
	@Path("/obtenerUltimoAPK/")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response obtenerUltimoAPK(@Context HttpServletRequest req) {
		ActualizacionAPP archivoAPK = new ActualizacionAPP();
		File file = archivoAPK.obtenerActualizacion();
		if(file == null) {
			return Response.ok().build();
			
		}else {
			return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
				      .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
				      .build();
		}
				  
	}
	
	@Path("/getMPOSAPK/")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getMPOSAPK(@Context HttpServletRequest req) {
		MPOS archivoAPK = new MPOS();
		log.trace("creando file");
		File file = archivoAPK.obtenerAPKMPOS();
		if(file == null) {
			log.trace("apk viene null");
			return Response.ok().build();
			
		}else {
			log.trace("apk no null");
			return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
				      .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
				      .build();
		}
				  
	}

	@Path("/getbodalmacen/")
	/**
	 * @api {POST} /consultasidra/getbodalmacen/ [getbodalmacen]
	 * @apiName getbodalmacen
	 * @apiDescription Servicio para obtener las bodegas asociadas a los
	 *                 distribuidores de un pa\u00EDs determinado.
	 * @apiGroup Bodegas_Almacen
	 * @apiVersion 1.0.0
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} idBodega Identificador de la bodega a consultar. Este
	 *           campo es opcional.
	 * @apiParam {String} bodegaSCL Nombre de la bodega del sistema comercial que se
	 *           ha asociado al distribuidor. Este campo es opcional.
	 * @apiParam {String} nombre Nombre de la bodega DTS. Este campo es opcional.
	 * @apiParam {String} distribuidor Identificador del distribuidor asociado a
	 *           bodega. Este campo es opcional.
	 * @apiParam {String} estado Estado de la bodega. Este campo es opcional.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas", "idBodega": "",
	 *                  "bodegaSCL": "", "nombre": "", "distribuidor": "", "estado":
	 *                  "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "bodegaDTS": {
	 *                    "bodegaSCL": "3", "bodegaId": "1", "nombre": "BODEGA 1",
	 *                    "distribuidor": "1", "estado": "ALTA", "usuario":
	 *                    "usuario.pruebas", "creado_el": "2015-10-22 14:20:26.0",
	 *                    "creado_por": "usuario.pruebas", "modificado_el":
	 *                    "2015-10-22 14:27:28.0", "modificado_por":
	 *                    "usuario.pruebas" } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase": "OperacionBodegaDTS",
	 *                  "metodo": "doGet", "excepcion": " ", "tipoExepcion": " " } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputBodegaDTS consultaBodegaDTS(@Context HttpServletRequest req, InputBodegaDTS input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputBodegaDTS response = new BodegaAlmacen().getBodegas(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getcatalogo/")
	/**
	 * @api {POST} /consultasidra/getcatalogo/ [getConfiguracionCatalogo]
	 *
	 * @apiName getConfiguracionCatalogo
	 * @apiDescription Servicio para obtener los grupos de catalogos configurados
	 *                 por pa\u00EDs
	 * @apiGroup Configuraciones
	 * @apiVersion 1.0.0
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} grupoParametro Nombre del grupo de par\u00E9metros que se
	 *           desea obtener. Este campo es opcional.
	 * @apiParam {String} nombre Nombre del par\u00E9metro que se desea obtener.
	 *           Este campo es opcional.
	 * @apiParam {String} estado Estado del par\u00E9metro que se desea obtener.
	 *           Este campo es opcional.
	 * @apiParam {String} tipoParametro Tipo del par\u00E9metro que se desea
	 *           obtener. Este campo es opcional (1 = Interno o 0 = Externo).
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "usuario":
	 *                  "usuario.pruebas", "grupoParametro": "", "par\u00E9metros":
	 *                  [ { "nombre": "", "estado": "", "tipoParametro": "" } ] }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "1", "descripcion":
	 *                    "Recursos recuperados exitosamente.", "clase": " ",
	 *                    "metodo": " ", "excepcion": " ", "tipoExepcion": " " },
	 *                    "grupoParametro":"Grupo1", "grupo": [ { "grupo": "Grupo
	 *                    1", "par\u00E9metros": [ { "codArea": "505", "grupo":
	 *                    "Grupo 1", "nombre": "Nombre 1", "valor": "Valor 1",
	 *                    "descripcion": "Descripcion 1", "estado": "ALTA",
	 *                    "creado_el": "2015-10-22 14:20:26.0", "creado_por":
	 *                    "usuario.pruebas", "modificado_el": "2015-10-22
	 *                    14:27:28.0", "modificado_por": "usuario.pruebas" }, {
	 *                    "codArea": "505", "grupo": "Grupo 1", "nombre": "Nombre
	 *                    2", "valor": "Valor 2", "descripcion": "Descripcion 2",
	 *                    "estado": "ALTA", "creado_el": "2015-10-22 14:20:26.0",
	 *                    "creado_por": "usuario.pruebas", "modificado_el":
	 *                    "2015-10-22 14:27:28.0", "modificado_por":
	 *                    "usuario.pruebas" } ] }, { "grupo": "Grupo 2",
	 *                    "par\u00E9metros": { "codArea": "505", "grupo": "Grupo 2",
	 *                    "nombre": "Nombre 2", "valor": "Valor 2", "descripcion":
	 *                    "Descripcion 2", "estado": "ALTA", "creado_el":
	 *                    "2015-10-22 14:20:26.0", "creado_por": "usuario.pruebas",
	 *                    "modificado_el": "2015-10-22 14:27:28.0",
	 *                    "modificado_por": "usuario.pruebas" } } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase": "OperacionCatalogo",
	 *                  "metodo": "doGet", "excepcion": " ", "tipoExepcion": " " } }
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputCatalogo consultaCatalogo(@Context HttpServletRequest req, InputCatalogo input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputCatalogo response = new Catalogos().getCatalogo(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getImagen/")
	/**
	 * @api {POST} /consultasidra/getImagen/ [ObtenerImagen]
	 * 
	 * @apiName getImagen
	 * @apiDescription Servicio para obtener imagen almacenada asociada a un punto
	 *                 de venta.
	 * @apiGroup ImagenPDV
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} idImgPDV Id del registro que se desea consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "token": "WEB", "usuario": "usuario.pruebas",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idImgPDV": "4" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0" }, "imagen": {
	 *                    "idPDV": "32", "archivo":
	 *                    "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkG", "nombreArchivo":
	 *                    "fotografia", "extension": ".jpg", "creado_el":
	 *                    "27/05/2016 09:28:39", "creado_por": "usuario.pruebas" } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase": "OperacionCargaFile",
	 *                  "metodo": "getImagenPDV", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputImagen getImagen(@Context HttpServletRequest req, InputCargaFile input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputImagen response = new CargaFile().getCargaFile(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("getdts")
	/**
	 * @api {POST} /consultasidra/getdts/ [getDistribuidor]
	 * 
	 * @apiName getDistribuidor
	 * @apiDescription Servicio para obtener los datos de distribuidores internos o
	 *                 externos configurados por pa\u00EDs.
	 * @apiGroup Distribuidor
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se
	 *           desea obtener informaci\u00F3n .
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} [idBodegaVirtual] Identificador de la bodega virtual.
	 * @apiParam {String} [tipo] Nombre del tipo de distribuidor.
	 * @apiParam {String} [idfichacliente] Identificador de la ficha del cliente del
	 *           sistema comercial.
	 * @apiParam {String} [nombres] Nombre o nombres del distribuidor que se desea
	 *           obtener.
	 * @apiParam {String} [numero] N\u00FAmero del distribuidor que se desea
	 *           obtener.
	 * @apiParam {String} [email] Email del distribuidor que se desea consultar.
	 * @apiParam {String} [administrador] Identificador del usuario del m\u00F3dulo
	 *           de seguridad que es asociado a un distribuidor.
	 * @apiParam {String} [pagoautomatico="1 o 0"] Dato boolean que indica si el
	 *           distribuidor realiza pago autom\u00E9tico.
	 * @apiParam {String} [canal] Canal del distribuidor a buscar.
	 * @apiParam {String} [numConvenio] N\u00FAmero de convenio del distribuidor a
	 *           buscar.
	 * @apiParam {String} [codCliente] C\u00F3digo de cliente del distribuidor a
	 *           buscar.
	 * @apiParam {String} [codCuenta] C\u00F3digo de cuenta del distribuidor a
	 *           buscar.
	 * @apiParam {String} [resultadoSCL] Resultado de ficha de cliente.
	 * @apiParam {String} [estado="ALTA o BAJA"] Estado del cu\u00E9l se desea
	 *           obtener distribuidores.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas", "idBodegaVirtual": "",
	 *                  "tipo": "", "idfichacliente": "", "nombres": "", "numero":
	 *                  "", "email": "", "pagoautomatico": "", "canal": "",
	 *                  "numConvenio": "", "codCliente": "", "codCuenta": "",
	 *                  "resultadoSCL": "", "estado": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "distribuidor": [
	 *                    { "idDTS": "36", "tipo": "INTERNO", "idfichacliente":
	 *                    "2202", "idBodegaVirtual": "73", "idAlmacenBod": "76",
	 *                    "idBodegaSCL": "542", "nombres": "daniel", "numero":
	 *                    "58320096", "email": "daniel@tobar.com",
	 *                    "administrador":"122", "pagoautomatico": "1", "canal": "",
	 *                    "numConvenio": "", "codCliente": "", "codCuenta": "",
	 *                    "resultadoSCL": "0", "estado": "ALTA", "creado_el":
	 *                    "09/12/2015 16:27:05", "creado_por": "sergio.lujan" }, {
	 *                    "idDTS": "33", "tipo": "INTERNO", "idfichacliente":
	 *                    "1542", "idBodegaVirtual": "63", "idAlmacenBod": "52",
	 *                    "idBodegaSCL": "1108", "nombres": "DIRCAM", "numero":
	 *                    "71806642", "email": "vladimir.rodriguez@telefonica.com",
	 *                    "administrador":"122", "pagoautomatico": "1", "canal": "",
	 *                    "numConvenio": "", "codCliente": "", "codCuenta": "",
	 *                    "resultadoSCL": "0", "estado": "ALTA", "creado_el":
	 *                    "08/12/2015 17:09:52", "creado_por": "vladimir.rodriguez"
	 *                    } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-101", "mostrar": "0", "descripcion": "Ocurrio un Problema
	 *                  inesperado, contacte a su Administrador.", "clase":
	 *                  "OperacionDistribuidor", "metodo": "doGet", "excepcion": "
	 *                  ", "tipoExepcion": " " } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputDistribuidor getdts(@Context HttpServletRequest req, InputDistribuidor input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputDistribuidor response = new Distribuidor().getDTS(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getbodegavirtual")
	/**
	 * @api {POST} /consultasidra/getbodegavirtual/ [getBodegaVirtual]
	 * 
	 * @apiName getBodegaVirtual
	 * @apiDescription Servicio para obtener las bodegas virtuales asociadas a los
	 *                 distribuidores de un pa\u00EDs determinado.
	 * @apiGroup Bodegas_Virtuales
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se
	 *           desea obtener informaci\u00F3n .
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} idBodega Identificador de la bodega virtual. Este campo es
	 *           opcional.
	 * @apiParam {String} nombre Nombre de la bodega virtual que se desea obtener.
	 *           Este campo es opcional.
	 * @apiParam {String} idDTS Id del distribuidor al que se encuentra asignada una
	 *           bodega virtual. Este campo es opcional.
	 * @apiParam {String} idRuta Id de la Ruta a la que se encuentra asignada una
	 *           bodega virtual. Este campo es opcional.
	 * @apiParam {String} idPanel Id de la Panel a la que se encuentra asignada una
	 *           bodega virtual. Este campo es opcional.
	 * @apiParam {String} nivel indicar\u00E9 el nivel de bodega a buscar. Este
	 *           campo es opcional.
	 * @apiParam {String} Indica el tipo de bodega de nivel 0, es obligatorio al
	 *           crear bodegas de nivel 0.
	 * @apiParam {String} idBodegaPadre id de la bodega padre de bodegas virtuales a
	 *           buscar. Este campo es opcional.
	 * @apiParam {String} estado Estado de bodegas que se desa obtener. Este campo
	 *           es opcional.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas", "idBodega": "3",
	 *                  "nombre": "BODEGA VIRTUAL", "idDTS": "2", "idRuta": "",
	 *                  "idPanel": "", "nivel":"1", "tipo": "PRINCIPAL",
	 *                  "idBodegaPadre":"", "estado": "ALTA" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "bodegaVirtual": [
	 *                    { "bodegaId": "1", "nombre": "BODEGA VIRTUAL 1 DTS 1",
	 *                    "idBodegaPadre": "42", "nivel": "2", "estado": "ALTA",
	 *                    "usuario": "usuario.pruebas", "latitud": "1825.15",
	 *                    "longitud": "7.21", "creado_el": "2015-10-22 14:20:26.0",
	 *                    "creado_por": "usuario.pruebas", "modificado_el":
	 *                    "2015-10-22 14:27:28.0", "modificado_por":
	 *                    "usuario.pruebas" }, { "bodegaId": "2", "nombre": "BODEGA
	 *                    VIRTUAL 2 DTS 1", "idBodegaPadre": "", "nivel": "1",
	 *                    "estado": "ALTA", "usuario": "usuario.pruebas",
	 *                    "creado_el": "2015-10-22 14:20:26.0", "creado_por":
	 *                    "usuario.pruebas" } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase":
	 *                  "OperacionBodegaVirtual", "metodo": "doGet", "excepcion": "
	 *                  ", "tipoExepcion": " " } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputBodegaVirtual doGetBodegaVirtual(@Context HttpServletRequest req, InputBodegaVirtual input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputBodegaVirtual response = new BodegaVirtual().getBodegaVirtual(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getfoliobodvirtual")
	/**
	 * @api {POST} /consultasidra/getfoliobodvirtual/ [getConfiguracionFolioVirtual]
	 * 
	 * @apiName getConfiguracionFolioVirtual
	 * @apiDescription Servicio para obtener las configuraciones de folios asociadas
	 *                 a las bodegas virtuales de los distribuidores de un pa\u00EDs
	 *                 determinado.
	 * @apiGroup Folios
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se
	 *           desea obtener informaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} idTipo Identificador del dispositivo (MAC ADDRESS). Este
	 *           campo es opcional.
	 * @apiParam {String} tipo Nombre del tipo (DISPOSITIVOS). Este campo es
	 *           opcional.
	 * @apiParam {String} estado Estado del folio asociado.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas", "idTipo":
	 *                  "11:22:33:44:55", "tipo": "DISPOSITIVOS", "estado": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." },
	 *                    "configuracionFolio": { "usuario": "usuario.pruebas",
	 *                    "idTipo": "11:22:33:44:55", "tipo": "DISPOSITIVOS",
	 *                    "folios": [ { "idFolio": "101", "tipoDocumento":
	 *                    "FACTURA", "serie": "A", "noInicialFolio": "1",
	 *                    "noFinalFolio": "50", "cant_utilizados": "0",
	 *                    "ultimo_utilizado": "", "folio_siguiente": "1", "estado":
	 *                    "ALTA", "caja_numero": "7845", "zona": "13", "creado_el":
	 *                    "02/12/2015 16:35:37", "creado_por": "usuario.pruebas" },
	 *                    { "idFolio": "102", "tipoDocumento": "FACTURA", "serie":
	 *                    "A", "noInicialFolio": "51", "noFinalFolio": "100",
	 *                    "cant_utilizados": "1", "ultimo_utilizado": "51",
	 *                    "folio_siguiente": "52", "estado": "EN_USO",
	 *                    "caja_numero": "4564", "zona": "12", "creado_el":
	 *                    "02/12/2015 16:35:37", "creado_por": "usuario.pruebas" } ]
	 *                    } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-394", "mostrar": "1", "descripcion": "No se encontraron
	 *                  folios configurados.", "clase": "OperacionFolioVirtual",
	 *                  "metodo": "doGet", "excepcion": " ", "tipoExepcion": " " } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputConfiguracionFolioVirtual getFolioBodVirtual(@Context HttpServletRequest req,
			InputFolioVirtual input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputConfiguracionFolioVirtual response = new FolioRutaPanel().getFolioBodVirtual(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getRuta")
	/**
	 * @api {POST} /consultasidra/getRuta/ [getRuta]
	 * 
	 * @apiName getRuta
	 * @apiDescription Servicio para obtener los datos de Rutas configuradas.
	 * @apiGroup Rutas
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se
	 *           desea obtener informaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} idRuta Identificador de la ruta que se desea obtener.
	 * @apiParam {String} [idDTS] Identificador del distribuidor que se desea
	 *           obtener.
	 * @apiParam {String} [nombreRuta] Nombre de la ruta que se desea obtener.
	 * @apiParam {String} [secUsuarioId] Identificador del vendedor o usuario del
	 *           modulo de seguridad.
	 * @apiParam {String} [idBodegaVirtual] Identificador de la bodega virtual que
	 *           se desea consultar.
	 * @apiParam {String} [estado] Estado del cu\u00E9l se desea obtener Rutas (ALTA
	 *           o BAJA).
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas", "idRuta": "", "idDTS":
	 *                  "", "nombreRuta": "", "secUsuarioId": "", "idBodegaVirtual":
	 *                  "", "estado": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "ruta": {
	 *                    "idBodegaVirtual": "26", "idRuta": "1", "nombreRuta":
	 *                    "RUTA 1", "idDTS": "1", "nombreDTS": "Mariel L\u00F3pez",
	 *                    "tipoDTS": "INTERNO", "secUsuarioId": "1",
	 *                    "idBodegaVendedor": "63", "estado": "ALTA", "creado_el":
	 *                    "25/10/2016 14:43:52", "creado_por": "usuario.pruebas",
	 *                    "datosVendedor": { "idVendPanelPDV": "", "vendedor":
	 *                    "2622", "nombre": "bernabe.santos", "cantInventario": "12"
	 *                    }, "puntoVenta": { "idPDV": "3", "nombrePDV": "TIENDA
	 *                    LUCY" } } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase": "OperacionRuta",
	 *                  "metodo": "doGet", "excepcion": " ", "tipoExepcion": " " } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputRuta doGetRuta(@Context HttpServletRequest req, InputRuta input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputRuta response = new Ruta().getRuta(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getpanel")
	/**
	 * @api {POST} /consultasidra/getpanel/ [getPanel]
	 * 
	 * @apiName getPanel
	 * @apiDescription Servicio para obtener los datos de Paneles configuradas.
	 * @apiGroup Panel
	 * @apiVersion 1.0.0
	 * @apiParam {String} token Token de autenticaci\u00F3n de sesion de usuario.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se
	 *           desea obtener informaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [idPanel] Identificador de la Panel que se desea obtener.
	 * @apiParam {String} [idDistribuidor] Identificador del distribuidor que se
	 *           desea obtener.
	 * @apiParam {String} [idBodegaVirtual] Identificador de la bodega virtual que
	 *           se desea consultar.
	 * @apiParam {String} [estado] Estado de la panel a consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "token": "WEB", "codDispositivo": "", "usuario":
	 *                  "usuario.pruebas", "idPanel": "", "idDistribuidor": "",
	 *                  "idBodegaVirtual": "", "estado": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token": "WEB",
	 *                    "respuesta": { "codResultado": "0", "mostrar": "0",
	 *                    "descripcion": "Recursos recuperados exitosamente." },
	 *                    "panel": { "idPanel": "1", "nombre": "PANEL 1",
	 *                    "idDistribuidor": "1", "idBodegaVirtual": "25",
	 *                    "nombreDTS": "Victor Cifuentes", "tipoDTS": "EXTERNO",
	 *                    "responsable": "2010", "nombreResponsable":
	 *                    "usuario.sidra", "idBodResponsable": "96",
	 *                    "nombreBodResponsable": "BODEGA VENDEDOR 2010", "estado":
	 *                    "ALTA", "creado_el": "2015-10-26 10:26:07.0",
	 *                    "creado_por": "usuario.pruebas", "datosVendedor": [{
	 *                    "idVendPanelPDV": "2010", "nombre": "usuario.sidra",
	 *                    "cantInventario": "0", "estado": "ALTA" }] } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase": "OperacionPanel",
	 *                  "metodo": "doGet", "excepcion": " ", "tipoExepcion": " " } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputPanel getPanel(@Context HttpServletRequest req, InputPanel input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputPanel response = new Panel().getPanel(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getpdv")
	/**
	 * @api {POST} /consultasidra/getpdv/ [getPDV]
	 * 
	 * @apiName getPDV
	 * @apiDescription Servicio para obtener los datos de PDVs configuradas.
	 * @apiGroup Puntos_de_Venta
	 * @apiVersion 1.0.0
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} [idPDV] Identificador de la PDV que se desea obtener.
	 * @apiParam {String} [idDTS] Identificador del distribuidor asociado al punto
	 *           de venta que se desea obtener.
	 * @apiParam {String} [idVendedor] Identificador del vendedor asociado al punto
	 *           de venta que se desea obtener.
	 * @apiParam {String} [idRuta] Identificador de la ruta asociada que se desea
	 *           obtener.
	 * @apiParam {String} [numRecarga] N\u00FAmero de recarga del pdv, del cual se
	 *           desea obtener informaci\u00F3n.
	 * @apiParam {String} [ruc_nit] Identificaci\u00F3n tributaria del punto de
	 *           venta a traves del cual se desea buscar el pdv.
	 * @apiParam {String} [categoria] Categor\u00EDa del punto de venta que se desea
	 *           consultar.
	 * @apiParam {String} [departamento] Departamento del punto de venta que se
	 *           desea consultar.
	 * @apiParam {String} [municipio] Municipio del punto de venta que se desea
	 *           consultar.
	 * @apiParam {String} [distrito] Distrito del punto de venta que se desea
	 *           consultar.
	 * @apiParam {String} [estado] Estado del punto de venta.
	 * @apiParam {String} [min] Registro m\u00EDnimo a consultar para pagineo.
	 * @apiParam {String} [max] Registro m\u00E9ximo a consultar para pagineo.
	 * @apiParam {String} [mostrarNumerosRecarga="1 o 0"] Valor boolean que indica
	 *           si se deben obtener y mostrar los numeros de recarga (1 = S\u00ED,
	 *           0 = No).
	 * @apiParam {String} [mostrarDiasVisita="1 o 0"] Valor boolean que indica si se
	 *           deben obtener y mostrar los d\u00EDas de visita (1 = S\u00ED, 0 =
	 *           No).
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token":"WEB",
	 *                  "usuario": "usuario.pruebas", "codDispositivo":
	 *                  "64E1CE4A48BF656974C376B7905F7E62", "idPDV": "1", "idDTS":
	 *                  "", "idVendedor": "", "idRuta": "", "numRecarga": "",
	 *                  "ruc_nit": "", "categoria": "", "departamento": "",
	 *                  "municipio": "", "distrito": "", "estado": "", "min": "",
	 *                  "max": "", "mostrarNumerosRecarga": "1",
	 *                  "mostrarDiasVisita": "0" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "PuntoDeVenta": [
	 *                    { "codArea": "505", "idPDV": "41", "tipoProducto":
	 *                    "FISICO", "nombrePDV": "Prueba creacion3", "canal":
	 *                    "SIDRA", "subcanal": "DTS", "categoria": "A",
	 *                    "digitoValidador": "", "distribuidorAsociado": "5",
	 *                    "nombreDTS": "DTS Uno", "tipoDTS": "EXTERNO",
	 *                    "tipoNegocio": "FARMACIA", "documento": "NIT", "nit":
	 *                    "54545454545", "nombreFiscal": "EE", "registroFiscal":
	 *                    "EE", "giroNegocio": "VENTAS POR MENOR",
	 *                    "tipoContribuyente": "Pequenio Contribuyente", "calle":
	 *                    "2-20", "avenida": "5ta av.", "pasaje": "123", "colonia":
	 *                    "Colonia Monja Blanca", "referencia": "Frente a Casa 2
	 *                    niveles port\u00F3n blanco", "barrio": "", "direccion":
	 *                    "ZONA 10, GUATEMALA", "zonaComercial": "1",
	 *                    "departamento": "GUATEMALA", "municipio": "GUATEMALA",
	 *                    "distrito": "CENTRAL", "observaciones": "estoy creando un
	 *                    pdv :D", "latitud": "14.581026", "longitud": "90.587321",
	 *                    "estado": "ACTIVO", "encargado": { "idEncargadoPDV": "1",
	 *                    "nombres": "LUISA FERNANDA", "apellidos": "ROSALES
	 *                    MENDEZ", "telefono": "45124422", "cedula": "" },
	 *                    "imgAsociadas": [ { "idImgPDV": "3" }, { "idImgPDV": "10"
	 *                    } ], "creado_el": "30/05/2016 10:09:41", "creado_por":
	 *                    "usuario", "modificado_el": "30/05/2016 16:28:09",
	 *                    "modificado_por": "usuario", "numerosRecarga": [ {
	 *                    "numero": "69857744", "orden": "1", "estado": "ALTA",
	 *                    "estadoPayment": "PENDIENTE", "idSolicitud": "44" }, {
	 *                    "numero": "96325588", "orden": "2", "estado": "ALTA",
	 *                    "estadoPayment": "PENDIENTE", "idSolicitud": "44" } ],
	 *                    "diasVisita": [ { "idDiaVisita": "255", "nombre": "LUNES",
	 *                    "estado": "ALTA" }, { "idDiaVisita": "256", "nombre":
	 *                    "MARTES", "estado": "ALTA" }, { "idDiaVisita": "257",
	 *                    "nombre": "MI\u00E9RCOLES", "estado": "ALTA" } ] } ] }
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-395", "mostrar": "1", "descripcion": "No se encontraron
	 *                  puntos de venta configurados.", "clase": "OperacionPDV",
	 *                  "metodo": "doGet", "excepcion": " ", "tipoExepcion": "" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputpdvDirec doGetPDV(@Context HttpServletRequest req, InputConsultaPDV input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputpdvDirec response = new PuntoVenta().getPDV(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getcountpdv")
	/**
	 * @api {POST} /consultasidra/getcountpdv/ [getcountpdv]
	 * 
	 * @apiName getcountpdv
	 * @apiDescription Servicio para contar los PDV's configurados en el sistema.
	 * @apiGroup Puntos_de_Venta
	 * @apiVersion 1.0.0
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} [idPDV] Identificador de la PDV que se desea obtener.
	 * @apiParam {String} [idDTS] Identificador del distribuidor asociado al punto
	 *           de venta que se desea obtener.
	 * @apiParam {String} [idVendedor] Identificador del vendedor asociado al punto
	 *           de venta que se desea obtener.
	 * @apiParam {String} [idRuta] Identificador de la ruta asociada que se desea
	 *           obtener.
	 * @apiParam {String} [categoria] Categor\u00EDa del punto de venta que se desea
	 *           consultar.
	 * @apiParam {String} [departamento] Departamento del punto de venta que se
	 *           desea consultar.
	 * @apiParam {String} [municipio] Municipio del punto de venta que se desea
	 *           consultar.
	 * @apiParam {String} [distrito] Distrito del punto de venta que se desea
	 *           consultar.
	 * @apiParam {String} [estado] Estado del punto de venta.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token":"WEB",
	 *                  "usuario": "usuario.pruebas", "codDispositivo":
	 *                  "64E1CE4A48BF656974C376B7905F7E62", "idPDV": "1", "idDTS":
	 *                  "", "idRuta": "", "categoria": "", "departamento": "",
	 *                  "municipio": "", "distrito": "", "estado": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "cantRegistros":
	 *                    "1" }
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-612", "mostrar": "1", "descripcion": "No se encontro
	 *                  informaci\u00F3n con los datos recibidos.", "clase":
	 *                  "OperacionPDV", "metodo": "doGet", "excepcion": " ",
	 *                  "tipoExepcion": "" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputpdvDirec doGetCountPDV(@Context HttpServletRequest req, InputConsultaPDV input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputpdvDirec response = new PuntoVenta().getCountPDV(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/**************************************************************************************
	 * SERVICIOS PARA OBTENER ART\u00EDCULOS
	 *************************************************************************************/
	@Path("/getArticulos")
	/**
	 * @api {POST} /consultasidra/getArticulos/ [getArticulos]
	 * 
	 * @apiName getArticulos
	 * @apiDescription Servicio para obtener los datos de art\u00EDculos del sistema
	 *                 comercial.
	 * @apiGroup Articulos
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se
	 *           desea obtener informaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "1", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "articulos": [ {
	 *                    "idArticulo": "403", "codArticulo": "71", "descArticulo":
	 *                    "ALCATEL 156A" }, { "idArticulo": "439", "codArticulo":
	 *                    "13", "descArticulo": "TERMINAL GSM PRUEBA" } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: Usuario.", "clase":
	 *                  "CtrlArtriculo", "metodo": "validarInput", "excepcion": " ",
	 *                  "tipoExepcion": "" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputConsultaArticulos doGetArticulo(@Context HttpServletRequest req, InputConsultaArticulos input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputConsultaArticulos response = new OfertaComercial().getArticulos(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getPrecioArticulo")
	/**
	 * @api {POST} /consultasidra/getPrecioArticulo/ [getPrecioArticulo]
	 * 
	 * @apiName getPrecioArticulo
	 * @apiDescription Servicio para obtener los datos de art\u00EDculos del sistema
	 *                 comercial.
	 * @apiGroup Articulos
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs del que se
	 *           desea obtener informaci\u00F3n.
	 * @apiParam {String} idArticulo Identificador del art\u00EDculo a buscar.
	 * @apiParam {String} tipoGestion Nombre del tipo de gesti\u00F3n. Este campo es
	 *           aplicable \u00FAnicamente a El Salvador, enviar vacio en los
	 *           dem\u00E9s paises.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas", "idArticulo": "1894",
	 *                  "tipoGestion": "ALTA" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "1", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "articulos": [ {
	 *                    "descArticulo": "GSM-ALCATEL OT-720", "desTipoPrecio":
	 *                    "PACK PREPAGO INDIVIDUAL", "precioArticulo": "57.5221" } ]
	 *                    }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: C\u00F3digo de Art\u00EDculo.",
	 *                  "clase": "CtrlArtriculo", "metodo": "validarInput",
	 *                  "excepcion": " ", "tipoExepcion": "" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputConsultaArticulos doGetPrecioArticulo(@Context HttpServletRequest req, InputConsultaArticulos input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputConsultaArticulos response = new OfertaComercial().getPrecioArt(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/*************************************************************************************************************
	 * OBTENER BUZONES
	 ***********************************************************************************************************/
	@Path("/getbuzones")
	/**
	 * @api {POST} /consultasidra/getbuzones/ [getBuzones]
	 * 
	 * @apiName getBuzones
	 * @apiDescription Servicio para obtener informaci\u00F3n de los buzones
	 *                 existentes, al no ingresar ning\u00FAn valor en el input, se
	 *                 obtendr\u00E9n todos los buzones registrados.
	 * @apiGroup Buzones
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea c\u00F3digo de area del pa\u00EDs que se desea
	 *           obtener la informaci\u00F3n.
	 * @apiParam {String} usuario nombre de usuario que realiza la operacion.
	 * @apiParam {String} idBuzon id del buzon que se desea obtener. CAMPO NO
	 *           OBLIGATORIO.
	 * @apiParam {String} nombre Nombre de buz\u00F3n a buscar. CAMPO NO
	 *           OBLIGATORIO.
	 * @apiParam {String} idDistribuidor id del distribuidor al que se
	 *           encontrar\u00E9 asociado el buzon. Este solo se ingresar\u00E9 en
	 *           caso que el buz\u00F3n a registrar sea de nivel 2.
	 * @apiParam {String} idBodegaVirtual id de la bodega virtual a la que se desea
	 *           consultar. Este solo se ingresar\u00E9 en caso que el buz\u00F3n a
	 *           registrar sea de nivel 3.
	 * @apiParam {String} [nivel="1 o 2 o 3"] nivel del distribuidor a crear .
	 * @apiParam {String} tipoWF Indica que tipo de solicitud atender\u00E9 el
	 *           buzon. Ejemplo: DEVOLUCION, SINIESTRO o TODAS.
	 * @apiParam {String} estado estado actual que se desea buscar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario":"usuarioprueba", "idBuzon":"", "nombre":
	 *                  "", "idDistribuidor": "", "idBodegaVirtual": "", "nivel":
	 *                  "", "tipoWF": "", "estado": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "1", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" }, "buzones": [ {
	 *                    "idBuzon": "2", "idDistribuidor":"",
	 *                    "nombreDistribuidor":"", "idBodegaVirtual":"",
	 *                    "nombreBodega":"", "nombre": "Logistica", "nivel":"",
	 *                    "tipoWF":"", "estado": "ALTA", "creado_por":
	 *                    "usuario.pruebas", "creado_el": "08/12/2015 00:00:00",
	 *                    "modificado_por": " ", "modificado_el": " " }, {
	 *                    "idBuzon": "8", "idDistribuidor":"",
	 *                    "nombreDistribuidor":"", "idBodegaVirtual":"",
	 *                    "nombreBodega":"", "nombre": "Buzon2", "nivel":"",
	 *                    "tipoWF":"", "estado": "ALTA", "creado_por":
	 *                    "usuarioprueba", "creado_el": "08/12/2015 00:00:00",
	 *                    "modificado_por": " ", "modificado_el": " " } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-392", "mostrar": "1", "descripcion": "No se encontraron
	 *                  buzones configurados.", "clase": "CtrlBuzonSidra", "metodo":
	 *                  "getBuzon", "excepcion": " ", "tipoExepcion": "Generales" }
	 *                  }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputBuzon getBuzones(@Context HttpServletRequest req, InputBuzon input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputBuzon response = new BuzonSidra().getBuzon(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/**
	 * SERVICIO PARA MOSTRAR SOLICITUDES DE WORK FLOW
	 */
	@Path("/getsolicitud")
	/**
	 * @api {POST} /consultasidra/getsolicitud/ [getSolicitud]
	 * 
	 * @apiName getSolicitud
	 * @apiDescription Servicio para obtener los datos de solicitudes por pais.
	 * @apiGroup Solicitud
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} token token de autenticaci\u00F3n de sesion de usuario.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [idSolicitud] Identificador de la solicitud que se desea.
	 * @apiParam {String} [origen="MOVIL o PC"] Origen de las solicitudes que desea
	 *           consultar.
	 * @apiParam {String} [idVendedor] Identificador del vendedor que se desea
	 *           consultar en la solicitud.
	 * @apiParam {String} [idBodega] Identificador de la bodega que se desea
	 *           consultar.
	 * @apiParam {String} [idDTS] Identificador del Distribuidor que se desea
	 *           consultar en la solicitud.
	 * @apiParam {String} [idBuzon] Identificador del buz\u00F3n que se desea
	 *           consultar en la solicitud.
	 * @apiParam {String} [idBuzonAnterior] Identificador del buz\u00F3n anterior en
	 *           que se encontraba la solicitud a consultar.
	 * @apiParam {String} [fechaInicio] Fecha en formato yyyyMMdd desde la cu\u00E9l
	 *           se desea consultar la solicitud.
	 * @apiParam {String} [fechaFin] Fecha en formato yyyyMMdd hasta la cu\u00E9l se
	 *           desea consultar la solicitud.
	 * @apiParam {String} [tipoSolicitud="DEVOLUCION, RESERVA, PEDIDO,
	 *           NUMEROS_PAYMENT, SINIESTRO"] Nombre del tipo de solicitud que se
	 *           desea consultar.
	 * @apiParam {String} [tipoSiniestro="TOTAL, PARCIAL o DISPOSITIVO"] Nombre del
	 *           tipo de siniestro que se desea consultar.
	 * @apiParam {String} [seriado="1 o 0"] Tipo de solicitud que se desea, 1
	 *           seriadas o 0 no seriadas.
	 * @apiParam {String} [idJornada] Identificador de la jornada que se desea
	 *           consultar en la solicitud.
	 * @apiParam {String} [idTipo] Identificador del tipo asociado que se desea
	 *           consultar en la solicitud.
	 * @apiParam {String} [tipo="RUTA, PANEL o PDV"] Tipo asociado que se desea
	 *           consultar en la solicitud.
	 * @apiParam {String} [creado_por] Nombre del usuario que cre\u00F3 la solicitud
	 *           que se desea consultar.
	 * @apiParam {String} [estado] Estado de la solicitud que se desea consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token":"WEB",
	 *                  "codArea":"505", "usuario":"usuarioprueba",
	 *                  "idSolicitud":"", "origen":"", "idVendedor":"",
	 *                  "idBodega":"", "idDTS":"", "idBuzon":"", "idBuzonAnterior":
	 *                  "", "fechaInicio":"", "fechaFin":"", "tipoSolicitud":"",
	 *                  "tipoSiniestro":"", "seriado":"", "idJornada":"",
	 *                  "idTipo":"", "tipo":"", "creado_por":"", "estado": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "solicitudes": {
	 *                    "tipoSolicitud": "SINIESTRO", "listaSolicitudes": [{
	 *                    "idSolicitud": "91", "idBodega": "114", "idDTS": "22",
	 *                    "nombreDTS": "DTS TECNOLOGIA S.A", "idBuzon": "11",
	 *                    "nombreBuzon": "Siniestros", "idBuzonAnterior": "",
	 *                    "fecha": "03/08/2016 00:00:00", "idVendedor": "961",
	 *                    "nombreVendedor": "Daniel", "apellidoVendedor": "Estevez",
	 *                    "usuarioVendedor": "99887768", "tipoSolicitud":
	 *                    "SINIESTRO", "tipoSiniestro": "TOTAL", "causaSolicitud":
	 *                    "", "idJornada": "70", "idTipo": "24", "tipo": "RUTA",
	 *                    "nombreTipo": "RUTA TECH S.A.", "observaciones":
	 *                    "Observaciones payment", "seriado": "0", "origen": "PC",
	 *                    "totalDeuda": "", "tasaCambio": "", "estado": "CERRADA",
	 *                    "origenCancelacion":"", "obsCancelacion":"" "creado_el":
	 *                    "03/08/2016 10:00:11", "creado_por": "victor.cifuentes",
	 *                    "modificado_el": "04/08/2016 11:53:26", "modificado_por":
	 *                    "sergio.lujan", "articulos": [ { "idSolicitudDet": "102",
	 *                    "codDispositivo": "", "idArticulo": "91", "descripcion":
	 *                    "TARJETA SIM PREPAGO 2FF/3FF", "serie":
	 *                    "8950702310512556368", "serieFinal": "NULL",
	 *                    "serieAsociada": "", "cantidad": "1", "observaciones":
	 *                    "Rechazada", "estado": "ACEPTADA", "creado_el":
	 *                    "03/08/2016 10:00:11", "creado_por": "victor.cifuentes",
	 *                    "modificado_el": "04/08/2016 11:53:26", "modificado_por":
	 *                    "sergio.lujan" }, { "idSolicitudDet": "103",
	 *                    "codDispositivo": "", "idArticulo": "12", "descripcion":
	 *                    "PACHON AZUL", "serie": "NULL", "serieFinal": "NULL",
	 *                    "serieAsociada": "", "cantidad": "200", "observaciones":
	 *                    "Rechazada", "estado": "ACEPTADA", "creado_el":
	 *                    "03/08/2016 10:00:11", "creado_por": "victor.cifuentes",
	 *                    "modificado_el": "04/08/2016 11:53:26", "modificado_por":
	 *                    "sergio.lujan" } ], "obsSolicitud": { "observacion":
	 *                    "observacion", "creado_el": "16/03/2017 22:44:05",
	 *                    "creado_por": "usuario.pruebas" }, "remesas": [ {
	 *                    "idRemesa": "99", "monto": "2944.45", "tasaCambio":
	 *                    "29.4445", "noBoleta": "A5", "banco": "BAC", "estado":
	 *                    "ALTA", "creado_el": "15/03/2017 19:53:29", "creado_por":
	 *                    "usuario.pruebas", "modificado_el": "", "modificado_por":
	 *                    "" } ], "detallePagos": [ { "formaPago": "EFECTIVO",
	 *                    "monto": "1194987.510026", "estado": "BAJA", "creado_el":
	 *                    "05/06/2017 11:22:01", "creado_por": "pablo.lopez",
	 *                    "modificado_el": "05/06/2017 12:45:49", "modificado_por":
	 *                    "pablo.lopez" } ] }] } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase": "OperacionSolicitud",
	 *                  "metodo": "doGet", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputSolicitud doGetSolicitud(@Context HttpServletRequest req, InputSolicitud input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputSolicitud response = new SolicitudWorkFlow().getSolicitud(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getusuariobuzon")
	/**
	 * @api {POST} /consultasidra/getusuariobuzon/ [getUsuarioBuzon]
	 * 
	 * @apiName getUsuarioBuzon
	 * @apiDescription Servicio para obtener los datos de usuarios asignados a
	 *                 buzones por pais.
	 * @apiGroup UsuarioBuzon
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} idUsuarioBuzon Identificador de la asignacion a consultar.
	 *           Este campo es opcional.
	 * @apiParam {String} idVendedor Identificador del vendedor que se desea
	 *           consultar. Este campo es opcional.
	 * @apiParam {String} idBuzon Identificador del buzon que se desea consultar.
	 *           Este campo es opcional.
	 * @apiParam {String} estado Estado de la asignacion que se desea consultar.
	 *           Este campo es opcional.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas", "idUsuarioBuzon": "",
	 *                  "idBuzon": "", "idVendedor": "", "estado": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "buzones": [ {
	 *                    "idBuzon": "1", "nombreBuzon": "Buzon 1", "nivelBuzon":
	 *                    "1", "buzones": [ { "idUsuarioBuzon": "1", "idBuzon": "1",
	 *                    "idVendedor": "1", "nombreVendedor": "Desarrollo El
	 *                    Salvador", "estado": "ALTA", "creado_el": "09/12/2015
	 *                    17:04:13", "creado_por": "usuario.pruebas",
	 *                    "modificado_el": "09/12/2015 17:08:45", "modificado_por":
	 *                    "usuario.pruebas" } ] }, { "idBuzon": "8", "nombreBuzon":
	 *                    "Logistica1", "nivelBuzon": "2", "buzones": [ {
	 *                    "idUsuarioBuzon": "2", "idBuzon": "8", "idVendedor": "2",
	 *                    "nombreVendedor": "Nombre vendedor", "estado": "ALTA",
	 *                    "creado_el": "09/12/2015 17:26:02", "creado_por":
	 *                    "usuario.pruebas", "modificado_el": "09/12/2015 17:27:18",
	 *                    "modificado_por": "usuario.pruebas" } ] } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase": "OperacionSolicitud",
	 *                  "metodo": "doGet", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputUsuarioBuzon getBuzones(@Context HttpServletRequest req, InputUsuarioBuzon input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputUsuarioBuzon response = new UsuarioBuzon().getUsuarioBuzon(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getinventario")
	/**
	 * @api {POST} /consultasidra/getinventario/ [getInventario]
	 * 
	 * @apiName getInventario
	 * @apiDescription Servicio para obtener los datos del inventario de bodegas
	 *                 virtuales.
	 * @apiGroup Consulta
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} token Token de autenticaci\u00F3n de sesion de usuario.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} [idBodega] Identificador de la Bodega Virtual que se desea
	 *           consultar.
	 * @apiParam {String} [idArticulo] Identificador del art\u00EDculo que se desea
	 *           consultar.
	 * @apiParam {String} [descripcion] Nombre del art\u00EDculo que se desea
	 *           consultar.
	 * @apiParam {String} [serie] Serie del art\u00EDculo que se desea consultar.
	 * @apiParam {String} [tipoInv] Tipo de Inventario que se desea consultar.
	 * @apiParam {String} [tipoGrupo] Grupo que se desea consultar.
	 * @apiParam {String} [seriado] Tipo de Art\u00EDculo que se desea, 1 seriados o
	 *           0 no seriados.
	 * @apiParam {String} [idVendedor] Identificador del vendedor que se desea
	 *           consultar.
	 * @apiParam {String} [estado] Estado del art\u00EDculo que se desea consultar.
	 * @apiParam {String} [tecnologia] Tipo de tecnolog\u00EDa del art\u00EDculo.
	 * @apiParam {String} [min] Registro m\u00EDnimo a consultar para pagineo.
	 * @apiParam {String} [max] Registro m\u00E9ximo a consultar para pagineo.
	 * @apiParam {String} [mostrarDetalle] Campo que sirve para indicar si se debe
	 *           mostrar el detalle de los art\u00EDculos o no (1 = S\u00ED, 0 =
	 *           No).
	 * @apiParam {String} [datosWeb] Par\u00E9metro que indica si se deben mostrar
	 *           los datos para la aplicaci\u00F3n web o normales (1 = S\u00ED, 0 =
	 *           No).
	 * @apiParam {String} [mostrarRecarga] Campo que sirve para indicar si se debe
	 *           mostrar el art\u00EDculo de recarga o no (1 = S\u00ED, 0 = No).
	 * @apiParam {String} [numTraspasoScl] N\u00FAmero de traspaso SCL.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token": "WEB",
	 *                  "codArea": "505", "usuario": "usuario.pruebas",
	 *                  "codDispositivo": "", "idBodega": "", "idArticulo": "",
	 *                  "descripcion": "", "serie": "", "tipoInv": "", "tipoGrupo":
	 *                  "", "seriado": "", "idVendedor": "", "estado": "",
	 *                  "tecnologia": "", "min": "", "max": "", "mostrarDetalle":
	 *                  "", "datosWeb": "", "mostrarRecarga": "", "numTraspasoScl":
	 *                  "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "inventario": [ {
	 *                    "bodegas": [ { "idBodega": "73", "nombreBodega": "BODEGA
	 *                    PRUEBA", "grupos": [ { "grupo": "OTRO", "articulos": [ {
	 *                    "idArticulo": "9", "descripcion": "Articulo 1",
	 *                    "precioScl": "", "cantidad": "39", "seriado": "0",
	 *                    "tipoInv": "INV_SIDRA", "tecnologia": "",
	 *                    "detalleArticulo": { "idInventario": "645", "tipoInv":
	 *                    "INV_SIDRA", "estado": "Disponible", "numTelefono":
	 *                    "32166897", "icc": "8950304203612907440", "imei":
	 *                    "451815082155740" "creado_el": "28/12/2015 17:43:17",
	 *                    "creado_por": "usuarioprueba", "modificado_el":
	 *                    "28/12/2015 17:44:24", "modificado_por": "usuario.pruebas"
	 *                    } }, { "idArticulo": "9", "descripcion": "Articulo 2",
	 *                    "cantidad": "5", "seriado": "0", "tipoInv": "INV_SIDRA",
	 *                    "detalleArticulo": { "idInventario": "640", "idSolicitud":
	 *                    "232", "tipoInv": "INV_SIDRA", "estado": "En Proceso
	 *                    Devoluci\u00F3n", "numTelefono": "32165497", "icc":
	 *                    "8950301303609907440", "imei": "451819882155740"
	 *                    "creado_el": "28/12/2015 17:18:31", "creado_por":
	 *                    "sergio.lujan" } }, ] }, { "grupo": "TERMINAL",
	 *                    "articulos": [ { "idArticulo": "8", "descripcion":
	 *                    "Articulo 3", "precioScl": ".58733531", "cantidad": "1",
	 *                    "seriado": "1", "tipoInv": "INV_TELCA", "tecnologia":
	 *                    "3G", "detalleArticulo": [ { "idInventario": "3",
	 *                    "tipoInv": "INV_TELCA", "serie": "A2", "serieAsociada":
	 *                    "A2ASOC", "estado": "Disponible", "numTelefono":
	 *                    "32165497", "icc": "8950304203609907440", "imei":
	 *                    "451815082155740" "creado_el": "10/12/2015 16:17:58",
	 *                    "creado_por": "usuario.pruebas" }, { "idInventario": "4",
	 *                    "tipoInv": "INV_TELCA", "serie": "A3", "serieAsociada":
	 *                    "A3ASOC", "estado": "Disponible", "numTelefono":
	 *                    "32165499", "icc": "8950304203609907555", "imei":
	 *                    "451815082155780" "creado_el": "10/12/2015 16:17:58",
	 *                    "creado_por": "usuario.pruebas" } ] } ] } ] } ] } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase": "OperacionInventario",
	 *                  "metodo": "doGet", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputInventario doGetInventario(@Context HttpServletRequest req, InputConsultaInventario input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputInventario response = new Inventario().getInventario(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getpromocionales")
	/**
	 * @api {POST} /consultasidra/getpromocionales/ [getPromocionales]
	 * 
	 * @apiName getPromocionales
	 * @apiDescription Servicio para obtener los datos de art\u00EDculo
	 *                 promocionales registrados por pais.
	 * @apiGroup Promocionales
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} idArtPromocional Identificador del art\u00EDculo
	 *           promocional a consultar. Este campo es opcional.
	 * @apiParam {String} codArticulo C\u00F3digo del art\u00EDculo promocional que
	 *           se desea consultar. Este campo es opcional.
	 * @apiParam {String} descripcion Nombre del art\u00EDculo promocional que se
	 *           desea consultar. Este campo es opcional.
	 * @apiParam {String} tipoGrupo Nombre del grupo del art\u00EDculo promocional
	 *           que se desea consultar. Este campo es opcional.
	 * @apiParam {String} estado Estado del art\u00EDculo promocional que se desea
	 *           consultar. Este campo es opcional.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas", "idArtPromocional": "",
	 *                  "codArticulo": "", "descripcion": "", "tipoGrupo": "",
	 *                  "estado": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "grupos": [ {
	 *                    "grupo": "GRUPO A", "art\u00EDculos": [ {
	 *                    "idArtPromocional": "1", "codArticulo": "C1",
	 *                    "descripcion": "PACHON AZUL", "tipoGrupo": "GRUPO A",
	 *                    "idOferta": "9146027023265528900", "estado": "BAJA",
	 *                    "creado_el": "21/12/2015 08:23:16", "creado_por":
	 *                    "usuario.pruebas", "modificado_el": "21/12/2015 09:25:06",
	 *                    "modificado_por": "usuario.pruebas" }, {
	 *                    "idArtPromocional": "2", "codArticulo": "C2",
	 *                    "descripcion": "PACHON NEGRO", "tipoGrupo": "GRUPO A",
	 *                    "idOferta": "9146027023265528900", "estado": "ALTA",
	 *                    "creado_el": "21/12/2015 08:40:44", "creado_por":
	 *                    "usuario.pruebas", "modificado_el": "21/12/2015 10:28:47",
	 *                    "modificado_por": "usuario.pruebas" } ] }, { "grupo":
	 *                    "GRUPO B", "art\u00EDculos": [ { "idArtPromocional": "4",
	 *                    "codArticulo": "C4", "descripcion": "PACHON BLANCO",
	 *                    "tipoGrupo": "GRUPO B", "idOferta": "9146027023265528900",
	 *                    "estado": "ALTA", "creado_el": "21/12/2015 10:48:21",
	 *                    "creado_por": "usuario.pruebas" }, { "idArtPromocional":
	 *                    "3", "codArticulo": "C3", "descripcion": "PACHON VERDE",
	 *                    "tipoGrupo": "GRUPO B", "idOferta": "9146027023265528900",
	 *                    "estado": "ALTA", "creado_el": "21/12/2015 09:29:07",
	 *                    "creado_por": "usuario.pruebas" } ] } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase":
	 *                  "OperacionPromocionales", "metodo": "doGet", "excepcion": "
	 *                  ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputPromocionales doGetPromocionales(@Context HttpServletRequest req, InputPromocionales input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputPromocionales response = new Promocionales().getPromocionales(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getasignacionreserva")
	/**
	 * @api {POST} /consultasidra/getasignacionreserva/ [getAsignacionReserva]
	 * 
	 * @apiName getAsignacionReserva
	 * @apiDescription Servicio para obtener los datos de asignaciones o reservas
	 *                 por pais.
	 * @apiGroup Asignacion/Reserva
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} [idAsignacionReserva] Identificador de la
	 *           asignaci\u00F3n/reserva que se desea.
	 * @apiParam {String} [tipo] Nombre del tipo de operacion que se desea
	 *           consultar, puede ser ASIGNACION o RESERVA.
	 * @apiParam {String} [idDTS] Identificador del distribuidor que se desea
	 *           consultar en la solicitud. Necesario \u00FAnicamente si el tipo de
	 *           Solicitud es MOVIL.
	 * @apiParam {String} [idVendedor] Identificador del vendedor que se desea
	 *           consultar en la solicitud. Necesario \u00FAnicamente si el tipo de
	 *           Solicitud es MOVIL.
	 * @apiParam {String} [idBodegaOrigen] Identificador de la bodega origen que se
	 *           desea.
	 * @apiParam {String} [idBodegaDestino] Identificador de la bodega destino que
	 *           se desea.
	 * @apiParam {String} [fechaInicio] Fecha en formato yyyyMMdd desde la cu\u00E9l
	 *           se desea consultar la solicitud.
	 * @apiParam {String} [fechaFin] Fecha en formato yyyyMMdd hasta la cu\u00E9l se
	 *           desea consultar la solicitud.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario":"usuarioprueba", "idAsignacionReserva":"1",
	 *                  "tipo":"ASIGNACION", "idDTS":"", "idVendedor":"15",
	 *                  "idBodegaOrigen":"3", "idBodegaDestino":"7",
	 *                  "fechaInicio":"20151005", "fechaFin":"20151005" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." },
	 *                    "asignacionReserva": { "idAsignacionReserva": "28",
	 *                    "tipo": "RESERVA", "idVendedor": "661", "nombreVendedor":
	 *                    "ejecutivo2", "idBodegaOrigen": "81",
	 *                    "nombreBodegaOrigen": "bodega pruebas", "idBodegaDestino":
	 *                    "148", "nombreBodegaDestino": "BODEGA VENDEDOR 661",
	 *                    "observaciones": " Algunas series no se pudieron ingresar:
	 *                    A3 Algunos art\u00EDculos no se pudieron ingresar por las
	 *                    siguientes razones: Art\u00EDculos no existen en el
	 *                    inventario: 598. ", "estado": "ALTA", "creado_el":
	 *                    "29/12/2015 17:15:01", "creado_por": "usuarioprueba",
	 *                    "articulos": [ { "idArticulo": "3002", "descripcion":
	 *                    "Nokia N8", "serie": "B2", "serieFinal":"NULL",
	 *                    "serieAsociada":"", "cantidad": "1", "estado": "Reservado"
	 *                    }, { "idArticulo": "3003", "descripcion": "Estuche Nokia
	 *                    Lumia", "serie": "NULL", "serieFinal":"NULL",
	 *                    "serieAsociada":"", "cantidad": "3", "estado": "Reservado"
	 *                    } ] } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: Tipo debe especificarse como
	 *                  ASIGNACION o RESERVA.", "clase": "CtrlAsignacion", "metodo":
	 *                  "validarInput", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputAsignacion doGetAsignacionReserva(@Context HttpServletRequest req, InputAsignacion input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputAsignacion response = new Asignacion().getAsignacion(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/****
	 * SERVICIO PARA OBTENER INFORMACION DE VENDEDORES POR DISTRIBUIDOR
	 */
	@Path("/getvendxdts")
	/**
	 * @api {POST} /consultasidra/getvendxdts/ [getvendxdts]
	 * 
	 * @apiName getvendxdts
	 * @apiDescription Servicio para obtener informaci\u00F3n de vendedores por
	 *                 distribuidor.
	 * @apiGroup Consulta
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs de donde se
	 *           desea obtener la informaci\u00F3n.
	 * @apiParam {String} usuario Nombre del usuario que desea obtener la
	 *           informaci\u00F3n.
	 * @apiParam {String} [idDTS] Id del distribuidor del cual se desea consultar
	 *           los vendedores asociados.
	 * @apiParam {String} [idVendedor] Id del vendedor que se desea consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario":"usuarioprueba", "idDTS": "", "idVendedor":
	 *                  "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "1", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" }, "vendedores": [
	 *                    { "idDTS": "41", "nombreDistribuidor":"DTS1",
	 *                    "idBodegaVirtual": "50003", "idBodegaVendedor": "137",
	 *                    "idVendedor": "123", "nombreUsuario": "Nombre vendedor",
	 *                    "tipo": "PANEL" }, { "idDTS": "41",
	 *                    "nombreDistribuidor":"DTS1", "idBodegaVirtual": "50003",
	 *                    "idBodegaVendedor": "135", "idVendedor": "1034",
	 *                    "nombreUsuario": "Nombre vendedor", "tipo": "PANEL" }, {
	 *                    "idDTS": "41", "nombreDistribuidor":"DTS1",
	 *                    "idBodegaVirtual": "50003", "idBodegaVendedor": "130",
	 *                    "idVendedor": "1522", "nombreUsuario": "aviasesorsv",
	 *                    "tipo": "PANEL" } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-42", "mostrar": "1", "descripcion": "No se encontraron
	 *                  datos.", "clase": "", "metodo": "getVendedorxDTS",
	 *                  "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public Outputvendedorxdts getVendxDTS(@Context HttpServletRequest req, InputVendxdts input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		Outputvendedorxdts response = new VendedorXDTS().getVendxdts(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/**
	 * SERVICIO PARA OBTENER INFORMACION DE HISTORICO DE TRANSACCIONES
	 */
	@Path("/gethistorico")
	/**
	 * @api {POST} /consultasidra/gethistorico/ [gethistorico]
	 * 
	 * @apiName gethistorico
	 * @apiDescription Servicio para obtener informaci\u00F3n de transacciones
	 *                 realizadas en el inventario de SIDRA.
	 * @apiGroup Consulta
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de area del pais del cual se desea
	 *           buscar informaci\u00F3n.
	 * @apiParam {String} usuario Nombre del usuario que desea obtener la
	 *           informaci\u00F3n.
	 * @apiParam {String} idTraslado Id de traslado si se desea obtener informacion
	 *           de traslados. CAMPO NO OBLIGATORIO.
	 * @apiParam {String} tipoMovimiento Tipo de movimiento de inventario que se
	 *           desea buscar. CAMPO NO OBLIGATORIO.
	 * @apiParam {String} bodegaOrigen id de la bodega de donde proceden las
	 *           transacciones.CAMPO NO OBLIGATORIO.
	 * @apiParam {String} bodegaDestino id de la bodega hacia donde van las
	 *           transacciones.CAMPO NO OBLIGATORIO.
	 * @apiParam {String} articulo id del art\u00EDculo del que se desea buscar la
	 *           transaccion.CAMPO NO OBLIGATORIO.
	 * @apiParam {String} descripcion descripcion del articulo del que se desea
	 *           buscar el historico.CAMPO NO OBLIGATORIO.
	 * @apiParam {String} serie serie de art\u00EDculo del que se desea buscar
	 *           historico de transacciones.CAMPO NO OBLIGATORIO.
	 * @apiParam {String} serieFinal Serie final del rango que se desea buscar del
	 *           hist\u00F3rico de transacciones. CAMPO NO OBLIGATORIO.
	 * @apiParam {String} tipoInv tipo de inventario del que se desea buscar
	 *           historico de transacciones (INV_SIDRA/INV_TELCA).CAMPO NO
	 *           OBLIGATORIO.
	 * @apiParam {String} fechaInicio rango inicial de fechas de donde se desea
	 *           iniciar a buscar el historico de transacciones.Formato YYYYMMDD.
	 *           CAMPO NO OBLIGATORIO.
	 * @apiParam {String} fechaFin rango final de fechas de donde se desea finalizar
	 *           la busqueda del historico de transacciones.Formato YYYYMMDD.CAMPO
	 *           NO OBLIGATORIO.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario":"usuarioprueba", "idTraslado":"",
	 *                  "tipoMovimiento":"", "bodegaOrigen":"", "bodegaDestino":"",
	 *                  "articulo":"", "descripcion":"", "serie":"",
	 *                  "serieFinal":"", "tipoInv":"", "fechaInicio":"20151220",
	 *                  "fechaFin":"20151225" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "1", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" }, "historico": [
	 *                    { "tcscloginvsidraid": "26", "idTraslado": "17",
	 *                    "codigoTransaccion": "T1", "tipoMovimiento": "Transaccion
	 *                    entre Almacenes", "bodegaOrigen": "73",
	 *                    "nombreBodegaOrigen": "BODEGA PRUEBA MIERCOLES",
	 *                    "bodegaDestino": "72", "nombreBodegaDestino": "BODEGA
	 *                    VIRTUAL2", "articulo": "7", "descripcion": "SIMCARD",
	 *                    "precio": "4.00", "serie": "A1", "serieFinal": " ",
	 *                    "cantidad": "1", "serieAsociada": " ", "codNum": " ",
	 *                    "estado": "ALTA", "tipoInv": " ", "creado_por":
	 *                    "usuarioprueba", "creado_el": "23/12/2015 12:43:51" }, {
	 *                    "tcscloginvsidraid": "27", "idTraslado": "17",
	 *                    "codigoTransaccion": "T1", "tipoMovimiento": "Transaccion
	 *                    entre Almacenes", "bodegaOrigen": "73",
	 *                    "nombreBodegaOrigen": "BODEGA PRUEBA MIERCOLES",
	 *                    "bodegaDestino": "72", "nombreBodegaDestino": "BODEGA
	 *                    VIRTUAL2", "articulo": "9", "descripcion": "SIMCARD",
	 *                    "precio": "4.00", "serie": " ", "serieFinal": " ",
	 *                    "cantidad": "5", "serieAsociada": " ", "codNum": " ",
	 *                    "estado": "ALTA", "tipoInv": " ", "creado_por":
	 *                    "usuarioprueba", "creado_el": "23/12/2015 12:43:51" } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-42", "mostrar": "1", "descripcion": "No se encontraron
	 *                  datos.", "clase": "", "metodo": "getHistorico", "excepcion":
	 *                  " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputHistorico getHistorico(@Context HttpServletRequest req, InputHistoricoInv input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputHistorico response = new HistoricoInv().getHistorico(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/****
	 * SERVICIO PARA OBTENER DISPOSITIVOS
	 */
	@Path("/getdispositivo")
	/**
	 * @api {POST} /consultasidra/getdispositivo/ [getdispositivo]
	 * 
	 * @apiName getdispositivo
	 * @apiDescription Servicio para obtener informaci\u00F3n de los dispositivos
	 *                 existentes, al no ingresar ning\u00FAn valor en el input, se
	 *                 obtendr\u00E9n todos los dispositivos registrados.
	 * @apiGroup Dispositivos
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de area del pa\u00EDs del cual se
	 *           desea obtener informaci\u00F3n.
	 * @apiParam {String} usuario Nombre del usuario que solicita la operacion.
	 * @apiParam {String} [idDispositivo] Id del dispositivo que se desea obtener.
	 * @apiParam {String} [codigoDispositivo] C\u00F3digo del dispositivo a buscar.
	 * @apiParam {String} [modelo] Modelo de dispositivo a buscar.
	 * @apiParam {String} [descripcion] Descripci\u00F3n de dispositivo a buscar.
	 * @apiParam {String} [numTelefono] N\u00FAmero de telefono de dispositivo a
	 *           buscar.
	 * @apiParam {String} [responsable] Identificador de la panel o ruta asociada al
	 *           dispositivo a buscar.
	 * @apiParam {String} [tipoResponsable="RUTA o PANEL"] Tipo panel o ruta
	 *           asociada al dispositivo a buscar.
	 * @apiParam {String} [estado] Estado actual que se desea buscar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: {
	 *                  "usuario":"usuario.sidra", "idDispositivo":"",
	 *                  "codigoDispositivo": "", "modelo":"", "descripcion":"",
	 *                  "numTelefono":"", "responsable":"", "tipoResponsable":"",
	 *                  "estado": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "1", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" }, "dispositivos":
	 *                    [ { "idDispositivo": "2", "codigoDispositivo": "545645ds",
	 *                    "modelo": "AA", "descripcion": "SAMSUNG S4 MINI",
	 *                    "numTelefono": "54140022", "responsable":"10",
	 *                    "tipoResponsable":"RUTA", "nombreResponsable":"Ruta
	 *                    Comercial Sur", "vendedorAsignado": "1020",
	 *                    "idDistribuidor":"", "codOficina":"", "codVendedor":"",
	 *                    "estado": "ALTA", "idPlaza":"", "idPuntoVenta": "",
	 *                    "userId": "", "username": "", "creado_el": "30/12/2015
	 *                    10:48:55", "creado_por": "usuario", "modificado_por": "",
	 *                    "modificado_el": "" }, { "idDispositivo": "3",
	 *                    "codigoDispositivo": "asdasf", "modelo": "1",
	 *                    "descripcion": "SAMSUNG S4 MINI", "numTelefono":
	 *                    "54140021", "responsable":"100",
	 *                    "tipoResponsable":"PANEL",
	 *                    "nombreResponsable":"Metacentro", "vendedorAsignado":
	 *                    "5000", "idDistribuidor":"", "codOficina":"",
	 *                    "codVendedor":"", "estado": "ALTA", "idPlaza":"",
	 *                    "idPuntoVenta": "", "userId": "", "username": "",
	 *                    "creado_el": "30/12/2015 10:59:22", "creado_por":
	 *                    "usuario", "modificado_por": "", "modificado_el": "" } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-42", "mostrar": "1", "descripcion": "No se encontraron
	 *                  datos.", "clase": "CtrlDispositivo", "metodo":
	 *                  "getDispositivo", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputDispositivo getDispositivo(@Context HttpServletRequest req, InputDispositivo input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputDispositivo response = new Dispositivo().getDispositivos(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/****
	 * SERVICIO PARA OBTENER TIPO DE TRANSACCIONES INVENTARIO
	 */
	@Path("/gettipotransaccion")
	/**
	 * @api {POST} /consultasidra/gettipotransaccion/ [gettipotransaccion]
	 * 
	 * @apiName gettipotransaccion
	 * @apiDescription Servicio para obtener informaci\u00F3n de los tipos de
	 *                 transacciones de inventario que pueden realizarse en SIDRA.
	 * @apiGroup TipoTransaccion
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea c\u00F3digo de area del pa\u00EDs en el que se
	 *           desea obtener informaci\u00F3n.
	 * @apiParam {String} usuario nombre del usuario que solicita la
	 *           informaci\u00F3n.
	 * @apiParam {String} [idTipoTransaccion] Id de la transaccion que se desea
	 *           obtener.
	 * @apiParam {String} [codigoTransaccion] c\u00F3digo de la transaccion a
	 *           buscar.
	 * @apiParam {String} [descripcion] descripci\u00F3n de la transaccion a buscar.
	 * @apiParam {String} [tipoMovimiento] tipo de movimiento de transaccion a
	 *           buscar.
	 * @apiParam {String} [estado] Estado actual que se desea buscar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario":"usuario.sidra", "idTipoTransaccion":"",
	 *                  "codigoTransaccion": "", "descripcion":"",
	 *                  "tipoMovimiento":"", "estado": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" },
	 *                    "tiposTransaccion": [ { "idTipoTransaccion": "1",
	 *                    "codigoTransaccion": "T1", "descripcion": "Traslado entre
	 *                    bodegas", "tipoMovimiento": "Transaccion entre Almacenes",
	 *                    "tipoAfecta": "Traslado", "estado": "ALTA", "creado_por":
	 *                    "susana.barrios", "creado_el": "17/12/2015 10:11:02",
	 *                    "modificado_por": " ", "modificado_el": " " }, {
	 *                    "idTipoTransaccion": "2", "codigoTransaccion": "ASIG",
	 *                    "descripcion": "Transacci\u00F3n entre almacen a
	 *                    vendedor", "tipoMovimiento": "Asignacion a vendedor",
	 *                    "tipoAfecta": "Traslado", "estado": "ALTA", "creado_por":
	 *                    "susana.barrios", "creado_el": "17/12/2015 10:21:24",
	 *                    "modificado_por": " ", "modificado_el": " " }
	 * 
	 *                    ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-42", "mostrar": "1", "descripcion": "No se encontraron
	 *                  datos.", "clase": "CtrlTipoTransaccionInv", "metodo":
	 *                  "getTipoTransaccionInv", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputTransaccionInv getTransaccion(@Context HttpServletRequest req, InputTransaccionInv input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputTransaccionInv response = new TipoTransaccion().getTransaccion(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/*************************************************************************/
	/*************************** SERVICIOS RELEASE 3 *************************/
	/*************************************************************************/
	@Path("/getjornada")
	/**
	 * @api {POST} /consultasidra/getjornada/ [getJornada]
	 * 
	 * @apiName getJornada
	 * @apiDescription Servicio para obtener los datos de Jornadaes internos o
	 *                 externos configurados por pa\u00EDs.
	 * @apiGroup Jornada
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} idDistribuidor Identificador del distribuidor a asociar.
	 * @apiParam {String} [idJornada] Identificador de la jornada a consultar.
	 * @apiParam {String} [idBodegaVirtual] Identificador de la bodega virtual
	 *           asociada a la Jornada.
	 * @apiParam {String} [idVendedor] Identificador del vendedor a consultar.
	 * @apiParam {String} [dispositivoJornada] C\u00F3digo del dispositivo a
	 *           consultar.
	 * @apiParam {String} [idTipo] Identificador del tipo asociado a la Jornada.
	 * @apiParam {String} [tipo="RUTA o PANEL"] Nombre del tipo asociado.
	 *           Obligatorio \u00FAnicamente en caso de enviar idTipo.
	 * @apiParam {String} [estado="INICIADA o FINALIZADA"] Estado del cu\u00E9l se
	 *           desea obtener Jornadas.
	 * @apiParam {String} [fechaDesde] Fecha desde la cual se desea obtener
	 *           informaci\u00F3n de jornadas iniciadas en formato yyyyMMdd.
	 * @apiParam {String} [fechaHasta] Fecha final de la cual se desea obtener
	 *           informaci\u00F3n de jornadas iniciadas en formato yyyyMMdd.
	 * @apiParam {String} [estadoLiquidacion="PENDIENTE, RECHAZADA o LIQUIDADA"]
	 *           Estado del cu\u00E9l se desea obtener Jornadas finalizadas.
	 * @apiParam {String} [fechaFinDesde] Fecha desde la cual se desea obtener
	 *           informaci\u00F3n de jornadas finalizadas en formato yyyyMMdd.
	 * @apiParam {String} [fechaFinHasta] Fecha final de la cual se desea obtener
	 *           informaci\u00F3n de jornadas finalizadas en formato yyyyMMdd.
	 * @apiParam {String} [fechaLiqDesde] Fecha desde la cual se desea obtener
	 *           informaci\u00F3n de jornadas liquidadas en formato yyyyMMdd.
	 * @apiParam {String} [fechaLiqHasta] Fecha final de la cual se desea obtener
	 *           informaci\u00F3n de jornadas liquidadas en formato yyyyMMdd.
	 * @apiParam {String} [mostrarDetPago] Filtro para mostrar detalle del Pago: 1.
	 *           mostrarar detalla 0 no mostrara detalle.
	 * @apiParam {String} [mostarObservacion] Filtro para mostrar observaciones de
	 *           liquidacion: 1. mostrarar detalla 0 no mostrara detalle.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "token": "WEB", "codDispositivo": "", "usuario":
	 *                  "usuario.pruebas", "codDispositivo":
	 *                  "64E1CE4A48BF656974C376B7905F7E62", "idJornada": "",
	 *                  "idDistribuidor": "5", "idBodegaVirtual": "", "idVendedor":
	 *                  "", "dispositivoJornada": "", "idTipo": "", "tipo": "",
	 *                  "estado": "", "fechaDesde": "", "fechaHasta": "",
	 *                  "estadoLiquidacion": "", "fechaFinDesde": "",
	 *                  "fechaFinHasta": "", "fechaLiqDesde": "", "fechaLiqHasta":
	 *                  "", "mostrarDetPago":"0", "mostrarObservacion":"0" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "jornadas": {
	 *                    "idJornada": "62", "idVendedor": "2322", "nombreVendedor":
	 *                    "usuario sidra2", "usuarioVendedor": "usuario.sidra2",
	 *                    "codDispositivo": "00:11:22:33:44:55", "idDistribuidor":
	 *                    "5", "nombreDistribuidor": "DTS Uno", "idTipo": "7",
	 *                    "tipo": "RUTA", "nombreTipo": "RUTA UNO",
	 *                    "idBodegaVirtual": "28", "nombreBodegaVirtual": "BODEGA
	 *                    VIRTUAL", "idBodegaVendedor": "30", "estado":
	 *                    "FINALIZADA", "fecha": "16/06/2016 15:15:00",
	 *                    "observaciones": "observaciones al iniciar jornada",
	 *                    "fechaFinalizacion": "16/06/2016 17:11:43",
	 *                    "estadoLiquidacion": "RECHAZADA", "fechaLiquidacion":
	 *                    "16/06/2016 17:11:43", "obsLiquidacion": [ {
	 *                    "observacion": "Observacion 1", "creado_el": "16/06/2016
	 *                    17:36:01", "creado_por": "victor.cifuentes" }, {
	 *                    "observacion": "Observacion 2", "creado_el": "16/06/2016
	 *                    17:36:18", "creado_por": "victor.cifuentes" } ],
	 *                    "creado_el": "16/06/2016 15:11:53", "creado_por":
	 *                    "victor.cifuentes", "modificado_el": "16/06/2016
	 *                    17:12:00", "modificado_por": "usuario.sidra" } }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: Usuario. ID Distribuidor.",
	 *                  "clase": "CtrlJornada", "metodo": "validarInput",
	 *                  "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputJornada doGetJornada(@Context HttpServletRequest req, InputJornada input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputJornada response = new Jornada().getJornada(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getvenddts")
	/**
	 * @api {POST} /consultasidra/getvenddts/ [getVendedorDTS]
	 * 
	 * @apiName getVendedorDTS
	 * @apiDescription Servicio para obtener los datos de VendedorDTSes internos o
	 *                 externos configurados por pa\u00EDs.
	 * @apiGroup VendedorDTS
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} [idVendedorDTS] Identificador de la asociacion
	 *           distribuidor-vendedor.
	 * @apiParam {String} idDistribuidor Identificador del distribuidor asociado.
	 * @apiParam {String} [idBodegaVirtual] Identificador de la bodega virtual
	 *           asociada.
	 * @apiParam {String} [idVendedor] Identificador del vendedor a asociar.
	 * @apiParam {String} [usuarioVendedor] Usuario del vendedor a asociar.
	 * @apiParam {String} [estado="ALTA o BAJA"] Estado del cu\u00E9l se desea
	 *           obtener informaci\u00F3n.
	 * @apiParam {String} [soloDisponibles="0 o 1"] Permite mostrar \u00FAnicamente
	 *           los vendedores asociados a distribuidor que a\u00FAn no poseen
	 *           asociada una ruta o panel. 1 = Solo disponibles para asociar. 0 =
	 *           Todos los vendedores.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: {
	 *                  "codArea":"505", "usuario":"usuario.pruebas",
	 *                  "idVendedorDTS":"", "idDistribuidor":"60",
	 *                  "idBodegaVirtual":"", "idVendedor":"", "usuarioVendedor":"",
	 *                  "estado":"", "soloDisponibles":"" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "vendedores": [ {
	 *                    "idVendedorDTS": "1", "idDTS": "60", "nombreDistribuidor":
	 *                    "DISTRIBUIDOR ISOFT 1001", "idBodegaVirtual": "237",
	 *                    "nombreBodegaVirtual": "BODEGA DE PRUEBA ISOFT #1",
	 *                    "idBodegaVendedor": "30", "idVendedor": "1",
	 *                    "usuarioVendedor": "susana.barrios", "nombres": "Susana",
	 *                    "apellidos": "Barrios", "canal": "SIDRA", "subcanal":
	 *                    "MULTIMARCA", "numeroRecarga": "45968700", "pin": "1123",
	 *                    "dtsFuente": "113", "email": "sbarrios@dts.com", "estado":
	 *                    "ALTA", "tipoAsociado": "RUTA", "nombreAsociado": "RUTA
	 *                    MOVIL ZONA 1", "creado_el": "08/02/2016 11:31:38",
	 *                    "creado_por": "usuario.pruebas" } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase": "OperacionVendedorDTS",
	 *                  "metodo": "doGet", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputVendedorDTS doGetVendedorDTS(@Context HttpServletRequest req, InputVendedorDTS input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputVendedorDTS response = new VendedorDTS().getVendedorDTS(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getcantinv")
	/**
	 * @api {POST} /consultasidra/getcantinv/ [getCantInv]
	 * 
	 * @apiName getCantInv
	 * @apiDescription Servicio para obtener los datos del inventario de bodegas
	 *                 virtuales.
	 * @apiGroup Consulta
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} token Token de autenticaci\u00F3n de sesion de usuario.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} idBodega Identificador de la Bodega Virtual que se desea
	 *           consultar. Este campo es opcional.
	 * @apiParam {String} idArticulo Identificador del art\u00EDculo que se desea
	 *           consultar. Este campo es opcional.
	 * @apiParam {String} serie Serie que se desea consultar. Este campo es
	 *           opcional.
	 * @apiParam {String} descripcion Descripci\u00F3n del art\u00EDculo que se
	 *           desea consultar. Este campo es opcional.
	 * @apiParam {String} tipoInv Tipo de inventario que se desea consultar. Este
	 *           campo es opcional.
	 * @apiParam {String} estado Estado que se desea consultar. Este campo es
	 *           opcional.
	 * @apiParam {String} numTraspasoScl N\u00FAmero de traspaso SCL que se desea
	 *           consultar. Este campo es opcional.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token": "WEB",
	 *                  "codArea": "505", "usuario": "usuario.pruebas",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idBodega": "", "idArticulo": "", "serie": "",
	 *                  "descripcion": "", "tipoInv": "", "estado": "",
	 *                  "numTraspasoScl": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "datos": [ {
	 *                    "idBodega": "203", "cantInv": "10", "cantTotalInv": "301"
	 *                    }, { "idBodega": "205", "cantInv": "368", "cantTotalInv":
	 *                    "3001" }, { "idBodega": "206", "cantInv": "27",
	 *                    "cantTotalInv": "72" } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: Datos no num\u00E9ricos en ID
	 *                  Bodega Virtual.", "clase": "CtrlConsultaCantInv", "metodo":
	 *                  "validarInput", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputConsultaCantInv doGetConsultaCantInv(@Context HttpServletRequest req, InputConsultaCantInv input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputConsultaCantInv response = new ConsultaCantInv().getDatos(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getseries")
	/**
	 * @api {POST} /consultasidra/getseries/ [getSeries]
	 * 
	 * @apiName getSeries
	 * @apiDescription Servicio para obtener los datos del inventario de bodegas
	 *                 virtuales.
	 * @apiGroup Consulta
	 * @apiVersion 1.0.0
	 * @apiParam (getSeries) {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam (getSeries) {String} token Token de autenticaci\u00F3n de sesion de
	 *           usuario.
	 * @apiParam (getSeries) {String} usuario Nombre de usuario que solicita la
	 *           operacion.
	 * @apiParam (getSeries) {String} codDispositivo c\u00F3digo del dispositivo
	 *           desde donde se realiza la operaci\u00F3n, en caso de utilizarse la
	 *           APP M\u00F3VIL.
	 * @apiParam (getSeries) {String} idBodega Identificador de la Bodega Virtual
	 *           que se desea consultar.
	 * @apiParam (getSeries) {String} idArticulo Identificador del art\u00EDculo que
	 *           se desea consultar.
	 * @apiParam (getSeries) {String} tipoInv Tipo de Inventario que se desea
	 *           consultar, puede ser INV_TELCA o INV_SIDRA.
	 * @apiParam (getSeries) {String} estado Estado del art\u00EDculo que se desea
	 *           consultar.
	 * @apiParam (getSeries) {String} min Registro m\u00EDnimo a consultar para
	 *           pagineo.
	 * @apiParam (getSeries) {String} max Registro m\u00E9ximo a consultar para
	 *           pagineo.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token": "WEB",
	 *                  "codArea": "505", "usuario": "usuario.pruebas",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idBodega": "1", "idArticulo": "1", "tipoInv": "INV_SIDRA",
	 *                  "estado": "", "min": "", "max": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "202", "mostrar": "1", "descripcion":
	 *                    "Recursos recuperados exitosamente.", "clase":
	 *                    "OperacionConsultaSeries", "metodo": "doGet", "excepcion":
	 *                    " ", "tipoExepcion": "" }, "idArticulo": "48",
	 *                    "descripcion": "ARTICULO DE PRUEBA QA #1", "tipoInv":
	 *                    "INV_SIDRA", "precioScl": "40.00", "series": [ {
	 *                    "idInventario": "214526", "serie": "A1", "serieAsociada":
	 *                    "A2" }, { "idInventario": "214527", "serie": "A3",
	 *                    "serieAsociada": "A4" } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: Datos no num\u00E9ricos en ID de
	 *                  Art\u00EDculo.", "clase": "CtrlConsultaSeries", "metodo":
	 *                  "validarInput", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputConsultaSeries doGetSeries(@Context HttpServletRequest req, InputConsultaSeries input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputConsultaSeries response = new ConsultaSeries().getDatos(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getofertacampania")
	/**
	 * @api {POST} /consultasidra/getofertacampania/ [getOfertaCampania]
	 * 
	 * @apiName getOfertaCampania
	 * @apiDescription Servicio para obtener los datos de Ofertas o Campanias y sus
	 *                 detalles.
	 * @apiGroup OfertaCampania
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operacion.
	 * @apiParam {String} idOfertaCampania Identificador de la Oferta o Campania que
	 *           se desea obtener. Este campo es opcional.
	 * @apiParam {String} tipo Nombre del tipo de Oferta o Campania que se desea,
	 *           puede ser OFERTA o CAMPAniA. Este campo es opcional.
	 * @apiParam {String} nombre Nombre de la Oferta o Campania a buscar. Este campo
	 *           es opcional.
	 * @apiParam {String} fechaDesde Fecha en formato aaaaMMdd desde la que aplica
	 *           la Oferta o Campania. Este campo es opcional.
	 * @apiParam {String} fechaHasta Fecha en formato aaaaMMdd hasta la que aplica
	 *           la Oferta o Campania. Este campo es opcional.
	 * @apiParam {String} estado Estado del cu\u00E9l se desea obtener Ofertas o
	 *           Campanias (ALTA o BAJA). Este campo es opcional.
	 * @apiParam {String} idRuta Identificador de la ruta a la que se le aplica la
	 *           Oferta o Campania. Este campo es opcional.
	 * @apiParam {String} idPanel Identificador de la panel a la que se le aplica la
	 *           Oferta o Campania. Este campo es opcional.
	 * @apiParam {String} idDTS Identificador del Distribuidot asociado a una panel
	 *           o ruta a la que se le aplica una Oferta o Campania. Este campo es
	 *           opcional.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.prueba", "idOfertaCampania": "",
	 *                  "tipo": "", "nombre": "", "fechaDesde": "", "fechaHasta":
	 *                  "", "estado": "", "idRuta": "", "idPanel": "", "idDTS": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "ofertaCampania":
	 *                    { "idOfertaCampania": "3", "tipo": "CAMPAniA", "nombre":
	 *                    "Oferta 1", "descripcion": "Desc Oferta 1",
	 *                    "cantMaxPromocionales": "5", "fechaDesde": "25/02/2016",
	 *                    "fechaHasta": "25/02/2016", "estado": "ALTA", "creado_el":
	 *                    "25/02/2016 16:38:58", "creado_por": "usuario.prueba",
	 *                    "modificado_el": "25/02/2016 16:43:11", "modificado_por":
	 *                    "usuario.prueba", "ofertaCampaniaDet": [ { "idTipo": "1",
	 *                    "tipo": "PANEL", "nombreTipo": "PRUEBAS UNO", "idDTS":
	 *                    "7", "nombreDTS": "DTS UNO", "estado": "ALTA",
	 *                    "creado_el": "25/02/2016 16:43:12", "creado_por":
	 *                    "usuario.prueba" }, { "idTipo": "2", "tipo": "PANEL",
	 *                    "nombreTipo": "PRUEBAS DOS", "idDTS": "7", "nombreDTS":
	 *                    "DTS UNO", "estado": "ALTA", "creado_el": "25/02/2016
	 *                    16:43:12", "creado_por": "usuario.prueba" } ] } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase":
	 *                  "OperacionOfertaCampania", "metodo": "doGet", "excepcion": "
	 *                  ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputOfertaCampania doGetOfertaCampania(@Context HttpServletRequest req, InputOfertaCampania input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputOfertaCampania response = new OfertaCampania().getOfertaCampania(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getofertacampaniamovil")
	/**
	 * @api {POST} /consultasidra/getofertacampaniamovil/ [getOfertaCampaniaMovil]
	 * 
	 * @apiName getOfertaCampaniaMovil
	 * @apiDescription Servicio para obtener los datos de Ofertas o Campanias y los
	 *                 art\u00EDculos promocionales asociados.
	 * @apiGroup OfertaCampania
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} idOfertaCampania Identificador de la Oferta o Campania que
	 *           se desea obtener. Este campo es opcional.
	 * @apiParam {String} tipo Nombre del tipo de Oferta o Campania que se desea,
	 *           puede ser OFERTA o CAMPAniA. Este campo es opcional.
	 * @apiParam {String} nombre Nombre de la Oferta o Campania a buscar. Este campo
	 *           es opcional.
	 * @apiParam {String} fechaDesde Fecha en formato aaaaMMdd desde la que aplica
	 *           la Oferta o Campania. Este campo es opcional.
	 * @apiParam {String} fechaHasta Fecha en formato aaaaMMdd hasta la que aplica
	 *           la Oferta o Campania. Este campo es opcional.
	 * @apiParam {String} estado Estado del cu\u00E9l se desea obtener Ofertas o
	 *           Campanias (ALTA o BAJA). Este campo es opcional.
	 * @apiParam {String} idRuta Identificador de la ruta a la que se le aplica la
	 *           Oferta o Campania. Este campo es opcional.
	 * @apiParam {String} idPanel Identificador de la panel a la que se le aplica la
	 *           Oferta o Campania. Este campo es opcional.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.prueba", "codDispositivo":
	 *                  "64E1CE4A48BF656974C376B7905F7E62", "idOfertaCampania": "",
	 *                  "tipo": "", "nombre": "", "fechaDesde": "", "fechaHasta":
	 *                  "", "estado": "", "idRuta": "", "idPanel": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "ofertaCampania":
	 *                    { "idOfertaCampania": "3", "tipo": "CAMPAniA", "nombre":
	 *                    "Oferta 1", "descripcion": "Desc Oferta 1",
	 *                    "cantMaxPromocionales": "5", "fechaDesde": "25/02/2016
	 *                    00:00:00", "fechaHasta": "25/02/2016 00:00:00", "estado":
	 *                    "ALTA", "creado_el": "25/02/2016 16:38:58", "creado_por":
	 *                    "usuario.prueba", "modificado_el": "25/02/2016 16:43:11",
	 *                    "modificado_por": "usuario.prueba",
	 *                    "articulosPromocionales": [ { "idPromoOfertaCampania":
	 *                    "1", "idOfertaCampania": "4", "idArtPromocional": "51",
	 *                    "nombreArticulo": "PLAYERA M", "tipoInv": "INV_SIDRA",
	 *                    "cantArticulos": "2", "estado": "ALTA", "creado_el":
	 *                    "29/02/2016 15:36:27", "creado_por": "usuario.prueba" }, {
	 *                    "idPromoOfertaCampania": "2", "idOfertaCampania": "4",
	 *                    "idArtPromocional": "52", "nombreArticulo": "PLAYERA M",
	 *                    "tipoInv": "INV_SIDRA", "cantArticulos": "1", "estado":
	 *                    "ALTA", "creado_el": "29/02/2016 15:36:27", "creado_por":
	 *                    "usuario.prueba" } ] } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-390", "mostrar": "1", "descripcion": "No se encontraron
	 *                  campanias configuradas.", "clase":
	 *                  "CtrlOfertaCampaniaMovil", "metodo": "doGet", "excepcion": "
	 *                  ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputOfertaCampaniaMovil doGetOfertaCampaniaMovil(@Context HttpServletRequest req,
			InputOfertaCampaniaMovil input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputOfertaCampaniaMovil response = new OfertaCampaniaMovil().getOfertaCampania(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getpromocampania")
	/**
	 * @api {POST} /consultasidra/getpromocampania/ [getPromoCampania]
	 * 
	 * @apiName getPromoCampania
	 * @apiDescription Servicio para obtener los datos de Ofertas o Campanias y sus
	 *                 detalles.
	 * @apiGroup OfertaCampania
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} idPromoCampania Identificador de la asignaci\u00F3n de
	 *           Promocional a Campania que se desea obtener. Este campo es
	 *           opcional.
	 * @apiParam {String} idOfertaCampania Identificador de la Campania que se desea
	 *           obtener. Este campo es opcional.
	 * @apiParam {String} idArtPromocional Identificador del Art\u00EDculo
	 *           Promocional que se desea obtener. Este campo es opcional.
	 * @apiParam {String} estado Estado del cu\u00E9l se desea consultar (ALTA o
	 *           BAJA). Este campo es opcional.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.prueba", "idPromoCampania": "",
	 *                  "idOfertaCampania": "", "idArtPromocional": "", "estado": ""
	 *                  }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "campanias": {
	 *                    "idOfertaCampania": "4", "nombreCampania": "Oferta 1",
	 *                    "articulosPromocionales": [ { "idPromoCampania": "1",
	 *                    "idOfertaCampania": "4", "idArtPromocional": "51",
	 *                    "nombreArticulo": "PLAYERA M", "cantArticulos": "2",
	 *                    "estado": "ALTA", "creado_el": "29/02/2016 15:36:27",
	 *                    "creado_por": "usuario.prueba" }, { "idPromoCampania":
	 *                    "2", "idOfertaCampania": "4", "idArtPromocional": "52",
	 *                    "nombreArticulo": "PLAYERA S", "cantArticulos": "1",
	 *                    "estado": "ALTA", "creado_el": "29/02/2016 15:36:27",
	 *                    "creado_por": "usuario.prueba" } ] } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase":
	 *                  "OperacionPromoOfertaCampania", "metodo": "doGet",
	 *                  "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputPromoOfertaCampania doGetPromoCampania(@Context HttpServletRequest req,
			InputPromoOfertaCampania input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputPromoOfertaCampania response = new PromoOfertaCampania().getPromoCampania(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getcondicion")
	/**
	 * @api {POST} /consultasidra/getcondicion/ [getCondicion]
	 * 
	 * @apiName getCondicion
	 * @apiDescription Servicio para obtener los datos de Condiciones de Campanias y
	 *                 sus detalles.
	 * @apiGroup Condicion
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} idCondicion Identificador de la Condici\u00F3n que se
	 *           desea obtener. Este campo es opcional.
	 * @apiParam {String} idOfertaCampania Identificador de la Campania que se desea
	 *           obtener. Este campo es opcional.
	 * @apiParam {String} nombre Nombre de la Condici\u00F3n a buscar. Este campo es
	 *           opcional.
	 * @apiParam {String} tipoGestion Tipo de gesti\u00F3n de la Condici\u00F3n que
	 *           se desea. Este campo es opcional.
	 * @apiParam {String} tipo Tipo al que se le aplica la Condici\u00F3n. Este
	 *           campo es opcional.
	 * @apiParam {String} idArticulo Identificador del art\u00EDculo al que se le
	 *           aplica la Condici\u00F3n. Este campo es opcional.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "usuario":
	 *                  "usuario.pruebas", "codArea": "505", "idCondicion": "",
	 *                  "idOfertaCampania": "", "nombre": "", "tipoGestion": "",
	 *                  "tipo": "", "idArticulo": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "condiciones": [ {
	 *                    "idCondicion": "23", "idOfertaCampania": "3",
	 *                    "nombreCampania": "Oferta 1", "nombre": "Nombre",
	 *                    "tipoGestion": "VENTA", "tipoCondicion": "GENERICO",
	 *                    "estado": "ALTA", "creadoEl": "03/03/2016 08:52:27",
	 *                    "creadoPor": "usuario.pruebas", "detalle": [ {
	 *                    "idCondicion": "23", "tipo": "TIPO 1", "idArticulo": "",
	 *                    "tipoInv": "", "montoInicial": "1", "montoFinal": "20",
	 *                    "estado": "ALTA", "creadoEl": "03/03/2016 08:52:27" }, {
	 *                    "idCondicion": "23", "tipo": "", "idArticulo": "3",
	 *                    "tipoInv": "INV_TELCA", "montoInicial": "7", "montoFinal":
	 *                    "", "estado": "ALTA", "creadoEl": "03/03/2016 08:52:27" }
	 *                    ] }, { "idCondicion": "24", "idOfertaCampania": "3",
	 *                    "nombreCampania": "Oferta 1", "nombre": "Nombre 2",
	 *                    "tipoGestion": "ALTA_PREPAGO", "tipoCondicion":
	 *                    "ARTICULO", "estado": "ALTA", "creadoEl": "03/03/2016
	 *                    08:52:27", "creadoPor": "usuario.pruebas", "detalle": [ {
	 *                    "idCondicion": "24", "tipo": "", "idArticulo": "1",
	 *                    "tipoInv": "INV_TELCA", "montoInicial": "21",
	 *                    "montoFinal": "", "estado": "ALTA", "creadoEl":
	 *                    "03/03/2016 08:52:27" }, { "idCondicion": "24", "tipo":
	 *                    "", "idArticulo": "2", "tipoInv": "INV_TELCA",
	 *                    "montoInicial": "2", "montoFinal": "", "estado": "ALTA",
	 *                    "creadoEl": "03/03/2016 08:52:27" } ] } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-391", "mostrar": "1", "descripcion": "No se encontraron
	 *                  condiciones de campania configuradas.", "clase":
	 *                  "OperacionCondicion", "metodo": "doGet", "excepcion": " ",
	 *                  "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputCondicion doGetCondicion(@Context HttpServletRequest req, InputCondicionPrincipal input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputCondicion response = new Condicion().getCondicion(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getcondicionoferta")
	/**
	 * @api {POST} /consultasidra/getcondicionoferta/ [getCondicionOferta]
	 * 
	 * @apiName getcondicionoferta
	 * @apiDescription Servicio para obtener los datos de Condiciones de Oferta y
	 *                 sus detalles.
	 * @apiGroup CondicionOferta
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [idCondicion] Identificador de la Condici\u00F3n que se
	 *           desea obtener.
	 * @apiParam {String} [idOfertaCampania] Identificador de la Oferta que se desea
	 *           obtener.
	 * @apiParam {String} [nombre] Nombre de la Condici\u00F3n a buscar.
	 * @apiParam {String} [tipoGestion] Tipo de gesti\u00F3n de la Condici\u00F3n
	 *           que se desea.
	 * @apiParam {String} [tipo] Tipo al que se le aplica la Condici\u00F3n.
	 * @apiParam {String} [idArticulo] Identificador del art\u00EDculo al que se le
	 *           aplica la Condici\u00F3n.
	 * @apiParam {String} [idPDV] Identificador del punto de venta que se desea
	 *           consultar.
	 * @apiParam {String} [zonaComercialPDV] Zona comercial de puntos de venta que
	 *           se desea consultar.
	 * @apiParam {String} [categoriaPDV] Categor\u00EDa de puntos de venta que se
	 *           desea consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "usuario":
	 *                  "usuario.pruebas", "codArea": "505", "idCondicion": "",
	 *                  "idOfertaCampania": "", "nombre": "", "tipoGestion": "",
	 *                  "tipo": "", "idArticulo": "", "idPDV": "",
	 *                  "zonaComercialPDV": "", "categoriaPDV": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "condiciones": [ {
	 *                    "idCondicion": "40", "idOfertaCampania": "9",
	 *                    "nombreCampania": "Oferta ruta", "nombre": "Nombre 2",
	 *                    "tipoGestion": "ALTA_PREPAGO", "tipoCondicion":
	 *                    "GENERICO", "estado": "ALTA", "creadoEl": "04/03/2016
	 *                    14:45:28", "creadoPor": "usuario.pruebas", "detalle": [ {
	 *                    "idCondicion": "40", "tipo": "ARTICULO", "idArticulo":
	 *                    "15", "nombreArticulo": "BLACKBERRY Q10 NEGRO",
	 *                    "tipoCliente": "PDV", "tecnologia": "", "montoInicial":
	 *                    "1", "montoFinal": "", "tipoDescuento": "MONTO",
	 *                    "valorDescuento": "1", "idPDV": "", "nombrePDV": "",
	 *                    "zonaComercialPDV": "", "categoriaPDV": "",
	 *                    "idArticuloRegalo": "", "nombreArticuloRegalo": "",
	 *                    "cantidadArticuloRegalo": "", "tipoDescuentoRegalo": "",
	 *                    "valorDescuentoRegalo": "", "estado": "ALTA", "creadoPor":
	 *                    "usuario.pruebas", "creadoEl": "04/03/2016 14:45:28",
	 *                    "modificadoPor": "", "modificadoEl": "" }, {
	 *                    "idCondicion": "40", "tipo": "VENTA", "idArticulo": "",
	 *                    "nombreArticulo": "", "tipoCliente": "", "tecnologia": "",
	 *                    "montoInicial": "2", "montoFinal": "2", "tipoDescuento":
	 *                    "MONTO", "valorDescuento": "1", "idPDV": "", "nombrePDV":
	 *                    "", "zonaComercialPDV": "", "categoriaPDV": "",
	 *                    "idArticuloRegalo": "", "nombreArticuloRegalo": "",
	 *                    "cantidadArticuloRegalo": "", "tipoDescuentoRegalo": "",
	 *                    "valorDescuentoRegalo": "", "estado": "ALTA", "creadoPor":
	 *                    "usuario.pruebas", "creadoEl": "04/03/2016 14:45:28",
	 *                    "modificadoPor": "", "modificadoEl": "" } ] } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-396", "mostrar": "1", "descripcion": "No se encontraron
	 *                  condiciones de oferta configuradas.", "clase":
	 *                  "OperacionCondicionOferta", "metodo": "doGet", "excepcion":
	 *                  " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputCondicionOferta doGetCondicionOferta(@Context HttpServletRequest req,
			InputCondicionPrincipalOferta input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputCondicionOferta response = new CondicionOferta().getCondicion(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getinventariomovil")
	/**
	 * @api {POST} /consultasidra/getinventariomovil/ [getInventarioMovil]
	 * 
	 * @apiName getInventarioMovil
	 * @apiDescription Servicio para obtener los datos del inventario de bodegas
	 *                 virtuales.
	 * @apiGroup Consulta
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} token Token de autenticaci\u00F3n de sesion de usuario.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} idBodega Identificador de la Bodega Virtual que se desea
	 *           consultar.
	 * @apiParam {String} [idArticulo] Identificador del art\u00EDculo que se desea
	 *           consultar.
	 * @apiParam {String} [serie] Serie del art\u00EDculo que se desea consultar.
	 * @apiParam {String} [tipoInv] Tipo de Inventario que se desea consultar.
	 * @apiParam {String} [tipoGrupo] Grupo que se desea consultar.
	 * @apiParam {String} [seriado] Tipo de Art\u00EDculo que se desea, 1 seriados o
	 *           0 no seriados.
	 * @apiParam {String} [idVendedor] Identificador del vendedor que se desea
	 *           consultar.
	 * @apiParam {String} [estado] Estado del art\u00EDculo que se desea consultar.
	 * @apiParam {String} [tecnologia] Tipo de tecnolog\u00EDa del art\u00EDculo.
	 * @apiParam {String} [version] Version de los precios que se desean consultar.
	 * @apiParam {String} [mostrarRecarga] Campo que sirve para indicar si se debe
	 *           mostrar el art\u00EDculo de recarga o no (1 = S\u00ED, 0 = No).
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token": "WEB",
	 *                  "codArea": "505", "usuario": "usuario.pruebas",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idBodega": "114", "idArticulo": "", "serie": "", "tipoInv":
	 *                  "", "tipoGrupo": "", "seriado": "", "idVendedor": "",
	 *                  "estado": "", "tecnologia": "", "idTipo": "82", "tipo":
	 *                  "PANEL", "version": "20160414111754", "mostrarRecarga": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "inventario": [ {
	 *                    "bodegas": [ { "idBodega": "73", "nombreBodega": "BODEGA
	 *                    PRUEBA", "grupos": [ { "grupo": "TERMINAL", "articulos": [
	 *                    { "idArticulo": "12147", "descripcion": "3G|GSM-BASE
	 *                    MOTOROLA FXC 851", "cantidad": "1", "seriado": "1",
	 *                    "tipoInv": "INV_TELCA", "tecnologia": "3G",
	 * 
	 *                    "detallePrecios": [ { "idArticulo": "20227", "precioSCL":
	 *                    "39.13", "descuentoSCL": "0", "tipoGestion": "VENTA",
	 *                    "version": "20160414111754" }, { "idArticulo": "20227",
	 *                    "precioSCL": "156.73999944", "descuentoSCL": "0",
	 *                    "tipoGestion": "ALTA_PREPAGO", "version": "20160414111754"
	 *                    } ],
	 * 
	 *                    "descuentos": [ { "tipo": "ARTICULO", "tecnologia": "",
	 *                    "tipoGestion": "VENTA", "tipoCliente": "CF",
	 *                    "tipoDescuento": "MONTO", "valorDescuento": "10",
	 *                    "idOfertaCampania": "33", "nombreCampania": "Descuento a
	 *                    Terminales 3G" }, { "tipo": "ARTICULO", "tecnologia": "",
	 *                    "tipoGestion": "ALTA_PREPAGO", "tipoCliente": "PDV",
	 *                    "tipoDescuento": "PORCENTAJE", "valorDescuento": "10",
	 *                    "idOfertaCampania": "33", "nombreCampania": "Descuento a
	 *                    Terminales 3G" } ],
	 * 
	 *                    "detalleArticulo": [ { "idInventario": "3789127",
	 *                    "idAsignacionReserva": "24", "serie": "014027001710521",
	 *                    "numTelefono": "32165497", "icc": "8950304203609907440",
	 *                    "imei": "451815082155740" "estadoComercial": "L",
	 *                    "tipoInv": "INV_TELCA", "estado": "Disponible",
	 *                    "creado_el": "03/03/2016 12:26:32", "creado_por":
	 *                    "P_SINCR_SIDRA" }, { "idInventario": "227618",
	 *                    "idAsignacionReserva": "24", "serie": "355986036045244",
	 *                    "numTelefono": "32165498", "icc": "8950304203609907960",
	 *                    "imei": "451815082155810" "estadoComercial": "L",
	 *                    "tipoInv": "INV_TELCA", "estado": "Disponible",
	 *                    "creado_el": "03/03/2016 12:26:32", "creado_por":
	 *                    "P_SINCR_SIDRA" } ] ] } ] } ] } ] } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase":
	 *                  "OperacionInventarioMovil", "metodo": "doGet", "excepcion":
	 *                  " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputInventarioMovil doGetInventarioMovil(@Context HttpServletRequest req,
			InputConsultaInventarioMovil input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputInventarioMovil response = new InventarioMovil().getInventario(input);
		String jsonRequest = new GsonBuilder().setPrettyPrinting().create().toJson(input);
		String jsonResponse = new GsonBuilder().setPrettyPrinting().create().toJson(response);
		//System.out.println("REQUEST: "+jsonRequest);
		//System.out.println("RESPONSE: "+jsonResponse);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getfichacliente")
	/**
	 * @api {POST} /consultasidra/getfichacliente/ [getFichaCliente]
	 * 
	 * @apiName getFichaCliente
	 * @apiDescription Servicio para obtener los datos de fichas de cliente.
	 * @apiGroup Ficha_Cliente
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} token Token de autenticaci\u00F3n de sesion de usuario.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} tipoDocumento Descripci\u00F3n del documento de
	 *           identificaci\u00F3n del cliente. Campo opcional.
	 * @apiParam {String} noDocumento N\u00FAmero de documento de
	 *           identificaci\u00F3n del cliente. Campo opcional.
	 * @apiParam {String} numTelefono N\u00FAmero de telefono del cliente. Campo
	 *           opcional
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token": "WEB",
	 *                  "codArea": "505", "usuario": "usuario.pruebas",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "noDocumento": "8-443-43323", "tipoDocumento": "RUC",
	 *                  "numTelefono": "47588115" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token": "WEB",
	 *                    "respuesta": { "codResultado": "0", "mostrar": "0",
	 *                    "descripcion": "Recursos recuperados exitosamente." },
	 *                    "cliente": { "primerNombre": "JORGE", "segundoNombre":
	 *                    "MARIO", "primerApellido": "YAC", "segundoApellido":
	 *                    "AJU", "tipoDocumento": "RUC", "noDocumento":
	 *                    "8-443-43323", "codCuenta": "2766661", "codCliente":
	 *                    "3009488", "numTelefono": "67979799" } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: Ocurrio un Problema inesperado,
	 *                  contacte a su Administrador.", "clase": "CtrlFichaCliente",
	 *                  "metodo": "validarInput", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputFichaCliente doGetFichaCliente(@Context HttpServletRequest req, InputFichaCliente input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputFichaCliente response = new FichaCliente().getFichaCliente(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getimpuestos")
	/**
	 * @api {POST} /consultasidra/getimpuestos/ [getImpuestos]
	 * 
	 * @apiName getImpuestos
	 * @apiDescription Servicio para obtener los datos de impuestos por pais.
	 * @apiGroup Impuestos
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} token Token de autenticaci\u00F3n de sesion de usuario.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token": "WEB",
	 *                  "codArea": "505", "usuario":"usuario.sidra",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "codArea": "505",
	 *                    "impuestos": [{ "nombre": "IVA", "valor": "IVA",
	 *                    "tipoCliente": "", "despuesDeDescuento": "TRUE",
	 *                    "porcentaje": "15", "estado": "ALTA", "grupos": [ {
	 *                    "nombre": "TERMINAL", "valor": "Terminal" }, { "nombre":
	 *                    "SIMCARD", "valor": "Simcard" } ] }], "descuentos": [ {
	 *                    "nombre": "ISC", "valor": "ISC", "porcentaje": "4.381",
	 *                    "despuesDeDescuento": "", "estado": "ALTA", "grupos": [ {
	 *                    "nombre": "RECARGA", "valor": "Recarga" }, { "nombre":
	 *                    "TARJETASRASCA", "valor": "Tarjetas Rasca" } ] } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: C\u00F3digo de \u00E9rea debe ser
	 *                  de 3 d\u00EDgitos.", "clase": "CtrlImpuestos", "metodo":
	 *                  "validarInput", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputImpuestos doGetImpuestos(@Context HttpServletRequest req, InputConsultaImpuestos input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputImpuestos response = new Impuestos().getDatos(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/****************************************************************************************
	 * SERVICIO PARA OBTENER GESTI\u00F3N VISITAS
	 *****************************************************************************************/
	@Path("/getvisita")
	/**
	 * @api {POST} /consultasidra/getvisita/ [getvisita]
	 * 
	 * @apiName getvisita
	 * @apiDescription Servicio para obtener informaci\u00F3n de los tipos de
	 *                 transacciones de inventario que pueden realizarse en SIDRA.
	 * @apiGroup GESTION_VISITA
	 * @apiVersion 1.0.0
	 * @apiParam {String} token token de validaci\u00F3n de sesi\u00F3n de usuario
	 * @apiParam {String} codArea c\u00F3digo de area del pa\u00EDs en el que se
	 *           desea obtener informaci\u00F3n.
	 * @apiParam {String} usuario nombre del usuario que solicita la
	 *           informaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} [idVendedor] id del vendedor del cual se desean obtener
	 *           las visitas registradas.
	 * @apiParam {String} [fechaInicio] rango inicial de fechas a tomar en cuenta
	 *           para obtener datos. FORMATO:YYYYmmdd.
	 * @apiParam {String} [fechaFin] rango final de fechas a tomar en cuenta para
	 *           obtener datos. FORMATO:YYYYmmdd.
	 * @apiParam {String} [idPuntoVenta] id del punto de venta del que se desean
	 *           obtener visitas.
	 * @apiParam {String} [idJornada] id de la Jornada de la que se desean obtener
	 *           visitas.
	 * @apiParam {String} [gestion="VENTA o NO VENTA"] tipo de gestion realizada en
	 *           la visita.
	 * @apiParam {String} [numRecarga] N\u00FAmero de recarga del punto de venta a
	 *           consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token": "WEB",
	 *                  "codArea": "505", "usuario":"usuario.sidra",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idVendedor":"", "fechaInicio": "", "fechaFin":"",
	 *                  "idPuntoVenta":"", "idJornada": "", "gestion": "",
	 *                  "numRecarga": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token": "ewwer",
	 *                    "respuesta": { "codResultado": "12", "mostrar": "0",
	 *                    "descripcion": "OK. Datos obtenidos exitosamente",
	 *                    "clase": " ", "metodo": " ", "excepcion": " ",
	 *                    "tipoExepcion": "" }, "visitas": [ { "idVisita": "9",
	 *                    "idVendedor": "2322", "nombreVendedor": "usuario sidra2",
	 *                    "idJornada": "26", "fecha": "25/02/2016 12:22:00",
	 *                    "idPuntoVenta": "265", "nombrePuntoVenta": "TIENDA MARY",
	 *                    "latitud": "0.0", "longitud": "0.0", "gestion": "NO
	 *                    VENTA", "causa": "No se encotr\u00F3 abierto.", "folio": "
	 *                    ", "observaciones": "", "creado_el": "25/02/2016
	 *                    14:36:24", "creado_por": "usuario", "modificado_el": " ",
	 *                    "modificado_por": " " } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "token": "WEB", "respuesta": {
	 *                  "codResultado": "-42", "mostrar": "1", "descripcion": "No se
	 *                  encontraron datos.", "clase": "CtrlVisitaGestion", "metodo":
	 *                  "getTipoTransaccionInv", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputVisita getVisita(@Context HttpServletRequest req, InputGetVisita input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputVisita response = new VisitaGestion().getVisita(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/****************************************************************************************
	 * SERVICIO PARA OBTENER VENTAS
	 *****************************************************************************************/
	@Path("/getventa")
	/**
	 * @api {POST} /consultasidra/getventa/ [getventa]
	 * 
	 * @apiName getventa
	 * @apiDescription Servicio para obtener informaci\u00F3n de las ventas que se
	 *                 han hecho en SIDRA.
	 * @apiGroup VENTAS
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de area del pa\u00EDs en el que se
	 *           desea obtener informaci\u00F3n.
	 * @apiParam {String} usuario Nombre del usuario que solicita la
	 *           informaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} [idVenta] Id de la venta que se desea obtener.
	 * @apiParam {String} [idJornada] Id de la jornada que se desea consultar.
	 * @apiParam {String} [idVendedor] Id del vendedor del cual se desean obtener
	 *           las ventas registradas.
	 * @apiParam {String} idDTS Id del Distribuidor del cual se desea obtener
	 *           informaci\u00F3n de las ventas asociadas a sus vendedores.
	 * @apiParam {String} [idRutaPanel] Id de la Ruta o Panel de la cual se quiere
	 *           obtener la informaci\u00F3n de ventas que estas han realizado.
	 * @apiParam {String} [tipoRutaPanel="RUTA o PANEL"] Tipo del ID de la Ruta o
	 *           Panel de la cual se quiere obtener la informaci\u00F3n de ventas
	 *           que han realizado.
	 * @apiParam {String} [idTipo] Id del punto de venta del que se desea obtener
	 *           informaci\u00F3n de vetas.
	 * @apiParam {String} [tipoCliente] Tipo de cliente que se desea consultar, CF o
	 *           PDV.
	 * @apiParam {String} [tipoDocumento] Tipo de documento que se desea consultar
	 *           TCK, CCF, etc.
	 * @apiParam {String} [tipoCliente] Tipo de cliente que se desea consultar, CF o
	 *           PDV.
	 * @apiParam {String} [serie] Serie de la venta a consultar.
	 * @apiParam {String} [folio] N\u00FAmero de folio o factura de venta que se
	 *           desea consultar.
	 * @apiParam {String} [nit] N\u00FAmero de NIT del cliente registrado en la
	 *           venta que se desea consultar.
	 * @apiParam {String} [primerNombreCliente] Primer nombre del cliente registrado
	 *           en la venta que se desea consultar.
	 * @apiParam {String} [primerApellidoCliente] Primer apellido del cliente
	 *           registrado en la venta que se desea consultar.
	 * @apiParam {String} [numTelefono] N\u00FAmero de tel\u00E9fono del cliente
	 *           registrado en la venta que se desea consultar.
	 * @apiParam {String} [tipoDocumentoCliente] Tipo de documento de
	 *           identificaci\u00F3n del cliente registrado en la venta que se desea
	 *           consultar.
	 * @apiParam {String} [numDocCliente] N\u00FAmero del documento de
	 *           identificaci\u00F3n del cliente registrado en la venta que se desea
	 *           consultar.
	 * @apiParam {String} [fechaInicio] Rango inicial de fechas a tomar en cuenta
	 *           para obtener datos. FORMATO:YYYYmmdd.
	 * @apiParam {String} [fechaFin] Rango final de fechas a tomar en cuenta para
	 *           obtener datos. FORMATO:YYYYmmdd.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token": "WEB",
	 *                  "usuario": "usuario.prueba", "codDispositivo":
	 *                  "64E1CE4A48BF656974C376B7905F7E62", "codArea": "505",
	 *                  "idVenta": "", "idJornada": "", "idVendedor": "", "idDTS":
	 *                  "5", "idRutaPanel": "", "tipoRutaPanel": "", "idTipo": "",
	 *                  "tipoCliente": "", "tipoDocumento": "", "serie": "",
	 *                  "folio": "", "serieSidra": "", "folioSidra": "", "nit": "",
	 *                  "primerNombreCliente": "", "primerApellidoCliente": "",
	 *                  "numTelefono": "", "tipoDocumentoCliente": "",
	 *                  "numDocCliente": "", "fechaInicio": "", "fechaFin": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" }, "venta": {
	 *                    "idVenta": "44", "idVendedor": "2322", "nombreVendedor":
	 *                    "usuario sidra", "idJornada": "44", "idBodegaVendedor":
	 *                    "28", "bodegaVendedor": "BODEGA VIRTUAL", "fecha":
	 *                    "26/04/2016 11:51:13", "folio": "13", "serie": "A",
	 *                    "folioSidra": "", "serieSidra": "", "tipoDocumento":
	 *                    "TCK", "idTipo": "42345678", "tipo": "CF", "nombrePDV":
	 *                    "", "departamento": "", "municipio": "", "nit": "",
	 *                    "registroFiscal": "", "giro": "", "nombreFiscal": "44",
	 *                    "nombre": "Hared", "segundoNombre": "", "apellido":
	 *                    "M\u00E9ndez", "segundoApellido": "Amaya", "direccion":
	 *                    "Guatemala", "numTelefono": "42345678", "tipoDocCliente":
	 *                    "Pasaporte", "numDocCliente": "1821832151201",
	 *                    "nombresFacturacion": "", "apellidosFacturacion": "",
	 *                    "zonaComercial": "Ana baez, Llanos del sol", "exento": "NO
	 *                    EXENTO", "impuesto": "16.95557", "idOfertaCampania": "",
	 *                    "descuentoMontoVenta": "", "descuentoTotal": "5",
	 *                    "estado": "REGISTRADO_SIDRA", "montoFactura": "75.181",
	 *                    "montoPagado": "75.18", "nombrePanelRuta": "RUTA UNO",
	 *                    "creadoPor": "usuario.pruebas", "creadoEl": "02/06/2016
	 *                    10:20:54", "envioAlarma": "0" "detallePago": [{
	 *                    "formaPago": "Tarjeta", "monto": "50.1800000001234",
	 *                    "numReferencia": "123456789012345", "numAutorizacion":
	 *                    "118588151358513" }, { "formaPago": "Efectivo", "monto":
	 *                    "25.000000001234", "numReferencia": "", "numAutorizacion":
	 *                    "" }], "articulosPromocionales": { "articuloPromocional":
	 *                    "PLAYERA S", "cantidad": "3" } } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase": "CtrlVenta", "metodo":
	 *                  "getVenta", "excepcion": " ", "tipoExepcion": "Generales" }
	 *                  }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputVenta getVenta(@Context HttpServletRequest req, InputGetVenta input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputVenta response = new VentaRuta().getVenta(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/****************************************************************************************
	 * SERVICIO PARA OBTENER DETALLE DE VENTAS
	 *****************************************************************************************/
	@Path("/getdetalleventa")
	/**
	 * @api {POST} /consultasidra/getdetalleventa/ [getdetalleventa]
	 * 
	 * @apiName getdetalleventa
	 * @apiDescription Servicio para obtener los detalles de una venta registrada en
	 *                 SIDRA.
	 * @apiGroup VENTAS
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de area del pa\u00EDs en el que se
	 *           desea obtener informaci\u00F3n.
	 * @apiParam {String} usuario Nombre del usuario que solicita la
	 *           informaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} idVenta Id de la venta de la que se desea obtener datos de
	 *           su detalle.
	 * @apiParam {String} [min] Registro m\u00EDnimo a consultar para pagineo.
	 * @apiParam {String} [max] Registro m\u00E9ximo a consultar para pagineo.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario":"usuario.sidra", "codDispositivo":
	 *                  "64E1CE4A48BF656974C376B7905F7E62", "idVenta":"", "min":"",
	 *                  "max":"" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" }, "detalleVenta":
	 *                    [ { "idVentaDet": "162", "articulo": "1173",
	 *                    "descripcion": "RECARGA ELECTRONICA", "cantidad": "100",
	 *                    "seriado": "0", "serie": "", "serieAsociada": "",
	 *                    "numTelefono": "12312312", "tipoInv": "INV_TELCA",
	 *                    "precio": "88", "descuentoSCL": "0", "descuentoSidra":
	 *                    "0", "detalleDescuentosSidra": { "idOfertaCampania":
	 *                    "Inicio de verano", "nombreOfertaCampania": ".3",
	 *                    "descuento": "0.30" }, "gestion": "ALTAPREPAGO",
	 *                    "impuesto": "12", "precioTotal": "100", "modalidad": "",
	 *                    "estado": "ALTA", "impuestosArticulo": [ {
	 *                    "nombreImpuesto": "ITBMS", "valor": "10" }, {
	 *                    "nombreImpuesto": "ISC", "valor": "2" } ] } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-42", "mostrar": "1", "descripcion": "No se encontraron
	 *                  datos.", "clase": "CtrlVenta", "metodo": "getDetalleVenta",
	 *                  "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputArticuloVenta getDetalleVenta(@Context HttpServletRequest req, InputGetDetalle input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputArticuloVenta response = new VentaRuta().getDetalleVenta(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/gethistoricopromo")
	/**
	 * @api {POST} /consultasidra/gethistoricopromo/ [getHistoricoPromo]
	 * 
	 * @apiName getHistoricoPromo
	 * @apiDescription Servicio para consultar los datos historicos de promocionales
	 *                 otorgados en Sidra.
	 * @apiGroup Historico
	 * @apiVersion 1.0.0
	 * 
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} token Token de autenticaci\u00F3n de sesion de usuario.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} idCampania Identificador de la campania a consultar. Este
	 *           campo es opcional.
	 * @apiParam {String} tipoCliente Tipo de cliente a buscar, puede ser CF o PDV.
	 * @apiParam {String} idTipo Identificador del cliente a consultar. Este campo
	 *           es opcional.
	 * @apiParam {String} idRuta Identificador de la ruta a consultar. Este campo es
	 *           opcional.
	 * @apiParam {String} idVendedor Identificador del vendedor a consultar. Este
	 *           campo es opcional.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token":"WEB",
	 *                  "codArea":"505", "usuario":"usuarioprueba",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idCampania":"", "tipoCliente":"", "idTipo":"", "idRuta":"",
	 *                  "idVendedor":"" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token": "WEB",
	 *                    "respuesta": { "codResultado": "0", "mostrar": "0",
	 *                    "descripcion": "Recursos recuperados exitosamente." },
	 *                    "clientes": { "tipoCliente": "CF", "idTipo": "55213455",
	 *                    "campanias": { "idCampania": "21", "nombreCampania":
	 *                    "Promcion Abril", "cantPromocionales": "7", "articulos": [
	 *                    { "idArticulo": "8", "descripcion": "PLAYERA M",
	 *                    "cantidad": "2", "tipoInv": "INV_SIDRA" }, { "idArticulo":
	 *                    "7", "descripcion": "PLAYERA S", "cantidad": "5",
	 *                    "tipoInv": "INV_SIDRA" } ] } } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: El par\u00E9metro de entrada
	 *                  \"tipo\"\\ esta vac\u00EDo", "clase": "CtrlHistoricoPromo",
	 *                  "metodo": "validarInput", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputHistoricoPromo getHistoricoPromo(@Context HttpServletRequest req, InputHistoricoPromocionales input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputHistoricoPromo response = new HistoricoPromo().getHistoricoPromocionales(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/*************************************************************************/
	/*************************** SERVICIOS RELEASE 5 *************************/
	/*************************************************************************/
	@Path("/getcuenta")
	/**
	 * @api {POST} /consultasidra/getcuenta/ [getCuenta]
	 * 
	 * @apiName getCuenta
	 * @apiDescription Servicio para consultar los datos de cuentas bancarias
	 *                 creadas en Sidra.
	 * @apiGroup Cuenta
	 * @apiVersion 1.0.0
	 * 
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} token Token de autenticaci\u00F3n de sesion de usuario.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} idDts Id del distribuidor al que pertenencen las cuentas a
	 *           consultar .
	 * @apiParam {String} [idCuenta] Identificador de la cuenta a consultar.
	 * @apiParam {String} [banco] Nombre del banco asociado a la cuenta a consultar.
	 * @apiParam {String} [noCuenta] N\u00FAmero de la cuenta bancaria a consultar.
	 * @apiParam {String} [tipoCuenta] Tipo de la cuenta consultar.
	 * @apiParam {String} [nombreCuenta] Nombre de la cuenta a consultar.
	 * @apiParam {String} [estado="ALTA o BAJA"] Estado de la cuenta a consultar.
	 * @apiParam {String} [bancosAsociados="1 o 0"] Par\u00E9metro para indicar si
	 *           se desean consultar los bancos con alguna cuenta asociada o las
	 *           cuentas.
	 * 
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token":"WEB",
	 *                  "codArea":"505", "usuario":"usuario.pruebas",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idDts""", "idCuenta":"", "banco":"", "noCuenta":"",
	 *                  "tipoCuenta":"", "nombreCuenta":"", "estado":"",
	 *                  "bancosAsociados":"1" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token":"WEB", "respuesta":
	 *                    { "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "distribuidores":
	 *                    { "idDts": "12", "nombre": "DTS_hvrm", "cuentas": [ {
	 *                    "idCuenta": "1", "banco": "Banco Industrial De El
	 *                    Salvdor", "noCuenta": "1234567890", "tipoCuenta":
	 *                    "Ahorro", "nombreCuenta": "Cuenta_hvrm", "estado": "ALTA",
	 *                    "creado_el": "01/11/2016 10:21:21", "creado_por":
	 *                    "vladimir.rodriguez", "modificado_el": "" }, { "idCuenta":
	 *                    "2", "banco": "Washington Mutual", "noCuenta":
	 *                    "4105545054564", "tipoCuenta": "Monetaria",
	 *                    "nombreCuenta": "Cuenta de Ahorro BdO", "estado": "ALTA",
	 *                    "creado_el": "04/11/2016 11:23:51", "creado_por":
	 *                    "usuario.pruebas", "modificado_el": "" } ] }, "bancos": [
	 *                    { "banco": "Banco de Occidente" }, { "banco": "Banco
	 *                    General" } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase": "OperacionCuenta",
	 *                  "metodo": "doGet", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputCuenta getCuenta(@Context HttpServletRequest req, InputCuenta input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputCuenta response = new Cuenta().getCuenta(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getremesa")
	/**
	 * @api {POST} /consultasidra/getremesa/ [getRemesa]
	 * 
	 * @apiName getRemesa
	 * @apiDescription Servicio para consultar los datos de remesas creadas en
	 *                 Sidra.
	 * @apiGroup Remesa
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} token Token de autenticaci\u00F3n de sesion de usuario.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} [idRemesa] Identificador de la remesa a consultar.
	 * @apiParam {String} [idJornada] Identificador de la jornada asociada a
	 *           consultar.
	 * @apiParam {String} [noBoleta] N\u00FAmero de la boleta de remesa a consultar.
	 * @apiParam {String} [banco] Nombre del banco de la cuenta asociada a
	 *           consultar.
	 * @apiParam {String} [idCuenta] Identificador de la cuenta bancaria asociada a
	 *           consultar.
	 * @apiParam {String} [idDistribuidor] Identificador del distribuidor a
	 *           consultar.
	 * @apiParam {String} [idTipo] Identificador de la ruta o panel asociada a
	 *           consultar.
	 * @apiParam {String} [tipo="RUTA o PANEL"] Nombre del tipo a consultar.
	 *           Obligatorio \u00FAnicamente si se ingresa idTipo.
	 * @apiParam {String} [idVendedor] Identificador del vendedor asociado a
	 *           consultar.
	 * @apiParam {String} [estado="ALTA o BAJA"] Estado de la remesa a consultar.
	 * @apiParam {String} [fechaInicio] fecha desde la cual se desean obtener las
	 *           remesas a consultar. Formato de fecha:yyyyMMdd.
	 * @apiParam {String} [fechaFin] fecha hasta la cual se desean obtener las
	 *           remesas a consultar.Formato de fecha:yyyyMMdd.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token":"WEB",
	 *                  "codArea":"505", "usuario":"usuario.pruebas",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idRemesa":"", "idJornada":"", "noBoleta":"", "banco":"",
	 *                  "idCuenta":"", "idDistribuidor":"", "idTipo":"", "tipo":"",
	 *                  "idVendedor":"", "estado":"", "fechaInicio":"20160820",
	 *                  "fechaFin":"20160906" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token":"WEB", "respuesta":
	 *                    { "codResultado": "202", "mostrar": "1", "descripcion":
	 *                    "Recursos recuperados exitosamente.", "clase":
	 *                    "OperacionRemesa", "metodo": "doGet", "excepcion": " ",
	 *                    "tipoExepcion": "" }, "datos": { "idJornada": "10",
	 *                    "estadoJornada": "FINALIZADA", "fechaInicioJornada":
	 *                    "27/06/2016 12:13:22", "estadoLiqJornada": "LIQUIDADA",
	 *                    "fechaFinJornada": "28/06/2016 11:28:23",
	 *                    "fechaLiqJornada": "28/06/2016 11:28:30", "totalVentas":
	 *                    "260.9097", "totaltarjetaCredito": "0", "cantVentas": "3",
	 *                    "remesas": { "idRemesa": "2", "origen": "MOVIL",
	 *                    "idDistribuidor": "17", "nombreDistribuidor":
	 *                    "Distribuidora Metro Mall", "idVendedor": "2262",
	 *                    "nombreVendedor": "pruebas.panama", "idTipo": "15",
	 *                    "tipo": "RUTA", "nombreTipo": "METRO MALL ZONA SUR",
	 *                    "monto": "200", "noBoleta": "185", "banco": "Banco de
	 *                    Occidente", "idCuenta": "3", "noCuenta": "46794697164",
	 *                    "tipoCuenta": "Monetaria", "nombreCuenta": "Cuenta de
	 *                    Ahorro", "estado": "BAJA", "creado_el": "04/07/2016
	 *                    16:02:29", "creado_por": "usuario.pruebas",
	 *                    "modificado_el": "", "modificado_por": "" } } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-714", "mostrar": "1", "descripcion": "No se encontraron
	 *                  datos de remesas.", "clase": "OperacionRemesa", "metodo":
	 *                  "doGet", "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputRemesa getRemesa(@Context HttpServletRequest req, InputRemesa input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputRemesa response = new Remesa().getRemesa(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}
	

	@Path("/getjornadamasiva")
	/**
	 * @api {POST} /consultasidra/getjornadamasiva/ [getJornadaMasiva]
	 * 
	 * @apiName getJornadaMasiva
	 * @apiDescription Servicio para obtener los datos de Jornadas registradas en
	 *                 Sidra por pa\u00EDs.
	 * @apiGroup Jornada
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} idDistribuidor Identificador del distribuidor a asociar.
	 * @apiParam {String} [idJornada] Identificador de la jornada a consultar.
	 * @apiParam {String} [idJornadaResponsable] Identificador de la jornada del
	 *           responsable que se desea a consultar.
	 * @apiParam {String} [idBodegaVirtual] Identificador de la bodega virtual
	 *           asociada a la Jornada.
	 * @apiParam {String} [idVendedor] Identificador del vendedor a consultar.
	 * @apiParam {String} [dispositivoJornada] C\u00F3digo del dispositivo a
	 *           consultar.
	 * @apiParam {String} [idTipo] Identificador del tipo asociado a la Jornada.
	 * @apiParam {String} [tipo="RUTA o PANEL"] Nombre del tipo asociado.
	 *           Obligatorio en caso de enviar idTipo.
	 * @apiParam {String} [estado="INICIADA o FINALIZADA"] Estado del cu\u00E9l se
	 *           desea obtener Jornadas.
	 * @apiParam {String} [fechaDesde] Fecha desde la cual se desea obtener
	 *           informaci\u00F3n de jornadas iniciadas en formato yyyyMMdd.
	 * @apiParam {String} [fechaHasta] Fecha final de la cual se desea obtener
	 *           informaci\u00F3n de jornadas iniciadas en formato yyyyMMdd.
	 * @apiParam {String} [estadoLiquidacion="PENDIENTE, RECHAZADA o LIQUIDADA"]
	 *           Estado del cu\u00E9l se desea obtener Jornadas finalizadas.
	 * @apiParam {String} [fechaFinDesde] Fecha desde la cual se desea obtener
	 *           informaci\u00F3n de jornadas finalizadas en formato yyyyMMdd.
	 * @apiParam {String} [fechaFinHasta] Fecha final de la cual se desea obtener
	 *           informaci\u00F3n de jornadas finalizadas en formato yyyyMMdd.
	 * @apiParam {String} [fechaLiqDesde] Fecha desde la cual se desea obtener
	 *           informaci\u00F3n de jornadas liquidadas en formato yyyyMMdd.
	 * @apiParam {String} [fechaLiqHasta] Fecha final de la cual se desea obtener
	 *           informaci\u00F3n de jornadas liquidadas en formato yyyyMMdd.
	 * @apiParam {String} [envioAlarma="1 o 0"] Par\u00E9metro para consultar
	 *           jornadas que han enviado alarmas en el inicio de jornada.
	 * @apiParam {String} [envioAlarmaFin="1 o 0"] Par\u00E9metro para consultar
	 *           jornadas que han enviado alarmas en el fin de jornada.
	 * @apiParam {String} [idDeuda] Identificado de la deuda asociada.
	 * @apiParam {String} [estadoPago="PENDIENTE o PAGADA"] Estado de pago de la
	 *           jornada a consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "token": "WEB", "codDispositivo": "", "usuario":
	 *                  "usuario.pruebas", "codDispositivo":
	 *                  "64E1CE4A48BF656974C376B7905F7E62", "idJornada": "",
	 *                  "idJornadaResponsable": "", "idDistribuidor": "5",
	 *                  "idBodegaVirtual": "", "idVendedor": "",
	 *                  "dispositivoJornada": "", "idTipo": "", "tipo": "",
	 *                  "estado": "", "fechaDesde": "", "fechaHasta": "",
	 *                  "estadoLiquidacion": "", "fechaFinDesde": "",
	 *                  "fechaFinHasta": "", "fechaLiqDesde": "", "fechaLiqHasta":
	 *                  "", "envioAlarma": "1", "envioAlarmaFin": "0", "idDeuda":
	 *                  "", "estadoPago": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "0", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." }, "jornadas": [{
	 *                    "idJornada": "62", "idJornadaResponsable": "61",
	 *                    "idVendedor": "2322", "nombreVendedor": "usuario sidra2",
	 *                    "usuarioVendedor": "usuario.sidra2", "codDispositivo":
	 *                    "2669305997BF98946B3B1DF77862936E1620E01E",
	 *                    "idDistribuidor": "5", "nombreDistribuidor": "DTS Uno",
	 *                    "idTipo": "7", "tipo": "RUTA", "nombreTipo": "RUTA UNO",
	 *                    "idBodegaVirtual": "28", "nombreBodegaVirtual": "BODEGA
	 *                    VIRTUAL", "idBodegaVendedor": "30", "saldoInicial": "2000"
	 *                    "estado": "FINALIZADA", "envioAlarma": "1", "tipoAlarma":
	 *                    "Jornada en fecha festiva.", "envioAlarmaFin": "0",
	 *                    "tipoAlarmaFin": "", "fecha": "16/06/2016 15:15:00",
	 *                    "observaciones": "observaciones al iniciar jornada",
	 *                    "fechaFinalizacion": "16/06/2016 17:11:43",
	 *                    "codDispositivoFinalizacion":
	 *                    "2669305997BF98946B3B1DF77862936E1620E01E",
	 *                    "estadoLiquidacion": "RECHAZADA", "fechaLiquidacion":
	 *                    "16/06/2016 17:11:43", "detallePagos": [ { "formaPago":
	 *                    "TARJETA", "monto": "10.1" }, { "formaPago": "REMESAS",
	 *                    "monto": "20.2" }, { "formaPago": "EFECTIVO", "monto":
	 *                    "27907.5" } ], "idDeuda": "", "estadoPago": "",
	 *                    "obsLiquidacion": [ { "observacion": "Observacion 1",
	 *                    "creado_el": "16/06/2016 17:36:01", "creado_por":
	 *                    "victor.cifuentes" }, { "observacion": "Observacion 2",
	 *                    "creado_el": "16/06/2016 17:36:18", "creado_por":
	 *                    "victor.cifuentes" } ], "creado_el": "16/06/2016
	 *                    15:11:53", "creado_por": "victor.cifuentes",
	 *                    "modificado_el": "16/06/2016 17:12:00", "modificado_por":
	 *                    "usuario.sidra" }] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: Usuario. ID Distribuidor.",
	 *                  "clase": "CtrlJornada", "metodo": "validarInput",
	 *                  "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputJornada doGetJornadaMasiva(@Context HttpServletRequest req, InputJornada input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputJornada response = new JornadaMasiva().getJornada(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getsincronizacion")
	/**
	 * @api {POST} /consultasidra/getsincronizacion/ [getsincronizacion]
	 * 
	 * @apiName getsincronizacion
	 * @apiDescription Servicio para obtener los datos de vendedores registrados
	 *                 para finalizar Jornada.
	 * @apiGroup Sincronizacion
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} idDistribuidor Identificador del distribuidor a asociar.
	 * @apiParam {String} [idJornada] Identificador de la jornada a consultar.
	 * @apiParam {String} [idDispositivo] Identificador del dispositivo a consultar.
	 * @apiParam {String} [idVendedor] Identificador del vendedor a consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "token": "WEB", "usuario": "usuario.pruebas",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idJornada": "53", "idDispositivo": "ASDF123QWER456ZXCV789",
	 *                  "idVendedor": "904" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token": "WEB",
	 *                    "respuesta": { "codResultado": "202", "mostrar": "1",
	 *                    "descripcion": "Recursos recuperados exitosamente.",
	 *                    "clase": "OperacionSincronizacion", "metodo": "doGet",
	 *                    "excepcion": " ", "tipoExepcion": "" }, "datos": {
	 *                    "idSincronizacion": "5", "idDispositivo":
	 *                    "ASDF123QWER456ZXCV789", "idVendedor": "904",
	 *                    "nombreVendedor": "supervisor3", "idJornada": "53",
	 *                    "creado_el": "19/07/2016 09:32:04", "creado_por":
	 *                    "usuario.pruebas", "modificado_el": "", "modificado_por":
	 *                    "" } }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: ID Jornada.", "clase":
	 *                  "CtrlSincronizacion", "metodo": "validarInput", "excepcion":
	 *                  " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputSincronizacion doGetSincronizacion(@Context HttpServletRequest req, InputSincronizacion input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputSincronizacion response = new Sincronizacion().getSincronizacion(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getfaltasinc")
	/**
	 * @api {POST} /consultasidra/getfaltasinc/ [getfaltasinc]
	 * 
	 * @apiName getfaltasinc
	 * @apiDescription Servicio para obtener los datos de vendedores que faltan por
	 *                 sincronizar operaciones para finalizar Jornada.
	 * @apiGroup Sincronizacion
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} idJornada Identificador de la jornada a consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "token": "WEB", "codDispositivo":
	 *                  "64E1CE4A48BF656974C376B7905F7E62", "usuario":
	 *                  "usuario.pruebas", "idJornada": "53" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente.", "clase":
	 *                    "OperacionSincronizacion", "metodo": "doGetFaltantes",
	 *                    "excepcion": " ", "tipoExepcion": "" } }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "token": "WEB", "respuesta": {
	 *                  "codResultado": "-199", "mostrar": "1", "descripcion": "Los
	 *                  siguientes datos faltan o son incorrectos: ID Jornada.",
	 *                  "clase": "CtrlSincronizacion", "metodo": "validarInput",
	 *                  "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputSincronizacion doGetFaltaSinc(@Context HttpServletRequest req, InputSincronizacion input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputSincronizacion response = new Sincronizacion().getFaltaSinc(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getreporteinventariovendido")
	/**
	 * @api {POST} /consultasidra/getreporteinventariovendido/
	 *      [getreporteinventariovendido]
	 * 
	 * @apiName getReporteInventarioVendido
	 * @apiDescription Servicio para obtener un listado detalle de los articulos
	 *                 vendidos.
	 * @apiGroup Reportes
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [idDistribuidor] Identificador del distribuidor que se
	 *           desea obtener. Este campo es opcional.
	 * @apiParam {String} [idBodegaVirtual] Identificador de la bodega virtual de la
	 *           que se vendieron los articulos a consultar. Este campo es opcional.
	 * @apiParam {String} [idJornada] Identificador de la jornada a consultar. Este
	 *           campo es opcional.
	 * @apiParam {String} [idVendedor] Identificador del vendedor asociado al punto
	 *           de venta que se desea obtener. Este campo es opcional.
	 * @apiParam {String} [idTipo] Identificador de una ruta o panel especifica de
	 *           la cual se dese consultar. Este campo es opcional.
	 * @apiParam {String} [tipo] Describe si el idtipo corresponde a una ruta o
	 *           panel. Este campo es opcional.
	 * @apiParam {String} fechaInicio Fecha en formato yyyyMMdd desde la cu\u00E9l
	 *           se desea consultar la solicitud.
	 * @apiParam {String} fechaFin Fecha en formato yyyyMMdd hasta la cu\u00E9l se
	 *           desea consultar la solicitud.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas",
	 *                  "fechaInicio":"20160601", "fechaFin":"20160701",
	 *                  "idDistribuidor": "22", "idBodegaVirtual": "", "idJornada":
	 *                  "67", "idVendedor": "", "idTipo": "", "tipo": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" }, "detalle": [ {
	 *                    "tipoProducto": "SIMCARD", "articulos": { "idArticulo":
	 *                    "1618", "descripcion": "Z-TARJETA SIM PREPAGO LINEA CON
	 *                    NUMERO", "cantidadVendida": "1", "montoTotalVendido": "10"
	 *                    } }, { "tipoProducto": "TARJETASRASCA", "articulos": [ {
	 *                    "idArticulo": "665", "descripcion": "TARJETA PREPAGO DE
	 *                    $2.00", "cantidadVendida": "1", "montoTotalVendido":
	 *                    "7.339" }, { "idArticulo": "674", "descripcion": "TARJETA
	 *                    PREPAGO DE $5.00", "cantidadVendida": "4",
	 *                    "montoTotalVendido": "20" }, { "idArticulo": "675",
	 *                    "descripcion": "TARJETA PREPAGO DE $10.00",
	 *                    "cantidadVendida": "1", "montoTotalVendido": "10" } ] } ]
	 *                    }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: Usuario.", "clase": "CtrlReporte",
	 *                  "metodo": "validarInput", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputReporteInventarioVendido getReporteInventarioVendido(@Context HttpServletRequest req,
			InputReporteInventarioVendido input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputReporteInventarioVendido response = new Reporte().getReporteInventarioVendido(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getresumeninventariovendido")
	/**
	 * @api {POST} /consultasidra/getresumeninventariovendido/
	 *      [getresumeninventariovendido]
	 * 
	 * @apiName getResumenInventarioVendido
	 * @apiDescription Servicio para obtener un listado del resumen de los articulos
	 *                 vendidos del inventario agrupados por tipo de producto.
	 * @apiGroup Reportes
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [idDistribuidor] Identificador del distribuidor que se
	 *           desea obtener. Este campo es opcional.
	 * @apiParam {String} [idBodegaVirtual] Identificador de la bodega virtual de la
	 *           que se vendieron los articulos a consultar. Este campo es opcional.
	 * @apiParam {String} [idJornada] Identificador de la jornada a consultar. Este
	 *           campo es opcional.
	 * @apiParam {String} [idVendedor] Identificador del vendedor asociado al punto
	 *           de venta que se desea obtener. Este campo es opcional.
	 * @apiParam {String} [idTipo] Identificador de una ruta o panel especifica de
	 *           la cual se dese consultar. Este campo es opcional.
	 * @apiParam {String} [tipo] Describe si el idtipo corresponde a una ruta o
	 *           panel. Este campo es opcional.
	 * @apiParam {String} fechaInicio Fecha en formato yyyyMMdd desde la cu\u00E9l
	 *           se desea consultar la solicitud.
	 * @apiParam {String} fechaFin Fecha en formato yyyyMMdd hasta la cu\u00E9l se
	 *           desea consultar la solicitud.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas",
	 *                  "fechaInicio":"20160601", "fechaFin":"20160701",
	 *                  "idDistribuidor": "22", "idBodegaVirtual": "", "idJornada":
	 *                  "67", "idVendedor": "", "idTipo": "", "tipo": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" }, "resumen": [ {
	 *                    "cantidadVendida": "1", "montoTotalVendido": "10",
	 *                    "tipoProducto": "SIMCARD" }, { "cantidadVendida": "6",
	 *                    "montoTotalVendido": "37.339", "tipoProducto":
	 *                    "TARJETASRASCA" } ] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: Usuario.", "clase": "CtrlReporte",
	 *                  "metodo": "validarInput", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputResumenInventarioVendido getResumenInventarioVendido(@Context HttpServletRequest req,
			InputReporteInventarioVendido input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputResumenInventarioVendido response = new Reporte().getResumenInventarioVendido(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getcountcumplimientoventa")
	/**
	 * @api {POST} /consultasidra/getcountcumplimientoventa/
	 *      [getcountcumplimientoventa]
	 * 
	 * @apiName getcountcumplimientoventa
	 * @apiDescription Servicio para obtener la cantidad de datos que puede retornar
	 *                 sobre el reporte cumplimiento de ventas con los filtros
	 *                 enviados.
	 * @apiGroup Reportes
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [idDistribuidor] Identificador del distribuidor que se
	 *           desea obtener. Este campo es opcional.
	 * @apiParam {String} [idRuta] Id de la Ruta de la cual se quiere obtener la
	 *           informaci\u00F3n de visitas que se han realizado. Este campo es
	 *           opcional.
	 * @apiParam {String} [idJornada] Identificador de la jornada a consultar. Este
	 *           campo es opcional.
	 * @apiParam {String} [idPuntoVenta] Identificador del punto de venta a
	 *           consultar. Este campo es opcional.
	 * @apiParam {String} fechaInicio Fecha en formato yyyyMMdd desde la cu\u00E9l
	 *           se desea consultar la solicitud.
	 * @apiParam {String} fechaFin Fecha en formato yyyyMMdd hasta la cu\u00E9l se
	 *           desea consultar la solicitud.
	 * 
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas",
	 *                  "fechaInicio":"20160701", "fechaFin":"20160727",
	 *                  "idDistribuidor": "123", "idJornada": "", "idRuta": "",
	 *                  "idPuntoVenta": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" },
	 *                    "cantRegistros": "2" }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-62", "mostrar": "1", "descripcion": "La fecha fin debe ser
	 *                  mayor a la fecha inicio", "clase": "CtrlReporte", "metodo":
	 *                  "getReporteCumplimientoVenta", "excepcion": " ",
	 *                  "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputReporteCumplimientoVenta getCountCumplimientoVenta(@Context HttpServletRequest req,
			InputReporteCumplimientoVenta input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputReporteCumplimientoVenta response = new Reporte().getCountCumplimientoVenta(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getreportecumplimientoventa")
	/**
	 * @api {POST} /consultasidra/getreportecumplimientoventa/
	 *      [getreportecumplimientoventa]
	 * 
	 * @apiName getreportecumplimientoventa
	 * @apiDescription Servicio para obtener un listado de Puntos de Ventas, cada
	 *                 item de PDV contendr\u00E9 dos listados m\u00E9s que
	 *                 representan las ventas realizadas, un listado de articulos y
	 *                 un listado de tarjetas.
	 * @apiGroup Reportes
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [idDistribuidor] Identificador del distribuidor que se
	 *           desea obtener. Este campo es opcional.
	 * @apiParam {String} [idRuta] Id de la Ruta de la cual se quiere obtener la
	 *           informaci\u00F3n de visitas que se han realizado. Este campo es
	 *           opcional.
	 * @apiParam {String} [idJornada] Identificador de la jornada a consultar. Este
	 *           campo es opcional.
	 * @apiParam {String} [idPuntoVenta] Identificador del punto de venta a
	 *           consultar. Este campo es opcional.
	 * @apiParam {String} fechaInicio Fecha en formato yyyyMMdd desde la cu\u00E9l
	 *           se desea consultar la solicitud.
	 * @apiParam {String} fechaFin Fecha en formato yyyyMMdd hasta la cu\u00E9l se
	 *           desea consultar la solicitud.
	 * @apiParam {String} max Registro m\u00E9ximo a consultar para pagineo .
	 * @apiParam {String} min Registro m\u00EDnimo a consultar para pagineo .
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas",
	 *                  "fechaInicio":"20160701", "fechaFin":"20160727", "min": "0",
	 *                  "max": "10" "idDistribuidor": "123", "idJornada": "",
	 *                  "idRuta": "", "idPuntoVenta": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" }, "cumplVenta": {
	 *                    "fecha": "2016-07-27 17:15:20.0", "idPuntoVenta": "61",
	 *                    "nombreDistribuidor": "DTS TECNOLOGIA S.A",
	 *                    "idBodegaVirtual": "119", "nombreBodega": "BODEGA RUTA
	 *                    25", "departamento": "BOCAS DEL TORO", "municipio": "BOCAS
	 *                    DEL TORO", "distrito": "", "idRuta": "25", "nombreRuta":
	 *                    "RUTA BOCAS DEL TORO 1", "idVendedor": "2462", "usuario":
	 *                    "ruta.panama", "nombrePdv": "TIENDA IV\u00E9NCITO",
	 *                    "telPrimario": "65980044", "tipoProducto": "FISICO",
	 *                    "tipoNegocio": "Tienda", "idVenta": "42",
	 *                    "articulosVendidos": { "tipoProducto": "SIMCARD",
	 *                    "cantidadSugerida": "0", "montoSugerido": "0",
	 *                    "cantidadFacturada": "1", "montoFacturado": "10" },
	 *                    "tarjetas": { "idArticulo": "665", "descripcion": "TARJETA
	 *                    PREPAGO DE $2.00", "cantidadSugerida": "0",
	 *                    "montoSugerido": "0", "cantidadFacturada": "1",
	 *                    "montoFacturado": "7.339" } } }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: Usuario.", "clase": "CtrlReporte",
	 *                  "metodo": "validarInput", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputReporteCumplimientoVenta getReporteCumplimientoVenta(@Context HttpServletRequest req,
			InputReporteCumplimientoVenta input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputReporteCumplimientoVenta response = new Reporte().getReporteCumplimientoVenta(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getreportecumplimientovisita")
	/**
	 * @api {POST} /consultasidra/getreportecumplimientovisita/
	 *      [getreportecumplimientovisita]
	 * 
	 * @apiName getreportecumplimientovisita
	 * @apiDescription Servicio para obtener un listado de visitas que realiza un
	 *                 vendedor por ruta.
	 * @apiGroup Reportes
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [idDistribuidor] Identificador del distribuidor que se
	 *           desea obtener. Este campo es opcional.
	 * @apiParam {String} [idRuta] Id de la Ruta de la cual se quiere obtener la
	 *           informaci\u00F3n de visitas que se han realizado. Este campo es
	 *           opcional.
	 * @apiParam {String} [idPuntoVenta] Id del punto de venta del que se desean
	 *           obtener visitas. Este campo es opcional.
	 * @apiParam {String} [departamento] Nombre del deparamento en donde se
	 *           encuentran los puntos de venta que se desean consultar. Este campo
	 *           es opcional.
	 * @apiParam {String} [municipio] Nombre del municipio en donde se encuentran
	 *           los puntos de venta que se desean consultar. Este campo es
	 *           opcional.
	 * @apiParam {String} [distrito] Nombre del distrito en donde se encuentran los
	 *           puntos de venta que se desean consultar. Este campo es opcional.
	 * @apiParam {String} fechaInicio Fecha en formato yyyyMMdd desde la cu\u00E9l
	 *           se desea consultar la solicitud.
	 * @apiParam {String} fechaFin Fecha en formato yyyyMMdd hasta la cu\u00E9l se
	 *           desea consultar la solicitud.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas",
	 *                  "fechaInicio":"20160701", "fechaFin":"20160727",
	 *                  "idDistribuidor": "123", "idRuta": "", "idPuntoVenta": "",
	 *                  "departamento": "", "municipio": "", "distrito": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" }, "cumplVisita":
	 *                    [ { "codPvd": "60", "codDistribuidor": "22",
	 *                    "distribuidor": "DTS TECNOLOGIA S.A", "codBodega": "114",
	 *                    "bodega": "BODEGA RUTA 24", "departamento":
	 *                    "CHIRIQU\u00ED", "municipio": "CHIRIQU\u00ED", "distrito":
	 *                    "", "usuario": "99887768", "vendedor": "961", "ruta":
	 *                    "RUTA TECH S.A.", "nombrePdv": "FARMACIA LA SALUD", "dia":
	 *                    "27", "mes": "07", "anio": "2016", "fecha": "2016-07-27
	 *                    09:59:22.0", "asignado": "NO", "gestion": "VENTA",
	 *                    "observaciones": " " }, { "codPvd": "59",
	 *                    "codDistribuidor": "22", "distribuidor": "DTS TECNOLOGIA
	 *                    S.A", "codBodega": "114", "bodega": "BODEGA RUTA 24",
	 *                    "departamento": "COCL\u00E9", "municipio": "COCL\u00E9",
	 *                    "distrito": "", "usuario": "99887768", "vendedor": "961",
	 *                    "ruta": "RUTA TECH S.A.", "nombrePdv": "TIENDA MARY",
	 *                    "dia": "27", "mes": "07", "anio": "2016", "fecha":
	 *                    "2016-07-27 09:59:22.0", "asignado": "NO", "gestion": "NO
	 *                    VENTA", "observaciones": "Tiene art\u00EDculos en
	 *                    existencia." } ] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: Usuario.", "clase": "CtrlReporte",
	 *                  "metodo": "validarInput", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputReporteCumplimientoVisita getReporteCumplimientoVisita(@Context HttpServletRequest req,
			InputReporteCumplimientoVisita input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputReporteCumplimientoVisita response = new Reporte().getReporteCumplimientoVisita(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getreporteefectividadventa")
	/**
	 * @api {POST} /consultasidra/getreporteefectividadventa/
	 *      [getreporteefectividadventa]
	 * 
	 * @apiName getReporteEfectividadVenta
	 * @apiDescription Servicio para obtener un listado de las ventas realizadas por
	 *                 cada Punto de Venta, en cada punto de venta se realiza una
	 *                 comparaci\u00F3n de ventas segun las fechas que se envien de
	 *                 dos meses distintos.
	 * @apiGroup Reportes
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [idDistribuidor] Identificador del distribuidor que se
	 *           desea obtener. Este campo es opcional.
	 * @apiParam {String} [idBodegaVirtual] Identificador de la bodega virtual de la
	 *           que se vendieron los articulos a consultar. Este campo es opcional.
	 * @apiParam {String} [idJornada] Identificador de la jornada a consultar. Este
	 *           campo es opcional.
	 * @apiParam {String} [idRuta] Identificador de la ruta que se desea obtener.
	 *           Este campo es opcional.
	 * @apiParam {String} [zona] Describe si la Zona de los puntos de venta a
	 *           consultar. Este campo es opcional.
	 * @apiParam {String} fechaInicioMesActual Fecha inicio en formato yyyyMMdd que
	 *           corresponde al Mes que se tomar\u00E9 como base para realizar la
	 *           comparaci\u00F3n de venta.
	 * @apiParam {String} fechaFinMesActual Fecha fin en formato yyyyMMdd que
	 *           corresponde al Mes que se tomar\u00E9 como base para realizar la
	 *           comparaci\u00F3n de venta.
	 * @apiParam {String} fechaInicioMesAnterior Fecha inicio en formato yyyyMMdd
	 *           que corresponde al Mes con el cual se comparar\u00E9n las ventas.
	 * @apiParam {String} fechaFinMesAnterior Fecha fin en formato yyyyMMdd que
	 *           corresponde al Mes con el cual se comparar\u00E9n las ventas.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas",
	 *                  "fechaInicioMesActual":"20170601",
	 *                  "fechaFinMesActual":"20160730",
	 *                  "fechaInicioMesAnterior":"20160601",
	 *                  "fechaFinMesAnterior":"20160630", "idDistribuidor": "22",
	 *                  "idBodegaVirtual": "", "idRuta": "", "idJornada": "",
	 *                  "zona": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" },
	 *                    "datosEfectividad": { "idDistribuidor": "22",
	 *                    "nombreDistribuidor": "DTS TECNOLOGIA S.A",
	 *                    "idBodegaVirtual": "119", "nombreBodega": "BODEGA RUTA
	 *                    25", "idRuta": "25", "nombreRuta": "RUTA BOCAS DEL TORO
	 *                    1", "dpv": { "idPuntoVenta": "61", "departamento": "BOCAS
	 *                    DEL TORO", "municipio": "BOCAS DEL TORO", "distrito": "",
	 *                    "nombrePdv": "TIENDA IV\u00E9NCITO", "comparacion": {
	 *                    "diaSemana": "VIERNES ", "diaMes": "29", "mes": "07",
	 *                    "anio": "2016", "venta": "SI",
	 *                    "cantProductoFacturadoActual": "5",
	 *                    "cantProductoFacturadoAnterior": "0",
	 *                    "montoTotalFacturadoActual": "30",
	 *                    "montoTotalFacturadoAnterior": "0", "diferencia": "30",
	 *                    "variacion": "100", "detalleVenta": { "cantidadVendida":
	 *                    "5", "montoTotalVendido": "30", "tipoProducto":
	 *                    "TARJETASRASCA" } } } } }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: Usuario.", "clase": "CtrlReporte",
	 *                  "metodo": "validarInput", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputReporteEfectividadVenta getReporteEfectividadVenta(@Context HttpServletRequest req,
			InputReporteEfectividadVenta input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputReporteEfectividadVenta response = new Reporte().getReporteEfectividadVenta(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getcountcontrolprecios")
	/**
	 * @api {POST} /consultasidra/getcountcontrolprecios/ [getcountcontrolprecios]
	 * 
	 * @apiName getcountcontrolprecios
	 * @apiDescription Servicio para obtener la cantidad de datos que puede retornar
	 *                 sobre el control de precios con los filtros enviados.
	 * @apiGroup Reportes
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} idDistribuidor Identificador del distribuidor a consultar.
	 * @apiParam {String} [idTipo] Identificador de la ruta o panel a consultar.
	 * @apiParam {String} tipo Describe si el idtipo corresponde a una ruta o panel.
	 * @apiParam {String} [Vendedor] Identificador del vendedor a consultar.
	 * @apiParam {String} [idBodegaVirtual] Identificador de la bodega virtual de la
	 *           que se vendieron los articulos a consultar.
	 * @apiParam {String} [articulo] Identificador del art\u00EDculo a consultar.
	 * @apiParam {String} fechaInicio Fecha en formato yyyyMMdd desde la cu\u00E9l
	 *           se desea consultar los art\u00EDculos.
	 * @apiParam {String} fechaFin Fecha en formato yyyyMMdd hasta la cu\u00E9l se
	 *           desea consultar los articulos.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas", "idDistribuidor": "53",
	 *                  "idTipo": "12", "tipo": "PANEL", "vendedor": "904",
	 *                  "idBodegaVirtual": "", "articulo": "", "fechaInicio":
	 *                  "20160506", "fechaFin": "20160515" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" },
	 *                    "cantRegistros": "0" }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-62", "mostrar": "1", "descripcion": "La fecha fin debe ser
	 *                  mayor a la fecha inicio", "clase": "CtrlPrecios", "metodo":
	 *                  "validarDatos", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public RespuestaControlPrecios getCountControlPrecios(@Context HttpServletRequest req, InputControlPrecios input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		RespuestaControlPrecios response = new ControlPrecios().getCountCPrecios(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getcontrolprecios")
	/**
	 * @api {POST} /consultasidra/getcontrolprecios/ [getcontrolprecios]
	 * 
	 * @apiName getcontrolprecios
	 * @apiDescription Servicio para obtener la cantidad de datos que puede retornar
	 *                 sobre el control de precios con los filtros enviados.
	 * @apiGroup Reportes
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} idDistribuidor Identificador del distribuidor a consultar.
	 * @apiParam {String} [idTipo] Identificador de la ruta o panel a consultar.
	 * @apiParam {String} tipo Describe si el idtipo corresponde a una ruta o panel.
	 * @apiParam {String} [Vendedor] Identificador del vendedor a consultar.
	 * @apiParam {String} [idBodegaVirtual] Identificador de la bodega virtual de la
	 *           que se vendieron los articulos a consultar.
	 * @apiParam {String} [articulo] Identificador del art\u00EDculo a consultar.
	 * @apiParam {String} fechaInicio Fecha en formato yyyyMMdd desde la cu\u00E9l
	 *           se desea consultar los art\u00EDculos.
	 * @apiParam {String} fechaFin Fecha en formato yyyyMMdd hasta la cu\u00E9l se
	 *           desea consultar los articulos.
	 * @apiParam {String} max Registro m\u00E9ximo a consultar para pagineo .
	 * @apiParam {String} min Registro m\u00EDnimo a consultar para pagineo .
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "usuario.pruebas", "idDistribuidor": "53",
	 *                  "idTipo": "12", "tipo": "PANEL", "vendedor": "904",
	 *                  "idBodegaVirtual": "", "articulo": "", "fechaInicio":
	 *                  "20160506", "fechaFin": "20160515", "max":"100", "min":"1" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" }, "articulos": [
	 *                    { "idVenta": "42", "numero": "", "serie": " ",
	 *                    "folioSidra": "11221", "serieSidra": "a", "tipoDocumento":
	 *                    "TCK", "fecha": "27/07/2016 17:15:20", "articulo": "665",
	 *                    "descripcion": "TARJETA PREPAGO DE $2.00", "cantidad":
	 *                    "1", "serieArticulo": "206893415", "precioInicial":
	 *                    "7.339", "descuentoSCL": "0", "descuentoSIDRA": "0",
	 *                    "precioVenta": "7.339" }, { "idVenta": "42", "numero": "",
	 *                    "serie": " ", "folioSidra": "11221", "serieSidra": "a",
	 *                    "tipoDocumento": "TCK", "fecha": "27/07/2016 17:15:20",
	 *                    "articulo": "1618", "descripcion": "Z-TARJETA SIM PREPAGO
	 *                    LINEA CON NUMERO", "cantidad": "1", "serieArticulo":
	 *                    "8950702110514468103", "precioInicial": "10",
	 *                    "descuentoSCL": "0", "descuentoSIDRA": "0", "precioVenta":
	 *                    "10" } ] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-62", "mostrar": "1", "descripcion": "La fecha fin debe ser
	 *                  mayor a la fecha inicio", "clase": "CtrlPrecios", "metodo":
	 *                  "validarDatos", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public RespuestaControlPrecios getControlPrecios(@Context HttpServletRequest req, InputControlPrecios input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		RespuestaControlPrecios response = new ControlPrecios().getCPrecios(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getestadonumrecarga")
	/**
	 * @api {POST} /consultasidra/getestadonumrecarga/ [getEstadoNumRecarga]
	 * 
	 * @apiName getEstadoNumRecarga
	 * @apiDescription Servicio para consultar el estado de un numero de recarga.
	 * @apiGroup Puntos_de_Venta
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} token Token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} idPDV Identificador del punto de venta a consultar.
	 * @apiParam {String} numRecarga N\u00FAmero de recarga a consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "token": "WEB", "usuario": "usuario.pruebas",
	 *                  "codDispositivo": "ASDF123QWER456ZXCV789", "idPDV": "58",
	 *                  "numRecarga": "44444444" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "202", "mostrar": "1", "descripcion":
	 *                    "Recursos recuperados exitosamente.", "clase":
	 *                    "OperacionConsultaNumRecarga", "metodo": "doGet",
	 *                    "excepcion": " ", "tipoExepcion": "" }, "estado":
	 *                    "ACTIVADO" }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-754", "mostrar": "1", "descripcion": "Los siguientes
	 *                  n\u00FAmeros de recarga no existen en el punto de venta:
	 *                  55545555", "clase": "OperacionConsultaNumRecarga", "metodo":
	 *                  "doGet", "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputConsultaNumRecarga getEstadoNumRecarga(@Context HttpServletRequest req,
			InputConsultaNumRecarga input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputConsultaNumRecarga response = new ConsultaNumRecarga().getConsultaNumRecarga(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/generareportexz")
	/**
	 * @api {POST} /consultasidra/generareportexz/ [generareportexz]
	 * 
	 * @apiName generareportexz
	 * @apiDescription Servicio para generar reporte x o z. El cual contiene un
	 *                 resumen del total de ventas por dispositivo en caso de ser
	 *                 reporte z o vendedor en caso de ser reporte x
	 * @apiGroup Reportes
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} token Token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo C\u00F3digo del dispositivo para
	 *           validaci\u00F3n de token.
	 * @apiParam {String} idJornada Identificador de la jornada de la que se desea
	 *           obtener el reporte
	 * @apiParam {String} idVendedor Identificador del vendedor del cual se desea
	 *           generar reporte X, este campo solo es obligatorio para reporte X.
	 * @apiParam {String} dispositivo listado de C\u00F3digos de dispositivos de los
	 *           cuales se desea generar reporte Z.
	 * @apiParam {String} tipoReporte Describe si el reporte a generar ser\u00E9 X o
	 *           Z.
	 * @apiParam {String} fecha fecha de la cual se desea generar el reporte.
	 * @apiParam {String} origen indica si se esta solicitando el reporte desde la
	 *           app m\u00F3vil o Web.
	 * @apiParam {String} idReporte identificador del reporte a consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token":"WEB",
	 *                  "codArea":"505", "usuario":"usuario123",
	 *                  "codDispositivo":"", "idJornada":"", "idVendedor":"",
	 *                  "dispositivos":["DSHFKJS894654654DS","SDFJ98UJMKLSDFS"],
	 *                  "tipoReporte":"", "fecha":"", "origen":"PC/MOVIL",
	 *                  "idReporte":"" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token":"SDSFDSFSDFSFD",
	 *                    "respuesta": { "codResultado": "12", "mostrar": "0",
	 *                    "descripcion": "OK. Datos obtenidos exitosamente",
	 *                    "clase": " ", "metodo": " ", "excepcion": " ",
	 *                    "tipoExepcion": "" }, "datos": { "idReporte": "8", "tipo":
	 *                    "Z", "acumuladoVentas": "52.5197", "fecha": "26/07/2016
	 *                    00:00:00", "ventas": [ { "descripcion": "FONDOS
	 *                    INICIALES", "monto": "52.5197", "cantVentas": "2" }, {
	 *                    "descripcion": "DEVOLUCIONES", "monto": "0", "cantVentas":
	 *                    "0" } ], "totalVentasNetas": "52.5197",
	 *                    "transaccionesMonetarias": [ { "descripcion": "FONDOS
	 *                    INICIALES", "monto": "0" }, { "descripcion": "DINERO EN
	 *                    GAVETA", "monto": "52.5197" }, { "descripcion": "ENTREGA
	 *                    PARCIAL", "monto": "0", "cantVentas": "0" }, {
	 *                    "descripcion": "PAGOS A TERCEROS", "monto": "0",
	 *                    "cantVentas": "0" } ], "otrasTransacciones": [ {
	 *                    "descripcion": "DESCUENTOS", "monto": "0", "cantVentas":
	 *                    "0" }, { "descripcion": "ANULACIONES", "monto": "0",
	 *                    "cantVentas": "0" }, { "descripcion": "NO VENTAS",
	 *                    "monto": "0", "cantVentas": "0" } ], "formasPago": [ {
	 *                    "formaPago": "EFECTIVO", "cantidad": "2", "monto":
	 *                    "28.9197" }, { "formaPago": "CHEQUE", "monto": "0" }, {
	 *                    "formaPago": "CREDITO", "cantidad": "1", "monto": "23.6" }
	 *                    ], "totales": [ { "descripcion": "TOTAL GRAVADO", "monto":
	 *                    "0" }, { "descripcion": "TOTAL EXENTO", "monto": "52.5197"
	 *                    }, { "descripcion": "TOTAL NO SUJETO", "monto": "0" } ],
	 *                    "totalVenta": "52.5197" } }
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-67", "mostrar": "1", "descripcion": "El par\u00E9metro de
	 *                  entrada \\\"codigoDispositivo\"\\ esta vac\u00EDo", "clase":
	 *                  "CtrlReporteXZ", "metodo": "validarDatos", "excepcion": " ",
	 *                  "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputReporteXZ generaReporteXZ(@Context HttpServletRequest req, InputReporteXZ input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputReporteXZ response = new ReporteXZ().generaReporteXZ(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getreportepdv")
	/**
	 * @api {POST} /consultasidra/getreportepdv/ [getreportepdv]
	 * 
	 * @apiName getreportepdv
	 * @apiDescription Servicio para obtener los datos de ventas registradas por
	 *                 punto de venta.
	 * @apiGroup Reportes
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [idDistribuidor] Identificador del distribuidor que se
	 *           desea consultar.
	 * @apiParam {String} [idPDV] Identificador del punto de venta que se desea
	 *           consultar.
	 * @apiParam {String} [idVendedor] Identificador del vendedor que se desea
	 *           consultar.
	 * @apiParam {String} [idRuta] Identificador de la ruta que se desea consultar.
	 * @apiParam {String} [categoria] Categor\u00EDa del punto de venta que se desea
	 *           consultar.
	 * @apiParam {String} [departamento] Departamento del punto de venta que se
	 *           desea consultar.
	 * @apiParam {String} [municipio] Municipio del punto de venta que se desea
	 *           consultar.
	 * @apiParam {String} [distrito] Distrito del punto de venta que se desea
	 *           consultar.
	 * @apiParam {String} [estado] Estado del punto de venta que se desea consultar.
	 * @apiParam {String} [fechaDesde] Fecha desde la cual se desea consultar en
	 *           formato dd/MM/yyyy.
	 * @apiParam {String} [fechaHasta] Fecha final se desea consultar en formato
	 *           dd/MM/yyyy, el rango m\u00E9ximo es de 6 meses (180 d\u00EDas)
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: {
	 *                  "codArea":"505", "usuario":"victor.cifuentes",
	 *                  "idDistribuidor":"", "idPDV":"", "idVendedor":"",
	 *                  "idRuta":"", "categoria": "", "departamento": "",
	 *                  "municipio": "", "distrito": "", "estado": "",
	 *                  "fechaDesde":"01/06/2016", "fechaHasta":"31/08/2016" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "202", "mostrar": "1", "descripcion":
	 *                    "Recursos recuperados exitosamente.", "clase":
	 *                    "OperacionReportePDV", "metodo": "doGet", "excepcion": "
	 *                    ", "tipoExepcion": "" }, "datos": { "idDistribuidor":
	 *                    "25", "nombreDistribuidor": "Distribuidor Celular Center",
	 *                    "puntosDeVenta": { "idDistribuidor": "25", "idPDV": "59",
	 *                    "nombrePDV": "TIENDA MARY", "idRuta": "24", "nombreRuta":
	 *                    "RUTA TECH S.A.", "idVendedor": "961", "canal":
	 *                    "MULTIMARCA", "subcanal": "DTS", "categoria": "B",
	 *                    "departamento": "DARI\u00E9N", "municipio": "CHEPIGANA",
	 *                    "distrito": "", "estado": "ACTIVO", "creado_por":
	 *                    "susana.barrios", "creado_el": "27/07/2016 10:26:55",
	 *                    "ventas": [ { "anio": "2016", "mes": "Junio",
	 *                    "sumFacturacion": "0" }, { "anio": "2016", "mes": "Julio",
	 *                    "sumFacturacion": "0" }, { "anio": "2016", "mes":
	 *                    "Agosto", "sumFacturacion": "208.39" } ] } } }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-138", "mostrar": "1", "descripcion": " Datos no
	 *                  num\u00E9ricos en ID Vendedor.", "clase": "CtrlReportePDV",
	 *                  "metodo": "validarInput", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputReportePDV getReportePDV(@Context HttpServletRequest req, InputReportePDV input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputReportePDV response = new Reporte().getReportePDV(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getsaldopayment")
	/**
	 * @api {POST} /consultasidra/getsaldopayment/ [getsaldopayment]
	 * 
	 * @apiName getsaldopayment
	 * @apiDescription Servicio para consultar el saldo payment de un numero de
	 *                 recarga.
	 * @apiGroup Consulta
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} token Token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} idVendedor Identificador del vendedor a consultar.
	 * @apiParam {String} numRecarga n\u00FAmero de telefono a consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token": "WEB",
	 *                  "codDispositivo":
	 *                  "CF3F305997BF98946B3B1DF77862936E1620E01E", "codArea":
	 *                  "505", "usuario": "usuario.pruebas", "idVendedor": "904",
	 *                  "numRecarga": "40258541" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token": "WEB",
	 *                    "respuesta": { "codResultado": "12", "mostrar": "0",
	 *                    "descripcion": "OK. Datos obtenidos exitosamente",
	 *                    "clase": " ", "metodo": " ", "excepcion": " ",
	 *                    "tipoExepcion": "" }, "saldoPayment":"1000" }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "token":"WEB", "respuesta": {
	 *                  "codResultado": "-522", "mostrar": "1", "descripcion": "El
	 *                  idVendedor y n\u00FAmero de recarga no coinciden", "clase":
	 *                  "CtrlSaldoPayment", "metodo": "validarDatos", "excepcion": "
	 *                  ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputConsultaSaldoPayment getSaldoPayment(@Context HttpServletRequest req,
			InputConsultaSaldoPayment input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputConsultaSaldoPayment response = new ConsultaSaldoPayment().getConsultaSaldoPayment(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/*************************************************************************/
	/*************************** SERVICIOS RELEASE 6 *************************/
	/*************************************************************************/
	@Path("/getimagenvisita/")
	/**
	 * @api {POST} /consultasidra/getimagenvisita/ [getimagenvisita]
	 * 
	 * @apiName getimagenvisita
	 * @apiDescription Servicio para obtener el listado de imagenes asociadas a una
	 *                 visita.
	 * @apiGroup ImagenPDV
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} [idPDV] Id del punto de venta que se desea consultar.
	 * @apiParam {String} [idVisita] Id de la visita que se desea consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "token": "WEB", "usuario": "usuarioPruebas",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idPDV": "5", "idVisita":"1" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "20", "mostrar": "1", "descripcion": "OK.
	 *                    Carga de im\u00E9genes correctamente.", "clase":
	 *                    "CtrlCargaFile", "metodo": "getImagenVisita", "excepcion":
	 *                    " ", "tipoExepcion": "" }, "imgAsociadas": [ { "idImgPDV":
	 *                    "28", "observaciones": "" }, { "idImgPDV": "29",
	 *                    "observaciones": "" } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-797", "mostrar": "1", "descripcion": "No se encontraron
	 *                  registros de im\u00E9genes asociadas.", "clase":
	 *                  "CtrlCargaFile", "metodo": "getImagenVisita", "excepcion": "
	 *                  ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputImagen getImagenVisita(@Context HttpServletRequest req, InputCargaFile input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputImagen response = new CargaFile().getImagenVisita(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/validaseries")
	/**
	 * @api {POST} /consultasidra/validaseries/ [validaseries]
	 * 
	 * @apiName validaseries
	 * @apiDescription Servicio para verificar que las series que ser\u00E9n
	 *                 vendidas se encuentran en estado Disponible.
	 * @apiGroup Consulta
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea c\u00F3digo del pa\u00EDs al que se desea
	 *           consultar la informaci\u00F3n.
	 * @apiParam {String} token token de validaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo codigo del dispositivo desde el cual se
	 *           realiza la operaci\u00F3n.
	 * @apiParam {String} idBodegaVendedor id de la bodega del vendedor que se desea
	 *           consultar.
	 * @apiParam {String} idArticulo id del art\u00EDculo a verificar.
	 * @apiParam {String} serieInicial serie a verificar.
	 * @apiParam {String} serieFinal serie final a verificar, en caso de verificar
	 *           un rango.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token": "WEB",
	 *                  "codArea": "505", "usuario": "oscar.sanchez",
	 *                  "codDispositivo": "123123123123", "idBodegaVendedor":"",
	 * 
	 *                  "listadoSeries": [{ "idArticulo":"90", "serieInicial":
	 *                  "123123123", "serieFinal": ""
	 * 
	 *                  }, { "idArticulo":"1090", "serieInicial": "111111",
	 *                  "serieFinal": "111112" }] }
	 *
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token":"WEB" "respuesta":
	 *                    { "codResultado": "20", "mostrar": "0", "descripcion":
	 *                    "OK. Series Correctas.", "clase": " ", "metodo": " ",
	 *                    "excepcion": " ", "tipoExepcion": "" } }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "token":"WEB", "respuesta": {
	 *                  "codResultado": "-473", "mostrar": "1", "descripcion": "Las
	 *                  siguientes series o art\u00EDculos no se encuentran
	 *                  disponibles en inventario. ", "clase":
	 *                  "CtrlValidaArticulos", "metodo": "validaSeries",
	 *                  "excepcion": " ", "tipoExepcion": "Generales" },
	 *                  "articulos": { "idArticulo": "1011", "tipoInventario":
	 *                  "INV_TELCA", "descripcionArticulo": "TARJETA PREPAGADA $1",
	 *                  "serieInicial": "123238020001001", "serieFinal":
	 *                  "123238020001020", "grupo": "TARJETASRASCA", "estado": "NO
	 *                  EXISTE" } }
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputValidaInventario getSeriesInvalidas(@Context HttpServletRequest req, InputValidaArticulo input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputValidaInventario response = new ValidaSerie().validaSeries(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getanulacion/")
	/**
	 * @api {POST} /consultasidra/getanulacion/ [getanulacion]
	 * 
	 * @apiName getanulacion
	 * @apiDescription Servicio para obtener el listado de anulaciones realizadas.
	 * @apiGroup Anulacion
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} [idJornada] Identificador de la jornada que se desea
	 *           consultar.
	 * @apiParam {String} [idVendedor] Identificador del vendedor que se desea
	 *           consultar.
	 * @apiParam {String} [idVenta] Identificador de la venta a consultar.
	 * @apiParam {String} [razonAnulacion] Raz\u00F3n de anulaci\u00F3n que se desea
	 *           consultar.
	 * @apiParam {String} [fechaInicio] Fecha en formato yyyyMMdd desde la cu\u00E9l
	 *           se desea consultar la anulaci\u00F3n.
	 * @apiParam {String} [fechaInicio] Fecha en formato yyyyMMdd hasta la cu\u00E9l
	 *           se desea consultar la anulaci\u00F3n. El tiempo m\u00E9ximo del
	 *           rango no puede ser mayor a 31 d\u00EDas.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "token": "WEB", "usuario": "usuarioPruebas",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idJornada":"", "idVendedor":"", "idVenta":"",
	 *                  "razonAnulacion":"", "fechaInicio":"20161101",
	 *                  "fechaFin":"20161130" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token": "WEB",
	 *                    "respuesta": { "codResultado": "22", "mostrar": "1",
	 *                    "descripcion": "OK. Carga de anulaciones correctamente.",
	 *                    "clase": "OperacionAnulacion", "metodo": "doGet",
	 *                    "excepcion": " ", "tipoExepcion": "" }, "anulaciones": {
	 *                    "idAnulacion": "4", "fechaAnulacion": "28/10/2016
	 *                    16:12:02", "idJornada": "4", "idVendedor": "2242",
	 *                    "nombreVendedor": "usuario.sidra", "idVenta": "8",
	 *                    "razonAnulacion": "CANCELACION", "creado_el": "11/11/2016
	 *                    09:14:34", "creado_por": "usuario.pruebas" } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "token": "WEB", "respuesta": {
	 *                  "codResultado": "-61", "mostrar": "1", "descripcion": "Debe
	 *                  ingresar ambas fechas: Fecha inicio y fecha Fin", "clase":
	 *                  "CtrlAnulacion", "metodo": "validarInput", "excepcion": " ",
	 *                  "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputAnulacion getAnulacion(@Context HttpServletRequest req, InputAnulacion input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputAnulacion response = new Anulacion().getAnulacion(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getreporteinvjornada")
	/**
	 * @api {POST} /consultasidra/getreporteinvjornada/ [getreporteinvjornada]
	 * 
	 * @apiName getreporteinvjornada
	 * @apiDescription Servicio para obtener las diferentes cantidades de
	 *                 inventario.
	 * @apiGroup Reportes
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} [idDTS] Identificador del distribuidor que se desea
	 *           consultar.
	 * @apiParam {String} [idJornada] Identificador de la jornada que se desea
	 *           consultar.
	 * @apiParam {String} [idVendedor] Identificador del vendedor que se desea
	 *           consultar.
	 * @apiParam {String} [idRutaPanel] Identificador de la ruta o panel que se
	 *           desea consultar.
	 * @apiParam {String} [tipoRutaPanel] Tipo ruta o panel a consultar.
	 * @apiParam {String} [idArticulo] Identificador del art\u00EDculo que se desea
	 *           consultar.
	 * @apiParam {String} [tipoGrupo] Tipo del grupo a consultar.
	 * @apiParam {String} [tipoInv] Tipo de inventario a consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "token":"WEB",
	 *                  "codDispositivo":"", "codArea":"505",
	 *                  "usuario":"victor.cifuentes", "idDTS":"25", "idJornada":"",
	 *                  "idVendedor":"", "idRutaPanel":"", "tipoRutaPanel":"",
	 *                  "idArticulo":"", "tipoGrupo":"", "tipoInv":"" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token": "WEB",
	 *                    "respuesta": { "codResultado": "70", "mostrar": "1",
	 *                    "descripcion": "OK. Datos de inventario inicial obtenidos
	 *                    correctamente.", "clase":
	 *                    "OperacionReporteCantInvJornada", "metodo": "doGet",
	 *                    "excepcion": " ", "tipoExepcion": "" }, "datos": {
	 *                    "idJornada": "253", "idDTS": "48", "nombreDTS":
	 *                    "DISTRIBUIDOR OCCIDENTAL", "idVendedor": "2563",
	 *                    "nombreVendedor": "El\u00EDas Daniel Tobar ",
	 *                    "usuarioVendedor": "elias.tobar", "idRutaPanel": "40",
	 *                    "tipoRutaPanel": "RUTA", "nombreRutaPanel": "EL\u00EDAS
	 *                    TOBAR", "articulos": [ { "idArticulo": "27",
	 *                    "descripcion": "PELOTA FUTBOL ", "tipoInv": "INV_SIDRA",
	 *                    "cantInicial": "75", "cantReservada": "0", "cantVendida":
	 *                    "0", "cantProcDevolucion": "2", "cantDevuelta": "0",
	 *                    "cantProcSiniestro": "0", "cantSiniestrada": "0",
	 *                    "cantFinal": "73" }, { "idArticulo": "1011",
	 *                    "descripcion": "TARJETA PREPAGADA $1", "tipoInv":
	 *                    "INV_TELCA", "cantInicial": "11", "cantReservada": "0",
	 *                    "cantVendida": "2", "cantProcDevolucion": "1",
	 *                    "cantDevuelta": "0", "cantProcSiniestro": "0",
	 *                    "cantSiniestrada": "0", "cantFinal": "8" }, {
	 *                    "idArticulo": "1285", "descripcion": "KIT APPLE IPHONE
	 *                    16GB BLANCO", "tipoInv": "INV_TELCA", "cantInicial": "3",
	 *                    "cantReservada": "0", "cantVendida": "0",
	 *                    "cantProcDevolucion": "1", "cantDevuelta": "0",
	 *                    "cantProcSiniestro": "0", "cantSiniestrada": "0",
	 *                    "cantFinal": "2" } ] } }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "token": "WEB", "respuesta": {
	 *                  "codResultado": "-847", "mostrar": "1", "descripcion": "No
	 *                  se encontraron datos de inventario inicial.", "clase":
	 *                  "OperacionReporteCantInvJornada", "metodo": "doGet",
	 *                  "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputReporteCantInvJornada getReporteCantVendida(@Context HttpServletRequest req,
			InputReporteCantInvJornada input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputReporteCantInvJornada response = new Reporte().getReporteInvJornada(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getfechajornada")
	/**
	 * @api {POST} /consultasidra/getfechajornada/ [getfechajornada]
	 * 
	 * @apiName getfechajornada
	 * @apiDescription Servicio para obtener la fecha de cierre de la jornada de un
	 *                 vendedor.
	 * @apiGroup Jornada
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} idVendedor Identificador del vendedor que se desea
	 *           consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: {
	 *                  "codArea":"505", "usuario":"victor.cifuentes",
	 *                  "idVendedor":"" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "72", "mostrar": "1", "descripcion": "OK.
	 *                    Fecha de cierre obtenida correctamente.", "clase":
	 *                    "OperacionJornada", "metodo": "getFechaJornada",
	 *                    "excepcion": " ", "tipoExepcion": "" }, "jornada": {
	 *                    "idVendedor": "2564", "fechaCierre": "30122016",
	 *                    "creado_el": "29/12/2016 15:08:48", "creado_por":
	 *                    "victor.cifuentes", "modificado_el": "29/12/2016
	 *                    15:10:19", "modificado_por": "victor.cifuentes" } }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-199", "mostrar": "1", "descripcion": "Los siguientes datos
	 *                  faltan o son incorrectos: ID Vendedor.", "clase":
	 *                  "CtrlJornada", "metodo": "validarInput", "excepcion": " ",
	 *                  "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputJornada getFechaJornada(@Context HttpServletRequest req, InputJornada input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputJornada response = new Jornada().getFechaJornada(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getfoliosscl")
	/**
	 * @api {POST} /consultasidra/getfoliosscl/ [getfoliosscl]
	 * 
	 * @apiName getfoliosscl
	 * @apiDescription Servicio para obtener la fecha de cierre de la jornada de un
	 *                 vendedor.
	 * @apiGroup SCL
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codOficina C\u00F3digo de la oficina del vendedor que se
	 *           desea consultar.
	 * @apiParam {String} idDTS Identificador del distribuidor que se desea
	 *           consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "victor.cifuentes", "codOficina": "",
	 *                  "idDTS": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "73", "mostrar": "1", "descripcion": "OK.
	 *                    Folios SCL obtenidos correctamente.", "clase":
	 *                    "OperacionFoliosSCL", "metodo": "doGet", "excepcion": " ",
	 *                    "tipoExepcion": "" }, "folios": { "tipoDocumento":
	 *                    "FACTURA CONTADO", "serie": "PA", "noInicialFolio":
	 *                    "189999999", "noFinalFolio": "199999999" } }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-851", "mostrar": "1", "descripcion": "El par\u00E9metro de
	 *                  entrada \"codVendedor\" esta vac\u00EDo.", "clase":
	 *                  "CtrlFoliosSCL", "metodo": "validarInput", "excepcion": " ",
	 *                  "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputConfiguracionFolioVirtual getFolioSCL(@Context HttpServletRequest req, InputFolioVirtual input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputConfiguracionFolioVirtual response = new FolioSCL().getFolioSCL(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getoficinasscl")
	/**
	 * @api {POST} /consultasidra/getoficinasscl/ [getoficinasscl]
	 * 
	 * @apiName getoficinasscl
	 * @apiDescription Servicio para obtener las oficinas de SCL.
	 * @apiGroup SCL
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "usuario": "victor.cifuentes" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "74", "mostrar": "1", "descripcion": "OK.
	 *                    Oficinas SCL obtenidas correctamente.", "clase":
	 *                    "OperacionOficinasSCL", "metodo": "doGet", "excepcion": "
	 *                    ", "tipoExepcion": "" }, "oficinas": [ { "nombres":
	 *                    "SUCURSAL PRUEBA", "codOficina": "16" }, { "nombres":
	 *                    "SUCURSAL LOS ANDES", "codOficina": "13" } ] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-868", "mostrar": "1", "descripcion": "No se encontraron
	 *                  datos de oficinas SCL.", "clase": "OperacionOficinasSCL",
	 *                  "metodo": "doGet", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputVendedorDTS getOficinasSCL(@Context HttpServletRequest req, InputVendedorDTS input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputVendedorDTS response = new FolioSCL().getOficinasSCL(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getpdvdisponible")
	/**
	 * @api {POST} /consultasidra/getpdvdisponible/ [getpdvdisponible]
	 * 
	 * @apiName getpdvdisponible
	 * @apiDescription Servicio para obtener los datos de Panels configuradas.
	 * @apiGroup Puntos de Venta
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} idPDV Identificador del punto de venta del que se desea
	 *           obtener datos.
	 * @apiParam {String} idDTS Identificador del distribuidor del que se desea
	 *           obtener datos.
	 * @apiParam {String} min Valor numerico minimo que permite consultar el listado
	 *           por rangos. Este campo es opcional.
	 * @apiParam {String} max Valor numerico maximo que permite consultar el listado
	 *           por rangos. Este campo es opcional.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "token": "WEB", "usuario": "usuarioPruebas",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idPDV": "", "idDTS": "1", "min": "1", "max": "10" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token":"WEB", "respuesta":
	 *                    { "codResultado": "25", "mostrar": "1", "descripcion":
	 *                    "Ok. Puntos de venta obtenidos exitosamente." },
	 *                    "puntoDeVenta": [ { "idPDV": "1", "nombrePDV": "TIENDA LA
	 *                    MEJOR" }, { "idPDV": "2", "nombrePDV": "Tienda los
	 *                    canches" } ] }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "token": "WEB", "respuesta": {
	 *                  "codResultado": "-541", "mostrar": "1", "descripcion": "No
	 *                  se encontraron puntos de venta disponibles.", "clase":
	 *                  "CtrlPuntoVenta", "metodo": "getPDVDisponible", "excepcion":
	 *                  " ", "tipoExepcion": " " } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputpdvDirec doGetPDVDisponible(@Context HttpServletRequest req, InputConsultaPDV input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputpdvDirec response = new PuntoVenta().getPDVDisponible(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getcountpdvdisponible")
	/**
	 * @api {POST} /consultasidra/getcountpdvdisponible/ [getcountpdvdisponible]
	 * 
	 * @apiName getcountpdvdisponible
	 * @apiDescription Servicio para contar todos los PDVs configurados y
	 *                 disponibles.
	 * @apiGroup Puntos de Venta
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} idPDV Identificador del punto de venta del que se desea
	 *           obtener datos.
	 * @apiParam {String} idDTS Identificador del distribuidor del que se desea
	 *           obtener datos.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "token": "WEB", "usuario": "usuarioPruebas",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idPDV": "", "idDTS": "1" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token":"WEB", "respuesta":
	 *                    { "codResultado": "77", "mostrar": "1", "descripcion":
	 *                    "OK. Conteo de PDVs Disponibles exitoso." },
	 *                    "cantRegistros": "1" }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "token": "WEB", "respuesta": {
	 *                  "codResultado": "-541", "mostrar": "1", "descripcion": "No
	 *                  se encontraron puntos de venta disponibles.", "clase":
	 *                  "CtrlPuntoVenta", "metodo": "getPDVDisponible", "excepcion":
	 *                  " ", "tipoExepcion": " " } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputpdvDirec doGetCountPDVDisponible(@Context HttpServletRequest req, InputConsultaPDV input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputpdvDirec response = new PuntoVenta().getCountPDVDisponible(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getadjuntogestion/")
	/**
	 * @api {POST} /consultasidra/getadjuntogestion/ [getadjuntogestion]
	 * 
	 * @apiName getadjuntogestion
	 * @apiDescription Servicio para obtener adjuntos.
	 * @apiGroup AdjuntoGestion
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} idAdjunto Identificador del adjunto a consultar.
	 * @apiParam {String} idGestion Identificador de la gestion a consultar.
	 * @apiParam {String} gestion Nombre de la gestion a consultar.
	 * 
	 * @apiParamExample {json} Ejemplo par\u00E9metros de Entrada: { "codArea":
	 *                  "505", "token": "WEB", "usuario": "usuario.pruebas",
	 *                  "codDispositivo": "64E1CE4A48BF656974C376B7905F7E62",
	 *                  "idAdjunto": "10", "idGestion": "", "gestion": "" }
	 * 
	 * @apiSuccessExample {json} Respuesta-\u00E9xito: { "token": "WEB",
	 *                    "respuesta": { "codResultado": "12", "mostrar": "0",
	 *                    "descripcion": "OK. Datos obtenidos exitosamente",
	 *                    "clase": " ", "metodo": " ", "excepcion": " ",
	 *                    "tipoExepcion": "" }, "adjunto": { "idAdjunto": "10",
	 *                    "idGestion": "1", "gestion": "REMESA", "adjunto":
	 *                    "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkG", "nombreArchivo":
	 *                    "Adjunto 1", "tipoArchivo": "Imagen", "extension": "JPG",
	 *                    "tipoDocumento": "DPI" } }
	 * 
	 * @apiErrorExample {json} Respuesta-Error: { "token": "WEB", "respuesta": {
	 *                  "codResultado": "-205", "mostrar": "1", "descripcion": "No
	 *                  existen registros con esos par\u00E9metros.", "clase":
	 *                  "OperacionCargaFile", "metodo": "getImagenPDV", "excepcion":
	 *                  " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputAdjuntoGestion getAdjuntoGestion(@Context HttpServletRequest req, InputAdjuntoGestion input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputAdjuntoGestion response = new AdjuntoGestion().getAdjunto(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getofertasruta")
	/**
	 * @api {POST} /consultasidra/getofertasruta/ [getofertasruta]
	 * 
	 * @apiName getofertasruta
	 * @apiDescription Servicio para obtener los datos de Condiciones de Oferta de
	 *                 una ruta espec\u00EDfica.
	 * @apiGroup CondicionOferta
	 * @apiVersion 1.0.0
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} token token de validaci\u00F3n de autenticaci\u00F3n de
	 *           usuario.
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [idRuta] Identificador de la ruta a consultar.
	 * 
	 * @apiParamExample {json} Ejemplo parametros de Entrada: { "token": "WEB",
	 *                  "codDispositivo": "B81A18BA1F8131C818E91CA3D19", "usuario":
	 *                  "usuario.pruebas", "codArea": "505", "idRuta": "10" }
	 * 
	 * @apiSuccessExample {} JSON Respuesta-\u00E9xito: { "token": "WEB",
	 *                    "respuesta": { "codResultado": "202", "mostrar": "1",
	 *                    "descripcion": "Recursos recuperados exitosamente.",
	 *                    "clase": "OperacionCondicionOferta", "metodo": "doGet",
	 *                    "excepcion": " ", "tipoExepcion": "" }, "condicionesPdv":
	 *                    [{ "idCondicion": "2", "idOfertaCampania": "5",
	 *                    "nombreCampania": "Camapania Descuentos Esperanza",
	 *                    "nombre": "Condicion PDV Punto 10", "tipoGestion":
	 *                    "VENTA", "tipoCondicion": "ARTICULO", "estado": "ALTA",
	 *                    "creadoEl": "28/03/2017 17:27:28", "creadoPor":
	 *                    "usuario.pruebas", "modificadoEl": "", "modificadoPor":
	 *                    "", "detalle": [ { "idCondicion": "1", "tipo": "PDV",
	 *                    "idArticulo": "2", "nombreArticulo": "BLACKBERRY Q10
	 *                    NEGRO", "tipoCliente": "PDV", "tecnologia": "", "tipoInv":
	 *                    "INV_TELCA", "montoInicial": "1", "montoFinal": "0",
	 *                    "tipoDescuento": "PORCENTAJE", "valorDescuento": "5",
	 *                    "idPDV": "1", "nombrePDV": "Reforma 7",
	 *                    "zonaComercialPDV": "", "categoriaPDV": "",
	 *                    "idArticuloRegalo": "103", "nombreArticuloRegalo":
	 *                    "TARJETA RASCA $C20", "cantidadArticuloRegalo": "1",
	 *                    "tipoDescuentoRegalo": "PORCENTAJE",
	 *                    "valorDescuentoRegalo": "100", "estado": "ALTA",
	 *                    "creadoPor": "usuario.pruebas", "creadoEl": "28/03/2017
	 *                    17:27:28", "modificadoPor": "", "modificadoEl": "" }, {
	 *                    "idCondicion": "2", "tipo": "PDV", "idArticulo": "4",
	 *                    "nombreArticulo": "BLACKBERRY Q10 NEGRO", "tipoCliente":
	 *                    "PDV", "tecnologia": "", "tipoInv": "INV_TELCA",
	 *                    "montoInicial": "1", "montoFinal": "0", "tipoDescuento":
	 *                    "PORCENTAJE", "valorDescuento": "5", "idPDV": "2",
	 *                    "nombrePDV": "Reforma 14", "zonaComercialPDV": "",
	 *                    "categoriaPDV": "", "idArticuloRegalo": "103",
	 *                    "nombreArticuloRegalo": "TARJETA RASCA $C20",
	 *                    "cantidadArticuloRegalo": "1", "tipoDescuentoRegalo":
	 *                    "PORCENTAJE", "valorDescuentoRegalo": "100", "estado":
	 *                    "ALTA", "creadoPor": "usuario.pruebas", "creadoEl":
	 *                    "28/03/2017 17:27:28", "modificadoPor": "",
	 *                    "modificadoEl": "" } ] }], "condicionesZonaCat": [{
	 *                    "idCondicion": "2", "idOfertaCampania": "5",
	 *                    "nombreCampania": "Campania Descuentos Esperanza",
	 *                    "nombre": "Condicion PDV Punto 10", "tipoGestion":
	 *                    "VENTA", "tipoCondicion": "ARTICULO", "estado": "ALTA",
	 *                    "creadoEl": "28/03/2017 17:27:28", "creadoPor":
	 *                    "usuario.pruebas", "modificadoEl": "", "modificadoPor":
	 *                    "", "detalle": [{ "idCondicion": "91", "tipo": "ZONA",
	 *                    "idArticulo": "130", "nombreArticulo": "BLACKBERRY Q10
	 *                    NEGRO", "tipoCliente": "PDV", "tecnologia": "", "tipoInv":
	 *                    "INV_TELCA", "montoInicial": "1", "montoFinal": "0",
	 *                    "tipoDescuento": "PORCENTAJE", "valorDescuento": "5",
	 *                    "idPDV": "15", "nombrePDV": "Managua Central",
	 *                    "zonaComercialPDV": "Managua", "categoriaPDV": "",
	 *                    "idArticuloRegalo": "103", "nombreArticuloRegalo":
	 *                    "TARJETA RASCA $C20", "cantidadArticuloRegalo": "1",
	 *                    "tipoDescuentoRegalo": "PORCENTAJE",
	 *                    "valorDescuentoRegalo": "100", "estado": "ALTA",
	 *                    "creadoPor": "usuario.pruebas", "creadoEl": "28/03/2017
	 *                    17:27:28", "modificadoPor": "", "modificadoEl": "" }] }] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "token": "WEB", "respuesta": {
	 *                  "codResultado": "-396", "mostrar": "1", "descripcion": "No
	 *                  se encontraron condiciones de oferta configuradas.",
	 *                  "clase": "OperacionCondicionOferta", "metodo": "doGet",
	 *                  "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputCondicionOferta getOfertasRuta(@Context HttpServletRequest req, InputCondicionPrincipalOferta input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputCondicionOferta response = new CondicionOferta().getOfertasRuta(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getresumendeuda")
	/**
	 * @api {POST} /consultasidra/getresumendeuda/ [getresumendeuda]
	 * 
	 * @apiName getresumendeuda
	 * @apiDescription Servicio para obtener los datos de usuarios asignados a
	 *                 buzones por pais.
	 * @apiGroup Deuda
	 * @apiVersion 1.0.0
	 * @apiParam (getresumendeuda) {String} codArea C\u00F3digo de \u00E9rea del
	 *           pais.
	 * @apiParam (getresumendeuda) {String} usuario Nombre de usuario que solicita
	 *           la operaci\u00F3n.
	 * @apiParam (getresumendeuda) {String} idDts Identificador del distribuidor a
	 *           consultar.
	 * @apiParam (getresumendeuda) {String} idBodega Identificador de la bodega a
	 *           consultar.
	 * @apiParam {getresumendeuda} [fechaInicio] Fecha en formato yyyyMMdd desde la
	 *           cu\u00E9l se desea consultar la solicitud.
	 * @apiParam {getresumendeuda} [fechaFin] Fecha en formato yyyyMMdd hasta la
	 *           cu\u00E9l se desea consultar la solicitud.
	 * 
	 * 
	 * @apiParamExample {json} Ejemplo parametros de Entrada: { "codArea": "505",
	 *                  "usuario": "usuario.pruebas", "idDts": "", "idBodega": "",
	 *                  "fechaInicio": "", "fechaFin": "" }
	 * 
	 * @apiSuccessExample {} JSON Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente." },
	 * 
	 *                    "listaSolicitudes": [ {"fecha": "02/08/2016 00:00:00",
	 *                    "idSolicitud": "91", "idBodega": "114", "idDTS": "22",
	 *                    "nombreDTS": "DTS TECNOLOGIA S.A", "idBuzon": "11",
	 *                    "nombreBuzon":"", "idBuzonAnterior": "",
	 *                    "nombreBuzonAnterior":"", "tipoSolicitud": "DEUDA",
	 *                    "observaciones": "Observaciones payment", "origen": "PC",
	 *                    "totalDeuda": "", "tasaCambio": "", "estado": "CERRADA",
	 *                    "origenCancelacion":"", "obsCancelacion":"", "creado_el":
	 *                    "03/08/2016 10:00:11", "creado_por": "victor.cifuentes",
	 *                    "modificado_el": "04/08/2016 11:53:26", "modificado_por":
	 *                    "sergio.lujan" }, { "fecha": "03/09/2016 00:00:00",
	 *                    "idSolicitud": "91", "idBodega": "114", "nombreBodega":
	 *                    "PROMIX 1.1", "idDTS": "22", "nombreDTS": "DTS TECNOLOGIA
	 *                    S.A", "idBuzon": "11", "nombreBuzon":"",
	 *                    "idBuzonAnterior": "", "nombreBuzonAnterior":"",
	 *                    "tipoSolicitud": "DEUDA", "observaciones": "Observaciones
	 *                    payment", "origen": "PC", "totalDeuda": "", "tasaCambio":
	 *                    "", "estado": "CERRADA", "origenCancelacion": "",
	 *                    "obsCancelacion": "", "creado_el": "03/08/2016 10:00:11",
	 *                    "creado_por": "victor.cifuentes", "modificado_el":
	 *                    "04/08/2016 11:53:26", "modificado_por": "sergio.lujan" },
	 *                    { "fecha": "04/09/2016 00:00:00", "idSolicitud": "",
	 *                    "idBodega": "", "nombreBodega": "", "idDTS": "",
	 *                    "nombreDTS": "", "idBuzon": "", "nombreBuzon": "",
	 *                    "idBuzonAnterior": "", "nombreBuzonAnterior": "",
	 *                    "tipoSolicitud": "", "observaciones": "", "origen": "",
	 *                    "totalDeuda": "", "tasaCambio": "", "estado": "",
	 *                    "origenCancelacion": "", "obsCancelacion": "",
	 *                    "creado_el": "", "creado_por": "", "modificado_el": "",
	 *                    "modificado_por": "" } ] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-205", "mostrar": "1", "descripcion": "No existen registros
	 *                  con esos par\u00E9metros.", "clase": "OperacionDeuda",
	 *                  "metodo": "doGet", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputDeuda getResumenDeuda(@Context HttpServletRequest req, InputSolicitud input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputDeuda response = new ResumenDeuda().getDeuda(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	/*************************************************************************/
	/*************************** SERVICIOS RELEASE 6.5 ***********************/
	/*************************************************************************/
	@Path("/gettransaccionesdeuda")
	/**
	 * @api {POST} /consultasidra/gettransaccionesdeuda/ [gettransaccionesdeuda]
	 * 
	 * @apiName gettransaccionesdeuda
	 * @apiDescription Servicio para obtener los datos de las transacciones de pago
	 *                 realizadas en las jornadas de una deuda.
	 * @apiGroup Deuda
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} idJornada Identificador de la jornada a consultar.
	 * @apiParam {String} idDTS Identificador del distribuidor a consultar.
	 * 
	 * @apiParamExample {json} Ejemplo parametros de Entrada: { "codArea": "505",
	 *                  "usuario": "victor.cifuentes", "idJornada": "198", "idDTS":
	 *                  "16" }
	 * 
	 * @apiSuccessExample {} JSON Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase":
	 *                    "OperacionTransaccionesDeuda", "metodo": "doGet",
	 *                    "excepcion": " ", "tipoExepcion": "" }, "remesas": [ {
	 *                    "idRemesa": "326", "origen": "MOVIL", "idVendedor": "242",
	 *                    "nombreVendedor": "cesar.casia", "idTipo": "10", "tipo":
	 *                    "RUTA", "nombreTipo": "Managua", "idJornada": "198",
	 *                    "monto": "10.10", "tasaCambio": "29.8906", "noBoleta":
	 *                    "4477445665", "banco": "BAC", "idCuenta": "10",
	 *                    "noCuenta": "64654-654-36-2", "tipoCuenta": "Cuenta
	 *                    Corriente", "nombreCuenta": "Distribuidor Alfredo Alaniz",
	 *                    "estado": "ALTA", "creado_el": "23/05/2017 17:56:54",
	 *                    "creado_por": "cesar.casia", "modificado_el": "",
	 *                    "modificado_por": "" }, { "idRemesa": "325", "origen":
	 *                    "MOVIL", "idVendedor": "242", "nombreVendedor":
	 *                    "cesar.casia", "idTipo": "10", "tipo": "RUTA",
	 *                    "nombreTipo": "Managua", "idJornada": "198", "monto":
	 *                    "10.10", "tasaCambio": "29.8906", "noBoleta": "4587",
	 *                    "banco": "BAC", "idCuenta": "7", "noCuenta": "3215481-1",
	 *                    "tipoCuenta": "Cuenta Corriente", "nombreCuenta":
	 *                    "Monetaria de remesas", "estado": "ALTA", "creado_el":
	 *                    "23/05/2017 17:49:58", "creado_por": "cesar.casia",
	 *                    "modificado_el": "", "modificado_por": "" } ],
	 *                    "transaccionesTarjeta": [ { "banco": "Banco De Finanzas
	 *                    Nicaragua", "monto": "99.4935102885", "numAutorizacion":
	 *                    "52345234", "marcaTarjeta": "MASTERCARD",
	 *                    "digitosTarjeta": "2345", "idVendedor": "387",
	 *                    "nombreVendedor": "Hugo Siles", "idTipo": "47", "tipo":
	 *                    "PANEL", "nombreTipo": "El Porvenir", "estado": "ALTA",
	 *                    "creado_el": "23/05/2017 17:56:54", "creado_por":
	 *                    "cesar.casia", "modificado_el": "", "modificado_por": "" }
	 *                    ], "transaccionesCheque": [ { "banco": "Banco Centro",
	 *                    "monto": "149.270138907", "numeroCheque": "123423",
	 *                    "fechaEmision": "03/05/2017 00:00:00", "numeroReserva":
	 *                    "12343", "cuentaCliente": "123412344", "idVendedor":
	 *                    "387", "nombreVendedor": "Hugo Siles", "idTipo": "47",
	 *                    "tipo": "PANEL", "nombreTipo": "El Porvenir", "estado":
	 *                    "ALTA", "creado_el": "23/05/2017 17:56:54", "creado_por":
	 *                    "cesar.casia", "modificado_el": "", "modificado_por": "" }
	 *                    ] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-42", "mostrar": "1", "descripcion": "No se encontraron
	 *                  datos.", "clase": "OperacionTransaccionesDeuda", "metodo":
	 *                  "doGet", "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputTransDeuda getTransaccionesDeuda(@Context HttpServletRequest req, InputTransDeuda input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputTransDeuda response = new ResumenDeuda().getTransaccionesDeuda(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getventafe")
	/**
	 * @api {POST} /consultasidra/getventafe/ [getventafe]
	 * 
	 * @apiName getventafe
	 * @apiDescription Servicio para obtener las ventas sincronizadas en sidra que
	 *                 ya pasaron por facturac\u00F3n electronica de CR.
	 * @apiGroup VENTAS_FACTURA_ELECTRONICA
	 * @apiVersion 1.0.0
	 * @apiParam (getventafe) {String} codArea codArea C\u00F3digo de area del
	 *           pa\u00EDs.
	 * @apiParam (getventafe) {String} usuario usuario de usuario que solicita la
	 *           operaci\u00F3n.
	 * @apiParam (getventafe) {String} codDispositivo codDispositivo del usuario que
	 *           solicita la informacion.
	 * @apiParam (getventafe) {String} idVenta idVenta a consultar. Este campo es
	 *           opcional.
	 * @apiParam (getventafe) {String} idJornada idJornada a consultar. Este campo
	 *           es opcional.
	 * 
	 * @apiParamExample {json} Ejemplo parametros de Entrada: { "codArea":"",
	 *                  "usuario":"", "codDispositivo":"", "token":"", "idVenta":"",
	 *                  "idJornada":"" }
	 * 
	 * @apiSuccessExample {} JSON Respuesta-\u00E9xito: { "token":"WEB",
	 *                    "respuesta": { "codResultado":"80", "mostrar":"0",
	 *                    "descripcion":"Ok. se obtuvo informacion de factura
	 *                    electr\u00F3nica." }, "ventas":[ { "idVenta":"2657793",
	 *                    "folio":"212", "qr":"fkdjkdsgkdjsglsjglkdf"
	 *
	 *                    } ] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "token":"", "respuesta": {
	 *                  "codResultado": "-205", "mostrar": "1", "descripcion": "No
	 *                  existen registros con esos par\u00E9metros.", "clase":
	 *                  "OperacionBodegaDTS", "metodo": "doGet", "excepcion": " ",
	 *                  "tipoExepcion": " " } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputgetVentaFE getVentaFE(InputVentaFE input) {
		VentaFE recurso = new VentaFE();

		return recurso.getVentaFE(input);
	}

	@Path("/getSolicitudescip")
	/**
	 * @api {POST} /consultasidra/getSolicitudescip/ [getSolicitudescip]
	 * 
	 * @apiName getsolicitudescip
	 * @apiDescription .
	 * @apiGroup SOLICITUDES_CIP
	 * @apiVersion 1.0.0
	 * @apiParam (getsolicitudessip) {String} codArea codArea C\u00F3digo de area
	 *           del pa\u00EDs.
	 * @apiParam (getsolicitudessip) {String} usuario usuario de usuario que
	 *           solicita la operaci\u00F3n.
	 * @apiParam (getsolicitudessip) {String} codDispositivo codDispositivo del
	 *           usuario que solicita la informacion.
	 * @apiParam (getsolicitudessip) {String} token token de validaci\u00F3n de
	 *           autenticaci\u00F3n de usuario.
	 * @apiParam (getsolicitudessip) {String} numeroAportar N\u00FAmero de cliente a
	 *           proporcionar.
	 * @apiParam (getsolicitudessip) {String} tipoDocumento Descripci\u00F3n del
	 *           documento de identificaci\u00F3n del cliente.
	 * @apiParam (getsolicitudessip) {String} numeroDocumento N\u00FAmero del
	 *           documento.
	 * @apiParamExample {json} Ejemplo parametros de Entrada: { "codArea":"503",
	 *                  "usuario":"pruebas", "codDispositivo":"456483",
	 *                  "token":"WEB", "numeroaPortar":"12345678",
	 *                  "tipoDocumento":"PAS", "numeroDocumento":"16545" }
	 * 
	 * @apiSuccessExample {} JSON Respuesta-\u00E9xito: { "token":"WEB",
	 *                    "respuesta": { "codResultado":"203", "mostrar":"0",
	 *                    "descripcion":"OK Servicio Solicitudes Cip." "clase":
	 *                    "CtrlCip", "metodo": "getDatos", "excepcion": " ",
	 *                    "tipoExepcion": "" },
	 * 
	 *                    }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "token":"", "respuesta": {
	 *                  "codResultado": "-203", "mostrar": "1", "descripcion": "No
	 *                  se pudo obtener el c\u00F3digo CIP", "clase": "CtrlCip",
	 *                  "metodo": "getDatos", "excepcion": " ", "tipoExepcion":
	 *                  "Generales " } }
	 * 
	 * @apiPermission admin
	 */

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputSolicitudesCip getSolicitudesCip(@Context HttpServletRequest req, InputSolicitudesCip input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputSolicitudesCip response = new SolicitudesCip().getSolicitudesClip(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);
		return response;
	}

	@Path("/getinventariopromo")
	/**
	 * @api {POST} /consultasidra/getinventariopromo/ [getinventariopromo]
	 * 
	 * @apiName getinventariopromo
	 * @apiDescription Servicio para obtener una lista de los articulos
	 *                 promocionales por bodega.
	 * @apiGroup Promocionales
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de area del pa\u00EDs en el que se
	 *           desea obtener informaci\u00F3n.
	 * @apiParam {String} usuario Nombre del usuario que solicita la
	 *           informaci\u00F3n.
	 * @apiParam {String} idBodegaVritual Identificador de la Bodega Virtual que se
	 *           desea consultar sus articulos.
	 * @apiParam {String} [idArticulo] Identificador del art\u00EDculo a buscar.
	 *           Este campo es opcional.
	 * @apiParam {String} [descripcion] Descripcion del articulo. Este campo es
	 *           opcional.
	 * @apiParam {String} [estado] Estado del articulo. Este campo es opcional.
	 * @apiParamExample {json} Ejemplo parametros de Entrada: { "codArea": "505",
	 *                  "usuario": "usuario.prueba", "idBodegaVirtual": "159",
	 *                  "idArticulo": "", "descripcion": "", "estado": "" }
	 * 
	 * @apiSuccessExample {} JSON Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado":"1", "mostrar": "0", "descripcion":
	 *                    "Recursos recuperados exitosamente", },
	 *                    "idBodegaVirtual":"1", "nombreBodegaVirtual":"BODEGA ABC",
	 *                    "articulos":[ { "idArticulo":"1", "descripcion":"PACHON",
	 *                    "cantidad":"100", "estado":"Disponible" }, {
	 *                    "idArticulo":"2", "descripcion":"LAPICERO MOVISTAR",
	 *                    "cantidad":"500", "estado":"Disponible" } ] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "token":"" "respuesta": {
	 *                  "codResultado": "-244", "mostrar": "1", "descripcion": "No
	 *                  existe el Art\u00EDculo Promocional enviado en el detalle
	 *                  #.", "clase": "OperacionConsultaArticulosPromocionales",
	 *                  "metodo": "Articulos", "excepcion": " ", "tipoExepcion":
	 *                  "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputArtPromInventario getProductosInventariosPromo(@Context HttpServletRequest req,
			InputGetArtPromInventario input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputArtPromInventario response = new Promocionales().getPromocionalesPorBodega(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getvalidarportacion")
	/**
	 * @api {POST} /consultasidra/getValidarPortacion/ [getValidarPortacion]
	 * 
	 * @apiName getValidarPortacion
	 * @apiDescription Servicio para consulta de CIP y operador donante
	 * @apiGroup Portabilidad
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pais.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} nip N\u00FAmero de Identificaci\u00F3n Personal (movil).
	 * @apiParam {String} numTelefono N\u00FAmero de tel\u00E9fono.
	 * @apiParam {String} validar numero de servicio a validar 1 valida CIP, 2
	 *           valida operador donante y 3 valida ambos.
	 * 
	 * @apiParamExample {json} Ejemplo parametros de Entrada: { "codArea": "505",
	 *                  "usuario": "admin-sidra", "nip": "1982", "numTelefono":
	 *                  "56322055", "validar": "3" }
	 * 
	 * @apiSuccessExample {} JSON Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "205", "mostrar": "1", "descripcion": "OK.
	 *                    Se obtuvieron datos de operador donante", "clase":
	 *                    "OperacionPortabilidad", "metodo": "doGet", "excepcion": "
	 *                    ", "tipoExepcion": "" },
	 * 
	 *                    "donante": "movistar" }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-643", "mostrar": "1", "descripcion": "No se encontraron
	 *                  datos.", "clase": "OperacionPortabilidad", "metodo":
	 *                  "doGet", "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputPortabilidad getValidarPortacion(@Context HttpServletRequest req, InputPortabilidad input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputPortabilidad response = new ValidaPortabilidad().validaPortabilidad(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getestadoporta")
	/**
	 * @api {POST} /consultasidra/getestadoporta/ [getestadoporta]
	 * 
	 * @apiName getestadoporta
	 * @apiDescription Servicio para consulta estado portabilidad
	 * @apiGroup Portabilidad
	 * @apiVersion 1.0.0
	 * @apiParam {String} token token de validaci\u00F3n de usuario.
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} idJornada dentificador de la jornada activa.
	 * @apiParam {String} idVendedor Identificador del vendedor de la jornada y la
	 *           venta.
	 * @apiParam {String} IdVenta Identificador de la venta a anular.
	 * @apiParam {String} numeroaPortar N\u00FAmero de cliente a proporcionar.
	 * @apiParam {String} numeroTemporal indica el numero temporal a utilizar.
	 * 
	 * @apiParamExample {json} Ejemplo parametros de Entrada: { "token": "WEB",
	 *                  "codArea": "503", "usuario": "usuario.pruebas",
	 *                  "codDispositivo":"", "idJornada": "", "idVendedor": "",
	 *                  "IdVenta": "", " numeroaPortar":"", "numeroTemporal":"" }
	 * 
	 * @apiSuccessExample {} JSON Respuesta-\u00E9xito: { "token": "WEB",
	 *                    "respuesta": { "codResultado": "1", "mostrar": "0",
	 *                    "descripcion": "OK Solicitudes obtenidas.", "clase":
	 *                    "CtrlPortabilidad", "metodo": "getDatos", "excepcion": " "
	 *                    } "ventas":[ { "idVenta":"", "numeroaPortar":"",
	 *                    "numeroTemporal":"", "operadorDonante":"", "estado":"" },
	 *                    { "idVenta":"", "numeroaPortar":"", "numeroTemporal":"",
	 *                    "operadorDonante":"", "estado":"" } ] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-643", "mostrar": "1", "descripcion": "No se encontraron
	 *                  datos.", "clase": "getestadoporta", "metodo": "doGet",
	 *                  "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputEstadoPortabilidad getEstadoPorta(@Context HttpServletRequest req, InputEstadoPortabilidad input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputEstadoPortabilidad response = new EstadoPorta().getEstadoPorta(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				input.getToken(), input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getreporterecargas")
	/**
	 * @api {POST} /consultasidra/getreporterecargas/ [getreporterecargas]
	 * 
	 * @apiName getReporteRecargas
	 * @apiDescription Servicio para obtener un listado recargas vendidas,
	 * @apiGroup Reportes
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [idDts] Identificador del distribuidor que se desea
	 *           obtener. Este campo es opcional.
	 * @apiParam {String} [idBodega] Identificador de la bodega virtual de la que se
	 *           vendieron las recargas a consultar. Este campo es opcional.
	 * @apiParam {String} [idTipo] Identificador de la ruta o panel que se desea
	 *           obtener. Este campo es opcional.
	 * @apiParam {String} [tipo="RUTA o PANEL"] Nombre del tipo a consultar.
	 *           Obligatorio \u00FAnicamente si se ingresa idTipo.
	 * @apiParam {String} fechaInicio Fecha inicio en formato yyyyMMdd desde donde
	 *           se desea consultar las recargas.
	 * @apiParam {String} fechaFin Fecha fin en formato yyyyMMdd hasta donde se
	 *           desea consultar las recargas.
	 * 
	 * @apiParamExample {json} Ejemplo parametros de Entrada: { "codArea": "505",
	 *                  "usuario": "usuario.pruebas", "idDts":"", "idBodega":"",
	 *                  "idTipo":"", "tipo":"", "fechaInicio":"20170307",
	 *                  "fechaFin": "20170405" }
	 * 
	 * @apiSuccessExample {} JSON Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" },
	 * 
	 *                    "recargas": [ { "jornada": "49",
	 *                    "estadoLiquidacionJornada": "LIQUIDADA", "vendedor":
	 *                    "diego.urbina", "totalFacturado": "3", "fecha":
	 *                    "07/03/2017 00:00:00", "nombreDts": "Movilway", "zona":
	 *                    "Promix Aux3" }, { "jornada": "57",
	 *                    "estadoLiquidacionJornada": "LIQUIDADA", "vendedor":
	 *                    "carlos.lopez", "totalFacturado": "5", "fecha":
	 *                    "07/03/2017 00:00:00", "nombreDts": "ANA BAEZ", "zona":
	 *                    "Ana baez, Llanos del sol" }, { "jornada": "58",
	 *                    "estadoLiquidacionJornada": "", "vendedor":
	 *                    "carlos.lopez", "totalFacturado": "6", "fecha":
	 *                    "07/03/2017 00:00:00", "nombreDts": "ANA BAEZ", "zona":
	 *                    "Ana baez, Llanos del sol" }, { "jornada": "92",
	 *                    "estadoLiquidacionJornada": "LIQUIDADA", "vendedor":
	 *                    "hugo.siles", "totalFacturado": "1", "fecha": "20/03/2017
	 *                    00:00:00", "nombreDts": "DISTRIBUIDOR LA ESPERANZA",
	 *                    "zona": "Bodega Esperanza 1" }, { "jornada": "125",
	 *                    "estadoLiquidacionJornada": "LIQUIDADA", "vendedor":
	 *                    "hugo.siles", "totalFacturado": "9", "fecha": "04/04/2017
	 *                    00:00:00", "nombreDts": "DISTRIBUIDOR LA ESPERANZA",
	 *                    "zona": "Bodega Esperanza 1" } ] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-61", "mostrar": "1", "descripcion": "Debe ingresar ambas
	 *                  fechas: Fecha inicio y fecha Fin", "clase":
	 *                  "CtrlReporteRecarga", "metodo": "validarInput", "excepcion":
	 *                  " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputReporteRecarga getReporteRecargas(@Context HttpServletRequest req, InputReporteRecarga input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputReporteRecarga response = new ReporteRecargas().getReporteRecarga(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getofertafs")
	/**
	 * @api {POST} /consultasidra/getofertafs [getofertafs]
	 * 
	 * @apiName getofertafs
	 * @apiDescription Servicio para obtener ofertas desde fullstack,
	 * @apiGroup OfertaCampania
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [tipoArticulo="KIT, RECARGA, SIMCARD, TARJETARASCA,
	 *           PROMOCIONAL"] Tipo de articulo del que se desea obtner oferta.
	 * @apiParam {String} [tipoGestion="ALTA PREPAGO, PORTABILIDAD y MANTENIMIENTO
	 *           NUMERO"] Tipo de gestion del que se desea tener ofertas .
	 * @apiParam {String} [nombre] Nombre de la oferta que desea obtener.
	 * 
	 * @apiParamExample {json} Ejemplo parametros de Entrada: { "codArea": "505",
	 *                  "usuario": "usuario.pruebas", "tipoArticulo": "SIMCARD",
	 *                  "tipoGestion": "", "nombre":"" }
	 * 
	 * @apiSuccessExample {} JSON Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" }, "oferta": [ {
	 *                    "idProductOffering": "9145627885465644670", "nombre":
	 *                    "Chip Movistar prepago (blister y sobre) PREACTIVA",
	 *                    "precio": "1.69", "precioMin": "", "precioMax": "" }, {
	 *                    "idProductOffering": "9148633510165834045", "nombre":
	 *                    "Chip Movistar prepago (Mantenimiento) PREACTIVA",
	 *                    "precio": "1.27", "precioMin": "", "precioMax": "" }, {
	 *                    "idProductOffering": "9148633510165834033", "nombre":
	 *                    "Chip Movistar prepago (PDV) PREACTIVA", "precio": "0.21",
	 *                    "precioMin": "", "precioMax": "" }, { "idProductOffering":
	 *                    "9148633510165834039", "nombre": "Chip Movistar prepago
	 *                    (PDV) PREACTIVA - 30 SIMCARD", "precio": "0", "precioMin":
	 *                    "", "precioMax": "" }, { "idProductOffering":
	 *                    "9145627885465644677", "nombre": "Chip Movistar prepago
	 *                    portabilidad PREACTIVA", "precio": "1.69", "precioMin":
	 *                    "", "precioMax": "" }, { "idProductOffering":
	 *                    "9148633510165834027", "nombre": "SIDRA: Chip Movistar
	 *                    prepago (blister y sobre) PREACTIVA", "precio": "1.69",
	 *                    "precioMin": "", "precioMax": "" }, { "idProductOffering":
	 *                    "9148633510165834021", "nombre": "SIDRA: Chip Movistar
	 *                    prepago portabilidad PREACTIVA", "precio": "0.01",
	 *                    "precioMin": "", "precioMax": "" }, { "idProductOffering":
	 *                    "9145627885465644684", "nombre": "Chip Movistar prepago
	 *                    (blister y sobre) LIBRE", "precio": "1.69", "precioMin":
	 *                    "", "precioMax": "" } ],
	 *
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-643", "mostrar": "1", "descripcion": "No se encontraron
	 *                  datos.", "clase": "OperacionOfertaFS", "metodo": "doGet",
	 *                  "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputOfertaFS getOfertaFS(@Context HttpServletRequest req, InputOfertaFS input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputOfertaFS response = new OfertaFS().getOfertasFS(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getodescuentofs")
	/**
	 * @api {POST} /consultasidra/getodescuentofs/ [getodescuentofs]
	 * 
	 * @apiName getodescuentofs
	 * @apiDescription Servicio para obtener descuentos desde fullstack,
	 * @apiGroup OfertaCampania
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} [idProductOffering] Identificador de la oferta de la que
	 *           se desea obtener sus descuentos. Este campo es opcional.
	 * @apiParam {String} [idDescuento] Identificador del descuento del que se desea
	 *           obtener informaci\u00F3n. Este campo es opcional.
	 * @apiParam {String} [nombreDescuento] Nombre del descuento del que se desea
	 *           obtener informaci\u00F3n. Este campo es opcional.
	 * 
	 * @apiParamExample {json} Ejemplo parametros de Entrada: { "codArea": "505",
	 *                  "usuario": "usuario.pruebas", "idProductOffering":
	 *                  "9145627885465644616", "idDescuento": "",
	 *                  "nombreDescuento":"" }
	 * 
	 * @apiSuccessExample {} JSON Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "0", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": " ", "metodo": "
	 *                    ", "excepcion": " ", "tipoExepcion": "" },
	 * 
	 *                    "listaDescuentos": { "idProductOffering":
	 *                    "9145627885465644616", "nombre": "Kit: Mobile Phone NEXUS
	 *                    GO MOBILE NX-175 AZUL NEGRO", "descuentos": [ {
	 *                    "idDescuento": "9145982480265567119", "nombreDescuento":
	 *                    "Precio Kit prepago Cadenas (retail) 15% descuento",
	 *                    "montoDescuento": "15", "creadoEl": "13/09/2017 09:44:17",
	 *                    "creadoPor": "PROC_SINC_OFERTA", "modificadoEl": "",
	 *                    "modificadoPor": "", "tipoDescuento": "ES_%" }, {
	 *                    "idDescuento": "9145982480265567134", "nombreDescuento":
	 *                    "Precio Kit prepago Cadenas (retail) 20% descuento",
	 *                    "montoDescuento": "20", "creadoEl": "13/09/2017 09:44:18",
	 *                    "creadoPor": "PROC_SINC_OFERTA", "modificadoEl": "",
	 *                    "modificadoPor": "", "tipoDescuento": "ES_%" }, {
	 *                    "idDescuento": "9145982480265567125", "nombreDescuento":
	 *                    "Precio Kit prepago Cadenas (retail) 17% descuento",
	 *                    "montoDescuento": "17", "creadoEl": "13/09/2017 09:44:18",
	 *                    "creadoPor": "PROC_SINC_OFERTA", "modificadoEl": "",
	 *                    "modificadoPor": "", "tipoDescuento": "ES_%" }, {
	 *                    "idDescuento": "9145982480265567122", "nombreDescuento":
	 *                    "Precio Kit prepago Cadenas (retail) 16% descuento",
	 *                    "montoDescuento": "16", "creadoEl": "13/09/2017 09:44:17",
	 *                    "creadoPor": "PROC_SINC_OFERTA", "modificadoEl": "",
	 *                    "modificadoPor": "", "tipoDescuento": "ES_%" }, {
	 *                    "idDescuento": "9145982480265567131", "nombreDescuento":
	 *                    "Precio Kit prepago Cadenas (retail) 19% descuento",
	 *                    "montoDescuento": "19", "creadoEl": "13/09/2017 09:44:18",
	 *                    "creadoPor": "PROC_SINC_OFERTA", "modificadoEl": "",
	 *                    "modificadoPor": "", "tipoDescuento": "ES_%" }, {
	 *                    "idDescuento": "9145982480265567128", "nombreDescuento":
	 *                    "Precio Kit prepago Cadenas (retail) 18% descuento",
	 *                    "montoDescuento": "18", "creadoEl": "13/09/2017 09:44:18",
	 *                    "creadoPor": "PROC_SINC_OFERTA", "modificadoEl": "",
	 *                    "modificadoPor": "", "tipoDescuento": "ES_%" } ] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "respuesta": { "codResultado":
	 *                  "-643", "mostrar": "1", "descripcion": "No se encontraron
	 *                  datos.", "clase": "OperacionDescuentoFS", "metodo": "doGet",
	 *                  "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputDescuentoFS getDescuentoFS(@Context HttpServletRequest req, InputDescuentoFS input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputDescuentoFS response = new DescuentoFS().getDescuentoFS(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/obtenerarticuloprecio")
	/**
	 * @api {POST} /consultasidra/obtenerarticuloprecio/ [obtenerarticuloprecio]
	 * 
	 * @apiName obtenerarticuloprecio
	 * @apiDescription Servicio para obtener configuraci\u00F3n de art\u00EDculo
	 *                 precio,
	 * @apiGroup ArticuloPrecio}
	 * @apiVersion 1.0.0
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} idArticulo Identificador del articulo a asociar.
	 * @apiParam {String} tipoGestion nombre de la gesti\u00F3n a la que estar\u00E9
	 *           asociado el articulo y su precio. ALTA_PREPAGO y PORTABILIDAD.
	 * @apiParam {String} precio cantidad en cuanto al precio que se le dar\u00E9 al
	 *           art\u00EDculo.
	 * @apiParam {String} idProductOffering Identificador de la oferta que se
	 *           asociar\u00E9 al precio de un art\u00EDculo.
	 * 
	 * @apiParamExample {json} Ejemplo parametros de Entrada: { "usuario":
	 *                  "usuario.pruebas", "codArea": "503", "idArticulo":"",
	 *                  "tipoGestion":"", "precio":"",
	 *                  "idProductOffering":"9145627885465644655" }
	 * 
	 * @apiSuccessExample {} JSON Respuesta-\u00E9xito: { "respuesta": {
	 *                    "codResultado": "12", "mostrar": "1", "descripcion": "OK.
	 *                    Datos obtenidos exitosamente", "clase": "", "metodo": "",
	 *                    "excepcion": " ", "tipoExepcion": "" }, "articulosPrecio":
	 *                    { "idArticulo": "9293", "tipoGestion": "PORTABILIDAD",
	 *                    "precio": "4.45", "idProductOffering":
	 *                    "9145627885465644655", "estado": "ALTA", "nombreArticulo":
	 *                    "TARJETA PREPAGO MOVISTAR $5.00", "version": "",
	 *                    "creadoEl": "18/09/2017 16:15:30", "creadoPor":
	 *                    "admin-sidra", "modificadoEl": "19/09/2017 15:01:26",
	 *                    "modificadoPor": "usuario.pruebas" }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "token": "WEB", "respuesta": {
	 *                  "codResultado": "-2", "mostrar": "1", "descripcion": "El
	 *                  par\u00E9metro de entrada \\\"usuario\"\\ esta vac\u00EDo",
	 *                  "clase": "CtrlArticuloPrecio", "metodo": "validarInput",
	 *                  "excepcion": " ", "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputArticuloPrecio getArticulosPrecio(@Context HttpServletRequest req, InputArticuloPrecio input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputArticuloPrecio response = new ArticuloPrecio().getArticuloPrecios(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/getticket")
	/**
	 * @api {POST} /consultasidra/getticket/ [getticket]
	 * 
	 * @apiName getticket
	 * @apiDescription Servicio para obtener ticket de una venta especifica,
	 * @apiGroup VisorTCK
	 * @apiVersion 1.0.0
	 * @apiParam {String} token token de validaci\u00F3n de usuario.
	 * @apiParam {String} codArea C\u00F3digo de \u00E9rea del pa\u00EDs en el que
	 *           se desea realizar la operaci\u00F3n.
	 * @apiParam {String} usuario Nombre de usuario que solicita la operaci\u00F3n.
	 * @apiParam {String} codDispositivo c\u00F3digo del dispositivo desde donde se
	 *           realiza la operaci\u00F3n, en caso de utilizarse la APP M\u00F3VIL.
	 * @apiParam {String} IdVenta Identificador de la venta a obtener ticket.
	 * @apiParamExample {json} Ejemplo parametros de Entrada: { "codArea":"503",
	 *                  "usuario":"pruebas", "codDispositivo":"456483",
	 *                  "token":"WEB", "idVenta":"1179" }
	 * 
	 * @apiSuccessExample {} JSON Respuesta-\u00E9xito: { "token": "WEB", "idVenta":
	 *                    "1179", "lineas": [ { "izquierda": "", "centro": "",
	 *                    "derecha": "", "alineacion": "E", "estilo": "", "extra":
	 *                    "" }, { "izquierda": "TELEFONICA MOVILES EL SALVADOR, S.A.
	 *                    DE C.V.", "centro": "", "derecha": "", "alineacion": "C",
	 *                    "estilo": "", "extra": "" } ] }
	 * 
	 * @apiErrorExample {} JSON Respuesta-Error: { "token": "WEB", "respuesta": {
	 *                  "codResultado": "-921", "mostrar": "1", "descripcion": "El
	 *                  par\u00E9metro de entrada \\\"idVenta\\\" esta vacio.",
	 *                  "clase": " ", "metodo": "getTicket", "excepcion": "class
	 *                  com.consystec.sc.sv.ws.metodos.CtrlVisorTicket",
	 *                  "tipoExepcion": "Generales" } }
	 * 
	 * @apiPermission admin
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public OutputTicket getTicket(@Context HttpServletRequest req, InputTicket input) {
		Date beginTime = new Date();
		ControladorBase.setRequestGlobal(req);
		OutputTicket response = new VisorTicket().getVisorTCK(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);

		return response;
	}

	@Path("/isnumeropayment")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON_ENCODING, MediaType.APPLICATION_XML_ENCODING })
	public AbstractResponse isNumeroPayment(@Context HttpServletRequest req, RequestNumeroPayment input) {
		Date beginTime = new Date();
		AbstractResponse response = new PuntoVenta().validaNumeroRecargaPayment(input);
		ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(),
				null, input.getUsuario(), req, input, response, beginTime);
		return response;
	}
}

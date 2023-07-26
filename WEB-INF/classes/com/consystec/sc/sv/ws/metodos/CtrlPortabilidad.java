package com.consystec.sc.sv.ws.metodos;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.portabilidad.InputCargaAdjuntoPorta;
import com.consystec.sc.ca.ws.input.portabilidad.InputCreaPortabilidad;
import com.consystec.sc.ca.ws.input.portabilidad.InputProductoCustomer;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.portabilidad.OutputInformacionClienteFS;
import com.consystec.sc.ca.ws.output.fichacliente.OutputDatosClienteFS;
import com.consystec.sc.ca.ws.output.portabilidad.OutputAdjuntoPorta;
import com.consystec.sc.ca.ws.output.portabilidad.OutputCreaPortabilidad;
import com.consystec.sc.ca.ws.so.request.AtributosModify;
import com.consystec.sc.ca.ws.so.request.ConsultaCrearOrdenVenta;
import com.consystec.sc.ca.ws.so.request.ModifySalesOrder;
import com.consystec.sc.ca.ws.so.response.RespuestaCrearOrdenVenta;
import com.consystec.sc.sv.ws.operaciones.OperacionCreaPortabilidad;
import com.consystec.sc.sv.ws.operaciones.SOPortabilidad;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class CtrlPortabilidad extends ControladorBase {
	private static final Logger log = Logger.getLogger(CtrlPortabilidad.class);
	private static String servicio = Conf.LOG_SERVICIO_CRE_PORTA;

	public Respuesta validarDatos(Connection conn, InputCreaPortabilidad input, BigDecimal ID_PAIS) {
		Respuesta objRespuesta = new Respuesta();
		String nombreMetodo = "validarDatos";
		String nombreClase = new CurrentClassGetter().getClassName();
		String datosErroneos = "";
		String estadoAlta = "";
		boolean flag = false;

		if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo,
					null, input.getCodArea()).getDescripcion();
			flag = true;
		}

		if (input.getCodArea() == null || "".equals(input.getCodArea())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_303, null, nombreClase, nombreMetodo,
					null, input.getCodArea()).getDescripcion();
			flag = true;
		} else if (input.getCodArea().length() != 3) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_LONG_304, null, nombreClase,
					nombreMetodo, null, input.getCodArea()).getDescripcion();
			flag = true;
		} else if (!isNumeric(input.getCodArea())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_COD_AREA_NUM_305, null, nombreClase, nombreMetodo,
					null, input.getCodArea()).getDescripcion();
			flag = true;
		}

		if (input.getIdPortaMovil() == null || "".equals(input.getIdPortaMovil())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_IDPORTAMOVIL_NULO_915, null, nombreClase, nombreMetodo, null, input.getCodArea())
					.getDescripcion();
			flag = true;
		}
		
		if (input.getIdJornada()==null || "".equals(input.getIdJornada()))
		{
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_IDJORNADA_NULO_89, null, nombreClase, nombreMetodo, null, input.getCodArea())
					.getDescripcion();
			flag = true;
		}
		if (input.getIdVendedor()==null || "".equals(input.getIdVendedor()))
		{
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_137, null, nombreClase, nombreMetodo, null, input.getCodArea())
					.getDescripcion();
			flag = true;	
		}
		
		if (input.getNumPortar() == null || "".equals(input.getNumPortar().trim())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_NUMEROAPORTAR_VACIO_656, null, nombreClase,
					nombreMetodo, null, input.getCodArea()).getDescripcion();
			flag = true;
		} else if (!isNumeric(input.getNumPortar())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_NUMEROAPORTAR_NO_NUMERICO_657, null, nombreClase,
					nombreMetodo, null, input.getCodArea()).getDescripcion();
			flag = true;
		}

		if (input.getOperadorDonante() == null || "".equals(input.getOperadorDonante().trim())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_OPERADOR_DONANTE_651, null, nombreClase,
					nombreMetodo, null, input.getCodArea()).getDescripcion();
			flag = true;
		}		
		else
		{
			// validar que sea un operador donante valido
			try {
				estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE,
						input.getOperadorDonante()));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO,
						Conf.OPERADOR_DONANTE));
				condiciones.add(
						UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,
						ID_PAIS.toString()));
				int existeOperadorDonate = UtileriasBD.selectCount(conn, Catalogo.N_TABLA, condiciones);

				if (existeOperadorDonate <= 0) {
					datosErroneos += getMensaje(Conf_Mensajes.MSJ_TIPO_PRODUCTO_DONANTE_NO_VALIDO_664, null, nombreClase,
							nombreMetodo, null, input.getCodArea()).getDescripcion();
					flag = true;
				}
			} catch (Exception e) {
				log.error(e, e);
				datosErroneos += getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo,
						null, input.getCodArea());
			}
		}
		
		if (input.getCip() == null || "".equals(input.getCip().trim())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_CIP_654, null, nombreClase, nombreMetodo, null, input.getCodArea())
					.getDescripcion();
			flag = true;
		}

		if (input.getProductoDonante() == null || "".equals(input.getProductoDonante().trim())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_TIPO_PRODUCTO_DONANTE_663, null, nombreClase, nombreMetodo,
					null, input.getCodArea()).getDescripcion();
			flag = true;
		} else {
			try {
				estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE,
						input.getProductoDonante()));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO,
						Conf.GRUPO_TIPO_DOCUMENTO_DONANTE));
				condiciones.add(
						UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,
						ID_PAIS.toString()));

				int existeTipoProducto = UtileriasBD.selectCount(conn, Catalogo.N_TABLA, condiciones);

				if (existeTipoProducto <= 0) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPO_PRODUCTO_DONANTE_NO_VALIDO_664, null, nombreClase,
							nombreMetodo, null, input.getCodArea());
					flag = true;
				}
			} catch (Exception e) {
				log.error(e, e);
				objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo,
						null, input.getCodArea());

			}
		}

		if (input.getNumTemporal() == null || "".equals(input.getNumTemporal().trim())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_NUMERO_TEMPORAL_652, null, nombreClase,
					nombreMetodo, null, input.getCodArea()).getDescripcion();
			flag = true;
		} else if (!isNumeric(input.getNumTemporal())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_NUMERO_TEMPORL_NO_NUMERICO_659, null, nombreClase,
					nombreMetodo, null, input.getCodArea()).getDescripcion();
			flag = true;
		}

		if (input.getPrimerNombre() == null || "".equals(input.getPrimerNombre().trim())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_PRIMERNOMBRE_VACIO_680, null, nombreClase, nombreMetodo, null, input.getCodArea())
					.getDescripcion();
			flag = true;
		}
		if (input.getPrimerApellido() == null || "".equals(input.getPrimerApellido().trim())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_APELLIDO_375, null, nombreClase, nombreMetodo, null, input.getCodArea())
					.getDescripcion();
			flag = true;
		}
		if (input.getTipoDocumento() == null || "".equals(input.getTipoDocumento().trim())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_TIPODOCUMENTO_NULO_274, null, nombreClase, nombreMetodo, null, input.getCodArea())
					.getDescripcion();
			flag = true;
		} else {
			try {
				estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE,
						input.getTipoDocumento()));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO,
						Conf.GRUPO_TIPO_DOCUMENTO));
				condiciones.add(
						UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,
						ID_PAIS.toString()));
				int existeTipoDoc = UtileriasBD.selectCount(conn, Catalogo.N_TABLA, condiciones);

				if (existeTipoDoc <= 0) {
					datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_DOCUMENTO_266, null, nombreClase,
							nombreMetodo, null, input.getCodArea()).getDescripcion();
					flag = true;
				}
			} catch (Exception e) {
				log.error(e, e);
				datosErroneos += getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo,
						null, input.getCodArea());
			}

		}

		if (input.getNoDocumento() == null || "".equals(input.getNoDocumento().trim())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_NUMERO_DOCUMENTO_658, null, nombreClase, nombreMetodo,
					null, input.getCodArea()).getDescripcion();
			flag = true;
		}

		if (flag) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo,
					datosErroneos, input.getCodArea());
		} else {
			objRespuesta.setDescripcion("OK");
			objRespuesta.setCodResultado("1");
			objRespuesta.setMostrar("0");
		}
		return objRespuesta;
	}

	public OutputCreaPortabilidad creaPortabilidad(InputCreaPortabilidad input) {
		OutputCreaPortabilidad objRespuestaPortabilidad = new OutputCreaPortabilidad();
		OutputInformacionClienteFS fullInfoCustomer = null;
		InputProductoCustomer estatusProducto = null;
		List<LogSidra> listaLog = new ArrayList<LogSidra>();
		String nombreMetodo = "getDatos";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta respuesta = new Respuesta();

		Connection conn = null;
		String Location = "";
		String canalSidra = "";
		ConsultaCrearOrdenVenta objCreaSO = new ConsultaCrearOrdenVenta();
		RespuestaCrearOrdenVenta respSO = new RespuestaCrearOrdenVenta();
		RespuestaCrearOrdenVenta respModify = new RespuestaCrearOrdenVenta();
		RespuestaCrearOrdenVenta respSubmit = new RespuestaCrearOrdenVenta();
		RespuestaCrearOrdenVenta creaNumero = new RespuestaCrearOrdenVenta();
		String IDCustomer = "";
		String estadoAlta = "";
		String idOrderItem = "";
		BigDecimal idPortabilidad = null;
		String valorRetorno="";
		String BusinessInteraction="";
		ModifySalesOrder datosModify = new ModifySalesOrder();
		String nombreArchivo="";
		
		
		try {
			conn = getConnRegional();
			BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());
			conn.setAutoCommit(false);
			// validar los datos
			respuesta = validarDatos(conn, input, ID_PAIS);
			log.trace("Respuesta validaci\u00F3n: " + respuesta.getDescripcion());
			if (!"OK".equals(respuesta.getDescripcion())) {
				log.trace("Error al validar los datos:" + respuesta.getDescripcion());
				objRespuestaPortabilidad.setRespuesta(respuesta);
				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicio, "0",
						Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));
				return objRespuestaPortabilidad;
			}


			
			log.trace("inicia a obtener valores");
			valorRetorno=  UtileriasJava.getConfig(conn, Conf.GRUPO_PRODUCT_CUSTOMER, Conf.VALOR_PRODUCT, input.getCodArea());
			log.trace("valor retorno:" + valorRetorno);
			
			estatusProducto = OperacionCreaPortabilidad.getProductoCustomer(conn, input);			
			log.trace("valor ws:" + estatusProducto.getStatusProduct());
			if (valorRetorno.equalsIgnoreCase(estatusProducto.getStatusProduct())) {
				
				// consumimos ws FS para validar el usuario con el n\u00FAmero de telefono
				fullInfoCustomer = OperacionCreaPortabilidad.getDatoscliente(conn, input);

				log.trace("get datos clientes retorno:" + fullInfoCustomer.getIDCustomer().toString());
				// obtener location
				OutputDatosClienteFS idGeographicLocation = new OutputDatosClienteFS();
				idGeographicLocation = OperacionCreaPortabilidad.getGeographicLocation(conn, input,
						fullInfoCustomer.getIDCustomer().toString()); 
				Location = idGeographicLocation.getIdGeographicLocation();
				// fin obtener location

				// validamos si es un cliente es cliente reservado
				if (fullInfoCustomer.getFirstLastNameIndividualName().contains(input.getNumTemporal())) {
					// actualizamos los datos
					IDCustomer = OperacionCreaPortabilidad.updateDatoscliente(conn, input, fullInfoCustomer);
					if (IDCustomer == null || "".equals(IDCustomer)) {
						log.trace("no se se logro actualizar los datos en el servicio ws fs");

						objRespuestaPortabilidad.setRespuesta(respuesta);
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicio, "0",
								Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
								respuesta.getDescripcion()));
						return objRespuestaPortabilidad;
					}
				}
				else
				{
					
					IDCustomer=fullInfoCustomer.getIDCustomer().toString();
				}

				// Actualizamos las tablas insertar la informacion de la
				// solicitud porta
				idPortabilidad = OperacionCreaPortabilidad.insertarPortabilidad(conn, input, ID_PAIS);
				log.trace("ID portabilidad: " + idPortabilidad);
				if ("0".equals(idPortabilidad.toString())) {
					respuesta = getMensaje(Conf_Mensajes.OK_DATOS_PORTABIIDAD_86, null, null, null, null, input.getCodArea());
				} else {
					conn.rollback();
					respuesta = getMensaje(Conf_Mensajes.MSJ_INCONVENIENTES_REGISTRO_PORTA_676, null, null, null, null, input.getCodArea());
					objRespuestaPortabilidad.setRespuesta(respuesta);
					return objRespuestaPortabilidad;
				}
				
				log.trace("AQUI CREA EL NUMERO DE TELEFONO EN EL INVENTARIO");

				//crea numero a portar en inventario
				creaNumero=SOPortabilidad.creaNumeroFS(conn, input.getNumPortar(), IDCustomer, input.getUsuario(),  input.getCodArea());
				
				if (creaNumero.getMensaje().getCodresultado().intValue() == 1){
					
				//obtenemos el ById Customer
				String byIdCustomer="";
				byIdCustomer=OperacionCreaPortabilidad.getCustomerById(conn,  estatusProducto.getParentIDProduct(), input.getUsuario(), input.getCodArea(), input.getNumTemporal());
				
				if ("0".equals(byIdCustomer))
				{
					respuesta = getMensaje(Conf_Mensajes.MSJ_PROBLEMAS_BYID_CUSTOMER_693, null, null, null, null, input.getCodArea());
					objRespuestaPortabilidad.setRespuesta(respuesta);
					conn.rollback();
					return objRespuestaPortabilidad;
				}
				
					
				// obtener canal de sidra
				canalSidra = UtileriasJava.getConfig(conn, Conf.CANAL_SIDRA_SO, Conf.CANAL_SIDRA_SO, input.getCodArea());
				estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
				String ofertaPorta= UtileriasJava.getConfig(conn, Conf.OFERTA_PORTABILIDAD, Conf.OFERTA_PORTABILIDAD, input.getCodArea());
				// crear sales order (Return Sales Order)
				objCreaSO.setIdCanalDistribucion(canalSidra);
				objCreaSO.setIdCliente(IDCustomer);
				objCreaSO.setIdUbicacion(Location);
				objCreaSO.setPais(input.getCodArea());
				objCreaSO.setUsuario(input.getUsuario());
				objCreaSO.setIdOffering(ofertaPorta);

				
						datosModify = getDatosAtributosPorta(conn, input, estadoAlta, objCreaSO, respSO.getIdOrdenVenta(),
								idOrderItem, ID_PAIS);
						respModify = SOPortabilidad.modificarItemOrdenVentaFS(conn, datosModify, byIdCustomer, input.getCodArea());
						
				if (respModify.getMensaje().getCodresultado().intValue()==1) {
					// todo ok
					// importamos archivo a FTP llamar el servicio ws
					// si hay archivos m\u00E9s para porta hacer ftp y enviarlos
					// obtener los archivos de las solicitud
					// idportamovil//obtener los archivos de las solicitud
					// idportamovil
					
					log.trace("idSalesOrder:"+ respModify.getIdOrdenVenta());
	
					//Enviamos el archivo adjunto al  ftp
					try
					{
						for (int i = 0; i < input.getAdjuntoporta().size(); i++) 
						{
							byte[] base64Decoded =null;
							base64Decoded = DatatypeConverter.parseBase64Binary(input.getAdjuntoporta().get(i).getAdjunto().toString());
							log.trace("Tamanio en bytes de archivo:" + base64Decoded.length);
							if (base64Decoded.length < 2097152) {
								nombreArchivo=input.getAdjuntoporta().get(i).getNombreArchivo().replace(" ", "_");
								InputStream in = new ByteArrayInputStream(base64Decoded);	
								subirFTP(in,nombreArchivo, conn, input.getCodArea());
							}
							else
							{
								respuesta = getMensaje(Conf_Mensajes.MSJ_ARCHIVO_SUPERA_2MB_691,
										respModify.getMensaje().getDescripcion(), nombreClase, nombreMetodo, null, input.getCodArea());
										objRespuestaPortabilidad.setRespuesta(respuesta);
								return objRespuestaPortabilidad;
							}
							//insertamos en la tabla de adjunto y consumimos el ws AddCustomerAttachment_v1
							//llamamos el servicio ws para obtener el itachmit jcsimon
							BusinessInteraction="Yes";
							
							String IDAttachment = OperacionCreaPortabilidad.getAddCustomerAttachment(conn, respModify.getIdOrdenVenta(),
									nombreArchivo, input.getUsuario(), input.getCodArea(), BusinessInteraction);
							
							if (!"0".equals(IDAttachment)) {
								log.trace("inserta adjunto Porta");
								BigDecimal idAdjunto = OperacionCreaPortabilidad.saveAdjunto(conn, 
										input.getAdjuntoporta().get(i).getNombreArchivo(),  idPortabilidad,
										input.getIdPortaMovil(), input.getUsuario(), IDAttachment);
								if (idAdjunto.compareTo(new BigDecimal(0))>0) {
									respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_ADJUNTO_64, null,
											nombreClase, nombreMetodo, null, input.getCodArea());

									listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, "adjuntoPorta",
											idAdjunto + "", Conf.LOG_TIPO_ADJUNTO, "Se registr\u00F3 el adjunto con ID " + idAdjunto
													+ " a la gesti\u00F3n con ID " + idPortabilidad + ".",
											""));
								}
							} else {
								respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT,
										"Error al consumir servicio ddCustomerAttachment ", nombreClase, nombreMetodo, "", input.getCodArea());
								objRespuestaPortabilidad.setRespuesta(respuesta);
								return objRespuestaPortabilidad;

							}
							
						}
						
					}catch(Exception e)
					{
						conn.rollback();
						log.error(e,e);
						respuesta = getMensaje(Conf_Mensajes.MSJ_PROBLEMAS_ENVIAR_ARCHIVO_FTP_690,
								respModify.getMensaje().getDescripcion(), nombreClase, nombreMetodo, null, input.getCodArea());
								objRespuestaPortabilidad.setRespuesta(respuesta);
						return objRespuestaPortabilidad;
					}
					//Buscamos si hay documentos Adicionales
					List<InputCargaAdjuntoPorta> DocumentosAdicionales = new ArrayList<InputCargaAdjuntoPorta>();

						DocumentosAdicionales = OperacionCreaPortabilidad.getDocumentosAdicionales(conn,
								input.getIdPortaMovil(), ID_PAIS);
						if(!DocumentosAdicionales.isEmpty() && DocumentosAdicionales.size()>0)
							{
								for (int i = 0; i < DocumentosAdicionales.size(); i++) {
									BusinessInteraction="No";
									byte[] base64Decoded =null;
									nombreArchivo=DocumentosAdicionales.get(i).getNombreArchivo().replace(" ", "_");
									base64Decoded = DatatypeConverter.parseBase64Binary(DocumentosAdicionales.get(i).getArchivo().toString());
									log.trace("Tamanio en bytes de archivo:" + base64Decoded.length);
									if (base64Decoded.length < 2097152) {
										//HACER FTP
										InputStream in = new ByteArrayInputStream(base64Decoded);	
										subirFTP(in, nombreArchivo, conn, input.getCodArea());
									}
									else
									{
										respuesta = getMensaje(Conf_Mensajes.MSJ_ARCHIVO_SUPERA_2MB_691,
												respModify.getMensaje().getDescripcion(), nombreClase, nombreMetodo, null, input.getCodArea());
												objRespuestaPortabilidad.setRespuesta(respuesta);
										return objRespuestaPortabilidad;
									}
									
									 //CONSUMIR SERVICIO OSB
									 //UPDATE ADJUNTO_PORTA CON IDATACCHMENT DE DOCUMENTOS ADICIONALES
									
									String IDAttachment = OperacionCreaPortabilidad.getAddCustomerAttachment(conn, respModify.getIdOrdenVenta(),
											nombreArchivo, input.getUsuario(), input.getCodArea(), BusinessInteraction);
	
									if (!"0".equals(IDAttachment)) {
										BigDecimal idAdjunto = OperacionCreaPortabilidad.saveAdjunto(conn, 
												nombreArchivo,  idPortabilidad,
												input.getIdPortaMovil(), input.getUsuario(), IDAttachment);
										if (idAdjunto.compareTo(new BigDecimal(0))>0) {
											respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_ADJUNTO_64, null,
													nombreClase, nombreMetodo, null, input.getCodArea());
	
											listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, "adjuntoPorta",
													idAdjunto + "", Conf.LOG_TIPO_ADJUNTO, "Se registr\u00F3 el adjunto con ID " + idAdjunto
															+ " a la gesti\u00F3n con ID " + idPortabilidad + ".",
													""));
										}
									} else {
										conn.rollback();
										respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT,
												"Error al consumir servicio ddCustomerAttachment ", nombreClase, nombreMetodo, "", input.getCodArea());
										objRespuestaPortabilidad.setRespuesta(respuesta);
										return objRespuestaPortabilidad;
	
									}
	
							}
						}
					
					
									
					
					if (respModify.getMensaje().getCodresultado().intValue() == 1) {
						// agrego submit
						
						respSubmit = SOPortabilidad.finalizarOrdenVentaFS(conn, input.getUsuario(), input.getCodArea(),
								respModify.getIdOrdenVenta());
						if (respSubmit.getMensaje().getCodresultado().intValue() == 1) {
							OperacionCreaPortabilidad.updatePorta(conn, input.getIdPortaMovil(),
									respModify.getIdOrdenVenta(), input.getUsuario(), ID_PAIS);
							respuesta = getMensaje(Conf_Mensajes.OK_PORTABILIDAD_83, null, null, null, null, input.getCodArea());
							conn.commit();
							// eliminar el archivo de la tabla tc_Sc_adjunto
							if (DocumentosAdicionales.size()>0)
							{
								for (int i = 0; i < DocumentosAdicionales.size(); i++) {
									boolean delete= OperacionCreaPortabilidad.delArchivo(conn, new BigDecimal(DocumentosAdicionales.get(i).getTcscadjuntoid()), ID_PAIS);
									if (delete) {
										respuesta = getMensaje(Conf_Mensajes.OK_ELIMINA_ADJUNTO_79, null, nombreClase, nombreMetodo,
												null, input.getCodArea());

										listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, "eliminarAdjunto", DocumentosAdicionales.get(i).getTcscadjuntoid(),
												Conf.LOG_TIPO_ADJUNTO, "Se elimin\u00F3 el adjunto de gesti\u00F3n.", ""));
									} else {
										respuesta = getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null, nombreClase, nombreMetodo,
												null, input.getCodArea());

										listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO,  "eliminarAdjunto", DocumentosAdicionales.get(i).getTcscadjuntoid(),
												Conf.LOG_TIPO_NINGUNO, "Ocurrio un inconveniente al eliminar adjunto.", ""));
									}
								}
							}
							// fin eliminacion de archivos					
						} else {
							conn.rollback();
							respuesta = getMensaje(Conf_Mensajes.MSJ_SUBMIT_PORTA_918,
									respSubmit.getMensaje().getDescripcion(), nombreClase, nombreMetodo, null, input.getCodArea());
						}
						// fin 
					} else {
						conn.rollback();
						respuesta = getMensaje(Conf_Mensajes.MSJ_INCONVENIENTES_REGISTRO_PORTA_676,
								respModify.getMensaje().getDescripcion(), nombreClase, nombreMetodo, null, input.getCodArea());
					}
			
				} else {
					conn.rollback();
					respuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_PORTA_916, respModify.getMensaje().getDescripcion(),
							nombreClase, nombreMetodo, null, input.getCodArea());
				}
				}else{
					conn.rollback();
					respuesta = getMensaje(Conf_Mensajes.MSJ_NO_CREANUMERO_920, creaNumero.getMensaje().getDescripcion(),
							nombreClase, nombreMetodo, null, input.getCodArea());
				}
			} else {
				conn.rollback();
				log.trace("El producto ingresado no esta activo");
				respuesta = getMensaje(Conf_Mensajes.MSJ_PRODUCTO_INGRESADO_NO_ACTIVO_681, null,
						nombreClase, nombreMetodo, null, input.getCodArea());
				objRespuestaPortabilidad.setRespuesta(respuesta);
			}

		} catch (Exception e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					log.error(e1,e1);
				}
			log.trace("Problemas con el servicio" + e);
			log.error(e,e);
			 respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());
			objRespuestaPortabilidad.setRespuesta(respuesta);
		}
		
		finally {
			DbUtils.closeQuietly(conn);
			objRespuestaPortabilidad.setRespuesta(respuesta);
		}

		return objRespuestaPortabilidad;	 




	}

	public ModifySalesOrder getDatosAtributosPorta(Connection conn, InputCreaPortabilidad datos, String estadoAlta,
			ConsultaCrearOrdenVenta datosVenta, String idSalesOrder, String idOrderItem, BigDecimal ID_PAIS) throws SQLException {
		ModifySalesOrder response = new ModifySalesOrder();
		List<AtributosModify> LstAtributos = new ArrayList<AtributosModify>();
		AtributosModify atributo = new AtributosModify();
		HashMap<String, String> atributosPorta ;

		atributosPorta = getGrupoParam(conn, estadoAlta, Conf.GRUPO_CARACTERISTICAS_PORTA_FS, ID_PAIS);

		response.setCodArea(datos.getCodArea());
		response.setUsuario(datos.getUsuario());
		response.setIdDistributionChanel(datosVenta.getIdCanalDistribucion());
		response.setIdCustomer(datosVenta.getIdCliente());
		response.setIdLocation(datosVenta.getIdUbicacion());

		log.trace("idatributo C_NUMERO_PORTAR:"+atributosPorta.get(Conf.C_NUMERO_PORTAR));
		atributo.setIdAtributo(atributosPorta.get(Conf.C_NUMERO_PORTAR));
		log.trace("idatributo C_NUMERO_PORTAR:"+atributo.getIdAtributo());
		atributo.setValorAtributo(datos.getNumPortar());
		LstAtributos.add(atributo);

		atributo = new AtributosModify();
		log.trace("idatributoNIP:"+atributosPorta.get(Conf.C_NIP));
		atributo.setIdAtributo(atributosPorta.get(Conf.C_NIP));
		log.trace("idAtributoNIP:"+ atributo.getIdAtributo());
		atributo.setValorAtributo(datos.getCip());
		LstAtributos.add(atributo);

		atributo = new AtributosModify();
		atributo.setIdAtributo(atributosPorta.get(Conf.C_NUMERO_TEMPORAL));
		log.trace("idatributo C_NUMERO_TEMPORAL:"+atributo.getIdAtributo());
		atributo.setValorAtributo("9149103871613522642");
		LstAtributos.add(atributo);

		atributo = new AtributosModify();
		atributo.setIdAtributo(atributosPorta.get(Conf.C_OP_DONANTE));
		log.trace("idatributo C_OP_DONANTE:"+atributo.getIdAtributo());
		atributo.setValorAtributo(UtileriasJava.getConfig(conn, Conf.OPERADOR_DONANTE, datos.getOperadorDonante(), datos.getCodArea()));
		LstAtributos.add(atributo);

		atributo = new AtributosModify();
		atributo.setIdAtributo(atributosPorta.get(Conf.C_OP_RECEPTOR));
		log.trace("idatributo C_OP_RECEPTOR:"+atributo.getIdAtributo());
		atributo.setValorAtributo(UtileriasJava.getConfig(conn, Conf.OPERADOR_DONANTE, Conf.OP_TELEFONICA, datos.getCodArea()));
		LstAtributos.add(atributo);

		atributo = new AtributosModify();
		atributo.setIdAtributo(atributosPorta.get(Conf.C_VALIDATION));
		log.trace("idatributo C_VALIDATION:"+atributo.getIdAtributo());
		atributo.setValorAtributo("7777001");
		LstAtributos.add(atributo);
		
		atributo=new AtributosModify();
		atributo.setIdAtributo(atributosPorta.get(Conf.C_RAZONVENTA));
		log.trace("id Razon de venta: "+ atributo.getIdAtributo());
		atributo.setValorAtributo("9146226178865059075");
		LstAtributos.add(atributo);
		
		response.setAtributos(LstAtributos);
		return response;
	}

	public OutputAdjuntoPorta cargarAdjuntoPorta(InputCreaPortabilidad input, BigDecimal idPortabilidad,
			String IdOrdenVenta, String BusinessInteraction) {
		String nombreMetodo = "cargarAdjunto";
		String servicioPost = Conf.LOG_POST_ADJUNTO;
		String objectIDRootEntity = "";
		Connection conn = null;
		Respuesta objRespuesta = new Respuesta();
		OutputAdjuntoPorta output = new OutputAdjuntoPorta();
		
		List<LogSidra> listaLog1 = new ArrayList<LogSidra>();
		String nombreClase = new CurrentClassGetter().getClassName();
		String nombreArchivo="";
		try {
			conn = getConnRegional();
//			getIdPais(conn, input.getCodArea());

			for (int i = 0; i < input.getAdjuntoporta().size(); i++) {
				// Convirtiendo en un arreglo de bytes el documento recibida en
				// base 64
				byte[] base64Decoded = DatatypeConverter.parseBase64Binary(input.getAdjuntoporta().get(i).getAdjunto());

				log.trace("Tamanio en bytes de imagen:" + base64Decoded.length);
				if (base64Decoded.length < 2097152) {
					nombreArchivo=input.getAdjuntoporta().get(i).getNombreArchivo().replace(" ", "_");
					String IDAttachment = OperacionCreaPortabilidad.getAddCustomerAttachment(conn, objectIDRootEntity,
							nombreArchivo, input.getUsuario(), input.getCodArea(),
							BusinessInteraction);
					if (!"0".equals(IDAttachment)) {
						BigDecimal idAdjunto = OperacionCreaPortabilidad.saveAdjunto(conn, 
								nombreArchivo,  idPortabilidad,
								input.getIdPortaMovil(), input.getUsuario(), IDAttachment);
						if (idAdjunto.compareTo(new BigDecimal(0))>0) {
							objRespuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CREA_ADJUNTO_64, null,
									nombreClase, nombreMetodo, null, input.getCodArea());

							output.setIdAdjunto(idAdjunto + "");
							output.setRespuesta(objRespuesta);

							listaLog1.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost,
									idAdjunto + "", Conf.LOG_TIPO_ADJUNTO, "Se registr\u00F3 el adjunto con ID " + idAdjunto
											+ " a la gesti\u00F3n con ID " + idPortabilidad + ".",
									""));
						}
					} else {
						objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT,
								"Error al consumir servicio ddCustomerAttachment ", nombreClase, nombreMetodo, "", input.getCodArea());
						objRespuesta.setCodResultado("1");
						output.setRespuesta(objRespuesta);

					}

				} else {
					objRespuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_IMG_2MB_629, null, nombreClase,
							nombreMetodo, null, input.getCodArea());
					objRespuesta.setCodResultado("1");
					output.setIdAdjunto("");
					output.setRespuesta(objRespuesta);
					listaLog1.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost, "0",
							Conf.LOG_TIPO_NINGUNO, "Problema al registrar adjunto de gesti\u00F3n.",
							objRespuesta.getDescripcion()));
				}
			}

		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			objRespuesta.setCodResultado("1");
			objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

			listaLog1.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
					"Problema al registrar adjunto de gesti\u00F3n.", e.getMessage()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			objRespuesta.setCodResultado("1");
			objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

			listaLog1.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_ADJUNTO, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
					"Problema al registrar adjunto de gesti\u00F3n.", e.getMessage()));
		} finally {
			DbUtils.closeQuietly(conn);
			output.setRespuesta(objRespuesta);

			UtileriasJava.doInsertLog(listaLog1, input.getUsuario(), input.getCodArea());
		}

		return output;
	}

	 private static void subirFTP(InputStream archivo, String nombre, Connection conn, String codArea) 
			   throws FileNotFoundException{
		 	
			String pathFTP="";
			String usr="";
			String pass="";
			String ip="";
			String port="";

			  JSch jsch = new JSch();
			  Session session = null;
			  ChannelSftp sftpChannel = null;
			  //Obtenemos variables de configuracion 
			  try{
				
				  pathFTP=UtileriasJava.getConfig(conn, Conf.GRUPO_FTP_PORTABILIDAD, Conf.TIPO_FTP_RUTA, codArea);
				  usr=UtileriasJava.getConfig(conn, Conf.GRUPO_FTP_PORTABILIDAD, Conf.TIPO_FTP_USUARIO, codArea);
				  pass=UtileriasJava.getConfig(conn, Conf.GRUPO_FTP_PORTABILIDAD, Conf.TIPO_FTP_CLAV, codArea);
				  ip=UtileriasJava.getConfig(conn, Conf.GRUPO_FTP_PORTABILIDAD, Conf.TIPO_FTP_IP, codArea);
				  port=UtileriasJava.getConfig(conn, Conf.GRUPO_FTP_PORTABILIDAD, Conf.TIPO_FTP_PUERTO, codArea);
				  
				  
			  }catch(Exception e)
			  {
				  log.trace("Error Exception" + e);	  
			  }
 
			  //InputStream archivo, String pathFTP, String nombre, String usr, String pass, String ip, String port
			  try{	 
			   try{
			    session = jsch.getSession(usr, ip, Integer.parseInt(port));
			    session.setConfig("StrictHostKeyChecking", "no");
			    session.setConfig("PreferredAuthentications",         //Esto es para evitar la solicitud de autenticacion
			                    "publickey,keyboard-interactive,password"); //por kerberos al ejecutar desde linux  
			    session .setPassword(pass);         
			    session.connect();
			    
			    Channel channel = session.openChannel("sftp");
			    channel.connect();
			    sftpChannel = (ChannelSftp) channel;
			    sftpChannel.put(archivo, pathFTP + nombre);
			    
			   } finally {
			    try{
				    if(session!=null){	
				     session.disconnect();
				     if(sftpChannel!=null)
				      sftpChannel.exit();
				    }
			    } catch(Exception e){
			     log.error(e, e);
			     log.trace("Error Exception" + e);			  
			    }
			   }
			  } catch(JSchException e){
				     log.error(e, e);
			   log.trace("Error jsch" + e);
			  } catch(SftpException e){
				     log.error(e, e);
			   log.trace("Error sftp" + e);			 
			  } 
			 }

}

package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.impuestos.InputConsultaImpuestos;
import com.consystec.sc.ca.ws.input.impuestos.InputImpuestos;
import com.consystec.sc.ca.ws.input.impuestos.InputImpuestosGrupos;
import com.consystec.sc.ca.ws.input.inventariomovil.InputInventarioCampaniaCondiciones;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.venta.ArticuloPromocionalVenta;
import com.consystec.sc.ca.ws.input.venta.ArticuloVenta;
import com.consystec.sc.ca.ws.input.venta.Descuento;
import com.consystec.sc.ca.ws.input.venta.DetallePago;
import com.consystec.sc.ca.ws.input.venta.Impuesto;
import com.consystec.sc.ca.ws.input.venta.InputVenta;
import com.consystec.sc.ca.ws.input.venta.UpdateSeries;
import com.consystec.sc.ca.ws.mapas.InputMapas;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.impuestos.OutputImpuestos;
import com.consystec.sc.ca.ws.output.venta.ArticuloIncorrecto;
import com.consystec.sc.ca.ws.output.venta.OutputVenta;
import com.consystec.sc.sv.ws.operaciones.OperacionImpuestos;
import com.consystec.sc.sv.ws.operaciones.OperacionInventarioMovil;
import com.consystec.sc.sv.ws.operaciones.OperacionMovimientosInventario;
import com.consystec.sc.sv.ws.operaciones.OperacionVentas;
import com.consystec.sc.sv.ws.operaciones.OperacionVisita;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.ConfiguracionFolioVirtual;
import com.consystec.sc.sv.ws.orm.DescuentosSidra;
import com.consystec.sc.sv.ws.orm.Dispositivo;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.PagoDet;
import com.consystec.sc.sv.ws.orm.PagoImpuesto;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.Venta;
import com.consystec.sc.sv.ws.orm.VentaDet;
import com.consystec.sc.sv.ws.orm.Visita;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.ericsson.sdr.dao.OlsDAO;
import com.ericsson.sdr.utils.Country;
import com.ericsson.sdr.utils.Gestion;

public class CtrlVentaRuta extends ControladorBase {
	private List<LogSidra> listaLog = new ArrayList<LogSidra>();
	private static String servicioPost = Conf.LOG_POST_VENTA;

	private static final Logger log = Logger.getLogger(CtrlVentaRuta.class);
	private boolean hayArtFallido = false;
	private boolean hayArtFallidoImpuesto = false;
	private boolean hayArtFallidoImpuestoNoExiste = false;
	private boolean hayArtFallidoOferta = false;
	private boolean hayArtFallidoCant = false;
	private boolean hayArtFallidoCantPromo = false;
	private boolean hayArtPromoFallido = false;
	private boolean hayArtFallidoOfertaPromo = false;
	private boolean errorTipoDescuento = false;
	private boolean errorDescuento = false;
	private boolean errorDescuentoTotal = false;
	private boolean errorImpuestoArt = false;
	private boolean errorImpuestoTotal = false;
	private boolean errorTotalFactura = false;
	private boolean errorPrecioTotal = false;
	private boolean banderaExento = false;
	MathContext mc = new MathContext(200, RoundingMode.HALF_UP);
	private boolean aplicaCR0 = false;
	private boolean aplicaCR500 = false;
	private boolean aplicaPercepcion = false;
	private String impuestoCR = "CR";
	int indexArticuloImpuestoCR500 = -1;

	// agregado sbarrios 16-04-18
	private boolean pequenioContribuyente = false;

	/**
	 * Validando que no vengan par\u00E9metros nulos.
	 * 
	 * @param objDatos
	 * @param estadoAlta
	 * @param estadoEnUso
	 * @param clienteFinal
	 * @param tipoInvTelca
	 * @param articuloRecarga
	 * @param tipoNoExento
	 * @param tipoExento
	 * @param tipoPagueLleve
	 * @return
	 * @throws SQLException
	 */
	public Respuesta validarDatos(Connection conn, InputVenta objDatos, String estadoAlta, String estadoEnUso,
			String clienteFinal, String tipoInvTelca, String articuloRecarga, String tipoNoExento, String tipoExento,
			String tipoPagueLleve, BigDecimal idPais) throws SQLException {
		String nombreMetodo = "validarDatos";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta objRespuesta = null;
		String tipoCCF = "";

		if ("503".equals(objDatos.getCodArea())) {
			tipoCCF = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPOS_DOCUMENTO_VENTA, Conf.DOCUMENTO_CCF,
					objDatos.getCodArea());
		}

		if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getIdVendedor() == null || "".equals(objDatos.getIdVendedor().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENDEDOR_NULO_38, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getIdJornada() == null || "".equals(objDatos.getIdJornada().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDJORNADA_NULO_89, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getIdBodegaVendedor() == null || "".equals(objDatos.getIdBodegaVendedor().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDBOD_VENDEDOR_NULO_425, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getFecha() == null || "".equals(objDatos.getFecha().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHANULO_40, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getIdVentaMovil() == null || "".equals(objDatos.getIdVentaMovil().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_ID_VENTA_MOVIL_NULO_632, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		// verificaciones y validaciones de folio
		if (objDatos.getFolioManual() == null || "".equals(objDatos.getFolioManual().trim())
				|| !isNumeric(objDatos.getFolioManual())
				|| (!objDatos.getFolioManual().equals("0") && !objDatos.getFolioManual().equals("1"))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_FOLIO_MANUAL_399, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getTipoDocumento() == null || "".equals(objDatos.getTipoDocumento().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPODOCUMENTO_NULO_428, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		} else {
			List<Filtro> condiciones = new ArrayList<Filtro>();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_VALOR,
					objDatos.getTipoDocumento()));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO,
					Conf.GRUPO_TIPOS_DOCUMENTO_VENTA));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,
					idPais.toString()));
			int existeTipoDoc = UtileriasBD.selectCount(conn, Catalogo.N_TABLA, condiciones);

			if (existeTipoDoc <= 0) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPO_DOCVENTA_INVALIDO_467, null, nombreClase, nombreMetodo,
						null, objDatos.getCodArea());

			} else {
				if (objDatos.getTipoDocumento().equalsIgnoreCase(tipoCCF)) {
					if (objDatos.getNit() == null || "".equals(objDatos.getNit().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_NIT_NULO_14, null, nombreClase, nombreMetodo, null,
								objDatos.getCodArea());
					}

					if (objDatos.getRegistroFiscal() == null || "".equals(objDatos.getRegistroFiscal().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_REGFISCAL_NULO_16, null, nombreClase, nombreMetodo,
								null, objDatos.getCodArea());
					}
				}
			}
		}

		// validaciones de serie y folio
		if (!"507".equals(objDatos.getCodArea())
				&& (objDatos.getSerie() == null || "".equals(objDatos.getSerie().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_SERIE_NULO_427, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getFolio() == null || "".equals(objDatos.getFolio().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_FOLIO_NULO_426, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objRespuesta == null) {
			BigDecimal idVentaRegistrada = null;
			// validando que la venta a registrar no ha sido enviada previamente por el
			// movil 25-09-17 sbarrios
			idVentaRegistrada = OperacionVentas.validaSincVenta(conn, objDatos.getFecha(), objDatos.getIdVentaMovil(),
					objDatos.getCodDispositivo(), objDatos.getIdJornada(), objDatos.getCodArea());

			if (!(idVentaRegistrada == null)) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENTA_SINCRONIZADA_633, null, nombreClase, nombreMetodo,
						null, objDatos.getCodArea());
				objRespuesta.setOrigen(idVentaRegistrada + "");
			} else { // fin cambio 25-09-17 sbarrios

				if ("1".equals(objDatos.getFolioManual()) && !"506".equals(objDatos.getCodArea())) {
					// se verifican folios de scl
					if (objDatos.getIdRangoFolio() == null || "".equals(objDatos.getIdRangoFolio().trim())
							|| !isNumeric(objDatos.getIdRangoFolio())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_IDRANGO_FOLIO_385, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
					}

					if (objRespuesta == null) {
						// se verifica que exista el folio correctamente
						objRespuesta = verificarFolio(conn, objDatos.getIdRangoFolio(),
								new Integer(objDatos.getFolio()), estadoAlta, estadoEnUso, objDatos.getCodArea(),
								idPais);

						if (objRespuesta != null) {
							// se verifica que el folio no este registrado en otra venta
							List<Filtro> condiciones = new ArrayList<Filtro>();
							if (!"507".equals(objDatos.getCodArea())) {
								condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
										Venta.CAMPO_SERIE, objDatos.getSerie()));
							}
							condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Venta.CAMPO_NUMERO,
									objDatos.getFolio()));
							condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
									Venta.CAMPO_TIPO_DOCUMENTO, objDatos.getTipoDocumento()));
							condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
									Venta.CAMPO_TCSCCATPAISID, idPais.toString()));
							String idVenta = UtileriasBD.getOneRecord(conn, Venta.CAMPO_TCSCVENTAID, Venta.N_TABLA,
									condiciones);
							if (idVenta != null && !idVenta.equals("")) {
								objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_FOLIO_280, null, nombreClase,
										nombreMetodo, null, objDatos.getCodArea());
								objRespuesta.setOrigen(idVenta); // reutilizo el objeto en la respuesta para devolver el
																	// idVenta
							}
						}
					}
				} else {
					// se verifican folios de sidra
					// se verifica que el folio no este registrado en otra venta
					List<Filtro> condiciones = new ArrayList<Filtro>();
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Venta.CAMPO_SERIE_SIDRA,
							objDatos.getSerie()));
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Venta.CAMPO_FOLIO_SIDRA,
							objDatos.getFolio()));
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Venta.CAMPO_TCSCCATPAISID,
							idPais.toString()));
					String idVenta = UtileriasBD.getOneRecord(conn, Venta.CAMPO_TCSCVENTAID, Venta.N_TABLA,
							condiciones);
					if (idVenta != null && !"".equals(idVenta)) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_FOLIO_280, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						objRespuesta.setOrigen(idVenta); // reutilizo el objeto en la respuesta para devolver el idVenta
						return objRespuesta;
					}
				}
			}
		}

		if (objDatos.getTipo() == null || "".equals(objDatos.getTipo().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPO_NULO_431, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		} else {
			String tipo = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPO_CLIENTE_VENTA, objDatos.getTipo().toUpperCase(),
					objDatos.getCodArea());
			log.trace("tipoCliente, tipo enviado: " + objDatos.getTipo() + ", tipo obtenido: " + tipo);

			if (tipo.equalsIgnoreCase(clienteFinal)) {
				if (objDatos.getNombre() == null || "".equals(objDatos.getNombre().trim())) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOMBRE_NULO_45, null, nombreClase, nombreMetodo, null,
							objDatos.getCodArea());
				}

				if (objDatos.getApellido() == null || "".equals(objDatos.getApellido().trim())) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_APELLIDO_375, null, nombreClase, nombreMetodo, null,
							objDatos.getCodArea());
				}

				if (objDatos.getNumTelefono() == null || "".equals(objDatos.getNumTelefono().trim())
						|| !isNumeric(objDatos.getNumTelefono())) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_NUMTELEFONO_70, null, nombreClase, nombreMetodo, null,
							objDatos.getCodArea());
				} else if (objDatos.getNumTelefono().length() != Conf.LONG_NUMERO) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONG_NUM_INVALIDA_504, null, nombreClase, nombreMetodo,
							null, objDatos.getCodArea());
				}

				if (objDatos.getTipoDocCliente() == null || "".equals(objDatos.getTipoDocCliente().trim())) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPO_DOC_CLIENTE_373, null, nombreClase, nombreMetodo,
							null, objDatos.getCodArea());
				} else {
					List<Filtro> condiciones = new ArrayList<Filtro>();
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO,
							Conf.GRUPO_DOC_IDENT_ALTA));
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE,
							objDatos.getTipoDocCliente()));
					condiciones.add(
							UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
							Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));

					int existeDocCliente = UtileriasBD.selectCount(conn, Catalogo.N_TABLA, condiciones);
					if (existeDocCliente <= 0) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_TIPO_DOCCLIENTE_378, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
					}
				}

				if (objDatos.getNumDocCliente() == null || "".equals(objDatos.getNumDocCliente().trim())) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_NUM_DOC_CLIENTE_374, null, nombreClase, nombreMetodo,
							null, objDatos.getCodArea());
				}
			}
		}

		if (objDatos.getMontoFactura() == null || "".equals(objDatos.getMontoFactura().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_MONTOFACTURA_NULO_436, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		} else if (!isDecimal(objDatos.getMontoFactura()) && !isNotCientif(objDatos.getMontoFactura())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_DECIMAL_INVALIDO_434, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getMontoPagado() == null || "".equals(objDatos.getMontoPagado().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_MONTOPAGADO_NULO_437, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		} else if (!isDecimal(objDatos.getMontoPagado()) && !isNotCientif(objDatos.getMontoPagado())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_DECIMAL_INVALIDO_434, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objRespuesta == null) {
			if (objDatos.getExento() == null || (!objDatos.getExento().equalsIgnoreCase(tipoExento)
					&& !objDatos.getExento().equalsIgnoreCase(tipoNoExento))) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_TIPO_EXENTO_810, null, nombreClase, nombreMetodo,
						null, objDatos.getCodArea());

			} else if (objDatos.getExento().equalsIgnoreCase(tipoExento)) {
				if (objDatos.getImpuestosExento() != null && objDatos.getImpuestosExento().size() > 0) {
					// se valida el listado de impuestos exentos
					String listadoImpuestosExentos = "";
					for (int i = 0; i < objDatos.getImpuestosExento().size(); i++) {
						if (objDatos.getImpuestosExento().get(i).getNombreImpuesto() == null
								|| objDatos.getImpuestosExento().get(i).getNombreImpuesto().trim().equals("")) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOMBRE_IMPUESTO_EXENTO_NULO_813, null,
									nombreClase, nombreMetodo, i + 1 + "", objDatos.getCodArea());
							return objRespuesta;
						}
						listadoImpuestosExentos += "'"
								+ objDatos.getImpuestosExento().get(i).getNombreImpuesto().toUpperCase() + "'";
						if (i < objDatos.getImpuestosExento().size() - 1) {
							listadoImpuestosExentos += ",";
						}
					}

					objRespuesta = OperacionVentas.validarImpuestosExentos(conn, listadoImpuestosExentos, estadoAlta,
							objDatos.getCodArea(), idPais);
					if (objRespuesta != null) {
						return objRespuesta;
					} else {
						banderaExento = true;
					}
				} else {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_LIST_IMPUESTOS_EXENTO_812, null, nombreClase,
							nombreMetodo, null, objDatos.getCodArea());
					return objRespuesta;
				}
			}

			// Validando detalle de formas de pago
			if (objDatos.getDetallePago() == null || objDatos.getDetallePago().size() == 0) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_DETALLEPAGO_NULO_439, null, nombreClase, nombreMetodo, null,
						objDatos.getCodArea());
			} else {
				int a = 0;
				log.trace("Inicia a validar detalle pago");

				for (DetallePago obj : objDatos.getDetallePago()) {
					a++;

					if (obj.getMonto() == null || "".equals(obj.getMonto().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_MONTO_DETPAGO_NULO_443, null, nombreClase,
								nombreMetodo, "" + a, objDatos.getCodArea());
					} else if (!isDecimal(obj.getMonto()) && !isNotCientif(obj.getMonto())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_DECIMAL_INVALIDO_434, null, nombreClase,
								nombreMetodo, "Elemento de Detalle Pago No." + a, objDatos.getCodArea());
					}

					if (obj.getFormaPago() == null || "".equals(obj.getFormaPago().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_FORMAPAGO_DETPAGO_NULO_442, null, nombreClase,
								nombreMetodo, "" + a, objDatos.getCodArea());
					} else {
						// TODO comentar y descomentar forma de pago cheque, solo en CR no aplica el
						// pago cheque
						if (obj.getFormaPago() == null || "".equals(obj.getFormaPago())
								|| (!obj.getFormaPago().equalsIgnoreCase(Conf.FORMA_PAGO_EFECTIVO)
										&& !obj.getFormaPago().equalsIgnoreCase(Conf.FORMA_PAGO_TARJETA)
										&& !obj.getFormaPago().equalsIgnoreCase(Conf.FORMA_PAGO_CHEQUE)
										&& !obj.getFormaPago().equalsIgnoreCase(Conf.FORMA_PAGO_CREDITO)
										&& !obj.getFormaPago().equalsIgnoreCase(Conf.FORMA_PAGO_MPOS))) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_FORMA_PAGO_INVALIDO_468, null, nombreClase,
									nombreMetodo, "Elemento de detalle de pago No." + a, objDatos.getCodArea());
							break;

						} else {
							// la forma de pago es correcta
							if (!obj.getFormaPago().equalsIgnoreCase(Conf.FORMA_PAGO_EFECTIVO)) {
								if (obj.getFormaPago().equalsIgnoreCase(Conf.FORMA_PAGO_CREDITO)) {
									log.trace("es pago de credito");
								} else if (obj.getFormaPago().equalsIgnoreCase(Conf.FORMA_PAGO_MPOS)) {
									
									log.trace("es pago de MPOS");
								} else {

									if (obj.getBanco() == null || "".equals(obj.getBanco().trim())) {
										objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_DETPAGO_BANCO_730, null,
												nombreClase, nombreMetodo, a + ".", objDatos.getCodArea());
										break;
									}

									if (obj.getFormaPago().equalsIgnoreCase(Conf.FORMA_PAGO_TARJETA)) {
										log.trace("es pago con tarjeta");
										if (obj.getDigitosTarjeta() == null || "".equals(obj.getDigitosTarjeta().trim())
												|| !isNumeric(obj.getDigitosTarjeta())) {
											objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_DETPAGO_DIGITOS_880, null,
													nombreClase, nombreMetodo, a + ".", objDatos.getCodArea());
											break;
										}

										if (obj.getMarcaTarjeta() == null || "".equals(obj.getMarcaTarjeta().trim())) {
											objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_DETPAGO_MARCA_879, null,
													nombreClase, nombreMetodo, a + ".", objDatos.getCodArea());
											break;
										}

										if (obj.getNumAutorizacion() == null
												|| "".equals(obj.getNumAutorizacion().trim())
												|| !isNumeric(obj.getNumAutorizacion())) {
											objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_DETPAGO_NUMAUT_731, null,
													nombreClase, nombreMetodo, a + ".", objDatos.getCodArea());
											break;
										}
									} else { // comentar y decomentar forma de pago cheque
										log.trace("es pago con cheque");
										if (obj.getNumeroCheque() == null || "".equals(obj.getNumeroCheque().trim())) {
											objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_DETPAGO_NUMCHEQUE_875,
													null, nombreClase, nombreMetodo, a + ".", objDatos.getCodArea());
											break;
										}

										if (obj.getFechaEmision() == null || "".equals(obj.getFechaEmision().trim())) {
											objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_DETPAGO_FECHAEMISION_876,
													null, nombreClase, nombreMetodo, a + ".", objDatos.getCodArea());
											break;
										}

										if (obj.getCuentaCliente() == null
												|| "".equals(obj.getCuentaCliente().trim())) {
											objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_DETPAGO_CUENTA_877, null,
													nombreClase, nombreMetodo, a + ".", objDatos.getCodArea());
											break;
										}

										if (obj.getNumeroReserva() == null || "".equals(obj.getNumeroReserva().trim())
												|| !isNumeric(obj.getNumeroReserva())) {
											objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_DETPAGO_NUMRESERVA_878,
													null, nombreClase, nombreMetodo, a + ".", objDatos.getCodArea());
											break;
										}
									} // */
								}
							}
						}
					}
				}
			}
		}

		if (objRespuesta == null) {
			// Validando todos los art\u00EDculos ingresados para la venta
			if (objDatos.getArticulos() == null || objDatos.getArticulos().size() == 0) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_ARTICULOS_NULO_48, null, nombreClase, nombreMetodo, null,
						objDatos.getCodArea());
			} else {
				int a = 0;
				for (ArticuloVenta obj : objDatos.getArticulos()) {
					a++;
					if (obj.getArticulo() == null || "".equals(obj.getArticulo().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ARTICULO_DETART_VENTA_NULO_445, null, nombreClase,
								nombreMetodo, "" + a, objDatos.getCodArea());
					}

					if (obj.getArticulo().equals(articuloRecarga)) {
						if (obj.getNumTelefono() == null || "".equals(obj.getNumTelefono().trim())
								|| !isNumeric(obj.getNumTelefono())) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_ARTICULO_NUMTELEFONO_VENTA_NULO_449, null,
									nombreClase, nombreMetodo, "" + a, objDatos.getCodArea());
						} else if (obj.getNumTelefono().length() != Conf.LONG_NUMERO) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONG_NUM_INVALIDA_504, null, nombreClase,
									nombreMetodo, "No. Elemento: " + a, objDatos.getCodArea());
						}
					}

					if (obj.getCantidad() == null || "".equals(obj.getCantidad().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_CANTIDAD_DETART_VENTA_NULO_446, null, nombreClase,
								nombreMetodo, "" + a, objDatos.getCodArea());
					}

					if (obj.getSeriado() == null || "".equals(obj.getSeriado().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_SERIADO_DETART_VENTA_NULO_462, null, nombreClase,
								nombreMetodo, "" + a, objDatos.getCodArea());

					} else if (obj.getRango() == null || "".equals(obj.getRango().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_RANGO_DETART_VENTA_NULO_463, null, nombreClase,
								nombreMetodo, "" + a, objDatos.getCodArea());

					} else if (("1".equals(obj.getSeriado()) && "0".equals(obj.getRango()))) {
						if (obj.getSerieInicial() == null || "".equals(obj.getSerieInicial().trim())) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_SERIE_NULO_427, null, nombreClase, nombreMetodo,
									"" + a, objDatos.getCodArea());
						}

					} else if (("1".equals(obj.getSeriado()) && "1".equals(obj.getRango()))) {
						if (obj.getSerieInicial() == null || obj.getSerieInicial().trim().equals("")) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_SERIE_NULO_427, null, nombreClase, nombreMetodo,
									"" + a, objDatos.getCodArea());
						}

						if (obj.getSerieFinal() == null || "".equals(obj.getSerieFinal().trim())) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_SERIEFINAL_DETART_VENTA_NULO_464, null,
									nombreClase, nombreMetodo, "" + a, objDatos.getCodArea());
						}
					}

					if (obj.getTipoInv() == null || "".equals(obj.getTipoInv().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ARTICULO_TIPOINV_VENTA_NULO_450, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
					} else if (!obj.getTipoInv().equalsIgnoreCase(tipoInvTelca)) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOINV_DETART_VENTA_INVALIDO_465, null,
								nombreClase, nombreMetodo, null, objDatos.getCodArea());
					}

					if (obj.getPrecio() == null || "".equals(obj.getPrecio().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_PRECIO_TOTAL_DETART_VENTA_NULO_457, null,
								nombreClase, nombreMetodo, null, objDatos.getCodArea());
					} else if (!isDecimal(obj.getPrecio()) && !isNotCientif(obj.getPrecio())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_DECIMAL_INVALIDO_434, null, nombreClase,
								nombreMetodo, "Elemento de Articulos No." + a + ". Parámetro precio.",
								objDatos.getCodArea());
					}

					if (obj.getDescuentoSCL() == null || "".equals(obj.getDescuentoSCL().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_DESCUENTOSCL_DETART_VENTA_NULO_452, null,
								nombreClase, nombreMetodo, null, objDatos.getCodArea());
					} else if (!isDecimal(obj.getDescuentoSCL()) && !isNotCientif(obj.getDescuentoSCL())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_DECIMAL_INVALIDO_434, null, nombreClase,
								nombreMetodo, "Elemento de Articulos No." + a + ". Parámetro descuentoSCL.",
								objDatos.getCodArea());
					}

					if (obj.getDescuentoSidra() == null || "".equals(obj.getDescuentoSidra().trim())) {
						obj.setDescuentoSidra("0");
					} else if (!isDecimal(obj.getDescuentoSidra()) && !isNotCientif(obj.getDescuentoSidra())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_DECIMAL_INVALIDO_434, null, nombreClase,
								nombreMetodo, "Elemento de Articulos No." + a + ". Parámetro descuentoSidra.",
								objDatos.getCodArea());
					}

					// detalle de descuentos por art\u00EDculo
					if (new BigDecimal(obj.getDescuentoSidra()).compareTo(BigDecimal.ZERO) > 0) {
						if (obj.getDetalleDescuentosSidra() == null || obj.getDetalleDescuentosSidra().isEmpty()) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_DET_DESCUENTOS_889, null, nombreClase,
									nombreMetodo, null, objDatos.getCodArea());

						} else {
							int a1 = 0;
							for (Descuento descuento : obj.getDetalleDescuentosSidra()) {
								a1++;

								if (descuento.getIdOfertaCampania() == null
										|| "".equals(descuento.getIdOfertaCampania().trim())) {
									objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDOFERTACAMPANIA_DETART_VENTA_NULO_454,
											null, nombreClase, nombreMetodo, a + ", detalle " + a1,
											objDatos.getCodArea());
								} else if (!isNumeric(descuento.getIdOfertaCampania())) {
									objRespuesta = getMensaje(Conf_Mensajes.MSJ_DECIMAL_INVALIDO_434, null, nombreClase,
											nombreMetodo,
											"Elemento de Articulos No." + a + ". Parámetro descuentoSidra.",
											objDatos.getCodArea());
								}

								if (descuento.getDescuento() == null || "".equals(descuento.getDescuento().trim())
										|| (!isDecimal(descuento.getDescuento())
												&& !isNotCientif(descuento.getDescuento()))) {
									objRespuesta = getMensaje(Conf_Mensajes.MSJ_VALOR_DESCUENTO_NULO_419, null,
											nombreClase, nombreMetodo,
											"Artículo No. " + a + ". Detalle Descuentos Artículos No. " + a1 + ".",
											objDatos.getCodArea());
								}

								if (descuento.getTipoDescuento() == null
										|| "".equals(descuento.getTipoDescuento().trim())) {
									objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPO_DESCUENTO_NO_DEFINIDO_420, null,
											nombreClase, nombreMetodo, "" + a1, objDatos.getCodArea());
								} else {
									int tipoDescuento = UtileriasJava.getCountConfigValor(conn,
											Conf.GRUPO_CONDICION_TIPOOFERTA, descuento.getTipoDescuento(), estadoAlta,
											objDatos.getCodArea());
									if (tipoDescuento < 1) {
										objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPO_DESCUENTO_NO_DEFINIDO_420,
												null, nombreClase, nombreMetodo,
												"Artículo No. " + a + ". Detalle Descuentos Artículos No. " + a1 + ".",
												objDatos.getCodArea());
									}
								}
								if (descuento.getTipoDescuento() != null
										&& descuento.getTipoDescuento().equals(tipoPagueLleve)) {
									if (descuento.getIdCondicion() == null
											|| "".equals(descuento.getIdCondicion().trim())) {
										objRespuesta = getMensaje(Conf_Mensajes.MSJ_CONDICION_NULO_418, null,
												nombreClase, nombreMetodo, a + ", detalle " + a1,
												objDatos.getCodArea());
									} else if (!isNumeric(descuento.getIdCondicion())) {
										objRespuesta = getMensaje(Conf_Mensajes.MSJ_CONDICION_NUM_413, null,
												nombreClase, nombreMetodo, "Elemento de Articulos No." + a + ".",
												objDatos.getCodArea());
									}
								}
							}
						}
					}

					if (obj.getImpuesto() == null || "".equals(obj.getImpuesto().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_IMPUESTO_DETART_VENTA_NULO_456, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
					} else if (!isDecimal(obj.getImpuesto()) && !isNotCientif(obj.getImpuesto())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_IMPUESTO_DETART_VENTA_NULO_456, null, nombreClase,
								nombreMetodo, "Elemento de Articulos No." + a + ". Parámetro impuesto.",
								objDatos.getCodArea());
					}

					if (obj.getPrecioTotal() == null || "".equals(obj.getPrecioTotal().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_PRECIO_TOTAL_DETART_VENTA_NULO_457, null,
								nombreClase, nombreMetodo, null, objDatos.getCodArea());
					} else if (!isDecimal(obj.getPrecioTotal()) && !isNotCientif(obj.getPrecioTotal())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_IMPUESTO_DETART_VENTA_NULO_456, null, nombreClase,
								nombreMetodo, "Elemento de Articulos No." + a + ". Parámetro precioTotal.",
								objDatos.getCodArea());
					}

					// detalle de impuestos por art\u00EDculo
					if (!banderaExento
							&& (obj.getImpuestosArticulo() == null || obj.getImpuestosArticulo().size() == 0)) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_DET_IMPUESTOSARTICULO_NULO_459, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						return objRespuesta;

					} else {
						int a1 = 0;
						for (Impuesto obj1 : obj.getImpuestosArticulo()) {
							a1++;
							if (obj1.getNombreImpuesto() == null || "".equals(obj1.getNombreImpuesto().trim())) {
								objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOMBRE_IMPUESTO_NULO_440, null, nombreClase,
										nombreMetodo, "" + a1, objDatos.getCodArea());
							}
							if (obj1.getValor() == null || "".equals(obj1.getValor().trim())) {
								objRespuesta = getMensaje(Conf_Mensajes.MSJ_VALOR_IMPUESTO_NULO_441, null, nombreClase,
										nombreMetodo, "" + a1, objDatos.getCodArea());
							} else if (!isDecimal(obj1.getValor()) && !isNotCientif(obj1.getValor())) {
								objRespuesta = getMensaje(Conf_Mensajes.MSJ_DECIMAL_INVALIDO_434, null, nombreClase,
										nombreMetodo,
										"Elemento de Detalle Impuestos Art\u00EDculos No." + a1 + ". Parámetro valor.",
										objDatos.getCodArea());
							}
						}
					}
				}
			}

			// se valida que no envien art\u00EDculos iguales o recargas con el mismo
			// n\u00FAmero
			// se comenta la parte de la recarga al permitirse envios iguales 22-02-2017
			int numeroArticulo = 1;
			for (ArticuloVenta detActual : objDatos.getArticulos()) {
				if (objRespuesta != null)
					break;
				int indexArt = 1;

				for (ArticuloVenta detalle : objDatos.getArticulos()) {
					if (detalle.getArticulo().equals(articuloRecarga)
							|| detalle.getTipoGrupoSidra().equalsIgnoreCase(Conf.TIPO_GRUPO_BONO)) {
						log.trace("1");
					} else {
						// solo se valida articulo
						if (detalle.getSerieInicial() != null && !"".equals(detalle.getSerieInicial())
								&& detActual.getSerieInicial() != null && !"".equals(detActual.getSerieInicial())) {
							if (indexArt != numeroArticulo
									&& detalle.getArticulo().trim().equalsIgnoreCase(detActual.getArticulo().trim())
									&& detalle.getSerieInicial().trim()
											.equalsIgnoreCase(detActual.getSerieInicial().trim())
									&& detalle.getTipoInv().trim().equalsIgnoreCase(detActual.getTipoInv().trim())) {
								objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_OC_DET_IGUALES_237, null,
										nombreClase, nombreMetodo,
										"#" + indexArt + " y #" + numeroArticulo + ", ID de art\u00EDculo "
												+ detActual.getArticulo() + " - Serie: " + detalle.getSerieInicial(),
										objDatos.getCodArea());
							}
						} else {
							if (indexArt != numeroArticulo
									&& detalle.getArticulo().trim().equalsIgnoreCase(detActual.getArticulo().trim())
									&& detalle.getTipoInv().trim().equalsIgnoreCase(detActual.getTipoInv().trim())) {
								objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_OC_DET_IGUALES_237, null,
										nombreClase, nombreMetodo, "#" + indexArt + " y #" + numeroArticulo
												+ ", ID de art\u00EDculo " + detActual.getArticulo() + ".",
										objDatos.getCodArea());
							}
						}
					}

					indexArt++;
				}

				numeroArticulo++;
			}
		}

		if (objRespuesta == null) {
			// validando art\u00EDculos promocionales
			if (objDatos.getArticulosPromocionales() == null || objDatos.getArticulosPromocionales().size() == 0
					|| objDatos.getArticulosPromocionales().isEmpty()) {
				log.trace("1");
			} else {
				log.trace("Articulos promocionales:" + objDatos.getArticulosPromocionales().size()
						+ objDatos.getArticulosPromocionales().isEmpty());
				int a = 0;
				for (ArticuloPromocionalVenta obj : objDatos.getArticulosPromocionales()) {
					a++;
					if (obj.getIdOfertaCampania() == null || "".equals(obj.getIdOfertaCampania().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDOFERTACAMPANIA_DETART_VENTA_NULO_454, null,
								nombreClase, nombreMetodo, null, objDatos.getCodArea());
					}
					if (obj.getArticuloPromocional() == null || "".equals(obj.getArticuloPromocional().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ARTICULOPROMOCIONAL_DETART_PROMO_NULO_460, null,
								nombreClase, nombreMetodo, null, objDatos.getCodArea());
					}
					if (obj.getCantidad() == null || "".equals(obj.getCantidad().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_CANTIDAD_DETART_PROMO_NULO_461, null, nombreClase,
								nombreMetodo, "" + a, objDatos.getCodArea());
					}
					if (obj.getTipoGrupoSidra() == null || "".equals(obj.getTipoGrupoSidra().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_GRUPOSIDRA_DETART_PROMO_NULO_869, null, nombreClase,
								nombreMetodo, "" + a, objDatos.getCodArea());
					}
				}
			}
		}

		if (objDatos.getLatitud() == null || "".equals(objDatos.getLatitud().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_LATITUD_NULO_25, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		} else if (!isValido(objDatos.getLatitud())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_LAT_INVALIDA_29, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getLongitud() == null || "".equals(objDatos.getLongitud().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONG_NULO_26, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		} else if (!isValido(objDatos.getLongitud())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONG_INVALIDA_28, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getTasaCambio() == null || "".equals(objDatos.getTasaCambio().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TASA_CAMBIO_859, null, nombreClase, nombreMetodo,
					null, objDatos.getCodArea());
		} else if (!isDecimal(objDatos.getTasaCambio()) && !isNotCientif(objDatos.getTasaCambio())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TASA_CAMBIO_NUM_860, null, nombreClase,
					nombreMetodo, null, objDatos.getCodArea());
		}

		if (objDatos.getModoOnline() == null || "".equals(objDatos.getModoOnline().trim())
				|| !isNumeric(objDatos.getModoOnline())
				|| (!"0".equals(objDatos.getModoOnline()) && !"1".equals(objDatos.getModoOnline()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_MODO_ONLINE_871, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		return objRespuesta;
	}

	/**
	 * Funci\u00F3n que valida que el folio exista con los valores correctos.
	 * 
	 * @param conn
	 * @param idRangoFolio
	 * @param tipoDocumento
	 * @param serie
	 * @param folioEnviado
	 * @param estadoAlta
	 * @param estadoEnUso
	 * @return
	 * @throws SQLException
	 */
	private Respuesta verificarFolio(Connection conn, String idRangoFolio, int folioEnviado, String estadoAlta,
			String estadoEnUso, String codArea, BigDecimal idPais) throws SQLException {
		String nombreMetodo = "verificarFolio";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta objRespuesta = null;

		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
				ConfiguracionFolioVirtual.CAMPO_TC_SC_FOLIO_ID, idRangoFolio));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
				ConfiguracionFolioVirtual.CAMPO_TC_SC_CATPAISID, idPais.toString()));

		String existeFolio = UtileriasBD.verificarExistencia(conn, ConfiguracionFolioVirtual.N_TABLA, condiciones);
		if (new Integer(existeFolio) > 0) {
			String[] campos = { ConfiguracionFolioVirtual.CAMPO_TIPODOCUMENTO, ConfiguracionFolioVirtual.CAMPO_SERIE,
					ConfiguracionFolioVirtual.CAMPO_NO_INICIAL_FOLIO, ConfiguracionFolioVirtual.CAMPO_NO_FINAL_FOLIO,
					ConfiguracionFolioVirtual.CAMPO_ULTIMO_UTILIZADO, ConfiguracionFolioVirtual.CAMPO_FOLIO_SIGUIENTE,
					ConfiguracionFolioVirtual.CAMPO_CANT_UTILIZADOS, ConfiguracionFolioVirtual.CAMPO_ESTADO };

			Map<String, String> datosFolio = UtileriasBD.getSingleFirstData(conn, ConfiguracionFolioVirtual.N_TABLA,
					campos, condiciones);

			if (!datosFolio.get(ConfiguracionFolioVirtual.CAMPO_ESTADO).equalsIgnoreCase(estadoAlta)
					&& !datosFolio.get(ConfiguracionFolioVirtual.CAMPO_ESTADO).equalsIgnoreCase(estadoEnUso)) {
				// el folio no se encuentra en un estado correcto ALTA o EN_USO
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_ESTADO_FOLIO_383, null, nombreClase, nombreMetodo,
						null, codArea);
			} else {

				// el folio es correcto, verificar rango para el setear estadoFolio y
				// folioSiguiente
				String ultimoUtilizado = datosFolio.get(ConfiguracionFolioVirtual.CAMPO_ULTIMO_UTILIZADO);

				if (ultimoUtilizado == null || "".equals(ultimoUtilizado)) {
					if (!datosFolio.get(ConfiguracionFolioVirtual.CAMPO_ESTADO).equalsIgnoreCase(estadoAlta)) {
						// el folio es nuevo pero no esta en estado alta
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_FOLIO_INCORRECTO_382, null, nombreClase,
								nombreMetodo, null, codArea);
					}
				} else {
					if (datosFolio.get(ConfiguracionFolioVirtual.CAMPO_ESTADO).equalsIgnoreCase(estadoAlta)) {
						// el folio tiene utilizados pero no esta en estado correcto
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_FOLIO_INCORRECTO_382, null, nombreClase,
								nombreMetodo, null, codArea);
					}
				}

				if (folioEnviado > new Integer(datosFolio.get(ConfiguracionFolioVirtual.CAMPO_NO_FINAL_FOLIO))
						|| folioEnviado < new Integer(
								datosFolio.get(ConfiguracionFolioVirtual.CAMPO_NO_INICIAL_FOLIO))) {
					// los datos del folio siguiente no estan bien en bd
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_FOLIO_INCORRECTO_382, null, nombreClase,
							nombreMetodo, null, codArea);
				}
			}
		} else {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_FOLIO_384, null, nombreClase, nombreMetodo,
					null, codArea);
		}

		return objRespuesta;
	}

	/**
	 * M\u00E9todo para registrar ventas de ruta
	 * 
	 * @throws SQLException
	 */
	public OutputVenta creaVentaRuta(InputVenta objDatos) throws SQLException {
		String nombreMetodo = "creaVentaRuta";
		String nombreClase = new CurrentClassGetter().getClassName();
		listaLog = new ArrayList<LogSidra>();
		OutputVenta objRespuestaVenta = new OutputVenta();
		Respuesta objRespuesta = new Respuesta();
		Connection conn = null;
		String clientePDV = "";
		String clienteFinal = "";
		String estadoAlta = "";
		String estadoEnUso = "";
		String estadoFinalizado = "";
		String estadoIniciada = "";
		String estadoActivo = "";
		String estadoVenta = "";
		String estadoAnulado = "";
		String estadoVendido = "";
		String estadoDisponible = "";
		String tipoInvTelca = "";
		String tipoInvSidra = "";
		BigDecimal totalVenta = BigDecimal.ZERO;
		BigDecimal sumDetPago = BigDecimal.ZERO;
		BigDecimal existePDV = null;
		BigDecimal existeCF = BigDecimal.ZERO;
		BigDecimal PDVasociadoVend = null;
		UpdateSeries objUpdate = new UpdateSeries();
		List<UpdateSeries> lstUpdate = new ArrayList<UpdateSeries>();
		List<UpdateSeries> lstUpdateCant = new ArrayList<UpdateSeries>();
		List<UpdateSeries> lstUpdatePromo = new ArrayList<UpdateSeries>();
		List<BigDecimal> lstValidaciones = new ArrayList<BigDecimal>();
		List<PagoDet> lstDetallePago = new ArrayList<PagoDet>();
		List<VentaDet> articulosValidados = new ArrayList<VentaDet>();
		List<VentaDet> articulosPromocionales = new ArrayList<VentaDet>();
		List<PagoImpuesto> lstImpuestosVenta = new ArrayList<PagoImpuesto>();
		List<String> lstNombreImpuesto = new ArrayList<String>();
		BigDecimal idVenta = null;
		List<Filtro> filtros = new ArrayList<Filtro>();
		PagoImpuesto objImpuestoVenta = new PagoImpuesto();
		BigDecimal totalImpuesto = BigDecimal.ZERO;

		int cantDecimales;
		int cantDecimalesBD;
		String tipoExento = "";
		String tipoNoExento = "";
		String articuloRecarga = "";
		String tipoMonto = "";
		String tipoOferta = "";
		String idPanelRuta = "";
		String tipoPanelRuta = "";
		String tipoGestionVenta = "";
		List<Filtro> condiciones = new ArrayList<Filtro>();
		BigDecimal descuentoMontoVenta = BigDecimal.ZERO;
		SimpleDateFormat FORMATO_FECHA_GT = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA_GT);
		SimpleDateFormat FORMATO_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");
		BigDecimal montoMinimoPercepcion = BigDecimal.ZERO;
		String validaSerieFolio = "0";
		String diferencia = "";
		boolean modoOnline = true;
		String tipoPagueLleve = "";
		boolean esAnulacion = false;

		try {
			conn = getConnRegional();
			conn.setAutoCommit(false);
			BigDecimal idPais = getIdPais(conn, objDatos.getCodArea());

			estadoEnUso = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_FOLIOS, Conf.FOLIO_EN_USO,
					objDatos.getCodArea());
			estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());
			clienteFinal = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPO_CLIENTE_VENTA, Conf.CLIENTEFINAL,
					objDatos.getCodArea());
			tipoInvTelca = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPOINV, Conf.SOL_TIPOINV_TELCA,
					objDatos.getCodArea());
			tipoInvSidra = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPOINV, Conf.SOL_TIPOINV_SIDRA,
					objDatos.getCodArea());
			articuloRecarga = UtileriasJava.getConfig(conn, Conf.GRUPO_ARTICULO_CANTIDAD, Conf.ARTICULO_RECARGA,
					objDatos.getCodArea());
			tipoExento = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPO_EXENTO, Conf.TIPO_EXENTO, objDatos.getCodArea());
			tipoNoExento = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPO_EXENTO, Conf.TIPO_NO_EXENTO,
					objDatos.getCodArea());
			tipoPagueLleve = UtileriasJava.getConfig(conn, Conf.GRUPO_CONDICION_TIPOOFERTA,
					Conf.CONDICION_OFERTA_PAGUE_LLEVE, objDatos.getCodArea());

			objRespuesta = validarDatos(conn, objDatos, estadoAlta, estadoEnUso, clienteFinal, tipoInvTelca,
					articuloRecarga, tipoNoExento, tipoExento, tipoPagueLleve, idPais);

			if (objRespuesta == null) {

				estadoIniciada = UtileriasJava.getConfig(conn, Conf.GRUPO_JORNADA_ESTADOS, Conf.JORNADA_ESTADO_INICIADA,
						objDatos.getCodArea());
				estadoActivo = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_ESTADOS_PDV, Conf.ESTADO_ACTIVO,
						objDatos.getCodArea());
				estadoVenta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_VENTA, Conf.VENTA_REGISTRADO_SIDRA,
						objDatos.getCodArea());
				estadoVendido = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_VENDIDO,
						objDatos.getCodArea());
				estadoDisponible = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_INVENTARIO, Conf.INV_EST_DISPONIBLE,
						objDatos.getCodArea());
				estadoFinalizado = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_FOLIOS, Conf.FOLIO_FINALIZADO,
						objDatos.getCodArea());
				clientePDV = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPO_CLIENTE_VENTA, Conf.CLIENTE_PDV,
						objDatos.getCodArea());
				cantDecimales = new Integer(UtileriasJava.getConfig(conn, Conf.GRUPO_PARAM_VENTA, Conf.CANT_DECIMALES,
						objDatos.getCodArea()));
				cantDecimalesBD = new Integer(UtileriasJava.getConfig(conn, Conf.GRUPO_PARAM_VENTA,
						Conf.CANT_DECIMALES_BD, objDatos.getCodArea()));
				tipoOferta = UtileriasJava.getConfig(conn, Conf.GRUPO_OFERTA_COMERCIAL, Conf.TIPO_OFERTA,
						objDatos.getCodArea());
				tipoMonto = UtileriasJava.getConfig(conn, Conf.GRUPO_DESCUENTOS, Conf.TIPO_MONTO,
						objDatos.getCodArea());
				tipoGestionVenta = UtileriasJava.getConfig(conn, Conf.GRUPO_TIPO_GESTION_VENTA,
						Conf.CONDICION_GESTION_VENTA, objDatos.getCodArea());
				validaSerieFolio = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.VALIDA_SERIE,
						objDatos.getCodArea());
				diferencia = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.DIFERENCIA_HORARIO,
						objDatos.getCodArea());
				modoOnline = objDatos.getModoOnline().equals("1");
				montoMinimoPercepcion = new BigDecimal(UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_PERCEPCION,
						Conf.MONTO_MINIMO_PERCEPCION, objDatos.getCodArea()));

				lstValidaciones = OperacionVentas.validacionPrincipal(conn, estadoAlta, objDatos.getIdVendedor(),
						objDatos.getIdBodegaVendedor(), objDatos.getIdJornada(), estadoIniciada, idPais,
						objDatos.getCodArea());

				filtros.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO,
						Conf.GRUPO_IMPUESTO_PAIS));
				filtros.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,
						idPais.toString()));
				lstNombreImpuesto = UtileriasBD.getOneField(conn, Catalogo.CAMPO_NOMBRE, Catalogo.N_TABLA, filtros,
						null);

				// validando que exista Vendedor
				if (lstValidaciones.get(1).intValue() == 0) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_EXISTENCIA_VENDEDOR_361, null, nombreClase,
							nombreMetodo, null, objDatos.getCodArea());
					listaLog.add(addLog(Conf.LOG_TRANSACCION_VENTA, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
							"Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

				} else if (lstValidaciones.get(0).intValue() == 0) { // existe Jornada
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENDEDOR_INVALIDO_PARAJORNADA_92, null, nombreClase,
							nombreMetodo, null, objDatos.getCodArea());
					listaLog.add(addLog(Conf.LOG_TRANSACCION_VENTA, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
							"Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

				} else if (lstValidaciones.get(2).intValue() == 0) { // existe Bodega
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_BOD_NO_ASOCIADA_A_VEND_469, null, nombreClase,
							nombreMetodo, null, objDatos.getCodArea());
					listaLog.add(addLog(Conf.LOG_TRANSACCION_VENTA, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
							"Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

				} else {
					log.trace("paso validaciones basicas");
					// Armo objeto encabezado de venta
					Venta objVenta = new Venta();
					objVenta.setTcsccatpaisid(idPais);
					objVenta.setId_venta_movil(new BigDecimal(objDatos.getIdVentaMovil())); // cambio agregado sbarrios
																							// 25-09-17
					objVenta.setVendedor(new BigDecimal(objDatos.getIdVendedor()));
					objVenta.setTcscjornadavenid(new BigDecimal(objDatos.getIdJornada()));
					objVenta.setCod_dispositivo(objDatos.getCodDispositivo());
					log.trace("setea nit");
					if (!(objDatos.getNit() == null || "".equals(objDatos.getNit()))) {
						objVenta.setNit(objDatos.getNit());
					}
					log.trace("monto factura");
					objVenta.setMonto_factura(new BigDecimal(objDatos.getMontoFactura()));
					objVenta.setMonto_pagado(new BigDecimal(objDatos.getMontoPagado()));

					// datos ficha cliente
					log.trace("ficha cliente");
					objVenta.setNombre(objDatos.getNombre());
					objVenta.setSegundo_nombre(objDatos.getSegundoNombre());
					objVenta.setApellido(objDatos.getApellido());
					objVenta.setSegundo_apellido(objDatos.getSegundoApellido());
					objVenta.setDireccion(objDatos.getDireccion());
					objVenta.setNum_telefono(objDatos.getNumTelefono());
					objVenta.setTipo_doccliente(objDatos.getTipoDocCliente());
					objVenta.setNum_doccliente(objDatos.getNumDocCliente());

					// datos cliente factura
					log.trace("datos cliente factura");
					objVenta.setNombres_factura(objDatos.getNombresFacturacion());
					objVenta.setApellidos_factura(objDatos.getApellidosFacturacion());

					log.trace("folio");
					objVenta.setTipo_documento(objDatos.getTipoDocumento().toUpperCase());
					if (objDatos.getFolioManual().equals("1")) {
						objVenta.setIdrango_folio(new BigDecimal(objDatos.getIdRangoFolio()));
						objVenta.setNumero(objDatos.getFolio());
						if (validaSerieFolio.equals("1")) {
							objVenta.setSerie(objDatos.getSerie());
						} else {
							objVenta.setSerie(null);
						}
						objVenta.setFolio_sidra(null);
						objVenta.setSerie_sidra(null);
					} else {
						objVenta.setIdrango_folio(null);
						objVenta.setNumero(null);
						objVenta.setSerie(null);
						objVenta.setFolio_sidra(new BigDecimal(objDatos.getFolio()));
						objVenta.setSerie_sidra(objDatos.getSerie());
					}

					if (objVenta.getTipo_documento().equalsIgnoreCase("CCF")) {
						log.trace("es ccf");
						objVenta.setGiro(objDatos.getGiro());
						objVenta.setRegistrofiscal(objDatos.getRegistroFiscal());
					}

					objVenta.setCreado_por(objDatos.getUsuario());
					objVenta.setLatitud(objDatos.getLatitud());
					objVenta.setLongitud(objDatos.getLongitud());
					objVenta.setTasa_cambio(new BigDecimal(objDatos.getTasaCambio()));
					objVenta.setExento(objDatos.getExento());

					// validando fecha
					Date fechaVenta;
					Date fechaActual;
					log.trace("fecha");
					try {
						Calendar cal = Calendar.getInstance();
						fechaVenta = FORMATO_TIMESTAMP.parse(objDatos.getFecha());
						fechaActual = FORMATO_TIMESTAMP.parse(FORMATO_TIMESTAMP.format(new Date()));
						log.trace("Fecha y hora venta: " + fechaVenta);

						cal.setTime(fechaActual); // Establece fecha y hora actuales
						cal.add(Calendar.MINUTE, new Integer(diferencia + 180)); // Suma/resta la diferencia de horario
						fechaActual = FORMATO_TIMESTAMP.parse(FORMATO_TIMESTAMP.format(cal.getTime()));
						log.trace("Fecha y hora actual con diferencia de horario: " + fechaActual);

						if (fechaVenta.after(fechaActual)) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_FECHA_VENTA_INCORRECTA_476, null, nombreClase,
									nombreMetodo, null, objDatos.getCodArea());
						} else {
							objVenta.setFecha_emision(new Timestamp(fechaVenta.getTime()));
							objVenta.setFecha_pago(new Timestamp(fechaVenta.getTime()));
						}
					} catch (ParseException e) {
						log.error(e.getMessage(), e);
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_CONVERTIR_FECHA_165, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setRespuesta(objRespuesta);
						return objRespuestaVenta;
					}

					// total de la venta
					log.trace("totalVenta");
					boolean aplicaRegla1 = false;
					boolean aplicaRegla2 = false;
					boolean aplicaRegla3 = false;
					boolean errorRegla = false;
					for (int a = 0; a < objDatos.getArticulos().size(); a++) {
						totalVenta = totalVenta.add(new BigDecimal(objDatos.getArticulos().get(a).getPrecioTotal()));

						// procesos para impuesto CR
						if ("506".equals(objDatos.getCodArea())) {
							verificacionImpuestos: for (int i = 0; i < objDatos.getArticulos().get(a)
									.getImpuestosArticulo().size(); i++) {
								if (objDatos.getArticulos().get(a).getImpuestosArticulo().get(i).getNombreImpuesto()
										.equalsIgnoreCase(impuestoCR)) {
									if (new BigDecimal(
											objDatos.getArticulos().get(a).getImpuestosArticulo().get(i).getValor())
													.compareTo(BigDecimal.ZERO) == 0) {
										// primera regla, no trae impuesto cr

										aplicaRegla1 = true;
										log.trace("aplica R1");
										break;
									} else if (new BigDecimal(
											objDatos.getArticulos().get(a).getImpuestosArticulo().get(i).getValor())
													.compareTo(BigDecimal.ZERO) > 0
											&& new BigDecimal(objDatos.getArticulos().get(a).getImpuestosArticulo()
													.get(i).getValor()).compareTo(new BigDecimal(500)) < 0) {
										// segunda regla, el impuesto es calculado normal
										if (aplicaRegla3) {
											// si ya se encontr\u00F3 un impuesto CR 500 y se encuentra otro que no sea
											// 0 se reporta error
											errorRegla = true;
											break verificacionImpuestos;
										}

										aplicaRegla2 = true;
										log.trace("aplica R2");
										break;
									} else if (new BigDecimal(
											objDatos.getArticulos().get(a).getImpuestosArticulo().get(i).getValor())
													.compareTo(new BigDecimal(500)) >= 0) {
										if (aplicaRegla2 || aplicaRegla3) {
											// si ya es encontr\u00F3 un impuesto CR > 0 y < 500 u otro de 500 se
											// reporta error
											errorRegla = true;
											break verificacionImpuestos;
										}

										// tercera regla, el impuesto aplicado es de 500
										aplicaRegla3 = true;
										indexArticuloImpuestoCR500 = a;
										log.trace("aplica R3");
										break;
									}
								}
							}
						}
					}

					if (errorRegla) {
						// problema, no se ubico correctamente la regla
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_REGLA_IMPUESTO_CR_616, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setRespuesta(objRespuesta);
						return objRespuestaVenta;
					} else if (aplicaRegla3) {
						aplicaCR500 = true;
					} else if (aplicaRegla2) {
						// se calcula impuesto CR normal, aplicaCR500 y aplicaCR0 deben ser falsas
						log.trace("impuesto CR se calcula normal");
					} else if (aplicaRegla1) {
						aplicaCR0 = true;
					} else {
						// no tiene CR
						log.trace("no se encontr\u00F3 impuesto CR en ning\u00FAn art\u00EDculo");
					}

					// obteniendo datos de panel o ruta
					String[] campos = { Jornada.CAMPO_IDTIPO, Jornada.CAMPO_DESCRIPCION_TIPO };
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
							Jornada.CAMPO_TCSCJORNADAVENID, objDatos.getIdJornada()));

					Map<String, String> datosJornada = UtileriasBD.getSingleFirstData(conn,
							getParticion(Jornada.N_TABLA, Conf.PARTITION, "", objDatos.getCodArea()), campos,
							condiciones);
					idPanelRuta = datosJornada.get(Jornada.CAMPO_IDTIPO);
					tipoPanelRuta = datosJornada.get(Jornada.CAMPO_DESCRIPCION_TIPO);

					objVenta.setIdofertacampania(null);
					objVenta.setDesc_montoventa(BigDecimal.ZERO);

					// armo Detalle de forma de pago
					log.trace("detPago");
					for (DetallePago detPago : objDatos.getDetallePago()) {
						PagoDet objPago = new PagoDet();
						objPago.setFormapago(detPago.getFormaPago());
						objPago.setMonto(new BigDecimal(detPago.getMonto()));
						objPago.setCreado_por(objDatos.getUsuario());
						objPago.setBanco(detPago.getBanco());
						objPago.setMarca_tarjeta(detPago.getMarcaTarjeta());
						objPago.setNum_cheque(detPago.getNumeroCheque());
						objPago.setNo_cuenta(detPago.getCuentaCliente());
						objPago.setIdPayment(detPago.getIdPayment());
						objPago.setEstadoReembolso(detPago.getEstadoReembolso()==null? null:new BigDecimal(detPago.getEstadoReembolso()));
						

						if (detPago.getNumAutorizacion() == null || detPago.getNumAutorizacion().equals("")) {
							objPago.setNum_autorizacion(null);
						} else {
							objPago.setNum_autorizacion(new BigDecimal(detPago.getNumAutorizacion()));
						}

						if (detPago.getDigitosTarjeta() == null || detPago.getDigitosTarjeta().equals("")) {
							objPago.setDigitos_tarjeta(null);
						} else {
							objPago.setDigitos_tarjeta(new BigDecimal(detPago.getDigitosTarjeta()));
						}

						if (detPago.getFechaEmision() == null || detPago.getFechaEmision().equals("")) {
							objPago.setFecha_emision(null);
						} else {
							Date fechaCheque = FORMATO_FECHA_GT.parse(detPago.getFechaEmision());
							objPago.setFecha_emision(new Timestamp(fechaCheque.getTime()));
						}

						if (detPago.getNumeroReserva() == null || detPago.getNumeroReserva().equals("")) {
							objPago.setNum_reserva(null);
						} else {
							objPago.setNum_reserva(new BigDecimal(detPago.getNumeroReserva()));
						}

						sumDetPago = sumDetPago.add(new BigDecimal(detPago.getMonto()));
						log.trace("sumDetPago: " + sumDetPago);
						lstDetallePago.add(objPago);
					}

					objVenta.setTipo(objDatos.getTipo().toUpperCase());
					// verifico primero si es PDV
					// si mi tipo de cliente es un pdv si es obligatorio enviar el idtipo que
					// corresponde al id del punto de venta
					if (clientePDV.equalsIgnoreCase(objDatos.getTipo())) {
						if (objDatos.getIdTipo() == null || objDatos.getIdTipo().trim().equals("")) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDTIPO_NULO_430, null, nombreClase,
									nombreMetodo, null, objDatos.getCodArea());
							objRespuestaVenta.setRespuesta(objRespuesta);
							return objRespuestaVenta;
						} else {
							// Verificando que el pdv exista
							existePDV = OperacionVentas.existePDV(conn, estadoActivo, objDatos.getIdTipo(), idPais);

							if (existePDV.intValue() == 0) {
								objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_PDV_472, null, nombreClase,
										nombreMetodo, null, objDatos.getCodArea());
								objRespuestaVenta.setRespuesta(objRespuesta);
								return objRespuestaVenta;
							} else {
								// si el pdv existe verificar que este asociado al vendedor ingresado o que
								// pertenezca al dts
								PDVasociadoVend = OperacionVentas.PVDasociadoVend(conn, estadoAlta,
										objDatos.getIdTipo(), objDatos.getIdVendedor(), estadoActivo,
										objDatos.getIdJornada(), idPais, objDatos.getCodArea());
								if (existePDV.intValue() == 1) {
									pequenioContribuyente = true;
								} else if (existePDV.intValue() == 2) {
									pequenioContribuyente = false;
								}
								log.trace("pequenioContribuyente:" + pequenioContribuyente);
								if (PDVasociadoVend.intValue() == 0) {
									objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENDEDOR_INVALIDO_PARAPDV_91, null,
											nombreClase, nombreMetodo, null, objDatos.getCodArea());
									objRespuestaVenta.setRespuesta(objRespuesta);
									return objRespuestaVenta;
								}
							}
						}
					}

					// verificando si es cliente Final
					if (clienteFinal.equalsIgnoreCase(objDatos.getTipo())) {

						objVenta.setIdtipo(null);
					}

					boolean val1 = existePDV != null && existePDV.intValue() > 0 && PDVasociadoVend != null
							&& PDVasociadoVend.intValue() > 0;
					if (val1 || (existeCF.intValue() > 0 && clienteFinal.equalsIgnoreCase(objDatos.getTipo()))
							|| clienteFinal.equalsIgnoreCase(objDatos.getTipo())) {

						// si el id PDV es correcto agrego los datos
						if (objDatos.getTipo().equalsIgnoreCase(clientePDV)) {
							objVenta.setIdtipo(new BigDecimal(objDatos.getIdTipo()));
						}
						objVenta.setNombre_fiscal(objDatos.getNombreFiscal());
					}

					// validaci\u00F3n para impuesto de percepci\u00F3n
					if ("503".equals(objDatos.getCodArea())) {
						aplicaPercepcion = validaPercepcion(objDatos, montoMinimoPercepcion);
					}
					// validando detalle de venta
					if ("REGISTRADO_PARA_ANULAR".equalsIgnoreCase(objDatos.getEstado())) {
						estadoAnulado = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_VENTA, Conf.VENTA_ANULADO,
								objDatos.getCodArea());
						esAnulacion = true;
						objVenta.setEstado(estadoAnulado);

						// no se valida el detalle de art\u00EDculos, solo se registra todo con la venta
						// en estado ANULADO
						articulosValidados = detalleVentaParaAnular(conn, objDatos, lstValidaciones.get(1),
								articuloRecarga, estadoAnulado);

					} else {
						objVenta.setEstado(estadoVenta);

						// se valida el detalle normal y la venta queda en estado REGISTRADO_SIDRA
						articulosValidados = validaVentaDet(conn, objDatos, lstValidaciones.get(1), tipoExento,
								tipoNoExento, articuloRecarga, estadoAlta, estadoDisponible, new Integer(cantDecimales),
								tipoInvTelca, idPanelRuta, tipoPanelRuta, tipoOferta, tipoMonto, tipoGestionVenta,
								clienteFinal, clientePDV, modoOnline, objDatos.getFecha(), idPais);
					}

					if (hayArtFallido) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_SERIES_NO_EXISTEN_473, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosValidados));

					} else if (hayArtFallidoOferta) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_SERIES_OFERTA_INCORRECTA_475, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosValidados));

					} else if (hayArtFallidoImpuesto) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_SERIES_IMPUESTO_INCORRECTO_474, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosValidados));

					} else if (hayArtFallidoImpuestoNoExiste) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_IMPUESTO_VENTA_785, null,
								nombreClase, nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosValidados));

					} else if (hayArtFallidoCant) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ART_CANT_INCORRECTA_477, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosValidados));

					} else if (errorTipoDescuento) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_TIPO_DESCUENTO_275, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosValidados));

					} else if (errorDescuento) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_DESCUENTO_276, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosValidados));

					} else if (errorDescuentoTotal) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_DESCUENTO_TOTAL_884, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosValidados));

					} else if (errorImpuestoArt) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_IMPUESTO_ART_277, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosValidados));

					} else if (errorImpuestoTotal) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_IMPUESTO_TOTAL_278, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosValidados));

					} else if (errorTotalFactura) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_VENTA_TOTAL_279, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosValidados));
					} else if (errorPrecioTotal) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOTAL_ART_ENVIADO_VS_CALCULADO_NO_IGUAL_786, null,
								nombreClase, nombreMetodo, null, objDatos.getCodArea());
						objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosValidados));

					} else {
						int cont = 0;
						String series = "";
						// total descuento
						BigDecimal descuento = BigDecimal.ZERO;

						for (int i = 0; i < articulosValidados.size(); i++) {
							if (articulosValidados.get(i).getSerie() == null
									|| "".equals(articulosValidados.get(i).getSerie())) {
								objUpdate = new UpdateSeries();
								objUpdate.setSeries("" + articulosValidados.get(i).getCantidad());
								objUpdate.setArticulo("" + articulosValidados.get(i).getArticulo());
								objUpdate.setBodega(objDatos.getIdBodegaVendedor());
								lstUpdateCant.add(objUpdate);
							} else {
								cont++;

								// *VALIDAMOS SI EL PRODUCTO ES SIMCAR creado por jcsimon
								List<Filtro> condicion = new ArrayList<Filtro>();

								condicion.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
										Catalogo.CAMPO_GRUPO, Conf.GRUPO_SIMCARD));
								condicion.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
										Catalogo.CAMPO_NOMBRE, articulosValidados.get(i).getTipo_grupo_sidra()));
								condicion.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
										Catalogo.CAMPO_ESTADO, estadoAlta));
								condicion.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
										Catalogo.CAMPO_TC_SC_CATPAIS_ID, idPais.toString()));
								String SerieProducto = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_VALOR,
										Catalogo.N_TABLA, condicion);

								String serieCompleta = "";
								// buscar la serie completa si la venta es simcard cambio realizado por jcsimon
								// 28/09/2017
								if (SerieProducto.equalsIgnoreCase(Conf.TIPO_GRUPO_SIMCAR)) {

									String sql = "";
									sql = "SELECT SERIE_COMPLETA FROM TC_SC_INVENTARIO WHERE TCSCCATPAISID="
											+ idPais.toString() + " AND  TCSCBODEGAVIRTUALID= "
											+ articulosValidados.get(i).getTcscbodegavirtualid() + " AND ARTICULO="
											+ articulosValidados.get(i).getArticulo() + " AND SUBSTR(SERIE,1,18)= '"
											+ articulosValidados.get(i).getSerie() + "'";
									serieCompleta = UtileriasBD.executeQueryOneRecord(conn, sql);

								} else if (SerieProducto == null || ("").equals(SerieProducto)) {
									serieCompleta = (articulosValidados.get(i).getSerie() == null ? ""
											: articulosValidados.get(i).getSerie());

								}
								// FIN DEL CAMBIO

								series += "'" + serieCompleta + "',";
								/*
								 * TODO se agrega la serie asociada en caso de una terminal tenga chip asociado
								 * se debe marcar como vendido igual en la tabla de HISTORICO habr\u00E9 que
								 * registrarlo como una salida
								 */
								if (!(articulosValidados.get(i).getSerie_asociada() == null
										|| "".equals(articulosValidados.get(i).getSerie_asociada()))) {
									series += "'" + articulosValidados.get(i).getSerie_asociada() + "',";
									cont++;
								}

								if (cont == 50) {
									objUpdate = new UpdateSeries();
									objUpdate.setSeries(series.substring(0, series.length() - 1));
									objUpdate.setArticulo("" + articulosValidados.get(i).getArticulo());
									objUpdate.setBodega(objDatos.getIdBodegaVendedor());
									lstUpdate.add(objUpdate);
									cont = 0;
									series = "";

								}
							}

							if (i == (articulosValidados.size() - 1) && !series.equals("")) {
								objUpdate = new UpdateSeries();
								objUpdate.setSeries(series.substring(0, series.length() - 1));
								objUpdate.setArticulo(articulosValidados.get(i).getArticulo().toString());
								objUpdate.setBodega(objDatos.getIdBodegaVendedor());
								lstUpdate.add(objUpdate);
							}

							// AGREGANDO TOTAL DE DESCUENTOS
							BigDecimal descuentoScl = null;
							BigDecimal descuentoSIDRA = null;

							if (articulosValidados.get(i).getDescuento_scl() == null) {
								descuentoScl = BigDecimal.ZERO;
							} else {
								descuentoScl = articulosValidados.get(i).getDescuento_scl();
							}

							if (articulosValidados.get(i).getDescuento_sidra() == null) {
								descuentoSIDRA = BigDecimal.ZERO;
							} else {
								descuentoSIDRA = articulosValidados.get(i).getDescuento_sidra();
							}

							descuento = descuento.add(descuentoScl.add(descuentoSIDRA));
						}

						objVenta.setDescuentos(descuento.add(descuentoMontoVenta));

						// armar detalle impuestos generales de venta
						BigDecimal totalesImpuesto = BigDecimal.ZERO;
						for (int j = 0; j < lstNombreImpuesto.size(); j++) { // listado de impuestos
							objImpuestoVenta = new PagoImpuesto();
							totalImpuesto = BigDecimal.ZERO;
							// recorremos articulo por articulo de detalle
							for (int k = 0; k < articulosValidados.size(); k++) { // listado de art\u00EDculos
								// listado de impuestos por art\u00EDculo
								for (int l = 0; l < articulosValidados.get(k).getImpuestoArticulo().size(); l++) {
									if (lstNombreImpuesto.get(j).equalsIgnoreCase(
											articulosValidados.get(k).getImpuestoArticulo().get(l).getImpuesto())) {
										// sumando los impuestos si es igual al nombre de impuesto de la lista recorrida
										// en el primer ciclo
										totalImpuesto = totalImpuesto
												.add(articulosValidados.get(k).getImpuestoArticulo().get(l).getValor());
									}
								}

								if (k == articulosValidados.size() - 1) {
									totalesImpuesto = totalesImpuesto.add(totalImpuesto);

									objImpuestoVenta.setImpuesto(lstNombreImpuesto.get(j));
									objImpuestoVenta.setValor(totalImpuesto);
									objImpuestoVenta.setCreado_por(objDatos.getUsuario());
									lstImpuestosVenta.add(objImpuestoVenta);
								}
							}
						}

						objVenta.setImpuestos(totalesImpuesto);
					}

					log.trace("Inicia a validar promocionales");
					if (objDatos.getArticulosPromocionales() != null
							&& objDatos.getArticulosPromocionales().size() > 0) {
						// validando promocionales
						log.trace("tiene promocionales");
						articulosPromocionales = validaPromocionales(conn, objDatos, 0, lstValidaciones.get(1),
								estadoAlta, estadoDisponible, modoOnline, idPais);
						if (hayArtPromoFallido) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_ART_PROMO_NO_EXISTEN_479, null, nombreClase,
									nombreMetodo, null, objDatos.getCodArea());
							objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosPromocionales));

						} else if (hayArtFallidoOfertaPromo) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_ART_PROMO_NO_EXISTE_CAMPANIA_480, null,
									nombreClase, nombreMetodo, null, objDatos.getCodArea());
							objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosPromocionales));

						} else if (hayArtFallidoCantPromo) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_ART_PROMO_CANT_INVALIDA_481, null, nombreClase,
									nombreMetodo, null, objDatos.getCodArea());
							objRespuestaVenta.setArticulosIncorrectos(getListIncorrectos(articulosPromocionales));

						} else {
							for (int i = 0; i < articulosPromocionales.size(); i++) {
								objUpdate = new UpdateSeries();
								objUpdate.setSeries("" + articulosPromocionales.get(i).getCantidad());
								objUpdate.setArticulo("" + articulosPromocionales.get(i).getArticulo());
								objUpdate.setBodega(objDatos.getIdBodegaVendedor());
								lstUpdatePromo.add(objUpdate);
							}

							log.trace("tamanio articulos promocionales:" + lstUpdatePromo.size());
						}
					}
					log.trace("termina validaciones...");

					if (objRespuesta == null) {
						log.trace("obtiene cod_vendedor y cod_oficina");
						condiciones.clear();
						condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
								Dispositivo.CAMPO_TCSCCATPAISID, idPais.toString()));
						condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
								Dispositivo.CAMPO_CODIGO_DISPOSITIVO, objDatos.getCodDispositivo()));
						condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO,
								estadoAlta));
						String[] camposDisp = { Dispositivo.CAMPO_COD_OFICINA, Dispositivo.CAMPO_USERID };
						Map<String, String> datosSCL = UtileriasBD.getSingleFirstData(conn, Dispositivo.N_TABLA,
								camposDisp, condiciones);
						log.trace("cod vendedor:" + datosSCL.get(Dispositivo.CAMPO_USERID.toUpperCase()) + ", "
								+ datosSCL.get(Dispositivo.CAMPO_USERID));
						if (datosSCL.isEmpty() || datosSCL.get(Dispositivo.CAMPO_COD_OFICINA) == null
								|| datosSCL.get(Dispositivo.CAMPO_USERID) == null
								|| datosSCL.get(Dispositivo.CAMPO_COD_OFICINA).equals("")
								|| datosSCL.get(Dispositivo.CAMPO_USERID).equals("")) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_CODIGOS_SCL_897, null, null, null, "",
									objDatos.getCodArea());
						}
						objVenta.setCod_oficina(datosSCL.get(Dispositivo.CAMPO_COD_OFICINA));
						objVenta.setCod_vendedor(datosSCL.get(Dispositivo.CAMPO_USERID));
					}

					// si no ocurrieron inconvenientes procedo a insertar
					if (objRespuesta == null) {
						log.trace("Inicia a insertar... ");

						// se registra encabezado de venta
						idVenta = OperacionVentas.insertVenta(conn, objVenta, cantDecimalesBD, idPais);
						log.trace("ID VENTA OBTENIDO: " + idVenta);
						if (idVenta != null) {
							listaLog.add(addLog(Conf.LOG_TRANSACCION_VENTA, Conf.LOG_POST_VENTA, idVenta + "",
									Conf.LOG_TIPO_VENTA, "Se registr\u00F3 una venta nueva con el ID " + idVenta, ""));

							// se registra detalle de venta
							log.trace("registra detalle venta");
							OperacionVentas.insertaDetVenta(conn, articulosValidados, idVenta, cantDecimalesBD,
									objDatos.getCodArea(), idPais);

							listaLog.add(addLog(Conf.LOG_TRANSACCION_VENTA, Conf.LOG_POST_VENTA, idVenta + "",
									Conf.LOG_TIPO_VENTA, "Se registr\u00F3 el detalle de la venta con ID " + idVenta,
									""));

							// si la venta es para anular, no se actualiza inventario
							if (!esAnulacion) {
								// actualizando en estado vendido las series
								log.trace("registra update en inv");
								for (UpdateSeries obj : lstUpdate) {
									OperacionVentas.updateInvSerie(conn, obj.getSeries(), estadoVendido,
											estadoDisponible, idVenta, objDatos.getUsuario(), idPais);
								}

								// actualizando articulos por cantidad
								log.trace("registra update por cant");
								if (!lstUpdateCant.isEmpty()) {
									for (UpdateSeries obj : lstUpdateCant) {
										if (!obj.getArticulo().equals(articuloRecarga)) {
											OperacionVentas.updateInvCant(conn, obj.getBodega(), estadoDisponible,
													obj.getArticulo(), new BigDecimal(obj.getSeries()).intValue(),
													objDatos.getUsuario(), idPais);

											OperacionVentas.insertarInv(conn, obj.getArticulo(), estadoVendido,
													estadoDisponible, obj.getBodega(), tipoInvTelca, obj.getSeries(),
													idVenta, idPais);
										}
									}
								}
							} else {
								log.trace("es venta anulada, no se actualiza inventario telca");
							}

							// registrandoPromocionales
							if (articulosPromocionales != null && !articulosPromocionales.isEmpty()) {
								log.trace("registra detalleventa promocionales");
								OperacionVentas.insertaDetVenta(conn, articulosPromocionales, idVenta, cantDecimalesBD,
										objDatos.getCodArea(), idPais);

								// si la venta es para anular, no se actualiza inventario
								if (!esAnulacion) {
									if (!lstUpdatePromo.isEmpty()) {
										for (UpdateSeries obj : lstUpdatePromo) {
											OperacionVentas.updateInvCant(conn, obj.getBodega(), estadoDisponible,
													obj.getArticulo(), new BigDecimal(obj.getSeries()).intValue(),
													objDatos.getUsuario(), idPais);
											OperacionVentas.insertarInv(conn, obj.getArticulo(), estadoVendido,
													estadoDisponible, obj.getBodega(), tipoInvSidra, obj.getSeries(),
													idVenta, idPais);

											OperacionVentas.updateInvCantJornada(conn,
													objVenta.getTcscjornadavenid() + "", obj.getArticulo(),
													new BigDecimal(obj.getSeries()).intValue(), objDatos.getUsuario(),
													tipoInvSidra, idPais);
										}
									}
								} else {
									log.trace("es venta anulada, no se actualiza inventario sidra");
								}
							}
							log.trace("inicia a insertar detalles globales");
							OperacionVentas.insertaDetImpuestoGlobal(conn, lstImpuestosVenta, idVenta, cantDecimalesBD);
							OperacionVentas.insertaDetPago(conn, lstDetallePago, idVenta, cantDecimalesBD);
							OperacionVentas.insertaDetImpuestoExento(conn, objDatos.getImpuestosExento(), idVenta,
									objDatos.getUsuario());

							// se actualiza folio
							if (objDatos.getFolioManual() != null && objDatos.getFolioManual().equals("1")) {
								OperacionVentas.updateFolios(conn, objDatos.getIdRangoFolio(),
										new Integer(objDatos.getFolio()), estadoFinalizado, estadoEnUso,
										objDatos.getUsuario(), idPais);
							}
							// crear visita

							if (clientePDV.equalsIgnoreCase(objDatos.getTipo())) {
								Visita objVisita = new Visita();
								objVisita.setTcscpuntoventaid(new BigDecimal(objDatos.getIdTipo()));
								objVisita.setTcscjornadavendid(new BigDecimal(objDatos.getIdJornada()));
								objVisita.setTcsccatpaisid(idPais);
								objVisita.setFecha(
										new java.sql.Timestamp(FORMATO_TIMESTAMP.parse(objDatos.getFecha()).getTime()));
								objVisita.setVendedor(new BigDecimal(objDatos.getIdVendedor()));
								objVisita.setLatitud(objDatos.getLatitud());
								objVisita.setLongitud(objDatos.getLongitud());
								objVisita.setTcscventaid(idVenta);
								objVisita.setGestion(Gestion.VENTA.toString());
								objVisita.setCreado_por(objDatos.getUsuario());

								OperacionVisita.insertVisita(conn, objVisita);
							}

							// objDatos.getidt

							conn.commit();

							objRespuestaVenta.setIdVenta(idVenta + "");
							objRespuesta = getMensaje(Conf_Mensajes.OK_VENTA18, null, null, null, "",
									objDatos.getCodArea());

						}
					} else {
						listaLog.add(addLog(Conf.LOG_TRANSACCION_VENTA, Conf.LOG_POST_VENTA, "0", Conf.LOG_TIPO_VENTA,
								"Ocurri\u00F3 un inconveniente al registrar la venta.", objRespuesta.getDescripcion()));
					}
				}
			} else {
				// Si es por error de folio repetido se setean valores de respuesta //agregado
				// 03/04/2017
				if (objRespuesta.getCodResultado().equals(Conf_Mensajes.MSJ_ERROR_VENTA_FOLIO_280 + "")
						|| objRespuesta.getCodResultado().equals(Conf_Mensajes.MSJ_VENTA_SINCRONIZADA_633 + "")) {
					objRespuestaVenta.setIdVenta(objRespuesta.getOrigen());
					objRespuesta.setOrigen(null);
				}

				listaLog.add(addLog(Conf.LOG_TRANSACCION_VENTA, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
						"Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			if (e.getErrorCode() == 1 && e.getMessage().toUpperCase().contains("UNIQUE_VENTA_MOV")) {
				BigDecimal ventaIDRegistrada = null;
				try {
					ventaIDRegistrada = OperacionVentas.validaSincVenta(conn, objDatos.getFecha(),
							objDatos.getIdVentaMovil(), objDatos.getCodDispositivo(), objDatos.getIdJornada(),
							objDatos.getCodArea());
					if (ventaIDRegistrada != null) {
						objRespuestaVenta.setIdVenta(ventaIDRegistrada.toString());
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENTA_SINCRONIZADA_633, e.getMessage(), nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
					} else {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENTA_NO_REGISTRADA_695, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
					}
				} catch (SQLException e1) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENTA_NO_REGISTRADA_695, e1.getMessage(), nombreClase,
							nombreMetodo, null, objDatos.getCodArea());
				}
			} else {
				objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, "",
						objDatos.getCodArea());
			}
			conn.rollback();
			log.trace("Rollback");

			listaLog.add(addLog(Conf.LOG_TRANSACCION_VENTA, Conf.LOG_POST_VENTA, "0", Conf.LOG_TIPO_NINGUNO,
					"Ocurri\u00F3 un inconveniente al registrar la venta.", e.getMessage()));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, "",
					objDatos.getCodArea());
			conn.rollback();
			log.trace("Rollback");

			listaLog.add(addLog(Conf.LOG_TRANSACCION_VENTA, Conf.LOG_POST_VENTA, "0", Conf.LOG_TIPO_NINGUNO,
					"Ocurri\u00F3 un inconveniente al registrar la venta.", e.getMessage()));

		} finally {
			objRespuestaVenta.setRespuesta(objRespuesta);
			conn.setAutoCommit(true);

			DbUtils.closeQuietly(conn);
			log.trace(objRespuestaVenta.getRespuesta().getDescripcion());

			UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
		}

		return objRespuestaVenta;
	}

	private List<ArticuloIncorrecto> getListIncorrectos(List<VentaDet> articulosValidados) {
		List<ArticuloIncorrecto> lstIncorrectos = new ArrayList<ArticuloIncorrecto>();

		if (articulosValidados != null && !articulosValidados.isEmpty()) {
			for (VentaDet obj : articulosValidados) {
				ArticuloIncorrecto objIncorrecto = new ArticuloIncorrecto();
				objIncorrecto.setArticulo("" + obj.getArticulo());
				objIncorrecto.setSerie(obj.getSerie());
				objIncorrecto.setTipoInv(obj.getTipo_inv());
				lstIncorrectos.add(objIncorrecto);
			}
		}
		return lstIncorrectos;
	}

	public String getQueryTCambio() {
		String sql = "SELECT V.TCSCVENTAID, SCL.CAMBIO AS TASA_CAMBIO, "
				+ " (V.MONTO_FACTURA)MONTO_FACTURA, SCL.FEC_DESDE FROM TC_SC_VENTA V INNER JOIN GE_CONVERSION"
				+ Conf.DBLINK_SCL
				+ " scl on TO_date(v.FECHA_EMISION,'DD/MM/YYYY')= TO_DATE (scl.FEC_DESDE, 'DD/MM/YYYY') "
				+ "WHERE TCSCVENTAID=?" + " AND SCL.COD_MONEDA='002' AND TCSCCATPAISID= ?";
		return sql;
	}

	public List<InputMapas> getDatosVentaMapa(Connection conn, BigDecimal idVenta, String tipoInvTelca,
			BigDecimal idPais, String codArea) throws SQLException {
		List<InputMapas> lstGruposVenta = new ArrayList<InputMapas>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		BigDecimal tipoCambio = BigDecimal.ZERO;
		BigDecimal montoFinal = BigDecimal.ZERO;

		if (idPais.toString() != "3") {
			// ---obtenemos el tipo de cambio ----
			String sql = getQueryTCambio();

			log.debug("Qry para recuperar el tipo de cambio: " + sql);

			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setBigDecimal(1, idVenta);
				pstmt.setBigDecimal(2, idPais);
				rst = pstmt.executeQuery();

				if (!rst.next()) {
					// no se encontro el tipo de cambio
					log.trace("Tipo de cambio no encontrados.");
					tipoCambio = new BigDecimal("1.00");

				} else {
					tipoCambio = new BigDecimal(rst.getString("TASA_CAMBIO"));
					log.trace("MONTO:" + rst.getBigDecimal("MONTO_FACTURA"));
					log.trace("MONTO:" + UtileriasJava.redondear(rst.getBigDecimal("MONTO_FACTURA"), 4));
					montoFinal = UtileriasJava.redondear(rst.getBigDecimal("MONTO_FACTURA"), 4);

				}
			} finally {
				DbUtils.closeQuietly(rst);
				DbUtils.closeQuietly(pstmt);
			}
		}

		String query = "SELECT"
				+ "(SELECT VALOR  FROM TC_SC_CONFIGURACION WHERE GRUPO='PRODUCTOS_DE_ENVIO_SOCKET' AND NOMBRE=TIPO_GRUPO_SIDRA AND TCSCCATPAISID=?) TIPO_GRUPO_SIDRA,"
				+ " TECNOLOGIA, SUM(CANTIDAD) CANTIDAD, (SUM(PRECIO_FINAL)/?)  MONTO "
				+ "FROM TC_SC_VENTA_DET WHERE TCSCVENTAID = ? AND TIPO_INV =? GROUP BY TIPO_GRUPO_SIDRA, TECNOLOGIA";

		log.debug("Qry datos detalles venta mapa: " + query);
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setBigDecimal(1, idPais);
			pstmt.setBigDecimal(2, tipoCambio);
			pstmt.setBigDecimal(3, idVenta);
			pstmt.setString(4, tipoInvTelca);
			rst = pstmt.executeQuery();

			if (rst.next()) {
				do {
					InputMapas grupoVenta = new InputMapas();
					grupoVenta.setNombre_producto(rst.getString("TIPO_GRUPO_SIDRA"));
					grupoVenta.setTecnologia(UtileriasJava.getValue(rst.getString("TECNOLOGIA")));
					grupoVenta.setCantidad(rst.getBigDecimal("CANTIDAD"));
					grupoVenta.setMonto(UtileriasJava.redondear(rst.getBigDecimal("MONTO"), 4));
					lstGruposVenta.add(grupoVenta);
				} while (rst.next());
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstmt);
		}
		if (lstGruposVenta.size() > 0) {
			// datos del dts
			query = "WITH " + "JORNADA AS (SELECT TCSCDTSID, IDTIPO, DESCRIPCION_TIPO FROM "
					+ getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea)
					+ " WHERE TCSCJORNADAVENID = (SELECT TCSCJORNADAVENID FROM "
					+ getParticion(Venta.N_TABLA, Conf.PARTITION, "", codArea)
					+ " WHERE TCSCVENTAID =? AND TCSCCATPAISID = ?)), "
					+ "DTS AS (SELECT NOMBRES, CANAL FROM TC_SC_DTS "
					+ " WHERE TCSCDTSID = (SELECT TCSCDTSID FROM JORNADA) AND TCSCCATPAISID =?), "
					+ "TIPO AS (SELECT CASE " + "WHEN UPPER(DESCRIPCION_TIPO) = 'PANEL' THEN "
					+ "(SELECT NOMBRE FROM TC_SC_PANEL WHERE TCSCPANELID = (SELECT IDTIPO FROM JORNADA) "
					+ "AND TCSCCATPAISID = ?) " + "WHEN UPPER(DESCRIPCION_TIPO) = 'RUTA' THEN"
					+ "(SELECT NOMBRE FROM TC_SC_RUTA WHERE TCSCRUTAID = (SELECT IDTIPO FROM JORNADA) "
					+ "AND TCSCCATPAISID =?) " + "END AS NOMBRETIPO FROM JORNADA) "
					+ "SELECT * FROM DTS, JORNADA, TIPO";

			log.debug("Qry datos encabezado venta mapa: " + query);
			try {
				pstmt = conn.prepareStatement(query);
				pstmt.setBigDecimal(1, idVenta);
				pstmt.setBigDecimal(2, idPais);
				pstmt.setBigDecimal(3, idPais);
				pstmt.setBigDecimal(4, idPais);
				pstmt.setBigDecimal(5, idPais);
				rst = pstmt.executeQuery();

				if (rst.next()) {
					do {
						lstGruposVenta.get(0).setId_distribuidor(rst.getBigDecimal("TCSCDTSID"));
						lstGruposVenta.get(0).setNombre_distribuidor(rst.getString("NOMBRES"));
						lstGruposVenta.get(0).setCanal(rst.getString("CANAL"));
						lstGruposVenta.get(0).setId_panelruta(rst.getBigDecimal("IDTIPO"));
						lstGruposVenta.get(0).setNombre_panelruta(rst.getString("NOMBRETIPO"));
						lstGruposVenta.get(0).setTipo_panelruta(rst.getString("DESCRIPCION_TIPO"));
						lstGruposVenta.get(0).setMonto(montoFinal);
					} while (rst.next());
				}
			} finally {
				DbUtils.closeQuietly(rst);
				DbUtils.closeQuietly(pstmt);
			}
		}

		return lstGruposVenta;
	}

	/**
	 * M\u00E9todo para validar todos los art\u00EDculos que se vendieron en la
	 * venta ingresada
	 * 
	 * @param conn
	 * @param objDatos
	 * @param bodegaRutaPanel
	 * @param tipoExento
	 * @param articuloRecarga
	 * @param estadoAlta
	 * @param estadoDisponible
	 * @param cantDecimales
	 * @param tipoInvTelca
	 * @param idPanelRuta
	 * @param tipoPanelRuta
	 * @param tipoOferta
	 * @param clienteFinal
	 * @param clientePDV
	 * @param modoOnline
	 * @param fechaVenta
	 * @return
	 * @throws SQLException
	 * @throws CloneNotSupportedException
	 */
	public List<VentaDet> validaVentaDet(Connection conn, InputVenta objDatos, BigDecimal bodegaRutaPanel,
			String tipoExento, String tipoNoExento, String articuloRecarga, String estadoAlta, String estadoDisponible,
			Integer cantDecimales, String tipoInvTelca, String idPanelRuta, String tipoPanelRuta, String tipoOferta,
			String tipoMonto, String tipoGestionVenta, String clienteFinal, String clientePDV, boolean modoOnline,
			String fechaVenta, BigDecimal idPais) throws SQLException, CloneNotSupportedException {
		Inventario objInv = new Inventario();
		String seriesInvalidas = "";

		List<VentaDet> lstArticulosFallidos = new ArrayList<VentaDet>();
		List<VentaDet> lstArticulosFallidosOf = new ArrayList<VentaDet>();
		List<VentaDet> lstArticulosFallidosImp = new ArrayList<VentaDet>();
		List<VentaDet> lstArticulosFallidosCant = new ArrayList<VentaDet>();
		List<VentaDet> lstArticulos = new ArrayList<VentaDet>();
		List<VentaDet> lstErrores = new ArrayList<VentaDet>();
		VentaDet objArt;

		BigDecimal impuestoArticulo;
		PagoImpuesto objImpuesto;
		List<PagoImpuesto> lstImpuesto;

		PagoImpuesto impuestoArt = null;
		List<PagoImpuesto> listImpuestosArt;
		List<InputInventarioCampaniaCondiciones> datosCampaniaCondiciones;

		String tipoOfertaArticulo = "";
		String tipoOfertaTecnologia = "";
		String tipoCondicionArticulo = "";
		String estadoAnulado = "";
		String estadoAnuladoSCL = "";
		String tipoProducto = "";
		String rangoInicial = "";
		String sql = "";

		BigDecimal cantidad;
		BigDecimal descuentoUnitarioSidra;

		BigDecimal descuentoSCL;

		String nombreImpuesto = "";
		BigDecimal valorImpuesto;
		BigDecimal valorTotalImpuestosPais = BigDecimal.ZERO;
		BigDecimal idOfertaCampania = null;
		boolean esRecarga = false;
		boolean esBono = false;
		boolean exentoImpuesto = false;
		BigDecimal totalCalc;
		BigDecimal valorIva = BigDecimal.valueOf(0.13);
		BigDecimal valorCes = BigDecimal.valueOf(0.05);
		// se obtienen los datos de los impuestos configurados en el sistema
		InputConsultaImpuestos consultaImpuestos = new InputConsultaImpuestos();
		consultaImpuestos.setCodArea(objDatos.getCodArea());
		OutputImpuestos datosImpuestosBD = OperacionImpuestos.doGet(conn, consultaImpuestos, idPais);

		// obteniendo par\u00E9metros
		tipoOfertaArticulo = UtileriasJava.getConfig(conn, Conf.GRUPO_CONDICION_TIPOOFERTA,
				Conf.CONDICION_OFERTA_ARTICULO, objDatos.getCodArea());
		tipoOfertaTecnologia = UtileriasJava.getConfig(conn, Conf.GRUPO_CONDICION_TIPOOFERTA,
				Conf.CONDICION_OFERTA_TECNOLOGIA, objDatos.getCodArea());
		tipoCondicionArticulo = UtileriasJava.getConfig(conn, Conf.GRUPO_CONDICION_TIPO, Conf.TIPO_CONDICION_ARTICULO,
				objDatos.getCodArea());
		estadoAnulado = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_VENTA, Conf.VENTA_ANULADO,
				objDatos.getCodArea());
		estadoAnuladoSCL = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_VENTA, Conf.VENTA_ANULADO_SCL,
				objDatos.getCodArea());
		// Se obtienen todos los datos de campañas y ofertas
		datosCampaniaCondiciones = OperacionInventarioMovil.getCampaniaCondiciones(conn, idPanelRuta, tipoPanelRuta,
				estadoAlta, tipoCondicionArticulo, tipoOferta, tipoOfertaArticulo, true, modoOnline,
				objDatos.getTipo().toUpperCase(), tipoOfertaTecnologia, objDatos.getCodArea(), idPais);

		for (int i = 0; i < datosImpuestosBD.getImpuestos().size(); i++) {
			valorImpuesto = new BigDecimal(datosImpuestosBD.getImpuestos().get(i).getPorcentaje())
					.divide(new BigDecimal(100), mc);
			valorTotalImpuestosPais = valorTotalImpuestosPais.add(valorImpuesto);
		}
		valorTotalImpuestosPais = valorTotalImpuestosPais.add(BigDecimal.ONE);
		log.trace("valorImpuesto global: " + valorTotalImpuestosPais);

		// se obtienen los datos del PDV si aplica
		Map<String, String> datosPDV = new HashMap<String, String>();
		if (objDatos.getTipo().equals(clientePDV)) {
			String[] campos = { PuntoVenta.CAMPO_CATEGORIA, PuntoVenta.CAMPO_TCSCZONACOMERCIALID };
			List<Filtro> condiciones = new ArrayList<Filtro>();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCPUNTOVENTAID,
					objDatos.getIdTipo()));
			datosPDV = UtileriasBD.getSingleFirstData(conn, PuntoVenta.N_TABLA, campos, condiciones);
		}

		for (int a = 0; a < objDatos.getArticulos().size(); a++) {
			esRecarga = false;
			esBono = false;
			// precio unitario con impuestos y descuento scl
			BigDecimal precioUnitario = new BigDecimal(objDatos.getArticulos().get(a).getPrecio());
			log.trace("precio unitario base pais (con/sin impuestos y descuento): " + precioUnitario); // con impuesto
																										// en SV y sin
																										// impuestos en
																										// PA, NI y CR

			// *VALIDAMOS SI EL PRODUCTO ES SIMCARD creado por jcsimon
			List<Filtro> condicion = new ArrayList<Filtro>();
			condicion.add(
					UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, Conf.GRUPO_SIMCARD));
			condicion.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE,
					objDatos.getArticulos().get(a).getTipoGrupoSidra()));
			condicion.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
			condicion.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,
					idPais.toString()));
			tipoProducto = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_VALOR, Catalogo.N_TABLA, condicion);
			// FIN DEL CAMBIO

			// se verifica si es venta por rango
			if ("0".equals(objDatos.getArticulos().get(a).getRango())) {
				impuestoArticulo = BigDecimal.ZERO;
				descuentoSCL = BigDecimal.ZERO;
				descuentoUnitarioSidra = BigDecimal.ZERO;
				lstImpuesto = new ArrayList<PagoImpuesto>();

				if (objDatos.getArticulos().get(a).getArticulo().equals(articuloRecarga)) {
					log.trace("es recarga");
					esRecarga = true;
				} else if (objDatos.getArticulos().get(a).getTipoGrupoSidra().equalsIgnoreCase(Conf.TIPO_GRUPO_BONO)) {
					log.trace("es bono");
					esBono = true;
					esRecarga = true;
				}

				if (tipoProducto.equalsIgnoreCase(Conf.TIPO_GRUPO_SIMCAR)) {
					// buscar la serie completa si la venta es simcard cambio realizado por jcsimon
					// 28/09/2017
					sql = "SELECT SERIE FROM TC_SC_INVENTARIO WHERE TCSCCATPAISID=" + idPais.toString()
							+ " AND  TCSCBODEGAVIRTUALID= " + objDatos.getIdBodegaVendedor() + " AND ARTICULO="
							+ objDatos.getArticulos().get(a).getArticulo() + " AND SUBSTR(SERIE,1,18)= '"
							+ objDatos.getArticulos().get(a).getSerieInicial() + "'";
					rangoInicial = UtileriasBD.executeQueryOneRecord(conn, sql);

				} else if (tipoProducto == null || "".equals(tipoProducto)) {
					rangoInicial = (objDatos.getArticulos().get(a).getSerieInicial() == null ? "NULL"
							: objDatos.getArticulos().get(a).getSerieInicial());

				}

				log.trace("consulta inv");
				objInv = OperacionVentas.getArticulo(conn, estadoDisponible, rangoInicial,
						objDatos.getIdBodegaVendedor(), // objDatos.getArticulos().get(a).getSerieInicial()
						objDatos.getArticulos().get(a).getArticulo(), tipoInvTelca, esRecarga, objDatos.getIdJornada(),
						objDatos.getCodArea(), idPais);

				if (objInv == null) {
					hayArtFallido = true;
					objArt = new VentaDet();
					objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
					objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
					objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
					lstArticulosFallidos.add(objArt);
					return lstArticulosFallidos;
				} else {
					log.trace("obtuvo valor inv");
					objArt = new VentaDet();

					if (esBono) {
						objArt.setArticulo(new BigDecimal(articuloRecarga));
						objArt.setObservaciones(objInv.getDescripcion());
					} else {
						objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
						objArt.setObservaciones(null);
					}

					if (objDatos.getArticulos().get(a).getSeriado().equals("1")) {
						objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
						log.trace("es seriado, serie: " + objArt.getSerie());
						objArt.setSerie_asociada(objDatos.getArticulos().get(a).getSerieAsociada());

						// se valida la serie contra inventario y ventas realizadas
						int existeSerieVendida = OperacionVentas.validaSerieIndividual(conn, rangoInicial,
								objDatos.getIdBodegaVendedor(), objDatos.getArticulos().get(a).getArticulo(),
								estadoAnulado, estadoAnuladoSCL, objDatos.getCodArea());

						if (existeSerieVendida > 0) {
							hayArtFallido = true;
							objArt = new VentaDet();
							objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
							objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
							objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
							lstArticulosFallidos.add(objArt);
							return lstArticulosFallidosCant;
						}
					} else if (objDatos.getArticulos().get(a).getSeriado().equals("0")) { // para art\u00EDculos no
																							// seriados
						log.trace("es no seriado, art\u00EDculo: " + objDatos.getArticulos().get(a).getArticulo());
						objArt.setSerie(null);
						objArt.setSerie_asociada(null);

						if (!objDatos.getArticulos().get(a).getArticulo().equals(articuloRecarga) && objInv
								.getCantidad()
								.intValue() < new BigDecimal(objDatos.getArticulos().get(a).getArticulo()).intValue()) {
							hayArtFallidoCant = true;
							objArt = new VentaDet();
							objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
							objArt.setSerie(null);
							objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
							lstArticulosFallidosCant.add(objArt);
							return lstArticulosFallidosCant;
						}
					}

					cantidad = new BigDecimal(objDatos.getArticulos().get(a).getCantidad());
					objArt.setCantidad(cantidad);
					objArt.setCreado_por(objDatos.getUsuario());
					objArt.setEstado(estadoAlta);
					objArt.setTipo_inv(objInv.getTipo_inv());
					objArt.setTipo_grupo_sidra(objInv.getTipo_grupo_sidra());
					objArt.setTecnologia(objInv.getTecnologia());
					objArt.setGestion(objDatos.getArticulos().get(a).getGestion());
					objArt.setBodega_panel_ruta(bodegaRutaPanel);
					objArt.setTcscbodegavirtualid(new BigDecimal(objDatos.getIdBodegaVendedor()));

					if (!(objDatos.getArticulos().get(a).getModalidad() == null
							|| "".equals(objDatos.getArticulos().get(a).getModalidad()))) {
						objArt.setModalidad((objDatos.getArticulos().get(a).getModalidad()));
					}

					// impuesto total
					if (objDatos.getArticulos().get(a).getImpuesto() == null
							|| "".equals(objDatos.getArticulos().get(a).getImpuesto())) {
						objArt.setImpuesto(BigDecimal.ZERO);
					} else {
						objArt.setImpuesto(new BigDecimal(objDatos.getArticulos().get(a).getImpuesto()));
					}
					if (!(objDatos.getArticulos().get(a).getNumTelefono() == null
							|| "".equals(objDatos.getArticulos().get(a).getNumTelefono()))) {
						objArt.setNum_telefono(new BigDecimal(objDatos.getArticulos().get(a).getNumTelefono()));
					}

					// descuento SCL
					if (!(objDatos.getArticulos().get(a).getDescuentoSCL() == null
							|| "".equals(objDatos.getArticulos().get(a).getDescuentoSCL()))) {
						descuentoSCL = new BigDecimal(objDatos.getArticulos().get(a).getDescuentoSCL());
						objArt.setDescuento_scl(descuentoSCL);
					} else {
						objArt.setDescuento_scl(BigDecimal.ZERO);
					}
					log.trace("descuentoSCL: " + descuentoSCL);

					// descuentos Sidra //17-12-16 agregados multiples descuentos por articulo
					// se cambio la variable tipogestionventa por el valor que trae el servicio para
					// manejar multiples gestiones
					if (new BigDecimal(objDatos.getArticulos().get(a).getDescuentoSidra())
							.compareTo(BigDecimal.ZERO) > 0) {
						lstArticulosFallidosOf = validarOfertas(conn, objDatos.getArticulos().get(a).getArticulo(),
								objDatos.getArticulos().get(a).getSerie(), idPanelRuta, tipoPanelRuta, estadoAlta,
								tipoOferta, objDatos.getArticulos().get(a).getGestion(), modoOnline,
								objDatos.getArticulos().get(a).getDetalleDescuentosSidra(),
								objDatos.getArticulos().get(a).getDescuentoSidra(), cantDecimales, objDatos.getTipo(),
								objDatos.getIdTipo(), objDatos.getUsuario(), esRecarga, cantidad,
								objInv.getTecnologia(), objInv.getTipo_inv(), objDatos.getCodArea(), idPais);
						if (hayArtFallidoOferta || errorDescuentoTotal) {
							return lstArticulosFallidosOf;
						} else {
							descuentoUnitarioSidra = lstArticulosFallidosOf.get(0).getDescuento_sidra();
							objArt.setDescuento_sidra(descuentoUnitarioSidra);
							objArt.setDescuentosArticulo(lstArticulosFallidosOf.get(0).getDescuentosArticulo());
						}

					} else {
						objArt.setDescuento_sidra(BigDecimal.ZERO);
					}
					log.trace("descuentoUnitarioSidraBD: " + descuentoUnitarioSidra);

					// impuestos por art\u00EDculo
					if (!(objDatos.getArticulos().get(a).getImpuestosArticulo() == null
							|| objDatos.getArticulos().get(a).getImpuestosArticulo().isEmpty())) {
						for (int b = 0; b < objDatos.getArticulos().get(a).getImpuestosArticulo().size(); b++) {
							// se verifica que el impuesto enviado coincida con los registrados en BD
							boolean existeImpuesto = false;
							for (int i = 0; i < datosImpuestosBD.getImpuestos().size(); i++) {
								if (datosImpuestosBD.getImpuestos().get(i).getNombre().equalsIgnoreCase(objDatos
										.getArticulos().get(a).getImpuestosArticulo().get(b).getNombreImpuesto())) {
									existeImpuesto = true;
									break;
								}
							}
							if (!existeImpuesto) {
								// si el impuesto no existe se retorna mensaje de error
								hayArtFallidoImpuestoNoExiste = true;
								objArt = new VentaDet();
								objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
								objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
								objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
								lstArticulosFallidosImp.add(objArt);
								return lstArticulosFallidosImp;
							}

							objImpuesto = new PagoImpuesto();
							objImpuesto.setCreado_por(objDatos.getUsuario());
							objImpuesto.setImpuesto(
									objDatos.getArticulos().get(a).getImpuestosArticulo().get(b).getNombreImpuesto());
							objImpuesto.setValor(new BigDecimal(
									objDatos.getArticulos().get(a).getImpuestosArticulo().get(b).getValor()));
							lstImpuesto.add(objImpuesto);

							impuestoArticulo = impuestoArticulo.add(new BigDecimal(
									objDatos.getArticulos().get(a).getImpuestosArticulo().get(b).getValor()));
						}
					}
					log.trace("impuestoArticulo: " + impuestoArticulo);

					log.trace("impuestoArticulo calculado=recibido? : "
							+ UtileriasJava.redondear(impuestoArticulo, cantDecimales) + " = "
							+ UtileriasJava.redondear(new BigDecimal(objDatos.getArticulos().get(a).getImpuesto()),
									cantDecimales));
					if (Country.PA.getCodArea().toString().compareTo(objDatos.getCodArea()) != 0
							&& UtileriasJava.redondear(impuestoArticulo, cantDecimales)
									.compareTo(UtileriasJava.redondear(
											new BigDecimal(objDatos.getArticulos().get(a).getImpuesto()),
											cantDecimales)) != 0) {
						hayArtFallidoImpuesto = true;
						objArt = new VentaDet();
						objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
						objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
						objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
						lstArticulosFallidosImp.add(objArt);
					} else {
						objArt.setImpuestoArticulo(lstImpuesto);
					}

					if (esBono) {
						objArt.setPrecio_unitario(precioUnitario.divide(cantidad, mc));
						objArt.setPrecio_total(precioUnitario);
					} else {
						objArt.setPrecio_unitario(precioUnitario);
						objArt.setPrecio_total(precioUnitario.multiply(cantidad));
					}
					log.trace("precioUnitario: " + objArt.getPrecio_unitario());

					if (esRecarga) {
						objArt.setPrecio_final(new BigDecimal(objDatos.getArticulos().get(a).getPrecioTotal()));
					} else {
						objArt.setPrecio_final(new BigDecimal(objDatos.getArticulos().get(a).getPrecioTotal()));
					}
					log.trace("precioFinal: " + objArt.getPrecio_final());

					lstArticulos.add(objArt);
				}

			} else if ("1".equals(objDatos.getArticulos().get(a).getRango()) && objDatos.getArticulos().get(a)
					.getTipoGrupoSidra().equalsIgnoreCase(Conf.TIPO_GRUPO_TARJETASRASCA)) { // cuando son series por
																							// tarjetas rasca
				seriesInvalidas = OperacionVentas.validarSeriesRasca(conn,
						objDatos.getArticulos().get(a).getSerieInicial(),
						objDatos.getArticulos().get(a).getSerieFinal(),
						new BigDecimal(objDatos.getArticulos().get(a).getArticulo()),
						new BigDecimal(objDatos.getIdBodegaVendedor()), objDatos.getCodArea());

				if (seriesInvalidas.equalsIgnoreCase("OK")) {
					objArt = new VentaDet();
					lstImpuesto = new ArrayList<PagoImpuesto>();
					listImpuestosArt = new ArrayList<PagoImpuesto>();
					BigDecimal precioConImpuesto;
					BigDecimal precioFinal;
					BigDecimal subtotal;
					BigDecimal totalDescuento = BigDecimal.ZERO;
					BigDecimal totalImpuestoIndividual = BigDecimal.ZERO;
					BigDecimal valorCES = BigDecimal.ZERO; // v\u00E9lido unicamente para SV
					descuentoSCL = BigDecimal.ZERO;
					BigDecimal descuentoPais = BigDecimal.ZERO;
					BigDecimal totalDescuentoPais = BigDecimal.ZERO;
					BigDecimal descuentoSidraTotal = BigDecimal.ZERO;
					boolean aplicaImpuestoCR = false;
					int indexImpuestoCR = 0;
					boolean aplicaImpuestoCRGlobal = false;

					String serieInicial = objDatos.getArticulos().get(a).getSerieInicial();
					objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
					cantidad = new BigDecimal(objDatos.getArticulos().get(a).getCantidad());
					log.trace("cantidad: " + cantidad);
					objArt.setCantidad(BigDecimal.ONE);
					objArt.setCreado_por(objDatos.getUsuario());
					objArt.setEstado(estadoAlta);
					objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
					objArt.setTipo_grupo_sidra(objDatos.getArticulos().get(a).getTipoGrupoSidra());
					objArt.setSerie_asociada(objDatos.getArticulos().get(a).getSerieAsociada());
					objArt.setGestion(objDatos.getArticulos().get(a).getGestion());
					objArt.setBodega_panel_ruta(bodegaRutaPanel);
					objArt.setTcscbodegavirtualid(new BigDecimal(objDatos.getIdBodegaVendedor()));

					if (!(objDatos.getArticulos().get(a).getModalidad() == null
							|| "".equals(objDatos.getArticulos().get(a).getModalidad()))) {
						objArt.setModalidad((objDatos.getArticulos().get(a).getModalidad()));
					}
					if (!(objDatos.getArticulos().get(a).getNumTelefono() == null
							|| "".equals(objDatos.getArticulos().get(a).getNumTelefono()))) {
						objArt.setNum_telefono(new BigDecimal(objDatos.getArticulos().get(a).getNumTelefono()));
					}

					// se arma el listado de impuestos del art\u00EDculo
					for (int b = 0; b < objDatos.getArticulos().get(a).getImpuestosArticulo().size(); b++) {
						objImpuesto = new PagoImpuesto();
						objImpuesto.setCreado_por(objDatos.getUsuario());
						objImpuesto.setImpuesto(
								objDatos.getArticulos().get(a).getImpuestosArticulo().get(b).getNombreImpuesto());
						objImpuesto.setValor(new BigDecimal(
								objDatos.getArticulos().get(a).getImpuestosArticulo().get(b).getValor()));
						lstImpuesto.add(objImpuesto);
					}

					// descuento SCL
					if (!(objDatos.getArticulos().get(a).getDescuentoSCL() == null
							|| "".equals(objDatos.getArticulos().get(a).getDescuentoSCL()))) {
						descuentoSCL = new BigDecimal(objDatos.getArticulos().get(a).getDescuentoSCL());
						objArt.setDescuento_scl(descuentoSCL);
					}
					log.trace("descuentoSCL: " + descuentoSCL);

					if (new BigDecimal(objDatos.getArticulos().get(a).getDescuentoSidra())
							.compareTo(BigDecimal.ZERO) > 0) {
						lstArticulosFallidosOf = validarOfertas(conn, objDatos.getArticulos().get(a).getArticulo(),
								objDatos.getArticulos().get(a).getSerie(), idPanelRuta, tipoPanelRuta, estadoAlta,
								tipoOferta, tipoGestionVenta, modoOnline,
								objDatos.getArticulos().get(a).getDetalleDescuentosSidra(),
								objDatos.getArticulos().get(a).getDescuentoSidra(), cantDecimales, objDatos.getTipo(),
								objDatos.getIdTipo(), objDatos.getUsuario(), esRecarga, cantidad,
								objInv.getTecnologia(), objInv.getTipo_inv(), objDatos.getCodArea(), idPais);
						if (hayArtFallidoOferta || errorDescuentoTotal) {
							return lstArticulosFallidosOf;
						} else {
							descuentoUnitarioSidra = lstArticulosFallidosOf.get(0).getDescuento_sidra();
							objArt.setDescuento_sidra(descuentoUnitarioSidra);
							objArt.setDescuentosArticulo(lstArticulosFallidosOf.get(0).getDescuentosArticulo());
						}
					} else {
						objArt.setDescuento_sidra(BigDecimal.ZERO);
					}
					log.trace("descuento Sidra enviado: " + objArt.getDescuento_sidra());

					subtotal = precioUnitario.multiply(cantidad);

					objArt.setPrecio_unitario(precioUnitario);
					objArt.setPrecio_total(precioUnitario);
					log.trace("precioUnitario sin impuestos ni descuento: " + precioUnitario);
					log.trace("subtotal: " + subtotal);

					// exentos
					if (banderaExento) {
						// se verifican los descuentos exentos por impuestos exentos (ej: si no se
						// aplica impuesto isc, no se calcula descuento isc)
						for (int i = 0; i < objDatos.getImpuestosExento().size(); i++) {
							for (int j = 0; j < datosImpuestosBD.getDescuentos().size(); j++) {
								if (objDatos.getImpuestosExento().get(i).getNombreImpuesto()
										.equalsIgnoreCase(datosImpuestosBD.getDescuentos().get(j).getNombre())) {
									datosImpuestosBD.getDescuentos().remove(j);
									break;
								}
							}
						}
					}

					log.trace("totalDescuentoPais: " + totalDescuentoPais);

					// descuento Sidra calculado
					if (objArt.getDescuentosArticulo() != null && !objArt.getDescuentosArticulo().isEmpty()) {
						for (DescuentosSidra descuento : objArt.getDescuentosArticulo()) {
							String articulo = objDatos.getArticulos().get(a).getArticulo();
							for (int j = 0; j < datosCampaniaCondiciones.size(); j++) {
								boolean descuentoPDV = false;
								boolean descuentoArticulo = false;
								boolean descuentoZona = false;

								boolean val1 = datosCampaniaCondiciones.get(j).getTipoCliente()
										.equalsIgnoreCase(objDatos.getTipo())
										|| "AMBOS".equalsIgnoreCase(datosCampaniaCondiciones.get(j).getTipoCliente());

								if ("ARTICULO".equalsIgnoreCase(descuento.getTipoDescuento())
										&& (articulo.equals(datosCampaniaCondiciones.get(j).getIdArticulo())
												&& objDatos.getArticulos().get(a).getGestion().equalsIgnoreCase(
														datosCampaniaCondiciones.get(j).getTipoGestion())
												&& descuento.getTipoDescuento().equalsIgnoreCase(
														datosCampaniaCondiciones.get(j).getTipoOferta())
												&& val1)) {
									descuentoArticulo = true;
								}

								if ("PDV".equalsIgnoreCase(descuento.getTipoDescuento())
										&& (articulo.equals(datosCampaniaCondiciones.get(j).getIdArticulo())
												&& objDatos.getArticulos().get(a).getGestion().equalsIgnoreCase(
														datosCampaniaCondiciones.get(j).getTipoGestion())
												&& descuento.getTipoDescuento().equalsIgnoreCase(
														datosCampaniaCondiciones.get(j).getTipoOferta())
												&& datosCampaniaCondiciones.get(j).getIdPDV()
														.equalsIgnoreCase(objDatos.getIdTipo()))) {
									descuentoPDV = true;
								}

								boolean valZona = (articulo.equals(datosCampaniaCondiciones.get(j).getIdArticulo())
										&& objDatos.getArticulos().get(a).getGestion()
												.equalsIgnoreCase(datosCampaniaCondiciones.get(j).getTipoGestion())
										&& descuento.getTipoDescuento()
												.equalsIgnoreCase(datosCampaniaCondiciones.get(j).getTipoOferta())
										&& datosCampaniaCondiciones.get(j).getZona()
												.equalsIgnoreCase(datosPDV.get(PuntoVenta.CAMPO_CATEGORIA))
										&& datosCampaniaCondiciones.get(j).getCategoria()
												.equalsIgnoreCase(datosPDV.get(PuntoVenta.CAMPO_TCSCZONACOMERCIALID)));
								if ("ZONA".equalsIgnoreCase(descuento.getTipoDescuento()) && valZona) {

									descuentoZona = true;
								}

								if (descuentoArticulo || descuentoPDV || descuentoZona) {
									idOfertaCampania = new BigDecimal(
											datosCampaniaCondiciones.get(j).getIdOfertaCampania());
									descuentoUnitarioSidra = new BigDecimal(
											datosCampaniaCondiciones.get(j).getValorDescuento());
									log.trace("idOfertaCampania: " + idOfertaCampania + ", art: "
											+ objDatos.getArticulos().get(a).getArticulo() + ", tipodesc: "
											+ datosCampaniaCondiciones.get(j).getTipoOferta() + ", desc: "
											+ datosCampaniaCondiciones.get(j).getValorDescuento());

									if (datosCampaniaCondiciones.get(j).getTipoDescuento()
											.equalsIgnoreCase(tipoMonto)) {
										// convertir monto descuento en porcentaje
										descuentoUnitarioSidra = descuentoUnitarioSidra
												.divide((precioUnitario.subtract(valorCES)), mc); // se resta el CES, si
																									// no aplica siempre
																									// es 0
									} else {
										descuentoUnitarioSidra = descuentoUnitarioSidra.divide(new BigDecimal(100), mc);
									}
									descuentoSidraTotal = descuentoSidraTotal.add(descuentoUnitarioSidra);
									break;
								}
							}
						}
					}
					log.trace("porcentaje descuentoSidraBD: " + descuentoSidraTotal);

					// impuestos por art\u00EDculo
					for (InputImpuestos impuestoBD : datosImpuestosBD.getImpuestos()) {
						boolean aplicaImpuesto = false;
						impuestoArt = new PagoImpuesto();

						nombreImpuesto = impuestoBD.getNombre();
						log.trace("---------------------------------------------------");
						log.trace("nombreImpuesto: " + nombreImpuesto);

						log.trace("tipoCliente: " + impuestoBD.getTipoCliente());
						log.trace("despuesDeDescuento: " + impuestoBD.getDespuesDeDescuento());

						if (banderaExento) {
							for (int j = 0; j < objDatos.getImpuestosExento().size(); j++) {
								if (objDatos.getImpuestosExento().get(j).getNombreImpuesto()
										.equalsIgnoreCase(nombreImpuesto)) {
									// no se calcula el impuesto
									exentoImpuesto = true;
									log.trace("exento de impuesto: " + nombreImpuesto);
									break;
								}
							}
						}

						if (!exentoImpuesto) {
							for (InputImpuestosGrupos grupoImpuesto : impuestoBD.getGrupos()) {
								if ("TARJETASRASCA".equalsIgnoreCase(grupoImpuesto.getNombre())) {
									aplicaImpuesto = true;
								}

								if (aplicaImpuesto) {
									if ("".equals(impuestoBD.getTipoCliente())) {
										// tipo de cliente del impuesto esta vacio (aplica para ambos)
										aplicaImpuesto = true;
										break;

									} else if (impuestoBD.getTipoCliente().equals(objDatos.getTipo())) {
										// tipo de cliente de la venta con el tipo de cliente del impuesto
										aplicaImpuesto = true;
										break;

									} else {
										// no aplica porque el cliente no es correcto
										aplicaImpuesto = false;
										break;
									}

								}
							}
							log.trace(objDatos.getCodArea() + ", aplica impuesto:" + aplicaImpuesto + ", impuesto: "
									+ impuestoBD.getNombre() + ", tipoDocumento:" + objDatos.getTipoDocumento());
							log.trace("aplica percepcion:" + aplicaPercepcion);
							if ("503".equals(objDatos.getCodArea())
									& "PERCEPCION".equalsIgnoreCase(impuestoBD.getNombre())) {
								if (aplicaPercepcion & aplicaImpuesto) {
									aplicaImpuesto = true;
								} else {
									aplicaImpuesto = false;
								}
							}

							if (nombreImpuesto.equals(impuestoCR) && aplicaCR0) {// TODO verificar mayor a 50000
								aplicaImpuesto = false;
							}

							log.trace("aplicaImpuesto:" + aplicaImpuesto);
							if (aplicaImpuesto) {
								log.trace("aplica impuesto");
								if ("503".equals(objDatos.getCodArea())) {// TODO proceso de SV, solo una vez para PA,
																			// NI y CR

									totalDescuento = new BigDecimal(objDatos.getArticulos().get(a).getDescuentoSidra());

									log.trace("totalDescuento Sidra: " + totalDescuento);

								} else {
									totalDescuento = new BigDecimal(objDatos.getArticulos().get(a).getDescuentoSidra());
								}

								valorImpuesto = new BigDecimal(impuestoBD.getPorcentaje());
								valorImpuesto = valorImpuesto.divide(new BigDecimal(100), mc);
								log.trace("porcentaje impuesto: " + valorImpuesto);

								aplicaImpuestoCR = false;
								if (nombreImpuesto.equals(impuestoCR) && aplicaCR500) {
									if (indexArticuloImpuestoCR500 == a) {
										aplicaImpuestoCR = true;
										aplicaImpuestoCRGlobal = true;
									} else {
										valorImpuesto = BigDecimal.ZERO;
										log.trace("valorImpuesto: " + valorImpuesto);

										impuestoArt.setValor(valorImpuesto);
									}
								}

								if (!aplicaImpuestoCR) {
									if ("FALSE".equalsIgnoreCase(impuestoBD.getDespuesDeDescuento())) {
										// Total Venta Sin Impuestos * %CESC
										valorImpuesto = subtotal.multiply(valorImpuesto);
										valorCES = valorImpuesto;
										log.trace("valorCES: " + valorCES);

									} else if (impuestoBD.getDespuesDeDescuento().equalsIgnoreCase("TRUE")) {
										if (isFullStack(objDatos.getCodArea())) {

											if (aplicaPercepcion & aplicaImpuesto
													& "PERCEPCION".equalsIgnoreCase(impuestoBD.getNombre())) {

												BigDecimal montoFacial;
												BigDecimal descuento;
												BigDecimal iva;
												BigDecimal cesc;
												BigDecimal totalRestas = BigDecimal.ZERO;
												montoFacial = subtotal
														.multiply(valorCes.add(valorIva, mc).add(BigDecimal.ONE, mc));
												descuento = new BigDecimal(
														objDatos.getArticulos().get(a).getDescuentoSidra());
												iva = (subtotal.subtract(
														descuento.divide((valorIva.add(BigDecimal.ONE, mc)), mc)))
																.multiply(valorIva, mc);
												cesc = subtotal.multiply(valorCes, mc);
												log.trace("montoFacial:" + montoFacial);
												log.trace("descuento:" + descuento);
												log.trace("iva:" + iva);
												log.trace("cesc:" + cesc);

												totalRestas = totalRestas.add(descuento, mc).add(iva, mc).add(cesc, mc);
												log.trace("totalRestas:" + totalRestas);
												totalCalc = montoFacial.subtract(totalRestas, mc);

												log.trace("precio Antes de percepcion:" + totalCalc);
												valorImpuesto = totalCalc.multiply(valorImpuesto);

											} else {
												valorImpuesto = (subtotal.subtract(
														totalDescuento.divide(valorImpuesto.add(BigDecimal.ONE), mc)))
																.multiply(valorImpuesto);
												// TODO revisiones a impuestos en ventas de SV

											}
										} else {
											valorImpuesto = (subtotal.subtract(objArt.getDescuento_sidra()))
													.multiply(valorImpuesto);
										}
									}
									log.trace("valorImpuesto: " + valorImpuesto);

									impuestoArt.setValor(valorImpuesto.divide(cantidad, mc));

								} else {
									valorImpuesto = new BigDecimal(500);
									log.trace("valorImpuesto: " + valorImpuesto);

									impuestoArt.setValor(valorImpuesto);
								}

								impuestoArt.setImpuesto(nombreImpuesto);
								impuestoArt.setCreado_por(objDatos.getUsuario());

								// se suma al total por articulo para verificarlo
								totalImpuestoIndividual = totalImpuestoIndividual.add(valorImpuesto);
								log.trace("totalImpuestoIndividual: " + totalImpuestoIndividual);

								listImpuestosArt.add(impuestoArt);
								if (aplicaImpuestoCR) {
									indexImpuestoCR = listImpuestosArt.size() - 1; // util para saber el impuesto
																					// especial CR
								}

								for (int j = 0; j < lstImpuesto.size(); j++) {
									if (lstImpuesto.get(j).getImpuesto().equalsIgnoreCase(nombreImpuesto)) {
										log.trace("impuesto calculado=enviado? => "
												+ UtileriasJava.redondear(valorImpuesto, cantDecimales) + " = "
												+ UtileriasJava.redondear(lstImpuesto.get(j).getValor(),
														cantDecimales));
										if (Country.PA.getCodArea().toString().compareTo(objDatos.getCodArea()) != 0
												&& UtileriasJava.redondear(lstImpuesto.get(j).getValor(), cantDecimales)
														.compareTo(UtileriasJava.redondear(valorImpuesto,
																cantDecimales)) != 0) {
											// los valor de los impuestos no coinciden
											log.trace("el valor de los impuestos no coinciden: " + nombreImpuesto
													+ ", art\u00EDculo: "
													+ objDatos.getArticulos().get(a).getArticulo());
											errorImpuestoArt = true;
											objArt = new VentaDet();
											objArt.setArticulo(
													new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
											objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
											objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
											lstErrores.add(objArt);
											return lstErrores;
										}
									}
								}

								log.trace("descuentoSidra calculado=enviado? => "
										+ UtileriasJava.redondear(totalDescuento, cantDecimales) + " = "
										+ UtileriasJava.redondear(
												new BigDecimal(objDatos.getArticulos().get(a).getDescuentoSidra()),
												cantDecimales));

								if (Country.PA.getCodArea().toString().compareTo(objDatos.getCodArea()) != 0
										&& UtileriasJava.redondear(totalDescuento, cantDecimales)
												.compareTo(UtileriasJava.redondear(
														new BigDecimal(
																objDatos.getArticulos().get(a).getDescuentoSidra()),
														cantDecimales)) != 0) {

									log.trace("el descuento sidra enviado no coincide con el descuento calculado");
									errorDescuento = true;
									objArt = new VentaDet();
									objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
									objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
									objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
									lstErrores.add(objArt);
									return lstErrores;
								}

								objArt.setImpuestoArticulo(listImpuestosArt);
							} else {
								log.trace("no aplica impuesto");
							}
						}
					}

					if (isFullStack(objDatos.getCodArea())) {
						log.trace("precioConImpuesto: " + objDatos.getArticulos().get(a).getPrecioTotal());
						precioConImpuesto = new BigDecimal(objDatos.getArticulos().get(a).getPrecioTotal())
								.divide(cantidad, mc);

					} else {
						precioConImpuesto = (precioUnitario.subtract(totalDescuentoPais.divide(cantidad, mc))
								.subtract(objArt.getDescuento_sidra().divide(cantidad, mc)))
										.add(totalImpuestoIndividual.divide(cantidad, mc));
					}
					log.trace("precioConImpuesto: " + precioConImpuesto);
					precioFinal = precioConImpuesto.multiply(cantidad);

					log.trace("precioFinal del rango: " + precioFinal);

					log.trace("impuestos totales? calculado=recibido => "
							+ UtileriasJava.redondear(totalImpuestoIndividual, cantDecimales) + " = "
							+ UtileriasJava.redondear(new BigDecimal(objDatos.getArticulos().get(a).getImpuesto()),
									cantDecimales));
					if (Country.PA.getCodArea().toString().compareTo(objDatos.getCodArea()) != 0 && UtileriasJava
							.redondear(new BigDecimal(objDatos.getArticulos().get(a).getImpuesto()), cantDecimales)
							.compareTo(UtileriasJava.redondear(totalImpuestoIndividual, cantDecimales)) != 0) {
						// el valor de los impuestos totales no coinciden
						log.trace("el valor de los impuestos totales no coinciden.");
						errorImpuestoTotal = true;
						objArt = new VentaDet();
						objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
						objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
						objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
						lstErrores.add(objArt);
						return lstErrores;
					}

					log.trace("precio total rango? calculado=recibido => "
							+ UtileriasJava.redondear(precioFinal, cantDecimales) + " = " + UtileriasJava.redondear(
									new BigDecimal(objDatos.getArticulos().get(a).getPrecioTotal()), cantDecimales));
					if (Country.PA.getCodArea().toString().compareTo(objDatos.getCodArea()) != 0 && UtileriasJava
							.redondear(new BigDecimal(objDatos.getArticulos().get(a).getPrecioTotal()), cantDecimales)
							.compareTo(UtileriasJava.redondear(precioFinal, cantDecimales)) != 0) {
						// el valor del precio total con el total del rango no coinciden
						log.trace("el valor del precio total con el total del rango no coinciden.");
						errorTotalFactura = true;
						objArt = new VentaDet();
						objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
						objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
						objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
						lstErrores.add(objArt);
						return lstErrores;
					}

					precioFinal = precioFinal.divide(cantidad, mc);
					log.debug("Precio final del articulo: " + precioFinal);

					objArt.setImpuesto(totalImpuestoIndividual.divide(cantidad, mc));
					objArt.setPrecio_final(precioFinal);
					objArt.setDescuento_sidra(totalDescuento.divide(cantidad, mc));

					BigDecimal rangoInicio = new BigDecimal(objDatos.getArticulos().get(a).getSerieInicial());
					BigDecimal rangoFinal = new BigDecimal(objDatos.getArticulos().get(a).getSerieFinal());
					int diferenciaCeros = OperacionMovimientosInventario.getDiferenciaCeros(serieInicial);
					String cerosSerie = "";
					for (int i = 0; i < diferenciaCeros; i++) {
						cerosSerie += "0";
					}

					String usuario = "";
					String impuesto = "";
					for (BigDecimal serie = rangoInicio; serie.compareTo(rangoFinal) <= 0; serie = serie
							.add(BigDecimal.ONE)) {
						VentaDet objArtRango;
						objArtRango = (VentaDet) objArt.clone();
						objArtRango.setSerie(cerosSerie + serie);

						if (aplicaImpuestoCRGlobal) {
							List<PagoImpuesto> list = new ArrayList<PagoImpuesto>();
							PagoImpuesto imp = new PagoImpuesto();

							if (serie.compareTo(rangoInicio) == 0) {
								usuario = objArtRango.getImpuestoArticulo().get(indexImpuestoCR).getCreado_por();
								impuesto = objArtRango.getImpuestoArticulo().get(indexImpuestoCR).getImpuesto();
								objArtRango.getImpuestoArticulo().remove(indexImpuestoCR);
							}

							for (PagoImpuesto pagoImpuesto : objArtRango.getImpuestoArticulo()) {
								list.add(pagoImpuesto);
							}

							if (serie.compareTo(rangoFinal) == 0) {
								imp.setValor(new BigDecimal(500));
							} else {
								imp.setValor(BigDecimal.ZERO);
							}
							imp.setCreado_por(usuario);
							imp.setImpuesto(impuesto);
							list.add(imp);

							objArtRango.setImpuestoArticulo(list);
						}

						lstArticulos.add(objArtRango);
					}

				} else {
					String seriesInvalidas1 = "";
					seriesInvalidas1 = seriesInvalidas.substring(0, seriesInvalidas.length() - 1);
					String series[] = seriesInvalidas1.split(",");

					hayArtFallido = true;
					for (int i = 0; i < series.length; i++) {
						objArt = new VentaDet();
						objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
						objArt.setSerie(series[i]);
						objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
						lstArticulosFallidos.add(objArt);
					}
				}
			}
			/* cambio agregado 25/09/2017 por jcsimon */
			// validamos la serie de simcar
			else if (objDatos.getArticulos().get(a).getRango().equals("1")
					&& tipoProducto.equalsIgnoreCase(Conf.TIPO_GRUPO_SIMCAR)) {
				seriesInvalidas = OperacionVentas.validarSeriesSimcard(conn,
						objDatos.getArticulos().get(a).getSerieInicial(),
						objDatos.getArticulos().get(a).getSerieFinal(),
						new BigDecimal(objDatos.getArticulos().get(a).getArticulo()),
						new BigDecimal(objDatos.getIdBodegaVendedor()), objDatos.getCodArea());

				if (seriesInvalidas.equalsIgnoreCase("OK")) {
					objArt = new VentaDet();
					lstImpuesto = new ArrayList<PagoImpuesto>();
					listImpuestosArt = new ArrayList<PagoImpuesto>();
					BigDecimal precioConImpuesto;
					BigDecimal precioFinal;
					BigDecimal subtotal;
					BigDecimal totalDescuento = BigDecimal.ZERO;
					BigDecimal totalImpuestoIndividual = BigDecimal.ZERO;
					BigDecimal valorCES = BigDecimal.ZERO; // v\u00E9lido unicamente para SV
					BigDecimal descuentoPais = BigDecimal.ZERO;
					BigDecimal totalDescuentoPais = BigDecimal.ZERO;
					BigDecimal descuentoSidraTotal = BigDecimal.ZERO;
					boolean aplicaImpuestoCR = false;
					int indexImpuestoCR = 0;
					boolean aplicaImpuestoCRGlobal = false;

					String serieInicial = objDatos.getArticulos().get(a).getSerieInicial();
					objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
					cantidad = new BigDecimal(objDatos.getArticulos().get(a).getCantidad());
					log.trace("cantidad: " + cantidad);
					objArt.setCantidad(BigDecimal.ONE);
					objArt.setCreado_por(objDatos.getUsuario());
					objArt.setEstado(estadoAlta);
					objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
					objArt.setTipo_grupo_sidra(objDatos.getArticulos().get(a).getTipoGrupoSidra());
					objArt.setSerie_asociada(objDatos.getArticulos().get(a).getSerieAsociada());
					objArt.setGestion(objDatos.getArticulos().get(a).getGestion());
					objArt.setBodega_panel_ruta(bodegaRutaPanel);
					objArt.setTcscbodegavirtualid(new BigDecimal(objDatos.getIdBodegaVendedor()));

					if (!(objDatos.getArticulos().get(a).getModalidad() == null
							|| "".equals(objDatos.getArticulos().get(a).getModalidad()))) {
						objArt.setModalidad((objDatos.getArticulos().get(a).getModalidad()));
					}
					if (!(objDatos.getArticulos().get(a).getNumTelefono() == null
							|| "".equals(objDatos.getArticulos().get(a).getNumTelefono()))) {
						objArt.setNum_telefono(new BigDecimal(objDatos.getArticulos().get(a).getNumTelefono()));
					}

					// se arma el listado de impuestos del art\u00EDculo
					for (int b = 0; b < objDatos.getArticulos().get(a).getImpuestosArticulo().size(); b++) {
						objImpuesto = new PagoImpuesto();
						objImpuesto.setCreado_por(objDatos.getUsuario());
						objImpuesto.setImpuesto(
								objDatos.getArticulos().get(a).getImpuestosArticulo().get(b).getNombreImpuesto());
						objImpuesto.setValor(new BigDecimal(
								objDatos.getArticulos().get(a).getImpuestosArticulo().get(b).getValor()));
						lstImpuesto.add(objImpuesto);
					}

					// descuento SCL
					if (!(objDatos.getArticulos().get(a).getDescuentoSCL() == null
							|| "".equals(objDatos.getArticulos().get(a).getDescuentoSCL()))) {
						descuentoSCL = new BigDecimal(objDatos.getArticulos().get(a).getDescuentoSCL());
						objArt.setDescuento_scl(descuentoSCL);
						log.trace("descuentoSCL: " + descuentoSCL);
					}

					if (new BigDecimal(objDatos.getArticulos().get(a).getDescuentoSidra())
							.compareTo(BigDecimal.ZERO) > 0) {
						lstArticulosFallidosOf = validarOfertas(conn, objDatos.getArticulos().get(a).getArticulo(),
								objDatos.getArticulos().get(a).getSerie(), idPanelRuta, tipoPanelRuta, estadoAlta,
								tipoOferta, tipoGestionVenta, modoOnline,
								objDatos.getArticulos().get(a).getDetalleDescuentosSidra(),
								objDatos.getArticulos().get(a).getDescuentoSidra(), cantDecimales, objDatos.getTipo(),
								objDatos.getIdTipo(), objDatos.getUsuario(), esRecarga, cantidad,
								objInv.getTecnologia(), objInv.getTipo_inv(), objDatos.getCodArea(), idPais);
						if (hayArtFallidoOferta || errorDescuentoTotal) {
							return lstArticulosFallidosOf;
						} else {
							descuentoUnitarioSidra = lstArticulosFallidosOf.get(0).getDescuento_sidra();
							objArt.setDescuento_sidra(descuentoUnitarioSidra);
							objArt.setDescuentosArticulo(lstArticulosFallidosOf.get(0).getDescuentosArticulo());
						}
					} else {
						objArt.setDescuento_sidra(BigDecimal.ZERO);
					}
					log.trace("descuento Sidra enviado: " + objArt.getDescuento_sidra());

					subtotal = precioUnitario.multiply(cantidad);

					objArt.setPrecio_unitario(precioUnitario);
					objArt.setPrecio_total(precioUnitario);
					log.trace("precioUnitario sin impuestos ni descuento: " + precioUnitario);
					log.trace("subtotal: " + subtotal);

					// exentos
					if (banderaExento) {
						// se verifican los descuentos exentos por impuestos exentos (ej: si no se
						// aplica impuesto isc, no se calcula descuento isc)
						for (int i = 0; i < objDatos.getImpuestosExento().size(); i++) {
							for (int j = 0; j < datosImpuestosBD.getDescuentos().size(); j++) {
								if (objDatos.getImpuestosExento().get(i).getNombreImpuesto()
										.equalsIgnoreCase(datosImpuestosBD.getDescuentos().get(j).getNombre())) {
									datosImpuestosBD.getDescuentos().remove(j);
									break;
								}
							}
						}
					}

					if (objDatos.getCodArea().equals("507")) {
						if (objDatos.getTipo().equalsIgnoreCase(clienteFinal)) {
							log.trace("descuentos de cliente final");

						} else {
							// procesos de calculo de impuestos aplicados a PDV (solo aplica PA)
							log.trace("descuentos de punto de venta");

							// se calcula el total de descuentos por pais
							for (int j = 0; j < datosImpuestosBD.getDescuentos().size(); j++) {
								descuentoPais = descuentoPais
										.add(new BigDecimal(datosImpuestosBD.getDescuentos().get(j).getPorcentaje())
												.divide(new BigDecimal(100), mc));
							}
							log.trace("porcentaje descuento pais: " + descuentoPais);

							totalDescuentoPais = subtotal.multiply(descuentoPais); // se calcula el descuento sobre el
																					// subtotal
							subtotal = subtotal.subtract(totalDescuentoPais);
						}
					}
					log.trace("totalDescuentoPais: " + totalDescuentoPais);

					// descuento Sidra calculado
					if (objArt.getDescuentosArticulo() != null && !objArt.getDescuentosArticulo().isEmpty()) {
						for (DescuentosSidra descuento : objArt.getDescuentosArticulo()) {
							String articulo = objDatos.getArticulos().get(a).getArticulo();
							for (int j = 0; j < datosCampaniaCondiciones.size(); j++) {
								boolean descuentoPDV = false;
								boolean descuentoArticulo = false;
								boolean descuentoZona = false;

								boolean val1 = datosCampaniaCondiciones.get(j).getTipoCliente()
										.equalsIgnoreCase(objDatos.getTipo())
										|| datosCampaniaCondiciones.get(j).getTipoCliente().equalsIgnoreCase("AMBOS");
								if (descuento.getTipoDescuento().equals("ARTICULO")
										&& (articulo.equals(datosCampaniaCondiciones.get(j).getIdArticulo())
												&& objDatos.getArticulos().get(a).getGestion().equalsIgnoreCase(
														datosCampaniaCondiciones.get(j).getTipoGestion())
												&& descuento.getTipoDescuento().equalsIgnoreCase(
														datosCampaniaCondiciones.get(j).getTipoOferta())
												&& val1)) {
									descuentoArticulo = true;
								}

								if ("PDV".equals(descuento.getTipoDescuento())
										&& (articulo.equals(datosCampaniaCondiciones.get(j).getIdArticulo())
												&& objDatos.getArticulos().get(a).getGestion().equalsIgnoreCase(
														datosCampaniaCondiciones.get(j).getTipoGestion())
												&& descuento.getTipoDescuento().equalsIgnoreCase(
														datosCampaniaCondiciones.get(j).getTipoOferta())
												&& datosCampaniaCondiciones.get(j).getIdPDV()
														.equalsIgnoreCase(objDatos.getIdTipo()))) {
									descuentoPDV = true;
								}

								boolean valZona = (articulo.equals(datosCampaniaCondiciones.get(j).getIdArticulo())
										&& objDatos.getArticulos().get(a).getGestion()
												.equalsIgnoreCase(datosCampaniaCondiciones.get(j).getTipoGestion())
										&& descuento.getTipoDescuento()
												.equalsIgnoreCase(datosCampaniaCondiciones.get(j).getTipoOferta())
										&& datosCampaniaCondiciones.get(j).getZona()
												.equalsIgnoreCase(datosPDV.get(PuntoVenta.CAMPO_CATEGORIA))
										&& datosCampaniaCondiciones.get(j).getCategoria()
												.equalsIgnoreCase(datosPDV.get(PuntoVenta.CAMPO_TCSCZONACOMERCIALID)));

								if (descuento.getTipoDescuento().equals("ZONA") && valZona) {
									descuentoZona = true;
								}

								if (descuentoArticulo || descuentoPDV || descuentoZona) {
									idOfertaCampania = new BigDecimal(
											datosCampaniaCondiciones.get(j).getIdOfertaCampania());
									descuentoUnitarioSidra = new BigDecimal(
											datosCampaniaCondiciones.get(j).getValorDescuento());
									log.trace("idOfertaCampania: " + idOfertaCampania + ", art: "
											+ objDatos.getArticulos().get(a).getArticulo() + ", tipodesc: "
											+ datosCampaniaCondiciones.get(j).getTipoOferta() + ", desc: "
											+ datosCampaniaCondiciones.get(j).getValorDescuento());

									if (datosCampaniaCondiciones.get(j).getTipoDescuento()
											.equalsIgnoreCase(tipoMonto)) {
										descuentoUnitarioSidra = descuentoUnitarioSidra
												.divide((precioUnitario.subtract(valorCES)), mc); // se resta el CES, si
																									// no aplica siempre
																									// es 0
									} else {
										descuentoUnitarioSidra = descuentoUnitarioSidra.divide(new BigDecimal(100), mc);
									}
									descuentoSidraTotal = descuentoSidraTotal.add(descuentoUnitarioSidra);
									break;
								}
							}
						}
					}
					log.trace("porcentaje descuentoSidraBD: " + descuentoSidraTotal);

					// impuestos por art\u00EDculo
					for (InputImpuestos impuestoBD : datosImpuestosBD.getImpuestos()) {
						boolean aplicaImpuesto = false;
						impuestoArt = new PagoImpuesto();

						nombreImpuesto = impuestoBD.getNombre();
						log.trace("---------------------------------------------------");
						log.trace("nombreImpuesto: " + nombreImpuesto);

						log.trace("tipoCliente: " + impuestoBD.getTipoCliente());
						log.trace("despuesDeDescuento: " + impuestoBD.getDespuesDeDescuento());

						if (banderaExento) {
							for (int j = 0; j < objDatos.getImpuestosExento().size(); j++) {
								if (objDatos.getImpuestosExento().get(j).getNombreImpuesto()
										.equalsIgnoreCase(nombreImpuesto)) {
									// no se calcula el impuesto
									exentoImpuesto = true;
									log.trace("exento de impuesto: " + nombreImpuesto);
									break;
								}
							}
						}

						if (!exentoImpuesto) {
							for (InputImpuestosGrupos grupoImpuesto : impuestoBD.getGrupos()) {
								if (grupoImpuesto.getNombre()
										.equalsIgnoreCase(objDatos.getArticulos().get(a).getTipoGrupoSidra())) {// SIMCARD
									aplicaImpuesto = true;
								}

								if (aplicaImpuesto) {
									if ("".equals(impuestoBD.getTipoCliente())) {
										// tipo de cliente del impuesto esta vacio (aplica para ambos)
										aplicaImpuesto = true;
										break;

									} else if (impuestoBD.getTipoCliente().equals(objDatos.getTipo())) {
										// tipo de cliente de la venta con el tipo de cliente del impuesto
										aplicaImpuesto = true;
										break;

									} else {
										// no aplica porque el cliente no es correcto
										aplicaImpuesto = false;
										break;
									}
								}
							}
							log.trace(objDatos.getCodArea() + ", aplica impuesto:" + aplicaImpuesto + ", impuesto: "
									+ impuestoBD.getNombre() + ", tipoDocumento:" + objDatos.getTipoDocumento());
							log.trace("aplica percepcion:" + aplicaPercepcion);
							if ("503".equals(objDatos.getCodArea())
									& "PERCEPCION".equalsIgnoreCase(impuestoBD.getNombre())) {
								if (aplicaPercepcion & aplicaImpuesto) {
									aplicaImpuesto = true;
								} else {
									aplicaImpuesto = false;
								}
							}

							if (nombreImpuesto.equals(impuestoCR) && aplicaCR0) {
								aplicaImpuesto = false;
							}

							if (aplicaImpuesto) {
								log.trace("aplica impuesto");

								totalDescuento = subtotal.multiply(descuentoSidraTotal);

								valorImpuesto = new BigDecimal(impuestoBD.getPorcentaje());
								valorImpuesto = valorImpuesto.divide(new BigDecimal(100), mc);
								log.trace("porcentaje impuesto: " + valorImpuesto);

								aplicaImpuestoCR = false;
								if (nombreImpuesto.equals(impuestoCR) && aplicaCR500) {
									if (indexArticuloImpuestoCR500 == a) {
										aplicaImpuestoCR = true;
										aplicaImpuestoCRGlobal = true;
									} else {
										valorImpuesto = BigDecimal.ZERO;
										log.trace("valorImpuesto: " + valorImpuesto);

										impuestoArt.setValor(valorImpuesto);
									}
								}

								if (!aplicaImpuestoCR) {
									if ("FALSE".equals(impuestoBD.getDespuesDeDescuento())) {
										// Total Venta Sin Impuestos * %CESC
										valorImpuesto = subtotal.multiply(valorImpuesto);
										valorCES = valorImpuesto;
										log.trace("valorCES: " + valorCES);

									} else if ("TRUE".equals(impuestoBD.getDespuesDeDescuento())) {
										if ("503".equals(objDatos.getCodArea())) {
											if (aplicaPercepcion & aplicaImpuesto
													& "PERCEPCION".equalsIgnoreCase(impuestoBD.getNombre())) {

												BigDecimal montoFacial;
												BigDecimal descuento;
												BigDecimal iva;
												BigDecimal cesc;
												BigDecimal totalRestas = BigDecimal.ZERO;
												montoFacial = subtotal
														.multiply(valorCes.add(valorIva, mc).add(BigDecimal.ONE, mc));
												descuento = new BigDecimal(
														objDatos.getArticulos().get(a).getDescuentoSidra());
												iva = (subtotal.subtract(
														descuento.divide((valorIva.add(BigDecimal.ONE, mc)), mc)))
																.multiply(valorIva, mc);
												cesc = subtotal.multiply(valorCes, mc);
												log.trace("montoFacial:" + montoFacial);
												log.trace("descuento:" + descuento);
												log.trace("iva:" + iva);
												log.trace("cesc:" + cesc);
												totalRestas = totalRestas.add(descuento, mc).add(iva, mc).add(cesc, mc);
												log.trace("totalRestas:" + totalRestas);
												totalCalc = montoFacial.subtract(totalRestas, mc);

												log.trace("precio Antes de percepcion:" + totalCalc);
												valorImpuesto = totalCalc.multiply(valorImpuesto);

											} else {
												valorImpuesto = (subtotal.subtract(
														totalDescuento.divide(valorImpuesto.add(BigDecimal.ONE), mc)))
																.multiply(valorImpuesto);
												// TODO revisiones a impuestos en ventas de SV

											}

										} else {
											valorImpuesto = (subtotal.subtract(objArt.getDescuento_sidra()))
													.multiply(valorImpuesto);
										}
									}
									log.trace("valorImpuesto: " + valorImpuesto);

									impuestoArt.setValor(valorImpuesto.divide(cantidad, mc));

								} else {
									valorImpuesto = new BigDecimal(500);
									log.trace("valorImpuesto: " + valorImpuesto);

									impuestoArt.setValor(valorImpuesto);
								}

								impuestoArt.setImpuesto(nombreImpuesto);
								impuestoArt.setCreado_por(objDatos.getUsuario());

								// se suma al total por articulo para verificarlo
								totalImpuestoIndividual = totalImpuestoIndividual.add(valorImpuesto);
								log.trace("totalImpuestoIndividual: " + totalImpuestoIndividual);

								listImpuestosArt.add(impuestoArt);
								if (aplicaImpuestoCR) {
									indexImpuestoCR = listImpuestosArt.size() - 1; // util para saber el impuesto
																					// especial CR
								}

								for (int j = 0; j < lstImpuesto.size(); j++) {
									if (lstImpuesto.get(j).getImpuesto().equalsIgnoreCase(nombreImpuesto)) {
										log.trace("impuesto calculado=enviado? => "
												+ UtileriasJava.redondear(valorImpuesto, cantDecimales) + " = "
												+ UtileriasJava.redondear(lstImpuesto.get(j).getValor(),
														cantDecimales));
										if (Country.PA.getCodArea().toString().compareTo(objDatos.getCodArea()) != 0
												&& UtileriasJava.redondear(lstImpuesto.get(j).getValor(), cantDecimales)
														.compareTo(UtileriasJava.redondear(valorImpuesto,
																cantDecimales)) != 0) {
											// los valor de los impuestos no coinciden
											log.trace("el valor de los impuestos no coinciden: " + nombreImpuesto
													+ ", art\u00EDculo: "
													+ objDatos.getArticulos().get(a).getArticulo());
											errorImpuestoArt = true;
											objArt = new VentaDet();
											objArt.setArticulo(
													new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
											objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
											objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
											lstErrores.add(objArt);
											return lstErrores;
										}
									}
								}

								log.trace("descuentoSidra calculado=enviado? => "
										+ UtileriasJava.redondear(totalDescuento, cantDecimales) + " = "
										+ UtileriasJava.redondear(
												new BigDecimal(objDatos.getArticulos().get(a).getDescuentoSidra()),
												cantDecimales));
								if (Country.PA.getCodArea().toString().compareTo(objDatos.getCodArea()) != 0
										&& UtileriasJava.redondear(totalDescuento, cantDecimales)
												.compareTo(UtileriasJava.redondear(
														new BigDecimal(
																objDatos.getArticulos().get(a).getDescuentoSidra()),
														cantDecimales)) != 0) {
									log.trace("el descuento sidra enviado no coincide con el descuento calculado");
									errorDescuento = true;
									objArt = new VentaDet();
									objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
									objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
									objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
									lstErrores.add(objArt);
									return lstErrores;
								}

								objArt.setImpuestoArticulo(listImpuestosArt);
							} else {
								log.trace("no aplica impuesto");
							}
						}
					}

					if (isFullStack(objDatos.getCodArea())) {
						log.trace("precioConImpuesto: " + objDatos.getArticulos().get(a).getPrecioTotal());
						precioConImpuesto = new BigDecimal(objDatos.getArticulos().get(a).getPrecioTotal())
								.divide(cantidad, mc);

					} else {
						precioConImpuesto = (precioUnitario.subtract(totalDescuentoPais.divide(cantidad, mc))
								.subtract(objArt.getDescuento_sidra().divide(cantidad, mc)))
										.add(totalImpuestoIndividual.divide(cantidad, mc));
					}
					log.trace("precioConImpuesto: " + precioConImpuesto);
					precioFinal = precioConImpuesto.multiply(cantidad);
					log.trace("precioFinal del rango: " + precioFinal);

					log.trace("impuestos totales? calculado=recibido => "
							+ UtileriasJava.redondear(totalImpuestoIndividual, cantDecimales) + " = "
							+ UtileriasJava.redondear(new BigDecimal(objDatos.getArticulos().get(a).getImpuesto()),
									cantDecimales));
					if (Country.PA.getCodArea().toString().compareTo(objDatos.getCodArea()) != 0 && UtileriasJava
							.redondear(new BigDecimal(objDatos.getArticulos().get(a).getImpuesto()), cantDecimales)
							.compareTo(UtileriasJava.redondear(totalImpuestoIndividual, cantDecimales)) != 0) {
						// el valor de los impuestos totales no coinciden
						log.trace("el valor de los impuestos totales no coinciden.");
						errorImpuestoTotal = true;
						objArt = new VentaDet();
						objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
						objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
						objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
						lstErrores.add(objArt);
						return lstErrores;
					}

					log.trace("precio total rango? calculado=recibido => "
							+ UtileriasJava.redondear(precioFinal, cantDecimales) + " = " + UtileriasJava.redondear(
									new BigDecimal(objDatos.getArticulos().get(a).getPrecioTotal()), cantDecimales));
					if (Country.PA.getCodArea().toString().compareTo(objDatos.getCodArea()) != 0 && UtileriasJava
							.redondear(new BigDecimal(objDatos.getArticulos().get(a).getPrecioTotal()), cantDecimales)
							.compareTo(UtileriasJava.redondear(precioFinal, cantDecimales)) != 0) {
						// el valor del precio total con el total del rango no coinciden
						log.trace("el valor del precio total con el total del rango no coinciden.");
						errorTotalFactura = true;
						objArt = new VentaDet();
						objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
						objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
						objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
						lstErrores.add(objArt);
						return lstErrores;
					}

					precioFinal = precioFinal.divide(cantidad, mc);
					log.debug("Precio final del articulo: " + precioFinal);

					objArt.setImpuesto(totalImpuestoIndividual.divide(cantidad, mc));
					objArt.setPrecio_final(precioFinal);
					objArt.setDescuento_sidra(totalDescuento.divide(cantidad, mc));

					BigDecimal rangoInicio = new BigDecimal(objDatos.getArticulos().get(a).getSerieInicial());
					BigDecimal rangoFinal = new BigDecimal(objDatos.getArticulos().get(a).getSerieFinal());
					int diferenciaCeros = OperacionMovimientosInventario.getDiferenciaCeros(serieInicial);
					String cerosSerie = "";
					for (int i = 0; i < diferenciaCeros; i++) {
						cerosSerie += "0";
					}

					String usuario = "";
					String impuesto = "";
					for (BigDecimal serie = rangoInicio; serie.compareTo(rangoFinal) <= 0; serie = serie
							.add(BigDecimal.ONE)) {
						VentaDet objArtRango;
						objArtRango = (VentaDet) objArt.clone();
						objArtRango.setSerie(cerosSerie + serie);

						if (aplicaImpuestoCRGlobal) {
							List<PagoImpuesto> list = new ArrayList<PagoImpuesto>();
							PagoImpuesto imp = new PagoImpuesto();

							if (serie.compareTo(rangoInicio) == 0) {
								usuario = objArtRango.getImpuestoArticulo().get(indexImpuestoCR).getCreado_por();
								impuesto = objArtRango.getImpuestoArticulo().get(indexImpuestoCR).getImpuesto();
								objArtRango.getImpuestoArticulo().remove(indexImpuestoCR);
							}

							for (PagoImpuesto pagoImpuesto : objArtRango.getImpuestoArticulo()) {
								list.add(pagoImpuesto);
							}

							if (serie.compareTo(rangoFinal) == 0) {
								imp.setValor(new BigDecimal(500));
							} else {
								imp.setValor(BigDecimal.ZERO);
							}
							imp.setCreado_por(usuario);
							imp.setImpuesto(impuesto);
							list.add(imp);

							objArtRango.setImpuestoArticulo(list);
						}

						lstArticulos.add(objArtRango);
					}

				} else {
					String seriesInvalidas1 = "";
					seriesInvalidas1 = seriesInvalidas.substring(0, seriesInvalidas.length() - 1);
					String series[] = seriesInvalidas1.split(",");

					hayArtFallido = true;
					for (int i = 0; i < series.length; i++) {
						objArt = new VentaDet();
						objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
						objArt.setSerie(series[i]);
						objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
						lstArticulosFallidos.add(objArt);
					}
				}

			}
			/* fin cambio agregado 25/09/2017 por jcsimon */
		}

		log.trace("termin\u00F3 de validar art\u00EDculos");
		if (hayArtFallido) {
			log.trace("hay articulos fallidos");
			return lstArticulosFallidos;
		} else if (hayArtFallidoOferta) {
			log.trace("hay articulos oferta fallida");
			return lstArticulosFallidosOf;
		} else if (hayArtFallidoImpuesto) {
			log.trace("hay articulos impuesto fallido");
			return lstArticulosFallidosImp;
		} else if (hayArtFallidoCant) {
			log.trace("hay articulos cantidad fallida");
			return lstArticulosFallidosCant;
		} else {
			log.trace("articulos correctos.");
			return lstArticulos;
		}
	}

	private List<VentaDet> detalleVentaParaAnular(Connection conn, InputVenta objDatos, BigDecimal bodegaRutaPanel,
			String articuloRecarga, String estadoAnulado) throws SQLException, CloneNotSupportedException {
		List<VentaDet> lstArticulos = new ArrayList<VentaDet>();
		VentaDet objArt;

		BigDecimal impuestoArticulo;
		PagoImpuesto objImpuesto;
		List<PagoImpuesto> lstImpuesto;
		ArrayList<DescuentosSidra> lstDescuentosSidra;

		BigDecimal cantidad;
		BigDecimal precioUnitario;
		BigDecimal descuentoSCL;

		boolean esRecarga = false;
		boolean esBono = false;

		for (int a = 0; a < objDatos.getArticulos().size(); a++) {
			log.trace(
					"------------------------------------------------------------------------------------------------------");
			esRecarga = false;
			esBono = false;
			impuestoArticulo = BigDecimal.ZERO;
			descuentoSCL = BigDecimal.ZERO;
			BigDecimal descuentoTotalArticuloSidra = BigDecimal.ZERO;
			lstImpuesto = new ArrayList<PagoImpuesto>();
			lstDescuentosSidra = new ArrayList<DescuentosSidra>();
			objArt = new VentaDet();
			boolean esRango = objDatos.getArticulos().get(a).getRango().equals("1");

			if (objDatos.getArticulos().get(a).getArticulo().equals(articuloRecarga)) {
				log.trace("es recarga");
				esRecarga = true;
			} else if (objDatos.getArticulos().get(a).getTipoGrupoSidra().equalsIgnoreCase(Conf.TIPO_GRUPO_BONO)) {
				log.trace("es bono");
				esBono = true;
				esRecarga = true;
			}

			if (esBono) {
				objArt.setArticulo(new BigDecimal(articuloRecarga));
				String nombreBono = UtileriasBD.executeQueryOneRecord(conn,
						"SELECT DESCRIPCION FROM TC_SC_ARTICULO_INV A WHERE A.ARTICULO = "
								+ objDatos.getArticulos().get(a).getArticulo()
								+ " AND A.TIPO_GRUPO_SIDRA = 'BONO' AND A.ESTADO ='ALTA'");
				objArt.setObservaciones(nombreBono);

			} else {
				objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
				objArt.setObservaciones(null);
			}

			cantidad = new BigDecimal(objDatos.getArticulos().get(a).getCantidad());
			if (!esRango) {
				objArt.setCantidad(cantidad);
				precioUnitario = new BigDecimal(objDatos.getArticulos().get(a).getPrecio());
			} else {
				objArt.setCantidad(BigDecimal.ONE);
				// modificado sbarrios 27-04-18
				precioUnitario = new BigDecimal(objDatos.getArticulos().get(a).getPrecio());
			}
			log.trace("precio base: " + precioUnitario);

			objArt.setCreado_por(objDatos.getUsuario());
			objArt.setEstado(estadoAnulado);
			objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
			objArt.setTipo_grupo_sidra(objDatos.getArticulos().get(a).getTipoGrupoSidra());
			objArt.setGestion(objDatos.getArticulos().get(a).getGestion());
			objArt.setBodega_panel_ruta(bodegaRutaPanel);
			objArt.setTcscbodegavirtualid(new BigDecimal(objDatos.getIdBodegaVendedor()));
			objArt.setModalidad(objDatos.getArticulos().get(a).getModalidad());
			if (objDatos.getArticulos().get(a).getNumTelefono() != null
					&& !"".equals(objDatos.getArticulos().get(a).getNumTelefono())) {
				objArt.setNum_telefono(new BigDecimal(objDatos.getArticulos().get(a).getNumTelefono()));
			}

			// descuento SCL
			if (objDatos.getArticulos().get(a).getDescuentoSCL() != null
					&& !"".equals(objDatos.getArticulos().get(a).getDescuentoSCL())) {
				descuentoSCL = new BigDecimal(objDatos.getArticulos().get(a).getDescuentoSCL());
				objArt.setDescuento_scl(descuentoSCL);
			} else {
				objArt.setDescuento_scl(BigDecimal.ZERO);
			}
			log.trace("descuentoSCL: " + descuentoSCL);

			// descuento sidra
			if (new BigDecimal(objDatos.getArticulos().get(a).getDescuentoSidra()).compareTo(BigDecimal.ZERO) > 0) {
				for (Descuento descuento : objDatos.getArticulos().get(a).getDetalleDescuentosSidra()) {
					BigDecimal cantidadDesc = esRecarga ? BigDecimal.ONE : cantidad;
					DescuentosSidra descuentoSidra = new DescuentosSidra();
					descuentoSidra.setTcscofertacampaniaid(new BigDecimal(descuento.getIdOfertaCampania()));
					descuentoSidra.setValor(new BigDecimal(descuento.getDescuento()));
					descuentoSidra.setTipoDescuento(descuento.getTipoDescuento());
					descuentoSidra.setCantidad(cantidadDesc);
					descuentoSidra.setCreado_por(objDatos.getUsuario());

					descuentoTotalArticuloSidra = descuentoTotalArticuloSidra.add(descuentoSidra.getValor());

					lstDescuentosSidra.add(descuentoSidra);
				}

				objArt.setDescuento_sidra(descuentoTotalArticuloSidra);
				objArt.setDescuentosArticulo(lstDescuentosSidra);
			} else {
				objArt.setDescuento_sidra(BigDecimal.ZERO);
			}

			// impuesto total
			if (objDatos.getArticulos().get(a).getImpuesto() == null
					|| "".equals(objDatos.getArticulos().get(a).getImpuesto())) {
				objArt.setImpuesto(BigDecimal.ZERO);
			}

			// impuestos por art\u00EDculo
			if (!(objDatos.getArticulos().get(a).getImpuestosArticulo() == null
					|| objDatos.getArticulos().get(a).getImpuestosArticulo().isEmpty())) {
				for (int b = 0; b < objDatos.getArticulos().get(a).getImpuestosArticulo().size(); b++) {
					objImpuesto = new PagoImpuesto();
					objImpuesto.setCreado_por(objDatos.getUsuario());
					objImpuesto.setImpuesto(
							objDatos.getArticulos().get(a).getImpuestosArticulo().get(b).getNombreImpuesto());
					objImpuesto.setValor(
							new BigDecimal(objDatos.getArticulos().get(a).getImpuestosArticulo().get(b).getValor()));
					lstImpuesto.add(objImpuesto);

					impuestoArticulo = impuestoArticulo.add(
							new BigDecimal(objDatos.getArticulos().get(a).getImpuestosArticulo().get(b).getValor()));
				}
				objArt.setImpuestoArticulo(lstImpuesto);
			}

			if (esRango) {
				objArt.setImpuesto(impuestoArticulo.divide(cantidad, mc));
				objArt.setDescuento_sidra(descuentoTotalArticuloSidra.divide(cantidad, mc));
				objArt.setPrecio_unitario(precioUnitario);
				objArt.setPrecio_total(precioUnitario);

			} else {
				objArt.setImpuesto(impuestoArticulo);
				objArt.setDescuento_sidra(descuentoTotalArticuloSidra);

				if (esBono) {
					objArt.setPrecio_unitario(precioUnitario.divide(cantidad, mc));
					objArt.setPrecio_total(precioUnitario);
				} else {
					objArt.setPrecio_unitario(precioUnitario);
					objArt.setPrecio_total(precioUnitario.multiply(cantidad));
				}
			}
			log.trace("descuentoUnitarioSidraBD: " + objArt.getDescuento_sidra());
			log.trace("impuestoArticulo: " + objArt.getImpuesto());
			log.trace("precioUnitario: " + objArt.getPrecio_unitario());

			// modificado sbarrios 27-04-18
			if (esRecarga) {
				objArt.setPrecio_final(new BigDecimal(objDatos.getArticulos().get(a).getPrecioTotal()));
			} else {
				objArt.setPrecio_final(
						new BigDecimal(objDatos.getArticulos().get(a).getPrecioTotal()).divide(cantidad, mc));
			}
			log.trace("precioFinal: " + objArt.getPrecio_final());
			// fin cambio

			// se verifica si es venta por rango
			if (!esRango) {
				if (objDatos.getArticulos().get(a).getSeriado().equals("1")) {
					objArt.setSerie(objDatos.getArticulos().get(a).getSerieInicial());
					log.trace("es seriado, serie: " + objArt.getSerie());
					objArt.setSerie_asociada(objDatos.getArticulos().get(a).getSerieAsociada());

				} else if (objDatos.getArticulos().get(a).getSeriado().equals("0")) {
					log.trace("es no seriado, art\u00EDculo: " + objDatos.getArticulos().get(a).getArticulo());
					objArt.setSerie(null);
					objArt.setSerie_asociada(null);
				}

				lstArticulos.add(objArt);
			} else {
				BigDecimal rangoInicio = new BigDecimal(objDatos.getArticulos().get(a).getSerieInicial());
				BigDecimal rangoFinal = new BigDecimal(objDatos.getArticulos().get(a).getSerieFinal());
				int diferenciaCeros = OperacionMovimientosInventario
						.getDiferenciaCeros(objDatos.getArticulos().get(a).getSerieInicial());
				String cerosSerie = "";
				for (int i = 0; i < diferenciaCeros; i++) {
					cerosSerie += "0";
				}

				List<PagoImpuesto> lstImpuestosRango = new ArrayList<PagoImpuesto>();
				for (int i = 0; i < objArt.getImpuestoArticulo().size(); i++) {
					objArt.getImpuestoArticulo().get(i)
							.setValor(objArt.getImpuestoArticulo().get(i).getValor().divide(cantidad, mc));
					lstImpuestosRango.add(objArt.getImpuestoArticulo().get(i));
				}

				while (rangoInicio.doubleValue() <= rangoFinal.doubleValue()) {
					VentaDet objArtRango;
					objArtRango = (VentaDet) objArt.clone();
					objArtRango.setSerie(cerosSerie + rangoInicio);
					objArtRango.setImpuestoArticulo(lstImpuestosRango);

					rangoInicio = rangoInicio.add(BigDecimal.ONE);
					lstArticulos.add(objArtRango);
				}
			}
		}

		return lstArticulos;
	}

	private List<VentaDet> validarOfertas(Connection conn, String articulo, String serie, String idPanelRuta,
			String tipoPanelRuta, String estadoAlta, String tipoOferta, String tipoGestionVenta, boolean modoOnline,
			List<Descuento> detalleDescuentosSidra, String descuentoUnitarioSidraEnviado, Integer cantDecimales,
			String tipoCliente, String idPDV, String usuario, boolean esRecarga, BigDecimal cantidad, String tecnologia,
			String tipoInvArticulo, String codArea, BigDecimal idPais) throws SQLException {
		VentaDet objArt;
		List<VentaDet> lstArticulosFallidosOf = new ArrayList<VentaDet>();
		ArrayList<DescuentosSidra> lstDescuentosSidra = new ArrayList<DescuentosSidra>();
		DescuentosSidra descuentoSidra;
		BigDecimal descuentoTotalArticuloSidra = BigDecimal.ZERO;
		BigDecimal cantidadDesc = esRecarga ? BigDecimal.ONE : cantidad;

		for (Descuento descuento : detalleDescuentosSidra) {
			// primero se verificar\u00E9 si existe el idOferta del cual se indica que viene
			// el descuento sidra
			BigDecimal existeOFC = OperacionVentas.existeOfCampania(conn, articulo, descuento.getIdOfertaCampania(),
					idPanelRuta, tipoPanelRuta, estadoAlta, tipoOferta, tipoGestionVenta, descuento.getTipoDescuento(),
					modoOnline, tipoCliente, idPDV, tecnologia, descuento.getIdCondicion(), idPais, codArea);

			if (existeOFC.intValue() == 0) {
				hayArtFallidoOferta = true;
				objArt = new VentaDet();
				objArt.setArticulo(new BigDecimal(articulo));
				objArt.setSerie(serie);
				objArt.setTipo_inv(tipoInvArticulo);
				lstArticulosFallidosOf.add(objArt);
				return lstArticulosFallidosOf;
			} else {
				descuentoSidra = new DescuentosSidra();
				descuentoSidra.setTcscofertacampaniaid(new BigDecimal(descuento.getIdOfertaCampania()));
				descuentoSidra.setValor(new BigDecimal(descuento.getDescuento()));
				descuentoSidra.setTipoDescuento(descuento.getTipoDescuento());
				descuentoSidra.setCantidad(cantidadDesc);
				descuentoSidra.setCreado_por(usuario);
				descuentoSidra.setIdCondicion(descuento.getIdCondicion());

				descuentoTotalArticuloSidra = descuentoTotalArticuloSidra.add(new BigDecimal(descuento.getDescuento()));

				lstDescuentosSidra.add(descuentoSidra);
			}
		}

		log.trace("descuentoSidra calculado=recibido? : "
				+ UtileriasJava.redondear(descuentoTotalArticuloSidra, cantDecimales) + " = "
				+ UtileriasJava.redondear(new BigDecimal(descuentoUnitarioSidraEnviado), cantDecimales));
		if (Country.PA.getCodArea().toString().compareTo(codArea) != 0
				&& UtileriasJava.redondear(new BigDecimal(descuentoUnitarioSidraEnviado), cantDecimales)
						.compareTo(UtileriasJava.redondear(descuentoTotalArticuloSidra, cantDecimales)) != 0) {
			errorDescuentoTotal = true;
			objArt = new VentaDet();
			objArt.setArticulo(new BigDecimal(articulo));
			objArt.setSerie(serie);
			objArt.setTipo_inv(tipoInvArticulo);
			lstArticulosFallidosOf.add(objArt);
			return lstArticulosFallidosOf;
		} else {
			objArt = new VentaDet();
			objArt.setDescuento_sidra(descuentoTotalArticuloSidra);
			objArt.setDescuentosArticulo(lstDescuentosSidra);
			lstArticulosFallidosOf.add(objArt);
			return lstArticulosFallidosOf;
		}
	}

	public List<VentaDet> validaPromocionales(Connection conn, InputVenta objDatos, int tipoVenta,
			BigDecimal bodegaRutaPanel, String estadoAlta, String estadoDisponible, boolean modoOnline,
			BigDecimal idPais) throws SQLException {

		Inventario objInv;

		List<VentaDet> lstArticulosFallidos = new ArrayList<VentaDet>();
		List<VentaDet> lstArticulosFallidosOf = new ArrayList<VentaDet>();
		List<VentaDet> lstArticulosFallidosCant = new ArrayList<VentaDet>();
		List<VentaDet> lstArticulos = new ArrayList<VentaDet>();
		VentaDet objArt = new VentaDet();
		BigDecimal existeOFC = null;
		String tipoInvSidra = "";

		// obteniendo par\u00E9metros
		tipoInvSidra = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPOINV, Conf.SOL_TIPOINV_SIDRA,
				objDatos.getCodArea());

		if (!objDatos.getArticulosPromocionales().isEmpty()) {
			for (int a = 0; a < objDatos.getArticulosPromocionales().size(); a++) {

				objInv = OperacionVentas.getArticulo(conn, estadoDisponible, null, objDatos.getIdBodegaVendedor(),
						objDatos.getArticulosPromocionales().get(a).getArticuloPromocional(), tipoInvSidra, false, null,
						objDatos.getCodArea(), idPais);

				if (objInv == null) {
					hayArtPromoFallido = true;
					objArt = new VentaDet();
					objArt.setArticulo(
							new BigDecimal(objDatos.getArticulosPromocionales().get(a).getArticuloPromocional()));
					objArt.setSerie(null);
					objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
					lstArticulosFallidos.add(objArt);
				} else {

					// primero se verificar\u00E9 si existe el idOferta del cual se indica que viene
					// el descuento sidra
					existeOFC = OperacionVentas.existeCampania(conn,
							objDatos.getArticulosPromocionales().get(a).getArticuloPromocional(),
							objDatos.getArticulosPromocionales().get(a).getIdOfertaCampania(), estadoAlta, modoOnline,
							idPais);
					if (existeOFC.intValue() == 0) {
						hayArtFallidoOfertaPromo = true;

						objArt = new VentaDet();
						objArt.setArticulo(
								new BigDecimal(objDatos.getArticulosPromocionales().get(a).getArticuloPromocional()));
						objArt.setSerie(" ");
						objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
						lstArticulosFallidosOf.add(objArt);
					} else {
						// para art\u00EDculos no seriados
						objArt.setSerie(null);
						objArt.setSerie_asociada(null);
						if (objInv.getCantidad()
								.intValue() < new BigDecimal(objDatos.getArticulosPromocionales().get(a).getCantidad())
										.intValue()) {
							hayArtFallidoCantPromo = true;
							objArt = new VentaDet();
							objArt.setArticulo(new BigDecimal(objDatos.getArticulos().get(a).getArticulo()));
							objArt.setSerie(null);
							objArt.setTipo_inv(objDatos.getArticulos().get(a).getTipoInv());
							lstArticulosFallidosCant.add(objArt);
						}
						objArt = new VentaDet();
						objArt.setArticulo(
								new BigDecimal(objDatos.getArticulosPromocionales().get(a).getArticuloPromocional()));
						objArt.setCantidad(new BigDecimal(objDatos.getArticulosPromocionales().get(a).getCantidad()));
						objArt.setCreado_por(objDatos.getUsuario());
						objArt.setEstado(estadoAlta);
						objArt.setImpuesto(new BigDecimal(0));
						objArt.setTipo_inv(tipoInvSidra);
						objArt.setTipo_grupo_sidra(objDatos.getArticulosPromocionales().get(a).getTipoGrupoSidra());

						objArt.setPrecio_unitario(new BigDecimal(0));
						objArt.setPrecio_final(new BigDecimal(0));
						objArt.setGestion(null);

						objArt.setDescuento_sidra(new BigDecimal(0));
						objArt.setDescuento_scl(new BigDecimal(0));
						objArt.setTcscofertacampaniaid(
								new BigDecimal(objDatos.getArticulosPromocionales().get(a).getIdOfertaCampania()));
						objArt.setBodega_panel_ruta(bodegaRutaPanel);
						objArt.setTcscbodegavirtualid(new BigDecimal(objDatos.getIdBodegaVendedor()));
					}

					lstArticulos.add(objArt);
				}
			}
		}

		if (hayArtPromoFallido) {
			return lstArticulosFallidos;
		} else if (hayArtFallidoOfertaPromo) {
			return lstArticulosFallidosOf;
		} else if (hayArtFallidoCantPromo) {
			return lstArticulosFallidosCant;
		} else {
			return lstArticulos;
		}
	}

	/**
	 * verifica si el impuesto PERCEPCION aplicaria
	 */
	public boolean validaPercepcion(InputVenta objDatos, BigDecimal montoMinimo) {
		BigDecimal totalFisico = BigDecimal.ZERO;
		boolean respuesta = false;
		int res;
		log.trace("suma para percepcion");
		log.trace("monto m\u00EDnimo:" + montoMinimo);
		if (!objDatos.getArticulos().isEmpty()) {
			log.trace("tamanio objetos:" + objDatos.getArticulos());

			for (int a = 0; a < objDatos.getArticulos().size(); a++) {
				if ("TARJETASRASCA".equalsIgnoreCase(objDatos.getArticulos().get(a).getTipoGrupoSidra())
						|| "TERMINAL".equalsIgnoreCase(objDatos.getArticulos().get(a).getTipoGrupoSidra())
						|| "SIMCARD".equalsIgnoreCase(objDatos.getArticulos().get(a).getTipoGrupoSidra())
						|| "KIT".equalsIgnoreCase(objDatos.getArticulos().get(a).getTipoGrupoSidra())) {
					log.trace("tipo grupo sidra:" + objDatos.getArticulos().get(a).getTipoGrupoSidra());
					totalFisico = totalFisico.add(new BigDecimal(objDatos.getArticulos().get(a).getPrecioTotal()), mc);
				}
			}
		}

		res = totalFisico.compareTo(montoMinimo);
		log.trace("respuesta comparacion monto m\u00EDnimo:" + res);
		log.trace("totalFisico:" + totalFisico);
		log.trace("pequeniocontribuyente:" + pequenioContribuyente);
		log.trace("tipoDocumento:" + objDatos.getTipoDocumento());
		if (res >= 0 && "CCF".equalsIgnoreCase(objDatos.getTipoDocumento()) && pequenioContribuyente) {
			log.trace("aplica Percepcion");
			respuesta = true;
		}

		log.trace("respuesta de validapercepcion:" + respuesta);
		return respuesta;

	}

	public void notificarOLS(InputVenta inVenta, OutputVenta outVenta) {
		try (Connection conn = getConnRegional()) {
			OlsDAO ols = new OlsDAO();

			double totalIva = 0;
			double totalCesc = 0;
			boolean enviarFactura =  Boolean.parseBoolean(inVenta.getEnviarLinkFactura());
			for (ArticuloVenta articulo : inVenta.getArticulos()) {

				for (Impuesto impuesto : articulo.getImpuestosArticulo()) {
					if ("IVA".equalsIgnoreCase(impuesto.getNombreImpuesto())) {
						totalIva += Double.valueOf(impuesto.getValor());
					} else if ("CES".equalsIgnoreCase(impuesto.getNombreImpuesto())) {
						totalCesc += Double.valueOf(impuesto.getValor());
					}
				}
			}
		
			ols.addOLSNotification(conn, new BigDecimal(outVenta.getIdVenta()), inVenta.getTelFactura(),
					Double.valueOf(inVenta.getMontoPagado())-totalIva-totalCesc, new Double("0.0"), new Double("0.0"), totalIva,
					Double.valueOf(Double.valueOf(inVenta.getMontoPagado())), new Double("0.0"), new Double("0.0"),
					totalCesc, Double.valueOf(inVenta.getDescuentoTotal()==null?"0.0":inVenta.getDescuentoTotal()), enviarFactura?null:"NO_ENVIAR","REGISTRADO_PARA_ANULAR".equalsIgnoreCase(inVenta.getEstado())?"ANULADA":"REGISTRADA");
		} catch (SQLException e) {
			log.error("Ocurrio un problema al notificar OLS.", e);
		}

	}
	public DetallePago registrarRefund(DetallePago objDatos) {
		DetallePago response=new DetallePago();
		log.trace("METODO RegistrarRefund json: "+ objDatos.getIdPayment());
		try (Connection conn = getConnRegional()) {
		
		 int resu=	OperacionVentas.updateDetPagoRefund(conn, objDatos.getIdPayment());
		 System.out.println("resultado update refund= "+ resu);
			//conn.setAutoCommit(false);
		//getIdPais(conn, objDatos.getCodArea());
			if (resu>0) {
				response.setEstadoSincronizacion(String.valueOf(resu));
				return response;
			}

		} catch (Exception e) {
			log.trace("EXCEPCION EN METODO registrarRefund: " + e.toString());
			return response;
		}
		
		
		return response;
		
	}
}

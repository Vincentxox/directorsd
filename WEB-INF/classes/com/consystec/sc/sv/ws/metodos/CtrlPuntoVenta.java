package com.consystec.sc.sv.ws.metodos;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.fichacliente.InputFichaCliente;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.pdv.InputBajaPDV;
import com.consystec.sc.ca.ws.input.pdv.InputConsultaPDV;
import com.consystec.sc.ca.ws.input.pdv.InputPDV;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.fichacliente.OutputFichaCliente;
import com.consystec.sc.ca.ws.output.pdv.OutputpdvDirec;
import com.consystec.sc.sv.ws.operaciones.OperacionPuntoVenta;
import com.consystec.sc.sv.ws.operaciones.OperacionRutaPdv;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.DiaVisita;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.EncargadoPDV;
import com.consystec.sc.sv.ws.orm.NumRecarga;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.RutaPDV;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.ericsson.sdr.dao.PaisDAO;
import com.ericsson.sdr.dao.VendedorDtsDAO;
import com.ericsson.sdr.dto.PaisDTO;
import com.ericsson.sdr.dto.VendedorDtsDTO;
import com.ericsson.sdr.dto.payment.PaymentExceptionCode;
import com.ericsson.sdr.dto.payment.QueryDailyTotalTransactionRequest;
import com.ericsson.sdr.dto.payment.QueryDailyTotalTransactionResponse;
import com.ericsson.sdr.exceptions.PaymentException;
import com.ericsson.sdr.service.PaymentServiceImpl;
import com.ericsson.sdr.utils.Constants;

public class CtrlPuntoVenta extends ControladorBase {

	/**
	 * @author susana barrios Consystec 2015
	 */
	private static final Logger log = Logger.getLogger(CtrlPuntoVenta.class);
	private static String servicioPost = Conf.LOG_POST_PDV;
	private static String servicioPut = Conf.LOG_PUT_PDV;
	private static String servicioGet = Conf.LOG_GET_PDV;
	private static String servicioCount = Conf.LOG_COUNT_PDV;

	/***
	 * Validando que no vengan par\u00E9metros nulos
	 * 
	 * @param conn
	 * @param estadoAlta
	 * 
	 * @throws SQLException
	 */
	public Respuesta validarDatos(Connection conn, InputPDV objDatos, int tipoMetodo, String estadoAlta,
			BigDecimal ID_PAIS) throws SQLException {
		String nombreMetodo = "validarDatos";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta objRespuesta = null;
		String documentoFiscal = "";
		String infFiscal = "";
		String origenWeb = "";
		String origenMovil = "";
		HashMap<String, String> camposPDV;
		HashMap<String, String> camposEncargado;

		documentoFiscal = UtileriasJava.getConfig(conn, Conf.GRUPO_DOC_FISCAL, Conf.DOCUMENTO_FISCAL,
				objDatos.getCodArea());
		infFiscal = UtileriasJava.getConfig(conn, Conf.GRUPO_SECCIONES_CAMPOS_PDV, Conf.INFORMACION_FISCAL,
				objDatos.getCodArea());
		// obteniendo los valores permitidos para este pa\u00EDs
		camposPDV = getGrupoParam(conn, estadoAlta, Conf.GRUPO_CAMPOS_PDV, ID_PAIS);
		camposEncargado = getGrupoParam(conn, estadoAlta, Conf.GRUPO_CAMPOS_ENCARGADO_PDV, ID_PAIS);
		origenWeb = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_ORIGEN, Conf.SOL_ORIGEN_PC,
				objDatos.getCodArea());
		origenMovil = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_ORIGEN, Conf.SOL_ORIGEN_MOVIL,
				objDatos.getCodArea());

		if (objDatos.getOrigen() == null || "".equals(objDatos.getOrigen().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_ORIGEN_NULO_75, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());

		} else {
			if (!objDatos.getOrigen().equalsIgnoreCase(origenMovil)
					&& !objDatos.getOrigen().equalsIgnoreCase(origenWeb)) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_ORIGEN_INVALIDO_516, null, nombreClase, nombreMetodo, null,
						objDatos.getCodArea());
			} else if ((objDatos.getOrigen().equalsIgnoreCase(origenMovil) && tipoMetodo == Conf.METODO_POST)
					&& (objDatos.getIdRuta() == null || "".equals(objDatos.getIdRuta().trim()))) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDRUTA_NULO_536, null, nombreClase, nombreMetodo, null,
						objDatos.getCodArea());
			}
		}

		if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_NOMBRE))
				&& (objDatos.getNombrePDV() == null || "".equals(objDatos.getNombrePDV().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOMBREPDV_NULO_9, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getDistribuidorAsociado() == null || "".equals(objDatos.getDistribuidorAsociado().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_DTSASOCIADO_NULO_10, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_TIPO_NEGOCIO))) {
			if (objDatos.getTipoNegocio() == null || "".equals(objDatos.getTipoNegocio().trim())) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPONEGOCIO_NULO_12, null, nombreClase, nombreMetodo, null,
						objDatos.getCodArea());
			} else {
				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_GRUPO + ")", Filtro.EQ,
						"'" + Conf.GRUPO_TIPOS_NEGOCIO_PDV + "'"));
				condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_VALOR + ")", Filtro.EQ,
						"'" + objDatos.getTipoNegocio().toUpperCase() + "'"));

				condiciones.add(new Filtro(Catalogo.CAMPO_TC_SC_CATPAIS_ID, Filtro.EQ, ID_PAIS));
				String tipoNegocio = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_VALOR, Catalogo.N_TABLA,
						condiciones);

				if (tipoNegocio == null || "".equals(tipoNegocio)) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIP_NEGOCIO_INVALIDO_497, null, nombreClase,
							nombreMetodo, null, objDatos.getCodArea());
				}
			}
		}

		/* agregando validaciones para nuevos campos */
		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_TIPO_PRODUCTO))) {
			if (objDatos.getTipoProducto() == null || "".equals(objDatos.getTipoProducto().trim())) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOPRODUCTO_NULO_482, null, nombreClase, nombreMetodo,
						null, objDatos.getCodArea());
			} else {

				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_GRUPO + ")", Filtro.EQ,
						"'" + Conf.GRUPO_TIPO_PRODUCTO_PDV + "'"));
				condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_VALOR + ")", Filtro.EQ,
						"'" + objDatos.getTipoProducto().toUpperCase() + "'"));

				condiciones.add(new Filtro(Catalogo.CAMPO_TC_SC_CATPAIS_ID, Filtro.EQ, ID_PAIS));

				String tipoProducto = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_VALOR, Catalogo.N_TABLA,
						condiciones);
				if (tipoProducto == null || "".equals(tipoProducto)) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIP_PRODUCTO_INVALIDO_500, null, nombreClase,
							nombreMetodo, null, objDatos.getCodArea());
				}
			}
		}

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_CANAL))) {
			if (objDatos.getCanal() == null || "".equals(objDatos.getCanal().trim())) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_CANAL_NULO_483, null, nombreClase, nombreMetodo, null,
						objDatos.getCodArea());
			} else {
				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(
						new Filtro("UPPER(" + Catalogo.CAMPO_GRUPO + ")", Filtro.EQ, "'" + Conf.GRUPO_CANAL_PDV + "'"));
				condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_VALOR + ")", Filtro.EQ,
						"'" + objDatos.getCanal().toUpperCase() + "'"));

				condiciones.add(new Filtro(Catalogo.CAMPO_TC_SC_CATPAIS_ID, Filtro.EQ, ID_PAIS));
				String canal = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_VALOR, Catalogo.N_TABLA, condiciones);
				if (canal == null || "".equals(canal)) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_CANAL_INVALIDO_501, null, nombreClase, nombreMetodo,
							null, objDatos.getCodArea());
				}
			}
		}
		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_SUBCANAL))) {
			if (objDatos.getSubcanal() == null || "".equals(objDatos.getSubcanal().trim())) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_SUBCANAL_NULO_484, null, nombreClase, nombreMetodo, null,
						objDatos.getCodArea());
			} else {
				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_GRUPO + ")", Filtro.EQ,
						"'" + Conf.GRUPO_SUBCANAL_PDV + "'"));
				condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_VALOR + ")", Filtro.EQ,
						"'" + objDatos.getSubcanal().toUpperCase() + "'"));

				condiciones.add(new Filtro(Catalogo.CAMPO_TC_SC_CATPAIS_ID, Filtro.EQ, ID_PAIS));
				String subCanal = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_VALOR, Catalogo.N_TABLA, condiciones);
				if (subCanal == null || "".equals(subCanal)) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_SUBCANAL_INVALIDO_502, null, nombreClase, nombreMetodo,
							null, objDatos.getCodArea());
				}
			}
		}

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_CATEGORIA))) {
			log.trace("categoria:" + objDatos.getCategoria());
			if (objDatos.getCategoria() == null || "".equals(objDatos.getCategoria().trim())) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_CATEGORIA_NULO_485, null, nombreClase, nombreMetodo, null,
						objDatos.getCodArea());
			} else {
				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_GRUPO + ")", Filtro.EQ,
						"'" + Conf.GRUPO_CATEGORIA_PDV + "'"));
				condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_NOMBRE + ")", Filtro.EQ,
						"'" + objDatos.getCategoria().toUpperCase() + "'"));
				condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_VALOR + ")", Filtro.EQ,
						"'" + objDatos.getCategoria().toUpperCase() + "'"));

				condiciones.add(new Filtro(Catalogo.CAMPO_TC_SC_CATPAIS_ID, Filtro.EQ, ID_PAIS));
				String categoria = "";
				categoria = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_VALOR, Catalogo.N_TABLA, condiciones);
				if (categoria == null || "".equals(categoria)) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_CAT_INVALIDA_498, null, nombreClase, nombreMetodo, null,
							objDatos.getCodArea());
				}
			}
		}

		if ("1".equals(camposPDV.get(Conf.CAMPO_ENCARGADO))) {
			if (objDatos.getEncargado() == null) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_INFO_ENCARGADO_NULO_496, null, nombreClase, nombreMetodo,
						null, objDatos.getCodArea());
			} else {
				if ("1".equals(camposEncargado.get(EncargadoPDV.CAMPO_NOMBRES))) {
					if (objDatos.getEncargado().getNombres() == null
							|| "".equals(objDatos.getEncargado().getNombres().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOMBRES_ENCARGADOPDV_NULO_486, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
					}
				} else if ("0".equals(camposEncargado.get(EncargadoPDV.CAMPO_NOMBRES))) {
					objDatos.getEncargado().setNombres(null);
				}
				if ("1".equals(camposEncargado.get(EncargadoPDV.CAMPO_APELLIDOS))) {
					if (objDatos.getEncargado().getApellidos() == null
							|| "".equals(objDatos.getEncargado().getApellidos().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_APELLIDOS_ENCARGADOPDV_NULO_487, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
					}
				} else if ("0".equals(camposEncargado.get(EncargadoPDV.CAMPO_APELLIDOS))) {
					objDatos.getEncargado().setApellidos(null);
				}

				if (!"507".equals(objDatos.getCodArea())
						&& "1".equals(camposEncargado.get(EncargadoPDV.CAMPO_CEDULA))) {
					if (objDatos.getEncargado().getCedula() == null || "".equals(objDatos.getEncargado().getCedula())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_CEDULA_NULO_508, null, nombreClase, nombreMetodo,
								null, objDatos.getCodArea());
					}
				} else if ("0".equals(camposEncargado.get(EncargadoPDV.CAMPO_CEDULA))) {
					objDatos.getEncargado().setCedula(null);
				}

				if ("1".equals(camposEncargado.get(EncargadoPDV.CAMPO_TELEFONO))) {
					if (objDatos.getEncargado().getTelefono() == null
							|| "".equals(objDatos.getEncargado().getTelefono().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_TEL_ENCARGADOPDV_NULO_488, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
					} else {
						if (objDatos.getEncargado().getTelefono().length() != 8) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONG_NUM_INVALIDA_504, null, nombreClase,
									nombreMetodo, null, objDatos.getCodArea());
						}
					}
				} else if ("0".equals(camposEncargado.get(EncargadoPDV.CAMPO_TELEFONO))) {
					objDatos.getEncargado().setTelefono(null);
				}
			}
		}

		/* TODO COMENTADO PARA EL SALVADOR REVISAR */
		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_CALLE))
				&& (objDatos.getCalle() == null || "".equals(objDatos.getCalle().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_CALLE_NULO_489, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_AVENIDA))
				&& (objDatos.getAvenida() == null || "".equals(objDatos.getAvenida().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_AVENIDA_NULO_490, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_PASAJE))
				&& (objDatos.getPasaje() == null || "".equals(objDatos.getPasaje().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_PASAJE_NULO_491, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_CASA))
				&& (objDatos.getCasa() == null || "".equals(objDatos.getCasa().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_CASA_NULO_492, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_COLONIA))
				&& (objDatos.getColonia() == null || "".equals(objDatos.getColonia().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_COLONIA_NULO_493, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_BARRIO))
				&& (objDatos.getBarrio() == null || "".equals(objDatos.getBarrio().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_BARRIO_NULO_506, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_REFERENCIA))
				&& (objDatos.getReferencia() == null || "".equals(objDatos.getReferencia().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_REFERENCIA_NULO_494, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_DOCUMENTO))
				&& (objDatos.getDocumento() == null || "".equals(objDatos.getDocumento().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_DOC_NULO_13, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		/*
		 * *****************************************************************************
		 * *********************
		 */
		// inician campos de informaci\u00F3n fiscal
		log.trace("documento fiscal:" + documentoFiscal);
		if ("1".equals(infFiscal)) {
			if (documentoFiscal.equalsIgnoreCase(Conf.DOC_FISCAL_GENERICO)
					|| documentoFiscal.equalsIgnoreCase(objDatos.getDocumento())) {
				if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_NIT))
						&& (objDatos.getNit() == null || "".equals(objDatos.getNit().trim()))) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_NIT_NULO_14, null, nombreClase, nombreMetodo, null,
							objDatos.getCodArea());
				}

				if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_NOMBRE_FISCAL))) {
					if (objDatos.getNombreFiscal() == null || "".equals(objDatos.getNombreFiscal().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOMBREFISCAL_NULO_15, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
					}
				} else if ("0".equals(camposPDV.get(PuntoVenta.CAMPO_NOMBRE_FISCAL))) {
					objDatos.setNombreFiscal(null);
				}

				if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_REGISTRO_FISCAL))) {
					if (objDatos.getRegistroFiscal() == null || "".equals(objDatos.getRegistroFiscal().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_REGFISCAL_NULO_16, null, nombreClase, nombreMetodo,
								null, objDatos.getCodArea());
					}
				} else if ("0".equals(camposPDV.get(PuntoVenta.CAMPO_REGISTRO_FISCAL))) {
					objDatos.setRegistroFiscal(null);
				}

				log.trace("valor campo digito validador: " + camposPDV.get(PuntoVenta.CAMPO_DIGITO_VALIDADOR));
				if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_DIGITO_VALIDADOR).trim())) {
					if (objDatos.getDigitoValidador() == null || "".equals(objDatos.getDigitoValidador().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_DIG_VALIDADOR_NULO_507, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
					}
				} else if ("0".equals(camposPDV.get(PuntoVenta.CAMPO_DIGITO_VALIDADOR).trim())) {
					log.trace("setea null digito validador");
					objDatos.setDigitoValidador(null);
				}

				if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_GIRO_NEGOCIO))) {
					if (objDatos.getGiroNegocio() == null || "".equals(objDatos.getGiroNegocio().trim())) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_GIRONEGOCIO_NULO_17, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
					}
				} else if ("0".equals(camposPDV.get(PuntoVenta.CAMPO_GIRO_NEGOCIO))) {
					objDatos.setGiroNegocio(null);
				}
			} else {
				if ("503".equals(objDatos.getCodArea())) {
					objDatos.setGiroNegocio(null);
					objDatos.setDigitoValidador(null);
					objDatos.setRegistroFiscal(null);
					objDatos.setNombreFiscal(null);
				}
			}
		}

		// aqui termina los campos de informaci\u00F3n fiscal
		/*
		 * *****************************************************************************
		 * *********************
		 */

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_TIPO_CONTRIBUYENTE))) {
			if (objDatos.getTipoContribuyente() == null || "".equals(objDatos.getTipoContribuyente().trim())) {
				log.trace("tipoContribuyente vac\u00EDo");
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIPOCONTRIBUYENTE_NULO_495, null, nombreClase, nombreMetodo,
						null, objDatos.getCodArea());
			} else {
				log.trace("tipoContribuyente no vac\u00EDo");
				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_GRUPO + ")", Filtro.EQ,
						"'" + Conf.GRUPO_TIPO_CONTRIBUYENTE_PDV + "'"));
				condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_VALOR + ")", Filtro.EQ,
						"'" + objDatos.getTipoContribuyente().toUpperCase() + "'"));
				condiciones.add(new Filtro(Catalogo.CAMPO_TC_SC_CATPAIS_ID, Filtro.EQ, ID_PAIS));
				String tipoContribuyente = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_VALOR, Catalogo.N_TABLA,
						condiciones);
				if (tipoContribuyente == null || "".equals(tipoContribuyente)) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_TIP_CONTRIBUYENTE_INVALIDO_499, null, nombreClase,
							nombreMetodo, null, objDatos.getCodArea());
				}
			}
		}

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_DIRECCION))
				&& (objDatos.getDireccion() == null || "".equals(objDatos.getDireccion().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_DIRECCION_NULO_18, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}
		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_DEPARTAMENTO))
				&& (objDatos.getDepartamento() == null || "".equals(objDatos.getDepartamento().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_DEPTO_NULO20, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if ("1".equals(camposPDV.get(PuntoVenta.CAMPO_MUNICIPIO))
				&& (objDatos.getMunicipio() == null || "".equals(objDatos.getMunicipio().trim()))) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_MUNI_NULO_21, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getDias() == null || objDatos.getDias().length < 1) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_DIASVISITA_NULO_23, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		} else {
			for (int a = 0; a < objDatos.getDias().length; a++) {
				if (objDatos.getDias()[a] == null || "".equals(objDatos.getDias()[a].trim())) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_DIASVISITA_NULO_23, null, nombreClase, nombreMetodo,
							null, objDatos.getCodArea());
				}
			}
		}

		// VALIDANDO N\u00FAMEROS DE RECARGA
		if (objDatos.getTelefonoRecargo() == null || objDatos.getTelefonoRecargo().length < 1) {
			log.trace("1");
		} else {
			for (int a = 0; a < objDatos.getTelefonoRecargo().length; a++) {
				if (objDatos.getTelefonoRecargo()[a].getNumero() == null
						|| "".equals(objDatos.getTelefonoRecargo()[a].getNumero())) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_TELRECARGO_NULO_24, null, nombreClase, nombreMetodo,
							null, objDatos.getCodArea());
				} else if (objDatos.getTelefonoRecargo()[a].getNumero().length() != 8) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGNUM_INVALIDA_27, null, nombreClase, nombreMetodo,
							"Número:" + objDatos.getTelefonoRecargo()[a].getNumero(), objDatos.getCodArea());
				}

				if (objDatos.getTelefonoRecargo()[a].getOrden() == null
						|| "".equals(objDatos.getTelefonoRecargo()[a].getOrden())) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_ORDEN_NUMRECARGA_NULO_30, null, nombreClase,
							nombreMetodo, "Número:" + objDatos.getTelefonoRecargo()[a].getNumero(),
							objDatos.getCodArea());
				}
			}
		}

		if (objDatos.getOrigen() != null ) {
			if (objDatos.getLatitud() == null || "".equals(objDatos.getLatitud().trim())) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_LATITUD_NULO_25, null, nombreClase, nombreMetodo, null,
						objDatos.getCodArea());
			} else if (!isValido(objDatos.getLatitud()) || (objDatos.getLatitud().equals("0"))) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_LAT_INVALIDA_29, null, nombreClase, nombreMetodo, null,
						objDatos.getCodArea());
			}

			if (objDatos.getLongitud() == null || "".equals(objDatos.getLongitud().trim())) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONG_NULO_26, null, nombreClase, nombreMetodo, null,
						objDatos.getCodArea());
			} else if (!isValido(objDatos.getLongitud()) || (objDatos.getLongitud().equals("0"))) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONG_INVALIDA_28, null, nombreClase, nombreMetodo, null,
						objDatos.getCodArea());
			}
		}

		if (objDatos.getQr() != null && !"".equals(objDatos.getQr().trim())) {
			List<Filtro> condiciones = new ArrayList<Filtro>();
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCCATPAISID,
					ID_PAIS.toString()));
			condiciones.add(
					UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PuntoVenta.CAMPO_QR, objDatos.getQr()));
			if (tipoMetodo == Conf.METODO_PUT && objDatos.getIdPDV() != null && !"".equals(objDatos.getIdPDV().trim())
					&& isNumeric(objDatos.getIdPDV())) {
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND_NEQ,
						PuntoVenta.CAMPO_TCSCPUNTOVENTAID, objDatos.getIdPDV()));
			}
			int existeQR = UtileriasBD.selectCount(conn, PuntoVenta.N_TABLA, condiciones);
			if (existeQR > 0) {
				objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_EXISTE_QR_902, null, nombreClase, nombreMetodo, null,
						objDatos.getCodArea());
			}
		}

		if (objRespuesta != null) {
			log.trace("resultado:" + objRespuesta.getDescripcion());
		}

		return objRespuesta;
	}

	/**
	 * M\u00E9todo para validar input para cambiar de estado
	 */
	public Respuesta validarDatosBaja(InputBajaPDV objDatos) {
		String nombreMetodo = "validarDatosBaja";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta objRespuesta = null;

		if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getIdPDV() == null || "".equals(objDatos.getIdPDV().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDPDV_VACIO_8, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		if (objDatos.getEstado() == null || "".equals(objDatos.getEstado().trim())) {
			objRespuesta = getMensaje(Conf_Mensajes.MSJ_ESTADO_NULO_11, null, nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
		}

		return objRespuesta;
	}

	/**************************************************************************
	 * M\u00E9todo para insertar un nuevo punto de venta
	 * 
	 * @throws SQLException
	 */
	public OutputpdvDirec insertarPDV(InputPDV objDatos) {
		List<LogSidra> listaLog = new ArrayList<LogSidra>();
		String nombreMetodo = "insertarPDV";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta objRespuesta = new Respuesta();
		OutputpdvDirec salidaPDV = new OutputpdvDirec();
		PuntoVenta objNewPdv = new PuntoVenta();
		RutaPDV objeto = new RutaPDV();
		EncargadoPDV objEncargadoPdv = new EncargadoPDV();
		DiaVisita diaVisita = new DiaVisita();
		List<DiaVisita> listaDias = new ArrayList<DiaVisita>();
		NumRecarga numeros = new NumRecarga();
		List<NumRecarga> lstnumeros = new ArrayList<NumRecarga>();
		List<Filtro> condiciones = new ArrayList<Filtro>();
		String razon = "";
		BigDecimal existenit = null;
		String numRepetido = "";
		BigDecimal idPDV = null;
		BigDecimal idEncargadoPDV = null;
		int respinsert = 0;
		Connection conn = null;
		String estadoActivo = "";
		String estadoAlta = "";
		String estadoBaja = "";
		String tipoPDV = "";
		String zonaComercial = "";
		BigDecimal existeDTS = null;

		try {
			conn = getConnRegional();
			BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());
			conn.setAutoCommit(false);

			estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());

			if (!"503".equals(objDatos.getCodArea())) {
				String codCuentaDTS = getCodCuentaDTS(conn, objDatos.getDistribuidorAsociado(), estadoAlta, ID_PAIS);
				if (codCuentaDTS.equals("")) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_CODCUENTA_DTS_857, null, nombreClase,
							nombreMetodo, null, objDatos.getCodArea());
					return salidaPDV;
				}
			}
			log.trace("inicia a validar valores...");
			objRespuesta = validarDatos(conn, objDatos, Conf.METODO_POST, estadoAlta, ID_PAIS);
			if (objRespuesta == null) {
				log.trace("inicia el proceso de inserci\u00F3n de datos");
				estadoActivo = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_ESTADOS_PDV, Conf.ESTADO_ACTIVO,
						objDatos.getCodArea());
				estadoBaja = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, objDatos.getCodArea());
				tipoPDV = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELPDV, Conf.TIPO_PDV, objDatos.getCodArea());

				existeDTS = OperacionPuntoVenta.existeDts(conn, objDatos.getDistribuidorAsociado(), estadoAlta,
						ID_PAIS);

				if (existeDTS.intValue() == 0) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_DTS_NOEXISTE_BAJA_88, null, nombreClase, nombreMetodo,
							null, objDatos.getCodArea());

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PDV, servicioPost, "0",
							Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
							objRespuesta.getDescripcion()));

				} else {
					// obteniendo id de PVD
					idPDV = JavaUtils.getSecuencia(conn, PuntoVenta.SEQUENCE);
					log.trace("idPDV:" + idPDV);
					// asignando valores a objeto a insertar de pdv
					objNewPdv.setTcscpuntoventaid(idPDV);
					objNewPdv.setTcscdtsid(new BigDecimal(objDatos.getDistribuidorAsociado()));
					objNewPdv.setTcsccatpaisid(ID_PAIS);
					objNewPdv.setNombre(objDatos.getNombrePDV());
					objNewPdv.setEstado(estadoActivo);
					objNewPdv.setTipo_negocio(objDatos.getTipoNegocio());
					objNewPdv.setDocumento(objDatos.getDocumento());
					objNewPdv.setNit(objDatos.getNit());
					objNewPdv.setRegistro_fiscal(objDatos.getRegistroFiscal());
					objNewPdv.setNombre_fiscal(objDatos.getNombreFiscal());
					objNewPdv.setGiro_negocio(objDatos.getGiroNegocio());
					objNewPdv.setDireccion(objDatos.getDireccion());
					objNewPdv.setDepartamento(objDatos.getDepartamento());
					objNewPdv.setMunicipio(objDatos.getMunicipio());
					objNewPdv.setDistrito(objDatos.getDistrito());
					objNewPdv.setObservaciones(objDatos.getObservaciones());
					objNewPdv.setCreado_por(objDatos.getUsuario());

					objNewPdv.setLongitud(objDatos.getLongitud());
					objNewPdv.setLatitud(objDatos.getLatitud());
					// agregando nuevos campos 27-05-16 sbarrios
					objNewPdv.setTipo_producto(objDatos.getTipoProducto());
					objNewPdv.setCanal(objDatos.getCanal());
					objNewPdv.setSubcanal(objDatos.getSubcanal());
					objNewPdv.setCategoria(objDatos.getCategoria());
					objNewPdv.setCalle(objDatos.getCalle());
					objNewPdv.setAvenida(objDatos.getAvenida());
					objNewPdv.setPasaje(objDatos.getPasaje());
					objNewPdv.setColonia(objDatos.getColonia());
					objNewPdv.setCasa(objDatos.getCasa());
					objNewPdv.setReferencia(objDatos.getReferencia());
					objNewPdv.setTipo_contribuyente(objDatos.getTipoContribuyente());
					objNewPdv.setBarrio(objDatos.getBarrio());
					objNewPdv.setQr(objDatos.getQr());

					// AGREGANDO ZONA COMERCIAL
					condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_GRUPO + ")", Filtro.EQ,
							"'" + Conf.GRUPO_ZONA_COMERCIAL_REG + "'"));
					if ("503".equals(objDatos.getCodArea())) {
						condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_VALOR + ")", Filtro.EQ,
								"'" + Conf.ZONA_COMERCIAL_1 + "'"));
					} else {
						condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_VALOR + ")", Filtro.EQ,
								"'" + objDatos.getMunicipio().toUpperCase() + "'"));
					}
					condiciones.add(new Filtro(Catalogo.CAMPO_TC_SC_CATPAIS_ID, Filtro.EQ, ID_PAIS));
					zonaComercial = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_NOMBRE, Catalogo.N_TABLA,
							condiciones);
					objNewPdv.setTcsczonacomercialid(zonaComercial);

					log.trace("valor digito validador:" + objDatos.getDigitoValidador());
					if (objDatos.getDigitoValidador() == null) {
						objNewPdv.setDigito_validador(null);
					} else {
						objNewPdv.setDigito_validador(new BigDecimal(objDatos.getDigitoValidador()));
					}

					objEncargadoPdv.setApellidos(objDatos.getEncargado().getApellidos());
					objEncargadoPdv.setNombres(objDatos.getEncargado().getNombres());
					objEncargadoPdv.setTelefono(new BigDecimal(objDatos.getEncargado().getTelefono()));
					objEncargadoPdv.setTcscpuntoventaid(idPDV);
					objEncargadoPdv.setEstado(estadoAlta);
					objEncargadoPdv.setCedula(objDatos.getEncargado().getCedula());
					objEncargadoPdv.setTipoDocumento(objDatos.getEncargado().getTipoDocumento());
					// fin agregado

					// Armando dias de visita
					for (int i = 0; i < objDatos.getDias().length; i++) {
						diaVisita = new DiaVisita();

						diaVisita.setEstado(estadoAlta);
						diaVisita.setCreado_por(objDatos.getUsuario());
						diaVisita.setNombre(objDatos.getDias()[i]);
						diaVisita.setTcscpuntoventaid(objNewPdv.getTcscpuntoventaid());
						listaDias.add(diaVisita);
					}

					// Armando numeros de recarga
					log.trace("Arma n\u00FAmeros recarga");
					if (objDatos.getTelefonoRecargo() != null) {
						for (int c = 0; c < objDatos.getTelefonoRecargo().length; c++) {

							/* validacion para que envien el orden de forma correcta */

							numeros = new NumRecarga();

							numeros.setNum_recarga(new BigDecimal(objDatos.getTelefonoRecargo()[c].getNumero()));
							numeros.setOrden(new BigDecimal(objDatos.getTelefonoRecargo()[c].getOrden()));
							numeros.setTipo(tipoPDV);
							numeros.setIdtipo(objNewPdv.getTcscpuntoventaid());
							numeros.setEstado_payment(objDatos.getTelefonoRecargo()[c].getEstadoPayment());
							numeros.setEstado(estadoAlta);
							numeros.setCreado_por(objDatos.getUsuario());
							lstnumeros.add(numeros);
						}
					}

					// validando que no existan n\u00FAmeros de recarga repetidos
					numRepetido = JavaUtils.validarNumeros(conn, lstnumeros, new BigDecimal(0), tipoPDV,
							objDatos.getCodArea(), ID_PAIS);
					log.trace("resultado de n\u00FAmeros repetidos:" + numRepetido);

					if ("".equals(numRepetido)) {

						UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, objDatos.getCodArea());

						// Validar si el numero existe en payment
						String numerosPayment = validaNumeroRecargaPayment(conn, objDatos, lstnumeros, UtileriasJava
								.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.PIN_DEFAULT, objDatos.getCodArea()));
						if (numerosPayment.isEmpty()) {
							log.trace("validad nit");

							existenit = OperacionPuntoVenta.validarNit(conn, objDatos.getNit(), estadoActivo,
									new BigDecimal(0), ID_PAIS);
							log.trace("resultado validacion nit:" + existenit);
							if (existenit.intValue() > 1) {
								razon = "NOTA: Otro punto de venta se encuentra registrado con el mismo n\u00FAmero de nit";
							}

							log.trace("Inicia a insertar valores");

							// se insertan valores
							respinsert = OperacionPuntoVenta.insertarPdv(conn, objNewPdv);

							if (respinsert > 0) {
								idEncargadoPDV = JavaUtils.getSecuencia(conn, EncargadoPDV.SEQUENCE);
								log.trace("idEncargadoPDV: " + idEncargadoPDV);
								objEncargadoPdv.setTcscencargadopdvid(idEncargadoPDV);
								OperacionPuntoVenta.insertDiaVisita(conn, listaDias);
								if (!lstnumeros.isEmpty()) {
									OperacionPuntoVenta.insertNumRecarga(conn, lstnumeros, estadoBaja, estadoAlta, 0,
											null, objDatos.getCodArea(), ID_PAIS, listaLog);
								}
								int insertoEncargado = OperacionPuntoVenta.inserEncargadoPdv(conn, objEncargadoPdv);
								log.trace("inserto encargado:" + insertoEncargado);

								// insertando asociaci\u00F3n ruta de pdv
								if (insertoEncargado > 0 && (!(objDatos.getIdRuta() == null
										|| "".equals(objDatos.getIdRuta().trim())))) {
									objeto = new RutaPDV();
									objeto.setTcscrutaid(new BigDecimal(objDatos.getIdRuta()));
									objeto.setCreado_por(objDatos.getUsuario());
									objeto.setTcscpuntoventaid(idPDV);
									objeto.setTcsccatpaisid(ID_PAIS);

									// insertando asociacion
									OperacionRutaPdv.insertAsociacion(conn, objeto, estadoAlta, ID_PAIS);
								}

								objRespuesta = getMensaje(Conf_Mensajes.OK_CREAPDV2, null, nombreClase, nombreMetodo,
										razon, objDatos.getCodArea());
								salidaPDV.setIdPDV(idPDV.toString());
								salidaPDV.setIdEncargadoPDV(idEncargadoPDV.toString());
								salidaPDV.setZonaComercial(objNewPdv.getTcsczonacomercialid().toString());

								Respuesta respFichaCliente = crearFichaCliente(conn, objDatos, idPDV.toString());
								log.trace("CodResultado: " + respFichaCliente.getCodResultado());
								if (new BigDecimal(respFichaCliente.getCodResultado()).intValue() < 0) {
									log.trace("fallo al crear la ficha de cliente");
								}

								log.trace("ficha cliente: ");

								conn.commit();
							}
						} else {
							// Respuesta de error al validar los números de payment
							objRespuesta = getMensaje(Conf_Mensajes.MSG_NUMERO_RECARGA_PAYMENT_INVALIDO, null,
									nombreClase, nombreMetodo, numerosPayment, objDatos.getCodArea());

							listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PDV, servicioPost, "0",
									Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
									objRespuesta.getDescripcion()));
						}

					} else {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_NUMRECARGAREPETIDO_32, null, nombreClase,
								nombreMetodo, numRepetido, objDatos.getCodArea());

						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PDV, servicioPost, "0",
								Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
								objRespuesta.getDescripcion()));
					}
				}
			} else {
				salidaPDV.setRespuesta(objRespuesta);

				listaLog.add(
						ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PDV, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
								"Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

				UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
			}

		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error(e1.getMessage(), e1);
			}

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PDV, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
					"Problema al crear nuevo punto de venta.", e.getMessage()));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null,
					objDatos.getCodArea());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error(e1.getMessage(), e1);
			}

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PDV, servicioPost, "0", Conf.LOG_TIPO_NINGUNO,
					"Problema al crear nuevo punto de venta.", e.getMessage()));
		} finally {
			DbUtils.closeQuietly(conn);
			salidaPDV.setRespuesta(objRespuesta);

			UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
		}

		return salidaPDV;
	}

	private Respuesta crearFichaCliente(Connection conn, InputPDV input, String idPDV) throws SQLException {
		InputFichaCliente inputFicha = new InputFichaCliente();

		/* para crear cliente con servicios de FS */
		String municipio = null;
		String departamento = null;
		if ("503".equals(input.getCodArea())) {
			municipio = getIdFS(conn, "MUNICIPIOS_SV", input.getMunicipio());
			departamento = getIdFS(conn, "DEPARTAMENTOS_SV", input.getDepartamento());
		}
		if ("507".equals(input.getCodArea())) {
			municipio = getIdFS(conn, "DISTRITOS_PA", input.getMunicipio());
			departamento = getIdFS(conn, "PROVINCIAS_PA", input.getDepartamento());
		}
		String direccion = input.getCalle() + " " + input.getAvenida() + " " + input.getPasaje() + " "
				+ input.getCasa();
		inputFicha.setTipoCliente(Conf.FICHA_CLIENTE_TIPO_PDV);

		inputFicha.setCodArea(input.getCodArea());
		inputFicha.setUsuario(input.getUsuario());
		inputFicha.setMunicipio(municipio);
		inputFicha.setDepartamento(departamento);
		inputFicha.setDireccion(direccion);
		inputFicha.setNit(input.getNit());
		inputFicha.setPrimerNombre(input.getTipoNegocio());
		inputFicha.setSegundoNombre(input.getNombrePDV());
		inputFicha.setPrimerApellido(input.getTipoNegocio());
		inputFicha.setSegundoApellido(input.getNombrePDV());
		inputFicha.setNoDocumento(input.getEncargado().getCedula());
		inputFicha.setTelContacto(input.getEncargado().getTelefono());

		inputFicha.setIdDts(input.getDistribuidorAsociado());
		inputFicha.setIdPdv(idPDV);

		OutputFichaCliente resp = new CtrlFichaCliente().getDatos(inputFicha, Conf.METODO_POST, true, conn);

		return resp.getRespuesta();
	}

	private String getIdFS(Connection conn, String grupo, String nombre) throws SQLException {
		String sql;
		String res = "";
		PreparedStatement pstm = null;
		ResultSet rst = null;

		sql = "SELECT valor ";
		sql = sql + "  FROM tc_sc_configuracion ";
		sql = sql + " WHERE grupo LIKE ? AND nombre LIKE ?";
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, grupo);
			pstm.setString(2, nombre);
			rst = pstm.executeQuery();

			if (rst.next()) {
				res = rst.getString("VALOR");
			}
		} finally {
			DbUtils.closeQuietly(rst);
			DbUtils.closeQuietly(pstm);
		}
		return res;
	}

	private String getCodCuentaDTS(Connection conn, String idDTS, String estadoAlta, BigDecimal ID_PAIS)
			throws SQLException {
		List<Filtro> condiciones = new ArrayList<Filtro>();
		condiciones
				.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TC_SC_DTS_ID, idDTS));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Distribuidor.CAMPO_ESTADO, estadoAlta));
		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TCSCCATPAISID,
				"" + ID_PAIS));
		String codCuentaDTS = UtileriasBD.getOneRecord(conn, Distribuidor.CAMPO_COD_CUENTA, Distribuidor.N_TABLA,
				condiciones);
		return codCuentaDTS == null ? "" : codCuentaDTS;
	}

	/**********************************************************************************************
	 * MODIFICAR PUNTO DE VENTA
	 ***********************************************************************************************/
	public OutputpdvDirec modificarPDV(InputPDV objDatos) {
		List<LogSidra> listaLog = new ArrayList<LogSidra>();
		String nombreMetodo = "modificarPDV";
		String nombreClase = new CurrentClassGetter().getClassName();
		OutputpdvDirec salidaPDV = new OutputpdvDirec();
		Respuesta objRespuesta = new Respuesta();

		PuntoVenta objNewPdv = new PuntoVenta();
		DiaVisita diaVisita = new DiaVisita();
		List<DiaVisita> listaDias = new ArrayList<DiaVisita>();
		NumRecarga numeros = new NumRecarga();
		List<NumRecarga> lstnumeros = new ArrayList<NumRecarga>();
		List<Filtro> condiciones = new ArrayList<Filtro>();
		EncargadoPDV objEncargadoPDV = new EncargadoPDV();
		String razon = "";
		BigDecimal existenit = null;
		String numRepetido = "";
		int respinsert = 0;
		Connection conn = null;
		String estadoActivo = "";
		String estadoAlta = "";
		String estadoBaja = "";
		String zonaComercial = "";
		String tipoPDV = "";
		BigDecimal existePDV = null;
		BigDecimal existeDTS = null;

		log.trace("inicia a validar valores...");
		try {
			conn = getConnRegional();
			conn.setAutoCommit(false);
			BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());

			estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());

			if (!"503".equals(objDatos.getCodArea())) {
				String codCuentaDTS = getCodCuentaDTS(conn, objDatos.getDistribuidorAsociado(), estadoAlta, ID_PAIS);
				if (codCuentaDTS.equals("")) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_CODCUENTA_DTS_857, null, nombreClase,
							nombreMetodo, null, objDatos.getCodArea());
					return salidaPDV;
				}
			}

			objRespuesta = validarDatos(conn, objDatos, Conf.METODO_PUT, estadoAlta, ID_PAIS);
			if (objRespuesta == null) {
				if (objDatos.getIdPDV() == null || "".equals(objDatos.getIdPDV().trim())
						|| !isNumeric(objDatos.getIdPDV())) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDPDV_VACIO_8, null, nombreClase, nombreMetodo,
							numRepetido, objDatos.getCodArea());

					listaLog.add(
							ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut, "0", Conf.LOG_TIPO_PDV,
									"Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

				} else if (objDatos.getEncargado().getIdEncargadoPDV() == null
						|| "".equals(objDatos.getEncargado().getIdEncargadoPDV())) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDENCARGADO_PDV_VACIO_503, null, nombreClase,
							nombreMetodo, numRepetido, objDatos.getCodArea());

					listaLog.add(
							ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut, "0", Conf.LOG_TIPO_PDV,
									"Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

				} else {
					existePDV = OperacionPuntoVenta.existePdv(conn, new BigDecimal(objDatos.getIdPDV()), ID_PAIS);
					if (existePDV.intValue() == 0) {
						log.trace("ENTRA NO EXISTE PDV");
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());
						salidaPDV.setRespuesta(objRespuesta);

						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut,
								objDatos.getIdPDV(), Conf.LOG_TIPO_PDV, "Problema en la validaci\u00F3n de datos.",
								objRespuesta.getDescripcion()));

					} else {
						estadoActivo = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_ESTADOS_PDV, Conf.ESTADO_ACTIVO,
								objDatos.getCodArea());
						estadoBaja = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA,
								objDatos.getCodArea());
						tipoPDV = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELPDV, Conf.TIPO_PDV,
								objDatos.getCodArea());

						existeDTS = OperacionPuntoVenta.existeDts(conn, objDatos.getDistribuidorAsociado(), estadoAlta,
								ID_PAIS);

						if (existeDTS.intValue() == 0) {
							objRespuesta = getMensaje(Conf_Mensajes.MSJ_DTS_NOEXISTE_BAJA_88, null, nombreClase,
									nombreMetodo, null, objDatos.getCodArea());

							listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut,
									objDatos.getIdPDV(), Conf.LOG_TIPO_PDV, "Problema en la validaci\u00F3n de datos.",
									objRespuesta.getDescripcion()));

						} else {
							// asignando valores a objeto a insertar de pdv
							objNewPdv.setTcscpuntoventaid(new BigDecimal(objDatos.getIdPDV()));
							objNewPdv.setTcscdtsid(new BigDecimal(objDatos.getDistribuidorAsociado()));
							objNewPdv.setTcsccatpaisid(ID_PAIS);
							objNewPdv.setNombre(objDatos.getNombrePDV());
							objNewPdv.setEstado(estadoActivo);
							objNewPdv.setTipo_negocio(objDatos.getTipoNegocio());
							objNewPdv.setDocumento(objDatos.getDocumento());
							objNewPdv.setNit(objDatos.getNit());
							objNewPdv.setRegistro_fiscal(objDatos.getRegistroFiscal());
							objNewPdv.setNombre_fiscal(objDatos.getNombreFiscal());
							objNewPdv.setGiro_negocio(objDatos.getGiroNegocio());
							objNewPdv.setDireccion(objDatos.getDireccion());
							objNewPdv.setDepartamento(objDatos.getDepartamento());
							objNewPdv.setMunicipio(objDatos.getMunicipio());
							objNewPdv.setDistrito(objDatos.getDistrito());
							objNewPdv.setObservaciones(objDatos.getObservaciones());
							objNewPdv.setModificado_por(objDatos.getUsuario());

							// AGREGANDO ZONA COMERCIAL
							condiciones.add(new Filtro(Catalogo.CAMPO_TC_SC_CATPAIS_ID, Filtro.EQ, ID_PAIS));
							condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_GRUPO + ")", Filtro.EQ,
									"'" + Conf.GRUPO_ZONA_COMERCIAL_REG + "'"));
							if ("503".equals(objDatos.getCodArea())) {
								condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_VALOR + ")", Filtro.EQ,
										"'" + Conf.ZONA_COMERCIAL_1 + "'"));
							} else {
								condiciones.add(new Filtro("UPPER(" + Catalogo.CAMPO_VALOR + ")", Filtro.EQ,
										"'" + objDatos.getMunicipio().toUpperCase() + "'"));
							}

							zonaComercial = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_NOMBRE, Catalogo.N_TABLA,
									condiciones);

							objNewPdv.setTcsczonacomercialid(zonaComercial);

							objNewPdv.setLongitud(objDatos.getLongitud());
							objNewPdv.setLatitud(objDatos.getLatitud());

							// agregando nuevos campos 27-05-16 sbarrios
							objNewPdv.setTipo_producto(objDatos.getTipoProducto());
							objNewPdv.setCanal(objDatos.getCanal());
							objNewPdv.setSubcanal(objDatos.getSubcanal());
							objNewPdv.setCategoria(objDatos.getCategoria());
							objNewPdv.setCalle(objDatos.getCalle());
							objNewPdv.setAvenida(objDatos.getAvenida());
							objNewPdv.setPasaje(objDatos.getPasaje());
							objNewPdv.setColonia(objDatos.getColonia());
							objNewPdv.setCasa(objDatos.getCasa());
							objNewPdv.setReferencia(objDatos.getReferencia());
							objNewPdv.setTipo_contribuyente(objDatos.getTipoContribuyente());
							objNewPdv.setBarrio(objDatos.getBarrio());
							objNewPdv.setQr(objDatos.getQr());
							if (objDatos.getDigitoValidador() == null) {
								objNewPdv.setDigito_validador(null);
							} else {
								objNewPdv.setDigito_validador(new BigDecimal(objDatos.getDigitoValidador()));
							}

							objEncargadoPDV.setApellidos(objDatos.getEncargado().getApellidos());
							objEncargadoPDV.setNombres(objDatos.getEncargado().getNombres());
							objEncargadoPDV.setTelefono(new BigDecimal(objDatos.getEncargado().getTelefono()));
							objEncargadoPDV.setTcscpuntoventaid(new BigDecimal(objDatos.getIdPDV()));
							objEncargadoPDV
									.setTcscencargadopdvid(new BigDecimal(objDatos.getEncargado().getIdEncargadoPDV()));
							objEncargadoPDV.setEstado(estadoAlta);
							objEncargadoPDV.setCedula(objDatos.getEncargado().getCedula());
							objEncargadoPDV.setTipoDocumento(objDatos.getEncargado().getTipoDocumento());
							
							// fin agregado

							// Armando dias de visita
							for (int i = 0; i < objDatos.getDias().length; i++) {
								diaVisita = new DiaVisita();

								diaVisita.setEstado(estadoAlta);
								diaVisita.setCreado_por(objDatos.getUsuario());
								diaVisita.setNombre(objDatos.getDias()[i].toUpperCase());
								diaVisita.setTcscpuntoventaid(objNewPdv.getTcscpuntoventaid());
								listaDias.add(diaVisita);
							}

							// Armando numeros de recarga
							log.trace("Arma n\u00FAmeros recarga");
							if (objDatos.getTelefonoRecargo() != null) {
								for (int c = 0; c < objDatos.getTelefonoRecargo().length; c++) {

									/* validacion para que envien el orden de forma correcta */

									numeros = new NumRecarga();

									numeros.setNum_recarga(
											new BigDecimal(objDatos.getTelefonoRecargo()[c].getNumero()));
									numeros.setOrden(new BigDecimal(objDatos.getTelefonoRecargo()[c].getOrden()));
									numeros.setTipo(tipoPDV);
									numeros.setIdtipo(objNewPdv.getTcscpuntoventaid());
									numeros.setEstado(estadoAlta);
									numeros.setCreado_por(objDatos.getUsuario());
									numeros.setEstado_payment(objDatos.getTelefonoRecargo()[c].getEstadoPayment());
									lstnumeros.add(numeros);
								}
							}

							// validando que no existan n\u00FAmeros de recarga repetidos
							numRepetido = JavaUtils.validarNumeros(conn, lstnumeros,
									new BigDecimal(objDatos.getIdPDV()), tipoPDV, objDatos.getCodArea(), ID_PAIS);
							log.trace("resultado de n\u00FAmeros repetidos:" + numRepetido);

							if ("".equals(numRepetido)) {

								String numerosPayment = validaNumeroRecargaPayment(conn, objDatos, lstnumeros,
										UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.PIN_DEFAULT,
												objDatos.getCodArea()));
								if (numerosPayment.isEmpty()) {

									log.trace("validad nit");
									existenit = OperacionPuntoVenta.validarNit(conn, objDatos.getNit(), estadoActivo,
											new BigDecimal(objDatos.getIdPDV()), ID_PAIS);
									log.trace("resultado validacion nit:" + existenit);
									if (existenit.intValue() > 1) {
										razon = "NOTA: Otro punto de venta se encuentra registrado con el mismo n\u00FAmero de nit";
									}

									log.trace("Inicia a insertar valores");

									// se insertan valores
									respinsert = OperacionPuntoVenta.updatePdv(conn, objNewPdv);

									if (respinsert > 0) {
										OperacionPuntoVenta.deleteTable(conn, DiaVisita.N_TABLA,
												objNewPdv.getTcscpuntoventaid());
										OperacionPuntoVenta.insertDiaVisita(conn, listaDias);
										if (!lstnumeros.isEmpty()) {
											OperacionPuntoVenta.insertNumRecarga(conn, lstnumeros, estadoBaja,
													estadoAlta, 1, objDatos.getIdPDV(), objDatos.getCodArea(), ID_PAIS,
													listaLog);
										} else {
											OperacionPuntoVenta.borrarNum(conn, new BigDecimal(objDatos.getIdPDV()),
													tipoPDV, objDatos.getCodArea(), ID_PAIS, listaLog);
										}
										OperacionPuntoVenta.updateEncargadoPdv(conn, objEncargadoPDV);
										conn.commit();
										objRespuesta = getMensaje(Conf_Mensajes.OK_ACTUALIZAPDV, null, null, null,
												razon, objDatos.getCodArea());
										salidaPDV.setIdPDV(objNewPdv.getTcscpuntoventaid().toString());
										salidaPDV.setZonaComercial(objNewPdv.getTcsczonacomercialid().toString());
										listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut,
												objDatos.getIdPDV(), Conf.LOG_TIPO_PDV,
												"Se modificaron datos del punto de venta con ID " + objDatos.getIdPDV()
														+ ".",
												""));

									}

								} else {
									// Respuesta de error al validar los números de payment
									objRespuesta = getMensaje(Conf_Mensajes.MSG_NUMERO_RECARGA_PAYMENT_INVALIDO, null,
											nombreClase, nombreMetodo, numerosPayment, objDatos.getCodArea());

									listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_PDV, servicioPost,
											"0", Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
											objRespuesta.getDescripcion()));
								}

							} else {
								objRespuesta = getMensaje(Conf_Mensajes.MSJ_NUMRECARGAREPETIDO_32, null, nombreClase,
										nombreMetodo, numRepetido, objDatos.getCodArea());

								listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut,
										objDatos.getIdPDV(), Conf.LOG_TIPO_PDV,
										"Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
							}
						}
					}
				}

			} else {
				salidaPDV.setRespuesta(objRespuesta);

				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut,
						objDatos.getIdPDV() != null ? objDatos.getIdPDV() : "0", Conf.LOG_TIPO_PDV,
						"Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error(e1.getMessage(), e1);
			}
			objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null,
					objDatos.getCodArea());

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut, objDatos.getIdPDV(),
					Conf.LOG_TIPO_PDV, "Problema al modificar punto de venta.", e.getMessage()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error(e1.getMessage(), e1);
			}
			objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null,
					objDatos.getCodArea());

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut, objDatos.getIdPDV(),
					Conf.LOG_TIPO_PDV, "Problema al modificar punto de venta.", e.getMessage()));
		} finally {
			DbUtils.closeQuietly(conn);
			salidaPDV.setRespuesta(objRespuesta);

			UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
		}

		return salidaPDV;
	}

	/**********************************************************************************
	 * M\u00E9todo para dar de baja un pdv
	 **********************************************************************************/
	public OutputpdvDirec cambiarEstadoPDV(InputBajaPDV objDatos) {
		List<LogSidra> listaLog = new ArrayList<LogSidra>();
		String nombreMetodo = "cambiarEstadoPDV";
		String nombreClase = new CurrentClassGetter().getClassName();
		OutputpdvDirec objSalida = new OutputpdvDirec();
		Respuesta objRespuesta = new Respuesta();
		Connection conn = null;
		String estado = "";
		String estadoActivo = "";
		String estadoAlta = "";
		String estadoBaja = "";
		String tipoPDV = "";
		BigDecimal existeDTS = null;

		log.trace("inicia a validar valores...");
		objRespuesta = validarDatosBaja(objDatos);
		if (objRespuesta == null) {
			try {
				conn = getConnRegional();
				BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());

				if (objDatos.getIdPDV() == null || "".equals(objDatos.getIdPDV())) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDPDV_VACIO_8, null, nombreClase, nombreMetodo, null,
							objDatos.getCodArea());

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut, "0",
							Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
							objRespuesta.getDescripcion()));

				} else if (!isNumeric(objDatos.getIdPDV())) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPDV_NUM_315, null, nombreClase,
							nombreMetodo, null, objDatos.getCodArea());

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut, "0",
							Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
							objRespuesta.getDescripcion()));

				} else {
					List<Filtro> condiciones = new ArrayList<Filtro>();
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
							PuntoVenta.CAMPO_TCSCPUNTOVENTAID, objDatos.getIdPDV()));
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, RutaPDV.CAMPO_TCSCCATPAISID,
							ID_PAIS.toString()));
					if (new Integer(UtileriasBD.verificarExistencia(conn, PuntoVenta.N_TABLA, condiciones)) < 1) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_PDV_472, null, nombreClase, nombreMetodo,
								null, objDatos.getCodArea());

						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut,
								objDatos.getIdPDV(), Conf.LOG_TIPO_PDV, "Problema en la validaci\u00F3n de datos.",
								objRespuesta.getDescripcion()));

						objSalida.setRespuesta(objRespuesta);
						return objSalida;
					}
				}

				estadoActivo = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_ESTADOS_PDV, Conf.ESTADO_ACTIVO,
						objDatos.getCodArea());
				estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());
				estadoBaja = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, objDatos.getCodArea());
				tipoPDV = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELPDV, Conf.TIPO_PDV, objDatos.getCodArea());
				estado = objDatos.getEstado();

				if (!estado.equalsIgnoreCase(estadoActivo) && !estado.equalsIgnoreCase(estadoBaja)) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_ESTADO_NOVALIDO_33, null, nombreClase, nombreMetodo,
							null, objDatos.getCodArea());

					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut, objDatos.getIdPDV(),
							Conf.LOG_TIPO_PDV, "Problema en la validaci\u00F3n de datos.",
							objRespuesta.getDescripcion()));

				} else if (estado.equalsIgnoreCase(estadoBaja)) {

					// validando que no existan n\u00FAmeros de recarga repetidos
					existeDTS = OperacionPuntoVenta.existeDts(conn,
							"(SELECT TCSCDTSID FROM TC_SC_PUNTOVENTA WHERE TCSCPUNTOVENTAID = " + objDatos.getIdPDV()
									+ ")",
							estadoAlta, ID_PAIS);

					List<Filtro> condiciones = new ArrayList<Filtro>();
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
							RutaPDV.CAMPO_TCSCPUNTOVENTAID, objDatos.getIdPDV()));
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, RutaPDV.CAMPO_TCSCCATPAISID,
							ID_PAIS.toString()));
					// se valida que no este asociado a una ruta
					if (UtileriasBD.selectCount(conn, RutaPDV.N_TABLA, condiciones) > 0) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_BAJA_PDV_809, null, nombreClase, nombreMetodo,
								null, objDatos.getCodArea());

						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut,
								objDatos.getIdPDV(), Conf.LOG_TIPO_PDV, "Problema en la validaci\u00F3n de datos.",
								objRespuesta.getDescripcion()));
					}

					else if (existeDTS.intValue() == 0 && estado.equalsIgnoreCase(estadoActivo)) {
						objRespuesta = getMensaje(Conf_Mensajes.MSJ_DTS_NOEXISTE_BAJA_88, null, nombreClase,
								nombreMetodo, null, objDatos.getCodArea());

						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut,
								objDatos.getIdPDV(), Conf.LOG_TIPO_PDV, "Problema en la validaci\u00F3n de datos.",
								objRespuesta.getDescripcion()));
					} else {
						conn.setAutoCommit(false);
						log.trace("Inicia a insertar valores");

						// actualizando a nuevo estado
						OperacionPuntoVenta.updateTable(conn, PuntoVenta.N_TABLA, estado,
								new BigDecimal(objDatos.getIdPDV()), objDatos.getUsuario());

						if (estado.equals(estadoActivo)) {
							estado = estadoAlta;
						}
						// actualizando las tablas dependientes de pdv
						OperacionPuntoVenta.updateTableRecarga(conn, NumRecarga.N_TABLA, estado,
								new BigDecimal(objDatos.getIdPDV()), objDatos.getUsuario(), tipoPDV, "", ID_PAIS);
						OperacionPuntoVenta.updateTable(conn, DiaVisita.N_TABLA, estado,
								new BigDecimal(objDatos.getIdPDV()), objDatos.getUsuario());
						OperacionPuntoVenta.updateEncargadoPDV(conn, EncargadoPDV.N_TABLA, estado,
								new BigDecimal(objDatos.getIdPDV()));

						conn.commit();
						objRespuesta = getMensaje(Conf_Mensajes.OK_CAMBIOESTADO_PDV3, null, null, null, null,
								objDatos.getCodArea());
						objSalida.setIdPDV(objDatos.getIdPDV());

						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut,
								objDatos.getIdPDV(), Conf.LOG_TIPO_PDV,
								"Se cambi\u00F3 el estado del punto de venta con ID " + objDatos.getIdPDV() + ".", ""));

						// se ejecuta el proceso para enviar los datos a los servicios de mapas para su
						// modificacion

					}
				}
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					log.error(e1.getMessage(), e1);
				}
				log.error(e.getMessage(), e);
				objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null,
						objDatos.getCodArea());

				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut,
						objDatos.getIdPDV() != null ? objDatos.getIdPDV() : "0", Conf.LOG_TIPO_PDV,
						"Problema al modificar punto de venta.", e.getMessage()));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo,
						null, objDatos.getCodArea());

				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut,
						objDatos.getIdPDV() != null ? objDatos.getIdPDV() : "0", Conf.LOG_TIPO_PDV,
						"Problema al modificar punto de venta.", e.getMessage()));
			} finally {
				DbUtils.closeQuietly(conn);
				objSalida.setRespuesta(objRespuesta);

				UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
			}

		} else {
			objSalida.setRespuesta(objRespuesta);
		}

		return objSalida;
	}

	/**
	 * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso
	 * al servicio.
	 * 
	 * @param input
	 * @param metodo
	 * @return Respuesta
	 */
	private Respuesta validarInput(InputConsultaPDV input, int metodo) {
		String nombreMetodo = "validarInput";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta r = new Respuesta();
		String datosErroneos = "";
		boolean flag = false;
		log.debug("Validando datos...");

		if (input.getIdDTS() != null && !isNumeric(input.getIdDTS())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo,
					null, input.getCodArea()).getDescripcion();
			flag = true;
		}

		if (input.getIdPDV() != null && !isNumeric(input.getIdPDV())) {
			datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPDV_NUM_315, null, nombreClase, nombreMetodo,
					null, input.getCodArea()).getDescripcion();
			flag = true;
		}

		if (metodo == Conf.METODO_GET) {
			if ((input.getMin() != null && !"".equals(input.getMin())) && !isNumeric(input.getMin())) {
				r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MINIMO_198, null, nombreClase, nombreMetodo, null,
						input.getCodArea());
				datosErroneos += r.getDescripcion();
				flag = true;
			}

			if (input.getMax() != null && !"".equals(input.getMax())) {
				if (!isNumeric(input.getMax())) {
					r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MAXIMO_217, null, nombreClase, nombreMetodo, null,
							input.getCodArea());
					datosErroneos += r.getDescripcion();
					flag = true;
				} else if ((input.getMin() != null && !"".equals(input.getMin()))
						&& Integer.parseInt(input.getMin()) > Integer.parseInt(input.getMax())) {
					r = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MINIMO_MAYOR_218, null, nombreClase, nombreMetodo,
							null, input.getCodArea());
					datosErroneos += r.getDescripcion();
					flag = true;
				}
			}
		}

		if (flag) {
			r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos,
					input.getCodArea());
		} else {
			r.setDescripcion("OK");
			r.setCodResultado("1");
			r.setMostrar("0");
		}
		return r;
	}

	/**
	 * M\u00E9todo para obtener puntos de venta que se encuentran de alta y no estan
	 * asignados a rutas
	 * 
	 * @param objDatos
	 * @return
	 */
	public OutputpdvDirec getPDVDisponible(InputConsultaPDV objDatos, int metodo) {
		List<LogSidra> listaLog = new ArrayList<LogSidra>();
		String nombreMetodo = "getPDVDisponible";
		String nombreClase = new CurrentClassGetter().getClassName();
		OutputpdvDirec objReturn = new OutputpdvDirec();
		Respuesta objRespuesta = new Respuesta();
		Connection conn = null;

		List<Filtro> lstFiltros = new ArrayList<Filtro>();
		List<InputPDV> lstPDV = new ArrayList<InputPDV>();

		String estadoActivo = "";

		try {
			conn = getConnRegional();
			BigDecimal ID_PAIS = getIdPais(conn, objDatos.getCodArea());

			// Validaci\u00F3n de datos en el input
			objRespuesta = validarInput(objDatos, metodo);
			log.trace("Respuesta validaci\u00F3n: " + objRespuesta.getDescripcion());
			if (!"OK".equals(objRespuesta.getDescripcion())) {
				objReturn = new OutputpdvDirec();
				objReturn.setRespuesta(objRespuesta);

				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
						Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.",
						objRespuesta.getDescripcion()));

				return objReturn;
			}

			// agregando filtros
			if (!(objDatos.getIdPDV() == null || "".equals(objDatos.getIdPDV().trim()))) {
				lstFiltros.add(new Filtro(PuntoVenta.CAMPO_TCSCPUNTOVENTAID, Filtro.EQ, objDatos.getIdPDV()));
			}

			if (!(objDatos.getIdDTS() == null || "".equals(objDatos.getIdDTS().trim()))) {
				lstFiltros.add(new Filtro(PuntoVenta.CAMPO_TCSCDTSID, Filtro.EQ, objDatos.getIdDTS()));
			}

			if (!(objDatos.getNombre() == null || "".equals(objDatos.getNombre()))) {
				lstFiltros.add(new Filtro("upper(" + PuntoVenta.CAMPO_NOMBRE + ")", Filtro.LIKE,
						" upper('%" + objDatos.getNombre() + "%')"));
			}

			estadoActivo = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_ESTADOS_PDV, Conf.ESTADO_ACTIVO,
					objDatos.getCodArea());

			int min = (objDatos.getMin() != null && !"".equals(objDatos.getMin())) ? new Integer(objDatos.getMin()) : 0;
			int max = (objDatos.getMax() != null && !"".equals(objDatos.getMax())) ? new Integer(objDatos.getMax()) : 0;

			if (metodo == Conf.METODO_GET) {
				lstPDV = OperacionPuntoVenta.getPdvDisponibles(conn, lstFiltros, estadoActivo, max, min, ID_PAIS);

				if (lstPDV.isEmpty()) {
					objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_HAY_PDV_DISPONIBLES_542, null, nombreClase,
							nombreMetodo, null, objDatos.getCodArea());
				} else {
					objReturn.setPuntoDeVenta(lstPDV);
					objRespuesta = getMensaje(Conf_Mensajes.OK_GET_PDV_DISPONIBLES25, null, nombreClase, nombreMetodo,
							null, objDatos.getCodArea());
				}

				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
						Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de puntos de venta.", ""));
			} else if (metodo == Conf.METODO_COUNT) {
				int cantidad = OperacionPuntoVenta.getCountPdvDisponibles(conn, lstFiltros, estadoActivo, ID_PAIS);

				if (cantidad < 1) {
					objRespuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_HAY_PDV_DISPONIBLES_542, null,
							nombreClase, nombreMetodo, null, objDatos.getCodArea());

					objReturn = new OutputpdvDirec();
					objReturn.setRespuesta(objRespuesta);
				} else {
					objRespuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CONTEO_PDV_DISPONIBLE_77, null,
							nombreClase, nombreMetodo, null, objDatos.getCodArea());
					objReturn = new OutputpdvDirec();
					objReturn.setRespuesta(objRespuesta);
					objReturn.setCantRegistros(cantidad + "");
				}

				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioCount, "0",
						Conf.LOG_TIPO_NINGUNO, "Se consulto el conteo total de puntos de venta.", ""));
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null,
					objDatos.getCodArea());

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_GET_PDV_DISPONIBLES, Conf.LOG_GET_PDV_DISPONIBLES,
					objDatos.getIdPDV() != null ? objDatos.getIdPDV() : "0", Conf.LOG_TIPO_PDV,
					"Problema al obtener punto de venta.", e.getMessage()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null,
					objDatos.getCodArea());

			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_GET_PDV_DISPONIBLES, Conf.LOG_GET_PDV_DISPONIBLES,
					objDatos.getIdPDV() != null ? objDatos.getIdPDV() : "0", Conf.LOG_TIPO_PDV,
					"Problema al obtener punto de venta.", e.getMessage()));
		} finally {
			DbUtils.closeQuietly(conn);
			objReturn.setRespuesta(objRespuesta);
			UtileriasJava.doInsertLog(listaLog, objDatos.getUsuario(), objDatos.getCodArea());
		}

		return objReturn;
	}

	public static void addLog(String mensaje, String id, List<LogSidra> listaLog) {
		listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_PDV, servicioPut, id, Conf.LOG_TIPO_SOLICITUD,
				"Se modificaron datos de Solicitud.", mensaje));
	}

	private String validaNumeroRecargaPayment(Connection sdr, InputPDV input, List<NumRecarga> numeros,
			String pinDefault) throws SQLException {
		StringBuilder sb = new StringBuilder();
		QueryDailyTotalTransactionRequest request;
		PaymentServiceImpl servicePayment = new PaymentServiceImpl();
		String pin = "";

		// Se consulta la informacion del pais
		PaisDTO pais = new PaisDAO().getPais(sdr, input.getCodArea());

		// Se consulta la informacion del vendedor
		VendedorDtsDTO vendedor = new VendedorDtsDAO().getVendedorByUsuario(sdr, input.getUsuario());

		if (vendedor == null) {
			pin = pinDefault;
		} else {
			pin = vendedor.getPin().toString();
		}

		List<NumRecarga> numerosInvalidos = new ArrayList<>();
		for (NumRecarga numero : numeros) {
			request = new QueryDailyTotalTransactionRequest();
			log.debug("Se configura el pais, codArea: " + pais.getArea() + " - nombre " + pais.getNombre());
			request.setCountry(pais);
			log.debug("El pin obtenido para configurar el request, pin " + pin);
			request.setPin(pin);
			request.setDialogueId(0L);
			request.setProviderId(0L);
			request.setVoucherPin(null);
			request.setSourceMsisdn(numero.getNum_recarga().longValue());
			log.debug("Realizando el request");
			QueryDailyTotalTransactionResponse transactionRepsonse;

			try {

				transactionRepsonse = servicePayment.queryDailyTotalTransaction(request, sdr);
				if (!Constants.PAYMENT_SUCCESS_RESULT
						.equals(transactionRepsonse.getResponseTotalTrnsaction().getResponseCode())) {
					numerosInvalidos.add(numero);
					log.error("El número de recarga n[" + numero.getNum_recarga() + "No es valido");
					sb.append("El número ").append(numero.getNum_recarga()).append(" no es valido").append("\n");
				}

			} catch (PaymentException e) {
				if (e.getCause() instanceof IOException) {
					log.error("error de conectividad ", e);
				} else {
					if (PaymentExceptionCode.PAYMENT_66.getCode() != e.getMessageCode()) {
						log.error("No fue posible validar el número de recarga en payment", e);
						log.error("El número de recarga presenta un error [" + numero.getNum_recarga() + "] , code: "
								+ e.getMessageCode() + " causa: " + e.getMessageText());
						sb.append("\n");
						sb.append("El número ").append(numero.getNum_recarga()).append(" presenta un error.")
								.append("\n");
						sb.append("- Mensaje: " + e.getMessageText());
					}
				}
			}
		}
		return sb.toString();
	}

}

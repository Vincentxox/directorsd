package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.bodegadts.InputBodegaVirtual;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.bogegas.OutputBodegaVirtual;
import com.consystec.sc.sv.ws.operaciones.OperacionBodegaVirtual;
import com.consystec.sc.sv.ws.orm.BodegaVirtual;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.Inventario;
import com.consystec.sc.sv.ws.orm.Panel;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class CtrlBodegaVirtual extends ControladorBase {
	private static final Logger log = Logger.getLogger(CtrlBodegaVirtual.class);
	private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
	private static String servicioGet = Conf.LOG_GET_BODEGA_VIRTUAL;
	private static String servicioPost = Conf.LOG_POST_BODEGA_VIRTUAL;
	private static String servicioPut = Conf.LOG_PUT_BODEGA_VIRTUAL;
    String COD_PAIS="";
    BigDecimal ID_PAIS =null;
	/**
	 * Funci\u00F3n que valida el conjunto de datos que se env\u00EDe como recurso al
	 * servicio.
	 * 
	 * @param input
	 * @param metodo
	 * @param estadoBaja
	 * @param estadoAlta
	 * @return Respuesta
	 */
	public Respuesta validarInput(Connection conn, InputBodegaVirtual input, int metodo, String estadoAlta,
			String estadoBaja, BigDecimal idPais) {
		String nombreMetodo = "validarInput";
		String nombreClase = new CurrentClassGetter().getClassName();

		Respuesta r = new Respuesta();
		String datosErroneos = "";
		boolean flag = false;

		if (metodo == Conf.METODO_POST || metodo == Conf.METODO_PUT) {
			if (input.getNombre() == null || "".equals(input.getNombre().trim())) {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NOMBRE_222, null, nombreClase, nombreMetodo,
						null, input.getCodArea()).getDescripcion();
				flag = true;
			}
			if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_USUARIO_129, null, nombreClase, nombreMetodo,
						null, input.getCodArea()).getDescripcion();
				flag = true;
			}
			if (input.getNivel() == null || "".equals(input.getNivel().trim()) || !isNumeric(input.getNivel())) {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NIVEL_290, null, nombreClase, nombreMetodo,
						null, input.getCodArea()).getDescripcion();
				flag = true;
			}else{
				if(("503".equals(input.getCodArea()) && new BigDecimal(input.getNivel()).intValue()>1) && (input.getDireccion() == null || input.getDireccion().trim().equals(""))) {
						datosErroneos += getMensaje(Conf_Mensajes.MSJ_DIRECCION_NULO_18, null, nombreClase, nombreMetodo,
								null, input.getCodArea()).getDescripcion();
						flag = true;
				}
			}

			
		}
			
		if (metodo== Conf.METODO_POST && ( "0".equals(input.getNivel())&& (input.getTipo() == null || "".equals(input.getTipo().trim())))) {
					datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TIPO_NULO_150, null, nombreClase,
							nombreMetodo, null, input.getCodArea()).getDescripcion();
					flag=true;
			
		}

		if (metodo == Conf.METODO_POST && !flag && !"".equals(input.getNivel().trim())
				&& isNumeric(input.getNivel()) && new BigDecimal(input.getNivel()).intValue() > 1) {
			if (input.getIdBodegaPadre() == null || "".equals(input.getIdBodegaPadre().trim())
					|| !isNumeric(input.getIdBodegaPadre())) {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBOD_PADRE_291, null, nombreClase,
						nombreMetodo, null, input.getCodArea()).getDescripcion();
				flag = true;
			} else {
				// verificando que la bodega padre sea de nivel 1

				try {
					List<Filtro> condiciones = new ArrayList<Filtro>();
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
							BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getIdBodegaPadre()));
					String nivel = "";
					nivel = UtileriasBD.getOneRecord(conn, BodegaVirtual.CAMPO_NIVEL,
							ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);

					int nivelNumero = 0;

					if (!("".equals(nivel))) {
						nivelNumero = new BigDecimal(nivel).intValue();
						nivelNumero = new BigDecimal(input.getNivel()).intValue() - nivelNumero;
					}

					if (nivelNumero != 1) {
						log.error("La bodega padre enviada no es de nivel para ser bodega padre.");

						datosErroneos += new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NIVEL_BODEGA_127,
								null, nombreClase, nombreMetodo, null, input.getCodArea()).getDescripcion();
						flag = true;
					}
				} catch (SQLException e) {
					log.error("Excepcion: " + e.getMessage(), e);
					datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_NIVEL_BOD_294, null, nombreClase,
							nombreMetodo, e.getMessage(), input.getCodArea()).getDescripcion();
					flag = true;
				}
			}

			// se verifica que envien bodega origen
			if (input.getIdBodegaOrigen() == null || "".equals(input.getIdBodegaOrigen().trim())
					|| !isNumeric(input.getIdBodegaOrigen())) {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBOD_ORIGEN_787, null, nombreClase,
						nombreMetodo, null, input.getCodArea()).getDescripcion();
				flag = true;
			} else {
				flag = OperacionBodegaVirtual.verificarNivelBodega(conn, input.getIdBodegaOrigen());
				if (flag) {
					datosErroneos += new ControladorBase()
							.getMensaje(Conf_Mensajes.MSJ_ERROR_NIVEL_BODEGA_127, null, nombreClase, nombreMetodo, null, input.getCodArea())
							.getDescripcion().replace("padre", "origen");
				}
			}
		}

		if (!flag && input.getNivel() != null && !"".equals(input.getNivel().trim())
				&& isNumeric(input.getNivel()) && new BigDecimal(input.getNivel()).intValue() > 1) {
			if (input.getLatitud() != null && !"".equals(input.getLatitud().trim()) && !isValido(input.getLatitud())) {
				datosErroneos += " "
						+ getMensaje(Conf_Mensajes.MSJ_LAT_INVALIDA_29, null, nombreClase, nombreMetodo, null, input.getCodArea())
								.getDescripcion();
				flag = true;
			}

			if (input.getLongitud() != null && !"".equals(input.getLongitud().trim())
					&& !isValido(input.getLongitud())) {
				datosErroneos += " "
						+ getMensaje(Conf_Mensajes.MSJ_LONG_INVALIDA_28, null, nombreClase, nombreMetodo, null, input.getCodArea())
								.getDescripcion();
				flag = true;
			}
		}

		if (metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE) {
			log.debug("Validando datos para edici\u00F3n en m\u00E9todos PUT.");

			if (input.getIdBodega() == null) {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBOD_292, null, nombreClase, nombreMetodo,
						null, input.getCodArea()).getDescripcion();
				flag = true;
			} else if (!isNumeric(input.getIdBodega())) {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDBOD_NUM_293, null, nombreClase,
						nombreMetodo, null, input.getCodArea()).getDescripcion();
				flag = true;
			}
		}

		if (metodo == Conf.METODO_DELETE) {
			// verificando que la bodega no tenga dependientes
			try {
				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						Inventario.CAMPO_TCSCBODEGAVIRTUALID, input.getIdBodega()));
				String existencia = UtileriasBD.verificarExistencia(conn, ControladorBase
						.getParticion(Inventario.N_TABLA, Conf.PARTITION, input.getIdBodega().toString(), input.getCodArea()), condiciones);
				if (new Integer(existencia) > 0) {
					log.error("La bodega posee dependientes.");

					datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_BAJA_BOD_295, null, nombreClase, nombreMetodo,
							null, input.getCodArea()).getDescripcion();
					flag = true;
				}
			} catch (SQLException e) {
				log.error("Excepcion: " + e.getMessage(), e);
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_ARTICULOS_BOD_296, null, nombreClase,
						nombreMetodo, e.getMessage(), input.getCodArea()).getDescripcion();
				flag = true;
			}
		}

		if (metodo == Conf.METODO_PUT) {
			// verificando que el nombre no exista en otra bodega existente.
			try {
				List<Filtro> condiciones = new ArrayList<Filtro>();
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVirtual.CAMPO_NOMBRE,
						input.getNombre()));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_NOT_IN_AND,
						BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getIdBodega()));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						BodegaVirtual.CAMPO_TCSCCATPAISID, idPais.toString()));
				String existencia = UtileriasBD.verificarExistencia(conn,
						ControladorBase.getParticion(BodegaVirtual.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);
				if (new Integer(existencia) > 0) {
					log.error("Ya existe el nombre.");
					datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_NOMBRE_BOD_297, null, nombreClase, nombreMetodo,
							null, input.getCodArea()).getDescripcion();
					flag = true;
				}
			} catch (SQLException e) {
				log.error("Excepcion: " + e.getMessage(), e);
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_VERIFICAR_NOMBRE_BOD_298, null, nombreClase,
						nombreMetodo, e.getMessage(), input.getCodArea()).getDescripcion();
				flag = true;
			}

			if (input.getEstado() == null || "".equals(input.getEstado().trim())) {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_134, null, nombreClase, nombreMetodo,
						null, input.getCodArea()).getDescripcion();
				flag = true;
			} else if (!input.getEstado().equalsIgnoreCase(estadoBaja)
					&& !input.getEstado().equalsIgnoreCase(estadoAlta)) {
				datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ESTADO_TIPO_135, null, nombreClase,
						nombreMetodo, estadoAlta + " o " + estadoBaja + ".", input.getCodArea()).getDescripcion();
				flag = true;
			}
			
			//verificar si la bodega cuenta con mercaderia, si es bodega de nivel 0
			if ("0".equals(input.getNivel()) && input.getTipo()!= null && !"".equals(input.getTipo().trim()) ){
				
				try
				{
					List<Filtro> condiciones = new ArrayList<Filtro>();
					condiciones.clear();
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Inventario.CAMPO_TCSCBODEGAVIRTUALID,
							input.getIdBodega()));
					
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND,
							Inventario.CAMPO_TIPO_INV, "INV_SIDRA"));
					String existencia = UtileriasBD.verificarExistencia(conn,
							ControladorBase.getParticion(Inventario.N_TABLA, Conf.PARTITION, "", input.getCodArea()), condiciones);
					
					if (new Integer(existencia) > 0) {
						log.error("La bodega tiene producto asignado no es posible modificar los datos.");
						datosErroneos += getMensaje(Conf_Mensajes.MSJ_NO_MODIFICACION_BODEGA_NIVEL_0_631, null, nombreClase, nombreMetodo,
								null, input.getCodArea()).getDescripcion();
						flag = true;
					}
				}catch(SQLException e)
				{
					log.error("Excepcion: " + e.getMessage(), e);
					datosErroneos += getMensaje(Conf_Mensajes.MSJ_ERROR_BODEGA_NIVEL_0_630, null, nombreClase,
							nombreMetodo, e.getMessage(), input.getCodArea()).getDescripcion();
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

	/**
	 * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET y en
	 * este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST.
	 * 
	 * @param metodo
	 * @return
	 */
	public static String[] obtenerCamposGetPost(int metodo) {
		if (metodo == Conf.METODO_GET) {
			String campos[] = { BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, BodegaVirtual.CAMPO_NOMBRE,
					BodegaVirtual.CAMPO_IDBODEGA_ORIGEN, BodegaVirtual.CAMPO_IDBODEGA_PADRE, BodegaVirtual.CAMPO_NIVEL,
					BodegaVirtual.CAMPO_LATITUD, BodegaVirtual.CAMPO_LONGITUD, BodegaVirtual.CAMPO_ESTADO,
					BodegaVirtual.CAMPO_CREADO_EL, BodegaVirtual.CAMPO_CREADO_POR, BodegaVirtual.CAMPO_MODIFICADO_EL,
					BodegaVirtual.CAMPO_MODIFICADO_POR, BodegaVirtual.CAMPO_TCSCCATPAISID, 
					BodegaVirtual.CAMPO_TIPO, BodegaVirtual.CAMPO_DIRECCION};
			return campos;
		} else {
			String campos[] = { BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, BodegaVirtual.CAMPO_NOMBRE,
					BodegaVirtual.CAMPO_IDBODEGA_ORIGEN, BodegaVirtual.CAMPO_IDBODEGA_PADRE, BodegaVirtual.CAMPO_NIVEL,
					BodegaVirtual.CAMPO_LATITUD, BodegaVirtual.CAMPO_LONGITUD, BodegaVirtual.CAMPO_ESTADO,
					BodegaVirtual.CAMPO_CREADO_EL, BodegaVirtual.CAMPO_CREADO_POR, BodegaVirtual.CAMPO_TCSCCATPAISID,
					BodegaVirtual.CAMPO_TIPO, BodegaVirtual.CAMPO_DIRECCION};
			return campos;
		}
	}

	/**
	 * Funci\u00F3n que indica los datos que ser\u00E9n insertados en el m\u00E9todo POST.
	 * 
	 * @param input
	 * @param sequencia
	 * @return inserts
	 * @throws SQLException
	 */
	public static List<String> obtenerInsertsPostPadre(Connection conn, InputBodegaVirtual input, String sequencia, BigDecimal idPais)
			throws SQLException {
		List<String> inserts = new ArrayList<String>();
		String valores = "";
		String estado = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());
		valores = "(" + UtileriasJava.setInsert(Conf.INSERT_SECUENCIA, sequencia, Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getNombre(), Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_NUMERO,
						input.getIdBodegaOrigen() == null || "".equals(input.getIdBodegaOrigen()) ? "NULL"
								: input.getIdBodegaOrigen(),
						Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_NUMERO,
						input.getIdBodegaPadre() == null || "".equals(input.getIdBodegaPadre()) ? "NULL"
								: input.getIdBodegaPadre(),
						Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, input.getNivel(), Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_NUMERO,
						input.getLatitud() == null || "".equals(input.getLatitud()) ? "NULL" : input.getLatitud(),
						Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_NUMERO,
						input.getLongitud() == null || "".equals(input.getLongitud()) ? "NULL" : input.getLongitud(),
						Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO_UPPER, estado, Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_SYSDATE, null, Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getUsuario(), Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_NUMERO, idPais.toString(), Conf.INSERT_SEPARADOR_SI) 
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getTipo()==null || "".equals(input.getTipo()) ? "NULL" :
					input.getTipo(), Conf.INSERT_SEPARADOR_SI)
				+ UtileriasJava.setInsert(Conf.INSERT_TEXTO, input.getDireccion(), Conf.INSERT_SEPARADOR_NO)	+ ") ";
		inserts.add(valores);

		return inserts;
	}

	/**
	 * Funci\u00F3n que indica los campos que se utilizar\u00E9n para modificaciones al
	 * recurso en los m\u00E9todos PUT y DELETE.
	 * 
	 * @param input
	 * @param metodo
	 * @param estadoAlta
	 * @return campos
	 */
	public static String[][] obtenerCamposPutDelPadre(InputBodegaVirtual input,  String estadoAlta) {
		if (input.getNivel() != null && new BigDecimal(input.getNivel()).intValue() > 1) {
			String campos[][] = {
					{ BodegaVirtual.CAMPO_NOMBRE, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getNombre()) },
					{ BodegaVirtual.CAMPO_ESTADO,
							UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER,
									(input.getEstado() == null ? estadoAlta : input.getEstado())) },
					{ BodegaVirtual.CAMPO_LATITUD,
							(input.getLatitud() == null || "".equals(input.getLongitud()) ? "NULL"
									: input.getLatitud()) },
					{ BodegaVirtual.CAMPO_LONGITUD,
							(input.getLongitud() == null || "".equals(input.getLongitud()) ? "NULL"
									: input.getLongitud()) },
					{ BodegaVirtual.CAMPO_DIRECCION,
							UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER,
							(input.getDireccion() )) },									
					{ BodegaVirtual.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
					{ BodegaVirtual.CAMPO_MODIFICADO_POR,
							UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) } };
			return campos;			
		} else {
			String campos[][] = {
					{ BodegaVirtual.CAMPO_NOMBRE, UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getNombre()) },
					{ BodegaVirtual.CAMPO_ESTADO,
							UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER,
									(input.getEstado() == null ? estadoAlta : input.getEstado())) },
					{ BodegaVirtual.CAMPO_DIRECCION,
							UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER,
									(input.getDireccion() )) },										
					{ BodegaVirtual.CAMPO_MODIFICADO_EL, UtileriasJava.setUpdate(Conf.INSERT_SYSDATE, null) },
					{ BodegaVirtual.CAMPO_MODIFICADO_POR,
							UtileriasJava.setUpdate(Conf.INSERT_TEXTO, input.getUsuario()) }, 
					{BodegaVirtual.CAMPO_TIPO,
							UtileriasJava.setUpdate(Conf.INSERT_TEXTO_UPPER, input.getTipo()==null || "".equals(input.getTipo())? "NULL" : input.getTipo())}
							};
			return campos;
		}	
	}

	/**
	 * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes
	 * consultas seg\u00FAn el m\u00E9todo que se trabaje.
	 * 
	 * @param input
	 * @param metodo
	 * @return condiciones
	 */
	public static List<Filtro> obtenerCondiciones(InputBodegaVirtual input, int metodo, BigDecimal idPais) {
		List<Filtro> condiciones = new ArrayList<Filtro>();

		if (input.getIdDTS() != null && !"".equals(input.getIdDTS())) {
				String camposSelect[] = { Distribuidor.CAMPO_TCSCBODEGAVIRTUALID };

				List<Filtro> condicionesSelect = new ArrayList<Filtro>();
				condicionesSelect.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						Distribuidor.CAMPO_TC_SC_DTS_ID, input.getIdDTS()));
				condicionesSelect.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						Distribuidor.CAMPO_TCSCCATPAISID, idPais.toString()));
				String selectIds = UtileriasBD.armarQuerySelect(Distribuidor.N_TABLA, camposSelect, condicionesSelect);

				condicionesSelect.clear();
				condicionesSelect.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_OR,
						BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, selectIds));
				condicionesSelect.add(
						UtileriasJava.setCondicion(Conf.FILTRO_IN_OR, BodegaVirtual.CAMPO_IDBODEGA_ORIGEN, selectIds));
				UtileriasBD.agruparCondiciones(condicionesSelect);
				condiciones.add(new Filtro(Filtro.AND, "", "", UtileriasBD.agruparCondiciones(condicionesSelect)));

			} else if (input.getIdPanel() != null && !("".equals(input.getIdPanel().trim()))) {
				String camposSelect[] = { Panel.CAMPO_TCSCBODEGAVIRTUALID };

				List<Filtro> condicionesSelect = new ArrayList<Filtro>();
				condicionesSelect.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCPANELID,
						input.getIdPanel()));
				condicionesSelect.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Panel.CAMPO_TCSCCATPAISID,
						idPais.toString()));
				String selectIds = UtileriasBD.armarQuerySelect(Panel.N_TABLA, camposSelect, condicionesSelect);

				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND,
						BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, selectIds));

			} else if (input.getIdRuta() != null &&  !"".equals(input.getIdRuta())) {
				String camposSelect[] = { Ruta.CAMPO_TC_SC_BODEGAVIRTUAL_ID };

				List<Filtro> condicionesSelect = new ArrayList<Filtro>();
				condicionesSelect.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TC_SC_RUTA_ID,
						input.getIdRuta()));
				condicionesSelect.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Ruta.CAMPO_TCSCCATPAISID,
						idPais.toString()));

				String selectIds = UtileriasBD.armarQuerySelect(Ruta.N_TABLA, camposSelect, condicionesSelect);

				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND,
						BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, selectIds));
		}else{
			log.trace("no aplica condiciones");
		}

		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TCSCCATPAISID,
				idPais.toString()));

		if (metodo == Conf.METODO_POST && (input.getNombre() != null && !input.getNombre().equals(""))) {
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVirtual.CAMPO_NOMBRE,
						input.getNombre()));
		}

		if (metodo == Conf.METODO_GET) {
			if (input.getEstado() != null && !"".equals(input.getEstado())) {
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVirtual.CAMPO_ESTADO,
						input.getEstado()));
			}
			if (input.getNombre() != null && !"".equals(input.getNombre())) {
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_AND, BodegaVirtual.CAMPO_NOMBRE,
						input.getNombre()));
			}
			if (input.getIdBodega() != null && !"".equals(input.getIdBodega())) {
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getIdBodega()));
			}
			if (input.getIdBodegaPadre() != null && !"".equals(input.getIdBodegaPadre().trim())) {
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
						BodegaVirtual.CAMPO_IDBODEGA_PADRE, input.getIdBodegaPadre()));
			}
			if (input.getNivel() != null && !"".equals(input.getNivel().trim())) {
				condiciones.add(
						UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, BodegaVirtual.CAMPO_NIVEL, input.getNivel()));
			}
			
			
			if ("0".equals(input.getNivel()) && (input.getTipo() !=null && !"".equals(input.getTipo().trim())))
				{
					condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVirtual.CAMPO_TIPO, input.getTipo()));
				
			}
			
			
			
		}

		if (metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE) {
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getIdBodega()));
		}

		return condiciones;
	}

	/**
	 * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las consultas de
	 * verificaci\u00F3n de existencia del recurso.
	 * 
	 * @param input
	 * @param estadoAlta
	 * @return condiciones
	 */
	public static List<Filtro> obtenerCondicionesExistencia(InputBodegaVirtual input,  int metodo,
			String estadoAlta, BigDecimal idPais) {
		List<Filtro> condiciones = new ArrayList<Filtro>();

		condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, BodegaVirtual.CAMPO_TCSCCATPAISID,
				idPais.toString()));
		if (metodo == Conf.METODO_POST && !"".equals(input.getNombre())) {
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVirtual.CAMPO_NOMBRE,
						input.getNombre()));
				condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, BodegaVirtual.CAMPO_ESTADO,
						estadoAlta));
		}
		if (metodo == Conf.METODO_PUT || metodo == Conf.METODO_DELETE) {
			condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
					BodegaVirtual.CAMPO_TC_SC_BODEGA_VIRTUAL_ID, input.getIdBodega()));
		}

		return condiciones;
	}

	/**
	 * M\u00E9todo principal que realiza las operaciones generales del servicio REST
	 * 
	 * @param input
	 * @param metodo
	 * @return
	 */
	public OutputBodegaVirtual getDatos(InputBodegaVirtual input, int metodo) {
		String nombreMetodo = "getDatos";
		String nombreClase = new CurrentClassGetter().getClassName();
		Respuesta r = null;
		OutputBodegaVirtual output = null;
		COD_PAIS = input.getCodArea();

		Connection conn = null;
		try {
			conn = getConnRegional();
			ID_PAIS = getIdPais(conn, input.getCodArea());

			String estadoBaja = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_BAJA, input.getCodArea());
			String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

			// Validaci\u00F3n de datos en el input
			r = validarInput(conn, input, metodo, estadoAlta, estadoBaja, ID_PAIS);
			log.trace("Respuesta validaci\u00F3n: " + r.getDescripcion());
			if (!"OK".equalsIgnoreCase(r.getDescripcion())) {
				output = new OutputBodegaVirtual();
				output.setRespuesta(r);

				listaLog = new ArrayList<LogSidra>();
				listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BODEGA_VIRTUAL, servicioPost, "0",
						Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", r.getDescripcion()));

				return output;
			}

			if (metodo == Conf.METODO_GET) {
				// Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
				try {
					output = OperacionBodegaVirtual.doGet(conn, input, metodo,ID_PAIS);

					listaLog = new ArrayList<LogSidra>();
					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
							Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de bodegas virtuales.", ""));
				} catch (SQLException e) {
					r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

					log.error("Excepcion: " + e.getMessage(), e);
					output = new OutputBodegaVirtual();
					output.setRespuesta(r);

					listaLog = new ArrayList<LogSidra>();
					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
							Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de bodegas virtuales.",
							e.getMessage()));
				}

			} else if (metodo == Conf.METODO_POST) {
				// Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
				try {
					output = OperacionBodegaVirtual.doPost(conn, input, metodo, estadoAlta, ID_PAIS);

					if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_BODEGA_VIRTUAL_29) {
						listaLog = new ArrayList<LogSidra>();
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BODEGA_VIRTUAL, servicioPost,
								output.getIdBodegaVirtual(), Conf.LOG_TIPO_BODEGA_VIRTUAL,
								"Se cre\u00F3 nueva bodega virtual con ID " + output.getIdBodegaVirtual() + " y nombre "
										+ input.getNombre().toUpperCase() + ".",
								""));
					} else {
						listaLog = new ArrayList<LogSidra>();
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BODEGA_VIRTUAL, servicioPost, "0",
								Conf.LOG_TIPO_NINGUNO, "Problema al crear bodega virtual.",
								output.getRespuesta().getDescripcion()));
					}
				} catch (SQLException e) {
					r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

					log.error("Excepcion: " + e.getMessage(), e);
					output = new OutputBodegaVirtual();
					output.setRespuesta(r);

					listaLog = new ArrayList<LogSidra>();
					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BODEGA_VIRTUAL, servicioPost, "0",
							Conf.LOG_TIPO_NINGUNO, "Problema al crear bodega virtual.", e.getMessage()));
				}

			} else if (metodo == Conf.METODO_PUT) {
				// Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT
				try {
					output = OperacionBodegaVirtual.doPutDel(conn, input, metodo, estadoAlta, ID_PAIS);

					if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_BODEGA_VIRTUAL_30) {
						listaLog = new ArrayList<LogSidra>();
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BODEGA_VIRTUAL, servicioPut,
								input.getIdBodega(),
								Conf.LOG_TIPO_BODEGA_VIRTUAL, "Se modificaron datos de la bodega virtual con ID "
										+ input.getIdBodega() + " y nombre " + input.getNombre().toUpperCase() + ".",
								""));
					} else {
						listaLog = new ArrayList<LogSidra>();
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BODEGA_VIRTUAL, servicioPut,
								input.getIdBodega(), Conf.LOG_TIPO_BODEGA_VIRTUAL,
								"Problema al modificar datos de bodega virtual.",
								output.getRespuesta().getDescripcion()));
					}
				} catch (SQLException e) {
					r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

					log.error("Excepcion: " + e.getMessage(), e);
					output = new OutputBodegaVirtual();
					output.setRespuesta(r);

					listaLog = new ArrayList<LogSidra>();
					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BODEGA_VIRTUAL, servicioPut, "0",
							Conf.LOG_TIPO_NINGUNO, "Problema al modificar bodega virtual.", e.getMessage()));
				}
			} else if (metodo == Conf.METODO_DELETE) {
				// Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo DELETE
				try {
					output = OperacionBodegaVirtual.doPutDel(conn, input, metodo, estadoAlta, ID_PAIS);

					if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_DEL_BODEGA_VIRTUAL_31) {
						listaLog = new ArrayList<LogSidra>();
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BODEGA_VIRTUAL, servicioPut,
								input.getIdBodega(), Conf.LOG_TIPO_BODEGA_VIRTUAL,
								"Se elimin\u00F3 la bodega virtual con ID " + input.getIdBodega() + ".", ""));
					} else {
						listaLog = new ArrayList<LogSidra>();
						listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BODEGA_VIRTUAL, servicioPut,
								input.getIdBodega(), Conf.LOG_TIPO_BODEGA_VIRTUAL,
								"Problema al modificar datos de bodega virtual.",
								output.getRespuesta().getDescripcion()));
					}
				} catch (SQLException e) {
					r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

					log.error("Excepcion: " + e.getMessage(), e);
					output = new OutputBodegaVirtual();
					output.setRespuesta(r);

					listaLog = new ArrayList<LogSidra>();
					listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_BODEGA_VIRTUAL, servicioPut, "0",
							Conf.LOG_TIPO_NINGUNO, "Problema al modificar bodega virtual.", e.getMessage()));
				}
			}
		} catch (SQLException e) {
			r = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

			log.error("Excepcion: " + e.getMessage(), e);
			output = new OutputBodegaVirtual();
			output.setRespuesta(r);

			listaLog = new ArrayList<LogSidra>();
			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BODEGA_VIRTUAL, servicioPost, "0",
					Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de bodega virtual.", e.getMessage()));
		} catch (Exception e) {
			r = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());
			log.error("Excepcion: " + e.getMessage(), e);
			output = new OutputBodegaVirtual();
			output.setRespuesta(r);

			listaLog = new ArrayList<LogSidra>();
			listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_BODEGA_VIRTUAL, servicioPost, "0",
					Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de bodega virtual.", e.getMessage()));

		} finally {
			DbUtils.closeQuietly(conn);
			UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
		}

		return output;
	}
}

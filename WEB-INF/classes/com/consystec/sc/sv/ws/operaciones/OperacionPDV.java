package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.ms.seguridad.excepciones.ExcepcionSeguridad;
import com.consystec.sc.ca.ws.input.file.InputCargaFile;
import com.consystec.sc.ca.ws.input.pdv.EncargadoPDV;
import com.consystec.sc.ca.ws.input.pdv.InputConsultaPDV;
import com.consystec.sc.ca.ws.input.pdv.InputDiaVisita;
import com.consystec.sc.ca.ws.input.pdv.InputPDV;
import com.consystec.sc.ca.ws.input.pdv.TelefonoRecargo;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.pdv.OutputpdvDirec;
import com.consystec.sc.sv.ws.orm.DiaVisita;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.ImgPDV;
import com.consystec.sc.sv.ws.orm.NumRecarga;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.orm.RutaPDV;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;


public class OperacionPDV {
	private OperacionPDV(){}
    private static final Logger log = Logger.getLogger(OperacionPDV.class);

    /**
     * Funci\u00F3n que realiza las operaciones necesarias para obtener los datos al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputPanel
     * @throws SQLException
     * @throws ExcepcionSeguridad 
     * @throws NamingException 
     */
    public static OutputpdvDirec doGet(Connection conn,  InputConsultaPDV input, int metodo, BigDecimal ID_PAIS)
            throws SQLException, ExcepcionSeguridad, NamingException {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        List<InputPDV> list = new ArrayList<InputPDV>();

        Respuesta respuesta = null;
        OutputpdvDirec output = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        try {
            String tipoPDV = UtileriasJava.getConfig(null, Conf.GRUPO_PANELPDV, Conf.TIPO_PDV, input.getCodArea());
            String estadoAlta = UtileriasJava.getConfig(null, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, input.getCodArea());

            String sql = null;
            String campos[] = {
                PuntoVenta.N_TABLA_ID,
                PuntoVenta.CAMPO_TIPO_PRODUCTO,
                PuntoVenta.CAMPO_NOMBRE,
                PuntoVenta.CAMPO_CANAL,
                PuntoVenta.CAMPO_SUBCANAL,
                PuntoVenta.CAMPO_CATEGORIA,
                PuntoVenta.CAMPO_TCSCDTSID,
                PuntoVenta.CAMPO_TIPO_NEGOCIO,
                PuntoVenta.CAMPO_DOCUMENTO,
                PuntoVenta.CAMPO_NIT,
                PuntoVenta.CAMPO_NOMBRE_FISCAL,
                PuntoVenta.CAMPO_REGISTRO_FISCAL,
                PuntoVenta.CAMPO_GIRO_NEGOCIO,
                PuntoVenta.CAMPO_TIPO_CONTRIBUYENTE,
                PuntoVenta.CAMPO_DIRECCION,
                PuntoVenta.CAMPO_CALLE,
                PuntoVenta.CAMPO_AVENIDA,
                PuntoVenta.CAMPO_PASAJE,
                PuntoVenta.CAMPO_CASA,
                PuntoVenta.CAMPO_BARRIO,
                PuntoVenta.CAMPO_COLONIA,
                PuntoVenta.CAMPO_REFERENCIA,
                PuntoVenta.CAMPO_TCSCZONACOMERCIALID,
                PuntoVenta.CAMPO_DEPARTAMENTO,
                PuntoVenta.CAMPO_MUNICIPIO,
                PuntoVenta.CAMPO_DISTRITO,
                PuntoVenta.CAMPO_OBSERVACIONES,
                PuntoVenta.CAMPO_DIGITO_VALIDADOR,
                // PuntoVenta.CAMPO_FRECUENCIA_VISITA,
                PuntoVenta.CAMPO_LATITUD,
                PuntoVenta.CAMPO_LONGITUD,
                PuntoVenta.CAMPO_COD_CLIENTE,
                PuntoVenta.CAMPO_RESULTADO_SCL,
                PuntoVenta.CAMPO_QR,
                PuntoVenta.CAMPO_ESTADO,
                PuntoVenta.CAMPO_CREADO_EL,
                PuntoVenta.CAMPO_CREADO_POR,
                PuntoVenta.CAMPO_MODIFICADO_EL,
                PuntoVenta.CAMPO_MODIFICADO_POR
            };

            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCCATPAISID, "" + ID_PAIS));

            if (input.getNombre() != null && !"".equals(input.getNombre())) {
            	condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_AND, PuntoVenta.CAMPO_NOMBRE, input.getNombre().toUpperCase()));
            }
            
            if (input.getIdPDV() != null && !"".equals(input.getIdPDV())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.N_TABLA_ID, input.getIdPDV()));

            } else if (input.getIdDTS() != null && !"".equals(input.getIdDTS())) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCDTSID, input.getIdDTS()));

             
            } else if (input.getIdRuta() != null && !input.getIdRuta().equals("")) {
                List<Order> orden = new ArrayList<Order>();
                orden.add(new Order(RutaPDV.CAMPO_TCSCPUNTOVENTAID, Order.ASC));

                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, RutaPDV.CAMPO_TCSCRUTAID, input.getIdRuta()));
                List<String> idsPDV = UtileriasBD.getOneField(conn, RutaPDV.CAMPO_TCSCPUNTOVENTAID, RutaPDV.N_TABLA, condiciones, orden);

                if (!idsPDV.isEmpty()) {
                    condiciones.clear();
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IN_AND, PuntoVenta.CAMPO_TCSCPUNTOVENTAID,
                            UtileriasJava.listToString(Conf.TIPO_NUMERICO, idsPDV, ",")));
                } else {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_PVD_395, null,
                            nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputpdvDirec();
                    output.setRespuesta(respuesta);
                    return output;
                }
            }
            
            if (input.getNumRecarga() != null && !input.getNumRecarga().equals("")) {
                List<Filtro> condicionesNumRecarga = new ArrayList<Filtro>();
                condicionesNumRecarga.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCCATPAISID, "" + ID_PAIS));
                condicionesNumRecarga.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, NumRecarga.CAMPO_TIPO, tipoPDV));
                condicionesNumRecarga.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, NumRecarga.CAMPO_NUM_RECARGA, input.getNumRecarga()));
                condicionesNumRecarga.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, NumRecarga.CAMPO_ESTADO, estadoAlta));

                String idPDV = UtileriasBD.getOneRecord(conn, NumRecarga.CAMPO_IDTIPO, NumRecarga.N_TABLA, condicionesNumRecarga);

                if (!idPDV.equals("")) {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCPUNTOVENTAID, idPDV));
                } else {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_PVD_395, null, nombreClase, nombreMetodo, null, input.getCodArea());

                    output = new OutputpdvDirec();
                    output.setRespuesta(respuesta);
                    return output;
                }
            }

            if (input.getDepartamento() != null && !input.getDepartamento().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PuntoVenta.CAMPO_DEPARTAMENTO,
                        input.getDepartamento()));
            }
            if (input.getRuc_nit() != null && !input.getRuc_nit().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PuntoVenta.CAMPO_NIT,
                        input.getRuc_nit()));
            }
            if (input.getMunicipio() != null && !input.getMunicipio().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PuntoVenta.CAMPO_MUNICIPIO,
                        input.getMunicipio()));
            }

            if (input.getDistrito() != null && !input.getDistrito().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PuntoVenta.CAMPO_DISTRITO,
                        input.getDistrito()));
            }

            if (input.getCategoria() != null && !input.getCategoria().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PuntoVenta.CAMPO_CATEGORIA,
                        input.getCategoria()));
            }

            if (input.getEstado() != null && !input.getEstado().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PuntoVenta.CAMPO_ESTADO,
                        input.getEstado()));
            }

            int min = (input.getMin() != null && !input.getMin().equals("")) ? new Integer(input.getMin()) : 0;
            int max = (input.getMax() != null && !input.getMax().equals("")) ? new Integer(input.getMax()) : 0;

            if (metodo == Conf.METODO_GET) {
                List<Order> orden = new ArrayList<Order>();
        		orden.add(new Order(PuntoVenta.CAMPO_NOMBRE, Order.ASC));
        		sql = UtileriasBD.armarQrySelect(ControladorBase.getParticion(PuntoVenta.N_TABLA, Conf.PARTITION, input.getIdDTS(), input.getCodArea()), campos, condiciones, null, orden, min, max);

        		pstmt = conn.prepareStatement(sql);
        		rst = pstmt.executeQuery();

        		if (rst != null) {
        			if (!rst.next()) {
        				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTEN_PVD_395, null,
        						nombreClase, nombreMetodo, null, input.getCodArea());

        				output = new OutputpdvDirec();
        				output.setRespuesta(respuesta);
        			} else {
        				respuesta = new Respuesta();
        				if (input.getToken().equals("WEB")) {
        					respuesta=new ControladorBase()
        							.getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null, input.getCodArea());
        				} else {
        					respuesta=new ControladorBase()
        							.getMensaje(Conf_Mensajes.OK_GETDATOS12, null, nombreClase, nombreMetodo, null, input.getCodArea());
        				}

        				List<Filtro> condicionesExistencia = new ArrayList<Filtro>();
        				Filtro filtroNumRecargaPDV = UtileriasJava.getCondicionDefault(Conf.FILTRO_PDV, NumRecarga.CAMPO_TIPO, conn, input.getCodArea());
        				String idPDV = "";

        				// Se obtienen los distribuidores.
        				List<Map<String, String>> listDTS = UtileriasJava.getDistribuidores(conn, ID_PAIS, input.getCodArea());

        				do {
        					InputPDV item = new InputPDV();
        					idPDV = rst.getString(PuntoVenta.N_TABLA_ID);

        					for (int i = 0; i < listDTS.size(); i++) {
        						if (rst.getString(PuntoVenta.CAMPO_TCSCDTSID).equals(listDTS.get(i).get(Distribuidor.CAMPO_TC_SC_DTS_ID))) {
        							item.setDistribuidorAsociado(rst.getString(Ruta.CAMPO_TC_SC_DTS_ID));
        							item.setNombreDTS(listDTS.get(i).get(Conf.DTS_NOMBRES));
        							item.setTipoDTS(listDTS.get(i).get(Conf.DTS_TIPO));
        						}
        					}

        					item.setCodArea(input.getCodArea());
        					item.setIdPDV(idPDV);
        					item.setNombrePDV(rst.getString(PuntoVenta.CAMPO_NOMBRE));
        					item.setTipoNegocio(rst.getString(PuntoVenta.CAMPO_TIPO_NEGOCIO));
        					item.setTipoProducto(rst.getString(PuntoVenta.CAMPO_TIPO_PRODUCTO));
        					item.setCanal(rst.getString(PuntoVenta.CAMPO_CANAL));
        					item.setSubcanal(rst.getString(PuntoVenta.CAMPO_SUBCANAL));
        					item.setCategoria(rst.getString(PuntoVenta.CAMPO_CATEGORIA));
        					item.setCasa(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_CASA)));
        					item.setCalle(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_CALLE)));
        					item.setAvenida(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_AVENIDA)));
        					item.setPasaje(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_PASAJE)));
        					item.setColonia(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_COLONIA)));
        					item.setReferencia(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_REFERENCIA)));
        					item.setBarrio(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_BARRIO)));
        					item.setTipoContribuyente(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_TIPO_CONTRIBUYENTE)));
        					item.setDocumento(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_DOCUMENTO)));
        					item.setNit(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_NIT)));
        					item.setNombreFiscal(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_NOMBRE_FISCAL)));
        					item.setRegistroFiscal(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_REGISTRO_FISCAL)));
        					item.setGiroNegocio(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_GIRO_NEGOCIO)));
        					item.setDireccion(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_DIRECCION)));
        					item.setZonaComercial(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_TCSCZONACOMERCIALID)));
        					item.setDepartamento(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_DEPARTAMENTO)));
        					item.setMunicipio(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_MUNICIPIO)));
        					item.setDistrito(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_DISTRITO)));
        					item.setObservaciones(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_OBSERVACIONES)));
        					item.setDigitoValidador(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_DIGITO_VALIDADOR)));

        					item.setLatitud(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_LATITUD)));
        					item.setLongitud(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_LONGITUD)));

                            item.setCodCliente(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_COD_CLIENTE)));
                            item.setResultadoSCL(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_RESULTADO_SCL)));
                            item.setQr(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_QR)));

                            item.setEstado(rst.getString(PuntoVenta.CAMPO_ESTADO));
                            item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(PuntoVenta.CAMPO_CREADO_EL)));
                            item.setCreado_por(rst.getString(PuntoVenta.CAMPO_CREADO_POR));
                            item.setModificado_el(UtileriasJava.getRstValue(rst, Conf.TIPO_FECHA, PuntoVenta.CAMPO_MODIFICADO_EL));
                            item.setModificado_por(UtileriasJava.getValue(rst.getString(PuntoVenta.CAMPO_MODIFICADO_POR)));

        					// agregando datos de encargado de PDV
        					item.setEncargado(getDatosEncargado(conn, idPDV, input.getCodArea()));

        					// agregando indices de imagenes asociadas
        					item.setImgAsociadas(getImgAsociadas(conn, idPDV, ID_PAIS));

        					if (input.getMostrarNumerosRecarga().equals("1")) {
        						// Se verifica si posee datos asociados.
        						condicionesExistencia.clear();
        						condicionesExistencia.add(filtroNumRecargaPDV);
        						condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, NumRecarga.CAMPO_IDTIPO, idPDV));
        						condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_AND, NumRecarga.CAMPO_TIPO, tipoPDV));

        						List<TelefonoRecargo> datosNumRecargas = new ArrayList<TelefonoRecargo>();

        						datosNumRecargas = getDatosNumRecarga(conn, condicionesExistencia, input.getCodArea());
        						item.setNumerosRecarga(datosNumRecargas);
        					}

        					if (input.getMostrarDiasVisita().equals("1")) {
        						// Se verifica si posee datos asociados.
        						condicionesExistencia.clear();
        						condicionesExistencia.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, DiaVisita.CAMPO_TCSCPUNTOVENTAID, idPDV));

        						List<InputDiaVisita> datosDiaVisita = new ArrayList<InputDiaVisita>();

        						datosDiaVisita = getDatosDiaVisita(conn, condicionesExistencia, input.getCodArea());
        						item.setDiasVisita(datosDiaVisita);
        					}

        					list.add(item);
        				} while (rst.next());

        				output = new OutputpdvDirec();
        				output.setRespuesta(respuesta);
        				output.setPuntoDeVenta(list);
        			}
        		}
            } else if (metodo == Conf.METODO_COUNT) {
                String campo = UtileriasJava.setSelect(Conf.SELECT_COUNT, PuntoVenta.CAMPO_TCSCPUNTOVENTAID);
                String cantidad = UtileriasBD.getOneRecord(conn, campo, PuntoVenta.N_TABLA, condiciones);
 
                if (cantidad.equals("0")) {
    				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_INFORMACION_PDV_612, null,
    						nombreClase, nombreMetodo, null, input.getCodArea());

    				output = new OutputpdvDirec();
    				output.setRespuesta(respuesta);
    			} else {
    				respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CONTEO_PDV_76, null,
    						nombreClase, nombreMetodo, null, input.getCodArea());
    				output = new OutputpdvDirec();
    				output.setRespuesta(respuesta);
    				output.setCantRegistros(cantidad + "");
                }
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return output;
    }

    /**
     * Para obtener datos de los d\u00EDas de visita.
     * 
     * @param conn
     * @param idPDV
     * @param input
     * @param condiciones
     * @return
     * @throws SQLException
     */
    private static List<InputDiaVisita> getDatosDiaVisita(Connection conn,
            List<Filtro> condiciones, String codArea) throws SQLException {
        String nombreMetodo = "getDatosDiaVisita";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<InputDiaVisita> list = new ArrayList<InputDiaVisita>();
        InputDiaVisita item = new InputDiaVisita();
        PreparedStatement pstmtIn = null;
        ResultSet rstIn = null;

        try {
	        String campos[] = {
	            DiaVisita.N_TABLA_ID,
	            DiaVisita.CAMPO_NOMBRE,
	            DiaVisita.CAMPO_ESTADO
	        };

            String sql = UtileriasBD.armarQuerySelect(DiaVisita.N_TABLA, campos, condiciones);

            pstmtIn = conn.prepareStatement(sql);
            rstIn = pstmtIn.executeQuery();

            if (rstIn != null) {
                if (!rstIn.next()) {
                    log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
                    item = new InputDiaVisita();

                    Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null,
                            nombreClase, nombreMetodo, null, codArea);

                    item.setEstado(respuesta.getDescripcion());
                    list.add(item);
                } else {
                    do {
                        item = new InputDiaVisita();
                        item.setIdDiaVisita(rstIn.getString(DiaVisita.CAMPO_TCSCDIAVISITAID));
                        item.setNombre(rstIn.getString(DiaVisita.CAMPO_NOMBRE));
                        item.setEstado(rstIn.getString(DiaVisita.CAMPO_ESTADO));

                        list.add(item);
                    } while (rstIn.next());
                }
            }
        } finally {

            DbUtils.closeQuietly(rstIn);
            DbUtils.closeQuietly(pstmtIn);
        }
        return list;
    }

   

    /**
     * Para obtener datos de los numeros de recarga.
     * 
     * @param conn
     * @param idPDV
     * @param input
     * @param condiciones
     * @return
     * @throws SQLException
     */
    private static List<TelefonoRecargo> getDatosNumRecarga(Connection conn,  
            List<Filtro> condiciones, String codArea) throws SQLException {
        String nombreMetodo = "getDatosNumRecarga";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<TelefonoRecargo> list = new ArrayList<TelefonoRecargo>();
        TelefonoRecargo item = new TelefonoRecargo();
        PreparedStatement pstmtIn = null;
        ResultSet rstIn = null;

        try {
	        String campos[] = {
	            NumRecarga.N_TABLA_ID,
	            NumRecarga.CAMPO_NUM_RECARGA,
	            NumRecarga.CAMPO_ORDEN,
	            NumRecarga.CAMPO_ESTADO,
	            NumRecarga.CAMPO_ESTADO_PAYMENT,
	            NumRecarga.CAMPO_TCSCSOLICITUDID
	        };

            List<Order> orden = new ArrayList<Order>();
            orden.add(new Order(NumRecarga.CAMPO_ORDEN, Order.ASC));

            String sql = UtileriasBD.armarQuerySelect(NumRecarga.N_TABLA, campos, condiciones, orden);

            pstmtIn = conn.prepareStatement(sql);
            rstIn = pstmtIn.executeQuery();

            if (rstIn != null) {
                if (!rstIn.next()) {
                    log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
                    item = new TelefonoRecargo();

                    Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null,
                            nombreClase, nombreMetodo, null, codArea);

                    item.setEstado(respuesta.getDescripcion());
                    list.add(item);
                } else {
                    do {
	                    item = new TelefonoRecargo();
	                    item.setIdNumero(rstIn.getString(NumRecarga.N_TABLA_ID));
	                    item.setNumero(rstIn.getString(NumRecarga.CAMPO_NUM_RECARGA));
	                    item.setOrden(rstIn.getString(NumRecarga.CAMPO_ORDEN));
	                    item.setEstado(rstIn.getString(NumRecarga.CAMPO_ESTADO));
	                    item.setEstadoPayment(UtileriasJava.getValue(rstIn.getString(NumRecarga.CAMPO_ESTADO_PAYMENT)));
	                    item.setIdSolicitud(UtileriasJava.getValue(rstIn.getString(NumRecarga.CAMPO_TCSCSOLICITUDID)));

                        list.add(item);
                    } while (rstIn.next());
                }
            }
        } finally {

            DbUtils.closeQuietly(rstIn);
            DbUtils.closeQuietly(pstmtIn);
        }
        return list;
    }

    /**
     * Para obtener informaci\u00F3n del encargado
     * 
     * @param conn
     * @param idPDV
     * @param input
     * @param condiciones
     * @return
     * @throws SQLException
     * @throws ExcepcionSeguridad
     * @throws NamingException
     */
    private static EncargadoPDV getDatosEncargado(Connection conn, String idPDV, String codArea)
            throws SQLException, ExcepcionSeguridad, NamingException {
        String nombreMetodo = "getDatosEncargado";
        String nombreClase = new CurrentClassGetter().getClassName();
        List<Filtro> condiciones = new ArrayList<Filtro>();
        EncargadoPDV objEncargado = new EncargadoPDV();
        PreparedStatement pstmtIn = null;
        ResultSet rstIn = null;

        try {
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND,
                    com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_TCSCPUNTOVENTAID, idPDV));

	        String campos[] = {
	        	com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_TCSCENCARGADOPDVID,
	        	com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_NOMBRES,
	        	com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_APELLIDOS,
	        	com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_TELEFONO,
	        	com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_CEDULA,
	        	com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_TIPO_DOCUMENTO
	        };

            String sql = UtileriasBD.armarQuerySelect(com.consystec.sc.sv.ws.orm.EncargadoPDV.N_TABLA, campos,
                    condiciones);

            pstmtIn = conn.prepareStatement(sql);
            rstIn = pstmtIn.executeQuery();

            if (rstIn != null) {
                if (!rstIn.next()) {
                    log.debug("No existen registros en la tabla hija con esos par\u00E9metros.");
                    objEncargado = new EncargadoPDV();

                    Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_NO_RECURSO_HIJO, null,
                            nombreClase, nombreMetodo, null, codArea);

                    objEncargado.setEstado(respuesta.getDescripcion());

                } else {
	                objEncargado = new EncargadoPDV();
	                objEncargado.setIdEncargadoPDV(rstIn.getString(com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_TCSCENCARGADOPDVID));
	                objEncargado.setNombres(rstIn.getString(com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_NOMBRES));
	                objEncargado.setApellidos(rstIn.getString(com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_APELLIDOS));
	                objEncargado.setTelefono(rstIn.getString(com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_TELEFONO));

	                if (rstIn.getString(com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_CEDULA) == null
	                        || "".equals(rstIn.getString(com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_CEDULA))) {
	                    objEncargado.setCedula("");
	                } else {
	                    objEncargado.setCedula(rstIn.getString(com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_CEDULA));
                    }
	                
	                if (rstIn.getString(com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_TIPO_DOCUMENTO) == null
	                        || "".equals(rstIn.getString(com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_TIPO_DOCUMENTO))) {
	                    objEncargado.setTipoDocumento("");
	                } else {
	                    objEncargado.setTipoDocumento(rstIn.getString(com.consystec.sc.sv.ws.orm.EncargadoPDV.CAMPO_TIPO_DOCUMENTO));
                    }
                }
            }
        } finally {
            DbUtils.closeQuietly(rstIn);
            DbUtils.closeQuietly(pstmtIn);
        }

        return objEncargado;
    }

    /**
     * Para obtener el listado de \u00EDndices de las im\u00E9genes asociadas.
     * 
     * @param conn
     * @param idPDV
     * @return
     * @throws SQLException 
     */
    private static List<InputCargaFile> getImgAsociadas(Connection conn, String idPDV, BigDecimal ID_PAIS) throws SQLException {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ImgPDV.CAMPO_TCSCPUNTOVENTAID, idPDV));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NULL_AND, ImgPDV.CAMPO_TCSCVISITAID, null));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ImgPDV.CAMPO_TCSCCATPAISID, ""+ID_PAIS));

        List<Order> orden = new ArrayList<Order>();
        orden.add(new Order(ImgPDV.CAMPO_TCSCIMGPDVID, Order.ASC));

        List<String> listado = UtileriasBD.getOneField(conn, ImgPDV.CAMPO_TCSCIMGPDVID, ImgPDV.N_TABLA, condiciones, orden);

        List<InputCargaFile> listIDs = new ArrayList<InputCargaFile>();

        InputCargaFile id = null;
        for (int i = 0; i < listado.size(); i++) {
            id = new InputCargaFile();
            id.setIdImgPDV(listado.get(i));
            listIDs.add(id);
        }

        return listIDs;
    }
}

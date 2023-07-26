package com.consystec.sc.sv.ws.metodos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.input.remesa.InputRemesa;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.remesa.OutputRemesa;
import com.consystec.sc.sv.ws.operaciones.OperacionRemesa;
import com.consystec.sc.sv.ws.orm.Cuenta;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.Panel;
import com.consystec.sc.sv.ws.orm.Remesa;
import com.consystec.sc.sv.ws.orm.Ruta;
import com.consystec.sc.sv.ws.orm.Solicitud;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2016
 *
 */
public class CtrlRemesa extends ControladorBase {
    private static final Logger log = Logger.getLogger(CtrlRemesa.class);
    private static String servicioGet = Conf.LOG_GET_REMESA;
    private static String servicioPost = Conf.LOG_POST_REMESA;
    private static String servicioPut = Conf.LOG_PUT_REMESA;

    public Respuesta validarInputMovil(Connection conn, InputRemesa input, String origenPC, String estadoAlta, int i, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        List<Filtro> condiciones = new ArrayList<Filtro>();
        boolean flag=false;
        Respuesta r = new Respuesta();
        String datosErroneos = "";
        log.debug("Validando datos...");
        
        String origenMovil = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_ORIGEN, Conf.SOL_ORIGEN_MOVIL, input.getCodArea());
        	 if(input.getOrigen().equalsIgnoreCase(origenMovil))
   		  {
   			  if (input.getRemesas().get(i).getIdRemesa() == null || "".equals(input.getRemesas().get(i).getIdRemesa())
                        || !isNumeric(input.getRemesas().get(i).getIdRemesa())) {
   				  datosErroneos += getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDREMESA_710, null, nombreClase, nombreMetodo, "Elemento No. " + (i + 1), input.getCodArea()).getDescripcion();
   				  flag=true;                   
                }

                if (input.getRemesas().get(i).getIdCuenta() == null || "".equals(input.getRemesas().get(i).getIdCuenta())
                        || !isNumeric(input.getRemesas().get(i).getIdCuenta())) {
                	datosErroneos +=getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDCUENTA_700, null, nombreClase, nombreMetodo, "Elemento No. " + (i + 1), input.getCodArea()).getDescripcion();
              	  flag=true;

                } else {
                    condiciones.clear();
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Cuenta.CAMPO_TCSCCTABANCARIAID, input.getRemesas().get(i).getIdCuenta()));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Cuenta.CAMPO_TCSCCATPAISID, "" + ID_PAIS));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Cuenta.CAMPO_ESTADO, estadoAlta));
                    String bancoCuenta = UtileriasBD.getOneRecord(conn, Cuenta.CAMPO_BANCO, Cuenta.N_TABLA, condiciones);

                    if (bancoCuenta == null || "".equals(bancoCuenta)) {
                    	datosErroneos+=getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_CUENTA_713, null, nombreClase, nombreMetodo, "Elemento No. " + (i + 1), input.getCodArea()).getDescripcion();
                  	  flag=true;
                    } else {
                        input.getRemesas().get(i).setBanco(bancoCuenta);
                    }
                }
                if (input.getRemesas().get(i).getTasaCambio() == null || "".equals(input.getRemesas().get(i).getTasaCambio())
                        || !isDecimal(input.getRemesas().get(i).getTasaCambio())) {
                	datosErroneos+=getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_TASA_CAMBIO_NUM_860, null, nombreClase, nombreMetodo, "Elemento No. " + (i + 1), input.getCodArea()).getDescripcion();
              	  flag=true;                  
                }
   		  }
        	    // validaciones generales
             if (input.getRemesas().get(i).getMonto() == null || "".equals(input.getRemesas().get(i).getMonto())
                     || !isDecimal(input.getRemesas().get(i).getMonto())) {
            	 datosErroneos+= getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MONTO_REMESA_711, null, nombreClase, nombreMetodo, "Elemento No. " + (i + 1), input.getCodArea()).getDescripcion();
            	 flag=true;
             }

             if (input.getRemesas().get(i).getNoBoleta() == null || "".equals(input.getRemesas().get(i).getNoBoleta().trim())) {
            	 datosErroneos+=getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NOBOLETA_712, null, nombreClase, nombreMetodo, "Elemento No. " + (i + 1), input.getCodArea()).getDescripcion();
            	 flag=true;
             } else {
                 condiciones.clear();
                 if (input.getOrigen().equalsIgnoreCase(origenPC)) {
                     condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.CAMPO_BANCO, input.getRemesas().get(i).getBanco()));
                     condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.CAMPO_NO_BOLETA, input.getRemesas().get(i).getNoBoleta()));
                     condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Remesa.CAMPO_TCSCCATPAISID, "" + ID_PAIS));
                     condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.CAMPO_ORIGEN, input.getOrigen()));
                 } else {
                     condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_AVANZADO_AND, "",
                         "(TCSCCTABANCARIAID IN (SELECT TCSCCTABANCARIAID FROM TC_SC_CTA_BANCARIA WHERE TCSCCATPAISID = " + ID_PAIS
                             + " AND BANCO IN (SELECT BANCO FROM TC_SC_CTA_BANCARIA WHERE TCSCCTABANCARIAID = " + input.getRemesas().get(i).getIdCuenta()
                             + " AND TCSCCATPAISID = " + ID_PAIS + ") AND ESTADO = '" + estadoAlta + "')" + " AND TCSCCATPAISID = " + ID_PAIS
                             + " AND NO_BOLETA = '" + input.getRemesas().get(i).getNoBoleta() + "')"
                         + " OR (BANCO = (SELECT BANCO FROM TC_SC_CTA_BANCARIA WHERE TCSCCTABANCARIAID = " + input.getRemesas().get(i).getIdCuenta()
                         + " AND TCSCCATPAISID = " + ID_PAIS + ") AND TCSCCATPAISID = " + ID_PAIS + " AND NO_BOLETA = '" + input.getRemesas().get(i).getNoBoleta() + "')"));
                     condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.CAMPO_ORIGEN, input.getOrigen()));
                 }

                 int existencia = UtileriasBD.selectCount(conn, Remesa.N_TABLA, condiciones);

                 if (existencia > 0) {
                	 datosErroneos+= getMensaje(Conf_Mensajes.MSJ_EXISTE_BOLETA_BANCO_753, null, nombreClase, nombreMetodo, (i + 1) + ".", input.getCodArea()).getDescripcion();
                	 flag=true;
                 }
             }
    	
        if (flag) {
            r = getMensaje(Conf_Mensajes.MSJ_RECURSO_DATOS_ERRONEOS, null, nombreClase, nombreMetodo, datosErroneos, input.getCodArea());
            r.setCodResultado("-1");
        } else {
            r.setDescripcion("OK");
            r.setCodResultado("1");
            r.setMostrar("0");
        }
	 return r;
    }
    /**
     * Funci\u00F3n que valida el conjunto de datos que se env\u00EDa como recurso al
     * servicio.
     * 
     * @param conn Objeto de tipo connetion con los datos de la conexi\u00F3n activa.
     * @param input Objeto con los datos enviados para validar.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @param origenPC 
     * @param estadoAlta Valor del estado de alta.
     * @param estadoRechazadaTelca 
     * @param estadoPendiente 
     * 
     * @return respuesta Objeto con la respuesta en caso de error o nulo si todo esta correcto.
     * @throws SQLException 
     */
    public Respuesta validarInput(Connection conn, InputRemesa input, int metodo, String origenPC, String estadoAlta,
            String estadoPendiente, String estadoRechazadaTelca, BigDecimal ID_PAIS) throws SQLException {
        String nombreMetodo = "validarInput";
        String nombreClase = new CurrentClassGetter().getClassName();
        List<Filtro> condiciones = new ArrayList<Filtro>();
        
        log.debug("Validando datos...");

        if (input.getUsuario() == null || "".equals(input.getUsuario().trim())) {
            return getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, input.getCodArea());
        }

        if (metodo == Conf.METODO_PUT) {
            //Agregado idDeuda para borrar remesas completas de deuda
            if (input.getIdDeuda() != null && !"".equals(input.getIdDeuda()) && input.getIdRemesa() != null && !"".equals(input.getIdRemesa())) {
                return getMensaje(Conf_Mensajes.MSJ_ERROR_INPUT_IDDEUDA_IDREMESA_903, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }

            if (input.getIdDeuda() != null && !"".equals(input.getIdDeuda())) {
                if (!isNumeric(input.getIdDeuda())) {
                    return getMensaje(Conf_Mensajes.MSJ_ERROR_INPUT_IDDEUDA_893, null, nombreClase, nombreMetodo, null, input.getCodArea());
                }
            } else {
                if (input.getIdRemesa() == null || "".equals(input.getIdRemesa()) || !isNumeric(input.getIdRemesa())) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDREMESA_710, null, nombreClase, nombreMetodo, null, input.getCodArea());
                }
            }
        }

        if (metodo == Conf.METODO_POST) {
            String origenMovil = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_ORIGEN, Conf.SOL_ORIGEN_MOVIL, input.getCodArea());

            if (input.getOrigen() == null || "".equals(input.getOrigen())) {
                return getMensaje(Conf_Mensajes.MSJ_ORIGEN_NULO_75, null, nombreClase, nombreMetodo, null, input.getCodArea());

            } else if (!input.getOrigen().equalsIgnoreCase(origenMovil) && !input.getOrigen().equalsIgnoreCase(origenPC)) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_ORIGEN_TIPO_326, null, nombreClase, nombreMetodo, origenPC + " o " + origenMovil, input.getCodArea());
            }

            if (input.getOrigen().equalsIgnoreCase(origenPC)) {
                if (input.getIdDeuda() == null || "".equals(input.getIdDeuda()) || !isNumeric(input.getIdDeuda())) {
                    return getMensaje(Conf_Mensajes.MSJ_ERROR_INPUT_IDDEUDA_893, null, nombreClase, nombreMetodo, null, input.getCodArea());
                } else {
                    condiciones.clear();
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCSOLICITUDID, input.getIdDeuda()));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Solicitud.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Solicitud.CAMPO_ESTADO, estadoPendiente));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_OR, Solicitud.CAMPO_ESTADO, estadoRechazadaTelca));
                    String existencia = UtileriasBD.verificarExistencia(conn, Solicitud.N_TABLA, condiciones);

                    if (existencia == null || "".equals(existencia) || new Integer(existencia) <= 0) {
                        return getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_IDDEUDA_894, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    }
                }
            } else {
                if (input.getIdJornada() == null || "".equals(input.getIdJornada()) || !isNumeric(input.getIdJornada())) {
                    return getMensaje(Conf_Mensajes.MSJ_IDJORNADA_NULO_89, null, nombreClase, nombreMetodo, null, input.getCodArea());

                } else {
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCJORNADAVENID, input.getIdJornada()));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Jornada.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_AVANZADO_AND, "",
                            "(SELECT COUNT(1) FROM TC_SC_VENTA WHERE TCSCCATPAISID = " + ID_PAIS + " AND TCSCJORNADAVENID = J.TCSCJORNADAVENID) > 0"));
                    condiciones.add(UtileriasJava.setCondicionConfig(Jornada.CAMPO_ESTADO, Conf.GRUPO_JORNADA_ESTADOS, Conf.JORNADA_ESTADO_INICIADA, input.getCodArea()));
                    String existencia = UtileriasBD.verificarExistencia(conn, Jornada.N_TABLA + " J", condiciones);

                    if (existencia == null || "".equals(existencia) || new Integer(existencia) <= 0) {
                        return getMensaje(Conf_Mensajes.MSJ_JORNADA_NOEXISTE_93, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    }
                }
            }

            for (int i = 0; i < input.getRemesas().size(); i++) {
                if (input.getOrigen().equalsIgnoreCase(origenPC)) {
                    // validaciones de pc
                    if (input.getRemesas().get(i).getBanco() == null ||"".equals(input.getRemesas().get(i).getBanco().trim())) {
                        return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_BANCO_701, null, nombreClase, nombreMetodo, "Elemento No. " + (i + 1), input.getCodArea());
                    } else {
                        if (UtileriasJava.getCountConfigValor(conn, Conf.GRUPO_BANCOS, input.getRemesas().get(i).getBanco().toUpperCase(), estadoAlta, input.getCodArea()) < 1) {
                            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_BANCO_TIPO_702, null, nombreClase, nombreMetodo, null, input.getCodArea());
                        }
                    }
                    // validaciones generales
                    if (input.getRemesas().get(i).getMonto() == null || "".equals(input.getRemesas().get(i).getMonto())
                            || !isDecimal(input.getRemesas().get(i).getMonto())) {
                        return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_MONTO_REMESA_711, null, nombreClase, nombreMetodo, "Elemento No. " + (i + 1), input.getCodArea());
                    }

                    if (input.getRemesas().get(i).getNoBoleta() == null || "".equals(input.getRemesas().get(i).getNoBoleta().trim())) {
                        return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_NOBOLETA_712, null, nombreClase, nombreMetodo, "Elemento No. " + (i + 1), input.getCodArea());
                    } else {
                        condiciones.clear();
                        if (input.getOrigen().equalsIgnoreCase(origenPC)) {
                            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.CAMPO_BANCO, input.getRemesas().get(i).getBanco()));
                            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.CAMPO_NO_BOLETA, input.getRemesas().get(i).getNoBoleta()));
                            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Remesa.CAMPO_TCSCCATPAISID, "" + ID_PAIS));
                            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.CAMPO_ORIGEN, input.getOrigen()));
                        } else {
                            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_AVANZADO_AND, "",
                                "(TCSCCTABANCARIAID IN (SELECT TCSCCTABANCARIAID FROM TC_SC_CTA_BANCARIA WHERE TCSCCATPAISID = " + ID_PAIS
                                    + " AND BANCO IN (SELECT BANCO FROM TC_SC_CTA_BANCARIA WHERE TCSCCTABANCARIAID = " + input.getRemesas().get(i).getIdCuenta()
                                    + " AND TCSCCATPAISID = " + ID_PAIS + ") AND ESTADO = '" + estadoAlta + "')" + " AND TCSCCATPAISID = " + ID_PAIS
                                    + " AND NO_BOLETA = '" + input.getRemesas().get(i).getNoBoleta() + "')"
                                + " OR (BANCO = (SELECT BANCO FROM TC_SC_CTA_BANCARIA WHERE TCSCCTABANCARIAID = " + input.getRemesas().get(i).getIdCuenta()
                                + " AND TCSCCATPAISID = " + ID_PAIS + ") AND TCSCCATPAISID = " + ID_PAIS + " AND NO_BOLETA = '" + input.getRemesas().get(i).getNoBoleta() + "')"));
                            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.CAMPO_ORIGEN, input.getOrigen()));
                        }

                        int existencia = UtileriasBD.selectCount(conn, Remesa.N_TABLA, condiciones);

                        if (existencia > 0) {
                            return getMensaje(Conf_Mensajes.MSJ_EXISTE_BOLETA_BANCO_753, null, nombreClase, nombreMetodo, (i + 1) + ".", input.getCodArea());
                        }
                    }
                } 
                else
                {
                    if (input.getRemesas().get(i).getIdCuenta() == null || "".equals(input.getRemesas().get(i).getIdCuenta())
                            || !isNumeric(input.getRemesas().get(i).getIdCuenta())) {
                        return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDCUENTA_700, null, nombreClase, nombreMetodo, "Elemento No. " + (i + 1), input.getCodArea());

                    } else {
                        condiciones.clear();
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Cuenta.CAMPO_TCSCCTABANCARIAID, input.getRemesas().get(i).getIdCuenta()));
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Cuenta.CAMPO_TCSCCATPAISID, "" + ID_PAIS));
                        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Cuenta.CAMPO_ESTADO, estadoAlta));
                        String bancoCuenta = UtileriasBD.getOneRecord(conn, Cuenta.CAMPO_BANCO, Cuenta.N_TABLA, condiciones);

                        if (bancoCuenta == null ||"".equals( bancoCuenta)) {
                        	input.getRemesas().get(i).setBanco("NULL");
                        } else {
                            input.getRemesas().get(i).setBanco(bancoCuenta);
                        }
                    }
                }

               
            }

            int numeroRemesa = 1;
            for (InputRemesa detalleActual : input.getRemesas()) {
                int indexDetalle = 1;

                for (InputRemesa detalle : input.getRemesas()) {
                    if (input.getOrigen().equalsIgnoreCase(origenPC)) {
                        if (indexDetalle != numeroRemesa && detalle.getBanco().trim().equalsIgnoreCase(detalleActual.getBanco().trim())
                                && detalle.getNoBoleta().trim().equals(detalleActual.getNoBoleta().trim())) {
                            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_OC_DET_IGUALES_237,
                                    this.getClass().toString(), null, nombreMetodo, numeroRemesa + " y " + indexDetalle + ".", input.getCodArea());
                        }

                    } else {

                        if (indexDetalle != numeroRemesa && detalle.getIdCuenta().trim().equals(detalleActual.getIdCuenta().trim())
                                && detalle.getNoBoleta().trim().equals(detalleActual.getNoBoleta().trim())) {
                            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_OC_DET_IGUALES_237,
                                    this.getClass().toString(), null, nombreMetodo, numeroRemesa + " y " + indexDetalle + ".", input.getCodArea());
                        }

                        if (indexDetalle != numeroRemesa && detalle.getIdRemesa().trim().equals(detalleActual.getIdRemesa().trim())) {
                            return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_OC_DET_IGUALES_237,
                                    this.getClass().toString(), null, nombreMetodo, "idRemesa " + numeroRemesa + " y " + indexDetalle + ".", input.getCodArea());
                        }
                    }

                    indexDetalle++;
                }

                numeroRemesa++;
            }
        }

        if (metodo == Conf.METODO_GET) {
            if (input.getIdRemesa() != null && !"".equals(input.getIdRemesa()) && !isNumeric(input.getIdRemesa())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDREMESA_710, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }

            if (input.getIdJornada() != null && !"".equals(input.getIdJornada()) && !isNumeric(input.getIdJornada())) {
                return getMensaje(Conf_Mensajes.MSJ_IDJORNADA_NULO_89, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }

            if (input.getBanco() != null && !"".equals(input.getBanco().trim())) {
                String banco = UtileriasJava.getConfig(conn, Conf.GRUPO_BANCOS, input.getBanco().toUpperCase(), input.getCodArea());

                if (banco == null || banco.equals("")) {
                    return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_BANCO_TIPO_702, null, nombreClase, nombreMetodo, null, input.getCodArea());
                }
            }

            if (input.getIdCuenta() != null && !"".equals(input.getIdCuenta()) && !isNumeric(input.getIdCuenta())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDCUENTA_700, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }

            if (input.getIdDistribuidor() != null && !"".equals(input.getIdDistribuidor())
                    && !isNumeric(input.getIdDistribuidor())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDDTS_NUM_162, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }

            if (input.getIdTipo() != null && !"".equals(input.getIdTipo()) && !isNumeric(input.getIdTipo())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDTIPO_NUM_167, null, nombreClase, nombreMetodo, null, input.getCodArea());

            } else if (input.getIdTipo() != null && !"".equals(input.getIdTipo())) {
                if (input.getTipo() == null || "".equals(input.getTipo())) {
                    return getMensaje(Conf_Mensajes.MSJ_TIPO_NULO_431, null, nombreClase, nombreMetodo, null, input.getCodArea());
                } else {
                    String tipoPanelRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, input.getTipo().toUpperCase(), input.getCodArea());

                    if (tipoPanelRuta == null || "".equals(tipoPanelRuta)) {
                        return getMensaje(Conf_Mensajes.MSJ_ERROR_TIPOOFERTA_NO_DEFINIDO_424, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    }
                }
            }

            if (input.getIdVendedor() != null && !"".equals(input.getIdVendedor())
                    && !isNumeric(input.getIdVendedor())) {
                return getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVENDEDOR_NUM_138, null, nombreClase, nombreMetodo, null, input.getCodArea());
            }

            // Filtro rango de fechas
            if ((input.getFechaInicio() != null && !"".equals(input.getFechaInicio().trim()))
                    && (input.getFechaFin() != null && !"".equals(input.getFechaFin().trim()))) {
                SimpleDateFormat formatoFecha = new SimpleDateFormat(Conf.TXT_FORMATO_FECHA_CORTA);
                Date fechaInicio = UtileriasJava.parseDate(input.getFechaInicio(), formatoFecha);
                Date fechaFin = UtileriasJava.parseDate(input.getFechaFin(), formatoFecha);
                long diferenciaDias = 0;
                if (fechaInicio != null && fechaFin != null) {
                    if (fechaInicio.after(fechaFin)) {
                        return getMensaje(Conf_Mensajes.MSJ_FECHAFIN_MENOR_62, null, nombreClase, nombreMetodo, null, input.getCodArea());

                    } else {
                        diferenciaDias = fechaFin.getTime() - fechaInicio.getTime();
                        long dias = diferenciaDias / (1000 * 60 * 60 * 24);
                        int totalDias = (int) dias;
                        if (totalDias > 31) {
                            return getMensaje(Conf_Mensajes.MSJ_RANGO_FECHA_INVALIDO_511, null,
                                    this.getClass().toString(), nombreMetodo, null, input.getCodArea());
                        }
                    }
                }
            }
        }

        return new Respuesta();
    }

    /**
     * Funci\u00F3n que indica los campos que se mostrar\u00E9n en los m\u00E9todos GET 
     * y en este caso tambi\u00E9n todos los que se insertar\u00E9n en el m\u00E9todo POST.
     * 
     * @param conn 
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * 
     * @return campos Array con los nombres de los campos.
     * @throws SQLException 
     */
    public static String[][] obtenerCamposGet(Connection conn, String codArea, BigDecimal ID_PAIS) throws SQLException {
        String tipoRuta = UtileriasJava.getConfig(conn, Conf.GRUPO_PANELRUTA, Conf.TIPO_RUTA, codArea);
        String nombreTipo = "CASE";
        
        nombreTipo += " WHEN " + Jornada.N_TABLA + "." + Jornada.CAMPO_DESCRIPCION_TIPO + " = '" + tipoRuta + "' THEN "
                + "(SELECT A." + Ruta.CAMPO_NOMBRE + " FROM " + Ruta.N_TABLA + " A WHERE A."
                + Ruta.CAMPO_TC_SC_RUTA_ID + " = " + Jornada.N_TABLA + "." + Jornada.CAMPO_IDTIPO 
                + " AND A." + Ruta.CAMPO_TCSCCATPAISID + " = " + Jornada.CAMPO_TCSCCATPAISID
                + " AND A." + Ruta.CAMPO_TCSCCATPAISID + " = " + ID_PAIS + ")";
        
        nombreTipo += " WHEN " + Jornada.N_TABLA + "." + Jornada.CAMPO_DESCRIPCION_TIPO + " != '" + tipoRuta + "' THEN "
                + "(SELECT A." + Panel.CAMPO_NOMBRE + " FROM " + Panel.N_TABLA + " A WHERE A."
                + Panel.CAMPO_TCSCPANELID + " = " + Jornada.N_TABLA + "." + Jornada.CAMPO_IDTIPO
                + " AND A." + Panel.CAMPO_TCSCCATPAISID + " = " + Jornada.CAMPO_TCSCCATPAISID
                + " AND A." + Ruta.CAMPO_TCSCCATPAISID + " = " + ID_PAIS + ")";
        
        nombreTipo += " END AS NOMBRE_TIPO";
        
        String campos[][] = {
            { Remesa.N_TABLA, Remesa.CAMPO_TCSCREMESAID },
            { Remesa.N_TABLA, Remesa.CAMPO_TCSCJORNADAVENID },
            { Distribuidor.N_TABLA, Distribuidor.CAMPO_NOMBRES },
            { Jornada.N_TABLA, Jornada.CAMPO_VENDEDOR },
            { VendedorDTS.N_TABLA, VendedorDTS.CAMPO_USUARIO },
            { Jornada.N_TABLA, Jornada.CAMPO_TCSCDTSID },
            { Jornada.N_TABLA, Jornada.CAMPO_IDTIPO },
            { Jornada.N_TABLA, Jornada.CAMPO_DESCRIPCION_TIPO },
            { Jornada.N_TABLA, Jornada.CAMPO_FECHA },
            { Jornada.N_TABLA, Jornada.CAMPO_FECHA_FINALIZACION },
            { Jornada.N_TABLA, Jornada.CAMPO_FECHA_LIQUIDACION },
            { Jornada.N_TABLA, Jornada.CAMPO_ESTADO + " AS ESTADO_JORNADA" },
            { Jornada.N_TABLA, Jornada.CAMPO_ESTADO_LIQUIDACION },
            { null, nombreTipo },
            { Remesa.N_TABLA, Remesa.CAMPO_MONTO },
            { Remesa.N_TABLA, Remesa.CAMPO_TASA_CAMBIO },
            { Remesa.N_TABLA, Remesa.CAMPO_NO_BOLETA },
            { Remesa.N_TABLA, Remesa.CAMPO_TCSCCTABANCARIAID },
            { Cuenta.N_TABLA, Cuenta.CAMPO_BANCO },
            { Cuenta.N_TABLA, Cuenta.CAMPO_NO_CUENTA },
            { Cuenta.N_TABLA, Cuenta.CAMPO_TIPO_CUENTA },
            { Cuenta.N_TABLA, Cuenta.CAMPO_NOMBRE_CUENTA },
            { Remesa.N_TABLA, Remesa.CAMPO_ESTADO },
            { Remesa.N_TABLA, Remesa.CAMPO_ID_REMESA_MOVIL},
            { Remesa.N_TABLA, Remesa.CAMPO_ORIGEN },
            { Remesa.N_TABLA, Remesa.CAMPO_CREADO_EL },
            { Remesa.N_TABLA, Remesa.CAMPO_CREADO_POR },
            { Remesa.N_TABLA, Remesa.CAMPO_MODIFICADO_EL },
            { Remesa.N_TABLA, Remesa.CAMPO_MODIFICADO_POR }
        };
        
        return campos;
    }
    
    public static String[] obtenerCamposPost() {
        String campos[] = {
            Remesa.CAMPO_TCSCREMESAID,
            Remesa.CAMPO_TCSCJORNADAVENID,
            Remesa.CAMPO_TCSCSOLICITUDID,
            Remesa.CAMPO_TCSCCATPAISID,
            Remesa.CAMPO_MONTO,
            Remesa.CAMPO_TASA_CAMBIO,
            Remesa.CAMPO_NO_BOLETA,
            Remesa.CAMPO_TCSCCTABANCARIAID,
            Remesa.CAMPO_BANCO,
            Remesa.CAMPO_ESTADO,
            Remesa.CAMPO_ORIGEN,
            Remesa.CAMPO_CREADO_EL,
            Remesa.CAMPO_CREADO_POR,
            Remesa.CAMPO_ID_REMESA_MOVIL,
            Remesa.CAMPO_CODIGO_DISPOSITIVO
        };
        
        return campos;
    }
        
    /**
     * Funci\u00F3n que indica las condiciones que se aplicar\u00E9n en las diferentes consultas
     * seg\u00FAn el m\u00E9todo que se trabaje.
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo Valor que indica tipo de operaci\u00F3n que se desea realizar en el servicio.
     * @return condiciones Listado con los filtros indicados seg\u00FAn el m\u00E9todo deseado.
     */
    public static List<Filtro> obtenerCondiciones(InputRemesa input, BigDecimal ID_PAIS) {
        List<Filtro> condiciones = new ArrayList<Filtro>();

        condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Remesa.N_TABLA,
                Remesa.CAMPO_TCSCCATPAISID, "" + ID_PAIS));

        if (input.getOrigen() != null && !"".equals(input.getOrigen().trim())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.N_TABLA,
                    Remesa.CAMPO_ORIGEN, input.getOrigen()));
        }

        if (input.getIdRemesa() != null && !"".equals(input.getIdRemesa().trim())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Remesa.N_TABLA,
                    Remesa.CAMPO_TCSCREMESAID, input.getIdRemesa()));
        }

        if (input.getIdJornada() != null && !"".equals(input.getIdJornada().trim())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Remesa.N_TABLA,
                    Remesa.CAMPO_TCSCJORNADAVENID, input.getIdJornada()));
        }

        if (input.getNoBoleta() != null && !"".equals(input.getNoBoleta().trim())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.N_TABLA,
                    Remesa.CAMPO_NO_BOLETA, input.getNoBoleta()));
        }

        if (input.getBanco() != null && !"".equals(input.getBanco().trim())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Cuenta.N_TABLA,
                    Cuenta.CAMPO_BANCO, input.getBanco()));
        }

        if (input.getIdCuenta() != null && !"".equals(input.getIdCuenta().trim())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Remesa.N_TABLA,
                    Remesa.CAMPO_TCSCCTABANCARIAID, input.getIdCuenta()));
        }

        if (input.getIdDistribuidor() != null && !"".equals(input.getIdDistribuidor().trim())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Jornada.N_TABLA,
                    Jornada.CAMPO_TCSCDTSID, input.getIdDistribuidor()));
        }

        if ((input.getIdTipo() != null && isNumeric(input.getIdTipo())&& !"".equals(input.getIdTipo().trim()))&& isNumeric(input.getIdTipo())){
        	          condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Jornada.N_TABLA, Jornada.CAMPO_IDTIPO, input.getIdTipo()));
        	          condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Jornada.N_TABLA, Jornada.CAMPO_DESCRIPCION_TIPO, input.getTipo()));
        }

        if (input.getIdVendedor() != null && !"".equals(input.getIdVendedor().trim())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_ID_NUMERICO_AND, Jornada.N_TABLA,
                    Jornada.CAMPO_VENDEDOR, input.getIdVendedor()));
        }

        if (input.getEstado() != null && !"".equals(input.getEstado().trim())) {
            condiciones.add(UtileriasJava.setCondicionMultiple(Conf.FILTRO_TEXTO_UPPER_AND, Remesa.N_TABLA,
                    Remesa.CAMPO_ESTADO, input.getEstado()));
        }

        if ((input.getFechaFin() != null && input.getFechaInicio() != null) && (!"".equals(input.getFechaFin().trim()) && !"".equals(input.getFechaInicio().trim()))) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_RANGO_FECHAS,
                        Remesa.N_TABLA + "." + Remesa.CAMPO_CREADO_EL, input.getFechaInicio(), input.getFechaFin(),
                        Conf.TXT_FORMATO_FECHA_CORTA));
        }

        return condiciones;
    }
    
    /**
     * M\u00E9todo principal que realiza las operaciones generales del servicio REST
     * 
     * @param input Objeto con los datos enviados mediante POST.
     * @param metodo M\u00E9todo que indica las acciones a realizar.
     * @return output Respuesta y listado con los Remesas encontrados.
     */
    public OutputRemesa getDatos(InputRemesa input, int metodo) {
        List<LogSidra> listaLog = new ArrayList<LogSidra>();
        String nombreMetodo = "getDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        OutputRemesa output = new OutputRemesa();
        Respuesta respuesta = new Respuesta();

        Connection conn = null;
        try {
            conn = getConnRegional();
            log.trace("Usuario: " + input.getUsuario());
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());

            String estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_REMESA, Conf.REMESA_ESTADO_ALTA, input.getCodArea());
            String estadoPendiente = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD, Conf.SOL_ESTADO_PENDIENTE, input.getCodArea());
            String estadoRechazadaTelca = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_SOLICITUD, Conf.SOL_ESTADO_RECHAZADA_TELCA, input.getCodArea());
            String tipoDeuda = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_TIPO, Conf.SOL_TIPO_DEUDA, input.getCodArea());
            String tipoTarjeta = UtileriasJava.getConfig(conn, Conf.GRUPO_FORMA_PAGO_VENTA, Conf.FORMA_PAGO_TARJETA, input.getCodArea());
            String origenPC = UtileriasJava.getConfig(conn, Conf.GRUPO_SOLICITUDES_ORIGEN, Conf.SOL_ORIGEN_PC, input.getCodArea());
            String estadoAnulado = UtileriasJava.getConfig(conn, Conf.ESTADOS_ANULADOS, Conf.ESTADOS_ANULADOS, input.getCodArea());

            // Validaci\u00F3n de datos en el input
            respuesta = validarInput(conn, input, metodo, origenPC, estadoAlta, estadoPendiente, estadoRechazadaTelca, ID_PAIS);
            log.trace("Respuesta validaci\u00F3n: " + (respuesta.getDescripcion() == null ? "OK" : respuesta.getDescripcion()));
            log.trace("Respuesta validaci\u00F3n: " + (respuesta.getDescripcion()));
           if (respuesta.getCodResultado() !=null) {
            
                output.setRespuesta(respuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_REMESA, servicioPost, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", respuesta.getDescripcion()));

                return output;
            }

            if (metodo == Conf.METODO_GET) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo GET
                try {
                    output = OperacionRemesa.doGet(conn, input,  tipoTarjeta, estadoAnulado, ID_PAIS);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de Remesas.", ""));
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al consultar datos de Remesas.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_POST) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo POST
                try {
                    output = OperacionRemesa.doPost(conn, input, estadoAlta, origenPC, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_CREA_REMESA_52) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_REMESA, servicioPost, "0",
                                Conf.LOG_TIPO_REMESA,
                                "Se registr\u00F3 la cantidad de " + output.getDatos().size() + " remesa(s) nueva(s) de la"
                                        + (input.getIdDeuda() != null && !input.getIdDeuda().trim().equals("")
                                                ? " deuda con ID " + input.getIdDeuda() : " jornada con ID " + input.getIdJornada())
                                        + ", desde origen " + input.getOrigen().toUpperCase() + ".", ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_REMESA, servicioPost, "0",
                                Conf.LOG_TIPO_NINGUNO, "Problema al crear Remesa.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output.setRespuesta(respuesta);

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_REMESA, servicioPost, "0",
                            Conf.LOG_TIPO_NINGUNO, "Problema al crear Remesas.", e.getMessage()));
                }

            } else if (metodo == Conf.METODO_PUT) {
                // Porci\u00F3n que se ejecuta al provenir de un m\u00E9todo PUT
                try {
                    output = OperacionRemesa.doPut(conn, input,  estadoPendiente, tipoDeuda, estadoRechazadaTelca, ID_PAIS);

                    if (new BigDecimal(output.getRespuesta().getCodResultado()).intValue() == Conf_Mensajes.OK_MOD_REMESA_53) {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_REMESA, servicioPut,
                                "0", Conf.LOG_TIPO_REMESA,
                                "Se modificaron datos de la Remesa con el ID " + input.getIdRemesa() + ".", ""));
                    } else {
                        listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_REMESA, servicioPut,
                                "0", Conf.LOG_TIPO_REMESA, "Problema al modificar datos de Remesa.",
                                output.getRespuesta().getDescripcion()));
                    }
                } catch (SQLException e) {
                    respuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

                    log.error("Excepcion: " + e.getMessage(), e);
                    output.setRespuesta(respuesta);

                    listaLog.add(
                            ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_REMESA, servicioPut, input.getIdRemesa(),
                                    Conf.LOG_TIPO_REMESA, "Problema al modificar Remesa.", e.getMessage()));
                }
            }
        } catch (Exception e) {
            respuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, null, input.getCodArea());

            log.error("Excepcion: " + e.getMessage(), e);
            output.setRespuesta(respuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CREA_REMESA, servicioPost, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema en el servicio de Remesas.", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}

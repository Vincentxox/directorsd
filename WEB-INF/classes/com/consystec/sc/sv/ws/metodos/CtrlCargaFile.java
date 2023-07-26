package com.consystec.sc.sv.ws.metodos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.dbutils.DbUtils;

import com.consystec.sc.ca.ws.input.file.InputCargaFile;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.file.OutputImagen;
import com.consystec.sc.sv.ws.operaciones.OperacionCargaFile;
import com.consystec.sc.sv.ws.orm.ImgPDV;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.PuntoVenta;
import com.consystec.sc.sv.ws.orm.Visita;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author susana barrios Consystec 2015
 **/
public class CtrlCargaFile extends ControladorBase {
    private  List<LogSidra> listaLog = new ArrayList<LogSidra>();
    private static String servicioGet = Conf.LOG_GET_CARGA_FILE;
    private static String servicioPost = Conf.LOG_POST_CARGA_FILE;
    private static String servicioDel = Conf.LOG_DEL_CARGA_FILE;
    
    /**
     * Validando que no vengan parametros nulos.
     * 
     * @param conn
     * @param objDatos
     * @param banderaVisita
     * @param banderaEdicionPDV_PC
     * @param maxFotos
     * @return
     * @throws SQLException
     */
    public Respuesta validarDatos(Connection conn, InputCargaFile objDatos, boolean banderaVisita,
            boolean banderaEdicionPDV_PC, int maxFotos, BigDecimal ID_PAIS) throws SQLException {
        Respuesta objRespuesta = null;
        String nombreMetodo = "validarDatos";
        String nombreClase = new CurrentClassGetter().getClassName();

        List<Filtro> condiciones = new ArrayList<Filtro>();
        int existencia = 0;

        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
        }

        if (objDatos.getIdPDV() == null || "".equals(objDatos.getIdPDV())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDPDV_VACIO_8, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
        } else {
            String estadoActivo = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS_ESTADOS_PDV, Conf.ESTADO_ACTIVO, objDatos.getCodArea());

            // se valida que exista el punto de venta
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, PuntoVenta.CAMPO_ESTADO, estadoActivo));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCPUNTOVENTAID, objDatos.getIdPDV()));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, PuntoVenta.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));

            existencia = UtileriasBD.selectCount(conn, PuntoVenta.N_TABLA, condiciones);
            if (existencia <= 0) {
                log.error("No existe el punto de venta.");
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_NO_EXISTE_PDV_472, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
            }
        }

        if (objDatos.getArchivo() == null || "".equals(objDatos.getArchivo())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_ARCHIVO_NULO_371, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
        }

        if (objDatos.getNombreArchivo() == null || "".equals(objDatos.getNombreArchivo())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOMBREARCHIVO_NULO_272, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());

        } else if (objDatos.getNombreArchivo().length() > Conf.LONG_NOMBRE_ARCHIVO) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_LENG_NOMBRE_844, null, nombreClase, nombreMetodo,
                    "(" + Conf.LONG_NOMBRE_ARCHIVO + ").", objDatos.getCodArea());
        }

        if (objDatos.getExtension() == null || "".equals(objDatos.getExtension())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_EXTENSION_NULO_270, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
        }

        if (banderaVisita && !banderaEdicionPDV_PC) {
            if (!isNumeric(objDatos.getIdVisita())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVISITA_795, null, nombreClase, nombreMetodo, null, objDatos.getCodArea());
                return objRespuesta;
            }

            // verificando que exista el idVisita
            if (existencia < verificarVisita(conn, objDatos, ID_PAIS)) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_NO_EXISTE_IDVISITA_796, null, null, nombreMetodo, null,objDatos.getCodArea());
                return objRespuesta;
            }

        }

        if (objRespuesta != null) {
            log.trace("resultado: " + objRespuesta.getDescripcion());
        }

        return objRespuesta;
    }

    private int verificarVisita(Connection conn, InputCargaFile objDatos, BigDecimal ID_PAIS) throws SQLException {
        int existencia = 0;
        PreparedStatement pstm = null;
        ResultSet rst = null;
       
	        String sql = "SELECT COUNT(1) FROM "
	                + getParticion(Visita.N_TABLA, Conf.PARTITION, "",objDatos.getCodArea()) + " V, "
                    + getParticion(Jornada.N_TABLA, Conf.PARTITION, "",objDatos.getCodArea()) + " J "
	                + "WHERE V.TCSCVISITAID =? AND V.TCSCPUNTOVENTAID =? " 
	                + " AND V.TCSCJORNADAVENID = J.TCSCJORNADAVENID AND J.ESTADO = "
	                    + "(SELECT VALOR FROM TC_SC_CONFIGURACION WHERE GRUPO = ?"
	                            + " AND TCSCCATPAISID =? AND NOMBRE = ?)"
	                                    + " AND V.TCSCCATPAISID =? AND J.TCSCCATPAISID = ?";
	
            log.debug("Qry existencia visita y jornada: " + sql);
        try {
        	pstm = conn.prepareStatement(sql);
        	pstm.setBigDecimal(1, new BigDecimal(objDatos.getIdVisita()));
        	pstm.setBigDecimal(2, new BigDecimal(objDatos.getIdPDV()));
        	pstm.setString(3,  Conf.GRUPO_JORNADA_ESTADOS);
        	pstm.setBigDecimal(4,ID_PAIS);
        	pstm.setString(5,  Conf.JORNADA_ESTADO_INICIADA);
        	pstm.setBigDecimal(6,ID_PAIS);
        	pstm.setBigDecimal(7,ID_PAIS);
           
            rst = pstm.executeQuery();
            if (rst.next()) {
                existencia = rst.getInt(1);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstm);
        }
        return existencia;
    }

    public OutputImagen cargarImagen(InputCargaFile input) throws NumberFormatException {
        String nombreMetodo = "cargarImagen";
        String nombreClase = new CurrentClassGetter().getClassName();
        listaLog = new ArrayList<LogSidra>();
        Connection conn = null;
        Respuesta objRespuesta = new Respuesta();
        OutputImagen output = new OutputImagen();
        String servicio = servicioPost;
        String transaccion = Conf.LOG_TRANSACCION_CREA_CARGA_FILE;
        boolean banderaVisita = false;

        boolean banderaEdicionPDV_Movil = false;
        boolean banderaEdicionPDV_PC = false;
        String textoLog = "";

        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());
            int maxFotos = new Integer(UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.FOTOGRAFIAS_MAXIMAS,input.getCodArea()));

            if ("WEB".equalsIgnoreCase(input.getToken())) {
                banderaEdicionPDV_PC = true;
            } else {
                banderaVisita = input.getIdVisita() != null && !"".equals(input.getIdVisita());

                if (!banderaVisita) {
                    banderaEdicionPDV_Movil = true;
                }
            }

            objRespuesta = validarDatos(conn, input, banderaVisita, banderaEdicionPDV_PC, maxFotos, ID_PAIS);
            if (objRespuesta == null) {
                BigDecimal idImgPdv = null;
                byte[] imagen = null;

                // Convirtiendo en un arreglo de bytes la imagen recibida en base 64
                imagen = DatatypeConverter.parseBase64Binary(input.getArchivo());

                if (banderaVisita) {
                    textoLog = "a la Visita con ID " + input.getIdVisita() + ".";
                } else {
                    textoLog = "al PDV con ID " + input.getIdPDV() + ".";
                }

                if (banderaVisita) {
                    // se crea el registro
                    idImgPdv = OperacionCargaFile.saveImagen(conn, new BigDecimal(input.getIdPDV()), imagen,
                            input.getNombreArchivo(), input.getExtension(), input.getUsuario(), input.getIdVisita(),
                            input.getObservaciones(), ID_PAIS);

                } else {
                    // se verifica la cantidad de fotos del pdv
                    List<Filtro> condiciones = new ArrayList<Filtro>();
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ImgPDV.CAMPO_TCSCPUNTOVENTAID, input.getIdPDV()));
                    condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NULL_AND, ImgPDV.CAMPO_TCSCVISITAID, null));
                    String cantFotosPDV = UtileriasBD.getOneRecord(conn, UtileriasJava.setSelect(Conf.SELECT_COUNT, ImgPDV.CAMPO_TCSCIMGPDVID),
                            ImgPDV.N_TABLA, condiciones);

                    if (banderaEdicionPDV_PC) {
                        if (new Integer(cantFotosPDV) >= maxFotos) {
                            objRespuesta = new Respuesta();
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_MAX_FOTO_PDV_372, null, null,
                                    nombreMetodo, maxFotos + ".",input.getCodArea());
                            return output;
                        }
                        // se crea el registro
                        idImgPdv = OperacionCargaFile.saveImagen(conn, new BigDecimal(input.getIdPDV()), imagen,
                                input.getNombreArchivo(), input.getExtension(), input.getUsuario(), input.getIdVisita(),
                                input.getObservaciones(), ID_PAIS);

                    } else if (banderaEdicionPDV_Movil) {
                        if (new Integer(cantFotosPDV) < maxFotos) {
                            // si la cantidad de imagenes es menor a 3 se crea nueva
                            idImgPdv = OperacionCargaFile.saveImagen(conn, new BigDecimal(input.getIdPDV()), imagen,
                                    input.getNombreArchivo(), input.getExtension(), input.getUsuario(),
                                    input.getIdVisita(), input.getObservaciones(), ID_PAIS);

                        } else {
                            // si ya existen 3 imagenes se borra la mas antigua y se crea la nueva

                            //se obtiene el registro de la mas antigua y se borra
                            idImgPdv = OperacionCargaFile.getIdImgPDV(conn, input.getIdPDV(), input.getCodArea(), ID_PAIS);
                            boolean update = OperacionCargaFile.delImagen(conn, idImgPdv,input.getCodArea(), ID_PAIS);
                            if (!update) {
                                idImgPdv = null;
                            } else {
                                // se crea el registro de nueva imagen
                                idImgPdv = OperacionCargaFile.saveImagen(conn, new BigDecimal(input.getIdPDV()), imagen,
                                        input.getNombreArchivo(), input.getExtension(), input.getUsuario(),
                                        input.getIdVisita(), input.getObservaciones(), ID_PAIS);
                            }

                        }
                    }
                }

                if (idImgPdv != null) {
                    objRespuesta = getMensaje(Conf_Mensajes.OK_CARGAFILE5, null, nombreClase, nombreMetodo, null, input.getCodArea());
                    output.setIdImgPDV(idImgPdv + "");

                    listaLog.add(ControladorBase.addLog(transaccion, servicio, idImgPdv + "", Conf.LOG_TIPO_CARGA_FILE,
                            "Se cre\u00F3 nueva carga de imagen " + textoLog, ""));
                } else {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_CREADO, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());

                    listaLog.add(ControladorBase.addLog(transaccion, servicio, "0", Conf.LOG_TIPO_NINGUNO,
                            "Ocurrio un problema al crear carga de imagen al PDV ID " + input.getIdPDV() + ".", ""));
                }

            } else {
                listaLog.add(ControladorBase.addLog(transaccion, servicio, "0", Conf.LOG_TIPO_NINGUNO,
                        "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
            }

        } catch (SQLException e) {
        	log.error(e,e);
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(transaccion, servicio, "0", Conf.LOG_TIPO_NINGUNO,
                    "Problema al cargar la imagen.", e.getMessage()));

        } catch (Exception e) {
        	log.error(e,e);
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

            listaLog = new ArrayList<LogSidra>();
            listaLog.add(ControladorBase.addLog(transaccion, servicio, "0", Conf.LOG_TIPO_NINGUNO,
                    "Problema al cargar la imagen.", e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);
            output.setRespuesta(objRespuesta);
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }

    public OutputImagen getImagen(InputCargaFile input) {
        listaLog = new ArrayList<LogSidra>();
        Respuesta objRespuesta = new Respuesta();
        OutputImagen output = new OutputImagen();
        String nombreMetodo = "getImagen";
        String nombreClase = new CurrentClassGetter().getClassName();
        Connection conn = null;

        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());

            if (input.getIdImgPDV() == null || "".equals(input.getIdImgPDV())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDPDV_VACIO_8, null, nombreClase, nombreMetodo, null, input.getCodArea());
                output.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

            } else if (input.getUsuario() == null || "".equals(input.getUsuario())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, input.getCodArea());
                output.setRespuesta(objRespuesta);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

            } else {
                output = OperacionCargaFile.getImagenPDV(conn, input.getIdImgPDV(), input.getCodArea(), ID_PAIS);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Se consult\u00F3 imagen asociada a PDV", ""));

            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());
            output.setRespuesta(objRespuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar imagen asociada a PDV", e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());
            output.setRespuesta(objRespuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar imagen asociada a PDV", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }

    public OutputImagen delImagen(InputCargaFile input) {
        String nombreMetodo = "delImagen";
        String nombreClase = new CurrentClassGetter().getClassName();
        listaLog = new ArrayList<LogSidra>();
        Respuesta objRespuesta = new Respuesta();
        OutputImagen output = new OutputImagen();
        Connection conn = null;

        try {
            conn = getConnRegional();
            BigDecimal ID_PAIS = getIdPais(conn, input.getCodArea());
            
            if (input.getIdImgPDV() == null || "".equals(input.getIdImgPDV())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_IDPDV_VACIO_8, null, nombreClase, nombreMetodo, null, input.getCodArea());

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CARGA_FILE, servicioDel, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

            } else if (input.getUsuario() == null || "".equals(input.getUsuario())) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, nombreClase, nombreMetodo, null, input.getCodArea());

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CARGA_FILE, servicioDel, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));

            } else {
                // se valida que exista la imagen
                List<Filtro> condiciones = new ArrayList<Filtro>();
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ImgPDV.CAMPO_TCSCIMGPDVID, input.getIdImgPDV()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ImgPDV.CAMPO_TCSCCATPAISID, ID_PAIS.toString()));

                String existencia = UtileriasBD.verificarExistencia(conn, ImgPDV.N_TABLA, condiciones);
                if (new Integer(existencia) <= 0) {
                    log.error("No existe la imagen asociada al punto de venta.");
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_EXISTENTE, null, null, nombreMetodo, null, input.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CARGA_FILE, servicioDel,
                            input.getIdImgPDV(), Conf.LOG_TIPO_CARGA_FILE,
                            "No existe la imagen asociada al punto de venta.", ""));

                    return output;
                }

                boolean update = OperacionCargaFile.delImagen(conn, new BigDecimal(input.getIdImgPDV()), input.getCodArea(), ID_PAIS);

                if (update) {
                    objRespuesta = getMensaje(Conf_Mensajes.OK_DEL_IMAGEN_32, null, nombreClase, nombreMetodo, null, input.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CARGA_FILE, servicioDel,
                            input.getIdImgPDV(), Conf.LOG_TIPO_CARGA_FILE, "Se elimin\u00F3 la imagen asociada a PDV.", ""));
                } else {
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_RECURSO_NO_MODIFICADO, null, nombreClase, nombreMetodo,
                            null, input.getCodArea());

                    listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CARGA_FILE, servicioDel,
                            input.getIdImgPDV(), Conf.LOG_TIPO_NINGUNO, "Ocurrio un problema al eliminar imagen.", ""));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CARGA_FILE, servicioDel, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al eliminar imagen asociada a PDV", e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_MOD_CARGA_FILE, servicioDel, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al eliminar imagen asociada a PDV", e.getMessage()));
        } finally {
            DbUtils.closeQuietly(conn);
            output.setRespuesta(objRespuesta);

            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }

    /**
     * M\u00E9todo para reconstruir la imagen contenida en el arreglo de bytes
     * 
     * @param
     */
    public boolean convertirImagen(byte[] imagen, String nombreImg) {
        /*
         * nuevo archivo que se generar\u00E9 de los datos obtenidos indicando la
         * ubicaci\u00F3n y extensi\u00F3n de la imagen
         */
        File newFile = new File("C:\\repositorio\\" + nombreImg + ".jpg");
        BufferedImage imag;

        /* decodificando imagen obtenida */
        try {
            imag = ImageIO.read(new ByteArrayInputStream(imagen));
            ImageIO.write(imag, "jpg", newFile);

            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public OutputImagen getImagenVisita(InputCargaFile input) {
        String nombreMetodo = "getImagenVisita";
        String nombreClase = new CurrentClassGetter().getClassName();
        OutputImagen output = new OutputImagen();
        Respuesta objRespuesta = null;
        Connection conn = null;

        try {
            conn = getConnRegional();
       

            if (input.getIdPDV() != null && !"".equals(input.getIdPDV()) && !isNumeric(input.getIdPDV())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPDV_NUM_315, null, nombreClase, nombreMetodo,
                        null, input.getCodArea());

            } else if (input.getIdVisita() != null && !"".equals(input.getIdVisita())
                    && !isNumeric(input.getIdVisita())) {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDVISITA_795, null, nombreClase, nombreMetodo,
                        null, input.getCodArea());

            } else if ((input.getIdPDV() == null || "".equals(input.getIdPDV()))
                    && (input.getIdVisita() == null || "".equals(input.getIdVisita()))) {
                // default pide idPDV
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_ERROR_IDPDV_NUM_315, null, nombreClase, nombreMetodo,
                        null, input.getCodArea());

            }

            if (objRespuesta != null) {
                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Problema en la validaci\u00F3n de datos.", objRespuesta.getDescripcion()));
                return output;
            }

            List<Filtro> condiciones = new ArrayList<Filtro>();
            if (input.getIdPDV() != null && !input.getIdPDV().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ImgPDV.CAMPO_TCSCPUNTOVENTAID, input.getIdPDV()));
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_IS_NULL_AND, ImgPDV.CAMPO_TCSCVISITAID, null));
            } else if (input.getIdVisita() != null && !input.getIdVisita().equals("")) {
                condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ImgPDV.CAMPO_TCSCVISITAID, input.getIdVisita()));
            }

            List<Order> orden = new ArrayList<Order>();
            orden.add(new Order(ImgPDV.CAMPO_TCSCIMGPDVID, Order.ASC));

            String[] campos = {
                    ImgPDV.CAMPO_TCSCIMGPDVID,
                    ImgPDV.CAMPO_OBSERVACIONES
            };
            List<Map<String, String>> listado = UtileriasBD.getSingleData(conn, ImgPDV.N_TABLA, campos, condiciones, orden);

            if (listado.isEmpty()) {
                log.debug("No existen registros en la tabla con esos par\u00E9metros.");
                objRespuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_NO_IMAGENES_797, null,
                        nombreClase, nombreMetodo, null, input.getCodArea());

            } else {
                objRespuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CARGA_IMAGENES_21, null, nombreClase,
                        nombreMetodo, null, input.getCodArea());

                List<InputCargaFile> listIDs = new ArrayList<InputCargaFile>();

                InputCargaFile item = null;
                for (int i = 0; i < listado.size(); i++) {
                    item = new InputCargaFile();
                    item.setIdImgPDV(listado.get(i).get(ImgPDV.CAMPO_TCSCIMGPDVID));
                    item.setObservaciones(UtileriasJava.getValue(listado.get(i).get(ImgPDV.CAMPO_OBSERVACIONES)));
                    listIDs.add(item);
                }

                output.setImgAsociadas(listIDs);

                listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                        Conf.LOG_TIPO_NINGUNO, "Se consultaron datos de im\u00E9genes asociadas.", ""));
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());
            output.setRespuesta(objRespuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar im\u00E9genes asociadas a PDV o Visita.", e.getMessage()));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            objRespuesta = getMensaje(Conf_Mensajes.MENSAJE_DEFAULT, e.getMessage(), nombreClase, nombreMetodo, "", input.getCodArea());
            output.setRespuesta(objRespuesta);

            listaLog.add(ControladorBase.addLog(Conf.LOG_TRANSACCION_CONSULTA_DATOS, servicioGet, "0",
                    Conf.LOG_TIPO_NINGUNO, "Problema al consultar im\u00E9genes asociadas a PDV o Visita.", e.getMessage()));

        } finally {
            DbUtils.closeQuietly(conn);
            output.setRespuesta(objRespuesta);
            UtileriasJava.doInsertLog(listaLog, input.getUsuario(), input.getCodArea());
        }

        return output;
    }
}

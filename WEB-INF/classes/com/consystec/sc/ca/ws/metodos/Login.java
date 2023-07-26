package com.consystec.sc.ca.ws.metodos;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.NamingException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.general.InputSesionParametros;
import com.consystec.sc.ca.ws.input.vendedorxdts.ValidarVendedor;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.orm.SesionUsuario;
import com.consystec.sc.ca.ws.output.login.OutputLogin;
import com.consystec.sc.ca.ws.output.vendedorxdts.Outputvendedorxdts;
import com.consystec.sc.ca.ws.util.Conf;
import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.db.StatementException;
import com.consystec.ms.seguridad.corecliente.SeguridadWeb;
import com.consystec.ms.seguridad.dao.UsuarioDAO;
import com.consystec.ms.seguridad.excepciones.ExcepcionClaveExpirada;
import com.consystec.ms.seguridad.excepciones.ExcepcionClaveNoCumplePoliticas;
import com.consystec.ms.seguridad.excepciones.ExcepcionClaveNoValidaLDAP;
import com.consystec.ms.seguridad.excepciones.ExcepcionClaveNoValidaModsec;
import com.consystec.ms.seguridad.excepciones.ExcepcionConfiguracionLdap;
import com.consystec.ms.seguridad.excepciones.ExcepcionSeguridad;
import com.consystec.ms.seguridad.excepciones.ExcepcionUsuarioBloqueado;
import com.consystec.ms.seguridad.excepciones.ExcepcionUsuarioNoExistente;
import com.consystec.ms.seguridad.orm.Perfilappusuariovw;
import com.consystec.ms.seguridad.orm.Usuario;
import com.novell.ldap.LDAPException;

public class Login extends ControladorBase {
    private static final Logger log = Logger.getLogger(Login.class);

    /* VARIABLES GLOBALES */
    int ADVERTENCIA = 0;
    String IDVENDEDOR = null;
    String RESPUESTAV = null;
    String nombreAplicacion = "";

    /* Objetos para la obtenciï¿½n de Datos */
    SesionUsuario RegistroSesion = new SesionUsuario();

    /* VALORES PARA ADVERTENCIAS */
    Respuesta mensajeAdv = new Respuesta();

    /************************************************************
     * M\u00E9todo para validar Datos
     ************************************************************/
    public Respuesta validaDatos(InputSesionParametros sesion) {
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";

        if (sesion.getCodArea() == null || "".equals(sesion.getCodArea())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_CODAREA_NULO_1, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);

        } else if (sesion.getCodArea().length() != Conf.LONG_CODPAIS) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_LONGINVALIDA_AREA_6, null, this.getClass().toString(), metodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);

        } else {
            BigDecimal idPais = null;
            Connection conn = null;
            try {
                conn = getConnLocal();
                idPais = getidpais(conn, sesion.getCodArea());
                if (idPais == null || idPais.intValue() == 0) {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_AREAINVALIDA_8, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                }
            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            } finally {
                DbUtils.closeQuietly(conn);
            }
        }

        if (sesion.getUsuario() == null || "".equals(sesion.getUsuario())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, null, this.getClass().toString(), metodo, null,
                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        if (sesion.getPassword() == null || "".equals(sesion.getPassword())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_PASSWORD_NULO_15, null, this.getClass().toString(), metodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }
        if (sesion.getCodDispositivo() == null || "".equals(sesion.getCodDispositivo().trim())) {
            objRespuesta = new Respuesta();
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_NULO_18, null, this.getClass().toString(), metodo,
                    null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
        }

        return objRespuesta;
    }

    /**************************************************************
     * MÃ©todo para autenticar usuario en Mï¿½dulo de Seguridadd
     * 
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SQLException
     * @throws NamingException
     * @throws ExcepcionUsuarioNoExistente
     * @throws ExcepcionSeguridad
     * @throws StatementException
     * @throws InterruptedException
     * @throws LDAPException 
     * @throws ExcepcionClaveNoValidaLDAP 
     * @throws ExcepcionConfiguracionLdap 
     * @throws ExcepcionUsuarioBloqueado 
     * @throws ExcepcionClaveExpirada 
     * @throws ExcepcionClaveNoValidaModsec 
     * @throws ExcepcionClaveNoCumplePoliticas 
     ****************************************************************/
    private Respuesta autenticarUsuario(InputSesionParametros sesion)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, SQLException, NamingException, InterruptedException, ExcepcionSeguridad,
            ExcepcionUsuarioNoExistente, ExcepcionClaveNoCumplePoliticas, ExcepcionClaveNoValidaModsec,
            ExcepcionClaveExpirada, ExcepcionUsuarioBloqueado, ExcepcionConfiguracionLdap, ExcepcionClaveNoValidaLDAP,
            LDAPException, StatementException {
        Connection conn = null;
        Respuesta objRespuesta = null;
        String metodo = "autenticarUsuario";
        try {
            conn = getConnSeg();
            Usuario userOK = new Usuario();
            SeguridadWeb seguridad = new SeguridadWeb();
            List<Perfilappusuariovw> perfiles = new ArrayList<Perfilappusuariovw>();

            UsuarioDAO usrDao = new UsuarioDAO();
            Usuario userAdmin = new Usuario();

            String perfil = "Vendedor";

            nombreAplicacion = getParametro(Conf.NOMBRE_APPMOVIL, sesion.getCodArea());
            userAdmin = usrDao.obtenerUsuario(conn, sesion.getUsuario());

            if (userAdmin != null) {
                log.trace("Usuario:" + userAdmin.getUsuario());
                IDVENDEDOR = userAdmin.getSecusuarioid().toString();

                // obtener perfil
                log.trace("nombre Aplicacion: " + nombreAplicacion);
                perfiles = seguridad.obtenerPerfilesXAplicacionUsuario(conn, nombreAplicacion, userAdmin);

                if (!perfiles.isEmpty()) {
                    log.trace("tama\u00F1o permisos: " + perfiles.size());
                    for (int i = 0; i < perfiles.size(); i++) {
	                    // se recorre el listado de perfiles
	                    log.trace("Perfil: " + perfiles.get(i).getNombre_perfil());
	                    if (perfil.equalsIgnoreCase(perfiles.get(i).getNombre_perfil())) {
	
	                        // si el perfil es el correcto se procede a autenticar
                            userOK = seguridad.autenticaUsuario(conn, sesion.getUsuario(), sesion.getPassword(),
                                    nombreAplicacion, "0.0.0.0");

                            if (userOK == null) {
                                objRespuesta = new Respuesta();
                                objRespuesta = getMensaje(Conf_Mensajes.MSJ_PASSWORD_INCORRECTO_14, null,
                                        this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            } else {
                                objRespuesta = null;
                                break;
                            }
                        } else {
                            objRespuesta = new Respuesta();
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NOTIENEPERFIL_17, null,
                                    this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        }
                    }

                } else {
                    objRespuesta = new Respuesta();
                    objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NOTIENEPERFIL_17, null, this.getClass().toString(),
                            metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                }

            } else {
                objRespuesta = new Respuesta();
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_NOEXISTE_USER_13, null, this.getClass().toString(), metodo,
                        null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
            }
        } finally {
            DbUtils.closeQuietly(conn);
        }

        return objRespuesta;
    }

    /**
     * MÃ©todo para iniciar sesi\u00F3n de usuario y agregar token de validaciÃ³n
     * @throws StatementException 
     * @throws LDAPException 
     * @throws ExcepcionClaveNoValidaLDAP 
     * @throws ExcepcionConfiguracionLdap 
     * @throws ExcepcionUsuarioBloqueado 
     * @throws ExcepcionClaveExpirada 
     * @throws ExcepcionClaveNoValidaModsec 
     * @throws ExcepcionClaveNoCumplePoliticas 
     */
    public OutputLogin iniciarSesion(InputSesionParametros sesion)  {
        OutputLogin respuestaLogin = new OutputLogin();
        Connection conn1 = null;
        Respuesta objRespuesta = new Respuesta();
        String metodo = "iniciarSesion";
        String token = "";
        objRespuesta = validaDatos(sesion);
        Outputvendedorxdts objV = new Outputvendedorxdts();
        if (objRespuesta == null) {

            try {
                conn1 = getConnLocal();
                objRespuesta = autenticarUsuario(sesion);
                if (objRespuesta == null) {
                    // obtiene datos sidra
                    ValidarVendedor obj = new ValidarVendedor();
                    obj.setCodArea(sesion.getCodArea());
                    log.trace("COD AREA: " + sesion.getCodArea());
                    obj.setIdVendedor(IDVENDEDOR);
                    obj.setUsuario(sesion.getUsuario());
                    obj.setCodDispositivo(sesion.getCodDispositivo());

                    objV = new VendedorXDTS().getValidarVend(obj);

                    log.trace("RESULTADO:" + objV.getRespuesta().getCodResultado());
                    log.trace("RESULTADO:" + objV.getRespuesta().getDescripcion());

                    if (objV.getRespuesta().getCodResultado().equals("12")) {

                        log.trace("SI ENCONTRO VALORES DE DTS PARA VENDEDOR");
                        // si no tiene mensajes de error podemos obtener token
                        // para el inicio de sesi\u00F3n
                        try {
                            token = getToken(conn1, sesion.getUsuario(), "", sesion.getCodArea(),
                                    sesion.getCodDispositivo(), 1);

                            log.trace("TOKEN:" + token);
                        } catch (Exception e) {

                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_TOKEN_INVALIDO_7, e.getMessage(),
                                    this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            respuestaLogin.setRespuesta(objRespuesta);
                            log.error(e,e);
                        }
                        log.trace("token:"+token);
                        if (token.equals("LOGIN")) {
                            log.trace("Usuario debe loguearse");
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_SESION_EXPIRO_12, null,
                                    this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            respuestaLogin.setRespuesta(objRespuesta);
                        } else if ((token.toUpperCase().contains("ERROR"))){
                            objRespuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_INCORRECTO_19, null,
                                    this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                        	String mensaje=token.replace("ERROR", "");
                        	objRespuesta.setExcepcion(mensaje);
                            respuestaLogin.setRespuesta(objRespuesta);
                            
                            log.trace("token con error:"+token);
                        }
                    
                        else if (objRespuesta != null) {
                        	 log.trace("token :"+token);
                        	log.trace("retorna mensaje error login");
                            respuestaLogin.setRespuesta(objRespuesta);
                        } else {
                        	log.trace("Login OK");
                        	 log.trace("token :"+token);
                            respuestaLogin.setFolioManual(getParametro(Conf.FOLIO_MANUAL, sesion.getCodArea()));

                            respuestaLogin.setIdBodegaVendedor(objV.getVendedores().get(0).getIdBodegaVendedor());
                            respuestaLogin.setIdBodegaVirtual(objV.getVendedores().get(0).getIdBodegaVirtual());
                            respuestaLogin.setIdDTS(objV.getVendedores().get(0).getIdDTS());
                            respuestaLogin.setIdVendedor(IDVENDEDOR);
                            respuestaLogin.setNombreDistribuidor(objV.getVendedores().get(0).getNombreDistribuidor());
                            respuestaLogin.setTipo(objV.getVendedores().get(0).getTipo());
                            respuestaLogin.setIdTipo(objV.getVendedores().get(0).getIdTipo());
                            respuestaLogin.setLatitud(objV.getVendedores().get(0).getLatitud());
                            respuestaLogin.setLongitud(objV.getVendedores().get(0).getLongitud());
                            respuestaLogin.setResponsable(objV.getVendedores().get(0).getResponsable());
                            respuestaLogin.setIdResponsable(objV.getVendedores().get(0).getIdResponsable());
                            respuestaLogin.setNombreTipo(objV.getVendedores().get(0).getNombreTipo());
                            respuestaLogin.setNumRecarga(objV.getVendedores().get(0).getNumRecarga());
                            respuestaLogin.setFechaCierre(objV.getVendedores().get(0).getFechaCierre());
                            respuestaLogin.setNumConvenio(objV.getVendedores().get(0).getNumConvenio());
                            respuestaLogin.setPin(objV.getVendedores().get(0).getPin());
                            respuestaLogin.setVendedorAsignado(objV.getVendedores().get(0).getVendedorAsignado());
                            respuestaLogin.setIdDispositivo(objV.getVendedores().get(0).getIdDispositivo());
                            respuestaLogin.setNivelBuzon(objV.getVendedores().get(0).getNivelBuzon());
                            respuestaLogin.setNumTelefono(objV.getVendedores().get(0).getNumTelefono());
                            respuestaLogin.setNumIdentificacion(objV.getVendedores().get(0).getNumIdentificacion());
                            respuestaLogin.setTipoIdentificacion(objV.getVendedores().get(0).getTipoIdentificacion());
                            respuestaLogin.setUserToken(objV.getVendedores().get(0).getUserToken());
                            respuestaLogin.setDevelopToken(objV.getVendedores().get(0).getDevelopToken());
                            log.trace("tase de cambio:"+objV.getVendedores().get(0).getTasaCambio());
                            if(objV.getVendedores().get(0).getTasaCambio()==null || objV.getVendedores().get(0).getTasaCambio().equals("")){
                            	  respuestaLogin.setTasaCambio("");
                            }else{
                            	 respuestaLogin.setTasaCambio(objV.getVendedores().get(0).getTasaCambio());
                            }
                          
                            respuestaLogin.setToken(token);
                            objRespuesta = getMensaje(Conf_Mensajes.OK_Login3, null, null, null, null, null);
                            respuestaLogin.setRespuesta(objRespuesta);
                        }

                    } else {
                        log.trace("NO ENCONTRO VALORES DE DTS PARA VENDEDOR");
                        if (objV.getRespuesta().getCodResultado().equals("-42")) {
                            objRespuesta = getMensaje(Conf_Mensajes.OK_LoginNotieneDTS4, null,
                                    this.getClass().toString(), metodo, null, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                            respuestaLogin.setRespuesta(objRespuesta);
                        } else {
                            objRespuesta = objV.getRespuesta();
                            respuestaLogin.setRespuesta(objRespuesta);
                        }
                    }

                } else {
                    respuestaLogin.setRespuesta(objRespuesta);
                }

            } catch (SQLException e) {
                log.error(e,e);
                objRespuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
                log.error(e,e);
            } catch (InvalidKeyException e) {
                log.trace("1");
                log.error(e,e);
                String mensaje = e.getMessage().substring((e.getMessage().indexOf(":") + 1), e.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
            } catch (NoSuchAlgorithmException e) {
                log.trace("2");
                log.error(e,e);
                String mensaje = e.getMessage().substring((e.getMessage().indexOf(":") + 1), e.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
            } catch (NoSuchPaddingException e) {
                log.trace("3");
                log.error(e,e);
                String mensaje = e.getMessage().substring((e.getMessage().indexOf(":") + 1), e.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
            } catch (IllegalBlockSizeException e) {
                log.trace("4");
                log.error(e,e);
                String mensaje = e.getMessage().substring((e.getMessage().indexOf(":") + 1), e.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
            } catch (BadPaddingException e) {
                log.trace("5");
                log.error(e,e);
                String mensaje = e.getMessage().substring((e.getMessage().indexOf(":") + 1), e.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
            } catch (NamingException e) {
                log.trace("6");
                log.error(e,e);
                String mensaje = e.getMessage().substring((e.getMessage().indexOf(":") + 1), e.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
            } catch (ExcepcionSeguridad e1) {
                log.trace("7");
                e1.printStackTrace();
                String mensaje = e1.getMessage().substring((e1.getMessage().indexOf(":") + 1),
                        e1.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e1.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
            } catch (ExcepcionUsuarioNoExistente e1) {
                log.trace("8");
                e1.printStackTrace();
                String mensaje = e1.getMessage().substring((e1.getMessage().indexOf(":") + 1),
                        e1.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e1.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
            } catch (InterruptedException e1) {
                log.trace("9");
                String mensaje = e1.getMessage().substring((e1.getMessage().indexOf(":") + 1),
                        e1.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e1.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
               log.error(e1,e1);
               log.warn(e1.getMessage(), e1);
            } catch (ExcepcionClaveNoCumplePoliticas e1) {
                log.trace("10");
                String mensaje = e1.getMessage().substring((e1.getMessage().indexOf(":") + 1),
                        e1.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e1.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
                log.error(e1,e1);
            } catch (ExcepcionClaveNoValidaModsec e1) {
                log.trace("11");
                String mensaje = e1.getMessage().substring((e1.getMessage().indexOf(":") + 1),
                        e1.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e1.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
                log.error(e1,e1);

            } catch (ExcepcionClaveExpirada e1) {
                log.trace("12");
                String mensaje = e1.getMessage().substring((e1.getMessage().indexOf(":") + 1),
                        e1.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e1.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
                log.error(e1,e1);
            } catch (ExcepcionUsuarioBloqueado e1) {
                log.trace("13");
                String mensaje = e1.getMessage().substring((e1.getMessage().indexOf(":") + 1),
                        e1.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e1.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
                log.error(e1,e1);
            } catch (ExcepcionConfiguracionLdap e1) {
                log.trace("14");
                String mensaje = e1.getMessage().substring((e1.getMessage().indexOf(":") + 1),
                        e1.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e1.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
                log.error(e1,e1);
            } catch (ExcepcionClaveNoValidaLDAP e1) {
                log.trace("15");
                String mensaje = e1.getMessage().substring((e1.getMessage().indexOf(":") + 1),
                        e1.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e1.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
                log.error(e1,e1);
            } catch (LDAPException e1) {
                log.trace("16");
                String mensaje = e1.getMessage().substring((e1.getMessage().indexOf(":") + 1),
                        e1.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e1.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
                log.error(e1,e1);
            } catch (StatementException e1) {
                log.trace("17");
                String mensaje = e1.getMessage().substring((e1.getMessage().indexOf(":") + 1),
                        e1.getMessage().length());
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e1.getMessage(),
                        this.getClass().toString(), metodo, mensaje, Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
                e1.printStackTrace();
            } catch (Exception e1) {
                log.trace("18");
        
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_ERROR_INICIASESION_16, e1.getMessage(),
                        this.getClass().toString(), metodo, "", Conf_Mensajes.ORIGEN_SERVICIOS_OP);
                respuestaLogin.setRespuesta(objRespuesta);
            } finally {
                DbUtils.closeQuietly(conn1);
            }
        } else {
            log.trace("Advertencia:" + objRespuesta.getCodResultado());
            log.trace("Descripcion:" + objRespuesta.getDescripcion());
            respuestaLogin.setRespuesta(objRespuesta);
        }
        return respuestaLogin;
    }

    /***
     * M\u00E9todo para solicitar reinicio de clave de usuario vendedor
     * @param objDatos
     * @return
     */
    public Respuesta reiniciarClave (InputSesionParametros objDatos){
    	Respuesta objRespuesta = new Respuesta();
    	SeguridadWeb seguridad = new SeguridadWeb();
    	Connection conn=null;
    	boolean reinicio=false;
    	
    	try {
			conn=getDtsSeg().getConnection();
			reinicio=seguridad.reinicioClave(conn, objDatos.getUsuario());
			
		
			if(reinicio){
				log.trace("reinicio de clave");
			}else{
				log.trace("no reinicio de clave");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e,e);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			log.error(e,e);
		} catch (ExcepcionSeguridad e) {
			// TODO Auto-generated catch block
			log.error(e,e);
		} catch (ExcepcionUsuarioNoExistente e) {
			// TODO Auto-generated catch block
			log.error(e,e);
		} catch (ExcepcionUsuarioBloqueado e) {
			// TODO Auto-generated catch block
			log.error(e,e);
		} catch (StatementException e) {
			// TODO Auto-generated catch block
			log.error(e,e);
		}finally{
			DbUtils.closeQuietly(conn);
		}
    	
    	
    	return objRespuesta;
    }
}

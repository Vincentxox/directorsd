package com.consystec.sc.ca.ws.service;



import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.log4j.xml.DOMConfigurator;

import com.consystec.sc.ca.ws.input.general.InputCatalogoVer;
import com.consystec.sc.ca.ws.input.general.InputConsultaWeb;
import com.consystec.sc.ca.ws.metodos.CatalogoVer;
import com.consystec.sc.ca.ws.metodos.EtiquetaMov;
import com.consystec.sc.ca.ws.metodos.Pais;
import com.consystec.sc.ca.ws.metodos.Servidor;
import com.consystec.sc.ca.ws.output.CatalogoVer.OutputCatalogoVer;
import com.consystec.sc.ca.ws.output.etiquetamov.OutputEtiquetaMov;
import com.consystec.sc.ca.ws.output.generico.OutputServidor;
import com.consystec.sc.ca.ws.output.pais.OutputConsultaPais;
import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.ca.ws.util.MediaType;

/**
 * @author sbarrios Consystec 2015
 */

@Path("/actualizacionsidra")
public class ServicioActualizacion extends ServicioBase {

	public static void main(String[] args)  {
        DOMConfigurator.configure(ServicioActualizacion.class.getClassLoader().getResource("log4j.xml"));
    }

    @Path("/getetiqueta/")
	 /**
	  *@api {POST} /actualizacionsidra/getetiqueta/ [getEtiqueta]
	  *
	  * @apiName getEtiqueta
	  * @apiDescription Servicio para obtener etiquetas de pantallas de aplicaci\u00F3n m\u00F3vil
	  * @apiGroup Servicios_Actualizacion
	  * @apiVersion 1.0.0
	  * @apiParam (getEtiqueta) {String}   codArea C\u00F3digo de  \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n  de bodegas
	  * @apiParam (getEtiqueta) {String}   usuario nombre de usuario que solicita la operacion
	  * @apiParamExample {json} Ejemplo parametros de Entrada:
	   *{
	   *    "codArea": "503",
	   *    "usuario": "usuario.pruebas"
	   *}
	   *
	  * @apiSuccessExample {} JSON Exito-respuesta:
		* {
	    * "respuesta": {
	    *    "codResultado": "2",
	    *    "mostrar": "1",
	    *    "descripcion": "OK. Actualizaci\u00F3n.",
	    *    "origen": "Servicios Operaciones",
	    *    "clase": "com.consystec.sc.ca.ws.metodos.EtiquetaMov",
	    *    "metodo": "obtenerEtiquetasBD",
	    *    "excepcion": " ",
	    *    "tipoExepcion": ""
	    * },
	    * "pantallas": [
	    *    {
	    *        "paisId": "503",
	    *        "id": "1",
	    *        "nombre": "Login",
	    *        "etiquetas": [
	    *            {
	    *                "nombreId": "1",
	    *                "nombre": "usuario",
	    *                "valorId": "1",
	    *                "valor": "Usuario:",
	    *                "orden": "1",
	    *                "mostrar": "1",
        *            	"obligatorio": "0"
	    *            },
	    *            {
	    *                "nombreId": "2",
	    *                "nombre": "contrasena",
	    *                "valorId": "2",
	    *                "valor": "Contrasenia:",
	    *                "orden": "1",
	    *                 "mostrar": "1",
        *            	"obligatorio": "0"
	    *            },
	    *            {
	    *                "nombreId": "3",
	    *                "nombre": "recordar",
	    *                "valorId": "3",
	    *                "valor": "Recordar contrasenia",
	    *               "orden": "1",
	    *                "mostrar": "1",
        *            	"obligatorio": "0"
	    *            },
	    *            {
	    *                "nombreId": "4",
	    *                "nombre": "ingresar",
	    *                "valorId": "4",
	    *                "valor": "Ingresar",
	    *                "orden": "1",
	    *                 "mostrar": "1",
        *            	"obligatorio": "0"
	    *            }
	    *       ]
	    *    },
	  * @apiErrorExample {} JSON Respuesta-Error:	  
	* {
	*    "respuesta": {
	*        "codResultado": "-101",
	*        "mostrar": "0",
	*        "descripcion": "Ocurrio un Problema inesperado, contacte a su Administrador.",
	*        "clase": "com.consystec.sc.ca.ws.metodos.Bodegas",
	*        "metodo": "getMensaje",
	*        "excepcion": "ORA-00942: table or view does not exist\n",
	*        "tipoExepcion": "Excepcion SQL"
	*    }
	*}
	  * @apiPermission admin
	  * 
	  */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public OutputEtiquetaMov getEtiquetas(@Context HttpServletRequest req, InputConsultaWeb input) {
    	Date beginTime = new Date();
        ControladorBase.setRequestGlobal(req);
        OutputEtiquetaMov response = new EtiquetaMov().getEtiquetas(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/getcatalogo/")
	 /**
	  *@api {POST} /actualizacionsidra/getcatalogo/ [getCatalogo]
	  *
	  * @apiName getCatalogo
	  * @apiDescription Servicio para obtener versiones de catalogos disponibles de la aplicaci\u00F3n
	  * @apiGroup Servicios_Actualizacion
	  * @apiVersion 1.0.0
	  * @apiParam (getCatalogo) {String}   codArea C\u00F3digo de  \u00E9rea del pa\u00EDs del que se desea obtener informaci\u00F3n  de bodegas
	 
	  * @apiParam (getCatalogo) {String}   fecha fecha de la versi\u00F3n que se desea consultar formato:yyyymmddHH24MiSS
	   * @apiParamExample {json} Ejemplo parametros de Entrada:
	   *{
	   *	"codArea": "502",
	   * 	"fecha":"20151103155643"
	   *}
	   *
	  * @apiSuccessExample {} JSON Exito-respuesta:
	* {
	*    "respuesta": {
	*        "codResultado": "2",
	*        "mostrar": "1",
	*        "descripcion": "OK. Actualizaci\u00F3n.",
	*        "clase": " ",
	*        "metodo": " ",
	*        "excepcion": " ",
	*        "tipoExepcion": ""
	*    },
	*    "fecha": "20151103172722",
	*    "catalogos": [
	*        {
	*            "nombre": "etiquetas",
	*            "url": "sd",
	*            "codPais": "503"
	*        },
	*        {
	*            "nombre": "paises",
	*            "url": "ss",
	*            "codPais": "-1"
	*        }
	*   ]
	* }
	  *@apiErrorExample {} JSON Respuesta-Error:	  
	* {
	*    "respuesta": {
	*        "codResultado": "-101",
	*        "mostrar": "0",
	*        "descripcion": "Ocurrio un Problema inesperado, contacte a su Administrador.",
	*        "clase": "com.consystec.sc.ca.ws.metodos.Bodegas",
	*        "metodo": "getMensaje",
	*        "excepcion": "ORA-00942: table or view does not exist\n",
	*        "tipoExepcion": "Excepcion SQL"
	*    }
	*}
	  * @apiPermission admin
	  * 
	  */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public OutputCatalogoVer getCatalogoVer(@Context HttpServletRequest req, InputCatalogoVer input) {
    	Date beginTime = new Date();
    	ControladorBase.setRequestGlobal(req);
        OutputCatalogoVer response = new CatalogoVer().getCatalogo(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, "usuario.actualizacion", req, input, response, beginTime);

        return response;
    }

    @Path("/getdatospais")
    /**
     * @api {POST} /actualizacionsidra/getdatospais/ [getdatospais]
     * 
     * @apiName getdatospais
     * @apiDescription Servicio para obtener los datos del pa\u00EDs.
     * @apiGroup Servicios_Actualizacion
     * @apiVersion 1.0.0
     * @apiParam (getdatospais) {String} codArea C\u00F3digo de \u00E9rea del pais.
     * @apiParam (getdatospais) {String} usuario Nombre de usuario que solicita la operacion.
     * 
     * @apiParamExample {json} Ejemplo parametros de Entrada:
     * {
     *     "codArea":"503",
     *     "usuario": "usuario.pruebas"
     * }
     * 
     * @apiSuccessExample {} JSON Exito-respuesta:
     * {
     *   "respuesta": {
     *     "codResultado": "0",
     *     "mostrar": "0",
     *     "descripcion": "Recursos recuperados exitosamente."
     *   },
     *   "codArea": "503",
     *   "nombrePais": "EL SALVADOR",
     *   "longMaxNumero": "8",
     *   "longMaxIdentificacion": "25",
     *   "departamentos": [
     *     {
     *       "nombreDepartamento": "AHUACHAP\u00E9N",
     *       "municipios": [
     *         {
     *           "nombreMunicipio": "AHUACHAP\u00E9N"
     *         },
     *         {
     *           "nombreMunicipio": "APANECA"
     *         }
     *     },
     *     {
     *       "nombreDepartamento": "CABAniAS",
     *       "municipios": [
     *         {
     *           "nombreMunicipio": "SENSUNTEPEQUE"
     *         },
     *     }
     *   ]
     * }
     * 
     * @apiErrorExample {} JSON Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-205",
     *        "mostrar": "1",
     *        "descripcion": "No existen registros con esos par\u00E9metros.",
     *        "clase": "com.consystec.sc.sv.ws.operaciones.OperacionConsultaPais",
     *        "metodo": "doGet",
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
    public OutputConsultaPais doGetDatosPais(@Context HttpServletRequest req, InputConsultaWeb input) {
    	Date beginTime = new Date();
    	ControladorBase.setRequestGlobal(req);
        OutputConsultaPais response = new Pais().getInfoPais(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }

    @Path("/getservidor")
    /**
     * @api {POST} /actualizacionsidra/getservidor/ [getservidor]
     * 
     * @apiName getservidor
     * @apiDescription Servicio para obtener las url's utilizadas para consumir los diferentes servicios web.
     * @apiGroup Servicios_Actualizacion
     * @apiVersion 1.0.0
     * @apiParam (getservidor) {String} codArea C\u00F3digo de \u00E9rea del pais.
     * @apiParam (getservidor) {String} usuario Nombre de usuario que solicita la operacion.
     * 
     * @apiParamExample {json} Ejemplo parametros de Entrada:
     * {
     *     "codArea":"503",
     *     "usuario": "usuario.pruebas"
     * }
     * 
     * @apiSuccessExample {} JSON Exito-respuesta:
     * {
     *   "respuesta": {
     *     "codResultado": "0",
     *     "mostrar": "0",
     *     "descripcion": "Recursos recuperados exitosamente."
     *   },
     *   "codArea": "503",
     *   "nombrePais": "EL SALVADOR",
     *   "longMaxNumero": "8",
     *   "longMaxIdentificacion": "25",
     *   "departamentos": [
     *     {
     *       "nombreDepartamento": "AHUACHAP\u00E9N",
     *       "municipios": [
     *         {
     *           "nombreMunicipio": "AHUACHAP\u00E9N"
     *         },
     *         {
     *           "nombreMunicipio": "APANECA"
     *         }
     *     },
     *     {
     *       "nombreDepartamento": "CABAniAS",
     *       "municipios": [
     *         {
     *           "nombreMunicipio": "SENSUNTEPEQUE"
     *         },
     *     }
     *   ]
     * }
     * 
     * @apiErrorExample {} JSON Respuesta-Error:     
     * {
     *    "respuesta": {
     *        "codResultado": "-205",
     *        "mostrar": "1",
     *        "descripcion": "No existen registros con esos par\u00E9metros.",
     *        "clase": "com.consystec.sc.sv.ws.operaciones.OperacionConsultaPais",
     *        "metodo": "doGet",
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
    public OutputServidor getServidor(@Context HttpServletRequest req, InputConsultaWeb input) {
    	Date beginTime = new Date();
    	ControladorBase.setRequestGlobal(req);
        OutputServidor response = new Servidor().getServidor(input);
        ControladorBase.logResponseTime(input.getCodArea(), Thread.currentThread().getStackTrace()[1].getMethodName(), null, input.getUsuario(), req, input, response, beginTime);

        return response;
    }
}

package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;



import com.consystec.sc.ca.ws.input.general.InputConsultaWeb;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.pais.InputConsultaAldeas;
import com.consystec.sc.ca.ws.output.pais.InputConsultaDepartamentos;
import com.consystec.sc.ca.ws.output.pais.InputConsultaMunicipios;
import com.consystec.sc.ca.ws.output.pais.OutputConsultaPais;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.Order;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class OperacionConsultaPais {
	private OperacionConsultaPais(){}
    private static final Logger log = Logger.getLogger(OperacionConsultaPais.class);

    /**
     * Funci\u00F3n que arma el query a utilizar al consultar en m\u00E9todo GET.
     * 
     * @param conn
     * @param input
     * @param metodo
     * @return OutputCatalogo
     * @throws SQLException
     */
    public static OutputConsultaPais doGet1(Connection conn, 
    		InputConsultaWeb input, BigDecimal idPais) {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        List<InputConsultaAldeas> listAldeas ;
        List<InputConsultaMunicipios> listMunicipios;
        List<InputConsultaDepartamentos> listDepartamentos ;
        
        String departamento = "";
        String municipio = "";
        String region = "";
        
        Respuesta respuesta = null;
        OutputConsultaPais output = null;
        List<Order> orden = new ArrayList<Order>();
        orden.add(new Order(Catalogo.CAMPO_GRUPO, Order.ASC));
        
        String campos[] = {
            Catalogo.CAMPO_GRUPO,
            Catalogo.CAMPO_NOMBRE
        };
        
        //Se obtienen todos los departamentos
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_AND, Catalogo.CAMPO_GRUPO, Conf.PAIS_REGION_GRUPO));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,idPais.toString()));
        
        List<Map<String, String>> datosDepto = new ArrayList<Map<String, String>>();
        try {
            datosDepto = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, orden);
        } catch (SQLException e) {
            log.error("Error al obtener departamentos. " + e);
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DATOS_DEPARTAMENTOS, null,
                nombreClase, nombreMetodo, e.getMessage(), input.getCodArea());
                
            output = new OutputConsultaPais();
            output.setRespuesta(r);
            return output;
        }
        
        //Se obtienen todos los municipios.
        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_AND, Catalogo.CAMPO_GRUPO, Conf.PAIS_DEPTO_GRUPO));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,idPais.toString()));
        
        List<Map<String, String>> datosMpio = new ArrayList<Map<String, String>>();
        try {
            datosMpio = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, orden);
        } catch (SQLException e) {
            log.error("Error al obtener municipios. " + e);
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DATOS_MUNICIPIOS, null,
                nombreClase, nombreMetodo, e.getMessage(), input.getCodArea());
                
            output = new OutputConsultaPais();
            output.setRespuesta(r);
            return output;
        }
        
        //Se obtienen todas las aldeas.
        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_AND, Catalogo.CAMPO_GRUPO, Conf.PAIS_MPIO_GRUPO));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,idPais.toString()));
        
        List<Map<String, String>> datosAldea = new ArrayList<Map<String, String>>();
        try {
            datosAldea = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, orden);
        } catch (SQLException e) {
            log.error("Error al obtener aldeas. " + e);
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DATOS_ALDEAS, null,
                nombreClase, nombreMetodo, e.getMessage(), input.getCodArea());
                
            output = new OutputConsultaPais();
            output.setRespuesta(r);
            return output;
        }
        
        //OBTENER REGIONES EL SALVADOR
        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_AND, Catalogo.CAMPO_GRUPO, Conf.PAIS_REGION_GRUPO));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,idPais.toString()));
       
        try {
        
        	UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, orden);
        } catch (SQLException e) {
            log.error("Error al obtener regiones. " + e);
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DATOS_ALDEAS, null,
                nombreClase, nombreMetodo, e.getMessage(), input.getCodArea());
                
            output = new OutputConsultaPais();
            output.setRespuesta(r);
            return output;
        }
        
        //Se imprime respuesta.
        respuesta=new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
            nombreClase, nombreMetodo, null, input.getCodArea());

        //Se crea output.
        output = new OutputConsultaPais();
        output.setRespuesta(respuesta);
        output.setCodArea(input.getCodArea());
        
			listDepartamentos = new ArrayList<InputConsultaDepartamentos>();
			for (int i = 0; i < datosDepto.size(); i++) {
	            departamento = Conf.PAIS_DEPTO_GRUPO + datosDepto.get(i).get(Catalogo.CAMPO_NOMBRE);
	            InputConsultaDepartamentos depto = new InputConsultaDepartamentos();
	            
	            listMunicipios = new ArrayList<InputConsultaMunicipios>();
	            for (int j = 0; j < datosMpio.size(); j++) {
	                municipio = Conf.PAIS_MPIO_GRUPO + datosMpio.get(j).get(Catalogo.CAMPO_NOMBRE);
	                InputConsultaMunicipios mpio = new InputConsultaMunicipios();
	                
	                listAldeas = new ArrayList<InputConsultaAldeas>();
	                for (int k = 0; k < datosAldea.size(); k++) {
	                    InputConsultaAldeas aldea = new InputConsultaAldeas();
	                    if (datosAldea.get(k).get(Catalogo.CAMPO_GRUPO).equals(municipio)){
	                        aldea.setNombreAldea(datosAldea.get(k).get(Catalogo.CAMPO_NOMBRE));
	                        listAldeas.add(aldea);
	                    }
	                }
	                mpio.setNombreMunicipio(datosMpio.get(j).get(Catalogo.CAMPO_NOMBRE));
	                mpio.setAldeas(listAldeas);

	                if (datosMpio.get(j).get(Catalogo.CAMPO_GRUPO).equals(departamento)){
	                    listMunicipios.add(mpio);
	                }
	            }
	            depto.setNombreDepartamento(datosDepto.get(i).get(Catalogo.CAMPO_NOMBRE));
	            depto.setMunicipios(listMunicipios);
	            
	            System.out.println("DEPARTAMENTO : " + datosDepto.get(i).get(Catalogo.CAMPO_GRUPO) + " REGION " + region);
	            
	            if(datosDepto.get(i).get(Catalogo.CAMPO_GRUPO).equals(region)){
	            	listDepartamentos.add(depto);
	            }
	        }
        return output;
    }
    
    
    
    public static OutputConsultaPais doGet(Connection conn, 
    		InputConsultaWeb input, BigDecimal idPais) {
        String nombreMetodo = "doGet";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        List<InputConsultaAldeas> listAldeas ;
        List<InputConsultaMunicipios> listMunicipios;
        List<InputConsultaDepartamentos> listDepartamentos = new ArrayList<InputConsultaDepartamentos>();
        String departamento = "";
        String municipio = "";
        
        Respuesta respuesta = null;
        OutputConsultaPais output = null;
        List<Order> orden = new ArrayList<Order>();
        orden.add(new Order(Catalogo.CAMPO_GRUPO, Order.ASC));
        
        String campos[] = {
            Catalogo.CAMPO_GRUPO,
            Catalogo.CAMPO_NOMBRE
        };
        
        //Se obtienen todos los departamentos
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_PR, Catalogo.CAMPO_GRUPO, Conf.PAIS_GRUPO));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,idPais.toString()));
        
        List<Map<String, String>> datosDepto = new ArrayList<Map<String, String>>();
        try {
            datosDepto = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, orden);
        } catch (SQLException e) {
            log.error("Error al obtener departamentos. " + e);
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DATOS_DEPARTAMENTOS, null,
                nombreClase, nombreMetodo, e.getMessage(), input.getCodArea());
                
            output = new OutputConsultaPais();
            output.setRespuesta(r);
            return output;
        }
        
        //Se obtienen todos los municipios.
        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_PR, Catalogo.CAMPO_GRUPO, Conf.PAIS_DEPTO_GRUPO));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,idPais.toString()));
        
        List<Map<String, String>> datosMpio = new ArrayList<Map<String, String>>();
        try {
            datosMpio = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, orden);
        } catch (SQLException e) {
            log.error("Error al obtener municipios. " + e);
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DATOS_MUNICIPIOS, null,
                nombreClase, nombreMetodo, e.getMessage(), input.getCodArea());
                
            output = new OutputConsultaPais();
            output.setRespuesta(r);
            return output;
        }
        
        //Se obtienen todas las aldeas.
        condiciones.clear();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_LIKE_PR, Catalogo.CAMPO_GRUPO, Conf.PAIS_MPIO_GRUPO));
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,idPais.toString()));
        
        List<Map<String, String>> datosAldea = new ArrayList<Map<String, String>>();
        try {
            datosAldea = UtileriasBD.getSingleData(conn, Catalogo.N_TABLA, campos, condiciones, orden);
        } catch (SQLException e) {
            log.error("Error al obtener aldeas. " + e);
            Respuesta r = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DATOS_ALDEAS, null,
                nombreClase, nombreMetodo, e.getMessage(), input.getCodArea());
                
            output = new OutputConsultaPais();
            output.setRespuesta(r);
            return output;
        }
        
        //Se imprime respuesta.
        respuesta=new ControladorBase().getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null,
            nombreClase, nombreMetodo, null, input.getCodArea());
        respuesta.setCodResultado("0");
        //Se crea output.
        output = new OutputConsultaPais();
        output.setRespuesta(respuesta);
        output.setCodArea(input.getCodArea());
        
        //Se ordenan y agrupan los datos respectivamente.
        for (int i = 0; i < datosDepto.size(); i++) {
            departamento = Conf.PAIS_DEPTO_GRUPO + datosDepto.get(i).get(Catalogo.CAMPO_NOMBRE);
            InputConsultaDepartamentos depto = new InputConsultaDepartamentos();
            
            listMunicipios = new ArrayList<InputConsultaMunicipios>();
            for (int j = 0; j < datosMpio.size(); j++) {
                municipio = Conf.PAIS_MPIO_GRUPO + datosMpio.get(j).get(Catalogo.CAMPO_NOMBRE);
                InputConsultaMunicipios mpio = new InputConsultaMunicipios();
                
                listAldeas = new ArrayList<InputConsultaAldeas>();
                for (int k = 0; k < datosAldea.size(); k++) {
                    InputConsultaAldeas aldea = new InputConsultaAldeas();
                    if (datosAldea.get(k).get(Catalogo.CAMPO_GRUPO).equals(municipio)){
                        aldea.setNombreAldea(datosAldea.get(k).get(Catalogo.CAMPO_NOMBRE));
                        listAldeas.add(aldea);
                    }
                }
                mpio.setNombreMunicipio(datosMpio.get(j).get(Catalogo.CAMPO_NOMBRE));
                mpio.setAldeas(listAldeas);

                if (datosMpio.get(j).get(Catalogo.CAMPO_GRUPO).equals(departamento)){
                    listMunicipios.add(mpio);
                }
            }
            depto.setNombreDepartamento(datosDepto.get(i).get(Catalogo.CAMPO_NOMBRE));
            depto.setMunicipios(listMunicipios);
            
            listDepartamentos.add(depto);
        }
        output.setDepartamentos(listDepartamentos);

        return output;
    }

}

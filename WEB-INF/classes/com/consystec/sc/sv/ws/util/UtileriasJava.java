package com.consystec.sc.sv.ws.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.consystec.sc.ca.ws.input.log.InputLogSidra;
import com.consystec.sc.ca.ws.input.log.LogSidra;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.sv.ws.metodos.CtrlLogSidra;
import com.consystec.sc.sv.ws.orm.Catalogo;
import com.consystec.sc.sv.ws.orm.Distribuidor;
import com.consystec.sc.sv.ws.orm.VendedorPDV;
import com.google.gson.Gson;

/**
 * @author Victor Cifuentes @ Consystec - 2015
 *
 */
public class UtileriasJava {
	private UtileriasJava(){}
    private static final Logger log = Logger.getLogger(UtileriasJava.class);
    
    /**
     * Funci\u00F3n que devuelve el valor de una configuraci\u00F3n almacenada en la base de datos,
     *  utilizando la tabla indicada.
     * 
     * @param conn Conexi\u00F3n a BD. Este par\u00E9metro puede ser nulo y se utilizar\u00E9 una conexi\u00F3n nueva.
     * @param grupo Nombre del grupo de configuraci\u00F3n.
     * @param config Nombre de la configuraci\u00F3n deseada.
     * @return String Valor encontrado, cadena vac\u00EDa en caso de encontrar error.
     * @throws Exception 
     * @throws SQLException
     */
    public static String getConfig(Connection conn, String grupo, String config, String codArea) throws SQLException {
        boolean conexionNueva = false;
        String valorConfig = "";

        try {
            if (conn == null) {
            	log.trace("inicia nueva conexion en getconfig");
                conn = new ControladorBase().getConnRegional();
                conexionNueva = true;
            } else {
                conexionNueva = false;
            }

            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, " (SELECT TCSCCATPAISID FROM TC_SC_CAT_PAIS WHERE AREA = '"+codArea+"')"));
            condiciones.add(setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, grupo));
            condiciones.add(setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE, config));
            condiciones.add(setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, "ALTA"));

            valorConfig = UtileriasBD.getOneRecord(conn, Catalogo.CAMPO_VALOR, Catalogo.N_TABLA, condiciones);

            if (!valorConfig.equals("")) {
                return valorConfig;
            } else {
                throw new SQLException("Error al obtener el par\u00E9metro de configuraci\u00F3n, GRUPO: " + grupo + ", NOMBRE: " + config);
            }
        } finally {
            if (conexionNueva == true && conn != null) {
                    DbUtils.closeQuietly(conn);
            }
        }
    }
    
    /**
     * Funci\u00F3n que realiza un count a la tabla de configuraciones con el grupo y valor indicados para verificar su existencia.
     * 
     * @param conn
     * @param grupo
     * @param config
     * @param estadoAlta
     * @return
     */
    public static int getCountConfigValor(Connection conn, String grupo, String config, String estadoAlta, String codArea) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID, " (SELECT TCSCCATPAISID FROM TC_SC_CAT_PAIS WHERE AREA ='"+codArea+"')"));
        condiciones.add(setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_ESTADO, estadoAlta));
        condiciones.add(setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, grupo));
        condiciones.add(setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_VALOR, config));

        try {
            return UtileriasBD.selectCount(conn, Catalogo.N_TABLA, condiciones);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }
    
    /**
     * Func\u00EDon que convierte un listado de elementos en un string de elementos separados por el caracter indicado.
     * 
     * @param tipo Indica el tipo de valor a utilizar para armar la cadena dependiendo el si se necesitan valores
     *  num\u00E9ricos o de texto. 
     * @param listado Listado de strings a convertir.
     * @param separador Caracter a utilizar para separar cada string.
     * @return cadena Objeto de tipo String con el texto en formato adecuado para utilizar en consultas de tipo IN.
     */
    public static String listToString(int tipo, List<String> listado, String separador) {
        String cadena = "";
        for (int i = 0; i < listado.size(); i++) {
            if (tipo == Conf.TIPO_TEXTO) {
                cadena += "'" + listado.get(i) + "'";
            } else {
                cadena += listado.get(i);
            }
            
            if (i != listado.size() - 1)
                cadena += separador;
        }
        return cadena;
    }
    
    /**
     * Funci\u00F3n que convierte una cadena de fecha en un objeto de tipo Date con el formato especificado.
     * 
     * @param stringDate Dadena de texto con la fecha a convertir.  
     * @param formato Objeto de tipo SimpleDateFormat conteniendo el formato para convertir la cadena a Date.
     * @return date Objeto de tipo Date conteniendo la fecha, NULL en caso de error.
     */
    public static Date parseDate(String stringDate, SimpleDateFormat formato) {
        String nombreMetodo = "parseDate";
        String nombreClase = new CurrentClassGetter().getClassName();
        
        Date date = null;
        try {
            date = formato.parse(stringDate);
        } catch (ParseException e) {
            log.error("Problema al convertir la fecha en el m\u00E9todo " + nombreMetodo + " de la clase " + nombreClase, e);
        }
        return date;
    }
    
    /**
     * Funci\u00F3n que cambia el formato de una fecha. Los formatos son especificados
     *  en la configuracion de la aplicaci\u00F3n. 
     * 
     * @param stringDate Cadena de texto que contiene la fecha a convertir.
     * @return stringDate Fecha con el formato nuevo.
     */
    public static String formatStringDate(String stringDate) {
    	   SimpleDateFormat FORMATO_FECHAHORA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHAHORA);
    	   SimpleDateFormat FECHAHORA2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatStringDate(stringDate, FECHAHORA2, FORMATO_FECHAHORA);
    }
    
    /**
     * Funci\u00F3n que cambia el formato de una fecha en base a los formatos indicados.
     * 
     * @param stringDate Cadena de texto que contiene la fecha a convertir.
     * @return stringDate Fecha con el formato nuevo.
     */
    public static String formatStringDate(String stringDate, SimpleDateFormat formatoEntrada,
            SimpleDateFormat formatoSalida) {
        if (stringDate != null) {
            Date date = parseDate(stringDate, formatoEntrada);
            if (date != null) {
                stringDate = formatoSalida.format(date);
            }
        } else {
            return "";
        }
        
        return stringDate;
    }
    
    /**
     * Funci\u00F3n que arma un string con el texto a insertar o actualizar en base de datos.
     * 
     * @param tipo Tipo de insert o update que se desea obtener.
     * @param valor Cadena con el valor a insertar o actualizar.
     * @return insert Cadena con el formato indicado para la cadena de inserci\u00F3n o modificaci\u00F3n.
     */
    private static String setInsertUpdateText(int tipo, String valor, String formato) {
        String insert = "";
        
        switch (tipo) {
            case Conf.INSERT_SECUENCIA:
                insert = "(extractvalue(dbms_xmlgen.getxmltype('select " + valor + " from dual'),'//text()'))";
            break;
            case Conf.INSERT_NUMERO:
                insert = valor== null || "".equals(valor) ? "NULL" : valor ;
            break;
            case Conf.INSERT_TEXTO:
            	if(valor==null || "".equals(valor)){
            		insert= "NULL";
            	}else{
            			
            	int caracter=0;
            	caracter=valor.indexOf("'");
            	if(caracter==-1){
            		insert= "'" + valor + "'";
            	}else{
            		String primeraParte=valor.substring(0, caracter);
            		log.trace("primeraParte String:"+ primeraParte);
            		String segundaParte=valor.substring(caracter, valor.length());
            		log.trace("segundaParte String:"+ segundaParte);
            	
            		insert="'"+primeraParte+"'"+segundaParte+"'";

            		log.trace("texto con comilla simple:"+insert);
            	}
            	
            }
            break;
            case Conf.INSERT_TEXTO_UPPER:
            	if(valor==null || "".equals(valor)){
            		insert= "NULL";
            	}else{
            			
            	int caracter=0;
            	caracter=valor.indexOf("'");
            	if(caracter==-1){
            		insert= "'" + valor + "'";
            	}else{
            		String primeraParte=valor.substring(0, caracter);
            		log.trace("primeraParte String:"+ primeraParte);
            		String segundaParte=valor.substring(caracter, valor.length());
            		log.trace("segundaParte String:"+ segundaParte);
            	
            		insert="upper('"+primeraParte+"''"+segundaParte+"')";

            		log.trace("texto con comilla simple:"+insert);
            	}
            	
            }
               
            break;
            case Conf.INSERT_FECHA:
                insert = "TO_DATE('" + valor + "', '" + formato + "')";
            break;
            case Conf.INSERT_TIMESTAMP:
                insert = "TO_TIMESTAMP('" + valor + "', '" + formato + "')";
            break;
            case Conf.INSERT_SYSDATE:
                insert = "SYSDATE";
            break;
            case Conf.INSERT_NULL:
                insert = "NULL";
            break;
        }
        
        return insert;
    }
    
    /**
     * Funci\u00F3n que arma un string con el texto a insertar en base de datos.
     * 
     * @param tipo Tipo de insert que se desea obtener.
     * @param valor Cadena con el valor a insertar.
     * @param separador Valor boolean indicando si se debe agregar el separador al final del insert (TRUE) o no (FALSE).
     * @return insert Cadena con el valor a insertar en el formato requerido.
     */
    public static String setInsert(int tipo, String valor, boolean separador) {
        String insert = setInsertUpdateText(tipo, valor, null);

        if (separador) {
            insert += Conf.INSERT_TEXTO_SEPARADOR;
        }

        return insert;
    }
    
    /**
     * Funci\u00F3n que arma un string con el texto a insertar una fecha en base de datos.
     * 
     * @param tipo Tipo de insert que se desea obtener.
     * @param valor Cadena con el valor a insertar.
     * @param formato Cadena con el formato de la fecha a insertar.
     * @param separador Valor boolean indicando si se debe agregar el separador al final del insert (TRUE) o no (FALSE).
     * @return insert Cadena con el valor a insertar en el formato requerido.
     */
    public static String setInsertFecha(int tipo, String valor, String formato, boolean separador) {
        String insert = setInsertUpdateText(tipo, valor, formato);
        
        if(separador){
            insert += Conf.INSERT_TEXTO_SEPARADOR;
        }
        
        return insert;
    }
    
    /**
     * Funci\u00F3n que arma un string con el texto a modificar en base de datos.
     * 
     * @param tipo Tipo de update que se desea obtener.
     * @param valor Cadena con el valor a modificar.
     * @param formato Cadena con el formato de la fecha a modificar.
     * @return insert Cadena con el valor a modificar en el formato requerido.
     */
    public static String setUpdateFecha(int tipo, String valor, String formato) {
        return setInsertUpdateText(tipo, valor, formato);
    }
    
    /**
     * Funci\u00F3n que arma un string con el texto a modificar en base de datos.
     * 
     * @param tipo Tipo de update que se desea obtener.
     * @param valor Cadena con el valor a modificar.
     * @return insert Cadena con el valor a modificar en el formato requerido.
     */
    public static String setUpdate(int tipo, String valor) {
        return setInsertUpdateText(tipo, valor, null);
    }
    
    /**
     * Funci\u00F3n que devuelve un string con el formato adecuado para utilizar en consultas SQL.
     * 
     * @param tipo Tipo de select que se desea obtener.
     * @param valor Cadena con el valor a seleccionar.
     * @return String Cadena con el valor a utilizar en el formato requerido.
     */
    public static String setSelect(int tipo, String valor) {
        String select = "";
        
        switch (tipo) {
            case Conf.SELECT_DISTINCT:
                select = "DISTINCT(" + valor + ") AS " + valor;
            break;
            case Conf.SELECT_UPPER:
                select = "UPPER('" + valor + "')";
            break;
            case Conf.SELECT_SUM:
                select = "SUM(" + valor + ") AS " + valor;
            break;
            case Conf.SELECT_COUNT:
                select = "COUNT(*) AS " + valor;
            break;
            case Conf.SELECT_FECHA_TRUNC:
                select = "TRUNC(" + valor + ")";
            break;
        }
        
        return select;
    }
    
    /**
     * M\u00E9todo que devuelve un filtro gen\u00E9rico con valores por default.
     * 
     * @param condicion Valor que indica el tipo de filtro a aplicar.
     * @param campo Nombre del campo donde se aplicar\u00E9 el filtro.
     * @return filtro
     */
    public static Filtro getCondicionDefault(int condicion, String campo, Connection conn, String codArea) throws SQLException {        
        Filtro filtro = null;
        switch (condicion) {
            case Conf.FILTRO_ESTADO:
                filtro = new Filtro(Filtro.AND, campo, Filtro.EQ,
                    "UPPER('" + getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, codArea) + "')");
            break;
            case Conf.FILTRO_PDV:
                filtro = new Filtro(Filtro.AND, campo, Filtro.EQ,
                    "UPPER('" + getConfig(conn, Conf.GRUPO_PANELPDV, Conf.TIPO_PDV, codArea) + "')");
            break;
            case Conf.FILTRO_PANEL:
                filtro = new Filtro(Filtro.AND, campo, Filtro.EQ,
                    "UPPER('" + getConfig(conn, Conf.GRUPO_PANELPDV, Conf.TIPO_PANEL,codArea) + "')");
            break;
        }
        
        return filtro;
    }
    
    /**
     * Funci\u00F3n que devuelve un filtro con los datos en formato correcto para ser usado en consultas
     * con el valor indicado. Los filtros pueden ser AND u OR segun se necesite y para los filtros
     * de tipo texto se puede comparar normal o con UPPER.
     * 
     * @param condicion Valor que indica el tipo de filtro a aplicar.
     * @param campo Nombre del campo donde se aplicar\u00E9 el filtro.
     * @param valor Dato que ser\u00E9 utilizado para realizar la comparaci\u00F3n.
     * @return filtro Objeto de tipo Filtro con los datos correspondientes.
     */
    public static Filtro setCondicion(int tipoFiltro, String campo, String valor) {
        Filtro filtro = null;

        switch (tipoFiltro) {
        case Conf.FILTRO_ID_NUMERICO_AND:
            filtro = new Filtro(Filtro.AND, campo, Filtro.EQ, valor);
            break;
        case Conf.FILTRO_ID_NUMERICO_OR:
            filtro = new Filtro(Filtro.OR, campo, Filtro.EQ, valor);
            break;

        case Conf.FILTRO_ID_NUMERICO_AND_NEQ:
            filtro = new Filtro(Filtro.AND, campo, Filtro.NEQ, valor);
            break;

        case Conf.FILTRO_TEXTO_AND:
            filtro = new Filtro(Filtro.AND, campo, Filtro.EQ, "'" + valor + "'");
            break;
        case Conf.FILTRO_TEXTO_OR:
            filtro = new Filtro(Filtro.OR, campo, Filtro.EQ, "'" + valor + "'");
            break;

        case Conf.FILTRO_TEXTO_UPPER_AND:
            filtro = new Filtro(Filtro.AND, "UPPER(" + campo + ")", Filtro.EQ, "'" + valor.toUpperCase() + "'");
            break;
        case Conf.FILTRO_TEXTO_UPPER_OR:
            filtro = new Filtro(Filtro.OR, "UPPER(" + campo + ")", Filtro.EQ, "'" + valor.toUpperCase() + "'");
            break;

        case Conf.FILTRO_TEXTO_UPPER_AND_NEQ:
            filtro = new Filtro(Filtro.AND, "UPPER(" + campo + ")", Filtro.NEQ, "'" + valor.toUpperCase() + "'");
            break;
        case Conf.FILTRO_TEXTO_UPPER_OR_NEQ:
            filtro = new Filtro(Filtro.OR, "UPPER(" + campo + ")", Filtro.NEQ, "'" + valor.toUpperCase() + "'");
            break;

        case Conf.FILTRO_IN_AND:
            filtro = new Filtro(Filtro.AND, campo, Filtro.IN, "(" + valor + ")");
            break;
        case Conf.FILTRO_IN_OR:
            filtro = new Filtro(Filtro.OR, campo, Filtro.IN, "(" + valor + ")");
            break;

        case Conf.FILTRO_NOT_IN_AND:
            filtro = new Filtro(Filtro.AND, campo, Filtro.NOTIN, "(" + valor + ")");
            break;
        case Conf.FILTRO_NOT_IN_OR:
            filtro = new Filtro(Filtro.OR, campo, Filtro.NOTIN, "(" + valor + ")");
            break;

        case Conf.FILTRO_IN_UPPER_AND:
            filtro = new Filtro(Filtro.AND, "UPPER(" + campo + ")", Filtro.IN, "(" + valor.toUpperCase() + ")");
            break;
        case Conf.FILTRO_IN_UPPER_OR:
            filtro = new Filtro(Filtro.OR, "UPPER(" + campo + ")", Filtro.IN, "(" + valor.toUpperCase() + ")");
            break;

        case Conf.FILTRO_IS_NULL_AND:
            filtro = new Filtro(Filtro.AND, campo, Filtro.ISNULL, "");
            break;
        case Conf.FILTRO_IS_NULL_OR:
            filtro = new Filtro(Filtro.OR, campo, Filtro.ISNULL, "");
            break;

        case Conf.FILTRO_IS_NOT_NULL_AND:
            filtro = new Filtro(Filtro.AND, campo, Filtro.ISNOTNULL, "");
            break;
        case Conf.FILTRO_IS_NOT_NULL_OR:
            filtro = new Filtro(Filtro.OR, campo, Filtro.ISNOTNULL, "");
            break;

        case Conf.FILTRO_LIKE_AND:
            filtro = new Filtro(Filtro.AND, "UPPER(" + campo + ")", Filtro.LIKE, "'%" + valor.toUpperCase() + "%'");
            break;
        case Conf.FILTRO_LIKE_OR:
            filtro = new Filtro(Filtro.OR, "UPPER(" + campo + ")", Filtro.LIKE, "'%" + valor.toUpperCase() + "%'");
            break;
            
        case Conf.FILTRO_LIKE_PR:
            filtro = new Filtro(Filtro.OR, "UPPER(" + campo + ")", Filtro.LIKE, "'" + valor.toUpperCase() + "%'");
            break;
        case Conf.FILTRO_AVANZADO_AND:
            filtro = new Filtro(Filtro.AND, campo, "", valor);
            break;
        case Conf.FILTRO_AVANZADO_OR:
            filtro = new Filtro(Filtro.OR, campo, "", valor);
            break;
        }

        return filtro;
    }
    
    /**
     * Funci\u00F3n que devuelve un filtro con los datos en formato correcto para ser usado en consultas
     * con las fechas indicadas.
     * 
     * @param condicion Valor que indica el tipo de filtro a aplicar.
     * @param campo Nombre del campo donde se aplicar\u00E9 el filtro.
     * @param fechaIni Fecha inicial del rango a comparar. Fecha a utilizar cuando se quiere un filtro de fecha \u00FAnica.
     * @param fechaFin Fecha final del rango a comparar.
     * @param formato Formato de las fechas ingresadas.
     * @return filtro Objeto de tipo Filtro con los datos correspondientes.
     */
    public static Filtro setCondicion(int tipoFiltro, String campo, String fechaIni, String fechaFin, String formato) {
        Filtro filtro = null;
        
        switch (tipoFiltro) {
            case Conf.FILTRO_FECHA:
                filtro = new Filtro(Filtro.AND, campo, Filtro.EQ,
                    "TO_DATE('" + fechaIni + "', '" + formato + "')");
            break;
            case Conf.FILTRO_FECHA_TRUNC:
                filtro = new Filtro(Filtro.AND, "TRUNC(" + campo + ")", Filtro.EQ,
                    "TRUNC(TO_DATE('" + fechaIni + "', '" + formato + "'))");
            break;
            case Conf.FILTRO_RANGO_FECHAS:
                String between =
                "TO_DATE('" + fechaIni + "', '" + formato + "')" + " AND " + "TO_DATE('" + fechaFin + "', '" + formato + "')";
                filtro = new Filtro(Filtro.AND, "TRUNC(" + campo + ")", Filtro.BETWEEN, between);
            break;
            case Conf.FILTRO_FECHA_LTE_AND:
                filtro = new Filtro(Filtro.AND, "TRUNC(" + campo + ")", Filtro.LTE,
                    "TO_DATE('" + fechaIni + "', '" + formato + "')");
            break;
            case Conf.FILTRO_FECHA_GTE_AND:
                filtro = new Filtro(Filtro.AND, "TRUNC(" + campo + ")", Filtro.GTE,
                    "TO_DATE('" + fechaIni + "', '" + formato + "')");
            break;
            case Conf.FILTRO_FECHA_LTE_OR:
                filtro = new Filtro(Filtro.OR, "TRUNC(" + campo + ")", Filtro.LTE,
                    "TO_DATE('" + fechaIni + "', '" + formato + "')");
            break;
            case Conf.FILTRO_FECHA_GTE_OR:
                filtro = new Filtro(Filtro.OR, "TRUNC(" + campo + ")", Filtro.GTE,
                    "TO_DATE('" + fechaIni + "', '" + formato + "')");
            break;
            case Conf.FILTRO_SYSDATE_TRUNC_AND:
                filtro = new Filtro(Filtro.AND, "TRUNC(" + campo + ")", Filtro.EQ,
                    "TRUNC(SYSDATE)");
            break;

            case Conf.FILTRO_TIMESTAMP_GTE_AND:
            filtro = new Filtro(Filtro.AND, campo, Filtro.GTE,
                    "TO_TIMESTAMP('" + fechaIni + "', '" + Conf.TXT_FORMATO_FECHA_BD + "')");
            break;
            
            case Conf.FILTRO_TIMESTAMP_TRUNC_AND:
            filtro = new Filtro(Filtro.AND, "TRUNC(" + campo + ")", Filtro.EQ,
                    "TRUNC(TO_TIMESTAMP('" + fechaIni + "', '" + Conf.TXT_FORMATO_FECHA_BD + "'))");
            break;
        }
        
        return filtro;
    }
    
    public static Filtro setCondicionConfig(String campo, String grupo, String nombre, String codArea) {
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Catalogo.CAMPO_TC_SC_CATPAIS_ID,  " (SELECT TCSCCATPAISID FROM TC_SC_CAT_PAIS WHERE AREA ='"+codArea+"')"));
        condiciones.add(setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_GRUPO, grupo));
        condiciones.add(setCondicion(Conf.FILTRO_TEXTO_UPPER_AND, Catalogo.CAMPO_NOMBRE, nombre));

        Filtro filtro = setCondicion(Conf.FILTRO_IN_UPPER_AND, campo, "("
                + UtileriasBD.armarQuerySelectField(Catalogo.N_TABLA, Catalogo.CAMPO_VALOR, condiciones, null) + ")");

        return filtro;
    }
    
    public static Filtro setCondicionMultiple(int tipoFiltro, String tabla, String campo, String valor) {
        Filtro filtro = null;
        
        switch (tipoFiltro) {
            case Conf.FILTRO_TEXTO_AND:
                filtro = new Filtro(Filtro.AND, tabla + "." + campo, Filtro.EQ, "'" + valor + "'");
            break;
            case Conf.FILTRO_TEXTO_OR:
                filtro = new Filtro(Filtro.OR, tabla + "." + campo, Filtro.EQ, "'" + valor + "'");
            break;
    
            case Conf.FILTRO_TEXTO_UPPER_AND:
                filtro = new Filtro(Filtro.AND, "UPPER(" + tabla + "." + campo + ")", Filtro.EQ, "'" + valor.toUpperCase() + "'");
            break;
            case Conf.FILTRO_TEXTO_UPPER_OR:
                filtro = new Filtro(Filtro.OR, "UPPER(" + tabla + "." + campo + ")", Filtro.EQ, "'" + valor.toUpperCase() + "'");
            break;
        
            case Conf.FILTRO_ID_NUMERICO_AND:
                filtro = new Filtro(Filtro.AND, tabla + "." + campo, Filtro.EQ, valor);
            break;
            case Conf.FILTRO_ID_NUMERICO_OR:
                filtro = new Filtro(Filtro.OR, tabla + "." + campo, Filtro.EQ, valor);
            break;
            
            case Conf.FILTRO_IS_NOT_NULL_AND:
                filtro = new Filtro(Filtro.OR, tabla + "." + campo, Filtro.ISNOTNULL, null);
            break;
            case Conf.FILTRO_IS_NOT_NULL_OR:
                filtro = new Filtro(Filtro.OR, tabla + "." + campo, Filtro.ISNOTNULL, null);
            break;
            
            case Conf.FILTRO_LIKE_AND:
                filtro = new Filtro(Filtro.AND, "UPPER(" + tabla + "." + campo + ")", Filtro.LIKE,
                    "'%" + valor.toUpperCase() + "%'");
            break;
            case Conf.FILTRO_LIKE_OR:
                filtro = new Filtro(Filtro.OR, "UPPER(" + tabla + "." + campo + ")", Filtro.LIKE,
                    "'%" + valor.toUpperCase() + "%'");
            break;
        }
        
        return filtro;
    }
    
    public static Filtro setCondicionMultiple(int tipoFiltro, String tablaA, String campoA, String tablaB, String campoB) {
        Filtro filtro = null;
        
        switch (tipoFiltro) {
            case Conf.FILTRO_ID_NUMERICO_AND:
                filtro = new Filtro(Filtro.AND, tablaA + "." + campoA, Filtro.EQ, tablaB + "." + campoB);
            break;
            case Conf.FILTRO_ID_NUMERICO_OR:
                filtro = new Filtro(Filtro.OR, tablaA + "." + campoA, Filtro.EQ, tablaB + "." + campoB);
            break;
            
            case Conf.FILTRO_IS_NOT_NULL_AND:
                filtro = new Filtro(Filtro.OR, tablaA + "." + campoA, Filtro.ISNOTNULL, null);
            break;
            case Conf.FILTRO_IS_NOT_NULL_OR:
                filtro = new Filtro(Filtro.OR, tablaA + "." + campoA, Filtro.ISNOTNULL, null);
            break;
        }
        
        return filtro;
    }
    
    /**
     * M\u00E9todo para devolver datos de los distribuidores.
     * 
     * @param conn Conexi\u00F3n a BD.
     * @return 
     * @throws SQLException
     */
    public static List<Map<String, String>> getDistribuidores(Connection conn, BigDecimal idPais, String codArea) throws SQLException {
        String nombreMetodo = "getDistribuidores";
        String nombreClase = new CurrentClassGetter().getClassName();

        Map<String, String> datosDTS = new HashMap<String, String>();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        List<Filtro> condiciones = new ArrayList<Filtro>();
        condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, Distribuidor.CAMPO_TCSCCATPAISID,idPais.toString()));
        PreparedStatement pstmt = null;
        ResultSet rst =null;
        String campos[] = {
            Distribuidor.CAMPO_TC_SC_DTS_ID,    
            Distribuidor.CAMPO_TCSCBODEGAVIRTUALID,
            Distribuidor.CAMPO_NOMBRES,
            Distribuidor.CAMPO_TIPO
        };
        
        String sql = UtileriasBD.armarQuerySelect(Distribuidor.N_TABLA, campos, condiciones);
        try{
	        pstmt = conn.prepareStatement(sql);
	        rst = pstmt.executeQuery();
	        
	        if (rst != null) {
	            if (!rst.next()) {
	                log.debug("No existen registros en la tabla con esos par\u00E9metros.");
	                
	                Respuesta respuesta = new ControladorBase().getMensaje(Conf_Mensajes.MSJ_ERROR_DATOS_DTS, null,
	                    nombreClase, nombreMetodo, null, codArea );
	                
	                datosDTS.put(Conf.DTS_NOMBRES , respuesta.getDescripcion());
	            } else {
	                do {
	                    datosDTS = new HashMap<String, String>();
	
	                    datosDTS.put(Distribuidor.CAMPO_TC_SC_DTS_ID, rst.getString(Distribuidor.CAMPO_TC_SC_DTS_ID));
	                    datosDTS.put(Conf.DTS_NOMBRES, rst.getString(Distribuidor.CAMPO_NOMBRES));
	                    datosDTS.put(Conf.DTS_IDBODEGAVIRTUAL, rst.getString(Distribuidor.CAMPO_TCSCBODEGAVIRTUALID));
	                    datosDTS.put(Conf.DTS_TIPO, rst.getString(Distribuidor.CAMPO_TIPO));
	
	                    list.add(datosDTS);
	                } while (rst.next());
	            }
	        }
        }finally{
        	DbUtils.closeQuietly(rst);
        	DbUtils.closeQuietly(pstmt);
        }
        return list;
    }
    
    /**
     * M\u00E9todo para consultar si no existe la bodega virtual ingresada asociada a otro dst, panel o ruta.
     * @throws SQLException 
     */
    public static BigDecimal existeBodegaVirtual(Connection conn, String estado, String idBodega, String idDTS, BigDecimal idPais)
            throws SQLException {
        BigDecimal respuesta = null;
        StringBuilder query = new StringBuilder();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            query .append("SELECT ((SELECT COUNT(1) FROM TC_SC_DTS " );
            query .append(        "WHERE TCSCCATPAISID = " + idPais + " AND TCSCBODEGAVIRTUALID = " + idBodega);
            query .append(" AND UPPER('ESTADO') = '" + estado + idDTS + "') + " );
            query .append(      "(SELECT COUNT(1) FROM TC_SC_PANEL " );
            query .append(       "WHERE TCSCCATPAISID = " + idPais + " AND TCSCBODEGAVIRTUALID = " + idBodega);
            query .append(         " AND UPPER('ESTADO') = '" + estado + "') + " );
            query .append(    "(SELECT COUNT(1) FROM TC_SC_RUTA " );
            query .append(        "WHERE TCSCCATPAISID = " + idPais + " AND TCSCBODEGAVIRTUALID = " + idBodega);
            query .append(         " AND UPPER('ESTADO') = '" + estado + "')) FROM DUAL"); 

            log.trace("query para validar que bodega virtual no exista en otra panel, ruta, o dts: " + query.toString());

            pstmt = conn.prepareStatement(query.toString());
     
            rst = pstmt.executeQuery();

            if (rst.next()) {
                respuesta = rst.getBigDecimal(1);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return respuesta;
    }
    
    /**
     * Funci\u00F3n que redondea un numero a la cantidad de decimales indicados, redondeando hacia el inmediato superior.   
     * 
     * @param valor Valor de tipo double a redondear.
     * @param decimales N\u00FAmero entero que indica la cantidad de decimales a la que se redondear\u00E9 el valor. 
     * @return Valor de tipo BigDecimal con el valor operado.
     */
    public static BigDecimal redondear(BigDecimal numero, int decimales) {
        numero = numero.setScale(decimales, RoundingMode.HALF_UP);
        return numero;
    }

    /**
     * Funci\u00F3n que redondea un numero a una cantidad de decimales parametrizados, redondeando hacia el inmediato superior.   
     * 
     * @param valor Valor de tipo double a redondear.
     * @param cantDecimalesBD Cantidad de deimales a los que se desea redondear.
     * @return Valor de tipo BigDecimal con el valor operado.
     */
    public static BigDecimal redondearBD(BigDecimal valor, int cantDecimalesBD) {
        return valor.setScale(cantDecimalesBD, RoundingMode.HALF_UP);
    }

    /***
     * M\u00E9todo para obtener los vendedores que tiene asignados una ruta o panel
     * @throws SQLException 
     * */
    public static List<VendedorPDV> getVendPanelPdv(Connection conn, BigDecimal id, String idTipo, String estado,
            String condicion, BigDecimal idPais) throws SQLException {
        List<VendedorPDV> listaVend = new ArrayList<VendedorPDV>();
        VendedorPDV obj = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String Addcondicion = "";

        if (!(condicion == null || "".equals(condicion))) {
            Addcondicion = " AND " + VendedorPDV.CAMPO_VENDEDOR + " NOT IN (" + condicion + ")";
        }
        if (!(estado == null || "".equals(estado))) {
            Addcondicion += " AND UPPER(" + VendedorPDV.CAMPO_ESTADO + ") = '" + estado.toUpperCase() + "'";
        }

        StringBuilder query = new StringBuilder();
        		
        		
        query.append("SELECT ");
        query.append( VendedorPDV.CAMPO_VENDEDOR + ", ");
        query.append( VendedorPDV.CAMPO_ESTADO );
        query.append( " FROM " + VendedorPDV.N_TABLA);
        query.append( " WHERE " + VendedorPDV.CAMPO_TCSCCATPAISID + " = " + idPais);
        query.append( " AND " + VendedorPDV.CAMPO_IDTIPO + " = " + id);
        query.append( " AND " + VendedorPDV.CAMPO_TIPO + " = '" + idTipo + "' " + Addcondicion);
        log.trace("query: " + query.toString());

        try{
	        pstmt = conn.prepareStatement(query.toString());
	        rst = pstmt.executeQuery();
	
	        while (rst.next()) {
	            obj = new VendedorPDV();
	            obj.setVendedor(rst.getBigDecimal(VendedorPDV.CAMPO_VENDEDOR));
	            obj.setEstado(rst.getString(VendedorPDV.CAMPO_ESTADO));
	            listaVend.add(obj);
	        }
        }finally{
        	DbUtils.closeQuietly(pstmt);
        	DbUtils.closeQuietly(rst);
        }

        return listaVend;
    }

    /**
     * Funci\u00F3n que valida si un statement batch ejecut\u00F3 correctamente todos los
     * items.
     * 
     * @param tipo
     *            Par\u00E9metro de tipo entero que indica el elemento a coincidir
     *            para validar, 0 para validar inserts, 1 o cantidad de querys
     *            para validar updates.
     * @param updateCounts
     *            Respuesta de un executeBatch.
     * @return
     */
    public static boolean validarBatch(int tipo, int[] updateCounts) {
        for (int i = 0; i < updateCounts.length; i++) {
            if (updateCounts[i] != tipo) {
                log.error("Uno de los updates/inserts devolvi\u00F3 un count diferente al deseado.");
                return false;
            }
        }

        return true;
    }
    
    /**
     * Funci\u00F3n que valida si un statement batch ejecut\u00F3 correctamente todos los items.
     * 
     * @param cantRangoUpdate Listado de cantidades de totales de rango a comparar contra los datos actualizados de executeBatch.
     * @param updateRangoCounts Respuesta de un executeBatch.
     * @return
     */
    public static boolean validarBatchRangos(List<Integer> cantRangoUpdate, int[] updateRangoCounts) {
        if (cantRangoUpdate != null && (cantRangoUpdate.size() == updateRangoCounts.length)) {
            for (int i = 0; i < updateRangoCounts.length; i++) {
                if (updateRangoCounts[i] != cantRangoUpdate.get(i)) {
                    log.error("Uno de los updates/inserts por rango devolvi\u00F3 un count diferente al deseado.");
                    return false;
                }
            }
        } else {
            log.error("La cantidad de rangos y totales de rango son diferentes.");
            return false;
        }

        return true;
    }
    
    /**
     * Funci\u00F3n que recibe una cadena String y valida
     * si esta es null o si est\u00E9 vac\u00EDa. Si as\u00ED fuese
     * devolver\u00E9 una Excepcion, en caso contrario retorna
     * la cadena recibida.
     * 
     * @param cadena
     * @return String
     * @throws Exception
     * */
    public static String validarParametroVacio(String cadena) throws Exception {
    	if (cadena != null && !"".equals(cadena.trim())) {
			return cadena;
		} else {
			throw new Exception("Par\u00E9metro de configuracion vac\u00EDo.");
		}
    }
    
    /**
     * Funci\u00F3n para insertar un listado de logs Sidra.
     * 
     * @param listaLog
     * @param usuario
     */
    public static void doInsertLog(List<LogSidra> listaLog, String usuario, String codArea) {
        InputLogSidra inputLog = new InputLogSidra();
        inputLog.setCodArea(codArea);
        inputLog.setLog(listaLog);
        inputLog.setUsuario(usuario);
        new CtrlLogSidra().insertaLog(inputLog, true);
    }

    /**
     * Funci\u00F3n que compara un valor contra la nulidad y devuelve una cadena vacia o el mismo valor.
     * 
     * @param campo Parametro de tipo String a comparar.
     * @return String con el valor enviado o una cadena vacia en caso de ser nulo.
     */
    public static String getValue(String campo) {
        return campo != null ? campo : "";
    }
    
    /**
     * Funci\u00F3n que retorna el valor de un ResultSet segun el tipo y en caso de ser nulo retorna una cadena vacia.
     * 
     * @param rst
     * @param tipo
     * @param campo
     * @return
     * @throws SQLException
     */
    public static String getRstValue(ResultSet rst, int tipo, String campo) throws SQLException {
    	   SimpleDateFormat FORMATO_FECHAHORA = new SimpleDateFormat(Conf.TXT_FORMATO_FECHAHORA);
        switch (tipo) {
        case Conf.TIPO_NUMERICO:
            return rst.getBigDecimal(campo) != null ? rst.getBigDecimal(campo).toString() : "";

        case Conf.TIPO_TEXTO:
            return rst.getString(campo) != null ? rst.getString(campo) : "";

        case Conf.TIPO_FECHA:
            return rst.getTimestamp(campo) != null ? FORMATO_FECHAHORA.format(rst.getTimestamp(campo)) : "";

        default:
            return "";
        }
    }

    public static String replaceChars(String cuerpo) {
        cuerpo = cuerpo.replace("\u00E9", "&aacute;");
        cuerpo = cuerpo.replace("\u00E9", "&Aacute;");
        cuerpo = cuerpo.replace("\u00E9", "&eacute;");
        cuerpo = cuerpo.replace("\u00E9", "&Eacute;");
        cuerpo = cuerpo.replace("\u00ED", "&iacute;");
        cuerpo = cuerpo.replace("\u00ED", "&Iacute;");
        cuerpo = cuerpo.replace("\u00F3", "&oacute;");
        cuerpo = cuerpo.replace("\u00F3", "&Oacute;");
        cuerpo = cuerpo.replace("\u00FA", "&uacute;");
        cuerpo = cuerpo.replace("\u00FA", "&Uacute;");
        cuerpo = cuerpo.replace("\u00F1", "&ntilde;");
        cuerpo = cuerpo.replace("\u00D1", "&Ntilde;");

        return cuerpo;
    }
    
    /**
     * Recibe un par\u00E9metro como tipo OBJECT, el cual podr\u00EDa
     * ser una clase con todas su propiedades, el objeto es 
     * convertido a JSon con la librer\u00EDa Gson.
     * @param mensaje
     * @param jsoncallback
     * @return
     */
    public static String convertJson(Object mensaje, String jsoncallback) {
        String response = "";
        if (mensaje instanceof JSONObject || mensaje instanceof String) {
            response = mensaje.toString();
        } else {
            response = new Gson().toJson(mensaje);
        }
        if (jsoncallback == null || jsoncallback.trim().length() == 0) {
            return response;
        } else {
            return jsoncallback + "(" + response + ");";
        }
    }

    /**
     * Funci\u00F3n que retorna un valor convertido seg\u00FAn la tasa de cambio.
     * 
     * @param valor
     * @param tasaCambio
     * @return
     */
    public static String convertirMoneda(String valor, String tasaCambio) {
        BigDecimal monto ;
        monto = new BigDecimal(valor).multiply(new BigDecimal(tasaCambio));
        return redondear(monto, 2).toString();
    }
}

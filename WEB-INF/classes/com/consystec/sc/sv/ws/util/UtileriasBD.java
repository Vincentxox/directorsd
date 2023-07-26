package com.consystec.sc.sv.ws.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

/**
 * @author      Victor Cifuentes @ Consystec - 2016
 * @version     2.4
 * 
 * @Changelog   - Arreglos a algunas funciones.<br>
 *              - Se cambiaron funciones globales.<br>
 *              - Se agregaron funciones nuevas.<br>
 *              - Cambios a documentaci\u00F3n.<br>
 *              - Cambios a funciones de consulta en los hashmaps.<br>
 *              - Arreglos en unos m\u00E9todos.<br>
 */
public class UtileriasBD {
	private UtileriasBD(){}
    private static final Logger log = Logger.getLogger(UtileriasBD.class);
    
    /**
     * Este m\u00E9todo es similar a {@link #armarQrySelect}, con la diferencia que
     * solicita menos par\u00E9metros, en este caso \u00FAnicamente son requeridos tabla,
     * campos y condiciones.
     */
    public static String armarQuerySelect(String tabla, String[] campos, List<Filtro> condiciones) {
        return armarQuerySelect(tabla, campos, condiciones, null);
    }

    /**
     * Este m\u00E9todo es similar a {@link #armarQrySelect}, con la diferencia que
     * solicita menos par\u00E9metros, en este caso son requeridos tabla, campos,
     * condiciones y orden.
     */
    public static String armarQuerySelect(String tabla, String[] campos, List<Filtro> condiciones, List<Order> orden) {
        String sql = "SELECT ";

        sql += getCampos(campos);
        sql += " FROM " + tabla;
        sql += getCondiciones(condiciones);
        sql += getOrden(orden);

        log.debug("Qry Get: " + sql);
        return sql;
    }
    
    /**
     * M\u00E9todo que devuelve una cadena con un query de consulta de un campo
     * espec\u00EDfico de una tabla bajo las condiciones enviadas.
     * 
     * @param tabla
     *            Tabla a utilizar en la consulta.
     * @param campo
     *            Nombre del campo de la tabla a consultar.
     * @param condiciones
     *            Listado de condiciones que se deben verificar mediante WHERE.
     * @param orden
     *            Listado de objetos de tipo Order para la orientacion de
     *            resultados mediante ORDER BY.
     * @return Cadena de texto que integra todos los par\u00E9metros en un query tipo
     *         SELECT.
     */
    public static String armarQuerySelectField(String tabla, String campo, List<Filtro> condiciones,
            List<Order> orden) {
        String sql = "SELECT " + campo;

        sql += " FROM " + tabla;
        sql += getCondiciones(condiciones);
        sql += getOrden(orden);

        log.debug("Qry Get: " + sql);
        return sql;
    }

    /**
     * Funci\u00F3n que arma una cadena de consulta de tipo SELECT a multiples tablas
     * relacionadas.
     * 
     * @param campos
     *            Arreglo bidimensional de Strings que indica el campo y su
     *            respectiva tabla a consultar.
     * @param tablas
     *            Arreglo de String que contiene el nombre de todas las tablas a
     *            consultar.
     * @param condiciones
     *            Listado de condiciones que se deben verificar mediante WHERE.
     * @param orden
     *            Listado de objetos de tipo Order para la orientacion de
     *            resultados mediante ORDER BY.
     * @return Cadena de texto que integra todos los par\u00E9metros en un query tipo
     *         SELECT.
     */
    public static String armarQueryGetMultiple(String[][] campos, String[] tablas, List<Filtro> condiciones, List<Order> orden) {
        String sql = "SELECT ";

        for (int i = 0; i < campos.length; i++) {
            if (campos[i][0] == null) {
                sql += campos[i][1];
            } else {
                sql += campos[i][0] + "." + campos[i][1];
            }
            if (i != campos.length - 1)
                sql += ", ";
        }

        sql += " FROM ";
        for (int i = 0; i < tablas.length; i++) {
            if (tablas[i].contains("PARTITION")) {
                sql += tablas[i] + " " + tablas[i].split(" ")[0];
            } else {
                sql += tablas[i] + " " + tablas[i];
            }

            if (i != tablas.length - 1)
                sql += ", ";
        }

        sql += getCondiciones(condiciones);
        sql += getOrden(orden);

        log.debug("Qry GetMultiple: " + sql);
        return sql;
    }
    
    /**
     * M\u00E9todo que mediante los par\u00E9metros indicados, arma un query de tipo
     * INSERT INTO de un listado de valores a insertar.
     * 
     * @param tabla
     *            Tabla a utilizar en la inserci\u00F3n.
     * @param campos
     *            Array de campos en los cuales se realiz\u00E9n los inserts.
     * @param inserts
     *            Listado de Strings con los inserts a utilizar, se deben enviar
     *            entre comillas simples o ap\u00F3strofes los valores de tipo texto,
     *            se pueden enviar insert compuestos como por ejemplo
     *            "UPPER('texto')".
     * @return Cadena de texto que integra todos los par\u00E9metros en un query
     *         tipo INSERT INTO.
     */
    public static String armarQueryInsert(String tabla, String[] campos, List<String> inserts) {
        String sql = "INSERT INTO " + tabla;

        String columnas = " (";
        for (int i = 0; i < campos.length; i++) {
            columnas += campos[i];
            if (i != campos.length - 1)
                columnas += ", ";
        }
        columnas += ")";
        
        String insert = "";
        for (int i = 0; i < inserts.size(); i++) {
            insert += inserts.get(i);
        }

        sql += columnas + " VALUES " + insert;

        log.debug("Qry Post: " + sql);
        return sql;
    }
    
    /**
     * M\u00E9todo que mediante los par\u00E9metros indicados, arma un query de tipo
     * INSERT INTO
     * 
     * @param tabla
     *            Tabla a utilizar en la inserci\u00F3n.
     * @param campos
     *            Array de campos en los cuales se realiz\u00E9n los inserts.
     * @param inserts
     *            Cadena con los inserts a utilizar, se deben enviar entre
     *            comillas simples o ap\u00F3strofes los valores de tipo texto, se
     *            pueden enviar insert compuestos como por ejemplo
     *            "UPPER('texto')".
     * @return Cadena de texto que integra todos los par\u00E9metros en un query
     *         tipo INSERT INTO.
     */
    public static String armarQryInsert(String tabla, String[] campos, String inserts) {
        String sql = "INSERT INTO " + tabla;
        sql += " (" + getCampos(campos) + ")";
        sql += " VALUES " + inserts;

        log.debug("Qry Insert: " + sql);
        return sql;
    }

    /**
     * M\u00E9todo que mediante los par\u00E9metros indicados, arma un query de tipo
     * INSERT ALL, lo que permite inserciones m\u00FAltiples.
     * 
     * @param tabla
     *            Tabla a utilizar en la consulta.
     * @param campos
     *            Array de campos en los cuales se realiz\u00E9n los inserts.
     * @param inserts
     *            Listado de Strings con los inserts a utilizar, se deben enviar
     *            entre comillas simples o ap\u00F3strofes los valores de tipo texto,
     *            se pueden enviar insert compuestos como por ejemplo
     *            "UPPER('texto')".
     * @return Cadena de texto que integra todos los par\u00E9metros en un query
     *         tipo INSERT ALL.
     */
    public static String armarQueryInsertAll(String tabla, String[] campos, List<String> inserts) {
        String sql = "INSERT INTO " + tabla;
        
        String columnas = " (";
        for (int i = 0; i < campos.length; i++) {
            columnas += campos[i];
            if (i != campos.length - 1)
                columnas += ", ";
        }
        columnas += ") ";
        
        String insert = "";
        for (int i = 0; i < inserts.size(); i++) {
            if (i > 0) {
                insert += "UNION ALL ";
            }   
            insert += "SELECT " + inserts.get(i) + " FROM DUAL ";
        }

        sql += columnas + insert;

        log.debug("Qry Post: " + sql);
        return sql;
    }

    /**
     * M\u00E9todo que mediante los par\u00E9metros indicados, arma un query de tipo
     * UPDATE.
     * 
     * @param tabla
     *            Tabla a utilizar en la consulta.
     * @param campos
     *            Array multidimensional de campos a modificar, en la primera
     *            dimensi\u00F3n se env\u00EDa el nombre del campo a modificar y en la
     *            segunda dimensi\u00F3n del array se env\u00EDa el valor a ingresar en
     *            ese campo modificado. se deben enviar entre comillas simples o
     *            ap\u00F3strofes los valores de tipo texto, se pueden enviar valores
     *            compuestos como por ejemplo "UPPER('texto')".
     * @param condiciones
     *            Listado de condiciones que se deben verificar mediante WHERE.
     * @return Cadena de texto que integra todos los par\u00E9metros en un query
     *         tipo UPDATE.
     */
    public static String armarQueryUpdate(String tabla, String[][] campos, List<Filtro> condiciones) {
        String sql = "UPDATE " + tabla + " SET ";
        
        for (int i = 0; i < campos.length; i++) {
            sql += campos[i][0] + " = " + campos[i][1];
            if (i != campos.length - 1)
                sql += ", ";
        }
        
        sql += getCondiciones(condiciones);

        log.debug("Qry Put/Del: " + sql);
        return sql;
    }
    
    /**
     * M\u00E9todo que mediante los par\u00E9metros indicados, arma un query de tipo
     * DELETE.
     * 
     * @param tabla
     *            Tabla a utilizar en la consulta.
     * @param condiciones
     *            Listado de condiciones que se deben verificar mediante WHERE.
     * @return Cadena de texto que integra todos los par\u00E9metros en un query
     *         tipo DELETE.
     */
    public static String armarQueryDelete(String tabla, List<Filtro> condiciones) {
        String sql = "DELETE FROM " + tabla;

        sql += getCondiciones(condiciones);

        log.debug("Qry Delete: " + sql);
        return sql;
    }
    
    /**
     * Este m\u00E9todo es similar a {@link #selectCount}, con la diferencia retorna
     * un string en vez de un int.
     */
    public static String verificarExistencia(Connection conn, String tabla, List<Filtro> condiciones)
            throws SQLException {
        return selectCount(conn, tabla, condiciones) + "";
    }
    
    /**
     * Funci\u00F3n que realiza una consulta para obtener un COUNT de registros que
     * cumplan con las condiciones indicadas. Verifica las condiciones mediante
     * el tipo de filtro que se env\u00EDe, se pueden enviar filtros de tipo AND y
     * OR.
     * 
     * @param conn
     *            Objeto de tipo Connection que contiene la conexi\u00F3n a BD a
     *            utilizar.
     * @param tabla
     *            Tabla a utilizar en la consulta.
     * @param condiciones
     *            Listado de filtros o condiciones que se desean en la consulta,
     *            pueden ser de tipo AND y OR.
     * @return Valor de tipo enter con la cantidad de registros encontrados.
     * @throws SQLException
     */
    public static int selectCount(Connection conn, String tabla, List<Filtro> condiciones) throws SQLException {
        int count = 0;
        StringBuilder sql = new StringBuilder();
       sql.append("SELECT COUNT(1) FROM ");
       sql.append( tabla);
       sql.append( getCondiciones(condiciones));


        log.debug("Qry Count: " + sql.toString());

        Statement stm = null;
        ResultSet rst = null;

        try {
            stm = conn.createStatement();
            rst = stm.executeQuery(sql.toString());
            if (rst.next()) {
                count = rst.getInt(1);
                log.trace("Existencias: " + count);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(stm);
        }

        return count;
    }

    /**
     * Funci\u00F3n que separa un listado de condiciones y las agrupa respectivamente
     * en AND u OR.
     * 
     * @param condiciones
     *            Listado de condiciones a agrupar.
     * @return Listado de condiciones agrupadas.
     */
    public static String agruparCondiciones(List<Filtro> condiciones) {
        String condicionesAND = "";
        String condicionesOR = "";

        String sql = "(";
        if (condiciones != null && condiciones.size() > 0) {
            for (int i = 0; i < condiciones.size(); i++) {
                String operadorLogico = condiciones.get(i).getComparacion();
                String where = condiciones.get(i).getField() + " " + condiciones.get(i).getOperator() + " "
                        + condiciones.get(i).getValue();

                if (i != 0) {
                    if (operadorLogico == null) {
                        condicionesAND += " " + Filtro.AND + " ";
                    } else if (operadorLogico.equals(Filtro.AND) && !condicionesAND.equals("")) {
                        condicionesAND += " " + condiciones.get(i).getComparacion() + " ";
                    } else if (operadorLogico.equals(Filtro.OR) && !condicionesOR.equals("")) {
                        condicionesOR += " " + condiciones.get(i).getComparacion() + " ";
                    }
                }

                if (condiciones.get(i).getComparacion() == null) {
                    condicionesAND += where;
                } else if (operadorLogico.equals(Filtro.AND)) {
                    condicionesAND += where;
                } else if (operadorLogico.equals(Filtro.OR)) {
                    condicionesOR += where;
                }
            }
            if (condicionesOR.equals("")) {
                sql += condicionesAND;
            } else if (condicionesAND.equals("")) {
                sql += condicionesOR;
            } else {
                sql += condicionesAND + " " + Filtro.AND + " (" + condicionesOR + ") ";
            }
        }

        sql += ") ";

        return sql;
    }

    /**
     * Devuelve un \u00FAnico campo, puede ser un ID o cualquier otro valor pero de
     * un solo registro, lo que implica que las condiciones enviadas deben
     * asegurar un registro \u00FAnico.
     * 
     * @param conn
     *            Objeto de tipo Connection que contiene la conexi\u00F3n a BD a
     *            utilizar.
     * @param campo
     *            Nombre del campo que se desea obtener.
     * @param tabla
     *            Tabla donde se encuentra el campo a mostrar.
     * @param condiciones
     *            Listado de filtros o condiciones que se desean en la consulta.
     * @return Valor que se encuentra en el campo que cumpla con las
     *         condiciones dadas.
     * @throws SQLException
     */
    public static String getOneRecord(Connection conn, String campo, String tabla, List<Filtro> condiciones)
            throws SQLException {
        String resultado = "";
        StringBuilder sql = new StringBuilder();
        sql.append( "SELECT " + campo + " FROM " + tabla);
        sql.append(getCondiciones(condiciones));
        log.debug("QryOneRecord: " + sql.toString());

        Statement stm = null;
        ResultSet rst = null;

        try {
            stm = conn.createStatement();
            rst = stm.executeQuery(sql.toString());

            if (rst.next()) {
                resultado = rst.getString(1);
            }
            log.trace("Valor del Campo: " + resultado);
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(stm);
        }

        return resultado;
    }
    
    /**
     * Devuelve una columna de un conjunto de resultados, puede ser un ID o
     * cualquier otro valor.
     * 
     * @param conn
     *            Objeto de tipo Connection que contiene la conexi\u00F3n a BD a
     *            utilizar.
     * @param columna
     *            Nombre del campo que se desea obtener.
     * @param tabla
     *            Tabla donde se encuentra el campo a mostrar.
     * @param condiciones
     *            Listado de filtros o condiciones que se desean en la consulta.
     * @param orden
     *            Listado que indica el orden por campo en que se desean los
     *            resultados.
     * @return Listado de valores que se encuentran en la columna que cumpla con
     *         las condiciones dadas.
     * @throws SQLException
     */
    public static List<String> getOneField(Connection conn, String columna, String tabla, List<Filtro> condiciones,
            List<Order> orden) throws SQLException {
        List<String> resultados = new ArrayList<String>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT " + columna + " FROM " + tabla);
        sql.append( getCondiciones(condiciones));
        sql.append(getOrden(orden));
        log.debug("QryOneField: " + sql.toString());

        Statement stm = null;
        ResultSet rst = null;

        try {
            stm = conn.createStatement();
            rst = stm.executeQuery(sql.toString());

            if (rst.next()) {
                do {
                    resultados.add(rst.getString(1));
                } while (rst.next());
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(stm);
        }

        return resultados;
    }

    /**
     * Funci\u00F3n que realiza una consulta en base a las condiciones dadas y
     * devuelve el listado de campos que se indican con el orden sugerido.
     * 
     * @param conn
     *            Objeto de tipo Connection que contiene la conexi\u00F3n a BD a
     *            utilizar.
     * @param tabla
     *            Tabla donde se encuentra el campo a mostrar.
     * @param campos
     *            Array de strings con los nombres de los campos a obtener de la
     *            consulta.
     * @param condiciones
     *            Listado de filtros o condiciones que se desean en la consulta.
     * @param orden
     *            Listado que indica el orden por campo en que se desean los
     *            resultados.
     * @param orden
     *            Listado que indica los campos por los que agruparan los
     *            resultados.
     * @return Listado de mapas de strings con los valores recuperados que
     *         cumplen con las condiciones dadas, identificados cada uno por su
     *         nombre de campo.
     * @throws SQLException
     */
    public static List<Map<String, String>> getSingleData(Connection conn, String tabla, String[] campos,
            List<Filtro> condiciones, List<Order> orden, String[] groupBy) throws SQLException {
        List<Map<String, String>> resultados = new ArrayList<Map<String, String>>();
        StringBuilder sql = new StringBuilder();
        sql.append( "SELECT ");
        sql.append( getCampos(campos));
        sql.append(" FROM " + tabla);
        sql.append( getCondiciones(condiciones));
        sql.append( getGroupBy(groupBy));
        sql.append(getOrden(orden));

        log.debug("QrySingleData: " + sql.toString());

        Statement stm = null;
        ResultSet rst = null;

        try {
            stm = conn.createStatement();
            rst = stm.executeQuery(sql.toString());
            if (rst.next()) {
                do {
                    Map<String, String> datos = new HashMap<String, String>();

                    datos.putAll(getHashMapsResults(campos, rst));

                    resultados.add(datos);
                } while (rst.next());
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(stm);
        }

        return resultados;
    }

    public static List<Map<String, String>> getSingleData(Connection conn, String tabla, String[] campos,
            List<Filtro> condiciones, List<Order> orden) throws SQLException {
        return getSingleData(conn, tabla, campos, condiciones, orden, null);
     }
    
    //-----------------------------------------------------------------------//
    /**
     * Funci\u00F3n que devuelve un mapa de strings con los resultados obtenidos al
     * consultarse mediante los par\u00E9metros indicados.
     * 
     * @param conn
     *            Objeto de tipo Connection que contiene la conexi\u00F3n a BD a
     *            utilizar.
     * @param tabla
     *            Nombre de la tabla a consultar.
     * @param campos
     *            Array de strings con los nombres de los campos a obtener de la
     *            consulta.
     * @param condiciones
     *            Listado de filtros o condiciones que se desean en la consulta.
     * @return Mapa de strings con los datos encontrados.
     * @throws SQLException
     */
    public static Map<String, String> getSingleFirstData(Connection conn, String tabla, String[] campos,
            List<Filtro> condiciones) throws SQLException {

        Map<String, String> datos = new HashMap<String, String>();
        String sql = armarQrySelect(tabla, campos, condiciones, null, null, 1, 1);

        Statement stm = null;
        ResultSet rst = null;
        try {
            stm = conn.createStatement();
            rst = stm.executeQuery(sql);
            if (rst.next()) {
                do {
                    datos.putAll(getHashMapsResults(campos, rst));
                    log.trace("valores:"+ rst.getString(1));
                    log.trace("valores:"+ rst.getString(2));
                } while (rst.next());
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(stm);
        }

        return datos;
    }
    
    /**
     * M\u00E9todo que mediante los par\u00E9metros indicados, arma un query de consulta
     * tipo SELECT. Con excepci\u00F3n de tabla y campos los par\u00E9metros pueden ser
     * nulos.
     * 
     * @param tabla
     *            Tabla a utilizar en la consulta.
     * @param campos
     *            Array de campos a obtener mediante el query.
     * @param condiciones
     *            Listado de condiciones que se deben verificar mediante WHERE,
     *            no se debe olvidar la condici\u00F3n que asocie ambas tablas para
     *            obtener resultados correctos.
     * @param groupBy
     *            Listado que indica los campos por los cuales se agrupar\u00E9n los
     *            resultados en la consulta.
     * @param orden
     *            Listado de objetos de tipo Order para la orientacion de
     *            resultados mediante ORDER BY.
     * @param min
     *            N\u00FAmero m\u00EDnimo del registro a obtener en la consulta.
     * @param max
     *            N\u00FAmero m\u00E9ximo del registro a obtener en la consulta.
     * @return Cadena de texto que integra todos los par\u00E9metros en un query tipo
     *         SELECT.
     */
    public static String armarQrySelect(String tabla, String[] campos, List<Filtro> condiciones, String[] groupBy,
            List<Order> orden, int min, int max) {
        String sql = "SELECT ";

        sql += getCampos(campos);
        sql += " FROM " + tabla;
        sql += getCondiciones(condiciones);
        sql += getGroupBy(groupBy);
        sql += getOrden(orden);

        if (min == max && min > 0) {
            sql = getRowNum(sql, min);
        } else {
            sql = getLimit(sql, min, max);
        }

        log.debug("Qry Get: " + sql);
        return sql;
    }
    
    /**
     * Funci\u00F3n que devuelve los datos encontrados utilizando
     * {@link #armarQrySelect} para generar la consulta de tipo SELECT, raz\u00F3n
     * por la cual solicita los mismos par\u00E9metros aniadiendo \u00FAnicamente la
     * conexi\u00F3n.
     * 
     * @param conn
     *            Objeto de tipo Connection que contiene la conexi\u00F3n a BD a
     *            utilizar.
     * @param tabla
     * @param campos
     * @param condiciones
     * @param groupBy
     * @param orden
     * @param min
     * @param max
     * @return Listado de HashMaps con los datos encontrados debidamente
     *         identificados.
     * @throws SQLException
     */
    public static List<Map<String, String>> getPaginatedData(Connection conn, String tabla, String[] campos,
            List<Filtro> condiciones, String[] groupBy, List<Order> orden, int min, int max) throws SQLException {
        List<Map<String, String>> resultados = new ArrayList<Map<String, String>>();
        String sql = armarQrySelect(tabla, campos, condiciones, groupBy, orden, min, max);
        Statement stm = null;
        ResultSet rst = null;

        try {
            stm = conn.createStatement();
            rst = stm.executeQuery(sql);
            if (rst.next()) {
                do {
                    Map<String, String> datos = new HashMap<String, String>();

                    datos.putAll(getHashMapsResults(campos, rst));

                    resultados.add(datos);
                } while (rst.next());
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(stm);
        }

        return resultados;
    }
    
    /**
     * Funci\u00F3n devuelve un mapa de strings en base a un listado de campos y un
     * set de resultados.
     * 
     * @param campos
     *            Array de campos de la consulta que gener\u00F3 el set de
     *            resultados. Pueden ser campos normales o con alias.
     * @param rst
     *            Set de resultados para agregar al mapa en su respectivo campo.
     * @return Mapa de string con los datos ordenados como (campo consulta,
     *         valor consulta).
     * @throws SQLException
     */
    private static Map<String, String> getHashMapsResults(String[] campos, ResultSet rst) throws SQLException {
        String alias = "";
        Map<String, String> datos = new HashMap<String, String>();

        for (int i = 0; i < campos.length; i++) {
            if (campos[i].contains(" AS ")) {
                alias = campos[i].split(" AS ")[1];
            } else {
                alias = campos[i];
            }
            datos.put(alias, rst.getString(i + 1));
        }

        return datos;
    }

    /**
     * Funci\u00F3n que convierte un array de strings en una cadena de texto.
     * 
     * @param campos
     *            Array de strings con el nombre de los campos a consultar.
     * @return Cadena separada por comas para ser usada en consultas.
     */
    public static String getCampos(String[] campos) {
        String sql = "";

        for (int i = 0; i < campos.length; i++) {
            sql += campos[i];
            if (i != campos.length - 1)
                sql += ", ";
        }
        
        return sql;
    }
    
    /**
     * Funci\u00F3n que convierte un listado de condiciones o filtros en una cadena
     * de texto.
     * 
     * @param condiciones
     *            Listado de condiciones o filtros a utilizar.
     * @return Cadena de texto que inicia con WHERE y las condiciones listas
     *         para utilizarse en una consulta.
     */
    public static String getCondiciones(List<Filtro> condiciones) {
        String condicionesAND = "";
        String condicionesOR = "";

        String sql = "";

        if (condiciones != null && condiciones.size() > 0) {
            sql += " WHERE ";
            for (int i = 0; i < condiciones.size(); i++) {
                String operadorLogico = condiciones.get(i).getComparacion();
                String where = condiciones.get(i).getField() + " " + condiciones.get(i).getOperator() + " "
                        + condiciones.get(i).getValue();

                if (i != 0) {
                    if (operadorLogico == null) {
                        condicionesAND += " " + Filtro.AND + " ";
                    } else if (operadorLogico.equals(Filtro.AND) && !condicionesAND.equals("")) {
                        condicionesAND += " " + condiciones.get(i).getComparacion() + " ";
                    } else if (operadorLogico.equals(Filtro.OR) && !condicionesOR.equals("")) {
                        condicionesOR += " " + condiciones.get(i).getComparacion() + " ";
                    }
                }

                if (condiciones.get(i).getComparacion() == null) {
                    condicionesAND += where;
                } else if (operadorLogico.equals(Filtro.AND)) {
                    condicionesAND += where;
                } else if (operadorLogico.equals(Filtro.OR)) {
                    condicionesOR += where;
                }
            }
            
            if (condicionesOR.equals("")) {
                sql += condicionesAND;
            } else if (condicionesAND.equals("")) {
                sql += condicionesOR;
            } else {
                sql += condicionesAND + " " + Filtro.AND + " (" + condicionesOR + ") ";
            }
        }

        return sql;
    }

    /**
     * Funci\u00F3n que convierte un listado de elementos de orden en una cadena de
     * texto.
     * 
     * @param orden
     *            Listado de objetos de tipo {@link Order} a utilizar.
     * @return Cadena de texto que inicia con ORDER BY y los elementos en que se
     *         ordenar\u00E9n los datos para utilizarse en una consulta.
     */
    public static String getOrden(List<Order> orden) {
        String sql = "";

        if (orden != null && orden.size() > 0) {
            sql += " ORDER BY ";
            for (int i = 0; i < orden.size(); i++) {
                sql += orden.get(i).getField() + " " + orden.get(i).getDir();
                if (i != orden.size() - 1) {
                    sql += ", ";
                }
            }
        }

        return sql;
    }

    /**
     * Funci\u00F3n que convierte un array de strings en una cadena de texto.
     * 
     * @param groupBy
     *            Array de strings con el nombre de los campos en los c\u00FAales se
     *            agrupar\u00E9n los resultados.
     * @return Cadena de texto que inicia con GROUP BY y los campos en que se
     *         agrupar\u00E9n los resultados listo para utilizarse en una consulta.
     */
    public static String getGroupBy(String[] groupBy) {
        String sql = "";

        if (groupBy != null && groupBy.length > 0) {
            sql += " GROUP BY ";
            for (int i = 0; i < groupBy.length; i++) {
                sql += groupBy[i];
                if (i != groupBy.length - 1) {
                    sql += ", ";
                }
            }
        }

        return sql;
    }

    public static String getLimit(String query, int minimo, int maximo) {
        String sql = "SELECT * FROM (SELECT /*+ FIRST_ROWS(n) */ LIMIT.*, ROWNUM rnum FROM (" + query + ") LIMIT";

        if (minimo > maximo) {
            return query;
        }

        if (maximo > 1) {
            sql += " WHERE ROWNUM <= " + maximo + ")";
        } else {
            sql += ")";
        }
        if (minimo <= 0) {
            minimo = 1;
        }
        sql += " WHERE rnum >= " + minimo;

        return sql;
    }

    public static String getRowNum(String query, int rownum) {
        String sql = "SELECT * FROM ("
                + "SELECT /*+ FIRST_ROWS(n) */ QRY.*, ROWNUM RNUM "
                + "FROM (" + query + ") QRY) WHERE RNUM = " + rownum;

        return sql;
    }

    public static String executeQueryOneRecord(Connection conn, String sql) throws SQLException {
        String resultado = "";
        log.debug("QryOneRecord: " + sql);
        Statement stm = null;
        ResultSet rst = null;

        try {
            stm = conn.createStatement();
            rst = stm.executeQuery(sql);

            if (rst.next()) {
                resultado = rst.getString(1);
            }
            log.trace("Valor: " + resultado);
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(stm);
        }

        return resultado;
    }
}

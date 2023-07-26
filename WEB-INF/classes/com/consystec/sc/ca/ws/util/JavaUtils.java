package com.consystec.sc.ca.ws.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;
/**
 * @author Marcos Alfaro - Consystec Corp. 2012
 *
 */
public class JavaUtils {

	private static final Logger log = Logger.getLogger(JavaUtils.class);


	private JavaUtils(){
		
	}
	public static String encriptaEnMD5(String stringAEncriptar){
		final char[] CONSTS_HEX = { '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f' };
		try {
			MessageDigest msgd = MessageDigest.getInstance("MD5");
			byte[] bytes = msgd.digest(stringAEncriptar.getBytes());
			StringBuilder strbCadenaMD5 = new StringBuilder(2 * bytes.length);
			for (int i = 0; i < bytes.length; i++) {
				int bajo = (int)(bytes[i] & 0x0f);
				int alto = (int)((bytes[i] & 0xf0) >> 4);
				strbCadenaMD5.append(CONSTS_HEX[alto]);
				strbCadenaMD5.append(CONSTS_HEX[bajo]);
			}
			return strbCadenaMD5.toString();
		} catch (NoSuchAlgorithmException e) {
			log.error(e,e);
			return null;
		}
	}


	/**Ejecuta una sentencia SQL de tipo select, y devuelve un listado List<> de objetos de tipo de la clase recibida.
	 * @param conn Conexi\u00F3n hacia la base de datos destino
	 * @param clase Tipo de objeto al cual se castear\u00E9 el listado generado de la consulta
	 * @param query String que contiene el sql a ejecutar.
	 * @param params Par\u00E9metros que se deben setear en el query antes de su ejecuci\u00F3n. Si el query no requiere de par\u00E9metros enviar NULL
	 * @return List<clase>
	 * @throws SQLException
	 */
	public static <T> List<T> executeSelect(Connection conn, Class<T> clase, String query, Object[] params) throws SQLException{
		List<T> ret = null;
		QueryRunner qry = new QueryRunner();

		BeanListHandler<T> blh = new BeanListHandler<T>(clase);
		if (params != null){
			ret =  qry.query(conn, query, blh, params);
		} else{
			ret =  qry.query(conn,  query , blh);		
		}
		return ret;
	}

	/**Ejecuta una sentencia SQL de tipo insert, update o delete, y devuelve la cantidad de registros insertados, modificados, o bien, eliminados.
	 * @param conn Conexi\u00F3n hacia la base de datos destino
	 * @param query String que contiene el sql a ejecutar.
	 * @param params Par\u00E9metros que se deben setear en el query antes de su ejecuci\u00F3n. Si el query no requiere de par\u00E9metros enviar NULL
	 * @return int
	 * @throws SQLException
	 */
	public static int executeUpdate(Connection conn, String query, Object[] params) throws SQLException{
		int i=0;
		QueryRunner qry = new QueryRunner();

		if (params!=null){
			i=qry.update(conn, query, params);
		} else {
			i=qry.update(conn, query);
		}
		return i;
	}

	/** Retorna un nuevo string sql que contiene el query enviado como parametro, con el agregado de delimitaci\u00F3n por rownum para pagineo.
	 * @param query cadena de texto sql que se desea delimitar
	 * @param minimo
	 * @param maximo
	 * @return String nuevo string sql
	 */
	public static String getQueryDelimitado(String query,int minimo, int maximo){
		return "select * from(" +
				"	select b.*,rownum r " +
				" 	  from(" +query+") b " +
				"	 where rownum <= " +maximo+
				") where r >  "+minimo;
	}


	/** Ejecuta una sentencia sql de la cual se espera obtener un unico campo en un solo registros. 
	 *  El object retornado podr\u00E9 ser casteado al objeto deseado.
	 *  El programador debe saber que tipo de dato est\u00E9 obteniendo, de lo contrario se recomienda controlar ClassCastException
	 * @param conn Conexi\u00F3n a la base de datos
	 * @param query String sql que se ejecutar\u00E9. Por lo regular deber\u00E9 ser de tipo "select 'lo_que_sea' from dual", o bien "count(*)"
	 * @return object primer campo del primer registro generado por la consulta. Casteable al tipo respectivo seg\u00FAn la base de datos.
	 * @throws SQLException
	 */
	public static Object getOneFieldFromDual(Connection conn, String query) throws SQLException{
		PreparedStatement prepare =null;
		ResultSet result = null;
		Object obj = null;
		try{
			prepare = conn.prepareStatement(query);
			result = prepare.executeQuery();
			if(result.next()){
				obj = result.getObject(1);
			}
		}finally{
			closeStatements(prepare, result);
		}
		return obj;
	}

	/** Funci\u00F3n que cierra el PreparedStatement y el ResultSet, cuando los usamos en una consulta SQL.
	 * @param prepare PreparedStatement
	 * @param result ResultSet
	 */
	public static void closeStatements(PreparedStatement prepare, ResultSet result){
		try {
			if (prepare!=null){
				prepare.close();
				
			} 
		}catch (SQLException e) {
			log.error(e,e);
		}
		try {
			if (result!=null){
				result.close();
				
			} 
		}catch (SQLException e) {
			log.error(e,e);
		}
	}



	/***
	 * Valida la direcci\u00F3n de correo electronico,
	 * @param correo
	 * @return true :correo valido/false : correo invalido
	 */
	public static boolean isEmailValido(String correo) {  
		//creamos una copia de la cadena para que no nos afecte despues de las conversiones
		String copiaCorreo = correo;
		int indice = copiaCorreo.indexOf("@"); 
		if(indice==-1){//si no encuentra la arroba en la cadena retornamos false
			return false;
		}
		copiaCorreo = copiaCorreo.substring(indice+1, correo.length());
		indice = copiaCorreo.indexOf("@");
		if(indice!=-1){//si encuentra una segunda arroba retornamos false
			return false;
		}
		Pattern pat = null;        
		Matcher mat = null;
		pat = Pattern.compile("^([0-9a-zA-Z]([_.w-]*[0-9a-zA-Z])*@([0-9a-zA-Z]{1,9}.)+[a-zA-Z]{2,3})$");        
		mat = pat.matcher(correo);        
		if (mat.find()) {     
			return true;        
		}else{            
			return false;        
		}            
	}


	public static String getNodeValueFromXmlFormatoSalida(String entrada, String etiqueta) {
		if (entrada==null) return "";
		try {
			int inicio,fin = 0;
			inicio = entrada.indexOf("<"+etiqueta+">");
			fin = entrada.indexOf("</"+etiqueta+">");
			return entrada.substring(inicio+etiqueta.length()+2,fin);
		}
		catch (Exception e) {
			log.error(e,e);
			return "";
		}
	}


	
	public static byte[] convertPDFtoByte(File documentPdf) throws IOException {
        InputStream inputStream = new FileInputStream(documentPdf);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        return convertPDFtoByte(inputStream,byteArrayOutputStream);
    }

    private static byte[] convertPDFtoByte(InputStream inputStream,ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        byte[] bytes = new byte[10048];
        for(int read; (read = inputStream.read(bytes)) != -1;) {
            byteArrayOutputStream.write(bytes, 0, read);
        }
        byte[] bytesResult = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        inputStream.close();
        return bytesResult;
    }
    
    public static String quitarCaracterEspecial(String cadena) {
    	String respuesta = "";
    	
    	try {
    		if (cadena != null && !"".equals(cadena.trim())) {
    			respuesta = cadena;
    			respuesta = respuesta.replaceAll("\u00E9", "a");
    			respuesta = respuesta.replaceAll("\u00E9", "e");
    			respuesta = respuesta.replaceAll("\u00ED", "i");
    			respuesta = respuesta.replaceAll("\u00F3", "o");
    			respuesta = respuesta.replaceAll("\u00FA", "u");
    			respuesta = respuesta.replaceAll("ñ", "n");
    		}
    	} catch (Exception e) {
    		log.error(e,e);
    	}
    	
    	return respuesta;
    }
    
}

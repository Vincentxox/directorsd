package com.consystec.sc.sv.ws.metodos;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.dbutils.DbUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.consystec.sc.ca.ws.input.consultas.InputConsultaSaldoPayment;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.consultas.OutputConsultaSaldoPayment;
import com.consystec.sc.sv.ws.orm.Jornada;
import com.consystec.sc.sv.ws.orm.VendedorDTS;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class CtrlSaldoPayment extends ControladorBase {

    public Respuesta validarDatos(InputConsultaSaldoPayment objDatos) throws SQLException {
        Respuesta objRespuesta = null;
        String metodo = "validarDatos";
        String estadoAlta = "";
        int longNumero = 0;
        boolean flag = false;
        Connection conn =null;
        
        try{
        conn = getConnRegional();
        estadoAlta = UtileriasJava.getConfig(conn, Conf.GRUPO_ESTADOS, Conf.ESTADO_ALTA, objDatos.getCodArea());
        longNumero = new Integer(UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.LONGITUD_TELEFONO, objDatos.getCodArea()));

        if (objDatos.getUsuario() == null || "".equals(objDatos.getUsuario().trim())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_USER_NULO_2, this.getClass().toString(), null, metodo, null, objDatos.getCodArea());
        }

        if (objDatos.getNumRecarga() == null || "".equals(objDatos.getNumRecarga().trim())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_NUMRECARGA_VACIO_721, null, this.getClass().toString(),
                    metodo, "", objDatos.getCodArea());
            flag = true;
        } else if (!isNumeric(objDatos.getNumRecarga())) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_NUMRECARGA_722, null, this.getClass().toString(), metodo,
                    null, objDatos.getCodArea());

        } else if (objDatos.getNumRecarga().length() != longNumero) {
            objRespuesta = getMensaje(Conf_Mensajes.MSJ_INPUT_NUMRECARGA_LONG_723, null, this.getClass().toString(),
                    metodo, null, objDatos.getCodArea());
        }

        if (!flag) {
            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(new Filtro("UPPER(" + VendedorDTS.CAMPO_ESTADO + ")", Filtro.EQ, "'" + estadoAlta.toUpperCase() + "'"));
            condiciones.add(new Filtro("UPPER(" + VendedorDTS.CAMPO_NUM_RECARGA + ")", Filtro.EQ, objDatos.getNumRecarga()));
            String vendedor = UtileriasBD.getOneRecord(conn, VendedorDTS.CAMPO_VENDEDOR, VendedorDTS.N_TABLA, condiciones);

            if (vendedor == null || "".equals(vendedor)) {
                objRespuesta = getMensaje(Conf_Mensajes.MSJ_VENDEDOR_NUMRECARGA_INVALIDOS_522, null,
                        this.getClass().toString(), metodo, null, objDatos.getCodArea());
            }
        }
        }finally{
        	DbUtils.closeQuietly(conn);
        }
        return objRespuesta;
    }
    
    public String getSaldoPayment(Connection conn, String idJornada, String idVendedor, int metodo, String codArea) throws Exception {
        InputConsultaSaldoPayment input = new InputConsultaSaldoPayment();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String saldo = null;
        String numero = "";
        String pin = "";
        
            String sql = "SELECT NUM_RECARGA, PIN FROM TC_SC_VEND_DTS WHERE VENDEDOR = ?" ;
            log.debug("Qry datos saldo: " + sql);
            try{
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setBigDecimal(1, new BigDecimal(idVendedor));
	            rst = pstmt.executeQuery();
	          
	
	            if (rst.next()) {
	                do {
	                    numero = UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "NUM_RECARGA");
	                    pin = UtileriasJava.getRstValue(rst, Conf.TIPO_TEXTO, "PIN");
	                } while (rst.next());
	            }	
            }finally{
            	   DbUtils.closeQuietly(pstmt);
                   DbUtils.closeQuietly(rst);
            }
            
            input.setNumRecarga(numero);
            input.setPin(pin);
            input.setCodArea(codArea);

            saldo = getSaldoPayment(input).getSaldoPayment();

            if (metodo == Conf.METODO_POST) {
                sql = "UPDATE" + getParticion(Jornada.N_TABLA, Conf.PARTITION, "", codArea)
                    + "SET saldo_payment = ? WHERE TCSCJORNADAVENID = ?";
                log.debug("Qry datos saldo: " + sql);

                try {   
	                pstmt = conn.prepareStatement(sql);
	                if ("0".equals(saldo)){
	                	pstmt.setBigDecimal(1, BigDecimal.ZERO);
	                }else {
	                	pstmt.setBigDecimal(1, new BigDecimal(saldo));
	                }
	                pstmt.setBigDecimal(2, new BigDecimal(idJornada));
	                int i = pstmt.executeUpdate();
	                if (i < 1) {
	                    log.error("error al actualizar jornada");
	                    throw new SQLException("Inconveniente al actualizar saldo payment de jornada");
	                }
                } finally {
                    DbUtils.closeQuietly(pstmt);
                    DbUtils.closeQuietly(rst);
                }
            }

      

        return saldo;
    }
    
    public OutputConsultaSaldoPayment getSaldoPayment(InputConsultaSaldoPayment input) {
        OutputConsultaSaldoPayment objReturn = new OutputConsultaSaldoPayment();
        Connection conn = null;
        String wsURL = "";
        try {
            conn = getConnRegional();
            getIdPais(conn, input.getCodArea());
            wsURL = UtileriasJava.getConfig(conn, Conf.GRUPO_CONFIG_SIDRA, Conf.URL_SALDO_PAYMENT, input.getCodArea());

            if ("NULL".equalsIgnoreCase(wsURL)) {
                objReturn.setSaldoPayment("0");
                return objReturn;
            }

            // Code to make a webservice HTTP request
            String responseString = "";
            String outputString = "";
            URL url = new URL(wsURL);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            String xmlInput =
            "<USSDRequest> " +
                "<MSISDN>" + input.getNumRecarga() + "</MSISDN> " +
                "<VoucherPin>*" + input.getNumRecarga() + "*" + input.getPin() + "</VoucherPin> " +
                "<DialogueId>122</DialogueId> " +
                "<ProviderId>233</ProviderId> " +
                "<ServiceCode>444</ServiceCode> " +
                "<Response></Response> " +
            "</USSDRequest>";

            byte[] buffer;
            buffer = xmlInput.getBytes();
            bout.write(buffer);
            byte[] b = bout.toByteArray();
            // Set the appropriate HTTP parameters.
            httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
            httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpConn.setRequestProperty("SOAPAction", wsURL);
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            OutputStream out = httpConn.getOutputStream();
            out.write(b);
            out.close();
            // Ready with sending the request.

            // Read the response.
            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
            BufferedReader in = new BufferedReader(isr);

            // Write the SOAP message response to a String.
            while ((responseString = in.readLine()) != null) {
                outputString = outputString + responseString;
            }
            // Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
            Document document = parseXmlFile(outputString);
            NodeList nodeLst = document.getElementsByTagName("Response");
            String response = nodeLst.item(0).getTextContent();
            log.trace("response: " + response);


            if (response != null && !response.equals("") && isDecimal(response) && response.contains(".")) {
                objReturn.setSaldoPayment(response);
            } else {
                objReturn.setSaldoPayment("0");
            }
        } catch (Exception e) {
            log.error(e, e);
            objReturn.setSaldoPayment("0");
        }finally{
        	DbUtils.closeQuietly(conn);
        }

        return objReturn;
    }

    // format the XML
    public String formatXML(String unformattedXml) {
        try {
            Document document = parseXmlFile(unformattedXml);
            OutputFormat format = new OutputFormat(document);
            format.setIndenting(true);
            format.setIndent(3);
            format.setOmitXMLDeclaration(true);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            return out.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
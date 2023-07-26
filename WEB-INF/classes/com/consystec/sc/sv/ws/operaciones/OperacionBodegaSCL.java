package com.consystec.sc.sv.ws.operaciones;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




import org.apache.commons.dbutils.DbUtils;

import com.consystec.sc.ca.ws.output.bogegas.BodegaSCL;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.FSUtil;
import com.consystec.sc.sv.ws.util.UtileriasJava;
import com.telefonica.globalintegration.header.HeaderInType;
import com.telefonica.globalintegration.header.HeaderOutType;
import com.telefonica.globalintegration.services.getwarehouse.v1.CustomCatalogItemType;
import com.telefonica.globalintegration.services.getwarehouse.v1.GetWarehouseRequestType;
import com.telefonica.globalintegration.services.getwarehouse.v1.GetWarehouseResponseType;
import com.telefonica.globalintegration.services.soap.getwarehouse.v1.GetWarehouseBindingQSService;
import com.telefonica.globalintegration.services.soap.getwarehouse.v1.GetWarehouseV1;
import com.telefonica.globalintegration.services.soap.getwarehouse.v1.MessageFault;


/**
 * @author SBarrios Consystec 2015
 */

public class OperacionBodegaSCL extends ControladorBase {
    // TODO cambiar funciones de bodega por pais

    /**
     * M\u00E9todo para obtener las bodegas disponibles de SCL.
     * 
     * @param conn
     * @param tipBodega
     * @return
     * @throws SQLException
     */
    /* PANAM\u00E9, NICARAGUA y CR*/
	public static String getQuery( String tipBodega) {
		 String query = "SELECT COD_BODEGA, DES_BODEGA"
	                + " FROM AL_BODEGAS"
	                + " WHERE TIP_BODEGA IN (" + tipBodega + ")"
	                + " ORDER BY DES_BODEGA ASC";
		 return query;
	}
    public static List<BodegaSCL> getBodegaSCL1(Connection conn, String tipBodega) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        List<BodegaSCL> lstBodegas = new ArrayList<BodegaSCL>();
        BodegaSCL objBodega = new BodegaSCL();

        String query = getQuery(  tipBodega) ;

        log.trace("query bodegasSCL: " + query);
       try{
        pstmt = conn.prepareStatement(query);
        rst = pstmt.executeQuery();

        while (rst.next()) {
            objBodega = new BodegaSCL();
            objBodega.setIdBodega("");
            objBodega.setCodBodega(rst.getString("COD_BODEGA"));
            objBodega.setNombreBodega(rst.getString("DES_BODEGA"));

            lstBodegas.add(objBodega);
        }
       }finally{
    	   DbUtils.closeQuietly(pstmt);
    	   DbUtils.closeQuietly(rst);
       }

        return lstBodegas;
    }


    /**
     * Obteniendo bodegas implementando servicios de FS
     * @param conn
     * @param tipBodega
     * @return
     * @throws SQLException 
     * @throws MalformedURLException 
     * @throws MessageFault 
     */
    public static List<BodegaSCL>  getBodegaSCL(Connection conn, String usuario, String area) throws MalformedURLException, SQLException, MessageFault {
    	
    	List<BodegaSCL> lstBodegas = new ArrayList<BodegaSCL>();
    	BodegaSCL bodega =null;
    	
    	
    	 URL endpoint = new URL(UtileriasJava.getConfig(conn, Conf.GRUPO_URL_FS_WS, Conf.FS_WS_GETBODEGAS, area));
         GetWarehouseBindingQSService ws = new GetWarehouseBindingQSService(endpoint);
         GetWarehouseV1 w = ws.getGetWarehouseBindingQSPort();
    	
         HeaderInType header ;       
         
         //Armando body de input
         GetWarehouseRequestType inputGetBod = new GetWarehouseRequestType();
         CustomCatalogItemType itemCatalogos = new CustomCatalogItemType();
         itemCatalogos.setDescriptionCatalog(null);
         itemCatalogos.setIDCatalog(null);
         inputGetBod.setCatalogItem(null);
         
         header=FSUtil.instanciaHeader(usuario, area);
         javax.xml.ws.Holder<HeaderOutType> headerResp = new javax.xml.ws.Holder<HeaderOutType>();
         
         GetWarehouseResponseType respuestaBodegas=w.getWarehouse(inputGetBod,header,headerResp );
          
         
         //obteniendo bodegas de FS
         if (!(respuestaBodegas==null)&&  (!respuestaBodegas.getCatalogListItem().isEmpty() && respuestaBodegas.getCatalogListItem().size()>0)){
        		 for (int a=0; a<respuestaBodegas.getCatalogListItem().size();a++){
        			 
        			 for (int b=0; b<respuestaBodegas.getCatalogListItem().get(a).getCatalogItem().size(); b++)
        			 {
        				 bodega= new BodegaSCL();
        				 bodega.setIdBodega("");
        				 bodega.setCodBodega(respuestaBodegas.getCatalogListItem().get(a).getCatalogItem().get(b).getIDCatalog());
        				 bodega.setNombreBodega(respuestaBodegas.getCatalogListItem().get(a).getCatalogItem().get(b).getDescriptionCatalog());
        				 lstBodegas.add(bodega);
        			 }
        		 }
         }
         

         
         
    	return lstBodegas;
    } 
}

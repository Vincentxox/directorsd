package com.consystec.sc.sv.ws.operaciones;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.input.file.InputCargaFile;
import com.consystec.sc.ca.ws.orm.Respuesta;
import com.consystec.sc.ca.ws.output.file.OutputImagen;
import com.consystec.sc.sv.ws.orm.ImgPDV;
import com.consystec.sc.sv.ws.util.Conf;
import com.consystec.sc.sv.ws.util.Conf_Mensajes;
import com.consystec.sc.sv.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.util.CurrentClassGetter;
import com.consystec.sc.sv.ws.util.Filtro;
import com.consystec.sc.sv.ws.util.JavaUtils;
import com.consystec.sc.sv.ws.util.UtileriasBD;
import com.consystec.sc.sv.ws.util.UtileriasJava;

public class OperacionCargaFile extends ControladorBase{
    private static final Logger log = Logger.getLogger(OperacionCargaFile.class);

    /**
     * M\u00E9todo utilizado para subir la imagen de un punto de venta
     * @param idVisita 
     * @param observaciones 
     * 
     * @return
     * @throws SQLException
     */
    public static BigDecimal saveImagen(Connection conn, BigDecimal idPDV, byte[] imagen, String nombreArchivo,
            String extension, String usuario, String idVisita, String observaciones, BigDecimal idPais) throws SQLException {
        BigDecimal idImgPDV = JavaUtils.getSecuencia(conn, ImgPDV.SEQUENCE);
        PreparedStatement pstmt = null;

        
            String query = "INSERT INTO TC_SC_IMG_PDV ("
                + "tcscimgpdvid, "
                + "tcscpuntoventaid, "
                + "tcscvisitaid, "
                + "archivo, "
                + "nombre_archivo, "
                + "extension_archivo, "
                + "observaciones, "
                + "creado_el, "
                + "creado_por, "
            + "tcsccatpaisid) VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?)";

            log.debug("QryInsertImgPdv: " + query);
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, idImgPDV);
            pstmt.setBigDecimal(2, idPDV);
            pstmt.setString(3, idVisita);
            pstmt.setBytes(4, imagen);
            pstmt.setString(5, nombreArchivo);
            pstmt.setString(6, extension);
            pstmt.setString(7, observaciones);
            pstmt.setString(8, usuario);
            pstmt.setBigDecimal(9, idPais);

            int ret = pstmt.executeUpdate();

            if (ret > 0) {
                return idImgPDV;
            } else {
                return null;
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }

    public static BigDecimal updImagen(Connection conn, BigDecimal idImgPDV, BigDecimal idPDV,  byte[] imagen, String nombreArchivo,
            String extension, String usuario, String codArea, BigDecimal idPais) throws SQLException {
        PreparedStatement pstmt = null;

       
            String query = "UPDATE " + getParticion(ImgPDV.N_TABLA, Conf.PARTITION, "", codArea) + " SET "
                + "archivo = ?, "
                + "nombre_archivo = ?, "
                + "extension_archivo = ?, "
                + "modificado_por = ?, "
                + "modificado_el = SYSDATE "
            + "WHERE tcscimgpdvid = ? "
                + "AND tcscpuntoventaid = ? "
                + "AND tcsccatpaisid = ?";

            log.debug("QryUpdateImgPdv: " + query);
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setBytes(1, imagen);
            pstmt.setString(2, nombreArchivo);
            pstmt.setString(3, extension);
            pstmt.setString(4, usuario);
            pstmt.setBigDecimal(5, idImgPDV);
            pstmt.setBigDecimal(6, idPDV);
            pstmt.setBigDecimal(7, idPais);

            int ret = pstmt.executeUpdate();

            if (ret > 0) {
                return idImgPDV;
            } else {
                return null;
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }

    /**
     * Obtener imagen de pdv
     * 
     * @throws SQLException
     */
    public static OutputImagen getImagenPDV(Connection conn, String idImgPDV, String codArea, BigDecimal idPais) throws SQLException {
        String nombreMetodo = "getImagenPDV";
        String nombreClase = new CurrentClassGetter().getClassName();
        OutputImagen output = new OutputImagen();
        Respuesta respuesta = new Respuesta();
        List<InputCargaFile> list = new ArrayList<InputCargaFile>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        try {
            String campos[] = {
                ImgPDV.CAMPO_TCSCIMGPDVID,
                ImgPDV.CAMPO_TCSCPUNTOVENTAID,
                ImgPDV.CAMPO_ARCHIVO,
                ImgPDV.CAMPO_NOMBRE_ARCHIVO,
                ImgPDV.CAMPO_EXTENSION_ARCHIVO,
                ImgPDV.CAMPO_CREADO_EL,
                ImgPDV.CAMPO_CREADO_POR
            };

            List<Filtro> condiciones = new ArrayList<Filtro>();
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ImgPDV.CAMPO_TCSCIMGPDVID, idImgPDV));
            condiciones.add(UtileriasJava.setCondicion(Conf.FILTRO_ID_NUMERICO_AND, ImgPDV.CAMPO_TCSCCATPAISID, idPais.toString()));

            String sql = UtileriasBD.armarQuerySelect(getParticion(ImgPDV.N_TABLA, Conf.PARTITION, "",codArea), campos, condiciones, null);

            pstmt = conn.prepareStatement(sql);
            rst = pstmt.executeQuery();

            if (rst != null) {
                if (!rst.next()) {
                    respuesta = new ControladorBase().getMensaje(Conf_Mensajes.OK_CARGA_IMAGENES_21, null, nombreClase,
                            nombreMetodo, null,codArea);

                    output = new OutputImagen();
                    output.setRespuesta(respuesta);
                } else {
                    respuesta = new Respuesta();
                    respuesta=new ControladorBase()
                            .getMensaje(Conf_Mensajes.MSJ_RECURSOS_OBTENIDOS, null, nombreClase, nombreMetodo, null,codArea);

                    do {
                        InputCargaFile item = new InputCargaFile();
                        item.setIdImgPDV(rst.getString(ImgPDV.CAMPO_TCSCIMGPDVID));
                        item.setIdPDV(rst.getString(ImgPDV.CAMPO_TCSCPUNTOVENTAID));

                        if (rst.getBlob(ImgPDV.CAMPO_ARCHIVO) != null) {
                            Blob bin = rst.getBlob(ImgPDV.CAMPO_ARCHIVO);
                            int blobLength = (int) bin.length();
                            byte[] blobAsBytes = bin.getBytes(1, blobLength);
                            String img = new sun.misc.BASE64Encoder().encode(blobAsBytes);
                            item.setArchivo(img);
                        } else {
                            item.setArchivo(null);
                        }

                        item.setNombreArchivo(rst.getString(ImgPDV.CAMPO_NOMBRE_ARCHIVO));
                        item.setExtension(rst.getString(ImgPDV.CAMPO_EXTENSION_ARCHIVO));

                        item.setCreado_el(UtileriasJava.formatStringDate(rst.getString(ImgPDV.CAMPO_CREADO_EL)));
                        item.setCreado_por(rst.getString(ImgPDV.CAMPO_CREADO_POR));

                        list.add(item);
                    } while (rst.next());

                    output = new OutputImagen();
                    output.setRespuesta(respuesta);
                    output.setImagen(list);
                }
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }
        return output;
    }

    /**
     * M\u00E9todo utilizado para eliminar una imagen de un punto de venta
     * 
     * @return
     * @throws SQLException
     */
    public static boolean delImagen(Connection conn, BigDecimal idImgPDV, String codArea, BigDecimal idPais) throws SQLException {
        PreparedStatement pstmt = null;
       
            String query = "DELETE FROM " + getParticion(ImgPDV.N_TABLA, Conf.PARTITION, "",codArea)
                + " WHERE tcscimgpdvid = ?"
                + " AND tcsccatpaisid = ?";

            log.debug("QryDelImgPdv: " + query);
        try { 
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, idImgPDV);
            pstmt.setBigDecimal(2, idPais);

            int ret = pstmt.executeUpdate();
            if (ret > 0) {
                return true;
            } else {
                return false;
            }
        } finally {
            DbUtils.closeQuietly(pstmt);
        }
    }
    
    public static BigDecimal getIdImgPDV(Connection conn, String idPDV,  String codArea, BigDecimal idPais) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        BigDecimal idImgPdv = null;

        
            String query = "SELECT * FROM (SELECT tcscimgpdvid" 
                    + " FROM " + getParticion(ImgPDV.N_TABLA, Conf.PARTITION, "", codArea)
                    + " WHERE tcsccatpaisid = ?"
                    + " AND tcscpuntoventaid = ?"
                    + " AND tcscvisitaid IS NULL"
                    + " ORDER BY tcscimgpdvid ASC) WHERE ROWNUM = 1 ";

            log.debug("QryIdImgPDV: " + query);
         try {
            pstmt = conn.prepareStatement(query);
            pstmt.setBigDecimal(1, idPais);
            pstmt.setString(2, idPDV);

            rst = pstmt.executeQuery();

            if (rst.next()) {
                idImgPdv = rst.getBigDecimal(1);
            }
        } finally {
            DbUtils.closeQuietly(rst);
            DbUtils.closeQuietly(pstmt);
        }

        return idImgPdv;
    }
}

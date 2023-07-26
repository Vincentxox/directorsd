package com.consystec.sc.ca.ws.metodos;

import java.io.File;
import java.sql.Connection;
import java.util.Map;

import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.util.ControladorBase;
import com.consystec.sc.sv.ws.operaciones.OperacionObtenerMPOS;
import com.consystec.sc.sv.ws.util.Sftp;
import com.ericsson.sc.ca.ws.dto.MposDTO;

import com.ericsson.sdr.dao.ConfigurationDAO;

public class MPOS extends ControladorBase{
	
	private static final Logger log = Logger.getLogger(MPOS.class);
	
	public  File obtenerAPKMPOS() {
		
		OperacionObtenerMPOS getMPOS = new OperacionObtenerMPOS();
		
			try (Connection conn = getConnLocal()){
				
				MposDTO apk= getMPOS.getDataMPOS(conn);
				String ruta = apk.getUbicacion();
				String nombre = apk.getNombre();
				
				
				
				Map<String, String> configuracionSFTP = ConfigurationDAO.getConfigGroup(conn,"GETMPOS",0);
				
				
				File archivo = new File(getPathAPK(configuracionSFTP.get("PATH_APK_LOCAL"))+nombre );
				if(!archivo.exists()) {
					Sftp sftp = new Sftp();
					
					sftp.conexion(configuracionSFTP.get("USUARIO_SFTP"), configuracionSFTP.get("IP_SFTP"), Integer.parseInt(configuracionSFTP.get("PUERTO_SFTP")), configuracionSFTP.get("PASS_SFTP"));
					sftp.getArchive(ruta+nombre, getPathAPK(configuracionSFTP.get("PATH_APK_LOCAL")));
					
					archivo = new File(getPathAPK(configuracionSFTP.get("PATH_APK_LOCAL"))+nombre );
					sftp.disconnect();
					
				}
				
	
				
				return archivo;
				
			} catch (Exception e) {
				log.error("Error al obtener el APK de MPOS",e);
			}
			
		
		return null;
	}

}

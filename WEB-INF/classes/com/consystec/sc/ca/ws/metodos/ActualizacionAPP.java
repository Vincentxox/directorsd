package com.consystec.sc.ca.ws.metodos;

import java.io.File;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;


import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.orm.Respuesta;

import com.consystec.sc.ca.ws.util.Conf_Mensajes;
import com.consystec.sc.ca.ws.util.ControladorBase;

import com.consystec.sc.sv.ws.operaciones.OperacionActualizacionAPK;
import com.consystec.sc.sv.ws.util.Sftp;
import com.ericsson.sc.ca.ws.dto.VersionApkDTO;
import com.ericsson.sc.ca.ws.input.actualizacion.movil.InputConsultaActualizacion;
import com.ericsson.sc.ca.ws.output.actualizacion.movil.OutputDispositivoActualizado;
import com.ericsson.sdr.dao.ConfigurationDAO;

public class ActualizacionAPP extends ControladorBase{
	private static final Logger log = Logger.getLogger(ActualizacionAPP.class);
	
	
	public OutputDispositivoActualizado existeActualizacion(InputConsultaActualizacion objDatos) {
		String metodo = "existeActualizacion";
		OutputDispositivoActualizado output = new OutputDispositivoActualizado();
		OperacionActualizacionAPK operacionActualizacion = new OperacionActualizacionAPK();
		Respuesta respuesta = null;
			try (Connection conn = getConnLocal()){
				VersionApkDTO apk= operacionActualizacion.getLastVersionAPK(conn);
				System.out.println("ACTUALIZACION "+apk.getActualizar());
				if(apk.getVersion().equalsIgnoreCase(objDatos.getVersion()) || apk.getActualizar() == 0) {
					respuesta = getMensaje(Conf_Mensajes.OK_ACTUALIZACION2, null, this.getClass().toString(), metodo, null,
		                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
					
				}else {
					output.setNombre(apk.getNombre());
					respuesta = getMensaje(Conf_Mensajes.MSJ_DISPOSITIVO_ACTUALIZACION, null, this.getClass().toString(), metodo, null,
		                    Conf_Mensajes.ORIGEN_SERVICIOS_OP);
				}
				
			} catch (SQLException e) {
				log.error("Error en la consulta de nueva actualizacion",e);
				 respuesta = getMensaje(e.getErrorCode(), e.getMessage(), this.getClass().toString(), metodo, null,
	                        Conf_Mensajes.ORIGEN_SERVICIOS_OP);
			}	
		
		output.setRespuesta(respuesta);
		return output;
	}
	
	public  File obtenerActualizacion() {
	
		OperacionActualizacionAPK operacionActualizacion = new OperacionActualizacionAPK();
		
			try (Connection conn = getConnLocal()){
				
				VersionApkDTO apk= operacionActualizacion.getLastVersionAPK(conn);
				String ruta = apk.getUbicacion();
				String nombre = apk.getNombre();
				
				
				
				Map<String, String> configuracionSFTP = ConfigurationDAO.getConfigGroup(conn,"ACTUALIZACIONAPK",0);
				
				
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
				log.error("Error al obtener la ultima version del APP",e);
			}
			
		
		return null;
	}
	


}

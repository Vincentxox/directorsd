package com.consystec.sc.sv.ws.util;


import org.apache.log4j.Logger;

import com.consystec.sc.ca.ws.util.ControladorBase;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Sftp {
	
	private static final Logger log = Logger.getLogger(ControladorBase.class);
	
	private Session session;

	public void conexion(String usuario, String ip, int puerto, String pass)
			throws JSchException, IllegalAccessException {
		log.debug("INICIA CONEXION SFTP");
		if (this.session == null || !this.session.isConnected()) {
			JSch jsch = new JSch();
			this.session = jsch.getSession(usuario, ip, puerto);
			this.session.setPassword(pass);
			this.session.setConfig("StrictHostKeyChecking", "no");
			this.session.connect();
		} else {
			throw new IllegalAccessException("SESION SFTP YA INICIADA  ");
		}
		log.debug("CONEXION SFTP ESTABLECIDA " );

	}
	
	public final void disconnect() {
		if (this.session != null && this.session.isConnected()) {
			this.session.disconnect();
		}
		log.debug("FINALIZA SESION SFTP");

	}

	public boolean getArchive(String directorioOrigen, String directorioDestino) {
		try {
			ChannelSftp channelSftp = (ChannelSftp) this.session.openChannel("sftp");
			channelSftp.connect();
			channelSftp.get(directorioOrigen, directorioDestino);
			channelSftp.exit();
			channelSftp.disconnect();
		} catch (Exception e) {
			log.error("ERROR AL INTENTAR BAJAR EL ARCHIVO: " + directorioDestino , e);
			return false;
		}
		return true;

	}	

}

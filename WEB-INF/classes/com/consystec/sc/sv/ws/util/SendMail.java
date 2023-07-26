package com.consystec.sc.sv.ws.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;

public class SendMail {
	 private SendMail(){}
	
    private static final Logger log = Logger.getLogger(SendMail.class);

    public static String sendMail(String sender, String host, String port, String correo, String asunto,
            String cuerpo) {
        String resp;

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.socketFactory.port", port);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(sender));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(correo));

            // Set Subject: header field
            message.setSubject(asunto);

            // Send the actual HTML message, as big as you like
            message.setContent(cuerpo, "text/html; charset=UTF-8");

            // Send message
            Transport.send(message);
            log.trace("mail sent");
            return "OK";
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            resp = e.getMessage();
        }
        return resp;
    }
}
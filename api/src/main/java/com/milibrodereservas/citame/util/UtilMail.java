package com.milibrodereservas.citame.util;

import com.milibrodereservas.citame.global.Base;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;

// dependencias necesarias (pom.xml):
// https://mvnrepository.com/artifact/org.eclipse.angus/angus-mail
//     (incluye https://mvnrepository.com/artifact/jakarta.mail/jakarta.mail-api)

import java.util.Properties;

@Component
public class UtilMail extends Base {
	public void sendEmail (String destinatario, String asunto, String cuerpo) {
    	// Durante las pruebas redirigir todos los correos a sebas_serrano@hotmail.com
    	final String forward = env.getProperty("ForwardEmailsDebug");
    	if (forward != null) {
    		cuerpo = cuerpo + "<p>Mensaje original dirigido a " + destinatario + "</p>";
    		destinatario = forward;
    	}
    	
    	// token Gmail buxs wvdi cgml pyzu (aplicacion citame)
        //La dirección de correo de envío
        final String remitente = env.getProperty("RemitenteEmail");
        //La clave de aplicación obtenida para Gmail. Hay que habilitar autenticación en 2 pasos
        final String claveemail = env.getProperty("TokenGmail");

        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", claveemail);    //La clave de la cuenta
        props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
        props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");  // para forzar que confie en gmail
        props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
        	message.setFrom(new InternetAddress(remitente));
        	message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));   //Se podrían añadir varios de la misma manera
        	message.setSubject(asunto, "UTF-8");
        	message.setContent(cuerpo, "text/html; charset=UTF-8");
        	Transport transport = session.getTransport("smtp");
        	transport.connect("smtp.gmail.com", remitente, claveemail);
        	transport.sendMessage(message, message.getAllRecipients());
        	transport.close();
        	logger.info("Enviado email a {} asunto {}", destinatario, asunto);
        }
        catch (MessagingException e) {
        	logger.error("Error en envío email", e);
        }
    }
}

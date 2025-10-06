package org.example.ProyectoGrpc.servicio.implementacion;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Async
    public void enviarPassword(String toEmail, String password) {
        final String fromEmail = "magosh90@gmail.com";  
        final String emailPassword = "oewntczrvekqpvap"; 

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        });

        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Tu contraseña para Empuje Comunitario");
            message.setText("Hola!\nTu usuario ha sido creado.\n\nContraseña: " + password);

            Transport.send(message);
            System.out.println("Email enviado a: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("Error al enviar email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

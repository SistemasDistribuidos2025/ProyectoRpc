package org.example.ProyectoGrpc.servicio.implementacion;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    @Async
    public static void enviarPassword(String toEmail, String password) {
        final String fromEmail = "magosh90@gmail.com"; // tu email registrado
        final String emailPassword = "oewntczrvekqpvap"; // si Gmail, generar App Password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // servidor SMTP
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Tu contraseña para Empuje Comunitario");
            message.setText("Hola! Tu usuario ha sido creado.\nContraseña: " + password);

            Transport.send(message);
            System.out.println("Email enviado a: " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

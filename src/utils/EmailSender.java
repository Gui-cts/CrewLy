package utils;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    public static void enviarToken(String destino, String token, String assunto, String corpoPersonalizado) throws AddressException, MessagingException {
        final String remetente = "crewly.team@gmail.com";
        final String senha = "csem qygp zkdc bpxf";

        // Adiciona o token ao corpo personalizado
        String corpo = corpoPersonalizado + "\n\nToken:\n" + token + "\n\nEste token é válido por 30 minutos.";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(remetente, senha);
                }
            });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(remetente));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
        message.setSubject(assunto);
        message.setText(corpo);
        Transport.send(message);
        System.out.println("E-mail enviado com sucesso!");
    }
}

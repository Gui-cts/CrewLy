package utils;

import javax.mail.Session;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public static void enviarToken(String destino, String token) {
        final String remetente = "crewly.team@gmail.com";
        final String senha = "csem qygp zkdc bpxf"; 

        // Corpo do e-mail com o token (ajustado para desktop/local)
        String corpo = "Você solicitou a redefinição de senha.\n\n"
                     + "Seu token é:\n\n" + token + "\n\n"
                     + "Copie e cole este token no campo de redefinição no aplicativo.\n"
                     + "Este token é válido por 30 minutos.";

        // Configurações do servidor SMTP do gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Sessão autenticada
        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(remetente, senha);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
            message.setSubject("Recuperação de Senha");
            message.setText(corpo);

            Transport.send(message);
            System.out.println("E-mail enviado com sucesso!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

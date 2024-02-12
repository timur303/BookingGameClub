package kg.kadyrbekov.service;

import kg.kadyrbekov.mapper.Mail;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    public void sendEmail(Mail mail, String url, String confirmationCode) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", "localhost");
        properties.setProperty("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("pro.fri.suppor5@gmail.com", "azwyzkslybwnfvdz");
            }
        });
        try {
            Message message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper((MimeMessage) message);
            message.setFrom(new InternetAddress("pro.fri.suppor5@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getTo()));
            message.setSubject(message.getSubject());
            String subject = "Код подтверждения сброса пароля";
            String text =
                    confirmationCode;
            message.setContent(text, "text/html; charset=UTF-8");

            helper.setSubject(subject);
            helper.setText(text, true);
            message.setText(text);
            message.setSubject(subject);
            Transport.send(message);

        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }

    }

    public void sendLoginConfirmationCode(String to, String confirmationCode) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", "localhost");
        properties.setProperty("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("pro.fri.suppor5@gmail.com", "azwyzkslybwnfvdz");
            }
        });

        try {
            Message message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper((MimeMessage) message);
            message.setFrom(new InternetAddress("pro.fri.suppor5@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Код подтверждения входа в систему");
            String subject = "Код подтверждения входа в систему";
            String text = "Здравствуйте!"
                    + "Вы успешно отправили код подтверждения."
                    + "Используйте этот код для подтверждения операции:"
                    + "Если вы не запрашивали этот код, просто проигнорируйте это письмо.";
            String textHTML = "<p><strong>" + confirmationCode + "</strong></p>";
            message.setSubject(text);
            helper.setSubject(subject);
//            message.setContent(text,"");
            message.setContent(textHTML, "text/html");

            Transport.send(message);

        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void sendForgotPasswordCode(String to, String confirmationCode) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", "localhost");
        properties.setProperty("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("pro.fri.suppor5@gmail.com", "azwyzkslybwnfvdz");
            }
        });

        try {
            Message message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper((MimeMessage) message);
            message.setFrom(new InternetAddress("pro.fri.suppor5@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            String subject = "Код подтверждения сброса пароля";
            String text = "<html><body>"
                    + "<p><strong>" + confirmationCode + "</strong></p>";
            message.setContent(text, "text/html; charset=UTF-8");

            helper.setSubject(subject);
            helper.setText(text, true);

            Transport.send(message);

        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }

}
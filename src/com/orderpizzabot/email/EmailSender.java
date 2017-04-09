package com.orderpizzabot.email;

import com.sun.mail.smtp.SMTPMessage;

import javax.mail.Authenticator;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.util.Properties;

/**
 * Created by Artoym on 04.04.2017.
 * The class represents a an email sender that is responsible
 * for sending emails.
 */
public class EmailSender {
    EmailMessage emailMessage;
    public static final String managerEmail = "admitriev5813@gmail.com";
    public static final String kitchenEmail = "";
    public static final String courierEmail = "";

    public EmailSender(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    /**
     * Sends an email.
     * @param to
     * @param text
     */
    private void sendMessage(String to, String text) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "805");

        EmailAccountInfo emailAccountInfo = new EmailAccountInfo();
        Authenticator authenticator = new Authenticator() {
            /*@Override
            protected Authenticator getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(emailAccountInfo.getLogin(),emailAccountInfo.getPassword());
            }*/

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(emailAccountInfo.getLogin(),emailAccountInfo.getPassword());
            }
        };

        Session session = Session.getDefaultInstance(props, authenticator);

        try {

            SMTPMessage message = new SMTPMessage(session);
            message.setFrom(new InternetAddress("orderpizzabot@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            message.setSubject(emailMessage.getSubject());
            message.setText(text);
            message.setNotifyOptions(SMTPMessage.NOTIFY_SUCCESS);
            int returnOption = message.getReturnOption();
            System.out.println(returnOption);
            Transport.send(message);
            System.out.println("sent");

        }
        catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends an email message for a kitchen to the kitchen mailbox.
     */
    public void sendEmailToKitchen() {
        sendMessage(kitchenEmail, emailMessage.getEmailTextForKitchen());
    }

    /**
     * Sends an email message for a manager to the manager mailbox.
     */
    public void sendEmailToManager() {
        sendMessage(managerEmail, emailMessage.getEmailTextForManager());
    }

    /**
     * Sends an email message for a courier to the courier mailbox.
     */
    public void sendEmailToCourier() {
        sendMessage(courierEmail, emailMessage.getEmailTextForCourier());
    }
}

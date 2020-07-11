package com.example.gesparkmove;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

//class que executa o envio de emails
public class MailSender extends Authenticator {
    private String user;
    private String password;
    private Session session;
    Context context;
    private Multipart _multipart = new MimeMultipart();

    static {
        Security.addProvider(new JSSEProvider());
    }

    public MailSender(Context context,String user, String password) {
        this.user = user;
        this.password = password;
        this.context = context;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        String mailhost = "ssl0.ovh.net";
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }
    //método para enviar o email de ativação de conta
    public synchronized void sendActivateMail(String recipients, String user, String mail, String ativacao) throws Exception {
        MimeMessage message = new MimeMessage(session);
        DataHandler handler = new DataHandler(new ByteArrayDataSource("Hi".getBytes(), "text/plain"));
        message.setFrom(new InternetAddress("info.gespark@gespark.pt"));
        message.setSender(new InternetAddress("GesPark-Move"));
        message.setSubject("Registo no Gespark Move");
        message.setDataHandler(handler);

        BodyPart messageBodyPart = new MimeBodyPart();
        InputStream is = context.getAssets().open("register.html");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String str = new String(buffer);
        str = str.replace("%nome%", user);
        str = str.replace("%mail%", mail);
        str = str.replace("%codigoActivacao%", ativacao);
        messageBodyPart.setContent(str,"text/html; charset=utf-8");

        _multipart.addBodyPart(messageBodyPart);

        message.setContent(_multipart);


        if (recipients.indexOf(',') > 0)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));

        Transport.send(message);
    }
    //método para enviar o email de recuperação de password
    public synchronized void sendRecoveryMail(String recipients, String user, String recoverypass) throws Exception {
        MimeMessage message = new MimeMessage(session);
        DataHandler handler = new DataHandler(new ByteArrayDataSource("Hi".getBytes(), "text/plain"));
        message.setFrom(new InternetAddress("info.gespark@gespark.pt"));
        message.setSender(new InternetAddress("GesPark-Move"));
        message.setSubject("Recuperar senha Gespark Move");
        message.setDataHandler(handler);

        BodyPart messageBodyPart = new MimeBodyPart();
        InputStream is = context.getAssets().open("recovery.html");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String str = new String(buffer);
        str = str.replace("%nome%", user);
        str = str.replace("%recovery%", recoverypass);
        messageBodyPart.setContent(str,"text/html; charset=utf-8");

        _multipart.addBodyPart(messageBodyPart);

        message.setContent(_multipart);

        if (recipients.indexOf(',') > 0)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));

        Transport.send(message);
    }
}

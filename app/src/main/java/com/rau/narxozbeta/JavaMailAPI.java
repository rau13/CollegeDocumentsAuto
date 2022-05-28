package com.rau.narxozbeta;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class JavaMailAPI extends AsyncTask<Void, Void, Void> {

    private Context context;

    private Session session;
    private String email, subject, message, attachementPath, fileName;

    public JavaMailAPI(Context context, String email, String subject, String message, String attachementPath, String fileName) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.attachementPath = attachementPath;
        this.fileName = fileName;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Utils.EMAIL, Utils.PASSWORD);
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(Utils.EMAIL));
            mimeMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(email)));
            mimeMessage.setSubject(subject);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(message);
            MimeBodyPart attachmentBodyPart= new MimeBodyPart();
            DataSource source = new FileDataSource(attachementPath); // ex : "C:\\test.pdf"
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(fileName); // ex : "test.pdf"

            multipart.addBodyPart(textBodyPart);  // add the text part
            multipart.addBodyPart(attachmentBodyPart); // add the attachement part
            mimeMessage.setContent(multipart);
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return null;
    }
    /* msg.setFrom(new InternetAddress(mailFrom));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
        msg.setSubject("your subject");

        Multipart multipart = new MimeMultipart();

        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setText("your text");

        MimeBodyPart attachmentBodyPart= new MimeBodyPart();
        DataSource source = new FileDataSource(attachementPath); // ex : "C:\\test.pdf"
        attachmentBodyPart.setDataHandler(new DataHandler(source));
        attachmentBodyPart.setFileName(fileName); // ex : "test.pdf"

        multipart.addBodyPart(textBodyPart);  // add the text part
        multipart.addBodyPart(attachmentBodyPart); // add the attachement part

        msg.setContent(multipart);*/
}

package com.autentia.plugin.sendmail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class GMailSender extends javax.mail.Authenticator {
    private String mailhost = "mail.mobixt.co.za";
    private String user;
    private String password;
    private Session session;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public GMailSender(String user, String password) {
        this.user = user;
        this.password = password;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.user", user);
        props.put("mail.password", password);
        props.put("mail.debug","true");
        
        props.setProperty("mail.smtp.quitwait", "true");
        

        session = Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipients, String[] attachFiles) throws Exception {
        // The message object
        MimeMessage message = new MimeMessage(session);

        // Add trivial information
        message.setFrom(new InternetAddress(sender));
       
        if (recipients.indexOf(',') > 0) {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        } else {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
        }
          message.setSubject("MobiXt_Silent");
          message.setSentDate(new Date());
        
        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");
        
        // Create multipart content.
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // Add the body part
        //MimeBodyPart messageBodyPart = new MimeBodyPart();
        //messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/html")));
        //multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        // if (attachment != null) {
        //    messageBodyPart = new MimeBodyPart();
        //    DataSource source = new FileDataSource(attachment);
        //    messageBodyPart.setDataHandler(new DataHandler(source));
        //     messageBodyPart.setFileName(attachment);
        //     multipart.addBodyPart(messageBodyPart);
        // }
        
                // adds attachments
   /*     if (attachFiles != null && attachFiles.length > 0) {
            for (String filePath : attachFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();
 
                try {
                    //attachPart.attachFile(filePath);
                    
                    File f =new File(filePath);

                    attachPart.attachFile(f);

                    multipart.addBodyPart(attachPart);
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
 
                //multipart.addBodyPart(attachPart);
            }
        }   */

        // Put parts in message
        message.setContent(multipart);

        // Send the message.
        Transport.send(message);
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}

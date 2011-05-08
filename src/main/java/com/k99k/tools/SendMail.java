/**
 * 
 */
package com.k99k.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Email发送客户端,使用javamail实现
 * @author keel
 *
 */
public class SendMail implements Runnable {

	/**
	 * 
	 */
	public SendMail() {
	}
	
	 private String from;
     private String server;
     private int port;

     // Create properties for the Session
     private Properties props;
     
     // Get a session
     private Session session;
     
     
     public boolean init(String SMTPServer,String from,int port){
    	 this.server = SMTPServer;
    	 this.from = from;
    	 this.port = port;
    	 props = new Properties();
    	 // If using static Transport.send(),
         // need to specify the mail server here
         props.put("mail.smtp.host", server);
         props.put("mail.smtp.auth", "true");
         props.put("mail.transport.protocol", "smtp");
         props.put("mail.smtp.port", port+"");

         // To see what is going on behind the scene
         //props.put("mail.debug", "true");

         session = Session.getInstance(props);
    	 
    	 return true;
     }
     
     public final void sendMail(String to,String subject,String txt){
    	 try {
             //Get a Transport object to send e-mail
             Transport bus = session.getTransport("smtp");
             
             //
             bus.connect();
             
             //bus.connect("smtpserver.yourisp.net", "username", "password");
  
             // Instantiate a message
             Message msg = new MimeMessage(session);
  
             // Set message attributes
             msg.setFrom(new InternetAddress(from));
             InternetAddress[] address = {new InternetAddress(to)};
             msg.setRecipients(Message.RecipientType.TO, address);
             // Parse a comma-separated list of email addresses. Be strict.
             msg.setRecipients(Message.RecipientType.CC,
                                 InternetAddress.parse(to, true));
             // Parse comma/space-separated list. Cut some slack.
             msg.setRecipients(Message.RecipientType.BCC,
                                 InternetAddress.parse(to, false));
  
             msg.setSubject("Test E-Mail through Java");
             msg.setSentDate(new Date());
  
             // Set message content and send
             msg.setText(txt);
             msg.saveChanges();
             bus.sendMessage(msg, address);

             bus.close();
  
         }
         catch (Exception e) {
        	 e.printStackTrace();
         }
     }

   

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
	}

    // A simple multipart/mixed e-mail. Both body parts are text/plain.
    public static void setMultipartContent(Message msg) throws MessagingException {
        // Create and fill first part
        MimeBodyPart p1 = new MimeBodyPart();
        p1.setText("This is part one of a test multipart e-mail.");
 
        // Create and fill second part
        MimeBodyPart p2 = new MimeBodyPart();
        // Here is how to set a charset on textual content
        p2.setText("This is the second part", "us-ascii");
 
        // Create the Multipart.  Add BodyParts to it.
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(p1);
        mp.addBodyPart(p2);
 
        // Set Multipart as the message's content
        msg.setContent(mp);
    }
 
    // Set a file as an attachment.  Uses JAF FileDataSource.
    public static void setFileAsAttachment(Message msg, String filename)
             throws MessagingException {
 
        // Create and fill first part
        MimeBodyPart p1 = new MimeBodyPart();
        p1.setText("This is part one of a test multipart e-mail." +
                    "The second part is file as an attachment");
 
        // Create second part
        MimeBodyPart p2 = new MimeBodyPart();
 
        // Put a file in the second part
        FileDataSource fds = new FileDataSource(filename);
        p2.setDataHandler(new DataHandler(fds));
        p2.setFileName(fds.getName());
 
        // Create the Multipart.  Add BodyParts to it.
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(p1);
        mp.addBodyPart(p2);
 
        // Set Multipart as the message's content
        msg.setContent(mp);
    }
 
    // Set a single part html content.
    // Sending data of any type is similar.
    public static void setHTMLContent(Message msg) throws MessagingException {
 
        String html = "<html><head><title>" +
                        msg.getSubject() +
                        "</title></head><body><h1>" +
                        msg.getSubject() +
                        "</h1><p>This is a test of sending an HTML e-mail" +
                        " through Java.</body></html>";
 
        // HTMLDataSource is an inner class
        msg.setDataHandler(new DataHandler(new HTMLDataSource(html)));
    }
 
    /*
     * Inner class to act as a JAF datasource to send HTML e-mail content
     */
    static class HTMLDataSource implements DataSource {
        private String html;
 
        public HTMLDataSource(String htmlString) {
            html = htmlString;
        }
 
        // Return html string in an InputStream.
        // A new stream must be returned each time.
        public InputStream getInputStream() throws IOException {
            if (html == null) throw new IOException("Null HTML");
            return new ByteArrayInputStream(html.getBytes());
        }
 
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }
 
        public String getContentType() {
            return "text/html";
        }
 
        public String getName() {
            return "JAF text/html dataSource to send e-mail only";
        }
    }

	/**
	 * @return the from
	 */
	public final String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public final void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the server
	 */
	public final String getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public final void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return the port
	 */
	public final int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public final void setPort(int port) {
		this.port = port;
	}
    
    
    
}

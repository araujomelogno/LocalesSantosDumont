package uy.com.innobit.rem.business.notifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final int SMTP_HOST_PORT = 465;
	private static final String SMTP_AUTH_USER = "notificaciones@equipos.com.uy";
	private static final String SMTP_AUTH_PWD = "m4dyt3ls4";

	public static void main(String... args) {
		MailSender ms = new MailSender();
		MailMessage m1 = new MailMessage();
		List<String> r = new ArrayList<String>();
		r.add("araujomelogno@gmail.com");
		m1.setRecipients(r);
		m1.setBody("este es el body");
		m1.setSubject("este es el asuntsub");
		List<MailMessage> mails = new ArrayList<MailMessage>();
		mails.add(m1);
		ms.sendMail(mails);
	}

	public void sendMail(List<MailMessage> messages) {
		Transport transport = null;
		try {
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtps");
			props.put("mail.smtps.host", SMTP_HOST_NAME);
			props.put("mail.smtps.auth", "true");
			Session mailSession = Session.getDefaultInstance(props);
			transport = mailSession.getTransport();
			for (MailMessage m : messages) {
				MimeMessage message = new MimeMessage(mailSession);
				for (String recipient : m.getRecipients())
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
				message.setSubject(m.getSubject());
				if (m.isHtml())
					message.setContent(m.getBody(), "text/html");
				else
					message.setText(m.getBody());
				transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
				transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			}
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}

	}
}

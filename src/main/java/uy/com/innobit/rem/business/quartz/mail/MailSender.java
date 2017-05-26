package uy.com.innobit.rem.business.quartz.mail;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.vaadin.server.VaadinService;

import uy.com.innobit.rem.business.managers.MailMessageManager;
import uy.com.innobit.rem.business.util.MessageTemplates;
import uy.com.innobit.rem.persistence.datamodel.mail.MailMessage;

public class MailSender {
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final int SMTP_HOST_PORT = 465;
	private static final String SMTP_AUTH_USER = "awssdum2017@gmail.com";
	private static final String SMTP_AUTH_PWD = "m4dyt3ls4";
	private final String MESSAGE_TITTLE = "$$NotificationTittle$$";
	private final String MESSAGE_MESSAGE = "$$NotificationMessage$$";
	private static MailSender instance;

	public static MailSender getInstance() {
		if (instance == null)
			instance = new MailSender();
		return instance;
	}

	public synchronized void saveMail(String subject, String content, Set<String> emails, String logoUrl) {
		MailMessage m1 = new MailMessage();

		m1.setRecipients(emails);
		m1.setBody(MessageTemplates.genericMailNotificationTemplate.replace(MESSAGE_TITTLE, subject)
				.replace(MESSAGE_MESSAGE, content));
		m1.setSubject(subject);
		m1.setLogoUrl(logoUrl);
		;
		m1.setHtml(true);
		MailMessageManager.getInstance().saveMailMessage(m1);
	}

	public synchronized void savePropertyMail(String subject, String message, String email,
			Map<String, String> properties) {
		MailMessage m1 = new MailMessage();

		m1.getRecipients().add(email);
		String body = MessageTemplates.propertyMailTemplate.replace(MESSAGE_TITTLE, subject).replace(MESSAGE_MESSAGE,
				message);

		if (properties.containsKey("name"))
			body = body.replace("$$name$$", properties.get("name"));
		else
			body = body.replace("$$hiddenName$$", "style= \"display:none !important;\"");

		if (properties.containsKey("padron"))
			body = body.replace("$$padron$$", properties.get("padron"));
		else
			body = body.replace("$$hiddenPadron$$", "style= \"display:none !important;\"");

		if (properties.containsKey("block"))
			body = body.replace("$$block$$", properties.get("block"));
		else
			body = body.replace("$$hiddenBlock$$", "style= \"display:none !important;\"");

		if (properties.containsKey("payExpenses"))
			body = body.replace("$$payExpenses$$", properties.get("payExpenses"));
		else
			body = body.replace("$$hiddenPayExpenses$$", "style= \"display:none !important;\"");

		if (properties.containsKey("expenses"))
			body = body.replace("$$expenses$$", properties.get("expenses"));
		else
			body = body.replace("$$hiddenExpenses$$", "style= \"display:none !important;\"");

		if (properties.containsKey("expensesFreq"))
			body = body.replace("$$expensesFreq$$", properties.get("expensesFreq"));
		else
			body = body.replace("$$hiddenExpensesFreq$$", "style= \"display:none !important;\"");

		if (properties.containsKey("refUte"))
			body = body.replace("$$refUte$$", properties.get("refUte"));
		else
			body = body.replace("$$hiddenRefUte$$", "style= \"display:none !important;\"");

		if (properties.containsKey("refAgua"))
			body = body.replace("$$refAgua$$", properties.get("refAgua"));
		else
			body = body.replace("$$hiddenRefAgua$$", "style= \"display:none !important;\"");

		if (properties.containsKey("size"))
			body = body.replace("$$size$$", properties.get("size"));
		else
			body = body.replace("$$hiddenSize$$", "style= \"display:none !important;\"");

		if (properties.containsKey("address"))
			body = body.replace("$$address$$", properties.get("address"));
		else
			body = body.replace("$$hiddenAddress$$", "style= \"display:none !important;\"");

		if (properties.containsKey("tel"))
			body = body.replace("$$tel$$", properties.get("tel"));
		else
			body = body.replace("$$hiddenTel$$", "style= \"display:none !important;\"");

		if (properties.containsKey("obs"))
			body = body.replace("$$obs$$", properties.get("obs"));
		else
			body = body.replace("$$hiddenObs$$", "style= \"display:none !important;\"");

		if (properties.containsKey("nro"))
			body = body.replace("$$nro$$", properties.get("nro"));
		else
			body = body.replace("$$hiddenNro$$", "style= \"display:none !important;\"");

		m1.setBody(body);
		m1.setSubject(subject);

		m1.setLogoUrl(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/WEB-INF/logo.png");

		m1.setHtml(true);
		MailMessageManager.getInstance().saveMailMessage(m1);
	}

	public synchronized void sendMail(List<MailMessage> messages) {
		Transport transport = null;
		try {
			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtps");
			props.put("mail.smtps.host", SMTP_HOST_NAME);
			props.put("mail.smtps.auth", "true");
			Session mailSession = Session.getDefaultInstance(props);
			transport = mailSession.getTransport();
			for (MailMessage m : messages) {
				if (!m.getRecipients().isEmpty()) {
					MimeMessage message = new MimeMessage(mailSession);
					message.setFrom(new InternetAddress(SMTP_AUTH_USER, "EPM"));
					for (String recipient : m.getRecipients())
						message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
					message.setSubject(m.getSubject());
					if (m.getHtml()) {
						message.setContent(m.getBody(), "text/html");

						// This mail has 2 part, the BODY and the embedded image
						MimeMultipart multipart = new MimeMultipart("related");

						// first part (the html)
						BodyPart messageBodyPart = new MimeBodyPart();
						messageBodyPart.setContent(m.getBody(), "text/html");
						// add it
						multipart.addBodyPart(messageBodyPart);

						// second part (the image)
						messageBodyPart = new MimeBodyPart();
						DataSource fds = new FileDataSource(m.getLogoUrl());

						messageBodyPart.setDataHandler(new DataHandler(fds));
						messageBodyPart.setHeader("Content-ID", "<image>");

						// add image to the multipart
						multipart.addBodyPart(messageBodyPart);

						// put everything together
						message.setContent(multipart);
					} else
						message.setText(m.getBody());

					if (!transport.isConnected())
						transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
					transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
					m.setSent(true);
					MailMessageManager.getInstance().updateMailMessage(m);
				} else {
					m.setSent(true);
					MailMessageManager.getInstance().updateMailMessage(m);
				}
			}
			transport.close();
		} catch (

		Exception e)

		{
			e.printStackTrace();
		} finally

		{
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

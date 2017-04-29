package uy.com.innobit.rem.business.notifications;

import java.util.ArrayList;
import java.util.List;

import uy.com.innobit.rem.business.managers.NotificationsManager;
import uy.com.innobit.rem.business.util.MessageTemplates;
import uy.com.innobit.rem.persistence.datamodel.notifications.Notification;

public class NotifiactionEngine {
	private static NotifiactionEngine instance;

	private NotifiactionEngine() {
	}

	public static NotifiactionEngine getInstance() {
		if (instance == null)
			instance = new NotifiactionEngine();
		return instance;
	}

	public synchronized void checkNotifications() {
		List<Notification> notifications = NotificationsManager.getInstance().getContractNotificationsToSend();
		notifications.addAll(NotificationsManager.getInstance().getPropertyNotificationsToSend());
		List<MailMessage> messages = new ArrayList<MailMessage>();
		for (Notification notification : notifications) {
			MailMessage message = new MailMessage();
			message.setSubject(notification.getSubject());
			message.setRecipients(notification.getRecipients());
			message.setHtml(true);
			message.setBody(MessageTemplates.mailNotificationTemplate.replace("$$BODY$$", notification.getMessage()));
			messages.add(message);
		}
		MailSender mailSender = new MailSender();
		mailSender.sendMail(messages);
	}

}

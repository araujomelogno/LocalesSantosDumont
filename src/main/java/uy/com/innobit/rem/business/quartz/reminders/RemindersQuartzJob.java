package uy.com.innobit.rem.business.quartz.reminders;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import uy.com.innobit.rem.business.managers.NotificationsManager;
import uy.com.innobit.rem.business.quartz.mail.MailSender;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractNotification;
import uy.com.innobit.rem.persistence.datamodel.notifications.Notification;
import uy.com.innobit.rem.persistence.datamodel.property.PropertyNotification;
import uy.com.innobit.rem.persistence.util.HibernateSessionManager;

public class RemindersQuartzJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		HibernateSessionManager.init();
		List<Notification> reminders = NotificationsManager.getInstance().getContractNotificationsToSend();
		reminders.addAll(NotificationsManager.getInstance().getPropertyNotificationsToSend()); 
		for (Notification not : reminders) { 
			Set<String> recipents = new HashSet<>();
			recipents.addAll(not.getRecipients());
			MailSender.getInstance().saveMail(not.getSubject(), not.getMessage(), recipents, not.getLogoUrl());
			not.setSent(true);
			if (not instanceof ContractNotification) {
				ContractNotification new_name = (ContractNotification) not;
				NotificationsManager.getInstance().update(new_name);
			}
			if (not instanceof PropertyNotification) {
				PropertyNotification new_name = (PropertyNotification) not;
				NotificationsManager.getInstance().update(new_name);
			}
		}
	}

}
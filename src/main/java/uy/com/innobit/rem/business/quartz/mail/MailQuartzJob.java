package uy.com.innobit.rem.business.quartz.mail;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import uy.com.innobit.rem.business.managers.MailMessageManager;
import uy.com.innobit.rem.persistence.datamodel.mail.MailMessage;
import uy.com.innobit.rem.persistence.util.HibernateSessionManager;

public class MailQuartzJob implements Job {
	private MailSender sender = new MailSender();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		HibernateSessionManager.init();
		List<MailMessage> messages = MailMessageManager.getInstance().getNotSent();
		if (messages != null && !messages.isEmpty())
			sender.sendMail(messages);

	}

}
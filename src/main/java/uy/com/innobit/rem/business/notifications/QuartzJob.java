package uy.com.innobit.rem.business.notifications;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("JSF 2 + Quartz 2 example");
		// NotifiactionEngine.getInstance().checkNotifications();

	}

}
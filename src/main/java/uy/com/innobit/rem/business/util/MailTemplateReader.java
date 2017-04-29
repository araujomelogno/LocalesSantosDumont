package uy.com.innobit.rem.business.util;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MailTemplateReader implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("/mailTemplate/receipt.html"));
			StringBuffer sb = new StringBuffer();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			br.close();
			MessageTemplates.mailNotificationTemplate = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

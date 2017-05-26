package uy.com.innobit.rem.business.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinService;

public class MailTemplateReader implements SessionInitListener {
	private static final long serialVersionUID = 1L;

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException {
		try {
			System.out.println("READEEEER");
			BufferedReader br = new BufferedReader(
					new FileReader(VaadinService.getCurrent().getBaseDirectory() + "/receipt.html"));
			StringBuffer sb = new StringBuffer();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			br.close();
			MessageTemplates.genericMailNotificationTemplate = sb.toString();
			br = new BufferedReader(
					new FileReader(VaadinService.getCurrent().getBaseDirectory() + "/propertyMail.html"));
			sb = new StringBuffer();
			line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			br.close();
			MessageTemplates.propertyMailTemplate = sb.toString();

			MessageTemplates.template = WordprocessingMLPackage
					.load(new File((VaadinService.getCurrent().getBaseDirectory() + "/contrato.docx")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

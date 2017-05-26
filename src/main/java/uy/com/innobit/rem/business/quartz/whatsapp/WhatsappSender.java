package uy.com.innobit.rem.business.quartz.whatsapp;

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
import uy.com.innobit.rem.business.managers.WhatsappMessageManager;
import uy.com.innobit.rem.business.util.MessageTemplates;
import uy.com.innobit.rem.persistence.datamodel.mail.MailMessage;
import uy.com.innobit.rem.persistence.datamodel.whatsapp.WhatsappMessage;

public class WhatsappSender {
	private static WhatsappSender instance;

	public static WhatsappSender getInstance() {
		if (instance == null)
			instance = new WhatsappSender();
		return instance;
	}

	public synchronized void savePropertyMail(String message, String mobile, Map<String, String> properties) {
		WhatsappMessage m1 = new WhatsappMessage();
		m1.setMobile(mobile);
		String body = message;

		if (properties.containsKey("nro"))
			body = body + "*Nro*: " + properties.get("nro") + ".";

		if (properties.containsKey("name"))
			body = body + "*Nombre*: " + properties.get("name") + ".";

		if (properties.containsKey("padron"))
			body = body + "*Padrón*: " + properties.get("padron") + ".";

		if (properties.containsKey("block"))
			body = body + "*Manzana*: " + properties.get("block") + ".";

		if (properties.containsKey("payExpenses"))
			body = body + "*Paga G.C.:*: " + properties.get("payExpenses") + ".";

		if (properties.containsKey("expenses"))
			body = body + "*G.C.:*: " + properties.get("expenses") + ".";

		if (properties.containsKey("expensesFreq"))
			body = body + "*Frecuencia G.C.*: " + properties.get("expensesFreq") + ".";

		if (properties.containsKey("refUte"))
			body = body + "*Ref UTE*: " + properties.get("refUte") + ".";

		if (properties.containsKey("refAgua"))
			body = body + "*Ref OSE*: " + properties.get("refAgua") + ".";

		if (properties.containsKey("size"))
			body = body + "*Tamaño*: " + properties.get("size") + ".";

		if (properties.containsKey("address"))
			body = body + "*Dirección*: " + properties.get("address") + ".";

		if (properties.containsKey("tel"))
			body = body + "*Teléfono*: " + properties.get("tel") + ".";

		if (properties.containsKey("obs"))
			body = body + "*Observaciones*: " + properties.get("obs") + ".";

		m1.setBody(body);
		WhatsappMessageManager.getInstance().saveWhatsappMessage(m1);
	}

}

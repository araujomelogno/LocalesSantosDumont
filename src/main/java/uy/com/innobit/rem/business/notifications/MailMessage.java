package uy.com.innobit.rem.business.notifications;

import java.util.ArrayList;
import java.util.List;

public class MailMessage {
	private List<String> recipients = new ArrayList<String>();
	private boolean html;
	private String body;
	private String subject;

	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}

	public boolean isHtml() {
		return html;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}

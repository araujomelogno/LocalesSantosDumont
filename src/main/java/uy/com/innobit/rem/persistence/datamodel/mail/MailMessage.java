package uy.com.innobit.rem.persistence.datamodel.mail;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import uy.com.innobit.rem.persistence.datamodel.Bean;
 
@Entity(name = "mail_message")
public class MailMessage extends Bean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id = 0;

	@org.hibernate.annotations.CollectionOfElements(targetElement = java.lang.String.class, fetch = FetchType.EAGER)
	@JoinTable(name = "mail_message_recipient", joinColumns = @JoinColumn(name = "mail_message_id") )
	@Column(name = "recipient", nullable = false)
	@Fetch(FetchMode.SELECT)
	private Set<String> recipients = new HashSet<String>();
	private Boolean html;
	@Column(columnDefinition = "TEXT")
	private String body;
	private String subject;
	private String logoUrl;

	private Boolean sent = false;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getHtml() {
		return html;
	}

	public void setHtml(Boolean html) {
		this.html = html;
	}

	public Boolean getSent() {
		return sent;
	}

	public void setSent(Boolean sent) {
		this.sent = sent;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public Set<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(Set<String> recipients) {
		this.recipients = recipients;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MailMessage) {
			MailMessage new_name = (MailMessage) obj;
			return new_name.getId().equals(id);

		}
		return false;
	}

}

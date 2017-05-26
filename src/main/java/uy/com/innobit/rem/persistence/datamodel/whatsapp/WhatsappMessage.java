package uy.com.innobit.rem.persistence.datamodel.whatsapp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import uy.com.innobit.rem.persistence.datamodel.Bean;

@Entity(name = "whatsapp_message")
public class WhatsappMessage extends Bean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id = 0;
	private String mobile;
	@Column(columnDefinition = "TEXT")
	private String body;

	private Boolean sent = false;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getSent() {
		return sent;
	}

	public void setSent(Boolean sent) {
		this.sent = sent;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WhatsappMessage) {
			WhatsappMessage new_name = (WhatsappMessage) obj;
			return new_name.getId().equals(id);

		}
		return false;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}

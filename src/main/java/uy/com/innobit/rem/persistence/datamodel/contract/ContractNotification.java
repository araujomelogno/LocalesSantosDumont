package uy.com.innobit.rem.persistence.datamodel.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

import uy.com.innobit.rem.persistence.datamodel.Bean;
import uy.com.innobit.rem.persistence.datamodel.notifications.Notification;

@Entity(name = "contract_notification")
public class ContractNotification extends Bean implements Notification {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Date notificationDate;
	private String message;
	private String name;
	private boolean resolved;
	private boolean sent;

//	// @ElementCollection
//	// @CollectionTable(name = "contract_notification_recipient", joinColumns =
//	// @JoinColumn(name = "contract_id") )
//	// @Column(name = "recipient", nullable = false)
//	@org.hibernate.annotations.CollectionOfElements(targetElement = java.lang.String.class)
//	@JoinTable(name = "contract", joinColumns = @JoinColumn(name = "contract_id") )
//	// @org.hibernate.annotations.IndexColumn(name = "POSITION", base = 1)
//	@Column(name = "recipient", nullable = false)
//	private List<String> recipients = new ArrayList<String>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}
//
//	public List<String> getRecipients() {
//		return recipients;
//	}
//
//	public void setRecipients(List<String> recipients) {
//		this.recipients = recipients;
//	}

	@Override
	public String getSubject() {
		return name;
	}

	@Override
	public List<String> getRecipients() {
		// TODO Auto-generated method stub
		return null;
	}

}

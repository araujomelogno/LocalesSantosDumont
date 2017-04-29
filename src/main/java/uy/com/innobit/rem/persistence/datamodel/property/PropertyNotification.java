package uy.com.innobit.rem.persistence.datamodel.property;

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
import javax.persistence.ManyToOne;

import uy.com.innobit.rem.persistence.datamodel.Bean;
import uy.com.innobit.rem.persistence.datamodel.notifications.Notification;

@Entity(name = "property_notification")
public class PropertyNotification extends Bean implements Notification {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Date notificationDate;
	private String message;
	private String name;
	private boolean resolved;
	private boolean sent;
	@ManyToOne(optional = false)
	@JoinColumn(name = "property_id", nullable = false)
	private Property property;

	// @ElementCollection
	// @CollectionTable(name = "property_notification_recipient", joinColumns =
	// @JoinColumn(name = "property_id") )
	// @Column(name = "recipient")

	// @ElementCollection
	// @CollectionTable(name = "contract_notification_recipient", joinColumns =
	// @JoinColumn(name = "contract_id") )
	// @Column(name = "recipient", nullable = false)
	@org.hibernate.annotations.CollectionOfElements(targetElement = java.lang.String.class)
	@JoinTable(name = "property_notification_recipient", joinColumns = @JoinColumn(name = "notification_id") )
//	@org.hibernate.annotations.IndexColumn(name = "POSITION", base = 1)
	@Column(name = "recipient", nullable = false)
	private List<String> recipients = new ArrayList<String>();

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

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}

	public String getSubject() {
		return name;
	}
}

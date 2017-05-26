package uy.com.innobit.rem.persistence.datamodel.contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import uy.com.innobit.rem.persistence.datamodel.Bean;
import uy.com.innobit.rem.persistence.datamodel.clients.Owner;
import uy.com.innobit.rem.persistence.datamodel.notifications.Notification;
import uy.com.innobit.rem.persistence.datamodel.property.PropertyDocument;

@Entity(name = "contract_notification")
public class ContractNotification extends Bean implements Notification {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id = 0;
	private Date notificationDate;
	private String message;
	private boolean resolved;
	private boolean sent;

	private String logoUrl;

	@org.hibernate.annotations.CollectionOfElements(targetElement = java.lang.String.class, fetch = FetchType.EAGER)
	@JoinTable(name = "contract_notification_recipient", joinColumns = @JoinColumn(name = "notification_id") )
	@Column(name = "recipient", nullable = false)
	private List<String> recipients = new ArrayList<String>();

	@Transient
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@ManyToOne(optional = true)
	@JoinColumn(name = "contract_id")
	private Contract contract;

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

	@Override
	public String getSubject() {
		return "Recordatorio de contrato";
	}

	public String getDateSDF() {
		if (notificationDate != null)
			return sdf.format(notificationDate);
		return "";
	}

	public String getMessageAux() {
		if (message != null)
			if (message.length() > 10)
				return message.substring(0, 10) + "...";
			else
				return message;
		return "";
	}

	public String getResolvedAux() {
		if (resolved)
			return "Sí";
		else
			return "No";
	}

	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ContractNotification) {
			ContractNotification new_name = (ContractNotification) obj;
			return id.equals(new_name.getId());
		}
		return false;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

}

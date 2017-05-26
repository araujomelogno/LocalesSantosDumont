package uy.com.innobit.rem.persistence.datamodel.notifications;

import java.util.Date;
import java.util.List;

public interface Notification {
	String getSubject();

	List<String> getRecipients();

	String getMessage();

	String getDateSDF();

	String getMessageAux();

	String getLogoUrl();

	Date getNotificationDate();

	void setResolved(boolean resolved);

	void setSent(boolean sent);

	boolean isResolved();

}

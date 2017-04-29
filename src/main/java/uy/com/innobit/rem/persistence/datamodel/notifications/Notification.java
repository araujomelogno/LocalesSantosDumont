package uy.com.innobit.rem.persistence.datamodel.notifications;

import java.util.List;

public interface Notification {
	String getSubject();
	List<String> getRecipients();
	String getMessage();
}

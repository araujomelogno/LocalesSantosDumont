package uy.com.innobit.rem.business.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import uy.com.innobit.rem.persistence.datamodel.contract.ContractNotification;
import uy.com.innobit.rem.persistence.datamodel.notifications.Notification;
import uy.com.innobit.rem.persistence.datamodel.property.PropertyNotification;
import uy.com.innobit.rem.persistence.util.DBEntityManagerFactory;

public class NotificationsManager {
	private static NotificationsManager instance;

	private NotificationsManager() {
	}

	public static NotificationsManager getInstance() {
		if (instance == null)
			instance = new NotificationsManager();
		return instance;
	}

	public synchronized List<Notification> getContractNotificationsToSend() {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.and(Restrictions.eq("resolved", false),
				Restrictions.and(Restrictions.le("notificationDate", new Date()), Restrictions.eq("sent", false))));
		List<Notification> result = new ArrayList<Notification>();
		result.addAll(DBEntityManagerFactory.get(ContractNotification.class).getByCriterion(criterias));
		return result;
	}

	public synchronized List<Notification> getPropertyNotificationsToSend() {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.and(Restrictions.eq("resolved", false),
				Restrictions.and(Restrictions.le("notificationDate", new Date()), Restrictions.eq("sent", false))));
		List<Notification> result = new ArrayList<Notification>();
		result.addAll(DBEntityManagerFactory.get(PropertyNotification.class).getByCriterion(criterias));
		return result;
	}

	public synchronized List<Notification> getPropertyNotificationsToResolve() {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("resolved", false));
		List<Notification> result = new ArrayList<Notification>();
		result.addAll(DBEntityManagerFactory.get(PropertyNotification.class).getByCriterion(criterias));
		return result;
	}

	public synchronized List<Notification> getContractNotificationsToResolve() {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("resolved", false));
		List<Notification> result = new ArrayList<Notification>();
		result.addAll(DBEntityManagerFactory.get(ContractNotification.class).getByCriterion(criterias));
		return result;
	}

	public synchronized List<Notification> getPropertyNotificationsToResolveEXPIRED() {
		Date now = new Date();
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("resolved", false));
		criterias.add(Restrictions.le("notificationDate", now));
		List<Notification> result = new ArrayList<Notification>();
		result.addAll(DBEntityManagerFactory.get(PropertyNotification.class).getByCriterion(criterias));
		return result;
	}

	public synchronized List<Notification> getContractNotificationsToResolveEXPIRED() {
		Date now = new Date();
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("resolved", false));
		criterias.add(Restrictions.le("notificationDate", now));
		List<Notification> result = new ArrayList<Notification>();
		result.addAll(DBEntityManagerFactory.get(ContractNotification.class).getByCriterion(criterias));
		return result;
	}

	public synchronized void update(ContractNotification not) {
		DBEntityManagerFactory.get(ContractNotification.class).updateEntity(not);
	}

	public synchronized void update(PropertyNotification not) {
		DBEntityManagerFactory.get(PropertyNotification.class).updateEntity(not);
	}

}

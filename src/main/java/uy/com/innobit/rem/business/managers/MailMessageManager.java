package uy.com.innobit.rem.business.managers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import uy.com.innobit.rem.persistence.datamodel.mail.MailMessage;
import uy.com.innobit.rem.persistence.util.DBEntityManagerFactory;

 
public class MailMessageManager {
	private static MailMessageManager instance;

	private MailMessageManager() {
	}

	public static MailMessageManager getInstance() {
		if (instance == null)
			instance = new MailMessageManager();
		return instance;

	}

	public synchronized MailMessage getById(Integer id) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("id", id));
		List<MailMessage> list = DBEntityManagerFactory.get(MailMessage.class).getByCriterion(criterias);
		if (list == null || list.isEmpty())
			return null;

		return list.get(0);

	}
	
	



	public synchronized List<MailMessage> getNotSent() {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("sent",false));
		List<MailMessage> list = DBEntityManagerFactory.get(MailMessage.class).getByCriterion(criterias);
		

		return list;

	}
	
	

	
	public synchronized List<MailMessage> getAll() {
		return DBEntityManagerFactory.get(MailMessage.class).getAll();

	}

	public synchronized void saveMailMessage(MailMessage entity) {
		DBEntityManagerFactory.get(MailMessage.class).saveEntity(entity);
	}

	public synchronized void updateMailMessage(MailMessage entity) {
		DBEntityManagerFactory.get(MailMessage.class).updateEntity(entity);
	}

	public synchronized void deleteMailMessage(MailMessage entity) {
		DBEntityManagerFactory.get(MailMessage.class).delete(entity);
	}
}

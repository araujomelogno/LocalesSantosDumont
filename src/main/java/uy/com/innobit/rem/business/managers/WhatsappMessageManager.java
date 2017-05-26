package uy.com.innobit.rem.business.managers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import uy.com.innobit.rem.persistence.datamodel.whatsapp.WhatsappMessage;
import uy.com.innobit.rem.persistence.util.DBEntityManagerFactory;

 
public class WhatsappMessageManager {
	private static WhatsappMessageManager instance;

	private WhatsappMessageManager() {
	}

	public static WhatsappMessageManager getInstance() {
		if (instance == null)
			instance = new WhatsappMessageManager();
		return instance;

	}

	public synchronized WhatsappMessage getById(Integer id) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("id", id));
		List<WhatsappMessage> list = DBEntityManagerFactory.get(WhatsappMessage.class).getByCriterion(criterias);
		if (list == null || list.isEmpty())
			return null;

		return list.get(0);

	}
	
	



	public synchronized List<WhatsappMessage> getNotSent() {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("sent",false));
		List<WhatsappMessage> list = DBEntityManagerFactory.get(WhatsappMessage.class).getByCriterion(criterias);
		

		return list;

	}
	
	

	
	public synchronized List<WhatsappMessage> getAll() {
		return DBEntityManagerFactory.get(WhatsappMessage.class).getAll();

	}

	public synchronized void saveWhatsappMessage(WhatsappMessage entity) {
		DBEntityManagerFactory.get(WhatsappMessage.class).saveEntity(entity);
	}

	public synchronized void updateWhatsappMessage(WhatsappMessage entity) {
		DBEntityManagerFactory.get(WhatsappMessage.class).updateEntity(entity);
	}

	public synchronized void deleteWhatsappMessage(WhatsappMessage entity) {
		DBEntityManagerFactory.get(WhatsappMessage.class).delete(entity);
	}
}

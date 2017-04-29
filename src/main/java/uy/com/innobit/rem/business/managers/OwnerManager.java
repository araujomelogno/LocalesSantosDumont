package uy.com.innobit.rem.business.managers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import uy.com.innobit.rem.persistence.datamodel.clients.Owner;
import uy.com.innobit.rem.persistence.util.DBEntityManagerFactory;

public class OwnerManager {
	private static OwnerManager instance;

	private OwnerManager() {
	}

	public static OwnerManager getInstance() {
		if (instance == null)
			instance = new OwnerManager();
		return instance;

	}

	public synchronized List<Owner> getAll() {
		return DBEntityManagerFactory.get(Owner.class).getAll();

	}

	public synchronized Owner getById(Integer id) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("id", id));
		List<Owner> list = DBEntityManagerFactory.get(Owner.class).getByCriterion(criterias);
		if (list == null || list.isEmpty())
			return null;

		return list.get(0);

	}

	public synchronized void saveUser(Owner entity) {
		DBEntityManagerFactory.get(Owner.class).saveEntity(entity);
	}

	public synchronized void updateUser(Owner entity) {
		DBEntityManagerFactory.get(Owner.class).updateEntity(entity);
	}

	public synchronized void deleteUser(Owner entity) {
		DBEntityManagerFactory.get(Owner.class).delete(entity);
	}
}

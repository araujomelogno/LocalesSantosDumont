package uy.com.innobit.rem.business.managers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import uy.com.innobit.rem.persistence.datamodel.clients.Occupant;
import uy.com.innobit.rem.persistence.util.DBEntityManagerFactory;

public class OccupantManager {
	private static OccupantManager instance;

	private OccupantManager() {
	}

	public static OccupantManager getInstance() {
		if (instance == null)
			instance = new OccupantManager();
		return instance;

	}

	public synchronized Occupant getById(Integer id) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("id", id));
		List<Occupant> list = DBEntityManagerFactory.get(Occupant.class).getByCriterion(criterias);
		if (list == null || list.isEmpty())
			return null;

		return list.get(0);

	}

	public synchronized List<Occupant> getAll() {
		return DBEntityManagerFactory.get(Occupant.class).getAll();

	}

	public synchronized void saveUser(Occupant entity) {
		DBEntityManagerFactory.get(Occupant.class).saveEntity(entity);
	}

	public synchronized void updateUser(Occupant entity) {
		DBEntityManagerFactory.get(Occupant.class).updateEntity(entity);
	}

	public synchronized void deleteUser(Occupant entity) {
		DBEntityManagerFactory.get(Occupant.class).delete(entity);
	}
}

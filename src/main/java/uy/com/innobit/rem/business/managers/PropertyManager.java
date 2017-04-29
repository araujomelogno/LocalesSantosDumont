package uy.com.innobit.rem.business.managers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.persistence.datamodel.property.PropertyDocument;
import uy.com.innobit.rem.persistence.util.DBEntityManagerFactory;

public class PropertyManager {
	private static PropertyManager instance;

	private PropertyManager() {
	}

	public static PropertyManager getInstance() {
		if (instance == null)
			instance = new PropertyManager();
		return instance;

	}

	public synchronized Property getById(Integer id) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("id", id));
		List<Property> list = DBEntityManagerFactory.get(Property.class).getByCriterion(criterias);
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	public synchronized List<Property> getAll() {
		return DBEntityManagerFactory.get(Property.class).getAll();

	}

	public synchronized void saveUser(Property entity) {
		DBEntityManagerFactory.get(Property.class).saveEntity(entity);
	}

	public synchronized void updateUser(Property entity) {
		DBEntityManagerFactory.get(Property.class).updateEntity(entity);
	}

	public synchronized void deleteUser(Property entity) {
		DBEntityManagerFactory.get(Property.class).delete(entity);
	}

	public synchronized void delete(PropertyDocument entity) {
		DBEntityManagerFactory.get(PropertyDocument.class).delete(entity);
	}

	public synchronized void saveDocument(PropertyDocument document) {
		DBEntityManagerFactory.get(PropertyDocument.class).saveEntity(document);

	}

	public synchronized void updateDocument(PropertyDocument document) {
		DBEntityManagerFactory.get(PropertyDocument.class).updateEntity(document);

	}

}

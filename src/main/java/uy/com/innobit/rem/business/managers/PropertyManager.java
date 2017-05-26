package uy.com.innobit.rem.business.managers;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.types.PropertySet.PropertyRef;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.vaadin.server.VaadinService;

import uy.com.innobit.rem.persistence.datamodel.clients.Occupant;
import uy.com.innobit.rem.persistence.datamodel.clients.Owner;
import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.persistence.datamodel.property.PropertyDocument;
import uy.com.innobit.rem.persistence.datamodel.property.PropertyNotification;
import uy.com.innobit.rem.persistence.datamodel.property.PropertyPicture;
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

	public synchronized List<Property> getByOwner(Owner owner) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("owner", owner));
		List<Property> list = DBEntityManagerFactory.get(Property.class).getByCriterion(criterias);
		return list;
	}

	public synchronized List<Property> getAll() {
		return DBEntityManagerFactory.get(Property.class).getAll();

	}

	public synchronized void saveProperty(Property entity) {
		DBEntityManagerFactory.get(Property.class).saveEntity(entity);
	}

	public synchronized void updateProperty(Property entity) {
		DBEntityManagerFactory.get(Property.class).updateEntity(entity);
	}

	public synchronized void deleteProperty(Property entity) {
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

	public synchronized void delete(PropertyPicture entity) {
		DBEntityManagerFactory.get(PropertyPicture.class).delete(entity);
	}

	public synchronized void save(PropertyPicture document) {
		DBEntityManagerFactory.get(PropertyPicture.class).saveEntity(document);

	}

	public synchronized void update(PropertyPicture document) {
		DBEntityManagerFactory.get(PropertyPicture.class).updateEntity(document);

	}

	public synchronized void updateReminder(PropertyNotification document) {
		DBEntityManagerFactory.get(PropertyNotification.class).updateEntity(document);

	}

	public synchronized void deleteReminder(PropertyNotification entity) {
		DBEntityManagerFactory.get(PropertyNotification.class).delete(entity);
	}

	public synchronized void saveReminder(PropertyNotification document) {
		document.setLogoUrl((VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/WEB-INF/logo.png"));
		DBEntityManagerFactory.get(PropertyNotification.class).saveEntity(document);

	}

	public List<Property> getByOccupant(Occupant occupant) {
		List<Property> result = new ArrayList<Property>();
		for (Contract c : ContractManager.getInstance().getByOccupant(occupant))
			if (!result.contains(c.getProperty()))
				result.add(c.getProperty());
		return result;
	}

}

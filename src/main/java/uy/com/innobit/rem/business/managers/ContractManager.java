package uy.com.innobit.rem.business.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.vaadin.server.VaadinService;

import uy.com.innobit.rem.persistence.datamodel.clients.Occupant;
import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractCharge;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractDocument;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractEntry;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractExpiration;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractNotification;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractPayment;
import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.persistence.util.DBEntityManagerFactory;

public class ContractManager {
	private static ContractManager instance;

	private ContractManager() {
	}

	public static ContractManager getInstance() {
		if (instance == null)
			instance = new ContractManager();
		return instance;

	}

	public synchronized List<Contract> getAll() {
		return DBEntityManagerFactory.get(Contract.class).getAll();

	}

	public synchronized Contract getById(Integer id) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("id", id));
		List<Contract> list = DBEntityManagerFactory.get(Contract.class).getByCriterion(criterias);
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	public synchronized ContractEntry getEntryByID(Integer id) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("id", id));
		List<ContractEntry> list = DBEntityManagerFactory.get(ContractEntry.class).getByCriterion(criterias);
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);
	}

	public synchronized List<ContractExpiration> getContractActualExpirations(String currency) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		Date aux = new Date();
		Date end = new Date(aux.getYear(), 0, 1, 0, 0, 0);
		Date init = new Date(aux.getYear(), 0, 1, 0, 0, 0);
		end.setYear(end.getYear() + 1);
		criterias.add(Restrictions.eq("currency", currency));
		criterias.add(Restrictions.lt("expectedDate", end));
		criterias.add(Restrictions.ge("expectedDate", init));
		List<ContractExpiration> list = DBEntityManagerFactory.get(ContractExpiration.class).getByCriterion(criterias);
		return list;
	}

	public synchronized List<ContractEntry> getContractEntries(Date init, Date end, String currency) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		if (init != null)
			criterias.add(Restrictions.ge("init", init));
		if (end != null)
			criterias.add(Restrictions.le("init", end));
		if (currency != null && !currency.equalsIgnoreCase(""))
			criterias.add(Restrictions.eq("currency", currency));
		List<String> order = new ArrayList<String>();
		order.add("init");
		List<ContractEntry> list = DBEntityManagerFactory.get(ContractEntry.class).getByCriterionAndAscOrder(criterias,
				order);
		return list;
	}

	public synchronized void saveContract(Contract contract) {
		DBEntityManagerFactory.get(Contract.class).saveEntity(contract);
	}

	public synchronized void updateContract(Contract contract) {
		DBEntityManagerFactory.get(Contract.class).updateEntity(contract);
	}

	public synchronized void updateEntry(ContractEntry entry) {
		DBEntityManagerFactory.get(ContractEntry.class).updateEntity(entry);
	}

	public synchronized void deleteEntry(ContractEntry entry) {
		DBEntityManagerFactory.get(ContractEntry.class).delete(entry);
	}

	public void saveContractReminder(ContractNotification p) {
		p.setLogoUrl((VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/WEB-INF/logo.png"));
		DBEntityManagerFactory.get(ContractNotification.class).saveEntity(p);
	}

	public void deleteContractReminder(ContractNotification p) {
		DBEntityManagerFactory.get(ContractNotification.class).delete(p);
	}

	public synchronized void deleteContract(Contract contract) {
		DBEntityManagerFactory.get(Contract.class).delete(contract);
	}

	public void saveContractCharge(ContractCharge p) {
		DBEntityManagerFactory.get(ContractCharge.class).saveEntity(p);
	}

	public void deleteContractCharge(ContractCharge p) {
		DBEntityManagerFactory.get(ContractCharge.class).delete(p);
	}

	public void saveContractPayment(ContractPayment p) {
		DBEntityManagerFactory.get(ContractPayment.class).saveEntity(p);
	}

	public void delete(ContractExpiration p) {
		DBEntityManagerFactory.get(ContractExpiration.class).delete(p);
	}

	public void update(ContractExpiration p) {
		DBEntityManagerFactory.get(ContractExpiration.class).updateEntity(p);
	}

	public void deleteContractPayment(ContractPayment p) {
		DBEntityManagerFactory.get(ContractPayment.class).delete(p);

	}

	public void saveContractDocument(ContractDocument p) {
		DBEntityManagerFactory.get(ContractDocument.class).saveEntity(p);
	}

	public void deleteContractDocument(ContractDocument p) {
		DBEntityManagerFactory.get(ContractDocument.class).delete(p);
	}

	public void save(ContractEntry p) {
		DBEntityManagerFactory.get(ContractEntry.class).saveEntity(p);
	}

	public void delete(ContractEntry p) {
		DBEntityManagerFactory.get(ContractEntry.class).delete(p);
	}

	public List<Contract> getByOccupant(Occupant occupant) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("occupant", occupant));
		List<Contract> list = DBEntityManagerFactory.get(Contract.class).getByCriterion(criterias);
		return list;
	}

	public List<ContractCharge> getActualYearCharges() {
		List<Criterion> criterias = new ArrayList<Criterion>();
		Date now = new Date();
		Date init = new Date(now.getYear(), 0, 1, 0, 0, 0);
		Date end = new Date(now.getYear() + 1, 0, 1, 0, 0, 0);
		criterias.add(Restrictions.ge("paymentDate", init));
		criterias.add(Restrictions.lt("paymentDate", end));
		List<ContractCharge> list = DBEntityManagerFactory.get(ContractCharge.class).getByCriterion(criterias);
		return list;
	}
	
	

	public synchronized Contract initialize(Contract contract) {
		final Criteria criteria = DBEntityManagerFactory.get(Contract.class).getSessionManager().getSession()
				.createCriteria(Contract.class);
		criteria.add(Restrictions.eq("id", contract.getId()));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List<Contract> list = new ArrayList<Contract>(new HashSet(criteria.list()));
		Contract p = list.get(0);
		Hibernate.initialize(p.getDocuments());

		// Commit session changes.
		if (DBEntityManagerFactory.get(Contract.class).getSessionManager().getSession().getTransaction().isActive()) {
			DBEntityManagerFactory.get(Contract.class).getSessionManager().getSession().getTransaction().commit();
		}
		return p;
	}

}

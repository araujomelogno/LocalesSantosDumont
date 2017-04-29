package uy.com.innobit.rem.business.managers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
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

	public synchronized void saveContract(Contract contract) {
		DBEntityManagerFactory.get(Contract.class).saveEntity(contract);
	}

	public synchronized void updateContract(Contract contract) {
		DBEntityManagerFactory.get(Contract.class).updateEntity(contract);
	}

	public synchronized void deleteContract(Contract contract) {
		DBEntityManagerFactory.get(Contract.class).delete(contract);
	}
}

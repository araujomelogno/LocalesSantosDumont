package uy.com.innobit.rem.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uy.com.equipos.informes.persistence.util.invoke.HibernateUtil;
import uy.com.innobit.rem.business.managers.ContractManager;
import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractEntry;
import uy.com.innobit.rem.persistence.util.HibernateSessionFactory;
import uy.com.innobit.rem.persistence.util.HibernateSessionManager;

public class ContractEntriesFixer {

	public static void main(String... args) {
		try {
			HibernateSessionManager.init();
			HibernateSessionFactory.getSessionFactory();
			HibernateUtil.getSessionFactory();
			ContractEntriesFixer t = new ContractEntriesFixer();
			t.fix();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fix() throws Exception {
		List<Contract> contracts = ContractManager.getInstance().getAll();
		for (Contract contract : contracts) {
			Date init = contract.getInit();
			Date end = contract.getEnd();
			int years = (end.getYear() - init.getYear()) + 1;
			List<ContractEntry> toremove = new ArrayList<>();
			for (ContractEntry centry : contract.getEntries()) {
				Integer aux = centry.getYearIndex() - 1;
				Date auxInitDate = new Date(init.getTime());
				auxInitDate.setYear(auxInitDate.getYear() + aux);
				Date auxEndDate = new Date(auxInitDate.getTime());
				if (years == centry.getYearIndex() + 1) {
					auxEndDate = contract.getEnd();
				} else {

					auxEndDate.setDate(auxInitDate.getDate() + 364);
					// auxEndDate.setYear(auxInitDate.getYear() + aux);
				}
				if (centry.getYearIndex() > years)
					toremove.add(centry);
				else {
					centry.setInit(auxInitDate);
					centry.setEnd(auxEndDate);
					ContractManager.getInstance().updateEntry(centry);
				}

			}
			for (ContractEntry ce : toremove) {
				contract.getEntries().remove(ce);
				ContractManager.getInstance().deleteEntry(ce);
			}
			ContractManager.getInstance().updateContract(contract);
		}
	}
}

package uy.com.innobit.rem.persistence.datamodel.billing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillingManager {
	private static BillingManager instance;

	private BillingManager() {
	}

	public static synchronized BillingManager getInstance() {
		if (instance == null)
			instance = new BillingManager();
		return instance;
	}

	public synchronized List<BillingDataValue> getBilling(Date init, Date end) {
		List<BillingDataValue> result = new ArrayList<BillingDataValue>();

		BillingDataValue bill1 = new BillingDataValue();
		bill1.setAmount(7800);
		bill1.setAmountPaied(5000);
		bill1.setOccupantComission(1000);
		bill1.setOwnerCommission(1000);
		bill1.setOccupantComissionCharged(800);
		bill1.setOwnerComissionCharged(1000);
		bill1.setInit("01/02/2015");
		bill1.setEnd("01/02/2016");
		bill1.setOccupant("Martin Roballo");
		bill1.setOwner("Ines Carvajal");
		bill1.setProperty("La cachimba/Pde");
		result.add(bill1);

		BillingDataValue bill2 = new BillingDataValue();
		bill2.setAmount(4000);
		bill2.setAmountPaied(4000);
		bill2.setOccupantComission(500);
		bill2.setOwnerCommission(500);
		bill2.setOccupantComissionCharged(500);
		bill2.setOwnerComissionCharged(0);
		bill2.setInit("01/07/2015");
		bill2.setEnd("01/07/2016");
		bill2.setOccupant("Eugenia Olaiz");
		bill2.setOwner("Carlos Esponda");
		bill2.setProperty("El Mojón/Pde");
		result.add(bill2);

		BillingDataValue bill3 = new BillingDataValue();
		bill3.setAmount(1500);
		bill3.setAmountPaied(1500);
		bill3.setOccupantComission(400);
		bill3.setOwnerCommission(0);
		bill3.setOccupantComissionCharged(200);
		bill3.setOwnerComissionCharged(0);
		bill3.setInit("01/10/2015");
		bill3.setEnd("01/10/2016");
		bill3.setOccupant("Francisco Portela");
		bill3.setOwner("Guzman Solari");
		bill3.setProperty("El Triangulo/A705");
		result.add(bill3);

		return result;
	}
}

package uy.com.innobit.rem.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.lob.SerializableBlob;
import org.hibernate.type.BlobType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;

import uy.com.equipos.informes.persistence.util.invoke.HibernateUtil;
import uy.com.innobit.rem.business.managers.ContractManager;
import uy.com.innobit.rem.business.managers.OccupantManager;
import uy.com.innobit.rem.business.managers.OwnerManager;
import uy.com.innobit.rem.business.managers.PropertyManager;
import uy.com.innobit.rem.persistence.datamodel.clients.Occupant;
import uy.com.innobit.rem.persistence.datamodel.clients.Owner;
import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractCharge;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractDocument;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractEntry;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractNotification;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractPayment;
import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.persistence.datamodel.property.PropertyDocument;
import uy.com.innobit.rem.persistence.util.HibernateSessionFactory;
import uy.com.innobit.rem.persistence.util.HibernateSessionManager;

public class Translator {

	public static void main(String[] args) {
		try {
			HibernateSessionManager.init();
			HibernateSessionFactory.getSessionFactory();
			HibernateUtil.getSessionFactory();
			Translator t = new Translator();
			t.createContractNotifications();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createClients() throws Exception {
		List<Object[]> aux = TranslatorManager.getInstance().getclients();
		for (Object[] a : aux) {
			// id,name,cell,surname,tel,mail,rut,address,obs,socialReason,
			// ci
			System.out.println(a[0]);
			Integer id = Integer.parseInt(a[0].toString());
			String name = a[1].toString();
			String cell = a[2].toString();
			String surname = a[3].toString();
			String tel = a[4].toString();
			String mail = a[5].toString();
			String rut = a[6].toString();
			String address = a[7].toString();
			String obs = a[8].toString();
			String socialReason = "";
			if (a[9] != null)
				socialReason = a[9].toString();

			String ci = "";
			if (a[10] != null)
				ci = a[10].toString();

			Occupant oc = new Occupant();

			oc.setName(name);
			oc.setSurname(surname);
			oc.setCell(cell);
			oc.setTel(tel);
			oc.setMail(mail);
			oc.setRut(rut);
			oc.setAddress(address);
			oc.setObs(obs);
			oc.setSocialReason(socialReason);
			oc.setDoc(ci);
			oc.setId(id);
			OccupantManager.getInstance().saveUser(oc);

		}

	}

	public void createContracts() throws Exception {
		List<Object[]> auxdata = TranslatorManager.getInstance().getContracts();
		for (Object[] a : auxdata) {
			// id,name,cell,surname,tel,mail,rut,address,obs,socialReason,
			// ci
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
			System.out.println(a[0]);
			Integer id = Integer.parseInt(a[0].toString());
			Date init = sdf.parse(a[1].toString());
			Date end = sdf.parse(a[2].toString());
			Boolean cede = (boolean) a[3];
			Boolean warranty = (boolean) a[4];
			String warrantyType = a[5].toString();
			Integer clientId = Integer.parseInt(a[7].toString());
			Integer localID = Integer.parseInt(a[6].toString());
			Integer notDays = Integer.parseInt(a[8].toString());
			// crear notificación
			String obs = a[9].toString();

			Contract c = new Contract();
			c.setId(id);
			c.setInit(init);
			c.setEnd(end);
			c.setCede(cede);
			c.setWarranty(warranty);
			c.setWarrantyType(warrantyType);
			c.setNotdays(notDays);
			c.setObs(obs);

			Occupant oc = OccupantManager.getInstance().getById(clientId);
			Property p = PropertyManager.getInstance().getById(localID);
			c.setOccupant(oc);
			c.setProperty(p);
			ContractManager.getInstance().saveContract(c);

			// Date aux = new Date(end.getTime());
			// try {
			// aux.setDate(aux.getDate() - notDays);
			// } catch (Exception e) {
			// }
			// ContractNotification cn = new ContractNotification();
			// cn.setContract(c);
			// cn.setMessage(
			// "El contrato de la propiedad " + c.getPropertyName() + " se
			// vencerá el " + sdf.format(c.getEnd()));
			// cn.setNotificationDate(aux);
			// cn.getRecipients().add("ivonne@santosdumont.com.uy");
			// ContractManager.getInstance().saveContractReminder(cn);
			// c.getNotifications().add(cn);
			// ContractManager.getInstance().updateContract(c);
		}

	}

	public void createContractEntries() throws Exception {
		List<Object[]> auxdata = TranslatorManager.getInstance().getContractsEntries();
		for (Object[] a : auxdata) {
			// id,name,cell,surname,tel,mail,rut,address,obs,socialReason,
			// ci
			// .addScalar("id", new StringType()).addScalar("year", new
			// StringType())
			// .addScalar("amount", new
			// DoubleType()).addScalar("ownerComission", new DoubleType())
			// .addScalar("clientComission", new
			// DoubleType()).addScalar("contractId", new StringType())
			// .addScalar("active", new BooleanType()).addScalar("init", new
			// StringType())
			// .addScalar("end", new StringType());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
			System.out.println(a[0]);
			Integer id = Integer.parseInt(a[0].toString());
			Integer year = Integer.parseInt(a[1].toString());
			Double amount = (Double) a[2];
			Double ownerComission = (Double) a[3];
			Double clientComission = (Double) a[4];
			Integer contractId = Integer.parseInt(a[5].toString());
			Boolean active = (Boolean) a[6];
			Date init = sdf.parse(a[7].toString());
			Date end = sdf.parse(a[8].toString());

			ContractEntry c = new ContractEntry();
			c.setId(id);
			c.setYearIndex(year);
			c.setAmount(amount);
			c.setOwnerComission(ownerComission);
			c.setClientComission(clientComission);
			c.setCurrency("$");
			c.setActive(active);
			c.setInit(init);
			c.setEnd(end);
			Contract contract = ContractManager.getInstance().getById(contractId);
			c.setContract(contract);
			ContractManager.getInstance().save(c);
			contract.getEntries().add(c);
			ContractManager.getInstance().updateContract(contract);
			if (c.isActive()) {

				Date aux = new Date(end.getTime());
				try {
					aux.setDate(aux.getDate() - contract.getNotdays());
				} catch (Exception e) {
				}
				ContractNotification cn = new ContractNotification();
				cn.setContract(contract);
				cn.setMessage("El contrato de la propiedad " + contract.getPropertyName() + " se vencerá el "
						+ c.getEndSDF());
				cn.setNotificationDate(aux);
				cn.getRecipients().add("ivonne@santosdumont.com.uy");
				ContractManager.getInstance().saveContractReminder(cn);
				contract.getNotifications().add(cn);
				ContractManager.getInstance().updateContract(contract);

			}
		}

	}

	public void createProperties() throws Exception {
		List<Object[]> aux = TranslatorManager.getInstance().getProperties();
		for (Object[] a : aux) {
			// id,name,cell,surname,tel,mail,rut,address,obs,socialReason,
			// ci
			System.out.println(a[0]);
			Integer id = Integer.parseInt(a[0].toString());
			String name = a[1].toString();
			String address = a[2].toString();
			String padron = a[3].toString();
			String block = a[4].toString();
			String refUte = a[5].toString();
			String refAgua = a[6].toString();
			String size = a[7].toString();
			Boolean expenses = (Boolean) a[8];

			Integer expensesAmount = 0;
			if (!a[9].toString().isEmpty())
				expensesAmount = Integer.parseInt(a[9].toString());

			String expensesFreq = "";
			if (a[10] != null)
				expensesFreq = a[10].toString();
			String obs = a[11].toString();
			String tel = a[12].toString();
			Integer ownerId = Integer.parseInt(a[13].toString());
			String number = "";
			if (a[14] != null)
				number = a[14].toString();
			Property p = new Property();
			p.setId(id);
			p.setName(name);
			p.setAddress(address);
			p.setPadron(padron);
			p.setBlock(block);
			p.setRefUte(refUte);
			p.setRefAgua(refAgua);
			p.setSize(size);
			p.setPayExpenses(expenses);
			p.setExpenses(expensesAmount);
			p.setExpensesFreq(expensesFreq);
			p.setObs(obs);
			p.setTel(tel);
			p.setNro(number);
			Owner owner = OwnerManager.getInstance().getById(ownerId);
			p.setOwner(owner);
			PropertyManager.getInstance().saveProperty(p);

		}

	}

	public void createContractNotifications() throws Exception {
		List<Object[]> aux = TranslatorManager.getInstance().getContractNotifications();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object[] a : aux) {
			// .addScalar("id", new StringType()).addScalar("notificationdate",
			// new StringType())
			// .addScalar("message", new StringType()).addScalar("name", new
			// StringType())
			// .addScalar("resolved", new BooleanType()).addScalar("contracid",
			// new StringType());
			System.out.println(a[0]);
			Integer id = Integer.parseInt(a[0].toString());
			Date notificationdate = sdf.parse(a[1].toString());
			String message = a[2].toString();
			String subject = a[3].toString();
			Integer contractId = Integer.parseInt(a[5].toString());

			Contract c = ContractManager.getInstance().getById(contractId);
			ContractNotification cn = new ContractNotification();
			cn.setId(id);
			cn.setMessage(message);
			cn.setNotificationDate(notificationdate);
			cn.setResolved(false);
			cn.setSent(true);
			cn.setContract(c);
			cn.getRecipients().add("ivonne@santosdumont.com.uy");
			ContractManager.getInstance().saveContractReminder(cn);
			c.getNotifications().add(cn);
			ContractManager.getInstance().updateContract(c);
		}

	}

	public void createCharges() throws Exception {
		List<Object[]> aux = TranslatorManager.getInstance().getCharges();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object[] a : aux) {
			// .addScalar("id", new StringType()).addScalar("type", new
			// StringType())
			// .addScalar("origin", new StringType()).addScalar("bank", new
			// StringType())
			// .addScalar("checknumber", new
			// StringType()).addScalar("checkscan", new BlobType())
			// .addScalar("expirationDate", new
			// StringType()).addScalar("amount", new DoubleType())
			// .addScalar("obs", new StringType()).addScalar("amountId", new
			// StringType())
			// .addScalar("comission", new BooleanType());
			System.out.println(a[0]);
			Integer id = Integer.parseInt(a[0].toString());
			String type = a[1].toString();
			byte[] yourBytes = null;
			String origin = a[2].toString();
			String bank = a[3].toString();
			String checknumber = a[4].toString();
			SerializableBlob checkscan = (SerializableBlob) a[5];
			if (checkscan != null) {

				int blobLength = (int) checkscan.length();
				yourBytes = checkscan.getBytes(1, blobLength);

			}
			Date expDate = null;
			if (a[6] != null)
				expDate = sdf.parse(a[6].toString());
			Double amount = (Double) a[7];
			Date paymentDate = sdf.parse(a[8].toString());
			String obs = a[9].toString();
			Integer entryId = Integer.parseInt(a[10].toString());
			Boolean comission = (Boolean) a[11];

			ContractCharge c = new ContractCharge();
			c.setId(id);

			c.setType(type);
			c.setSource(origin);
			c.setBank(bank);
			c.setCheckDate(expDate);
			c.setCheckNumber(checknumber);
			c.setCheckImage(yourBytes);
			c.setAmount(amount);
			c.setPaymentDate(paymentDate);
			c.setCommission(comission);
			c.setCurrency("$");
			c.setObs(obs);
			ContractEntry ce = ContractManager.getInstance().getEntryByID(entryId);
			c.setEntry(ce);
			ContractManager.getInstance().saveContractCharge(c);
			ce.getContractCharges().add(c);
			ContractManager.getInstance().updateEntry(ce);

		}

	}

	public void createPayments() throws Exception {
		List<Object[]> aux = TranslatorManager.getInstance().getpayment();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object[] a : aux) {
			// .addScalar("id", new StringType()).addScalar("type", new
			// StringType())
			// .addScalar("bank", new StringType()).addScalar("checknumber", new
			// StringType())
			// .addScalar("checksacn", new
			// BlobType()).addScalar("expirationDate", new StringType())
			// .addScalar("amount", new DoubleType()).addScalar("paymentDate",
			// new StringType())
			// .addScalar("obs", new StringType()).addScalar("amountid", new
			// StringType());
			System.out.println(a[0]);
			Integer id = Integer.parseInt(a[0].toString());
			String type = a[1].toString();
			String bank = null;
			if (a[2] != null)
				bank = a[2].toString();
			String checknumber = null;
			byte[] yourBytes = null;
			if (a[3] != null)
				checknumber = a[3].toString();
			SerializableBlob checkscan = (SerializableBlob) a[4];
			if (checkscan != null) {

				int blobLength = (int) checkscan.length();
				yourBytes = checkscan.getBytes(1, blobLength);

			}
			Date expDate = null;
			if (a[5] != null)
				expDate = sdf.parse(a[5].toString());
			Double amount = (Double) a[6];
			Date paymentDate = sdf.parse(a[7].toString());
			String obs = a[8].toString();
			Integer entryId = Integer.parseInt(a[9].toString());

			ContractPayment c = new ContractPayment();
			c.setId(id);

			c.setType(type);
			c.setBank(bank);
			c.setCheckDate(expDate);
			c.setCheckNumber(checknumber);
			c.setCheckImage(yourBytes);
			c.setAmount(amount);
			c.setPaymentDate(paymentDate);
			c.setCurrency("$");
			c.setObs(obs);
			ContractEntry ce = ContractManager.getInstance().getEntryByID(entryId);
			if (ce != null) {

				c.setEntry(ce);
				ContractManager.getInstance().saveContractPayment(c);
				ce.getPayments().add(c);
				ContractManager.getInstance().updateEntry(ce);

			} else {
				System.out.println("sin conract entryj");
			}
		}

	}

	public void createContractdocument() throws Exception {
		List<Object[]> aux = TranslatorManager.getInstance().getContractDocuments();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object[] a : aux) {
			// .addScalar("id", new StringType()).addScalar("type", new
			// StringType())
			// .addScalar("origin", new StringType()).addScalar("bank", new
			// StringType())
			// .addScalar("checknumber", new
			// StringType()).addScalar("checkscan", new BlobType())
			// .addScalar("expirationDate", new
			// StringType()).addScalar("amount", new DoubleType())
			// .addScalar("obs", new StringType()).addScalar("amountId", new
			// StringType())
			// .addScalar("comission", new BooleanType());
			System.out.println(a[0]);
			Integer id = Integer.parseInt(a[0].toString());
			String name = a[1].toString();
			SerializableBlob checkscan = (SerializableBlob) a[2];

			int blobLength = (int) checkscan.length();
			byte[] yourBytes = checkscan.getBytes(1, blobLength);

			Integer contractId = Integer.parseInt(a[3].toString());

			ContractDocument doc = new ContractDocument();
			doc.setContent(yourBytes);
			doc.setId(id);
			doc.setName(name);
			ContractManager.getInstance().saveContractDocument(doc);
			Contract c = ContractManager.getInstance().getById(contractId);
			c.getDocuments().add(doc);
			ContractManager.getInstance().updateContract(c);

		}

	}

	public void createlocalDocument() throws Exception {
		List<Object[]> aux = TranslatorManager.getInstance().getLocalDocument();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object[] a : aux) {
			// .addScalar("id", new StringType()).addScalar("type", new
			// StringType())
			// .addScalar("origin", new StringType()).addScalar("bank", new
			// StringType())
			// .addScalar("checknumber", new
			// StringType()).addScalar("checkscan", new BlobType())
			// .addScalar("expirationDate", new
			// StringType()).addScalar("amount", new DoubleType())
			// .addScalar("obs", new StringType()).addScalar("amountId", new
			// StringType())
			// .addScalar("comission", new BooleanType());
			System.out.println(a[0]);
			Integer id = Integer.parseInt(a[0].toString());
			String name = a[1].toString();
			SerializableBlob checkscan = (SerializableBlob) a[2];

			int blobLength = (int) checkscan.length();
			byte[] yourBytes = checkscan.getBytes(1, blobLength);
			Integer propertyId = Integer.parseInt(a[3].toString());

			PropertyDocument doc = new PropertyDocument();
			doc.setContent(yourBytes);
			doc.setId(id);
			doc.setName(name);
			PropertyManager.getInstance().saveDocument(doc);

			Property p = PropertyManager.getInstance().getById(propertyId);
			p.getDocuments().add(doc);
			PropertyManager.getInstance().updateProperty(p);
		}

	}

	public void createOwners() throws Exception {
		List<Object[]> aux = TranslatorManager.getInstance().getOwners();
		for (Object[] a : aux) {
			// id,name,cell,surname,tel,mail,rut,address,obs,socialReason,
			// ci
			System.out.println(a[0]);
			Integer id = Integer.parseInt(a[0].toString());
			String name = a[1].toString();
			String cell = a[2].toString();
			String surname = a[3].toString();
			String tel = a[4].toString();
			String mail = a[5].toString();
			String rut = a[6].toString();
			String address = "";
			if (a[7] != null)
				address = a[7].toString();
			String obs = a[8].toString();
			String socialReason = "";
			if (a[9] != null)
				socialReason = a[9].toString();

			String ci = "";
			if (a[10] != null)
				ci = a[10].toString();

			Owner oc = new Owner();

			oc.setName(name);
			oc.setSurname(surname);
			oc.setCell(cell);
			oc.setTel(tel);
			oc.setMail(mail);
			oc.setRut(rut);
			oc.setAddress(address);
			oc.setObs(obs);
			oc.setSocialReason(socialReason);
			oc.setDoc(ci);
			oc.setId(id);
			OwnerManager.getInstance().saveUser(oc);

		}

	}

}

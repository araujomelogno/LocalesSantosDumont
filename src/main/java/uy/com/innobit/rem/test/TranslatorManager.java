package uy.com.innobit.rem.test;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.BlobType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;

import uy.com.equipos.informes.persistence.util.invoke.HibernateUtil;

public class TranslatorManager {
	public static final Logger log = LogManager.getLogger(TranslatorManager.class.getName());
	private static TranslatorManager instance;

	private TranslatorManager() {
	}

	public static TranslatorManager getInstance() {
		if (instance == null)
			instance = new TranslatorManager();
		return instance;
	}

	public List<Object[]> getclients() throws Exception {
		Transaction t = null;
		try {
			Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			if (!HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				s = HibernateUtil.getSessionFactory().openSession();
			t = s.beginTransaction();
			String sql_query = "select id,name,cell,surname,tel,mail,rut,address,obs,socialReason, ci from client";
			Query query1 = HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql_query)
					.addScalar("id", new StringType()).addScalar("name", new StringType())
					.addScalar("cell", new StringType()).addScalar("surname", new StringType())
					.addScalar("tel", new StringType()).addScalar("mail", new StringType())
					.addScalar("rut", new StringType()).addScalar("address", new StringType())
					.addScalar("obs", new StringType()).addScalar("socialReason", new StringType())
					.addScalar("ci", new StringType());
			List amos = query1.list();
			t.commit();

			return amos;
		} catch (Exception e) {
			log.error(e.getMessage());
			if (t != null)
				t.rollback();
			throw new Exception(e.getMessage());
		} finally {
			if (HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				HibernateUtil.getSessionFactory().getCurrentSession().close();
		}
	}

	public List<Object[]> getCharges() throws Exception {
		Transaction t = null;
		try {
			Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			if (!HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				s = HibernateUtil.getSessionFactory().openSession();
			t = s.beginTransaction();
			String sql_query = "select id,type,origin,bank,checknumber,chekscan,expirationDate,amount, paymentDate,obs,amountId,comission from charge";
			Query query1 = HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql_query)
					.addScalar("id", new StringType()).addScalar("type", new StringType())
					.addScalar("origin", new StringType()).addScalar("bank", new StringType())
					.addScalar("checknumber", new StringType()).addScalar("chekscan", new BlobType())
					.addScalar("expirationDate", new StringType()).addScalar("amount", new DoubleType())
					.addScalar("paymentDate", new StringType()).addScalar("obs", new StringType())
					.addScalar("amountId", new StringType()).addScalar("comission", new BooleanType());
			List amos = query1.list();
			t.commit();

			return amos;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (t != null)
				t.rollback();
			throw new Exception(e.getMessage());
		} finally {
			if (HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				HibernateUtil.getSessionFactory().getCurrentSession().close();
		}
	}

	public List<Object[]> getpayment() throws Exception {
		Transaction t = null;
		try {
			Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			if (!HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				s = HibernateUtil.getSessionFactory().openSession();
			t = s.beginTransaction();
			String sql_query = "select id,type,bank,checknumber,checksacn,expirationDate,ammount, paymentDate,obs,amountid from payment";
			Query query1 = HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql_query)
					.addScalar("id", new StringType()).addScalar("type", new StringType())
					.addScalar("bank", new StringType()).addScalar("checknumber", new StringType())
					.addScalar("checksacn", new BlobType()).addScalar("expirationDate", new StringType())
					.addScalar("ammount", new DoubleType()).addScalar("paymentDate", new StringType())
					.addScalar("obs", new StringType()).addScalar("amountid", new StringType());
			List amos = query1.list();
			t.commit();

			return amos;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (t != null)
				t.rollback();
			throw new Exception(e.getMessage());
		} finally {
			if (HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				HibernateUtil.getSessionFactory().getCurrentSession().close();
		}
	}

	public List<Object[]> getContractDocuments() throws Exception {
		Transaction t = null;
		try {
			Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			if (!HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				s = HibernateUtil.getSessionFactory().openSession();
			t = s.beginTransaction();
			String sql_query = "select id,name,content,contractid from contractdocument";
			Query query1 = HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql_query)
					.addScalar("id", new StringType()).addScalar("name", new StringType())
					.addScalar("content", new BlobType()).addScalar("contractid", new StringType());
			List amos = query1.list();
			t.commit();

			return amos;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (t != null)
				t.rollback();
			throw new Exception(e.getMessage());
		} finally {
			if (HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				HibernateUtil.getSessionFactory().getCurrentSession().close();
		}
	}

	public List<Object[]> getLocalDocument() throws Exception {
		Transaction t = null;
		try {
			Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			if (!HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				s = HibernateUtil.getSessionFactory().openSession();
			t = s.beginTransaction();
			String sql_query = "select id,name,content,localid from document";
			Query query1 = HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql_query)
					.addScalar("id", new StringType()).addScalar("name", new StringType())
					.addScalar("content", new BlobType()).addScalar("localid", new StringType());
			List amos = query1.list();
			t.commit();

			return amos;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (t != null)
				t.rollback();
			throw new Exception(e.getMessage());
		} finally {
			if (HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				HibernateUtil.getSessionFactory().getCurrentSession().close();
		}
	}

	public List<Object[]> getContractNotifications() throws Exception {
		Transaction t = null;
		try {
			Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			if (!HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				s = HibernateUtil.getSessionFactory().openSession();
			t = s.beginTransaction();
			String sql_query = "select id,notificationDate,message,name,resolved,contractid from contractnotification where resolved = 0";
			Query query1 = HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql_query)
					.addScalar("id", new StringType()).addScalar("notificationdate", new StringType())
					.addScalar("message", new StringType()).addScalar("name", new StringType())
					.addScalar("resolved", new BooleanType()).addScalar("contractid", new StringType());
			List amos = query1.list();
			t.commit();

			return amos;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (t != null)
				t.rollback();
			throw new Exception(e.getMessage());
		} finally {
			if (HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				HibernateUtil.getSessionFactory().getCurrentSession().close();
		}
	}

	public List<Object[]> getContracts() throws Exception {
		Transaction t = null;
		try {
			Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			if (!HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				s = HibernateUtil.getSessionFactory().openSession();
			t = s.beginTransaction();
			String sql_query = "select id,init,end,CEDE,warranty,warrantyType,localid,clientid,notdays,obs from contract";
			Query query1 = HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql_query)
					.addScalar("id", new StringType()).addScalar("init", new StringType())
					.addScalar("end", new StringType()).addScalar("CEDE", new BooleanType())
					.addScalar("warranty", new BooleanType()).addScalar("warrantyType", new StringType())
					.addScalar("localid", new StringType()).addScalar("clientid", new StringType())
					.addScalar("notdays", new StringType()).addScalar("obs", new StringType());
			List amos = query1.list();
			t.commit();

			return amos;
		} catch (Exception e) {
			log.error(e.getMessage());
			if (t != null)
				t.rollback();
			throw new Exception(e.getMessage());
		} finally {
			if (HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				HibernateUtil.getSessionFactory().getCurrentSession().close();
		}
	}

	public List<Object[]> getContractsEntries() throws Exception {
		Transaction t = null;
		try {
			Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			if (!HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				s = HibernateUtil.getSessionFactory().openSession();
			t = s.beginTransaction();
			String sql_query = "select id,year,amount,ownerComission,clientComission,contractId,active,init,end from amount";
			Query query1 = HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql_query)
					.addScalar("id", new StringType()).addScalar("year", new StringType())
					.addScalar("amount", new DoubleType()).addScalar("ownerComission", new DoubleType())
					.addScalar("clientComission", new DoubleType()).addScalar("contractId", new StringType())
					.addScalar("active", new BooleanType()).addScalar("init", new StringType())
					.addScalar("end", new StringType());
			List amos = query1.list();
			t.commit();

			return amos;
		} catch (Exception e) {
			log.error(e.getMessage());
			if (t != null)
				t.rollback();
			throw new Exception(e.getMessage());
		} finally {
			if (HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				HibernateUtil.getSessionFactory().getCurrentSession().close();
		}
	}

	public List<Object[]> getProperties() throws Exception {
		Transaction t = null;
		try {
			Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			if (!HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				s = HibernateUtil.getSessionFactory().openSession();
			t = s.beginTransaction();
			String sql_query = "select id,name,address,padron, block,refUte,refAgua,size,expenses,expensesAmount, expensesFreq,obs,tel,ownerId,number from local";
			Query query1 = HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql_query)
					.addScalar("id", new StringType()).addScalar("name", new StringType())
					.addScalar("address", new StringType()).addScalar("padron", new StringType())
					.addScalar("block", new StringType()).addScalar("refUte", new StringType())
					.addScalar("refAgua", new StringType()).addScalar("size", new StringType())
					.addScalar("expenses", new BooleanType()).addScalar("expensesAmount", new StringType())
					.addScalar("expensesFreq", new StringType()).addScalar("obs", new StringType())
					.addScalar("tel", new StringType()).addScalar("ownerId", new StringType())
					.addScalar("number", new StringType());
			List amos = query1.list();
			t.commit();

			return amos;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			if (t != null)
				t.rollback();
			throw new Exception(e.getMessage());
		} finally {
			if (HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				HibernateUtil.getSessionFactory().getCurrentSession().close();
		}
	}

	public List<Object[]> getOwners() throws Exception {
		Transaction t = null;
		try {
			Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			if (!HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				s = HibernateUtil.getSessionFactory().openSession();
			t = s.beginTransaction();
			String sql_query = "select id,name,cell,surname,tel,mail,rut,address,obs,socialReason, ci from owner";
			Query query1 = HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery(sql_query)
					.addScalar("id", new StringType()).addScalar("name", new StringType())
					.addScalar("cell", new StringType()).addScalar("surname", new StringType())
					.addScalar("tel", new StringType()).addScalar("mail", new StringType())
					.addScalar("rut", new StringType()).addScalar("address", new StringType())
					.addScalar("obs", new StringType()).addScalar("socialReason", new StringType())
					.addScalar("ci", new StringType());
			List amos = query1.list();
			t.commit();

			return amos;
		} catch (Exception e) {
			log.error(e.getMessage());
			if (t != null)
				t.rollback();
			throw new Exception(e.getMessage());
		} finally {
			if (HibernateUtil.getSessionFactory().getCurrentSession().isOpen())
				HibernateUtil.getSessionFactory().getCurrentSession().close();
		}
	}

}

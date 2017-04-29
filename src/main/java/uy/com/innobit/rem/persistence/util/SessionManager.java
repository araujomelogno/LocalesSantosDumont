package uy.com.innobit.rem.persistence.util;

import org.hibernate.Session;

public interface SessionManager {

	/**
	 * @return a Hibernate Session with open transaction
	 */
	Session getSession();

	Session openSession();

	void closeSession(Session session);
}

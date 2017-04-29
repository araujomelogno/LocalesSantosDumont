package uy.com.innobit.rem.persistence.util;

import java.io.Serializable;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public final class HibernateSessionFactory implements Serializable {

	private static final long serialVersionUID = -6344821642863804721L;

	/**
	 * Log.
	 */
	private static final Logger LOG = LogManager.getLogger(HibernateSessionFactory.class.toString());

	/**
	 * Provides only static methods.
	 */
	private HibernateSessionFactory() {
	}

	/**
	 * Hibernate session factory.
	 */
	private static SessionFactory sessionFactory;

	/**
	 * Initializes the session factory from the Hibernate configuration file
	 * <code>hibernate.cfg.xml</code> found in the classpath.
	 */
	private static void initSessionFactory() {

		try {
			// Builds Hibernate configuration.
			final AnnotationConfiguration cnf = new AnnotationConfiguration();
			cnf.configure("hibernate.cfg.xml");
			// Builds session factory from configuration.
			sessionFactory = cnf.buildSessionFactory();// new
														// Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
			
		} catch (final Throwable e) {

			LOG.fatal("Cannot configure or build Hibernate session factory.", e);
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * Gets the session factory singleton.
	 * 
	 * @return The session factory.
	 */
	public static SessionFactory getSessionFactory() {

		if (sessionFactory == null) {
			initSessionFactory();
		}
		return sessionFactory;
	}
}

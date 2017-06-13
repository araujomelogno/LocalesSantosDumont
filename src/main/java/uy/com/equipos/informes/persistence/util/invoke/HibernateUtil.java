package uy.com.equipos.informes.persistence.util.invoke;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static final SessionFactory sessionFactory;

	static {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			sessionFactory = new Configuration().configure(("hibernateInvoke.cfg.xml")).buildSessionFactory();
			System.out.println("SESSIONFACTORY CREADA");
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);

		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}

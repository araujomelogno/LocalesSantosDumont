package uy.com.innobit.rem.persistence.util;

import java.io.Serializable;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;

public final class HibernateSessionManager implements SessionManager,
Serializable {

private static final long serialVersionUID = 5255164035729835632L;

/**
* Log.
*/
private static final Logger LOG = LogManager
	.getLogger(HibernateSessionManager.class.toString());

/**
* Singleton.
*/
private static HibernateSessionManager singleton;

/**
* Initialize this session manager. This method must be call before any
* other method.
*/
public static void init() {
singleton = new HibernateSessionManager();
}

/**
* Gets the session manager singleton.<br/>
* <br/>
* The session manager must be initialized before calling this method.
* 
* @return the session manager.
* @see #init(Application);
*/
static synchronized HibernateSessionManager getInstance() {

if (singleton == null) {

	LOG.error("The session manager isn't instantiated yet.");
	throw new IllegalStateException(
			"The session manager must be initialized before getting its instance.");
}

return singleton;
}

/**
* Instantiates the singleton.
* 
*/
private HibernateSessionManager() {

if (LOG.isDebugEnabled()) {
	LOG.debug("Set up the session-per-request pattern with Hibernate sessions and Vaadin HTTP transactions.");
}

// Listen to HTTP transactions opening and closing events of Vaadin
// applications.
// Uses Vaadin transaction listener to ensure that session is closed on
// each request (session-per-request pattern).
}

/**
* Gets the current session with an opened transaction.
* 
* @return the current session with an opened transaction.
*/
@Override
public Session getSession() {

// Gets the current session.
final Session currentSession = HibernateSessionFactory
		.getSessionFactory().getCurrentSession();

// Opens a new transaction.
if (!currentSession.getTransaction().isActive()) {
	currentSession.beginTransaction();
}
return currentSession;
}

@Override
public synchronized Session openSession() {

	final Session newSession = HibernateSessionFactory
		.getSessionFactory().openSession();

// Opens a new transaction.
if (!newSession.getTransaction().isActive()) {
	newSession.beginTransaction();
}
return newSession;
}

/**
* Commit the current session transactions and closes it.
*/
private void closeSession() {

// Gets the current session.
final Session sess = HibernateSessionFactory.getSessionFactory()
		.getCurrentSession();

// Commit session changes.
if (sess.getTransaction().isActive()) {
	sess.getTransaction().commit();
}

// Closes session.
if (sess.isOpen()) {
	sess.close();
}
}

@Override
public synchronized void closeSession(Session sess) {

// Commit session changes.
if (sess.getTransaction().isActive()) {
	sess.getTransaction().commit();
}

// Closes session.
if (sess.isOpen()) {
	sess.close();
}
}
}

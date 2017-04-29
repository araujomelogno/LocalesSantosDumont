package uy.com.innobit.rem.persistence.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.metadata.ClassMetadata;

/**
 * Lazy, almost full-featured, general purpose Hibernate entity container. Makes
 * lots of queries, but shouldn't consume too much memory.
 * <p>
 * HbnContainer is developed and tested with session-per-request pattern, but
 * should work with other session handling mechanisms too. To abstract away
 * session handling, user must provide an implementation of SessionManager
 * interface, via HbnContainer fetches reference to Session instance whenever it
 * needs it. Session returned by HbnContainer is expected to be open.
 * 
 * <p>
 * In in its constructor it will only need entity class type (Pojo) and a
 * SessionManager.
 * <p>
 * HbnContainer also expects that identifiers are auto generated. This matters
 * only if HbnContainer is used to create new entities.
 * <p>
 * Note, container caches size, firstId, lastId to be much faster with large
 * datasets. TODO make this caching optional, actually should trust on
 * Hibernates and DB engines query caches.
 * 
 * <p>
 * See http://vaadin.com/wiki/-/wiki/Main/Using%20Hibernate%20with%20Vaadin?
 * p_r_p_185834411_title=Using%20Hibernate%20with%20Vaadin for a working example
 * application.
 * 
 * @author Matti Tahvonen (IT Mill)
 * @author Henri Sara (IT Mill)
 * @author Daniel Bell (itree.com.au, bugfixes, support for embedded composite
 *         keys, ability to add non Hibernate-mapped properties)
 * @author Marc Englund (IT Mill, weak item cache to conserve memory/return same
 *         item instance) Update item to reference updated pojo.
 * @author Pavel Micka updateEntity method
 * @author tmi mergeEntity method
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class NewDBEntityManager<T> extends DBEntityManager<T> {

	public NewDBEntityManager(Class<T> entityType, SessionManager sessionMgr) {
		super(entityType, sessionMgr);
		type = entityType;
		sessionManager = sessionMgr;
	}

	/** Entity class that will be listed in container */
	protected Class<T> type;
	protected final SessionManager sessionManager;
	private ClassMetadata classMetadata;

	/**
	 * Creates a new instance of HbnContainer, listing all object of given type
	 * from database.
	 * 
	 * @param entityType
	 *            Entity class to be listed in container.
	 * @param sessionMgr
	 *            interface via Hibernate session is fetched
	 */
	// public NewDBEntityManager(Class<T> entityType, SessionManager sessionMgr)
	// {
	// super()
	// ;
	// }

	/**
	 * HbnContainer specific method to persist newly created entity.
	 * 
	 * @param entity
	 *            the unsaved entity object
	 * @return the identifier for newly persisted entity
	 */
	public synchronized Serializable saveEntity(T entity) {
		// insert into DB
		Session session = sessionManager.openSession();
		session.saveOrUpdate(entity);
		Serializable id = (Serializable) getIdForPojo(entity, session);

		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}

		sessionManager.closeSession(session);
		return id;
	}

	/**
	 * HbnContainer specific method to update entity.
	 * 
	 * @param entity
	 *            to update
	 * @return the identifier of the updated entity
	 */
	public synchronized Serializable updateEntity(T entity) {
		// update DB
		Session session = sessionManager.openSession();
		session.saveOrUpdate(entity);

		final Serializable itemId = (Serializable) getIdForPojo(entity, session);
		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);
		return itemId;
	}

	public synchronized Serializable mergeEntity(T entity) {

		// update DB
		Session session = sessionManager.openSession();
		session.merge(entity);
		final Serializable itemId = (Serializable) getIdForPojo(entity, session);

		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);
		return itemId;
	}

	/**
	 * Helper method to get the Hibernate ClassMetadata for listed entity type.
	 * Method lazyly caches the metadata, optimizing the lookup a bit (instead
	 * of fetching it continuously from Session).
	 * 
	 * @return Hibernates ClassMetadata for the listed entity type
	 */
	private ClassMetadata getClassMetadata(Session session) {
		if (classMetadata == null) {
			classMetadata = session.getSessionFactory().getClassMetadata(type);
		}
		return classMetadata;
	}

	public synchronized List<T> getAll() {
		Session session = sessionManager.openSession();
		Criteria criteria = session.createCriteria(type);
		List<T> list = criteria.list();
		;
		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);

		return list;
	}

	public synchronized List<T> getByCriterion(List<Criterion> criterias) {
		Session session = sessionManager.openSession();
		final Criteria criteria = session.createCriteria(type);
		for (Criterion c : criterias) {
			criteria.add(c);
		}

		List<T> result = criteria.list();
		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);

		return result;
	}

	public synchronized List<T> getByCriterion(List<Criterion> criterias, Map<String, FetchMode> fetchModes) {
		Session session = sessionManager.openSession();
		final Criteria criteria = session.createCriteria(type);
		for (String property : fetchModes.keySet()) {
			criteria.setFetchMode(property, fetchModes.get(property));
		}
		for (Criterion c : criterias) {
			criteria.add(c);
		}

		List<T> result = criteria.list();
		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);

		return result;

	}

	public synchronized List<T> getByCriterionAndAscOrder(List<Criterion> criterias, List<String> order) {
		Session session = sessionManager.openSession();
		final Criteria criteria = session.createCriteria(type);
		for (Criterion c : criterias) {
			criteria.add(c);
		}

		for (String s : order) {
			criteria.addOrder(Order.asc(s));
		}

		List<T> result = criteria.list();
		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);

		return result;
	}

	public synchronized List<T> getByCriterionAndDescOrder(List<Criterion> criterias, List<String> order) {
		Session session = sessionManager.openSession();
		final Criteria criteria = session.createCriteria(type);
		for (Criterion c : criterias) {
			criteria.add(c);
		}

		for (String s : order) {
			criteria.addOrder(Order.desc(s));
		}

		List<T> result = criteria.list();
		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);
		return result;
	}

	public synchronized List<T> getByCriterionAndDescOrderAndLimit(List<Criterion> criterias, List<String> order,
			Integer maxResults) {
		Session session = sessionManager.openSession();
		final Criteria criteria = session.createCriteria(type);
		for (Criterion c : criterias) {
			criteria.add(c);
		}

		for (String s : order) {
			criteria.addOrder(Order.desc(s));
		}
		criteria.setMaxResults(maxResults);

		List<T> result = criteria.list();
		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);
		return result;
	}

	public synchronized List<T> getByHql(String hql, Map<String, Collection> parameterList) {
		Session session = sessionManager.openSession();
		Query query = session.createQuery(hql);
		for (String p : parameterList.keySet()) {
			query.setParameterList(p, parameterList.get(p));
		}
		List<T> list = query.list();
		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);
		return list;
	}

	public synchronized List<T> getByCriterionAndOrderAndAliases(List<Criterion> criterias, List<String> order,
			HashMap<String, String> aliases, String mainEntityNAme) {
		Session session = sessionManager.openSession();

		final Criteria criteria = session.createCriteria(type, mainEntityNAme);
		for (String k : aliases.keySet())
			criteria.createAlias(k, aliases.get(k));
		for (Criterion c : criterias) {
			criteria.add(c);
		}

		for (String s : order) {
			criteria.addOrder(Order.asc(s));
		}

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<T> result = criteria.list();
		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);
		return result;

	}

	public synchronized List executeSQLQuery(String query) {
		Session session = sessionManager.openSession();
		List result = session.createSQLQuery(query).list();
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);
		return result;
	}

	/**
	 * Helper method to detect identifier of given entity object.
	 * 
	 * @param pojo
	 *            the entity object which identifier is to be resolved
	 * @return the identifier if the given Hibernate entity object
	 */
	private synchronized Object getIdForPojo(Object pojo, Session session) {
		return getClassMetadata(session).getIdentifier(pojo, EntityMode.POJO);
	}

	/**
	 * @return column name of identifier propertyName
	 */
	public synchronized String getIdPropertyName(Session session) {
		return getClassMetadata(session).getIdentifierPropertyName();
	}

	public synchronized boolean delete(T entity) {
		Session session = sessionManager.openSession();
		session.delete(entity);
		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);
		return true;
	}

	public Object getUniqueResult(List<Criterion> criterias, Projection projection) {
		Session session = sessionManager.openSession();
		final Criteria criteria = session.createCriteria(type);
		criteria.setProjection(projection);
		for (Criterion c : criterias) {
			criteria.add(c);
		}
		Object result = criteria.uniqueResult();

		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);
		return result;
	}

	public Object getUniqueResult(List<Criterion> criterias) {
		Session session = sessionManager.openSession();
		final Criteria criteria = session.createCriteria(type);
		for (Criterion c : criterias) {
			criteria.add(c);
		}
		Object result = criteria.uniqueResult();
		// Commit session changes.
		if (session.getTransaction().isActive()) {
			session.getTransaction().commit();
		}
		sessionManager.closeSession(session);
		return result;
	}

}
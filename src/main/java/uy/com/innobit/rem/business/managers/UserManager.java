package uy.com.innobit.rem.business.managers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import uy.com.innobit.rem.persistence.datamodel.clients.Occupant;
import uy.com.innobit.rem.persistence.datamodel.user.User;
import uy.com.innobit.rem.persistence.util.DBEntityManagerFactory;

public class UserManager {
	private static UserManager instance;

	private UserManager() {
	}

	public static UserManager getInstance() {
		if (instance == null)
			instance = new UserManager();
		return instance;
	}
	public User getBylogin(String login) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("login", login));
		List<User> list = DBEntityManagerFactory.get(User.class).getByCriterion(criterias);
		if (list == null || list.isEmpty())
			return null;

		return list.get(0);
	}

	public synchronized User login(String login, String password) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("login", login));
		criterias.add(Restrictions.eq("password", password));
		List<User> list = DBEntityManagerFactory.get(User.class).getByCriterion(criterias);
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);

	}

	public synchronized User getById(Integer id) {
		List<Criterion> criterias = new ArrayList<Criterion>();
		criterias.add(Restrictions.eq("id", id));
		List<User> list = DBEntityManagerFactory.get(User.class).getByCriterion(criterias);
		if (list == null || list.isEmpty())
			return null;
		return list.get(0);

	}

	public synchronized List<User> getAll() {
		return DBEntityManagerFactory.get(User.class).getAll();

	}

	public synchronized void saveUser(User user) {
		DBEntityManagerFactory.get(User.class).saveEntity(user);
	}

	public synchronized void updateUser(User user) {
		DBEntityManagerFactory.get(User.class).updateEntity(user);
	}

	public synchronized void deleteUser(User user) {
		DBEntityManagerFactory.get(User.class).delete(user);
	}
}

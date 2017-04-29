package uy.com.innobit.rem.persistence.util;

import java.io.Serializable;
import java.util.HashMap;

import uy.com.innobit.rem.persistence.datamodel.Bean;

 
/**
 * Factory to get Hibernate MANAGERS singletons.s
 * 
 * @author tmi
 * 
 */
public  class DBEntityManagerFactory implements Serializable {
  private static final long serialVersionUID = -757454894243692989L;

  /**
   * Mapping of containers.
   */
  private static final HashMap<Class<? extends Bean>, DBEntityManager<? extends Bean>> managers = new HashMap<Class<? extends Bean>, DBEntityManager<? extends Bean>>();

  /**
   * Provides only static methods.
   */
  private DBEntityManagerFactory() {
  }

  /**
   * Gets the Hibernate manager linked with this class. If doesn't exist, the container is created.
   * 
   * @param <T>
   *          Class of the beans managed by the container.
   * @param clazz
   *          The bean class.
   * @return The container.
   */
  @SuppressWarnings("unchecked")
  public static synchronized <T extends Bean> DBEntityManager<T> get(Class<T> clazz) {

    // Gets the container (cast is safe because the instantiations of the containers are executed locally).
	  DBEntityManager<T> manager = (DBEntityManager<T>) managers.get(clazz);

    // Creates it if it doesn't exist.
    if (manager == null) {
      manager = new NewDBEntityManager<T>(clazz, HibernateSessionManager.getInstance());
      managers.put(clazz, manager);
    }

    return manager;
  }
}

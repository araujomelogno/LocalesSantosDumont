package uy.com.innobit.rem.persistence.util;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;

public class HibernateInitListener implements SessionInitListener {
	private static final long serialVersionUID = 1L;

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException {
		HibernateSessionManager.init();
		HibernateSessionFactory.getSessionFactory();
	}

}

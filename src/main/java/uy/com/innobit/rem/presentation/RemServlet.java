package uy.com.innobit.rem.presentation;

import javax.servlet.ServletException;

import com.vaadin.server.VaadinServlet;

import uy.com.innobit.rem.persistence.util.HibernateInitListener;

@SuppressWarnings("serial")
public class RemServlet extends VaadinServlet {

	@Override
	protected final void servletInitialized() throws ServletException {
		super.servletInitialized();
		getService().addSessionInitListener(new RemSessionInitListener());
		getService().addSessionInitListener(new HibernateInitListener());
	}
}
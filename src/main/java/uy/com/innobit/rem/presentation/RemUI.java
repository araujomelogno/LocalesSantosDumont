package uy.com.innobit.rem.presentation;

import java.util.Locale;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.business.managers.UserManager;
import uy.com.innobit.rem.business.util.Encryption;
import uy.com.innobit.rem.persistence.datamodel.dashboardemo.DataProvider;
import uy.com.innobit.rem.persistence.datamodel.dashboardemo.DummyDataProvider;
import uy.com.innobit.rem.persistence.datamodel.user.User;
import uy.com.innobit.rem.presentation.event.DashboardEvent.BrowserResizeEvent;
import uy.com.innobit.rem.presentation.event.DashboardEvent.CloseOpenWindowsEvent;
import uy.com.innobit.rem.presentation.event.DashboardEvent.UserLoggedOutEvent;
import uy.com.innobit.rem.presentation.event.DashboardEvent.UserLoginRequestedEvent;
import uy.com.innobit.rem.presentation.event.DashboardEventBus;
import uy.com.innobit.rem.presentation.view.LoginView;
import uy.com.innobit.rem.presentation.view.MainView;

@Theme("dashboard")
@Widgetset("uy.com.innobit.rem.RealEstateManagerWidgetSet")
@Title("SantosDumont")
@SuppressWarnings("serial")
public final class RemUI extends UI {
	private final DataProvider dataProvider = new DummyDataProvider();
	private final DashboardEventBus dashboardEventbus = new DashboardEventBus();
	private User loggedUser;

	public static RemUI get() {
		return (RemUI) UI.getCurrent();
	}

	@Override
	protected void init(final VaadinRequest request) {
		setLocale(Locale.US);
		DashboardEventBus.register(this);
		Responsive.makeResponsive(this);
		addStyleName(ValoTheme.UI_WITH_MENU);
		// Some views need to be aware of browser resize events so a
		// BrowserResizeEvent gets fired to the event bus on every occasion.
		Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(final BrowserWindowResizeEvent event) {
				DashboardEventBus.post(new BrowserResizeEvent());
			}
		});

		String name = request.getParameter("user");
		String code = request.getParameter("code");
		if (name != null && code != null) {
			loggedUser = UserManager.getInstance().getBylogin(name);
			if (loggedUser != null && code.trim().equalsIgnoreCase(Encryption.MD5(loggedUser.getPassword()))) {
				VaadinSession.getCurrent().setAttribute(User.class.getName(), loggedUser);
				setContent(new MainView());
				removeStyleName("loginview");
				getNavigator().navigateTo("Horas");
			}
		} else {
			setContent(new LoginView());
			addStyleName("loginview");
		}
	}

	@Subscribe
	public void userLoginRequested(final UserLoginRequestedEvent event) {
		loggedUser = UserManager.getInstance().login(event.getUserName(), event.getPassword());
		if (loggedUser != null) {
			VaadinSession.getCurrent().setAttribute(User.class.getName(), loggedUser);
			// Authenticated user
			setContent(new MainView());
			removeStyleName("loginview");
			getNavigator().navigateTo(getNavigator().getState());
		} else {
			Notification.show("Usuario o contraseña incorrecta", Notification.Type.ERROR_MESSAGE);
			// setContent(new LoginView());
			// addStyleName("loginview");
		}
	}

	@Subscribe
	public void userLoggedOut(final UserLoggedOutEvent event) {
		// When the user logs out, current VaadinSession gets closed and the
		// page gets reloaded on the login screen. Do notice the this doesn't
		// invalidate the current HttpSession.
		VaadinSession.getCurrent().close();
		Page.getCurrent().reload();
	}

	@Subscribe
	public void closeOpenWindows(final CloseOpenWindowsEvent event) {
		for (Window window : getWindows()) {
			window.close();
		}
	}

	/**
	 * @return An instance for accessing the (dummy) services layer.
	 */
	public static DataProvider getDataProvider() {
		return ((RemUI) getCurrent()).dataProvider;
	}

	public static DashboardEventBus getDashboardEventbus() {
		return ((RemUI) getCurrent()).dashboardEventbus;
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}
}

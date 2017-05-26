package uy.com.innobit.rem.presentation.view;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.presentation.event.DashboardEvent.UserLoginRequestedEvent;
import uy.com.innobit.rem.presentation.RemUI;
import uy.com.innobit.rem.presentation.event.DashboardEventBus;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {

	public LoginView() {
		setSizeFull();
		Component loginForm = buildLoginForm();
		addComponent(loginForm);
		setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
		Notification notification = new Notification("Gestión de Locales");
		notification.setDescription("<span>Inmobiliaria Santos Dumont</span>");
		notification.setHtmlContentAllowed(true);
		notification.setStyleName("tray dark small closable login-help");
		notification.setPosition(Position.BOTTOM_CENTER);
		notification.show(Page.getCurrent());

	}

	private Component buildLoginForm() {
		final VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setSizeUndefined();
		loginPanel.setSpacing(true);
		Responsive.makeResponsive(loginPanel);
		loginPanel.addStyleName("login-panel");

		loginPanel.addComponent(buildLabels());
		loginPanel.addComponent(buildFields());
		return loginPanel;
	}

	private Component buildFields() {
		HorizontalLayout fields = new HorizontalLayout();
		fields.setSpacing(true);
		fields.addStyleName("fields");

		final TextField username = new TextField("Login:");
		username.setIcon(FontAwesome.USER);
		username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		final PasswordField password = new PasswordField("Password:");
		password.setIcon(FontAwesome.LOCK);
		password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		final Button signin = new Button("Entrar");
		signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
		signin.setClickShortcut(KeyCode.ENTER);
		signin.focus();

		fields.addComponents(username, password, signin);
		fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

		signin.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				DashboardEventBus.post(new UserLoginRequestedEvent(username.getValue(), password.getValue()));
			}
		});
		return fields;
	}

	private Component buildLabels() {
		CssLayout labels = new CssLayout();
		labels.addStyleName("labels");

		Label welcome = new Label("Bienvenido");
		welcome.setSizeUndefined();
		welcome.addStyleName(ValoTheme.LABEL_H4);
		welcome.addStyleName(ValoTheme.LABEL_COLORED);
		labels.addComponent(welcome);

		Label title = new Label("Gestión de Locales");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H3);
		title.addStyleName(ValoTheme.LABEL_LIGHT);
		labels.addComponent(title);
		return labels;
	}

}

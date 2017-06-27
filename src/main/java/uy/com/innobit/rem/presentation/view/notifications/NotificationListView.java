package uy.com.innobit.rem.presentation.view.notifications;

import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.business.managers.NotificationsManager;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractNotification;
import uy.com.innobit.rem.persistence.datamodel.notifications.Notification;
import uy.com.innobit.rem.persistence.datamodel.property.PropertyNotification;
import uy.com.innobit.rem.presentation.RemUI;
import uy.com.innobit.rem.presentation.ScreenSize;
import uy.com.innobit.rem.presentation.view.properties.PropertyForm;

@SuppressWarnings("serial")
public class NotificationListView extends MVerticalLayout implements View {

	Table notificationsTable = new Table();

	MHorizontalLayout mainContent = new MHorizontalLayout(notificationsTable).withFullWidth().withMargin(false);

	Header header = new Header("Recordatorios").setHeaderLevel(2);
	Window notificationsWindow;
	Notification notification;

	private void layout() {
		removeAllComponents();
		if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
			addComponents(new MHorizontalLayout(header).expand(header).alignAll(Alignment.MIDDLE_LEFT), mainContent);
		} else {
			addComponents(header, mainContent);
		}
		setMargin(new MarginInfo(false, true, true, true));
		expand(mainContent);
	}

	private void buildRemindersWindow() {
		notificationsWindow = new Window("Recordatorio");
		notificationsWindow.setModal(true);
		notificationsWindow.setClosable(true);
		notificationsWindow.setResizable(false);
		notificationsWindow.setDraggable(false);
		notificationsWindow.setWidth("60%");
		notificationsWindow.setStyleName("mipa");

		final DateField date = new DateField("Fecha:");
		date.setDateFormat("dd/MM/yyyy");
		date.setValue(notification.getNotificationDate());
		date.setReadOnly(true);

		final CheckBox resolved = new CheckBox(":Resuelta");
		resolved.setValue(notification.isResolved());
		final MTextArea message = new MTextArea("Mensaje:");
		message.setValue(notification.getMessage());
		message.setReadOnly(true);

		GridLayout grid = new GridLayout(3, 2);
		grid.setWidth("100%");
		grid.setStyleName(ValoTheme.PANEL_WELL);
		grid.setSpacing(true);
		grid.addComponent(date, 0, 0);
		grid.addComponent(resolved, 1, 0);
		grid.addComponent(message, 0, 1);

		grid.setWidth("100%");
		grid.setStyleName("mipa");
		VerticalLayout layout = new VerticalLayout(grid);
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);
		notificationsWindow.setContent(layout);
		notificationsWindow.addCloseListener(new CloseListener() {

			@Override
			public void windowClose(CloseEvent e) {
				notification.setResolved(resolved.getValue());
				if (notification.isResolved()) {
					notificationsTable.getContainerDataSource().removeItem(notification);
					notificationsTable.markAsDirty();
				}
				if (notification instanceof ContractNotification) {
					ContractNotification new_name = (ContractNotification) notification;
					NotificationsManager.getInstance().update(new_name);
				}

				if (notification instanceof PropertyNotification) {
					PropertyNotification new_name = (PropertyNotification) notification;
					NotificationsManager.getInstance().update(new_name);

				}

			}
		});
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		notificationsTable.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				notification = (Notification) notificationsTable.getValue();
				buildRemindersWindow();
				RemUI.getCurrent().addWindow(notificationsWindow);

			}
		});
		layout();
		UI.getCurrent().setResizeLazy(true);
		Page.getCurrent().addBrowserWindowResizeListener(new Page.BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(Page.BrowserWindowResizeEvent browserWindowResizeEvent) {
				layout();
			}
		});

		IndexedContainer cont = new IndexedContainer();
		cont.addContainerProperty("subject", String.class, "");
		cont.addContainerProperty("date", String.class, "");
		cont.addContainerProperty("message", String.class, "");
		notificationsTable.setContainerDataSource(cont);
		notificationsTable.setVisibleColumns("subject", "date", "message");
		notificationsTable.setColumnHeaders("Tipo", "Fecha", "Mensaje");
		for (Notification not : NotificationsManager.getInstance().getContractNotificationsToResolve()) {
			cont.addItem(not);
			cont.getContainerProperty(not, "subject").setValue(not.getSubject());
			cont.getContainerProperty(not, "date").setValue(not.getDateSDF());
			cont.getContainerProperty(not, "message").setValue(not.getMessage());
		}
		for (Notification not : NotificationsManager.getInstance().getPropertyNotificationsToResolve()) {
			cont.addItem(not);
			cont.getContainerProperty(not, "subject").setValue(not.getSubject());
			cont.getContainerProperty(not, "date").setValue(not.getDateSDF());
			cont.getContainerProperty(not, "message").setValue(not.getMessage());
		}
		notificationsTable.setWidth("100%");
		notification = VaadinSession.getCurrent().getAttribute(Notification.class);
		if (notification != null) {
			buildRemindersWindow();
			RemUI.get().addWindow(notificationsWindow);
		}
	}

}

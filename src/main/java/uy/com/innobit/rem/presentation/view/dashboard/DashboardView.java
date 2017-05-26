package uy.com.innobit.rem.presentation.view.dashboard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.business.managers.ContractManager;
import uy.com.innobit.rem.business.managers.NotificationsManager;
import uy.com.innobit.rem.business.managers.OwnerManager;
import uy.com.innobit.rem.business.managers.PropertyManager;
import uy.com.innobit.rem.business.managers.UserManager;
import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractCharge;
import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.presentation.RemUI;
import uy.com.innobit.rem.presentation.component.AnualIncomeChart;
import uy.com.innobit.rem.presentation.component.PropertiesPieChart;
import uy.com.innobit.rem.presentation.component.SparklineChart;
import uy.com.innobit.rem.presentation.component.TextFieldWithButton;
import uy.com.innobit.rem.presentation.component.TopTenContractsCloseToExpireTable;
import uy.com.innobit.rem.presentation.event.DashboardEvent.CloseOpenWindowsEvent;
import uy.com.innobit.rem.presentation.event.DashboardEvent.NotificationsCountUpdatedEvent;
import uy.com.innobit.rem.presentation.event.DashboardEventBus;
import uy.com.innobit.rem.presentation.view.dashboard.DashboardEdit.DashboardEditListener;

@SuppressWarnings("serial")
public final class DashboardView extends Panel implements View, DashboardEditListener {

	public static final String EDIT_ID = "dashboard-edit";
	public static final String TITLE_ID = "dashboard-title";

	private Label titleLabel;
	private NotificationsButton notificationsButton;
	private CssLayout dashboardPanels;
	private final VerticalLayout root;
	private Window notificationsWindow;
	private DashboardStats stats;
	private Map<String, Date> contractExpirations = new HashMap<>();
	private TextFieldWithButton search = null;;
	List<uy.com.innobit.rem.persistence.datamodel.notifications.Notification> notifications;

	private Component buildSparklines() {
		CssLayout sparks = new CssLayout();
		sparks.addStyleName("sparks");
		sparks.setWidth("100%");
		Responsive.makeResponsive(sparks);

		SparklineChart s = new SparklineChart("Propiedades", stats.getPropertyQ().toString());
		sparks.addComponent(s);

		s = new SparklineChart("Contratos activos", stats.getFullQ().toString());
		sparks.addComponent(s);

		s = new SparklineChart("Propietarios", stats.getOwnersQ().toString());
		sparks.addComponent(s);

		DecimalFormat formatter = new DecimalFormat("#,###");
		s = new SparklineChart("Facturado anual", "$" + formatter.format(stats.getIncome()));
		sparks.addComponent(s);

		return sparks;
	}

	private Component buildHeader() {

		//
		// header.setSpacing(true);

		titleLabel = new Label("Dashboard");
		titleLabel.setId(TITLE_ID);
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);

		notificationsButton = buildNotificationsButton();
		// Component edit = buildEditButton();

		search = new TextFieldWithButton("", FontAwesome.SEARCH, onClick -> {
			VaadinSession.getCurrent().setAttribute(String.class, search.getTextField().getValue());
			UI.getCurrent().getNavigator().navigateTo("/search");
		});
		// HorizontalLayout tools = new HorizontalLayout(search,
		// notificationsButton);
		// tools.setSpacing(true);
		// header.addComponent(tools);
		MHorizontalLayout header = new MHorizontalLayout(titleLabel, notificationsButton, search).expand(titleLabel);
		header.setComponentAlignment(notificationsButton, Alignment.BOTTOM_CENTER);
		header.setComponentAlignment(titleLabel, Alignment.BOTTOM_CENTER);
		header.setComponentAlignment(search, Alignment.TOP_CENTER);

		header.setMargin(new MarginInfo(false, true, false, false));
		// header.addStyleName("viewheader");
		return header;
	}

	private NotificationsButton buildNotificationsButton() {
		notifications = NotificationsManager.getInstance().getContractNotificationsToResolveEXPIRED();
		notifications.addAll(NotificationsManager.getInstance().getPropertyNotificationsToResolveEXPIRED());
		NotificationsButton result = new NotificationsButton(notifications.size());
		result.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				openNotificationsPopup(event, notifications);
			}
		});
		return result;
	}

	private Component buildContent() {
		dashboardPanels = new CssLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);

		dashboardPanels.addComponent(buildAnualIncomeChart());
		dashboardPanels.addComponent(buildNotes());
		dashboardPanels.addComponent(buildContractsCloseToExpire());
		dashboardPanels.addComponent(buildPropertiesPie());

		return dashboardPanels;
	}

	private Component buildAnualIncomeChart() {
		AnualIncomeChart anualIncomeChart = new AnualIncomeChart(stats);
		anualIncomeChart.setSizeFull();
		return createContentWrapper(anualIncomeChart);
	}

	private Component buildNotes() {
		TextArea notes = new TextArea("Notas");
		if (RemUI.get().getLoggedUser() != null) {

			notes.setValue(RemUI.get().getLoggedUser().getNotepad());
			notes.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(ValueChangeEvent event) {
					RemUI.get().getLoggedUser().setNotepad(notes.getValue());
					UserManager.getInstance().updateUser(RemUI.get().getLoggedUser());

				}
			});

		}
		notes.setSizeFull();
		notes.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		Component panel = createContentWrapper(notes);
		panel.addStyleName("notes");
		return panel;
	}

	private Component buildContractsCloseToExpire() {
		Component contentWrapper = createContentWrapper(new TopTenContractsCloseToExpireTable(stats));
		contentWrapper.addStyleName("top10-revenue");
		return contentWrapper;
	}

	private Component buildPropertiesPie() {
		return createContentWrapper(new PropertiesPieChart(stats));
	}

	private Component createContentWrapper(final Component content) {
		final CssLayout slot = new CssLayout();
		slot.setWidth("100%");
		slot.addStyleName("dashboard-panel-slot");

		CssLayout card = new CssLayout();
		card.setWidth("100%");
		card.addStyleName(ValoTheme.LAYOUT_CARD);

		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.addStyleName("dashboard-panel-toolbar");
		toolbar.setWidth("100%");

		Label caption = new Label(content.getCaption());
		caption.addStyleName(ValoTheme.LABEL_H4);
		caption.addStyleName(ValoTheme.LABEL_COLORED);
		caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		content.setCaption(null);

		MenuBar tools = new MenuBar();
		tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
		MenuItem max = tools.addItem("", FontAwesome.EXPAND, new Command() {

			@Override
			public void menuSelected(final MenuItem selectedItem) {
				if (!slot.getStyleName().contains("max")) {
					selectedItem.setIcon(FontAwesome.COMPRESS);
					toggleMaximized(slot, true);
				} else {
					slot.removeStyleName("max");
					selectedItem.setIcon(FontAwesome.EXPAND);
					toggleMaximized(slot, false);
				}
			}
		});
		max.setStyleName("icon-only");
		// MenuItem root = tools.addItem("", FontAwesome.COG, null);
		// root.addItem("Configure", new Command() {
		// @Override
		// public void menuSelected(final MenuItem selectedItem) {
		// Notification.show("Not implemented in this demo");
		// }
		// });
		// root.addSeparator();
		// root.addItem("Close", new Command() {
		// @Override
		// public void menuSelected(final MenuItem selectedItem) {
		// Notification.show("Not implemented in this demo");
		// }
		// });

		toolbar.addComponents(caption, tools);
		toolbar.setExpandRatio(caption, 1);
		toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

		card.addComponents(toolbar, content);
		slot.addComponent(card);
		return slot;
	}

	private void openNotificationsPopup(ClickEvent event,
			List<uy.com.innobit.rem.persistence.datamodel.notifications.Notification> notifications) {
		VerticalLayout notificationsLayout = new VerticalLayout();
		notificationsLayout.setMargin(true);
		notificationsLayout.setSpacing(true);

		Label title = new Label("Recordatorios");
		title.addStyleName(ValoTheme.LABEL_H3);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		notificationsLayout.addComponent(title);

		DashboardEventBus.post(new NotificationsCountUpdatedEvent());

		for (int i = 0; i < Math.min(4, notifications.size()); i++) {
			final uy.com.innobit.rem.persistence.datamodel.notifications.Notification not = notifications.get(i);
			VerticalLayout notificationLayout = new VerticalLayout();
			notificationLayout.addStyleName("notification-item");

			Button titleLabel = new Button(not.getSubject());
			titleLabel.setStyleName(ValoTheme.BUTTON_LINK);
			titleLabel.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {

					VaadinSession.getCurrent().setAttribute(
							uy.com.innobit.rem.persistence.datamodel.notifications.Notification.class, not);
					UI.getCurrent().getNavigator().navigateTo("/reminders");
				}
			});
			titleLabel.addStyleName("notification-title");

			Label timeLabel = new Label(not.getDateSDF());
			timeLabel.addStyleName("notification-time");

			Label contentLabel = new Label(not.getMessageAux());
			contentLabel.addStyleName("notification-content");

			notificationLayout.addComponents(titleLabel, timeLabel, contentLabel);
			notificationsLayout.addComponent(notificationLayout);
		}

		HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		footer.setWidth("100%");
		Button showAll = new Button("Mostrar todos", new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo("/reminders");
			}
		});
		showAll.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		showAll.addStyleName(ValoTheme.BUTTON_SMALL);
		footer.addComponent(showAll);
		footer.setComponentAlignment(showAll, Alignment.TOP_CENTER);
		notificationsLayout.addComponent(footer);

		if (notificationsWindow == null) {
			notificationsWindow = new Window();
			notificationsWindow.setWidth(300.0f, Unit.PIXELS);
			notificationsWindow.addStyleName("notifications");
			notificationsWindow.setClosable(false);
			notificationsWindow.setResizable(false);
			notificationsWindow.setDraggable(false);
			notificationsWindow.setCloseShortcut(KeyCode.ESCAPE, null);
			notificationsWindow.setContent(notificationsLayout);
		}

		if (!notificationsWindow.isAttached()) {
			notificationsWindow.setPositionY(event.getClientY() - event.getRelativeY() + 40);
			notificationsWindow.setPositionX(event.getClientX() - event.getRelativeX() + 40);
			getUI().addWindow(notificationsWindow);
			notificationsWindow.focus();
		} else {
			notificationsWindow.close();
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	public DashboardView() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		setSizeFull();

		DashboardEventBus.register(this);

		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		Date now = new Date();
		stats = new DashboardStats();
		stats.setOwnersQ(OwnerManager.getInstance().count().intValue());
		for (Property p : PropertyManager.getInstance().getAll()) {
			stats.setPropertyQ(stats.getPropertyQ() + 1);
			if (p.isEmpty())
				stats.setFreeQ(stats.getFreeQ() + 1);
			else {
				stats.setFullQ(stats.getFullQ() + 1);
			}
			if (!p.getContracts().isEmpty()) {
				for (Contract c : p.getContracts()) {
					if (c.getEnd() != null && c.getEnd().after(now)) {
						if (contractExpirations.containsKey(p.getName())) {
							if (c.getEnd().after(contractExpirations.get(p.getName())))
								contractExpirations.put(p.getName(), c.getEnd());
						} else {
							contractExpirations.put(p.getName(), c.getEnd());
						}
					}
				}
			}
		}
		List<String> aux = new ArrayList<>(contractExpirations.keySet());
		Collections.sort(aux, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return contractExpirations.get(o1).compareTo(contractExpirations.get(o2));
			}
		});
		for (int i = 0; i < Math.min(aux.size(), 10); i++) {
			Object[] data = new Object[2];
			data[0] = aux.get(i);
			data[1] = contractExpirations.get(aux.get(i));
			stats.getExpirations().add(data);
		}
		List<ContractCharge> charges = ContractManager.getInstance().getActualYearCharges();
		for (ContractCharge c : charges) {
			Double am = c.getAmount();
			if (!c.getCurrency().equalsIgnoreCase("$"))
				if (c.getDollarCotization() != null)
					am = am * c.getDollarCotization();

			stats.setIncome(stats.getIncome() + am);
			switch (c.getPaymentDate().getMonth()) {
			case 0:
				stats.setIncomeM1(stats.getIncomeM1() + am);
				break;
			case 1:
				stats.setIncomeM2(stats.getIncomeM2() + am);
				break;
			case 2:
				stats.setIncomeM3(stats.getIncomeM3() + am);
				break;
			case 3:
				stats.setIncomeM4(stats.getIncomeM4() + am);
				break;
			case 4:
				stats.setIncomeM5(stats.getIncomeM5() + am);
				break;
			case 5:
				stats.setIncomeM6(stats.getIncomeM6() + am);
				break;
			case 6:
				stats.setIncomeM7(stats.getIncomeM7() + am);
				break;
			case 7:
				stats.setIncomeM8(stats.getIncomeM8() + am);
				break;
			case 8:
				stats.setIncomeM9(stats.getIncomeM9() + am);
				break;
			case 9:
				stats.setIncomeM10(stats.getIncomeM10() + am);
				break;
			case 10:
				stats.setIncomeM11(stats.getIncomeM11() + am);
				break;
			case 11:
				stats.setIncomeM12(stats.getIncomeM12() + am);
				break;
			default:
				break;
			}

		}
		root.addComponent(buildHeader());

		root.addComponent(buildSparklines());

		Component content = buildContent();
		root.addComponent(content);
		root.setExpandRatio(content, 1);

		// All the open sub-windows should be closed whenever the root layout
		// gets clicked.
		root.addLayoutClickListener(new LayoutClickListener() {
			@Override
			public void layoutClick(final LayoutClickEvent event) {
				DashboardEventBus.post(new CloseOpenWindowsEvent());
			}
		});

	}

	@Override
	public void dashboardNameEdited(final String name) {
		titleLabel.setValue(name);
	}

	private void toggleMaximized(final Component panel, final boolean maximized) {
		for (Iterator<Component> it = root.iterator(); it.hasNext();) {
			it.next().setVisible(!maximized);
		}
		dashboardPanels.setVisible(true);

		for (Iterator<Component> it = dashboardPanels.iterator(); it.hasNext();) {
			Component c = it.next();
			c.setVisible(!maximized);
		}

		if (maximized) {
			panel.setVisible(true);
			panel.addStyleName("max");
		} else {
			panel.removeStyleName("max");
		}
	}

	public static final class NotificationsButton extends Button {
		private static final String STYLE_UNREAD = "unread";
		public static final String ID = "dashboard-notifications";

		public NotificationsButton(Integer count) {
			setIcon(FontAwesome.BELL);
			setId(ID);
			addStyleName("notifications");
			addStyleName(ValoTheme.BUTTON_ICON_ONLY);
			setUnreadCount(count);
		}

		public void setUnreadCount(final int count) {
			setCaption(String.valueOf(count));

			String description = "Notificaciones";
			if (count > 0) {
				addStyleName(STYLE_UNREAD);
				description += " (" + count + " )";
			} else {
				removeStyleName(STYLE_UNREAD);
			}
			setDescription(description);
		}
	}

}

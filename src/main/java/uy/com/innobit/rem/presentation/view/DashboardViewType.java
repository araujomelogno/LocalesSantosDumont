package uy.com.innobit.rem.presentation.view;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

import uy.com.innobit.rem.presentation.view.billing.BillingView;
import uy.com.innobit.rem.presentation.view.dashboard.DashboardView;
import uy.com.innobit.rem.presentation.view.map.MapView;
import uy.com.innobit.rem.presentation.view.occupants.OccupantListView;
import uy.com.innobit.rem.presentation.view.owners.OwnerListView;
import uy.com.innobit.rem.presentation.view.properties.PropertyListView;
import uy.com.innobit.rem.presentation.view.setup.SetupView;
import uy.com.innobit.rem.presentation.view.setup.users.UserListView;

public enum DashboardViewType {
	DASHBOARD("dashboard", DashboardView.class, FontAwesome.HOME, true),
	PROPERTIES("propiedades", PropertyListView.class, FontAwesome.HOME, false),
	OWNERS("propietarios", OwnerListView.class, FontAwesome.MALE, false),
	OCCUPANTS("inquilinos", OccupantListView.class, FontAwesome.GROUP, false),
	MAP("mapa", MapView.class, FontAwesome.GLOBE, false),
	CONTRACTS("facturaciones", BillingView.class, FontAwesome.FILE_O, false),
	SETUP("usuarios", UserListView.class, FontAwesome.COG, false),
	
//	SALES("sales", SalesView.class,FontAwesome.BAR_CHART_O, false), 
//	TRANSACTIONS("transactions", TransactionsView.class, FontAwesome.TABLE,false),
//	REPORTS("reports", ReportsView.class, FontAwesome.FILE_TEXT_O, true), 
//	SCHEDULE("schedule",ScheduleView.class, FontAwesome.CALENDAR_O, false)
	;
	private final String viewName;
	private final Class<? extends View> viewClass;
	private final Resource icon;
	private final boolean stateful;

	private DashboardViewType(final String viewName, final Class<? extends View> viewClass, final Resource icon,
			final boolean stateful) {
		this.viewName = viewName;
		this.viewClass = viewClass;
		this.icon = icon;
		this.stateful = stateful;
	}

	public boolean isStateful() {
		return stateful;
	}

	public String getViewName() {
		return viewName;
	}

	public Class<? extends View> getViewClass() {
		return viewClass;
	}

	public Resource getIcon() {
		return icon;
	}

	public static DashboardViewType getByViewName(final String viewName) {
		DashboardViewType result = null;
		for (DashboardViewType viewType : values()) {
			if (viewType.getViewName().equals(viewName)) {
				result = viewType;
				break;
			}
		}
		return result;
	}

}

package uy.com.innobit.rem.presentation;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

import uy.com.innobit.rem.presentation.event.DashboardEvent.BrowserResizeEvent;
import uy.com.innobit.rem.presentation.event.DashboardEvent.CloseOpenWindowsEvent;
import uy.com.innobit.rem.presentation.event.DashboardEvent.PostViewChangeEvent;
import uy.com.innobit.rem.presentation.event.DashboardEventBus;
import uy.com.innobit.rem.presentation.view.DashboardViewType;
import uy.com.innobit.rem.presentation.view.contracts.ContractEditView;

@SuppressWarnings("serial")
public class RemNavigator extends Navigator {

	// Provide a Google Analytics tracker id here
	private static final String TRACKER_ID = null;// "UA-658457-6";
	private GoogleAnalyticsTracker tracker;

	private static final DashboardViewType ERROR_VIEW = DashboardViewType.DASHBOARD;
	private ViewProvider errorViewProvider;

	Map<String, Class<? extends View>> internalViews = new HashMap<String, Class<? extends View>>();

	public RemNavigator(final ComponentContainer container) {
		super(UI.getCurrent(), container);

		String host = getUI().getPage().getLocation().getHost();
		if (TRACKER_ID != null && host.endsWith("demo.vaadin.com")) {
			initGATracker(TRACKER_ID);
		}
		internalViews.put("/contractEdit", ContractEditView.class);
		initViewChangeListener();
		initViewProviders();
 
	}

	private void initGATracker(final String trackerId) {
		tracker = new GoogleAnalyticsTracker(trackerId, "demo.vaadin.com");

		// GoogleAnalyticsTracker is an extension add-on for UI so it is
		// initialized by calling .extend(UI)
		tracker.extend(UI.getCurrent());
	}

	private void initViewChangeListener() {
		addViewChangeListener(new ViewChangeListener() {

			@Override
			public boolean beforeViewChange(final ViewChangeEvent event) {
				// Since there's no conditions in switching between the views
				// we can always return true.
				return true;
			}

			@Override
			public void afterViewChange(final ViewChangeEvent event) {
				DashboardViewType view = DashboardViewType.getByViewName(event.getViewName());
				// Appropriate events get fired after the view is changed.
				DashboardEventBus.post(new PostViewChangeEvent(view));
				DashboardEventBus.post(new BrowserResizeEvent());
				DashboardEventBus.post(new CloseOpenWindowsEvent());

				if (tracker != null) {
					// The view change is submitted as a pageview for GA tracker
					tracker.trackPageview("/dashboard/" + event.getViewName());
				}
			}
		});
	}

	private void initViewProviders() {
		// A dedicated view provider is added for each separate view type
		for (final DashboardViewType viewType : DashboardViewType.values()) {
			ViewProvider viewProvider = new ClassBasedViewProvider(viewType.getViewName(), viewType.getViewClass()) {

				// This field caches an already initialized view instance if the
				// view should be cached (stateful views).
				private View cachedInstance;

				@Override
				public View getView(final String viewName) {
					View result = null;
					if (viewType.getViewName().equals(viewName)) {
						if (viewType.isStateful()) {
							// Stateful views get lazily instantiated
							if (cachedInstance == null) {
								cachedInstance = super.getView(viewType.getViewName());
							}
							result = cachedInstance;
						} else {
							// Non-stateful views get instantiated every time
							// they're navigated to
							result = super.getView(viewType.getViewName());
						}
					}
					return result;
				}
			};

			if (viewType == ERROR_VIEW) {
				errorViewProvider = viewProvider;
			}

			addProvider(viewProvider);
		}

		for (String route : internalViews.keySet()) {
			ViewProvider viewProvider = new ClassBasedViewProvider(route, internalViews.get(route)) {
				@Override
				public View getView(final String viewName) {
					View result = null;
					result = super.getView(viewName);
					return result;
				}
			};

			addProvider(viewProvider);
		}

		setErrorProvider(new ViewProvider() {
			@Override
			public String getViewName(final String viewAndParameters) {
				return ERROR_VIEW.getViewName();
			}

			@Override
			public View getView(final String viewName) {
				return errorViewProvider.getView(ERROR_VIEW.getViewName());
			}
		});
	}
}

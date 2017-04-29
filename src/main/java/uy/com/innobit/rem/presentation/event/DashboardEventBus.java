package uy.com.innobit.rem.presentation.event;

import java.io.Serializable;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

import uy.com.innobit.rem.presentation.RemUI;

/**
 * A simple wrapper for Guava event bus. Defines static convenience methods for
 * relevant actions.
 */
@SuppressWarnings("serial")
public class DashboardEventBus implements SubscriberExceptionHandler, Serializable {

	private final EventBus eventBus = new EventBus(this);

	public static void post(final Object event) {
		RemUI.getDashboardEventbus().eventBus.post(event);
	}

	public static void register(final Object object) {
		RemUI.getDashboardEventbus().eventBus.register(object);
	}

	public static void unregister(final Object object) {
		RemUI.getDashboardEventbus().eventBus.unregister(object);
	}

	@Override
	public final void handleException(final Throwable exception, final SubscriberExceptionContext context) {
		exception.printStackTrace();
	}
}

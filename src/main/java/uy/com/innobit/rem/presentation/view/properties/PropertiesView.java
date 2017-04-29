package uy.com.innobit.rem.presentation.view.properties;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings({ "serial", "unchecked" })
public class PropertiesView extends VerticalLayout implements View {

	@Override
	public void enter(ViewChangeEvent event) {
		Label tittle = new Label("Propiedades");
		addComponent(tittle);

	}

}

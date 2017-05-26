package uy.com.innobit.rem.presentation.view.contracts;

import uy.com.innobit.rem.presentation.component.ExpirationsTable;

public class DollarExpirationsView extends ContractExpirationsView {

	@Override
	public void enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent event) {
		currency = "US$";
		table = new ExpirationsTable(currency);
		super.enter(event);

	};
}

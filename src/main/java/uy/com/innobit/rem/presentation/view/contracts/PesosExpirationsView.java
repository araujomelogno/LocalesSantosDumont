package uy.com.innobit.rem.presentation.view.contracts;

import uy.com.innobit.rem.presentation.component.ExpirationsTable;

public class PesosExpirationsView extends ContractExpirationsView {

	@Override
	public void enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent event) {
		currency = "$";
		table = new ExpirationsTable(currency);
		super.enter(event);

	};
}

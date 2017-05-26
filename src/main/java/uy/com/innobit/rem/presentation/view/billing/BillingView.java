package uy.com.innobit.rem.presentation.view.billing;

import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

import uy.com.innobit.rem.persistence.datamodel.billing.BillingDataValue;
import uy.com.innobit.rem.persistence.datamodel.billing.BillingManager;

public class BillingView extends MVerticalLayout implements View {
	Header header = new Header("Facturación").setHeaderLevel(2);
	Table table;
	Button query = new Button("Consultar");
	DateField init = new DateField("Desde:");
	DateField end = new DateField("Hasta:");

	private void layout() {
		removeAllComponents();
		buildTable();
		HorizontalLayout hl = new HorizontalLayout(init, end, query);
		hl.setComponentAlignment(query, Alignment.BOTTOM_CENTER);
		hl.setSpacing(true);
		hl.setMargin(true);
		Panel hl2 = new Panel(table);
		hl2.setWidth(super.getWidth() - 2f, super.getWidthUnits());
		addComponents(header, hl, hl2);
		expand(table);
		setMargin(new MarginInfo(false, true, true, true));
		query.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				table.getContainerDataSource().removeAllItems();
				Integer totamount = 0;
				Integer totownerocom = 0;
				Integer totocccom = 0;
				Integer totownercomCh = 0;
				Integer totoccComCh = 0;
				Integer totRentPaid = 0;

				for (BillingDataValue bill : BillingManager.getInstance().getBilling(init.getValue(), end.getValue())) {
					table.getContainerDataSource().addItem(bill);
					totamount += bill.getAmount();
					totownerocom += bill.getOwnerCommission();
					totocccom += bill.getOccupantComission();
					totownercomCh += bill.getOwnerComissionCharged();
					totoccComCh += bill.getOccupantComissionCharged();
					totRentPaid += bill.getAmountPaied();
				}
				table.setColumnFooter("amount", totamount.toString());
				table.setColumnFooter("ownerCommission", totownerocom.toString());
				table.setColumnFooter("occupantComission", totocccom.toString());
				table.setColumnFooter("ownerComissionCharged", totownercomCh.toString());
				table.setColumnFooter("occupantComissionCharged", totoccComCh.toString());
				table.setColumnFooter("amountPaied", totRentPaid.toString());
			}
		});

	}

	public void buildTable() {
		table = new Table();
		BeanItemContainer<BillingDataValue> cont = new BeanItemContainer<BillingDataValue>(BillingDataValue.class);
		table.setContainerDataSource(cont);
		table.setVisibleColumns("init", "end", "property", "amount", "ownerCommission", "occupantComission",
				"ownerComissionCharged", "occupantComissionCharged", "amountPaied");
		table.setColumnHeader("init", "Desde");
		table.setColumnHeader("end", "Hasta");
		table.setColumnHeader("propertyName", "Propiedad");
		table.setColumnHeader("occupant", "Inquilino");
		table.setColumnHeader("ownerName", "Propietario");
		table.setColumnHeader("amount", "Monto");
		table.setColumnHeader("ownerCommission", "Com. Prop.");
		table.setColumnHeader("occupantComission", "Com. Inq.");
		table.setColumnHeader("ownerComissionCharged", "Com. Prop. Cob.");
		table.setColumnHeader("occupantComissionCharged", "Com. Inq. Cob.");
		table.setColumnHeader("amountPaied", "Alq. Pagos");
		table.setFooterVisible(true);
		table.setColumnFooter("propertyName", "Total");

	}

	@Override
	public void enter(ViewChangeEvent event) {
		layout();
	}

}

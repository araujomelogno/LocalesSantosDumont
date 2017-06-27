package uy.com.innobit.rem.presentation.view.contracts;

import java.util.Collection;

import org.tepi.filtertable.FilterTable;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import uy.com.innobit.rem.business.managers.ContractManager;
import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractEntry;
import uy.com.innobit.rem.presentation.view.properties.PropertyForm;

public class ContractListView extends MVerticalLayout implements View {

	// Introduce and configure some UI components used on this view
	private FilterTable table;
	private Header header = new Header("Contratos").setHeaderLevel(2);
	private DateField init = new DateField("Inicio:");
	private DateField end = new DateField("Fin:");
	private Button query = new Button("Consultar");
	private Double totalAmount = 0d;
	private Double totalOwnerComission = 0d;
	private Double totalClientComission = 0d;
	private Double totalOwnerComissionCharged = 0d;
	private Double totalClientComissionCharged = 0d;

	// private Double totalFinalPaymentToOwner = 0d;
	private Double totalNextYearEntryAmount = 0d;
	private Double totalNextYearClientCommision = 0d;
	private Double totalNextYearOwnerCommision = 0d;
	private ExcelExport excelExport;
	private ComboBox currency;

	/**
	 * Do the application layout that is optimized for the screen size.
	 * <p>
	 * Like in Java world in general, Vaadin developers can modularize their
	 * helpers easily and re-use existing code. E.g. In this method we are using
	 * extended versions of Vaadins basic layout that has "fluent API" and this
	 * way we get bit more readable code. Check out vaadin.com/directory for a
	 * huge amount of helper libraries and custom components. They might be
	 * valuable for your productivity.
	 * </p>
	 */
	private void layout() {
		removeAllComponents();
		MHorizontalLayout mainContent = new MHorizontalLayout(table).withFullWidth().withMargin(false);

		query.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				table.getContainerDataSource().removeAllItems();
				table.removeAllItems();
				totalAmount = 0d;
				totalOwnerComission = 0d;
				totalClientComission = 0d;
				totalOwnerComissionCharged = 0d;
				totalClientComissionCharged = 0d;

				// totalFinalPaymentToOwner = 0d;
				totalNextYearEntryAmount = 0d;
				totalNextYearClientCommision = 0d;
				totalNextYearOwnerCommision = 0d;
				for (ContractEntry entry : ContractManager.getInstance().getContractEntries(init.getValue(),
						end.getValue(), currency.getValue().toString())) {
					table.getContainerDataSource().addItem(entry);
					totalAmount = totalAmount + entry.getAmount();
					totalOwnerComission = totalOwnerComission + entry.getOwnerComission();
					totalClientComission = totalClientComission + entry.getClientComission();
					totalOwnerComissionCharged = totalOwnerComissionCharged + entry.getOwnerComissionCharged();
					totalClientComissionCharged = totalClientComissionCharged + entry.getClientComissionCharged();

					// totalFinalPaymentToOwner = totalFinalPaymentToOwner +
					// entry.getFinalPaymentToOwner();
					totalNextYearEntryAmount = totalNextYearEntryAmount + entry.getNextEntryAmount();
					totalNextYearClientCommision = totalNextYearClientCommision + entry.getNextEntryClientCommision();
					totalNextYearOwnerCommision = totalNextYearOwnerCommision + entry.getNextEntryOwnerCommision();
				}
				table.setColumnFooter("amount", round(totalAmount, 0).toString());
				table.setColumnFooter("ownerComission", round(totalOwnerComission, 0).toString());
				table.setColumnFooter("clientComission", round(totalClientComission, 0).toString());
				table.setColumnFooter("ownerComissionCharged", round(totalOwnerComissionCharged, 0).toString());
				table.setColumnFooter("clientComissionCharged", round(totalClientComissionCharged, 0).toString());

				// table.setColumnFooter("finalPaymentToOwner",
				// round(totalFinalPaymentToOwner, 0).toString());
				table.setColumnFooter("nextYearEntryAmount", round(totalNextYearEntryAmount, 0).toString());
				table.setColumnFooter("totalNextYearClientCommision",
						round(totalNextYearClientCommision, 0).toString());
				table.setColumnFooter("totalNextYearOwnerCommision", round(totalNextYearOwnerCommision, 0).toString());

			}
		});

		Button exportTableButton = new Button("Exportar");
		exportTableButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -73954695086117200L;
			private ExcelExport excelExport;

			public void buttonClick(final ClickEvent event) {
				excelExport = new ExcelExport(buildTableForExport());
				excelExport.export();
			}
		});
		currency = new ComboBox("Moneda:");
		currency.addItem("$");
		currency.addItem("US$");
		currency.select("$");
		currency.setTextInputAllowed(false);
		currency.setNewItemsAllowed(false);
		currency.setNullSelectionAllowed(false);

		addComponents(new MHorizontalLayout(header, exportTableButton).expand(header).alignAll(Alignment.MIDDLE_LEFT),
				new MHorizontalLayout(init, end, currency, query).alignAll(Alignment.MIDDLE_LEFT).withAlign(query,
						Alignment.BOTTOM_LEFT),
				mainContent);

		setMargin(new MarginInfo(false, false, false, false));
		expand(mainContent);
	}

	public Double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return Double.valueOf((double) tmp / factor);
	}

	private Table buildTableForExport() {
		Table atable = new Table();
		atable.setSizeFull();
		IndexedContainer cont = new IndexedContainer();
		cont.addContainerProperty("property", String.class, null);
		cont.addContainerProperty("owner", String.class, null);
		cont.addContainerProperty("occupant", String.class, null);
		cont.addContainerProperty("initSDF", String.class, null);
		cont.addContainerProperty("endSDF", String.class, null);
		cont.addContainerProperty("amount", Double.class, 0d);
		cont.addContainerProperty("ownerComission", Double.class, 0d);
		cont.addContainerProperty("clientComission", Double.class, 0d);
		cont.addContainerProperty("ownerComissionCharged", Double.class, 0d);
		cont.addContainerProperty("clientComissionCharged", Double.class, 0d);

		// cont.addContainerProperty("finalPaymentToOwner", Double.class, 0d);
		cont.addContainerProperty("nextEntryAmount", Double.class, 0d);
		cont.addContainerProperty("nextEntryOwnerCommision", Double.class, 0d);
		cont.addContainerProperty("nextEntryClientCommision", Double.class, 0d);

		atable.setContainerDataSource(cont);
		atable.setVisibleColumns("property", "owner", "occupant", "initSDF", "endSDF", "amount", "nextEntryAmount",
				"nextEntryOwnerCommision", "nextEntryClientCommision", "ownerComission", "clientComission",
				"ownerComissionCharged", "clientComissionCharged");
		atable.setColumnHeaders("Local", "Propietario", "Inquilino", "Inicio", "Fin", "Monto", "Monto prox. año",
				"Com. Inq. prox. año", "Com. Prop. prox. año", "Com. Prop.", "Com. Inq.", "C. Prop. Cob",
				"C. Inq. Cob.");

		for (ContractEntry centry : (Collection<ContractEntry>) table.getContainerDataSource().getItemIds()) {
			cont.addItem(centry.getId());
			cont.getContainerProperty(centry.getId(), "property").setValue(centry.getProperty());
			cont.getContainerProperty(centry.getId(), "owner").setValue(centry.getOwner());
			cont.getContainerProperty(centry.getId(), "occupant").setValue(centry.getOccupant());
			cont.getContainerProperty(centry.getId(), "initSDF").setValue(centry.getInitSDF());
			cont.getContainerProperty(centry.getId(), "endSDF").setValue(centry.getEndSDF());
			cont.getContainerProperty(centry.getId(), "amount").setValue(centry.getAmount());
			cont.getContainerProperty(centry.getId(), "ownerComission").setValue(centry.getOwnerComission());
			cont.getContainerProperty(centry.getId(), "clientComission").setValue(centry.getClientComission());
			cont.getContainerProperty(centry.getId(), "ownerComissionCharged")
					.setValue(centry.getOwnerComissionCharged());
			cont.getContainerProperty(centry.getId(), "clientComissionCharged")
					.setValue(centry.getClientComissionCharged());
			// cont.getContainerProperty(centry.getId(),
			// "finalPaymentToOwner").setValue(centry.getFinalPaymentToOwner());
			cont.getContainerProperty(centry.getId(), "nextEntryAmount").setValue(centry.getNextEntryAmount());
			cont.getContainerProperty(centry.getId(), "nextEntryOwnerCommision").setValue(centry.getNextEntryAmount());
			cont.getContainerProperty(centry.getId(), "nextEntryClientCommision").setValue(centry.getNextEntryAmount());

		}

		return atable;
	}

	private void buildFilterTable() {
		table = new FilterTable();
		table.setSizeFull();
		BeanItemContainer<ContractEntry> cont = new BeanItemContainer<ContractEntry>(ContractEntry.class);

		table.setContainerDataSource(cont);
		table.setVisibleColumns("property", "owner", "occupant", "initSDF", "endSDF", "amount", "nextEntryAmount",
				"nextEntryOwnerCommision", "nextEntryClientCommision", "ownerComission", "clientComission",
				"ownerComissionCharged", "clientComissionCharged");
		table.setColumnHeaders("Local", "Propietario", "Inquilino", "Inicio", "Fin", "Monto", "Monto prox. año",
				"Com. Inq. prox. año", "Com. Prop. prox. año", "Com. Prop.", "Com. Inq.", "C. Prop. Cob",
				"C. Inq. Cob.");

		table.setColumnFooter("endSDF", "Total:");
		table.setColumnFooter("amount", totalAmount.toString());
		table.setColumnFooter("ownerComission", totalOwnerComission.toString());
		table.setColumnFooter("clientComission", totalClientComission.toString());
		table.setColumnFooter("ownerComissionCharged", totalOwnerComissionCharged.toString());
		table.setColumnFooter("clientComissionCharged", totalClientComissionCharged.toString());

		// table.setColumnFooter("finalPaymentToOwner",
		// totalFinalPaymentToOwner.toString());
		table.setColumnFooter("nextEntryAmount", totalNextYearEntryAmount.toString());
		table.setColumnFooter("nextEntryClientCommision", totalNextYearClientCommision.toString());
		table.setColumnFooter("nextEntryOwnerCommision", totalNextYearOwnerCommision.toString());
		table.setFooterVisible(true);
		table.setFilterBarVisible(true);
		table.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				ContractEntry contract = (ContractEntry) event.getItemId();
				ContractEditForm w = new ContractEditForm(contract.getContract());
				Window window = new Window();
				window.setDraggable(false);
				window.setModal(true);
				window.setResizable(false);
				window.setCaption("Contrato");
				window.setWidth("95%");
				window.setHeight("95%");
				window.setStyleName("mipa");
				window.setContent(w);
				UI.getCurrent().addWindow(window);
			}
		});
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {

		buildFilterTable();
		table.setWidth("100%");
		table.setImmediate(true);

		layout();
		UI.getCurrent().setResizeLazy(true);
		Page.getCurrent().addBrowserWindowResizeListener(new Page.BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(Page.BrowserWindowResizeEvent browserWindowResizeEvent) {
				layout();
			}
		});
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

}
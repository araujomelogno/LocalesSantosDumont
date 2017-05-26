package uy.com.innobit.rem.presentation.view.contracts;

import java.text.DecimalFormat;
import java.util.Collection;

import org.vaadin.haijian.ExcelExporter;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

import uy.com.innobit.rem.presentation.component.ContractExpirationDataValue;
import uy.com.innobit.rem.presentation.component.ExpirationsTable;

@SuppressWarnings("serial")
public abstract class ContractExpirationsView extends MVerticalLayout implements View {

	// Introduce and configure some UI components used on this view
	protected ExpirationsTable table;
	protected String currency;
	MHorizontalLayout mainContent;

	Header header;

	private void layout() {
		removeAllComponents();
		Label label = new Label("(Día) <font color=\"green\">Monto", ContentMode.HTML);
		ExcelExporter excelExporter = new ExcelExporter(buildTableToExport());
		excelExporter.setCaption("Exportar");
		addComponents(new MHorizontalLayout(header, excelExporter).expand(header).alignAll(Alignment.MIDDLE_LEFT),
				label, mainContent);
		setMargin(new MarginInfo(false, true, true, true));
		expand(mainContent);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		header = new Header("Vencimientos " + currency).setHeaderLevel(2);
		mainContent = new MHorizontalLayout(table).withFullWidth().withMargin(false);
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

	public MHorizontalLayout getMainContent() {
		return mainContent;
	}

	public void setMainContent(MHorizontalLayout mainContent) {
		this.mainContent = mainContent;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Table buildTableToExport() {
		Double m1 = 0d;
		Double m2 = 0d;
		Double m3 = 0d;
		Double m4 = 0d;
		Double m5 = 0d;
		Double m6 = 0d;
		Double m7 = 0d;
		Double m8 = 0d;
		Double m9 = 0d;
		Double m10 = 0d;
		Double m11 = 0d;
		Double m12 = 0d;
		Double rentalTotal = 0d;
		Double ownerComissionTotal = 0d;
		Double clientComissionTotal = 0d;
		DecimalFormat formatter = new DecimalFormat("#,###");
		Table exportTable = new Table();
		IndexedContainer cont = new IndexedContainer();
		cont.addContainerProperty("property", String.class, "");
		cont.addContainerProperty("rentalTotal", Label.class, "");
		cont.addContainerProperty("m1", Label.class, "");
		cont.addContainerProperty("m2", Label.class, "");
		cont.addContainerProperty("m3", Label.class, "");
		cont.addContainerProperty("m4", Label.class, "");
		cont.addContainerProperty("m5", Label.class, "");
		cont.addContainerProperty("m6", Label.class, "");
		cont.addContainerProperty("m7", Label.class, "");
		cont.addContainerProperty("m8", Label.class, "");
		cont.addContainerProperty("m9", Label.class, "");
		cont.addContainerProperty("m10", Label.class, "");
		cont.addContainerProperty("m11", Label.class, "");
		cont.addContainerProperty("m12", Label.class, "");
		cont.addContainerProperty("m12", Label.class, "");
		cont.addContainerProperty("ownerComission", Label.class, "");
		cont.addContainerProperty("clientComission", Label.class, "");
		exportTable.setContainerDataSource(cont);
		exportTable.setColumnHeaders("Local", "Tot. Alq.", "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago",
				"Set", "Oct", "Nov", "Dic", "Com. Prop.", "Com. Ing.");
		for (ContractExpirationDataValue data : (Collection<ContractExpirationDataValue>) this.table
				.getContainerDataSource().getItemIds()) {
			Label label;
			exportTable.getContainerDataSource().addItem(data);
			exportTable.getContainerDataSource().getContainerProperty(data, "property").setValue(data.getProperty());
			exportTable.getContainerDataSource().getContainerProperty(data, "rentalTotal")
					.setValue(new Label(currency + formatter.format(data.getRentalTotal())));
			exportTable.getContainerDataSource().getContainerProperty(data, "ownerComission")
					.setValue(new Label(currency + formatter.format(data.getOwnerComission())));
			exportTable.getContainerDataSource().getContainerProperty(data, "clientComission")
					.setValue(new Label(currency + formatter.format(data.getClientComission())));
			label = new Label("(" + formatter.format(data.getD1()) + ")" + currency + formatter.format(data.getM1()),
					ContentMode.HTML);
			if (data.getM1() != 0d) {
				label = new Label(
						"(" + formatter.format(data.getD1()) + ")" + currency + formatter.format(data.getM1()),
						ContentMode.HTML);
				exportTable.getContainerDataSource().getContainerProperty(data, "m1").setValue(label);
			}

			if (data.getM2() != 0d) {
				label = new Label(
						"(" + formatter.format(data.getD2()) + "))" + currency + formatter.format(data.getM2()),
						ContentMode.HTML);
				exportTable.getContainerDataSource().getContainerProperty(data, "m2").setValue(label);

			}
			if (data.getM3() != 0d) {
				label = new Label(
						"(" + formatter.format(data.getD3()) + "))" + currency + formatter.format(data.getM3()),
						ContentMode.HTML);
				exportTable.getContainerDataSource().getContainerProperty(data, "m3").setValue(label);
			}
			if (data.getM4() != 0d) {
				label = new Label(
						"(" + formatter.format(data.getD4()) + ")" + currency + formatter.format(data.getM4()),
						ContentMode.HTML);
				exportTable.getContainerDataSource().getContainerProperty(data, "m4").setValue(label);
			}
			if (data.getM5() != 0d) {
				label = new Label(
						"(" + formatter.format(data.getD5()) + ")" + currency + formatter.format(data.getM5()),
						ContentMode.HTML);
				exportTable.getContainerDataSource().getContainerProperty(data, "m5").setValue(label);
			}

			if (data.getM6() != 0d) {
				label = new Label(
						"(" + formatter.format(data.getD6()) + ")" + currency + formatter.format(data.getM6()),
						ContentMode.HTML);
				exportTable.getContainerDataSource().getContainerProperty(data, "m6").setValue(label);
			}
			if (data.getM7() != 0d) {
				label = new Label(
						"(" + formatter.format(data.getD7()) + ")" + currency + formatter.format(data.getM7()),
						ContentMode.HTML);
				exportTable.getContainerDataSource().getContainerProperty(data, "m7").setValue(label);
			}
			if (data.getM8() != 0d) {
				label = new Label(
						"(" + formatter.format(data.getD8()) + ")" + currency + formatter.format(data.getM8()),
						ContentMode.HTML);
				exportTable.getContainerDataSource().getContainerProperty(data, "m8").setValue(label);
			}
			if (data.getM9() != 0d) {
				label = new Label(
						"(" + formatter.format(data.getD9()) + ")" + currency + formatter.format(data.getM9()),
						ContentMode.HTML);
				exportTable.getContainerDataSource().getContainerProperty(data, "m9").setValue(label);
			}
			if (data.getM10() != 0d) {
				label = new Label(
						"(" + formatter.format(data.getD10()) + ")" + currency + formatter.format(data.getM10()),
						ContentMode.HTML);
				exportTable.getContainerDataSource().getContainerProperty(data, "m10").setValue(label);
			}
			if (data.getM11() != 0d) {
				label = new Label(
						"(" + formatter.format(data.getD11()) + ")" + currency + formatter.format(data.getM11()),
						ContentMode.HTML);
				exportTable.getContainerDataSource().getContainerProperty(data, "m11").setValue(label);
			}
			if (data.getM12() != 0d) {
				label = new Label(
						"(" + formatter.format(data.getD12()) + ")" + currency + formatter.format(data.getM12()),
						ContentMode.HTML);
				exportTable.getContainerDataSource().getContainerProperty(data, "m12").setValue(label);
			}

			m1 = m1 + data.getM1();
			m2 = m2 + data.getM2();
			m3 = m3 + data.getM3();
			m4 = m4 + data.getM4();
			m5 = m5 + data.getM5();
			m6 = m6 + data.getM6();
			m7 = m7 + data.getM7();
			m8 = m8 + data.getM8();
			m9 = m9 + data.getM9();
			m10 = m10 + data.getM10();
			m11 = m11 + data.getM11();
			m12 = m12 + data.getM12();
			rentalTotal = rentalTotal + data.getRentalTotal();
			ownerComissionTotal = ownerComissionTotal + data.getOwnerComission();
			clientComissionTotal = clientComissionTotal + data.getClientComission();

		}
		exportTable.addItem("total");
		exportTable.getContainerDataSource().getContainerProperty("total", "property").setValue("TOTAL");
		exportTable.getContainerDataSource().getContainerProperty("total", "m1")
				.setValue(new Label(currency + formatter.format(m1)));
		exportTable.getContainerDataSource().getContainerProperty("total", "m2")
				.setValue(new Label(currency + formatter.format(m2)));
		exportTable.getContainerDataSource().getContainerProperty("total", "m3")
				.setValue(new Label(currency + formatter.format(m3)));
		exportTable.getContainerDataSource().getContainerProperty("total", "m4")
				.setValue(new Label(currency + formatter.format(m4)));
		exportTable.getContainerDataSource().getContainerProperty("total", "m5")
				.setValue(new Label(currency + formatter.format(m5)));
		exportTable.getContainerDataSource().getContainerProperty("total", "m6")
				.setValue(new Label(currency + formatter.format(m6)));
		exportTable.getContainerDataSource().getContainerProperty("total", "m7")
				.setValue(new Label(currency + formatter.format(m7)));
		exportTable.getContainerDataSource().getContainerProperty("total", "m8")
				.setValue(new Label(currency + formatter.format(m8)));
		exportTable.getContainerDataSource().getContainerProperty("total", "m9")
				.setValue(new Label(currency + formatter.format(m9)));
		exportTable.getContainerDataSource().getContainerProperty("total", "m10")
				.setValue(new Label(currency + formatter.format(m10)));
		exportTable.getContainerDataSource().getContainerProperty("total", "m11")
				.setValue(new Label(currency + formatter.format(m11)));
		exportTable.getContainerDataSource().getContainerProperty("total", "m12")
				.setValue(new Label(currency + formatter.format(m12)));
		exportTable.getContainerDataSource().getContainerProperty("total", "rentalTotal")
				.setValue(new Label(currency + formatter.format(rentalTotal)));
		exportTable.getContainerDataSource().getContainerProperty("total", "ownerComission")
				.setValue(new Label(currency + formatter.format(ownerComissionTotal)));
		exportTable.getContainerDataSource().getContainerProperty("total", "clientComission")
				.setValue(new Label(currency + formatter.format(clientComissionTotal)));
		return exportTable;
	}

}

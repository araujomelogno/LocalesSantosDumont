package uy.com.innobit.rem.presentation.component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.business.managers.ContractManager;
import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractCharge;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractExpiration;
import uy.com.innobit.rem.presentation.view.contracts.ContractEditForm;

@SuppressWarnings("serial")
public class ExpirationsTable extends Table {
	private Double m1 = 0d;
	private Double m2 = 0d;
	private Double m3 = 0d;
	private Double m4 = 0d;
	private Double m5 = 0d;
	private Double m6 = 0d;
	private Double m7 = 0d;
	private Double m8 = 0d;
	private Double m9 = 0d;
	private Double m10 = 0d;
	private Double m11 = 0d;
	private Double m12 = 0d;

	private Double rentalTotal = 0d;
	private Double ownerComissionTotal = 0d;
	private Double clientComissionTotal = 0d;

	private DecimalFormat formatter = new DecimalFormat("#,###");
	private String currency;

	private Map<String, ContractExpirationDataValue> map = new HashMap<String, ContractExpirationDataValue>();

	public ExpirationsTable(String currency) {
		this.currency = currency;
		addStyleName(ValoTheme.TABLE_BORDERLESS);
		addStyleName(ValoTheme.TABLE_NO_STRIPES);
		addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
		addStyleName(ValoTheme.TABLE_SMALL);
		setRowHeaderMode(RowHeaderMode.HIDDEN);
		setColumnHeaderMode(ColumnHeaderMode.EXPLICIT);
		setSizeFull();
		this.setSortEnabled(false);
		IndexedContainer cont = new IndexedContainer();
		cont.addContainerProperty("property", String.class, "");
		cont.addContainerProperty("rentalTotal", String.class, "");
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
		cont.addContainerProperty("ownerComission", String.class, "");
		cont.addContainerProperty("clientComission", String.class, "");
		setContainerDataSource(cont);
		setColumnHeaders("Local", "Tot. Alq.", "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Set", "Oct",
				"Nov", "Dic", "Com. Prop.", "Com. Ing.");
		List<ContractExpiration> currentYearExirations = ContractManager.getInstance()
				.getContractActualExpirations(currency);
		for (ContractExpiration ce : currentYearExirations) {
			if (ce.getEntry().getProperty() != null && !ce.getEntry().getProperty().equalsIgnoreCase("")) {

				ContractExpirationDataValue ced;
				if (map.containsKey(ce.getEntry().getProperty()))
					ced = map.get(ce.getEntry().getProperty());
				else {
					ced = new ContractExpirationDataValue();
					ced.setContract(ce.getEntry().getContract());
					ced.setProperty(ce.getEntry().getProperty());
					ced.setClientComission(ce.getEntry().getClientComission());
					ced.setOwnerComission(ce.getEntry().getOwnerComission());
					ced.setRentalTotal(ce.getEntry().getAmount());
					map.put(ced.getProperty(), ced);
				}
				Double aux = 0d;
				for (ContractCharge cc : ce.getContractCharges())
					aux = aux + cc.getAmount();
				switch (ce.getExpectedDate().getMonth()) {
				case 0:
					ced.setM1(ced.getM1() + ce.getAmount() - aux);
					ced.setD1(ce.getExpectedDate().getDate());
					break;
				case 1:
					ced.setM2(ced.getM2() + ce.getAmount() - aux);
					ced.setD2(ce.getExpectedDate().getDate());
					break;

				case 2:
					ced.setM3(ced.getM3() + ce.getAmount() - aux);
					ced.setD3(ce.getExpectedDate().getDate());
					break;
				case 3:
					ced.setM4(ced.getM4() + ce.getAmount() - aux);
					ced.setD4(ce.getExpectedDate().getDate());
					break;
				case 4:
					ced.setM5(ced.getM5() + ce.getAmount() - aux);
					ced.setD5(ce.getExpectedDate().getDate());
					break;
				case 5:
					ced.setM6(ced.getM6() + ce.getAmount() - aux);
					ced.setD6(ce.getExpectedDate().getDate());
					break;
				case 6:
					ced.setM7(ced.getM7() + ce.getAmount() - aux);
					ced.setD7(ce.getExpectedDate().getDate());
					break;
				case 7:
					ced.setM8(ced.getM8() + ce.getAmount() - aux);
					ced.setD8(ce.getExpectedDate().getDate());
					break;
				case 8:
					ced.setM9(ced.getM9() + ce.getAmount() - aux);
					ced.setD9(ce.getExpectedDate().getDate());
					break;
				case 9:
					ced.setM10(ced.getM10() + ce.getAmount() - aux);
					ced.setD10(ce.getExpectedDate().getDate());
					break;
				case 10:
					ced.setM11(ced.getM11() + ce.getAmount() - aux);
					ced.setD11(ce.getExpectedDate().getDate());
					break;
				case 11:
					ced.setM12(ced.getM12() + ce.getAmount() - aux);
					ced.setD12(ce.getExpectedDate().getDate());
					break;
				default:
					break;
				}
			}

		}

		List<ContractExpirationDataValue> list = new ArrayList<ContractExpirationDataValue>(map.values());
		Collections.sort(list, new Comparator<ContractExpirationDataValue>() {
			@Override
			public int compare(ContractExpirationDataValue o1, ContractExpirationDataValue o2) {

				return o1.getProperty().toLowerCase().trim().compareTo(o2.getProperty().toLowerCase().trim());
			}
		});
		addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				Contract contract = ((ContractExpirationDataValue) event.getItemId()).getContract();
				ContractEditForm w = new ContractEditForm(contract);
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
		for (ContractExpirationDataValue data : list) {
			getContainerDataSource().addItem(data);
			getContainerDataSource().getContainerProperty(data, "property").setValue(data.getProperty());
			getContainerDataSource().getContainerProperty(data, "rentalTotal")
					.setValue(currency + formatter.format(data.getRentalTotal()));
			getContainerDataSource().getContainerProperty(data, "ownerComission")
					.setValue(currency + formatter.format(data.getOwnerComission()));
			getContainerDataSource().getContainerProperty(data, "clientComission")
					.setValue(currency + formatter.format(data.getClientComission()));
			Label label;
			if (data.getM1() != 0d) {
				label = new Label("<font color=\"red\">(" + formatter.format(data.getD1())
						+ ")</font><font color=\"green\">" + currency + formatter.format(data.getM1()) + "</font>",
						ContentMode.HTML);
				getContainerDataSource().getContainerProperty(data, "m1").setValue(label);
			}

			if (data.getM2() != 0d) {
				label = new Label("<font color=\"red\">(" + formatter.format(data.getD2())
						+ "))</font><font color=\"green\">" + currency + formatter.format(data.getM2()) + "</font>",
						ContentMode.HTML);
				getContainerDataSource().getContainerProperty(data, "m2").setValue(label);

			}
			if (data.getM3() != 0d) {
				label = new Label("<font color=\"red\">(" + formatter.format(data.getD3())
						+ "))</font><font color=\"green\">" + currency + formatter.format(data.getM3()) + "</font>",
						ContentMode.HTML);
				getContainerDataSource().getContainerProperty(data, "m3").setValue(label);
			}
			if (data.getM4() != 0d) {
				label = new Label("<font color=\"red\">(" + formatter.format(data.getD4())
						+ ")</font><font color=\"green\">" + currency + formatter.format(data.getM4()) + "</font>",
						ContentMode.HTML);
				getContainerDataSource().getContainerProperty(data, "m4").setValue(label);
			}
			if (data.getM5() != 0d) {
				label = new Label("<font color=\"red\">(" + formatter.format(data.getD5())
						+ ")</font><font color=\"green\">" + currency + formatter.format(data.getM5()) + "</font>",
						ContentMode.HTML);
				getContainerDataSource().getContainerProperty(data, "m5").setValue(label);
			}

			if (data.getM6() != 0d) {
				label = new Label("<font color=\"red\">(" + formatter.format(data.getD6())
						+ ")</font><font color=\"green\">" + currency + formatter.format(data.getM6()) + "</font>",
						ContentMode.HTML);
				getContainerDataSource().getContainerProperty(data, "m6").setValue(label);
			}
			if (data.getM7() != 0d) {
				label = new Label("<font color=\"red\">(" + formatter.format(data.getD7())
						+ ")</font><font color=\"green\">" + currency + formatter.format(data.getM7()) + "</font>",
						ContentMode.HTML);
				getContainerDataSource().getContainerProperty(data, "m7").setValue(label);
			}
			if (data.getM8() != 0d) {
				label = new Label("<font color=\"red\">(" + formatter.format(data.getD8())
						+ ")</font><font color=\"green\">" + currency + formatter.format(data.getM8()) + "</font>",
						ContentMode.HTML);
				getContainerDataSource().getContainerProperty(data, "m8").setValue(label);
			}
			if (data.getM9() != 0d) {
				label = new Label("<font color=\"red\">(" + formatter.format(data.getD9())
						+ ")</font><font color=\"green\">" + currency + formatter.format(data.getM9()) + "</font>",
						ContentMode.HTML);
				getContainerDataSource().getContainerProperty(data, "m9").setValue(label);
			}
			if (data.getM10() != 0d) {
				label = new Label("<font color=\"red\">(" + formatter.format(data.getD10())
						+ ")</font><font color=\"green\">" + currency + formatter.format(data.getM10()) + "</font>",
						ContentMode.HTML);
				getContainerDataSource().getContainerProperty(data, "m10").setValue(label);
			}
			if (data.getM11() != 0d) {
				label = new Label("<font color=\"red\">(" + formatter.format(data.getD11())
						+ ")</font><font color=\"green\">" + currency + formatter.format(data.getM11()) + "</font>",
						ContentMode.HTML);
				getContainerDataSource().getContainerProperty(data, "m11").setValue(label);
			}
			if (data.getM12() != 0d) {
				label = new Label("<font color=\"red\">(" + formatter.format(data.getD12())
						+ ")</font><font color=\"green\">" + currency + formatter.format(data.getM12()) + "</font>",
						ContentMode.HTML);
				getContainerDataSource().getContainerProperty(data, "m12").setValue(label);
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
		setColumnFooter("m1", currency + formatter.format(m1));
		setColumnFooter("m2", currency + formatter.format(m2));
		setColumnFooter("m3", currency + formatter.format(m3));
		setColumnFooter("m4", currency + formatter.format(m4));
		setColumnFooter("m5", currency + formatter.format(m5));
		setColumnFooter("m6", currency + formatter.format(m6));
		setColumnFooter("m7", currency + formatter.format(m7));
		setColumnFooter("m8", currency + formatter.format(m8));
		setColumnFooter("m9", currency + formatter.format(m9));
		setColumnFooter("m10", currency + formatter.format(m10));
		setColumnFooter("m11", currency + formatter.format(m11));
		setColumnFooter("m12", currency + formatter.format(m12));
		setColumnFooter("rentalTotal", currency + formatter.format(rentalTotal));
		setColumnFooter("ownerComission", currency + formatter.format(ownerComissionTotal));
		setColumnFooter("clientComission", currency + formatter.format(clientComissionTotal));
		this.setFooterVisible(true);

	}

}

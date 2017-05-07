package uy.com.innobit.rem.presentation.view.contracts;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;

import org.tepi.filtertable.FilterTable;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;
import org.vaadin.viritin.button.DeleteButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.button.PrimaryButton;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomTable.RowHeaderMode;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.business.managers.ContractManager;
import uy.com.innobit.rem.business.managers.OccupantManager;
import uy.com.innobit.rem.persistence.datamodel.clients.Occupant;
import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractCharge;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractCharge;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractEntry;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractPayment;
import uy.com.innobit.rem.presentation.RemUI;
import uy.com.innobit.rem.presentation.view.DashboardViewType;
import uy.com.innobit.rem.presentation.view.owners.OwnerFilterGenerator;
import uy.com.innobit.rem.presentation.view.owners.OwnersFilterDecorator;
import uy.com.innobit.rem.presentation.view.properties.PropertyForm;

@SuppressWarnings("serial")
public class ContractEditView extends AbstractForm<Contract>implements View {
	Header header = new Header("Contrato").setHeaderLevel(2);
	MTextField propertyName = new MTextField("Propiedad:");
	MTextField ownerName = new MTextField("Propietario:");
	MButton occupant = new MButton("Inquilino:");
	MDateField init = new MDateField("Inicio:");
	MDateField end = new MDateField("Fin:");
	MTextField notDays = new MTextField("Días de recordatorio:");
	CheckBox warranty = new CheckBox("Garantía");
	CheckBox cede = new CheckBox("Cede");
	MTextField warrantyType = new MTextField("Tipo de garantía");
	MTextArea obs = new MTextArea("Observaciones").withFullWidth();
	MTextField totalPayments = new MTextField("Pagos Totales:");
	MTextField totalCharges = new MTextField("Cobros Totales:");
	Table contractEntries;
	ContractEntry entry;
	PropertyForm propertyForm;
	private SimpleFileDownloader downloader = new SimpleFileDownloader();

	private Window occupantList;
	private FilterTable occupantsTable;

	private Window paymentsWindow;
	private Window chargesWindow;
	private byte[] uploadData;
	private String uploadDataName;
	// private void layout() {
	// addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
	// obs.setInputPrompt("Observaciones");
	// obs.setRows(3);
	// warrantyType.setInputPrompt("Tipo de garantía");
	// propertyName.setStyleName(ValoTheme.BUTTON_LINK);
	// propertyName.setReadOnly(true);
	// ownerName.setStyleName(ValoTheme.BUTTON_LINK);
	// ownerName.setReadOnly(true);
	// occupant.setStyleName(ValoTheme.BUTTON_LINK);
	// occupant.addClickListener(new ClickListener() {
	// @Override
	// public void buttonClick(ClickEvent event) {
	// buildOccupantsListWindow();
	// RemUI.getCurrent().addWindow(occupantList);
	//
	// }
	// });
	// contractEntries = buildTable();
	//
	// if (entity != null && entity.getProperty() != null) {
	// if (entity.getProperty().getOwner() != null)
	// ownerName.setCaption("Propietario: " +
	// entity.getProperty().getOwner().getName());
	// propertyName.setCaption("Propiedad: " + entity.getProperty().getName());
	// for (ContractEntry ce : entity.getEntries())
	// contractEntries.getContainerDataSource().addItem(ce);
	// }
	// end.addValueChangeListener(new ValueChangeListener() {
	// @Override
	// public void valueChange(ValueChangeEvent event) {
	// Date initDate = init.getValue();
	// Date endDate = end.getValue();
	// int years = endDate.getYear() - initDate.getYear();
	// for (int i = 0; i < years; i++) {
	// ContractEntry entry = new ContractEntry();
	// entry.setYearIndex(i + 1);
	// entity.getEntries().add(entry);
	// contractEntries.getContainerDataSource().addItem(entry);
	// }
	//
	// }
	// });
	// Panel main = new Panel();
	// main.setContent(new MVerticalLayout(header, new
	// MHorizontalLayout(propertyName, ownerName, occupant),
	// new MHorizontalLayout(init, end,
	// notDays).withStyleName(ValoTheme.FORMLAYOUT_LIGHT),
	// new MHorizontalLayout(cede, warranty, warrantyType,
	// obs).withStyleName(ValoTheme.FORMLAYOUT_LIGHT),
	// new MHorizontalLayout(contractEntries).withFullWidth(), getToolbar())
	// .withStyleName(ValoTheme.LAYOUT_CARD));
	// main.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
	//// addComponents(main);
	//// expand(main);
	//// setMargin(new MarginInfo(false, false, false, false));
	// }

	private void buildOccupantsListWindow() {
		occupantList = new Window("Inquilinos");
		occupantList.setModal(true);
		occupantList.setClosable(true);
		occupantList.setResizable(false);
		occupantList.setDraggable(false);
		occupantList.setWidth("65%");
		occupantList.setContent(buildOccupantFilterTable());

	}

	private FilterTable buildOccupantFilterTable() {
		occupantsTable = new FilterTable();
		occupantsTable.setFilterBarVisible(true);
		occupantsTable.setFilterDecorator(new OwnersFilterDecorator());
		occupantsTable.setFilterGenerator(new OwnerFilterGenerator());
		occupantsTable.setPageLength(8);
		occupantsTable.setImmediate(true);
		occupantsTable.setRowHeaderMode(RowHeaderMode.HIDDEN);
		occupantsTable.setContainerDataSource(new BeanItemContainer<Occupant>(Occupant.class));
		occupantsTable.setVisibleColumns("socialReason", "name", "surname", "doc", "cell", "tel", "mail", "rut");
		occupantsTable.setColumnHeaders("Raz.Social", "Nombre", "Apellidio", "Documento", "Celular", "Teléfono", "Mail",
				"Rut");
		occupantsTable.addItemClickListener(new ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				getEntity().setOccupant((Occupant) event.getItemId());
				occupant.setCaption("Inquilino: " + getEntity().getOccupant().getName());
				occupantList.close();
			}
		});

		for (Occupant o : OccupantManager.getInstance().getAll()) {
			occupantsTable.getContainerDataSource().addItem(o);
			occupantsTable.getContainerDataSource().getContainerProperty(o, "name").setValue(o.getName());
			occupantsTable.getContainerDataSource().getContainerProperty(o, "rut").setValue(o.getRut());
			occupantsTable.getContainerDataSource().getContainerProperty(o, "mail").setValue(o.getMail());
			occupantsTable.getContainerDataSource().getContainerProperty(o, "cell").setValue(o.getCell());
			occupantsTable.addItem(o);
		}
		occupantsTable.setWidth("100%");
		return occupantsTable;
	}

	private Table buildContractEntriesTable() {
		BeanItemContainer<ContractEntry> container = new BeanItemContainer<ContractEntry>(ContractEntry.class);
		final Table table = new Table();
		table.setContainerDataSource(container);
		table.setVisibleColumns("yearIndex", "amount", "ownerComission", "clientComission", "finalPaymentToOwner",
				"ownerComissionCharged", "clientComissionCharged", "rentalCharged", "rentalPaid");
		table.setColumnHeaders("Año", "Monto", "Com. Prop.", "Com. Inq.", "Pago final a Prop.", "Com. a Prop",
				"Com. a Inq.", "Alq. Pag.", "Alq. Cob");
		table.setPageLength(4);
		table.addGeneratedColumn("Acciones", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button charges = new Button("Cobros");
				charges.setStyleName(ValoTheme.BUTTON_LINK);
				charges.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						entry = (ContractEntry) itemId;
						buildChargesWindow();
						RemUI.getCurrent().addWindow(chargesWindow);

					}

				});
				Button payments = new Button("Pagos");
				payments.setStyleName(ValoTheme.BUTTON_LINK);
				payments.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						entry = (ContractEntry) itemId;
						buildPaymentsWindow();
						RemUI.getCurrent().addWindow(paymentsWindow);
					}

				});
				HorizontalLayout hl = new HorizontalLayout(charges, payments);

				return hl;
			}
		});
		table.setEditable(true);
		table.setImmediate(true);
		table.setTableFieldFactory(new TableFieldFactory() {
			@Override
			public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
				String property = propertyId.toString();

				if (property.equalsIgnoreCase("amount") || property.equalsIgnoreCase("ownerComission")
						|| property.equalsIgnoreCase("clientComission")) {
					MTextField tf = new MTextField();
					tf.setConverter(Integer.class);
					tf.setWidth(1.7f, Unit.CM);
					return tf;
				}
				return null;
			}
		});
		table.setWidth("100%");
		return table;
	}

	private void buildChargesWindow() {
		chargesWindow = new Window("Cobros");
		chargesWindow.setModal(true);
		chargesWindow.setClosable(true);
		chargesWindow.setResizable(false);
		chargesWindow.setDraggable(false);
		chargesWindow.setWidth("70%");
		chargesWindow.setStyleName("mipa");

		final Table charges = buildChargeTable();
		charges.setWidth("100%");
		Button add = new Button("Agregar");
		final ComboBox source = new ComboBox("Origen:");
		source.addItem("Inquilino");
		source.addItem("Propietario");
		source.select("Inquilino");
		source.setEnabled(false);
		final ComboBox commission = new ComboBox("Comisión:");
		commission.addItem("Si");
		commission.addItem("No");
		commission.select("No");
		;
		commission.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (commission.getValue().toString().equalsIgnoreCase("No")) {
					source.select("Inquilino");
					source.setEnabled(false);

				} else {
					source.setEnabled(true);

				}

			}
		});

		final Upload upload = new Upload("", new Receiver() {
			@Override
			public OutputStream receiveUpload(final String filename, String mimeType) {
				return new ByteArrayOutputStream() {
					@Override
					public void close() throws IOException {
						if (filename.contains("pdf") || filename.contains("jpg") || filename.contains("jpeg")
								|| filename.contains("png")) {
							uploadData = toByteArray();
							uploadDataName = filename;
						} else {
							Notification.show("Tipo de imagen no soportada");
						}
					}
				};
			}
		});
		upload.setButtonCaption("Frente cheque");
		upload.setImmediate(true);
		HorizontalLayout hl = new HorizontalLayout(add);
		hl.setSpacing(true);

		final DateField date = new DateField("Fecha:");
		date.setValue(new Date());
		final MTextField amount = new MTextField("Monto:");
		amount.setConverter(Integer.class);
		final ComboBox type = new ComboBox("Tipo:");
		type.addItem("Efectivo");
		type.addItem("Cheque");
		type.select("Efectivo");

		final ComboBox currency = new ComboBox("Moneda:");
		currency.addItem("$");
		currency.addItem("US$");
		currency.select("$");

		final MTextField checkNumber = new MTextField("No. cheque:");
		final MTextField bank = new MTextField("Banco:");
		final DateField checkDate = new DateField("Fecha cheque:");

		checkNumber.setEnabled(false);
		checkDate.setEnabled(false);
		bank.setEnabled(false);
		upload.setEnabled(false);

		type.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (type.getValue().toString().equalsIgnoreCase("efectivo")) {
					checkNumber.setEnabled(false);
					checkDate.setEnabled(false);
					bank.setEnabled(false);
					upload.setEnabled(false);

				} else {
					checkNumber.setEnabled(true);
					checkDate.setEnabled(true);
					bank.setEnabled(true);
					upload.setEnabled(true);
				}

			}
		});

		GridLayout grid = new GridLayout(5, 2);
		grid.setWidth("100%");
		grid.setStyleName(ValoTheme.PANEL_WELL);
		grid.setSpacing(true);
		grid.addComponent(date, 0, 0);
		grid.addComponent(amount, 1, 0);
		grid.addComponent(currency, 2, 0);
		grid.addComponent(commission, 3, 0);
		grid.addComponent(source, 4, 0);
		grid.addComponent(type, 0, 1);
		grid.addComponent(bank, 1, 1);
		grid.addComponent(checkDate, 2, 1);
		grid.addComponent(checkNumber, 3, 1);
		grid.addComponent(upload, 4, 1);

		grid.setWidth("100%");
		grid.setStyleName("mipa");
		VerticalLayout layout = new VerticalLayout(grid, hl, charges);
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);

		add.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (date.getValue() == null) {
					Notification.show("Debe ingresar fecha para el pago");
					return;
				}
				if (amount.getValue() == null || amount.getValue().isEmpty()) {
					Notification.show("Debe ingresar monto  para el pago");
					return;
				}
				ContractCharge p = new ContractCharge();
				p.setSOURCE(source.getValue().toString());
				p.setPaymentDate(date.getValue());
				p.setAmount(new Double(amount.getValue().replace(",", "")));
				p.setCurrency(currency.getValue().toString());
				p.setCommission(commission.getValue().toString().equalsIgnoreCase("si"));
				p.setType(type.getValue().toString());
				if (type.getValue().toString().equalsIgnoreCase("cheque")) {
					p.setCheckPayment(true);
					p.setCheckDate(checkDate.getValue());
					p.setCheckNumber(checkNumber.getValue());
					p.setBank(bank.getValue());
					p.setCheckImage(uploadData);
					p.setCheckName(uploadDataName);
				}
				entry.getContractCharges().add(p);
				if (p.getCommission()) {
					if (source.getValue().toString().equalsIgnoreCase("propietario")) {
						entry.setOwnerComissionCharged(entry.getOwnerComissionCharged() + p.getAmount());
					} else {
						entry.setClientComissionCharged(entry.getClientComissionCharged() + p.getAmount());
					}
				} else {
					entry.setRentalCharged(entry.getRentalCharged() + p.getAmount());
				}
				p.setEntry(entry);
				ContractManager.getInstance().updateContract(getEntity());
				charges.getContainerDataSource().addItem(p);
				date.setValue(new Date());
				amount.setValue("");
				currency.setValue("$");
				type.setValue("Efectivo");
				commission.setValue("No");
				bank.setValue("");
				checkDate.setValue(null);
				checkNumber.setValue("");
				source.setValue("Inquilino");

				uploadData = null;
				uploadDataName = "";

			}
		});
		charges.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				ContractCharge charge = (ContractCharge) charges.getValue();
				date.setValue(charge.getPaymentDate());
				amount.setValue(charge.getAmount().toString());
				currency.setValue(charge.getCurrency());
				type.setValue(charge.getCheckPayment() ? "Cheque" : "Efectivo");
				commission.setValue(charge.getCommission() ? "Si" : "No");
				bank.setValue(charge.getBank());
				checkDate.setValue(charge.getCheckDate());
				checkNumber.setValue(charge.getCheckNumber());
				source.setValue(charge.getSOURCE());
				uploadData = charge.getCheckImage();
				uploadDataName = charge.getCheckName();
			}
		});
		chargesWindow.setContent(layout);
	}

	public Table buildChargeTable() {
		BeanItemContainer<ContractCharge> container = new BeanItemContainer<ContractCharge>(ContractCharge.class);
		final Table table = new Table();
		table.setContainerDataSource(container);
		table.setVisibleColumns("paymentDateSDF", "SOURCE", "type", "currency", "amount", "checkDateSDF", "checkNumber",
				"bank");
		table.setColumnHeaders("Fecha", "Origen", "Tipo", "", "Monto", "Fecha Ch.", "No.Ch.", "Banco");
		table.setPageLength(4);
		table.setImmediate(true);
		table.setBuffered(false);
		table.addGeneratedColumn("url", new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				final ContractCharge c = (ContractCharge) itemId;
				Button button = new Button("Cheque");

				button.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						final StreamResource resource = new StreamResource(new StreamSource() {
							@Override
							public InputStream getStream() {
								return new ByteArrayInputStream(uploadData);
							}
						}, uploadDataName);
						downloader.setFileDownloadResource(resource);
						downloader.download();
					}
				});
				button.setStyleName(ValoTheme.BUTTON_LINK);
				button.setEnabled(c.getCheckPayment() && c.getCheckImage() != null && c.getCheckImage().length != 0);
				return button;
			}
		});
		table.setColumnHeader("url", "cheque");

		table.addGeneratedColumn("delete", new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				final ContractCharge c = (ContractCharge) itemId;
				Button button = new Button("Borrar");
				button.setStyleName(ValoTheme.BUTTON_LINK);
				button.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						table.getContainerDataSource().removeItem(c);
						entry.getContractCharges().remove(c);
					}
				});
				if (c.getCommission()) {
					if (source.getValue().toString().equalsIgnoreCase("propietario")) {
						entry.setOwnerComissionCharged(entry.getOwnerComissionCharged() - c.getAmount());
					} else {
						entry.setClientComissionCharged(entry.getClientComissionCharged() - c.getAmount());
					}
				} else {
					entry.setRentalCharged(entry.getRentalCharged() - c.getAmount());
				}
				ContractManager.getInstance().updateContract(getEntity());
				return button;
			}
		});
		table.setColumnHeader("delete", " ");
		for (ContractCharge c : entry.getContractCharges())
			table.getContainerDataSource().addItem(c);
		return table;
	}

	private void buildPaymentsWindow() {
		paymentsWindow = new Window("Pagos");
		paymentsWindow.setModal(true);
		paymentsWindow.setClosable(true);
		paymentsWindow.setResizable(false);
		paymentsWindow.setDraggable(false);
		final Table payments = buildPayments();

		Button add = new Button("Agregar");

		final DateField date = new DateField("Fecha:");
		final MTextField amount = new MTextField("Monto:");
		amount.setConverter(Integer.class);

		final ComboBox type = new ComboBox("Tipo:");
		type.addItem("Efectivo");
		type.addItem("Cheque");
		type.select("Efectivo");

		final MTextField checkNumber = new MTextField("No. cheque:");
		final MTextField bank = new MTextField("Banco:");
		final DateField checkDate = new DateField("Fecha cheque");
		GridLayout grid = new GridLayout(3, 2);
		grid.setSpacing(true);
		grid.addComponent(date, 0, 0);
		grid.addComponent(amount, 1, 0);
		grid.addComponent(type, 2, 0);
		grid.addComponent(bank, 0, 1);
		grid.addComponent(checkDate, 1, 1);
		grid.addComponent(checkNumber, 2, 1);
		grid.setWidthUndefined();
		VerticalLayout layout = new VerticalLayout(grid, add, payments);
		layout.setWidthUndefined();
		layout.setSpacing(true);
		layout.setMargin(true);
		type.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getValue().toString().equalsIgnoreCase("Efectivo")) {
					checkDate.setEnabled(false);
					checkNumber.setEnabled(false);
					bank.setEnabled(false);
				} else {
					checkDate.setEnabled(true);
					checkNumber.setEnabled(true);
					bank.setEnabled(true);
				}
			}
		});
		add.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (date.getValue() == null) {
					Notification.show("Debe ingresar fecha para el pago");
					return;
				}
				if (amount.getValue() == null || amount.getValue().isEmpty()) {
					Notification.show("Debe ingresar monto  para el pago");
					return;
				}
				ContractPayment p = new ContractPayment();
				p.setPaymentDate(date.getValue());
				p.setAmount(new BigDecimal(amount.getValue().replace(",", "")));
				if (type.getValue().toString().equalsIgnoreCase("cheque")) {
					p.setCheck(true);
					p.setCheckDate(checkDate.getValue());
					p.setCheckNumber(checkNumber.getValue());
					p.setBank(bank.getValue());
				}
				payments.getContainerDataSource().addItem(p);
				entry.getPayments().add(p);
			}
		});
		paymentsWindow.setContent(layout);
	}

	public Table buildPayments() {
		BeanItemContainer<ContractPayment> container = new BeanItemContainer<ContractPayment>(ContractPayment.class);
		final Table table = new Table();
		table.setContainerDataSource(container);
		table.setVisibleColumns("paymentDateSDF", "type", "amount", "checkDateSDF", "checkNumber", "bank");
		table.setColumnHeaders("Fecha", "Tipo", "Monto", "Fecha Ch.", "No.Ch.", "Banco");
		table.setPageLength(4);
		table.setImmediate(true);
		table.addGeneratedColumn("delete", new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				final ContractPayment c = (ContractPayment) itemId;
				Button button = new Button("Borrar");
				button.setStyleName(ValoTheme.BUTTON_LINK);
				button.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						table.getContainerDataSource().removeItem(c);
						entry.getPayments().remove(c);
					}
				});
				return button;
			}
		});
		table.setColumnHeader("delete", " ");
		for (ContractPayment payment : entry.getPayments())
			table.getContainerDataSource().addItem(payment);
		return table;
	}

	public HorizontalLayout getToolbar() {
		return new MHorizontalLayout(getDocumentsButton(), getGenerateDocumentButton(), getSaveButton(),
				getResetButton(), getDeleteButton());
	}

	private Component getGenerateDocumentButton() {
		Button button = new Button("Descargar Contrato");
		button.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show("En construcción");
			}
		});
		return button;
	}

	private Component getDocumentsButton() {
		Button button = new Button("Documentos");
		button.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show("En construcción");
			}
		});
		return button;
	}

	protected Button createCancelButton() {
		return new MButton("Cancelar");
	}

	private Button resetButton;

	public Button getResetButton() {
		if (resetButton == null) {
			setResetButton(createCancelButton());
		}
		return resetButton;
	}

	public void setResetButton(Button resetButton) {
		this.resetButton = resetButton;
		this.resetButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				propertyForm.resetContract(getEntity());
				UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PROPERTIES.getViewName());
			}
		});
	}

	protected Button createSaveButton() {
		return new PrimaryButton("Guardar");
	}

	private Button saveButton;

	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
		saveButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				propertyForm.saveContract(getEntity());
				UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PROPERTIES.getViewName());
			}
		});
	}

	public Button getSaveButton() {
		if (saveButton == null) {
			setSaveButton(createSaveButton());
		}
		return saveButton;
	}

	protected Button createDeleteButton() {
		return new DeleteButton("Borrar");
	}

	private Button deleteButton;

	public void setDeleteButton(final Button deleteButton) {
		this.deleteButton = deleteButton;
		deleteButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				propertyForm.deleteContract(getEntity());
				UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PROPERTIES.getViewName());
			}
		});
	}

	public Button getDeleteButton() {
		if (deleteButton == null) {
			setDeleteButton(createDeleteButton());
		}
		return deleteButton;
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		setEntity(VaadinSession.getCurrent().getAttribute(Contract.class));
		propertyForm = VaadinSession.getCurrent().getAttribute(PropertyForm.class);
		init();

	}

	@Override
	protected Component createContent() {
		propertyName.setReadOnly(true);
		ownerName.setReadOnly(true);
		occupant.setStyleName(ValoTheme.BUTTON_LINK);
		occupant.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				buildOccupantsListWindow();
				RemUI.getCurrent().addWindow(occupantList);

			}
		});
		contractEntries = buildContractEntriesTable();

		if (getEntity() != null && getEntity().getProperty() != null) {
			if (getEntity().getProperty().getOwner() != null)
				ownerName.setCaption("Propietario: " + getEntity().getProperty().getOwner().getName());
			propertyName.setCaption("Propiedad: " + getEntity().getProperty().getName());
			for (ContractEntry ce : getEntity().getEntries())
				contractEntries.getContainerDataSource().addItem(ce);
		}
		end.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date initDate = init.getValue();
				Date endDate = end.getValue();
				if (initDate != null && endDate != null) {
					int years = endDate.getYear() - initDate.getYear();
					if (years > getEntity().getEntries().size())
						for (int i = 0; i < years; i++) {
							ContractEntry entry = new ContractEntry();
							entry.setYearIndex(i + 1);
							getEntity().getEntries().add(entry);
							contractEntries.getContainerDataSource().addItem(entry);
						}
				}

			}
		});

		init.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date initDate = init.getValue();
				Date endDate = end.getValue();
				if (initDate != null && endDate != null) {
					int years = endDate.getYear() - initDate.getYear();
					if (years > getEntity().getEntries().size())
						for (int i = 0; i < years; i++) {
							ContractEntry entry = new ContractEntry();
							entry.setYearIndex(i + 1);
							getEntity().getEntries().add(entry);
							contractEntries.getContainerDataSource().addItem(entry);
						}
				}

			}
		});
		setStyleName(ValoTheme.LAYOUT_CARD);
		ownerName.setStyleName(ValoTheme.BUTTON_LINK);
		totalPayments.setReadOnly(true);
		totalCharges.setReadOnly(true);
		MFormLayout form = new MFormLayout(propertyName, ownerName, occupant, init, end, notDays, warranty,
				warrantyType, cede, totalPayments, totalCharges, obs).withFullWidth();

		form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		form.setComponentAlignment(ownerName, Alignment.MIDDLE_LEFT);
		Panel panel = new Panel(new MVerticalLayout(form, contractEntries).withFullWidth());
		panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		MVerticalLayout layout = new MVerticalLayout(new Header("Editar Contrato").setHeaderLevel(5), panel,
				getToolbar()).withStyleName(ValoTheme.LAYOUT_CARD);
		layout.setHeight("100%");
		setHeight("100%");
		layout.expand(panel);
		return layout;
	}

	// @Override
	// protected void lazyInit() {
	//
	// }

	void init() {
		setEagerValidation(true);
		getSaveButton().setCaption("Guardar");
		getResetButton().setCaption("Cancelar");
		getDeleteButton().setCaption("Borrar");
		if (getEntity() != null && getEntity().getProperty() != null) {
			if (getEntity().getProperty().getOwner() != null)
				ownerName.setCaption("Propietario: " + getEntity().getProperty().getOwner().getName());
			propertyName.setCaption("Propiedad: " + getEntity().getProperty().getName());
			for (ContractEntry ce : getEntity().getEntries())
				contractEntries.getContainerDataSource().addItem(ce);
		}
	}

}

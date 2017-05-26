package uy.com.innobit.rem.presentation.view.contracts;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.apache.james.mime4j.field.address.Mailbox;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Text;
import org.tepi.filtertable.FilterTable;
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
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.CustomTable.RowHeaderMode;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import de.steinwedel.messagebox.MessageBox;
import uy.com.innobit.rem.business.managers.ContractManager;
import uy.com.innobit.rem.business.managers.NotificationsManager;
import uy.com.innobit.rem.business.managers.OccupantManager;
import uy.com.innobit.rem.business.util.MailTemplateReader;
import uy.com.innobit.rem.business.util.MessageTemplates;
import uy.com.innobit.rem.persistence.datamodel.clients.Occupant;
import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractCharge;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractCharge;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractDocument;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractEntry;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractExpiration;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractNotification;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractPayment;
import uy.com.innobit.rem.presentation.RemUI;
import uy.com.innobit.rem.presentation.view.DashboardViewType;
import uy.com.innobit.rem.presentation.view.owners.OwnerFilterGenerator;
import uy.com.innobit.rem.presentation.view.owners.OwnersFilterDecorator;
import uy.com.innobit.rem.presentation.view.properties.PropertyForm;

@SuppressWarnings("serial")
public class ContractEditView extends AbstractForm<Contract>implements View {
	Header header = new Header("Contrato").setHeaderLevel(4);
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

	private FileResource resource = new FileResource(new File(
			(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/WEB-INF/contratoGenerado.docx")));

	private Window occupantList;
	private FilterTable occupantsTable;

	private Window expirationsWindow;
	private Window paymentsWindow;
	private Window chargesWindow;
	private Window remindersWindow;
	private Window documentsWindow;
	private Table documentsTable;

	private byte[] uploadData;
	private String uploadDataName;

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
		occupantsTable.addGeneratedColumn("goto", new CustomTable.ColumnGenerator() {

			@Override
			public Object generateCell(CustomTable source, Object itemId, Object columnId) {
				final Occupant occuppant = (Occupant) itemId;
				Button button = new Button("Ver");
				button.setStyleName(ValoTheme.BUTTON_LINK);
				button.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						VaadinSession.getCurrent().setAttribute(Occupant.class, occuppant);
						UI.getCurrent().getNavigator().navigateTo("/occuppants");

					}
				});
				return button;
			}

		});
		occupantsTable.setColumnHeader("goto", "");
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
		table.setVisibleColumns("yearIndex", "currency", "amount", "ownerComission", "clientComission",
				"finalPaymentToOwner", "ownerComissionCharged", "clientComissionCharged", "rentalCharged",
				"rentalPaid");
		table.setColumnHeaders("Año", "Moneda", "Monto", "Com. Prop.", "Com. Inq.", "Pago final a Prop.", "Com. a Prop",
				"Com. a Inq.", "Alq. Cob.", "Alq. Pag");
		table.setPageLength(4);
		table.setBuffered(false);
		table.setImmediate(true);
		table.addGeneratedColumn("Acciones", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button charges = new Button("Cobros");
				charges.setStyleName(ValoTheme.BUTTON_LINK);
				charges.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						if (getEntity().getId() == 0) {
							propertyForm.saveContract(getEntity());
							contractEntries.getContainerDataSource().removeAllItems();
							for (ContractEntry ce : getEntity().getEntries())
								contractEntries.getContainerDataSource().addItem(ce);
						}

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
						if (getEntity().getId() == 0) {
							propertyForm.saveContract(getEntity());
							contractEntries.getContainerDataSource().removeAllItems();
							for (ContractEntry ce : getEntity().getEntries())
								contractEntries.getContainerDataSource().addItem(ce);
						}
						entry = (ContractEntry) itemId;
						buildPaymentsWindow();
						RemUI.getCurrent().addWindow(paymentsWindow);
					}

				});

				Button expirations = new Button("Vencimientos");
				expirations.setStyleName(ValoTheme.BUTTON_LINK);
				expirations.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						if (getEntity().getId() == 0) {
							propertyForm.saveContract(getEntity());
							contractEntries.getContainerDataSource().removeAllItems();
							for (ContractEntry ce : getEntity().getEntries())
								contractEntries.getContainerDataSource().addItem(ce);
						}
						entry = (ContractEntry) itemId;
						buildExpirationsWindow();
						RemUI.getCurrent().addWindow(expirationsWindow);
					}

				});

				Button cancel = new Button();
				entry = (ContractEntry) itemId;
				if (entry.isActive())
					cancel.setCaption("Rescindir");
				else
					cancel.setCaption("Activar");
				cancel.setStyleName(ValoTheme.BUTTON_LINK);
				cancel.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						if (getEntity().getId() == 0) {
							propertyForm.saveContract(getEntity());
							contractEntries.getContainerDataSource().removeAllItems();
							for (ContractEntry ce : getEntity().getEntries())
								contractEntries.getContainerDataSource().addItem(ce);
						}
						entry = (ContractEntry) itemId;
						String message = "";
						if (entry.isActive())
							message = "Desea rescrinidr el contrato";
						else
							message = "Desea activar el contrato";
						MessageBox.createQuestion().withCaption(message).withOkButton(() -> {
							entry.setActive(!entry.isActive());
							ContractManager.getInstance().updateContract(getEntity());
							contractEntries.refreshRowCache();
						}).withCancelButton().open();
					}
				});

				MHorizontalLayout hl = new MHorizontalLayout(charges, payments, expirations, cancel).withSpacing(false)
						.withMargin(false);
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
				if (property.equalsIgnoreCase("currency")) {
					ComboBox currency = new ComboBox();
					currency.addItem("$");
					currency.addItem("US$");
					currency.select("$");
					currency.setWidth(2.5f, UNITS_CM);
					currency.setTextInputAllowed(false);
					currency.setNewItemsAllowed(false);
					currency.setNullSelectionAllowed(false);
					return currency;
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
		source.setTextInputAllowed(false);
		source.setNewItemsAllowed(false);
		source.setNullSelectionAllowed(false);
		final ComboBox commission = new ComboBox("Comisión:");
		commission.addItem("Si");
		commission.addItem("No");
		commission.select("No");
		commission.setTextInputAllowed(false);
		commission.setNewItemsAllowed(false);
		commission.setNullSelectionAllowed(false);
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
						uploadData = toByteArray();
						uploadDataName = filename;

					}
				};
			}
		});
		upload.setButtonCaption("Frente cheque");
		upload.setImmediate(true);
		HorizontalLayout hl = new HorizontalLayout(add);
		hl.setSpacing(true);

		final DateField date = new DateField("Fecha:");
		date.setDateFormat("dd/MM/yyyy");
		date.setValue(new Date());
		final MTextField amount = new MTextField("Monto:");
		amount.setConverter(Double.class);
		final ComboBox type = new ComboBox("Tipo:");
		type.addItem("Efectivo");
		type.addItem("Cheque");
		type.select("Efectivo");
		type.setNullSelectionAllowed(false);

		final ComboBox currency = new ComboBox("Moneda:");
		currency.addItem("$");
		currency.addItem("US$");
		currency.select("$");
		currency.setTextInputAllowed(false);
		currency.setNewItemsAllowed(false);
		currency.setNullSelectionAllowed(false);

		final MTextField checkNumber = new MTextField("No. cheque:");
		final MTextField bank = new MTextField("Banco:");
		final DateField checkDate = new DateField("Fecha cheque:");
		checkDate.setDateFormat("dd/MM/yyyy");
		checkNumber.setEnabled(false);
		checkDate.setEnabled(false);
		bank.setEnabled(false);
		upload.setEnabled(false);
		type.setTextInputAllowed(false);
		type.setNewItemsAllowed(false);
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
		grid.addComponent(amount, 2, 0);
		grid.addComponent(currency, 1, 0);
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

				if (currency.getValue() != null
						&& (currency.getValue().toString().equalsIgnoreCase("US$") || entry.getCurrency() != null
								&& !entry.getCurrency().equalsIgnoreCase(currency.getValue().toString()))) {
					final TextField input = new TextField("Ingrese la cotización del dólar");
					input.setValue("0");
					input.setConverter(Double.class);

					MessageBox.createQuestion().withCaption("Cotización Dólar").withMessage(input).withOkButton(() -> {
						ContractCharge p = new ContractCharge();
						p.setSource(source.getValue().toString());
						p.setPaymentDate(date.getValue());
						p.setAmount(new Double(amount.getValue().replace(",", "")));
						p.setCurrency(currency.getValue().toString());
						p.setCommission(commission.getValue().toString().equalsIgnoreCase("si"));
						p.setType(type.getValue().toString());
						p.setDollarCotization(Double.parseDouble(input.getValue()));
						if (type.getValue().toString().equalsIgnoreCase("cheque")) {
							p.setCheckPayment(true);
							p.setCheckDate(checkDate.getValue());
							p.setCheckNumber(checkNumber.getValue());
							p.setBank(bank.getValue());
							p.setCheckImage(uploadData);
							p.setCheckName(uploadDataName);
						}
						p.setEntry(entry);
						entry.getContractCharges().add(p);
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
						contractEntries.markAsDirty();
						contractEntries.refreshRowCache();
						uploadData = null;
						uploadDataName = "";
					}).withCancelButton().open();
				} else {
					ContractCharge p = new ContractCharge();
					p.setSource(source.getValue().toString());
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
					p.setEntry(entry);
					entry.getContractCharges().add(p);
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
					totalCharges.markAsDirty();
					totalPayments.markAsDirty();
					contractEntries.refreshRowCache();
					uploadData = null;
					uploadDataName = "";
				}

			}
		});
		charges.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				ContractCharge charge = (ContractCharge) charges.getValue();
				date.setValue(charge.getPaymentDate());
				amount.setValue(charge.getAmount().toString());
				currency.setValue(charge.getCurrency());
				type.setValue(charge.getCheckPayment() ? "Cheque" : "Efectivo");
				commission.setValue(charge.getCommission() ? "Si" : "No");
				bank.setValue(charge.getBank());
				checkDate.setValue(charge.getCheckDate());
				checkNumber.setValue(charge.getCheckNumber());
				source.setValue(charge.getSource());
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
		table.setVisibleColumns("paymentDateSDF", "source", "type", "currency", "comissionString", "amount",
				"checkDateSDF", "checkNumber", "bank");
		table.setColumnHeaders("Fecha", "Origen", "Tipo", "", "Comission", "Monto", "Fecha Ch.", "No.Ch.", "Banco");
		table.setPageLength(4);
		table.setImmediate(true);
		table.setBuffered(false);
		table.addGeneratedColumn("url", new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				final ContractCharge c = (ContractCharge) itemId;
				final Button button = new Button("Cheque");

				button.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						final StreamResource resource = new StreamResource(new StreamSource() {
							@Override
							public InputStream getStream() {
								return new ByteArrayInputStream(c.getCheckImage());
							}
						}, c.getCheckName());
						FileDownloader fileDownloader = new FileDownloader(resource);
						fileDownloader.extend(button);

					}
				});
				button.setStyleName(ValoTheme.BUTTON_LINK);
				button.setEnabled(c.getCheckPayment() && c.getCheckImage() != null && c.getCheckImage().length != 0);
				return button;
			}
		});
		table.setColumnHeader("url", "Cheque");

		table.addGeneratedColumn("delete", new ColumnGenerator() {

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final ContractCharge c = (ContractCharge) itemId;

				Button button = new Button("Borrar");
				button.setStyleName(ValoTheme.BUTTON_LINK);
				button.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						entry.getContractCharges().remove(c);
						c.setEntry(null);
						ContractManager.getInstance().updateContract(getEntity());
						ContractManager.getInstance().deleteContractCharge(c);
						source.getContainerDataSource().removeItem(itemId);
						contractEntries.markAsDirty();
						contractEntries.refreshRowCache();
						totalCharges.markAsDirty();
						totalPayments.markAsDirty();
					}
				});

				return button;
			}
		});
		table.setColumnHeader("delete", " ");
		for (ContractCharge c : entry.getContractCharges())
			table.getContainerDataSource().addItem(c);
		return table;
	}

	public Table buildPaymentsTable() {
		BeanItemContainer<ContractPayment> container = new BeanItemContainer<ContractPayment>(ContractPayment.class);
		final Table table = new Table();
		table.setContainerDataSource(container);
		table.setVisibleColumns("paymentDateSDF", "type", "currency", "amount", "checkDateSDF", "checkNumber", "bank");
		table.setColumnHeaders("Fecha", "Tipo", "", "Monto", "Fecha Ch.", "No.Ch.", "Banco");
		table.setPageLength(4);
		table.setImmediate(true);
		table.setBuffered(false);
		table.addGeneratedColumn("url", new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				final ContractPayment c = (ContractPayment) itemId;
				final Button button = new Button("Cheque");

				button.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						final StreamResource resource = new StreamResource(new StreamSource() {
							@Override
							public InputStream getStream() {
								return new ByteArrayInputStream(c.getCheckImage());
							}
						}, c.getCheckName());
						FileDownloader fileDownloader = new FileDownloader(resource);
						fileDownloader.extend(button);

					}
				});
				button.setStyleName(ValoTheme.BUTTON_LINK);
				button.setEnabled(c.getCheckPayment() && c.getCheckImage() != null && c.getCheckImage().length != 0);
				return button;
			}
		});
		table.setColumnHeader("url", "Cheque");

		table.addGeneratedColumn("delete", new ColumnGenerator() {

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final ContractPayment c = (ContractPayment) itemId;

				Button button = new Button("Borrar");
				button.setStyleName(ValoTheme.BUTTON_LINK);
				button.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						entry.getPayments().remove(c);
						c.setEntry(null);
						ContractManager.getInstance().updateContract(getEntity());
						ContractManager.getInstance().deleteContractPayment(c);
						source.getContainerDataSource().removeItem(itemId);
						contractEntries.markAsDirty();
						contractEntries.refreshRowCache();
						totalCharges.markAsDirty();
						totalPayments.markAsDirty();
					}
				});

				return button;
			}
		});
		table.setColumnHeader("delete", " ");
		for (ContractPayment c : entry.getPayments())
			table.getContainerDataSource().addItem(c);
		return table;
	}

	public Table buildExpirationsTable() {
		BeanItemContainer<ContractExpiration> container = new BeanItemContainer<ContractExpiration>(
				ContractExpiration.class);
		final Table table = new Table();
		table.setContainerDataSource(container);
		table.setVisibleColumns("expectedDateSdf", "currency", "amount");
		table.setColumnHeaders("Fecha", "", "Monto");
		table.setPageLength(4);
		table.setImmediate(true);
		table.setBuffered(false);

		table.addGeneratedColumn("delete", new ColumnGenerator() {

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final ContractExpiration c = (ContractExpiration) itemId;

				Button button = new Button("Borrar");
				button.setStyleName(ValoTheme.BUTTON_LINK);
				button.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						entry.getExpirations().remove(c);
						c.setEntry(null);
						ContractManager.getInstance().updateContract(getEntity());
						ContractManager.getInstance().delete(c);
						source.getContainerDataSource().removeItem(itemId);
					}
				});

				return button;
			}
		});
		table.setColumnHeader("delete", " ");
		for (ContractExpiration c : entry.getExpirations())
			table.getContainerDataSource().addItem(c);
		return table;
	}

	public Table buildRemindersTable() {
		BeanItemContainer<ContractNotification> container = new BeanItemContainer<ContractNotification>(
				ContractNotification.class);
		final Table table = new Table();
		table.setContainerDataSource(container);
		table.setVisibleColumns("dateSDF", "resolvedAux", "messageAux");
		table.setColumnHeaders("Fecha", "Resuelta", "Mensaje");
		table.setPageLength(4);
		table.setImmediate(true);
		table.setBuffered(false);

		table.addGeneratedColumn("delete", new ColumnGenerator() {

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final ContractNotification c = (ContractNotification) itemId;
				Button button = new Button("Borrar");
				button.setStyleName(ValoTheme.BUTTON_LINK);
				button.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						getEntity().getNotifications().remove(c);
						ContractManager.getInstance().deleteContractReminder(c);
						ContractManager.getInstance().updateContract(getEntity());
						source.getContainerDataSource().removeItem(itemId);
					}
				});

				return button;
			}
		});
		table.setColumnHeader("delete", " ");
		for (ContractNotification c : getEntity().getNotifications())
			table.getContainerDataSource().addItem(c);
		return table;
	}

	private void buildPaymentsWindow() {
		paymentsWindow = new Window("Pagos");
		paymentsWindow.setModal(true);
		paymentsWindow.setClosable(true);
		paymentsWindow.setResizable(false);
		paymentsWindow.setDraggable(false);
		paymentsWindow.setWidth("70%");
		paymentsWindow.setStyleName("mipa");

		final Table payments = buildPaymentsTable();
		payments.setWidth("100%");
		Button add = new Button("Agregar");

		final Upload upload = new Upload("", new Receiver() {
			@Override
			public OutputStream receiveUpload(final String filename, String mimeType) {
				return new ByteArrayOutputStream() {
					@Override
					public void close() throws IOException {
						uploadData = toByteArray();
						uploadDataName = filename;
					}
				};
			}
		});
		upload.setButtonCaption("Frente cheque");
		upload.setImmediate(true);
		HorizontalLayout hl = new HorizontalLayout(add);
		hl.setSpacing(true);

		final DateField date = new DateField("Fecha:");
		date.setDateFormat("dd/MM/yyyy");
		date.setValue(new Date());
		final MTextField amount = new MTextField("Monto:");
		amount.setConverter(Double.class);
		final ComboBox type = new ComboBox("Tipo:");
		type.addItem("Efectivo");
		type.addItem("Cheque");
		type.select("Efectivo");
		type.setNullSelectionAllowed(false);

		final ComboBox currency = new ComboBox("Moneda:");
		currency.addItem("$");
		currency.addItem("US$");
		currency.select("$");
		currency.setTextInputAllowed(false);
		currency.setNewItemsAllowed(false);
		currency.setNullSelectionAllowed(false);

		final MTextField checkNumber = new MTextField("No. cheque:");
		final MTextField bank = new MTextField("Banco:");
		final DateField checkDate = new DateField("Fecha cheque:");
		checkDate.setDateFormat("dd/MM/yyyy");
		checkNumber.setEnabled(false);
		checkDate.setEnabled(false);
		bank.setEnabled(false);
		upload.setEnabled(false);
		type.setTextInputAllowed(false);
		type.setNewItemsAllowed(false);
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

		GridLayout grid = new GridLayout(4, 2);
		grid.setWidth("100%");
		grid.setStyleName(ValoTheme.PANEL_WELL);
		grid.setSpacing(true);
		grid.addComponent(date, 0, 0);
		grid.addComponent(amount, 2, 0);
		grid.addComponent(currency, 1, 0);
		grid.addComponent(type, 3, 0);

		grid.addComponent(bank, 0, 1);
		grid.addComponent(checkDate, 1, 1);
		grid.addComponent(checkNumber, 2, 1);
		grid.addComponent(upload, 3, 1);

		grid.setWidth("100%");
		grid.setStyleName("mipa");
		VerticalLayout layout = new VerticalLayout(grid, hl, payments);
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

				if (currency.getValue() != null
						&& (currency.getValue().toString().equalsIgnoreCase("US$") || entry.getCurrency() != null
								&& !entry.getCurrency().equalsIgnoreCase(currency.getValue().toString()))) {
					final TextField input = new TextField("Ingrese la cotización del dólar");
					input.setValue("0");
					input.setConverter(Double.class);

					MessageBox.createQuestion().withCaption("Cotización Dólar").withMessage(input).withOkButton(() -> {
						ContractPayment p = new ContractPayment();
						p.setPaymentDate(date.getValue());
						p.setAmount(new Double(amount.getValue().replace(",", "")));
						p.setCurrency(currency.getValue().toString());
						p.setType(type.getValue().toString());
						p.setDollarCotization(Double.parseDouble(input.getValue()));
						if (type.getValue().toString().equalsIgnoreCase("cheque")) {
							p.setCheckPayment(true);
							p.setCheckDate(checkDate.getValue());
							p.setCheckNumber(checkNumber.getValue());
							p.setBank(bank.getValue());
							p.setCheckImage(uploadData);
							p.setCheckName(uploadDataName);
						}
						p.setEntry(entry);
						entry.getPayments().add(p);
						ContractManager.getInstance().updateContract(getEntity());

						payments.getContainerDataSource().addItem(p);
						date.setValue(new Date());
						amount.setValue("");
						currency.setValue("$");
						type.setValue("Efectivo");
						bank.setValue("");
						checkDate.setValue(null);
						checkNumber.setValue("");
						contractEntries.markAsDirty();
						contractEntries.refreshRowCache();
						uploadData = null;
						uploadDataName = "";
					}).withCancelButton().open();
				} else {
					ContractPayment p = new ContractPayment();
					p.setPaymentDate(date.getValue());
					p.setAmount(new Double(amount.getValue().replace(",", "")));
					p.setCurrency(currency.getValue().toString());
					p.setType(type.getValue().toString());
					if (type.getValue().toString().equalsIgnoreCase("cheque")) {
						p.setCheckPayment(true);
						p.setCheckDate(checkDate.getValue());
						p.setCheckNumber(checkNumber.getValue());
						p.setBank(bank.getValue());
						p.setCheckImage(uploadData);
						p.setCheckName(uploadDataName);
					}
					p.setEntry(entry);
					entry.getPayments().add(p);
					ContractManager.getInstance().updateContract(getEntity());

					payments.getContainerDataSource().addItem(p);
					date.setValue(new Date());
					amount.setValue("");
					currency.setValue("$");
					type.setValue("Efectivo");
					bank.setValue("");
					checkDate.setValue(null);
					checkNumber.setValue("");

					totalCharges.markAsDirty();
					totalPayments.markAsDirty();
					payments.refreshRowCache();
					contractEntries.refreshRowCache();
					uploadData = null;
					uploadDataName = "";
				}

			}
		});
		payments.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				ContractPayment payment = (ContractPayment) payments.getValue();
				date.setValue(payment.getPaymentDate());
				amount.setValue(payment.getAmount().toString());
				currency.setValue(payment.getCurrency());
				type.setValue(payment.getCheckPayment() ? "Cheque" : "Efectivo");

				bank.setValue(payment.getBank());
				checkDate.setValue(payment.getCheckDate());
				checkNumber.setValue(payment.getCheckNumber());

				uploadData = payment.getCheckImage();
				uploadDataName = payment.getCheckName();
			}

		});

		paymentsWindow.setContent(layout);
	}

	private void buildExpirationsWindow() {
		expirationsWindow = new Window("Vencimientos");
		expirationsWindow.setModal(true);
		expirationsWindow.setClosable(true);
		expirationsWindow.setResizable(false);
		expirationsWindow.setDraggable(false);
		expirationsWindow.setWidth("70%");
		expirationsWindow.setStyleName("mipa");

		final Table expirations = buildExpirationsTable();
		expirations.setWidth("100%");
		Button add = new Button("Agregar");

		final DateField date = new DateField("Fecha:");
		date.setDateFormat("dd/MM/yyyy");
		date.setValue(new Date());
		final MTextField amount = new MTextField("Monto:");
		amount.setConverter(Double.class);

		final ComboBox currency = new ComboBox("Moneda:");
		currency.addItem("$");
		currency.addItem("US$");
		currency.select("$");
		currency.setTextInputAllowed(false);
		currency.setNewItemsAllowed(false);
		currency.setNullSelectionAllowed(false);
		HorizontalLayout hl = new HorizontalLayout(date, currency, amount, add);
		hl.setComponentAlignment(add, Alignment.BOTTOM_CENTER);
		hl.setSpacing(true);

		VerticalLayout layout = new VerticalLayout(hl, expirations);
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

				ContractExpiration p = new ContractExpiration();
				p.setExpectedDate(date.getValue());
				p.setAmount(new Double(amount.getValue().replace(",", "")));
				p.setCurrency(currency.getValue().toString());
				p.setEntry(entry);
				entry.getExpirations().add(p);
				ContractManager.getInstance().updateContract(getEntity());

				expirations.getContainerDataSource().addItem(p);
				date.setValue(new Date());
				amount.setValue("");
				currency.setValue("$");

				expirations.refreshRowCache();
			}
		});
		expirations.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				ContractExpiration expiration = (ContractExpiration) expirations.getValue();
				date.setValue(expiration.getExpectedDate());
				amount.setValue(expiration.getAmount().toString());
				currency.setValue(expiration.getCurrency());
			}

		});

		expirationsWindow.setContent(layout);
	}

	private void buildRemindersWindow() {
		remindersWindow = new Window("Recordatorios");
		remindersWindow.setModal(true);
		remindersWindow.setClosable(true);
		remindersWindow.setResizable(false);
		remindersWindow.setDraggable(false);
		remindersWindow.setWidth("60%");
		remindersWindow.setStyleName("mipa");

		final Table reminders = buildRemindersTable();
		reminders.setWidth("100%");
		Button add = new Button("Agregar");

		HorizontalLayout hl = new HorizontalLayout(add);
		hl.setSpacing(true);

		final DateField date = new DateField("Fecha:");
		date.setDateFormat("dd/MM/yyyy");
		date.setValue(new Date());
		final CheckBox resolved = new CheckBox(":Resuelta");
		final MTextArea message = new MTextArea("Mensaje:");

		GridLayout grid = new GridLayout(3, 2);
		grid.setWidth("100%");
		grid.setStyleName(ValoTheme.PANEL_WELL);
		grid.setSpacing(true);
		grid.addComponent(date, 0, 0);
		grid.addComponent(resolved, 1, 0);
		grid.addComponent(message, 0, 1);

		grid.setWidth("100%");
		grid.setStyleName("mipa");
		VerticalLayout layout = new VerticalLayout(grid, hl, reminders);
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);

		add.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (date.getValue() == null) {
					Notification.show("Debe ingresar fecha para el recordatorio");
					return;
				}
				ContractNotification cn = new ContractNotification();
				cn.setMessage(message.getValue());
				cn.setNotificationDate(date.getValue());
				cn.setResolved(false);
				cn.setSent(false);
				cn.getRecipients().add(RemUI.get().getLoggedUser().getEmail());
				cn.setContract(getEntity());
				ContractManager.getInstance().saveContractReminder(cn);
				getEntity().getNotifications().add(cn);
				ContractManager.getInstance().updateContract(getEntity());
				reminders.getContainerDataSource().addItem(cn);

				date.setValue(new Date());
				resolved.setValue(false);
				message.setValue("");
			}

		});
		reminders.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				ContractNotification reminder = (ContractNotification) reminders.getValue();
				message.setValue(reminder.getMessage());
				date.setValue(reminder.getNotificationDate());
				resolved.setValue(reminder.isResolved());
			}

		});

		remindersWindow.setContent(layout);
	}

	public HorizontalLayout getToolbar() {
		return new MHorizontalLayout(getRemindersButton(), getDocumentsButton(), getGenerateDocumentButton(),
				getSaveButton(), getResetButton(), getDeleteButton());
	}

	private Component getGenerateDocumentButton() {

		final FileDownloader fileDownloader = new FileDownloader(resource);
		Button button = new Button("Descargar Contrato");
		button.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					WordprocessingMLPackage template = MessageTemplates.template;
					List texts = template.getMainDocumentPart().getJAXBNodesViaXPath("//w:t", true);

					for (Object obj : texts) {
						Text text = (Text) ((JAXBElement) obj).getValue();

						String textValue = text.getValue();

						textValue = textValue.replaceAll("PROPNAME", getEntity().getPropertyName());

						text.setValue(textValue);
					}

					template.save(new File((VaadinService.getCurrent().getBaseDirectory().getAbsolutePath()
							+ "/WEB-INF/contratoGenerado.docx")));
					resource = new FileResource(
							new File((VaadinService.getCurrent().getBaseDirectory().getAbsolutePath()
									+ "/WEB-INF/contratoGenerado.docx")));
					fileDownloader.setFileDownloadResource(resource);
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("No fue posible descargar el contrato", Type.ERROR_MESSAGE);
				}
			}
		});
		fileDownloader.extend(button);
		return button;
	}

	private Component getDocumentsButton() {
		Button button = new Button("Documentos");
		button.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

				buildDocumentsWindow();
				RemUI.getCurrent().addWindow(documentsWindow);
			}
		});
		return button;
	}

	private Component getRemindersButton() {
		Button button = new Button("Recordatorios");
		button.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				buildRemindersWindow();
				RemUI.getCurrent().addWindow(remindersWindow);
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
		init.setDateFormat("dd/MM/yyyy");
		end.setDateFormat("dd/MM/yyyy");
		end.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date initDate = init.getValue();
				Date endDate = end.getValue();
				if (initDate != null && endDate != null) {
					Date auxInitDate = new Date(initDate.getTime());
					Date auxEndDate = new Date(initDate.getTime());
					auxEndDate.setDate(auxEndDate.getDate() + 364);
					int years = endDate.getYear() - initDate.getYear();
					if (years > getEntity().getEntries().size()) {
						for (int i = 0; i < years; i++) {
							ContractEntry entry = new ContractEntry();
							entry.setYearIndex(i + 1);
							entry.setInit(new Date(auxInitDate.getTime()));
							entry.setEnd(new Date(auxEndDate.getTime()));
							entry.setContract(getEntity());
							ContractManager.getInstance().save(entry);

							Date aux = new Date(endDate.getTime());
							try {
								aux.setDate(aux.getDate() - Integer.parseInt(notDays.getValue()));
							} catch (Exception e) {
							}
							ContractNotification cn = new ContractNotification();
							cn.setContract(getEntity());
							cn.setMessage("El contrato de la propiedad " + getEntity().getPropertyName()
									+ " se vencerá el " + entry.getEndSDF());
							cn.setNotificationDate(aux);
							cn.getRecipients().add(RemUI.get().getLoggedUser().getEmail());
							ContractManager.getInstance().saveContractReminder(cn);
							getEntity().getNotifications().add(cn);
							ContractManager.getInstance().updateContract(getEntity());

							getEntity().getEntries().add(entry);
							contractEntries.getContainerDataSource().addItem(entry);
							auxInitDate.setYear(auxInitDate.getYear() + 1);
							auxEndDate.setYear(auxEndDate.getYear() + 1);
							if (i == years - 2)
								auxEndDate = new Date(endDate.getTime());
						}

					}

				}
				int h = 2;
				h++;

			}
		});

		init.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date initDate = init.getValue();
				Date endDate = end.getValue();
				if (initDate != null && endDate != null) {
					Date auxInitDate = new Date(initDate.getTime());
					Date auxEndDate = new Date(initDate.getTime());
					auxEndDate.setDate(auxEndDate.getDate() + 364);
					int years = endDate.getYear() - initDate.getYear();
					if (years > getEntity().getEntries().size())
						for (int i = 0; i < years; i++) {
							ContractEntry entry = new ContractEntry();
							entry.setYearIndex(i + 1);
							entry.setInit(auxInitDate);
							entry.setEnd(auxEndDate);
							entry.setContract(getEntity());
							ContractManager.getInstance().save(entry);
							getEntity().getEntries().add(entry);

							Date aux = new Date(endDate.getTime());
							try {
								aux.setDate(aux.getDate() - Integer.parseInt(notDays.getValue()));
							} catch (Exception e) {
							}
							ContractNotification cn = new ContractNotification();
							cn.setContract(getEntity());
							cn.setMessage("El contrato de la propiedad " + getEntity().getPropertyName()
									+ " se vencerá el " + entry.getEndSDF());
							cn.setNotificationDate(aux);
							cn.getRecipients().add(RemUI.get().getLoggedUser().getEmail());
							ContractManager.getInstance().saveContractReminder(cn);
							getEntity().getNotifications().add(cn);
							ContractManager.getInstance().updateContract(getEntity());

							getEntity().getEntries().add(entry);

							contractEntries.getContainerDataSource().addItem(entry);
							auxInitDate.setYear(auxInitDate.getYear() + 1);
							auxEndDate.setYear(auxEndDate.getYear() + 1);
							if (i == years - 2)
								auxEndDate = new Date(endDate.getTime());
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

		MVerticalLayout layout = new MVerticalLayout(header, panel, getToolbar()).withStyleName(ValoTheme.LAYOUT_CARD);
		layout.setHeight("100%");
		setHeight("100%");
		layout.expand(panel);
		return layout;
	}

	@Override
	protected void lazyInit() {
		super.lazyInit();
		getSaveButton().setEnabled(true);
		getResetButton().setEnabled(true);
	}

	void init() {
		try {
			setEagerValidation(true);
			getSaveButton().setCaption("Guardar");
			getResetButton().setCaption("Cancelar");
			getDeleteButton().setCaption("Borrar");
			if (getEntity() != null && getEntity().getProperty() != null) {
				if (getEntity().getProperty().getOwner() != null)
					ownerName.setCaption("Propietario: " + getEntity().getProperty().getOwner().getName());
				propertyName.setCaption("Propiedad: " + getEntity().getProperty().getName());
				List<ContractEntry> aux = new ArrayList<ContractEntry>(getEntity().getEntries());
				Collections.sort(aux, new Comparator<ContractEntry>() {

					@Override
					public int compare(ContractEntry o1, ContractEntry o2) {
						return o1.getYearIndex().compareTo(o2.getYearIndex());
					}
				});
				for (ContractEntry ce : aux)
					contractEntries.getContainerDataSource().addItem(ce);
			}

			getSaveButton().setEnabled(true);
			getResetButton().setEnabled(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buildDocumentsWindow() {
		documentsWindow = new Window("Documentos");
		documentsWindow.setModal(true);
		documentsWindow.setClosable(true);
		documentsWindow.setResizable(false);
		documentsWindow.setDraggable(false);
		documentsWindow.setWidth("65%");

		final Upload upload = new Upload("", new Receiver() {
			@Override
			public OutputStream receiveUpload(final String filename, String mimeType) {
				return new ByteArrayOutputStream() {
					@Override
					public void close() throws IOException {
						ContractDocument cd = new ContractDocument();
						cd.setName(filename);
						cd.setContent(toByteArray());
						ContractManager.getInstance().saveContractDocument(cd);
						getEntity().getDocuments().add(cd);
						ContractManager.getInstance().updateContract(getEntity());
						documentsTable.getContainerDataSource().addItem(cd);
					}
				};
			}
		});
		upload.setButtonCaption("Agregar");
		upload.setImmediate(true);
		MVerticalLayout layout = new MVerticalLayout(upload, buildDocumentsTable()).withFullWidth();
		documentsWindow.setContent(layout);

	}

	private Table buildDocumentsTable() {
		documentsTable = new Table();
		documentsTable.setPageLength(8);
		documentsTable.setImmediate(true);
		documentsTable.setWidth("100%");
		BeanItemContainer<ContractDocument> cont = new BeanItemContainer<ContractDocument>(ContractDocument.class);
		documentsTable.setContainerDataSource(cont);
		String[] visibleColumns = new String[] { "name" };
		documentsTable.setVisibleColumns(visibleColumns);
		documentsTable.setColumnHeaders("Nombre");

		for (ContractDocument cd : getEntity().getDocuments())
			documentsTable.getContainerDataSource().addItem(cd);
		documentsTable.addGeneratedColumn("delete", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button delete = new Button("Borrar");
				delete.setStyleName(ValoTheme.BUTTON_LINK);
				ContractDocument document = (ContractDocument) itemId;
				delete.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						ContractDocument document = (ContractDocument) itemId;
						getEntity().getDocuments().remove(document);
						ContractManager.getInstance().updateContract(getEntity());
						documentsTable.getContainerDataSource().removeItem(document);

					}
				});
				return delete;
			}
		});
		documentsTable.setColumnHeader("delete", "Borrar");
		documentsTable.addGeneratedColumn("download", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button button = new Button("Descargar");
				button.setStyleName(ValoTheme.BUTTON_LINK);
				final ContractDocument document = (ContractDocument) itemId;
				button.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						final StreamResource resource = new StreamResource(new StreamSource() {
							@Override
							public InputStream getStream() {
								return new ByteArrayInputStream(document.getContent());
							}
						}, document.getName());
						FileDownloader fileDownloader = new FileDownloader(resource);
						fileDownloader.extend(button);

					}
				});
				return button;
			}
		});
		documentsTable.setColumnHeader("download", "Descargar");
		return documentsTable;
	}

}

package uy.com.innobit.rem.presentation.view.properties;

import java.awt.image.IndexColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tepi.filtertable.FilterTable;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinSession;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
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
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.business.managers.ContractManager;
import uy.com.innobit.rem.business.managers.OwnerManager;
import uy.com.innobit.rem.business.managers.PropertyManager;
import uy.com.innobit.rem.business.quartz.mail.MailSender;
import uy.com.innobit.rem.business.quartz.whatsapp.WhatsappSender;
import uy.com.innobit.rem.persistence.datamodel.clients.Owner;
import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.persistence.datamodel.property.PropertyDocument;
import uy.com.innobit.rem.persistence.datamodel.property.PropertyNotification;
import uy.com.innobit.rem.persistence.datamodel.property.PropertyPicture;
import uy.com.innobit.rem.presentation.RemUI;
import uy.com.innobit.rem.presentation.view.owners.OwnerFilterGenerator;
import uy.com.innobit.rem.presentation.view.owners.OwnerForm;
import uy.com.innobit.rem.presentation.view.owners.OwnersFilterDecorator;

/**
 * A UI component built to modify Customer entities. The used superclass
 * provides binding to the entity object and e.g. Save/Cancel buttons by
 * default. In larger apps, you'll most likely have your own customized super
 * class for your forms.
 * <p>
 * Note, that the advanced bean binding technology in Vaadin is able to take
 * advantage also from Bean Validation annotations that are used also by e.g.
 * JPA implementation. Check out annotations in Customer objects email field and
 * how they automatically reflect to the configuration of related fields in UI.
 * </p>
 * 
 * private String name; private Owner ownerName; private String address;
 * 
 * private String admin;
 * 
 * private String obs;
 */
public class PropertyForm extends AbstractForm<Property> {
	private static final long serialVersionUID = 1L;
	TextField name = new MTextField("Nombre:").withFullWidth();
	MButton owner = new MButton("Propietario:");
	TextArea address = new MTextArea("Dirección:").withFullWidth();
	MTextField tel = new MTextField("Tel.:").withFullWidth();
	MTextField nro = new MTextField("Nro.:").withFullWidth();
	MTextField padron = new MTextField("Padrón:").withFullWidth();
	MTextField block = new MTextField("Manzana:").withFullWidth();
	MTextField refUte = new MTextField("Ref.Ute:").withFullWidth();
	MTextField refAgua = new MTextField("Ref.Ose:").withFullWidth();
	MTextField size = new MTextField("Tamaño:").withFullWidth();
	CheckBox payExpenses = new CheckBox("Paga G.C.");
	ComboBox expensesFreq = new ComboBox("Frecuencia G.C.:");
	MTextField expenses = new MTextField("Monto U$D G.C.:").withFullWidth();
	TextArea obs = new MTextArea("Observaciones:").withFullWidth();

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private Window ownerList;
	private FilterTable ownersTable;

	private Window contractsWindow;
	private Table contractsTable;

	private Window remindersWindow;
	private Window mailWindow;
	private Window whatsappWindow;

	private Window documentsWindow;
	private Table documentsTable;

	private Window picturesWindow;
	private Window picturesShow;
	private Window mapWindow;
	private Table picturesTable;

	private Button contracts = new Button("Contratos");
	private Button docs = new Button("Documentos");
	private Button pics = new Button("Fotos");
	private Button map = new Button("Mapa");
	private Button mail = new Button("Mail");
	private Button whatsapp = new Button("WhatsApp");
	private Button reminders = new Button("Recordatorios");

	PropertyListView view;

	public PropertyForm(PropertyListView listView) {
		this.view = listView;
		init();

	}

	public Property getEntity() {
		return super.getEntity();
	}

	@Override
	protected Component createContent() {

		setStyleName(ValoTheme.LAYOUT_CARD);
		owner.setStyleName(ValoTheme.BUTTON_LINK);
		owner.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				buildOwnerListWindow();
				RemUI.getCurrent().addWindow(ownerList);

			}
		});
		MHorizontalLayout buttons = new MHorizontalLayout();

		buttons.addComponent(contracts);
		buttons.addComponent(docs);
		buttons.addComponent(pics);
		buttons.addComponent(reminders);
		MHorizontalLayout buttons2 = new MHorizontalLayout();
		buttons2.addComponent(map);
		buttons2.addComponent(mail);
		buttons2.addComponent(whatsapp);

		contracts.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (getEntity().getId() == null) {
					PropertyManager.getInstance().saveProperty(getEntity());
					view.saveProperty(getEntity());
				}

				buildContractListWindow();
				RemUI.getCurrent().addWindow(contractsWindow);

			}
		});

		mail.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				buildMailWindow();
				RemUI.getCurrent().addWindow(mailWindow);

			}
		});

		whatsapp.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				buildWhatsappWindow();
				RemUI.getCurrent().addWindow(whatsappWindow);

			}
		});
		docs.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				buildDocumentsWindow();
				RemUI.getCurrent().addWindow(documentsWindow);

			}
		});

		pics.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				buildPicturesWindow();
				RemUI.getCurrent().addWindow(picturesWindow);
			}
		});

		map.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				buildmapWindow();
				RemUI.getCurrent().addWindow(mapWindow);

			}
		});
		reminders.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				buildRemindersWindow();
				RemUI.getCurrent().addWindow(remindersWindow);

			}
		});

		expensesFreq.addItem("Mensual");
		expensesFreq.addItem("Bimestral");
		expensesFreq.addItem("Trimestral");
		expensesFreq.addItem("Semestral");
		expensesFreq.addItem("Anual");

		MFormLayout form = new MFormLayout(name, owner, nro, padron, block, tel, payExpenses, expenses, expensesFreq,
				refUte, refAgua, size, obs, address).withFullWidth();

		form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		form.setComponentAlignment(owner, Alignment.MIDDLE_LEFT);
		Panel panel = new Panel(form);
		panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		MVerticalLayout layout = new MVerticalLayout(new Header("Editar Propiedad").setHeaderLevel(4), panel, buttons,
				buttons2, getToolbar()).withStyleName(ValoTheme.LAYOUT_CARD);
		layout.setHeight(view.getHeight(), view.getHeightUnits());
		setHeight(view.getHeight(), view.getHeightUnits());

		layout.expand(panel);
		return layout;
	}

	@Override
	public HorizontalLayout getToolbar() {
		return new MHorizontalLayout(getSaveButton(), getResetButton(), getDeleteButton());
	}

	void init() {
		setEagerValidation(true);
		setSavedHandler(new SavedHandler<Property>() {

			@Override
			public void onSave(Property entity) {
				try {
					if (entity.getId() == null || entity.getId() == 0)
						PropertyManager.getInstance().saveProperty(entity);
					else
						PropertyManager.getInstance().updateProperty(entity);
					view.saveProperty(entity);
				} catch (Exception e) {
					/*
					 * The Customer object uses optimitic locking with the
					 * version field. Notify user the editing didn't succeed.
					 */
					e.printStackTrace();
					Notification.show("No fue posible guardar los cambios", Notification.Type.ERROR_MESSAGE);
					// refrehsEvent.fire(entity);
				}
			}
		});
		setResetHandler(new ResetHandler<Property>() {

			@Override
			public void onReset(Property entity) {
				Property aux = PropertyManager.getInstance().getById(entity.getId());
				view.resetProperty(aux);
			}
		});
		setDeleteHandler(new DeleteHandler<Property>() {
			@Override
			public void onDelete(Property entity) {
				PropertyManager.getInstance().deleteProperty(entity);
				view.deleteProperty(entity);
			}
		});
		getSaveButton().setCaption("Guardar");
		getResetButton().setCaption("Cancelar");
		getDeleteButton().setCaption("Borrar");

	}

	private void buildOwnerListWindow() {
		ownerList = new Window("Propietarios");
		ownerList.setModal(true);
		ownerList.setClosable(true);
		ownerList.setResizable(false);
		ownerList.setDraggable(false);
		ownerList.setWidth("65%");
		ownerList.setContent(buildFilterTable());

	}

	private void buildContractListWindow() {
		contractsWindow = new Window("Contratos");
		contractsWindow.setModal(true);
		contractsWindow.setClosable(true);
		contractsWindow.setResizable(false);
		contractsWindow.setDraggable(false);
		contractsWindow.setWidth("65%");
		contractsWindow.setContent(buildContacts());
		Button add = new Button("Nuevo");
		add.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Contract contract = new Contract();
				contract.setProperty(getEntity());
				ContractManager.getInstance().saveContract(contract);
				VaadinSession.getCurrent().setAttribute(Contract.class, contract);
				VaadinSession.getCurrent().setAttribute(PropertyForm.class, PropertyForm.this);
				UI.getCurrent().getNavigator().navigateTo("/contractEdit");
			}
		});
		MVerticalLayout layout = new MVerticalLayout(add, buildContacts()).withFullWidth();
		contractsWindow.setContent(layout);

	}

	public void deleteContract(Contract contract) {
		if (contract.getId() != 0) {
			ContractManager.getInstance().deleteContract(contract);
			getEntity().getContracts().remove(contract);
		}
	}

	public void saveContract(Contract contract) {
		if (contract.getId() != 0) {
			ContractManager.getInstance().updateContract(contract);
		} else {
			ContractManager.getInstance().saveContract(contract);
			getEntity().getContracts().add(contract);
		}
	}

	public void resetContract(Contract contract) {
		if (contract.getId() != 0) {
			getEntity().getContracts().add(ContractManager.getInstance().getById(contract.getId()));// es
																									// un
																									// set...se
																									// sustituyen
		}
	}

	@SuppressWarnings("unchecked")
	private Container buildOwnersContainer() {
		return new BeanItemContainer<Owner>(Owner.class);
	}

	private FilterTable buildFilterTable() {
		ownersTable = new FilterTable();
		ownersTable.setWidth("100%");
		ownersTable.setFilterBarVisible(true);
		ownersTable.setFilterDecorator(new OwnersFilterDecorator());
		ownersTable.setFilterGenerator(new OwnerFilterGenerator());
		ownersTable.setPageLength(8);
		ownersTable.setImmediate(true);
		ownersTable.setRowHeaderMode(RowHeaderMode.HIDDEN);
		ownersTable.setContainerDataSource(buildOwnersContainer());
		ownersTable.setVisibleColumns("socialReason", "name", "surname", "doc", "cell", "tel", "mail", "rut");
		ownersTable.setColumnHeaders("Raz.Social", "Nombre", "Apellidio", "Documento", "Celular", "Teléfono", "Mail",
				"Rut");
		ownersTable.addItemClickListener(new ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				getEntity().setOwner((Owner) event.getItemId());
				owner.setCaption("Propietario: " + getEntity().getOwner().getName());
				ownerList.close();
				getFieldGroup().setBeanModified(true);
				adjustSaveButtonState();

			}
		});
		ownersTable.addGeneratedColumn("goto", new CustomTable.ColumnGenerator() {

			@Override
			public Object generateCell(CustomTable source, Object itemId, Object columnId) {
				final Owner owner = (Owner) itemId;
				Button button = new Button("Ver");
				button.setStyleName(ValoTheme.BUTTON_LINK);
				button.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						VaadinSession.getCurrent().setAttribute(Owner.class, owner);
						UI.getCurrent().getNavigator().navigateTo("/owners");

					}
				});
				return button;
			}

		});
		ownersTable.setColumnHeader("goto", "");
		for (Owner o : OwnerManager.getInstance().getAll()) {
			ownersTable.getContainerDataSource().addItem(o);
			// ownersTable.getContainerDataSource().getContainerProperty(o,
			// "name").setValue(o.getName());
			// ownersTable.getContainerDataSource().getContainerProperty(o,
			// "rut").setValue(o.getRut());
			// ownersTable.getContainerDataSource().getContainerProperty(o,
			// "mail").setValue(o.getMail());
			// ownersTable.getContainerDataSource().getContainerProperty(o,
			// "cell").setValue(o.getCell());
			// ownersTable.addItem(o);
		}

		return ownersTable;
	}

	private Table buildContacts() {
		contractsTable = new Table();
		contractsTable.setPageLength(8);
		contractsTable.setImmediate(true);
		contractsTable.setWidth("100%");
		IndexedContainer cont = new IndexedContainer();
		cont.addContainerProperty("id", Integer.class, null);
		cont.addContainerProperty("occupant", String.class, null);
		cont.addContainerProperty("init", String.class, null);
		cont.addContainerProperty("end", String.class, null);
		contractsTable.setContainerDataSource(cont);
		String[] visibleColumns = new String[] { "occupant", "init", "end" };
		contractsTable.setColumnHeaders("", "Inquilino", "Inicio", "Fin");
		contractsTable.setVisibleColumns(visibleColumns);
		contractsTable.addItemClickListener(new ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				Contract contract = (Contract) event.getItemId();
				VaadinSession.getCurrent().setAttribute(Contract.class, contract);
				VaadinSession.getCurrent().setAttribute(PropertyForm.class, PropertyForm.this);
				UI.getCurrent().getNavigator().navigateTo("/contractEdit");
			}
		});
		for (Contract o : getEntity().getContracts()) {
			contractsTable.getContainerDataSource().addItem(o);
			if (o.getOccupant() != null)
				contractsTable.getContainerDataSource().getContainerProperty(o, "occupant")
						.setValue(o.getOccupant().getName());
			if (o.getInit() != null)
				contractsTable.getContainerDataSource().getContainerProperty(o, "init")
						.setValue(sdf.format(o.getInit()));
			if (o.getEnd() != null)
				contractsTable.getContainerDataSource().getContainerProperty(o, "end").setValue(sdf.format(o.getEnd()));
			contractsTable.addItem(o);
		}
		contractsTable.addGeneratedColumn("delete", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button delete = new Button("Borrar");
				delete.setStyleName(ValoTheme.BUTTON_LINK);
				final Contract contract = (Contract) itemId;
				delete.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						getEntity().getContracts().remove(contract);
						contractsTable.getContainerDataSource().removeItem(contract);
						ContractManager.getInstance().deleteContract(contract);
					}
				});
				return delete;
			}
		});
		contractsTable.setColumnHeader("delete", " ");
		return contractsTable;
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
				PropertyNotification cn = new PropertyNotification();
				cn.setMessage(message.getValue());
				cn.setNotificationDate(date.getValue());
				cn.setResolved(false);
				cn.setSent(false);
				cn.getRecipients().add(RemUI.get().getLoggedUser().getEmail());

				PropertyManager.getInstance().saveReminder(cn);
				getEntity().getNotifications().add(cn);

				cn.setProperty(getEntity());
				PropertyManager.getInstance().updateProperty(getEntity());
				PropertyManager.getInstance().updateReminder(cn);
				reminders.getContainerDataSource().addItem(cn);

				date.setValue(new Date());
				resolved.setValue(false);
				message.setValue("");
			}

		});
		reminders.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				PropertyNotification reminder = (PropertyNotification) reminders.getValue();
				message.setValue(reminder.getMessage());
				date.setValue(reminder.getNotificationDate());
				resolved.setValue(reminder.isResolved());
			}

		});

		remindersWindow.setContent(layout);
	}

	private void buildMailWindow() {
		mailWindow = new Window("Mail");
		mailWindow.setModal(true);
		mailWindow.setClosable(true);
		mailWindow.setResizable(false);
		mailWindow.setDraggable(false);
		mailWindow.setWidth("60%");
		mailWindow.setStyleName("mipa");
		TextField mail = new TextField("Mail:");
		final MTextArea message = new MTextArea("Mensaje:");
		Map<String, String> properties = new HashMap<>();
		Table data = new Table();
		IndexedContainer ic = new IndexedContainer();
		ic.addContainerProperty("property", String.class, "");
		ic.addContainerProperty("propertyValue", String.class, "");
		ic.addContainerProperty("propertyKey", String.class, "");
		data.setContainerDataSource(ic);
		data.setVisibleColumns("property");
		data.setColumnHeaders("Propiedad");
		ic.addItem(1);
		ic.getContainerProperty(1, "property").setValue("Nombre");
		ic.getContainerProperty(1, "propertyValue")
				.setValue(getEntity().getName() != null ? getEntity().getName() : "");
		ic.getContainerProperty(1, "propertyKey").setValue("name");
		ic.addItem(2);
		ic.getContainerProperty(2, "property").setValue("Nro");
		ic.getContainerProperty(2, "propertyValue").setValue(getEntity().getNro() != null ? getEntity().getNro() : "");
		ic.getContainerProperty(2, "propertyKey").setValue("nro");
		ic.addItem(3);
		ic.getContainerProperty(3, "property").setValue("Padrón");
		ic.getContainerProperty(3, "propertyValue")
				.setValue(getEntity().getPadron() != null ? getEntity().getPadron() : "");
		ic.getContainerProperty(3, "propertyKey").setValue("padron");
		ic.addItem(4);
		ic.getContainerProperty(4, "property").setValue("Manzana");
		ic.getContainerProperty(4, "propertyValue")
				.setValue(getEntity().getBlock() != null ? getEntity().getBlock() : "");
		ic.getContainerProperty(4, "propertyKey").setValue("block");
		ic.addItem(5);
		ic.getContainerProperty(5, "property").setValue("Paga G.C.:");
		ic.getContainerProperty(5, "propertyValue")
				.setValue(getEntity().getPayExpenses() != null && getEntity().getPayExpenses() ? "Si" : "No");
		ic.getContainerProperty(5, "propertyKey").setValue("payExpenses");
		ic.addItem(6);
		ic.getContainerProperty(6, "property").setValue("Monto U$D G.C.");
		ic.getContainerProperty(6, "propertyValue")
				.setValue(getEntity().getExpenses() != null ? getEntity().getExpenses().toString() : "");
		ic.getContainerProperty(6, "propertyKey").setValue("expenses");
		ic.addItem(7);
		ic.getContainerProperty(7, "property").setValue("Frecuencia G.C.");
		ic.getContainerProperty(7, "propertyValue")
				.setValue(getEntity().getExpensesFreq() != null ? getEntity().getExpensesFreq() : "");
		ic.getContainerProperty(7, "propertyKey").setValue("expensesFreq");
		ic.addItem(8);
		ic.getContainerProperty(8, "property").setValue("Ref. Ute");
		ic.getContainerProperty(8, "propertyValue")
				.setValue(getEntity().getRefUte() != null ? getEntity().getRefUte() : "");
		ic.getContainerProperty(8, "propertyKey").setValue("refUte");
		ic.addItem(9);
		ic.getContainerProperty(9, "property").setValue("Ref. Ose");
		ic.getContainerProperty(9, "propertyValue")
				.setValue(getEntity().getRefAgua() != null ? getEntity().getRefAgua() : "");
		ic.getContainerProperty(9, "propertyKey").setValue("refAgua");

		ic.addItem(10);
		ic.getContainerProperty(10, "property").setValue("Tamaño");
		ic.getContainerProperty(10, "propertyValue")
				.setValue(getEntity().getSize() != null ? getEntity().getSize() : "");
		ic.getContainerProperty(10, "propertyKey").setValue("size");
		ic.addItem(11);
		ic.getContainerProperty(11, "property").setValue("Dirección");
		ic.getContainerProperty(11, "propertyValue")
				.setValue(getEntity().getAddress() != null ? getEntity().getAddress() : "");
		ic.getContainerProperty(11, "propertyKey").setValue("address");
		ic.addItem(12);
		ic.getContainerProperty(12, "property").setValue("Tel");
		ic.getContainerProperty(12, "propertyValue").setValue(getEntity().getTel() != null ? getEntity().getTel() : "");
		ic.getContainerProperty(12, "propertyKey").setValue("tel");
		ic.addItem(13);
		ic.getContainerProperty(13, "property").setValue("Observaciones");
		ic.getContainerProperty(13, "propertyValue").setValue(getEntity().getObs() != null ? getEntity().getObs() : "");
		ic.getContainerProperty(13, "propertyKey").setValue("obs");
		data.addGeneratedColumn("selected", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				final CheckBox cb = new CheckBox();
				cb.addValueChangeListener(new ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						if (cb.getValue()) {
							properties.put(ic.getItem(itemId).getItemProperty("propertyKey").getValue().toString(),
									ic.getItem(itemId).getItemProperty("propertyValue").getValue().toString());
						} else {
							properties.remove(ic.getItem(itemId).getItemProperty("propertyKey").getValue().toString());
						}

					}
				});
				return cb;
			}
		});
		data.setColumnHeader("selected", " ");
		data.setWidth("100%");
		data.setPageLength(5);
		Button save = new Button("Enviar");
		save.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				MailSender.getInstance().savePropertyMail("Información de propiedad", message.getValue(),
						mail.getValue(), properties);
				mailWindow.close();
				Notification.show("El mensaje será enviado.");

			}
		});
		Button cancel = new Button("Cancelar");
		cancel.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				mailWindow.close();

			}
		});
		MHorizontalLayout hl = new MHorizontalLayout(save, cancel).withAlign(save, Alignment.MIDDLE_RIGHT)
				.withAlign(cancel, Alignment.MIDDLE_RIGHT);
		MVerticalLayout layout = new MVerticalLayout(mail, message, data, hl);
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);

		mailWindow.setContent(layout);
	}

	private void buildWhatsappWindow() {
		whatsappWindow = new Window("Whatsapp");
		whatsappWindow.setModal(true);
		whatsappWindow.setClosable(true);
		whatsappWindow.setResizable(false);
		whatsappWindow.setDraggable(false);
		whatsappWindow.setWidth("60%");
		whatsappWindow.setStyleName("mipa");
		TextField mobile = new TextField("Celular:");
		final MTextArea message = new MTextArea("Mensaje:");
		Map<String, String> properties = new HashMap<>();
		Table data = new Table();
		IndexedContainer ic = new IndexedContainer();
		ic.addContainerProperty("property", String.class, "");
		ic.addContainerProperty("propertyValue", String.class, "");
		ic.addContainerProperty("propertyKey", String.class, "");
		data.setContainerDataSource(ic);
		data.setVisibleColumns("property");
		data.setColumnHeaders("Propiedad");
		ic.addItem(1);
		ic.getContainerProperty(1, "property").setValue("Nombre");
		ic.getContainerProperty(1, "propertyValue")
				.setValue(getEntity().getName() != null ? getEntity().getName() : "");
		ic.getContainerProperty(1, "propertyKey").setValue("name");
		ic.addItem(2);
		ic.getContainerProperty(2, "property").setValue("Nro");
		ic.getContainerProperty(2, "propertyValue").setValue(getEntity().getNro() != null ? getEntity().getNro() : "");
		ic.getContainerProperty(2, "propertyKey").setValue("nro");
		ic.addItem(3);
		ic.getContainerProperty(3, "property").setValue("Padrón");
		ic.getContainerProperty(3, "propertyValue")
				.setValue(getEntity().getPadron() != null ? getEntity().getPadron() : "");
		ic.getContainerProperty(3, "propertyKey").setValue("padron");
		ic.addItem(4);
		ic.getContainerProperty(4, "property").setValue("Manzana");
		ic.getContainerProperty(4, "propertyValue")
				.setValue(getEntity().getBlock() != null ? getEntity().getBlock() : "");
		ic.getContainerProperty(4, "propertyKey").setValue("block");
		ic.addItem(5);
		ic.getContainerProperty(5, "property").setValue("Paga G.C.:");
		ic.getContainerProperty(5, "propertyValue")
				.setValue(getEntity().getPayExpenses() != null && getEntity().getPayExpenses() ? "Si" : "No");
		ic.getContainerProperty(5, "propertyKey").setValue("payExpenses");
		ic.addItem(6);
		ic.getContainerProperty(6, "property").setValue("Monto U$D G.C.");
		ic.getContainerProperty(6, "propertyValue")
				.setValue(getEntity().getExpenses() != null ? getEntity().getExpenses().toString() : "");
		ic.getContainerProperty(6, "propertyKey").setValue("expenses");
		ic.addItem(7);
		ic.getContainerProperty(7, "property").setValue("Frecuencia G.C.");
		ic.getContainerProperty(7, "propertyValue")
				.setValue(getEntity().getExpensesFreq() != null ? getEntity().getExpensesFreq() : "");
		ic.getContainerProperty(7, "propertyKey").setValue("expensesFreq");
		ic.addItem(8);
		ic.getContainerProperty(8, "property").setValue("Ref. Ute");
		ic.getContainerProperty(8, "propertyValue")
				.setValue(getEntity().getRefUte() != null ? getEntity().getRefUte() : "");
		ic.getContainerProperty(8, "propertyKey").setValue("refUte");
		ic.addItem(9);
		ic.getContainerProperty(9, "property").setValue("Ref. Ose");
		ic.getContainerProperty(9, "propertyValue")
				.setValue(getEntity().getRefAgua() != null ? getEntity().getRefAgua() : "");
		ic.getContainerProperty(9, "propertyKey").setValue("refAgua");

		ic.addItem(10);
		ic.getContainerProperty(10, "property").setValue("Tamaño");
		ic.getContainerProperty(10, "propertyValue")
				.setValue(getEntity().getSize() != null ? getEntity().getSize() : "");
		ic.getContainerProperty(10, "propertyKey").setValue("size");
		ic.addItem(11);
		ic.getContainerProperty(11, "property").setValue("Dirección");
		ic.getContainerProperty(11, "propertyValue")
				.setValue(getEntity().getAddress() != null ? getEntity().getAddress() : "");
		ic.getContainerProperty(11, "propertyKey").setValue("address");
		ic.addItem(12);
		ic.getContainerProperty(12, "property").setValue("Tel");
		ic.getContainerProperty(12, "propertyValue").setValue(getEntity().getTel() != null ? getEntity().getTel() : "");
		ic.getContainerProperty(12, "propertyKey").setValue("tel");
		ic.addItem(13);
		ic.getContainerProperty(13, "property").setValue("Observaciones");
		ic.getContainerProperty(13, "propertyValue").setValue(getEntity().getObs() != null ? getEntity().getObs() : "");
		ic.getContainerProperty(13, "propertyKey").setValue("obs");
		data.addGeneratedColumn("selected", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				final CheckBox cb = new CheckBox();
				cb.addValueChangeListener(new ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						if (cb.getValue()) {
							properties.put(ic.getItem(itemId).getItemProperty("propertyKey").getValue().toString(),
									ic.getItem(itemId).getItemProperty("propertyValue").getValue().toString());
						} else {
							properties.remove(ic.getItem(itemId).getItemProperty("propertyKey").getValue().toString());
						}

					}
				});
				return cb;
			}
		});
		data.setColumnHeader("selected", " ");
		data.setWidth("100%");
		data.setPageLength(5);
		Button save = new Button("Enviar");
		save.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WhatsappSender.getInstance().savePropertyMail(message.getValue(), mobile.getValue(), properties);
				whatsappWindow.close();
				Notification.show("El mensaje será enviado.");

			}
		});
		Button cancel = new Button("Cancelar");
		cancel.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				whatsappWindow.close();

			}
		});
		MHorizontalLayout hl = new MHorizontalLayout(save, cancel).withAlign(save, Alignment.MIDDLE_RIGHT)
				.withAlign(cancel, Alignment.MIDDLE_RIGHT);
		MVerticalLayout layout = new MVerticalLayout(mobile, message, data, hl);
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);

		whatsappWindow.setContent(layout);
	}

	public Table buildRemindersTable() {
		BeanItemContainer<PropertyNotification> container = new BeanItemContainer<PropertyNotification>(
				PropertyNotification.class);
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
				final PropertyNotification c = (PropertyNotification) itemId;
				Button button = new Button("Borrar");
				button.setStyleName(ValoTheme.BUTTON_LINK);
				button.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						getEntity().getNotifications().remove(c);
						PropertyManager.getInstance().deleteReminder(c);
						PropertyManager.getInstance().updateProperty(getEntity());
						source.getContainerDataSource().removeItem(itemId);
					}
				});

				return button;
			}
		});
		table.setColumnHeader("delete", " ");
		for (PropertyNotification c : getEntity().getNotifications())
			table.getContainerDataSource().addItem(c);
		return table;
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
						PropertyDocument cd = new PropertyDocument();
						cd.setName(filename);
						cd.setContent(toByteArray());
						PropertyManager.getInstance().saveDocument(cd);
						getEntity().getDocuments().add(cd);
						PropertyManager.getInstance().updateProperty(getEntity());
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
		BeanItemContainer<PropertyDocument> cont = new BeanItemContainer<PropertyDocument>(PropertyDocument.class);
		documentsTable.setContainerDataSource(cont);
		String[] visibleColumns = new String[] { "name" };
		documentsTable.setVisibleColumns(visibleColumns);
		documentsTable.setColumnHeaders("Nombre");

		for (PropertyDocument cd : getEntity().getDocuments())
			documentsTable.getContainerDataSource().addItem(cd);
		documentsTable.addGeneratedColumn("delete", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button delete = new Button("Borrar");
				delete.setStyleName(ValoTheme.BUTTON_LINK);
				PropertyDocument document = (PropertyDocument) itemId;
				delete.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						PropertyDocument document = (PropertyDocument) itemId;
						getEntity().getDocuments().remove(document);
						PropertyManager.getInstance().updateProperty(getEntity());
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
				final PropertyDocument document = (PropertyDocument) itemId;
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

	public void buildPicturesWindow() {
		picturesWindow = new Window("Fotos");
		picturesWindow.setModal(true);
		picturesWindow.setClosable(true);
		picturesWindow.setResizable(false);
		picturesWindow.setDraggable(false);
		picturesWindow.setWidth("65%");

		final Upload upload = new Upload("", new Receiver() {
			@Override
			public OutputStream receiveUpload(final String filename, String mimeType) {
				return new ByteArrayOutputStream() {
					@Override
					public void close() throws IOException {
						PropertyPicture cd = new PropertyPicture();
						cd.setName(filename);
						cd.setContent(toByteArray());
						PropertyManager.getInstance().save(cd);
						getEntity().getPictures().add(cd);
						PropertyManager.getInstance().updateProperty(getEntity());
						picturesTable.getContainerDataSource().addItem(cd);
					}
				};
			}
		});
		upload.setButtonCaption("Agregar");
		upload.setImmediate(true);

		MVerticalLayout layout = new MVerticalLayout(upload, buildPicturesTable()).withFullWidth();

		picturesWindow.setContent(layout);

	}

	public void buildShowWindow(final byte[] pic) {
		picturesShow = new Window("Fotos");
		picturesShow.setModal(true);
		picturesShow.setClosable(true);
		picturesShow.setResizable(false);
		picturesShow.setDraggable(false);
		picturesShow.setWidth("100%");
		picturesShow.setHeight("100%");
		final StreamResource resource = new StreamResource(new StreamSource() {
			@Override
			public InputStream getStream() {
				return new ByteArrayInputStream(pic);
			}
		}, "");

		Image sample = new Image("", resource);

		picturesShow.setContent(sample);

	}

	public void buildmapWindow() {
		mapWindow = new Window("Mapa");
		mapWindow.setModal(true);
		mapWindow.setClosable(true);
		mapWindow.setResizable(false);
		mapWindow.setDraggable(false);
		mapWindow.setWidth("70%");
		mapWindow.setHeight("70%");
		GoogleMap googleMap = new GoogleMap("AIzaSyDGRIg7CtaNCjrCzohdm6J_xo7gKSlHV30", null, "spanish");
		googleMap.setSizeFull();
		if (getEntity().getLat() != 0 && getEntity().getLng() != 0) {
			googleMap.addMarker("", new LatLon(getEntity().getLat(), getEntity().getLng()), false, null);
			googleMap.setCenter(new LatLon(getEntity().getLat(), getEntity().getLng()));
		} else {
			googleMap.setCenter(new LatLon(-34.9424383, -54.9468935));

		}
		googleMap.addMapClickListener(new MapClickListener() {

			@Override
			public void mapClicked(LatLon position) {
				getEntity().setLat(position.getLat());
				getEntity().setLng(position.getLon());
				googleMap.getMarkers().clear();
				googleMap.addMarker("", position, false, null);
				PropertyManager.getInstance().updateProperty(getEntity());
			}
		});
		googleMap.setMinZoom(4);
		googleMap.setMaxZoom(16);
		googleMap.setZoom(13);
		mapWindow.setContent(googleMap);

	}

	private Table buildPicturesTable() {
		picturesTable = new Table();
		picturesTable.setPageLength(8);
		picturesTable.setImmediate(true);
		picturesTable.setWidth("100%");
		BeanItemContainer<PropertyPicture> cont = new BeanItemContainer<PropertyPicture>(PropertyPicture.class);
		picturesTable.setContainerDataSource(cont);
		String[] visibleColumns = new String[] { "name" };
		picturesTable.setVisibleColumns(visibleColumns);
		picturesTable.setColumnHeaders("Nombre");
		for (PropertyPicture cd : getEntity().getPictures())
			picturesTable.getContainerDataSource().addItem(cd);
		picturesTable.addGeneratedColumn("delete", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button delete = new Button("Borrar");
				delete.setStyleName(ValoTheme.BUTTON_LINK);
				PropertyPicture document = (PropertyPicture) itemId;
				delete.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						PropertyPicture document = (PropertyPicture) itemId;
						getEntity().getPictures().remove(document);
						PropertyManager.getInstance().updateProperty(getEntity());
						picturesTable.getContainerDataSource().removeItem(document);

					}
				});
				return delete;
			}
		});
		picturesTable.setColumnHeader("delete", "Borrar");
		picturesTable.addGeneratedColumn("download", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button button = new Button("Descargar");
				button.setStyleName(ValoTheme.BUTTON_LINK);
				final PropertyPicture document = (PropertyPicture) itemId;
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

		picturesTable.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				buildShowWindow(((PropertyPicture) picturesTable.getValue()).getContent());
				RemUI.getCurrent().addWindow(picturesShow);
			}
		});
		picturesTable.setColumnHeader("download", "Descargar");
		return picturesTable;
	}

	@Override
	protected void adjustResetButtonState() {
		// always enabled in this form
		getResetButton().setEnabled(true);
		getDeleteButton().setEnabled(getEntity() != null && getEntity().getId() != null && getEntity().getId() != 0);
	}

	public void refreshOwner() {
		if (getEntity().getOwner() != null)
			owner.setCaption("Propietario: " + getEntity().getOwner().getName());

	}

}

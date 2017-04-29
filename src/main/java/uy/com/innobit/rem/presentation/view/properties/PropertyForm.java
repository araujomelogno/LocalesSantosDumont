package uy.com.innobit.rem.presentation.view.properties;

import java.text.SimpleDateFormat;

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
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomTable.RowHeaderMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.business.managers.ContractManager;
import uy.com.innobit.rem.business.managers.OwnerManager;
import uy.com.innobit.rem.business.managers.PropertyManager;
import uy.com.innobit.rem.persistence.datamodel.clients.Owner;
import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.presentation.RemUI;
import uy.com.innobit.rem.presentation.component.PropertyDocumentsWindow;
import uy.com.innobit.rem.presentation.view.owners.OwnerFilterGenerator;
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

	private Button contracts = new Button("Contratos");
	private Button docs = new Button("Docs");
	private Button pics = new Button("Fotos");
	private Button reminders = new Button("Notificaciones");

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

		contracts.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				buildContractListWindow();
				RemUI.getCurrent().addWindow(contractsWindow);

			}
		});
		docs.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				PropertyDocumentsWindow.open(PropertyForm.this);
			}
		});

		pics.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show("En construcción");
			}
		});

		reminders.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show("En construcción");
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
				getToolbar()).withStyleName(ValoTheme.LAYOUT_CARD);
		layout.setHeight(view.getHeight(), view.getHeightUnits());
		setHeight(view.getHeight(), view.getHeightUnits());

		layout.expand(panel);
		return layout;
	}

	void init() {
		setEagerValidation(true);
		setSavedHandler(new SavedHandler<Property>() {

			@Override
			public void onSave(Property entity) {
				try {
					if (entity.getId() == null || entity.getId() == 0)
						PropertyManager.getInstance().saveUser(entity);
					else
						PropertyManager.getInstance().updateUser(entity);
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
				PropertyManager.getInstance().deleteUser(entity);
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
						ContractManager.getInstance().deleteContract(contract);
					}
				});
				return delete;
			}
		});
		contractsTable.setColumnHeader("delete", " ");
		return contractsTable;
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

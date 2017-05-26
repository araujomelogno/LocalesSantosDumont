package uy.com.innobit.rem.presentation.view.owners;

import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.business.managers.OwnerManager;
import uy.com.innobit.rem.business.managers.PropertyManager;
import uy.com.innobit.rem.persistence.datamodel.clients.Owner;
import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.presentation.RemUI;

/**
 * A UI component built to modify Customer entities. The used superclass
 * provides binding to the entity object and e.g. Save/Cancel buttons by
 * default. In larger apps, you'll most likely have your own customized super
 * class for your forms.
 * <p>
 * 
 * 
 * Note, that the advanced bean binding technology in Vaadin is able to take
 * advantage also from Bean Validation annotations that are used also by e.g.
 * JPA implementation. Check out annotations in Customer objects email field and
 * how they automatically reflect to the configuration of related fields in UI.
 * </p>
 */
public class OwnerForm extends AbstractForm<Owner> {
	TextField name = new MTextField("Nombre:").withFullWidth();
	TextField surname = new MTextField("Apellido:").withFullWidth();
	TextField doc = new MTextField("Documento:").withFullWidth();
	TextField rut = new MTextField("Rut:").withFullWidth();
	TextField bankAccount = new MTextField("Cuenta Bancaria:").withFullWidth();
	TextField socialReason = new MTextField("Razón Social:").withFullWidth();
	TextArea address = new MTextArea("Dirección:").withFullWidth();
	TextField cell = new MTextField("Celular:").withFullWidth();
	TextField tel = new MTextField("Teléfono:").withFullWidth();
	TextField mail = new MTextField("Email:").withFullWidth();
	TextArea obs = new MTextArea("Observaciones:").withFullWidth();
	Button properties = new Button("Propiedades");
	private Window propertiesWindow;
	private Table propertiesTable;
	OwnerListView view;

	public OwnerForm(OwnerListView listView) {
		this.view = listView;
		init();

	}

	@Override
	protected Component createContent() {
		setStyleName(ValoTheme.LAYOUT_CARD);
		MFormLayout form = new MFormLayout(name, surname, doc, rut,bankAccount, socialReason, mail, tel, cell, address, obs)
				.withFullWidth();
		form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		Panel panel = new Panel(form);
		panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		HorizontalLayout toolbar = getToolbar();
		properties.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				buildPropertiesWindow();
				RemUI.getCurrent().addWindow(propertiesWindow);

			}
		});
		toolbar.addComponent(properties);
		MVerticalLayout layout = new MVerticalLayout(new Header("Editar Propietario").setHeaderLevel(4), panel, toolbar)
				.withStyleName(ValoTheme.LAYOUT_CARD);
		layout.setHeight(view.getHeight(), view.getHeightUnits());
		setHeight(view.getHeight(), view.getHeightUnits());
		layout.expand(panel);
		return layout;
	}

	public void buildPropertiesWindow() {
		propertiesWindow = new Window("Propiedades");
		propertiesWindow.setModal(true);
		propertiesWindow.setClosable(true);
		propertiesWindow.setResizable(false);
		propertiesWindow.setDraggable(false);
		propertiesWindow.setWidth("65%");
		MVerticalLayout layout = new MVerticalLayout(buildPropertiesTable()).withFullWidth();
		propertiesWindow.setContent(layout);

	}

	private Table buildPropertiesTable() {
		propertiesTable = new Table();
		propertiesTable.setPageLength(8);
		propertiesTable.setImmediate(true);
		propertiesTable.setWidth("100%");
		BeanItemContainer<Property> cont = new BeanItemContainer<Property>(Property.class);
		propertiesTable.setContainerDataSource(cont);
		propertiesTable.setVisibleColumns("name", "padron", "nro", "address");
		propertiesTable.setColumnHeaders("Nombre", "Padrón", "Número", "Dirección" );
		propertiesTable.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Property p = (Property) propertiesTable.getValue();
				if (p != null) {
					VaadinSession.getCurrent().setAttribute(Property.class, p);
					UI.getCurrent().getNavigator().navigateTo("/properties");
				}

			}
		});
		for (Property cd : PropertyManager.getInstance().getByOwner(getEntity()))
			propertiesTable.getContainerDataSource().addItem(cd);

		return propertiesTable;
	}

	void init() {
		setEagerValidation(true);
		setSavedHandler(new SavedHandler<Owner>() {

			@Override
			public void onSave(Owner entity) {
				try {
					if (entity.getId() == null || entity.getId() == 0)
						OwnerManager.getInstance().saveUser(entity);
					else
						OwnerManager.getInstance().updateUser(entity);
					view.saveOwner(entity);
				} catch (Exception e) {
					/*
					 * The Customer object uses optimitic locking with the
					 * version field. Notify user the editing didn't succeed.
					 */
					Notification.show("No fue posible guardar los cambios", Notification.Type.ERROR_MESSAGE);
					// refrehsEvent.fire(entity);
				}
			}
		});
		setResetHandler(new ResetHandler<Owner>() {

			@Override
			public void onReset(Owner entity) {
				Owner aux = OwnerManager.getInstance().getById(entity.getId());
				view.resetOwner(aux);
			}
		});
		setDeleteHandler(new DeleteHandler<Owner>() {
			@Override
			public void onDelete(Owner entity) {
				OwnerManager.getInstance().deleteUser(entity);
				view.deleteOwner(entity);
			}
		});
	}

	@Override
	protected void adjustResetButtonState() {
		// always enabled in this form
		getResetButton().setEnabled(true);
		getDeleteButton().setEnabled(getEntity() != null && getEntity().getId() != null && getEntity().getId() != 0);
	}

}

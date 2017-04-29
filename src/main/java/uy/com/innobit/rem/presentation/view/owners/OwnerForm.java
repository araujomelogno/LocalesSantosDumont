package uy.com.innobit.rem.presentation.view.owners;

import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.business.managers.OwnerManager;
import uy.com.innobit.rem.persistence.datamodel.clients.Owner;

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
	TextField socialReason = new MTextField("Razón Social:").withFullWidth();
	TextArea address = new MTextArea("Dirección:").withFullWidth();
	TextField cell = new MTextField("Celular:").withFullWidth();
	TextField tel = new MTextField("Teléfono:").withFullWidth();
	TextField mail = new MTextField("Email:").withFullWidth();
	TextArea obs = new MTextArea("Observaciones:").withFullWidth();

	OwnerListView view;

	public OwnerForm(OwnerListView listView) {
		this.view = listView;
		init();

	}

	@Override
	protected Component createContent() {
		setStyleName(ValoTheme.LAYOUT_CARD);
		MFormLayout form = new MFormLayout(name, surname, doc, rut, socialReason, mail, tel, cell, address, obs)
				.withFullWidth();
		form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		Panel panel = new Panel(form);
		panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		MVerticalLayout layout = new MVerticalLayout(new Header("Editar Propietario").setHeaderLevel(4), panel,
				getToolbar()).withStyleName(ValoTheme.LAYOUT_CARD);
		layout.setHeight(view.getHeight(), view.getHeightUnits());
		setHeight(view.getHeight(), view.getHeightUnits());
		layout.expand(panel);
		return layout;
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

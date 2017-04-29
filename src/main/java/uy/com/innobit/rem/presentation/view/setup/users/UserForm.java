package uy.com.innobit.rem.presentation.view.setup.users;

import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.business.managers.OccupantManager;
import uy.com.innobit.rem.business.managers.UserManager;
import uy.com.innobit.rem.persistence.datamodel.clients.Occupant;
import uy.com.innobit.rem.persistence.datamodel.user.User;

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
 */
public class UserForm extends AbstractForm<User> {
	TextField name = new MTextField("Nombre:").withFullWidth();
	TextField login = new MTextField("Login:").withFullWidth();
	MPasswordField password = new MPasswordField("Password:").withFullWidth();
	TextField email = new MTextField("Email:").withFullWidth();

	UserListView view;

	public UserForm(UserListView listView) {
		this.view = listView;
		init();

	}

	@Override
	protected Component createContent() {
		setStyleName(ValoTheme.LAYOUT_CARD);
		MFormLayout form = new MFormLayout(name, login, password, email).withFullWidth();
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
		setSavedHandler(new SavedHandler<User>() {

			@Override
			public void onSave(User entity) {
				try {
					if (entity.getId() == null || entity.getId() == 0)
						UserManager.getInstance().saveUser(entity);
					else
						UserManager.getInstance().updateUser(entity);
					view.saveOccupant(entity);
				} catch (Exception e) {
					Notification.show("No fue posible guardar los cambios", Notification.Type.ERROR_MESSAGE);

				}
			}
		});
		setResetHandler(new ResetHandler<User>() {

			@Override
			public void onReset(User entity) {
				User aux = UserManager.getInstance().getById(entity.getId());
				view.resetOccupant(aux);
			}
		});
		setDeleteHandler(new DeleteHandler<User>() {
			@Override
			public void onDelete(User entity) {
				UserManager.getInstance().deleteUser(entity);
				view.deleteOccupant(entity);
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

package uy.com.innobit.rem.presentation.view.setup.users;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

import uy.com.innobit.rem.business.managers.UserManager;
import uy.com.innobit.rem.persistence.datamodel.user.User;
import uy.com.innobit.rem.presentation.ScreenSize;

@SuppressWarnings("serial")
public class UserListView extends MVerticalLayout implements View {

	UserForm userEditor = new UserForm(this);

	// Introduce and configure some UI components used on this view
	MTable<User> usersTable = new MTable(User.class).withFullWidth().withFullHeight();

	List<User> users = new ArrayList<User>();

	MHorizontalLayout mainContent = new MHorizontalLayout(usersTable).withFullWidth().withMargin(false);

	TextField filter = new TextField();

	Header header = new Header("Usuarios").setHeaderLevel(2);

	Button addButton = new MButton(FontAwesome.EDIT, new Button.ClickListener() {
		@Override
		public void buttonClick(Button.ClickEvent event) {
			addUser();
		}
	});

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
		if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
			addComponents(
					new MHorizontalLayout(header, filter, addButton).expand(header).alignAll(Alignment.MIDDLE_LEFT),
					mainContent);

			filter.setSizeUndefined();
		} else {
			addComponents(header,
					new MHorizontalLayout(filter, addButton).expand(filter).alignAll(Alignment.MIDDLE_LEFT),
					mainContent);
		}
		setMargin(new MarginInfo(false, true, true, true));
		expand(mainContent);
	}

	/**
	 * Similarly to layouts, we can adapt component configurations based on the
	 * client details. Here we configure visible columns so that a sane amount
	 * of data is displayed for various devices.
	 */
	private void adjustTableColumns() {
		usersTable.setVisibleColumns("name", "login", "email");
		usersTable.setColumnHeaders("Nombre", "Login", "Email");

	}

	private synchronized void listCustomers(String filterString) {
		if (filterString != null && !filterString.isEmpty()) {
			List<User> aux = new ArrayList<User>();
			for (User user : users)
				if ((user.getName() != null && user.getName().contains(filterString))
						|| (user.getLogin() != null && user.getLogin().contains(filterString))
						|| (user.getEmail() != null && user.getEmail().contains(filterString)))
					aux.add(user);
			usersTable.clear();
			usersTable.setBeans(aux);
		} else {
			usersTable.clear();
			usersTable.setBeans(users);
		}

	}

	void editEntity(User user) {
		if (user != null) {
			openEditor(user);
		} else {
			closeEditor();
		}
	}

	void addUser() {
		openEditor(new User());
	}

	private void openEditor(User user) {
		userEditor.setEntity(user);
		// display next to table on desktop class screens
		if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
			mainContent.addComponent(userEditor);
			userEditor.focusFirst();
		} else {
			removeAllComponents();
			addComponent(userEditor);
			expand(userEditor);
			markAsDirty();
		}
	}

	private void closeEditor() {
		// As we display the editor differently in different devices,
		// close properly in each modes
		if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
			mainContent.removeComponent(userEditor);
		} else {
			removeAllComponents();
			addComponents(header,
					new MHorizontalLayout(filter, addButton).expand(filter).alignAll(Alignment.MIDDLE_LEFT),
					mainContent);
			setMargin(new MarginInfo(false, true, true, true));
			expand(mainContent);
		}
	}

	void saveOccupant(User user) {
		if (!users.contains(user))
			users.add(user);
		usersTable.refreshRowCache();
		closeEditor();
	}

	void resetOccupant(User occupant) {
		if (occupant != null) {
			int index = users.indexOf(occupant);
			users.remove(occupant);
			users.add(index, occupant);
		}
		usersTable.clear();
		usersTable.setBeans(users);
		closeEditor();
	}

	void deleteOccupant(User user) {
		users.remove(user);
		usersTable.refreshRowCache();
		closeEditor();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		usersTable.addMValueChangeListener(new MValueChangeListener<User>() {

			@Override
			public void valueChange(MValueChangeEvent<User> event) {
				editEntity(event.getValue());
			}
		});

		/*
		 * Configure the filter input and hook to text change events to
		 * repopulate the table based on given filter. Text change events are
		 * sent to the server when e.g. user holds a tiny pause while typing or
		 * hits enter.
		 */
		filter.setInputPrompt("Filtrar Usuarios");
		filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
			@Override
			public void textChange(FieldEvents.TextChangeEvent textChangeEvent) {
				listCustomers(textChangeEvent.getText());
			}
		});

		/*
		 * "Responsive Web Design" can be done with plain Java as well. Here we
		 * e.g. do selective layouting and configure visible columns in table
		 * based on available width
		 */
		layout();
		adjustTableColumns();
		/*
		 * If you wish the UI to adapt on window resize/page orientation change,
		 * hook to BrowserWindowResizeEvent
		 */
		UI.getCurrent().setResizeLazy(true);
		Page.getCurrent().addBrowserWindowResizeListener(new Page.BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(Page.BrowserWindowResizeEvent browserWindowResizeEvent) {
				adjustTableColumns();
				layout();
			}
		});
		users = UserManager.getInstance().getAll();
		usersTable.setBeans(users);
	}

}

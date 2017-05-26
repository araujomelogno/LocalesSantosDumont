package uy.com.innobit.rem.presentation.view.owners;

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
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

import uy.com.innobit.rem.business.managers.OwnerManager;
import uy.com.innobit.rem.persistence.datamodel.clients.Owner;
import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.presentation.ScreenSize;

@SuppressWarnings("serial")
public class OwnerListView extends MVerticalLayout implements View {

	OwnerForm customerEditor = new OwnerForm(this);

	// Introduce and configure some UI components used on this view
	MTable<Owner> customerTable = new MTable(Owner.class).withFullWidth().withFullHeight();

	List<Owner> owners = new ArrayList<Owner>();

	MHorizontalLayout mainContent = new MHorizontalLayout(customerTable).withFullWidth().withMargin(false);

	TextField filter = new TextField();

	Header header = new Header("Propietarios").setHeaderLevel(2);

	Button addButton = new MButton(FontAwesome.EDIT, new Button.ClickListener() {
		@Override
		public void buttonClick(Button.ClickEvent event) {
			addOwner();
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
		if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
			customerTable.setVisibleColumns("socialReason", "name", "surname", "doc", "cell", "tel", "mail", "rut");
			customerTable.setColumnHeaders("Raz.Social", "Nombre", "Apellidio", "Documento", "Celular", "Teléfono",
					"Mail", "Rut");

		} else {
			// Only show one (generated) column with combined first + last name
			if (customerTable.getColumnGenerator("name") == null) {
				customerTable.addGeneratedColumn("name", new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table table, Object o, Object o2) {
						Owner c = (Owner) o;
						return c.getName();
					}
				});
			}
			if (ScreenSize.getScreenSize() == ScreenSize.MEDIUM) {
				customerTable.setVisibleColumns("name", "mail");
				customerTable.setColumnHeaders("Nombre", "Email");
			} else {
				customerTable.setVisibleColumns("name");
				customerTable.setColumnHeaders("Nombre");
			}
		}
	}

	private synchronized void listCustomers(String filterString) {
		if (filterString != null && !filterString.isEmpty()) {
			List<Owner> aux = new ArrayList<Owner>();
			for (Owner owner : owners)
				if ((owner.getName() != null && owner.getName().contains(filterString))
						|| (owner.getSurname() != null && owner.getSurname().contains(filterString))
						|| (owner.getDoc() != null && owner.getDoc().contains(filterString))
						|| (owner.getSocialReason() != null && owner.getSocialReason().contains(filterString))
						|| (owner.getTel() != null && owner.getTel().contains(filterString))
						|| (owner.getRut() != null && owner.getRut().contains(filterString))
						|| (owner.getMail() != null && owner.getMail().contains(filterString))
						|| (owner.getCell() != null && owner.getCell().contains(filterString)))
					aux.add(owner);
			customerTable.clear();
			customerTable.setBeans(aux);
		} else {
			customerTable.clear();
			customerTable.setBeans(owners);
		}

	}

	void editEntity(Owner owner) {
		if (owner != null) {
			openEditor(owner);
		} else {
			closeEditor();
		}
	}

	void addOwner() {
		openEditor(new Owner());
	}

	private void openEditor(Owner owner) {
		customerEditor.setEntity(owner);
		// display next to table on desktop class screens
		if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
			mainContent.addComponent(customerEditor);
			customerEditor.focusFirst();
		} else {
			removeAllComponents();
			addComponent(customerEditor);
			expand(customerEditor);
			markAsDirty();
		}
	}

	private void closeEditor() {
		// As we display the editor differently in different devices,
		// close properly in each modes
		if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
			mainContent.removeComponent(customerEditor);
		} else {
			removeAllComponents();
			addComponents(header,
					new MHorizontalLayout(filter, addButton).expand(filter).alignAll(Alignment.MIDDLE_LEFT),
					mainContent);
			setMargin(new MarginInfo(false, true, true, true));
			expand(mainContent);
		}
	}

	void saveOwner(Owner owner) {
		if (!owners.contains(owner))
			owners.add(owner);
		customerTable.refreshRowCache();
		closeEditor();
	}

	void resetOwner(Owner owner) {
		if (owner != null) {
			int index = owners.indexOf(owner);
			owners.remove(owner);
			owners.add(index, owner);
		}
		customerTable.clear();
		customerTable.setBeans(owners);
		closeEditor();
	}

	void deleteOwner(Owner owner) {
		owners.remove(owner);
		customerTable.refreshRowCache();
		closeEditor();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		customerTable.addMValueChangeListener(new MValueChangeListener<Owner>() {

			@Override
			public void valueChange(MValueChangeEvent<Owner> event) {
				editEntity(event.getValue());
			}
		});

		/*
		 * Configure the filter input and hook to text change events to
		 * repopulate the table based on given filter. Text change events are
		 * sent to the server when e.g. user holds a tiny pause while typing or
		 * hits enter.
		 */
		filter.setInputPrompt("Filtrar Propitarios");
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
		owners = OwnerManager.getInstance().getAll();
		customerTable.setBeans(owners);
		if (VaadinSession.getCurrent().getAttribute(Owner.class) != null) {
			Owner aux = VaadinSession.getCurrent().getAttribute(Owner.class);
			VaadinSession.getCurrent().setAttribute(Owner.class, null);
			editEntity(aux);
		}
	}

}

package uy.com.innobit.rem.presentation.view.occupants;

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

import uy.com.innobit.rem.business.managers.OccupantManager;
import uy.com.innobit.rem.persistence.datamodel.clients.Occupant;
import uy.com.innobit.rem.persistence.datamodel.clients.Owner;
import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.presentation.ScreenSize;

@SuppressWarnings("serial")
public class OccupantListView extends MVerticalLayout implements View {

	OccupantForm occupantEditor = new OccupantForm(this);

	// Introduce and configure some UI components used on this view
	MTable<Occupant> occupantsTable = new MTable(Occupant.class).withFullWidth().withFullHeight();

	List<Occupant> occupants = new ArrayList<Occupant>();

	MHorizontalLayout mainContent = new MHorizontalLayout(occupantsTable).withFullWidth().withMargin(false);

	TextField filter = new TextField();

	Header header = new Header("Inquilinos").setHeaderLevel(2);

	Button addButton = new MButton(FontAwesome.EDIT, new Button.ClickListener() {
		@Override
		public void buttonClick(Button.ClickEvent event) {
			addOccupant();
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
			occupantsTable.setVisibleColumns("socialReason", "name", "surname", "doc", "cell", "tel", "mail", "rut");
			occupantsTable.setColumnHeaders("Raz.Social", "Nombre", "Apellidio", "Documento", "Celular", "Teléfono",
					"Mail", "Rut");

		} else {
			// Only show one (generated) column with combined first + last name
			if (occupantsTable.getColumnGenerator("name") == null) {
				occupantsTable.addGeneratedColumn("name", new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table table, Object o, Object o2) {
						Occupant c = (Occupant) o;
						return c.getName();
					}
				});
			}
			if (ScreenSize.getScreenSize() == ScreenSize.MEDIUM) {
				occupantsTable.setVisibleColumns("name", "mail");
				occupantsTable.setColumnHeaders("Nombre", "Email");
			} else {
				occupantsTable.setVisibleColumns("name");
				occupantsTable.setColumnHeaders("Nombre");
			}
		}
	}

	private synchronized void listCustomers(String filterString) {

		if (filterString != null && !filterString.isEmpty()) {
			filterString = filterString.toLowerCase();
			List<Occupant> aux = new ArrayList<Occupant>();
			for (Occupant occupant : occupants)
				if ((occupant.getName() != null && occupant.getName().toLowerCase().contains(filterString))
						|| (occupant.getSurname() != null && occupant.getSurname().toLowerCase().contains(filterString))
						|| (occupant.getDoc() != null && occupant.getDoc().toLowerCase().contains(filterString))
						|| (occupant.getSocialReason() != null
								&& occupant.getSocialReason().toLowerCase().contains(filterString))
						|| (occupant.getTel() != null && occupant.getTel().toLowerCase().contains(filterString))
						|| (occupant.getRut() != null && occupant.getRut().toLowerCase().contains(filterString))
						|| (occupant.getMail() != null && occupant.getMail().toLowerCase().contains(filterString))
						|| (occupant.getCell() != null && occupant.getCell().toLowerCase().contains(filterString)))
					aux.add(occupant);
			occupantsTable.clear();
			occupantsTable.setBeans(aux);
		} else {
			occupantsTable.clear();
			occupantsTable.setBeans(occupants);
		}

	}

	void editEntity(Occupant occupant) {
		if (occupant != null) {
			openEditor(occupant);
		} else {
			closeEditor();
		}
	}

	void addOccupant() {
		openEditor(new Occupant());
	}

	private void openEditor(Occupant occupant) {
		occupantEditor.setEntity(occupant);
		// display next to table on desktop class screens
		if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
			mainContent.addComponent(occupantEditor);
			occupantEditor.focusFirst();
		} else {
			removeAllComponents();
			addComponent(occupantEditor);
			expand(occupantEditor);
			markAsDirty();
		}
	}

	private void closeEditor() {
		// As we display the editor differently in different devices,
		// close properly in each modes
		if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
			mainContent.removeComponent(occupantEditor);
		} else {
			removeAllComponents();
			addComponents(header,
					new MHorizontalLayout(filter, addButton).expand(filter).alignAll(Alignment.MIDDLE_LEFT),
					mainContent);
			setMargin(new MarginInfo(false, true, true, true));
			expand(mainContent);
		}
	}

	void saveOccupant(Occupant occupant) {
		if (!occupants.contains(occupant))
			occupants.add(occupant);
		occupantsTable.refreshRowCache();
		closeEditor();
	}

	void resetOccupant(Occupant occupant) {
		if (occupant != null) {
			int index = occupants.indexOf(occupant);
			occupants.remove(occupant);
			occupants.add(index, occupant);
		}
		occupantsTable.clear();
		occupantsTable.setBeans(occupants);
		closeEditor();
	}

	void deleteOccupant(Occupant occupant) {
		occupants.remove(occupant);
		occupantsTable.refreshRowCache();
		closeEditor();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		occupantsTable.addMValueChangeListener(new MValueChangeListener<Occupant>() {

			@Override
			public void valueChange(MValueChangeEvent<Occupant> event) {
				editEntity(event.getValue());
			}
		});

		/*
		 * Configure the filter input and hook to text change events to
		 * repopulate the table based on given filter. Text change events are
		 * sent to the server when e.g. user holds a tiny pause while typing or
		 * hits enter.
		 */
		filter.setInputPrompt("Filtrar Inquilinos");
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
		occupants = OccupantManager.getInstance().getAll();
		occupantsTable.setBeans(occupants);
		if (VaadinSession.getCurrent().getAttribute(Occupant.class) != null) {
			Occupant aux = VaadinSession.getCurrent().getAttribute(Occupant.class);
			VaadinSession.getCurrent().setAttribute(Occupant.class, null);
			editEntity(aux);
		}
	}

}

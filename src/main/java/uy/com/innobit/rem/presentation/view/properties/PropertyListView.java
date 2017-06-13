package uy.com.innobit.rem.presentation.view.properties;

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

import uy.com.innobit.rem.business.managers.PropertyManager;
import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.presentation.ScreenSize;

@SuppressWarnings("serial")
public class PropertyListView extends MVerticalLayout implements View {

	PropertyForm propertyEditor = new PropertyForm(this);

	// Introduce and configure some UI components used on this view
	MTable<Property> propertyTable = new MTable(Property.class).withFullWidth().withFullHeight();

	List<Property> properties = new ArrayList<Property>();

	MHorizontalLayout mainContent = new MHorizontalLayout(propertyTable).withFullWidth().withMargin(false);

	TextField filter = new TextField();

	Header header = new Header("Propiedades").setHeaderLevel(2);

	Button addButton = new MButton(FontAwesome.EDIT, new Button.ClickListener() {
		@Override
		public void buttonClick(Button.ClickEvent event) {
			addProperty();
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
		if (propertyTable.getColumnGenerator("propertyName") == null) {
			propertyTable.addGeneratedColumn("propertyName", new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table table, Object o, Object o2) {
					Property c = (Property) o;
					if (c.getOwner() != null)
						return c.getOwner().getName();
					else
						return "N/A";
				}
			});
		}
		if (propertyTable.getColumnGenerator("address") == null) {
			propertyTable.addGeneratedColumn("address", new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table table, Object o, Object o2) {
					Property c = (Property) o;
					if (c.getAddress() != null && c.getAddress().length() > 40)
						return c.getAddress().substring(0, 40) + "...";
					else
						return c.getAddress();
				}
			});
		}
		if (propertyTable.getColumnGenerator("state") == null) {
			propertyTable.addGeneratedColumn("state", new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table table, Object o, Object o2) {
					Property c = (Property) o;
					return c.isEmpty() ? "Libre" : "Ocupado";
				}
			});
		}
		if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {

			propertyTable.setVisibleColumns("name", "propertyName", "padron", "nro", "address", "state");
			propertyTable.setColumnHeaders("Nombre", "Propietario", "Padrón", "Número", "Dirección", "Estado");

		} else {
			if (ScreenSize.getScreenSize() == ScreenSize.MEDIUM) {
				propertyTable.setVisibleColumns("name", "propertyName");
				propertyTable.setColumnHeaders("Nombre", "Propietario");
			} else {
				propertyTable.setVisibleColumns("name");
				propertyTable.setColumnHeaders("Nombre");
			}
		}
	}

	private synchronized void listCustomers(String filterString) {

		if (filterString != null && !filterString.isEmpty()) {
			filterString = filterString.toLowerCase();

			List<Property> aux = new ArrayList<Property>();
			for (Property property : properties)
				if ((property.getName() != null && property.getName().toLowerCase().contains(filterString))
						|| (property.getNro() != null && property.getNro().toLowerCase().contains(filterString))
						|| (property.getPadron() != null && property.getPadron().toLowerCase().contains(filterString))
						|| (property.getBlock() != null && property.getBlock().toLowerCase().contains(filterString))
						|| (property.getTel() != null && property.getTel().toLowerCase().contains(filterString))
						|| (property.getAddress() != null
								&& property.getAddress().toLowerCase().contains(filterString)))
					aux.add(property);
			propertyTable.clear();
			propertyTable.setBeans(aux);
		} else {
			propertyTable.clear();
			propertyTable.setBeans(properties);
		}

	}

	void editEntity(Property property) {
		if (property != null) {
			openEditor(property);
		} else {
			closeEditor();
		}
	}

	void addProperty() {
		openEditor(new Property());
	}

	private void openEditor(Property property) {
		propertyEditor.setEntity(property);
		propertyEditor.refreshOwner();
		// display next to table on desktop class screens
		if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
			mainContent.addComponent(propertyEditor);
			propertyEditor.focusFirst();
		} else {
			removeAllComponents();
			addComponent(propertyEditor);
			expand(propertyEditor);
			markAsDirty();
		}
	}

	private void closeEditor() {
		// As we display the editor differently in different devices,
		// close properly in each modes
		if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
			mainContent.removeComponent(propertyEditor);
		} else {
			removeAllComponents();
			addComponents(header,
					new MHorizontalLayout(filter, addButton).expand(filter).alignAll(Alignment.MIDDLE_LEFT),
					mainContent);
			setMargin(new MarginInfo(false, true, true, true));
			expand(mainContent);
		}
	}

	void saveProperty(Property property) {
		if (!properties.contains(property))
			properties.add(property);
		propertyTable.refreshRowCache();
		closeEditor();
	}

	void resetProperty(Property property) {
		if (property != null) {
			int index = properties.indexOf(property);
			properties.remove(property);
			properties.add(index, property);
		}
		propertyTable.clear();
		propertyTable.setBeans(properties);
		closeEditor();
	}

	void deleteProperty(Property property) {
		properties.remove(property);
		propertyTable.refreshRowCache();
		closeEditor();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		propertyTable.addMValueChangeListener(new MValueChangeListener<Property>() {

			@Override
			public void valueChange(MValueChangeEvent<Property> event) {
				editEntity(event.getValue());
			}
		});

		/*
		 * Configure the filter input and hook to text change events to
		 * repopulate the table based on given filter. Text change events are
		 * sent to the server when e.g. user holds a tiny pause while typing or
		 * hits enter.
		 */
		filter.setInputPrompt("Filtrar Propiedades");
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
		properties = PropertyManager.getInstance().getAll();
		propertyTable.setBeans(properties);
		if (VaadinSession.getCurrent().getAttribute(Property.class) != null) {
			Property aux = VaadinSession.getCurrent().getAttribute(Property.class);
			VaadinSession.getCurrent().setAttribute(Property.class, null);
			editEntity(aux);
		}
	}

}

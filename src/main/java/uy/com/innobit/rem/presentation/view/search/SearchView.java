package uy.com.innobit.rem.presentation.view.search;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.business.managers.OccupantManager;
import uy.com.innobit.rem.business.managers.OwnerManager;
import uy.com.innobit.rem.business.managers.PropertyManager;
import uy.com.innobit.rem.persistence.datamodel.clients.Occupant;
import uy.com.innobit.rem.persistence.datamodel.clients.Owner;
import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.presentation.ScreenSize;

@SuppressWarnings("serial")
public class SearchView extends MVerticalLayout implements View {

	Table resultsTable = new Table();

	MHorizontalLayout mainContent = new MHorizontalLayout(resultsTable).withFullWidth().withMargin(false);

	Header header = new Header("Busqueda").setHeaderLevel(2);

	private void layout() {
		removeAllComponents();
		if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
			addComponents(new MHorizontalLayout(header).expand(header).alignAll(Alignment.MIDDLE_LEFT), mainContent);
		} else {
			addComponents(header, mainContent);
		}
		setMargin(new MarginInfo(false, true, true, true));
		expand(mainContent);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		resultsTable.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

			}
		});
		layout();
		UI.getCurrent().setResizeLazy(true);
		Page.getCurrent().addBrowserWindowResizeListener(new Page.BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(Page.BrowserWindowResizeEvent browserWindowResizeEvent) {
				layout();
			}
		});

		IndexedContainer cont = new IndexedContainer();
		cont.addContainerProperty("type", String.class, "");

		resultsTable.setContainerDataSource(cont);
		resultsTable.setVisibleColumns("type");
		resultsTable.setColumnHeaders("Tipo");
		String query = VaadinSession.getCurrent().getAttribute(String.class);
		if (query != null && !query.equalsIgnoreCase("")) {
			List<Owner> owners = OwnerManager.getInstance().getAll();
			List<Occupant> occupants = OccupantManager.getInstance().getAll();
			List<Property> properties = PropertyManager.getInstance().getAll();

			for (Owner not : listCustomers(owners, query)) {
				cont.addItem(not);
				cont.getContainerProperty(not, "type").setValue("Propietario");
			}
			for (Occupant not : listOccupants(occupants, query)) {
				cont.addItem(not);
				cont.getContainerProperty(not, "type").setValue("Inqilino");
			}
			for (Property not : listProperty(properties, query)) {
				cont.addItem(not);
				cont.getContainerProperty(not, "type").setValue("Propiedad");
			}

		}
		resultsTable.addGeneratedColumn("data", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button button = new Button();
				button.setStyleName(ValoTheme.BUTTON_LINK);
				if (itemId instanceof Owner) {
					Owner new_name = (Owner) itemId;
					if (new_name.getName() != null && !new_name.getName().equalsIgnoreCase("")) {
						button.setCaption(new_name.getName());
					}
					button.addClickListener(new ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
							VaadinSession.getCurrent().setAttribute(Owner.class, new_name);
							UI.getCurrent().getNavigator().navigateTo("/owners");

						}
					});
				}
				if (itemId instanceof Occupant) {
					Occupant new_name = (Occupant) itemId;
					if (new_name.getName() != null && !new_name.getName().equalsIgnoreCase("")) {
						button.setCaption(new_name.getName());
					}
					button.addClickListener(new ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
							VaadinSession.getCurrent().setAttribute(Occupant.class, new_name);
							UI.getCurrent().getNavigator().navigateTo("/occuppants");

						}
					});
				}
				if (itemId instanceof Property) {
					Property new_name = (Property) itemId;
					if (new_name.getName() != null && !new_name.getName().equalsIgnoreCase("")) {
						button.setCaption(new_name.getName());
					}
					button.addClickListener(new ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
							VaadinSession.getCurrent().setAttribute(Property.class, new_name);
							UI.getCurrent().getNavigator().navigateTo("/properties");

						}
					});
				}
				return button;
			}
		});

		resultsTable.setWidth("100%");

	}

	private synchronized List<Owner> listCustomers(List<Owner> owners, String filterString) {
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
			return aux;
		}
		return owners;

	}

	private synchronized List<Property> listProperty(List<Property> properties, String filterString) {
		if (filterString != null && !filterString.isEmpty()) {
			List<Property> aux = new ArrayList<Property>();
			for (Property property : properties)
				if ((property.getName() != null && property.getName().contains(filterString))
						|| (property.getNro() != null && property.getNro().contains(filterString))
						|| (property.getPadron() != null && property.getPadron().contains(filterString))
						|| (property.getBlock() != null && property.getBlock().contains(filterString))
						|| (property.getTel() != null && property.getTel().contains(filterString))
						|| (property.getAddress() != null && property.getAddress().contains(filterString)))
					aux.add(property);
			return aux;
		}
		return properties;

	}

	private List<Occupant> listOccupants(List<Occupant> occupants, String filterString) {
		if (filterString != null && !filterString.isEmpty()) {
			List<Occupant> aux = new ArrayList<Occupant>();

			for (Occupant occupant : occupants)
				if ((occupant.getName() != null && occupant.getName().contains(filterString))
						|| (occupant.getSurname() != null && occupant.getSurname().contains(filterString))
						|| (occupant.getDoc() != null && occupant.getDoc().contains(filterString))
						|| (occupant.getSocialReason() != null && occupant.getSocialReason().contains(filterString))
						|| (occupant.getTel() != null && occupant.getTel().contains(filterString))
						|| (occupant.getRut() != null && occupant.getRut().contains(filterString))
						|| (occupant.getMail() != null && occupant.getMail().contains(filterString))
						|| (occupant.getCell() != null && occupant.getCell().contains(filterString)))
					aux.add(occupant);
			return aux;
		}
		return occupants;

	}

}

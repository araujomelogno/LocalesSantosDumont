package uy.com.innobit.rem.presentation.view.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.google.gwt.dev.js.DuplicateExecuteOnceRemover;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;

import uy.com.innobit.rem.business.managers.OccupantManager;
import uy.com.innobit.rem.business.managers.OwnerManager;
import uy.com.innobit.rem.business.managers.PropertyManager;
import uy.com.innobit.rem.persistence.datamodel.clients.Occupant;
import uy.com.innobit.rem.persistence.datamodel.clients.Owner;
import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.presentation.view.properties.PropertyForm;

@SuppressWarnings({ "serial", "unchecked" })
public class MapView extends MVerticalLayout implements View {
	Header header = new Header("Propiedades").setHeaderLevel(2);
	ComboBox stateFilter = new ComboBox("Estado");
	ComboBox ownerFilter = new ComboBox("Propietario");
	ComboBox clientFilter = new ComboBox("Inquilino");
	ComboBox showWindow = new ComboBox("Mostrar nombres");
	GoogleMap googleMap = new GoogleMap("AIzaSyDGRIg7CtaNCjrCzohdm6J_xo7gKSlHV30", null, "spanish");
	List<Property> allProperties = new ArrayList<Property>();
	List<Property> showProperties = new ArrayList<Property>();
	List<GoogleMapInfoWindow> infos = new ArrayList<GoogleMapInfoWindow>();
	Map<GoogleMapMarker, Property> directory = new HashMap<GoogleMapMarker, Property>();

	void init() {
		for (Property p : PropertyManager.getInstance().getAll())
			if (p.getLat() != 0d && p.getLng() != 0d)
				allProperties.add(p);
		add(new MHorizontalLayout(header, showWindow, stateFilter, ownerFilter, clientFilter).expand(header)
				.alignAll(Alignment.MIDDLE_LEFT), googleMap);
		googleMap.setSizeFull();
		expand(googleMap);
		googleMap.setCenter(new LatLon(-34.9424383, -54.9468935));
		googleMap.setMinZoom(4);
		googleMap.setMaxZoom(16);
		googleMap.setZoom(13);
		setMargin(new MarginInfo(false, false, false, false));
		stateFilter.setNewItemsAllowed(false);
		stateFilter.setTextInputAllowed(false);
		stateFilter.addItem("Libre");
		stateFilter.addItem("Ocupado");
		stateFilter.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				filterProperties();
			}

		});
		showWindow.setNewItemsAllowed(false);
		showWindow.setTextInputAllowed(false);
		showWindow.setNullSelectionAllowed(false);
		showWindow.addItem("No");
		showWindow.addItem("Sí");
		showWindow.select("No");
		showWindow.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (showWindow.getValue().toString().equalsIgnoreCase("sí")) {
					for (GoogleMapInfoWindow w : infos)
						googleMap.openInfoWindow(w);
				} else {
					filterProperties();
				}
			}
		});

		ownerFilter.setNewItemsAllowed(false);
		ownerFilter.setTextInputAllowed(true);
		for (Owner ow : OwnerManager.getInstance().getAll())
			ownerFilter.addItem(ow);

		ownerFilter.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				filterProperties();
			}

		});

		clientFilter.setNewItemsAllowed(false);
		clientFilter.setTextInputAllowed(true);
		for (Occupant oc : OccupantManager.getInstance().getAll())
			clientFilter.addItem(oc);

		clientFilter.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				filterProperties();
			}

		});

		for (Property p : allProperties) {
			GoogleMapMarker marker = new GoogleMapMarker(p.getName(), new LatLon(p.getLat(), p.getLng()), false);
			if (p.isEmpty())
				marker.setIconUrl("http://maps.google.com/mapfiles/marker_green.png");
			else
				marker.setIconUrl("http://maps.google.com/mapfiles/marker_red.png");
			googleMap.addMarker(marker);

			marker.setCaption(p.getName());
			GoogleMapInfoWindow infoW = new GoogleMapInfoWindow(p.getName(), marker);
			directory.put(marker, p);
			infos.add(infoW);
		}

		googleMap.addMarkerClickListener(new MarkerClickListener() {

			@Override
			public void markerClicked(GoogleMapMarker clickedMarker) {
				VaadinSession.getCurrent().setAttribute(Property.class, directory.get(clickedMarker));
				UI.getCurrent().getNavigator().navigateTo("/properties");
			}
		});

	}

	private void filterProperties() {
		showProperties.clear();
		directory.clear();
		googleMap.clearMarkers();
		infos.clear();
		for (Property p : allProperties) {
			if (stateFilter.getValue() == null
					|| (stateFilter.getValue().toString().equalsIgnoreCase("libre") && p.isEmpty())
					|| (stateFilter.getValue().toString().equalsIgnoreCase("ocupado") && !p.isEmpty())) {
				if (ownerFilter.getValue() == null
						|| ownerFilter.getValue().toString().equals(p.getOwner().toString())) {
					if (clientFilter.getValue() == null || p.getActualContract() != null && p.getActualContract()
							.getOccupant().toString().equalsIgnoreCase(clientFilter.getValue().toString()))
						showProperties.add(p);
				}
			}
		}
		for (Property p : showProperties) {
			GoogleMapMarker marker = new GoogleMapMarker(p.getName(), new LatLon(p.getLat(), p.getLng()), false);
			if (p.isEmpty())
				marker.setIconUrl("http://maps.google.com/mapfiles/marker_green.png");
			else
				marker.setIconUrl("http://maps.google.com/mapfiles/marker_red.png");
			googleMap.addMarker(marker);
			marker.setCaption(p.getName());
			directory.put(marker, p);
			if (showWindow.getValue().toString().equalsIgnoreCase("sí")) {
				GoogleMapInfoWindow infoW = new GoogleMapInfoWindow(p.getName(), marker);
				googleMap.openInfoWindow(infoW);
				infos.add(infoW);
			}

		}

	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		init();
	}

}
package uy.com.innobit.rem.presentation.view.map;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.control.LZoom;
import org.vaadin.addon.leaflet.shared.ControlPosition;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Notification;

import uy.com.innobit.rem.business.managers.PropertyManager;
import uy.com.innobit.rem.persistence.datamodel.property.Property;

@SuppressWarnings({ "serial", "unchecked" })
public class MapView extends MVerticalLayout implements View {

	LMap worldMap = new LMap();

	void init() {
		add(new Header("Propiedades").setHeaderLevel(2));
		expand(worldMap);
		setMargin(new MarginInfo(false, true, true, true));
		LZoom zoom = new LZoom();
		zoom.setPosition(ControlPosition.topright);
		worldMap.addControl(zoom);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		init();
		fillMap();
	}

	private void fillMap() {
		worldMap.removeAllComponents();
		LOpenStreetMapLayer osm = new LOpenStreetMapLayer();
		osm.setDetectRetina(true);
		worldMap.addComponent(osm);
		for (final Property p : PropertyManager.getInstance().getAll()) {
			if (p.getLat() != 0d && p.getLng() != 0d) {
				LMarker marker = new LMarker(new Point(p.getLat(), p.getLng()));
				marker.addClickListener(new LeafletClickListener() {
					@Override
					public void onClick(LeafletClickEvent event) {
						Notification.show("Propiedad: " + p.getName());
					}
				});
				worldMap.addComponent(marker);
			}
		}

		worldMap.zoomToContent();
	}

}
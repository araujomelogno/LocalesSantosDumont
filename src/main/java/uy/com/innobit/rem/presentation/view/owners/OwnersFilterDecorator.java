package uy.com.innobit.rem.presentation.view.owners;


import java.io.Serializable;
import java.text.DateFormat;
import java.util.Locale;

import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.datefield.Resolution;

@SuppressWarnings("serial")
public class OwnersFilterDecorator implements FilterDecorator, Serializable {

    @Override
    public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
        if ("validated".equals(propertyId)) {
            return value ? "Validated" : "Not validated";
        }
        // returning null will output default value
        return null;
    }

    @Override
    public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
        if ("validated".equals(propertyId)) {
            return value ? new ThemeResource("../runo/icons/16/ok.png")
                    : new ThemeResource("../runo/icons/16/cancel.png");
        }
        return null;
    }

    @Override
    public String getFromCaption() {
        return "Start date:";
    }

    @Override
    public String getToCaption() {
        return "End date:";
    }

    @Override
    public String getSetCaption() {
        // use default caption
        return null;
    }

    @Override
    public String getClearCaption() {
        // use default caption
        return null;
    }

    @Override
    public boolean isTextFilterImmediate(Object propertyId) {
         return true;
    }

    @Override
    public int getTextChangeTimeout(Object propertyId) {
        // use the same timeout for all the text fields
        return 500;
    }

    @Override
    public String getAllItemsVisibleString() {
        return "Todos";
    }

    @Override
    public Resolution getDateFieldResolution(Object propertyId) {
        return Resolution.DAY;
    }

    public DateFormat getDateFormat(Object propertyId) {
        return DateFormat.getDateInstance(DateFormat.SHORT, new Locale("fi",
                "FI"));
    }

    @Override
    public boolean usePopupForNumericProperty(Object propertyId) {
         return true;
    }

    @Override
    public String getDateFormatPattern(Object propertyId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Locale getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NumberFilterPopupConfig getNumberFilterPopupConfig() {
    	NumberFilterPopupConfig f = new NumberFilterPopupConfig();
    	f.setEqPrompt("Igual");
    	f.setGtPrompt("Mayor");
    	f.setLtPrompt("Menor");
    	f.setOkCaption("Aceptar");
    	f.setResetCaption("Limpiar");
    	f.setValueMarker("valor");
        return f;
    }

	@Override
	public String getEnumFilterDisplayName(Object propertyId, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getEnumFilterIcon(Object propertyId, Object value) {
		// TODO Auto-generated method stub
		return null;
	}
}

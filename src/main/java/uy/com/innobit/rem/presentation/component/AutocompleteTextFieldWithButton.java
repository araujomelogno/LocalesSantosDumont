package uy.com.innobit.rem.presentation.component;

import java.util.List;
import java.util.Locale;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

public class AutocompleteTextFieldWithButton extends CssLayout {

	AutocompleteTextField field = new AutocompleteTextField();

	private final Button button;

	public AutocompleteTextFieldWithButton(String caption, Resource icon, ClickListener listener,
			List<String> arrayList, ValueChangeListener changelistener) {
		setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		setCaption(caption);
		AutocompleteSuggestionProvider suggestionProvider = new CollectionSuggestionProvider(arrayList,
				MatchMode.CONTAINS, true, Locale.US);
		field.setWidth(100, Unit.PERCENTAGE);
		field.setCache(true);
		field.setDelay(75);
		field.setItemAsHtml(false);
		field.setMinChars(1);
		field.setSuggestionLimit(0);

		field.setSuggestionProvider(suggestionProvider);
		field.addValueChangeListener(changelistener);
		button = new Button(icon);
		button.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		button.addClickListener(listener);

		addComponents(field, button);
	}

	public TextField getTextField() {
		return field;
	}

	public Button getButton() {
		return button;
	}
}
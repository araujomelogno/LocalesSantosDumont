package uy.com.innobit.rem.presentation.component;

import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class TextFieldWithButton extends CssLayout {

	private final TextField textField;
	private final Button button;

	public TextFieldWithButton(String caption, Resource icon, ClickListener listener) {
		setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		setCaption(caption);

		textField = new TextField();
		textField.setWidth(100, Unit.PERCENTAGE);
		
		button = new Button(icon);
		button.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		button.addClickListener(listener);

		addComponents(textField, button);
	}

	public TextField getTextField() {
		return textField;
	}

	public Button getButton() {
		return button;
	}
}
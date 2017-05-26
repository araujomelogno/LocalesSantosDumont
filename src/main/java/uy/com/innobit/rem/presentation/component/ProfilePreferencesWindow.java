package uy.com.innobit.rem.presentation.component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.business.managers.UserManager;
import uy.com.innobit.rem.persistence.datamodel.user.User;
import uy.com.innobit.rem.presentation.RemUI;

@SuppressWarnings("serial")
public class ProfilePreferencesWindow extends Window {

	public static final String ID = "profilepreferenceswindow";

	private final BeanFieldGroup<User> fieldGroup;
	/*
	 * Fields for editing the User object are defined here as class members.
	 * They are later bound to a FieldGroup by calling
	 * fieldGroup.bindMemberFields(this). The Fields' values don't need to be
	 * explicitly set, calling fieldGroup.setItemDataSource(user) synchronizes
	 * the fields with the user object.
	 */
	@PropertyId("name")
	private TextField firstNameField;
	@PropertyId("email")
	private TextField emailField;
	@PropertyId("login")
	private TextField loginField;
	@PropertyId("password")
	private PasswordField password;

	private Image profilePic;

	private ProfilePreferencesWindow(final User user, final boolean preferencesTabOpen) {
		addStyleName("profile-window");
		setId(ID);
		Responsive.makeResponsive(this);

		setModal(true);
		setCloseShortcut(KeyCode.ESCAPE, null);
		setResizable(false);
		setClosable(false);
		setHeight(90.0f, Unit.PERCENTAGE);

		VerticalLayout content = new VerticalLayout();
		content.setSizeFull();
		content.setMargin(new MarginInfo(true, false, false, false));
		setContent(content);

		TabSheet detailsWrapper = new TabSheet();
		detailsWrapper.setSizeFull();
		detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
		detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
		content.addComponent(detailsWrapper);
		content.setExpandRatio(detailsWrapper, 1f);

		detailsWrapper.addComponent(buildProfileTab());

		if (preferencesTabOpen) {
			detailsWrapper.setSelectedTab(1);
		}

		content.addComponent(buildFooter());

		fieldGroup = new BeanFieldGroup<User>(User.class);
		fieldGroup.bindMemberFields(this);
		fieldGroup.setItemDataSource(RemUI.get().getLoggedUser());
	}

	private Component buildProfileTab() {
		HorizontalLayout root = new HorizontalLayout();
		root.setCaption("Perfil de usuario");
		root.setIcon(FontAwesome.USER);
		root.setWidth(100.0f, Unit.PERCENTAGE);
		root.setSpacing(true);
		root.setMargin(true);
		root.addStyleName("profile-form");

		VerticalLayout pic = new VerticalLayout();
		pic.setSizeUndefined();
		pic.setSpacing(true);

		if (RemUI.get().getLoggedUser().getPicture() == null)
			profilePic = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
		else {
			StreamResource resource = new StreamResource(new StreamResource.StreamSource() {
				private static final long serialVersionUID = 1L;

				@Override
				public InputStream getStream() {
					return new ByteArrayInputStream(RemUI.get().getLoggedUser().getPicture());
				}
			}, "");
			// Instruct browser not to cache the image
			resource.setCacheTime(0);
			profilePic = new Image(null, resource);
		}
		profilePic.setWidth(100.0f, Unit.PIXELS);

		pic.addComponent(profilePic);

		Upload upload = new Upload("", new Receiver() {
			@Override
			public OutputStream receiveUpload(final String filename, String mimeType) {
				return new ByteArrayOutputStream() {
					@Override
					public void close() throws IOException {
						if (filename.contains("jpg") || filename.contains("jpeg") || filename.contains("png")) {
							byte[] uploadData = toByteArray();
							RemUI.get().getLoggedUser().setPicture(uploadData);
							StreamResource resource = new StreamResource(new StreamResource.StreamSource() {
								private static final long serialVersionUID = 1L;

								@Override
								public InputStream getStream() {
									return new ByteArrayInputStream(RemUI.get().getLoggedUser().getPicture());
								}
							}, "");

							resource.setCacheTime(0);
							resource.setFilename(resource.getFilename() + "1");
							profilePic.setSource(resource);
							profilePic.markAsDirty();
						} else {
							Notification.show("Tipo de imagen no soportada");
						}
					}
				};
			}
		});
		upload.setButtonCaption("Cambiar");
		upload.setImmediate(true);
		upload.addStyleName(ValoTheme.BUTTON_TINY);
		pic.addComponent(upload);

		root.addComponent(pic);

		FormLayout details = new FormLayout();
		details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		root.addComponent(details);
		root.setExpandRatio(details, 1);

		firstNameField = new TextField("Nombre:");
		details.addComponent(firstNameField);
		loginField = new TextField("Login:");
		details.addComponent(loginField);

		password = new PasswordField("Password:");
		details.addComponent(password);

		emailField = new TextField("Email");
		emailField.setWidth("100%");
		emailField.setRequired(true);
		emailField.setNullRepresentation("");
		details.addComponent(emailField);

		return root;
	}

	private Component buildFooter() {
		HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		footer.setWidth(100.0f, Unit.PERCENTAGE);

		Button ok = new Button("OK");
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					fieldGroup.commit();
					// Updated user should also be persisted to database. But
					// not in this demo.
					UserManager.getInstance().updateUser(RemUI.get().getLoggedUser());
					Notification success = new Notification("Profile updated successfully");
					success.setDelayMsec(2000);
					success.setStyleName("bar success small");
					success.setPosition(Position.BOTTOM_CENTER);
					success.show(Page.getCurrent());
					close();
				} catch (CommitException e) {
					e.printStackTrace();
					Notification.show("Error while updating profile", Type.ERROR_MESSAGE);
				}

			}
		});
		ok.focus();
		footer.addComponent(ok);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
		return footer;
	}

	public static void open(final User user, final boolean preferencesTabActive) {
		Window w = new ProfilePreferencesWindow(user, preferencesTabActive);
		UI.getCurrent().addWindow(w);
		w.focus();
	}
}

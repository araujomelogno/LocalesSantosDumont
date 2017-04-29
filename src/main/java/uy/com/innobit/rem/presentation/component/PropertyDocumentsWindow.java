package uy.com.innobit.rem.presentation.component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.business.managers.PropertyManager;
import uy.com.innobit.rem.persistence.datamodel.property.PropertyDocument;
import uy.com.innobit.rem.presentation.view.properties.PropertyForm;

@SuppressWarnings("serial")
public class PropertyDocumentsWindow extends Window {
	public static final String ID = "paymentsWindow";

	Table table;

	private PropertyForm form;

	private PropertyDocumentsWindow(PropertyForm form) {
		addStyleName("profile-window");
		setId(ID);
		Responsive.makeResponsive(this);
		setModal(true);
		setCloseShortcut(KeyCode.ESCAPE, null);
		setResizable(false);
		setClosable(false);
		setHeight(90.0f, Unit.PERCENTAGE);
		setWidth(70.0f, Unit.PERCENTAGE);
		this.form = form;

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
		detailsWrapper.addComponent(buildDocumentsTab());

		content.addComponent(buildFooter());
	}

	private Component buildDocumentsTab() {
		VerticalLayout root = new VerticalLayout();
		root.setCaption("Documentos");
		root.setIcon(FontAwesome.BRIEFCASE);
		root.setWidth(100.0f, Unit.PERCENTAGE);
		root.setSpacing(true);
		root.setMargin(true);
		root.addStyleName("profile-form");

		Upload upload = new Upload("", new Receiver() {
			@Override
			public OutputStream receiveUpload(final String filename, String mimeType) {
				return new ByteArrayOutputStream() {
					@Override
					public void close() throws IOException {
						byte[] uploadData = toByteArray();
						PropertyDocument document = new PropertyDocument();
						document.setUploaded(new Date());
						document.setContent(uploadData);
						document.setName(filename);
						String name = filename;
						if (name.contains("doc") || name.contains("docx"))
							document.setMimeType("application/msword");
						if (name.contains("ppt") || name.contains("pptx"))
							document.setMimeType("application/vnd.ms-powerpoint");
						if (name.contains("xls") || name.contains("xlsx"))
							document.setMimeType("application/vnd.ms-excel");
						if (name.contains("pdf"))
							document.setMimeType("application/pdf");
						if (name.contains("jpeg"))
							document.setMimeType("image/jpeg");
						// ESTO HAY QUE HACERLO IGUAL
						// ProjectManager.getInstance().initialize(getEntity());
						form.getEntity().getDocuments().add(document);
						PropertyManager.getInstance().saveDocument(document);
						PropertyManager.getInstance().updateUser(form.getEntity());
						table.getContainerDataSource().addItem(document);
					}
				};
			}
		});
		upload.setButtonCaption("Subir documento");
		upload.setImmediate(true);
		root.addComponents(upload, buildDocumentsTable());
		return root;
	}

	private Component buildDocumentsTable() {
		BeanItemContainer<PropertyDocument> fws = new BeanItemContainer<PropertyDocument>(PropertyDocument.class);
		table = new Table();
		table.setContainerDataSource(fws);
		table.setSizeFull();
		for (PropertyDocument doc : this.form.getEntity().getDocuments()) {
			fws.addBean(doc);
		}

		table.addGeneratedColumn("download", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				final PropertyDocument doc = (PropertyDocument) itemId;
				Button download = new Button(doc.getName());
				download.setStyleName(ValoTheme.BUTTON_LINK);
				StreamResource resource = new StreamResource(new StreamResource.StreamSource() {
					private static final long serialVersionUID = 1L;

					@Override
					public InputStream getStream() {
						return new ByteArrayInputStream(doc.getContent());
					}
				}, doc.getName());
				FileDownloader fileDownloader = new FileDownloader(resource);
				fileDownloader.extend(download);
				return download;
			}
		});
		table.addGeneratedColumn("delete", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button button = new Button("Borrar");
				button.setStyleName(ValoTheme.BUTTON_LINK);
				button.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						PropertyDocument doc = (PropertyDocument) itemId;
						table.getContainerDataSource().removeItem(doc);
						form.getEntity().getDocuments().remove(doc);
						PropertyManager.getInstance().delete(doc);
						PropertyManager.getInstance().updateUser(form.getEntity());

					}
				});
				return button;
			}
		});
		table.setVisibleColumns("dateSDF", "download", "delete");
		table.setColumnHeaders("Subido", "Nombre", "");
		table.setImmediate(true);
		table.setBuffered(false);

		return table;
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
				PropertyManager.getInstance().updateUser(form.getEntity());
				close();
			}
		});
		ok.focus();
		footer.addComponent(ok);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

		return footer;
	}

	public static void open(PropertyForm form) {
		PropertyDocumentsWindow w = new PropertyDocumentsWindow(form);
		UI.getCurrent().addWindow(w);
		w.focus();
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public PropertyForm getForm() {
		return form;
	}

	public void setForm(PropertyForm form) {
		this.form = form;
	}

}

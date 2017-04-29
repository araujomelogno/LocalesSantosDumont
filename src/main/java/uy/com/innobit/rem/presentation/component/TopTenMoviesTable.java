package uy.com.innobit.rem.presentation.component;

import java.text.DecimalFormat;

import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public final class TopTenMoviesTable extends Table {

	@Override
	protected String formatPropertyValue(final Object rowId, final Object colId, final Property<?> property) {
		String result = super.formatPropertyValue(rowId, colId, property);
		if (colId.equals("revenue")) {
			if (property != null && property.getValue() != null) {
				Double r = (Double) property.getValue();
				String ret = new DecimalFormat("#.##").format(r);
				result = "$" + ret;
			} else {
				result = "";
			}
		}
		return result;
	}

	public TopTenMoviesTable() {
		setCaption("Contratos próximos a vencer");

		addStyleName(ValoTheme.TABLE_BORDERLESS);
		addStyleName(ValoTheme.TABLE_NO_STRIPES);
		addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
		addStyleName(ValoTheme.TABLE_SMALL);
		setSortEnabled(false);
		setColumnAlignment("date", Align.RIGHT);
		setRowHeaderMode(RowHeaderMode.INDEX);
		setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		setSizeFull();

		IndexedContainer cont = new IndexedContainer();
		cont.addContainerProperty("name", String.class, null);
		cont.addContainerProperty("date", String.class, null);
		setContainerDataSource(cont);

		getContainerDataSource().addItem(1);
		getContainerDataSource().getContainerProperty(1, "name").setValue("Propiedad xxx");
		getContainerDataSource().getContainerProperty(1, "date").setValue("15/11/2015");

		getContainerDataSource().addItem(2);
		getContainerDataSource().getContainerProperty(2, "name").setValue("Propiedad xxy");
		getContainerDataSource().getContainerProperty(2, "date").setValue("20/11/2015");

		getContainerDataSource().addItem(3);
		getContainerDataSource().getContainerProperty(3, "name").setValue("Propiedad xxz");
		getContainerDataSource().getContainerProperty(3, "date").setValue("20/11/2015");

		getContainerDataSource().addItem(4);
		getContainerDataSource().getContainerProperty(4, "name").setValue("Propiedad xzy");
		getContainerDataSource().getContainerProperty(4, "date").setValue("20/12/2015");

		getContainerDataSource().addItem(5);
		getContainerDataSource().getContainerProperty(4, "name").setValue("Propiedad xzy");
		getContainerDataSource().getContainerProperty(4, "date").setValue("20/12/2015");

		setVisibleColumns("name", "date");
		setColumnHeaders("Propiedad", "Fecha");
		setColumnExpandRatio("name", 2);
		setColumnExpandRatio("date", 1);

		setSortAscending(false);
	}

}

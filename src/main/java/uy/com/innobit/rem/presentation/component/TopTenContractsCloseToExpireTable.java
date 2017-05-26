package uy.com.innobit.rem.presentation.component;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

import uy.com.innobit.rem.presentation.view.dashboard.DashboardStats;

@SuppressWarnings("serial")
public final class TopTenContractsCloseToExpireTable extends Table {

	public TopTenContractsCloseToExpireTable(DashboardStats stats) {
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for (int i = 0; i < stats.getExpirations().size(); i++) {
			getContainerDataSource().addItem(i);
			getContainerDataSource().getContainerProperty(i, "name")
					.setValue(stats.getExpirations().get(i)[0].toString());
			getContainerDataSource().getContainerProperty(i, "date")
					.setValue(sdf.format((Date) stats.getExpirations().get(i)[1]));

		}
		setVisibleColumns("name", "date");
		setColumnHeaders("Propiedad", "Fecha");
		setColumnExpandRatio("name", 2);
		setColumnExpandRatio("date", 1);

		setSortAscending(false);
	}

}

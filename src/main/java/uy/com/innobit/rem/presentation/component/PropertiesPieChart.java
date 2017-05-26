package uy.com.innobit.rem.presentation.component;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Credits;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsPie;

import uy.com.innobit.rem.persistence.datamodel.dashboardemo.DummyDataGenerator;
import uy.com.innobit.rem.persistence.datamodel.dashboardemo.Movie;
import uy.com.innobit.rem.presentation.RemUI;
import uy.com.innobit.rem.presentation.view.dashboard.DashboardStats;

@SuppressWarnings("serial")
public class PropertiesPieChart extends Chart {

	public PropertiesPieChart(DashboardStats stats) {
		super(ChartType.PIE);

		setCaption("Locales");
		getConfiguration().setTitle("");
		getConfiguration().getChart().setType(ChartType.PIE);
		getConfiguration().getChart().setAnimation(false);
		setWidth("100%");
		setHeight("90%");

		DataSeries series = new DataSeries();

		List<Movie> movies = new ArrayList<Movie>(RemUI.getDataProvider().getMovies());
		DataSeriesItem item = new DataSeriesItem("Libre", stats.getFreeQ());
		series.add(item);
		item.setColor(DummyDataGenerator.chartColors[5]);

		item = new DataSeriesItem("Ocupado", stats.getFullQ());
		series.add(item);
		item.setColor(DummyDataGenerator.chartColors[1]);

		getConfiguration().setSeries(series);

		PlotOptionsPie opts = new PlotOptionsPie();
		opts.setBorderWidth(0);
		opts.setShadow(false);
		opts.setAnimation(false);
		getConfiguration().setPlotOptions(opts);

		Credits c = new Credits("");
		getConfiguration().setCredits(c);
	}

}

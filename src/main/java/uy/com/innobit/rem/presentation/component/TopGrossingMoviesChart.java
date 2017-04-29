package uy.com.innobit.rem.presentation.component;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Credits;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsBar;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.Series;

import uy.com.innobit.rem.persistence.datamodel.dashboardemo.DummyDataGenerator;

@SuppressWarnings("serial")
public class TopGrossingMoviesChart extends Chart {

	public TopGrossingMoviesChart() {
		setCaption("Cobranza Mensual");
		getConfiguration().setTitle("");
		getConfiguration().getChart().setType(ChartType.COLUMN);
		getConfiguration().getChart().setAnimation(false);
		getConfiguration().getxAxis().getLabels().setEnabled(false);
		getConfiguration().getxAxis().setTickWidth(0);
		getConfiguration().getyAxis().setTitle("");
		setSizeFull();

		List<Series> series = new ArrayList<Series>();
		for (int i = 0; i < 6; i++) {
			;
			PlotOptionsColumn opts = new PlotOptionsColumn();
			opts.setColor(DummyDataGenerator.chartColors[5 - i]);
			opts.setBorderWidth(0);
			opts.setShadow(false);
			opts.setPointPadding(0.4);
			opts.setAnimation(false);
			String name = "";
			Integer quantity = 0;
			switch (i) {
			case 0:
				name = "Junio";
				quantity = 45000;
				break;
			case 1:
				name = "Julio";
				quantity = 30000;
				break;
			case 2:
				name = "Agosto";
				quantity = 50000;
				break;
			case 3:
				name = "Setiembre";
				quantity = 45000;
				break;
			case 4:
				name = "Octubre";
				quantity = 50000;
				break;
			case 5:
				name = "Noviembre";
				quantity = 10000;
				break;
			default:
				break;
			}

			ListSeries item = new ListSeries(name, quantity);
			item.setPlotOptions(opts);
			series.add(item);

		}
		getConfiguration().setSeries(series);

		Credits c = new Credits("");
		getConfiguration().setCredits(c);

		PlotOptionsBar opts = new PlotOptionsBar();
		opts.setGroupPadding(0);
		getConfiguration().setPlotOptions(opts);

	}
}

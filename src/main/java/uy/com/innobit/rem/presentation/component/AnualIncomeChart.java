package uy.com.innobit.rem.presentation.component;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Credits;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsBar;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.Series;

import uy.com.innobit.rem.persistence.datamodel.dashboardemo.DummyDataGenerator;
import uy.com.innobit.rem.presentation.view.dashboard.DashboardStats;

@SuppressWarnings("serial")
public class AnualIncomeChart extends Chart {

	public AnualIncomeChart(DashboardStats stats) {
		setCaption("Cobranza Mensual");
		getConfiguration().setTitle("");
		getConfiguration().getChart().setType(ChartType.LINE);
		getConfiguration().getChart().setAnimation(false);
		getConfiguration().getxAxis().getLabels().setEnabled(false);
		getConfiguration().getxAxis().setTickWidth(0);
		getConfiguration().getyAxis().setTitle("");
		setSizeFull();

		List<Series> series = new ArrayList<Series>();
		DataSeries ls = new DataSeries();
		for (int i = 0; i < 12; i++) {

			PlotOptionsColumn opts = new PlotOptionsColumn();
			opts.setColor(DummyDataGenerator.chartColors[11 - i]);
			opts.setBorderWidth(0);
			opts.setShadow(false);
			opts.setPointPadding(0.4);
			opts.setAnimation(false);
			String name = "";
			Integer quantity = 0;
			switch (i) {
			case 0:
				name = "1";
				quantity = stats.getIncomeM1().intValue();
				break;
			case 1:
				name = "2";
				quantity = stats.getIncomeM2().intValue();
				break;
			case 2:
				name = "3";
				quantity = stats.getIncomeM3().intValue();
				break;
			case 3:
				name = "4";
				quantity = stats.getIncomeM4().intValue();
				break;
			case 4:
				name = "5";
				quantity = stats.getIncomeM5().intValue();
				break;
			case 5:
				name = "6";
				quantity = stats.getIncomeM6().intValue();
				break;
			case 6:
				name = "7";
				quantity = stats.getIncomeM7().intValue();
				break;
			case 7:
				name = "8";
				quantity = stats.getIncomeM8().intValue();
				break;
			case 8:
				name = "9";
				quantity = stats.getIncomeM9().intValue();
				break;
			case 9:
				name = "10";
				quantity = stats.getIncomeM10().intValue();
				break;
			case 10:
				name = "11";
				quantity = stats.getIncomeM11().intValue();
				break;
			case 11:
				name = "12";
				quantity = stats.getIncomeM12().intValue();
				break;
			default:
				break;
			}

			DataSeriesItem dsi = new DataSeriesItem(name, quantity);
			ls.add(dsi);
		}
		getConfiguration().addSeries(ls);
		getConfiguration().getyAxis().setMin(0);
		Credits c = new Credits("");
		getConfiguration().setCredits(c);

		PlotOptionsBar opts = new PlotOptionsBar();
		opts.setGroupPadding(0);
		getConfiguration().setPlotOptions(opts);

	}
}

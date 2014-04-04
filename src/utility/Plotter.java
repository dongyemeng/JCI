package utility;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jci.ContextVector;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;

public class Plotter {

	public Plotter() {
		// TODO Auto-generated constructor stub
	}

	public void makePlot(String name, List<Number> xData,
			List<Number> yData, String title, String xTitle, String yTitle,
			String seriesName, boolean isFixedRadio, boolean isSave) {	
		// Create Chart
		Chart chart = new ChartBuilder().width(800).height(600).title(title).xAxisTitle(xTitle).yAxisTitle(yTitle).build();
		 
		// Customize Chart
		chart.getStyleManager().setChartTitleVisible(true);
		
		// Series
		chart.addSeries(seriesName, xData, yData);
		chart.getStyleManager().setLegendPosition(LegendPosition.InsideNE);

		
		new SwingWrapper(chart).displayChart();
		
		if (isSave) {
			try {
				BitmapEncoder.savePNG(chart, name);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
//	
//	public void makeHistgram(String name, List<Double> data, String title, String xTitle, String yTitle,
//			String seriesName, boolean isFixedRadio, boolean isSave) {
//		
//	}
	
	public void makeQQPlot(String name, List<Double> data, String title, String xTitle, String yTitle,
			String seriesName, boolean isFixedRadio, boolean isSave) {
		List<Number> xData = new ArrayList<Number>();
		List<Number> yData = new ArrayList<Number>();

		double sum = 0.0; 
		for (int i = 0; i < data.size(); i++) {
			sum += data.get(i);
			xData.add(i+1);
			yData.add(sum);
		}
		
		// Create Chart
		Chart chart = new ChartBuilder().width(800).height(600).title(title)
				.xAxisTitle(xTitle).yAxisTitle(yTitle).build();
		 
		// Customize Chart
		chart.getStyleManager().setChartTitleVisible(true);
		
		// Series
		chart.addSeries(seriesName, xData, yData);
		chart.getStyleManager().setLegendPosition(LegendPosition.InsideNE);
		
		new SwingWrapper(chart).displayChart();
		
		if (isSave) {
			try {
				BitmapEncoder.savePNG(chart, name);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	
	public void makeScatter(String name, List<Number> xData,
			List<Number> yData, String title, String xTitle, String yTitle,
			String seriesName, boolean isFixedRadio, boolean isSave) {
		// Chart chart = new Chart(800, 600);
		Chart chart = new ChartBuilder().chartType(ChartType.Scatter)
				.width(900).height(900).title(title)
				.xAxisTitle(xTitle).yAxisTitle(yTitle).build();
		chart.getStyleManager().setChartType(ChartType.Scatter);

		// Customize Chart
		chart.getStyleManager().setChartTitleVisible(true);

		// Series
		chart.addSeries(seriesName, xData, yData);
		
		chart.getStyleManager().setLegendPosition(LegendPosition.InsideNE);
		
		if (isFixedRadio) {
			int xMax = 0;
			for (Number n : xData) {
				xMax = Math.max(xMax, n.intValue());
			}

			int yMax = 0;
			for (Number n : yData) {
				yMax = Math.max(yMax, n.intValue());
			}
			chart.getStyleManager().setXAxisMin(0);
			chart.getStyleManager().setXAxisMax(Math.max(xMax, yMax) * 1.1);
			chart.getStyleManager().setYAxisMin(0);
			chart.getStyleManager().setYAxisMax(Math.max(xMax, yMax) * 1.1);
		}
		
		
		new SwingWrapper(chart).displayChart();

		if (isSave) {
			try {
				BitmapEncoder.savePNG(chart, name);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void makeChart(String name1, ContextVector cv1, String name2,
			ContextVector cv2, String dir) {
		String fileName = name1 + "_" + name2 + ".png";

		// Create Chart
		Chart chart1 = new ChartBuilder().chartType(ChartType.Bar).width(2048)
				.height(600).title("Score Histogram").xAxisTitle("Score")
				.yAxisTitle("Number").build();
		// chart1.addSeries("test 1", new double[] { 0, 1, 2, 3, 4 }, new
		// double[] { 4, 5, 9, 6, 5 });

		// prepare data

		List<String> x = new ArrayList<String>();
		List<Number> y1 = new ArrayList<Number>();
		List<Number> y2 = new ArrayList<Number>();

		SortedSet<String> keys = new TreeSet<String>(cv1.vector.keySet());
		keys.addAll(cv2.vector.keySet());
		for (String key : keys) {
			if (key.equals("control")) {
				System.out.println();
			}
			x.add(key);
			if (cv1.vector.containsKey(key)) {
				double c = cv1.vector.get(key);
				y1.add(c);
			} else {
				y1.add(0);
			}

			if (cv2.vector.containsKey(key)) {
				double c = cv2.vector.get(key);
				y2.add(c);
			} else {
				y2.add(0);
			}
			;
		}

		chart1.addCategorySeries("context vector 1", x, y1);
		chart1.addCategorySeries("context vector 2", x, y2);

		// Customize Chart
		chart1.getStyleManager().setLegendPosition(LegendPosition.InsideNW);

		// new SwingWrapper(chart1).displayChart();

		// Save it
		if (dir != null) {
			try {
				BitmapEncoder.savePNG(chart1, dir + "/" + fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	

	public static void main(String[] args) {
		Plotter myPlotter = new Plotter();
		List<Number> xData = new ArrayList<Number>();
		List<Number> yData = new ArrayList<Number>();
		for (int i = 1; i <= 10; i++) {
			xData.add(i);
			yData.add(i);
		}
		myPlotter.makeScatter("test", xData, yData, "test title", "xTitle",
				"yTitle", "test series", true, false);
	}

}

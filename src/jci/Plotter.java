package jci;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;

public class Plotter {

	public Plotter() {
		// TODO Auto-generated constructor stub
	}
	
	public void makeChart(String name1, ContextVector cv1, String name2, ContextVector cv2, String dir) {
		String fileName = name1+"_"+name2+".png";
		
		
		// Create Chart
				 Chart chart1 = new ChartBuilder().chartType(ChartType.Bar).width(2048).height(600).title("Score Histogram").xAxisTitle("Score").yAxisTitle("Number").build();
//				 chart1.addSeries("test 1", new double[] { 0, 1, 2, 3, 4 }, new double[] { 4, 5, 9, 6, 5 });
				 
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
							int c = cv1.vector.get(key);
							y1.add(c);
						}
						else {
							y1.add(0);
						}
					   
						if (cv2.vector.containsKey(key)) {
							int c = cv2.vector.get(key);
							y2.add(c);
						}
						else {
							y2.add(0);
						};
					}

				 

				 chart1.addCategorySeries("context vector 1", x, y1);
				 chart1.addCategorySeries("context vector 2", x, y2);
				    
				 // Customize Chart
				 chart1.getStyleManager().setLegendPosition(LegendPosition.InsideNW);
				 
//				 new SwingWrapper(chart1).displayChart();
				 
				 // Save it
				 if (dir != null) {
				    try {
						BitmapEncoder.savePNG(chart1, dir+"/"+fileName);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 

	}
	
	public static void main(String[] args) {
		
	}

}

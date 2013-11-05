import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.SortedSet;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;

import jci.Article;
import jci.CRAFT;
import jci.ContextVector;
import jci.Plotter;
import jci.Term;
import jci.TermIDName;


public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		double[] xData = new double[] { 0.0, 1.0, 2.0 };
	    double[] yData = new double[] { 2.0, 1.0, 0.0 };
	 
	    // Create Chart
	    Chart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
	 
	    // Show it
	    new SwingWrapper(chart).displayChart();
	 
	    // Save it
	    try {
			BitmapEncoder.savePNG(chart, "./Sample_Chart.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Create Chart
		 Chart chart1 = new ChartBuilder().chartType(ChartType.Bar).width(800).height(600).title("Score Histogram").xAxisTitle("Score").yAxisTitle("Number").build();
//		 chart1.addSeries("test 1", new double[] { 0, 1, 2, 3, 4 }, new double[] { 4, 5, 9, 6, 5 });
		 List<String> x = new ArrayList<String>(Arrays.asList("a b".split(" ")));
		 List<Number> y1 = new ArrayList<Number>();
		 y1.add(0.5);
		 y1.add(0.1);
		 List<Number> y2 = new ArrayList<Number>();
		 y2.add(0.55);
		 y2.add(0.7);
		 chart1.addCategorySeries("test 2", x, y1);
		 chart1.addCategorySeries("test 23", x, y2);
		    // Save it
		    try {
				BitmapEncoder.savePNG(chart1, "chart/Sample_Chart.png");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 // Customize Chart
		 chart1.getStyleManager().setLegendPosition(LegendPosition.InsideNW);
		 
		 new SwingWrapper(chart1).displayChart();
		*/
		String dir = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0";
		CRAFT myCRAFT = new CRAFT(dir);
		List<String> ids = myCRAFT.getArticleIDs();
		int windowSize = 10;
		Map<TermIDName, ContextVector> termAndContextVector = new HashMap<TermIDName, ContextVector>();
		for (String id : ids) {
			Article myArticle = myCRAFT.getArticle(id);
			myArticle.process(windowSize/2);
			termAndContextVectorAddition(termAndContextVector,
					myArticle.getTermAndContextVector());
		}
		
		SortedSet<TermIDName> terms = new TreeSet<TermIDName>(termAndContextVector.keySet());
		for (TermIDName term : terms) { 
			   ContextVector value = termAndContextVector.get(term);
			   System.out.println(term.toString());
			   System.out.println(value.toString());
			}
		
		Iterator<Entry<TermIDName, ContextVector>> iter = termAndContextVector.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<TermIDName, ContextVector> m = iter.next();
			TermIDName t = m.getKey();
			ContextVector cv = m.getValue();
			String ID = t.getID();
			Term term = myCRAFT.app.id2term.get(ID);
			if (term.is_a.size() > 0) {
				String name1 = null;
				String name2 = null;
				ContextVector cv1 = null;
				ContextVector cv2 = null;
				Plotter myPlot = new Plotter();
				for (String parent : term.is_a) {
					// for (int i = 0; i < term.is_a.size(); i++) {
					// String parent = term.is_a.
					// System.out.println("[Parent Child Pair]");
					// System.out.println("\t"+term.getID()+ " IS_A " +parent);
					if (parent.equals("go:0016020"))
						System.out.println();
					boolean a = termAndContextVector
							.containsKey(new TermIDName("go:0016020", ""));
					if (termAndContextVector.containsKey(new TermIDName(parent,
							""))) {
						System.out.println("[Parent Child Pair]");
						System.out.println("\t" + term.getID() + " IS_A "
								+ parent);
						System.out.println("\tParent:");
						System.out.println("\t" + parent);
						System.out.println(termAndContextVector.get(
								new TermIDName(parent, "")).toString());
						System.out.println("\tChild:");
						System.out.println("\t" + term.getID());
						System.out.println(termAndContextVector.get(
								new TermIDName(term.getID(), "")).toString());

						name2 = term.getID();
						name2 = name2.replaceAll(":", "_");
						name1 = parent;
						name1 = name1.replaceAll(":", "_");

						if (name1.equals("go_0000785") && name2.equals("go_0000790")) {
							System.out.println();
						}
						cv1 = termAndContextVector.get(new TermIDName(parent,
								""));
						cv2 = termAndContextVector.get(new TermIDName(term
								.getID(), ""));
						
						myPlot.makeChart(name1, cv1, name2, cv2, "chart");
					}

				}

				
				
			}
		}
		
		
		
		

	}
	
	public static void termAndContextVectorAddition(Map<TermIDName, ContextVector> t, Map<TermIDName, ContextVector> s) {
		Iterator<Entry<TermIDName, ContextVector>> iter = s.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<TermIDName, ContextVector> e = iter.next();
			TermIDName key = e.getKey();
			ContextVector value = e.getValue();
			
			if (t.containsKey(key)) {
				ContextVector oldValue = t.get(key);
				oldValue.add(value);
				t.put(key, oldValue);
			}
			else {
				t.put(key, value);
			}
		}
	}

}

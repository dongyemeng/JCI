import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.SortedSet;

import utility.Biostar45366;
import utility.Plotter;

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
import jci.Term;
import jci.TermIDName;

public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		boolean task1 = true;
		boolean task1_scatter = true;
		boolean task2 = false;
		boolean task3 = false;
		
		String ontologyName = "CHEBI";
		ontologyName = "CL";
		ontologyName = "GO_BPMF";
//		ontologyName = "NCBITaxon";
		ontologyName = "PR";
		ontologyName = "SO";

		if (task1) {
			String dir = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0";
			CRAFT myCRAFT = new CRAFT(dir, ontologyName);
			List<String> ids = myCRAFT.getArticleIDs();
			int windowSize = 10;
			Map<TermIDName, ContextVector> termAndContextVector = new HashMap<TermIDName, ContextVector>();
			for (String id : ids) {
				Article myArticle = myCRAFT.getArticle(id);
				myArticle.process(windowSize / 2);
				termAndContextVectorAddition(termAndContextVector,
						myArticle.getTermAndContextVector());
			}

			SortedSet<TermIDName> terms = new TreeSet<TermIDName>(
					termAndContextVector.keySet());
			for (TermIDName term : terms) {
				if (term.getID().equals("chebi:9754")) {
					System.out.println();
				}
				ContextVector value = termAndContextVector.get(term);
				System.out.println(term.toString());
				System.out.println(value.toString());
			}

			Iterator<Entry<TermIDName, ContextVector>> iter = termAndContextVector
					.entrySet().iterator();
			List<Number> xData = new ArrayList<Number>();
			List<Number> yData = new ArrayList<Number>();
			
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
						// System.out.println("\t"+term.getID()+ " IS_A "
						// +parent);
						if (parent.equals("go:0016020"))
							System.out.println();
						boolean a = termAndContextVector
								.containsKey(new TermIDName("go:0016020", ""));
						if (termAndContextVector.containsKey(new TermIDName(
								parent, ""))) {
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
									new TermIDName(term.getID(), ""))
									.toString());

							name2 = term.getID();
							name2 = name2.replaceAll(":", "_");
							name1 = parent;
							name1 = name1.replaceAll(":", "_");

							if (name1.equals("go_0000785")
									&& name2.equals("go_0000790")) {
								System.out.println();
							}
							
							cv1 = termAndContextVector.get(new TermIDName(
									parent, ""));
							cv2 = termAndContextVector.get(new TermIDName(term
									.getID(), ""));

//							myPlot.makeChart(name1, cv1, name2, cv2, "chart");
							
							int x = cv1.getCount();
							int y = cv2.getCount();
							xData.add(x);
							yData.add(y);
						}
					}
				}
			}
			
			if (task1_scatter) {
				Plotter myPlotter = new Plotter();
				myPlotter.makeScatter("parent_vs_child_"+ontologyName.toUpperCase()+".png", xData, yData, ontologyName.toUpperCase()+" Parent-Child Pairs", ontologyName, true);
			}
		}

		if (task2) {
			// CHEBI
			Biostar45366 appCHEBI = new Biostar45366();
			appCHEBI.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\CHEBI.obo");
			int termNumCHEBI = appCHEBI.id2term.size();
			System.out.println("[CHEBI] " + termNumCHEBI);

			// CL
			Biostar45366 appCL = new Biostar45366();
			appCL.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\CL.obo");
			int termNumCL = appCL.id2term.size();
			System.out.println("[CL] " + termNumCL);

			// GO
			Biostar45366 appGO = new Biostar45366();
			appGO.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\GO.obo");
			int termNumGO = appGO.id2term.size();
			System.out.println("[GO] " + termNumGO);

			// NCBITaxon
			Biostar45366 appNCBITaxon = new Biostar45366();
			// appNCBITaxon.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\NCBITaxon.obo");
			// int termNumNCBITaxon = appNCBITaxon.id2term.size();
			// System.out.println("[NCBITaxon] "+termNumNCBITaxon);
			int countNCBITaxon = appNCBITaxon
					.countTerm("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\NCBITaxon.obo");
			System.out.println("[NCBITaxon] " + countNCBITaxon);

			// PR
			Biostar45366 appPR = new Biostar45366();
			appPR.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\PR.obo");
			int countPR = appPR
					.countTerm("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\PR.obo");
			int termNumPR = appPR.id2term.size();
			// System.out.println("[PR] "+termNumPR);
			System.out.println("[PR] " + countPR);

			// SO
			Biostar45366 appSO = new Biostar45366();
			appSO.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\SO.obo");
			int termNumSO = appSO.id2term.size();
			System.out.println("[SO] " + termNumSO);
		}

		if (task3) {
			List<Number> xData = new ArrayList<Number>();
			List<Number> yData = new ArrayList<Number>();
			Random random = new Random();
			int size = 1000;
			for (int i = 0; i < size; i++) {
				xData.add(random.nextGaussian());
				yData.add(random.nextGaussian());
			}

			// Create Chart
			Chart chart = new Chart(800, 600);
			chart.getStyleManager().setChartType(ChartType.Scatter);

			// Customize Chart
			chart.getStyleManager().setChartTitleVisible(false);
			chart.getStyleManager().setLegendPosition(LegendPosition.InsideSW);

			// Series
			chart.addSeries("Gaussian Blob", xData, yData);
			new SwingWrapper(chart).displayChart();
		}
	}

	public static void termAndContextVectorAddition(
			Map<TermIDName, ContextVector> t, Map<TermIDName, ContextVector> s) {
		Iterator<Entry<TermIDName, ContextVector>> iter = s.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Entry<TermIDName, ContextVector> e = iter.next();
			TermIDName key = e.getKey();
			ContextVector value = e.getValue();

			if (t.containsKey(key)) {
				ContextVector oldValue = t.get(key);
				oldValue.add(value);
				t.put(key, oldValue);
			} else {
				t.put(key, value);
			}
		}
	}

}

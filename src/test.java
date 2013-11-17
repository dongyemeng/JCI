import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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
import jci.TermOccurrence;

public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		boolean task1 = false;
		boolean task1_scatter = false;
		boolean task2 = false;
		boolean task3 = false;
		boolean task4 = false;
		boolean task5 = true;
		
		String ontologyName = "CHEBI";
		ontologyName = "CL";
		ontologyName = "GO_BPMF";
//		ontologyName = "NCBITaxon";
//		ontologyName = "PR";
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
							
							double x = cv1.getCount();
							
							double y = cv2.getCount();
							
							x = Math.log(x);
							y =  Math.log(y);
							
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
		
		if (task4) {
			double[] recall = new double[] 
					{ 1.0 / 7.0, 	2.0 / 7.0, 	2.0 / 7.0, 	4.0 / 7.0, 	4.0 / 7.0, 	6.0 / 7.0, 	6.0 / 7.0,	7.0 / 7.0 };
			double[] precision = new double[] 
					{ 1.0,     		1.0,     	4.0 / 5.0, 	4.0 / 5.0, 	3.0 / 4.0, 	3.0 / 4.0, 	7.0 / 10.0, 7.0 / 10.0 };

			// Create Chart
			Chart chart = QuickChart.getChart("Precision-Recall Curve",
					"Recall", "Precision", "precision-recall curve", recall,
					precision);

			chart.getStyleManager().setXAxisMin(1.0 / 7.0);
			chart.getStyleManager().setXAxisMax(1.0);
			chart.getStyleManager().setYAxisMin(0);
			chart.getStyleManager().setYAxisMax(1.0);
			
			// Show it
			new SwingWrapper(chart).displayChart();

			// Save it
			BitmapEncoder.savePNG(chart, "./Sample_Chart.png");
		}
		
		if (task5) {
			String dir = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0";
			CRAFT myCRAFT = new CRAFT(dir, ontologyName);
			List<String> ids = myCRAFT.getArticleIDs();
			int windowSize = 10;
			Map<TermIDName, ContextVector> termAndContextVector = new HashMap<TermIDName, ContextVector>();
			List<TermOccurrence> occurrences = new LinkedList<TermOccurrence>(); 
			for (String id : ids) {
				Article myArticle = myCRAFT.getArticle(id);
				myArticle.process(windowSize / 2);
				occurrences.addAll(myArticle.occurrences);
				
			}
			Map<String, Set<String>> dict = getDict(occurrences);
			Map<String, List<TermOccurrence>> dict2 = getDict2(occurrences);
			Map<String, Set<String>> dup = getDuplicates(dict);
			Map<String, Map<String, List<TermOccurrence>>> dup2 = getDuplicates2(dict2, dup);
			
			printStat(dup2);
			
			System.out.println();
			
		}
	}
	


	private static void printStat(
			Map<String, Map<String, List<TermOccurrence>>> dup2) {
		Iterator<String> iter = dup2.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			Map<String, List<TermOccurrence>> map = dup2.get(name);
			System.out.println("[Name] "+name);
			Iterator<String> idIter = map.keySet().iterator();
			while (idIter.hasNext()) {
				String id = idIter.next();
				List<TermOccurrence> ocrs = map.get(id);
				System.out.println("\t[ID] "+id);
				System.out.println("\t[Num] "+ocrs.size());
				System.out.println();
			}
		}
		
	}

	public static Map<String, Set<String>> getDuplicates(Map<String, Set<String>> dict) {
		Map<String,Set<String>> dup = new HashMap<String, Set<String>>();
		Iterator<String> iter = dict.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			Set<String> ids = dict.get(name);
			if (ids.size() > 1) {
				dup.put(name, ids);
			}
		}
		
		return dup;
	}
	
	private static Map<String, Map<String, List<TermOccurrence>>> getDuplicates2(
			Map<String, List<TermOccurrence>> dict2,
			Map<String, Set<String>> dup) {
		HashMap<String, Map<String, List<TermOccurrence>>> res = new HashMap<String, Map<String, List<TermOccurrence>>>();
		
		Iterator<String> iter = dup.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			List<TermOccurrence> ocrs = dict2.get(name);
			Map<String, List<TermOccurrence>> map = new HashMap<String, List<TermOccurrence>>();
			for (TermOccurrence ocr : ocrs) {
				String id = ocr.id;
				if (map.containsKey(id)) {
					map.get(id).add(ocr);
				}
				else {
					List<TermOccurrence> ocrList = new ArrayList<TermOccurrence>();
					ocrList.add(ocr);
					map.put(id, ocrList);
				}
			}
			res.put(name, map);
		}
		
		return res;
	}

	public static Map<String, Set<String>> getDict(List<TermOccurrence> occurrences) {
		Map<String,Set<String>> dict = new HashMap<String, Set<String>>();
		for (TermOccurrence ocr : occurrences) {
			String id = ocr.id;
			String name = ocr.name;
			if (dict.containsKey(name)) {
				dict.get(name).add(id);
			}
			else {
				Set<String> ids = new HashSet<String>();
				ids.add(id);
				dict.put(name, ids);
			}
		}
		
		return dict;		
	}
	
	public static Map<String, List<TermOccurrence>> getDict2(List<TermOccurrence> occurrences) {
		Map<String, List<TermOccurrence>> dict = new HashMap<String, List<TermOccurrence>>();
		for (TermOccurrence ocr : occurrences) {
			String name = ocr.name;
			if (dict.containsKey(name)) {
				dict.get(name).add(ocr);
			}
			else {
				List<TermOccurrence> ocrs = new ArrayList<TermOccurrence>();
				ocrs.add(ocr);
				dict.put(name, ocrs);
			}
		}
		
		return dict;		
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

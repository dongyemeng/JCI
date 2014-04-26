import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

import org.apache.commons.lang3.StringUtils;

import utility.Biostar45366;
import utility.OpenNLPSentencesTokenizer;
import utility.Plotter;
import utility.Utility;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;

import jci.AnnotatedArticle;
import jci.CRAFT;
import jci.ContextVector;
import jci.Instance;
import jci.Term;
import jci.TermIDName;
import jci.TermOccurrence;
import jci.TextProcessor;
import jci.UnannotatedArticle;

public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// print parent-child pairs for each ontology
		boolean task1 = false;
		boolean task1_scatter = false;
		
		// compute the # of terms for each ontology
		boolean task2 = false;
		
		// test xChart
		boolean task3 = false;
		
		// INLS 509
		boolean task4 = false;
		
		// make the mutation train arff file
		boolean task5 = false;
		List<Instance> mutationIns = new LinkedList<Instance>();
		
		// make the mutation unknown arff file

		
		boolean task6 = false;
		
		
		
		// make the count vs depth plot
		boolean task8 = false;
		
		String ontologyName = "CHEBI";
		ontologyName = "CL";
		ontologyName = "GO_BPMF";
//		ontologyName = "GO_CC";
		
//		ontologyName = "NCBITaxon";
//		ontologyName = "PR";
//		ontologyName = "SO";
		
		boolean task10 = false;
		
		// Compute TermOccrsSharingName: Map<Name, Map<TermID, Occrs>>
		boolean task11 = true;

		
		
		int winSize = 10;
		String CRAFTDir = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0";
		int threshold = 10;
		String outputFile = String.format("%s_Duplicated Terms_%d.txt", ontologyName, threshold);
		
		
		if (task11) {
			String output = findAllDupOccrs(CRAFTDir, ontologyName, winSize, threshold);
			System.out.println(output);
			
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(outputFile, "UTF-8");
				writer.println(output);
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		if (task1) {
			String dir = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0";
			CRAFT myCRAFT = new CRAFT(dir, ontologyName);
			List<String> ids = myCRAFT.getArticleIDs();
			int windowSize = 10;
			
			// map maps a term to its sum context vector (adding all context vector together)
			Map<TermIDName, ContextVector> termAndContextVector = new HashMap<TermIDName, ContextVector>();			
			for (String id : ids) {
				AnnotatedArticle myArticle = myCRAFT.getArticle(id);
				myArticle.process(windowSize / 2);
				ContextVector.termAndContextVectorAddition(termAndContextVector,
						myArticle.getTermAndContextVector());
			}
			

			
//			termAndContextVector.keySet().i
			

			// print out all terms appear in the corpus
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
				System.out.println(ID);
				if (StringUtils.equals(ID, "independent_continuant")) {
					continue;
				}
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
							
							x = Math.log(x) / Math.log(2);
							y =  Math.log(y) / Math.log(2);
							
							xData.add(x);
							yData.add(y);
						}
					}
				}
			}
			
			if (task1_scatter) {
				System.out.println("[Plot]");
				Plotter myPlotter = new Plotter();
				myPlotter.makeScatter(
						"parent_vs_child_" + ontologyName.toUpperCase() + ".png", 
						xData, 
						yData,
						ontologyName.toUpperCase() + " Parent-Child Pairs",
						"log of # of parent term context words (base 2)",
						"log of # of child term context words (base 2)",
						ontologyName, 
						true,
						true);
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
			List<TermOccurrence> allOccurrences = new LinkedList<TermOccurrence>(); 
			
			List<TermOccurrence> mutationOcrs = new LinkedList<TermOccurrence>(); 
			
			// Get term so:0001059 (mutation/mutations)
			String targetID1 = "so:0001059";
			String targetID2 = "so:0000041";
			Term mutation1 = myCRAFT.app.getTermById(targetID1, false);
			Term mutation2 = myCRAFT.app.getTermById(targetID2, false);
			
			
			
			List<TermOccurrence> parentOcrs1 = new ArrayList<TermOccurrence>();
			List<TermOccurrence> childOcrs1 = new ArrayList<TermOccurrence>();
			List<TermOccurrence> neighborOcrs1 = new ArrayList<TermOccurrence>();
			
			List<TermOccurrence> parentOcrs2 = new ArrayList<TermOccurrence>();
			List<TermOccurrence> childOcrs2 = new ArrayList<TermOccurrence>();
			List<TermOccurrence> neighborOcrs2 = new ArrayList<TermOccurrence>();
			
			List<Instance> neighborIns1 = new ArrayList<Instance>();
			List<Instance> neighborIns2 = new ArrayList<Instance>();
			
			for (String id : ids) {
				AnnotatedArticle myArticle = myCRAFT.getArticle(id);
				myArticle.process(windowSize / 2);
				List<TermOccurrence> ocrs = new ArrayList<TermOccurrence>();

				int dist = 1;
				
				for (TermOccurrence ocr : myArticle.occurrences) {
					if (StringUtils.equals(ocr.name, "mutation") || StringUtils.equals(ocr.name, "mutations")) {
						int c = myArticle.getCount(ocr.cv);
						ocrs.add(ocr);
					}
					
					int d = -1;
					
					d = myCRAFT.app.isAncestor(targetID1, ocr.id, dist); 
					if (d > 0) {
						parentOcrs1.add(ocr);
					}
					
					d = myCRAFT.app.isAncestor(targetID2, ocr.id, dist);
					if (d > 0) {
						parentOcrs2.add(ocr);
					}
					
					d = myCRAFT.app.isDescendent(targetID1, ocr.id, dist);
					if (d > 0) {
						childOcrs1.add(ocr);
					}
					
					d = myCRAFT.app.isDescendent(targetID2, ocr.id, dist);
					if (d > 0) {
						childOcrs2.add(ocr);
					}
				}
				System.out.println("ID: "+id);
				System.out.println("Count: "+ocrs.size());
				mutationOcrs.addAll(ocrs);
				
				allOccurrences.addAll(myArticle.occurrences);
				
			}
			Map<String, Set<String>> nameToIDs = computeNameToIDs(allOccurrences);
			Map<String, List<TermOccurrence>> nameToOcrs = computeNameToOccrs(allOccurrences);
			Map<String, Set<String>> dupNameToIDs = getDuplicates(nameToIDs);
			Map<String, Map<String, List<TermOccurrence>>> termOccrsSharingName = computeTermOccrSharingName(nameToOcrs, dupNameToIDs);
			
			printStat(termOccrsSharingName, 0);
			
			neighborOcrs1.addAll(childOcrs1);
			neighborOcrs1.addAll(parentOcrs1);
			
			neighborOcrs2.addAll(childOcrs2);
			neighborOcrs2.addAll(parentOcrs2);
			
			mutationIns = Utility.termOccurrencesToInstances(mutationOcrs);
			

			neighborIns1 = Utility.termOccurrencesToInstances(neighborOcrs1);
			neighborIns2 = Utility.termOccurrencesToInstances(neighborOcrs2);
			
			List<String> wordList = Utility.getWordList(mutationIns);
			System.out.println();

			List<Instance> allInstances = new LinkedList<Instance>();
			String folderDir = "C:/Users/Dongye/Dropbox/2013 fall/big data/project/articles/nxml/";
			folderDir = "C:/Users/Dongye/Documents/2013 fall/big data/project/articles/nxml/";
			File folder = new File(folderDir);
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
			    if (file.isFile()) {
			        System.out.println(file.getName());
					String article_dir = folderDir+file.getName();
					UnannotatedArticle art = new UnannotatedArticle(article_dir);
					List<Instance> ins = art.extractInstances("mutation", 5);
					List<Instance> ins2 = art.extractInstances("mutations", 5);
					allInstances.addAll(ins);
					allInstances.addAll(ins2);
					System.out.println();
					
			    }
			}
			
			System.out.println(allInstances.size());
			
			LinkedList<Instance> combinedIns = new LinkedList<Instance>();
			combinedIns.addAll(mutationIns);
			combinedIns.addAll(allInstances);
			combinedIns.addAll(neighborIns1);
			combinedIns.addAll(neighborIns2);
			List<String> featureList = Utility.getWordList(combinedIns);
			
			Utility.instancesToARFFOneClass(neighborIns1, "c1", featureList, "data/neighbors1_train.arff");
			Utility.instancesToARFFOneClass(neighborIns2, "c2", featureList, "data/neighbors2_train.arff");
			
			Utility.instancesToARFF(mutationIns, featureList, "data/mutation_train.arff");
			Utility.instancesToARFF(allInstances, featureList, "data/mutation_unknown.arff");
		}
		
		if (task6) {
			String dir = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0";
			CRAFT myCRAFT = new CRAFT(dir, ontologyName);
			List<String> ids = myCRAFT.getArticleIDs();
			int windowSize = 10;
			Map<TermIDName, ContextVector> termAndContextVector = new HashMap<TermIDName, ContextVector>();
			for (String id : ids) {
				AnnotatedArticle myArticle = myCRAFT.getArticle(id);
				String processedGeniaXMLTerm = myArticle.getProcessedGeniaXMLTerm();
				OpenNLPSentencesTokenizer sDetector = myArticle.getSentenceDetector();
				Set<String> idList = new HashSet<String>();
				idList.add("so:0001059");
				idList.add("so:0000041");
				List<String> sentences = sDetector.tokenize(processedGeniaXMLTerm);
				for (String sentence : sentences) {
					String res = TextProcessor.searchUnannotatedTerm(
							"mutation", idList, sentence);
					if (res != null) {
						char c3 = sentence.charAt(3);
						int i3 = (int) c3;
						char c4 = sentence.charAt(4);
						int i4 = (int) c4;
						char c5 = sentence.charAt(5);
						int i5 = (int) c5;
						char c6 = sentence.charAt(6);
						int i6 = (int) c6;
						String[] sents = res.split("\n\n");
						System.out.println();

					}
				}
			}
		}
		

		
		if (task8) {
			String dir = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0";
			CRAFT myCRAFT = new CRAFT(dir, ontologyName);
			List<String> ids = myCRAFT.getArticleIDs();
			int windowSize = 10;
			
			// compute term counts
			Map<Term, Integer> termCounts = new HashMap<Term, Integer>();
			

			
			for (String id : ids) {
				AnnotatedArticle myArticle = myCRAFT.getArticle(id);
				myArticle.process(windowSize / 2);

				for (TermOccurrence ocr : myArticle.occurrences) {
					if (ocr.id.equals("independent_continuant")) {
						continue;
					}
					Term t = myCRAFT.app.id2term.get(ocr.id);
					if (t==null) {
						System.out.println();
					}
					if (termCounts.containsKey(t)) {
						int c = termCounts.get(t);
						c++;
						termCounts.put(t, c);
					}
					else {
						termCounts.put(t, 1);
					}
					
					
				}

				
			}
			
			Iterator<Term> iter = termCounts.keySet().iterator();
			List<Number> xData = new ArrayList<Number>();
			List<Number> yData = new ArrayList<Number>();
			
			// 
			Map<Integer, List<Integer>> depthToCount = new HashMap<Integer, List<Integer>>();
			Map<Integer, Double> depthToAverageCount = new HashMap<Integer, Double>();
			
			while (iter.hasNext()) {
				Term key = iter.next();
				int count = termCounts.get(key);
				int depth = key.depth(myCRAFT.app.id2term);
				System.out.println("Term: "+key.id);
				System.out.println("\tDepth: "+depth);
				System.out.println("\tCount: "+count);
				System.out.println();
				xData.add(depth);
				yData.add(count);
				
				if (depthToCount.containsKey(depth)) {
					depthToCount.get(depth).add(count);
				}
				else {
					List<Integer> counts = new ArrayList<Integer>();
					counts.add(count);
					depthToCount.put(depth, counts);
				}
			}
			
			Plotter myPlotter = new Plotter();
			myPlotter.makeScatter(
					"depth vs occurrence_" + ontologyName.toUpperCase() + ".png", 
					xData, 
					yData,
					ontologyName.toUpperCase() + " depth vs. # of term occurrence",
					"depth",
					"# of occurrence",
					ontologyName, 
					false,
					true);	
			
			Iterator<Integer> iter2 = depthToCount.keySet().iterator();					
			while (iter2.hasNext()) {
				int key = iter2.next();
				List<Integer> counts = depthToCount.get(key);
				int sum = 0;
				for (int i = 0; i < counts.size();i++) {
					sum+=counts.get(i);
				}
				
				double averageCount = ((double) sum) / ((double)counts.size());
				depthToAverageCount.put(key, averageCount);
			}
			
			List<Number> xData2 = new ArrayList<Number>();

			List<Number> yData2 = new ArrayList<Number>();
			SortedSet<Integer> depthes = new TreeSet();
			depthes.addAll(depthToAverageCount.keySet());
			for (Integer d: depthes){
				xData2.add(d);
				yData2.add(depthToAverageCount.get(d));
			}
			
			myPlotter.makePlot(
					"depth vs average occurrence_" + ontologyName.toUpperCase() + ".png", 
					xData2, 
					yData2,
					ontologyName.toUpperCase() + " depth vs. average # of term occurrence",
					"depth",
					"average # of occurrence",
					ontologyName, 
					false,
					true);	
			
			
					
		}
	}
	

	private static String findAllDupOccrs(String CRAFTDir, String ontologyName, int winSize, int threshold) {
		String dir = CRAFTDir;
		CRAFT myCRAFT = new CRAFT(dir, ontologyName);
		List<String> ids = myCRAFT.getArticleIDs();
		int windowSize = winSize;
		List<TermOccurrence> allOccurrences = new LinkedList<TermOccurrence>();

		for (String id : ids) {
			AnnotatedArticle myArticle = myCRAFT.getArticle(id);
			myArticle.process(windowSize / 2);
			allOccurrences.addAll(myArticle.occurrences);

		}
		Map<String, Set<String>> nameToIDs = computeNameToIDs(allOccurrences);
		Map<String, List<TermOccurrence>> nameToOcrs = computeNameToOccrs(allOccurrences);
		Map<String, Set<String>> dupNameToIDs = getDuplicates(nameToIDs);
		Map<String, Map<String, List<TermOccurrence>>> termOccrsSharingName = computeTermOccrSharingName(
				nameToOcrs, dupNameToIDs);

		String output = printStat(termOccrsSharingName, threshold);
		
		return output;
	}

	private static String printStat(
			Map<String, Map<String, List<TermOccurrence>>> termOccrsSharingName, int threshold) {
		StringBuilder sb = new StringBuilder();

		Iterator<String> iter = termOccrsSharingName.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			Map<String, List<TermOccurrence>> map = termOccrsSharingName
					.get(name);
			boolean isLarge = true;
			String temp = "";
			temp = "[Name] " + name + "\n";
			Iterator<String> idIter = map.keySet().iterator();
			while (idIter.hasNext()) {
				String id = idIter.next();
				List<TermOccurrence> ocrs = map.get(id);
				temp = temp + "\t[ID] " + id + "\n";
				temp = temp + "\t[Num] " + ocrs.size() + "\n\n"; 
				if (ocrs.size() < threshold) {
					isLarge = false;
				}
			}
			if (isLarge) {
				sb.append(temp);
			}
		}

		return sb.toString();
	}

	public static Map<String, Set<String>> getDuplicates(Map<String, Set<String>> nameToIDs) {
		Map<String,Set<String>> dupNametoIDs = new HashMap<String, Set<String>>();
		Iterator<String> iter = nameToIDs.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			Set<String> ids = nameToIDs.get(name);
			if (ids.size() > 1) {
				dupNametoIDs.put(name, ids);
			}
		}
		
		return dupNametoIDs;
	}
	
	/**
	 * Compute the Map<Name, Map<Term ID, Occurrences>>
	 * 
	 * @param nameToOcrs
	 * @param dupNameToIDs
	 * @return a map
	 */
	private static Map<String, Map<String, List<TermOccurrence>>> computeTermOccrSharingName(
			Map<String, List<TermOccurrence>> nameToOcrs,
			Map<String, Set<String>> dupNameToIDs) {
		HashMap<String, Map<String, List<TermOccurrence>>> res = new HashMap<String, Map<String, List<TermOccurrence>>>();
		
		Iterator<String> iter = dupNameToIDs.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			List<TermOccurrence> ocrs = nameToOcrs.get(name);
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

	/**
	 * Make a map maps any name to a set of ids where there is a occurrence of a
	 * term with that id and expressed by that name
	 * 
	 * @param occurrences
	 * @return a map
	 */
	public static Map<String, Set<String>> computeNameToIDs(List<TermOccurrence> occurrences) {
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
	
	
	/**
	 * Make a map maps any name to a list of occurrences of that name
	 * 
	 * @param occurrences
	 * @return a map
	 */
	public static Map<String, List<TermOccurrence>> computeNameToOccrs(List<TermOccurrence> occurrences) {
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



}

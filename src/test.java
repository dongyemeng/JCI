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

	public static void main(String[] args) throws IOException {
		// 1) print parent-child pairs for each ontology
		// 2) plot log of # of context words of parent term vs. log of # of context words of child term 
		boolean expOfParentChildPair = true;
		
		
		// compute the # of terms for each ontology
		boolean expOfCountNumOfTerms = false;
		
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
		ontologyName = "SO";
		
		
		// Compute TermOccrsSharingName: Map<Name, Map<TermID, Occrs>>
		boolean expOfFindingDuplicatedOccurrence = false;

		
		
		int winSize = 10;
		String CRAFTDir = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0";
		int threshold = 10;
		String outputFile = String.format("%s_Duplicated Terms_%d.txt", ontologyName, threshold);
		
		
		// Find text strings that can be expressed by more than one terms, and write them into a file
		if (expOfFindingDuplicatedOccurrence) {
			writeDuplicatedOccurrenceToFile(CRAFTDir, ontologyName, winSize, threshold, outputFile);	
		}
		
		// Find the parent-child pairs for each ontology
		// Plot the log of # of parent term context words vs. the log of # of child term context words (base 2)",
		
		if (expOfParentChildPair) {
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
			
			// print out all terms appear in the corpus
			SortedSet<TermIDName> terms = new TreeSet<TermIDName>(
					termAndContextVector.keySet());
			for (TermIDName term : terms) {
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

					for (String parent : term.is_a) {
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
			
							cv1 = termAndContextVector.get(new TermIDName(
									parent, ""));
							cv2 = termAndContextVector.get(new TermIDName(term
									.getID(), ""));

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

			System.out.println("[Plot]");
			Plotter myPlotter = new Plotter();
			myPlotter.makeScatter(
					"parent_vs_child_" + ontologyName.toUpperCase() + ".png",
					xData, yData, ontologyName.toUpperCase()
							+ " Parent-Child Pairs",
					"log of # of parent term context words (base 2)",
					"log of # of child term context words (base 2)",
					ontologyName, true, true);
		}		
		

		if (expOfCountNumOfTerms) {
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
		if (task5) {
			String dir = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0";
			CRAFT myCRAFT = new CRAFT(dir, ontologyName);
			List<String> ids = myCRAFT.getArticleIDs();
			int windowSize = 10;
			
			List<TermOccurrence> allOccurrences = new LinkedList<TermOccurrence>(); 
			
			List<TermOccurrence> mutationOcrs = new LinkedList<TermOccurrence>(); 
			
			// Get term so:0001059 (mutation/mutations)
			String targetID1 = "so:0001059";
			String targetID2 = "so:0000041";	
			
			
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
			Map<String, Set<String>> nameToIDs = Utility.computeNameToIDs(allOccurrences);
			Map<String, List<TermOccurrence>> nameToOcrs = Utility.computeNameToOccrs(allOccurrences);
			Map<String, Set<String>> dupNameToIDs = getDuplicates(nameToIDs);
			Map<String, Map<String, List<TermOccurrence>>> termOccrsSharingName = Utility.computeTermOccrSharingName(nameToOcrs, dupNameToIDs);
			
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
			Utility.instancesToARFF(mutationIns, featureList, "data/mutation_train_class1.arff", "c1", "so:0001059");
			Utility.instancesToARFF(mutationIns, featureList, "data/mutation_train_class2.arff", "c2", "so:0000041");
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
		Map<String, Set<String>> nameToIDs = Utility.computeNameToIDs(allOccurrences);
		Map<String, List<TermOccurrence>> nameToOcrs = Utility.computeNameToOccrs(allOccurrences);
		Map<String, Set<String>> dupNameToIDs = getDuplicates(nameToIDs);
		Map<String, Map<String, List<TermOccurrence>>> termOccrsSharingName = Utility.computeTermOccrSharingName(
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
	 * Find all duplicated occurrences of terms where the the number of the term
	 * appears in the corpus is above a threshold
	 * 
	 * @param CRAFTDir
	 *            directory of CRAFT corpus
	 * @param ontologyName
	 *            name of the ontology
	 * @param winSize
	 *            size of sliding window
	 * @param threshold
	 *            threshold of number of occurrences
	 * @param outputFile
	 *            the directory and name of output file
	 */
	public static void writeDuplicatedOccurrenceToFile(String CRAFTDir,
			String ontologyName, int winSize, int threshold, String outputFile) {
		String output = findAllDupOccrs(CRAFTDir, ontologyName, winSize,
				threshold);
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
}

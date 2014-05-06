import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import utility.ListUtility;
import utility.Plotter;

import jci.AnnotatedArticle;
import jci.CRAFT;
import jci.ContextVector;
import jci.Term;
import jci.TermIDName;

public class Exp2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String ontologyName = "SO";
//		ontologyName = "PR";
		// ontologyName = "NCBITaxon"; NOT work
//		ontologyName = "GO_CC";
//		ontologyName = "GO_BPMF";
//		ontologyName = "CL";
//		ontologyName = "CHEBI";

		List<String> ontologies = new ArrayList<String>();
		ontologies.add("SO");
		ontologies.add("PR");
		ontologies.add("GO_CC");
		ontologies.add("GO_BPMF");
		ontologies.add("CL");
		ontologies.add("CHEBI");
		
		for (String ontologyName : ontologies) {
		doExpForOneOntology(ontologyName);
		}
		
	}
	
	public static void doExpForOneOntology(String ontologyName) {
		String dir = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0";
		CRAFT myCRAFT = new CRAFT(dir, ontologyName);
		List<String> ids = myCRAFT.getArticleIDs();
		int windowSize = 10;
		int threshold = 50;

		Plotter myPlot = new Plotter();

		int depthStart = 1;
		int depthNum = 20;
		int depthInterval = 1;
		int[] depths = new int[depthNum];
		for (int i = 0; i < depthNum; i++) {
			depths[i] = depthStart + i * depthInterval;
		}

		List<List<Double>> cosineSimilarityScoresAll = new ArrayList<List<Double>>();
		String[] seriesNames = new String[depthNum + 1];

		System.out.println("Ontology: " + ontologyName);
		System.out.println("Threshold: " + threshold);

		// Part I:
		boolean part1 = true;

		// map maps a term to its sum context vector (adding all context vector
		// together)
		Map<TermIDName, ContextVector> termAndContextVector = new HashMap<TermIDName, ContextVector>();
		for (String id : ids) {
			AnnotatedArticle myArticle = myCRAFT.getArticle(id);
			myArticle.process(windowSize / 2);
			ContextVector.termAndContextVectorAddition(termAndContextVector,
					myArticle.getTermAndContextVector());
		}

		if (part1) {
			// put <term, context vector> into two list
			List<TermIDName> termIDs = new ArrayList<TermIDName>();
			List<ContextVector> contextVectors = new ArrayList<ContextVector>();

			Iterator<TermIDName> iter = termAndContextVector.keySet()
					.iterator();
			while (iter.hasNext()) {
				TermIDName termID = iter.next();
				ContextVector cv = termAndContextVector.get(termID);
				termIDs.add(termID);
				contextVectors.add(cv);
			}

			System.out.println("Computing averagedCosineSimilarity...");

			List<ContextVector> largeContextVectors = new ArrayList<ContextVector>();
			for (ContextVector cv : contextVectors) {
				if (isLargeContextVector(cv, threshold)) {
					largeContextVectors.add(cv);
				}
			}
			List<Double> cosineSimilarityScores = computeCosineSimilarityScoresForAllvsAll(largeContextVectors);

			Collections.sort(cosineSimilarityScores);

			// System.out.println(cosineSimilarityScores.size());

			// myPlot.makeQQPlot(
			// "QQplot of cosine similarity scores between all pairs of terms.png",
			// cosineSimilarityScores,
			// "QQplot of cosine similarity scores between all pairs of terms",
			// "Count", "cumulative scores",
			// "cosine similarity scores between all pairs of terms",
			// false, true);

			double averagedCosineSimilarity = ListUtility
					.computeAverage(cosineSimilarityScores);
			System.out.println("averagedCosineSimilarity");
			System.out.println(averagedCosineSimilarity);
			
			
			
			
			
			
			

			cosineSimilarityScoresAll.add(cosineSimilarityScores);
			seriesNames[0] = "All vs. All";
		}

		// Part II: cosine similarity between parent and child
		System.out.println("Computing parent-child cosine similarity...");

		List<List<Double>> cosineSimilarityScoresList = new ArrayList<List<Double>>();

		int i = 1;
		
		List<Number> depthNumbers = new ArrayList<Number>();
		List<Number> sizes = new ArrayList<Number>();
		List<Number> means = new ArrayList<Number>();
		List<Number> standardDevs = new ArrayList<Number>();
		
		for (int d : depths) {
			List<Double> cosineSimilarityScores = computeCosineSimilarityScoresWithDepthFixed(
					termAndContextVector, myCRAFT.app.id2term, d, threshold);
			
			DescriptiveStatistics stat = new DescriptiveStatistics(); 
			
			int size = cosineSimilarityScores.size();
			
			if (size > 0) {
				for (int j = 0; j < size; j++) {
					stat.addValue(cosineSimilarityScores.get(j));
				}
				
				sizes.add(size);

				double mean = stat.getMean();
				means.add(mean);

				double standardDev = stat.getStandardDeviation();
				standardDevs.add(standardDev);

				depthNumbers.add(d);

				cosineSimilarityScoresList.add(cosineSimilarityScores);
				seriesNames[i] = "Distance: " + d;
				i++;
			}
		}
		cosineSimilarityScoresAll.addAll(cosineSimilarityScoresList);

		// Print out averaged scores
		// for (int i = 0; i < cosineSimilarityScoresList.size(); i++) {
		// List<Double> cosineSimilarityScores =
		// cosineSimilarityScoresList.get(i);
		// Collections.sort(cosineSimilarityScores);
		// if (i==12)
		// System.out.println(i);
		// if (cosineSimilarityScores.size() > 0) {
		// myPlot.makeQQPlot(
		// String.format("QQplot_depth_%d_threshold_%d.png",
		// i + 1, threshold),
		// cosineSimilarityScores,
		// String.format(
		// "QQplot of cosine similarity scores between terms and their ancestors of distance %d (Both the term and its ancestors have at least %d context words)",
		// i + 1, threshold), "Count",
		// "cumulative scores", "cosine similarity scores", false,
		// true);
		// double averagedScore = ListUtility
		// .computeAverage(cosineSimilarityScores);
		// System.out.println(String.format(
		// "Depth: %d, averaged score: %f", i + 1, averagedScore));
		// }
		// }

		myPlot.QQPlotWithNormal(String.format("%s_Q-Q plot.png", ontologyName),
				cosineSimilarityScoresAll, String.format("%s", ontologyName),
				"Quantiles", "Cosine similarity scores",
				seriesNames, false, true);
		
//		// Plot # of pairs of terms available at a distance vs. distance
//		myPlot.makePlot(String.format(
//				"%s_# of pairs of terms vs. distance.png", ontologyName), 
//				depthNumbers, 
//				sizes,
//				String.format("# of pairs of terms vs. distance (%s)", ontologyName), 
//				"Distance",
//				"# of pairs of terms",
//				"# of pairs of terms", 
//				false, 
//				true);
//		
//		// Plot means vs. distance
//		myPlot.makePlot(String.format(
//				"%s_mean of cosine similarity scores vs. distance.png", ontologyName), 
//				depthNumbers, 
//				means,
//				String.format("Mean of cosine similarity scores vs. distance (%s)", ontologyName), 
//				"Distance",
//				"mean of cosine similarity scores",
//				"mean of cosine similarity scores", 
//				false, 
//				true);
//
//		// Plot standard deviation vs. distance
//		myPlot.makePlot(String.format(
//				"%s_standard deviation vs. distance.png", ontologyName), 
//				depthNumbers, 
//				standardDevs,
//				String.format("Standard deviation vs. distance (%s)", ontologyName), 
//				"Distance",
//				"Standard deviation",
//				"Standard deviation", 
//				false, 
//				true);
	}

	private static List<Double> computeCosineSimilarityScoresWithDepthFixed(
			Map<TermIDName, ContextVector> termAndContextVector,
			Map<String, Term> id2term, int d, int threshold) {
		List<Double> cosineSimilarityScores = new ArrayList<Double>();

		Iterator<Entry<TermIDName, ContextVector>> iter2 = termAndContextVector
				.entrySet().iterator();
		while (iter2.hasNext()) {
			Entry<TermIDName, ContextVector> m = iter2.next();
			TermIDName t = m.getKey();
			ContextVector cv = m.getValue();

			if (!isLargeContextVector(cv, threshold)) {
				continue;
			}

			String ID = t.getID();
			// System.out.println(ID);
			if (StringUtils.equals(ID, "independent_continuant")) {
				continue;
			}
			Term term = id2term.get(ID);
			List<Term> ancestors = getAncestors(term, id2term, d);

			List<ContextVector> contextVectors = new ArrayList<ContextVector>();
			if (ancestors.size() > 0) {
				ContextVector cvParent = null;
				for (Term parentTerm : ancestors) {
					String parent = parentTerm.getID();
					if (termAndContextVector.containsKey(new TermIDName(parent,
							""))) {
						cvParent = termAndContextVector.get(new TermIDName(
								parent, ""));
						if (isLargeContextVector(cvParent, threshold)) {
							contextVectors.add(cvParent);
						}
					}
				}
				List<Double> subScores = computeCosineSimilarityScoresForOnevsAll(
						cv, contextVectors, 0, contextVectors.size());
				cosineSimilarityScores.addAll(subScores);
			}
		}

		return cosineSimilarityScores;
	}

	public static List<Double> computeCosineSimilarityScoresForOnevsAll(
			ContextVector cv1, List<ContextVector> contextVectors, int start,
			int end) {
		List<Double> cosineSimilarityScores = new ArrayList<Double>();
		ContextVector normalizedCV1 = cv1.getNormalized();

		int count = 0;
		for (int j = start; j < end; j++) {
			ContextVector cv2 = contextVectors.get(j);
			ContextVector normalizedCV2 = cv2.getNormalized();

			double cosineSimilarity = ContextVector.computeCosineSimilarity(
					normalizedCV1, normalizedCV2);

			cosineSimilarityScores.add(cosineSimilarity);
			count++;
		}

		return cosineSimilarityScores;
	}

	public static List<Double> computeCosineSimilarityScoresForAllvsAll(
			List<ContextVector> contextVectors) {
		List<Double> cosineSimilarityScores = new ArrayList<Double>();

		for (int i = 0; i < contextVectors.size() - 1; i++) {
			ContextVector cv1 = contextVectors.get(i);
			List<Double> subCosineSimilarityScores = computeCosineSimilarityScoresForOnevsAll(
					cv1, contextVectors, i + 1, contextVectors.size());
			cosineSimilarityScores.addAll(subCosineSimilarityScores);
		}

		return cosineSimilarityScores;
	}

	public static List<Term> getAncestors(Term term, Map<String, Term> id2term,
			int depth) {
		List<Term> curLevel = new ArrayList<Term>();
		List<Term> nextLevel = new ArrayList<Term>();

		curLevel.add(term);
		while (depth > 0) {
			for (Term t : curLevel) {
				if (t.is_a.size() > 0) {
					for (String parent : t.is_a) {
						Term parentTerm = id2term.get(parent);
						nextLevel.add(parentTerm);
					}
				}
			}
			curLevel = nextLevel;
			nextLevel = new ArrayList<Term>();
			depth--;
		}

		return curLevel;
	}

	public static boolean isLargeContextVector(ContextVector cv, int threshold) {
		cv.updateTotal();
		return cv.total >= threshold;
	}
}

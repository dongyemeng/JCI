import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

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
		String ontologyName = "SO";
		ontologyName = "PR";
		ontologyName = "NCBITaxon";
		ontologyName = "GO_CC";
		ontologyName = "GO_BPMF";
		ontologyName = "CL";
		ontologyName = "CHEBI";
		System.out.println("Ontology: "+ ontologyName);

		String dir = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0";
		CRAFT myCRAFT = new CRAFT(dir, ontologyName);
		List<String> ids = myCRAFT.getArticleIDs();
		int windowSize = 10;

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
			double averagedCosineSimilarity = computeAveragedCosineSimilarity(contextVectors);
			System.out.println("averagedCosineSimilarity");
			System.out.println(averagedCosineSimilarity);
		}

		// Part II: cosine similarity between parent and child
		System.out.println("Computing parent-child cosine similarity...");

		int maxDepth = 15;
//		int d = 10;
		for (int d = maxDepth; d > 0; d--) {
			double parentChildCosineSimilarity = 0.0;
			int count = 0;
			
			Iterator<Entry<TermIDName, ContextVector>> iter2 = termAndContextVector
					.entrySet().iterator();
			while (iter2.hasNext()) {
				Entry<TermIDName, ContextVector> m = iter2.next();
				TermIDName t = m.getKey();
				ContextVector cv = m.getValue();
				String ID = t.getID();
				// System.out.println(ID);
				if (StringUtils.equals(ID, "independent_continuant")) {
					continue;
				}
				Term term = myCRAFT.app.id2term.get(ID);
				List<Term> ancestors = getAncestors(term, myCRAFT.app.id2term,
						d);
				if (ancestors.size() > 0) {
					ContextVector cvParent = null;
					for (Term parentTerm : ancestors) {
						String parent = parentTerm.getID();

						// if (term.is_a.size() > 0) {
						// ContextVector cvParent = null;
						//
						// for (String parent : term.is_a) {
						if (termAndContextVector.containsKey(new TermIDName(
								parent, ""))) {
							// System.out.println("[Parent Child Pair]");
							// System.out.println("\t" + term.getID() + " IS_A "
							// + parent);
							// System.out.println("\tParent:");
							// System.out.println("\t" + parent);
							// System.out.println(termAndContextVector.get(
							// new TermIDName(parent, "")).toString());
							// System.out.println("\tChild:");
							// System.out.println("\t" + term.getID());
							// System.out.println(termAndContextVector.get(
							// new TermIDName(term.getID(), ""))
							// .toString());

							cvParent = termAndContextVector.get(new TermIDName(
									parent, ""));

							ContextVector normalizedParent = cvParent
									.getNormalized();
							ContextVector normalizedChild = cv.getNormalized();

							double cosineSimilarity = ContextVector
									.computeCosineSimilarity(normalizedParent,
											normalizedChild);
							// System.out.println(String.format("\t[%d]%f",
							// count, cosineSimilarity));
							parentChildCosineSimilarity = parentChildCosineSimilarity
									+ cosineSimilarity;
							// System.out.println(String.format("\t[%d]%f, %f",
							// count, parentChildCosineSimilarity,
							// cosineSimilarity));
							count++;

						}
					}
				}

			}
			parentChildCosineSimilarity /= ((double) count);
			System.out.println(String.format(
					"[Depth: %d] parentChildCosineSimilarity", d));
			System.out.println(parentChildCosineSimilarity);
		}
	}

	public static double computeAveragedCosineSimilarity(
			List<ContextVector> contextVectors) {
		double averagedCosineSimilarity = 0;
		int size = contextVectors.size();
		int count = 0;

		ContextVector cvx = contextVectors.get(0);
		ContextVector normalizedCVx = cvx.getNormalized();
		// double cosineSimilarity =
		// ContextVector.computeCosineSimilarity(normalizedCVx, normalizedCVx);

		for (int i = 0; i < contextVectors.size() - 1; i++) {
			ContextVector cv1 = contextVectors.get(i);
			ContextVector normalizedCV1 = cv1.getNormalized();

			for (int j = i + 1; j < contextVectors.size(); j++) {
				ContextVector cv2 = contextVectors.get(j);
				ContextVector normalizedCV2 = cv2.getNormalized();

				double cosineSimilarity = ContextVector
						.computeCosineSimilarity(normalizedCV1, normalizedCV2);

//				System.out.println(String.format("\t[%d]%f", count,
//						cosineSimilarity));

				averagedCosineSimilarity += cosineSimilarity;
				count++;
			}
		}

		averagedCosineSimilarity /= ((double) count);

		return averagedCosineSimilarity;
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
}

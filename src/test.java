import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.SortedSet;

import jci.Article;
import jci.CRAFT;
import jci.ContextVector;
import jci.Term;


public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dir = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0";
		CRAFT myCRAFT = new CRAFT(dir);
		List<String> ids = myCRAFT.getArticleIDs();
		int windowSize = 10;
		Map<Term, ContextVector> termAndContextVector = new HashMap<Term, ContextVector>();
		for (String id : ids) {
			Article myArticle = myCRAFT.getArticle(id);
			myArticle.process(windowSize/2);
			termAndContextVectorAddition(termAndContextVector,
					myArticle.getTermAndContextVector());
		}
		
		SortedSet<Term> terms = new TreeSet<Term>(termAndContextVector.keySet());
		for (Term term : terms) { 
			   ContextVector value = termAndContextVector.get(term);
			   System.out.println(term.toString());
			   System.out.println(value.toString());
			}
		
		
		
		

	}
	
	public static void termAndContextVectorAddition(Map<Term, ContextVector> t, Map<Term, ContextVector> s) {
		Iterator<Entry<Term, ContextVector>> iter = s.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Term, ContextVector> e = iter.next();
			Term key = e.getKey();
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

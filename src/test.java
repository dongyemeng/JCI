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
import jci.TermIDName;


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
			for (String parent : term.is_a) {
//				System.out.println("[Parent Child Pair]");
//				System.out.println("\t"+term.getID()+ " IS_A " +parent);
				if (parent.equals("go:0016020"))
					System.out.println();
				boolean a = termAndContextVector.containsKey(new TermIDName("go:0016020", ""));
				if (termAndContextVector.containsKey(new TermIDName(parent, ""))){
				System.out.println("[Parent Child Pair]");
				System.out.println("\t"+term.getID()+ " IS_A " +parent);
				System.out.println("\tParent:");
				System.out.println("\t"+parent);
				System.out.println(termAndContextVector.get(new TermIDName(parent, "")).toString());
				System.out.println("\tChild:");
				System.out.println("\t"+term.getID());
				System.out.println(termAndContextVector.get(new TermIDName(term.getID(), "")).toString());
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

package jci;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ContextVector {
	private Map<String, Integer> vector;
	
	public ContextVector() {
		vector = new HashMap<String, Integer>();
	}
	
	public void addWord(String word, int count) {
		if (vector.containsKey(word)) {
			int oldCount = vector.get(word);
			count = oldCount + count;
			vector.put(word, count);
		}
		else {
			vector.put(word, count);
		}
	}
	
	public void addAll(Collection<String> c) {
		for (String w : c) {
			this.addWord(w, 1);
		}
	}
	
	public ContextVector add(ContextVector cv) {
		ContextVector newCV = new ContextVector();
		
		Iterator<Entry<String, Integer>> iter = this.getIterator();
		while (iter.hasNext()) {
			Entry<String, Integer> e = iter.next();
			newCV.addWord(e.getKey(), e.getValue());
		}
		
		Iterator<Entry<String, Integer>> iter2 = cv.getIterator();
		while (iter2.hasNext()) {
			Entry<String, Integer> e = iter2.next();
			newCV.addWord(e.getKey(), e.getValue());
		}
		
		return newCV;		
	}
	
	public Iterator<Entry<String, Integer>> getIterator() {
		return this.vector.entrySet().iterator();
	}
	


}

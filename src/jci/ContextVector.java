package jci;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

public class ContextVector {
	private Map<String, Integer> vector;
	
	public ContextVector() {
		vector = new HashMap<String, Integer>();
	}
	
	public ContextVector(Collection<String> c) {
		vector = new HashMap<String, Integer>();
		this.addAll(c);
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
	
	@Override
	public String toString(){
		String s="[";
		SortedSet<String> keys = new TreeSet<String>(this.vector.keySet());
		for (String key : keys) { 
		   int value = this.vector.get(key);
		   s = s + String.format("(%s, %d)", key, value);
		}
		s = s+"]\n";
		
		
		return s;
	}
	


}

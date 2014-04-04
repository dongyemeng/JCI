package jci;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

public class ContextVector {
	public Map<String, Double> vector;
	public double total;
	
	public ContextVector() {
		vector = new HashMap<String, Double>();
		this.total = 0;
	}
	
	public void updateTotal() {
		double updatedTotal = 0;
		Iterator<String> iter = this.vector.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			updatedTotal += this.vector.get(key);
		}
		this.total = updatedTotal;
	}
	
	public ContextVector(Collection<String> c) {
		vector = new HashMap<String, Double>();
		this.addAll(c);
		this.updateTotal();
	}
	
	public void addWord(String word, Double count) {
		if (vector.containsKey(word)) {
			double oldCount = vector.get(word);
			double newCount = oldCount + count;
			vector.put(word, newCount);
			this.total += count;
		}
		else {
			vector.put(word, (double) count);
			this.total += count;
		}
	}
	
	public void addAll(Collection<String> c) {
		for (String w : c) {
			this.addWord(w, (double) 1);
		}
		this.total += c.size();
	}
	
	public void add(ContextVector cv) {
		Iterator<Entry<String, Double>> iter = cv.getIterator();
		while (iter.hasNext()) {
			Entry<String, Double> e = iter.next();
			this.addWord(e.getKey(), e.getValue());
			this.total += e.getValue();
		}		
	}
	
	public Iterator<Entry<String, Double>> getIterator() {
		return this.vector.entrySet().iterator();
	}
	
	public Double getCount() {
		return this.total;
	}
	
	@Override
	public String toString(){
		String s="[";
		SortedSet<String> keys = new TreeSet<String>(this.vector.keySet());
		for (String key : keys) { 
		   double value = this.vector.get(key);
		   s = s + String.format("[\"%s\": %f],", key, value);
		}
		s = s.substring(0, s.length() - 1);
		s = s+"]\n";
		
		
		return s;
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
	
	public static double computeCosineSimilarity(ContextVector cv1, ContextVector cv2) {
		ContextVector normalizedCV1 = cv1;
		ContextVector normalizedCV2 = cv2;
		
		double score = 0;
		
		Iterator<String> iter = normalizedCV1.vector.keySet().iterator();
		while (iter.hasNext()) {
			String word = iter.next();
			double count1 = normalizedCV1.vector.get(word);
			if (normalizedCV2.vector.containsKey(word)) {
				double count2 = normalizedCV2.vector.get(word);
				score += count1 * count2;
			}
		}
		
		double mag1 = normalizedCV1.computeMagnitude();
		double mag2 = normalizedCV2.computeMagnitude();
		
		score /= mag1;
		score /= mag2;
		
		return score;
	}
	
	public ContextVector getNormalized() {
		Map<String, Double> normalizedVector = new HashMap<String, Double>();
		this.updateTotal();
		
		Iterator<String> iter = this.vector.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			double value = this.vector.get(key);
			normalizedVector.put(key, value / this.total);
		}
		
		ContextVector normalizedContextVector = new ContextVector();
		normalizedContextVector.vector = normalizedVector;
		normalizedContextVector.total = 1;
		
		return normalizedContextVector;
	}
	
	public double computeMagnitude() {
		double mag = 0;
		
		Iterator<String> iter = this.vector.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			double value = this.vector.get(key);
			mag += value * value;
		}
		
		mag = Math.sqrt(mag);
		
		return mag;
	}
}

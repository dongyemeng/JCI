package utility;

import java.util.Collection;
import java.util.Iterator;

public class ListUtility {
	public static double computeAverage(Collection<Double> c) {
		double avg = 0;
		int count = 0;
		Iterator<Double> iter = c.iterator();
		while(iter.hasNext()) {
			avg += iter.next();
			count = count + 1;
		}
		
		avg = avg / ((double) count);
		return avg;
	}
}

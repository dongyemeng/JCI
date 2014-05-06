package utility;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jci.ContextVector;
import jci.Instance;
import jci.TermOccurrence;

import org.apache.commons.lang3.StringUtils;

public class Utility {

	public Utility() {
		// TODO Auto-generated constructor stub
	}


	
	public static List<Instance> termOccurrencesToInstances(List<TermOccurrence> ocrs) {
		List<Instance> ins = new LinkedList<Instance>();
		
		for (TermOccurrence ocr : ocrs) {
			String cls = ocr.id;
			String word = ocr.name;
			ContextVector cv = new ContextVector();
			cv.add(ocr.cv);
			Instance in = new Instance(cls, word, cv);
			ins.add(in);
		}
		
		return ins;
		
	}
	
	public static List<String> getWordList(List<Instance> ins) {
		List<String> res = new LinkedList<String>();
		SortedSet<String> words = new TreeSet<String>();
		for (Instance in : ins) {
			ContextVector cv = in.cVector;
			Iterator<String> iter = cv.vector.keySet().iterator();
			while (iter.hasNext()) {
				words.add(iter.next());
			}
		}
		
		res.addAll(words);
		
		return res;
	}
	
	public static String instanceToString(List<String> featureList, Instance in) {
		String cls = "c1";
		if (StringUtils.equals(in.cls, "so:0001059")) {
			cls = "c1";
		}
		else if (StringUtils.equals(in.cls, "so:0000041")){
			cls = "c2";
		}
		else {
			cls = "unknown";
		}
		
		StringBuilder lineBuilder = new StringBuilder();
		for (String feature : featureList) {
			
			if (in.cVector.vector.containsKey(feature)) {
				double count = in.cVector.vector.get(feature);
				if (count > 10) {
					System.out.println();
				}
				lineBuilder.append(String.format("%f, ", count));
			}
			else {
				lineBuilder.append("0, ");
			}
		}
		
		lineBuilder.append(cls);
		
		return lineBuilder.toString();
	}
	
	/**
	 * 
	 * @param ins
	 * @param featureList
	 * @param fileName
	 */
	public static void instancesToARFFOneClass(List<Instance> ins, String cls,
			List<String> featureList, String fileName) {

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName, "UTF-8");

			writer.println("");
				writer.println("@relation 'big data'");
				for (int i = 0; i < featureList.size(); i++) {
					writer.println(String.format("@attribute f%d numeric", i));
				}
				writer.println("@attribute class {c1, c2, unknown}");
				writer.println("\n");
				writer.println("@data");
				for (Instance in : ins) {
					String line = instanceToStringOneClass(featureList, in, cls);
					writer.println(line);
				}

			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String instanceToStringOneClass(List<String> featureList,
			Instance in, String cls) {		
		StringBuilder lineBuilder = new StringBuilder();
		for (String feature : featureList) {
			
			if (in.cVector.vector.containsKey(feature)) {
				double count = in.cVector.vector.get(feature);
				if (count > 10) {
					System.out.println();
				}
				lineBuilder.append(String.format("%f, ", count));
			}
			else {
				lineBuilder.append("0, ");
			}
		}
		
		lineBuilder.append(cls);
		
		return lineBuilder.toString();
	}

	public static void instancesToARFF(List<Instance> ins,
			List<String> featureList, String fileName, String cls, String target) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName, "UTF-8");

			writer.println("");
			writer.println("@relation 'big data'");
			for (int i = 0; i < featureList.size(); i++) {
				writer.println(String.format("@attribute f%d numeric", i));
			}
			writer.println("@attribute class {c1, c2, unknown}");
			writer.println("\n");
			writer.println("@data");
			for (Instance in : ins) {
				String line = instanceToString(featureList, in, cls, target);
				if (line != null) {
					writer.println(line);
				}
			}

			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String instanceToString(List<String> featureList,
			Instance in, String cls, String target) {
		if (!StringUtils.equals(in.cls, target)) {
			return null;
		}
		
		StringBuilder lineBuilder = new StringBuilder();
		for (String feature : featureList) {
			
			if (in.cVector.vector.containsKey(feature)) {
				double count = in.cVector.vector.get(feature);
				if (count > 10) {
					System.out.println();
				}
				lineBuilder.append(String.format("%f, ", count));
			}
			else {
				lineBuilder.append("0, ");
			}
		}
		
		lineBuilder.append(cls);
		
		return lineBuilder.toString();
	}



	public static void instancesToARFF(List<Instance> ins, List<String> featureList, String fileName) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName, "UTF-8");

			writer.println("");
				writer.println("@relation 'big data'");
				for (int i = 0; i < featureList.size(); i++) {
					writer.println(String.format("@attribute f%d numeric", i));
				}
				writer.println("@attribute class {c1, c2, unknown}");
				writer.println("\n");
				writer.println("@data");
				for (Instance in : ins) {
					String line = instanceToString(featureList, in);
					writer.println(line);
				}

			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Utility.instancesToARFF("data/mutation_train.arff");
	}

}

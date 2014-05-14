package utility;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	 * Compute the Map<Name, Map<Term ID, Occurrences>>
	 * 
	 * @param nameToOcrs
	 * @param dupNameToIDs
	 * @return a map
	 */
	public static Map<String, Map<String, List<TermOccurrence>>> computeTermOccrSharingName(
			Map<String, List<TermOccurrence>> nameToOcrs,
			Map<String, Set<String>> dupNameToIDs) {
		HashMap<String, Map<String, List<TermOccurrence>>> res = new HashMap<String, Map<String, List<TermOccurrence>>>();
		
		Iterator<String> iter = dupNameToIDs.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			List<TermOccurrence> ocrs = nameToOcrs.get(name);
			Map<String, List<TermOccurrence>> map = new HashMap<String, List<TermOccurrence>>();
			for (TermOccurrence ocr : ocrs) {
				String id = ocr.id;
				if (map.containsKey(id)) {
					map.get(id).add(ocr);
				}
				else {
					List<TermOccurrence> ocrList = new ArrayList<TermOccurrence>();
					ocrList.add(ocr);
					map.put(id, ocrList);
				}
			}
			res.put(name, map);
		}
		
		return res;
	}

	/**
	 * Make a map maps any name to a set of ids where there is a occurrence of a
	 * term with that id and expressed by that name
	 * 
	 * @param occurrences
	 * @return a map
	 */
	public static Map<String, Set<String>> computeNameToIDs(List<TermOccurrence> occurrences) {
		Map<String,Set<String>> dict = new HashMap<String, Set<String>>();
		for (TermOccurrence ocr : occurrences) {
			String id = ocr.id;
			String name = ocr.name;
			if (dict.containsKey(name)) {
				dict.get(name).add(id);
			}
			else {
				Set<String> ids = new HashSet<String>();
				ids.add(id);
				dict.put(name, ids);
			}
		}
		
		return dict;		
	}
	
	
	/**
	 * Make a map maps any name to a list of occurrences of that name
	 * 
	 * @param occurrences
	 * @return a map
	 */
	public static Map<String, List<TermOccurrence>> computeNameToOccrs(List<TermOccurrence> occurrences) {
		Map<String, List<TermOccurrence>> dict = new HashMap<String, List<TermOccurrence>>();
		for (TermOccurrence ocr : occurrences) {
			String name = ocr.name;
			if (dict.containsKey(name)) {
				dict.get(name).add(ocr);
			}
			else {
				List<TermOccurrence> ocrs = new ArrayList<TermOccurrence>();
				ocrs.add(ocr);
				dict.put(name, ocrs);
			}
		}
		
		return dict;		
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Utility.instancesToARFF("data/mutation_train.arff");
	}

}

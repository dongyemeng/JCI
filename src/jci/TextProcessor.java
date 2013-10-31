package jci;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.List;

import weka.core.Stopwords;

public class TextProcessor {
	

	public TextProcessor() {
		// TODO Auto-generated constructor stub
	}

	public static String xmlToText(String s) {
		String[] tags = new String[] {

		};

		s = s.replaceAll("<.*?>", " ");

		return s;
	}

	public static String mergeSpace(String s) {
		s = s.replaceAll("\\s+", " ");

		return s;
	}

	public static String trim(String s) {
		s = s.replaceAll("(^\\s+|\\s+$)", "");

		return s;
	}
	
	public static String removeStopWords(String s){
//		OboParser parser = new OboParser();
		
		Stopwords swords = new Stopwords();
		
		Enumeration swEnum = swords.elements();
		StringBuilder sb = new StringBuilder();
		while (swEnum.hasMoreElements()) {
			String word = (String)swEnum.nextElement();
			sb.append(word);
			sb.append("|");			
		}
		
		sb.insert(0, "\\b(");
		sb.setCharAt(sb.length()-1, ')');
		sb.append("\\b");
		
		String swPattern = sb.toString();
		
		s = s.replaceAll(swPattern, "");
		System.out.println(sb.toString());
		return s;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String path = "C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\articles\\nxml\\PLoS Biol-2-1-314463.nxml";
		File f = new File(path);
		StringBuilder sb = new StringBuilder();
		try {
			List<String> lines = Files.readAllLines(f.toPath(),
					Charset.forName("UTF-8"));
			// for (String line : lines) {
			for (int i = 1; i < lines.size(); i++) {
				sb.append(lines.get(i) + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String doc = sb.toString();


		String s = doc;

//		TextProcessor myPro = new TextProcessor();
		s = xmlToText(s);
		s = mergeSpace(s);
		s = trim(s);

		
		String s1 = removeStopWords(s);
		System.out.println(s);
	}

}

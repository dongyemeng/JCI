package jci;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import weka.core.Stopwords;

import org.biojava.bio.Annotation;
import org.biojava.ontology.Ontology;
import org.biojava.ontology.Term;
import org.biojava.ontology.io.OboParser;

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

		
		Stopwords swords = new Stopwords();
		swords.remove("go");
		
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
//		System.out.println(sb.toString());
		return s;
	}
	
	public static String handleDigits(String s) {
		String regex = "\\b\\d+\\b";
		s = s.replaceAll(regex, "0");
		
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
		
//		OboParser parser = new OboParser();
//
//		InputStream inStream = null;
//		try {
//			inStream = new FileInputStream("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\Go.obo");
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	 
//		BufferedReader oboFile = new BufferedReader ( new InputStreamReader ( inStream ) );
//			try {
//				Ontology ontology = parser.parseOBO(oboFile, "my Ontology name", "description of ontology");
//	 
//				Set<Term> keys = ontology.getTerms();
//				Term termx = ontology.getTerm("GO:0016301");
//				String name = termx.getName();
//				String des = termx.getDescription();
//				Annotation ann = termx.getAnnotation();
//				Object[] sym = termx.getSynonyms();
//				Iterator iter = keys.iterator();
//				while (iter.hasNext()){
//					Term term = (Term) iter.next();
//					System.out.println("TERM: " + term.getName());
//					System.out.println("Description: " + term.getDescription());
//					System.out.println(term.getAnnotation());
//					Object[] synonyms =  term.getSynonyms();
//					for ( Object syn : synonyms ) {
//						System.out.println(syn);
//					}					
//				}			
//			} catch (Exception e){
//				e.printStackTrace();
//			}
	}

}

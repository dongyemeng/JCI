package jci;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import utility.OpenNLPSentencesTokenizer;
import utility.OpenNLPTokenizer;

public class Article {

	private String text;
	private String geniaXMLTerm;
	private List<TermIDName> terms;
	public List<TermOccurrence> occurrences;
	
	private Map<TermIDName, ContextVector> termAndContextVector;
	private OpenNLPTokenizer myTokenizer;
	private OpenNLPSentencesTokenizer mySentenceDetector;
	
	
	public Article(String gTerm) {
		String openNLPSentenceDetectorDir = "res//en-sent.bin";
		String openNLPTokenizerDir = "res//en-token.bin";
		this.mySentenceDetector = new OpenNLPSentencesTokenizer(openNLPSentenceDetectorDir);
		this.myTokenizer = new OpenNLPTokenizer(openNLPTokenizerDir);
		this.geniaXMLTerm = gTerm;
		
		termAndContextVector = new HashMap<TermIDName, ContextVector>();
		occurrences = new LinkedList<TermOccurrence>();
	}
	
	public void process(int windowRadius) {
		
		this.geniaXMLTerm = this.preprocess(this.geniaXMLTerm);
//		String[] sentences = geniaXMLTerm.split("(\\. |, |\n)");
		
		geniaXMLTerm = TextProcessor.removeStopWords(geniaXMLTerm);
//		geniaXMLTerm = TextProcessor.handleDigits(geniaXMLTerm);
		geniaXMLTerm = TextProcessor.mergeSpace(geniaXMLTerm);
		geniaXMLTerm = TextProcessor.trim(geniaXMLTerm);
		
		List<String> sentences = this.mySentenceDetector.tokenize(geniaXMLTerm);
		for (String sentence : sentences) {
			if (!StringUtils.equals(sentence, "")) {
				if (StringUtils.equals(sentence, "protein belongs family <term sem=\"so:0000857\">evolutionarily conserved</term> proteins bipartite structure variable -terminal <term sem=\"so:0000856\">conserved</term> <term sem=\"so:0100015\">-terminal <term sem=\"so:0000417\">domain</term></term>.")) {
					System.out.println();
				}
				List<String> res = this.test(sentence);
				while (res != null) {
					String head = res.get(0);
					String id = res.get(1);
					String name = res.get(2);
					String tail = res.get(3);

					String rawHead = this.removeAnnotation(head);
					String rawName = this.removeAnnotation(name);
					String rawTail = this.removeAnnotation(tail);

					rawHead = TextProcessor.trim(rawHead);
					rawName = TextProcessor.trim(rawName);
					rawTail = TextProcessor.trim(rawTail);

					rawHead = TextProcessor.handleDigits(rawHead);
					rawTail = TextProcessor.handleDigits(rawTail);

					List<String> headWords = this.myTokenizer.tokenize(rawHead);
					List<String> tailWords = this.myTokenizer.tokenize(rawTail);

					List<String> contextWords = new ArrayList<String>();
					for (int i = 0; i < windowRadius; i++) {
						if (headWords.size() - i - 1 >= 0) {
							String w = headWords.get(headWords.size() - i - 1);
							if (w != null && !w.equals("")) {
								contextWords.add(w);
							}
						}
					}
					for (int i = 0; i < windowRadius; i++) {
						if (i < tailWords.size()) {
							String w = tailWords.get(i);
							if (w != null && !w.equals("")) {
								contextWords.add(w);
							}
						}
					}

					TermIDName term = new TermIDName(id, rawName);

					ContextVector contextVector = new ContextVector(
							contextWords);
					TermOccurrence ocr = new TermOccurrence(id, rawName,
							contextVector);
					if (StringUtils.equals(name,
							"-terminal <term sem=\"so:0000417\">domain")) {
						System.out.println();
					}
					this.occurrences.add(ocr);
					if (this.termAndContextVector.containsKey(term)) {
						ContextVector value = this.termAndContextVector
								.get(term);
						value.add(contextVector);
						this.termAndContextVector.put(term, value);
					} else {
						this.termAndContextVector.put(term, contextVector);
					}

					// System.out.println(contextVector);
					// System.out.println();

					sentence = head + " " + name + " " + tail;
					sentence = TextProcessor.mergeSpace(sentence);
					sentence = TextProcessor.trim(sentence);

					res = this.test(sentence);
				}
			}
		}
	}
	
	public List<String> processSentence(String sentence) {
		if (sentence != null && !StringUtils.equals(sentence, "")) {
			
			
			String regex = "^(.*)<term sem=\"(.*)\">(\\w+)<\term>.*$";
			regex = "^(.*?)<term sem=\"(.*?)\">(.*)</term>(.*)$";
			Pattern p = Pattern.compile(regex);
			String test = "word1 word2 <term sem=\"GO:0005694\"><term sem=\"GO:0005694-2\">chromosome</term></term> word3 word4";
			Matcher m = p.matcher(sentence);
//			m = p.matcher(test);
			if (m.find()) {
				String g1 = m.group(1);
				String g2 = m.group(2);
				String g3 = m.group(3);
				String g4 = m.group(4);
				
				String id = g2;
				String name = g3;
				String head  = g1;
				String tail = g4;


				List<String> res = new ArrayList<String>();
				res.add(id);
				res.add(name);
				res.add(head);
				res.add(tail);
				res.add(sentence);
				return res;
			}
		}
		else {
			return null;
		}
		return null;
		
	}
	
	public String removeAnnotation(String sentence) {
		sentence = sentence.replaceAll("<term.*?>", " ");
		sentence = sentence.replaceAll("</term.*?>", " ");
		
		return sentence;
	}
	
	
	public List<TermIDName> getTerms() {
		List<TermIDName> terms = new ArrayList<TermIDName>();
		return terms;
	}
	
	public Map<TermIDName, ContextVector> getTermAndContextVector(){
		return this.termAndContextVector;
	}
	
	public String preprocess(String s) {
		s = this.preprocess1(s);
		s = this.preprocess2(s);
		s = this.preprocess3(s);
		
		return s;
	}
	
	public String preprocess1(String s) {
		s = s.replaceAll("\\[\\d+\\]", "");
		
		return s;
	}
	
	public String preprocess2(String s) {
		s = s.toLowerCase();
		
		return s;
	}
	
	public String preprocess3(String s) {
		s = s.replaceAll("\\(", " ");
		s = s.replaceAll("\\)", " ");
		
		return s;
	}
	
	public String trimString(String s) {
		s = s.replaceAll("^\\s+", "");
		s = s.replaceAll("\\s+$", "");
		
		return s;
	}
	
	
	
	public List<String>  test(String sentence) {
		if (sentence == null) {
			return null;
		}
		
		String regex = "^(.*?)(<term sem=\"(.*?)\">)(.*)$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(sentence);

		if (m.find()) {
			String head = m.group(1);
			String mid = m.group(2);
			String id = m.group(3);
			String nameAndTail = m.group(4);
			
			List<String> nt = test2(nameAndTail);
			if (nt == null) {
				return null;
			}
			
			String name = nt.get(0);
			String tail = nt.get(1);

			List<String> res = new ArrayList<String>();
			res.add(head);
			res.add(id);
			res.add(name);
			res.add(tail);

			return res;
		}
		else {
			return null;
		}
	}
	


	private List<String> test2(String nameAndTail) {
		if (nameAndTail == null) {
			return null;
		}
		
		int level = 1;
		String processed = "";
		String unprocessed = nameAndTail;
		
		String regex = "^(.*?)(<term sem=\"(.*?)\">|</term>)(.*)$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(unprocessed);
		
		

		while (m.find()) {
			String head = m.group(1);
			String mid = m.group(2);
			if (mid.matches("^<term.*$")) {
				level++;
				processed = processed + head + mid;
				unprocessed = m.group(4);
			}
			else {
				level--;
				if (level == 0) {
					String name = processed + head;				
					String tail = m.group(4);
					if (tail == null) {
						tail = "";
					}
					List<String> nt = new ArrayList<String>();
					
					nt.add(name);
					nt.add(tail);
					
					return nt;
				}
				else {
				processed = processed + head + mid;
				unprocessed = m.group(4);
//				String g0 = m.group(0);
//				String g1 = m.group(1);
//				String g2 = m.group(2);
//				String g3 = m.group(3);
//				String g4 = m.group(4);
//				System.out.println();
				}
			}
			
			m = p.matcher(unprocessed);
		}
		
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Article myA = new Article("");
		String sent = "protein belongs family <term sem=\"so:0000857\">evolutionarily conserved</term> proteins bipartite structure variable -terminal <term sem=\"so:0000856\">conserved</term> <term sem=\"so:0100015\">-terminal <term sem=\"so:0000417\">domain</term></term>.";
		sent = "protein belongs family <term sem=\"so:0000857\">evolutionarily conserved</term> proteins bipartite structure variable -terminal <term sem=\"so:0000856\">conserved</term> <term sem=\"so:0100015\">-terminal <term sem=\"so:0000417\">domain</term></term>.";
		sent = " <term sem=\"so:0100015\">-terminal <term sem=\"so:0000417\">domain</term></term>.";

		List<String> res = myA.test(sent);
		while (res != null) {
			String head = res.get(0);
			String id = res.get(1);
			String name = res.get(2);
			String tail = res.get(3);			
			
			String rawHead = myA.removeAnnotation(head);
			String rawName = myA.removeAnnotation(name);
			String rawTail = myA.removeAnnotation(tail);
			
			rawHead = TextProcessor.trim(rawHead);
			rawTail = TextProcessor.trim(rawTail);
			rawHead = TextProcessor.handleDigits(rawHead);
			rawTail = TextProcessor.handleDigits(rawTail);
			
			List<String> headWords = myA.myTokenizer.tokenize(rawHead);
			List<String> tailWords = myA.myTokenizer.tokenize(rawTail);
			int windowRadius = 5;
			List<String> contextWords = new ArrayList<String>();
			for (int i = 0; i < windowRadius; i++) {
				if (headWords.size() - i -1 >= 0) {
					String w = headWords.get(headWords.size()-i-1);
					if (w != null && !w.equals("")) {
						contextWords.add(w);
					}
				}
			}
			for (int i = 0; i < windowRadius; i++) {
				if (i < tailWords.size()) {
					String w = tailWords.get(i);
					if (w != null && !w.equals("")) {
						contextWords.add(w);
					}
				}
			}
			
			TermIDName term = new TermIDName(id, rawName); 
			
			ContextVector contextVector = new ContextVector(contextWords);
			TermOccurrence ocr = new TermOccurrence(id, rawName, contextVector);
			
			res = myA.test(head + name + tail);
		}

//		       "^(.*?)<term sem=\"(.*?)\">(.*?)</term>(.*)$";
		       
//		       myA.test(sent);
//		List<String> res = myA.processSentence(sent);
//		String id = res.get(0);
//		String name = res.get(1);
//		String head = res.get(2);
//		String tail = res.get(3);
//		String full = res.get(4);
//		System.out.println();

	}

}

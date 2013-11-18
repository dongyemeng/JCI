package jci;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import utility.OpenNLPSentencesTokenizer;
import utility.OpenNLPTokenizer;

public class AnnotatedArticle {

	private String text;
	private String rawGeniaXMLTerm;
	private String processedGeniaXMLTerm;
	private List<TermIDName> terms;
	public List<TermOccurrence> occurrences;
	
	public Map<TermIDName, ContextVector> termAndContextVector;
	private OpenNLPTokenizer myTokenizer;
	private OpenNLPSentencesTokenizer mySentenceDetector;
	
	public String getProcessedGeniaXMLTerm(){
		return this.processedGeniaXMLTerm;
	}
	
	public OpenNLPSentencesTokenizer getSentenceDetector() {
		return this.mySentenceDetector;
	}
	
	
	public AnnotatedArticle(String gTerm) {
		String openNLPSentenceDetectorDir = "res//en-sent.bin";
		String openNLPTokenizerDir = "res//en-token.bin";
		this.mySentenceDetector = new OpenNLPSentencesTokenizer(openNLPSentenceDetectorDir);
		this.myTokenizer = new OpenNLPTokenizer(openNLPTokenizerDir);
		this.rawGeniaXMLTerm = gTerm;
		
		this.processedGeniaXMLTerm = this.preprocess(this.rawGeniaXMLTerm);
//		String[] sentences = geniaXMLTerm.split("(\\. |, |\n)");
		
		this.processedGeniaXMLTerm = TextProcessor.removeStopWords(this.processedGeniaXMLTerm);
//		geniaXMLTerm = TextProcessor.handleDigits(geniaXMLTerm);
		this.processedGeniaXMLTerm = TextProcessor.mergeSpace(this.processedGeniaXMLTerm);
		this.processedGeniaXMLTerm = TextProcessor.trim(this.processedGeniaXMLTerm);
		
		termAndContextVector = new HashMap<TermIDName, ContextVector>();
		occurrences = new LinkedList<TermOccurrence>();
	}

	
	public void process(int windowRadius) {
	
		List<String> sentences = this.mySentenceDetector.tokenize(this.processedGeniaXMLTerm);
		for (String sentence : sentences) {
			if (!StringUtils.equals(sentence, "")) {
				if (StringUtils.equals(sentence, "protein belongs family <term sem=\"so:0000857\">evolutionarily conserved</term> proteins bipartite structure variable -terminal <term sem=\"so:0000856\">conserved</term> <term sem=\"so:0100015\">-terminal <term sem=\"so:0000417\">domain</term></term>.")) {
					System.out.println();
				}
				List<String> res = TextProcessor.extractAnnotation(sentence);
				while (res != null) {
					String head = res.get(0);
					String id = res.get(1);
					String name = res.get(2);
					String tail = res.get(3);

					String rawHead = this.removeAnnotation(head);
					String rawName = this.removeAnnotation(name);
					String rawTail = this.removeAnnotation(tail);
					
					if (StringUtils.equals(rawName, "Mutation")) {
						System.out.println();
					}

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

					id = id.toLowerCase();
					TermIDName term = new TermIDName(id, rawName);

					ContextVector contextVector = new ContextVector(
							contextWords);
					
					List<String> contextWordsCopy = new ArrayList<String>();
					contextWordsCopy.addAll(contextWords);
					TermOccurrence ocr = new TermOccurrence(id, rawName,
							new ContextVector(contextWordsCopy));
					if (StringUtils.equals(name,
							"-terminal <term sem=\"so:0000417\">domain")) {
						System.out.println();
					}
					if (StringUtils.equals(name,
							"mutation")) {
						if (this.getCount(contextVector) >10){
						System.out.println();
						}
					}
					if (StringUtils.equals(name,
							"mutations")) {
						if (this.getCount(contextVector) >10){
							System.out.println();
						}
					}
					if (this.getCount(contextVector) >10){
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

					res = TextProcessor.extractAnnotation(sentence);
				}
			}
		}
	}
	
	public int getCount(ContextVector cv) {
		int c = 0;
		Iterator<String> iter = cv.vector.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			c += cv.vector.get(key);
		}
		
		return c;
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


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AnnotatedArticle myA = new AnnotatedArticle("");
		String sent = "protein belongs family <term sem=\"so:0000857\">evolutionarily conserved</term> proteins bipartite structure variable -terminal <term sem=\"so:0000856\">conserved</term> <term sem=\"so:0100015\">-terminal <term sem=\"so:0000417\">domain</term></term>.";
		sent = "protein belongs family <term sem=\"so:0000857\">evolutionarily conserved</term> proteins bipartite structure variable -terminal <term sem=\"so:0000856\">conserved</term> <term sem=\"so:0100015\">-terminal <term sem=\"so:0000417\">domain</term></term>.";
		sent = " <term sem=\"so:0100015\">-terminal <term sem=\"so:0000417\">domain</term></term>.";

		List<String> res = TextProcessor.extractAnnotation(sent);
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
			
			res = TextProcessor.extractAnnotation(head + name + tail);
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

package jci;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class Article {

	private String text;
	private String geniaXMLTerm;
	private List<Term> terms;
	
	private Map<Term, ContextVector> termAndContextVector = new HashMap<Term, ContextVector>();
	
	public Article(String gTerm) {
		this.geniaXMLTerm = gTerm;
	}
	
	public void process(int windowRadius) {
		
		this.geniaXMLTerm = this.preprocess(this.geniaXMLTerm);
		String[] sentences = geniaXMLTerm.split("(\\. |, |\n)");
		for (String sentence : sentences) {
			List<String> res = processSentence(sentence);
			while (res != null) {
				String id = res.get(0);
				String name = res.get(1);
				String head = res.get(2);
				String tail = res.get(3);
				String full = res.get(4);
				
				
				String rawHead = this.removeAnnotation(head);
				String rawTail = this.removeAnnotation(tail);
				String[] headWords = rawHead.split("\\s+");
				String[] tailWords = rawTail.split("\\s+");
				
				List<String> contextWords = new ArrayList<String>();
				for (int i = 0; i < windowRadius; i++) {
					if (headWords.length-i-1>=0) {
						String w = headWords[headWords.length-i-1];
						if (w != null && !w.equals("")) {
							contextWords.add(w);
						}
					}
				}
				for (int i = 0; i < windowRadius; i++) {
					if (i < tailWords.length) {
						String w = tailWords[i];
						if (w != null && !w.equals("")) {
							contextWords.add(w);
						}
					}
				}
//				System.out.println(full);
//				System.out.println(id);
//				System.out.println(name);
//				System.out.println(head);
//				System.out.println(tail);
//				System.out.println(contextWords);
				
				Term term = new Term(id, name); 
				ContextVector contextVector = new ContextVector(contextWords);
				if (this.termAndContextVector.containsKey(term)) {
					ContextVector value = this.termAndContextVector.get(term);
					value.add(contextVector);
					this.termAndContextVector.put(term, value);
				}
				else {
					this.termAndContextVector.put(term, contextVector);
				}
				
//				System.out.println(contextVector);
//				System.out.println();
				
				res = this.processSentence(tail);
			}
		}
	}
	
	public List<String> processSentence(String sentence) {
		if (sentence != null && !StringUtils.equals(sentence, "")) {
			String regex = "^(.*)<term sem=\"(.*)\">(\\w+)<\term>.*$";
			regex = "^(.*?)<term sem=\"(.*?)\">(.*?)</term>(.*)$";
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
		sentence = sentence.replaceAll("<term.*?>", "");
		sentence = sentence.replaceAll("</term.*?>", "");
		
		return sentence;
	}
	
	
	public List<Term> getTerms() {
		List<Term> terms = new ArrayList<Term>();
		return terms;
	}
	
	public Map<Term, ContextVector> getTermAndContextVector(){
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
		s = s.replaceAll("\\(", ".");
		s = s.replaceAll("\\)", ".");
		
		return s;
	}

}

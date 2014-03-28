package jci;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import utility.OpenNLPSentencesTokenizer;
import utility.OpenNLPTokenizer;

public class UnannotatedArticle {
	public String rawText;
	public String plainText;
	public String processedText;
	
	
	private OpenNLPTokenizer myTokenizer;
	private OpenNLPSentencesTokenizer mySentenceDetector;
	
	public UnannotatedArticle(String dir) {
		String openNLPSentenceDetectorDir = "res//en-sent.bin";
		String openNLPTokenizerDir = "res//en-token.bin";
		this.mySentenceDetector = new OpenNLPSentencesTokenizer(openNLPSentenceDetectorDir);
		this.myTokenizer = new OpenNLPTokenizer(openNLPTokenizerDir);
		
		File file = new File(dir);
		try {
			List<String> lines = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
//			for (String line : lines) {
			StringBuilder textBuilder = new StringBuilder();
			for (int i = 1; i < lines.size(); i++) {
				textBuilder.append("\n");
				textBuilder.append(lines.get(i));
			}
			this.rawText = textBuilder.toString();
			
			this.plainText = TextProcessor.xmlToText(this.rawText);
			
			this.processedText = this.plainText.toLowerCase();
			this.processedText = TextProcessor.removeStopWords(processedText);
			this.processedText = TextProcessor.mergeSpace(processedText);
			this.processedText = TextProcessor.trim(processedText);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public List<Instance> extractInstances(String word, int windowRadius) {
		List<Instance> ins = new ArrayList<Instance>();
		
		List<String> sentences = this.mySentenceDetector.tokenize(this.processedText);
		for (String sentence : sentences) {
			List<String> words = this.myTokenizer.tokenize(sentence);
			for (int i = 0; i < words.size(); i++) {
				if (StringUtils.equals(words.get(i), word)) {
					ContextVector cv = new ContextVector();
					
					for (int j = 1; j <= windowRadius; j++) {
						if (i - j >= 0) {
							String w = words.get(i-j);
							cv.addWord(w, (double) 1);
						}
						
						if (i + j < words.size()) {
							String w = words.get(i+j);
							cv.addWord(w, (double) 1);
						}
					}

					Instance in = new Instance("unknown", word, cv);
					ins.add(in);					
				}
			}
		}
		return ins;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

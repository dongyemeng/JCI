package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

public class OpenNLPSentencesTokenizer implements CTokenizer{
	private SentenceDetectorME mySentenceDetector;
	
	public  OpenNLPSentencesTokenizer (String openNLPSentenceDetectorDir) {
		InputStream sentModelIn;

		try {
			sentModelIn = new FileInputStream(openNLPSentenceDetectorDir);
			SentenceModel model = new SentenceModel(sentModelIn);
			this.mySentenceDetector = new SentenceDetectorME(model);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public List<String> tokenize(String text) {
		List<String> sentences = new LinkedList<String>();
		String[] segments = text.split("\n\n");
		for (String seg : segments) {
			String[] sentenceArray = this.mySentenceDetector.sentDetect(seg);

			for (String sentence : sentenceArray) {
				sentences.add(sentence);
			}
		}

		return sentences;
	}

}

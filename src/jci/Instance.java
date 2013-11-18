package jci;

import utility.OpenNLPSentencesTokenizer;
import utility.OpenNLPTokenizer;

public class Instance {
	public String cls;
	public String word;
	public ContextVector cVector;

	
	public Instance(String c, String w, ContextVector cv) {
		
		this.cls = c;
		this.word = w;
		this.cVector = cv;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

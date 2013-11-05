package jci;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

public class OpenNLPTokenizer implements CTokenizer {
	private TokenizerME myTokenizer;

	public OpenNLPTokenizer(String OpenNLPTokenizerDir) {
		// Get OpenNLP tokenizer
				InputStream tokenModelIn;
				try {
					tokenModelIn = new FileInputStream(OpenNLPTokenizerDir);
					TokenizerModel model = new TokenizerModel(tokenModelIn);
					this.myTokenizer = new TokenizerME(model);
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
		// TODO Auto-generated method stub
		String[] tempTokens = this.myTokenizer.tokenize(text);
		
		List<String> tokens = new LinkedList<String>();
		for (int i=0;i<tempTokens.length;i++){
			String token = new String(tempTokens[i]);
			tokens.add(token);
		}
		
		return tokens;
	}

}

package utility;

import java.util.List;

public interface CTokenizer {

	/**
	 * @param text
	 * @return List of Tokens representation of the text
	 */
	public List<String> tokenize(String text);

}

import java.util.List;

import jci.Article;
import jci.CRAFT;


public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dir = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0";
		CRAFT myCRAFT = new CRAFT(dir);
		List<String> ids = myCRAFT.getArticleIDs();
		for (String id : ids) {
			Article myArticle = myCRAFT.getArticle(id);
			myArticle.process(3);
		}
		
		

	}

}

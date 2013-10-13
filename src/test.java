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
		String id = "11532192";
		Article myArticle = myCRAFT.getArticle(id);
		myArticle.process();

	}

}

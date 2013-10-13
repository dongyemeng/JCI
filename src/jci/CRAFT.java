package jci;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class CRAFT {
	private String dir;

	public CRAFT(String d) {
		this.dir = d;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public Article getArticle(String id) {
		String path = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0/README.txt";
		
		path = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0/genia-xml/term/go_cc/11532192.txt";
		path = this.dir+"/genia-xml/term/go_cc/"+id+".txt.xml";
		//File geniaXMLTermFile = new File(this.dir+"/genia-xml/term/go_cc/"+id+".txt.xml");
		File geniaXMLTermFile = new File(path);
		String geniaXMLTerm = "";
		try {
			List<String> lines = Files.readAllLines(geniaXMLTermFile.toPath(), Charset.forName("UTF-8"));
			for (String line : lines) {
				geniaXMLTerm = geniaXMLTerm + "\n" + line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.out.println(geniaXMLTerm);
		Article myArticle = new Article(geniaXMLTerm);
		return myArticle;
	}

}

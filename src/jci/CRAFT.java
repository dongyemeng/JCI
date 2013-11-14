package jci;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import utility.Biostar45366;

public class CRAFT {
	private String dir;
	private String ontologyName;
	public Biostar45366 app;

	public CRAFT(String d, String oName) {
		this.dir = d;
		this.ontologyName = oName;
		app = new Biostar45366();
		try {
//			app.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\Go.obo");
//			app.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\CHEBI.obo");
			String oNameTemp = ""+this.ontologyName;
			if (StringUtils.equals(this.ontologyName.toLowerCase(), "go_bpmf") || StringUtils.equals(this.ontologyName.toLowerCase(), "go_cc")) {
				oNameTemp = "GO";
			}
			String fullPath = "C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\"
					+ oNameTemp + ".obo";
			app.parse(fullPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public List<String> getArticleIDs() {
		List<String> ids = new ArrayList<String>();
		String pmidsDir = this.dir + "/articles/ids/craft-pmids-release";
		File pmidsFile = new File(pmidsDir);
		
		try {
			List<String> lines = Files.readAllLines(pmidsFile.toPath(), Charset.forName("UTF-8"));
			for (String line : lines) {
				ids.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ids;
	}
	
	public Article getArticle(String id) {
		String path = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0/README.txt";
		
		path = "C:/Users/Dongye/Dropbox/Phenoscape/CRAFT corpus/craft-1.0/genia-xml/term/go_cc/11532192.txt";
//		path = this.dir+"/genia-xml/term/go_cc/"+id+".txt.xml";
//		path = this.dir+"/genia-xml/term/go_bpmf/"+id+".txt.xml";
		
//		path = this.dir+"/genia-xml/term/chebi/"+id+".txt.xml";
		
		path = this.dir+"/genia-xml/term/"+this.ontologyName.toLowerCase()+"/"+id+".txt.xml";
		//File geniaXMLTermFile = new File(this.dir+"/genia-xml/term/go_cc/"+id+".txt.xml");
		File geniaXMLTermFile = new File(path);
		String geniaXMLTerm = "";
		try {
			List<String> lines = Files.readAllLines(geniaXMLTermFile.toPath(), Charset.forName("UTF-8"));
//			for (String line : lines) {
			for (int i = 1; i < lines.size(); i++) {
				geniaXMLTerm = geniaXMLTerm + "\n" + lines.get(i);
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

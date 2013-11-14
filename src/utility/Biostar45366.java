package utility;

import java.io.*;
import java.util.*;

import jci.Term;

// Source URL: https://gist.github.com/lindenb/2762967

public class Biostar45366 {
	private BufferedReader in;
	private String buffer;
	public Map<String, Term> id2term = new HashMap<String, Term>();



	private Set<String> getAllDescendantById(String id) {
		Set<String> set = new HashSet<String>();
		set.add(id);
		Term t = id2term.get(id);
		for (String c : t.children) {
			set.addAll(getAllDescendantById(c));
		}
		return set;
	}

	private Term getTermById(String id, boolean create) {
		Term t = this.id2term.get(id);
		if (t == null && create) {
			t = new Term();
			t.id = id;
			t.name = id;
			t.def = id;
			this.id2term.put(id, t);
		}
		return t;
	}

	private static String nocomment(String s) {
		int excl = s.indexOf('!');
		if (excl != -1)
			s = s.substring(0, excl);
		return s.trim();
	}

	private String next() throws IOException {
		if (buffer != null) {
			String s = buffer;
			buffer = null;
			return s;
		}
		return in.readLine();
	}

	private void parseTerm() throws IOException {
		Term t = null;
		String line;
		while ((line = next()) != null) {
			if (line.startsWith("[")) {
				this.buffer = line;
				break;
			}
			int colon = line.indexOf(':');
			if (colon == -1)
				continue;
			if (line.startsWith("id:") && t == null) {
				t = getTermById(line.substring(colon + 1).trim().toLowerCase(), true);
				continue;
			}
			if (t == null)
				continue;
			if (line.startsWith("name:")) {
				t.name = nocomment(line.substring(colon + 1));
				continue;
			} else if (line.startsWith("def:")) {
				t.def = nocomment(line.substring(colon + 1));
				continue;
			} else if (line.startsWith("is_a:")) {
				String rel = nocomment(line.substring(colon + 1));
				rel = rel.toLowerCase();
				t.is_a.add(rel);
				Term parent = getTermById(rel, true);
				parent.children.add(t.id);
				continue;
			}
		}
	}

	public void parse(String dir) throws IOException {
		InputStream inputStream = new FileInputStream(dir);
		in = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while ((line = next()) != null) {
			if (line.equals("[Term]"))
				parseTerm();
		}
		in.close();
	}
	
	public int countTerm(String dir) throws IOException {
		int count = 0;
		InputStream inputStream = new FileInputStream(dir);
		in = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while ((line = next()) != null) {
			if (line.equals("[Term]"))
				count++;
		}
		in.close();
		
		return count;
	}

	public static void main(String args[]) throws IOException {
		boolean task1 = false;
		
		// count the number of onology terms
		boolean task2 = true;
		
		
		if (task1) {
		Biostar45366 app = new Biostar45366();
		app.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\Go.obo");
		int level = 1;
		boolean found = true;
		int count = 1;
		while (found) {
			found = false;
			for (Term t : app.id2term.values()) {
				if (t.depth(app.id2term) == level) {
					System.out.println("Count: "+count+ ", Level: " + level + "\t" + t);
					count++;
					found = true;
				}
			}
			level++;
		}
//		for (String id : app.getAllDescendantById("GO:0001783")) {
//			System.out.println(app.id2term.get(id));
//		}
		}
		
		if (task2) {
			// CHEBI
			Biostar45366 appCHEBI = new Biostar45366();
			appCHEBI.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\CHEBI.obo");
			int termNumCHEBI = appCHEBI.id2term.size();
			System.out.println("[CHEBI] "+termNumCHEBI);
			
			// CL
			Biostar45366 appCL = new Biostar45366();
			appCL.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\CL.obo");
			int termNumCL = appCL.id2term.size();
			System.out.println("[CL] "+termNumCL);
			
			// GO
			Biostar45366 appGO = new Biostar45366();
			appGO.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\GO.obo");
			int termNumGO = appGO.id2term.size();
			System.out.println("[GO] "+termNumGO);
			
			// NCBITaxon
			Biostar45366 appNCBITaxon = new Biostar45366();
//			appNCBITaxon.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\NCBITaxon.obo");
//			int termNumNCBITaxon = appNCBITaxon.id2term.size();
//			System.out.println("[NCBITaxon] "+termNumNCBITaxon);
			int countNCBITaxon = appNCBITaxon.countTerm("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\NCBITaxon.obo");
			System.out.println("[NCBITaxon] "+countNCBITaxon);
			
			// PR
			Biostar45366 appPR = new Biostar45366();
			appPR.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\PR.obo");
			int countPR = appPR.countTerm("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\PR.obo");
			int termNumPR = appPR.id2term.size();
//			System.out.println("[PR] "+termNumPR);
			System.out.println("[PR] "+countPR);
			
			// SO
			Biostar45366 appSO = new Biostar45366();
			appSO.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\SO.obo");
			int termNumSO = appSO.id2term.size();
			System.out.println("[SO] "+termNumSO);
		}
		
		
	}
}
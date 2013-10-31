package jci;

import java.io.*;
import java.util.*;

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

	public static void main(String args[]) throws IOException {
		Biostar45366 app = new Biostar45366();
		app.parse("C:\\Users\\Dongye\\Dropbox\\Phenoscape\\CRAFT corpus\\craft-1.0\\ontologies\\Go.obo");
		int level = 1;
		boolean found = true;
		while (found) {
			found = false;
			for (Term t : app.id2term.values()) {
				if (t.depth(app.id2term) == level) {
					System.out.println("" + level + "\t" + t);
					found = true;
				}
			}
			level++;
		}
		for (String id : app.getAllDescendantById("GO:0001783")) {
			System.out.println(app.id2term.get(id));
		}
	}
}
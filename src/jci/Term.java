package jci;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Term {
		String id;
		String name;
		String def;
		public Set<String> children = new HashSet<String>();
		public Set<String> is_a = new HashSet<String>();
		
		public String getID() {
			return this.id;
		}

		int depth(Map<String, Term> id2term) {
			int min_child = 0;
			for (String p : is_a) {
				Term parent = id2term.get(p);
				if (parent == null) {
					System.err.println("Cannot get " + p);
					continue;
				}
				int n2 = parent.depth(id2term);
				if (min_child == 0 || n2 < min_child)
					min_child = n2;
			}
			return 1 + min_child;
		}

		public String toString() {
			return id + "\t" + name + "\t" + is_a;
		}
	}
package jci;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Term {
	public String id;
	public String name;
	public String def;
	public Set<String> children = new HashSet<String>();
	public Set<String> is_a = new HashSet<String>();

	public String getID() {
		return this.id;
	}
	


	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this.getClass() != obj.getClass()) {
			return false;
		}

		if (this.id.equals(((Term) obj).id)) {
			return true;
		} else {
			return false;
		}
	}

	public int depth(Map<String, Term> id2term) {
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
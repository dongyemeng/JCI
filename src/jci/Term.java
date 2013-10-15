package jci;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Term {
	private String ID;
	private String name;
	public Term(String id, String n) {
		this.ID = id;
		this.name = n;
	}

	
	@Override
	public boolean equals(Object obj){
		if (obj==this){
			return true;
		}
		
		if (obj==null||obj.getClass()!=this.getClass()){
			return false;
		}
		
		Term myTerm = (Term) obj;
		
		return StringUtils.equals(ID, myTerm.ID);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(13, 37)
		.append(this.ID)
		.toHashCode();
	}
	

}

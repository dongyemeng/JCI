package jci;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class TermIDName implements Comparable<TermIDName>{
	private String ID;
	private String name;
	public TermIDName(String id, String n) {
		this.ID = id;
		this.name = n;
	}

	
	public String getID(){
		return this.ID;
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj==this){
			return true;
		}
		
		if (obj==null||obj.getClass()!=this.getClass()){
			return false;
		}
		
		TermIDName myTerm = (TermIDName) obj;
		
		return StringUtils.equals(ID, myTerm.ID);
	}
	
	@Override
	public int hashCode() {
//		int hashCode = new HashCodeBuilder(13, 37)
//		.append(this.ID)
//		.toHashCode(); 
//		
//		return hashCode;
		int hashCode = this.ID.hashCode();
		return hashCode;
	}
	
	@Override
	public String toString() {
		String termString = "\n"
				+ "Term:\n"
				+ "\tID: " + this.ID + "\n"
				+ "\tName: " + this.name + "\n" 
				+ "\n";

		return termString;
	}


	@Override
	public int compareTo(TermIDName t) {
		return this.ID.compareTo(t.ID);
	}
	

}

package com.threesat.nineteenth;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;



public class Literal implements Comparable<Literal> {
	
	@Override
	public int compareTo(Literal literal) {
	     return new CompareToBuilder()
	       .append(getVar(), literal.getVar())
	       .append(isNegate(), literal.isNegate())
	       .toComparison();
	}
	
	private String var;
	private Boolean negate;

	public Literal(String var, Boolean negate) {
		this.var = var;
		this.negate = negate;
	}
	
	public Literal getOpposite() {
		return new Literal(var,!negate);
	}
	
	public boolean isOpposite(Literal l) {
		return l.getVar().equals(var) && l.isNegate()!=negate;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public Boolean isNegate() {
		return negate;
	}

	public void setNegate(Boolean negate) {
		this.negate = negate;
	}
	
	@Override
	public boolean equals(final Object obj){
	    if(obj instanceof Literal){
	        final Literal other = (Literal) obj;
	        return new EqualsBuilder()
	            .append(var, other.getVar())
	            .append(negate, other.isNegate())
	            .isEquals();
	    } else{
	        return false;
	    }
	}
	
	@Override
	public int hashCode(){
	    return new HashCodeBuilder()
	        .append(var)
	        .append(negate)
	        .toHashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(var);
		sb.append(negate?"-":"");
		return sb.toString();
	}

}

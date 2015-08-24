package com.threesat.eight;



public class Literal {
	
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
	public boolean equals(Object o) {
		if(o instanceof Literal) {
			Literal l = (Literal)o;
			return l.getVar().equals(var) && l.isNegate()==negate;
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(var);
		sb.append(negate?"'":"");
		return sb.toString();
	}

}

package com.threesat.fourth;

import java.util.ArrayList;
import java.util.List;




public class Clause {
	
	
	private String first = null;
	private boolean firstNegate = false;
	
	private String second = null;
	private boolean secondNegate = false;
	
	private String third = null;
	private boolean thirdNegate = false;
	
	
	public Clause(String first, boolean firstNegate, 
			String second, boolean secondNegate, 
			String third, boolean thirdNegate) {
		super();
		this.first = first;
		this.firstNegate = firstNegate;
		this.second = second;
		this.secondNegate = secondNegate;
		this.third = third;
		this.thirdNegate = thirdNegate;
	}
	
	public Clause(String[] clause) {
		super();
		clause = sanitizeArray(clause);
		this.first = clause[0].trim();
		this.firstNegate = first.startsWith("-");
		this.first = clause[0].replace("-","");
		this.second = clause[1].trim();
		this.secondNegate = second.startsWith("-");
		this.second = clause[1].replace("-","");
		this.third = clause[2].trim();
		this.thirdNegate = third.startsWith("-");
		this.third = clause[2].replace("-","");
	}
	
	private String[] sanitizeArray(String[] a) {
		List<String> l = new ArrayList<>();
		for(String s : a) {
			if(!s.trim().isEmpty()) {
				l.add(s);
			}
		}
		return l.toArray(new String[]{});
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public boolean isFirstNegate() {
		return firstNegate;
	}

	public void setFirstNegate(boolean firstNegate) {
		this.firstNegate = firstNegate;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public boolean isSecondNegate() {
		return secondNegate;
	}

	public void setSecondNegate(boolean secondNegate) {
		this.secondNegate = secondNegate;
	}

	public String getThird() {
		return third;
	}

	public void setThird(String third) {
		this.third = third;
	}

	public boolean isThirdNegate() {
		return thirdNegate;
	}

	public void setThirdNegate(boolean thirdNegate) {
		this.thirdNegate = thirdNegate;
	}
	
	public String toString() {
		return "(" + first.replace("-", "") + (firstNegate?"'":"") + " ^ "
				+ second.replace("-", "") + (secondNegate?"'":"") + " ^ "
				+ third.replace("-", "") + (thirdNegate?"'":"") + ")";
	}

}

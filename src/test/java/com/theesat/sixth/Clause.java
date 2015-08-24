package com.theesat.sixth;

import java.util.ArrayList;
import java.util.List;




public class Clause {
	
	
	private String first = null;
	private boolean firstNegate = false;
	
	private String second = null;
	private boolean secondNegate = false;
	
	private String third = null;
	private boolean thirdNegate = false;
	
	public Clause() {}
	
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
	
	public boolean isZero() {
		return !isFirstNegate() && !isSecondNegate() && !isThirdNegate();
	}
	
	public int clauseMatchLength(Clause c) {
		if(!c.getFirst().equals(first) && !c.getSecond().equals(second) && !c.getThird().equals(third)) {
			return 0;
		} else {
			if(c.equals(this)) {
				//case A B C - A B C
				return 0;
			} else {
				//case A B C - A' B C
				if(c.getFirst().equals(first) && c.getSecond().equals(second) && c.getThird().equals(third)) {
					if(c.isFirstNegate()==isFirstNegate()) {						
						if(c.isSecondNegate()==isSecondNegate()) {
							if(c.isThirdNegate()!=isThirdNegate()) {
								return 3;
							} else {
								return 0;
							}
						} else {
							if(c.isThirdNegate()==isThirdNegate()) {
								return 3;
							} else {
								return 0;
							}
						}
					} else {
						if(c.isSecondNegate()==isSecondNegate() && c.isThirdNegate()==isThirdNegate()) {
							return 3;
						} else {
							return 0;
						}
					}
				} else {
					//case A B C - A' B D
					if(!c.getFirst().equals(first) && c.getSecond().equals(second) && c.getThird().equals(third)) {
						if(c.isSecondNegate()==isSecondNegate()) {
							if(c.isThirdNegate()!=isThirdNegate()) {
								return 2;
							} else {
								return 0;
							}
						} else {
							if(c.isThirdNegate()==isThirdNegate()) {
								return 2;
							} else {
								return 0;
							}
						}
					} else if(c.getFirst().equals(first) && !c.getSecond().equals(second) && c.getThird().equals(third)) {
						if(c.isFirstNegate()==isFirstNegate()) {
							if(c.isThirdNegate()!=isThirdNegate()) {
								return 2;
							} else {
								return 0;
							}
						} else {
							if(c.isThirdNegate()==isThirdNegate()) {
								return 2;
							} else {
								return 0;
							}
						}
					} if(c.getFirst().equals(first) && c.getSecond().equals(second) && !c.getThird().equals(third)) {
						if(c.isSecondNegate()==isSecondNegate()) {
							if(c.isFirstNegate()!=isFirstNegate()) {
								return 2;
							} else {
								return 0;
							}
						} else {
							if(c.isFirstNegate()!=isFirstNegate()) {
								return 2;
							} else {
								return 0;
							}
						}
					} else {
						//case A B C - C' B D
						if(c.getFirst().equals(first) && !c.getSecond().equals(second) && !c.getThird().equals(third)) {
							if(c.isFirstNegate()!=isFirstNegate()) {
								return 1;
							} else {
								return 0;
							}
						} else if(!c.getFirst().equals(first) && c.getSecond().equals(second) && !c.getThird().equals(third)) {
							if(c.isSecondNegate()!=isSecondNegate()) {
								return 1;
							} else {
								return 0;
							}
						} else if(c.getFirst().equals(first) && !c.getSecond().equals(second) && !c.getThird().equals(third)) {
							if(c.isThirdNegate()!=isThirdNegate()) {
								return 1;
							} else {
								return 0;
							}
						} else {
							return 0;
						}
					}
				}
			}
		}
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
	
	public String getFirstLiteral() {
		return first.concat(isFirstNegate()?"'":"");
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
	
	public String getSecondLiteral() {
		return second.concat(isSecondNegate()?"'":"");
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
	
	public String getThirdLiteral() {
		return third.concat(isThirdNegate()?"'":"");
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

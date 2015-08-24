package com.threesat.ninth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;




public class Clause extends ArrayList<Literal> {
	
	private Clause() {}
	
	public Clause(List<String> ls) {
		for(String l : ls) {
			l = l.trim();
			boolean negate = l.startsWith("-") || l.endsWith("-") || l.startsWith("'") || l.endsWith("'");
			l = l.replace("-","").replace("'",""); 
			add(new Literal(l,negate));
		}
	}
	
	public String getFirst() {
		return get(0).getVar();
	}
	
	public String getSecond() {
		return get(1).getVar();
	}
	
	public String getThird() {
		return get(2).getVar();
	}
	
	public boolean isFirstNegate() {
		return get(0).isNegate();
	}
	
	public boolean isSecondNegate() {
		return get(1).isNegate();
	}
	
	public boolean isThirdNegate() {
		return get(2).isNegate();
	}

	public Clause(String... literals) {
		this(Arrays.asList(literals));
	}
	
	public List<String> getVariables() {
		List<String> aux = new ArrayList<>();
		for(Literal l : this) {
			if(!aux.contains(l.getVar()))
				aux.add(l.getVar());
		}
		return aux;
	}
	
	public List<Literal> getNegateLiterals() {
		List<Literal> aux = new ArrayList<>();
		for(Literal l : this) {
			if(l.isNegate())
				aux.add(l);
		}
		return aux;
	}
	
	public boolean isZero() {
		for(Literal l : this) {
			if(l.isNegate()) {
				return false;
			}
		}
		return true;
	}
	
	public Clause simplify(Clause cright) {
		List<Literal> opposite = new ArrayList<>();
		for(Literal lr : cright) {
			for(Literal ll : this) {
				if(ll.isOpposite(lr)) {
					opposite.add(ll);
				}
			}
		}
		if(opposite.size()==1) {
			Set<Literal> uniqueLiterals = new LinkedHashSet<>();
			uniqueLiterals.addAll(this);
			uniqueLiterals.addAll(cright);
			uniqueLiterals.remove(opposite.get(0));
			uniqueLiterals.remove(opposite.get(0).getOpposite());
			Clause r = new Clause();
			r.addAll(uniqueLiterals);
			//System.out.println(this + " + " + cright + " ---> " + r + " " + opposite);
			return r;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		if(size()>0) {
			StringBuilder s = new StringBuilder();
			s.append("(");
			for(Literal l : this) {
				s.append(l);
				s.append(" ");
			}
			return s.replace(s.length()-1, s.length(),")").toString();
		} else {
			return null;
		}
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Clause) {
			Clause c = (Clause)o;
			if(c.size()!=size()) {
				return false;
			} else {
				for(int i=0;i<c.size();i++) {
					c.get(i).equals(i);
				}
			}
		}
		return false;
	}
	
	public int clauseMatchLength(Clause c) {
		if(!c.getFirst().equals(getFirst()) && !c.getSecond().equals(getSecond()) && !c.getThird().equals(getThird())) {
			return 0;
		} else {
			if(c.equals(this)) {
				//case A B C - A B C
				return 0;
			} else {
				//case A B C - A' B C
				if(c.getFirst().equals(getFirst()) && c.getSecond().equals(getSecond()) && c.getThird().equals(getThird())) {
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
					if(!c.getFirst().equals(getFirst()) && c.getSecond().equals(getSecond()) && c.getThird().equals(getThird())) {
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
					} else if(c.getFirst().equals(getFirst()) && !c.getSecond().equals(getSecond()) && c.getThird().equals(getThird())) {
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
					} if(c.getFirst().equals(getFirst()) && c.getSecond().equals(getSecond()) && !c.getThird().equals(getThird())) {
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
						if(c.getFirst().equals(getFirst()) && !c.getSecond().equals(getSecond()) && !c.getThird().equals(getThird())) {
							if(c.isFirstNegate()!=isFirstNegate()) {
								return 1;
							} else {
								return 0;
							}
						} else if(!c.getFirst().equals(getFirst()) && c.getSecond().equals(getSecond()) && !c.getThird().equals(getThird())) {
							if(c.isSecondNegate()!=isSecondNegate()) {
								return 1;
							} else {
								return 0;
							}
						} else if(c.getFirst().equals(getFirst()) && !c.getSecond().equals(getSecond()) && !c.getThird().equals(getThird())) {
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
	
}

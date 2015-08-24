package com.threesat.seventh;

import java.util.ArrayList;
import java.util.List;




public class Clause {
	
	
	private String first = "";
	private boolean firstNegate = false;
	
	private String second = "";
	private boolean secondNegate = false;
	
	private String third = "";
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
	
	public int length() {
		if(second.length()>0) {
			if(third.length()>0) {
				return 3;
			} else {
				return 2;
			}
		}
		return 1;
	}
	
	public List<String> literals;
	
	public Clause(List<String> literals) {
		if(literals.size()==1) {
			this.first = literals.get(0).trim();
			this.firstNegate = first.startsWith("-") || first.endsWith("-");
			this.first = literals.get(0).trim().replace("-",""); 
		} else if(literals.size()==2) {
			this.first = literals.get(0).trim();
			this.firstNegate = first.startsWith("-") || first.endsWith("-");
			this.first = literals.get(0).trim().replace("-",""); 
			
			this.second = literals.get(1).trim();
			this.secondNegate = second.startsWith("-") || second.endsWith("-");;
			this.second = literals.get(1).trim().replace("-",""); 
		} else if(literals.size()==3) {
			this.first = literals.get(0).trim();
			this.firstNegate = first.startsWith("-") || first.endsWith("-");
			this.first = literals.get(0).trim().replace("-",""); 
			
			this.second = literals.get(1).trim();
			this.secondNegate = second.startsWith("-") || second.endsWith("-");;
			this.second = literals.get(1).trim().replace("-",""); 
			
			this.third = literals.get(2).trim();
			this.thirdNegate = third.startsWith("-") || third.endsWith("-");;
			this.third = literals.get(2).trim().replace("-",""); 
		}
	}
	
	public Clause(String a, String b, String c) {
		super();
		
		this.first = a.trim();
		this.firstNegate = first.startsWith("-") || first.endsWith("-");
		this.first = a.replace("-","");
		
		this.second = b.trim();
		this.secondNegate = second.startsWith("-") || second.endsWith("-");;
		this.second = b.replace("-","");
		
		this.third = c.trim();
		this.thirdNegate = third.startsWith("-") || third.endsWith("-");;
		this.third = c.replace("-","");	
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
	
	public String clauseMatchVariable(Clause c) {
		if(!c.getFirst().equals(first) && !c.getSecond().equals(second) && !c.getThird().equals(third)) {
			return null;
		} else {
			if(c.equals(this)) {
				//case A B C - A B C
				return null;
			} else {
				//case A B C - A' B C
				if(c.getFirst().equals(first) && c.getSecond().equals(second) && c.getThird().equals(third)) {
					if(c.isFirstNegate()==isFirstNegate()) {						
						if(c.isSecondNegate()==isSecondNegate()) {
							if(c.isThirdNegate()!=isThirdNegate()) {
								return c.getThird();
							} else {
								return null;
							}
						} else {
							if(c.isThirdNegate()==isThirdNegate()) {
								return c.getSecond();
							} else {
								return null;
							}
						}
					} else {
						if(c.isSecondNegate()==isSecondNegate() && c.isThirdNegate()==isThirdNegate()) {
							return c.getFirst();
						} else {
							return null;
						}
					}
				} else {
					//case A B C - A' B D
					if(!c.getFirst().equals(first) && c.getSecond().equals(second) && c.getThird().equals(third)) {
						if(c.isSecondNegate()==isSecondNegate()) {
							if(c.isThirdNegate()!=isThirdNegate()) {
								return c.getThird();
							} else {
								return null;
							}
						} else {
							if(c.isThirdNegate()==isThirdNegate()) {
								return c.getSecond();
							} else {
								return null;
							}
						}
					} else if(c.getFirst().equals(first) && !c.getSecond().equals(second) && c.getThird().equals(third)) {
						if(c.isFirstNegate()==isFirstNegate()) {
							if(c.isThirdNegate()!=isThirdNegate()) {
								return c.getThird();
							} else {
								return null;
							}
						} else {
							if(c.isThirdNegate()==isThirdNegate()) {
								return c.getFirst();
							} else {
								return null;
							}
						}
					} if(c.getFirst().equals(first) && c.getSecond().equals(second) && !c.getThird().equals(third)) {
						if(c.isSecondNegate()==isSecondNegate()) {
							if(c.isFirstNegate()!=isFirstNegate()) {
								return c.getFirst();
							} else {
								return null;
							}
						} else {
							if(c.isFirstNegate()!=isFirstNegate()) {
								return c.getFirst();
							} else {
								return null;
							}
						}
					} else {
						//case A B C - C' B D
						if(c.getFirst().equals(first) && !c.getSecond().equals(second) && !c.getThird().equals(third)) {
							if(c.isFirstNegate()!=isFirstNegate()) {
								return c.getFirst();
							} else {
								return null;
							}
						} else if(!c.getFirst().equals(first) && c.getSecond().equals(second) && !c.getThird().equals(third)) {
							if(c.isSecondNegate()!=isSecondNegate()) {
								return c.getSecond();
							} else {
								return null;
							}
						} else if(c.getFirst().equals(first) && !c.getSecond().equals(second) && !c.getThird().equals(third)) {
							if(c.isThirdNegate()!=isThirdNegate()) {
								return c.getThird();
							} else {
								return null;
							}
						} else {
							return null;
						}
					}
				}
			}
		}
	}
	
	
	public int clause2MatchLength(Clause c) {
		if(c.length()==2) {
			if((c.getFirst().equals(first)) && c.isFirstNegate()!=isFirstNegate()) {
				if(c.getSecond().equals(second) || c.getSecond().equals(third)) {
					return 2;
				} else {
					return 1;
				}
			}
			if((c.getFirst().equals(second)) && c.isFirstNegate()!=isSecondNegate()) {
				if(c.getSecond().equals(second) || c.getSecond().equals(third)) {
					return 2;
				} else {
					return 1;
				}
			}
			if((c.getFirst().equals(third)) && c.isFirstNegate()!=isThirdNegate()) {
				if(c.getSecond().equals(second) || c.getSecond().equals(third)) {
					return 2;
				} else {
					return 1;
				}
			}
			if((c.getSecond().equals(first)) && c.isSecondNegate()!=isFirstNegate()) {
				if(c.getSecond().equals(second) || c.getSecond().equals(third)) {
					return 2;
				} else {
					return 1;
				}
			}
			if((c.getSecond().equals(second)) && c.isSecondNegate()!=isSecondNegate()) {
				if(c.getSecond().equals(second) || c.getSecond().equals(third)) {
					return 2;
				} else {
					return 1;
				}
			}
			if((c.getSecond().equals(third)) && c.isSecondNegate()!=isThirdNegate()) {
				if(c.getSecond().equals(second) || c.getSecond().equals(third)) {
					return 2;
				} else {
					return 1;
				}
			}
			return 0;
		} else {
			return 0;
		}
	}
	
	public int clauseMatchLength(Clause c) {
		if(c.length()<3) {
			return clause2MatchLength(c);
		}
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
		if(third.length()==0) {
			return "(" + first.replace("-", "") + (firstNegate?"'":"") + " ^ "
					+ second.replace("-", "") + (secondNegate?"'":"") + ")";
		} else {
			return "(" + first.replace("-", "") + (firstNegate?"'":"") + " ^ "
					+ second.replace("-", "") + (secondNegate?"'":"") + " ^ "
					+ third.replace("-", "") + (thirdNegate?"'":"") + ")";
		}
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Clause) {
			Clause c = (Clause)o;
			return (c.getFirstLiteral().equals(getFirstLiteral()) 
					|| c.getFirstLiteral().equals(getSecondLiteral()) 
					|| c.getFirstLiteral().equals(getThirdLiteral())) &&
			(c.getSecondLiteral().equals(getFirstLiteral()) 
					|| c.getSecondLiteral().equals(getSecondLiteral()) 
					|| c.getSecondLiteral().equals(getThirdLiteral())) &&
			(c.getThirdLiteral().equals(getFirstLiteral()) 
					|| c.getThirdLiteral().equals(getSecondLiteral()) 
					|| c.getThirdLiteral().equals(getThirdLiteral()));
		}
		return false;
	}
}

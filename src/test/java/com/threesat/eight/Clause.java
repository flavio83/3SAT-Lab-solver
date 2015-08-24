package com.threesat.eight;

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
			Set<Literal> fooSet = new LinkedHashSet<>(cright);
			fooSet.addAll(this);
			List<Literal> uniqueLiterals = new ArrayList<>(fooSet);
			//System.out.println(uniqueLiterals);
			Literal junction = opposite.get(0);
			uniqueLiterals.remove(junction);
			uniqueLiterals.remove(junction.getOpposite());
			//System.out.println(uniqueLiterals);
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
	
}

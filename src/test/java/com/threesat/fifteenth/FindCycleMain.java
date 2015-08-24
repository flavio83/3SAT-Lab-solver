package com.threesat.fifteenth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class FindCycleMain {
	
	//static String filename = "C:\\DEV\\3sat\\UUF150.645.100\\uuf150-01.cnf";
	//static String filename = "C:\\DEV\\3sat\\UUF250.1065.100\\uuf250-037.cnf";
	//static String filename = "C:\\DEV\\3sat\\UUF250.1065.100\\uuf250-043.cnf";
	//static String filename = "C:\\DEV\\3sat\\UF250.1065.100\\uf250-037.cnf";
	//static String filename = "C:\\DEV\\3sat\\3sat1000sot\\uf20-0532.cnf";
	//static String filename = "C:\\DEV\\3sat\\UF50.218.1000\\uf50-01.cnf";
	static String filename = "C:\\DEV\\3sat\\UUF50.218.1000\\uuf50-018.cnf";
	//static String filename = "C:\\DEV\\3sat\\zfcp-2.8-u2-nh.cnf";
	//static String filename = "C:\\DEV\\3sat\\UR-20-10p1_HARD.cnf";
	//static String filename = "C:\\DEV\\3sat\\MD5-28-3.cnf";

	public FindCycleMain() throws Exception {
		DIMACSReader reader = new DIMACSReader(filename);
		List<Clause> expression = reader.getExpression();
		calculatePair(expression);
		
	}
	
	Map<Clause,List<Clause>[]> PAIRS = new HashMap<>(); 
	
	private void calculatePair(List<Clause> expression) {
		for(Clause c : expression) {
			List<Clause> aux = new ArrayList<>();
			aux.addAll(expression);
			aux.remove(c);
			PAIRS.put(c, findBestPair(c,aux));
		}
	}
	
	private List<Clause>[] findBestPair(Clause one, List<Clause> expression) {
		List<Clause>[] aux = new ArrayList[4];
		aux[1] = new ArrayList<Clause>();
		aux[2] = new ArrayList<Clause>();
		aux[3] = new ArrayList<Clause>();
		for(Clause c : expression) {
			int k = one.clauseMatchLength(c);
			if(k!=0) {
				aux[k].add(c);
			}
		}
		return aux;
	}

	public static void main(String[] args) {
		try {
			new FindCycleMain();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

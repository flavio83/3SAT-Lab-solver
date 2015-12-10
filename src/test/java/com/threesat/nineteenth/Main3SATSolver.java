package com.threesat.nineteenth;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;




public class Main3SATSolver {
	
	static long cyclecounter = 0;
	long variablespermutationCounter = 0;
	
	//long index = 0;
	static BigInteger index = new BigInteger("0");
	static String[] variables = new String[] { "a", "b", "c", "d" };
	
	static String filename = "C:\\DEV\\3sat\\UUF250.1065.100\\uuf250-098.cnf";
	//static String filename = "C:\\DEV\\3sat\\UF250.1065.100\\uf250-072.cnf";
	//static String filename = "C:\\DEV\\3sat\\3sat1000sot\\uf20-0532.cnf";
	//static String filename = "C:\\DEV\\3sat\\UF50.218.1000\\uf50-01.cnf";
	//static String filename = "C:\\DEV\\3sat\\UUF50.218.1000\\uuf50-018.cnf";
	//static String filename = "C:\\DEV\\3sat\\zfcp-2.8-u2-nh.cnf";
	//static String filename = "C:\\DEV\\3sat\\UR-20-10p1_HARD.cnf";
	//static String filename = "C:\\DEV\\3sat\\MD5-28-3.cnf";
	
	Map<Clause,Integer> removedClause = new HashMap<>();
	Map<Clause,Integer> addedClause = new HashMap<>();
	List<Clause> usedClause = new ArrayList<>();
	
	static long timePass = System.currentTimeMillis();
	
	static BigInteger prev = new BigInteger("0");
	
	static {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
		   @Override  
		   public void run() {
			   //System.out.println("---> " + (System.currentTimeMillis()-timePass)/1000 + "sec, index: " + index + " " + new StringBuilder(index.toString(2)).reverse().toString());
			   System.out.println("---> " + calculateStringProblemSpace(index) + " "+ (System.currentTimeMillis()-timePass)/1000 + "sec, cycleCounter: " + cyclecounter + " index: " + index + " indexDiff: " + index.subtract(prev) );
			   prev = new BigInteger(index.toString());
		   }
		},  5000, 5000);
		timer.cancel();
	}
	
	private static String calculateStringProblemSpace(BigInteger index) {
		BigInteger problemspace = new BigInteger("2").pow(variables.length);
		int l = problemspace.toString(2).length();
		String s = index.toString(2);
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<(l-s.length());i++) {
			sb.append("0");
		}
		sb.append(s);
		return sb.toString();
	}
	
	public Main3SATSolver(List<Clause> expression) {
		main3SATSolver(expression.toArray(new Clause[]{}));
	}
	
	int acc = 0;
	List<String> val = new ArrayList<>();
	
	private void main3SATSolver(Clause... expression) {
		main3SATSolver(new ArrayList<String>(),expression);
	}
	
	private void main3SATSolver(List<String> vars, Clause... expression) {
		System.out.println("CALLED " + vars.size());
		
		long cyclecounter = 0;
		long variablespermutationCounter = 0;
		
		index = new BigInteger("0");
		prev = new BigInteger("0");
		variables = new String[] { "a", "b", "c", "d" };
		
		removedClause = new HashMap<>();
		addedClause = new HashMap<>();
		usedClause = new ArrayList<>();

		variables = reshuffleVariables(vars,expression);
		
		System.out.println("filename: " + filename);
		System.out.println("exp lengh " + variables.length);
		System.out.println("problem space's length: " + new BigInteger("2").pow(variables.length));
		
		Generator<Boolean> binaryGenerator = Factory.createPermutationWithRepetitionGenerator(
				Factory.createVector(new Boolean[] { Boolean.TRUE, Boolean.FALSE }), variables.length);
		
		List<Clause> zeros = findZeroClauses(expression);
		for(Clause zero : zeros) {
			for(Clause c : expression) {
				Assignment2 ass = zero.getFirstAssigment(zero, c);
				if(ass!=null) {
					System.out.println(ass + " " + zero + " " + c);
				}
			}
		}
	}
	
	public List<Clause> findZeroClauses(Clause... expression) {
		List<Clause> zeros = new ArrayList<Clause>();
		for(Clause c : expression) {
			if(c.isAllZero()) {
				zeros.add(c);
			}
		}
		return zeros;
	}
	
	static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
            new Comparator<Map.Entry<K,V>>() {
                @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                    int res = -1*e1.getValue().compareTo(e2.getValue());
                    return res != 0 ? res : 1; // Special fix to preserve items with equal values
                }
            }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
	
	private String[] findVariables(Clause... expression) {
		List<String> vars = new ArrayList<>();
		for(Clause s : expression) {
			String first = s.getFirst().replace("-", "");
			String second = s.getSecond().replace("-", "");
			String third = s.getThird().replace("-", "");
			if(!vars.contains(first)) {
				vars.add(first);
			}
			if(!vars.contains(second)) {
				vars.add(second);		
			}
			if(!vars.contains(third)) {
				vars.add(third);
			}
		}
		Collections.reverse(vars);
		return vars.toArray(new String[]{});
	}
	
	private String[] reshuffleVariables(List<String> vars, Clause... expression) {
		if(vars.size()>0) {
			 List<String> aux = new ArrayList<>();
			 aux.addAll(vars);
			 Collections.reverse(aux);
			 for(Clause s : expression) {
				 String first = s.getFirst().replace("-", "");
				 String second = s.getSecond().replace("-", "");
				 String third = s.getThird().replace("-", "");
				 if(!aux.contains(first)) {
					 aux.add(first);
				 }
				 if(!aux.contains(second)) {
					 aux.add(second);
				 }
				if(!aux.contains(third)) {
					aux.add(third);
				}
			 }
			 return aux.toArray(new String[]{});
		} else {
			Map<String,Integer> map = new HashMap<>();
			for(Clause s : expression) {
				String first = s.getFirst().replace("-", "");
				String second = s.getSecond().replace("-", "");
				String third = s.getThird().replace("-", "");
				if(!map.containsKey(first)) {
					map.put(first, 0);
				} else {
					map.put(first, 1 + map.get(first));
				}
				if(!map.containsKey(second)) {
					map.put(second, 0);
				} else {
					map.put(second, 1 + map.get(second));
				}
				if(!map.containsKey(third)) {
					map.put(third, 0);
				} else {
					map.put(third, 1 + map.get(third));
				}
			}
			TreeMap<Integer,List<String>> sortedMap = inverse(map);
			List<String> rtr = new ArrayList<>();
			for(Integer s : sortedMap.keySet()) {
				System.out.println(s + " " + sortedMap.get(s));
				rtr.addAll(sortedMap.get(s));
			}
			return rtr.toArray(new String[]{});
		}
	}
	
	public static <K, V> TreeMap<K, List<V>> inverse(Map<V, K> map) {
		TreeMap<K, List<V>> nmap = new TreeMap<>();
		for(V key : map.keySet()) {
			if(nmap.containsKey(map.get(key))) {
				List<V> list = nmap.get(map.get(key));
				list.add(key);
			} else {
				List<V> list = new ArrayList<>();
				list.add(key);
				nmap.put(map.get(key), list);
			}
		}
		return nmap;
	}

	public static void main(String[] args) {
		try {
			DIMACSReader reader = new DIMACSReader(filename);
			new Main3SATSolver(reader.getExpression());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Boolean> buildStringListForEachChar(String s) {
		//System.out.println("--> " + s.length() + " " + s);
		List<Boolean> list = new ArrayList<>(s.length());
		for(char c : s.toCharArray()) {
			list.add(c=='0'?false:true);
		}
		return list;
	}
	
	public static String toBinaryString(BigInteger value, int assignementLength) {
		String str = new StringBuilder(value.toString(2)).reverse().toString();
		if(str.length()<assignementLength) {
			StringBuilder sbuild = new StringBuilder();
			sbuild.append(str);
			for(int i=0;i<(assignementLength-str.length());i++) {
				sbuild.append("0");
			}
			return sbuild.toString();
		} else {
			return str;
		}
	}

}

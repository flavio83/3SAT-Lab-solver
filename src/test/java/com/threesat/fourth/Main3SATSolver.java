package com.threesat.fourth;

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
	
	//static String filename = "C:\\DEV\\3sat\\UUF250.1065.100\\uuf250-098.cnf";
	//static String filename = "C:\\DEV\\3sat\\UF250.1065.100\\uf250-072.cnf";
	//static String filename = "C:\\DEV\\3sat\\3sat1000sot\\uf20-0532.cnf";
	static String filename = "C:\\DEV\\3sat\\UF50.218.1000\\uf50-01.cnf";
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
		new Main3SATSolver(expression.toArray(new Clause[]{}));
	}
	
	int acc = 0;
	List<String> val = new ArrayList<>();
	
	public Main3SATSolver(Clause... expression) {

		variables = reshuffleVariables(expression);
		System.out.println("filename: " + filename);
		System.out.println("exp lengh " + variables.length);
		System.out.println("problem space's length: " + new BigInteger("2").pow(variables.length));
		
		Generator<Boolean> binaryGenerator = Factory.createPermutationWithRepetitionGenerator(
				Factory.createVector(new Boolean[] { Boolean.TRUE, Boolean.FALSE }), variables.length);
		
		Generator<String> variablesGenerator = Factory.createPermutationGenerator(Factory.createVector(variables));
		
		//System.out.println(variablesGenerator.getNumberOfGeneratedObjects());
		
		// for each variables combination... (a b c d -> ... -> d b c a)
		for (ICombinatoricsVector<String> vcombination : variablesGenerator) { //no needed of this 
			// for each binary combination
			index = new BigInteger("0");
			variablespermutationCounter++;
			//System.out.println("------|Change permutation");
			List<Clause> aux = new ArrayList<>(Arrays.asList(expression));
			while(true) {
				cyclecounter++;
				
				List<Boolean> v = buildStringListForEachChar(toBinaryString(index,variables.length));
				Assignments ass = new Assignments(vcombination.getVector(),v);
				
				//System.out.println(index.divideAndRemainder(new BigInteger("2").pow(152))[1]);
				//System.out.println("index: " + index + " variablespermutationCounter: " 
				//		+ variablespermutationCounter + " cyclecounter: " + cyclecounter);
				//ICombinatoricsVector<Boolean> bcombination =  null;
				//if(index>(long)Math.pow(2.0d, (double)variables.length))
				//	break;
				//bcombination = binaryGenerator.generateObjectsRange((int)index, (int)index+1).get(0);
				
				//System.out.println(ass);
				Map<Integer,List<Clause>> map = new HashMap<>();
				for(Clause c : aux) {
					int skey = ass.isUnsatisfiable(c);
					List<Clause> l = map.get(skey);
					if(l==null) {
						l = new ArrayList<Clause>();
						map.put(skey, l);
					}
					l.add(c);
				}
				Entry<Integer,List<Clause>>  maxEntry = null;
				for(Entry<Integer,List<Clause>> entry : map.entrySet()) {
				    if (maxEntry == null || entry.getKey().compareTo(maxEntry.getKey())>0) {
				        maxEntry = entry;
				    }
				}
				
				for(Clause c : maxEntry.getValue()) {
					if(usedClause.contains(c)) {
						usedClause.remove(c);
						if(removedClause.containsKey(c)) {
							removedClause.put(c, 1 + removedClause.get(c));
						} else {
							removedClause.put(c, 0);
						}
					} else {
						usedClause.add(c);
						if(addedClause.containsKey(c)) {
							addedClause.put(c, 1 + addedClause.get(c));
						} else {
							addedClause.put(c, 0);
						}
					}
				}
				
				//System.out.println("a------------> " + entriesSortedByValues(addedClause));
				//System.out.println("r------------> " + entriesSortedByValues(removedClause));
				
				int pow = maxEntry.getKey();
				if(!val.contains(findKeyValue(maxEntry.getValue(),ass))) {
					val.add(findKeyValue(maxEntry.getValue(),ass));
				}
				if(acc!=val.size()) {
					System.out.println("num vars used: " + val.size() + " - vars: " + val);
					acc = val.size();
				}
				//System.out.println("index: " + index + " shift of " + maxEntry.getKey() + "(" + (int)Math.pow(2.0d, pow) + ") " + maxEntry.getValue());
				//aux.remove(maxEntry.getValue().get(0));
				if(pow==-1) {
					System.out.println("last---> " + calculateStringProblemSpace(index) + " "+ (System.currentTimeMillis()-timePass)/1000 + "sec, cycleCounter: " + cyclecounter + " index: " + index + " indexDiff: " + index.subtract(prev) );
					System.out.println("satisfiable");
					System.exit(-1);
				} else {
					index = index.add(new BigInteger("2").pow(pow));
					System.out.println(findKeyValue(maxEntry.getValue(),ass) + " - " + new BigInteger("2").pow(pow) + " - " + maxEntry.getValue());
				}
				//System.out.println(index + " " + variables.length);
				if(index.equals(new BigInteger("2").pow(variables.length))) {
					System.out.println("last---> " + calculateStringProblemSpace(index) + " "+ (System.currentTimeMillis()-timePass)/1000 + "sec, cycleCounter: " + cyclecounter + " index: " + index + " indexDiff: " + index.subtract(prev) );
					System.out.println("UNsatisfiable");
					System.exit(-1);
				}
			}
			
		}
	}
	
	
	
	private String findKeyValue(List<Clause> l, Assignments ass) {
		if(l.size()==1) {
			return ass.getUnsatisfiableVariableFromClause(l.get(0));
		} else {
			return Assignments.getCommonVariable(l);
		}
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
	
	private String[] reshuffleVariables(Clause... expression) {
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
			Clause c1 = new Clause("a",false,"b",false,"c",false);
			Clause c2 = new Clause("a",false,"b",false,"c",true);
			Clause c3 = new Clause("a",false,"b",true,"c",false);
			Clause c4 = new Clause("a",false,"b",true,"c",true);
			Clause c5 = new Clause("a",true,"b",false,"c",false);
			Clause c6 = new Clause("a",true,"b",false,"c",true);
			Clause c7 = new Clause("a",true,"b",true,"c",false);
			Clause c8 = new Clause("a",true,"b",true,"c",true);
			new Main3SATSolver(reader.getExpression());
			//new Main3SATSolver(c1,c2,c3,c4,c5,c6,c7,c8);
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

package com.theesat.sixth;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import com.abc.Pair;




public class Main3SATSolver {
	
	static long cyclecounter = 0;
	long variablespermutationCounter = 0;
	
	//long index = 0;
	static BigInteger index = new BigInteger("0");
	String[] variables = new String[] { "a", "b", "c", "d" };
	
	//static String filename = "C:\\DEV\\3sat\\UUF150.645.100\\uuf150-01.cnf";
	static String filename = "C:\\DEV\\3sat\\UUF250.1065.100\\uuf250-087.cnf";
	//static String filename = "C:\\DEV\\3sat\\UF250.1065.100\\uf250-070.cnf";
	//static String filename = "C:\\DEV\\3sat\\3sat1000sot\\uf20-0532.cnf";
	//static String filename = "C:\\DEV\\3sat\\UF50.218.1000\\uf50-01.cnf";
	//static String filename = "C:\\DEV\\3sat\\UUF50.218.1000\\uuf50-018.cnf";
	//static String filename = "C:\\DEV\\3sat\\zfcp-2.8-u2-nh.cnf";
	//static String filename = "C:\\DEV\\3sat\\UR-20-10p1_HARD.cnf";
	//static String filename = "C:\\DEV\\3sat\\MD5-28-3.cnf";
	
	static Map<Clause,Integer> removedClause = new HashMap<>();
	static Map<Clause,Integer> addedClause = new HashMap<>();
	List<Clause> usedClause = new ArrayList<>();
	
	static boolean flag = false;
	
	static long timePass = System.currentTimeMillis();
	
	static BigInteger prev = new BigInteger("0");
	
	static {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() { 
		   @Override  
		   public void run() {
			  // System.out.println("a------------> " + entriesSortedByValues(addedClause));
			   //System.out.println("r------------> " + entriesSortedByValues(removedClause));
			   //System.out.println("var: " + countVariables(addedClause.keySet()) + " clauses: " + entriesSortedByValues(removedClause).size());
			   //System.out.println("---> " + (System.currentTimeMillis()-timePass)/1000 + "sec, index: " + index + " " + new StringBuilder(index.toString(2)).reverse().toString());
			   System.out.println("---> " + (System.currentTimeMillis()-timePass)/1000 + "sec, cycleCounter: " + cyclecounter + " index: " + index + " indexDiff: " + index.subtract(prev) + " " + new StringBuilder(index.toString(2)).reverse().toString());
			   prev = new BigInteger(index.toString());
		   }
		},  5000, 5000);
	}
	
	public static int countVariables(Set<Clause> set) {
		List<String> aux = new ArrayList<>();
		for(Clause c : set) {
			if(!aux.contains(c.getFirst())) {
				aux.add(c.getFirst());
			}
			if(!aux.contains(c.getSecond())) {
				aux.add(c.getSecond());
			}
			if(!aux.contains(c.getThird())) {
				aux.add(c.getThird());
			}
		}
		return aux.size();
	}
	
	public Main3SATSolver(List<Clause> expression) {
		new Main3SATSolver(expression.toArray(new Clause[]{}));
	}
	
	Map<Clause,List<Clause>[]> pair = new HashMap<>(); 
	
	private void calculatePair(Clause... expression) {
		for(Clause c : expression) {
			List<Clause> aux = new ArrayList<>();
			aux.addAll(Arrays.asList(expression));
			aux.remove(c);
			pair.put(c, findBestPair(c,aux));
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
	
	private List<Clause> findClausesZero(Clause[] expression) {
		List<Clause> aux = new ArrayList<>();
		for(Clause zero : expression) {
			if(zero.isZero())
				aux.add(zero);
		}
		return aux;
	}
	
	private Pair<Integer,List<Clause>> next(Clause c) {
		List<Clause>[] aux = pair.get(c);
		if(aux[1].size()==0 && aux[2].size()==0 && aux[3].size()==0) {
			//System.out.println("sodd");
			return null;
		} else {
			if(aux[3].size()>0) {
				List<Clause> r = aux[3];
				aux[3].remove(0);
				return new Pair<Integer,List<Clause>>(3,r);
			} else {
				if(aux[2].size()>0) {
					List<Clause> r = aux[2];
					aux[2].remove(0);
					return new Pair<Integer,List<Clause>>(2,r);
				} else {
					if(aux[1].size()>0) {
						List<Clause> r = aux[1];
						aux[1].remove(0);
						return new Pair<Integer,List<Clause>>(1,r);
					} else {
						//System.out.println("sodd");
						return null;
					}
				}
			}
		}
	}
	
	public Main3SATSolver(Clause... expression) {
		
		calculatePair(expression);
		
		List<Double> laux = new ArrayList<>();
		
		List<Clause> lc = findClausesZero(expression);
		
		for(Clause zero : lc) {
			Clause c = zero;
			List<String> variables = new ArrayList<>();
			List<String> literal = new ArrayList<>();
			Map<String,Integer> map = new HashMap<>();
			Map<String,Integer> literalmap = new HashMap<>();
			List<Integer> length = new ArrayList<>();
			List<Clause> clauses = new ArrayList<>();
			while(c!=null) {
				//System.out.println(c);
				Pair<Integer, List<Clause>> pair = next(c);
				if(pair!=null) {
					for(Clause aux : pair.getElement1()) {
						if(!clauses.contains(aux)) {
							c = aux;
						} else {
							break;
						}
					}
				} else {
					c = null;
				}
				if(c==null) {
					//System.out.println("no more variables to add");
				} else {
					length.add(pair.getElement0());
					if(!clauses.contains(c)) {
						clauses.add(c);
						
						if(map.containsKey(c.getFirst())) {
							map.put(c.getFirst(), 1 + map.get(c.getFirst()));
						} else {
							map.put(c.getFirst(), 1);
						}
						if(map.containsKey(c.getSecond())) {
							map.put(c.getSecond(), 1 + map.get(c.getSecond()));
						} else {
							map.put(c.getSecond(), 1);
						}
						if(map.containsKey(c.getThird())) {
							map.put(c.getThird(), 1 + map.get(c.getThird()));
						} else {
							map.put(c.getThird(), 1);
						}
						
						//variables
						if(!variables.contains(c.getFirst())) {
							variables.add(c.getFirst());
						}
						if(!variables.contains(c.getSecond())) {
							variables.add(c.getSecond());
						}
						if(!variables.contains(c.getThird())) {
							variables.add(c.getThird());
						}
						
						//literals
						if(!literal.contains(c.getFirstLiteral())) {
							literal.add(c.getFirstLiteral());
						}
						if(!literal.contains(c.getSecondLiteral())) {
							literal.add(c.getSecondLiteral());
						}
						if(!literal.contains(c.getThirdLiteral())) {
							literal.add(c.getThirdLiteral());
						}
						
						if(literalmap.containsKey(c.getFirstLiteral())) {
							literalmap.put(c.getFirstLiteral(), 1 + literalmap.get(c.getFirstLiteral()));
						} else {
							literalmap.put(c.getFirstLiteral(), 1);
						}
						if(literalmap.containsKey(c.getSecondLiteral())) {
							literalmap.put(c.getSecondLiteral(), 1 + literalmap.get(c.getSecondLiteral()));
						} else {
							literalmap.put(c.getSecondLiteral(), 1);
						}
						if(literalmap.containsKey(c.getThirdLiteral())) {
							literalmap.put(c.getThirdLiteral(), 1 + literalmap.get(c.getThirdLiteral()));
						} else {
							literalmap.put(c.getThirdLiteral(), 1);
						}
						
					}
				}
			}
			
			//clauses = cleanExpression(clauses,map);
			//variables = countVariables(clauses);
			/*
			if(variables.size()>0)
			System.out.println(zero + " <> var"  + variables.size() 
					+ "|cls" + clauses.size() 
					+ "|" + ((double)clauses.size()/(double)variables.size())
					+ "|" + length
					+ "|" + clauses
					+ "|" + literalmap);
					*/
			
			if(variables.size()>0)
				System.out.println(zero + " <> var"  + literalmap);
			
			laux.add((double)clauses.size()/(double)variables.size());
			
			int one = 0;
			int two = 0;
			int three = 0;
			for(Integer i : length) {
				if(i==1) {
					one++;
				}
				if(i==2) {
					two++;
				}
				if(i==3) {
					three++;
				}
			}
			BigDecimal total = new BigDecimal("0");
			int numVariables = variables.size();
			if(numVariables>2)
			total = total.add(new BigDecimal("2").pow(numVariables-2));
			for(int i=0;i<numVariables-2;i++) {
				total = total.add(new BigDecimal("2").pow(numVariables-i-2));
			}
			//System.out.println("total: " + total + " - " + new BigDecimal("2").pow(numVariables));
			//Main3SATSolver(expression.a
		}
		
		Collections.sort(laux);
		Collections.reverse(laux);
		System.out.println(laux);
		
		System.exit(-1);

		variables = reshuffleVariables(expression);
		System.out.println("exp lengh " + variables.length);
		for(String s : variables)
			System.out.print(s+" ");
		System.out.println();
		System.out.println("problem space's length: " + new BigInteger("2").pow(variables.length));
		BigInteger op = new BigInteger("3975405992839780742222228472405272316195085799067540798431152783656747008");
		System.out.println(new BigInteger("2").pow(variables.length).divide(op));
		
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
				
				if(entriesSortedByValues(removedClause).size()>=15 && flag) {
					System.out.println("index-> " + index);
					System.out.println("a------------> " + entriesSortedByValues(addedClause));
					System.out.println("r------------> " + entriesSortedByValues(removedClause));
					
					SortedSet<Map.Entry<Clause,Integer>> set = entriesSortedByValues(removedClause);
					
					ass = new Assignments();
					List<Clause> addedCluase = new ArrayList<>();
					for(Map.Entry<Clause,Integer> entry : set) {
						Clause c = entry.getKey();
						addedCluase.add(c);
					}
					//add the rest of the clauses
					for(Clause c : expression) {
						if(!addedCluase.contains(c)) {
							ass.addAssignment(c.getFirst(),false);
							ass.addAssignment(c.getSecond(),false);
							ass.addAssignment(c.getThird(),false);
						}
					}
					for(Clause c : addedCluase) {
						ass.addAssignment(c.getFirst(),false);
						ass.addAssignment(c.getSecond(),false);
						ass.addAssignment(c.getThird(),false);
					}
					index = new BigInteger("0");
					System.out.println("-> " + ass);
					flag = false;
				}
				
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

				//System.out.println("a------------> " + entriesSortedByValues(addedClause) + " " + index);
				//System.out.println("r------------> " + entriesSortedByValues(removedClause));
				
				int pow = maxEntry.getKey();
				//System.out.println("index: " + index + " shift of " + maxEntry.getKey() + "(" + (int)Math.pow(2.0d, pow) + ") " + maxEntry.getValue());
				//aux.remove(maxEntry.getValue().get(0));
				if(pow==-1) {
					System.out.println("last ---> " + (System.currentTimeMillis()-timePass)/1000 + "sec, index: " + index + " indexDiff: " + index.subtract(prev) + " " + new StringBuilder(index.toString(2)).reverse().toString());
					System.out.println("satisfiable");
					System.exit(-1);
				} else {
					index = index.add(new BigInteger("2").pow(pow));
				}
				//System.out.println(index + " " + variables.length);
				if(index.equals(new BigInteger("2").pow(variables.length))) {
					System.out.println("last ---> " + (System.currentTimeMillis()-timePass)/1000 + "sec, index: " + index + " indexDiff: " + index.subtract(prev) + " " + new StringBuilder(index.toString(2)).reverse().toString());
					System.out.println("UNsatisfiable");
					System.exit(-1);
				}
			}
			
		}
	}
	
	private List<String> countVariables(List<Clause> lc) {
		List<String> variables = new ArrayList<>();
		for(Clause c : lc) {
			if(!variables.contains(c.getFirst())) {
				variables.add(c.getFirst());
			}
			if(!variables.contains(c.getSecond())) {
				variables.add(c.getSecond());
			}
			if(!variables.contains(c.getThird())) {
				variables.add(c.getThird());
			}
		}
		return variables;
	}
	
	private List<Clause> cleanExpression(List<Clause> l, Map<String,Integer> v) {
		List<String> k = getVarWithOne(v);
		List<Clause> sub = new ArrayList<>();
		for(Clause c : l) {
			if(k.contains(c.getFirst()) || k.contains(c.getSecond()) || k.contains(c.getThird()))
					sub.add(c);
		}
		return sub;
	}
	
	private List<String> getVarWithOne(Map<String,Integer> v) {
		List<String> aux = new ArrayList<>();
		for(String i : v.keySet()) {
			if(v.get(i)==1)
				aux.add(i);
		}
		return aux;
	}
	
	private boolean containOneAsValue(Map<String,Integer> v) {
		for(Integer i : v.values()) {
			if(i==1)
				return true;
		}
		return false;
	}
	
	static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
            new Comparator<Map.Entry<K,V>>() {
                @Override 
                public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                    int res = e1.getValue().compareTo(e2.getValue());
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
		for(String s : rtr) 
			System.out.print(s + " ");
		System.out.println();
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

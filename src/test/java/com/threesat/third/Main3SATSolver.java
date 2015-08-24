package com.threesat.third;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;




public class Main3SATSolver {
	
	long cyclecounter = 0;
	long variablespermutationCounter = 0;
	
	//long index = 0;
	static BigInteger index = new BigInteger("0");
	String[] variables = new String[] { "a", "b", "c", "d" };
	
	//static String filename = "C:\\Documents and Settings\\marchifl\\Desktop\\3sat\\UUF250.1065.100\\uuf250-01.cnf";
	//static String filename = "C:\\Documents and Settings\\marchifl\\Desktop\\3sat\\3sat1000sot\\uf20-0532.cnf";
	static String filename = "C:\\DEV\\3sat\\UF50.218.1000\\uf50-01.cnf";
	
	static long timePass = System.currentTimeMillis();
	
	static {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() { 
		   BigInteger prev = new BigInteger("0");
		   @Override  
		   public void run() {
			   //System.out.println("---> " + (System.currentTimeMillis()-timePass)/1000 + "sec, index: " + index + " " + new StringBuilder(index.toString(2)).reverse().toString());
			   System.out.println("---> " + (System.currentTimeMillis()-timePass)/1000 + "sec, index: " + index + " indexDiff: " + index.subtract(prev) + " " + new StringBuilder(index.toString(2)).reverse().toString());
			   prev = new BigInteger(index.toString());
		   }
		},  0, 4000);
	}
	
	public Main3SATSolver(List<Clause> expression) {
		new Main3SATSolver(expression.toArray(new Clause[]{}));
	}
	
	public Main3SATSolver(Clause... expression) {
		
		

		variables = findVariables(expression);
		System.out.println("exp lengh " + variables.length);
		
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
				//System.out.println("index: " + index + " variablespermutationCounter: " 
				//		+ variablespermutationCounter + " cyclecounter: " + cyclecounter);
				//ICombinatoricsVector<Boolean> bcombination =  null;
				//if(index>(long)Math.pow(2.0d, (double)variables.length))
				//	break;
				//bcombination = binaryGenerator.generateObjectsRange((int)index, (int)index+1).get(0);
				List<Boolean> v = buildStringListForEachChar(toBinaryString(index,variables.length));
				Assignments ass = new Assignments(vcombination.getVector(),v);
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
				int pow = maxEntry.getKey();
				//System.out.println("index: " + index + " shift of " + maxEntry.getKey() + "(" + (int)Math.pow(2.0d, pow) + ") " + maxEntry.getValue());
				//aux.remove(maxEntry.getValue().get(0));
				if(pow==-1) {
					System.out.println("satisfiable");
					System.exit(-1);
				} else {
					index = index.add(new BigInteger(String.valueOf((long)Math.pow(2.0d, pow))));
				}
				//System.out.println(index + " " + variables.length);
				if(index.equals(new BigInteger("2").pow(variables.length))) {
					System.out.println("UNsatisfiable");
					System.exit(-1);
				}
			}
			
		}
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
		return vars.toArray(new String[]{});
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
			list.add(c=='0'?true:false);
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

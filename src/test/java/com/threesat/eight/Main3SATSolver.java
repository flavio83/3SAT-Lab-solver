package com.threesat.eight;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;




public class Main3SATSolver {
	
	static long cyclecounter = 0;
	long variablespermutationCounter = 0;
	
	//long index = 0;
	static BigInteger index = new BigInteger("0");
	String[] variables = new String[] { "a", "b", "c", "d" };
	
	//static String filename = "C:\\DEV\\3sat\\UUF150.645.100\\uuf150-01.cnf";
	//static String filename = "C:\\DEV\\3sat\\UUF250.1065.100\\uuf250-087.cnf";
	//static String filename = "C:\\DEV\\3sat\\UF250.1065.100\\uf250-070.cnf";
	//static String filename = "C:\\DEV\\3sat\\3sat1000sot\\uf20-0532.cnf";
	//static String filename = "C:\\DEV\\3sat\\UF50.218.1000\\uf50-01.cnf";
	static String filename = "C:\\DEV\\3sat\\UUF50.218.1000\\uuf50-018.cnf";
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
			   //System.out.println("---> " + (System.currentTimeMillis()-timePass)/1000 + "sec, cycleCounter: " + cyclecounter + " index: " + index + " indexDiff: " + index.subtract(prev) + " " + new StringBuilder(index.toString(2)).reverse().toString());
			   prev = new BigInteger(index.toString());
		   }
		},  5000, 5000);
	}
	
	public Main3SATSolver(List<Clause> expression) {
		new Main3SATSolver(expression.toArray(new Clause[]{}));
	}
	
	public Main3SATSolver(Clause... expression) {
		List<Clause> r = Arrays.asList(expression);
		int last = 0;
		boolean flag = true;
		int limit = 10;
		while(flag) {
			r = solve(r,limit--);
			System.out.println(r.size() + " - " + last);
			flag = r.size()!=last;
			last = r.size();
		}
		System.out.println("finish");
	}
	
	public List<Clause> solve(List<Clause> expression, int limit) {
		System.out.println("new round, limit: <" + limit);
		//System.out.println(expression.length);
		//expression = Arrays.copyOfRange(expression, 0, 6);
		Generator<Clause> clauseGen = Factory.createSimpleCombinationGenerator(Factory.createVector(expression),2);
		System.out.println("->"+binomial(expression.size(),2));
		long counter = 0;
		List<Clause> newc = new ArrayList<Clause>();
		for (ICombinatoricsVector<Clause> vcombination : clauseGen) {
			//System.out.println(vcombination);
			List<Clause> vector = vcombination.getVector();
			Clause c = vector.get(0).simplify(vector.get(1));
			
			if(c!=null && c.size()<limit) {
				//System.out.println(c);
				newc.add(c);
			}
			counter++;
		}
		List<Clause> lexpression = new ArrayList<>();
		lexpression.addAll(expression);
		long countAdded = 0;
		for(Clause c : newc) {
			if(!lexpression.contains(c)) {
				lexpression.add(c);
				countAdded++;
				System.out.println("added " + c);
			} else {
				//System.out.println("already present " + c);
			}
		}
		System.out.println("length list release: " + lexpression.size() + " vs: " + expression.size());
		return lexpression;
	}
	
	static BigInteger binomial(final int N, final int K) {
	    BigInteger ret = BigInteger.ONE;
	    for (int k = 0; k < K; k++) {
	        ret = ret.multiply(BigInteger.valueOf(N-k))
	                 .divide(BigInteger.valueOf(k+1));
	    }
	    return ret;
	}
	
	public static void main(String[] args) {
		try {
			DIMACSReader reader = new DIMACSReader(filename);
			new Main3SATSolver(reader.getExpression());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

package com.threesat.first;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;




public class Main3SATSolver {
	
	long index = 0;
	String[] variables = new String[] { "a", "b", "c", "d" };

	public Main3SATSolver(Clause... expression) {
		Generator<Boolean> binaryGenerator = Factory.createPermutationWithRepetitionGenerator(
				Factory.createVector(new Boolean[] { Boolean.TRUE, Boolean.FALSE }), variables.length);
		
		Generator<String> variablesGenerator = Factory.createPermutationGenerator(Factory.createVector(variables));
		
		// for each variables combination... (a b c d -> ... -> d b c a)
		for (ICombinatoricsVector<String> vcombination : variablesGenerator) {
			// for each binary combination
			while(true) {
				ICombinatoricsVector<Boolean> bcombination = binaryGenerator.generateObjectsRange((int)index, (int)index+1).get(0);
				Assignments ass = new Assignments(vcombination.getVector(),bcombination.getVector());
				List<Integer> max = new ArrayList<>();
				for(Clause c : expression) {
					max.add(ass.isUnsatisfiable(c));
				}
				int pow = Collections.max(max);
				if(pow==-1) {
					System.out.println("satisfiable");
					System.exit(-1);
				} else {
					System.out.println(index + " " + pow);
					index += (long)Math.pow(2.0d, (double)pow);
					System.out.println("->" + index);
				}
				//System.out.println((long)Math.pow(2.0d, (double)variables.length));
				if(index==(long)Math.pow(2.0d, (double)variables.length)) {
					System.out.println("UNsatisfiable");
					System.exit(-1);
				}
			}
			
		}
	}

	public static void main(String[] args) {
		Clause c1 = new Clause("a",false,"b",false,"c",false);
		Clause c2 = new Clause("a",false,"b",false,"c",true);
		Clause c3 = new Clause("a",false,"b",true,"c",false);
		Clause c4 = new Clause("a",false,"b",true,"c",true);
		Clause c5 = new Clause("a",true,"b",false,"c",false);
		Clause c6 = new Clause("a",true,"b",false,"c",true);
		Clause c7 = new Clause("a",true,"b",true,"c",false);
		Clause c8 = new Clause("a",true,"b",true,"c",true);
		new Main3SATSolver(c1,c2,c3,c4,c5,c6,c7,c8);
	}

}

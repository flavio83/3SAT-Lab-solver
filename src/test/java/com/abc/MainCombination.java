package com.abc;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;




public class MainCombination {

	public MainCombination() {
		// Create the initial vector
		ICombinatoricsVector<String> originalVector = Factory
				.createVector(new String[] { "0", "1" });

		// Create a simple combination generator to generate 3-combinations of
		// the initial vector
		//Generator<String> gen = Factory.createSimpleCombinationGenerator(initialVector, 10);
		
		Generator<String> gen = Factory.createPermutationWithRepetitionGenerator(originalVector, 10);

		// Print all possible combinations
		for (ICombinatoricsVector<String> combination : gen) {
			System.out.println(combination);
		}
		
		System.out.println(gen.generateObjectsRange(8,8));
		System.out.println(gen.getNumberOfGeneratedObjects());
	}

	public static void main(String[] args) {
		new MainCombination();
	}

}

package com.threesat.nineteenth;

import java.util.List;




public class StraightCaluseProcessor {

	private StraightCaluseProcessor(Clause a, Clause b) {
		
	}
	
	public static void simplify(List<Clause> exp) {
		
	}
	
	private StraightCaluseProcessor getInstance(Clause a, Clause b) {
		if(a.isAllOnes() || a.isAllZero()) {
			
		}
		a.simplify(b);
		
		return null;
	}

}

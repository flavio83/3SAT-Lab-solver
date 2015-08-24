package com.theesat.fifth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.abc.Pair;


// 0 1 0 1 0 1 0 1 0 0 0 1 0 0 1....
public class Assignments extends ArrayList<Pair<String,Boolean>> {
	
	public Assignments() {
		
	}
	
	public Assignments(List<String> lstring, List<Boolean> lboolean) {
		for(int i=0;i<lstring.size();i++) {
			this.add(new Pair<String,Boolean>(lstring.get(i),lboolean.get(i)));
		}
	}
	
	public int isUnsatisfiable(Clause c) {
		List<Integer> max = new ArrayList<>();
		int first = isPerformed(c.getFirst(),c.isFirstNegate());
		int second = isPerformed(c.getSecond(),c.isSecondNegate());
		int third = isPerformed(c.getThird(),c.isThirdNegate());
		if(first > -1 && second > -1 && third > -1) {
			max.add(first);
			max.add(second);
			max.add(third);
			return Collections.min(max);
		}
		return -1;
	}
	
	public void addAssignment(String v, Boolean n) {
		this.add(new Pair<String,Boolean>(v,n));
	}
	
	private int isPerformed(String v, Boolean negate) {
		//System.out.println(this);
		//System.out.println(v + " " + negate + " " + indexOf(new Pair<String,Boolean>(v,negate)));
		return indexOf(new Pair<String,Boolean>(v,negate));
		
	}

}



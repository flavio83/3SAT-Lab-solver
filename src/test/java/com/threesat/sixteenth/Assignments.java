package com.threesat.sixteenth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.abc.Pair;


// 0 1 0 1 0 1 0 1 0 0 0 1 0 0 1....
public class Assignments extends ArrayList<Pair<String,Boolean>> {
	
	public static String getCommonVariable(List<Clause> l) {
		HashMap<String,Integer> set = new HashMap<String,Integer>();
		for(Clause c : l) {
			if(set.containsKey(c.getFirst())) {
				set.put(c.getFirst(), set.get(c.getFirst()).intValue() + 1);
			} else {
				set.put(c.getFirst(), 1);
			}
			if(set.containsKey(c.getSecond())) {
				set.put(c.getSecond(), set.get(c.getSecond()).intValue() + 1);
			} else {
				set.put(c.getSecond(), 1);
			}
			if(set.containsKey(c.getThird())) {
				set.put(c.getThird(), set.get(c.getThird()).intValue() + 1);
			} else {
				set.put(c.getThird(), 1);
			}
		}
		return getKeyByValue(set, Collections.max(set.values()));
	}
	
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	
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
	
	public String getUnsatisfiableVariableFromClause(Clause c) {
			List<Integer> max = new ArrayList<>();
			int first = isPerformed(c.getFirst(),c.isFirstNegate());
			int second = isPerformed(c.getSecond(),c.isSecondNegate());
			int third = isPerformed(c.getThird(),c.isThirdNegate());
			if(first > -1 && second > -1 && third > -1) {
				max.add(first);
				max.add(second);
				max.add(third);
				int rmin = Collections.min(max);
				if(rmin==first)
					return c.getFirst();
				else if(rmin==second)
					return c.getSecond();
				else
					return c.getThird();
			} else {
				return "null";
			}
	}
	
	public void addAssignment(String v, Boolean n) {
		this.add(new Pair<String,Boolean>(v,n));
	}
	
	private int isPerformed(String v, Boolean negate) {
		//System.out.println(this);
		//System.out.println(v + " " + negate + " " + indexOf(new Pair<String,Boolean>(v,negate)));
		return indexOf(new Pair<String,Boolean>(v,negate));
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(Pair<String,Boolean> p : this) {
			s.append(p.getElement0());
			s.append(" ");
		}
		return s.toString();
	}

}



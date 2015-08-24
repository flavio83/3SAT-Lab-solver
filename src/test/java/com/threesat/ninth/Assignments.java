package com.threesat.ninth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

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
	
	private static Map<Clause,Integer> map = new HashMap<>();
	
	public int isUnsatisfiable(Clause c) {
		List<Integer> max = new ArrayList<>();
		int first = isPerformed(c.getFirst(),c.isFirstNegate());
		int second = isPerformed(c.getSecond(),c.isSecondNegate());
		int third = isPerformed(c.getThird(),c.isThirdNegate());
		if(first > -1 && second > -1 && third > -1) {
			max.add(first);
			max.add(second);
			max.add(third);
			//stat(c,Collections.min(max));
			return Collections.min(max);
		}
		return -1;
	}
	
	static long aux = 0;
	static long last = 0;
	
	private void stat(Clause c, int shift) {
		if(map.containsKey(c)) {
			map.put(c, 1 + map.get(c));
		} else {
			map.put(c, 1);
		}
		SortedSet<Map.Entry<Clause,Integer>> smap = entriesSortedByValues(map);
		aux = smap.first().getValue();
		if(aux%10000==0 && aux!=last) {
			System.out.println(entriesSortedByValues(map));
			last = aux;
		}
	}
	
	static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
            new Comparator<Map.Entry<K,V>>() {
                @Override 
                public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                    int res = -1 * e1.getValue().compareTo(e2.getValue());
                    return res != 0 ? res : 1; // Special fix to preserve items with equal values
                }
            }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
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



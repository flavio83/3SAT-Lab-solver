package com.threesat.eleventh;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class DIMACSReader {
	
	List<Clause> exp = new ArrayList<>();
	
	public DIMACSReader(String filename) throws Exception {
	    Path path = Paths.get(filename);
	    //When filteredLines is closed, it closes underlying stream as well as underlying file.
	    try(Stream<String> filteredLines = Files.lines(path)
	                                    //test if file is closed or not
	                                    .onClose(() -> System.out.println("reading complete"))){

    		List<String> list = filteredLines.collect(Collectors.toCollection(ArrayList::new));
    		for(String s : list) {
    			if(s.startsWith("p")) {
    				//parse info...
    			} else if(s.startsWith("%")) {
    				//boh
    			} else if(s.startsWith("0")) {
    				//stream consumed
    				break;
    			} else if(!s.startsWith("c")) {
    				String[] aux = s.trim().split(" ");
    				exp.add(new Clause(Arrays.copyOfRange(aux, 0, aux.length-1)));
    				System.out.println("->" + new Clause(Arrays.copyOfRange(aux, 0, aux.length-1)));
    			}
    		}
	    }
	}
	
	public List<Clause> getExpression() {
		return exp;
	}

}

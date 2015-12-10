package com.threesat.nineteenth;

import java.util.ArrayList;
import java.util.List;

import com.abc.Pair;
import com.bethecoder.ascii_table.ASCIITable;
import com.bethecoder.ascii_table.ASCIITableHeader;
import com.threesat.thirteenth.Clause;
import com.threesat.thirteenth.Literal;


public class Assignment2 extends ArrayList<Pair<String,Boolean>> {
	
	public Assignment2(List<String> lstring) {
		for(int i=0;i<lstring.size();i++) {
			this.add(new Pair<String,Boolean>(lstring.get(i),Boolean.FALSE));
		}
	}
	
	public Assignment2(List<String> lstring, String except) {
		for(int i=0;i<lstring.size();i++) {
			if(lstring.get(i).compareTo(except)==0) {
				this.add(new Pair<String,Boolean>(lstring.get(i),Boolean.TRUE));
			}
			this.add(new Pair<String,Boolean>(lstring.get(i),Boolean.FALSE));
		}
	}
	
	public Assignment2(String except, List<String>... llstring) {
		for(List<String> lstring : llstring) {
			for(int i=0;i<lstring.size();i++) {
				if(lstring.get(i).compareTo(except)!=0) {
					this.add(new Pair<String,Boolean>(lstring.get(i),Boolean.FALSE));
				}
			}
		}
		this.add(new Pair<String,Boolean>(except,Boolean.TRUE));
	}
	
	public Assignment2(List<String> lstring, List<Boolean> lboolean) {
		for(int i=0;i<lstring.size();i++) {
			this.add(new Pair<String,Boolean>(lstring.get(i),lboolean.get(i)));
		}
	}

	public Assignment2() {
		// TODO Auto-generated constructor stub
	}
	
	public Assignment2 solveAssignment(Clause c, Assignment2 ass) {
		
		return null;
	}
	
	public String[] findVariables() {
		String[] aux = new String[this.size()];
		int index = 0;
		for(Pair<String,Boolean> pair : this) {
			aux[index++] = pair.getElement0();
		}
		return aux;
	}
	
	public String toString() {
		String[] vars = findVariables();	
		ASCIITableHeader[] headerObjs = new ASCIITableHeader[vars.length];
		for(int i=0;i<headerObjs.length;i++) {
			headerObjs[i] = new ASCIITableHeader(vars[i]);
		}
		String[][] data = new String[1][this.size()];
		for(int e=0;e<this.size();e++) {
			data[0][e] = this.get(e).getElement1()?"1":"0";
		}
		String text = ASCIITable.getInstance().getTable(headerObjs, data).toString();
		return text.substring(0,text.length()-1);
	}

}

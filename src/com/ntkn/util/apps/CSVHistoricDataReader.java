package com.ntkn.util.apps;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.net.flavix.dto.CSVHistoricRecord;




public class CSVHistoricDataReader {
	
	private BufferedReader breader = null;
	
	String path1 = "C:\\DEV\\AlphaFlash\\Historical Data By Year\\Historical Data By Year\\AF Historical dat 2015 through June 25.csv";
	String path2 = "C:\\DEV\\AlphaFlash\\Historical Data By Year\\Historical Data By Year\\AF Historical Data all 2009.csv";
	String path3 = "C:\\DEV\\AlphaFlash\\Historical Data By Year\\Historical Data By Year\\AF Historical Data all 2010.csv";
	String path4 = "C:\\DEV\\AlphaFlash\\Historical Data By Year\\Historical Data By Year\\AF Historical Data all 2011.csv";
	String path5 = "C:\\DEV\\AlphaFlash\\Historical Data By Year\\Historical Data By Year\\AF Historical Data all 2012.csv";
	String path6 = "C:\\DEV\\AlphaFlash\\Historical Data By Year\\Historical Data By Year\\AF Historical Data all 2013.csv";
	String path7 = "C:\\DEV\\AlphaFlash\\Historical Data By Year\\Historical Data By Year\\AF Historical Data all 2014.csv";

	public CSVHistoricDataReader() throws Exception {
		 breader = new BufferedReader(new FileReader(path2));
	     String[] header = readCSVLine();
	     for(String s : header)
    		 System.out.println(s);
	     long count = 0;
	     List<CSVHistoricRecord> list = new ArrayList<>();
	     if(header!=null) {
		     String[] readLine = readCSVLine();
		     if(readLine!=null) {
			     do {
			    	 list.add(new CSVHistoricRecord(readLine));
			    	 readLine = readCSVLine();
			    	 count++;
			     } while(readLine!=null);
		     }   
	     }
	     new CSVHistoricRecord().persist(list);
	     breader.close();
	}
	
	private String[] readCSVLine() throws Exception {
		String aux = breader.readLine();
		if(aux!=null) {
			return aux.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			new CSVHistoricDataReader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}



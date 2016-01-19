package com.ntkn.util.apps;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.ntkn.util.Pair;



public class GetCSVToComputer {
	
	String category = "40003";
	String country = "CA"; 
	String currency = "EUR_CAD";
	
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
	
	List<LocalDateTime> list = new ArrayList<LocalDateTime>();

	public GetCSVToComputer() {
		try {
			Result<Record> res = getCurrency(category);
			StringBuilder sBuilder = new StringBuilder();
        	for(Record r : res) {
        		String date = r.getValue("datetime").toString();
        		LocalDateTime lDateTime = LocalDateTime.parse(date,dtf);
        		if(lDateTime.getHour()==6 || lDateTime.getHour()==7) {
        			lDateTime = lDateTime.plusHours(6);
        		}
        		if(lDateTime.getHour()==17 || lDateTime.getHour()==18) {
        			lDateTime = lDateTime.minusHours(6);
        		}
        		if(!list.contains(lDateTime)) {
	        		sBuilder.append(lDateTime);
	        		String[] values = (String[])r.getValue("values");
	        		sBuilder.append(" ");
	        		for(int i=1;i<values.length;i++)
	        			sBuilder.append(values[i]+" ");
	        		String diff2 = OandaDataFetcher.getDiff(lDateTime, 10, currency);
	        		String diff3 = OandaDataFetcher.getDiff(lDateTime, 20, currency);
	        		String diff4 = OandaDataFetcher.getDiff(lDateTime, 30, currency);
	        		sBuilder.append(diff2);
	        		sBuilder.append(" ");
	        		sBuilder.append(diff3);
	        		sBuilder.append(" ");
	        		sBuilder.append(diff4);
	        		sBuilder.append(" ");
	        		sBuilder.append("\r\n");
	        		list.add(lDateTime);
        		}
        	}
        	System.out.println(sBuilder.toString());
        	System.out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		/*
		for(int i=0;i<120;i++) {
			//String diff = OandaDataFetcher.getDiff(lDateTime, i, "EUR_GBP");
			//System.out.println(i +") " +diff);
			LocalDateTime lDateTime = LocalDateTime.of(2007, Month.DECEMBER, 6, 12, 00, i);
			Pair<double[],double[]> diff = OandaDataFetcher.getAsksBids(lDateTime, i, "EUR_GBP");
			BigDecimal bid = BigDecimal.valueOf(diff.getElement1()[0]);
			BigDecimal ask = BigDecimal.valueOf(diff.getElement0()[0]);
			System.out.println(i + ") " + bid + " " + ask + " " + (ask.subtract(bid).setScale(5, RoundingMode.HALF_UP)).toPlainString());
		}*/
		new GetCSVToComputer();
	}
	
	private Result<Record> getCurrency(String category) throws Exception {
		String userName = "marchifl";
        String password = "velvia";
        String url = "jdbc:postgresql://localhost:5432/postgres";
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
        	DSLContext create = DSL.using(conn, SQLDialect.POSTGRES_9_4);
        	Result<Record> result = create.fetch("select distinct datetime,values from csvhistoricrecord where categoryid='" + category + "' order by datetime desc");
        	return result;
        }
	}

}

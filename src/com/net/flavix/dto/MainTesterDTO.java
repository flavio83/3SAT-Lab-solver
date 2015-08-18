package com.net.flavix.dto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.ntkn.flavix.CurrencyEvent;




public class MainTesterDTO {

	public MainTesterDTO() throws Exception {
		//AlphaflashCalendar.persistWithRandomData();
		//AlphaflashFlow.persistWithRandomData();
		//CategoriesPlexer.persistWithRandomData();
		fetchReleaseEstimatePrevious(LocalDate.of(2015, 8, 4));
	}
	
	public List<CurrencyEvent> fetchReleaseEstimatePrevious(LocalDate date) throws Exception {
		//obtain all the incoming release for that day
		/*
		String sql = "select b.category,b.date from categoriesplexer a join alphaflashcalendar b "
				+ "on a.category=b.category where b.type=0 "
				+ "
				*/
		
		String sql = "select category,date from alphaflashcalendar where type='RELEASE'" +
				" and date>'"+date.toString()+"' and date<'"+date.plusDays(1)+"'";
		
		Result<Record> result = doQuery(sql);
		for(Record r : result) {
			System.out.println(r);
			
			int category = r.getValue(0, Long.class).intValue();
			LocalDateTime dateTime = r.getValue(1, java.sql.Timestamp.class).toLocalDateTime();
			
			//get last estimate
			sql = "select * from alphaflashflow where category=" + category + " and type=1 order by date asc limit 1";
			int idEstimate = doQuery(sql).get(0).getValue(0, Long.class).intValue();
			
			//get previous release
			sql = "select * from alphaflashflow where category=" + category + " and type=0 and date<'"+date+"' order by date asc limit 1";
			int idPreviousRelease = doQuery(sql).get(0).getValue(0, Long.class).intValue();
			
			CategoriesPlexer plex = new CategoriesPlexer().loadPerCategory(category);
			System.out.println(plex);
			//System.out.println("PREVIOUS " + new AlphaflashFlow().loadPerID(idPreviousRelease));
			//System.out.println("ESTIMATE " + new AlphaflashFlow().loadPerID(idEstimate));
			System.out.println("RELEASE ON " + dateTime);
		}
		
		/*
		String sql = "select * from alphaflashflow where date>'"+date.toString()+"' and date<'"+date.plusDays(1)+"'";
		Result<Record> result = doQuery(sql);
		System.out.println(result.size());
		*/
		
		return null;
	}
	
	private Result<Record> doQuery(String query) throws Exception {
		String userName = "marchifl";
        String password = "velvia";
        String url = "jdbc:postgresql://localhost:5432/postgres";
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
        	DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
        	return create.fetch(query);
        }
	}

	public static void main(String[] args) {
		try {
			new MainTesterDTO();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

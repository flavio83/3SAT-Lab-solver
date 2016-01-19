package com.net.flavix.autotrader.bprocess.eventpopulation;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.postgres.PostgresUtils;

import com.net.flavix.dto.CategoriesPlexer;
import com.net.flavix.dto.CountryEvent;
import com.net.flavix.persistflow.NewsPlainRow;




public class MainCountryEventPopulationBProcess {
	
	public static void main(String[] args) {
		new MainCountryEventPopulationBProcess();
	}

	
	/*
	 * Picking for the AlphaflashStream and AlpahflashCalendar 
	 * to populate the CountryEvent table.
	 * The CategoriesPlexer is used to identify which side has to 
	 * be 
	 */
	public MainCountryEventPopulationBProcess() {
		 try {
			fetchReleaseEstimatePrevious(LocalDate.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<CountryEvent> fetchReleaseEstimatePrevious(LocalDate date) throws Exception {
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
			sql = "select * from newsplainrow where messageheadercategoryid=" + category + " and messageheadertypeid=1 order by date asc limit 1";
			Result<Record> recResult = doQuery(sql);
			if(recResult.size()>0) {
				String aux = doQuery(sql).get(0).getValue("uuid", String.class);
				System.out.println("> "  + aux);
				UUID idEstimate = UUID.fromString(aux);
				System.out.println(idEstimate + " " + getGuidFromByteArray(PostgresUtils.toBytes(aux)));
				
				//get previous release
				sql = "select * from newsplainrow where messageheadercategoryid=" + category + " and messageheadertypeid=0 and date<'"+date+"' order by date asc limit 1";
				UUID idPreviousRelease = UUID.fromString(doQuery(sql).get(0).getValue("uuid", String.class));
				
				CategoriesPlexer plex = new CategoriesPlexer().loadPerCategory(category);
				System.out.println(plex);
				System.out.println("PREVIOUS " + (new NewsPlainRow().loadPerID(idPreviousRelease)));
				System.out.println("ESTIMATE " + new NewsPlainRow().loadPerID(idEstimate));
				System.out.println("RELEASE ON " + dateTime);
			} else {
				System.out.println("no last estimate");
			}
			
		}
		
		/*
		String sql = "select * from alphaflashflow where date>'"+date.toString()+"' and date<'"+date.plusDays(1)+"'";
		Result<Record> result = doQuery(sql);
		System.out.println(result.size());
		*/
		
		return null;
	}
	
	private static UUID getGuidFromByteArray(byte[] bytes)
	{
	    ByteBuffer bb = ByteBuffer.wrap(bytes);
	    return new UUID(bb.getLong(), bb.getLong());
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
}

package com.ntkn.util.apps;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;




public class FromFFToLibFormat {
	
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM d  yyyy");
	
	String[] pairs = {"GBP_CAD","GBP_USD","GBP_JPY","EUR_GBP","GBP_NZD"};
	//String path = "C:\\Documents and Settings\\marchifl\\Desktop\\US40Unemployment.txt";
	String path = "C:\\Documents and Settings\\marchifl\\Desktop\\UKCPI_YOverY.txt";

	public FromFFToLibFormat() throws Exception {
		String content = new String(Files.readAllBytes(Paths.get(path)));
		String[] aux = content.split("\r\n");
		for(String s : aux) {
			String[] auxrow = s.split("\t");
			Double actual = Double.parseDouble(auxrow[1].replace('%', ' ').trim());
			Double estimate = Double.parseDouble(auxrow[2].replace('%', ' ').trim());
			Double previous = Double.parseDouble(auxrow[3].replace('%', ' ').trim());
			LocalDate lDate = LocalDate.parse(auxrow[0].replace(',', ' '),dtf);
			LocalTime lTime = LocalTime.of(9, 30);
			LocalDateTime localDateTime = LocalDateTime.of(lDate, lTime);
			//Date date = offsetTimeZone(Date.from(lDate.atStartOfDay(ZoneId.of("GMT")).toInstant()),"GMT","GMT");
			//System.out.println(date);
			boolean isDST = TimeZone.getTimeZone("Europe/London").inDaylightTime(Date.from(lDate.atStartOfDay(ZoneId.of("GMT")).toInstant()));
			boolean useDST = TimeZone.getTimeZone("Europe/London").useDaylightTime();
			//System.out.println("----> " + isDST);
			if(isDST) {
				lTime = LocalTime.of(8, 30);
				localDateTime = LocalDateTime.of(lDate, lTime);
			}
			//boolean inDs = localDateTime.is(localDateTime);
			for(String pair : pairs) {
				String diff1 = "diff2";
				String diff2 = "diff2";
				String diff3 = "diff2";
				String diff4 = "diff2";
				diff1 = OandaDataFetcher.getDiff(localDateTime, 5, pair);
				diff2 = OandaDataFetcher.getDiff(localDateTime, 10, pair);
				diff3 = OandaDataFetcher.getDiff(localDateTime, 15, pair);
				diff4 = OandaDataFetcher.getDiff(localDateTime, 20, pair);
				System.out.println(isDST + " " + pair + " "+ localDateTime + " " + diff1 + " " + diff2 + " " + diff3 + " " + diff4 + " " + actual + " " + estimate + " " + previous);
			}
			System.out.println();
		}
		//LocalDateTime lDateTime = LocalDateTime.parse(date,dtf);
		//String diff2 = OandaDataFetcher.getDiff(lDateTime, 10, currency);
		
	}
	
	private static Double calculatePercentageDifference(Integer da, Integer db, int precision) {
		return calculatePercentageDifference(BigDecimal.valueOf(da),BigDecimal.valueOf(db),precision).doubleValue();
	}
	
	private static Double calculatePercentageDifference(Double da, Double db, int precision) {
		return calculatePercentageDifference(BigDecimal.valueOf(da),BigDecimal.valueOf(db),precision).doubleValue();
	}
	
	//https://www.mathsisfun.com/percentage-difference.html
	private static BigDecimal calculatePercentageDifference(BigDecimal a, BigDecimal b, int precision) {
		BigDecimal sub = a.subtract(b);
		BigDecimal sum = a.add(b);
		BigDecimal division = sum.divide(new BigDecimal("2"),RoundingMode.HALF_UP);
		division = sub.divide(division, precision, RoundingMode.HALF_UP);
		return division.abs().multiply(new BigDecimal("100"));
	}
	
	private static Double calculatePercentageDifference2(Integer da, Integer db, int precision) {
		return calculatePercentageDifference2(BigDecimal.valueOf(da),BigDecimal.valueOf(db),precision).doubleValue();
	}
	
	private static BigDecimal calculatePercentageDifference2(BigDecimal originalv, BigDecimal finalv, int precision) {
		BigDecimal sub = originalv.subtract(finalv);
		BigDecimal division = sub.divide(originalv, precision, RoundingMode.HALF_UP);
		return division.abs().multiply(new BigDecimal("100"));
	}
	
	private static Date offsetTimeZone(Date date, String fromTZ, String toTZ){
		 
		// Construct FROM and TO TimeZone instances
		TimeZone fromTimeZone = TimeZone.getTimeZone(fromTZ);
		TimeZone toTimeZone = TimeZone.getTimeZone(toTZ);
		 
		// Get a Calendar instance using the default time zone and locale.
		Calendar calendar = Calendar.getInstance();
		 
		// Set the calendar's time with the given date
		calendar.setTimeZone(fromTimeZone);
		calendar.setTime(date);
		 
		System.out.println("Input: " + calendar.getTime() + " in " + fromTimeZone.getDisplayName());
		 
		// FROM TimeZone to UTC
		calendar.add(Calendar.MILLISECOND, fromTimeZone.getRawOffset() * -1);
		 
		if (fromTimeZone.inDaylightTime(calendar.getTime())) {
		calendar.add(Calendar.MILLISECOND, calendar.getTimeZone().getDSTSavings() * -1);
		}
		 
		// UTC to TO TimeZone
		calendar.add(Calendar.MILLISECOND, toTimeZone.getRawOffset());
		 
		if (toTimeZone.inDaylightTime(calendar.getTime())) {
		calendar.add(Calendar.MILLISECOND, toTimeZone.getDSTSavings());
		}
		 
		return calendar.getTime();
		 
		}
	
	public static void main(String[] args) {
		try {
			//System.out.println(calculatePercentageDifference2(200,239,5));
			new FromFFToLibFormat();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

package com.ntkn.calendar;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.axis2.client.Options;
import org.apache.commons.httpclient.HttpConstants;
import org.apache.commons.lang3.ArrayUtils;

import com.net.flavix.dto.AlphaflashCalendar;




public class Main {
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
	
	//yyyyMMdd'T'HH:mm:ssZ
	CalendarWebService service = new CalendarWebServiceProxy("http://www.alphaflash.com/calendarservice/soap?wsdl");

	public Main() {
		System.setProperty("proxySet", "true");
		System.setProperty("http.proxyHost", "primary-proxy.gslb.intranet.barcapint.com");
		System.setProperty("http.proxyPort", "8080");
		System.setProperty("http.agent", "Mozilla/4.0(compatible; MSIE 7.0b; Windows NT 6.0)");
		System.setProperty("networkaddress.cache.ttl", "0");
		System.setProperty("networkaddress.cache.negative.ttl", "0");
		Authenticator.setDefault(new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication("marchifl","b*80*CH9ab".toCharArray());
		    }
		});
		
		try {
			for(int i=1;i<=12;i++) {
				System.out.println(i);
				Thread.sleep(1000);
				fetchAndPersistData(2015,i);
			}
			Thread.sleep(1000);
			//fetchAndPersistData(2015,8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("import completed");
	}
	
	private void fetchAndPersistData(int year, int month) throws Exception {
		ZonedDateTime now = ZonedDateTime.now();
		//if(month<1 || month>12 || (now.getMonth().ordinal()<month && now.getYear()<year))
		if(month<1 || month>12)
			return;
		ZonedDateTime start = ZonedDateTime.of(year, month, 1, 0, 0, 0, 0, ZoneId.of("Europe/London"));
		ZonedDateTime end = start.plusMonths(1).with(firstDayOfMonth());
		//if(end.isAfter(now)) {
		//	end = now;
		//}
		System.out.println(start.format(formatter) + " " + end.format(formatter));
		CalendarEventExt[] eventsEstimate = service.getEvents(start.format(formatter), end.format(formatter), "ESTIMATE");
		CalendarEventExt[] eventsRelease = service.getEvents(start.format(formatter), end.format(formatter), "RELEASE");
		CalendarEventExt[] events = ArrayUtils.addAll(eventsEstimate,eventsRelease);
		if(events!=null && events.length>0) {
			List<AlphaflashCalendar> fromWebService = new ArrayList<>(events.length);
			for(CalendarEventExt event : events) {
				fromWebService.add(new AlphaflashCalendar(event));
			}
			Collections.sort(fromWebService);
			List<AlphaflashCalendar> fromDataBase = new AlphaflashCalendar().loadPerDateRange(start,end);
			Collections.sort(fromDataBase);
			List<AlphaflashCalendar> toPersist = new ArrayList<>();
			for(AlphaflashCalendar releaseEstimate : fromWebService) {
				if(!fromDataBase.contains(releaseEstimate)) {
					System.out.println("added " + releaseEstimate);
					toPersist.add(releaseEstimate);
				}
			}
			new AlphaflashCalendar().persist(toPersist.toArray(new AlphaflashCalendar[]{}));
			List<AlphaflashCalendar> toRemove = new ArrayList<>();
			for(AlphaflashCalendar releaseEstimate : fromDataBase) {
				if(!fromWebService.contains(releaseEstimate)) {
					releaseEstimate.setRemovedDate(LocalDateTime.now());
					toRemove.add(releaseEstimate);
				}
			}
			new AlphaflashCalendar().merge(toRemove.toArray(new AlphaflashCalendar[]{}));
		}
	}

	public static void main(String[] args) {
		new Main();
	}
	
}

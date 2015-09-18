package com.ntkn.util.apps;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.net.flavix.dto.EstimateReleaseNewsFeed;
import com.net.flavix.dto.HibernateUtil;
import com.ntkn.messages.IndicatorMessage;
import com.ntkn.messages.IndicatorMessageField;
import com.ntkn.messages.IndicatorMessageHeader;
import com.ntkn.messages.IndicatorMessagePayload;
import com.sun.org.apache.xml.internal.security.utils.Base64;



public class TestEstimateReleaseNewsFeed {
	
	String filename = "C:\\DEV\\AlphaFlash\\logLive.txt";

	Gson gson = new Gson();
	
	private static final String DATE_FORMAT = "E dd-MM-yyyy HH:mm:ss.SSS";

	public TestEstimateReleaseNewsFeed() {
		SimpleDateFormat dateFormatter =  new SimpleDateFormat(DATE_FORMAT);
		dateFormatter.setTimeZone(TimeZone.getDefault());   
        try {
              Scanner content = new Scanner(new File(filename)).useDelimiter("\\r\\n");
              while(content.hasNext()) {
            	  String text = content.next();
            	  LinkedTreeMap news = gson.fromJson(text,LinkedTreeMap.class);
            	  Date date = dateFormatter.parse((String)news.get("date"));
                  byte[] headerBytes = Base64.decode((String)news.get("HeaderBytes"));
                  byte[] payloadBytes = Base64.decode((String)news.get("PayloadBytes"));
                  byte[] crcBytes = Base64.decode((String)news.get("CrcBytes"));
                  IndicatorMessageHeader header = new IndicatorMessageHeader(headerBytes);
                  IndicatorMessagePayload payload = new IndicatorMessagePayload(payloadBytes);
                  IndicatorMessage msg = new IndicatorMessage(header, payload, crcBytes);
                  List<IndicatorMessageField> payloadList = msg.getPayload().getIndicatorMessageFields();
                  if(header.getMessageTypeId()<2) {
	                  Iterator<IndicatorMessageField> iter =  payloadList.iterator();
	                  EstimateReleaseNewsFeed est = new EstimateReleaseNewsFeed();
	                  est.setCategory(header.getMessageCategoryId());
	                  est.setType(header.getMessageTypeId());
	                  est.setDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneOffset.UTC));
	                  while(iter.hasNext()) {
	                	  IndicatorMessageField field = iter.next();
	                	  est.setField((int)field.getFieldId(), field.getfieldValue());
	                  }
	                  EstimateReleaseNewsFeed.persistTupla(HibernateUtil.getSessionFactory().openSession(), est);
                  }
              }
              System.out.println("import completed");
        } catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		new TestEstimateReleaseNewsFeed();
	}

}

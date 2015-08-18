package com.net.flavix.persistflow;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import org.apache.axis.encoding.Base64;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.nktin.protocol.tailored.NewsEvent;
import com.nktin.protocol.tailored.ParseAllXMLs;
import com.ntkn.messages.IndicatorMessage;
import com.ntkn.messages.IndicatorMessageField;
import com.ntkn.messages.IndicatorMessageHeader;
import com.ntkn.messages.IndicatorMessagePayload;



public class AlphaParser {

	String filename = "C:\\DEV\\AlphaFlash\\logLive.txt";

	Gson gson = new Gson();
	
	 private static final String DATE_FORMAT = "E dd-MM-yyyy HH:mm:ss.SSS";
	 
	 ParseAllXMLs xmls = new ParseAllXMLs();
	 
	 PrintWriter writer = null;

	public AlphaParser() {
		SimpleDateFormat dateFormatter =  new SimpleDateFormat(DATE_FORMAT);
		dateFormatter.setTimeZone(TimeZone.getDefault());   
        try {
        	writer = new PrintWriter("F:\\demo\\logUS_Pending_Home_Sales.txt", "UTF-8");
              Scanner content = new Scanner(new File(filename)).useDelimiter("\\r\\n");
              while(content.hasNext()) {
            	  String text = content.next();
            	  LinkedTreeMap news = gson.fromJson(text,LinkedTreeMap.class);
//            	  for(Object key : news.keySet()) {
//            		  System.out.println(key + "> " + news.get(key));
//            	  }
            	  Date date = dateFormatter.parse((String)news.get("date"));
                  byte[] headerBytes = Base64.decode((String)news.get("HeaderBytes"));
                  byte[] payloadBytes = Base64.decode((String)news.get("PayloadBytes"));
                  byte[] crcBytes = Base64.decode((String)news.get("CrcBytes"));
                  IndicatorMessageHeader header = new IndicatorMessageHeader(headerBytes);
                  IndicatorMessagePayload payload = new IndicatorMessagePayload(payloadBytes);
                  IndicatorMessage msg = new IndicatorMessage(header, payload, crcBytes);
                  
                  if(header.getMessageTypeId()!=2) {
	                  NewsPlainRow row = new NewsPlainRow(date,msg);
	                  System.out.println("fieldsnum: " + msg.getPayload().getIndicatorMessageFields().size());
	                  new NewsPlainRow().persist(row);
                  }
                  List<IndicatorMessageField> payloadList = msg.getPayload().getIndicatorMessageFields();
                  Iterator<IndicatorMessageField> iter =  payloadList.iterator();
                  if(msg.getHeader().getMessageTypeId()<2 && msg.getHeader().getMessageCategoryId()==40017) {
                	 // System.out.print(date+" ");
                	  //System.out.print(msg.getHeader().getMessageCategoryId()+" "+(msg.getHeader().getMessageTypeId()==0?"RELEASE":"ESTIMATE"));
                	  NewsEvent newsEvent = xmls.getMap().get(header.getMessageCategoryId());
                	  if(newsEvent!=null) {
                		  System.out.print("("+msg.getHeader().getMessageTypeId()+") "+newsEvent.getDescription() + " ");
                	  } else {
                		  System.out.print("no description ");
                	  }
	      			  while(iter.hasNext()) {
	      				IndicatorMessageField msgField = iter.next();
	      			//	System.out.print(msgField.toFormattedString());
	      				//System.out.print(" ");
	      			  }
	      			 // System.out.println();
                  }
                  if(header.getMessageTypeId()==0) {
                	  NewsEvent event = xmls.getMap().get(header.getMessageCategoryId());
                	  //if(event!=null && "US_Pending_Home_Sales".compareToIgnoreCase(event.getName())==0) {
	                	  writer.write(date + " TypeID " + header.getMessageTypeId());
	                	  writer.write("\r\n");
	                	  writer.write(event==null ? "null" : event.toString());
	                	  writer.write("\r\n");
	                	  writer.write(payload.getIndicatorMessageFields().toString());
	                	  writer.write("\r\n");
	                	  writer.flush();
                	  //}
                  }
              }
              writer.close();
              content.close();
        } catch(Exception e) {
              e.printStackTrace();
        }
    }

	public static void main(String[] args) {
		new AlphaParser();
	}
	
	class AlphaNews extends HashMap<String,String> {
		
	}

}
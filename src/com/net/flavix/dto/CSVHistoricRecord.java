package com.net.flavix.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;



@Entity
public class CSVHistoricRecord extends Persister<CSVHistoricRecord> {
	
	/*
	category id
	category name
	date
	time
	message type
	message version
	datum id
	datum label
	field type
	value
	*/
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime dateTime;
	
	String categoryId = null;
	String categoryName = null;
	String messageType = null;
	String messageVersion = null;
	
	@Type(type = "ObjectArrayType")
	String[] values = null;
	
	@Transient
	DateTimeFormatter dTF = DateTimeFormatter.ofPattern("M/d/yyyy H:m:ss");
	
	
	public CSVHistoricRecord() {}
	
	public CSVHistoricRecord(String[] line) {
		categoryId = line[0];
		categoryName = line[1];
		String date = line[2];
		String time = line[3];
		dateTime = LocalDateTime.parse(date + " " + time,dTF);
		messageType = line[4];
		messageVersion = line[5];
		String[] aux = new String[1000];
		line = Arrays.copyOfRange(line, 6, line.length);
		for(int i=0;i<line.length;i++) {
			if(i%4==0 && line[i].length()>0) {
				/*
				for(String ss : line)
					System.out.print(ss + "||");
				System.out.println(">" + i + " " + line.length + " " + line[i]);
				*/
				try {
					aux[Integer.parseInt(line[i])] = line[i+3];
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		//trim array
		int lastIndex = 0;
		for(int i=0;i<aux.length;i++) {
			if(aux[i]!=null) {
				lastIndex = i;
			}
		}
		values = Arrays.copyOfRange(aux, 0, lastIndex+1);
		System.out.println(this);
	}
	
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(dateTime);
		sbuf.append(" CategoryId: ");
		sbuf.append(categoryId);
		sbuf.append(" Fields: { ");
		Iterator<String> it = Arrays.asList(values).iterator();
		while(it.hasNext()) {
			sbuf.append(it.next());
			if(it.hasNext())
				sbuf.append(", "); 
		}
		sbuf.append(" }");
		return sbuf.toString();
	}
	
}
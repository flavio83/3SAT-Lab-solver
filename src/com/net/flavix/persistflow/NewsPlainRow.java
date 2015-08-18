package com.net.flavix.persistflow;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.net.flavix.dto.LocalDateTimePersistenceConverter;
import com.net.flavix.dto.Persister;
import com.ntkn.messages.IndicatorMessage;
import com.ntkn.messages.IndicatorMessageField;
import com.ntkn.messages.IndicatorMessageHeader;
import com.ntkn.messages.IndicatorMessagePayload;



@Entity
@TypeDefs({ @TypeDef(name = "ObjectArrayType", typeClass = ObjectArrayType.class) })
public class NewsPlainRow extends Persister<NewsPlainRow> {
	
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(name = "id")
//	private long id;
	
	@Type(type="org.hibernate.type.PostgresUUIDType")
	@GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid")
    @Id
    private UUID uuid;
	
	@Column(columnDefinition="TIMESTAMP(3) NULL DEFAULT NULL")
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime date;
	
	long messageCRC= 0;
	int messageIdentifier= 0;
	int messageType= 0;
	int messageTxmitId = 0;
	
	@Type(type="text")
	String messageMultilineString = null;
	
	int messageHeaderCategoryId = 0;
	int messageHeaderIdentifier = 0;
	int messageHeaderLength = 0;
	int messageHeaderTxmitId = 0;
	int messageHeaderTypeId = 0;
	int messageHeaderVersion = 0;
	
	@Type(type = "ObjectArrayType")
	Object[] fields = null;
	
	byte[] messageBytes = null;
	byte[] indicatorMessageHeaderBytes = null;
	byte[] indicatorMessagePayloadBytes = null;

	public NewsPlainRow() {
		
	}
	
	public NewsPlainRow(Date date, IndicatorMessage msg) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
	    this.date = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
		//messageCountry = msg.getCountry();
		messageCRC = msg.getCRC();
		//messageMcastGroupName = msg.getMcastGroupName();
		messageIdentifier = msg.getMessageIdentifier();
		messageType = msg.getMessageType();
		//messageName = msg.getName();
		messageTxmitId = msg.getTxmitId();
		messageMultilineString = msg.toMultilineString();
		messageBytes = msg.getBytes();
		IndicatorMessageHeader imh = msg.getHeader();
		messageHeaderCategoryId = imh.getMessageCategoryId();
		messageHeaderIdentifier = imh.getMessageIdentifier();
		messageHeaderLength = imh.getMessageLength();
		messageHeaderTxmitId = imh.getMessageTxmitId();
		messageHeaderTypeId = imh.getMessageTypeId();
		messageHeaderVersion = imh.getMessageVersion();
		indicatorMessageHeaderBytes = imh.getBytes();
		IndicatorMessagePayload imp = msg.getPayload();
		List<IndicatorMessageField> payloadList = msg.getPayload().getIndicatorMessageFields();
		byte maxValue = -1;
		for(IndicatorMessageField field : payloadList) {
			if(maxValue<field.getFieldId())
				maxValue = field.getFieldId();
		}
		fields = new Object[maxValue+1];
		System.out.println(maxValue);
		for(int i=0;i<payloadList.size();i++) {
			IndicatorMessageField field = payloadList.get(i);
			fields[field.getFieldId()] = field.getfieldValue().toString();
		}
		indicatorMessagePayloadBytes = imp.getBytes();
	}
	
	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(date);
		sbuf.append(" CategoryId: ");
		sbuf.append(messageHeaderCategoryId);
		sbuf.append(" Fields: { ");
		Iterator<Object> it = Arrays.asList(fields).iterator();
		while(it.hasNext()) {
			sbuf.append(it.next());
			if(it.hasNext())
				sbuf.append(", "); 
		}
		sbuf.append(" }");
		return sbuf.toString();
	}

}

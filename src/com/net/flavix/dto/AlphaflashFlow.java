package com.net.flavix.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;

import org.hibernate.annotations.BatchSize;



@Entity
public class AlphaflashFlow extends Persister<AlphaflashFlow> {
	
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime acquiredDate;
	
	@Column
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime date;
	
	@Column(name = "category")
	private int category = -1;
	
	@Column(name = "type")
	private int type = -1;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "AlphaflashFlowFields", joinColumns = @JoinColumn(name = "event_id"))
	@MapKeyColumn(name = "field_key", length = 50)
	@Column(name = "field_val", length = 100)
	@BatchSize(size = 20)
	private Map<Integer, String> fields = new HashMap<>();

	@Column
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime dateTime;

	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(date);
		buf.append(" ");
		buf.append(category);
		buf.append(" ");
		buf.append(type==0?"RELEASE":"ESTIMATE");
		buf.append(" MAP: ");
		buf.append(toPrettyPrintMapString(fields));
		return buf.toString();
	}
	
	public String toPrettyPrintMapString(Map map) {
        StringBuilder sb = new StringBuilder();
        Iterator<Entry> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();
    }

	public AlphaflashFlow() {
		
	}
	
	public AlphaflashFlow(LocalDateTime date, int category, int type) {
		this.category = category;
		this.type = type;
		this.date = date;
		this.acquiredDate = LocalDateTime.now();
	}
	
	public static void persistWithRandomData() throws Exception {
		AlphaflashFlow c0 = new AlphaflashFlow(LocalDateTime.of(2015, 5, 14, 13, 30), 90, 0);
		AlphaflashFlow c1 = new AlphaflashFlow(LocalDateTime.of(2015, 5, 17, 23, 30), 90, 1);
		AlphaflashFlow c2 = new AlphaflashFlow(LocalDateTime.of(2015, 5, 18, 13, 30), 90, 0);
		AlphaflashFlow c3 = new AlphaflashFlow(LocalDateTime.of(2015, 5, 20, 23, 30), 20003, 1);
		AlphaflashFlow c4 = new AlphaflashFlow(LocalDateTime.of(2015, 5, 22, 9, 30), 20003, 0);
		c0.setField(1, "2");
		c1.setField(1, "4");
		c2.setField(1, "5");
		c3.setField(1, "45.7");
		c4.setField(1, "48.4");
		new AlphaflashFlow().persist(c0,c1,c2,c3,c4);
	}

	public LocalDateTime getAcquiredDate() {
		return acquiredDate;
	}

	public void setAcquiredDate(LocalDateTime acquiredDate) {
		this.acquiredDate = acquiredDate;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Map<Integer, String> getFields() {
		return fields;
	}
	
	public void setField(int i, String field) {
		fields.put(i, String.valueOf(field));
	}

	public void setFields(Map<Integer, String> fields) {
		this.fields = fields;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

}

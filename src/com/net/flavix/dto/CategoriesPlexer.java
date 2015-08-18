package com.net.flavix.dto;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;

import org.hibernate.annotations.BatchSize;



@Entity
public class CategoriesPlexer extends Persister<CategoriesPlexer> {
	
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "category")
	private int category = -1;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "CategoriesPlexerFields", joinColumns = @JoinColumn(name = "event_id"))
	@MapKeyColumn(name = "field_key", length = 50)
	@Column(name = "field_val", length = 100)
	@BatchSize(size = 20)
	private Map<Integer, String> fields = new HashMap<>();

	
	public CategoriesPlexer(int category) {
		this.category = category;
	}
	
	public CategoriesPlexer() {
		
	}
	
	public static void persistWithRandomData() throws Exception {
		CategoriesPlexer c1 = new CategoriesPlexer(90);
		c1.setField(1, ">");
		CategoriesPlexer c2 = new CategoriesPlexer(20003);
		c2.setField(2, "<");
		new CategoriesPlexer().persist(c1,c2);
	}
	
	public void setField(int key, String value) {
		fields.put(key, value);
	}
	
	public void setField(int key, Double value) {
		fields.put(key, String.valueOf(value));
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public Map<Integer, String> getFields() {
		return fields;
	}

	public void setFields(Map<Integer, String> fields) {
		this.fields = fields;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cat plex ");
		builder.append(category);
		builder.append(" ");
		builder.append(toPrettyPrintMapString(fields));
		return builder.toString();
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

}

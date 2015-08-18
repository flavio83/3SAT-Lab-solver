package com.net.flavix.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.ntkn.calendar.CalendarEventExt;



@Entity
@Table(uniqueConstraints=
	@UniqueConstraint(columnNames = {"date", "category", "type", "addedBy", "country", "title", "uid"})) 
public class AlphaflashCalendar extends Persister<AlphaflashCalendar> implements Comparable<AlphaflashCalendar> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime acquiredDate;
	
	@Column(columnDefinition="TIMESTAMP(3) NULL DEFAULT NULL")
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime removedDate;

	@Column
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime date;
	
	@Column(name = "category")
	private int category = -1;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "addedBy")
	private String addedBy;
	
	@Column(name = "uid")
	private String uid;
	
	
	public AlphaflashCalendar() {
		
	}
	
	public AlphaflashCalendar(LocalDateTime date, int category, int type) {
		this.category = category;
		this.type = String.valueOf(type);
		this.date = date;
		this.acquiredDate = LocalDateTime.now();
	}
	
	
	
	public AlphaflashCalendar(CalendarEventExt event) {
		Instant instant = Instant.ofEpochMilli(event.getDate().getDate().getTime().getTime());
	    this.date = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
	    this.acquiredDate = LocalDateTime.now();
	    category = event.getCategoryId();
		addedBy = event.getAddedBy();
		country = event.getCountry();
		title = event.getTitle();
		uid = event.getUid();
		type = event.getType().getValue();
	}
	
	public static void persistWithRandomData() throws Exception {
		AlphaflashCalendar c1 = new AlphaflashCalendar(LocalDateTime.of(2015, 5, 17, 23, 30), 90, 1);
		AlphaflashCalendar c2 = new AlphaflashCalendar(LocalDateTime.of(2015, 5, 18, 13, 30), 90, 0);
		AlphaflashCalendar c3 = new AlphaflashCalendar(LocalDateTime.of(2015, 5, 20, 23, 30), 20003, 1);
		AlphaflashCalendar c4 = new AlphaflashCalendar(LocalDateTime.of(2015, 5, 22, 9, 30), 20003, 0);
		new AlphaflashCalendar().persist(c1,c2,c3,c4);
	}
	
	public int compareTo(AlphaflashCalendar other) {
	    return this.date.compareTo(other.getDate());
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(date);
		str.append(" ");
		str.append(country);
		str.append(" ");
		str.append(title);
		return str.toString();
	}
	

	@Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
    		append(category).
            append(date).
            append(type).
            append(addedBy).
            append(country).
            append(title).
            append(uid).
            toHashCode();
    }
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AlphaflashCalendar))
            return false;
        if (obj == this)
            return true;

        AlphaflashCalendar rhs = (AlphaflashCalendar) obj;
        return new EqualsBuilder().
            // if deriving: appendSuper(super.equals(obj)).
            append(category, rhs.category).
            append(date, rhs.date).
            append(type, rhs.type).
            append(addedBy, rhs.addedBy).
            append(country, rhs.country).
            append(title, rhs.title).
            append(uid, rhs.uid).
            isEquals();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public LocalDateTime getRemovedDate() {
		return removedDate;
	}

	public void setRemovedDate(LocalDateTime removedDate) {
		this.removedDate = removedDate;
	}
	
}

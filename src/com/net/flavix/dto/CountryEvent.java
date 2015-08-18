package com.net.flavix.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.BatchSize;



@Entity
public class CountryEvent {
	
	@Id
    @GeneratedValue
    @Column(name = "id")
    private long id;
	
	@Column(name = "category")
	private int category;
	
	@Column(name = "toEnter")
	private boolean toEnter = false;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "raw_events_custom", joinColumns = @JoinColumn(name = "raw_event_id"))
	@MapKeyColumn(name = "field_key", length = 50)
	@Column(name = "field_val", length = 100)
	@BatchSize(size = 20)
	Map<Integer,Double> fields = new HashMap<Integer,Double>();
	
	@Column
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime dateTime;

	public CountryEvent() {
		/*
		category = 10;
		dateTime = LocalDateTime.now(Clock.systemUTC());
		for(int i=0;i<1000;i++)
			fields.put(new Random().nextInt(), new Random().nextDouble());
		*/
	}
	
	public static void persistTupla(Session session) throws Exception {
		try {
			Transaction transaction = session.getTransaction();
			transaction.begin();
			CountryEvent tupla = new CountryEvent();
			session.save(tupla);
			tupla = new CountryEvent();
			session.save(tupla);
			transaction.commit();
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			throw e;
		}
	}
	
	public static void persistTupla(Session session, CountryEvent t) throws Exception {
		try {
			Transaction transaction = session.getTransaction();
			transaction.begin();
			session.save(t);
			transaction.commit();
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			throw e;
		}
	}
	
	public static List<CountryEvent> loadAll(Session session) throws Exception {
		try {
			Criteria criteria = session.createCriteria(CountryEvent.class);
			List list = criteria.list();
			List<CountryEvent> listt = new ArrayList<CountryEvent>(list.size());
			for(Object o : list) {
				listt.add((CountryEvent)o);
			}
			return listt;
		} catch (Exception e) {
			if (session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			throw e;
		}
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}
	
	public Map<Integer,Double> getFieldsMap() {
		return fields;
	}

	public Double getFields(int key) {
		return fields.get(key);
	}

	public void setFields(Integer key, Double value) {
		this.fields.put(key, value);
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	
}

package com.net.flavix.dto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;



@Entity
public class OandaForexHistorical extends Persister<OandaForexHistorical> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime dateTime;
	
	@Column(name = "nation")
	private String nation = null;
	
	@Column(name = "pair")
	private String pair = null;
	
	@Column(name = "category")
	private int category = -1;
	
	@Type(type = "ObjectArrayType")
	private Double[] asks = null;
	
	@Type(type = "ObjectArrayType")
	private Double[] bids = null;

	public OandaForexHistorical() {}
	
	public OandaForexHistorical(
			LocalDateTime dateTime, 
			String nation, 
			String pair, 
			int category, 
			double asks[], 
			double[] bids) {
		
		this.dateTime = dateTime;
		this.nation = nation;
		this.pair = pair;
		this.category = category;
		this.asks = toDoubleArray(asks);
		this.bids = toDoubleArray(bids);
	}
	
	private Double[] toDoubleArray(double[] array) {
		//Stream.of(bids).mapToDouble(d -> d).toArray();
		int count = 0;
		Double[] a = new Double[array.length];
		for(double v : array) {
			a[count++] = v;
		}
		return a;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(dateTime);
		str.append(" ");
		str.append(nation);
		str.append(" ");
		str.append(pair);
		str.append(" ");
		str.append(new BigDecimal(asks[asks.length-1]-asks[0],
				new MathContext(5,RoundingMode.HALF_UP)).toPlainString());
		return str.toString();
	}

}

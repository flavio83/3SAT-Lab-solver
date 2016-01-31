package com.ntkn.flavix;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.ntkn.messages.evnveloped.IndicatorMessageEnvelope;




public class CurrencyEvent {
	
	
	private transient final Logger logger = LoggerFactory.getLogger(CurrencyEvent.class);
	
	private String currency;
	private LocalDateTime dateTime;
	private List<NewsEvent> newsEvent;
	private Map<String,Double> pairs = null;
	
	private transient List<EventResult> eventResults;
	private transient EventResult result = EventResult.NOT_EVALUATED;

	public CurrencyEvent(String currency, LocalDateTime date) {
		this.dateTime = date;
		this.setCurrency(currency);
		newsEvent = new ArrayList<NewsEvent>();
		eventResults = new ArrayList<EventResult>();
	}
	
	public CurrencyEvent(String currency, Map<String,Double> pairs, LocalDateTime date) {
		this.dateTime = date;
		this.pairs = pairs;
		this.setCurrency(currency);
		newsEvent = new ArrayList<NewsEvent>();
		eventResults = new ArrayList<EventResult>();
	}
	
	public Set<String> getPairs() {
		return pairs.keySet();
	}
	
	public Double getDelta(String pair) {
		return pairs.get(pair);
	}
	
	public boolean isLong() {
		return EventResult.EVALUATION_PASSED_AS_FIRST_EVALUATION_FOR_LONG_ENTRY.equals(result);
	}
	
	public void reset() {
		result = EventResult.NOT_EVALUATED;
		for(NewsEvent event : newsEvent) {
			event.reset();
			eventResults.clear();
		}
	}
	
	public EventResult setEvaluation(EventResult state) {
		if(result.equals(EventResult.NOT_EVALUATED)) {
			result = state;
		} else {
			if(result.equals(EventResult.EVALUATION_PASSED_AS_FIRST_EVALUATION_FOR_LONG_ENTRY)) {
				result = EventResult.ALREADY_EVALUATED;
			} else if(result.equals(EventResult.EVALUATION_PASSED_AS_FIRST_EVALUATION_FOR_SHORT_ENTRY)) {
				result = EventResult.ALREADY_EVALUATED;
			}
		}
		return state;
	}
	
	public void addNews(NewsEvent event) {
		newsEvent.add(event);
	}
	
	public boolean isPassed(EventResult expected) {
		for(NewsEvent news : newsEvent) {
			if(!news.getResult().toString().contains("EVALUATION_PASSED")) {
				return false;
			}
		}
		boolean returnvalue = setEvaluation(EventResult.EVALUATION_PASSED_AS_FIRST_EVALUATION_FOR_LONG_ENTRY).equals(expected) || 
				setEvaluation(EventResult.EVALUATION_PASSED_AS_FIRST_EVALUATION_FOR_SHORT_ENTRY).equals(expected);
		
		return returnvalue;
	}
	
	
	public boolean isPassed() {
		for(NewsEvent news : newsEvent) {
			if(!news.getResult().toString().contains("EVALUATION_PASSED")) {
				return false;
			}
		}
		//check for collisions
		EventResult first = newsEvent.get(0).getResult();
		for(int i=1;i<newsEvent.size();i++) {
			if(!newsEvent.get(i).getResult().equals(first)) {
				logger.info("CurrencyEvent NOT passed: " + this);
				return false;
			}
		}
		setEvaluation(first);
		logger.info("CurrencyEvent passed: " + this);
		return true;
	}
	
	public void onEvent(IndicatorMessageEnvelope msg) {	
		if(!EventResult.NOT_EVALUATED.equals(result)) {
			result = EventResult.ALREADY_EVALUATED;
		}
		for(NewsEvent news : newsEvent) {
			if(news.onEvent(msg).toString().contains("EVALUATION_PASSED")) {
				eventResults.add(news.getResult());
			}
		}
	}

	public LocalDateTime getDate() {
		return dateTime;
	}

	public void setDate(LocalDateTime date) {
		this.dateTime = date;
	}

	public EventResult getResult() {
		return result;
	}

	public void setResult(EventResult result) {
		this.result = result;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public void setPairs(List<String> fxpairs) {
		pairs = new HashMap<String,Double>();
		for(String pair : fxpairs) {
			pairs.put(pair, 0d);
		}
	}

	public void setPairs(Map<String, Double> pairs) {
		this.pairs = pairs;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("CurrencyEvent: ");
		buf.append(currency);
		buf.append(", ");
		buf.append("numEvents: ");
		buf.append(newsEvent);
		buf.append(", ");
		buf.append("Date: ");
		buf.append(dateTime);
		buf.append(", ");
		buf.append("Pairs: ");
		if(pairs!=null) {
			for(String pair : pairs.keySet()) {
				buf.append(pair);
				buf.append("[");
				buf.append(pairs.get(pair));
				buf.append("]");
				buf.append(" - ");
			}
		}
		buf.append(", ");
		buf.append("State: ");
		buf.append(result);
		return buf.toString();
	}
	
	public String serializeAsJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public static CurrencyEvent deserializeAsJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, CurrencyEvent.class);
	}

}

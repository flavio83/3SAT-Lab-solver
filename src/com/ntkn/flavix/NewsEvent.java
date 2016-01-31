package com.ntkn.flavix;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ntkn.messages.evnveloped.IndicatorMessageEnvelope;
import com.ntkn.messages.evnveloped.IndicatorMessageFieldEnvelope;




public class NewsEvent {
	
	private transient final Logger logger = LoggerFactory.getLogger(NewsEvent.class);
	
	private transient final int type = 0;
	
	private int category;
	private String currency;
	private LocalDateTime dateTime;
	private List<Field> fields;
	
	private transient EventResult result = EventResult.NOT_EVALUATED;

	public NewsEvent(int category) {
		this.dateTime = LocalDateTime.now();
		this.category = category;
		fields = new ArrayList<Field>();
	}
	
	public NewsEvent(LocalDateTime dateTime, int category, String currency) {
		this.dateTime = dateTime;
		this.setCurrency(currency);
		this.category = category;
		fields = new ArrayList<Field>();
	}
	
	public void reset() {
		result = EventResult.NOT_EVALUATED;
		for(Field field : fields) {
			field.reset();
		}
	}
	
	public boolean isPassed() {
		return result.toString().contains("EVALUATION_PASSED");
	}
	
	public EventResult getResult() {
		return result;
	}
	
	public void addDatum(int index, double value, boolean greatherThan) {
		addField(index,value,greatherThan);
	}

	public void addField(int index, double value, boolean greatherThan) {
		fields.add(new Field(index,value,greatherThan));
	}
	
	public EventResult onEvent(IndicatorMessageEnvelope msg) {
		if(msg.getHeader().getMessageTypeId()==type 
				&& msg.getHeader().getMessageCategoryId()==category) {
			
			List<IndicatorMessageFieldEnvelope> payload = msg.getPayload().getIndicatorMessageFields();
			for(Field field : fields) {
				double actualValue = Double.parseDouble(payload.get(field.getIndex()-1).getfieldValue().toString());
				logger.info("test field " + field + " against value " + actualValue);
				if(actualValue == field.getValue()) {
					field.setEvaluation(EventResult.EVALUATION_PASSED_AS_FIRST_EVALUATION_FOR_FLAT_ENTRY);
				} else if(actualValue > field.getValue() && field.isGreaterThan()) {
					field.setEvaluation(EventResult.EVALUATION_PASSED_AS_FIRST_EVALUATION_FOR_LONG_ENTRY);
				} else if (actualValue < field.getValue() && !field.isGreaterThan()) {
					field.setEvaluation(EventResult.EVALUATION_PASSED_AS_FIRST_EVALUATION_FOR_LONG_ENTRY);
				} else if (actualValue > (field.getValue()+field.getSpread()) && !field.isGreaterThan()) {
					field.setEvaluation(EventResult.EVALUATION_PASSED_AS_FIRST_EVALUATION_FOR_SHORT_ENTRY);
				} else if (actualValue < (field.getValue()-field.getSpread()) && field.isGreaterThan()) {
					field.setEvaluation(EventResult.EVALUATION_PASSED_AS_FIRST_EVALUATION_FOR_SHORT_ENTRY);
				}
				field.setActualValue(actualValue);
				logger.info("field evaluated and passed: " + field);
			}
			EventResult first = fields.get(0).getResult();
			for(int i=0;i<fields.size();i++) {
				if(!fields.get(i).getResult().equals(first) 
						|| fields.get(i).getResult().name().contains("FLAT")) {
					logger.info("CurrencyEvent NOT passed: " + this);
					return setEvaluation(EventResult.EVALUATION_NOTPASSED);
				}
			}
			return setEvaluation(fields.get(0).getResult());
		}
		return EventResult.NOT_EVALUATED;
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
	
	public int getCategory() {
		return category;
	}
	
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("Category: ");
		buf.append(category);
		buf.append("Datetime: ");
		buf.append(dateTime);
		buf.append(" Field's list:");
		buf.append("\r\n");
		for(Field field : fields) {
			buf.append(field.toString());
			buf.append("\r\n");
		}
		return buf.toString();
	}

}

class Field {
	
	private int index;
	private double value;
	private double spread;
	private boolean greaterThan;
	
	private transient double actualValue;
	private transient EventResult result = EventResult.NOT_EVALUATED;
	
	public Field(int index, double value, boolean greaterThan) {
		super();
		this.index = index;
		this.value = value;
		this.greaterThan = greaterThan;
	}
	
	public EventResult getResult() {
		return result;
	}
	
	public void reset() {
		result = EventResult.NOT_EVALUATED;
	}
	
	public boolean isEvaluated() {
		return !result.equals(EventResult.NOT_EVALUATED);
	}
	
	public boolean isPassed() {
		return result.toString().contains("EVALUATION_PASSED");
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public double getSpread() {
		return spread;
	}

	public void setSpread(double spread) {
		this.spread = spread;
	}

	public boolean isGreaterThan() {
		return greaterThan;
	}

	public void setGreaterThan(boolean greaterThan) {
		this.greaterThan = greaterThan;
	}

	public double getActualValue() {
		return actualValue;
	}

	public void setActualValue(double actualValue) {
		this.actualValue = actualValue;
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("Field: ");
		buf.append(index);
		buf.append(", predicted: ");
		buf.append(value);
		buf.append(", actualValue: ");
		buf.append(actualValue);
		buf.append(", greaterThan: ");
		buf.append(greaterThan);
		buf.append(", status: ");
		buf.append(result);
		return buf.toString();
	}
	
}

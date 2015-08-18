package com.ntkn.messages.evnveloped;

import com.ntkn.messages.IndicatorMessage;




public class IndicatorMessageEnvelope {
	
	/*
	 * if(msg.getHeader().getMessageTypeId()==type 
				&& msg.getHeader().getMessageCategoryId()==category) {
			
			List<IndicatorMessageField> payload = msg.getPayload().getIndicatorMessageFields();
			for(Field field : fields) {
				double fieldValue = Double.parseDouble(payload.get(field.getIndex()-1)
	 */
	
	private int type;
	private int category;
	
	private IndicatorMessage indicator = null;
	
	private IndicatorMessagePayloadEnvelope payload = null;

	public IndicatorMessageEnvelope(IndicatorMessage indicator) {
		this.indicator = indicator;
		this.payload = new IndicatorMessagePayloadEnvelope(indicator.getPayload());;
	}
	
	public IndicatorMessageEnvelope(int type, int category, String... fields) {
		this.type = type;
		this.category = category;
		this.payload = new IndicatorMessagePayloadEnvelope(fields);
	}
	
	public IndicatorMessageHeaderEnvelope getHeader() {
		if(indicator!=null) {
			return new IndicatorMessageHeaderEnvelope(indicator.getHeader());
		} else {
			return new IndicatorMessageHeaderEnvelope(type,category);
		}
	}
	
	public IndicatorMessagePayloadEnvelope getPayload() {
		return payload;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(category);
		s.append(" Type: ");
		s.append(type);
		return s.toString();
	}

}

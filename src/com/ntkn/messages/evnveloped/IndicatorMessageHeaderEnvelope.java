package com.ntkn.messages.evnveloped;

import com.ntkn.messages.IndicatorMessageHeader;




public class IndicatorMessageHeaderEnvelope {
	
	/*
	 * if(msg.getHeader().getMessageTypeId()==type 
				&& msg.getHeader().getMessageCategoryId()==category) {
			
			List<IndicatorMessageField> payload = msg.getPayload().getIndicatorMessageFields();
			for(Field field : fields) {
				double fieldValue = Double.parseDouble(payload.get(field.getIndex()-1)
	 */
	
	private int type;
	private int category;

	public IndicatorMessageHeaderEnvelope(IndicatorMessageHeader header) {
		this.type = header.getMessageTypeId();
		this.category = header.getMessageCategoryId();
	}
	
	
	public IndicatorMessageHeaderEnvelope(int type, int category) {
		this.type = type;
		this.category = category;
	}
	
	public int getMessageTypeId() {
		return type;
	}
	
	public int getMessageCategoryId() {
		return category;
	}

}

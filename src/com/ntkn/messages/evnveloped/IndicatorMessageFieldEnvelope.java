package com.ntkn.messages.evnveloped;

import com.ntkn.messages.IndicatorMessageField;



public class IndicatorMessageFieldEnvelope {
	
	private String value;

	public IndicatorMessageFieldEnvelope(IndicatorMessageField field) {
		this.value = field.getfieldValue().toString();
	}
	
	public IndicatorMessageFieldEnvelope(String field) {
		this.value = field;
	}
	
	public String getfieldValue() {
		return value;
	}

}

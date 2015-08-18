package com.ntkn.messages.evnveloped;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ntkn.messages.IndicatorMessageField;
import com.ntkn.messages.IndicatorMessagePayload;




public class IndicatorMessagePayloadEnvelope {
	
	private List<IndicatorMessageFieldEnvelope> list = null;

	public IndicatorMessagePayloadEnvelope(IndicatorMessagePayload payload) {
		list = new ArrayList<>();
		for(IndicatorMessageField field : payload.getIndicatorMessageFields()) {
			list.add(new IndicatorMessageFieldEnvelope(field));
		}
	}
	
	public IndicatorMessagePayloadEnvelope(String... fields) {
		list = new ArrayList<>();
		for(String field : fields) {
			list.add(new IndicatorMessageFieldEnvelope(field));
		}
	}
	
	public List<IndicatorMessageFieldEnvelope> getIndicatorMessageFields(){
		return list;
	}
	
	public IndicatorMessageFieldEnvelope get(int index) {
		return list.get(index);
	}

}

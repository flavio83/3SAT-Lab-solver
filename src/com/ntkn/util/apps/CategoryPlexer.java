package com.ntkn.util.apps;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ntkn.util.apps.EconomicEvent.StringEventDeserializer;
import com.ntkn.util.apps.EconomicEvent.StringEventSerializer;




public class CategoryPlexer {
	
	private String category = null;
	private String description = null;
	private String nation = null;
	private String currency = null;
	private Map<Integer,String> fields = null;

	public CategoryPlexer(
			String category, 
			String description, 
			String nation, 
			String currency,
			Map<Integer, String> fields) {
		
		this.category = category;
		this.description = description;
		this.nation = nation;
		this.currency = currency;
		this.fields = fields;
	}

	public String getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	public String getNation() {
		return nation;
	}

	public String getCurrency() {
		return currency;
	}

	public Map<Integer, String> getFields() {
		return fields;
	}
	
	public static Gson createGson() {
		Gson gson = new GsonBuilder().setPrettyPrinting()
	    		.registerTypeAdapter(
	    			EconomicEvent.class, 
	    			new StringEventSerializer())
	    		.registerTypeAdapter(
	    			EconomicEvent.class, 
	    			new StringEventDeserializer()).create();
		return gson;
	}
}

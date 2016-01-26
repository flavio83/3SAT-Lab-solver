package com.ntkn.util.apps;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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




public class EconomicEvent {
	
	private LocalDateTime dateTime = null;
	
	private String actual;
	private String forecast;
	private String previous;
	
	public EconomicEvent(String actual, String forecast, String previous) {
		dateTime = LocalDateTime.now();
		this.actual = actual;
		this.forecast = forecast;
		this.previous = previous;
	}
	
	public EconomicEvent(LocalDateTime dateTime, String actual, String forecast, String previous) {
		this.dateTime = dateTime;
		this.actual = actual;
		this.forecast = forecast;
		this.previous = previous;
	}
	
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	public String getActual() {
		return actual;
	}
	
	public String getForecast() {
		return forecast;
	}
	
	public String getPrevious() {
		return previous;
	}
	
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM d yyyy");
	
	public static class StringEventSerializer implements JsonSerializer<EconomicEvent> {
        public JsonElement serialize(final EconomicEvent eevent, final Type type, final JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.add("datetime", new JsonPrimitive(eevent.getDateTime().format(dtf)));
            result.add("actual", new JsonPrimitive(eevent.getActual()));
            result.add("forecast", new JsonPrimitive(eevent.getForecast()));
            result.add("previous", new JsonPrimitive(eevent.getPrevious()));
            return result;
        }
    }
	
	public static class StringEventDeserializer implements JsonDeserializer<EconomicEvent> {
		@Override
		public EconomicEvent deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) throws JsonParseException {
			final JsonObject jsonObject = json.getAsJsonObject();
			final LocalDate lDate = LocalDate.parse(jsonObject.get("datetime").getAsString().replace(',', ' '),dtf);
		    final LocalTime lTime = LocalTime.of(9, 30);
			final LocalDateTime ldt = LocalDateTime.of(lDate, lTime);
		    final String actual = jsonObject.get("actual").getAsString();
		    final String forecast = jsonObject.get("forecast").getAsString();
		    final String previous = jsonObject.get("previous").getAsString();
		    return new EconomicEvent(ldt,actual, forecast, previous);
		}
    }
	
	public static class CustomToStringEasyStyle extends ToStringStyle {
		
		private static final long serialVersionUID = 1L;

		public CustomToStringEasyStyle() {
			setUseClassName(false);
			setUseIdentityHashCode(false);
			setUseShortClassName(false);
			setUseFieldNames(false);
		}

		protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
			if(value instanceof LocalDateTime) {
				buffer.append(((LocalDateTime)value).format(dtf));
		    } else {
				buffer.append(value);
			}
		}
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
	
	@Override
    public String toString() {
		return ToStringBuilder.reflectionToString(this, new CustomToStringEasyStyle());
    }

}

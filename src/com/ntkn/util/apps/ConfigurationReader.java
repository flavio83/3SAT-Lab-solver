package com.ntkn.util.apps;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;



public class ConfigurationReader {

	public ConfigurationReader() {
		List<EconomicEvent> list = new ArrayList<>();
		Type typeList = new TypeToken<List<EconomicEvent>>(){}.getType();
		EconomicEvent p = new EconomicEvent("p","v","d");
		list.add(p);
		list.add(p);
	    Gson gson = EconomicEvent.createGson();
	    System.out.println(gson.toJson(list));
	    List<EconomicEvent> aux = gson.fromJson(gson.toJson(list), typeList);
	    System.out.println(aux);
	    
	    Map<Integer,String> map = new HashMap<Integer,String>();
	    map.put(4, ">");
	    map.put(6, "<");
 	    CategoryPlexer cp = new CategoryPlexer("40","US40Unemployment.txt","US","USD",map);
 	    gson = new GsonBuilder().enableComplexMapKeySerialization()
 		        .setPrettyPrinting().create();
 	    
 	   System.out.println(gson.toJson(cp));
	}
	
	public static void main(String[] args) {
		try {
			new ConfigurationReader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

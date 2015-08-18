package com.ntkn.util.apps;

import java.util.List;

import com.ntkn.flavix.CurrencyEvent;




public class TestTupla {

	public TestTupla() {
		try {
			List<CurrencyEvent> events = LoadCountryEvents.getLoader().load();
			events.forEach(name -> System.out.println(name));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new TestTupla();
	}

}

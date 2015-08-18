package com.net.testng.protocol;

import static org.testng.Assert.*;

import java.time.LocalDateTime;

import org.testng.annotations.Test;

import com.ntkn.flavix.CurrencyEvent;
import com.ntkn.flavix.EventResult;
import com.ntkn.flavix.NewsEvent;
import com.ntkn.flavix.enums.Pair;
import com.ntkn.messages.evnveloped.IndicatorMessageEnvelope;




public class TestCurrencyEvent {

	@Test
	public void straightforword() {
		DukasConsoleForTest dukas = new DukasConsoleForTest();
		LocalDateTime now = LocalDateTime.now();
		CurrencyEvent currencyEvent = new CurrencyEvent("EUR",now.plusMinutes(2));
		NewsEvent news = new NewsEvent(5);
		news.addField(1, 2.8d, true);
		currencyEvent.addNews(news);
		dukas.updateList(currencyEvent);
		IndicatorMessageEnvelope msg = new IndicatorMessageEnvelope(0,5,"2.85");
		dukas.onEvent(msg);
		assertEquals(dukas.getStatus(),DukasConsoleStatus.ENTERED);
	}
	
	@Test
	public void straightforwordOutOfTime() {
		DukasConsoleForTest dukas = new DukasConsoleForTest();
		LocalDateTime now = LocalDateTime.now();
		CurrencyEvent currencyEvent = new CurrencyEvent("EUR",now.plusMinutes(4));
		NewsEvent news = new NewsEvent(5);
		news.addField(1, 2.8d, true);
		currencyEvent.addNews(news);
		dukas.updateList(currencyEvent);
		IndicatorMessageEnvelope msg = new IndicatorMessageEnvelope(0,5,"2.85");
		dukas.onEvent(msg);
		assertEquals(dukas.getStatus(),DukasConsoleStatus.OUT_OF_TIME);
	}
	
	@Test
	public void straightforwordTestLong() {
		DukasConsoleForTest dukas = new DukasConsoleForTest();
		LocalDateTime now = LocalDateTime.now();
		CurrencyEvent currencyEvent = new CurrencyEvent("EUR",now);
		NewsEvent news = new NewsEvent(5);
		news.addField(1, 2.8d, true);
		currencyEvent.addNews(news);
		dukas.updateList(currencyEvent);
		IndicatorMessageEnvelope msg = new IndicatorMessageEnvelope(0,5,"2.85");
		dukas.onEvent(msg);
		assertEquals(dukas.getStatus(),DukasConsoleStatus.ENTERED);
		assertEquals(Pair.EURUSD, dukas.getPair());
		assertTrue(dukas.isLong());
	}
	
	@Test
	public void straightforwordTestShort() {
		DukasConsoleForTest dukas = new DukasConsoleForTest();
		LocalDateTime now = LocalDateTime.now();
		CurrencyEvent currencyEvent = new CurrencyEvent("EUR",now);
		NewsEvent news = new NewsEvent(5);
		news.addField(1, 2.8d, true);
		currencyEvent.addNews(news);
		dukas.updateList(currencyEvent);
		IndicatorMessageEnvelope msg = new IndicatorMessageEnvelope(0,5,"2.75");
		dukas.onEvent(msg);
		assertEquals(dukas.getStatus(),DukasConsoleStatus.ENTERED);
		assertEquals(Pair.EURUSD, dukas.getPair());
		assertFalse(dukas.isLong());
	}
	
	@Test
	public void twoNewsPerEventTestShort() {
		DukasConsoleForTest dukas = new DukasConsoleForTest();
		LocalDateTime now = LocalDateTime.now();
		CurrencyEvent currencyEvent = new CurrencyEvent("USD",now);
		NewsEvent news = new NewsEvent(5);
		news.addField(1, 2.8d, false);
		currencyEvent.addNews(news);
		news = new NewsEvent(15);
		news.addField(1, 0.1d, false);
		currencyEvent.addNews(news);
		dukas.updateList(currencyEvent);
		IndicatorMessageEnvelope msg = new IndicatorMessageEnvelope(0,5,"2.65");
		dukas.onEvent(msg);
		msg = new IndicatorMessageEnvelope(0,15,"0.05");
		dukas.onEvent(msg);
		assertEquals(dukas.getStatus(),DukasConsoleStatus.ENTERED);
		assertEquals(Pair.EURUSD, dukas.getPair());
		assertFalse(dukas.isLong());
	}
	
	@Test
	public void twoFieldPerNewsTestLongPlusEventAlreadyEvaluatedTest() {
		DukasConsoleForTest dukas = new DukasConsoleForTest();
		LocalDateTime now = LocalDateTime.now();
		CurrencyEvent currencyEvent = new CurrencyEvent("USD",now);
		NewsEvent news = new NewsEvent(5);
		news.addField(1, 6.5d, false);
		news.addField(2, 0.1d, true);
		currencyEvent.addNews(news);
		dukas.updateList(currencyEvent);
		IndicatorMessageEnvelope msg = new IndicatorMessageEnvelope(0,5,"4.65","0.2");
		dukas.onEvent(msg);
		assertEquals(dukas.getStatus(),DukasConsoleStatus.ENTERED);
		assertEquals(Pair.EURUSD, dukas.getPair());
		assertFalse(dukas.isLong());
		msg = new IndicatorMessageEnvelope(0,5,"9.9","0.4");
		dukas.onEvent(msg);
		assertEquals(EventResult.ALREADY_EVALUATED,currencyEvent.getResult());
	}
	
	@Test
	public void twoFieldPerTwoNewsTestLongPlusEventAlreadyEvaluatedTest() {
		DukasConsoleForTest dukas = new DukasConsoleForTest();
		LocalDateTime now = LocalDateTime.now();
		CurrencyEvent currencyEvent = new CurrencyEvent("USD",now);
		NewsEvent news = new NewsEvent(5);
		news.addField(1, 6.5d, false);
		news.addField(2, 0.1d, true);
		news = new NewsEvent(25);
		news.addField(1, 2.5d, false);
		news.addField(3, 3.1d, true);
		currencyEvent.addNews(news);
		dukas.updateList(currencyEvent);
		IndicatorMessageEnvelope msg = new IndicatorMessageEnvelope(0,5,"4.65","0.2");
		dukas.onEvent(msg);
		msg = new IndicatorMessageEnvelope(0,25,"2.4","NULL","3.2");
		dukas.onEvent(msg);
		assertEquals(dukas.getStatus(),DukasConsoleStatus.ENTERED);
		assertEquals(Pair.EURUSD, dukas.getPair());
		assertFalse(dukas.isLong());
		msg = new IndicatorMessageEnvelope(0,5,"9.9","0.4");
		dukas.onEvent(msg);
		assertEquals(EventResult.ALREADY_EVALUATED,currencyEvent.getResult());
	}

}

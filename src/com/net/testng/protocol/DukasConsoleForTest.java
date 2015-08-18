package com.net.testng.protocol;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ntkn.flavix.CurrencyEvent;
import com.ntkn.flavix.enums.Currency;
import com.ntkn.flavix.enums.Pair;
import com.ntkn.messages.IndicatorMessage;
import com.ntkn.messages.evnveloped.IndicatorMessageEnvelope;




public class DukasConsoleForTest {
	
	private Timer time = new Timer();
	private DukasConsoleStatus status = DukasConsoleStatus.WAITING;
	private List<CurrencyEvent> eventList = new CopyOnWriteArrayList<>();
	
	private final Logger logger = LoggerFactory.getLogger(DukasConsoleForTest.class);

	public DukasConsoleForTest() {
		//this.context = context;
	}
	
	protected void setInWaiting() {
		if(DukasConsoleStatus.OUT_OF_TIME.equals(status)) {
			status = DukasConsoleStatus.WAITING;
		}
	}
	
	protected void setOutOfTime() {
		if(DukasConsoleStatus.WAITING.equals(status)) {
			status = DukasConsoleStatus.OUT_OF_TIME;
		}
	}
	
	public synchronized void updateList(CurrencyEvent... cEvent) {
		updateList(Arrays.asList(cEvent));
	}
	
	public synchronized void updateList(List<CurrencyEvent> cEvent) {
		logger.info("received CurrencyEvent list of " + cEvent.size() + " elements");
		eventList.clear();
		eventList.addAll(cEvent);
		for(CurrencyEvent event : cEvent) {
			//time.schedule(new DukasConsoleTask(this, true), toDate(event.getDate().minusMinutes(3)));
			//time.schedule(new DukasConsoleTask(this, false),toDate(event.getDate().plusMinutes(3)));
			logger.info("added " + event);
		}
	}
	
	private Date toDate(LocalDateTime ldt) {
		Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}
	
	public void onEvent(IndicatorMessage msg) {
		onEvent(new IndicatorMessageEnvelope(msg));
	}
	
	public void onEvent(IndicatorMessageEnvelope msg) {
		//if(DukasConsoleStatus.WAITING.equals(status)) {
			for(CurrencyEvent event : eventList) {
	    		LocalDateTime tradeDate = event.getDate();
	    		if(LocalDateTime.now().isAfter(tradeDate.minusMinutes(2)) 
	    				&& LocalDateTime.now().isBefore(tradeDate.plusMinutes(2))) {
	    			logger.info("evaluate IndicatorMessageEnvelope " + msg);
	        		event.onEvent(msg);
		        	if(event.isPassed()) {
		        		//entry pushing order to TSAuto's Engine
		        		//enterPosition(event.getCurrency(), event.isLong());
		        		logger.info("call open position " + event.getCurrency() + " " + event.isLong());
		        		openPostion(event.getCurrency(), event.isLong());
		        		logger.info("back open position " + event.getCurrency() + " " + event.isLong());
		        	}
	    		}
	    	}
		//}
	}
	
	private boolean longp = false;
	private Pair pair = null;
	
	private void enterPosition(Pair pair, boolean better) {
		this.pair = pair;
		status = DukasConsoleStatus.ENTERED;
		longp = better;
		Date d = new Date();
		System.out.println(d + " ms " + d.getTime() +  " PAIR: " + pair + " LONGP: " + better );
	}
	
	/*
	 * JPY
	 * EUR
	 * CHF
	 * AUD
	 * NZD
	 * USD 
	 * CAD
	 */
	private void openPostion(String scur, boolean betterForTheCurrency) {
		Currency cur = Currency.valueOf(scur);
		if(Currency.JPY.equals(cur)) {
			enterPosition(Pair.AUDJPY, !betterForTheCurrency);
		} else if(Currency.EUR.equals(cur)) {
			enterPosition(Pair.EURUSD, betterForTheCurrency);
		} else if(Currency.CHF.equals(cur)) {
			//enterPosition(Pair.CHFJPY,better);
		} else if(Currency.AUD.equals(cur)) {
			enterPosition(Pair.AUDJPY, betterForTheCurrency);
		} else if(Currency.NZD.equals(cur)) {
			enterPosition(Pair.NZDUSD, betterForTheCurrency);
		} else if(Currency.USD.equals(cur)) {
			enterPosition(Pair.EURUSD, !betterForTheCurrency);
		} else if(Currency.CAD.equals(cur)) {
			enterPosition(Pair.EURCAD, !betterForTheCurrency);
		} else if(Currency.GBP.equals(cur)) {
			enterPosition(Pair.EURGBP, !betterForTheCurrency);
		} else {
			return;
		}
	}

	public DukasConsoleStatus getStatus() {
		return status;
	}

	public void setStatus(DukasConsoleStatus status) {
		this.status = status;
	}

	public boolean isLong() {
		return longp;
	}

	public void setLongp(boolean longp) {
		this.longp = longp;
	}

	public Pair getPair() {
		return pair;
	}

	public void setPair(Pair pair) {
		this.pair = pair;
	}
	
	public class DukasConsoleTask extends TimerTask {
		
		boolean active = false;
		DukasConsoleForTest console = null;
		
		DukasConsoleTask(DukasConsoleForTest console, boolean active) {
			this.console = console;
			this.active = active;
		}
		
		public void run() {
			if(active) {
				//console.setInWaiting();
			} else {
				//console.setOutOfTime();
			}
		}
	}

}

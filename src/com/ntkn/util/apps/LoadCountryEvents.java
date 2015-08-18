package com.ntkn.util.apps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.net.flavix.dto.HibernateUtil;
import com.net.flavix.dto.CountryEvent;
import com.ntkn.flavix.CurrencyEvent;
import com.ntkn.flavix.NewsEvent;



public class LoadCountryEvents {

	private LoadCountryEvents() {
		try {
			loadRandomDataOnDB();
			List<CountryEvent> list = CountryEvent.loadAll(HibernateUtil.getSessionFactory().openSession());
			List<NewsEvent> listNews = new ArrayList<NewsEvent>(list.size()); 
			System.out.println(list.get(0).getDateTime().equals(list.get(1).getDateTime()));
			for(CountryEvent e : list) {
				NewsEvent news = new NewsEvent(e.getCategory());
				Set<Integer> set = e.getFieldsMap().keySet();
				for(Integer i : set) {
					Double value = e.getFieldsMap().get(i);
					news.addDatum(Math.abs(i), value, i>0);
				}
				listNews.add(news);
				addNews(e.getDateTime(),news);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadRandomDataOnDB() throws Exception {
		LocalDateTime localtime = LocalDateTime.now();
		CountryEvent t = new CountryEvent();
		t.setCategory(20003);
		t.setDateTime(localtime);
		t.setFields(4, 9.8d);
		t.setFields(3, 1.1d);
		CountryEvent.persistTupla(HibernateUtil.getSessionFactory().openSession(), t);
		t = new CountryEvent();
		t.setDateTime(localtime);
		t.setCategory(20003);
		t.setFields(1, 27.7d);
		t.setFields(-2, 55.0d);
		CountryEvent.persistTupla(HibernateUtil.getSessionFactory().openSession(), t);
	}
	
	public static LoadCountryEvents getLoader() {
		return new LoadCountryEvents();
	}
	
	public List<CurrencyEvent> load() throws Exception {
		Map<Pair<LocalDateTime,String>,CurrencyEvent> aux = new HashMap<>();
		Set<LocalDateTime> set = map.keySet();
		for(LocalDateTime time : set) {
			List<NewsEvent> l = map.get(time);
			for(NewsEvent news : l) {
				String nation = getNation(news.getCategory());
				CurrencyEvent cEvent = aux.get(new Pair<LocalDateTime,String>(time,nation));
				if(cEvent==null) {
					cEvent = new CurrencyEvent(nation,time);
					cEvent.addNews(news);
					aux.put(new Pair<LocalDateTime,String>(time,nation), cEvent);
				} else {
					cEvent.addNews(news);
				}
			}
		}
		return new ArrayList<CurrencyEvent>(aux.values());
	}
	
	private String getNation(int category) throws Exception {
		String userName = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/test";
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
        	DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
        	Result<Record> result = create.fetch("select distinct Code,Category,Nation from mytable where Code="+category);
        	return (String)result.get(0).getValue("Nation");
        }
	}
	
	private static Map<LocalDateTime,List<NewsEvent>> map = new HashMap<>();
	
	private static void addNews(LocalDateTime dateTime, NewsEvent newsEvent) {
		List<NewsEvent> list = map.get(dateTime);
		if(list==null) {
			list = new ArrayList<NewsEvent>();
			list.add(newsEvent);
			map.put(dateTime, list);
		} else {
			list.add(newsEvent);
		}
	}
	
	public class Pair<F, S> {
		
	    private F first;
	    private S second;
	    
	    public Pair(F first, S second) {
	        this.first = first;
	        this.second = second;
	    }

	    public void setFirst(F first) {
	        this.first = first;
	    }

	    public void setSecond(S second) {
	        this.second = second;
	    }

	    public F getFirst() {
	        return first;
	    }

	    public S getSecond() {
	        return second;
	    }
	    
	    @Override
	    public boolean equals(Object o) {
	    	return ((Pair<F, S>)o).getFirst().equals(first) && ((Pair<F, S>)o).getSecond().equals(second);
	    }
	    
	    @Override
	    public int hashCode() {
	    	return first.hashCode() * second.hashCode();
	    }
	}

}

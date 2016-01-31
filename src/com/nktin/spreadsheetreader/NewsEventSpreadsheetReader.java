package com.nktin.spreadsheetreader;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.ntkn.flavix.CurrencyEvent;
import com.ntkn.flavix.NewsEvent;




public class NewsEventSpreadsheetReader {

	public NewsEventSpreadsheetReader() {
		try
	    {
	        FileInputStream x = new FileInputStream(new File("C:\\Documents and Settings\\marchifl\\Desktop\\AlphaStreamNewsCalendar.xlsx"));

	        //Create Workbook instance holding reference to .xlsx file
	        Workbook wb = WorkbookFactory.create(x);
	        
	        //Get first/desired sheet from the workbook
	        Sheet sheet = wb.getSheetAt(0);

	        //Iterate through each rows one by one
	        for (Iterator<Row> iterator = sheet.iterator(); iterator.hasNext();) {
	            Row row = (Row) iterator.next();
	            if(row.getRowNum()>3 && row.getCell(0)!=null && row.getCell(0).getCellType()!=Cell.CELL_TYPE_BLANK) {
	            	 String date = new DataFormatter().formatCellValue(row.getCell(0));
	            	 String time = new DataFormatter().formatCellValue(row.getCell(1));
	            	 String currency = String.valueOf(row.getCell(2).getStringCellValue());
	            	 int category = (int) (row.getCell(3).getNumericCellValue());
	            	 int fieldindex = (int) (row.getCell(4).getNumericCellValue());
	            	 boolean greatherthan = row.getCell(5).getBooleanCellValue();
	            	 double value = row.getCell(6).getNumericCellValue();
	            	 addNewsEvent(date,time,currency,category,fieldindex,greatherthan,value);
	            }
	        }   
	        for(LocalDateTime ldtkey : map.keySet()) {
	        	List<NewsEvent> newsEvent = map.get(ldtkey);
	        	 for(NewsEvent e : newsEvent)
	     	        System.out.println(e);
	        }
	        List<CurrencyEvent> aux = buildUpCategoryEvent();
	        for(CurrencyEvent e : aux)
	        System.out.println(e);
	        x.close();
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	}
	
	Map<LocalDateTime,List<NewsEvent>> map = new TreeMap<>();
	
	static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	public void addNewsEvent(String date, String time, String currency, int category, int fieldindex, boolean greatherThan, double value) {
		LocalDate lDate = LocalDate.parse(date,dateFormat);
	    LocalTime lTime = LocalTime.parse(time,timeFormat);
		LocalDateTime ldt = LocalDateTime.of(lDate, lTime);
		boolean findit = false;
		List<NewsEvent> list = map.get(ldt);
		System.out.println(ldt + " " + currency+  " " + category);
		if(list!=null) {
			for(int i=0;i<list.size();i++) {
				NewsEvent newsEvent = list.get(i);
				if(newsEvent.getCategory()==category) {
					newsEvent.addField(fieldindex, value, greatherThan);
					findit = true;
				}
			}
		}
		if(!findit) {
			NewsEvent newsEvent = new NewsEvent(ldt,category,currency);
			newsEvent.addField(fieldindex, value, greatherThan);
			if(list!=null) {
				list.add(newsEvent);
			} else {
				List<NewsEvent> aux = new ArrayList<>();
				aux.add(newsEvent);
				map.put(ldt, aux);
			}
		}
	}
	
	static Map<String,List<String>> fxpairs = new HashMap<>();
	
	//currencies crosses
	static {
		fxpairs.put("EUR", Arrays.asList("EUR_USD","EUR_JPY","EUR_CHF","EUR_GBP","EUR_CAD","EUR_AUD","EUR_NZD"));
		fxpairs.put("JPY", Arrays.asList("EUR_JPY","USD_JPY","GBP_JPY","CHF_JPY","CAD_JPY","AUD_JPY","NZD_JPY"));
		fxpairs.put("GBP", Arrays.asList("EUR_GBP","GBP_JPY","GBP_CHF","GBP_AUD","GBP_CAD","GBP_AUD","GBP_NZD"));
		fxpairs.put("USD", Arrays.asList("EUR_USD","USD_JPY","GBP_USD","USD_CAD","USD_AUD","USD_NZD","USD_CHF"));
		fxpairs.put("CHF", Arrays.asList("EUR_CHF","JPY_CHF","GBP_CHF","USD_CHF","USD_CHF","CHF_NZD","CHF_AUD"));
	}
	
	private List<CurrencyEvent> buildUpCategoryEvent() {
		List<CurrencyEvent> list = new ArrayList<>();
		for(LocalDateTime key : map.keySet()) {
			List<NewsEvent> aux = map.get(key);
			CurrencyEvent cEvent = null;
			for(NewsEvent newsEvent : aux) {
				if(cEvent!=null) {
					if(cEvent.getCurrency().compareTo(newsEvent.getCurrency())==0) {
						cEvent.addNews(newsEvent);
					} else {
						cEvent = new CurrencyEvent(newsEvent.getCurrency(),key);
						cEvent.addNews(newsEvent);
						list.add(cEvent);
					}
				} else {
					cEvent = new CurrencyEvent(newsEvent.getCurrency(),key);
					cEvent.addNews(newsEvent);
					list.add(cEvent);
				}
			}
		}
		for(CurrencyEvent currEvent : list) {
			List<String> pairs = fxpairs.get(currEvent.getCurrency());
			if(pairs!=null) {
				System.out.println(getAllCurrenciesAtTheSameTime(currEvent.getDate(),list));
				currEvent.setPairs(selectPairs(currEvent.getCurrency(),
						getAllCurrenciesAtTheSameTime(currEvent.getDate(),list)));
			}
		}
		return list;
	}
	
	private static List<String> getAllCurrenciesAtTheSameTime(LocalDateTime ldt, List<CurrencyEvent> list) {
		List<String> aux = new ArrayList<>();
		for(CurrencyEvent cEvent : list) {
			if(cEvent.getDate().compareTo(ldt)==0) {
				aux.add(cEvent.getCurrency());
			}
		}
		return aux;
	}
	
	private static List<String> selectPairs(String currency, List<String> currenciesAtSameTime) {
		return selectPairs(currency, currenciesAtSameTime.toArray(new String[]{}));
	}
	
	private static List<String> selectPairs(String currency, String... currenciesAtSameTime) {
		List<String> aux = new ArrayList<>(fxpairs.get(currency));
		for(String cry : currenciesAtSameTime) {
			if(!(cry.compareToIgnoreCase(currency)==0)) {
				for(int i=0;i<aux.size();i++) {
					if(aux.get(i).contains(cry)) {
						aux.remove(i);
					}
				}
 			}
		}
		return aux;
	}
	
	public static void main(String[] args) {
		new NewsEventSpreadsheetReader();
	}

}

package com.ntkn.util.apps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.axis2.util.IOUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.net.flavix.dto.OandaForexHistorical;
import com.ntkn.util.Pair;
import com.ntkn.util.Triplet;




public class OandaDataFetcher {
	
	static String proxyhost = "primary-proxy.gslb.intranet.barcapint.com";
	static String username = "marchifl";
	static String password = "b*81*CH9ab";
	
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
	
	private void singleFetch(Triplet<String,String,String> pair) throws Exception {
		int count = 0;
		List<OandaForexHistorical> list = new ArrayList<OandaForexHistorical>();
		List<String> pairs = nationPairs.get(pair.getB());
		if(pairs!=null && pairs.get(0)!=null) {
			LocalDateTime dateTime = LocalDateTime.parse(pair.getA(),dtf);
			System.out.println(dateTime);
			if(!dateTime.isAfter(LocalDateTime.now())) {
				for(String currencyPair : pairs) {
					Pair<double[],double[]> aux = OandaDataFetcher.getAsksBids(dateTime,20,currencyPair);
					if(aux!=null) {
						OandaForexHistorical f = new OandaForexHistorical(dateTime,
								pair.getB(),
								currencyPair,
								Integer.parseInt(pair.getC()),
								aux.getElement0(),
								aux.getElement1());
						count++;
						System.out.println(f);
						list.add(f);
					}
				}
			}
			new OandaForexHistorical().persist(list);
			System.exit(-1);
		}
	}
	
	public OandaDataFetcher() throws Exception {
		 java.sql.Timestamp date = Timestamp.valueOf("2015-09-10 12:30:00.0");
		 String country = "US";
		 String category = "40";
		 Triplet<String,String,String> triplet = new Triplet<>(date.toString(),country,category);
		 singleFetch(triplet);
	}
	
	//curl -H "Authorization: Bearer 12345678900987654321-abc34135acde13f13530" https://api-fxtrade.oanda.com/v1/candles?instrument=EUR_USD&start=137849394&count=1
	public void OandaFataFetcher2() throws Exception {
		//new Triplet<>(date.toString(),country,category);
		List<Triplet<String,String,String>> events = getEventsDate();
		List<OandaForexHistorical> list = new ArrayList<OandaForexHistorical>();
		int count = 0;
		for(Triplet<String,String,String> pair : events) {
			List<String> pairs = nationPairs.get(pair.getB());
			if(pairs!=null && pairs.get(0)!=null) {
				LocalDateTime dateTime = LocalDateTime.parse(pair.getA(),dtf);
				System.out.println(dateTime);
				if(!dateTime.isAfter(LocalDateTime.now())) {
					for(String currencyPair : pairs) {
						Pair<double[],double[]> aux = OandaDataFetcher.getAsksBids(dateTime,1000,currencyPair);
						if(aux!=null) {
							OandaForexHistorical f = new OandaForexHistorical(dateTime,
									pair.getB(),
									currencyPair,
									Integer.parseInt(pair.getC()),
									aux.getElement0(),
									aux.getElement1());
							count++;
							list.add(f);
						}
					}
				}
			}
			
		}
		new OandaForexHistorical().persist(list);
		System.exit(-1);
		/*
		NTCredentials ntCreds = new NTCredentials("marchifl", password, "LDNDWM406292", "marchifl" );

		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(proxyhost,8080), ntCreds);
		*/
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();

		//clientBuilder.useSystemProperties();
		//clientBuilder.setProxy(new HttpHost(proxyhost, 8080));
		//clientBuilder.setDefaultCredentialsProvider(credsProvider);
		//clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

		CloseableHttpClient httpclient = clientBuilder.build();
		System.out.println(Calendar.getInstance().getTime().getTime());
		HttpGet request = new HttpGet("https://api-fxpractice.oanda.com/v1/candles?instrument=EUR_CAD&since=1425821443100&count=100");
		request.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; MSIE 7.0; Windows NT 6.0; el-GR)");
		String userCredentials = username+":"+password;
		String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
		request.setHeader("Proxy-Authorization", basicAuth);
		//request.setHeader("Authorization", "Bearer db00310cc003c706a54bd5822c49367b-0982493a12d64d17d14ceb196f5d4b0e");
		HttpResponse response = httpclient.execute(request);

		// Get the response
		BufferedReader rd = new BufferedReader
		  (new InputStreamReader(response.getEntity().getContent()));
		    
		String line = "";
		while ((line = rd.readLine()) != null) {
		  System.out.println(line);
		} 

	}
	
	public static void main(String[] args) {
		try {
			new OandaDataFetcher();
			//getDiff(new Date(Calendar.getInstance().getTimeInMillis()-60*1000));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Map<String,List<String>> nationPairs = new HashMap<>();
	static {
		nationPairs.put("CA", Arrays.asList("EUR_CAD"));
		nationPairs.put("US", Arrays.asList("EUR_USD"));
		nationPairs.put("DE", Arrays.asList("EUR_USD"));
		nationPairs.put("FR", Arrays.asList("EUR_USD"));
		nationPairs.put("EU", Arrays.asList("EUR_USD"));
		nationPairs.put("UK", Arrays.asList("EUR_GBP", "GBP_USD"));
		nationPairs.put("CH", Arrays.asList("EUR_CHF", "USD_CHF"));
	}
	
	private List<Triplet<String,String,String>> getEventsDate() throws Exception {
		String userName = "marchifl";
        String password = "velvia";
        String url = "jdbc:postgresql://localhost:5432/postgres";
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
        	 DSLContext create = DSL.using(conn, SQLDialect.POSTGRES_9_4);
        	 Result<Record> result = create.fetch("select distinct date,country,category from alphaflashcalendar where country!=''");
        	 List<Triplet<String,String,String>> aux = new ArrayList<>(result.size());
        	 for(Record r : result) {
        		 java.sql.Timestamp date = (java.sql.Timestamp)r.getValue("date");
        		 String country = (String)r.getValue("country");
        		 String category = ((Integer)r.getValue("category")).toString();
        		 Triplet<String,String,String> p = new Triplet<>(date.toString(),country,category);
        		 aux.add(p);
        	 }
        	 return  aux;
        }
	}

	private static List<Triple> triplemap = new ArrayList<Triple>();
	
	public static String getDiff(String date, int forward, String pair) {
		return getDiff(LocalDateTime.parse(date,dtf), forward, pair);
	}
	
	public static String getDiff(LocalDateTime date, int forward, String pair) {		
		if(pair==null)
			return null;
		Triple t = lookup(date,forward,pair);
		if(t!=null) {
			return t.getReturnValue();
		}
		String url = null;
		try {
			java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
			java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);
			System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
			System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
			System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
			System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
			System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");
			
			//NTCredentials ntCreds = new NTCredentials("marchifl", password, "LDNDWM406292", "marchifl" );
	
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			//credsProvider.setCredentials(new AuthScope(proxyhost,8080), ntCreds);
			HttpClientBuilder clientBuilder = HttpClientBuilder.create();
	
	        credsProvider.setCredentials(
	                new AuthScope(proxyhost, 8080),
	                new UsernamePasswordCredentials(username, password));
	
			//clientBuilder.useSystemProperties();
			//clientBuilder.setProxy(new HttpHost(proxyhost, 8080));
			//clientBuilder.setDefaultCredentialsProvider(credsProvider);
			//clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
	        
	        Registry<AuthSchemeProvider> r = RegistryBuilder.<AuthSchemeProvider>create()
	                .register(AuthSchemes.BASIC, new BasicSchemeFactory())
	                .register(AuthSchemes.DIGEST, new DigestSchemeFactory())
	                .build();
			
			CloseableHttpClient httpclient = HttpClients.custom()
					.setDefaultAuthSchemeRegistry(r)
	                .setDefaultCredentialsProvider(credsProvider).build();
			
			url = "/v1/candles?instrument="+pair+"&granularity=S5&start="
					+ toURLStringDate(date.minusSeconds(2)) + "&end=" + toURLStringDate(date.plusSeconds(forward));
			//url = "/v1/candles?instrument="+pair+"&granularity=S5&start="
			//		+ date.minusSeconds(2).toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() 
			//		+ "&end=" + date.plusSeconds(forward).minusSeconds(2).toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
			//url = "http://finance.yahoo.com/q?s=EURUSD";
			//System.out.println(url);
			
			HttpHost target = new HttpHost("api-fxpractice.oanda.com", 443, "https");
	        HttpHost proxy = new HttpHost(proxyhost, 8080);
	
	        RequestConfig config = RequestConfig.custom()
	        	.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
	            .setProxy(proxy)
	            .build();
			
			//"https://api-fxpractice.oanda.com/v1/candles?instrument=EUR_USD&start=2014-06-19T15%3A47%3A40Z&end=2014-06-19T15%3A47%3A50Z"
			HttpGet httpget = new HttpGet(url);
			httpget.setConfig(config);
			httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; MSIE 7.0; Windows NT 6.0; el-GR)");
			String userCredentials = username+":"+password;
			String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
			httpget.setHeader("Proxy-Authorization", basicAuth);
			httpget.setHeader("Authorization", "Bearer db00310cc003c706a54bd5822c49367b-0982493a12d64d17d14ceb196f5d4b0e");
			
			//System.out.println(httpget.getURI());
			
			HttpResponse response = httpclient.execute(target,httpget);
			//System.out.println("->" + convertStreamToString(response.getEntity().getContent()));
			//while(response.getEntity()==null)
			//	Thread.sleep(100);
			
			// Get the response
			BufferedReader rd = new BufferedReader
			  (new InputStreamReader(response.getEntity().getContent()));
			    
			StringBuffer sbuff = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
			  sbuff.append(line);
			}
			
			JsonParser parser = new JsonParser();
			JsonObject rootObj = parser.parse(sbuff.toString()).getAsJsonObject();
			JsonArray locObj = rootObj.getAsJsonArray("candles");
			//String locObj..get("openBid").getAsDouble();
			//locObj.get(0).get("openBid").getAsDouble();
			Iterator<JsonElement> iter = locObj.iterator();
			//double[] asks = new double[locObj.size()+1];
			List<Double> asks = new ArrayList<Double>();
			List<Double> bids = new ArrayList<Double>();
			//double[] bids = new double[locObj.size()+1];
			int index = 0;
			while(iter.hasNext()) {
				JsonElement object = iter.next();
				String time = object.getAsJsonObject().get("time").getAsString();
				double bid = object.getAsJsonObject().get("openBid").getAsDouble();
				bids.add(bid);
				double ask = object.getAsJsonObject().get("openAsk").getAsDouble();
				//asks[index++] = ask;
				asks.add(ask);
				//System.out.println(time + " - " + bid + " - " + ask);
			}
			double first = locObj.get(0).getAsJsonObject().get("openBid").getAsDouble();
			double last = locObj.get(locObj.size()-1).getAsJsonObject().get("openBid").getAsDouble();
			rd.close();
			
			Triple triple = new Triple(date,forward,pair,fromatDoubleValue(last - first));
			triplemap.add(triple);
	
			return fromatDoubleValue(last - first);
		}catch(Exception e) {
			//System.out.println(url);
			e.printStackTrace();
		}
		return null;
	}
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public static Pair<double[],double[]> getAsksBids(LocalDateTime date, int forward, String pair) {
		if(pair==null)
			return null;
		Triple t = lookup(date,forward,pair);
		if(t!=null) {
			return null;
		}
		String url = null;
		try {
			java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
			java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);
			System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
			System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
			System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
			System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
			System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");
			
			//NTCredentials ntCreds = new NTCredentials("marchifl", password, "LDNDWM406292", "marchifl" );
	
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			//credsProvider.setCredentials(new AuthScope(proxyhost,8080), ntCreds);
			HttpClientBuilder clientBuilder = HttpClientBuilder.create();
	
	        credsProvider.setCredentials(
	                new AuthScope(proxyhost, 8080),
	                new UsernamePasswordCredentials(username, password));
	
			//clientBuilder.useSystemProperties();
			//clientBuilder.setProxy(new HttpHost(proxyhost, 8080));
			//clientBuilder.setDefaultCredentialsProvider(credsProvider);
			//clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
	        
	        Registry<AuthSchemeProvider> r = RegistryBuilder.<AuthSchemeProvider>create()
	                .register(AuthSchemes.BASIC, new BasicSchemeFactory())
	                .register(AuthSchemes.DIGEST, new DigestSchemeFactory())
	                .build();
			
			CloseableHttpClient httpclient = HttpClients.custom()
					.setDefaultAuthSchemeRegistry(r)
	                .setDefaultCredentialsProvider(credsProvider).build();
			
			url = "/v1/candles?instrument="+pair+"&granularity=S5&start="
					+ toURLStringDate(date.minusSeconds(2)) + "&end=" + toURLStringDate(date.plusSeconds(forward));
			//url = "/v1/candles?instrument="+pair+"&granularity=S5&start="
			//		+ date.minusSeconds(2).toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() 
			//		+ "&end=" + date.plusSeconds(forward).minusSeconds(2).toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
			//url = "http://finance.yahoo.com/q?s=EURUSD";
			//System.out.println(url);
			
			HttpHost target = new HttpHost("api-fxpractice.oanda.com", 443, "https");
	        HttpHost proxy = new HttpHost(proxyhost, 8080);
	
	        RequestConfig config = RequestConfig.custom()
	        	.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
	            .setProxy(proxy)
	            .build();
			
			//"https://api-fxpractice.oanda.com/v1/candles?instrument=EUR_USD&start=2014-06-19T15%3A47%3A40Z&end=2014-06-19T15%3A47%3A50Z"
			HttpGet httpget = new HttpGet(url);
			httpget.setConfig(config);
			httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; MSIE 7.0; Windows NT 6.0; el-GR)");
			String userCredentials = username+":"+password;
			String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
			httpget.setHeader("Proxy-Authorization", basicAuth);
			httpget.setHeader("Authorization", "Bearer db00310cc003c706a54bd5822c49367b-0982493a12d64d17d14ceb196f5d4b0e");
			
			HttpResponse response = httpclient.execute(target,httpget);
			//System.out.println("->" + response);
			//while(response.getEntity()==null)
			//	Thread.sleep(100);
			
			// Get the response
			BufferedReader rd = new BufferedReader
			  (new InputStreamReader(response.getEntity().getContent()));
			    
			StringBuffer sbuff = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
			  sbuff.append(line);
			}
			
			JsonParser parser = new JsonParser();
			JsonObject rootObj = parser.parse(sbuff.toString()).getAsJsonObject();
			JsonArray locObj = rootObj.getAsJsonArray("candles");
			//String locObj..get("openBid").getAsDouble();
			//locObj.get(0).get("openBid").getAsDouble();
			Iterator<JsonElement> iter = locObj.iterator();
			double[] asks = new double[locObj.size()];
			double[] bids = new double[locObj.size()];
			int index = 0;
			while(iter.hasNext()) {
				JsonElement object = iter.next();
				String time = object.getAsJsonObject().get("time").getAsString();
				double bid = object.getAsJsonObject().get("openBid").getAsDouble();
				bids[index] = bid;
				double ask = object.getAsJsonObject().get("openAsk").getAsDouble();
				asks[index] = ask;
				++index;
				//System.out.println(time + " - " + bid + " - " + ask);
			}
			double first = locObj.get(0).getAsJsonObject().get("openBid").getAsDouble();
			
			double last = locObj.get(locObj.size()-1).getAsJsonObject().get("openBid").getAsDouble();
			rd.close();
			
			Triple triple = new Triple(date,forward,pair,fromatDoubleValue(last - first));
			triplemap.add(triple);
	
			return new Pair<double[],double[]>(asks,bids);
		}catch(Exception e) {
			//System.out.println(url);
			e.printStackTrace();
		}
		return null;
	}
	
	private static Triple lookup(LocalDateTime dTime, int forword, String pair) {
		for(Triple t : triplemap) {
			if(t.isTheSame(dTime, forword, pair))
				return t;
		}
		return null;
	}
	
	public static String fromatDoubleValue(double v) {
		return new BigDecimal(v).setScale(5,RoundingMode.HALF_UP).toPlainString();
	}
	
	public static String toURLStringDate(LocalDateTime date) {
		//SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'");
		return URLEncoder.encode(date.toString());
	}

}

class Triple {
	LocalDateTime dTime;
	int forword;
	String pair;
	String returnValue;
	public Triple(LocalDateTime dTime, int forword, String pair,
			String returnValue) {
		super();
		this.dTime = dTime;
		this.forword = forword;
		this.pair = pair;
		this.returnValue = returnValue;
	}
	public LocalDateTime getdTime() {
		return dTime;
	}
	public void setdTime(LocalDateTime dTime) {
		this.dTime = dTime;
	}
	public int getForword() {
		return forword;
	}
	public void setForword(int forword) {
		this.forword = forword;
	}
	public String getPair() {
		return pair;
	}
	public void setPair(String pair) {
		this.pair = pair;
	}
	public String getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}
	
	public boolean isTheSame(LocalDateTime dTime, int forword, String pair) {
		return dTime.equals(this.dTime) && forword==this.forword && pair.compareTo(this.pair)==0;
	}
	
	public static Triple getInstance(LocalDateTime dTime, int forword, String pair, String returnValue) {
		return new Triple(dTime,forword,pair,returnValue);
	}
}

package com.nktin.spreadsheetreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ntkn.flavix.CurrencyEvent;



public class ReadCurrencyEventForToday {

	public ReadCurrencyEventForToday() {
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
 		        .setPrettyPrinting().create();
		byte[] text;
		try {
			text = Files.readAllBytes(Paths.get("C:\\Documents and Settings\\marchifl\\Desktop\\AlphaStreamNewsCalendar.json"));
			Type listType = new TypeToken<List<CurrencyEvent>>() {}.getType();
			List<CurrencyEvent> cEvents = selectEventValidFromNow(gson.fromJson(new String(text),listType));
			System.out.println(cEvents.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void startProcess() {
		try {
			// Create ProcessBuilder.
			ProcessBuilder pb = new ProcessBuilder();

			// Use command "notepad.exe" and open the file.
			pb.command("notepad.exe", "C:\\file.txt");
			pb.start();
			
		
			
	      String line;
	      Process p = Runtime.getRuntime().exec
	        (System.getenv("windir") +"\\system32\\"+"tree.com /A");
	      BufferedReader input =
	        new BufferedReader
	          (new InputStreamReader(p.getInputStream()));
	      while ((line = input.readLine()) != null) {
	        System.out.println(line);
	      }
	      input.close();
	    }
	    catch (Exception err) {
	      err.printStackTrace();
	    }
	}
	
	private List<CurrencyEvent> selectEventValidFromNow(List<CurrencyEvent> list) {
		List<CurrencyEvent> aux = new LinkedList<CurrencyEvent>();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime till = LocalDateTime.from(now).plusHours(1);
		for(CurrencyEvent c : list) {
			if(c.getDate().compareTo(now)>0 && c.getDate().compareTo(till)<0) {
				aux.add(c);
			}
		}
		return aux;
	}
	
	/*
	public static void main(String[] args) {
		new ReadCurrencyEventForToday();
	}*/
	
	public static void main(String[] args) throws Exception {
        String execStr = "C:\\Program Files\\Java\\jre1.8.0_66\\bin\\java.exe -cp C:\\Documents and Settings\\marchifl\\Desktop HelloWorld";
        Process proc = Runtime.getRuntime().exec(execStr);
        System.out.println("proc: " + proc);
        Thread.sleep(20000);
        System.out.println("destroying");
        proc.destroyForcibly();
        System.out.println("destroyed");
    }

}

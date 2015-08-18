package com.nktin.protocol.tailored;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import com.nktin.protocol.Protocol;
import com.nktin.protocol.Protocol.Category;
import com.nktin.protocol.Protocol.Category.Data.Datum;
import com.nktin.protocol.Protocol.Category.Messages.Message;
import com.nktin.protocol.Protocol.Category.Messages.Message.Fields.Field;



public class XMLParserTest {
	
	private static Map<Integer,NewsEvent> mapNewsEvent = new TreeMap<>();
	
	private static String basedir = "C:\\DEV\\AlphaFlash\\AlphaFlash-2014_11_25\\";
	private static String xmldir = basedir;
	private static String xsddir = basedir + "\\AlphaFlashClientProtocolSpecification.xsd";
	
	public static Map<Integer,NewsEvent> getMap() {
		return mapNewsEvent;
	}

	public XMLParserTest(String name) {
		xmldir = basedir + "\\" + name + ".xml";
		try {
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Schema schema = sf.newSchema(new File(xsddir));
	 
	        JAXBContext jc = JAXBContext.newInstance(Protocol.class);
	 
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        unmarshaller.setSchema(schema);
	        unmarshaller.setEventHandler(new MyValidationEventHandler());
	        Protocol customer = (Protocol) unmarshaller.unmarshal(new File(xmldir));
	        for(Category c : customer.getCategory()) {
	        	if(c.getCategoryId()==40017) {
	        	NewsEvent news = new NewsEvent();
	        	news.setName(c.getName());
	        	news.setDescription(c.getDescription());
	        	news.setCategoryId(c.getCategoryId());
	  
	        	news.setXmlName(xmldir);
	        	
	        	System.out.println(news);
	        	//System.out.println(c.getCategoryId() + " " + c.getName());
	        	//System.out.println(c.getDescription());
	        	for(Datum d : c.getData().getDatum()) {
	        		List<JAXBElement<?>> list = d.getShortDescriptionOrDescriptionOrDatumScale();
	        		int id = 0;
	        		String description = null;
	        		String unit = null;
	        		for(JAXBElement<?> o : list) {
	        			System.out.println("\t " + o.getName() + " " + o.getValue());
	        			switch(o.getName().toString()) {
		        			case "datum_id":
		        				id = Integer.valueOf(o.getValue().toString());
		        				break;
		        			case "description":
		        				description = o.getValue().toString();
		        				break;	        				
		        			case "datum_type":
		        				unit = o.getValue().toString();	
		        				break;
	        			}
	        		}
	        		news.addField(id, description, unit);
	        		System.out.println("\t ");
	        	}
	        	
	        	mapNewsEvent.put(news.getCategoryId(), news);
	        	for(Message d : c.getMessages().getMessage()) {
	        		//System.out.println(d.getMessageType().name());
	        		for(Field f : d.getFields().getField()) {
	        			
	        			//System.out.println(f.getExampleValue());
	        			//System.out.println(f.getFieldType());
	        		}
	        	}
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Map<Integer,NewsEvent> getCategories() {
		return mapNewsEvent;
	}
	
}

class MyValidationEventHandler implements ValidationEventHandler {
	 
    public boolean handleEvent(ValidationEvent event) {
        System.out.println("\nEVENT");
        System.out.println("SEVERITY:  " + event.getSeverity());
        System.out.println("MESSAGE:  " + event.getMessage());
        System.out.println("LINKED EXCEPTION:  " + event.getLinkedException());
        System.out.println("LOCATOR");
        System.out.println("    LINE NUMBER:  " + event.getLocator().getLineNumber());
        System.out.println("    COLUMN NUMBER:  " + event.getLocator().getColumnNumber());
        System.out.println("    OFFSET:  " + event.getLocator().getOffset());
        System.out.println("    OBJECT:  " + event.getLocator().getObject());
        System.out.println("    NODE:  " + event.getLocator().getNode());
        System.out.println("    URL:  " + event.getLocator().getURL());
        return true;
    }
 
}

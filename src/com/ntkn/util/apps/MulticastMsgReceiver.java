/**
 * All of the information (including source code, content and artwork) are copyright. No part of this message 
 * or any included attachment may be reproduced, stored in a retrieval system, transmitted, broadcast or published by any means 
 * (optical, magnetic, electronic, mechanical or otherwise) without the prior written permission of Need to Know News. 
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND 
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, 
 * ARE HEREBY EXCLUDED. "Need to Know News, LLC" AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL "Need to Know News" OR 
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL
 * OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO
 *  USE THIS SOFTWARE, EVEN IF "Need To Know News" HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 *  Need to Know News, Copyright 2008
 * 
 */

package com.ntkn.util.apps;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

import com.ntkn.flavix.CurrencyEvent;
import com.ntkn.flavix.EventResult;
import com.ntkn.flavix.NewsEvent;
import com.ntkn.messages.IndicatorMessage;
import com.ntkn.messages.evnveloped.IndicatorMessageEnvelope;




public class MulticastMsgReceiver 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		/*
		SessionFactory s = HibernateUtil.getSessionFactory();
		try {
			Tupla.persistTupla(s.openSession());
		} catch (HibernateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		
       String DATE_FORMAT = "HH:mm:ss.SSS";
        int port = 16899;
        String mcastAddress = "";
        int delayMillis = 0;
        long count = 0;
        
		if (args.length < 2)
		{
			System.out.println("Usage: MulticastMsgReceiver <Addr> <Port>");
			System.exit(1);
		}	        
        
		try
		{
        	System.setProperty("java.net.preferIPv4Stack", "true");			
			mcastAddress = args[0];
			port = Integer.parseInt(args[1]);
			
			System.out.println("Listening on "+ mcastAddress + " port "+ port);
			
	        SimpleDateFormat dateFormatter =  new java.text.SimpleDateFormat(DATE_FORMAT);
			dateFormatter.setTimeZone(TimeZone.getDefault());       			
			
		    MulticastSocket socket = new MulticastSocket(port);
		    socket.setReuseAddress(true);
		    InetAddress address = InetAddress.getByName(mcastAddress); 
		    socket.joinGroup(address);
		    			    
		    DatagramPacket packet;
		    
		    CurrencyEvent countryEvent = new CurrencyEvent("EUR", LocalDateTime.now());
		    NewsEvent newsEvent = new NewsEvent(20003);
		    newsEvent.addDatum(4, 9.8d, false);
		    newsEvent.addDatum(3, 1.1d, true);
		    countryEvent.addNews(newsEvent);
		    newsEvent = new NewsEvent(101);
		    newsEvent.addDatum(1, 27.7d, true);
		    newsEvent.addDatum(2, 55.0d, false);
		    countryEvent.addNews(newsEvent);
		    
		    while(true)
		    {
			    byte messageBuf[] = new byte[4096];
			    packet = new DatagramPacket(messageBuf, messageBuf.length);
			    socket.receive(packet);
			    count++;
			    
			    Date date = new Date();
			    IndicatorMessage indMsg = new IndicatorMessage(messageBuf);		
			    
			    countryEvent.onEvent(new IndicatorMessageEnvelope(indMsg));
			    
			    if(indMsg.getHeader().getMessageCategoryId()==20003
			    		|| indMsg.getHeader() .getMessageCategoryId()==101) {
			    	System.out.println(indMsg);
			    }
			    
			    if(countryEvent.isPassed(EventResult.EVALUATION_PASSED_AS_FIRST_EVALUATION_FOR_LONG_ENTRY) 
			    		|| countryEvent.isPassed(EventResult.EVALUATION_PASSED_AS_FIRST_EVALUATION_FOR_SHORT_ENTRY)) {
			    	System.out.println(countryEvent);
			    	//countryEvent.reset();
			    	//Tupla.loadAll(HibernateUtil.getSessionFactory().getCurrentSession());
			    }
			    
			    /*
			    if(indMsg.getHeader().getMessageCategoryId()==20003) {
			    	System.out.println("Received #"+count+" "+ indMsg.toString()+ " at "+dateFormatter.format(date));			    
			    	for(IndicatorMessageField indicator : indMsg.getPayload().getIndicatorMessageFields()) {
			    		System.out.println(indicator.getfieldIdentifier() + " " + indicator.getfieldValue());
			    	}
			    }
			    */
			}		    			
		}
		catch(Exception e)
		{
			System.out.println("Exception occurred:"+e.getMessage());
		}

	}

}

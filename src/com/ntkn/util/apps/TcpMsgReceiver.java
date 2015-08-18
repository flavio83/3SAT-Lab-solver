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

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.text.*; 
import java.util.*;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


import com.ntkn.messages.HexDump;
import com.ntkn.messages.IndicatorMessage;
import com.ntkn.messages.IndicatorMessageHeader;
import com.ntkn.messages.IndicatorMessagePayload;

/**
 * TcpMsgReceiver is a simple example demonstrating connect, login, and receiving of 
 * indicator data from a Lightning Bolt v5.x data feed.
 *  
 * @author jwalton
 *
 */
public class TcpMsgReceiver
{
    private Socket xferSocket = null;  
    private DataOutputStream dataOutStream = null;
    private DataInputStream dataInStream = null;    
    private SimpleDateFormat dateFormatter = null;    
    
    private static final String DATE_FORMAT = "HH:mm:ss.SSS";
	
	/**
	 * Entry point for this application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		if (args.length < 6)
		{
			System.out.println("Usage: TcpMsgReceiver <host> <port> <user> <pass> <showMessage> <useSSL>");
			System.out.println("       <showMessage> - true to show the complete message");
			System.out.println("       <useSSL> - true to use SSL connection");
			System.exit(1);
		}	
		
		int port = Integer.parseInt(args[1]);
		boolean show = Boolean.parseBoolean(args[4]);
		boolean useSSL = Boolean.parseBoolean(args[5]);
		
		TcpMsgReceiver receiver = new TcpMsgReceiver();
		
		System.out.println("Connecting to "+args[0]+":"+port);
		
		receiver.receive(args[0],port,args[2],args[3], show, useSSL);
	}
	
	/**
	 * Default constructor.
	 */
	public TcpMsgReceiver() 
	{
		super();
		dateFormatter =  new java.text.SimpleDateFormat(DATE_FORMAT);
		dateFormatter.setTimeZone(TimeZone.getDefault());    		
	}

	/**
	 * Start receiving indicator data using the supplied parameters.
	 * 
	 * @param host The host to connect to
	 * @param port The port to use
	 * @param user user name
	 * @param pass password
	 * @param show True to show message details
	 */
	public void receive(String host, int port, String user, String pass, boolean show, boolean useSSL)
	{
		try 
		{
		    // connect to the server
			xferSocket = createSocket(host, port, useSSL);
			
			// setup some socket option to keep our connection alive
			xferSocket.setTcpNoDelay(true);
			xferSocket.setKeepAlive(true);			
			
			// setup the streams for read/write
	        dataOutStream = new DataOutputStream(xferSocket.getOutputStream());
	        dataInStream = new DataInputStream(xferSocket.getInputStream());	        
	        
	        // try to authenticate
	        if (negotiateAuth(user,pass))
	        {	            
	            // we're in, start reading messages 
	        	int count = 0;
			Date timestamp = null;

	        	do
	        	{	        		 
		        	// Read the header
		        	byte[] headerBytes = new byte[IndicatorMessageHeader.HEADER_SIZE_IN_BYTES];		        		        
		        	dataInStream.readFully(headerBytes);
		        	IndicatorMessageHeader header = new IndicatorMessageHeader(headerBytes);
		        	
		        	// Read the payload       	
		        	byte[] payloadBytes = new byte[header.getMessageLength()-IndicatorMessageHeader.HEADER_SIZE_IN_BYTES-IndicatorMessageHeader.INT_SIZE_IN_BYTES];		        	
		        	dataInStream.readFully(payloadBytes);	        	
		        	IndicatorMessagePayload payload = new IndicatorMessagePayload(payloadBytes);

	        		// this is the CRC
		        	byte[] crcBytes = new byte[4];
		        	dataInStream.readFully(crcBytes);

		        	timestamp = new Date();
		        	
		        	// construct an object from the bytes
		        	IndicatorMessage msg = new IndicatorMessage(header, payload, crcBytes);
		        	count++;
		        	
		        	// show some info on console
		        	if (show)
		        	{
		        		System.out.println("\nReceived Indicator:"+msg.toString()+" at "+ dateFormatter.format(timestamp));
		        		System.out.println("Hex:"+HexDump.dumpToString(msg.getBytes()));
		        	}
		        	else
		        	{
		        		System.out.println("\nReceive #"+count+" Indicator TxmitId:"+msg.getTxmitId()+" CRC:"+msg.getCRC()+" at "+ dateFormatter.format(timestamp));
		        	}
		        	
	        	} while(true);
	        }
	        else
	        {
	        	System.out.println("Login failed.");
	        }	        	        
	    } 
		catch (Exception e) 
		{
			System.out.println("Unexpected exception occurred:"+e.getMessage());
			e.printStackTrace();
	    }		
		finally
		{
	        try { if (dataOutStream != null) dataOutStream.close(); } catch(Exception e){};	    
	        try { if (dataInStream != null) dataInStream.close(); } catch(Exception e){};
	        try { if (xferSocket != null) xferSocket.close();	} catch(Exception e){};
	        
	        System.out.println("Closing up shop.");
		}
	}
	
	private Socket createSocket(String host, int port, boolean useSSL) 
		throws UnknownHostException, IOException
	{
		Socket socket = null;
		
		if (useSSL)
		{
		    socket = SSLSocketFactory.getDefault().createSocket(host, port);
		}
		else
		{
			socket = new Socket(host, port);
		}
		
		return socket;
	}

	/**
	 * Send the login to the Lightning Bolt v5.x server.
	 * 
	 * @param user username
	 * @param pass password
	 * @return boolean indicating status of authentication. 
	 * @throws IOException Socket can throw this exception
	 */
	private boolean negotiateAuth(String user, String pass) 
		throws IOException
	{
		boolean allowed = false;
		
		if (xferSocket != null && dataOutStream != null && dataInStream != null) 
		{
			xferSocket.getOutputStream().write( new String( "AUTH "+user+" "+pass+"\n\n" ).getBytes() );
			
			
			String responseLine;

			responseLine = dataInStream.readLine();			
            System.out.println("Server AUTH repsonse: " +  responseLine);
            if (responseLine.indexOf("OK") != -1) 
            {
            	allowed = true;
            	
                // next new line ???
    			responseLine = dataInStream.readLine();
    			
    			System.out.println("Connected and authorized, waiting for indicator messages.");    			
            }					
		}
		
		return allowed;
	}
	
}

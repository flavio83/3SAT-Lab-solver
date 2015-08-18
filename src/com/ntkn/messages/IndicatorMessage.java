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

package com.ntkn.messages;

import java.util.zip.CRC32;

/**
 * An indicator message consists of a header and a payload.
 */
public class IndicatorMessage 
{
    public static int TRAILER_SIZE_IN_BYTES = 4;	
	
    private String name;
    private String country; 
    private String mcastGroupName;
    private IndicatorMessageHeader header;
    private IndicatorMessagePayload payload;
    private long crc;

    /**
     * Construct an IndicatorMessage from a header and a payload.
     * The message CRC value will be set from the provided data.
     * 
     * @param header
     * @param payload
     */
    public IndicatorMessage(IndicatorMessageHeader header, IndicatorMessagePayload payload)
    {
        this.header = header;
        this.setPayload(payload);
    }
    
    /**
     * Construct an IndicatorMessage from a header and a payload and validate the
     * message CRC from the array of bytes. Throws an exception if CRC is not valid.  
     * 
     * @param header
     * @param payload
     * @param crcBytes
     * @throws IndicatorMessageInitializationException
     */
    public IndicatorMessage(IndicatorMessageHeader header, IndicatorMessagePayload payload, byte[] crcBytes)
    	throws IndicatorMessageInitializationException
    {
    	this(header,payload);
    	
    	// decode and check the CRC 
        long crc = ByteDecoder.unsignedIntByteArrayToLong(crcBytes, 0);
        if (crc != this.crc)        	
        	throw new IndicatorMessageInitializationException("Invalid CRC, "+crc+" != "+this.crc);   
    }    
    
    /**
     * Construct an IndicatorMessage from the array of bytes read from the network.
     * 
     * @param msgBytes
     * @throws IndicatorMessageInitializationException
     */
    public IndicatorMessage(byte[] msgBytes)
        throws IndicatorMessageInitializationException
    {
        this.setHeader(new IndicatorMessageHeader(msgBytes));
        this.setPayload(new IndicatorMessagePayload(msgBytes, IndicatorMessageHeader.HEADER_SIZE_IN_BYTES, 
        		this.header.getMessageLength()-IndicatorMessageHeader.HEADER_SIZE_IN_BYTES+IndicatorMessage.TRAILER_SIZE_IN_BYTES));        
        
        // check CRC
        long crc = ByteDecoder.unsignedIntByteArrayToLong(msgBytes, (this.header.getMessageLength()-IndicatorMessage.TRAILER_SIZE_IN_BYTES));
        if (crc != this.crc)
        	throw new IndicatorMessageInitializationException("Invalid CRC, "+crc+" != "+this.crc);        
    }    

    public IndicatorMessageHeader getHeader()
    {
        return header;
    }

    private void setHeader(IndicatorMessageHeader header)
    {
        this.header = header;
    }

    public IndicatorMessagePayload getPayload()
    {
        return payload;
    }

    public void setPayload(IndicatorMessagePayload payload)
    {
        this.payload = payload;
        this.header.setMessageLength(payload);
        this.setCRCValue();
    }
    
    public int getTxmitId()
    {
    	return header.messageTxmitId();
    }
    
    public int getMessageIdentifier()
    {
    	return header.getMessageIdentifier();
    }
    
    public String getName()
    {
        return name;
    }

    public String getCountry()
    {
        return country;
    }

    public int getMessageType()
    {
        return header.getMessageTypeId();
    }
    
    public long getCRC()
    {
    	return this.crc;
    }
        
    public String getMcastGroupName() {
		return mcastGroupName;
	}

	public void setMcastGroupName(String mcastGroupName) {
		this.mcastGroupName = mcastGroupName;
	}
	
	private void setCRCValue()
	{
		// Compute CRC-32 checksum
		byte[] bytes = this.getBytes();
		
		CRC32 checksumEngine = new CRC32();
	    checksumEngine.update(bytes, 0, bytes.length-TRAILER_SIZE_IN_BYTES);
	    this.crc = checksumEngine.getValue();
	}	

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (crc ^ (crc >>> 32));
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		result = prime * result + ((payload == null) ? 0 : payload.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final IndicatorMessage other = (IndicatorMessage) obj;
		if (crc != other.crc)
			return false;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		if (payload == null) {
			if (other.payload != null)
				return false;
		} else if (!payload.equals(other.payload))
			return false;
		return true;
	}

	@Override
    public String toString()
    {
	    if (name != null)
	        return "Indicator_Name:"+name+header.toString()+payload.toString()+" CRC:"+crc;
	    else
	        return "Indicator"+header.toString()+payload.toString()+" CRC:"+crc;
    }       
	
    public String toMultilineString()
    {
        if (name != null)
            return "IndicatorMessage "+name+":"+header.toMultilineString()+payload.toMultilineString()+" CRC:"+crc;
        else
            return "IndicatorMessage :"+header.toMultilineString()+payload.toMultilineString()+" CRC:"+crc;
    }
    
    /**
     * Get the byte[] consisting of header & payload.
     * 
     * @return byte[] suitable for marshalling over the wire.
     */
    public byte[] getBytes()
    {
       byte[] hdrBytes = this.header.getBytes();
       byte[] payloadBytes = this.payload.getBytes();     
       byte[] msgBytes = new byte[hdrBytes.length+payloadBytes.length+TRAILER_SIZE_IN_BYTES];
       
       // copy the header & payload into byte[]
       System.arraycopy(hdrBytes, 0, msgBytes, 0, hdrBytes.length);
       System.arraycopy(payloadBytes, 0, msgBytes, hdrBytes.length, payloadBytes.length);
       
       //byte[] crcBytes = ByteEncoder.intToByteArray(crc);
       byte[] crcBytes = ByteEncoder.longToUnsignedIntByteArray(crc);       
       System.arraycopy(crcBytes, 0, msgBytes, hdrBytes.length+payloadBytes.length, TRAILER_SIZE_IN_BYTES);
       
       return msgBytes;
    }
        
    public boolean isSystemHearbeat()
    {
        return (this.header.getMessageCategoryId() == 1);
    }
}

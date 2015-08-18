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

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * The indicator message header consisting of length, and identification fields for processing.
 */
public class IndicatorMessageHeader 
{
    private static Random RANDOM = new Random();
    private static final int BILLION = 1000000000;
    private static final int HUNDRED_THOUSAND = 100000;
    private static final int TEN_THOUSAND = 10000;
    
    public static int HEADER_SIZE_IN_BYTES = 10;	   
    
    public static int BYTE_SIZE_IN_BYTES = 1;    
    public static int SHORT_SIZE_IN_BYTES = 2;   
    public static int INT_SIZE_IN_BYTES = 4;        
    
    private char  messageLength;    
    private int   messageTxmitId;
    private byte  messageTypeId;
    private byte  messageVersion;
    private char  messageCategoryId;
    private int   messageIdentifier;
    
    public IndicatorMessageHeader(byte messageTypeId, 
                                  byte messageVersion, 
                                  char messageCategoryId, 
                                  IndicatorMessagePayload payload)
    {        
        this.messageTxmitId = this.createTxmitId(messageCategoryId, messageTypeId);
        this.messageTypeId = messageTypeId;
        this.messageVersion = messageVersion;        
        this.messageCategoryId = messageCategoryId;
        this.messageLength = (char)(payload.getPayloadSize()+IndicatorMessageHeader.HEADER_SIZE_IN_BYTES+IndicatorMessage.TRAILER_SIZE_IN_BYTES);
        this.messageIdentifier = createIdentifier(messageTypeId, messageVersion, messageCategoryId);
    }
    
	/**
	 * Construct the object from the primitive values.
	 * 
	 * @param messageTxmitId txmit id
	 * @param messageTypeId message type
	 * @param messageVersion message version
	 * @param messageCategoryId category id
	 * @param payload payload
	 */
    public IndicatorMessageHeader(int messageTxmitId, 
                                  byte messageTypeId, 
                                  byte messageVersion, 
                                  char messageCategoryId, 
                                  IndicatorMessagePayload payload)
    {
        this.messageTxmitId = messageTxmitId;
        this.messageTypeId = messageTypeId;
        this.messageVersion = messageVersion;        
        this.messageCategoryId = messageCategoryId;
        this.messageLength = (char)(payload.getPayloadSize()+IndicatorMessageHeader.HEADER_SIZE_IN_BYTES+IndicatorMessage.TRAILER_SIZE_IN_BYTES);
        this.messageIdentifier = createIdentifier(messageTypeId, messageVersion, messageCategoryId);
    }
    
    public IndicatorMessageHeader(byte messageTypeId, 
                                  byte messageVersion, 
                                  char messageCategoryId,
                                  char payloadLength)
    {
        this.messageTxmitId = this.createTxmitId(messageCategoryId, messageTypeId);
        this.messageTypeId = messageTypeId;
        this.messageVersion = messageVersion;        
        this.messageCategoryId = messageCategoryId;
        this.messageLength  = (char) (payloadLength +IndicatorMessageHeader.HEADER_SIZE_IN_BYTES+IndicatorMessage.TRAILER_SIZE_IN_BYTES);
        this.messageIdentifier = createIdentifier(messageTypeId, messageVersion, messageCategoryId);
    }
    
    public IndicatorMessageHeader(int messageTxmitId,
                                  byte messageTypeId, 
                                  byte messageVersion, 
                                  char messageCategoryId,
                                  char payloadLength)
        {
            this.messageTxmitId = messageTxmitId;
            this.messageTypeId = messageTypeId;
            this.messageVersion = messageVersion;        
            this.messageCategoryId = messageCategoryId;
            this.messageLength  = (char) (payloadLength +IndicatorMessageHeader.HEADER_SIZE_IN_BYTES+IndicatorMessage.TRAILER_SIZE_IN_BYTES);
            this.messageIdentifier = createIdentifier(messageTypeId, messageVersion, messageCategoryId);
        }    
        
    /**
     * Construct the object from an array of bytes.
     * 
     * @param msgBytes byte[] that consists of the bytes from an indicator message.
     * @throws com.ntkn.messages.IndicatorMessageInitializationException
     */
    public IndicatorMessageHeader(byte[] msgBytes)
        throws IndicatorMessageInitializationException
    {        
        if (msgBytes.length < HEADER_SIZE_IN_BYTES)
            throw new IndicatorMessageInitializationException("Initialization buffer too small.");
                
        this.messageLength = ByteDecoder.byteArrayToChar(msgBytes, 0);
        this.messageTxmitId = ByteDecoder.byteArrayToInt(msgBytes, 2);
        this.messageTypeId = msgBytes[6];
        this.messageVersion = msgBytes[7];
        this.messageCategoryId = ByteDecoder.byteArrayToChar(msgBytes, 8);
        this.messageIdentifier = createIdentifier(messageTypeId, messageVersion, messageCategoryId);        
    }    

    void setMessageLength(IndicatorMessagePayload payload)
    {
        messageLength = (char)(payload.getPayloadSize()+IndicatorMessageHeader.HEADER_SIZE_IN_BYTES+IndicatorMessage.TRAILER_SIZE_IN_BYTES);
    }    
    
    public int getMessageLength()
    {
        return messageLength;
    }

    public int messageTxmitId()
    {
        return messageTxmitId;
    }

    public int getMessageTypeId()
    {
        return messageTypeId;
    }

    public int getMessageCategoryId()
    {
        return messageCategoryId;
    }
   
    public int getMessageIdentifier() {
		return messageIdentifier;
	}
    
    public int getMessageTxmitId() {
		return messageTxmitId;
	}    
    
    public void resetMessageTxmitId()
    {
        messageTxmitId = this.createTxmitId(messageCategoryId, messageTypeId);
    }    

	public void setMessageTxmitId(int messageTxmitId)
    {
        this.messageTxmitId = messageTxmitId;
    }
       
    public byte getMessageVersion()
    {
        return messageVersion;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final IndicatorMessageHeader other = (IndicatorMessageHeader) obj;
        if (this.messageLength != other.messageLength)
        {
            return false;
        }
        if (this.messageTxmitId != other.messageTxmitId)
        {
            return false;
        }
        if (this.messageTypeId != other.messageTypeId)
        {
            return false;
        }
        if (this.messageCategoryId != other.messageCategoryId)
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + this.messageLength;
        hash = 67 * hash + this.messageTxmitId;
        hash = 67 * hash + this.messageTypeId;
        hash = 67 * hash + this.messageVersion;
        hash = 67 * hash + this.messageCategoryId;
        return hash;
    }

    /*
     * Get the message as a byte array suitable for network output.
     */ 
    public byte[] getBytes() 
    {
        byte[] hdrBytes = new byte[HEADER_SIZE_IN_BYTES];
        
        // Convert to byte array
        //byte[] lenBytes = ByteEncoder.shortToByteArray(messageLength);
        byte[] lenBytes = ByteEncoder.charToByteArray(messageLength);
        byte[] txmitIdBytes = ByteEncoder.intToByteArray(messageTxmitId);        
        byte typeIdBytes = new Integer(messageTypeId).byteValue();
        byte versionBytes = new Integer(messageVersion).byteValue();        
        byte[] categoryBytes = ByteEncoder.charToByteArray(messageCategoryId);       
        
        // concatenate to single byte array suitable for network output
        System.arraycopy(lenBytes, 0, hdrBytes, 0, SHORT_SIZE_IN_BYTES);
        System.arraycopy(txmitIdBytes, 0, hdrBytes, 2, INT_SIZE_IN_BYTES);
        hdrBytes[6] = typeIdBytes;
        hdrBytes[7] = versionBytes;
        
        // skip the LSB below
        System.arraycopy(categoryBytes, 0, hdrBytes, 8, SHORT_SIZE_IN_BYTES);
        
        return hdrBytes;
    }

    @Override
    public String toString()
    {
        StringBuffer retStringBuff = new StringBuffer();
        
        retStringBuff.append(" Length:"+(int)messageLength);
        retStringBuff.append(" Transmit_Id:"+messageTxmitId);
        retStringBuff.append(" Type_Id:"+messageTypeId);
        retStringBuff.append(" Version:"+messageVersion);       
        retStringBuff.append(" Category:"+(int)messageCategoryId);
        retStringBuff.append(" Identifier:"+messageIdentifier+" ");
        
        return retStringBuff.toString();
    }
    
    public String toMultilineString()
    {
        StringBuffer retStringBuff = new StringBuffer("IndicatorMessageHeader:\r\n");
        
        retStringBuff.append("Length:"+(int)messageLength);
        retStringBuff.append("\r\n");
        retStringBuff.append("Transmit_Id:"+messageTxmitId);
        retStringBuff.append("\r\n");
        retStringBuff.append("Type_Id:"+messageTypeId);
        retStringBuff.append("\r\n");
        retStringBuff.append("Version:"+messageVersion);
        retStringBuff.append("\r\n");        
        retStringBuff.append("Category:"+(int)messageCategoryId);
        retStringBuff.append("\r\n");
        retStringBuff.append("Identifier:"+messageIdentifier);
        retStringBuff.append("\r\n");
        
        return retStringBuff.toString();
    }
    
    /**
     * Combine the message attributes into a single value that 
     * can be used as an identifier.
     * 
     * @param type message type
     * @param version message version
     * @param id category id
     * @return integer representing an identifier for the message.
     */
    private int createIdentifier(byte type, byte version, char category)
    {
    		byte[] idBytes = new byte[INT_SIZE_IN_BYTES];

            idBytes[0] = type;
            idBytes[1] = version;
            idBytes[2] = (byte)((category) >> 8);          
            idBytes[3] = (byte)(category); 
            
    		return idBytes[0]<<24 | (idBytes[1]&0xff)<<16 | (idBytes[2]&0xff)<<8 | (idBytes[3]&0xff);
    }
    
    /**
     * Create a Txmit id based on category id and days since epoch.
     * Days since epoch can be simulated with:
     * 
     *  perl -e 'printf qq{%d\n},time/86400'
     *  
     * If message category is less than 10, a random number will be used. 
     * 
     * @param messageCategoryId
     * @return int the category id
     */
    private int createTxmitId(char messageCategoryId, byte messageTypeId)
    {
        int txmitId = 0;
        
        // heartbeats, system test messages use random numbers
        if (messageCategoryId < 10)
        {
            txmitId = IndicatorMessageHeader.RANDOM.nextInt();
        }
        else
        {
            long now = System.currentTimeMillis();
            int today = (int)TimeUnit.MILLISECONDS.toDays(now);

            // shift the individual numbers using multipliers to concatenate
            txmitId = (messageTypeId*BILLION) + ((today-TEN_THOUSAND)*HUNDRED_THOUSAND)+messageCategoryId;
        }
                
        return txmitId;                    
    }
    
    /**
     * Create a Txmit id based on category id, days since epoch and an increment
     * Days since epoch can be simulated with:
     * 
     *  perl -e 'printf qq{%d\n},time/86400'
     *  
     * If message category is less than 10, a random number will be used. 
     * 
     * @param messageCategoryId
     * @param messageTypeId
     * @param increment
     * @return int the createTxmitId
     */
     private int createTxmitId(char messageCategoryId, byte messageTypeId, int increment)
     {   	    	    	
         int txmitId = 0;
         int rnd = 0;
         
         // heartbeats, system test messages use random numbers
         if (messageCategoryId < 10)
         {
         	rnd = IndicatorMessageHeader.RANDOM.nextInt();
             txmitId = (rnd + increment >= Integer.MAX_VALUE)?(rnd + increment - 1000): (rnd + increment); 
             
         }
         else
         {
             long now = System.currentTimeMillis();
             int today = (int)TimeUnit.MILLISECONDS.toDays(now);
             
             // shift the individual numbers using multipliers to concatenate           
            txmitId = (messageTypeId*BILLION) + ((today-TEN_THOUSAND)*HUNDRED_THOUSAND) + (increment*TEN_THOUSAND) + messageCategoryId;
            
         }                     
         
         return txmitId;                    
     }


 /**
      * Increments the messageTxmitId by increment * ten thousand.
      * Used when need to ensure unique messageTxmitId
      * 
      * @param increment
      */
 	public void incrementMessageTxmitId(int increment)
     {
 		this.messageTxmitId = createTxmitId(this.messageCategoryId, this.messageTypeId, increment);
     }
}

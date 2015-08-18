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

import com.ntkn.util.structs.Date;
import com.ntkn.util.structs.ISOCountryCode;
import com.ntkn.util.structs.Time;
import com.ntkn.util.structs.TimeFrame;

public class ByteEncoder 
{   
	/**
     * Convert the int value to a 4-byte array in 
     * Network Byte Order representation.
     *
     * @param value The integer value
     * @return The byte array
     */
    public static final byte[] intToByteArray(int value)
    {
    	byte[] bArray = new byte[4];
    	
    	bArray[0] = (byte) ((value >>> 24) & 0xFF);
    	bArray[1] = (byte) ((value >>> 16) & 0xFF);
    	bArray[2] = (byte) ((value >>> 8) & 0xFF);
        bArray[3] = (byte) (value & 0xFF);
        
    	return bArray;     	
    }
    
	/**
     * Convert the long value to a 4-byte array in 
     * Network Byte Order representation. The array
     * is essentially an unsigned integer.
     *
     * @param value The long value
     * @return The byte array
     */
    public static final byte[] longToUnsignedIntByteArray(long value)
    {
    	byte[] bArray = new byte[4];

    	bArray[0] = (byte) ((value & 0xFF000000L) >> 24);
    	bArray[1] = (byte) ((value & 0x00FF0000L) >> 16);
    	bArray[2] = (byte) ((value & 0x0000FF00L) >> 8);
    	bArray[3] = (byte) (value & 0x000000FFL);
        
    	return bArray;     	
    }    
    
    /**
     * Convert the long value to a 8-byte array in 
     * Network Byte Order representation. 
     *
     * @param value The long value
     * @return The byte array
     */    
    public static final byte[] longByteArray(long value)
    {
        byte[] bArray = new byte[8];
        
        bArray[0] = (byte)(value >>> 56);
        bArray[1] = (byte)(value >>> 48);
        bArray[2] = (byte)(value >>> 40);
        bArray[3] = (byte)(value >>> 32);
        bArray[4] = (byte)(value >>> 24);
        bArray[5] = (byte)(value >>> 16);
        bArray[6] = (byte)(value >>>  8);
        bArray[7] = (byte)(value >>>  0);        
        
        return bArray;      
    }    
    
    
    /**
     * Convert the short value to a 2-byte array in 
     * Network Byte Order representation.
     *
     * @param value The integer value
     * @return The byte array
     */
    public static final byte[] shortToByteArray(short value)
    {
    	byte[] bArray = new byte[2];
    	
    	bArray[0] = (byte) ((value >>> 8) & 0xFF);
        bArray[1] = (byte) (value & 0xFF);
        
    	return bArray;     	
    }
    
    /**
     * Convert the char (unsigned short) value to a 2-byte array in 
     * Network Byte Order representation.
     *
     * @param value The char value
     * @return The byte array
     */
    public static final byte[] charToByteArray(char value)
    {
        byte[] bArray = new byte[2];
                  
        bArray[0] = (byte)((value & 0xFF00) >> 8);        
        bArray[1] = (byte)(value & 0x00FF);
        
        return bArray;      
    }    
    
    /**
     * Convert the Date structure into a 4-byte array. Initial short year 
     * is converted into Network Byte Order representation within the 
     * resulting byte array.
     * 
     * @param value The Date value
     * @return The byte array
     */
    public static final byte[] dateToByteArray(Date value)
    {
        byte[] bArray = new byte[value.getStructLength()];
        bArray[0] = (byte) ((value.getYear() >>> 8) & 0xFF);
        bArray[1] = (byte) (value.getYear() & 0xFF);
        bArray[2] = value.getMonth();        
        bArray[3] = value.getDay();
        
        return bArray;
    }
    
    /**
     * Convert the Time structure into a 3-byte array.
     *  
     * @param value The Date value
     * @return The byte array
     */
    public static final byte[] timeToByteArray(Time value)
    {
        byte[] bArray = new byte[value.getStructLength()];
        bArray[0] = value.getHours();
        bArray[1] = value.getMinutes();
        bArray[2] = value.getSeconds();        
        
        return bArray;        
    }

    /**
     * Convert the TimeFrame structure into a 4-byte array.
     *  
     * @param value The Date value
     * @return The byte array
     */
    public static final byte[] timeFrameToByteArray(TimeFrame value)
    {
        byte[] bArray = new byte[value.getStructLength()];
        bArray[0] = value.getPeriod();
        bArray[1] = value.getYearOffset();
        bArray[2] = (byte) ((value.getPeriodValue() >>> 8) & 0xFF);
        bArray[3] = (byte) (value.getPeriodValue() & 0xFF);                
        
        return bArray;        
    }    
    
    /**
     * Convert the ISOCountryCode structure into a 2-byte array.
     *  
     * @param value The ISOCountryCode value
     * @return The byte array
     */
    
    public static final byte[] ISOCountryCodeToByteArray(ISOCountryCode value)
    {
               
        return value.getISOCountryCode().getBytes();        
    }

}

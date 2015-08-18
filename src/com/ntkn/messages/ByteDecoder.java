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

import java.nio.ByteBuffer;

import com.ntkn.util.InvalidFormatException;
import com.ntkn.util.structs.Date;
import com.ntkn.util.structs.ISOCountryCode;
import com.ntkn.util.structs.Time;
import com.ntkn.util.structs.TimeFrame;

public class ByteDecoder 
{
	/**
     * Convert the Network Byte Order byte array
     * to an int value.
     *
     * @param bArray the byte[]
     * @return int value
     */
    public static final int byteArrayToInt(byte [] bArray) 
    {       
    	return ( (0xff & bArray[0]) << 24 |
    			 (0xff & bArray[1]) << 16 |
    			 (0xff & bArray[2]) << 8 |
    			 (0xff & bArray[3]) << 0 );
    }
    
    /**
     * Convert the Network Byte Order byte array
     * to an int value.
     *
     * @param bArray the byte[]
     * @return long value
     */
    public static final long byteArrayToLong(byte [] bArray) 
    {               
        ByteBuffer b = ByteBuffer.wrap(bArray);
        return b.getLong();        
    }
    
    public static final long byteArrayToLong(byte [] bArray, int offset) 
    {
        ByteBuffer b = ByteBuffer.wrap(bArray, offset, 8);
        return b.getLong();
    }    
        
    /**
     * Convert the Network Byte Order byte array
     * to an short value.
     *
     * @param bArray the byte[] 
     * @return short value
     */
    public static final short byteArrayToShort(byte [] bArray) 
    {       
    	return (short)( (0xff & bArray[0]) << 8 |
    			        (0xff & bArray[1]) << 0 );
    }    
       
   /**
     * Convert the Network Byte Order byte array 
     * to an int value.
     *
     * @param bArray the byte[] 
     * @param offset offset into array
     * @return int value
     */
    public static final int byteArrayToInt(byte [] bArray, int offset) 
    {        
    	return ( (0xff & bArray[offset]) << 24 |
    			      (0xff & bArray[offset+1]) << 16 |
    			      (0xff & bArray[offset+2]) << 8 |
    			      (0xff & bArray[offset+3]) << 0	);    	
    }
    
    /**
     * Convert the Network Byte Order byte array 
     * to an long value. The array is assumed to be a
     * an unsigned integer.
     *
     * @param bArray the byte[] 
     * @param offset offset into array
     * @return long value
     */
    public static final long unsignedIntByteArrayToLong(byte [] bArray, int offset) 
    {           
    	return (((0x000000FF & (bArray[offset])) << 24   |
				 		(0x000000FF & (bArray[offset+1])) << 16 |
				 		(0x000000FF & (bArray[offset+2])) << 8  |
				 		(0x000000FF & (bArray[offset+3]))))
				 		& 0xFFFFFFFFL;    	    	
    }       
    
    
    /**
     * Convert a single byte from an array at 
     * offset to an int value.
     *
     * @param bArray the byte[] 
     * @param offset offset into array
     * @return int value
     */
    public static final int byteToInt(byte [] bArray, int offset) 
    {        
    	return bArray[offset] & 0xff;    	
    }    
    
    /**
     * Convert the Network Byte Order byte array 
     * to an short value.
     *
     * @param bArray the byte[] 
     * @param offset offset into array
     * @return short value
     */
    public static final short byteArrayToShort(byte [] bArray, int offset) 
    {        
    	return (short)( (0xff & bArray[offset]) << 8 |
    			        (0xff & bArray[offset+1]) << 0 );    	
    }    

    /**
     * Convert the Network Byte Order byte array 
     * to an char value.
     * 
     * @param bArray the byte[] 
     * @param offset offset into array
     * @return char value
     */
    public static final char byteArrayToChar(byte [] bArray, int offset) 
    {                
        return (char)((0x000000FF & (bArray[offset])) << 8 | 
                      (0x000000FF & (bArray[offset+1])));
    }    
    
    public static final Date byteArrayToDate(byte [] bArray, int offset)
        throws InvalidFormatException
    {
        short year = byteArrayToShort(bArray, offset);
        byte month = bArray[offset+2];
        byte day = bArray[offset+3];
        
        return new Date(year, month, day);
    }
    
    public static final Time byteArrayToTime(byte [] bArray, int offset)
        throws InvalidFormatException    
    {
        byte hours = bArray[offset+0];
        byte minutes = bArray[offset+1];
        byte seconds = bArray[offset+2];
        
        return new Time(hours, minutes, seconds);
    }
    
    public static final TimeFrame byteArrayToTimeFrame(byte [] bArray, int offset)
    	throws InvalidFormatException    
    {
    	byte period = bArray[offset+0];
    	byte yearOffset = bArray[offset+1];
    	short periodvalue = byteArrayToShort(bArray, offset+2);
    
    	return new TimeFrame(period, yearOffset, periodvalue);
    }    
    
    /**
     * Converts a 2-byte array into a ISOCountryCode
     * 
     * @param bArray
     * @param offset
     * @return 
     * @throws InvalidFormatException 
     */
    
    public static final ISOCountryCode byteArrayToISOCountryCode(byte [] bArray, int offset)
            throws InvalidFormatException
    {    	
    	String iso = byteArrayToString(bArray, offset, 2);
    	return new ISOCountryCode(iso);
    }
   
    /**
     * 
     * Converts the specified number of bytes from a byte array starting from the offset into a String. 
     * 
     * @param bArray
     * @param offset
     * @param length
     * @return
     */
    
	private static String byteArrayToString(byte[] bArray, int offset, int length)
	{
		return new String(bArray, offset, length);
	}

    
}

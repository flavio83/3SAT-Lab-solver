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

import com.ntkn.util.InvalidFormatException;
import com.ntkn.util.structs.Date;
import com.ntkn.util.structs.ISOCountryCode;
import com.ntkn.util.structs.Time;
import com.ntkn.util.structs.TimeFrame;

/**
 * An individual field of an indicator message. Indicators consist of 1 or more indicator fields.
 */
public class IndicatorMessageField implements Comparable<IndicatorMessageField>
{
    private static int FLOAT_SIZE_IN_BYTES = 4;
    private static int SHORT_SIZE_IN_BYTES = 2;  
    public  static int MINIMUM_SIZE_IN_BYTES = SHORT_SIZE_IN_BYTES+1;
    
    private String shortDescription;
    private byte fieldType;
    private byte fieldId;
    private short fieldIdentifier;
    private Object fieldValue;
    
    public IndicatorMessageField(byte fieldType, byte fieldId, Object fieldValue)
    {        
    	this.fieldType = fieldType;    	
        this.fieldId = fieldId;
        this.fieldIdentifier = bytesToShort(fieldType, fieldId);
        this.fieldValue = fieldValue;
    }
    
    public IndicatorMessageField(int fieldType, int fieldId, Object fieldValue)    
    {
    	this( (byte)(fieldType & 0xFF), (byte)(fieldId & 0xFF), fieldValue);
    }    
    
    public IndicatorMessageField(byte[] fieldBytes)
        throws IndicatorMessageInitializationException
    {
        if (fieldBytes.length < MINIMUM_SIZE_IN_BYTES)
            throw new IndicatorMessageInitializationException("Initialization buffer too small.");
        
        validateFieldType(fieldType);        
        
        this.fieldType = fieldBytes[0];
        this.fieldId = fieldBytes[1];
        this.fieldIdentifier = bytesToShort(fieldType, fieldId);
                
        this.fieldValue = getFieldValue(fieldType, fieldBytes);
    }

    public IndicatorMessageField(byte[] fieldBytes, int offset)
        throws IndicatorMessageInitializationException
    {
        if (fieldBytes.length-offset < MINIMUM_SIZE_IN_BYTES)
            throw new IndicatorMessageInitializationException("Initialization buffer too small.");
        
        validateFieldType(fieldType);
                              
        this.fieldType = fieldBytes[offset];
        this.fieldId = fieldBytes[offset+1];
        this.fieldIdentifier = bytesToShort(fieldType, fieldId);
       
        this.fieldValue = getFieldValue(fieldType, fieldBytes, offset+2);
    }
    
    private void validateFieldType(int fieldType)
        throws IndicatorMessageInitializationException
    {
        IndicatorFieldType ifieldType = IndicatorFieldType.getIndicatorFieldType(fieldType);
        if (ifieldType == null)
            throw new IndicatorMessageInitializationException("Invalid Field Type Specified:"+fieldType);    
    }
    
    private void setFieldValueFromArray(byte[] fieldBytes, int offset)
    {        
        this.fieldValue = getFieldValue(this.fieldType, fieldBytes, offset);        
    }

    public short getfieldIdentifier()
    {
        return fieldIdentifier;
    }
    
    public byte getFieldType() {
		return fieldType;
	}

	public byte getFieldId() {
		return fieldId;
	}

	public void setfieldId(byte fieldId)
    {
        this.fieldId = fieldId;
    }
    
    public void setfieldType(byte fieldType)
    {
        this.fieldType = fieldType;
    }    

    public Object getfieldValue()
    {
        return fieldValue;
    }

    public void setfieldValue(Object fieldValue)
    {
        this.fieldValue = fieldValue;
    }
    
    public String getShortDescription()
    {
        return shortDescription;
    }

    private short bytesToShort(byte type, byte id) 
    {        
    	return (short)( (0xff & type) << 8 |
    			        (0xff & id) << 0 );    	
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
        final IndicatorMessageField other = (IndicatorMessageField) obj;
        if (this.fieldId != other.fieldId)
        {
            return false;
        }
        if (this.fieldValue != other.fieldValue && (this.fieldValue == null || !this.fieldValue.equals(other.fieldValue)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 29 * hash + this.fieldId;
        hash = 29 * hash + (this.fieldValue != null ? this.fieldValue.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString()
    {
        if (shortDescription != null)
            return "Field_Name:\""+shortDescription+"\" Field_Id:"+fieldId+" Field_Type:"+fieldType+" Value:"+fieldValue;
        else
            return "Field_Id:"+fieldId+" Value:"+fieldValue;
    }
    
    public String toFormattedString()
    {
        if (shortDescription != null)
            return fieldId+". \""+shortDescription+"\" "+fieldValue;
        else
            return fieldId+". "+fieldValue;
    }    
    
    private Object getFieldValue(byte fieldType, byte[] fieldBytes)
    {        
        IndicatorFieldType ifieldType = IndicatorFieldType.getIndicatorFieldType(fieldType);
        
        switch (ifieldType)
        {
            case FLOAT:                
                int floatIntBits = ByteDecoder.byteArrayToInt(fieldBytes, SHORT_SIZE_IN_BYTES);        
                return new Float(Float.intBitsToFloat(floatIntBits));                
                
            case FLOAT_RANGE:
                
                break;
                
            case SHORT:
            case SHORT_VALUE_ENUMERATION:
                short val = ByteDecoder.byteArrayToShort(fieldBytes, 2);
                return new Short(val);
                
            case INT:
                int val2 = ByteDecoder.byteArrayToInt(fieldBytes, 2);
                return new Integer(val2);                

            case LONG:
                long val1 = ByteDecoder.byteArrayToLong(fieldBytes, 2);
                return new Long(val1);


            case DOUBLE:
                long doublelongBits = ByteDecoder.byteArrayToLong(fieldBytes, 2);
                return Double.longBitsToDouble(doublelongBits);
                
            case DATE:
                try
                {
                    return  ByteDecoder.byteArrayToDate(fieldBytes, 2);
                }
                catch (InvalidFormatException e)
                {
                    // log it, requires dependency within example code of log4j
                    // should throw exception
                    //return null now
                }
                break;

            case TIME:
                try
                {
                    return  ByteDecoder.byteArrayToTime(fieldBytes, 2);
                }
                catch (InvalidFormatException e)
                {
                    // log it, requires dependency within example code of log4j
                    // should throw exception
                    //return null now
                }
                break;
                
            case TIMEFRAME:
                try
                {
                    return  ByteDecoder.byteArrayToTimeFrame(fieldBytes, 2);
                }
                catch (InvalidFormatException e)
                {
                    // log it, requires dependency within example code of log4j
                    // should throw exception
                    //return null now
                }
                break;                

            case BOOLEAN:
                if (fieldBytes[2] == 0)
                {
                    return Boolean.FALSE;
                }
                else
                {
                    return Boolean.TRUE;
                }
                
            case YES_NO_NA:
            case DIRECTIONAL:
            case ENUMERATION:            	
                    return new Byte(fieldBytes[2]);              
            case ISO_COUNTRY_CODE:
            	try
            	{
            		return ByteDecoder.byteArrayToISOCountryCode(fieldBytes, 2);
            	}
                catch (InvalidFormatException e)
                {
                    // log it, requires dependency within example code of log4j
                    // should throw exception
                    //return null now
                }
                break;  
                
            default:
                // uh oh! unknown type returning null
                break;
        }        
        
        return null;
    }
        
    private Object getFieldValue(byte fieldType, byte[] ifieldBytes, int offset)
    {                
        IndicatorFieldType ifieldType = IndicatorFieldType.getIndicatorFieldType(fieldType);
            
        byte fieldBytes[] = new byte[ifieldType.getLengthInBytes()];
        System.arraycopy(ifieldBytes, offset, fieldBytes, 0, ifieldType.getLengthInBytes());        
        
        switch (ifieldType)
        {
            case FLOAT:
                int floatIntBits = ByteDecoder.byteArrayToInt(fieldBytes);        
                return new Float(Float.intBitsToFloat(floatIntBits));                
                
            case FLOAT_RANGE:
                
                break;
                
            case SHORT:
            case SHORT_VALUE_ENUMERATION:
                short val = ByteDecoder.byteArrayToShort(fieldBytes);
                return new Short(val);

            case INT:
                int val2 = ByteDecoder.byteArrayToInt(fieldBytes);
                return new Integer(val2);
                
            case LONG:
                long val1 = ByteDecoder.byteArrayToLong(fieldBytes, 0);
                return new Long(val1);

            case DOUBLE:
                long doublelongBits = ByteDecoder.byteArrayToLong(fieldBytes);
                return Double.longBitsToDouble(doublelongBits);
                
            case DATE:                
                try
                {
                    return  ByteDecoder.byteArrayToDate(fieldBytes, 0);
                }
                catch (InvalidFormatException e)
                {
                    // log it, requires dependency within example code of log4j
                    // should throw exception
                    // return null now 
                }
                break;

            case TIME:
                try
                {
                    return  ByteDecoder.byteArrayToTime(fieldBytes, 0);
                }
                catch (InvalidFormatException e)
                {
                    // log it, requires dependency within example code of log4j
                    // should throw exception
                    //return null now
                }
                break;

            case TIMEFRAME:
                try
                {
                    return  ByteDecoder.byteArrayToTimeFrame(fieldBytes, 0);
                }
                catch (InvalidFormatException e)
                {
                    // log it, requires dependency within example code of log4j
                    // should throw exception
                    //return null now
                }
                break;
                
            case BOOLEAN:
                if (fieldBytes[0] == 0)
                {
                    return Boolean.FALSE;
                }
                else
                {
                    return Boolean.TRUE;
                }

            case YES_NO_NA:
            case DIRECTIONAL:
            case ENUMERATION:            	
                    return new Byte(fieldBytes[0]);
                    
            case ISO_COUNTRY_CODE:
            	try
            	{
            		return ByteDecoder.byteArrayToISOCountryCode(fieldBytes, 0);
            	}
                catch (InvalidFormatException e)
                {
                    // log it, requires dependency within example code of log4j
                    // should throw exception
                    //return null now
                }
                break;
                
            default:
                // uh oh! unknown type returning null
                break;
        }        
        
        return null;
    }
            
    public byte[] getBytes()
    {
        IndicatorFieldType fieldType = IndicatorFieldType.getIndicatorFieldType(this.fieldType);        
        byte[] fieldBytes = new byte[SHORT_SIZE_IN_BYTES+fieldType.getLengthInBytes()];
        fieldBytes[0] = this.fieldType;
        fieldBytes[1] = this.fieldId;
        
        switch (fieldType)
        {
            case FLOAT:
                    byte[] floatBytes = ByteEncoder.intToByteArray(Float.floatToIntBits(((Float)fieldValue).floatValue()));
                    System.arraycopy(floatBytes , 0, fieldBytes, 2, fieldType.getLengthInBytes());                
                break;
                
            case FLOAT_RANGE:
                
                break;
                
            case SHORT:
            case SHORT_VALUE_ENUMERATION:
                    byte[] shortBytes = ByteEncoder.shortToByteArray(((Short)fieldValue).shortValue());
                    System.arraycopy(shortBytes, 0, fieldBytes, 2, fieldType.getLengthInBytes());                                
                break;
                
            case INT:
                    byte[] intBytes = ByteEncoder.intToByteArray((Integer)fieldValue);
                    System.arraycopy(intBytes , 0, fieldBytes, 2, fieldType.getLengthInBytes());                
                break;                

            case LONG:
                    byte[] longBytes = ByteEncoder.longByteArray(((Long)fieldValue).longValue());
                    System.arraycopy(longBytes, 0, fieldBytes, 2, fieldType.getLengthInBytes());                
                break;

            case DOUBLE:
                    byte[] doubleBytes = ByteEncoder.longByteArray(Double.doubleToRawLongBits(((Double)fieldValue).doubleValue()));
                    System.arraycopy(doubleBytes, 0, fieldBytes, 2, fieldType.getLengthInBytes());                                
                break;

            case DATE:
                    byte[] dateBytes = ByteEncoder.dateToByteArray((Date)fieldValue);
                    System.arraycopy(dateBytes, 0, fieldBytes, 2, fieldType.getLengthInBytes());                                                
                break;
                
            case TIME:
                byte[] timeBytes = ByteEncoder.timeToByteArray((Time)fieldValue);
                System.arraycopy(timeBytes, 0, fieldBytes, 2, fieldType.getLengthInBytes());                                                
            break;                
            
            case TIMEFRAME:
                byte[] timeFrameBytes = ByteEncoder.timeFrameToByteArray((TimeFrame)fieldValue);
                System.arraycopy(timeFrameBytes, 0, fieldBytes, 2, fieldType.getLengthInBytes());                                                
            break;            

            case BOOLEAN:
                if (((Boolean)fieldValue).equals(Boolean.FALSE))
                {
                    fieldBytes[2] = (byte)0;
                }
                else
                {
                    fieldBytes[2] = (byte)1;
                }
                break;
                
            case YES_NO_NA:
            case DIRECTIONAL:
            case ENUMERATION:            	
                    byte valueByte = ((Byte)fieldValue).byteValue();
                    fieldBytes[2] = valueByte;                                                
                break;
                
            case ISO_COUNTRY_CODE:
            	 byte[] isoCountryCodeBytes = ByteEncoder.ISOCountryCodeToByteArray((ISOCountryCode)fieldValue);
                 System.arraycopy(isoCountryCodeBytes, 0, fieldBytes, 2, fieldType.getLengthInBytes());
                break; 
                
            default:
                // uh oh! unknown type returning null
                break;
        }
                
        return fieldBytes;
    }    
        
    public int compareTo(IndicatorMessageField otherObject)
    {
        Integer id = (int)this.fieldId; 
        return id.compareTo((int)otherObject.fieldId);
    }

}

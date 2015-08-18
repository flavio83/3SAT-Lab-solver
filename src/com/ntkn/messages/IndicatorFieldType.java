package com.ntkn.messages;

import com.ntkn.util.InvalidFormatException;
import com.ntkn.util.structs.Date;
import com.ntkn.util.structs.ISOCountryCode;
import com.ntkn.util.structs.Time;
import com.ntkn.util.structs.TimeFrame;

public enum IndicatorFieldType 
{
    FLOAT(0, 4), 
    FLOAT_RANGE(1,8), 
    SHORT(2,2), 
    LONG(3,8), 
    DOUBLE(4,8), 
    DATE(5,4), 
    BOOLEAN(6,1), 
    YES_NO_NA(7,1), 
    DIRECTIONAL(8,1), 
    INT(9,4), 
    TIME(10,3),
    TIMEFRAME(11,4),
    ENUMERATION(12,1),
    ISO_COUNTRY_CODE(13,2),
    SHORT_VALUE_ENUMERATION(14,2);

    private byte fieldTypeValue;
    private int lengthInBytes;

    private IndicatorFieldType(int fieldTypeValue, int lengthInBytes)
    {
        this.fieldTypeValue = (byte) fieldTypeValue;
        this.lengthInBytes = lengthInBytes;
    }

    public byte getFieldTypeValue()
    {
        return fieldTypeValue;
    }
    
    public int getLengthInBytes()
    {
        return lengthInBytes;
    }

    public static IndicatorFieldType getIndicatorFieldType(int fieldTypeValue)
    {
        IndicatorFieldType[] types = IndicatorFieldType.values();
        for (IndicatorFieldType indicatorFieldType : types)
        {
            if (indicatorFieldType.fieldTypeValue == fieldTypeValue)
                return indicatorFieldType;
        }
        
        return null;
    }
    
    public static Object coerseString(IndicatorFieldType type, String value) throws InvalidFormatException{
    	switch (type) {
		case FLOAT:
			return Float.parseFloat(value);
		case SHORT:
		case SHORT_VALUE_ENUMERATION:
			return Short.parseShort(value);
		case LONG:
			return Long.parseLong(value);
		case DOUBLE:
			return Double.parseDouble(value);
		case DATE:
			return new Date(value);
		case BOOLEAN:
			return Boolean.parseBoolean(value);
		case YES_NO_NA:
		case ENUMERATION:
		case DIRECTIONAL:
			return Byte.parseByte(value);
		case INT:
			return Integer.parseInt(value);
		case TIME:
			return new Time(value);
		case ISO_COUNTRY_CODE:
			return new ISOCountryCode(value);
		
		
		}
    	
    	throw new IllegalArgumentException("Unknown type: " + type);
    }
    
    public static Object coerseString(int type, String value) throws InvalidFormatException{
    	return coerseString(getIndicatorFieldType(type), value);
    }
        
}

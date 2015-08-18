package com.ntkn.util.structs;

import java.util.StringTokenizer;

import com.ntkn.messages.IndicatorFieldType;
import com.ntkn.util.InvalidFormatException;

public class Time
{
    private static final String DELIMITER           = ":";
    private static final String FORMAT              = "hh:mm:ss";
    private static final String DEFAULT_SECONDS     = ":00";
    private static final String ZERO_FILL           = "0";
    
    private static final int    MIN_EXPECTED_TOKENS = 2;    
    private static final int    MAX_EXPECTED_TOKENS = 3;
    
    private String timeStr;
    private byte   hours;
    private byte   minutes;
    private byte   seconds;
    
    public Time(String timeStr)
        throws InvalidFormatException
    {
        super();
        init(timeStr);
        formatInternalString();        
    }
        
    public Time(byte hours, byte minutes, byte seconds)
        throws InvalidFormatException    
    {
        super();
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        formatInternalString();        
    }    
    
    private void formatInternalString()
        throws InvalidFormatException    
    {
        StringBuilder strBld = new StringBuilder();
        appendByte(strBld, hours);
        strBld.append(DELIMITER);
        appendByte(strBld, minutes);
        strBld.append(DELIMITER);
        appendByte(strBld, seconds);
            
        this.timeStr = strBld.toString();
        
        if (isValidTime()==false)
        {
            throw new InvalidFormatException("Invalide Time:"+this.timeStr);
        }        
    }
    
    public boolean isValidTime() 
    {
        if (hours < 0 || hours > 24)
            return false;
        
        if (minutes < 0 || minutes > 59)
            return false;
        
        if (seconds < 0 || seconds > 59)
            return false;
        
        return true;
    }
    
    private void appendByte(StringBuilder strBld, byte value)
    {
        if (value < 10)
        {
            strBld.append(ZERO_FILL).append(value);
        }
        else
        {
            strBld.append(value);
        }        
    }
    
    private void init(String timeStr) 
        throws InvalidFormatException
    {
        StringTokenizer st = new StringTokenizer(timeStr, DELIMITER);
        int actualTokens = st.countTokens(); 
                
        if (actualTokens < MIN_EXPECTED_TOKENS || actualTokens > MAX_EXPECTED_TOKENS)
            throw new InvalidFormatException("Expected "+ FORMAT +" format.");
        
        try
        {
            int tokenCount = 0;
            while (st.hasMoreTokens()) 
            {
                String token = st.nextToken();            
                switch (tokenCount)
                {
                    case 0:
                        hours = Byte.parseByte(token);
                        break;
                        
                    case 1:
                        minutes = Byte.parseByte(token);
                        break;
   
                    case 2:
                        seconds = Byte.parseByte(token);
                        break;
   
                    default:
                        break;
                }
                
                tokenCount++;
            }        
            
//            if (actualTokens == MIN_EXPECTED_TOKENS)
//            {
//                timeStr = timeStr + DEFAULT_SECONDS;
//            }
        }
        catch (Exception e)
        {
            throw new InvalidFormatException("Unexpected format.", e); 
        }
    }

    public byte getHours()
    {
        return hours;
    }

    public byte getMinutes()
    {
        return minutes;
    }

    public byte getSeconds()
    {
        return seconds;
    }
    
    public int getStructLength()
    {
        return IndicatorFieldType.TIME.getLengthInBytes();
    }    

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((timeStr == null) ? 0 : timeStr.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Time))
            return false;
        Time other = (Time) obj;
        if (timeStr == null)
        {
            if (other.timeStr != null)
                return false;
        }
        else if (!timeStr.equals(other.timeStr))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return timeStr.toString();
    }    
}

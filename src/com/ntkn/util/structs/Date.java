package com.ntkn.util.structs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import com.ntkn.messages.IndicatorFieldType;
import com.ntkn.util.InvalidFormatException;

public class Date
{
    private static final String DELIMITER       = "-";
    private static final String FORMAT          = "yyyy-mm-dd";
    private static final int    EXPECTED_TOKENS = 3;    
    private static final String ZERO_FILL       = "0";
    
    private String dateStr;
    private short  year;
    private byte   month;
    private byte   day;
    
    public Date(String dateStr)
        throws InvalidFormatException
    {
        super();
        init(dateStr);
        formatInternalString();        
    }
    
    public Date(short year, byte month, byte day)
        throws InvalidFormatException
    {
        super();
        this.year = year;
        this.month = month;
        this.day = day;
        formatInternalString();                
    }
    
    private void formatInternalString()
        throws InvalidFormatException
    {
        StringBuilder strBld = new StringBuilder();
        strBld.append(year);
        strBld.append(DELIMITER);
        appendByte(strBld, month);
        strBld.append(DELIMITER);
        appendByte(strBld, day);
            
        this.dateStr = strBld.toString();      

        if (isValidDate()==false)
        {
            throw new InvalidFormatException("Invalide Date:"+this.dateStr);
        }
    }
    
    public boolean isValidDate() 
    {
        try 
        {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setLenient(false);        // must do this
            gc.set(GregorianCalendar.YEAR, (int)year);
            gc.set(GregorianCalendar.MONTH, (int)(month-1));// month is zero based
            gc.set(GregorianCalendar.DATE, (int)day);

            gc.getTime(); // exception thrown here for an invalide date            
        }
        catch (Exception e) 
        {
            return false;
        }

        return true;
    }
    
    /**
     * Pad in the zero filler.
     * @param strBld
     * @param value
     */
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
    
    private void init(String dateStr) 
        throws InvalidFormatException
    {
        StringTokenizer st = new StringTokenizer(dateStr, DELIMITER);
        if (st.countTokens() != EXPECTED_TOKENS)
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
                        year = Short.parseShort(token);
                        break;
                        
                    case 1:
                        month = Byte.parseByte(token);
                        break;

                    case 2:
                        day = Byte.parseByte(token);
                        break;

                    default:
                        break;
                }
                
                tokenCount++;
            }
        }
        catch (Exception e)
        {
            throw new InvalidFormatException("Unexpected format.", e);
        }        
    }

    public short getYear()
    {
        return year;
    }

    public byte getMonth()
    {
        return month;
    }

    public byte getDay()
    {
        return day;
    }

    public int getStructLength()
    {
        return IndicatorFieldType.DATE.getLengthInBytes();
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateStr == null) ? 0 : dateStr.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Date))
            return false;
        Date other = (Date) obj;
        if (dateStr == null)
        {
            if (other.dateStr != null)
                return false;
        }
        else if (!dateStr.equals(other.dateStr))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return dateStr.toString();
    }    
}

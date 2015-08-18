package com.ntkn.util.structs;

import com.ntkn.messages.IndicatorFieldType;
import com.ntkn.util.InvalidFormatException;

public class ISOCountryCode 
{
	private String iso_country_code;
	
	public ISOCountryCode(String country_code)
	        throws InvalidFormatException
	{
		super();
		
		if ((country_code.length() != 2) || !country_code.matches("[a-zA-Z]{2}"))	
			throw new InvalidFormatException("country_code length or characters incorrect");
		
		iso_country_code= country_code;
	}
	
    public int getStructLength()
    {
        return IndicatorFieldType.ISO_COUNTRY_CODE.getLengthInBytes();
    }

    public String getISOCountryCode()
    {
    	return iso_country_code;
    }
	
    @Override
    public String toString()
    {
        return iso_country_code;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((iso_country_code == null) ? 0 : iso_country_code.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ISOCountryCode))
            return false;
        ISOCountryCode other = (ISOCountryCode) obj;
        if (iso_country_code == null)
        {
            if (other.iso_country_code != null)
                return false;
        }
        else if (!iso_country_code.equals(other.iso_country_code))
            return false;
        return true;
    }      
    
}

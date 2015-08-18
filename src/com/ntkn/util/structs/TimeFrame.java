package com.ntkn.util.structs;


import com.ntkn.messages.IndicatorFieldType;
import com.ntkn.util.InvalidFormatException;

public class TimeFrame
{
    private byte   period = 0;
    private byte   yearOffset = 0;
    private short   periodValue = 0;
    
    public TimeFrame(byte period, byte yearOffset, short periodValue) 
    {
		super();
		this.period = period;
		this.yearOffset = yearOffset;
		this.periodValue = periodValue;
	}

	public byte getPeriod() {
		return period;
	}

	public byte getYearOffset() {
		return yearOffset;
	}

	public short getPeriodValue() {
		return periodValue;
	}

	public int getStructLength()
    {
        return IndicatorFieldType.TIMEFRAME.getLengthInBytes();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + period;
		result = prime * result + periodValue;
		result = prime * result + yearOffset;
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
		TimeFrame other = (TimeFrame) obj;
		if (period != other.period)
			return false;
		if (periodValue != other.periodValue)
			return false;
		if (yearOffset != other.yearOffset)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TimeFrame [period=" + period + ", periodValue=" + periodValue
				+ ", yearOffset=" + yearOffset + "]";
	}    

}
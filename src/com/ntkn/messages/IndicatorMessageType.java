package com.ntkn.messages;

public enum IndicatorMessageType 
{
    RELEASE((byte)0), ESTIMATE((byte)1), SYSTEM((byte)2); 
    
    private byte messageTypeValue;

    private IndicatorMessageType(byte messageTypeValue)
    {
        this.messageTypeValue = messageTypeValue;
    }

    public byte getMessageTypeValue()
    {
        return messageTypeValue;
    }
    
    
}

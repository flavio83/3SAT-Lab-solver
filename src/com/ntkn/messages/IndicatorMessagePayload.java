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


import java.util.*;

/**
 * An indicator payload consists of a list of indicator message fields. 
 * 
 */
public class IndicatorMessagePayload 
{
    private static int FLOAT_SIZE_IN_BYTES = 4;
    private static int FIELD_HEADER_SIZE_IN_BYTES = 2;    
    
    // all message fields
    //private List<IndicatorMessageField> indicatorList = new ArrayList<IndicatorMessageField>();
    private Set<IndicatorMessageField> indicatorList = new TreeSet<IndicatorMessageField>();
    
    // lookup support for field
    private Map<Integer, IndicatorMessageField> indicatorMap = new TreeMap<Integer, IndicatorMessageField>();    

    /**
     * Default constructor.
     */
    public IndicatorMessagePayload()
    {
    }
    
    /**
     * Construct an object from a byte array.
     * 
     * @param payloadBytes
     * @throws com.ntkn.messages.IndicatorMessageInitializationException
     */
    public IndicatorMessagePayload(byte[] payloadBytes)
        throws IndicatorMessageInitializationException
    {
        boolean bytesLeft = true;
        int fieldOffset = 0;
        while(bytesLeft)
        {
            // figure out the type of field so length can be calculated
            byte fieldType = payloadBytes[fieldOffset];
            IndicatorFieldType ifieldType = IndicatorFieldType.getIndicatorFieldType(fieldType);
            
            // add the field
            IndicatorMessageField part = new IndicatorMessageField(payloadBytes, fieldOffset);
            addIndicator(part);
            
            // change the offset for next field            
            fieldOffset += ifieldType.getLengthInBytes()+FIELD_HEADER_SIZE_IN_BYTES;            
            if (fieldOffset >= payloadBytes.length)
                bytesLeft = false;
        }        
        
    }    
    
    /**
     * Construct an object from a byte array. Use offsets as needed.
     * 
     * @param msgBytes
     * @throws com.ntkn.messages.IndicatorMessageInitializationException
     */    
    public IndicatorMessagePayload(byte[] msgBytes, int offset, int payloadSize)
        throws IndicatorMessageInitializationException
    {        
        boolean bytesLeft = true;
        while(bytesLeft)
        {
            // figure out the type of field so length can be calculated
            byte fieldType = msgBytes[offset];
            IndicatorFieldType ifieldType = IndicatorFieldType.getIndicatorFieldType(fieldType);
            
            // add the field
            IndicatorMessageField part = new IndicatorMessageField(msgBytes, offset);
            addIndicator(part);
            
            // change the offset for next field
            offset += ifieldType.getLengthInBytes()+FIELD_HEADER_SIZE_IN_BYTES;            
            if (offset >= payloadSize)
                bytesLeft = false;
        }        
        
    }    
        
    /**
     * Add a new indicator value.
     * 
     * @param indicator
     */
    public void addIndicator(IndicatorMessageField indicator)
    {
        indicatorList.add(indicator);
        indicatorMap.put((int)indicator.getFieldId(), indicator);
    }
    
    public IndicatorMessageField getIndicator(byte fieldId)
    {
        return indicatorMap.get((int)fieldId);
    }
    
    public List<IndicatorMessageField> getIndicatorMessageFields()
    {
        return Collections.unmodifiableList(new ArrayList<IndicatorMessageField>(indicatorList));        
    }
    
    public Set<IndicatorMessageField> getIndicatorMessageFieldSet()
    {
        return Collections.unmodifiableSet(indicatorList);        
    }    
    
    public int getPayloadSize()
    {
        int length = 0;
        
        // sum up the length from all the fields
        for (IndicatorMessageField part : this.indicatorList)
        {
            IndicatorFieldType ifieldType = IndicatorFieldType.getIndicatorFieldType(part.getFieldType());
            length += ifieldType.getLengthInBytes()+FIELD_HEADER_SIZE_IN_BYTES;
        }
        
        return length;
    }
           
    /**
     * Construct byte[] from the IndicatorPart values in the list.
     * 
     * @return byte[] array representation for this payload object.
     */
    public byte[] getBytes() //throws java.io.IOException
    {
      byte[] payloadBytes = new byte[getPayloadSize()];
      
      int offset = 0;
      for (Iterator<IndicatorMessageField> it = indicatorList.iterator(); it.hasNext();)
      {
          IndicatorMessageField part = it.next();          
          IndicatorFieldType ifieldType = IndicatorFieldType.getIndicatorFieldType(part.getFieldType());
          int length = ifieldType.getLengthInBytes()+FIELD_HEADER_SIZE_IN_BYTES;
          
          System.arraycopy(part.getBytes(), 0, payloadBytes, offset, length); 
          offset+=length;
      }
      
      return payloadBytes;        
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
        final IndicatorMessagePayload other = (IndicatorMessagePayload) obj;
        if (this.indicatorList != other.indicatorList && (this.indicatorList == null || !this.indicatorList.equals(other.indicatorList)))
        {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + (this.indicatorList != null ? this.indicatorList.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString()
    {
        StringBuffer retStringBuff = new StringBuffer();
        
        for (Iterator<IndicatorMessageField> it = indicatorList.iterator(); it.hasNext();)
        {
            IndicatorMessageField part = it.next();
            retStringBuff.append("<");
            retStringBuff.append(part.toString());
            retStringBuff.append(">");
        }
        
        return retStringBuff.toString();
    }
    
    public String toMultilineString()
    {
        StringBuffer retStringBuff = new StringBuffer("IndicatorMessagePayload:\r\n");
        
        for (Iterator<IndicatorMessageField> it = indicatorList.iterator(); it.hasNext();)
        {
            IndicatorMessageField part = it.next();
            retStringBuff.append(part.toString());
            retStringBuff.append("\r\n");
        }
        
        return retStringBuff.toString();
    }    
    
    public String toFormattedString()
    {
        StringBuffer retStringBuff = new StringBuffer("Payload:\r\n");
        
        for (Iterator<IndicatorMessageField> it = indicatorList.iterator(); it.hasNext();)
        {
            IndicatorMessageField part = it.next();
            retStringBuff.append(part.toFormattedString());
            retStringBuff.append("\r\n");
        }
        
        return retStringBuff.toString();
    }    

}

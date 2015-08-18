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

import java.util.Arrays;

public class HexDump
{
    /**
     * HexDump the content of a String, for easy debugging.
     *
     * @param bytes   byte [] : byte array
     *
     * @return none
     */
    public static void dumpToConsole(byte[] bytes)
    {
        String hex = "";
        String ascii = "";
        int i,j;

        for ( i=0; i < bytes.length; i++)
        {
            if ( (i > 0) && (i % 16 == 0))
            {
                    System.out.println( hex + " " + ascii);

                    hex = "";
                    ascii = "";
            }

            if ( (bytes[i] & 0xFF) < 16)
            {
                hex += "0" + Integer.toHexString( 0xFF & bytes[i]);
            } else {
                hex +=  Integer.toHexString( 0xFF & bytes[i]);
            }
            hex += " ";

            if (     ((bytes[i] & 0xFF) > 31) &&
            ((bytes[i] & 0xFF) < 128))
            {
                ascii += new Character((char)(0xFF & bytes[i])).toString();
            } else {
                ascii += ".";
            }
        }

        for( j=(i%16); j < 16; j++)
        {
            hex += "   ";
            ascii += ".";
        }
        
        System.out.println( hex + " " + ascii + "\n");        
    }
    
    public static String dumpToString(byte[] bytes, int length)
    {
    	return HexDump.dumpToString(Arrays.copyOf(bytes, length));
    }
    
    public static String dumpToString(byte[] bytes)
    {
        StringBuffer returnStringBuf = new StringBuffer();
        
        String hex = "";
        String ascii = "";
        int i,j;

        for ( i=0; i < bytes.length; i++)
        {
            if ( (i > 0) && (i % 16 == 0))
            {
                    returnStringBuf.append(hex);

                    hex = "";
                    ascii = "";
            }

            if ( (bytes[i] & 0xFF) < 16)
            {
                hex += "0" + Integer.toHexString( 0xFF & bytes[i]);
            } else {
                hex +=  Integer.toHexString( 0xFF & bytes[i]);
            }
            hex += " ";

            if (     ((bytes[i] & 0xFF) > 31) &&
            ((bytes[i] & 0xFF) < 128))
            {
                ascii += new Character((char)(0xFF & bytes[i])).toString();
            } else {
                ascii += ".";
            }
        }

        for( j=(i%16); j < 16; j++)
        {
            hex += "   ";
            ascii += ".";
        }
        
        returnStringBuf.append(hex).append("\n");
        
        return returnStringBuf.toString();
    }    
}

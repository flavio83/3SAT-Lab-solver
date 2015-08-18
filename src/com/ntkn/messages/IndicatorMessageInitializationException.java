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

/**
 * General exception thrown from decoding routines.
 */
public class IndicatorMessageInitializationException 
    extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 9215758286072673880L;

    public IndicatorMessageInitializationException(Throwable cause)
    {
        super(cause);
    }

    public IndicatorMessageInitializationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public IndicatorMessageInitializationException(String message)
    {
        super(message);
    }

    public IndicatorMessageInitializationException()
    {
    }

}

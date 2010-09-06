/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.ndpireader;

/**
 * General exception for any unexpected errors returned by the NDPRead JNA classes
 * 
 * @version $Rev$
 */
public class NDPReadException extends Exception
{

    private static final long serialVersionUID = 1L;
    
    private final String details;
    private final String messageFromNdpRead;

    public NDPReadException(String details, String messageFromNdpRead)
    {
        super(details + " message from NDPRead interface: [" + messageFromNdpRead + "]");
        this.details = details;
        this.messageFromNdpRead = messageFromNdpRead;
    }

    public String getDetails()
    {
        return details;
    }

    public String getMessageFromNdpRead()
    {
        return messageFromNdpRead;
    }

}

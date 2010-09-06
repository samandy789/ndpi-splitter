/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.splitter;

/**
 * @version $Rev$
 * 
 */
public class ImageReadingException extends Exception
{
    private static final long serialVersionUID = 1L;

    public ImageReadingException(Throwable cause)
    {
        super(cause);
    }

    public ImageReadingException(String message)
    {
        super(message);
    }

}

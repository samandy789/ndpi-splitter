/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.imaging;

/**
 * @version $Rev$
 * 
 */
public class UnsupportedImageFormatException extends RuntimeException
{

    private static final long serialVersionUID = 1L;

    public UnsupportedImageFormatException(String fileType, String[] writerFormats)
    {
        super("Image file type [" + fileType
                + "] is not supported. Perhaps you are missing the required image plugins? Supported types are: "
                + writerFormats);
    }

}

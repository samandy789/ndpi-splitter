/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.file;


/**
 * @version $Rev$
 * 
 */
public class FileProcessorException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public FileProcessorException(String message)
    {
        super(message);
    }

    public FileProcessorException(String message, Throwable cause)
    {
        super(message, cause);
    }

}

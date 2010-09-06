/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.splitter;

import java.io.FileNotFoundException;

import au.org.intersect.ndpisplitter.ndpireader.ImageInformation;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadException;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadWrapper;

/**
 * @version $Rev$
 * 
 */
public class NdpiFileInfoGetter
{
    private static final double CLOSE_TO_ZERO = 0.01;

    private NDPReadWrapper wrapper;

    public NdpiFileInfoGetter(NDPReadWrapper wrapper)
    {
        super();
        this.wrapper = wrapper;
    }

    public float getMagnification(String ndpiFile) throws ImageReadingException
    {
        try
        {
            float sourceLensMagnification = wrapper.getSourceLensMagnification(ndpiFile);
            if (sourceLensMagnification < CLOSE_TO_ZERO)
            {
                // zero indicates an error, so throw an exception
                throw new ImageReadingException(
                        "Get source lens magnification returned zero, this usually indicates an error");
            }
            return sourceLensMagnification;
        }
        finally
        {
            // call cleanup so the file isn't locked
            wrapper.cleanUp();
        }
    }

    public ImageInformation getImageInformation(String ndpiFile) throws ImageReadingException
    {
        try
        {
            return wrapper.getImageInformation(ndpiFile);
        }
        catch (FileNotFoundException e)
        {
            throw new ImageReadingException(e);
        }
        catch (NDPReadException e)
        {
            throw new ImageReadingException(e);
        }
        finally
        {
            // call cleanup so the file isn't locked
            wrapper.cleanUp();
        }
    }

}

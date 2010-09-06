/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.processing;

/**
 * Used by PixelInspector to do something with each pixel from the image. Implementors should implement whatever rules
 * or logic are required for each pixel
 * 
 * @version $Rev$
 */
public interface PixelHandler
{

    void handlePixel(byte band1, byte band2, byte band3);

}

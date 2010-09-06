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
 * 
 * @version $Rev$
 */
public class CountingPixelHandler implements PixelHandler
{
    private int totalPoints;

    @Override
    public void handlePixel(byte band1, byte band2, byte band3)
    {
        if ((band1 != -1) && (band2 != -1) && (band3 != -1))
        {
            totalPoints++;
        }
    }

    public int getTotalPoints()
    {
        return totalPoints;
    }
    
    
}

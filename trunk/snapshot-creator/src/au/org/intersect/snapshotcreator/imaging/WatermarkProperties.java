/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.imaging;

/**
 * 
 * @version $Rev$
 */
public class WatermarkProperties
{
    private final float transparency;
    private final float relativeXPosition;
    private final float relativeYPosition;
    private final float relativeWidth;

    public WatermarkProperties(float transparency, float relativeXPosition, float relativeYPosition, 
            float relativeWidth)
    {
        super();
        this.transparency = transparency;
        this.relativeXPosition = relativeXPosition;
        this.relativeYPosition = relativeYPosition;
        this.relativeWidth = relativeWidth;
    }

    public float getTransparency()
    {
        return transparency;
    }

    public float getRelativeXPosition()
    {
        return relativeXPosition;
    }

    public float getRelativeYPosition()
    {
        return relativeYPosition;
    }

    public float getRelativeWidth()
    {
        return relativeWidth;
    }

}

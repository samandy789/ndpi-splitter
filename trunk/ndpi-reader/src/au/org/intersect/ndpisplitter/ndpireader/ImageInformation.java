/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.ndpireader;

import au.org.intersect.ndpisplitter.util.Formats;

/**
 * Wrapper class that holds various pieces of info about an NDPI file.
 * 
 * @version $Rev$
 */
public class ImageInformation
{

    private long imageWidthInNanometres;
    private long imageHeightInNanometres;
    private long imageWidthInPixels;
    private long imageHeightInPixels;
    private long physicalXPositionOfCentreInNanometres;
    private long physicalYPositionOfCentreInNanometres;
    private float sourceLensMagnification;

    public long getImageWidthInNanometres()
    {
        return imageWidthInNanometres;
    }

    public void setImageWidthInNanometres(long imageWidthInNanometres)
    {
        this.imageWidthInNanometres = imageWidthInNanometres;
    }

    public long getImageHeightInNanometres()
    {
        return imageHeightInNanometres;
    }

    public void setImageHeightInNanometres(long imageHeightInNanometres)
    {
        this.imageHeightInNanometres = imageHeightInNanometres;
    }

    public long getImageWidthInPixels()
    {
        return imageWidthInPixels;
    }

    public void setImageWidthInPixels(long imageWidthInPixels)
    {
        this.imageWidthInPixels = imageWidthInPixels;
    }

    public long getImageHeightInPixels()
    {
        return imageHeightInPixels;
    }

    public void setImageHeightInPixels(long imageHeightInPixels)
    {
        this.imageHeightInPixels = imageHeightInPixels;
    }

    public long getPhysicalXPositionOfCentreInNanometres()
    {
        return physicalXPositionOfCentreInNanometres;
    }

    public void setPhysicalXPositionOfCentreInNanometres(long physicalXPositionOfCentreInNanometres)
    {
        this.physicalXPositionOfCentreInNanometres = physicalXPositionOfCentreInNanometres;
    }

    public long getPhysicalYPositionOfCentreInNanometres()
    {
        return physicalYPositionOfCentreInNanometres;
    }

    public void setPhysicalYPositionOfCentreInNanometres(long physicalYPositionOfCentreInNanometres)
    {
        this.physicalYPositionOfCentreInNanometres = physicalYPositionOfCentreInNanometres;
    }

    public double locateBottomEdge()
    {
        return ((double) physicalYPositionOfCentreInNanometres) + getHalfOfHeightInNanometres();
    }

    public double locateTopEdge()
    {
        return ((double) physicalYPositionOfCentreInNanometres) - getHalfOfHeightInNanometres();
    }

    public double locateRightEdge()
    {
        return ((double) physicalXPositionOfCentreInNanometres) + getHalfOfWidthInNanometres();
    }

    public double locateLeftEdge()
    {
        return ((double) physicalXPositionOfCentreInNanometres) - getHalfOfWidthInNanometres();
    }

    private double getHalfOfWidthInNanometres()
    {
        return imageWidthInNanometres / 2D;
    }

    private double getHalfOfHeightInNanometres()
    {
        return imageHeightInNanometres / 2D;
    }

    public float getSourceLensMagnification()
    {
        return sourceLensMagnification;
    }

    public void setSourceLensMagnification(float sourceLensMagnification)
    {
        this.sourceLensMagnification = sourceLensMagnification;
    }

    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("image size in nm = ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(imageWidthInNanometres));
        buffer.append("w x ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(imageHeightInNanometres));
        buffer.append("h");
        buffer.append(Formats.NEWLINE);

        buffer.append("image size in pixels = ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(imageWidthInPixels));
        buffer.append("w x ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(imageHeightInPixels));
        buffer.append("h");
        buffer.append(Formats.NEWLINE);

        buffer.append("x position of centre in nm = ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(physicalXPositionOfCentreInNanometres));
        buffer.append(Formats.NEWLINE);

        buffer.append("y position of centre in nm = ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(physicalYPositionOfCentreInNanometres));
        buffer.append(Formats.NEWLINE);

        buffer.append("source lens magnification = ");
        buffer.append(sourceLensMagnification);
        return buffer.toString();
    }
}

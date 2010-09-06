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
public class IntensityPixelHandler implements PixelHandler
{

    private int numberOfIntensePixels;
    private final int thresholdIntensity;
    private final int whitenessIntensity;

    public IntensityPixelHandler(int thresholdIntensity, int whitenessIntensity)
    {
        this.thresholdIntensity = thresholdIntensity;
        this.whitenessIntensity = whitenessIntensity;
    }

    @Override
    public void handlePixel(byte band1, byte band2, byte band3)
    {
        if (isPixelInteresting(band1, band2, band3))
        {
            numberOfIntensePixels++;
        }
    }

    public boolean isPixelInteresting(int band1, int band2, int band3)
    {
        if ((band1 == -1) && (band2 == -1) && (band3 == -1))
        {
            return false;
        }

        // Odd characteristic of the pixels. Values higher than
        // 128 are represented as negative values. I suspect that
        // the value returned by the library is an unsigned number
        // We need to convert it to signed
        int byteMaxValue = Byte.MAX_VALUE + 1;
        int signedBand1 = band1 < 0 ? byteMaxValue - band1 : band1;
        int signedBand2 = band2 < 0 ? byteMaxValue - band2 : band2;
        int signedBand3 = band3 < 0 ? byteMaxValue - band3 : band3;

        int intensity = Math.max(signedBand1, Math.max(signedBand2, signedBand3));
        int whiteness = Math.min(signedBand1, Math.min(signedBand2, signedBand3));

        return (intensity > thresholdIntensity) && (whiteness < whitenessIntensity);
    }

    public int getNumberOfIntensePixels()
    {
        return numberOfIntensePixels;
    }

}

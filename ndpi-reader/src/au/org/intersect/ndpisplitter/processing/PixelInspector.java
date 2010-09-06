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
public class PixelInspector
{
    private static final int NUMBER_OF_BANDS = 3;
    private final int pixelWidth;
    private final int pixelHeight;
    private final int scanlineStride;

    public PixelInspector(int pixelWidth, int pixelHeight, int scanlineStride)
    {
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
        this.scanlineStride = scanlineStride;
    }

    public void inspectPixels(byte[] imageBytes, PixelHandler handler)
    {
        int realBytesPerLine = pixelWidth * NUMBER_OF_BANDS;

        // because the data we get back is padded to 4-byte boundaries, we have to carefully avoid these padding bytes
        int i = 0;
        while (i < pixelHeight)
        {
            for (int j = 0; j < scanlineStride; j += NUMBER_OF_BANDS)
            {
                if (j < realBytesPerLine)
                {
                    byte band1 = imageBytes[(i * scanlineStride) + j];
                    byte band2 = imageBytes[(i * scanlineStride) + j + 1];
                    byte band3 = imageBytes[(i * scanlineStride) + j + 2];
                    handler.handlePixel(band1, band2, band3);
                }
            }
            i++;
        }
    }

}

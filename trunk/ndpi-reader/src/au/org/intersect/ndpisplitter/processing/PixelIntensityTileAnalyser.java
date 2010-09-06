/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.processing;

import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

/**
 * The PixelIntensityTileAnalyser checks the intensity of each pixel in the image It does so by checking the maximum
 * value of the three bands. For a pixel to be consider interesting, it needs to meet two criteria: - If any of the
 * three bands is greater than the THRESHOLD_INTENSITY - The minimum of the three bands is less than the
 * WHITENESS_INTENSITY
 * 
 * If the three bands have a high value, the pixel is white-ish, and not interesting for the research
 * 
 * @author Diego Alonso
 * 
 */
public class PixelIntensityTileAnalyser implements TileAnalyser
{
    /**
     * The number of bands in the image
     */
    private static final int NUMBER_OF_BANDS = 3;

    private final int thresholdIntensity;
    private final int whitenessIntensity;
    private final double interestingThreshold;

    public PixelIntensityTileAnalyser(int thresholdIntensity, int whitenessIntensity, double interestingThreshold)
    {
        super();
        this.thresholdIntensity = thresholdIntensity;
        this.whitenessIntensity = whitenessIntensity;
        this.interestingThreshold = interestingThreshold;
    }

    @Override
    public boolean isTileInteresting(Raster raster, String outputFileName, String outputDirectory, int pixelWidth,
            int pixelHeight, int scanlineStride)
    {
        DataBufferByte dataByteBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] imageBytes = dataByteBuffer.getData();

        int numberOfPixels = numberOfPixels(imageBytes, pixelWidth, pixelHeight, scanlineStride);
        int numberOfInterestingPixels = numberOfIntensePixels(imageBytes, pixelWidth, pixelHeight, scanlineStride);
        double ratio = (double) numberOfInterestingPixels / numberOfPixels;

        boolean isInteresting = ratio > interestingThreshold;
        InterestingnessLogger.logTile(outputFileName, outputDirectory, isInteresting, ratio, interestingThreshold);
        return isInteresting;
    }

    private int numberOfIntensePixels(byte[] imageBytes, int pixelWidth, int pixelHeight, int scanlineStride)
    {
        PixelInspector inspector = new PixelInspector(pixelWidth, pixelHeight, scanlineStride);
        IntensityPixelHandler handler = new IntensityPixelHandler(thresholdIntensity,
                whitenessIntensity);
        inspector.inspectPixels(imageBytes, handler);
        return handler.getNumberOfIntensePixels();
    }

    private int numberOfPixels(byte[] imageBytes, int pixelWidth, int pixelHeight, int scanlineStride)
    {
        PixelInspector inspector = new PixelInspector(pixelWidth, pixelHeight, scanlineStride);
        CountingPixelHandler handler = new CountingPixelHandler();
        inspector.inspectPixels(imageBytes, handler);
        return handler.getTotalPoints();

    }

}

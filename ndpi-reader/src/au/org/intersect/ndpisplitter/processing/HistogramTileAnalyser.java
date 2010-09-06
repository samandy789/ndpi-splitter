/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.processing;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

/**
 * Creates an histogram of the tile. At the moment there is no algorithm to detect interesting tiles from the histogram
 */
public class HistogramTileAnalyser implements TileAnalyser
{
    @Override
    public boolean isTileInteresting(Raster raster, String outputFileName, String outputDirectory, int pixelWidth,
            int pixelHeight, int scanlineStride)
    {
        BufferedImage image = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_INT_RGB);

        image.setData(raster.createTranslatedChild(0, 0));

        Histogram hist = getHistogram(image);

        /*
         * for (int i = 0; i < hist.getNumBins()[1]; i++) { // Do some analisys with hist.getBinSize(0, i);
         * hist.getBinSize(1, i); hist.getBinSize(2, i); }
         */

        return false;
    }

    public Histogram getHistogram(BufferedImage image)
    {
        // Create the parameter block.
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image); // Specify the source image
        pb.add(null); // No ROI
        pb.add(1); // Sampling
        pb.add(1); // periods

        // Perform the histogram operation.
        PlanarImage dst = (PlanarImage) JAI.create("histogram", pb, null);

        // Return the histogram data.
        return (Histogram) dst.getProperty("histogram");
    }
}

/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.processing;

import java.awt.image.Raster;

/**
 * Analyser which always returns true. This is plugged in for the case where we don't want to separate out empty tiles
 * 
 * @version $Rev$
 */
public class NullTileAnalyser implements TileAnalyser
{

    @Override
    public boolean isTileInteresting(Raster raster, String outputFileName, String outputDirectory, int pixelWidth,
            int pixelHeight, int scanlineStride)
    {
        // all tiles are interesting
        return true;
    }
}

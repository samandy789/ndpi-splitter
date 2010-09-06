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
 * Generic Tile Analizer
 * 
 */
public interface TileAnalyser
{
    boolean isTileInteresting(Raster raster, String outputFileName, String outputDirectory, int pixelWidth,
            int pixelHeight, int scanlineStride);
}

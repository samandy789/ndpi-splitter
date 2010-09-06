/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.imaging;

import java.awt.image.Raster;
import java.io.File;

import au.org.intersect.ndpisplitter.file.FileManager;
import au.org.intersect.ndpisplitter.processing.TileAnalyser;

/**
 * 
 * @version $Rev$
 */
public class FilterEmptyImagesImageCreator extends BaseImageCreator
{

    private final TileAnalyser tileAnalyser;
    private final String emptyDirectoryName;
    private final FileManager fileManager;

    public FilterEmptyImagesImageCreator(String fileType, TileAnalyser tileAnalyser, String emptyDirectoryName,
            FileManager fileManager)
    {
        super(fileType);
        this.tileAnalyser = tileAnalyser;
        this.emptyDirectoryName = emptyDirectoryName;
        this.fileManager = fileManager;
    }

    @Override
    protected String decideWhereToStoreImage(Raster raster, String outputFileName, String outputDirectory,
            int pixelWidth, int pixelHeight, int scanlineStride)
    {
        boolean isInteresting = tileAnalyser.isTileInteresting(raster, outputFileName, outputDirectory, pixelWidth,
                pixelHeight, scanlineStride);
        if (isInteresting)
        {
            return super.decideWhereToStoreImage(raster, outputFileName, outputDirectory, pixelWidth, pixelHeight,
                    scanlineStride);
        }
        else
        {
            String uninterestingDirectory = outputDirectory + File.separator + emptyDirectoryName;
            fileManager.createDirectoryIfNeeded(new File(uninterestingDirectory));
            return uninterestingDirectory + File.separator + outputFileName;
        }
    }

}

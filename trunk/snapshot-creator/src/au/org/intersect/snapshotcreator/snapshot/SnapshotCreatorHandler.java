/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.snapshot;

import java.io.File;

import org.apache.log4j.Logger;

import au.org.intersect.ndpisplitter.ndpireader.ImageInformation;
import au.org.intersect.ndpisplitter.splitter.ImageReadingException;
import au.org.intersect.ndpisplitter.splitter.ImageTilingException;
import au.org.intersect.ndpisplitter.splitter.NdpiFileInfoGetter;
import au.org.intersect.ndpisplitter.splitter.NdpiFileSplitter;
import au.org.intersect.ndpisplitter.splitter.StatusUpdater;
import au.org.intersect.ndpisplitter.splitter.TilePositions;
import au.org.intersect.snapshotcreator.config.SnapshotCreatorProperties;
import au.org.intersect.snapshotcreator.file.FileHandler;
import au.org.intersect.snapshotcreator.file.FileProcessorException;

/**
 * 
 * @version $Rev$
 */
public class SnapshotCreatorHandler implements FileHandler
{
    private static final Logger LOG = Logger.getLogger(SnapshotCreatorHandler.class);

    private final NdpiFileSplitter splitter;
    private final NdpiFileInfoGetter fileInfoGetter;
    private final SnapshotCreatorProperties properties;

    private StatusUpdater statusUpdater;

    public SnapshotCreatorHandler(SnapshotCreatorProperties properties, NdpiFileSplitter splitter,
            NdpiFileInfoGetter fileInfoGetter, StatusUpdater statusUpdater)
    {
        this.properties = properties;
        this.splitter = splitter;
        this.fileInfoGetter = fileInfoGetter;
        this.statusUpdater = statusUpdater;
    }

    @Override
    public void handleFile(File file)
    {
        try
        {
            String sourceFilePath = file.getAbsolutePath();
            LOG.info("Processing " + sourceFilePath);

            ImageInformation imageInfo = fileInfoGetter.getImageInformation(sourceFilePath);
            TilePositions tilePositions = new CentrePositionCalculator(imageInfo, properties.getOutputMagnification(),
                    properties.getMaxPixelsPerSnapshot());
            File outputDirectory = new File(properties.getSnapshotsWorkDirectory());

            splitter.tileImage(sourceFilePath, tilePositions, imageInfo, properties.getOutputMagnification(),
                    outputDirectory, statusUpdater);
        }
        catch (ImageReadingException e)
        {
            LOG.info("Error reading image ", e);
            throw new FileProcessorException("Error reading image", e);
        }
        catch (ImageTilingException e)
        {
            LOG.info("Error tiling image", e);
            throw new FileProcessorException("Error tiling image", e);
        }
    }
}

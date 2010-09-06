/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.splitter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import au.org.intersect.ndpisplitter.file.FileManager;
import au.org.intersect.ndpisplitter.imaging.ImageCreator;
import au.org.intersect.ndpisplitter.ndpireader.ImageInformation;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadException;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadWrapper;
import au.org.intersect.ndpisplitter.processing.InterestingnessLogger;

/**
 * 
 * @version $Rev$
 */
public class NdpiFileSplitter
{

    private static final Logger LOG = Logger.getLogger(NdpiFileSplitter.class);

    private NDPReadWrapper wrapper;
    private ImageCreator imageCreator;
    private TileFileNamer tileFileNamer;
    private FileManager directoryCreator;

    public NdpiFileSplitter(NDPReadWrapper wrapper, ImageCreator imageCreator,
            TileFileNamer tileFileNamer, FileManager directoryCreator)
    {
        super();
        this.wrapper = wrapper;
        this.imageCreator = imageCreator;
        this.tileFileNamer = tileFileNamer;
        this.directoryCreator = directoryCreator;
    }

    public void tileImage(String ndpiFile, TilePositions tilePositions, ImageInformation imageInfo,
            int requiredMagnification, File outputDirectory, StatusUpdater statusUpdater) throws ImageTilingException
    {
        try
        {
            directoryCreator.createDirectoryIfNeeded(outputDirectory);
            InterestingnessLogger.createNewLog(outputDirectory);
            doImageTiling(ndpiFile, tilePositions, imageInfo, requiredMagnification, outputDirectory, statusUpdater);
        }
        catch (Exception e)
        {
            // catch all exceptions and wrap and rethrow so that callers only have to handle one type of exception
            throw new ImageTilingException(e);
        }
        finally
        {
            // call cleanup on the ndpi sdk in the hope that it will free up some memory or do something useful at least
            wrapper.cleanUp();
        }
    }

    private void doImageTiling(String ndpiFile, TilePositions positions, ImageInformation imageInfo,
            int requiredMagnification, File outputDirectory, StatusUpdater statusUpdater) throws NDPReadException,
        IOException
    {
        List<Long> tileXPositions = positions.getTileXPositions();
        List<Long> tileYPositions = positions.getTileYPositions();

        int totalNumberOfTiles = positions.getTotalNumberOfTiles();

        logImageAndTileDetails(ndpiFile, requiredMagnification, imageInfo, positions);

        statusUpdater.setNumberOfTiles(totalNumberOfTiles);
        statusUpdater.setNumberOfTilesCompleted(0);

        int yCounter = 1;
        int tileCounter = 0;
        for (Long yPosition : tileYPositions)
        {
            int xCounter = 1;
            for (Long xPosition : tileXPositions)
            {
                String outputFileName = tileFileNamer.getOutputFileName(ndpiFile, xCounter, yCounter);
                createTileImageFile(ndpiFile, (int) xPosition.longValue(), (int) yPosition.longValue(), positions
                        .getTileWidthInPixels(), positions.getTileHeightInPixels(), (float) requiredMagnification,
                        outputFileName, outputDirectory);
                tileCounter++;
                statusUpdater.setNumberOfTilesCompleted(tileCounter);
                xCounter++;
            }
            yCounter++;
        }
    }

    public void logImageAndTileDetails(String ndpiFile, int magnification, ImageInformation imageInfo,
            TilePositions positions)
    {
        LOG.info(CalculationLogger.getCalculationDetails(ndpiFile, magnification, imageInfo, positions));
    }

    private void createTileImageFile(String ndpiFile, long xPos, long yPos, int pixelWidth, int pixelHeight,
            float magnification, String outputFileName, File outputDirectory) throws NDPReadException, IOException
    {
        byte[] bs = wrapper
                .getImageSegment(ndpiFile, (int) xPos, (int) yPos, 0, magnification, pixelWidth, pixelHeight);

        imageCreator.createImageFromNdpiBytes(bs, pixelWidth, pixelHeight, outputFileName, outputDirectory
                .getAbsolutePath());
    }
}

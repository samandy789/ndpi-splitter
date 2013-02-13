/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: CentrePositionCalculator.java 7 2010-09-21 07:07:51Z intersect.engineering.team@gmail.com $
 */
package au.org.intersect.snapshotcreator.snapshot;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import au.org.intersect.ndpisplitter.ndpireader.ImageInformation;
import au.org.intersect.ndpisplitter.splitter.TilePositions;
import au.org.intersect.ndpisplitter.util.Formats;

/**
 * 
 * @version $Rev: 7 $
 */
public class TilePositionCalculator implements TilePositions 
{
    private static final int FOUR = 4;
    private static final Logger LOG = Logger
            .getLogger(TilePositionCalculator.class);

    private long tileXPosition;
    private long tileYPosition;
    private long centreXPosition;
    private long centreYPosition;
    private int tileHeightInPixels;
    private int tileWidthInPixels;
    private long imageHeightInNanometres;
    private long imageWidthInNanometres;

    public TilePositionCalculator(ImageInformation map,
            int requestedMagnification, int maxPixelsPerSnapshot,
            String userSpecifiedTilePosition) 
    {
        double requestedMag = (double) requestedMagnification;
        double sourceMag = (double) map.getSourceLensMagnification();
        double ratio = sourceMag / requestedMag;

        double imageWidthInPixelsAsDouble = (double) map
                .getImageWidthInPixels();
        double imageHeightInPixelsAsDouble = (double) map
                .getImageHeightInPixels();

        this.imageWidthInNanometres = map.getImageWidthInNanometres();
        this.imageHeightInNanometres = map.getImageHeightInNanometres();

        this.centreXPosition = map.getPhysicalXPositionOfCentreInNanometres();
        this.centreYPosition = map.getPhysicalYPositionOfCentreInNanometres();

        positionTileAccordingToUserSpecification(userSpecifiedTilePosition);

        // work out tile pixel size by using ratio of source to requested
        // magnficiation
        // tile width/height is half of the total, with the ratio applied
        double tileWidth = imageWidthInPixelsAsDouble / ratio / 2;
        double tileHeight = imageHeightInPixelsAsDouble / ratio / 2;

        // limit the max size - if its too big we run out of memory, so cap it
        // at a certain size and if we hit it,
        // downscale the size to fit under the max pixels
        double sizeLimit = maxPixelsPerSnapshot;
        double size = tileWidth * tileHeight;
        if (size > sizeLimit) 
        {
            double sizeRatio = Math.sqrt(sizeLimit / size);
            String logStmt = logSizeReduction(tileWidth, tileHeight, size,
                    sizeRatio);
            tileWidth = tileWidth * sizeRatio;
            tileHeight = tileHeight * sizeRatio;
            logStmt += logNewSize(tileWidth, tileHeight);
            LOG.info(logStmt);
        }

        this.tileWidthInPixels = (int) Math.round(tileWidth);
        this.tileHeightInPixels = (int) Math.round(tileHeight);

    }

    private void positionTileAccordingToUserSpecification(
            String userSpecifiedTilePosition)
    {
        // separate the y and x coordinates
        String[] yxPositions = userSpecifiedTilePosition.trim().split("(?!^)");
        long quarterOfImageHeight = this.imageHeightInNanometres / FOUR;
        long quarterOfImageWidth = this.imageWidthInNanometres / FOUR;

        if ("T".equalsIgnoreCase(yxPositions[0]))
        {
            this.tileYPosition = this.centreYPosition - quarterOfImageHeight;
        } 
        else if ("B".equalsIgnoreCase(yxPositions[0]))
        {
            this.tileYPosition = this.centreYPosition + quarterOfImageHeight;
        }
        else 
        {
            this.tileYPosition = this.centreYPosition;
        }

        if ("L".equalsIgnoreCase(yxPositions[1])) 
        {
            this.tileXPosition = this.centreXPosition - quarterOfImageWidth;
        } 
        else if ("R".equalsIgnoreCase(yxPositions[1])) 
        {
            this.tileXPosition = this.centreXPosition + quarterOfImageWidth;
        } 
        else 
        {
            this.tileXPosition = this.centreXPosition;
        }
    }

    private String logNewSize(double tileWidth, double tileHeight) 
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("New downsized dimensions are ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(tileWidth));
        buffer.append("w x ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(tileHeight));
        buffer.append("h = ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(tileWidth
                * tileHeight));
        buffer.append(Formats.NEWLINE);
        return buffer.toString();
    }

    private String logSizeReduction(double tileWidth, double tileHeight,
            double size, double sizeRatio) 
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(Formats.NEWLINE);
        buffer.append("Image is too large to process, downsizing dimensions");
        buffer.append(Formats.NEWLINE);
        buffer.append("Planned size was ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(tileWidth));
        buffer.append("w x ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(tileHeight));
        buffer.append("h = ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(size));
        buffer.append(Formats.NEWLINE);
        buffer.append("Size reduction ratio ");
        buffer.append(Formats.DECIMAL_FORMATTER.format(sizeRatio));
        buffer.append(Formats.NEWLINE);
        return buffer.toString();
    }

    public List<Long> getTileXPositions() 
    {
        return Collections.singletonList(this.tileXPosition);
    }

    public List<Long> getTileYPositions() 
    {
        return Collections.singletonList(this.tileYPosition);
    }

    public int getTotalNumberOfTiles() 
    {
        return 1;
    }

    @Override
    public int getTileHeightInPixels() 
    {
        return this.tileHeightInPixels;
    }

    @Override
    public int getTileWidthInPixels() 
    {
        return this.tileWidthInPixels;
    }

    @Override
    public String toString() 
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("centre of tile (x,y) = (");
        buffer.append(tileXPosition);
        buffer.append(",");
        buffer.append(tileYPosition);
        buffer.append(")");
        buffer.append(Formats.NEWLINE);

        buffer.append("tile size in pixels (w,h) = (");
        buffer.append(this.tileWidthInPixels);
        buffer.append(",");
        buffer.append(this.tileHeightInPixels);
        buffer.append(")");
        buffer.append(" --> total pixels = " + this.tileWidthInPixels
                * this.tileHeightInPixels);
        buffer.append(Formats.NEWLINE);

        return buffer.toString();
    }

}

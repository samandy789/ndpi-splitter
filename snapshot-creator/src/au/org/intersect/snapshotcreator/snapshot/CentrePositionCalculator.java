/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
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
 * @version $Rev$
 */
public class CentrePositionCalculator implements TilePositions
{
    private static final Logger LOG = Logger.getLogger(CentrePositionCalculator.class);
    private List<Long> tileXPositions;
    private List<Long> tileYPositions;
    private int tileHeightInPixels;
    private int tileWidthInPixels;

    public CentrePositionCalculator(ImageInformation map, int requestedMagnification, int maxPixelsPerSnapshot)
    {
        tileXPositions = Collections.singletonList(map.getPhysicalXPositionOfCentreInNanometres());
        tileYPositions = Collections.singletonList(map.getPhysicalYPositionOfCentreInNanometres());

        double requestedMag = (double) requestedMagnification;
        double sourceMag = (double) map.getSourceLensMagnification();
        double ratio = sourceMag / requestedMag;

        double imageWidthInPixelsAsDouble = (double) map.getImageWidthInPixels();
        double imageHeightInPixelsAsDouble = (double) map.getImageHeightInPixels();

        // work out tile pixel size by using ratio of source to requested magnficiation
        // tile width/height is half of the total, with the ratio applied
        double tileWidth = imageWidthInPixelsAsDouble / ratio / 2;
        double tileHeight = imageHeightInPixelsAsDouble / ratio / 2;

        // limit the max size - if its too big we run out of memory, so cap it at a certain size and if we hit it,
        // downscale the size to fit under the max pixels
        double sizeLimit = maxPixelsPerSnapshot;
        double size = tileWidth * tileHeight;
        if (size > sizeLimit)
        {
            double sizeRatio = Math.sqrt(sizeLimit / size);
            String logStmt = logSizeReduction(tileWidth, tileHeight, size, sizeRatio);
            tileWidth = tileWidth * sizeRatio;
            tileHeight = tileHeight * sizeRatio;
            logStmt += logNewSize(tileWidth, tileHeight);
            LOG.info(logStmt);
        }

        this.tileWidthInPixels = (int) Math.round(tileWidth);
        this.tileHeightInPixels = (int) Math.round(tileHeight);
    }

    private String logNewSize(double tileWidth, double tileHeight)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("New downsized dimensions are ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(tileWidth));
        buffer.append("w x ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(tileHeight));
        buffer.append("h = ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(tileWidth * tileHeight));
        buffer.append(Formats.NEWLINE);
        return buffer.toString();
    }

    private String logSizeReduction(double tileWidth, double tileHeight, double size, double sizeRatio)
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
        return tileXPositions;
    }

    public List<Long> getTileYPositions()
    {
        return tileYPositions;
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
        buffer.append(tileXPositions.get(0));
        buffer.append(",");
        buffer.append(tileYPositions.get(0));
        buffer.append(")");
        buffer.append(Formats.NEWLINE);

        buffer.append("tile size in pixels (w,h) = (");
        buffer.append(this.tileWidthInPixels);
        buffer.append(",");
        buffer.append(this.tileHeightInPixels);
        buffer.append(")");
        buffer.append(" --> total pixels = " + this.tileWidthInPixels * this.tileHeightInPixels);
        buffer.append(Formats.NEWLINE);

        return buffer.toString();
    }

}

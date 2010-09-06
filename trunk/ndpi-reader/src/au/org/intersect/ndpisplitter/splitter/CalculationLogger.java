/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.splitter;

import au.org.intersect.ndpisplitter.ndpireader.ImageInformation;
import au.org.intersect.ndpisplitter.util.Formats;

/**
 * 
 * @version $Rev$
 */
public class CalculationLogger
{
    public static String getCalculationDetails(String ndpiFile, int magnification, ImageInformation imageInfo,
            TilePositions positions)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Tiling ");
        buffer.append(ndpiFile);
        buffer.append(Formats.NEWLINE);
        buffer.append("Tile Pixel Size ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(positions.getTileWidthInPixels()));
        buffer.append("w x ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(positions.getTileHeightInPixels()));
        buffer.append("h");
        buffer.append(Formats.NEWLINE);
        buffer.append("Requested Magnification ");
        buffer.append(Formats.WHOLE_NUMBER_FORMATTER.format(magnification));
        buffer.append("x");
        buffer.append(Formats.NEWLINE);
        buffer.append("----Image Details----");
        buffer.append(Formats.NEWLINE);
        buffer.append(imageInfo.toString());
        buffer.append(Formats.NEWLINE);
        buffer.append("----Tile Calculations----");
        buffer.append(Formats.NEWLINE);
        buffer.append(positions.toString());
        return buffer.toString();
    }
}

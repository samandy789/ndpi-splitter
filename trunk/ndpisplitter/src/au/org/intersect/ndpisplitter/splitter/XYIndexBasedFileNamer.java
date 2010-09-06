/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.splitter;


/**
 * Tile file namer that generates tile names in the format [output-path]/[original-file-name-without-extension]_y_x.tiff
 * 
 * @version $Rev$
 */
public class XYIndexBasedFileNamer extends TileFileNamer
{
    private final SequenceCodeGenerator sequenceCodeGenerator;
    private final String fileExtension;

    public XYIndexBasedFileNamer(SequenceCodeGenerator sequenceCodeGenerator, String fileExtension)
    {
        super();
        this.sequenceCodeGenerator = sequenceCodeGenerator;
        this.fileExtension = fileExtension;
    }

    /**
     * Get the tile file name in the format [output-path]/[original-file-name-without-extension]_y_x.tiff
     * 
     * Where y = column the tile is in, represented by a letter starting from a Where x = row the tile is in,
     * represented by a number starting from 1
     */
    public String getOutputFileName(String originalFileFullPath, int xIndex, int yIndex)
    {
        String originalFileName = getNameWithoutPathAndExtension(originalFileFullPath);
        String rowLetter = sequenceCodeGenerator.getSequenceCode(yIndex);
        // TODO: perhaps should pad numeric indices to 2/3 digits ?
        String columnNumber = Integer.toString(xIndex);

        StringBuffer buffer = new StringBuffer();
        buffer.append(originalFileName);
        buffer.append("_");
        buffer.append(rowLetter);
        buffer.append("_");
        buffer.append(columnNumber);
        buffer.append(".");
        buffer.append(fileExtension);
        return buffer.toString();
    }

}

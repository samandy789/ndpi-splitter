/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.snapshot;

import au.org.intersect.ndpisplitter.splitter.TileFileNamer;

/**
 * @version $Rev$
 * 
 */
public class SnapshotFileNamer extends TileFileNamer
{

    private final String fileExtension;

    public SnapshotFileNamer(String fileExtension)
    {
        this.fileExtension = fileExtension;
    }

    @Override
    public String getOutputFileName(String originalFileFullPath, int xIndex, int yIndex)
    {
        if (yIndex != 1 || xIndex != 1)
        {
            throw new IllegalArgumentException("This only handles single-image tiling operations, "
                    + "you tried to request a filename with x,y index [" + xIndex + "," + yIndex
                    + "]. Only 1,1 is supported.");
        }
        String originalFileName = getNameWithoutPathAndExtension(originalFileFullPath);

        StringBuffer buffer = new StringBuffer();
        buffer.append(originalFileName);
        buffer.append(".");
        buffer.append(fileExtension);
        return buffer.toString();
    }

}

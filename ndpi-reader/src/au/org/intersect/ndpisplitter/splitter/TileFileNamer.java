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

/**
 * 
 * @version $Rev$
 */
public abstract class TileFileNamer
{

    /**
     * Get the name for a tile file based on the original file name and x/y indices.
     * 
     * @param originalFileFullPath
     *            the original file (full path)
     * @param xIndex
     *            x index of the tile (starts from 1)
     * @param yIndex
     *            y index of the tile (starts from 1)
     * @return a string representing the file name (without path) for the output file
     */
    public abstract String getOutputFileName(String originalFileFullPath, int xIndex, int yIndex);

    protected String getNameWithoutPathAndExtension(String originalFileFullPath)
    {
        File file = new File(originalFileFullPath);
        String fileName = file.getName();
        int dot = fileName.lastIndexOf(".");
        if (dot > -1)
        {
            return fileName.substring(0, dot);
        }
        else
        {
            return fileName;
        }
    }

}
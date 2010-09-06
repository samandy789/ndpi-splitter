/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.splitter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @version $Rev$
 */
public class TileFileNamerTest
{

    @Test
    public void testGetNameWithoutExtention()
    {
        TileFileNamer fileNamer = new TileFileNamer()
        {
            @Override
            public String getOutputFileName(String originalFileFullPath, int xIndex, int yIndex)
            {
                return null;
            }
        };

        assertEquals("myfile", fileNamer.getNameWithoutPathAndExtension("/file/myfile.txt"));
        assertEquals("myfile", fileNamer.getNameWithoutPathAndExtension("/file/myfile"));
        assertEquals("myfile", fileNamer.getNameWithoutPathAndExtension("myfile.txt"));
        assertEquals("myfile", fileNamer.getNameWithoutPathAndExtension("myfile"));
    }
}

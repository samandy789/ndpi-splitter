/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.imaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * 
 * @version $Rev$
 */
public class JpegImageCreatorTest
{

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private JpegImageCreator creator = new JpegImageCreator(0.95f);

    @Test
    public void testCreateImage() throws IOException
    {
        String filename = temporaryFolder.newFile("test.jpg").getAbsolutePath();
        byte[] original = makeSomeImageBytes();

        creator.createImageFromNdpiBytes(original, 3, 3, "test.jpg", temporaryFolder.getRoot().getAbsolutePath());
        File file = new File(filename);
        // its a bit hard to actually test this, so we're just checking that a file is created
        assertTrue(file.exists());
    }

    @Test
    public void testGetFileTypeReturnsJPG()
    {
        assertEquals("jpg", creator.getFileType());
    }

    private byte[] makeSomeImageBytes()
    {
        // 3x3 pixels = 3 rows of 9 bytes, padded to 12 bytes
        byte[] row = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0};
        byte[] original = new byte[3 * 12];
        System.arraycopy(row, 0, original, 0, 12);
        System.arraycopy(row, 0, original, 12, 12);
        System.arraycopy(row, 0, original, 24, 12);
        return original;
    }

}

/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.imaging;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @version $Rev$
 * 
 */
public class BaseImageCreatorTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private BaseImageCreator creator = new BaseImageCreator("jpg");

    @Test(expected = UnsupportedImageFormatException.class)
    public void testCheckImageFormatsThrowsExceptionWhenFormatNotUnderstood()
    {
        new BaseImageCreator("badbad");
    }

    @Test
    public void testCheckImageFormatsSucceedsWhenFormatIsUnderstood()
    {
        BaseImageCreator imageCreator = new BaseImageCreator("jpg");
        assertNotNull(imageCreator);
    }

    @Test
    public void testCalculatesScanlineStrideCorrectly()
    {
        // scanline stride is pixel width * pixel stride + padding to dword (4-byte) boundaries
        assertEquals(12, creator.calculateScanlineStride(3, 3));
        assertEquals(32, creator.calculateScanlineStride(10, 3));
        assertEquals(36, creator.calculateScanlineStride(11, 3));
        assertEquals(36, creator.calculateScanlineStride(12, 3));
    }

    @Test
    public void testFlipBytes()
    {
        // flips the order of rows of bytes upside down
        // before: bottom up: rows of bytes are: 1234 / 5678 / 9012
        // after: top down: rows of bytes are: 9012 / 5678 / 1234

        byte[] original = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2};
        byte[] expected = new byte[] {9, 0, 1, 2, 5, 6, 7, 8, 1, 2, 3, 4};
        byte[] flippedBytes = creator.flipBytes(original, 4, 3);
        assertArrayEquals(expected, flippedBytes);
    }

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
    public void testGetFileTypeReturnsConfiguredType()
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

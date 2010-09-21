/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.snapshot;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import au.org.intersect.ndpisplitter.splitter.TileFileNamer;

/**
 * @version $Rev$
 * 
 */
public class SnapshotFileNamerTest
{
    private static final String ORIGINAL_DIRECTORY = "C:" + File.separator + "mydocs" + File.separator + "anotherdir";
    private static final String ORIGINAL = ORIGINAL_DIRECTORY + File.separator + "my ndpifile.ndpi";
    private static final String ORIGINAL_NO_EXTENSION = ORIGINAL_DIRECTORY + File.separator + "my ndpifile";
    private static final String ORIGINAL_NO_PATH = "my ndpifile.ndpi";
    private TileFileNamer namer;

    @Before
    public void setUp()
    {
        namer = new SnapshotFileNamer("bmp");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailsIfYCoordinateGreaterThan1()
    {
        namer.getOutputFileName("original", 1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailsIfYCoordinateLessThan1()
    {
        namer.getOutputFileName("original", 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailsIfXCoordinateGreaterThan1()
    {
        namer.getOutputFileName("original", 2, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailsIfXCoordinateLessThan1()
    {
        namer.getOutputFileName("original", 0, 1);
    }

    @Test
    public void testFileNamerCreatesCorrectNameWhenOriginalHasFileExtension()
    {
        assertFileName("my ndpifile.bmp", 1, 1, ORIGINAL);
    }

    @Test
    public void testFileNamerCreatesCorrectNameWhenOriginalHasNoFileExtension()
    {
        assertFileName("my ndpifile.bmp", 1, 1, ORIGINAL_NO_EXTENSION);
    }

    @Test
    public void testFileNamerCreatesCorrectNameWhenOriginalHasNoPathInfo()
    {
        assertFileName("my ndpifile.bmp", 1, 1, ORIGINAL_NO_PATH);
    }

    private void assertFileName(String expected, int xCoordinate, int yCoordinate, String originalFileName)
    {
        assertEquals(expected, namer.getOutputFileName(originalFileName, xCoordinate, yCoordinate));
    }

}

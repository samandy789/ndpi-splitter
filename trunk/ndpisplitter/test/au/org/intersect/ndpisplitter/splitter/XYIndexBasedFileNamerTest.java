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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @version $Rev$
 */
public class XYIndexBasedFileNamerTest
{

    private static final String ORIGINAL_DIRECTORY = "C:" + File.separator + "mydocs" + File.separator + "anotherdir";
    private static final String ORIGINAL = ORIGINAL_DIRECTORY + File.separator + "my ndpifile.ndpi";
    private static final String ORIGINAL_NO_EXTENSION = ORIGINAL_DIRECTORY + File.separator + "my ndpifile";
    private static final String ORIGINAL_NO_PATH = "my ndpifile.ndpi";
    private TileFileNamer namer;

    @Before
    public void setUp()
    {
        SequenceCodeGenerator codeGenerator = mock(SequenceCodeGenerator.class);
        namer = new XYIndexBasedFileNamer(codeGenerator, "bmp");
        // stub the mock sequence code generator to always return "aa" - code generation is tested elsewhere
        when(codeGenerator.getSequenceCode(anyInt())).thenReturn("aa");
    }

    @Test
    public void testFileNamerCreatesCorrectNameWhenOriginalHasFileExtension()
    {
        assertFileName("my ndpifile_aa_1.bmp", 1, 0, ORIGINAL);
        assertFileName("my ndpifile_aa_2.bmp", 2, 0, ORIGINAL);
        assertFileName("my ndpifile_aa_500.bmp", 500, 0, ORIGINAL);
    }

    @Test
    public void testFileNamerCreatesCorrectNameWhenOriginalHasNoFileExtension()
    {
        assertFileName("my ndpifile_aa_1.bmp", 1, 0, ORIGINAL_NO_EXTENSION);
        assertFileName("my ndpifile_aa_2.bmp", 2, 0, ORIGINAL_NO_EXTENSION);
        assertFileName("my ndpifile_aa_500.bmp", 500, 0, ORIGINAL_NO_EXTENSION);
    }

    @Test
    public void testFileNamerCreatesCorrectNameWhenOriginalHasNoPathInfo()
    {
        assertFileName("my ndpifile_aa_1.bmp", 1, 0, ORIGINAL_NO_PATH);
        assertFileName("my ndpifile_aa_2.bmp", 2, 0, ORIGINAL_NO_PATH);
        assertFileName("my ndpifile_aa_500.bmp", 500, 0, ORIGINAL_NO_PATH);
    }

    private void assertFileName(String expected, int xCoordinate, int yCoordinate, String originalFileName)
    {
        assertEquals(expected, namer.getOutputFileName(originalFileName, xCoordinate, yCoordinate));
    }
}

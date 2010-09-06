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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.awt.image.Raster;
import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.org.intersect.ndpisplitter.file.FileManager;
import au.org.intersect.ndpisplitter.processing.TileAnalyser;

/**
 * 
 * @version $Rev$
 */
public class FilterEmptyImagesImageCreatorTest
{

    @Mock
    private FileManager fileManager;

    @Mock
    private TileAnalyser tileAnalizer;

    @Mock
    private Raster raster;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreatesSuitableSubdirectoryForUninterestingImages()
    {
        FilterEmptyImagesImageCreator creator = new FilterEmptyImagesImageCreator("tiff", tileAnalizer, "empty",
                fileManager);
        when(tileAnalizer.isTileInteresting(raster, "outputfile", "outputdir", 200, 100, 12)).thenReturn(false);
        String outputFilePath = creator.decideWhereToStoreImage(raster, "myoutput.tiff", "/output", 200, 100, 12);
        assertEquals("/output" + File.separator + "empty" + File.separator + "myoutput.tiff", outputFilePath);
        verify(fileManager).createDirectoryIfNeeded(new File("/output/empty"));
    }

    @Test
    public void testReturnsBaseDirectoryForInterestingImages()
    {
        FilterEmptyImagesImageCreator creator = new FilterEmptyImagesImageCreator("tiff", tileAnalizer, "empty",
                fileManager);
        when(tileAnalizer.isTileInteresting(raster, "myoutput.tiff", "/output", 200, 100, 12)).thenReturn(true);
        String outputFilePath = creator.decideWhereToStoreImage(raster, "myoutput.tiff", "/output", 200, 100, 12);
        assertEquals("/output" + File.separator + "myoutput.tiff", outputFilePath);
        verifyZeroInteractions(fileManager);
    }
}

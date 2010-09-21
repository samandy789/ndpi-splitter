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
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.org.intersect.ndpisplitter.ndpireader.ImageInformation;
import au.org.intersect.ndpisplitter.splitter.ImageReadingException;
import au.org.intersect.ndpisplitter.splitter.ImageTilingException;
import au.org.intersect.ndpisplitter.splitter.NdpiFileInfoGetter;
import au.org.intersect.ndpisplitter.splitter.NdpiFileSplitter;
import au.org.intersect.ndpisplitter.splitter.StatusUpdater;
import au.org.intersect.ndpisplitter.splitter.TilePositions;
import au.org.intersect.snapshotcreator.config.SnapshotCreatorProperties;
import au.org.intersect.snapshotcreator.file.FileProcessorException;

/**
 * 
 * @version $Rev$
 */
public class SnapshotCreatorHandlerTest
{

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private SnapshotCreatorHandler snapshotCreatorHandler;

    @Mock
    private NdpiFileSplitter splitter;

    @Mock
    private NdpiFileInfoGetter fileInfoGetter;

    @Mock
    private StatusUpdater statusUpdater;

    @Mock
    private SnapshotCreatorProperties properties;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        snapshotCreatorHandler = new SnapshotCreatorHandler(properties, splitter, fileInfoGetter, statusUpdater);
    }

    @Test
    public void testCorrectlyTilesImage() throws IOException, ImageReadingException, ImageTilingException
    {
        File file = folder.newFile("text.txt");
        String filePath = file.getAbsolutePath();
        ImageInformation imageInfo = new ImageInformation();
        imageInfo.setImageHeightInPixels(200);
        imageInfo.setImageWidthInPixels(300);
        imageInfo.setPhysicalXPositionOfCentreInNanometres(123);
        imageInfo.setPhysicalYPositionOfCentreInNanometres(432);
        imageInfo.setSourceLensMagnification(5);
        when(fileInfoGetter.getImageInformation(filePath)).thenReturn(imageInfo);
        when(properties.getSnapshotsWorkDirectory()).thenReturn("snapswork");
        when(properties.getOutputMagnification()).thenReturn(5);
        when(properties.getMaxPixelsPerSnapshot()).thenReturn(500000);
        snapshotCreatorHandler.handleFile(file);

        ArgumentCaptor<String> filePathArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TilePositions> tilePosArg = ArgumentCaptor.forClass(TilePositions.class);
        ArgumentCaptor<ImageInformation> imageInfoArg = ArgumentCaptor.forClass(ImageInformation.class);
        ArgumentCaptor<Integer> magArg = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<File> outputPathArg = ArgumentCaptor.forClass(File.class);
        ArgumentCaptor<StatusUpdater> statusUpdaterArg = ArgumentCaptor.forClass(StatusUpdater.class);
        verify(splitter).tileImage(filePathArg.capture(), tilePosArg.capture(), imageInfoArg.capture(),
                magArg.capture(), outputPathArg.capture(), statusUpdaterArg.capture());

        assertEquals(filePath, filePathArg.getValue());
        assertSame(imageInfo, imageInfoArg.getValue());
        assertEquals(5, magArg.getValue().intValue());
        assertEquals("snapswork", outputPathArg.getValue().getName());
        assertSame(statusUpdater, statusUpdaterArg.getValue());

        TilePositions tilePositions = tilePosArg.getValue();
        assertEquals(100, tilePositions.getTileHeightInPixels());
        assertEquals(150, tilePositions.getTileWidthInPixels());
        assertEquals(123, tilePositions.getTileXPositions().get(0).intValue());
        assertEquals(432, tilePositions.getTileYPositions().get(0).intValue());
    }

    @Test(expected = FileProcessorException.class)
    public void testWrapsTilingExceptionCorrectly() throws ImageTilingException, IOException, ImageReadingException
    {
        File file = folder.newFile("text.txt");
        when(fileInfoGetter.getImageInformation(anyString())).thenReturn(new ImageInformation());
        when(properties.getSnapshotsWorkDirectory()).thenReturn("Something");
        doThrow(new ImageTilingException(null)).when(splitter).tileImage(anyString(), any(TilePositions.class),
                any(ImageInformation.class), anyInt(), any(File.class), any(StatusUpdater.class));
        snapshotCreatorHandler.handleFile(file);
    }

    @Test(expected = FileProcessorException.class)
    public void testWrapsReadingExceptionCorrectly() throws IOException, ImageReadingException
    {
        File file = folder.newFile("text.txt");
        when(fileInfoGetter.getImageInformation(anyString())).thenThrow(new ImageReadingException(""));
        snapshotCreatorHandler.handleFile(file);
    }
}

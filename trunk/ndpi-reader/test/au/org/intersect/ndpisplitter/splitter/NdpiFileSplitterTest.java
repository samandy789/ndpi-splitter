/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.splitter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.org.intersect.ndpisplitter.file.FileManager;
import au.org.intersect.ndpisplitter.imaging.ImageCreator;
import au.org.intersect.ndpisplitter.ndpireader.ImageInformation;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadException;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadWrapper;

/**
 * @version $Rev$
 * 
 */
public class NdpiFileSplitterTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private NDPReadWrapper wrapper;
    @Mock
    private ImageCreator imageCreator;
    @Mock
    private TileFileNamer fileNamer;
    @Mock
    private TilePositions tilePositions;
    @Mock
    private StatusUpdater statusUpdater;
    @Mock
    private FileManager directoryCreator;

    private NdpiFileSplitter splitter;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        when(fileNamer.getOutputFileName(anyString(), anyInt(), anyInt())).thenReturn(
                temporaryFolder.getRoot().getAbsolutePath() + "/something.txt");

        splitter = new NdpiFileSplitter(wrapper, imageCreator, fileNamer, directoryCreator);
    }

    @Test
    public void testCorrectTilesAreCreated() throws NDPReadException, ImageTilingException, IOException
    {
        ImageInformation info = createImageInformation();

        // setup mocks so we get 2x3 tiles
        when(tilePositions.getTileXPositions()).thenReturn(Arrays.asList(new Long[] {1L, 2L}));
        when(tilePositions.getTileYPositions()).thenReturn(Arrays.asList(new Long[] {5L, 7L, 9L}));
        when(tilePositions.getTileWidthInPixels()).thenReturn(20);
        when(tilePositions.getTileHeightInPixels()).thenReturn(10);

        File outputDirectory = temporaryFolder.newFolder("output");

        when(fileNamer.getOutputFileName("testfile", 1, 1)).thenReturn("x1-y1");
        when(fileNamer.getOutputFileName("testfile", 1, 2)).thenReturn("x1-y2");
        when(fileNamer.getOutputFileName("testfile", 1, 3)).thenReturn("x1-y3");
        when(fileNamer.getOutputFileName("testfile", 2, 1)).thenReturn("x2-y1");
        when(fileNamer.getOutputFileName("testfile", 2, 2)).thenReturn("x2-y2");
        when(fileNamer.getOutputFileName("testfile", 2, 3)).thenReturn("x2-y3");

        byte[] t1Bytes = "1".getBytes();
        byte[] t2Bytes = "2".getBytes();
        byte[] t3Bytes = "3".getBytes();
        byte[] t4Bytes = "4".getBytes();
        byte[] t5Bytes = "5".getBytes();
        byte[] t6Bytes = "6".getBytes();
        when(wrapper.getImageSegment("testfile", 1, 5, 0, 30.0f, 20, 10)).thenReturn(t1Bytes);
        when(wrapper.getImageSegment("testfile", 2, 5, 0, 30.0f, 20, 10)).thenReturn(t2Bytes);
        when(wrapper.getImageSegment("testfile", 1, 7, 0, 30.0f, 20, 10)).thenReturn(t3Bytes);
        when(wrapper.getImageSegment("testfile", 2, 7, 0, 30.0f, 20, 10)).thenReturn(t4Bytes);
        when(wrapper.getImageSegment("testfile", 1, 9, 0, 30.0f, 20, 10)).thenReturn(t5Bytes);
        when(wrapper.getImageSegment("testfile", 2, 9, 0, 30.0f, 20, 10)).thenReturn(t6Bytes);

        splitter.tileImage("testfile", tilePositions, info, 30, outputDirectory, statusUpdater);

        // create inOrder object passing any mocks that need to be verified in order
        InOrder inOrder = inOrder(imageCreator, directoryCreator, wrapper);

        inOrder.verify(directoryCreator).createDirectoryIfNeeded(outputDirectory);
        // they come out ordered y then x as follows
        String outputPath = outputDirectory.getAbsolutePath();
        inOrder.verify(imageCreator).createImageFromNdpiBytes(t1Bytes, 20, 10, "x1-y1", outputPath);
        inOrder.verify(imageCreator).createImageFromNdpiBytes(t2Bytes, 20, 10, "x2-y1", outputPath);
        inOrder.verify(imageCreator).createImageFromNdpiBytes(t3Bytes, 20, 10, "x1-y2", outputPath);
        inOrder.verify(imageCreator).createImageFromNdpiBytes(t4Bytes, 20, 10, "x2-y2", outputPath);
        inOrder.verify(imageCreator).createImageFromNdpiBytes(t5Bytes, 20, 10, "x1-y3", outputPath);
        inOrder.verify(imageCreator).createImageFromNdpiBytes(t6Bytes, 20, 10, "x2-y3", outputPath);
        inOrder.verify(wrapper).cleanUp();
        verifyNoMoreInteractions(imageCreator);
    }

    @Test
    public void testCorrectStatusUpdatesAreSent() throws FileNotFoundException, NDPReadException, ImageTilingException
    {
        ImageInformation info = createImageInformation();

        // setup mocks so we get 3x4 tiles
        when(tilePositions.getTileXPositions()).thenReturn(Arrays.asList(new Long[] {1L, 2L, 3L}));
        when(tilePositions.getTileYPositions()).thenReturn(Arrays.asList(new Long[] {5L, 7L, 9L, 11L}));
        when(tilePositions.getTileWidthInPixels()).thenReturn(20);
        when(tilePositions.getTileHeightInPixels()).thenReturn(10);
        when(tilePositions.getTotalNumberOfTiles()).thenReturn(12);

        File outputDirectory = temporaryFolder.newFolder("output");

        splitter.tileImage("testfile", tilePositions, info, 30, outputDirectory, statusUpdater);

        // check that we got updates for tiles 1-12 being completed
        verify(statusUpdater).setNumberOfTiles(12);
        verify(statusUpdater).setNumberOfTilesCompleted(0);
        verify(statusUpdater).setNumberOfTilesCompleted(1);
        verify(statusUpdater).setNumberOfTilesCompleted(2);
        verify(statusUpdater).setNumberOfTilesCompleted(3);
        verify(statusUpdater).setNumberOfTilesCompleted(4);
        verify(statusUpdater).setNumberOfTilesCompleted(5);
        verify(statusUpdater).setNumberOfTilesCompleted(6);
        verify(statusUpdater).setNumberOfTilesCompleted(7);
        verify(statusUpdater).setNumberOfTilesCompleted(8);
        verify(statusUpdater).setNumberOfTilesCompleted(9);
        verify(statusUpdater).setNumberOfTilesCompleted(10);
        verify(statusUpdater).setNumberOfTilesCompleted(11);
        verify(statusUpdater).setNumberOfTilesCompleted(12);
        verifyNoMoreInteractions(statusUpdater);
    }

    @Test
    public void testCatchesAndRethrowsAnyExceptions() throws FileNotFoundException, NDPReadException
    {
        doThrow(new RuntimeException()).when(directoryCreator).createDirectoryIfNeeded(any(File.class));
        try
        {
            File outputDirectory = temporaryFolder.newFolder("f1").getAbsoluteFile();
            splitter.tileImage("somefile", tilePositions, new ImageInformation(), 30, outputDirectory, statusUpdater);
            // should still clean up on exception
            verify(wrapper).cleanUp();
            fail("Should throw exception when an error occurs");
        }
        catch (ImageTilingException ite)
        {
            assertNotNull(ite.getCause());
        }
    }

    private ImageInformation createImageInformation()
    {
        return new ImageInformation();
    }
}

/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.file;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @version $Rev$
 * 
 */
public class SingleFileProcessorTest
{
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Mock
    private FileHandler handler;
    @Mock
    private FileHandler successHandler;
    @Mock
    private FileHandler failureHandler;

    private FilenameFilter filter;

    private String nonExistentDirectory;
    private String validFile;
    private String rootDirectoryPath;
    private String emptyDirectoryPath;
    private File txtFile1;

    private SingleFileProcessor processor;

    @Before
    public void setUp() throws IOException
    {
        MockitoAnnotations.initMocks(this);
        nonExistentDirectory = tempFolder.getRoot().getAbsolutePath() + File.separator + "nonexistent";
        validFile = tempFolder.newFile("test").getAbsolutePath();
        rootDirectoryPath = tempFolder.getRoot().getAbsolutePath();
        emptyDirectoryPath = tempFolder.newFolder("empty").getAbsolutePath();

        txtFile1 = tempFolder.newFile("test1.txt");
        processor = new SingleFileProcessor(handler, successHandler, failureHandler);
    }

    @Test(expected = FileProcessorException.class)
    public void testFileProcessorRejectsNonExistentFile()
    {
        processor.processFile(new File("asdf.txt"));
    }

    @Test(expected = FileProcessorException.class)
    public void testFileProcessorRejectsFileThatIsADirectory()
    {
        processor.processFile(tempFolder.getRoot());
    }

    @Test
    public void testFileProcessorCallsSuccessHandlerIfEverythingWorksOk()
    {
        processor.processFile(txtFile1);

        verify(handler).handleFile(txtFile1);
        verify(successHandler).handleFile(txtFile1);
        verifyNoMoreInteractions(handler, successHandler, failureHandler);
    }

    @Test
    public void testFileProcessorCallsFailureHandlerIfSomethingGoesWrong()
    {
        // make the second file fail
        doThrow(new IllegalArgumentException()).when(handler).handleFile(txtFile1);

        processor.processFile(txtFile1);

        verify(handler).handleFile(txtFile1);
        verify(failureHandler).handleFile(txtFile1);
        verifyNoMoreInteractions(handler, successHandler, failureHandler);
    }

    @Test
    public void testErrorsInSuccessHandlerAreIgnored()
    {
        // make the success handler throw an error
        doThrow(new IllegalArgumentException()).when(successHandler).handleFile(txtFile1);

        processor.processFile(txtFile1);

        verify(handler).handleFile(txtFile1);
        verify(successHandler).handleFile(txtFile1);
        verifyNoMoreInteractions(handler, successHandler, failureHandler);
    }

    @Test
    public void testErrorsInFailureHandlerAreIgnored()
    {
        // make the second file fail and its failure handler throw an error
        doThrow(new IllegalArgumentException()).when(handler).handleFile(txtFile1);
        doThrow(new IllegalArgumentException()).when(failureHandler).handleFile(txtFile1);

        processor.processFile(txtFile1);

        verify(handler).handleFile(txtFile1);
        verify(failureHandler).handleFile(txtFile1);
        verifyNoMoreInteractions(handler, successHandler, failureHandler);
    }
}

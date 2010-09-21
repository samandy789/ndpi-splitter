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
import static org.mockito.Mockito.verifyZeroInteractions;

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
public class DirectoryBasedMultipleFileProcessorTest
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
    private File txtFile2;
    private File txtFile3;
    private File docFile1;

    @Before
    public void setUp() throws IOException
    {
        MockitoAnnotations.initMocks(this);
        nonExistentDirectory = tempFolder.getRoot().getAbsolutePath() + File.separator + "nonexistent";
        validFile = tempFolder.newFile("test").getAbsolutePath();
        rootDirectoryPath = tempFolder.getRoot().getAbsolutePath();
        emptyDirectoryPath = tempFolder.newFolder("empty").getAbsolutePath();

        txtFile1 = tempFolder.newFile("test1.txt");
        txtFile2 = tempFolder.newFile("test2.txt");
        txtFile3 = tempFolder.newFile("test3.txt");
        docFile1 = tempFolder.newFile("test4.doc");

        filter = new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".txt");
            }
        };
    }

    @Test(expected = FileProcessorException.class)
    public void testFileProcessorRejectsNonExistentDirectory()
    {
        DirectoryBasedMultipleFileProcessor processor = new DirectoryBasedMultipleFileProcessor(nonExistentDirectory,
                successHandler, failureHandler, handler, filter);
        processor.processFiles();
    }

    @Test(expected = FileProcessorException.class)
    public void testFileProcessorRejectsDirectoryThatsActuallyAFile()
    {
        DirectoryBasedMultipleFileProcessor processor = new DirectoryBasedMultipleFileProcessor(validFile,
                successHandler, failureHandler, handler, filter);
        processor.processFiles();
    }

    @Test
    public void testFileProcessorOnlyHandlesFilesOfConfiguredExtension()
    {
        DirectoryBasedMultipleFileProcessor processor = new DirectoryBasedMultipleFileProcessor(rootDirectoryPath,
                successHandler, failureHandler, handler, filter);
        processor.processFiles();

        // should only handle .txt files
        verify(handler).handleFile(txtFile1);
        verify(handler).handleFile(txtFile2);
        verify(handler).handleFile(txtFile3);
        verifyNoMoreInteractions(handler);
    }

    @Test
    public void testFileProcessorDoesNothingIfNoFilesMatch()
    {
        DirectoryBasedMultipleFileProcessor processor = new DirectoryBasedMultipleFileProcessor(emptyDirectoryPath,
                successHandler, failureHandler, handler, filter);
        processor.processFiles();

        verifyZeroInteractions(handler);
    }

    @Test
    public void testFileProcessorCallsFailureHandlerIfSomethingGoesWrongAndSuccessHandlerIfItsOk()
    {
        DirectoryBasedMultipleFileProcessor processor = new DirectoryBasedMultipleFileProcessor(rootDirectoryPath,
                successHandler, failureHandler, handler, filter);
        // make the second file fail
        doThrow(new IllegalArgumentException()).when(handler).handleFile(txtFile2);

        processor.processFiles();

        verify(handler).handleFile(txtFile1);
        verify(successHandler).handleFile(txtFile1);
        verify(handler).handleFile(txtFile2);
        verify(failureHandler).handleFile(txtFile2);
        verify(handler).handleFile(txtFile3);
        verify(successHandler).handleFile(txtFile3);
        verifyNoMoreInteractions(handler, successHandler, failureHandler);
    }

    @Test
    public void testErrorsInSuccessOrFailureHandlerAreIgnored()
    {
        DirectoryBasedMultipleFileProcessor processor = new DirectoryBasedMultipleFileProcessor(rootDirectoryPath,
                successHandler, failureHandler, handler, filter);
        // make the second file fail and its failure handler throw an error
        doThrow(new IllegalArgumentException()).when(handler).handleFile(txtFile2);
        doThrow(new IllegalArgumentException()).when(failureHandler).handleFile(txtFile2);
        // make success handler throw error for 3rd file
        doThrow(new IllegalArgumentException()).when(successHandler).handleFile(txtFile3);

        processor.processFiles();

        verify(handler).handleFile(txtFile1);
        verify(successHandler).handleFile(txtFile1);
        verify(handler).handleFile(txtFile2);
        verify(failureHandler).handleFile(txtFile2);
        verify(handler).handleFile(txtFile3);
        verify(successHandler).handleFile(txtFile3);
        verifyNoMoreInteractions(handler, successHandler, failureHandler);
    }

}

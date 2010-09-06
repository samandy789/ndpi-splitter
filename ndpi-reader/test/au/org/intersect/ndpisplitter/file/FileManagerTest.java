/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.file;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @version $Rev$
 * 
 */
public class FileManagerTest
{
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private FileManager fileManager = new FileManager();

    private String fileNameThatExists;
    private String fileNameThatDoesNotExist;
    private String directoryPath;

    @Before
    public void setUp() throws IOException
    {
        directoryPath = folder.getRoot().getAbsolutePath();

        File fileThatExists = folder.newFile(new Random().nextInt() + "file1.txt");
        fileNameThatExists = fileThatExists.getAbsolutePath();

        fileNameThatDoesNotExist = directoryPath + new Random().nextInt() + "file2.txt";
        File fileThatDoesntExist = new File(fileNameThatDoesNotExist);

        assertFalse(fileThatDoesntExist.exists());
        assertTrue(fileThatExists.exists());
    }

    @Test
    public void testCheckExistenceReturnsSuccessfullyIfFileExists() throws IOException
    {
        fileManager.checkFileExists(fileNameThatExists);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCheckExistenceThrowsExceptionIfFileDoesNotExist() throws IOException
    {
        fileManager.checkFileExists(fileNameThatDoesNotExist);
    }

    @Test
    public void testCreateDirectoryIfNeededDeletesIfPathIsAFile() throws IOException
    {
        File directory = folder.newFile("afile");
        fileManager.createDirectoryIfNeeded(directory);
        File after = directory.getAbsoluteFile();
        assertTrue(after.exists());
        assertTrue(after.isDirectory());
    }

    @Test
    public void testCreateDirectoryIfNeededDoesNothingIfPathIsAlreadyADirectory() throws IOException
    {
        File directory = folder.newFolder("afolder");
        fileManager.createDirectoryIfNeeded(directory);
        File after = directory.getAbsoluteFile();
        assertTrue(after.exists());
        assertTrue(after.isDirectory());
    }

    @Test
    public void testCreateDirectoryIfNeededCreatesDirectoryIfItDoesntExist() throws IOException
    {
        String path = folder.getRoot().getAbsolutePath() + File.separator + "newfolder";
        File directory = new File(path);
        assertFalse(directory.exists());

        fileManager.createDirectoryIfNeeded(directory);
        File after = new File(path);
        assertTrue(after.exists());
        assertTrue(after.isDirectory());
    }

    @Test
    public void testMoveFile() throws IOException
    {
        File destinationDirectory = folder.newFolder("dest");
        File sourceFile = folder.newFile("myfile.txt");
        fileManager.moveFile(sourceFile, destinationDirectory);
        String newPath = destinationDirectory.getAbsolutePath() + File.separator + "myfile.txt";
        File newFile = new File(newPath);
        assertTrue(newFile.exists());
    }

    @Test(expected = FileManagerException.class)
    public void testMoveFileThrowsExceptionIfSomethingGoesWrong() throws IOException
    {
        File destinationDirectory = folder.newFolder("dest");
        File sourceFile = new File(folder.getRoot().getAbsolutePath() + File.separator + "doesntexist.txt");
        fileManager.moveFile(sourceFile, destinationDirectory);
    }
}

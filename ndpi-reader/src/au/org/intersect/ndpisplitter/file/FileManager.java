/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.file;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Utility class with various file-handling methods
 * 
 * @version $Rev$
 */
public class FileManager
{

    /**
     * Creates a directory for the given path if needed. If the path currently represents a file, the file is deleted
     * and replaced with a directory. If the directory already exists, nothing is done. If the directory doesn't exist,
     * it is created.
     * 
     * @param directory
     *            to be created if needed
     */
    public void createDirectoryIfNeeded(File directory)
    {
        if (directory.exists() && !directory.isDirectory())
        {
            directory.delete();
        }
        if (!directory.exists())
        {
            directory.mkdir();
        }
    }

    /**
     * Checks that the file with the given path exists.
     * 
     * @param fileName
     * @throws FileNotFoundException
     *             if the file does not exist
     */
    public void checkFileExists(String fileName) throws FileNotFoundException
    {
        File file = new File(fileName);
        if (!file.exists())
        {
            throw new FileNotFoundException(fileName);
        }
    }

    /**
     * Moves the specified file to the given destination directory
     * 
     * @param file
     *            the file to be moved
     * @param destinationDirectory
     *            the directory to move it to
     */
    public void moveFile(File file, File destinationDirectory)
    {
        createDirectoryIfNeeded(destinationDirectory);
        File destinationFile = new File(destinationDirectory, file.getName());
        boolean success = file.renameTo(destinationFile);
        if (!success)
        {
            throw new FileManagerException("Failed to move file " + file.getAbsolutePath() + " to directory "
                    + destinationDirectory.getAbsolutePath());
        }
    }

}

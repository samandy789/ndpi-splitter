/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.file;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.log4j.Logger;

/**
 * 
 * @version $Rev$
 */
public class DirectoryBasedMultipleFileProcessor
{
    private static final Logger LOG = Logger.getLogger(DirectoryBasedMultipleFileProcessor.class);

    private String directoryToProcess;
    private final FileHandler successHandler;
    private final FileHandler failureHandler;
    private final FileHandler handler;
    private final FilenameFilter filenameFilter;

    public DirectoryBasedMultipleFileProcessor(String directoryToProcess, FileHandler successHandler,
            FileHandler failureHandler, FileHandler handler, FilenameFilter filenameFilter)
    {
        super();
        this.directoryToProcess = directoryToProcess;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.handler = handler;
        this.filenameFilter = filenameFilter;
    }

    public void processFiles()
    {
        File directory = new File(directoryToProcess);
        validateDirectory(directory);
        File[] matchingFiles = directory.listFiles(filenameFilter);
        processFiles(directory, matchingFiles);
    }

    private void processFiles(File directory, File[] matchingFileNames)
    {
        for (File file : matchingFileNames)
        {
            try
            {
                handler.handleFile(file);
                handleSuccess(file);
            }
            catch (Throwable e)
            {
                // catch anything and everything that goes wrong, we want to continue with other files even if one fails
                handleFailure(file, e);
            }
        }

    }

    private void handleSuccess(File file)
    {
        try
        {
            successHandler.handleFile(file);
        }
        catch (Throwable t)
        {
            LOG.error(handler.getClass().getSimpleName() + " failed to run success handler. "
                    + "The file has still been processed but this should be investigated.");
        }
    }

    private void handleFailure(File file, Throwable e)
    {
        LOG.error(handler.getClass().getSimpleName() + " failed to process file " + file.getAbsolutePath(), e);
        try
        {
            failureHandler.handleFile(file);
        }
        catch (Throwable t)
        {
            LOG.info("Failed to execute failure handler for failed file, please investigate", t);
        }
    }

    private void validateDirectory(File directory)
    {
        if (!directory.exists())
        {
            throw new FileProcessorException("Configured directory does not exist " + directory);
        }
        if (!directory.isDirectory())
        {
            throw new FileProcessorException("Configured directory is not a directory " + directory);
        }
    }

}

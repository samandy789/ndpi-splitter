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

import org.apache.log4j.Logger;

/**
 * 
 * @version $Rev$
 */
public class SingleFileProcessor
{
    private static final Logger LOG = Logger.getLogger(SingleFileProcessor.class);

    private final FileHandler successHandler;
    private final FileHandler failureHandler;
    private final FileHandler handler;

    public SingleFileProcessor(FileHandler handler, FileHandler successHandler, FileHandler failureHandler)
    {
        super();
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.handler = handler;
    }

    public void processFile(File file)
    {
        validateFile(file);
        try
        {
            handler.handleFile(file);
            handleSuccess(file);
        }
        catch (Throwable e)
        {
            // catch anything and everything that goes wrong, we want to handle it accordingly
            handleFailure(file, e);
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

    private File validateFile(File file)
    {
        if (!file.exists())
        {
            throw new FileProcessorException("File to process does not exist: " + file.getAbsolutePath());
        }
        if (file.isDirectory())
        {
            throw new FileProcessorException("File to process is a directory " + file.getAbsolutePath());
        }
        return file;
    }
}

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

import au.org.intersect.ndpisplitter.file.FileManager;

/**
 * 
 * @version $Rev$
 */
public class FileMover implements FileHandler
{
    private static final Logger LOG = Logger.getLogger(FileMover.class);
    private String destination;

    private final FileManager fileManager;

    public FileMover(String destination)
    {
        this(destination, new FileManager());
    }

    public FileMover(String destination, FileManager fileManager)
    {
        super();
        this.destination = destination;
        this.fileManager = fileManager;
    }

    @Override
    public void handleFile(File file)
    {
        LOG.info("Moving " + file.getName() + " to " + destination);
        File destinationDirectory = new File(destination);
        fileManager.moveFile(file, destinationDirectory);
    }

}

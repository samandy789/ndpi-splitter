/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import au.org.intersect.ndpisplitter.splitter.NdpiFileInfoGetter;
import au.org.intersect.ndpisplitter.splitter.NdpiFileSplitter;
import au.org.intersect.snapshotcreator.config.SnapshotCreatorProperties;
import au.org.intersect.snapshotcreator.factory.ComponentFactory;
import au.org.intersect.snapshotcreator.file.FileHandler;
import au.org.intersect.snapshotcreator.file.FileMover;
import au.org.intersect.snapshotcreator.file.SingleFileProcessor;
import au.org.intersect.snapshotcreator.snapshot.NullStatusUpdater;
import au.org.intersect.snapshotcreator.snapshot.SnapshotCreatorHandler;

/**
 * TODO: does too much - refactor me
 * 
 * @version $Rev$
 */
public class SnapshotCreatorMain
{
    private static final String JNA_LIBRARY_PATH_PROPERTY = "jna.library.path";
    private static final Logger LOG = Logger.getLogger(SnapshotCreatorMain.class);

    private final NdpiFileSplitter splitter;
    private final NdpiFileInfoGetter fileInfoGetter;
    private SnapshotCreatorProperties properties;

    public SnapshotCreatorMain() throws IOException
    {
        super();
        LOG.info("--------------SnapshotCreatorMain starting-----------------");
        configureEnvironment();
        Properties propertiesFile = loadProperties();
        properties = new SnapshotCreatorProperties(propertiesFile);
        splitter = ComponentFactory.getNdpiFileSplitterInstance(properties);
        fileInfoGetter = ComponentFactory.getNdpiFileInfoGetterInstance();
    }

    public static void main(String[] args) throws IOException
    {
        try
        {
            if (args.length != 1)
            {
                throw new IllegalArgumentException("SnapshotCreatorMain must be called with exactly 1 "
                        + "argument (which must be the full path of the file to process");
            }
            new SnapshotCreatorMain().doSnapshotCreation(args[0]);
        }
        catch (Throwable t)
        {
            // catch everything to make sure its logged
            LOG.error("Error in SnapshotCreatorMain", t);
            throw new RuntimeException(t);
        }
    }

    private void doSnapshotCreation(String filePath)
    {
        NullStatusUpdater statusUpdater = new NullStatusUpdater();
        FileHandler fileHandler = new SnapshotCreatorHandler(properties, splitter, fileInfoGetter, statusUpdater);
        FileHandler successHandler = new FileMover(properties.getNdpiProcessedDirectory());
        FileHandler failureHandler = new FileMover(properties.getNdpiFailedDirectory());
        SingleFileProcessor processor = new SingleFileProcessor(fileHandler, successHandler, failureHandler);
        processor.processFile(new File(filePath));
    }

    private static void configureEnvironment()
    {
        String currentLibraryPath = System.getProperty(JNA_LIBRARY_PATH_PROPERTY);
        if (currentLibraryPath == null)
        {
            currentLibraryPath = "";
        }
        LOG.info("Current jna.library.path " + currentLibraryPath);
        String workingDirectory = System.getProperty("user.dir");
        LOG.info("Working directory " + workingDirectory);

        // add these in just in case it helps
        String extraPaths = "/windows/system32;/ndpisplitter/dll";
        // add the current working directory/dll which should be correct
        String workingDirectoryPath = workingDirectory + File.separator + "dll";
        // include the current library path (if set), working directory, plus extras
        String newLibraryPath = currentLibraryPath + ";" + workingDirectoryPath + ";" + extraPaths;

        System.setProperty(JNA_LIBRARY_PATH_PROPERTY, newLibraryPath);
        LOG.info("New jna.library.path " + System.getProperty(JNA_LIBRARY_PATH_PROPERTY));
    }

    private static Properties loadProperties() throws IOException
    {
        Properties properties = new Properties();
        InputStream propertiesStream = SnapshotCreatorMain.class.getResourceAsStream("/snapshot-creator.properties");
        if (propertiesStream == null)
        {
            throw new IllegalStateException("Could not find properties file snapshot-creator.properties");
        }
        properties.load(propertiesStream);
        return properties;
    }

}

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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import au.org.intersect.snapshotcreator.caisis.CaisisDao;
import au.org.intersect.snapshotcreator.caisis.CaisisUpdaterHandler;
import au.org.intersect.snapshotcreator.caisis.TissueImageNameParser;
import au.org.intersect.snapshotcreator.config.SnapshotCreatorProperties;
import au.org.intersect.snapshotcreator.file.DirectoryBasedMultipleFileProcessor;
import au.org.intersect.snapshotcreator.file.FileHandler;
import au.org.intersect.snapshotcreator.file.FileMover;
import au.org.intersect.snapshotcreator.file.NullHandler;

/**
 * @version $Rev$
 */
public class CaisisUpdaterMain
{
    private static final Logger LOG = Logger.getLogger(CaisisUpdaterMain.class);

    private SnapshotCreatorProperties properties;

    private CaisisDao caisisDao;

    public CaisisUpdaterMain() throws IOException
    {
        super();
        LOG.info("--------------CaisisUpdaterMain starting-----------------");
        Properties propertiesFile = loadProperties();
        properties = new SnapshotCreatorProperties(propertiesFile);
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("app-context.xml");
        caisisDao = appContext.getBean("caisisDao", CaisisDao.class);
    }

    public static void main(String[] args) throws IOException
    {
        try
        {
            new CaisisUpdaterMain().updateCaisis();
        }
        catch (Throwable t)
        {
            // catch everything to make sure it gets logged
            LOG.error("Error in CaisisUpdaterMain", t);
            throw new RuntimeException(t);
        }
    }

    private void updateCaisis()
    {
        LOG.info("Preparing to update caisis for files in " + properties.getSnapshotsPublishingDirectory());
        FileHandler fileHandler = new CaisisUpdaterHandler(properties, caisisDao, new TissueImageNameParser());
        FileHandler successHandler = new NullHandler();
        FileHandler failureHandler = new FileMover(properties.getSnapshotsFailedDirectory());
        FilenameFilter filenameFilter = new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".jpg");
            }
        };
        DirectoryBasedMultipleFileProcessor processor = new DirectoryBasedMultipleFileProcessor(properties
                .getSnapshotsPublishingDirectory(), successHandler, failureHandler, fileHandler, filenameFilter);
        processor.processFiles();
    }

    private static Properties loadProperties() throws IOException
    {
        Properties properties = new Properties();
        InputStream propertiesStream = CaisisUpdaterMain.class.getResourceAsStream("/snapshot-creator.properties");
        if (propertiesStream == null)
        {
            throw new IllegalStateException("Could not find properties file snapshot-creator.properties");
        }
        properties.load(propertiesStream);
        return properties;
    }

}

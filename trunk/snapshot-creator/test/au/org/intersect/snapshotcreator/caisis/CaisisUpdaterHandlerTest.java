/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.caisis;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.org.intersect.snapshotcreator.config.SnapshotCreatorProperties;

/**
 * 
 * @version $Rev$
 */
public class CaisisUpdaterHandlerTest
{
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Mock
    private CaisisDao dao;

    @Mock
    private TissueImageNameParser nameParser;

    private Properties props;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        props = new Properties();
        props.put("ndpi.new.directory", "ndpi_new");
        props.put("ndpi.processed.directory", "ndpi_processed");
        props.put("ndpi.failed.directory", "ndpi_failed");
        props.put("snapshots.holding.directory", "snap_holding");
        props.put("snapshots.failed.directory", "snap_failed");
        props.put("snapshots.publishing.directory", "snap_publishing");
        props.put("image.name.pattern", "image_url/${fileName}");
        props.put("output.magnification", "5");
        props.put("output.jpeg.quality", "0.95");
        props.put("caisis.update.sql", "query_string");
        props.put("check.at.least.one.row.updated", "true");
        props.put("max.pixels.per.snapshot", "50000");
        props.put("watermark.transparency", "0.8");
        props.put("watermark.relative.x", "0.9");
        props.put("watermark.relative.y", "0.1");
        props.put("watermark.relative.width", "0.3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsInvalidURLPatter()
    {
        props.put("image.name.pattern", "this is not valid ${notValid}");
        new CaisisUpdaterHandler(new SnapshotCreatorProperties(props), dao, nameParser);
    }

    @Test
    public void testSetsQuerySqlAndCheckBooleanCorrectly()
    {
        new CaisisUpdaterHandler(new SnapshotCreatorProperties(props), dao, nameParser);
        verify(dao).setUpdateSql("query_string");
        verify(dao).setCheckAtLeastOneRowUpdated(true);
    }

    @Test(expected = CaisisUpdateFailedException.class)
    public void testFailsIfFileNameNotInCorrectFormat() throws IOException
    {
        when(nameParser.getTissueBankRefAndSpecimenId("blah.jpg")).thenThrow(
                new CaisisUpdateFailedException("some msg"));
        CaisisUpdaterHandler handler = new CaisisUpdaterHandler(new SnapshotCreatorProperties(props), dao, nameParser);
        handler.handleFile(folder.newFile("blah.jpg"));
    }

    @Test
    public void testHandlesFileCorrectly() throws IOException
    {
        CaisisUpdaterHandler handler = new CaisisUpdaterHandler(new SnapshotCreatorProperties(props), dao, nameParser);
        when(nameParser.getTissueBankRefAndSpecimenId("abc.def.jpg")).thenReturn(new String[] {"abc", "def"});
        handler.handleFile(folder.newFile("abc.def.jpg"));
        verify(dao).updateImageURL("abc", "def", "image_url/abc.def");
    }

}

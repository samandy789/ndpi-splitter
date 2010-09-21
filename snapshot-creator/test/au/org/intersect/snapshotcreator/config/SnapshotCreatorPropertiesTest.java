/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @version $Rev$
 */
public class SnapshotCreatorPropertiesTest
{
    Properties props;

    @Before
    public void setUp()
    {
        createValidPropertyValues();
    }

    @Test
    public void testReadsPropertiesCorrectly()
    {
        SnapshotCreatorProperties properties = new SnapshotCreatorProperties(props);
        assertEquals("ndpi_new", properties.getNdpiNewDirectory());
        assertEquals("snap_holding", properties.getSnapshotsWorkDirectory());
        assertEquals("ndpi_processed", properties.getNdpiProcessedDirectory());
        assertEquals("ndpi_failed", properties.getNdpiFailedDirectory());
        assertEquals("snap_failed", properties.getSnapshotsFailedDirectory());
        assertEquals("snap_publishing", properties.getSnapshotsPublishingDirectory());
        assertEquals("image_url", properties.getImageUrlPattern());
        assertEquals("query_string", properties.getCaisisUpdateSql());
        assertEquals(5, properties.getOutputMagnification());
        assertEquals(4500000, properties.getMaxPixelsPerSnapshot());
        assertEquals(0.95F, properties.getJpegQuality(), 0.01);
        assertEquals(0.8F, properties.getWatermarkProperties().getTransparency(), 0.01);
        assertEquals(0.9F, properties.getWatermarkProperties().getRelativeXPosition(), 0.01);
        assertEquals(0.1F, properties.getWatermarkProperties().getRelativeYPosition(), 0.01);
        assertEquals(0.1F, properties.getWatermarkProperties().getRelativeWidth(), 0.3);
        assertTrue(properties.isShouldCheckAtLeastOneRowUpdated());
    }

    @Test
    public void testFailsIfAnyPropertyIsMissing()
    {
        assertFailsWhenPropertyMissing("ndpi.new.directory");
        assertFailsWhenPropertyMissing("ndpi.processed.directory");
        assertFailsWhenPropertyMissing("ndpi.failed.directory");
        assertFailsWhenPropertyMissing("snapshots.holding.directory");
        assertFailsWhenPropertyMissing("snapshots.failed.directory");
        assertFailsWhenPropertyMissing("snapshots.holding.directory");
        assertFailsWhenPropertyMissing("output.magnification");
        assertFailsWhenPropertyMissing("output.jpeg.quality");
        assertFailsWhenPropertyMissing("image.name.pattern");
        assertFailsWhenPropertyMissing("caisis.update.sql");
        assertFailsWhenPropertyMissing("check.at.least.one.row.updated");
        assertFailsWhenPropertyMissing("max.pixels.per.snapshot");
        assertFailsWhenPropertyMissing("watermark.relative.x");
        assertFailsWhenPropertyMissing("watermark.relative.y");
        assertFailsWhenPropertyMissing("watermark.transparency");
        assertFailsWhenPropertyMissing("watermark.relative.width");
    }

    @Test
    public void testFailsIfAnyPropertyIsBlank()
    {
        assertFailsWhenPropertyBlank("ndpi.new.directory");
        assertFailsWhenPropertyBlank("ndpi.processed.directory");
        assertFailsWhenPropertyBlank("ndpi.failed.directory");
        assertFailsWhenPropertyBlank("snapshots.holding.directory");
        assertFailsWhenPropertyBlank("snapshots.failed.directory");
        assertFailsWhenPropertyBlank("snapshots.holding.directory");
        assertFailsWhenPropertyBlank("output.magnification");
        assertFailsWhenPropertyBlank("output.jpeg.quality");
        assertFailsWhenPropertyBlank("image.name.pattern");
        assertFailsWhenPropertyBlank("caisis.update.sql");
        assertFailsWhenPropertyBlank("check.at.least.one.row.updated");
        assertFailsWhenPropertyBlank("max.pixels.per.snapshot");
        assertFailsWhenPropertyBlank("watermark.relative.x");
        assertFailsWhenPropertyBlank("watermark.relative.y");
        assertFailsWhenPropertyBlank("watermark.transparency");
        assertFailsWhenPropertyBlank("watermark.relative.width");
    }

    @Test
    public void testFailsIfIntegerValueIsNotFormattedCorrectly()
    {
        assertFailsForBadFormat("output.magnification", "asdf");
        assertFailsForBadFormat("output.magnification", " ");
        assertFailsForBadFormat("output.magnification", "1.1");
        assertFailsForBadFormat("max.pixels.per.snapshot", "asdf");
        assertFailsForBadFormat("max.pixels.per.snapshot", " ");
        assertFailsForBadFormat("max.pixels.per.snapshot", "1.1");
    }

    @Test
    public void testFailsIfFloatIsNotFormattedCorrectly()
    {
        assertFailsForBadFormat("output.jpeg.quality", "asdf");
        assertFailsForBadFormat("output.jpeg.quality", " ");
        assertFailsForBadFormat("output.jpeg.quality", "1.1aa");

    }

    @Test
    public void testReadsBooleanCorrectly()
    {
        props.setProperty("check.at.least.one.row.updated", "false");
        SnapshotCreatorProperties properties = new SnapshotCreatorProperties(props);
        assertFalse(properties.isShouldCheckAtLeastOneRowUpdated());

        props.setProperty("check.at.least.one.row.updated", "blah");
        properties = new SnapshotCreatorProperties(props);
        assertFalse(properties.isShouldCheckAtLeastOneRowUpdated());

        props.setProperty("check.at.least.one.row.updated", "True");
        properties = new SnapshotCreatorProperties(props);
        assertTrue(properties.isShouldCheckAtLeastOneRowUpdated());
    }

    private void assertFailsForBadFormat(String key, String value)
    {
        createValidPropertyValues();
        props.put(key, value);
        try
        {
            new SnapshotCreatorProperties(props);
            fail("Should fail if a property is incorrecrly formatted");
        }
        catch (SnapshotPropertiesException e)
        {
            assertTrue(e.getMessage().contains(key));
            assertTrue(e.getMessage().contains(value));
            assertTrue(e.getMessage().contains("Could not convert"));
        }
    }

    private void assertFailsWhenPropertyMissing(String key)
    {
        createValidPropertyValues();
        props.remove(key);
        try
        {
            new SnapshotCreatorProperties(props);
            fail("Should fail if a property is missing");
        }
        catch (SnapshotPropertiesException e)
        {
            assertTrue(e.getMessage().contains(key));
        }
    }

    private void assertFailsWhenPropertyBlank(String key)
    {
        createValidPropertyValues();
        props.put(key, "");
        try
        {
            new SnapshotCreatorProperties(props);
            fail("Should fail if a property is missing");
        }
        catch (SnapshotPropertiesException e)
        {
            assertTrue(e.getMessage().contains(key));
        }
    }

    private void createValidPropertyValues()
    {
        props = new Properties();
        props.put("ndpi.new.directory", "ndpi_new");
        props.put("ndpi.processed.directory", "ndpi_processed");
        props.put("ndpi.failed.directory", "ndpi_failed");
        props.put("snapshots.holding.directory", "snap_holding");
        props.put("snapshots.failed.directory", "snap_failed");
        props.put("snapshots.publishing.directory", "snap_publishing");
        props.put("image.name.pattern", "image_url");
        props.put("output.magnification", "5");
        props.put("output.jpeg.quality", "0.95");
        props.put("caisis.update.sql", "query_string");
        props.put("check.at.least.one.row.updated", "true");
        props.put("max.pixels.per.snapshot", "4500000");
        props.put("watermark.transparency", "0.8");
        props.put("watermark.relative.x", "0.9");
        props.put("watermark.relative.y", "0.1");
        props.put("watermark.relative.width", "0.3");
    }
}

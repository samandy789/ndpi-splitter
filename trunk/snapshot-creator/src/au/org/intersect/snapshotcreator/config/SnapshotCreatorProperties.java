/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.config;

import java.util.Properties;

import au.org.intersect.snapshotcreator.imaging.WatermarkProperties;

/**
 * 
 * @version $Rev$
 */
public class SnapshotCreatorProperties
{

    private String ndpiNewDirectory;
    private String ndpiProcessedDirectory;
    private String ndpiFailedDirectory;
    private String snapshotsWorkDirectory;
    private String snapshotsPublishingDirectory;
    private String snapshotsFailedDirectory;
    private String imageUrlPattern;
    private String caisisUpdateSql;
    private int outputMagnification;
    private int maxPixelsPerSnapshot;
    private float jpegQuality;
    private float watermarkRelativeY;
    private float watermarkRelativeX;
    private float watermarkTransparency;
    private float watermarkRelativeWidth;

    private boolean shouldCheckAtLeastOneRowUpdated;

    public SnapshotCreatorProperties(Properties properties)
    {
        ndpiNewDirectory = getStringProperty(properties, "ndpi.new.directory");
        ndpiProcessedDirectory = getStringProperty(properties, "ndpi.processed.directory");
        ndpiFailedDirectory = getStringProperty(properties, "ndpi.failed.directory");

        snapshotsWorkDirectory = getStringProperty(properties, "snapshots.holding.directory");
        snapshotsFailedDirectory = getStringProperty(properties, "snapshots.failed.directory");
        snapshotsPublishingDirectory = getStringProperty(properties, "snapshots.publishing.directory");

        imageUrlPattern = getStringProperty(properties, "image.name.pattern");
        caisisUpdateSql = getStringProperty(properties, "caisis.update.sql");

        outputMagnification = getIntProperty(properties, "output.magnification");
        maxPixelsPerSnapshot = getIntProperty(properties, "max.pixels.per.snapshot");
        jpegQuality = getFloatProperty(properties, "output.jpeg.quality");

        watermarkRelativeY = getFloatProperty(properties, "watermark.relative.y");
        watermarkRelativeX = getFloatProperty(properties, "watermark.relative.x");
        watermarkTransparency = getFloatProperty(properties, "watermark.transparency");
        watermarkRelativeWidth = getFloatProperty(properties, "watermark.relative.width");

        shouldCheckAtLeastOneRowUpdated = getBooleanProperty(properties, "check.at.least.one.row.updated");
    }

    public String getNdpiNewDirectory()
    {
        return ndpiNewDirectory;
    }

    public String getNdpiProcessedDirectory()
    {
        return ndpiProcessedDirectory;
    }

    public String getNdpiFailedDirectory()
    {
        return ndpiFailedDirectory;
    }

    public String getSnapshotsWorkDirectory()
    {
        return snapshotsWorkDirectory;
    }

    public String getSnapshotsPublishingDirectory()
    {
        return snapshotsPublishingDirectory;
    }

    public String getSnapshotsFailedDirectory()
    {
        return snapshotsFailedDirectory;
    }

    public String getImageUrlPattern()
    {
        return imageUrlPattern;
    }

    public int getOutputMagnification()
    {
        return outputMagnification;
    }

    public float getJpegQuality()
    {
        return jpegQuality;
    }

    public String getCaisisUpdateSql()
    {
        return caisisUpdateSql;
    }

    public boolean isShouldCheckAtLeastOneRowUpdated()
    {
        return shouldCheckAtLeastOneRowUpdated;
    }

    public int getMaxPixelsPerSnapshot()
    {
        return maxPixelsPerSnapshot;
    }

    public WatermarkProperties getWatermarkProperties()
    {
        return new WatermarkProperties(watermarkTransparency, watermarkRelativeX, watermarkRelativeY,
                watermarkRelativeWidth);
    }

    private String getProperty(Properties properties, String key)
    {
        String value = properties.getProperty(key);
        if (value == null || value.length() == 0)
        {
            throw new SnapshotPropertiesException("Expected to find property with key [" + key
                    + "] in properties file, but it wasn't found.");
        }
        return value;
    }

    private String getStringProperty(Properties properties, String key)
    {
        String value = getProperty(properties, key);
        return value;
    }

    private int getIntProperty(Properties properties, String key)
    {
        String property = getProperty(properties, key);
        try
        {
            return Integer.parseInt(property);
        }
        catch (NumberFormatException nfe)
        {
            throw new SnapshotPropertiesException("Could not convert [" + key
                    + "] property to integer, property value was " + property);
        }
    }

    private float getFloatProperty(Properties properties, String key)
    {
        String property = getProperty(properties, key);
        try
        {
            return Float.parseFloat(property);
        }
        catch (NumberFormatException nfe)
        {
            throw new SnapshotPropertiesException("Could not convert [" + key
                    + "] property to float, property value was " + property);
        }
    }

    private boolean getBooleanProperty(Properties properties, String key)
    {
        String property = getProperty(properties, key);
        return Boolean.parseBoolean(property);
    }
}

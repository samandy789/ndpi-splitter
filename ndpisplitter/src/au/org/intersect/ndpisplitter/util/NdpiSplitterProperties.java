/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.util;

import java.util.Properties;

/**
 * 
 * @version $Rev$
 */
public class NdpiSplitterProperties
{
    private static final String EMPTY_DIRECTORY_NAME_PROPERTY = "empty.directory.name";
    private static final String OUTPUT_FILE_TYPE_PROPERTY = "output.file.type";
    private static final String DEFAULT_EMPTY_TILE_ALGORITHM_PROPERTY = "default.empty.tile.algorithm";
    private static final String EMPTY_TILE_ALGORITHM_LIST_PROPERTY = "empty.tile.algorithms";
    private static final String TILE_MAGNIFICATION_LIST_PROPERTY = "tile.magnification.list";
    private static final String DEFAULT_TILE_HEIGHT_PROPERTY = "default.tile.height";
    private static final String DEFAULT_TILE_WIDTH_PROPERTY = "default.tile.width";
    private static final String INTENSITY_ALGORITHM_WHITENESS_INTENSITY = "intensity.algorithm.whitness.intensity";
    private static final String INTENSITY_ALGORITHM_THRESHOLD_INTENSITY = "intensity.algorithm.threshold.intensity";
    private static final String INTENSITY_ALGORITHM_INTERESTING_THRESHOLD = "intensity.algorithm.interesting.threshold";
    private static final String COMPRESSION_ALGORITHM_INTERESTING_THRESHOLD 
        = "compression.algorithm.interesting.threshold";

    private String[] magnificationList;
    private String defaultHeight;
    private String defaultWidth;
    private String[] emptyTileAlgorithmList;
    private String defaultEmptyTileAlgorithm;
    private String outputFileType;
    private String emptyDirectoryName;
    private int intensityAlgorithmWhitenessIntensity;
    private int intensityAlgorithmThresholdIntensity;
    private double intensityAlgorithmInterestingThreshold;
    private double compressionAlgorithmInterestingThreshold;

    public NdpiSplitterProperties(Properties properties) throws NdpiSplitterPropertiesException
    {
        this.defaultWidth = getProperty(properties, DEFAULT_TILE_WIDTH_PROPERTY);
        this.defaultHeight = getProperty(properties, DEFAULT_TILE_HEIGHT_PROPERTY);
        String magnificationListProp = getProperty(properties, TILE_MAGNIFICATION_LIST_PROPERTY);
        this.magnificationList = magnificationListProp.split(",");
        String emptyTileAlgorithmListProp = getProperty(properties, EMPTY_TILE_ALGORITHM_LIST_PROPERTY);
        this.emptyTileAlgorithmList = emptyTileAlgorithmListProp.split(",");
        this.defaultEmptyTileAlgorithm = getProperty(properties, DEFAULT_EMPTY_TILE_ALGORITHM_PROPERTY);
        this.outputFileType = getProperty(properties, OUTPUT_FILE_TYPE_PROPERTY);
        this.emptyDirectoryName = getProperty(properties, EMPTY_DIRECTORY_NAME_PROPERTY);
        this.intensityAlgorithmWhitenessIntensity = getIntProperty(properties, INTENSITY_ALGORITHM_WHITENESS_INTENSITY);
        this.intensityAlgorithmThresholdIntensity = getIntProperty(properties, INTENSITY_ALGORITHM_THRESHOLD_INTENSITY);
        this.intensityAlgorithmInterestingThreshold = getDoubleProperty(properties,
                INTENSITY_ALGORITHM_INTERESTING_THRESHOLD);
        this.compressionAlgorithmInterestingThreshold = getDoubleProperty(properties,
                COMPRESSION_ALGORITHM_INTERESTING_THRESHOLD);
    }

    public String getDefaultHeight()
    {
        return defaultHeight;
    }

    public String getDefaultWidth()
    {
        return defaultWidth;
    }

    public String[] getMagnificationList()
    {
        return magnificationList;
    }

    public String[] getEmptyTileAlgorithmList()
    {
        return emptyTileAlgorithmList;
    }

    public String getDefaultEmptyTileAlgorithm()
    {
        return this.defaultEmptyTileAlgorithm;
    }

    public String getOutputFileType()
    {
        return outputFileType;
    }

    public String getEmptyDirectoryName()
    {
        return emptyDirectoryName;
    }

    public int getIntensityAlgorithmWhitenessIntensity()
    {
        return intensityAlgorithmWhitenessIntensity;
    }

    public int getIntensityAlgorithmThresholdIntensity()
    {
        return intensityAlgorithmThresholdIntensity;
    }

    public double getIntensityAlgorithmInterestingThreshold()
    {
        return intensityAlgorithmInterestingThreshold;
    }

    public double getCompressionAlgorithmInterestingThreshold()
    {
        return compressionAlgorithmInterestingThreshold;
    }

    public void setCompressionAlgorithmInterestingThreshold(double compressionAlgorithmInterestingThreshold)
    {
        this.compressionAlgorithmInterestingThreshold = compressionAlgorithmInterestingThreshold;
    }

    private int getIntProperty(Properties properties, String key) throws NdpiSplitterPropertiesException
    {
        String property = getProperty(properties, key);
        try
        {
            return Integer.parseInt(property);
        }
        catch (NumberFormatException nfe)
        {
            throw new NdpiSplitterPropertiesException("Could not convert [" + key
                    + "] property to integer, property value was " + property);
        }
    }

    private double getDoubleProperty(Properties properties, String key) throws NdpiSplitterPropertiesException
    {
        String property = getProperty(properties, key);
        try
        {
            return Double.parseDouble(property);
        }
        catch (NumberFormatException nfe)
        {
            throw new NdpiSplitterPropertiesException("Could not convert [" + key
                    + "] property to double, property value was " + property);
        }
    }

    private String getProperty(Properties properties, String key) throws NdpiSplitterPropertiesException
    {
        String value = properties.getProperty(key);
        if (value == null || value.length() == 0)
        {
            throw new NdpiSplitterPropertiesException("Expected to find property with key [" + key
                    + "] in properties file, but it wasn't found.");
        }
        return value;
    }

}

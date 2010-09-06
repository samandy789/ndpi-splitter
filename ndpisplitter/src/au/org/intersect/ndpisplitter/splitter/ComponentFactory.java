/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.splitter;

import au.org.intersect.ndpisplitter.file.FileManager;
import au.org.intersect.ndpisplitter.imaging.FilterEmptyImagesImageCreator;
import au.org.intersect.ndpisplitter.imaging.ImageCreator;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadFactory;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadWrapper;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadWrapperImpl;
import au.org.intersect.ndpisplitter.processing.CompressionTileAnalyser;
import au.org.intersect.ndpisplitter.processing.NullTileAnalyser;
import au.org.intersect.ndpisplitter.processing.PixelIntensityTileAnalyser;
import au.org.intersect.ndpisplitter.processing.TileAnalyser;
import au.org.intersect.ndpisplitter.util.NdpiSplitterProperties;

/**
 * Since we're not using Spring, this provides a rudimentary means of getting a fully wired up object without having to
 * do it manually each time.
 * 
 * @version $Rev$
 */
public class ComponentFactory
{
    private static final String NO_ALGORITHM = "None";
    private static final String COMPRESSION_ALGORITHM = "Compression";
    private static final String INTENSITY_ALGORITHM = "Intensity";

    /**
     * Get a NdpiFileSplitter with all its dependencies wired up for the NDPISplitter application. Since the application
     * is too small to use Spring, this is our depedency wiring.
     * 
     * @return a NdpiFileSplitter
     */
    public static NdpiFileSplitter getNdpiFileSplitterInstance(String emptyTilesAlgorithm,
            NdpiSplitterProperties properties)
    {
        String fileType = properties.getOutputFileType();
        FileManager fileManager = new FileManager();
        TileAnalyser analyser = getTileAnalyser(emptyTilesAlgorithm, properties);
        ImageCreator imageCreator = new FilterEmptyImagesImageCreator(fileType, analyser, properties
                .getEmptyDirectoryName(), fileManager);

        NDPReadWrapper wrapper = new NDPReadWrapperImpl(NDPReadFactory.INSTANCE, fileManager);

        XYIndexBasedFileNamer tileFileNamer = new XYIndexBasedFileNamer(new SequenceCodeGeneratorImpl(), fileType);

        NdpiFileSplitter splitter = new NdpiFileSplitter(wrapper, imageCreator, tileFileNamer, fileManager);
        return splitter;
    }

    private static TileAnalyser getTileAnalyser(String emptyTilesAlgorithm, NdpiSplitterProperties properties)
    {
        if (emptyTilesAlgorithm == null || emptyTilesAlgorithm.isEmpty()
                || emptyTilesAlgorithm.startsWith(NO_ALGORITHM))
        {
            return new NullTileAnalyser();
        }
        else if (emptyTilesAlgorithm.startsWith(INTENSITY_ALGORITHM))
        {
            return new PixelIntensityTileAnalyser(properties.getIntensityAlgorithmThresholdIntensity(), properties
                    .getIntensityAlgorithmWhitenessIntensity(), properties.getIntensityAlgorithmInterestingThreshold());

        }
        else if (emptyTilesAlgorithm.startsWith(COMPRESSION_ALGORITHM))
        {
            return new CompressionTileAnalyser(properties.getCompressionAlgorithmInterestingThreshold());
        }
        else
        {
            throw new IllegalArgumentException("Unsupported algorithm " + emptyTilesAlgorithm);
        }
    }

    /**
     * Get a NdpiFileInfoGetter with all its dependencies wired up for the NDPISplitter application
     * 
     * @return a NdpiFileInfoGetter
     */
    public static NdpiFileInfoGetter getNdpiFileInfoGetterInstance()
    {
        FileManager fileManager = new FileManager();
        NDPReadWrapper wrapper = new NDPReadWrapperImpl(NDPReadFactory.INSTANCE, fileManager);
        NdpiFileInfoGetter infoGetter = new NdpiFileInfoGetter(wrapper);
        return infoGetter;
    }

}

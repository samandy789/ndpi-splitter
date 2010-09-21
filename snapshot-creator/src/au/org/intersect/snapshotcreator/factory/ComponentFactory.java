/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.factory;

import au.org.intersect.ndpisplitter.file.FileManager;
import au.org.intersect.ndpisplitter.imaging.JpegImageCreator;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadFactory;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadWrapper;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadWrapperImpl;
import au.org.intersect.ndpisplitter.splitter.NdpiFileInfoGetter;
import au.org.intersect.ndpisplitter.splitter.NdpiFileSplitter;
import au.org.intersect.snapshotcreator.config.SnapshotCreatorProperties;
import au.org.intersect.snapshotcreator.imaging.WatermarkingImageCreator;
import au.org.intersect.snapshotcreator.snapshot.SnapshotFileNamer;

/**
 * Since we're not using Spring, this provides a rudimentary means of getting a fully wired up object without having to
 * do it manually each time.
 * 
 * @version $Rev$
 */
public class ComponentFactory
{
    /**
     * Get a NdpiFileSplitter with all its dependencies wired up for the SnapshotCreator application
     * 
     * @return a NdpiFileSplitter
     */
    public static NdpiFileSplitter getNdpiFileSplitterInstance(SnapshotCreatorProperties properties)
    {
        // since we're not using Spring, this is a way to get a fully wired up NdpiFileSplitter

        FileManager fileManager = new FileManager();
        JpegImageCreator imageCreator = new WatermarkingImageCreator(properties.getJpegQuality(), properties
                .getWatermarkProperties());
        String fileType = imageCreator.getFileType();

        NDPReadWrapper wrapper = new NDPReadWrapperImpl(NDPReadFactory.INSTANCE, fileManager);
        SnapshotFileNamer tileFileNamer = new SnapshotFileNamer(fileType);

        NdpiFileSplitter splitter = new NdpiFileSplitter(wrapper, imageCreator, tileFileNamer, fileManager);
        return splitter;
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

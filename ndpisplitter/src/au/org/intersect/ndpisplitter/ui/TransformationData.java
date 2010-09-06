/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */

package au.org.intersect.ndpisplitter.ui;

import java.io.File;
import java.util.List;
import java.util.Map;

import au.org.intersect.ndpisplitter.splitter.ImageTilingException;

/**
 * Interface to reveal the data the user entered in the UI
 * 
 * @version $Rev$
 * 
 */
public interface TransformationData
{
    /**
     * Map with the list of files and their destination directory
     * 
     * @return key - source file, value - destination directory
     */
    Map<File, File> getOriginAndDestinationFiles();

    /**
     * Add a file to transform
     * 
     * @param origin
     *            ndpi file
     * @param destination
     *            destination directory
     * @return
     */
    boolean addFile(File origin, File destination);

    /**
     * Remove a file from the list
     * 
     * @param fileName name of the file
     * @param outputDirectory  corresponding output directory
     */
    void deleteFile(String fileName, String outputDirectory);
    
    /**
     * @return Tile width
     */
    int getTileWidth();

    /**
     * @return Tile Height
     */
    int getTileHeight();

    /**
     * @return image magnification
     */
    int getImageMagnification();

    String getEmptyTilesAlgorithm();

    /**
     * @param tileHeight
     */
    void setTileHeight(int tileHeight);

    /**
     * @param tileWidth
     */
    void setTileWidth(int tileWidth);

    /**
     * @param imageMagnification
     */
    void setImageMagnification(int imageMagnification);

    void setEmptyTilesAlgorithm(String emptyTilesAlgorithm);

    /*
     * Indicates is image transformation has finished
     */
    boolean isTransformationFinished();

    /*
     * Sets the status of the transformation (finished or not)
     */
    void setTransformationFinished(boolean finished);

    /*
     * Add an error in tiling processing
     */
    void addTilingError(File errorFile, ImageTilingException ite);

    /*
     * Add success in tiling processing
     */
    void addTilingSuccess(File errorFile);

    /**
     * Tells callers if there's output directories which already exist in the transformation data
     */
    boolean shouldWarnAboutExistingDirectories();

    Map<File, ImageTilingException> getTilingErrorMap();

    List<File> getTilingSuccessList();

}

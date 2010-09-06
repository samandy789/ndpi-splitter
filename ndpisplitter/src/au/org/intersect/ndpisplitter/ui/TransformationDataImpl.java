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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import au.org.intersect.ndpisplitter.splitter.ImageTilingException;

/**
 * Data to carry information across wizard pages
 * 
 * @version $Rev$
 * 
 */
public class TransformationDataImpl implements TransformationData
{
    /**
     * Map of origin/destination files
     */
    private Map<File, File> fileMap = new HashMap<File, File>();

    private int tileHeight;
    private int tileWidth;
    private int imageMagnification;

    private boolean transformationFinished;

    private Map<File, ImageTilingException> tilingErrorMap = new HashMap<File, ImageTilingException>();
    private List<File> tilingSuccessList = new ArrayList<File>();

    private String emptyTilesAlgorithm;

    /**
     * 
     */
    public TransformationDataImpl()
    {
        transformationFinished = false;
    }

    @Override
    public Map<File, File> getOriginAndDestinationFiles()
    {
        return fileMap;
    }

    @Override
    public boolean addFile(File origin, File destination)
    {
        if (fileMap.keySet().contains(origin))
        {
            return false;
        }

        fileMap.put(origin, destination);
        return true;
    }

    @Override
    public void deleteFile(String fileName, String outputDirectory)
    {
        // since we only display the name (not path) of the source file, we have to do some smart matching to figure out
        // which to remove here
        File keyToRemove = null;
        for (Entry<File, File> item : fileMap.entrySet())
        {
            File sourceFile = item.getKey();
            if (sourceFile.getName().equals(fileName) && item.getValue().getAbsolutePath().equals(outputDirectory))
            {
                keyToRemove = sourceFile;
            }
        }
        if (keyToRemove != null)
        {
            fileMap.remove(keyToRemove);
        }
    }

    @Override
    public int getImageMagnification()
    {
        return imageMagnification;
    }

    @Override
    public int getTileHeight()
    {
        return tileHeight;
    }

    @Override
    public int getTileWidth()
    {
        return tileWidth;
    }

    @Override
    public String getEmptyTilesAlgorithm()
    {
        return emptyTilesAlgorithm;
    }

    @Override
    public void setTileHeight(int tileHeight)
    {
        this.tileHeight = tileHeight;
    }

    @Override
    public void setTileWidth(int tileWidth)
    {
        this.tileWidth = tileWidth;
    }

    @Override
    public void setImageMagnification(int imageMagnification)
    {
        this.imageMagnification = imageMagnification;
    }

    @Override
    public void setEmptyTilesAlgorithm(String emptyTilesAlgorithm)
    {
        this.emptyTilesAlgorithm = emptyTilesAlgorithm;
    }

    @Override
    public boolean isTransformationFinished()
    {
        return transformationFinished;
    }

    @Override
    public void setTransformationFinished(boolean finished)
    {
        transformationFinished = finished;
    }

    /*
     * Add an error in tiling processing
     */
    @Override
    public void addTilingError(File errorFile, ImageTilingException ite)
    {
        tilingErrorMap.put(errorFile, ite);
    }

    /*
     * Add success in tiling processing
     */
    @Override
    public void addTilingSuccess(File file)
    {
        tilingSuccessList.add(file);
    }

    @Override
    public boolean shouldWarnAboutExistingDirectories()
    {
        Map<File, File> filesToProcess = getOriginAndDestinationFiles();
        for (File destDir : filesToProcess.values())
        {
            if (destDir.exists())
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<File, ImageTilingException> getTilingErrorMap()
    {
        return tilingErrorMap;
    }

    @Override
    public List<File> getTilingSuccessList()
    {
        return tilingSuccessList;
    }

}

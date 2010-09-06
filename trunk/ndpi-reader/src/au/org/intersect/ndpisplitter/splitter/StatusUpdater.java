/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.splitter;

/**
 * 
 * @version $Rev$
 */
public interface StatusUpdater
{

    /**
     * Notifies the object about the number of completed tiles
     * 
     * @param tilesCompleted
     *            the number of tiles that are done
     */
    void setNumberOfTilesCompleted(int tilesCompleted);

    /**
     * Notifies the object about the total number of tiles to be created
     * 
     * @param totalNumberOfTiles
     *            the total number of tiles to be created
     */
    void setNumberOfTiles(int totalNumberOfTiles);

}

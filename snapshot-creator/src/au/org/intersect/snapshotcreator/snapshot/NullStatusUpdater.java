/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.snapshot;

import au.org.intersect.ndpisplitter.splitter.StatusUpdater;

/**
 * @version $Rev$
 * 
 */
public class NullStatusUpdater implements StatusUpdater
{
    @Override
    public void setNumberOfTiles(int totalNumberOfTiles)
    {
        // do nothing
    }

    @Override
    public void setNumberOfTilesCompleted(int tilesCompleted)
    {
        // do nothing
    }
}

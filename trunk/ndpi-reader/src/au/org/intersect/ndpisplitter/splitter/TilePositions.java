/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.splitter;

import java.util.List;

/**
 * @version $Rev$
 * 
 */
public interface TilePositions
{
    public List<Long> getTileXPositions();

    public List<Long> getTileYPositions();

    public int getTileWidthInPixels();

    public int getTileHeightInPixels();

    public int getTotalNumberOfTiles();

}

/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.ui;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * 
 * @version $Rev$
 */
public class TransformationDataImplTest
{
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private File file1;
    private File file2;
    private File sameAs1ButDifferentFolder;
    private File subDir;
    private File file1Tiles;
    private File file2Tiles;
    private File sameAs1ButDifferentFolderTiles;

    @Before
    public void setUp() throws IOException
    {
        file1 = folder.newFile("my-file.ndpi");
        file1Tiles = folder.newFolder("my-file_tiles");
        file2 = folder.newFile("another.ndpi");
        file2Tiles = folder.newFolder("another_tiles");
        subDir = folder.newFolder("subdir");

        sameAs1ButDifferentFolder = new File(subDir.getAbsolutePath() + File.separator + "my-file.ndpi");
        sameAs1ButDifferentFolderTiles = new File(subDir.getAbsolutePath() + File.separator + "my-file_tiles");
        sameAs1ButDifferentFolderTiles.mkdir();
    }

    @Test
    public void testAddAndRemoveFiles()
    {
        TransformationDataImpl transform = new TransformationDataImpl();
        transform.addFile(file1, file1Tiles);
        transform.addFile(file2, file2Tiles);
        transform.addFile(sameAs1ButDifferentFolder, sameAs1ButDifferentFolderTiles);
        
        assertEquals(3, transform.getOriginAndDestinationFiles().size());
        transform.deleteFile("my-file.ndpi", sameAs1ButDifferentFolderTiles.getAbsolutePath());
        transform.deleteFile("another.ndpi", file2Tiles.getAbsolutePath());
        assertEquals(1, transform.getOriginAndDestinationFiles().size());
        Entry<File, File> remainingItem = transform.getOriginAndDestinationFiles().entrySet().iterator().next();
        assertEquals(file1, remainingItem.getKey());
        assertEquals(file1Tiles, remainingItem.getValue());
    }
}

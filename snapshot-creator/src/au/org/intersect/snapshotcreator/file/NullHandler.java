/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.file;

import java.io.File;


/**
 * 
 * @version $Rev$
 */
public class NullHandler implements FileHandler
{

    @Override
    public void handleFile(File file)
    {
        // does nothing
    }

}

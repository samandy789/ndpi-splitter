/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.imaging;

import java.io.IOException;

/**
 *
 * @version $Rev$
 */
public interface ImageCreator
{
    public void createImageFromNdpiBytes(byte[] imageBytes, int pixelWidth, int pixelHeight, String outputFileName,
            String outputDirectory) throws IOException;
}

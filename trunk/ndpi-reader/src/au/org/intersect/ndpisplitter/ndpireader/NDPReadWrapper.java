/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.ndpireader;

import java.io.FileNotFoundException;

/**
 * Wrapper around the NDPRead jna interface that provides a more user friendly API. So far only the methods we actually
 * need are implemented. Further wrapper methods can be added if needed.
 * 
 * 
 * @version $Rev$
 */
public interface NDPReadWrapper
{

    /**
     * Gets the data about the dimensions "centre" of the image
     * 
     * @param fileName
     *            the file to read
     * @return an ImageInformation object with the details about the image
     * @throws FileNotFoundException
     *             if the file does not exist
     * @throws NDPReadException
     *             if something unexpected goes wrong
     */
    public ImageInformation getImageInformation(String fileName) throws FileNotFoundException, NDPReadException;

    /**
     * Gets the bytes representing a portion of the image
     * 
     * @param ndpiFileName
     *            full path of the original NDPI file
     * @param xPositionOfDesiredCentreInNM
     * @param yPositionOfDesiredCentreInNM
     * @param desiredFocalPositionInNM
     * @param desiredMagnification
     * @param desiredPixelWidth
     * @param desiredPixelHeight
     * @return a byte array holding the image data. this is a bottom-up DIB padded to DWORD boundards
     * @throws NDPReadException
     *             if something unexpected goes wrong
     * @throws FileNotFoundException
     *             if the file does not exist
     */
    public byte[] getImageSegment(String ndpiFileName, int xPositionOfDesiredCentreInNM,
            int yPositionOfDesiredCentreInNM, int desiredFocalPositionInNM, float desiredMagnification,
            int desiredPixelWidth, int desiredPixelHeight) throws NDPReadException, FileNotFoundException;

    public float getSourceLensMagnification(String ndpiFile);

    public void cleanUp();

}

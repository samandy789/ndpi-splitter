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
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;

import au.org.intersect.ndpisplitter.file.FileManager;

/**
 * Wraps the NDPRead JNA interface to provide a more user-friendly API. So far only the methods we actually need are
 * implemented. Further wrapper methods can be added if needed.
 * 
 * @version $Rev$
 */
public class NDPReadWrapperImpl implements NDPReadWrapper
{
    private static final Logger LOG = Logger.getLogger(NDPReadWrapperImpl.class);
    private static final long ERROR_RESPONSE_CODE = 0;
    private NDPRead ndpRead;
    private final FileManager fileManager;

    public NDPReadWrapperImpl(NDPRead ndpRead, FileManager fileManager)
    {
        super();
        this.fileManager = fileManager;
        this.ndpRead = ndpRead;
    }

    @Override
    public ImageInformation getImageInformation(String fileName) throws FileNotFoundException, NDPReadException
    {
        fileManager.checkFileExists(fileName);

        ImageInformation imageInformation = new ImageInformation();

        imageInformation.setImageHeightInNanometres(ndpRead.GetImageHeight(fileName));
        imageInformation.setImageWidthInNanometres(ndpRead.GetImageWidth(fileName));
        imageInformation.setSourceLensMagnification(ndpRead.GetSourceLens(fileName));
        setPixelWidthAndHeight(fileName, imageInformation);
        setPhysicalCentre(fileName, imageInformation);

        return imageInformation;
    }

    @Override
    public byte[] getImageSegment(String ndpiFileName, int xPositionOfDesiredCentreInNM,
            int yPositionOfDesiredCentreInNM, int desiredFocalPositionInNM, float desiredMagnification,
            int desiredPixelWidth, int desiredPixelHeight) throws NDPReadException, FileNotFoundException
    {
        fileManager.checkFileExists(ndpiFileName);

        NativeLong iPhysicalXPos = new NativeLong(xPositionOfDesiredCentreInNM);
        NativeLong iPhysicalYPos = new NativeLong(yPositionOfDesiredCentreInNM);
        NativeLong iPhysicalZPos = new NativeLong(desiredFocalPositionInNM);
        NativeLongByReference oPhysicalWidth = new NativeLongByReference();
        NativeLongByReference oPhysicalHeight = new NativeLongByReference();
        ByteBuffer iBuffer = ByteBuffer.allocate(0);
        NativeLongByReference ioBufferSize = new NativeLongByReference(new NativeLong(0));

        // set camera resolution
        ndpRead.SetCameraResolution(new NativeLong(desiredPixelWidth), new NativeLong(desiredPixelHeight));

        // initial call to work out necessary buffer size
        int bufferSize = determineRequiredBufferSize(ndpiFileName, desiredMagnification, iPhysicalXPos, iPhysicalYPos,
                iPhysicalZPos, oPhysicalWidth, oPhysicalHeight, iBuffer, ioBufferSize);

        // allocate buffer of the required size
        iBuffer = ByteBuffer.allocate(bufferSize);

        // call again with appropriately sized buffer
        long resultCode = ndpRead.GetImageData(ndpiFileName, iPhysicalXPos, iPhysicalYPos, iPhysicalZPos,
                desiredMagnification, oPhysicalWidth, oPhysicalHeight, iBuffer, ioBufferSize);

        // check if an error occurred
        if (resultCode == ERROR_RESPONSE_CODE)
        {
            String message = ndpRead.GetLastErrorMessage();
            throw new NDPReadException("Failed on second call to GetImageData with suitably sized buffer", message);
        }

        // return the bytes
        byte[] imageBytes = new byte[bufferSize];
        iBuffer.get(imageBytes);
        return imageBytes;
    }

    private void setPhysicalCentre(String fileName, ImageInformation imageInformation) throws NDPReadException
    {
        NativeLongByReference oPhysicalX = new NativeLongByReference();
        NativeLongByReference oPhysicalY = new NativeLongByReference();
        // zero sized buffer because we don't want the actual image back, just want info about the centre
        NativeLongByReference ioBufferSize = new NativeLongByReference(new NativeLong(0));
        ByteBuffer iBuffer = ByteBuffer.allocate(0);

        int resultCode = ndpRead.GetMap(fileName, oPhysicalX, oPhysicalY, new NativeLongByReference(),
                new NativeLongByReference(), iBuffer, ioBufferSize, new NativeLongByReference(),
                new NativeLongByReference());

        if (resultCode == ERROR_RESPONSE_CODE)
        {
            String message = ndpRead.GetLastErrorMessage();
            throw new NDPReadException("Failed to get image details through call to GetMap", message);
        }

        imageInformation.setPhysicalXPositionOfCentreInNanometres(oPhysicalX.getValue().longValue());
        imageInformation.setPhysicalYPositionOfCentreInNanometres(oPhysicalY.getValue().longValue());
    }

    private void setPixelWidthAndHeight(String fileName, ImageInformation imageInformation) throws NDPReadException
    {
        int resultCode;
        NativeLongByReference oPixelWidth = new NativeLongByReference();
        NativeLongByReference oPixelHeight = new NativeLongByReference();
        resultCode = ndpRead.GetSourcePixelSize(fileName, oPixelWidth, oPixelHeight);
        if (resultCode == ERROR_RESPONSE_CODE)
        {
            String message = ndpRead.GetLastErrorMessage();
            throw new NDPReadException("Failed to get pixel size through call to GetSourcePixelSize", message);
        }
        imageInformation.setImageHeightInPixels(oPixelHeight.getValue().longValue());
        imageInformation.setImageWidthInPixels(oPixelWidth.getValue().longValue());
    }

    private int determineRequiredBufferSize(String ndpiFileName, float desiredMagnification, NativeLong iPhysicalXPos,
            NativeLong iPhysicalYPos, NativeLong iPhysicalZPos, NativeLongByReference oPhysicalWidth,
            NativeLongByReference oPhysicalHeight, ByteBuffer iBuffer, NativeLongByReference ioBufferSize)
        throws NDPReadException
    {
        // initial call with zero buffer size to find out the size of buffer we need
        long resultCode = ndpRead.GetImageData(ndpiFileName, iPhysicalXPos, iPhysicalYPos, iPhysicalZPos,
                desiredMagnification, oPhysicalWidth, oPhysicalHeight, iBuffer, ioBufferSize);

        if (resultCode == ERROR_RESPONSE_CODE)
        {
            String message = ndpRead.GetLastErrorMessage();
            throw new NDPReadException("Failed on initial call to GetImageData with zero sized buffer", message);
        }

        // second call with correctly sized buffer
        int bufferSize = ioBufferSize.getValue().intValue();
        return bufferSize;
    }

    @Override
    public float getSourceLensMagnification(String ndpiFile)
    {
        return ndpRead.GetSourceLens(ndpiFile);
    }

    @Override
    public void cleanUp()
    {
        try
        {
            ndpRead.CleanUp();
        }
        catch (Exception e)
        {
            LOG.info("Error occurred during call to CleanUp. Ignoring and continuing anyway");
        }

    }
}

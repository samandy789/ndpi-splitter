/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.imaging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.imageio.ImageIO;

/**
 * This is a helper we've used to analyse the images by saving the pixel values as integers. Keeping it here in the test
 * tree in case we want to use it again.
 * 
 * @version $Rev$
 */
public class CsvImageCreator extends BaseImageCreator
{
    private static final String NEWLINE = System.getProperty("line.separator");
    private final String fileType;

    public CsvImageCreator(String fileType)
    {
        super(fileType);
        checkFormatIsSupported(fileType);
        this.fileType = fileType;
    }

    private void checkFormatIsSupported(String fileType)
    {
        String[] writerFormats = ImageIO.getWriterFormatNames();
        for (String format : writerFormats)
        {
            if (format.equals(fileType))
            {
                return;
            }
        }
        throw new UnsupportedImageFormatException(fileType, writerFormats);
    }

    public void createImageFromNdpiBytes(byte[] imageBytes, int pixelWidth, int pixelHeight, String outputFileName)
        throws IOException
    {
        int pixelStride = 3;
        int scanlineStride = calculateScanlineStride(pixelWidth, pixelStride);

        byte[] invertedImageBytes = flipBytes(imageBytes, scanlineStride, pixelHeight);

        saveAsCsv(scanlineStride, invertedImageBytes, outputFileName);
    }

    private static void saveAsCsv(int scanlineStride, byte[] bytes, String csvFilename) throws IOException
    {
        File file = new File(csvFilename + ".csv");

        Writer output = new BufferedWriter(new FileWriter(file));
        try
        {
            for (int i = 0; i < bytes.length; i++)
            {
                byte b = bytes[i];
                output.write(b + ",");
                if ((i + 1) % scanlineStride == 0)
                {
                    output.write(NEWLINE);
                }
            }
        }
        finally
        {
            output.close();
        }
    }

    /**
     * Calculate the number of bytes per "row" in the image. Each pixel is [pixelStride] bytes, but lines are padded to
     * 4 byte (DWORD) boundaries
     * 
     * @param pixelWidth
     *            of the image data
     * @param pixelStride
     *            the number of bytes per pixel
     * @return
     */
    protected int calculateScanlineStride(int pixelWidth, int pixelStride)
    {
        // byte array is padded to dword boundaries (4 bytes) so calculate what the row length (scanline stride) will be
        int pixelBytesPerRow = pixelWidth * pixelStride;
        int mod = pixelBytesPerRow % 4;
        int padding = mod > 0 ? 4 - mod : 0;
        int scanlineStride = (pixelWidth * pixelStride) + padding;
        return scanlineStride;
    }

    /**
     * This converts an array of image bytes from bottom-up to top-down.
     * 
     * @param original
     *            the bottom-up bytes
     * @param scanlineStride
     *            number of bytes per row
     * @param height
     *            number of rows
     * @return
     */
    protected byte[] flipBytes(byte[] original, int scanlineStride, int height)
    {
        byte[] newOne = new byte[original.length];
        int positionInNewArray = 0;
        for (int j = height - 1; j >= 0; j--)
        {
            for (int i = 0; i < scanlineStride; i++)
            {
                int arrayIndexToPull = j * scanlineStride + i;
                newOne[positionInNewArray] = original[arrayIndexToPull];
                positionInNewArray++;
            }
        }
        return newOne;
    }

    public String getFileType()
    {
        return this.fileType;
    }
}

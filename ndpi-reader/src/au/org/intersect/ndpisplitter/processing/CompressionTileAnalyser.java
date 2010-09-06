/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.processing;

import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.sun.media.imageio.plugins.jpeg2000.J2KImageWriteParam;

/**
 * In this class, we are using a J2KImageWriteParam to compress the image and then calculate the size of the compressed
 * image. From there we calculate the compressed size/total size rate. This rate is used to determine whether the image
 * is interesting or not.
 * 
 * We need to set the lossless parameter to false to allow compression.
 * 
 * The compression rate is specified by ENCODING_RATE. When using aggressive compression (e.g: Double.MAX_VALUE*0.1) the
 * compression will eliminate the subtler differences in the tone, and only account for more drastic differences in the
 * color of the image. The threshold rate for aggressive compression should be decreased accordingly.
 * 
 * @author Diego Alonso
 * 
 */
public class CompressionTileAnalyser implements TileAnalyser
{
    /**
     * Encoding rate represents the bits per pixel bitrate
     */
    private static final double ENCODING_RATE = Double.MAX_VALUE * 0.5;

    /**
     * The number of bands in the image
     */
    private static final int NUMBER_OF_BANDS = 3;

    private final double interestingThreshold;

    public CompressionTileAnalyser(double interestingThreshold)
    {
        super();
        this.interestingThreshold = interestingThreshold;
    }

    @Override
    public boolean isTileInteresting(Raster raster, String outputFileName, String outputDirectory, int pixelWidth,
            int pixelHeight, int scanlineStride)
    {
        DataBufferByte dataByteBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] imageBytes = dataByteBuffer.getData();

        int compressedSize = compressedSize(raster);
        int numberOfPixels = numberOfPixels(imageBytes);
        double ratio = (double) compressedSize / numberOfPixels;

        boolean isInteresting = ratio > interestingThreshold;
        InterestingnessLogger.logTile(outputFileName, outputDirectory, isInteresting, ratio, interestingThreshold);
        return isInteresting;
    }

    /**
     * Calculate the size of the image when compressed with jpeg 2000
     * 
     * @param raster
     * @param outputFileName
     * @return
     */
    private int compressedSize(Raster raster)
    {
        ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg 2000").next();
        ImageOutputStream ios = null;
        try
        {
            ByteArrayOutputStream os1 = new ByteArrayOutputStream();
            ios = ImageIO.createImageOutputStream(os1);
            writer.setOutput(ios);
            J2KImageWriteParam paramJ2K = new J2KImageWriteParam();
            paramJ2K.setLossless(false);
            paramJ2K.setEncodingRate(ENCODING_RATE);
            IIOImage ioimage = new IIOImage(raster, null, null);
            writer.write(null, ioimage, paramJ2K);
            ios.flush();
            return os1.size();
        }
        catch (IOException e)
        {
            return 0;
        }
        finally
        {
            writer.dispose();
        }
    }

    private int numberOfPixels(byte[] imageBytes)
    {
        int totalPoints = 0;
        for (int i = 0; i < imageBytes.length; i += NUMBER_OF_BANDS)
        {
            if ((imageBytes[i] != -1) && (imageBytes[i + 1] != -1) && (imageBytes[i + 2] != -1))
            {
                totalPoints++;
            }
        }
        return totalPoints;
    }
}

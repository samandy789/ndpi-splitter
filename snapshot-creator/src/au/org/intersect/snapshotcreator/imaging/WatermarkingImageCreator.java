/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.imaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import au.org.intersect.ndpisplitter.imaging.JpegImageCreator;

/**
 * 
 * @version $Rev$
 */
public class WatermarkingImageCreator extends JpegImageCreator
{
    private final WatermarkProperties watermarkProperties;

    public WatermarkingImageCreator(float jpegQuality, WatermarkProperties watermarkProperties)
    {
        super(jpegQuality);
        this.watermarkProperties = watermarkProperties;
    }

    protected void writeImage(String fileType, BufferedImage image, File file) throws IOException
    {
        BufferedImage watermark = ImageIO.read(WatermarkingImageCreator.class.getResourceAsStream("/watermark.jpg"));
        Watermarker.doWatermark(image, watermark, watermarkProperties);
        super.writeImage(fileType, image, file);

    }

}

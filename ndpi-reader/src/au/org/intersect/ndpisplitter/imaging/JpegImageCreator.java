/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.imaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

/**
 * 
 * @version $Rev$
 */
public class JpegImageCreator extends BaseImageCreator
{

    private static final String JPG_FILE_TYPE = "jpg";
    private final float jpegQuality;

    public JpegImageCreator(float jpegQuality)
    {
        super(JPG_FILE_TYPE);
        this.jpegQuality = jpegQuality;
    }

    /**
     * Overriding the superclass so we can set JPEG quality explicitly
     */
    @Override
    protected void writeImage(String fileType, BufferedImage image, File file) throws IOException
    {
        ImageWriter writer = ImageIO.getImageWritersByFormatName(JPG_FILE_TYPE).next();

        // instantiate an ImageWriteParam object with default compression options
        ImageWriteParam iwp = writer.getDefaultWriteParam();

        // Now we can set the compression quality:
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        // an integer between 0 and 1: 1 specifies minimum compression and maximum quality
        iwp.setCompressionQuality(jpegQuality);

        // Output the file:
        FileImageOutputStream output = new FileImageOutputStream(file);
        writer.setOutput(output);
        IIOImage image2 = new IIOImage(image, null, null);
        writer.write(null, image2, iwp);
        writer.dispose();
    }
}

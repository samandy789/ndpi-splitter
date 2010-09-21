/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.imaging;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * 
 * @version $Rev$
 */
public class ImageResizer
{
    public static BufferedImage scale(BufferedImage image, double multiplier)
    {
        int sourceHeight = image.getHeight();
        int sourceWidth = image.getWidth();
        int newWidth = (int) (sourceWidth * multiplier);
        int newHeight = (int) (sourceHeight * multiplier);

        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = scaledImage.createGraphics();
        AffineTransform transform = AffineTransform.getScaleInstance(multiplier, multiplier);
        graphics.drawRenderedImage(image, transform);
        return scaledImage;
    }
}

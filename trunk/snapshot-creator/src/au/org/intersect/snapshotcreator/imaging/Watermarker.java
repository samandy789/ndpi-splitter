/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.imaging;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * 
 * @version $Rev$
 */
public class Watermarker
{
    public static void doWatermark(BufferedImage destinationImage, BufferedImage watermark,
            WatermarkProperties watermarkProperties)
    {
        BufferedImage resizedWatermark = scaleWatermark(watermark, destinationImage, watermarkProperties
                .getRelativeWidth());

        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, watermarkProperties
                .getTransparency());
        Graphics2D graphics = (Graphics2D) destinationImage.getGraphics();
        graphics.setComposite(alphaComposite);
        graphics.drawImage(destinationImage, 0, 0, null);
        Point sourcePosition = getPosition(destinationImage, resizedWatermark, watermarkProperties
                .getRelativeXPosition(), watermarkProperties.getRelativeYPosition());

        int x = (int) sourcePosition.getX();
        int y = (int) sourcePosition.getY();
        graphics.drawImage(resizedWatermark, x, y, null);
        graphics.dispose();
    }

    private static Point getPosition(BufferedImage destImage, BufferedImage watermarkImage, float xRelative,
            float yRelative)
    {
        int destinationWidth = destImage.getWidth();
        int destinationHeight = destImage.getHeight();
        int sourceHeight = watermarkImage.getHeight();
        int sourceWidth = watermarkImage.getWidth();

        int x = (int) (xRelative * destinationWidth);
        int y = (int) (yRelative * destinationHeight);

        Rectangle rectangle = new Rectangle();
        rectangle.setBounds(x, y, sourceWidth, sourceHeight);
        Point location = rectangle.getLocation();

        // we now have the top left corner of the image at the desired location
        // we want the centre of the image at the desired location, so shift by half the watermark width/height
        int xOffset = -sourceWidth / 2;
        int yOffset = -sourceHeight / 2;
        location.translate(xOffset, yOffset);

        return location;
    }

    private static BufferedImage scaleWatermark(BufferedImage watermarkImage, BufferedImage destinationImage,
            float relativeWidth)
    {
        int destinationWidth = destinationImage.getWidth();
        int sourceWidth = watermarkImage.getWidth();

        double ratio = (double) sourceWidth / (double) destinationWidth;
        double desiredRatio = relativeWidth;
        double multiplier = desiredRatio / ratio;

        BufferedImage watermarkImageResized = ImageResizer.scale(watermarkImage, multiplier);
        return watermarkImageResized;
    }

}

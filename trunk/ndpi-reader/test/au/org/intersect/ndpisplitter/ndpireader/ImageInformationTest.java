/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.ndpireader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * TODO: rounding errors?
 * 
 * @version $Rev$
 */
public class ImageInformationTest
{

    @Test
    public void testGetEdgesWithNegativeXAndPositiveYCoordinates()
    {
        ImageInformation imageInformation = createImageInformation(4000, 3000, -150, 500);
        assertDouble(-2150, imageInformation.locateLeftEdge());
        assertDouble(1850, imageInformation.locateRightEdge());
        assertDouble(-1000, imageInformation.locateTopEdge());
        assertDouble(2000, imageInformation.locateBottomEdge());
        // check that difference between top/bottom edges = height
        assertDouble(3000, imageInformation.locateBottomEdge() - imageInformation.locateTopEdge());
        // check that difference between left/rigth edges = height
        assertDouble(4000, imageInformation.locateRightEdge() - imageInformation.locateLeftEdge());
    }

    @Test
    public void testGetEdgesWithNegativeXAndNegativeYCoordinates()
    {
        ImageInformation imageInformation = createImageInformation(4000, 3000, -150, -500);
        assertDouble(-2150, imageInformation.locateLeftEdge());
        assertDouble(1850, imageInformation.locateRightEdge());
        assertDouble(-2000, imageInformation.locateTopEdge());
        assertDouble(1000, imageInformation.locateBottomEdge());
        // check that difference between top/bottom edges = height
        assertDouble(3000, imageInformation.locateBottomEdge() - imageInformation.locateTopEdge());
        // check that difference between left/rigth edges = height
        assertDouble(4000, imageInformation.locateRightEdge() - imageInformation.locateLeftEdge());
    }

    @Test
    public void testGetEdgesWithPositiveXAndNegativeYCoordinates()
    {
        ImageInformation imageInformation = createImageInformation(4000, 3000, 150, -500);
        assertDouble(-1850, imageInformation.locateLeftEdge());
        assertDouble(2150, imageInformation.locateRightEdge());
        assertDouble(-2000, imageInformation.locateTopEdge());
        assertDouble(1000, imageInformation.locateBottomEdge());
        // check that difference between top/bottom edges = height
        assertDouble(3000, imageInformation.locateBottomEdge() - imageInformation.locateTopEdge());
        // check that difference between left/rigth edges = height
        assertDouble(4000, imageInformation.locateRightEdge() - imageInformation.locateLeftEdge());
    }

    @Test
    public void testGetEdgesWithPositiveXAndPositiveYCoordinates()
    {
        ImageInformation imageInformation = createImageInformation(4000, 3000, 150, 500);
        assertDouble(-1850, imageInformation.locateLeftEdge());
        assertDouble(2150, imageInformation.locateRightEdge());
        assertDouble(-1000, imageInformation.locateTopEdge());
        assertDouble(2000, imageInformation.locateBottomEdge());
        // check that difference between top/bottom edges = height
        assertDouble(3000, imageInformation.locateBottomEdge() - imageInformation.locateTopEdge());
        // check that difference between left/rigth edges = height
        assertDouble(4000, imageInformation.locateRightEdge() - imageInformation.locateLeftEdge());
    }

    @Test
    public void testGetEdgesWhereImageDoesntCrossCentreAxis()
    {
        ImageInformation imageInformation = createImageInformation(4000, 3000, 10000, 12000);
        assertDouble(8000, imageInformation.locateLeftEdge());
        assertDouble(12000, imageInformation.locateRightEdge());
        assertDouble(10500, imageInformation.locateTopEdge());
        assertDouble(13500, imageInformation.locateBottomEdge());
        // check that difference between top/bottom edges = height
        assertDouble(3000, imageInformation.locateBottomEdge() - imageInformation.locateTopEdge());
        // check that difference between left/right edges = height
        assertDouble(4000, imageInformation.locateRightEdge() - imageInformation.locateLeftEdge());
    }

    @Test
    public void testGetEdgesWithOddNumbers()
    {
        ImageInformation imageInformation = createImageInformation(4001, 3001, -150, 500);
        assertDouble(-2150.5, imageInformation.locateLeftEdge());
        assertDouble(1850.5, imageInformation.locateRightEdge());
        assertDouble(-1000.5, imageInformation.locateTopEdge());
        assertDouble(2000.5, imageInformation.locateBottomEdge());
        // check that difference between top/bottom edges = height
        assertDouble(3001, imageInformation.locateBottomEdge() - imageInformation.locateTopEdge());
        // check that difference between left/rigth edges = height
        assertDouble(4001, imageInformation.locateRightEdge() - imageInformation.locateLeftEdge());
    }

    private ImageInformation createImageInformation(int width, int height, int physicalXOfCentre, int physicalYOfCentre)
    {
        ImageInformation imageInformation = new ImageInformation();
        imageInformation.setImageHeightInNanometres(height);
        imageInformation.setImageWidthInNanometres(width);
        imageInformation.setPhysicalXPositionOfCentreInNanometres(physicalXOfCentre);
        imageInformation.setPhysicalYPositionOfCentreInNanometres(physicalYOfCentre);
        assertNotNull(imageInformation.toString());
        return imageInformation;
    }

    /**
     * Assert a double value with a tolerance of 0.1
     */
    private void assertDouble(double expected, double actual)
    {
        assertEquals(expected, actual, 0.1);
    }
}

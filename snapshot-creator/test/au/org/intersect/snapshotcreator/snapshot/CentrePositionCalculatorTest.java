/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.snapshot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import au.org.intersect.ndpisplitter.ndpireader.ImageInformation;

/**
 * 
 * @version $Rev$
 */
public class CentrePositionCalculatorTest
{

    private static final int VERY_BIG = Integer.MAX_VALUE;

    @Test
    public void testCalculatesCorrectlyWhenSourceAndDestinationMagnificationAreSame()
    {
        ImageInformation map = new ImageInformation();
        map.setImageWidthInPixels(2043);
        map.setImageHeightInPixels(1560);
        map.setPhysicalXPositionOfCentreInNanometres(-1500);
        map.setPhysicalYPositionOfCentreInNanometres(1944);
        map.setSourceLensMagnification(20f);

        // x,y positions = physical centre
        // tile width in pixels = total width / 2 = 1021;
        // tile height in pixels = total height / 2 = 780
        CentrePositionCalculator calculator = new CentrePositionCalculator(map, 20, VERY_BIG);
        assertPositions(calculator.getTileXPositions(), -1500L);
        assertPositions(calculator.getTileYPositions(), 1944L);
        assertEquals(1022, calculator.getTileWidthInPixels());
        assertEquals(780, calculator.getTileHeightInPixels());
        assertEquals(1, calculator.getTotalNumberOfTiles());
        assertNotNull(calculator.toString());
    }

    @Test
    public void testCalculatesCorrectlyWhenDestinationMagnificationIsLower()
    {
        ImageInformation map = new ImageInformation();
        map.setImageWidthInPixels(2043);
        map.setImageHeightInPixels(1560);
        map.setPhysicalXPositionOfCentreInNanometres(-1500);
        map.setPhysicalYPositionOfCentreInNanometres(1944);
        map.setSourceLensMagnification(20f);

        // x,y positions = physical centre
        // tile size is scaled down by 10 as we are asking for mag 2 from a mag 20 image
        // tile width in pixels = total width / 2 / 10 = 102;
        // tile height in pixels = total height / 2 /10= 78;
        CentrePositionCalculator calculator = new CentrePositionCalculator(map, 2, VERY_BIG);
        assertPositions(calculator.getTileXPositions(), -1500L);
        assertPositions(calculator.getTileYPositions(), 1944L);
        assertEquals(102, calculator.getTileWidthInPixels());
        assertEquals(78, calculator.getTileHeightInPixels());
    }

    @Test
    public void testCalculatesCorrectlyWhenDestinationMagnificationIsHigher()
    {
        ImageInformation map = new ImageInformation();
        map.setImageWidthInPixels(2043);
        map.setImageHeightInPixels(1560);
        map.setPhysicalXPositionOfCentreInNanometres(-1500);
        map.setPhysicalYPositionOfCentreInNanometres(1944);
        map.setSourceLensMagnification(5f);

        // x,y positions = physical centre
        // tile size is scaled up by x4 as we are asking for mag 20 from a mag 5 image
        // tile width in pixels = total width / 2 * 4 = 4086;
        // tile height in pixels = total height / 2 * 4= 3120;
        CentrePositionCalculator calculator = new CentrePositionCalculator(map, 20, VERY_BIG);
        assertPositions(calculator.getTileXPositions(), -1500L);
        assertPositions(calculator.getTileYPositions(), 1944L);
        assertEquals(4086, calculator.getTileWidthInPixels());
        assertEquals(3120, calculator.getTileHeightInPixels());
    }

    @Test
    public void testScalesDownWhenDimensionsExceedMaxPixels()
    {
        ImageInformation map = new ImageInformation();
        map.setImageWidthInPixels(2043);
        map.setImageHeightInPixels(1560);
        map.setPhysicalXPositionOfCentreInNanometres(-1500);
        map.setPhysicalYPositionOfCentreInNanometres(1944);
        map.setSourceLensMagnification(5f);
        
        // x,y positions = physical centre
        // tile size is scaled up by x4 as we are asking for mag 20 from a mag 5 image
        // tile width in pixels = total width / 2 * 4 = 4086;
        // tile height in pixels = total height / 2 * 4= 3120;
        // total pixels = 12748320, max is 10000000
        // scaling factor is 10000000/12748320 = sqrt(0.784417083976555) = 0.885673237699184
        // therefore wxh = 3619x2763
        CentrePositionCalculator calculator = new CentrePositionCalculator(map, 20, 10000000);
        assertPositions(calculator.getTileXPositions(), -1500L);
        assertPositions(calculator.getTileYPositions(), 1944L);
        assertEquals(3619, calculator.getTileWidthInPixels());
        assertEquals(2763, calculator.getTileHeightInPixels());
    }
    
    private void assertPositions(List<Long> tilePositions, Long... positions)
    {
        assertEquals("Incorrect number of positions in list", positions.length, tilePositions.size());
        List<Long> expected = Arrays.asList(positions);
        assertEquals(expected, tilePositions);
    }
}

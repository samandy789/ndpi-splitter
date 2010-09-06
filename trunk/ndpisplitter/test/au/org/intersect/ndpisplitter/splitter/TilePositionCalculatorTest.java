/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.splitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import au.org.intersect.ndpisplitter.ndpireader.ImageInformation;

/**
 * @version $Rev$
 * 
 */
public class TilePositionCalculatorTest
{

    @Test
    public void testCalculatesTilePositionsCorrectly()
    {
        ImageInformation info = new ImageInformation();
        info.setImageHeightInNanometres(10000);
        info.setImageWidthInNanometres(20000);
        info.setImageHeightInPixels(500);
        info.setImageWidthInPixels(1000);
        info.setSourceLensMagnification(20.0f);

        info.setPhysicalXPositionOfCentreInNanometres(3000);
        info.setPhysicalYPositionOfCentreInNanometres(-4000);

        TilePositionCalculator calculator = new TilePositionCalculator(info, 220, 180, 20);

        List<Long> tileXPositions = calculator.getTileXPositions();
        // physical x = 3000, so the left edge = -7000
        // requested tiles are 220pixels, and there's 20 nm per pixel so each tile is 4400 nm wide
        assertPositions(tileXPositions, -4800L, -400L, 4000L, 8400L, 12800L);

        List<Long> tileYPositions = calculator.getTileYPositions();
        // physical y = -4000, so the top edge = -9000
        // requested tiles are 180pixels, and there's 20 nm per pixel so each tile is 3600 nm wide
        assertPositions(tileYPositions, -7200L, -3600L, 0L);
        
        assertEquals(180, calculator.getTileHeightInPixels());
        assertEquals(220, calculator.getTileWidthInPixels());
    }

    @Test
    public void testCalculatesTilePositionsCorrectlyWhenCentreOfLastTileIsPastEdge()
    {
        ImageInformation info = new ImageInformation();
        info.setImageWidthInNanometres(2000);
        info.setImageWidthInPixels(200);
        info.setImageHeightInNanometres(1000);
        info.setImageHeightInPixels(100);
        info.setSourceLensMagnification(20.0f);

        info.setPhysicalXPositionOfCentreInNanometres(3000);
        info.setPhysicalYPositionOfCentreInNanometres(-4000);

        TilePositionCalculator calculator = new TilePositionCalculator(info, 90, 45, 20);

        List<Long> tileXPositions = calculator.getTileXPositions();
        // physical x = 3000, so the left edge = 3000 - (2000/2) = 2000
        // requested tiles are 90pixels, and there's 10 nm per pixel so each tile is 900 nm wide
        // first tile = 2000 + (900/2) = 2450
        assertPositions(tileXPositions, 2450L, 3350L, 4250L);

        List<Long> tileYPositions = calculator.getTileYPositions();
        // physical y = -4000, so the top edge = -4000 - (1000/2) = -4500
        // requested tiles are 45pixels, and there's 10 nm per pixel so each tile is 450 nm wide
        // first tile = -4,500 + (450/2) = -4,275
        assertPositions(tileYPositions, -4275L, -3825L, -3375L);
        assertEquals(45, calculator.getTileHeightInPixels());
        assertEquals(90, calculator.getTileWidthInPixels());
    }

    @Test
    public void testCalculatesTilePositionsCorrectlyWhenNmPerPixelDifferentOnEachAxis()
    {
        ImageInformation info = new ImageInformation();
        info.setImageHeightInNanometres(15000);
        info.setImageWidthInNanometres(20000);
        info.setImageHeightInPixels(500);
        info.setImageWidthInPixels(1000);
        info.setSourceLensMagnification(20.0f);

        info.setPhysicalXPositionOfCentreInNanometres(3000);
        info.setPhysicalYPositionOfCentreInNanometres(-4000);

        TilePositionCalculator calculator = new TilePositionCalculator(info, 220, 180, 20);

        List<Long> tileXPositions = calculator.getTileXPositions();
        // physical x = 3000, width=20000 so the left edge = -7000
        // requested tiles are 220pixels, and there's 20 nm per pixel so each tile is 4400 nm wide
        assertPositions(tileXPositions, -4800L, -400L, 4000L, 8400L, 12800L);

        List<Long> tileYPositions = calculator.getTileYPositions();
        // physical y = -4000, width=15000 so the top edge = -11500
        // requested tiles are 180pixels, and there's 30 nm per pixel so each tile is 5400 nm wide
        assertPositions(tileYPositions, -8800L, -3400L, 2000L);
    }

    @Test
    public void testCalculatesTilePositionsCorrectlyTilesFitExactly()
    {
        ImageInformation info = new ImageInformation();
        info.setImageWidthInNanometres(20000);
        info.setImageWidthInPixels(2000);
        info.setImageHeightInNanometres(10000);
        info.setImageHeightInPixels(1000);
        info.setSourceLensMagnification(20.0f);

        info.setPhysicalXPositionOfCentreInNanometres(3000);
        info.setPhysicalYPositionOfCentreInNanometres(-4000);

        TilePositionCalculator calculator = new TilePositionCalculator(info, 1000, 500, 20);

        List<Long> tileXPositions = calculator.getTileXPositions();
        // physical x = 3000, width=20,000 so the left edge = 3000-10000 = -7,000
        // requested tiles are 1,000 pixels, and there's 10 nm per pixel so each tile is 10,000 nm wide
        assertPositions(tileXPositions, -2000L, 8000L);

        List<Long> tileYPositions = calculator.getTileYPositions();
        // physical y = -4,000, height=10,000 so the top edge = -4000-5000 = -9,000
        // requested tiles are 500 pixels, and there's 10 nm per pixel so each tile is 5,000 nm wide
        assertPositions(tileYPositions, -6500L, -1500L);
    }

    @Test
    public void testCalculatesTilePositionsCorrectlyWhenTileIsBiggerThanImage()
    {
        // this case is unlikely, so it will just get an image which is in the top left corner, using the same
        // calculations are normal
        ImageInformation info = new ImageInformation();
        info.setImageHeightInNanometres(10000);
        info.setImageHeightInPixels(1000);
        info.setImageWidthInNanometres(20000);
        info.setImageWidthInPixels(2000);
        info.setSourceLensMagnification(20.0f);

        info.setPhysicalXPositionOfCentreInNanometres(3000);
        info.setPhysicalYPositionOfCentreInNanometres(-4000);

        TilePositionCalculator calculator = new TilePositionCalculator(info, 3000, 1500, 20);

        List<Long> tileXPositions = calculator.getTileXPositions();
        // physical x = 3,000, width=20,000 so the left edge = 3000-10000 = -7,000
        // requested tiles are 3,000 pixels, and there's 10 nm per pixel so each tile is 30,000 nm wide
        // first tile = -7,000 + 15,000
        assertPositions(tileXPositions, 8000L);

        List<Long> tileYPositions = calculator.getTileYPositions();
        // physical y = -4,000, height=10,000 so the top edge = -4000-5000 = -9,000
        // requested tiles are 1500 pixels, and there's 10 nm per pixel so each tile is 15,000 nm wide
        // first tile = -9000 + 7500
        assertPositions(tileYPositions, -1500L);
    }

    @Test
    public void testCalculatesTilePositionsCorrectlyWhenTileMoreThanDoubleTheSizeOfTheImage()
    {
        // this case is unlikely, so it will just get an image which is in the top left corner, using the same
        // calculations are normal
        ImageInformation info = new ImageInformation();
        info.setImageHeightInNanometres(10000);
        info.setImageHeightInPixels(1000);
        info.setImageWidthInNanometres(20000);
        info.setImageWidthInPixels(2000);
        info.setSourceLensMagnification(20.0f);

        info.setPhysicalXPositionOfCentreInNanometres(3000);
        info.setPhysicalYPositionOfCentreInNanometres(-4000);

        TilePositionCalculator calculator = new TilePositionCalculator(info, 5000, 3000, 20);

        List<Long> tileXPositions = calculator.getTileXPositions();
        // physical x = 3,000, width=20,000 so the left edge = 3000-10000 = -7,000
        // requested tiles are 5,000 pixels, and there's 10 nm per pixel so each tile is 50,000 nm wide
        // first tile = -7,000 + 25,000
        assertPositions(tileXPositions, 18000L);

        List<Long> tileYPositions = calculator.getTileYPositions();
        // physical y = -4,000, height=10,000 so the top edge = -4000-5000 = -9,000
        // requested tiles are 3000 pixels, and there's 10 nm per pixel so each tile is 30,000 nm wide
        // first tile = -9000 + 15,000
        assertPositions(tileYPositions, 6000L);
    }

    @Test
    public void testCalculatesTilePositionsCorrectlyWhenNumbersDontDivideExactly()
    {
        ImageInformation info = new ImageInformation();
        info.setImageWidthInNanometres(3233);
        info.setImageWidthInPixels(200);
        info.setImageHeightInNanometres(1234);
        info.setImageHeightInPixels(100);
        info.setSourceLensMagnification(20.0f);

        info.setPhysicalXPositionOfCentreInNanometres(3000);
        info.setPhysicalYPositionOfCentreInNanometres(-4000);

        TilePositionCalculator calculator = new TilePositionCalculator(info, 80, 40, 20);

        List<Long> tileXPositions = calculator.getTileXPositions();
        // physical x = 3,000, width=3,233 so the left edge = 3,000-(3,233/2) = 1,383.5
        // requested tiles are 80 pixels, and there's 16.165 nm per pixel so each tile is 1,293.2 nm wide
        // first tile = 1,383.5 + 646.6 = 2030.1 => round to 2030
        // subsequent tiles we add 1,293.2 and then round
        assertPositions(tileXPositions, 2030L, 3323L, 4616L);

        List<Long> tileYPositions = calculator.getTileYPositions();
        // physical y = -4,000,W height=1,234 so the top edge = -4,000-(1,234/2) = -4,617
        // requested tiles are 40 pixels, and there's 12.34 nm per pixel so each tile is 493.6 nm wide
        // first tile = -4,617 + (493.6/2)= -4370.2 => round to 4370
        // subsequent tiles we add 493.6 and then round
        assertPositions(tileYPositions, -4370L, -3876L, -3382L);
    }

    // TODO: test when last tile centres on the edge.

    @Test
    public void testCalculatesTilePositionsCorrectlyWhenRequestingLowerMagnification()
    {
        ImageInformation info = new ImageInformation();
        info.setSourceLensMagnification(40.0f);
        info.setImageHeightInNanometres(10000);
        info.setImageWidthInNanometres(20000);
        info.setImageHeightInPixels(500);
        info.setImageWidthInPixels(1000);

        info.setPhysicalXPositionOfCentreInNanometres(3000);
        info.setPhysicalYPositionOfCentreInNanometres(-4000);

        TilePositionCalculator calculator = new TilePositionCalculator(info, 220, 180, 20);

        List<Long> tileXPositions = calculator.getTileXPositions();
        // physical x = 3000, so the left edge = -7000, right edge = 13000
        // requested tiles are 220pixels, and there's 20 nm per pixel so each tile would be 4400 nm wide
        // however we're doubling the size of the tiles since we're requesting 20x from a 40x image... so
        // tiles are 8800 nm wide -> first tile is -7000+4400 =
        assertPositions(tileXPositions, -2600L, 6200L, 15000L);

        List<Long> tileYPositions = calculator.getTileYPositions();
        // physical y = -4000, so the top edge = -9000, bottom edge = 1000
        // requested tiles are 180pixels, and there's 20 nm per pixel so each tile is 3600 nm wide
        // however we're doubling tile size since requesting 20x from 40x... sotiles are 7200 wide
        // first tile is -9000 + 3600 = -5400
        assertPositions(tileYPositions, -5400L, 1800L);
    }

    public void testCalculatesTilePositionsCorrectlyWhenRequestingHigherMagnification()
    {
        ImageInformation info = new ImageInformation();
        info.setSourceLensMagnification(20.0f);
        info.setImageHeightInNanometres(10000);
        info.setImageWidthInNanometres(20000);
        info.setImageHeightInPixels(500);
        info.setImageWidthInPixels(1000);

        info.setPhysicalXPositionOfCentreInNanometres(3000);
        info.setPhysicalYPositionOfCentreInNanometres(-4000);

        TilePositionCalculator calculator = new TilePositionCalculator(info, 220, 180, 40);

        List<Long> tileXPositions = calculator.getTileXPositions();
        // physical x = 3000, so the left edge = -7000, right edge = 13000
        // requested tiles are 220pixels, and there's 20 nm per pixel so each tile would be 4400 nm wide
        // however we're halving the size of the tiles since we're requesting 40x from a 20x image... so
        // tiles are 2200 nm wide -> first tile is -7000+1100 =
        assertPositions(tileXPositions, -2600L, 6200L, 15000L);

        List<Long> tileYPositions = calculator.getTileYPositions();
        // physical y = -4000, so the top edge = -9000, bottom edge = 1000
        // requested tiles are 180pixels, and there's 20 nm per pixel so each tile is 3600 nm wide
        // however we're halving tile size since requesting 40x from 20x... sotiles are 1800 wide
        // first tile is -9000 + 900 = -8100
        assertPositions(tileYPositions, -8100L, 1800L);
    }

    @Test
    public void testToString()
    {
        // just test that it doesn't die
        ImageInformation info = new ImageInformation();
        info.setSourceLensMagnification(20.0f);
        info.setImageHeightInNanometres(10000);
        info.setImageWidthInNanometres(20000);
        info.setImageHeightInPixels(500);
        info.setImageWidthInPixels(1000);

        info.setPhysicalXPositionOfCentreInNanometres(3000);
        info.setPhysicalYPositionOfCentreInNanometres(-4000);
        TilePositionCalculator calculator = new TilePositionCalculator(info, 10, 20, 20);
        assertNotNull(calculator.toString());
    }

    private void assertPositions(List<Long> tilePositions, Long... positions)
    {
        assertEquals("Incorrect number of positions in list", positions.length, tilePositions.size());
        List<Long> expected = Arrays.asList(positions);
        assertEquals(expected, tilePositions);
    }
}

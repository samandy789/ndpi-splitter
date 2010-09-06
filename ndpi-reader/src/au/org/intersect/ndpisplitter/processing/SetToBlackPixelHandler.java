/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.processing;


/**
 *
 * @version $Rev$
 */
public class SetToBlackPixelHandler implements PixelHandler
{

    @Override
    public void handlePixel(byte band1, byte band2, byte band3)
    {
//        /**
//         * Useful for debugging - gives an image where only the 'interesting' pixels are displayed
//         */
//        @SuppressWarnings("unused")
//        private void setNonInterestingPixelsToBlack(byte[] invertedImageBytes, PixelTileAnalyser analizer)
//        {
//            for (int i = 0; i < invertedImageBytes.length; i += 3)
//            {
//                int band1 = invertedImageBytes[i];
//                int band2 = invertedImageBytes[i + 1];
//                int band3 = invertedImageBytes[i + 2];
//
//                if (!analizer.isPixelInteresting(band1, band2, band3))
//                {
//                    invertedImageBytes[i] = 0;
//                    invertedImageBytes[i + 1] = 0;
//                    invertedImageBytes[i + 2] = 0;
//                }
//            }
//        }

    }

}

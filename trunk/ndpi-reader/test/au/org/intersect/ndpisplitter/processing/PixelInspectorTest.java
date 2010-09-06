/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.processing;

import static org.mockito.Mockito.inOrder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * 
 * @version $Rev$
 */
public class PixelInspectorTest
{

    @Mock
    private PixelHandler pixelHandler;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSkipsPaddingPixels()
    {
        int pixelWidth = 3;
        int pixelHeight = 2;
        int scanlineStride = 12;
        PixelInspector inspector = new PixelInspector(pixelWidth, pixelHeight, scanlineStride);

        byte[] imageBytes = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 100, 101, 102, 103, 104, 105, 106, 107,
            108, 109, 110, 111};

        inspector.inspectPixels(imageBytes, pixelHandler);

        InOrder order = inOrder(pixelHandler);
        order.verify(pixelHandler).handlePixel((byte) 0, (byte) 1, (byte) 2);
        order.verify(pixelHandler).handlePixel((byte) 3, (byte) 4, (byte) 5);
        order.verify(pixelHandler).handlePixel((byte) 6, (byte) 7, (byte) 8);
        order.verify(pixelHandler).handlePixel((byte) 100, (byte) 101, (byte) 102);
        order.verify(pixelHandler).handlePixel((byte) 103, (byte) 104, (byte) 105);
        order.verify(pixelHandler).handlePixel((byte) 106, (byte) 107, (byte) 108);
    }

    @Test
    public void testHandlesNoPadding()
    {
        int pixelWidth = 4;
        int pixelHeight = 2;
        int scanlineStride = 12;
        PixelInspector inspector = new PixelInspector(pixelWidth, pixelHeight, scanlineStride);

        byte[] imageBytes = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 100, 101, 102, 103, 104, 105, 106, 107,
            108, 109, 110, 111};

        inspector.inspectPixels(imageBytes, pixelHandler);

        InOrder order = inOrder(pixelHandler);
        order.verify(pixelHandler).handlePixel((byte) 0, (byte) 1, (byte) 2);
        order.verify(pixelHandler).handlePixel((byte) 3, (byte) 4, (byte) 5);
        order.verify(pixelHandler).handlePixel((byte) 6, (byte) 7, (byte) 8);
        order.verify(pixelHandler).handlePixel((byte) 9, (byte) 10, (byte) 11);
        order.verify(pixelHandler).handlePixel((byte) 100, (byte) 101, (byte) 102);
        order.verify(pixelHandler).handlePixel((byte) 103, (byte) 104, (byte) 105);
        order.verify(pixelHandler).handlePixel((byte) 106, (byte) 107, (byte) 108);
        order.verify(pixelHandler).handlePixel((byte) 109, (byte) 110, (byte) 111);
    }

    @Test
    public void testSkipsPaddingWhenOnly2Over()
    {
        int scanlineStride = 8;
        int pixelWidth = 2;
        int pixelHeight = 2;
        PixelInspector inspector = new PixelInspector(pixelWidth, pixelHeight, scanlineStride);

        byte[] imageBytes = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 100, 101, 102, 103, 104, 105, 106, 107};

        inspector.inspectPixels(imageBytes, pixelHandler);

        InOrder order = inOrder(pixelHandler);
        order.verify(pixelHandler).handlePixel((byte) 0, (byte) 1, (byte) 2);
        order.verify(pixelHandler).handlePixel((byte) 3, (byte) 4, (byte) 5);
        order.verify(pixelHandler).handlePixel((byte) 100, (byte) 101, (byte) 102);
        order.verify(pixelHandler).handlePixel((byte) 103, (byte) 104, (byte) 105);
    }
}

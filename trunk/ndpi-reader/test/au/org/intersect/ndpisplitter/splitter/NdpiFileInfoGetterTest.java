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
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.org.intersect.ndpisplitter.ndpireader.ImageInformation;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadException;
import au.org.intersect.ndpisplitter.ndpireader.NDPReadWrapper;

/**
 * @version $Rev$
 * 
 */
public class NdpiFileInfoGetterTest
{

    @Mock
    private NDPReadWrapper wrapper;

    private NdpiFileInfoGetter infoGetter;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        infoGetter = new NdpiFileInfoGetter(wrapper);
    }

    @Test
    public void testGetMagnification() throws ImageReadingException
    {
        when(wrapper.getSourceLensMagnification("testfile")).thenReturn(20.0f);
        assertEquals(20.0f, infoGetter.getMagnification("testfile"), 0.01);
    }

    @Test(expected = ImageReadingException.class)
    public void testGetThrowsExceptionIfWrapperReturnsZero() throws ImageReadingException
    {
        when(wrapper.getSourceLensMagnification("testfile")).thenReturn(0.0f);
        infoGetter.getMagnification("testfile");
    }

    @Test
    public void testGetImageInfo() throws ImageReadingException, FileNotFoundException, NDPReadException
    {
        ImageInformation info = new ImageInformation();
        when(wrapper.getImageInformation("testfile")).thenReturn(info);
        assertSame(info, infoGetter.getImageInformation("testfile"));
    }

    @Test(expected = ImageReadingException.class)
    public void testWrapsNdpReadExceptionsFromWrapper() throws ImageReadingException, FileNotFoundException,
        NDPReadException
    {
        when(wrapper.getImageInformation("testfile")).thenThrow(new NDPReadException("msg", "details"));
        infoGetter.getImageInformation("testfile");
    }

    @Test(expected = ImageReadingException.class)
    public void testWrapsFileNotFoundExceptionsFromWrapper() throws ImageReadingException, FileNotFoundException,
        NDPReadException
    {
        when(wrapper.getImageInformation("testfile")).thenThrow(new FileNotFoundException());
        infoGetter.getImageInformation("testfile");
    }

}

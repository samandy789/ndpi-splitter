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
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;

import au.org.intersect.ndpisplitter.file.FileManager;

/**
 * TODO: needs some refactoring and tidying up
 * 
 * @version $Rev$
 */
public class NDPReadWrapperImplTest
{
    public static final int SUCCESS_RESPONSE_CODE = 1;
    public static final int ERROR_RESPONSE_CODE = 0;
    private static final String SAMPLE_FILE_NAME = "afile";
    private NDPReadWrapperImpl ndpReadWrapper;
    private NDPRead ndpRead;
    private FileManager fileManager;

    @Before
    public void setUp()
    {
        ndpRead = mock(NDPRead.class);
        fileManager = mock(FileManager.class);
        ndpReadWrapper = new NDPReadWrapperImpl(ndpRead, fileManager);
    }

    @Test
    public void testGetImageInformationReturnsCorrectValuesWhenFileExists() throws FileNotFoundException,
        NDPReadException
    {
        // stub the methods we need on the NDPRead interface

        // callback which sets the output parameters for physical x and y position
        when(
                ndpRead.GetMap(eq(SAMPLE_FILE_NAME), any(NativeLongByReference.class),
                        any(NativeLongByReference.class), any(NativeLongByReference.class),
                        any(NativeLongByReference.class), any(ByteBuffer.class), any(NativeLongByReference.class),
                        any(NativeLongByReference.class), any(NativeLongByReference.class))).thenAnswer(
                (Answer<Integer>) new GetMapCallback(50, 60));

        // image width, height and source lens
        when(ndpRead.GetImageWidth(SAMPLE_FILE_NAME)).thenReturn(500);
        when(ndpRead.GetImageHeight(SAMPLE_FILE_NAME)).thenReturn(300);
        when(ndpRead.GetSourceLens(SAMPLE_FILE_NAME)).thenReturn(20.0f);

        // callback which will set the source pixel size
        when(
                ndpRead.GetSourcePixelSize(eq(SAMPLE_FILE_NAME), any(NativeLongByReference.class),
                        any(NativeLongByReference.class))).thenAnswer(new GetSourcePixelSizeCallback(20, 15));

        // call the method under test
        ImageInformation imageInformation = ndpReadWrapper.getImageInformation(SAMPLE_FILE_NAME);

        assertEquals(500, imageInformation.getImageWidthInNanometres());
        assertEquals(300, imageInformation.getImageHeightInNanometres());
        assertEquals(20, imageInformation.getImageWidthInPixels());
        assertEquals(15, imageInformation.getImageHeightInPixels());
        assertEquals(50, imageInformation.getPhysicalXPositionOfCentreInNanometres());
        assertEquals(60, imageInformation.getPhysicalYPositionOfCentreInNanometres());
        // TODO: sort out floating point precision issues
        assertEquals(20.0f, imageInformation.getSourceLensMagnification(), .5);
    }

    @Test
    public void testGetImageInformationThrowsExceptionIfGetMapMethodReturnsZeroResponseCode()
        throws FileNotFoundException, NDPReadException
    {
        // image width, height and source lens
        when(ndpRead.GetImageWidth(SAMPLE_FILE_NAME)).thenReturn(500);
        when(ndpRead.GetImageHeight(SAMPLE_FILE_NAME)).thenReturn(300);
        when(ndpRead.GetSourceLens(SAMPLE_FILE_NAME)).thenReturn(20.0f);

        // callback which will set the source pixel size
        when(
                ndpRead.GetSourcePixelSize(eq(SAMPLE_FILE_NAME), any(NativeLongByReference.class),
                        any(NativeLongByReference.class))).thenAnswer(new GetSourcePixelSizeCallback(20, 15));

        // make the GetMap method return a zero return code which indicates failure
        when(
                ndpRead.GetMap(anyString(), any(NativeLongByReference.class), any(NativeLongByReference.class),
                        any(NativeLongByReference.class), any(NativeLongByReference.class), any(ByteBuffer.class),
                        any(NativeLongByReference.class), any(NativeLongByReference.class),
                        any(NativeLongByReference.class))).thenReturn(ERROR_RESPONSE_CODE);
        when(ndpRead.GetLastErrorMessage()).thenReturn("My Error Message");
        try
        {
            ndpReadWrapper.getImageInformation(SAMPLE_FILE_NAME);
            fail("Should throw exception when NDPRead interface returns zero response code");
        }
        catch (NDPReadException e)
        {
            assertEquals("My Error Message", e.getMessageFromNdpRead());
            assertEquals("Failed to get image details through call to GetMap", e.getDetails());
        }
    }

    @Test
    public void testGetImageInformationThrowsExceptionIfGetSourcePixelsMethodReturnsZeroResponseCode()
        throws FileNotFoundException, NDPReadException
    {
        // image width, height and source lens
        when(ndpRead.GetImageWidth(SAMPLE_FILE_NAME)).thenReturn(500);
        when(ndpRead.GetImageHeight(SAMPLE_FILE_NAME)).thenReturn(300);
        when(ndpRead.GetSourceLens(SAMPLE_FILE_NAME)).thenReturn(20.0f);

        // make the GetSourcePixelSize method return a failure response code
        when(
                ndpRead.GetSourcePixelSize(eq(SAMPLE_FILE_NAME), any(NativeLongByReference.class),
                        any(NativeLongByReference.class))).thenReturn(ERROR_RESPONSE_CODE);

        // make the GetMap method return a zero return code which indicates failure
        when(
                ndpRead.GetMap(anyString(), any(NativeLongByReference.class), any(NativeLongByReference.class),
                        any(NativeLongByReference.class), any(NativeLongByReference.class), any(ByteBuffer.class),
                        any(NativeLongByReference.class), any(NativeLongByReference.class),
                        any(NativeLongByReference.class))).thenAnswer(new GetMapCallback(1, 1));
        when(ndpRead.GetLastErrorMessage()).thenReturn("My Error Message");
        try
        {
            ndpReadWrapper.getImageInformation(SAMPLE_FILE_NAME);
            fail("Should throw exception when NDPRead interface returns zero response code");
        }
        catch (NDPReadException e)
        {
            assertEquals("My Error Message", e.getMessageFromNdpRead());
            assertEquals("Failed to get pixel size through call to GetSourcePixelSize", e.getDetails());
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetImageInformationThrowsExceptionIfFileDoesNotExist() throws FileNotFoundException,
        NDPReadException
    {
        doThrow(new FileNotFoundException()).when(fileManager).checkFileExists(SAMPLE_FILE_NAME);
        ndpReadWrapper.getImageInformation(SAMPLE_FILE_NAME);
    }

    @Test
    public void testGetImageSegmentReturnsCorrectInformation() throws FileNotFoundException, NDPReadException
    {
        final byte[] byteArray = "somestring".getBytes();
        final int bufferSize = byteArray.length;
        // callback for the first call to GetImageData which will set the required buffer size
        Answer<Integer> call1Answer = new Answer<Integer>()
        {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable
            {
                // check that buffer size passed in was zero
                assertEquals(0, ((NativeLongByReference) invocation.getArguments()[8]).getValue().longValue());
                // TODO: check that the buffer was empty
                // update the buffer size to a non-zero number
                ((NativeLongByReference) invocation.getArguments()[8]).setValue(new NativeLong(bufferSize));
                return SUCCESS_RESPONSE_CODE;
            }
        };

        // callback for the second call to GetImageData which will actually fill the buffer
        Answer<Integer> call2Answer = new Answer<Integer>()
        {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable
            {
                // check that buffer size passed in was correctly updated to the real value
                Object[] arguments = invocation.getArguments();
                assertEquals(bufferSize, ((NativeLongByReference) arguments[8]).getValue().longValue());
                // TODO: check that the buffer was correctly updated to the required size

                // update the byte buffer so there's some real bytes to pass back
                ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
                buffer.put(byteArray);
                arguments[7] = buffer;
                return SUCCESS_RESPONSE_CODE;
            }
        };

        // expect first call with zero buffer size, second call with correct buffer
        when(
                ndpRead.GetImageData(eq(SAMPLE_FILE_NAME), eq(new NativeLong(10)), eq(new NativeLong(20)),
                        eq(new NativeLong(30)), eq(20.0f), any(NativeLongByReference.class),
                        any(NativeLongByReference.class), any(ByteBuffer.class), any(NativeLongByReference.class)))
                .thenAnswer(call1Answer).thenAnswer(call2Answer);

        byte[] segment = ndpReadWrapper.getImageSegment(SAMPLE_FILE_NAME, 10, 20, 30, 20.0f, 300, 200);
        assertNotNull(segment);
        assertEquals(bufferSize, segment.length);
        verify(ndpRead).SetCameraResolution(new NativeLong(300), new NativeLong(200));

    }

    @Test
    public void testGetImageSegmentThrowsExceptionIfFirstCallToInterfaceReturnsZeroResponseCode()
        throws FileNotFoundException, NDPReadException
    {
        // make the interface return an error code on the first call
        when(
                ndpRead.GetImageData(eq(SAMPLE_FILE_NAME), eq(new NativeLong(10)), eq(new NativeLong(20)),
                        eq(new NativeLong(30)), eq(20.0f), any(NativeLongByReference.class),
                        any(NativeLongByReference.class), any(ByteBuffer.class), any(NativeLongByReference.class)))
                .thenReturn(ERROR_RESPONSE_CODE);
        when(ndpRead.GetLastErrorMessage()).thenReturn("My Error Message");
        try
        {
            ndpReadWrapper.getImageSegment(SAMPLE_FILE_NAME, 10, 20, 30, 20.0f, 300, 200);
            fail("Should throw exception when NDPRead interface returns zero response code");
        }
        catch (NDPReadException e)
        {
            assertEquals("My Error Message", e.getMessageFromNdpRead());
        }
    }

    @Test
    public void testGetImageSegmentThrowsExceptionIfSecondCallToInterfaceReturnsZeroResponseCode()
        throws FileNotFoundException, NDPReadException
    {
        // make the interface return a zero return code which indicates failure - but only on 2nd call
        when(
                ndpRead.GetImageData(eq(SAMPLE_FILE_NAME), eq(new NativeLong(10)), eq(new NativeLong(20)),
                        eq(new NativeLong(30)), eq(20.0f), any(NativeLongByReference.class),
                        any(NativeLongByReference.class), any(ByteBuffer.class), any(NativeLongByReference.class)))
                .thenReturn(SUCCESS_RESPONSE_CODE).thenReturn(ERROR_RESPONSE_CODE);
        when(ndpRead.GetLastErrorMessage()).thenReturn("My Error Message");
        try
        {
            ndpReadWrapper.getImageSegment(SAMPLE_FILE_NAME, 10, 20, 30, 20.0f, 300, 200);
            fail("Should throw exception when NDPRead interface returns zero response code on second call");
        }
        catch (NDPReadException e)
        {
            assertEquals("My Error Message", e.getMessageFromNdpRead());
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetImageSegmentThrowsExceptionIfFileDoesNotExist() throws FileNotFoundException, NDPReadException
    {
        doThrow(new FileNotFoundException()).when(fileManager).checkFileExists(SAMPLE_FILE_NAME);
        ndpReadWrapper.getImageSegment(SAMPLE_FILE_NAME, 0, 0, 0, 20.0f, 200, 200);
    }

    @Test
    public void testGetMagnification()
    {
        when(ndpRead.GetSourceLens(SAMPLE_FILE_NAME)).thenReturn(20.0f);
        assertEquals(20.0f, ndpReadWrapper.getSourceLensMagnification(SAMPLE_FILE_NAME), 0.01);
    }

    @Test
    public void testCleanup()
    {
        ndpReadWrapper.cleanUp();
        verify(ndpRead).CleanUp();
    }

    @Test
    public void testCleanupIgnoresExceptions()
    {
        when(ndpRead.CleanUp()).thenThrow(new RuntimeException());
        ndpReadWrapper.cleanUp();
    }
}

class GetMapCallback implements Answer<Integer>
{

    private final long physicalX;
    private final long physicalY;

    public GetMapCallback(long physicalX, long physicalY)
    {
        this.physicalX = physicalX;
        this.physicalY = physicalY;
    }

    @Override
    public Integer answer(InvocationOnMock invocation) throws Throwable
    {
        // set the output parameters - seems to be the only sensible way to do this in Mockito
        ((NativeLongByReference) invocation.getArguments()[1]).setValue(new NativeLong(physicalX));
        ((NativeLongByReference) invocation.getArguments()[2]).setValue(new NativeLong(physicalY));
        return NDPReadWrapperImplTest.SUCCESS_RESPONSE_CODE;
    }
}

class GetSourcePixelSizeCallback implements Answer<Integer>
{
    private long pixelWidth;
    private long pixelHeight;

    public GetSourcePixelSizeCallback(long pixelWidth, long pixelHeight)
    {
        super();
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
    }

    @Override
    public Integer answer(InvocationOnMock invocation) throws Throwable
    {
        // set the output parameters - seems to be the only sensible way to do this in Mockito
        ((NativeLongByReference) invocation.getArguments()[1]).setValue(new NativeLong(pixelWidth));
        ((NativeLongByReference) invocation.getArguments()[2]).setValue(new NativeLong(pixelHeight));
        return NDPReadWrapperImplTest.SUCCESS_RESPONSE_CODE;
    }

}

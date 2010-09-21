/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.caisis;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * 
 * @version $Rev$
 */
public class TissueImageNameParserTest
{
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private TissueImageNameParser parser = new TissueImageNameParser();

    @Test
    public void testFailsIfFileNameNotInCorrectFormat() throws IOException
    {
        assertHandleFails("somename.jpg"); // not enough .
        assertHandleFails("somename.blah"); // not enough .
        assertHandleFails(".som enam e.jpg"); // . at the start
        assertHandleFails("som enam e..jpg"); // . at the end
        assertHandleFails("abcdef"); // no .
    }

    @Test
    public void testSucceedsIfFileNameInCorrectFormat() throws IOException
    {
        assertHandleWorks("abc.def.", "abc", "def"); // no extension
        assertHandleWorks("some.name.jpg", "some", "name"); // normal
        assertHandleWorks("so.me.name.jpg", "so", "me.name"); // extra .
        assertHandleWorks("so..m.e.name.jpg", "so", ".m.e.name"); // lots of extra...
        assertHandleWorks("so   me.na   me.jpg", "so   me", "na   me"); // lots of space
        assertHandleWorks("1.2.blah", "1", "2"); // another one
    }

    private void assertHandleFails(String filename) throws IOException
    {
        try
        {
            parser.getTissueBankRefAndSpecimenId(filename);
            fail("Should throw exception for invalid file name");
        }
        catch (CaisisUpdateFailedException e)
        {
            // expected
        }

    }

    private void assertHandleWorks(String filename, String tbRef, String specimenId) throws IOException
    {
        parser.getTissueBankRefAndSpecimenId(filename);
    }

}

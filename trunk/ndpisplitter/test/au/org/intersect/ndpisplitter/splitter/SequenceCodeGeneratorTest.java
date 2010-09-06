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

import org.junit.Test;

/**
 * @version $Rev$
 * 
 */
public class SequenceCodeGeneratorTest
{
    private SequenceCodeGenerator generator = new SequenceCodeGeneratorImpl();

    @Test
    public void testSequenceCodeGeneration()
    {
        assertEquals("a", generator.getSequenceCode(1));
        assertEquals("b", generator.getSequenceCode(2));
        assertEquals("c", generator.getSequenceCode(3));
        assertEquals("j", generator.getSequenceCode(10));
        assertEquals("z", generator.getSequenceCode(26));
        assertEquals("aa", generator.getSequenceCode(27));
        assertEquals("cv", generator.getSequenceCode(100));
        assertEquals("ay", generator.getSequenceCode(51));
        assertEquals("az", generator.getSequenceCode(52));
        assertEquals("ba", generator.getSequenceCode(53));
        assertEquals("zy", generator.getSequenceCode((26 * 27) - 1));
        assertEquals("zz", generator.getSequenceCode(702));
        assertEquals("aaa", generator.getSequenceCode(703));
        assertEquals("aab", generator.getSequenceCode(704));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowsExceptionForZero()
    {
        generator.getSequenceCode(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowsExceptionForNegativeNumber()
    {
        generator.getSequenceCode(-1);
    }
}

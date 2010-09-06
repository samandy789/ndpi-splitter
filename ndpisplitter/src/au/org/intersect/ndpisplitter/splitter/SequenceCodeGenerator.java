/**
 * Copyright (C) Intersect 2009.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.splitter;


/**
 * Converts numbers to alphabetic sequences - eg 1=a, 2=b, 26=z, 27=aa, 28=ab etc.
 * 
 * @version $Rev$
 */
public interface SequenceCodeGenerator
{
    /**
     * Given a positive (>0) sequence number, generate the corresponding alphabetic code
     * 
     * @param sequence
     *            a positive number to convert
     * @return the alphabetic sequence code as a string
     */
    public String getSequenceCode(int sequence);
}

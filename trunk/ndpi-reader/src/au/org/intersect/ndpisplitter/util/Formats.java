/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 
 * @version $Rev$
 */
public class Formats
{
    public static final String NEWLINE = System.getProperty("line.separator");
    public static final NumberFormat WHOLE_NUMBER_FORMATTER = new DecimalFormat("###,##0");
    public static final NumberFormat DECIMAL_FORMATTER = new DecimalFormat("###,##0.000000");

}

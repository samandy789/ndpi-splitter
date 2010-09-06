/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.ndpireader;

import com.sun.jna.Native;

/**
 * @version $Rev$
 * 
 */
public class NDPReadFactory
{
    public static final NDPRead INSTANCE = (NDPRead) Native.loadLibrary("NDPRead", NDPRead.class);
}

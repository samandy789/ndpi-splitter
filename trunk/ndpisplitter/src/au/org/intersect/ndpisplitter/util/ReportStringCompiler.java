/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.util;

import java.io.File;
import java.util.List;
import java.util.Map;

import au.org.intersect.ndpisplitter.splitter.ImageTilingException;

/**
 * @version $Rev$
 *
 */
public final class ReportStringCompiler
{
    public static final String compileReportString(Map<File, ImageTilingException> errorMap, List<File> successList)
    {
        String outputString = "";
        if (!successList.isEmpty())
        {
            outputString = "\nThe selected files have been successfully converted "
                + "and placed in the following directories:\n\n";
        } 
        for (File file : successList)
        {
            outputString += file.getPath() + "\n";
        }
        if (!errorMap.isEmpty())
        {
            String errorString = getErrorString(errorMap);
            outputString += errorString;
        }
        return outputString;
    }
    
    private static final String getErrorString(Map<File, ImageTilingException> errorMap)
    {
        String outputString = "\nSome files were not successfully converted: \n\n";
        for (File file : errorMap.keySet())
        {
            outputString += file.getAbsolutePath() + "\n\n";
        }
        return outputString;
        
    }
}

/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import au.org.intersect.ndpisplitter.splitter.ImageTilingException;

/**
 * @version $Rev$
 * 
 */
public class ReportStringCompilerTest
{
    @Test
    public void testSuccessMessageNoFailure()
    {
        String expected = "\nThe selected files have been successfully converted " 
                          + "and placed in the following directories:\n\nfile1\nfile2\n";
        Map<File, ImageTilingException> errorMap = new HashMap<File, ImageTilingException>();
        List<File> successList = new ArrayList<File>();
        successList.add(new File("file1"));
        successList.add(new File("file2"));

        String reportString = ReportStringCompiler.compileReportString(errorMap, successList);
        assertEquals("Incorrect Report String", expected, reportString);
    }
    
    @Test
    public void testSuccessMessageWithFailure()
    {
        String expected1 = "\nThe selected files have been successfully converted " 
                          + "and placed in the following directories:\n\nfile1\nfile2\n";
        
        // compile exception list
        Map<File, ImageTilingException> errorMap = new HashMap<File, ImageTilingException>();
        File file1 = new File("file1");       
        IOException cause1 = new IOException("File Error 1");
        errorMap.put(file1, new ImageTilingException(cause1));
        
        // compile success list
        List<File> successList = new ArrayList<File>();
        successList.add(new File("file1"));
        successList.add(new File("file2"));

        String reportString = ReportStringCompiler.compileReportString(errorMap, successList);
        assertTrue("Incorrect Report String", reportString.startsWith(expected1));
    }
    
    @Test
    public void testNoSuccessNoFailure()
    {
        String expected = "";
        Map<File, ImageTilingException> errorMap = new HashMap<File, ImageTilingException>();
        List<File> successList = new ArrayList<File>();

        String reportString = ReportStringCompiler.compileReportString(errorMap, successList);
        assertEquals("Incorrect Report String", expected, reportString);
    }
    
    @Test
    public void testNoSuccessWithFailure()
    {
        String expected1 = "\nSome files were not successfully converted:";
        
        Map<File, ImageTilingException> errorMap = new HashMap<File, ImageTilingException>();
        File file1 = new File("file1");
        File file2 = new File("file2");
        
        // compile exception list
        IOException cause1 = new IOException("File Error 1");
        errorMap.put(file1, new ImageTilingException(cause1));
        IOException cause2 = new IOException("File Error 2");
        errorMap.put(file2, new ImageTilingException(cause2));
        
        // empty success file list
        List<File> successList = new ArrayList<File>();

        String reportString = ReportStringCompiler.compileReportString(errorMap, successList);
        assertTrue("Incorrect Report String", reportString.startsWith(expected1));
    }
}

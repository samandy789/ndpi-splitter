/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.processing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @version $Rev$
 */
public class InterestingnessLogger
{
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0#######");
    private static final String LOG_MESSAGE = "Failed to log interestingness";
    private static final Logger LOG = Logger.getLogger(InterestingnessLogger.class);

    /**
     * Creates a new log file in the given directory.
     * 
     * @param outputDirectory
     */
    public static void createNewLog(File outputDirectory)
    {
        String text = tabDelimit("Tile name", "Interesting", "Rating", "Threshold");
        writeLineToFile(outputDirectory.getAbsolutePath(), text, false);
    }

    /**
     * Writes a line to the log file in the given directory about the interestingness of the tile in question
     * 
     * @param outputFileName
     * @param outputDirectory
     * @param ratio
     * @param interestThreshold
     */
    public static void logTile(String outputFileName, String outputDirectory, boolean interesting, double ratio,
            double interestThreshold)
    {
        String ratioAsString = DECIMAL_FORMAT.format(ratio);
        String thresholdAsString = DECIMAL_FORMAT.format(interestThreshold);
        String text = tabDelimit(outputFileName, Boolean.toString(interesting), ratioAsString, thresholdAsString);
        writeLineToFile(outputDirectory, text, true);
    }

    private static String tabDelimit(String... items)
    {
        StringBuffer buffer = new StringBuffer();
        for (String item : items)
        {
            buffer.append(item);
            buffer.append("\t");
        }
        return buffer.toString();
    }

    private static String getLogFilePath(String outputDirectory)
    {
        return outputDirectory + File.separator + "log.txt";
    }

    private static void writeLineToFile(String outputDirectory, String text, boolean append)
    {
        String logFilePath = getLogFilePath(outputDirectory);
        FileWriter outFile = null;
        PrintWriter printWriter = null;
        try
        {
            outFile = new FileWriter(new File(logFilePath), append);
            printWriter = new PrintWriter(outFile);
            printWriter.println(text);
            printWriter.flush();
        }
        catch (FileNotFoundException e)
        {
            LOG.warn(LOG_MESSAGE, e);
        }
        catch (IOException e)
        {
            LOG.warn(LOG_MESSAGE, e);
        }
        finally
        {
            IOUtils.closeQuietly(outFile);
            IOUtils.closeQuietly(printWriter);
        }
    }
}

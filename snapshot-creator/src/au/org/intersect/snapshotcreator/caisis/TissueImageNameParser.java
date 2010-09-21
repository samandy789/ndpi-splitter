/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.caisis;

/**
 * 
 * @version $Rev$
 */
public class TissueImageNameParser
{
    public String[] getTissueBankRefAndSpecimenId(String fileName)
    {
        int indexOfLastPeriod = fileName.lastIndexOf('.');
        if (indexOfLastPeriod < 0)
        {
            reportBadFilename(fileName);
        }

        String beforeExtension = fileName.substring(0, indexOfLastPeriod);
        int indexOfFirstPeriod = beforeExtension.indexOf('.');
        if (indexOfFirstPeriod < 0)
        {
            reportBadFilename(fileName);
        }

        String tbRefNum = beforeExtension.substring(0, indexOfFirstPeriod);
        String specimenId = beforeExtension.substring(indexOfFirstPeriod + 1);

        if (tbRefNum.length() < 1 || specimenId.length() < 1)
        {
            reportBadFilename(fileName);
        }
        return new String[] {tbRefNum, specimenId};

    }

    private void reportBadFilename(String fileName)
    {
        throw new CaisisUpdateFailedException("File name did not match the expected format of [TBRef.SpecimenRef.jpg]."
                + " Snapshot cannot be added to Caisis. Filename was " + fileName);
    }

}

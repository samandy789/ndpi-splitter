/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.caisis;

import java.io.File;

import org.apache.log4j.Logger;

import au.org.intersect.snapshotcreator.config.SnapshotCreatorProperties;
import au.org.intersect.snapshotcreator.file.FileHandler;

/**
 * @version $Rev$
 */
public class CaisisUpdaterHandler implements FileHandler
{
    private static final Logger LOG = Logger.getLogger(CaisisUpdaterHandler.class);
    private final CaisisDao caisisDao;
    private String imageUrlPattern;
    private final TissueImageNameParser tissueImageNameParser;

    public CaisisUpdaterHandler(SnapshotCreatorProperties properties, CaisisDao caisisDao,
            TissueImageNameParser tissueImageNameParser)
    {
        this.caisisDao = caisisDao;
        this.tissueImageNameParser = tissueImageNameParser;
        imageUrlPattern = properties.getImageUrlPattern();
        if (!imageUrlPattern.contains("${fileName}"))
        {
            throw new IllegalArgumentException(
                    "Image name pattern did not contain the required placeholder ${fileName} "
                            + "for where the filename should go. This property must contain the placeholder "
                            + "- for example http://localhost/snapshot-viewer/TissueImage.aspx?file=${fileName}");
        }
        caisisDao.setUpdateSql(properties.getCaisisUpdateSql());
        caisisDao.setCheckAtLeastOneRowUpdated(properties.isShouldCheckAtLeastOneRowUpdated());
    }

    @Override
    public void handleFile(File file)
    {
        LOG.info("Handling file " + file.getAbsolutePath());
        String fileName = file.getName();
        String[] fileNameParts = tissueImageNameParser.getTissueBankRefAndSpecimenId(fileName);

        String imageLocation = imageUrlPattern.replace("${fileName}", fileNameParts[0] + "." + fileNameParts[1]);
        String tissueBankRef = fileNameParts[0];
        String sampleRef = fileNameParts[1];
        caisisDao.updateImageURL(tissueBankRef, sampleRef, imageLocation);
    }
}

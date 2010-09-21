/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.snapshotcreator.caisis;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

/**
 * 
 * @version $Rev$
 */
public class CaisisDao extends SimpleJdbcDaoSupport
{
    private static final String SPECIMEN_ID_PARAM = ":specimen_id";
    private static final String IMAGE_LOCATION_PARAM = ":image_location";
    private static final String TB_NUMBER_PARAM = ":tb_number";
    private static final Logger LOG = Logger.getLogger(CaisisDao.class);
    private String caisisUpdateSql;
    private boolean shouldCheckAtLeastOneRowUpdated;

    public void updateImageURL(String tissueBankRef, String sampleRef, String imageUrl)
        throws CaisisUpdateFailedException
    {
        if (caisisUpdateSql == null)
        {
            throw new IllegalStateException("caisisUpdateSql must be configured. Call setUpdateSql first.");
        }
        
        String sqlStatement = caisisUpdateSql;
        sqlStatement = sqlStatement.replace(":image_location", imageUrl);
        sqlStatement = sqlStatement.replace(":tb_number", tissueBankRef);
        sqlStatement = sqlStatement.replace(":specimen_id", sampleRef);
        
        LOG.info("Executing SQL " + sqlStatement);
        int result = super.getSimpleJdbcTemplate().update(sqlStatement);
        if (shouldCheckAtLeastOneRowUpdated && result < 1)
        {
            throw new CaisisUpdateFailedException("Expected to update at least 1 row in the " + "database but " + result
                    + " rows were updated");
        }
    }

    public void setUpdateSql(String caisisUpdateSql)
    {
        if (!caisisUpdateSql.contains(TB_NUMBER_PARAM) || !caisisUpdateSql.contains(IMAGE_LOCATION_PARAM)
                || !caisisUpdateSql.contains(SPECIMEN_ID_PARAM))
        {
            throw new IllegalArgumentException("SQL to update Caisis must include parameter placeholders "
                    + ":image_location, :tb_number, :specimen_id. Configured SQL was " + caisisUpdateSql);
        }
        this.caisisUpdateSql = caisisUpdateSql;
    }

    public void setCheckAtLeastOneRowUpdated(boolean shouldCheckAtLeastOneRowUpdated)
    {
        this.shouldCheckAtLeastOneRowUpdated = shouldCheckAtLeastOneRowUpdated;
    }
}

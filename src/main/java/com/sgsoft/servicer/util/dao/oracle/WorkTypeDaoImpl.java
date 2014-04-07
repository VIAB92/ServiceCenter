package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.WorkType;
import com.sgsoft.servicer.util.dao.WorkTypeDAO;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;

import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class WorkTypeDaoImpl extends DAOCloseHelper implements WorkTypeDAO {
    @Override
    public WorkType getById(Integer id) throws DBException {
        return null;
    }

    @Override
    public int saveWorkType(WorkType workType) throws DBException {
        return 0;
    }

    @Override
    public int updateWorkType(WorkType workType) throws DBException {
        return 0;
    }

    @Override
    public int deleteById(Integer id) throws DBException {
        return 0;
    }

    @Override
    public List<WorkType> getWorkTypesOrderByName() throws DBException {
        return null;
    }
}

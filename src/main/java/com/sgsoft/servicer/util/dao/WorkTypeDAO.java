package com.sgsoft.servicer.util.dao;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.WorkType;

import java.util.List;

/**
 * Created by Viktor Rotar on 04.04.14.
 */
public interface WorkTypeDAO {

    public WorkType getById(Integer id) throws DBException;

    public int saveWorkType(WorkType workType) throws DBException;

    public int updateWorkType(WorkType workType) throws DBException;

    public int deleteById(Integer id) throws DBException;

    public List<WorkType> getWorkTypesOrderByName() throws DBException;

    public int addWorkTypeToBid(Integer bidId, Integer workTypeId) throws DBException;

    public int removeWorkTypeFromBid(Integer bidId, Integer workTypeId) throws DBException;

    public List<WorkType> getWorkTypesByBid(Integer bidId) throws DBException;
}

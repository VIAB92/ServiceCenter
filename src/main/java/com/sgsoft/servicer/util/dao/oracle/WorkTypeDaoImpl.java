package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.DBManager;
import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.WorkType;
import com.sgsoft.servicer.util.dao.WorkTypeDAO;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;
import com.sun.java.swing.plaf.windows.resources.windows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class WorkTypeDaoImpl extends DAOCloseHelper implements WorkTypeDAO {

    private static final String TYPE_ID_VALUE = "TYPE_ID";
    private static final String TYPE_NAME_VALUE = "TYPE_NAME";
    private static final String TYPE_PRICE_VALUE = "PRICE";

    private static final String CREATE_WORK_TYPE_QUERY = "INSERT INTO Work_type VALUES(work_seq.NEXTVAL, ?, ?)";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM Work_type WHERE "+TYPE_ID_VALUE+" = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM Work_type WHERE "+TYPE_ID_VALUE+" = ?";
    private static final String GET_ALL_TYPE_QUERY = "SELECT * FROM Work_type ORDER BY "+TYPE_NAME_VALUE;
    private static final String UPDATE_QUERY = "UPDATE Work_type SET "+TYPE_NAME_VALUE+" = ?, "+TYPE_PRICE_VALUE+" = ? WHERE "+TYPE_ID_VALUE+" = ?";

    private DBManager dbManager;

    public WorkTypeDaoImpl(DBManager dbManager)
    {
        this.dbManager = dbManager;
    }

    @Override
    public WorkType getById(Integer id) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        WorkType workType = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_ID_QUERY);
            preparedStatement.setInt(1, id.intValue());
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                workType = getWorkTypeEntity(resultSet);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
        return workType;
    }

    private WorkType getWorkTypeEntity(ResultSet resultSet) throws SQLException {

        WorkType workType = new WorkType();
        workType.setId(resultSet.getInt(TYPE_ID_VALUE));
        workType.setName(resultSet.getString(TYPE_NAME_VALUE));
        workType.setPrice(resultSet.getDouble(TYPE_PRICE_VALUE));
        return workType;

    }

    @Override
    public int saveWorkType(WorkType workType) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(CREATE_WORK_TYPE_QUERY);
            preparedStatement.setString(1, workType.getName());
            preparedStatement.setDouble(2, workType.getPrice().doubleValue());

            result = preparedStatement.executeUpdate();
            dbManager.commit();
        }
        catch (SQLException ex)
        {
            dbManager.rollback();
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeStatement(preparedStatement);
        }
        return result;
    }

    @Override
    public int updateWorkType(WorkType workType) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(UPDATE_QUERY);
            preparedStatement.setString(1, workType.getName());
            preparedStatement.setDouble(2, workType.getPrice());
            preparedStatement.setInt(3, workType.getId());
            result = preparedStatement.executeUpdate();
            dbManager.commit();
        }
        catch(SQLException ex)
        {
            dbManager.rollback();
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeStatement(preparedStatement);
        }
        return result;
    }

    @Override
    public int deleteById(Integer id) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(DELETE_BY_ID_QUERY);
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeUpdate();
            dbManager.commit();
        }
        catch (SQLException ex)
        {
            dbManager.rollback();
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeStatement(preparedStatement);
        }
        return result;
    }

    @Override
    public List<WorkType> getWorkTypesOrderByName() throws DBException {
        ResultSet resultSet = dbManager.execSQL(GET_ALL_TYPE_QUERY);
        List<WorkType> workTypes = new ArrayList<WorkType>();
        try
        {
            while(resultSet.next())
            {
                workTypes.add(getWorkTypeEntity(resultSet));
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
        }
        return workTypes;
    }
}

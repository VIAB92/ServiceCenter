package com.sgsoft.servicer.util.dao.closer;

import com.sgsoft.servicer.db.exception.DBException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public abstract class DAOCloseHelper {
    protected void closeResultSet(ResultSet resultSet) throws DBException
    {
        if (resultSet != null)
        {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage(), e);
            }
        }
    }

    protected void closeStatement(Statement statement) throws DBException {
        if (statement != null)
        {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage(), e);
            }
        }
    }
}

package com.sgsoft.servicer.db;

import com.sgsoft.servicer.db.exception.DBException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Viktor Rotar on 04.04.14.
 */
public interface DBManager {

    public void executeUpdate(String sql) throws DBException;

    public ResultSet execSQL(String sql) throws DBException;

    public PreparedStatement preparedStatement(String sql) throws DBException;

    public void close() throws DBException;

    public void commit() throws DBException;

    public void rollback() throws DBException;

}

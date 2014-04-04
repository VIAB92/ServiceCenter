package com.sgsoft.servicer.db.impl;

import com.sgsoft.servicer.db.DBManager;
import com.sgsoft.servicer.db.exception.DBException;

import java.sql.*;
import java.util.Locale;

/**
 * Created by Viktor Rotar on 04.04.14.
 */
public class OracleDBManager implements DBManager {

    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

    private Connection connection = null;
    private Statement statement = null;

    public OracleDBManager(String url, String login, String password) throws DBException
    {
        try
        {
            Locale.setDefault(Locale.ENGLISH);
            Class.forName(DRIVER).newInstance();
            createConnection(url, login, password);
        }
        catch (ClassNotFoundException ex)
        {
            throw new RuntimeException("Class "+DRIVER+" not found", ex);
        } catch (InstantiationException e) {
            throw new RuntimeException("Driver " + DRIVER + " class InstantiationException.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("IllegalAccess for " + DRIVER + " class.", e);
        }

    }

    private void createConnection(String url, String login, String password) throws DBException {
        try
        {
            connection = DriverManager.getConnection(url, login, password);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public void executeUpdate(String sql) throws DBException {
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public ResultSet execSQL(String sql) throws DBException {
        try {
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public PreparedStatement preparedStatement(String sql) throws DBException {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public synchronized void close() throws DBException {
        closeConnectionAndStatement();
    }

    private void closeConnectionAndStatement() throws DBException {
        try
        {
            if (statement != null)
            {
                statement.close();
            }
            if(connection != null)
            {
                connection.close();
            }
        }
        catch(SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    @Override
    public void commit() throws DBException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public void rollback() throws DBException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DBException(e.getMessage(), e);
        }
    }
}

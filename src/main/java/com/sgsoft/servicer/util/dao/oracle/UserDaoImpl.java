package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.DBManager;
import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.Role;
import com.sgsoft.servicer.entity.User;
import com.sgsoft.servicer.util.dao.StateDAO;
import com.sgsoft.servicer.util.dao.UserDAO;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class UserDaoImpl extends DAOCloseHelper implements UserDAO {

    protected static final String USER_ID_VALUE = "USER_ID";
    protected static final String USER_RIGHT_VALUE = "USER_TYPE";
    protected static final String USER_NAME_VALUE = "FULLNAME";
    protected static final String USER_LOGIN_VALUE = "LOGIN";
    protected static final String USER_PASSWORD_VALUE = "PASSWORD";
    protected static final String USER_EMAIL_VALUE = "EMAIL";
    protected static final String USER_COUNT_VALUE = "count";

    protected static final String CREATE_USER_QUERY = "INSERT INTO User_info VALUES(user_seq.NEXTVAL, ?, ?, ?, ?, (SELECT Right_id FROM User_right WHERE Right_name = ?))";
    protected static final String UPDATE_USER_QUERY = "UPDATE User_info SET "+USER_RIGHT_VALUE+" = (SELECT Right_id FROM User_right WHERE Right_name = ?), " +
            USER_NAME_VALUE + " = ?, " + USER_PASSWORD_VALUE + " = ?, " + USER_EMAIL_VALUE + " = ? WHERE " + USER_ID_VALUE + " = ?";
    protected static final String GET_BY_ID_QUERY = "SELECT * FROM User_info WHERE "+USER_ID_VALUE+ " = ?";
    protected static final String GET_BY_LOGIN_AND_PWD_QUERY = "SELECT * FROM User_info WHERE "+USER_LOGIN_VALUE + " = ? AND "+USER_PASSWORD_VALUE + " = ?";
    protected static final String GET_ALL_USERS_QUERY = "SELECT * FROM User_info ORDER BY "+USER_LOGIN_VALUE;
    protected static final String EXISTS_BY_LOGIN_QUERY = "SELECT COUNT() " + USER_COUNT_VALUE + " FROM User_info WHERE "+USER_LOGIN_VALUE+" = ?";

    private DBManager dbManager;
    private StateDAO stateDAO;

    public UserDaoImpl(DBManager dbManager, StateDAO stateDAO)
    {
        this.dbManager = dbManager;
        this.stateDAO = stateDAO;
    }

    @Override
    public User getByLoginAndPassword(String login, String password) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_LOGIN_AND_PWD_QUERY);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                user = getUserEntity(resultSet);
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
        return user;
    }

    private User getUserEntity(ResultSet resultSet) throws SQLException, DBException {
        User user = new User();
        user.setId(resultSet.getInt(USER_ID_VALUE));
        user.setFullname(resultSet.getString(USER_NAME_VALUE));
        user.setLogin(resultSet.getString(USER_LOGIN_VALUE));
        user.setPassword(resultSet.getString(USER_PASSWORD_VALUE));
        user.setEmail(resultSet.getString(USER_EMAIL_VALUE));

        Role userRole = Role.getRoleById(resultSet.getInt(USER_RIGHT_VALUE));
        user.setRole(userRole);
        user.setStatesVisibleList(stateDAO.getStatesByUserRole(userRole));

        return user;
    }

    @Override
    public User getById(Integer id) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_ID_QUERY);
            preparedStatement.setInt(1, id.intValue());
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                user = getUserEntity(resultSet);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeStatement(preparedStatement);
            closeResultSet(resultSet);
        }
        return user;
    }

    @Override
    public int saveUser(User user) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(CREATE_USER_QUERY);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFullname());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getRole().getName());

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
    public List<User> getUsersOrderByLogin() throws DBException {
        ResultSet resultSet = dbManager.execSQL(GET_ALL_USERS_QUERY);
        List<User> users = new ArrayList<User>();
        try
        {
            while (resultSet.next())
            {
                users.add(getUserEntity(resultSet));
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
        }
        return users;

    }

    @Override
    public int updateUser(User user) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(UPDATE_USER_QUERY);
            preparedStatement.setString(1, user.getRole().getName());
            preparedStatement.setString(2, user.getFullname());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setInt(5, user.getId().intValue());

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
    public boolean isExists(String login) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean exists = false;
        try
        {
            preparedStatement = dbManager.preparedStatement(EXISTS_BY_LOGIN_QUERY);
            preparedStatement.setString(1, login);
            resultSet = preparedStatement.executeQuery();
            exists = checkExisting(resultSet);
        }
        catch (SQLException ex)
        {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
        return exists;
    }

    private boolean checkExisting(ResultSet resultSet) throws SQLException {
        int count = resultSet.getInt(USER_COUNT_VALUE);
        if (count != 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}

package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.DBManager;
import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.Client;
import com.sgsoft.servicer.util.dao.ClientDAO;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class ClientDaoImpl extends DAOCloseHelper implements ClientDAO {

    private static final String CLIENT_ID_VALUE = "CLIENT_ID";
    private static final String CLIENT_NAME_VALUE = "FULLNAME";
    private static final String CLIENT_CITY_NAME_VALUE = "CITY";
    private static final String CLIENT_TELEPHONE_VALUE = "TELEPHONE";
    private static final String CLIENT_EMAIL_VALUE = "EMAIL";
    private static final String NUMBER_NAME = "quantity";

    private static final String GET_BY_ID_QUERY = "SELECT * FROM Client WHERE "+CLIENT_ID_VALUE+" = ?";
    private static final String GET_ALL_CLIENTS_QUERY = "SELECT * FROM Client";
    private static final String IS_EXISTS_QUERY = "SELECT COUNT(*) "+NUMBER_NAME+" FROM Client WHERE "+CLIENT_NAME_VALUE+" = ? AND "+
            CLIENT_EMAIL_VALUE+" = ? AND "+CLIENT_TELEPHONE_VALUE+" = ?";
    private static final String GET_BY_UNIQUE_PARAMS_QUERY= "SELECT * FROM Client WHERE "+CLIENT_NAME_VALUE+" = ? AND "+
            CLIENT_EMAIL_VALUE+" = ? AND "+CLIENT_TELEPHONE_VALUE+" = ?";
    private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client VALUES(client_seq.NEXTVAL, ?, ?, ?, ?)";

    private DBManager dbManager;

    public ClientDaoImpl(DBManager dbManager)
    {
        this.dbManager = dbManager;
    }

    @Override
    public List<Client> findClientsByName(String name) throws DBException {
        return null;
    }

    @Override
    public Client findById(Integer id) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Client client = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_ID_QUERY);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                client = getClientEntity(resultSet);
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
        return client;
    }

    private Client getClientEntity(ResultSet resultSet) throws SQLException {
        Client client = new Client();
        client.setId(resultSet.getInt(CLIENT_ID_VALUE));
        client.setFullName(resultSet.getString(CLIENT_NAME_VALUE));
        client.setCity(resultSet.getString(CLIENT_CITY_NAME_VALUE));
        client.setEmail(resultSet.getString(CLIENT_EMAIL_VALUE));
        client.setTelephone(resultSet.getString(CLIENT_TELEPHONE_VALUE));
        return client;
    }

    @Override
    public int saveClient(Client client) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(CREATE_CLIENT_QUERY);
            preparedStatement.setString(1, client.getFullName());
            preparedStatement.setString(2, client.getCity());
            preparedStatement.setString(3, client.getTelephone());
            preparedStatement.setString(4, client.getEmail());
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
    public boolean isExists(String name, String email, String telephone) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean exists = false;
        try
        {
            preparedStatement = dbManager.preparedStatement(IS_EXISTS_QUERY);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, telephone);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                exists = checkExisting(resultSet);
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
        return exists;
    }

    private boolean checkExisting(ResultSet resultSet) throws SQLException {
        int count = resultSet.getInt(NUMBER_NAME);
        if (count > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public Client findByUniqueParams(String name, String email, String telephone) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Client client = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_UNIQUE_PARAMS_QUERY);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, telephone);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                client = getClientEntity(resultSet);
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
        return client;
    }

    @Override
    public List<Client> getAllClients() throws DBException {
        ResultSet resultSet = dbManager.execSQL(GET_ALL_CLIENTS_QUERY);
        List<Client> clients = new ArrayList<Client>();
        try
        {
            while(resultSet.next())
            {
                clients.add(getClientEntity(resultSet));
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
        }
        return clients;
    }
}

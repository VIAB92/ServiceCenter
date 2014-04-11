package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.DBManager;
import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.Component;
import com.sgsoft.servicer.util.dao.ComponentDAO;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class ComponentDaoImpl extends DAOCloseHelper implements ComponentDAO {

    private static final String COMPONENT_ID_VALUE = "COMPONENT_ID";
    private static final String COMPONENT_NAME_VALUE = "COMPONENT_NAME";
    private static final String COMPONENT_DESCRIPTION_VALUE = "COMPONENT_DESCRIPTION";
    private static final String COMPONENT_PRICE_VALUE = "PRICE";
    private static final String ORDER_ID_VALUE = "ORDER_ID";
    private static final String BID_ID_VALUE = "BID_ID";

    private static final String GET_BY_ID_QUERY = "SELECT * FROM Component WHERE "+COMPONENT_ID_VALUE+" = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM Component WHERE "+COMPONENT_ID_VALUE+" = ?";
    private static final String GET_ALL_COMPONENTS_QUERY = "SELECT * FROM Components ORDER BY "+COMPONENT_NAME_VALUE;
    private static final String CREATE_COMPONENT_QUERY = "INSERT INTO Component VALUES(component_seq.NEXTVAL, ?, ?, ?)";
    private static final String UPDATE_COMPONENT_QUERY = "UPDATE Component SET "+COMPONENT_NAME_VALUE+" = ?, "+COMPONENT_DESCRIPTION_VALUE+" = ?, "+COMPONENT_PRICE_VALUE
            +" = ? WHERE "+COMPONENT_ID_VALUE+" = ?";
    private static final String GET_COMPONENTS_BY_ORDER_QUERY = "SELECT "+COMPONENT_ID_VALUE+" FROM Order_component WHERE "+
            ORDER_ID_VALUE+" = ?";
    private static final String ADD_COMPONENT_TO_BID_QUERY = "INSERT INTO Component_economics VALUES(?, ?)";
    private static final String REMOVE_COMPONENT_FROM_BID_QUERY = "DELETE FROM Component_economics WHERE "+BID_ID_VALUE+" = ? AND "+ORDER_ID_VALUE+" = ?";
    private static final String GET_COMPONENTS_BY_BID_QUERY = "SELECT * FROM Component_economics WHERE "+BID_ID_VALUE+" = ?";
    private static final String ADD_COMPONENTS_QUERY = "INSERT INTO Order_component VALUES(?, ?)";
    private static final String DELETE_COMPONENTS_QUERY = "DELETE FROM Order_component WHERE "+ORDER_ID_VALUE+" = ? AND "+
            COMPONENT_ID_VALUE + " = ?";

    private DBManager dbManager;

    public ComponentDaoImpl(DBManager dbManager)
    {
        this.dbManager = dbManager;
    }

    @Override
    public Component getById(Integer id) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Component component  = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_ID_QUERY);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                component = getComponentEntity(resultSet);
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
        return component;
    }

    private Component getComponentEntity(ResultSet resultSet) throws SQLException {
        Component component = new Component();
        component.setId(resultSet.getInt(COMPONENT_ID_VALUE));
        component.setName(resultSet.getString(COMPONENT_NAME_VALUE));
        component.setDescription(resultSet.getString(COMPONENT_DESCRIPTION_VALUE));
        component.setPrice(resultSet.getDouble(COMPONENT_PRICE_VALUE));
        return component;
    }

    @Override
    public int saveComponent(Component component) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(CREATE_COMPONENT_QUERY);
            preparedStatement.setString(1, component.getName());
            preparedStatement.setString(2, component.getDescription());
            preparedStatement.setDouble(3, component.getPrice());
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
    public int updateComponent(Component component) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(UPDATE_COMPONENT_QUERY);
            preparedStatement.setString(1, component.getName());
            preparedStatement.setString(2, component.getDescription());
            preparedStatement.setDouble(3, component.getPrice());
            preparedStatement.setInt(4, component.getId());
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
    public List<Component> getComponentsOrderByName() throws DBException {
        ResultSet resultSet = dbManager.execSQL(GET_ALL_COMPONENTS_QUERY);
        List<Component> components = new ArrayList<Component>();
        try
        {
            while(resultSet.next())
            {
                components.add(getComponentEntity(resultSet));
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
        }
        return components;
    }

    @Override
    public List<Component> getComponentsByOrderId(Integer id) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Component> components= new ArrayList<Component>();
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_COMPONENTS_BY_ORDER_QUERY);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                components.add(getComponentEntityById(resultSet));
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
        return components;
    }

    @Override
    public List<Component> getComponentsByBid(Integer bidId) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Component> components = new ArrayList<Component>();
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_COMPONENTS_BY_BID_QUERY);
            preparedStatement.setInt(1, bidId);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                components.add(getComponentEntityById(resultSet));
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
        return components;
    }

    @Override
    public int addComponentToBid(Integer bidId, Integer componentId) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(ADD_COMPONENT_TO_BID_QUERY);
            preparedStatement.setInt(1, bidId);
            preparedStatement.setInt(2, componentId);
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
        return  result;
    }

    @Override
    public int removeComponentFromBid(Integer bidId, Integer componentId) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(REMOVE_COMPONENT_FROM_BID_QUERY);
            preparedStatement.setInt(1, bidId);
            preparedStatement.setInt(2, componentId);
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
    public int addComponentToOrder(Integer orderId, Integer componentId) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(ADD_COMPONENTS_QUERY);
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, componentId);
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
        return  result;
    }

    @Override
    public int removeComponentFromOrder(Integer orderId, Integer componentId) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(DELETE_COMPONENTS_QUERY);
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, componentId);
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

    private Component getComponentEntityById(ResultSet resultSet) throws SQLException, DBException {
        int componentId = resultSet.getInt(COMPONENT_ID_VALUE);
        Component component = this.getById(componentId);
        return component;
    }
}

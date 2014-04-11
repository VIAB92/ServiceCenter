package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.DBManager;
import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.Component;
import com.sgsoft.servicer.entity.Order;
import com.sgsoft.servicer.entity.User;
import com.sgsoft.servicer.util.dao.ComponentDAO;
import com.sgsoft.servicer.util.dao.OrderDAO;
import com.sgsoft.servicer.util.dao.UserDAO;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class OrderDaoImpl extends DAOCloseHelper implements OrderDAO {

    private static final String ORDER_ID_VALUE = "ORDER_ID";
    private static final String ORDER_DATE_VALUE = "ORDER_DATE";
    private static final String ORDER_DETAILS_VALUE = "DETAILS";
    private static final String ORDER_USER_ID_VALUE = "USER_ID";
    private static final String COMPONENT_ID_VALUE = "COMPONENT_ID";

    private static final String GET_BY_ID_QUERY = "SELECT * FROM Order_info WHERE "+ORDER_ID_VALUE+" = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM Order_info WHERE "+ORDER_ID_VALUE+" = ?";
    private static final String CREATE_ORDER_QUERY = "INSERT INTO Order_info VALUES(order_seq.NEXTVAL, ?, ?, ?, ?)";
    private static final String UPDATE_ORDER_QUERY = "UPDATE Order_info SET "+ORDER_DATE_VALUE+" = ?, "+ORDER_DETAILS_VALUE+
            " = ?, "+ORDER_USER_ID_VALUE + " = ? WHERE "+ORDER_ID_VALUE+" = ?";
    private static final String GET_BY_DATE_QUERY = "SELECT * FROM Order_info WHERE "+ORDER_DATE_VALUE+" = ?";
    private static final String GET_BY_USER_QUERY = "SELECT * FROM Order_info WHERE "+ORDER_USER_ID_VALUE+" = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM Order_info ORDER BY "+ORDER_ID_VALUE;

    private DBManager dbManager;
    private ComponentDAO componentDAO;
    private UserDAO userDAO;

    public OrderDaoImpl(DBManager dbManager, ComponentDAO componentDAO, UserDAO userDAO)
    {
        this.dbManager = dbManager;
        this.componentDAO = componentDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Order getById(Integer id) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Order order = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_ID_QUERY);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                order = getOrderEntity(resultSet);
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
        return order;
    }

    private Order getOrderEntity(ResultSet resultSet) throws SQLException, DBException {
        Order order = new Order();
        Integer orderId = resultSet.getInt(ORDER_ID_VALUE);
        order.setId(orderId);
        order.setDetails(resultSet.getString(ORDER_DETAILS_VALUE));
        order.setOrderDate(resultSet.getDate(ORDER_DATE_VALUE));
        order.setUser(userDAO.getById(resultSet.getInt(ORDER_USER_ID_VALUE)));
        order.setComponents(componentDAO.getComponentsByOrderId(orderId));
        return order;
    }

    @Override
    public int saveOrder(Order order) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(CREATE_ORDER_QUERY);
            preparedStatement.setDate(1, new java.sql.Date(order.getOrderDate().getTime()));
            preparedStatement.setString(2, order.getDetails());
            preparedStatement.setInt(3, order.getUser().getId());
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
    public int updateOrder(Order order) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(UPDATE_ORDER_QUERY);
            preparedStatement.setDate(1, new java.sql.Date(order.getOrderDate().getTime()));
            preparedStatement.setString(2, order.getDetails());
            preparedStatement.setInt(3, order.getUser().getId());
            preparedStatement.setInt(4, order.getId());
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
    public List<Order> findByDate(Date date) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Order> orders = new ArrayList<Order>();
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_DATE_QUERY);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                orders.add(getOrderEntity(resultSet));
            }
        }
        catch (SQLException ex)
        {
            throw  new DBException(ex.getMessage(), ex);
        }
        finally {
            closeStatement(preparedStatement);
            closeResultSet(resultSet);
        }
        return orders;
    }

    @Override
    public List<Order> getByUser(User user) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Order> orders = new ArrayList<Order>();
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_USER_QUERY);
            preparedStatement.setInt(1, user.getId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                orders.add(getOrderEntity(resultSet));
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
        return orders;
    }

    @Override
    public List<Order> getOrderListOrderById() throws DBException {
        ResultSet resultSet = dbManager.execSQL(GET_ALL_QUERY);
        List<Order> orders = new ArrayList<Order>();
        try
        {
            while (resultSet.next())
            {
                orders.add(getOrderEntity(resultSet));
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
        }
        return orders;
    }

}

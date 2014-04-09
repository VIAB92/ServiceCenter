package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.DBManager;
import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.Order;
import com.sgsoft.servicer.entity.User;
import com.sgsoft.servicer.util.dao.ComponentDAO;
import com.sgsoft.servicer.util.dao.OrderDAO;
import com.sgsoft.servicer.util.dao.UserDAO;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;

import java.util.Date;
import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class OrderDaoImpl extends DAOCloseHelper implements OrderDAO {

    public static final String ORDER_ID_VALUE = "ORDER_ID";
    public static final String ORDER_DATE_VALUE = "ORDER_DATE";
    public static final String ORDER_DETAILS_VALUE = "DETAILS";
    public static final String ORDER_USER_ID_VALUE = "USER_ID";



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
        return null;
    }

    @Override
    public int saveOrder(Order order) throws DBException {
        return 0;
    }

    @Override
    public int updateOrder(Order order) throws DBException {
        return 0;
    }

    @Override
    public int deleteById(Integer id) throws DBException {
        return 0;
    }

    @Override
    public List<Order> findByDate(Date date) throws DBException {
        return null;
    }

    @Override
    public List<Order> getByUser(User user) throws DBException {
        return null;
    }

    @Override
    public List<Order> getOrderListOrderById() throws DBException {
        return null;
    }
}

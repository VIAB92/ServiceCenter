package com.sgsoft.servicer.util.dao;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.Component;
import com.sgsoft.servicer.entity.Order;
import com.sgsoft.servicer.entity.User;

import java.util.Date;
import java.util.List;

/**
 * Created by Viktor Rotar on 04.04.14.
 */
public interface OrderDAO {

    public Order getById(Integer id) throws DBException;

    public int saveOrder(Order order) throws DBException;

    public int updateOrder(Order order) throws DBException;

    public int deleteById(Integer id) throws DBException;

    public List<Order> findByDate(Date date) throws DBException;

    public List<Order> getByUser(User user) throws DBException;

    public List<Order> getOrderListOrderById() throws DBException;

    public int addComponentToOrder(Integer orderId, Integer componentId) throws DBException;

    public int deleteComponentFromOrder(Integer orderId, Integer componentId) throws DBException;

}

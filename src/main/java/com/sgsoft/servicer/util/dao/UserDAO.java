package com.sgsoft.servicer.util.dao;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.User;

import java.util.List;

/**
 * Created by Viktor Rotar on 04.04.14.
 */
public interface UserDAO {

    public User getByLoginAndPassword(String login, String password) throws DBException;

    public User getById(Integer id) throws DBException;

    public int saveUser(User user) throws DBException;

    public List<User> getUsersOrderByLogin() throws DBException;

    public int updateUser(User user) throws DBException;

    boolean isExists(String login) throws DBException;
}

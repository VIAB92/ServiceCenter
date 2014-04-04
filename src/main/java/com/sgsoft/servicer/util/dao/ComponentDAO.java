package com.sgsoft.servicer.util.dao;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.Component;

import java.util.List;

/**
 * Created by Viktor Rotar on 04.04.14.
 */
public interface ComponentDAO {

    public Component getById(Integer id) throws DBException;

    public int saveComponent(Component component) throws DBException;

    public int updateComponent(Component component) throws DBException;

    public int deleteById(Integer id) throws DBException;

    public List<Component> getComponentsOrderByName() throws DBException;

}

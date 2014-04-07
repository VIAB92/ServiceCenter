package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.Component;
import com.sgsoft.servicer.util.dao.ComponentDAO;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;

import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class ComponentDaoImpl extends DAOCloseHelper implements ComponentDAO {
    @Override
    public Component getById(Integer id) throws DBException {
        return null;
    }

    @Override
    public int saveComponent(Component component) throws DBException {
        return 0;
    }

    @Override
    public int updateComponent(Component component) throws DBException {
        return 0;
    }

    @Override
    public int deleteById(Integer id) throws DBException {
        return 0;
    }

    @Override
    public List<Component> getComponentsOrderByName() throws DBException {
        return null;
    }
}

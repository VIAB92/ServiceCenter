package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.Client;
import com.sgsoft.servicer.util.dao.ClientDAO;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;

import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class ClientDaoImpl extends DAOCloseHelper implements ClientDAO {
    @Override
    public List<Client> findClientsByName(String name) throws DBException {
        return null;
    }

    @Override
    public Client findById(Integer id) throws DBException {
        return null;
    }

    @Override
    public int saveClient(Client client) throws DBException {
        return 0;
    }

    @Override
    public boolean isExists(String name, String email, String telephone) throws DBException {
        return false;
    }

    @Override
    public Client findByUniqueParams(String name, String email, String telephone) throws DBException {
        return null;
    }
}

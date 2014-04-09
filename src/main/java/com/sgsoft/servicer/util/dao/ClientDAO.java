package com.sgsoft.servicer.util.dao;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.Client;

import java.util.List;

/**
 * Created by Viktor Rotar on 04.04.14.
 */
public interface ClientDAO {

    public List<Client> findClientsByName(String name) throws DBException;

    public Client findById(Integer id) throws DBException;

    public int saveClient(Client client) throws DBException;

    public boolean isExists(String name, String email, String telephone) throws DBException;

    public Client findByUniqueParams(String name, String email, String telephone) throws DBException;

    public List<Client> getAllClients() throws DBException;

}

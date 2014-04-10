package com.sgsoft.servicer.util.dao;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Viktor Rotar on 04.04.14.
 */
public interface BidDAO {

    public Bid getById(Integer id) throws DBException;

    public int saveBid(Bid bid) throws DBException;

    public int updateBid(Bid bid) throws DBException;

    public List<Bid> getBidsOrderById() throws DBException;

    public List<Bid> getByDate(Date date) throws DBException;

    public List<Bid> getByClient(Client client) throws DBException;

    public List<Bid> getByGuarantee(Boolean guarantee) throws DBException;

    public Bid getByDeclaration(String declarationNumber) throws DBException;

    public List<Bid> getByCurrentState(State orderState) throws DBException;

}

package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.Bid;
import com.sgsoft.servicer.entity.Client;
import com.sgsoft.servicer.entity.State;
import com.sgsoft.servicer.util.dao.BidDAO;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;

import java.util.Date;
import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class BidDaoImpl extends DAOCloseHelper implements BidDAO {
    @Override
    public Bid getById(Integer id) throws DBException {
        return null;
    }

    @Override
    public int saveBid(Bid bid) throws DBException {
        return 0;
    }

    @Override
    public int updateBid(Bid bid) throws DBException {
        return 0;
    }

    @Override
    public List<Bid> getBidsOrderById() throws DBException {
        return null;
    }

    @Override
    public List<Bid> getByDate(Date date) throws DBException {
        return null;
    }

    @Override
    public List<Bid> getByClient(Client client) throws DBException {
        return null;
    }

    @Override
    public List<Bid> getByGuarantee(Boolean guarantee) throws DBException {
        return null;
    }

    @Override
    public Bid getByDeclaration(String declarationNumber) throws DBException {
        return null;
    }

    @Override
    public List<Bid> getByCurrentState(State orderState) throws DBException {
        return null;
    }
}

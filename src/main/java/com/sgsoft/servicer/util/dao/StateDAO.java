package com.sgsoft.servicer.util.dao;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.BidState;
import com.sgsoft.servicer.entity.Role;
import com.sgsoft.servicer.entity.State;

import java.util.List;

/**
 * Created by Viktor Rotar on 04.04.14.
 */
public interface StateDAO {

    public State getById(Integer id) throws DBException;

    public List<State> getStatesOrderByName() throws DBException;

    public int deleteById(Integer id) throws DBException;

    public int saveState(State state) throws DBException;

    public int updateState(State state) throws DBException;

    public List<State> getStatesByUserRole(Role role) throws DBException;

    public int addVisibleStateToRole(Role role, State state) throws DBException;

    public int addBidStateToBid(Integer bidId, BidState bidState) throws DBException;

    public List<BidState> getBidStatesByBid(Integer bidId) throws DBException;

}

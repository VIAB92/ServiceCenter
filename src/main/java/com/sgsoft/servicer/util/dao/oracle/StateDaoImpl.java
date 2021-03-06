package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.DBManager;
import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.BidState;
import com.sgsoft.servicer.entity.Role;
import com.sgsoft.servicer.entity.State;
import com.sgsoft.servicer.util.dao.StateDAO;
import com.sgsoft.servicer.util.dao.UserDAO;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class StateDaoImpl extends DAOCloseHelper implements StateDAO {

    protected static final String STATE_ID_VALUE = "STATE_ID";
    protected static final String STATE_NAME_VALUE = "STATE_NAME";
    protected static final String STATE_RATE_VALUE = "RATE";
    protected static final String USER_RIGHT_VALUE = "USER_RIGHT";
    protected static final String BID_ID_VALUE = "BID_ID";
    protected static final String DATE_CHANGED_VALUE = "DATE_CHANGED";
    protected static final String USER_CHANGED_VALUE = "USER_CHANGED";

    protected static final String GET_BY_ID_QUERY = "SELECT * FROM State_info WHERE "+STATE_ID_VALUE+" = ?";
    protected static final String GET_ALL_STATES_QUERY = "SELECT * FROM State_info ORDER BY "+STATE_NAME_VALUE;
    protected static final String DELETE_BY_ID_QUERY = "DELETE FROM State_info WHERE "+STATE_ID_VALUE+" = ?";
    protected static final String CREATE_STATE_QUERY = "INSERT INTO State_info VALUES(state_seq.NEXTVAL, ?, ?)";
    protected static final String UPDATE_STATE_QUERY = "UPDATE State_info SET " + STATE_NAME_VALUE + " = ?, " + STATE_RATE_VALUE +
            " = ? WHERE " + STATE_ID_VALUE + " = ?";
    protected static final String GET_VISIBLE_STATES = "SELECT si."+STATE_ID_VALUE+", si."+STATE_NAME_VALUE+", si."+ STATE_RATE_VALUE+
            " FROM States_visible sv JOIN State_info si ON sv."+STATE_ID_VALUE+" = si."+STATE_ID_VALUE+" WHERE sv."+USER_RIGHT_VALUE+" = (SELECT Right_id FROM User_right WHERE Right_name = ?)";
    protected static final String ADD_VISIBLE_STATE_TO_ROLE = "INSERT INTO States_visible  VALUES(?, (SELECT Right_id FROM User_right WHERE Right_name = ?))";
    protected static final String ADD_STATE_TO_BID_QUERY = "INSERT INTO Bid_state VALUES(?, ?, ?, ?)";
    protected static final String GET_STATES_BY_BID = "SELECT * FROM Bid_state WHERE "+BID_ID_VALUE+" = ?";

    private DBManager dbManager;
    private UserDAO userDAO;

    public StateDaoImpl(DBManager dbManager, UserDAO userDAO)
    {
        this.dbManager = dbManager;
        this.userDAO = userDAO;
    }

    @Override
    public State getById(Integer id) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        State state = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_ID_QUERY);
            preparedStatement.setInt(1, id.intValue());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                state = getStateEntity(resultSet);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
        return null;
    }

    private State getStateEntity(ResultSet resultSet) throws SQLException {
        State state = new State();
        state.setId(resultSet.getInt(STATE_ID_VALUE));
        state.setName(resultSet.getString(STATE_NAME_VALUE));
        state.setRate(resultSet.getInt(STATE_RATE_VALUE));
        return state;
    }

    @Override
    public List<State> getStatesOrderByName() throws DBException {
        ResultSet resultSet = dbManager.execSQL(GET_ALL_STATES_QUERY);
        List<State> states = new ArrayList<State>();
        try
        {
            while(resultSet.next())
            {
                states.add(getStateEntity(resultSet));
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
        }
        return states;
    }

    @Override
    public int deleteById(Integer id) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(DELETE_BY_ID_QUERY);
            preparedStatement.setInt(1, id.intValue());
            result = preparedStatement.executeUpdate();
            dbManager.commit();
        }
        catch (SQLException ex)
        {
            dbManager.rollback();
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeStatement(preparedStatement);
        }
        return result;
    }

    @Override
    public int saveState(State state) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(CREATE_STATE_QUERY);
            preparedStatement.setString(1, state.getName());
            preparedStatement.setInt(2, state.getRate());
            result = preparedStatement.executeUpdate();
            dbManager.commit();
        }
        catch(SQLException ex)
        {
            dbManager.rollback();
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeStatement(preparedStatement);
        }
        return result;
    }

    @Override
    public int updateState(State state) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(UPDATE_STATE_QUERY);
            preparedStatement.setString(1, state.getName());
            preparedStatement.setInt(2, state.getRate());
            preparedStatement.setInt(3, state.getId().intValue());
            result = preparedStatement.executeUpdate();
            dbManager.commit();
        }
        catch(SQLException ex)
        {
            dbManager.rollback();
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeStatement(preparedStatement);
        }
        return result;
    }

    @Override
    public List<State> getStatesByUserRole(Role role) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<State> states = new ArrayList<State>();
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_VISIBLE_STATES);
            preparedStatement.setString(1, role.getName());
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                states.add(getStateEntity(resultSet));
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
        return states;
    }

    @Override
    public int addVisibleStateToRole(Role role, State state) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(ADD_VISIBLE_STATE_TO_ROLE);
            preparedStatement.setInt(1, state.getId());
            preparedStatement.setString(2, role.getName());
            result = preparedStatement.executeUpdate();
            dbManager.commit();
        }
        catch (SQLException ex)
        {
            dbManager.rollback();
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeStatement(preparedStatement);
        }
        return result;
    }

    @Override
    public int addBidStateToBid(Integer bidId, BidState bidState) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(ADD_STATE_TO_BID_QUERY);
            preparedStatement.setInt(1, bidId);
            preparedStatement.setInt(2, bidState.getState().getId());
            preparedStatement.setDate(3, new Date(bidState.getDateChanged().getTime()));
            preparedStatement.setInt(4, bidState.getUserChanged().getId());
            result = preparedStatement.executeUpdate();
            dbManager.commit();
        }
        catch (SQLException ex)
        {
            dbManager.rollback();
            throw new DBException(ex.getMessage(),ex);
        }
        finally {
            closeStatement(preparedStatement);
        }
        return result;
    }

    @Override
    public List<BidState> getBidStatesByBid(Integer bidId) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<BidState> bidStates = new ArrayList<BidState>();
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_STATES_BY_BID);
            preparedStatement.setInt(1, bidId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                bidStates.add(getBidStateEntity(resultSet));
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
        return bidStates;
    }

    private BidState getBidStateEntity(ResultSet resultSet) throws SQLException, DBException {
        BidState bidState = new BidState();
        bidState.setDateChanged(resultSet.getDate(DATE_CHANGED_VALUE));
        bidState.setUserChanged(userDAO.getById(resultSet.getInt(USER_CHANGED_VALUE)));
        bidState.setState(this.getById(resultSet.getInt(STATE_ID_VALUE)));
        return bidState;
    }

}

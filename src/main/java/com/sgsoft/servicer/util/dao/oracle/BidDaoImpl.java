package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.DBManager;
import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.*;
import com.sgsoft.servicer.util.dao.*;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class BidDaoImpl extends DAOCloseHelper implements BidDAO {

    private static final String TABLE_MAIN_NAME = "Bid_main";
    private static final String TABLE_DIAGNOSTIC_NAME = "Diagnostic";
    private static final String TABLE_SERVICE_NAME = "Service";
    private static final String TABLE_FINAL_NAME = "Bid_final";
    private static final String NEXT_ID_VALUE = "NEXTID";
    //Bid_main
    private static final String BID_ID_VALUE = "BID_ID";
    private static final String BID_REG_DATE_VALUE = "REGISTER_DATE";
    private static final String BID_CLIENT_VALUE = "CLIENT_ID";
    private static final String BID_PRODUCT_VALUE = "PRODUCT_TYPE";
    private static final String BID_SN_VALUE = "SN1";
    private static final String BID_DEFECT_VALUE = "DEFECT";

    //Diagnostic
    private static final String BID_ID_DIAG_VALUE = "DIAG_BID_ID";
    private static final String BID_DIAG_ISSUE_VALUE = "DIAG_ISSUE_DATE";
    private static final String BID_DIAG_RETURN_VALUE = "DIAG_RETURN_DATE";
    private static final String BID_DIAG_RESULT_VALUE = "DIAGNOSTICS_RESULT";
    private static final String BID_GUARANTEE_VALUE = "IS_GUARANTEE";
    private static final String BID_SERV_TERM_VALUE = "TERM_IN_DAYS";

    //Service
    private static final String BID_ID_SERVICE_VALUE = "SERVICE_BID_ID";
    private static final String BID_SERV_ISSUE_VALUE = "SERVICE_ISSUE_DATE";
    private static final String BID_SERV_RETURN_VALUE = "SERVICE_RETURN_DATE";
    private static final String BID_SERV_RESULT_VALUE = "SERVICE_RESULT";

    //Final
    private static final String BID_ID_FINAL_VALUE = "FINAL_BID_ID";
    private static final String BID_CLIENT_RESULT_VALUE = "CLIENT_RESULT";
    private static final String BID_DECL_NAME_VALUE = "DECLARATION_NAME";
    private static final String BID_OFFICE_NAME_VALUE = "OFFICE_NAME";
    private static final String BID_NOTES_VALUE = "NOTES";

    //State
    private static final String BID_STATE_TABLE = "Bid_state";
    private static final String BID_DATE_CHANGED_VALUE = "date_changed";
    private static final String STATE_ID_VALUE = "state_id";

    private static final String GET_BY_ID_QUERY = "SELECT * FROM "+TABLE_MAIN_NAME+" m JOIN "+TABLE_DIAGNOSTIC_NAME+
            "d ON m."+BID_ID_VALUE+" = d."+BID_ID_DIAG_VALUE+" JOIN "+TABLE_SERVICE_NAME+" s ON m."+BID_ID_VALUE+" = s."+BID_ID_SERVICE_VALUE+
            " JOIN "+TABLE_FINAL_NAME+" f ON m."+BID_ID_VALUE+" = f."+BID_ID_FINAL_VALUE+" WHERE m."+BID_ID_VALUE+" = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM "+TABLE_MAIN_NAME+" m JOIN "+TABLE_DIAGNOSTIC_NAME+
            "d ON m."+BID_ID_VALUE+" = d."+BID_ID_DIAG_VALUE+" JOIN "+TABLE_SERVICE_NAME+" s ON m."+BID_ID_VALUE+" = s."+BID_ID_SERVICE_VALUE+
            " JOIN "+TABLE_FINAL_NAME+" f ON m."+BID_ID_VALUE+" = f."+BID_ID_FINAL_VALUE+" ORDER BY m."+BID_ID_VALUE;
    private static final String CREATE_BID_MAIN_QUERY = "INSERT INTO "+TABLE_MAIN_NAME+" VALUES(?, ?, ?, ?, ?, ?)";
    private static final String GET_NEXT_ID_QUERY = "SELECT bid_seq.NEXTVAL "+NEXT_ID_VALUE+" FROM DUAL";
    private static final String CREATE_BID_DIAGNOSTIC_QUERY = "INSERT INTO "+TABLE_DIAGNOSTIC_NAME+" VALUES(?, ?, ?, ?, ?, ?)";
    private static final String CREATE_BID_SERVICE_QUERY = "INSERT INTO "+TABLE_SERVICE_NAME+" VALUES(?, ?, ?, ?)";
    private static final String CREATE_BID_FINAL_QUERY = "INSERT INTO "+TABLE_FINAL_NAME+" VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_BID_MAIN_QUERY = "UPDATE "+TABLE_MAIN_NAME+" SET "+BID_REG_DATE_VALUE+" = ?, "+
            BID_PRODUCT_VALUE+" = ?, "+BID_SN_VALUE+" = ?, "+BID_DEFECT_VALUE+" = ? WHERE "+BID_ID_VALUE+" = ?";
    private static final String UPDATE_BID_DIAGNOSTIC_QUERY = "UPDATE "+TABLE_DIAGNOSTIC_NAME+" SET "+BID_DIAG_ISSUE_VALUE+" = ?, "+
            BID_DIAG_RETURN_VALUE+" = ?, "+BID_DIAG_RESULT_VALUE+" = ?, "+BID_GUARANTEE_VALUE+" = ?, "+BID_SERV_TERM_VALUE+" = ? WHERE "+
            BID_ID_DIAG_VALUE+" = ?";
    private static final String UPDATE_BID_SERVICE_QUERY = "UPDATE "+TABLE_SERVICE_NAME+" SET "+BID_SERV_ISSUE_VALUE+" = ?, "+
            BID_SERV_RETURN_VALUE+" = ?, "+BID_SERV_RESULT_VALUE+" = ? WHERE "+BID_ID_SERVICE_VALUE+" = ?";
    private static final String UPDATE_BID_FINAL_QUERY = "UPDATE "+TABLE_FINAL_NAME+" SET "+BID_CLIENT_RESULT_VALUE+" = ?, "+
            BID_DECL_NAME_VALUE+" = ?, "+BID_OFFICE_NAME_VALUE+" = ?, "+BID_NOTES_VALUE+" = ? WHERE "+BID_ID_FINAL_VALUE+" = ?";
    private static final String GET_BY_DECLARATION_QUERY = "SELECT * FROM "+TABLE_MAIN_NAME+" m JOIN "+TABLE_DIAGNOSTIC_NAME+
            "d ON m."+BID_ID_VALUE+" = d."+BID_ID_DIAG_VALUE+" JOIN "+TABLE_SERVICE_NAME+" s ON m."+BID_ID_VALUE+" = s."+BID_ID_SERVICE_VALUE+
            " JOIN "+TABLE_FINAL_NAME+" f ON m."+BID_ID_VALUE+" = f."+BID_ID_FINAL_VALUE+" WHERE f."+BID_DECL_NAME_VALUE+" = ?";
    private static final String GET_BY_DATE_QUERY = "SELECT * FROM "+TABLE_MAIN_NAME+" m JOIN "+TABLE_DIAGNOSTIC_NAME+
            "d ON m."+BID_ID_VALUE+" = d."+BID_ID_DIAG_VALUE+" JOIN "+TABLE_SERVICE_NAME+" s ON m."+BID_ID_VALUE+" = s."+BID_ID_SERVICE_VALUE+
            " JOIN "+TABLE_FINAL_NAME+" f ON m."+BID_ID_VALUE+" = f."+BID_ID_FINAL_VALUE+" WHERE m."+BID_REG_DATE_VALUE+" = ?";
    private static final String GET_BY_CLIENT_QUERY = "SELECT * FROM "+TABLE_MAIN_NAME+" m JOIN "+TABLE_DIAGNOSTIC_NAME+
            "d ON m."+BID_ID_VALUE+" = d."+BID_ID_DIAG_VALUE+" JOIN "+TABLE_SERVICE_NAME+" s ON m."+BID_ID_VALUE+" = s."+BID_ID_SERVICE_VALUE+
            " JOIN "+TABLE_FINAL_NAME+" f ON m."+BID_ID_VALUE+" = f."+BID_ID_FINAL_VALUE+" WHERE m."+BID_CLIENT_VALUE+" = ?";
    private static final String GET_BY_GUARANTEE_QUERY = "SELECT * FROM "+TABLE_MAIN_NAME+" m JOIN "+TABLE_DIAGNOSTIC_NAME+
            "d ON m."+BID_ID_VALUE+" = d."+BID_ID_DIAG_VALUE+" JOIN "+TABLE_SERVICE_NAME+" s ON m."+BID_ID_VALUE+" = s."+BID_ID_SERVICE_VALUE+
            " JOIN "+TABLE_FINAL_NAME+" f ON m."+BID_ID_VALUE+" = f."+BID_ID_FINAL_VALUE+" WHERE d."+BID_GUARANTEE_VALUE+" = ?";
    private static final String GET_BY_CURRENT_STATE_QUERY = "SELECT "+BID_ID_VALUE+" FROM "+BID_STATE_TABLE+" b WHERE "+BID_DATE_CHANGED_VALUE+" = "+
            "(SELECT MAX("+BID_DATE_CHANGED_VALUE+") FROM "+BID_STATE_TABLE+" f WHERE f."+BID_ID_VALUE+" = b."+BID_ID_VALUE+") AND "+STATE_ID_VALUE+" = ?";

    /*
    SELECT  `bid_id` ,  `bid_state` ,  `date_changed`
FROM test b
WHERE date_changed = (
SELECT MAX(  `date_changed` )
FROM test f
WHERE f.bid_id = b.bid_id )

     */
    private ComponentDAO componentDAO;
    private ClientDAO clientDAO;
    private StateDAO stateDAO;
    private DBManager dbManager;
    private ProductTypeDAO productTypeDAO;
    private WorkTypeDAO workTypeDAO;

    public BidDaoImpl (DBManager dbManager, ClientDAO clientDAO, ComponentDAO componentDAO, StateDAO stateDAO, ProductTypeDAO productTypeDAO, WorkTypeDAO workTypeDAO)
    {
        this.dbManager = dbManager;
        this.clientDAO = clientDAO;
        this.componentDAO = componentDAO;
        this.stateDAO = stateDAO;
        this.productTypeDAO = productTypeDAO;
        this.workTypeDAO = workTypeDAO;
    }

    @Override
    public Bid getById(Integer id) throws DBException {
        PreparedStatement preparedStatement = null;
        Bid bid = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_ID_QUERY);
            preparedStatement.setInt(1, id);
            bid = getBids(preparedStatement).get(0);
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeStatement(preparedStatement);
        }
        return bid;
    }

    private List<Bid> getBids(PreparedStatement preparedStatement) throws DBException {
        ResultSet resultSet = null;
        List<Bid> bids = new ArrayList<Bid>();
        try
        {
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                bids.add(getBidEntity(resultSet));
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
        }
        return bids;
    }

    private Bid getBidEntity(ResultSet resultSet) throws DBException, SQLException {
        Bid bid = new Bid();
        int bidId = resultSet.getInt(BID_ID_VALUE);
        bid.setId(bidId);
        bid.setBidStates(stateDAO.getBidStatesByBid(bidId));
        bid.setClient(clientDAO.findById(resultSet.getInt(BID_CLIENT_VALUE)));
        bid.setClientResult(resultSet.getString(BID_CLIENT_RESULT_VALUE));
        bid.setComponentList(componentDAO.getComponentsByBid(bidId));
        bid.setDeclarationNumber(resultSet.getString(BID_DECL_NAME_VALUE));
        bid.setDefect(resultSet.getString(BID_DEFECT_VALUE));
        bid.setDiagnosticIssueDate(resultSet.getDate(BID_DIAG_ISSUE_VALUE));
        bid.setDiagnosticReturnDate(resultSet.getDate(BID_DIAG_RETURN_VALUE));
        bid.setDiagnosticResult(resultSet.getString(BID_DIAG_RESULT_VALUE));
        bid.setFinalNotes(resultSet.getString(BID_NOTES_VALUE));
        bid.setOfficeNumber(resultSet.getString(BID_OFFICE_NAME_VALUE));
        bid.setProductType(productTypeDAO.getById(resultSet.getInt(BID_PRODUCT_VALUE)));
        bid.setRegisterDate(resultSet.getDate(BID_REG_DATE_VALUE));
        bid.setSerialNumber(resultSet.getString(BID_SN_VALUE));
        bid.setServiceIssueDate(resultSet.getDate(BID_SERV_ISSUE_VALUE));
        bid.setServiceReturnDate(resultSet.getDate(BID_SERV_RETURN_VALUE));
        bid.setServiceResult(resultSet.getString(BID_SERV_RESULT_VALUE));
        bid.setTermToService(resultSet.getInt(BID_SERV_TERM_VALUE));
        bid.setWorkTypeList(workTypeDAO.getWorkTypesByBid(bidId));
        int guarantee = resultSet.getInt(BID_GUARANTEE_VALUE);
        bid.setGuarantee(checkGuarantee(guarantee));
        return bid;
    }

    private Boolean checkGuarantee(int guarantee) {
        if (guarantee > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public int saveBid(Bid bid) throws DBException {
        int nextBidId = getNextBidId();
        bid.setId(nextBidId);
        int result = 0;
        try
        {
            dbManager.commit();
            createBidMain(bid);
            createBidDiagnostic(bid);
            createBidService(bid);
            result = createBidFinal(bid);
            dbManager.commit();
        }
        catch (DBException ex)
        {
            dbManager.rollback();
            throw ex;
        }
        return result;
    }

    private int createBidFinal(Bid bid) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            preparedStatement = dbManager.preparedStatement(CREATE_BID_FINAL_QUERY);
            preparedStatement.setInt(1, bid.getId());
            preparedStatement.setString(2, bid.getClientResult());
            preparedStatement.setString(3, bid.getDeclarationNumber());
            preparedStatement.setString(4, bid.getOfficeNumber());
            preparedStatement.setString(5, bid.getFinalNotes());
            result = preparedStatement.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        return result;
    }

    private void createBidService(Bid bid) throws DBException {
        PreparedStatement preparedStatement = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(CREATE_BID_SERVICE_QUERY);
            preparedStatement.setInt(1, bid.getId());
            preparedStatement.setDate(2, new java.sql.Date(bid.getServiceIssueDate().getTime()));
            preparedStatement.setDate(3, new java.sql.Date(bid.getServiceReturnDate().getTime()));
            preparedStatement.setString(4, bid.getServiceResult());
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    private void createBidDiagnostic(Bid bid) throws DBException {
        PreparedStatement preparedStatement = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(CREATE_BID_DIAGNOSTIC_QUERY);
            preparedStatement.setInt(1, bid.getId());
            preparedStatement.setDate(2, new java.sql.Date(bid.getDiagnosticIssueDate().getTime()));
            preparedStatement.setDate(3, new java.sql.Date(bid.getDiagnosticReturnDate().getTime()));
            preparedStatement.setString(4, bid.getDiagnosticResult());
            preparedStatement.setInt(5, getGuaranteeValue(bid.isGuarantee()));
            preparedStatement.setInt(6, bid.getTermToService());
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeStatement(preparedStatement);
        }

    }

    private int getGuaranteeValue(Boolean guarantee) {
        if (guarantee == false || guarantee == null)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    private void createBidMain(Bid bid) throws DBException {
        PreparedStatement preparedStatement = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(CREATE_BID_MAIN_QUERY);
            preparedStatement.setInt(1, bid.getId());
            preparedStatement.setDate(2, new java.sql.Date(bid.getRegisterDate().getTime()));
            preparedStatement.setInt(3, bid.getClient().getId());
            preparedStatement.setInt(4, bid.getProductType().getId());
            preparedStatement.setString(5, bid.getSerialNumber());
            preparedStatement.setString(6, bid.getDefect());
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    private int getNextBidId() throws DBException {
        ResultSet resultSet = dbManager.execSQL(GET_NEXT_ID_QUERY);
        int nextId = 0;
        try
        {
            while (resultSet.next())
            {
                nextId = resultSet.getInt(GET_NEXT_ID_QUERY);
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
        }
        return nextId;

    }

    @Override
    public int updateBid(Bid bid) throws DBException {
        int result = 0;
        try
        {
            dbManager.commit();
            updateBidMain(bid);
            updateBidDiagnostic(bid);
            updateBidService(bid);
            result = updateBidFinal(bid);
            dbManager.commit();
        }
        catch (DBException ex)
        {
            dbManager.rollback();
            throw ex;
        }
        return result;
    }

    private int updateBidFinal(Bid bid) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            preparedStatement = dbManager.preparedStatement(UPDATE_BID_FINAL_QUERY);
            preparedStatement.setString(1, bid.getClientResult());
            preparedStatement.setString(2, bid.getDeclarationNumber());
            preparedStatement.setString(3, bid.getOfficeNumber());
            preparedStatement.setString(4, bid.getFinalNotes());
            preparedStatement.setInt(5, bid.getId());
            result = preparedStatement.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        return result;
    }

    private void updateBidService(Bid bid) throws DBException {
        PreparedStatement preparedStatement = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(UPDATE_BID_SERVICE_QUERY);
            preparedStatement.setDate(1, new java.sql.Date(bid.getServiceIssueDate().getTime()));
            preparedStatement.setDate(2, new java.sql.Date(bid.getServiceReturnDate().getTime()));
            preparedStatement.setString(3, bid.getServiceResult());
            preparedStatement.setInt(4, bid.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    private void updateBidDiagnostic(Bid bid) throws DBException {
        PreparedStatement preparedStatement = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(UPDATE_BID_DIAGNOSTIC_QUERY);
            preparedStatement.setDate(1, new java.sql.Date(bid.getDiagnosticIssueDate().getTime()));
            preparedStatement.setDate(2, new java.sql.Date(bid.getServiceReturnDate().getTime()));
            preparedStatement.setString(3, bid.getDiagnosticResult());
            preparedStatement.setInt(4, getGuaranteeValue(bid.isGuarantee()));
            preparedStatement.setInt(5, bid.getTermToService());
            preparedStatement.setInt(6, bid.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
    }

    private void updateBidMain(Bid bid) throws DBException {
        PreparedStatement preparedStatement = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(UPDATE_BID_MAIN_QUERY);
            preparedStatement.setDate(1, new java.sql.Date(bid.getRegisterDate().getTime()));
            preparedStatement.setInt(2, bid.getProductType().getId());
            preparedStatement.setString(3, bid.getSerialNumber());
            preparedStatement.setString(4, bid.getDefect());
            preparedStatement.setInt(5, bid.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Bid> getBidsOrderById() throws DBException {
        ResultSet resultSet = dbManager.execSQL(GET_ALL_QUERY);
        List<Bid> bids = new ArrayList<Bid>();
        try
        {
            while(resultSet.next())
            {
                bids.add(getBidEntity(resultSet));
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
        }
        return bids;
    }

    @Override
    public List<Bid> getByDate(Date date) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Bid> bids = new ArrayList<Bid>();
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_DATE_QUERY);
            preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                bids.add(getBidEntity(resultSet));
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
        return bids;
    }

    @Override
    public List<Bid> getByClient(Client client) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Bid> bids = new ArrayList<Bid>();
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_CLIENT_QUERY);
            preparedStatement.setInt(1, client.getId());
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                bids.add(getBidEntity(resultSet));
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
        return bids;
    }

    @Override
    public List<Bid> getByGuarantee(Boolean guarantee) throws DBException {
        int guaranteeValue = getGuaranteeValue(guarantee);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Bid> bids = new ArrayList<Bid>();
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_GUARANTEE_QUERY);
            preparedStatement.setInt(1, guaranteeValue);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                bids.add(getBidEntity(resultSet));
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
        return bids;
    }

    @Override
    public Bid getByDeclaration(String declarationNumber) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Bid bid = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_DECLARATION_QUERY);
            preparedStatement.setString(1, declarationNumber);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                bid = getBidEntity(resultSet);
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
        return bid;
    }

    @Override
    public List<Bid> getByCurrentState(State orderState) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Bid> bids = new ArrayList<Bid>();
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_CURRENT_STATE_QUERY);
            preparedStatement.setInt(1, orderState.getId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                bids.add(getBidEntity(resultSet));
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
        return bids;
    }

}

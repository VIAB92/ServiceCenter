package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.Bid;
import com.sgsoft.servicer.entity.BidState;
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

    private static final String TABLE_MAIN_NAME = "Bid_main";
    private static final String TABLE_DIAGNOSTIC_NAME = "Diagnostic";
    private static final String TABLE_SERVICE_NAME = "Service";
    private static final String TABLE_FINAL_NAME = "Bid_final";
    private static final String TABLE_STATE_NAME = "Bid_state";
    private static final String TABLE_COMPONENT_ECONOMICS_NAME = "Component_economics";
    private static final String TABLE_WORK_ECONOMICS_NAME = "Work_economics";

    //Bid_main
    private static final String BID_ID_VALUE = "BID_ID";
    private static final String BID_REG_DATE_VALUE = "REGISTER_DATE";
    private static final String BID_CLIENT_VALUE = "CLIENT_ID";
    private static final String BID_PRODUCT_VALUE = "PRODUCT_TYPE";
    private static final String BID_SN_VALUE = "SN1";
    private static final String BID_DEFECT_VALUE = "DEFECT";

    //Diagnostic
    private static final String BID_DIAG_ISSUE_VALUE = "ISSUE_DATE";
    private static final String BID_DIAG_RETURN_VALUE = "RETURN_DATE";
    private static final String BID_DIAG_RESULT_VALUE = "DIAGNOSTICS_RESULT";
    private static final String BID_GUARANTEE_VALUE = "IS_GUARANTEE";
    private static final String BID_SERV_TERM_VALUE = "TERM_IN_DAYS";

    //Service
    private static final String BID_SERV_ISSUE_VALUE = "ISSUE_DATE";
    private static final String BID_SERV_RETURN_VALUE = "RETURN_DATE";
    private static final String BID_SERV_RESULT_VALUE = "SERVICE_RESULT";

    //Final
    private static final String BID_CLIENT_RESULT_VALUE = "CLIENT_RESULT";
    private static final String BID_DECL_NAME_VALUE = "DECLARATION_NAME";
    private static final String BID_OFFICE_NAME_VALUE = "OFFICE_NAME";
    private static final String BID_NOTES_VALUE = "NOTES";

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

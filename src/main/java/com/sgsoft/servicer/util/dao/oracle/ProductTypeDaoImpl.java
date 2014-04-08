package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.DBManager;
import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.ProductType;
import com.sgsoft.servicer.util.dao.ProductTypeDAO;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class ProductTypeDaoImpl extends DAOCloseHelper implements ProductTypeDAO {

    private static final String TYPE_ID_VALUE = "TYPE_ID";
    private static final String TYPE_NAME_VALUE = "TYPE_NAME";

    private static final String GET_BY_ID_QUERY = "SELECT * FROM Product_type WHERE "+TYPE_ID_VALUE+" = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM Product_type WHERE "+TYPE_ID_VALUE+" = ?";
    private static final String GET_ALL_TYPES_QUERY = "SELECT * FROM Product_type ORDER BY "+TYPE_NAME_VALUE;
    private static final String CREATE_TYPE_QUERY = "INSERT INTO Product_type VALUES(product_seq.NEXTVAL, ?)";
    private static final String UPDATE_TYPE_QUERY = "UPDATE Product_type SET "+TYPE_NAME_VALUE+" = ? WHERE "+TYPE_ID_VALUE+" = ?";

    private DBManager dbManager;

    public ProductTypeDaoImpl(DBManager dbManager)
    {
        this.dbManager = dbManager;
    }

    @Override
    public ProductType getById(Integer id) throws DBException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ProductType productType = null;
        try
        {
            preparedStatement = dbManager.preparedStatement(GET_BY_ID_QUERY);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                productType = getProductTypeEntity(resultSet);
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
        return productType;
    }

    private ProductType getProductTypeEntity(ResultSet resultSet) throws SQLException {
        ProductType productType = new ProductType();
        productType.setId(resultSet.getInt(TYPE_ID_VALUE));
        productType.setName(resultSet.getString(TYPE_NAME_VALUE));
        return productType;
    }

    @Override
    public int saveProductType(ProductType productType) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(CREATE_TYPE_QUERY);
            preparedStatement.setString(1, productType.getName());
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
        return  result;
    }

    @Override
    public int updateProductType(ProductType productType) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(UPDATE_TYPE_QUERY);
            preparedStatement.setString(1, productType.getName());
            preparedStatement.setInt(2, productType.getId());
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
    public int deleteById(Integer id) throws DBException {
        PreparedStatement preparedStatement = null;
        int result = 0;
        try
        {
            dbManager.commit();
            preparedStatement = dbManager.preparedStatement(DELETE_BY_ID_QUERY);
            preparedStatement.setInt(1, id);
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
    public List<ProductType> getProductTypesOrderByName() throws DBException {
        ResultSet resultSet = dbManager.execSQL(GET_ALL_TYPES_QUERY);
        List<ProductType> productTypes = new ArrayList<ProductType>();
        try
        {
            while(resultSet.next())
            {
                productTypes.add(getProductTypeEntity(resultSet));
            }
        }
        catch (SQLException ex)
        {
            throw new DBException(ex.getMessage(), ex);
        }
        finally {
            closeResultSet(resultSet);
        }
        return productTypes;
    }
}

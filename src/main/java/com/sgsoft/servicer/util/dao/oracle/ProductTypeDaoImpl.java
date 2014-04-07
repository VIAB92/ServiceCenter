package com.sgsoft.servicer.util.dao.oracle;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.ProductType;
import com.sgsoft.servicer.util.dao.ProductTypeDAO;
import com.sgsoft.servicer.util.dao.closer.DAOCloseHelper;

import java.util.List;

/**
 * Created by Viktor Rotar on 07.04.14.
 */
public class ProductTypeDaoImpl extends DAOCloseHelper implements ProductTypeDAO {
    @Override
    public ProductType getById(Integer id) throws DBException {
        return null;
    }

    @Override
    public int saveProductType(ProductType productType) throws DBException {
        return 0;
    }

    @Override
    public int updateProductType(ProductType productType) throws DBException {
        return 0;
    }

    @Override
    public int deleteById(Integer id) throws DBException {
        return 0;
    }

    @Override
    public List<ProductType> getProductTypesOrderByName() throws DBException {
        return null;
    }
}

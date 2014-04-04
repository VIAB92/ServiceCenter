package com.sgsoft.servicer.util.dao;

import com.sgsoft.servicer.db.exception.DBException;
import com.sgsoft.servicer.entity.ProductType;

import java.util.List;

/**
 * Created by Viktor Rotar on 04.04.14.
 */
public interface ProductTypeDAO {

    public ProductType getById(Integer id) throws DBException;

    public int saveProductType(ProductType productType) throws DBException;

    public int updateProductType(ProductType productType) throws DBException;

    public int deleteById(Integer id) throws DBException;

    public List<ProductType> getProductTypesOrderByName() throws DBException;

}

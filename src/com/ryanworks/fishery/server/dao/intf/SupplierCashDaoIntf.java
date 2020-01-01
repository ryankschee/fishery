package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.bean.SupplierCashBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface SupplierCashDaoIntf 
{   
    public int insertCash(SupplierCashBean bean);
    public boolean updateCash(SupplierCashBean bean);
    public boolean deleteCash(SupplierCashBean bean);
    
    public List<SupplierCashBean> getAllCashs();
    public SupplierCashBean findSingleCashById(String id);     
    public List<SupplierCashBean> findCashsByDateRange(long dateTimeFrom, long dateTimeTo);
    public List<SupplierCashBean> findCashsByDateAndSupplier(long dateTimeFrom, long dateTimeTo, SupplierBean supplier);
    
    public boolean deleteCashesBySupplier(String supplierId);
}

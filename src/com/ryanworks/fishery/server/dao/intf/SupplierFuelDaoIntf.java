package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.bean.SupplierFuelBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface SupplierFuelDaoIntf 
{   
    public int insertFuel(SupplierFuelBean bean);
    public boolean updateFuel(SupplierFuelBean bean);
    public boolean deleteFuel(SupplierFuelBean bean);
    
    public List<SupplierFuelBean> getAllFuels();
    public SupplierFuelBean findSingleFuelById(String id);     
    public List<SupplierFuelBean> findFuelsByDateRange(long dateTimeFrom, long dateTimeTo);
    public List<SupplierFuelBean> findFuelsByDateAndSupplier(long dateTimeFrom, long dateTimeTo, SupplierBean supplier);
        
    public boolean deleteFuelsBySupplier(String supplierId);
}

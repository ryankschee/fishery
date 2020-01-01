package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.SupplierBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface SupplierDaoIntf 
{   
    public int insertSupplier(SupplierBean bean);
    public boolean updateSupplier(SupplierBean bean);
    public boolean deleteSupplier(SupplierBean bean);
    
    public List<SupplierBean> getAllSuppliers();
    public List<SupplierBean> getFrequentSuppliers();
    public List<SupplierBean> getNonFrequentSuppliers();
    public SupplierBean findSingleSupplierById(String id);     
    public List<SupplierBean> findSuppliersByDate(long dateTime);
}

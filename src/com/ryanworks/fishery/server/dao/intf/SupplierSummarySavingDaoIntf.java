package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.SupplierSummarySavingBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface SupplierSummarySavingDaoIntf 
{   
    public int insertSummarySaving(SupplierSummarySavingBean bean);
    public boolean updateSummarySaving(SupplierSummarySavingBean bean);
    public boolean deleteSummarySaving(SupplierSummarySavingBean bean);
    
    public SupplierSummarySavingBean findSingleSummarySavingById(String id);      
    public List<SupplierSummarySavingBean> findSummarySavingBySupplier(String supplierId);
    public SupplierSummarySavingBean findSummarySavingBySupplierAndDate(String supplierId, long anyday);
    
    public boolean deleteSummarySavingsBySupplier(String supplierId);
}
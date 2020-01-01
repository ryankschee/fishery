package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.SupplierSummaryBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface SupplierSummaryDaoIntf 
{   
    public int insertSummary(SupplierSummaryBean bean);
    public boolean updateSummary(SupplierSummaryBean bean);
    public boolean deleteSummary(SupplierSummaryBean bean);
    
    public SupplierSummaryBean findSingleSummaryById(String id);      
    public List<SupplierSummaryBean> findSummaryBySupplier(String supplierId);
    public SupplierSummaryBean findSummaryBySupplierAndDate(String supplierId, long anyday);
    
    public boolean deleteSummaryBySupplier(String supplierId);
}
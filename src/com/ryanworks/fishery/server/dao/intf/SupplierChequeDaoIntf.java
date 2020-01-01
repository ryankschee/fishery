package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.bean.SupplierChequeBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface SupplierChequeDaoIntf 
{   
    public int insertCheque(SupplierChequeBean bean);
    public boolean updateCheque(SupplierChequeBean bean);
    public boolean deleteCheque(SupplierChequeBean bean);
    
    public List<SupplierChequeBean> getAllCheques();
    public SupplierChequeBean findSingleChequeById(String id);     
    public List<SupplierChequeBean> findChequesByDateRange(long dateTimeFrom, long dateTimeTo);
    public List<SupplierChequeBean> findChequesByDateAndSupplier(long dateTimeFrom, long dateTimeTo, SupplierBean supplier);
    
    public boolean deleteChequesBySupplier(String supplierId);
}

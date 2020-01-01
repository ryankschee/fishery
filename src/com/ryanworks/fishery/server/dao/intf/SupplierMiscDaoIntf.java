package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.bean.SupplierMiscBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface SupplierMiscDaoIntf 
{   
    public int insertMisc(SupplierMiscBean bean);
    public boolean updateMisc(SupplierMiscBean bean);
    public boolean deleteMisc(SupplierMiscBean bean);
    
    public List<SupplierMiscBean> getAllMiscs();
    public SupplierMiscBean findSingleMiscById(String id);     
    public List<SupplierMiscBean> findMiscsByDateRange(long dateTimeFrom, long dateTimeTo);
    public List<SupplierMiscBean> findMiscsByDateAndSupplier(long dateTimeFrom, long dateTimeTo, SupplierBean supplier);
    
    public boolean deleteMiscsBySupplier(String supplierId);
}

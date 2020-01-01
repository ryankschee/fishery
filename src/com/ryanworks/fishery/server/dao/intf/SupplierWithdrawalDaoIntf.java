package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.bean.SupplierWithdrawalBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface SupplierWithdrawalDaoIntf 
{   
    public int insertWithdrawal(SupplierWithdrawalBean bean);
    public boolean updateWithdrawal(SupplierWithdrawalBean bean);
    public boolean deleteWithdrawal(SupplierWithdrawalBean bean);
    
    public List<SupplierWithdrawalBean> getAllWithdrawals();
    public SupplierWithdrawalBean findSingleWithdrawalById(String id);     
    public List<SupplierWithdrawalBean> findWithdrawalsByDateRange(long dateTimeFrom, long dateTimeTo);
    public List<SupplierWithdrawalBean> findWithdrawalsByDateAndSupplier(long dateTimeFrom, long dateTimeTo, SupplierBean supplier);
    
    public boolean deleteWithdrawalsBySupplier(String supplierId);
}

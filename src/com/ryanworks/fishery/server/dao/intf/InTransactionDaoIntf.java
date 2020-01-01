package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.InTransactionBean;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface InTransactionDaoIntf 
{
    public int insertInTransaction(InTransactionBean bean);
    public boolean updateInTransaction(InTransactionBean bean);
    public boolean deleteInTransaction(InTransactionBean bean);
    
    public InTransactionBean findSingleInTransactionById(String id);   
    public int getTripNoBySupplierIdAndDate(String supplierId, long dateTime);
    public List<InTransactionBean> findInTransactionsByDate(long timeInMillis);      
    public List<InTransactionBean> findInTransactionsByDateRange(long dateTimeFrom, long dateTimeTo);
    public List<InTransactionBean> findInTransactionsBySupplier(SupplierBean supplier);
    public List<InTransactionBean> findInTransactionsByDateAndSupplier(long dateTimeFrom, long dateTimeTo, SupplierBean supplier);
    public List<InTransactionBean> findSavingTransactionsByDateAndSupplier(long dateTimeFrom, long dateTimeTo, SupplierBean supplier);
}

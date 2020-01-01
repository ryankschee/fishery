package com.ryanworks.fishery.shared.service;

import com.ryanworks.fishery.shared.bean.InTransactionBean;
import com.ryanworks.fishery.shared.bean.InTransactionLineBean;
import com.ryanworks.fishery.shared.bean.ItemBean;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import java.util.List;

/**
 *
 * @author Ryan
 */
public interface InTransactionServiceIntf 
{
    public int getTripNoBySupplierIdAndDate(String supplierId, long dateTime);
    
    public int saveTransaction(InTransactionBean transactionBean);
    
    public int saveOrUpdateTransactionLine(InTransactionLineBean lineBean);
    
    public InTransactionBean getTransactionById(String transactionId);
    
    public InTransactionLineBean getInTransactionLineBySalesLine(String salesLineId);
    
    public List<InTransactionLineBean> getTransactionLineByTransactionId(String transactionId);
    
    public List<InTransactionLineBean> getTransactionLineByItemAndDate(ItemBean item, long dateTime);
    
    public List<InTransactionBean> getTransactionsByDate(long timeInMillis, boolean fullBean);
        
    public List<InTransactionBean> getTransactionListByDateRange(long dateFrom, long dateTo, boolean fullBean);
    
    public List<InTransactionBean> getTransactionListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier, boolean fullBean);
    
    public List<InTransactionBean> getSavingListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier);
    
    public void deleteTransactionsBySupplier(SupplierBean supplierObj);
    
    public void deleteTransaction(InTransactionBean transactionBean);
    
    public void deleteTransactionLine(InTransactionLineBean lineBean);
}

package com.ryanworks.fishery.client.delegate;

import com.ryanworks.fishery.shared.bean.InTransactionBean;
import com.ryanworks.fishery.shared.bean.InTransactionLineBean;
import com.ryanworks.fishery.shared.bean.ItemBean;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.service.ServiceLocator;
import java.util.List;
import javax.swing.SwingWorker;

public class InTransactionDelegate 
{
    // Singleton
    private static final InTransactionDelegate instance = new InTransactionDelegate();

    private InTransactionDelegate() {}

    public static InTransactionDelegate getInstance() 
    {
        return instance;
    }

    //--------------------------------------------------------------------------

    public InTransactionLineBean getInTransactionLineBySalesLine(String salesLineId)
    {
        return ServiceLocator.getInTransactionService().getInTransactionLineBySalesLine(salesLineId);
    }
    
    public int getTripNoBySupplierIdAndDate(String supplierId, long dateTime)
    {
        return ServiceLocator.getInTransactionService().getTripNoBySupplierIdAndDate(supplierId, dateTime);
    }
    
    public int saveTransaction(InTransactionBean transactionBean)
    {        
        ServiceLocator.getInTransactionService().saveTransaction(transactionBean);        
        return 0;
    }
    
    public int saveTransactionLine(InTransactionLineBean lineBean)
    {
        ServiceLocator.getInTransactionService().saveOrUpdateTransactionLine(lineBean);
        return 0;
    }
    
    public InTransactionBean getTransactionById(String transactionId)
    {
        return ServiceLocator.getInTransactionService().getTransactionById(transactionId);
    }
    
    public List<InTransactionLineBean> getTransactionLineByTransactionId(String transactionId)
    {
        return ServiceLocator.getInTransactionService().getTransactionLineByTransactionId(transactionId);
    }
    
    public List<InTransactionLineBean> getTransactionLineByItemAndDate(ItemBean item, long dateTime)
    {
        return ServiceLocator.getInTransactionService().getTransactionLineByItemAndDate(item, dateTime);
    }
    
    public List<InTransactionBean> getTransactionsByDate(long timeInMillis, boolean fullBean)
    {
        return ServiceLocator.getInTransactionService().getTransactionsByDate(timeInMillis, fullBean);
    }
    
    public List<InTransactionBean> getTransactionListByDateRange(long dateFrom, long dateTo, boolean fullBean)
    {
        return ServiceLocator.getInTransactionService().getTransactionListByDateRange(dateFrom, dateTo, fullBean);
    }
    
    public List<InTransactionBean> getTransactionListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier, boolean fullBean)
    {
        return ServiceLocator.getInTransactionService().getTransactionListByDateAndSupplier(dateFrom, dateTo, supplier, fullBean);
    }    
    
    public List<InTransactionBean> getSavingListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier)
    {
        return ServiceLocator.getInTransactionService().getSavingListByDateAndSupplier(dateFrom, dateTo, supplier);
    }
    
    public void deleteTransactionsBySupplier(SupplierBean supplierObj)
    {
        ServiceLocator.getInTransactionService().deleteTransactionsBySupplier(supplierObj);
    }
    
    public void deleteTransaction(InTransactionBean transactionObj)
    {
        ServiceLocator.getInTransactionService().deleteTransaction(transactionObj);
    }
    
    public void deleteTransactionLine(InTransactionLineBean lineBean)
    {
        ServiceLocator.getInTransactionService().deleteTransactionLine(lineBean);
    }
}

package com.ryanworks.fishery.client.delegate;

import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.bean.SupplierCashBean;
import com.ryanworks.fishery.shared.bean.SupplierChequeBean;
import com.ryanworks.fishery.shared.bean.SupplierFuelBean;
import com.ryanworks.fishery.shared.bean.SupplierMiscBean;
import com.ryanworks.fishery.shared.bean.SupplierSummaryBean;
import com.ryanworks.fishery.shared.bean.SupplierSummarySavingBean;
import com.ryanworks.fishery.shared.bean.SupplierWithdrawalBean;
import com.ryanworks.fishery.shared.service.ServiceLocator;
import java.util.List;
import javax.swing.SwingWorker;

public class SupplierDelegate 
{
    // Singleton
    private static final SupplierDelegate instance = new SupplierDelegate();

    private SupplierDelegate() {}

    public static SupplierDelegate getInstance() 
    {
        return instance;
    }

    public List<SupplierBean> getAllSuppliers()
    {
        return ServiceLocator.getSupplierService().getAllSuppliers();
    }
    
    public List<SupplierBean> getFrequentSuppliers()
    {
        return ServiceLocator.getSupplierService().getFrequentSuppliers();
    }
    
    public List<SupplierBean> getNonFrequentSuppliers()
    {
        return ServiceLocator.getSupplierService().getNonFrequentSuppliers();
    }
    
    public List<SupplierBean> getSuppliersByDate(long dateTime)
    {
        return ServiceLocator.getSupplierService().getSuppliersByDate(dateTime);
    }
    
    public SupplierBean getSupplierById(String id)
    {
        return ServiceLocator.getSupplierService().getSupplierById(id);
    }
    
    public void saveOrUpdateSupplier(SupplierBean supplier)
    {
        ServiceLocator.getSupplierService().saveOrUpdateSupplier( supplier );
    }
    
    public void deleteSupplier(SupplierBean supplier)
    {
        ServiceLocator.getSupplierService().deleteSupplier( supplier );
    }
    
    public SupplierChequeBean getChequeById(String id)
    {
        return ServiceLocator.getSupplierService().getChequeById(id);
    }
    
    public List<SupplierChequeBean> getChequeListByDateRange(long dateFrom, long dateTo)
    {
        return ServiceLocator.getSupplierService().getChequeListByDateRange(dateFrom, dateTo);
    }
    
    public List<SupplierChequeBean> getChequeListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier)
    {
        return ServiceLocator.getSupplierService().getChequeListByDateAndSupplier(dateFrom, dateTo, supplier);
    }
    
    public void saveOrUpdateCheque(SupplierChequeBean cheque)
    {
        ServiceLocator.getSupplierService().saveOrUpdateCheque(cheque);
    }
    
    public void deleteCheque(SupplierChequeBean cheque)
    {
        ServiceLocator.getSupplierService().deleteCheque(cheque);
    }
    
    public SupplierFuelBean getFuelById(String id)
    {
        return ServiceLocator.getSupplierService().getFuelById(id);
    }
    
    public List<SupplierFuelBean> getFuelListByDateRange(long dateFrom, long dateTo)
    {
        return ServiceLocator.getSupplierService().getFuelListByDateRange(dateFrom, dateTo);
    }
    
    public List<SupplierFuelBean> getFuelListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier)
    {
        return ServiceLocator.getSupplierService().getFuelListByDateAndSupplier(dateFrom, dateTo, supplier);
    }
    
    public void saveOrUpdateFuel(SupplierFuelBean fuel)
    {
        ServiceLocator.getSupplierService().saveOrUpdateFuel(fuel);
    }
    
    public void deleteFuel(SupplierFuelBean fuel)
    {
        ServiceLocator.getSupplierService().deleteFuel(fuel);
    }
    
    public SupplierMiscBean getMiscById(String id)
    {
        return ServiceLocator.getSupplierService().getMiscById(id);
    }
    
    public List<SupplierMiscBean> getMiscListByDateRange(long dateFrom, long dateTo)
    {
        return ServiceLocator.getSupplierService().getMiscListByDateRange(dateFrom, dateTo);
    }
    
    public List<SupplierMiscBean> getMiscListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier)
    {
        return ServiceLocator.getSupplierService().getMiscListByDateAndSupplier(dateFrom, dateTo, supplier);
    }
    
    public void saveOrUpdateMisc(SupplierMiscBean misc)
    {
        ServiceLocator.getSupplierService().saveOrUpdateMisc(misc);
    }
    
    public void deleteMisc(SupplierMiscBean misc)
    {
        ServiceLocator.getSupplierService().deleteMisc(misc);
    }
    
    public SupplierCashBean getCashById(String id)
    {
        return ServiceLocator.getSupplierService().getCashById(id);
    }
    
    public List<SupplierCashBean> getCashListByDateRange(long dateFrom, long dateTo)
    {
        return ServiceLocator.getSupplierService().getCashListByDateRange(dateFrom, dateTo);
    }
    
    public List<SupplierCashBean> getCashListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier)
    {
        return ServiceLocator.getSupplierService().getCashListByDateAndSupplier(dateFrom, dateTo, supplier);
    }
    
    public void saveOrUpdateCash(SupplierCashBean cash)
    {
        ServiceLocator.getSupplierService().saveOrUpdateCash(cash);
    }
    
    public void deleteCash(SupplierCashBean cash)
    {
        ServiceLocator.getSupplierService().deleteCash(cash);
    }
    
    public SupplierWithdrawalBean getWithdrawalById(String id)
    {
        return ServiceLocator.getSupplierService().getWithdrawalById(id);
    }
    
    public List<SupplierWithdrawalBean> getWithdrawalListByDateRange(long dateFrom, long dateTo)
    {
        return ServiceLocator.getSupplierService().getWithdrawalListByDateRange(dateFrom, dateTo);
    }
    
    public List<SupplierWithdrawalBean> getWithdrawalListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier)
    {
        return ServiceLocator.getSupplierService().getWithdrawalListByDateAndSupplier(dateFrom, dateTo, supplier);
    }
    
    public void saveOrUpdateWithdrawal(SupplierWithdrawalBean cash)
    {
        ServiceLocator.getSupplierService().saveOrUpdateWithdrawal(cash);
        //ServiceLocator.getSupplierService().updateSummarySaving(cash.getSupplierId());
    }
    
    public void deleteWithdrawal(SupplierWithdrawalBean cash)
    {
        ServiceLocator.getSupplierService().deleteWithdrawal(cash);
        //ServiceLocator.getSupplierService().updateSummarySaving(cash.getSupplierId());
    }
    
    public void saveOrUpdateSummary(SupplierSummaryBean bean)
    {
        ServiceLocator.getSupplierService().saveOrUpdateSummary(bean);
    }
    
    public SupplierSummaryBean getSummaryBySupplierAndDate(String supplierId, long anyday)
    {
        return ServiceLocator.getSupplierService().getSummaryBySupplierAndDate(supplierId, anyday);
    }
    
    public void saveOrUpdateSummarySaving(SupplierSummarySavingBean bean)
    {
        ServiceLocator.getSupplierService().saveOrUpdateSummarySaving(bean);
    }
    
    public SupplierSummarySavingBean getSummarySavingBySupplierAndDate(String supplierId, long anyday)
    {
        return ServiceLocator.getSupplierService().getSummarySavingBySupplierAndDate(supplierId, anyday);
    }
    
    public void updateSupplierSummary(String supplierId)
    {
        ServiceLocator.getSupplierService().updateSummary(supplierId);
    }
    
    public void updateSupplierSummarySaving(String supplierId)
    {
        ServiceLocator.getSupplierService().updateSummarySaving(supplierId);
    }
}

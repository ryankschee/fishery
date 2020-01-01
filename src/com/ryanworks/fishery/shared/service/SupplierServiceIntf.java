package com.ryanworks.fishery.shared.service;

import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.bean.SupplierCashBean;
import com.ryanworks.fishery.shared.bean.SupplierChequeBean;
import com.ryanworks.fishery.shared.bean.SupplierFuelBean;
import com.ryanworks.fishery.shared.bean.SupplierMiscBean;
import com.ryanworks.fishery.shared.bean.SupplierSummaryBean;
import com.ryanworks.fishery.shared.bean.SupplierSummarySavingBean;
import com.ryanworks.fishery.shared.bean.SupplierWithdrawalBean;
import java.util.List;

public interface SupplierServiceIntf 
{
    public List<SupplierBean> getAllSuppliers();    
    public List<SupplierBean> getFrequentSuppliers();   
    public List<SupplierBean> getNonFrequentSuppliers();   
    public List<SupplierBean> getSuppliersByDate(long dateTime);    
    public SupplierBean getSupplierById(String id);  
    public void saveOrUpdateSupplier(SupplierBean supplier);    
    public void deleteSupplier(SupplierBean supplier);
    
    public SupplierChequeBean getChequeById(String id);
    public List<SupplierChequeBean> getChequeListByDateRange(long dateFrom, long dateTo);
    public List<SupplierChequeBean> getChequeListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier);
    public void saveOrUpdateCheque(SupplierChequeBean cheque);
    public void deleteCheque(SupplierChequeBean cheque);
    
    public SupplierFuelBean getFuelById(String id);
    public List<SupplierFuelBean> getFuelListByDateRange(long dateFrom, long dateTo);
    public List<SupplierFuelBean> getFuelListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier);
    public void saveOrUpdateFuel(SupplierFuelBean fuel);
    public void deleteFuel(SupplierFuelBean fuel);
    
    public SupplierMiscBean getMiscById(String id);
    public List<SupplierMiscBean> getMiscListByDateRange(long dateFrom, long dateTo);
    public List<SupplierMiscBean> getMiscListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier);
    public void saveOrUpdateMisc(SupplierMiscBean misc);
    public void deleteMisc(SupplierMiscBean misc);
    
    public SupplierCashBean getCashById(String id);
    public List<SupplierCashBean> getCashListByDateRange(long dateFrom, long dateTo);
    public List<SupplierCashBean> getCashListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier);
    public void saveOrUpdateCash(SupplierCashBean cash);
    public void deleteCash(SupplierCashBean cash);
        
    public SupplierWithdrawalBean getWithdrawalById(String id);
    public List<SupplierWithdrawalBean> getWithdrawalListByDateRange(long dateFrom, long dateTo);
    public List<SupplierWithdrawalBean> getWithdrawalListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier);
    public void saveOrUpdateWithdrawal(SupplierWithdrawalBean cash);
    public void deleteWithdrawal(SupplierWithdrawalBean cash);
        
    public void saveOrUpdateSummary(SupplierSummaryBean bean);
    public void updateSummary(String supplierId);
    public SupplierSummaryBean getSummaryBySupplierAndDate(String supplierId, long anyday);
    
    public void saveOrUpdateSummarySaving(SupplierSummarySavingBean bean);
    public void updateSummarySaving(String supplierId);
    public SupplierSummarySavingBean getSummarySavingBySupplierAndDate(String supplierId, long anyday);
}

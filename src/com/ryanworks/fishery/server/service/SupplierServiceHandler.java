package com.ryanworks.fishery.server.service;

import com.ryanworks.fishery.server.dao.DaoException;
import com.ryanworks.fishery.server.dao.impl.MySQLInTransactionDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLInTransactionLineDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSupplierCashDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSupplierChequeDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSupplierDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSupplierFuelDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSupplierMiscDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSupplierSummaryDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSupplierSummarySavingDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSupplierWithdrawalDaoImpl;
import com.ryanworks.fishery.shared.bean.InTransactionBean;
import com.ryanworks.fishery.shared.bean.InTransactionLineBean;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.bean.SupplierCashBean;
import com.ryanworks.fishery.shared.bean.SupplierChequeBean;
import com.ryanworks.fishery.shared.bean.SupplierFuelBean;
import com.ryanworks.fishery.shared.bean.SupplierMiscBean;
import com.ryanworks.fishery.shared.bean.SupplierSummaryBean;
import com.ryanworks.fishery.shared.bean.SupplierSummarySavingBean;
import com.ryanworks.fishery.shared.bean.SupplierWithdrawalBean;
import com.ryanworks.fishery.shared.service.SupplierServiceIntf;
import com.ryanworks.fishery.shared.util.MathUtil;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SupplierServiceHandler
    extends MyServiceHandler
    implements SupplierServiceIntf 
{
    @Override
    public List<SupplierBean> getAllSuppliers()
    {
        try 
        {
            MySQLSupplierDaoImpl dao = this.getDaoFactoryObject().getSupplierDao();
            return dao.getAllSuppliers();
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getAllSuppliers(): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public List<SupplierBean> getFrequentSuppliers()
    {
        try 
        {
            MySQLSupplierDaoImpl dao = this.getDaoFactoryObject().getSupplierDao();
            return dao.getFrequentSuppliers();
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getFrequentSuppliers(): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public List<SupplierBean> getNonFrequentSuppliers()
    {
        try 
        {
            MySQLSupplierDaoImpl dao = this.getDaoFactoryObject().getSupplierDao();
            return dao.getNonFrequentSuppliers();
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getNonFrequentSuppliers(): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public List<SupplierBean> getSuppliersByDate(long dateTime)
    {
        try 
        {
            MySQLSupplierDaoImpl dao = this.getDaoFactoryObject().getSupplierDao();
            return dao.findSuppliersByDate(dateTime);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getSuppliersByDate(): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public SupplierBean getSupplierById(String id)
    {
        try 
        {
            MySQLSupplierDaoImpl dao = this.getDaoFactoryObject().getSupplierDao();
            return dao.findSingleSupplierById(id);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getSupplierById(): exception (" + ex.getMessage() + ")");
            return null;
        }
    }
    
    @Override
    public void saveOrUpdateSupplier(SupplierBean supplier) 
    {
        try 
        {
            MySQLSupplierDaoImpl dao = this.getDaoFactoryObject().getSupplierDao();
            if (dao.findSingleSupplierById( supplier.getId() ) == null)
            {
                dao.insertSupplier(supplier);
            }
            else
            {
                dao.updateSupplier(supplier);
            }
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "saveOrUpdateSupplier(): exception (" + ex.getMessage() + ")");            
        }
    }
    
    @Override
    public void deleteSupplier(SupplierBean supplier) 
    {
        try 
        {
            this.getDaoFactoryObject().getSupplierCashDao().deleteCashesBySupplier(supplier.getId());
            this.getDaoFactoryObject().getSupplierChequeDao().deleteChequesBySupplier(supplier.getId());
            this.getDaoFactoryObject().getSupplierFuelDao().deleteFuelsBySupplier(supplier.getId());
            this.getDaoFactoryObject().getSupplierMiscDao().deleteMiscsBySupplier(supplier.getId());
            this.getDaoFactoryObject().getSupplierSummaryDao().deleteSummaryBySupplier(supplier.getId());
            this.getDaoFactoryObject().getSupplierSummarySavingDao().deleteSummarySavingsBySupplier(supplier.getId());
            this.getDaoFactoryObject().getSupplierWithdrawalDao().deleteWithdrawalsBySupplier(supplier.getId());
            
            MySQLSupplierDaoImpl dao = this.getDaoFactoryObject().getSupplierDao();
            dao.deleteSupplier(supplier);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "deleteSupplier(): exception (" + ex.getMessage() + ")");            
        }
    }
    
    @Override
    public SupplierChequeBean getChequeById(String id)
    {
        try 
        {
            MySQLSupplierChequeDaoImpl dao = this.getDaoFactoryObject().getSupplierChequeDao();
            return dao.findSingleChequeById(id);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getChequeById(): exception (" + ex.getMessage() + ")");
            return null;
        }
    }
            
    @Override
    public List<SupplierChequeBean> getChequeListByDateRange(long dateFrom, long dateTo)
    {
        try 
        {
            MySQLSupplierChequeDaoImpl dao = this.getDaoFactoryObject().getSupplierChequeDao();
            return dao.findChequesByDateRange(dateFrom, dateTo);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getChequeListByDateRange(): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public List<SupplierChequeBean> getChequeListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier)
    {
        try 
        {
            MySQLSupplierChequeDaoImpl dao = this.getDaoFactoryObject().getSupplierChequeDao();
            return dao.findChequesByDateAndSupplier(dateFrom, dateTo, supplier);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getChequeListByDateAndSupplier(): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public void saveOrUpdateCheque(SupplierChequeBean cheque)
    {
        try 
        {
            MySQLSupplierChequeDaoImpl dao = this.getDaoFactoryObject().getSupplierChequeDao();
            if (dao.findSingleChequeById( cheque.getId() ) == null)
            {
                dao.insertCheque(cheque);
            }
            else
            {
                dao.updateCheque(cheque);
            }
            
            // Update status
            this.updateSupplierChangedStatus(cheque.getSupplierId(), true);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "saveOrUpdateCheque(): exception (" + ex.getMessage() + ")");                        
        }
    }
    
    @Override
    public void deleteCheque(SupplierChequeBean cheque)
    {
        try 
        {
            MySQLSupplierChequeDaoImpl dao = this.getDaoFactoryObject().getSupplierChequeDao();
            dao.deleteCheque(cheque);
            
            // Update status
            this.updateSupplierChangedStatus(cheque.getSupplierId(), true);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "deleteCheque: exception (" + ex.getMessage() + ")");            
        }
    }
    
    @Override
    public SupplierFuelBean getFuelById(String id)
    {
        try 
        {
            MySQLSupplierFuelDaoImpl dao = this.getDaoFactoryObject().getSupplierFuelDao();
            return dao.findSingleFuelById(id);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getFuelById(): exception (" + ex.getMessage() + ")");
            return null;
        }
    }
            
    @Override
    public List<SupplierFuelBean> getFuelListByDateRange(long dateFrom, long dateTo)
    {
        try 
        {
            MySQLSupplierFuelDaoImpl dao = this.getDaoFactoryObject().getSupplierFuelDao();
            return dao.findFuelsByDateRange(dateFrom, dateTo);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getFuelListByDateRange(): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public List<SupplierFuelBean> getFuelListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier)
    {
        try 
        {
            MySQLSupplierFuelDaoImpl dao = this.getDaoFactoryObject().getSupplierFuelDao();
            return dao.findFuelsByDateAndSupplier(dateFrom, dateTo, supplier);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getFuelListByDateAndSupplier(): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public void saveOrUpdateFuel(SupplierFuelBean fuel)
    {
        try 
        {
            MySQLSupplierFuelDaoImpl dao = this.getDaoFactoryObject().getSupplierFuelDao();
            if (dao.findSingleFuelById( fuel.getId() ) == null)
            {
                dao.insertFuel(fuel);
            }
            else
            {
                dao.updateFuel(fuel);
            }
            
            // Update status
            this.updateSupplierChangedStatus(fuel.getSupplierId(), true);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "saveOrUpdateFuel(): exception (" + ex.getMessage() + ")");            
        }
    }
    
    @Override
    public void deleteFuel(SupplierFuelBean fuel)
    {
        try 
        {
            MySQLSupplierFuelDaoImpl dao = this.getDaoFactoryObject().getSupplierFuelDao();
            dao.deleteFuel(fuel);
            
            // Update status
            this.updateSupplierChangedStatus(fuel.getSupplierId(), true);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "deleteFuel(): exception (" + ex.getMessage() + ")");            
        }
    }
    
    @Override
    public SupplierMiscBean getMiscById(String id)
    {
        try 
        {
            MySQLSupplierMiscDaoImpl dao = this.getDaoFactoryObject().getSupplierMiscDao();
            return dao.findSingleMiscById(id);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getMiscById(): exception (" + ex.getMessage() + ")");
            return null;
        }
    }
            
    @Override
    public List<SupplierMiscBean> getMiscListByDateRange(long dateFrom, long dateTo)
    {
        try 
        {
            MySQLSupplierMiscDaoImpl dao = this.getDaoFactoryObject().getSupplierMiscDao();
            return dao.findMiscsByDateRange(dateFrom, dateTo);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getMiscListByDateRange): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public List<SupplierMiscBean> getMiscListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier)
    {
        try 
        {
            MySQLSupplierMiscDaoImpl dao = this.getDaoFactoryObject().getSupplierMiscDao();
            return dao.findMiscsByDateAndSupplier(dateFrom, dateTo, supplier);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getMiscListByDateAndSupplier(): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public void saveOrUpdateMisc(SupplierMiscBean misc)
    {
        try 
        {
            MySQLSupplierMiscDaoImpl dao = this.getDaoFactoryObject().getSupplierMiscDao();
            if (dao.findSingleMiscById( misc.getId() ) == null)
            {
                dao.insertMisc(misc);
            }
            else
            {
                dao.updateMisc(misc);
            }
            
            // Update status
            this.updateSupplierChangedStatus(misc.getSupplierId(), true);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "saveOrUpdateMisc(): exception (" + ex.getMessage() + ")");            
        }
    }
    
    @Override
    public void deleteMisc(SupplierMiscBean misc)
    {
        try 
        {
            MySQLSupplierMiscDaoImpl dao = this.getDaoFactoryObject().getSupplierMiscDao();
            dao.deleteMisc(misc);
            
            // Update status
            this.updateSupplierChangedStatus(misc.getSupplierId(), true);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "deleteMisc(): exception (" + ex.getMessage() + ")");
        }
    }
    
    @Override
    public SupplierCashBean getCashById(String id)
    {
        try 
        {
            MySQLSupplierCashDaoImpl dao = this.getDaoFactoryObject().getSupplierCashDao();
            return dao.findSingleCashById(id);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getCashById(): exception (" + ex.getMessage() + ")");
            return null;
        }
    }
            
    @Override
    public List<SupplierCashBean> getCashListByDateRange(long dateFrom, long dateTo)
    {
        try 
        {
            MySQLSupplierCashDaoImpl dao = this.getDaoFactoryObject().getSupplierCashDao();
            return dao.findCashsByDateRange(dateFrom, dateTo);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getCashListByDateRange(): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public List<SupplierCashBean> getCashListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier)
    {
        try 
        {
            MySQLSupplierCashDaoImpl dao = this.getDaoFactoryObject().getSupplierCashDao();
            return dao.findCashsByDateAndSupplier(dateFrom, dateTo, supplier);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getCashListByDateAndSupplier(): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public void saveOrUpdateCash(SupplierCashBean cash)
    {
        try 
        {
            MySQLSupplierCashDaoImpl dao = this.getDaoFactoryObject().getSupplierCashDao();
            if (dao.findSingleCashById( cash.getId() ) == null)
            {
                dao.insertCash(cash);
            }
            else
            {
                dao.updateCash(cash);
            }
            
            // Update status
            this.updateSupplierChangedStatus(cash.getSupplierId(), true);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "saveOrUpdateCash(): exception (" + ex.getMessage() + ")");
        }
    }
    
    @Override
    public void deleteCash(SupplierCashBean cash)
    {
        try 
        {
            MySQLSupplierCashDaoImpl dao = this.getDaoFactoryObject().getSupplierCashDao();
            dao.deleteCash(cash);
            
            // Update status
            this.updateSupplierChangedStatus(cash.getSupplierId(), true);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "deleteCash(): exception (" + ex.getMessage() + ")");
        }
    }
    
    @Override
    public SupplierWithdrawalBean getWithdrawalById(String id)
    {
        try 
        {
            MySQLSupplierWithdrawalDaoImpl dao = this.getDaoFactoryObject().getSupplierWithdrawalDao();
            return dao.findSingleWithdrawalById(id);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getWithdrawalById(): exception (" + ex.getMessage() + ")");
            return null;
        }
    }
            
    @Override
    public List<SupplierWithdrawalBean> getWithdrawalListByDateRange(long dateFrom, long dateTo)
    {
        try 
        {
            MySQLSupplierWithdrawalDaoImpl dao = this.getDaoFactoryObject().getSupplierWithdrawalDao();
            return dao.findWithdrawalsByDateRange(dateFrom, dateTo);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getWithdrawalListByDateRange(): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public List<SupplierWithdrawalBean> getWithdrawalListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier)
    {
        try 
        {
            MySQLSupplierWithdrawalDaoImpl dao = this.getDaoFactoryObject().getSupplierWithdrawalDao();
            return dao.findWithdrawalsByDateAndSupplier(dateFrom, dateTo, supplier);
        }
        catch (Exception ex)
        {            
            MyLogger.logError(getClass(), "getWithdrawalListByDateAndSupplier(): exception (" + ex.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public void saveOrUpdateWithdrawal(SupplierWithdrawalBean cash)
    {
        try 
        {
            MySQLSupplierWithdrawalDaoImpl dao = this.getDaoFactoryObject().getSupplierWithdrawalDao();
            if (dao.findSingleWithdrawalById( cash.getId() ) == null)
            {
                dao.insertWithdrawal(cash);
            }
            else
            {
                dao.updateWithdrawal(cash);
            }
            
            // Update status
            this.updateSupplierChangedStatus(cash.getSupplierId(), true);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "saveOrUpdate(): exception (" + ex.getMessage() + ")");
        }
    }
    
    @Override
    public void deleteWithdrawal(SupplierWithdrawalBean cash)
    {
        try 
        {
            MySQLSupplierWithdrawalDaoImpl dao = this.getDaoFactoryObject().getSupplierWithdrawalDao();
            dao.deleteWithdrawal(cash);

            // Update status
            this.updateSupplierChangedStatus(cash.getSupplierId(), true);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "deleteWithdrawal(): exception (" + ex.getMessage() + ")");
        }
    }
    
    @Override
    public void saveOrUpdateSummary(SupplierSummaryBean bean)
    {
        try 
        {
            MySQLSupplierSummaryDaoImpl dao = this.getDaoFactoryObject().getSupplierSummaryDao();
            if (dao.findSingleSummaryById( bean.getId() ) == null)
            {
                dao.insertSummary(bean);
            }
            else
            {
                dao.updateSummary(bean);
            }
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "saveOrUpdateSummary(): exception (" + ex.getMessage() + ")");
        }
    }
    
    @Override
    public void updateSummary(String supplierId)
    {
        try 
        {
            MySQLSupplierDaoImpl supplierDao = SupplierServiceHandler.this.getDaoFactoryObject().getSupplierDao();
            MySQLSupplierSummaryDaoImpl summaryDao = SupplierServiceHandler.this.getDaoFactoryObject().getSupplierSummaryDao();
            MySQLInTransactionDaoImpl transactionDao = SupplierServiceHandler.this.getDaoFactoryObject().getInTransactionDao();
            MySQLInTransactionLineDaoImpl transactionLineDao = SupplierServiceHandler.this.getDaoFactoryObject().getInTransactionLineDao();
            MySQLSupplierCashDaoImpl cashDao = SupplierServiceHandler.this.getDaoFactoryObject().getSupplierCashDao();
            MySQLSupplierChequeDaoImpl chequeDao = SupplierServiceHandler.this.getDaoFactoryObject().getSupplierChequeDao();
            MySQLSupplierFuelDaoImpl fuelDao = SupplierServiceHandler.this.getDaoFactoryObject().getSupplierFuelDao();
            MySQLSupplierMiscDaoImpl miscDao = SupplierServiceHandler.this.getDaoFactoryObject().getSupplierMiscDao();

            // Remove existing
            summaryDao.deleteSummaryBySupplier(supplierId);

            int startingYear = 2017;
            int endingYear = Calendar.getInstance().get(Calendar.YEAR);

            double lastMonthBalance = 0.0;

            for (int i = startingYear ; i <= endingYear ; i++) {
                for (int j = 0 ; j < 24 ; j++) {
                    int month = (int) Math.ceil(j/2);

                    Calendar givenDay = Calendar.getInstance();
                    givenDay.set(Calendar.YEAR, i);
                    givenDay.set(Calendar.MONTH, month);
                    givenDay.set(Calendar.DAY_OF_MONTH, 15);

                    int firstDate;
                    int endDate;
                    if ((j%2)==0) {
                        firstDate = 1;
                        endDate = 15;
                    } else {
                        firstDate = 16;
                        endDate = givenDay.getActualMaximum(Calendar.DAY_OF_MONTH);
                    }

                    Calendar firstDay = Calendar.getInstance();
                    firstDay.set(Calendar.YEAR, i);
                    firstDay.set(Calendar.MONTH, month);
                    firstDay.set(Calendar.DAY_OF_MONTH, firstDate);
                    firstDay.set(Calendar.HOUR_OF_DAY, 0);
                    firstDay.set(Calendar.MINUTE, 0);
                    firstDay.set(Calendar.SECOND, 0);

                    Calendar endDay = Calendar.getInstance();
                    endDay.set(Calendar.YEAR, i);
                    endDay.set(Calendar.MONTH, month);
                    endDay.set(Calendar.DAY_OF_MONTH, endDate);
                    endDay.set(Calendar.HOUR_OF_DAY, 23);
                    endDay.set(Calendar.MINUTE, 59);
                    endDay.set(Calendar.SECOND, 59);

                    // Retrieve supplier from database
                    SupplierBean supplier = supplierDao.findSingleSupplierById(supplierId);

                    // Retrieve summary from Database
                    SupplierSummaryBean summary = summaryDao.findSummaryBySupplierAndDate(supplierId, firstDay.getTimeInMillis());

                    List<InTransactionBean> transactionList = transactionDao.findInTransactionsByDateAndSupplier(firstDay.getTimeInMillis(), endDay.getTimeInMillis(), supplier);
                    List<SupplierCashBean> cashList = cashDao.findCashsByDateAndSupplier(firstDay.getTimeInMillis(), endDay.getTimeInMillis(), supplier);
                    List<SupplierChequeBean> chequeList = chequeDao.findChequesByDateAndSupplier(firstDay.getTimeInMillis(), endDay.getTimeInMillis(), supplier);
                    List<SupplierFuelBean> fuelList = fuelDao.findFuelsByDateAndSupplier(firstDay.getTimeInMillis(), endDay.getTimeInMillis(), supplier);
                    List<SupplierMiscBean> miscList = miscDao.findMiscsByDateAndSupplier(firstDay.getTimeInMillis(), endDay.getTimeInMillis(), supplier);

                    double totalCreditAmount = 0.0d;
                    double totalDebitAmount = 0.0d;

                    for (InTransactionBean bean : transactionList)
                    {
                        List<InTransactionLineBean> lineList = transactionLineDao.findInTransactionLinesByInTransactionId(bean.getId());
                        if (lineList.size()==0)
                            continue;

                        totalDebitAmount = totalDebitAmount + bean.getTotalPrice();
                    }

                    for (SupplierCashBean bean : cashList)
                    {
                        //totalCreditAmount = totalCreditAmount + bean.getCashAmount();
                        totalDebitAmount = totalDebitAmount + bean.getCashAmount();
                    }

                    for (SupplierChequeBean bean : chequeList)
                    {
                        totalCreditAmount = totalCreditAmount + bean.getChequeAmount();
                    }

                    for (SupplierFuelBean bean : fuelList)
                    {
                        totalCreditAmount = totalCreditAmount + bean.getFuelTotalPrice();
                    }

                    for (SupplierMiscBean bean : miscList)
                    {
                        totalCreditAmount = totalCreditAmount + bean.getMiscAmount();
                    }

                    double currentMonthBalance = lastMonthBalance + totalDebitAmount - totalCreditAmount;
                    System.err.println("> " + lastMonthBalance + " + " + totalDebitAmount + " - " + totalCreditAmount + " = " + currentMonthBalance);

                    summary.setTotalCreditAmount(MathUtil.round(totalCreditAmount, 2));
                    summary.setTotalDebitAmount(MathUtil.round(totalDebitAmount, 2));
                    summary.setBalance(MathUtil.round(lastMonthBalance + totalDebitAmount - totalCreditAmount, 2));
                    lastMonthBalance = summary.getBalance();

                    if ("".equalsIgnoreCase(summary.getId()))
                    {
                        summary.generateId();
                        summaryDao.insertSummary(summary);
                    }
                    else
                    {
                        summaryDao.updateSummary(summary);
                    }
                }
            }

            // Update status
            updateSupplierChangedStatus(supplierId, false);
        } 
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "updateSummary(): exception (" + ex.getMessage() + ")");
        }
    }
        
    public SupplierSummaryBean getSummaryBySupplierAndDate(String supplierId, long anyday)
    {
        try 
        {
            MySQLSupplierSummaryDaoImpl dao = this.getDaoFactoryObject().getSupplierSummaryDao();
            return dao.findSummaryBySupplierAndDate(supplierId, anyday);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "getSummaryBySupplierAndDate(): exception (" + ex.getMessage() + ")");
            return null;
        }
    }
    
    //--------------------------------------------------------------------------
    // SupplySummarySavingBean
        
    @Override
    public void saveOrUpdateSummarySaving(SupplierSummarySavingBean bean)
    {
        try 
        {
            MySQLSupplierSummarySavingDaoImpl dao = this.getDaoFactoryObject().getSupplierSummarySavingDao();
            if (dao.findSingleSummarySavingById( bean.getId() ) == null)
            {
                dao.insertSummarySaving(bean);
            }
            else
            {
                dao.updateSummarySaving(bean);
            }
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "saveOrUpdateSummarySaving(): exception (" + ex.getMessage() + ")");
        }
    }
    
    @Override
    public void updateSummarySaving(String supplierId)
    {
        try 
        {
            MySQLSupplierDaoImpl supplierDao = 
                    SupplierServiceHandler.this.getDaoFactoryObject().getSupplierDao();
            MySQLSupplierSummarySavingDaoImpl summarySavingDao = 
                    SupplierServiceHandler.this.getDaoFactoryObject().getSupplierSummarySavingDao();
            MySQLInTransactionDaoImpl transactionDao = 
                    SupplierServiceHandler.this.getDaoFactoryObject().getInTransactionDao();
            MySQLSupplierWithdrawalDaoImpl withdrawalDao = 
                    SupplierServiceHandler.this.getDaoFactoryObject().getSupplierWithdrawalDao();

            summarySavingDao.deleteSummarySavingsBySupplier(supplierId);

            int startingYear = 2017;
            int endingYear = Calendar.getInstance().get(Calendar.YEAR);

            double lastMonthBalance = 0.0;

            for (int i = startingYear ; i <= endingYear ; i++) {
                for (int j = 0 ; j < 24 ; j++) {
                    int month = (int) Math.ceil(j/2);

                    Calendar givenDay = Calendar.getInstance();
                    givenDay.set(Calendar.YEAR, i);
                    givenDay.set(Calendar.MONTH, month);
                    givenDay.set(Calendar.DAY_OF_MONTH, 15);

                    int firstDate;
                    int endDate;

                    if (j%2 == 0) {
                        firstDate = 1;
                        endDate = 15;
                    } else {
                        firstDate = 16;
                        endDate = givenDay.getActualMaximum(Calendar.DAY_OF_MONTH);
                    }

                    Calendar firstDay = Calendar.getInstance();
                    firstDay.set(Calendar.YEAR, i);
                    firstDay.set(Calendar.MONTH, month);
                    firstDay.set(Calendar.DAY_OF_MONTH, firstDate);
                    firstDay.set(Calendar.HOUR_OF_DAY, 0);
                    firstDay.set(Calendar.MINUTE, 0);
                    firstDay.set(Calendar.SECOND, 0);

                    Calendar endDay = Calendar.getInstance();
                    endDay.set(Calendar.YEAR, i);
                    endDay.set(Calendar.MONTH, month);
                    endDay.set(Calendar.DAY_OF_MONTH, endDate);
                    endDay.set(Calendar.HOUR_OF_DAY, 23);
                    endDay.set(Calendar.MINUTE, 59);
                    endDay.set(Calendar.SECOND, 59);

                    // Retrieve supplier from database
                    SupplierBean supplier = supplierDao.findSingleSupplierById(supplierId);

                    // Retrieve summary from Database
                    SupplierSummarySavingBean summarySaving = summarySavingDao.findSummarySavingBySupplierAndDate(supplierId, firstDay.getTimeInMillis());

                    List<InTransactionBean> transactionList = transactionDao.findInTransactionsByDateAndSupplier(firstDay.getTimeInMillis(), endDay.getTimeInMillis(), supplier);
                    List<SupplierWithdrawalBean> withdrawalList = withdrawalDao.findWithdrawalsByDateAndSupplier(firstDay.getTimeInMillis(), endDay.getTimeInMillis(), supplier);

                    double totalSavingAmount = 0.0d;
                    double totalWithdrawalAmount = 0.0d;

                    for (InTransactionBean bean : transactionList)
                    {
                        totalSavingAmount = totalSavingAmount + bean.getTotalSaving();
                    }

                    for (SupplierWithdrawalBean bean : withdrawalList)
                    {
                        totalWithdrawalAmount = totalWithdrawalAmount + bean.getCashAmount();
                    }

                    if (summarySaving != null)
                    {
                        summarySaving.setTotalSavingAmount(totalSavingAmount);
                        summarySaving.setTotalWithdrawalAmount(totalWithdrawalAmount);
                        summarySaving.setBalance(lastMonthBalance + totalSavingAmount - totalWithdrawalAmount);
                        lastMonthBalance = summarySaving.getBalance();

                        if ("".equalsIgnoreCase(summarySaving.getId()))
                        {
                            summarySaving.generateId();
                            summarySavingDao.insertSummarySaving(summarySaving);
                        }
                        else
                        {
                            summarySavingDao.updateSummarySaving(summarySaving);
                        }
                    }
                }
            }

            // Update status
            updateSupplierChangedStatus(supplierId, false);
        } 
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "updateSummarySaving(): exception (" + ex.getMessage() + ")");
        }
    }
            
    @Override
    public SupplierSummarySavingBean getSummarySavingBySupplierAndDate(String supplierId, long anyday)
    {
        try 
        {
            MySQLSupplierSummarySavingDaoImpl dao = this.getDaoFactoryObject().getSupplierSummarySavingDao();
            return dao.findSummarySavingBySupplierAndDate(supplierId, anyday);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "getSummarySavingBySupplierAndDate(): exception (" + ex.getMessage() + ")");
            return null;
        }
    }
    
    private void updateSupplierChangedStatus(String supplier, boolean changed) {
        
        try 
        {
            MySQLSupplierDaoImpl supplierDao = getDaoFactoryObject().getSupplierDao();
            SupplierBean supplierObj = supplierDao.findSingleSupplierById(supplier);
            supplierObj.setChanged(changed);
            
            supplierDao.updateSupplier( supplierObj );
        } 
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "updateSupplierChangeStatus(): exception (" + ex.getMessage() + ")");
        }
    }    
}
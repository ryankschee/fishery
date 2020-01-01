package com.ryanworks.fishery.server.service;

import com.ryanworks.fishery.server.dao.DaoException;
import com.ryanworks.fishery.server.dao.impl.MySQLInTransactionDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLInTransactionLineDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSupplierDaoImpl;
import com.ryanworks.fishery.shared.bean.InTransactionBean;
import com.ryanworks.fishery.shared.bean.InTransactionLineBean;
import com.ryanworks.fishery.shared.bean.ItemBean;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.service.InTransactionServiceIntf;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.util.ArrayList;
import java.util.List;

public class InTransactionServiceHandler
    extends MyServiceHandler
    implements InTransactionServiceIntf 
{
    @Override
    public InTransactionLineBean getInTransactionLineBySalesLine(String salesLineId)
    {
        try 
        {
            MySQLInTransactionLineDaoImpl dao = this.getDaoFactoryObject().getInTransactionLineDao();
            return dao.findSingleInTransactionLineBySalesLineId(salesLineId);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "getInTransactionLineBySalesLine(): exception (" + ex.getMessage() + ")");
            return null;
        }
    }
    
    @Override
    public int getTripNoBySupplierIdAndDate(String supplierId, long dateTime)
    {
        try 
        {
            MySQLInTransactionDaoImpl dao = this.getDaoFactoryObject().getInTransactionDao();
            return dao.getTripNoBySupplierIdAndDate(supplierId, dateTime);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "getTripNoBySupplierIdAndDate(): exception (" + ex.getMessage() + ")");
            return -1;
        }
    }

    @Override
    public int saveTransaction(InTransactionBean transactionBean) 
    {
        try 
        {
            MySQLInTransactionDaoImpl transactionDao = InTransactionServiceHandler.this.getDaoFactoryObject().getInTransactionDao();
            MySQLInTransactionLineDaoImpl lineDao = InTransactionServiceHandler.this.getDaoFactoryObject().getInTransactionLineDao();

            for (InTransactionLineBean lineBean : transactionBean.getLineList())
            {                
                if (lineDao.findSingleInTransactionLineById(lineBean.getId())==null)
                {
                    lineDao.insertInTransactionLine(lineBean);
                }
                else
                {
                    lineDao.updateInTransactionLine(lineBean);
                }
            }

            if (transactionDao.findSingleInTransactionById(transactionBean.getId())==null) 
            {
                transactionDao.insertInTransaction(transactionBean);
            }
            else
            {
                transactionDao.updateInTransaction(transactionBean);
            }

            // Update status
            updateSupplierChangedStatus(transactionBean.getSupplierId(), true);

            return 1;
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "saveTransaction(): exception (" + e.getMessage() + ")");
            return 0;
        }
    }
        
    @Override
    public int saveOrUpdateTransactionLine(InTransactionLineBean lineBean)
    {
        try 
        {
            MySQLInTransactionLineDaoImpl lineDao = this.getDaoFactoryObject().getInTransactionLineDao();
            
            if (lineDao.findSingleInTransactionLineById(lineBean.getId())==null)
            {
                lineDao.insertInTransactionLine(lineBean);
            }
            else
            {
                lineDao.updateInTransactionLine(lineBean);
            }
                
            // Update status
            this.updateSupplierChangedStatus(lineBean.getSupplierId(), true);
            
            return 1;
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "saveOrUpdateTransactionLine(): exception (" + e.getMessage() + ")");
            return -1;
        }
    }
    
    @Override
    public InTransactionBean getTransactionById(String transactionId)
    {
        try 
        {
            MySQLInTransactionDaoImpl transactionDao = this.getDaoFactoryObject().getInTransactionDao();
            MySQLInTransactionLineDaoImpl transactionLineDao = this.getDaoFactoryObject().getInTransactionLineDao();
            
            InTransactionBean bean = transactionDao.findSingleInTransactionById(transactionId);
            if (bean != null)
            {
                bean.setLineList( transactionLineDao.findInTransactionLinesByInTransactionId(transactionId) );
            }
            
            return bean;
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "getTransactonById(): exception (" + e.getMessage() + ")");
            return null;
        }
    }
    
    @Override
    public List<InTransactionLineBean> getTransactionLineByTransactionId(String transactionId)
    {
        try 
        {
            MySQLInTransactionLineDaoImpl lineDao = this.getDaoFactoryObject().getInTransactionLineDao();
            return lineDao.findInTransactionLinesByInTransactionId(transactionId);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "getTransactionLineByTransactionId(): exception (" + e.getMessage() + ")");
            return null;
        }
    }
    
    @Override
    public List<InTransactionLineBean> getTransactionLineByItemAndDate(ItemBean item, long dateTime)
    {
        try 
        {
            MySQLInTransactionLineDaoImpl lineDao = this.getDaoFactoryObject().getInTransactionLineDao();
            return lineDao.findInTransactionLinesByItemAndDate(item, dateTime);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "getTransactionLineByItemAndDate(): exception (" + e.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public List<InTransactionBean> getTransactionsByDate(long timeInMillis, boolean fullBean)
    {
        try 
        {
            MySQLInTransactionDaoImpl transactionDao = this.getDaoFactoryObject().getInTransactionDao();
            return transactionDao.findInTransactionsByDate(timeInMillis);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "getTransactionsByDate(): exception (" + e.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public List<InTransactionBean> getTransactionListByDateRange(long dateFrom, long dateTo, boolean fullBean)
    {
        try 
        {
            MySQLInTransactionDaoImpl transactionDao = this.getDaoFactoryObject().getInTransactionDao();
            return transactionDao.findInTransactionsByDateRange(dateFrom, dateTo);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "getTransactionListByDateRange(): exception (" + e.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public List<InTransactionBean> getTransactionListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier, boolean fullBean)
    {
        try 
        {
            MySQLInTransactionDaoImpl transactionDao = this.getDaoFactoryObject().getInTransactionDao();
            return transactionDao.findInTransactionsByDateAndSupplier(dateFrom, dateTo, supplier);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "getTransactionListByDateAndSupplier(): exception (" + e.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public List<InTransactionBean> getSavingListByDateAndSupplier(long dateFrom, long dateTo, SupplierBean supplier)
    {
        try 
        {
            MySQLInTransactionDaoImpl transactionDao = this.getDaoFactoryObject().getInTransactionDao();
            return transactionDao.findSavingTransactionsByDateAndSupplier(dateFrom, dateTo, supplier);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "getSavingListByDateAndSupplier(): exception (" + e.getMessage() + ")");
            return new ArrayList();
        }
    }
    
    @Override
    public void deleteTransactionsBySupplier(SupplierBean supplierObj)
    {
        try 
        {
            MySQLInTransactionDaoImpl transactionDao = this.getDaoFactoryObject().getInTransactionDao();
            MySQLInTransactionLineDaoImpl lineDao = this.getDaoFactoryObject().getInTransactionLineDao();
                        
            List<InTransactionBean> transactionList = transactionDao.findInTransactionsBySupplier(supplierObj);            
            for (InTransactionBean transactionObj : transactionList) {
                
                List<InTransactionLineBean> lineList = 
                        lineDao.findInTransactionLinesByInTransactionId(transactionObj.getId());
                for (InTransactionLineBean lineObj : lineList) {
                    lineDao.deleteInTransactionLine(lineObj);
                }
                
                transactionDao.deleteInTransaction(transactionObj);
            }
            
            // Update status
            this.updateSupplierChangedStatus(supplierObj.getId(), true);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "deleteTransactionsBySupplier(): exception (" + e.getMessage() + ")");
        }
    }
    
    @Override
    public void deleteTransactionLine(InTransactionLineBean lineBean)
    {
        try 
        {
            MySQLInTransactionLineDaoImpl lineDao = this.getDaoFactoryObject().getInTransactionLineDao();
            lineDao.deleteInTransactionLine(lineBean);

            // Update status
            this.updateSupplierChangedStatus(lineBean.getSupplierId(), true);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "deleteTransactionLine(): exception (" + e.getMessage() + ")");
        }
    }
    
    @Override
    public void deleteTransaction(InTransactionBean transactionObj)
    {
        try 
        {
            MySQLInTransactionDaoImpl dao = this.getDaoFactoryObject().getInTransactionDao();
            dao.deleteInTransaction(transactionObj);
            
            // Update status
            this.updateSupplierChangedStatus(transactionObj.getSupplierId(), true);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "deleteTransaction(): exception (" + e.getMessage() + ")");
        }
    }
    
    private void updateSupplierChangedStatus(String supplierId, boolean changed) {
        
        try 
        {
            MySQLSupplierDaoImpl supplierDao = getDaoFactoryObject().getSupplierDao();
            SupplierBean supplierObj = supplierDao.findSingleSupplierById(supplierId);
            supplierObj.setChanged(changed);
            
            supplierDao.updateSupplier( supplierObj );
        } 
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "updateSupplierChangedStatus(): exception (" + ex.getMessage() + ")");
        }
    }    
}
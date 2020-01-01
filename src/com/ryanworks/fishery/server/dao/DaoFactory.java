package com.ryanworks.fishery.server.dao;
 
import com.ryanworks.fishery.server.dao.impl.*;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
 
public abstract class DaoFactory
{
    public static final int MYSQL = 1;
    static Map cachedDAO;
     
    public static DaoFactory getDAOFactory(int whichFactory) 
    {
        switch (whichFactory) 
        {
            case MYSQL:
                return new MySQLDaoFactory();
            default:
                return null;
        }
    }
 
    protected Object createDAO(Class classObj)
        throws DaoException
    {
        // Check if pool of DAO is available
        if (cachedDAO == null) 
        {
            cachedDAO = Collections.synchronizedMap(new HashMap());
        }
     
        // Check if pool contain the respective DAO object, if YES, then reuse
        Object daoObj = cachedDAO.get(classObj.getName()); 
        if (daoObj != null) 
        {
            return daoObj;
        }
        
        // No cached DAO object, so create a new one and insert it into pool of 
        // cache for reuse purpose.
        try
        {
            daoObj = classObj.newInstance();
 
            cachedDAO.put(classObj.getName(), daoObj);
            return daoObj;
        }
        catch (InstantiationException e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            throw new DaoException(e);
        }
        catch (IllegalAccessException e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            throw new DaoException(e);
        } 
        finally {
            return daoObj;
        }
    }
    
    public MySQLUserDaoImpl getUserDao() 
        throws DaoException
    {
        return (MySQLUserDaoImpl) createDAO(MySQLUserDaoImpl.class);
    }
     
    public MySQLCustomerDaoImpl getCustomerDao() 
        throws DaoException
    {
        return (MySQLCustomerDaoImpl) createDAO(MySQLCustomerDaoImpl.class);
    }  
    
    public MySQLCustomerPaymentDaoImpl getCustomerPaymentDao() 
        throws DaoException
    {
        return (MySQLCustomerPaymentDaoImpl) createDAO(MySQLCustomerPaymentDaoImpl.class);
    }
    
    public MySQLCustomerSummaryDaoImpl getCustomerSummaryDao() 
        throws DaoException
    {
        return (MySQLCustomerSummaryDaoImpl) createDAO(MySQLCustomerSummaryDaoImpl.class);
    }
    
    public MySQLCategoryDaoImpl getCategoryDao() 
        throws DaoException
    {
        return (MySQLCategoryDaoImpl) createDAO(MySQLCategoryDaoImpl.class);
    } 
    
    public MySQLItemDaoImpl getItemDao() 
        throws DaoException
    {
        return (MySQLItemDaoImpl) createDAO(MySQLItemDaoImpl.class);
    } 
    
    public MySQLSupplierDaoImpl getSupplierDao() 
        throws DaoException
    {
        return (MySQLSupplierDaoImpl) createDAO(MySQLSupplierDaoImpl.class);
    } 
    
    public MySQLSupplierChequeDaoImpl getSupplierChequeDao() 
        throws DaoException
    {
        return (MySQLSupplierChequeDaoImpl) createDAO(MySQLSupplierChequeDaoImpl.class);
    }
    
    public MySQLSupplierFuelDaoImpl getSupplierFuelDao() 
        throws DaoException
    {
        return (MySQLSupplierFuelDaoImpl) createDAO(MySQLSupplierFuelDaoImpl.class);
    }
    
    public MySQLSupplierMiscDaoImpl getSupplierMiscDao() 
        throws DaoException
    {
        return (MySQLSupplierMiscDaoImpl) createDAO(MySQLSupplierMiscDaoImpl.class);
    }
    
    public MySQLSupplierCashDaoImpl getSupplierCashDao() 
        throws DaoException
    {
        return (MySQLSupplierCashDaoImpl) createDAO(MySQLSupplierCashDaoImpl.class);
    }
    
    public MySQLSupplierWithdrawalDaoImpl getSupplierWithdrawalDao() 
        throws DaoException
    {
        return (MySQLSupplierWithdrawalDaoImpl) createDAO(MySQLSupplierWithdrawalDaoImpl.class);
    }
    
    public MySQLInTransactionDaoImpl getInTransactionDao() 
        throws DaoException
    {
        return (MySQLInTransactionDaoImpl) createDAO(MySQLInTransactionDaoImpl.class);
    } 
    
    public MySQLInTransactionLineDaoImpl getInTransactionLineDao() 
        throws DaoException
    {
        return (MySQLInTransactionLineDaoImpl) createDAO(MySQLInTransactionLineDaoImpl.class);
    } 
    
    public MySQLSalesDaoImpl getSalesDao() 
        throws DaoException
    {
        return (MySQLSalesDaoImpl) createDAO(MySQLSalesDaoImpl.class);
    } 
    
    public MySQLSalesLineDaoImpl getSalesLineDao() 
        throws DaoException
    {
        return (MySQLSalesLineDaoImpl) createDAO(MySQLSalesLineDaoImpl.class);
    } 
    
    public MySQLSalesBucketDaoImpl getSalesBucketDao() 
        throws DaoException
    {
        return (MySQLSalesBucketDaoImpl) createDAO(MySQLSalesBucketDaoImpl.class);
    }
    
    public MySQLSupplierSummaryDaoImpl getSupplierSummaryDao() 
        throws DaoException
    {
        return (MySQLSupplierSummaryDaoImpl) createDAO(MySQLSupplierSummaryDaoImpl.class);
    }
    
    public MySQLSupplierSummarySavingDaoImpl getSupplierSummarySavingDao() 
        throws DaoException
    {
        return (MySQLSupplierSummarySavingDaoImpl) createDAO(MySQLSupplierSummarySavingDaoImpl.class);
    }
    
    public MySQLSystemDaoImpl getSystemDao() 
        throws DaoException
    {
        return (MySQLSystemDaoImpl) createDAO(MySQLSystemDaoImpl.class);
    }
}
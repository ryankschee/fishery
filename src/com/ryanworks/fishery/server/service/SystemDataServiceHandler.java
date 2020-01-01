package com.ryanworks.fishery.server.service;

import com.ryanworks.fishery.server.dao.DaoException;
import com.ryanworks.fishery.server.dao.impl.MySQLCategoryDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLInTransactionLineDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLItemDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSalesLineDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSystemDaoImpl;
import com.ryanworks.fishery.shared.bean.CategoryBean;
import com.ryanworks.fishery.shared.bean.ItemBean;
import com.ryanworks.fishery.shared.bean.SystemBean;
import com.ryanworks.fishery.shared.service.SystemDataServiceIntf;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.util.List;

public class SystemDataServiceHandler
    extends MyServiceHandler
    implements SystemDataServiceIntf 
{
    @Override
    public String getNextInTransactionId()
    {
        try 
        {
            MySQLSystemDaoImpl dao = this.getDaoFactoryObject().getSystemDao();
            SystemBean system = dao.getSystemBean();
            system.setInTransactionId( system.getInTransactionId()+1 );
            dao.updateSystem(system);
            
            return String.format( "%06d", system.getInTransactionId());            
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
    
    @Override
    public String getNextSalesId()
    {
        try 
        {
            MySQLSystemDaoImpl dao = this.getDaoFactoryObject().getSystemDao();
            SystemBean system = dao.getSystemBean();
            system.setSalesId(system.getSalesId()+1 );
            dao.updateSystem(system);
            
            return String.format( "%06d", system.getSalesId());            
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
    
    @Override
    public List<CategoryBean> getAllCategories()
    {
        try 
        {
            MySQLCategoryDaoImpl dao = this.getDaoFactoryObject().getCategoryDao();
            return dao.getAllCategories();
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
    
    @Override
    public ItemBean getItemById(String itemId)
    {
        try 
        {
            MySQLItemDaoImpl dao = this.getDaoFactoryObject().getItemDao();
            return dao.findSingleItemById(itemId);
        }
        catch (DaoException ex)
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
    
    @Override
    public List<ItemBean> getItemsByCategoryId(String categoryId)
    {
        try 
        {
            MySQLItemDaoImpl dao = this.getDaoFactoryObject().getItemDao();
            return dao.findItemsByCategoryId(categoryId);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }    

    @Override
    public void saveOrUpdateItem(ItemBean item) 
    {
        try 
        {
            MySQLItemDaoImpl dao = this.getDaoFactoryObject().getItemDao();
            if (dao.findSingleItemById( item.getId() ) == null)
            {
                dao.insertItem(item);
            }
            else
            {
                dao.updateItem(item);
                
                MySQLInTransactionLineDaoImpl inTransactionLineDao = this.getDaoFactoryObject().getInTransactionLineDao();
                inTransactionLineDao.updateItemName(item);
                
                MySQLSalesLineDaoImpl salesLineDao = this.getDaoFactoryObject().getSalesLineDao();
                salesLineDao.updateItemName(item);                
            }
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
        }
    }
    
    @Override
    public void deleteItem(ItemBean item) 
    {
        try 
        {
            MySQLItemDaoImpl dao = this.getDaoFactoryObject().getItemDao();
            dao.deleteItem(item);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
        }
    }
}
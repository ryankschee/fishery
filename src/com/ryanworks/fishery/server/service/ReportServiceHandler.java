package com.ryanworks.fishery.server.service;

import com.ryanworks.fishery.server.dao.DaoException;
import com.ryanworks.fishery.server.dao.impl.MySQLInTransactionDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLInTransactionLineDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSalesDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSalesLineDaoImpl;
import com.ryanworks.fishery.shared.bean.InTransactionBean;
import com.ryanworks.fishery.shared.bean.InTransactionLineBean;
import com.ryanworks.fishery.shared.bean.ItemBean;
import com.ryanworks.fishery.shared.bean.ReportStockInBean;
import com.ryanworks.fishery.shared.bean.ReportStockOutBean;
import com.ryanworks.fishery.shared.bean.SalesBean;
import com.ryanworks.fishery.shared.bean.SalesLineBean;
import com.ryanworks.fishery.shared.service.ReportServiceIntf;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportServiceHandler
    extends MyServiceHandler
    implements ReportServiceIntf 
{
    
    @Override
    public List<ReportStockInBean> getStockInByDateAndItem(long timeInMillis, List<ItemBean> itemList)
    {
        try 
        {
            MySQLInTransactionDaoImpl inTransactionDao = this.getDaoFactoryObject().getInTransactionDao();
            MySQLInTransactionLineDaoImpl inTransactionLineDao = this.getDaoFactoryObject().getInTransactionLineDao();
         
            List<ReportStockInBean> stockInList = new ArrayList<>();
            
            // Get list of transaction from particular date.
            List<InTransactionBean> inTransactionList = inTransactionDao.findInTransactionsByDate(timeInMillis);
            
            // Iterate through each transaction
            for (InTransactionBean inTransaction : inTransactionList)
            {
                // Get list of transaction line from it
                List<InTransactionLineBean> inTransactionLineList 
                        = inTransactionLineDao.findInTransactionLinesByInTransactionId( inTransaction.getId() );
                
                for (InTransactionLineBean lineBean : inTransactionLineList)
                {
                    for (ItemBean item : itemList)
                    {
                        if (lineBean.getItemNewName().equalsIgnoreCase( item.getName() ))
                        {
                            ReportStockInBean stockInBean = new ReportStockInBean();
                            stockInBean.setItemName( lineBean.getItemName() );
                            stockInBean.setItemNewName( lineBean.getItemNewName()==null || lineBean.getItemNewName().length()==0 ? lineBean.getItemName() : lineBean.getItemNewName() );
                            stockInBean.setSupplierId( inTransaction.getSupplierId() );
                            stockInBean.setInvoiceNo( inTransaction.getTransactionNo() );
                            stockInBean.setWeight( lineBean.getWeight() );
                            stockInBean.setCustomerId( lineBean.getCustomerId() );
                            stockInBean.setBucketNo( lineBean.getBucketNo() );

                            stockInList.add(stockInBean);
                        }
                    }
                }
            }
            
            boolean itemInside = false;
            // Re-insert item which has no data
            for (ItemBean item : itemList)
            {
                itemInside = false;
                
                innerloop:
                for (ReportStockInBean stockInBean : stockInList)
                {
                    if (item.getName().equalsIgnoreCase( stockInBean.getItemNewName() ))
                    {
                        itemInside = true;
                        break innerloop;
                    }
                }
                
                if (itemInside == false)
                {
                    ReportStockInBean stockInBean = new ReportStockInBean();
                    stockInBean.setItemName( item.getName() );
                    stockInBean.setItemNewName( item.getName() );
                    stockInBean.setSupplierId( "" );
                    stockInBean.setInvoiceNo( "" );
                    stockInBean.setWeight( 0 );
                    stockInBean.setCustomerId( "" );
                    stockInBean.setBucketNo( "" );

                    stockInList.add(stockInBean);
                }
            }
            
            // Sort list by item new name
            Collections.sort( stockInList );
            
            return stockInList;
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }
    
    @Override
    public List<ReportStockOutBean> getStockOutByDateAndItem(long timeInMillis, List<ItemBean> itemList)
    {
        try 
        { 
            MySQLSalesDaoImpl salesDao = this.getDaoFactoryObject().getSalesDao();
            MySQLSalesLineDaoImpl salesLineDao = this.getDaoFactoryObject().getSalesLineDao();
         
            List<ReportStockOutBean> stockOutList = new ArrayList<>();
            
            List<SalesBean> salesList = salesDao.findSalesByDate(timeInMillis);
            for (SalesBean sales : salesList)
            {
                List<SalesLineBean> salesLineList 
                        = salesLineDao.findSalesLinesBySalesId( sales.getId() );
                
                for (SalesLineBean lineBean : salesLineList)
                {
                    // Only show line item which is not assigned from Supplier directly (from inbound screen)
                    if (lineBean.getSupplierId()==null || "".equalsIgnoreCase(lineBean.getSupplierId())) 
                    {                    
                        for (ItemBean item : itemList)
                        {                    
                            if (lineBean.getItemNewName().equalsIgnoreCase( item.getName() ))
                            {
                                ReportStockOutBean stockOutBean = new ReportStockOutBean();
                                stockOutBean.setItemName( lineBean.getItemName() );
                                stockOutBean.setItemNewName( lineBean.getItemNewName()==null || lineBean.getItemNewName().length()==0 ? lineBean.getItemName() : lineBean.getItemNewName() );
                                stockOutBean.setWeight( lineBean.getWeight() );
                                stockOutBean.setBucketNo( lineBean.getBucketNo() );
                                stockOutBean.setCustomerId( lineBean.getCustomerId() );

                                stockOutList.add(stockOutBean);
                            }
                        }
                    }
                }
            }
            
            // Sort list by item new name
            Collections.sort( stockOutList );
            
            return stockOutList;
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return null;
        }
    }    
}
package com.ryanworks.fishery.server.service;

import com.ryanworks.fishery.server.dao.DaoException;
import com.ryanworks.fishery.server.dao.impl.MySQLCustomerDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSalesBucketDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSalesDaoImpl;
import com.ryanworks.fishery.server.dao.impl.MySQLSalesLineDaoImpl;
import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.SalesBean;
import com.ryanworks.fishery.shared.bean.SalesBucketBean;
import com.ryanworks.fishery.shared.bean.SalesLineBean;
import com.ryanworks.fishery.shared.service.SalesServiceIntf;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.util.ArrayList;
import java.util.List;

public class SalesServiceHandler
    extends MyServiceHandler
    implements SalesServiceIntf 
{   
    @Override
    public int saveOrUpdateSalesLine(SalesLineBean salesLine) 
    {
        try 
        {
            if ("SL".equals(salesLine.getSupplierId()) || "SL".equals(salesLine.getSupplierName()))
            {
                MyLogger.logError(
                    this.getClass(), 
                    "EXCEPTION: Found supplier with SL in SalesLineTable (salesLine:" + 
                    salesLine.getId() + ", item:" + salesLine.getItemId() + ", weight:" + salesLine.getWeight() + ")");
                return 0;
            }
            
            // Set status 'Changed' to true
            updateCustomerChangedStatus(salesLine.getCustomerId(), true);
            
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();                      
            if (lineDao.findSingleSalesLineById(salesLine.getId())==null)
            {
                return lineDao.insertSalesLine(salesLine);
            }
            else
            {
                return lineDao.updateSalesLine(salesLine) ? 1 : 0;
            }
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return -1;
        }
    }
    
    @Override
    public int saveOrUpdateSalesBucket(SalesBucketBean salesBucket) 
    {
        try 
        {            
            // Set status 'Changed' to true
            updateCustomerChangedStatus(salesBucket.getCustomerId(), true);
            
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();                      
            if (bucketDao.findSingleSalesBucketById(salesBucket.getId())==null)
            {
                return bucketDao.insertSalesBucket(salesBucket);
            }
            else
            {
                return bucketDao.updateSalesBucket(salesBucket) ? 1 : 0;
            }
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return -1;
        }
    }
    
    @Override
    public int saveOrUpdateSales(SalesBean salesBean) 
    {
        // DO NOT SAVE Sales object with empty customer
        if (salesBean == null || salesBean.getCustomerId().trim().length()==0 )
            return 0;

        try 
        {
            MySQLSalesDaoImpl salesDao = SalesServiceHandler.this.getDaoFactoryObject().getSalesDao();
            MySQLSalesLineDaoImpl lineDao = SalesServiceHandler.this.getDaoFactoryObject().getSalesLineDao();
            MySQLSalesBucketDaoImpl bucketDao = SalesServiceHandler.this.getDaoFactoryObject().getSalesBucketDao();

            SalesBean existingSalesObj = salesDao.findSingleSalesById(salesBean.getId());
            if (existingSalesObj==null) 
            {
                salesDao.insertSales(salesBean);
            }
            else
            {
                boolean saveSalesObj = true;
                // Check Data Integrity: Both has invoice number, but different invoice number.
                if (existingSalesObj.hasInvoiceNo() && salesBean.hasInvoiceNo()) {
                    if (existingSalesObj.getInvoiceNo().equals(salesBean.getInvoiceNo())==false)
                        saveSalesObj = false;
                }
                // Check Data Integrity: Server has incoive number, but client does not.
                if (existingSalesObj.hasInvoiceNo() && !salesBean.hasInvoiceNo())
                    saveSalesObj = false;

                if (saveSalesObj)
                    salesDao.updateSales(salesBean);
            }

            // Process SalesLine, take out item from SL group
            List<SalesLineBean> list = new ArrayList<>();
            for (SalesLineBean groupedLineBean : salesBean.getLineList()) {
                if ("SL".equalsIgnoreCase(groupedLineBean.getSupplierId())) {
                    List<SalesLineBean> subList = 
                            SalesServiceHandler.this.getSalesLineByGroup(salesBean.getId(), groupedLineBean.getItemNewName(), groupedLineBean.getBucketNo());
                    for (SalesLineBean realLineBean : subList) {
                        realLineBean.setUnitPrice(groupedLineBean.getUnitPrice());
                        list.add(realLineBean);
                    }
                }
                else {
                    // This is single-item
                    list.add(groupedLineBean);
                }
            }

            for (SalesLineBean lineBean : list)
            {
                if (lineDao.findSingleSalesLineById(lineBean.getId())==null)
                {
                    lineDao.insertSalesLine(lineBean);
                }
                else
                {
                    lineDao.updateSalesLine(lineBean);
                }
            }

            for (SalesBucketBean bucketBean : salesBean.getBucketList())
            {
                if (bucketDao.findSingleSalesBucketById(bucketBean.getId())==null)
                {
                    bucketDao.insertSalesBucket(bucketBean);
                }
                else
                {
                    bucketDao.updateSalesBucket(bucketBean);
                }
            }

            // Set status 'Changed' to true
            updateCustomerChangedStatus(salesBean.getCustomerId(), true);

            return 1;
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return 1;
        }
    }
        
    @Override
    public int saveSalesBucket(SalesBucketBean salesBucketBean)
    {
        try 
        {                    
            // Set status 'Changed' to true
            updateCustomerChangedStatus(salesBucketBean.getCustomerId(), true);
            
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();            
            if (bucketDao.findSingleSalesBucketById(salesBucketBean.getId())==null)
            {
                return bucketDao.insertSalesBucket(salesBucketBean);
            }
            else
            {
                bucketDao.updateSalesBucket(salesBucketBean);
                
                return 1;
            }
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return -1;
        }
    }
    
    @Override
    public int getTripNoByCustomerIdAndDate(String customerId, long dateTime)
    {
        try 
        {
            MySQLSalesDaoImpl dao = this.getDaoFactoryObject().getSalesDao();
            return dao.getTripNoByCustomerIdAndDate(customerId, dateTime);
        } 
        catch (DaoException ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
            return -1;
        }
    }

    @Override
    public SalesBean getSalesById(String salesId)
    {
        try 
        {
            MySQLSalesDaoImpl salesDao = this.getDaoFactoryObject().getSalesDao();
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();
            
            SalesBean salesObj = salesDao.findSingleSalesById(salesId);
            if (salesObj != null) {
                salesObj.setBucketList( bucketDao.findSalesBucketBySalesId(salesId) );
                salesObj.setLineList( lineDao.findSalesLinesBySalesId(salesId) );
            }
            
            return salesObj;
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public SalesLineBean getSalesLineById(String id)
    {
        try 
        {
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();
            return lineDao.findSingleSalesLineById(id);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<SalesLineBean> getSalesLineBySalesId(String salesId)
    {
        try 
        {
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();
            return lineDao.findSalesLinesBySalesId(salesId);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<SalesLineBean> getSalesLineBySalesIdAndBucketNo(String salesId, String bucketNo)
    {
        try 
        {
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();
            return lineDao.findSalesLinesBySalesIdAndBucketNo(salesId, bucketNo);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<SalesLineBean> getSalesLineByGroup(String salesId, String itemNewName, String bucketNo)
    {
        try 
        {
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();
            return lineDao.findSalesLinesByGroup(salesId, itemNewName, bucketNo);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<SalesBean> getSalesByDate(long timeInMillis, boolean fullBean)
    {
        try 
        {
            MySQLSalesDaoImpl salesDao = this.getDaoFactoryObject().getSalesDao();
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();
            
            List<SalesBean> salesList = salesDao.findSalesByDate(timeInMillis);   
            if (fullBean) {
                for (SalesBean salesObj : salesList)
                {
                    salesObj.setBucketList( bucketDao.findSalesBucketBySalesId(salesObj.getId()) );
                    salesObj.setLineList( lineDao.findSalesLinesBySalesId(salesObj.getId()) );
                }
            }
            
            return salesList;
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<SalesBean> getSalesByStatusAndDateRange(int status, long startDate, long endDate, boolean fullBean)
    {
        try 
        {
            MySQLSalesDaoImpl salesDao = this.getDaoFactoryObject().getSalesDao();
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();
            
            List<SalesBean> salesList = salesDao.findSalesByStatusAndDateRange(status, startDate, endDate);
            if (fullBean) {
                for (SalesBean salesObj : salesList)
                {
                    salesObj.setBucketList( bucketDao.findSalesBucketBySalesId(salesObj.getId()) );
                    salesObj.setLineList( lineDao.findSalesLinesBySalesId(salesObj.getId()) );
                }
            }
            return salesList;
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public SalesBean getSalesByCustomerAndDate(String customerId, long dateTime)
    {
        try 
        {
            MySQLSalesDaoImpl salesDao = this.getDaoFactoryObject().getSalesDao();
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();
            
            SalesBean salesObj = salesDao.findSalesByCustomerAndDate(customerId, dateTime);  
            if (salesObj != null)
            {
                salesObj.setBucketList( bucketDao.findSalesBucketBySalesId(salesObj.getId()) );
                salesObj.setLineList( lineDao.findSalesLinesBySalesId(salesObj.getId()) );
            }
            
            return salesObj;
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<SalesBean> getSalesByDateRange(long startTime, long endTime, boolean fullBean)
    {
        try 
        {
            MySQLSalesDaoImpl salesDao = this.getDaoFactoryObject().getSalesDao();
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();
            
            List<SalesBean> salesList = salesDao.findSalesByDateRange(startTime, endTime);   
            if (fullBean) {
                for (SalesBean salesObj : salesList)
                {
                    salesObj.setBucketList( bucketDao.findSalesBucketBySalesId(salesObj.getId()) );
                    salesObj.setLineList( lineDao.findSalesLinesBySalesId(salesObj.getId()) );
                }
            }
            
            return salesList;
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<SalesBean> getSalesByCustomer(String customerId, boolean fullBean)
    {
        try 
        {
            MySQLSalesDaoImpl salesDao = this.getDaoFactoryObject().getSalesDao();
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();
            
            List<SalesBean> salesList = salesDao.findSalesByCustomer(customerId);         
            if (fullBean) {
                for (SalesBean salesObj : salesList)
                {
                    salesObj.setBucketList( bucketDao.findSalesBucketBySalesId(salesObj.getId()) );
                    salesObj.setLineList( lineDao.findSalesLinesBySalesId(salesObj.getId()) );
                }
            }
            return salesList;
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<SalesBean> getSalesByCustomerAndDateRange(String customerId, long startTime, long endTime, boolean fullBean)
    {
        try 
        {
            MySQLSalesDaoImpl salesDao = this.getDaoFactoryObject().getSalesDao();
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();
            
            List<SalesBean> salesList = salesDao.findSalesByCustomerAndDateRange(customerId, startTime, endTime);     
            if (fullBean) {
                for (SalesBean salesObj : salesList)
                {
                    salesObj.setBucketList( bucketDao.findSalesBucketBySalesId(salesObj.getId()) );
                    salesObj.setLineList( lineDao.findSalesLinesBySalesId(salesObj.getId()) );
                }
            }
            return salesList;
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
        
    @Override
    public SalesBucketBean getSalesBucketBySalesIdAndBucketNo(String salesId, String bucketNo)
    {
        try 
        {
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();
            
            List<SalesBucketBean> bucketList = bucketDao.findSalesBucketBySalesId(salesId);
            for (SalesBucketBean bucket : bucketList)
            {
                if (bucketNo.equalsIgnoreCase( bucket.getBucketNo() ))
                    return bucket;
            }
            
            return null;
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public SalesBucketBean getBucketByNumberDateCustomer(String bucketNo, long date, String customerId)
    {
        try 
        {
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();
            
            return bucketDao.findSingleSalesBucketByCustomerBucketDate(customerId, bucketNo, date);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<SalesBucketBean> getBucketListBySalesId(String salesId) 
    {
        try 
        {
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();
            
            return bucketDao.findSalesBucketBySalesId(salesId);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    
    @Override
    public List<SalesBucketBean> getBucketListByDate(long dateTime) 
    {
        try 
        {
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();
            
            return bucketDao.findSalesBucketByDate(dateTime);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public void deleteSales(SalesBean salesBean)
    {
        try 
        {
            MySQLSalesDaoImpl salesDao = this.getDaoFactoryObject().getSalesDao();
            salesDao.deleteSales(salesBean);            
            
            // Set status 'Changed' to true
            updateCustomerChangedStatus(salesBean.getCustomerId(), true);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
        }
    }
    
    @Override
    public void deleteSalesLine(SalesLineBean lineBean)
    {
        try 
        {
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();
            lineDao.deleteSalesLine(lineBean);
            
            // Set status 'Changed' to true
            updateCustomerChangedStatus(lineBean.getCustomerId(), true);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
        }
    }
    
    @Override
    public void deleteSalesBucket(SalesBucketBean bucketBean)
    {
        try 
        {
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();
            bucketDao.deleteSalesBucket(bucketBean);

            // Set status 'Changed' to true
            updateCustomerChangedStatus(bucketBean.getCustomerId(), true);
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
        }
    }
    
    @Override
    public void deleteSalesLineByCustomerBucketDate(String customerId, String bucketNo, long dateTime)
    {
        try 
        {
            MySQLSalesLineDaoImpl lineDao = this.getDaoFactoryObject().getSalesLineDao();
            
            SalesLineBean bean = lineDao.findSingleSalesLineByCustomerBucketDate(customerId, bucketNo, dateTime);
            if (bean!=null) {
                lineDao.deleteSalesLine(bean);
                // Set status 'Changed' to true
                updateCustomerChangedStatus(customerId, true);
            }
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
        }
    }
    
    @Override
    public void deleteSalesBucketByCustomerBucketDate(String customerId, String bucketNo, long dateTime)
    {
        try 
        {
            MySQLSalesBucketDaoImpl bucketDao = this.getDaoFactoryObject().getSalesBucketDao();
            
            SalesBucketBean bean = bucketDao.findSingleSalesBucketByCustomerBucketDate(customerId, bucketNo, dateTime);
            if (bean!=null) {
                bucketDao.deleteSalesBucket(bean);
                // Set status 'Changed' to true
                updateCustomerChangedStatus(customerId, true);
            }
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
        }
    }
    
    @Override
    public void updateSalesStatus(long startDate, long endDate)
    {
        try 
        {
            MySQLSalesDaoImpl salesDao = SalesServiceHandler.this.getDaoFactoryObject().getSalesDao();
            MySQLSalesLineDaoImpl salesLineDao = SalesServiceHandler.this.getDaoFactoryObject().getSalesLineDao();

            List<SalesBean> salesList = salesDao.findSalesByStatusAndDateRange(SalesBean.STATUS_INCOMPLETE, startDate, endDate);

            for (SalesBean sales : salesList)
            {
                boolean salesLineCheck = true;

                // No invoice number yet.
                if (sales.getInvoiceNo()==null || "".equalsIgnoreCase(sales.getInvoiceNo().trim()))
                    salesLineCheck = false;

                // Invoice printed with number, but not enough payment found. (implement later)

                // Zero unit-price in SalesLine
                if ( salesLineCheck == true)
                {
                    List<SalesLineBean> salesLineList = salesLineDao.findSalesLinesBySalesId(sales.getId());
                    for (SalesLineBean salesLine : salesLineList)
                    {
                        if (salesLine.getUnitPrice() == 0.0d)
                        {
                            salesLineCheck = false;
                            break;
                        }
                    }
                }

                if (salesLineCheck == true)
                {
                    sales.setStatus(SalesBean.STATUS_COMPLETED);
                    salesDao.updateSales(sales);                   

                    // Set status 'Changed' to true
                    updateCustomerChangedStatus(sales.getCustomerId(), true);
                }
            }
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
        }
    }
    
    @Override
    public boolean isSalesLineCompleted(SalesBean salesBean)
    {
        try 
        {
            MySQLSalesDaoImpl salesDao = this.getDaoFactoryObject().getSalesDao();
            MySQLSalesLineDaoImpl salesLineDao = this.getDaoFactoryObject().getSalesLineDao();
            
            boolean salesLineCheck = true;

            if ( salesLineCheck == true)
            {
                List<SalesLineBean> salesLineList = salesLineDao.findSalesLinesBySalesId(salesBean.getId());
                for (SalesLineBean salesLine : salesLineList)
                {
                    if (salesLine.getUnitPrice() == 0.0d)
                    {
                        salesLineCheck = false;
                        break;
                    }
                }
            }

            return salesLineCheck;
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return false;
        }
    }
          
    private void updateCustomerChangedStatus(String customerId, boolean changed) {        
        try 
        {
            MySQLCustomerDaoImpl customerDao = getDaoFactoryObject().getCustomerDao();
            CustomerBean customerObj = customerDao.findSingleCustomerById(customerId);
            customerObj.setChanged(changed);
            
            customerDao.updateCustomer( customerObj );
        } 
        catch (Exception ex) 
        {
            MyLogger.logError(getClass(), "exception: " + ex.getMessage());
        }
    }
}
package com.ryanworks.fishery.client.delegate;

import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.SalesBean;
import com.ryanworks.fishery.shared.bean.SalesBucketBean;
import com.ryanworks.fishery.shared.bean.SalesLineBean;
import com.ryanworks.fishery.shared.service.ServiceLocator;
import java.util.List;
import javax.swing.SwingWorker;

public class SalesDelegate 
{
    // Singleton
    private static final SalesDelegate instance = new SalesDelegate();

    private SalesDelegate() {}

    public static SalesDelegate getInstance() 
    {
        return instance;
    }

    //--------------------------------------------------------------------------

    public int saveOrUpdateSalesLine(SalesLineBean salesLineBean)
    {
        return ServiceLocator.getSalesService().saveOrUpdateSalesLine(salesLineBean);
    }
    
    public int saveOrUpdateSalesBucket(SalesBucketBean salesBucketBean)
    {
        return ServiceLocator.getSalesService().saveOrUpdateSalesBucket(salesBucketBean);
    }
    
    public int saveOrUpdateSales(SalesBean salesBean)
    {
        ServiceLocator.getSalesService().saveOrUpdateSales(salesBean);        
        return 0;
    }
        
    public SalesBucketBean getBucketByNumberDateCustomer(String bucketNo, long date, String customerId)
    {
        return ServiceLocator.getSalesService().getBucketByNumberDateCustomer(bucketNo, date, customerId);
    }
    
    public List<SalesBucketBean> getBucketListBySalesId(String salesId)
    {
        return ServiceLocator.getSalesService().getBucketListBySalesId(salesId);
    }
    
    public List<SalesBucketBean> getBucketListByDate(long dateTime)
    {
        return ServiceLocator.getSalesService().getBucketListByDate(dateTime);
    }
    
    public int getTripNoByCustomerIdAndDate(String customerId, long dateTime)
    {
        return ServiceLocator.getSalesService().getTripNoByCustomerIdAndDate(customerId, dateTime);
    }
    
    public SalesBean getSalesById(String salesId)
    {
        return ServiceLocator.getSalesService().getSalesById(salesId);
    }
    
    public List<SalesBean> getSalesByStatusAndDateRange(int status, long startDate, long endDate, boolean fullBean)
    {
        return ServiceLocator.getSalesService().getSalesByStatusAndDateRange(status, startDate, endDate, fullBean);
    }
    
    public SalesLineBean getSalesLineById(String id)
    {
        return ServiceLocator.getSalesService().getSalesLineById(id);
    }
    
    public List<SalesLineBean> getSalesLineBySalesId(String salesId)
    {
        return ServiceLocator.getSalesService().getSalesLineBySalesId(salesId);
    }
    
    public List<SalesLineBean> getSalesLineBySalesIdAndBucketNo(String salesId, String bucketNo)
    {
        return ServiceLocator.getSalesService().getSalesLineBySalesIdAndBucketNo(salesId, bucketNo);
    }
    
    public List<SalesLineBean> getSalesLineByGroup(String salesId, String itemNewName, String bucketNo)
    {
        return ServiceLocator.getSalesService().getSalesLineByGroup(salesId, itemNewName, bucketNo);
    }
    
    public List<SalesBean> getSalesByDate(long timeInMillis, boolean fullBean)
    {
        return ServiceLocator.getSalesService().getSalesByDate(timeInMillis, fullBean);
    }
    
    public SalesBean getSalesByCustomerAndDate(String customerId, long dateTime)
    {
        return ServiceLocator.getSalesService().getSalesByCustomerAndDate(customerId, dateTime);
    }
    
    public List<SalesBean> getSalesByCustomerAndDateRange(String customerId, long startTime, long endTime, boolean fullBean)
    {
        return ServiceLocator.getSalesService().getSalesByCustomerAndDateRange(customerId, startTime, endTime, fullBean);
    }
    
    public List<SalesBean> getSalesByDateRange(long startTime, long endTime, boolean fullBean)
    {
        return ServiceLocator.getSalesService().getSalesByDateRange(startTime, endTime, fullBean);
    }
    
    public SalesBucketBean getSalesBucketBySalesIdAndBucketNo(String salesId, String bucketNo)
    {
        return ServiceLocator.getSalesService().getSalesBucketBySalesIdAndBucketNo(salesId, bucketNo);
    }
    
    public void deleteSalesByCustomer(CustomerBean customerObj)
    {    
        List<SalesBean> salesList = ServiceLocator.getSalesService().getSalesByCustomer(customerObj.getId(), true);
        for (SalesBean salesObj : salesList) {
            
            for (SalesLineBean lineObj : salesObj.getLineList()) {
                ServiceLocator.getSalesService().deleteSalesLine(lineObj);
            }
            
            for (SalesBucketBean bucketObj : salesObj.getBucketList()) {
                ServiceLocator.getSalesService().deleteSalesBucket(bucketObj);
            }
            
            ServiceLocator.getSalesService().deleteSales(salesObj);
        }
    }
    
    public void deleteSales(SalesBean salesBean)
    {
        ServiceLocator.getSalesService().deleteSales(salesBean);
    }
    
    public void deleteSalesLine(SalesLineBean lineBean)
    {
        ServiceLocator.getSalesService().deleteSalesLine(lineBean);
    }
    
    public void deleteSalesBucket(SalesBucketBean bucketBean)
    {
        ServiceLocator.getSalesService().deleteSalesBucket(bucketBean);
    }
    
    public void deleteSalesLineByCustomerBucketDate(String customerId, String bucketNo, long dateTime)
    {
        ServiceLocator.getSalesService().deleteSalesLineByCustomerBucketDate(customerId, bucketNo, dateTime);
    }
        
    public void deleteSalesBucketByCustomerBucketDate(String customerId, String bucketNo, long dateTime)
    {
        ServiceLocator.getSalesService().deleteSalesBucketByCustomerBucketDate(customerId, bucketNo, dateTime);
    }
    
    public void updateSalesStatus(long startDate, long endDate)
    {
        ServiceLocator.getSalesService().updateSalesStatus(startDate, endDate);
    }
    
    public boolean isSalesLineCompleted(SalesBean salesBean)
    {
        return ServiceLocator.getSalesService().isSalesLineCompleted( salesBean );
    }
}

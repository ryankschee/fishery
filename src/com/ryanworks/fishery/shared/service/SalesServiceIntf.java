package com.ryanworks.fishery.shared.service;

import com.ryanworks.fishery.shared.bean.SalesBean;
import com.ryanworks.fishery.shared.bean.SalesBucketBean;
import com.ryanworks.fishery.shared.bean.SalesLineBean;
import java.util.List;

public interface SalesServiceIntf 
{    
    public int saveOrUpdateSales(SalesBean salesBean);
    
    public int saveOrUpdateSalesLine(SalesLineBean salesLineBean);
    
    public int saveOrUpdateSalesBucket(SalesBucketBean salesBucketBean);
    
    public int saveSalesBucket(SalesBucketBean salesBucketBean);
    
    public int getTripNoByCustomerIdAndDate(String customerId, long dateTime);
    
    public SalesBean getSalesById(String salesId);
    
    public SalesLineBean getSalesLineById(String id);
    
    public List<SalesLineBean> getSalesLineBySalesId(String salesId);
    
    public List<SalesLineBean> getSalesLineBySalesIdAndBucketNo(String salesId, String bucketNo);
    
    public List<SalesLineBean> getSalesLineByGroup(String salesId, String itemNewName, String bucketNo);
    
    public List<SalesBean> getSalesByDate(long timeInMillis, boolean fullBean);
    
    public List<SalesBean> getSalesByStatusAndDateRange(int status, long startDate, long endDate, boolean fullBean);    
    
    public SalesBean getSalesByCustomerAndDate(String customerId, long dateTime);
    
    public List<SalesBean> getSalesByDateRange(long startTime, long endTime, boolean fullBean);
    
    public List<SalesBean> getSalesByCustomer(String customerId, boolean fullBean);
    
    public List<SalesBean> getSalesByCustomerAndDateRange(String customerId, long startTime, long endTime, boolean fullBean);
    
    public SalesBucketBean getSalesBucketBySalesIdAndBucketNo(String salesId, String bucketNo);
    
    public SalesBucketBean getBucketByNumberDateCustomer(String bucketNo, long date, String customerId);
    
    public List<SalesBucketBean> getBucketListBySalesId(String salesId);
            
    public List<SalesBucketBean> getBucketListByDate(long dateTime);
    
    public void deleteSales(SalesBean salesBean);
    
    public void deleteSalesLine(SalesLineBean lineBean);
    
    public void deleteSalesBucket(SalesBucketBean bucketBean);
    
    public void deleteSalesLineByCustomerBucketDate(String customerId, String bucketNo, long dateTime);
    
    public void deleteSalesBucketByCustomerBucketDate(String customerId, String bucketNo, long dateTime);
    
    public void updateSalesStatus(long startDate, long endDate);
    
    public boolean isSalesLineCompleted(SalesBean salesBean);
    
}

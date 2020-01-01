package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.SalesBucketBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface SalesBucketDaoIntf 
{
    public int insertSalesBucket(SalesBucketBean bean);
    public boolean updateSalesBucket(SalesBucketBean bean);
    public boolean deleteSalesBucket(SalesBucketBean bean);
    
    public SalesBucketBean findSingleSalesBucketById(String id);   
    public SalesBucketBean findSingleSalesBucketByCustomerBucketDate(String customerId, String bucketNo, long dateTime);
    public List<SalesBucketBean> findSalesBucketByDate(long timeInMillis);      
    public List<SalesBucketBean> findSalesBucketBySalesId(String salesId);
    public List<SalesBucketBean> findSalesBucketByCustomerIdAndDate(String customerId, long dateTime);
}

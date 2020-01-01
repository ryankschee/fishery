package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.ItemBean;
import com.ryanworks.fishery.shared.bean.SalesLineBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface SalesLineDaoIntf 
{
    public int insertSalesLine(SalesLineBean bean);
    public boolean updateSalesLine(SalesLineBean bean);
    public boolean deleteSalesLine(SalesLineBean bean);
    
    public SalesLineBean findSingleSalesLineById(String id);
    public SalesLineBean findSingleSalesLineByCustomerBucketDate(String customerId, String bucketNo, long dateTime);
    public List<SalesLineBean> findSalesLinesBySalesId(String salesId);
    public List<SalesLineBean> findSalesLinesBySalesIdAndBucketNo(String salesId, String bucketNo);
    public List<SalesLineBean> findSalesLinesByCustomerIdAndDate(String customerId, long date);
    public List<SalesLineBean> findSalesLinesByGroup(String salesId, String itemNewName, String bucketNo);
    
    public int updateItemName(ItemBean itemObj);
}

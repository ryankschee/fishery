package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.SalesBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface SalesDaoIntf 
{
    public int insertSales(SalesBean bean);
    public boolean updateSales(SalesBean bean);
    public boolean deleteSales(SalesBean bean);
    
    public SalesBean findSingleSalesById(String id);   
    public List<SalesBean> findSalesByCustomer(String customerId);  
    public List<SalesBean> findSalesByDate(long timeInMillis);      
    public List<SalesBean> findSalesByStatusAndDateRange(int status, long startDate, long endDate);
    public SalesBean findSalesByCustomerAndDate(String customerId, long timeInMillis);     
    public List<SalesBean> findSalesByCustomerAndDateRange(String customerId, long startTime, long endTime);   
    public List<SalesBean> findSalesByDateRange(long startTime, long endTime);     
    public int getTripNoByCustomerIdAndDate(String customerId, long dateTime);
    
    public int updateCustomer(CustomerBean customerObj);
}

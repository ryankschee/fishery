package com.ryanworks.fishery.server.dao.intf;

import com.ryanworks.fishery.shared.bean.CustomerSummaryBean;
import java.util.List;

/**
 *
 * @author Ryan Chee
 */
public interface CustomerSummaryDaoIntf 
{   
    public int insertSummary(CustomerSummaryBean bean);
    public boolean updateSummary(CustomerSummaryBean bean);
    public boolean deleteSummary(CustomerSummaryBean bean);
    
    public CustomerSummaryBean findSingleSummaryById(String id);      
    public List<CustomerSummaryBean> findSummaryByCustomer(String customerId);
    public CustomerSummaryBean findSummaryByCustomerAndDate(String customerId, int year, int month);
    public List<CustomerSummaryBean> findSummaryByCustomerBeforeDate(String customerId, long anyday);
    
    public boolean deleteSummaryByCustomer(String customerId);
}
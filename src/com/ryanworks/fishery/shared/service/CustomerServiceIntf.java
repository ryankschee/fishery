package com.ryanworks.fishery.shared.service;

import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.CustomerPaymentBean;
import com.ryanworks.fishery.shared.bean.CustomerSummaryBean;
import java.util.List;

/**
 *
 * @author Ryan
 */
public interface CustomerServiceIntf 
{
    public List<CustomerBean> getAllCustomers();
    
    public CustomerBean getCustomerByCustomerId(String customerId);
    
    public List<CustomerBean> getCustomersByDate(long dateTime);
    
    public int getTripNoByCustomerIdAndDate(String customerId, long dateTime);
    
    public List<CustomerPaymentBean> getPaymentsByCustomerAndDateRange(String customerId, long startTime, long endTime);
    
    public CustomerSummaryBean getSummaryByCustomerAndDate(String customerId, int year, int month);
    
    public void saveOrUpdateCustomer(CustomerBean customer);
    
    public void saveOrUpdatePayment(CustomerPaymentBean payment);
    
    public void saveOrUpdateSummary(CustomerSummaryBean summary);
    
    public void deleteCustomer(CustomerBean customer);
    
    public void deletePayment(CustomerPaymentBean payment);
    
    public void deleteSummary(CustomerSummaryBean summary);
    
    public void updateSummaryByMonth(String customerId);
    
    public void updateCustomerBalance(String customerId);
}
